package com.miniproject.compiler_service.compile;

import java.nio.file.Path;
import java.util.Map;
import org.springframework.core.io.Resource;

public interface CompileService {
    Resource exec(String version, Path path, Map<String, String> parameters);
}
