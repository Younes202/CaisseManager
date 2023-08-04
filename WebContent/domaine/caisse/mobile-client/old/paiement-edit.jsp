<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementPersistant"%>
<%@page import="appli.controller.domaine.caisse.ContextAppliCaisse"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="appli.model.domaine.fidelite.persistant.CarteFideliteClientPersistant"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<%
boolean isPoint = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("PAIE_POINT"));
boolean isPortefeuille = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("PAIE_PORTEFEUILLE"));

CarteFideliteClientPersistant carteClientP = null;
BigDecimal mttPointUtilisable = null;
BigDecimal mttPortefeuilleUtilisable = null;
//
if(isPoint){
	carteClientP = (CarteFideliteClientPersistant)request.getAttribute("carteClientP");
	mttPointUtilisable = (BigDecimal)request.getAttribute("mttPointsUtilisable");
}
if(isPortefeuille){
	mttPortefeuilleUtilisable = (BigDecimal)request.getAttribute("mttPortefeuilleUtilisable");
}

BigDecimal mttTotal = (BigDecimal)request.getAttribute("mtt_total");
boolean isSoldeNegatif = request.getAttribute("isSoldeNegatif") != null;
CaisseMouvementPersistant CURRENT_COMMANDE = (CaisseMouvementPersistant) ControllerUtil.getUserAttribute("CURRENT_COMMANDE", request);
BigDecimal soldePortefeuille = (CURRENT_COMMANDE.getOpc_client() != null ? CURRENT_COMMANDE.getOpc_client().getSolde_portefeuille() : null);
%>

<script type="text/javascript">
var mttTotalCmd = <%=mttTotal%>;

$(document).ready(function (){
<%BigDecimal fraisLivrason = BigDecimalUtil.get(ContextGloabalAppli.getGlobalConfig(ContextAppli.PARAM_APPLI_ENUM.FRAIS_LIVRAISON.toString()));
boolean isFraisLivraison = !BigDecimalUtil.isZero(fraisLivrason);%>	

<%if(CURRENT_COMMANDE != null && StringUtil.isNotEmpty(CURRENT_COMMANDE.getType_commande())){%>
	manageTypeCmd($("#div-type a[v='<%=CURRENT_COMMANDE.getType_commande()%>']"));
<%}%>


	$("#div-type a").click(function(){
		manageTypeCmd($(this));
	});
	
	$("#div_mode_paie a").click(function(){
		$("#div_mode_paie a").attr("class", "mode-paie");
		$(this).attr("class", "mode-paie-sel");
		
		if($(this).attr("v") == 'CARTE'){
			$("#mtt_portefeuille, #mtt_especes").val('');
			$('#cb_div').show(1000);
		} else{
			$("#mtt_carte, mtt_portefeuille").val('');
			$('#cb_div').hide(1000);			
		}
		$('#mode_paie').val($(this).attr("v"));
	});
	
	$("#div-detail-paie a").click(function(){
		$(this).closest("tr").find("input").val("");
	});
});
function getValNum(elementId){
	return $("#"+elementId).val() ? parseFloat($("#"+elementId).val()) : 0;
}
function getValNumFormated(val){
	return (val && $.trim(val)!='') ? parseFloat(val).toFixed(2) : "0.00";
}
function manageTypeCmd(selectorTypeCmd){
	$("#div-type a").attr("class", "type-btn-paie");
	selectorTypeCmd.attr("class", "type-btn-paie-sel");
	//
	$("#typeCmd").val(selectorTypeCmd.attr("v"));
	
	if(selectorTypeCmd.attr("v") == 'L'){
		<%-- Recalculer la livraison --%>
		if(<%=isFraisLivraison%>){
			if($("#fraisLiv").val() != "1"){
				mttTotalCmd = mttTotalCmd + <%=fraisLivrason%>;
				$("#span_total").text(getValNumFormated(mttTotalCmd));
				$("#fraisLiv").val("1");
			}
		}
	} else{
		if(<%=isFraisLivraison%> && $("#fraisLiv").val()=="1"){
			mttTotalCmd = mttTotalCmd - <%=fraisLivrason%>;
			$("#span_total").text(getValNumFormated(mttTotalCmd));
			$("#fraisLiv").val("");
		}
	}
}
</script>

	<!-- Page Header -->
	<std:form name="data-form">
		<input type="hidden" id="typeCmd" name="typeCmd">
		<input type="hidden" id="fraisLiv" name="fraisLiv">
		<input type="hidden" id="mode_paie" name="mode_paie">
		
	     <div class="widget-header bordered-bottom bordered-blue" style="padding-bottom: 5px; padding-top: 5px; padding-right: 5px; ">
            <span class="widget-caption">
            	<%
            		if(carteClientP != null){
            	%>
	            	| Fidélité : <span style="font-size: 12px;color:orange;font-weight: normal;"><%=BigDecimalUtil.formatNumberZero(carteClientP.getMtt_total())%> Dhs</span>
            <%
            	}
                 if(!BigDecimalUtil.isZero(soldePortefeuille)){
            %>
            		| Réserve : <span style="font-size: 12px;color:orange;font-weight: normal;"><%=BigDecimalUtil.formatNumberZero(soldePortefeuille)%> Dhs</span>
            <%
                  }
            %>
            </span>	
			
         	<button type="button" id="closeMd" data-dismiss="modal" class="btn btn-primary" data-dismiss="modal" style="margin-top: -3px;margin-right: -2px;">
				<i class="fa fa-times"></i> Fermer
			</button>            
         </div>
		<div class="row" style="margin-left: 0px;margin-right: 0px;">
			<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
		</div>
         <div class="widget-body">
         	<div class="row" style="margin-left: 0px;margin-right: 0px;margin-top: -7px;text-align: center;">
       	         <span id="span_total" style="font-size: 3em;font-weight: bold;color: black;"><%=BigDecimalUtil.formatNumber(mttTotal) %> 
       				<span style="font-size: 0.5em">MAD</span>
       			</span>
       			<span id="span_total_new" style="width: 165px;height: 40px;font-size: 30px;font-weight: bold;text-align: right;color:fuchsia;display: none;">res</span>
         		<hr style="margin: 0px;">
         	</div>
         	<div class="row" style="margin-left: 0px;margin-right: 0px;">
         	
         		<c:if test="${dateEstimee != null }">
         			<h3>Date préparation estimée : ${dateEstimee }</h3>
         		</c:if>
         		
         		<!-- Type -->
         		<h2>Type de commande :</h2>
         		<div style="width: 103%;float: left;text-align: center;margin-bottom: 33px;margin-top: 6px;" id="div-type">
         			<a href="javascript:void(0);" class="type-btn-paie" v='P'>Sur place</a>
         			<a href="javascript:void(0);" class="type-btn-paie" v='E'>A emporter</a>
         			<a href="javascript:void(0);" class="type-btn-paie" v='L'>Livraison</a>
         		</div>

				<h2 style="text-align: left;">Date souhaitée</h2>         		
         		<div class="row" style="center;margin-left: 30px;">
				     <div style="float:left;"> 
				 		<std:date name="date_souhaite" placeholder="Date" forceWriten="true" />
				 	</div>
				 	<div style="float:left;">	
				 		<std:text name="heure_souhaite" type="string" mask="99:99" placeholder="Heure" forceWriten="true" style="width:60px;margin-left: 10px;" />
				 	</div>	
				 	<span style="float:left;width: 100%;color: #3f51b5;">Laisser vide si la commande est immédiate</span>
         		</div>
         		
         		<!-- Mode paiement -->
         		<div style="float: left;text-align: center;width: 100%;">
         			<hr style="margin-top: 7px;margin-bottom: 0px;">
         			<h2 style="text-align: left;margin-bottom: 15px;">Mode de paiement :</h2>
         			<div class="row" >
         				<div class="col-lg-12 col-sm126 col-xs-12" id="div_mode_paie">
		         			<a href="javascript:void(0);" class="mode-paie" v="ESPECES">
								 <i class="fa fa-money"></i> ESPECES
							</a>
		         			<a href="javascript:void(0);" class="mode-paie" v="CARTE">
								<i class="fa fa-cc-mastercard"></i> CARTE
							</a>
		         			<%if(isPoint && carteClientP != null){ %>
		         				<a href="javascript:void(0);" style="background-color: #c2e79a !important;" class="mode-paie" v="POINTS">         					
		         					<i class="fa fa-cc-amex"></i> POINTS
		         				</a>
		         			<%} %>
		         			<% if(isPortefeuille && (!BigDecimalUtil.isZero(mttPortefeuilleUtilisable) || isSoldeNegatif)){%>
		         				<a href="javascript:void(0);" style="background-color: #c2e79a !important;" class="mode-paie" v="RESERVE">         					
		         					<i class="fa fa-folder"></i> RESERVE
		         				</a>
		         			<%} %>
		         		</div>
		         	</div>
		         	<div class="row" id="cb_div" style="    margin: 21px 0px 0px 0px;
						    text-align: left;
						    margin-left: 0px;
						    border: 1px solid rgb(156, 39, 176);
						    border-radius: 6px;
						    padding: 7px;
						    display: block;display: none;">
    						
    						<table>
    							<tr style="line-height: 30px;">
    								<td>
				         				<std:label classStyle="control-label label-paie" value="Numéro" /> 
				         			</td>
				         			<td>
										<std:text type="string" name="cb_num" maxlength="30" mask="9999-9999-9999-9999" forceWriten="true" style="height: 30px;" />
									</td>
								</tr>
								<tr style="line-height: 30px;">
									<td style="padding-right: 10px;">
										<std:label classStyle="control-label label-paie" value="Date validité" /> 
									</td>
									<td>	
										<std:date name="cb_date" style="height: 30px;" forceWriten="true" />
									</td>
								</tr>
								<tr style="line-height: 30px;">
									<td>	
										<std:label classStyle="control-label label-paie" value="Code" /> 
									</td>
									<td>	
										<std:text type="string" name="cb_code" maxlength="3" mask="999" forceWriten="true" style="height: 30px;width:50px;" />
									</td>
								</tr>	
							</table>
		         	</div>
		         	<div class="row" style="margin-top: 34px;display: flex;justify-content: center;width: 100%;">
		         		<std:button classStyle="btn btn-default shiny" style="margin-bottom: 9px;
		         				border-radius: 6px;
		         				box-shadow: 5px 10px 12px grey;
		         				height: 55px;
		         				font-size: 24px;
		         				font-weight: bold;" 
		         			action="caisse.clientMobile.valider_commande" 
		         			onComplete="$('#closeMd').trigger('click');$('#home_lnk').trigger('click');" 
		         			params="cuis=1" 
		         			targetDiv="left-div" icon="" 
		         			value="Valider la commande" />
		         	</div>
		         </div>	
         	</div>
		</div>	
</std:form>