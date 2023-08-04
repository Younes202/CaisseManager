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

<style>
.label-paie{
    font-size: 16px;
    font-weight: bold;
    color: gray;
}
.btn-calc-paie{
	margin: 2px !important;
	padding: 0px 0px !important;
	width: 28px !important;
 	height: 28px !important;
 	font-size: 17px !important;
 	font-weight: bold !important;
}
.mode-paie{
	padding : 0px !important;;
	font-size: 16px;
    font-weight: bold;
    padding-top: 6px !important;;
    margin-top: 15px;
    width: 38px !important;
    height: 38px !important;
}

.mode-paie img{
	width: 24px !important;
}
.type-btn-paie{
    height: 35px;
    font-weight: bold;
    font-size: 16px;
    text-align: center;
 }
.mtt-paie{
   	font-size: 16px;
   	font-weight: bold;
   	color: blue;
   }  
   #table_paie tr{
      line-height: 15px;
   } 
    #table_paie input{
   	   border: 1px solid gray;
   	}
</style>

<%
boolean isCheque = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("PAIE_CHEQUE"));
boolean isDej = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("PAIE_DEJ"));
boolean isPoint = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("PAIE_POINT"));
boolean isPortefeuille = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("PAIE_PORTEFEUILLE"));
boolean isCarte = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("PAIE_CARTE"));
boolean isShowModeCmd = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("SHOW_MODE_CMD"));
boolean isCostomCall = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("SHOW_COSTOM_CALL"));

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
	$("#div-calc a").click(function(){
		if($(this).attr("v") == 'E'){
			$("#txt-calc").val('');
		} else if($(this).attr("v") == 'B'){
			$("#txt-calc").val($("#txt-calc").val().substring(0, $("#txt-calc").val().length-1));
		} else{
			$("#txt-calc").val($("#txt-calc").val()+$(this).attr("v"));
		}
	});

<%
BigDecimal fraisLivrason = BigDecimalUtil.get(ContextGloabalAppli.getGlobalConfig(ContextAppli.PARAM_APPLI_ENUM.FRAIS_LIVRAISON.toString()));
boolean isFraisLivraison = !BigDecimalUtil.isZero(fraisLivrason)
				&& CURRENT_COMMANDE.getOpc_societe_livr()==null 
				&& CURRENT_COMMANDE.getOpc_livreurU() == null;
%>	


<%if(CURRENT_COMMANDE != null && StringUtil.isNotEmpty(CURRENT_COMMANDE.getType_commande())){%>
	manageTypeCmd($("#div-type a[v='<%=CURRENT_COMMANDE.getType_commande()%>']"));
<%}%>


	$("#div-type a").click(function(){
		manageTypeCmd($(this));
	});
	
	$("#div_mode_paie a").click(function(){
		$("#div_mode_paie a").attr("class", "btn btn-primary btn-circle btn-lg mode-paie");
		$(this).attr("class", "btn btn-warning btn-circle btn-lg mode-paie");
	});
	
	$("#div-paie a").click(function(){
		var val = $(this).attr("v");
		
		<%if(carteClientP != null){%>
			if(val == "pt"){
				$("#mtt_points").val(getValNumFormated(<%=mttPointUtilisable%>));
				refreshMttArendre();
				$("#txt-calc").val('');
				return;
			} 
		<%}%>
		<%if(!BigDecimalUtil.isZero(mttPortefeuilleUtilisable)){%>
		if(val == "pf"){
			$("#mtt_portefeuille").val(getValNumFormated(<%=mttPortefeuilleUtilisable%>));
			refreshMttArendre();
			$("#txt-calc").val('');
			return;
		} 
		<%}%>
	
		var mttCalc = $("#txt-calc").val();
		var isCalc = (mttCalc && $.trim(mttCalc)!='');
		
		var mttDej = getValNum("mtt_dej");
		var mttCarte = getValNum("mtt_carte");
		var mttCheque = getValNum("mtt_cheque");
		var mttEspeces = getValNum("mtt_especes");
		var mttPoints = getValNum("mtt_points");
		var mttPortefeuille = getValNum("mtt_portefeuille");
		
		var totalPaye = mttDej + mttCarte + mttCheque + mttEspeces + mttPoints + mttPortefeuille;
		var mtt = isCalc ? mttCalc : (mttTotalCmd-totalPaye);
		
		if(!isCalc && mtt < 0){
			mtt = 0;
		}
		
		//
		if(val == "de"){
			var mttDet = mtt+(isCalc?'':mttDej);
			$("#mtt_dej").val(getValNumFormated(mttDet));
		} else if(val == "cb"){
			var mttDet = mtt+(isCalc?'':mttCarte);
			$("#mtt_carte").val(getValNumFormated(mttDet));
		} else if(val == "ch"){
			var mttDet = mtt+(isCalc?'':mttCheque);
			$("#mtt_cheque").val(getValNumFormated(mttDet));
		} else if(val == "es"){
			var mttDet = mtt+(isCalc?'':mttEspeces);
			$("#mtt_especes").val(getValNumFormated(mttDet));
		} else{
			var mttDet = val;
			$("#mtt_especes").val(getValNumFormated(mttDet));
		}
		// Refresh a rendre
		refreshMttArendre();
		$("#txt-calc").val('');
	});
	$("#div-detail-paie a").click(function(){
		$(this).closest("tr").find("input").val("");
		// Refresh a rendre
		refreshMttArendre();
	});
});
function refreshMttArendre(){
	totalPaye = getValNum("mtt_dej")+getValNum("mtt_carte")+getValNum("mtt_cheque")+getValNum("mtt_especes")+getValNum("mtt_points")+getValNum("mtt_portefeuille");
	if(totalPaye == 0){
		$("#mtt_rendu").val('');
	} else{
		$("#mtt_rendu").val(getValNumFormated((mttTotalCmd-totalPaye)*-1));
	}
}
function getValNum(elementId){
	return $("#"+elementId).val() ? parseFloat($("#"+elementId).val()) : 0;
}
function getValNumFormated(val){
	return (val && $.trim(val)!='') ? parseFloat(val).toFixed(2) : "0.00";
}
function manageTypeCmd(selectorTypeCmd){
	$("#div-type a").attr("class", "btn btn-palegreen shiny type-btn-paie");
	selectorTypeCmd.attr("class", "btn btn-warning shiny type-btn-paie");
	//
	$("#typeCmd").val(selectorTypeCmd.attr("v"));
	
	
	if(selectorTypeCmd.attr("v") == 'L'){
		<%-- Recalculer la livraison --%>
		if(<%=isFraisLivraison%>){
			if($("#fraisLiv").val() != "1"){
				mttTotalCmd = mttTotalCmd + <%=fraisLivrason%>;
				$("#span_total").text(getValNumFormated(mttTotalCmd));
				refreshMttArendre();
				$("#fraisLiv").val("1");
			}
		}
		$('#custom_call').val('');
	} else{
		if(<%=isFraisLivraison%> && $("#fraisLiv").val()=="1"){
			mttTotalCmd = mttTotalCmd - <%=fraisLivrason%>;
			$("#span_total").text(getValNumFormated(mttTotalCmd));
			refreshMttArendre();
			$("#fraisLiv").val("");
		}
		$('#custom_call').val('<%=StringUtil.getValueOrEmpty(request.getAttribute("num_token"))%>');
	}
}
</script>

	<!-- Page Header -->
	<std:form name="data-form">
		<input type="hidden" id="typeCmd" name="typeCmd" value='<%=isShowModeCmd ? "":"P"%>'>
		<input type="hidden" id="fraisLiv" name="fraisLiv">
	     <div class="widget-header bordered-bottom bordered-blue" style="padding-bottom: 5px; padding-top: 5px; padding-right: 5px; ">
            <span class="widget-caption">
            <%
            	if(CURRENT_COMMANDE != null && CURRENT_COMMANDE.getOpc_client() != null){
            %>
            	<span style="font-size: 15px;font-weight: bold;"><%=CURRENT_COMMANDE.getOpc_client().getNom()+" "+StringUtil.getValueOrEmpty(CURRENT_COMMANDE.getOpc_client().getPrenom())%></span>
            	<%
            		if(carteClientP != null){
            	%>
	            	| Fid&eacute;lit&eacute; : <span style="font-size: 12px;color:orange;font-weight: normal;"><%=BigDecimalUtil.formatNumberZero(carteClientP.getMtt_total())%> Dhs</span>
            <%
            	}
                 if(!BigDecimalUtil.isZero(soldePortefeuille)){
            %>
            		| R&eacute;serve : <span style="font-size: 12px;color:orange;font-weight: normal;"><%=BigDecimalUtil.formatNumberZero(soldePortefeuille)%> Dhs</span>
            <%
            	}
                        }
            %>
            </span>	
			
         	<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal" style="margin-top: -3px;margin-right: -2px;">
				<i class="fa fa-times"></i> Fermer
			</button>            
         </div>
		<div class="row" style="margin-left: 0px;margin-right: 0px;">
			<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
		</div>
         <div class="widget-body">
         	<div class="row" style="margin-left: 0px;margin-right: 0px;margin-top: -7px;">
         		<!-- Type -->
         		<div style="width: 100%;float: left;" id="div-type">
         		<%if(isShowModeCmd){
         			if(!BooleanUtil.isTrue(ContextAppliCaisse.getCaisseBean().getIs_livraison())){ %>
         				<a href="javascript:void(0);" class="btn btn-palegreen shiny type-btn-paie" v='P'>Sur place</a>
         				<a href="javascript:void(0);" class="btn btn-palegreen shiny type-btn-paie" v='E'>A emporter</a>
         			<%} %>	
         			<a href="javascript:void(0);" class="btn btn-palegreen shiny type-btn-paie" v='L'>Livraison</a>
         		<%} %>	
         		</div>
         		<hr>
         		<!-- Mode paiement -->
         		<div style="float: left;text-align: center;" id="div-paie">
         			<span style="font-size: 30px;font-weight: bold;">Total : </span>
         			<span id="span_total" style="width: 165px;height: 40px;font-size: 30px;font-weight: bold;text-align: right;color:fuchsia;"><%=BigDecimalUtil.formatNumber(mttTotal) %></span>
         			
         			<span id="span_total_new" style="width: 165px;height: 40px;font-size: 30px;font-weight: bold;text-align: right;color:fuchsia;display: none;">res</span>
         		
         			<%if(request.getAttribute("mtt_deja_encaisse") != null){ %>
		         		<div class="row" style="margin-left: 0px;margin-right: 0px;margin-top: -7px;text-align: center;">
			         		<i class="fa fa-check-circle-o" style="font-size: 17px;color: #53a93f;"></i>
			         		<b style="color: green;"><%=request.getAttribute("mtt_deja_encaisse")  %></b> Dhs d&eacute;j&agrave; encaiss&eacute;.
			         	</div>	
		         	<%} %>
         			<hr style="margin-top: 7px;margin-bottom: -14px;">
         			
         			<div class="row">
         				<div class="col-lg-6 col-sm-6 col-xs-6" id="div_mode_paie">
		         			<a href="javascript:void(0);" class="btn btn-primary  btn-circle btn-lg mode-paie" v="20">20</a>
		         			<a href="javascript:void(0);" class="btn btn-primary  btn-circle btn-lg mode-paie" v="50">50</a>
		         			<a href="javascript:void(0);" class="btn btn-primary  btn-circle btn-lg mode-paie" v="100">100</a>
		         			<a href="javascript:void(0);" class="btn btn-primary  btn-circle btn-lg mode-paie" v="200">200</a>
		         			<a href="javascript:void(0);" class="btn btn-primary  btn-circle btn-lg mode-paie" v="es">
								<img style="width: 40px;margin-top: -6px;" src="resources/caisse/img/caisse-web/coins.png" />
							</a>
							<%if(isCheque){ %>
		         			<a href="javascript:void(0);" class="btn btn-primary  btn-circle btn-lg mode-paie" v="ch">
								<img src="resources/caisse/img/caisse-web/bank_check.png" />
							</a>
							<%} %>
							<%if(isCarte){ %>
		         			<a href="javascript:void(0);" class="btn btn-primary  btn-circle btn-lg mode-paie" v="cb">
								<img src="resources/caisse/img/caisse-web/credit_card.png" />
							</a>
							<%} %>
							<%if(isDej){ %>
		         			<a href="javascript:void(0);" class="btn btn-primary  btn-circle btn-lg mode-paie" v="de">
								<img src="resources/caisse/img/caisse-web/cheque_dej.jpg" />
							</a>
							<%} %>
		         			<%if(isPoint && carteClientP != null){ %>
		         				<a href="javascript:void(0);" style="background-color: #c2e79a !important;" class="btn btn-primary  btn-circle btn-lg mode-paie" v="pt">         					
		         					<img src="resources/caisse/img/caisse-web/v_card.png" />
		         				</a>
		         			<%} %>
		         			<% if(isPortefeuille && (!BigDecimalUtil.isZero(mttPortefeuilleUtilisable) || isSoldeNegatif)){%>
		         				<a href="javascript:void(0);" style="background-color: #c2e79a !important;" class="btn btn-primary btn-circle btn-lg mode-paie" v="pf">         					
		         					<img src="resources/caisse/img/caisse-web/money_safe1.png" />
		         				</a>
		         			<%} %>
		         		</div>
		         		<!-- Calc -->
		         		<div class="col-lg-6 col-sm-6 col-xs-6" id="div-calc" style="margin-top: 14px;">
		         			<div class="form-group" style="margin:0px; margin-bottom: 1px;">
			         			<input type="text" readonly="readonly" size="15" maxlength="17" id="txt-calc" class="form-control" style="width: 80%;float: left;background-color: #e5e5e5;height: 25px;font-size: 23px;font-weight: bold;text-align: right;" />
			         			<a href="javascript:void(0);" class="btn btn-magenta btn-circle btn-sm btn-calc-paie" v="E"><i class="fa fa-eraser"></i></a>
		         			</div>
		         			<div class="form-group" style="margin:0px;">
			         			<%
			         			for(int i=0; i<10; i++){%>
			         				<a href="javascript:void(0);" class="btn btn-danger btn-circle btn-sm btn-calc-paie" v="<%=i%>"><%=i %></a>
			         			<%} %>
			         			<a href="javascript:void(0);" class="btn btn-danger btn-circle btn-sm btn-calc-paie" v=".">.</a>
			         			<a href="javascript:void(0);" class="btn btn-danger btn-circle btn-sm btn-calc-paie" v="B"><i class="fa fa-mail-reply"></i></a>
		         			</div>
		         		</div>
		         	</div>
		         </div>	
         	</div>
         	<hr style="margin-top: 10px;margin-bottom: 10px;">
			<div class="row" style="margin-left: 0px;margin-right: 0px;" id="div-detail-paie">
				<table style="width: 100%;" id="table_paie">
					<tr>
						<td></td>
						<td></td>
						<td></td>
					</tr>
					<tr>
						<td nowrap="nowrap"><std:label classStyle="label-paie" style="float:left;" value="Esp&egrave;ces" /></td>
						<td><input type="text" readonly="readonly" size="15" name="mtt_especes" id="mtt_especes" class="form-control" style="background-color: #e5e5e5;width: 80%;float: left;height: 25px;font-size: 23px;font-weight: bold;text-align: right;" /></td>
						<td><a href="javascript:void(0);" class="btn btn-magenta btn-circle btn-sm" style="width: 30px;height: 30px;padding: 0px 0px;margin-top: 3px;margin-left: 3px;"><i class="fa fa-remove"></i></a></td>
					</tr>
					<%if(isCarte){ %>
					<tr>
						<td nowrap="nowrap"><std:label classStyle="label-paie" style="float:left;" value="Carte" /></td>
						<td><input type="text" readonly="readonly" size="15" name="mtt_carte" id="mtt_carte" class="form-control" style="background-color: #e5e5e5;width: 80%;float: left;height: 25px;font-size: 23px;font-weight: bold;text-align: right;" /></td>
						<td><a href="javascript:void(0);" class="btn btn-magenta btn-circle btn-sm" style="width: 30px;height: 30px;padding: 0px 0px;margin-top: 3px;margin-left: 3px;"><i class="fa fa-remove"></i></a></td>
					</tr>
					<%} %>
					<%if(isCheque){ %>
					<tr>
						<td nowrap="nowrap"><std:label classStyle="label-paie" style="float:left;" value="Ch&egrave;que" /></td>
						<td><input type="text" readonly="readonly" size="15" name="mtt_cheque" id="mtt_cheque" class="form-control" style="background-color: #e5e5e5;width: 80%;float: left;height: 25px;font-size: 23px;font-weight: bold;text-align: right;" /></td>
						<td><a href="javascript:void(0);" class="btn btn-magenta btn-circle btn-sm" style="width: 30px;height: 30px;padding: 0px 0px;margin-top: 3px;margin-left: 3px;"><i class="fa fa-remove"></i></a></td>
					</tr>
					<%} %>
					<%if(isDej){ %>
					<tr>
						<td nowrap="nowrap"><std:label classStyle="label-paie" style="float:left;" value="Chq. d&eacute;j." /></td>
						<td><input type="text" readonly="readonly" size="15" name="mtt_dej" id="mtt_dej" class="form-control" style="background-color: #e5e5e5;width: 80%;float: left;height: 25px;font-size: 23px;font-weight: bold;text-align: right;" /></td>
						<td><a href="javascript:void(0);" class="btn btn-magenta btn-circle btn-sm" style="width: 30px;height: 30px;padding: 0px 0px;margin-top: 3px;margin-left: 3px;"><i class="fa fa-remove"></i></a></td>
					</tr>
					<%} %>
				<%if(carteClientP != null || isPortefeuille){ %>
					<%if(isPoint && carteClientP != null){%>
						<tr>
							<td nowrap="nowrap"><std:label classStyle="label-paie" style="float:left;" value="Fid&eacute;lit&eacute;" /></td>
							<td><input type="text" readonly="readonly" size="15" name="mtt_points" id="mtt_points" class="form-control" style="background-color: #e5e5e5;width: 80%;float: left;height: 25px;font-size: 23px;font-weight: bold;text-align: right;" /></td>
							<td><a href="javascript:void(0);" class="btn btn-magenta btn-circle btn-sm" style="width: 30px;height: 30px;padding: 0px 0px;margin-top: 3px;margin-left: 3px;"><i class="fa fa-remove"></i></a></td>
						</tr>
					<%} 
					if(isPortefeuille){ %>
						<tr>
							<td nowrap="nowrap"><std:label classStyle="label-paie" style="float:left;" value="R&eacute;serve" /></td>
							<td><input type="text" readonly="readonly" size="15" name="mtt_portefeuille" id="mtt_portefeuille" class="form-control" style="background-color: #e5e5e5;width: 80%;float: left;height: 25px;font-size: 23px;font-weight: bold;text-align: right;" /></td>
							<td><a href="javascript:void(0);" class="btn btn-magenta btn-circle btn-sm" style="width: 30px;height: 30px;padding: 0px 0px;margin-top: 3px;margin-left: 3px;"><i class="fa fa-remove"></i></a></td>
						</tr>
					<%} %>
				<%} %>
				
				<%if(isCostomCall){ %>
					<tr>
						<td nowrap="nowrap"><std:label classStyle="label-paie" value="Coaster call" /></td>
						<td><input type="text" size="15" name="custom_call" id="custom_call" maxlength="30" class="form-control" style="background-color: #e5e5e5;width: 80%;float: left;background-color: #e7faff;height: 25px;font-size: 23px;font-weight: bold;" /></td>
						<td><a href="javascript:void(0);" onclick="$('#custom_call').val('');" class="btn btn-magenta btn-circle btn-sm" style="width: 30px;height: 30px;padding: 0px 0px;margin-top: 3px;margin-left: 3px;"><i class="fa fa-remove"></i></a></td>
					</tr>
				<%} %>	
					<tr>
						<td nowrap="nowrap"><std:label classStyle="label-paie" value="A rendre" style="color;#d73d32;" /></td>
						<td><input type="text" readonly="readonly" size="15" name="mtt_rendu" id="mtt_rendu" class="form-control" style="width: 80%;float: left;background-color: #a0d468;height: 30px;font-size: 23px;font-weight: bold;text-align: right;color: blue !important;" /></td>
						<td></td>
					</tr>
				</table>
			</div>
			<div class="row" style="text-align: center;margin-top: 10px;">
				<div class="col-md-12">
					<std:button onClick="managerInitCaisse();" classStyle="btn btn-success shiny" style="box-shadow: 5px 10px 12px grey;height: 40px;font-size: 17px;font-weight: bold;background:url('resources/caisse/img/caisse-web/paie_ticket.png') no-repeat;background-size: 30px;padding-left: 30px;" action="caisse-web.caisseWeb.validerPaiement" closeOnSubmit="true" targetDiv="left-div" icon="" value="Encaisser" />
					<std:button onClick="managerInitCaisse();" classStyle="btn btn-darkorange shiny" style="box-shadow: 5px 10px 12px grey;margin-left: 2px;height: 40px;font-size: 17px;font-weight: bold;background:url('resources/caisse/img/caisse-web/paie.png') no-repeat;background-size: 30px;padding-left: 30px;" action="caisse-web.caisseWeb.validerPaiement" closeOnSubmit="true" params="not=1" targetDiv="left-div" icon="" value="Encaisser S.T" />
					<std:button onClick="managerInitCaisse();" classStyle="btn btn-info shiny" style="box-shadow: 5px 10px 12px grey;margin-left: 2px;height: 40px;font-size: 17px;font-weight: bold;background:url('resources/caisse/img/caisse-web/open_dash.png') no-repeat;background-size: 35px;padding-left: 30px;" action="caisse-web.caisseWeb.ouvrirTiroirCaisse" closeOnSubmit="true" targetDiv="X" params="wibaj=1" icon="" value="" />
				</div>
			</div>
		</div>	
</std:form>