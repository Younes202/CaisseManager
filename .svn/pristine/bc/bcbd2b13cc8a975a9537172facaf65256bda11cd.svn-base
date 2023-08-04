<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="appli.controller.domaine.administration.bean.EtatFinanceBean"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.controller.Context"%>
<%@page import="framework.model.common.util.StrimUtil"%>
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
boolean isOptimisation = ContextGloabalAppli.getAbonementBean().isOptPlusOptimisation();
boolean isRestau = ContextAppli.IS_RESTAU_ENV();
%>
<script type="text/javascript">
$(document).ready(function (){ 
	$('.pdf_link').click(function(){
		$("#pdf_load_trig").attr("href", "front?w_f_act=<%=EncryptionUtil.encrypt("caisse.razPrint.imprimer_raz_caisse")%>&dateDebut="+$("#dateDebut").val()+"&empl="+$("#employe").val()+"&dateFin="+$("#dateFin").val()+"&dateJour="+$("#dateJour").val()+"&"+$(this).attr("params")+"&optimOptPrint="+$("#optimOptPrint").prop("checked"));
		document.getElementById("pdf_load_trig").click();
	});	
});
</script>

<style>
.span_lab{
	width: 136px;
    float: left;
}
</style>

<a href="" id="pdf_load_trig" target="downloadframe" style="display:none;"></a>

<!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Gestion de caisse</li>
         <li class="active">RAZ</li>
     </ul>
 </div>

<div class="page-header position-relative">
		<div class="header-title" style="padding-top: 4px;">
			<std:linkPopup actionGroup="C" classStyle="btn btn-info" action="caisse.razPrint.init_print_conf" icon="fa-3x fa-cogs" tooltip="Configurer l'imprimante" value="Configurer l'imprimante" />
			
			<%if(isRestau){ %>
				<std:linkPopup actionGroup="C" params="skipI=1&skipP=1" value="Familles boissons" action="caisse.razPrint.config_boisson_raz" tooltip="Configuration familles RAZ boissons" icon="fa fa-cogs" />
			<%} %>
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
         <div class="widget-body">
			<div class="row" style="margin-left: 0px;">
				<div class="form-title">
					RAZ divers
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Date début" />
					<div class="col-md-2">
						<std:date name="dateDebut" value='${dateDebut }' />
					</div>
					<std:label classStyle="control-label col-md-3" value="Date fin" />
					<div class="col-md-2">
						<std:date name="dateFin" value='${dateFin }' />
					</div>
				</div>
				<%if(isOptimisation){ %>	
					<div class="form-group">
						<std:label classStyle="control-label col-md-3" value="Marquer les ventes" />
						<div class="col-md-3">
							<std:checkbox name="marquerMvmPrint" />
							<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Le marquage des ventes permettra de reproduire la RAZ comme lors de sa première impression.
					         <br>Ce paramètre est pris en compte uniquement pour les impressions PDF." src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
					     </div> 
					<%if(isOptimisation){ %>
					     <std:label classStyle="control-label col-md-3" value="Avec calcul optimisation" />
						 <div class="col-md-3">
							<std:checkbox name="optimOptPrint" style="vertical-align: bottom;"/>
							<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Avec prise en compte de l'optimisation dans l'affichage" src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
						 </div>	
						<%} %>
					</div>
				<%} %>
				<div class="col-md-12">
					<hr style="width: 100%;">
				</div>
				
					<div class="col-md-4" style="margin-top: 15px;">
						<span class="span_lab">RAZ période</span>
						<std:button classStyle="btn btn-sm btn-default shiny icon-only" action="caisse.razPrint.imprimer_raz_caisse" targetDiv="div_gen_printer" params="format=PRINT&mnu=RP" icon="fa-print" value=""/>
						<std:link tooltip="T&eacute;l&eacute;charger au format PDF"  params="format=PDF&mnu=RP&skipI=true&skipP=true" targetDiv="div_gen_printer" icon="fa-file-pdf-o" classStyle="btn btn-azure btn-sm shiny icon-only primary pdf_link" />
						<std:link action="caisse.razPrint.imprimer_raz_caisse" tooltip="Consulter la page"  params="format=HTML&mnu=RP&skipI=true&skipP=true" icon="fa-eye" classStyle="btn btn-azure btn-sm shiny icon-only primary" />
					</div>
					<div class="col-md-4" style="margin-top: 15px;">
						<span class="span_lab">RAZ journée</span>
						<std:button classStyle="btn btn-sm btn-default shiny icon-only" action="caisse.razPrint.imprimer_raz_caisse" params="format=PRINT&mnu=RCJ" targetDiv="div_gen_printer" icon="fa-print" value=""/>
						<std:link tooltip="T&eacute;l&eacute;charger au format PDF"  params="format=PDF&mnu=RCJ&skipI=true&skipP=true" icon="fa-file-pdf-o" targetDiv="div_gen_printer" classStyle="btn btn-azure btn-sm shiny icon-only primary pdf_link" />
						<std:link action="caisse.razPrint.imprimer_raz_caisse" tooltip="Consulter la page"  params="format=HTML&mnu=RCJ&skipI=true&skipP=true" icon="fa-eye" classStyle="btn btn-azure btn-sm shiny icon-only primary" />
					</div>
					<div class="col-md-4" style="margin-top: 15px;">
						<span class="span_lab">RAZ vente articles</span>
						<std:button classStyle="btn btn-sm btn-default shiny icon-only" action="caisse.razPrint.imprimer_raz_caisse" params="format=PRINT&mnu=RA" targetDiv="div_gen_printer" icon="fa-print" value=""/>
						<std:link tooltip="T&eacute;l&eacute;charger au format PDF"  params="format=PDF&mnu=RA&skipI=true&skipP=true" icon="fa-file-pdf-o" targetDiv="div_gen_printer" classStyle="btn btn-azure btn-sm shiny icon-only primary pdf_link" />
						<std:link action="caisse.razPrint.imprimer_raz_caisse" tooltip="Consulter la page"  params="format=HTML&mnu=RA&skipI=true&skipP=true" icon="fa-eye" classStyle="btn btn-azure btn-sm shiny icon-only primary" />
					</div>
					<hr style="width: 100%;">
					<div class="col-md-8" style="margin-top: 15px;">
						<span class="span_lab">RAZ employé</span>
						<div style="float: left;">
							<std:button classStyle="btn btn-sm btn-default shiny icon-only" action="caisse.razPrint.imprimer_raz_caisse" targetDiv="div_gen_printer" params="format=PRINT&mnu=RE" icon="fa-print" value=""/>
							<std:link tooltip="T&eacute;l&eacute;charger au format PDF"  params="format=PDF&mnu=RE&skipI=true&skipP=true" targetDiv="div_gen_printer" icon="fa-file-pdf-o" classStyle="btn btn-azure btn-sm shiny icon-only primary pdf_link" />
							<std:link action="caisse.razPrint.imprimer_raz_caisse" tooltip="Consulter la page"  params="format=HTML&mnu=RE&skipI=true&skipP=true" icon="fa-eye" classStyle="btn btn-azure btn-sm shiny icon-only primary" />
						</div>
						<div style="float: left;width: 350px;"><std:select name="user" type="long" width="100%" data="${listUser }" key="id" labels="login" /></div>
					</div>
					<div class="col-md-4" style="margin-top: 15px;">
						<span class="span_lab">RAZ quantité articles</span>
						<std:button classStyle="btn btn-sm btn-default shiny icon-only" action="caisse.razPrint.imprimer_raz_caisse" targetDiv="div_gen_printer" params="format=PRINT&mnu=RAQTE" icon="fa-print" value=""/>
						<std:link tooltip="T&eacute;l&eacute;charger au format PDF"  params="format=PDF&mnu=RAQTE&skipI=true&skipP=true" targetDiv="div_gen_printer" icon="fa-file-pdf-o" classStyle="btn btn-azure btn-sm shiny icon-only primary pdf_link" />
						<std:link action="caisse.razPrint.imprimer_raz_caisse" tooltip="Consulter la page"  params="format=HTML&mnu=RAQTE&skipI=true&skipP=true" icon="fa-eye" classStyle="btn btn-azure btn-sm shiny icon-only primary" />
					</div>
					
					<hr style="width: 100%;">
					
					<%if(isRestau){ %>
					<div class="col-md-4" style="margin-top: 15px;">
						<span class="span_lab">RAZ boisson</span>
						<std:button classStyle="btn btn-sm btn-default shiny icon-only" action="caisse.razPrint.imprimer_raz_caisse" targetDiv="div_gen_printer" params="format=PRINT&mnu=RB" icon="fa-print" value=""/>
						<std:link tooltip="T&eacute;l&eacute;charger au format PDF"  params="format=PDF&mnu=RB&skipI=true&skipP=true" targetDiv="div_gen_printer" icon="fa-file-pdf-o" classStyle="btn btn-azure btn-sm shiny icon-only primary pdf_link" />
						<std:link action="caisse.razPrint.imprimer_raz_caisse" tooltip="Consulter la page"  params="format=HTML&mnu=RB&skipI=true&skipP=true" icon="fa-eye" classStyle="btn btn-azure btn-sm shiny icon-only primary" />
					</div>
					<%} %>
					<div class="col-md-4" style="margin-top: 15px;">
						<span class="span_lab">RAZ livreurs</span>
						<std:button classStyle="btn btn-sm btn-default shiny icon-only" action="caisse.razPrint.imprimer_raz_caisse" targetDiv="div_gen_printer" params="format=PRINT&mnu=RL" icon="fa-print" value=""/>
						<std:link tooltip="T&eacute;l&eacute;charger au format PDF"  params="format=PDF&mnu=RL&skipI=true&skipP=true" targetDiv="div_gen_printer" icon="fa-file-pdf-o" classStyle="btn btn-azure btn-sm shiny icon-only primary pdf_link" />
						<std:link action="caisse.razPrint.imprimer_raz_caisse" tooltip="Consulter la page"  params="format=HTML&mnu=RL&skipI=true&skipP=true" icon="fa-eye" classStyle="btn btn-azure btn-sm shiny icon-only primary" />
					</div>
					<div class="col-md-4" style="margin-top: 15px;">
						<span class="span_lab">RAZ sociétés livraison</span>
						<std:button classStyle="btn btn-sm btn-default shiny icon-only" action="caisse.razPrint.imprimer_raz_caisse" targetDiv="div_gen_printer" params="format=PRINT&mnu=RSL" icon="fa-print" value=""/>
						<std:link tooltip="T&eacute;l&eacute;charger au format PDF"  params="format=PDF&mnu=RSL&skipI=true&skipP=true" targetDiv="div_gen_printer" icon="fa-file-pdf-o" classStyle="btn btn-azure btn-sm shiny icon-only primary pdf_link" />
						<std:link action="caisse.razPrint.imprimer_raz_caisse" tooltip="Consulter la page"  params="format=HTML&mnu=RSL&skipI=true&skipP=true" icon="fa-eye" classStyle="btn btn-azure btn-sm shiny icon-only primary" />
					</div>
					
					<div class="col-md-4" style="margin-top: 15px;">
						<span class="span_lab">RAZ mode paiements</span>
						<std:button classStyle="btn btn-sm btn-default shiny icon-only" action="caisse.razPrint.imprimer_raz_caisse" targetDiv="div_gen_printer" params="format=PRINT&mnu=RMP" icon="fa-print" value=""/>
						<std:link tooltip="T&eacute;l&eacute;charger au format PDF"  params="format=PDF&mnu=RMP&skipI=true&skipP=true" targetDiv="div_gen_printer" icon="fa-file-pdf-o" classStyle="btn btn-azure btn-sm shiny icon-only primary pdf_link" />
						<std:link action="caisse.razPrint.imprimer_raz_caisse" tooltip="Consulter la page"  params="format=HTML&mnu=RMP&skipI=true&skipP=true" icon="fa-eye" classStyle="btn btn-azure btn-sm shiny icon-only primary" />
					</div>
					<div class="col-md-4" style="margin-top: 15px;">
						<span class="span_lab">RAZ globale</span>
						<std:button classStyle="btn btn-sm btn-default shiny icon-only" action="caisse.razPrint.imprimer_raz_caisse" targetDiv="div_gen_printer" params="format=PRINT&mnu=GLO" icon="fa-print" value=""/>
						<std:link tooltip="T&eacute;l&eacute;charger au format PDF"  params="format=PDF&mnu=GLO&skipI=true&skipP=true" targetDiv="div_gen_printer" icon="fa-file-pdf-o" classStyle="btn btn-azure btn-sm shiny icon-only primary pdf_link" />
						<std:link action="caisse.razPrint.imprimer_raz_caisse" tooltip="Consulter la page"  params="format=HTML&mnu=GLO&skipI=true&skipP=true" icon="fa-eye" classStyle="btn btn-azure btn-sm shiny icon-only primary" />
					</div>
					<div class="col-md-4" style="margin-top: 15px;">
						<span class="span_lab">RAZ poste</span>
						<std:button classStyle="btn btn-sm btn-default shiny icon-only" action="caisse.razPrint.imprimer_raz_caisse" targetDiv="div_gen_printer" params="format=PRINT&mnu=POS" icon="fa-print" value=""/>
						<std:link tooltip="T&eacute;l&eacute;charger au format PDF"  params="format=PDF&mnu=POS&skipI=true&skipP=true" targetDiv="div_gen_printer" icon="fa-file-pdf-o" classStyle="btn btn-azure btn-sm shiny icon-only primary pdf_link" />
						<std:link action="caisse.razPrint.imprimer_raz_caisse" tooltip="Consulter la page"  params="format=HTML&mnu=POS&skipI=true&skipP=true" icon="fa-eye" classStyle="btn btn-azure btn-sm shiny icon-only primary" />
					</div>
			</div>
		</div>		
	</std:form>
  </div>
</div>

<jsp:include page="/commun/print-local.jsp" /> 

<script type="text/javascript">
/*Handles ToolTips*/
$("[data-toggle=tooltip]")
    .tooltip({
        html: true
    });
</script>  