<%@page import="appli.controller.domaine.util_erp.ContextAppli.TYPE_CAISSE_ENUM"%>
<%@page import="appli.controller.domaine.caisse.bean.CaisseBean"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="framework.model.beanContext.EtablissementPersistant"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="appli.model.domaine.administration.persistant.ParametragePersistant"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="java.util.List"%>
<%@page import="appli.model.domaine.stock.service.IArticleService"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<script src="resources/framework/js/util_file_upload.js?v=1.1"></script>

<style>
	#copie-cal {
	    vertical-align: sub;
    	margin-left: 2px;
	}
</style>

<script type="text/javascript">
	var idxFile = 1;
	$(document).ready(function() {
		$("#addFile").click(function(){
			var content = $("#fileLoadDiv").clone().html();
			content = content.replace(/photoX/g,"photo"+idxFile);
			$("#row_file").append(content);
			idxFile++;
		});
		
		
		<%if(ControllerUtil.isEditionWritePage(request)){%>
		loadInputFileEvents();
		<%} else{%>
		$("div[id^='sep_photo']").remove();
		<%}
		// Initialiser les photos ou documents
	
	EtablissementPersistant restauBean = ContextAppli.getEtablissementBean();
	if(restauBean != null && restauBean.getId() != null){
		IArticleService service = (IArticleService)ServiceUtil.getBusinessBean(IArticleService.class);
		Map<String, byte[]> dataimg = service.getDataImage(restauBean.getId(), "param");
		for(String key : dataimg.keySet()){%>
			var content = $("#fileLoadDiv").clone().html();
			content = content.replace(/photoX/g,"photo"+idxFile);
			$("#row_file").append(content);
			var ext = '<%=key.substring(key.lastIndexOf(".")+1)%>';
			var img = "data:image/jpeg;base64,<%=FileUtil.getByte64(dataimg.get(key)) %>";
	        $("#photo"+idxFile+"_div").css("background", "");
	        $("#photo"+idxFile+"_div").html("<img src='"+img+"' width='120' height='120'/>");
			$("#photo"+idxFile+"_name_span").html('<a href="front?w_f_act=<%=EncryptionUtil.encrypt("admin.parametrage.downloadPieceJointe")%>&pj=<%=EncryptionUtil.encrypt(restauBean.getId().toString())%>&nm=<%=key%>" target="_blank"><%=key%></a> | <a href="front?w_f_act=<%=EncryptionUtil.encrypt("stock.mouvement.downloadPieceJointe")%>&pj=<%=EncryptionUtil.encrypt(restauBean.getId().toString())%>&nm=<%=key%>&isdown=1" target="downloadframe"><i class="fa fa-cloud-download"></i></a>');
			$("#photo"+idxFile+"_name").val('<%=key%>');
			idxFile++;
	<%}
	} %>

	});
</script>	
<!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Gestion de caisse</li>
         <li class="active">Configuration</li>
     </ul>
 </div>

<div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">      
        <std:link actionGroup="C" classStyle="btn btn-default" action="caisse.caisseConfiguration.work_init_update" workId="${caisseId }" icon="fa-3x fa-pencil" tooltip="Modifier" />
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

	<std:form name="data-form">	
	<!-- widget grid -->
	<div class="widget">
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
                                    <a data-toggle="tab" href="#configCaisse" wact="<%=EncryptionUtil.encrypt("caisse.caisseConfiguration.work_edit")%>">
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
	<div class="row">
			<div class="col-md-6">
				<fieldset>
					<hr>
					<h4>Ajouter Image</h4>
					<hr>
				<!-- Pieces -->
							<div class="widget">
								<div class="widget-header bordered-bottom bordered-blue">
									<span class="widget-caption">
										<a href="javascript:void(0);" id="addFile" targetBtn="C" class="btn btn-default" title="Ajouter pi&egrave;ce jointe" style="margin-top: -2px;">
											<i class="fa fa-3x fa-plus"> </i> Ajouter pi&egrave;ces jointes
										</a>
									</span>
								</div>
								 <div class="widget-body">
										<!-- Photos -->
										<div class="row" id="row_file">
											<div id="fileLoadDiv" style="display: none;">
												<div class="col-md-4">
													<div class="col-sm-12">	
														<div id="photoX_div" style="border: 1px solid #c6d983; border-radius: 25px; text-align: center; height: 150px; width: 160px; background: url('resources/framework/img/picture_file.png') no-repeat center;cursor: pointer;">
															<span style="font-size: 11px;">Fichier</span>
														</div>
													</div>
													<div class="col-sm-12" style="text-align: center;color: olive;">
														<span id="photoX_name_span"></span>
														<input type="hidden" name="photoX_name" id="photoX_name">
													</div>
													<div class="col-sm-12">
														<!-- Separator -->
														<div id="sep_photoX" style="margin-bottom: 5px; height: 20px; text-align: center;">
															<a href="javascript:"><b>X</b></a>
														</div>
														<!-- End -->
														<input type="file" name="photoX" id="photoX_div_file" style="display: none;" accept="image/*,.doc,.pdf,.xls,.xlsx,.doc,.docx,.txt">
													</div>
												</div>
											</div>
									</div>
								</div>		
							</div>	
					</fieldset>
				</div>
		
				<div class="col-md-6">
					<fieldset>
						<hr>
						<h4>Param&eacute;trage Image</h4>
						<hr>
					<div class="row">
					<c:forEach items="${listParams }" var="parametre">
						<c:if test="${parametre.groupe == 'SLIDE'}">
							<div class="form-group">
								<std:label classStyle="control-label col-md-5" value="${parametre.libelle}" />
								<div class="col-md-7">
									<c:choose>
										<c:when test="${parametre.code=='SLIDE_EFFET'}">
	
<%
	String currVal = "";
	List<ParametragePersistant> paramsAll = (List<ParametragePersistant>)request.getAttribute("listParams");
	for(ParametragePersistant param : paramsAll){
		if(param.getCode().equals("SLIDE_EFFET")){
			currVal = param.getValeur();
		}
	}
	
	boolean isWriteForm = ControllerUtil.isEditionWritePage(request);
	Map<String, String> mapDataEffect = (Map<String, String>)request.getAttribute("mapDataEffect");
	
	if(!isWriteForm){
	%>
		<input type="hidden" name="param_${parametre.code}" value="${parametre.valeur} ">
	<%} %>	
	
									<script type="text/javascript">
									$(document).ready(function() {
										$("#ssTransition").val("<%=currVal %>");
										<%if(!isWriteForm){%>
											$("#ssTransition").css("background-color", "#eeeeee");
										<%}%>
									});
									</script>
											
											<select name="param_${parametre.code}" id="ssTransition" style="width: 90%;" <%=!isWriteForm?" disabled='disabled'":"" %>">
												<%
												if(mapDataEffect != null){
													for(String effect : mapDataEffect.keySet()){ %>
														<option value="<%=effect %>"><%=effect %></option>
												<%	}
												} %>
											</select>
										</c:when>
										<c:when test="${parametre.type=='STRING'}">
											<std:text name="param_${parametre.code}" type="string" style="width:50%;float:left;" maxlength="120" value="${parametre.valeur}" />
										</c:when>
										<c:when test="${parametre.type=='NUMERIC'}">
											<std:text name="param_${parametre.code}" type="long" style="width:120px;float:left;" maxlength="120" value="${parametre.valeur}" />
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
					</fieldset>
				</div>
			
				<hr>
				<div class="row" style="text-align: center;">
					<div class="col-md-12">
						<std:button actionGroup="M" classStyle="btn btn-success" action="caisse.caisseConfiguration.work_update" icon="fa-save" value="Sauvegarder" />
					</div>
				</div>
			</div>
		</div>
	</div>
  </std:form>
</div>

<script type="text/javascript"> 
/*Handles ToolTips*/
$("[data-toggle=tooltip]")
    .tooltip({
        html: true
    });
</script>  