<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/cas/AdministrarSolicitud.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="cas" key="cas.solicitudEditAdapter.title"/></h1>	
	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>

	<!-- Solicitud -->
	<fieldset>
		<legend><bean:message bundle="cas" key="cas.solicitud.title"/></legend>
		
		<table class="tabladatos">
			<!-- Usuario alta y fecha alta -->
			<tr>
				<td><label><bean:message bundle="cas" key="cas.solicitud.usuarioAlta.label"/>: </label></td>
				<td class="normal">
					<bean:write name="solicitudAdapterVO" property="solicitud.usuarioAlta"/>
				</td>
			</tr>
			<tr>
				<td><label><bean:message bundle="cas" key="cas.solicitud.areaOrigen.label"/>: </label></td>
				<td class="normal">
					<bean:write name="solicitudAdapterVO" property="solicitud.areaOrigen.desArea"/>
				</td>
			</tr>
			<!-- Fecha alta-->
			<tr>
				<td><label><bean:message bundle="cas" key="cas.solicitud.fechaAlta.label"/>: </label></td>
				<td class="normal">
					<bean:write name="solicitudAdapterVO" property="solicitud.fechaAltaView"/>
				</td>
			</tr>
			
			<!-- Inclucion de Caso -->
			<tr>
				<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
				<td colspan="3">
					<bean:define id="IncludedVO" name="solicitudAdapterVO" property="solicitud"/>
					<bean:define id="voName" value="solicitud" />
					<%@ include file="/cas/caso/includeCaso.jsp" %>
				</td>
			</tr>
			<!-- Fin Inclucion de Caso -->	
			
			<!-- Tipo solicitud -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="cas" key="cas.tipoSolicitud.label"/>: </label></td>
				<td class="normal">
					<html:select name="solicitudAdapterVO" property="solicitud.tipoSolicitud.id" styleClass="select"  onchange="submitForm('paramAreaDestino', '');">
						<html:optionsCollection name="solicitudAdapterVO" property="listTipoSolicitud"  label="descripcion" value="id"  />
					</html:select>
				</td>
			</tr>
			<tr>
			
				<td><label><bean:message bundle="cas" key="cas.solicitud.areaDestino.label"/>: </label></td>
				<td class="normal">
					<bean:write name="solicitudAdapterVO" property="solicitud.areaDestino.desArea"/>
				</td>
			</tr>
			<!-- Asunto -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="cas" key="cas.solicitud.asuntoSolicitud.label"/>: </label></td>
				<td class="normal">
					<html:text name="solicitudAdapterVO" property="solicitud.asuntoSolicitud" size="15" maxlength="20" />
				</td>
			</tr>
			<!-- Descripcion -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="cas" key="cas.solicitud.descripcion.label"/>: </label></td>
				<td class="normal">
					<html:textarea name="solicitudAdapterVO" property="solicitud.descripcion" cols="80" rows="15"/>
				</td>
			</tr>
			
			<!-- Numero Cuenta -->
			<tr>				
				<td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.ref"/>: </label></td>
				<td class="normal" colspan="3">
					<html:text name="solicitudAdapterVO" property="solicitud.cuenta.numeroCuenta" size="20" disabled="true"/>
					<html:button property="btnBuscarCuenta"  styleClass="boton" onclick="submitForm('buscarCuenta', '');">
						<bean:message bundle="cas" key="cas.solicitudEditAdapter.button.buscarCuenta"/>
					</html:button>
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td class="normal" colspan="3">
					<html:button property="btnLimpiarCuenta"  styleClass="boton" onclick="submitForm('limpiarCampoCuenta', '');">
						<bean:message bundle="cas" key="cas.solicitudEditAdapter.button.limpiarCuenta"/>
					</html:button>
				</td>				
			</tr>					
		</table>
	</fieldset>	
	<!-- Solicitud -->

	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="solicitudAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="solicitudAdapterVO" property="act" value="agregar">
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