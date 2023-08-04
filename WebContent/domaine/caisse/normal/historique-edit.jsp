<%@page import="appli.model.domaine.util_srv.raz.RazService"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementOffrePersistant"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementPersistant"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="appli.model.domaine.stock.service.IArticleService"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.ArrayList"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli.TYPE_LIGNE_COMMANDE"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="appli.model.domaine.stock.persistant.MouvementArticlePersistant"%>
<%@page import="java.util.List"%>
<%@page import="appli.model.domaine.stock.persistant.MouvementPersistant"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementPersistant"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>

<%
boolean IS_UPD_CMD_HISTO = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("UPD_CMD_HISTO"));
%>
<style>
  #div_glob label{
    padding-top: 0px;
    color: #4caf50;
  }
  #div_glob .col-md-3{
  	padding-top: 7px;
  }
</style>

<script type="text/javascript">
	$(document).ready(function (){
		$(".lnk_mnu").click(function(){
			var mnuTr = $(".mnu_"+$(this).attr("mnu"));
			//
			if($(this).find("i").attr("class") == "fa fa-plus"){
				$(this).find("i").attr("class", "fa fa-minus");
				mnuTr.find(".span_mtt_art").show();
				mnuTr.find(".span_mtt_mnu").hide();
			} else{
				$(this).find("i").attr("class", "fa fa-plus");
				mnuTr.find(".span_mtt_art").hide();
				mnuTr.find(".span_mtt_mnu").show();
			}
			
			$("tr[id='tr_"+$(this).attr("mnu")+"']").toggle();
		});
	});
</script>

	<!-- widget grid -->
	<div class="widget" id="div_glob ">
		<div class="widget-header bordered-bottom bordered-blue" style="padding-bottom: 5px; padding-top: 5px; padding-right: 5px;">
			<span class="widget-caption">Vente : <b>${caisseMouvement.ref_commande }</b> du <b><fmt:formatDate value="${caisseMouvement.date_creation }" pattern="dd/MM/yyyy HH:mm:ss" /></b>
			${caisseMouvement.is_annule ? ' (<b style="color:red;">Annul&eacute;e</b>)' : '' }
			</span>
			
			<%if(IS_UPD_CMD_HISTO){ %>
				<std:link actionGroup="X" targetDiv="generic_modal_body" style="margin-top: -3px;" classStyle="btn btn-default" action="caisse.journee.init_update_journee" workId="${caisseMouvement.id}" icon="fa fa-pencil" tooltip="Modifier" />
			<%} %>
			
			<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
				<i class="fa fa-times"></i> Fermer
			</button>
		</div>

		<div class="widget-body">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			
			<std:form name="data-form">

<div class="row">
	<div class="form-group" style="margin-bottom: 0px;">
		<std:label classStyle="control-label col-md-3" value="Type" style="line-height: 5px;" />
		<div class="col-md-3"><b>
			<c:choose>
				<c:when test="${caisseMouvement.type_commande=='P' }">
					Sur place
				</c:when>
				<c:when test="${caisseMouvement.type_commande=='E' }">
					A emporter
				</c:when>
				<c:when test="${caisseMouvement.type_commande=='L' }">
					Livraison
				</c:when>
			</c:choose></b>
		</div>
		<std:label classStyle="control-label col-md-3" valueKey="caisseMouvement.mode_paiement" style="line-height: 5px;" />
		<div class="col-md-3">
			<b>${caisseMouvement.mode_paiement }</b>
		</div>
	</div>	
	<hr style="margin-top: 5px;margin-bottom: 0px;">
	<div class="form-group" style="margin-bottom: 0px;">
		<c:if test="${caisseMouvement.mtt_donne > 0 }">
			<std:label classStyle="control-label col-md-3" value="Esp&egrave;ces" style="line-height: 5px;" />
			<div class="col-md-3">
				<fmt:formatDecimal value="${caisseMouvement.mtt_donne }"/>
			</div>
		</c:if>
		<c:if test="${caisseMouvement.mtt_donne_cheque > 0 }">
			<std:label classStyle="control-label col-md-3" value="Ch&egrave;que" style="line-height: 5px;" />
			<div class="col-md-3">
				<fmt:formatDecimal value="${caisseMouvement.mtt_donne_cheque }"/>
			</div>
		</c:if>
		
		<c:if test="${caisseMouvement.mtt_donne_cb > 0 }">
			<std:label classStyle="control-label col-md-3" value="Carte" style="line-height: 5px;" />
			<div class="col-md-3">
				<fmt:formatDecimal value="${caisseMouvement.mtt_donne_cb }"/>
			</div>
		</c:if>
		<c:if test="${caisseMouvement.mtt_donne_dej > 0 }">
			<std:label classStyle="control-label col-md-3" value="Ch&egrave;que d&eacute;j." style="line-height: 5px;" />
			<div class="col-md-3">
				<fmt:formatDecimal value="${caisseMouvement.mtt_donne_dej }"/>
			</div>
		</c:if>
		<c:if test="${caisseMouvement.mtt_donne_dej > 0 }">
			<std:label classStyle="control-label col-md-3" value="Dej" style="line-height: 5px;" />
			<div class="col-md-3">
				<fmt:formatDecimal value="${caisseMouvement.mtt_donne_dej }"/>
			</div>
		</c:if>
		<c:if test="${caisseMouvement.mtt_portefeuille > 0 }">
			<std:label classStyle="control-label col-md-3" value="Portefeuille" style="line-height: 5px;" />
			<div class="col-md-3">
				<fmt:formatDecimal value="${caisseMouvement.mtt_portefeuille }"/>
			</div>
		</c:if>
		<c:if test="${caisseMouvement.mtt_donne_point > 0 }">
			<std:label classStyle="control-label col-md-3" value="Point" style="line-height: 5px;" />
			<div class="col-md-3">
				<fmt:formatDecimal value="${caisseMouvement.mtt_donne_point }"/>
			</div>
		</c:if>
		
		<c:if test="${caisseMouvement.mtt_art_offert > 0 }">
			<std:label classStyle="control-label col-md-3" value="Offert" style="line-height: 5px;" /> 
			<div class="col-md-3">
				<fmt:formatDecimal value="${caisseMouvement.mtt_art_offert }" /> 
			</div>
		</c:if>
		<c:if test="${caisseMouvement.mtt_reduction > 0 }">	
			<std:label classStyle="control-label col-md-3" value="R&eacute;duction Cmd" style="line-height: 5px;" />
			<div class="col-md-3">
				<fmt:formatDecimal value="${caisseMouvement.mtt_reduction }"/>
			</div>
		</c:if>
		<c:if test="${caisseMouvement.mtt_art_reduction > 0 }">	
			<std:label classStyle="control-label col-md-3" value="R&eacute;duction Art" style="line-height: 5px;" />
			<div class="col-md-3">
				<fmt:formatDecimal value="${caisseMouvement.mtt_art_reduction }"/>
			</div>
		</c:if>
	</div>
	<div class="form-group" style="margin-bottom:0px;">	
		<c:if test="${caisseMouvement.opc_client != null }">	
			<std:label classStyle="control-label col-md-3" valueKey="caisseMouvement.opc_client" style="line-height: 5px;" />
			<div class="col-md-6">
				${caisseMouvement.opc_client.nom } ${caisseMouvement.opc_client.prenom } ${caisseMouvement.opc_client.getAdressFull() }
			</div>
		</c:if>	
		<c:if test="${caisseMouvement.opc_employe != null }">	
			<std:label classStyle="control-label col-md-3" valueKey="caisseMouvement.opc_employe" style="line-height: 5px;" />
			<div class="col-md-6">
				${caisseMouvement.opc_employe.nom } ${caisseMouvement.opc_employe.prenom }
			</div>
		</c:if>	
		<c:if test="${caisseMouvement.mtt_annul_ligne > 0 }">
			<std:label classStyle="control-label col-md-3" value="Annulation" /> 
			<div class="col-md-3" style="color: red;">
				<fmt:formatDecimal value="${caisseMouvement.mtt_annul_ligne }" /> 
			</div>	
		</c:if>
	</div>
	<hr style="margin-top: 5px;margin-bottom: 0px;">
	<div class="form-group" style="margin-bottom: 0px;">
		<std:label classStyle="control-label col-md-3" value="Montant cmd" style="line-height: 5px;" />
		<div class="col-md-3" style="color: blue;font-weight: bold;">
			<fmt:formatDecimal value="${caisseMouvement.mtt_commande }" /> 
		</div>
		<std:label classStyle="control-label col-md-3" value="Montant net" style="line-height: 5px;" />
		<div class="col-md-3" style="font-weight: bold;font-size:16px;color: #630767;">
			<fmt:formatDecimal value="${caisseMouvement.mtt_commande_net }" /> 
		</div>	
	</div>

<%
	CaisseMouvementPersistant CURRENT_COMMANDE = (CaisseMouvementPersistant)request.getAttribute("caisseMouvement");
List<CaisseMouvementArticlePersistant> listSortedArticle = CURRENT_COMMANDE.getList_article();
if(listSortedArticle == null){
	listSortedArticle = new ArrayList();
}
IArticleService service = (IArticleService)ServiceUtil.getBusinessBean(IArticleService.class);
%>
	
	<div class="form-group" style="margin-top: 10px;">
		<div class="col-md-12" style="margin-left: 30px;">
			<table style="width: 80%;color: black;border: 1px solid #2dc3e8;" id="cmd-table" align="center">

<%
	// Total par menu
String currentTableRef = (String)ControllerUtil.getUserAttribute("CURRENT_TABLE_REF", request);
Integer NBR_NIVEAU_CAISSE = StringUtil.isNotEmpty(ContextGloabalAppli.getGlobalConfig("NBR_NIVEAU_CAISSE"))?Integer.valueOf(ContextGloabalAppli.getGlobalConfig("NBR_NIVEAU_CAISSE")):null;
Map<String, BigDecimal> mttMenuMap = new HashMap<>();
List<String> mttTableClient = new ArrayList<>();
List<String> listTables = new ArrayList<>();
Map<String, Integer> nbrCouvertTable = new HashMap<>();
List<Integer> listIdxClient = new ArrayList<>();
CaisseMouvementArticlePersistant ligneLivraison = null;

for(CaisseMouvementArticlePersistant caisseMvmP : listSortedArticle){
	/*if(BooleanUtil.isTrue(caisseMvmP.getIs_annule())){
		continue;
	}*/
	if(caisseMvmP.getIdx_client() != null && !listIdxClient.contains(caisseMvmP.getIdx_client())){
		listIdxClient.add(caisseMvmP.getIdx_client());
	}
	// Clients par table
	if(!mttTableClient.contains(caisseMvmP.getIdx_client()+"_"+caisseMvmP.getRef_table())){
		mttTableClient.add(caisseMvmP.getIdx_client()+"_"+caisseMvmP.getRef_table());
	}
	
	// Ajout tables
	 if(caisseMvmP.getRef_table() != null && !listTables.contains(caisseMvmP.getRef_table())){
		 listTables.add(caisseMvmP.getRef_table());
	 }
	 // Nombre de couverts
	 if(caisseMvmP.getNbr_couvert() != null){
		nbrCouvertTable.put(caisseMvmP.getRef_table(), caisseMvmP.getNbr_couvert());
	 }
	 if(caisseMvmP.getMenu_idx() == null){
	     continue;
	 }
	 mttMenuMap.put(caisseMvmP.getMenu_idx(),  BigDecimalUtil.add( mttMenuMap.get(caisseMvmP.getMenu_idx()), caisseMvmP.getMtt_total()));
}
	// Trier les tables
	Collections.sort(listTables);
	Collections.sort(listIdxClient);

	if(listIdxClient != null && listIdxClient.size()>0 && listIdxClient.get(listIdxClient.size()-1) > CURRENT_COMMANDE.getMax_idx_client()){
		CURRENT_COMMANDE.setMax_idx_client(listIdxClient.get(listIdxClient.size()-1));
	}
	
	if(currentTableRef == null){
		if(listTables.size() == 0){
	listTables.add("XX");
		}
	} else if(!listTables.contains(currentTableRef)){
		listTables.add(currentTableRef);
	}

	Integer idxArticle = 0;
	int nbrNiveau = 0;
	BigDecimal sousTotal = null;
	BigDecimal sousTotalTable = null;

	// Tables -------------
	int idxTable = 0;
	for(String refTable : listTables){
		idxTable++;
		boolean isTablePassed = true;
		
		// Clients
		for(int i=1; i<=CURRENT_COMMANDE.getMax_idx_client(); i++){
	if(!listIdxClient.contains(i)){
		continue;
	}

	// Si client de la table
	if(!"XX".equals(refTable) 
			&& !refTable.equals(currentTableRef) 
			&& !mttTableClient.contains(i+"_"+refTable)
		){
		continue;
	}
	
	boolean isFamillePassed = false;
	
	if(listIdxClient.size() > 1){
		    	   if(i != listIdxClient.get(0)){ %>
		    		   <tr style="color: #d73d32;font-weight: normal;background-color: #eeeeee;" class="client-root-style">
			       			<td colspan="2" align="right" style="font-weight: bold;">SOUS TOTAL CLIENT</td>
			       			<td align="right" style="font-weight: bold;"><%=BigDecimalUtil.formatNumber(sousTotal) %></td>
			      	   </tr>
		    	 	<%}
		    	 
		    	 if(isTablePassed && !"XX".equals(refTable)){
			    	   if(idxTable != 1){%>
			    		   <tr style="color: black;font-weight: normal;background-color: #ccc;" class="client-root-style">
				       			<td colspan="2" align="right" style="font-weight: bold;">SOUS TOTAL TABLE</td>
				       			<td align="right" style="font-weight: bold;"><%=BigDecimalUtil.formatNumber(sousTotalTable) %></td>
				      	   </tr>
			    	 <%
			    	 sousTotalTable = null;
			    	   }
			      }
			  }%>
			  
			  <%
// 			  if(listIdxClient.size() > 1 || ){ 
			 	 if(isTablePassed && !"XX".equals(refTable)){ %>
		    	 <tr style="color: black;background-color: #262626;" class="client-root-style">
		       		<td colspan="3" style="border-radius: 15px;font-weight: bold;color: #fbfbfb;" align="center">
		       			<%=(currentTableRef!=null && currentTableRef.equals(refTable) ? "<i style='color:#e3f2fd;' class='fa fa-arrow-circle-right'></i>":"") %> <%=refTable %> <%=nbrCouvertTable.get(refTable)!=null?" ("+nbrCouvertTable.get(refTable)+" couverts)":"" %>
		       			&nbsp;<input style="display: none;" type="checkbox" name="checktab_" id="checktab_" value="<%=refTable%>">
		       		</td>
		       </tr>
		    	 <%//}
		    	} %> 
		    	
		    	<%if(listIdxClient.size() > 1){  %>
		    	   <tr style="color: white;background-color: #000000;height: 30px;" class="client-root-style">
			       		<td colspan="3" style="border-radius: 15px;padding-left: 10px;"> 
			       			<i class="fa fa-street-view" style="color: #a0d468"></i>
			       			CLIENT <%=i %>
			       			&nbsp;<input style="display: none;" type="checkbox" name="checkcli_" id="checkcli_" value="<%=i%>">
			       			
			       			<span>
			       				<std:link action="caisse-web.caisseWeb.print" targetDiv="div_gen_printer" params='<%="mvm="+CURRENT_COMMANDE.getId()+"&cli="+i %>' classStyle="btn btn-xs btn-yellow shiny" icon="fa-print" style="left:60%;" />
			       			</span>
			       		</td>
			       </tr>
		      <%}
		   	 
		    sousTotal = null;
		    isTablePassed = false;
		    //
			for(CaisseMouvementArticlePersistant caisseMvmP : listSortedArticle){
		       if(/*BooleanUtil.isTrue(caisseMvmP.getIs_annule()) ||*/ caisseMvmP.getIdx_client()==null || caisseMvmP.getIdx_client()!=i || (caisseMvmP.getRef_table()!=null && !caisseMvmP.getRef_table().equals(refTable))){
		           continue;
		       }
		       if("LIVRAISON".equals(caisseMvmP.getType_ligne())){
					ligneLivraison = caisseMvmP;
					continue;
				}
		       
		      sousTotal = BigDecimalUtil.add(sousTotal, (BooleanUtil.isTrue(caisseMvmP.getIs_offert()) ? null:caisseMvmP.getMtt_total()));
		      sousTotalTable = BigDecimalUtil.add(sousTotalTable, (BooleanUtil.isTrue(caisseMvmP.getIs_offert()) ? null:caisseMvmP.getMtt_total()));
		       
		       // Ajout du num�ro dans le tableau
		       String type = caisseMvmP.getType_ligne();
		       String libCmd = caisseMvmP.getLibelle();
		       if(type == null){
		           type = "XXX";
		       }
		       
		       if(type.equals(TYPE_LIGNE_COMMANDE.MENU.toString()) && (caisseMvmP.getLevel() == null || caisseMvmP.getLevel() > 1)) {
		           idxArticle++;
		           libCmd = idxArticle + " - " + libCmd;
		       } else if(type.equals(TYPE_LIGNE_COMMANDE.ART.toString())){
		           idxArticle++;
		           libCmd = idxArticle + " - " + libCmd;
		       }
		       
		       if(StringUtil.isNotEmpty(caisseMvmP.getCommentaire())){
		           libCmd = libCmd + " <i class='fa fa-comments-o' style='color:orange;font-size: 17px;cursor:pointer;' data-container='body' data-titleclass='bordered-blue' data-class='' data-toggle='popover' data-placement='top' data-title='Commentaire' data-content='"+caisseMvmP.getCommentaire()+"' data-original-title='' title=''></i>";
		       }
		       
		       String qte = RazService.getQteFormatted(caisseMvmP.getQuantite());
      	       
		       String mttTotal = "";
		       if( BooleanUtil.isTrue(caisseMvmP.getIs_offert())){
		    	   mttTotal = "<i class='fa fa-gift'style='color:green;font-size: 17px;cursor:pointer;' data-container='body' data-titleclass='bordered-blue' data-class='' data-toggle='popover' data-placement='top' data-title='Article offert' data-content='Prix : "+BigDecimalUtil.formatNumber(caisseMvmP.getMtt_total())+"' data-original-title='' title=''></i>";
		       } else if(!BigDecimalUtil.isZero(caisseMvmP.getMtt_total())){
		    	   mttTotal = BigDecimalUtil.formatNumber(caisseMvmP.getMtt_total());
		       }
		       
		       String styleTd = "";
		       String classType = "";
		       String height = "35px";
		       boolean isRootMenu = false;
		       boolean isMenu = false;
		      if(caisseMvmP.getMenu_idx() != null || type.equals(TYPE_LIGNE_COMMANDE.MENU.toString())){
		    	  if(type.equals(TYPE_LIGNE_COMMANDE.MENU.toString())) {
		    		  if(caisseMvmP.getLevel() != null && caisseMvmP.getLevel() == 1){
		    			  isRootMenu = true;
		    			  classType = "menu-cat-style cat-cmd-detail";
		    		  } else{
		    			  if(BooleanUtil.isTrue(caisseMvmP.getIs_menu())){
		    			 	 isMenu = true;
		    			  }
		        	   	  classType = "menu-style cat-cmd-detail";
		    		  }
		           } else if(type.equals(TYPE_LIGNE_COMMANDE.ART_MENU.toString())) {
		        	   classType = "ligne-style";
		        	   height = "23px";
		        	   nbrNiveau = 0;
		           } else if(type.equals(TYPE_LIGNE_COMMANDE.GROUPE_MENU.toString())){
		        	   classType = "group-style-fam";//"group-style cat-cmd-detail";
		        	   height = "5px";
		        	   nbrNiveau++;
		           } else if(type.equals(TYPE_LIGNE_COMMANDE.GROUPE_FAMILLE.toString())){
		        	   classType = "group-style-fam";//"group-style cat-cmd-detail";
		        	   height = "5px";
		        	   nbrNiveau++;
		           }
		       } else{//---------------------------------------------------------
		    	   if(type.equals(TYPE_LIGNE_COMMANDE.ART.toString())){
		    		   classType = "ligne-fam-style";
		    		   height = "23px";
		    		   nbrNiveau = 0;
		           } else if(type.equals(TYPE_LIGNE_COMMANDE.GROUPE_FAMILLE.toString())){
		        	   // Ajouter une ligne e s�paration
		        	   if(!isFamillePassed){
		        		   %>
		        	       <tr style="height:3px;color: black;background-color:#f35318;" class="menu-root-style">
		        	       		<td colspan="3" align="center"></td>
		        	       </tr>
		        	       <%
		        	       isFamillePassed = true;
		        	   }
		        	   classType = "group-style-fam";//"famille-style cat-cmd-famille";
		        	   height = "5px";
		        	   nbrNiveau++;
		           } 
		       }
		       
		       boolean isArticle = (type.equals(TYPE_LIGNE_COMMANDE.ART.toString()) || type.equals(TYPE_LIGNE_COMMANDE.ART_MENU.toString()));
		       boolean isToAdd = (isArticle || NBR_NIVEAU_CAISSE == null || nbrNiveau <= NBR_NIVEAU_CAISSE) && !caisseMvmP.getLibelle().startsWith("#");
		       if(caisseMvmP.getLevel() != null && caisseMvmP.getLevel() > 1){
		    	   styleTd = styleTd + "padding-left:"+(7*caisseMvmP.getLevel())+"px;";
		       } else if(isArticle){
		    	   styleTd = styleTd + "padding-left:30px;";
		       }
		      
		       if(isToAdd){
		    	   boolean isNotArt = (!type.equals(TYPE_LIGNE_COMMANDE.ART.toString()) && !type.equals(TYPE_LIGNE_COMMANDE.ART_MENU.toString()));
		       
		       		boolean isToClolapse = (caisseMvmP.getMenu_idx()!= null 
		       										&& !type.equals(TYPE_LIGNE_COMMANDE.MENU.toString()));
		       %>
		       
		       <%if(isRootMenu){ %>
		       	<tr id="<%=isToClolapse ? "tr_"+caisseMvmP.getMenu_idx():"" %>" class="<%=classType%>" style="color:#ebbd08;background-color: #fff8c6;font-size: 19px;height: <%=height %>;<%=BooleanUtil.isTrue(caisseMvmP.getIs_annule())?"text-decoration: line-through;color: red;":""%>" >
		       		<td colspan="3" style="<%=styleTd %>;<%="5px".equals(height)?"":"line-height:"+height %>;">
		       			<%=libCmd %>
		       		</td>
		       	</tr>
		       <%} else{ 
		       String mttTotalMenu = BigDecimalUtil.formatNumber(mttMenuMap.get(caisseMvmP.getMenu_idx()));
		       %>
		         <tr id="<%=isToClolapse ? "tr_"+caisseMvmP.getMenu_idx():"" %>" class="<%=classType%> <%=isMenu?(" mnu_"+caisseMvmP.getMenu_idx()):"" %>" style="height: <%=height %>;<%=isToClolapse?"display:none;":"" %> <%=isArticle?"font-weight:bold;":""%><%=BooleanUtil.isTrue(caisseMvmP.getIs_annule())?"text-decoration: line-through;color: red;":""%>">
		       		<td style="<%=styleTd %>;<%="5px".equals(height)?"":"line-height:"+height %>;">
		       			<%
		       			if(type.equals(TYPE_LIGNE_COMMANDE.MENU.toString()) && caisseMvmP.getLevel()>1 ){
		       				%>
		       				<a class="btn btn-default btn-xs icon-only white lnk_mnu" style="padding: 4px 1px;" href="javascript:void(0);" mnu="<%=caisseMvmP.getMenu_idx() %>">
		       					<i class="fa fa-plus" style="font-size: 16px;color: #03A9F4;"></i>
			       			</a>
			       			<%
		       				Map<String, byte[]> dataimg = service.getDataImage(caisseMvmP.getElementId(), "menu");
		       				String stl = "";
		       				if(dataimg.size() > 0){
		       					stl = "width: 33px;height:33px;float: right;background-size: 33px 33px !important;background: url(data:image/jpeg;base64,"+FileUtil.getByte64(dataimg.entrySet().iterator().next().getValue())+") no-repeat;";
		       					%>
		       					<span style="<%=stl%>"></span>
		       					<%
		       				}
		       			} else if(isArticle){
		       				Map<String, byte[]> dataimg = service.getDataImage(caisseMvmP.getElementId(), "article");
		       				String stl = "";
		       				if(dataimg.size() > 0){
		       					stl = "width: 23px;height:23px;float: right;background-size: 23px 23px !important;background: url(data:image/jpeg;base64,"+FileUtil.getByte64(dataimg.entrySet().iterator().next().getValue())+") no-repeat;";
		       					%>
		       					<span style="<%=stl%>"></span>
		       					<%
		       				}
		       			}
		       			%>
		       		
		       			<%=(!isNotArt ? "<span class='fa fa-circle' style='color:#4caf50;'></span>&nbsp;":"")+libCmd %>
		       		</td>
		       		<td align="right">
		       			<%if(BooleanUtil.isTrue(caisseMvmP.getIs_encaisse())){ %>
		       					<i class="fa fa-check-circle-o" style="font-size: 17px;color: #53a93f;"></i>
		       			<%} %>
		       			<%=isNotArt ? "": qte %>
		       		</td>
		       		<td align="right" style="font-weight: bold;">
		       			<%if(caisseMvmP.getMenu_idx() == null){ %>
		       				<%=mttTotal %>
		       			<%} else{ 
		       			%>
		       				<span class="span_mtt_art" style='display:<%=isToClolapse ? "":"none"%>;'><%=mttTotal %></span>
		       				
		       			<%
		       			if(BooleanUtil.isTrue(caisseMvmP.getIs_offert())){
		       				mttTotalMenu = "<i class='fa fa-gift'style='color:green;font-size: 17px;cursor:pointer;' data-container='body' data-titleclass='bordered-blue' data-class='' data-toggle='popover' data-placement='top' data-title='Article offert' data-content='Prix : "+mttTotalMenu+"' data-original-title='' title=''></i>";
					       }
		       			%>	
		       				<span class="span_mtt_mnu" style='display:<%=isToClolapse ? "none":""%>;'><%=isMenu ? mttTotalMenu : "" %></span>
		       			<%} %>
		       		</td>
		       </tr>
		       <%
		         }
		       }
			}
	   }
	}
	
	if(listIdxClient.size() > 1){%>
 		 <tr style="color: #d73d32;background-color: #eeeeee;font-weight: normal;" class="menu-root-style">
	     	<td colspan="2" align="right" style="font-weight: bold;">SOUS TOTAL CLIENT</td>
	     	<td align="right" style="font-weight: bold;"><%=BigDecimalUtil.formatNumber(sousTotal) %></td>
	     </tr>
   <%
	}
	
	if(listTables.size()>0 && !listTables.get(0).equals("XX")){%>
	 <tr style="color: black;background-color: #ccc;font-weight: normal;" class="menu-root-style">
    	<td colspan="2" align="right" style="font-weight: bold;">SOUS TOTAL TABLE</td>
    	<td align="right" style="font-weight: bold;"><%=BigDecimalUtil.formatNumber(sousTotalTable) %></td>
    </tr>
	<%
	}
	
    // Ligne livraison
    if(ligneLivraison != null) {%>
    	<tr style="color: blue;" class="ligne-style">
       		<td><%="**"+ligneLivraison.getLibelle() %></td>
       		<td></td>
       		<td align="right"><%=BigDecimalUtil.formatNumber(ligneLivraison.getMtt_total()) %></td>
       </tr>
    <%}
    
    List<CaisseMouvementOffrePersistant> listOffres = RazService.getListOffre(CURRENT_COMMANDE);
   	
   // Les offres
   if(listOffres != null && listOffres.size() > 0){
	   %>
       <tr style="color: green;" class="menu-root-style">
       		<td colspan="3" align="center">Offres</td>
       </tr>
       <%
	   
	   for (CaisseMouvementOffrePersistant offreDet : listOffres) {
	       if(offreDet.getIs_annule() != null && offreDet.getIs_annule()){
	           continue;
	       }
	       Long idOffre = offreDet.getOpc_offre().getId();
	       String libelleOffre = offreDet.getOpc_offre().getLibelle();
	       
      		String params = "cd="+idOffre+"&elm="+offreDet.getOpc_offre().getId()+"&tp=OFFRE";
	       %>
	       <tr style="color: red;" class="ligne-style">
	       		<td><%="**"+libelleOffre %></td>
	       		<td></td>
	       		<td align="right">-<%=BigDecimalUtil.formatNumber(offreDet.getMtt_reduction()) %></td>
	       </tr>
	       <%
	   }
   }
%>     
</table>  
			
		</div>	
	</div>
</div>
</std:form>
</div>
</div>