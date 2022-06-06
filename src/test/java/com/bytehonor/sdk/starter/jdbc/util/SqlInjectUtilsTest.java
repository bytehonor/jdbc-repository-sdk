package com.bytehonor.sdk.starter.jdbc.util;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @Test
    public void testLike() {
        String src = "boy";
        String like = SqlInjectUtils.like(src, true, true);
        boolean l1 = "%boy%".equals(like);
        LOG.info("like:{}", like);

        String likeLeft = SqlInjectUtils.like(src, true, false);
        boolean l2 = "%boy".equals(likeLeft);
        LOG.info("likeLeft:{}", likeLeft);

        String likeRight = SqlInjectUtils.like(src, false, true);
        boolean l3 = "boy%".equals(likeRight);
        LOG.info("likeRight:{}", likeRight);

        assertTrue("testLike", l1 && l2 && l3);
    }

    @Test
    public void testLike2() {
        String src = "%boy%";
        String like = SqlInjectUtils.like(src, true, true);
        boolean l1 = src.equals(like);
        LOG.info("testLike2 like:{}", like);

        String likeLeft = SqlInjectUtils.like(src, true, false);
        boolean l2 = src.equals(likeLeft);
        LOG.info("testLike2 likeLeft:{}", likeLeft);

        String likeRight = SqlInjectUtils.like(src, false, true);
        boolean l3 = src.equals(likeRight);
        LOG.info("testLike2 likeRight:{}", likeRight);

        assertTrue("testLike2", l1 && l2 && l3);
    }
}
