<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="appli.controller.domaine.administration.bean.AgencementBean"%>
<%@page import="appli.model.domaine.administration.persistant.AgencementPersistant"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="java.util.List"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
.lib_table {
	width: 70px;
	padding: 0px !important;
	margin-top: 76% !important;
	font-size: 11px;
	text-align: center;
	color: #FF5722 !important;
}
.badgeTable{
    font-size: 20px !important;
    height: 32px !important;
    width: 32px !important;
    margin-left: 4px;
    margin-top: 4px;
}
.lib_style {
	color: black;
}
#div_plan tr{
	line-height: 37px;
}
</style>

<%
boolean isReprise = "REP".equals(ControllerUtil.getUserAttribute("PLAN_MODE", request));
boolean isTransfert = "TRA".equals(ControllerUtil.getUserAttribute("PLAN_MODE", request));
boolean isConfirmTransfert = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("SECURE_TRANSFERT_ART"));
boolean isServeurProfil = "SERVEUR".equals(ContextAppli.getUserBean().getOpc_profile().getCode());
AgencementPersistant agencementBean = (AgencementPersistant)request.getAttribute("agencement");
if(agencementBean == null){
	agencementBean = new AgencementBean();
}

List<String> listTablesOccupee = (List<String>)request.getAttribute("listTableTemp");
String[] dataTable = StringUtil.getArrayFromStringDelim(agencementBean.getTable_coords(), ";");
if(dataTable == null){
	dataTable = new String[0];
}
%> 
<script type="text/javascript">
	$(document).ready(function() {
	   <%if(isConfirmTransfert && isTransfert){%>
	   	  setTimeout(function(){
			$("#div_auth").show(200);
		  }, 1000);
	  <%}%>
	  
		$("#table_zone span").click(function(){
<%-- 			if(<%=isReprise%> || $(this).attr("class").indexOf("badge-darkpink") == -1){ --%>
				<% if(isReprise){%>
					var caissVal = ($("#caissePlan\\.id") && $("#caissePlan\\.id").length>0) ? $("#caissePlan\\.id").val():'';
					submitAjaxForm('<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.selectPlan")%>', 'cais='+caissVal+'&ref='+$(this).attr("ref"), $("#plndata-form"), $("#targ_link_plan"));
				<%} else{%>
					submitAjaxForm('<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.selectPlan")%>', 'ref='+$(this).attr("ref"), $("#plndata-form"), $("#targ_link_plan"));
				<%}	%>
				$("#close_modal_plan").trigger("click");
// 			} else{
// 				alertify.error("<b>Cette table est d&eacute;j&agrave; ocup&eacute;e.</b>");
// 			}
		});
	});
</script>

<!-- Page Body -->
<std:form name="plndata-form">
	<a href="javascript:void(0)" closepop="true" targetDiv="left-div" id="targ_link_plan" style="display: none;"></a>
	<std:link action="caisse-web.caisseWeb.initPlan" targetDiv="generic_modal_body" style="display:none;" id="plan-lnk" />

	<div class="widget-header bordered-bottom bordered-blue">
		<span class="widget-caption">Plan restaurant</span>
		<button type="button" id="close_modal_plan" class="btn btn-primary" data-dismiss="modal" style="margin-top: 2px;">
			<i class="fa fa-times"></i> Fermer
		</button>
	</div>
	<div class="widget-body">
		<div class="row">
			<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
		</div>
		
		<div class="row">
			<%
			if(isTransfert){%>
				<b style="color: blue;"> ** Transfert du client <%=ControllerUtil.getUserAttribute("CURR_TRA_CLI", request) %></b>
			<%}
			%>
		</div>
		
		<%if(isConfirmTransfert && isTransfert){ %>
				<div class="row" style="
								margin-left: 0px;
								margin-right: 0px;
								background-color: #ffddac;
							    padding: 17px;
							    border-radius: 13px;
								display: none;" id="div_auth">
					<div class="col-md-12">
						<h3>Autorisation manager</h3>
					</div>
					<div class="col-md-12">
						<std:label classStyle="control-label col-md-4" value="Login" style="font-size: 19px;"/>&nbsp;
						<div class="col-md-8">
							<std:select forceWriten="true" name="unlockQte.login" type="long" style="width:100%;font-size: 25px;" data="${listUser }" key="id" labels="login" />
						</div>
					</div>
					<div class="col-md-12">
						<std:label classStyle="control-label col-md-4" value="Mot de passe" style="font-size: 19px;"/>&nbsp;
						<div class="col-md-8">
							<std:password forceWriten="true" name="unlockQte.password" placeholder="Mot de passe" type="string" style="width:140px;font-size: 18px;" maxlength="80" />
						</div>
					</div>
				</div>
				<hr>
			<%} %>
			
		<div id="div_plan" class="row" style="margin-left: 0px;">
			<table style="width: 100%;">
				<tr>	
					<td><std:label classStyle="lib_style" value="Table" /></td>
					<td>
						<std:text name="table-sasie" type="string" maxlength="15" style="width:150px;float: left;" />
						<std:link action="caisse-web.caisseWeb.selectPlan" style="margin-top: -5px;" targetDiv="left-div" params="is_saisie=1" classStyle="btn btn-default shiny icon-only green" icon="fa fa-check-square-o" />
					</td>
				</tr>
				<tr>
					<td><std:label classStyle="lib_style" value="Place" /></td>
					<td><std:select name="emplacement" mode="std" type="long" style="width:90%;" data="${listAgencement }" key="id" labels="emplacement" value="${agencement.id }" onChange="$('#plan-lnk').trigger('click');" /></td>
				</tr>
		<%if(isReprise && !isServeurProfil){ %>	
			<tr>
				<td><std:label classStyle="lib_style" value="Serveur" /></td>
				<td><std:select name="serveur" mode="std" type="long" data="${listServeur }" key="id" style="width:90%;" labels="login" value="${currServeurId }" onChange="$('#plan-lnk').trigger('click');" /></td>
			</tr>
			<tr>	
				<td><std:label classStyle="lib_style" value="Caisse" /></td>
				<td><std:select name="caissePlan.id" mode="std" type="long" data="${listCaisse }" key="id" labels="reference" value="${currCaisseId }" onChange="$('#plan-lnk').trigger('click');" style="width:90%;" /></td>
			</tr>
		<%} %>	
		</table>
		</div>
		
		<h3>Tables</h3>
		<hr>
		<div class="row" style="margin-left: 20px;margin-top: 20px;" id="table_zone">
			<!--   *********************************la zone de dessin********************************************* -->
				<%
				for(String data : dataTable){
					String style = "badgeTable badge badge-primary";
					String[] dataDetail = StringUtil.getArrayFromStringDelim(data, ":");
					String tableRef = dataDetail[0];
					if(listTablesOccupee.contains(tableRef)){
						style = "badgeTable badge badge-darkpink";
					}
					%>
						<span style="width: 45% !important;float: left;" class="<%=style%>" ref="<%=tableRef %>"><%=tableRef %></span>
					<%
				}
				%>
		</div>
	</div>
	<!-- row   -->
<!-- /Page Body -->
</std:form>