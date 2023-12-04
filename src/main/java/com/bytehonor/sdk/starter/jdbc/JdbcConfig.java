package com.bytehonor.sdk.starter.jdbc;

public class JdbcConfig {

    private boolean infoEnabled;

    private JdbcConfig() {
        this.infoEnabled = false;
    }

    private static class LazyHolder {
        private static JdbcConfig SINGLE = new JdbcConfig();
    }

    private static JdbcConfig self() {
        return LazyHolder.SINGLE;
    }

    public static boolean isInfoEnabled() {
        return self().infoEnabled;
    }

    public static boolean setInfoEnabled(boolean infoEnabled) {
        return self().infoEnabled = infoEnabled;
    }

}
