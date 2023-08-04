<%@page import="java.util.ArrayList"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="java.util.List"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>

<%
List<Object[]> listData = (List<Object[]>)request.getAttribute("listData");
if(listData == null){
	listData = new ArrayList<>();
}
%>
<script type="text/javascript">

var chartDom = document.getElementById('veh_depence');
var myChart = echarts.init(chartDom, null,  {width: 600, height: 600});
var option;

option = {
  title: {
    text: 'Répartition depences',
    subtext: '',
    left: 'center'
  },
  
  tooltip: {
    trigger: 'item',
  },
  
    // 					  
//	  legend: {
//	    orient: 'vertical',
//	    left: 'left'
//	  },
  series: [
    {
      name: 'Répartition',
      type: 'pie',
      radius: '50%',
      top: '-30%',
      data: [
    	  <% for(Object[] data: listData){%>
        	{ value: <%=data[0]%>, name: 'Carburant' },
        	{ value: <%=data[1]%>, name: 'Vidange' },
        	{ value: <%=data[2]%>, name: 'Visite' },
        	{ value: <%=data[3]%>, name: 'Assurance' },
        	{ value: <%=data[4]%>, name: 'Vignette' },
        	{ value: <%=data[5]%>, name: 'Incident' }
        	<%}%>
      ],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)',
          
        }
      },

      itemStyle : {
            normal : {
                 label : {
                    show: true, 
                    position: 'inner',
                    formatter : function (params){
                          return params.name + '\n('+ params.percent + '%'+')'
                    },
                },
                labelLine : {
                    show : true
                }
            }},
    
    }
  ]
};

option && myChart.setOption(option);


</script>
			<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 430px;" id="veh_depence">
			 
			</div>


