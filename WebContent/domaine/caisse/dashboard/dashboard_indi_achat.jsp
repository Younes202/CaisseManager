<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="java.util.List"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>

<%
List<Object[]> listAchatsPie = (List<Object[]>)request.getAttribute("achatsPie");
List<Object[]> listDepencesPie = (List<Object[]>)request.getAttribute("depencesPie");
List<Object[]> listRecetesPie = (List<Object[]>)request.getAttribute("recetesPie");

%>
<script type="text/javascript">
var chartDomAchats = document.getElementById('pie-chartAchats');
var myChartAchats = echarts.init(chartDomAchats, null,  {width: 500, height: 500});
var optionAchats;	
optionAchats = {
title: {
  	text: 'Répartition des achats',
	subtext: '',
	left: 'left'
},
tooltip: {
	trigger: 'item'
},
legend: {
	orient: 'vertical',
	left: 'left',
	show: false
},
series: [
{
  name: 'Répartition des achats',
  type: 'pie',
  radius: '50%',
  top: '-10%',
  left: '-30%',
  data: [
	  <%for (Object[] journeeA : listAchatsPie) {%>
    { value: <%=journeeA[0]%>, name: "<%=journeeA[1]%>" },
    <%}%>
  ],
  emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
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

var chartDomDepences = document.getElementById('pie-chartDepences');
var myChartDepences = echarts.init(chartDomDepences, null,  {width: 500, height: 500});
var optionDepences;	
optionDepences = {
title: {
  	text: 'Répartition des dépenses',
	subtext: '',
	left: 'left'
},
tooltip: {
trigger: 'item'
},
legend: {
orient: 'vertical',
left: 'left',
show: false
},
series: [
{
  name: 'Répartition des dépenses',
  type: 'pie',
  radius: '50%',
  top: '-10%',
  left: '-30%',
  data: [
	  <%for (Object[] journeeA : listDepencesPie) {%>
    { value: <%=journeeA[0]%>, name: "<%=journeeA[1]%>" },
    <%}%>
  ],
  emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
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

var chartDomRecetes = document.getElementById('pie-chartRecetes');
var myChartRecetes = echarts.init(chartDomRecetes, null,  {width: 500, height: 500});
var optionRecetes;	
optionRecetes = {
title: {
  	text: 'Réparition des recettes',
	subtext: '',
	left: 'left'
},
tooltip: {
trigger: 'item'
},
legend: {
orient: 'vertical',
left: 'left',
show: false
},
series: [
{
  name: 'Répartition des recettes',
  type: 'pie',
  radius: '50%',
  top: '-10%',
  left: '-30%',
  data: [
	  <%for (Object[] journeeA : listRecetesPie) {%>
    { value: <%=journeeA[0]%>, name: "<%=journeeA[1]%>" },
    <%}%>
  ],
  emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
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

optionAchats && myChartAchats.setOption(optionAchats);
optionDepences && myChartDepences.setOption(optionDepences);
optionRecetes && myChartRecetes.setOption(optionRecetes);

</script>

<div class="col-lg-4 col-md-12 col-sm-12 col-xs-12">
	<div class="well with-header" style="height: 575px;">
		<div class="header bordered-darkorange">Répartition des Achats</div>
		<div style="height: 510px;">
			<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 510px;" id="pie-chartAchats">
			
			</div>
		</div>
	</div>
</div>	

<div class="col-lg-4 col-md-12 col-sm-12 col-xs-12">
	<div class="well with-header" style="height: 575px;">
		<div class="header bordered-darkorange">Répartition des Dépences</div>
		<div style="height: 510px;">
			<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 510px;" id="pie-chartDepences">
			</div>
		</div>
	</div>
</div>

<div class="col-lg-4 col-md-12 col-sm-12 col-xs-12">
	<div class="well with-header" style="height: 575px;">
		<div class="header bordered-darkorange">Répartition des Recettes</div>
		<div style="height: 510px;">
			<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 510px;" id="pie-chartRecetes">
			</div>
		</div>
	</div>
</div>

