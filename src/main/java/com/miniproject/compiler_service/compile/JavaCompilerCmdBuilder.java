package com.miniproject.compiler_service.compile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JavaCompilerCmdBuilder extends CompilerCmdBuilder {
    //Usage: javac <options> <source files>
    private static final String DEFAULT_JAVA_COMPILER_VERSION = "11";
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
        command.add(JAVA_COMPILE_TOOL);
        command.add(SOURCE_OPTION);
        command.add(compileVersion);
        command.addAll(getOptionsCmd());
        command.add(sourceFile);
        return command;
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
