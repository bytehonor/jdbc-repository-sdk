package com.bytehonor.sdk.repository.jdbc.query;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.framework.lang.query.QueryCondition;
import com.bytehonor.sdk.repository.jdbc.Student;
import com.bytehonor.sdk.repository.jdbc.sql.SqlConvertor;
import com.bytehonor.sdk.repository.jdbc.statement.DeletePrepareStatement;
import com.bytehonor.sdk.repository.jdbc.statement.PrepareStatement;

public class DeleteQueryTestValueBlank {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteQueryTestValueBlank.class);

    @Test
    public void test() {
        String uuid = "";
        QueryCondition condition = QueryCondition.and().eq(Student::getNickname, uuid);
        PrepareStatement statement = new DeletePrepareStatement(Student.class, SqlConvertor.convert(condition));
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
