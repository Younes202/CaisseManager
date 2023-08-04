<%@page import="framework.model.common.service.FrameworkMessageService"%>
<%@page import="framework.model.common.service.MessageService"%>
<%@page import="org.apache.log4j.Logger"%>
<%@ page isErrorPage="true" import="java.io.*" %>

<%
	Logger LOGGER = Logger.getLogger(this.getClass());
	if(exception != null && LOGGER != null){
		LOGGER.error(exception.getMessage(), exception);
	}
%>

<div style="color: red;">
	<i class="fa fa-cogs" style="padding: 20px;font-size: 43px;"></i> ...
</div>

<%
MessageService.clearMessages();
FrameworkMessageService.clearMessages();
%>
