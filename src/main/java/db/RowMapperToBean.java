package db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 仅供DBRunner使用，请勿外部使用
 */
public class RowMapperToBean<T> implements RowMapper<T> {

	/* bell(2015-1): 旧逻辑是，将每列用 rs.getObject() 读出来，然后再一个一个转为所需类型
     * 但这样遇到了个问题，rs.getObject() 对 tinyint(1) 类型的字段，有可能返回 Boolean（可通过参数 tinyInt1isBit 控制此行为）
	 *
	 * 所以修改这个行为，对于已知需要类型的情况，直接用 getInt() 等读到需要的值
	 */

    static final Map<String, RowMapperToBean<?>> _metaCache = new ConcurrentHashMap<>();
    static final Logger logger = LoggerFactory.getLogger(RowMapperToBean.class);
    final Class<T> clazz;
    final Map<String, Field> fields = new HashMap<>();
    final Map<String, Method> methods = new HashMap<>();
    private final Set<String> missFields = Collections.synchronizedSet(new HashSet<String>());

	/* ------------------------- cache ------------------------- */

    public RowMapperToBean(Class<T> type) {
        this.clazz = type;
        for (Field field : type.getFields()) {
            int mod = field.getModifiers();
            if (!Modifier.isStatic(mod) && Modifier.isPublic(mod))
                fields.put(field.getName().toLowerCase(), field);
        }
        for (Method method : type.getMethods()) {
            int mod = method.getModifiers();
            if (!Modifier.isStatic(mod) && Modifier.isPublic(mod) //
                    && method.getParameterTypes().length == 1)
                methods.put(method.getName().toLowerCase(), method);
        }
    }

    private static void fillMethod(Method method, Object bean, ResultSet rs, int col) throws Exception {
        Object val = trans(rs, col, method.getParameterTypes()[0]);
        if (val != null)
            method.invoke(bean, val);
    }

    private static void fillField(Field field, Object bean, ResultSet rs, int col) throws Exception {
        Object val = trans(rs, col, field.getType());
        if (val != null)
            field.set(bean, val);
    }

	/* ------------------------- impl ------------------------- */

    private static Object trans(ResultSet rs, int col, Class<?> type) throws SQLException {
        if (int.class.equals(type)) return rs.getInt(col);
        if (long.class.equals(type)) {
            Object sqlVal = rs.getObject(col);
            if (sqlVal instanceof Date)
                return ((Date) sqlVal).getTime();

            return rs.getLong(col);
        }
        if (boolean.class.equals(type)) return rs.getBoolean(col);
        if (double.class.equals(type)) return rs.getDouble(col);
        if (String.class.equals(type)) return rs.getString(col);
        if (Date.class.equals(type)) return rs.getTimestamp(col);

        Object sqlVal = rs.getObject(col);
        if (sqlVal == null) return null;
        if (type.isInstance(sqlVal)) return sqlVal;

        // TODO: may be other type casts
        logger.error("TYPECAST: " + sqlVal.getClass() + " -> " + type);
        return null;
    }

    public static <T> RowMapperToBean<T> of(Class<T> type) {
        @SuppressWarnings("unchecked")
        RowMapperToBean<T> cache = (RowMapperToBean<T>) _metaCache.get(type.getName());
        if (cache == null)
            _metaCache.put(type.getName(), cache = new RowMapperToBean<>(type));
        return cache;
    }

    @Override
    public T apply(ResultSet rs) throws SQLException {
        T bean;
        int col = 0;
        try {
            bean = clazz.newInstance();

            ResultSetMetaData meta = rs.getMetaData();
            int cols = meta.getColumnCount();
            for (col = 1; col <= cols; col++) {
                String key = meta.getColumnLabel(col);
                fill(bean, rs, key, col);
            }
        } catch (Exception e) {
            logger.error(Predef.f("extract fail for %s #%s", clazz, col), e);
            throw new SQLException(e);
        }
        return bean;
    }

    @SuppressWarnings("unchecked")
    private void fill(T bean, ResultSet rs, String name, int col) throws Exception {
        name = name.toLowerCase();

        // map.put("foo_bar", X)
        if (bean instanceof Map<?, ?>) {
            ((Map<String, Object>) bean).put(name, rs.getObject(col));
            return;
        }

        // call setFoo_bar(X)
        Method method1 = method("set" + name);
        if (method1 != null) {
            fillMethod(method1, bean, rs, col);
            return;
        }

        // call foo_bar(X)
        Method method2 = method(name);
        if (method2 != null) {
            fillMethod(method2, bean, rs, col);
            return;
        }

        // fill public foo_bar;
        Field field1 = field(name);
        if (field1 != null) {
            fillField(field1, bean, rs, col);
            return;
        }

        name = name.replace("_", "");

        // call setFooBar(X)
        method1 = method("set" + name);
        if (method1 != null) {
            fillMethod(method1, bean, rs, col);
            return;
        }

        // call fooBar(X)
        method2 = method(name);
        if (method2 != null) {
            fillMethod(method2, bean, rs, col);
            return;
        }

        // fill public fooBar;
        field1 = field(name);
        if (field1 != null) {
            fillField(field1, bean, rs, col);
            return;
        }

        if (!Predef.trimToEmpty(name).toLowerCase().equals("id"))
            if (missFields.add(name))
                logger.info(Predef.f("miss entry %s :: %s", clazz, name));
    }

    Field field(String name) {
        return fields.get(name);
    }

    Method method(String name) {
        return methods.get(name);
    }

}

