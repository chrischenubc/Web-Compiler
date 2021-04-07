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

    @Autowired
    CommandLineRunner commandLineRunner;

    @Override
    public Resource exec(String version, MultipartFile file, Map<String,String> flags) {
        version = version == null ? DEFAULT_GO_COMPILER_VERSION : version;
        storageService.store(file);
        Path path = storageService.load(file.getOriginalFilename());;
        CompilerCmdBuilder goCmdBuilder = new GoCompilerCmdBuilder(version, path.toString(), flags);

        logger.info("Executing: {}", goCmdBuilder.toString());
        System.out.println(commandLineRunner.exec(goCmdBuilder.build()));

        Resource resource = storageService.loadAsResource(
                FilenameUtils.removeExtension(path.toAbsolutePath().toString()));
        logger.info("{} was compiled in Go {}", path.getFileName(), goCmdBuilder.getCompileVersion());
        return resource;
    }

    @Override
    public Resource execWithCommand(String command, MultipartFile file) {
        storageService.store(file);
        Path path = storageService.load(file.getOriginalFilename());
        commandLineRunner.exec("bash", "-c", command);
        Resource resource = storageService.loadAsResource(
                FilenameUtils.removeExtension(path.toAbsolutePath().toString()));
        logger.info("{} was compiled in Go", path.getFileName());
        return resource;
    }

    @Override
    public Resource execWithVersionAndFlags(String version, String flags, MultipartFile file) {
        version = version == null ? DEFAULT_GO_COMPILER_VERSION : version;
        flags = flags == null ? "" : flags;
        String sourceFile = file.getOriginalFilename();
        storageService.store(file);
        Path path = storageService.load(file.getOriginalFilename());
        String command = String.format("go tool compile -lang=go%s %s %s && go tool link -o %s %s.o"
                ,version, flags, file.getOriginalFilename(), FilenameUtils.removeExtension(sourceFile),
                FilenameUtils.removeExtension(sourceFile));
        logger.info("Executing: {}", command);
        commandLineRunner.exec("bash", "-c", command);
        Resource resource = storageService.loadAsResource(
                FilenameUtils.removeExtension(path.toAbsolutePath().toString()));
        logger.info("{} was compiled in Go {}", path.getFileName(), version);
        return resource;
    }
}
