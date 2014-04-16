<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/rec/AdministrarObra.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="rec" key="rec.obraViewAdapter.title"/></h1>

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
				
				<tr>
					<!-- Descripcion del Estado de la Obra -->
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="obraAdapterVO" property="obra.estadoObra.desEstadoObra"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- Obra -->

	<!-- Leyendas -->
	<fieldset>
		<legend><bean:message bundle="rec" key="rec.obra.leyendas.label"/></legend>
		
		<table class="tabladatos">
			
			<!-- Leyenda Contado -->			
			<tr>
				<td><label><bean:message bundle="rec" key="rec.obra.leyCon.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="obraAdapterVO" property="obra.leyCon"/></td>
			</tr>
			
			<!-- Leyenda Primer Cuota -->			
			<tr>
				<td><label><bean:message bundle="rec" key="rec.obra.leyPriCuo.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="obraAdapterVO" property="obra.leyPriCuo"/></td>
			</tr>
			
			<!-- Leyenda Cuota Restante -->			
			<tr>
				<td><label><bean:message bundle="rec" key="rec.obra.leyResCuo.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="obraAdapterVO" property="obra.leyResCuo"/></td>
			</tr>

			<!-- Leyenda Cambio de Plan -->			
			<tr>
				<td><label><bean:message bundle="rec" key="rec.obra.leyCamPla.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="obraAdapterVO" property="obra.leyCamPla"/></td>
			</tr>
		
		</table>
	</fieldset>

		<!-- Historial de Cambios de Estado de la Obra -->		
		<logic:equal name="obraAdapterVO" property="act" value="ver">
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="rec" key="rec.obra.listHisEstadoObra.label"/></caption>
				<tbody>
					<logic:notEmpty  name="obraAdapterVO" property="obra.listHisEstadoObra">	    	
					    <tr>
							<th align="left"><bean:message bundle="rec" key="rec.hisEstadoObra.fechaEstado.label"/></th>
							<th align="left"><bean:message bundle="rec" key="rec.hisEstadoObra.descripcion.label"/></th>
							<th align="left"><bean:message bundle="rec" key="rec.hisEstadoObra.estadoObra.label"/></th>
						</tr>
						<logic:iterate id="HisEstadoObraVO" name="obraAdapterVO" property="obra.listHisEstadoObra">
							<tr>
								<td><bean:write name="HisEstadoObraVO" property="fechaEstadoView" />&nbsp;</td>
								<td><bean:write name="HisEstadoObraVO" property="descripcion" />&nbsp;</td>
								<td><bean:write name="HisEstadoObraVO" property="estadoObra.desEstadoObra" />&nbsp;</td>
							</tr>
						</logic:iterate>
					</logic:notEmpty>
					<logic:empty  name="obraAdapterVO" property="obra.listHisEstadoObra">
						<tr><td align="center"><bean:message bundle="base" key="base.noExistenRegitros"/></td></tr>
					</logic:empty>
				</tbody>
			</table>			
		</logic:equal>
		
		<logic:equal name="obraAdapterVO" property="act" value="cambiarEstado">		
			<!-- Estado Obra -->
			<fieldset>
				<legend><bean:message bundle="rec" key="rec.estadoObra.title"/></legend>
				<table class="tabladatos">
					<tr>
						<td><label><bean:message bundle="rec" key="rec.estadoObra.label"/>: </label></td>
						<td class="normal" colspan="3"><bean:write name="obraAdapterVO" property="obra.estadoObra.desEstadoObra"/></td>
					</tr>
					<tr>				
						<td><label><bean:message bundle="rec" key="rec.obra.nuevoEstado.label"/>: </label></td>
						<td class="normal">
							<html:select name="obraAdapterVO" property="obra.estadoObra.id" styleClass="select">
								<html:optionsCollection name="obraAdapterVO" property="listEstadoObra" label="desEstadoObra" value="id" />
							</html:select>
						</td>				
					</tr>
				 </table>	
			</fieldset>		
		</logic:equal>

		<logic:equal name="obraAdapterVO" property="act" value="ver">
			<!-- ObraFormaPago -->
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="rec" key="rec.obra.listObraFormaPago.label"/></caption>
		    	<tbody>
					<logic:notEmpty  name="obraAdapterVO" property="obra.listObraFormaPago">
				    	<tr>
							<th width="1">&nbsp;</th> <!-- Ver -->				    	
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
			</table>
			<!-- ObraFormaPago -->			
			
			<!-- Obra Planilla Cuadra-->		
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="rec" key="rec.obra.listPlanillaCuadra.label"/></caption>
		    	<tbody>
					<logic:notEmpty  name="obraAdapterVO" property="obra.listPlanillaCuadra">	    	
				    	<tr>
							<th width="1">&nbsp;</th> <!-- Ver -->
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
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="obraAdapterVO" property="obra.listObrRepVen">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			</tbody>
		</table>
		<!-- ObrRepVen -->
		
		</logic:equal>
		
		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
					<logic:equal name="obraAdapterVO" property="act" value="ver">
		   	    	   <html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						    <bean:message bundle="base" key="abm.button.imprimir"/>
					   </html:button>
	   	            </logic:equal>
	   	    	</td>
	   	    	<td align="right" width="50%">
	   	    		<logic:equal name="obraAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="obraAdapterVO" property="act" value="cambiarEstado">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('cambiarEstado', '');">
							<bean:message bundle="base" key="abm.button.cambiarEstado"/>
						</html:button>
					</logic:equal>
				</td>
	   	    </tr>
	   	 </table>

	   	<input type="hidden" name="name"  value="<bean:write name='obraAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>

		<input type="hidden" name="method" value=""/>
		<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
		<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
		<input type="hidden" name="name"         value="<bean:write name='obraAdapterVO' property='name'/>" id="name"/>
		<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
		
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
