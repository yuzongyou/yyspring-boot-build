package com.duowan.common.jdbc;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于 jsqlparser 实现的 count sql 构造器（参考 pagehelper 插件： https://gitee.com/free/Mybatis_PageHelper/blob/master/src/main/java/com/github/pagehelper/parser/CountSqlParser.java）
 *
 * @author Arvin
 */
public class CountSqlParser {

    public static final String KEEP_ORDER_BY = "/*keep orderby*/";
    private static final Alias TABLE_ALIAS;

    static {
        TABLE_ALIAS = new Alias("table_count");
        TABLE_ALIAS.setUseAs(false);
    }

    /**
     * 获取智能的countSql
     *
     * @param sql 要处理的sql
     * @return 返回处理后的countSql
     */
    public String getSmartCountSql(String sql) {
        return getSmartCountSql(sql, "0");
    }

    /**
     * 获取智能的countSql
     *
     * @param sql        要处理的sql
     * @param columnName 列名，默认 0
     * @return 返回处理后的 countSql
     */
    public String getSmartCountSql(String sql, String columnName) {
        //解析SQL
        Statement stmt = null;
        //特殊sql不需要去掉order by时，使用注释前缀
        if (sql.contains(KEEP_ORDER_BY)) {
            return getSimpleCountSql(sql);
        }
        try {
            stmt = CCJSqlParserUtil.parse(sql);
        } catch (Exception e) {
            //无法解析的用一般方法返回count语句
            return getSimpleCountSql(sql);
        }
        Select select = (Select) stmt;
        SelectBody selectBody = select.getSelectBody();
        try {
            //处理body-去order by
            processSelectBody(selectBody);
        } catch (Exception e) {
            //当 sql 包含 group by 时，不去除 order by
            return getSimpleCountSql(sql);
        }
        //处理with-去order by
        processWithItemsList(select.getWithItemsList());
        //处理为count查询
        sqlToCount(select, columnName);
        return select.toString();
    }

    /**
     * 获取普通的Count-sql
     *
     * @param sql 原查询sql
     * @return 返回count查询sql
     */
    public String getSimpleCountSql(final String sql) {
        return getSimpleCountSql(sql, "0");
    }

    /**
     * 获取普通的Count-sql
     *
     * @param sql        原查询sql
     * @param columnName 列名
     * @return 返回count查询sql
     */
    public String getSimpleCountSql(final String sql, String columnName) {
        return "select count(" + columnName + ") from (" + sql + ") tmp_count";
    }

    /**
     * 将sql转换为count查询
     *
     * @param select select对象
     * @param name   列名称
     */
    public void sqlToCount(Select select, String name) {
        SelectBody selectBody = select.getSelectBody();
        // 是否能简化count查询
        List<SelectItem> countItem = new ArrayList<>();
        countItem.add(new SelectExpressionItem(new Column("count(" + name + ")")));
        if (selectBody instanceof PlainSelect && isSimpleCount((PlainSelect) selectBody)) {
            ((PlainSelect) selectBody).setSelectItems(countItem);
        } else {
            PlainSelect plainSelect = new PlainSelect();
            SubSelect subSelect = new SubSelect();
            subSelect.setSelectBody(selectBody);
            subSelect.setAlias(TABLE_ALIAS);
            plainSelect.setFromItem(subSelect);
            plainSelect.setSelectItems(countItem);
            select.setSelectBody(plainSelect);
        }
    }

    /**
     * 是否可以用简单的count查询方式
     *
     * @param select select 对象
     * @return 返回是否简单的countsql
     */
    public boolean isSimpleCount(PlainSelect select) {
        //包含group by的时候不可以
        if (select.getGroupByColumnReferences() != null) {
            return false;
        }
        //包含distinct的时候不可以
        if (select.getDistinct() != null) {
            return false;
        }
        for (SelectItem item : select.getSelectItems()) {
            //select列中包含参数的时候不可以，否则会引起参数个数错误
            if (item.toString().contains("?")) {
                return false;
            }
            //如果查询列中包含函数，也不可以，函数可能会聚合列
            if (item instanceof SelectExpressionItem && ((SelectExpressionItem) item).getExpression() instanceof Function) {
                return false;
            }
        }
        return true;
    }

    /**
     * 处理selectBody去除Order by
     *
     * @param selectBody selectBody 对象
     */
    public void processSelectBody(SelectBody selectBody) {
        if (selectBody instanceof PlainSelect) {
            processPlainSelect((PlainSelect) selectBody);
        } else if (selectBody instanceof WithItem) {
            WithItem withItem = (WithItem) selectBody;
            if (withItem.getSelectBody() != null) {
                processSelectBody(withItem.getSelectBody());
            }
        } else {
            SetOperationList operationList = (SetOperationList) selectBody;
            if (operationList.getSelects() != null && !operationList.getSelects().isEmpty()) {
                List<SelectBody> plainSelects = operationList.getSelects();
                for (SelectBody plainSelect : plainSelects) {
                    processSelectBody(plainSelect);
                }
            }
            if (!orderByHashParameters(operationList.getOrderByElements())) {
                operationList.setOrderByElements(null);
            }
        }
    }

    /**
     * 处理PlainSelect类型的selectBody
     *
     * @param plainSelect plainSelect 对象
     */
    public void processPlainSelect(PlainSelect plainSelect) {
        if (!orderByHashParameters(plainSelect.getOrderByElements())) {
            plainSelect.setOrderByElements(null);
        }
        if (plainSelect.getFromItem() != null) {
            processFromItem(plainSelect.getFromItem());
        }
        if (plainSelect.getJoins() != null && !plainSelect.getJoins().isEmpty()) {
            List<Join> joins = plainSelect.getJoins();
            for (Join join : joins) {
                if (join.getRightItem() != null) {
                    processFromItem(join.getRightItem());
                }
            }
        }
    }

    /**
     * 处理WithItem
     *
     * @param withItemsList 要处理的列表
     */
    public void processWithItemsList(List<WithItem> withItemsList) {
        if (withItemsList != null && !withItemsList.isEmpty()) {
            for (WithItem item : withItemsList) {
                processSelectBody(item.getSelectBody());
            }
        }
    }

    /**
     * 处理子查询
     *
     * @param fromItem 要处理的项
     */
    public void processFromItem(FromItem fromItem) {
        if (fromItem instanceof SubJoin) {
            processFromSubJoinItem((SubJoin) fromItem);
        } else if (fromItem instanceof SubSelect) {
            processFromSubSelectItem((SubSelect) fromItem);
        } else if (fromItem instanceof LateralSubSelect) {
            processFromLateralSubSelectItem((LateralSubSelect) fromItem);
        }
        //Table时不用处理
    }

    private void processFromLateralSubSelectItem(LateralSubSelect lateralSubSelect) {
        if (lateralSubSelect.getSubSelect() != null) {
            SubSelect subSelect = lateralSubSelect.getSubSelect();
            if (subSelect.getSelectBody() != null) {
                processSelectBody(subSelect.getSelectBody());
            }
        }
    }

    private void processFromSubSelectItem(SubSelect subSelect) {
        if (subSelect.getSelectBody() != null) {
            processSelectBody(subSelect.getSelectBody());
        }
    }

    private void processFromSubJoinItem(SubJoin subJoin) {
        List<Join> joinList = subJoin.getJoinList();
        for (Join join : joinList) {
            if (join.getRightItem() != null) {
                processFromItem(join.getRightItem());
            }
        }
        if (subJoin.getLeft() != null) {
            processFromItem(subJoin.getLeft());
        }
    }

    /**
     * 判断Orderby是否包含参数，有参数的不能去
     *
     * @param orderByElements 要检查的参数列表
     * @return 是否包含参数
     */
    public boolean orderByHashParameters(List<OrderByElement> orderByElements) {
        if (orderByElements == null) {
            return false;
        }
        for (OrderByElement orderByElement : orderByElements) {
            if (orderByElement.toString().contains("?")) {
                return true;
            }
        }
        return false;
    }
}
