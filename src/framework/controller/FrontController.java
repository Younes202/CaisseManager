package framework.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import framework.model.common.constante.ProjectConstante;

@SuppressWarnings("serial")
@WebServlet(displayName="front", name="FrontController", urlPatterns="/front")
public class FrontController extends HttpServlet{
	private final static Logger LOGGER = Logger.getLogger(FrontController.class);
	private static Map<String, String> MAPPING_MAP = null;

	static{
		MAPPING_MAP = new HashMap<String, String>();
		MAPPING_MAP.put("srvid", "/WEB-INF/commun/center/banner_message.jsp");
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map params = (Map)request.getAttribute(ProjectConstante.WORK_PARAMS);
		//
		if(params != null){
			String url = getUrl((String)params.get("frnturl"));
			forward(request, response, url);
		}

		return;
	}

	/**
	 * @param request
	 * @param response
	 * @param destination
	 */
	protected void forward(ServletRequest request, ServletResponse response, String destination) {
		try {
			RequestDispatcher dispatcher = request.getRequestDispatcher(destination);
			dispatcher.forward(request, response);
		} catch (Exception e) {
			LOGGER.error("Erreur : ", e);
		}
	}

	/**
	 * @param url
	 * @return
	 */
	private String getUrl(String url){
		String value = MAPPING_MAP.get(url);
		return (value == null) ? "/index.jsp" : value;
	}

}
