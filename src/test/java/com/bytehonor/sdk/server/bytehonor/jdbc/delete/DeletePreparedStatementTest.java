package com.bytehonor.sdk.server.bytehonor.jdbc.delete;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.jdbc.bytehonor.jdbc.delete.DeleteCondition;
import com.bytehonor.sdk.jdbc.bytehonor.jdbc.delete.DeletePreparedStatement;
import com.bytehonor.sdk.jdbc.bytehonor.query.MatchColumn;

public class DeletePreparedStatementTest {
    private static final Logger LOG = LoggerFactory.getLogger(DeletePreparedStatementTest.class);

    @Test
    public void test() {
        DeletePreparedStatement statement = DeletePreparedStatement.create("tbl_user");
        statement.match(MatchColumn.eq("name", "john"));
        statement.match(MatchColumn.egt("create_at", System.currentTimeMillis()));
        statement.setLimit(1);

        LOG.info("sql:{}", statement.toDeleteRealSql());
        LOG.info("sql:{}", statement.toDeleteLogicSql());
        List<Object> args = statement.listArgs();
        LOG.info("args:{}", args);
        assertTrue("test", args.size() == 2);
    }

    @Test
    public void test2() {
        DeleteCondition condition = DeleteCondition.create();
        condition.eq("name", "john").egt("create_at", System.currentTimeMillis());
        condition.setLimit(1);
        DeletePreparedStatement statement = DeletePreparedStatement.create("tbl_user", condition);

        LOG.info("sql:{}", statement.toDeleteRealSql());
        LOG.info("sql:{}", statement.toDeleteLogicSql());
        List<Object> args = statement.listArgs();
        LOG.info("args:{}", args);
        assertTrue("test2", args.size() == 2);
    }

}
