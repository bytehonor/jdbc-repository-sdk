package com.bytehonor.sdk.server.bytehonor.jdbc;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.jdbc.bytehonor.jdbc.SqlInjectUtils;

public class SqlInjectUtilsTest {

    private static final Logger LOG = LoggerFactory.getLogger(SqlInjectUtilsTest.class);

    @Test
    public void testEscape() {
        String src = "';drop table tbl_other;--";
        String esc = SqlInjectUtils.escape(src);
        LOG.info("escape src:{}, len:{}", src, src.length());
        LOG.info("escape esc:{}, len:{}", esc, esc.length());
        assertTrue("testEscape", esc.length() == (src.length() + 3));
    }

    @Test
    public void testColumn() {
        String src = "';drop table tbl_other;--";
        String esc = SqlInjectUtils.column(src);
        LOG.info("column esc:{}", esc);
        assertTrue("testColumn", esc.length() == (src.length() - 2));
    }
}
