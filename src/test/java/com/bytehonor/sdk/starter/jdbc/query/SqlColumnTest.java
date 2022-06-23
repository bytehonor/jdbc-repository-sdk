package com.bytehonor.sdk.starter.jdbc.query;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlColumnTest {

    private static final Logger LOG = LoggerFactory.getLogger(SqlColumnTest.class);

    @Test
    public void test() {
        SqlColumn sc = SqlColumn.like("nickname", "");
        boolean accept = SqlColumn.accept(sc) == false;
        LOG.info("test {}, {}, {}, {}", sc.getKey(), sc.getValue(), sc.getOperator(), sc.getType());
        assertTrue("test", accept);
    }

    @Test
    public void test2() {
        SqlColumn sc = SqlColumn.like("nickname", "boy");
        boolean accept = SqlColumn.accept(sc);

        LOG.info("test2 {}, {}, {}, {}", sc.getKey(), sc.getValue(), sc.getOperator(), sc.getType());
        assertTrue("test", accept);
    }
}
