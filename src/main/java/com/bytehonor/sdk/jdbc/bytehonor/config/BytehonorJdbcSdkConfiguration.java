package com.bytehonor.sdk.jdbc.bytehonor.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.bytehonor.sdk.jdbc.bytehonor.jdbc.JdbcProxyDao;

@Configuration
@ConditionalOnClass({ DataSourceProperties.class, JdbcTemplate.class })
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
public class BytehonorJdbcSdkConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(BytehonorJdbcSdkConfiguration.class);

    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    @Bean
    @ConditionalOnProperty(prefix = "spring.datasource", name = "driver-class-name", matchIfMissing = false)
    @ConditionalOnMissingBean(value = JdbcProxyDao.class)
    public JdbcProxyDao jdbcProxyDao() {
        LOG.info("JdbcProxyDao instance new");
        return new JdbcProxyDao(jdbcTemplate);
    }
}
