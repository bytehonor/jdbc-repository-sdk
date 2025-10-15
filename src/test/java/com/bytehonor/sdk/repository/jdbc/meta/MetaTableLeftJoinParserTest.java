package com.bytehonor.sdk.repository.jdbc.meta;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.repository.jdbc.StudentContact;

public class MetaTableLeftJoinParserTest {

    private static final Logger LOG = LoggerFactory.getLogger(MetaTableLeftJoinParserTest.class);

    @Test
    public void testTable() {
        MetaTableLeftJoin table = MetaTableLeftJoinParser.parse(StudentContact.class);
        LOG.info("on:{}", table.getOn());

        LOG.info("full:{}", table.getFullColumns());
        List<MetaTableField> fields = table.getFields();
        for (MetaTableField field : fields) {
            LOG.info("camel:{}, underline:{}, type:{}", field.getCamel(), field.getUnderline(), field.getType());
        }

    }

}
