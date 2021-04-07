package com.miniproject.webcompiler.compile;

import java.util.Map;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface CompileService {
    Resource execWithVersionAndFlags(String version, String flags, MultipartFile file);
    Resource execWithCommand(String command, MultipartFile file);
}
