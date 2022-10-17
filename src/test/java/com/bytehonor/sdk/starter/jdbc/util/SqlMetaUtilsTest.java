package com.bytehonor.sdk.starter.jdbc.util;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.starter.jdbc.Student;
import com.bytehonor.sdk.starter.jdbc.meta.MetaTable;
import com.bytehonor.sdk.starter.jdbc.meta.MetaTableField;

public class SqlMetaUtilsTest {

    private static final Logger LOG = LoggerFactory.getLogger(SqlMetaUtilsTest.class);

    @Test
    public void testParse() {
        MetaTable table = SqlMetaUtils.parse(Student.class);
        LOG.info("tableName:{}, primaryKey:{}", table.getTableName(), table.getPrimaryKey());
        List<MetaTableField> fields = table.getFields();
        for (MetaTableField field : fields) {
            LOG.info("key:{}, column:{}, type:{}", field.getKey(), field.getColumn(), field.getType());
        }
    }

}
