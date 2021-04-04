package com.miniproject.compiler_service.compile;

import com.miniproject.compiler_service.helper.CommandLineHelper;
import com.miniproject.compiler_service.storage.StorageService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class JavaCompileService implements CompileService{
    private int version;
    private static final String JAVA_COMPILER = "javac";
    private static final String DOT_CLASS_FILE_EXTENSION = ".class";

    @Autowired
    StorageService storageService;

    @Override
    public Resource exec(Path path, Map<String,String> parameters) {
        String[] command = buildCommand(path, parameters);
        String stdout = CommandLineHelper.exec(command);
        System.out.println(stdout);
        return storageService.loadAsResource(
                FilenameUtils.removeExtension(path.toAbsolutePath().toString()) + DOT_CLASS_FILE_EXTENSION);
    }


    private String[] buildCommand(Path path, Map<String,String> parameters) {
        List<String> cmdList = new ArrayList<>();
        cmdList.add(JAVA_COMPILER);
        for (String flag: parameters.keySet()) {
            String argument = parameters.get(flag);
            if (argument != null && !argument.equals("")) {
                flag += " ";
                flag += argument;
            }
            cmdList.add(flag);
        }
        cmdList.add(path.toAbsolutePath().toString());
        String[] command = new String[cmdList.size()];
        command = cmdList.toArray(command);
        return command;
    }
}
