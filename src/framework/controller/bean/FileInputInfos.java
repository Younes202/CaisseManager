package framework.controller.bean;

import java.io.InputStream;

public class FileInputInfos {
	private InputStream fileContent;
	private String fileName;
	private String filePath;

	public InputStream getFileContent() {
		return fileContent;
	}

	public void setFileContent(InputStream fileContent) {
		this.fileContent = fileContent;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}