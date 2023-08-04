<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
#generic_modal_body{
	width: 450px;
}
</style>

<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Ordonner l'arborescence</span>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row">
				<div class="col-md-8">
				
				</div>
				<div class="col-md-4" style="color: orange;">Ordre</div>
			</div>
			<c:set var="cpt" value="1" />
			<c:forEach items="${listGed }" var="tree">
				<div class="row" style="margin-top: 4px;">
					<div class="col-md-8">
						${tree.code }-${tree.libelle }
					</div>
					<div class="col-md-4">
						<std:text name="treeOrder_${tree.id }" type="long" placeholder="Ordre" maxlength="3" value="${cpt }" style="width: 50px;" />
					</div>
				</div>
				<c:set var="cpt" value="${cpt+1 }" />
			</c:forEach>
		</div>
		<div class="modal-footer">
			<std:button actionGroup="M" classStyle="btn btn-success" action="admin.ged.ordonner" icon="fa-save" value="Sauvegarder" />
			<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
				<i class="fa fa-times"></i> Fermer
			</button>
		</div>
	</div>

</std:form>
