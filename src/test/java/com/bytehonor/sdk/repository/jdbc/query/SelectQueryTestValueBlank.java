package com.bytehonor.sdk.repository.jdbc.query;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.lang.spring.query.QueryCondition;
import com.bytehonor.sdk.repository.jdbc.Student;
import com.bytehonor.sdk.repository.jdbc.sql.SqlConvertor;
import com.bytehonor.sdk.repository.jdbc.statement.PrepareStatement;
import com.bytehonor.sdk.repository.jdbc.statement.SelectPrepareStatement;

public class SelectQueryTestValueBlank {

    private static final Logger LOG = LoggerFactory.getLogger(SelectQueryTestValueBlank.class);

    @Test
    public void test() {
        QueryCondition condition = QueryCondition.and();
        condition.eq(Student::getNickname, "");
        PrepareStatement statement = new SelectPrepareStatement(Student.class, SqlConvertor.convert(condition));
        String sql = statement.sql();
        Object[] args = statement.args();

        LOG.info("sql:{}", sql);
        statement.check();

        String target = "SELECT id, nickname, age, update_at, create_at FROM tbl_student WHERE nickname = ? LIMIT 0,20";
        assertTrue("test", target.equals(sql) && args.length == 1);
    }
}
