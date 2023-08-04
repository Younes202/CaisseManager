<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementOffrePersistant"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementPersistant"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="java.util.Collections"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="appli.model.domaine.stock.service.IArticleService"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli.TYPE_LIGNE_COMMANDE"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<link href="resources/caisse/css/caisse.css?v=<%=StrimUtil.getGlobalConfigPropertie("version.resources")%>15" rel="stylesheet" />

<style>
  #div_glob .control-label{
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

<%
	CaisseMouvementPersistant CURRENT_COMMANDE = (CaisseMouvementPersistant)request.getAttribute("caisseMouvement");
List<CaisseMouvementArticlePersistant> listSortedArticle = CURRENT_COMMANDE.getList_article();
IArticleService service = (IArticleService)ServiceUtil.getBusinessBean(IArticleService.class);
%>

	<!-- widget grid -->
	<div class="widget" id="div_glob">
		<div class="widget-header bordered-bottom bordered-blue" style="padding-bottom: 5px; padding-top: 5px; padding-right: 5px;">
			<span class="widget-caption">Vente : <b>${caisseMouvement.ref_commande }</b> du <b><fmt:formatDate value="${caisseMouvement.date_creation }" pattern="dd/MM/yyyy HH:mm:ss" /></b>
			${caisseMouvement.is_annule ? ' (<b style="color:red;">Annul&eacute;e</b>)' : '' }
			</span>
			<%
				if(!ContextAppli.APPLI_ENV.cais.toString().equals(ControllerUtil.getUserAttribute("CURRENT_ENV", request))){
			%>
			<std:link actionGroup="X" targetDiv="generic_modal_body" style="margin-top: -3px;" classStyle="btn btn-default" action="caisse.journee.init_update_journee" workId="${caisseMouvement.id}" icon="fa fa-pencil" tooltip="Cr&eacute;er" />
			<%
				}
			%>
			<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal" style="margin-top: -3px;">
				<i class="fa fa-times"></i> Fermer
			</button>
		</div>

		<div class="widget-body">
			<std:form name="data-form">
				<div class="row">
					<div class="form-group">
						<std:label classStyle="control-label col-md-3" value="Date encaissement" />
						<div class="col-md-9">
							<span class="widget-caption">:<b><fmt:formatDate value="${caisseMouvement.date_encais }" pattern="dd/MM/yyyy HH:mm:ss" /></b></span>
						</div>
						
						<c:if test="${caisseMouvement.opc_user_confirm != null }">
							<std:label classStyle="control-label col-md-3" value="Confirmé par" />
							<div class="col-md-9">
								<span style="color: fuchsia;">${caisseMouvement.opc_user_confirm.login }</span>
							</div>
						</c:if>
					</div>	
					<div class="form-group" style="margin-bottom: 0px;">
						<std:label classStyle="control-label col-md-3" value="Type" />
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
						<std:label classStyle="control-label col-md-3" valueKey="caisseMouvement.mode_paiement" />
						<div class="col-md-3">
							<b>${caisseMouvement.mode_paiement }</b>
						</div>
					</div>	
					<hr style="margin-top: 5px;margin-bottom: 0px;">
					<div class="form-group" style="margin-bottom: 0px;">
						<c:if test="${caisseMouvement.mtt_donne > 0 }">
							<std:label classStyle="control-label col-md-3" value="Esp&egrave;ces" />
							<div class="col-md-3">
								<fmt:formatDecimal value="${caisseMouvement.mtt_donne }"/>
							</div>
						</c:if>
						<c:if test="${caisseMouvement.mtt_donne_cheque > 0 }">	
							<std:label classStyle="control-label col-md-3" value="Ch&egrave;que" />
							<div class="col-md-3">
								<fmt:formatDecimal value="${caisseMouvement.mtt_donne_cheque }"/>
							</div>
						</c:if>	
						<c:if test="${caisseMouvement.mtt_donne_cb > 0 }">
							<std:label classStyle="control-label col-md-3" value="Carte" />
							<div class="col-md-3">
								<fmt:formatDecimal value="${caisseMouvement.mtt_donne_cb }"/>
							</div>
						</c:if>
						<c:if test="${caisseMouvement.mtt_donne_dej > 0 }">	
							<std:label classStyle="control-label col-md-3" value="Ch&egrave;que d&eacute;j." />
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
							<std:label classStyle="control-label col-md-3" value="Offert" /> 
							<div class="col-md-3">
								<fmt:formatDecimal value="${caisseMouvement.mtt_art_offert }" /> 
							</div>	
						</c:if>
						<c:if test="${caisseMouvement.mtt_reduction > 0 }">	
							<std:label classStyle="control-label col-md-3" value="R&eacute;duction Cmd" />
							<div class="col-md-3">
								<fmt:formatDecimal value="${caisseMouvement.mtt_reduction }"/>
							</div>
						</c:if>	
						<c:if test="${caisseMouvement.mtt_art_reduction > 0 }">	
							<std:label classStyle="control-label col-md-3" value="R&eacute;duction Art" />
							<div class="col-md-3">
								<fmt:formatDecimal value="${caisseMouvement.mtt_art_reduction }"/>
							</div>
						</c:if>	
					</div>
					<c:if test="${caisseMouvement.mtt_annul_ligne > 0 }">
						<std:label classStyle="control-label col-md-3" value="Annulation" /> 
						<div class="col-md-3" style="color: red;">
							<fmt:formatDecimal value="${caisseMouvement.mtt_annul_ligne }" /> 
						</div>	
					</c:if>
					<c:if test="${caisseMouvement.opc_client != null }">	
						<div class="form-group" style="margin-bottom:0px;">	
							<std:label classStyle="control-label col-md-3" valueKey="caisseMouvement.opc_client" />
							<div class="col-md-9" style="color: blue;padding-top: 6px;">
								${caisseMouvement.opc_client.nom } ${caisseMouvement.opc_client.prenom } ${caisseMouvement.opc_client.getAdressFull() }
							</div>
						</div>	
					</c:if>	
					<c:if test="${caisseMouvement.opc_employe != null }">
						<div class="form-group" style="margin-bottom:0px;">		
							<std:label classStyle="control-label col-md-3" valueKey="caisseMouvement.opc_employe" />
							<div class="col-md-9" style="color: blue;padding-top: 6px;">
								${caisseMouvement.opc_employe.nom } ${caisseMouvement.opc_employe.prenom }
							</div>
						</div>	
					</c:if>	
					<c:if test="${caisseMouvement.opc_livreurU != null }">
						<div class="form-group" style="margin-bottom:0px;">		
							<std:label classStyle="control-label col-md-3" value="Livreur" />
							<div class="col-md-9" style="color: blue;padding-top: 6px;">
								${caisseMouvement.opc_livreurU.login }
							</div>
						</div>	
					</c:if>	
					</div>
					<hr style="margin-top: 5px;margin-bottom: 0px;">
					<div class="form-group" style="margin-bottom: 0px;">
						<std:label classStyle="control-label col-md-3" value="Montant cmd" />
						<div class="col-md-3" style="color: blue;font-weight: bold;">
							<fmt:formatDecimal value="${caisseMouvement.mtt_commande }" /> 
						</div>
						<std:label classStyle="control-label col-md-3" value="Montant net" />
						<div class="col-md-3" style="font-weight: bold;font-size:16px;color: #630767;">
							<fmt:formatDecimal value="${caisseMouvement.mtt_commande_net }" /> 
						</div>	
					</div>
	
					
					<div class="form-group" style="margin-top: 10px;margin-left: 0px;">
						<div class="col-md-12" style="margin-left: -2px;max-height: 400px;overflow-y: auto;overflow-x:hidden;">
							<table style="width: 98%;color: black;border: 1px solid #2dc3e8;" id="cmd-tablexxx" align="center">

<%
	// Total par menu
String currentTableRef = (String)ControllerUtil.getUserAttribute("CURRENT_TABLE_REF", request);
Integer NBR_NIVEAU_CAISSE = StringUtil.isNotEmpty(ContextGloabalAppli.getGlobalConfig("NBR_NIVEAU_CAISSE"))?Integer.valueOf(ContextGloabalAppli.getGlobalConfig("NBR_NIVEAU_CAISSE")):null;
Map<String, BigDecimal> mttMenuMap = new HashMap<>();
List<String> mttTableClient = new ArrayList<>();
List<String> listTables = new ArrayList<>();
List<Integer> listIdxClient = new ArrayList<>();
Map<String, Integer> nbrCouvertTable = new HashMap<>();

for(CaisseMouvementArticlePersistant caisseMvmP : listSortedArticle){
	if(!listIdxClient.contains(caisseMvmP.getIdx_client()) && caisseMvmP.getIdx_client() != null){
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
		    	   if(i != listIdxClient.get(0)){
%>
		    		   <tr style="color: #2196f3;font-weight: normal;background-color: #eeeeee;" class="client-root-style">
			       			<td colspan="2" align="right" style="font-weight: bold;">SOUS TOTAL CLIENT</td>
			       			<td align="right" style="font-weight: bold;"><%=BigDecimalUtil.formatNumber(sousTotal) %></td>
			       			<td></td>
			      	   </tr>
		    	 <%}
		    	 
		    	 if(isTablePassed && !"XX".equals(refTable)){
			    	   if(idxTable != 1){%>
			    		   <tr style="color: black;font-weight: normal;background-color: #ccc;" class="client-root-style">
				       			<td colspan="2" align="right" style="font-weight: bold;">SOUS TOTAL TABLE</td>
				       			<td align="right" style="font-weight: bold;"><%=BigDecimalUtil.formatNumber(sousTotalTable) %></td>
				       			<td></td>
				      	   </tr>
			    	 <%
			    	 sousTotalTable = null;
			    	   }
			      }
			  }%>
			  
			  <%
			 	 if(isTablePassed && !"XX".equals(refTable)){ %>
		    	 <tr style="color: black;background-color: #262626;" class="client-root-style">
		       		<td colspan="3" style="border-radius: 15px;font-weight: bold;color: #fbfbfb;" align="center">
		       			<%=(currentTableRef!=null && currentTableRef.equals(refTable) ? "<i style='color:#e3f2fd;' class='fa fa-arrow-circle-right'></i>":"") %> <%=refTable %> <%=nbrCouvertTable.get(refTable)!=null?" ("+nbrCouvertTable.get(refTable)+" couverts)":"" %>
		       			&nbsp;<input style="display: none;" type="checkbox" name="checktab_" id="checktab_" value="<%=refTable%>">
		       		</td>
		       </tr>
		    	 <%
		    	} %> 
		    	
		    	<%if(listIdxClient.size() > 1){  %>
		    	   <tr style="color: black;background-color: #d9d9d9;height: 32px;" class="client-root-style">
			       		<td colspan="3" style="border-radius: 15px;padding-left: 10px;"> 
			       			<i class="fa fa-street-view" style="color: #a0d468"></i>
			       			CLIENT <%=i %>
			       			&nbsp;<input style="display: none;" type="checkbox" name="checkcli_" id="checkcli_" value="<%=i%>">
			       			
			       			<span>
			       				<std:link action="caisse-web.caisseWeb.print" targetDiv="div_gen_printer" params='<%="mvm="+CURRENT_COMMANDE.getId()+"&cli="+i %>' classStyle="btn btn-xs btn-yellow shiny" icon="fa-print" style="position: absolute;right: 23px;" />
			       			</span>
			       		</td>
			       </tr>
		      <%}
		   	 
		    sousTotal = null;
		    isTablePassed = false;
		    //
			for(CaisseMouvementArticlePersistant caisseMvmP : listSortedArticle){
		       if((caisseMvmP.getIdx_client()!=null && caisseMvmP.getIdx_client()!=i) 
		    		   || (caisseMvmP.getRef_table()!=null && !caisseMvmP.getRef_table().equals(refTable))){
		           continue;
		       }
		       
		       if(!BooleanUtil.isTrue(caisseMvmP.getIs_annule()) && !BooleanUtil.isTrue(caisseMvmP.getIs_offert())){
		      		sousTotal = BigDecimalUtil.add(sousTotal, caisseMvmP.getMtt_total());
		      		sousTotalTable = BigDecimalUtil.add(sousTotalTable, caisseMvmP.getMtt_total());
		       }
		       
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
		       
		       Integer qte = (caisseMvmP.getQuantite() != null ? caisseMvmP.getQuantite().intValue() : null);
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
		       String params = (BooleanUtil.isTrue(caisseMvmP.getIs_offert())?"isaf=1&":"") +"cli="+i+"&cd="+caisseMvmP.getCode()+"&elm="+caisseMvmP.getElementId()+"&par="+StringUtil.getValueOrEmpty(caisseMvmP.getParent_code())+"&mnu="+caisseMvmP.getMenu_idx()+"&tp="+caisseMvmP.getType_ligne();
		       
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
		       	<tr id="<%=isToClolapse ? "tr_"+caisseMvmP.getMenu_idx():"" %>" class="<%=classType%>" style="color:#ebbd08;background-color: #fff8c6;font-size: 19px;height: <%=height %>;" par="<%="isroot=1&"+params%>">
		       		<td colspan="4" style="<%=styleTd %>;<%="5px".equals(height)?"":"line-height:"+height %>;<%=BooleanUtil.isTrue(caisseMvmP.getIs_annule())?"text-decoration: line-through;color: red;":""%>">
		       			<%=libCmd %>
		       			<%if(BooleanUtil.isTrue(caisseMvmP.getIs_annule()) && caisseMvmP.getDate_annul() != null){ %>
		       				<span style="color: black;">(<%=(caisseMvmP.getOpc_user_annul()!=null?caisseMvmP.getOpc_user_annul().getLogin():"")%> - <%=DateUtil.dateTimeToString(caisseMvmP.getDate_annul()) %>)</span>
		       			<%} %>
		       		</td>
		       	</tr>
		       <%} else{ 
		       String mttTotalMenu = BigDecimalUtil.formatNumber(mttMenuMap.get(caisseMvmP.getMenu_idx()));
		       boolean isMtt = (!BigDecimalUtil.isZero(caisseMvmP.getMtt_total()) && !BooleanUtil.isTrue(caisseMvmP.getIs_offert()));
		       %>
		         <tr id="<%=isToClolapse ? "tr_"+caisseMvmP.getMenu_idx():"" %>" class="<%=classType%> <%=isMenu?(" mnu_"+caisseMvmP.getMenu_idx()):"" %>" style="height: <%=height %>;<%=isToClolapse?"display:none;":"" %> <%=isArticle?"font-weight:bold;":""%>" par="<%=(isMtt?"isart=1&":"")+params%>">
		       		<td style="<%=styleTd %>;<%="5px".equals(height)?"":"line-height:"+height %>;<%=BooleanUtil.isTrue(caisseMvmP.getIs_annule())?"text-decoration: line-through;color: red;":""%>">
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
		       			<%if(BooleanUtil.isTrue(caisseMvmP.getIs_annule()) && caisseMvmP.getDate_annul() != null){ %>
		       				<span style="color: black;">(<%=(caisseMvmP.getOpc_user_annul()!=null?caisseMvmP.getOpc_user_annul().getLogin():"")%> - <%=DateUtil.dateTimeToString(caisseMvmP.getDate_annul()) %>)</span>
		       			<%} %>
		       			
		       		</td>
		       		<td align="right">
		       			<%if(BooleanUtil.isTrue(caisseMvmP.getIs_encaisse())){ %>
		       					<i class="fa fa-check-circle-o" style="font-size: 17px;color: #53a93f;"></i>
		       			<%} %>
		       			<%=isNotArt ? "":qte %>
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
 		 <tr style="color: #2196f3;background-color: #eeeeee;font-weight: normal;" class="menu-root-style">
	     	<td colspan="2" align="right" style="font-weight: bold;">SOUS TOTAL CLIENT</td>
	     	<td align="right" style="font-weight: bold;"><%=BigDecimalUtil.formatNumber(sousTotal) %></td>
	     	<td></td>
	     </tr>
   <%
	}
	
	if(listTables.size()>0 && !listTables.get(0).equals("XX")){%>
	 <tr style="color: black;background-color: #ccc;font-weight: normal;" class="menu-root-style">
    	<td colspan="2" align="right" style="font-weight: bold;">SOUS TOTAL TABLE</td>
    	<td align="right" style="font-weight: bold;"><%=BigDecimalUtil.formatNumber(sousTotalTable) %></td>
    	<td></td>
    </tr>
	<%
	}
	
   // Les offres
   if(CURRENT_COMMANDE.getList_offre() != null && CURRENT_COMMANDE.getList_offre().size() > 0){
	   %>
       <tr style="color: green;" class="menu-root-style">
       		<td colspan="4" align="center">Offres</td>
       </tr>
       <%
	   
	   for (CaisseMouvementOffrePersistant offreDet : CURRENT_COMMANDE.getList_offre()) {
	       if(offreDet.getIs_annule() != null && offreDet.getIs_annule()){
	           continue;
	       }
	       Long idOffre = offreDet.getOpc_offre().getId();
	       String libelleOffre = offreDet.getOpc_offre().getLibelle();
	       
      		String params = "cd="+idOffre+"&elm="+offreDet.getOpc_offre().getId()+"&tp=OFFRE";
	       %>
	       <tr style="color: red;" class="ligne-style" par="<%=params%>">
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
			</std:form>
		</div>
	</div>
