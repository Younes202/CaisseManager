<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="appli.controller.domaine.caisse.ContextAppliCaisse"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>

<%
boolean isDroitOuvrirJrn = StringUtil.isTrueOrNull(""+ControllerUtil.getUserAttribute("RIGHT_CLOJRN", request));
boolean isCaisseVerouille = ControllerUtil.getMenuAttribute("IS_CAISSE_VERROUILLE", request) != null;
boolean isCaisseNotFermee = ContextAppliCaisse.getJourneeCaisseBean() != null && ContextAppliCaisse.getJourneeCaisseBean().getStatut_caisse().equals("O");
boolean isJourneeCaisseOuverte = !isCaisseVerouille && isCaisseNotFermee;
boolean isJourneeOuverte = (ContextAppliCaisse.getJourneeBean() != null && ContextAppliCaisse.getJourneeBean().getStatut_journee().equals("O"));
%>

<table style="text-align: center;width: 100%;margin-top: 30%;">
<%if (!isJourneeOuverte){ %>
          <% if(isDroitOuvrirJrn){ %>
			<tr style="height: 150px;">
				<td>
			<std:linkPopup actionGroup="C" classStyle="" 
			style="font-size: 26px;
				    border: 1px solid #eeeeee;
				    font-weight: bold;
				    height: 150px;
				    border-radius: 30px;
				    padding-top: 27px;
				    padding-bottom: 27px;
				    padding-left: 15px;
				    padding-right: 15px;
				    text-decoration: none;
				    background-color: #dce775;
				    box-shadow: 7px 9px 36px 1px;"
			action="caisse.journee.work_init_create" tooltip="Ouvrir la journée">
				<i class="fa fa-calendar-o" style="color: green !important;"></i> OUVRIR LA JOURNÉE
			</std:linkPopup>
			</td>
		</tr>
		<%} %>	
<% } else if(isCaisseVerouille){ %> 
                 <tr style="height: 150px;">
                 	<td>
                   		<std:link classStyle="" id='delock_lnk' style='
                   				font-size: 47px;
				    border: 1px solid #eeeeee;
				    font-weight: bold;
				    height: 150px;
				    border-radius: 30px;
				    padding-top: 27px;
				    padding-bottom: 27px;
				    padding-left: 15px;
				    padding-right: 15px;
				    text-decoration: none;
				    background-color: #ffb74d;
				    box-shadow: 7px 9px 36px 1px;' targetDiv="menu-detail-div" tooltip='Dé-verrouiller'>
                   			<i class="fa fa-unlock" style="color: red !important;"></i> DÉ-VERROUILLER
                   		</std:link> 
                  		</td>
                  	</tr>
<% } else if(!isCaisseNotFermee){ %>
                  	<tr style="height: 150px;">
                  		<td>
                   	<a style="color:green;
                   					font-size: 26px;
					    border: 1px solid #eeeeee;
					    font-weight: bold;
					    height: 150px;
					    border-radius: 30px;
					    padding-top: 27px;
					    padding-bottom: 27px;
					    padding-left: 15px;
					    padding-right: 15px;
					    text-decoration: none;
					    background-color: #dce775;
					    box-shadow: 7px 9px 36px 1px;" class="" targetdiv="generic_modal_body" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#generic_modal" wact="<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.initShift")%>" title="Shift" href="javascript:void(0);">
          	<i class="fa fa-pie-chart"></i>
          	OUVRIR LE SHIFT
        </a>
       </td>
       </tr>
       
       <% if(isDroitOuvrirJrn && isJourneeOuverte){ %>
       <tr style="height: 150px;">
       	<td>
       	<std:link action="" id="clore_lnk" style='
                   				font-size: 26px;
				    border: 1px solid #eeeeee;
				    font-weight: bold;
				    height: 150px;
				    border-radius: 30px;
				    padding-top: 27px;
				    padding-bottom: 27px;
				    padding-left: 15px;
				    padding-right: 15px;
				    text-decoration: none;
				    background-color: #ffb74d;
				    box-shadow: 7px 9px 36px 1px;' params="isReclos=1" actionGroup="C" classStyle="" tooltip="Clore la journée">
       		<i class="fa fa-calendar-o"></i>
       		CLORE LA JOURNÉE
       	</std:link>
       </td>
       </tr>	
       <%} %>
<% } %>
</table>