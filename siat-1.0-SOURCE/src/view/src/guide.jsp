<%@ page import="ar.gov.rosario.siat.base.view.util.*, ar.gov.rosario.siat.base.iface.model.*" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<% 
	String g = request.getParameter("g"); 
	int demo = SiatParam.getInteger("Demo", 0);
	if (demo != 1) return;
%>

<% if ("login".equals(g)) { %>
	<p>Probar con:</p>
	<blockquote>
		<p><b>Usuario: </b>demo &nbsp;&nbsp;&nbsp;<b>Contraseña: </b>demo</p>
	</blockquote>
<% } %>

<% if ("liqDeudaIngresoGr".equals(g)) { %>
	<p>Probar con:</p>
	<blockquote>
		<p>Tasa Inmobiliaria / 1000</p> 
		<p>Tasa Inmobiliaria / 1001</p>
		<p>Derecho Inspección / 3000</p> 
		<p>Derecho Inspección / 3001</p>
	</blockquote>
<% } %>

<% if ("menu".equals(g)) { %>
<div style="clear:both">
	<br/><br/>
	<p>Algunas opciones útiles:</p>
	<blockquote>
		<p><b>Gestión de Deuda -> Gestión Deuda / Convenio -> Gestión por Recurso/Cuenta</b></p>
		<p>Cuenta -> Cuenta -> Mantenedor de Cuentas</p>
		<p>Emisión -> Emisión -> Emisión Masiva</p> 
		<p>Emisión -> Emisión -> Emisión Extraordinaria</p> 
		<p>Admin. de Sistema -> Recursos -> Mantenedor de Recurso Tributario</p> 
		<p>Definición -> Gestión Deuda -> Mantenedor de Plan de Pago</p> 
	</blockquote>
</div>
<% } %> 