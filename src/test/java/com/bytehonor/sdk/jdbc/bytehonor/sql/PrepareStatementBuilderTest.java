package com.bytehonor.sdk.jdbc.bytehonor.sql;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.jdbc.bytehonor.Student;
import com.bytehonor.sdk.jdbc.bytehonor.query.QueryCondition;

public class PrepareStatementBuilderTest {

    private static final Logger LOG = LoggerFactory.getLogger(PrepareStatementBuilderTest.class);

    @Test
    public void test() {
        Set<Integer> set = new HashSet<Integer>();
        set.add(1);
        set.add(2);
        set.add(3);
        QueryCondition condition = QueryCondition.create();
        condition.inInt("age", set);
        condition.gt("create_at", System.currentTimeMillis());
        condition.like("nickname", "boy");
        condition.descBy("age");

        PrepareStatement select = PrepareStatementBuilder.select(Student.class, condition);
        LOG.info("select sql:{}", select.sql());
        select.check();

        PrepareStatement selectById = PrepareStatementBuilder.selectById(Student.class, 1L);
        LOG.info("selectById sql:{}", selectById.sql());
        selectById.check();

        PrepareStatement count = PrepareStatementBuilder.count(Student.class, condition);
        LOG.info("count sql:{}", count.sql());
        count.check();

        PrepareStatement delete = PrepareStatementBuilder.delete(Student.class, condition);
        LOG.info("delete sql:{}", delete.sql());
        delete.check();

        PrepareStatement deleteById = PrepareStatementBuilder.deleteById(Student.class, 1L);
        LOG.info("deleteById sql:{}", deleteById.sql());
        deleteById.check();
    }

}
