<%@page import="framework.model.common.util.StrimUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<%
boolean isRestau = "restau".equals(StrimUtil.getGlobalConfigPropertie("context.soft"));
%>

<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget" id="shift-div">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Options caisse</span>
			
			<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
				<i class="fa fa-times"></i> Fermer
			</button>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row" style="margin-left: 10px;margin-right: 0px;">
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Nom" />&nbsp;
					<div class="col-md-9">
						<std:text name="cmd.user" type="string" style="width:100%;" required="true" />
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Téléphone" />&nbsp;
					<div class="col-md-9">
						<std:text name="cmd.phone" type="string" mask="99-99-99-99-99" style="width:50%;" required="true" />
					</div>
				</div>		
				<div class="form-group">
					<std:label classStyle="control-label col-md-5" value="Mot de passe"/>&nbsp;
					<div class="col-md-7">
						<std:password name="cmd.password" placeholder="Mot de passe" type="string" style="width:140px;" required="true" maxlength="80" />
					</div> 
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-4" value="Recevoir les offres" />
					<div class="col-md-8">
						<std:checkbox name="opt_offre" checked="${paramVeille.valeur }" />
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-4" value="Recevoir les notifications commande" />
					<div class="col-md-8">
						<std:checkbox name="opt_notif" checked="${paramVeille.valeur }" />
					</div>
				</div>
			</div>	

		<div class="row" style="text-align: center;">
			<div class="col-md-12">
				<std:button actionGroup="M" classStyle="btn btn-success" action="caisse-web.caisseWeb.init_opts" closeOnSubmit="true" params="isSub=1" icon="fa-lock" style="border-radius: 37px;height: 42px;font-size: 18px;" value="Sauvegarder" />
			</div>
		</div>
	</div>
</div>
</std:form>