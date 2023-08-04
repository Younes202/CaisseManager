<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="appli.model.domaine.caisse.service.ICaisseJourneeService"%>
<%@page import="java.util.List"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementPersistant"%>
<%@page import="appli.model.domaine.vente.persistant.CaissePersistant"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/fn" prefix="fn"%>
<%@page errorPage="/commun/error.jsp" %>

<%
List<CaissePersistant> listCaisse = (List<CaissePersistant>)request.getAttribute("listCaisse");
List<CaisseMouvementPersistant> listMvm = (List<CaisseMouvementPersistant>)request.getAttribute("listMvm");
ICaisseJourneeService caisseSrv = ServiceUtil.getBusinessBean(ICaisseJourneeService.class);
%>

<style>
	#tbl_time td{
    	border-right: 1px solid #bababa;
    }
    #tbl_time tr:nth-child(even) {
	  background-color: #f2f2f2;
}
    
    .sep_border{
    	background-color: #ffffff;
    }
</style>

 <!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Caisse enregistreuse</li>
         <li>Cuisine</li>
         <li class="active">Temps commandes</li>
     </ul>
 </div>
<!-- /Page Breadcrumb -->

<std:form name="search-form">
  <!-- Page Header -->
  <div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
      </div>
      <!--Header Buttons-->
      <jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
      <!--Header Buttons End-->
  </div>
  <!-- /Page Header -->
<div class="tabbable">
	<ul class="nav nav-tabs" id="myTab">
	<%
	String tpSuivi = (String)ControllerUtil.getMenuAttribute("tpSuivi", request);
	%>
		<li class="<%="temps".equals(tpSuivi) || tpSuivi == null ? " active" : ""%>"><a data-toggle="tab" href="#descripton" wact="<%=EncryptionUtil.encrypt("caisse_restau.caisseConfigurationRestau.load_temps_cuisine")%>" >Temps par commandes </a></li>
		<li class="<%="stat".equals(tpSuivi) ? " active" : ""%>"><a data-toggle="tab" href="#journeeCaisse" wact="<%=EncryptionUtil.encrypt("caisse.caisse.tempsGlobalParEmploye")%>"> Statuts des commandes </a></li>
	</ul>
</div>
	<!-- Page Body -->
	<div class="page-body">
		<div class="row" style="margin-top: 15px;">
			<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
		</div>
		<div class="row">
	        <div class="form-group">
	        	<std:label classStyle="control-label col-md-2" value="Date début" />
	            <div class="col-md-2">
	                 <std:date name="dateDebut" value="${dateDebut }"/>
	            </div>
	            <div class="col-md-2" style="text-align: center;">
	            	<std:link action="caisse_restau.caisseConfigurationRestau.load_temps_cuisine" params="prev=1" icon="fa fa-arrow-circle-left" tooltip="Mois précédent" />
	            	<std:link action="caisse_restau.caisseConfigurationRestau.load_temps_cuisine" params="next=1" icon="fa fa-arrow-circle-right" tooltip="Mois suivant" />
	            </div>
	            
	            <std:label classStyle="control-label col-md-1" value="Date fin" />
	            <div class="col-md-2">
	                 <std:date name="dateFin" value="${dateFin }"/>
	            </div>
	            <div class="col-md-2">
	           	 	<std:button action="caisse_restau.caisseConfigurationRestau.load_temps_cuisine" value="Filtrer" classStyle="btn btn-primary" />
	           	 </div>	
	       </div>
	   </div>
		<!-- row -->
		<div class="row" style="margin-left: 0px;margin-right: 0px;">
	 		<table id="tbl_time" class="table table-striped" style="width: 100%;font-size: 12px;border: 1px solid #bababa;">
	 			<thead>
	 				<tr style="background-color: #bababa;">
	 					<th style="width: 141px;background-color:white;border-right: 2px solid #595656;"></th>
	 					<%for(CaissePersistant caP : listCaisse){ %>
	 						<th style="text-align: center;"><%=caP.getReference() %></th>
	 						<th style="width: 1%;border-top: 0px #ddd;" class="sep_border"></th>
	 					<%} %>
	 					
	 				</tr>
	 			</thead>
	 			<tbody>
					<%String cuisineEdit = (String) ControllerUtil.getMenuAttribute("cuisineEdit", request); %>
					<%
	 				String tpName = null;
	 				for(CaisseMouvementPersistant mvmP : listMvm){ 
	 					Map<Long, List<CaisseMouvementArticlePersistant>> mapArts = new HashMap<>();
 						String[] vals = StringUtil.getArrayFromStringDelim(mvmP.getCaisse_cuisine(), ";");
 						
 						if(vals == null){
 							continue;
 						}
	 				%>
	 					<tr>
	 						<td style="color: #595656;border-top: 1px solid;border-right: 1px solid #595656;">	    
					 			<std:linkPopup style="width: 100%" classStyle="btn btn-sm btn-primary" params="cuisineEdit" action="caisse.journee.edit_mouvement" workId="<%=mvmP.getId().toString() %>">
									<%=mvmP.getRef_commande() %> <span class="fa fa-eye"></span>
								</std:linkPopup>
	 							<br>
	 							<%	String tp = mvmP.getType_commande();
	 								if("E".equals(mvmP.getType_commande())){
	 									tp = " class=\"fa fa-dropbox\"";
	 									tpName = "A emporter";
	 								} else if("L".equals(mvmP.getType_commande())){
	 									tp = "class=\"fa fa-motorcycle\"";
	 									tpName = "Livraison";
	 								} else if("P".equals(mvmP.getType_commande())){
	 									tp = "class=\"fa fa-street-view\"";
	 									tpName = "Sur place";
	 								}
	 							%>
	 							<span style="color: black;"><i style="color: black;" <%=tp %>> <strong><%= StringUtil.getValueOrEmpty(tpName) %></strong></i></span>
	 							<br>
	 							<%if(mvmP.getDate_debut_stat() != null && mvmP.getDate_fin_stat() != null){ %>
	 								 [<span style="color: fuchsia;">P: <%=DateUtil.getPeriodeInNormeStr(DateUtil.getDiffSeconds(mvmP.getDate_debut_stat(), mvmP.getDate_fin_stat())) %></span>]
	 							<%} %> 
	 							<%if(mvmP.getDate_debut_stat2() != null && mvmP.getDate_fin_stat2() != null){ %>
	 								 [<span style="color: fuchsia;">L: <%=DateUtil.getPeriodeInNormeStr(DateUtil.getDiffSeconds(mvmP.getDate_debut_stat2(), mvmP.getDate_fin_stat2())) %></span>]
	 							<%} %>
	 						</td>
	 						
	 						<%
							if(vals != null){
	 							for(String caisElmnt : vals){
	 								String[] inf = StringUtil.getArrayFromStringDelim(caisElmnt, ":");
	 								Long caisseId = Long.valueOf(inf[0]);
	 								List<CaisseMouvementArticlePersistant> listData = mapArts.get(caisseId);
	 								
	 								if(listData == null){
	 									listData = new ArrayList<>();
	 									mapArts.put(caisseId, listData);
	 								}
	 								
									CaisseMouvementArticlePersistant elementP = caisseSrv.findById(CaisseMouvementArticlePersistant.class, Long.valueOf(inf[1]));
									listData.add(elementP);
	 							}
							}
	 						
	 						for(CaissePersistant caP : listCaisse){
	 							StringBuilder data = new StringBuilder();
	 							List<CaisseMouvementArticlePersistant> listData = mapArts.get(caP.getId());
	 							if(listData != null){
		 							for(CaisseMouvementArticlePersistant elementP : listData){
		 								if(elementP == null){
		 									continue;
		 								}
		 								int tempsRef = 1000;
		 								int tempsPrep = -1, tempsLivr = -1;
		 								if(elementP.getDate_debut_stat() != null && elementP.getDate_fin_stat() != null){
		 									tempsPrep = DateUtil.getDiffSeconds(elementP.getDate_debut_stat(), elementP.getDate_fin_stat());
		 								}
		 								if(elementP.getDate_debut_stat2() != null && elementP.getDate_fin_stat2() != null){
		 									tempsLivr = DateUtil.getDiffSeconds(elementP.getDate_debut_stat2(), elementP.getDate_fin_stat2());
		 								}
		 								
	 									if(elementP.getOpc_article() != null){
	 										data.append("- "+elementP.getOpc_article().getLibelle());
	 										
	 										if(elementP.getOpc_article().getTemps_cuis_ref() != null){
	 											tempsRef =  elementP.getOpc_article().getTemps_cuis_ref();
	 										}
	 									} else if(elementP.getOpc_menu() != null){
	 										data.append("- "+elementP.getOpc_menu().getLibelle());
	 									}
		 								
		 								if(elementP.getOpc_user_stat() != null){
 											data.append("<span style='color: fuchsia;'> ["+elementP.getOpc_user_stat().getLogin()+"]</span>");
 										}
 										if(elementP.getDate_debut_stat() != null && elementP.getDate_fin_stat() != null){
 											String tooltip = "";
 											boolean isRetard = (tempsPrep>tempsRef);
 											
 											if(isRetard){
 												tooltip = "Retard : "+(tempsRef-tempsPrep);
 											} else{
 												tooltip = "Temps référence : "+(tempsRef==1000?"Non attribué":tempsRef);
 											}
 											data.append(" [<span title='"+tooltip+"' style='"+(!isRetard ? "color: blue;":"color: red;font-weight: bold;")+"'>P : "+DateUtil.getPeriodeInNormeStr(tempsPrep)+"</span>]");
 											
 											if(tempsLivr > 0){
 												data.append(" [<span title='"+tooltip+"' style='color: blue;'>L : "+DateUtil.getPeriodeInNormeStr(tempsLivr)+"</span>]");
 											}
 										}
 										
	 									data.append("<br>");
	 								  }
	 								}
	 								
	 							if(StringUtil.isNotEmpty(data.toString())){%>
	 								<td style="border-top: 2px solid;">
	 									<%=data.toString() %>
	 								</td>
	 								<td class="sep_border" style="border-top: 0px #ddd;"></td>
	 							<%} else{%>
	 								<td style="border-top: 2px solid;"></td>
	 								<td class="sep_border" style="border-top: 0px #ddd;"></td>
	 								
	 							<%
	 							}
	 						}%>
	 						</tr>
	 				<%} %>
	 			</tbody>
	 		</table>
	 	</div>
	</div>
 </std:form>




