package edu.csupomona.cs585.ibox;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.google.api.services.drive.Drive;

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
	
	@Test public void testDeleteFile() {
		String fileName = "/home/dat/Java/ibox-app/watch/test";
		java.io.File localFile = new java.io.File(fileName);
		try {
			localFile.createNewFile();
			fileSyncManager.addFile(localFile);
			fileSyncManager.deleteFile(localFile);
			assertFalse(fileSyncManager.fileExists(localFile.getName()));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			localFile.delete();
		}
	}
}
