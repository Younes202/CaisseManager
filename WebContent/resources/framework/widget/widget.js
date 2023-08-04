var script = document.currentScript || 
/*Polyfill*/ Array.prototype.slice.call(document.getElementsByTagName('script')).pop();

var hostW = (script.getAttribute('src') + "/mob-client?act=init");

alert(hostW);

document.writeln(
	'<iframe id="elog_widget_frame" style="position: absolute;top: 20%;right: 1px;z-index:99999;" ' +
	'src="'+hostW+'" ' +
	'name="Elog software" width="380" height="700" align="left" ' +
	'scrolling="no" marginheight="0" marginwidth="0" frameborder="0">' +
	'</iframe>'
);