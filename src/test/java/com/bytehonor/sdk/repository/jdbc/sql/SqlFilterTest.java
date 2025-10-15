package com.bytehonor.sdk.repository.jdbc.sql;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.repository.jdbc.sql.SqlFilter.SqlFilterColumn;

public class SqlFilterTest {

    private static final Logger LOG = LoggerFactory.getLogger(SqlFilterTest.class);

    @Test
    public void test() {
        SqlFilterColumn sc = SqlFilterColumn.like("nickname", "");
        boolean accept = SqlFilterColumn.accept(sc) == false;
        LOG.info("test {}, {}, {}, {}, {}", sc.getKey(), sc.getValue(), sc.getOperator(), sc.getSqlType(),
                sc.getJavaType());
        assertTrue("test", accept);
    }

    @Test
    public void test2() {
        SqlFilterColumn sc = SqlFilterColumn.like("nickname", "boy");
        boolean accept = SqlFilterColumn.accept(sc);

        LOG.info("test2 {}, {}, {}, {}, {}", sc.getKey(), sc.getValue(), sc.getOperator(), sc.getSqlType(),
                sc.getJavaType());
        assertTrue("test", accept);
    }
}
