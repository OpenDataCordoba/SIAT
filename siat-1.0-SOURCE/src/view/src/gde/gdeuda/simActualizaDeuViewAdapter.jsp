<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<%@ page import="coop.tecso.demoda.iface.helper.*" %>
<%@ page import="ar.gov.rosario.siat.gde.buss.bean.*" %>
<%@ page import="java.lang.*" %>
<%@ page import="java.util.*" %>

<%
	String importe = request.getParameter("importe");
	String fechaActualizacion = request.getParameter("fechaActualizacion");
	String fechaOriginal = request.getParameter("fechaOriginal");
	if (importe == null) importe = "";
	if (fechaActualizacion == null) fechaActualizacion = "";
	if (fechaOriginal == null) fechaOriginal = "";

	
	String importeOrig = "";
	String importeActualizado = "";
	String actualizacion = "";
	String coeficiente = "";
	
	Date fActualizacion = DateUtil.getDate(request.getParameter("fechaActualizacion"));
	Date fOriginal = DateUtil.getDate(request.getParameter("fechaOriginal"));
	Double dImporte = NumberUtil.getDouble(request.getParameter("importe"));
		
	if (dImporte != null && fActualizacion != null && fOriginal != null) {		
		DeudaAct dact = ActualizaDeuda.getInstance().actualizar(fActualizacion, fOriginal, dImporte, false, true);
		importeOrig = StringUtil.formatDoubleWithComa(dact.getImporteOrig()); 
		importeActualizado = StringUtil.formatDoubleWithComa(dact.getImporteAct());
		actualizacion = StringUtil.formatDoubleWithComa(dact.getRecargo());
		coeficiente = StringUtil.formatDouble(dact.getCoeficiente());
	}
	
%>

<!-- Tabla que contiene todos los formularios -->
<form name="filter" id="filter" action="/siat/gde/AdministrarSimActualizaDeu.do" method="POST">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1>C&aacute;lculo acutlizaci&oacute;n deuda</h1>	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- Actualizacion -->
	<fieldset>
		<legend>Calcular Actualizaci&oacute;n</legend>
		
		<table class="tabladatos">
			<!-- TextCodigo -->
			<tr>
				<td><label>(*)&nbsp;Importe: </label></td>
				<td class="normal"><input type="text" name="importe" size="12" value="<%= importe %>"/></td>			
				<td></td>
				<td class="normal"></td>
			</tr>

			<tr>
				<td><label>(*)&nbsp;Fecha Original: </label></td>
				<td class="normal">
					<input type="text" name="fechaOriginal" id="fechaOriginal" size="12" value="<%= fechaOriginal %>"/>
					<a class="link_siat" onclick="return show_calendar('fechaOriginal');" id="a_fechaOriginal">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/> 
					</a>				
				</td>			
			</tr>

			<tr>
				<td><label>(*)&nbsp;Fecha Actualizaci&oacute;n: </label></td>
				<td class="normal">
					<input type="text" name="fechaActualizacion" id="fechaActualizacion" size="12" value="<%= fechaActualizacion %>"/>
					<a class="link_siat" onclick="return show_calendar('fechaActualizacion');" id="a_fechaActualizacion">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/>
					</a>
				</td>
			</tr>

			<tr>
				<td colspan="4"><hr/></td>
			</tr>

			<tr>
				<td><label>Importe: </label></td>
				<td class="normal" style="text-align:left">
					<%= importeOrig %>
				</td>
			
				<td><label>Coeficiente: </label></td>
				<td class="normal" style="text-align:left">
					<%= coeficiente %>
				</td>
			</tr>
			
			</tr>

			<tr>
				<td><label>Actualizaci&oacute;n: </label></td>
				<td class="normal" style="text-align:left">
					<%= actualizacion %>
				</td>
			</tr>

			<tr>
				<td><label>Importe Actualizado: </label></td>
				<td class="normal" style="text-align:left">
					<%= importeActualizado %>
				</td>
			</tr>
						
		</table>
	</fieldset>	
	<!-- Calcular -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('ver', '');">
					Calcular
				</html:button>
   	    	</td>   	    	
   	    </tr>
   	</table>
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</form>
<!-- Fin Tabla que contiene todos los formularios -->
