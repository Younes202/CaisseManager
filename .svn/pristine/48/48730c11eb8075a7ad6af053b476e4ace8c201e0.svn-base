<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

 <%
 boolean isRh = ContextGloabalAppli.getAbonementBean().isOptRh();
 %>
 
<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion des personnels</li>
		<li>Liste commandes</li>
		<li class="active">Recherche</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link classStyle="btn btn-default" action="pers.employe.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
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

	<!-- row -->
	<div class="widget">
		<div class="row">
			<div class="col-lg-12 col-sm-12 col-xs-12">
				<div class="tabbable">
					<ul class="nav nav-tabs" id="myTab">
						<li><a data-toggle="tab" href="#descripton" noVal="true" wact="<%=EncryptionUtil.encrypt("pers.employe.work_edit")%>"> Fiche </a></li>
						
						<%if(isRh){ %>			
							<li><a data-toggle="tab" href="#mouvement" noVal="true" wact="<%=EncryptionUtil.encrypt("pers.salaire.work_find")%>"> Salaires </a></li>
						<li><a data-toggle="tab" href="#mouvement" noVal="true" wact="<%=EncryptionUtil.encrypt("pers.pointage.work_init")%>" params="tp=emp"> Pointage </a></li>
						<%} else{ %>
								<li><a data-toggle="tab" href="#mouvement" noVal="true" style="color:#e0e0e0;"> Salaires </a></li>
								<li><a data-toggle="tab" href="#mouvement" noVal="true" style="color:#e0e0e0;"> Pointage </a></li>
						<%} %>
						
						<li><a data-toggle="tab" href="#mouvement" noVal="true" wact="<%=EncryptionUtil.encrypt("caisse.caisse.find_mvm_employe")%>"> Commandes </a></li>
						<li class="active"><a data-toggle="tab" href="#mouvement" noVal="true" wact="<%=EncryptionUtil.encrypt("pers.employe.find_reduction")%>"> R&eacute;duction </a></li>
					</ul>
				</div>
			</div>
		</div>
		<div class="widget-body">
			<div class="row">

				<std:form name="search-form">
					<!-- Liste des employes -->
					<cplx:table name="list_caisseMouvement" transitionType="simple" width="100%" titleKey="caisseMouvement.list" initAction="caisse.caisse.find_mvm_employe">
						<cplx:header>
							<cplx:th type="empty" />
							<cplx:th type="date" valueKey="caisseMouvement.date_vente" field="caisseMouvement.date_vente" width="120" />
							<cplx:th type="decimal" valueKey="caisseMouvement.mtt_commande" field="caisseMouvement.mtt_commande" width="150" />
							<cplx:th type="decimal" valueKey="caisseMouvement.mtt_donne" field="caisseMouvement.mtt_donne" width="150" />
							<cplx:th type="string" valueKey="caisseMouvement.mtt_reduction" field="caisseMouvement.mtt_reduction" width="150" />
							<cplx:th type="string" value="Art. offerts" field="caisseMouvement.mtt_art_offert" width="150" />
							<cplx:th type="string" valueKey="caisseMouvement.type_commande" field="caisseMouvement.type_commande" width="150" />
							<cplx:th type="string" valueKey="caisseMouvement.mode_paiement" field="caisseMouvement.mode_paiement" width="150" />
							<cplx:th type="empty" />
						</cplx:header>
						<cplx:body>
							<c:forEach items="${list_caisseMouvement }" var="caisseMouvement">
								<cplx:tr workId="${caisseMouvement.id }">
									<cplx:td>
										<std:linkPopup classStyle="btn btn-sm btn-primary" action="caisse.journee.edit_mouvement" workId="${caisseMouvement.id }">
											<span class="fa  fa-eye"></span>
										</std:linkPopup>
									</cplx:td>
									<cplx:td value="${caisseMouvement.date_vente}"></cplx:td>
									<cplx:td align="right" value="${caisseMouvement.mtt_commande}"></cplx:td>
									<cplx:td align="right" value="${caisseMouvement.mtt_donne}"></cplx:td>
									<cplx:td align="right" value="${caisseMouvement.mtt_reduction}"></cplx:td>
									<cplx:td align="right" value="${caisseMouvement.mtt_art_offert}"></cplx:td>
									<cplx:td value="${caisseMouvement.type_commande}"></cplx:td>
									<cplx:td value="${caisseMouvement.mode_paiement}"></cplx:td>
									<cplx:td align="center">
										<work:delete-link />
									</cplx:td>
								</cplx:tr>
							</c:forEach>
						</cplx:body>
					</cplx:table>
				</std:form>

			</div>
			<!-- end widget content -->
		</div>
	</div>
</div>
<!-- end widget div -->
