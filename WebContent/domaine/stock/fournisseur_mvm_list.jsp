<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="java.util.Date"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>

<script type="text/javascript">
	$(document).ready(function (){
		$(document).off('click', "a[id^='lnk_det']");
		$(document).on('click', "a[id^='lnk_det']", function(){
			setTrContent($(this).attr("curr"), "<%=EncryptionUtil.encrypt("stock.mouvement.editTrMvm")%>");
		});
	});
</script>


<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion de stock</li>
		<li>Fiche fournisseur</li>
		<li class="active">Liste des mouvements</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link actionGroup="U" classStyle="btn btn-default" action="stock.fournisseur.work_init_update" workId="${fournisseur.id }" icon="fa-3x fa-pencil" tooltip="Modifier" />
		<std:link classStyle="btn btn-default" action="stock.fournisseur.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
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
	<std:form name="search-form">
		<!-- widget grid -->
		<div class="widget">
			<div class="row">
				<%request.setAttribute("curMnu", "mvm");  %>
				<jsp:include page="/domaine/stock/fournisseur_header_tab.jsp" />
		     </div>
			<div class="widget-body">
				<div class="row">
					<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
				</div>
				
			<div class="row">	
				<!-- Liste des mouvements -->
				<cplx:table name="list_fournisseur_mvm" transitionType="simple" forceFilter="true" width="100%" title="Mouvements fournisseur" initAction="stock.fournisseur.findMouvement" checkable="false" autoHeight="true">
					<cplx:header>
							<cplx:th type="date" valueKey="mouvement.date_mouvement" field="mvm.date_mouvement" width="150"/>
							<cplx:th type="decimal" value="Montant TTC" field="mvm.montant_ttc" width="160"/>
							<cplx:th type="decimal" value="Montant HT" field="mvm.montant_ht" width="160"/>
							<cplx:th type="decimal" value="Montant TVA" field="mvm.montant_tva" width="160"/>
							<cplx:th type="string" value="Type mouvement" field="mvm.type_mvmnt" groupValues="${typeMvmArray }"/>
					</cplx:header>
					<cplx:body>
						<c:forEach items="${listMvmFournisseur }" var="detail">
							<cplx:tr workId="${detail.id }">
								<cplx:td>
									 <a href="javascript:" id="lnk_det" curr="${detail.id}"><fmt:formatDate value="${detail.date_mouvement}"/></a>
								</cplx:td>
								<cplx:td align="right" value="${detail.montant_ttc}"></cplx:td>
								<cplx:td align="right" value="${detail.montant_ht}"></cplx:td>
								<cplx:td align="right">
									<c:choose>
										<c:when test="${detail.type_mvmnt=='a' }">
											<span style="color:green;">+<fmt:formatDecimal value="${detail.montant_tva}"/></span>
										</c:when>
										<c:otherwise>
											<span style="color:red;">-<fmt:formatDecimal value="${detail.montant_tva}"/></span>
										</c:otherwise>	
									</c:choose>
								</cplx:td>
								<cplx:td style="padding-left: 15px;">
									<c:choose>
										<c:when test="${detail.type_mvmnt=='v' }">Vente</c:when>
										<c:when test="${detail.type_mvmnt=='vc' }">Vente caisse</c:when>
										<c:when test="${detail.type_mvmnt=='a' }">Achat</c:when>
										<c:when test="${detail.type_mvmnt=='dv' }">Devis</c:when>
										<c:when test="${detail.type_mvmnt=='av' }">Avoir</c:when>
										<c:when test="${detail.type_mvmnt=='p' }">Perte</c:when>
										<c:when test="${detail.type_mvmnt=='c' }">Consommation</c:when>
										<c:when test="${detail.type_mvmnt=='cm' }">Commande</c:when>
										<c:when test="${detail.type_mvmnt=='t' }">Transfert</c:when>
										<c:when test="${detail.type_mvmnt=='tr' }">Transformation</c:when>
										<c:when test="${detail.type_mvmnt=='rt' }">Retour</c:when>
										<c:when test="${detail.type_mvmnt=='i' }">Inventaire</c:when>
									</c:choose>
								</cplx:td>
							</cplx:tr>
							<tr style="display: none;" id="tr_det_${detail.id}" class="sub">
								<td colspan="5" noresize="true" style="background-color: #fff4d3;cursor: default;" align="center" id="tr_consult_${detail.id}">
									
								</td>
							</tr>
						</c:forEach>
					</cplx:body>
				</cplx:table>
			</div>
			
			<% String devise = StrimUtil.getGlobalConfigPropertie("devise.html");
			BigDecimal mttHt = (BigDecimal)request.getAttribute("mttHt");
			BigDecimal mttTva = (BigDecimal)request.getAttribute("mttTva");
			BigDecimal mttTtc = (BigDecimal)request.getAttribute("mttTtc");
			%>
			
			<div class="row">
				<std:label classStyle="control-label col-md-2" value="Total TTC" />
				<div class="col-md-2" style="margin-top: 2px;">
					<span style="font-size: 12px !important;font-weight: bold;" class="badge badge-green">
						<%=BigDecimalUtil.formatNumberZero(mttHt)%> <%=devise %>
					</span>
				</div>
				<std:label classStyle="control-label col-md-2" value="Total TVA" />
				<div class="col-md-2" style="margin-top: 2px;">
					<span style="font-size: 12px !important;font-weight: bold;" class="badge badge-green">
						<%=BigDecimalUtil.formatNumberZero(mttTva)%> <%=devise %>
					</span>
				</div>
				<std:label classStyle="control-label col-md-2" value="Total TTC" style="font-weight:bold;"/>
				<div class="col-md-2" style="margin-top: 2px;">
					<span style="font-size: 14px !important;font-weight: bold;" class="badge badge-green">
						<%=BigDecimalUtil.formatNumberZero(mttTtc)%> <%=devise %>
					</span>
				</div>
			</div>
				
			 	</div>		
			</div>
		</std:form> 	
	</div>