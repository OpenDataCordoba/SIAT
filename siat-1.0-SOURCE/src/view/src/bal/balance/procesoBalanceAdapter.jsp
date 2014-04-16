<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/AdministrarProcesoBalance.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.procesarBalanceAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		<!-- Datos del Balance -->		
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.balance.title"/></legend>			
			<table class="tabladatos" width="100%">
				<!-- Fecha Balance -->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.balance.fechaBalance.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoBalanceAdapterVO" property="balance.fechaBalanceView"/></td>
				</tr>		
				<!-- Ejercicio -->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.ejercicio.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoBalanceAdapterVO" property="balance.ejercicio.desEjercicio"/></td>
				</tr>	
				<!-- Fecha Desde y Hasta -->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.balance.fechaDesde.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoBalanceAdapterVO" property="balance.fechaDesdeView"/></td>
					<td><label><bean:message bundle="bal" key="bal.balance.fechaHasta.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoBalanceAdapterVO" property="balance.fechaHastaView"/></td>
				</tr>			
				<!-- Observacion-->		
				<tr>
					<td><label><bean:message bundle="bal" key="bal.balance.observacion.label"/>: </label></td>
					<td colspan="3" class="normal"><bean:write name="procesoBalanceAdapterVO" property="balance.observacion"/></td>					
				</tr>
				<!-- Estado Corrida-->		
				<tr>
					<td><label><bean:message bundle="bal" key="bal.balance.estadoProceso.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoBalanceAdapterVO" property="balance.corrida.estadoCorrida.desEstadoCorrida"/></td>					
				</tr>
			</table>
			<table class="tablabotones" width="100%">
			<tr>				
				<td align="right">
					<bean:define id="modificarEncabezadoEnabled" name="procesoBalanceAdapterVO" property="modificarEncabezadoEnabled"/>
					<logic:equal name="procesoBalanceAdapterVO" property="paramModificar" value="true">
						<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificar', 
							'<bean:write name="procesoBalanceAdapterVO" property="balance.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.modificar"/>"/>
					</logic:equal>
					<logic:equal name="procesoBalanceAdapterVO" property="paramModificar" value="false">
						<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificar', 
							'<bean:write name="procesoBalanceAdapterVO" property="balance.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.modificar"/>" disabled="true"/>
					</logic:equal>
				</td>
			</tr>
			</table>
		</fieldset>
		<!-- Fin Datos del Balance -->		
		
		<!-- 1- Preparacion del Balance -->		
		<logic:equal name="procesoBalanceAdapterVO" property="paramPaso" value="1">
			<a name="pasoActual">&nbsp;</a> 
		</logic:equal>
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.procesarBalanceAdapter.fieldset1"/></legend>			
						
			<table class="tabladatos" width="100%">
				<tr>
					<td class="normal" colspan="4" align="left"><bean:message bundle="bal" key="bal.procesoBalanceAdapter.paso1.description"/></td>
				</tr>
				<tr><td>&nbsp;&nbsp;</td></tr> 
				<tr><td>&nbsp;&nbsp;</td></tr> 
			</table>

		<!-- Seleccionar Folios  -->
		<div id="foliosSeleccionados">
					<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
						<caption><bean:message bundle="bal" key="bal.balance.listFolio"/></caption>
	                	<tbody>
		                	<tr>
								<th width="1">&nbsp;</th> <!-- Ver -->
								<th width="1">&nbsp;</th> <!-- Eliminar -->
							  	<th align="left"><bean:message bundle="bal" key="bal.folio.fechaFolio.label"/></th>
							  	<th align="left"><bean:message bundle="bal" key="bal.folio.numero.label"/></th>
							  	<th align="left"><bean:message bundle="bal" key="bal.folio.descripcion.label"/></th>
							</tr>							
						<logic:notEmpty  name="procesoBalanceAdapterVO" property="balance.listFolio">	
							<logic:iterate id="FolioVO" name="procesoBalanceAdapterVO" property="balance.listFolio">
								<tr>						
									<!-- Ver-->
									<td>
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verFolio', '<bean:write name="FolioVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</td>		
									<td>
										<!-- Excluir-->								
										<logic:equal name="procesoBalanceAdapterVO" property="paramPaso" value="1">
											<logic:equal name="procesoBalanceAdapterVO" property="excluirFolioEnabled" value="enabled">		
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('excluirFolio', '<bean:write name="FolioVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="bal" key="bal.procesoBalanceAdapter.adm.button.excluir"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>
											</logic:equal>							
											<logic:notEqual name="procesoBalanceAdapterVO" property="excluirFolioEnabled" value="enabled">		
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
											</logic:notEqual>
										</logic:equal>						
										<logic:notEqual name="procesoBalanceAdapterVO" property="paramPaso" value="1">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>																
									</td>
							  	    <td><bean:write name="FolioVO" property="fechaFolioView"/>&nbsp;</td>
							  	    <td><bean:write name="FolioVO" property="numeroView"/>&nbsp;</td>
	   						  	    <td><bean:write name="FolioVO" property="descripcion"/>&nbsp;</td>
								</tr>
							</logic:iterate>
					</logic:notEmpty>
					<logic:empty  name="procesoBalanceAdapterVO" property="balance.listFolio">
						<tr><td colspan="5" align="center">
							<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
					</logic:empty>					
					<logic:equal name="procesoBalanceAdapterVO" property="paramPaso" value="1">
						<td colspan="5" align="right">
			  				<bean:define id="incluirFolioEnabled" name="procesoBalanceAdapterVO" property="incluirFolioEnabled"/>
							<input type="button" <%=incluirFolioEnabled%> class="boton" 
								onClick="submitForm('incluirFolio', '<bean:write name="procesoBalanceAdapterVO" property="balance.id" bundle="base" formatKey="general.format.id"/>');" 
								value="<bean:message bundle="bal" key="bal.procesoBalanceAdapter.adm.button.incluir"/>"	align="left" />
						</td>
					</logic:equal>						
				</tbody>
				</table>			
		</div>
		
		<!-- Seleccionar Archivos de Transacciones -->
		<div id="seleccionarArchivos" class="scrolable" style="height: 200px;">
				<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
					<caption><bean:message bundle="bal" key="bal.balance.listArchivo"/></caption>
                	<tbody>
	                	<tr>
							<th width="1">&nbsp;</th> <!-- Ver-->
							<th width="1">&nbsp;</th> <!-- Excluir -->
						  	<th align="left"><bean:message bundle="bal" key="bal.archivo.fechaBanco.label"/></th>
						  	<th align="left"><bean:message bundle="bal" key="bal.tipoArc.label"/></th>
						  	<th align="left"><bean:message bundle="bal" key="bal.archivo.nroBanco.label"/></th>
						  	<th align="left"><bean:message bundle="bal" key="bal.archivo.total.label"/></th>
						  	<th align="left"><bean:message bundle="bal" key="bal.archivo.nombre.label"/></th>
						</tr>
						<logic:notEmpty  name="procesoBalanceAdapterVO" property="balance.listArchivo">							
						<logic:iterate id="ArchivoVO" name="procesoBalanceAdapterVO" property="balance.listArchivo">
							<tr>						
								<!-- Ver -->
								<td>
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verArchivo', '<bean:write name="ArchivoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</td>
								<td>
									<!-- Excluir-->								
									<logic:equal name="procesoBalanceAdapterVO" property="paramPaso" value="1">
										<logic:equal name="procesoBalanceAdapterVO" property="excluirArchivoEnabled" value="enabled">		
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('excluirArchivo', '<bean:write name="ArchivoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="bal" key="bal.procesoBalanceAdapter.adm.button.excluir"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
											</a>
										</logic:equal>							
										<logic:notEqual name="procesoBalanceAdapterVO" property="excluirArchivoEnabled" value="enabled">		
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</logic:equal>						
									<logic:notEqual name="procesoBalanceAdapterVO" property="paramPaso" value="1">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>																
								</td>
							    <td><bean:write name="ArchivoVO" property="fechaBancoView"/>&nbsp;</td>
						  	    <td><bean:write name="ArchivoVO" property="tipoArc.descripcion"/>&nbsp;</td>
   						  	    <td><bean:write name="ArchivoVO" property="nroBancoView"/>&nbsp;</td>
   						  	    <td><bean:write name="ArchivoVO" property="totalView"/>&nbsp;</td>
	 					  	    <td><bean:write name="ArchivoVO" property="nombre"/>&nbsp;</td>
							</tr>
						</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="procesoBalanceAdapterVO" property="balance.listArchivo">
						<tr><td colspan="10" align="center">
							<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
				</logic:empty>		
				<logic:equal name="procesoBalanceAdapterVO" property="paramPaso" value="1">
					<td colspan="10" align="right">
			  				<bean:define id="incluirArchivoEnabled" name="procesoBalanceAdapterVO" property="incluirArchivoEnabled"/>
							<input type="button" <%=incluirArchivoEnabled%> class="boton" 
								onClick="submitForm('incluirArchivo', '<bean:write name="procesoBalanceAdapterVO" property="balance.id" bundle="base" formatKey="general.format.id"/>');" 
								value="<bean:message bundle="bal" key="bal.procesoBalanceAdapter.adm.button.incluir"/>"	align="left" />
					</td>
				</logic:equal>							
				</tbody>
			</table>
		</div>
		<br/>
		<!-- Fin Seleccionar Archivos de Transacciones -->

		<!-- Seleccionar Compensaciones -->
		<div id="seleccionarCompensaciones" class="scrolable" style="height: 200px;">
				<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
					<caption><bean:message bundle="bal" key="bal.balance.listCompensacion"/></caption>
                	<tbody>
	                	<tr>
							<th width="1">&nbsp;</th> <!-- Ver-->
							<th width="1">&nbsp;</th> <!-- Excluir -->
						  	<th align="left"><bean:message bundle="bal" key="bal.compensacion.fechaAlta.label"/></th>
						  	<th align="left"><bean:message bundle="def" key="def.recurso.label"/></th>
						  	<th align="left"><bean:message bundle="pad" key="pad.cuenta.label"/></th>
						  	<th align="left"><bean:message bundle="bal" key="bal.compensacion.descripcion.label"/></th>
						</tr>
						<logic:notEmpty  name="procesoBalanceAdapterVO" property="balance.listCompensacion">							
						<logic:iterate id="CompensacionVO" name="procesoBalanceAdapterVO" property="balance.listCompensacion">
							<tr>						
								<!-- Ver -->
								<td>
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verCompensacion', '<bean:write name="CompensacionVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</td>
								<td>
									<!-- Excluir-->								
									<logic:equal name="procesoBalanceAdapterVO" property="paramPaso" value="1">
										<logic:equal name="procesoBalanceAdapterVO" property="excluirCompensacionEnabled" value="enabled">		
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('excluirCompensacion', '<bean:write name="CompensacionVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="bal" key="bal.procesoBalanceAdapter.adm.button.excluir"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
											</a>
										</logic:equal>							
										<logic:notEqual name="procesoBalanceAdapterVO" property="excluirCompensacionEnabled" value="enabled">		
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</logic:equal>						
									<logic:notEqual name="procesoBalanceAdapterVO" property="paramPaso" value="1">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>																
								</td>
							    <td><bean:write name="CompensacionVO" property="fechaAltaView"/>&nbsp;</td>
						  	    <td><bean:write name="CompensacionVO" property="cuenta.recurso.desRecurso"/>&nbsp;</td>
   						  	    <td><bean:write name="CompensacionVO" property="cuenta.numeroCuenta"/>&nbsp;</td>
   						  	    <td><bean:write name="CompensacionVO" property="descripcion"/>&nbsp;</td>
							</tr>
						</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="procesoBalanceAdapterVO" property="balance.listCompensacion">
						<tr><td colspan="10" align="center">
							<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
				</logic:empty>		
				<logic:equal name="procesoBalanceAdapterVO" property="paramPaso" value="1">
					<td colspan="10" align="right">
			  				<bean:define id="incluirCompensacionEnabled" name="procesoBalanceAdapterVO" property="incluirCompensacionEnabled"/>
							<input type="button" <%=incluirCompensacionEnabled%> class="boton" 
								onClick="submitForm('incluirCompensacion', '<bean:write name="procesoBalanceAdapterVO" property="balance.id" bundle="base" formatKey="general.format.id"/>');" 
								value="<bean:message bundle="bal" key="bal.procesoBalanceAdapter.adm.button.incluir"/>"	align="left" />
					</td>
				</logic:equal>							
				</tbody>
			</table>
		</div>
		<br/>
		<!-- Seleccionar Compensaciones -->


		<!-- Administrar Reingresos -->		
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.procesarBalanceAdapter.reingreso.title"/></legend>			
			<table class="tabladatos" width="100%">
				<!-- Cant. de Reingresos -->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.procesarBalanceAdapter.cantReingreso.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoBalanceAdapterVO" property="cantReingreso"/></td>
				</tr>		
				<!-- Total Imp. Cobrado de Reingresos -->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.procesarBalanceAdapter.impCobradoReingreso.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoBalanceAdapterVO" property="impCobradoReingreso"/></td>
				</tr>	
			</table>
			<table class="tablabotones" width="100%">
			<tr align="center">				
				<td>
					<logic:equal name="procesoBalanceAdapterVO" property="paramPaso" value="1">
						<logic:equal name="procesoBalanceAdapterVO" property="deshabilitarAdm" value="false">
							<input type="button" class="boton" onClick="submitForm('incluirReingreso', 
								'<bean:write name="procesoBalanceAdapterVO" property="balance.id" bundle="base" formatKey="general.format.id"/>');" 
								value="<bean:message bundle="bal" key="bal.procesoBalanceAdapter.adm.button.incluir"/>"/>
							<input type="button" class="boton" onClick="submitForm('excluirReingreso', 
								'<bean:write name="procesoBalanceAdapterVO" property="balance.id" bundle="base" formatKey="general.format.id"/>');" 
								value="<bean:message bundle="bal" key="bal.procesoBalanceAdapter.adm.button.excluir"/>"/>
						</logic:equal>		
					</logic:equal>							
						<input type="button" class="boton" onClick="submitForm('verReingreso', 
							'<bean:write name="procesoBalanceAdapterVO" property="balance.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="bal" key="bal.procesoBalanceAdapter.adm.button.listar"/>"/>
				</td>
			</tr>
			</table>
		</fieldset>
		<!-- Fin Administrar Reingresos -->		
		
		<fieldset>
				<legend><bean:message bundle="bal" key="bal.procesarBalanceAdapter.paso.title"/></legend>			
							
				<table class="tabladatos" width="100%">
				<tr>
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.fecha.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoBalanceAdapterVO" property="pasoCorrida1.fechaCorridaView"/></td>
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.hora.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoBalanceAdapterVO" property="pasoCorrida1.horaCorridaView"/></td>
				</tr>	
				<tr>
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.observacion.label"/>: </label></td>
					<td colspan="2" class="normal"><bean:write name="procesoBalanceAdapterVO" property="pasoCorrida1.observacion"/></td>
				</tr>	
				<tr>
					<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.estado.label"/>: </label></td>
					<td colspan="2" class="normal"><bean:write name="procesoBalanceAdapterVO" property="pasoCorrida1.estadoCorrida.desEstadoCorrida"/></td>
				</tr>	
				</table>
				<logic:equal name="procesoBalanceAdapterVO" property="paramPaso" value="1">
				<table class="tablabotones" width="100%">
				<tr>				
			    	<td align="center">
						<logic:equal name="procesoBalanceAdapterVO" property="paramActivar" value="true">
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');" disabled='false'>
								<bean:message bundle="pro" key="pro.abm.button.activar"/>
							</html:button>&nbsp;&nbsp;
						</logic:equal>
						<logic:equal name="procesoBalanceAdapterVO" property="paramActivar" value="false">
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');" disabled='true'>
								<bean:message bundle="pro" key="pro.abm.button.activar"/>
							</html:button>&nbsp;&nbsp;
						</logic:equal>
						<logic:equal name="procesoBalanceAdapterVO" property="paramReiniciar" value="true">
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('reiniciar', '');" disabled='false'>
								<bean:message bundle="pro" key="pro.abm.button.reiniciar"/>
							</html:button>&nbsp;&nbsp;	
						</logic:equal>			
						<logic:equal name="procesoBalanceAdapterVO" property="paramReiniciar" value="false">
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
		</fieldset>
		<!-- Fin Preparacion del Balance -->			

		<!-- Reportes  -->		
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.procesarBalanceAdapter.fieldset2"/></legend>					
			<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.procesoBalanceAdapter.paso1.reportes"/></caption>
	    	<tbody>
				<logic:notEmpty  name="procesoBalanceAdapterVO" property="listFileCorrida1">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Adjunto -->
						<th align="left"><bean:message bundle="base" key="base.nombre.label"/></th>
						<th align="left"><bean:message bundle="base" key="base.descripcion.label"/></th>						
					</tr>
					<logic:iterate id="FileCorridaVO" name="procesoBalanceAdapterVO" property="listFileCorrida1">
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
				<logic:empty  name="procesoBalanceAdapterVO" property="listFileCorrida1">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			</tbody>
			</table>
		</fieldset>
		<!-- Fin Reportes  -->		
		
		<!-- 2- Validacion de Total a Imputar a Partidas  -->		
		<logic:equal name="procesoBalanceAdapterVO" property="paramPaso" value="2">
			<a name="pasoActual">&nbsp;</a> 
		</logic:equal>
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.procesarBalanceAdapter.fieldset3"/></legend>			
			<table class="tabladatos" width="100%">
			<tr>
				<td class="normal" colspan="4" align="left"><bean:message bundle="bal" key="bal.procesoBalanceAdapter.paso2.description"/></td>
			</tr>
			</table>
			<logic:equal name="procesoBalanceAdapterVO" property="paramPaso" value="2">
			<!-- Detalle Caja7 -->
			<div id="detalleCaja7" class="scrolable" style="height: 200px;">
				<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
					<caption><bean:message bundle="bal" key="bal.balance.listCaja7"/></caption>
                	<tbody>
	                	<tr>
							<th width="1">&nbsp;</th> <!-- Ver-->
							<th width="1">&nbsp;</th> <!-- Modificar -->
							<th width="1">&nbsp;</th> <!-- Eliminar -->
							<th align="left"><bean:message bundle="bal" key="bal.caja7.fecha.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.partida.label"/></th>
							<th align="center"><bean:message bundle="bal" key="bal.caja7.importe.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.caja7.descripcion.label"/></th>
					</tr>
						<logic:notEmpty  name="procesoBalanceAdapterVO" property="balance.listCaja7">							
						<logic:iterate id="Caja7VO" name="procesoBalanceAdapterVO" property="balance.listCaja7">
							<tr>						
								<!-- Ver -->
								<td>
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verCaja7', '<bean:write name="Caja7VO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</td>
								<!-- Modificar-->								
								<td>
									<logic:equal name="procesoBalanceAdapterVO" property="paramPaso" value="2">
										<logic:equal name="procesoBalanceAdapterVO" property="modificarCaja7Enabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarCaja7', '<bean:write name="Caja7VO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="procesoBalanceAdapterVO" property="modificarCaja7Enabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="procesoBalanceAdapterVO" property="paramPaso" value="2">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</td>
								<!-- Eliminar-->								
								<td>
									<logic:equal name="procesoBalanceAdapterVO" property="paramPaso" value="2">
										<logic:equal name="procesoBalanceAdapterVO" property="eliminarCaja7Enabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarCaja7', '<bean:write name="Caja7VO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
											</a>
										</logic:equal>	
										<logic:notEqual name="procesoBalanceAdapterVO" property="eliminarCaja7Enabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</logic:equal>	
									<logic:notEqual name="procesoBalanceAdapterVO" property="paramPaso" value="2">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</td>
								<td><bean:write name="Caja7VO" property="fechaView"/>&nbsp;</td>
								<td><bean:write name="Caja7VO" property="partida.desPartidaView"/>&nbsp;</td>
								<td align="center"><bean:write name="Caja7VO" property="importeView"/>&nbsp;$</td>
								<td><bean:write name="Caja7VO" property="descripcion"/>&nbsp;</td>
								</tr>
						</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="procesoBalanceAdapterVO" property="balance.listCaja7">
						<tr><td colspan="10" align="center">
							<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
				</logic:empty>		
				</tbody>
			</table>
		</div>
		<logic:equal name="procesoBalanceAdapterVO" property="paramPaso" value="2">
		<table class="tablabotones" width="100%">
			<tr>				
				<td colspan="10" align="right">
		  				<bean:define id="agregarCaja7Enabled" name="procesoBalanceAdapterVO" property="agregarCaja7Enabled"/>
						<input type="button" <%=agregarCaja7Enabled%> class="boton" 
							onClick="submitForm('agregarCaja7', '<bean:write name="procesoBalanceAdapterVO" property="balance.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"	align="left" />
				</td>
			</tr>
		</table>
		</logic:equal>
		<!-- Fin Detalle Caja7 -->
		
		<br>&nbsp;</br>
		
		<!-- Detalle Caja69 -->
			<div id="detalleCaja69" class="scrolable" style="height: 200px;">
				<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
					<caption><bean:message bundle="bal" key="bal.balance.listCaja69"/></caption>
                	<tbody>
	                	<tr>
							<th width="1">&nbsp;</th> <!-- Ver-->
							<th width="1">&nbsp;</th> <!-- Modificar -->
							<th width="1">&nbsp;</th> <!-- Eliminar -->
							<th align="left"><bean:message bundle="bal" key="bal.sistema.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.transaccion.nroComprobante.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.transaccion.clave.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.transaccion.resto.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.transaccion.importe.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.transaccion.fechaPago.label"/></th>
					</tr>
						<logic:notEmpty  name="procesoBalanceAdapterVO" property="balance.listCaja69">							
						<logic:iterate id="Caja69VO" name="procesoBalanceAdapterVO" property="balance.listCaja69">
							<tr>						
								<!-- Ver -->
								<td>
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verCaja69', '<bean:write name="Caja69VO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</td>
								<!-- Modificar-->								
								<td>
									<logic:equal name="procesoBalanceAdapterVO" property="paramPaso" value="2">
										<logic:equal name="procesoBalanceAdapterVO" property="modificarCaja69Enabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarCaja69', '<bean:write name="Caja69VO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="procesoBalanceAdapterVO" property="modificarCaja69Enabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="procesoBalanceAdapterVO" property="paramPaso" value="2">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>					
								</td>
								<!-- Eliminar-->								
								<td>
									<logic:equal name="procesoBalanceAdapterVO" property="paramPaso" value="2">
										<logic:equal name="procesoBalanceAdapterVO" property="eliminarCaja69Enabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarCaja69', '<bean:write name="Caja69VO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
											</a>
										</logic:equal>	
										<logic:notEqual name="procesoBalanceAdapterVO" property="eliminarCaja69Enabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</logic:equal>	
									<logic:notEqual name="procesoBalanceAdapterVO" property="paramPaso" value="2">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
									
								</td>
								<td><bean:write name="Caja69VO" property="sistemaView"/>&nbsp;</td>
								<td><bean:write name="Caja69VO" property="nroComprobanteView"/>&nbsp;</td>
								<td><bean:write name="Caja69VO" property="clave"/>&nbsp;</td>
								<td><bean:write name="Caja69VO" property="restoView"/>&nbsp;</td>
								<td><bean:write name="Caja69VO" property="importeView"/>&nbsp;$</td>
								<td><bean:write name="Caja69VO" property="fechaPagoView"/>&nbsp;</td>
								</tr>
						</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="procesoBalanceAdapterVO" property="balance.listCaja69">
						<tr><td colspan="10" align="center">
							<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
				</logic:empty>		
				</tbody>
			</table>
		</div>
		<logic:equal name="procesoBalanceAdapterVO" property="paramPaso" value="2">
			<table class="tablabotones" width="100%">
				<tr>				
					<td colspan="10" align="right">
			  				<bean:define id="agregarCaja69Enabled" name="procesoBalanceAdapterVO" property="agregarCaja69Enabled"/>
							<input type="button" <%=agregarCaja69Enabled%> class="boton" 
								onClick="submitForm('agregarCaja69', '<bean:write name="procesoBalanceAdapterVO" property="balance.id" bundle="base" formatKey="general.format.id"/>');" 
								value="<bean:message bundle="base" key="abm.button.agregar"/>"	align="left" />
					</td>
				</tr>	
			</table>
		</logic:equal>		
		<!-- Fin Detalle Caja69 -->
			
		<!-- Totales de Cajas -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.procesarBalanceAdapter.totalesCaja.title"/></legend>			
			<table class="tabladatos" width="100%">
				<!-- Total Caja 7 -->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.procesarBalanceAdapter.totalCaja7.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoBalanceAdapterVO" property="totalCaja7"/></td>
				</tr>		
				<!-- Total Caja 69 -->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.procesarBalanceAdapter.totalCaja69.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoBalanceAdapterVO" property="totalCaja69"/></td>
				</tr>	
			</table>
		</fieldset>
		<!-- Fin Detalle TranBal -->
		
		<!-- Detalle TranBal -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.balance.listTranBal"/></legend>			
			<table class="tabladatos" width="100%">
				<!-- Cant. de TranBal -->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.procesarBalanceAdapter.cantTranBal.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoBalanceAdapterVO" property="cantTranBal"/></td>
				</tr>		
				<!-- Total Imp. Cobrado de TranBal -->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.procesarBalanceAdapter.impCobradoTranBal.label"/>: </label></td>
					<td class="normal"><bean:write name="procesoBalanceAdapterVO" property="impCobradoTranBal"/></td>
				</tr>	
			</table>
		</fieldset>
		<!-- Fin Detalle TranBal -->
		
		</logic:equal>
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.procesarBalanceAdapter.paso.title"/></legend>			
			<table class="tabladatos" width="100%">
			<tr>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.fecha.label"/>: </label></td>
				<td class="normal"><bean:write name="procesoBalanceAdapterVO" property="pasoCorrida2.fechaCorridaView"/></td>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.hora.label"/>: </label></td>
				<td class="normal"><bean:write name="procesoBalanceAdapterVO" property="pasoCorrida2.horaCorridaView"/></td>
			</tr>	
			<tr>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.observacion.label"/>: </label></td>
				<td colspan="2" class="normal"><bean:write name="procesoBalanceAdapterVO" property="pasoCorrida2.observacion"/></td>
			</tr>	
			<tr>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.estado.label"/>: </label></td>
				<td colspan="2" class="normal"><bean:write name="procesoBalanceAdapterVO" property="pasoCorrida2.estadoCorrida.desEstadoCorrida"/></td>
			</tr>	
			</table>
			<logic:equal name="procesoBalanceAdapterVO" property="paramPaso" value="2">
			<table class="tablabotones" width="100%">
			<tr>				
		    	<td align="center">
					<logic:equal name="procesoBalanceAdapterVO" property="paramActivar" value="true">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');" disabled='false'>
							<bean:message bundle="pro" key="pro.abm.button.activar"/>
						</html:button>&nbsp;&nbsp;
					</logic:equal>
					<logic:equal name="procesoBalanceAdapterVO" property="paramActivar" value="false">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');" disabled='true'>
							<bean:message bundle="pro" key="pro.abm.button.activar"/>
						</html:button>&nbsp;&nbsp;
					</logic:equal>
					<logic:equal name="procesoBalanceAdapterVO" property="paramRetroceder" value="true">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('retroceder', '');" disabled='false'>
							<bean:message bundle="pro" key="pro.abm.button.retroceder"/>
						</html:button>&nbsp;&nbsp;	
					</logic:equal>			
					<logic:equal name="procesoBalanceAdapterVO" property="paramRetroceder" value="false">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('retroceder', '');" disabled='true'>
							<bean:message bundle="pro" key="pro.abm.button.retroceder"/>
						</html:button>&nbsp;&nbsp;	
					</logic:equal>	
					<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('refill', '');" disabled='false' styleId="locate_paso">
						<bean:message bundle="pro" key="pro.abm.button.refill"/>
					</html:button>&nbsp;&nbsp;
			
			    	<%@ include file="/pro/adpProceso/includeLogFileButton.jsp" %>												
	   	    	</td>	   	    	
 			</tr>
 			&nbsp;&nbsp;
			</table>
			</logic:equal>
		</fieldset>
		<!-- Fin Validacion de Total a Imputar a Partidas  -->		
		
		<!-- Reportes de Clasificadores  -->		
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.procesarBalanceAdapter.fieldset4"/></legend>			
			<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.procesoBalanceAdapter.paso2.reportes"/></caption>
	    	<tbody>
					<logic:notEmpty  name="procesoBalanceAdapterVO" property="listFileCorrida2">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Adjunto -->
						<th align="left"><bean:message bundle="base" key="base.nombre.label"/></th>
						<th align="left"><bean:message bundle="base" key="base.descripcion.label"/></th>						
					</tr>
					<logic:iterate id="FileCorridaVO" name="procesoBalanceAdapterVO" property="listFileCorrida2">
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
				<logic:empty  name="procesoBalanceAdapterVO" property="listFileCorrida2">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
		</tbody>
			</table>
		</fieldset>
		<!-- Fin Reportes de Clasificadores  -->		


		<!-- 3- Desgloce por Servicio Banco -->		
		<logic:equal name="procesoBalanceAdapterVO" property="paramPaso" value="3">
			<a name="pasoActual">&nbsp;</a> 
		</logic:equal>		
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.procesarBalanceAdapter.fieldset5"/></legend>			
			<table class="tabladatos" width="100%">
			<tr>
				<td class="normal" colspan="4" align="left"><bean:message bundle="bal" key="bal.procesoBalanceAdapter.paso3.description"/></td>
			</tr>
			<tr>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.fecha.label"/>: </label></td>
				<td class="normal"><bean:write name="procesoBalanceAdapterVO" property="pasoCorrida3.fechaCorridaView"/></td>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.hora.label"/>: </label></td>
				<td class="normal"><bean:write name="procesoBalanceAdapterVO" property="pasoCorrida3.horaCorridaView"/></td>
			</tr>	
			<tr>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.observacion.label"/>: </label></td>
				<td colspan="2" class="normal"><bean:write name="procesoBalanceAdapterVO" property="pasoCorrida3.observacion"/></td>
			</tr>	
			<tr>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.estado.label"/>: </label></td>
				<td colspan="2" class="normal"><bean:write name="procesoBalanceAdapterVO" property="pasoCorrida3.estadoCorrida.desEstadoCorrida"/></td>
			</tr>	
			</table>
			<logic:equal name="procesoBalanceAdapterVO" property="paramPaso" value="3">
			<table class="tablabotones" width="100%">
			<tr>				
		    	<td align="center">
					<logic:equal name="procesoBalanceAdapterVO" property="paramActivar" value="true">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');" disabled='false'>
							<bean:message bundle="pro" key="pro.abm.button.activar"/>
						</html:button>&nbsp;&nbsp;
					</logic:equal>
					<logic:equal name="procesoBalanceAdapterVO" property="paramActivar" value="false">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');" disabled='true'>
							<bean:message bundle="pro" key="pro.abm.button.activar"/>
						</html:button>&nbsp;&nbsp;
					</logic:equal>
					<logic:equal name="procesoBalanceAdapterVO" property="paramRetroceder" value="true">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('retroceder', '');" disabled='false'>
							<bean:message bundle="pro" key="pro.abm.button.retroceder"/>
						</html:button>&nbsp;&nbsp;	
					</logic:equal>			
					<logic:equal name="procesoBalanceAdapterVO" property="paramRetroceder" value="false">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('retroceder', '');" disabled='true'>
							<bean:message bundle="pro" key="pro.abm.button.retroceder"/>
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
		<!-- Fin Desgloce por Servicio Banco  -->		

		<!-- Reportes del Balance  -->		
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.procesarBalanceAdapter.fieldset6"/></legend>			
			<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.procesoBalanceAdapter.paso3.reportes"/></caption>
	    	<tbody>
				<logic:notEmpty  name="procesoBalanceAdapterVO" property="listFileCorrida3">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Adjunto -->
						<th align="left"><bean:message bundle="base" key="base.nombre.label"/></th>
						<th align="left"><bean:message bundle="base" key="base.descripcion.label"/></th>						
					</tr>
					<logic:iterate id="FileCorridaVO" name="procesoBalanceAdapterVO" property="listFileCorrida3">
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
				<logic:empty  name="procesoBalanceAdapterVO" property="listFileCorrida3">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			</tbody>
			</table>
		</fieldset>
		<!-- Fin Reportes del Balance  -->				
		
		<!-- 4- Administracion de Procesos de Asentamientos  -->		
		<logic:equal name="procesoBalanceAdapterVO" property="paramPaso" value="4">
			<a name="pasoActual">&nbsp;</a> 
		</logic:equal>		
		<fieldset>
		<legend><bean:message bundle="bal" key="bal.procesarBalanceAdapter.fieldset7"/></legend>			
		
		<table class="tabladatos" width="100%">
			<tr>
			<td class="normal" colspan="4" align="left"><bean:message bundle="bal" key="bal.procesoBalanceAdapter.paso4.description"/></td>
			</tr>
			<tr><td>&nbsp;&nbsp;</td></tr> 
			<tr><td>&nbsp;&nbsp;</td></tr> 
		</table>
	
		<!-- Administrar Asentamientos de Pago  -->
		<div id="asentamientosAsociados">
					<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
						<caption><bean:message bundle="bal" key="bal.balance.listAsentamiento"/></caption>
	                	<tbody>
		                	<tr>
								<th width="1">&nbsp;</th> <!-- Ver -->
								<th width="1">&nbsp;</th> <!-- Administrar Proceso -->
						  		<th align="left"><bean:message bundle="bal" key="bal.asentamiento.numero.label"/></th>
							  	<th align="left"><bean:message bundle="def" key="def.servicioBanco.label"/></th>
  								<th align="left"><bean:message bundle="bal" key="bal.asentamiento.estadoProceso.label"/></th>
							</tr>							
						<logic:notEmpty  name="procesoBalanceAdapterVO" property="balance.listAsentamiento">	
							<logic:iterate id="AsentamientoVO" name="procesoBalanceAdapterVO" property="balance.listAsentamiento">
								<tr>						
									<!-- Ver-->
									<td>
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verAsentamiento', '<bean:write name="AsentamientoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</td>		
									<td>
										<!-- Administrar Proceso-->								
										<logic:equal name="procesoBalanceAdapterVO" property="admProcesoAsentamientoEnabled" value="enabled">		
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('admProcesoAsentamiento', '<bean:write name="AsentamientoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="bal" key="bal.asentamientoSearchPage.adm.button.admProceso"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/admProceso0.gif"/>
											</a>
										</logic:equal>							
										<logic:notEqual name="procesoBalanceAdapterVO" property="admProcesoAsentamientoEnabled" value="enabled">		
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/admProceso1.gif"/>
										</logic:notEqual>
									</td>
									<td><bean:write name="AsentamientoVO" property="id" bundle="base" formatKey="general.format.id"/>&nbsp;</td>
   							  	    <td><bean:write name="AsentamientoVO" property="servicioBanco.desServicioBanco"/>&nbsp;</td>
							 		<td><bean:write name="AsentamientoVO" property="corrida.estadoCorrida.desEstadoCorrida" />&nbsp;</td>
								</tr>
							</logic:iterate>
					</logic:notEmpty>
					<logic:empty  name="procesoBalanceAdapterVO" property="balance.listAsentamiento">
						<tr><td colspan="5" align="center">
							<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
					</logic:empty>					
				</tbody>
				</table>			
		</div>

		<!-- Administrar AseDels Delegados  -->
		<div id="aseDelsAsociados">
					<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
						<caption><bean:message bundle="bal" key="bal.balance.listAseDel"/></caption>
	                	<tbody>
		                	<tr>
								<th width="1">&nbsp;</th> <!-- Ver -->
								<th width="1">&nbsp;</th> <!-- Administrar Proceso -->
						  		<th align="left"><bean:message bundle="bal" key="bal.aseDel.numero.label"/></th>
							  	<th align="left"><bean:message bundle="def" key="def.servicioBanco.label"/></th>
  								<th align="left"><bean:message bundle="bal" key="bal.aseDel.estadoProceso.label"/></th>
							</tr>							
						<logic:notEmpty  name="procesoBalanceAdapterVO" property="balance.listAseDel">	
							<logic:iterate id="AseDelVO" name="procesoBalanceAdapterVO" property="balance.listAseDel">
								<tr>						
									<!-- Ver-->
									<td>
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verAseDel', '<bean:write name="AseDelVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</td>		
									<td>
										<!-- Administrar Proceso-->								
										<logic:equal name="procesoBalanceAdapterVO" property="admProcesoAseDelEnabled" value="enabled">		
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('admProcesoAseDel', '<bean:write name="AseDelVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="bal" key="bal.aseDelSearchPage.adm.button.admProceso"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/admProceso0.gif"/>
											</a>
										</logic:equal>							
										<logic:notEqual name="procesoBalanceAdapterVO" property="admProcesoAseDelEnabled" value="enabled">		
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/admProceso1.gif"/>
										</logic:notEqual>
									</td>
									<td><bean:write name="AseDelVO" property="id" bundle="base" formatKey="general.format.id"/>&nbsp;</td>
   							  	    <td><bean:write name="AseDelVO" property="servicioBanco.desServicioBanco"/>&nbsp;</td>
							 		<td><bean:write name="AseDelVO" property="corrida.estadoCorrida.desEstadoCorrida" />&nbsp;</td>
								</tr>
							</logic:iterate>
					</logic:notEmpty>
					<logic:empty  name="procesoBalanceAdapterVO" property="balance.listAseDel">
						<tr><td colspan="5" align="center">
							<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
					</logic:empty>					
				</tbody>
				</table>			
		</div>
		</fieldset>
		
		<fieldset>
		<legend><bean:message bundle="bal" key="bal.procesarBalanceAdapter.paso.title"/></legend>			
		<table class="tabladatos" width="100%">
			<tr>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.fecha.label"/>: </label></td>
				<td class="normal"><bean:write name="procesoBalanceAdapterVO" property="pasoCorrida4.fechaCorridaView"/></td>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.hora.label"/>: </label></td>
				<td class="normal"><bean:write name="procesoBalanceAdapterVO" property="pasoCorrida4.horaCorridaView"/></td>
			</tr>	
			<tr>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.observacion.label"/>: </label></td>
				<td colspan="2" class="normal"><bean:write name="procesoBalanceAdapterVO" property="pasoCorrida4.observacion"/></td>
			</tr>	
			<tr>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.estado.label"/>: </label></td>
				<td colspan="2" class="normal"><bean:write name="procesoBalanceAdapterVO" property="pasoCorrida4.estadoCorrida.desEstadoCorrida"/></td>
			</tr>	
			</table>
			<logic:equal name="procesoBalanceAdapterVO" property="paramPaso" value="4">
			<table class="tablabotones" width="100%">
			<tr>				
		    	<td align="center">
					<logic:equal name="procesoBalanceAdapterVO" property="paramActivar" value="true">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');" disabled='false'>
							<bean:message bundle="pro" key="pro.abm.button.activar"/>
						</html:button>&nbsp;&nbsp;
					</logic:equal>
					<logic:equal name="procesoBalanceAdapterVO" property="paramActivar" value="false">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');" disabled='true'>
							<bean:message bundle="pro" key="pro.abm.button.activar"/>
						</html:button>&nbsp;&nbsp;
					</logic:equal>
					<logic:equal name="procesoBalanceAdapterVO" property="paramRetroceder" value="true">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('retroceder', '');" disabled='false'>
							<bean:message bundle="pro" key="pro.abm.button.retroceder"/>
						</html:button>&nbsp;&nbsp;	
					</logic:equal>			
					<logic:equal name="procesoBalanceAdapterVO" property="paramRetroceder" value="false">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('retroceder', '');" disabled='true'>
							<bean:message bundle="pro" key="pro.abm.button.retroceder"/>
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
		<!-- Fin Administracion de Procesos de Asentamientos  -->		

		<!-- Reportes del Balance  -->		
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.procesarBalanceAdapter.fieldset8"/></legend>			
			<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.procesoBalanceAdapter.paso4.reportes"/></caption>
	    	<tbody>
				<logic:notEmpty  name="procesoBalanceAdapterVO" property="listFileCorrida4">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Adjunto -->
						<th align="left"><bean:message bundle="base" key="base.nombre.label"/></th>
						<th align="left"><bean:message bundle="base" key="base.descripcion.label"/></th>						
					</tr>
					<logic:iterate id="FileCorridaVO" name="procesoBalanceAdapterVO" property="listFileCorrida4">
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
				<logic:empty  name="procesoBalanceAdapterVO" property="listFileCorrida4">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			</tbody>
			</table>
		</fieldset>
		<!-- Fin Reportes del Balance  -->		
		
		<!-- 5- Caja7 Finales  -->		
		<logic:equal name="procesoBalanceAdapterVO" property="paramPaso" value="5">
			<a name="pasoActual">&nbsp;</a> 
		</logic:equal>		
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.procesarBalanceAdapter.fieldset9"/></legend>			
			<table class="tabladatos" width="100%">
			<tr>
				<td class="normal" colspan="4" align="left"><bean:message bundle="bal" key="bal.procesoBalanceAdapter.paso5.description"/></td>
			</tr>
			</table>
			<!-- Detalle Caja7 -->
			<div id="detalleCaja7" class="scrolable" style="height: 200px;">
				<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
					<caption><bean:message bundle="bal" key="bal.balance.listCaja7"/></caption>
                	<tbody>
	                	<tr>
							<th width="1">&nbsp;</th> <!-- Ver-->
							<th width="1">&nbsp;</th> <!-- Modificar -->
							<th width="1">&nbsp;</th> <!-- Eliminar -->
							<th align="left"><bean:message bundle="bal" key="bal.caja7.fecha.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.partida.label"/></th>
							<th align="center"><bean:message bundle="bal" key="bal.caja7.importe.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.caja7.descripcion.label"/></th>
					</tr>
						<logic:notEmpty  name="procesoBalanceAdapterVO" property="balance.listCaja7">							
						<logic:iterate id="Caja7VO" name="procesoBalanceAdapterVO" property="balance.listCaja7">
							<tr>						
								<!-- Ver -->
								<td>
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verCaja7', '<bean:write name="Caja7VO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</td>
								<!-- Modificar-->								
								<td>
									<logic:equal name="procesoBalanceAdapterVO" property="paramPaso" value="5">
										<logic:equal name="procesoBalanceAdapterVO" property="modificarCaja7Enabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarCaja7', '<bean:write name="Caja7VO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="procesoBalanceAdapterVO" property="modificarCaja7Enabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="procesoBalanceAdapterVO" property="paramPaso" value="5">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</td>
								<!-- Eliminar-->								
								<td>
									<logic:equal name="procesoBalanceAdapterVO" property="paramPaso" value="5">
										<logic:equal name="procesoBalanceAdapterVO" property="eliminarCaja7Enabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarCaja7', '<bean:write name="Caja7VO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
											</a>
										</logic:equal>	
										<logic:notEqual name="procesoBalanceAdapterVO" property="eliminarCaja7Enabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</logic:equal>	
									<logic:notEqual name="procesoBalanceAdapterVO" property="paramPaso" value="5">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</td>
								<td><bean:write name="Caja7VO" property="fechaView"/>&nbsp;</td>
								<td><bean:write name="Caja7VO" property="partida.desPartidaView"/>&nbsp;</td>
								<td align="center"><bean:write name="Caja7VO" property="importeView"/>&nbsp;$</td>
								<td><bean:write name="Caja7VO" property="descripcion"/>&nbsp;</td>
								</tr>
						</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="procesoBalanceAdapterVO" property="balance.listCaja7">
						<tr><td colspan="10" align="center">
							<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
				</logic:empty>		
				</tbody>
			</table>
		</div>
		<logic:equal name="procesoBalanceAdapterVO" property="paramPaso" value="5">
		<table class="tablabotones" width="100%">
			<tr>	
				<td colspan="10" align="right">
			  				<bean:define id="incluirAuxCaja7Enabled" name="procesoBalanceAdapterVO" property="incluirAuxCaja7Enabled"/>
							<input type="button" <%=incluirAuxCaja7Enabled%> class="boton" 
								onClick="submitForm('incluirAuxCaja7', '<bean:write name="procesoBalanceAdapterVO" property="balance.id" bundle="base" formatKey="general.format.id"/>');" 
								value="<bean:message bundle="bal" key="bal.procesoBalanceAdapter.adm.button.incluir"/>"	align="left" />
		  				<bean:define id="agregarCaja7Enabled" name="procesoBalanceAdapterVO" property="agregarCaja7Enabled"/>
						<input type="button" <%=agregarCaja7Enabled%> class="boton" 
							onClick="submitForm('agregarCaja7', '<bean:write name="procesoBalanceAdapterVO" property="balance.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"	align="left" />
				</td>
			</tr>
		</table>
		</logic:equal>
		<!-- Fin Detalle Caja7 -->
		</fieldset>
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.procesarBalanceAdapter.paso.title"/></legend>			
			<table class="tabladatos" width="100%">
			<tr>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.fecha.label"/>: </label></td>
				<td class="normal"><bean:write name="procesoBalanceAdapterVO" property="pasoCorrida5.fechaCorridaView"/></td>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.hora.label"/>: </label></td>
				<td class="normal"><bean:write name="procesoBalanceAdapterVO" property="pasoCorrida5.horaCorridaView"/></td>
			</tr>	
			<tr>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.observacion.label"/>: </label></td>
				<td colspan="2" class="normal"><bean:write name="procesoBalanceAdapterVO" property="pasoCorrida5.observacion"/></td>
			</tr>	
			<tr>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.estado.label"/>: </label></td>
				<td colspan="2" class="normal"><bean:write name="procesoBalanceAdapterVO" property="pasoCorrida5.estadoCorrida.desEstadoCorrida"/></td>
			</tr>	
			</table>
			<logic:equal name="procesoBalanceAdapterVO" property="paramPaso" value="5">
			<table class="tablabotones" width="100%">
			<tr>				
		    	<td align="center">
					<logic:equal name="procesoBalanceAdapterVO" property="paramActivar" value="true">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');" disabled='false'>
							<bean:message bundle="pro" key="pro.abm.button.activar"/>
						</html:button>&nbsp;&nbsp;
					</logic:equal>
					<logic:equal name="procesoBalanceAdapterVO" property="paramActivar" value="false">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');" disabled='true'>
							<bean:message bundle="pro" key="pro.abm.button.activar"/>
						</html:button>&nbsp;&nbsp;
					</logic:equal>
					<logic:equal name="procesoBalanceAdapterVO" property="paramRetroceder" value="true">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('retroceder', '');" disabled='false'>
							<bean:message bundle="pro" key="pro.abm.button.retroceder"/>
						</html:button>&nbsp;&nbsp;	
					</logic:equal>			
					<logic:equal name="procesoBalanceAdapterVO" property="paramRetroceder" value="false">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('retroceder', '');" disabled='true'>
							<bean:message bundle="pro" key="pro.abm.button.retroceder"/>
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
		<!-- Fin Caja7 Finales  -->		

		<!-- Reportes del Balance  -->		
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.procesarBalanceAdapter.fieldset10"/></legend>			
			<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.procesoBalanceAdapter.paso5.reportes"/></caption>
	    	<tbody>
				<logic:notEmpty  name="procesoBalanceAdapterVO" property="listFileCorrida5">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Adjunto -->
						<th align="left"><bean:message bundle="base" key="base.nombre.label"/></th>
						<th align="left"><bean:message bundle="base" key="base.descripcion.label"/></th>						
					</tr>
					<logic:iterate id="FileCorridaVO" name="procesoBalanceAdapterVO" property="listFileCorrida5">
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
				<logic:empty  name="procesoBalanceAdapterVO" property="listFileCorrida5">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			</tbody>
			</table>
		</fieldset>
		<!-- Fin Reportes del Balance  -->		
		
		<!-- 6- Afectar maestro de Rentas  -->		
		<logic:equal name="procesoBalanceAdapterVO" property="paramPaso" value="6">
			<a name="pasoActual">&nbsp;</a> 
		</logic:equal>		
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.procesarBalanceAdapter.fieldset11"/></legend>			
			<table class="tabladatos" width="100%">
			<tr>
				<td class="normal" colspan="4" align="left"><bean:message bundle="bal" key="bal.procesoBalanceAdapter.paso6.description"/></td>
			</tr>
			<tr>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.fecha.label"/>: </label></td>
				<td class="normal"><bean:write name="procesoBalanceAdapterVO" property="pasoCorrida6.fechaCorridaView"/></td>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.hora.label"/>: </label></td>
				<td class="normal"><bean:write name="procesoBalanceAdapterVO" property="pasoCorrida6.horaCorridaView"/></td>
			</tr>	
			<tr>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.observacion.label"/>: </label></td>
				<td colspan="2" class="normal"><bean:write name="procesoBalanceAdapterVO" property="pasoCorrida6.observacion"/></td>
			</tr>	
			<tr>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.estado.label"/>: </label></td>
				<td colspan="2" class="normal"><bean:write name="procesoBalanceAdapterVO" property="pasoCorrida6.estadoCorrida.desEstadoCorrida"/></td>
			</tr>	
			</table>
			<logic:equal name="procesoBalanceAdapterVO" property="paramPaso" value="6">
			<table class="tablabotones" width="100%">
			<tr>				
		    	<td align="center">
					<logic:equal name="procesoBalanceAdapterVO" property="paramActivar" value="true">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');" disabled='false'>
							<bean:message bundle="pro" key="pro.abm.button.activar"/>
						</html:button>&nbsp;&nbsp;
					</logic:equal>
					<logic:equal name="procesoBalanceAdapterVO" property="paramActivar" value="false">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');" disabled='true'>
							<bean:message bundle="pro" key="pro.abm.button.activar"/>
						</html:button>&nbsp;&nbsp;
					</logic:equal>
					<logic:equal name="procesoBalanceAdapterVO" property="paramRetroceder" value="true">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('retroceder', '');" disabled='false'>
							<bean:message bundle="pro" key="pro.abm.button.retroceder"/>
						</html:button>&nbsp;&nbsp;	
					</logic:equal>			
					<logic:equal name="procesoBalanceAdapterVO" property="paramRetroceder" value="false">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('retroceder', '');" disabled='true'>
							<bean:message bundle="pro" key="pro.abm.button.retroceder"/>
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
		<!-- Fin Afectar maestro de Rentas  -->		
		
		<!-- Reportes -->		
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.procesarBalanceAdapter.fieldset12"/></legend>			
			<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.procesoBalanceAdapter.paso6.reportes"/></caption>
	    	<tbody>
				<logic:notEmpty  name="procesoBalanceAdapterVO" property="listFileCorrida6">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Adjunto -->
						<th align="left"><bean:message bundle="base" key="base.nombre.label"/></th>
						<th align="left"><bean:message bundle="base" key="base.descripcion.label"/></th>						
					</tr>
					<logic:iterate id="FileCorridaVO" name="procesoBalanceAdapterVO" property="listFileCorrida6">
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
				<logic:empty  name="procesoBalanceAdapterVO" property="listFileCorrida6">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			</tbody>
			</table>
		</fieldset>
		<!-- Fin Reportes  -->	
		
		<!-- 7 - Actualizar Indeterminados y Guardar Saldos A Favor  -->		
		<logic:equal name="procesoBalanceAdapterVO" property="paramPaso" value="7">
			<a name="pasoActual">&nbsp;</a> 
		</logic:equal>		
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.procesarBalanceAdapter.fieldset13"/></legend>			
			<table class="tabladatos" width="100%">
			<tr>
				<td class="normal" colspan="4" align="left"><bean:message bundle="bal" key="bal.procesoBalanceAdapter.paso7.description"/></td>
			</tr>
			<tr>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.fecha.label"/>: </label></td>
				<td class="normal"><bean:write name="procesoBalanceAdapterVO" property="pasoCorrida7.fechaCorridaView"/></td>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.hora.label"/>: </label></td>
				<td class="normal"><bean:write name="procesoBalanceAdapterVO" property="pasoCorrida7.horaCorridaView"/></td>
			</tr>	
			<tr>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.observacion.label"/>: </label></td>
				<td colspan="2" class="normal"><bean:write name="procesoBalanceAdapterVO" property="pasoCorrida7.observacion"/></td>
			</tr>	
			<tr>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.pasoCorrida.estado.label"/>: </label></td>
				<td colspan="2" class="normal"><bean:write name="procesoBalanceAdapterVO" property="pasoCorrida7.estadoCorrida.desEstadoCorrida"/></td>
			</tr>	
			</table>
			<logic:equal name="procesoBalanceAdapterVO" property="paramPaso" value="7">
			<table class="tablabotones" width="100%">
			<tr>				
		    	<td align="center">
					<logic:equal name="procesoBalanceAdapterVO" property="paramActivar" value="true">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');" disabled='false'>
							<bean:message bundle="pro" key="pro.abm.button.activar"/>
						</html:button>&nbsp;&nbsp;
					</logic:equal>
					<logic:equal name="procesoBalanceAdapterVO" property="paramActivar" value="false">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');" disabled='true'>
							<bean:message bundle="pro" key="pro.abm.button.activar"/>
						</html:button>&nbsp;&nbsp;
					</logic:equal>
					<logic:equal name="procesoBalanceAdapterVO" property="paramRetroceder" value="true">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('retroceder', '');" disabled='false'>
							<bean:message bundle="pro" key="pro.abm.button.retroceder"/>
						</html:button>&nbsp;&nbsp;	
					</logic:equal>			
					<logic:equal name="procesoBalanceAdapterVO" property="paramRetroceder" value="false">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('retroceder', '');" disabled='true'>
							<bean:message bundle="pro" key="pro.abm.button.retroceder"/>
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
		<!-- Fin Afectar maestro de Rentas  -->		
		
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

<logic:notEqual name="procesoBalanceAdapterVO" property="paramPaso" value="">
	<script type="text/javascript">irAPasoActual();</script>
</logic:notEqual>
