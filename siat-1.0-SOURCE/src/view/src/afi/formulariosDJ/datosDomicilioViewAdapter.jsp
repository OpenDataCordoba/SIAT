<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/afi/AdministrarDatosDomicilio.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="afi" key="afi.datosDomicilioViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- DatosDomicilio -->
		<fieldset>
			<legend><bean:message bundle="afi" key="afi.datosDomicilio.title"/></legend>
			<table class="tabladatos">				
				<tr>
					<td><label><bean:message bundle="afi" key="afi.datosDomicilio.codPropietario.label"/>: </label></td>
					<td class="normal"><bean:write name="datosDomicilioAdapterVO" property="datosDomicilio.codPropietarioView"/></td>

					<td><label><bean:message bundle="afi" key="afi.datosDomicilio.codInterno.label"/>: </label></td>
					<td class="normal"><bean:write name="datosDomicilioAdapterVO" property="datosDomicilio.codInternoView"/></td>
				</tr>				
				<tr>
					<td><label><bean:message bundle="afi" key="afi.datosDomicilio.calle.label"/>: </label></td>
					<td class="normal"><bean:write name="datosDomicilioAdapterVO" property="datosDomicilio.calle"/></td>

					<td><label><bean:message bundle="afi" key="afi.datosDomicilio.numero.label"/>: </label></td>
					<td class="normal"><bean:write name="datosDomicilioAdapterVO" property="datosDomicilio.numeroView"/></td>
				</tr>	

				<tr>
					<td><label><bean:message bundle="afi" key="afi.datosDomicilio.adicional.label"/>: </label></td>
					<td class="normal"><bean:write name="datosDomicilioAdapterVO" property="datosDomicilio.adicional"/></td>

					<td><label><bean:message bundle="afi" key="afi.datosDomicilio.torre.label"/>: </label></td>
					<td class="normal"><bean:write name="datosDomicilioAdapterVO" property="datosDomicilio.torre"/></td>
				</tr>				
				<tr>
					<td><label><bean:message bundle="afi" key="afi.datosDomicilio.piso.label"/>: </label></td>
					<td class="normal"><bean:write name="datosDomicilioAdapterVO" property="datosDomicilio.piso"/></td>

					<td><label><bean:message bundle="afi" key="afi.datosDomicilio.dptoOficina.label"/>: </label></td>
					<td class="normal"><bean:write name="datosDomicilioAdapterVO" property="datosDomicilio.dptoOficina"/></td>
				</tr>	

				<tr>
					<td><label><bean:message bundle="afi" key="afi.datosDomicilio.sector.label"/>: </label></td>
					<td class="normal"><bean:write name="datosDomicilioAdapterVO" property="datosDomicilio.sector"/></td>

					<td><label><bean:message bundle="afi" key="afi.datosDomicilio.barrio.label"/>: </label></td>
					<td class="normal"><bean:write name="datosDomicilioAdapterVO" property="datosDomicilio.barrio"/></td>
				</tr>				
				<tr>
					<td><label><bean:message bundle="afi" key="afi.datosDomicilio.localidad.label"/>: </label></td>
					<td class="normal"><bean:write name="datosDomicilioAdapterVO" property="datosDomicilio.localidad"/></td>

					<td><label><bean:message bundle="afi" key="afi.datosDomicilio.codPostal.label"/>: </label></td>
					<td class="normal"><bean:write name="datosDomicilioAdapterVO" property="datosDomicilio.codPostal"/></td>
				</tr>					
				
				<tr>
					<td><label><bean:message bundle="afi" key="afi.datosDomicilio.provincia.label"/>: </label></td>
					<td class="normal"><bean:write name="datosDomicilioAdapterVO" property="datosDomicilio.provinciaView"/></td>

				</tr>	
			</table>
		</fieldset>	
		<!-- DatosDomicilio -->

		<!-- Socios -->
		<logic:notEmpty  name="datosDomicilioAdapterVO" property="datosDomicilio.listSocio">	
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="afi" key="afi.forDecJur.listSocio.label"/></caption>
	    	<tbody>
			    	<tr>						
						<th align="left"><bean:message bundle="afi" key="afi.socio.apellidoYNombre.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.socio.documento.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.socio.cuit.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.socio.enCaracterDe.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.socio.soloFirmante.label"/></th>
					</tr>
					<logic:iterate id="SocioVO" name="datosDomicilioAdapterVO" property="datosDomicilio.listSocio">			
						<tr>
							<td><bean:write name="SocioVO" property="apellidoYNombreView"/>&nbsp;</td>
							<td><bean:write name="SocioVO" property="documentoView"/>&nbsp;</td>
							<td><bean:write name="SocioVO" property="cuit"/>&nbsp;</td>
							<td><bean:write name="SocioVO" property="enCaracterDeView"/>&nbsp;</td>
							<td><bean:write name="SocioVO" property="soloFirmanteView"/>&nbsp;</td>
						</tr>
					</logic:iterate>
			</tbody>
		</table>
		</logic:notEmpty>
		
		<logic:notEmpty  name="datosDomicilioAdapterVO" property="datosDomicilio.listLocal">	
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="afi" key="afi.forDecJur.listLocal.label"/></caption>
	    	<tbody>
			    	<tr>
						<th align="left"><bean:message bundle="afi" key="afi.local.numeroCuenta.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.local.nombreFantasia.label"/></th>
						<th align="left"><bean:message bundle="afi" key="afi.local.fecVigAct.label"/></th>
					</tr>
					<logic:iterate id="LocalVO" name="datosDomicilioAdapterVO" property="datosDomicilio.listLocal">			
						<tr>					
							<td><bean:write name="LocalVO" property="numeroCuenta"/>&nbsp;</td>
							<td><bean:write name="LocalVO" property="nombreFantasia"/>&nbsp;</td>
							<td><bean:write name="LocalVO" property="fecVigActView"/>&nbsp;</td>
						</tr>
					</logic:iterate>
			</tbody>
		</table>
		</logic:notEmpty>		

		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>	   	    	
	   	    </tr>
	   	 </table>
	    <input type="hidden" name="name"  value="<bean:write name='datosDomicilioAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->