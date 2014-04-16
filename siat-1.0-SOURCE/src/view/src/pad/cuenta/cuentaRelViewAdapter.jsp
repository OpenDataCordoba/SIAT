<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/pad/AdministrarCuentaRel.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="pad" key="pad.cuentaRelAdapter.title"/></h1>
		
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
			
		<!-- Cuenta Origen -->
		<fieldset>
			<legend><bean:message bundle="pad" key="pad.cuentaRel.cuentaOrigen.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="cuentaRelAdapterVO" property="cuentaRel.cuentaOrigen.recurso.desRecurso"/></td>
					<td><label><bean:message bundle="pad" key="pad.objImp.label"/>: </label></td>
					<td class="normal"><bean:write name="cuentaRelAdapterVO" property="cuentaRel.cuentaOrigen.objImp.clave"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.label"/>: </label></td>
					<td class="normal"><bean:write name="cuentaRelAdapterVO" property="cuentaRel.cuentaOrigen.numeroCuenta"/></td>
					<td><label><bean:message bundle="pad" key="pad.cuenta.codGesCue.ref"/>: </label></td>
					<td class="normal"><bean:write name="cuentaRelAdapterVO" property="cuentaRel.cuentaOrigen.codGesCue"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="pad" key="pad.cuenta.fechaAlta.label"/>: </label></td>
					<td class="normal"><bean:write name="cuentaRelAdapterVO" property="cuentaRel.cuentaOrigen.fechaAltaView"/></td>
					<td><label><bean:message bundle="pad" key="pad.cuenta.fechaBaja.label"/>: </label></td>
					<td class="normal"><bean:write name="cuentaRelAdapterVO" property="cuentaRel.cuentaOrigen.fechaBajaView"/></td>
				</tr>
			</table>
		</fieldset>
		<!-- Fin Cuenta Origen-->
		
		<!-- Cuenta Destino -->
		<fieldset>
			<legend><bean:message bundle="pad" key="pad.cuentaRel.cuentaDestino.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="cuentaRelAdapterVO" property="cuentaRel.cuentaDestino.recurso.desRecurso"/></td>
					<td><label><bean:message bundle="pad" key="pad.objImp.label"/>: </label></td>
					<td class="normal"><bean:write name="cuentaRelAdapterVO" property="cuentaRel.cuentaDestino.objImp.clave"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.label"/>: </label></td>
					<td class="normal"><bean:write name="cuentaRelAdapterVO" property="cuentaRel.cuentaDestino.numeroCuenta"/></td>
					<td><label><bean:message bundle="pad" key="pad.cuenta.codGesCue.ref"/>: </label></td>
					<td class="normal"><bean:write name="cuentaRelAdapterVO" property="cuentaRel.cuentaDestino.codGesCue"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="pad" key="pad.cuenta.fechaAlta.label"/>: </label></td>
					<td class="normal"><bean:write name="cuentaRelAdapterVO" property="cuentaRel.cuentaDestino.fechaAltaView"/></td>
					<td><label><bean:message bundle="pad" key="pad.cuenta.fechaBaja.label"/>: </label></td>
					<td class="normal"><bean:write name="cuentaRelAdapterVO" property="cuentaRel.cuentaDestino.fechaBajaView"/></td>
				</tr>
			</table>
		</fieldset>
		<!-- Fin Cuenta Destino-->

	
		<fieldset width="100%">
		<legend><bean:message bundle="pad" key="pad.cuentaRel.cuentaDestino.title"/></legend>
		<table class="tabladatos">	
			<tr>
				<td><label><bean:message bundle="pad" key="pad.cuentaRel.fechaDesde.label"/>: </label></td>
				<td class="normal"><bean:write name="cuentaRelAdapterVO" property="cuentaRel.fechaDesdeView"/></td>
				<td><label><bean:message bundle="pad" key="pad.cuentaRel.fechaHasta.label"/>: </label></td>
				<td class="normal"><bean:write name="cuentaRelAdapterVO" property="cuentaRel.fechaHastaView"/></td>
			</tr>
		</table>
		</fieldset>
			
	      <table class="tablabotones" width="100%">
	            <tr>
		             <td align="left" width="50%">
		                   <html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
		                      <bean:message bundle="base" key="abm.button.volver"/>
		                   </html:button>
		             </td>
	               <td align="right" width="50%">
			               <logic:equal name="cuentaRelAdapterVO" property="act" value="eliminar">
			                  <html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
			                     <bean:message bundle="base" key="abm.button.eliminar"/>
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

	<!-- Inclucion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->