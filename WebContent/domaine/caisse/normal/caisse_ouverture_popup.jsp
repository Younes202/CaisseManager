<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<std:form name="data-form">
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Ouverture de la caisse</span>
		</div>
		<div class="widget-body">
		<div class="row">
			<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
		</div>
		<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-5" valueKey="caisseJournee.mtt_ouverture" />
					<div class="col-md-5">
						<std:text name="caisseJournee.mtt_ouverture_caissier" type="decimal" placeholderKey="caisseJournee.mtt_ouverture" required="true" maxlength="14" />
					</div>
				</div>
				<hr>
				<div class="row" style="text-align: center;">
					<div class="col-md-12">
						<std:button classStyle="btn btn-success" action="caisse.caisse.ouvrir_caisse" workId="${caisseId }" icon="fa-save" value="Ouvrir" />
						<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
							<i class="fa fa-times"></i> Fermer
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</std:form>