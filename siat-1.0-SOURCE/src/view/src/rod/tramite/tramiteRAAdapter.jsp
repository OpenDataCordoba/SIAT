<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/rod/AdministrarTramiteRA.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="rod" key="rod.tramiteRAEditAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- TramiteRA -->
	<fieldset>
		<legend><bean:message bundle="rod" key="rod.tramiteRA.title"/></legend>
		
		<table class="tabladatos">
			<!-- TextNroTramite -->
			<tr>
			<!-- Numero Comuna -->

				<td><label><bean:message bundle="rod" key="rod.tramiteRA.nroComuna.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.nroComunaView"/></td>			
			
			<!--Fecha-->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.fecha.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.fechaView" /></td>
			
			</tr>
			<!-- Tipo de Tramite -->
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.tipoTramite.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.desTipoTramite" /></td>
			
				<!-- Rubros -->
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.tipoTramite.rubros.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.rubros"/></td>
			</tr>
	
			<!-- A-IDENTIFICACION DEL VEHICULO -->
			<!-- Numero Patente -->
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.ANroPatente.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.ANroPatente"/></td>

			<!-- Digito de control -->

				<td><label><bean:message bundle="rod" key="rod.tramiteRA.ADigVerif.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.ADigVerifView"/></td>			
			</tr>
			
		
			<!-- C -->
			<!-- C-DATOS DEL PROPIETARIO -->
			<!-- Apellido o Razon -->
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.CApellidoORazon.label"/>: </label></td>
				<td><bean:write name="tramiteRAAdapterVO" property="tramiteRA.CApellidoORazon"/></td>
			</tr>
			<!-- Tipo Doc -->
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.CTipoDoc.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.CDesTipoDoc"/></td>
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.CNroDoc.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.CNroDocView"/></td>
				
			<!-- Cod Sexo -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.CCodSexo.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.CDesSexo"/></td>
						
			</tr>
			<!-- cuit -->
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.CNroCuit.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.CNroCuit"/></td>			
			
			<!-- Cod Estado Civil -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.CCodEstCiv.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.CDesEstCiv" /></td>
			
			
			</tr>
			<!--D -->
			<!-- D-DOMICILIO -->
			<!-- Localidad -->
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.DLocalidad.label"/>: </label></td>				
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.DDesLocalidad" /></td>
				
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.DCodLocalidad.label"/>: </label></td>
				<td class="normal"><bean:write name="tramiteRAAdapterVO" property="tramiteRA.DCodLocalidad" bundle="base" formatKey="general.format.id" /></td>
	
			</tr>
			
			<!-- Calle -->
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.DDesCalle.label"/>: </label></td>
				<td><bean:write name="tramiteRAAdapterVO" property="tramiteRA.DDomicilio.view"/></td>
				
			</tr>
				
			
		</table>
	</fieldset>
	<!-- TramiteRA -->
	
	<logic:equal name="tramiteRAAdapterVO" property="act" value="cambiarEstado">
		<fieldset>
			<legend><bean:message bundle="rod" key="rod.estadoTramiteRA.title"/></legend>
			<table class="tabladatos">
				
				<!-- Estado del Nuevo Estado del tramite -->
				<tr>	
					<td><label>(*)&nbsp;<bean:message bundle="rod" key="rod.estadoTramiteRA.label"/>: </label></td>
					<td class="normal">
						<html:select name="tramiteRAAdapterVO" property="tramiteRA.estadoTramiteRA.id" styleClass="select">
							<html:optionsCollection name="tramiteRAAdapterVO" property="listEstTramiteRA" label="desEstTra" value="id" />
						</html:select>
					</td>					
				</tr>

				<tr>
					<!-- Observacion -->
					<td><label><bean:message bundle="rod" key="rod.estadoTramiteRA.observacion.label"/>: </label></td>
					<td class="normal">
						<html:textarea name="tramiteRAAdapterVO" property="tramiteRA.estadoTramiteRA.observacion" styleClass="datos" cols="80" rows="15"/>
					</td>					
				</tr>

			</table>
		</fieldset>	
	</logic:equal>		

	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="100%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="left" width="100%">
	   	    	<logic:equal name="tramiteRAAdapterVO" property="act" value="cambiarEstado">
					<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('cambiarEstado', '');">
						<bean:message bundle="rod" key="rod.tramiteRAAdapter.button.cambiarEstado"/>
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