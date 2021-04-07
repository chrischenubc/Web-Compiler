package com.miniproject.webcompiler.helper;

import com.miniproject.webcompiler.compile.CompileFailureException;
import com.miniproject.webcompiler.storage.StorageProperties;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.tomcat.jni.Proc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
//TODO Make it to a interface or abstract class!
public class CommandLineRunner {
    @Autowired
    StorageProperties storageProperties;

    public String exec(List<String> command) {
        StringBuilder output = new StringBuilder();
        ProcessBuilder processBuilder = new ProcessBuilder()
                                        .directory(new File(storageProperties.getLocation()))
                                        .command(command)
                                        .redirectErrorStream(true);
        //processBuilder = new ProcessBuilder().command("go tool compile", "/Users/chenchris/compiler_test/go_test/main.go").redirectErrorStream(true);
        try {
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
            int exitVal = process.waitFor();  //TODO: NON BLOCKING
            if (exitVal != 0) {
                String errMsg = output.toString();
                throw new CompileFailureException(errMsg);
            }

        } catch (IOException | InterruptedException e) {
            throw new CompileFailureException(e.getMessage(), e);
        }
        return output.toString();
    }

    public String exec(String ... command) {
        List<String> cmdList = new ArrayList<>();
        for (String word: command) {
            cmdList.add(word);
        }
        return exec(cmdList);
    }

    public static void main(String[] args) {
        CommandLineRunner.runCommand();
    }

    public static void runCommand() {
        String line = "go tool compile /Users/chenchris/compiler_test/go_test/main.go && ls";
        String line2 = "go tool link -o main main.o";
        CommandLine cmdLine = CommandLine.parse(line);
        CommandLine cmdLine2 = CommandLine.parse(line2);
        DefaultExecutor executor = new DefaultExecutor();
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
            executor.setStreamHandler(streamHandler);
            int exitValue = executor.execute(cmdLine);
            //executor.execute(cmdLine2);
            System.out.println(outputStream.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
