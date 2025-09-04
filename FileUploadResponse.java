package com.app.entiry;

public class FileUploadResponse {
    private String fileName;
    private String filePath;
    
    public FileUploadResponse(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
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
