package com.miniproject.webcompiler;

import com.miniproject.webcompiler.storage.StorageProperties;
import com.miniproject.webcompiler.storage.StorageService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class CompilerServiceTests {

    @Autowired
    private MockMvc mvc;

    private Path location;
    private String javaSourceFileName;
    private String javaOutputFileName;
    private String goSourceFileName;
    private String goOutputFileName;
    private File javaSourceFile;
    private File goSourceFile;

    @BeforeEach
    public void init() {
        location = Paths.get("testFiles");
        javaSourceFileName = "Sample.java";
        javaOutputFileName = String.format("%s.class", FilenameUtils.getBaseName(javaSourceFileName));
        goSourceFileName = "main.go";
        goOutputFileName = FilenameUtils.getBaseName(goSourceFileName);
        javaSourceFile = location.resolve(javaSourceFileName).toFile();
        goSourceFile = location.resolve(goSourceFileName).toFile();
    }

    @Test
    public void shouldCompileJavaFileWithCommand() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", javaSourceFileName,
                "multipart/form-data", FileUtils.readFileToByteArray(javaSourceFile));
        MockPart command = new MockPart("command", String.format("javac %s", javaSourceFileName).getBytes());
        MockPart language = new MockPart("language", "java".getBytes());

        this.mvc.perform(multipart("/command")
                .part(language)
                .part(command)
                .file(multipartFile))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + javaOutputFileName + "\""));
    }

    @Test
    public void shouldCompileGoFileWithCommand() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", goSourceFileName,
                "multipart/form-data", FileUtils.readFileToByteArray(goSourceFile));
        MockPart command = new MockPart("command",
                String.format("go tool compile %s && go tool link -o %s %s.o", goSourceFileName, goOutputFileName, goOutputFileName).getBytes());
        MockPart language = new MockPart("language", "go".getBytes());

        this.mvc.perform(multipart("/command")
                .part(language)
                .part(command)
                .file(multipartFile))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + goOutputFileName + "\""));
    }

    @Test
    public void shouldCompileJavaFileWithVersionAndFlags() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", javaSourceFileName,
                "multipart/form-data", FileUtils.readFileToByteArray(javaSourceFile));
        MockPart flags = new MockPart("flags", String.format("-verbose", javaSourceFileName).getBytes());
        MockPart language = new MockPart("language", "java".getBytes());
        MockPart version = new MockPart("version", "10".getBytes());

        this.mvc.perform(multipart("/compiler")
                .part(language)
                .part(version)
                .part(flags)
                .file(multipartFile))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + javaOutputFileName + "\""));
    }

    @Test
    public void shouldCompileGoFileWithVersionAndFlags() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", goSourceFileName,
                "multipart/form-data", FileUtils.readFileToByteArray(goSourceFile));
        MockPart flags = new MockPart("flags", String.format("-N", goSourceFileName).getBytes());
        MockPart language = new MockPart("language", "go".getBytes());
        MockPart version = new MockPart("version", "1.11".getBytes());

        this.mvc.perform(multipart("/compiler")
                .part(language)
                .part(version)
                .part(flags)
                .file(multipartFile))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + goOutputFileName + "\""));
    }

}
