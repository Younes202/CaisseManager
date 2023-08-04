<%@page import="appli.controller.domaine.caisse.ContextAppliCaisse"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@page errorPage="/commun/error.jsp"%>

<%
String mnu = (String)ControllerUtil.getMenuAttribute("curMnu", request);
%>

<script type="text/javascript">
$(document).ready(function (){
	<%if(ControllerUtil.getMenuAttribute("clientId", request) == null){ %>	
	
	<%}%>
	$(document).off('click', "a[id^='lnk_det']");
	$(document).on('click', "a[id^='lnk_det']", function(){
		setTrContent($(this).attr("curr"), "<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.editTrHistorique")%>");
	});
	
	<%if(ContextAppliCaisse.getCaisseBean() != null){%>
		$("#left-div").hide();
	//	$("#div-sit").css("width", (screen.width-120)+"px").css("w", (screen.height-180)+"px").css("margin-left", "3%");
	<%} %>	
});
</script>

	<%if(ControllerUtil.getMenuAttribute("clientId", request) != null){ %>	
			<!-- Page Breadcrumb -->
		<div class="page-breadcrumbs breadcrumbs-fixed">
			<ul class="breadcrumb">
				<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
				<li>Fiche client</li>
				<li class="active">Situation</li>
			</ul>
		</div>
		<!-- /Page Breadcrumb -->
		<!-- Page Header -->
		<div class="page-header position-relative">
			<div class="header-title" style="padding-top: 4px;">
				<std:link classStyle="btn btn-default" action="pers.client.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
			</div>
			<!--Header Buttons-->
			<jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
			<!--Header Buttons End-->
		</div>
		<!-- /Page Header -->
		
		<div class="page-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<!-- widget grid -->
			<div class="widget">
				<div class="row">
			        <div class="col-lg-12 col-sm-12 col-xs-12">
			              <% request.setAttribute("curMnu", "situat"); %>
							<jsp:include page="/domaine/personnel/client_header_tab.jsp" />
			        </div>
			    </div>  
     <%} %> 
     
	<!-- widget grid -->
	<div id="div-sit" style="margin-top: 3px;margin-left: 4px;padding-right: 5px;overflow-x: hidden;overflow-y: auto;border: 2px solid green;border-radius: 9px;width: 127%;
    height: 100vh;">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
		
		<std:form name="data-form">
			<div class="widget-body">
				<div class="row">
					<std:link action="caisse-web.caisseWeb.init_situation" params="isSub=1" id="link_stutation" targetDiv="div_situation" style="display:none;" />
			        <div class="form-group">
			        	<%
			        	if(ControllerUtil.getMenuAttribute("clientId", request) == null){	
				        	if("cli".equals(mnu) && StringUtil.isTrueOrNull(ContextGloabalAppli.getGlobalConfig("RIGHT_SITUATION_CLIENT"))){ %> 
					       		<std:label classStyle="control-label col-md-2" value="Client" />
								<div class="col-md-2">
									<std:select required="true" name="elmentId" type="long" data="${listClient }" key="id" labels="nom;' ';prenom;' ';telephone;' ';cin;" width="100%" />
								</div>	
							<%}
			        	}%>
						<%if("socLivr".equals(mnu) && StringUtil.isTrueOrNull(ContextGloabalAppli.getGlobalConfig("RIGHT_SITUATION_SOC_LIVR"))){ %> 
							<std:label classStyle="control-label col-md-2" value="Société" />
							<div class="col-md-2">
								<std:select required="true" name="elmentId" type="long" data="${listSociete }" key="id" labels="nom" width="100%" />
							</div>	
						<%} %>
						<%if("livr".equals(mnu) && StringUtil.isTrueOrNull(ContextGloabalAppli.getGlobalConfig("RIGHT_SITUATION_LIVREUR"))){ %> 
							<std:label classStyle="control-label col-md-2" value="Livreur" />
							<div class="col-md-2">
								<std:select required="true" name="elmentId" type="long" data="${listLivreur }" key="id" labels="login" width="100%" />
							</div>	
						<%} %>
			       </div>
			   </div>
			   <div class="row">
			        <div class="form-group">
			        	<std:label classStyle="control-label col-md-2" value="Date début" />
			            <div class="col-md-2">
			                 <std:date name="dateDebut" value="${dateDebut }"/>
			            </div>
			            <div class="col-md-2" style="text-align: center;">
			            </div>
			            <std:label classStyle="control-label col-md-1" value="Date fin" />
			            <div class="col-md-2">
			                 <std:date name="dateFin" value="${dateFin }"/>
			            </div>
			            <div class="col-md-2">
			           	 	<std:button action="caisse-web.caisseWeb.init_situation" targetDiv="div_situation" params="isSub=1" value="Filtrer" classStyle="btn btn-primary" />
			           	 </div>	
			       </div>
			   </div>
				<div class="row" id="div_situation">
				</div>
			</div>
		</std:form>
	</div>
<%if(ControllerUtil.getMenuAttribute("clientId", request) != null){ %>	
	</div>
</div>
<%}%>