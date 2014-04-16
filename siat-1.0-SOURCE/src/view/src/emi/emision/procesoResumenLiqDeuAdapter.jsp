<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/emi/AdministrarProcesoResumenLiqDeu.do">
	
		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="emi" key="emi.procesoEmisionResumenLiqDeuAdapter.title"/></h1>

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- Datos de la Emision de Resumen Liq Deuda -->		
		<fieldset>
			<legend><bean:message bundle="emi" key="emi.emision.title"/></legend>			
			<table class="tabladatos" width="100%">	
				<!-- Recurso -->
				<tr>
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoResumenLiqDeuAdapterVO" property="emision.recurso.desRecurso"/></td>
				<!-- Fecha Emision -->
					<td><label><bean:message bundle="emi" key="emi.emision.fechaEmision.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoResumenLiqDeuAdapterVO" property="emision.fechaEmisionView"/></td>
				</tr>
				<!-- Fecha Vencimiento -->
				<tr>	
					<td><label><bean:message bundle="gde" key="gde.deuda.fechaVencimiento.label"/> </label></td>
					<td class="normal"><bean:write name="procesoResumenLiqDeuAdapterVO" property="fechaVencimientoView"/></td>
				</tr>
			</table>
	
			<table class="tablabotones" width="100%">
				<tr>				
					<td align="right">
						<bean:define id="modificarEncabezadoEnabled" name="procesoResumenLiqDeuAdapterVO" property="modificarEncabezadoEnabled"/>
						<logic:equal name="procesoResumenLiqDeuAdapterVO" property="modificarEncEmiEnabled" value="true">
							<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificar', 
								'<bean:write name="procesoResumenLiqDeuAdapterVO" property="emision.id" bundle="base" formatKey="general.format.id"/>');" 
								value="<bean:message bundle="base" key="abm.button.modificar"/>"/>
						</logic:equal>
						<logic:equal name="procesoResumenLiqDeuAdapterVO" property="modificarEncEmiEnabled" value="false">
							<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificar', 
								'<bean:write name="procesoResumenLiqDeuAdapterVO" property="emision.id" bundle="base" formatKey="general.format.id"/>');" 
								value="<bean:message bundle="base" key="abm.button.modificar"/>" disabled="true"/>
						</logic:equal>
					</td>
				</tr>
			</table>
	
		</fieldset>
		<!-- FIN Datos de la Emision de Resumen Liq Deuda -->		
		
		<!-- 1-Generar Archivos -->		
		<fieldset>
			<legend><bean:message bundle="emi" key="emi.procesoEmisionResumenLiqDeuAdapter.paso1.title"/></legend>			

			<table class="tabladatos" width="100%">
				<!-- Descripcion del Paso 1 -->		
				<tr>
					<td class="normal" colspan="4" align="left"><bean:message bundle="emi" key="emi.procesoEmisionResumenLiqDeuAdapter.paso1.descripcion"/></td>
				</tr>
			</table>
   			
   			<!-- Fecha Inicio, Hora Inicio, Estado Corrida y Observacion -->
   			<bean:define id="idCorrida" name="procesoResumenLiqDeuAdapterVO" property="emision.corrida.idView"/>
			<jsp:include page="/pro/AdministrarAdpCorrida.do">
   				<jsp:param name="method" value="estadoPaso" />
   				<jsp:param name="paso" value="1" />
   				<jsp:param name="id" value="<%= idCorrida %>" />
			</jsp:include>

			<logic:equal name="procesoResumenLiqDeuAdapterVO" property="paramPaso" value="1">
				<table class="tablabotones" width="100%">
				<tr>				
			    	<td align="center">
						<bean:define id="corridaVO" name="procesoResumenLiqDeuAdapterVO" property="emision.corrida" />
			    		<%@ include file="/pro/adpProceso/includeAdpBotones.jsp" %>
					</td>
				 </tr>
				</table>
			</logic:equal>
		</fieldset>
		<!--FIN  1-Generar Archivos -->		
		
		<!-- Archivos Generados  -->		
		<fieldset>
			<legend><bean:message bundle="emi" key="emi.procesoEmisionResumenLiqDeuAdapter.paso1.reportes.title"/></legend>					
			<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="emi" key="emi.procesoEmisionResumenLiqDeuAdapter.reportesDisponibles"/></caption>
	    	<tbody>
				<logic:notEmpty  name="procesoResumenLiqDeuAdapterVO" property="listFileCorrida1">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Adjunto -->
						<th align="left"><bean:message bundle="base" key="base.nombre.label"/></th>
						<th align="left"><bean:message bundle="base" key="base.descripcion.label"/></th>						
					</tr>
					<logic:iterate id="FileCorridaVO" name="procesoResumenLiqDeuAdapterVO" property="listFileCorrida1">
						<tr>
							<!-- Adjunto -->
							<td>
								<logic:equal name="FileCorridaVO" property="esPlanilla" value="true">
									<a style="cursor: pointer; cursor: hand;" onclick="submitDownload('downloadFile', '<bean:write name="FileCorridaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/excel0.gif"/>
									</a>
								</logic:equal>
								<logic:equal name="FileCorridaVO" property="noTieneExtension" value="true">
									<a style="cursor: pointer; cursor: hand;" onclick="submitDownload('downloadFile', '<bean:write name="FileCorridaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/verArchivo0.gif"/>
									</a>
								</logic:equal>
							</td>
							<td><bean:write name="FileCorridaVO" property="nombre"/>&nbsp;</td>
							<td><bean:write name="FileCorridaVO" property="observacion"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="procesoResumenLiqDeuAdapterVO" property="listFileCorrida1">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			</tbody>
			</table>
		</fieldset>
		<!-- Archivos Generados  -->	

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
		<input type="hidden" name="idTipoEmision" value="<bean:write name='procesoResumenLiqDeuAdapterVO' property='emision.tipoEmision.idView'/>"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->							