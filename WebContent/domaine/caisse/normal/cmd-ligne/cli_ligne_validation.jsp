<%@page import="java.util.Date"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementOffrePersistant"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="appli.model.domaine.stock.service.IArticleService"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="java.util.Collections"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli.TYPE_LIGNE_COMMANDE"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementPersistant"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.Map"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>




<style>
#customers {
  font-family: Arial, Helvetica, sans-serif;
  border-collapse: collapse;
  width: 100%;
}

#customers td, #customers th {
  border: 1px solid #ddd;
  padding: 8px;
}

#customers tr:nth-child(even){background-color: #f2f2f2;}

#customers tr:hover {background-color: #ddd;}

#customers th {
  padding-top: 12px;
  padding-bottom: 12px;
  text-align: left;
  background-color: #a0a0a0;
  color: white;
}
</style>


<script type="text/javascript">
	$(document).ready(function (){
		$(document).off('click', "a[id^='lnk_det']");
		$(document).on('click', "a[id^='lnk_det']", function(){
			var idx = $(this).attr("curr");
			$("tr[id^='tr_det_']").each(function(){
				if($(this).attr("id") != "tr_det_"+idx){
					$(this).hide();
				}
			});
			$("#tr_det_"+idx).toggle(100);
		});
		
		$("#pdf_link").click(function(){
			$(this).attr("href", $(this).attr("href")+"&dateDebut="+$("#dateDebut").val()+"&dateFin="+$("#dateFin").val());
		});
	});
	
</script>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion des commandes</li>
		<li class="active">Commandes en ligne</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		
	</div>
	<!--Header Buttons-->
	<jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
	<!--Header Buttons End-->
</div>
<!-- /Page Header -->

<!-- Page Body -->
<div class="page-body">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>

	<div class="row">
		<std:form name="search-form">
			<div class="col-lg-12 col-sm-12 col-xs-12">
				<div class="widget">
					<div class="widget-header bordered-bottom bordered-themeprimary">
                         <i class="widget-icon fa fa-tasks themeprimary"></i>
                         <span class="widget-caption themeprimary">CLIENTS EN ATTENTE DE VALIDATION</span>
                     </div>
					<table id="customers">
					  <tr>
					    <th>Nom</th>
					    <th>Mail</th>
					    <th style="width: 120px;">Téléphone</th>
					    <th>Adresse</th>
					    <th style="width: 150px;">Accepter | Rejeter</th>
					    
					  </tr>
					 <c:forEach var="cli" items="${list_client }">
					  <tr>
					    <td>${cli.nom }</td>
					    <td>${cli.mail }</td>
					    <td style="text-align: center;">${cli.telephone }</td>
					    <td>${cli.adresse_rue }</td>
					    <td>
					        <div style="width: 100%;text-align: center;">
								<std:link actionGroup="C" classStyle="btn btn-success" workId="${cli.id }" action="caisse.clientMobile.statClient" params="stat=A" icon="fa-3x fa-check" value="Accepter" tooltip="Accepter" />
								<std:link actionGroup="C" classStyle="btn btn-danger" workId="${cli.id }" action="caisse.clientMobile.statClient" params="stat=R" icon="fa-3x fa-time" value="Rejeter" tooltip="Rejeter" />
							</div>
					     </td>
					  </tr>
				</c:forEach>
				<c:if test="${list_client.size() == 0 }">
					<tr>
						<td colspan="5" style="text-align: center;">
							<span style="line-height: 43px;padding-left: 5px;">Aucun client à valider.</span>
						</td>
					</tr>	
				</c:if>
			</table>	
		</div>		
	</div>	
		</std:form>
	</div>
</div>
