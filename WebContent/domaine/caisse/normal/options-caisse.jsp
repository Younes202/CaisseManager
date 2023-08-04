<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<%
boolean isRestau = ContextAppli.IS_RESTAU_ENV();
%>
<style>
#generic_modal_body{
	width: 750px;
	margin-left: -25%;
}
</style>

<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget" id="shift-div">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Options caisse</span>
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
			<div class="row" style="margin-left: 0px;margin-right: 0px;">
				<%if(isRestau){ %>
				<div class="form-group">
					<std:label classStyle="control-label col-md-4" value="Code wifi" />
					<div class="col-md-8">
						<std:text name="code_wifi" type="string" classStyle="form-control" maxlength="150" style="border-radius: 21px !important;float: left;width:80%;" value="${paramWifi.valeur }" />
						<std:button action="caisse-web.caisseWeb.printCodeWifi" targetDiv="div_gen_printer" closeOnSubmit="true" icon="fa-print" />
					</div>
				</div>
				<%} %>
				<div class="form-group">
					<std:label classStyle="control-label col-md-4" value="Hauteur bloc famille(px)" />
					<div class="col-md-8">
						<std:text name="hauteur_fam" type="long" classStyle="form-control" maxlength="5" style="border-radius: 21px !important;float: left;width:50%;" value="${paramHauteur.valeur }" />
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-4" value="Eléments par page" />
					<div class="col-md-8">
						<std:text name="param_nbr_fam" type="long" classStyle="form-control" maxlength="2" style="border-radius: 21px !important;float: left;width:50%;" value="${paramPagger.valeur }" />
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-4" value="Afficher image mise en veille" />
					<div class="col-md-8">
						<std:checkbox name="param_veille" checked="${paramVeille.valeur }" />
					</div>
				</div>
			</div>	

		<div class="row" style="text-align: center;">
			<div class="col-md-12">
				<std:button actionGroup="M" classStyle="btn btn-success" action="caisse-web.caisseWeb.init_opts" closeOnSubmit="true" params="isSub=1" icon="fa-lock" style="border-radius: 37px;height: 52px;font-size: 21px;" value="Sauvegarder" />
				<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal" style="border-radius: 37px;height: 52px;font-size: 21px;">
					<i class="fa fa-times"></i> Fermer
				</button>
			</div>
		</div>
	</div>
</div>
</std:form>