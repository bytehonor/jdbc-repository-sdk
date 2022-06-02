package com.bytehonor.sdk.jdbc.bytehonor.selector;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import com.bytehonor.sdk.jdbc.bytehonor.config.BytehonorJdbcSdkConfiguration;

public class BytehonorJdbcSdkImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[] { BytehonorJdbcSdkConfiguration.class.getName() };
    }
}
