<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="appli.model.domaine.administration.persistant.EcriturePersistant"%>
<%@page import="java.util.List"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>

<%
List<EcriturePersistant> listBanqueEvol =  (List<EcriturePersistant>)request.getAttribute("compteEvol");
listBanqueEvol = (listBanqueEvol == null) ? new ArrayList() : listBanqueEvol;
%>

<std:link style="display:none;" id="refresh_compte" action="dash.dashCompta.evolution_banque" targetDiv="dash-compte" />
	<div class="form-group">
		<std:label classStyle="control-label col-md-2" value="Compte" />
		<div class="col-md-4">
			<std:select type="long" name="curr_banque" data="${list_banque }" mode="std" key="id" labels="libelle" width="100%;" onChange="$('#refresh_compte').trigger('click');" value="${curr_banque }" />
		</div>
		<std:label classStyle="control-label col-md-2" value="Solde" />
		<div class="col-md-4">
			<span style="font-size: 20px;font-weight: bold;color:#2dc3e8;"><%=BigDecimalUtil.formatNumberZero(listBanqueEvol.get(12).getMontant()) %> <%=StrimUtil.getGlobalConfigPropertie("devise.symbole") %></span>
		</div>	
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

<script>
        // If you want to draw your charts with Theme colors you must run initiating charts after that current skin is loaded   
        	  /*Sets Themed Colors Based on Themes*/
            themesecondary = getThemeColorFromCss('themesecondary');// rouge
            themethirdcolor = getThemeColorFromCss('themethirdcolor'); // jaune
            themefourthcolor = getThemeColorFromCss('themefourthcolor');
            themefifthcolor = getThemeColorFromCss('themefifthcolor');
        	

            //Sets The Hidden Chart Width
            $('#dashboard-bandwidth-chart')
                .data('width', $('.box-tabbs')
                    .width() - 20);
			$(".databox-cell cell-4 no-padding text-align-center bordered-right bordered-platinum").css("background-color",themefifthcolor);

		var chartDomChart2 = document.getElementById('area-chart1');
		var myChartChart2 = echarts.init(chartDomChart2);
		var optionChart2;
		
		optionChart2 = {
		  title: {
		    text: 'Evolution solde compte bancaire sur 12 mois'
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
		      axisLabel: { interval: 0, rotate: 30 },
		      boundaryGap: false,
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