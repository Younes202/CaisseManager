<%@page import="java.util.HashMap"%>
<%@page import="appli.model.domaine.stock.service.impl.RepartitionBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant"%>
<%@page import="java.math.BigDecimal"%>
<div id="donut-chart" class="chart chart-lg" style="height: 400px;"></div>

<%
String devise = StrimUtil.getGlobalConfigPropertie("devise.symbole");

Map<String,Object> dataRepartion = (Map<String,Object>)request.getAttribute("dataRepartion");
if(dataRepartion == null){
	dataRepartion = new HashMap<>();
}

Long familleId = (Long)request.getAttribute("familleId");
Map<Long, RepartitionBean> mapMenuRecap = (Map<Long, RepartitionBean>)dataRepartion.get("ARTS");
if(mapMenuRecap == null){
	mapMenuRecap = new HashMap<>();
}
%>	

<script>

<%
if(mapMenuRecap.size() == 0){%>
$("#repartition_div").html("<span style='padding-left:20px;'>Aucune donn&eacute;e &agrave; afficher.</span>");
<%} else{ %>
        var chartDom = document.getElementById('donut-chart');
        var myChart = echarts.init(chartDom, {width: 500, height: 500});
        var option;

        option = {
          tooltip: {
            trigger: 'item'
          },
          legend: {
            top: '5%',
            left: 'center'
          },
          series: [
            {
              name: 'Répartition',
              type: 'pie',
              radius: ['40%', '70%'],
              avoidLabelOverlap: false,
              itemStyle: {
                borderRadius: 10,
                borderColor: '#fff',
                borderWidth: 2
              },
              label: {
                show: false,
                position: 'center'
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: '20',
                  fontWeight: 'bold',
                  formatter : function (params){
   				   return params.name + '\n('+ params.percent + '%'+')'
   				},
                }
              },
              labelLine: {
                show: false
              },
              data: [
					<%
					BigDecimal quantiteTotal = BigDecimalUtil.ZERO;
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
							name: '<%=libelle%>\n(<%=BigDecimalUtil.formatNumber(qteVnt)%>)' },
                       <%}
                   }%>
              ]
            }
          ]
        };

        option && myChart.setOption(option);
        <%}%>
        
    </script>	
