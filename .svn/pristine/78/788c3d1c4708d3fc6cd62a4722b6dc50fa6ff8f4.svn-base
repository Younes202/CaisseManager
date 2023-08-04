<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<std:form name="data-form">
	<input type="hidden" name="w_f_act" value="<%=EncryptionUtil.encrypt("stock.inventaire.downloadInventairePDF") %>">

<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption"> Fiche inventaire
			</span>
		</div>
		<div class="widget-body">
		<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
				<div class="row">
					<div class="form-group">
						<std:label classStyle="control-label col-md-4" value="Emplacement" />
						<div class="col-md-6">
							<std:select name="emplacement.id" key="id" labels="titre" type="long" data="${listEmplacement}" width="100%"/>
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-4" value="Familles" />
						<div class="col-md-6">
							<std:select name="familles" key="id" labels="code;'-';libelle" type="string[]" data="${listFamille}" multiple="true" width="100%" isTree="true"/>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="modal-footer">
				<a href="javascript:" class="btn btn-sky shiny" target="downloadframe" onclick="$('#data-form').attr('action', 'front');$('#data-form').submit();$('#close_modal').trigger('click');">
					<i class="fa fa-print"></i>
					Imprimer
				</a>
				<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
					<i class="fa fa-times"></i> Fermer
				</button>
			</div>
		
	</std:form>
		