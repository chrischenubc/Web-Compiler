package com.miniproject.webcompiler.compile;

import java.util.Map;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface CompileService {
    Resource exec(String version, MultipartFile file, Map<String, String> parameters);
}
