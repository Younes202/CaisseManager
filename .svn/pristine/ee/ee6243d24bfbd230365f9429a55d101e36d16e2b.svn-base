<%@page import="appli.model.domaine.vente.persistant.MenuCompositionPersistant"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="appli.model.domaine.stock.service.IArticleService"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="java.util.List"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<link href="resources/framework/css/tree/jquery.treeview.css?v=1.0" rel="stylesheet" type="text/css" />

<script src="resources/framework/js/tree/jquery.treeview.js"></script>

<style>
	.filetree span.folder { background: url(resources/framework/img/folder_close_blue.png) 0 0 no-repeat !important; }
</style>


<script type="text/javascript">
$(document).ready(function (){
	getTabElement("#treeattack-ul").treeview({
		collapsed: true,
		control: "#div-control",
		animated: "fast",
		persist: "cookie",
// 		unique: true,
		cookieId: "treeMenu"
	});
	
	$(document).on('click', ".filetree a", function(){
		getTabElement(".filetree a").css("background-color", "transparent");
		$(this).css("background-color", "#CDDC39");
		getTabElement(".page-header a, #order_lnk").attr("params", "fam="+$(this).attr("fam")+"&workId="+$(this).attr("fam"));
		getTabElement("#del-tree-link, #upd-tree-link, #dup-tree-link").removeAttr("disabled").css("display", "");
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
   		} else if($(this).attr("targetBtn") == 'DU'){
   			getTabElement("#trigger_duplic").attr("params", $(this).attr("params"));
   			getTabElement("#trigger_duplic").trigger("click");
   		}
	});
	
	getTabElement("#actifs-activator").change(function(){
		getTabElement("#BTN_find").trigger("click");
	});
});
</script>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion de caisse</li>
		<li>Fiche des menus</li>
		<li class="active">Recherche</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<std:form name="search-form">
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<c:if test="${empty isEditable or isEditable }">
			<std:link actionGroup="C" classStyle="btn btn-default" style="float:left;" icon="fa-3x fa-plus" value="Ajouter" forceShow="true"/>
		</c:if>	
		<div style="margin-left: 100px;margin-top: 6px;float: left;">
			<span>Actifs</span>          
	   		<label style="margin-top: 3px;" title="Uniquements actifs">
	            <input class="checkbox-slider toggle colored-blue" type="checkbox" ${isCheked ? ' checked="checked"':'' } name="actifs-activator" id="actifs-activator" style="display: none;">
	            <span class="text"></span>
	        </label>
        </div>
		
		<std:link actionGroup="U" id="upd-tree-link" classStyle="btn btn-default" style="display: none;" icon="fa-3x fa-pencil" value="Modifier" forceShow="true"/>
		
		<std:link actionGroup="DU" id="dup-tree-link" classStyle="btn btn-primary" style="display: none;" icon="fa-3x fa-copy" value="Dupliquer" forceShow="true"/>
		
		<std:link id="act-tree-link" actionGroup="U" action="caisse.menuComposition.desactiver" style="display: none;" icon="fa fa-3x fa-unlock" classStyle="btn btn-success" tooltip="Activer':'Désactiver" forceShow="true"/>
		<std:link id="dis-tree-link" actionGroup="U" action="caisse.menuComposition.desactiver" style="display: none;" icon="fa fa-3x fa-lock" classStyle="btn btn-warning" tooltip="Désactiver" forceShow="true"/>
		<std:link actionGroup="D" id="del-tree-link" classStyle="btn btn-danger" style="display: none;" action="caisse.menuComposition.work_delete" icon="fa-3x fa-trash" value="Supprimer"  forceShow="true"/>
		<std:link classStyle="btn btn-default" action="caisse.menuComposition.work_find" style="display:none;" id="BTN_find" />
	</div>
	<!--Header Buttons-->
	<jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
	<!--Header Buttons End-->
</div>
<!-- /Page Header -->

<!-- Page Body -->
<div class="page-body">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>

<a id="trigger_create" data-toggle="modal" data-backdrop="static" data-keyboard="false" data-target="#generic_modal" targetdiv="generic_modal_body" href="javascript:" wact="<%=EncryptionUtil.encrypt("caisse.menuComposition.work_init_create") %>" style="display: none;"></a>
<a id="trigger_update" data-toggle="modal" data-backdrop="static" data-keyboard="false" data-target="#generic_modal" targetdiv="generic_modal_body" href="javascript:" wact="<%=EncryptionUtil.encrypt("caisse.menuComposition.work_init_update") %>" style="display: none;"></a>
<a id="trigger_duplic" data-toggle="modal" data-backdrop="static" data-keyboard="false" data-target="#generic_modal" targetdiv="generic_modal_body" href="javascript:" wact="<%=EncryptionUtil.encrypt("caisse.menuComposition.duplic") %>" style="display: none;"></a>

	<!-- row -->
	<div class="row">
		<a href="javascript:" onclick="$('#aide_div').toggle(100);" style="padding-left: 17px;">Aide <i class="fa fa-info-circle" style="color: blue;"></i></a>
		<div class="alert alert-warning fade in" id="aide_div" style="display: none;">
                    <i class="fa-fw fa fa-warning"></i>
                    <strong>Configuration des menus</strong><br>
                    La composition doit être sur <b style="font-weight: bold;">plusieurs</b> niveaux.<br>
                    Le niveau correspondant à un menu, doit avoir la case "Est un menu" cochée et le prix de vente saisi.<br>
                    <br>
                    <b>Exemple :</b><br>
                    	<b style="color: green;">BURGERS</b><br>
                    	<span style="margin-left: 20px;color:blue;">BURGER MAXI</span> (case "Est un menu" cochée et le prix saisi)<br>
                    	<span style="margin-left: 50px;">CHOIX VIANDES</span><br>
						<span style="margin-left: 50px;">CHOIX SAUCES</span><br>
						<span style="margin-left: 50px;">CHOIX SUPPLEMENTS</span><br>
						<span style="margin-left: 50px;">CHOIX OPTIONS</span><br>
			Si le libellé d'un élément commence par <b style="color: red;">#</b> alors il ne s'affichera pas dans le ticket de caisse.			
        </div>
	</div>
	<div class="row">
			<div class="col-lg-6 col-sm-6 col-xs-12">
				<div class="widget">
				<div class="widget-header bg-palegreen">
                        <i class="widget-icon fa fa-arrow-down"></i>
                        <span class="widget-caption">Composition des menus</span>
                    </div>
					<div class="widget-body">
					
	<div id="div-control" style="float: left;">
		<a title="Collapse the entire tree below" href="#"><img src="resources/framework/img/tree/minus.gif" /> Fermer</a> |
		<a title="Expand the entire tree below" href="#"><img src="resources/framework/img/tree/plus.gif" /> Ouvrir</a> |
	</div>
	<std:linkPopup id="order_lnk" actionGroup="C" classStyle="" action="caisse.menuComposition.initOrderMenu" tooltip="Ordonner la racine" icon="fa fa-sort-amount-desc" value="Ordonner"/>
	<hr>
					
<%
boolean isShowActifOnly = StringUtil.isTrue(""+request.getAttribute("actifOnly"));
List<MenuCompositionPersistant> listMenu = (List<MenuCompositionPersistant>) request.getAttribute("listMenus");
if(listMenu !=  null){%>
						<ul id="treeattack-ul" class="filetree">
							 <%
		Integer oldLevel = null;
		IArticleService service = (IArticleService)ServiceUtil.getBusinessBean(IArticleService.class);
									
		for(MenuCompositionPersistant menuPersistant : listMenu){
			if(menuPersistant.getLevel() == 0){
				continue;
			}
			
			boolean isNode = (menuPersistant.getB_right()- menuPersistant.getB_left() > 1);
			String wid = EncryptionUtil.encrypt(""+menuPersistant.getId());
			
			if(oldLevel != null){
				if(oldLevel > menuPersistant.getLevel()){
					while(oldLevel > menuPersistant.getLevel()){	%>
							</li> 
						</ul>
						<%
						oldLevel--;
					}
				} else if(oldLevel == menuPersistant.getLevel()){%>
						</li>
						<%	}
			}
			%>
						<li style="<%=isShowActifOnly && BooleanUtil.isTrue(menuPersistant.getIs_desactive()) ? "display:none;":"" %>"> 
						<%int nbrComposante = menuPersistant.getList_composition().size(); %>
						<span class="<%=isNode ? "folder" :"file"%>" style="border-bottom: 1px dashed #FFEB3B;"> 
							<a wact='<%=EncryptionUtil.encrypt("caisse.menuComposition.load_detail") %>' params='det=<%=menuPersistant.getId() %>' targetDiv="det_div" href="javascript:" style="<%=BooleanUtil.isTrue(menuPersistant.getIs_desactive())?"text-decoration: line-through !important;":""%><%=isNode ? "color:black;" :""%>" stat="<%=BooleanUtil.isTrue(menuPersistant.getIs_desactive())?"1":"0"%>" fam="<%=wid%>"> 
								<%=menuPersistant.getCode() %> - <%=menuPersistant.getLibelle() %> <%=nbrComposante>0?"<span class='badge badge-sky'>"+nbrComposante+"</span>" : "" %>
							</a>
							<%
							if(!BigDecimalUtil.isZero(menuPersistant.getMtt_prix())){%>
								<span class='badge badge-success graded'><%=BigDecimalUtil.formatNumber(menuPersistant.getMtt_prix()) %> <%=StrimUtil.getGlobalConfigPropertie("devise.html") %></span>
							<%} %>
							<img alt="" src='resourcesCtrl?elmnt=<%=EncryptionUtil.encrypt(menuPersistant.getId().toString()) %>&path=menu&rdm=<%=menuPersistant.getDate_maj().getTime() %>' width='32' height='32' onerror="this.onerror=null;this.remove();"/>
							
							<% if(BooleanUtil.isTrue(menuPersistant.getIs_menu())){%>
								<i class="fa fa-medium" title="Est un menu"></i>
							<%} %>
							
							<%if(isNode){	%>
							<std:linkPopup actionGroup="C" classStyle="" action="caisse.menuComposition.initOrderMenu" workId="<%=menuPersistant.getId().toString() %>" tooltip="Ordonner la racine" icon="fa fa-sort-amount-desc"/>
							<%} %>
						</span>
						 
			<%
			if(isNode){ %>
							<ul style="<%=isShowActifOnly && BooleanUtil.isTrue(menuPersistant.getIs_desactive()) ? "display:none;":"" %>">
								<%} %>
								<%
			oldLevel = menuPersistant.getLevel();
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
			<div class="col-lg-6 col-sm-6 col-xs-12" id="det_div">
					
			</div>
		<div>
			
	</div>
	</div>
	<!-- end widget row -->
</div>
<!-- end page-body div -->
</std:form>
