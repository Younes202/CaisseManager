<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>


<std:form name="data-form">
		<div class="widget">
	         <div class="widget-header bordered-bottom bordered-blue">
	            <span class="widget-caption">Fiche token</span>
	            <std:link actionGroup="U" targetDiv="generic_modal_body" classStyle="btn btn-default" action="admin.token.work_init_update" workId="${token.id}" icon="fa fa-pencil" tooltip="Cr&eacute;er" />
	         </div>
	         <div class="widget-body">
	         	<div class="row">
					<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
				</div>
				<div class="row">
					<div class="form-group">
	
						<std:label classStyle="control-label col-md-3" valueKey="token.reference"/>
						<div class="col-md-6">
							<std:text name="token.reference" type="string" placeholderKey="token.reference" required="true" maxlength="50" style="width: 80%;"/>
						</div>
					</div>
					<div class="form-group">
	
						<std:label classStyle="control-label col-md-3" valueKey="token.libelle"/>
						<div class="col-md-6">
							<std:text name="token.libelle" type="string" placeholderKey="token.libelle" required="true" maxlength="120"/>
						</div>
					</div>
					<div class="form-group">
	
						<std:label classStyle="control-label col-md-3" valueKey="token.opc_user"/>
						<div class="col-md-9">
							<std:select name="token.opc_user.id" type="long" key="id" labels="login" data="${listeUser}" />
						</div>
					</div>
						<div class="row" style="text-align: center;">
							<div class="col-md-12">
								<std:button actionGroup="M" classStyle="btn btn-success" action="admin.token.work_merge" workId="${token.id }" icon="fa-save" value="Sauvegarder" />
								<std:button actionGroup="D" classStyle="btn btn-danger" action="admin.token.work_delete" workId="${token.id }" icon="fa-trash-o" value="Supprimer" />
								<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
									<i class="fa fa-times"></i> Fermer
								</button>
						</div>
					</div>
			</div>
		</div>
	</div>
</std:form>	