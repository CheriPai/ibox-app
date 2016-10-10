package edu.csupomona.cs585.ibox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.Drive.Files.List;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import edu.csupomona.cs585.ibox.sync.GoogleDriveFileSyncManager;

public class GoogleDriveFileSyncManagerTest {
	
	private Drive mockedDrive;
	private GoogleDriveFileSyncManager fileSyncManager;
	private ByteArrayOutputStream outContent;
	
	@Before
	public void setup() {
		mockedDrive = mock(Drive.class);
		fileSyncManager = new GoogleDriveFileSyncManager(mockedDrive);
		outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
	}
	
	@After
	public void cleanup() {
		System.setOut(null);
	}
	
	@Test
	public void testGoogleDriveFileSyncManager() {
		assertNotNull(fileSyncManager.service);
	}
	
	@Test
	public void testAddFile() throws IOException {
		java.io.File localFile = new java.io.File("~/Java/ibox-app/watch/test");
		File file = new File();
		file.setId("test_id");

		Files files = mock(Files.class);
		Files.Insert insert = mock(Files.Insert.class);
		when(mockedDrive.files()).thenReturn(files);
		when(files.insert(Mockito.any(File.class), Mockito.any(FileContent.class))).thenReturn(insert);
		when(insert.execute()).thenReturn(file);
				
		fileSyncManager.addFile(localFile);

		verify(insert).execute();
		assertEquals("File ID: test_id\n", outContent.toString());
	}
	
	@Test(expected=IOException.class)
	public void testAddFileIOException() throws IOException {
		java.io.File localFile = new java.io.File("~/Java/ibox-app/watch/test");
		File file = new File();
		file.setId("test_id");

		Files files = mock(Files.class);
		Files.Insert insert = mock(Files.Insert.class);

		when(mockedDrive.files()).thenReturn(files);
		when(files.insert(Mockito.any(File.class), Mockito.any(FileContent.class))).thenReturn(insert);
		when(insert.execute()).thenThrow(new IOException());
				
		fileSyncManager.addFile(localFile);
	}
	
	@Test
	public void testGetFileId() throws IOException {
		File file = new File();
		file.setId("test_id");
		file.setTitle("test_title");

		FileList filelist = new FileList();
		java.util.List<File> list = new java.util.ArrayList<File>();
		list.add(file);
		filelist.setItems(list);

		Files files = mock(Files.class);
		List request = mock(List.class);

		when(mockedDrive.files()).thenReturn(files);
		when(files.list()).thenReturn(request);
		when(request.execute()).thenReturn(filelist);
		
		assertEquals("test_id", fileSyncManager.getFileId("test_title"));
	}

	@Test()
	public void testGetFileIdIOException() throws IOException {
		Files files = mock(Files.class);
		List request = mock(List.class);

		when(mockedDrive.files()).thenReturn(files);
		when(files.list()).thenReturn(request);
		when(request.execute()).thenThrow(new IOException());
		
		fileSyncManager.getFileId("test");
		assertEquals("An error occurred: java.io.IOException\n", outContent.toString());
	}
	
	@Test()
	public void testGetFileIdNull() throws IOException {
		File file = new File();
		file.setId("test_id");
		file.setTitle("test_title");

		FileList filelist = new FileList();
		java.util.List<File> list = new java.util.ArrayList<File>();
		list.add(file);
		filelist.setItems(list);

		Files files = mock(Files.class);
		List request = mock(List.class);

		when(mockedDrive.files()).thenReturn(files);
		when(files.list()).thenReturn(request);
		when(request.execute()).thenReturn(filelist);
		
		assertNull(fileSyncManager.getFileId("DNE"));
	}
}
