package com.duowan.common.jdbc;

import com.duowan.common.jdbc.exception.JdbcException;
import com.duowan.common.jdbc.sqlbuilder.AbstractSelectBuilder;
import com.duowan.common.jdbc.statement.BatchInsertPreparedStatementSetter;
import com.duowan.common.jdbc.statement.BatchInsertStatementCallback;
import com.duowan.common.jdbc.statement.SimplePreparedStatementCreator;
import com.duowan.common.utils.AssertUtil;
import com.duowan.common.utils.ReflectUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Arvin
 */
public abstract class AbstractJdbc extends JdbcDaoSupport implements Jdbc {

    protected final Log logger = LogFactory.getLog(getClass());

    /**
     * 事务模版
     */
    protected TransactionTemplate transactionTemplate;

    @Override
    public TransactionTemplate getTransactionTemplate() {
        return transactionTemplate;
    }

    @Override
    public synchronized void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        if (null == this.transactionTemplate) {
            this.transactionTemplate = transactionTemplate;
        }
    }

    protected boolean isEmptyParams(List<Object> paramList) {
        return null == paramList || paramList.isEmpty();
    }

    @Override
    public <T> T queryByPrimaryKey(Class<T> modelType, Object... primaryKeyValues) {
        return queryByPrimaryKey(null, modelType, primaryKeyValues);
    }

    @Override
    public <T> T queryByPrimaryKey(String tableName, Class<T> modelType, Object... primaryKeyValues) {
        DBExecuteContext dbec = buildSelectByPrimaryKeyDBExecuteContext(tableName, modelType, primaryKeyValues);
        return queryModel(dbec.getSql(), modelType, dbec.getParams());
    }

    /**
     * 构造根据主键查询的执行上下文
     *
     * @param tableName        数据库表名称
     * @param requireType      模型对象
     * @param primaryKeyValues 主键值
     * @param <T>              模型类型
     * @return 返回执行上下文
     */
    protected abstract <T> DBExecuteContext buildSelectByPrimaryKeyDBExecuteContext(String tableName, Class<T> requireType, Object[] primaryKeyValues);

    @Override
    public <T> T queryModel(String sql, Class<T> requireType, Object... params) {
        try {
            return getJdbcTemplate().queryForObject(sql, new BeanPropertyRowMapper<>(requireType), params);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public <T> T queryModel(String sql, List<Object> paramList, Class<T> requireType) {
        try {
            BeanPropertyRowMapper<T> rowMapper = new BeanPropertyRowMapper<>(requireType);
            if (isEmptyParams(paramList)) {
                return getJdbcTemplate().queryForObject(sql, rowMapper);
            }
            return getJdbcTemplate().queryForObject(sql, rowMapper, paramList.toArray());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public <T> T queryModel(String sql, RowMapper<T> rowMapper, Class<T> requireType, Object... params) {
        try {
            return getJdbcTemplate().queryForObject(sql, rowMapper, params);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public <T> T queryModel(String sql, List<Object> paramList, Class<T> requireType, RowMapper<T> rowMapper) {
        try {
            if (isEmptyParams(paramList)) {
                return getJdbcTemplate().queryForObject(sql, rowMapper);
            }
            return getJdbcTemplate().queryForObject(sql, rowMapper, paramList.toArray());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Map<String, Object> queryMap(String sql, Object... params) {
        try {
            return getJdbcTemplate().queryForMap(sql, params);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Map<String, Object> queryMap(List<Object> paramList, String sql) {
        try {
            if (isEmptyParams(paramList)) {
                return getJdbcTemplate().queryForMap(sql);
            }
            return getJdbcTemplate().queryForMap(sql, paramList.toArray());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public <T> List<T> queryModelList(String sql, Class<T> requireType, Object... params) {
        return getJdbcTemplate().query(sql, new BeanPropertyRowMapper<>(requireType), params);
    }

    @Override
    public <T> List<T> queryModelList(String sql, List<Object> paramList, Class<T> requireType) {
        BeanPropertyRowMapper<T> rowMapper = new BeanPropertyRowMapper<>(requireType);
        if (isEmptyParams(paramList)) {
            return getJdbcTemplate().query(sql, rowMapper);
        }
        return getJdbcTemplate().query(sql, rowMapper, paramList.toArray());
    }

    @Override
    public <T> List<T> queryModelList(String sql, RowMapper<T> rowMapper, Class<T> requireType, Object... params) {
        return getJdbcTemplate().query(sql, rowMapper, params);
    }

    @Override
    public <T> List<T> queryModelList(String sql, List<Object> paramList, Class<T> requireType, RowMapper<T> rowMapper) {
        if (isEmptyParams(paramList)) {
            return getJdbcTemplate().query(sql, rowMapper);
        }
        return getJdbcTemplate().query(sql, rowMapper, paramList.toArray());
    }

    @Override
    public List<Map<String, Object>> queryMapList(String sql, Object... params) {
        return getJdbcTemplate().queryForList(sql, params);
    }

    @Override
    public List<Map<String, Object>> queryMapList(List<Object> paramList, String sql) {
        if (isEmptyParams(paramList)) {
            return getJdbcTemplate().queryForList(sql);
        }
        return getJdbcTemplate().queryForList(sql, paramList.toArray());
    }

    @Override
    public Integer queryInteger(String sql, Integer defaultValue, Object... params) {
        return querySingleColumn(sql, defaultValue, Integer.class, params);
    }

    @Override
    public Long queryLong(String sql, Long defaultValue, Object... params) {
        return querySingleColumn(sql, defaultValue, Long.class, params);
    }

    @Override
    public String queryString(String sql, String defaultValue, Object... params) {
        return querySingleColumn(sql, defaultValue, String.class, params);
    }

    @Override
    public <T> T querySingleColumn(String sql, T defaultValue, Class<T> requireType, Object... params) {
        try {
            T ret = getJdbcTemplate().queryForObject(sql, new SingleColumnRowMapper<>(requireType), params);
            return null == ret ? defaultValue : ret;
        } catch (EmptyResultDataAccessException e) {
            return defaultValue;
        }

    }

    @Override
    public <T> T querySingleColumn(List<Object> paramList, T defaultValue, Class<T> requireType, String sql) {
        T ret;
        try {
            if (isEmptyParams(paramList)) {
                ret = getJdbcTemplate().queryForObject(sql, new SingleColumnRowMapper<>(requireType));
            } else {
                ret = getJdbcTemplate().queryForObject(sql, new SingleColumnRowMapper<>(requireType), paramList.toArray());
            }
        } catch (EmptyResultDataAccessException e) {
            return defaultValue;
        }
        return ret == null ? defaultValue : ret;
    }

    @Override
    public <T> List<T> querySingleColumnList(String sql, Class<T> requireType, Object... params) {
        return getJdbcTemplate().query(sql, new SingleColumnRowMapper<>(requireType), params);
    }

    @Override
    public <T> List<T> querySingleColumnList(List<Object> paramList, Class<T> requireType, String sql) {
        if (isEmptyParams(paramList)) {
            return getJdbcTemplate().query(sql, new SingleColumnRowMapper<>(requireType));
        }
        return getJdbcTemplate().query(sql, new SingleColumnRowMapper<>(requireType), paramList.toArray());
    }

    @Override
    public List<String> queryStringList(String sql, Object... params) {
        return querySingleColumnList(sql, String.class, params);
    }

    @Override
    public List<Integer> queryIntegerList(String sql, Object... params) {
        return querySingleColumnList(sql, Integer.class, params);
    }

    @Override
    public List<Long> queryLongList(String sql, Object... params) {
        return querySingleColumnList(sql, Long.class, params);
    }

    @Override
    public <T> Page<T> queryPage(String sql, Class<T> requireType, int pageNo, int pageSize, Object... params) {
        // 查询数量的 sql
        String countSql = buildCountSql(sql);
        int count = querySingleColumn(countSql, 0, Integer.class, params);
        Page<T> page = new Page<>(pageNo, pageSize);
        page.setCount(count);
        if (count < 1) {
            page.setDataList(new ArrayList<T>());
            return page;
        }

        String pageSql = buildPagingSql(sql, pageNo, pageSize);
        List<T> dateList = queryModelList(pageSql, requireType, params);
        page.setDataList(dateList);
        return page;
    }

    protected abstract String buildPagingSql(String sql, int pageNo, int pageSize);

    protected abstract String buildCountSql(String sql);

    @Override
    public <T> Page<T> queryPage(String sql, Class<T> requireType, List<Object> paramList, int pageNo, int pageSize) {

        if (isEmptyParams(paramList)) {
            return queryPage(sql, requireType, pageNo, pageSize);
        }

        return queryPage(sql, requireType, pageNo, pageSize, paramList.toArray());
    }

    @Override
    public <T> Page<T> queryPage(Class<T> requiredType, Object queryCondition, int pageNo, int pageSize, String tableName, SqlBuilderConfigurer<AbstractSelectBuilder<?>> configurer) {
        AbstractSelectBuilder selectBuilder = createSelectBuilder(requiredType, queryCondition);

        if (StringUtils.isNotBlank(tableName)) {
            selectBuilder.customTableName(tableName);
        }

        if (configurer != null) {
            configurer.configure(selectBuilder);
        }

        String selectSql = selectBuilder.getSelectSql();

        return queryPage(selectSql, requiredType, pageNo, pageSize, selectBuilder.getParams());
    }

    @Override
    public <T> Page<T> queryPage(Class<T> requiredType, Object queryCondition, int pageNo, int pageSize, String tableName) {
        return queryPage(requiredType, queryCondition, pageNo, pageSize, tableName, null);
    }

    @Override
    public <T> Page<T> queryPage(Class<T> requiredType, Object queryCondition, int pageNo, int pageSize) {
        return queryPage(requiredType, queryCondition, pageNo, pageSize, null, null);
    }

    @Override
    public <T> List<T> queryModelList(Class<T> requiredType, Object queryCondition, int pageNo, int pageSize, String tableName, SqlBuilderConfigurer<AbstractSelectBuilder<?>> configurer) {
        AbstractSelectBuilder selectBuilder = createSelectBuilder(requiredType, queryCondition);

        if (JdbcHelper.isValidPagingParams(pageNo, pageSize)) {
            selectBuilder.limit(pageNo, pageSize);
        }

        if (StringUtils.isNotBlank(tableName)) {
            selectBuilder.customTableName(tableName);
        }

        if (configurer != null) {
            configurer.configure(selectBuilder);
        }

        String selectSql = selectBuilder.getSelectSql();

        return queryModelList(selectSql, requiredType, selectBuilder.getParams());
    }

    @Override
    public <T> List<T> queryModelList(Class<T> requiredType, Object queryCondition, int pageNo, int pageSize, String tableName) {
        return queryModelList(requiredType, queryCondition, pageNo, pageSize, tableName, null);
    }

    @Override
    public <T> List<T> queryModelList(Class<T> requiredType, Object queryCondition, int pageNo, int pageSize) {
        return queryModelList(requiredType, queryCondition, pageNo, pageSize, null, null);
    }

    protected abstract <T> AbstractSelectBuilder createSelectBuilder(Class<T> requiredType, Object queryCondition);

    // Insert implement

    @Override
    public int insert(String sql, Object... params) {
        return getJdbcTemplate().update(sql, params);
    }

    @Override
    public int insert(List<Object> params, String sql) {
        if (isEmptyParams(params)) {
            return getJdbcTemplate().update(sql);
        }
        return getJdbcTemplate().update(sql, params.toArray());
    }

    @Override
    public int insert(String sql, GenerateKeyCallback keyCallback, Object... params) {

        PreparedStatementSetter preparedStatementSetter = null;
        if (params != null && params.length > 0) {
            preparedStatementSetter = new ArgumentPreparedStatementSetter(params);
        }

        PreparedStatementCreator preparedStatementCreator = new SimplePreparedStatementCreator(sql, true, preparedStatementSetter);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int ret = getJdbcTemplate().update(preparedStatementCreator, keyHolder);

        if (keyCallback != null) {
            keyCallback.call(1, keyHolder.getKey());
        }

        return ret;
    }

    @Override
    public int insert(String sql, List<Object> params, GenerateKeyCallback keyCallback) {
        if (isEmptyParams(params)) {
            return insert(sql, keyCallback);
        }
        return insert(sql, keyCallback, params.toArray());
    }

    protected int intArrayToSum(int[] arr) {
        int total = 0;
        for (int r : arr) {
            total += r;
        }
        return total;
    }

    @Override
    public int batchInsert(String sql, List<Object[]> paramsList) {

        AssertUtil.assertNotEmpty(paramsList, "参数列表不能为空！");
        return intArrayToSum(getJdbcTemplate().batchUpdate(sql, paramsList));
    }

    private List<Object[]> listParamsToArrayParams(List<List<Object>> paramsList) {
        AssertUtil.assertNotEmpty(paramsList, "参数列表不能为空！");
        List<Object[]> realParamsList = new ArrayList<>();
        for (List<Object> paramList : paramsList) {
            realParamsList.add(paramList.toArray());
        }
        return realParamsList;
    }

    @Override
    public int batchInsert(List<List<Object>> paramsList, String sql) {
        return batchInsert(sql, listParamsToArrayParams(paramsList));
    }

    @Override
    public int batchInsert(final String sql, List<Object[]> paramsList, GenerateKeyCallback keyCallback) {
        BatchInsertPreparedStatementSetter pss = new BatchInsertPreparedStatementSetter(paramsList);
        BatchInsertStatementCallback bsc = new BatchInsertStatementCallback(pss, keyCallback);

        return getJdbcTemplate().execute(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                return con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            }
        }, bsc);
    }

    @Override
    public int batchInsert(List<List<Object>> paramsList, String sql, GenerateKeyCallback keyCallback) {
        return batchInsert(sql, listParamsToArrayParams(paramsList), keyCallback);
    }

    @Override
    public int insert(Object model) {
        return insert(null, model, false);
    }

    @Override
    public int insert(String tableName, Object model) {
        return insert(tableName, model, false);
    }

    @Override
    public int insert(final Object model, boolean fillAutoIncrementKey) {
        return insert(null, model, fillAutoIncrementKey);
    }

    @Override
    public int insert(final String tableName, final Object model, boolean fillAutoIncrementKey) {
        DBExecuteContext dbec = buildInsertDBExecuteContext(tableName, model);
        String sql = dbec.getSql();
        final Object[] params = dbec.getParams();
        if (fillAutoIncrementKey) {
            FieldDef autoIncrementFieldDef = MDContext.getMD(model.getClass()).getAutoIncrementFieldDef();
            if (autoIncrementFieldDef != null) {
                // 设置自增主键
                GenerateKeyCallback callback = new GenerateKeyCallback() {
                    @Override
                    public void call(int rowIndex, Object primaryKey) {
                        try {
                            FieldDef autoIncrementFieldDef = MDContext.getMD(model.getClass()).getAutoIncrementFieldDef();
                            ReflectUtil.setFieldValue(model, autoIncrementFieldDef.getField(), primaryKey);
                        } catch (Exception e) {
                            String message = "自增主键设置失败： " + e.getMessage();
                            logger.warn(message, e);
                            throw new JdbcException(message, e);
                        }
                    }
                };
                return insert(sql, callback, params);
            }
        }
        return insert(sql, params);
    }

    /**
     * 构造 Insert Builder
     *
     * @param tableName 指定数据库表名称
     * @param model     模型对象
     * @return 返回 数据库sql执行上下文对象
     */
    protected abstract DBExecuteContext buildInsertDBExecuteContext(String tableName, Object model);

    // update implementation


    @Override
    public int updateByPrimaryKey(Object model) {

        return updateByPrimaryKey(null, model);
    }

    @Override
    public int updateByPrimaryKey(String tableName, Object model) {
        DBExecuteContext dbec = buildUpdateByPrimaryKeyDBExecuteContext(tableName, model);

        return update(dbec.getSql(), dbec.getParams());
    }

    /**
     * 构造根据主键更新模型的sql执行上下文
     *
     * @param tableName 指定数据库表名称
     * @param model     要更新的模型对象
     * @return 返回sql执行上下文
     */
    protected abstract DBExecuteContext buildUpdateByPrimaryKeyDBExecuteContext(String tableName, Object model);

    @Override
    public int update(String sql, Object... params) {
        return getJdbcTemplate().update(sql, params);
    }

    @Override
    public int update(List<Object> paramList, String sql) {
        if (isEmptyParams(paramList)) {
            return getJdbcTemplate().update(sql);
        }
        return getJdbcTemplate().update(sql, paramList.toArray());
    }

    @Override
    public int batchUpdate(String sql, List<Object[]> paramsList) {
        return intArrayToSum(getJdbcTemplate().batchUpdate(sql, paramsList));
    }

    @Override
    public int batchUpdate(List<List<Object>> paramsList, String sql) {
        return batchUpdate(sql, listParamsToArrayParams(paramsList));
    }

    @Override
    public int updateLimit(String sql, int limit, Object... params) {
        return getJdbcTemplate().update(buildUpdateLimitSql(sql, 1, limit), params);
    }

    @Override
    public int updateLimit(List<Object> paramList, String sql, int limit) {
        if (isEmptyParams(paramList)) {
            return getJdbcTemplate().update(buildUpdateLimitSql(sql, 1, limit));
        }
        return getJdbcTemplate().update(buildUpdateLimitSql(sql, 1, limit), paramList.toArray());
    }

    // delete implementation


    @Override
    public <T> int deleteByPrimaryKey(Class<T> modelType, Object... primaryKeyValues) {
        return deleteByPrimaryKey(null, modelType, primaryKeyValues);
    }

    @Override
    public <T> int deleteByPrimaryKey(String tableName, Class<T> modelType, Object... primaryKeyValues) {
        DBExecuteContext dbec = buildDeleteByPrimaryKeyDBExecuteContext(tableName, modelType, primaryKeyValues);
        return update(dbec.getSql(), dbec.getParams());
    }

    @Override
    public int deleteByPrimaryKey(Object model) {
        return deleteByPrimaryKey(null, model);
    }

    @Override
    public int deleteByPrimaryKey(String tableName, Object model) {
        DBExecuteContext dbec = buildDeleteByPrimaryKeyDBExecuteContext(tableName, model);
        return update(dbec.getSql(), dbec.getParams());
    }

    /**
     * 构造根据主键删除记录的sql执行上下文
     *
     * @param tableName        指定数据库表名称
     * @param modelType        模型类型
     * @param primaryKeyValues 主键值列表
     * @param <T>              模型类型
     * @return 返回sql执行上下文
     */
    protected abstract <T> DBExecuteContext buildDeleteByPrimaryKeyDBExecuteContext(String tableName, Class<T> modelType, Object[] primaryKeyValues);

    /**
     * 构造根据主键删除记录的sql执行上下文
     *
     * @param tableName 指定数据库表名称
     * @param model     模型对象
     * @return 返回sql执行上下文
     */
    protected abstract DBExecuteContext buildDeleteByPrimaryKeyDBExecuteContext(String tableName, Object model);

    protected abstract String buildUpdateLimitSql(String sql, int startPageNo, int limit);
}
