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

	<p>
		<bean:message bundle="pad" key="pad.cambiarDomEnvioAdapter.cuentasRel.legend"/>
	</p>
	
	<!-- CambiarDomEnvio -->
	<fieldset>
		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>				

			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="pad" key="pad.cambiarDomEnvio.listCuenta.title"/></caption>
            		<tbody>
                	<tr>
						<th align="left"><bean:message bundle="def" key="def.tributo.label"/></th>
						<th align="left"><bean:message bundle="pad" key="pad.cuenta.label"/></th>
						<th align="left"><bean:message bundle="pad" key="pad.cambiarDomEnvioAdapter.domEnvioActual"/></th>
						<th align="left"><bean:message bundle="pad" key="pad.cuenta.estado.label"/></th>
						<th align="left"><bean:message bundle="pad" key="pad.cambiarDomEnvioAdapter.seleccionar"/></th>
					</tr>
								
						<logic:iterate id="CuentaVO" name="cambiarDomEnvioAdapterVO" property="listCuenta">
							<tr>
								<td><bean:write name="CuentaVO" property="recurso.desRecurso"/>&nbsp;</td>
								<td><bean:write name="CuentaVO" property="numeroCuenta" />&nbsp;</td>
								<td><bean:write name="CuentaVO" property="domicilioEnvio.viewConOtraLocalidad" />&nbsp;</td>	
								<td><bean:write name="CuentaVO" property="vigenciaForCamDomView" />&nbsp;</td>
								<td>
									<logic:equal name="CuentaVO" property="vigenciaForCamDomView" value="Activo"> 
									<html:multibox name="cambiarDomEnvioAdapterVO" property="listIdCuentasSeleccionadas" >
                                         <bean:write name="CuentaVO" property="id" bundle="base" formatKey="general.format.id"/>
                                    </html:multibox>
                                    </logic:equal>
                                </td>
							</tr>
						</logic:iterate>
					</tbody>
			</table>

		<p align="right">
			<html:button property="btnSiguiente"  styleClass="boton" onclick="submitForm('seleccionarCuentas', '');">
				<bean:message bundle="base" key="abm.button.siguiente"/>
			</html:button>
     	</p>				
	</fieldset>	
	
	<input type="hidden" name="method" value=""/>
<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
