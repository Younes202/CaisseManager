<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp" %>


<script type="text/javascript">
$(document).ready(function (){
	$(document).off('click', "#annul_lnk").on('click', "#annul_lnk", function(){
		showConfirmDeleteBox('<%=EncryptionUtil.encrypt("caisse-web.caisseWeb.annulerCommande")%>', $(this).attr("params")+"&caisse.id="+$("#caisse\\.id").val(), $("#cmd_histo_lnk"), "Cette commande sera annulée.<br>Souhaitez-vous continuer ?", null, "Annulation commande");
	});
});
</script>

<div style="margin-top: -1px;
    overflow-x: hidden;
    overflow-y: auto;
    margin-left: -10px;">
	<std:form name="search-form">
		<a href="javascript:" id="histo_targ_link" targetDiv="right-div"></a>
		<std:link action="caisse-web.caisseWeb.initHistorique" targetDiv="right-div" style="display:none;" id="histo-lnk" />
		<std:link id="annul_cmd_lnk" style="display:none;" targetDiv="right-div" />
		
		<div class="widget-header bordered-bottom bordered-blue">
			<span class="widget-caption">Historique des commandes</span>
		</div>
		<div class="widget-body" id="div-histo">
			<div class="row">
				<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
			</div>
			
			<c:set var="encryptionUtil" value="<%=new EncryptionUtil()%>" />

			<!-- Liste des mouvements -->
			<c:forEach items="${listCmd }" var="mouvement">
				<div class="databox-right bordered-thick bordered-sky bg-white" 
								style="padding: 7px 6px 5px 7px;
									   border: 1px solid gray;
										border-radius: 9px;
										background-color: #efefef !important;
										margin-right: -8px;
										margin-bottom: 5px;">
	            	
	            	<std:linkPopup action="caisse.clientMobile.selectHistorique" classStyle="" workId="${mouvement.id}" style="text-decoration: underline;font-size:12px;font-weight:bold;">
						COMMANDE : ${mouvement.ref_commande }<br> [${mouvement.is_tovalidate ? '<span style="color:red;font-weight:normal;">NON VALIDEE</span>' : '<span style="color:green;font-weight:normal;">VALIDEE</span>'}] 
					</std:linkPopup>
					<c:if test="${not mouvement.is_tovalidate and mouvement.last_statut == 'ANNUL'}">
						<span style="color: red;"> [ANNULÉE]</span>
					</c:if>
					<span style="color: #795548;right: 10px;position: absolute;margin-top: 3px;font-size: 11px;"> 
						<fmt:formatDate value="${mouvement.date_vente }" pattern="dd/MM/yyyy HH:mm" />
					</span>
					<br>
					<b style="font-size: 18px;"><fmt:formatDecimal value="${mouvement.mtt_commande_net}"/></b>dhs
					
	           		<c:if test="${not empty mouvement.mtt_reduction and mouvement.mtt_reduction > 0}">
						<i class="fa fa-gift" style="color: green;" title="Avec réduction"></i> 
						<fmt:formatDecimal value="${mouvement.mtt_reduction}"/>
					</c:if>
					<c:if test="${empty mouvement.mode_paiement || mouvement.last_statut=='TEMP' || mouvement.is_tovalidate}">
						<span style="color: blue;">
							 [NON ENCAISSÉE]
						</span>
					</c:if>
					<c:if test="${not empty mouvement.mode_paiement and mouvement.last_statut!='ANNUL'}">
						<span style="color: green;">
							 [ENCAISSÉE]
						</span>
					</c:if>
					
					<c:if test="${mouvement.last_statut != 'ANNUL'}">
						<br>	
						<a id="annul_lnk" class="btn btn-default shiny btn-xs" style="
									color:red;
									height: 30px;
									padding-top: 0px;
									width: 120px;
									line-height: 30px;
    								margin-left: 34%;
    								margin-top: 10px;" params="tp=histo&workId=${encryptionUtil.encrypt(mouvement.id)}" title="Annuler la commande">
							<i class="fa fa-ban" style="font-size:15px;"></i> Annuler
						</a>
					</c:if>
	            </div>
			</c:forEach>
		</div>	
 	</std:form>	
</div>