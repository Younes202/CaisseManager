<%@page import="appli.controller.domaine.util_erp.ContextAppli.TYPE_CAISSE_ENUM"%>
<%@page import="appli.controller.domaine.caisse.bean.CaisseBean"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
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

<script type="text/javascript">
	$(document).ready(function() {
		<%if(ControllerUtil.isEditionWritePage(request)){%>
			loadInputFileEvents();
	<%} else{%>
		resetInputFileEvents();
		$("div[id^='sep_photo']").remove();
	<%}
		// Initialiser les photos ou documents
		IArticleService service = (IArticleService)ServiceUtil.getBusinessBean(IArticleService.class);
		Map<String, byte[]> dataimg = service.getDataImage(ContextAppli.getEtablissementBean().getId(), "paramTICK");
		for(String key : dataimg.keySet()){%>
			var ext = '<%=key.substring(key.lastIndexOf(".")+1)%>';
			var img = "";
	        if(ext.startsWith('pdf')){
	        	img = "resources/framework/img/filetype_pdf.png";
	        } else if(ext.startsWith('xls')){
	        	img = "resources/framework/img/xls_file.png";
	        } else if(ext.startsWith('txt')){
	        	img = "resources/framework/img/txt.png";
	        } else if(ext.startsWith('doc')){
	        	img = "resources/framework/img/document_microsoft_word_01.png";
	        } else{
	        	img = "data:image/jpeg;base64,<%=FileUtil.getByte64(dataimg.get(key))%>";
	        }
	        $("#photoX_div").css("background", "");
	        $("#photoX_div").html("<img src='"+img+"' width='120' height='120'/>");
			$("#photoX_name_span").html('<a href="front?w_f_act=<%=EncryptionUtil.encrypt("admin.parametrage.downloadPieceJointe")%>&pj=<%=EncryptionUtil.encrypt(ContextAppli.getEtablissementBean().getId().toString())%>&nm=<%=key%>" target="_blank"><%=key%></a> | <a href="front?w_f_act=<%=EncryptionUtil.encrypt("admin.parametrage.downloadPieceJointe")%>&pj=<%=EncryptionUtil.encrypt(ContextAppli.getEtablissementBean().getId().toString())%>&nm=<%=key%>&isdown=1" target="downloadframe"><i class="fa fa-cloud-download"></i></a>');
			$("#photoX_name").val('<%=key%>');
		<%}
		
		dataimg = service.getDataImage(ContextAppli.getEtablissementBean().getId(), "paramFE");
		for(String key : dataimg.keySet()){%>
			var ext = '<%=key.substring(key.lastIndexOf(".")+1)%>';
			var img = "";
	        if(ext.startsWith('pdf')){
	        	img = "resources/framework/img/filetype_pdf.png";
	        } else if(ext.startsWith('xls')){
	        	img = "resources/framework/img/xls_file.png";
	        } else if(ext.startsWith('txt')){
	        	img = "resources/framework/img/txt.png";
	        } else if(ext.startsWith('doc')){
	        	img = "resources/framework/img/document_microsoft_word_01.png";
	        } else{
	        	img = "data:image/jpeg;base64,<%=FileUtil.getByte64(dataimg.get(key)) %>";
	        }
	        $("#photoZ_div").css("background", "");
	        $("#photoZ_div").html("<img src='"+img+"' width='120' height='120'/>");
			$("#photoZ_name_span").html('');
			$("#photoZ_name").val('<%=key%>');
		<%} %>
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
                            <li>
                              <a data-toggle="tab" href="#journeeCaisse" wact="<%=EncryptionUtil.encrypt("caisse.caisse.find_mouvement")%>">
                               Mouvements caisses
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
        		<div class="row" style="margin-left: -5px;">
					<div class="form-title">OPTIONS GÉNÉRALES</div>
					<c:forEach items="${listParams }" var="parametre">
						<c:if test="${parametre.groupe == 'CAISSE'}">
							<div class="form-group">
								<std:label classStyle="control-label col-md-5" value="${parametre.libelle}" />
								<div class="col-md-7">
									<c:choose>
										<c:when test="${parametre.code == 'AROUNDI_PRIX_VENTE' }">
											<std:select name="param_${parametre.code}" type="string" style="width:50%;float:left;" data="${listTypeArrondi }" value="${parametre.valeur}" />
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
				<div class="row">
					<div class="form-title" style="margin-left: 10px;">DROITS</div>
					<c:forEach items="${listParams }" var="parametre">
						<c:if test="${parametre.groupe == 'CAISSE_CONF'}">
							<div class="form-group">
								<std:label classStyle="control-label col-md-5" value="${parametre.libelle}" />
								<div class="col-md-7">
									<c:choose>
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
				
		<div class="row">
			<std:label classStyle="control-label col-md-4" value="Zoom écran" />
			<div class="col-md-2">
				<std:text name="caisse.zoom" type="long" maxlength="3"/>
			</div>
		</div>
		<hr>
		<div class="row" style="margin-top: 25px;margin-left: 10px;">
			<div class="form-title">Image mise en veille caisse</div>
			<div class="col-md-12">
				<div class="col-md-3">
					<!-- Photos -->
					<div id="fileLoadDiv">
						<div class="col-sm-12">	
							<div id="photoZ_div" style="border: 1px solid #c6d983; border-radius: 25px; text-align: center; height: 120px; background: url('resources/framework/img/picture_file.png') no-repeat center;cursor: pointer;">
								<span style="font-size: 11px;">Fichier</span>
							</div>
						</div>
						<div class="col-sm-12" style="text-align: center;color: olive;">
							<span id="photoZ_name_span"></span>
							<input type="hidden" name="photoZ_name" id="photoZ_name">
						</div>
						<div class="col-sm-12">
							<!-- Separator -->
							<div id="sep_photoZ" style="margin-bottom: 5px; height: 20px; text-align: center;">
								<a href="javascript:"><b>X</b></a>
							</div>
							<!-- End -->
							<input type="file" name="photoZ" id="photoZ_div_file" path="paramFE" style="display: none;" accept="image/*">
						</div>
					</div>
				</div>
			</div>	
		</div>
		<hr>
     	 <div>
			<div class="row" style="text-align: center;" class="col-md-12" id="action-div">
					<std:button classStyle="btn btn-success" action="caisse.caisseConfiguration.work_update" icon="fa-save" value="Sauvegarder" />
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