<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue"> 
			<span class="widget-caption">Famille des boissons</span>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Famille boissons chaudes" />
					<div class="col-md-9">
						<std:select name="boisson_chaude" type="long" placeholder="Famille boisson chaude" data="${listFamille }" value="<%=ContextGloabalAppli.getEtablissementBean().getFam_boisson_chaude() %>" isTree="true" key="id" labels="code;'-';libelle" width="100%" />
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Famille boissons froides" />
					<div class="col-md-9">
						<std:select name="boisson_froide" type="long" placeholder="Famille boisson froide" data="${listFamille }" value="<%=ContextGloabalAppli.getEtablissementBean().getFam_boisson_froide() %>" isTree="true" key="id" labels="code;'-';libelle" width="100%" />
					</div>
				</div>
			</div>	
			<div class="row" style="text-align: center;">
				<div class="col-md-12">
					<std:button actionGroup="M" params="skipI=1&skipP=1" classStyle="btn btn-success" action="caisse.razPrint.config_boisson_raz" workId="${composantId }" icon="fa-save" value="Sauvegarder" />
					<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
						<i class="fa fa-times"></i> Fermer
					</button>
				</div>
			</div>
		</div>
	</div>
</std:form>