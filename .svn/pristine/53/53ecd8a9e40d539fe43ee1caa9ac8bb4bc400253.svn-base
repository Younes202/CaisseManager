<%@page import="framework.model.common.util.StrimUtil"%>
<%@ taglib uri="http://www.customtaglib.com/complexe" prefix="cplx"%>
<%@ taglib uri="http://www.customtaglib.com/standard" prefix="std"%>
<%@ taglib uri="http://www.customtaglib.com/html" prefix="html"%>
<%@ taglib uri="http://www.customtaglib.com/work" prefix="work"%>
<%@ taglib uri="http://www.customtaglib.com/c" prefix="c"%>
<%@page errorPage="/commun/error.jsp"%>

<style>
#map {
	width: 100%;
	height: 250px;
}

.gmnoprint, .gm-control-active {
	display: none;
}

#account_div div {
	margin-bottom: 5px;
}

.css {
	display: none;
	outline: 0;
	cursor: pointer;
	padding: 3px 12px;
	font-size: 14px;
	margin-top: 4px;
	font-weight: 500;
	line-height: 15px;
	vertical-align: middle;
	border: 1px solid;
	border-radius: 4px;
	color: #ffffff;
	background-color: #2ea44f;
	border-color: #1b1f2326;
	box-shadow: rgba(27, 31, 35, 0.04) 0px 1px 0px 0px,
		rgba(255, 255, 255, 0.25) 0px 1px 0px 0px inset;
	transition: 0.2s cubic-bezier(0.3, 0, 0.5, 1);
	transition-property: color, background-color, border-color;
}

button:hover {
	background-color: #2c974b;
	border-color: #1b1f2326;
	transition-duration: 0.1s;
}
</style>
<script type="text/javascript">
var map = null;
var marker = null;

function initMap(){
	map = new google.maps.Map(document.getElementById('map'), {
        zoom: 17
    });
    
    autocompleteGoogle();	
}

function getCurrentPosition(){
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(function(position) {
			
			var pos = {
					  lat: position.coords.latitude,
					  lng: position.coords.longitude
					};
	        
	        marker = new google.maps.Marker({
	        	  position: {lng: position.coords.longitude, lat: position.coords.latitude},
	        	  map: map
	        });
						
	        map.setCenter(pos);
	        getAdressByCoord(pos);
	        
		}, function() {
            handleLocationError(true, infoWindow, map.getCenter());
	    });
	} else{
		handleLocationError(false, infoWindow, map.getCenter());
	}
}

function autocompleteGoogle(){
	var input = document.getElementById('adresse');

    var autocomplete = new google.maps.places.Autocomplete(input);

    var infowindow = new google.maps.InfoWindow();
    var marker = new google.maps.Marker({
        map: map,
        anchorPoint: new google.maps.Point(0, -29)
    });

    autocomplete.addListener('place_changed', function() {
        infowindow.close();
        marker.setVisible(false);
        var place = autocomplete.getPlace();
        if (!place.geometry) {
            window.alert("Autocomplete's returned place contains no geometry");
            return;
        }
  
        // If the place has a geometry, then present it on a map.
        if (place.geometry.viewport) {
            map.fitBounds(place.geometry.viewport);
        } else {
            map.setCenter(place.geometry.location);
            map.setZoom(17);
        }
        marker.setIcon(({
            url: place.icon,
            size: new google.maps.Size(71, 71),
            origin: new google.maps.Point(0, 0),
            anchor: new google.maps.Point(17, 34),
            scaledSize: new google.maps.Size(35, 35)
        }));
        marker.setPosition(place.geometry.location);
        marker.setVisible(true);
    
        var address = '';
        if (place.address_components) {
            address = [
              (place.address_components[0] && place.address_components[0].short_name || ''),
              (place.address_components[1] && place.address_components[1].short_name || ''),
              (place.address_components[2] && place.address_components[2].short_name || '')
            ].join(' ');
        }
    
        infowindow.setContent('<div><strong>' + place.name + '</strong><br>' + address);
        infowindow.open(map, marker);
      
        document.getElementById('adresse').innerHTML = place.formatted_address;
        document.getElementById('position_lat').value = place.geometry.location.lat();
        document.getElementById('position_lng').value = place.geometry.location.lng();
    });
 	
    mapMouseListener();
}

function mapMouseListener(){
	// Configure the click listener.
    map.addListener("click", (mapsMouseEvent) => {
    	var pos = mapsMouseEvent.latLng;
		var val = String(mapsMouseEvent.latLng).split(",");
		var lat = val[0].replace("(", "");
		var lng = val[1].replace(")", ""); 
		
		if(marker == null){
			marker = new google.maps.Marker({
				position: pos, 
				map: map
			});
		} else{
			marker.setPosition( new google.maps.LatLng(lat, lng));
		}
		$("#position_lat").val(lat);
		$("#position_lng").val(lng);
		
		getAdressByCoord(pos);
		
    });
}

function getAdressByCoord(pos){
	var geocoder = new google.maps.Geocoder();
	  geocoder.geocode({
          'latLng': pos
      }, function (results, status) {
          if (status == google.maps.GeocoderStatus.OK) {
              if (results[0]) {
              	$('#adresse').val(results[0].formatted_address);
              }
          }
      });
}

function showPosition(){
	if($("#div_map").is(':visible')){
		$("#div_map").hide();
	} else{
		getCurrentPosition();
		$("#div_map").show();
	}
}

$(document).ready(function() {
  $("#GlobalSearchInput").keyup(function() {
    var x = document.getElementById('showSendButt');
    if($(this).val().replace(/-/g,'').replace(/_/g,'').length == 10) {
      x.style.display = 'block';
    } else {
      x.style.display = 'none';
    }
  });
  $("#showSendButt").click(function() {
	  $("#inputSms").show();
	  $(this).hide();
  });
  
});
</script>

	<div class="form-group">
		<std:label classStyle="control-label col-xs-4 col-md-4" value="Téléphone *" />
		<div class="col-xs-8 col-md-8">
			<std:text name="client.telephone" type="string" id="GlobalSearchInput" mask="99-99-99-99-99" style="width:120px;" />
         	<std:button classStyle="css"  id="showSendButt"  params="skipF=1" action="caisse.clientMobile.generateSmsToken" icon="fa-send" value="send" />
            <div id="inputSms" style="margin-top: 7px;display: none;">Type Verification Code :
             <input style="width:100px" type="text" name="token_val" />
            </div>  

		</div>
		<std:label classStyle="control-label col-xs-4 col-md-4" value="Mot de passe *"/>
		<div class="col-xs-8 col-md-8">
			<std:password name="client.password" placeholder="Mot de passe" type="string" style="width:120px;" maxlength="80" />
		</div> 
	</div>
	<div class="form-group">	
		<std:label classStyle="control-label col-xs-4 col-md-4" value="Nom *" />
		<div class="col-xs-8 col-md-8">
			<std:text name="client.nom" type="string" style="width:100%;" />
		</div>
		<std:label classStyle="control-label col-xs-4 col-md-4" value="Mail" />
		<div class="col-xs-8 col-md-8">
			<std:text name="client.mail" validator="email" type="string" style="width: 100%"/>
			<span style="font-size: 10px;color: #e91e63;">* Utile si vous perdez votre mot de passe</span>
		</div>
	</div>		
	<div class="form-group">
		<std:label classStyle="control-label col-xs-12 col-md-12" style="text-align:left;" value="Adresse" />
		<div class="col-xs-12 col-md-12">
			<std:text type="text" name="adresse" style="font-size: 12px; width: 90%;float:left;"/>
			<span onclick="showPosition()" style="text-align: center;line-height: 37px;margin-left: 6px;cursor: pointer;">
				<i class="fa fa-map-marker" style="font-size: 20px;color:red;"></i>
			</span>
		</div>
	</div>
	<div class="form-group" style="display: none;" id="div_map">
		<div id="map"></div>
	</div>
	<div class="form-group">
		<std:label classStyle="control-label col-xs-4 col-md-8" value="Recevoir offres" addSeparator="false" />
		<div class="col-xs-8 col-md-4">
			<std:checkbox name="client.is_offre_cmd" id="client.is_offre_cmd" checked="${client.is_offre_cmd }" />
		</div>
		<std:label classStyle="control-label col-xs-4 col-md-8" value="Recevoir notifications" addSeparator="false" />
		<div class="col-xs-8 col-md-4">
			<std:checkbox name="client.is_statut_cmd" id="client.is_statut_cmd" checked="${client.is_statut_cmd }" />
			<img class="tooltip-lg" data-toggle="tooltip" data-placement="top" data-original-title="Recevoir les notifications liées aux statuts des commandes et de la position du livreur" src="resources/framework/img/info.png" style="vertical-align: bottom;"/>
		</div>
	</div>
	
	<std:hidden id="position_lat" name="position_lat"/>
	<std:hidden id="position_lng" name="position_lng"/>
	
	<script src="https://maps.googleapis.com/maps/api/js?key=<%=StrimUtil.getGlobalConfigPropertie("api.google.key") %>&libraries=places&callback=initMap" async defer></script>
	