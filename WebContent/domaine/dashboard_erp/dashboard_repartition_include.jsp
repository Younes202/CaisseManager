<%@page import="java.util.HashMap"%>
<%@page import="appli.model.domaine.stock.service.impl.RepartitionBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="java.math.BigDecimal"%>

<%
Map<String,Object> dataRepartion = (Map<String,Object>)request.getAttribute("dataRepartion");
if(dataRepartion == null){
	dataRepartion = new HashMap<>();
}

Map<Long, RepartitionBean> mapMenuRecap = (Map<Long, RepartitionBean>)dataRepartion.get("MENU");
Map<Long, RepartitionBean> mapMenuRecapArts = (Map<Long, RepartitionBean>)dataRepartion.get("ARTS");

String devise = StrimUtil.getGlobalConfigPropertie("devise.symbole");
%>
	
	
<% if(mapMenuRecap != null && mapMenuRecap.size() > 0){%>
<div id="donut-chart" class="chart chart-lg" style="height: 430px;"></div>
<br>
<%} %>
<%if(mapMenuRecapArts != null && mapMenuRecapArts.size() > 0){%>
<div id="donut-chartArt" class="chart chart-lg" style="height: 430px;"></div>
<%} %>
	
<script>
/*Sets Themed Colors Based on Themes*/
themeprimary = getThemeColorFromCss('themeprimary');// bleu
themesecondary = getThemeColorFromCss('themesecondary');// rouge
themethirdcolor = getThemeColorFromCss('themethirdcolor'); // jaune
themefourthcolor = getThemeColorFromCss('themefourthcolor');
themefifthcolor = getThemeColorFromCss('themefifthcolor');
 
 
<%
if((mapMenuRecap == null || mapMenuRecap.size() == 0) && (mapMenuRecapArts == null || mapMenuRecapArts.size()==0)){%>
$("#repartition_div").html("<span style='padding-left:20px;'>Aucune donn&eacute;e &agrave; afficher.</span>");
<%} else{ %>
	
<%
BigDecimal quantiteTotal = BigDecimalUtil.ZERO;
if(mapMenuRecap != null && mapMenuRecap.size() > 0){%>    	

 var chartDom = document.getElementById('donut-chart');
 var myChart = echarts.init(chartDom, {width: 650, height: 450});
 var option;

 option = {
   tooltip: {
     trigger: 'item'
   },
   legend: {
     top: '5%',
     left: 'center',
     show: false
   },
   label:{
       fontSize: 8
     },
   series: [
     {
       name: 'Réparition',
       type: 'pie',
       radius: ['40%', '70%'],
       center: ['50%', '40%'],
       emphasis: {
         label: {
           show: true,
           fontSize: '13',
           fontWeight: 'bold',
           formatter : function (params){
			   return params.name + '\n('+ params.percent + '%'+')'
			},
         }
       },
       data: [
			<%
			for(Long key : mapMenuRecap.keySet()){
				RepartitionBean data = mapMenuRecap.get(key);
				BigDecimal qte = data.getQuantite();
				quantiteTotal = BigDecimalUtil.add(quantiteTotal, qte);
			}
					
			if(quantiteTotal.compareTo(BigDecimalUtil.ZERO) > 0){							
				for(Long key : mapMenuRecap.keySet()){
					RepartitionBean data = mapMenuRecap.get(key);
					BigDecimal qteVnt = data.getQuantite();
					BigDecimal qteTaux = BigDecimalUtil.divide(BigDecimalUtil.multiply(qteVnt, BigDecimalUtil.get(100)), quantiteTotal);
					String libelle = data.getLibelle();
					int value = qteTaux.intValue();
				%>   
				{ value: "<%=value%>", 
					name: "<%=libelle%>\n(<%=BigDecimalUtil.formatNumber(qteVnt)%>)" },
                <%}
            }%>
       ],
     }
   ]
 };

 option && myChart.setOption(option);
 <%}%>
 
<%if(mapMenuRecapArts != null && mapMenuRecapArts.size() > 0){%>	
	// ARTS -------------------
	    var chartDom = document.getElementById('donut-chartArt');
        var myChart = echarts.init(chartDom, {width: 650, height: 450});
        var option;

        option = {
          tooltip: {
              show: false,
            trigger: 'item'
          },
          legend: {
        	show:false,
            top: '5%',
            left: 'center'
          },
          series: [
            {
              name: 'Répartition',
              type: 'pie',
              radius: ['40%', '70%'],
              center: ['50%', '40%'],
              label:{
                  fontSize: 8
                },
              emphasis: {
                label: {
                  show: true,
                  fontSize: '13',
                  fontWeight: 'bold',
                  formatter : function (params){
   				   return params.name + '\n('+ params.percent + '%'+')'
   				},
                }
              },
              data: [
					<%
					quantiteTotal = BigDecimalUtil.ZERO;
					for(Long key : mapMenuRecapArts.keySet()){
						RepartitionBean data = mapMenuRecapArts.get(key);
						BigDecimal qte = data.getQuantite();
						quantiteTotal = BigDecimalUtil.add(quantiteTotal, qte);
					}
							
					if(quantiteTotal.compareTo(BigDecimalUtil.ZERO) > 0){							
						for(Long key : mapMenuRecapArts.keySet()){
							RepartitionBean data = mapMenuRecapArts.get(key);
							BigDecimal qteVnt = data.getQuantite();
							BigDecimal qteTaux = BigDecimalUtil.divide(BigDecimalUtil.multiply(qteVnt, BigDecimalUtil.get(100)), quantiteTotal);
							String libelle = data.getLibelle();
							int value = qteTaux.intValue();
						%>   
						{ value: "<%=value%>",
							name: "<%=libelle%>\n(<%=BigDecimalUtil.formatNumber(qteVnt)%>)" },
                       <%}
                   }%>
              ]
            }
          ]
        };

        option && myChart.setOption(option);
<%}%>	
<%}%>		

    </script>	
