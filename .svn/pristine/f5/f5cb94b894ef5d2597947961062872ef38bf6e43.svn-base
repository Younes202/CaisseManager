<%@page import="appli.controller.domaine.util_erp.ContextAppli.TYPE_CAISSE_ENUM"%>
<%@page import="appli.controller.domaine.caisse.bean.CaisseBean"%>
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

<%
CaisseBean caisseBean = (CaisseBean)request.getAttribute("caisse");
%>

<script type="text/javascript">
$(document).ready(function (){
	$(document).on('change', 'select[id="caisse.type_ecran"]', function(){
		manageAffichage($(this).val());
	});
	manageAffichage('<%=(caisseBean!=null?caisseBean.getType_ecran():"")%>');
});

function manageAffichage(val){
	$("#print-div, #print-check, #stock-div, #caisse-div, #annee-div, #livr-div, #spec-div, #notcuis-div, #balance-div").css("display", "none");
	
	if(val == 'AFFICHEUR' || val == 'AFFICLIENT'){
		if(val == 'AFFICHEUR'){
			$("#caisse-div").css("display", "");
		} else{
			$("#caisse-div, #livr-div, #spec-div, #notcuis-div").css("display", "none");
		}
		$("#stock-div, #print-div, #print-check,#annee-div").css("display", "none");
	} else if(val == 'PRESENTOIRE'){
		$("#caisse-div, #print-div,#print-check, #stock-div, #livr-div, #spec-div, #notcuis-div").css("display", "none");
		$("#annee-div").css("display", "");
	} else if(val == 'PILOTAGE'){
		$("#print-div, #print-check").css("display", "");
		$("#stock-div, #annee-div").css("display", "none");
	} else if(val == 'CUISINE'){
		$("#caisse-div, #livr-div, #spec-div, #notcuis-div").css("display", "none");
		$("#print-div,#print-check, #stock-div, #annee-div").css("display", "");
	} else if(val == 'CAISSE' || val == 'CAISSE_CLIENT'){
		$("#caisse-div, #annee-div").css("display", "none");
		$("#print-div,#print-check, #stock-div, #livr-div, #spec-div, #notcuis-div").css("display", "");
	} else if(val == 'BALANCE'){
		$("#balance-div").css("display", "");
	}
}

</script>

<!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Gestion de caisse</li>
         <li>Fiche de caisse
		</li>
         <li class="active">Edition</li>
     </ul>
 </div>

<div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">      
        <std:link actionGroup="U" classStyle="btn btn-default" action="caisse.caisse.work_init_update" workId="${caisse.id }" icon="fa-3x fa-pencil" tooltip="Modifier" />
		<std:link classStyle="btn btn-default" action="caisse.caisse.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
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
	<std:form name="data-form">
         
       <div class="row">
        <div class="col-lg-12 col-sm-12 col-xs-12">
              <div class="tabbable">
                    <ul class="nav nav-tabs" id="myTab">
                          <li class="active">
                              <a data-toggle="tab" href="#descripton" wact="<%=EncryptionUtil.encrypt("caisse.caisse.work_edit")%>" >
                               Fiche
                              </a>
                           </li>
                           <%if(ControllerUtil.getMenuAttribute("caisseId", request)!=null){ %>
                           
                           	<%if(ContextAppli.TYPE_CAISSE_ENUM.CAISSE.toString().equals(ControllerUtil.getMenuAttribute("typeCaisse", request))){ %>
                            <li>
                              <a data-toggle="tab" href="#journeeCaisse" wact="<%=EncryptionUtil.encrypt("caisse.caisse.find_mouvement")%>">
                               Mouvements caisses
                              </a>
                            </li>
                            <%} %>
                            <%
                           for(TYPE_CAISSE_ENUM typeCaisse : TYPE_CAISSE_ENUM.values()){
                           		if(typeCaisse.toString().equals(caisseBean.getType_ecran())){%>
                           			<li>
                           			<%if(caisseBean.getType_ecran().equals(ContextAppli.TYPE_CAISSE_ENUM.PILOTAGE.toString())
                           					|| caisseBean.getType_ecran().equals(ContextAppli.TYPE_CAISSE_ENUM.PRESENTOIRE.toString())
                           					|| caisseBean.getType_ecran().equals(ContextAppli.TYPE_CAISSE_ENUM.AFFICLIENT.toString())
                           					|| caisseBean.getType_ecran().equals(ContextAppli.TYPE_CAISSE_ENUM.CAISSE_CLIENT.toString())
                           					|| caisseBean.getType_ecran().equals(ContextAppli.TYPE_CAISSE_ENUM.CUISINE.toString())
                           				){ %>
	                                    <a data-toggle="tab" href="#configCaisse" wact="<%=EncryptionUtil.encrypt("caisse_restau.caisseConfigurationRestau.work_edit")%>">
	                                     	Configuration <%=typeCaisse.getLibelle() %>
	                                    </a>
                                    <%} else{ %>
                                    	<a data-toggle="tab" href="#configCaisse" wact="<%=EncryptionUtil.encrypt("caisse.caisseConfiguration.work_edit")%>">
                                     	Configuration <%=typeCaisse.getLibelle() %>
                                    	</a>
                                    <%} %>
                                  </li>
                           		<%}
                           		}
                           	}%>
                     </ul>
                </div>
          </div>
      </div>
         
         <div class="widget-body">
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="IP ou Identifiant terminal" />
					<div class="col-md-4">
						<std:text name="caisse.adresse_mac" type="string" placeholder="Adresse IP/Identifiant" required="true" maxlength="50"/>
					</div>
				</div>	
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" valueKey="caisse.reference"/>
					<div class="col-md-4">
						<std:text name="caisse.reference" type="string" placeholderKey="caisse.reference" maxlength="50" required="true"/>
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" valueKey="caisse.marque"/>
					<div class="col-md-4">
						<std:text name="caisse.marque" type="string" placeholderKey="caisse.marque" maxlength="50"/>
					</div>
 				</div>
 				
 				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Type d'&eacute;cran"/>
					<div class="col-md-4">
						<std:select name="caisse.type_ecran" type="string" data="${listType }" required="true"/>
					</div>
 				</div>
 				
 				<div class="form-group" style="display: none;" id="balance-div">
					<std:label classStyle="control-label col-md-2" value="Familles balance"/>
					<div class="col-md-8">
						<std:select name="caisse.familles_balance_array" type="string[]" isTree="true" key="id" labels="libelle" multiple="true" data="${listFamilleBalance }" value="${famBalArray }" />
					</div>
 				</div>
 				
 				<div class="form-group" style="display: none;" id="caisse-div">
					<std:label classStyle="control-label col-md-2" value="Caisse li&eacute;e"/>
					<div class="col-md-4">
						<std:select name="caisse.opc_caisse.id" type="long" key="id" labels="reference" data="${listCaisseNoAfficheur }" />
					</div>
					<std:label classStyle="control-label col-md-2" value="Afficheur intégré ?" />
					<div class="col-md-4">
						<std:checkbox name="caisse.is_aff_integre" />
						<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Cocher cette case s'il s'agit d'un afficheur intégré à la caisse." src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
					</div>
 				</div>
 					
 				<div class="form-group" style="display: none;" id="annee-div">
 					<std:label classStyle="control-label col-md-2" valueKey="caisse.annee"/>
					<div class="col-md-4">
						<std:text name="caisse.annee" type="long" placeholderKey="caisse.annee" style="width: 40%;" maxlength="4"/>
					</div>
				</div>
				<div class="form-group" style="display: none;" id="stock-div">
					<std:label classStyle="control-label col-md-2" valueKey="caisse.opc_stock_cible" />
					<div class="col-md-4">
						<std:select name="caisse.opc_stock_cible.id" type="long" key="id" labels="titre" data="${listeEmplacement}" width="100%" />
					</div>
				</div>
				<div class="form-group" style="display: none;" id="livr-div">
					<std:label classStyle="control-label col-md-2" value="Caisse sp&eacute;ciale livraison" />
					<div class="col-md-4">
						<std:checkbox name="caisse.is_livraison" />
						<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Cocher cette case si cette caisse est d&eacute;di&eacute;e &agrave; la livraison. Cela aura un impacte sur les options graphiques de la caisse" src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
					</div>
				</div>
				<div class="form-group" style="display: none;" id="spec-div">
					<std:label classStyle="control-label col-md-2" value="Caisse sp&eacute;cifique" />
					<div class="col-md-4">
						<std:checkbox name="caisse.is_specifique" />
						<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Si coch&eacute;e, alors cette caisse affichera uniquement les familles et les menus qui lui sont d&eacute;di&eacute;s" src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
					</div>
				</div>
				<div class="form-group" style="display: none;" id="notcuis-div">
					<std:label classStyle="control-label col-md-2" value="Ne pas imprimer cuisine" />
					<div class="col-md-4">
						<std:checkbox name="caisse.is_notprint_cuis" />
						<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Si coch&eacute;e, alors le ticket ne sortira pas en cuisine" src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
					</div>
				</div>
				<hr>
				<div class="form-group" style="display: none;" id="print-check">
					<std:label classStyle="control-label col-md-2" value="Impression locale" />
					<div class="col-md-4">
						<std:checkbox name="caisse.is_local_print" />
					</div>
				</div>
				<div class="form-group" style="display: none;" id="print-div">
					<std:label classStyle="control-label col-md-2" value="Imprimantes d'impression" />
					<div class="col-md-4">
						<std:select name="caisse.imprimante_array" multiple="true" type="string[]" data="${list_imprimante}" width="80%" value="${imprArray }" />
					</div>
					<std:label classStyle="control-label col-md-2" value="Nombre tickets" />
					<div class="col-md-4">
						<std:text name="caisse.nbr_ticket" type="long" placeholder="Nbr ticket" maxlength="1" style="width:80px;border: 1px solid silver;"/>
					</div>
				</div>
				<div class="form-group" style="display: none;" id="print-div">
					<std:label classStyle="control-label col-md-2" value="Imprimante suppl&egrave;mentaire" />
					<div class="col-md-4">
						<std:select name="caisse.imprimente_special" type="string" data="${list_imprimante}" width="80%" />
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Ticket personnalis&eacute;" />
					<div class="col-md-4">
						<std:checkbox name="caisse.is_ticketPersonnalise"/>
						<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Cocher cette case pour imprimer les donn&eacute;es personnalis&eacute;es sur le ticket manuel" src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
					</div>
				</div>
				<hr>
				<!-- Generic Form -->
				<jsp:include page="/domaine/administration/dataValue_form.jsp" />
				
			<div class="form-actions">
				<div class="row" style="text-align: center;" class="col-md-12" id="action-div">
						<std:button actionGroup="M" classStyle="btn btn-success" action="caisse.caisse.work_merge" workId="${caisse.id }" icon="fa-save" value="Sauvegarder" />
						<std:button actionGroup="D" classStyle="btn btn-danger" action="caisse.caisse.work_delete" workId="${caisse.id }" icon="fa-trash-o" value="Supprimer" />
					</div>
					
				</div>
			</div>
		</div>		
	</std:form>
</div>
</div>

<script type="text/javascript">
$("[data-toggle=tooltip]")
    .tooltip({
        html: true
    });
</script> 