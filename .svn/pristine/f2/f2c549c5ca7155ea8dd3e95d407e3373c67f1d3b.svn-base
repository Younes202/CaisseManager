<%@page import="framework.model.common.constante.ProjectConstante.MSG_TYPE"%>
<%@page import="framework.model.common.service.MessageService"%>
<%@page import="framework.controller.bean.message.BannerMessageBean"%>
<%@page import="java.util.List"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>

<%
List<BannerMessageBean> listBannerMessage = MessageService.getListBannerMessageBean();
boolean isBannerError = (listBannerMessage != null && listBannerMessage.size() > 0);
%>

<style type="text/css">
.banner-class{
    border: 1px solid red;
    padding-top: 5px;
    padding-bottom: 5px;
    margin: 0px;
    margin-bottom: 5px;
    background-color: #ffe2ca;
   }
</style>

<div class="row banner-class" id="top_msg_banner" style="<%=isBannerError ? "":"display: none;" %>">
	<div class='col-md-12' id="top_msg_banner_det">
<%
	// List message
	if(isBannerError){
		for(BannerMessageBean messageBean : listBannerMessage){
			String message = "<span style='color:red;'><i class='fa-fw fa fa-times'></i>"+messageBean.getMessage()+"</span><br>";//ERROR
			if(messageBean.getType().equals(MSG_TYPE.INFO)){
				message = "<span style='color:#3276b1;'><i class='fa-fw fa fa-info'></i>"+messageBean.getMessage()+"</span><br>";
			} else if(messageBean.getType().equals(MSG_TYPE.SUCCES)){
				message = "<span style='color:green;'><i class='fa-fw fa fa-check'></i>"+messageBean.getMessage()+"</span><br>";	
			} else if(messageBean.getType().equals(MSG_TYPE.WARNING)){
				message = "<span style='color:#e8823a;'><i class='fa-fw fa fa-warning'></i>"+messageBean.getMessage()+"</span><br>";	
			}
		%>
			<%=message %>
	<%	}
	}
	%>
	</div>
</div>