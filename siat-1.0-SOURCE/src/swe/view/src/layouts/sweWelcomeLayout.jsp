<%@ page errorPage="/error.jsp" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>

<!DOCTYPE html 
    PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html:html>
	
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<style type="text/css">
	@import url("<%= request.getContextPath()%>/styles/swe.css");
	</style>
	<title><bean:message bundle="base" key="index.title" /></title>
	<html:base/>
</head>

<body>
	<div id="container">
		<div id="top">
			<tiles:insert attribute="header"/>
		</div>

		<!-- div id="menu">
			<tiles:insert attribute="menutab"/>
		</div-->

		<div id="contenido">	
			<tiles:insert attribute="body"/>
		</div>

		<h1>&nbsp;</h1>

		<div id="pie">
			<tiles:insert attribute="footer"/>
		</div>
	</div>
</body>

</html:html>
