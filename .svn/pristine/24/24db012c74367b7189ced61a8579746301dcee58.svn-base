<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementPersistant"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<script type="text/javascript">
$(document).ready(function (){
	init_keyboard_events();
});
</script>
<%
CaisseMouvementPersistant CURRENT_COMMANDE = (CaisseMouvementPersistant) ControllerUtil.getUserAttribute("CURRENT_COMMANDE", request);
%>
<div class="widget-header bordered-bottom bordered-blue" style="padding-bottom: 5px; padding-top: 5px; padding-right: 5px; ">
     <span class="widget-caption">
     <%if(CURRENT_COMMANDE != null && CURRENT_COMMANDE.getOpc_client() != null){ %>
     	<span style="font-size: 15px;font-weight: bold;"><i style="color: #8a1313;font-size: 26px;" class="fa fa-user"></i> Bienvenue <%=CURRENT_COMMANDE.getOpc_client().getNom()+" "+StringUtil.getValueOrEmpty(CURRENT_COMMANDE.getOpc_client().getPrenom()) %></span>
    <%} %>
    </span>	
	<div class="widget-buttons buttons-bordered" style="margin-bottom: 10px;">
  		<i class="fa fa-keyboard-o" style="font-size: 20px;"></i>        
  		<label>
           <input class="checkbox-slider toggle colored-blue" type="checkbox" id="keyboard-activator" style="display: none;">
           <span class="text"></span>
       </label>
  	</div>
  	<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal" style="margin-top: -10px;margin-right: 5px;">
		<i class="fa fa-times"></i> Fermer
	</button>            
</div>
         
<std:form name="paiement-form">
	<jsp:include page="/commun/keyboard-popup.jsp" />
	<div id="paie-div">
		<jsp:include page="authentification.jsp" />
	</div>
</std:form>