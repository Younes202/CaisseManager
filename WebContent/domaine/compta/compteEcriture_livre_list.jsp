<%@page import="framework.controller.ControllerUtil"%>
<%@page import="appli.model.domaine.administration.service.ICompteBancaireService"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
	#livreTab tr:hover{
		background: #DBEDF3;
	}
	#livreTab td {
		border-bottom: 1px dashed #ff9900;
		padding-right: 20px;
		vertical-align: top;
	}
</style>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Comptabilit&eacute;</li>
		<li>Grand livre</li>
		<li class="active">Ecritures</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->

<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link classStyle="btn btn-default" action="admin.compteBancaire.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
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

<c:set var="cbService" value="<%=ServiceUtil.getBusinessBean(ICompteBancaireService.class) %>" />
<c:set var="dateUtil" value="<%=new DateUtil() %>" />

<std:form name="search-form">	
	<div class="row">
		<div class="widget-body">
		<!-- Filtre -->
		<fieldset>
			<legend>Ecritures</legend>
			<table width="100%">
				<tr>
					<td nowrap="nowrap">
						Compte : 
					</td>
					<td colspan="5" nowrap="nowrap">	
						<std:select name="compte" type="long" data="${list_comptes }" key="id" labels="code;' - ';libelle" width="350" forceWriten="true"/>
					</td>
					<td rowspan="2" valign="middle" align="center">
						<std:button action="admin.compteBancaire.find_ecriture_livre" targetDiv="div_result_livr" value="Rechercher" forceWriten="true" style="width:90px;height:50px;" />
					</td>
				</tr>	
				<tr style="line-height: 50px;">
					<td>
						Exercice :
					</td>	
					<td>
						<std:select name="exercice_select.id" type="long" key="id" labels="libelle" data="${list_exercices }" width="280" forceWriten="true" required="true" addBlank="false"/>
					</td>
					<td nowrap="nowrap">
					Date d&eacute;but : 
					</td>
					<td nowrap="nowrap">
						<std:date name="date_debut" forceWriten="true" value="${date_debut }" />
					</td>
					<td nowrap="nowrap">
					Date fin :
					</td>
					<td nowrap="nowrap">
						<std:date name="date_fin" forceWriten="true" value="${date_fin }" />
					</td>
				</tr>
			</table>		
		</fieldset>
		<br>
	<!-- Liste des ecritures -->
	<div id="div_result_livr">
		<jsp:include page="/domaine/compta/compteEcriture_livre_result.jsp" />
	</div>
</div>
</div>
</std:form>
</div>