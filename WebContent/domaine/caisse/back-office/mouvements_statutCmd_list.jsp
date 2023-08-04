<%@page import="framework.controller.ControllerUtil"%>
<%@page import="java.util.Random"%>
<%@page import="java.awt.Color"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="java.util.Date"%>
<%@page import="java.math.BigInteger"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

	<%
List<Object[]> listTempsGlobalParEmploye = (List<Object[]>)request.getAttribute("listTempsGlobalParEmploye");
if(listTempsGlobalParEmploye == null){
	listTempsGlobalParEmploye = new ArrayList<>();
}
%>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion des ventes</li>
		<li class="active">Changement de statut des commandes</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		
	</div>
	<!--Header Buttons-->
	<jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
	<!--Header Buttons End-->
</div>
<!-- /Page Header -->

<!-- Page Body -->
<div class="page-body">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>

<div class="row">
		<div class="col-lg-12 col-sm-12 col-xs-12">
			<div class="widget">
				<div class="widget-main ">
					<div class="tabbable">
						<%
						String tpSuivi = (String)ControllerUtil.getMenuAttribute("tpSuivi", request);
						%>
						<ul class="nav nav-tabs" id="myTab">
							<li class="<%="temps".equals(tpSuivi) || tpSuivi == null ? " active" : ""%>"><a data-toggle="tab" href="#descripton" wact="<%=EncryptionUtil.encrypt("caisse_restau.caisseConfigurationRestau.load_temps_cuisine")%>">Temps par commandes </a></li>
							<li class="<%="stat".equals(tpSuivi) ? " active" : ""%>"><a data-toggle="tab" href="#journeeCaisse" wact="<%=EncryptionUtil.encrypt("caisse.caisse.tempsGlobalParEmploye")%>"> Statuts des commandes </a></li>
						</ul>
					</div>
					<std:form name="search-form">
								<div class="row" style="margin-top: 15px;">
							        <div class="form-group" style="padding: 0px 16px;">
							        	<std:label classStyle="control-label col-md-2" value="Date d&eacute;but" />
							            <div class="col-md-2" style="width: 15% !important;">
							                 <std:date name="dateDebut" required="true" value="${dateDebut }"/>
							            </div>
							            <div class="col-md-2" style="text-align: center;width: 13% !important;">
							            	<std:link action="caisse.caisse.tempsGlobalParEmploye" params="prev=1&is_fltr=1" icon="fa fa-arrow-circle-left" tooltip="Journ&eacute;e pr&eacute;c&eacute;dente" />
							            	<std:link action="caisse.caisse.tempsGlobalParEmploye" params="next=1&is_fltr=1" icon="fa fa-arrow-circle-right" tooltip="Journ&eacute;e suivante" />
							            </div>
							            <std:label classStyle="control-label col-md-2" value="Date fin" />
							            <div class="col-md-2" style="width: 15% !important;">
							                 <std:date name="dateFin" required="true" value="${dateFin }"/>
							            </div>

								
								<div class="col-md-2" style="width: 12%;">
							           	 	<std:button action="caisse.caisse.tempsGlobalParEmploye" value="Filtrer" params="is_fltr=1" classStyle="btn btn-primary" style="margin-left: 85px;"/>
							           	 </div>	
							           	 </div>
							         </div>  	
							         
							         <div class="row">
							          <div class="col-md-1">
							          </div>
							         <div class="col-md-4">
								<div class="form-group">
	                               <std:label classStyle="control-label col-md-2" value="Article" />
									<std:select type="string[]" isTree="true" name="articles" data="${listArticle }" key="id" labels="libelle" width="100%;" multiple="true" />
								</div>
								</div>
								<div class="col-md-2">
							          </div>
								<div class="col-md-4">
								<div class="form-group">
	                               <std:label classStyle="control-label col-md-2" value="Menu" />
									<std:select type="string[]" isTree="true" name="menus" data="${listeMenu }" key="id" labels="libelle" width="100%;" multiple="true"   />
								</div>
								</div>
							         </div> 
							   
							   <hr>
							   
							   <div class="row" style="margin-left: 30%;">
							   		<span style="height: 30px;background-color: #ffce55;min-width: 50px;float: left;padding: 5px;margin-right: 5px;">Nombre de cmd</span>
							   		<span style="height: 30px;background-color: #57ceeb;min-width: 50px;float: left;padding: 5px;">Durée en minutes</span>
							   </div>
							   <div class="row">
									<div class="chartcontainer">
                                         <canvas id="bar" height="450" width="1440"></canvas>
                                     </div>
							   </div>
		
								<script type="text/javascript">
								var chartDomTravail = document.getElementById('bar');
								var myChartTravail = echarts.init(chartDomTravail);
								var optionTravail;

								optionTravail = {
								  tooltip: {
									formatter: '{b} <br/>{a} : {c} Heures',
								    trigger: 'axis',
								    axisPointer: {
								      type: 'shadow'
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
								      data: [
								    	   <% for(Object[] data : listTempsGlobalParEmploye){ %>
												"<%=data[1]%>",
												<%}%>
												],
												
								      axisTick: {
								        alignWithLabel: true
								      }
								    }
								  ],
								  yAxis: [
								    {
								      type: 'value'
								    }
								  ],
								  series: [
								    {
								      name: 'Durée',
								      type: 'bar',
								      barWidth: '60%',
								      itemStyle: {
								          color: function (param) {
								            const color = ["#FFBF00", "#FFBF00", "#285430","#DC3535"];
								            // param.value[0] is the x-axis value
								            return color[param.value.toString().replace(/\./g,0) % color.length]
								          }
								      },
								      data: [
								    	  <% for(Object[] data : listTempsGlobalParEmploye){ %>
												<%=data[0]%>,
												<%}%>
												],
								    }
								  ]
								};

								optionTravail && myChartTravail.setOption(optionTravail);
									</script>
							</std:form>
							</div>
						</div>
					</div>
				</div>
			</div>


