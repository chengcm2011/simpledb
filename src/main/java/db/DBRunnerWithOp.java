package db;

import com.cheng.lang.ClassUtil;
import com.cheng.lang.PageVO;
import com.cheng.lang.model.SuperModel;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.AbstractListHandler;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;


public class DBRunnerWithOp extends QueryRunner {

	/* ------------------------- init ------------------------- */

    public DBRunnerWithOp(DataSource ds, boolean closeConn) {
        super(ds, closeConn);
    }

	/* ------------------------- shortcuts - single row ------------------------- */

    public int queryInt(String sql, Object... params) throws SQLException {
        return query(sql, DBRunnerHandlers.singleIntHandler, params);
    }

    public long queryLong(String sql, Object... params) throws SQLException {
        return query(sql, DBRunnerHandlers.singleLongHandler, params);
    }

    public double queryDouble(String sql, Object... params) throws SQLException {
        return query(sql, DBRunnerHandlers.singleDoubleHandler, params);
    }

    public String queryString(String sql, Object... params) throws SQLException {
        return query(sql, DBRunnerHandlers.singleStringHandler, params);
    }

    public Timestamp queryTimestamp(String sql, Object... params) throws SQLException {
        return query(sql, DBRunnerHandlers.singleTimestampHandler, params);
    }



    public <T> T queryBean(Class<T> type, String condition) throws SQLException {
        SuperModel t = (SuperModel) ClassUtil.initClass(type);
        String sql = createQuerySql(t.getTableName(), null, condition);
        return queryOne(RowMapperToBean.of(type), sql, null);
    }

    public <T> T queryBean(Class<T> type, String condition, Object... params) throws SQLException {
        SuperModel t = (SuperModel) ClassUtil.initClass(type);
        String sql = createQuerySql(t.getTableName(), null, condition);
        return queryOne(RowMapperToBean.of(type), sql, params);
    }

    public <T> T queryBean(Class<T> type, String condition, String orderbycondition, Object... params) throws SQLException {
        SuperModel t = (SuperModel) ClassUtil.initClass(type);
        String sql = createQuerySql(t.getTableName(), null, condition,orderbycondition);
        return queryOne(RowMapperToBean.of(type), sql, params);
    }

    public <T> T queryBeanById(Class<T> type, Integer id) throws SQLException {
        SuperModel t = (SuperModel) ClassUtil.initClass(type);
        String sql = createQuerySql(t.getTableName(), null, t.getPKFieldName() + "=? ");
        return queryOne(RowMapperToBean.of(type), sql, id);
    }


    public <T> T queryBean(Class<T> type, String[] selectfield, String condition) throws SQLException {
        return queryBean(type, selectfield, condition, "");
    }

    public <T> T queryBean(Class<T> type, String[] selectfield, String condition, String orderbycondition) throws SQLException {
        SuperModel t = (SuperModel) ClassUtil.initClass(type);
        String sql = createQuerySql(t.getTableName(), selectfield, condition, orderbycondition);
        return queryBean(type, sql, null);
    }

    public <T> T queryBean(Class<T> type, String[] selectfield, String condition, Object... params) throws SQLException {
        SuperModel t = (SuperModel) ClassUtil.initClass(type);
        String sql = createQuerySql(t.getTableName(), selectfield, condition);
        return queryBean(type, sql, params);
    }
    public <T> T queryBean(Class<T> type, String[] selectfield, String condition,String orderbycondition, Object... params) throws SQLException {
        SuperModel t = (SuperModel) ClassUtil.initClass(type);
        String sql = createQuerySql(t.getTableName(), selectfield, condition,orderbycondition);
        return queryBean(type, sql, params);
    }


    public PageVO queryBeanByPage(Class type, PageVO pageVO) throws SQLException {
        String[] sql = buildSql(type, pageVO);
        int i = queryInt(sql[1]);
        Collection com =  queryBeans(type, sql[0], null);
        pageVO.setTotalCount(i);
        pageVO.setData(com);
        return pageVO;
    }


    private String createQuerySql(String tablename, String[] selectfield, String condition) {
        return createQuerySql(tablename, selectfield, condition, null);
    }

    private String createQuerySql(String tablename, String[] selectfield, String condition, String orderbycondition) {
        StringBuffer stringBuffer = new StringBuffer("select");
        if (selectfield == null || selectfield.length == 0) {
            stringBuffer.append(" * ");
        } else {
            stringBuffer.append(" ");
            for (String pc : selectfield) {
                stringBuffer.append(pc).append(",");
            }
            stringBuffer.setLength(stringBuffer.length() - 1);
        }
        stringBuffer.append(" from ").append(tablename);
        if (StringUtils.isNotBlank(condition)) {
            stringBuffer.append(" where ").append(condition);
        }
        if (StringUtils.isNotBlank(orderbycondition)) {
            stringBuffer.append(" order by ").append(orderbycondition);
        }
        return stringBuffer.toString();
    }

    public Map<String, Object> queryMap(String sql, Object... params) throws SQLException {
        return queryOne(RowMapperToDbHelperMap.instance, sql, params);
    }

    public Map<String, Object> queryRawMap(String sql, Object... params) throws SQLException {
        return queryOne(RowMapperToRawMap.instance, sql, params);
    }

    public Map<String, String> queryStringMap(String sql, Object... params) throws SQLException {
        return queryOne(RowMapperToStringMap.instance, sql, params);
    }

    public <T> T queryOne(final RowMapper<T> mapper, String sql, Object... params) throws SQLException {
        return query(sql, new ResultSetHandler<T>() {
            public T handle(ResultSet rs) throws SQLException {
                return rs.next() ? mapper.apply(rs) : null;
            }
        }, params);
    }

	/* ------------------------- shortcuts - multi rows ------------------------- */

    public List<Integer> queryIntList(String sql, Object... params) throws SQLException {
        return query(sql, DBRunnerHandlers.intListHandler, params);
    }

    public List<Long> queryLongList(String sql, Object... params) throws SQLException {
        return query(sql, DBRunnerHandlers.longListHandler, params);
    }

    public List<String> queryStringList(String sql, Object... params) throws SQLException {
        return query(sql, DBRunnerHandlers.stringListHandler, params);
    }

    public <T> List<T> queryBeans2(Class<T> type, String condition) throws SQLException {
        SuperModel t = (SuperModel) ClassUtil.initClass(type);
        String sql = createQuerySql(t.getTableName(), null, condition);
        return queryBeans(type, sql, null);
    }

    public <T> List<T> queryBeans2(Class<T> type, String condition, Object... params) throws SQLException {
        SuperModel t = (SuperModel) ClassUtil.initClass(type);
        String sql = createQuerySql(t.getTableName(), null, condition);
        return queryBeans(type, sql, params);
    }

    public <T> List<T> queryBeans2(Class<T> type, String[] selectfield, String condition) throws SQLException {
        SuperModel t = (SuperModel) ClassUtil.initClass(type);
        String sql = createQuerySql(t.getTableName(), selectfield, condition);
        return queryBeans(type, sql, null);
    }

    public <T> List<T> queryBeans2(Class<T> type, String[] selectfield, String condition, Object... params) throws SQLException {
        SuperModel t = (SuperModel) ClassUtil.initClass(type);
        String sql = createQuerySql(t.getTableName(), selectfield, condition);
        return queryBeans(type, sql, params);
    }

    private <T> List<T> queryBeans(Class<T> type, String sql, Object... params) throws SQLException {
        return queryList(RowMapperToBean.of(type), sql, params);
    }

    public List<Map<String, Object>> queryMapList(String sql, Object... params) throws SQLException {
        return queryList(RowMapperToDbHelperMap.instance, sql, params);
    }

    public List<Map<String, Object>> queryRawMapList(String sql, Object... params) throws SQLException {
        return queryList(RowMapperToRawMap.instance, sql, params);
    }

    public List<Map<String, String>> queryStringMapList(String sql, Object... params) throws SQLException {
        return queryList(RowMapperToStringMap.instance, sql, params);
    }

    public <T> List<T> queryList(final RowMapper<T> mapper, String sql, Object... params) throws SQLException {
        return query(sql, new AbstractListHandler<T>() {
            protected T handleRow(ResultSet rs) throws SQLException {
                return mapper.apply(rs);
            }
        }, params);
    }

    public PageVO queryMapListByPage(String[] sqls, PageVO pageVO) throws SQLException {
        List<Map<String, Object>> data = queryList(RowMapperToRawMap.instance, sqls[0], null);
        int i = queryInt(sqls[1]);
        pageVO.setTotalCount(i);
        pageVO.setData(data);
        return pageVO;
    }


	/* ------------------------- misc ------------------------- */

    public int insertReturnInt(String sql, Object... params) throws SQLException {
        return super.insert(sql, DBRunnerHandlers.singleIntHandler, params);
    }

    public long insertReturnLong(String sql, Object... params) throws SQLException {
        return super.insert(sql, DBRunnerHandlers.singleLongHandler, params);
    }


	public int update(SuperModel superModel) throws SQLException {
		List<Object> valobj = new ArrayList<>();
		List<String> keys = new ArrayList<>();
		Object kval = 0 ;
		Map<String,Object> item = BeanUtil.getValueMap(superModel);
		Iterator iter = item.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			if (!key.equals(superModel.getPKFieldName())) {
				keys.add(key.toString());
				Object val = entry.getValue();
				valobj.add(val);
			}else{
				kval = entry.getValue();
			}
		}
		valobj.add(kval);
		String sql = createUpdateSql(superModel,keys) ;
		return super.update(sql, valobj.toArray(new Object[0]));
	}

    	public int update(SuperModel superModel,String[] updatefield) throws SQLException {
		List<Object> valobj = new ArrayList<>();
		List<String> keys = new ArrayList<>();
		Map<String,Object> item = BeanUtil.getValueMap(superModel);
		for(int i=0 ;i<updatefield.length;i++){
			if(item.containsKey(updatefield[i])){
				if (!updatefield[i].equals(superModel.getPKFieldName())) {
					keys.add(updatefield[i]);
					Object val = item.get(updatefield[i]);
					valobj.add(val);
				}
			}
		}
		valobj.add(item.get(superModel.getPKFieldName()));
		String sql = createUpdateSql(superModel,keys) ;
		return super.update(sql, valobj.toArray(new Object[0]));
	}
    public synchronized int dump(String sql, Object... params) throws SQLException {
        return query(sql, new ResultSetHandler<Integer>() {
            @Override
            public Integer handle(ResultSet rs) throws SQLException {
                ResultSetMetaData meta = rs.getMetaData();
                int len = meta.getColumnCount();
                PrintStream out = System.out;

                int rows = 0;
                while (rs.next()) {
                    // bell(2014-6): 无输出则不print thead
                    if (rows == 0) {
                        for (int i = 1; i <= len; i++) {
                            if (i > 1)
                                out.print(" \t ");
                            out.print(meta.getColumnLabel(i));
                        }
                        out.println();
                    }

                    rows++;

                    for (int i = 1; i <= len; i++) {
                        if (i > 1)
                            out.print(" \t ");
                        out.print(rs.getObject(i));
                    }
                    out.println();
                }
                return rows;
            }
        }, params);
    }

    private String createInsertSql(SuperModel superModel, List<String> keys) {
        StringBuffer stringBuffer = new StringBuffer(" INSERT INTO `").append(superModel.getTableName()).append("` (");
        for (int i = 0; i < keys.size(); i++) {
            stringBuffer.append(keys.get(i) + " ,");
        }
        stringBuffer.setLength(stringBuffer.length() - 1);
        stringBuffer.append(") values (");
        for (int i = 0; i < keys.size(); i++) {
            stringBuffer.append(" ?,");
        }
        stringBuffer.setLength(stringBuffer.length() - 1);
        stringBuffer.append(")");
        return stringBuffer.toString();
    }

    private String createUpdateSql(SuperModel superModel, List<String> keys) {
        StringBuffer stringBuffer = new StringBuffer("update ");
        stringBuffer.append(superModel.getTableName());
        stringBuffer.append(" set ");
        for (int i = 0; i < keys.size(); i++) {
            String field = keys.get(i);
            stringBuffer.append(field + "= ?").append(" ,");
        }
        stringBuffer.setLength(stringBuffer.length() - 1);
        stringBuffer.append("where ").append(superModel.getPKFieldName()).append("=?");
        return stringBuffer.toString();
    }

    private String[] buildSql(Class className, PageVO pagevo) {
        String[] sqls = new String[2];
        SuperModel vo = (SuperModel) ClassUtil.initClass(className);
        String condition = pagevo.getCondition();
        String[] fields = pagevo.getSelectedFields();
        StringBuffer buffer = new StringBuffer();
        StringBuffer countbuffer = new StringBuffer();
        String tableName = vo.getTableName();
        if (fields == null) {
            buffer.append("SELECT * FROM ").append(tableName);
            countbuffer.append("SELECT count(" + vo.getPKFieldName() + ") total FROM ").append(tableName);
        } else {
            buffer.append("SELECT ");
            for (int i = 0; i < fields.length; i++) {
                buffer.append(fields[i]).append(",");
            }
            buffer.setLength(buffer.length() - 1);
            buffer.append(" FROM ").append(tableName);
        }
        if (condition != null && condition.length() != 0) {
            if (condition.toUpperCase().trim().startsWith("ORDER ")) {
                buffer.append(" ").append(condition);
                countbuffer.append(" ").append(condition);
            } else {
                buffer.append(" WHERE ").append(condition);
                countbuffer.append(" WHERE ").append(condition);
            }
        }
        sqls[1] = countbuffer.toString();
        buffer.append(" limit " + pagevo.getStartIndex() + "," + pagevo.getPageSize());
        sqls[0] = buffer.toString();

        return sqls;
    }

}
