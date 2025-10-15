package com.bytehonor.sdk.repository.jdbc.query;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.lang.spring.query.QueryCondition;
import com.bytehonor.sdk.repository.jdbc.Student;
import com.bytehonor.sdk.repository.jdbc.sql.SqlConvertor;
import com.bytehonor.sdk.repository.jdbc.statement.DeletePrepareStatement;
import com.bytehonor.sdk.repository.jdbc.statement.PrepareStatement;

public class DeleteQueryTestNonFilter {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteQueryTestNonFilter.class);

    @Test
    public void testNonFilter() {
        QueryCondition condition = QueryCondition.and();
        PrepareStatement statement = new DeletePrepareStatement(Student.class, SqlConvertor.convert(condition));
        String sql = statement.sql();

        LOG.info("testNonFilter sql:{}", sql);
        boolean hasError = false;
        try {
            statement.args();
        } catch (Exception e) {
            hasError = true;
            LOG.error("testNonFilter error {}", e.getMessage());
        }
        String target = "DELETE FROM tbl_student";
        assertTrue("testNonFilter", target.equals(sql) && hasError);
    }
}
