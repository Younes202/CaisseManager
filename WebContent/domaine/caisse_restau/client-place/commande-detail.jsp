<%@page import="appli.model.domaine.util_srv.raz.RazService"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementOffrePersistant"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementPersistant"%>
<%@page import="appli.controller.domaine.caisse.ContextAppliCaisse"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="appli.model.domaine.stock.service.IArticleService"%>
<%@page import="java.util.Map"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="appli.controller.domaine.caisse.action.CaisseWebBaseAction"%>
<%@page import="framework.model.common.service.MessageService"%>
<%@page import="framework.controller.bean.message.GrowlMessageBean"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli.TYPE_LIGNE_COMMANDE"%>
<%@page import="java.util.List"%>

<style>
#cmd-table td{
	vertical-align: middle;
	font-size: 11px;
	text-transform:  uppercase;
}
</style>


<%
String CURRENT_ITEM_ADDED = (String)ControllerUtil.getUserAttribute("CURRENT_ITEM_ADDED", request);
String CURRENT_MENU_NUM = (String)ControllerUtil.getUserAttribute("CURRENT_MENU_NUM", request);
CaisseMouvementPersistant CURRENT_COMMANDE = (CaisseMouvementPersistant)ControllerUtil.getUserAttribute("CURRENT_COMMANDE", request);
List<CaisseMouvementArticlePersistant> listSortedArticle = CURRENT_COMMANDE.getList_article();
if(listSortedArticle == null){
	listSortedArticle = new ArrayList<>();
}
Integer NBR_NIVEAU_CAISSE = StringUtil.isNotEmpty(ContextGloabalAppli.getGlobalConfig("NBR_NIVEAU_CAISSE"))?Integer.valueOf(ContextGloabalAppli.getGlobalConfig("NBR_NIVEAU_CAISSE")):null;
IArticleService service = (IArticleService)ServiceUtil.getBusinessBean(IArticleService.class);
Map<String, BigDecimal> mttMenuMap = new HashMap<>(); 
boolean isJourneeCaisseOuverte = ContextAppli.getEtablissementBean() != null 
			&& ContextAppliCaisse.getJourneeCaisseBean() != null
			&& "O".equals(ContextAppliCaisse.getJourneeCaisseBean().getStatut_caisse());
%>
<div class="dataill-montant" id="detialDev" style="background-color: white;
			    position: fixed;
			    width: 73%;
			    height:100%;
			    top: 0px;
			    left: 10%;">
<!-- R&eacute;cap commande -->
<div style="height: 70px;border-radius: 48px;border: 1px dashed #cd4c4c; <%=StringUtil.isEmpty(CaisseWebBaseAction.GET_STYLE_CONF("PANEL_ENETETE", null))?"background-image: linear-gradient(to right, #750404 0%, #480f0f 51%, #e80a39 100%);":"background:"+ContextGloabalAppli.getGlobalConfig("PANEL_COMMANDE")%>">
	<span style="font-weight: bold;font-size: 50px;margin-left: 15%;line-height: 65px;left: 26%;position: absolute;color: white;"> 
		<%=CURRENT_COMMANDE!=null?BigDecimalUtil.formatNumberZeroBd(CURRENT_COMMANDE.getMtt_commande_net()):""%>
	</span>
	
	<a href="javascript:" wact="<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.ajouterGroupe")%>" params="addCli=1" targetDiv="left-div" style="    position: absolute;
    right: 78%;
    top: 7px;
    font-size: 2em;
    margin-right: -4em;
    white-space: nowrap;
    color: white;
    font-weight: 800;"><i class="fa fa-male" style="color: orange;font-size: 25px;"></i> Ajouter personne</a>
	
	<%
			if(CURRENT_COMMANDE != null){
				if(CURRENT_COMMANDE.getOpc_client() != null){
		%>
			<span style="font-size: 13px;color: #ed4e2a;position: absolute;left: 29px;top: -5px;">
				<i class="fa fa-users" style="color: #795548;"></i> <%=CURRENT_COMMANDE.getOpc_client().getNom()+" "+StringUtil.getValueOrEmpty(CURRENT_COMMANDE.getOpc_client().getPrenom())%>
				<a href="javascript:" wact="<%=EncryptionUtil.encrypt("caisse-web.caisseWebClient.resetElement")%>" params="tp=cli" targetDiv="left-div" style="font-size: 17px;margin-left: 2px;"><i class="fa fa-times" style="color: red;"></i></a>
			</span>
		<%
				}
			}
		%>
</div>
  
 <div style="color: yellow;overflow: auto;border: 1px solid #cabdbd;<%=StringUtil.isEmpty(CaisseWebBaseAction.GET_STYLE_CONF("PANEL_ENETETE", null))?"background: white;":"background:"+ContextGloabalAppli.getGlobalConfig("PANEL_COMMANDE")%>" id="div_detail_cmds">    		
<table style="width: 100%;color: black;" id="cmd-table">
	<tr>
		<th></th>
		<th></th>
		<th></th>
		<th style="width: 65px;"></th>
	</tr>
	
	<%if(request.getAttribute("mtt_rendu") != null){ %>
	<tr>
		<td>
			<div class="alert alert-success fade in radius-bordered alert-shadowed" style="font-size: 24px;margin-top: 50px;">
		         <button class="close" data-dismiss="alert" style="margin-top: 10px;">X</button>
		         <i class="fa-fw fa fa-check"></i>
		         A rendre :<strong> <%=BigDecimalUtil.formatNumber(BigDecimalUtil.get(""+request.getAttribute("mtt_rendu"))) %></strong> 
		     </div>
     	</td>
     </tr>
	<%} %>

<%
//Total par menu
for(CaisseMouvementArticlePersistant caisseMvmP : listSortedArticle){
	 if(StringUtil.isEmpty(caisseMvmP.getMenu_idx()) || BooleanUtil.isTrue(caisseMvmP.getIs_annule())){
	     continue;
	 }
	 mttMenuMap.put(caisseMvmP.getMenu_idx(),  BigDecimalUtil.add( mttMenuMap.get(caisseMvmP.getMenu_idx()), caisseMvmP.getMtt_total()));
}

	Integer idxArticle = 0;
	int nbrNiveau = 0;
	BigDecimal sousTotal = null;
	Integer currentIdxClient = (Integer)ControllerUtil.getUserAttribute("CURRENT_IDX_CLIENT", request);
	//
	for(int i=1; i<=CURRENT_COMMANDE.getMax_idx_client(); i++){
		boolean isFamillePassed = false;
		
		if(CURRENT_COMMANDE.getMax_idx_client() > 1){
	    	   if(i != 1){%>
	    		   <tr style="color: black;background-color: #eeeeee;height: 22px;" class="client-root-style">
		       			<td colspan="2" align="right" style="font-weight: bold;font-size: 2em;">SOUS TOTAL</td>
		       			<td></td>
		       			<td align="right" style="font-weight: bold;font-size: 2em;"><%=BigDecimalUtil.formatNumber(sousTotal) %></td>
		      	   </tr>
	    	 <%}
	    	   %>
	    	   <tr style="color: black;background-color: #f44336;height: 22px;" class="client-root-style" par="idx_cli=<%=i %>&tp=CLI">
		       		<td colspan="4" style="border-radius: 15px;font-size: 2em;font-weight: 700;color: black;" align="center">
		       			<a href="javascript:" class="mnu_td" tp="CLI" style="text-decoration: underline;font-size: 2em;
    font-weight: 700;
    color: black;">
		       				<%=(currentIdxClient==i ? "<i style='color:#e3f2fd;' class='fa fa-arrow-circle-right'></i>":"") %>  CLIENT <%=i %>
		       			</a>
		       		</td>
		       </tr>
	      <%
		}
	    sousTotal = null;
	    //
		for(CaisseMouvementArticlePersistant caisseMvmP : listSortedArticle){
	       if(BooleanUtil.isTrue(caisseMvmP.getIs_annule()) || caisseMvmP.getIdx_client()!=i){
	           continue;
	       }
	      sousTotal = BigDecimalUtil.add(sousTotal, caisseMvmP.getMtt_total());
	       
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
	       
	       String qte = "";
	       if(caisseMvmP.getQuantite() != null){
	    	   if(caisseMvmP.getQuantite().doubleValue() % 1 != 0){
	    		 qte = BigDecimalUtil.formatNumber(caisseMvmP.getQuantite());
	    	   } else{
	    		 qte = ""+caisseMvmP.getQuantite().intValue();
	    	   }
	       }
		       
	       String mttTotal = "";
	       if( BooleanUtil.isTrue(caisseMvmP.getIs_offert())){
	    	   mttTotal = "<i class='fa fa-gift' style='color:green;font-size: 17px;cursor:pointer;' data-container='body' data-titleclass='bordered-blue' data-class='' data-toggle='popover' data-placement='top' data-title='Article offert' data-content='Prix : "+BigDecimalUtil.formatNumber(caisseMvmP.getMtt_total())+"' data-original-title='' title=''></i>";
	       } else if(!BigDecimalUtil.isZero(caisseMvmP.getMtt_total())){
	    	   mttTotal = BigDecimalUtil.formatNumber(caisseMvmP.getMtt_total());
	       }
	       
	       String styleTd = "";
	       boolean isSelected = false;
	       if(CURRENT_ITEM_ADDED != null){
	    	   String parentCode = (caisseMvmP.getParent_code() != null && caisseMvmP.getParent_code().indexOf("_")!=-1 ? caisseMvmP.getParent_code().substring(caisseMvmP.getParent_code().indexOf("_")+1) : caisseMvmP.getParent_code());
	    	   String currElmntPath = caisseMvmP.getIdx_client()+"-"+caisseMvmP.getElementId()+"-"+caisseMvmP.getType_ligne()+"-"+parentCode+(StringUtil.isEmpty(caisseMvmP.getMenu_idx()) ?"":"-"+caisseMvmP.getMenu_idx());
	    	   if(CURRENT_ITEM_ADDED.equals(currElmntPath)){
	    		   isSelected = true;
	       		}
	       }
	       boolean isRootMenu = false;
	       boolean isMenu = false;
	       String classType = "";
	       String height = "35px";
	      if(StringUtil.isNotEmpty(caisseMvmP.getMenu_idx()) || type.equals(TYPE_LIGNE_COMMANDE.MENU.toString())){
	    	  if(type.equals(TYPE_LIGNE_COMMANDE.MENU.toString())) {
	    		  if(caisseMvmP.getLevel() != null && caisseMvmP.getLevel() == 1){
	    			  classType = "menu-cat-style cat-cmd-detail";
	    			  isRootMenu = true;
	    		  } else{
	        	   	classType = "menu-style cat-cmd-detail";
	        	   	isMenu = true;
	    		  }
	           } else if(type.equals(TYPE_LIGNE_COMMANDE.ART_MENU.toString())) {
	        	   classType = "ligne-style";
	        	   height = "30px";
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
	    		   height = "30px";
	    		   nbrNiveau = 0;
	           } else if(type.equals(TYPE_LIGNE_COMMANDE.GROUPE_FAMILLE.toString())){
	        	   // Ajouter une ligne e s�paration
	        	   if(!isFamillePassed){
	        		   %>
	        	       <tr style="height:3px;color: black;background-color:<%=isSelected?"#ffca06;":"#f35318" %>;" <%=isSelected?" isSel='1' ":"" %> class="menu-root-style">
	        	       		<td colspan="4" align="center" style="font-size: 2em;font-weight: 700;color: black;"></td>
	        	       </tr>
	        	       <%
	        	       isFamillePassed = true;
	        	   }
	        	   height = "5px";
	        	   classType = "group-style-fam";//"famille-style cat-cmd-famille";
	        	   nbrNiveau++;
	           } 
	       }
	       
	       boolean isArticle = (type.equals(TYPE_LIGNE_COMMANDE.ART.toString()) || type.equals(TYPE_LIGNE_COMMANDE.ART_MENU.toString()));
	       boolean isToAdd = !isRootMenu && (isArticle || NBR_NIVEAU_CAISSE == null || nbrNiveau <= NBR_NIVEAU_CAISSE) && !caisseMvmP.getLibelle().startsWith("#");
	       String params = "cli="+currentIdxClient+"&cd="+caisseMvmP.getCode()+"&elm="+caisseMvmP.getElementId()+"&par="+StringUtil.getValueOrEmpty(caisseMvmP.getParent_code())+"&mnu="+caisseMvmP.getMenu_idx()+"&tp="+caisseMvmP.getType_ligne();
	       if(caisseMvmP.getLevel() != null && caisseMvmP.getLevel() > 1){
	    	   styleTd = styleTd + "padding-left:"+(7*caisseMvmP.getLevel())+"px;";
	       } else if(isArticle){
	    	   styleTd = styleTd + "padding-left:30px;";
	       }
	      
	       if(isToAdd){
	    	   String mttTotalMenu = BigDecimalUtil.formatNumber(mttMenuMap.get(caisseMvmP.getMenu_idx()));
	    	   boolean isNotArt = (!type.equals(TYPE_LIGNE_COMMANDE.ART.toString()) && !type.equals(TYPE_LIGNE_COMMANDE.ART_MENU.toString()));
	       
	       		boolean isToClolapse = (StringUtil.isNotEmpty(caisseMvmP.getMenu_idx()) 
	       										&& !type.equals(TYPE_LIGNE_COMMANDE.MENU.toString())
	       										&& (CURRENT_MENU_NUM == null || !CURRENT_MENU_NUM.equals(caisseMvmP.getMenu_idx())));
	       %>
	       <tr id="<%=isToClolapse ? "tr_"+caisseMvmP.getMenu_idx():"" %>" class="<%=classType%><%=isMenu?(" mnu_"+caisseMvmP.getMenu_idx()):"" %>" style="height: <%=height %>;<%=isToClolapse?"display:none;":"" %><%=isArticle?"font-weight:bold;":""%> <%=isSelected?"background-color:#ffca06;":"" %>" <%=isSelected?" isSel='1' ":"" %> par="<%=params%>">
	       		<td style="<%=styleTd %>;<%="5px".equals(height)?"":"line-height:"+height %>;    font-size: 3em;
    font-weight: 700;
    padding-left: 1%;">
	       			
	       			<%
	       			if(type.equals(TYPE_LIGNE_COMMANDE.MENU.toString()) && (CURRENT_MENU_NUM == null || !CURRENT_MENU_NUM.equals(caisseMvmP.getMenu_idx())) && caisseMvmP.getLevel()>1 ){
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
	       		<td align="right" style="font-size: 2em;"><%=isNotArt ? "":qte %> </td>
	       		
	       		<td align="right" style="font-weight: bold;font-size: 2em;text-align: left;width: 8em;">
	       			<%if(StringUtil.isEmpty(caisseMvmP.getMenu_idx())){ %>
	       				<%=mttTotal %>
	       			<%} else{
	       				if(isMenu){
	       					boolean isNotCurr = (CURRENT_MENU_NUM == null || !CURRENT_MENU_NUM.equals(caisseMvmP.getMenu_idx()));
	       					%>
		       				<span class="span_mtt_art" style='display:<%=isNotCurr ? "none":""%>;'><%=mttTotal %></span>
		       				<span class="span_mtt_mnu" style='display:<%=isNotCurr ? "":"none"%>;'><%=mttTotalMenu %></span>
	       			<% } else{%>
	       				<%=mttTotal %>
	       			<% }
	       			} %>
	       		</td>
	       		
	       		<td align="right" style="padding: 0px;font-size: 2em;">
	       			<%if(isMenu){ %>
		       			<a href="javascript:" class="btn btn-default btn-xs shiny icon-only yellow mnu_td" tp="UPD" style="    height: 5em;
					    margin-left: -5em;
					    float: left;
					    width: 5em;padding-top: 12px;"><i class="fa fa-pencil" style="font-size: 40px;color: #4CAF50;"></i></a>
		       		<%} else if(isArticle && "C".equals(caisseMvmP.getOpc_article().getDestination())){ %>
		       			<a href="javascript:" class="btn btn-default btn-xs shiny icon-only yellow mnu_td" tp="COM" style="    height: 5em;
					    margin-left: -5em;
					    float: left;
					    width: 5em;padding-top: 12px;"><i class="fa fa-comment-o" style="font-size: 40px;"></i></a>
							       		<%} %>
							       		<%if(isArticle || isMenu){ %>
		       			<a href="javascript:" class="btn btn-danger btn-xs shiny icon-only white mnu_td" style="margin-right: 4px;height: 5em;width: 5em;padding-top: 12px;" tp="DEL"><i class="fa fa-times" style="font-size: 40px;"></i></a>
		       		<%} %>	
	       		</td>
	       </tr>
	       <%
	       }
	   }
	}
	
	if(CURRENT_COMMANDE.getMax_idx_client() > 1){%>
 		 <tr style="color: black;background-color: #eeeeee;height: 22px;" class="menu-root-style">
	     	<td colspan="2" align="right" style="font-weight: bold;font-size: 2em;">SOUS TOTAL</td>
	     	<td></td>
	     	<td align="right" style="font-weight: bold;font-size: 2em;"><%=BigDecimalUtil.formatNumber(sousTotal) %></td>
	     </tr>
   <%
	}
	
	List<CaisseMouvementOffrePersistant> listOffres = RazService.getListOffre(CURRENT_COMMANDE);
   	
   // Les offres
   if(listOffres != null && listOffres.size() > 0){
	   %>
       <tr style="color: green;" class="menu-root-style">
       		<td colspan="4" align="center">Offres</td>
       </tr>
       <%
	   
	   for (CaisseMouvementOffrePersistant offreDet : listOffres) {
	       if(offreDet.getIs_annule() != null && offreDet.getIs_annule()){
	           continue;
	       }
	       Long idOffre = offreDet.getOpc_offre().getId();
	       String libelleOffre = offreDet.getOpc_offre().getLibelle();
	       boolean idMajoration = offreDet.getMtt_reduction().compareTo(BigDecimalUtil.ZERO)<0;
      		String params = "cd="+idOffre+"&elm="+offreDet.getOpc_offre().getId()+"&tp=OFFRE";
	       %>
	      <tr style="color: <%=idMajoration?"green":"red" %>;" par="<%=params%>" class="ligne-style">
	       		<td><%="**"+libelleOffre %></td>
	       		<td></td>
	       			       		
	       		<%
	       		String mtt = idMajoration ? BigDecimalUtil.formatNumber(offreDet.getMtt_reduction().abs()) : "-"+BigDecimalUtil.formatNumber(offreDet.getMtt_reduction());
	       		%>
	       		<td align="right"><%=mtt %></td>
	       		<td></td>
	       </tr>
	       <%
	   }
   }
%>     
</table>  
</div>
</div>
<style>
.classlink{
background-image: linear-gradient(to right, #f31606 0%, #c2db56 51%, #ec1e09 100%) !important;
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
		// Commande
		var widowHeight = $(window).height();
		var ratioArray = getWindowRatioZoom();
		widowHeight = parseInt(widowHeight+(widowHeight/ratioArray[1]));
		$("#left-div").css("height", (widowHeight-103)+"px");
		$("#div_detail_cmds").css("height", (widowHeight-103)+"px");
		//Scroll
		var selectedTr = $('#cmd-table tr[isSel]');
		if(selectedTr && selectedTr.length > 0){
			selectedTr.get(0).scrollIntoView();
		}
		<%
		// List growl message
		List<GrowlMessageBean> listGrowlMessage = MessageService.getListGrowlMessageBean();
		if((listGrowlMessage != null) && (listGrowlMessage.size() > 0)){
			for(GrowlMessageBean growlBean : listGrowlMessage) { %>
				showPostAlert("<%=growlBean.getTitle()%>", "<%=growlBean.getMessage() %>" , "<%=growlBean.getType().toString()%>");
			<%}
		}
		MessageService.clearMessages();
		%>
		
		<%if(request.getAttribute("NXT_STEP") != null){%>
			$("#btn-wizard-next").trigger("click");
		<%} else if(request.getAttribute("UP_MNU") != null){%>
			$("#up_btn").attr("params", 'up=<%=request.getAttribute("UP_MNU")%>').show().trigger("click");
			$("#up_btn").removeAttr("params");
		<%}%>
		<%if(request.getAttribute("PAGE_JS") != null){%>
			<%=request.getAttribute("PAGE_JS")%>
		<%}%>
			
		    $("#detialDev").hide();
		    
		    $(document).off('click', '#detailsBott').on('click', '#detailsBott', function(){
		 //   	var text = $("#detialDev").attr("display")
		     //	alert("----------------->"+$("#detialDev").attr("display"));
		        $(".dataill-montant").toggle();
		        if($("#font_ow").hasClass('fa-shopping-cart')){
		           $("#font_ow").attr('class', "fa fa-remove");
		           $("#font_ow").css("color", "red");
		        }
		        else{
		           $("#font_ow").attr('class', "fa fa-shopping-cart");
		           $("#font_ow").css("color", "black");
		        }   
		        	
		            
		    });
		    $(document).off('click', 'a').on('click', 'a', function(){
		    	$(this).css("background-color", "yellow");
		    });
	});

	
</script>

<jsp:include page="/commun/print-local.jsp" />

<!-- HTML !-->



<div class="dataill-montant" style="    width: 258px;
    position: fixed;
    color: white;
    left: 5px;
    bottom: 10px;
    background-image: linear-gradient(to right, #121111 0%, #603535 51%, #c41b3f 100%);
    border: 4px solid transparent;
    border-image: linear-gradient(188deg, #000000 , #dc190b);
    border-image-slice: 1;
    border-radius: 10px;
    height: 140px;">
	<span style="font-weight: bold;font-size: 50px;margin-left: 15%;line-height: 65px;"> 
		<%=CURRENT_COMMANDE!=null?BigDecimalUtil.formatNumberZeroBd(CURRENT_COMMANDE.getMtt_commande_net()):""%>
	</span>
	
</div>


 