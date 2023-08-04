<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
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

<%
boolean isRestau = ContextAppli.IS_RESTAU_ENV();
%>
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
	
	$("#invFilter").bind('click', function(e) {
	    submitAjaxForm('<%=EncryptionUtil.encrypt("caisse-web.caisseInventaire.init_inventaire")%>', 'isSub=1&famInvParams='+$("#famInvParams").val()+'&famInvFilter='+$("#famInvFilter").val()+'&artInvFilter='+$("#artInvFilter").val(), null, null, 'generic_modal_body');
	});
	
	setTimeout(() => {
		$("#artInvFilter").focus();
		var strLength = $("#artInvFilter").val().length * 2;
		$("#artInvFilter")[0].setSelectionRange(strLength, strLength);
	}, 1000);
});	
</script>

<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget" style="width: 1050px;">
		<div class="widget-header bordered-bottom bordered-blue">
			<div>
				<span style="float: left;line-height:38px;" class="widget-caption">Familles inventaire : </span>
				<div style="width: 300px;float: left;margin-top: 3px;margin-right: 30px;">	
					<std:select name="famInvParams" width="100%" style="
					    margin-top: 1px;
					    margin-left: 19px;
					    float: left;
					    border: 2px solid #a59a9a;" multiple="true" placeholder="Famille" type="long[]" key="id" labels="code;'-';libelle" 
					    	data="${listeFamille}" value="${famInvParams }" isTree="true" />
				 </div>
				 
				 <span style="float: left;line-height:38px;" class="widget-caption">Filtre familles : </span>
				<div style="width: 150px;float: left;margin-top: 3px;margin-right: 30px;">	
					<std:select name="famInvFilter" width="100%" style="
					    margin-top: 1px;
					    margin-left: 19px;
					    float: left;
					    border: 2px solid #a59a9a;" multiple="true" placeholder="Famille" type="long[]" key="id" labels="code;'-';libelle" 
					    	data="${listeFamille}" value="${famInvFilter }" isTree="true" />
				 </div>
				<span class="widget-caption" style="float: left;line-height:38px;">Filtre article : </span>
				 <std:text name="artInvFilter" type="string" style="width: 190px;
				 		float: left;
					    height: 35px;
					    margin-top: 1px;
					    float: left;
					    border: 2px solid #a59a9a;" value="${artInvFilter }" />
	    
	    		<std:button id="invFilter" icon="fa-search" style="float: left;
    height: 33px;
    margin-top: 2px;
    margin-left: 4px;" />
    		</div>
    
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
			<div class="row" style="max-height: 650px;overflow-y: auto;font-size: 12px;background-color: #eaeaea;margin-left: -10px;margin-right: -10px;">
				<c:set var="cpt" value="${1 }" />
				<c:set var="encodeService" value="<%=new EncryptionUtil()%>" />
				<c:set var="controllerUtil" value="<%=new ControllerUtil()%>" />
				<c:set var="oldfamille" value="${null }" />
				<c:set var="isRestau" value="<%=isRestau %>" />
			
			<c:forEach items="${mapInfo }" var="detail">
				<c:set var="infoP" value="${detail.value }" />
				<c:set var="artP" value="${detail.value.opc_article }" />
				
				<c:choose>
					<c:when test="${isRestau }">
						<c:set var="famille" value="${artP.opc_famille_cuisine }" />	
					</c:when>
					<c:otherwise>
						<c:set var="famille" value="${artP.opc_famille_stock }" />
					</c:otherwise>
				</c:choose>
			
				<std:hidden name="eaiid_${cpt}" value="${infoP.id }" />
				<std:hidden name="opc_article.id_${cpt}" value="${artP.id }" />
				<std:hidden name="article_lib_${cpt}" value="${artP.code}-${artP.libelle}"/>
							
				<c:if test="${empty oldfam or famille.id != oldfam}">
				     <div class="col-md-12 col-lg-12" style="background-color: #00BCD4;margin-top: 4px;margin-bottom: 5px;">
						<span class="fa fa-fw fa-folder-open-o separator-icon"></span> ${famille.libelle}
					</div>
				</c:if>	
				<c:set var="oldfam" value="${famille.id }"></c:set>
				
				<div class="col-md-6 col-lg-6" style="line-height: 30px;border-right: 2px solid #FF9800;border-bottom: 1px dashed #E91E63;padding-left: 5px;padding-right: 3px;">
					<div class="col-md-5 col-lg-5 ellipsify" style="white-space: nowrap;overflow: hidden;text-overflow: ellipsis;">
						<span style="color: blue;">${artP.libelle}</span>
					</div>
					<div class="col-md-1 col-lg-1" style="color: #9c27b0;">
						<span style="float: right;margin-right: 17px;">
							<fmt:formatDecimal value="${infoP.qte_reel }" />
						</span>
					</div>
					<div class="col-md-2 col-lg-2 reel_qte">
						<std:text style="padding:2px;height:25px;width: 75px;margin-top: 2px;font-size: 12px;" placeholder="Quantit&eacute;" type='decimal' name="qte_reel_${cpt}" value="${infoP.qte_reel }"/>	
					</div>	
					<c:set var="cpt" value="${cpt+1 }" />
				</div>
				
			</c:forEach>
				</div>
				<div class="row" style="text-align: center;margin-top: 10px;">
					<div class="col-md-12">
						<std:button actionGroup="M" classStyle="btn btn-success" action="caisse-web.caisseInventaire.saveSaisieInventaire" params="isSub=1" icon="fa-save" value="Mettre &agrave; jour" targetDiv="inventaireDetail_div" />
						<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
							<i class="fa fa-times"></i> Fermer
						</button>
					</div>
				</div>
			</div>
		</div>
</std:form>