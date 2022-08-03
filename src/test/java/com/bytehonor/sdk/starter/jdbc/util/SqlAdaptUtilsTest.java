package com.bytehonor.sdk.starter.jdbc.util;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.lang.spring.constant.JavaValueTypes;

public class SqlAdaptUtilsTest {

    private static final Logger LOG = LoggerFactory.getLogger(SqlAdaptUtilsTest.class);

    @Test
    public void testJoinCollection() {
        Set<Integer> set = new HashSet<Integer>();
        set.add(1);
        set.add(2);
        set.add(3);

        String src = SqlAdaptUtils.joinCollection(JavaValueTypes.INTEGER, set);
        LOG.info("testJoinCollection:{}", src);

        assertTrue("testJoinCollection", "1,2,3".equals(src));
    }

    @Test
    public void testJoinCollection2() {
        Set<Long> set = new HashSet<Long>();
        set.add(1L);
        set.add(2L);
        set.add(3L);

        String src = SqlAdaptUtils.joinCollection(JavaValueTypes.LONG, set);
        LOG.info("testJoinCollection2:{}", src);

        assertTrue("testJoinCollection2", "1,2,3".equals(src));
    }

    @Test
    public void testJoinCollection21() {
        List<Long> set = new ArrayList<Long>();
        set.add(1L);
        set.add(2L);
        set.add(3L);

        String src = SqlAdaptUtils.joinCollection(JavaValueTypes.LONG, set);
        LOG.info("testJoinCollection21:{}", src);

        assertTrue("testJoinCollection21", "1,2,3".equals(src));
    }

    @Test
    public void testJoinCollection3() {
        Set<String> set = new HashSet<String>();
        set.add("1");
        set.add("2");
        set.add("3");

        String src = SqlAdaptUtils.joinCollection(JavaValueTypes.STRING, set);
        LOG.info("testJoinCollection3:{}", src);

        assertTrue("testJoinCollection3", "'1','2','3'".equals(src));
    }

    @Test
    public void testJoinCollection31() {
        List<String> set = new ArrayList<String>();
        set.add("1");
        set.add("2");
        set.add("3");

        String src = SqlAdaptUtils.joinCollection(JavaValueTypes.STRING, set);
        LOG.info("testJoinCollection31:{}", src);

        assertTrue("testJoinCollection31", "'1','2','3'".equals(src));
    }
}
