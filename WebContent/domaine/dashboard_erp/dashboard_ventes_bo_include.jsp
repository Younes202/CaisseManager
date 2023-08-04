<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="java.util.List"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>

<%
List<Object[]> repVentesParArticle = (List<Object[]>)request.getAttribute("repVentesParArticle");
List<Object[]> repVentesParModePaie = (List<Object[]>)request.getAttribute("repVentesParModePaie");

%>
<script type="text/javascript">

var chartDomAchats = document.getElementById('rep_vente_article_div');
var myChartAchats = echarts.init(chartDomAchats, null, {width: 500, height: 500});
var optionAchats;	
optionAchats = {
title: {
  text: '',
subtext: '',
left: 'center'
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
  name: 'Répartition par article',
  type: 'pie',
  radius: '50%',
  top: '-20%',
  left: '-60%',
  right: '-30%',
  data: [

	  <%for (Object[] journeeA : repVentesParArticle) {%>
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


var chartDomVente = document.getElementById('rep_vente_mode_paie_div');
var myChartVente = echarts.init(chartDomVente, null,  {width: 500, height: 500});
var optionVente;	
optionVente = {
title: {
  text: '',
subtext: '',
left: 'center'
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
	name: 'Répartition par mode de paiement',
  type: 'pie',
  radius: '50%',
  top: '-20%',
  left: '-60%',
  right: '-30%',
  data: [
	  <%for (Object[] journeeA : repVentesParModePaie) {%>
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
optionVente && myChartVente.setOption(optionVente);

</script>

<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
	<div class="well with-header" style="height: 450px;">
		<div class="header bordered-darkorange">Répartition par article</div>
		<div style="height: 510px;margin-right: 100px;
    margin-left: 134px;">
			<div style="height: 510px;" id="rep_vente_article_div">
			
			</div>
		</div>
	</div>
</div>

<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
	<div class="well with-header" style="height: 450px;">
		<div class="header bordered-darkorange">Répartition par mode de paiement</div>
		<div style="height: 510px;margin-right: 100px;margin-left: 134px;">
			<div style="height: 510px;" id="rep_vente_mode_paie_div">
			</div>
		</div>
	</div>
</div>

