<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="appli.controller.domaine.caisse.ContextAppliCaisse"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementPersistant"%>
<%@page import="appli.model.domaine.fidelite.persistant.CarteFideliteClientPersistant"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
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
    font-size: 20px;
    font-weight: bold;
    color: gray;
}
#generic_modal_body{
	width: 720px;
	margin-left: -10%;
}
.btn-calc-paie{
	margin: 3px !important;
	padding: 0px 0px !important;
	width: 40px !important;
 	height: 40px !important;
 	font-size: 25px !important;
 	font-weight: bold !important;
}
.mode-paie{
	padding : 0px !important;;
	font-size: 18px;
    font-weight: bold;
    padding-top: 15px !important;;
    margin-top: 15px;
    width: 60px !important;
    height: 60px !important;
}
.type-btn-paie{
    width: 137px;
    height: 58px;
    padding-top: 13px;
    font-weight: bold;
    margin-bottom: 5px;
    font-size: 20px;
 }
.mtt-paie{
   	font-size: 16px;
   	font-weight: bold;
   	color: blue;
   }   
</style>

<%
	CaisseMouvementPersistant CURRENT_COMMANDE = (CaisseMouvementPersistant) ControllerUtil.getUserAttribute("CURRENT_COMMANDE", request);
%>

	<!-- Page Header -->
	<std:form name="data-form">
		<input type="hidden" id="typeCmd" name="typeCmd">
		<input type="hidden" id="fraisLiv" name="fraisLiv">
	     <div class="widget-header bordered-bottom bordered-blue" style="padding-bottom: 5px; padding-top: 5px; padding-right: 5px; ">
            <span class="widget-caption">
            <%
            	if(CURRENT_COMMANDE != null && CURRENT_COMMANDE.getOpc_client() != null){
            %>
            	<span style="font-size: 15px;font-weight: bold;"><%=CURRENT_COMMANDE.getOpc_client().getNom()+" "+StringUtil.getValueOrEmpty(CURRENT_COMMANDE.getOpc_client().getPrenom())%></span>
           <%
           	}
           %>
            </span>	
         	<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal" style="margin-top: -3px;margin-right: 5px;">
				<i class="fa fa-times"></i> Fermer
			</button>            
         </div>
		<div class="row" style="margin-left: 0px;margin-right: 0px;">
			<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
		</div>
         <div class="widget-body">
         	<div class="row" style="margin-left: 0px;margin-right: 0px;text-align: center;font-size: 20px;">
         	
         	<%
         	         		if(request.getAttribute("is_elment_encaisse") != null){
         	         	%>
         		<i class="fa fa-check-circle-o" style="font-size: 20px;color: #53a93f;"></i>
	         		Cet &eacute;l&eacute;ment est d&eacute;jà encaiss&eacute;.
         	<%
         			} else{
         		%>
	         		<i class="fa fa-check-circle-o" style="font-size: 20px;color: #53a93f;"></i>
	         		Commande encaiss&eacute;e.
	         <%
	         			}
	         		%>		
         	</div>
			<hr style="margin-top: 0px;margin-bottom: 15px;">
			
			<%
							if(request.getAttribute("is_elment_encaisse") == null){
						%>
				<div class="row" style="text-align: center;">
					<div class="col-md-12">
						<std:button classStyle="btn btn-success shiny" style="border-radius: 18px;box-shadow: 5px 10px 12px grey;height: 55px;font-size: 24px;font-weight: bold;background:url('resources/caisse/img/caisse/normal/paie_ticket.png') no-repeat;background-size: 56px;padding-left: 70px;" action="caisse-web.caisseWeb.validerPaiement" params="&isfin=1" targetDiv="left-div" icon="" value="Finaliser" />
						<std:button classStyle="btn btn-darkorange shiny" style="border-radius: 18px;box-shadow: 5px 10px 12px grey;margin-left: 20px;height: 55px;font-size: 24px;font-weight: bold;background:url('resources/caisse/img/caisse/normal/paie.png') no-repeat;background-size: 56px;padding-left: 70px;" action="caisse-web.caisseWeb.validerPaiement" params="not=1&isfin=1" targetDiv="left-div" icon="" value="Finaliser S.T" />
						
						<%
						if(!StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CAISSE_PRINT"))){
						%>
							<a href="javascript:" onclick="javascript:callExternalUrl('http://localhost:8001/cm-client?act=print&tp=dashONL');" class="btn btn-info shiny" style="border-radius: 18px;box-shadow: 5px 10px 12px grey;margin-left: 20px;height: 55px;font-size: 24px;font-weight: bold;background:url('resources/caisse/img/caisse/normal/open_dash.png') no-repeat;background-size: 65px;padding-left: 70px;">Tiroir</a>
						<%} else{ %>
							<std:button classStyle="btn btn-info shiny" style="border-radius: 18px;box-shadow: 5px 10px 12px grey;margin-left: 20px;height: 55px;font-size: 24px;font-weight: bold;background:url('resources/caisse/img/caisse/normal/open_dash.png') no-repeat;background-size: 65px;padding-left: 70px;" action="caisse-web.caisseWeb.ouvrirTiroirCaisse" targetDiv="X" params="wibaj=1" icon="" value="Tiroir" />
						<%} %>
					</div>
				</div>
			<%} %>
		</div>
</std:form>