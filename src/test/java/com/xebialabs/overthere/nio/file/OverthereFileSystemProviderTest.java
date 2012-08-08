package com.xebialabs.overthere.nio.file;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.google.common.collect.Maps;
import com.google.common.io.Closeables;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class OverthereFileSystemProviderTest {

    private FileSystem fileSystem;
    private File tempDir;

    @BeforeMethod
    public void createFileSystem() throws IOException {
        fileSystem = FileSystems.newFileSystem(URI.create("local:///"), Maps.<String, Object>newHashMap());
        tempDir = com.google.common.io.Files.createTempDir();
    }

    @AfterMethod
    public void closeFileSystem() throws IOException {
        Closeables.closeQuietly(fileSystem);
        Files.walkFileTree(tempDir.toPath(), new DeleteDirVisitor());
    }

    @Test(expectedExceptions = FileSystemAlreadyExistsException.class)
    public void shouldNotCreateFilesystemTwice() throws IOException {
        FileSystems.newFileSystem(URI.create("local:///"), Maps.<String, Object>newHashMap());
    }

    @Test(expectedExceptions = FileSystemNotFoundException.class)
    public void shouldNotBeAbleToGetClosedFileSystem() throws IOException {
        fileSystem.close();
        FileSystems.getFileSystem(URI.create("local:///"));
    }

    @Test
    public void shouldCreateDir() throws IOException {
        File file = new File(tempDir, "a-dir");
        assertThat(file.exists(), equalTo(false));
        fileSystem.provider().createDirectory(fileSystem.getPath(file.getAbsolutePath()));
        assertThat(file.exists(), equalTo(true));
        assertThat(file.isDirectory(), equalTo(true));
    }

    @Test
    public void shouldDeleteFile() throws IOException {
        File to = new File(tempDir, "test.txt");
        com.google.common.io.Files.write("Some text", to, Charset.defaultCharset());
        assertThat(to.exists(), equalTo(true));
        Path path = fileSystem.getPath(to.getAbsolutePath());
        fileSystem.provider().delete(path);
        assertThat(to.exists(), equalTo(false));
    }
}
