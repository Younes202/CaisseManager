package framework.controller;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import framework.controller.bean.FileInputInfos;
import framework.model.common.exception.ActionValidationException;
import framework.model.common.service.MessageService;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.StringUtil;

public class UploadFileUtil {
	public static final String UNLOAD = "0";
	public static final String UPLOAD = "1";
	public static final String IMAGE = "2";

	/**
	 * @param request
	 */
	public static void manageUploadFile(HttpServletRequest request) {
		if (!ControllerUtil.isAvailableConnexion(request)) {
			MessageService.addDialogMessage("Vous devez vous connecter avant de télécharger.");
			throw new ActionValidationException("");
		}
		String url = request.getParameter("upload");
		String fieldName = request.getParameter("fn");
		String fieldPath = request.getParameter("path");

		if (url.equals(UNLOAD)) {
			fieldName = fieldName.substring(fieldName.indexOf("_") + 1);
			String key = EncryptionUtil.encrypt(fieldName);
			Map<String, FileInputInfos> mapFiles = (Map<String, FileInputInfos>) ControllerUtil.getMenuAttribute("CAISSE_DOCUMENTS", request);
			if (mapFiles != null) {
				mapFiles.remove(key);
			}

			// Map pour savoir ce qu'on a supprimé
			String elementJointName = request.getParameter("fnm");
			Map<String, String> mapFilesUnloaded = (Map<String, String>) ControllerUtil
					.getMenuAttribute("CAISSE_UNLOADED_DOCUMENTS", request);
			if (mapFilesUnloaded == null) {
				mapFilesUnloaded = new HashMap();
				ControllerUtil.setMenuAttribute("CAISSE_UNLOADED_DOCUMENTS", mapFilesUnloaded, request);
			}
			mapFilesUnloaded.put(elementJointName, fieldName);
			return;
		} else if (url.equals(UPLOAD)) {
			Map<String, FileInputInfos> mapFiles = (Map<String, FileInputInfos>) ControllerUtil
					.getMenuAttribute("CAISSE_DOCUMENTS", request);
			if (mapFiles == null) {
				mapFiles = new HashMap<>();
				ControllerUtil.setMenuAttribute("CAISSE_DOCUMENTS", mapFiles, request);
			}

			fieldName = fieldName.substring(0, fieldName.indexOf("_"));
			String key = EncryptionUtil.encrypt(fieldName);

			try {
				List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
				for (FileItem item : items) {
					if (!item.isFormField()) {
						FileInputInfos inputInfos = new FileInputInfos();
						// Process form file field (input type="file").
						// String fieldName = item.getFieldName();
						String fileName = FilenameUtils.getName(item.getName());
						InputStream fileContent = item.getInputStream();

						inputInfos.setFileContent(fileContent);
						inputInfos.setFileName(key + "." + fileName.substring(fileName.indexOf(".") + 1));
						inputInfos.setFilePath(fieldPath);
						//
						mapFiles.put(key, inputInfos);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
	}

	/**
	 * @param request
	 * @param fileNameN
	 * @return
	 */
	public static FileInputInfos getFileInfos(HttpServletRequest request, String fileNameN, String path) {
		Map<String, FileInputInfos> mapFiles = (Map<String, FileInputInfos>) ControllerUtil.getMenuAttribute("CAISSE_DOCUMENTS", request);
		FileInputInfos fileInfo = null;

		if (mapFiles != null) {
			FileInputInfos fileInfos = mapFiles.get(fileNameN);
			if (fileInfos != null
					&& (StringUtil.isEmpty(fileInfos.getFilePath()) || fileInfos.getFilePath().equals(path))) {
				fileInfo = fileInfos;
				// Purge
				mapFiles.remove(fileNameN);
			}
		}

		return fileInfo;
	}

	/**
	 * @param request
	 * @param fileNameN
	 * @return
	 */
	public static byte[] getFileBytes(HttpServletRequest request, String fileNameN) {
		Map<String, FileInputInfos> mapFiles = (Map<String, FileInputInfos>) ControllerUtil.getMenuAttribute("CAISSE_DOCUMENTS", request);
		byte[] fileByte = null;

		if (mapFiles != null) {
			try {
				FileInputInfos fileInfos = mapFiles.get(fileNameN);
				if (fileInfos != null) {
					fileByte = IOUtils.toByteArray(fileInfos.getFileContent());
					// String extention =
					// fileInfos.getFileName().substring(fileInfos.getFileName().lastIndexOf(".")
					// + 1);
					// fileByte = scale(fileByte, IMG_WIDTH, IMG_HEIGHT);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Purge
			mapFiles.remove(fileNameN);
		}

		return fileByte;
	}

	/**
	 * @param imageFile
	 * @throws IOException
	 */
	public static void resizeImageWithHint(File imageFile, Integer width, Integer height) throws IOException {
		BufferedImage scaledImage = getScaledInstance(ImageIO.read(imageFile), width, height, RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);
		String docType = imageFile.getName().substring(imageFile.getName().lastIndexOf(".")+1);
		ImageIO.write(scaledImage, docType, imageFile);
	}

	private static BufferedImage getScaledInstance(BufferedImage img, int targetWidth, int targetHeight, Object hint,
			boolean higherQuality) {
		int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
		BufferedImage ret = (BufferedImage) img;
		
		int originalWidth = img.getWidth();
	    int originalHeight = img.getHeight();

	    int newWidth;
	    int newHeight;
	    if (originalWidth == 0 || originalHeight == 0
	            || (originalWidth == targetWidth && originalHeight == targetHeight)) {
	        return img;
	    }

	    double aspectRatio = (double) originalWidth / (double) originalHeight;
	    double boundaryAspect = (double) targetWidth / (double) targetHeight;

	    if (aspectRatio > boundaryAspect) {
	        newWidth = targetWidth;
	        newHeight = (int) Math.round(newWidth / aspectRatio);
	    } else {
	        newHeight = targetHeight;
	        newWidth = (int) Math.round(aspectRatio * newHeight);
	    }
	    targetHeight = newHeight;
	    targetWidth = newWidth;
	    
		int w, h;
		if (higherQuality) {
			// Use multi-step technique: start with original size, then
			// scale down in multiple passes with drawImage()
			// until the target size is reached
			w = img.getWidth();
			h = img.getHeight();
		} else {
			// Use one-step technique: scale directly from original
			// size to target size with a single drawImage() call
			w = targetWidth;
			h = targetHeight;
		}

		do {
			if (higherQuality && w > targetWidth) {
				w /= 2;
				if (w < targetWidth) {
					w = targetWidth;
				}
			}

			if (higherQuality && h > targetHeight) {
				h /= 2;
				if (h < targetHeight) {
					h = targetHeight;
				}
			}

			BufferedImage tmp = new BufferedImage(w, h, type);
			Graphics2D g2 = tmp.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
			g2.drawImage(ret, 0, 0, w, h, null);
			g2.dispose();

			ret = tmp;
		} while (w != targetWidth || h != targetHeight);

		return ret;
	}
}
