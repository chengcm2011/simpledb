package db;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * bell(2013-8): 常用工具方法小集合，旨在弥补Java语法的不足
 * <p/>
 * 1. 建议import static使用，并修改IDE配置中的import合并规则为：2条import static就合并为.*
 * Eclipse："Java / Code Style / Organize Imports / Number of static imports needed for .*" = 1
 * IntelliJ："Code Style / Java / Imports / Names count to use static import with *" = 1
 * <p/>
 * 2. 建议在IDE中配置模板，通过快捷方式输入"import static arch.util.lang.Predef.*;"
 * Intellij："Live Templates"
 */
public final class Predef {

	/* ------------------------- string ------------------------- */

    // escape
    public static final String utf8 = "UTF-8";
    public static final String gbk = "GB18030";
    public static final Charset charsetUtf8 = Charset.forName(utf8);
    public static final Charset charsetGbk = Charset.forName(gbk);
    public static final Random rand = new Random();
    static final Logger logger = LoggerFactory.getLogger("");

    // format
    public static String f(String format, Object... args) {
        return String.format(format, args);
    }

    // with empty
    public static String trimToEmpty(String s) {
        return s == null ? "" : s.trim();
    }

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static boolean isNotEmpty(String s) {
        return s != null && s.length() > 0;
    }

    public static boolean isEmpty(Object obj) {
        return obj == null || obj instanceof String && ((String) obj).trim().isEmpty() || obj instanceof Collection && isEmpty((Collection) ((Collection) obj)) || obj instanceof Map && isEmpty((Map) ((Map) obj)) || obj instanceof Object[] && ((Object[]) ((Object[]) obj)).length == 0;
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty((Object) obj);
    }

    // with num
    public static int toInt(String s) {
        return NumberUtils.toInt(s, -1);
    }

    public static int toInt(String s, int def) {
        return NumberUtils.toInt(s, def);
    }

    public static int toInt(Object o) {
        return NumberUtils.toInt(toStr(o), -1);
    }

    public static int toInt(Object o, int def) {
        return NumberUtils.toInt(toStr(o), def);
    }

    public static long toLong(String s) {
        return NumberUtils.toLong(s, -1);
    }

    public static long toLong(String s, long def) {
        return NumberUtils.toLong(s, def);
    }
    // bell(2013-8): 默认值设为-1的考虑：确实有少数情况0不是默认值，或需要区分无效数据。使用-1作为无效值至少无害

    public static long toLong(Object o) {
        return NumberUtils.toLong(toStr(o), -1);
    }

    public static long toLong(Object o, long def) {
        return NumberUtils.toLong(toStr(o), def);
    }

    public static double toDouble(String s) {
        return NumberUtils.toDouble(s, -1);
    }

    public static double toDouble(String s, double def) {
        return NumberUtils.toDouble(s, def);
    }

    public static double toDouble(Object o) {
        return NumberUtils.toDouble(toStr(o), -1);
    }

    public static double toDouble(Object o, double def) {
        return NumberUtils.toDouble(toStr(o), def);
    }

    public static String toStr(boolean v) {
        return Boolean.toString(v);
    }

    public static String toStr(int v) {
        return Integer.toString(v);
    }

    public static String toStr(long v) {
        return Long.toString(v);
    }

    public static String toStr(double v) {
        return Double.toString(v);
    }

    public static String toStr(Object o) {
        return o == null ? "" : String.valueOf(o);
    }

    // with regex
    public static String find(Pattern regex, String s) {
        Matcher m = regex.matcher(s);
        return m.find() ? m.group(m.groupCount() > 0 ? 1 : 0) : null;
    }

    public static int findInt(Pattern regex, String s) {
        String i = find(regex, s);
        return isEmpty(i) ? -1 : toInt(i);
    }

	/* ------------------------- arrays ------------------------- */

    public static double findDouble(Pattern regex, String s) {
        String i = find(regex, s);
        return isEmpty(i) ? -1 : toDouble(i);
    }

    // misc
    public static String join(Iterable<?> iterable, String separator) {
        return StringUtils.join(iterable, separator);
    }

    public static boolean isEmpty(long[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean isNotEmpty(long[] arr) {
        return arr != null && arr.length > 0;
    }

    public static boolean isEmpty(byte[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean isNotEmpty(byte[] arr) {
        return arr != null && arr.length > 0;
    }

	/* ------------------------- collection ------------------------- */

    public static boolean isEmpty(char[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean isNotEmpty(char[] arr) {
        return arr != null && arr.length > 0;
    }

    // builder
    @SafeVarargs
    public static <T> List<T> asList(T... a) {
        return Arrays.asList(a);
    }

    @SuppressWarnings("unchecked")
    public static <T> Map<String, T> asMap(Object... args) {
        if (args.length % 2 != 0)
            throw new IllegalArgumentException("args.length = " + args.length);

        Map<String, T> map = new LinkedHashMap<>();
        for (int i = 0; i < args.length - 1; i += 2)
            map.put(String.valueOf(args[i]), (T) args[i + 1]);
        return map;
    }

    public static Map<String, String> asStringMap(Object... args) {
        if (args.length % 2 != 0)
            throw new IllegalArgumentException("args.length = " + args.length);

        Map<String, String> map = new LinkedHashMap<>();
        for (int i = 0; i < args.length - 1; i += 2)
            map.put(String.valueOf(args[i]), args[i + 1] == null ? null : String.valueOf(args[i + 1]));
        return map;
    }

    // with empty
    public static int size(Collection<?> coll) {
        return coll != null ? coll.size() : 0;
    }

    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> coll) {
        return coll != null && !coll.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return map != null && !map.isEmpty();
    }

    public static <T> List<T> emptyList() {
        return Collections.emptyList();
    }

	/* ------------------------- log ------------------------- */

    public static <K, V> Map<K, V> emptyMap() {
        return Collections.emptyMap();
    }

    public static <T> List<T> nonEmpty(List<T> list) {
        return list != null ? list : Collections.<T>emptyList();
    }

    public static void p(String s) {
        logger.info(s);
    }

    public static void p(String format, Object... args) {
        logger.info(f(format, args));
    }

    // log for scripts or debug.
    public static void p(Object o) {
        if (o == null) {
            logger.info("(null)");
        } else if (o instanceof Collection) {
            Collection<?> list = (Collection<?>) o;
            p("list size: %s", list.size());
            for (Object item : list)
                p("+ %s", item);
        } else if (o instanceof Map<?, ?>) {
            Map<?, ?> map = (Map<?, ?>) o;

            List<Object> keys = new ArrayList<>(map.keySet());
            Collections.sort(keys, new Comparator<Object>() {
                public int compare(Object o1, Object o2) {
                    String k1 = String.valueOf(o1);
                    String k2 = String.valueOf(o2);
                    return k1.compareTo(k2);
                }
            });

            p("map size: %s", map.size());
            for (Object key : keys)
                p("+ %s\t -> %s", key, map.get(key));
        } else if (o.getClass().isArray()) {
            if (o instanceof boolean[]) {
                p(Arrays.toString((boolean[]) o));
            } else if (o instanceof byte[]) {
                p(Arrays.toString((byte[]) o));
            } else if (o instanceof char[]) {
                p(Arrays.toString((char[]) o));
            } else if (o instanceof int[]) {
                p(Arrays.toString((int[]) o));
            } else if (o instanceof long[]) {
                p(Arrays.toString((long[]) o));
            } else if (o instanceof float[]) {
                p(Arrays.toString((float[]) o));
            } else if (o instanceof double[]) {
                p(Arrays.toString((double[]) o));
            } else {
                p(Arrays.toString((Object[]) o));
            }
        } else {
            p(String.valueOf(o));
        }
    }

	/* ------------------------- math ------------------------- */

    public static void pHr() {
        p("------------------------------------------------------------");
    }

    public static int max(int a, int b) {
        return Math.max(a, b);
    }

    public static double max(double a, double b) {
        return Math.max(a, b);
    }

    public static int min(int a, int b) {
        return Math.min(a, b);
    }

    public static double min(double a, double b) {
        return Math.min(a, b);
    }

    public static double random() {
        return Math.random();
    }

    public static int randInt(int n) {
        return rand.nextInt(n);
    }

	/* ------------------------- misc ------------------------- */

    public static void sleep(long millis) {
        try {
            if (millis > 0)
                Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

}
