package com.miniproject.webcompiler.helper;

import com.miniproject.webcompiler.compile.CompileFailureException;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Component
//TODO Make it to a interface or abstract class!
public class CommandLineRunner {
    public static String exec(List<String> command) {
        StringBuilder output = new StringBuilder();
        ProcessBuilder processBuilder = new ProcessBuilder().command(command).redirectErrorStream(true);
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

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    public static void main(String[] args) {
        String commad = "javac -verbose /Users/chenchris/Work/compiler_service/temp/Sample.java";
        //String[] commands = {"javac", "-verbose", "/Users/chenchris/Work/compiler_service/temp/Sample.java"};
        //String output = CommandLineRunner.exec(commands);
        //System.out.println(output);
    }
}
