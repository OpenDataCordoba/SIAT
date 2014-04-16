<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/pad/AdministrarCambiarDomEnvio.do">
	
	<logic:equal name="userSession" property="isAnonimoView" value="1">
		<%@ include file="/gde/gdeuda/includeDivButtons.jsp" %>
	</logic:equal>	

		
	<h1><bean:message bundle="pad" key="pad.cambiarDomEnvioAdapter.title"/>
		<bean:write name="cambiarDomEnvioAdapterVO" property="cuenta.recurso.desRecurso"/> 
	
	</h1>	

	<!-- CambiarDomEnvio -->
	<fieldset>
		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>				

			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="pad" key="pad.cambiarDomEnvioAdapter.title.cuentasSeleccionadas"/></caption>
            		<tbody>
                	<tr>
						<th align="left"><bean:message bundle="def" key="def.tributo.label"/></th>						
						<th align="left"><bean:message bundle="pad" key="pad.cuenta.label"/></th>
						<th align="left"><bean:message bundle="pad" key="pad.cambiarDomEnvioAdapter.domEnvioActual"/></th>
						<th align="left"><bean:message bundle="pad" key="pad.cuenta.estado.label"/></th>
					</tr>

					<logic:iterate id="CuentaVO" name="cambiarDomEnvioAdapterVO" property="listCuentasMarcadas">
						<tr>
							<td><bean:write name="CuentaVO" property="recurso.desRecurso"/>&nbsp;</td>
							<td><bean:write name="CuentaVO" property="numeroCuenta" />&nbsp;</td>
							<td><bean:write name="CuentaVO" property="domicilioEnvio.viewConOtraLocalidad" />&nbsp;</td>	
							<td><bean:write name="CuentaVO" property="vigenciaForCamDomView" />&nbsp;</td>
						</tr>
					</logic:iterate>
				</tbody>
			</table>
				
			<fieldset>
				<legend><bean:message bundle="pad" key="pad.cambiarDomEnvioAdapter.title.nuevoDomicilioSeleccionado"/></legend>
				<p><bean:write name="cambiarDomEnvioAdapterVO" property="camDomWeb.domNue.viewConOtraLocalidad" />
            	</p>
            </fieldset>	
            
            <fieldset>
				<legend><bean:message bundle="pad" key="pad.cambiarDomEnvioAdapter.solicitante.title"/></legend>
				<p>
					<label>
						(*)&nbsp;<bean:message bundle="pad" key="pad.persona.nombre.label"/>:
						<html:text name="cambiarDomEnvioAdapterVO" property="camDomWeb.nomSolicitante" size="20" maxlength="30" styleClass="datos"/>						
					</label>
					<label>
						(*)&nbsp;<bean:message bundle="pad" key="pad.persona.apellido.label"/>:
						<html:text name="cambiarDomEnvioAdapterVO" property="camDomWeb.apeSolicitante" size="20" maxlength="30" styleClass="datos"/>
					</label>
				</p>
				<p>
					<label>
						(*)&nbsp;<bean:message bundle="pad" key="pad.tipoNroDocumento.label"/>: </label>
						<html:select name="cambiarDomEnvioAdapterVO" property="camDomWeb.documento.tipoDocumento.id" styleClass="select">
						<html:optionsCollection name="cambiarDomEnvioAdapterVO" property="listTipoDocumento" label="abreviatura" value="id" />
					</html:select>
					<html:text name="cambiarDomEnvioAdapterVO" property="camDomWeb.documento.numeroView" size="9" maxlength="8" styleClass="datos"/>
					
				</p>
				<p>
					<label>
						<bean:message bundle="pad" key="pad.cambiarDomEnvioAdapter.solicitante.mail.label"/>:
						<html:text name="cambiarDomEnvioAdapterVO" property="camDomWeb.mail" size="30" maxlength="30" styleClass="datos"/>
					</label>
				</p>			
			</fieldset>
	</fieldset>	
	
	<p align="center">
		<html:button property="btnAnterior"  styleClass="boton" onclick="submitForm('volver', '');">
			<bean:message bundle="base" key="abm.button.anterior"/>
		</html:button>
		<html:button property="btnSiguiente"  styleClass="boton" onclick="submitForm('modificar', '');">
			<bean:message bundle="base" key="abm.button.aceptar"/>
		</html:button>			
    </p>				
	
	<input type="hidden" name="method" value=""/>
<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>
	
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
