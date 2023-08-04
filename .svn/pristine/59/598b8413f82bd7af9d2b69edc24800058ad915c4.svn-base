<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli.TYPE_LIGNE_COMMANDE"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementOffrePersistant"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="java.util.Date"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="java.util.Random"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementPersistant"%>
<%@page import="java.util.List"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<%
List<CaisseMouvementPersistant> listCaisseMouvement = (List<CaisseMouvementPersistant>)request.getAttribute("listCaisseMouvement");
List<String> newIds = (List<String>)request.getAttribute("newIds");
String[] colors = {"yellow", "palegreen", "azure", "red", "black", "blue", "gray"};
%>

<script type="text/javascript">
	$(document).ready(function (){
		<%if(newIds != null && newIds.size() > 0){%>
			var cmds = '';
			<%for(String newId : newIds){
				 String refCmd = (newId.length()>12 ? newId.substring(12) : newId);
			%>
				cmds = cmds + '<h1 style="color:#e91e63;font-weight:bold;font-size:4em;"><i class="fa fa-arrow-right"></i> <%=refCmd%></h1>';
			
		  <%}%>
		$("#dialog_cmd_title").html(cmds);
		$("#dialog_alert_cmd").trigger("click");
		// close modal
		setTimeout(function(){ 	
			$("#dialog_cmd_close").trigger("click");
		}, 10000);
		
		setTimeout(function(){
			<%for(String newId : newIds){
				String refCmd = (newId.length()>12 ? newId.substring(12) : newId);
			%>
				$("#divCmd_<%=newId%>").attr("class", "cardCmd blink");
				setTimeout(function(){speak('Commande <%=refCmd %>')}, 3000); 
			<%
			}%>
		}, 10000); 
		setTimeout(function(){ 	
			$("div.blink").attr("class", "cardCmd");
		}, 25000);
		<%}%>
	});
</script>

<%
	if(newIds != null && newIds.size() > 0){
%>
	<embed src="resources/framework/sound/rington.wav" height="0px" width="0px" autoplay="true" style="position: absolute;left: -100px;top: -100px;">
<%
	}
%>		
<div class="row pricing-container" style="padding: 8px;padding-right: 16px;">
	  <%
	  	if(listCaisseMouvement.size() == 0){
	  %>
		<h2 style="text-align: center;color:red;margin-top: 10%;">Aucune commande disponible.</h2>
	<%
		} else{
	   
	     for(CaisseMouvementPersistant mvm : listCaisseMouvement){
	        String typeCmd = null;
	        if(ContextAppli.TYPE_COMMANDE.E.toString().equals(mvm.getType_commande())){
	            typeCmd = "A EMPORTER";
	        } else if(ContextAppli.TYPE_COMMANDE.P.toString().equals(mvm.getType_commande())){
	             typeCmd = "SUR PLACE";
	        } else{
	            typeCmd = "LIVRAISON";
	        }
	        String refTables = mvm.getRefTablesDetail();
	        String refCmd = (mvm.getRef_commande().length()>12 ? mvm.getRef_commande().substring(12) : mvm.getRef_commande());
	%>
   			<div class="cardCmd" id="divCmd_<%=mvm.getRef_commande()%>">
   				<span class="refCmdStyle"><%=refCmd %></span>
   				<%=StringUtil.isNotEmpty(refTables) ? "<i class='fa fa-cutlery'></i> "+refTables : "" %><hr>
   				<i style="font-size: 30px;color: green;" class="fa fa-fw fa-check-square-o"></i>
   				<span style="font-size: 20px;color:green;font-weight: bold;"><%=typeCmd %></span>
   			</div>
        <%}
     }%>
 </div>    