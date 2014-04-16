<%@ taglib uri="/tags/struts-bean"  prefix="bean" %>
<%@ taglib uri="/tags/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/tags/struts-html"  prefix="html" %>

<%@page contentType="application/vnd.ms-excel"%>

<% String nombre = "fichero.xls";
response.setHeader("Content-Disposition","attachment; filename=\""+ nombre + "\"");%>

<html>

<head>

<title><bean:message bundle="aps" key="index.title" /></title>
<html:base/>

<style type="text/css">
@import url("<%= request.getContextPath()%>/styles/layout.css");
@import url("<%= request.getContextPath()%>/styles/main.css");
@import url("<%= request.getContextPath()%>/styles/menu.css");
@import url("<%= request.getContextPath()%>/styles/abm.css");
@import url("<%= request.getContextPath()%>/styles/tables.css");
@import url("<%= request.getContextPath()%>/styles/aps.css");
</style>


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

</html>
