package com.bytehonor.sdk.starter.jdbc.statement;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.lang.spring.query.QueryCondition;
import com.bytehonor.sdk.starter.jdbc.Student;
import com.bytehonor.sdk.starter.jdbc.model.GroupCountItem;
import com.bytehonor.sdk.starter.jdbc.sql.SqlCondition;
import com.bytehonor.sdk.starter.jdbc.sql.SqlConvertor;
import com.bytehonor.sdk.starter.jdbc.util.SqlPrinter;

public class GroupCountPrepareStatementTest {

    private static final Logger LOG = LoggerFactory.getLogger(GroupCountPrepareStatementTest.class);

    @Test
    public void test() {
        Set<Integer> set = new HashSet<Integer>();
        set.add(1);
        set.add(2);
        set.add(3);
        SqlCondition condition = SqlCondition.create();
        condition.in("age", set, Integer.class);
        condition.gt("createAt", System.currentTimeMillis());
        condition.like("nickname", "boy");
        PrepareStatement statement = new GroupCountPrepareStatement(Student.class, Student::getAge, condition);
        String sql = statement.sql();
        Object[] args = statement.args();
        SqlPrinter.print(sql, args);
        statement.check();

        String target = "SELECT `age` AS `value`, COUNT(id) AS `size` FROM tbl_student WHERE age IN (1,2,3) AND create_at > ? AND nickname LIKE ? GROUP BY `age` ORDER BY NULL";
        assertTrue("test", target.equals(sql) && args.length == 2);
    }

    @Test
    public void testWithOrder() {
        Set<Integer> set = new HashSet<Integer>();
        set.add(1);
        set.add(2);
        set.add(3);
        QueryCondition condition = QueryCondition.and();
        condition.in(Student::getAge, set);
        condition.gt(Student::getCreateAt, System.currentTimeMillis());
        condition.like(Student::getNickname, "boy");
        condition.desc(GroupCountItem::getSize);
        PrepareStatement statement = new GroupCountPrepareStatement(Student.class, Student::getAge,
                SqlConvertor.convert(condition));
        String sql = statement.sql();
        Object[] args = statement.args();

        LOG.info("testWithOrder sql:[{}]", sql);
        statement.check();

        String target = "SELECT `age` AS `value`, COUNT(id) AS `size` FROM tbl_student WHERE age IN (1,2,3) AND create_at > ? AND nickname LIKE ? GROUP BY `age` ORDER BY size DESC";
        assertTrue("testWithOrder", target.equals(sql) && args.length == 2);
    }
}
