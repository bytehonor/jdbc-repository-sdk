package com.bytehonor.sdk.starter.jdbc.query;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.lang.spring.query.QueryCondition;
import com.bytehonor.sdk.starter.jdbc.Student;
import com.bytehonor.sdk.starter.jdbc.sql.SqlAdapter;
import com.bytehonor.sdk.starter.jdbc.statement.PrepareStatement;
import com.bytehonor.sdk.starter.jdbc.statement.SelectPrepareStatement;

public class SelectPrepareStatementTestMatchTwice {

    private static final Logger LOG = LoggerFactory.getLogger(SelectPrepareStatementTestMatchTwice.class);

    @Test
    public void test() {
        QueryCondition condition = QueryCondition.and();
        condition.eq(Student::getNickname, "eq");
        condition.neq(Student::getNickname, "neq"); // 两个参数都被用了

        PrepareStatement statement = new SelectPrepareStatement(Student.class, SqlAdapter.convert(condition));
        String sql = statement.sql();
        Object[] args = statement.args();

        LOG.info("sql:({})", sql);
        statement.check();

        String target = "SELECT id, nickname, age, update_at, create_at FROM tbl_student WHERE nickname = ? AND nickname != ? LIMIT 0,20";
        assertTrue("test", target.equals(sql) && args.length == 2);
    }
}
