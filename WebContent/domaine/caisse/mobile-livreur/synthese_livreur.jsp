
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@page errorPage="/commun/error.jsp" %>

<%
	String devise = StrimUtil.getGlobalConfigPropertie("devise.symbole");
	Map<String, Object> map_data = (Map<String, Object>) request.getAttribute("mapData");
%>

<script type="text/javascript">
	$(document).ready(function (){
	
	});
</script>

<div class="row" id="div_chiffre">
	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="height: 100vh;">
		<div class="widget flat radius-bordered">
	            <div class="widget-header bg-blue" style="display: flex">
	                <span class="widget-caption">Synthése des chiffres</span>
	            </div>
	            <div class="widget-body" style="padding: 0px">
						<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 100vh;">
							<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-left: -15px;margin-right: -15px;">
								<table style="width: 100%;">
									<tr>
										<td>	
											<std:date name="curr_dtDebut" value="${curr_dtDebut }"/>
										</td>
										<td style="padding-left: 5px;">	
											<std:date name="curr_dtFin" value="${curr_dtFin }"/>
										</td>
										<td>
											<a href="javascript:" targetDiv="right-div" wact="<%=EncryptionUtil.encrypt("caisse.livreurMobile.loadDashBoard")%>">
												<img src="resources/framework/img/refresh.png"/>
											</a>
										</td>
									</tr>		
								</table>	
							</div>
							<div style="margin-top: 75px;">
								<hr>
								<div class="databox-row">
									<div class="databox-cell cell-4 no-padding text-align-center bordered-right bordered-platinum">
										<span class="databox-number lightcarbon no-margin" style="color: blue !important;font-size: 28px;line-height: 32px;"> 
											<%=StringUtil.getValueOrEmpty(map_data.get("nbr_cmd")) %>
										</span> 
										<span class="databox-text sonic-silver  no-margin">
											Nbr. commandes
										</span>
									</div>
									<div class="databox-cell cell-4 no-padding text-align-center bordered-right bordered-platinum">
										<span class="databox-number lightcarbon no-margin" style="color: blue !important;font-size: 28px;line-height: 32px;"> 
											<%=StringUtil.getValueOrEmpty(map_data.get("mtt_cmd")) %> 
										</span> 
										<span class="databox-text sonic-silver no-margin">
											Montant total
										</span>
									</div>
									<div class="databox-cell cell-4 no-padding text-align-center">
										<span class="databox-number lightcarbon no-margin" style="color: blue !important;font-size: 28px;line-height: 32px;"> 
											<%=StringUtil.getValueOrEmpty(map_data.get("part")) %> 
										</span> 
										<span class="databox-text sonic-silver no-margin">
											Part
										</span>
									</div>
								</div>
								<hr>
							</div>
						</div>
					</div>
				</div>
			</div>
	</div>
