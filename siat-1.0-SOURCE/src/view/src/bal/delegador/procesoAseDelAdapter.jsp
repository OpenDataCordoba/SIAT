<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/AdministrarProcesoAseDel.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.procesarAseDelAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		<!-- Datos del Asentamiento Delegado -->		
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.aseDel.title"/></legend>			
			<table class="tabladatos" width="100%">
				<!-- Servicio Banco -->		
				<tr>
					<td><label><bean:message bundle="def" key="def.servicioBanco.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoAseDelAdapterVO" property="aseDel.servicioBanco.desServicioBanco"/></td>
				</tr>	
				<!-- Fecha Balance -->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.aseDel.fechaBalance.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoAseDelAdapterVO" property="aseDel.fechaBalanceView"/></td>
				</tr>		
				<!-- Ejercicio y Estado Ejercicio-->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.ejercicio.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoAseDelAdapterVO" property="aseDel.ejercicio.desEjercicio"/></td>
					<td><label><bean:message bundle="bal" key="bal.estEjercicio.label"/>: </label></td>
					<td  class="normal"><bean:write name="procesoAseDelAdapterVO" property="aseDel.ejercicio.estEjercicio.desEjeBal"/></td>
				</tr>		
				<!-- Mensaje que depende del estado del ejercicio -->
				<tr>
				<logic:equal name="procesoAseDelAdapterVO" property="paramEstadoEjercicio" value="CERRADO">
				<td class="normal" colspan="4" align="center"><label>&nbsp;<bean:message bundle="bal" key="bal.aseDel.ejercicioCerrado.label"/></label></td>
				</logic:equal>
				<logic:equal name="procesoAseDelAdapterVO" property="paramEstadoEjercicio" value="ABIERTO">
				<td class="normal" colspan="4" align="center"><label>&nbsp;<bean:message bundle="bal" key="bal.aseDel.ejercicioAbierto.label"/></label></td>
				</logic:equal>
				</tr>
				<!-- Caso -->
				<!-- Inclucion de CasoView -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
					<td colspan="3">
						<bean:define id="IncludedVO" name="procesoAseDelAdapterVO" property="aseDel"/>
						<%@ include file="/cas/caso/includeCasoView.jsp" %>				
					</td>
				</tr>
				<!-- Fin Inclucion de CasoView -->
				<!-- Observacion-->		
				<tr>
					<td><label><bean:message bundle="bal" key="bal.aseDel.observacion.label"/>: </label></td>
					<td colspan="3" class="normal"><bean:write name="procesoAseDelAdapterVO" property="aseDel.observacion"/></td>					
				</tr>
				<!-- Estado Corrida-->		
				<tr>
					<td><label><bean:message bundle="bal" key="bal.aseDel.estadoProceso.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoAseDelAdapterVO" property="aseDel.corrida.estadoCorrida.desEstadoCorrida"/></td>					
				</tr>
				<!-- Usuario Alta y Ultima Modificacion-->		
				<tr>
					<td><label><bean:message bundle="bal" key="bal.aseDel.usuarioAlta.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoAseDelAdapterVO" property="aseDel.usuarioAlta"/></td>					
					<td><label><bean:message bundle="base" key="base.usuarioUltMdf.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoAseDelAdapterVO" property="aseDel.usuario"/></td>					
				</tr>
			</table>
			<table class="tablabotones" width="100%">
			<tr>				
				<td align="right">
					<bean:define id="modificarEncabezadoEnabled" name="procesoAseDelAdapterVO" property="modificarEncabezadoEnabled"/>
					<logic:equal name="procesoAseDelAdapterVO" property="paramModificar" value="true">
						<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificar', 
							'<bean:write name="procesoAseDelAdapterVO" property="aseDel.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.modificar"/>"/>
					</logic:equal>
					<logic:equal name="procesoAseDelAdapterVO" property="paramModificar" value="false">
						<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificar', 
							'<bean:write name="procesoAseDelAdapterVO" property="aseDel.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.modificar"/>" disabled="true"/>
					</logic:equal>
				</td>
			</tr>
			</table>
		</fieldset>
		<!-- Fin Datos del Asentamiento Delegado -->		
		
		<!-- 1- Determinar Transacciones a Procesar y Enviar a sistema externo -->		
		<logic:equal name="procesoAseDelAdapterVO" property="paramPaso" value="1">
			<a name="pasoActual">&nbsp;</a> 
		</logic:equal>
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.procesarAseDelAdapter.fieldset1"/></legend>			
			<table class="tabladatos" width="100%">
			<tr>
				<td class="normal" colspan="4" align="left"><bean:message bundle="bal" key="bal.procesoAseDelAdapter.paso1.description"/></td>
			</tr>
			<tr>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.fecha.label"/>: </label></td>
				<td class="normal"><bean:write name="procesoAseDelAdapterVO" property="pasoCorrida1.fechaCorridaView"/></td>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.hora.label"/>: </label></td>
				<td class="normal"><bean:write name="procesoAseDelAdapterVO" property="pasoCorrida1.horaCorridaView"/></td>
			</tr>	
			<tr>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.observacion.label"/>: </label></td>
				<td colspan="2" class="normal"><bean:write name="procesoAseDelAdapterVO" property="pasoCorrida1.observacion"/></td>
			</tr>	
			<tr>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.estado.label"/>: </label></td>
				<td colspan="2" class="normal"><bean:write name="procesoAseDelAdapterVO" property="pasoCorrida1.estadoCorrida.desEstadoCorrida"/></td>
			</tr>	
			</table>
			<logic:equal name="procesoAseDelAdapterVO" property="paramPaso" value="1">
			<table class="tablabotones" width="100%">
			<tr>				
		    	<td align="center">
					<logic:equal name="procesoAseDelAdapterVO" property="paramActivar" value="true">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');" disabled='false'>
							<bean:message bundle="pro" key="pro.abm.button.activar"/>
						</html:button>&nbsp;&nbsp;
					</logic:equal>
					<logic:equal name="procesoAseDelAdapterVO" property="paramActivar" value="false">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');" disabled='true'>
							<bean:message bundle="pro" key="pro.abm.button.activar"/>
						</html:button>&nbsp;&nbsp;
					</logic:equal>
					<logic:equal name="procesoAseDelAdapterVO" property="paramReprogramar" value="true">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('reprogramar', '');" disabled='false'>
							<bean:message bundle="pro" key="pro.abm.button.reprogramar"/>
						</html:button>&nbsp;&nbsp;
					</logic:equal>
					<logic:equal name="procesoAseDelAdapterVO" property="paramReprogramar" value="false">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('reprogramar', '');" disabled='true'>
							<bean:message bundle="pro" key="pro.abm.button.reprogramar"/>
						</html:button>&nbsp;&nbsp;
					</logic:equal>
					<logic:equal name="procesoAseDelAdapterVO" property="paramCancelar" value="true">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('cancelar', '');" disabled='false'>
							<bean:message bundle="pro" key="pro.abm.button.cancelar"/>
						</html:button>&nbsp;&nbsp;	
					</logic:equal>
					<logic:equal name="procesoAseDelAdapterVO" property="paramCancelar" value="false">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('cancelar', '');" disabled='true'>
							<bean:message bundle="pro" key="pro.abm.button.cancelar"/>
						</html:button>&nbsp;&nbsp;	
					</logic:equal>
					<logic:equal name="procesoAseDelAdapterVO" property="paramReiniciar" value="true">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('reiniciar', '');" disabled='false'>
							<bean:message bundle="pro" key="pro.abm.button.reiniciar"/>
						</html:button>&nbsp;&nbsp;	
					</logic:equal>			
					<logic:equal name="procesoAseDelAdapterVO" property="paramReiniciar" value="false">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('reiniciar', '');" disabled='false'>
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
		<!-- Fin Determinar Transacciones a Procesar y Enviar a sistema externo -->			

		<!-- Reportes de Transacciones  -->		
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.procesarAseDelAdapter.fieldset2"/></legend>					
			<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.procesoAseDelAdapter.paso1.reportes"/></caption>
	    	<tbody>
				<logic:notEmpty  name="procesoAseDelAdapterVO" property="listFileCorrida1">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Adjunto -->
						<th align="left"><bean:message bundle="base" key="base.nombre.label"/></th>
						<th align="left"><bean:message bundle="base" key="base.descripcion.label"/></th>						
					</tr>
					<logic:iterate id="FileCorridaVO" name="procesoAseDelAdapterVO" property="listFileCorrida1">
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
				<logic:empty  name="procesoAseDelAdapterVO" property="listFileCorrida1">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			</tbody>
			</table>
		</fieldset>
		<!-- Fin Reportes de Transacciones  -->		
		
		<!-- 2- Leer archivos de sistema Externo y cargar datos en SIAT  -->		
		<logic:equal name="procesoAseDelAdapterVO" property="paramPaso" value="2">
			<a name="pasoActual">&nbsp;</a> 
		</logic:equal>
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.procesarAseDelAdapter.fieldset3"/></legend>			
			<table class="tabladatos" width="100%">
			<tr>
				<td class="normal" colspan="4" align="left"><bean:message bundle="bal" key="bal.procesoAseDelAdapter.paso2.description"/></td>
			</tr>
			<tr>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.fecha.label"/>: </label></td>
				<td class="normal"><bean:write name="procesoAseDelAdapterVO" property="pasoCorrida2.fechaCorridaView"/></td>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.hora.label"/>: </label></td>
				<td class="normal"><bean:write name="procesoAseDelAdapterVO" property="pasoCorrida2.horaCorridaView"/></td>
			</tr>	
			<tr>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.observacion.label"/>: </label></td>
				<td colspan="2" class="normal"><bean:write name="procesoAseDelAdapterVO" property="pasoCorrida2.observacion"/></td>
			</tr>	
			<tr>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.estado.label"/>: </label></td>
				<td colspan="2" class="normal"><bean:write name="procesoAseDelAdapterVO" property="pasoCorrida2.estadoCorrida.desEstadoCorrida"/></td>
			</tr>	
			</table>
			<logic:equal name="procesoAseDelAdapterVO" property="paramPaso" value="2">
			<table class="tablabotones" width="100%">
			<tr>				
		    	<td align="center">
					<logic:equal name="procesoAseDelAdapterVO" property="paramActivar" value="true">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');" disabled='false'>
							<bean:message bundle="pro" key="pro.abm.button.activar"/>
						</html:button>&nbsp;&nbsp;
					</logic:equal>
					<logic:equal name="procesoAseDelAdapterVO" property="paramActivar" value="false">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');" disabled='true'>
							<bean:message bundle="pro" key="pro.abm.button.activar"/>
						</html:button>&nbsp;&nbsp;
					</logic:equal>
					<logic:equal name="procesoAseDelAdapterVO" property="paramReprogramar" value="true">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('reprogramar', '');" disabled='false'>
							<bean:message bundle="pro" key="pro.abm.button.reprogramar"/>
						</html:button>&nbsp;&nbsp;
					</logic:equal>
					<logic:equal name="procesoAseDelAdapterVO" property="paramReprogramar" value="false">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('reprogramar', '');" disabled='true'>
							<bean:message bundle="pro" key="pro.abm.button.reprogramar"/>
						</html:button>&nbsp;&nbsp;
					</logic:equal>
					<logic:equal name="procesoAseDelAdapterVO" property="paramCancelar" value="true">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('cancelar', '');" disabled='false'>
							<bean:message bundle="pro" key="pro.abm.button.cancelar"/>
						</html:button>&nbsp;&nbsp;	
					</logic:equal>
					<logic:equal name="procesoAseDelAdapterVO" property="paramCancelar" value="false">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('cancelar', '');" disabled='true'>
							<bean:message bundle="pro" key="pro.abm.button.cancelar"/>
						</html:button>&nbsp;&nbsp;	
					</logic:equal>
					<logic:equal name="procesoAseDelAdapterVO" property="paramReiniciar" value="true">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('reiniciar', '');" disabled='false'>
							<bean:message bundle="pro" key="pro.abm.button.reiniciar"/>
						</html:button>&nbsp;&nbsp;	
					</logic:equal>			
					<logic:equal name="procesoAseDelAdapterVO" property="paramReiniciar" value="false">
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
		<!-- Fin Leer archivos de sistema Externo y cargar datos en SIAT  -->		
		
		<!-- Reportes de Sincronismo  -->		
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.procesarAseDelAdapter.fieldset4"/></legend>			
			<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.procesoAseDelAdapter.paso2.reportes"/></caption>
	    	<tbody>
					<logic:notEmpty  name="procesoAseDelAdapterVO" property="listFileCorrida2">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Adjunto -->
						<th align="left"><bean:message bundle="base" key="base.nombre.label"/></th>
						<th align="left"><bean:message bundle="base" key="base.descripcion.label"/></th>						
					</tr>
					<logic:iterate id="FileCorridaVO" name="procesoAseDelAdapterVO" property="listFileCorrida2">
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
				<logic:empty  name="procesoAseDelAdapterVO" property="listFileCorrida2">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
		</tbody>
			</table>
		</fieldset>
		<!-- Fin Reportes de Sincronismo  -->		
		
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

<logic:notEqual name="procesoAseDelAdapterVO" property="paramPaso" value="">
	<script type="text/javascript">irAPasoActual();</script>
</logic:notEqual>
