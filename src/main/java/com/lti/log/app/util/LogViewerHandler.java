package com.lti.log.app.util;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.atmosphere.cpr.AtmosphereConfig;
import org.atmosphere.cpr.AtmosphereHandler;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.AtmosphereServletProcessor;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.json.simple.JSONValue;

/**
 * @author luciano - luciano@aestasit.com
 */
public class LogViewerHandler implements AtmosphereHandler,  AtmosphereServletProcessor {

    private static String FILE_TO_WATCH = "/Users/sivaraja/Desktop/DevOps/logs";
    private BroadcasterFactory broadcasterFactory;

    private static List<String> watchableLogs = new ArrayList<String>();
    
    private static List<String> filesLogs = new ArrayList<String>();
    
	public static String getFileToWatch() {
		
		return FILE_TO_WATCH;
	}
	
	public static String getFileToWatchFolder(String folderName) {
		System.out.println(folderName);
		return FILE_TO_WATCH;
	}

	public static List<String> getFilesLogs() {
		return filesLogs;
	}

	public static void setFilesLogs(List<String> filesLogs) {
		LogViewerHandler.filesLogs = filesLogs;
	}

	public static List<String> getWatchableLogs() {
		return watchableLogs;
	}

	public static void setWatchableLogs(List<String> watchableLogs) {
		LogViewerHandler.watchableLogs = watchableLogs;
	}

	@Override
	public void init(AtmosphereConfig config) throws ServletException {
		this.broadcasterFactory = config.getBroadcasterFactory();
		
	}
	
	 public LogViewerHandler() {

	        final File logsDir = new File(FILE_TO_WATCH);

	        if (logsDir.exists() && logsDir.isDirectory()) {

	            File[] logs = logsDir.listFiles();
	            for (File f : logs) {
	                if (f.getName().endsWith(".log")) {
	                    watchableLogs.add(f.getName());
	                }
	                if(f.isDirectory()) {
	                	filesLogs.add(f.getName());
	                }
	            }
	        } else {
	            System.out.println("either logsDir doesn't exist or is not a folder");
	        }

	         System.out.println("log count: " + watchableLogs.size());
	    }

    public List<String> LogViewerHandler(String filePath) {
    	
    	
    	List<String> fileName = new ArrayList<String>();
    	//List<String> logFileName = new ArrayList<String>();
        final File logsDir = new File(filePath);

        if (logsDir.exists() && logsDir.isDirectory()) {

            File[] logs = logsDir.listFiles();
            for (File f : logs) {
                //if (f.getName().endsWith(".log")) {
                	fileName.add(f.getName());
                //}
            }
        } else {
            System.out.println("either logsDir doesn't exist or is not a folder");
        }

         System.out.println("log count: " + watchableLogs.size());
         return fileName;
    }

    @Override
    public void onRequest(final AtmosphereResource event) throws IOException {

        HttpServletRequest req = event.getRequest();//req.getContextPath(); req
        HttpServletResponse res = event.getResponse();
        res.setContentType("text/html");
        res.addHeader("Cache-Control", "private");
        res.addHeader("Pragma", "no-cache");
        
        Broadcaster broadcaster = getBroadcaster(event);

        if (req.getMethod().equalsIgnoreCase("POST")) {
            final String postPayload = req.getReader().readLine();
            String filePath = null;
            if(postPayload != null && postPayload.startsWith("file=")) {
            	filePath = postPayload.split("=")[1];
            	filePath = URLDecoder.decode(filePath, "UTF-8");
            	if(filePath != null) {
            		File logsDir = new File(filePath);
            		System.out.println("log path: " + logsDir.getAbsolutePath());
            		if(logsDir.isDirectory()) {
            			FILE_TO_WATCH = filePath;
            			File[] logs = logsDir.listFiles();
            			watchableLogs.removeAll(watchableLogs);
            			for (File f : logs) {
                            if (f.getName().endsWith(".log")) {
                                watchableLogs.add(f.getName());
                            }
                            
            			}
            		}else {
            		((LogViewerBroadcaster) broadcaster).startTailer(filePath);
            		broadcaster.broadcast(asJson("filename", filePath));
            		}
            	}
            }
        }
        res.getWriter().flush();
    }

	private Broadcaster getBroadcaster(final AtmosphereResource resource) {
		String fileId = getIdFile(resource);
		Broadcaster broadcaster = broadcasterFactory.lookup("/log-viewer/" + fileId);
		if(broadcaster == null) {
			broadcaster = broadcasterFactory.get("/log-viewer/" + fileId);
		}

		broadcaster.addAtmosphereResource(resource);
		return broadcaster;
	}
	
    private String getIdFile(AtmosphereResource resource) {
    	HttpServletRequest req = resource.getRequest();
    	String path = req.getContextPath() + req.getServletPath() + "/";
    	String uri = req.getRequestURI();
    	return uri.replace(path, "");
    }

    @Override
    public void onStateChange(final AtmosphereResourceEvent event) throws IOException {

        HttpServletResponse res = event.getResource().getResponse();
        if (event.isResuming()) {
            res.getWriter().write("Atmosphere closed<br/>");
            res.getWriter().write("</body></html>");
        } else {
            if (event.getMessage() != null) {
                res.getWriter().write(event.getMessage().toString());
            }
            else {
                //res.getWriter().write("");
            }
        }

        res.getWriter().flush();
    }

    @Override
    public void destroy() {
    }

    protected String asJson(final String key, String value) {
    	value = JSONValue.escape(value);
        return "{\"" + key + "\":\"" + value + "\"}";
    }

    protected String asJsonArray(final String key, final List<String> list) {

        return ("{\"" + key + "\":" + JSONValue.toJSONString(list) + "}");
    }

}
