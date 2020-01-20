package org.uniqueck.asciidoctorj.liquibase;

import com.uniqueck.asciidoctorj.extension.support.AbstractAsciidoctorjExtensionRegistry;
import org.asciidoctor.extension.JavaExtensionRegistry;

public class LiquibaseExtensionRegistry extends AbstractAsciidoctorjExtensionRegistry {

    @Override
    protected void registerExtension(JavaExtensionRegistry javaExtensionRegistry) {
            javaExtensionRegistry.blockMacro(LiquibaseBlockMacroProcessor.class);
    }

}
