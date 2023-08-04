
<%@page import="appli.model.domaine.stock.service.IArticleService"%>
<%@page import="appli.model.domaine.personnel.persistant.EmployePersistant"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="framework.model.common.util.BigDecimalUtil"%>
<%@page import="appli.model.domaine.personnel.persistant.ClientPersistant"%>
<%@page import="framework.model.common.util.ServiceUtil"%>
<%@page import="appli.model.domaine.fidelite.dao.IPortefeuille2Service"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="framework.model.common.constante.ProjectConstante"%>
<%@page import="framework.model.common.service.MessageService"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli.SOFT_ENVS"%>
<%@page import="java.util.Calendar"%>
<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="java.awt.Color"%>
<%@page import="framework.model.common.util.DateUtil"%>
<%@page import="framework.model.common.util.BooleanUtil"%>
<%@page import="appli.model.domaine.personnel.persistant.PlanningPersistant"%>
<%@page import="java.util.List"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
	.fc table {
		z-index: 0 !important;
	}
	.fc-title{
	    white-space: normal !important;
	}
</style>

<%
List<PlanningPersistant> list_planning = (List<PlanningPersistant>) request.getAttribute("list_planning");
%>

<script src='resources/framework/js/fullcalendar/moment.min.js'></script>
<script src='resources/framework/js/fullcalendar/jquery-ui.custom.min.js'></script>
<script src='resources/framework/js/fullcalendar/fullcalendar.min.js'></script>
<script type="text/javascript">
(function(){
    function onload (moment, $) {

        // RIPPED STRAIGHT FROM MOMENT'S SOURCE
        // (https://github.com/moment/moment/blob/develop/lang/fr.js)
        //
        moment.lang('fr', {
            months : "janvier_février_mars_avril_mai_juin_juillet_ao�t_septembre_octobre_novembre_décembre".split("_"),
            monthsShort : "janv._févr._mars_avr._mai_juin_juil._ao�t_sept._oct._nov._déc.".split("_"),
            weekdays : "dimanche_lundi_mardi_mercredi_jeudi_vendredi_samedi".split("_"),
            weekdaysShort : "dim._lun._mar._mer._jeu._ven._sam.".split("_"),
            weekdaysMin : "Di_Lu_Ma_Me_Je_Ve_Sa".split("_"),
            longDateFormat : {
                LT : "HH:mm",
                L : "DD/MM/YYYY",
                LL : "D MMMM YYYY",
                LLL : "D MMMM YYYY LT",
                LLLL : "dddd D MMMM YYYY LT"
            },
            calendar : {
                sameDay: "[Aujourd'hui ] LT",
                nextDay: '[Demain ] LT',
                nextWeek: 'dddd [] LT',
                lastDay: '[Hier ] LT',
                lastWeek: 'dddd [dernier ] LT',
                sameElse: 'L'
            },
            relativeTime : {
                future : "dans %s",
                past : "il y a %s",
                s : "quelques secondes",
                m : "une minute",
                mm : "%d minutes",
                h : "une heure",
                hh : "%d heures",
                d : "un jour",
                dd : "%d jours",
                M : "un mois",
                MM : "%d mois",
                y : "un an",
                yy : "%d ans"
            },
            ordinal : function (number) {
                return number + (number === 1 ? 'er' : '');
            },
            week : {
                dow : 1, // Monday is the first day of the week.
                doy : 4  // The week that contains Jan 4th is the first week of the year.
            }
        });
        
        
        if ($.fullCalendar) {
            $.fullCalendar.lang('fr', {
                // strings we need that are neither in Moment nor datepicker
                "day": "Jour",
                "week": "Semaine",
                "month": "Mois",
                "list": "Mon planning"
            }, {
                // VALUES ARE FROM JQUERY-UI DATEPICKER'S TRANSLATIONS
                // (https://github.com/jquery/jquery-ui/blob/master/ui/i18n/jquery.ui.datepicker-fr.js)
                // 
                // Values that are reduntant because of Moment are not included here.
                //
                // When fullCalendar's lang method is called, it will merge this config with Moment's
                // and make this stuff available for jQuery UI's datepicker:
                //
                //     $.datepicker.regional['fr'] = ...
                //
                closeText: 'Fermer',
                prevText: 'Précédent',
                nextText: 'Suivant',
                currentText: 'Aujourd\'hui',
                dayNamesMin: ['D','L','M','M','J','V','S'],
                weekHeader: 'Sem.',
                dateFormat: 'dd/mm/yy',
                firstDay: 1,
                isRTL: false,
                showMonthAfterYear: false,
                yearSuffix: ''
            });
        }

    }

    // we need Moment and jQuery before we can begin...
    //
    if (typeof define === "function" && define.amd) {
        define(["moment", "jquery"], onload);
    }
    if (typeof window !== "undefined" && window.moment && window.jQuery) {
        onload(window.moment, window.jQuery);
    }

})();
    $(document).ready(function () {	
    	
    	setTimeout(() => {
    		 $('.fc-title').each(function(){
             	$(this).html($(this).text());
             });
		}, 10);
    	
        /* initialize the calendar
        -----------------------------------------------------------------*/
        $('#calendar').fullCalendar({
        	height: (window.innerHeight-160),
        	lang: 'fr',
            header: {
                left: 'prev,next today',
                center: 'title',
                right: 'month,agendaWeek,agendaDay'
            },
            editable: false,
            droppable: false, // this allows things to be dropped onto the calendar
            drop: function () {
                // is the "remove after drop" checkbox checked?
                if ($('#drop-remove').is(':checked')) {
                    // if so, remove the element from the "Draggable Events" list
                    $(this).remove();
                }
            },
            dayClick: function(date, jsEvent, view) {
            	$("#add-button").attr("params", "dt="+date.format()).trigger("click");
            },
            eventClick: function(event){
            	$("#link-popup").attr("params", "id="+event._id);
            	$('#link-popup').trigger('click');
            },
            events: [
            	<%
            	for(PlanningPersistant data : list_planning){
            		int diff_days = DateUtil.getDiffDays(data.getDate_debut(), data.getDate_fin());
            		
            		long timestamp = data.getDate_debut().getTime();
            		Calendar calDebut = Calendar.getInstance(), calFin = Calendar.getInstance();
            		calDebut.setTimeInMillis(timestamp);
            		
            		int start_day = calDebut.get(Calendar.DAY_OF_MONTH);
            		int start_month = calDebut.get(Calendar.MONTH);
            		int start_year = calDebut.get(Calendar.YEAR);
            		
            		int start_hour = calDebut.get(Calendar.HOUR_OF_DAY);
            		int start_minute = calDebut.get(Calendar.MINUTE);
            		
            		timestamp = data.getDate_fin().getTime();
            		calFin.setTimeInMillis(timestamp);
            		
            		int end_day = calFin.get(Calendar.DAY_OF_MONTH);
            		int end_month = calFin.get(Calendar.MONTH);
            		int end_year = calFin.get(Calendar.YEAR);
            		
            		int end_hour = calFin.get(Calendar.HOUR_OF_DAY);
            		int end_minute = calFin.get(Calendar.MINUTE);

            		String color = data.getOpc_type_planning().getColor();
            		
            		//-----------------------------------------------------------------------------------            		
            		String employeTitle = "", clientTitle = "";
            		
            		IPortefeuille2Service portefeuilleSrv = ServiceUtil.getBusinessBean(IPortefeuille2Service.class);
            		IArticleService articleSrv = ServiceUtil.getBusinessBean(IArticleService.class);
            		
            		String[] employes = StringUtil.getArrayFromStringDelim(data.getEmployes_str(), ";");
            		String[] clients = StringUtil.getArrayFromStringDelim(data.getClients_str(), ";");
            		
            		if(employes != null){
            			for(String employeId : employes){
            				if(StringUtil.isEmpty(employeId)){
            					continue;
            				}
            				EmployePersistant employeP = articleSrv.findById(EmployePersistant.class, Long.valueOf(employeId));
            				employeTitle += employeP.getNomPrenom()+" | ";
            			}
            		}
            		if(clients != null){
            			for(String clientId : clients){
            				if(StringUtil.isEmpty(clientId)){
            					continue;
            				}
            				ClientPersistant clientP = articleSrv.findById(ClientPersistant.class, Long.valueOf(clientId));
            				
            				if(data.getOpc_type_planning() != null && BooleanUtil.isTrue(data.getOpc_type_planning().getIs_situation_cli())){
            					BigDecimal solde = portefeuilleSrv.getSoldePortefeuilleMvm(Long.valueOf(clientId), "CLI");
            					boolean isDebiteur = (solde != null && solde.compareTo(BigDecimalUtil.ZERO)<0);
            					clientTitle += clientP.getNomPrenom()+" <span style=\"color:"+(isDebiteur?"red":"green")+";\">["+BigDecimalUtil.formatNumber(solde)+"€]</span> | ";
            				} else{
            					clientTitle += clientP.getNomPrenom()+" | ";
            				}
            			}
            		}
            		
            		if(StringUtil.isNotEmpty(clientTitle)){
            			clientTitle = " => <span style=\"color:blue;font-size:9px;\">" + clientTitle + "</span>";
            		}
            		if(StringUtil.isNotEmpty(employeTitle)){
            			clientTitle = " => <span style=\"color:blue;font-size:9px;\">" + employeTitle + "</span>";
            		}
            		//----------------------------------------------------------------------------------
            		
            		if(BooleanUtil.isTrue(data.getIs_all_day())){
            			%>
    	            	{
    	            		id: '<%=data.getId()%>',
    	    			    title: '<%=data.getTitre() + clientTitle + employeTitle %>',
    	    			    start: new Date(<%=start_year%>, <%=start_month%>, <%=start_day%>),
    	    			    borderColor: '<%=color%>'
    	    			},
                	<%} else{%>
		            	{
  	            		    id: '<%=data.getId()%>',
		            		title: '<%=data.getTitre() + clientTitle + employeTitle %>',
		    			    start: new Date(<%=start_year%>, <%=start_month%>, <%=start_day%>, <%=start_hour%>, <%=start_minute%>),
		    			    end: new Date(<%=end_year%>, <%=end_month%>, <%=end_day%>, <%=end_hour%>, <%=end_minute%>),
 		    				allDay: false,
							borderColor: '<%=color%>'
		    			},
            	<%	}
            	}%>
		    ]
        });

        $(".fc-month-button").text("Mois");
        $(".fc-agendaWeek-button").text("Semaine");
        $(".fc-agendaDay-button").text("Jour");
        $(".fc-today-button").text("Aujourd'hui");
    });
</script>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Calendrier</li>
		<li>Fiche calendrier</li>
		<li class="active">Recherche</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<%if("agenda-visite".equals(MessageService.getGlobalMap().get(ProjectConstante.LEFT_MENU_ID))){ %>
			<std:linkPopup actionGroup="C" id="add-button" classStyle="btn btn-default" action="agenda.clientVisite.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
		<%} else{ %>
			<std:linkPopup actionGroup="C" id="add-button" classStyle="btn btn-default" action="pers.planning.work_init_create" icon="fa-3x fa-plus" tooltip="Cr&eacute;er" />
			<std:link actionGroup="C" classStyle="btn btn-primary" action="pers.planning.vue_lieu" icon="fa-3x fa-map" value="Vue emplacement" tooltip="Vue emplacement" />
			<std:link actionGroup="C" id="add-button" classStyle="btn btn-primary" action="pers.typePlanning.work_find" icon="fa-3x fa-table" tooltip="Type planning" value="Types événements" />		
		<%} %>
	</div>
	<!--Header Buttons-->
	<jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
	<!--Header Buttons End-->
</div>
<!-- /Page Header -->

<!-- Page Body -->
<div class="page-body">
    <div class="row">
       <div class="widget flat">
           <div class="widget-body">
           		<std:linkPopup style="display:none;" id="link-popup" action="pers.planning.work_edit" />
               <div id='calendar'></div>
           </div><!--Widget Body-->
       </div>
    </div>
</div>

<!-- end widget div -->
