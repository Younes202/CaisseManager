<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.common.constante.ActionConstante"%>
<%@page import="framework.model.common.constante.ProjectConstante"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
#generic_modal_body{
	width: 720px;
	margin-left: -10%;
}
</style>

<script type="text/javascript">
	$(document).ready(function() {
		init_keyboard_events();
		
		$("#generer_code").off("click").on("click", function(){
			if($.trim($("#article\\.opc_famille_stock\\.id").val()) == ''){
				alertify.error("Veuillez s&eacute;lectionner une famille.");
				return;
			}
			executePartialAjax($(this), '<%=EncryptionUtil.encrypt("stock.composant.generer_code")%>', 'article.code', true, true, null, true);
		});
	});
</script>

<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Fiche article</span>
			<div class="widget-buttons buttons-bordered" style="margin-bottom: 10px;">
        		<i class="fa fa-keyboard-o" style="font-size: 20px;"></i>         
        		<label>
                 <input class="checkbox-slider toggle colored-blue" type="checkbox" id="keyboard-activator" style="display: none;">
                 <span class="text"></span>
             </label>
        	</div>
		</div>
         <div class="widget-body">
         	<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row" style="margin-left: 0px;">
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" valueKey="article.opc_famille"/>
					<div class="col-md-6">
						<std:select name="article.opc_famille_stock.id" type="long" key="id" labels="code;'-';libelle" data="${listeFaimlle}" required="true" isTree="true"  width="100%"/>
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" valueKey="article.reference" />
					<div class="col-md-2">
						<std:text name="article.code" type="string" placeholderKey="article.reference" style="width: 100px;float: left;" required="true" maxlength="50"/>
						<a class="refresh-num" id="generer_code" href="javascript:" title="G&eacute;n&eacute;rer la r&eacute;f&eacute;ce d'article">
			            	<i class="fa fa-refresh"></i>
			        	</a>
					</div>
					<std:label classStyle="control-label col-md-2" valueKey="article.libelle"/>
					<div class="col-md-6">
						<std:text name="article.libelle" type="string" placeholderKey="article.libelle" required="true" maxlength="80" style="width:90%;"/>
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" valueKey="article.code_barre"/>
					<div class="col-md-4">
						<std:text name="article.code_barre" type="string" placeholderKey="article.code_barre" style="width: 70%;" maxlength="50"/>
					</div>
				</div>
				
			<fieldset>
				<legend style="margin-left: 10px;">Informations de vente</legend>
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" valueKey="article.opc_unite_vente_enum"/>
					<div class="col-md-4">
						<std:select name="article.opc_unite_vente_enum.id" type="long" key="id" labels="libelle" data="${listeUnite}" width="150"/>
					</div>
					<std:label classStyle="control-label col-md-2" valueKey="article.unite_vente_quantite"/>
					<div class="col-md-4">
						<std:text name="article.unite_vente_quantite" type="string" placeholder="Unit&eacute; quantit&eacute;" style="width: 40%;text-align:right;" maxlength="20"/>
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-2" value="Prix vente TTC"/>
					<div class="col-md-4">
						<std:text name="article.prix_vente" type="string" placeholder="Prix vente" style="width: 40%;text-align:right;" maxlength="14" required="true"/>
					</div>
					<std:label classStyle="control-label col-md-2" valueKey="article.opc_tva_enum"/>
					<div class="col-md-4">
						<std:select name="article.opc_tva_enum.id" type="long" key="id" labels="libelle" data="${listeTva}" />
					</div>
				</div>
			</fieldset>
		</div>
			<hr>
			<div class="form-actions">
				<div class="row" style="text-align: center;" class="col-md-12" id="action-div">
					<std:button style="border-radius: 37px;height: 52px;font-size: 21px;" actionGroup="M" classStyle="btn btn-success" action="stock.composant.work_merge" closeOnSubmit="true" params="is_dupp=1&is_cai=1" icon="fa-save" value="Sauvegarder" />
					<button style="border-radius: 37px;height: 52px;font-size: 21px;" type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
						<i class="fa fa-times"></i> Fermer
					</button>
				</div>
				</div>
			</div>
		</div>
	</std:form>
	
<script type="text/javascript">
/*Handles ToolTips*/
$("[data-toggle=tooltip]")
    .tooltip({
        html: true
    });
</script>  
