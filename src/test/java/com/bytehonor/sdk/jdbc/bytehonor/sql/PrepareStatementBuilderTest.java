package com.bytehonor.sdk.jdbc.bytehonor.sql;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.jdbc.bytehonor.model.Student;
import com.bytehonor.sdk.jdbc.bytehonor.query.MatchCondition;

public class PrepareStatementBuilderTest {

    private static final Logger LOG = LoggerFactory.getLogger(PrepareStatementBuilderTest.class);

    @Test
    public void test() {
        Set<Integer> set = new HashSet<Integer>();
        set.add(1);
        set.add(2);
        set.add(3);
        MatchCondition condition = MatchCondition.create();
        condition.inInt("age", set);
        condition.gt("create_at", System.currentTimeMillis());
        condition.like("nickname", "boy");
        condition.descBy("age");

        PrepareStatement select = PrepareStatementBuilder.select(Student.class, condition);
        LOG.info("select sql:{}", select.sql());
        for (Object arg : select.args()) {
            LOG.info("select arg:{}", arg);
        }

        select = PrepareStatementBuilder.selectById(Student.class, 1L);
        LOG.info("selectById sql:{}", select.sql());
        for (Object arg : select.args()) {
            LOG.info("selectById arg:{}", arg);
        }

        PrepareStatement count = PrepareStatementBuilder.count(Student.class, condition);
        LOG.info("count sql:{}", count.sql());
        for (Object arg : count.args()) {
            LOG.info("count arg:{}", arg);
        }

        PrepareStatement delete = PrepareStatementBuilder.delete(Student.class, condition);
        LOG.info("delete sql:{}", delete.sql());
        for (Object arg : delete.args()) {
            LOG.info("delete arg:{}", arg);
        }

        delete = PrepareStatementBuilder.deleteById(Student.class, 1L);
        LOG.info("deleteById sql:{}", delete.sql());
        for (Object arg : delete.args()) {
            LOG.info("deleteById arg:{}", arg);
        }
    }

}
