package com.miniproject.webcompiler.compile;

import org.apache.commons.io.FilenameUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JavaCompilerCmdBuilder extends CompilerCmdBuilder {
    //Usage: javac <options> <source files>
    private static final String JAVA_COMPILE_TOOL = "javac";
    private static final String RELEASE_OPTION = "--release";
    private static final String SOURCE_OPTION = "--source";

    public JavaCompilerCmdBuilder(String version, String path, Map<String, String> options) {
        super(version, path, options);
        searchAndSetVersion(options);
    }

    @Override
    public List<String> build() {
        List<String> command = new ArrayList<>();
        command.add("bash");
        command.add("-c");
        command.add(commandStr());
        return command;
    }

    private String commandStr() {
        return String.format("%s --release %s %s",
                JAVA_COMPILE_TOOL, compileVersion,
                sourceFile);
    }

    private List<String> getOptionsCmd() {
        List<String> optionList = new ArrayList<>();
        for (String flag : options.keySet()) {
            optionList.add(flag);
            String argument = options.get(flag);
            if (argument != null && !argument.equals("")) {
                optionList.add(argument);
            }
        }
        return optionList;
    }

    private void searchAndSetVersion(Map<String, String> options) {
        for (String key: options.keySet()) {
            if (key.equals(RELEASE_OPTION) || key.equals(SOURCE_OPTION)) {
                compileVersion = options.get(key); //TODO: ERROR/NULL CHECKING
                options.remove(key);
                return;
            }
        }
    }
}
