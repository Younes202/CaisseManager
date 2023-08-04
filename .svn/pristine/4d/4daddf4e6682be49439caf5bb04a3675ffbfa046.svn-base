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
			<span class="widget-caption">Fiche portefeuille</span>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Montant recharge" />
					<div class="col-md-4">
						<std:text name="mtt_recharge" type="decimal" placeholder="Montant" maxlength="14"/>
					</div>	
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Mode paiement" />
					<div class="col-md-6">
						<std:select name="mode_paie" required="true" data="${list_mode_paiement }" forceWriten="true" type="string" width="100%"/>
					</div>
				</div>
				<div class="row" style="text-align: center;">
					<div class="col-md-12">
						<std:button actionGroup="M" classStyle="btn btn-success" action="fidelite.portefeuille2.save_recharge" icon="fa-save" value="Sauvegarder" />
<%-- 						<std:button actionGroup="D" classStyle="btn btn-danger" action="fidelite.portefeuille.delete_recharge" workId="${portefeuille.id }" icon="fa-trash-o" value="Supprimer" /> --%>
						<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
							<i class="fa fa-times"></i> Fermer
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</std:form>