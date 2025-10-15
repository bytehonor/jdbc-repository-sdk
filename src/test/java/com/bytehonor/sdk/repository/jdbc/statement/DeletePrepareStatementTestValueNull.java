package com.bytehonor.sdk.repository.jdbc.statement;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.repository.jdbc.Student;
import com.bytehonor.sdk.repository.jdbc.sql.SqlCondition;

public class DeletePrepareStatementTestValueNull {

    private static final Logger LOG = LoggerFactory.getLogger(DeletePrepareStatementTestValueNull.class);

    @Test
    public void testValueNull() {
        String uuid = null;
        SqlCondition condition = SqlCondition.create().eq("uuid", uuid);
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
        assertTrue("test", target.equals(sql) && hasError);
    }

}
