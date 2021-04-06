package com.miniproject.webcompiler.compile;

import java.util.List;
import java.util.Map;

public abstract class CompilerCmdBuilder {
    protected String tool; //compiler tool
    protected String compileVersion;  //compile version
    protected String sourceFile;
    protected Map<String, String> options;

    public CompilerCmdBuilder(String compileVersion, String filePath, Map<String, String> options) {
        this.tool = tool;
        this.compileVersion = compileVersion;
        this.sourceFile = filePath;
        this.options = options;
    }

    public abstract List<String> build();

    @Override
    public String toString() {
        List<String> command = build();
        return toCommandString(command);
    }

    public String toCommandString(List<String> command) {
        StringBuilder builder = new StringBuilder();
        for (String str: command) {
            builder.append(str);
            builder.append(" ");
        }
        return builder.toString();
    }

    public String getTool() {
        return tool;
    }

    public void setTool(String tool) {
        this.tool = tool;
    }

    public String getCompileVersion() {
        return compileVersion;
    }

    public void setCompileVersion(String compileVersion) {
        this.compileVersion = compileVersion;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }
}

    //    protected void writeBytesToFile(String fileName, byte[] fileData) {
//        try {
//            File file = new File(fileName);
//            FileOutputStream outputStream= new FileOutputStream(file);
//            outputStream.write(fileData);
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    protected byte[] readFileToBytes(String fileName) {
//        try {
//            File file = new File(fileName);
//            return Files.readAllBytes(file.toPath());
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


