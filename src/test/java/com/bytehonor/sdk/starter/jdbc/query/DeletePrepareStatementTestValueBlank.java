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

public class DeletePrepareStatementTestValueBlank {

    private static final Logger LOG = LoggerFactory.getLogger(DeletePrepareStatementTestValueBlank.class);

    @Test
    public void test() {
        String uuid = "";
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
        String target = "DELETE FROM tbl_student WHERE nickname = ?";
        assertTrue("test", target.equals(sql) && hasError == false && statement.args().length == 1);
    }
}
