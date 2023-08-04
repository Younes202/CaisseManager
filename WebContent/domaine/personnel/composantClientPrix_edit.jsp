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
			<span class="widget-caption">Fiche offre prix</span>
			<std:link actionGroup="U" targetDiv="generic_modal_body" classStyle="btn btn-default" action="pers.composantClientPrix.work_init_update" workId="${composantClientPrix.id}" icon="fa fa-pencil" tooltip="Cr&eacute;er" />
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<std:hidden name="composantClientPrix.opc_client.id" value="${currClient }"/>
			
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Article" />
					<div class="col-md-9">
						<std:select type="long" data="${listArticle }" labels="code;'-';libelle" key="id" name="composantClientPrix.opc_article.id" required="true" />
					</div>
				</div>	
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Prix vente" />
					<div class="col-md-9">		
						<std:text type="decimal" required="true" name="composantClientPrix.mtt_prix" placeholder="Prix" maxlength="14" style="width: 150px;" />
					</div>	
				</div>
					
				<div class="row" style="text-align: center;">
					<div class="col-md-12">
						<std:button actionGroup="M" classStyle="btn btn-success" action="pers.composantClientPrix.work_merge" workId="${composantClientPrix.id }" icon="fa-save" value="Sauvegarder" />
						<std:button actionGroup="D" classStyle="btn btn-danger" action="pers.composantClientPrix.work_delete" workId="${composantClientPrix.id }" icon="fa-trash-o" value="Supprimer" />
						<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
							<i class="fa fa-times"></i> Fermer
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</std:form>