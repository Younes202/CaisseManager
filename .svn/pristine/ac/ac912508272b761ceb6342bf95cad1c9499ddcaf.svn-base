<%@page import="appli.controller.domaine.util_erp.ContextAppli.TYPE_LIGNE_COMMANDE"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementPersistant"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>
	
<%
CaisseMouvementPersistant caisseMouvement = (CaisseMouvementPersistant)request.getAttribute("caisseMouvement");
Boolean isEdit = (Boolean)request.getAttribute("isEdit");
Boolean isUpd = (Boolean)request.getAttribute("isUpd");
%>	

<script type="text/javascript">
	$(document).ready(function (){
		$('#div_btn_rt button').click(function(){
			$("#div_RC, #div_CB").hide();
			
			if($(this).attr("name") == 'L'){
				submitAjaxForm('<%=EncryptionUtil.encrypt("stock-caisse.mouvementStock.manageRetourCaisseCommande")%>', 'isCai=1', $("#data-form"), $(this), 'left-div');
				$("#close_modal").trigger("click");
			} else{
				$("#div_"+$(this).attr("name")).show();	
			}
		});
		<%-- Code barre --%>
		var barcode="";
	    $(document).keydown(function(e) {
	        var code = (e.keyCode ? e.keyCode : e.which);
	        var sourceEvent = $(e.target).prop('nodeName');
	        var isInput = (sourceEvent == 'INPUT') ? true : false;
	        //
	        if(!isInput && code==13 && $.trim(barcode) != ''){
	        	if($.trim(barcode).length > 5){
	        		e.preventDefault();
		        	submitAjaxForm('<%=EncryptionUtil.encrypt("stock-caisse.mouvementStock.manageRetourCaisseCommande")%>', 'isCai=1&tpRetour=CB&cb='+barcode, $("#data-form"), null, 'left-div');
	        	}
	            barcode="";
	        } else{
	  			 barcode = barcode + String.fromCharCode(code);
	        }
	    });
	});
</script> 


<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Retour commande</span>
			<img class="imgBarCode" src="resources/framework/img/barcode_scanner.png" style="width: 20px" title="Lecteur code barre utilisable sur cet écran">
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row" style="margin-left: 0px;">
				<div class="form-group">
					<div class="col-md-12" id="div_btn_rt">
						<std:button name="CB" value="Par code barre" style="background-color: #e0f7fa;height: 58px;border-radius: 15px;width: 152px;font-size: 17px;margin-left: 10%;float: left;" />
						<std:button name="RC" value="Par référence" style="background-color: #fce4ec;height: 58px;border-radius: 15px;width: 152px;font-size: 17px;margin-left: 15px;float: left;" />
						<std:button name="L" value="Par saisie libre" style="height: 58px;border-radius: 15px;width: 152px;font-size: 17px;margin-left: 15px;float: left;" />
					</div>
				</div>	
			</div>
			<div class="row" style="margin-left: 0px;">
				<div class="form-group" id="div_RC" style="display: none;">
					<std:label classStyle="control-label col-md-4" value="Référence commande" />
					<div class="col-md-8">
						<std:text name="ref" type="string" placeholder="R&eacute;f&eacute;rence" maxlength="50" style="width:190px;float: left;" />
						<std:link action="stock-caisse.mouvementStock.manageRetourCaisseCommande" closeOnSubmit="true" style="height: 33px;margin-left: 5px;" actionGroup="C" targetDiv="left-div" icon="fa-search" classStyle="btn btn-sm btn-primary" />
					</div>
				</div>
				<div class="form-group" id="div_CB" style="display: none;">
					<std:label classStyle="control-label col-md-4" value="Code barre" />
					<div class="col-md-8">
						<std:text name="cb" type="string" placeholder="Code barre" maxlength="50" style="width:190px;float: left;" />
						<std:link action="stock-caisse.mouvementStock.manageRetourCaisseCommande" closeOnSubmit="true" style="margin-left: 5px;height: 33px;" targetDiv="left-div" actionGroup="C" icon="fa-search" classStyle="btn btn-sm btn-primary" />
					</div>
				</div>
			</div>
			<hr>
			<div class="row" style="text-align: center;margin-top: 10px;">
				<div class="col-md-12">
					<button style="border-radius: 37px;height: 52px;font-size: 21px;" type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
						<i class="fa fa-times"></i> Fermer
					</button>
				</div>
			</div>
		</div>	
	</div>
	</std:form>
<script src="resources/framework/js/fuelux/spinbox/fuelux.spinbox.min.js"></script>
