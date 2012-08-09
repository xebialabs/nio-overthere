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

import com.xebialabs.overthere.OperatingSystemFamily;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class OverthereFileSystemProviderTest {

    private FileSystem fileSystem;
    private File tempDir;
    private File testFile;

    @BeforeMethod
    public void createFileSystem() throws IOException {
        fileSystem = FileSystems.newFileSystem(URI.create("local:///"), Maps.<String, Object>newHashMap());
        tempDir = com.google.common.io.Files.createTempDir();
        testFile = new File(tempDir, "test.txt");
        com.google.common.io.Files.write("Some text", testFile, Charset.defaultCharset());
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

    @Test(expectedExceptions = FileAlreadyExistsException.class)
    public void shouldNotCreateDirTwice() throws IOException {
        File file = new File(tempDir, "b-dir");
        file.mkdir();
        assertThat(file.exists(), equalTo(true));
        fileSystem.provider().createDirectory(fileSystem.getPath(file.getAbsolutePath()));
    }

    @Test(expectedExceptions = IOException.class)
    public void shouldNotCreateDirWhenParentDoesNotExist() throws IOException {
        File cDir = new File(tempDir, "c-dir");
        assertThat(cDir.exists(), equalTo(false));
        File file = new File(cDir, "child");
        assertThat(file.exists(), equalTo(false));
        fileSystem.provider().createDirectory(fileSystem.getPath(file.getAbsolutePath()));
    }

    @Test
    public void shouldDeleteFile() throws IOException {
        assertThat(testFile.exists(), equalTo(true));
        Path path = fileSystem.getPath(testFile.getAbsolutePath());
        fileSystem.provider().delete(path);
        assertThat(testFile.exists(), equalTo(false));
    }

    @Test
    public void shouldGetPath() {
        Path path = fileSystem.provider().getPath(URI.create("local:///first/second/third"));
        assertThat(path, equalTo(fileSystem.getPath("/first/second/third")));
    }

    @Test
    public void shouldCheckExistence() throws IOException {
        Path path = fileSystem.getPath(testFile.getAbsolutePath());
        assertThat(Files.exists(path), equalTo(true));
        Files.delete(path);
        assertThat(Files.exists(path), equalTo(false));
    }

    @Test
    public void shouldCheckReadAccess() {
        Path path = fileSystem.getPath(testFile.getAbsolutePath());
        assertThat(Files.isReadable(path), equalTo(true));
        testFile.setReadable(false);
        assertThat(Files.isReadable(path), equalTo(false));
    }

    @Test
    public void shouldCheckWriteAccess() {
        Path path = fileSystem.getPath(testFile.getAbsolutePath());
        assertThat(Files.isWritable(path), equalTo(true));
        testFile.setReadOnly();
        assertThat(Files.isWritable(path), equalTo(false));
    }

    @Test
    public void shouldCheckExecute() {
        Path path = fileSystem.getPath(testFile.getAbsolutePath());
        assertThat(Files.isExecutable(path), equalTo(false));
        testFile.setExecutable(true);
        assertThat(Files.isExecutable(path), equalTo(true));
    }

    @Test
    public void shouldCheckHiddenness() throws IOException {
        Path hiddenPath;
        if (((OverthereFileSystem) fileSystem).getConnection().getHostOperatingSystem() == OperatingSystemFamily.UNIX) {
            hiddenPath = fileSystem.getPath("/first/.foo");
        } else {
            File hidden = new File(tempDir, "hidden.txt");
            Files.setAttribute(hidden.toPath(), "dos:hidden", true);
            hiddenPath = fileSystem.getPath(hidden.getAbsolutePath());
        }
        assertThat(".foo should be hidden on unix", Files.isHidden(hiddenPath));
    }
}
