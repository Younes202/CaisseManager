<%@page import="java.util.Date"%>
<%@page import="appli.model.domaine.vente.persistant.JourneePersistant"%>
<%@page import="java.util.HashMap"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="java.util.ArrayList"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>

<%@page errorPage="/commun/error.jsp"%>

<%
Map<String,Object> variables = (Map<String,Object>)request.getAttribute("dataIndicateur");
if(variables == null){
	variables = new HashMap();
}
String devise = StrimUtil.getGlobalConfigPropertie("devise.symbole");

List<Object[]> listVentes = (List<Object[]>)variables.get("ventes");
listVentes = (listVentes == null) ? new ArrayList() : listVentes;

List<Object[]> listAchats = (List<Object[]>)variables.get("achats");
listAchats = (listAchats == null) ? new ArrayList() : listAchats;

List<Object[]> listDepences = (List<Object[]>)variables.get("depences");
listDepences = (listDepences == null) ? new ArrayList() : listDepences;

List<Object[]> listRecetes = (List<Object[]>)variables.get("recetes");
listRecetes = (listRecetes == null) ? new ArrayList() : listRecetes;

List<Object[]> listAchatsPie = (List<Object[]>)variables.get("ventePie");
listAchatsPie = (listAchatsPie == null) ? new ArrayList() : listAchatsPie;

List<Object[]> mttResultatNetParMois = (List<Object[]>)variables.get("mttResultatNetParMois");
mttResultatNetParMois = (mttResultatNetParMois == null) ? new ArrayList() : mttResultatNetParMois;
%>

<style>
	#scroll_list_article_alert{
		max-height: 290px;
	}

.morris-hover {
	position: absolute;
	z-index: 1000
}

.morris-hover.morris-default-style {
	border-radius: 10px;
	padding: 6px;
	color: #666;
	background: rgba(255, 255, 255, 0.8);
	border: solid 2px rgba(230, 230, 230, 0.8);
	font-family: sans-serif;
	font-size: 12px;
	text-align: center
}

.morris-hover.morris-default-style .morris-hover-row-label {
	font-weight: bold;
	margin: 0.25em 0
}

.morris-hover.morris-default-style .morris-hover-point {
	white-space: nowrap;
	margin: 0.1em 0
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
						<% request.setAttribute("tab", "indic"); %>
						<jsp:include page="/domaine/dashboard_erp/tabs_header.jsp" />
					<div class="tab-content">
						<!-- *******************************  Evolution des ventes sur 30 jours ******************************* -->
						
					
						<!--     **************************  Répartion des achats && taux de ventes par articles  ************************ -->
						<div class="horizontal-space"></div>
						<div class="row">
							<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
								<div class="widget">
									<div class="widget-header ">
										<span class="widget-caption"> Evolution du résultat net</span>
									</div>
									<div class="widget-body">
										<div id="line-chart" class="chart chart-lg"></div>
									</div>
								</div>
							</div>
							<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
								<div class="widget">
									<div class="widget-header ">
										<span class="widget-caption"> Evolution des achats</span>
									</div>
									<div class="widget-body">
										<div id="area-chart2" class="chart chart-lg"></div>
									</div>
								</div>
							</div>
							
							<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
								<div class="widget">
									<div class="widget-header ">
										<span class="widget-caption"> Evolution des dépenses</span>
									</div>
									<div class="widget-body">
										<div id="area-chart3" class="chart chart-lg"></div>
									</div>
								</div>
							</div>
							
							<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
								<div class="widget">
									<div class="widget-header ">
										<span class="widget-caption"> Evolution des recettes</span>
									</div>
									<div class="widget-body">
										<div id="area-chart4" class="chart chart-lg"></div>
									</div>
								</div>
							</div>
							
							<div class="row">
							<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
								<div class="widget">
									<div class="widget-header ">
										<span class="widget-caption">Evolution des ventes</span>
									</div>
									<div class="widget-body" style="height: 525px;">
										<div id="area-chart1" class="chart chart-lg" style="height: 400px;"></div>
									</div>
								</div>
							</div>
                        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12"> 
 								<div class="widget"> 
 									<div class="widget-header "> 
 										<span class="widget-caption">Répartitions </span> 
 									</div> 
 									<div class="widget-body"> 
 											<div class="row" style="margin-bottom: 10px;"> 
 												<div class="col-lg-1 col-md-2"> 
 													<span style="float: left;margin-top: 5px;">Début &nbsp;</span> 
 												</div> 
 												<div class="col-lg-2 col-md-4">	 
 													<std:date name="rep_dt_debut" value="${curr_dtDebut }"/> 
 												</div> 
 												<div class="col-lg-1 col-md-2"> 
 													<span style="float: left;margin-top: 5px;">Fin &nbsp;</span> 
 												</div>
												<div class="col-lg-2 col-md-4">
													<std:date name="rep_dt_fin" value="${curr_dtFin }" />
												</div>
												<div class="col-lg-1 col-md-2">
													<span style="float: left; margin-top: 5px;">Type &nbsp;</span>
												</div>
												<div class="col-lg-3 col-md-9">
													<std:select type="string" name="curr_famille" data="${list_familleStock }" key="id" labels="libelle" width="100%;" value="${curr_famille }" isTree="true" />
												</div>
												<div class="col-lg-1 col-md-1">
													<a href="javascript:" wact="<%=EncryptionUtil.encrypt("dash.dashBoard2.init_data_achats")%>" targetDiv="repartition_div" style="margin-top: 4px; position: absolute; left: 0px;"> <img src="resources/framework/img/refresh.png" />
													</a>
												</div>
											</div>
											<div class="row" id="repartition_div"> 
												<jsp:include page="/domaine/caisse/dashboard/dashboard_indi_achat.jsp" />
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

<script>
            //Sets The Hidden Chart Width
            $('#dashboard-bandwidth-chart')
                .data('width', $('.box-tabbs')
                    .width() - 20);
			$(".databox-cell cell-4 no-padding text-align-center bordered-right bordered-platinum").css("background-color",themefifthcolor);

		        
//------------------------------  évolution des ventes sur 30 jours chart------------------------------------------------//

			var chartDomChart = document.getElementById('area-chart1');
			var myChartChart1 = echarts.init(chartDomChart);
			var optionChart;
			
			let date = [];
			let data = [];
					  <%for (Object[] journeeV : listVentes) {
	                  Date dtJv = (Date) journeeV[1];%>
					  var now = new Date(<%=dtJv.getTime()%>);
					  date.push([now.getFullYear(), now.getMonth()+1, now.getDate()].join('/'));
					  data.push(<%=journeeV[0]%>);
					  <%}%>
					optionChart = {
					  tooltip: {
							formatter: '{b} <br/>{a} : {c} <%=StrimUtil.getGlobalConfigPropertie("devise.symbole")%> <br/>',
					    trigger: 'axis',
					    position: function (pt) {
					      return [pt[0], '10%'];
					    }
					  },
					  title: {
					    left: 'center',
					    text: 'Evolution Des Ventes'
					  },
					  toolbox: {
					    feature: {
					      dataZoom: {
					        yAxisIndex: 'none'
					      },
					      restore: {},
					      saveAsImage: {}
					    }
					  },
					  xAxis: {
					    type: 'category',
					    boundaryGap: false,
					    data: date
					  },
					  yAxis: {
					    type: 'value',
					    boundaryGap: [0, '100%'],
					    axisLabel: {
					          formatter: function (value) { 
					   			    	  return value.toString().replace(/\,/g,' ') + ' <%=StrimUtil.getGlobalConfigPropertie("devise.symbole")%>'; 
					   			   		},
					       }
					  },
					  dataZoom: [
					    {
					      type: 'inside',
					      start: 0,
					      end: 10
					    },
					    {
					      start: 0,
					      end: 10
					    }
					  ],
					  series: [
					    {
					      name: 'Montant : ',
					      type: 'line',
					      symbol: 'none',
					      sampling: 'lttb',
					      itemStyle: {
					        color: 'rgb(255, 70, 131)'
					      },
					      areaStyle: {
					        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
					          {
					            offset: 0,
					            color: 'rgb(255, 158, 68)'
					          },
					          {
					            offset: 1,
					            color: 'rgb(255, 70, 131)'
					          }
					        ])
					      },
					      data: data
					    }
					  ]
					};

			
			 // Evolution des ventes sur 12 mois			
			var chartDomChart3 = document.getElementById('area-chart3');
			var myChartChart3 = echarts.init(chartDomChart3);
			var optionChart3;
			
					let date3 = [];
					let data3 = [];
							  <%for (Object[] journeeA : listDepences) {
	                          Date dtJa = (Date) journeeA[1];%>
							  var now = new Date(<%=dtJa.getTime()%>);
							  date3.push([now.getFullYear(), now.getMonth()+1, now.getDate()].join('/'));
							  data3.push(<%=journeeA[0]%>);
							  <%}%>
							  optionChart3 = {
							  tooltip: {
									formatter: '{b} <br/>{a} : {c} <%=StrimUtil.getGlobalConfigPropertie("devise.symbole")%> <br/>',
							    trigger: 'axis',
							    position: function (pt) {
							      return [pt[0], '10%'];
							    }
							  },
							  title: {
							    left: 'center',
							    text: 'Evolution Des Depences'
							  },
							  toolbox: {
							    feature: {
							      dataZoom: {
							        yAxisIndex: 'none'
							      },
							      restore: {},
							      saveAsImage: {}
							    }
							  },
							  xAxis: {
							    type: 'category',
							    boundaryGap: false,
							    data: date3
							  },
							  yAxis: {
							    type: 'value',
							    boundaryGap: [0, '100%'],
							    axisLabel: {
							          formatter: function (value) { 
							   			    	  return value.toString().replace(/\,/g,' ') + ' <%=StrimUtil.getGlobalConfigPropertie("devise.symbole")%>'; 
							   			   		},
							       }
							  },
							  dataZoom: [
							    {
							      type: 'inside',
							      start: 0,
							      end: 10
							    },
							    {
							      start: 0,
							      end: 10
							    }
							  ],
							  series: [
							    {
							      name: 'Montant : ',
							      type: 'line',
							      symbol: 'none',
							      sampling: 'lttb',
							      itemStyle: {
							        color: 'rgb(255, 70, 131)'
							      },
							      areaStyle: {
							        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
							          {
							            offset: 0,
							            color: 'rgb(255, 158, 68)'
							          },
							          {
							            offset: 1,
							            color: 'rgb(255, 70, 131)'
							          }
							        ])
							      },
							      data: data3
							    }
							  ]
							};
							  
							  
							  // Evolution des ventes sur 12 mois			
								var chartDomChart2 = document.getElementById('area-chart2');
								var myChartChart2 = echarts.init(chartDomChart2);
								var optionChart2;
								
										let date2 = [];
										let data2 = [];
												  <%for (Object[] journeeA : listAchats) {
	                                               Date dtJa = (Date) journeeA[1];%>
												  var now = new Date(<%=dtJa.getTime()%>);
												  date2.push([now.getFullYear(), now.getMonth()+1, now.getDate()].join('/'));
												  data2.push(<%=journeeA[0]%>);
												  <%}%>
												  optionChart2 = {
												  tooltip: {
														formatter: '{b} <br/>{a} : {c} <%=StrimUtil.getGlobalConfigPropertie("devise.symbole")%> <br/>',
												    trigger: 'axis',
												    position: function (pt) {
												      return [pt[0], '10%'];
												    }
												  },
												  title: {
												    left: 'center',
												    text: 'Evolution Des Achats'
												  },
												  toolbox: {
												    feature: {
												      dataZoom: {
												        yAxisIndex: 'none'
												      },
												      restore: {},
												      saveAsImage: {}
												    }
												  },
												  xAxis: {
												    type: 'category',
												    boundaryGap: false,
												    data: date2
												  },
												  yAxis: {
												    type: 'value',
												    boundaryGap: [0, '100%'],
												    axisLabel: {
												          formatter: function (value) { 
												   			    	  return value.toString().replace(/\,/g,' ') + ' <%=StrimUtil.getGlobalConfigPropertie("devise.symbole")%>'; 
												   			   		},
												       }
												  },
												  dataZoom: [
												    {
												      type: 'inside',
												      start: 0,
												      end: 10
												    },
												    {
												      start: 0,
												      end: 10
												    }
												  ],
												  series: [
												    {
												      name: 'Montant : ',
												      type: 'line',
												      symbol: 'none',
												      sampling: 'lttb',
												      itemStyle: {
												        color: 'rgb(255, 70, 131)'
												      },
												      areaStyle: {
												        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
												          {
												            offset: 0,
												            color: 'rgb(255, 158, 68)'
												          },
												          {
												            offset: 1,
												            color: 'rgb(255, 70, 131)'
												          }
												        ])
												      },
												      data: data2
												    }
												  ]
												};
												  
												  // Evolution des ventes sur 12 mois			
													var chartDomChart4 = document.getElementById('area-chart4');
													var myChartChart4 = echarts.init(chartDomChart4);
													var optionChart4;
													
															let date4 = [];
															let data4 = [];
																	  <%for (Object[] journeeA : listRecetes) {
						                                               Date dtJa = (Date) journeeA[1];%>
																	  var now = new Date(<%=dtJa.getTime()%>);
																	  date4.push([now.getFullYear(), now.getMonth()+1, now.getDate()].join('/'));
																	  data4.push(<%=journeeA[0]%>);
																	  <%}%>
																	  optionChart4 = {
																	  tooltip: {
																			formatter: '{b} <br/>{a} : {c} <%=StrimUtil.getGlobalConfigPropertie("devise.symbole")%> <br/>',
																	    trigger: 'axis',
																	    position: function (pt) {
																	      return [pt[0], '10%'];
																	    }
																	  },
																	  title: {
																	    left: 'center',
																	    text: 'Evolution Des recettes'
																	  },
																	  toolbox: {
																	    feature: {
																	      dataZoom: {
																	        yAxisIndex: 'none'
																	      },
																	      restore: {},
																	      saveAsImage: {}
																	    }
																	  },
																	  xAxis: {
																	    type: 'category',
																	    boundaryGap: false,
																	    data: date4
																	  },
																	  yAxis: {
																	    type: 'value',
																	    boundaryGap: [0, '100%'],
																	    axisLabel: {
																	          formatter: function (value) { 
																	   			    	  return value.toString().replace(/\,/g,' ') + ' <%=StrimUtil.getGlobalConfigPropertie("devise.symbole")%>'; 
																	   			   		},
																	       }
																	  },
																	  dataZoom: [
																	    {
																	      type: 'inside',
																	      start: 0,
																	      end: 10
																	    },
																	    {
																	      start: 0,
																	      end: 10
																	    }
																	  ],
																	  series: [
																	    {
																	      name: 'Montant : ',
																	      type: 'line',
																	      symbol: 'none',
																	      sampling: 'lttb',
																	      itemStyle: {
																	        color: 'rgb(255, 70, 131)'
																	      },
																	      areaStyle: {
																	        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
																	          {
																	            offset: 0,
																	            color: 'rgb(255, 158, 68)'
																	          },
																	          {
																	            offset: 1,
																	            color: 'rgb(255, 70, 131)'
																	          }
																	        ])
																	      },
																	      data: data4
																	    }
																	  ]
																	};
												  
												  
												  
			 // Evolution des ventes sur 12 mois			
			var chartDomline = document.getElementById('line-chart');
			var myChartline = echarts.init(chartDomline);
			var optionline;
			
			optionline = {
			  title: {
				left: 'center',
			    text: 'Evolution du Net'
			  },
			  tooltip: {
			    trigger: 'axis',
				formatter: '{b} <br/>{a} : {c} <%=StrimUtil.getGlobalConfigPropertie("devise.symbole")%> <br/>',
			    axisPointer: {
			      type: 'cross',
			      label: {
			        backgroundColor: '#6a7985'
			      }
			    }
			  },
			  
			//   legend: {
			//     data: ['Search Engine']
			//   },
			  toolbox: {
			    feature: {
			      saveAsImage: {}
			    }
			  },
			  grid: {
			    left: '3%',
			    right: '4%',
			    bottom: '3%',
			    containLabel: true
			  },
			  xAxis: [
			    {
			      type: 'category',
			      axisLabel: { interval: 0, rotate: 30 },
			      boundaryGap: false,
			      data: [
			    	  <%for (Object[] data : mttResultatNetParMois) {
	BigDecimal mtt = (BigDecimal) data[0];
	mtt = (mtt == null) ? BigDecimalUtil.get(0) : mtt;
	String date = "" + data[1];%>   
			                 '<%=date%>',
			           <%}%>   
			           ]
			    }
			  ],
			  yAxis: [
			    {
			      type: 'value',
			      axisLabel: {
			          formatter: function (value) { 
			   			    	  return value.toString().replace(/\,/g,' ') + ' <%=StrimUtil.getGlobalConfigPropertie("devise.symbole")%>'; 
			   			   	  },
			       }
			    }
			  ],
			  series: [
			    {
			      name: 'Montant : ',
			      type: 'line',
			      stack: 'Total',
<%-- 			      formatter: function (params) { return params.toString().replace(/\,/g,' ') + '<%=StrimUtil.getGlobalConfigPropertie("devise.symbole")%>'; }, --%>
			      label: {
			        show: true,
			        position: 'top',
			        formatter: function (params) { 
	   			    	  return params.value.toString().replace(/\,/g,' ') + ' <%=StrimUtil.getGlobalConfigPropertie("devise.symbole")%>'; 
	   			   		}
			      },
			      areaStyle: {},
			      emphasis: {
			        focus: 'series'
			      },
			      data: [
			          <%for (Object[] data : mttResultatNetParMois) {
	BigDecimal mtt = (BigDecimal) data[0];
	mtt = (mtt == null) ? BigDecimalUtil.get(0) : mtt;
	String date = "" + data[1];%>   
			                 '<%=mtt%>',
			           <%}%>   
			              
			           ]
			    }
			  ]
			};

optionChart && myChartChart1.setOption(optionChart);

optionChart2 && myChartChart2.setOption(optionChart2);

optionChart3 && myChartChart3.setOption(optionChart3);
 
optionChart4 && myChartChart4.setOption(optionChart4);

optionline && myChartline.setOption(optionline);




           

    </script>