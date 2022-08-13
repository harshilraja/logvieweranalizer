package com.lti.log.app.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class LogViewerModel {

	private String application;
	
	private String environment;
	
	private String fileName;
	
	private LinkedHashMap<String, String> logFilesList;
	
	private List<String> fileNames;
	
	public LogViewerModel() {
		logFilesList = new LinkedHashMap<String, String>();
		logFilesList.put("sample.log", "sample.log");
		fileNames = new ArrayList<String>();
		fileNames.add("sample.log");
	}

	public List<String> getFileNames() {
		return fileNames;
	}

	public void setFileNames(List<String> fileNames) {
		this.fileNames = fileNames;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public LinkedHashMap<String, String> getLogFilesList() {
		return logFilesList;
	}

	public void setLogFilesList(LinkedHashMap<String, String> logFilesList) {
		this.logFilesList = logFilesList;
	}
	
	
	
}
