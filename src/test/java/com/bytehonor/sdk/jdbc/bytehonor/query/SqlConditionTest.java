package com.bytehonor.sdk.jdbc.bytehonor.query;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlConditionTest {

    private static final Logger LOG = LoggerFactory.getLogger(SqlConditionTest.class);

    @Test
    public void test() {
        SqlCondition sc = SqlCondition.like("nickname", "");
        boolean accept = SqlCondition.accept(sc) == false;
        LOG.info("test {}, {}, {}, {}", sc.getKey(), sc.getValue(), sc.getOperator(), sc.getType());
        assertTrue("test", accept);
    }

    @Test
    public void test2() {
        SqlCondition sc = SqlCondition.like("nickname", "boy");
        boolean accept = SqlCondition.accept(sc);

        LOG.info("test2 {}, {}, {}, {}", sc.getKey(), sc.getValue(), sc.getOperator(), sc.getType());
        assertTrue("test", accept);
    }
}
