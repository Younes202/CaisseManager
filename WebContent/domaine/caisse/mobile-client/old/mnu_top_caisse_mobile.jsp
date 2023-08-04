<!-- Page Breadcrumb -->
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="framework.model.util.FileUtil"%>
<%@page import="java.util.Map"%>
<%@page import="appli.model.domaine.administration.service.IUserService"%>
<%@page import="framework.model.beanContext.EtablissementPersistant"%>
<%@page import="framework.model.common.constante.ProjectConstante"%>
<%@page import="framework.model.common.service.MessageService"%>
<%@page import="appli.controller.domaine.administration.bean.UserBean"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementPersistant"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli.STATUT_JOURNEE"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="appli.model.domaine.personnel.persistant.EmployePersistant"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="appli.controller.domaine.caisse.ContextAppliCaisse"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.controller.ContextGloabalAppli"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="appli.controller.domaine.caisse.action.CaisseWebBaseAction"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>

<%
UserBean userBean = (UserBean)MessageService.getGlobalMap().get(ProjectConstante.SESSION_GLOBAL_USER);
boolean isAvailableConnexion = (userBean != null && !userBean.getLogin().equals("CLIENT_GEN"));
%>
	<div id="mnu_r_top">
		<ul>
	          <li>  
	          	<std:link id="cmd_histo_lnk" classStyle="" targetDiv="right-div" style="color:black;text-align:left;" action="caisse.clientMobile.initHistorique" tooltip="Mes commandes">
	          		<i class="fa fa-history"></i> MES COMMANDES
	          	</std:link>
	          </li>
            <li>
              	<std:link classStyle="" targetDiv="right-div" style="color:#007bd5;text-align:left;" action="caisse.clientMobile.merge_compte" tooltip="Mon compte">
					<i class="fa fa-gears"></i> MON COMPTE 
				</std:link>
		  	</li> 
             <li>
				<std:link style="color:#3f51b5;text-align: left;" id="ets_lnk" action="caisse.clientMobile.loadEts" targetDiv="right-div" classStyle="" tooltip="Etablissements">
					 <i class="fa fa-building"></i> ETABLISSEMENTS
				</std:link>
			  </li>
		<%-- 	  <%if(ContextAppli.getUserBean().getIsMultiMobileEnv()){%>
				  <li>
					<std:link style="color:#3f51b5;text-align: left;" action="caisse.clientMobile.loadApp" targetDiv="right-div" classStyle="" tooltip="APPLICATIONS">
						 <i class="fa fa-building"></i> APPLICATIONS
					</std:link>
				  </li>
			  <%} %> --%>
			  <li>
		     	<a href='<%=ControllerUtil.getUserAttribute("URL_ABONNE", request) %>' style="line-height: 35px;
				    color: #bc00dc;">
					<i class="fa fa-mail-reply" style="font-size: 22px;"></i> Retour au site
				</a>
			</li>	
	         <li>
	         	<a href="javascript:" id="delogCli_lnk" style="color: red;" title="Quitter">
	         		<i class="fa fa-sign-out"></i> DÉCONNEXION
	         	</a>
	         </li>
      	</ul>
	</div>
	
	
<div class="page-header position-relative page-header-fixed" style="height: 50px;left: 0px;top: -0px !important;<%=StringUtil.isEmpty(CaisseWebBaseAction.GET_STYLE_CONF("PANEL_ENETETE", null))?"background-image: linear-gradient(#fbfbfb, #ddd)":"background:"+ContextGloabalAppli.getGlobalConfig("PANEL_ENETETE")%>">
	<div class="header-title" style="padding-top: 4px;width: 100%;">
         
         <std:link id="home_lnk" style="color: black;
			    padding-top: 0px;  
			    padding-left: 7px; 
			    position: absolute;   
			    left: -9px;    
			    top: 2px;
			    height: 44px; 
			    width: 44px;" classStyle="btn btn-default btn-circle btn-lg btn-menu shiny" action="caisse-web.caisseWeb.init_home" targetDiv="right-div" icon="fa-3x fa fa-home" value="" />
         
         <std:link id="up_btn" targetDiv="right-div" style="display:none;    
         		line-height: 35px;
         		padding-left: 6px;padding-top: 4px;
         		font-size: 18px;
    			position: absolute;
    			left: 32px;" classStyle="" action="caisse-web.caisseWeb.upButtonEvent" tooltip="Monter d'un niveau">
			<i class="fa fa-chevron-up" style="font-size: 22px;"></i>
		</std:link>
                       
	        <%if(ContextAppli.getEtablissementBean() != null){ %>
	        	<div style="position: absolute;right: 40px;">
	        				
            <%
            EtablissementPersistant etsBean = ContextGloabalAppli.getEtablissementBean();
            if(etsBean == null){
            	etsBean = new EtablissementPersistant();
            }
            if(etsBean.getId() != null){
                 Map<String, byte[]> imagep = ServiceUtil.getBusinessBean(IUserService.class).getDataImage(etsBean.getId(), "restau");
                 if(imagep.size() > 0){
                %>
						<img src="data:image/jpeg;base64,<%=FileUtil.getByte64(imagep.entrySet().iterator().next().getValue())%>" 
							alt="Caisse manager" style="height: 40px;margin-top:-9px;border-radius: 19px;" />                        
                 <%
                  } 
            }
             %>
	        	
	        		<span style="color: #0d0c0d;
    font-size: 22px;
    line-height: 40px;
    text-align: center;
    text-transform: uppercase;"><%=ContextAppli.getEtablissementBean().getRaison_sociale() %></span>
	        	</div>
	        <%} %>
     	</div>
     	
     <!--Header Buttons-->
     <div style="position: absolute;right: 7px;top: 10px;">
     	<a onclick="$('#mnu_r_top').toggle(100);">
             <i class="fa fa-reorder" style="font-size: 27px;"></i>
         </a>
	 </div>                          
     <!--Header Buttons End-->
 </div>
 <!-- /Page Header -->