package com.miniproject.webcompiler.compile;

import org.apache.commons.io.FilenameUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GoCompilerCmdBuilder extends CompilerCmdBuilder{
    //go tool compile -lang=go1.11 main.go && go tool link -o main main.o
    private static final String GO_TOOL_COMPILE = "go tool compile";
    private static final String GO_TOOL_LINK = "go tool link";

    public GoCompilerCmdBuilder(String version, String path, Map<String, String> options) {
        super(version, path, options);
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
        return String.format("%s -lang=go%s -o %s.o %s%s && %s -o %s %s.o",
                GO_TOOL_COMPILE, compileVersion,
                FilenameUtils.removeExtension(sourceFile),
                parseOptions(),
                sourceFile, GO_TOOL_LINK,
                FilenameUtils.removeExtension(sourceFile), FilenameUtils.removeExtension(sourceFile));
    }

    private String parseOptions() {
        StringBuilder strBuilder = new StringBuilder();
        for (String flag: options.keySet()) {
            strBuilder.append(flag);
            strBuilder.append(" ");
            String argument = options.get(flag);
            if (argument != null && !argument.equals("")) {
                strBuilder.append(argument);
                strBuilder.append(" ");
            }
        }
        return strBuilder.toString();
    }
}