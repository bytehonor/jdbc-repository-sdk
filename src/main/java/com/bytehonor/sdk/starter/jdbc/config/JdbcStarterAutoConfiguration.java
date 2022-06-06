package com.bytehonor.sdk.starter.jdbc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.bytehonor.sdk.starter.jdbc.dao.JdbcProxyDao;

@Configuration
@ConditionalOnClass({ DataSourceProperties.class, JdbcTemplate.class })
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class JdbcStarterAutoConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(JdbcStarterAutoConfiguration.class);

    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    @Bean
    @ConditionalOnProperty(prefix = "spring.datasource", name = "driver-class-name", matchIfMissing = false)
    public JdbcProxyDao jdbcProxyDao() {
        LOG.info("[Bytehonor] JdbcProxyDao");
        return new JdbcProxyDao(jdbcTemplate);
    }
}
