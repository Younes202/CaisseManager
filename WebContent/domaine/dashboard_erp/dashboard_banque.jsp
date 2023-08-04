<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="appli.model.domaine.administration.persistant.EcriturePersistant"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="java.util.Map"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="java.util.ArrayList"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
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
List<EcriturePersistant> listBanqueEvol =  (List<EcriturePersistant>)request.getAttribute("compteEvol");
listBanqueEvol = (listBanqueEvol == null) ? new ArrayList() : listBanqueEvol;



%>
<script>
<%
Map<String, List<String>> paiementEvol = (Map<String, List<String>>)request.getAttribute("paiementEvol");
List<String> listMonth = (List<String>) paiementEvol.get("listMonth");
%>
var app = {};

var chartDom = document.getElementById('area-chart-paiement');
var myChart = echarts.init(chartDom);
var option;

const posList = [
  'left',
  'right',
  'top',
  'bottom',
  'inside',
  'insideTop',
  'insideLeft',
  'insideRight',
  'insideBottom',
  'insideTopLeft',
  'insideTopRight',
  'insideBottomLeft',
  'insideBottomRight'
];
app.configParameters = {
  rotate: {
    min: -90,
    max: 90
  },
  align: {
    options: {
      left: 'left',
      center: 'center',
      right: 'right'
    }
  },
  verticalAlign: {
    options: {
      top: 'top',
      middle: 'middle',
      bottom: 'bottom'
    }
  },
  position: {
    options: posList.reduce(function (map, pos) {
      map[pos] = pos;
      return map;
    }, {})
  },
  distance: {
    min: 0,
    max: 100
  }
};
app.config = {
  rotate: 90,
  align: 'left',
  verticalAlign: 'middle',
  position: 'insideBottom',
  distance: 15,
  onChange: function () {
    const labelOption = {
      rotate: app.config.rotate,
      align: app.config.align,
      verticalAlign: app.config.verticalAlign,
      position: app.config.position,
      distance: app.config.distance
    };
    myChart.setOption({
      series: [
        {
          label: labelOption
        },
        {
          label: labelOption
        },
        {
          label: labelOption
        },
        {
          label: labelOption
        }
      ]
    });
  }
};
const labelOption = {
  show: true,
  position: app.config.position,
  distance: app.config.distance,
  align: app.config.align,
  verticalAlign: app.config.verticalAlign,
  rotate: app.config.rotate,
  formatter: '{c}  {name|{a}}',
  fontSize: 16,
  rich: {
    name: {}
  }
};
option = {
  title: {
    text: 'Répartition des modes de paiement'
  },
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'shadow'
    }
  },
  legend: {
	  data: [
		  <%  for(String paieV : paiementEvol.keySet()){%>
		 <% if(!"listMonth".equals(paieV)){%>
		 '<%=paieV%>',
		  <%}}%>
		  ]
	  },
  toolbox: {
    show: true,
    orient: 'vertical',
    left: 'right',
    top: 'center',
    feature: {
      mark: { show: true },
      dataView: { show: true, readOnly: false },
      magicType: { show: true, type: ['line', 'bar', 'stack'] },
      restore: { show: true },
      saveAsImage: { show: true }
    }
  },
  xAxis: [
    {
      type: 'category',
      axisTick: { show: false },
      axisLabel: { interval: 0, rotate: 30 },
      data: [
      <% for(String dataArray : listMonth){  %>  
          '<%=dataArray%>',
    	<%}%>   	   ]
    }
  ],
  yAxis: [
    {
      type: 'value'
    }
  ],
  series: [
	  <%  for(String paieV : paiementEvol.keySet()){
		   if(!"listMonth".equals(paieV)){
			List<String> listData = paiementEvol.get(paieV);%>
    {
      name: '<%=paieV%>',
      type: 'bar',
      barGap: 0,
      label: labelOption,
      emphasis: {
        focus: 'series'
      },
      data: [
    	  <% for(String dataArray : listData){  
    	  %>  
    	   <%=BigDecimalUtil.get(""+dataArray)%>,
    	<%}%>  
      	   ]
    },
    <%}}%>
  ]
};

option && myChart.setOption(option);



</script>
<style>
#list_vehicule th{
	background-color: #c8f4ff;
    text-align: center;
    font-size: initial;
    font-weight: bold;
}

.bg-class{
    background-color: #f3f4f5; 
}
</style>
		
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
						<% request.setAttribute("tab", "banque"); %>
						<jsp:include page="tabs_header.jsp" />
					<div class="tab-content">
						<div class="form-group">
							<std:label classStyle="control-label col-md-2" value="Compte bancaire" />
							<div class="col-md-5">
								<std:select type="long" name="curr_banque" data="${list_banque }" key="id" labels="libelle" width="100%;" onChange="$('#tab_bqe').attr('params', 'bq='+$('#curr_banque').val()).trigger('click');" value="${curr_banque }" />
							</div>
						</div>
						<hr>
						<!-- *******************************  Evolution des ventes sur 30 jours ******************************* -->
						<div class="row" style="text-align: center;">
							<span style="font-size: 20px;font-weight: bold;">Solde courant : </span>
							<span style="font-size: 20px;font-weight: bold;color:#2dc3e8;"><%=BigDecimalUtil.formatNumberZero(listBanqueEvol.get(12).getMontant()) %> <%=StrimUtil.getGlobalConfigPropertie("devise.symbole") %></span>
						</div>
						<hr>
						<div class="row">
							<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
								<div class="widget">
									<div class="widget-body">
										<div id="area-chart1" class="chart chart-lg"></div>
									</div>
								</div>
							</div>
						</div>
						
						<div class="row">
							<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
								<div class="widget">
									<div class="widget-body">
										<div id="area-chart-paiement" class="chart chart-lg"></div>
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
        // If you want to draw your charts with Theme colors you must run initiating charts after that current skin is loaded   
        	  /*Sets Themed Colors Based on Themes*/
            //Sets The Hidden Chart Width
            $('#dashboard-bandwidth-chart')
                .data('width', $('.box-tabbs')
                    .width() - 20);
			$(".databox-cell cell-4 no-padding text-align-center bordered-right bordered-platinum").css("background-color",themefifthcolor);

		        
//------------------------------  &eacute;volution des ventes sur 30 jours chart------------------------------------------------//
                 
		var chartDomChart2 = document.getElementById('area-chart1');
		var myChartChart2 = echarts.init(chartDomChart2);
		var optionChart2;
		
		optionChart2 = {
		  title: {
		    text: 'Evolution du solde du compte bancaire sur 12 mois'
		  },
		  tooltip: {  
			formatter: '{b} <br/>{a} : {c} <%=StrimUtil.getGlobalConfigPropertie("devise.symbole")%> <br/>',
			trigger: 'axis',
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
		      boundaryGap: false,
		      axisLabel: { interval: 0, rotate: 30 },
		      data: [
		    	  <%for(EcriturePersistant ectB : listBanqueEvol){
                 		if(ectB.getDate_mouvement() == null){
                 			continue;
                 		}
                   %>		             
                 '<%=DateUtil.dateToString(ectB.getDate_mouvement(), "dd/MM")%>',
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
		      color:'#00E7FF',
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
		    	  <%for(EcriturePersistant ectB : listBanqueEvol){
                 		if(ectB.getDate_mouvement() == null){
                 			continue;
                 		}
                   %>		                 
                 '<%= ectB.getMontant()!=null?ectB.getMontant().intValue():0%>',
		           <%}%>   
		              
		           ]
		    }
		  ]
		};
		optionChart2 && myChartChart2.setOption(optionChart2);

   
    </script>