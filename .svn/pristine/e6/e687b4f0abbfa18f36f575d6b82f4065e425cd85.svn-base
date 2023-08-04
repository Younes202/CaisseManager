<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
#generic_modal_body{
	width: 750px;
	margin-left: -25%;
}
.input_shift{
    text-align: right !important;
    height: 35px !important;
    font-size: 22px !important;
    font-weight: bold !important;
    border-color: #03a9f4 !important;
   }
</style>

<script type="text/javascript">
	$(document).ready(function() {
		init_keyboard_events();
		
		$("#shift-div input[type='text']").keyup(function (e) {
			checkNumeric(e, $(this));
		});
		$("#shift-div input[type='text']").focusout(function (e) {
			checkNumeric(e, $(this));
		});
		
		init_keyboard_events();
	});
	
	function checkNumeric(e, input){
		var re = /^([0-9]+[\.]?[0-9]?[0-9]?|[0-9]+)$/g;
	    var re1 = /^([0-9]+[\.]?[0-9]?[0-9]?|[0-9]+)/g;
	    //
	    if (!re.test(input.val())) {
	    	input.val(input.val().substring(0, input.val().length-1));
	    }
	}
</script>	
<%
String tp = (String)request.getAttribute("tp");
String labelBtn = (tp.equals("ouv") ? "Clore": "Ouvrir")+" le shift";
%>

<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget" id="shift-div">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption"><%=labelBtn %></span>
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
	<%if(tp.equals("ouv")){ %>		
			<div class="row" style="margin-left: 0px;margin-right: 0px;">
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Montant esp&egrave;ces" />
					<div class="col-md-3">
						<std:text autocomplete="false" name="caisseJournee.mtt_cloture_caissier_espece" type="string" classStyle="form-control input_shift" maxlength="12" style="text-align:right;border-radius: 21px !important;"/>
					</div>
					<std:label classStyle="control-label col-md-3" value="Montant carte" />
					<div class="col-md-3">
						<std:text autocomplete="false" name="caisseJournee.mtt_cloture_caissier_cb" type="string" classStyle="form-control input_shift" maxlength="12" style="text-align:right;border-radius: 21px !important;" />
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Montant ch&egrave;que" />
					<div class="col-md-3">
						<std:text autocomplete="false" name="caisseJournee.mtt_cloture_caissier_cheque" type="string" classStyle="form-control input_shift" maxlength="12" style="text-align:right;border-radius: 21px !important;" />
					</div>
					<std:label classStyle="control-label col-md-3" value="Montant ch&egrave;que d&eacute;j." />
					<div class="col-md-3">
						<std:text autocomplete="false" name="caisseJournee.mtt_cloture_caissier_dej" type="string" classStyle="form-control input_shift" maxlength="12" style="text-align:right;border-radius: 21px !important;" />
					</div>
				</div>
			</div>
			<div class="row" style="margin-left: 0px;margin-right: 0px;">	
				<div class="form-group">
					<std:label classStyle="control-label col-md-5" value="Total" style="font-weight: bold;color: #ed4e2a;font-size: 18px;" />
					<div class="col-md-3">
						<std:text autocomplete="false" name="caisseJournee.mtt_cloture_caissier" type="string" classStyle="form-control input_shift" maxlength="12" required="true" style="text-align:right;border-radius: 21px !important;" />
					</div>
				</div>
			</div>	
			
			<c:if test="${isPass }">
					<div class="alert alert-warning fade in">
			             <i class="fa-fw fa fa-warning"></i>
			             <span>
			             	Des commandes en attente ou non encaissées ont été trouvées. Merci de sélectionner le caissier pour 
			             	la passation et le montant d'ouverture du prochain shift 
			             </span>
			        </div>     
					<div class="form-group">
						<std:label classStyle="control-label col-md-5" valueKey="caisseJournee.mtt_ouverture" />
						<div class="col-md-5">
							<std:text autocomplete="false" name="mttOuvertureCaissier" type="string" classStyle="form-control input_shift" placeholderKey="caisseJournee.mtt_ouverture" required="true" maxlength="14" style="text-align:right;border-radius: 21px !important;"/>
						</div>
					</div>	
					<div class="form-group">
						<std:label classStyle="control-label col-md-5" value="Prochain caissier"/>&nbsp;
						<div class="col-md-7" style="margin-top: -15px;">
							<std:select name="userPass.id" type="long" style="width:100%;font-size: 25px;" required="true" data="${listUser }" key="id" labels="login" />
						</div>
					</div>
				</c:if>			
		<%} else{ %>
			<div class="row" style="margin-left: 0px;margin-right: 0px;">
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Montant esp&egrave;ces" />
					<div class="col-md-3">
						<std:text autocomplete="false" name="caisseJournee.mtt_cloture_caissier_espece" type="string" classStyle="form-control input_shift" maxlength="12" style="text-align:right;border-radius: 21px !important;" />
					</div>
					
				</div>
			</div>	
		<%} %>

		<div class="row" style="text-align: center;">
			<div class="col-md-12">
			<%
			tp = "tp="+tp+"&isPass="+StringUtil.getValueOrEmpty(request.getAttribute("isPass"));
			%>
				<std:button actionGroup="M" classStyle="btn btn-success" action="caisse-web.caisseWeb.ouvrirCloreShift" params="<%=tp %>" icon="fa-lock" style="border-radius: 37px;height: 52px;font-size: 21px;" value="<%=labelBtn %>" />
				<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal" style="border-radius: 37px;height: 52px;font-size: 21px;">
					<i class="fa fa-times"></i> Fermer
				</button>
			</div>
		</div>
	</div>
</div>
</std:form>