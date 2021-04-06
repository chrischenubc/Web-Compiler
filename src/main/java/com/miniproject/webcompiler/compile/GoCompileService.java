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
public class GoCompileService implements CompileService{
    private static final Logger logger = LoggerFactory.getLogger(GoCompileService.class);
    private static final String DEFAULT_GO_COMPILER_VERSION = "1.12";

    @Autowired
    StorageService storageService;

    @Override
    public Resource exec(String version, MultipartFile file, Map<String,String> parameters) {
        version = version == null ? DEFAULT_GO_COMPILER_VERSION : version;
        storageService.store(file);
        Path path = storageService.load(file.getOriginalFilename());
        CompilerCmdBuilder goCmdBuilder = new GoCompilerCmdBuilder(version, path.toString(), parameters);

        logger.info("Executing: {}", goCmdBuilder.toString());
        CommandLineRunner.exec(goCmdBuilder.build());

        Resource resource = storageService.loadAsResource(
                FilenameUtils.removeExtension(path.toAbsolutePath().toString()));
        logger.info("{} was compiled in Go {}", resource.getFilename(), goCmdBuilder.getCompileVersion());
        return resource;
    }
}
