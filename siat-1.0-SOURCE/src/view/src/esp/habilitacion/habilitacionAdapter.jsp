<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/esp/AdministrarHabilitacion.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="esp" key="esp.habilitacionAdapter.title"/></h1>		
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
			<legend><bean:message bundle="esp" key="esp.habilitacion.title"/></legend>			
			<table class="tablabotones" width="100%">
				<tr>
					<!-- Recurso -->		
					<td><label>&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="habilitacionAdapterVO" property="habilitacion.recurso.desRecurso"/></td>
				</tr>
				<tr>
					<!-- Numero -->		
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.numero.label"/>: </label></td>
					<td class="normal"><bean:write name="habilitacionAdapterVO" property="habilitacion.numeroView"/></td>

					<!-- Anio -->		
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.anio.label"/>: </label></td>
					<td class="normal"><bean:write name="habilitacionAdapterVO" property="habilitacion.anioView"/></td>
				</tr>
				<tr>
					<!-- Tipo Habilitacion -->		
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.tipoHab.label"/>: </label></td>
					<td class="normal"><bean:write name="habilitacionAdapterVO" property="habilitacion.tipoHab.descripcion"/></td>
					<!-- Tipo Cobro -->		
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.tipoCobro.label"/>: </label></td>
					<td class="normal"><bean:write name="habilitacionAdapterVO" property="habilitacion.tipoCobro.descripcion"/></td>
				</tr>
				<logic:equal name="habilitacionAdapterVO" property="habilitacion.paramTipoHab" value="EXT">
				<tr>
					<!-- Cuenta -->		
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
					<td class="normal"><bean:write name="habilitacionAdapterVO" property="habilitacion.cuenta.numeroCuenta"/></td>
				</tr>
				</logic:equal>
				<logic:notEqual name="habilitacionAdapterVO" property="habilitacion.paramTipoHab" value="EXT">
				<tr>
					<!-- Valores Cargados -->		
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.valoresCargados.label"/>: </label></td>
					<td class="normal"><bean:write name="habilitacionAdapterVO" property="habilitacion.valoresCargados.descripcion"/></td>
				</tr>
				</logic:notEqual>
				
				<tr>
					<!-- Fecha Habilitacion -->		
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.fechaHab.label"/>: </label></td>
					<td class="normal"><bean:write name="habilitacionAdapterVO" property="habilitacion.fechaHabView"/></td>
				</tr>
				<tr>
					<!-- Descripcion -->		
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.descripcion.label"/>: </label></td>
					<td class="normal"><bean:write name="habilitacionAdapterVO" property="habilitacion.descripcion"/></td>
				</tr>
				<!-- Lugar Evento -->		
				<tr>				
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.lugarEvento.label"/>: </label></td>
					<td class="normal"><bean:write name="habilitacionAdapterVO" property="habilitacion.lugarEvento.descripcion"/></td>				
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.factorOcupacional.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="habilitacionAdapterVO" property="habilitacion.factorOcupacionalView"/></td>
				</tr>
				<tr>
					<!-- Desc. Lugar Evento -->		
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.lugarEventoStr.label"/>: </label></td>
					<td class="normal"><bean:write name="habilitacionAdapterVO" property="habilitacion.lugarEventoStr"/></td>
				</tr>
				<tr>
					<!-- Hora Acceso -->		
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.horaAcceso.label"/>: </label></td>
					<td class="normal"><bean:write name="habilitacionAdapterVO" property="habilitacion.horaAccesoView"/></td>

					<!-- Cantidad Funciones -->		
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.cantFunciones.label"/>: </label></td>
					<td class="normal"><bean:write name="habilitacionAdapterVO" property="habilitacion.cantFuncionesView"/></td>
				</tr>
				<tr>
					<!-- Fecha Evento Desde -->		
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.fecEveDes.label"/>: </label></td>
					<td class="normal"><bean:write name="habilitacionAdapterVO" property="habilitacion.fecEveDesView"/></td>
					<!-- Fecha Evento Hasta -->		
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.fecEveHas.label"/>: </label></td>
					<td class="normal"><bean:write name="habilitacionAdapterVO" property="habilitacion.fecEveHasView"/></td>
				</tr>
				<tr>
					<!-- Observaciones -->		
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.observaciones.label"/>: </label></td>
					<td class="normal"><bean:write name="habilitacionAdapterVO" property="habilitacion.observaciones"/></td>
				</tr>
				<tr>
					<!-- Estado Habilitacion -->		
					<td><label>&nbsp;<bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="habilitacionAdapterVO" property="habilitacion.estHab.desEstHab"/></td>
					<!-- Clasificacion del Evento -->		
					<td><label>&nbsp;<bean:message bundle="esp" key="esp.habilitacion.claHab.label"/>: </label></td>
					<td class="normal"><bean:write name="habilitacionAdapterVO" property="habilitacion.claHab.value"/></td>
				</tr>
			</table>
		</fieldset>
		<!-- Persona que Habilita -->		
		<fieldset>
			<legend><bean:message bundle="esp" key="esp.habilitacion.perHab.label"/></legend>			
			<table class="tablabotones" width="100%">
				<tr>
					<td colspan="3">
						<tr>
							<td class="normal">
								<bean:define id="personaVO" name="habilitacionAdapterVO" property="habilitacion.perHab"/>	
								<%@ include file="/pad/persona/includePersonaReducida.jsp"%>
							</td>
						</tr>
					</td>
				</tr>
			</table>
		</fieldset>
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="right">
					<bean:define id="modificarEncabezadoEnabled" name="habilitacionAdapterVO" property="modificarEncabezadoEnabled"/>
					<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificarEncabezado', 
						'<bean:write name="habilitacionAdapterVO" property="habilitacion.id" bundle="base" formatKey="general.format.id"/>');" 
						value="<bean:message bundle="base" key="abm.button.modificar"/>"/>
				</td>
			</tr>
		</table>
		<!-- PrecioEvento -->		
		<div style="border:0px" class="horizscroll">
		<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="esp" key="esp.precioEvento.label"/></caption>
	    	<tbody>
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="esp" key="esp.tipoEntrada.label"/></th>
						<th align="left"><bean:message bundle="esp" key="esp.precioEvento.precio.label"/></th>						
					</tr>
					<logic:notEmpty  name="habilitacionAdapterVO" property="habilitacion.listPrecioEvento">	    	
					<logic:iterate id="PrecioEventoVO" name="habilitacionAdapterVO" property="habilitacion.listPrecioEvento">
							<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="habilitacionAdapterVO" property="verPrecioEventoEnabled" value="enabled">							
									<logic:equal name="PrecioEventoVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verPrecioEvento', '<bean:write name="PrecioEventoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="PrecioEventoVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="habilitacionAdapterVO" property="verPrecioEventoEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							<td>
								<!-- Modificar-->
								<logic:equal name="habilitacionAdapterVO" property="modificarPrecioEventoEnabled" value="enabled">
									<logic:equal name="PrecioEventoVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarPrecioEvento', '<bean:write name="PrecioEventoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="PrecioEventoVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="habilitacionAdapterVO" property="modificarPrecioEventoEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							<td>
								<!-- Eliminar-->
								<logic:equal name="habilitacionAdapterVO" property="eliminarPrecioEventoEnabled" value="enabled">
									<logic:equal name="PrecioEventoVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarPrecioEvento', '<bean:write name="PrecioEventoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PrecioEventoVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="habilitacionAdapterVO" property="eliminarPrecioEventoEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="PrecioEventoVO" property="descripcion"/>&nbsp;</td>
							<td>$<bean:write name="PrecioEventoVO" property="precioView"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="habilitacionAdapterVO" property="habilitacion.listPrecioEvento">
					<tr><td colspan="9" align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			<td colspan="20" align="right">
  				<bean:define id="agregarHabilitacionEnabled" name="habilitacionAdapterVO" property="agregarPrecioEventoEnabled"/>
				<input type="button" <%=agregarHabilitacionEnabled%> class="boton" 
					onClick="submitForm('agregarPrecioEvento', '<bean:write name="habilitacionAdapterVO" property="habilitacion.id" bundle="base" formatKey="general.format.id"/>');" 
					value="<bean:message bundle="base" key="abm.button.agregar"/>"	align="left" />
			</td>
			</tbody>
			</table>
			</div>
		<!-- PrecioEvento -->			
		
		<!-- Entradas Habilitadas -->		
		<div style="border:0px" class="horizscroll">
		<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="esp" key="esp.entHab.label"/></caption>
	    	<tbody>
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="esp" key="esp.tipoEntrada.label"/></th>
						<th align="left"><bean:message bundle="esp" key="esp.entHab.serie.label"/></th>						
						<th align="left"><bean:message bundle="esp" key="esp.entHab.descripcion.label"/></th>
						<th align="left"><bean:message bundle="esp" key="esp.entHab.nroDesde.label"/></th>						
						<th align="left"><bean:message bundle="esp" key="esp.entHab.nroHasta.label"/></th>
						<th align="left"><bean:message bundle="esp" key="esp.entHab.totalHabilitadas.label"/></th>							
						<th align="left"><bean:message bundle="esp" key="esp.entHab.cantidadRestantes.label"/></th>
												
					</tr>
					<logic:notEmpty  name="habilitacionAdapterVO" property="habilitacion.listEntHab">	    	
					<logic:iterate id="EntHabVO" name="habilitacionAdapterVO" property="habilitacion.listEntHab">
							<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="habilitacionAdapterVO" property="verEntHabEnabled" value="enabled">							
									<logic:equal name="EntHabVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verEntHab', '<bean:write name="EntHabVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="EntHabVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="habilitacionAdapterVO" property="verEntHabEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							<td>
								<!-- Modificar-->
								<logic:equal name="habilitacionAdapterVO" property="modificarEntHabEnabled" value="enabled">
									<logic:equal name="EntHabVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarEntHab', '<bean:write name="EntHabVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="EntHabVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="habilitacionAdapterVO" property="modificarEntHabEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							<td>
								<!-- Eliminar-->
								<logic:equal name="habilitacionAdapterVO" property="eliminarEntHabEnabled" value="enabled">
									<logic:equal name="EntHabVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarEntHab', '<bean:write name="EntHabVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="EntHabVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="habilitacionAdapterVO" property="eliminarEntHabEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
							
							
							<td><bean:write name="EntHabVO" property="precioEvento.descripcion"/>&nbsp;</td>
							<td><bean:write name="EntHabVO" property="serie"/>&nbsp;</td>
							<td><bean:write name="EntHabVO" property="descripcion"/>&nbsp;</td>
							<td><bean:write name="EntHabVO" property="nroDesdeView"/>&nbsp;</td>
							<td><bean:write name="EntHabVO" property="nroHastaView"/>&nbsp;</td>
							<td><bean:write name="EntHabVO" property="totalHabilitadasView"/>&nbsp;</td>
							<td><bean:write name="EntHabVO" property="totalRestantesView"/>&nbsp;</td>
							
								<!-- Total vendidas-->
								<logic:notEmpty name="EntHabVO" property="listEntVen">
							        </tr>
							        <tr>
							        	<td>&nbsp;</td>
							        	<td colspan="9">
							        		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
							        			<tbody>
							        				<tr>
							        					<th align="left" ><bean:message bundle="esp" key="esp.entVen.fecha.label"/></th>		        									  	
													  	<th align="left" ><bean:message bundle="esp" key="esp.entVen.totalVendidas.label"/></th>
													  	<th align="left" ><bean:message bundle="esp" key="esp.entVen.importe.label"/></th>													  														  	
													  	<th align="left" ><bean:message bundle="esp" key="esp.entVen.tipo.label"/></th>
							        				</tr>
										       		<logic:iterate id="EntVenVO" name="EntHabVO" property="listEntVen">
														<tr>															
															<td><bean:write name="EntVenVO" property="fechaEmisionView"/>&nbsp;</td>													        
													        <td><bean:write name="EntVenVO" property="totalVendidasView"/>&nbsp;</td>
													        													        
													        <logic:equal name="EntVenVO" property="esAnulada" value="1">
													        	<td>-&nbsp;</td>
													        	<td>Anuladas&nbsp;</td>									
													        </logic:equal>	        
													        <logic:equal name="EntVenVO" property="esAnulada" value="0">
													        	<td><bean:write name="EntVenVO" property="importeView"/>&nbsp;</td>
													        	<td>Vendidas&nbsp;</td>									
													        </logic:equal>
												       	</tr>							        				
												    </logic:iterate>
							        			</tbody>
							        		</table>
							        	</td>
							        </tr>							       		
								  </logic:notEmpty> 
							
							<!--Fin Total vendidas-->
										
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="habilitacionAdapterVO" property="habilitacion.listEntHab">
					<tr><td colspan="10" align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			<td colspan="20" align="right">
  				<bean:define id="eliminarHabilitacionEnabled" name="habilitacionAdapterVO" property="eliminarEntHabEnabled"/>
				<input type="button" <%=eliminarHabilitacionEnabled%> class="boton" 
					onClick="submitForm('anularEntVen', '<bean:write name="habilitacionAdapterVO" property="habilitacion.id" bundle="base" formatKey="general.format.id"/>');" 
					value="<bean:message bundle="esp" key="abm.button.anular"/>"	align="left" />
			
  				<bean:define id="agregarHabilitacionEnabled" name="habilitacionAdapterVO" property="agregarEntHabEnabled"/>
				<input type="button" <%=agregarHabilitacionEnabled%> class="boton" 
					onClick="submitForm('agregarEntHab', '<bean:write name="habilitacionAdapterVO" property="habilitacion.id" bundle="base" formatKey="general.format.id"/>');" 
					value="<bean:message bundle="base" key="abm.button.agregar"/>"	align="left" />
			</td>
			</tbody>
			</table>
			</div>
		<!-- Entradas Habilitadas -->		
		
		<!-- Exenciones -->		
		<div style="border:0px" class="horizscroll">
		<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="exe" key="exe.exencion.label"/></caption>
	    	<tbody>
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="exe" key="exe.exencion.desExencion.label"/></th>
						<th align="left"><bean:message bundle="esp" key="esp.habExe.fechaDesde.label"/></th>						
						<th align="left"><bean:message bundle="esp" key="esp.habExe.fechaHasta.label"/></th>						
												
					</tr>
					<logic:notEmpty  name="habilitacionAdapterVO" property="habilitacion.listHabExe">	    	
					<logic:iterate id="HabExeVO" name="habilitacionAdapterVO" property="habilitacion.listHabExe">
							<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="habilitacionAdapterVO" property="verHabExeEnabled" value="enabled">							
									<logic:equal name="HabExeVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verHabExe', '<bean:write name="HabExeVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="HabExeVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="habilitacionAdapterVO" property="verHabExeEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							<td>
								<!-- Modificar-->
								<logic:equal name="habilitacionAdapterVO" property="modificarHabExeEnabled" value="enabled">
									<logic:equal name="HabExeVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarHabExe', '<bean:write name="HabExeVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="HabExeVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="habilitacionAdapterVO" property="modificarHabExeEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							<td>
								<!-- Eliminar-->
								<logic:equal name="habilitacionAdapterVO" property="eliminarHabExeEnabled" value="enabled">
									<logic:equal name="HabExeVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarHabExe', '<bean:write name="HabExeVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="HabExeVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="habilitacionAdapterVO" property="eliminarHabExeEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="HabExeVO" property="exencion.desExencion"/>&nbsp;</td>
							<td><bean:write name="HabExeVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="HabExeVO" property="fechaHastaView"/>&nbsp;</td>
							
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="habilitacionAdapterVO" property="habilitacion.listHabExe">
					<tr><td colspan="9" align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			<td colspan="20" align="right">
  				<bean:define id="agregarHabilitacionEnabled" name="habilitacionAdapterVO" property="agregarHabExeEnabled"/>
				<input type="button" <%=agregarHabilitacionEnabled%> class="boton" 
					onClick="submitForm('agregarHabExe', '<bean:write name="habilitacionAdapterVO" property="habilitacion.id" bundle="base" formatKey="general.format.id"/>');" 
					value="<bean:message bundle="base" key="abm.button.agregar"/>"	align="left" />
			</td>
			</tbody>
			</table>
			</div>
		<!-- Exenciones -->
		
		
		<table class="tablabotones">
			<tr>				
				<td align="left" width="100%">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<td align="right">
					<html:button property="btnImprimir"  styleClass="boton" onclick="submitForm('imprimir', '1');">
						<bean:message bundle="base" key="abm.button.imprimir"/>
					</html:button>
				</td>
				<logic:equal name="habilitacionAdapterVO" property="act" value="modificar">
					<logic:equal name="habilitacionAdapterVO" property="emisionInicialEnabled" value="enabled">
						<logic:equal name="habilitacionAdapterVO" property="emisionInicialBussEnabled" value="true">
							<td align="right">
								<html:button property="btnEmitir"  styleClass="boton" onclick="submitForm('emisionInicial', '');">
									<bean:message bundle="esp" key="esp.habilitacionAdapter.button.emisionInicial"/>
								</html:button>
							</td>
						</logic:equal>
						<logic:notEqual name="habilitacionAdapterVO" property="emisionInicialBussEnabled" value="true">
							<td align="right">
								<html:button property="btnEmitir"  styleClass="boton" onclick="submitForm('emisionInicial', '');" disabled="true">
									<bean:message bundle="esp" key="esp.habilitacionAdapter.button.emisionInicial"/>
								</html:button>
							</td>
						</logic:notEqual>					
					</logic:equal>
				</logic:equal>
   	    	</tr>
		</table>
	   	
		<input type="hidden" name="method" value=""/>
		<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
		<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->		