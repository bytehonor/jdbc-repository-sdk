package com.bytehonor.sdk.jdbc.bytehonor.meta;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.jdbc.bytehonor.model.Student;
import com.bytehonor.sdk.jdbc.bytehonor.util.MetaParseUtils;

public class MetaParseUtilsTest {

    private static final Logger LOG = LoggerFactory.getLogger(MetaParseUtilsTest.class);

    @Test
    public void testParse() {
        MetaTable table = MetaParseUtils.parse(Student.class);
        LOG.info("tableName:{}, primaryKey:{}", table.getTableName(), table.getPrimaryKey());
        List<MetaTableColumn> columns = table.getColumns();
        for (MetaTableColumn column : columns) {
            LOG.info("field:{}, column:{}", column.getField(), column.getColumn());
        }
    }

}
