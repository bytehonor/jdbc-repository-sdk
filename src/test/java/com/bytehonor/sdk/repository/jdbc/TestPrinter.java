package com.bytehonor.sdk.repository.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.repository.jdbc.util.SqlInjectUtils;

public class TestPrinter {

    private static final Logger LOG = LoggerFactory.getLogger(TestPrinter.class);

    public static void print(String sql, Object[] args) {
        LOG.info("sql:[{}], args:[{}]", sql, SqlInjectUtils.toString(args));
    }
}
