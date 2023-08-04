<%@page import="appli.controller.domaine.util_erp.ContextAppli.TYPE_CAISSE_ENUM"%>
<%@page import="appli.controller.domaine.caisse.bean.CaisseBean"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page import="appli.model.domaine.vente.persistant.JourneePersistant"%>
<%@page import="framework.controller.Context"%>
<%@page errorPage="/commun/error.jsp"%>



		<link href="resources/framework/js/contextmenu/jquery.contextMenu.min.css" rel="stylesheet" type="text/css" />
		 <script src="resources/framework/js/contextmenu/jquery.contextMenu.min.js" type="text/javascript"></script>
	 	<script src="resources/framework/js/contextmenu/jquery.ui.position.min.js" type="text/javascript"></script>
	
		<style type="text/css">
			.context-menu-root .del {
				color: red;
			}
			.context-menu-root .off {
				color: green;
			}
			.context-menu-root .com{
				color: blue;
			}
		</style>
		
</script>
<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion de la journ&eacute;</li>
		<li class="active">Mouvements journ&eacute;e</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<%
			if(ControllerUtil.getMenuAttribute("isCaisse", request) == null){
		%>
		<%if(ControllerUtil.getMenuAttribute("IS_DASH_JRN", request) == null){ %>
			<std:link classStyle="btn btn-default" action="caisse.journee.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
		<%}	
			} else{
		%>
			<std:link classStyle="btn btn-default" action="caisse.caisse.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
		<%
			}
		%>
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
		<div class="col-lg-12 col-sm-12 col-xs-12">
			<div class="widget">
				<div class="widget-main ">
					<div class="tabbable">
						<%request.setAttribute("curr_tab", "mvmAnn"); %>
						<jsp:include page="journee_tab_header.jsp" />

						<!-- row -->
						<div class="tab-content">
							<std:form name="search-form">
								<cplx:table filtrable="false" initAction="" sortable="false" paginate="false" name="listMvmAnnomalie" transitionType="simple" width="100%" title="Mouvements en annomalie" checkable="false" autoHeight="true">
									<cplx:header>
										<cplx:th type="string" valueKey="caisseMouvement.ref_commande" field="caisseMouvement.ref_commande" width="145" />
										<cplx:th type="string" value="Qui a commandé ?" sortable="false" filtrable="false"/>
										<cplx:th type="string" value="Caissier" field="caisseMouvement.opc_user.id" />
										<cplx:th type="decimal" value="Mtt. commande" field="caisseMouvement.mtt_commande" width="100" />
										<cplx:th type="decimal" value="Mtt. commande_net" field="caisseMouvement.mtt_commande_net" width="100" />
										<cplx:th type="decimal" value="R&eacute;duction Cmd" field="caisseMouvement.mtt_reduction" width="80" filtrable="false" />
										<cplx:th type="decimal" value="R&eacute;duction Art" field="caisseMouvement.mtt_art_reduction" width="80" filtrable="false" />
										<cplx:th type="decimal" value="Mtt. offert" field="caisseMouvement.mtt_art_offert" width="80" filtrable="false" />
										<cplx:th type="string" value="Caisse" field="caisseMouvement.type_commande" width="120" />
										<cplx:th type="string" valueKey="caisseMouvement.mode_paiement" field="caisseMouvement.mode_paiement" width="110" />
										<cplx:th type="empty" width="50"/>
									</cplx:header>
									<cplx:body>
									
										<c:set var="oldCaisse" value="${null }"></c:set>
										
										<c:forEach items="${listMvmAnnomalie }" var="caisseMouvement">
											
											<!-- Caisse -->
											<c:if test="${oldCaisse == null  or oldCaisse != caisseMouvement.caisse }">
												<tr>
													<td colspan="10" noresize="true" class="separator-group">
														<span class="fa fa-fw fa-folder-open-o separator-icon"></span> ${caisseMouvement.caisse.toUpperCase() }
													</td>
												</tr>	
											</c:if>
											<c:set var="oldCaisse" value="${caisseMouvement.caisse }"></c:set>
										
											<cplx:tr workId="${caisseMouvement.id }">
												<cplx:td>
													${caisseMouvement.ref_commande}[<fmt:formatDate value="${caisseMouvement.date_vente}"/>] [${caisseMouvement.type_commande }]
												</cplx:td>
												<cplx:td>
													${caisseMouvement.employe}
													${caisseMouvement.client}
												</cplx:td>
												<cplx:td value="${caisseMouvement.user_encaiss }"></cplx:td>
												<cplx:td style="font-weight:bold;" align="right" value="${caisseMouvement.mtt_commande}"></cplx:td>
												<cplx:td style="font-weight:bold;" align="right" value="${caisseMouvement.mtt_commande_net}"></cplx:td>
												<cplx:td align="right" value="${caisseMouvement.mtt_reduction}"></cplx:td>
												<cplx:td align="right" value="${caisseMouvement.mtt_art_reduction}"></cplx:td>
												<cplx:td align="right" value="${caisseMouvement.mtt_art_offert}"></cplx:td>
												<cplx:td align="center" value="${caisseMouvement.caisse}"></cplx:td>
												<cplx:td align="center" style="color:green;" value="${caisseMouvement.mode_paiement}"></cplx:td>
												<cplx:td align="right">
												
												</cplx:td>	
											</cplx:tr>
											<tr>
												<td colspan="10" style="padding-left: 50px;">
													${caisseMouvement.articles}
												</td>
											</tr>
										</c:forEach>
										<c:if test="${listMvmAnnomalie.size() > 0 }">
												<tr class="sub">
													<td colspan="3"></td>
														<td align="right">
															<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
																<fmt:formatDecimal value="${mvmDetTotal.mtt_commande }"/>
															</span>
														</td>
														<td align="right">
															<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
																<fmt:formatDecimal value="${mvmDetTotal.mtt_commande_net }"/>
															</span>
														</td>
														<td align="right">
															<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
																<fmt:formatDecimal value="${mvmDetTotal.mtt_reduction }"/>
															</span>
														</td>
														<td align="right">
															<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
																<fmt:formatDecimal value="${mvmDetTotal.mtt_art_reduction }"/>
															</span>
														</td>
														<td align="right">
															<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
																<fmt:formatDecimal value="${mvmDetTotal.mtt_art_offert }"/>
															</span>
														</td>
													<td colspan="3"></td>
												</tr>
											</c:if>	
									</cplx:body>
								</cplx:table>
							</std:form>

						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- end widget content -->

</div>
<!-- end widget div -->
