package com.miniproject.compiler_service.controller;

import com.miniproject.compiler_service.compile.CompileService;
import com.miniproject.compiler_service.compile.CompileServiceFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
public class RemoteCompileController {
    @Autowired
    private CompileServiceFactory compileServiceFactory;

    private static final Logger logger = LoggerFactory.getLogger(RemoteCompileController.class);

    @PostMapping(value = {"/compiler/{language}", "/compiler/{language}/{version}"})
    public ResponseEntity remoteCompileWithFile(@PathVariable String language,
                                                @PathVariable(required = false) String version,
                                                @RequestParam Map<String,String> options,
                                                @RequestParam("file") MultipartFile file
                                                ) {
        try {
            CompileService compileService = compileServiceFactory.getService(language);
            Resource compiledFile = compileService.exec(version, file, options);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + compiledFile.getFilename() + "\"")
                    .body(compiledFile);
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
