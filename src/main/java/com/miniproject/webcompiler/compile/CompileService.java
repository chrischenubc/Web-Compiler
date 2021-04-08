package com.miniproject.webcompiler.compile;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * Defines two compile services
 */
public interface CompileService {

    /**
     * Compile the source code with the given compiler version and compile flags
     * @param version sets the programming language version for the compiler to compile the code
     * @param flags sets the flags(i.e options) for the compiler
     * @param file represents the source code to be compiled
     * @return the output file or executable that the compiler generates after execution
     */
    Resource compileWithVersionAndFlags(String version, String flags, MultipartFile file);

    /**
     * Compile the source code with the user-defined compile command
     * @param command defines the command that users want to compile the code
     * @param file represents the source code to be compiled
     * @return the output file or executable that the compiler generates after execution
     */
    Resource compileWithCommand(String command, MultipartFile file);
}
