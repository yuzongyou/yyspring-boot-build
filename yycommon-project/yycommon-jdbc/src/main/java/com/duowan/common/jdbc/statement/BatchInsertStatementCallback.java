package com.duowan.common.jdbc.statement;

import com.duowan.common.jdbc.GenerateKeyCallback;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.InterruptibleBatchPreparedStatementSetter;
import org.springframework.jdbc.core.ParameterDisposer;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Arvin
 */
public class BatchInsertStatementCallback implements PreparedStatementCallback<Integer> {

    /**
     * 参数设置，不能为空
     */
    private final BatchPreparedStatementSetter pss;

    /**
     * 主键回调函数，可以为空
     */
    private final GenerateKeyCallback generateKeyCallback;

    public BatchInsertStatementCallback(BatchPreparedStatementSetter pss) {
        this(pss, null);
    }

    public BatchInsertStatementCallback(BatchPreparedStatementSetter pss, GenerateKeyCallback generateKeyCallback) {
        this.pss = pss;
        this.generateKeyCallback = generateKeyCallback;
    }

    /**
     * 返回影响行数
     *
     * @param ps 执行语句
     * @return 返回处理条数
     * @throws SQLException        sql异常
     * @throws DataAccessException 数据访问异常
     */
    @Override
    public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
        try {
            int total = 0;
            int batchSize = pss.getBatchSize();
            InterruptibleBatchPreparedStatementSetter ipss =
                    (pss instanceof InterruptibleBatchPreparedStatementSetter ?
                            (InterruptibleBatchPreparedStatementSetter) pss : null);

            if (JdbcUtils.supportsBatchUpdates(ps.getConnection())) {
                total = executeForSupportBatchInsert(ps, batchSize, ipss);
            } else {
                total = executeForUnSupportBatchInsert(ps, total, batchSize, ipss);
            }

            return total;
        } finally {
            if (pss instanceof ParameterDisposer) {
                ((ParameterDisposer) pss).cleanupParameters();
            }
        }
    }

    private int executeForUnSupportBatchInsert(PreparedStatement ps, int total, int batchSize, InterruptibleBatchPreparedStatementSetter ipss) throws SQLException {
        List<Integer> rowsAffected = new ArrayList<>();
        for (int i = 0; i < batchSize; i++) {
            pss.setValues(ps, i);
            if (ipss != null && ipss.isBatchExhausted(i)) {
                break;
            }
            rowsAffected.add(ps.executeUpdate());

            // 设置主键
            if (generateKeyCallback != null) {
                ResultSet keyRs = ps.getGeneratedKeys();
                if (keyRs != null) {
                    if (keyRs.next()) {
                        generateKeyCallback.call(i + 1, keyRs.getObject(1));
                    }
                }
            }
        }

        int rowSize = rowsAffected.size();
        for (int i = 0; i < rowSize; i++) {
            total += rowsAffected.get(i);
        }
        return total;
    }

    private int executeForSupportBatchInsert(PreparedStatement ps, int batchSize, InterruptibleBatchPreparedStatementSetter ipss) throws SQLException {
        int total;
        for (int i = 0; i < batchSize; i++) {
            pss.setValues(ps, i);
            if (ipss != null && ipss.isBatchExhausted(i)) {
                break;
            }
            ps.addBatch();
        }
        total = intArraySum(ps.executeBatch());

        // 设置主键
        if (generateKeyCallback != null) {
            ResultSet keyRs = ps.getGeneratedKeys();
            if (keyRs != null) {
                int i = 1;
                while (keyRs.next()) {
                    generateKeyCallback.call(i++, keyRs.getObject(1));
                }
            }
        }
        return total;
    }

    private int intArraySum(int[] intArr) {
        int total = 0;
        for (int i : intArr) {
            total += i;
        }
        return total;
    }
}
