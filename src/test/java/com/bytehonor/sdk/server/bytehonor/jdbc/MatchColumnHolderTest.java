package com.bytehonor.sdk.server.bytehonor.jdbc;



import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.jdbc.bytehonor.jdbc.MatchColumnHolder;
import com.bytehonor.sdk.jdbc.bytehonor.query.MatchColumn;
import com.google.common.collect.Lists;

public class MatchColumnHolderTest {

    private static final Logger LOG = LoggerFactory.getLogger(MatchColumnHolderTest.class);

    @Test
    public void test() {
        MatchColumnHolder holder = MatchColumnHolder.create();
        holder.and(MatchColumn.eq("name", "john"));
        holder.and(MatchColumn.neq("neq", 1));
        holder.and(MatchColumn.elt("elt", 3));
        holder.and(MatchColumn.lt("lt", 4));
        holder.and(MatchColumn.in("in", Lists.newArrayList("xxx", "yyy")));

        LOG.info("sql:{}", holder.toAndSql());

        List<Object> args = holder.getArgs();
        LOG.info("args:{}", args);
        assertTrue("test", args.size() == 4);
    }

}
