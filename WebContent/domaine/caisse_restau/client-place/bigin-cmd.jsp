<%@page import="framework.model.common.service.MessageService"%>
<%@page import="framework.controller.bean.message.GrowlMessageBean"%>
<%@page import="java.util.List"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>

<script type="text/javascript">
$(document).ready(function(){
	$("#div_welcom").css("height", ($(window).height())+"px");
	<%
	// List growl message
	List<GrowlMessageBean> listGrowlMessage = MessageService.getListGrowlMessageBean();
	if((listGrowlMessage != null) && (listGrowlMessage.size() > 0)){
		for(GrowlMessageBean growlBean : listGrowlMessage) { %>
			showPostAlert("<%=growlBean.getTitle()%>", "<%=growlBean.getMessage() %>" , "<%=growlBean.getType().toString()%>");
		<%}
	}
	MessageService.clearMessages();
	%>
	<%if(request.getAttribute("PAGE_JS") != null){%>
		<%=request.getAttribute("PAGE_JS")%>
	<%}%>
});
</script>
	
<div id="div_welcom" style="background-image: url(resources/restau/img/restaurant-691377_1920.jpg);position: absolute;width: 100%;height: 100%;top: 0px;background-size: 103%;">
	<std:button action="caisse-web.caisseWebClient.editCaisseMenu" params="isNcmd=1" targetDiv="main-div" classStyle="btn btn-danger" id="btn-commande" style="box-shadow: 15px 24px 38px 0px #9E9E9E;font-size: 48px;font-weight: bold;border-radius: 27px;height:160px;" value="PASSEZ VOTRE COMMANDE"/>
</div>