package com.bytehonor.sdk.starter.jdbc.query;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.lang.spring.query.QueryCondition;
import com.bytehonor.sdk.starter.jdbc.Student;
import com.bytehonor.sdk.starter.jdbc.sql.SqlAdapter;
import com.bytehonor.sdk.starter.jdbc.statement.DeletePrepareStatement;
import com.bytehonor.sdk.starter.jdbc.statement.PrepareStatement;

public class DeletePrepareStatementTest {

    private static final Logger LOG = LoggerFactory.getLogger(DeletePrepareStatementTest.class);

    @Test
    public void test() {
        Set<Integer> set = new HashSet<Integer>();
        set.add(1);
        set.add(2);
        set.add(3);
        QueryCondition condition = QueryCondition.and();
        condition.in(Student::getAge, set);
        condition.gt(Student::getCreateAt, System.currentTimeMillis());
        condition.like(Student::getNickname, "boy");
        condition.desc(Student::getAge);
        PrepareStatement statement = new DeletePrepareStatement(Student.class, SqlAdapter.convert(condition));
        String sql = statement.sql();
        Object[] args = statement.args();

        LOG.info("sql:{}", sql);
        statement.check();

        String target = "DELETE FROM tbl_student WHERE age IN ? AND create_at > ? AND nickname LIKE ?";
        assertTrue("test", target.equals(sql) && args.length == 3);
    }
}
