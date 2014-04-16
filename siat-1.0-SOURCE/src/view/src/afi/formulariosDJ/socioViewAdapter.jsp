<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/afi/AdministrarSocio.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="afi" key="afi.socioViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- Socio -->
		<fieldset>
			<legend><bean:message bundle="afi" key="afi.socio.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="afi" key="afi.socio.soloFirmante.label"/>: </label></td>
					<td class="normal"><bean:write name="socioAdapterVO" property="socio.soloFirmanteView"/></td>
					
					<td><label><bean:message bundle="afi" key="afi.socio.idPersona.label"/>: </label></td>
					<td class="normal"><bean:write name="socioAdapterVO" property="socio.idPersonaView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="afi" key="afi.socio.apellido.label"/>: </label></td>
					<td class="normal"><bean:write name="socioAdapterVO" property="socio.apellido"/></td>
					
					<td><label><bean:message bundle="afi" key="afi.socio.apellidoMaterno.label"/>: </label></td>
					<td class="normal"><bean:write name="socioAdapterVO" property="socio.apellidoMaterno"/></td>
				</tr>
				
				<tr>
					<td><label><bean:message bundle="afi" key="afi.socio.nombre.label"/>: </label></td>
					<td class="normal"><bean:write name="socioAdapterVO" property="socio.nombre"/></td>
					
					<td><label><bean:message bundle="afi" key="afi.socio.enCaracterDe.label"/>: </label></td>
					<td class="normal"><bean:write name="socioAdapterVO" property="socio.enCaracterDeView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="afi" key="afi.socio.tipoDocumento.label"/>: </label></td>
					<td class="normal"><bean:write name="socioAdapterVO" property="socio.tipoDocumentoView"/></td>
					
					<td><label><bean:message bundle="afi" key="afi.socio.nroDocumento.label"/>: </label></td>
					<td class="normal"><bean:write name="socioAdapterVO" property="socio.nroDocumento"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="afi" key="afi.socio.cuit.label"/>: </label></td>
					<td class="normal"><bean:write name="socioAdapterVO" property="socio.cuit"/></td>
				</tr>		

			</table>
		</fieldset>	
		
		<!-- DatosDomicilio -->
		<logic:equal name="socioAdapterVO" property="paramDatosDomicilio" value="true">
		<fieldset>
			<legend><bean:message bundle="afi" key="afi.datosDomicilio.title"/></legend>
			<table class="tabladatos">				
				<tr>
					<td><label><bean:message bundle="afi" key="afi.datosDomicilio.codPropietario.label"/>: </label></td>
					<td class="normal"><bean:write name="socioAdapterVO" property="socio.datosDomicilio.codPropietarioView"/></td>

					<td><label><bean:message bundle="afi" key="afi.datosDomicilio.codInterno.label"/>: </label></td>
					<td class="normal"><bean:write name="socioAdapterVO" property="socio.datosDomicilio.codInternoView"/></td>
				</tr>				
				<tr>
					<td><label><bean:message bundle="afi" key="afi.datosDomicilio.calle.label"/>: </label></td>
					<td class="normal"><bean:write name="socioAdapterVO" property="socio.datosDomicilio.calle"/></td>

					<td><label><bean:message bundle="afi" key="afi.datosDomicilio.numero.label"/>: </label></td>
					<td class="normal"><bean:write name="socioAdapterVO" property="socio.datosDomicilio.numeroView"/></td>
				</tr>	

				<tr>
					<td><label><bean:message bundle="afi" key="afi.datosDomicilio.adicional.label"/>: </label></td>
					<td class="normal"><bean:write name="socioAdapterVO" property="socio.datosDomicilio.adicional"/></td>

					<td><label><bean:message bundle="afi" key="afi.datosDomicilio.torre.label"/>: </label></td>
					<td class="normal"><bean:write name="socioAdapterVO" property="socio.datosDomicilio.torre"/></td>
				</tr>				
				<tr>
					<td><label><bean:message bundle="afi" key="afi.datosDomicilio.piso.label"/>: </label></td>
					<td class="normal"><bean:write name="socioAdapterVO" property="socio.datosDomicilio.piso"/></td>

					<td><label><bean:message bundle="afi" key="afi.datosDomicilio.dptoOficina.label"/>: </label></td>
					<td class="normal"><bean:write name="socioAdapterVO" property="socio.datosDomicilio.dptoOficina"/></td>
				</tr>	

				<tr>
					<td><label><bean:message bundle="afi" key="afi.datosDomicilio.sector.label"/>: </label></td>
					<td class="normal"><bean:write name="socioAdapterVO" property="socio.datosDomicilio.sector"/></td>

					<td><label><bean:message bundle="afi" key="afi.datosDomicilio.barrio.label"/>: </label></td>
					<td class="normal"><bean:write name="socioAdapterVO" property="socio.datosDomicilio.barrio"/></td>
				</tr>				
				<tr>
					<td><label><bean:message bundle="afi" key="afi.datosDomicilio.localidad.label"/>: </label></td>
					<td class="normal"><bean:write name="socioAdapterVO" property="socio.datosDomicilio.localidad"/></td>

					<td><label><bean:message bundle="afi" key="afi.datosDomicilio.codPostal.label"/>: </label></td>
					<td class="normal"><bean:write name="socioAdapterVO" property="socio.datosDomicilio.codPostal"/></td>
				</tr>					
				
				<tr>
					<td><label><bean:message bundle="afi" key="afi.datosDomicilio.provincia.label"/>: </label></td>
					<td class="normal"><bean:write name="socioAdapterVO" property="socio.datosDomicilio.provinciaView"/></td>
				</tr>	
			</table>
		</fieldset>	
		</logic:equal>
		<!-- DatosDomicilio -->
		<!-- Socio -->

		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="left">
	   	    	   <logic:equal name="socioAdapterVO" property="act" value="ver">
		   	    	    <html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						    <bean:message bundle="base" key="abm.button.imprimir"/>
					    </html:button>
					</logic:equal>					
	   	    	</td>
	   	    </tr>
	   	 </table>
	    <input type="hidden" name="name"  value="<bean:write name='socioAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->