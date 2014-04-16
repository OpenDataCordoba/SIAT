<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/gde/AdministrarProcesoLiqCom.do">
	
		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="gde" key="gde.procesoLiqComAdapter.title"/></h1>
		
		<!-- Datos de la LiqCom -->		
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.liqCom.title"/></legend>			
			<table class="tabladatos" width="100%">	
		
				<!-- Fecha Liquidacion -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.liqCom.fechaLiquidacion.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoLiqComAdapterVO" property="liqCom.fechaLiquidacionView"/></td>
				</tr>
				
				<!-- ServicioBanco/Recurso -->
				<tr>
					<logic:equal name="procesoLiqComAdapterVO" property="liqCom.porServicioBanco" value="true">
						<td><label><bean:message bundle="def" key="def.servicioBanco.desServicioBanco.ref"/>: </label></td>
						<td class="normal"><bean:write name="procesoLiqComAdapterVO" property="liqCom.servicioBanco.desServicioBanco"/></td>
					</logic:equal>
					<logic:equal name="procesoLiqComAdapterVO" property="liqCom.porServicioBanco" value="false">
						<td><label><bean:message bundle="def" key="def.recurso.desRecurso.ref"/>: </label></td>
						<td class="normal"><bean:write name="procesoLiqComAdapterVO" property="liqCom.recurso.desRecurso"/></td>
					</logic:equal>
				</tr>
			
				<!-- Procurador -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procurador.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoLiqComAdapterVO" property="liqCom.procurador.descripcion"/></td>
				</tr>
				
				<!-- Observacion -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.liqCom.observacion.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoLiqComAdapterVO" property="liqCom.observacion"/></td>
				</tr>
				
				<!-- Fecha Pago Hasta -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.liqCom.fechaPagoHasta.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoLiqComAdapterVO" property="liqCom.fechaPagoHastaView"/></td>
				</tr>				
								
			</table>	
	
			<table class="tablabotones" width="100%">
				<tr>				
					<td align="right">
						<bean:define id="modificarEncabezadoEnabled" name="procesoLiqComAdapterVO" property="modificarEncabezadoEnabled"/>
						<logic:equal name="procesoLiqComAdapterVO" property="modficarEncLiqComEnabled" value="true">
							<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificar', 
								'<bean:write name="procesoLiqComAdapterVO" property="liqCom.id" bundle="base" formatKey="general.format.id"/>');" 
								value="<bean:message bundle="base" key="abm.button.modificar"/>"/>
						</logic:equal>
						<logic:equal name="procesoLiqComAdapterVO" property="modficarEncLiqComEnabled" value="false">
							<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificar', 
								'<bean:write name="procesoLiqComAdapterVO" property="liqCom.id" bundle="base" formatKey="general.format.id"/>');" 
								value="<bean:message bundle="base" key="abm.button.modificar"/>" disabled="true"/>
						</logic:equal>
					</td>
				</tr>
			</table>	
	
		</fieldset>
		<!-- FIN Datos de la LiqCom -->		
		
		<!-- 1- Liquidar Comision a Procurador -->		
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.procesoLiqComAdapter.paso1.title"/></legend>			
			<table class="tabladatos" width="100%">

				<tr>
				<!-- Fecha del Paso 1 -->		
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.fecha.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoLiqComAdapterVO" property="pasoCorrida1.fechaCorridaView"/></td>

				<!-- Hora del Paso 1 -->							
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.hora.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoLiqComAdapterVO" property="pasoCorrida1.horaCorridaView"/></td>
				</tr>	

				<!-- Observacion del Paso 1 -->							
				<tr>
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.observacion.label"/>: </label></td>
					<td colspan="2" class="normal"><bean:write name="procesoLiqComAdapterVO" property="pasoCorrida1.observacion"/></td>
				</tr>	

				<!-- Estado del Paso 1 -->							
				<tr>
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.estado.label"/>: </label></td>
					<td colspan="2" class="normal"><bean:write name="procesoLiqComAdapterVO" property="pasoCorrida1.estadoCorrida.desEstadoCorrida"/></td>
				</tr>	
			</table>

			<logic:equal name="procesoLiqComAdapterVO" property="paramPaso" value="1">
			<!-- 	<table class="tablabotones" width="100%">
				<tr>				
			    	<td align="center">
		
						<!-- Boton Activar -->							
					<!-- 	<logic:equal name="procesoLiqComAdapterVO" property="activarEnabled" value="true">
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');" disabled='false'>
								<bean:message bundle="pro" key="pro.abm.button.activar"/>
							</html:button>&nbsp;&nbsp;
						</logic:equal>
						<logic:equal name="procesoLiqComAdapterVO" property="activarEnabled" value="false">
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');" disabled='true'>
								<bean:message bundle="pro" key="pro.abm.button.activar"/>
							</html:button>&nbsp;&nbsp;
						</logic:equal>
						
						<!-- Boton Reprogramar -->							
				<!-- 		<logic:equal name="procesoLiqComAdapterVO" property="reprogramarEnabled" value="true">
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('reprogramar', '');" disabled='false'>
								<bean:message bundle="pro" key="pro.abm.button.reprogramar"/>
							</html:button>&nbsp;&nbsp;
						</logic:equal>
						<logic:equal name="procesoLiqComAdapterVO" property="reprogramarEnabled" value="false">
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('reprogramar', '');" disabled='true'>
								<bean:message bundle="pro" key="pro.abm.button.reprogramar"/>
							</html:button>&nbsp;&nbsp;
						</logic:equal>
						
			-->
				
		    <table class="tablabotones" width="100%">
		      <tr>
				<td align="center">
				  <bean:define id="corridaVO" name="procesoLiqComAdapterVO" property="liqCom.corrida"/>
				  <%@ include file="/pro/adpProceso/includeAdpBotones.jsp" %>
				</td>
		      </tr>
		    </table>	
				
				
			</logic:equal>
		</fieldset>
		<!--FIN  1- -->		
		
		<logic:equal name="procesoLiqComAdapterVO" property="paramPaso" value="2">
			<!-- Reportes de 1  -->		
			<fieldset>
				<legend><bean:message bundle="gde" key="gde.procesoLiqComAdapter.paso1.reportes.title"/></legend>					
				<logic:notEmpty  name="procesoLiqComAdapterVO" property="listFileCorrida1">	    	
					<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
					<caption><bean:message bundle="gde" key="gde.procesoLiqComAdapter.reportesDisponibles"/></caption>
			    	<tbody>
					    	<tr>
								<th width="20">&nbsp;</th> <!-- Adjunto -->
								<th align="left"><bean:message bundle="base" key="base.nombre.label"/></th>
								<th align="left"><bean:message bundle="base" key="base.descripcion.label"/></th>						
							</tr>
						
							<!-- Adjunto -->
								<logic:iterate id="FileCorridaVO" name="procesoLiqComAdapterVO" property="listFileCorrida1">							
									<tr>
										<td>
											<logic:equal name="FileCorridaVO" property="esPlanilla" value="true">
												<a style="cursor: pointer; cursor: hand;" onclick="submitDownload('downloadFile', '<bean:write name="FileCorridaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/excel0.gif"/>
												</a>
											</logic:equal>
											<logic:equal name="FileCorridaVO" property="esPlanilla" value="false">
												<a style="cursor: pointer; cursor: hand;" onclick="submitDownload('downloadFile', '<bean:write name="FileCorridaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/pdf.gif"/>
												</a>
											</logic:equal>
										</td>
										<td><bean:write name="FileCorridaVO" property="nombre"/>&nbsp;</td>
										<td><bean:write name="FileCorridaVO" property="observacion"/>&nbsp;</td>	
									</tr>
								</logic:iterate>
					</tbody>
					</table>
				</logic:notEmpty>
				<logic:empty  name="procesoLiqComAdapterVO" property="listFileCorrida1">
					<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
						<tbody>
						    	<tr>
						    		<td><bean:message bundle="gde" key="gde.procesoLiqComAdapter.paso1.resultVacio"/></td>
						    	</tr>
					   </tbody>
					</table>									
				</logic:empty>
				
				<table class="tablabotones" width="100%">
					<tr>
						<td align="center">
							<%@ include file="/pro/adpProceso/includeLogFileButton.jsp" %>
						</td>
					</tr>
				</table>				
				
			</fieldset>
			<!-- Fin Reportes de 1  -->
		</logic:equal>		

		<!-- 2- Ejecutar Liquidacion -->		
		<logic:equal name="procesoLiqComAdapterVO" property="paramPaso" value="2">
			<fieldset>
				<legend><bean:message bundle="gde" key="gde.procesoLiqComAdapter.paso2.title"/></legend>			
				<table class="tabladatos" width="100%">
	
					<tr>
					<!-- Fecha del Paso 2 -->		
						<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.fecha.label"/>: </label></td>
						<td class="normal"><bean:write name="procesoLiqComAdapterVO" property="pasoCorrida2.fechaCorridaView"/></td>
	
					<!-- Hora del Paso 2 -->							
						<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.hora.label"/>: </label></td>
						<td class="normal"><bean:write name="procesoLiqComAdapterVO" property="pasoCorrida2.horaCorridaView"/></td>
					</tr>	
	
					<!-- Observacion del Paso 1 -->							
					<tr>
						<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.observacion.label"/>: </label></td>
						<td colspan="2" class="normal"><bean:write name="procesoLiqComAdapterVO" property="pasoCorrida2.observacion"/></td>
					</tr>	
	
					<!-- Estado del Paso 2 -->							
					<tr>
						<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.estado.label"/>: </label></td>
						<td colspan="2" class="normal"><bean:write name="procesoLiqComAdapterVO" property="pasoCorrida2.estadoCorrida.desEstadoCorrida"/></td>
					</tr>	
				</table>
		
			<table class="tablabotones" width="100%">
		      <tr>
				<td align="center">
				  <bean:define id="corridaVO" name="procesoLiqComAdapterVO" property="liqCom.corrida"/>
				  <%@ include file="/pro/adpProceso/includeAdpBotones.jsp" %>
				</td>
		      </tr>
		    </table>	
							
			</fieldset>
		</logic:equal>
		<!-- FIN 2- Ejecutar Liquidacion -->		
				
		<logic:equal name="procesoLiqComAdapterVO" property="paramPaso" value="3">
		<!-- Reportes de LiqCom PASO 2  -->		
			<fieldset>
				<legend><bean:message bundle="gde" key="gde.procesoLiqComAdapter.paso2.reportes.title"/></legend>					
				
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
					<caption><bean:message bundle="gde" key="gde.procesoLiqComAdapter.reportesDisponibles"/></caption>
			    	<tbody>
						<logic:notEmpty  name="procesoLiqComAdapterVO" property="listFileCorrida2">	    	
					    	<tr>
								<th width="20">&nbsp;</th> <!-- Adjunto -->
								<th align="left"><bean:message bundle="base" key="base.nombre.label"/></th>
								<th align="left"><bean:message bundle="base" key="base.descripcion.label"/></th>						
							</tr>
						
							<!-- Adjunto -->
								<logic:iterate id="FileCorridaVO" name="procesoLiqComAdapterVO" property="listFileCorrida2">							
									<tr>
										<td>
											<logic:equal name="FileCorridaVO" property="esPlanilla" value="true">
												<a style="cursor: pointer; cursor: hand;" onclick="submitDownload('downloadFile', '<bean:write name="FileCorridaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/excel0.gif"/>
												</a>
											</logic:equal>
											<logic:equal name="FileCorridaVO" property="esPlanilla" value="false">
												<a style="cursor: pointer; cursor: hand;" onclick="submitDownload('downloadFile', '<bean:write name="FileCorridaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/pdf.gif"/>
												</a>
											</logic:equal>
										</td>
										<td><bean:write name="FileCorridaVO" property="nombre"/>&nbsp;</td>
										<td><bean:write name="FileCorridaVO" property="observacion"/>&nbsp;</td>	
									</tr>
								</logic:iterate>
						</logic:notEmpty>
						<logic:empty  name="procesoLiqComAdapterVO" property="listFileCorrida1">
							<tr><td align="center">
							<bean:message bundle="base" key="base.noExistenRegitros"/>
							</td></tr>
						</logic:empty>					
					</tbody>
				</table>
				
			</fieldset>			
		<!-- Fin Reportes de LiqCom  -->
		</logic:equal>	

		
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
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->		
					