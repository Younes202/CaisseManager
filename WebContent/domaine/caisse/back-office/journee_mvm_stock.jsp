<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="framework.model.common.util.DateUtil"%>
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

<script type="text/javascript">
	$(document).ready(function (){
		$(document).off('click', "a[id^='lnk_det']");
		$(document).on('click', "a[id^='lnk_det']", function(){
			setTrContent($(this).attr("curr"), "<%=EncryptionUtil.encrypt("caisse.journee.editTrMvm")%>");
		});
	});
</script>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion de la journ&eacute;</li>
		<li class="active">Mouvements stock</li>
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
		<div class="col-lg-12 col-sm-12 col-xs-12">
			<div class="widget">
				<div class="widget-main ">
					<div class="tabbable">
						<%request.setAttribute("curr_tab", "mvmStk"); %>
						<jsp:include page="journee_tab_header.jsp" />

						<!-- row -->
						<div class="tab-content">
							<std:form name="search-form">
								<cplx:table name="list_mouvement" transitionType="simple" width="100%" forceFilter="true" title="Liste des mouvements" initAction="caisse.journee.find_mvmStock" autoHeight="true" checkable="false">
									<cplx:header>
										<cplx:th type="empty" width="250" />
										<cplx:th type="long[]" value="Stock" field="mouvement.opc_emplacement.id" fieldExport="mouvement.opc_emplacement.titre"  groupValues="${listEmplacement }" groupKey="id" groupLabel="titre"/>
										<cplx:th type="decimal" value="Montant H.T" field="mouvement.montant_ht" width="110"/>
										<cplx:th type="decimal" value="Montant TVA" field="mouvement.montant_tva" width="110"/>
										<cplx:th type="decimal" value="Montant T.T.C" field="mouvement.montant_ttc" width="110"/>
									</cplx:header>
									
									<c:set var="bigDecimalUtil" value="<%=new BigDecimalUtil() %>" />
									
									<cplx:body>
										<c:forEach items="${list_mouvement }" var="mouvement">
											<cplx:tr workId="${mouvement.id }">
												<cplx:td>
													<a href="javascript:" id="lnk_det" curr="${mouvement.id}">
														<span class="fa fa-plus" style="color:green;"></span> Détail
													</a>
													<c:if test="${mapMvmStockCaisse.get(mouvement.id) != null }">
														[<std:linkPopup value="${mapMvmStockCaisse.get(mouvement.id).ref_commande }" action="caisse.journee.edit_mouvement" workId="${mapMvmStockCaisse.get(mouvement.id).id }" />]
													</c:if>
												</cplx:td>
												<cplx:td value="${mouvement.opc_emplacement.titre}"></cplx:td>
												<cplx:td align="right" style="font-weight:bold;" value="${mouvement.montant_ht }" />
												<cplx:td align="right" style="font-weight:bold;" value="${mouvement.montant_tva }" />
												<cplx:td align="right" style="font-weight:bold;" value="${mouvement.montant_ttc }" />
											</cplx:tr>
											<tr style="display: none;" id="tr_det_${mouvement.id}" class="sub">
												<td colspan="5" noresize="true" style="background-color: #fff4d3;cursor: default;" align="center" id="tr_consult_${mouvement.id}">
													
												</td>
											</tr>
									</c:forEach>	
										<c:if test="${list_mouvement.size() > 0 }">
											<tr class="sub">
												<td colspan="2"></td>
												<td align="right">
													<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
														<fmt:formatDecimal value="${totalHt }"/>
													</span>
												</td>
												<td align="right">
													<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
														<fmt:formatDecimal value="${totalTva }"/>
													</span>
												</td>
												<td align="right">
													<span style="font-size: 14px !important;font-weight: bold;height: 28px;" class="badge badge-blue">
														<fmt:formatDecimal value="${totalTtc }"/>
													</span>
												</td>
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
</div>
<!-- end widget div -->

