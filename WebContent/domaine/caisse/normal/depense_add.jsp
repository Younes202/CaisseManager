<%@page import="framework.model.util.FileUtil"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="appli.model.domaine.stock.service.IArticleService"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="appli.controller.domaine.stock.bean.ChargeDiversBean"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<script src="resources/framework/js/util_file_upload.js?v=1.2"></script>

<script type="text/javascript">
	$(document).ready(function() {
		$("#generer_bl").click(function(){
			executePartialAjax($(this), '<%=EncryptionUtil.encrypt("stock.chargeDivers.generer_numBL")%>', 'chargeDivers.num_bl', true, true, null, true);
		});
		$("#chargeDivers\\.type").change(function(){
			manageDep();
		});
		setTimeout(function(){
			manageDep();
		}, 1000);
		
		$(".spinbox-up").click(function(){
			$("#quantite_custom").val(parseInt($("#quantite_custom").val())+1);
		});
		$(".spinbox-down").click(function(){
			if($("#quantite_custom").val() != '' && parseInt($("#quantite_custom").val()) > 1){
				$("#quantite_custom").val(parseInt($("#quantite_custom").val())-1);
			}
		});
	});
	function manageDep(){
		if($("#chargeDivers\\.type").val() == 'A'){
			$("#div_dep").show(1000);
		} else{
			$("#div_dep").hide();
		}
	}
</script>

<std:form name="data-form">
	<!-- widget grid -->
	<div class="widget">
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Vente/achat</span>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row">
					<div style="display: none;">
					<std:select name="num_cheque_all" type="long" key="id" labels="num_cheque" hiddenkey="opc_fournisseur.id" data="${listChequeFournisseur }" style="display:none;" />
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" valueKey="chargeDivers.libelle" />
						<div class="col-md-4">
							<std:text name="chargeDivers.libelle" type="string" placeholderKey="chargeDivers.libelle" maxlength="120"/>
						</div>
						
						<std:label classStyle="control-label col-md-2" value="OU Libellé prédéfini" />
						<div class="col-md-4">
							<std:select mode="std" name="chargeDivers.libelle2" type="string" data="${typeLibPredef}" width="100%"/>
						</div>
					</div>	
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Prix unitaire" />
						<div class="col-md-4">
							<std:text name="chargeDivers.montant" type="decimal" placeholder="Prix unitaire" maxlength="14" style="width: 150px;" required="true"/>
						</div>
					</div>
					<div class="form-group">	
						<std:label classStyle="control-label col-md-2" value="Quantité" />
						<div class="col-md-2">
							 <div class="spinbox spinbox-horizontal spinbox-two-sided">
			                      <div class="spinbox-buttons btn-group spinbox-buttons-left">
			                          <button type="button" class="btn spinbox-down danger">
			                              <i class="fa fa-minus"></i>
			                          </button>
			                      </div>
			                      <std:text name="quantite_custom" type="long" style="width: 90%;text-align:center !important;" value="1" classStyle="spinbox-input form-control" />
			                      <div class="spinbox-buttons btn-group spinbox-buttons-right">
			                          <button type="button" class="btn spinbox-up blue">
			                              <i class="fa fa-plus"></i>
			                          </button>
			                      </div>
			                  </div>
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Type" />
						<div class="col-md-4">
							<std:select mode="std" name="chargeDivers.type" addBlank="false" required="true" type="string" data="${typeCharge}" width="50%"/>
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" valueKey="chargeDivers.commentaire" />
						<div class="col-md-4">
							<std:textarea name="chargeDivers.commentaire" rows="5" cols="50" />
						</div>
					</div>
				</div>
				<!-- Generic Form -->
				<jsp:include page="/domaine/administration/dataValue_form.jsp" />
				<div class="row" style="text-align: center;">
					<div class="col-md-12">
						<std:button style="border-radius: 37px;height: 52px;font-size: 21px;" targetDiv="left-div" actionGroup="M" closeOnSubmit="true" classStyle="btn btn-success" action="caisse-web.caisseWeb.merge_depense" params="isCai=1" icon="fa-save" value="Sauvegarder" />
						<button style="border-radius: 37px;height: 52px;font-size: 21px;" type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
							<i class="fa fa-times"></i> Fermer
						</button>
					</div>
				</div>
			</div>
		</div>
</std:form>

