package com.bytehonor.sdk.server.bytehonor.jdbc.select;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.jdbc.bytehonor.jdbc.select.SelectPreparedStatement;
import com.bytehonor.sdk.jdbc.bytehonor.query.QueryCondition;
import com.bytehonor.sdk.jdbc.bytehonor.query.SqlOrder;

public class SelectPreparedStatementTest {

    private static final Logger LOG = LoggerFactory.getLogger(SelectPreparedStatementTest.class);

    @Test
    public void test() {
        QueryCondition condition = QueryCondition.create(0, 10).eq("name", "john';drop table tbl_other;--")
                .orderBy(SqlOrder.descOf("age"));
        SelectPreparedStatement statement = SelectPreparedStatement.create("tbl_user", "id", "id,name,age", condition);

        LOG.info("sql:{}", statement.toSelectSql());
        LOG.info("count:{}", statement.toCountSql());
        List<Object> args = statement.listArgs();
        int[] typesArr = statement.types();
        LOG.info("args:{}, types:{}", args, typesArr);
        assertTrue("test", args.size() == 1);
    }

}
