<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
.form-title {
	margin-left: 12px;
}
</style>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Finance</li>
		<li>Compte bancaire</li>
		<li class="active">Edition</li>
	</ul>
</div>

<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link actionGroup="U" classStyle="btn btn-default" action="admin.compteBancaire.work_init_update" workId="${compteBancaire.id }" icon="fa-3x fa-pencil" tooltip="Modifier" />
		<std:link classStyle="btn btn-default" action="admin.compteBancaire.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
	</div>
	<!--Header Buttons-->
	<jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
	<!--Header Buttons End-->
</div>
<!-- /Page Header -->

<div class="page-body">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>
	<!-- widget grid -->
	<div class="widget">
		<std:form name="data-form">
			<div class="row">
		        <div class="col-lg-12 col-sm-12 col-xs-12">
		              <div class="tabbable">
		                    <ul class="nav nav-tabs" id="myTab">
		                          <li class="active">
		                              <a data-toggle="tab" href="#data" wact="<%=EncryptionUtil.encrypt("admin.compteBancaire.work_edit")%>">
		                               D&eacute;tail
		                              </a>
		                           </li>
		                        <c:if test="${not empty compteBancaire.id }">
		                            <li>
		                              <a data-toggle="tab" href="#data" wact="<%=EncryptionUtil.encrypt("admin.compteBancaire.find_ecriture_journal")%>">
		                               Mouvements du compte
		                              </a>
		                            </li>
		                        </c:if>
		                     </ul>
		                </div>
		          </div>
		      </div>
		
			<div class="widget-body">
				<div class="row">
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" valueKey="compteBancaire.libelle" />
						<div class="col-md-4">
							<std:text name="compteBancaire.libelle" type="string" style="width:50%;" maxlength="80" required="true" />
						</div>
						<std:label classStyle="control-label col-md-2" valueKey="compteBancaire.titulaire" />
						<div class="col-md-4">
							<std:text name="compteBancaire.titulaire" type="string" maxlength="50" />
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" valueKey="compteBancaire.banque" />
						<div class="col-md-4">
							<std:text name="compteBancaire.banque" required="true" placeholderKey="compteBancaire.banque" type="string" maxlength="80" />
						</div>
						<std:label classStyle="control-label col-md-2" value="Type" />
						<div class="col-md-4">
							<std:select name="compteBancaire.type_compte" required="true" type="string" data="${typeCompteArray }" width="100%" />
						</div>
					</div>
					<div class="form-title">Informations du RIB</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="" addSeparator="false"/>
						<div class="col-md-10">
							<table>
								<tr>
							<td style="padding-left: 20px;"><std:text name="compteBancaire.rib_banque" type="string" style="width:70px;" maxlength="5" placeholderKey="compteBancaire.rib_banque" /></td>
							<td style="padding-left: 20px;"><std:text name="compteBancaire.rib_guichet" type="string" style="width:70px;" maxlength="5" placeholderKey="compteBancaire.rib_guichet" /></td>
							<td style="padding-left: 20px;"><std:text name="compteBancaire.rib_numero" type="string" style="width:150px;;" maxlength="16" placeholderKey="compteBancaire.rib_numero" /></td>
							<td style="padding-left: 20px;"><std:text name="compteBancaire.rib_cle" type="string" style="width:50px;" maxlength="2" placeholderKey="compteBancaire.rib_cle" /></td>
								</tr>
							</table>
						</div>
					</div>
					<div class="form-title">Adresse</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" valueKey="adresse.adresse" />
						<div class="col-md-4">
							<std:text name="compteBancaire.adresse_rue" type="string" placeholderKey="adresse.rue" style="width:90%;" maxlength="120" />
						</div>
						<std:label classStyle="control-label col-md-2" valueKey="adresse.complement" />
						<div class="col-md-4">
							<std:text name="compteBancaire.adresse_compl" type="string" placeholderKey="adresse.complement" style="width:90%;" maxlength="120" />
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" valueKey="adresse.ville" />
						<div class="col-md-4">
							<std:select name="compteBancaire.opc_ville.id" type="long" data="${listKey }" key="id" labels="libelle" placeholderKey="adresse.ville" style="width:70%;" />
						</div>
					</div>
				</div>

				<!-- Generic Form -->
				<jsp:include page="/domaine/administration/dataValue_form.jsp" />
				
			</div>
			<hr>
			<div class="form-actions">
				<div class="row" style="text-align: center;" class="col-md-12" id="action-div">
					<std:button actionGroup="M" classStyle="btn btn-success" action="admin.compteBancaire.work_merge" workId="${compteBancaire.id }" icon="fa-save" value="Sauvegarder" />
					<std:button actionGroup="D" classStyle="btn btn-danger" action="admin.compteBancaire.work_delete" workId="${compteBancaire.id }" icon="fa-trash-o" value="Supprimer" />
				</div>

			</div>
		</std:form>
	</div>
</div>
