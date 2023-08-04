<%@page import="framework.model.util.MenuMappingService"%>
<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%> 
<%@page import="framework.controller.bean.MenuBean"%>
<style>
@import url(https://fonts.googleapis.com/css?family=Lato:300,400,700);

.contenaire-stl{ 
  	position: fixed;
	-moz-box-sizing: border-box;
	box-sizing: border-box;
	margin: 0;
	padding: 0;
	list-style: none;
	margin: 0;
    padding: 0;
    list-style: none;
    left: 50%;
    top: -33px;
    z-index: 999999999999;
}

.component {
	position: relative;
	margin-bottom: 3em;
	height: 15em;
	font-family: 'Lato', Arial, sans-serif;
}

.component > h2 {
	position: absolute;
	overflow: hidden;
	width: 100%;
	text-align: center;
	text-transform: uppercase;
	white-space: nowrap;
	font-weight: 300;
	font-style: italic;
	font-size: 12em;
	opacity: 0.1;
	cursor: default;
}

.cn-button {
	position: absolute;
	top: 130%;
	left: 13px;
	z-index: 11;
	margin-top: -2.25em;
	margin-left: -2.25em;
	padding-top: 0;
	width: 4.5em;
	height: 4.5em;
	border: none;
	border-radius: 50%;
	background: none;
	background-color: #fff;
	color: #52be7f;
	text-align: center;
	font-weight: 700;
	font-size: 1.5em;
	text-transform: uppercase;
	cursor: pointer;
	-webkit-backface-visibility: hidden;
}

.csstransforms .cn-wrapper {
	    position: absolute;
    top: 100%;
    left: 50%;
    z-index: 10;
    margin-top: -51.5em;
    margin-left: -14em;
    width: 30em;
    height: 1002px;
    border-radius: 50%;
    background: transparent;
    opacity: 0;
    -webkit-transition: all .3s ease 0.3s;
    -moz-transition: all .3s ease 0.3s;
    transition: all .3s ease 0.3s;
    -webkit-transform: scale(0.1);
    -ms-transform: scale(0.1);
    -moz-transform: scale(0.1);
    transform: scale(0.1);
    pointer-events: none;
    overflow: hidden;
}

/*cover to prevent extra space of anchors from being clickable*/
.csstransforms .cn-wrapper:after{
  content:".";	
  display:block;
  font-size:2em;
  width:6.2em;
  height:6.2em;
  position: absolute;
  left: 50%;
  margin-left: -3.1em;
  top:50%;
  margin-top: -3.1em;
  border-radius: 50%;
  z-index:10;
  color: transparent;
}

.csstransforms .opened-nav {
	border-radius: 50%;
	opacity: 1;
	-webkit-transition: all .3s ease;
	-moz-transition: all .3s ease;
	transition: all .3s ease;
	-webkit-transform: scale(1);
	-moz-transform: scale(1);
	-ms-transform: scale(1);
	transform: scale(1);
	pointer-events: auto;
}

.csstransforms .cn-wrapper li {
	position: absolute;
	top: 56%;
	left: 50%;
	overflow: hidden;
	margin-top: -1.3em;
	margin-left: -10em;
	width: 10em;
	height: 10em;
	font-size: 1.5em;
	-webkit-transition: all .3s ease;
	-moz-transition: all .3s ease;
	transition: all .3s ease;
	-webkit-transform: rotate(75deg) skew(62deg); 
	-moz-transform: rotate(75deg) skew(62deg); 
	-ms-transform: rotate(75deg) skew(62deg); 
	transform: rotate(75deg) skew(62deg); 
	-webkit-transform-origin: 100% 100%;
	-moz-transform-origin: 100% 100%;
	transform-origin: 100% 100%;
	pointer-events: none;
}

.csstransforms .cn-wrapper li a {
	position: absolute;

	right: -7.25em;
	bottom: -7.25em;
	display: block;
	width: 14.5em;
	height: 14.5em;
	border-radius: 50%;
	background: #000000;
	background: -webkit-radial-gradient(transparent 35%, #000000 35%);
	background: -moz-radial-gradient(transparent 35%, #000000 35%);
	background: radial-gradient(transparent 35%, #000000 35%);
	color: #fff;
	text-align: center;
	text-decoration: none;
	font-size: 1.2em;
	line-height: 2;
	-webkit-transform: skew(-62deg) rotate(-75deg) scale(1);
	-moz-transform: skew(-62deg) rotate(-75deg) scale(1);
	-ms-transform: skew(-62deg) rotate(-75deg) scale(1);
	transform: skew(-62deg) rotate(-75deg) scale(1);
	-webkit-backface-visibility: hidden;
	backface-visibility: hidden;
	pointer-events: auto;
}

.csstransforms .cn-wrapper li a span {
	position: relative;
	top: 1em;
	display: block;
	font-size: .5em;
	font-weight: 700;
	text-transform: uppercase;
}

.csstransforms .cn-wrapper li a:hover,
.csstransforms .cn-wrapper li a:active,
.csstransforms .cn-wrapper li a:focus {
	background: -webkit-radial-gradient(transparent 35%, #449e6a 35%);
	background: -moz-radial-gradient(transparent 35%, #449e6a 35%);
	background: radial-gradient(transparent 35%, #449e6a 35%);
}

.csstransforms .opened-nav li {
	-webkit-transition: all .3s ease .3s;
	-moz-transition: all .3s ease .3s;
	transition: all .3s ease .3s;
}

.csstransforms .opened-nav li:first-child {
	-webkit-transform: skew(62deg);
	-moz-transform: skew(62deg);
	-ms-transform: skew(62deg);
	transform: skew(62deg); 
}

.csstransforms .opened-nav li:nth-child(2) {
	-webkit-transform: rotate(30deg) skew(62deg);
	-moz-transform: rotate(30deg) skew(62deg);
	-ms-transform: rotate(30deg) skew(62deg);
	transform: rotate(30deg) skew(62deg);
}

.csstransforms .opened-nav  li:nth-child(3) {
	-webkit-transform: rotate(60deg) skew(62deg);
	-moz-transform: rotate(60deg) skew(62deg);
	-ms-transform: rotate(60deg) skew(62deg);
	transform: rotate(60deg) skew(62deg);
}

.csstransforms .opened-nav li:nth-child(4) {
	-webkit-transform: rotate(90deg) skew(62deg);
	-moz-transform: rotate(90deg) skew(62deg);
	-ms-transform: rotate(90deg) skew(62deg);
	transform: rotate(90deg) skew(62deg);
}

.csstransforms .opened-nav li:nth-child(5) {
	-webkit-transform: rotate(120deg) skew(62deg);
	-moz-transform: rotate(120deg) skew(62deg);
	-ms-transform: rotate(120deg) skew(62deg);
	transform: rotate(120deg) skew(62deg);
}

.csstransforms .opened-nav li:nth-child(6) {
	-webkit-transform: rotate(150deg) skew(62deg);
	-moz-transform: rotate(150deg) skew(62deg);
	-ms-transform: rotate(150deg) skew(62deg);
	transform: rotate(150deg) skew(62deg);
}

.csstransforms .opened-nav li:nth-child(7) {
	-webkit-transform: rotate(180deg) skew(62deg);
	-moz-transform: rotate(180deg) skew(62deg);
	-ms-transform: rotate(180deg) skew(62deg);
	transform: rotate(180deg) skew(62deg);
}

.csstransforms .opened-nav li:nth-child(8) {
	-webkit-transform: rotate(210deg) skew(62deg);
	-moz-transform: rotate(210deg) skew(62deg);
	-ms-transform: rotate(210deg) skew(62deg);
	transform: rotate(210deg) skew(62deg);
}
.csstransforms .opened-nav li:nth-child(9) {
	-webkit-transform: rotate(240deg) skew(62deg);
	-moz-transform: rotate(240deg) skew(62deg);
	-ms-transform: rotate(240deg) skew(62deg);
	transform: rotate(240deg) skew(62deg);
}
.csstransforms .opened-nav li:nth-child(10) {
	-webkit-transform: rotate(270deg) skew(62deg);
	-moz-transform: rotate(270deg) skew(62deg);
	-ms-transform: rotate(270deg) skew(62deg);
	transform: rotate(270deg) skew(62deg);
}
.csstransforms .opened-nav li:nth-child(11) {
	-webkit-transform: rotate(300deg) skew(62deg);
	-moz-transform: rotate(300deg) skew(62deg);
	-ms-transform: rotate(300deg) skew(62deg);
	transform: rotate(300deg) skew(62deg);
}
.csstransforms .opened-nav li:nth-child(12) {
	-webkit-transform: rotate(330deg) skew(62deg);
	-moz-transform: rotate(330deg) skew(62deg);
	-ms-transform: rotate(330deg) skew(62deg);
	transform: rotate(330deg) skew(62deg);
}
.no-csstransforms .cn-wrapper {
	overflow: hidden;
	margin: 10em auto;
	padding: .5em;
	text-align: center;
}

.no-csstransforms .cn-wrapper ul {
	display: inline-block;
}

.no-csstransforms .cn-wrapper li {
	float: left;
	width: 5em;
	height: 5em;
	background-color: #fff;
	text-align: center;
	font-size: 1em;
	line-height: 5em;
}

.no-csstransforms .cn-wrapper li a {
	display: block;
	width: 100%;
	height: 100%;
	color: inherit;
	text-decoration: none;
}

.no-csstransforms .cn-wrapper li a:hover,
.no-csstransforms .cn-wrapper li a:active,
.no-csstransforms .cn-wrapper li a:focus {
	background-color: #f8f8f8;
}

.no-csstransforms .cn-wrapper li.active a{
	background-color: #6F325C;
	color: #fff;
}

.no-csstransforms .cn-button {
	display: none;
}

@media only screen and (max-width: 620px) {
	.no-csstransforms li {
		width: 4em;
		height: 4em;
		line-height: 4em;
	}
}

@media only screen and (max-width: 500px) {
	.no-ccstransforms .cn-wrapper {
		padding: .5em;
	}

	.no-csstransforms .cn-wrapper li {
		width: 4em;
		height: 4em;
		font-size: .9em;
		line-height: 4em;
	}
}

@media only screen and (max-width: 480px) {
	.csstransforms .cn-wrapper {
		font-size: .68em;
	}

	.cn-button {
		font-size: 1em;
	}
}

@media only screen and (max-width:420px) {
	.no-csstransforms .cn-wrapper li {
		width: 100%;
		height: 3em;
		line-height: 3em;
	}
}
</style>

  <script>
  window.console = window.console || function(t) {};

  if (document.location.search.match(/type=embed/gi)) {
    window.parent.postMessage("resize", "*");
  }
  
  $(document).ready(function (){
		$(document).off('click', '.cn-wrapper a');
		$(document).on('click', '.cn-wrapper a', function(){
			$('#cn-button').trigger('click');
		});
  });
  
  function toggleMnuLeft(){
	  $("li[class='open']").find('a').first().trigger('click');
  }
</script> 

<%
String favoris_nav = (ContextAppli.getUserBean()!=null?ContextAppli.getUserBean().getFavoris_nav():"");
favoris_nav = (favoris_nav==null ? "" : favoris_nav);
String[] favArray = StringUtil.getArrayFromStringDelim(favoris_nav, ";");
%>

 <div class="contenaire-stl" style="display: none;">
 <div class="js flexbox flexboxlegacy canvas canvastext webgl no-touch geolocation postmessage websqldatabase indexeddb hashchange history draganddrop websockets rgba hsla multiplebgs backgroundsize borderimage borderradius boxshadow textshadow opacity cssanimations csscolumns cssgradients cssreflections csstransforms csstransforms3d csstransitions fontface generatedcontent video audio localstorage sessionstorage webworkers no-applicationcache svg inlinesvg smil svgclippaths">
  <div class="component">
    <!-- Start Nav Structure -->
    <button class="cn-button" id="cn-button">Fermer</button>
    <div class="cn-wrapper opened-nav" id="cn-wrapper">
      <ul>
      		<%
      		if(favArray != null){
	      		for(String mnu : favArray){
	        	MenuBean mnuBean = MenuMappingService.getMenuById(mnu);
	        	if(mnuBean == null || mnuBean.getLinkText() == null){
	        		continue;
	        	}
	        	%>
	        	<li>
	        		<a href="#lmnu=<%=mnu %>&rdm=<%=System.currentTimeMillis() %>" title="<%=mnuBean.getLinkText()%>" onclick="toggleMnuLeft();">
	        			<span style="transform: scale(1, 1.7);"><%=mnuBean.getLinkText().length()>7 ? mnuBean.getLinkText().substring(0, 7):mnuBean.getLinkText() %></span>
	        		</a>
	        	</li>
	        	<%}
	      	}%>
      </ul>
    </div>
    <!-- End of Nav Structure -->
  </div>
</div><!-- /container -->
</div>
  
      <script id="rendered-js">
/*!
      * classie - class helper functions
      * from bonzo https://github.com/ded/bonzo
      * 
      * classie.has( elem, 'my-class' ) -> true/false
      * classie.add( elem, 'my-new-class' )
      * classie.remove( elem, 'my-unwanted-class' )
      * classie.toggle( elem, 'my-class' )
      */

/*jshint browser: true, strict: true, undef: true */
/*global define: false */

(function (window) {

  'use strict';

  // class helper functions from bonzo https://github.com/ded/bonzo

  function classReg(className) {
    return new RegExp("(^|\\s+)" + className + "(\\s+|$)");
  }

  // classList support for class management
  // altho to be fair, the api sucks because it won't accept multiple classes at once
  var hasClass, addClass, removeClass;

  if ('classList' in document.documentElement) {
    hasClass = function (elem, c) {
      return elem.classList.contains(c);
    };
    addClass = function (elem, c) {
      elem.classList.add(c);
    };
    removeClass = function (elem, c) {
      elem.classList.remove(c);
    };
  } else
  {
    hasClass = function (elem, c) {
      return classReg(c).test(elem.className);
    };
    addClass = function (elem, c) {
      if (!hasClass(elem, c)) {
        elem.className = elem.className + ' ' + c;
      }
    };
    removeClass = function (elem, c) {
      elem.className = elem.className.replace(classReg(c), ' ');
    };
  }

  function toggleClass(elem, c) {
    var fn = hasClass(elem, c) ? removeClass : addClass;
    fn(elem, c);
  }

  var classie = {
    // full names
    hasClass: hasClass,
    addClass: addClass,
    removeClass: removeClass,
    toggleClass: toggleClass,
    // short names
    has: hasClass,
    add: addClass,
    remove: removeClass,
    toggle: toggleClass };


  // transport
  if (typeof define === 'function' && define.amd) {
    // AMD
    define(classie);
  } else {
    // browser global
    window.classie = classie;
  }

})(window);
// EventListener | @jon_neal | //github.com/jonathantneal/EventListener
!window.addEventListener && window.Element && function () {
  function addToPrototype(name, method) {
    Window.prototype[name] = HTMLDocument.prototype[name] = Element.prototype[name] = method;
  }

  var registry = [];

  addToPrototype("addEventListener", function (type, listener) {
    var target = this;

    registry.unshift({
      __listener: function (event) {
        event.currentTarget = target;
        event.pageX = event.clientX + document.documentElement.scrollLeft;
        event.pageY = event.clientY + document.documentElement.scrollTop;
        event.preventDefault = function () {event.returnValue = false;};
        event.relatedTarget = event.fromElement || null;
        event.stopPropagation = function () {event.cancelBubble = true;};
        event.relatedTarget = event.fromElement || null;
        event.target = event.srcElement || target;
        event.timeStamp = +new Date();

        listener.call(target, event);
      },
      listener: listener,
      target: target,
      type: type });


    this.attachEvent("on" + type, registry[0].__listener);
  });

  addToPrototype("removeEventListener", function (type, listener) {
    for (var index = 0, length = registry.length; index < length; ++index) {if (window.CP.shouldStopExecution(0)) break;
      if (registry[index].target == this && registry[index].type == type && registry[index].listener == listener) {
        return this.detachEvent("on" + type, registry.splice(index, 1)[0].__listener);
      }
    }window.CP.exitedLoop(0);
  });

  addToPrototype("dispatchEvent", function (eventObject) {
    try {
      return this.fireEvent("on" + eventObject.type, eventObject);
    } catch (error) {
      for (var index = 0, length = registry.length; index < length; ++index) {if (window.CP.shouldStopExecution(1)) break;
        if (registry[index].target == this && registry[index].type == eventObject.type) {
          registry[index].call(this, eventObject);
        }
      }window.CP.exitedLoop(1);
    }
  });
}();
//# sourceURL=pen.js


(function () {

  var button = document.getElementById('cn-button'),
  wrapper = document.getElementById('cn-wrapper');

  //open and close menu when the button is clicked
  var open = false;
  button.addEventListener('click', handler, false);
  
  function handler() {
    if(!open){
      $('.contenaire-stl').show();
      this.innerHTML = "Fermer";
      classie.add(wrapper, 'opened-nav');
      
    } else{
      this.innerHTML = "FAVORIS";
      classie.remove(wrapper, 'opened-nav');
      setTimeout(function(){ $('.contenaire-stl').hide(); }, 500);
    }
    open = !open;
  }
  function closeWrapper() {
    classie.remove(wrapper, 'opened-nav');
  }

})();
    </script>
