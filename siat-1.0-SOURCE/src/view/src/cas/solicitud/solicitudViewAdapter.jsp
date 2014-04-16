<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/cas/AdministrarSolicitud.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="cas" key="cas.solicitudViewAdapter.title"/></h1>	
		
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
				
				<!-- Nro Solicitud -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.solicitud.numero.label"/>: </label></td>
					<td class="normal">
						<bean:write name="solicitudAdapterVO" property="solicitud.idView"/>
					</td>
				</tr>
				
				<!-- Usuario alta y fecha alta -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.solicitud.usuarioAlta.label"/>: </label></td>
					<td class="normal">
						<bean:write name="solicitudAdapterVO" property="solicitud.usuarioAlta"/>
					</td>
				</tr>
				
				<!-- area Origen -->
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
				
				<!-- Inclucion de CasoView -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
					<td colspan="3">
						<bean:define id="IncludedVO" name="solicitudAdapterVO" property="solicitud"/>
						<%@ include file="/cas/caso/includeCasoView.jsp" %>				
					</td>
				</tr>
				<!-- Fin Inclucion de CasoView -->
	
				<!-- Tipo solicitud -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.tipoSolicitud.label"/>: </label></td>
					<td class="normal">
						<bean:write name="solicitudAdapterVO" property="solicitud.tipoSolicitud.descripcion" />
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
					<td><label><bean:message bundle="cas" key="cas.solicitud.asuntoSolicitud.label"/>: </label></td>
					<td class="normal">
						<bean:write name="solicitudAdapterVO" property="solicitud.asuntoSolicitud"/>
					</td>
				</tr>
				<!-- Descripcion -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.solicitud.descripcion.label"/>: </label></td>
					<td class="normal" colspan="3">
						<bean:write name="solicitudAdapterVO" property="solicitud.descripcion" />
					</td>
				</tr>
				<!-- Numero Cuenta -->
				<tr>
					<td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.ref"/>: </label></td>
					<td class="normal">
						<bean:write name="solicitudAdapterVO" property="solicitud.cuenta.numeroCuenta" />
					</td>
				</tr>
				
				<!-- Estado -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.estSolicitud.label"/>: </label></td>
					<td class="normal">
						<bean:write name="solicitudAdapterVO" property="solicitud.estSolicitud.descripcion" />
					</td>
				</tr>
				
				<logic:notEqual name="solicitudAdapterVO" property="act" value="cambiarEstado">
					<logic:equal name="solicitudAdapterVO" property="solicitud.tipoSolicitud.codigo" value="8">
						<!-- muestra los datos como solo lectura de modificacion de datos de persona -->						
						<tr>
							<td class="normal" colspan="3">
								<fieldSet>								
									<!-- datos existentes en el padron -->
									<label><bean:message bundle="cas" key="cas.solicitudAdapter.modifEstado.datosPadron"/>:</label>
									<bean:write name="solicitudAdapterVO" property="solicitudCUIT.datosRegPadron"/>								
									<br><br>
																				
									<!-- modificacion de denominacion -->
									<logic:equal name="solicitudAdapterVO" property="solicitudCUIT.tipoModificacion" value="2">
										<legend><bean:message bundle="cas" key="cas.solicitudAdapter.modifEstado.Denominacion.title"/></legend>
									
										<!-- Persona Fisica -->
										<logic:equal name="solicitudAdapterVO" property="solicitudCUIT.tipoPersona" value="1">
											<!-- Datos reportados -->
											<label><bean:message bundle="cas" key="cas.solicitudAdapter.modifEstado.apeNomReportados"/>:</label>
											<bean:write name="solicitudAdapterVO" property="solicitudCUIT.apellidoReportado"/>,
											<bean:write name="solicitudAdapterVO" property="solicitudCUIT.nombreReportado"/>
											<br><br>
											
											<!-- Datos aprobados -->
											<label><bean:message bundle="cas" key="cas.solicitudAdapter.modifEstado.apellidoAprobado"/>:</label>
											<bean:write name="solicitudAdapterVO" property="solicitudCUIT.apellidoAprobado"/>
											<br>
											<label><bean:message bundle="cas" key="cas.solicitudAdapter.modifEstado.nombresAprobado"/>:</label>
											<bean:write name="solicitudAdapterVO" property="solicitudCUIT.nombreAprobado" />																														
										</logic:equal>
										
										<!-- Persona Juridica -->
										<logic:equal name="solicitudAdapterVO" property="solicitudCUIT.tipoPersona" value="2">
											
											<!-- Datos reportados -->
											<label><bean:message bundle="cas" key="cas.solicitudAdapter.modifEstado.RazSocReportado"/>:</label>
											<bean:write name="solicitudAdapterVO" property="solicitudCUIT.razonSocReportado"/>
											<br><br>
											
											<!-- Datos aprobados -->
											<label><bean:message bundle="cas" key="cas.solicitudAdapter.modifEstado.razSocAprobado"/>:</label>
											<bean:write name="solicitudAdapterVO" property="solicitudCUIT.razonSocAprobado"/>
										</logic:equal>								
									</logic:equal>
									
									<!-- Modificacion de CUIT -->
									<logic:equal name="solicitudAdapterVO" property="solicitudCUIT.tipoModificacion" value="1">
										<legend><bean:message bundle="cas" key="cas.solicitudAdapter.modifEstado.Cuit.title"/></legend>
										<!-- Datos reportados -->
											<label><bean:message bundle="cas" key="cas.solicitudAdapter.modifEstado.docReportado"/>:</label>
											<bean:write name="solicitudAdapterVO" property="solicitudCUIT.tipoDocReportado"/>
											<bean:write name="solicitudAdapterVO" property="solicitudCUIT.docReportadoView"/>
											<br><br>
											
											<!-- Datos aprobados -->
											<label><bean:message bundle="cas" key="cas.solicitudAdapter.modifEstado.cuitAprobado"/>:</label>
											<bean:write name="solicitudAdapterVO" property="solicitudCUIT.cuit01AprobadoView" /> - 
											<bean:write name="solicitudAdapterVO" property="solicitudCUIT.cuit02AprobadoView" /> - 
											<bean:write name="solicitudAdapterVO" property="solicitudCUIT.cuit03AprobadoView" />																			
									</logic:equal>							
								</fieldSet>
							</td>
						</tr>														
						</logic:equal>
						
						<tr>
							<td><label><bean:message bundle="cas" key="cas.solicitud.logSolicitud.label"/>: </label></td>
							<td class="normal" colspan="3">
								<bean:write name="solicitudAdapterVO" property="solicitud.logsolicitud" filter="false"/>
							</td>
						</tr>				
						
						<tr>
							<td><label><bean:message bundle="cas" key="cas.solicitud.obsEstSolicitud.label"/>: </label></td>
							<td class="normal" colspan="3">
								<bean:write name="solicitudAdapterVO" property="solicitud.obsestsolicitud"/>
							</td>
						</tr>					
					</logic:notEqual>
										
					<!-- Cambiar Estado -->
					<logic:equal name="solicitudAdapterVO" property="act" value="cambiarEstado">					
						
						<!-- modificacion de datos de persona -->
						<logic:equal name="solicitudAdapterVO" property="solicitud.tipoSolicitud.id" value="6">
							<tr>
								<td class="normal" colspan="3">
									<fieldSet>								
										<!-- datos existentes en el padron -->
										<label><bean:message bundle="cas" key="cas.solicitudAdapter.modifEstado.datosPadron"/>:</label>
										<bean:write name="solicitudAdapterVO" property="solicitudCUIT.datosRegPadron"/>								
										<br><br>
																					
										<!-- modificacion de denominacion -->
										<logic:equal name="solicitudAdapterVO" property="solicitudCUIT.tipoModificacion" value="2">
											<legend><bean:message bundle="cas" key="cas.solicitudAdapter.modifEstado.Denominacion.title"/></legend>
										
											<!-- Persona Fisica -->
											<logic:equal name="solicitudAdapterVO" property="solicitudCUIT.tipoPersona" value="1">
												<!-- Datos reportados -->
												<label><bean:message bundle="cas" key="cas.solicitudAdapter.modifEstado.apeNomReportados"/>:</label>
												<bean:write name="solicitudAdapterVO" property="solicitudCUIT.apellidoReportado"/>,
												<bean:write name="solicitudAdapterVO" property="solicitudCUIT.nombreReportado"/>
												<br><br>
												
												<!-- Datos aprobados -->
												<label><bean:message bundle="cas" key="cas.solicitudAdapter.modifEstado.apellidoAprobado"/>:</label>
												<html:text name="solicitudAdapterVO" property="solicitudCUIT.apellidoAprobado" maxlength="20"/>
												<br>
												<label><bean:message bundle="cas" key="cas.solicitudAdapter.modifEstado.nombresAprobado"/>:</label>
												<html:text name="solicitudAdapterVO" property="solicitudCUIT.nombreAprobado" maxlength="20"/>																														
											</logic:equal>
											
											<!-- Persona Juridica -->
											<logic:equal name="solicitudAdapterVO" property="solicitudCUIT.tipoPersona" value="2">
												
												<!-- Datos reportados -->
												<label><bean:message bundle="cas" key="cas.solicitudAdapter.modifEstado.RazSocReportado"/>:</label>
												<bean:write name="solicitudAdapterVO" property="solicitudCUIT.razonSocReportado"/>
												<br><br>
												
												<!-- Datos aprobados -->
												<label><bean:message bundle="cas" key="cas.solicitudAdapter.modifEstado.razSocAprobado"/>:</label>
												<html:text name="solicitudAdapterVO" property="solicitudCUIT.razonSocAprobado" maxlength="20"/>
											</logic:equal>								
										</logic:equal>
										
										<!-- Modificacion de CUIT -->
										<logic:equal name="solicitudAdapterVO" property="solicitudCUIT.tipoModificacion" value="1">
											<legend><bean:message bundle="cas" key="cas.solicitudAdapter.modifEstado.Cuit.title"/></legend>
											<!-- Datos reportados -->
												<label><bean:message bundle="cas" key="cas.solicitudAdapter.modifEstado.docReportado"/>:</label>
												<bean:write name="solicitudAdapterVO" property="solicitudCUIT.tipoDocReportado"/>
												<bean:write name="solicitudAdapterVO" property="solicitudCUIT.docReportadoView"/>
												<br><br>
												
												<!-- Datos aprobados -->
												<label><bean:message bundle="cas" key="cas.solicitudAdapter.modifEstado.cuitAprobado"/>:</label>
												<html:text name="solicitudAdapterVO" property="solicitudCUIT.cuit01AprobadoView" size="2" maxlength="2"/> - 
												<html:text name="solicitudAdapterVO" property="solicitudCUIT.cuit02AprobadoView" size="8" maxlength="8"/> - 
												<html:text name="solicitudAdapterVO" property="solicitudCUIT.cuit03AprobadoView" size="1" maxlength="1"/>																			
										</logic:equal>							
									</fieldSet>
								</td>
							</tr>														
						</logic:equal>
						
						<tr>
							<td><label><bean:message bundle="cas" key="cas.solicitud.logSolicitud.label"/>: </label></td>
							<td class="normal" colspan="3">
								<bean:write name="solicitudAdapterVO" property="solicitud.logsolicitud" filter="false"/>
							</td>
						</tr>				
						<tr>
							<td><label>(*)&nbsp;<bean:message bundle="cas" key="cas.solicitud.cambiarEstado.label"/>: </label></td>
							<td class="normal">
								<html:select name="solicitudAdapterVO" property="solicitud.estSolicitud.id" styleClass="select">
									<html:optionsCollection name="solicitudAdapterVO" property="listEstSolicitud"  label="descripcion" value="id"  />
								</html:select>
							</td>
						</tr>
						
						<tr>
							<td><label>(*)&nbsp;<bean:message bundle="cas" key="cas.solicitud.obsEstSolicitud.label"/>: </label></td>
							<td class="normal" colspan="3">
								<html:textarea name="solicitudAdapterVO" property="solicitud.obsestsolicitud" cols="20" rows="5" />
							</td>
						</tr>
						
					</logic:equal>		
					<!-- FIN Cambiar Estado -->										
					
			</table>
		</fieldset>	
		<!-- Solicitud -->


		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="solicitudAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="solicitudAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="solicitudAdapterVO" property="act" value="desactivar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
							<bean:message bundle="base" key="abm.button.desactivar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="solicitudAdapterVO" property="act" value="cambiarEstado">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('cambiarEstado', '');">
							<bean:message bundle="cas" key="cas.solicitudViewAdapter.button.cambiarEstado"/>
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