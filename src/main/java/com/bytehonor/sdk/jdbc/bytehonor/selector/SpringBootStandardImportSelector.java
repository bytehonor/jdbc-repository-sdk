package com.bytehonor.sdk.jdbc.bytehonor.selector;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import com.bytehonor.sdk.jdbc.bytehonor.config.SpringBootStandardConfiguration;

public class SpringBootStandardImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{SpringBootStandardConfiguration.class.getName()};
    }
}
