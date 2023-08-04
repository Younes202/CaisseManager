<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
#generic_modal_body{
	width: 920px;
	margin-left: -10%;
}
</style>

<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Fusionner article</span>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Aticle origine" />
					<div class="col-md-9">
						<std:select name="article_src" type="long" placeholder="Article origine" data="${listArticle }" key="id" labels="code;'-';libelle" required="true" />
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-3" value="Remplaçer par" />
					<div class="col-md-9">		
						<std:select name="article_dest" type="long" placeholder="Article destination" data="${listArticle }" key="id" labels="code;'-';libelle" required="true" />
					</div>	
				</div>
			</div>					
				<div class="row" style="text-align: center;">
					<div class="col-md-12">
						<std:button actionGroup="M" classStyle="btn btn-success" params="isSub=1" action="stock.composant.fusionner_article" icon="fa-save" value="Fusionner" />
						<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
							<i class="fa fa-times"></i> Fermer
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</std:form>