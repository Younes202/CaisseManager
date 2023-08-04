<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="framework.model.beanContext.ComptePersistant"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="appli.model.domaine.stock.service.IArticleService"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
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
	.filetree span.folder { 
		background: url(resources/img/table/action/select.gif) 0 0 no-repeat !important; 
	}
	#treeattack-ul span{
		margin-top: -4px;
	}
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
		getTabElement(".page-header a").attr("params", "workId="+$(this).attr("fam"));
		
		if($(this).attr("isaj") == 1){
			getTabElement("#del-tree-link").removeAttr("disabled").css("display", "");
		} else{
			getTabElement("#del-tree-link").removeAttr("disabled").css("display", "none");
		}
		getTabElement("#upd-tree-link").removeAttr("disabled").css("display", "");
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
});
</script>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Comptabilit&eacute;</li>
		<li class="active">Plan comptable</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<%if(ContextGloabalAppli.getExerciceBean() != null){ %>
			<std:link actionGroup="C" classStyle="btn btn-default" icon="fa-3x fa-plus" value="Ajouter" forceShow="true"/>
			<std:link actionGroup="U" id="upd-tree-link" classStyle="btn btn-default" style="display: none;" icon="fa-3x fa-eye" value="Afficher" forceShow="true"/>
			<std:link id="act-tree-link" actionGroup="U" action="compta.compte.desactiver" style="display: none;" icon="fa fa-3x fa-unlock" classStyle="btn btn-success" tooltip="Activer':'D&eacute;sactiver" forceShow="true"/>
			<std:link actionGroup="D" id="del-tree-link" classStyle="btn btn-danger" style="display: none;" action="compta.compte.work_delete" icon="fa-3x fa-trash" value="Supprimer"  forceShow="true"/>
		<%} %>
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

<a id="trigger_create" data-toggle="modal" data-target="#generic_modal" targetdiv="generic_modal_body" href="javascript:" wact="<%=EncryptionUtil.encrypt("compta.compte.work_init_create") %>" style="display: none;"></a>
<a id="trigger_update" data-toggle="modal" data-target="#generic_modal" targetdiv="generic_modal_body" href="javascript:" wact="<%=EncryptionUtil.encrypt("compta.compte.work_edit") %>" style="display: none;"></a>

	<!-- row -->
	<div class="row">
		<std:form name="search-form">

			<div class="col-lg-12 col-sm-12 col-xs-12">
				<div class="widget">
				<div class="widget-header bg-palegreen">
                        <i class="widget-icon fa fa-arrow-down"></i>
                        <span class="widget-caption">Plan comptable</span>
                    </div>
					<div class="widget-body">
					
	<div id="div-control" style="float: left;">
		<a title="Collapse the entire tree below" href="#"><img src="resources/framework/img/tree/minus.gif" /> Fermer</a> |
		<a title="Expand the entire tree below" href="#"><img src="resources/framework/img/tree/plus.gif" /> Ouvrir</a> |
	</div>
	<hr>
					
<%
List<ComptePersistant> listCompte = (List<ComptePersistant>) request.getAttribute("listCompte");
if(listCompte !=  null){%>
	<ul id="treeattack-ul" class="filetree">
		<%
		Integer oldLevel = null;
		for(ComptePersistant compteP : listCompte){
			if(compteP.getLevel() == 0){
				//continue;
				compteP.setCode("");
				compteP.setLibelle("PLAN COMPTABLE");
			}
			
			boolean isNode = (compteP.getB_right()- compteP.getB_left() > 1);
			String wid = EncryptionUtil.encrypt(""+compteP.getId());
			
			if(oldLevel != null){
				if(oldLevel > compteP.getLevel()){
					while(oldLevel > compteP.getLevel()){	%>
							</li> 
						</ul>
						<%
						oldLevel--;
					}
				} else if(oldLevel == compteP.getLevel()){%>
						</li>
			<%	}
			}
			%>
				<li> 
				<span class="<%=isNode ? "folder" :"file"%>" style="border-bottom: 1px dashed #FFEB3B;"> 
					<a href="javascript:" isaj="<%=BooleanUtil.isTrue(compteP.getIs_ajoute()) ? 1 : 0 %>" style="<%=isNode ? "color:black;" :""%>" fam="<%=wid%>"> 
						<span style="font-weight: bold;"><%=compteP.getCode() %></span> - <%=compteP.getLibelle() %>
					</a>
				</span>
						 
			<%
			if(isNode){ %>
					<ul>
			<%} 
			oldLevel = compteP.getLevel();
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
		<div>
			
	</div>
			
		</std:form>
	</div>
	<!-- end widget row -->
</div>
<!-- end page-body div -->

