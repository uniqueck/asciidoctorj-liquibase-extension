package org.uniqueck.asciidoctorj.liquibase.parser;

import java.io.File;

public interface LoggingFacade {

        String LOG_MESSAGE_IGNORED_ELEMENT = "skip tag '%s'";
        String LOG_MESSAGE_UNSUPPORTED_ELEMENT = "Unsupported liquibase element '%s' detected";
        String LOG_ERROR_ON_PARSING_FILE = "Error on parsing '%s' : %s";

        void logIgnoredElement(String element);
        void logUnsupportedElement(String element);
        void logParsingError(File inputFile, Exception cause);

    }