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
		
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
			
		<!-- Cuenta -->
		<fieldset>
			<legend><bean:message bundle="pad" key="pad.cuenta.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="cuentaTitularAdapterVO" property="cuentaTitular.cuenta.recurso.desRecurso"/></td>
					<td><label><bean:message bundle="pad" key="pad.objImp.label"/>: </label></td>
					<td class="normal"><bean:write name="cuentaTitularAdapterVO" property="cuentaTitular.cuenta.objImp.clave"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.label"/>: </label></td>
					<td class="normal"><bean:write name="cuentaTitularAdapterVO" property="cuentaTitular.cuenta.numeroCuenta"/></td>
					<td><label><bean:message bundle="pad" key="pad.cuenta.codGesCue.ref"/>: </label></td>
					<td class="normal"><bean:write name="cuentaTitularAdapterVO" property="cuentaTitular.cuenta.codGesCue"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="pad" key="pad.cuenta.fechaAlta.label"/>: </label></td>
					<td class="normal"><bean:write name="cuentaTitularAdapterVO" property="cuentaTitular.cuenta.fechaAltaView"/></td>
					<td><label><bean:message bundle="pad" key="pad.cuenta.fechaBaja.label"/>: </label></td>
					<td class="normal"><bean:write name="cuentaTitularAdapterVO" property="cuentaTitular.cuenta.fechaBajaView"/></td>
				</tr>
			</table>
		</fieldset>
		<!-- Fin Cuenta -->
		
		<!-- Cuenta Titular -->
		<fieldset width="100%">
			<legend><bean:message bundle="pad" key="pad.cuentaTitular.title"/></legend>
			<table class="tabladatos">
			<bean:define id="personaVO" name="cuentaTitularAdapterVO" property="cuentaTitular.contribuyente.persona" />
			<!-- Persona Fisica -->
			<logic:equal  name="personaVO" property="esPersonaFisica" value="true">
				<tr>
					<td><label><bean:message bundle="pad" key="pad.persona.nombres.label"/>: </label></td>
					<td class="normal"><bean:write name="personaVO" property="nombres"/></td>
					<td><label><bean:message bundle="pad" key="pad.persona.apellido.label"/>: </label></td>
					<td class="normal"><bean:write name="personaVO" property="apellido"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="pad" key="pad.tipoDocumento.label"/>: </label></td>
					<td class="normal"><bean:write name="personaVO" property="documento.tipoDocumento.abreviatura"/></td>
					<td><label><bean:message bundle="pad" key="pad.documento.numero.ref"/>: </label></td>
					<td class="normal"><bean:write name="personaVO" property="documento.numeroView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="pad" key="pad.persona.cuit.label"/>: </label></td>
					<td class="normal"><bean:write name="personaVO" property="cuit"/></td>
				</tr>
			</logic:equal>	
			<!-- Fin Persona Fisicar-->
			
			<!-- Persona Juridica-->
			<logic:notEqual  name="personaVO" property="esPersonaFisica" value="true">
				<tr>
					<td><label><bean:message bundle="pad" key="pad.persona.razonSocial.label"/>: </label></td>
					<td class="normal"><bean:write name="personaVO" property="razonSocial"/></td>
					<td><label><bean:message bundle="pad" key="pad.persona.cuit.label"/>: </label></td>				
					<td class="normal"><bean:write name="personaVO" property="cuit"/></td>
				</tr>		
			</logic:notEqual>	
			<!-- Fin Persona Juridica -->	
	
			<tr>
				<td><label><bean:message bundle="pad" key="pad.tipoTitular.label"/>: </label></td>
				<td class="normal"><bean:write name="cuentaTitularAdapterVO" property="cuentaTitular.tipoTitular.desTipoTitular"/> </td>
			</tr>
			<tr>	
				<td><label><bean:message bundle="pad" key="pad.cuentaTitular.esTitularPrincipal.label"/>: </label></td>
				<td class="normal"><bean:write name="cuentaTitularAdapterVO" property="cuentaTitular.esTitularPrincipal.value"/> </td>
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
			               <logic:equal name="cuentaTitularAdapterVO" property="act" value="marcarPrincipal">
			                  <html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('marcarTitular', '');">
			                     <bean:message bundle="pad" key="pad.cuentaTitularAdapter.button.marcar"/>
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