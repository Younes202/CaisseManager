<%@page import="framework.controller.bean.message.NotifyMessageBean"%>
<%@page import="framework.controller.bean.message.GrowlMessageBean"%>
<%@page import="java.util.List"%>
<%@page import="framework.model.common.service.MessageService"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>

<%
	// List growl message
	List<GrowlMessageBean> listGrowlMessage = MessageService.getListGrowlMessageBean();
	if((listGrowlMessage != null) && (listGrowlMessage.size() > 0)){%>
	<script type="text/javascript">
		$(document).ready(function (){
	<%	for(GrowlMessageBean growlBean : listGrowlMessage) { %>
			showPostAlert("<%=growlBean.getTitle()%>", "<%=growlBean.getMessage() %>" , "<%=growlBean.getType().toString()%>");
		<%}%>
		});
		</script>
	<%}%>

	<!-- Show dialog box if message exist -->
	<std:dialogBox/>
	

<%
List<NotifyMessageBean> listNotifyMessage = MessageService.getListNotifyMessageBean();
if(listNotifyMessage != null){ 
	// Gerer les notifications
}
%>
