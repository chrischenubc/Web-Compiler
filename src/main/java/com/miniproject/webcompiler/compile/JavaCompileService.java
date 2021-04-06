package com.miniproject.webcompiler.compile;

import com.miniproject.webcompiler.helper.CommandLineRunner;
import com.miniproject.webcompiler.storage.StorageService;
import org.apache.commons.io.FilenameUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Map;

@Service
public class JavaCompileService implements CompileService{
    private static final String DOT_CLASS_FILE_EXTENSION = ".class";
    private static final String DEFAULT_JAVA_COMPILER_VERSION = "11";
    private static final Logger logger = LoggerFactory.getLogger(JavaCompileService.class);

    @Autowired
    StorageService storageService;

    @Override
    public Resource exec(String version, MultipartFile file, Map<String,String> parameters) {
        version = version == null ? DEFAULT_JAVA_COMPILER_VERSION : version;
        storageService.store(file);
        Path path = storageService.load(file.getOriginalFilename());
        CompilerCmdBuilder javaCmdBuilder = new JavaCompilerCmdBuilder(version, path.toString(), parameters);

        logger.info("Java Compiler Executing: {}", javaCmdBuilder.toString());
        CommandLineRunner.exec(javaCmdBuilder.build());

        Resource resource = storageService.loadAsResource(
                FilenameUtils.removeExtension(path.toAbsolutePath().toString()) + DOT_CLASS_FILE_EXTENSION);
        logger.info("{} was compiled in Java {}", resource.getFilename(), javaCmdBuilder.getCompileVersion());
        return resource;
    }
}