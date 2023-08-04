<%@page import="framework.model.common.util.EncryptionUtil"%>
<%@page import="framework.model.common.util.StrimUtil"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="appli.model.domaine.administration.persistant.UserPersistant"%>
<%@page import="java.util.List"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/fmt" prefix="fmt"%>
<%@page import="framework.controller.ControllerUtil"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>

<style>
     #mapLivr {
       height: 550px;
        width: 100%;
     }
</style>

<%
List<UserPersistant> list_livreur = (List<UserPersistant>) request.getAttribute("listLivreur");
list_livreur = (list_livreur == null ? new ArrayList() : list_livreur);
%>

<!-- Page Breadcrumb -->
<div class="page-breadcrumbs breadcrumbs-fixed">
	<ul class="breadcrumb">
		<li><i class="fa fa-home"></i> <a href="#">Accueil</a></li>
		<li>Gestion des livraisons</li>
		<li class="active">Trajet livreurs</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
	</div>
	<!--Header Buttons-->
	<jsp:include page="/WEB-INF/fragment/shortcut.jsp"></jsp:include>
	<!--Header Buttons End-->
</div>
<!-- /Page Header -->

<!-- Page Body -->
<div class="page-body">
	<div class="row">
		<jsp:include page="/WEB-INF/commun/center/banner_message.jsp"></jsp:include>
	</div>

	<!-- row -->
	<div class="row">
		<std:form name="search-form">
			<div class="col-md-12">	
				<div class="form-group">
					<std:label classStyle="control-label col-md-1" value="Date début" />
					<div class="col-md-2" style="width: 160px">
						<std:date name="date_debut" required="true" value="${date_debut }" />
					</div>
					<div class="col-md-1">
						<std:text name="heure_debut" type="string" style="width:60px;" mask="99:00"  value='${heure_debut}'/>
					</div>
					<std:label classStyle="control-label col-md-1" value="Date fin" />
					<div class="col-md-2" style="width: 160px">
						<std:date name="date_fin" required="true" value="${date_fin }" />
					</div>
					<div class="col-md-1">
						<std:text name="heure_fin" type="string" style="width:60px;" mask="99:00"  value='${heure_fin}'/>
					</div>
					<std:label classStyle="control-label col-md-1" value="Livreur" />
					<div class="col-md-2">
						<std:select name="livreur_id" type="long" style="width:100%;" data="${listLivreur }" key="id" labels="login" value="${livreur_id }"/>
					</div>
				</div>
				
				<div class="form-group" style="float: right; margin: 0px 10px 10px 0px">
					<std:link id="refersh_pos" actionGroup="C" classStyle="btn btn-primary" icon="icon-refresh" tooltip="Actualiser" value="Actualiser"/>
				</div>
			</div>	
			<hr style="border-top: 1px solid rgba(126, 108, 108, 0.2);">
			<div id="mapLivr"></div>
		</std:form>
				
	<script type="text/javascript">
		var route = null;
		$(document).ready(function (){
			 $("#refersh_pos").click(function(){
				 drawTrajet();
				 
			 });
		 });
	
	      function initMap() {
	    	  
	    	  map = new google.maps.Map(document.getElementById('mapLivr'), {
	           	  center: {lat: 0, lng: 0},
	              zoom: 6
	          });
	    	  
	    	  route = new google.maps.Polyline({
                path: [],
                geodesic : true,
                strokeColor: '#299df3',
                strokeOpacity: 1.0,
                strokeWeight: 5,
                editable: false,
                map:map
            });
	    	  	    		
	    	  getPositionsLivreur();
	      }
	      
	      function handleLocationError(browserHasGeolocation, infoWindow, pos) {
//	          infoWindow.setPosition(pos);
// 	          infoWindow.setContent(browserHasGeolocation ?
// 	                                'Error: The Geolocation service failed.' :
// 	                                'Error: Your browser doesn\'t support geolocation.');
//	          infoWindow.open(map);
	      }
	      
	      function getCurrentPosition(){
	        	 infoWindow = new google.maps.InfoWindow;
		    	 
		    	 if (navigator.geolocation) {
		              navigator.geolocation.getCurrentPosition(function(position) {
		                var pos = {
		                  lat: position.coords.latitude,
		                  lng: position.coords.longitude
		                };

		                infoWindow.setPosition(pos);
		                infoWindow.setContent('Ma position');
		                infoWindow.open(map);
		                map.setCenter(pos);
		              }, function() {
		                handleLocationError(true, infoWindow, map.getCenter());
		              });
		          } else {
		              // Browser doesn't support Geolocation
		              handleLocationError(false, infoWindow, map.getCenter());
		          }
	         }
	      
	      function getPositionsLivreur(){
	    	 
	    	  var lasPost = null;
	          var posyPolyArray = [];
	          var bounds = new google.maps.LatLngBounds();
	          
		      <%
		      	int i = 0;
		         for(UserPersistant livreur : list_livreur){
		        	if(livreur.getPosition_lng() == null){
		            	continue;
		            }
		        	i++;
		       %>
		        	
		        	var imgUrl = '<%=StringUtil.isEmpty(livreur.getImage()) ? "resources/restau/img/needleleftyellow2.png" : livreur.getImage() %>';
		        		
		        	var image = {
		        		url: imgUrl,
		        	};
		        		
		        	lasPost = {lng: <%=livreur.getPosition_lng() %>, lat: <%=livreur.getPosition_lat() %> };
		        		
		        	posyPolyArray.push({lat: <%=livreur.getPosition_lat() %>, lng: <%=livreur.getPosition_lng() %>});
		        	
		        	var nom_livreur = "<%=livreur.getLogin()%>";
		        	
		        	var content = "<div id='content<%=i%>'>"+
	        		 "<div id='siteNotice<%=i%>'>"+
	        		 "</div>"+
	        		 "<div style='display: flex'>"+
	        		 "<p style='margin-bottom: 6%;font-size:20px;color:#5a94eb'><b>"+nom_livreur+"</b></p>"+
	        		 "</div>"+
	        		 "<div id='bodyContent'>"+
	        		 "<p style='margin-bottom: 2%;'><b>T&eacute;l&eacute;phone: </b><%=(livreur.getOpc_employe()!=null?StringUtil.getValueOrEmpty(livreur.getOpc_employe().getTelephone()):"-")%></p>" +
	        		 "</div>"+
	        		 "</div>";
		        		
		        	var marker = new google.maps.Marker({
				        title: nom_livreur,
		   	            position: lasPost,
		   	           	icon: image
		   	        });
		        	
		        	var infowindow = new google.maps.InfoWindow();
			        google.maps.event.addListener(marker,'click', (function(marker, content, infowindow){ 
			        	return function() {
			                infowindow.setContent(content);
			                infowindow.open(map, marker);
			            };
			        })(marker, content, infowindow)); 
		        	
				    marker.setMap(map);
				    bounds.extend(lasPost);
					
				    map.setCenter(lasPost);
		        <%}
					if(i == 0){		        
		        %>
		        	getCurrentPosition();
		        <%}%>
		        
		        console.log(JSON.stringify(bounds));
		        map.fitBounds(bounds);
			    
			 }

	      function drawTrajet(){
	    	  var bounds = new google.maps.LatLngBounds();
	    	  
	    	  var frontUrl = "front?w_f_act=<%=EncryptionUtil.encrypt("pers.chauffeur.find_trajets")%>&isR=1";
	  		  showSpinner();
	          
	          $.ajax({
	  	        beforeSend: function(data) {
	  	        },
	  	        url: frontUrl,
	  	        data: $("#search-form").serialize(),
	  	        type: "POST",
	  	        cache: false,
	  	        dataType: 'text',
	  	        error: function(data) {
	  	        	console.debug(data);
	  	        },
	  	        success: function(data) {
	  	        	hideSpinner();
	  	        	var parsedJSON = JSON.parse(data);
	  	        	
	  	        	var coords = [];
	  	        	
	  	            for (var i=0;i<parsedJSON.length;i++) {
	  	            	var posLivreur = parsedJSON[i];
	  	            	
	  	            	if(posLivreur.position_lng == null){
	  	            		continue;
	  	            	}
	  	            	
	  	            	var pos = {"lat": posLivreur.position_lat, "lng": posLivreur.position_lng};
	  	            	if(i==0){
	  	            		var markerDep = new google.maps.Marker({
		  	 		             position: pos,
		  	 		             map: map
		  	 		        });
	  	            		markerDep.setMap(map);
	  	            	}
	  	            	if(i==(parsedJSON.length-1)){
	  	            		var markerDest = new google.maps.Marker({
		  	 		             position: pos,
		  	 		             map: map
		  	 		        });
	  	            		markerDest.setMap(map);
	  	            	}
	  	            	
	  	            	coords.push(pos);
	  	            	
	  	            	bounds.extend(pos);
	  	            }

	  	            route.setPath([]);
	  	            
	  	          	var i;
	  	            for (i = 0; i < coords.length; i++) {
	                     route.getPath().push(new google.maps.LatLng(coords[i].lat, coords[i].lng));
	  	            }
	  	            map.fitBounds(bounds);
	  		    }
	  	     });
	      	
	      }	
	    </script>
	    <script src="https://maps.googleapis.com/maps/api/js?key=<%=StrimUtil.getGlobalConfigPropertie("api.google.key") %>&libraries=geometry&callback=initMap"
	         async defer></script>			
				   
	</div>	
</div>