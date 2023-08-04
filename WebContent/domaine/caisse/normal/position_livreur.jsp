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
		<li class="active">Positions livreurs</li>
	</ul>
</div>
<!-- /Page Breadcrumb -->
<!-- Page Header -->
<div class="page-header position-relative">
	<div class="header-title" style="padding-top: 4px;">
		<std:select name="livreur" type="long[]" data="${listLivreur }" style="width:250px;" multiple="true" key="id" labels="login" />
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
		<div class="col-sm-12 col-md-12">
				<div id="mapLivr"></div>
		</div>
				
	<script type="text/javascript">
	      // This example requires the Drawing library. Include the libraries=drawing
	      // parameter when you first load the API. For example:
	      function initMap() {
	    	  
	    	  map = new google.maps.Map(document.getElementById('mapLivr'), {
	           	  center: {lat: 0, lng: 0},
	              zoom: 6
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
	        		 "<p style='margin-bottom: 2%;'><b>T&eacute;l&eacute;phone: </b><%=(livreur.getOpc_employe()!=null?StringUtil.getValueOrEmpty(livreur.getOpc_employe().getTelephone()) : "")%></p>" +
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
	    	
	    </script>
	    <script src="https://maps.googleapis.com/maps/api/js?key=<%=StrimUtil.getGlobalConfigPropertie("api.google.key") %>&libraries=geometry&callback=initMap"
	         async defer></script>			
				   
	</div>	
</div>