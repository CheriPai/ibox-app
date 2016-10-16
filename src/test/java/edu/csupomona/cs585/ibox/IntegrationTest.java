package edu.csupomona.cs585.ibox;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import edu.csupomona.cs585.ibox.sync.GoogleDriveFileSyncManager;
import edu.csupomona.cs585.ibox.sync.GoogleDriveServiceProvider;


public class IntegrationTest {

	private GoogleDriveFileSyncManager fileSyncManager;
	
	@Before
	public void setup() {
        fileSyncManager = new GoogleDriveFileSyncManager(GoogleDriveServiceProvider.get().getGoogleDriveClient());
	}

	@Test
	public void testAddFile() {
		String fileName = "/home/dat/Java/ibox-app/watch/test";
		java.io.File localFile = new java.io.File(fileName);
		try {
			localFile.createNewFile();
			fileSyncManager.addFile(localFile);
			assertTrue(fileSyncManager.fileExists(localFile.getName()));
			fileSyncManager.deleteFile(localFile);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			localFile.delete();
		}
	}
	
	@Test 
	public void testUpdateFile() {
		String fileName = "/home/dat/Java/ibox-app/watch/test";
		java.io.File localFile = new java.io.File(fileName);
		try {
			localFile.createNewFile();
			fileSyncManager.addFile(localFile);
			Long initialSize = fileSyncManager.getFileSize(localFile.getName());
			FileWriter fw = new FileWriter(localFile.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("test");
			bw.close();
			fileSyncManager.updateFile(localFile);
			Long modifiedSize = fileSyncManager.getFileSize(localFile.getName());
			assertThat(initialSize, not(modifiedSize));
			fileSyncManager.deleteFile(localFile);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			localFile.delete();
		}
	}
	
	@Test 
	public void testDeleteFile() {
		String fileName = "/home/dat/Java/ibox-app/watch/test";
		java.io.File localFile = new java.io.File(fileName);
		try {
			localFile.createNewFile();
			if (!fileSyncManager.fileExists(localFile.getName())) {
				fileSyncManager.addFile(localFile);
			}
			fileSyncManager.deleteFile(localFile);
			assertFalse(fileSyncManager.fileExists(localFile.getName()));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			localFile.delete();
		}
	}
}
