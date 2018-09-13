package com.duowan.common.jdbc.dialect;

import com.duowan.common.jdbc.exception.JdbcException;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

/**
 * Mysql 数据库
 *
 * @author Arvin
 */
public class MysqlDialect extends AbstractDialect {

    private static final String END_WIDTH_WITH_LIMIT_REGEX = "(?i).*limit\\s+[0-9]+\\s*(,\\s*[0-9]+)?\\s*$";

    /**
     * 关键字
     */
    private static final Set<String> KEYS_WORDS = new HashSet<>();

    static {
        ClassPathResource resource = new ClassPathResource("/META-INF/mysql-keyword.txt");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                if (!"".equals(line.trim())) {
                    KEYS_WORDS.add(line.trim().toLowerCase());
                    KEYS_WORDS.add(line.trim().toUpperCase());
                }
            }
        } catch (IOException e) {
            throw new JdbcException("解析Mysql关键字异常！");
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new JdbcException("解析Mysql关键字关闭异常！");
                }
            }
        }
    }

    @Override
    public String getPagingSql(String sql, int pageNo, int pageSize) {

        if (sql.matches(END_WIDTH_WITH_LIMIT_REGEX)) {
            return sql;
        }

        StringBuilder sqlBuilder = new StringBuilder(sql);
        int offset = getStartRow(pageNo, pageSize);
        if (offset == 0) {
            sqlBuilder.append(" LIMIT ").append(pageSize).append(" ");
        } else {
            sqlBuilder.append(" LIMIT ").append(offset).append(",").append(pageSize).append(" ");
        }
        return sqlBuilder.toString();
    }

    @Override
    public String getUpdateLimitSql(String sql, int startPageNo, int limit) {
        if (limit < 1) {
            return sql;
        }
        startPageNo = startPageNo < 1 ? 1 : startPageNo;
        if (!sql.matches(END_WIDTH_WITH_LIMIT_REGEX)) {
            return getPagingSql(sql, startPageNo, limit);
        }
        return sql;
    }


    public boolean isKeyWorld(String word) {
        return KEYS_WORDS.contains(word);
    }

    public static final String DEFAULT_DB_WRAP_OPEN = "`";
    public static final String DEFAULT_DB_WRAP_CLOSE = "`";

    /**
     * 解包装数据库字段
     *
     * @param dbAttrName 数据库属性名称
     * @param prefix     前缀
     * @param suffix     后缀
     * @return 去掉包装后的字段
     */
    public static String unWrapDbAttrName(String dbAttrName, String prefix, String suffix) {

        while (dbAttrName.startsWith(prefix)) {
            dbAttrName = dbAttrName.substring(prefix.length());
        }

        while (dbAttrName.endsWith(suffix)) {
            dbAttrName = dbAttrName.substring(0, dbAttrName.length() - suffix.length());
        }

        return dbAttrName;
    }

    /**
     * 关键字列表： https://dev.mysql.com/doc/refman/5.7/en/keywords.html
     *
     * @param columnName 列名称
     * @return 包装后的字段
     */
    @Override
    public String wrapColumnName(String columnName) {
        return DEFAULT_DB_WRAP_OPEN + unWrapDbAttrName(columnName, DEFAULT_DB_WRAP_OPEN, DEFAULT_DB_WRAP_CLOSE) + DEFAULT_DB_WRAP_CLOSE;
    }

    @Override
    public String wrapTableName(String tableName) {
        return wrapColumnName(tableName);
    }
}
