package com.xebialabs.overthere.nio.file;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

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

	private Path path;

	private Path root;

	@BeforeClass
	public void createFileSystem() throws IOException {
		fileSystem = FileSystems.newFileSystem(URI.create("overthere://overthere:overhere@overthere?os=UNIX&connectionType=SFTP"), Collections.<String, Object> emptyMap());
		path = fileSystem.getPath("/first", "second", "third");
		root = fileSystem.getPath("/");
	}

	@AfterClass
	public void closeFileSystem() throws IOException {
		fileSystem.close();
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
	public void shouldBeAbsolute() {
		
	}
	@Test
	public void shouldToString() {
		assertThat(path.toString(), equalTo("/first/second/third"));
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
	public void shouldGetParentOfRoot() {
		assertThat(root.getParent(), nullValue());
	}

}
