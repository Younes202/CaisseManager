package framework.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import framework.model.common.service.MessageIdsService;

public class BackCallController {
	private final static Logger LOGGER = Logger.getLogger(BackCallController.class);
    
    /**
     * @param request
     * @param response
     * @throws ServletException
     */
    public void uploadFile(HttpServletRequest request, HttpServletResponse response) {
		PrintWriter writer = null;
		InputStream is = null;

		try {
		    writer = response.getWriter();
		} catch (IOException e) {
			LOGGER.error("Erreur : ", e);
		}
		
		String fieldName = ControllerUtil.getParam(request, "fieldName");
		String fileName = request.getHeader("X-File-Name");
		try {
		    is = request.getInputStream();
		    
		    Map<String, byte[]> mapStream = (Map<String, byte[]>)ControllerUtil.getMenuAttribute(fieldName, request);
		    if(mapStream == null){
		    	mapStream = new HashMap<String, byte[]>();
		    	ControllerUtil.setMenuAttribute(fieldName, mapStream, request);
		    } 

			mapStream.put(fileName, IOUtils.toByteArray(is));
			
			// Return response
		    response.setStatus(HttpServletResponse.SC_OK);
		    writer.print("{success: true}");
		} catch (Exception e) {
		    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		    writer.print("{success: false}");
		    LOGGER.error("Error when uploading file : " + e);
		} finally {
		    try {
		        is.close();
		    } catch (Exception ignored) {
		    	ignored.fillInStackTrace();
		    }
		}
		
		writer.flush();
		writer.close();
	}

    /**
     * @param request
     */
    @SuppressWarnings("unchecked")
	public void deleteUploaded(HttpServletRequest request, HttpServletResponse response){
    	String fileName = ControllerUtil.getParam(request, "uplfname");
    	String fieldName = ControllerUtil.getParam(request, "fieldName");
    	Map<String, InputStream> mapStream = (Map<String, InputStream>)ControllerUtil.getMenuAttribute(fieldName, request);
    	//
		if(mapStream != null){
			mapStream.remove(fileName);
		}
    }
    
	/**
	 * @param request
	 * @param response
	 * @param context
	 */
	public void cleanQmsg(HttpServletRequest request, HttpServletResponse response){
		MessageIdsService.clearQuestionAction();
		MessageIdsService.clearConfirmIds();
	}
}
