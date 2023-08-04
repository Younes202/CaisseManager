<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.beanContext.AbonnementBean"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fn" prefix="fn"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
   .form-title{ 
    	margin-left: 12px;
	}
</style>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Param&eacute;trage</li>
		<li class="active">Divers</li>
	</ul>
</div>

<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:link actionGroup="C" classStyle="btn btn-default" action="admin.parametrage.work_init_update" icon="fa-3x fa-pencil" tooltip="Modifier" />
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
		                          <li>
		                              <a data-toggle="tab" href="#data" wact="<%=EncryptionUtil.encrypt("admin.parametrage.work_edit")%>" params="tp=def">
		                               G&eacute;n&eacute;rale
		                              </a>
		                           </li>
									<li>
		                              <a data-toggle="tab" href="#data" wact="<%=EncryptionUtil.encrypt("admin.parametrage.work_edit")%>" params="tp=cai">
		                               Caisse
		                              </a>
		                           </li>		                           
		                            <li>
		                              <a data-toggle="tab" href="#data" wact="<%=EncryptionUtil.encrypt("admin.token.work_find")%>">
		                               Tokens
		                              </a>
		                            </li>
		                            <li>
		                              <a data-toggle="tab" href="#data" wact="<%=EncryptionUtil.encrypt("admin.parametrage.work_edit")%>" params="tp=ihm">
		                               Interface graphique
		                              </a>
		                            </li>
		                            <li class="active">
		                              <a data-toggle="tab" href="#data" wact="<%=EncryptionUtil.encrypt("admin.parametrage.work_edit")%>" params="tp=div">
		                               Divers
		                              </a>
		                            </li>
		                            <li>
					                   <a data-toggle="tab" href="#data" wact="<%=EncryptionUtil.encrypt("admin.parametrage.work_edit")%>" params="tp=rst">
					                    Etablissement
					                   </a>
					                 </li>
					                 <li>
                  					   <a data-toggle="tab" href="#data" wact="<%=EncryptionUtil.encrypt("admin.parametrage.work_edit")%>" params="tp=aff">
                                        Afficheur
                                       </a>
                                    </li> 
					            	<li>
					                   <a data-toggle="tab" href="#data" wact="<%=EncryptionUtil.encrypt("admin.parametrage.work_edit")%>" params="tp=mnt">
					                    Maintenance
					                   </a>
					                 </li>
		                     </ul>
		                </div>
		          </div>
		      </div>
		
		<% AbonnementBean abnBean = ContextAppli.getAbonementBean();%> 
		<c:set var="isOptCompta" value="<%=abnBean.isOptCompta() %>" />	
		<c:set var="isOptStock" value="<%=abnBean.isOptStock() %>" />
		<c:set var="isOptCommercial" value="<%=abnBean.isOptCommercial() %>" />
		<c:set var="isOptOptimisation" value="<%=abnBean.isOptPlusOptimisation() %>" />		
									
			<div class="widget-body">
				<div class="row">
					<c:if test="${isOptCompta}">
						<div class="form-title">COMPTES BANCAIRES</div>
						<c:forEach items="${listParams }" var="parametre">
							<c:if test="${parametre.groupe == 'DIVERS'}">
								<c:if test="${fn:startsWith(parametre.code ,'COMPTE_')}">
									<div class="form-group">
										<std:label classStyle="control-label col-md-5" value="${parametre.libelle}" />
										<div class="col-md-7">
											<std:select name="param_${parametre.code}" type="long" style="width:50%;float:left;" key="id" labels="libelle" data="${listeBanque }" value="${parametre.valeur}" />
										</div>
									</div>
								</c:if>
							</c:if>	
						</c:forEach>
					
						<br>
					</c:if>	
				
				<div class="form-title">PARAMETRAGES DIVERS</div>
				<c:forEach items="${listParams }" var="parametre">
					<c:if test="${parametre.groupe == 'DIVERS' and not ((parametre.abonnement eq 'OPT_STOCK' and not isOptStock) or (parametre.abonnement eq 'OPT_COMMERCIAL' and not isOptCommercial))}">
						<c:if test="${!fn:startsWith(parametre.code ,'COMPTE_') and parametre.code != 'TAUX_OPTIM' and parametre.code != 'SEUIL_OPTIM'}">
							<div class="form-group">
								<std:label classStyle="control-label col-md-5" value="${parametre.libelle}" />
								<div class="col-md-7">
									<c:choose>
										<c:when test="${parametre.code == 'PRINT_RAZ' }">
											<std:select name="param_${parametre.code}" type="string" data="${list_imprimante}" width="70%" value="${parametre.valeur}" />
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
										<c:when test="${parametre.type=='BOOLEAN'}">	
											<std:checkbox name="param_${parametre.code}" checked='${parametre.valeur!=null?"1":"0"}' />
										</c:when>
									</c:choose>
									<c:if test="${parametre.help != null && parametre.help != ''}">
										<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="${parametre.help}" src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
									</c:if>
								</div>
							</div>
						</c:if>
					</c:if>
				</c:forEach>
				
				<c:if test="${isOptOptimisation }">
					<div class="form-title" style="color: red;">OPTIMISATION</div>
						<c:forEach items="${listParams }" var="parametre">
							<c:if test="${parametre.groupe == 'DIVERS' }">
								<c:if test="${parametre.code == 'TAUX_OPTIM' or parametre.code == 'SEUIL_OPTIM'}}">
									<div class="form-group">
										<std:label classStyle="control-label col-md-5" value="${parametre.libelle}" />
										<div class="col-md-7">
											<c:choose>
												<c:when test="${parametre.code == 'PRINT_RAZ' }">
													<std:select name="param_${parametre.code}" type="string" data="${list_imprimante}" width="70%" value="${parametre.valeur}" />
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
												<c:when test="${parametre.type=='BOOLEAN'}">	
													<std:checkbox name="param_${parametre.code}" checked='${parametre.valeur!=null?"1":"0"}' />
												</c:when>
											</c:choose>
											<c:if test="${parametre.help != null && parametre.help != ''}">
												<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="${parametre.help}" src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
											</c:if>
										</div>
									</div>
								</c:if>
							</c:if>
						</c:forEach>	
				</c:if>
			</div>
		</div>	
		<hr>
		<div class="form-actions">
			<div class="row" style="text-align: center;" class="col-md-12" id="action-div">
				<std:button actionGroup="M" classStyle="btn btn-success" action="admin.parametrage.work_update" params="tp=div" icon="fa-save" value="Sauvegarder" />
			</div>
		</div>
		</std:form>
	</div>
</div>

<script type="text/javascript">
/*Handles ToolTips*/
$("[data-toggle=tooltip]")
    .tooltip({
        html: true
    });
</script>    