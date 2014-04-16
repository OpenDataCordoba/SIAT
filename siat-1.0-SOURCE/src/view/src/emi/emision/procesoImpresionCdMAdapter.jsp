<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/emi/AdministrarProcesoImpresionCdM.do">
	
		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="emi" key="emi.procesoImpresionCdMAdapter.title"/></h1>

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- Datos de la Impresion de CDM -->		
		<fieldset>
			<legend><bean:message bundle="emi" key="emi.impresionCdMEditAdapter.title"/></legend>			
			<table class="tabladatos" width="100%">	
		
				<!-- Recurso -->
				<tr>
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoImpresionCdMAdapterVO" property="emision.recurso.desRecurso"/></td>

				<!-- Fecha Emision -->

					<td><label><bean:message bundle="emi" key="emi.emision.fechaEmision.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoImpresionCdMAdapterVO" property="emision.fechaEmisionView"/></td>
				</tr>

				<!-- Obra -->
				<tr>
					<td><label><bean:message bundle="emi" key="emi.impresionCdMEditAdapter.obras.label"/>: </label></td>
					<td class="normal" colspan="2"><bean:write name="procesoImpresionCdMAdapterVO" property="obra.desObra"/></td>
				</tr>

				<!-- Estado del Proceso -->
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoImpresionCdMAdapterVO" 
						property="emision.corrida.estadoCorrida.desEstadoCorrida"/></td>
				</tr>
			</table>	
		</fieldset>
		<!-- FIN Datos de la Impresion de CDM -->		
		
		<!-- 1-Generar Reportes -->		
		<fieldset>
			<legend><bean:message bundle="emi" key="emi.procesoImpresionCdMAdapter.paso1.title"/></legend>			
			<table class="tabladatos" width="100%">

				<!-- Descripcion del Paso 1 -->		
				<tr>
					<td class="normal" colspan="4" align="left"><bean:message bundle="emi" key="emi.procesoImpresionCdMAdapter.paso1.descripcion"/></td>
				</tr>

				<tr>
				<!-- Fecha del Paso 1 -->		
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.fecha.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoImpresionCdMAdapterVO" property="pasoCorrida1.fechaCorridaView"/></td>

				<!-- Hora del Paso 1 -->							
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.hora.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoImpresionCdMAdapterVO" property="pasoCorrida1.horaCorridaView"/></td>
				</tr>	

				<!-- Observacion del Paso 1 -->							
				<tr>
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.observacion.label"/>: </label></td>
					<td colspan="2" class="normal"><bean:write name="procesoImpresionCdMAdapterVO" property="pasoCorrida1.observacion"/></td>
				</tr>	

				<!-- Estado del Paso 1 -->							
				<tr>
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.estado.label"/>: </label></td>
					<td colspan="2" class="normal"><bean:write name="procesoImpresionCdMAdapterVO" property="pasoCorrida1.estadoCorrida.desEstadoCorrida"/></td>
				</tr>	
			</table>

			<!-- Botonera -->
			<logic:equal name="procesoImpresionCdMAdapterVO" property="paramPaso" value="1">
				<table class="tablabotones" width="100%">
				<tr>				
			    	<td align="center">
						<bean:define id="procesoEmisionAdapterVO" name="procesoImpresionCdMAdapterVO" />
						<%@ include file="/emi/emision/includeBotonesProcesoEmision.jsp" %>
					</td>
				 </tr>
				</table>
			</logic:equal>

		</fieldset>
		<!--FIN 1-Generar PDF -->

		<!-- Lista con Reportes generados en Paso 1  -->		
		<logic:equal name="procesoImpresionCdMAdapterVO" property="verReportesPaso1" value="true">
			<fieldset>
				<legend><bean:message bundle="emi" key="emi.procesoImpresionCdMAdapter.paso1.reportes.title"/></legend>					
				<table class="tramonline" border="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="emi" key="emi.procesoImpresionCdMAdapter.reportesDisponibles"/></caption>
			   	<tbody>
					<logic:notEmpty  name="procesoImpresionCdMAdapterVO" property="listFileCorrida1">	    	
				    	<tr>
							<th width="1">&nbsp;</th> <!-- Adjunto -->
							<th align="left"><bean:message bundle="base" key="base.nombre.label"/></th>
							<th align="left"><bean:message bundle="base" key="base.descripcion.label"/></th>						
						</tr>
						<logic:iterate id="FileCorridaVO" name="procesoImpresionCdMAdapterVO" property="listFileCorrida1">
							<tr>
								<!-- Adjunto -->
								<td>
									<logic:equal name="FileCorridaVO" property="esPdf" value="true">
										<a style="cursor: pointer; cursor: hand;" onclick="submitDownload('downloadFile', '<bean:write name="FileCorridaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/imprimir0.gif"/>
										</a>
									</logic:equal>
		
									<logic:equal name="FileCorridaVO" property="esTxt" value="true">
										<a style="cursor: pointer; cursor: hand;" onclick="submitDownload('downloadFile', '<bean:write name="FileCorridaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/imprimir0.gif"/>
										</a>
									</logic:equal>
								</td>
	
								<td><bean:write name="FileCorridaVO" property="nombre"/>&nbsp;</td>
								<td><bean:write name="FileCorridaVO" property="observacion"/>&nbsp;</td>
							</tr>
						</logic:iterate>
					</logic:notEmpty>
					<logic:empty  name="procesoImpresionCdMAdapterVO" property="listFileCorrida1">
						<tr><td align="center">
							<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
					</logic:empty>					
		
				</tbody>
			</table>
		</fieldset>
	</logic:equal>
	
	<!-- 2-Marcar Deuda -->		
	<a name="Paso2">&nbsp;</a>
	
	<fieldset>
		<legend><bean:message bundle="emi" key="emi.procesoImpresionCdMAdapter.paso2.title"/></legend>			

		<table class="tabladatos" width="100%">

				<!-- Descripcion del Paso 2 -->		
				<tr>
					<td class="normal" colspan="4" align="left"><bean:message bundle="emi" key="emi.procesoImpresionCdMAdapter.paso2.descripcion"/></td>
				</tr>

				<tr>
				<!-- Fecha del Paso 2 -->		
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.fecha.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoImpresionCdMAdapterVO" property="pasoCorrida2.fechaCorridaView"/></td>

				<!-- Hora del Paso  2 -->							
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.hora.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoImpresionCdMAdapterVO" property="pasoCorrida2.horaCorridaView"/></td>
				</tr>	

				<!-- Observacion del Paso 2 -->							
				<tr>
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.observacion.label"/>: </label></td>
					<td colspan="2" class="normal"><bean:write name="procesoImpresionCdMAdapterVO" property="pasoCorrida2.observacion"/></td>
				</tr>	

				<!-- Estado del Paso 2 -->							
				<tr>
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.estado.label"/>: </label></td>
					<td colspan="2" class="normal"><bean:write name="procesoImpresionCdMAdapterVO" property="pasoCorrida2.estadoCorrida.desEstadoCorrida"/></td>
				</tr>	
			</table>

			<!-- Botonera -->
			<logic:equal name="procesoImpresionCdMAdapterVO" property="paramPaso" value="2">
				<table class="tablabotones" width="100%">
				<tr>				
			    	<td align="center">
						<bean:define id="procesoEmisionAdapterVO" name="procesoImpresionCdMAdapterVO" />
						<%@ include file="/emi/emision/includeBotonesProcesoEmision.jsp" %>
					</td>
				 </tr>
				</table>
			</logic:equal>

	</fieldset>
	<!--FIN 2-Marcar Deuda -->
		
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
		<input type="hidden" name="idTipoEmision" value="<bean:write name="procesoImpresionCdMAdapterVO" property="emision.tipoEmision.id" bundle="base" formatKey="general.format.id"/>"/>

	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->


	<script type="text/javascript">
		function irAPaso() {
	   		document.location = document.URL + '#Paso' + "<bean:write name="procesoImpresionCdMAdapterVO" property="paramPaso"/>";
	   	}
	
		irAPaso();
	</script>

	



