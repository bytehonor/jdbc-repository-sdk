package com.bytehonor.sdk.starter.jdbc.statement;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.starter.jdbc.Student;
import com.bytehonor.sdk.starter.jdbc.sql.SqlCondition;

public class DeletePrepareStatementTestNonFilter {

    private static final Logger LOG = LoggerFactory.getLogger(DeletePrepareStatementTestNonFilter.class);

    @Test
    public void test() {
        SqlCondition condition = SqlCondition.create();
        PrepareStatement statement = new DeletePrepareStatement(Student.class, condition);
        String sql = statement.sql();

        LOG.info("sql:{}", sql);
        boolean hasError = false;
        try {
            statement.args();
        } catch (Exception e) {
            hasError = true;
            LOG.error("error {}", e.getMessage());
        }
        String target = "DELETE FROM tbl_student";
        assertTrue("testNonFilter", target.equals(sql) && hasError);
    }
}
