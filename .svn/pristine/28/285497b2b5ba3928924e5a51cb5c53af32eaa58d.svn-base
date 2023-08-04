<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="java.util.ArrayList"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli.TYPE_LIGNE_COMMANDE"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementOffrePersistant"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="java.util.Date"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="java.util.Random"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementPersistant"%>
<%@page import="java.util.List"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<%
	List<CaisseMouvementPersistant> listCaisseMouvement = (List<CaisseMouvementPersistant>)request.getAttribute("listCaisseMouvement");
String[] colors = {"yellow", "palegreen", "azure", "#03a9f4", "#ffc107", "#fff59d", "gray", "#fb6e52", "#f4b400", "#5db2ff", "#838422", "#6c2d28", "#c4ddbd", "#b6897f"};
boolean isAlertSonore = request.getAttribute("alert_sonore") != null;
String tp = (String)ControllerUtil.getUserAttribute("tp", request);
Integer NBR_NIVEAU_CAISSE = StringUtil.isNotEmpty(ContextGloabalAppli.getGlobalConfig("NBR_NIVEAU_CAISSE"))?Integer.valueOf(ContextGloabalAppli.getGlobalConfig("NBR_NIVEAU_CAISSE")):null;
%>

<!-- Pager -->
<%
	Integer nbrCols = (Integer)request.getAttribute("nbrCols");
int nbrTotal = (Integer)request.getAttribute("nbrTotal");
int page_count = (int) Math.ceil((double) nbrTotal / nbrCols);// Calculate

String currPageSt = (String)ControllerUtil.getUserAttribute("sidx", request);
int currPage = Integer.valueOf(currPageSt==null?"0":currPageSt);

int previousPage = currPage==0 ? 0 : currPage-1;
int nextPage = (currPage+1)>=page_count ? currPage : currPage+1;
int currNbrElmnt = (currPage+1)*nbrCols;
currNbrElmnt = (currNbrElmnt > nbrTotal) ? nbrTotal : currNbrElmnt;

String stlPrev = "margin-left: 70px;"+(currPage==0 ? "background-color:gray !important;":"");
String stlNext = ((currPage+1)>=page_count ? "background-color:gray !important;":"");

String[] checks = (String[])request.getAttribute("checks");
%>
<style>
.class-bg td{
	background-color: #DADADA;
	color :black;
	font-weight: bold;
}

.td-bg{
    background-color: white;
    color :black;
}

</style>

<script type="text/javascript">
	$(document).ready(function (){
		$(".zoomDiv").click(function(e){
			if($(e.target).prop("nodeName")=='INPUT'){
				return;
			}
			$("#zoomLnk").attr("params", $(this).attr("params")).trigger('click'); 
		});
		<%if (page_count == 0){%>
			$("#pager-bloc").css("display", "none");
		<%} else{%>
		$("#pager-bloc").css("display", "");
		<%}
		if(page_count == 0 || /*tp.equals("val") ||*/ tp.equals("liv")){%>
			$("#valid-cmds").css("display", "none");
		<%} else{%>
			$("#valid-cmds").css("display", "");
		<%}%>
	});
</script>
		
<%
			if(request.getAttribute("alert_sonore") != null){
		%>		
	<embed src="resources/framework/sound/rington.wav" height="0px" width="0px" autoplay="true" style="position: absolute;left: -100px;top: -100px;">
<%
	}
%>	

<%
		if (page_count >= 1){
		String prev = "sidx="+previousPage;
		String next = "sidx="+nextPage;
	%>
<div style="position: absolute;top: 4px;right: 170px;z-index: 1000;" id="pager-bloc">
  <std:link classStyle="btn btn-blue btn-sm icon-only white" targetDiv="detail-pres-div" action="caisse-web.presentoire.loadCommande" params="<%=prev%>" style="<%=stlPrev%>" icon="fa fa-arrow-circle-left" />
	<span style="color: white;"><%=currNbrElmnt%>/<%=nbrTotal%></span>
  <std:link classStyle="btn btn-blue btn-sm icon-only white" targetDiv="detail-pres-div" action="caisse-web.presentoire.loadCommande" params="<%=next%>" style="<%=stlNext%>" icon="fa fa-arrow-circle-right" />
</div>
<%
	}
%>

		<std:linkPopup id="zoomLnk" action="caisse-web.presentoire.zoomCommande" style="display:none;" classStyle="" />
      <div class="row pricing-container" style="padding: 8px;padding-right: 16px;">
     <%
     	if(listCaisseMouvement.size() == 0){
     %>
		<h2 style="text-align: center;color:white;margin-top: 10%;">Aucune commande disponible.</h2>
	<%
		}
	     Integer DELAIS_ALERT_MINUTE = (Integer)ControllerUtil.getUserAttribute("DELAIS_ALERT_MINUTE", request);
	     int idx = 0;
	     Date now = new Date();
	     for(CaisseMouvementPersistant mvm : listCaisseMouvement){ 
	    	 boolean isTempsDepasse = false;
	    	 String clock = "";
	         
	    	 if(DELAIS_ALERT_MINUTE != null){
		    	 Date dateCmd = mvm.getDate_creation();
		         if(dateCmd == null){
		             continue;    
		         }
		         
		         int minutes = DateUtil.getDiffMinuts(dateCmd, now);
		        int secondes = DateUtil.getDiffSeconds(dateCmd, now);
		
		         if (minutes > DELAIS_ALERT_MINUTE) {
		             isTempsDepasse = true;
		         }
			    if (secondes < 60) {
			        clock += secondes < 10 ? "0" + secondes : "" + secondes;
			    } else {
			        int mints = secondes / 60;
			        secondes = secondes % 60;
			        clock += mints < 10 ? "0" + mints : "" + mints;
			        clock += ":";
			        clock += secondes < 10 ? "0" + secondes : "" + secondes;
			    }
	    	}
	        String typeCmd = null;
	        String iconTP = null;
	        if(ContextAppli.TYPE_COMMANDE.E.toString().equals(mvm.getType_commande())){
	            typeCmd = "A emporter";
	            iconTP = "class=\"fa fa-street-view\"";
	        } else if(ContextAppli.TYPE_COMMANDE.P.toString().equals(mvm.getType_commande())){
	             typeCmd = "Sur place";
	             iconTP = " class=\"fa fa-dropbox\"";
	        } else{
	            typeCmd = "Livraison";
	            iconTP = "class=\"fa fa-motorcycle\"";
	        }
	        String parMvm = "tp=P&mvm="+EncryptionUtil.encrypt(mvm.getId().toString());
	%>     
           <div class="plan" style="width: ${Integer.valueOf(100/nbrCols)-2}%;float: left;margin-left: 12px;border-radius: 10px; background-color:<%=colors[idx]%>;">
                <table style="width: 100%;background-color: #FFADBC;border-bottom: 1px;border:none; border-top-right-radius: 10px;border-top-left-radius: 10px;border-collapse: separate; " class="entete-cmd">
                	 <tr style="border-bottom: 1px dashed white;height: 30px;">
                	 	<td style="width: 15px;margin-left: 6px;">
	                		<%
	                			if(tp.equals("pre")){
	                		%>
	                			<input type="checkbox" name="mvm-check" value="<%=mvm.getId()%>" <%=StringUtil.contains(mvm.getId().toString(), checks)?" checked='checked'":""%> style="height: 24px;width: 24px;margin-left: 6px;outline: 1px solid #1e5180">
		                	<%
		                		}
		                	%>
                		</td>
                		<td align="right" style="padding-right: 2px;">
                			<%
                				if(tp.equals("pre")){
                			%>
                				<std:link classStyle="btn btn-palegreen shiny btn-xs" style="width: 108px;color:black;" targetDiv="detail-pres-div" params='<%="mnu="+mvm.getId()%>' action="caisse-web.presentoire.changerStatut" icon="fa-3x fa-check" value="Statut" tooltip="Changer le statut de la commande" />
                			<%
                				} else{
                			%>
	                			<%
	                				if(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.PREP.toString().equals(mvm.getLast_statut())){
	                			%>
<!-- 						       		<i class="fa fa-recycle" style="color: blue;font-size: 17px;"></i> -->
<!-- 									<img src="resources/caisse/img/caisse-web/inprogress.gif" width="24" /> -->
						       	<%
						       		}
						       	%>		
                			<%
		                				}
		                			%>
                			<a href="javascript:" class="btn btn-yellow shiny btn-xs zoomDiv" params="<%=parMvm%>" style="width: 60px;color:black;"><i class="fa fa-search-plus"></i> Zoom</a>
                		</td>
                	</tr>
                	<tr style="border-radius: 4px;cursor:pointer;border-bottom: 1px dashed blue;">
                		<td colspan="2" style="text-align: center;">
                			<%
                				if(isTempsDepasse){
                			%>
                				<i class="fa fa-warning" style="font-size: 18px;color: red;"></i>
                			<%
                				}
                			%>	
                			<span style="font-size: 18px;font-weight: bold;"><%=(mvm.getRef_commande().length()>12?mvm.getRef_commande().substring(12):mvm.getRef_commande())%></span> <br>
                			<span style="font-size: 12px;"><%=clock%></span>
							<div style="padding: 0px 10px 0px 7px;
								    float: left;
								    margin-left: 150px;
								    height: 20px;
								    line-height: 18px;
								    background-color: #ba8c15;
								    border-radius: 10px;"> 
                			| <span style="color: white;font-weight: bold;font-size: 12px;"><%=typeCmd.toUpperCase()%></span>
		                		<i style="color: black;" <%=iconTP %>/>
		                	</div>	                		</td>
                	 </tr>
                	 
                	 <%
                	                 	 	String refTables = mvm.getRefTablesDetail();
                	                 	                 	 if(StringUtil.isNotEmpty(mvm.getNum_token_cmd()) || StringUtil.isNotEmpty(refTables)){
                	                 	 %>
                	<tr align="center">
		                <td colspan="2" style="font-weight: bold;">
		                	<%=StringUtil.isNotEmpty(mvm.getNum_token_cmd()) ?"<i class='fa fa-tag'></i> "+mvm.getNum_token_cmd() : ""%>
		                	&nbsp;&nbsp;&nbsp;
		                	<%=StringUtil.isNotEmpty(refTables) ? "<i class='fa fa-cutlery'></i> "+refTables : ""%>
		                </td>
                	</tr>
                	<%
                		}
                	%>
                </table>
                <table style="width: 100%;color: black;text-align: left;border-collapse: separate;" id="cmd-table">
					<%
						List<Integer> listIdxClient = new ArrayList();
								for(CaisseMouvementArticlePersistant caisseMvmP : mvm.getList_article()){
									if(BooleanUtil.isTrue(caisseMvmP.getIs_annule())){
										continue;
									}
									if(!listIdxClient.contains(caisseMvmP.getIdx_client()) && caisseMvmP.getIdx_client() != null){
										listIdxClient.add(caisseMvmP.getIdx_client()); 
									}
								}
								
								Integer idxArticle = 0;
								int nbrNiveau = 0;
								int nbrClient = (mvm.getMax_idx_client() == null ? 1 : mvm.getMax_idx_client());
							    //
								for(int i=1; i<=nbrClient; i++){// Les clients
									boolean isFamillePassed = false;  
									
									if(!listIdxClient.contains(i)){
										continue;
									}
									
									if(nbrClient > 1){
					%>
				    	   <tr style="color: black;background-color: #f44336;" class="client-root-style">
					       		<td colspan="2" style="border-radius: 15px;" align="center">CLIENT <%=i%></td>
					       </tr>
				      	<%
				      		}
				      					
				      					// Detail
				      					for(CaisseMouvementArticlePersistant caisseMvmP : mvm.getList_article()){//---------------------------------Detail commande
				      				       if(caisseMvmP.getIdx_client()!=null && caisseMvmP.getIdx_client()!=i){
				      				           continue;
				      				       }

				      					  // Ajout du num&eacute;ro dans le tableau
				      				       String type = caisseMvmP.getType_ligne();
				      				       String libCmd = caisseMvmP.getLibelle();
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
				      				       
				      				       String styleAnnul = "";
				      				       if(BooleanUtil.isTrue(caisseMvmP.getIs_annule())){
				      				    		styleAnnul = "color:#ffbebe !important;font-style: italic !important;;text-decoration: line-through !important;;";
				      				       }
				      				       
				      				       Integer qte = (caisseMvmP.getQuantite() != null ? caisseMvmP.getQuantite().intValue() : null);
				      	// 					       String mttTotal = "";
				      	// 					       if( BooleanUtil.isTrue(caisseMvmP.getIs_offert())){
				      	// 					    	   mttTotal = "<i class='fa fa-gift'style='color:green;font-size: 17px;cursor:pointer;' data-container='body' data-titleclass='bordered-blue' data-class='' data-toggle='popover' data-placement='top' data-title='Article offert' data-content='Prix : "+BigDecimalUtil.formatNumber(caisseMvmP.getMtt_total())+"' data-original-title='' title=''></i>";
				      	// 					       } else if(!BigDecimalUtil.isZero(caisseMvmP.getMtt_total())){
				      	// 					    	   mttTotal = BigDecimalUtil.formatNumber(caisseMvmP.getMtt_total());
				      	// 					       }
				      				       
				      				       String styleTd = "";
				      				       String iconStl = "";
				      				       String classType = "";
				      				      if(caisseMvmP.getMenu_idx() != null || type.equals(TYPE_LIGNE_COMMANDE.MENU.toString())){
				      				    	  if(type.equals(TYPE_LIGNE_COMMANDE.MENU.toString())) {
				      				    		  if(caisseMvmP.getLevel() != null && caisseMvmP.getLevel() == 1){
				      				    			  classType = "menu-cat-style class-bg";
				      				    		  } else{
				      				        	   	classType = "menu-style class-bg";
				      				    		  }
					      				    		iconStl = "<i id='curr-btn' style='color: red;' class='fa fa-fire'></i>";
				      				           } else if(type.equals(TYPE_LIGNE_COMMANDE.ART_MENU.toString())) {
				      				        	   classType = "ligne-style td-bg";
				      				        	   nbrNiveau = 0;
				      				           } else if(type.equals(TYPE_LIGNE_COMMANDE.GROUPE_MENU.toString())){
				      				        	   classType = "group-style td-bg";
				      				        	   nbrNiveau++;
				      				           } else if(type.equals(TYPE_LIGNE_COMMANDE.GROUPE_FAMILLE.toString())){
				      				        	   classType = "group-style class-bg";
				      				        	   nbrNiveau++;
					      				    		iconStl = "<i id='curr-btn' style='color: red;' class='fa fa-fire'></i>";
				      				           }
				      				       } else{
				      				    	   if(type.equals(TYPE_LIGNE_COMMANDE.ART.toString())){
				      				    		   classType = "ligne-fam-style td-bg";
				      				    		   nbrNiveau = 0;
				      				           } else if(type.equals(TYPE_LIGNE_COMMANDE.GROUPE_FAMILLE.toString())){
				      				        	   // Ajouter une ligne e s&eacute;paration
				      				        	   if(!isFamillePassed){
				      	%>
					        	       <tr style="height:3px;color: black;background-color:#d7ccc8;" class="menu-root-style">
					        	       		<td colspan="2" align="center"></td>
					        	       </tr>
					        	       <%
					        	       	isFamillePassed = true;
					        	       			        	   }
					        	       			        	   classType = "famille-style class-bg";
					        	       			        	   nbrNiveau++;
								      				    		iconStl = "<i id='curr-btn' style='color: red;' class='fa fa-fire'></i>";
					        	       			           } 
					        	       			       }
					        	       			        boolean isArticle = (type.equals(TYPE_LIGNE_COMMANDE.ART.toString()) || type.equals(TYPE_LIGNE_COMMANDE.ART_MENU.toString()));
					        	       				    boolean isToAdd = (isArticle || NBR_NIVEAU_CAISSE == null || nbrNiveau <= NBR_NIVEAU_CAISSE) && !caisseMvmP.getLibelle().startsWith("#") && !type.equals(TYPE_LIGNE_COMMANDE.LIVRAISON.toString());

					        	       			       String params = "cd="+caisseMvmP.getCode()+"&elm="+caisseMvmP.getElementId()+"&par="+StringUtil.getValueOrEmpty(caisseMvmP.getParent_code())+"&mnu="+caisseMvmP.getMenu_idx()+"&tp="+caisseMvmP.getType_ligne();
					        	       			       if(caisseMvmP.getLevel() != null && caisseMvmP.getLevel() > 1){
					        	       			    	   styleTd = styleTd + "padding-left:"+(7*caisseMvmP.getLevel())+"px;";
					        	       			       } else if(isArticle){
					        	       			    	   styleTd = styleTd + "padding-left:40px;";
					        	       			       }
					        	       			       boolean isNotArt = (!type.equals(TYPE_LIGNE_COMMANDE.ART.toString()) && !type.equals(TYPE_LIGNE_COMMANDE.ART_MENU.toString()));
					        	       			      
					        	       			       if(isToAdd){
					        	       %>
					       <%
					       	if(BooleanUtil.isTrue(caisseMvmP.getIs_menu())){
					       %>
					         	<tr style="height:28px;<%=styleAnnul%>" class="<%=classType%>" par="<%=params%>">
						       		<td colspan="2" style="<%=styleTd%>">
						       			<%
						       				if(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.PRETE.toString().equals(caisseMvmP.getLast_statut())){
						       			%>
						       				<i class="fa fa-check-square-o" style="color: green;font-size: 17px;"></i>
										<%
											} else if(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.PREP.toString().equals(caisseMvmP.getLast_statut())){
										%>
						       				<img style="margin-top:-5px;" src="resources/caisse/img/caisse-web/inprogress.gif" width="24" />
						       			<%
						       				} else if(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.LIVRE.toString().equals(caisseMvmP.getLast_statut())){
						       			%>
						       				<i class="fa fa-user" title="Livr&eacute;e" style="color: blue;font-size: 17px;"></i>
						       			<%
						       				} else if(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE.toString().equals(caisseMvmP.getLast_statut())){
						       			%>
						       				<i class="fa fa-bars" title="Valid&eacute;e" style="color: green;font-size: 17px;"></i>
						       			<%
						       				}
						       			%>
						       			
						       			<%=(!isNotArt ? "<span class='fa fa-circle' style='color:#9dc2ff;'></span>&nbsp;":"&nbsp;")+iconStl+libCmd%>
						       			
						       			<div style="margin-top: 4px;float: right;">
							       			<%
							       				if(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.PRETE.toString().equals(caisseMvmP.getLast_statut())){
							       			%>
												<std:link style="" action="caisse-web.presentoire.changerStatut" targetDiv="detail-pres-div" params='<%="cmd="+mvm.getId()+"&mnu="+caisseMvmP.getId()%>' classStyle="btn btn-palegreen shiny btn-xs" icon="fa-3x fa-check" value="" tooltip="Changer statut" />
											<%
												} else{
											%>
							       				<std:link action="" targetDiv="XXX" classStyle="btn btn-palegreen btn-xs" icon="fa fa-fire" style="visibility:hidden;position:absolute;top:-100px;" />
							       			<%
							       				}
							       			%>
						       			</div>
									</td>
						       </tr>
					        <%
					        	} else{
					        %>
					         	<tr style="<%=styleAnnul%><%=!isNotArt ? "height:28px;":""%>" class="<%=classType%>" par="<%=params%>">
						       		<td <%=isNotArt?" colspan='2' ":""%> style="<%=styleTd%>">
						       			<%
						       				if(!isNotArt){
						       			%>
						       			 <%
						       			 	if(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.PRETE.toString().equals(caisseMvmP.getLast_statut())){
						       			 %>
						       				<i class="fa fa-check-square-o" style="color: green;font-size: 17px;"></i>
						       			<%
						       				} else if(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.PREP.toString().equals(caisseMvmP.getLast_statut())){
						       			%>
						       				<img style="margin-top:-5px;" src="resources/caisse/img/caisse-web/inprogress.gif" width="24" />
						       			 <%
						       			 	} else if(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.LIVRE.toString().equals(caisseMvmP.getLast_statut())){
						       			 %>
						       				<i class="fa fa-user" title="Livr&eacute;e" style="color: blue;font-size: 17px;"></i>
						       			<%
						       				} else if(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE.toString().equals(caisseMvmP.getLast_statut())){
						       			%>
						       				<i class="fa fa-bars" title="Valid&eacute;e" style="margin-top: -8px;color: green;font-size: 17px;"></i>
						       			<%
						       				}
						       							       			}
						       			%>
						       			<%=(!isNotArt ? "<span class='fa fa-circle' style='color:#9dc2ff;'></span>&nbsp;":"&nbsp;")+iconStl+libCmd%>
						       		<%
						       			if(!isNotArt){
						       		%>
						       		</td>
						       		<td align="right" nowrap="nowrap" style="padding-right: 5px;">
						       		<%
						       			}
						       		%>
						       		<div style="vertical-align: middle;">
						       			<%
						       				if(!isNotArt){
						       			%>
						       			 <%
						       			 	if(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.PRETE.toString().equals(caisseMvmP.getLast_statut())){
						       			 %>
						       				<std:link style="" action="caisse-web.presentoire.changerStatut" targetDiv="detail-pres-div" params='<%="cmd="+mvm.getId()+"&mnu="+caisseMvmP.getId() %>' classStyle="btn btn-palegreen shiny btn-xs" icon="fa-3x fa-check" value="" tooltip="Changer statut" />
						       			<% } else{%>
							       			<std:link action="" targetDiv="XXX" classStyle="btn btn-palegreen btn-xs" icon="fa fa-fire" style="visibility:hidden;position:absolute;top:-100px;" />
							       		<%}%>
								<span class="badge badge-darkorange " style="font-weight: bold;padding-right: 3px;font-weight: bold;
									    padding: 1px 8px 6px 6px;
									    font-size: 19px !important;
									    height: 24px;"><%=qte %></span>
							       	  <%}%>
							       	  </div>
						       		</td>
						       </tr>
					        <%} %>
					       <%if(StringUtil.isNotEmpty(caisseMvmP.getCommentaire())){ %>
						       		<tr style="background-color: yellow;">
						       			<td colspan="2" style="font-size: 12px;"><%=caisseMvmP.getCommentaire() %></td>
						       		</tr>
						       <%} %>
					       <%
					       }
					   }
					}
					%>     
			</table>  
        </div>
  <%
  	idx++;
	  if(idx > colors.length){
		  idx = 0;
	  
     } 
    }%>      
    </div>
