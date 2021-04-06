package com.miniproject.compiler_service.compile;

import java.nio.file.Path;
import java.util.Map;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface CompileService {
    Resource exec(String version, MultipartFile file, Map<String, String> parameters);
}
