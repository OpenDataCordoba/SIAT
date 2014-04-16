<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>

<html:html locale="true">

<head>

<title><bean:message bundle="sat" key="index.title" /></title>
<html:base/>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<style type="text/css">
@import url("<%= request.getContextPath()%>/styles/layout.css");
@import url("<%= request.getContextPath()%>/styles/main.css");
@import url("<%= request.getContextPath()%>/styles/menu.css");
@import url("<%= request.getContextPath()%>/styles/abm.css");
@import url("<%= request.getContextPath()%>/styles/tables.css");
@import url("<%= request.getContextPath()%>/styles/aps.css");
</style>

<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
<META HTTP-EQUIV="Expires" content="-1">
<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-STORE">
<script>
window.history.forward();
if(window.history.forward(1) != null)
                 window.history.forward(1);

// handle keyboard events
if (navigator.appName == "Mozilla")
   document.addEventListener("keyup",keypress,true);
else if (navigator.appName == "Netscape")
     document.captureEvents(Event.KEYPRESS);
    
if (navigator.appName != "Mozilla")  
    document.onkeypress=keypress;

function keypress(e) { 
	var keyCode = '';
	
	if (navigator.appName == "Microsoft Internet Explorer") 
   		keyCode = window.event.keyCode;
	else if (navigator.appName == "Netscape")
		keyCode = e.which;
	else if (navigator.appName == "Mozilla")
    	keyCode = e.keyCode;

    if (keyCode == '13' )
	    return false;
}

</script>

</head>

<body>
	<table class="main" cellspacing="0" cellpadding="0">
		<tr>
			<td colspan="2">
				<tiles:insert attribute="header"/>	
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<tiles:insert attribute="loggedUser"/>	
			</td>
		</tr>
		<tr> 
			<td id="menu">
				<tiles:insert attribute="menu"/>				
			</td>
			<td width="100%" id="content">
				<tiles:insert attribute="body"/>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<tiles:insert attribute="footer"/>
			</td>
		</tr>
	</table>
</body>

</html:html>
