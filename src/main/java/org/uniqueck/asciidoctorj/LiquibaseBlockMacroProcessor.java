package org.uniqueck.asciidoctorj;

import org.asciidoctor.ast.StructuralNode;
import org.asciidoctor.extension.BlockMacroProcessor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LiquibaseBlockMacroProcessor extends BlockMacroProcessor {


    protected  List<String> generateAsciiDocMarkup(StructuralNode parent, File sourceFile, Map<String, Object> attributes) {
        return new ArrayList<>();
    }

    @Override
    public Object process(StructuralNode parent, String target, Map<String, Object> attributes) {
        parseContent(parent, generateAsciiDocMarkup(parent, getTargetAsFile(parent, target), attributes));
        return null;
    }

    protected File getBuildDir(StructuralNode structuralNode) {
        Map<Object, Object> globalOptions = structuralNode.getDocument().getOptions();

        String toDir = (String) globalOptions.get("to_dir");
        String destDir = (String) globalOptions.get("destination_dir");
        String buildDir = toDir != null ? toDir : destDir;
        return new File(buildDir);
    }

    protected String getAttribute(StructuralNode structuralNode, String attributeName, String defaultValue) {
        String value = (String) structuralNode.getAttribute(attributeName);

        if (value == null || value.trim().isEmpty()) {
            value = defaultValue;
        }

        return value;
    }


    protected File getTargetAsFile(StructuralNode structuralNode, String target) {
        String docdir = getAttribute(structuralNode, "docdir", "");
        return new File(docdir, target);
    }
}
