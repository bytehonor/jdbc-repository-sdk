package com.bytehonor.sdk.repository.jdbc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.jdbc.JdbcProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.bytehonor.sdk.repository.jdbc.dao.JdbcProxyDao;

/**
 * @author lijianqiang
 *
 */
@Configuration
@ConditionalOnClass({ DataSourceProperties.class, JdbcTemplate.class, JdbcProperties.class })
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class JdbcRepositoryAutoConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(JdbcRepositoryAutoConfiguration.class);

    @Bean
    @ConditionalOnProperty(prefix = "spring.datasource", name = "driver-class-name", matchIfMissing = false)
    JdbcProxyDao jdbcProxyDao(JdbcTemplate jdbcTemplate) {
        LOG.info("[Bytehonor] JdbcProxyDao");
        return new JdbcProxyDao(jdbcTemplate);
    }
}
