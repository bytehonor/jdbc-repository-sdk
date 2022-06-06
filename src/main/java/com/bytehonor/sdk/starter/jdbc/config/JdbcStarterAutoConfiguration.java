package com.bytehonor.sdk.starter.jdbc.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.jdbc.JdbcProperties;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import com.bytehonor.sdk.starter.jdbc.dao.JdbcProxyDao;

@Configuration
@ConditionalOnClass({ DataSourceProperties.class, JdbcTemplate.class })
@AutoConfigureAfter(JdbcTemplateAutoConfiguration.class)
public class JdbcStarterAutoConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(JdbcStarterAutoConfiguration.class);

    @ConditionalOnMissingBean
    @Bean
    @Primary
    JdbcTemplate jdbcTemplate(DataSource dataSource, JdbcProperties properties) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        JdbcProperties.Template template = properties.getTemplate();
        jdbcTemplate.setFetchSize(template.getFetchSize());
        jdbcTemplate.setMaxRows(template.getMaxRows());
        if (template.getQueryTimeout() != null) {
            jdbcTemplate.setQueryTimeout((int) template.getQueryTimeout().getSeconds());
        }
        return jdbcTemplate;
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.datasource", name = "driver-class-name", matchIfMissing = false)
    public JdbcProxyDao jdbcProxyDao(JdbcTemplate jdbcTemplate) {
        LOG.info("[Bytehonor] JdbcProxyDao");
        return new JdbcProxyDao(jdbcTemplate);
    }
}
