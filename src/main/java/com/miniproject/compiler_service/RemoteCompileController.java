package com.miniproject.compiler_service;

import com.miniproject.compiler_service.compile.CompileFailureException;
import com.miniproject.compiler_service.compile.CompileService;
import com.miniproject.compiler_service.storage.FileSystemStorageService;
import com.miniproject.compiler_service.storage.StorageFileNotFoundException;
import com.miniproject.compiler_service.storage.StorageProperties;
import com.miniproject.compiler_service.storage.StorageService;
import org.apache.commons.io.IOUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
public class RemoteCompileController {
    private final CompileService compileService;
    private final StorageService storageService;

    private static final Logger logger = LoggerFactory.getLogger(RemoteCompileController.class);

    @Autowired
    public RemoteCompileController(CompileService compileService, StorageService storageService) {
        this.compileService = compileService;
        this.storageService = storageService;
    }

    @GetMapping("/compiler/java/{filename:.+}")
    public ResponseEntity compileJava(@PathVariable String filename,
                                      @RequestParam Map<String,String> parameters) {
        try {
            Path path = storageService.load(filename);
            Resource compiledFile = compileService.exec(path, parameters);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + compiledFile.getFilename() + "\"")
                    .body(compiledFile);
        } catch (CompileFailureException e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (StorageFileNotFoundException e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body("Requested File Not Found");
        }
    }

    @GetMapping("/download/{filename:.+}")
    public ResponseEntity downloadFileFromLocal(@PathVariable String filename) {
        Resource resource = storageService.loadAsResource(filename);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


    @PostMapping("/compiler/go")
    public String goCompiler(@RequestHeader HttpHeaders headers) {
        return "hello world";
    }
}
