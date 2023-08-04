<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="java.util.Date"%>
<%@page import="framework.controller.Context"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="java.util.List"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="appli.model.domaine.vente.persistant.JourneePersistant"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%> 
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<script type="text/javascript">
	$(document).ready(function (){
		$(document).off('click', "a[id^='lnk_det']");
		$(document).on('click', "a[id^='lnk_det']", function(){
			setTrContent($(this).attr("curr"), "<%=EncryptionUtil.encrypt("stock.caisseMouvementArticle.editTrMvm")%>");
		});
		$(document).off('click', "span[id^='grp_parent']");
		$(document).on('click', "span[id^='grp_parent']", function(){
			$("."+$(this).attr("id")).toggle(500);
		});
	});
</script>

<style>
#journee_body .databox .databox-text {
	font-size: 12px;
}
</style>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion des journées</li>
		<li class="active">Dépenses journée</li>
	</ul>
</div>

<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<%if(ControllerUtil.getMenuAttribute("IS_DASH_JRN", request) == null){ %>
			<std:link classStyle="btn btn-default" action="caisse.journee.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
		<%} %>
	</div>
	<!--Header Buttons-->
	<jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
	<!--Header Buttons End-->
</div>
<!-- /Page Header -->

<div class="page-body" id="journee_body">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>

	<!-- widget grid -->
	<div class="widget">
		<std:form name="data-form">
			<div class="row">
				<div class="col-lg-12 col-sm-12 col-xs-12">
					<div class="widget-main ">
						<div class="tabbable">
							<%request.setAttribute("curr_tab", "depense"); %>
							<jsp:include page="journee_tab_header.jsp" />

							<div class="tab-content">
								<h3 style="text-align: center;">Journée du <fmt:formatDate value="${journee.date_journee}" /></h3>
								<hr>
								<div class="row">
									<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
										<!-- Liste des dépenses -->
										<cplx:table name="list_depense" transitionType="simple" forceFilter="true" width="100%" title="Dépenses journée" checkable="false" initAction="caisse.journee.depenses_journee" autoHeight="true">
											<cplx:header>
												<cplx:th type="string" value="Libellé" field="caisseMouvementArticle.libelle" />
												<cplx:th type="string" value="Caissier" field="caisseMouvementArticle.opc_mouvement_caisse.opc_user.login" width="150"/>
												<cplx:th type="decimal" value="Montant" field="caisseMouvementArticle.mtt_total" width="100"/>
											</cplx:header>
											<cplx:body>
												<c:forEach items="${list_depense }" var="caisseMouvementArticle">
													<cplx:tr workId="${caisseMouvementArticle.id }">
														<cplx:td value="${caisseMouvementArticle.libelle}"></cplx:td>
														<cplx:td value="${caisseMouvementArticle.opc_mouvement_caisse.opc_user.login}"></cplx:td>
														<cplx:td align="right" style="font-weight:bold;" value="${caisseMouvementArticle.mtt_total.negate()}"></cplx:td>
													</cplx:tr>
												</c:forEach>
										<c:if test="${list_depense.size() > 0 }">
												<tr class="sub">
													<td></td>
													<td></td>
													<td align="right">
														<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
															<fmt:formatDecimal value="${totalDepTtc.negate() }"/>
														</span>
													</td>
												</tr>
										</c:if>		
											</cplx:body>
										</cplx:table>
									</div>
									<!-- Liste des recettes -->									
									<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
										<cplx:table name="list_recette" transitionType="simple" forceFilter="true" width="100%" title="Recettes journée" checkable="false" initAction="caisse.journee.depenses_journee" autoHeight="true">
											<cplx:header>
												<cplx:th type="string" value="Libellé" field="caisseMouvementArticle.libelle" />
												<cplx:th type="string" value="Caissier" field="caisseMouvementArticle.opc_mouvement_caisse.opc_user.login" width="150"/>
												<cplx:th type="decimal" value="Montant" field="caisseMouvementArticle.mtt_total" width="100"/>
											</cplx:header>
											<cplx:body>
												<c:forEach items="${list_recette }" var="caisseMouvementArticle">
													<cplx:tr workId="${caisseMouvementArticle.id }">
														<cplx:td value="${caisseMouvementArticle.libelle}"></cplx:td>
														<cplx:td value="${caisseMouvementArticle.opc_mouvement_caisse.opc_user.login}"></cplx:td>
														<cplx:td align="right" style="font-weight:bold;" value="${caisseMouvementArticle.mtt_total}"></cplx:td>
													</cplx:tr>
												</c:forEach>
										<c:if test="${list_recette.size() > 0 }">
												<tr class="sub">
													<td></td>
													<td></td>
													<td align="right">
														<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
															<fmt:formatDecimal value="${totalRecTtc }"/>
														</span>
													</td>
												</tr>
										</c:if>		
											</cplx:body>
										</cplx:table>
									</div>
								</div>
								
								<div class="row">
									<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
										<!-- Liste des garanties -->
										<cplx:table name="list_garantie" transitionType="simple" forceFilter="true" width="100%" title="Garantie journée" checkable="false" initAction="caisse.journee.depenses_journee" autoHeight="true">
											<cplx:header>
												<cplx:th type="string" value="Libellé" field="caisseMouvementArticle.libelle" />
												<cplx:th type="string" value="Caissier" field="caisseMouvementArticle.opc_mouvement_caisse.opc_user.login" width="150"/>
												<cplx:th type="decimal" value="Montant" field="caisseMouvementArticle.mtt_total" width="100"/>
											</cplx:header>
											<cplx:body>
												<c:forEach items="${list_garantie }" var="caisseMouvementArticle">
													<cplx:tr workId="${caisseMouvementArticle.id }">
														<cplx:td value="${caisseMouvementArticle.libelle}"></cplx:td>
														<cplx:td value="${caisseMouvementArticle.opc_mouvement_caisse.opc_user.login}"></cplx:td>
														<cplx:td align="right" style="font-weight:bold;" value="${caisseMouvementArticle.mtt_total}"></cplx:td>
													</cplx:tr>
												</c:forEach>
										<c:if test="${list_garantie.size() > 0 }">
												<tr class="sub">
													<td></td>
													<td></td>
													<td align="right">
														<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
															<fmt:formatDecimal value="${totalGarTtc }"/>
														</span>
													</td>
												</tr>
										</c:if>		
											</cplx:body>
										</cplx:table>
									</div>
								</div>	
						</div>
					</div>
				</div>
			</div>
		</div>
		</std:form>
	</div>
</div>

