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
		<p><b>Usuario: </b>demo &nbsp;&nbsp;&nbsp;<b>Contrase�a: </b>demo</p>
	</blockquote>
<% } %>

<% if ("liqDeudaIngresoGr".equals(g)) { %>
	<p>Probar con:</p>
	<blockquote>
		<p>Tasa Inmobiliaria / 1000</p> 
		<p>Tasa Inmobiliaria / 1001</p>
		<p>Derecho Inspecci�n / 3000</p> 
		<p>Derecho Inspecci�n / 3001</p>
	</blockquote>
<% } %>

<% if ("menu".equals(g)) { %>
<div style="clear:both">
	<br/><br/>
	<p>Algunas opciones �tiles:</p>
	<blockquote>
		<p><b>Gesti�n de Deuda -> Gesti�n Deuda / Convenio -> Gesti�n por Recurso/Cuenta</b></p>
		<p>Cuenta -> Cuenta -> Mantenedor de Cuentas</p>
		<p>Emisi�n -> Emisi�n -> Emisi�n Masiva</p> 
		<p>Emisi�n -> Emisi�n -> Emisi�n Extraordinaria</p> 
		<p>Admin. de Sistema -> Recursos -> Mantenedor de Recurso Tributario</p> 
		<p>Definici�n -> Gesti�n Deuda -> Mantenedor de Plan de Pago</p> 
	</blockquote>
</div>
<% } %> 