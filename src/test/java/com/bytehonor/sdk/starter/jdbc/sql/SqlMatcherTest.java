package com.bytehonor.sdk.starter.jdbc.sql;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlMatcherTest {

    private static final Logger LOG = LoggerFactory.getLogger(SqlMatcherTest.class);

    @Test
    public void test() {
        SqlMatcher sc = SqlMatcher.like("nickname", "");
        boolean accept = SqlMatcher.accept(sc) == false;
        LOG.info("test {}, {}, {}, {}, {}", sc.getKey(), sc.getValue(), sc.getOperator(), sc.getSqlType(),
                sc.getJavaType());
        assertTrue("test", accept);
    }

    @Test
    public void test2() {
        SqlMatcher sc = SqlMatcher.like("nickname", "boy");
        boolean accept = SqlMatcher.accept(sc);

        LOG.info("test2 {}, {}, {}, {}, {}", sc.getKey(), sc.getValue(), sc.getOperator(), sc.getSqlType(),
                sc.getJavaType());
        assertTrue("test", accept);
    }
}
