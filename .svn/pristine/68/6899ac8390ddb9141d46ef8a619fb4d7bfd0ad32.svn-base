<%@page import="appli.model.domaine.util_srv.raz.RazService"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli.TYPE_LIGNE_COMMANDE"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementPersistant"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant"%>

<style>
#generic_modal_body{
	width: 930px;
	margin-left: -25%;
}
</style>

<%
	CaisseMouvementPersistant mvm = (CaisseMouvementPersistant) request.getAttribute("currMvm");
Integer NBR_NIVEAU_CAISSE = StringUtil.isNotEmpty(ContextGloabalAppli.getGlobalConfig("NBR_NIVEAU_CAISSE"))?Integer.valueOf(ContextGloabalAppli.getGlobalConfig("NBR_NIVEAU_CAISSE")):null;
String typeCmd = null;
 if(ContextAppli.TYPE_COMMANDE.E.toString().equals(mvm.getType_commande())){
     typeCmd = "A emporter";
 } else if(ContextAppli.TYPE_COMMANDE.P.toString().equals(mvm.getType_commande())){
      typeCmd = "Sur place";
 } else{
     typeCmd = "Livraison";
 }
%> 
       
 <!-- widget grid -->
	<div class="widget">
		<%
		String refTables = mvm.getRefTablesDetail();
		%>
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption" style="font-size: 18px;"><%=(mvm.getRef_commande().length()>12?mvm.getRef_commande().substring(12):mvm.getRef_commande()) %></span> | 
			<span><%=StringUtil.isNotEmpty(mvm.getNum_token_cmd()) ?"<i class='fa fa-tag'></i> "+mvm.getNum_token_cmd() : "" %></span> | 
	        <span><%=StringUtil.isNotEmpty(refTables) ? "<i class='fa fa-cutlery'></i> "+refTables : "" %></span> | 
	        <span><%=typeCmd %></span>
	                	
			<button type="button" id="close_modal" class="btn btn-primary" data-dismiss="modal">
				<i class="fa fa-times"></i> Fermer
			</button>
		</div>
		<div class="widget-body" style="overflow: hidden;">
			<div class="row">
	             <div style="width: 100%;margin-left: 12px;"> 
	             <% 
	             Integer idxArticle = 0;
				 int nbrNiveau = 0;
				 Map<Integer, Map<String, List<CaisseMouvementArticlePersistant>>> mapClientMvm = (Map<Integer, Map<String, List<CaisseMouvementArticlePersistant>>>)request.getAttribute("mapClientMvm");
				 Map<Integer, List<CaisseMouvementArticlePersistant>> mapClientMvmHS = (Map<Integer, List<CaisseMouvementArticlePersistant>>)request.getAttribute("mapHorsMenu");
				
				 Integer maxClient = mvm.getMax_idx_client();
				 
				 //
	             for(int i=1; i<maxClient+1; i++) {
	             %>
	             <div style="width: 300px;float: left;">
	             		<%if(mapClientMvm.size() > 1){ %>
	             		
			            	 <div style="width: 300px;color: black;background-color: #f44336;border-radius: 15px;text-align: center;width: 300px;" class="client-root-style">
						       		CLIENT <%=i %>
						      </div>
						 <%} %>     
						      <%if(mapClientMvm.size() > 1){ %>
						      <div style="width: 300px;">
		    		<%}
		    		Map<String, List<CaisseMouvementArticlePersistant>> mapDet = mapClientMvm.get(i);
		    		List<CaisseMouvementArticlePersistant> listDetHs = mapClientMvmHS.get(i);
		    		int idx = 0;
		    		for(String mnuIdx : mapDet.keySet()) {
		    			boolean isFamillePassed = false;  
		    			List<CaisseMouvementArticlePersistant> listDetail = mapDet.get(mnuIdx);
		    			for(CaisseMouvementArticlePersistant caisseMvmP : listDetail) {
		    				 if(BooleanUtil.isTrue(caisseMvmP.getIs_annule())){
						           continue;
						       }
		    				
		    				if("MENU".equals(caisseMvmP.getType_ligne()) && caisseMvmP.getOpc_menu() == null) {
		    					if(idx != 0){%>
		    					</table>
		    					</div>
		    					<%} 
		    						idx++;
		    					%>
		    					<div style="width: 300px;float: left;">
		    						<table style="width: 98%;">
		    				<%}
		    					boolean isArticle = false;
			    				// Ajout du num�ro dans le tableau
						       String libCmd = caisseMvmP.getLibelle();
						       String type = caisseMvmP.getType_ligne();
						       if(type == null){
						           type = "XXX";
						       }
						       if(type.equals(TYPE_LIGNE_COMMANDE.MENU.toString()) && (caisseMvmP.getLevel() == null || caisseMvmP.getLevel() > 1)) {
						           idxArticle++;
						           libCmd = idxArticle + "-" + libCmd;
						       } else if(type.equals(TYPE_LIGNE_COMMANDE.ART.toString())){
						           idxArticle++;
						           libCmd = idxArticle + "-" + libCmd;
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
						      if(caisseMvmP.getMenu_idx() != null || type.equals(TYPE_LIGNE_COMMANDE.MENU.toString())){
						    	  if(type.equals(TYPE_LIGNE_COMMANDE.MENU.toString())) {
						    		  if(caisseMvmP.getLevel() != null && caisseMvmP.getLevel() == 1){
						    			  classType = "menu-cat-style";
						    		  } else{
						        	   	classType = "menu-style";
						    		  }
						           } else if(type.equals(TYPE_LIGNE_COMMANDE.ART_MENU.toString())) {
						        	   isArticle = true;
						        	   classType = "ligne-style";
						        	   nbrNiveau = 0;
						           } else if(type.equals(TYPE_LIGNE_COMMANDE.GROUPE_MENU.toString())){
						        	   classType = "group-style";
						        	   nbrNiveau++;
						           } else if(type.equals(TYPE_LIGNE_COMMANDE.GROUPE_FAMILLE.toString())){
						        	   classType = "group-style";
						        	   nbrNiveau++;
						           }
						       } else{
						    	   if(type.equals(TYPE_LIGNE_COMMANDE.ART.toString())){
						    		   classType = "ligne-fam-style";
						    		   isArticle = true;
						    		   nbrNiveau = 0;
						           } else if(type.equals(TYPE_LIGNE_COMMANDE.GROUPE_FAMILLE.toString())){
						        	   // Ajouter une ligne e s�paration
						        	   if(!isFamillePassed){
						        		   %>
						        	       <tr style="height:3px;color: black;background-color:#d7ccc8;" class="menu-root-style">
						        	       		<td colspan="2">&nbsp;</td>
						        	       </tr>
						        	       <%
						        	       isFamillePassed = true;
						        	   }
						        	   classType = "famille-style";
						        	   nbrNiveau++;
						           } 
						       }
						       
						       String params = "cd="+caisseMvmP.getCode()+"&elm="+caisseMvmP.getElementId()+"&par="+StringUtil.getValueOrEmpty(caisseMvmP.getParent_code())+"&mnu="+caisseMvmP.getMenu_idx()+"&tp="+caisseMvmP.getType_ligne();
						       if(caisseMvmP.getLevel() != null){
						    	   if(caisseMvmP.getLevel() != null && caisseMvmP.getLevel() > 1){
							    	   styleTd = styleTd + "padding-left:"+(10*caisseMvmP.getLevel())+"px;";
							       } else if(isArticle){
							    	   styleTd = styleTd + "padding-left:60px;";
							       }
						       }
						      
						       boolean isToAdd = (isArticle || NBR_NIVEAU_CAISSE == null || nbrNiveau <= NBR_NIVEAU_CAISSE) && !caisseMvmP.getLibelle().startsWith("#") && !type.equals(TYPE_LIGNE_COMMANDE.LIVRAISON.toString());
						       //
						       if (isToAdd){
						    	   boolean isNotArt = (!type.equals(TYPE_LIGNE_COMMANDE.ART.toString()) && !type.equals(TYPE_LIGNE_COMMANDE.ART_MENU.toString()));
						       %>
						               
							       <tr class="<%=classType%>" style="border-bottom: 1px dotted #777;">
							       		<td align="left" style="<%=styleTd %>"><%=(!isNotArt ? "<span class='fa fa-circle' style='color:#9dc2ff;'></span>&nbsp;":"&nbsp;")+libCmd %></td>
							       		<td align="right" style="font-weight: bold;"><%=isNotArt ? "":qte %></td>
							       </tr>
							       
							       <%if(StringUtil.isNotEmpty(caisseMvmP.getCommentaire())){ %>
						       		<tr style="background-color: yellow;">
						       			<td colspan="2"><%=caisseMvmP.getCommentaire() %></td>
						       		</tr>
						       <%} %>
						       <%
		    				}
		    			}%>
		    			</table>
		    			</div>
		    	<% }
		    		
		    		if(listDetHs != null){
		    	%>
		    	
		    	<table>
		    	<%		    	
		    		idx = 0;
		    		for(CaisseMouvementArticlePersistant caisseMvmP : listDetHs) {
	    				 if(BooleanUtil.isTrue(caisseMvmP.getIs_annule())){
					           continue;
					       }
	    				   boolean isArticle = false;
		    			   // Ajout du num�ro dans le tableau
					       String libCmd = caisseMvmP.getLibelle();
					       String type = caisseMvmP.getType_ligne();
					       if(type == null){
					           type = "XXX";
					       }
					       if(type.equals(TYPE_LIGNE_COMMANDE.MENU.toString()) && (caisseMvmP.getLevel() == null || caisseMvmP.getLevel() > 1)) {
					           idxArticle++;
					           libCmd = idxArticle + "-" + libCmd;
					       } else if(type.equals(TYPE_LIGNE_COMMANDE.ART.toString())){
					           idxArticle++;
					           libCmd = idxArticle + "-" + libCmd;
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
				    	   if(type.equals(TYPE_LIGNE_COMMANDE.ART.toString())){
				    		   classType = "ligne-fam-style";
				    		   isArticle = true;
				    		   nbrNiveau = 0;
				           } else if(type.equals(TYPE_LIGNE_COMMANDE.GROUPE_FAMILLE.toString())){
				        	   classType = "famille-style";
				        	   nbrNiveau++;
				           } 
					       
					       String params = "cd="+caisseMvmP.getCode()+"&elm="+caisseMvmP.getElementId()+"&par="+StringUtil.getValueOrEmpty(caisseMvmP.getParent_code())+"&mnu="+caisseMvmP.getMenu_idx()+"&tp="+caisseMvmP.getType_ligne();
					       if(caisseMvmP.getLevel() != null){
					    	   if(caisseMvmP.getLevel() != null && caisseMvmP.getLevel() > 1){
						    	   styleTd = styleTd + "padding-left:"+(10*caisseMvmP.getLevel())+"px;";
						       } else if(isArticle){
						    	   styleTd = styleTd + "padding-left:60px;";
						       }
					       }
					      
					       boolean isToAdd = (isArticle || NBR_NIVEAU_CAISSE == null || nbrNiveau <= NBR_NIVEAU_CAISSE) && !caisseMvmP.getLibelle().startsWith("#") && !type.equals(TYPE_LIGNE_COMMANDE.LIVRAISON.toString());
					       //
					       if (isToAdd){
					    	   boolean isNotArt = (!type.equals(TYPE_LIGNE_COMMANDE.ART.toString()) && !type.equals(TYPE_LIGNE_COMMANDE.ART_MENU.toString()));
					       %>
					               
						       <tr class="<%=classType%>" style="border-bottom: 1px dotted #777;">
						       		<td align="left" style="<%=styleTd %>"><%=(!isNotArt ? "<span class='fa fa-circle' style='color:#9dc2ff;'></span>&nbsp;":"&nbsp;")+libCmd %></td>
						       		<td align="right" style="font-weight: bold;"><%=isNotArt ? "":qte %></td>
						       </tr>
						       
						       <%if(StringUtil.isNotEmpty(caisseMvmP.getCommentaire())){ %>
					       		<tr style="background-color: yellow;">
					       			<td colspan="2"><%=caisseMvmP.getCommentaire() %></td>
					       		</tr>
					       <%}
		    			}
		    		}%>
		    	</table>
		    	<%} %>
		    			</div>
		    	
		    			</div>
		    		</div>
		    		<%
		    	}
						%> 		    
				</div>
			</div>
		</div>	
</div>