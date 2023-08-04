<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="java.util.Date"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
.mDiv{
	display: none !important;
}
.flexigrid{
	border: 0px !important;
}
</style>

<script type="text/javascript">
$(document).ready(function() {
	$(document).on('change', 'select[id^="date"]', function(){
		submitAjaxForm('<%=EncryptionUtil.encrypt("stock.inventaire.find_synth_inventaire")%>', 'refreshInv=1', $("#data-form"), null);
	});
	$(".typeUnite").change(function() {
		var unite = $(".typeUnite").val();
		submitAjaxForm('<%=EncryptionUtil.encrypt("stock.inventaire.find_synth_inventaire")%>', 'typeUnite='+unite, $("#data-form"), null);
	});
});

</script>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion de stock</li>
		<li class="active">Ecart inventaire</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link classStyle="btn btn-default" action="stock.inventaire.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all"
			tooltip="Retour &agrave; la recherche" />
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
	
	<std:form name="data-form">	
		<c:set var="bigDecimalUtil" value="<%=new BigDecimalUtil() %>" />
        <div class="form-group">
			<div class="row">
				<std:label classStyle="control-label col-md-2" value="Type d'article" />
			    <div class="col-md-6">
					<std:select name="typeUnite" classStyle="typeUnite" type="long" data="${listeType}"/>
			    </div>
		    </div>
		</div>
		    
		<div class="row">
	        <cplx:table name="list_synthese_inv" transitionType="simple" width="100%" autoHeight="true" checkable="false" initAction="stock.inventaire.find_synth_inventaire" exportable="false">
				<cplx:header>
					<cplx:th type="string" value="Article" />
					<cplx:th type="decimal" value="Qte th&eacute;orique" width="130"/>
					<cplx:th type="decimal" value="Qte r&eacute;elle" width="130"/>
					<cplx:th type="decimal" value="Ecart" width="130"/>
					<cplx:th type="decimal" value="Montant &eacute;cart" width="130"/>
				</cplx:header>
				<cplx:body>

					<c:set var="oldFam" value="${null }" />
					<c:forEach items="${list_synthese_inv }" var="synthInv">
						<c:if test="${empty oldFam or oldFam != synthInv.opc_article.opc_famille_stock.id }">
							<tr>
								<td colspan="5" style="background-color:#bcdfa6;border-bottom: 2px solid orange;color: blue;font-weight:bold;">${synthInv.opc_article.opc_famille_stock.code } - ${synthInv.opc_article.opc_famille_stock.libelle }</td>
							</tr>
						</c:if>
						<c:set var="oldFam" value="${synthInv.opc_article.opc_famille_stock.id }" />
						<tr>
							<td>${synthInv.opc_article.getLibelleDataVal() }</td>
							<td align="right"><fmt:formatDecimal value="${synthInv.qte_theorique }" /></td>
							<td align="right"><fmt:formatDecimal value="${synthInv.qte_reel }" /></td>
							
							<c:if test="${synthInv.qte_ecart.intValue() < 0 }">
								<c:set var="colorRed" value="${true }" />
							</c:if>
							
							<td align="right" style="${colorRed ? 'color:red;' : 'color:green;'}">
								<fmt:formatDecimal value="${synthInv.qte_ecart }" />
							</td>
							<td align="right" style="${colorRed ? 'color:red;' : 'color:green;'}"><fmt:formatDecimal value="${bigDecimalUtil.multiply(synthInv.opc_article.prix_achat_ttc, synthInv.qte_ecart) }" /></td>
                 			</tr>
                 			<c:set var="colorRed" value="${false }" />
					</c:forEach>
				</cplx:body>
			</cplx:table>
		</div>
	 </std:form>
</div>
	 