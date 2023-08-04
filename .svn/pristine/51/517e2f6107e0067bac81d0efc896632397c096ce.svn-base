<%@page import="java.util.ArrayList"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="java.util.List"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>

<%
List<Object[]> dataTypeEmploye = (List<Object[]>)request.getAttribute("dataTypeEmploye");
List<Object[]> dataTypeFrais = (List<Object[]>)request.getAttribute("dataTypeFrais");


%>
<script type="text/javascript">

var chartDomFrais = document.getElementById('typeFrais');
var myChartFrais = echarts.init(chartDomFrais,  {width: 600, height: 600});
var optionFrais;

optionFrais = {
  title: {
    text: 'Répartition Frais Par Type',
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
      data: [
    	  <% for(Object[] data: dataTypeFrais){%>
        	{ value: <%=data[0]%>, name: "<%=data[1]%>" },
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
  
  
  
var chartDomEmploye = document.getElementById('employeId');
var myChartEmploye = echarts.init(chartDomEmploye,  {width: 600, height: 600});
var optionEmploye;

optionEmploye = {
  title: {
    text: 'Répartition Frais Par Employe',
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
      data: [
    	  <% for(Object[] data: dataTypeEmploye){%>
        	{ value: <%=data[0]%>, name: "<%=data[1]%>" },
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



optionFrais && myChartFrais.setOption(optionFrais);
optionEmploye && myChartEmploye.setOption(optionEmploye);

</script>

<div class="row" style="margin-left: 0px; margin-right: 0px;">
	<div class="col-lg-5 col-md-5 col-sm-5 col-xs-5">
		<span style="float: left; margin-top: 5px;">De: &nbsp;</span>
		<std:date name="empl_fr_debut" value="${curr_fr_dtDebut }" style="width: 202px; margin-left: 24px;" />
	</div>
	<div class="form-group">
		<std:label classStyle="control-label col-md-2" value="Employes" />
		<div class="col-md-4">
			<std:select name="employe" value="${employe }" data="${listEmploye }" key="id" labels="nom;' ';prenom" type="long" placeholder="employe" />
		</div>
	</div>
	<div class="col-lg-5 col-md-5 col-sm-5 col-xs-5">
		<span style="float: left; margin-top: 5px;">A:  &nbsp;</span>
		<std:date name="empl_fr_fin" value="${curr_fr_dtFin }" style="width: 202px;margin-left: 32px;" />
	</div>
	<div class="form-group">
		<std:label classStyle="control-label col-md-2" value="Type Frais" />
		<div class="col-md-4">
			<std:select name="typeFraiSlct" value="${typeFrais }" data="${listTypeFrais }" key="id" labels="libelle" type="long" placeholder="type Frais" />
		</div>
	</div>
	<a href="javascript:" wact="<%=EncryptionUtil.encrypt("dash.dashRH.repartition_frais_type_employe")%>" targetDiv="dash_travail2" style="margin-top: -34px;position: absolute;right: 120px;"> 
		<img src="resources/framework/img/refresh.png" />
	</a>
</div>

<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
	<div class="well with-header" style="height: 575px;">
		<div class="header bordered-darkorange">Répartition Frais</div>
		<div style="height: 510px;">
			<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 510px;" id="employeId">
			
			</div>
		</div>
	</div>
</div>

<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
	<div class="well with-header" style="height: 575px;">
		<div class="header bordered-darkorange">Répartition Frais</div>
		<div style="height: 510px;">
			<div class="databox databox-xxlg databox-vertical databox-shadowed bg-white radius-bordered padding-5" style="height: 510px;" id="typeFrais">
			
			</div>
		</div>
	</div>
</div>

