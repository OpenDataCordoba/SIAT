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

	<p> En esta pantalla, usted debe ingresar los datos correspondientes al 
        domicilio al que desea se env&iacute;en su/sus boleta/s.</p>
	<p>En el caso de ser Rosario la localidad donde reside, complete el campo 
        "Calle" colocando las 3 primeras letras de la misma, "Altura" 
        y dem&aacute;s campos; luego presione el bot&oacute;n  "Siguiente". 
         El sistema listar&aacute; los nombres de  calles que contengan dichas letras, 
         para que pueda seleccionar la deseada. 
         Por mayor informaci&oacute;n sobre calles y alturas, puede consultar el 
			<a href="http://visualizador.rosario.gov.ar/" target="_blank">Mapa de la ciudad</a>. 
		</p>

	<p>Si no es Rosario la localidad de pertenencia de su domicilio de env&iacute;o, 
        seleccione la opci&oacute;n "Otra" (colocando las 4 primeras 
        letras de la localidad a elegir y haciendo clic en "Buscar" 
        se desplegar&aacute; un listado del cual deber&aacute; seleccionar la 
        ciudad deseada).
    </p>

	<!-- CambiarDomEnvio -->
	<fieldset>
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
			<!-- Mensajes y/o Advertencias -->
			<%@ include file="/base/warning.jsp" %>
			<!-- Mensajes y/o Advertencias -->
			<fieldset>
				<legend><bean:message bundle="pad" key="pad.cambiarDomEnvioAdapter.title.nuevoDomicilio"/></legend>
				<p>
					<label>(*)&nbsp;<bean:message bundle="pad" key="pad.domicilio.localidad.label"/>: </label>

					<html:radio name="cambiarDomEnvioAdapterVO" property="rosarioSelec" value="true" 
						onclick="submitForm('seleccionarRosario', '');"/>
    		        	<bean:message bundle="pad" key="pad.cambiarDomEnvioAdapter.rosario"/>
						&nbsp;&nbsp;&nbsp;&nbsp;

					<html:radio name="cambiarDomEnvioAdapterVO" property="rosarioSelec" value="false" 
						onclick="submitForm('seleccionarOtraLocalidad', '');"/>

					<bean:write name="cambiarDomEnvioAdapterVO" property="otraLocalidadView" />
					<label>
						<bean:message bundle="pad" key="pad.cambiarDomEnvioAdapter.otra"/>:
						<html:text name="cambiarDomEnvioAdapterVO" property="localidadAuxiliar" size="20" maxlength="100" styleClass="datos"/>
					</label>
					<html:button property="btnSiguiente"  styleClass="boton" onclick="submitForm('buscarOtraLocalidad', '');">
						<bean:message bundle="base" key="abm.button.buscar"/>
					</html:button>			
            	</p>

				<!--  codPostal para localidades fuera de Rosario -->
				<logic:equal name="cambiarDomEnvioAdapterVO" property="camDomWeb.domNue.localidad.esRosario" value="false">
					<label>
						<bean:message bundle="pad" key="pad.localidad.codPostal.label"/>:
						<html:text name="cambiarDomEnvioAdapterVO" property="camDomWeb.domNue.codPostalFueraRosario" size="12" maxlength="10" styleClass="datos"/>						
					</label>	
				</logic:equal>
				
            	<!-- tabla de otras localidades -->
            	<logic:equal name="cambiarDomEnvioAdapterVO" property="viewResultLocalidades" value="true">
            	<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="pad" key="pad.cambiarDomEnvioAdapter.title.localidad.resultadoBusqueda"/></caption>
	            	<tbody>
        	        	<tr>
							<th align="left"><bean:message bundle="pad" key="pad.cambiarDomEnvioAdapter.seleccionar"/></th>
							<th align="left"><bean:message bundle="pad" key="pad.localidad.nombreLocalidad"/></th>
						</tr>
							
						<logic:iterate id="LocalidadVO" name="cambiarDomEnvioAdapterVO" property="listLocalidad">
							<tr>
								<td>
									<input name="indiceLocalidad" value="0" onclick="submitForm('seleccionarLocalidad',
										'<bean:write name="LocalidadVO" property="id" bundle="base" formatKey="general.format.id"/>')"
										type="radio"/>
								</td>
								<td><bean:write name="LocalidadVO" property="localidadPciaView" />&nbsp;</td>
							</tr>
						</logic:iterate>
					 </tbody>
				</table>
				</logic:equal>
				
				<p>
					<label>
						(*)&nbsp;<bean:message bundle="pad" key="pad.calle.label"/>: 
						<html:text name="cambiarDomEnvioAdapterVO" property="camDomWeb.domNue.calle.nombreCalle" size="20" maxlength="25" styleClass="datos"/>
					</label>	
					<label>
						(*)&nbsp;<bean:message bundle="pad" key="pad.domicilio.altura.label"/>: 
						<html:text name="cambiarDomEnvioAdapterVO" property="camDomWeb.domNue.numeroView" size="5" maxlength="5" styleClass="datos"/> 
					</label>
					<label>
						<bean:message bundle="pad" key="pad.domicilio.letra.label"/>: 
						<html:text name="cambiarDomEnvioAdapterVO" property="camDomWeb.domNue.letraCalle" size="1" maxlength="1" styleClass="datos"/> 
					</label>
					<label>
						<bean:message bundle="pad" key="pad.domicilio.bis.label"/>: 
						<html:checkbox name="cambiarDomEnvioAdapterVO" property="camDomWeb.domNue.bisView" styleClass="datos"/> 
					</label>
				</p>
				<p>
					<label>
						<bean:message bundle="pad" key="pad.domicilio.piso.label"/>: 
						<html:text name="cambiarDomEnvioAdapterVO" property="camDomWeb.domNue.piso" size="2" maxlength="2" styleClass="datos"/> 
					</label>
					<label>
						<bean:message bundle="pad" key="pad.domicilio.depto.label"/>: 
						<html:text name="cambiarDomEnvioAdapterVO" property="camDomWeb.domNue.depto" size="4" maxlength="4" styleClass="datos"/> 
					</label>
					<label>
						<bean:message bundle="pad" key="pad.domicilio.monoblock.abrev"/>: 
						<html:text name="cambiarDomEnvioAdapterVO" property="camDomWeb.domNue.monoblock" size="4" maxlength="4" styleClass="datos"/> 
					</label>
				</p>
				
            	<!-- tabla de calles -->
            	<logic:equal name="cambiarDomEnvioAdapterVO" property="viewResultCalles" value="true">
            	<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
					<caption><bean:message bundle="pad" key="pad.cambiarDomEnvioAdapter.title.calle.resultadoBusqueda"/></caption>
	            	<tbody>
        	        	<tr>
							<th align="left"><bean:message bundle="pad" key="pad.cambiarDomEnvioAdapter.seleccionar"/></th>
							<th align="left"><bean:message bundle="pad" key="pad.calle.nombreCalle.label"/></th>
						</tr>
							
						<logic:iterate id="CalleVO" name="cambiarDomEnvioAdapterVO" property="listCalle">
							<tr>
								<td>
									<input name="indiceCalle" value="0" onclick="submitForm('seleccionarCalle',
											'<bean:write name="CalleVO" property="id" bundle="base" formatKey="general.format.id"/>')" type="radio" 
									/>
								</td>
								<td><bean:write name="CalleVO" property="nombreCalle" /></td>
							</tr>
						</logic:iterate>
					 </tbody>
				</table>
				</logic:equal>
				
	            <p> <bean:message bundle="pad" key="pad.cambiarDomEnvioAdapter.camposRequeridos"/></p>
              </fieldset>				

		<p align="center">
			<html:button property="btnAnterior"  styleClass="boton" onclick="submitForm('volver', '');">
				<bean:message bundle="base" key="abm.button.anterior"/>
			</html:button>
			<html:button property="btnSiguiente"  styleClass="boton" onclick="submitForm('cargarNuevoDomicilio', '');">
				<bean:message bundle="base" key="abm.button.siguiente"/>
			</html:button>			
     	</p>				
	</fieldset>	
	
	<input type="hidden" name="method" value=""/>
<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>
	
	<input type="hidden" name="codPostal"    value=""/>
	<input type="hidden" name="codSubPostal" value=""/>
	

</html:form>
<!-- Fin Tabla que contiene todos los formularios -->