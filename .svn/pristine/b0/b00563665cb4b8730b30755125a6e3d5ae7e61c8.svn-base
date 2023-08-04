<%@page import="framework.model.common.service.MessageService"%>
<%@page import="framework.model.util.printGen.PrintHtmlUtil"%>
<%@page import="framework.model.util.printGen.PrintPosBean"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.StringUtil"%>

<script type="text/javascript">
$(document).ready(function (){

<%
if(request.getAttribute("LOAD_POINTAGE") != null){%>
	callExternalUrl("http://localhost:8001/cm-client?act=pointage");

<%} else if(MessageService.getGlobalMap().get("CURRENT_CAISSE") != null){
		PrintPosBean printBean = (PrintPosBean)request.getAttribute("PRINT_POS_BEAN");
		//
		if(printBean != null && ControllerUtil.getUserAttribute("IS_EMBDED_MOBILE_PRINTER", request) != null){
			String html = PrintHtmlUtil.prinbterBeanToHtml(printBean);
			%>
			androidPrint("<%=html%>");
			<%
		} else if(!StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CAISSE_PRINT"))){
				if(printBean != null && request.getAttribute("OPEN_DASH_ONLY") == null){
					String openDashParam = (request.getAttribute("OPEN_DASH") != null ? "&dash=1" : "");
					List<PrintPosBean> listPrintBean = (List<PrintPosBean>)request.getAttribute("PRINT_POS_LIST");
					Long userId = ContextGloabalAppli.getUserBean().getId();
				
					if(listPrintBean == null){
						listPrintBean = new ArrayList<>();
						listPrintBean.add(printBean);
					}
				
					for(PrintPosBean pos : listPrintBean){
						long time = System.currentTimeMillis();
						String printId = userId+"_"+time;
						request.getServletContext().setAttribute("CURR_PRINT_CMD_"+printId, printBean);
						%>
						callExternalUrl("http://localhost:8001/cm-client?act=print&elmnt=<%=printId %><%=openDashParam%>");
				<% }
				} else if(request.getAttribute("OPEN_DASH_ONLY") != null){
					String printers = (String)request.getAttribute("OPEN_DASH_ONLY");
					%>
					callExternalUrl("http://localhost:8001/cm-client?act=print&dashONL=<%=printers%>");
				<%}
			}
			if(request.getAttribute("SCREEN_COM") != null){%>
				callExternalUrl("http://localhost:8001/cm-client?act=screen&data=<%=request.getAttribute("SCREEN_COM")%>");
			<%}
	}%>

});
</script>