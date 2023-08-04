var token = document.getElementById('caisse-jtn').getAttribute('jtn');
var url = document.getElementById('caisse-jtn').getAttribute('url');
var URL_WIDGET = url+"?src=WEB_CLI&jtn="+token+window.location.search.replace('?', '&');
//
document.writeln(
//		'<a href="'+URL_WIDGET+'" style="position: absolute;'+
//    'top: 28px;'+
//    'right: 10px;'+
//    'z-index: 99999;'+
//    'border-radius: 16px;'+
//    'width: 280px;'+
//    'height: 49px;'+
//    'background-color: black;'+
//    'color: white;'+
//    'text-align: center;'+
//    'padding-top: 8px;'+
//    'font-size: 20px;'+
//    'font-weight: bold;'+
//    'border: 3px solid #ffffff;">COMMANDER EN LIGNE</a>'

		'<script type="text/javascript">'+
		' var eventMethod = window.addEventListener ? "addEventListener" : "attachEvent";'+
		 'var eventer = window[eventMethod];'+
		 ' var messageEvent = eventMethod == "attachEvent" ? "onmessage" : "message";'+
		 ' eventer(messageEvent,function(e) {'+
		 '	var iframe = document.getElementById("driver-widget-frame");'+
		'if (e.data.indexOf("xxxxx::") != -1) {'+
			
		'}'+
		' } ,false);'+
		'</script>'+

		'<iframe src="'+url+'" id="widget_cmd_frame" scrolling="no" style="position: absolute;border: 1px solid black;right: 9px;top: 20px;z-index: 999999;width: 380px;height: 660px;"></iframe>'
	); 