<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/AdministrarReclamo.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.reclamoViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>

	<!-- Reclamo -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.reclamo.title"/></legend>
			<table class="tabladatos">
		
			<tr>
				<td><label><bean:message bundle="bal" key="bal.reclamo.nroReclamo.label"/>: </label></td>
				<td class="normal"><label><bean:write name="reclamoAdapterVO" property="reclamo.id" bundle="base" formatKey="general.format.id"/></label></td>
			</tr>

			<!-- Datos Deuda -->
			<logic:equal name="reclamoAdapterVO" property="reclamo.tipoBoleta" value="1">
			<tr>
				<td><label><bean:message bundle="bal" key="bal.reclamo.tipoBoleta.label"/>: </label></td>
				<td class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.desTipoBoleta"/></td>

				<td><label><bean:message bundle="bal" key="bal.reclamo.periodo.label"/> / <bean:message bundle="bal" key="bal.reclamo.anio.label"/>: </label></td>
				<td class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.periodoView"/> / <bean:write name="reclamoAdapterVO" property="reclamo.anioView"/></td>
			</tr>
			</logic:equal>

			<!-- Datos Cuota -->
			<logic:equal name="reclamoAdapterVO" property="reclamo.tipoBoleta" value="3">
			<tr>
				<td><label><bean:message bundle="bal" key="bal.reclamo.tipoBoleta.label"/>: </label></td>
				<td class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.desTipoBoleta"/></td>
				<td><label><bean:message bundle="bal" key="bal.reclamo.nroConvenio.label"/> / <bean:message bundle="bal" key="bal.reclamo.nroCuota.label"/>: </label></td>
				<td class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.nroConvenioView"/> / <bean:write name="reclamoAdapterVO" property="reclamo.nroCuotaView"/></td>				
			</tr>
			</logic:equal>

			<!-- Datos extra de deuda o cuota: via y procurador-->
			<tr>
				<td><label><bean:message bundle="bal" key="bal.reclamo.desViaDeuda.label"/>: </label></td>
				<td class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.viaDeuda.desViaDeuda"/></td>
				<logic:equal name="reclamoAdapterVO" property="reclamo.viaDeuda.id" value="2"> <!-- solo si es judicial mostramos procurador -->
					<td><label><bean:message bundle="bal" key="bal.reclamo.desProcurador.label"/>: </label></td>
					<td class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.procurador.descripcion"/></td>
				</logic:equal>
			</tr>

			<!-- Datos de la cuenta -->
			<tr>
				<td><label><bean:message bundle="bal" key="bal.reclamo.nroCuenta.label"/>: </label></td>
				<td class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.recurso.codRecurso"/> - <bean:write name="reclamoAdapterVO" property="reclamo.nroCuentaView"/></td>				
				<td><label><bean:message bundle="bal" key="bal.canal.label"/>: </label></td>
				<td class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.canal.desCanal"/>&nbsp;&nbsp;<bean:write name="reclamoAdapterVO" property="reclamo.infoUsuarioAlta"/></td>
			</tr>

			<!-- Datos ingresados por el usuario -->
			<tr><td>&nbsp;</td></tr>

			<tr>
				<td><label><bean:message bundle="bal" key="bal.reclamo.desBanco.label"/>: </label></td>
				<td class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.banco.desBanco"/></td>
			</tr>

			<tr>
				<td><label><bean:message bundle="bal" key="bal.reclamo.fechaPago.label"/>: </label></td>
				<td class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.fechaPagoView"/></td>
				<td><label><bean:message bundle="bal" key="bal.reclamo.importePagado.label"/>: </label></td>
				<td class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.importePagadoView"/></td>
			</tr>

			<tr>
				<td><label><bean:message bundle="bal" key="bal.reclamo.nombre.label"/>: </label></td>
				<td class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.nombre"/></td>
				<td><label><bean:message bundle="bal" key="bal.reclamo.apellido.label"/>: </label></td>
				<td class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.apellido"/></td>
			</tr>

			<tr>
				<td><label><bean:message bundle="bal" key="bal.reclamo.nroDoc.label"/>: </label></td>
				<td class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.tipoDocView"/> - <bean:write name="reclamoAdapterVO" property="reclamo.nroDocView"/></td>
				<td><label><bean:message bundle="bal" key="bal.reclamo.correoElectronico.label"/>: </label></td>
				<td class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.correoElectronico"/></td>
			</tr>
			
			<tr>
				<td><label><bean:message bundle="bal" key="bal.reclamo.observacion.label"/>: </label></td>
				<td class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.observacion"/></td>
			</tr>
			
			<tr><td>&nbsp;</td></tr>
			<!-- Estado reclamo -->
			<tr>	
				<td><label><bean:message bundle="bal" key="bal.reclamo.estadoReclamo.label"/>: </label></td>
				<td class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.estadoReclamo.desEstadoReclamo"/></td>

			</tr>
			
			<!-- Respuesta -->
			<tr><td>&nbsp;</td></tr>
			<tr>
				<td><label>A:</label></td>
				<td colspan="3" class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.correoElectronico"/></td>
			</tr>
			
			<tr>
				<td><label>Asunto:</label></td>
				<td colspan="3" class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.asuntoCorreo"/></td>
			</tr>

			<tr>
				<td><label>&nbsp;</label></td>
				<td colspan="3" class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.encabezadoCorreoView" filter="false"/></td>
			</tr>
			
			<tr>
				<td><label><bean:message bundle="bal" key="bal.reclamo.respuesta.label"/>:</label></td>
				<td colspan="3" class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.respuesta"></bean:write></td>
			</tr>
<!--
<bean:write name="reclamoAdapterVO" property="reclamo.esMigrada"/>-
<bean:write name="reclamoAdapterVO" property="reclamo.esDeuda"/>-
<bean:write name="reclamoAdapterVO" property="reclamo.esCuota"/>-
<bean:write name="reclamoAdapterVO" property="reclamo.tieneRecibo"/>
-->


			</table>
		</fieldset>
		<!-- Reclamo -->

		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="left">
	   	    	  <logic:equal name="reclamoAdapterVO" property="act" value="ver">
		   	    	    <html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						    <bean:message bundle="base" key="abm.button.imprimir"/>
					    </html:button>
					</logic:equal>
					<logic:equal name="reclamoAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="reclamoAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="reclamoAdapterVO" property="act" value="desactivar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
							<bean:message bundle="base" key="abm.button.desactivar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
	   	    </tr>
	   	 </table>
	   	<input type="hidden" name="name"  value="<bean:write name='reclamoAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
