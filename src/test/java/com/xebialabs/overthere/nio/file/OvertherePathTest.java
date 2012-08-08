package com.xebialabs.overthere.nio.file;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.fail;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Collections;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class OvertherePathTest {

    FileSystem fileSystem;

    private FileSystem myFileSystem;

    private Path absolutePath;
    private Path relativePath;
    private Path root;
    private Path emptyPath;

    @BeforeClass
    public void createFileSystem() throws IOException {
        myFileSystem = FileSystems.getFileSystem(URI.create("file:///"));
        fileSystem = FileSystems.newFileSystem(URI.create("local:///"), Collections.<String, Object>emptyMap());

        absolutePath = fileSystem.getPath("/first", "second", "third");
        relativePath = fileSystem.getPath("first", "second", "third");
        emptyPath = fileSystem.getPath("");
        root = fileSystem.getPath("/");
    }

    @AfterClass
    public void closeFileSystem() throws IOException {
        fileSystem.close();
    }

    @Test
    public void shouldCreatePathsWithAbsoluteAndToStringChecks() {
        checkPath("", false, "");
        checkPath("/", true, "/");
        checkPath("first", false, "first");
        checkPath("first/second/third", false, "first", "second", "third");
        checkPath("first/second/third", false, "first/second", "third");
        checkPath("first/second/third", false, "first", "second/third");
        checkPath("/first", true, "/first");
        checkPath("/first/second/third", true, "/first", "second", "third");
        checkPath("/first/second/third", true, "/first/second", "third");
        checkPath("/first/second/third", true, "/first", "second/third");
        checkPath("/first/third", true, "/first", "", "third");
    }

    private void checkPath(String expectedPath, boolean expectedAbsolute, String first, String... more) {
        Path p = fileSystem.getPath(first, more);
        assertThat(p.toString(), equalTo(expectedPath));
        assertThat(p.isAbsolute(), equalTo(expectedAbsolute));
    }

    @Test
    public void shouldCreatePath() {
        assertThat(absolutePath, notNullValue());
    }

    @Test
    public void shouldCreateOvertherePath() {
        assertThat(absolutePath, instanceOf(OvertherePath.class));
    }

    @Test
    public void shouldGetNameCount() {
        assertThat(absolutePath.getNameCount(), equalTo(3));
    }

    @Test
    public void shouldGetParent() {
        assertThat(absolutePath.getParent().toString(), equalTo("/first/second"));
    }

    @Test
    public void shouldGetParentShouldEndUpAtRoot() {
        assertThat(absolutePath.getParent().getParent().getParent().toString(), equalTo("/"));
    }

    @Test
    public void shouldGetParentOfRoot() {
        assertThat(root.getParent(), nullValue());
    }

    @Test
    public void shouldGetRootOfRelativePath() {
        assertThat(fileSystem.getPath("foo").getRoot(), nullValue());
    }

    @Test
    public void shoulGetRootOfAbsolutePath() {
        assertThat(absolutePath.getRoot().toString(), equalTo("/"));
    }

    @Test
    public void shouldGetFileName() {
        assertThat(absolutePath.getFileName().toString(), equalTo("third"));
    }

    @Test
    public void shouldNotGetFileNameOfRoot() {
        assertThat(root.getFileName(), nullValue());
    }

    @Test
    public void shouldGetNameOfPath() {
        assertThat(absolutePath.getName(0).toString(), equalTo("first"));
        assertThat(absolutePath.getName(1).toString(), equalTo("second"));
        assertThat(absolutePath.getName(2).toString(), equalTo("third"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldNotGetNameForNegativeIndex() {
        absolutePath.getName(-1);
        fail();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldNotGetNameForTooLargeIndex() {
        absolutePath.getName(3);
        fail();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldNotGetNameOfRoot() {
        root.getName(0);
        fail();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldNotGetSubpathForTooLowBeginIndex() {
        absolutePath.subpath(-1, 2);
        fail();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldNotGetSubpathForTooHighEndIndex() {
        absolutePath.subpath(0, 4);
        fail();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldNotGetSubpathForReversedBeginAndEndIndex() {
        absolutePath.subpath(3, 1);
        fail();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldNotGetSubpathForEmptyRange() {
        absolutePath.subpath(1, 1);
        fail();
    }

    @Test
    public void shouldGetSubpath() {
        assertThat(absolutePath.subpath(0, 3).toString(), equalTo("first/second/third"));
        assertThat(absolutePath.subpath(1, 3).toString(), equalTo("second/third"));
        assertThat(absolutePath.subpath(2, 3).toString(), equalTo("third"));
        assertThat(absolutePath.subpath(1, 2).toString(), equalTo("second"));
    }

    @Test
    public void shouldStartWithPath() {
        Path onMyFileSystem = myFileSystem.getPath("first", "second", "third");
        Path twoComponents = fileSystem.getPath("first", "second");
        Path twoComponentsAbsolute = fileSystem.getPath("/first", "second");
        Path lastTwoComponents = fileSystem.getPath("second", "third");
        assertThat(relativePath.startsWith(twoComponents), equalTo(true));
        assertThat(absolutePath.startsWith(twoComponentsAbsolute), equalTo(true));
        assertThat(relativePath.startsWith(relativePath), equalTo(true));
        assertThat(absolutePath.startsWith(absolutePath), equalTo(true));
        assertThat(onMyFileSystem.startsWith(relativePath), equalTo(false));
        assertThat(relativePath.startsWith(onMyFileSystem), equalTo(false));
        assertThat(twoComponents.startsWith(relativePath), equalTo(false));
        assertThat(absolutePath.startsWith(relativePath), equalTo(false));
        assertThat(relativePath.startsWith(lastTwoComponents), equalTo(false));
        assertThat(absolutePath.startsWith(relativePath), equalTo(false));
    }

    @Test
    public void shouldStartWithString() {
        Path twoComponents = fileSystem.getPath("first", "second");
        assertThat(relativePath.startsWith("first/second"), equalTo(true));
        assertThat(absolutePath.startsWith("/first/second"), equalTo(true));
        assertThat(relativePath.startsWith("first/second/third"), equalTo(true));
        assertThat(absolutePath.startsWith("/first/second/third"), equalTo(true));
        assertThat(twoComponents.startsWith("first/second/third"), equalTo(false));
        assertThat(absolutePath.startsWith("first/second/third"), equalTo(false));
        assertThat(relativePath.startsWith("second/third"), equalTo(false));
        assertThat(absolutePath.startsWith("first/second/third"), equalTo(false));
    }

    @Test
    public void shouldEndWithPath() {
        Path lastTwoComponents = fileSystem.getPath("second", "third");
        Path onMyFileSystem = myFileSystem.getPath("first", "second", "third");
        assertThat(absolutePath.endsWith(lastTwoComponents), equalTo(true));
        assertThat(relativePath.endsWith(lastTwoComponents), equalTo(true));
        assertThat(absolutePath.endsWith(absolutePath), equalTo(true));
        assertThat(relativePath.endsWith(relativePath), equalTo(true));
        assertThat(absolutePath.endsWith(relativePath), equalTo(true));
        assertThat(relativePath.endsWith(absolutePath), equalTo(false));
        assertThat(onMyFileSystem.endsWith(relativePath), equalTo(false));
        assertThat(relativePath.endsWith(onMyFileSystem), equalTo(false));
    }

    @Test
    public void shouldEndWithString() {
        assertThat(absolutePath.endsWith("second/third"), equalTo(true));
        assertThat(relativePath.endsWith("second/third"), equalTo(true));
        assertThat(absolutePath.endsWith("/first/second/third"), equalTo(true));
        assertThat(relativePath.endsWith("first/second/third"), equalTo(true));
        assertThat(absolutePath.endsWith("first/second/third"), equalTo(true));
        assertThat(relativePath.endsWith("/first/second/third"), equalTo(false));
    }

    @Test
    public void shouldNormalize() {
        assertThat(fileSystem.getPath("/", "..").normalize().toString(), equalTo("/"));
        assertThat(fileSystem.getPath("/", "first", ".").normalize().toString(), equalTo("/first"));
        assertThat(fileSystem.getPath("/", "first", "second/..").normalize().toString(), equalTo("/first"));
        assertThat(fileSystem.getPath("/", "first", "..", "second").normalize().toString(), equalTo("/second"));
        assertThat(fileSystem.getPath("/", "first", ".", "second").normalize().toString(), equalTo("/first/second"));
    }

    @Test
    public void shouldResolvePaths() {
        assertThat(relativePath.resolve(absolutePath), equalTo(absolutePath));
        assertThat(absolutePath.resolve(relativePath), equalTo(fileSystem.getPath("/first/second/third/first/second/third")));
        assertThat(relativePath.resolve(relativePath), equalTo(fileSystem.getPath("first/second/third/first/second/third")));
        assertThat(absolutePath.resolve(fileSystem.getPath("")), equalTo(absolutePath));
    }

    @Test
    public void shouldResolveStrings() {
        assertThat(relativePath.resolve("/first/second/third"), equalTo(absolutePath));
        assertThat(absolutePath.resolve("first/second/third"), equalTo(fileSystem.getPath("/first/second/third/first/second/third")));
        assertThat(relativePath.resolve("first/second/third"), equalTo(fileSystem.getPath("first/second/third/first/second/third")));
        assertThat(absolutePath.resolve(""), equalTo(absolutePath));
    }

    @Test
    public void shouldResolveSiblingPaths() {
        assertThat(relativePath.resolveSibling(absolutePath), equalTo(absolutePath));
        assertThat(absolutePath.resolveSibling(relativePath), equalTo(fileSystem.getPath("/first/second/first/second/third")));
        assertThat(relativePath.resolveSibling(relativePath), equalTo(fileSystem.getPath("first/second/first/second/third")));
        assertThat(absolutePath.resolveSibling(emptyPath), equalTo(absolutePath.getParent()));
    }

    @Test
    public void shouldResolveSiblingStrings() {
        assertThat(relativePath.resolveSibling("/first/second/third"), equalTo(absolutePath));
        assertThat(absolutePath.resolveSibling("first/second/third"), equalTo(fileSystem.getPath("/first/second/first/second/third")));
        assertThat(relativePath.resolveSibling("first/second/third"), equalTo(fileSystem.getPath("first/second/first/second/third")));
        assertThat(absolutePath.resolveSibling(""), equalTo(absolutePath.getParent()));
    }

    @Test
    public void shouldOnlyRelativizeWhenBothAreAbsoluteOrNot() {
        try {
            absolutePath.relativize(relativePath);
            fail();
        } catch (IllegalArgumentException ok) {
            // ok
        }

        try {
            relativePath.relativize(absolutePath);
            fail();
        } catch (IllegalArgumentException ok) {
            // ok
        }
    }

    @Test
    public void shouldRelativizePaths() {
        assertThat(absolutePath.relativize(absolutePath), equalTo(emptyPath));
        assertThat(relativePath.relativize(relativePath), equalTo(emptyPath));
        assertThat(absolutePath.relativize(fileSystem.getPath("/first/fourth/fifth")), equalTo(fileSystem.getPath("../../fourth/fifth")));
        assertThat(absolutePath.relativize(fileSystem.getPath("/first/fourth")), equalTo(fileSystem.getPath("../../fourth")));
    }
}
