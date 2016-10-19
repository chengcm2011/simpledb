package db;


import com.cheng.lang.IService;
import com.cheng.lang.PageVO;
import com.cheng.lang.exception.BusinessException;
import com.cheng.lang.model.SuperModel;

import java.util.List;
import java.util.Map;

public interface IDataBaseService extends IService {

    public String insert(final SuperModel vo) throws BusinessException;

    public String[] insert(final List<? extends SuperModel> vos) throws BusinessException;

    public String[] insert(final SuperModel vo[]) throws BusinessException;

    public int update(final SuperModel vo) throws BusinessException;

    public int update(final SuperModel vo, String[] fields) throws BusinessException;

    public int update(final List<? extends SuperModel> vos) throws BusinessException;

    public int update(final List<? extends SuperModel> vos, String[] fields) throws BusinessException;

    public int update(final SuperModel[] superVOs, String[] fields) throws BusinessException;

    public int update(String sql) throws BusinessException;

    public int update(String sql, SQLParameter sqlParameter) throws BusinessException;

    public int deleteByPK(Class className, String pk) throws BusinessException;

    public <T> T queryByPK(Class<T> className, String pk) throws BusinessException;

    public <T> T queryByPK(Class<T> className, String pk, String[] selectedFields) throws BusinessException;

    public PageVO queryByPage(Class className, PageVO pagevo) throws BusinessException;

    public PageVO queryByPage(String[] sqls, PageVO pageVO) throws BusinessException;

    public <T> T queryOneByClause(Class<T> className, String condition) throws BusinessException;

    public <T> T queryOneByClause(Class<T> className, String condition, SQLParameter parameter) throws BusinessException;

    public <T> T queryOneByClause(Class<T> className, String condition, String[] fields) throws BusinessException;

    public <T> T queryOneByClause(Class<T> className, String condition, SQLParameter parameter, String[] fields) throws BusinessException;

    public <T> List<T> queryByClause(Class<T> className, String condition) throws BusinessException;

    public <T> List<T> queryByClause(Class<T> className, String condition, String[] fields) throws BusinessException;

    public <T> List<T> queryByClause(Class<T> className, String condition, SQLParameter sqlParameter) throws BusinessException;

    public <T> List<T> queryByClause(Class<T> className, String condition, String[] fields, SQLParameter param) throws BusinessException;

    public <T> List<T> queryAll(Class<T> className) throws BusinessException;

//    public Object queryBySql(String sql, SQLParameter parameter, ResultSetProcessor processor) throws BusinessException;
//
//    public Object queryBySql(String sql, ResultSetProcessor processor) throws BusinessException;

    public Map<String, Object> queryMap(String sql, Object... params) throws BusinessException;

    public List<Map<String, Object>> queryMapList(String sql, Object... params) throws BusinessException;

    public PageVO queryMapListByPage(String[] sqls, PageVO pageVO) throws BusinessException;

    public boolean isTableExist(String tablename);

    public boolean isTableColmnExist(String tablename, String colmnName);

}
