<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Gestion de caisse</li>
         <li class="active">Param&egrave;trage commandes caisses</li>
     </ul>
 </div>

<div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">      
		<std:link actionGroup="C" classStyle="btn btn-default" action="admin.parametrage.work_init_update" icon="fa-3x fa-pencil" tooltip="Modifier" />
		<std:linkPopup actionGroup="X" classStyle="btn btn-info" action="caisse-web.cuisinePilotage.init_print_fiche" icon="fa-3x fa-cogs" tooltip="Configurer l'imprimante d'&eacute;tiquettes" value="Configurer l'imprimante" />
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
		
		<%if(ControllerUtil.getMenuAttribute("isCuis", request) == null){ %>
			<div class="row">
		        <div class="col-lg-12 col-sm-12 col-xs-12">
		              <div class="tabbable">
		                    <ul class="nav nav-tabs" id="myTab">
		                          <%request.setAttribute("mnu_param", "cuis"); %> 
			 					  <jsp:include page="parametrage_headers.jsp" />
		                     </ul>
		                </div>
		          </div>
		      </div>
		  <%} %>
		      
         <div class="widget-body">
         		<div class="row" style="margin-left: 0px;">
					<c:set var="oldSubGroupe" value="" />
					<c:forEach items="${listParams }" var="parametre">
						<c:if test="${parametre.groupe == 'CAISSE_CUISINE_PIL'}">
							<c:if test="${oldSubGroupe != parametre.groupe_sub or empty oldSubGroupe}">
								<h3 style="color: #2dc3e8;padding-left: 20px;border-bottom: 1px dashed rgba(0, 0, 0, .2);">${parametre.groupe_sub }</h3>
							</c:if>
							<c:set var="oldSubGroupe" value="${parametre.groupe_sub }" />
							<div class="form-group">
								<std:label classStyle="control-label col-md-5" value="${parametre.libelle}" />
								<div class="col-md-7">
									<c:choose>
										<c:when test="${parametre.code=='ECRAN_CMD_VALIDE'}">
											<std:select type="string" name="param_${parametre.code}" required="true" data="${dataSourceStatut }" value="${parametre.valeur }" />
										</c:when>
										<c:when test="${parametre.code=='ECRAN_CMD_ENPREPARATION'}">
											<std:select type="string" name="param_${parametre.code}" required="true" data="${dataSourceStatut }" value="${parametre.valeur }" />
										</c:when>
										<c:when test="${parametre.code=='ECRAN_STRATEGIE'}">
											<std:select type="string" name="param_${parametre.code}" required="true" data="${dataStrategieEcran }" value="${parametre.valeur }" />
										</c:when>
										<c:when test="${parametre.code=='MODE_TRAVAIL_CUISINE'}">
											<std:select type="string" name="param_${parametre.code}" required="true" data="${listModeTravail }" value="${parametre.valeur }" />
										</c:when>
										<c:when test="${parametre.code=='ECRAN_STATUT'}">
											<std:select type="string[]" name="param_${parametre.code}" data="${dataStatut }" value="${paramStatutArray }" multiple="true" />
										</c:when>
										
										<c:when test="${parametre.type=='STRING'}">
											<std:text name="param_${parametre.code}" type="string" style="width:50%;float:left;" maxlength="120" value="${parametre.valeur}" />
										</c:when>
										<c:when test="${parametre.type=='TEXT'}">
											<std:textarea name="param_${parametre.code}" style="width:50%;float:left;" rows="3" value="${parametre.valeur}" />
										</c:when>
										<c:when test="${parametre.type=='NUMERIC'}">
											<std:text name="param_${parametre.code}" type="long" style="width:120px;float:left;" maxlength="120" value="${parametre.valeur}" />
										</c:when>
										<c:when test="${parametre.type=='DECIMAL'}">
											<std:text name="param_${parametre.code}" type="decimal" style="width:120px;float:left;" maxlength="120" value="${parametre.valeur}" />
										</c:when>
										<c:when test="${parametre.type=='BOOLEAN'}">	
											<std:checkbox name="param_${parametre.code}" checked='${parametre.valeur }' />
										</c:when>
									</c:choose>
									<c:if test="${parametre.help != null && parametre.help != ''}">
										<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="${parametre.help}" src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
									</c:if>
								</div>
							</div>
						</c:if>
					</c:forEach>
				</div>
         		<div class="row" style="margin-left: 0px;">
					<hr>
					<div class="form-actions">
						<div class="row" style="text-align: center;" class="col-md-12" id="action-div">
							<std:button actionGroup="M" classStyle="btn btn-success" action="admin.parametrage.work_update" params="tp=cuis" icon="fa-save" value="Sauvegarder" />
						</div>
					</div>
				</div>
			</div>		
		</std:form>
	  </div>
</div>

<script type="text/javascript">
$("[data-toggle=tooltip]")
    .tooltip({
        html: true
    });
</script> 