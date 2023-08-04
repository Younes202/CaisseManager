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
			<span class="widget-caption">Cl&ocirc;turer définitivement la caisse</span>
		</div>
		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			<div class="row">
				<div class="form-group">
					<std:label classStyle="control-label col-md-5" value="Montant esp&egrave;ces" />
					<div class="col-md-5">
						<std:text autocomplete="false" name="caisseJournee.mtt_cloture_caissier_espece" type="decimal" placeholder="Montant esp&egrave;ces" required="true" maxlength="14"/>
					</div>
				</div>
				<div class="form-group">
					<std:label classStyle="control-label col-md-5" value="Montant carte" />
					<div class="col-md-5">
						<std:text autocomplete="false" name="caisseJournee.mtt_cloture_caissier_cb" type="decimal" placeholder="Montant carte" required="true" maxlength="14"/>
					</div>
				</div>	
				<div class="form-group">
					<std:label classStyle="control-label col-md-5" value="Montant ch&egrave;que" />
					<div class="col-md-5">
						<std:text autocomplete="false" name="caisseJournee.mtt_cloture_caissier_cheque" type="decimal" placeholder="Montant ch&egrave;que" required="true" maxlength="14"/>
					</div>
				</div>	
				<div class="form-group">
					<std:label classStyle="control-label col-md-5" value="Montant ch&egrave;que déj." />
					<div class="col-md-5">
						<std:text autocomplete="false" name="caisseJournee.mtt_cloture_caissier_dej" type="decimal" placeholder="Montant ch&egrave;que déj" required="true" maxlength="14"/>
					</div>
				</div>	
			
				<div class="form-group">
					<div class="col-md-12">
						<span style="font-size: 11px;color: orange;margin-left: 10px;">
							${rect != 1 ? "Attention : il ne sera pas possible d'annuler cette opération":"Saisissez uniquement si écart"}.
						</span>
					</div>	
				</div>
				
				<c:if test="${isPass }">
					<div class="alert alert-warning fade in">
			             <button class="close" data-dismiss="alert">
			                 x
			             </button>
			             <i class="fa-fw fa fa-warning"></i>
			             <span>
			             	Des commandes en attente ou non encaissées ont été trouvées. Merci de sélectionner le caissier pour 
			             	la passation et le montant d'ouverture du prochain shift 
			             </span>
			        </div>     
					<div class="form-group">
						<std:label classStyle="control-label col-md-5" valueKey="caisseJournee.mtt_ouverture" />
						<div class="col-md-5">
							<std:text autocomplete="false" name="mttOuvertureCaissier" type="string" placeholderKey="caisseJournee.mtt_ouverture" required="true" maxlength="14" style="text-align:right;border-radius: 21px !important;" />
						</div>
					</div>
					<div class="form-group">	
						<std:label classStyle="control-label col-md-3" value="Prochain caissier" style="font-weight:bold;font-size: 19px;"/>&nbsp;
						<div class="col-md-9" style="margin-top: -15px;">
							<std:select name="userPass.id" type="long" style="width:100%;font-size: 25px;" required="true" data="${listUser }" key="id" labels="login" />
						</div>
					</div>
				</c:if>				
				
				
					<div class="row" style="text-align: center;">
						<div class="col-md-12">
							<std:button classStyle="btn btn-success" action="caisse.caisse.cloturer_definitive" workId="${caisseId }" params="rect=${isRectMode }&isPass=${isPass }" icon="fa-save" value="${isRectMode ? 'Valider les montants':'Cl&ocirc;turer la caisse' }" />
							<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
								<i class="fa fa-times"></i> Fermer
							</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</std:form>