package com.lti.log.app.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.lti.log.app.util.LogViewerHandler;

@Controller
public class IndexController {
	//private static String FILE_TO_WATCH = "/Users/sivaraja/Desktop/DevOps/logs";
	private static String FILE_TO_WATCH = "/home/test/logs";
	/*@RequestMapping("/")
	public String home(Map<String, Object> model) {
		model.put("message", "HowToDoInJava Reader !!");
		return "index";
	}*/
	
	@RequestMapping("/next")
	public String next(Map<String, Object> model) {
		model.put("message", "You are in new page !!");
		return "next";
	}
	/*
	@RequestMapping(value = "/")
	public String showHomePage(Model theModel) {
		theModel.addAttribute("employee", new Employee());
		return "home-page";
		
	}*/
	
	
	@RequestMapping(value = "/")
	public ModelAndView showLoggerPage(Model theModel) {
		LogViewerModel logViewerModel = new LogViewerModel();
		LogViewerHandler logViewerHandler = new LogViewerHandler();
		List<String> logFiles = logViewerHandler.LogViewerHandler(FILE_TO_WATCH);
		Map<String,String> logMap = new LinkedHashMap<String,String>();
		List<String> fileNames = new ArrayList<String>();
		for (String logFile : logFiles) {
			logMap.put(logFile, logFile);
			fileNames.add(FILE_TO_WATCH+"/"+logFile);
		}
		logViewerModel.setLogFilesList((LinkedHashMap<String, String>) logMap);
		logViewerModel.setFileNames(fileNames);
		theModel.addAttribute("logAnalizer", logViewerModel);
		ModelAndView modelAndView = new ModelAndView("logviewer");
		modelAndView.addObject("fileNames", fileNames);
		return modelAndView;
		
	}
	
	@RequestMapping(value = "/logFiles")
	public ModelAndView showDashboardPage(@ModelAttribute("logAnalizer") LogViewerModel logViewerModel, Model theModel) {
		
		LogViewerHandler logViewerHandler = new LogViewerHandler();
		String filePath = FILE_TO_WATCH + "/"+logViewerModel.getApplication() + "/" +logViewerModel.getEnvironment();
		List<String> logFiles = logViewerHandler.LogViewerHandler(filePath);
		Map<String,String> logMap = new LinkedHashMap<String,String>();
		List<String> fileNames = new ArrayList<String>();
		for (String logFile : logFiles) {
			logMap.put(logFile, logFile);
			fileNames.add(FILE_TO_WATCH+"/"+logViewerModel.getApplication()+"/"+logViewerModel.getEnvironment()+"/"+logFile);
		}
		logViewerModel.setLogFilesList((LinkedHashMap<String, String>) logMap);
		logViewerModel.setFileNames(fileNames);
		theModel.addAttribute("logAnalizer", logViewerModel);
		
		ModelAndView modelAndView = new ModelAndView("logviewer");
		modelAndView.addObject("fileNames", fileNames);
		return modelAndView;
		//return "logviewer";
	}
	
	@RequestMapping(value = "/processForm")
	public String showDashboardPage(@ModelAttribute("logAnalizer") Employee logViewerModel, Model theModel) {
		theModel.addAttribute("logAnalizer", logViewerModel);
		return "dashboard";
	}
	
	 /*
     * Download a file from 
     *   - inside project, located in resources folder.
     *   - outside project, located in File system somewhere. 
     */
    @RequestMapping(value="/download", method = RequestMethod.GET)
    public void downloadFile(@RequestParam  String fileName,HttpServletResponse response) throws IOException {
     
        File file = null;
         
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            file = new File(fileName);
         
        if(!file.exists()){
            String errorMessage = "Sorry. The file you are looking for does not exist";
            System.out.println(errorMessage);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
            outputStream.close();
            return;
        }
         
        String mimeType= URLConnection.guessContentTypeFromName(file.getName());
        if(mimeType==null){
            System.out.println("mimetype is not detectable, will take default");
            mimeType = "application/octet-stream";
        }
         
        System.out.println("mimetype : "+mimeType);
         
        response.setContentType(mimeType);
         
        /* "Content-Disposition : inline" will show viewable types [like images/text/pdf/anything viewable by browser] right on browser 
            while others(zip e.g) will be directly downloaded [may provide save as popup, based on your browser setting.]*/
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() +"\""));
 
         
        /* "Content-Disposition : attachment" will be directly download, may provide save as popup, based on your browser setting*/
        //response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
         
        response.setContentLength((int)file.length());
 
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
 
        //Copy bytes from source to destination(outputstream in this example), closes both streams.
        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }
 


}
