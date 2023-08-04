package framework.model.util.synchro;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import framework.controller.FileUtilController;
import framework.model.common.util.StrimUtil;

@Named
public class SynchroniseFileService {
	
	public static void writeResponseFile(HttpServletResponse response, String filePath, String fileName) {
		String urlCloud = ""+"/printCtrl";
		
		try{
			URL url = new URL(urlCloud);
			InputStream in = url.openStream();
            BufferedInputStream bis = new BufferedInputStream(in);
            FileOutputStream fos = new FileOutputStream(fileName);
            
	        byte[] data = new byte[1024];
	        int count;
	        while ((count = bis.read(data, 0, 1024)) != -1) {
	            fos.write(data, 0, count);
	        }
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	 public static void savePostedFile(HttpServletRequest request) {
		  String module = request.getParameter("md");
		  String localPth = StrimUtil._GET_PATH(module);
		  
		  try {
			// Gets file name for HTTP header
	        String fileName = request.getHeader("fileName");
	        File saveFile = new File(localPth + fileName);
	         
	        // prints out all header values
//	        Enumeration<String> names = request.getHeaderNames();
//	        while (names.hasMoreElements()) {
//	            String headerName = names.nextElement();
//	            System.out.println(headerName + " = " + request.getHeader(headerName));        
//	        }
	         
	        // opens input stream of the request for reading data
	        InputStream inputStream = request.getInputStream();
	         
	        // opens an output stream for writing file
	        FileOutputStream outputStream = new FileOutputStream(saveFile);
	         
	        byte[] buffer = new byte[100000];
	        int bytesRead = -1;
	         
	        while ((bytesRead = inputStream.read(buffer)) != -1) {
	            outputStream.write(buffer, 0, bytesRead);
	        }
	         
	        outputStream.close();
	        inputStream.close();
	         
		  } catch(Exception e) {
			  e.printStackTrace();
		  }
	  }
	 
	  public static void sendFileToUrl(String url, String codeFunc, String fileName, String path) {
//		  String path = StrimUtil.getGlobalConfigPropertie("data.path");
//		  String localPth = StrimUtil._GET_PATH(module);
		  final String EOL = "\r\n";
		  
		  try (FileInputStream file = new FileInputStream(fileName)) {
	            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
	            final String boundary = UUID.randomUUID().toString();
	            con.setDoOutput(true);
	            con.setRequestMethod("POST");
	            
				// give it 10 seconds to respond
			    con.setReadTimeout(FileUtilController.TIMEOUT_URL_CALL);
	            
	            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
	            try (OutputStream out = con.getOutputStream()) {
	                out.write(("--" + boundary + EOL +
	                    "Content-Disposition: form-data; name=\"file\"; " +
	                    "filename=\"" + fileName + "\"" + EOL +
	                    "Content-Type: application/octet-stream" + EOL + EOL)
	                    .getBytes(StandardCharsets.UTF_8)
	                );
	                byte[] buffer = new byte[128];
	                int size = -1;
	                while (-1 != (size = file.read(buffer))) {
	                    out.write(buffer, 0, size);
	                }
	                out.write((EOL + "--" + boundary + "--" + EOL).getBytes(StandardCharsets.UTF_8));
	                out.flush();
	                System.err.println(con.getResponseMessage());
	                int res = con.getResponseCode();
	                
	                if (res == HttpURLConnection.HTTP_OK) {
	                    System.err.println("Success!");
	                } else {
	                    System.err.printf("Failed %d\n", res);
	                }
	                
	            } catch (Exception e) {
					e.printStackTrace();
				} finally {
	                con.disconnect();
	            }
	        } catch (Exception e1) {
				e1.printStackTrace();
			}	
	  }

}
