<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion des cartes</li>
		<li>Fiche carte</li>
		<li class="active">Edition</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link actionGroup="U" classStyle="btn btn-default" action="fidelite.carteFidelite.work_init_update" workId="${carteFidelite.id }" icon="fa-3x fa-pencil" tooltip="Modifier" />
		<std:link classStyle="btn btn-default" action="fidelite.carteFidelite.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
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
	<std:form name="data-form">
		<!-- widget grid -->
		<div class="widget">
		
<%if(!ControllerUtil.isEditionCreateAction(request)){ %>		
	<div class="row">
        <div class="col-lg-12 col-sm-12 col-xs-12">
              <div class="tabbable">
                    <ul class="nav nav-tabs" id="myTab">
                          <li class="active">
                              <a data-toggle="tab" href="#descripton" wact="<%=EncryptionUtil.encrypt("fidelite.carteFidelite.work_edit")%>" >
                             Description
                              </a>
                           </li>
                            <li>
                              <a data-toggle="tab" href="#client" wact="<%=EncryptionUtil.encrypt("fidelite.carteFideliteClient.work_find")%>">
                               Clients
                              </a>
                            </li>
                     </ul>
                </div>
          </div>
      </div>
    <%} %>  
		
		<div class="widget" >
			<div class="widget-body">
				<h3>Param&eacute;trage des points de f&eacute;dilit&eacute;</h3>
				<hr>
				<div class="row">
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Intitul&eacute;" />
						<div class="col-md-4">
							<std:text name="carteFidelite.libelle" type="string" maxlength="120" required="true"/>
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Seuil utilisation" />
						<div class="col-md-4">
							<std:text name="carteFidelite.mtt_seuil_util" type="decimal" style="width:120px;float: left;"/>
							<i class="fa fa-info-circle tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Solde points minimum avant utilisation"></i>
							<span style="font-size: 10px;">Dhs</span>
						</div>
						<std:label classStyle="control-label col-md-1" value="Chaque" />
						<div class="col-md-2">
							<std:text name="carteFidelite.mtt_palier" type="decimal" required="true" style="width:120px;float: left;"/>
							<i class="fa fa-info-circle tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Chaque montant dde cette valeur correspondera &agrave; un nombre de points suivant"></i>
							<span style="font-size: 10px;">Dhs</span>
						</div>
						<std:label classStyle="control-label col-md-1" value="&rArr;Donne" />
						<div class="col-md-2">
							<std:text name="carteFidelite.mtt_pf_palier" type="decimal" style="width:120px;float: left;" required="true"/>
							<i class="fa fa-info-circle tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Equivalent en points"></i>
							<span style="font-size: 10px;">Dhs</span>
						</div>
					</div>
					<hr>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Minimum achat" />
						<div class="col-md-4">
							<std:text name="carteFidelite.mtt_seuil" type="decimal" style="width:120px;float: left;"/>
							<i class="fa fa-info-circle tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Montant de la commande minimal pour attribuer les points"></i>
							<span style="font-size: 10px;">Dhs</span>
						</div>
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Planfond" />
						<div class="col-md-4">
							<std:text name="carteFidelite.mtt_plafond" type="decimal" style="width:120px;float: left;"/>
							<i class="fa fa-info-circle tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Maximum des points cumulable avant utilisation"></i>
							<span style="font-size: 10px;">Dhs</span>
						</div>	
						<std:label classStyle="control-label col-md-2" value="Utilisation par" />
						<div class="col-md-4">
							<std:text name="carteFidelite.mtt_bloc_util" type="decimal" style="width:120px;float: left;"/>
							<i class="fa fa-info-circle tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Les points sont utilisable par bloc de ... Ex: bloc de 10 points"></i>
							<span style="font-size: 10px;">Dhs</span>
						</div>	
					</div>
					<div class="form-group">
						<std:label classStyle="control-label col-md-2" value="Descriptions" />
						<div class="col-md-4">
							<std:textarea name="carteFidelite.description" rows="5" cols="61" maxlength="255" />
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="row" style="text-align: center;">
				<std:button actionGroup="M" classStyle="btn btn-success" action="fidelite.carteFidelite.work_merge" workId="${carteFidelite.id }" icon="fa-save" value="Sauvegarder" />
				<std:button actionGroup="D" classStyle="btn btn-danger" action="fidelite.carteFidelite.work_delete" workId="${carteFidelite.id }" icon="fa-trash-o" value="Supprimer" />
			</div>
		</div>
	</std:form>
</div>

<script type="text/javascript">
$("[data-toggle=tooltip]")
    .tooltip({
        html: true
    });
</script>  