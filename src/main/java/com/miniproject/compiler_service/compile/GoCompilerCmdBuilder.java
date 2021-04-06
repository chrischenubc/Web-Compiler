package com.miniproject.compiler_service.compile;

import org.apache.commons.io.FilenameUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GoCompilerCmdBuilder extends CompilerCmdBuilder{
    //go tool compile -lang=go1.11 main.go && go tool link -o main main.o
    private static final String DEFAULT_GO_COMPILER_VERSION = "11";
    private static final String GO_TOOL_COMPILE = "go tool compile";
    private static final String GO_TOOL_LINK = "go tool link";

    public GoCompilerCmdBuilder(String version, String path, Map<String, String> options) {
        super(version, path, options);
    }

    @Override
    public List<String> build() {
        List<String> command = new ArrayList<>();
        command.add(GO_TOOL_COMPILE);
        command.add(String.format("-lang=%s", compileVersion));
        command.add(sourceFile);
        command.add("&&");
        command.add(GO_TOOL_LINK);
        command.add("-o");
        command.add(FilenameUtils.getBaseName(sourceFile));
        command.add(String.format("%s.o",FilenameUtils.getBaseName(sourceFile)));
        return command;
    }
}