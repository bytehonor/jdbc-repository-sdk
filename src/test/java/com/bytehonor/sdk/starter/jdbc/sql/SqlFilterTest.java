package com.bytehonor.sdk.starter.jdbc.sql;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlFilterTest {

    private static final Logger LOG = LoggerFactory.getLogger(SqlFilterTest.class);

    @Test
    public void test() {
        SqlFilter sc = SqlFilter.like("nickname", "");
        boolean accept = SqlFilter.accept(sc) == false;
        LOG.info("test {}, {}, {}, {}, {}", sc.getKey(), sc.getValue(), sc.getOperator(), sc.getSqlType(),
                sc.getJavaType());
        assertTrue("test", accept);
    }

    @Test
    public void test2() {
        SqlFilter sc = SqlFilter.like("nickname", "boy");
        boolean accept = SqlFilter.accept(sc);

        LOG.info("test2 {}, {}, {}, {}, {}", sc.getKey(), sc.getValue(), sc.getOperator(), sc.getSqlType(),
                sc.getJavaType());
        assertTrue("test", accept);
    }
}
