package org.uniqueck.asciidoctorj.liquibase.parser;

import org.uniqueck.asciidoctorj.liquibase.model.Table;

import java.io.File;
import java.util.Map;

public interface LiquibaseChangesetParser {

    Map<String,Table> parse(File inputFile, String tillTag);

    static LiquibaseChangesetParser xmlParser(LoggingFacade loggingFacade) {
        return new LiquibaseChangesetXMLParser(loggingFacade);
    }

}
