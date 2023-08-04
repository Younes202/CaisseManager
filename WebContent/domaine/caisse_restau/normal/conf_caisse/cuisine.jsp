<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli.TYPE_CAISSE_ENUM"%>
<%@page import="appli.controller.domaine.caisse.bean.CaisseBean"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="java.util.Map"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="appli.model.domaine.stock.service.IArticleService"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<%
CaisseBean caisseB = (CaisseBean)request.getAttribute("caisse");
%>

<script type="text/javascript">
	$(document).ready(function() {
		<%if(ControllerUtil.isEditionWritePage(request)){%>
			loadInputFileEvents();
	<%} else{%>
		resetInputFileEvents();
		$("div[id^='sep_photo']").remove();
	<%}%>
		$("#caisse\\.is_auto_cmd").change(function(){
			manageAutoConf($(this).prop("checked"));
		});
		manageAutoConf(<%=(caisseB!=null?""+BooleanUtil.isTrue(caisseB.getIs_auto_cmd()):"false") %>);
	});
	function manageAutoConf(state){
		$("#row_conf").css("display", (state?'':'none'));
	}
</script>

<!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Gestion de cuisine</li>
         <li class="active">Configuration</li>
     </ul>
 </div>

<div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">      
        <std:link actionGroup="U" classStyle="btn btn-default" workId="${caisseId }" action="caisse_restau.caisseConfigurationRestau.work_init_update" icon="fa-3x fa-pencil" tooltip="Modifier" />
		<std:link classStyle="btn btn-default" action="caisse.caisse.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
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
                              <a data-toggle="tab" href="#descripton" wact="<%=EncryptionUtil.encrypt("caisse.caisse.work_edit")%>" >
                               Fiche
                              </a>
                           </li>
                           <% 
                           for(TYPE_CAISSE_ENUM typeCaisse : TYPE_CAISSE_ENUM.values()){
                           		if(typeCaisse.toString().equals(ControllerUtil.getMenuAttribute("typeCaisse", request))){%>
                           			<li class="active">
                                    <a data-toggle="tab" href="#configCaisse" wact="<%=EncryptionUtil.encrypt("caisse_restau.caisseConfigurationRestau.work_edit")%>">
                                     	Configuration <%=typeCaisse.getLibelle() %>
                                    </a>
                                  </li>
                           		<%
                           			break;
                           		} 
                           	}%>
                     </ul>
                </div>
          </div>
      </div>
         
         <div class="widget-body">
        	<div class="row" style="margin-left: 7px;margin-right: 6px;">
				<div class="row">
					<div class="form-title">DIVERS</div>
					<c:forEach items="${listParams }" var="parametre">
						<c:if test="${parametre.groupe == 'CAISSE_CUISINE'}">
							<div class="form-group">
								<std:label classStyle="control-label col-md-5" value="${parametre.libelle}" />
								<div class="col-md-7">
									<c:choose>
										<c:when test="${parametre.code=='MODE_TRAVAIL_CUISINE'}">
											<std:select name="param_${parametre.code}" type="string" data="${listModeTravail}" width="70%" value="${parametre.valeur}" />
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
											<std:checkbox name="param_${parametre.code}" checked='${(parametre.valeur!="" && parametre.valeur!=null)?"1":"0"}' />
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
				
<!-- 		<div class="row" style="margin-bottom: 10px;"> -->
<%-- 			<std:label classStyle="control-label col-md-4" value="Type de gestion" /> --%>
<!-- 			<div class="col-md-2"> -->
<%-- 				<std:select name="caisse.mode_travail" type="string" multiple="true" width="100%" data="${listModeTravail}" /> --%>
<!-- 			</div> -->
<!-- 		</div> -->
<!-- 		<div class="row"> -->
<%-- 			<std:label classStyle="control-label col-md-4" value="Nombre de colonnes" /> --%>
<!-- 			<div class="col-md-2"> -->
<%-- 				<std:text name="caisse.nbr_colonne" type="long" maxlength="1"/> --%>
<!-- 			</div> -->
<%-- 			<std:label classStyle="control-label col-md-4" value="Zoom écran" /> --%>
<!-- 			<div class="col-md-2"> -->
<%-- 				<std:text name="caisse.zoom" type="long" maxlength="3"/> --%>
<!-- 			</div> -->
<!-- 		</div> -->

		<hr>
		<div class="form-title">PILOTAGE</div>
			<c:forEach items="${listParams }" var="parametre">
				<c:if test="${parametre.groupe == 'CAISSE_CUISINE_PIL'}">
					<div class="form-group">
						<std:label classStyle="control-label col-md-5" value="${parametre.libelle}" />
						<div class="col-md-7">
							<c:choose>
								<c:when test="${parametre.code=='MODE_TRAVAIL_CUISINE'}">
									<std:select name="param_${parametre.code}" type="string" data="${listModeTravail}" width="70%" value="${parametre.valeur}" />
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
									<std:checkbox name="param_${parametre.code}" checked='${(parametre.valeur!="" && parametre.valeur!=null)?"1":"0"}' />
								</c:when>
							</c:choose>
							<c:if test="${parametre.help != null && parametre.help != ''}">
								<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="${parametre.help}" src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
							</c:if>
						</div>
					</div>
				</c:if>
			</c:forEach>
		
		
		<hr>
		<div class="row">
			<std:label classStyle="control-label col-md-4" value="Gestion automatique" />
			<div class="col-md-2">
				<std:checkbox name="caisse.is_auto_cmd" />
				<span class="glyphicon glyphicon-info-sign" style="cursor: help;font-size: 19px;" title="Cocher pour sp&eacute;cifier les &eacute;l&eacute;ments &agrave; afficher dans cet &eacute;cran"></span>
			</div>
		</div>
		<div class="row" id="row_conf" style="display: none;">
			<div class="col-lg-6 col-sm-6 col-xs-12">
				<div class="widget">
					<div class="widget-header bg-palegreen">
					  <i class="widget-icon fa fa-arrow-down"></i>
					  <span class="widget-caption">Menus associ&eacute;s</span>
					 </div>
					<div class="widget-body">
						<std:select name="caisse.menus_array" type="string[]" multiple="true" width="100%" data="${listMenus }" key="id" labels="libelle" />
					</div>
				</div>	
        	</div>
        	<div class="col-lg-6 col-sm-6 col-xs-12">
				<div class="widget">
					<div class="widget-header bg-orange">
					  <i class="widget-icon fa fa-arrow-down"></i>
					  <span class="widget-caption">Familles associ&eacute;es </span>
					 </div>
					<div class="widget-body">
						<std:select name="caisse.familles_array" isTree="true" type="string[]" multiple="true" width="100%" data="${listFamilles }" key="id" labels="libelle" />
         			</div>
        		</div>
         	</div>
         	<div class="col-lg-6 col-sm-6 col-xs-12">
				<div class="widget">
					<div class="widget-header bg-blue">
					  <i class="widget-icon fa fa-arrow-down"></i>
					  <span class="widget-caption">Articles associ&eacute;s </span>
					 </div>
					<div class="widget-body">
						<std:select name="caisse.articles_array" type="string[]" multiple="true" width="100%" data="${listArticles }" key="id" labels="libelle" />
         			</div>
        		</div>
         	</div>
         </div>
         <hr>
       	<div class="row">
			<div class="form-title">IMPRIMANTE ÉTIQUETTE</div>
			<c:forEach items="${listParams }" var="parametre">
				<c:if test="${parametre.groupe == 'CAISSE_CUISINE_ETQ'}">
					<div class="form-group">
						<std:label classStyle="control-label col-md-5" value="${parametre.libelle}" />
						<div class="col-md-7">
							<c:choose>
								<c:when test="${parametre.code=='ETIQUETTE_PRINT'}">
									<std:select name="param_${parametre.code}" type="string" data="${list_imprimante}" width="70%" value="${parametre.valeur}" />
								</c:when>
								<c:when test="${parametre.code=='ETIQUETTE_ORIENTATION'}">
									<std:select name="param_${parametre.code}" type="string" data="${orientationBalance}" width="70%" value="${parametre.valeur}" />
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
									<std:checkbox name="param_${parametre.code}" checked='${(parametre.valeur!="" && parametre.valeur!=null)?"1":"0"}' />
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
				     
		<div class="row" style="text-align: center;" class="col-md-12" id="action-div">
			<std:button classStyle="btn btn-success" action="caisse_restau.caisseConfigurationRestau.work_update" icon="fa-save" value="Sauvegarder" />
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