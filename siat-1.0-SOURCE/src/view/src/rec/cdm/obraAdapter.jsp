<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/rec/AdministrarObra.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="rec" key="rec.obraAdapter.title"/></h1>

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>	
		
		<!-- Obra -->
		<fieldset>
			<legend><bean:message bundle="rec" key="rec.obra.title"/></legend>
			
			<table class="tabladatos">
				<tr>
					<!-- Recurso -->
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal" colspan="3">
						<bean:write name="obraAdapterVO" property="obra.recurso.desRecurso"/>
					</td>
				</tr>
				<tr>
					<!-- Numero Obra -->
					<td><label><bean:message bundle="rec" key="rec.obra.numeroObra.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="obraAdapterVO" property="obra.numeroObraView"/></td>
				</tr>
				<tr>
					<!-- Descripcion -->
					<td><label><bean:message bundle="rec" key="rec.obra.desObra.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="obraAdapterVO" property="obra.desObra"/></td>
				</tr>
				
				<tr>
					<!--Permite Cambio a Plan Mayor -->
					<td><label><bean:message bundle="rec" key="rec.obra.permiteCamPlaMay.label"/>: </label></td>
					<td class="normal"><bean:write name="obraAdapterVO" property="obra.permiteCamPlaMay.value"/></td>				
				</tr>
				
				<tr>
					<!-- Es Por Valuacion -->
					<td><label><bean:message bundle="rec" key="rec.obra.esPorValuacion.label"/>: </label></td>
					<td class="normal"><bean:write name="obraAdapterVO" property="obra.esPorValuacion.value"/></td>				
				</tr>

				<logic:equal name="obraAdapterVO" property="obra.esCostoEspEnabled" value="true">
					<tr>	
						<!-- Es Costo Especial -->
						<td><label><bean:message bundle="rec" key="rec.obra.esCostoEsp.label"/>: </label></td>
						<td class="normal"><bean:write name="obraAdapterVO" property="obra.esCostoEsp.value"/></td>				
						<!-- Costo Especial -->			
						<logic:equal name="obraAdapterVO" property="obra.costoEspEnabled" value="true">					
							<td><label><bean:message bundle="rec" key="rec.obra.costoEsp.label"/>: </label></td>
							<td class="normal"><bean:write name="obraAdapterVO" property="obra.costoEspView"/></td>
						</logic:equal>
					</tr>
				</logic:equal>
				
				<!-- Inclucion de CasoView -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
					<td colspan="3">
						<bean:define id="IncludedVO" name="obraAdapterVO" property="obra"/>
						<%@ include file="/cas/caso/includeCasoView.jsp" %>				
					</td>
				</tr>
				<!-- Fin Inclucion de CasoView -->
				
				<!-- Estado -->
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="obraAdapterVO" property="obra.estadoObra.desEstadoObra"/></td>
				</tr>
				
			</table>
			<p align="right">
				<bean:define id="modificarEncabezadoEnabled" name="obraAdapterVO" property="modificarEncabezadoEnabled"/>
				<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificarEncabezado', 
					'<bean:write name="obraAdapterVO" property="obra.id" bundle="base" formatKey="general.format.id"/>');" 
					value="<bean:message bundle="base" key="abm.button.modificar"/>"/>	
			</p>
		</fieldset>	
		<!-- Obra -->
		
		<!-- ObraFormaPago -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="rec" key="rec.obra.listObraFormaPago.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="obraAdapterVO" property="obra.listObraFormaPago">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th width="1">&nbsp;</th> <!-- Activar/Desactivar -->
						<th align="left"><bean:message bundle="rec" key="rec.obraFormaPago.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="rec" key="rec.obraFormaPago.fechaHasta.label"/></th>
						<th align="left"><bean:message bundle="rec" key="rec.obraFormaPago.cantCuotas.label"/></th>
						<th align="left"><bean:message bundle="rec" key="rec.obraFormaPago.montoMinimoCuota.label"/></th>
						<th align="left"><bean:message bundle="rec" key="rec.obraFormaPago.descuento.label"/></th>
						<th align="left"><bean:message bundle="rec" key="rec.obraFormaPago.interesFinanciero.label"/></th>
						<th align="left"><bean:message bundle="rec" key="rec.obraFormaPago.esEspecial.label"/></th>						
					</tr>
					<logic:iterate id="ObraFormaPagoVO" name="obraAdapterVO" property="obra.listObraFormaPago">
			
						<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="obraAdapterVO" property="verObraFormaPagoEnabled" value="enabled">
									<logic:equal name="ObraFormaPagoVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verObraFormaPago', '<bean:write name="ObraFormaPagoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="ObraFormaPagoVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="obraAdapterVO" property="verObraFormaPagoEnabled" value="enabled">										
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Modificar-->								
							<td>
								<logic:equal name="obraAdapterVO" property="modificarObraFormaPagoEnabled" value="enabled">
									<logic:equal name="ObraFormaPagoVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarObraFormaPago', '<bean:write name="ObraFormaPagoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="ObraFormaPagoVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="obraAdapterVO" property="modificarObraFormaPagoEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Eliminar-->								
							<td>
								<logic:equal name="obraAdapterVO" property="eliminarObraFormaPagoEnabled" value="enabled">
									<logic:equal name="ObraFormaPagoVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarObraFormaPago', '<bean:write name="ObraFormaPagoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="ObraFormaPagoVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="obraAdapterVO" property="eliminarObraFormaPagoEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
							<td>

								<!-- Activar -->
								<logic:equal name="ObraFormaPagoVO" property="estado.id" value="0">
									<logic:equal name="obraAdapterVO" property="activarObraFormaPagoEnabled" value="enabled">
										<logic:equal name="ObraFormaPagoVO" property="activarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('activarObraFormaPago', '<bean:write name="ObraFormaPagoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.activar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/activar0.gif"/>
											</a>
										</logic:equal> 
										<logic:notEqual name="ObraFormaPagoVO" property="activarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/activar1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="obraAdapterVO" property="activarObraFormaPagoEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/activar1.gif"/>
									</logic:notEqual>
								</logic:equal> 

								<!-- Desactivar -->
								<logic:equal name="ObraFormaPagoVO" property="estado.id" value="1">
									<logic:equal name="obraAdapterVO" property="desactivarObraFormaPagoEnabled" value="enabled">
										<logic:equal name="ObraFormaPagoVO" property="desactivarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('desactivarObraFormaPago', '<bean:write name="ObraFormaPagoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.desactivar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="ObraFormaPagoVO" property="desactivarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="obraAdapterVO" property="desactivarObraFormaPagoEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar1.gif"/>
									</logic:notEqual>										
								</logic:equal>

								<!-- En estado creado -->
								<logic:equal name="ObraFormaPagoVO" property="estado.id" value="-1">
									<a style="cursor: pointer; cursor: hand;">
									<img border="0" title="<bean:message bundle="base" key="abm.button.creado"/>" src="<%=request.getContextPath()%>/images/iconos/creado0.gif"/>
									</a>
								</logic:equal> 
							</td>
							<td><bean:write name="ObraFormaPagoVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="ObraFormaPagoVO" property="fechaHastaView"/>&nbsp;</td>								
							<td><bean:write name="ObraFormaPagoVO" property="cantCuotasView"/>&nbsp;</td>
							<td><bean:write name="ObraFormaPagoVO" property="montoMinimoCuotaView"/>&nbsp;</td>
							<td><bean:write name="ObraFormaPagoVO" property="descuentoView"/>&nbsp;</td>
							<td><bean:write name="ObraFormaPagoVO" property="interesFinancieroView"/>&nbsp;</td>
							<td><bean:write name="ObraFormaPagoVO" property="esEspecial.value"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="obraAdapterVO" property="obra.listObraFormaPago">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			</tbody>
			
			<tr>
				<td align="right" colspan="11">
   	    				<bean:define id="agregarEnabled" name="obraAdapterVO" property="agregarObraFormaPagoEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregarObraFormaPago', '<bean:write name="obraAdapterVO" property="obra.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
				</td>			
			</tr>		
		</table>
		<!-- ObraFormaPago -->
		
		<!-- Obra Planilla Cuadra-->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="rec" key="rec.obra.listPlanillaCuadra.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="obraAdapterVO" property="obra.listPlanillaCuadra">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->						
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th width="1">&nbsp;</th> <!-- cambiar estado -->
						<th align="left"><bean:message bundle="rec" key="rec.planillaCuadra.numeroPlanilla.label"/></th>
						<th align="left"><bean:message bundle="rec" key="rec.planillaCuadra.numeroCuadra.ref"/></th>
						<th align="left"><bean:message bundle="rec" key="rec.tipoObra.label"/></th>
						<th align="left"><bean:message bundle="rec" key="rec.planillaCuadra.descripcion.label"/></th>
						<th align="left"><bean:message bundle="pad" key="pad.repartidor.label"/></th>
						<th align="left"><bean:message bundle="base" key="base.estado.label"/></th>
					</tr>
					<logic:iterate id="PlanillaCuadraVO" name="obraAdapterVO" property="obra.listPlanillaCuadra">
			
						<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="obraAdapterVO" property="verPlanillaCuadraEnabled" value="enabled">
									<logic:equal name="PlanillaCuadraVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verPlanillaCuadra', '<bean:write name="PlanillaCuadraVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanillaCuadraVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="obraAdapterVO" property="verPlanillaCuadraEnabled" value="enabled">										
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							<!-- Modificar-->								
							<td>
								<logic:equal name="obraAdapterVO" property="modificarPlanillaCuadraEnabled" value="enabled">
									<logic:equal name="PlanillaCuadraVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarPlanillaCuadra', '<bean:write name="PlanillaCuadraVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanillaCuadraVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="obraAdapterVO" property="modificarPlanillaCuadraEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>							
							<!-- Eliminar-->								
							<td>
								<logic:equal name="obraAdapterVO" property="eliminarPlanillaCuadraEnabled" value="enabled">
									<logic:equal name="PlanillaCuadraVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarPlanillaCuadra', '<bean:write name="PlanillaCuadraVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanillaCuadraVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="obraAdapterVO" property="eliminarPlanillaCuadraEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
							<!-- Accion cambiarEstado -->
							<td>
								<logic:equal name="obraAdapterVO" property="cambiarEstadoPlanillaCuadraEnabled" value="enabled">
									<logic:equal name="PlanillaCuadraVO" property="cambiarEstadoBussEnabled" value="true">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('cambiarEstadoPlanillaCuadra', '<bean:write name="PlanillaCuadraVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="rec" key="rec.planillaCuadraSearchPage.button.cambiarEstado"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/cambiarEstado0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="PlanillaCuadraVO" property="cambiarEstadoBussEnabled" value="true">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/cambiarEstado1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="obraAdapterVO" property="cambiarEstadoPlanillaCuadraEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/cambiarEstado1.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="PlanillaCuadraVO" property="idView"/>&nbsp;</td>
							<td><bean:write name="PlanillaCuadraVO" property="numeroCuadraView"/>&nbsp;</td>
							<td><bean:write name="PlanillaCuadraVO" property="tipoObra.desTipoObra"/>&nbsp;</td>
							<td><bean:write name="PlanillaCuadraVO" property="descripcion"/>&nbsp;</td>
							<td><bean:write name="PlanillaCuadraVO" property="repartidor.desRepartidorView"/>&nbsp;</td>															
							<td><bean:write name="PlanillaCuadraVO" property="estPlaCua.desEstPlaCua"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="obraAdapterVO" property="obra.listPlanillaCuadra">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			</tbody>

			<tr>
				<td align="right" colspan="10">
	   				<bean:define id="asignarRepartidorEnabled" name="obraAdapterVO" property="asignarRepartidorEnabled"/>
					<input type="button" <%=asignarRepartidorEnabled%> class="boton" 
						onClick="submitForm('asignarRepartidor', '<bean:write name="obraAdapterVO" property="obra.id" bundle="base" formatKey="general.format.id"/>');" 
						value="<bean:message bundle="rec" key="rec.obraAdapter.button.asignarRepartidor"/>"/>&nbsp;

	   				<bean:define id="agregarEnabled" name="obraAdapterVO" property="agregarPlanillaCuadraEnabled"/>
					<input type="button" <%=agregarEnabled%> class="boton" 
						onClick="submitForm('agregarPlanillaCuadra', '<bean:write name="obraAdapterVO" property="obra.id" bundle="base" formatKey="general.format.id"/>');" 
						value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
				</td>
			</tr>		
		</table>
		<!-- Obra Planilla Cuadra-->

		<!-- ObrRepVen -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="rec" key="rec.obra.listObrRepVen.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="obraAdapterVO" property="obra.listObrRepVen">	    	
			    	<tr>
						<th align="left"><bean:message bundle="rec" key="rec.obrRepVen.descripcion.label"/></th>
						<th align="left"><bean:message bundle="rec" key="rec.obrRepVen.cuotaDesde.label"/></th>
						<th align="left"><bean:message bundle="rec" key="rec.obrRepVen.nueFecVen.label"/></th>
						<th align="left"><bean:message bundle="rec" key="rec.obrRepVen.cantDeuda.label"/></th>
						<th align="left"><bean:message bundle="cas" key="cas.caso.label"/></th>
					</tr>
					<logic:iterate id="ObrRepVenVO" name="obraAdapterVO" property="obra.listObrRepVen">
			
						<tr>
							<td><bean:write name="ObrRepVenVO" property="descripcion"/>&nbsp;</td>
							<td><bean:write name="ObrRepVenVO" property="cuotaDesdeView"/>&nbsp;</td>
							<td><bean:write name="ObrRepVenVO" property="nueFecVenView"/>&nbsp;</td>
							<td><bean:write name="ObrRepVenVO" property="canDeuActView"/>&nbsp;</td>
							<td><bean:write name="ObrRepVenVO" property="caso.sistemaOrigen.desSistemaOrigen"/>
							&nbsp;							
							<bean:write name="ObrRepVenVO" property="caso.numero"/></td>			
						
						</tr>
				<!-- Fin Inclucion de CasoView -->
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="obraAdapterVO" property="obra.listObrRepVen">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			</tbody>
			
			<tr>
				<td align="right" colspan="10">
   	    				<bean:define id="agregarEnabled" name="obraAdapterVO" property="agregarObrRepVenEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregarObrRepVen', '<bean:write name="obraAdapterVO" property="obra.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
				</td>			
			</tr>		
		</table>
		<!-- ObrRepVen -->

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
		<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
