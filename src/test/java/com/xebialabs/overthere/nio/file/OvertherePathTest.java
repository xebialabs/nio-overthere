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

	private Path path;
    private Path root;

    @BeforeClass
	public void createFileSystem() throws IOException {
		// FIXME Waiting for new overthere...
//		fileSystem = FileSystems.newFileSystem(URI.create("overthere://overthere:overhere@overthere?os=UNIX&connectionType=SFTP"), Collections.<String, Object> emptyMap());
        myFileSystem = FileSystems.getFileSystem(URI.create("file:///"));
        fileSystem = FileSystems.newFileSystem(URI.create("local:///"), Collections.<String, Object>emptyMap());

        path = fileSystem.getPath("/first", "second", "third");
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
		assertThat(path, notNullValue());
	}

	@Test
	public void shouldCreateOvertherePath() {
		assertThat(path, instanceOf(OvertherePath.class));
	}

	@Test
	public void shouldGetNameCount() {
		assertThat(path.getNameCount(), equalTo(3));
	}
	
	@Test
	public void shouldGetParent() {
		assertThat(path.getParent().toString(), equalTo("/first/second"));
	}

	@Test
	public void shouldGetParentShouldEndUpAtRoot() {
		assertThat(path.getParent().getParent().getParent().toString(), equalTo("/"));
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
		assertThat(path.getRoot().toString(), equalTo("/"));
	}

	@Test
	public void shouldGetFileName() {
		assertThat(path.getFileName().toString(), equalTo("third"));
	}

	@Test
	public void shouldNotGetFileNameOfRoot() {
		assertThat(root.getFileName(), nullValue());
	}

	@Test
	public void shouldGetNameOfPath() {
		assertThat(path.getName(0).toString(), equalTo("first"));
		assertThat(path.getName(1).toString(), equalTo("second"));
		assertThat(path.getName(2).toString(), equalTo("third"));
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void shouldNotGetNameForNegativeIndex() {
		path.getName(-1);
		fail();
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void shouldNotGetNameForTooLargeIndex() {
		path.getName(3);
		fail();
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void shouldNotGetNameOfRoot() {
		root.getName(0);
		fail();
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void shouldNotGetSubpathForTooLowBeginIndex() {
		path.subpath(-1, 2);
		fail();
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void shouldNotGetSubpathForTooHighEndIndex() {
		path.subpath(0, 4);
		fail();
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void shouldNotGetSubpathForReversedBeginAndEndIndex() {
		path.subpath(3, 1);
		fail();
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void shouldNotGetSubpathForEmptyRange() {
		path.subpath(1, 1);
		fail();
	}

	@Test
	public void shouldGetSubpath() {
		assertThat(path.subpath(0, 3).toString(), equalTo("first/second/third"));
		assertThat(path.subpath(1, 3).toString(), equalTo("second/third"));
		assertThat(path.subpath(2, 3).toString(), equalTo("third"));
		assertThat(path.subpath(1, 2).toString(), equalTo("second"));
	}

    @Test
    public void shouldStartWithPath() {
        Path threeComponents = fileSystem.getPath("first", "second", "third");
        Path onMyFileSystem = myFileSystem.getPath("first", "second", "third");
        Path twoComponents = fileSystem.getPath("first", "second");
        Path twoComponentsAbsolute = fileSystem.getPath("/first", "second");
        Path lastTwoComponents = fileSystem.getPath("second", "third");
        assertThat(threeComponents.startsWith(twoComponents), equalTo(true));
        assertThat(path.startsWith(twoComponentsAbsolute), equalTo(true));
        assertThat(threeComponents.startsWith(threeComponents), equalTo(true));
        assertThat(path.startsWith(path), equalTo(true));
        assertThat(onMyFileSystem.startsWith(threeComponents), equalTo(false));
        assertThat(threeComponents.startsWith(onMyFileSystem), equalTo(false));
        assertThat(twoComponents.startsWith(threeComponents), equalTo(false));
        assertThat(path.startsWith(threeComponents), equalTo(false));
        assertThat(threeComponents.startsWith(lastTwoComponents), equalTo(false));
        assertThat(path.startsWith(threeComponents), equalTo(false));
    }

    @Test
    public void shouldStartWithString() {
        Path threeComponents = fileSystem.getPath("first", "second", "third");
        Path twoComponents = fileSystem.getPath("first", "second");
        assertThat(threeComponents.startsWith("first/second"), equalTo(true));
        assertThat(path.startsWith("/first/second"), equalTo(true));
        assertThat(threeComponents.startsWith("first/second/third"), equalTo(true));
        assertThat(path.startsWith("/first/second/third"), equalTo(true));
        assertThat(twoComponents.startsWith("first/second/third"), equalTo(false));
        assertThat(path.startsWith("first/second/third"), equalTo(false));
        assertThat(threeComponents.startsWith("second/third"), equalTo(false));
        assertThat(path.startsWith("first/second/third"), equalTo(false));
    }

    @Test
    public void shouldEndWithPath() {
        Path lastTwoComponents = fileSystem.getPath("second", "third");
        Path threeComponents = fileSystem.getPath("first", "second", "third");
        Path onMyFileSystem = myFileSystem.getPath("first", "second", "third");
        assertThat(path.endsWith(lastTwoComponents), equalTo(true));
        assertThat(threeComponents.endsWith(lastTwoComponents), equalTo(true));
        assertThat(path.endsWith(path), equalTo(true));
        assertThat(threeComponents.endsWith(threeComponents), equalTo(true));
        assertThat(path.endsWith(threeComponents), equalTo(true));
        assertThat(threeComponents.endsWith(path), equalTo(false));
        assertThat(onMyFileSystem.endsWith(threeComponents), equalTo(false));
        assertThat(threeComponents.endsWith(onMyFileSystem), equalTo(false));
    }

    @Test
    public void shouldEndWithString() {
        Path threeComponents = fileSystem.getPath("first", "second", "third");
        assertThat(path.endsWith("second/third"), equalTo(true));
        assertThat(threeComponents.endsWith("second/third"), equalTo(true));
        assertThat(path.endsWith("/first/second/third"), equalTo(true));
        assertThat(threeComponents.endsWith("first/second/third"), equalTo(true));
        assertThat(path.endsWith("first/second/third"), equalTo(true));
        assertThat(threeComponents.endsWith("/first/second/third"), equalTo(false));
    }

    @Test
    public void shouldNormalize() {
        assertThat(fileSystem.getPath("/", "..").normalize().toString(), equalTo("/"));
        assertThat(fileSystem.getPath("/", "first", ".").normalize().toString(), equalTo("/first"));
        assertThat(fileSystem.getPath("/", "first", "second/..").normalize().toString(), equalTo("/first"));
        assertThat(fileSystem.getPath("/", "first", "..", "second").normalize().toString(), equalTo("/second"));
        assertThat(fileSystem.getPath("/", "first", ".", "second").normalize().toString(), equalTo("/first/second"));
    }
}
