<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/pad/AdministrarCuentaTitular.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="pad" key="pad.cuentaTitularAdapter.title"/></h1>
		
		<!-- Contribuyente -->
			<!-- Inclusion de los datos del Contribuyente -->
			<bean:define id="contribuyenteVO" name="cuentaTitularAdapterVO" property="cuentaTitular.contribuyente"/>
			<%@ include file="/pad/contribuyente/includeContribuyente.jsp" %>
		<!-- Contribuyente -->
		
		<!-- Cuenta Titular -->
		<fieldset>
			<legend><bean:message bundle="pad" key="pad.cuenta.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="cuentaTitularAdapterVO" property="cuentaTitular.cuenta.recurso.desRecurso"/></td>
					<td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.label"/>: </label></td>
					<td class="normal"><bean:write name="cuentaTitularAdapterVO" property="cuentaTitular.cuenta.numeroCuenta"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="pad" key="pad.cuentaTitular.fechaDesde.label"/>: </label></td>
					<td class="normal"><bean:write name="cuentaTitularAdapterVO" property="cuentaTitular.fechaDesdeView"/></td>
					<td><label><bean:message bundle="pad" key="pad.cuentaTitular.fechaHasta.label"/>: </label></td>
					<td class="normal"><bean:write name="cuentaTitularAdapterVO" property="cuentaTitular.fechaHastaView"/></td>
				</tr>
			</table>
		</fieldset>
		<!-- Fin Cuenta Titular -->
				
	      <table class="tablabotones" width="100%">
	            <tr>
	                 <td align="left" width="50%">
	                   <html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	                      <bean:message bundle="base" key="abm.button.volver"/>
	                   </html:button>
	                </td>
	               <td align="right" width="50%">
		               <logic:equal name="cuentaTitularAdapterVO" property="act" value="eliminar">
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
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->