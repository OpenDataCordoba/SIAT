<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/AdministrarProcesoAsentamiento.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.procesarAsentamientoAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		<!-- Datos del Asentamiento de Pagos -->		
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.asentamiento.title"/></legend>			
			<table class="tabladatos" width="100%">
				<!-- Servicio Banco -->		
				<tr>
					<td><label><bean:message bundle="def" key="def.servicioBanco.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoAsentamientoAdapterVO" property="asentamiento.servicioBanco.desServicioBanco"/></td>
				</tr>	
				<!-- Fecha Balance -->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.asentamiento.fechaBalance.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoAsentamientoAdapterVO" property="asentamiento.fechaBalanceView"/></td>
				</tr>		
				<!-- Ejercicio y Estado Ejercicio-->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.ejercicio.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoAsentamientoAdapterVO" property="asentamiento.ejercicio.desEjercicio"/></td>
					<td><label><bean:message bundle="bal" key="bal.estEjercicio.label"/>: </label></td>
					<td  class="normal"><bean:write name="procesoAsentamientoAdapterVO" property="asentamiento.ejercicio.estEjercicio.desEjeBal"/></td>
				</tr>		
				<!-- Mensaje que depende del estado del ejercicio -->
				<tr>
				<logic:equal name="procesoAsentamientoAdapterVO" property="paramEstadoEjercicio" value="CERRADO">
				<td class="normal" colspan="4" align="center"><label>&nbsp;<bean:message bundle="bal" key="bal.asentamiento.ejercicioCerrado.label"/></label></td>
				</logic:equal>
				<logic:equal name="procesoAsentamientoAdapterVO" property="paramEstadoEjercicio" value="ABIERTO">
				<td class="normal" colspan="4" align="center"><label>&nbsp;<bean:message bundle="bal" key="bal.asentamiento.ejercicioAbierto.label"/></label></td>
				</logic:equal>
				</tr>
				<!-- Caso -->
				<!-- Inclucion de CasoView -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
					<td colspan="3">
						<bean:define id="IncludedVO" name="procesoAsentamientoAdapterVO" property="asentamiento"/>
						<%@ include file="/cas/caso/includeCasoView.jsp" %>				
					</td>
				</tr>
				<!-- Fin Inclucion de CasoView -->
				<!-- Observacion-->		
				<tr>
					<td><label><bean:message bundle="bal" key="bal.asentamiento.observacion.label"/>: </label></td>
					<td colspan="3" class="normal"><bean:write name="procesoAsentamientoAdapterVO" property="asentamiento.observacion"/></td>					
				</tr>
				<!-- Estado Corrida-->		
				<tr>
					<td><label><bean:message bundle="bal" key="bal.asentamiento.estadoProceso.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoAsentamientoAdapterVO" property="asentamiento.corrida.estadoCorrida.desEstadoCorrida"/></td>					
				</tr>
				<!-- Usuario Alta y Ultima Modificacion-->		
				<tr>
					<td><label><bean:message bundle="bal" key="bal.asentamiento.usuarioAlta.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoAsentamientoAdapterVO" property="asentamiento.usuarioAlta"/></td>					
					<td><label><bean:message bundle="base" key="base.usuarioUltMdf.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoAsentamientoAdapterVO" property="asentamiento.usuario"/></td>					
				</tr>
				<logic:equal name="procesoAsentamientoAdapterVO" property="tieneBalanceAsociado" value="true">
					<!-- Nro Balance Asociado -->
					<tr>
						<td><label><bean:message bundle="bal" key="bal.balance.numero.ref"/>: </label></td>
						<td class="normal"><bean:write name="procesoAsentamientoAdapterVO" property="asentamiento.balance.id"  bundle="base" formatKey="general.format.id"/></td>
					</tr>		
				</logic:equal>

			</table>
			<table class="tablabotones" width="100%">
			<tr>				
				<td align="right">
					<bean:define id="modificarEncabezadoEnabled" name="procesoAsentamientoAdapterVO" property="modificarEncabezadoEnabled"/>
					<logic:equal name="procesoAsentamientoAdapterVO" property="paramModificar" value="true">
						<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificar', 
							'<bean:write name="procesoAsentamientoAdapterVO" property="asentamiento.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.modificar"/>"/>
					</logic:equal>
					<logic:equal name="procesoAsentamientoAdapterVO" property="paramModificar" value="false">
						<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificar', 
							'<bean:write name="procesoAsentamientoAdapterVO" property="asentamiento.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.modificar"/>" disabled="true"/>
					</logic:equal>
				</td>
			</tr>
			</table>
		</fieldset>
		<!-- Fin Datos del Asentamiento de Pagos -->		
		
		<!-- 1- Determinar Transacciones a Procesar -->		
		<logic:equal name="procesoAsentamientoAdapterVO" property="paramPaso" value="1">
			<a name="pasoActual">&nbsp;</a> 
		</logic:equal>
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.procesarAsentamientoAdapter.fieldset1"/></legend>			
			<table class="tabladatos" width="100%">
			<tr>
				<td class="normal" colspan="4" align="left"><bean:message bundle="bal" key="bal.procesoAsentamientoAdapter.paso1.description"/></td>
			</tr>
			<tr>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.fecha.label"/>: </label></td>
				<td class="normal"><bean:write name="procesoAsentamientoAdapterVO" property="pasoCorrida1.fechaCorridaView"/></td>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.hora.label"/>: </label></td>
				<td class="normal"><bean:write name="procesoAsentamientoAdapterVO" property="pasoCorrida1.horaCorridaView"/></td>
			</tr>	
			<tr>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.observacion.label"/>: </label></td>
				<td colspan="2" class="normal"><bean:write name="procesoAsentamientoAdapterVO" property="pasoCorrida1.observacion"/></td>
			</tr>	
			<tr>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.estado.label"/>: </label></td>
				<td colspan="2" class="normal"><bean:write name="procesoAsentamientoAdapterVO" property="pasoCorrida1.estadoCorrida.desEstadoCorrida"/></td>
			</tr>	
			</table>
			<logic:equal name="procesoAsentamientoAdapterVO" property="paramPaso" value="1">
			<table class="tablabotones" width="100%">
			<tr>				
		    	<td align="center">
					<logic:equal name="procesoAsentamientoAdapterVO" property="paramActivar" value="true">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');" disabled='false'>
							<bean:message bundle="pro" key="pro.abm.button.activar"/>
						</html:button>&nbsp;&nbsp;
					</logic:equal>
					<logic:equal name="procesoAsentamientoAdapterVO" property="paramActivar" value="false">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');" disabled='true'>
							<bean:message bundle="pro" key="pro.abm.button.activar"/>
						</html:button>&nbsp;&nbsp;
					</logic:equal>
					<logic:equal name="procesoAsentamientoAdapterVO" property="paramReprogramar" value="true">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('reprogramar', '');" disabled='false'>
							<bean:message bundle="pro" key="pro.abm.button.reprogramar"/>
						</html:button>&nbsp;&nbsp;
					</logic:equal>
					<logic:equal name="procesoAsentamientoAdapterVO" property="paramReprogramar" value="false">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('reprogramar', '');" disabled='true'>
							<bean:message bundle="pro" key="pro.abm.button.reprogramar"/>
						</html:button>&nbsp;&nbsp;
					</logic:equal>
					<logic:equal name="procesoAsentamientoAdapterVO" property="paramCancelar" value="true">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('cancelar', '');" disabled='false'>
							<bean:message bundle="pro" key="pro.abm.button.cancelar"/>
						</html:button>&nbsp;&nbsp;	
					</logic:equal>
					<logic:equal name="procesoAsentamientoAdapterVO" property="paramCancelar" value="false">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('cancelar', '');" disabled='true'>
							<bean:message bundle="pro" key="pro.abm.button.cancelar"/>
						</html:button>&nbsp;&nbsp;	
					</logic:equal>
					<logic:equal name="procesoAsentamientoAdapterVO" property="paramReiniciar" value="true">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('reiniciar', '');" disabled='false'>
							<bean:message bundle="pro" key="pro.abm.button.reiniciar"/>
						</html:button>&nbsp;&nbsp;	
					</logic:equal>			
					<logic:equal name="procesoAsentamientoAdapterVO" property="paramReiniciar" value="false">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('reiniciar', '');" disabled='true'>
							<bean:message bundle="pro" key="pro.abm.button.reiniciar"/>
						</html:button>&nbsp;&nbsp;	
					</logic:equal>		

					<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('refill', '');" disabled='false' styleId="locate_paso">
						<bean:message bundle="pro" key="pro.abm.button.refill"/>
					</html:button>&nbsp;&nbsp;

					<%@ include file="/pro/adpProceso/includeLogFileButton.jsp" %>	
	   	    	</td>	   	    	
 			</tr>
			</table>
			</logic:equal>
		</fieldset>
		<!-- Fin Determinar Transacciones a Procesar -->			

		<!-- Reportes de Transacciones  -->		
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.procesarAsentamientoAdapter.fieldset2"/></legend>					
			<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.procesoAsentamientoAdapter.paso1.reportes"/></caption>
	    	<tbody>
				<logic:notEmpty  name="procesoAsentamientoAdapterVO" property="listFileCorrida1">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Adjunto -->
						<th align="left"><bean:message bundle="base" key="base.nombre.label"/></th>
						<th align="left"><bean:message bundle="base" key="base.descripcion.label"/></th>						
					</tr>
					<logic:iterate id="FileCorridaVO" name="procesoAsentamientoAdapterVO" property="listFileCorrida1">
							<tr>
							<!-- Adjunto -->
							<td>
								<logic:equal name="FileCorridaVO" property="esPdf" value="true">							
									<a style="cursor: pointer; cursor: hand;" onclick="submitDownload('downloadFile', '<bean:write name="FileCorridaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/imprimir0.gif"/>
									</a>
								</logic:equal>
								<logic:equal name="FileCorridaVO" property="esPlanilla" value="true">
									<a style="cursor: pointer; cursor: hand;" onclick="submitDownload('downloadFile', '<bean:write name="FileCorridaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/excel0.gif"/>
									</a>
								</logic:equal>
								<logic:equal name="FileCorridaVO" property="esPdf" value="false">	
									<logic:equal name="FileCorridaVO" property="esPlanilla" value="false">														
										<a style="cursor: pointer; cursor: hand;" onclick="submitDownload('downloadFile', '<bean:write name="FileCorridaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/sincFile0.gif"/>
										</a>	
									</logic:equal>								
								</logic:equal>								
							</td>
							<td><bean:write name="FileCorridaVO" property="nombre"/>&nbsp;</td>
							<td><bean:write name="FileCorridaVO" property="observacion"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="procesoAsentamientoAdapterVO" property="listFileCorrida1">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			</tbody>
			</table>
		</fieldset>
		<!-- Fin Reportes de Transacciones  -->		
		
		<!-- 2- Distribuir Partidas  -->		
		<logic:equal name="procesoAsentamientoAdapterVO" property="paramPaso" value="2">
			<a name="pasoActual">&nbsp;</a> 
		</logic:equal>
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.procesarAsentamientoAdapter.fieldset3"/></legend>			
			<table class="tabladatos" width="100%">
			<tr>
				<td class="normal" colspan="4" align="left"><bean:message bundle="bal" key="bal.procesoAsentamientoAdapter.paso2.description"/></td>
			</tr>
			<tr>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.fecha.label"/>: </label></td>
				<td class="normal"><bean:write name="procesoAsentamientoAdapterVO" property="pasoCorrida2.fechaCorridaView"/></td>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.hora.label"/>: </label></td>
				<td class="normal"><bean:write name="procesoAsentamientoAdapterVO" property="pasoCorrida2.horaCorridaView"/></td>
			</tr>	
			<tr>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.observacion.label"/>: </label></td>
				<td colspan="2" class="normal"><bean:write name="procesoAsentamientoAdapterVO" property="pasoCorrida2.observacion"/></td>
			</tr>	
			<tr>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.estado.label"/>: </label></td>
				<td colspan="2" class="normal"><bean:write name="procesoAsentamientoAdapterVO" property="pasoCorrida2.estadoCorrida.desEstadoCorrida"/></td>
			</tr>	
			</table>
			<logic:equal name="procesoAsentamientoAdapterVO" property="paramPaso" value="2">
			<table class="tablabotones" width="100%">
			<tr>				
		    	<td align="center">
					<logic:equal name="procesoAsentamientoAdapterVO" property="paramActivar" value="true">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');" disabled='false'>
							<bean:message bundle="pro" key="pro.abm.button.activar"/>
						</html:button>&nbsp;&nbsp;
					</logic:equal>
					<logic:equal name="procesoAsentamientoAdapterVO" property="paramActivar" value="false">
						<logic:equal name="procesoAsentamientoAdapterVO" property="forzarEnabled" value="enabled">		
							<logic:equal name="procesoAsentamientoAdapterVO" property="paramForzar" value="true">
								<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('forzar', '');" disabled='false'>
									<bean:message bundle="pro" key="pro.abm.button.forzar"/>
								</html:button>&nbsp;&nbsp;
							</logic:equal>
							<logic:equal name="procesoAsentamientoAdapterVO" property="paramForzar" value="false">
								<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');" disabled='true'>
									<bean:message bundle="pro" key="pro.abm.button.activar"/>
								</html:button>&nbsp;&nbsp;
							</logic:equal>
						</logic:equal>
						<logic:notEqual name="procesoAsentamientoAdapterVO" property="forzarEnabled" value="enabled">		
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');" disabled='true'>
								<bean:message bundle="pro" key="pro.abm.button.activar"/>
							</html:button>&nbsp;&nbsp;
						</logic:notEqual>
					</logic:equal>
					<logic:equal name="procesoAsentamientoAdapterVO" property="paramReprogramar" value="true">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('reprogramar', '');" disabled='false'>
							<bean:message bundle="pro" key="pro.abm.button.reprogramar"/>
						</html:button>&nbsp;&nbsp;
					</logic:equal>
					<logic:equal name="procesoAsentamientoAdapterVO" property="paramReprogramar" value="false">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('reprogramar', '');" disabled='true'>
							<bean:message bundle="pro" key="pro.abm.button.reprogramar"/>
						</html:button>&nbsp;&nbsp;
					</logic:equal>
					<logic:equal name="procesoAsentamientoAdapterVO" property="paramCancelar" value="true">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('cancelar', '');" disabled='false'>
							<bean:message bundle="pro" key="pro.abm.button.cancelar"/>
						</html:button>&nbsp;&nbsp;	
					</logic:equal>
					<logic:equal name="procesoAsentamientoAdapterVO" property="paramCancelar" value="false">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('cancelar', '');" disabled='true'>
							<bean:message bundle="pro" key="pro.abm.button.cancelar"/>
						</html:button>&nbsp;&nbsp;	
					</logic:equal>
					<logic:equal name="procesoAsentamientoAdapterVO" property="paramReiniciar" value="true">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('reiniciar', '');" disabled='false'>
							<bean:message bundle="pro" key="pro.abm.button.reiniciar"/>
						</html:button>&nbsp;&nbsp;	
					</logic:equal>			
					<logic:equal name="procesoAsentamientoAdapterVO" property="paramReiniciar" value="false">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('reiniciar', '');" disabled='true'>
							<bean:message bundle="pro" key="pro.abm.button.reiniciar"/>
						</html:button>&nbsp;&nbsp;	
					</logic:equal>	
					<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('refill', '');" disabled='false' styleId="locate_paso">
						<bean:message bundle="pro" key="pro.abm.button.refill"/>
					</html:button>&nbsp;&nbsp;
			
			    	<%@ include file="/pro/adpProceso/includeLogFileButton.jsp" %>												
	   	    	</td>	   	    	
 			</tr>
 			&nbsp;&nbsp;
			<tr>
				<td colspan="2"><input type="checkbox" name="logDetalladoEnabled">
				<label><bean:message bundle="bal" key="bal.procesoAsentamientoAdapter.logDetallado"/></label></td>
			</tr>
			</table>
			</logic:equal>
		</fieldset>
		<!-- Fin Distribuir Partidas  -->		
		
		<!-- Reportes de Distribución de Partidas  -->		
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.procesarAsentamientoAdapter.fieldset4"/></legend>			
			<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.procesoAsentamientoAdapter.paso2.reportes"/></caption>
	    	<tbody>
					<logic:notEmpty  name="procesoAsentamientoAdapterVO" property="listFileCorrida2">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Adjunto -->
						<th align="left"><bean:message bundle="base" key="base.nombre.label"/></th>
						<th align="left"><bean:message bundle="base" key="base.descripcion.label"/></th>						
					</tr>
					<logic:iterate id="FileCorridaVO" name="procesoAsentamientoAdapterVO" property="listFileCorrida2">
							<tr>
							<!-- Adjunto -->
							<td>
								<logic:equal name="FileCorridaVO" property="esPdf" value="true">							
									<a style="cursor: pointer; cursor: hand;" onclick="submitDownload('downloadFile', '<bean:write name="FileCorridaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/imprimir0.gif"/>
									</a>
								</logic:equal>
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
				<logic:empty  name="procesoAsentamientoAdapterVO" property="listFileCorrida2">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
		</tbody>
			</table>
		</fieldset>
		<!-- Fin Reportes de Distribución de Partidas  -->		


		<!-- 3- Realizar Asentamiento  -->		
		<logic:equal name="procesoAsentamientoAdapterVO" property="paramPaso" value="3">
			<a name="pasoActual">&nbsp;</a> 
		</logic:equal>		
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.procesarAsentamientoAdapter.fieldset5"/></legend>			
			<table class="tabladatos" width="100%">
			<tr>
				<td class="normal" colspan="4" align="left"><bean:message bundle="bal" key="bal.procesoAsentamientoAdapter.paso3.description"/></td>
			</tr>
			<tr>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.fecha.label"/>: </label></td>
				<td class="normal"><bean:write name="procesoAsentamientoAdapterVO" property="pasoCorrida3.fechaCorridaView"/></td>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.hora.label"/>: </label></td>
				<td class="normal"><bean:write name="procesoAsentamientoAdapterVO" property="pasoCorrida3.horaCorridaView"/></td>
			</tr>	
			<tr>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.observacion.label"/>: </label></td>
				<td colspan="2" class="normal"><bean:write name="procesoAsentamientoAdapterVO" property="pasoCorrida3.observacion"/></td>
			</tr>	
			<tr>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.estado.label"/>: </label></td>
				<td colspan="2" class="normal"><bean:write name="procesoAsentamientoAdapterVO" property="pasoCorrida3.estadoCorrida.desEstadoCorrida"/></td>
			</tr>	
			</table>
			<logic:equal name="procesoAsentamientoAdapterVO" property="paramPaso" value="3">
			<table class="tablabotones" width="100%">
			<tr>				
		    	<td align="center">
					<logic:equal name="procesoAsentamientoAdapterVO" property="paramActivar" value="true">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');" disabled='false'>
							<bean:message bundle="pro" key="pro.abm.button.activar"/>
						</html:button>&nbsp;&nbsp;
					</logic:equal>
					<logic:equal name="procesoAsentamientoAdapterVO" property="paramActivar" value="false">
						<logic:equal name="procesoAsentamientoAdapterVO" property="paramContinuar" value="false">
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');" disabled='true'>
								<bean:message bundle="pro" key="pro.abm.button.activar"/>
							</html:button>&nbsp;&nbsp;
						</logic:equal>
						<logic:equal name="procesoAsentamientoAdapterVO" property="paramContinuar" value="true">
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');" disabled='false'>
								<bean:message bundle="pro" key="pro.abm.button.continuar"/>
							</html:button>&nbsp;&nbsp;
						</logic:equal>
					</logic:equal>
					<logic:equal name="procesoAsentamientoAdapterVO" property="paramReprogramar" value="true">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('reprogramar', '');" disabled='false'>
							<bean:message bundle="pro" key="pro.abm.button.reprogramar"/>
						</html:button>&nbsp;&nbsp;
					</logic:equal>
					<logic:equal name="procesoAsentamientoAdapterVO" property="paramReprogramar" value="false">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('reprogramar', '');" disabled='true'>
							<bean:message bundle="pro" key="pro.abm.button.reprogramar"/>
						</html:button>&nbsp;&nbsp;
					</logic:equal>
					<logic:equal name="procesoAsentamientoAdapterVO" property="paramCancelar" value="true">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('cancelar', '');" disabled='false'>
							<bean:message bundle="pro" key="pro.abm.button.cancelar"/>
						</html:button>&nbsp;&nbsp;	
					</logic:equal>
					<logic:equal name="procesoAsentamientoAdapterVO" property="paramCancelar" value="false">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('cancelar', '');" disabled='true'>
							<bean:message bundle="pro" key="pro.abm.button.cancelar"/>
						</html:button>&nbsp;&nbsp;	
					</logic:equal>
					<logic:equal name="procesoAsentamientoAdapterVO" property="paramReiniciar" value="true">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('reiniciar', '');" disabled='false'>
							<bean:message bundle="pro" key="pro.abm.button.reiniciar"/>
						</html:button>&nbsp;&nbsp;	
					</logic:equal>			
					<logic:equal name="procesoAsentamientoAdapterVO" property="paramReiniciar" value="false">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('reiniciar', '');" disabled='true'>
							<bean:message bundle="pro" key="pro.abm.button.reiniciar"/>
						</html:button>&nbsp;&nbsp;	
					</logic:equal>	

					<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('refill', '');" disabled='false' styleId="locate_paso">
						<bean:message bundle="pro" key="pro.abm.button.refill"/>
					</html:button>&nbsp;&nbsp;
					
					<%@ include file="/pro/adpProceso/includeLogFileButton.jsp" %>		
	   	    	</td>	   	    	
 			</tr>
			</table>
			</logic:equal>
		</fieldset>
		<!-- Fin Realizar Asentamiento  -->		

		<!-- Reportes del Asentamiento  -->		
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.procesarAsentamientoAdapter.fieldset6"/></legend>			
			<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.procesoAsentamientoAdapter.paso3.reportes"/></caption>
	    	<tbody>
				<logic:notEmpty  name="procesoAsentamientoAdapterVO" property="listFileCorrida3">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Adjunto -->
						<th align="left"><bean:message bundle="base" key="base.nombre.label"/></th>
						<th align="left"><bean:message bundle="base" key="base.descripcion.label"/></th>						
					</tr>
					<logic:iterate id="FileCorridaVO" name="procesoAsentamientoAdapterVO" property="listFileCorrida3">
							<tr>
							<!-- Adjunto -->
							<td>
								<logic:equal name="FileCorridaVO" property="esPdf" value="true">							
									<a style="cursor: pointer; cursor: hand;" onclick="submitDownload('downloadFile', '<bean:write name="FileCorridaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/imprimir0.gif"/>
									</a>
								</logic:equal>
								<logic:equal name="FileCorridaVO" property="esPlanilla" value="true">
									<a style="cursor: pointer; cursor: hand;" onclick="submitDownload('downloadFile', '<bean:write name="FileCorridaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/excel0.gif"/>
									</a>
								</logic:equal>
								<logic:equal name="FileCorridaVO" property="esTxt" value="true">
									<a style="cursor: pointer; cursor: hand;" onclick="submitDownload('downloadFile', '<bean:write name="FileCorridaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/sincFile0.gif"/>
									</a>
								</logic:equal>
							</td>
							<td><bean:write name="FileCorridaVO" property="nombre"/>&nbsp;</td>
							<td><bean:write name="FileCorridaVO" property="observacion"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="procesoAsentamientoAdapterVO" property="listFileCorrida3">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			</tbody>
			</table>
		</fieldset>
		<!-- Fin Reportes del Asentamiento  -->				
		
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

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="fileParam" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->		
	
<script type="text/javascript">
	function irAPasoActual() {
   		document.location = document.URL + '#pasoActual';
	}
</script>

<logic:notEqual name="procesoAsentamientoAdapterVO" property="paramPaso" value="">
	<script type="text/javascript">irAPasoActual();</script>
</logic:notEqual>
