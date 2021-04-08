package com.miniproject.webcompiler.compile;

import com.miniproject.webcompiler.CommandExecutor;
import com.miniproject.webcompiler.storage.StorageService;
import org.apache.commons.io.FilenameUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

/**
 * An implementation of CompileService for compiling Java code
 */
@Service
public class JavaCompileService implements CompileService{

    private static final String DOT_CLASS_FILE_EXTENSION = ".class";
    private static final String DEFAULT_JAVA_COMPILER_VERSION = "11";
    private static final Logger logger = LoggerFactory.getLogger(JavaCompileService.class);

    @Autowired
    StorageService storageService;

    @Autowired
    CommandExecutor commandLineRunner;

    @Override
    public Resource compileWithCommand(String command, MultipartFile file) {
        storageService.store(file);
        Path path = storageService.load(file.getOriginalFilename());

        commandLineRunner.exec("bash", "-c", command);
        Resource resource = storageService.loadAsResource(
                FilenameUtils.removeExtension(path.toAbsolutePath().toString()) + DOT_CLASS_FILE_EXTENSION);
        logger.info("{} was compiled in Java", path.getFileName());
        return resource;
    }

    @Override
    public Resource compileWithVersionAndFlags(String version, String flags, MultipartFile file) {
        version = version == null ? DEFAULT_JAVA_COMPILER_VERSION : version;
        flags = flags == null ? "" : flags;
        storageService.store(file);
        Path path = storageService.load(file.getOriginalFilename());

        String command = String.format("javac --release %s %s %s", version, flags, file.getOriginalFilename());
        logger.info("Executing: {}", command);
        commandLineRunner.exec("bash", "-c", command);
        Resource resource = storageService.loadAsResource(
                FilenameUtils.removeExtension(path.toAbsolutePath().toString()) + DOT_CLASS_FILE_EXTENSION);
        logger.info("{} was compiled in Java {}", path.getFileName(), version);
        return resource;
    }
}
