package com.bytehonor.sdk.repository.jdbc.query;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.framework.lang.query.QueryCondition;
import com.bytehonor.sdk.repository.jdbc.Student;
import com.bytehonor.sdk.repository.jdbc.sql.SqlConvertor;
import com.bytehonor.sdk.repository.jdbc.statement.PrepareStatement;
import com.bytehonor.sdk.repository.jdbc.statement.SelectPrepareStatement;

public class SelectQueryTestMatchTwice {

    private static final Logger LOG = LoggerFactory.getLogger(SelectQueryTestMatchTwice.class);

    @Test
    public void test() {
        QueryCondition condition = QueryCondition.and();
        condition.eq(Student::getNickname, "eq");
        condition.neq(Student::getNickname, "neq"); // 两个参数都被用了

        PrepareStatement statement = new SelectPrepareStatement(Student.class, SqlConvertor.convert(condition));
        String sql = statement.sql();
        Object[] args = statement.args();

        LOG.info("sql:[{}]", sql);
        statement.check();

        String target = "SELECT id, nickname, age, update_at, create_at FROM tbl_student WHERE nickname = ? AND nickname != ? LIMIT 0,20";
        assertTrue("test", target.equals(sql) && args.length == 2);
    }
}
