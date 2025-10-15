package com.bytehonor.sdk.repository.jdbc.statement;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.repository.jdbc.Student;
import com.bytehonor.sdk.repository.jdbc.sql.SqlCondition;

public class DeletePrepareStatementTestValueBlank {

    private static final Logger LOG = LoggerFactory.getLogger(DeletePrepareStatementTestValueBlank.class);

    @Test
    public void test() {
        String uuid = "";
        SqlCondition condition = SqlCondition.create().eq("uuid", uuid); // 空值会被查询
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
        String target = "DELETE FROM tbl_student WHERE uuid = ?";
        assertTrue("testStringEmpty", target.equals(sql) && hasError == false && statement.args().length == 1);
    }
}
