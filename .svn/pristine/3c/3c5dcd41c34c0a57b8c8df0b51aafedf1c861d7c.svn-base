package appli.controller.domaine.util_erp;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import framework.controller.ContextGloabalAppli;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;

@WebServlet("/resourcesCtrl")
public class ImageServlet extends HttpServlet {
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try{
			String elementId = EncryptionUtil.decrypt(req.getParameter("elmnt"));
			String module = req.getParameter("path");
			String fullPath = req.getParameter("fpath");
			File imageFile = null;
			
			if(StringUtil.isEmpty(fullPath)) {
				EtablissementPersistant etsBean = ContextGloabalAppli.getEtablissementBean();
				if(etsBean == null || etsBean.getId() == null || StringUtil.isEmpty(elementId)){
					try {
						resp.getWriter().write("");
					} catch (IOException e) {
					}
					return;
				}
				String path = StrimUtil.BASE_FILES_PATH+"/"+etsBean.getId().toString()+"/"+module+"/"+elementId;
				File imageFolder = new File(path);
				
				if(imageFolder.listFiles() != null){
					for (final File file : imageFolder.listFiles()) {
						imageFile = file;
						break;
					}
				}
				if(imageFile == null){
					try {
						resp.getWriter().write("");
					} catch (IOException e) {
					}
					return;
				}
			} else {
				imageFile = new File(fullPath);
			}
			
			int cacheAge = 50000;
			long expiry = new Date().getTime() + cacheAge *1000;
			resp.setDateHeader("Expires", expiry);
			resp.setHeader("Cache-Control", "max-age="+ cacheAge);
	
			if(imageFile != null){
				String docType = imageFile.getName().substring(imageFile.getName().lastIndexOf(".")+1); 
				BufferedImage bi = ImageIO.read(imageFile);
				if(bi != null && StringUtil.isNotEmpty(docType)){
					resp.setContentType("image/"+docType);
					ServletOutputStream os = resp.getOutputStream();
					ImageIO.write(bi, docType, os);
				}
			}
		} catch(Exception e){
			resp.getWriter().write("");
			System.out.println(e.getMessage());
		}
	}
}
