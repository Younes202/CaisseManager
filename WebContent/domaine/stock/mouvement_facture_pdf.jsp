<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<script type="text/javascript">
	function triggerDownLoadFacVente(isDet){
		if($.trim($("#nomClient").val()) == ''){
			return;
		}
		$("#isDet").val(isDet);
		$('#data-formGed').attr('action', 'front').attr("target", "downloadframe");
		$('#data-formGed').submit();
		$('#close_modal').trigger('click');
	}
	
	function triggerAjaxCli(sel){
		var cli = sel.value
		executePartialAjax($("#client"), '<%=EncryptionUtil.encrypt("stock-caisse.mouvementStock.init_pdf_facture")%>', 'bloc_client', "true", "true",'refresh=1&client='+cli, false);
	}
</script>

<std:form name="data-formGed">
	<input type="hidden" name="w_f_act" value="<%=EncryptionUtil.encrypt("stock-caisse.mouvementStock.editFactureVentePDF") %>">
	<input type="hidden" name="mvm" value="${mvmId }">
	<input type="hidden" name="src" value="${src_mvm }">
	<input type="hidden" name="isDet" id="isDet">

<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption"> Informations facture client
			</span>
		</div>
		<div class="widget-body">
		<div class="row">
			<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
		</div>
			<div class="row">
				<div class="alert alert-warning fade in">
                   <button class="close" data-dismiss="alert">
                       ×
                   </button>
                   <i class="fa-fw fa fa-warning"></i>
                   <strong>INFO</strong> Les factures éditées sont archivées dans la rubrique "DOCS"
               </div>
				<%if(ControllerUtil.getMenuAttribute("IS_MENU_CMLIENT", request) == null){%>
				<div class="form-group">
					<std:label classStyle="control-label col-md-4" value="Client" />
					<div class="col-md-6">
						<std:select onChange="triggerAjaxCli(this);" name="client" type="long" placeholder="S&eacute;lection du client" key="id" labels="nom;' ';prenom" data="${listClient }" width="300" />
					</div>
				</div>
				<%} %>
				<div id="bloc_client">
					<div class="form-group">
						<std:label classStyle="control-label col-md-4" value="Numéro" />
						<div class="col-md-6">
							<std:text name="numero" type="string" maxlength="150" placeholder="Numéro facture" />
							<span style="font-size: 10px;
							    color: orange;
							    margin-top: 0px;
							    float: left;">* Si vide, le numéro sera généré automatiquement.</span>
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-4" value="Date facture" />
						<div class="col-md-6">
							<std:date name="dateFac" placeholder="Date facture" value="${dateFac }" /> 
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-4" value="Nom client" />
						<div class="col-md-6">
							<std:text name="nomClient" required="true" type="string" maxlength="150" placeholder="Nom du client" value="${currClient.nom } ${currClient.prenom }" />
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-4" value="ICE client" />
						<div class="col-md-6">
							<std:text name="iceClient" type="string" maxlength="150" placeholder="ICE" value="${currClient.ice }" />
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-4" value="Adresse client" />
							<div class="col-md-6">
								<std:textarea name="adresseClient" rows="5" cols="35" maxlength="255" value="${currClient.getAdressFull() }"/>
							</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="modal-footer">
		<a href="javascript:" class="btn btn-sky shiny" onclick="triggerDownLoadFacVente(false);">
			<i class="fa fa-print"></i>
			Imprimer
		</a>
		<a href="javascript:" class="btn btn-sky shiny" onclick="triggerDownLoadFacVente(true);">
			<i class="fa fa-print"></i>
			Imprimer avec détail
		</a>
		<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
			<i class="fa fa-times"></i> Fermer
		</button>
	</div>
		
</std:form>
		