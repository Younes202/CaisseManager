<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.model.common.service.MessageService"%>
<%@page import="framework.controller.bean.message.GrowlMessageBean"%>
<%@page import="java.util.List"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>

<script type="text/javascript">
	$(document).ready(function (){
		$("a[id^='desactiver_notif_']").click(function(){
			showConfirmDeleteBox('<%=EncryptionUtil.encrypt("dash.dashBoardPharma.desactiverNotif")%>', $(this).attr("params"), $(this), "Cette notification ne s\'affichera plus.<br>Souhaitez-vous continuer ?", null, "D&eacute;sactiver notification");
		});
		
		<%// List growl message
			List<GrowlMessageBean> listGrowlMessage = MessageService.getListGrowlMessageBean();
			if ((listGrowlMessage != null) && (listGrowlMessage.size() > 0)) {
				for (GrowlMessageBean growlBean : listGrowlMessage) {%>
				showPostAlert("<%=growlBean.getTitle()%>", "<%=growlBean.getMessage()%>" , "<%=growlBean.getType().toString()%>");
<%}
			}
			MessageService.clearMessages();%>
	});
</script>

<!-- Page Header -->
<div class="page-header position-relative">
	<%
	String soft = StrimUtil.getGlobalConfigPropertie("context.soft");
	if("pharma".equals(soft)){ %>
	<std:link actionGroup="C" classStyle="btn btn-success" action="pharma.medoc.importAllComposants" icon="fa-3x fa-print" tooltip="Importer tous les médicments" value="Importer tous les médicments [DEMARRAGE]" />
	<%} %>
	
</div>
<!-- /Page Header -->
<!-- Page Body -->
<div class="page-body">
	<std:form name="search-form">
		<!-- ******************************* the Flash Info chart row ******************************* -->
		<div class="row">
			<div class="col-lg-12 col-sm-12 col-xs-12">
				<div class="widget-main ">
					<div class="tabbable">
						<%
							request.setAttribute("tab", "notif");
						%>
						<jsp:include page="/domaine/dashboard_erp/tabs_header.jsp" />
						<div class="tab-content">
							<!-- *******************************  Evolution des ventes sur 30 jours ******************************* -->
							<div class="row">
								<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">

									<div class="header bordered-darkorange">
										Notifications médicaments
									</div>
									<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 300px; overflow: auto; max-height: 350px;">
										<c:choose>
											<c:when test="${listNotifMedoc != null }">
												<table class="table table-condensed table-striped table-bordered table-hover no-margin">
													<thead>
														<tr>
															<th>Message</th>
															<th style="text-align: right; width: 140px;">
																<c:if test="${newArtCount > 0 }">
																	<std:link action="dash.dashBoardPharma.importAllNewComposants" actionGroup="C" icon="fa fa-cloud-download" classStyle="btn btn-sm btn-success" value="Tout importer (${newArtCount})" />
																</c:if>
															</th>
														</tr>
													</thead>
													<tbody>
														<c:forEach items="${listNotifMedoc }" var="notif">
																<tr>
																	<c:set value="black" var="color" />
																	<c:choose>
																		<c:when test="${notif.type_notif == 'ETS_UPDATE' }">
																			<c:set value="orange" var="color" />
																		</c:when>
																		<c:when test="${notif.type_notif == 'ADM_CREATE' }">
																			<c:set value="green" var="color" />
																		</c:when>
																		<c:when test="${notif.type_notif == 'ADM_REJECT' }">
																			<c:set value="red" var="color" />
																		</c:when>
																	</c:choose>	
																	<td>
																		<h6 style="color:${color };">
										                					${notif.message }
																		</h6>
																	</td>
																	<td style="text-align: center;">
																		<c:choose>
																			<c:when test="${notif.type_notif == 'ADM_REJECT' }">
																				<std:link action="dash.dashBoardPharma.importerArticle" params="isRej=1" workId="${notif.id }" actionGroup="C" icon="fa fa-reply" classStyle="btn btn-sm btn-warning" tooltip="Annuler ma modification" style="margin-right: 5px;" />
																			</c:when>
																			<c:otherwise>
																				<std:link action="dash.dashBoardPharma.importerArticle" workId="${notif.id }" actionGroup="C" icon="fa fa-cloud-download" classStyle="btn btn-sm btn-success" tooltip="Importer médicament" style="margin-right: 5px;" />
																			</c:otherwise>
																		</c:choose>
																		<std:link id="desactiver_notif_${notif.id }" params="curr=${notif.id }" actionGroup="C" icon="fa fa-close" classStyle="btn btn-sm btn-danger" tooltip="Ne plus afficher" />
																	</td>
																</tr>
														</c:forEach>
													</tbody>
												</table>
											</c:when>
											<c:otherwise>
												<h6 class="left-align-text">
													<span style="color: green;">Aucune notification.</span>
												</h6>
											</c:otherwise>
										</c:choose>
									</div>
								</div>
								<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
									<div class="header bordered-darkorange">
										Notifications admin
									</div>
									<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 300px; overflow: auto; max-height: 350px;">
										<c:choose>
											<c:when test="${listNotifAdmin != null and listNotifAdmin.size() > 0}">
												<table class="table table-condensed table-striped table-bordered table-hover no-margin">
													<thead>
														<tr>
															<th>Message</th>
															<th style="text-align: right; width: 150px;">
															</th>
														</tr>
													</thead>
													<tbody>
														<c:forEach items="${listNotifAdmin }" var="notif">
																<tr>
																	<td>
										                				<h6>${notif.message }</h6>
																	</td>
																	<td style="text-align: center;">
																		<std:linkPopup action="dash.dashBoardPharma.afficherMessage" workId="${notif.id }" actionGroup="C" icon="fa fa-eye" classStyle="btn btn-sm btn-info" tooltip="Lire message" style="margin-right: 5px;" />
																		<std:link id="desactiver_notif_${notif.id }" params="curr=${notif.id }" actionGroup="C" icon="fa fa-close" classStyle="btn btn-sm btn-danger" tooltip="Ne plus afficher" />
																	</td>
																</tr>
														</c:forEach>
													</tbody>
												</table>
											</c:when>
											<c:otherwise>
												<h6 class="left-align-text">
													<span style="color: green;">Aucune notification.</span>
												</h6>
											</c:otherwise>
										</c:choose>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</std:form>
</div>