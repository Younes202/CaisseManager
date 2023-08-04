<%@page import="java.util.ArrayList"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="java.util.List"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>

<%
List<Object[]> dataEmploye = (List<Object[]>)request.getAttribute("dataEmploye");
%>
<script type="text/javascript">

var chartDomTravail = document.getElementById('travail');
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
      data: [
    	  <% for(Object[] data : dataEmploye){ %>
				'<%=data[0]%>',
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
    	  <% for(Object[] data : dataEmploye){ %>
				<%=data[1]%>,
				<%}%>
				],
    }
  ]
};

var chartDomRetard = document.getElementById('Retard');
var myChartRetard = echarts.init(chartDomRetard);
var optionRetard;

optionRetard = {
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
    	  <% for(Object[] data : dataEmploye){ %>
				'<%=data[0]%>',
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
      itemStyle: {
          color: function (param) {
            const color = [ "#DC3535", "#285430"];
            if (param.value < 0) {
              return color[0];
            }
            else{
              return color[1];
            }
          }
      },
      barWidth: '60%',
      data: [
    	  <% for(Object[] data : dataEmploye){ %>
				<%=data[2]%>,
				<%}%>
				],
    }
  ]
};


optionTravail && myChartTravail.setOption(optionTravail);
optionRetard && myChartRetard.setOption(optionRetard);
</script>
<div class="row" style="margin-left: 0px; margin-right: 0px;">
	<div class="col-lg-5 col-md-5 col-sm-5 col-xs-5">
		<span style="float: left; margin-top: 5px;">De: &nbsp;</span>
		<std:date name="empl_dt_debut" value="${curr_empl_dtDebut }" style="width: 202px;
    margin-left: 24px;" />
	</div>
	<div class="form-group">
		<std:label classStyle="control-label col-md-2" value="Employes" />
		<div class="col-md-4">
			<std:select name="employe" value="${employe}" data="${listEmploye }" key="id" labels="nom;' ';prenom" type="long" placeholder="employe" />
		</div>
	</div>
	<div class="col-lg-5 col-md-5 col-sm-5 col-xs-5">
		<span style="float: left; margin-top: 5px;">A:  &nbsp;</span>
		<std:date name="empl_dt_fin" value="${curr_empl_dtFin }" style="width: 202px;
    margin-left: 32px;" />
	</div>

	<div class="form-group">
		<std:label classStyle="control-label col-md-2" value="Postes" />
		<div class="col-md-4">
			<std:select name="poste" value="${poste}" data="${listPoste }" key="id" labels="intitule" type="long" placeholder="poste" />
		</div>
	</div>
	
		<a href="javascript:" wact="<%=EncryptionUtil.encrypt("dash.dashRH.init_data_employe")%>" targetDiv="dash_travail" style="margin-top: -34px;position: absolute;right: 120px;"> 
			<img src="resources/framework/img/refresh.png" />
		</a>
</div>

<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
	<div class="well with-header" style="height: 575px;">
		<div class="header bordered-darkorange">Heures travaillées par employé</div>
		<div style="height: 510px;">
			<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 510px;" id="travail">
			
			</div>
		</div>
	</div>
</div>

<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
	<div class="well with-header" style="height: 575px;">
		<div class="header bordered-darkorange">Retards pointage par employé</div>
		<div style="height: 510px;">
			<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 510px;" id="Retard">
			
			</div>
		</div>
	</div>
</div>

