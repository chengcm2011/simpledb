package db;

import com.cheng.lang.model.*;

import java.io.Serializable;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class SQLParameter implements Serializable {

    static final long serialVersionUID = 1118941587227355811L;
    private List paramList = new ArrayList();

    /**
     * 默认构造函数
     */
    public SQLParameter() {
    }

    /**
     * 加入一个Null参数类型
     *
     * @param type 参数的类型参考java.sql.Types
     */
    public void addNullParam(int type) {

    }


    /**
     * 加入一个任意对象参数，注意该参数不能为空
     *
     * @param param 对象参数
     */
    public void addParam(Object param) {
        if (param == null)
            throw new IllegalArgumentException("SQL参数对象不能为空，可用NullType对象代替");
        paramList.add(param);

    }

    /**
     * 添加一个UFDouble类型数组
     *
     * @param param
     */
    public void addParam(UFDouble param) {
        if (param == null) {
            addNullParam(Types.DECIMAL);
        } else {
            paramList.add(param);
        }
    }

    /**
     * 加入一个整型参数
     *
     * @param param
     */
    public void addParam(Integer param) {
        if (param == null) {
            addNullParam(Types.INTEGER);
        } else {
            paramList.add(param);
        }
    }

    /**
     * 加入一个字符串类型参数
     *
     * @param param
     */
    public void addParam(String param) {
        if (param == null) {
            addNullParam(Types.VARCHAR);
        } else if (param.equals("")) {
            addNullParam(Types.VARCHAR);
        } else {
            paramList.add(param);
        }
    }

    /**
     * 加入一个UFBoolean类型参数
     *
     * @param param
     */
    public void addParam(UFBoolean param) {
        if (param == null) {
            addNullParam(Types.VARCHAR);
        } else {
            paramList.add(param);
        }
    }

    /**
     * 加入一个UFDate类型参数
     *
     * @param param
     */
    public void addParam(UFDate param) {
        if (param == null) {
            addNullParam(Types.VARCHAR);
        } else {
            paramList.add(param);
        }
    }

    public void addParam(UFTime param) {
        if (param == null) {
            addNullParam(Types.VARCHAR);
        } else {
            paramList.add(param);
        }
    }

    /**
     * 加入一个UFDateTime类型参数
     *
     * @param param
     */
    public void addParam(UFDateTime param) {
        if (param == null) {
            addNullParam(Types.VARCHAR);
        } else {
            paramList.add(param);
        }
    }

    /**
     * 加入一个整数类型参数
     *
     * @param param
     */
    public void addParam(int param) {
        paramList.add(new Integer(param));
    }

    /**
     * 加入一个长整数类型参数
     *
     * @param param
     */
    public void addParam(long param) {
        paramList.add(new Long(param));
    }

    /**
     * 加入一个双精度类型参数
     *
     * @param param
     */
    public void addParam(double param) {
        paramList.add(new Double(param));
    }

    /**
     * 加入一个布尔类型参数
     *
     * @param param
     */
    public void addParam(boolean param) {
        paramList.add(Boolean.valueOf(param));
    }

    /**
     * 加入一个浮点类型参数
     *
     * @param param
     */
    public void addParam(float param) {
        paramList.add(new Float(param));
    }

    /**
     * 加入一个短整数类型参数
     *
     * @param param
     */
    public void addParam(short param) {
        paramList.add(new Short(param));
    }

    /**
     * 根据索引得到参数对象
     *
     * @param index 参数的顺序索引
     * @return
     */
    public Object get(int index) {
        return paramList.get(index);
    }

    /**
     * 参数的替换，用来保持参数对象的原始信息，而不用重新构造参数对象.
     *
     * @param index 要替换对象的索引从0开始记数
     * @param param
     */
    public void replace(int index, Object param) {
        if (param == null)
            throw new IllegalArgumentException("SQL参数对象不能为空，可用NullType对象代替");
        paramList.remove(index);
        paramList.add(index, param);
    }


    /**
     * 清除所有参数
     */
    public void clearParams() {
        paramList.clear();
    }

    /**
     * 得到参数的个数
     *
     * @return 返回参数的个数
     */
    public int getCountParams() {
        return paramList.size();
    }

    /**
     * 得到所有参数集合
     *
     * @return 返回参数的集合
     */
    public List getParameters() {
        return paramList;
    }

    public String toString() {
        return "SQLParameter--" + paramList + "";
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        final SQLParameter that = (SQLParameter) o;

        return !(paramList != null ? !paramList.equals(that.paramList) : that.paramList != null);

    }

    public int hashCode() {
        return (paramList != null ? paramList.hashCode() : 0);
    }

    public Object[] toObjectArray() {
        if (paramList != null) {
            return paramList.toArray(new Object[]{});
        }
        return new Object[]{};
    }
}
