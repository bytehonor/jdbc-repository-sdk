package com.bytehonor.sdk.starter.jdbc.query;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.lang.spring.query.QueryCondition;
import com.bytehonor.sdk.starter.jdbc.Student;
import com.bytehonor.sdk.starter.jdbc.sql.SqlConvertor;
import com.bytehonor.sdk.starter.jdbc.statement.PrepareStatement;
import com.bytehonor.sdk.starter.jdbc.statement.SelectPrepareStatement;

public class SelectQueryTestNonFilter {

    private static final Logger LOG = LoggerFactory.getLogger(SelectQueryTestNonFilter.class);

    @Test
    public void testNonFilter() {
        QueryCondition condition = QueryCondition.and();
        PrepareStatement statement = new SelectPrepareStatement(Student.class, SqlConvertor.convert(condition));
        String sql = statement.sql();
        Object[] args = statement.args();

        LOG.info("testNonFilter sql:{}", sql);
        statement.check();

        String target = "SELECT id, nickname, age, update_at, create_at FROM tbl_student LIMIT 0,20";
        assertTrue("testNonFilter", target.equals(sql) && args.length == 0);
    }
}
