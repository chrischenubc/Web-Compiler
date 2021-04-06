package com.miniproject.compiler_service.compile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CompileServiceFactory {
    @Autowired
    CompileService javaCompileService;

    @Autowired
    CompileService goCompileService;

    public CompileService getService(String language){
        switch (language.toLowerCase()) {
            case "java": return javaCompileService;
            case "go": return goCompileService;
        }
        throw new UnknownLanguageException(String.format("Unsupported Programming Language: %s", language));
    }


}
