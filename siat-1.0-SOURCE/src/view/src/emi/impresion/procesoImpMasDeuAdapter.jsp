<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
	<%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/emi/AdministrarProcesoImpMasDeu.do">
	
		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
	
		<!-- Errors  -->
		<html:errors bundle="base"/>
	
		<!-- Titulo -->	
		<h1><bean:message bundle="emi" key="emi.procesoImpMasDeuAdapter.title"/></h1>

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- Datos del Proceso de Impresion Masiva de Deuda -->		
		<fieldset>
			<legend><bean:message bundle="emi" key="emi.impMasDeu.title"/></legend>
			<table class="tabladatos">
				<tr>
					<!-- Recurso -->
					<td><label><bean:message bundle="emi" key="emi.impMasDeu.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoImpMasDeuAdapterVO" property="impMasDeu.recurso.desRecurso"/></td>
				</tr>

				<logic:equal name="procesoImpMasDeuAdapterVO" property="selectAtrValEnabled" value="true">
					<tr>					
						<!-- Atributo -->
						<td><label><bean:write name="procesoImpMasDeuAdapterVO" property="impMasDeu.atributo.desAtributo"/>: </label></td>
						<td class="normal"><bean:write name="procesoImpMasDeuAdapterVO" property="impMasDeu.atrValor"/></td>
					</tr>
				</logic:equal>

				<tr>
					<!-- Formato de salida -->	
					<td><label><bean:message bundle="emi" key="emi.impMasDeu.formatoSalida.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoImpMasDeuAdapterVO" property="impMasDeu.formatoSalida.value"/></td>					
				</tr>

				<tr>
					<!-- Anio -->
					<td><label><bean:message bundle="emi" key="emi.impMasDeu.anio.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoImpMasDeuAdapterVO" property="impMasDeu.anioView"/></td>
				</tr>
				<tr>
					<!-- Periodo Desde -->
					<td><label><bean:message bundle="emi" key="emi.impMasDeu.periodoDesde.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoImpMasDeuAdapterVO" property="impMasDeu.periodoDesdeView"/></td>

					<!-- Periodo Hasta -->
					<td><label><bean:message bundle="emi" key="emi.impMasDeu.periodoHasta.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoImpMasDeuAdapterVO" property="impMasDeu.periodoHastaView"/></td>
				</tr>

				<tr>
					<!-- Aplicar criterios de reparto -->
					<td><label><bean:message bundle="emi" key="emi.impMasDeu.abrirPorBroche.label"/>: </label></td>
					<td class="normal">
						<bean:write name="procesoImpMasDeuAdapterVO" property="impMasDeu.abrirPorBroche.value"/>					
					</td>
					<td class="normal" colspan="2">
						<ul class="vinieta">
							<li><bean:message bundle="emi" key="emi.impMasDeu.abrirPorBroche.description"/></li>
						</ul>
					</td>
				</tr>

				<tr>
					<!-- Estado -->
					<td><label><bean:message bundle="emi" key="emi.impMasDeu.estadoCorrida.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoImpMasDeuAdapterVO" property="impMasDeu.corrida.estadoCorrida.desEstadoCorrida"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- FIN Datos del Proceso de Impresion Masiva de Deuda -->


		<!-- Paso 1: Generar los archivos para impresion -->		
		<fieldset>
			<legend><bean:message bundle="emi" key="emi.procesoImpMasDeuAdapter.paso1.title"/></legend>			
			<table class="tabladatos" width="100%">
				
				<tr>
					<!-- Descripcion del Paso 1 -->
					<td class="normal" colspan="4" align="left"><bean:message bundle="emi" key="emi.procesoImpMasDeuAdapter.paso1.descripcion"/></td>
				</tr>

				<tr>
					<!-- Fecha del Paso 1 -->
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.fecha.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoImpMasDeuAdapterVO" property="pasoCorrida1.fechaCorridaView"/></td>

					<!-- Hora del Paso 1 -->							
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.hora.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoImpMasDeuAdapterVO" property="pasoCorrida1.horaCorridaView"/></td>
				</tr>	

				<tr>
					<!-- Observacion del Paso 1 -->
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.observacion.label"/>: </label></td>
					<td colspan="2" class="normal"><bean:write name="procesoImpMasDeuAdapterVO" property="pasoCorrida1.observacion"/></td>
				</tr>	

				<tr>
					<!-- Estado del Paso 1 -->
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.estado.label"/>: </label></td>
					<td colspan="2" class="normal"><bean:write name="procesoImpMasDeuAdapterVO" property="pasoCorrida1.estadoCorrida.desEstadoCorrida"/></td>
				</tr>	
	
			</table>

			<!-- Botonera -->
			<logic:equal name="procesoImpMasDeuAdapterVO" property="paramPaso" value="1">
				<table class="tablabotones" width="100%">
				<tr>				
			    	<td align="center">
						<bean:define id="procesoEmisionAdapterVO" name="procesoImpMasDeuAdapterVO" />
						<%@ include file="/emi/emision/includeBotonesProcesoEmision.jsp" %>
					</td>
				 </tr>
				</table>
			</logic:equal>
		</fieldset>
		<!-- FIN Paso 1: Generar los archivos para impresion -->

			<fieldset>
				<legend><bean:message bundle="emi" key="emi.procesoProPasDebAdapter.paso1.reportes.title"/></legend>					
				<table class="tramonline" border="0" cellspacing="1" width="100%">            
					<caption><bean:message bundle="emi" key="emi.procesoProPasDebAdapter.paso1.reportes.legend"/></caption>
			    	<tbody>
						<logic:notEmpty  name="procesoImpMasDeuAdapterVO" property="listFileCorrida1">	    	
					    	<tr>
								<th width="1">&nbsp;</th> <!-- Adjunto -->
								<th align="left"><bean:message bundle="base" key="base.nombre.label"/></th>
								<th align="left"><bean:message bundle="base" key="base.descripcion.label"/></th>						
							</tr>
							<logic:iterate id="FileCorridaVO" name="procesoImpMasDeuAdapterVO" property="listFileCorrida1">
								<tr>
									<!-- Adjunto -->
									<td>
										<a style="cursor: pointer; cursor: hand;" onclick="submitDownload('downloadFile', '<bean:write name="FileCorridaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/imprimir0.gif"/>
										</a>
									</td>
									<td><bean:write name="FileCorridaVO" property="nombre"/>&nbsp;</td>
									<td><bean:write name="FileCorridaVO" property="observacion"/>&nbsp;</td>
								</tr>
							</logic:iterate>
						</logic:notEmpty>
						<logic:empty  name="procesoImpMasDeuAdapterVO" property="listFileCorrida1">
							<tr><td align="center">
							<bean:message bundle="base" key="base.noExistenRegitros"/>
							</td></tr>
						</logic:empty>					
					</tbody>
				</table>
				<!-- Envio de Archivos Generados en el Paso -->
				<table class="tablabotones" width="100%" >
					<tr>				
						<td align="center">
							<bean:define id="pasoCorridaVO" name="procesoImpMasDeuAdapterVO" property="pasoCorrida1" />
							<%@ include file="/pro/adpProceso/includeEnvioArchivos.jsp" %>
						</td>
					</tr>
				</table>
			</fieldset>

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
		<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->		
					