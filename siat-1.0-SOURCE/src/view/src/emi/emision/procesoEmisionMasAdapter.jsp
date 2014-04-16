<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/emi/AdministrarProcesoEmisionMas.do">
	
		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="emi" key="emi.procesoEmisionMasAdapter.title"/></h1>

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<fieldset>
			<legend><bean:message bundle="emi" key="emi.emision.title"/></legend>			
			<table class="tabladatos" width="100%">	
				<tr>
					<!-- Recurso -->
					<td><label><bean:message bundle="emi" key="emi.emision.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoEmisionMasAdapterVO" property="emision.recurso.desRecurso"/></td>
				</tr>

				<logic:equal name="procesoEmisionMasAdapterVO" property="selectAtrValEnabled" value="true">
					<tr>					
						<!-- Atributo -->
						<td><label><bean:write name="procesoEmisionMasAdapterVO" property="emision.atributo.desAtributo"/>: </label></td>
						<td class="normal"><bean:write name="procesoEmisionMasAdapterVO" property="emision.valor"/></td>
					</tr>
				</logic:equal>

				<tr>
					<!-- Anio -->
					<td><label><bean:message bundle="emi" key="emi.emision.anio.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoEmisionMasAdapterVO" property="emision.anioView"/></td>
				</tr>

				<tr>
					<!-- Periodo Desde -->
					<td><label><bean:message bundle="emi" key="emi.emision.periodoDesde.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoEmisionMasAdapterVO" property="emision.periodoDesdeView"/></td>

					<!-- Periodo Hasta -->
					<td><label><bean:message bundle="emi" key="emi.emision.periodoHasta.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoEmisionMasAdapterVO" property="emision.periodoHastaView"/></td>
				</tr>

				<tr>
					<!-- Estado del Proceso-->
					<td><label><bean:message bundle="emi" key="emi.emision.estadoCorrida.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoEmisionMasAdapterVO" property="emision.corrida.estadoCorrida.desEstadoCorrida"/></td>
				</tr>
			</table>	
		</fieldset>
		<!-- FIN Datos de la Emision de TGI -->		
		
		<!-- 1-Generar Deuda Auxiliar -->		
		<fieldset>
			<legend><bean:message bundle="emi" key="emi.procesoEmisionMasAdapter.paso1.title"/></legend>			

			<table class="tabladatos" width="100%">
				<!-- Descripcion del Paso 1 -->		
				<tr>
					<td class="normal" colspan="4" align="left"><bean:message bundle="emi" key="emi.procesoEmisionMasAdapter.paso1.descripcion"/></td>
				</tr>
			</table>
   			
   			<!-- Fecha Inicio, Hora Inicio, Estado Corrida y Observacion -->
   			<bean:define id="idCorrida" name="procesoEmisionMasAdapterVO" property="emision.corrida.idView"/>
			<jsp:include page="/pro/AdministrarAdpCorrida.do">
   				<jsp:param name="method" value="estadoPaso" />
   				<jsp:param name="paso" value="1" />
   				<jsp:param name="id" value="<%= idCorrida %>" />
			</jsp:include>

			<logic:equal name="procesoEmisionMasAdapterVO" property="paramPaso" value="1">
				<table class="tablabotones" width="100%">
				<tr>				
			    	<td align="center">
						<bean:define id="procesoEmisionAdapterVO" name="procesoEmisionMasAdapterVO"/>
			    		<%@ include file="/emi/emision/includeBotonesProcesoEmision.jsp" %>
					</td>
				 </tr>
				</table>
			</logic:equal>

			<logic:equal name="procesoEmisionMasAdapterVO" property="paramPaso" value="2">
				<!-- Consultar AuxDeuda generadas  -->		
				<table class="tablabotones" width="100%">
					<tr>
						<td align="center">
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('consultarAuxDeuda', '');">
								<bean:message bundle="emi" key="emi.procesoEmisionMasAdapter.button.consultarAuxDeu"/>
							</html:button>
						</td>
					</tr>
				</table>				
			</logic:equal>	
			<!-- Fin Consultar AuxDeuda generadas  -->
			<fieldset>
				<legend>Reportes</legend>					
				<table class="tramonline" border="0" cellspacing="1" width="100%">            
					<caption>Deuda para control</caption>
			    	<tbody>
						<logic:notEmpty  name="procesoEmisionMasAdapterVO" property="listFileCorrida1">	    	
					    	<tr>
								<th width="1">&nbsp;</th> <!-- Adjunto -->
								<th align="left"><bean:message bundle="base" key="base.nombre.label"/></th>
								<th align="left"><bean:message bundle="base" key="base.descripcion.label"/></th>						
							</tr>
							<logic:iterate id="FileCorridaVO" name="procesoEmisionMasAdapterVO" property="listFileCorrida1">
								<tr>
									<!-- Adjunto -->
									<td>
										<a style="cursor: pointer; cursor: hand;" onclick="submitDownload('downloadFile', '<bean:write name="FileCorridaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/excel0.gif"/>
										</a>
									</td>
									<td><bean:write name="FileCorridaVO" property="nombre"/>&nbsp;</td>
									<td><bean:write name="FileCorridaVO" property="observacion"/>&nbsp;</td>
								</tr>
							</logic:iterate>
						</logic:notEmpty>
						<logic:empty  name="procesoEmisionMasAdapterVO" property="listFileCorrida1">
							<tr><td align="center">
							<bean:message bundle="base" key="base.noExistenRegitros"/>
							</td></tr>
						</logic:empty>					
					</tbody>
				</table>
			</fieldset>
			<!-- FIN Archivos de Salida  -->
			

		</fieldset>
		<!--FIN 1-Generar Deuda Auxiliar -->		
		
		<!-- 2- Generar DeudaAdmin -->		
		<fieldset>
			<legend><bean:message bundle="emi" key="emi.procesoEmisionMasAdapter.paso2.title"/></legend>			
			<table class="tabladatos" width="100%">

				<!-- Descripcion del Paso 2 -->		
				<tr>
					<td class="normal" colspan="4" align="left"><bean:message bundle="emi" key="emi.procesoEmisionMasAdapter.paso2.descripcion"/></td>
				</tr>
			</table>
			<!-- Fecha Inicio, Hora Inicio, Estado Corrida y Observacion -->
   			<bean:define id="idCorrida" name="procesoEmisionMasAdapterVO" property="emision.corrida.idView"/>
			<jsp:include page="/pro/AdministrarAdpCorrida.do">
   				<jsp:param name="method" value="estadoPaso" />
   				<jsp:param name="paso" value="2" />
   				<jsp:param name="id" value="<%= idCorrida %>" />
			</jsp:include>

			<logic:equal name="procesoEmisionMasAdapterVO" property="paramPaso" value="2">
				<table class="tablabotones" width="100%">
					<tr>				
			    		<td align="center">
							<bean:define id="procesoEmisionAdapterVO" name="procesoEmisionMasAdapterVO"/>
			    			<%@ include file="/emi/emision/includeBotonesProcesoEmision.jsp" %>
						</td>
				  	</tr>
				</table>
			</logic:equal>
		</fieldset>
		<!-- FIN 2- Generar DeudaAdmin -->		

		<fieldset>
			<legend>Reportes</legend>					
			<table class="tramonline" border="0" cellspacing="1" width="100%">            
				<caption>Reportes de Totales</caption>
		    	<tbody>
					<logic:notEmpty  name="procesoEmisionMasAdapterVO" property="listFileCorrida2">	    	
				    	<tr>
							<th width="1">&nbsp;</th> <!-- Adjunto -->
							<th align="left"><bean:message bundle="base" key="base.nombre.label"/></th>
							<th align="left"><bean:message bundle="base" key="base.descripcion.label"/></th>						
						</tr>
						<logic:iterate id="FileCorridaVO" name="procesoEmisionMasAdapterVO" property="listFileCorrida2">
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
					<logic:empty  name="procesoEmisionMasAdapterVO" property="listFileCorrida2">
						<tr><td align="center">
						<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
					</logic:empty>					
				</tbody>
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
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->							