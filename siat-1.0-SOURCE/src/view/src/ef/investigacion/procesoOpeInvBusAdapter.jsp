<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/ef/AdministrarProcesoOpeInvBus.do">
	
		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="ef" key="ef.procesoOpeInvBusAdapter.title"/></h1>
		
		
	<!-- OpeInv -->
	<bean:define id="opeInvVO" name="procesoOpeInvBusAdapterVO" property="opeInvBus.opeInv"/>
	<%@include file="/ef/investigacion/includeOpeInvView.jsp" %>	
	<!-- OpeInv -->		
		
	
	<!-- opeInvBus -->
	<bean:define id="opeInvBusAdapterVO" name="procesoOpeInvBusAdapterVO"/>
	<%@include file="/ef/investigacion/includeOpeInvBusView.jsp" %>		
	<!-- opeInvBus -->
					
	<a name="paso1">&nbsp;</a>				
	<!-- 1- Realizar seleccion -->		
		<fieldset>
			<legend><bean:message bundle="ef" key="ef.procesoOpeInvBusAdapter.paso1.title"/></legend>			
			<table class="tabladatos" width="100%">

				<tr>
				<!-- Fecha del Paso 1 -->		
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.fecha.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoOpeInvBusAdapterVO" property="pasoCorrida1.fechaCorridaView"/></td>

				<!-- Hora del Paso 1 -->							
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.hora.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoOpeInvBusAdapterVO" property="pasoCorrida1.horaCorridaView"/></td>
				</tr>	

				<!-- Observacion del Paso 1 -->							
				<tr>
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.observacion.label"/>: </label></td>
					<td colspan="2" class="normal"><bean:write name="procesoOpeInvBusAdapterVO" property="pasoCorrida1.observacion"/></td>
				</tr>	

				<!-- Estado del Paso 1 -->							
				<tr>
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.estado.label"/>: </label></td>
					<td colspan="2" class="normal"><bean:write name="procesoOpeInvBusAdapterVO" property="pasoCorrida1.estadoCorrida.desEstadoCorrida"/></td>
				</tr>	
			</table>

			<logic:equal name="procesoOpeInvBusAdapterVO" property="paramPaso" value="1">				
			    <table class="tablabotones" width="100%">
			      <tr>
					<td align="center">
					  <bean:define id="corridaVO" name="procesoOpeInvBusAdapterVO" property="opeInvBus.corrida"/>
					  <%@ include file="/pro/adpProceso/includeAdpBotones.jsp" %>
					</td>
			      </tr>
			    </table>					
			</logic:equal>
		</fieldset>
		<!--FIN  1- -->		

		<logic:equal name="procesoOpeInvBusAdapterVO" property="paramPaso" value="2">
			<!-- Reportes de 1  -->		
			<fieldset>
				<legend><bean:message bundle="ef" key="ef.procesoOpeInvBusAdapterVO.reportes.title"/></legend>					
				<logic:notEmpty  name="procesoOpeInvBusAdapterVO" property="listFileCorrida1">	    	
					<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
					<caption><bean:message bundle="gde" key="gde.procesoLiqComAdapter.reportesDisponibles"/></caption>
			    	<tbody>
					    	<tr>
								<th width="20">&nbsp;</th> <!-- Adjunto -->
								<th align="left"><bean:message bundle="base" key="base.nombre.label"/></th>
								<th align="left"><bean:message bundle="base" key="base.descripcion.label"/></th>						
							</tr>
						
							<!-- Adjunto -->
								<logic:iterate id="FileCorridaVO" name="procesoOpeInvBusAdapterVO" property="listFileCorrida1">							
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
				<logic:empty  name="procesoOpeInvBusAdapterVO" property="listFileCorrida1">
					<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
						<tbody>
						    	<tr>
						    		<td><bean:message bundle="gde" key="gde.procesoLiqComAdapter.paso1.resultVacio"/></td>
						    	</tr>
					   </tbody>
					</table>									
				</logic:empty>
			
			<!-- 	
				<table class="tablabotones" width="100%">
					<tr>
						<td align="center">
							<%@ include file="/pro/adpProceso/includeLogFileButton.jsp" %>
						</td>
					</tr>
				</table>				
			-->	
			</fieldset>
			<!-- Fin Reportes de 1  -->
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

<script type="text/javascript">
	document.location = document.URL + '#paso1';
</script>	
	<!-- Fin Tabla que contiene todos los formularios -->		
					