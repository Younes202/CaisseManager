<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="java.util.Date"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.List"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>

<%@page errorPage="/commun/error.jsp"%>

<%
// Papiers &agrave; l'approche
List<Object[]> listAssurance = (List<Object[]>)request.getAttribute("listAssurance");
List<Object[]> listVignette = (List<Object[]>)request.getAttribute("listVignette");
List<Object[]> listVisite = (List<Object[]>)request.getAttribute("listVisite");
List<Object[]> listVidange = (List<Object[]>)request.getAttribute("listVidange");
List<Object[]> listIncident = (List<Object[]>)request.getAttribute("listIncident");
List<Object[]> listConsommation = (List<Object[]>)request.getAttribute("listConsommation");
%>

<style>
.table-bordered{
	font-size: 12px;
}
</style>

<!-- Page Body -->
<div class="page-body">
<std:form name="search-form">
	<!-- ******************************* the Flash Info chart row ******************************* -->
	<div class="row">
		<div class="col-lg-12 col-sm-12 col-xs-12">
			<div class="widget-main ">
				<div class="tabbable">
						<% request.setAttribute("tab", "auto"); %>
						<jsp:include page="../tabs_header.jsp" />
					
					<div class="tab-content">
	<div class="row">
		<div class="col-lg-4 col-md-12 col-sm-12 col-xs-12">
			<div class="well with-header" style="height: 420px;">
				<div class="header bordered-darkorange">Documents &agrave; l'approche 
				(<span style="font-weight: bold;color: red;"><%=listVisite.size()+listVignette.size()+listAssurance.size() %></span>)</div>
				<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 350px;overflow: auto;max-height: 350px;">
					<table class="table table-condensed table-striped table-bordered table-hover no-margin">
                		<tr>
                			<th>Véhicule</th>
                			<th></th>
                			<th width="100px">Date limite</th>
                		</tr>
                		
                		<%if(listAssurance.size() > 0){ %>
                		<tr style="background-color: #e5ebaa;border-bottom: 2px solid #2196F3;color: #FF5722;font-weight:bold;">
                			<td colspan="3">Assurance</td>
                		</tr>
                		<%for(Object[] data : listAssurance){ %>
	                		<tr>
	                			<td colspan="2"><%=data[0] %> <%=data[1] %> (<%=data[2] %>)</td>
	                			<td style="text-align:center;font-weight: bold;color: red;"><%=DateUtil.dateToString((Date)data[3]) %></td>
	                		<tr>
	                	<%} 
	                	}%>
	                	<%if(listVignette.size() > 0){ %>
                		<tr style="background-color: #e5ebaa;border-bottom: 2px solid #2196F3;color: #FF5722;font-weight:bold;">
                			<td colspan="3">Vignette</td>
                		</tr>
                		<%for(Object[] data : listVignette){ %>
	                		<tr>
	                			<td colspan="2"><%=data[0] %> <%=data[1] %> (<%=data[2] %>)</td>
	                			<td style="text-align:center;font-weight: bold;color: red;"><%=DateUtil.dateToString((Date)data[3]) %></td>
	                		<tr>
                		<%} 
                		}%>
						<%if(listVisite.size() > 0){ %>
                			<tr style="background-color: #e5ebaa;border-bottom: 2px solid #2196F3;color: #FF5722;font-weight:bold;">
                		      <td colspan="3">Visite Technique</td>
                		    </tr>
                		<%for(Object[] data : listVisite){ %>
	                		<tr>
	                			<td colspan="2"><%=data[0] %> <%=data[1] %> (<%=data[2] %>)</td>
	                			<td style="text-align:center;font-weight: bold;color: red;"><%=DateUtil.dateToString((Date)data[3]) %></td>
	                		<tr>
                		<%} 
                		}%>               		
                	</table>
				</div>
			</div>
		</div>
		<div class="col-lg-4 col-md-12 col-sm-12 col-xs-12">
			<div class="well with-header" style="height: 420px;">
				<div class="header bordered-darkorange">
					Entretien &agrave; l'approche (<span style="font-weight: bold;color: red;"><%=listVidange.size() %></span>)
				</div>
				<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 350px;overflow: auto;max-height: 350px;">
					<table class="table table-condensed table-striped table-bordered table-hover no-margin">
                		<tr>
                			<th>Véhicule</th>
                			<th>Km théorique</th>
                			<th width="100px">Derni&egrave;re vidange</th>
                		</tr>
                		<%for(Object[] data : listVidange){ %>
                			<tr>
	                			<td><%=data[0] %> <%=data[1] %> (<%=data[2] %>)</td>
	                			<td align="right" style="font-weight: bold;">
	                				<%
	                				BigDecimal km_theorique = (BigDecimal)data[3];
	                				BigDecimal km_theorique_carb = (BigDecimal)data[4];
	                				BigDecimal km = km_theorique;
	                				if(km_theorique_carb != null && km_theorique_carb.compareTo(km_theorique) > 0){
	                				    km = km_theorique_carb;
	                				}%>
	                				<%=BigDecimalUtil.formatNumberZeroBd(km) %> km
	                			</td>
<!-- 	                			date_passage -->
	                			<td align="center"><%=DateUtil.dateToString((Date)data[5]) %></td>
                			</tr>
                	<%} %>
                	</table>
				</div>
			</div>
		</div>
		<div class="col-lg-4 col-md-12 col-sm-12 col-xs-12">
			<div class="well with-header" style="height: 420px;">
				<div class="header bordered-darkorange">Derniers incidents (<span style="font-weight: bold;color: red;"><%=listIncident.size() %></span>)</div>
				<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 350px;overflow: auto;max-height: 350px;">
					<table class="table table-condensed table-striped table-bordered table-hover no-margin">
                		<tr>
                			<th width="80px">Date</th>
                			<th>Véhicule</th>
                			<th>Conducteur</th>
                		</tr>
                		<%for(Object[] data : listIncident){ %>
                			<tr>
<!--                 				date incident -->
	                			<td><%=DateUtil.dateToString((Date)data[0]) %></td>
<!-- 	                			vehicule -->
	                			<td><%=data[1] %> <%=data[2] %> (<%=data[3] %>)</td>
<!-- 	                			conducteur -->
	                			<td><%=data[4] %></td>
                			</tr>
                	<%} %>
                	</table>
                	
				</div>
			</div>
		</div>
	</div>
	<div class="row">		
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
			<div class="well with-header" style="height: 420px;">
				<div class="header bordered-darkorange">
					Alertes consommations (<span style="font-weight: bold;color: red;"><%=listConsommation.size() %></span>)
				</div>
				<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 350px;overflow: auto;max-height: 350px;">
					<table class="table table-responsive table-striped table-bordered table-hover no-margin" style="width:100%">
                		<tr>
                			<th>Véhicule</th>
                			<th>Conducteur</th>
                			<th>Consommation de réference</th>
                			<th>Consommation minimale</th>
                			<th>Consommation maximale</th>
                			<th>Consommation enregistrée</th>
                			<th width="180px">Date de valeur</th>
                		</tr>
                		<%for(Object[] data : listConsommation){ %>
	                		<tr>
<!-- 	                			vehicule -->
	                			<td><%=data[0] %> <%=data[1] %> (<%=data[2] %>)</td>
<!-- 	                			conducteur -->
	                			<td><%=data[3] %></td>
<!-- 	                			conso_ref -->
	                			<td style="font-weight:bold;text-align:right;"><%=BigDecimalUtil.formatNumberZeroBd((BigDecimal)data[4]) %></td>
<!-- 	                			conso_min -->
	                			<td style="font-weight:bold;text-align:right;"><%=BigDecimalUtil.formatNumberZeroBd((BigDecimal)data[5]) %></td>
<!-- 	                			conso_max -->
	                			<td style="font-weight:bold;text-align:right;"><%=BigDecimalUtil.formatNumberZeroBd((BigDecimal)data[6]) %></td>
<!-- 	                			consoCent -->
	                			<td style="font-weight:bold;text-align:right;color:red;"><%=BigDecimalUtil.formatNumberZeroBd((BigDecimal)data[7]) %></td>
<!-- 	                			dateCarb -->
	                			<td><%=DateUtil.dateToString((Date)data[8]) %></td>
	                		<tr>
                		<%} %>	
                	</table>
				</div>
			</div>
		</div>		
	</div>


							<div class="row">
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
									<div class="well with-header" style="height: 575px;">
										<div class="header bordered-darkorange">Charges des véhicules</div>
										<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 510px; font-size: 12px;">
											<div class="row" style="margin-left: 0px; margin-right: 0px;">

												<div class="col-lg-2 col-md-2">
													<span style="float: left; margin-top: 5px;">De &nbsp;</span>
												</div>
												<div class="col-lg-4 col-md-4">
													<std:date name="rep_dt_debut" value="${curr_dtDebut }" />
												</div>
												<div class="col-lg-2 col-md-2">
													<span style="float: left; margin-top: 5px;">A &nbsp;</span>
												</div>
												<div class="col-lg-4 col-md-4">
													<std:date name="rep_dt_fin" value="${curr_dtFin }" />
													<div class="col-lg-2 col-md-2">
														<a href="javascript:" wact="<%=EncryptionUtil.encrypt("dash.dashAuto.compare_charge_vehicule")%>" targetDiv="repartitionCaisseBO_div" style="margin-top: -28px; position: absolute; margin-left: 215%"> <img src="resources/framework/img/refresh.png" />
														</a>
													</div>
												</div>
											</div>

											<div class="row" id="repartitionCaisse_div" style="margin-left: 0px; margin-right: 0px;">
											<jsp:include page="/domaine/dashboard_erp/auto/dashboard_compare.jsp"></jsp:include>
											</div>
										</div>
									</div>
								</div>
							</div>

                          <div class="row">
								<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
									<div class="well with-header" style="height: 575px;">
										<div class="header bordered-darkorange">Répartition des dépenses</div>
										<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 510px; font-size: 12px;">
											<div class="row">
											  <div class="col-sm-3" style="margin-right: 7px;">
													<span style="width: 250px;margin-left: 0Px;margin-top: -7px;">De &nbsp;</span>
													<std:date name="empl_dt_debut" value="${curr_empl_dtDebut }" />
											  </div>
											  <div class="col-sm-3" style="margin-right: 7px;">
													<span style="width: 250px;margin-left: 0Px;margin-top: -7px;">A &nbsp &nbsp;</span>
													<std:date name="empl_dt_fin" value="${curr_empl_dtFin }" />
											  </div>
											  <div class="col-sm-4" style="margin-right: 7px;">
													<span style="width: 250px;margin-left: 0Px;margin-top: -7px;">Véhicule</span>
													<std:select name="vehicule_id" value="${vehicule }" data="${list_vehicule }" width="100%" key="id" labels="modele" type="long" placeholder="vehicule" />
											  </div>
											  <div class="col-sm-1">
											  		<a href="javascript:" wact="<%=EncryptionUtil.encrypt("dash.dashAuto.compare_depence_vehicule")%>" targetDiv="dash_CMPd" style="margin-top: 22PX;FLOAT: LEFT;"> 
											  			<img src="resources/framework/img/refresh.png" />
													</a>
											  </div>
											</div>
											<div class="row" id="dash_CMPd" style="margin-left: 0px; margin-right: 0px;">
												<jsp:include page="/domaine/dashboard_erp/auto/dashboard_veh_depence.jsp"></jsp:include>
											</div>
										</div>
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
<!-- /Page Body -->