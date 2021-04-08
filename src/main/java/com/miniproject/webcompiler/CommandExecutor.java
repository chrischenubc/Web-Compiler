package com.miniproject.webcompiler;

import com.miniproject.webcompiler.compile.CompileFailureException;
import com.miniproject.webcompiler.storage.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * A helper class to start a new process and run a command.
 *     The working directory is set to the temporary storage folder
 */
@Component
public class CommandExecutor {
    @Autowired
    StorageProperties storageProperties;

    /**
     * Execute the given command
     * @param command The command string is split and stored in an array of strings
     * @return the stdout of the command after execution
     */
    public String exec(List<String> command) {
        StringBuilder output = new StringBuilder();
        ProcessBuilder processBuilder = new ProcessBuilder()
                .directory(new File(storageProperties.getLocation()))
                .command(command)
                .redirectErrorStream(true);
        try {
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
            int exitVal = process.waitFor();
            if (exitVal != 0) {
                String errMsg = output.toString();
                throw new CompileFailureException(errMsg);
            }

        } catch (IOException | InterruptedException e) {
            throw new CompileFailureException(e.getMessage(), e);
        }
        return output.toString();
    }

    /**
     * Execute the given command
     * @param command The command is composed by a list of strings
     * @return the stdout of the command after execution
     */
    public String exec(String... command) {
        List<String> cmdList = new ArrayList<>();
        for (String word : command) {
            cmdList.add(word);
        }
        return exec(cmdList);
    }
}
