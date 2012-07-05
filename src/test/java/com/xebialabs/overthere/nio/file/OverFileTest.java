package com.xebialabs.overthere.nio.file;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Collections;

import org.testng.annotations.Test;

public class OverFileTest {

	@Test
	public void shouldOpenConnection() throws IOException {
		FileSystems.newFileSystem(URI.create("overthere://overthere:overhere@overthere?os=UNIX&connectionType=SFTP"), Collections.<String, Object>emptyMap());
	}

	@Test
	public void shouldGetRoot() throws IOException {
		FileSystem fileSystem = FileSystems.getFileSystem(URI.create("file:///"));
		Path ajvanerp = fileSystem.getPath("/Users", "ajvanerp");
		System.out.println(fileSystem.getPath("/Users", "ajvanerp").getName(0));
		System.out.println(fileSystem.getPath("/Users", "ajvanerp").getName(0).isAbsolute());
		System.out.println(fileSystem.getPath("Users", "ajvanerp").getName(-1));
		System.out.println(fileSystem.getPath("Users", "ajvanerp").getName(2));
		System.out.println(fileSystem.getPath("/Users", "ajvanerp").getNameCount());
		System.out.println(fileSystem.getPath("/Users", "..").getParent().getParent().getParent());
	}

}
