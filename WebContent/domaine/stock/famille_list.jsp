<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.constante.ProjectConstante"%>
<%@page import="framework.model.common.service.MessageService"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="java.util.List"%>
<%@page import="appli.model.domaine.stock.persistant.FamillePersistant"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<link href="resources/framework/css/tree/jquery.treeview.css?v=1.0" rel="stylesheet" type="text/css" />
<script src="resources/framework/js/tree/jquery.treeview.js"></script>

<style>
.filetree span.folder { background: url(resources/framework/img/folder_close_blue.png) 0 0 no-repeat !important; }
</style>

<%
String tp = (String) ControllerUtil.getMenuAttribute("tp", request);
String titre = "";
switch (tp) {
	case "ST": {titre = " de stock";}; break;
	case "FO": {titre = " de fournisseurs";}; break;
	case "CU": {titre = " de cuisine";}; break;
	case "CO": {titre = " de consommations";}; break;
}
%>

<script type="text/javascript">
$(document).ready(function (){
	getTabElement("#treeattack-ul").treeview({
		collapsed: true,
		animated: "fast",
		persist: "cookie",
		//unique: true,
		control: "#div-control",
		cookieId: "treefamille"
	});
	
	$(document).on('click', ".filetree a", function(){
		getTabElement(".filetree a").css("background-color", "transparent");
		$(this).css("background-color", "#CDDC39");
		getTabElement(".page-header a, #order_lnk").attr("params", "fam="+$(this).attr("fam")+"&workId="+$(this).attr("fam"));
		getTabElement("#del-tree-link, #upd-tree-link").removeAttr("disabled").css("display", "");
		//
		if($(this).attr("stat") == '1'){
			getTabElement("#act-tree-link").attr("tp", "dis").removeAttr("disabled").css("display", "");
		} else{
			getTabElement("#dis-tree-link").attr("tp", "dis").removeAttr("disabled").css("display", "");
		}
	});
	
	getTabElement(".page-header a").click(function(){
			if($(this).attr("tp") == 'dis'){
				return;
			}
	   		if($(this).attr("targetBtn") == 'C'){
	   			getTabElement("#trigger_create").attr("params", $(this).attr("params"));
	   			getTabElement("#trigger_create").trigger("click");
	   		} else if($(this).attr("targetBtn") == 'U'){
	   			getTabElement("#trigger_update").attr("params", $(this).attr("params"));
	   			getTabElement("#trigger_update").trigger("click");
	   		}
	});
	
	getTabElement("#actifs-activator").change(function(){
		getTabElement("#CU_block").trigger("click");
	});
});
</script>
<std:form name="data-form">
  <%if(ControllerUtil.getUserAttribute("IS_INITIALIZE_MODE", request) != null){ %>
<div class="widget">
			<div class="col-lg-12 col-sm-12 col-xs-12">
					<%
				    request.setAttribute("tabName", "famille");
					request.setAttribute("pAction", EncryptionUtil.encrypt("stock.emplacement.work_find"));
					%>
					<%if(ControllerUtil.getUserAttribute("IS_INITIALIZE_MODE", request) != null){ %>
					<jsp:include page="/domaine/administration/wizard/wizard.jsp"  />
					<%} %>
					<div class="step-content" id="simplewizardinwidget-steps">
					
					</div>
				</div>
				</div>
		</div>		
	<%} %>


<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion de stocks</li>
		<li>Famille</li>
		<li class="active">Edition</li>
	</ul>
</div>



<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<c:if test="${empty isEditable or isEditable }">
			<std:link actionGroup="C" style="float:left;" classStyle="btn btn-default" icon="fa-3x fa-plus" forceShow="true" />
		</c:if>
		<div style="margin-left: 100px;margin-top: 6px;float: left;">
			<span>Actifs</span>          
	   		<label style="margin-top: 3px;" title="Uniquements actifs">
	            <input class="checkbox-slider toggle colored-blue" type="checkbox" ${isCheked ? ' checked="checked"':'' } name="actifs-activator" id="actifs-activator" style="display: none;">
	            <span class="text"></span>
	        </label>
        </div>
        
		<std:link id="upd-tree-link" actionGroup="U" classStyle="btn btn-default" style="display: none;" icon="fa-3x fa-eye" forceShow="true" />
		<c:if test="${empty isEditable or isEditable }">
			<std:link id="act-tree-link" actionGroup="U" action="stock.famille.desactiver" style="display: none;" icon="fa fa-3x fa-unlock" classStyle="btn btn-success" tooltip="Activer':'D&eacute;sactiver" forceShow="true"/>
			<std:link id="dis-tree-link" actionGroup="U" action="stock.famille.desactiver" style="display: none;" icon="fa fa-3x fa-lock" classStyle="btn btn-warning" tooltip="D&eacute;sactiver" forceShow="true"/>
			<std:link id="del-tree-link" actionGroup="D" classStyle="btn btn-danger" style="display: none;" action="stock.famille.work_delete" icon="fa-3x fa-trash" forceShow="true" />
		</c:if>
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

	<a id="trigger_create" data-toggle="modal" data-backdrop="static" data-keyboard="false" data-target="#generic_modal" targetdiv="generic_modal_body" href="javascript:" wact="<%=EncryptionUtil.encrypt("stock.famille.work_init_create") %>" style="display: none;"></a>
	<a id="trigger_update" data-toggle="modal" data-backdrop="static" data-keyboard="false" data-target="#generic_modal" targetdiv="generic_modal_body" href="javascript:" wact="<%=EncryptionUtil.encrypt("stock.famille.work_edit") %>" style="display: none;"></a>

	<!-- widget grid -->
	<div class="widget">
		
		<div class="row">
			<div class="col-lg-12 col-sm-12 col-xs-12">
				<div class="widget-main ">
					<div class="tabbable">
						<ul class="nav nav-tabs" id="myTab">
					<%if("stock-famille".equals(MessageService.getGlobalMap().get(ProjectConstante.LEFT_MENU_ID))){ %>	
						<%if(!"CU".equals(tp)){ %>	
							<li <%=tp.equals("ST") ? " class='active'":""  %>><a data-toggle="tab" href="#descripton" wact="<%=EncryptionUtil.encrypt("stock.famille.work_find")%>" params="tp=ST"> Stock </a></li>
						<%} else{ %>
							<li <%=tp.equals("CU") ? " class='active'":""  %>><a data-toggle="tab" href="#mouvementCaisse" wact="<%=EncryptionUtil.encrypt("stock.famille.work_find")%>" params="tp=CU" id="CU_block"> Cuisine </a></li>
						<%} %>
						<%if(!"CU".equals(tp)){ %>	
							<li <%=tp.equals("FO") ? " class='active'":""  %>><a data-toggle="tab" href="#repartitionVente" wact="<%=EncryptionUtil.encrypt("stock.famille.work_find")%>" params="tp=FO"> Fournisseur </a></li>
							<li <%=tp.equals("CO") ? " class='active'":""  %>><a data-toggle="tab" href="#repartitionVente" wact="<%=EncryptionUtil.encrypt("stock.famille.work_find")%>" params="tp=CO"> Consommation/Dépense </a></li>
						<%} %>
					<%} else if(ContextAppli.IS_SYNDIC_ENV()){ %>
						<li><a data-toggle="tab" href="#data" wact="<%=EncryptionUtil.encrypt("admin.etablissement.work_edit")%>" workId="${societe.id }"> Fiche établissement </a></li>
						<li class="active"><a data-toggle="tab" href="#data" wact="<%=EncryptionUtil.encrypt("stock.famille.work_find")%>" params="tp=CM"> Composition </a></li>
					<%} %>
							</ul>					
						<div class="tab-content">
                      <div class="row">
								<div class="col-lg-6 col-sm-6 col-xs-12">
									<div class="widget">
									<div class="widget-header bg-palegreen">
					                        <i class="widget-icon fa fa-arrow-down"></i>
					                        <span class="widget-caption">Familles <%=titre %></span>
				                    </div>
									<div class="widget-body">
										<div id="div-control" style="float: left;">
											<a title="Collapse the entire tree below" href="#"><img src="resources/framework/img/tree/minus.gif" /> Fermer</a> |
											<a title="Expand the entire tree below" href="#"><img src="resources/framework/img/tree/plus.gif" /> Ouvrir</a> |
										</div>
										<c:if test="${empty isEditable or isEditable }">
											<std:linkPopup id="order_lnk" actionGroup="C" classStyle="" action="stock.famille.initOrderMenu" tooltip="Ordonner la racine" icon="fa fa-sort-amount-desc" value="Ordonner"/>
										</c:if>
										<hr>
									<%
									boolean isShowActifOnly = StringUtil.isTrue(""+request.getAttribute("actifOnly"));
									List<FamillePersistant> listFamille = (List<FamillePersistant>) request.getAttribute("listFamilles");
									if(listFamille !=  null){%>
															<ul id="treeattack-ul" class="filetree">
																 <%
											Integer oldLevel = null;
											String path = (String) ControllerUtil.getMenuAttribute("tp", request);
																 
											for(FamillePersistant famillePersistant : listFamille){
												if(famillePersistant.getLevel() == 0){
													continue;
												}
												
												boolean isNode = (famillePersistant.getB_right()- famillePersistant.getB_left() > 1);
												String wid = EncryptionUtil.encrypt(""+famillePersistant.getId());
												
												if(oldLevel != null){
													if(oldLevel > famillePersistant.getLevel()){
														while(oldLevel > famillePersistant.getLevel()){	%>
																</li> 
															</ul>
															<%
															oldLevel--;
														}
													} else if(oldLevel == famillePersistant.getLevel()){%>
															</li>
															<%	}
												}
												%>
															<li style="<%=isShowActifOnly && BooleanUtil.isTrue(famillePersistant.getIs_disable()) ? "display:none;":"" %>"> 
															<span class="<%=isNode ? "folder" :"file"%>" style="border-bottom: 1px dashed #FFEB3B;"> 
																<a href="javascript:" style="<%=BooleanUtil.isTrue(famillePersistant.getIs_disable())?"text-decoration: line-through;":""%><%=isNode ? "color:black;" :""%>" fam="<%=wid%>" stat="<%=BooleanUtil.isTrue(famillePersistant.getIs_disable())?"1":"0"%>"> <%=famillePersistant.getCode() %> - <%=famillePersistant.getLibelle() %></a>
																
															<%if(!tp.equals("CO")){ %>
																<img alt="" src='resourcesCtrl?elmnt=<%=EncryptionUtil.encrypt(famillePersistant.getId().toString()) %>&path=famille&rdm=<%=famillePersistant.getDate_maj().getTime() %>' style='border-radius: 18px;' width='32' height='32' onerror="this.onerror=null;this.remove();"/> 
															<%}	
															if(isNode){	%>
																<std:linkPopup actionGroup="C" classStyle="" action="stock.famille.initOrderMenu" workId="<%=famillePersistant.getId().toString() %>" tooltip="Ordonner la racine" icon="fa fa-sort-amount-desc"/>
															<%} %>
															</span> 
															 
												<%
												if(isNode){ %>
																<ul style="<%=isShowActifOnly && BooleanUtil.isTrue(famillePersistant.getIs_disable()) ? "display:none;":"" %>">
														<%} %>
																	<%
												oldLevel = famillePersistant.getLevel();
											}
											%>
																</li>
															</ul>
															</ul>
									<%} %>			
														</div>
														<!--  end div widget body -->
													</div>
													<!--  end div the whole widget -->
												</div>
						</div>
					</div>
				</div></div></div></div>
		
	</div>
</div>
</std:form>
