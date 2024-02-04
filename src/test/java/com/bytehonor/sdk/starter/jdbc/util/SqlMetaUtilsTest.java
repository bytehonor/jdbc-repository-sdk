package com.bytehonor.sdk.starter.jdbc.util;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.starter.jdbc.Student;
import com.bytehonor.sdk.starter.jdbc.meta.MetaTable;
import com.bytehonor.sdk.starter.jdbc.meta.MetaTableField;
import com.google.common.base.Objects;

public class SqlMetaUtilsTest {

    private static final Logger LOG = LoggerFactory.getLogger(SqlMetaUtilsTest.class);

    @Test
    public void testParse() {
        MetaTable table = SqlMetaUtils.parse(Student.class);
        LOG.info("name:{}, primary:{}", table.getName(), table.getPrimary());
        List<MetaTableField> fields = table.getFields();
        for (MetaTableField field : fields) {
            LOG.info("camel:{}, underline:{}, type:{}", field.getCamel(), field.getUnderline(), field.getType());
        }

        assertTrue("testColumn", Objects.equal("tbl_student", table.getName()));
    }

}
