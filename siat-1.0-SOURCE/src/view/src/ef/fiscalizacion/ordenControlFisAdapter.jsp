<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/AdministrarOrdenControlFis.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="ef" key="ef.ordenControlFisViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- OrdenControlFis -->
		<bean:define id="ordenControlFis" name="ordenControlFisAdapterVO" property="ordenControl"/>
		<%@include file="/ef/fiscalizacion/includeOrdenControlFisView.jsp" %>
		<!-- OrdenControlFis -->
	
		<!-- Periodos Seleccionados -->
		<fieldset>
			<legend
				<logic:empty name="ordenControlFisAdapterVO" property="ordenControl.listPeriodoOrden"> 
					style="background-color: #a7aaaa;"
				</logic:empty>
			>		
				<a onclick="toggle(this, 'bloquePeriodos')" style="cursor: pointer;"> (+) &nbsp; </a> 
				<bean:message bundle="ef" key="ef.periodoOrden.legend"/>
			</legend>
			
			<span id="bloquePeriodos" style="display:none">
			
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="ef" key="ef.periodoOrden.title"/></caption>				
	              	<tbody>
						<logic:notEmpty name="ordenControlFisAdapterVO" property="ordenControl.listPeriodoOrden">
			               	<tr>
								<th width="1">&nbsp;</th> <!-- Eliminar -->
								<th><bean:message bundle="def" key="def.recurso.label"/></th> <!-- recurso -->
								<th><bean:message bundle="pad" key="pad.cuenta.label"/></th> <!-- cuenta -->
								<th><bean:message bundle="ef" key="ef.ordenControlFisAdapter.periodoOrden.label"/></th> <!-- periodo/anio -->																				
								<!-- <#ColumnTitles#> -->
							</tr>
							
							<logic:iterate id="PeriodoOrdenVO" name="ordenControlFisAdapterVO" property="ordenControl.listPeriodoOrden">
								<tr>
									<!-- eliminar -->
									<td>
										<logic:equal name="ordenControlFisAdapterVO" property="eliminarPeriodoEnabled" value="enabled">
											<logic:equal name="PeriodoOrdenVO" property="eliminarEnabled" value="enabled">																		
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarPeriodoOrden', '<bean:write name="PeriodoOrdenVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>
											</logic:equal>
										</logic:equal>	
										<logic:notEqual name="ordenControlFisAdapterVO" property="eliminarPlaFueDatEnabled" value="enabled">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>																			
									</td>

									<!-- recurso -->
									<td><bean:write name="PeriodoOrdenVO" property="ordConCue.cuenta.recurso.desRecurso"/></td>
									
									<!-- cuenta -->
									<td><bean:write name="PeriodoOrdenVO" property="ordConCue.cuenta.numeroCuenta"/></td>
									
									<!-- periodo - anio -->
									<td>
										<bean:write name="PeriodoOrdenVO" property="periodoView"/>/
										<bean:write name="PeriodoOrdenVO" property="anioView"/>
									</td>														
								</tr>
							</logic:iterate>
						</logic:notEmpty>
						<logic:empty name="ordenControlFisAdapterVO" property="ordenControl.listPeriodoOrden">
								<tr><td align="center">
									<bean:message bundle="ef" key="ef.ordenControlFisAdapter.listPeriodoOrden.vacio"/>
								</td></tr>				
						</logic:empty>																			
					</tbody>
				</table>
				<p align="right">
					<logic:equal name="ordenControlFisAdapterVO" property="agregarBussEnabled" value="true">
						<bean:define id="agregarPeriodoEnabled" name="ordenControlFisAdapterVO" property="agregarPeriodoEnabled"/>					
			   	    	<input type="button" <%=agregarPeriodoEnabled%> class="boton" onclick="submitForm('agregarPeriodoOrden', '');"
			   	    		value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
			   	    </logic:equal>		
				</p>
			</span>		
		</fieldset>
		<!-- Fin Periodos Seleccionados -->
					
		<!-- Actas -->
		<fieldset>
			<legend
				<logic:empty name="ordenControlFisAdapterVO" property="ordenControl.listActa"> 
					style="background-color: #a7aaaa;"
				</logic:empty>
			>		

				<a onclick="toggle(this, 'bloqueActas')" style="cursor: pointer;"> (+) &nbsp; </a> 
				<bean:message bundle="ef" key="ef.ordenControlFisAdapter.listActa.legend"/>
			</legend>
			
			<span id="bloqueActas" style="display:none">
			
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="ef" key="ef.ordenControlFisAdapter.listActa.title"/></caption>				
	              	<tbody>
						<logic:notEmpty name="ordenControlFisAdapterVO" property="ordenControl.listActa">
			               	<tr>
			               		<th width="1">&nbsp;</th> <!-- Ver -->
			               		<th width="1">&nbsp;</th> <!-- modificar -->
								<th width="1">&nbsp;</th> <!-- Eliminar -->
								<th><bean:message bundle="ef" key="ef.tipoActa.label"/></th> <!-- tipoActa -->
								<th><bean:message bundle="ef" key="ef.acta.nroActa.label"/></th> <!-- nroActa -->
								<!-- <#ColumnTitles#> -->
							</tr>
							
							<logic:iterate id="ActaVO" name="ordenControlFisAdapterVO" property="ordenControl.listActa">
								<tr>
									<!-- ver -->
									<td>
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verActa', '<bean:write name="ActaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>									
									</td>
									
									<!-- modificar -->
									<td>
										<logic:equal name="ordenControlFisAdapterVO" property="modificarPlaFueDatEnabled" value="enabled">
											<logic:equal name="ActaVO" property="modificarEnabled" value="enabled">																		
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarActa', '<bean:write name="ActaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>
											</logic:equal>
										</logic:equal>	
										<logic:notEqual name="ordenControlFisAdapterVO" property="modificarPlaFueDatEnabled" value="enabled">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>									
									</td>
									
									<!-- eliminar -->
									<td>
										<logic:equal name="ordenControlFisAdapterVO" property="eliminarPlaFueDatEnabled" value="enabled">
											<logic:equal name="ActaVO" property="eliminarEnabled" value="enabled">																		
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarActa', '<bean:write name="ActaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>
											</logic:equal>
										</logic:equal>	
										<logic:notEqual name="ordenControlFisAdapterVO" property="eliminarPlaFueDatEnabled" value="enabled">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>																			
									</td>
																																		
									<!-- tipoActa -->
									<td><bean:write name="ActaVO" property="tipoActa.desTipoActa"/></td>
									
									<!-- nroActa -->
									<td><bean:write name="ActaVO" property="numeroActaView"/></td>
									
								</tr>
							</logic:iterate>
						</logic:notEmpty>
						<logic:empty name="ordenControlFisAdapterVO" property="ordenControl.listActa">
								<tr><td align="center">
									<bean:message bundle="ef" key="ef.ordenControlFisAdapter.listActa.vacio"/>
								</td></tr>				
						</logic:empty>																			
					</tbody>
				</table>
				<p align="right">
					<bean:define id="agregarActaEnabled" name="ordenControlFisAdapterVO" property="agregarActaEnabled"/>					
		   	    	<input type="button" <%=agregarActaEnabled%> class="boton" onclick="submitForm('agregarActa', '');"
		   	    		value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
				</p>
			</span>		
		</fieldset>		
		<!-- Fin Actas -->	
		
		<!-- Inicio investigacion -->
		<fieldset>
			<legend
				<logic:equal name="ordenControlFisAdapterVO" property="inicioInv.id" value="0"> 
					style="background-color: #a7aaaa;"
				</logic:equal>
			>		

				<a onclick="toggle(this, 'bloqueInicioInv')" style="cursor: pointer;"> (+) &nbsp; </a> 
				<bean:message bundle="ef" key="ef.inicioInv.legend"/>
			</legend>
			
			<span id="bloqueInicioInv" style="display:none">

					<logic:lessEqual name="ordenControlFisAdapterVO" property="inicioInv.id" value="0">
						<p><bean:message bundle="ef" key="ef.ordenControlFisAdapter.inicioInv.vacio"/></p>					
						<bean:define id="agregarInicioInvEnabled" name="ordenControlFisAdapterVO" property="agregarInicioInvEnabled"/>					
			   	    	<p align="right"><input type="button" <%=agregarInicioInvEnabled%> class="boton" onclick="submitForm('agregarInicioInv', '');"
			   	    		value="<bean:message bundle="base" key="abm.button.agregar"/>"/></p>
					</logic:lessEqual>
					
					<logic:greaterThan name="ordenControlFisAdapterVO" property="inicioInv.id" value="0" value="">
						<p><bean:write name="ordenControlFisAdapterVO" property="inicioInv.detalle"/></p>
						<bean:define id="modificarInicioInvEnabled" name="ordenControlFisAdapterVO" property="modificarInicioInvEnabled"/>					
			   	    	<p align="right"><input type="button" <%=modificarInicioInvEnabled%> class="boton" onclick="submitForm('modificarInicioInv', '<bean:write name="ordenControlFisAdapterVO" property="inicioInv.id" bundle="base" formatKey="general.format.id"/>');"
			   	    		value="<bean:message bundle="base" key="abm.button.modificar"/>"/></p>
					</logic:greaterThan>
				
			</span>
		</fieldset>			
		<!-- FIN Inicio investigacion -->
		
		<!-- Informacion de Ingresos (planillas) -->
		<fieldset>
			<legend
				<logic:empty name="ordenControlFisAdapterVO" property="ordenControl.listPlaFueDat"> 
					style="background-color: #a7aaaa;"
				</logic:empty>
			>
				<a onclick="toggle(this, 'bloquePlaFueDat')" style="cursor: pointer;"> (+) &nbsp; </a> 
				<bean:message bundle="ef" key="ef.plaFueDat.legend"/>
			</legend>
			
			<span id="bloquePlaFueDat" style="display:none">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="ef" key="ef.plaFueDat.title"/></caption>				
	              	<tbody>
						<logic:notEmpty name="ordenControlFisAdapterVO" property="ordenControl.listPlaFueDat">
			               	<tr>
			               		<th width="1">&nbsp;</th> <!-- modificar -->
								<th width="1">&nbsp;</th> <!-- Eliminar -->			               	
								<th><bean:message bundle="ef" key="ef.fuenteInfo.label"/></th> <!-- fuente -->
								<th><bean:message bundle="ef" key="ef.plaFueDat.observacion.label"/></th> <!-- Obs -->
								<th><bean:message bundle="ef" key="ef.plaFueDat.total.label"/></th> <!-- Total -->
								<!-- <#ColumnTitles#> -->
							</tr>
							
							<logic:iterate id="PlaFueDatVO" name="ordenControlFisAdapterVO" property="ordenControl.listPlaFueDat">
								<tr>		
																	
									<!-- modificar -->
									<td>
										<logic:equal name="ordenControlFisAdapterVO" property="modificarPlaFueDatEnabled" value="enabled">
											<logic:equal name="PlaFueDatVO" property="modificarEnabled" value="enabled">																		
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarPlaFueDat', '<bean:write name="PlaFueDatVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>
											</logic:equal>
										</logic:equal>	
										<logic:notEqual name="ordenControlFisAdapterVO" property="modificarPlaFueDatEnabled" value="enabled">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>									
									</td>
									
									<!-- eliminar -->
									<td>
										<logic:equal name="ordenControlFisAdapterVO" property="eliminarPlaFueDatEnabled" value="enabled">
											<logic:equal name="PlaFueDatVO" property="eliminarEnabled" value="enabled">																		
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarPlaFueDat', '<bean:write name="PlaFueDatVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>
											</logic:equal>
										</logic:equal>	
										<logic:notEqual name="ordenControlFisAdapterVO" property="eliminarPlaFueDatEnabled" value="enabled">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>																			
									</td>
															
									<td><bean:write name="PlaFueDatVO" property="fuenteInfo.nombreFuente"/></td>
									
									<td><bean:write name="PlaFueDatVO" property="observacion"/></td>
									
									<td><bean:write name="PlaFueDatVO" property="total" bundle="base" formatKey="general.format.currency"/></td>
									
								</tr>
							</logic:iterate>
						</logic:notEmpty>
						<logic:empty name="ordenControlFisAdapterVO" property="ordenControl.listPlaFueDat">
								<tr><td align="center">
									<bean:message bundle="ef" key="ef.ordenControlFisAdapter.listPlaFueDat.vacio"/>
								</td></tr>				
						</logic:empty>																			
					</tbody>
				</table>
				<p align="right">
						<bean:define id="agregarPlaFueDatEnabled" name="ordenControlFisAdapterVO" property="agregarPlaFueDatEnabled"/>					
			   	    	<input type="button" <%=agregarPlaFueDatEnabled%> class="boton" onclick="submitForm('agregarPlaFueDat', '');"
			   	    		value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
				</p>
				
			</span>
		</fieldset>		
		<!-- FIN Informacion de Ingresos -->
					
		<!-- Comparaciones -->
		<fieldset>
			<legend
				<logic:empty name="ordenControlFisAdapterVO" property="ordenControl.listComparacion"> 
					style="background-color: #a7aaaa;"
				</logic:empty>
			>

				<a onclick="toggle(this, 'bloqueComparaciones')" style="cursor: pointer;"> (+) &nbsp; </a> 
				<bean:message bundle="ef" key="ef.comparacion.legend"/>
			</legend>
			
			<span id="bloqueComparaciones" style="display:none">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="ef" key="ef.comparacion.title"/></caption>				
	              	<tbody>
						<logic:notEmpty name="ordenControlFisAdapterVO" property="ordenControl.listComparacion">
			               	<tr>
			               		<th width="1">&nbsp;</th> <!-- ver -->
			               		<th width="1">&nbsp;</th> <!-- modificar -->
								<th width="1">&nbsp;</th> <!-- Eliminar -->			               	
								<th><bean:message bundle="ef" key="ef.comparacion.descripcion.label"/></th>
								<th><bean:message bundle="ef" key="ef.comparacion.fecha.label"/></th>								
								<!-- <#ColumnTitles#> -->
							</tr>
							
							<logic:iterate id="ComparacionVO" name="ordenControlFisAdapterVO" property="ordenControl.listComparacion">
								<tr>		
											
									<!-- ver -->
									<td>
										<logic:equal name="ordenControlFisAdapterVO" property="eliminarComparacionEnabled" value="enabled">
											<logic:equal name="ComparacionVO" property="eliminarEnabled" value="enabled">									
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verComparacion', '<bean:write name="ComparacionVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
												</a>									
											</logic:equal>
										</logic:equal>
										<logic:notEqual name="ordenControlFisAdapterVO" property="eliminarComparacionEnabled" value="enabled">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>		
									</td>
																	
									<!-- modificar -->
									<td>
										<logic:equal name="ordenControlFisAdapterVO" property="modificarComparacionEnabled" value="enabled">
											<logic:equal name="ComparacionVO" property="modificarEnabled" value="enabled">																		
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarComparacion', '<bean:write name="ComparacionVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>
											</logic:equal>
										</logic:equal>	
										<logic:notEqual name="ordenControlFisAdapterVO" property="modificarComparacionEnabled" value="enabled">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</td>
									
									<!-- eliminar -->
									<td>
										<logic:equal name="ordenControlFisAdapterVO" property="eliminarComparacionEnabled" value="enabled">
											<logic:equal name="ComparacionVO" property="eliminarEnabled" value="enabled">																											
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarComparacion', '<bean:write name="ComparacionVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>
											</logic:equal>
										</logic:equal>	
										<logic:notEqual name="ordenControlFisAdapterVO" property="eliminarComparacionEnabled" value="enabled">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>										
									</td>
															
									<td><bean:write name="ComparacionVO" property="descripcion"/></td>
									
									<td><bean:write name="ComparacionVO" property="fechaView"/></td>
									
								</tr>
							</logic:iterate>
						</logic:notEmpty>
						<logic:empty name="ordenControlFisAdapterVO" property="ordenControl.listComparacion">
								<tr><td align="center">
									<bean:message bundle="ef" key="ef.ordenControlFisAdapter.listComparacion.vacio"/>
								</td></tr>				
						</logic:empty>																			
					</tbody>
				</table>
				<p align="right">
						<bean:define id="agregarComparacionEnabled" name="ordenControlFisAdapterVO" property="agregarComparacionEnabled"/>					
			   	    	<input type="button" <%=agregarComparacionEnabled%> class="boton" onclick="submitForm('agregarComparacion', '');"
			   	    		value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
				</p>
				
			</span>
		</fieldset>	
		<!-- FIN Comparaciones -->	
		
		<!-- Bases imponibles -->
		<fieldset>
			<legend
				<logic:empty name="ordenControlFisAdapterVO" property="ordenControl.listOrdConBasImp"> 
					style="background-color: #a7aaaa;"
				</logic:empty>
			>

				<a onclick="toggle(this, 'bloqueOrdConBasImp')" style="cursor: pointer;"> (+) &nbsp; </a> 
				<bean:message bundle="ef" key="ef.ordConBasImp.legend"/>
			</legend>
			
			<span id="bloqueOrdConBasImp" style="display:none">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="ef" key="ef.ordConBasImp.title"/></caption>				
	              	<tbody>
						<logic:notEmpty name="ordenControlFisAdapterVO" property="ordenControl.listOrdConBasImp">
			               	<tr>
			               		<th width="1">&nbsp;</th> 
			               		<th width="1">&nbsp;</th> 
								<th width="1">&nbsp;</th>
								<th><bean:message bundle="pad" key="pad.cuenta.label"/></th> 			               	
								<th><bean:message bundle="ef" key="ef.ordConBasImp.periodoDesde.label"/></th>
								<th><bean:message bundle="ef" key="ef.ordConBasImp.periodoHasta.label"/></th>
								<th><bean:message bundle="ef" key="ef.fuenteInfo.label"/></th>
								<th><bean:message bundle="ef" key="ef.compFuente.total.label"/></th>
								<!-- <#ColumnTitles#> -->
							</tr>
							
							<logic:iterate id="OrdConBasImpVO" name="ordenControlFisAdapterVO" property="ordenControl.listOrdConBasImp">
								<tr>		
										
									<!-- cargar ajustes -->		
									<td>
										<logic:equal name="ordenControlFisAdapterVO" property="cargarAjuOrdConBasImpEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('cargarAjustesOrdConBasImp', '<bean:write name="OrdConBasImpVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="ef" key="ef.ordenControlFisViewAdapter.button.cargarAjustes"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>											
										</logic:equal>	
										<logic:notEqual name="ordenControlFisAdapterVO" property="cargarAjuOrdConBasImpEnabled" value="enabled">
											<img title="<bean:message bundle="ef" key="ef.ordenControlFisViewAdapter.button.cargarAjustes"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</td>
									
									<!-- eliminar -->
									<td>
										<logic:equal name="ordenControlFisAdapterVO" property="eliminarOrdConBasImpEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarOrdConBasImp', '<bean:write name="OrdConBasImpVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>											
										</logic:equal>	
										<logic:notEqual name="ordenControlFisAdapterVO" property="eliminarOrdConBasImpEnabled" value="enabled">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
										
									</td>
									
									<!-- convenio multilateral -->									
									<td>
										<logic:equal name="ordenControlFisAdapterVO" property="cargarAlicuotasOrdConBasImpEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('cargarAlicuotasOrdConBasImp', '<bean:write name="OrdConBasImpVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="ef" key="ef.ordenControlFisViewAdapter.button.cargarAlicuotas"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/porcent0.gif"/>
												</a>											
										</logic:equal>	
										<logic:notEqual name="ordenControlFisAdapterVO" property="cargarAlicuotasOrdConBasImpEnabled" value="enabled">
											<img title="<bean:message bundle="base" key="ef.ordenControlFisViewAdapter.button.cargarAlicuotas"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/porcent1.gif"/>
										</logic:notEqual>									
									</td>
									
									<td><bean:write name="OrdConBasImpVO" property="ordConCue.cuenta.recursoCuentaView"/></td>
															
									<td><bean:write name="OrdConBasImpVO" property="periodoAnioDesdeView"/></td>
									
									<td><bean:write name="OrdConBasImpVO" property="periodoAnioHastaView"/></td>
									
									<td><bean:write name="OrdConBasImpVO" property="compFuente.plaFueDat.tituloView"/></td>
									
									<td><bean:write name="OrdConBasImpVO" property="compFuente.totalView"/></td>
								</tr>
							</logic:iterate>
						</logic:notEmpty>
						<logic:empty name="ordenControlFisAdapterVO" property="ordenControl.listOrdConBasImp">
								<tr><td align="center">
									<bean:message bundle="ef" key="ef.ordenControlFisAdapter.listOrdConBasImp.vacio"/>
								</td></tr>				
						</logic:empty>																			
					</tbody>
				</table>
				<p align="right">
						<bean:define id="agregarOrdConBasImpEnabled" name="ordenControlFisAdapterVO" property="agregarOrdConBasImpEnabled"/>					
			   	    	<input type="button" <%=agregarOrdConBasImpEnabled%> class="boton" onclick="submitForm('agregarOrdConBasImp', '');"
			   	    		value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
				</p>
				
			</span>
		</fieldset>
		<!-- FIN Bases imponibles -->
						
		<!-- Ajustes -->
		<fieldset>
			<legend
				<logic:empty name="ordenControlFisAdapterVO" property="ordenControl.listDetAju"> 
					style="background-color: #a7aaaa;"
				</logic:empty>
			>
				<a onclick="toggle(this, 'bloqueAjustes')" style="cursor: pointer;"> (+) &nbsp; </a> 
				<bean:message bundle="ef" key="ef.detAju.legend"/>
			</legend>
			
			<span id="bloqueAjustes" style="display:none">
			
				<div class="horizscroll">
				
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="ef" key="ef.detAju.title"/></caption>				
	              	<tbody>
						<logic:notEmpty name="ordenControlFisAdapterVO" property="ordenControl.listDetAju">
			               	<tr>
			               		<th width="1">&nbsp;</th>
			               		<th width="1">&nbsp;</th>
								<th><bean:message bundle="pad" key="pad.cuenta.label"/></th>
								<th><bean:message bundle="def" key="def.recurso.label"/></th>
								<th><bean:message bundle="ef" key="ef.detAju.fecha.label"/></th>
							</tr>
							
							<logic:iterate id="DetAjuVO" name="ordenControlFisAdapterVO" property="ordenControl.listDetAju">
								<tr>		
										
									<!-- modificar -->		
									<td>
										<logic:equal name="ordenControlFisAdapterVO" property="modificarDetAjuEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarDetAju', '<bean:write name="DetAjuVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="ef" key="ef.ordenControlFisViewAdapter.button.modificarDetAju"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>											
										</logic:equal>	
										<logic:notEqual name="ordenControlFisAdapterVO" property="modificarDetAjuEnabled" value="enabled">
											<img title="<bean:message bundle="ef" key="ef.ordenControlFisViewAdapter.button.modificarDetAju"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</td>
									<td>
										<logic:equal name="ordenControlFisAdapterVO" property="eliminarDetAjuEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarDetAju', '<bean:write name="DetAjuVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="ef" key="ef.ordenControlFisViewAdapter.button.eliminarDetAju"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>											
										</logic:equal>	
										<logic:notEqual name="ordenControlFisAdapterVO" property="eliminarDetAjuEnabled" value="enabled">
											<img title="<bean:message bundle="ef" key="ef.ordenControlFisViewAdapter.button.eliminarDetAju"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</td>
																								
									<td><bean:write name="DetAjuVO" property="ordConCue.cuenta.numeroCuenta"/></td>
									
									<td><bean:write name="DetAjuVO" property="ordConCue.cuenta.recurso.desRecurso"/></td>
									
									<td><bean:write name="DetAjuVO" property="fechaView"/></td>
									
								</tr>
							</logic:iterate>
						</logic:notEmpty>
						<logic:empty name="ordenControlFisAdapterVO" property="ordenControl.listDetAju">
								<tr><td align="center">
									<bean:message bundle="ef" key="ef.ordenControlFisAdapter.listDetAju.vacio"/>
								</td></tr>				
						</logic:empty>																			
					</tbody>
				</table>
				
				</div>
				
				<p align="right">
						<bean:define id="agregarDetAjuEnabled" name="ordenControlFisAdapterVO" property="agregarDetAjuEnabled"/>					
			   	    	<input type="button" <%=agregarDetAjuEnabled%> class="boton" onclick="submitForm('agregarDetAju', '');"
			   	    		value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
				</p>
				
			</span>
		</fieldset>
		<!-- FIN Ajustes -->
		
		<!-- Compensaciones -->
		<fieldset>
			<legend
				<logic:empty name="ordenControlFisAdapterVO" property="ordenControl.listComAju"> 
					style="background-color: #a7aaaa;"
				</logic:empty>
			>
				<a onclick="toggle(this, 'bloqueCompensaciones')" style="cursor: pointer;"> (+) &nbsp; </a> 
				<bean:message bundle="ef" key="ef.comAju.legend"/>
			</legend>
			
			<span id="bloqueCompensaciones" style="display:none">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="ef" key="ef.comAju.title"/></caption>				
	              	<tbody>
						<logic:notEmpty name="ordenControlFisAdapterVO" property="ordenControl.listComAju">
			               	<tr>
			               		<th width="1">&nbsp;</th>
			               		<th width="1">&nbsp;</th>
			               		<th width="1">&nbsp;</th>
								<th><bean:message bundle="pad" key="pad.cuenta.label"/></th>
								<th><bean:message bundle="def" key="def.recurso.label"/></th>
								<th><bean:message bundle="ef" key="ef.comAju.fechaAplicacion.label"/></th>
							</tr>
							
							<logic:iterate id="ComAjuVO" name="ordenControlFisAdapterVO" property="ordenControl.listComAju">
								<tr>		
									<!-- ver -->
									<td>
										<logic:equal name="ordenControlFisAdapterVO" property="eliminarComAjuEnabled" value="enabled">
											<logic:equal name="ComAjuVO" property="eliminarEnabled" value="enabled">									
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verComAju', '<bean:write name="ComAjuVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
												</a>									
											</logic:equal>
										</logic:equal>
										<logic:notEqual name="ordenControlFisAdapterVO" property="eliminarComparacionEnabled" value="enabled">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>		
									</td>
									
									<!-- modificar -->		
									<td>
										<logic:equal name="ordenControlFisAdapterVO" property="modificarComAjuEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarComAju', '<bean:write name="ComAjuVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>											
										</logic:equal>	
										<logic:notEqual name="ordenControlFisAdapterVO" property="modificarComAjuEnabled" value="enabled">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</td>
													
									<!-- eliminar -->
									<td>
										<logic:equal name="ordenControlFisAdapterVO" property="eliminarComAjuEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarComAju', '<bean:write name="ComAjuVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>											
										</logic:equal>	
										<logic:notEqual name="ordenControlFisAdapterVO" property="eliminarOrdConBasImpEnabled" value="enabled">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
										
									</td>
									<td><bean:write name="ComAjuVO" property="detAju.ordConCue.cuenta.numeroCuenta"/></td>
									
									<td><bean:write name="ComAjuVO" property="detAju.ordConCue.cuenta.recurso.desRecurso"/></td>
									
									<td><bean:write name="ComAjuVO" property="fechaAplicacionView"/></td>
									
								</tr>
							</logic:iterate>
						</logic:notEmpty>
						<logic:empty name="ordenControlFisAdapterVO" property="ordenControl.listComAju">
								<tr><td align="center">
									<bean:message bundle="ef" key="ef.ordenControlFisAdapter.listComAju.vacio"/>
								</td></tr>				
						</logic:empty>																			
					</tbody>
				</table>
				<p align="right">
						<bean:define id="agregarComAjuEnabled" name="ordenControlFisAdapterVO" property="agregarComAjuEnabled"/>					
			   	    	<input type="button" <%=agregarComAjuEnabled%> class="boton" onclick="submitForm('agregarComAju', '');"
			   	    		value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
				</p>
				
			</span>
		</fieldset>
		<!-- FIN Compensaciones -->
		
		<!-- Informes y Resoluciones (DetAjuDocSop) -->
		<fieldset>
			<legend
				<logic:empty name="ordenControlFisAdapterVO" property="ordenControl.listDetAjuDocSop"> 
					style="background-color: #a7aaaa;"
				</logic:empty>
			>

				<a onclick="toggle(this, 'bloqueDetAjuDocSop')" style="cursor: pointer;"> (+) &nbsp; </a> 
				<bean:message bundle="ef" key="ef.detAjuDocSop.legend"/>
			</legend>
			
			<span id="bloqueDetAjuDocSop" style="display:none">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="ef" key="ef.detAjuDocSop.title"/></caption>				
	              	<tbody>
						<logic:notEmpty name="ordenControlFisAdapterVO" property="ordenControl.listDetAjuDocSop">
			               	<tr>
			               		<th width="1">&nbsp;</th>
			               		<th width="1">&nbsp;</th>
			               		<th width="1">&nbsp;</th>
								<th><bean:message bundle="ef" key="ef.detAjuDocSop.fechaGenerada.label"/></th>
								<th><bean:message bundle="ef" key="ef.docSop.label"/></th>
								<th><bean:message bundle="ef" key="ef.detAjuDocSop.observacion.label"/></th>
							</tr>
							
							<logic:iterate id="DetAjuDocSopVO" name="ordenControlFisAdapterVO" property="ordenControl.listDetAjuDocSop">
								<tr>		
										
									<!-- ver -->		
									<td>
										<logic:equal name="ordenControlFisAdapterVO" property="verDetAjuDocSopEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verDetAjuDocSop', '<bean:write name="DetAjuDocSopVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
												</a>											
										</logic:equal>	
										<logic:notEqual name="ordenControlFisAdapterVO" property="verDetAjuDocSopEnabled" value="enabled">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</td>
									
									<!-- modificar -->
									<td>
										<logic:equal name="ordenControlFisAdapterVO" property="modificarDetAjuDocSopEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarDetAjuDocSop', '<bean:write name="DetAjuDocSopVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>											
										</logic:equal>	
										<logic:notEqual name="ordenControlFisAdapterVO" property="modificarDetAjuDocSopEnabled" value="enabled">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</td>
									
									<!-- eliminar -->
									<td>
										<logic:equal name="ordenControlFisAdapterVO" property="eliminarDetAjuDocSopEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarDetAjuDocSop', '<bean:write name="DetAjuDocSopVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>											
										</logic:equal>	
										<logic:notEqual name="ordenControlFisAdapterVO" property="eliminarDetAjuDocSopEnabled" value="enabled">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</td>
																								
									<td><bean:write name="DetAjuDocSopVO" property="fechaGeneradaView"/></td>

									<td><bean:write name="DetAjuDocSopVO" property="docSop.desDocSop"/></td>
									
									<td><bean:write name="DetAjuDocSopVO" property="observacion"/></td>									
									
								</tr>
							</logic:iterate>
						</logic:notEmpty>
						<logic:empty name="ordenControlFisAdapterVO" property="ordenControl.listDetAjuDocSop">
								<tr><td align="center">
									<bean:message bundle="base" key="base.resultadoVacio"/>
								</td></tr>				
						</logic:empty>																			
					</tbody>
				</table>
				<p align="right">
						<bean:define id="agregarDetAjuDocSopEnabled" name="ordenControlFisAdapterVO" property="agregarDetAjuDocSopEnabled"/>					
			   	    	<input type="button" <%=agregarDetAjuDocSopEnabled%> class="boton" onclick="submitForm('agregarDetAjuDocSop', '');"
			   	    		value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
				</p>
				
			</span>
		</fieldset>
		<!-- FIN Informes y Resoluciones (DetAjuDocSop) -->
				
		<!-- envios a mesa de entrada (Control Administrativo) -->
		<fieldset>
			<legend
				<logic:empty name="ordenControlFisAdapterVO" property="ordenControl.listMesaEntrada"> 
					style="background-color: #a7aaaa;"
				</logic:empty>
			>

				<a onclick="toggle(this, 'bloqueMesaEntrada')" style="cursor: pointer;"> (+) &nbsp; </a> 
				<bean:message bundle="ef" key="ef.mesaEntrada.legend"/>
			</legend>
			
			<span id="bloqueMesaEntrada" style="display:none">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="ef" key="ef.mesaEntrada.title"/></caption>				
	              	<tbody>
						<logic:notEmpty name="ordenControlFisAdapterVO" property="ordenControl.listMesaEntrada">
			               	<tr>
			               		<th width="1">&nbsp;</th>
			               		<th width="1">&nbsp;</th>
			               		<th width="1">&nbsp;</th>
								<th><bean:message bundle="ef" key="ef.mesaEntrada.fecha.label"/></th>
								<th><bean:message bundle="ef" key="ef.estadoOrden.label"/></th>
								<th><bean:message bundle="ef" key="ef.mesaEntrada.observacion.label"/></th>
							</tr>
							
							<logic:iterate id="MesaEntradaVO" name="ordenControlFisAdapterVO" property="ordenControl.listMesaEntrada">
								<tr>		
										
									<!-- ver -->		
									<td>
										<logic:equal name="ordenControlFisAdapterVO" property="verMesaEntradaEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verMesaEntrada', '<bean:write name="MesaEntradaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
												</a>											
										</logic:equal>	
										<logic:notEqual name="ordenControlFisAdapterVO" property="verMesaEntradaEnabled" value="enabled">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</td>
									
									<!-- modificar -->
									<td>
										<logic:equal name="ordenControlFisAdapterVO" property="modificarMesaEntradaEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarMesaEntrada', '<bean:write name="MesaEntradaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="ef" key="ef.ordenControlFisViewAdapter.button.modificarMesaEntrada"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>											
										</logic:equal>	
										<logic:notEqual name="ordenControlFisAdapterVO" property="modificarMesaEntradaEnabled" value="enabled">
											<img title="<bean:message bundle="ef" key="ef.ordenControlFisViewAdapter.button.modificarMesaEntrada"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</td>
									
									<!-- eliminar -->
									<td>
										<logic:equal name="ordenControlFisAdapterVO" property="eliminarMesaEntradaEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarMesaEntrada', '<bean:write name="MesaEntradaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>											
										</logic:equal>	
										<logic:notEqual name="ordenControlFisAdapterVO" property="eliminarMesaEntradaEnabled" value="enabled">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</td>
																								
									<td><bean:write name="MesaEntradaVO" property="fechaView"/></td>

									<td><bean:write name="MesaEntradaVO" property="estadoOrden.desEstadoOrden"/></td>
									
									<td><bean:write name="MesaEntradaVO" property="observacion"/></td>									
									
								</tr>
							</logic:iterate>
						</logic:notEmpty>
						<logic:empty name="ordenControlFisAdapterVO" property="ordenControl.listMesaEntrada">
								<tr><td align="center">
									<bean:message bundle="base" key="base.resultadoVacio"/>
								</td></tr>				
						</logic:empty>																			
					</tbody>
				</table>
				<p align="right">
						<bean:define id="agregarMesaEntradaEnabled" name="ordenControlFisAdapterVO" property="agregarMesaEntradaEnabled"/>					
			   	    	<input type="button" <%=agregarMesaEntradaEnabled%> class="boton" onclick="submitForm('agregarMesaEntrada', '');"
			   	    		value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
				</p>
				
			</span>
		</fieldset>		
		<!-- FIN envios a mesa de entrada -->
					
		<!-- Aprobaciones -->
		<fieldset>
			<legend
				<logic:empty name="ordenControlFisAdapterVO" property="ordenControl.listAproOrdCon"> 
					style="background-color: #a7aaaa;"
				</logic:empty>
			>

				<a onclick="toggle(this, 'bloqueAproOrdCon')" style="cursor: pointer;"> (+) &nbsp; </a> 
				<bean:message bundle="ef" key="ef.aproOrdCon.legend"/>
			</legend>
			
			<span id="bloqueAproOrdCon" style="display:none">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="ef" key="ef.aproOrdCon.title"/></caption>				
	              	<tbody>
						<logic:notEmpty name="ordenControlFisAdapterVO" property="ordenControl.listAproOrdCon">
			               	<tr>
			               		<th width="1">&nbsp;</th>
			               		<th width="1">&nbsp;</th>
			               		<th width="1">&nbsp;</th>
								<th><bean:message bundle="ef" key="ef.aproOrdCon.fecha.label"/></th>
								<th><bean:message bundle="ef" key="ef.estadoOrden.label"/></th>
								<th><bean:message bundle="ef" key="ef.aproOrdCon.observacion.label"/></th>
							</tr>
							
							<logic:iterate id="AproOrdConVO" name="ordenControlFisAdapterVO" property="ordenControl.listAproOrdCon">
								<tr>		
										
									<!-- ver -->		
									<td>
										<logic:equal name="ordenControlFisAdapterVO" property="verAproOrdConEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verAproOrdCon', '<bean:write name="AproOrdConVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
												</a>											
										</logic:equal>	
										<logic:notEqual name="ordenControlFisAdapterVO" property="verAproOrdConEnabled" value="enabled">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</td>
									
									<!-- modificar -->
									<td>
										<logic:equal name="ordenControlFisAdapterVO" property="modificarAproOrdConEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarAproOrdCon', '<bean:write name="AproOrdConVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>											
										</logic:equal>	
										<logic:notEqual name="ordenControlFisAdapterVO" property="modificarAproOrdConEnabled" value="enabled">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</td>
									
									<!-- eliminar -->
									<td>
										<logic:equal name="ordenControlFisAdapterVO" property="eliminarAproOrdConEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarAproOrdCon', '<bean:write name="AproOrdConVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>											
										</logic:equal>	
										<logic:notEqual name="ordenControlFisAdapterVO" property="eliminarAproOrdConEnabled" value="enabled">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</td>
																								
									<td><bean:write name="AproOrdConVO" property="fechaView"/></td>

									<td><bean:write name="AproOrdConVO" property="estadoOrden.desEstadoOrden"/></td>
									
									<td><bean:write name="AproOrdConVO" property="observacion"/></td>									
									
								</tr>
							</logic:iterate>
						</logic:notEmpty>
						<logic:empty name="ordenControlFisAdapterVO" property="ordenControl.listAproOrdCon">
								<tr><td align="center">
									<bean:message bundle="base" key="base.resultadoVacio"/>
								</td></tr>				
						</logic:empty>																			
					</tbody>
				</table>
				<p align="right">
						<bean:define id="agregarAproOrdConEnabled" name="ordenControlFisAdapterVO" property="agregarAproOrdConEnabled"/>					
			   	    	<input type="button" <%=agregarAproOrdConEnabled%> class="boton" onclick="submitForm('agregarAproOrdCon', '');"
			   	    		value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
				</p>
				
			</span>
		</fieldset>
		<!-- FIN Aprobaciones -->		
					

						
		<table class="tablabotones" width="100%">		
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	
	   	    	<td align="right">
	   	    		<logic:equal name="ordenControlFisAdapterVO" property="enviarMesaEntradaBussEnabled" value="true">
	   	    			<bean:define id="enviarME" name="ordenControlFisAdapterVO" property="enviarMesaEntradaEnabled"/>
			   	    	<input type="button" <%=enviarME%> class="boton" onclick="submitForm('enviarMesaEntrada', '');"
			   	    		value="<bean:message bundle="ef" key="ef.ordenControlFisAdapter.button.enviarMesaEntrada"/>"/>
	   	    		</logic:equal>
	   	    	</td>
	   	    	<td align="right">
	   	    		<logic:equal name="ordenControlFisAdapterVO" property="cerrarOrdenBussEnabled" value="true">
	   	    			<bean:define id="cerrarEnabled" name="ordenControlFisAdapterVO" property="cerrarOrdenControlEnabled"/>
			   	    	<input type="button" <%=cerrarEnabled%> class="boton" onclick="submitForm('cerrarOrden', '');"
			   	    		value="<bean:message bundle="ef" key="ef.ordenControlFisAdapter.button.cerrarOrden"/>"/>
	   	    		</logic:equal>
	   	    	</td>
	   	    </tr>
	   	 </table>
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
		<logic:present name="irA">
			<script type="text/javascript">document.location = document.URL + '#<bean:write name="irA"/>';</script>
		</logic:present>
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->	
<!-- ordenControlFisAdapter.jsp -->
