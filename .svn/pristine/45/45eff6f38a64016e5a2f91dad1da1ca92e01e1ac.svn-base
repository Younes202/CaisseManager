<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="java.util.List"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>

<%
List<Object[]> ListVenteParCaissierBO = (List<Object[]>)request.getAttribute("dataVente");
%>
<script type="text/javascript">

var chartDomTravail = document.getElementById('rep_vente_bo_div');
var myChartTravail = echarts.init(chartDomTravail);
var optionTravail;

optionTravail = {
  tooltip: {
	formatter: '{b} <br/>{a} : {c} Dhs',
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
    	   <% for(Object[] data : ListVenteParCaissierBO){ %>
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
      name: 'Montant',
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
    	  <% for(Object[] data : ListVenteParCaissierBO){ %>
				<%=data[0]%>,
				<%}%>
				],
    }
  ]
};

optionTravail && myChartTravail.setOption(optionTravail);
</script>

<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 380px;" id="rep_vente_bo_div">
 
</div>


