package appli.controller.util_ctrl;

import java.io.File;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class ZipUtil4 {
	private String password;
	private String outputPath;
	private String inputPath;
	private boolean isFolder;

	public ZipUtil4(String password, String inputPath, String outputPath, boolean isFolder) {
	    this.password = password;
	    this.outputPath = outputPath;
	    this.inputPath = inputPath;
	    this.isFolder = isFolder;
	}

	public void pack() throws ZipException {
		ZipParameters zipParameters = new ZipParameters();
		zipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
		zipParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_ULTRA);
		zipParameters.setEncryptFiles(true);
		zipParameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
		zipParameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
		zipParameters.setPassword(password);
		
		
		ZipFile zipFile = new ZipFile(outputPath); 
		
		if(isFolder) {
			zipFile.addFolder(new File(inputPath), zipParameters);	
		} else {
			zipFile.addFile(new File(inputPath), zipParameters);
		}
		
	}

	public void unpack() throws ZipException {
		ZipFile zipFile = new ZipFile(inputPath);

		if (zipFile.isEncrypted()) {
			zipFile.setPassword(password);
		}

		zipFile.extractAll(outputPath);
	}
}
