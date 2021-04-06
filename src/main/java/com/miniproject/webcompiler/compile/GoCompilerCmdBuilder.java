package com.miniproject.webcompiler.compile;

import org.apache.commons.io.FilenameUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GoCompilerCmdBuilder extends CompilerCmdBuilder{
    //go tool compile -lang=go1.11 main.go && go tool link -o main main.o
    private static final String DEFAULT_GO_COMPILER_VERSION = "11";
    private static final String GO_TOOL_COMPILE = "/usr/local/go/pkg/tool/darwin_amd64/compile";
    private static final String GO_TOOL_LINK = "/usr/local/go/pkg/tool/darwin_amd64/link";

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
        return String.format("%s -lang=go%s -o %s.o %s && %s -o %s %s.o", "go tool compile", compileVersion,
                FilenameUtils.removeExtension(sourceFile),
                sourceFile, "go tool link",
                FilenameUtils.removeExtension(sourceFile), FilenameUtils.removeExtension(sourceFile));
    }
}