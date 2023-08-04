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
			<span class="widget-caption">Offrir des points</span>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Point &agrave; offrir" />
					<div class="col-md-9">
						<std:text name="carteFidelite.points" type="long" placeholder="Point &agrave; offrir" required="true" maxlength="10" style="width:120px;"/>
					</div>
				</div>
				<div class="row" style="text-align: center;">
					<div class="col-md-12">
						<std:button actionGroup="M" classStyle="btn btn-success" action="fidelite.carteFideliteClient.offrirPoints" icon="fa-save" value="Sauvegarder"  />
						<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
							<i class="fa fa-times"></i> Fermer
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</std:form>