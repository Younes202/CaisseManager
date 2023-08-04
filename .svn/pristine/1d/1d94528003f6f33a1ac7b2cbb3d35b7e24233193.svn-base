<%@page import="appli.controller.domaine.util_erp.ContextAppli.SOFT_ENVS"%>
<%@page import="framework.model.beanContext.EtablissementPersistant"%>
<%@page import="framework.model.beanContext.AbonnementBean"%>
<%@page import="framework.model.util.MenuMappingService"%>
<%@page import="java.util.ArrayList"%> 
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.controller.Context"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.controller.bean.MenuBean"%>
<%@page import="java.util.List"%>

<style>

.page-sidebar .sidebar-menu li:not(.open):hover:before {
    display: block;
    background-color: #fb6e52;
}
.page-sidebar .sidebar-menu li.active:not(.open):before {
    display: block;
}
.page-sidebar .sidebar-menu li:before {
    display: none;
    content: "";
    position: absolute;
    top: 0;
    bottom: 0;
    left: -4px;
    width: 4px;
    max-width: 4px;
    overflow: hidden;
    background-color: #2dc3e8;
}
:after, :before {
    -webkit-box-sizing: border-box;
    -moz-box-sizing: border-box;
    box-sizing: border-box;
}
.menu_href{
   
}

.custom_mnu{
    text-transform: uppercase;
    color: #262626;
}
.custom_mnu i{
    color: red;
}
.tab-add{
    position: absolute;
    right: 0px;
    top: 0px;
    width: 30px;
    padding: 3px 0px 0px 9px !important;;
    height: 19px !important;;
    line-height: 0px !important;
    margin-top: 10px !important;
    border-radius: 8px;
    color: #999 !important;
    font-size: 10px !important;
}
.tab-add:hover {
	background-color: #ddd !important;
	color: red !important;
}
</style>

<%
EtablissementPersistant etsBean = ContextGloabalAppli.getEtablissementBean();
if(etsBean == null){
	etsBean = new EtablissementPersistant();
}
%>

<script type="text/javascript">
$(document).ready(function (){
	$(".sidebar-menu a").click(function(){
		if($(this).attr("url")){
			window.location.href = "#lmnu="+$(this).attr("url") + '&rdm='+randString(4);
		}
	});
	
	$(".sidebar-menu").css("overflow", "initial");
	
	$(".searchinput").keyup(function(){
		var txt = $(this).val();

		if($.trim(txt) == ''){
			$(".slimScrollDiv li").show();
			return;
		}
		$(".slimScrollDiv li").hide();
		
		$(".slimScrollDiv span").each(function(){
			if($(this).html().toUpperCase().indexOf(txt.toUpperCase()) != -1){
				$(this).parents("li").removeAttr("class").show();
				$(this).parents("li:first").attr("class", "open");
			}
		});
	});
	
	$(document).off('click', '#sidebar-collapse').on('click', '#sidebar-collapse', function(){
		if($(this).attr("class").indexOf("active") != -1){
			$("#mnu_short").css("height", "170px");
			$("#mnu_short a").css("width", "38px");
			
			$("#mnu_short a").find("span").each(function(){
				$(this).attr("title", $(this).html());	
				$(this).html("");	
			});
		} else{
			$("#mnu_short").css("height", "95px");
			$("#mnu_short a").css("width", "47%");
			
			$("#mnu_short a").find("span").each(function(){
				$(this).html($(this).attr("title"));	
			});
		}
	});
});

</script>
 
 <%
 String soft = StrimUtil.getGlobalConfigPropertie("context.soft");
 AbonnementBean abnBean = ContextGloabalAppli.getAbonementBean();
 if(abnBean == null){
	 abnBean = new AbonnementBean();
 }
	 boolean isCompta = abnBean.isOptCompta();
	 boolean isRh = abnBean.isOptRh();
	 boolean isCommercial = abnBean.isOptCommercial();
	 boolean isLivraison = abnBean.isOptLivraison();
	 boolean isControle = abnBean.isOptControle();
	 boolean isStock = abnBean.isOptStock();
	 boolean isSatCuisine = abnBean.isSatCuisine(); 
	 boolean isOptPlusEtsCentrale = abnBean.isOptPlusEtsCentrale();
	 boolean isOptPlusSyncCentrale = abnBean.isOptPlusSyncCentrale();
	 boolean isOptPlusCmdLigne = abnBean.isOptPlusCmdVitrine();
 %>
 
  <!-- Page Sidebar Header-->
  <div class="sidebar-header-wrapper">
      <input type="text" class="searchinput" style="border: 1px solid #262626;
    border-radius: 15px !important;
    height: 30px;
    margin-top: 4px;background-color: #ffffff;" />
      <i class="searchicon fa fa-search"></i>
  </div>
  <!-- /Page Sidebar Header -->
  
<style>
.bloc_mnu_short{
    width: 49%;
    float: left;
    border: 1px solid #c2bbbb !important;
    margin-bottom: 3px !important;
    margin-right: 1% !important;
    cursor: pointer;
    margin-left: 0px !important;
    padding-left: 11px !important;
}
.icon_mnu_short{
    margin-left: 0px;
    font-size: 17px;
    min-width: 10px !important;
}
.span_sub_cai{
	font-size: 11px;
}
.slide-stl{
	overflow-y: scroll !important;
	margin-right: -14px !important;
}
</style>
  
  <!-- Sidebar Menu -->
  <ul class="nav sidebar-menu slide-stl">
  
  <%
boolean isCaisseApp = (SOFT_ENVS.market.toString().equals(soft) || SOFT_ENVS.restau.toString().equals(soft));
  
 List<String> mnuTop = new ArrayList<>();
 if(SOFT_ENVS.admin.toString().equals(soft)){
	 mnuTop.add("dashboard");
	 %>
	 <li id="mnu_short" style="height: 60px;">
     		<a href="javascript:" class="bloc_mnu_short btn-big" url="dashboard" style="width: 160px;background-color: <%=etsBean.getThemeDet("fond_mnu") %>">
     			<i class="menu-icon fa fa-dashboard icon_mnu_short" style="color:<%=etsBean.getThemeDet("icon_mnu1") %>;"></i>
				<span class="span_sub_cai" style="font-size: 13px;color:<%=etsBean.getThemeDet("txt_mnu1") %>;">Tableau de bord</span>       		
     		</a>
	</li>	 
	 
 <%} else{
  	  mnuTop.add("dashboard");
  
  	  if(isCaisseApp){
	  	mnuTop.add("cai-jrn");
	  }
	  mnuTop.add("agenda");
	  mnuTop.add("fichiers");
  %>

       <li id="mnu_short" style="height: 95px;">
       		<%if(Context.isMenuAvailable("dashboard", false)){ %>
	       		<a href="javascript:" class="bloc_mnu_short btn-big" url="dashboard" style="<%=isCaisseApp?"":"width:99%;"%>background-color: <%=etsBean.getThemeDet("fond_mnu1") %>">
	       			<i class="menu-icon fa fa-dashboard icon_mnu_short" style="color:<%=etsBean.getThemeDet("icon_mnu1")%>;"></i>
					<span class="span_sub_cai" style="color:<%=etsBean.getThemeDet("txt_mnu1") %>;"><%=isCaisseApp?"T.D.B":"TABLEAU DE BORD" %></span>       		
	       		</a>
       		<%} %>
       		<%if((SOFT_ENVS.market.toString().equals(soft)
       				|| SOFT_ENVS.restau.toString().equals(soft)
       				) && Context.isMenuAvailable("cai-jrn", false)){ %>
	       		<a href="javascript:" class="bloc_mnu_short btn-big" url="cai-jrn" style="background-color: <%=etsBean.getThemeDet("fond_mnu1") %>">
	       			<i class="menu-icon fa fa-laptop icon_mnu_short" style="color:<%=etsBean.getThemeDet("icon_mnu1")%>;"></i>
					<span class="span_sub_cai" style="color:<%=etsBean.getThemeDet("txt_mnu1") %>;">CHIFFRES</span>       		
	       		</a>
       		<%} %>
       		<%if(Context.isMenuAvailable("agenda", false)){ %>
	       		<a href="javascript:" class="bloc_mnu_short btn-big" url="agenda" style="background-color: <%=etsBean.getThemeDet("fond_mnu1") %>">
	       			<i class="menu-icon fa fa-calendar icon_mnu_short" style="color:<%=etsBean.getThemeDet("icon_mnu1")%>;"></i>
					<span class="span_sub_cai" style="color:<%=etsBean.getThemeDet("txt_mnu1") %>;">AGENDA</span>       		
	       		</a>
       		<%} %>
       		<%if(Context.isMenuAvailable("fichiers", false)){ %>
	       		<a href="javascript:" class="bloc_mnu_short btn-big" url="fichiers" style="background-color: <%=etsBean.getThemeDet("fond_mnu1") %>">
	       			<i class="menu-icon fa fa-folder-open icon_mnu_short" style="color:<%=etsBean.getThemeDet("icon_mnu1")%>;"></i>
					<span class="span_sub_cai" style="color:<%=etsBean.getThemeDet("txt_mnu1") %>;">DOCS</span>        		
	       		</a>
       		<%} %>
       </li>     
      <%
 }
  
		List<MenuBean> listMenu = MenuMappingService.mapMenu;
      	Integer oldLevel = null;
      	String currModule = "XX";
      	String currSousModule = "XX";
      	//
		for(MenuBean menuBean : listMenu){
			String leftMenuId = (String)menuBean.getId();
			boolean isNode = !menuBean.isSheet();
			boolean isTemporary = (menuBean.getLinkText() != null && menuBean.getLinkText().startsWith("**"));
			boolean isAvailable = true; 
			
			if(mnuTop.contains(leftMenuId)){
				continue;
			}
			
			if(!menuBean.getIsVisible() || menuBean.getLevel() == 0){
				continue;
			}
			if(!Context.isMenuAvailable(leftMenuId, isNode)){
				isAvailable = false;
			}
			
			if( menuBean.getLevel() == 1){
				currModule = leftMenuId;
			} else if(menuBean.getLevel() == 2){
				currSousModule = leftMenuId;
			}
						
			boolean isMnuAbonne = true;
			if((currModule.equals("compta") && !isCompta) 
						|| (currModule.equals("paaie") && !isRh)
						|| (currModule.equals("stock") && !isStock)	
						|| (currModule.equals("commercial") && !isCommercial)	
						|| (currModule.equals("livr") && !isLivraison)
						|| (currModule.equals("ctrl") && !isControle)	
						|| (currModule.equals("livr-cmd") && !isOptPlusCmdLigne)
					){
				isMnuAbonne = false;
			} 
			else if(currSousModule.equals("suivicuisine") && !isSatCuisine){
				isMnuAbonne = false;
			}
			else if(!isCommercial && !isStock && leftMenuId.equals("cai-pers-client")){
				continue;
			}
			else if(!isOptPlusEtsCentrale && leftMenuId.equals("centrale-sync")){
				continue;
			}
			else if(!isOptPlusEtsCentrale && !isOptPlusSyncCentrale && leftMenuId.equals("stock-dem-transf")){
				continue;
			}
			
			if(oldLevel != null){
				if(oldLevel > menuBean.getLevel()){
					while(oldLevel > menuBean.getLevel()){	%>
				 		 </li>
				  		</ul>			
						<%
						oldLevel--;
					}
				} else if(oldLevel == menuBean.getLevel()){%>
					</li>	
			<%	}
			}
			boolean isSubLevel2 = menuBean.getLevel()==2 && StringUtil.isEmpty(menuBean.getUrl());
			
			String styleMnu = "";
			String colorIconMnu = "";
			String txtMnu = "";
			
			if(isMnuAbonne){
				if(isTemporary){
					styleMnu = "#B2EBF2";
					colorIconMnu = "#B2EBF2";
					txtMnu = "#B2EBF2";
				} else{
					if(isNode){
						
					}
					styleMnu = "background-color:"+etsBean.getThemeDet("fond_mnu"+menuBean.getLevel())+" !important";
					colorIconMnu = "color:"+etsBean.getThemeDet("icon_mnu"+menuBean.getLevel())+" !important";
					txtMnu = "color:"+etsBean.getThemeDet("txt_mnu"+menuBean.getLevel())+" !important";
				}
			} else{
				styleMnu = "background-color:transparent;color:red !important;";
				colorIconMnu = "color:gray !important";
				txtMnu = "color:gray !important";
			}
			
			%>
			 <li id="li_<%=menuBean.getId()%>" style="display:<%=isAvailable ? "":"none"%>;">
			 
			 <%if(isMnuAbonne && isAvailable){ %>
				<a href="javascript:" style='<%=isSubLevel2?"height: 21px;line-height: 21px;":"" %><%=styleMnu%>' <%=isNode ?"":" url='"+menuBean.getId()+"'" %> class="<%=isNode ? "menu-dropdown menu_href":"" %>" title="<%=StringUtil.getValueOrEmpty(menuBean.getPageTitle()) %>">
					<%if(menuBean.getLevel() > 1){ %>
						<i class="menu-icon fa <%=menuBean.getIcon() %>" style='<%=colorIconMnu%>'></i> 
					<%} else{ %>
						<i class="menu-icon fa <%=menuBean.getIcon() %> tree-loading" style="margin-left: 0px;<%=colorIconMnu%>;"></i> 
					<%} %>
					
					<span class="menu-text" style='<%=txtMnu%>'><%=isTemporary ? menuBean.getLinkText().substring(2) : menuBean.getLinkText() %></span>
					<%if(isNode){ %>
					<i class="menu-expand" style='<%=isSubLevel2?"margin-top: -9px;":""%>;<%=colorIconMnu%>'></i>
					<%} %>
				</a>
				<%if(!isNode){ %>
					<a href="javascript:" url="<%=menuBean.getId() %>&ntab=1&ttl=<%=menuBean.getLinkText() %>" style="background-color: transparent !important;" class="tab-add">
						<i class="fa fa-share-square-o" style="<%=colorIconMnu%>;"></i>
					</a>
				<%} %>
			<%} else{ %>				
				<a href="javascript:" class=<%=isNode ? "menu-dropdown menu_href":"" %>"  style="color:<%=styleMnu%>" title="<%=StringUtil.getValueOrEmpty(menuBean.getPageTitle())%>">
					<%if(menuBean.getLevel() > 1){ %>
						<i class="menu-icon fa <%=menuBean.getIcon() %>" style='<%=colorIconMnu%>;'></i> 
					<%} else{ %>
						<i class="menu-icon fa <%=menuBean.getIcon() %> tree-loading" style="margin-left: 0px;<%=colorIconMnu%>;"></i> 
					<%} %>
					
					<span class="menu-text" style='<%=styleMnu%>;<%=txtMnu%>'><%=menuBean.getLinkText() %></span>
					<%if(isNode){ %>
						<i class="menu-expand" style="<%=colorIconMnu%>"></i>
					<%} %>
				</a>
			<%} %>
				
				<% 
				if(isNode){ %>
					<ul class="submenu" style='<%=menuBean.getLevel()==2?"overflow:auto;max-height: 240px;":""%>'>
				<%} %>
			<%
			oldLevel = menuBean.getLevel();
		}
		%>
		</li>
	</ul>
 </ul>
<!-- /Sidebar Menu -->

