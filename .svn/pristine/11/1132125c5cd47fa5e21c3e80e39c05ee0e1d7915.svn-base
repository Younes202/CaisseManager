<%@page import="appli.model.domaine.stock.persistant.ChargeDiversPersistant"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

		<div class="widget" style="width: 95%;margin: 5px;">
			<div class="widget-body">
				<div class="row">
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" valueKey="chargeDivers.libelle" />
						<div class="col-md-6" style="text-align: left;text-align: left;">
							${mouvementBean.libelle}
						</div>
					</div>	
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" valueKey="chargeDivers.num_bl" />
						<div class="col-md-4" style="text-align: left;text-align: left;">
						    ${mouvementBean.num_bl}
						</div>

						<std:label classStyle="control-label col-md-2" valueKey="chargeDivers.date_mouvement" />
						<div class="col-md-4" style="text-align: left;text-align: left;">
							<fmt:formatDate value="${mouvementBean.date_mouvement}"/>
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Type" />
						<div class="col-md-4" style="text-align: left;text-align: left;">
						  ${mouvementBean.opc_famille_consommation.code} - ${mouvementBean.opc_famille_consommation.libelle} 
						</div>
						<std:label classStyle="control-label col-md-2" valueKey="chargeDivers.opc_fournisseur" />
						<div class="col-md-4" style="text-align: left;text-align: left;">
						${mouvementBean.opc_fournisseur.libelle}
						</div>
					</div>
					<!-- **************************** FINANCEMENT BLOC ********************** -->
					<c:set var="menu_scope.PAIEMENT_DATA" value="${mouvementBean.getList_paiement() }" scope="session" />
					<div class="form-group" id="finance_bloc">
						<jsp:include page="/domaine/compta/paiement_consult.jsp"></jsp:include>
					</div>
					<!-- **************************** FIN FINANCEMENT BLOC ********************** -->
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" valueKey="chargeDivers.montant" />
						<div class="col-md-4" style="text-align: left;text-align: left;">
						${mouvementBean.montant}
						</div>
						<std:label classStyle="control-label col-md-2" valueKey="chargeDivers.tva" />
						<div class="col-md-4" style="text-align: left;text-align: left;">
						${mouvementBean.opc_tva_enum.libelle}
						</div>
					</div>
					<div class="form-group">

						<std:label classStyle="control-label col-md-2" valueKey="chargeDivers.commentaire" />
						<div class="col-md-4" style="text-align: left;">
						${mouvementBean.commentaire}
						</div>
					</div>
				</div>
			</div>
	</div>
