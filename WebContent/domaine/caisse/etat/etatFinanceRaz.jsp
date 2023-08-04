<%@page import="framework.model.common.service.MessageService"%>
<%@page import="framework.controller.bean.message.GrowlMessageBean"%>
<%@page import="java.util.List"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli.APPLI_ENV"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<!-- Page Breadcrumb -->
<%
String env = (String) ControllerUtil.getUserAttribute("CURRENT_ENV", request);
if( !env.equals(APPLI_ENV.cais.toString()) ) { %>
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Caisse enregistreuse</li>
         <li>Suivi</li>
         <li class="active">RAZ</li>
     </ul>
 </div>
<%}

if( !env.equals(APPLI_ENV.cais.toString()) ) { %>
<div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">      
		<std:link classStyle="btn btn-default" action="caisse.razPrint.init_raz_bo" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la RAZ" />
      </div>
      <!--Header Buttons-->
      <jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
      <!--Header Buttons End-->
  </div>
  <!-- /Page Header -->
<%} %>
<div class="page-body">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>
	
	<!-- widget grid -->
	<div class="widget">
         <div class="widget-body">
			<div class="row" style="margin-left: 5%;">
				<%if(request.getAttribute("rapport") != null){ %>
					<%=request.getAttribute("rapport") %>
					<br>
				<%} %>	
				<%=StringUtil.getValueOrEmpty(request.getAttribute("html_data")) %>
			</div>
		</div>		
	</div>
</div>

<script type="text/javascript">
	$(document).ready(function (){
<%// List growl message
List<GrowlMessageBean> listGrowlMessage = MessageService.getListGrowlMessageBean();
if((listGrowlMessage != null) && (listGrowlMessage.size() > 0)){
	for(GrowlMessageBean growlBean : listGrowlMessage) {%>
		showPostAlert("<%=growlBean.getTitle()%>", "<%=growlBean.getMessage()%>" , "<%=growlBean.getType().toString()%>");
	<%}
}
MessageService.clearMessages();%>
});
</script>