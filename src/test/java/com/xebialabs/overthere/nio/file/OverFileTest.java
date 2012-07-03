package com.xebialabs.overthere.nio.file;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystems;
import java.util.Collections;

import org.testng.annotations.Test;

public class OverFileTest {

	@Test
	public void shouldOpenConnection() throws IOException {
		FileSystems.newFileSystem(URI.create("overthere://overthere:overhere@overthere?os=UNIX&connectionType=SFTP"), Collections.<String, Object>emptyMap());
	}

}
