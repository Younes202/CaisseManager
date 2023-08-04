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

	<script type="text/javascript" src="resources/restau/js/agencement/jquery-ui.js"></script>
	<script type="text/javascript" src="resources/restau/js/agencement/jquery.canvasAreaDraw.js"></script>
		
<style>
.lib_table {
	width: 70px;
	padding: 0px !important;
	margin-top: 76% !important;
	font-size: 11px;
	text-align: center;
	color: #FF5722 !important;
}
#generic_modal_body{
	width: 930px;
	margin-left: -25%;
}
</style>

<%
boolean isReprise = "REP".equals(ControllerUtil.getUserAttribute("PLAN_MODE", request));
boolean isConfirmTransfert = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("SECURE_TRANSFERT_ART"));
boolean isTransfert = "TRA".equals(ControllerUtil.getUserAttribute("PLAN_MODE", request));
boolean isChangementTable = "CHNG_TAB".equals(ControllerUtil.getUserAttribute("PLAN_MODE", request));

boolean isServeurProfil = "SERVEUR".equals(ContextAppli.getUserBean().getOpc_profile().getCode());

String finalTables = "";

AgencementPersistant agencementBean = (AgencementPersistant)request.getAttribute("agencement");
if(agencementBean == null){
	agencementBean = new AgencementPersistant();
}


List<String> listTablesOccupee = (List<String>)request.getAttribute("listTableTemp");
String[] dataTable = StringUtil.getArrayFromStringDelim(agencementBean.getTable_coords(), ";");
if(dataTable == null){
	dataTable = new String[0];
}
%> 
<script type="text/javascript">
	var tableOccup = "";

<%
for(String data : dataTable){
	String[] dataDetail = StringUtil.getArrayFromStringDelim(data, ":");
	String tableRef = dataDetail[0];
	String tableLeft = dataDetail[1];
	String tableTop = (dataDetail.length>2) ? dataDetail[2]:null;
	
	if(listTablesOccupee.contains(tableRef)){
		finalTables = finalTables+";"+data;
		%>
		tableOccup = tableOccup + "|<%=tableRef%>";
		<%
	}
}

if(!isReprise){
	finalTables = agencementBean.getTable_coords();
}
%>
	tableOccup = tableOccup + "|";

	var oldDataTable = "<%=StringUtil.getValueOrEmpty(finalTables)%>";
	var oldDataCoin = "<%=StringUtil.getValueOrEmpty(agencementBean.getLimite_coords())%>";
	var tables = {};
	
	function restaureCoords() {
		$("#limite_coords").val(oldDataCoin);
		$('.canvas-area[data-image-url]').canvasAreaDraw({readOnly:true});

		if(oldDataTable != ''){
			var data = oldDataTable.split(";");
			for (var i = 0; i < data.length; i++) {
				var coord = data[i].split(":");
				if(coord[1] && coord[1] != null && coord[1] != ''){
					var newImg = cloneImage(coord[0], coord[1], coord[2], true);
					// Marquer comme occup�
					if(tableOccup.indexOf("|"+coord[0]+"|") != -1){
						newImg.css("background", "url(resources/restau/js/agencement/table_occupe_64.png) no-repeat");
					}
					
					updateDataTable(newImg);
				}
			}
		}
		return false;
	}

	function cloneImage(lib, left, top, isRestaure){
		var newImg = $("#img_zone_ori").clone(false);
		var d = new Date();
		var n = d.getMilliseconds();
		newImg.attr("id", "img_zone_" + n);
		newImg.show();

		if (isRestaure) {
			var txtField = newImg.find(".lib_table");
			txtField.val(lib);
			txtField.attr("readOnly", "readOnly").css("background", "transparent");
			txtField.show();
			
			top = top - 130;
			left = left-250;	
		}
		newImg.css("position", "absolute").css("left", left + "px").css("top",top + "px");
		
		//
		$("#img_div").append(newImg);
		
		return newImg;
	}
	
	function updateDataTable(comp) {
		var offset = comp.offset();
		var txt = comp.find(".lib_table").val() + ":" + offset.left + ":" + offset.top;

		tables[comp.attr("id")] = txt + ";";
		//
		manageTableCoords();
	}

	function manageTableCoords() {
		var tblAll = "";
		for ( var name in tables) {
			if (name != null && tables[name] != null) {
				tblAll += tables[name];
			}
		}
		$("#table_coords").text(tblAll);
	}
	
	$(document).ready(function() {
		
	  <%if(isConfirmTransfert && isTransfert){%>
	  	  setTimeout(function(){
			$("#div_auth").show(200);
		  }, 1000);
	  <%}%>

		init_keyboard_events();
		
		restaureCoords();
		
		$("div[id^='img_zone_']").click(function(){
			$(".error").remove();
<%-- 			if(<%=isReprise%> || $(this).css("background").indexOf("table_occupe_64.png") == -1){ --%>
				
				<% if(isReprise){%>
					var caissVal = ($("#caissePlan\\.id") && $("#caissePlan\\.id").length>0) ? $("#caissePlan\\.id").val():'';
					submitAjaxForm('<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.selectPlan")%>', 'cais='+caissVal+'&ref='+$(this).find("input").val(), $("#plndata-form"), $("#targ_link"));
				<%} else{%>
					submitAjaxForm('<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.selectPlan")%>', 'ref='+$(this).find("input").val(), $("#plndata-form"), $("#targ_link"));
				<%}	%>
// 			} else{
// 				alertify.error("<b>Cet emplacement est d&eacute;j&agrave; ocup&eacute;e.</b>");
// 			}
		});
	});
</script>

<!-- Page Body -->
<std:form name="plndata-form">
	<a href="javascript:void(0)" targetDiv="left-div" id="targ_link" style="display: none;"></a>
	<std:link action="caisse-web.caisseWeb.initPlan" targetDiv="generic_modal_body" style="display:none;" id="plan-lnk" />

	<div class="widget-header bordered-bottom bordered-blue">
		<span class="widget-caption">Plan restaurant
		<%
		if(isTransfert){%>
			<b style="color: blue;"> ** Transfert du client <%=ControllerUtil.getUserAttribute("CURR_TRA_CLI", request) %></b>
		<%} else if(isChangementTable){%>
			<b style="color: blue;"> ** Changement de la table <%=ControllerUtil.getUserAttribute("CURR_CHANG_TAB", request) %></b>
		<%} %>
		</span> [<span style="font-size: 11px;color: blue;">S&eacute;lectionner sur la carte OU saisissez une r&eacute;f&eacute;rence]</span>
		<div class="widget-buttons buttons-bordered" style="margin-bottom: 10px;">
       		<i class="fa fa-keyboard-o" style="font-size: 20px;"></i>         
       		<label>
                <input class="checkbox-slider toggle colored-blue" type="checkbox" id="keyboard-activator" style="display: none;">
                <span class="text"></span>
            </label>
         </div>
		<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
			<i class="fa fa-times"></i> Fermer
		</button>
	</div>
	<div class="widget-body">
		<div class="row">
			<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
		</div>
			
		<div class="row" style="margin-left: 0px;">
			<std:label classStyle="control-label col-md-1" value="Place" />
			<div class="col-md-2">
				<std:select name="emplacement" mode="std" type="long" data="${listAgencement }" key="id" width="100%" labels="emplacement" value="${agencement.id }" onChange="$('#plan-lnk').trigger('click');" />
			</div>
		<%if(isReprise && !isServeurProfil){ %>	
			<std:label classStyle="control-label col-md-1" value="Serveur" />
			<div class="col-md-2">
				<std:select name="serveur" mode="std" type="long" data="${listServeur }" key="id" width="100%" labels="login" value="${currServeurId }" onChange="$('#plan-lnk').trigger('click');" />
			</div>
			<std:label classStyle="control-label col-md-1" value="Caisse" />
			<div class="col-md-2"> 
				<std:select name="caissePlan.id" mode="std" type="long" data="${listCaisse }" key="id" labels="reference" value="${currCaisseId }" onChange="$('#plan-lnk').trigger('click');" width="100%" />
			</div>
		<%} %>	
			<std:label classStyle="control-label col-md-1" value="Table" />
			<div class="col-md-2">
				<std:text name="table-sasie" type="string" maxlength="15" style="width:100px;float: left;" />
				<std:link action="caisse-web.caisseWeb.selectPlan" targetDiv="left-div" params="is_saisie=1" classStyle="btn btn-default shiny icon-only green" icon="fa fa-check-square-o" />
			</div>		
		</div>
		
		<div id="img_zone_ori" style="background: url(resources/restau/js/agencement/table_64.png) no-repeat; height: 93px; width: 70px;display: none;cursor: pointer;">
			<input type="text" class="lib_table" style="display: none;">
		</div>
	
		<div class="row" style="margin-left: 20px;">
			<!--   *********************************la zone de dessin********************************************* -->
			<div class="row">
				<div id="img_div" style="padding-left: 50px;width: 30%;float: left;height: 0px;"></div>
				<textarea rows=3 name="table_coords" id="table_coords" style="display: none;"></textarea>
				<textarea rows=3 name="limite_coords" id="limite_coords" class="canvas-area input-xxlarge" data-image-url="resources/restau/js/agencement/zone-dessin.png" style="display: none;"></textarea>
			</div>
		</div>
			
		<%if(isConfirmTransfert && isTransfert){ %>
				<div class="row" style="margin-left: 0px;
								margin-right: 0px;
								display: none;
								margin-left: 0px;
							    margin-right: 0px;
							    background-color: #ffddac;
							    padding: 17px;
							    border-radius: 13px;" id="div_auth">
					<div class="col-md-12">
						<h3 style="margin-top: 0px;">Autorisation manager</h3>
					</div>
					<div class="col-md-12">
						<std:label classStyle="control-label col-md-4" value="Login" style="font-size: 19px;"/>&nbsp;
						<div class="col-md-8" style="margin-top: -15px;">
							<std:select forceWriten="true" name="unlockQte.login" type="long" style="width:100%;font-size: 25px;" data="${listUser }" key="id" labels="login" />
						</div>
					</div>
					<div class="col-md-12">
						<std:label classStyle="control-label col-md-4" value="Mot de passe" style="font-size: 19px;"/>&nbsp;
						<div class="col-md-8">
							<std:password forceWriten="true" name="unlockQte.password" placeholder="Mot de passe" type="string" style="width:140px;font-size: 18px;margin-top: -15px;" maxlength="80" />
						</div>
					</div>
				</div>
				<hr>
			<%} %>	
	</div>
	<!-- row   -->
<!-- /Page Body -->
</std:form>