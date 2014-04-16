<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/AdministrarCaja69.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="bal" key="bal.caja69EditAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- Caja69 -->
	<fieldset>
		<legend><bean:message bundle="bal" key="bal.caja69.title"/></legend>
		
		<table class="tabladatos">
		<tr>
			<!-- Sistema -->
			<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.sistema.label"/>: </label></td>
			<td class="normal"><html:text name="caja69AdapterVO" property="caja69.sistemaView" size="5" maxlength="10"/></td>			
		</tr>
		<tr>
			<!-- Nro Comprobante -->
			<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.transaccion.nroComprobante.label"/>: </label></td>
			<td class="normal"><html:text name="caja69AdapterVO" property="caja69.nroComprobanteView" size="15" maxlength="10"/></td>			
			<!-- Clave -->
			<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.transaccion.clave.label"/>: </label></td>
			<td class="normal"><html:text name="caja69AdapterVO" property="caja69.clave" size="10" maxlength="6"/></td>			
		<tr>
		<tr>
			<!-- Fecha Pago -->
			<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.transaccion.fechaPago.label"/>: </label></td>
			<td class="normal">
				<html:text name="caja69AdapterVO" property="caja69.fechaPagoView" styleId="fechaPagoView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaPagoView');" id="a_fechaPagoView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>
		    <!-- Importe -->
			<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.transaccion.importe.label"/>: </label></td>
			<td class="normal"><html:text name="caja69AdapterVO" property="caja69.importeView" size="20" maxlength="100"/></td>			
		</tr>
		</table>
	</fieldset>	
	<!-- Caja69 -->
	
	<table class="tablabotones">
	   	<tr>
  	   		<td align="left">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="left">
				<logic:equal name="caja69AdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="caja69AdapterVO" property="act" value="agregar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
						<bean:message bundle="base" key="abm.button.agregar"/>
					</html:button>
				</logic:equal>
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
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
