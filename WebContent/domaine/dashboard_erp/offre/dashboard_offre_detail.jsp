<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="java.util.List"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>

<%
List<Object[]> dataCMP = (List<Object[]>)request.getAttribute("dataCMP");
%>
<script type="text/javascript">

var chartDomDonnePoint = document.getElementById('DonnePoint');
var myChartDonnePoint = echarts.init(chartDomDonnePoint);
var optionDonnePoint;

optionDonnePoint = {
  tooltip: {
    trigger: 'axis',
    formatter: '{a} <br/>{b} : {c} <%=StrimUtil.getGlobalConfigPropertie("devise.symbole")%> <br/>',
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
    	  <% for(Object[] data : dataCMP){ %>
    	  '<%=(""+data[0])%>',
				<%}%>
				],
				
      axisTick: {
        alignWithLabel: true
      }
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
      name: 'Montant',
      itemStyle: {
          color: function (param) {
            const color = ["#FFBF00", "#FFBF00", "#285430","#DC3535"];
            // param.value[0] is the x-axis value
            return color[param.value.toString().replace(/\./g,0) % color.length]
          }
      },
      type: 'bar',
      barWidth: '10%',
      data: [
    	  <% for(Object[] data : dataCMP){ %>
				<%=data[1]%>,
				<%}%>
				],
    }
  ]
};
var chartDomReduction = document.getElementById('Reduction');
var myChartReduction = echarts.init(chartDomReduction);
var optionReduction;

optionReduction = {
  tooltip: {
	  formatter: '{a} <br/>{b} : {c} <%=StrimUtil.getGlobalConfigPropertie("devise.symbole")%> <br/>',
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
    	  <% for(Object[] data : dataCMP){ %>
    	  '<%=(""+data[0])%>',
				<%}%>
				],
				
      axisTick: {
        alignWithLabel: true
      }
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
      name: 'Montant',
      itemStyle: {
          color: function (param) {
            const color = ["#FFBF00", "#FFBF00", "#285430","#DC3535"];
            // param.value[0] is the x-axis value
            return color[param.value.toString().replace(/\./g,0) % color.length]
          }
      },
      type: 'bar',
      barWidth: '60%',
      data: [
    	  <% for(Object[] data : dataCMP){ %>
				<%=data[2]%>,
				<%}%>
				],
    }
  ]
};

var chartDomOffert = document.getElementById('Offert');
var myChartOffert = echarts.init(chartDomOffert);
var optionOffert;

optionOffert = {
  tooltip: {
	formatter: '{a} <br/>{b} : {c} <%=StrimUtil.getGlobalConfigPropertie("devise.symbole")%> <br/>',
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
    	  <% for(Object[] data : dataCMP){ %>
    	  '<%=(""+data[0])%>',
    	  <%}%>
				],
				
      axisTick: {
        alignWithLabel: true
      }
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
      name: 'Montant',
      itemStyle: {
          color: function (param) {
            const color = ["#FFBF00", "#FFBF00", "#285430","#DC3535"];
            // param.value[0] is the x-axis value
            return color[param.value.toString().replace(/\./g,0) % color.length]
          }
      },
      type: 'bar',
      barWidth: '60%',
      data: [
    	  <% for(Object[] data : dataCMP){ %>
				<%=data[3]%>,
				<%}%>
				],
    }
  ]
};


optionDonnePoint && myChartDonnePoint.setOption(optionDonnePoint);
optionReduction && myChartReduction.setOption(optionReduction);
optionOffert && myChartOffert.setOption(optionOffert);

</script>

<div class="row">
  <div class="col-sm-3">
  <div class="col-lg-5 col-md-5 col-sm-5 col-xs-5">
		<span style="    width: 250px;
        margin-left: 0Px;
        margin-top: -7px;">De &nbsp;</span>
		<std:date name="empl_dt_debut" value="${curr_empl_dtDebut }" />
	</div>
  </div>
  
  <div class="col-sm-3">
  	<div class="col-lg-5 col-md-5 col-sm-5 col-xs-5">
		<span style="    width: 250px;	
        margin-left: 0Px;
        margin-top: -7px;">A &nbsp &nbsp;</span>
		<std:date name="empl_dt_fin" value="${curr_empl_dtFin }" />
	</div>
  
  </div>
  
  <div class="col-sm-3" style="margin-top: 22px">
   <div class="form-group">
		<std:label classStyle="control-label col-md-2" value="Utilisateur" />
		<div class="col-md-4">
			<std:select name="user" value="${user}" data="${listUsers }" key="id" labels="login" type="long" placeholder="Utilisateur" />
		</div>
	</div>
  </div>
  
  <div class="col-sm-3">
  <a href="javascript:" wact="<%=EncryptionUtil.encrypt("dash.dashOffre.init_data_employe")%>" targetDiv="dash_CMP" style="margin-top: 22PX;FLOAT: LEFT;"> <img src="resources/framework/img/refresh.png" />
		</a>
  </div>
</div>



<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
	<div class="well with-header" style="height: 575px;">
		<div class="header bordered-darkorange">fidèlité de donnes par utlisateurs</div>
		<div style="height: 510px;">
			<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 510px;" id="DonnePoint">
			
			</div>
		</div>
	</div>
</div>

<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
	<div class="well with-header" style="height: 575px;">
		<div class="header bordered-darkorange">Réduction par utlisateurs</div>
		<div style="height: 510px;">
			<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 510px;" id="Reduction">
			</div>
		</div>
	</div>
</div>

<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
	<div class="well with-header" style="height: 575px;">
		<div class="header bordered-darkorange">Offerts pointage par utlisateurs</div>
		<div style="height: 510px;">
			<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 510px;" id="Offert">
			</div>
		</div>
	</div>
</div>

