<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.controller.Context"%>
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
#generic_modal_body{
	width: 1050px;
	margin-left: -50%;
}
</style>

<script type="text/javascript">
$(document).ready(function (){
	init_keyboard_events();
	
	$(".ellipsify").each(function() {
		  var elementToTest = $(this),
		    contentToTest = $(this).text(),
		    testElement = $("<div/>").css({
		      position: "absolute",
		      left: "-10000px",
		      width: elementToTest.width() + "px"
		    }).appendTo("body").text(contentToTest);

		  if (testElement.height() > elementToTest.height()) {
		    elementToTest.attr("title", contentToTest);
		  }
	});
	
	var timer, delay = 700;
	$("#art_filter_inv").bind('keydown change', function(e) {
	    var _this = $(this);
	    clearTimeout(timer);
	    timer = setTimeout(function() {
	    	submitAjaxForm('<%=EncryptionUtil.encrypt("stock.inventaire.loadSaisieInventaire")%>', 'artInv='+$("#art_filter_inv").val(), null, null, 'generic_modal_body');
	    }, delay );
	});
	setTimeout(() => {
		$("#art_filter_inv").focus();
		var strLength = $("#art_filter_inv").val().length * 2;
		$("#art_filter_inv")[0].setSelectionRange(strLength, strLength);
	}, 1000);
});	
</script>

<%
boolean isShowEcart = Context.isOperationAvailable("ECARTINV");
%>

<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Fiche inventaire</span>
			<std:text name="art_filter_inv" type="string" style="width: 180px;
    height: 27px;
    margin-top: 5px;
    margin-left: 19px;
    float: left;
    border: 2px solid #a59a9a;" value="${artInvFilter }" />
			
			<div class="widget-buttons buttons-bordered" style="margin-bottom: 10px;">
         		<i class="fa fa-keyboard-o" style="font-size: 20px;"></i>         
         		<label>
	                 <input class="checkbox-slider toggle colored-blue" type="checkbox" id="keyboard-activator" style="display: none;">
	                 <span class="text"></span>
	             </label>
         	</div>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row" style="max-height: 650px;overflow-y: auto;font-size: 12px;background-color: #f0f4c3;margin-left: -10px;margin-right: -10px;">
				<c:set var="cpt" value="${1 }" />
				<c:set var="encodeService" value="<%=new EncryptionUtil()%>" />
				<c:set var="controllerUtil" value="<%=new ControllerUtil()%>" />
				<c:set var="oldfamille" value="${null }"></c:set>
			
			
			<c:forEach items="${inventaireDtail }" var="detail">
				<c:set var="detailP" value="${mapInventaire.get(detail.opc_article.id) }" />
				<std:hidden name="opc_article.id_${cpt}" value="${detail.opc_article.id }" />
				<std:hidden name="qte_theorique_${cpt}" value="${empty detailP.id ? (detail.qte_entree - detail.qte_sortie) : detailP.qte_theorique}"/>
				<std:hidden name="article_lib_${cpt}" value="${detail.opc_article.code}-${detail.opc_article.libelle}"/>
				<std:hidden name="famille_bleft_${cpt}" value="${detail.opc_article.opc_famille_stock.b_left}"/>
						
							
				<c:if test="${empty oldfam or detail.opc_article.opc_famille_stock.id != oldfam}">
				     <div class="col-md-12 col-lg-12" style="background-color: #00BCD4;margin-top: 4px;margin-bottom: 5px;">
						<span class="fa fa-fw fa-folder-open-o separator-icon"></span> ${detail.opc_article.opc_famille_stock.code}-${detail.opc_article.opc_famille_stock.libelle}
					</div>
				</c:if>	
				<c:set var="oldfam" value="${detail.opc_article.opc_famille_stock.id }"></c:set>
				
				<div class="col-md-12 col-lg-12" style="line-height: 30px;border-right: 2px solid #FF9800;border-bottom: 1px dashed #E91E63;padding-left: 5px;padding-right: 3px;">
					<div class="col-md-6 col-lg-6 ellipsify" style="white-space: nowrap;overflow: hidden;text-overflow: ellipsis;">
						<span style="color: blue;">${detail.opc_article.code}</span> - ${detail.opc_article.getLibelleDataVal()}
					</div>
					<%if(isShowEcart){%>
						<div class="col-md-1 col-lg-1" style="font-weight: bold;" id="qte_theorique_${cpt}">
							<span style="float: right;"><fmt:formatDecimal value="${empty detailP.id ? (detail.qte_entree - detail.qte_sortie) : detailP.qte_theorique }" /></span>
						</div>
					<%} %>
					<div class="col-md-2 col-lg-2 reel_qte">
						<std:text style="padding:2px;height:25px;width: 75px;margin-top: 2px;font-size: 12px;text-align:right;" placeholder="Quantit&eacute;" type='decimal' name="qte_reel_${cpt}" value="${detailP.qte_reel }"/>	
					</div>	
					<%if(isShowEcart){%>
							<div class="col-md-3 col-lg-3">
								<std:text style="padding:2px;height:25px;margin-top: 2px;font-size: 12px;" placeholder="Motif &eacute;cart" name="motif_ecart_${cpt}" type="string" value="${detailP.motif_ecart}"/>
							</div>
						<%}
					%>	
						<c:set var="cpt" value="${cpt+1 }" />
				</div>
			</c:forEach>
				</div>
				<div class="row" style="text-align: center;margin-top: 10px;">
					<div class="col-md-12">
					<c:if test="${inventaireDtail.size() > 0 }">
						<std:button actionGroup="M" classStyle="btn btn-success" action="stock.inventaire.saveSaisieInventaire" icon="fa-save" value="Mettre &agrave; jour" targetDiv="inventaireDetail_div" onClick="$('#close_modal').trigger('click');"/>
					</c:if>
						<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
							<i class="fa fa-times"></i> Fermer
						</button>
					</div>
				</div>
			</div>
		</div>
</std:form>