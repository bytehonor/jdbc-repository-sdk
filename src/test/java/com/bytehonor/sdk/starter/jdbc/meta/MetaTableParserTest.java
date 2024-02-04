package com.bytehonor.sdk.starter.jdbc.meta;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.starter.jdbc.Student;
import com.google.common.base.Objects;

public class MetaTableParserTest {

    private static final Logger LOG = LoggerFactory.getLogger(MetaTableParserTest.class);

    @Test
    public void testTable() {
        MetaTable table = MetaTableParser.parse(Student.class);
        LOG.info("name:{}, primary:{}", table.getName(), table.getPrimary());
        List<MetaTableField> fields = table.getFields();
        for (MetaTableField field : fields) {
            LOG.info("camel:{}, underline:{}, type:{}", field.getCamel(), field.getUnderline(), field.getType());
        }

        assertTrue("testColumn", Objects.equal("tbl_student", table.getName()));
    }

}
