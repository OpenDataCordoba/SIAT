<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/rec/AdministrarProcesoAnulacionObra.do">
	
		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="rec" key="rec.procesoAnulacionObraAdapter.title"/></h1>

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- Datos de la Anulacion de Obra -->		
		<fieldset>
			<legend><bean:message bundle="rec" key="rec.anulacionObra.title"/></legend>			
			<table class="tabladatos" width="100%">	
				<!-- Fecha de Anulacion -->
				<tr>
					<td><label><bean:message bundle="rec" key="rec.anulacionObra.fechaAnulacion.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoAnulacionObraAdapterVO" property="anulacionObra.fechaAnulacionView"/></td>
				</tr>
				<!--  Obra -->
				<tr>
					<td><label><bean:message bundle="rec" key="rec.anulacionObra.obra.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoAnulacionObraAdapterVO" property="anulacionObra.obra.desObraConNumeroObra"/></td>
				</tr>
				<!--  Planilla -->
				<tr>
					<td><label><bean:message bundle="rec" key="rec.anulacionObra.planillaCuadra.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoAnulacionObraAdapterVO" property="anulacionObra.planillaCuadra.descripcion"/></td>
				</tr>
				<!--  Cuenta CdM -->
				<tr>
					<td><label><bean:message bundle="rec" key="rec.anulacionObra.plaCuaDet.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoAnulacionObraAdapterVO" property="anulacionObra.plaCuaDet.cuentaCdM.numeroCuenta"/></td>
				</tr>
				<!-- Fecha de Vencimiento -->
				<tr>
					<td><label><bean:message bundle="rec" key="rec.anulacionObra.fechaVencimiento.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoAnulacionObraAdapterVO" property="anulacionObra.fechaVencimientoView"/></td>
				</tr>
				<!-- Observacion -->
				<tr>
					<td><label><bean:message bundle="rec" key="rec.anulacionObra.observacion.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoAnulacionObraAdapterVO" property="anulacionObra.observacion"/></td>
				</tr>
				<!-- Inclucion de CasoView -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
					<td colspan="3">
						<bean:define id="IncludedVO" name="procesoAnulacionObraAdapterVO" property="anulacionObra"/>
						<%@ include file="/cas/caso/includeCasoView.jsp" %>				
					</td>
				</tr>
				<!-- Fin Inclucion de CasoView -->
				<!-- Estado del Proceso -->
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoAnulacionObraAdapterVO" 
						property="anulacionObra.corrida.estadoCorrida.desEstadoCorrida"/></td>
				</tr>
			</table>	
		</fieldset>
		<!-- FIN Datos de la Anulacion de Obra -->		
		
		<!-- 1- Simular Anulacion de Obra -->		
		<fieldset>
			<legend><bean:message bundle="rec" key="rec.procesoAnulacionObraAdapter.paso1.title"/></legend>			
			<table class="tabladatos" width="100%">

				<!-- Descripcion del Paso 1 -->		
				<tr>
					<td class="normal" colspan="4" align="left"><bean:message bundle="rec" key="rec.procesoAnulacionObraAdapter.paso1.descripcion"/></td>
				</tr>

				<tr>
				<!-- Fecha del Paso 1 -->		
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.fecha.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoAnulacionObraAdapterVO" property="pasoCorrida1.fechaCorridaView"/></td>

				<!-- Hora del Paso 1 -->							
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.hora.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoAnulacionObraAdapterVO" property="pasoCorrida1.horaCorridaView"/></td>
				</tr>	

				<!-- Observacion del Paso 1 -->							
				<tr>
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.observacion.label"/>: </label></td>
					<td colspan="2" class="normal"><bean:write name="procesoAnulacionObraAdapterVO" property="pasoCorrida1.observacion"/></td>
				</tr>	

				<!-- Estado del Paso 1 -->							
				<tr>
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.estado.label"/>: </label></td>
					<td colspan="2" class="normal"><bean:write name="procesoAnulacionObraAdapterVO" property="pasoCorrida1.estadoCorrida.desEstadoCorrida"/></td>
				</tr>	
			</table>

			<logic:equal name="procesoAnulacionObraAdapterVO" property="paramPaso" value="1">
				<table class="tablabotones" width="100%">
				<tr>				
			    	<td align="center">
		
						<!-- Boton Activar -->							
						<logic:equal name="procesoAnulacionObraAdapterVO" property="activarEnabled" value="true">
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');" disabled='false'>
								<bean:message bundle="pro" key="pro.abm.button.activar"/>
							</html:button>&nbsp;&nbsp;
						</logic:equal>
						<logic:equal name="procesoAnulacionObraAdapterVO" property="activarEnabled" value="false">
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');" disabled='true'>
								<bean:message bundle="pro" key="pro.abm.button.activar"/>
							</html:button>&nbsp;&nbsp;
						</logic:equal>
						
						<!-- Boton Reprogramar -->							
						<logic:equal name="procesoAnulacionObraAdapterVO" property="reprogramarEnabled" value="true">
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('reprogramar', '');" disabled='false'>
								<bean:message bundle="pro" key="pro.abm.button.reprogramar"/>
							</html:button>&nbsp;&nbsp;
						</logic:equal>
						<logic:equal name="procesoAnulacionObraAdapterVO" property="reprogramarEnabled" value="false">
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('reprogramar', '');" disabled='true'>
								<bean:message bundle="pro" key="pro.abm.button.reprogramar"/>
							</html:button>&nbsp;&nbsp;
						</logic:equal>
						
						<!-- Boton Cancelar -->							
						<logic:equal name="procesoAnulacionObraAdapterVO" property="cancelarEnabled" value="true">
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('cancelar', '');" disabled='false'>
								<bean:message bundle="pro" key="pro.abm.button.cancelar"/>
							</html:button>&nbsp;&nbsp;
						</logic:equal>
						<logic:equal name="procesoAnulacionObraAdapterVO" property="cancelarEnabled" value="false">
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('cancelar', '');" disabled='true'>
								<bean:message bundle="pro" key="pro.abm.button.cancelar"/>
							</html:button>&nbsp;&nbsp;
						</logic:equal>
						
						<%@ include file="/pro/adpProceso/includeLogFileButton.jsp" %>
						
						<!-- Boton Refrescar -->
						<logic:equal name="procesoAnulacionObraAdapterVO" property="refrescarEnabled" value="enabled">
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('refill', '');" disabled='false' styleId="locate_paso">
								<bean:message bundle="pro" key="pro.abm.button.refill"/>
							</html:button>&nbsp;&nbsp;
						</logic:equal>
						<logic:equal name="procesoAnulacionObraAdapterVO" property="refrescarEnabled" value="disabled">
							<html:button property="btnAccionBase"  styleClass="boton" disabled='true' styleId="locate_paso">
								<bean:message bundle="pro" key="pro.abm.button.refill"/>
							</html:button>&nbsp;&nbsp;
						</logic:equal>
					</td>
				 </tr>
				</table>
			</logic:equal>
		</fieldset>
		<!--FIN 1- Simular Anulacion de Obra -->		
		
	<logic:equal name="procesoAnulacionObraAdapterVO" property="paramPaso" value="2">
		<!-- Reportes de Deuda CDM para Simulacion  -->		
		<fieldset>
			<legend><bean:message bundle="rec" key="rec.procesoAnulacionObraAdapter.paso1.reportes.title"/></legend>					
			<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="rec" key="rec.procesoAnulacionObraAdapter.reportesDisponibles"/></caption>
	    	<tbody>
				<logic:notEmpty  name="procesoAnulacionObraAdapterVO" property="listFileCorrida1">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Adjunto -->
						<th align="left"><bean:message bundle="base" key="base.nombre.label"/></th>
						<th align="left"><bean:message bundle="base" key="base.descripcion.label"/></th>						
					</tr>
					<logic:iterate id="FileCorridaVO" name="procesoAnulacionObraAdapterVO" property="listFileCorrida1">
						<tr>
							<!-- Adjunto -->
							<td>
								<logic:equal name="FileCorridaVO" property="esPlanilla" value="true">
									<a style="cursor: pointer; cursor: hand;" onclick="submitDownload('downloadFile', '<bean:write name="FileCorridaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/excel0.gif"/>
									</a>
								</logic:equal>
							</td>
							<td><bean:write name="FileCorridaVO" property="nombre"/>&nbsp;</td>
							<td><bean:write name="FileCorridaVO" property="observacion"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="procesoAnulacionObraAdapterVO" property="listFileCorrida1">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			</tbody>
			</table>
		</fieldset>
		<!-- Fin Reportes de Deuda CDM para Simulacion  -->
	</logic:equal>

		<!-- 2- Ejecutar Anulacion de Obra -->		
		<fieldset>
			<legend><bean:message bundle="rec" key="rec.procesoAnulacionObraAdapter.paso2.title"/></legend>			
			<table class="tabladatos" width="100%">

				<!-- Descripcion del Paso 2 -->		
				<tr>
					<td class="normal" colspan="4" align="left"><bean:message bundle="rec" key="rec.procesoAnulacionObraAdapter.paso2.descripcion"/></td>
				</tr>

				<tr>
				<!-- Fecha del Paso 2 -->		
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.fecha.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoAnulacionObraAdapterVO" property="pasoCorrida2.fechaCorridaView"/></td>

				<!-- Hora del Paso 2 -->							
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.hora.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoAnulacionObraAdapterVO" property="pasoCorrida2.horaCorridaView"/></td>
				</tr>	

				<!-- Observacion del Paso 1 -->							
				<tr>
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.observacion.label"/>: </label></td>
					<td colspan="2" class="normal"><bean:write name="procesoAnulacionObraAdapterVO" property="pasoCorrida2.observacion"/></td>
				</tr>	

				<!-- Estado del Paso 2 -->							
				<tr>
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.estado.label"/>: </label></td>
					<td colspan="2" class="normal"><bean:write name="procesoAnulacionObraAdapterVO" property="pasoCorrida2.estadoCorrida.desEstadoCorrida"/></td>
				</tr>	
			</table>


				<table class="tablabotones" width="100%">
				<tr>				
			    	<td align="center">
		
						<!-- Boton Activar -->							
						<logic:equal name="procesoAnulacionObraAdapterVO" property="activarEnabled" value="true">
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');" disabled='false'>
								<bean:message bundle="pro" key="pro.abm.button.activar"/>
							</html:button>&nbsp;&nbsp;
						</logic:equal>
						<logic:equal name="procesoAnulacionObraAdapterVO" property="activarEnabled" value="false">
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');" disabled='true'>
								<bean:message bundle="pro" key="pro.abm.button.activar"/>
							</html:button>&nbsp;&nbsp;
						</logic:equal>
						
						<!-- Boton Reprogramar -->							
						<logic:equal name="procesoAnulacionObraAdapterVO" property="reprogramarEnabled" value="true">
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('reprogramar', '');" disabled='false'>
								<bean:message bundle="pro" key="pro.abm.button.reprogramar"/>
							</html:button>&nbsp;&nbsp;
						</logic:equal>
						<logic:equal name="procesoAnulacionObraAdapterVO" property="reprogramarEnabled" value="false">
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('reprogramar', '');" disabled='true'>
								<bean:message bundle="pro" key="pro.abm.button.reprogramar"/>
							</html:button>&nbsp;&nbsp;
						</logic:equal>
						
						<!-- Boton Cancelar -->							
						<logic:equal name="procesoAnulacionObraAdapterVO" property="cancelarEnabled" value="true">
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('cancelar', '');" disabled='false'>
								<bean:message bundle="pro" key="pro.abm.button.cancelar"/>
							</html:button>&nbsp;&nbsp;
						</logic:equal>
						<logic:equal name="procesoAnulacionObraAdapterVO" property="cancelarEnabled" value="false">
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('cancelar', '');" disabled='true'>
								<bean:message bundle="pro" key="pro.abm.button.cancelar"/>
							</html:button>&nbsp;&nbsp;
						</logic:equal>
						
						<!-- Boton Reiniciar -->							
						<logic:equal name="procesoAnulacionObraAdapterVO" property="reiniciarEnabled" value="true">
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('reiniciar', '');" disabled='false'>
								<bean:message bundle="pro" key="pro.abm.button.reiniciar"/>
							</html:button>&nbsp;&nbsp;
						</logic:equal>
						<logic:equal name="procesoAnulacionObraAdapterVO" property="reiniciarEnabled" value="false">
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('reiniciar', '');" disabled='true'>
								<bean:message bundle="pro" key="pro.abm.button.reiniciar"/>
							</html:button>&nbsp;&nbsp;
						</logic:equal>
						
					</td>
				 </tr>
				</table>

		</fieldset>
	
	<logic:equal name="procesoAnulacionObraAdapterVO" property="paramPaso" value="2">
		<!-- Reportes con los saldos  -->		
		<fieldset>
			<legend><bean:message bundle="rec" key="rec.procesoAnulacionObraAdapter.paso2.reportes.title"/></legend>					
			<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="rec" key="rec.procesoAnulacionObraAdapter.reportesDisponibles"/></caption>
	    	<tbody>
				<logic:notEmpty  name="procesoAnulacionObraAdapterVO" property="listFileCorrida2">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Adjunto -->
						<th align="left"><bean:message bundle="base" key="base.nombre.label"/></th>
						<th align="left"><bean:message bundle="base" key="base.descripcion.label"/></th>						
					</tr>
					<logic:iterate id="FileCorridaVO" name="procesoAnulacionObraAdapterVO" property="listFileCorrida2">
						<tr>
							<!-- Adjunto -->
							<td>
								<logic:equal name="FileCorridaVO" property="esPlanilla" value="true">
									<a style="cursor: pointer; cursor: hand;" onclick="submitDownload('downloadFile', '<bean:write name="FileCorridaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/excel0.gif"/>
									</a>
								</logic:equal>
							</td>
							<td><bean:write name="FileCorridaVO" property="nombre"/>&nbsp;</td>
							<td><bean:write name="FileCorridaVO" property="observacion"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="procesoAnulacionObraAdapterVO" property="listFileCorrida2">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			</tbody>
			</table>
		</fieldset>
		<!-- Fin Reportes de Deuda CDM para Simulacion  -->
	</logic:equal>


		<!-- FIN 2- Ejecutar Anulacion de Obra -->		
		
		<table class="tablabotones">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
   	    	</tr>
		</table>
	   	
		<input type="hidden" name="method" value=""/>
		<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
		<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="fileParam" value=""/>
		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		<input type="hidden" name="idTipoEmision" value="1"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->		
					