package framework.model.util.printGen;

import java.util.ArrayList;
import java.util.List;

public class PrintHtmlUtil {
	public static String prinbterBeanToHtml(PrintPosBean printBean) {
		List<PrintPosDetailBean> listDetail = printBean.getListDetail();
		if(printBean == null || listDetail == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder("");
		int margin = -8;
		
		sb.append("<html>"
					+ "<head>"
					+ "<style>"
					+ "@page{" + 
					"margin-left: "+margin+"px;" + 
					"margin-right: "+margin+"px;" + 
					"margin-top: 0px;" + 
					"margin-bottom: -10px;" + 
					"}"
					+ "</style>"
					+ "</head>"
					+ "<body>"
						+ "<table style='width:100%;margin-left:0px;'>");
		
		int oldY = 0;
		
		List<Integer> listY = new ArrayList<>();
		for(PrintPosDetailBean printPosBean : listDetail){
			listY.add(printPosBean.getY());
		}
		
		int idx = 0;
		int idxLeftX = 0;
		
		for(PrintPosDetailBean printPosBean : listDetail){
				int colSpan = 3;
				int y = printPosBean.getY();
				int leftX = 0;
				int nextY = (idx < listDetail.size()-1 ? listY.get(idx+1) : y);
				//
    			if(oldY != y) {
    				if(oldY != 0) {
    					sb.append("</tr>");
    				}
    				sb.append("<tr style='height:15px;'>");
    			}
    			
    			if(oldY != y){
    				idxLeftX = 0;    				
    			}
    			
    			if((oldY != y && y != nextY)) {
    				colSpan = 3;
    			} else {
    				colSpan = 0;
    				
    				if(idxLeftX == 0) {
    					leftX = printPosBean.getX();
    					idxLeftX++;
    				}
    			}
    			
				if(printPosBean.getType().equals("T")){// Text
        				// Libellé
	        			int fontSize = printPosBean.getFont().getSize()+2;
	        			
	        			if(fontSize > 13) {
	        				fontSize = 13;
	        			}
	        			
						sb.append("<td colspan='"+colSpan+"' style='font-size:"+fontSize+"px;padding-left:"+leftX+"px;");
		        			if(printPosBean.getFont().isBold()) {
			        			sb.append("font-weight:bold;");
			        		}
			        		if("C".equals(printPosBean.getAlign())){// Center
			        			sb.append("text-align:center;");
			        		} else if("R".equals(printPosBean.getAlign())){// A droite
			        			sb.append("text-align:right;");
			        		}
			        		sb.append("'>"+printPosBean.getData());
		        		sb.append("</td>");
        		
        		} else if(printPosBean.getType().equals("I")){// Image
        		
        		} else if(printPosBean.getType().equals("LP")){// Ligne pointillée
        				sb.append("<td colspan='3'>");
        					sb.append("<hr></hr>");
    		        	sb.append("</td>");

        		} else{// Séparateur
    				sb.append("<td colspan='3'>");
    					sb.append("<hr></hr>");
		        	sb.append("</td>");
		        }
        		oldY = y;
        		idx++;
        	}
			sb.append("</tr>");
		sb.append("</table>"
			+ "<body>"
		+ "</html>");
		
		return sb.toString();
	}
}
