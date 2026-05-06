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
 * JDBC Repository SDK 自动装配配置。
 * <p>
 * 当 Spring 环境存在数据源相关类且配置了 {@code spring.datasource.driver-class-name}
 * 时，自动注册 {@link JdbcProxyDao}。
 * </p>
 *
 * @author lijianqiang
 */
@Configuration
@ConditionalOnClass({ DataSourceProperties.class, JdbcTemplate.class, JdbcProperties.class })
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class JdbcRepositorySdkConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(JdbcRepositorySdkConfiguration.class);

    @Bean
    @ConditionalOnProperty(prefix = "spring.datasource", name = "driver-class-name", matchIfMissing = false)
    JdbcProxyDao jdbcProxyDao(JdbcTemplate jdbcTemplate) {
        LOG.info("[Bytehonor] JdbcProxyDao");
        return new JdbcProxyDao(jdbcTemplate);
    }
}
