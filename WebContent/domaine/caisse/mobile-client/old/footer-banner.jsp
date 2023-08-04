<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementPersistant"%>
<%@page import="appli.controller.domaine.caisse.ContextAppliCaisse"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>
<%@page import="framework.controller.ControllerUtil"%>

<%
CaisseMouvementPersistant CURRENT_COMMANDE = (CaisseMouvementPersistant)ControllerUtil.getUserAttribute("CURRENT_COMMANDE", request);
%>
<div id="banner_foot_act">
     <!-- TOTAL COMMANDE -->
	 <div id="cmd_total" class="label label-success" style="position: absolute;right: 20px;bottom: 10px;font-size: 22px;font-weight: bold;color: white;border-radius:15px !important;">
	 
	 </div>				
		<!-- AFFICHER CACHER LA COMMANDE -->
	<a id="lnk_panier_det" onClick="$('#left-div').toggle(500);" 
				href="javascript:" style="padding-left: 6px;
    				line-height: 42px;
    				font-size: 19px;display: none;" 
				class="">
		<img src="resources/caisse/img/caisse-web/shopping_cart2.png" style="width:30px;" />
		MA COMMANDE
	</a>
</div>

