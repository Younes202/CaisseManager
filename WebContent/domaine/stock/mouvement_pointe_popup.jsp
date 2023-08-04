<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue" style="padding-bottom: 5px; padding-top: 5px; padding-right: 5px;">
			<span class="widget-caption">Pointage des ch&egrave;ques</span>
		</div>

		<div class="widget-body">
		     <div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			
			<div class="row">
					<div class="form-group">
						<std:label classStyle="control-label col-md-5" valueKey="mouvement.date_encaissement" />
						<div class="col-md-5">
							<std:date name="date_encaissement" value="${currentDate }" required="true" />
						</div>
					</div>
					<div class="row" style="text-align: center;">
						<div class="col-md-12">
							<std:button classStyle="btn btn-success" action="admin.compteBancaire.pointerCheque" workId="${elementId }" icon="fa-save" value="Pointer" />
							<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
								<i class="fa fa-times"></i> Fermer
							</button>
						</div>
					</div>
			</div>
		</div>
	</div>
</std:form>