<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="appli.model.domaine.administration.persistant.GedFichierPersistant"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="appli.model.domaine.administration.persistant.GedPersistant"%>
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

<script type="text/javascript">
$(document).ready(function (){
	getTabElement("#treeged-ul").treeview({
		collapsed: true,
		animated: "fast",
		persist: "cookie",
		unique: false,
		control: "#div-control",
		cookieId: "treeGed"
	});
	
	$(document).off('click', ".filetree a").on('click', ".filetree a", function(){
		getTabElement(".filetree a").css("background-color", "transparent");
		getTabElement("#del-tree-link, #upd-tree-link, #add-doc").css("display", "none");
		$(this).css("background-color", "#CDDC39");
		
		if($(this).attr("wact") || $(this).attr("targetBtn")){
			return;
		}
		getTabElement(".page-header a, #order_lnk").attr("params", "ged_cur="+$(this).attr("ged_parent")+"&ged_parent="+$(this).attr("ged_parent"));
		
		if(!$(this).attr("class") || $(this).attr("class").indexOf("notSelect") == -1){
			getTabElement("#del-tree-link, #upd-tree-link, #add-doc").css("display", "");
		}
	});
	
	getTabElement(".sub-header-act a").click(function(){
	   		if($(this).attr("targetBtn") == 'C'){
	   			getTabElement("#trigger_create").attr("params", $(this).attr("params"));
	   			getTabElement("#trigger_create").trigger("click");
	   		} else if($(this).attr("targetBtn") == 'CF'){ 
	   			getTabElement("#trigger_create_fic").attr("params", "tpline=FC&"+$(this).attr("params"));
	   			getTabElement("#trigger_create_fic").trigger("click");
	   		} else if($(this).attr("targetBtn") == 'U'){
	   			getTabElement("#trigger_update").attr("params", $(this).attr("params"));
	   			getTabElement("#trigger_update").trigger("click");
	   		}
	});
});

function resizeIframe(obj) {
    obj.style.height = obj.contentWindow.document.documentElement.scrollHeight + 'px';
  }
  
function getValOrEmpty(val){
	return (!val || val == null || val == 'null') ? '&nbsp;' : val;
}


function formatDate(date) {
	if(!date || date == null || date == ''){
		return;
	}
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear(),
    	 hour = d.getHours();
   	 minute = d.getMinutes();
   	 
    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;
    if (hour < 10) hour = '0' + hour;
    if (minute < 10) minute = '0' + minute;
    
    var dt = [day, month, year].join('/')+' '+[hour, minute].join(':');
    
    return dt;
}

  
function info(obJson,tp){
	var data = JSON.parse(obJson);
	var html = "";
	
	if(tp=="fch"){
		document.getElementById("iframe_a").style.display = "block";
		html = '<div class="row">'
			+ '<div class="col-md-2">Nom fichier :</div>		<div class="col-md-4">'+getValOrEmpty(data.file_name)+'</div>'
			+ '<div class="col-md-2">Libellé :</div>			<div class="col-md-4">'+getValOrEmpty(data.libelle)+'</div>'
			+ '<div class="col-md-2">Commentaire :</div>		<div class="col-md-4">'+getValOrEmpty(data.commentaire)+'</div>'
			+ '<div class="col-md-2">Date de Creation  :</div>	<div class="col-md-4">'+getValOrEmpty(formatDate(data.date_creation))+'</div>'
			+ '<div class="col-md-2">Extention :</div>			<div class="col-md-4">'+getValOrEmpty(data.extention)+'</div>'
			+ '<div class="col-md-2">Chemin :</div>				<div class="col-md-4">'+getValOrEmpty(data.path)+'</div>'
			+ '<div class="col-md-2">Nom dossier :</div>		<div class="col-md-4">'+getValOrEmpty(data.opc_ged.libelle)+'</div>'
			+ '<div class="col-md-2">Date de Mise a jour :</div><div class="col-md-4">'+getValOrEmpty(formatDate(data.date_maj))+'</div>'
			+'</div>';
	} else{
		html = 
			'<div class="row">'	
			+ '<div class="col-md-2">Code :</div>				<div class="col-md-4" >'+getValOrEmpty(data.code)+'</div>'
			+ '<div class="col-md-2">Libellé :</div>			<div class="col-md-4" >'+getValOrEmpty(data.libelle)+'</div>'
			+ '<div class="col-md-2">Type de dossier :</div>	<div class="col-md-4" >'+getValOrEmpty(data.type_ged)+'</div>'
			+ '<div class="col-md-2">Date de Creation :</div>	<div class="col-md-4" >'+getValOrEmpty(formatDate(data.date_creation))+'</div>'
			+ '<div class="col-md-2">Date de Mise a jour :</div><div class="col-md-4" >'+getValOrEmpty(formatDate(data.date_maj))+'</div>'
			+'</div>';
			document.getElementById("iframe_a").style.display = "none";
	}
	
	var widowHeight = $(window).height();
	var widowWidth = $(window).width();
	$("#iframe_a").css("width", (widowWidth-520)+"px").css("height", (widowHeight-190)+"px");
	
	document.getElementById("toogled").innerHTML = html;
}


let mybutton = document.getElementById("myBtn");

//When the user scrolls down 20px from the top of the document, show the button
window.onscroll = function() {scrollFunction()};

function scrollFunction() {
	if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
	 mybutton.style.display = "block";
	} else {
	 mybutton.style.display = "none";
	}
}

//When the user clicks on the button, scroll to the top of the document
function topFunction() {
/*	document.body.scrollTop = 0;
	document.documentElement.scrollTop = 0;
	*/
	$("html, body").animate({ scrollTop: 0 }, "slow");
}


</script>

<style>
#toogled {
    margin-left: -20px;
    margin-top: 20px;
    font-size: 12px;
    white-space: nowrap;
}

#toogled .col-md-2{
	color: blue;
}
#myBtn {
  display: none;
  position: fixed;
  bottom: 20px;
  right: 30px;
  z-index: 99;
  font-size: 18px;
  border: none;
  outline: none;
   background-color: red; /*green #34990a */
  color: white;
  cursor: pointer;
  padding: 15px;
  border-radius: 4px;
}

#myBtn:hover {
  background-color: #555;
}

</style>

<!-- Page Breadcrumb -->

<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion des documents</li>
		<li class="active">Recherche</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;width: 35%;margin-left: -15px;">
	        <std:link actionGroup="C" style="float:left;margin-left:5px;width: 80px;padding-left: 9px;" classStyle="btn btn-info" action="admin.ged.majArbreClient" icon="fa-3x fa-refresh" value=" Clients" tooltip="Metter à jour arbre des client" />
		    <std:link actionGroup="C" style="float:left;margin-left:5px;width: 100px;padding-left: 7px;" classStyle="btn btn-info" action="admin.ged.majArbreFournisseur" icon="fa-3x fa-refresh" value="Fournisseur" tooltip="Metter à jour arbre des fournisseur" />
            <std:link actionGroup="C" style="float:left;margin-left:5px;width: 90px;padding-left: 7px;" classStyle="btn btn-info" action="admin.ged.majArbreEmploye" icon="fa-3x fa-refresh" value="Employes" tooltip="Metter à jour arbre des employes" />
	
	
<!-- 		<div class="sub-header-act" style="float: right;"> -->
<!-- 			<a href="javascript:" targetBtn="C" class="btn btn-default" style="margin-left:5px;"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span>Ajouter un répértoire</a> -->
<!-- 			<a href="javascript:" id="upd-tree-link" targetBtn="U" class="btn btn-default" style="display: none;"><span class="glyphicon glyphicon-eye-open" aria-hidden="true">Afficher</span></a> -->
<!-- 			<a href="javascript:" targetBtn="CF" class="btn btn-default" id="add-doc" style="display: none;"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span>Ajouter un document</a> -->
			
<%-- 			<a href="javascript:" id="del-tree-link" targetBtn="D" class="btn btn-danger" style="display: none;" wact="<%=EncryptionUtil.encrypt("admin.ged.work_delete") %>"> --%>
<!-- 				 <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>Supprimer -->
<!-- 			 </a> -->
<!-- 		  <button type="button" class="btn btn-info" data-toggle="modal" data-target="#myModal">Filter</button> -->
<!-- 		 </div> -->
<!-- 		 | -->
		 
	</div>
	<!--Header Buttons-->
	<jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
	<!--Header Buttons End-->
</div>
<!-- /Page Header -->

<!-- Page Body -->
<div class="page-body">

	<%if(ControllerUtil.getMenuAttribute("isTabMnu", request) != null){ %>
	<div class="row">
		<%request.setAttribute("curMnu", "ged");  %>
		<jsp:include page="/domaine/stock/fournisseur_header_tab.jsp" />

	<%if(ContextAppli.IS_SYNDIC_ENV()
			&& ControllerUtil.getMenuAttribute("userCoproId", request) != null){	
				String tab = "ged";
				%>
				<ul class="nav nav-tabs" id="myTab">
					<li <%="copro".equals(tab) ? " class='active'":"" %>><a data-toggle="tab" href="#descripton" wact="<%=EncryptionUtil.encrypt("reg.userCopro.work_edit")%>"><i class="fa fa-tachometer" style="color: #03A9F4;"></i> Copropriété </a></li>
					<li <%="lot".equals(tab) ? " class='active'":"" %>><a data-toggle="tab" href="#repCaisse" wact="<%=EncryptionUtil.encrypt("ges.lot.work_find")%>"><i class="fa fa-users" style="color: #3F51B5;"></i> Lots </a></li>
					<li <%="ass".equals(tab) ? " class='active'":"" %>><a data-toggle="tab" href="#repCaisse" wact="<%=EncryptionUtil.encrypt("ges.associe.work_edit")%>"><i class="fa fa-users" style="color: #3F51B5;"></i> Associés </a></li>
					<li <%="appel".equals(tab) ? " class='active'":"" %>><a data-toggle="tab" href="#repCaisse" wact="<%=EncryptionUtil.encrypt("reg.appelFond.work_find")%>"><i class="fa fa-users" style="color: #3F51B5;"></i> Appels de fonds </a></li>
					<li <%="enc".equals(tab) ? " class='active'":"" %>><a data-toggle="tab" href="#repCaisse" wact="<%=EncryptionUtil.encrypt("compta.ecriture.work_find")%>"><i class="fa fa-users" style="color: #3F51B5;"></i> Encaissements </a></li>
					<li <%="proc".equals(tab) ? " class='active'":"" %>><a data-toggle="tab" href="#repCaisse" wact="<%=EncryptionUtil.encrypt("reg.procedure.work_edit")%>"><i class="fa fa-users" style="color: #3F51B5;"></i> Procédures </a></li>
					<li <%="ged".equals(tab) ? " class='active'":"" %>><a data-toggle="tab" href="#repCaisse" wact="<%=EncryptionUtil.encrypt("adm.ged.work_find")%>"><i class="fa fa-users" style="color: #3F51B5;"></i> Ged </a></li>
				</ul>
	<%} %>		
		
     </div>
     <%} %>
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>

<a id="trigger_create" data-toggle="modal" data-backdrop="static" data-keyboard="false" data-target="#generic_modal" targetdiv="generic_modal_body" href="javascript:" wact="<%=EncryptionUtil.encrypt("admin.ged.work_init_create") %>" style="display: none;"></a>
<a id="trigger_update" data-toggle="modal" data-backdrop="static" data-keyboard="false" data-target="#generic_modal" targetdiv="generic_modal_body" href="javascript:" wact="<%=EncryptionUtil.encrypt("admin.ged.work_edit") %>" style="display: none;"></a>
<a id="trigger_create_fic" data-toggle="modal" data-backdrop="static" data-keyboard="false" data-target="#generic_modal" targetdiv="generic_modal_body" href="javascript:" wact="<%=EncryptionUtil.encrypt("admin.ged.work_init_create") %>" style="display: none;"></a>

	<!-- row -->
	<div class="row">
		<std:form name="search-form">

			<div class="col-lg-3 col-sm-3 col-xs-12">
				<div class="widget" style="width: 398px;">
				<div class="widget-header bg-palegreen">
                        <i class="widget-icon fa fa-arrow-down"></i>
                        <span class="widget-caption">Documents archivés</span>
                    </div>
					<div class="widget-body">
								<div id="div-control" style="float: left;">
									<a title="Collapse the entire tree below" href="#"><img src="resources/framework/img/tree/minus.gif" /> Fermer</a> |
									<a title="Expand the entire tree below" href="#"><img src="resources/framework/img/tree/plus.gif" /> Ouvrir</a> |
								</div>
								<std:linkPopup id="order_lnk" actionGroup="C" classStyle="" action="admin.ged.initOrder" tooltip="Ordonner la racine" icon="fa fa-sort-amount-desc" value="Ordonner"/>
								<hr>
								
<%
String libelleSearch = (String)request.getAttribute("file_name");
String extentionSearch = (String)request.getAttribute("extention");
boolean isSearchMode = StringUtil.isNotEmpty(libelleSearch) || StringUtil.isNotEmpty(extentionSearch);

List<GedPersistant> listGed = (List<GedPersistant>) request.getAttribute("listGed");
if(listGed !=  null){%>
	<ul id="treeged-ul" class="filetree">
	 <%
		Integer oldLevel = null;
		String path = (String) ControllerUtil.getMenuAttribute("tp", request);
							 
		for(GedPersistant gedPersistant : listGed){
		
				int nbrFiles = (gedPersistant.getList_fichier() != null ? gedPersistant.getList_fichier().size() : 0);
				boolean isNode = (gedPersistant.getB_right()- gedPersistant.getB_left() > 1);
				String wid = ""+gedPersistant.getId();
				String jsonObj = ControllerUtil.getJSonDataAnnotStartegy(gedPersistant);
				if(oldLevel != null){
					if(oldLevel > gedPersistant.getLevel()){
						while(oldLevel > gedPersistant.getLevel()){	%>
								</li> 
							</ul>
							<%
							oldLevel--;
						}
					} else if(oldLevel == gedPersistant.getLevel()){%>
							</li>
							<%	}
				}
				%>
						<li> 
						<span class="folder" style="border-bottom: 1px dashed #FFEB3B;"> 
							<a href="javascript:" onclick='info(`<%=jsonObj %>`,`rep`);' style="<%=isNode ? "color:black;" :""%>" ged_parent="<%=wid%>"> <%=gedPersistant.getLibelle() %></a>
							<% if(gedPersistant.getList_fichier() != null && gedPersistant.getList_fichier().size() > 0){%>
							<span class='badge badge-sky'><%=gedPersistant.getList_fichier().size() %> fichiers</span>
							<%} %>	
						</span>
							
					<% if(gedPersistant.getList_fichier() != null && gedPersistant.getList_fichier().size() > 0){%>
							<ul>			
							<% for(GedFichierPersistant gedFicP : gedPersistant.getList_fichier()){
									if(isSearchMode && 
											(
												(StringUtil.isNotEmpty(libelleSearch) && gedFicP.getLibelle().toLowerCase().indexOf(libelleSearch.toLowerCase()) == -1)
													&& (StringUtil.isNotEmpty(extentionSearch) && !gedFicP.getExtention().equalsIgnoreCase(extentionSearch))
											)
										){
										continue;
									}
							%>
								<li>	
									<%
									String gedParentId = EncryptionUtil.encrypt(gedFicP.getOpc_ged().getId().toString());
									String gedDetId = EncryptionUtil.encrypt(gedFicP.getId().toString());
									String params = "tpline=FC&ged_parent="+gedFicP.getOpc_ged().getId();
									String commentFile = StringUtil.getValueOrEmpty(gedFicP.getFile_name()) +"\n"+StringUtil.getValueOrEmpty(gedFicP.getCommentaire());
									String obJson = ControllerUtil.getJSonDataAnnotStartegy(gedFicP);
									%>
									<span style="border-bottom: 1px dashed #FFEB3B;background: url(resources/framework/img/sheet.png) 0 0 no-repeat;padding-left: 22px;font-weight: bold;">
										<std:linkPopup classStyle="" params="<%=params %>" value="<%=gedFicP.getLibelle() %>" action="admin.ged.work_edit" workId="<%=gedFicP.getId().toString() %>" tooltip="<%=commentFile %>"/>			
									</span>
									
									<%if(StringUtil.isNotEmpty(gedFicP.getPath())){ %>
									<a class="btn btn-xs notSelect" style="color: green;" onclick='info(`<%=obJson.replaceAll("\\\\", "/") %>`,`fch`);' href="front?w_f_act=<%=EncryptionUtil.encrypt("admin.ged.downLoadDocument")%>&skipI=true&skipP=true&pj=<%=gedDetId%>&nm=<%=gedFicP.getFile_name()%>" target="iframe_a">
										<span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
									</a>
									<%
										params = "skipI=true&skipP=true&pj="+gedFicP.getId()+"&nm="+gedFicP.getFile_name()+"&isdown=1"; 
									%>
									<std:link classStyle="btn btn-xs notSelect" action="admin.ged.downLoadDocument" params="<%=params%>" tooltip="Télécharger" target="downloadframe">
										<span class="glyphicon glyphicon-download" aria-hidden="true" style="color: blue;"></span>
									</std:link>
								  <%if(!BooleanUtil.isTrue(gedFicP.getIs_not_sup())){ %>
									<std:link actionGroup="D" classStyle="btn btn-xs notSelect" action="admin.ged.deleteDetailFichier" workId="<%=gedFicP.getId().toString() %>" tooltip="Supprimer">
										<span class="glyphicon glyphicon-trash" aria-hidden="true" style="color: red;"></span>
									</std:link>
									<%} %>
									<%} %>
									
								</li>				
							<%} %>
							</ul>
					<%} %>
						 
			<%
			if(isNode){ %>
							<ul>
								<%} %>
								<%
			oldLevel = gedPersistant.getLevel();
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
			
			<div style=" width: calc(100vw - 660px);
   						 float: right;
   						 margin-top: -41px;">
				<div style="position: fixed;"> 
					<div class="sub-header-act">
						<a href="javascript:" targetBtn="C" class="btn btn-default" style="margin-left: 5px;">
							<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>Ajouter un répértoire
						</a> 
						<a href="javascript:" id="upd-tree-link" targetBtn="U" class="btn btn-default" style="display: none;">
							<span class="glyphicon glyphicon-eye-open" aria-hidden="true">Afficher</span>
						</a> 
						<a href="javascript:" targetBtn="CF" class="btn btn-default" id="add-doc" style="display: none;">
							<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>Ajouter un document
						</a> 
						<a href="javascript:" id="del-tree-link" targetBtn="D" class="btn btn-danger" style="display: none;" wact="<%=EncryptionUtil.encrypt("admin.ged.work_delete")%>"> 
							<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>Supprimer
						</a>
					<button type="button" class="btn btn-info" data-toggle="modal" data-target="#myModal">Filter</button>
					<button type="button" onclick="topFunction();" id="myBtn" title="Aller au top"><i class="fa fa-arrow-up"></i></button>
				</div>
				<div class="container" id="toogled"></div>
					<iframe name="iframe_a" id="iframe_a" style="border: 0;overflow:auto;"></iframe>
			    </div>
			</div>

			<div class="col-lg-4 col-sm-4 col-xs-12">
				<div class="modal fade" id="myModal" role="dialog">
					<div class="modal-dialog">

						<!-- Modal content-->
						<div class="modal-content">
							<div class="modal-header widget-header bg-blueberry">

								<i class="widget-icon fa fa-arrow-down"></i> <span class="widget-caption">Chercher un document</span>
							</div>
							<div class="modal-body">
								<div class="form-group">
									<std:label classStyle="control-label col-md-4" value="Fichier" />
									<div class="col-md-8">
										<std:text name="file_name" value="${file_name }" type="string" placeholder="Fichier" />
									</div>
								</div>
								<div class="form-group">
									<std:label classStyle="control-label col-md-4" value="Extention" />
									<div class="col-md-8">
										<std:text name="extention" value="${extention }" type="string" placeholder="Extention" />
									</div>
								</div>

								<div class="form-group">
									<std:label classStyle="control-label col-md-4" value="Client" />
									<div class="col-md-8">
										<std:select name="client" value="${clientId }" data="${listClient }" key="id" labels="nom;' ';prenom" type="long" placeholder="Client" />
									</div>
								</div>
								<div class="form-group">
									<std:label classStyle="control-label col-md-4" value="Fournisseur" />
									<div class="col-md-8">
										<std:select name="fournisseur" value="${fournisseurId }" data="${listFournisseur }" key="id" labels="libelle" type="long" placeholder="Fournisseur" />
									</div>
								</div>
								<div class="form-group">
									<std:label classStyle="control-label col-md-4" value="Employé" />
									<div class="col-md-8">
										<std:select name="employe" value="${employeId }" data="${listEmploye }" key="id" labels="nom;' ';prenom" type="long" placeholder="Employé" />
									</div>
								</div>
								<div class="form-group" style="text-align: center;">
									<std:button action="admin.ged.getFile" closeOnSubmit="true" value="Filtrer" classStyle="btn btn-success" />
									<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">Fermer</button>
								</div>
							</div>

						</div>
					</div>
				</div>
			</div>	
		</std:form>
	</div>
	<!-- end widget row -->
</div>
<!-- end page-body div -->


<script type="text/javascript">
	/*Handles ToolTips*/
	$("[data-toggle=tooltip]").tooltip({
		html : true
	});
</script> 