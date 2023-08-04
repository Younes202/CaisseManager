<%@page import="framework.model.util.MenuMappingService.RIGHT_ENUM"%>
<%@page import="framework.model.util.MenuMappingService"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="appli.controller.domaine.habilitation.bean.ProfileBean"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="framework.model.beanContext.AbonnementBean"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%> 
<%@page import="framework.controller.bean.MenuBean"%>
<%@page import="framework.controller.bean.RightBean"%>
<%@page import="framework.model.common.constante.ProjectConstante"%>
<%@page import="appli.model.domaine.habilitation.persistant.ProfileMenuPersistant"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="framework.model.common.util.NumericUtil"%>
<%@page import="java.util.List"%>
<%@taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@page errorPage="/commun/error.jsp"  %>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
 <%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<%Long profileId = (Long)ControllerUtil.getMenuAttribute("profileId", request); %>
<%
ProfileBean profileBean = (ProfileBean)ControllerUtil.getMenuAttribute("profileBean", request);
boolean isAccessBackOff = (profileBean != null && BooleanUtil.isTrue(profileBean.getIs_backoffice()));
boolean isAccessCaisse = (profileBean != null && BooleanUtil.isTrue(profileBean.getIs_caisse()));
%>

<script type="text/javascript">
$(document).ready(function() {
	$(".inputROnly").each(function(){
	    if($(this).text()=='Oui'){
	        $(this).replaceWith('<i style="font-size:16px;font-weight:bold;color:green;" class="fa fa-check-square-o"></i>');
	    } else{
	        $(this).replaceWith('<i style="font-size:16px;font-weight:bold;color:red;" class="fa fa-times"></i>');
	    }
	});
});
</script>

<!-- Page Breadcrumb -->
 <div class="page-breadcrumbs breadcrumbs-fixed">
     <ul class="breadcrumb">
         <li>
             <i class="fa fa-home"></i>
             <a href="#">Accueil</a>
         </li>
         <li>Gestion de hab</li>
         <li>Fiche d'habilitation
		</li>
         <li class="active">Edition</li>
     </ul>
 </div>

<div class="page-header position-relative">
      <div class="header-title" style="padding-top: 4px;">
        <std:link actionGroup="U" classStyle="btn btn-default" action="hab.habilitation.work_init_update" workId="<%=profileId.toString() %>" icon="fa-3x fa-pencil" tooltip="Modifier" />
		<std:link classStyle="btn btn-default" action="hab.profile.work_find" params="bck=1" icon="fa fa-3x fa-mail-reply-all" tooltip="Retour &agrave; la recherche" />
      </div>
      <!--Header Buttons-->
      <jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
      <!--Header Buttons End-->
  </div>
  <!-- /Page Header -->

<div class="page-body">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>
	
	
	
	<!-- widget grid -->
	
	<div class="widget">
	<std:form name="data-form">
         
       <div class="row">
        <div class="col-lg-12 col-sm-12 col-xs-12">
              <div class="tabbable">
                    <ul class="nav nav-tabs" id="myTab">
						<li><a data-toggle="tab" href="#descripton" wact="<%=EncryptionUtil.encrypt("hab.profile.work_edit")%>"> Fiche </a></li>
						<%if(isAccessBackOff){ %>
							<li class="active"><a data-toggle="tab" href="#mouvement" wact="<%=EncryptionUtil.encrypt("hab.habilitation.work_edit")%>"> Droits back-office </a></li>
						<%} %>
						<%if(isAccessCaisse){ %>
							<li><a data-toggle="tab" href="#mouvement" wact="<%=EncryptionUtil.encrypt("hab.habilitation.work_edit")%>" params="isCais=1"> Droits caisse</a></li>
						<%} %>	
					</ul>
                </div>
          </div>
      </div>
         
         <div class="widget-body">
<%
List<RightBean> listeRights = MenuMappingService.mapRights;
List<MenuBean> listMenu = (List<MenuBean>)request.getAttribute("listMenu");
List<ProfileMenuPersistant> listProfileMenu = (List<ProfileMenuPersistant>)request.getAttribute("listProfileMenu");
String disable = ControllerUtil.isEditionWritePage(request) ? "false" : "true";
Map<String, List<String>> mapExcludedRights = MenuMappingService.mapExcludedRights;
AbonnementBean abnBean = ContextGloabalAppli.getAbonementBean();
%>

	<script type="text/javascript">
		/*
		^=start with	*=contains 		$=end with
		*/

		$(document).ready(function (){
			$("a[id^='fav_lnk_']").click(function(){
				if($(this).attr("id").indexOf("_1") != -1){
					$(this).css("color", "black");
					$(this).find("i").attr("class", "fa fa-fw fa-heart");
				} else{
					$(this).css("color", "red");
					$(this).find("i").attr("class", "fa fa-fw fa-heart-o");
				}
			});
			
			// Alternate color
			alternateHighlightRows('listHabilitation');
			
			$("#check_p").click(function(){
				$("input[id^='chck_']").prop('checked', $(this).prop('checked'));
				$("input[id='check_all']").prop('checked', $(this).prop('checked'));
			});
			
			$("input[id='check_all']").click(function(){
				
				var mnuId = $(this).attr("style");
				var check = $(this).prop('checked');
				
				$("input:checkbox[id$='_"+mnuId+"_']").each(function() { 
					 $(this).prop("checked", check);
				});
			});
		});

		/**
		* Update checks
		*/
		function updateCheckboxes(check, type, mnuId){
			// Check edite in all casers
			if(check.checked){
				var checkEditId  = "chck_<%=RIGHT_ENUM.RIGHT_SHOW.getType() %>_" + mnuId + "_";
				$("#"+checkEditId).prop("checked", true);
			}
			// Uncheck all if edit is unchecked
			if(type == '<%=RIGHT_ENUM.RIGHT_SHOW.getType()%>' && !check.checked){
				 $("input:checkbox[id$='_" + mnuId + "_']").each(function() {
					 $(this).prop("checked", false);
				 });
			} else {
				<%
				for(RightBean rightBean : listeRights){
					if(StringUtil.isNotEmpty(rightBean.getParentId())){	%>
						if(type == '<%=rightBean.getParentId()%>'){
							if(!check.checked){
								// Update groupe
								var checkId  = "chck_<%=rightBean.getId() %>_" + mnuId + "_";
								$("#"+checkId).prop("checked", false);
								return;
							}
						}
						if(type == '<%=rightBean.getId()%>'){
							if(check.checked){
								// Update groupe
								var checkId  = "chck_<%=rightBean.getParentId() %>_" + mnuId + "_";
								$("#"+checkId).prop("checked", true);
								return;
							}
						}
					<%}
				}
				%>
			}
		}

		/**
		* Call create or update
		*/
		function updateProfile(mnuId){
			var params = "";
			<%
			for(RightBean rightBean : listeRights){%>
				var checkId  = "chck_<%=rightBean.getId()%>_" + mnuId + "_";
				params += "<%=rightBean.getId()%>:"+($("#"+checkId).attr('checked') ? '1' : '0') + ";";
			<%}	%>

			var url = 'front?w_uact=<%=EncryptionUtil.encrypt("hab.habilitation.update_elmnt")%>&profileMenu.menu_id='+mnuId+'&rights=' + params;
			callBackJobAjaxUrl(url, true);
		}
	</script>

		<br>
		<table id="listHabilitation" align="center" width="100%" border="0" class="sortable" cellpadding="0" cellspacing="0">
			<tr>
			 <%if(ControllerUtil.isEditionWritePage(request)){%>
				<th width="50" style="text-align: center;">
					<std:checkbox id="check_p" name="check_p"/> 
				</th>
			 <%} %>
				<th height="30">&nbsp;&nbsp;Menu</th>

			<%for(RightBean rightBean : listeRights){
				if(rightBean.isGlobal()){ %>
					<th width="90" align="center"><%=rightBean.getLabel() %></th>
			<%	}
			} %>
				<th align="center"><%=StrimUtil.label("commun.other") %></th>
			</tr>
		<%
				boolean isCompta = ContextGloabalAppli.getAbonementBean().isOptCompta();
				 boolean isRh = ContextGloabalAppli.getAbonementBean().isOptRh();
				 boolean isCommercial = ContextGloabalAppli.getAbonementBean().isOptCommercial();
				 boolean isLivraison = ContextGloabalAppli.getAbonementBean().isOptLivraison();
				 boolean isControle = ContextGloabalAppli.getAbonementBean().isOptControle();
				 boolean isStock = ContextGloabalAppli.getAbonementBean().isOptStock();

				 boolean isSatCuisine = ContextGloabalAppli.getAbonementBean().isSatCuisine();
				//
				String currModule = "XX";
				String currSousModule = "XX";
				String favUser = ContextAppli.getUserBean().getFavoris_nav();
				//
				for (MenuBean menu : listMenu) {
					String leftMenuId = (String) menu.getId();
					String mnuId = menu.getId();

					String lnkTxt = menu.getLinkText();
					
					if (menu.getId().equals("dashboard")){
						lnkTxt = "Tableau de bord";
						menu.setSheet(true);
					} else if (menu.getId().equals("cai-jrn")){
						lnkTxt = "Chiffres jounrée";
						menu.setSheet(true);
					} else if (menu.getId().equals("agenda")){
						lnkTxt = "Agenda";
						menu.setSheet(true);
					} else if (menu.getId().equals("fichiers")){
						lnkTxt = "Fichiers";
						menu.setSheet(true);
					}
					
					if (StringUtil.isEmpty(lnkTxt)) {
						continue;
					}

					if (menu.getLevel() == 1) {
						currModule = leftMenuId;
					} else if (menu.getLevel() == 2) {
						currSousModule = leftMenuId;
					}
					boolean isMnuAbonne = true;
					if ((currModule.equals("compta") && !isCompta) || (currModule.equals("paaie") && !isRh)
							|| (currModule.equals("stock") && !isStock)
							|| (currModule.equals("commercial") && !isCommercial)
							|| (currModule.equals("livr") && !isLivraison)
							|| (currModule.equals("controle") && !isControle)) {
						isMnuAbonne = false;
					} else if (currSousModule.equals("suivicuisine") && !isSatCuisine) {
						isMnuAbonne = false;
					} else if (isCommercial && isStock && leftMenuId.equals("cai-pers-client")) {
						continue;
					}

					int level = menu.getLevel();
					List<String> listExludedRights = mapExcludedRights.get(mnuId);
					Map<String, Integer> mapSelectedRights = new HashMap<String, Integer>();
					//
					if (level > 0) {
		%>
				<tr>
			<%	String espace = "";
				while(level != 1){
		    		espace = "--" + espace;
		    		level--;
		    	}
				// Restaure checked
				if(listProfileMenu != null){
					for(ProfileMenuPersistant pmenuPers : listProfileMenu){
						if(pmenuPers.getMenu_id().equals(mnuId)){
							String[] rightsArray = StringUtil.getArrayFromStringDelim(pmenuPers.getRights(), ";");
							//
							if(rightsArray != null){
								for(String r : rightsArray){
									String[] valuesArray = StringUtil.getArrayFromStringDelim(r, ":");
									 if(valuesArray.length > 1){
										// Id right and value
										String right = valuesArray[0];
										int value = NumericUtil.toInteger(valuesArray[1]);
										mapSelectedRights.put(right, value);
									 }
								}
							}
						}
					}
				}

				//
				String img = !menu.isSheet() ? "+" : "-";
				boolean isSheet = menu.isSheet();
				String label = !isSheet ? ("<span class='back-sheet'></span><b style='color: blue;'>"+img + espace + lnkTxt+"</b>")
																		: ("<span class='back-folder'></span>"+img + espace + "&nbsp;&nbsp;&nbsp;" + lnkTxt);
				%>
				 <%if(ControllerUtil.isEditionWritePage(request)){%>
				<td align="center">
				<%if(isSheet){%>
					<std:checkbox id="check_all" name="check_all" style="<%=mnuId %>" disable="<%=disable %>"/>
				<%} %>
				</td>
				<%} %>
				<td valign="bottom" align="left">
					<%if(StringUtil.isNotEmpty(menu.getUrl()) && isSheet){
						boolean isFav = (favUser != null && favUser.indexOf(";"+menu.getId()+";") != -1); %>
						<std:link classStyle="" id='<%="fav_lnk_"+(isFav?"1":"0") %>' style='<%="color:"+(isFav?"red;":"black;") %>' targetDiv="xxxx" action="admin.user.manageFavNav" params='<%="mnu="+menu.getId()+"&wibaj=1&isfav="+isFav %>' tooltip='<%=isFav?"Retirer des fouvoris":"Ajouter au favoris" %>' icon='<%=isFav?"fa-fw fa-heart":"fa-fw fa-heart-o" %>' />
					<%} %>	
					<%=label %>
				</td>

				<%
				// Add global rights
				for(RightBean rightBean : listeRights){
					if(rightBean.isGlobal()){%>
						<td align="center">
							<%if(isSheet &&
									!(
										(listExludedRights != null)
										&& (listExludedRights.contains(rightBean.getId()) || listExludedRights.contains(rightBean.getParentId()))
									  )
								){
								// Rights name
								String rightFunction = "updateCheckboxes(this, '"+rightBean.getId()+"', '"+mnuId+"')";
								// Checks name
								String checkId = "chck_" + rightBean.getId() + "_" + mnuId + "_";
								String isChecked = ((mapSelectedRights.get(rightBean.getId()) != null) && (mapSelectedRights.get(rightBean.getId()) == 1)) ? "true" : "";
							%>
								<std:checkbox autoValue="false" id="<%=checkId %>" name="<%=checkId %>" disable="<%=disable %>" checked="<%=isChecked %>" onClick="<%=rightFunction %>"/>
							<%} %>
						</td>
					<%
					}
				}
				%>
				<td>
				<%
				if(isSheet){%>
					<%String[] otherRight = menu.getAdditionalRrights();
					if(otherRight != null){
						for(String r : otherRight){
							RightBean rightBean = MenuMappingService.getRightBean(MenuMappingService.mapRights, r);
							if(rightBean != null){
								String checkId = "chck_" + rightBean.getId() + "_" + mnuId + "_";
								String isChecked = ((mapSelectedRights.get(rightBean.getId()) != null) && (mapSelectedRights.get(rightBean.getId()) == 1)) ? "true" : "";
								String rightFunction = "updateCheckboxes(this, '"+rightBean.getId()+"', '"+mnuId+"')";
								%>
								<std:checkbox autoValue="false" id="<%=checkId %>" name="<%=checkId %>" disable="<%=disable %>" checked="<%=isChecked %>" onClick="<%=rightFunction %>"/>
								&nbsp; [<b><%=rightBean.getLabel() %></b>]
								<br>
							<%}
							}
						}
					%>
				<%}	%>
				</td>
				</tr>
			<%
			}
		}
		%>
		</table>
		<br /><br />
			<div class="form-actions">
				<div class="row" style="text-align: center;" class="col-md-12" id="action-div">
						<std:button actionGroup="M" classStyle="btn btn-success" action="hab.habilitation.work_merge" workId="<%=profileId.toString() %>" icon="fa-save" value="Sauvegarder" />
					</div>
				</div>
		</div>
			
			</std:form>
		</div>		
</div>

