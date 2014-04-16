<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
	<%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/emi/AdministrarProcesoEmisionCorCdM.do">
	
		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
	
		<!-- Errors  -->
		<html:errors bundle="base"/>
	
		<!-- Titulo -->	
		<h1><bean:message bundle="emi" key="emi.procesoEmisionCorCdMAdapter.title"/></h1>

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- Datos de la Emision Corregida de CdM -->		
		<fieldset>
			<legend><bean:message bundle="emi" key="emi.emision.title"/></legend>			
			<table class="tabladatos" width="100%">	
		
				<tr>
					<!-- Recurso -->
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoEmisionCorCdMAdapterVO" property="emision.recurso.desRecurso"/></td>

					<!-- Fecha Emision -->
					<td><label><bean:message bundle="emi" key="emi.emision.fechaEmision.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoEmisionCorCdMAdapterVO" property="emision.fechaEmisionView"/></td>
				</tr>

				<tr>
					<!-- Obra -->
					<td><label><bean:message bundle="rec" key="rec.obra.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoEmisionCorCdMAdapterVO" property="obra.desObra"/></td>
				</tr>
				
			</table>	
	
		</fieldset>
		<!-- FIN Datos de la Emision Corregida de CdM -->		
		
		<!-- 1- Simular Emision Corregida -->		
		<fieldset>
			<legend><bean:message bundle="emi" key="emi.procesoEmisionCorCdMAdapter.paso1.title"/></legend>			
			<table class="tabladatos" width="100%">
				
				<tr>
					<!-- Descripcion del Paso 1 -->
					<td class="normal" colspan="4" align="left"><bean:message bundle="emi" key="emi.procesoEmisionCorCdMAdapter.paso1.descripcion"/></td>
				</tr>

				<tr>
					<!-- Fecha del Paso 1 -->		
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.fecha.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoEmisionCorCdMAdapterVO" property="pasoCorrida1.fechaCorridaView"/></td>

					<!-- Hora del Paso 1 -->							
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.hora.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoEmisionCorCdMAdapterVO" property="pasoCorrida1.horaCorridaView"/></td>
				</tr>	

				<tr>
					<!-- Observacion del Paso 1 -->
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.observacion.label"/>: </label></td>
					<td colspan="2" class="normal"><bean:write name="procesoEmisionCorCdMAdapterVO" property="pasoCorrida1.observacion"/></td>
				</tr>	

				<tr>
					<!-- Estado del Paso 1 -->
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.estado.label"/>: </label></td>
					<td colspan="2" class="normal"><bean:write name="procesoEmisionCorCdMAdapterVO" property="pasoCorrida1.estadoCorrida.desEstadoCorrida"/></td>
				</tr>	
	
			</table>

			<!-- Botonera -->
			<logic:equal name="procesoEmisionCorCdMAdapterVO" property="paramPaso" value="1">
				<table class="tablabotones" width="100%">
				<tr>				
			    	<td align="center">
						<bean:define id="procesoEmisionAdapterVO" name="procesoEmisionCorCdMAdapterVO" />
						<%@ include file="/emi/emision/includeBotonesProcesoEmision.jsp" %>
					</td>
				 </tr>
				</table>
			</logic:equal>
		</fieldset>
		<!-- FIN 1- Simular Emision Corregida -->	
		
		<!-- Reportes Paso 1 -->
		<logic:equal name="procesoEmisionCorCdMAdapterVO" property="verReportesPaso1" value="true">
			
			<fieldset>
				<legend><bean:message bundle="emi" key="emi.procesoEmisionCorCdMAdapter.paso1.reportes.title"/></legend>					
				<table class="tramonline" border="0" cellspacing="1" width="100%">            
					<caption><bean:message bundle="emi" key="emi.procesoEmisionCorCdMAdapter.reportesDisponibles"/></caption>
			    	<tbody>
						<logic:notEmpty  name="procesoEmisionCorCdMAdapterVO" property="listFileCorrida1">	    	
					    	<tr>
								<th width="1">&nbsp;</th> <!-- Adjunto -->
								<th align="left"><bean:message bundle="base" key="base.nombre.label"/></th>
								<th align="left"><bean:message bundle="base" key="base.descripcion.label"/></th>						
							</tr>
							<logic:iterate id="FileCorridaVO" name="procesoEmisionCorCdMAdapterVO" property="listFileCorrida1">
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
						<logic:empty  name="procesoEmisionCorCdMAdapterVO" property="listFileCorrida1">
							<tr><td align="center">
							<bean:message bundle="base" key="base.noExistenRegitros"/>
							</td></tr>
						</logic:empty>					
					</tbody>
				</table>
				
				<table class="tablabotones" width="100%">
					<tr>
						<td align="center">
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('consultarAuxDeuda', '');">
								<bean:message bundle="emi" key="emi.procesoEmisionCorCdMAdapter.button.consultarAuxDeu"/>
							</html:button>
						</td>
					</tr>
				</table>				
			</fieldset>
		</logic:equal>
		<!-- FIN Reportes Paso 1 -->
	
		<!-- 2- Generar Cuentas CdM -->		
		<fieldset>
			<legend><bean:message bundle="emi" key="emi.procesoEmisionCorCdMAdapter.paso2.title"/></legend>			
			<table class="tabladatos" width="100%">
	
				<tr>
					<!-- Descripcion del Paso 2 -->
					<td class="normal" colspan="4" align="left"><bean:message bundle="emi" key="emi.procesoEmisionCorCdMAdapter.paso2.descripcion"/></td>
				</tr>
	
				<tr>
					<!-- Fecha del Paso 2 -->		
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.fecha.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoEmisionCorCdMAdapterVO" property="pasoCorrida2.fechaCorridaView"/></td>
	
					<!-- Hora del Paso 2 -->							
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.hora.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoEmisionCorCdMAdapterVO" property="pasoCorrida2.horaCorridaView"/></td>
				</tr>	
	
				<tr>
					<!-- Observacion del Paso 1 -->
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.observacion.label"/>: </label></td>
					<td colspan="2" class="normal"><bean:write name="procesoEmisionCorCdMAdapterVO" property="pasoCorrida2.observacion"/></td>
				</tr>	
	
				<tr>
					<!-- Estado del Paso 2 -->
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.estado.label"/>: </label></td>
					<td colspan="2" class="normal"><bean:write name="procesoEmisionCorCdMAdapterVO" property="pasoCorrida2.estadoCorrida.desEstadoCorrida"/></td>
				</tr>	
			</table>

			<!-- Botonera -->
			<logic:equal name="procesoEmisionCorCdMAdapterVO" property="paramPaso" value="2">
				<table class="tablabotones" width="100%">
				<tr>				
			    	<td align="center">
						<bean:define id="procesoEmisionAdapterVO" name="procesoEmisionCorCdMAdapterVO" />
						<%@ include file="/emi/emision/includeBotonesProcesoEmision.jsp" %>
					</td>
				 </tr>
				</table>
			</logic:equal>
		</fieldset>
		<!-- FIN 2- Generar Cuentas CdM -->

		<!-- Reportes Paso 2 -->
		<logic:equal name="procesoEmisionCorCdMAdapterVO" property="verReportesPaso2" value="true">
			
			<fieldset>
				<legend><bean:message bundle="emi" key="emi.procesoEmisionCorCdMAdapter.paso2.reportes.title"/></legend>					
				<table class="tramonline" border="0" cellspacing="1" width="100%">            
					<caption><bean:message bundle="emi" key="emi.procesoEmisionCorCdMAdapter.reportesDisponibles"/></caption>
			    	<tbody>
						<logic:notEmpty  name="procesoEmisionCorCdMAdapterVO" property="listFileCorrida2">	    	
					    	<tr>
								<th width="1">&nbsp;</th> <!-- Adjunto -->
								<th align="left"><bean:message bundle="base" key="base.nombre.label"/></th>
								<th align="left"><bean:message bundle="base" key="base.descripcion.label"/></th>						
							</tr>
							<logic:iterate id="FileCorridaVO" name="procesoEmisionCorCdMAdapterVO" property="listFileCorrida2">
								<tr>
									<!-- Adjunto -->
									<td>
										<logic:equal name="FileCorridaVO" property="esPdf" value="true">
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
						<logic:empty  name="procesoEmisionCorCdMAdapterVO" property="listFileCorrida2">
							<tr><td align="center">
							<bean:message bundle="base" key="base.noExistenRegitros"/>
							</td></tr>
						</logic:empty>					
					</tbody>
				</table>
			</fieldset>
		</logic:equal>
		<!-- FIN Reportes Paso 2 -->
		
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
					