package com.miniproject.webcompiler.controller;

import com.miniproject.webcompiler.compile.CompileService;
import com.miniproject.webcompiler.compile.CompileServiceException;
import com.miniproject.webcompiler.compile.CompileServiceFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

/**
 * The REST controller to handle the incoming compile service requests
 */
@RestController
public class RemoteCompileController {
    @Autowired
    private CompileServiceFactory compileServiceFactory;

    private static final Logger logger = LoggerFactory.getLogger(RemoteCompileController.class);

    /**
     * Handle compile service with the pre-programmed compile command
     * User only needs to provide language versions (optional) and flags (optional)
     * @param language defines the programming language to be used
     * @param version sets the compile language version; null if it not provided by the user
     * @param flags sets options/flags for the compiler; null if it not provided by the user
     * @param file represents the source code to be compiled
     * @return HTTPStatus.ok if the compile is successful,
     *           and responds with the output file or executable in the response body
     */
    @PostMapping(value = {"/compiler"})
    public ResponseEntity remoteCompileWithVersion(@RequestParam String language,
                                                   @RequestParam(value = "version", required = false) String version,
                                                   @RequestPart(value = "flags", required = false) String flags,
                                                   @RequestPart("file") MultipartFile file
    ) {
        try {
            CompileService compileService = compileServiceFactory.getCompileService(language);
            Resource compiledFile = compileService.compileWithVersionAndFlags(version, flags, file);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + compiledFile.getFilename() + "\"")
                    .body(compiledFile);
        } catch (CompileServiceException e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Handle compile service with used-defined commands
     * @param language defines the programming language to be used
     * @param command sets the users-defined compile command
     * @param file represents the source code to be compiled
     * @return HTTPStatus.ok if the compile is successful,
     *          and responds with the output file or executable in the response body
     */
    @PostMapping(value = {"/command"})
    public ResponseEntity compileCommand(@RequestParam String language,
                                         @RequestPart("command") String command,
                                         @RequestPart("file") MultipartFile file
                                         ) {
        try {
            CompileService compileService = compileServiceFactory.getCompileService(language);
            Resource compiledFile = compileService.compileWithCommand(command, file);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + compiledFile.getFilename() + "\"")
                    .body(compiledFile);
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity handleMissingParams(MissingServletRequestPartException ex) {
        logger.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
