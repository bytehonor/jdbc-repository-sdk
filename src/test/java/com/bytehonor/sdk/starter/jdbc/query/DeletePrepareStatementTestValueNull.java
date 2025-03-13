package com.bytehonor.sdk.starter.jdbc.query;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.lang.spring.query.QueryCondition;
import com.bytehonor.sdk.starter.jdbc.Student;
import com.bytehonor.sdk.starter.jdbc.sql.SqlAdapter;
import com.bytehonor.sdk.starter.jdbc.statement.DeletePrepareStatement;
import com.bytehonor.sdk.starter.jdbc.statement.PrepareStatement;

public class DeletePrepareStatementTestValueNull {

    private static final Logger LOG = LoggerFactory.getLogger(DeletePrepareStatementTestValueNull.class);

    @Test
    public void testValueNull() {
        String uuid = null;
        QueryCondition condition = QueryCondition.and().eq(Student::getNickname, uuid);
        PrepareStatement statement = new DeletePrepareStatement(Student.class, SqlAdapter.convert(condition));
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
