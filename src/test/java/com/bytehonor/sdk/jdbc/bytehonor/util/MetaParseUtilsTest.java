package com.bytehonor.sdk.jdbc.bytehonor.util;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.jdbc.bytehonor.Student;
import com.bytehonor.sdk.jdbc.bytehonor.meta.MetaTable;
import com.bytehonor.sdk.jdbc.bytehonor.meta.MetaTableColumn;

public class MetaParseUtilsTest {

    private static final Logger LOG = LoggerFactory.getLogger(MetaParseUtilsTest.class);

    @Test
    public void testParse() {
        MetaTable table = MetaParseUtils.parse(Student.class);
        LOG.info("tableName:{}, primaryKey:{}", table.getTableName(), table.getPrimaryKey());
        List<MetaTableColumn> columns = table.getColumns();
        for (MetaTableColumn column : columns) {
            LOG.info("key:{}, column:{}", column.getKey(), column.getColumn());
        }
    }

}
