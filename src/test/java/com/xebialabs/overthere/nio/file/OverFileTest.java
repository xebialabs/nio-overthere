package com.xebialabs.overthere.nio.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

import org.testng.annotations.Test;

public class OverFileTest {

	@Test
	public void shouldOpenConnection() throws IOException {
	    FileSystem fs = FileSystems.newFileSystem(URI.create("ssh://overthere:overhere@overthere?os=UNIX&connectionType=SFTP"), Collections.<String, Object>emptyMap());
		try {
		    Path p = fs.getPath("/home", "overthere", "file.txt");

            if (false) {
                System.out.println(p);
                BufferedReader r = Files.newBufferedReader(p, Charset.forName("UTF-8"));
                try {

                } finally {
                    r.close();
                }
            }

            if (true) {
                Path d = p.resolveSibling("dir");
                Files.createDirectory(d);
            }
		} finally {
		    fs.close();
		}
		
	}

	//@Test
	public void shouldGetRoot() throws IOException {
		FileSystem fileSystem = FileSystems.getFileSystem(URI.create("file:///"));
		Path ajvanerp = fileSystem.getPath("/Users", "ajvanerp");
        System.out.println(fileSystem.getPath("/foo", "bar", "..", "..", "..").getRoot());
        System.out.println(ajvanerp.getRoot());
        System.out.println(fileSystem.getPath("foo/bar").getRoot());
        System.out.println(ajvanerp.endsWith("ajvanerp"));
//		System.out.println(fileSystem.getPath("/Users", "ajvanerp").getName(0));
//		System.out.println(fileSystem.getPath("/Users", "ajvanerp").getName(0).isAbsolute());
		System.out.println(fileSystem.getPath("Users", "ajvanerp").endsWith(ajvanerp));
//		System.out.println(fileSystem.getPath("Users", "ajvanerp").getName(2));
//		System.out.println(fileSystem.getPath("/Users", "ajvanerp").getNameCount());
//		System.out.println(fileSystem.getPath("/Users", "..").getParent().getParent().getParent());
	}

}
