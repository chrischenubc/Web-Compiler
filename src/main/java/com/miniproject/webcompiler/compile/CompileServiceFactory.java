package com.miniproject.webcompiler.compile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Generate proper CompileService for the given programming language
 */
@Component
public class CompileServiceFactory {
    @Autowired
    CompileService javaCompileService;

    @Autowired
    CompileService goCompileService;

    public CompileService getCompileService(String language){
        switch (language.toLowerCase()) {
            case "java": return javaCompileService;
            case "go": return goCompileService;
        }
        throw new UnknownLanguageException(String.format("Unsupported Programming Language: %s", language));
    }
}
