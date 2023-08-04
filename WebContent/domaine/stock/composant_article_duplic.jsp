<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.common.constante.ProjectConstante"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

	<!-- widget grid -->
	<div class="widget">
         <div class="widget-header bordered-bottom bordered-blue">
            <span class="widget-caption">Cr&eacute;er des fiches articles &agrave; partir des composants</span>
         </div>
         <div class="widget-body">
         	<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
	
         <div class="row">
			<std:form name="data-form">
				<input type="hidden" name="list_article_<%=ProjectConstante.CHECK_SAVE_STR%>" value="${listChecked }">
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" valueKey="article.opc_famille"/>
					<div class="col-md-9">
						<std:select name="famille_cuisine" type="long" key="id" labels="code;'-';libelle" data="${listeFamilleCuisine}" required="true" isTree="true" width="100%" />
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" valueKey="article.destination"/>
					<div class="col-md-8">
						<std:select name="destination" type="string" data="${listeDestination}" required="true" />
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Prix de vente"/>
					<div class="col-md-8">
						<std:text name="prixV" type="decimal" maxlength="15" style="width:150px;" />
					</div>
				</div>
				<div class="row" style="text-align: center;">
					<div class="col-md-12">
						<std:button actionGroup="M" classStyle="btn btn-success" action="stock.composant.dupliquer_fiche_article" workId="${emplacement.id }" icon="fa-save" value="Sauvegarder" />
						<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
							<i class="fa fa-times"></i> Fermer
						</button>
					</div>
				</div>
			</std:form>
		</div>
	</div>
</div>
