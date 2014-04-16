<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarMulta.do">

	<script>
		function calcularImporte(){
			var cantidad = document.getElementById('canMin');
			var minimo = document.getElementById('minimo');
			var importe = document.getElementById('importeCalc');
			importe.value=cantidad.value * minimo.value;
		}
	</script>

	<!-- Mensajes y/o Advertencias -->
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	
	<h1><bean:message bundle="gde" key="gde.multaEditAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- Multa -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.multa.title"/></legend>
		<table class="tabladatos">
				<logic:equal name="multaAdapterVO" property="act" value="agregar">
					<tr>
						<td>
							<label><bean:message bundle="pad" key="pad.cuenta.label"/>: </label>
						</td>
						<td class="normal">
							<bean:write name="multaAdapterVO" property="multa.cuenta.numeroCuenta"/>
						</td>
					</tr>
					<tr>
						<td>
							<label><bean:message bundle="def" key="def.recurso.label"/>: </label>
						</td>
						<td class="normal">
							<bean:write name="multaAdapterVO" property="multa.cuenta.recurso.desRecurso"/>
						</td>
					</tr>
					<logic:equal name="multaAdapterVO" property="detalleValido" value="true">
						<!-- TipoMulta -->
						<tr>
							<td><label><bean:message bundle="gde" key="gde.tipoMulta.label"/>: </label></td>
							<td class="normal">
								<bean:write name="multaAdapterVO" property="multa.tipoMulta.desTipoMulta"/>
							</td>
						</tr>
						<!-- Filtros si la cuenta posee esImporteManual=No -->
						<logic:equal name="multaAdapterVO" property="multa.tipoMulta.esImporteManual.id" value="0">
							<tr>
								<td><label><bean:message bundle="gde" key="gde.multaEditAdapter.fechaActualizacion.label"/>: </label></td>
					      		<td class="normal">
									<bean:write name="multaAdapterVO" property="multa.fechaActualizacionView"/>
								</td>
							</tr>
							<tr>
									<td><label><bean:message bundle="gde" key="gde.multaEditAdapter.fechaNotificacion.label"/>: </label></td>
									<td class="normal"><bean:write name="multaAdapterVO" property="multa.fechaNotificacionView"/></td>
							</tr>
						
							<tr>
					      		<td><label><bean:message bundle="ef" key="ef.ordenControl.nroOrden.label"/>: </label></td>
					      		<td class="normal">
									<bean:write name="multaAdapterVO" property="multa.ordenControl.numeroOrdenView"/>
								</td>
					      		<td><label><bean:message bundle="ef" key="ef.ordenControl.anioOrden.label"/>: </label></td>
					      		<td class="normal">
									<bean:write name="multaAdapterVO" property="multa.ordenControl.anioOrdenView"/>
								</td>
							</tr>
						</logic:equal>
						<!-- Fin filtros si la cuenta posee esImporteManual=No -->
						
					</logic:equal>
				
					<!-- Si es detalle valido false-->
					<logic:equal name="multaAdapterVO" property="detalleValido" value="false">
						<tr>
							<td><label>(*)<bean:message bundle="gde" key="gde.tipoMulta.label"/>: </label></td>
							<td class="normal">
								<html:select name="multaAdapterVO" property="multa.tipoMulta.id" styleClass="select" onchange="submitForm('paramTipoMulta', '');">
									<html:optionsCollection name="multaAdapterVO" property="listTipoMulta" label="desTipoMulta" value="id" />
								</html:select>
							</td>
						</tr>
						<!-- Filtros si la cuenta posee esImporteManual=No -->
						<logic:equal name="multaAdapterVO" property="multa.tipoMulta.esImporteManual.id" value="0">
							<tr>
								<td><label><bean:message bundle="gde" key="gde.multaEditAdapter.fechaActualizacion.label"/>: </label></td>
					      		<td class="normal">
									<html:text name="multaAdapterVO" property="multa.fechaActualizacionView" styleId="fechaActualizacionView" size="15" maxlength="10" styleClass="datos" />
									<a class="link_siat" onclick="return show_calendar('fechaActualizacionView');" id="a_fechaActualizacionView">
										<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
								</td>
							</tr>
							<tr>
								<td><label><bean:message bundle="gde" key="gde.multaEditAdapter.fechaNotificacion.label"/>: </label></td>
					      		<td class="normal">
									<html:text name="multaAdapterVO" property="multa.fechaNotificacionView" styleId="fechaNotificacionView" size="15" maxlength="10" styleClass="datos" />
									<a class="link_siat" onclick="return show_calendar('fechaNotificacionView');" id="a_fechaNotificacionView">
										<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
								</td>
							</tr>
							<tr>
					      		<td><label><bean:message bundle="gde" key="gde.multaEditAdapter.ordenControl.label"/>: </label></td>
					      		<td class="normal">
					      			<html:text name="multaAdapterVO" property="multa.ordenControl.numeroOrden" size="5" maxlength="8" styleClass="datos"/>
					      				 / <html:text name="multaAdapterVO" property="multa.ordenControl.anioOrden" size="4" maxlength="4" styleClass="datos"/>
					      				 <bean:message bundle="gde" key="gde.multaEditAdapter.mascaraPeriodo"/>
								</td>
							</tr>

						</logic:equal>
					
					
					<!-- Fin filtros si la cuenta posee esImporteManual=No -->
					<tr>
						<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
						<!-- Inclucion de CasoView -->
						<td colspan="3">
							<bean:define id="IncludedVO" name="multaAdapterVO" property="multa"/>
							<bean:define id="voName" value="multa" />
							<%@ include file="/cas/caso/includeCaso.jsp" %>				
						</td>
					</tr>
					<!-- Caso -->
					<logic:notEqual name="multaAdapterVO" property="multa.tipoMulta.esImporteManual.id" value="0">
							<tr>
								<td><label><bean:message bundle="gde" key="gde.multaEditAdapter.fechaResolucion.label"/>: </label></td>
								<td class="normal">
									<html:text name="multaAdapterVO" property="multa.fechaResolucionView" styleId="fechaResolucionView" size="15" maxlength="10" styleClass="datos" />
										<a class="link_siat" onclick="return show_calendar_change('fechaResolucionView');" id="a_fechaResolucionView">
											<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/>
										</a>
								</td>
							</tr>
						</logic:notEqual>
					

				</logic:equal>
				<!-- FIN es detalle valido false -->			
				</logic:equal>
				
				<logic:equal name="multaAdapterVO" property="act" value="modificar">
		
				<tr>
					<td>
						<label><bean:message bundle="pad" key="pad.cuenta.label"/>: </label>
					</td>
					<td class="normal">
						<bean:write name="multaAdapterVO" property="multa.cuenta.numeroCuenta"/>
					</td>
				</tr>
				<tr>
					<td>
						<label><bean:message bundle="def" key="def.recurso.label"/>: </label>
					</td>
					<td class="normal">
						<bean:write name="multaAdapterVO" property="multa.cuenta.recurso.desRecurso"/>
					</td>
				</tr>
				<tr>
					<td>
						<label><bean:message bundle="gde" key="gde.multaEditAdapter.fechaEmision.label"/>: </label>
					</td>
					<td class="normal">
						<bean:write name="multaAdapterVO" property="multa.fechaEmisionView"/>
					</td>
				</tr>
				<tr>
					<td>
						<label><bean:message bundle="gde" key="gde.multa.fechaVencimiento.label"/>: </label>
					</td>
					<td class="normal">
						<bean:write name="multaAdapterVO" property="multa.fechaVencimientoView"/>
					</td>
				</tr>
				<tr>
					<td>
						<label><bean:message bundle="gde" key="gde.multaEditAdapter.importe.label"/>: </label>
					</td>
					<td class="normal">
						<bean:write name="multaAdapterVO" property="multa.importeView"/>
					</td>
				</tr>
				<tr>
		      		<td><label><bean:message bundle="gde" key="gde.multaEditAdapter.fechaNotificacion.label"/>: </label></td>
		      		<td class="normal">
						<html:text name="multaAdapterVO" property="multa.fechaNotificacionView" styleId="fechaNotificacionView" size="15" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaNotificacionView');" id="a_fechaNotificacionView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
				</tr>
				<tr>
		      		<td><label><bean:message bundle="gde" key="gde.multa.fechaVencimiento.label"/>: </label></td>
		      		<td class="normal">
						<html:text name="multaAdapterVO" property="multa.fechaVencimientoView" styleId="fechaVencimientoEditView" size="15" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaVencimientoEditView');" id="a_fechaVencimientoEditView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
				</tr>
				</logic:equal>
		</table>
	</fieldset>	
	<!-- Multa -->
	
	<!-- Filtros si la cuenta posee esImporteManual=Si -->
	<logic:equal name="multaAdapterVO" property="act" value="agregar">
		<logic:equal name="multaAdapterVO" property="multa.tipoMulta.esImporteManual.id" value="1">
			<logic:equal name="multaAdapterVO" property="detalleValido" value="true">
				<fieldset>
					<legend><bean:message bundle="gde" key="gde.multaDet.label"/></legend>
					
					<table class="tabladatos">
						<logic:notEqual name="multaAdapterVO" property="multa.tieneRangosDeMinimos" value="true">
							<tr>
								<td><label>(*)<bean:message bundle="gde" key="gde.deuda.importe.label"/>: </label></td>
								<td class="normal">
									<logic:equal name="multaAdapterVO" property="multa.modificaImporte" value="true">
						    			<html:text name="multaAdapterVO" property="multa.importe" size="8" maxlength="12" styleClass="datos"/>
						    		</logic:equal>
						    		<logic:notEqual name="multaAdapterVO" property="multa.modificaImporte" value="true">
						    			<bean:write name="multaAdapterVO" property="multa.importeView"/>
						    		</logic:notEqual>
								</td>
							</tr>
							<tr>
								<td><label>(*)<bean:message bundle="gde" key="gde.deuda.fechaVencimiento.label"/>: </label></td>
								<td class="normal">
									<html:text name="multaAdapterVO" property="multa.fechaVencimientoView" styleId="fecVencimientoView" size="15" maxlength="10" styleClass="datos" />
									<a class="link_siat" onclick="return show_calendar_change('fecVencimientoView');" id="a_fecVencimientoView">
										<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/>
									</a>
								</td>
							</tr>
						</logic:notEqual>
						<logic:equal name="multaAdapterVO" property="multa.tieneRangosDeMinimos" value="true">
							<tr>
								<td><label><bean:message bundle="gde" key="gde.multa.minimoRecurso.label"/>: </label></td>
								<td class="normal"><bean:write name="multaAdapterVO" property="multa.minimoView"/></td>
							</tr>
							<tr>
								<td><label><bean:message bundle="gde" key="gde.multa.tipoMulta.rangoMinimos.label"/>: </label></td>
								<td class="normal"><bean:write name="multaAdapterVO" property="multa.tipoMulta.rango"/></td>
							</tr>
							<tr>
								<td><label><bean:message bundle="gde" key="gde.multa.canMin.label"/>: </label></td>
								<td class="normal">
									<html:text name="multaAdapterVO" styleId="canMin" property="multa.canMinView" size="8" onchange="calcularImporte();"/>
								</td>
							</tr>
							<tr>
								<td><label><bean:message bundle="gde" key="gde.deuda.importe.label"/>: </label></td>
								<td class="normal">
									<html:text name="multaAdapterVO" property="multa.importeView" size="8" styleId="importeCalc"/>
								</td>
							</tr>
							<tr>
								<td>
									<label><bean:message bundle="gde" key="gde.multa.fechaVencimiento.label"/>: </label>
								</td>
								<td class="normal">
									<html:text name="multaAdapterVO" property="multa.fechaVencimientoView" styleId="fechaVencimientoView" size="15" maxlength="10" styleClass="datos"/>
									<a class="link_siat" onclick="return show_calendar_change('fechaVencimientoView');" id="a_fechaVencimientoView">
										<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/>
									</a>
								</td>
							</tr>
						</logic:equal>
					</table>
				</fieldset>
			</logic:equal>
		</logic:equal>
	</logic:equal>

	<!-- Filtros si la cuenta posee esImporteManual=No -->
	<logic:equal name="multaAdapterVO" property="multa.tipoMulta.esImporteManual.id" value="0">
	<logic:equal name="multaAdapterVO" property="detalleValido" value="true">
	
			<fieldset>
				<legend><bean:message bundle="gde" key="gde.multaDet.label"/></legend>
				
				<table class="tabladatos">
					<tr>
						<td><label><bean:message bundle="gde" key="gde.multaEditAdapter.resolucion.label"/>: </label></td>
						<!-- Inclucion de CasoView -->
						<td>
							<bean:define id="IncludedVO" name="multaAdapterVO" property="multa"/>
							<bean:define id="voName" value="multa" />
							<%@ include file="/cas/caso/includeCaso.jsp" %>				
						</td>
					</tr>
					<tr>
						<td><label><bean:message bundle="gde" key="gde.multaEditAdapter.fechaResolucion.label"/>: </label></td>
						<td class="normal">
							<html:text name="multaAdapterVO" property="multa.fechaResolucionView" styleId="fechaResolucionView" size="15" maxlength="10" styleClass="datos" />
								<a class="link_siat" onclick="return show_calendar_change('fechaResolucionView');" id="a_fechaResolucionView">
									<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/>
								</a>
						</td>
					</tr>
					<logic:equal name="multaAdapterVO" property="esTipoRevision" value="0">
						<tr>
				    		<td><label><bean:message bundle="gde" key="gde.descuentoMulta.label"/>: </label></td>
							<td class="normal">
								<html:select name="multaAdapterVO" property="multa.descuentoMulta.id" styleClass="select" style="width:100%" onchange="submitForm('paramDescuentoMulta', '');">
									<html:optionsCollection name="multaAdapterVO" property="listDescuentoMulta" label="descripcion" value="id" />
								</html:select>
							</td>
						</tr>
					</logic:equal>	
				</table>
			</fieldset>
		
		<!-- Resultado MultaDet -->		
		<logic:equal name="multaAdapterVO" property="esTipoRevision" value="0">
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="gde" key="gde.multaDet.label"/></caption>
		    	<tbody>
					<logic:notEmpty  name="multaAdapterVO" property="multa.listMultaDet">
						<tr>
							<th align="left"><bean:message bundle="ef" key="ef.periodoOrden.periodo.label"/></th>
							<th align="left"><bean:message bundle="ef" key="ef.periodoOrden.anio.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.multaDet.importeBase.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.multaDet.importeAct.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.multaDet.porOri.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.multaDet.totalOriginal.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.multaDet.porDes.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.multaDet.importeAplicado.label"/></th>
						</tr>	    	
						<logic:iterate id="MultaDetVO" name="multaAdapterVO" property="multa.listMultaDet" indexId="count">
							<tr>
								<td><bean:write name="MultaDetVO" property="periodoView"/>&nbsp;</td>
								<td><bean:write name="MultaDetVO" property="anioView" />&nbsp;</td>
								<td><bean:write name="MultaDetVO" property="importeBaseView"/>&nbsp;</td>
								<td><bean:write name="MultaDetVO" property="importeActView"/>&nbsp;</td>
								<td><bean:write name="MultaDetVO" property="porOriView"/>&nbsp;</td>
								<td><bean:write name="MultaDetVO" property="totalOriginalView"/></td>
								<td><bean:write name="MultaDetVO" property="porDesView"/>&nbsp;</td>
								<td><bean:write name="MultaDetVO" property="importeAplicadoView"/>&nbsp;</td>
							</tr>
						</logic:iterate>
						<td colspan="7" class="celdatotales"> &nbsp;</td>       		
			       		<td class="celdatotales" align="left">
			       			<bean:message bundle="gde" key="gde.multaEditAdpater.total.label"/>:
				        	<b><bean:write name="multaAdapterVO" property="totalMultaView" bundle="base" formatKey="general.format.currency"/></b>
				        </td>
					</logic:notEmpty>
					
					<logic:empty  name="multaAdapterVO" property="multa.listMultaDet">
						<tr><td align="center">
						<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
					</logic:empty>					
				</tbody>
			</table>
		</logic:equal>
		
		<!-- Multa por revision -->
		<logic:equal name="multaAdapterVO" property="esTipoRevision" value="1">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="gde" key="gde.multaDet.label"/></caption>
		    	<tbody>
					<logic:notEmpty  name="multaAdapterVO" property="multa.listMultaDet">
						<tr>
							<th align="left"><bean:message bundle="ef" key="ef.periodoOrden.periodo.label"/></th>
							<th align="left"><bean:message bundle="ef" key="ef.periodoOrden.anio.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.multaDet.importeBase.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.multaDet.pagoContadoOBueno.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.multaDet.resto.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.multaDet.pagoActualizado.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.multaDet.restoActualizado.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.multaDet.aplicado.label"/></th>
						</tr>	    	
						<logic:iterate id="MultaDetVO" name="multaAdapterVO" property="multa.listMultaDet" indexId="count">
							<tr>
								<td><bean:write name="MultaDetVO" property="periodoView"/>&nbsp;</td>
								<td><bean:write name="MultaDetVO" property="anioView" />&nbsp;</td>
								<td><bean:write name="MultaDetVO" property="importeBaseView"/>&nbsp;</td>
								<td><bean:write name="MultaDetVO" property="pagoContadoOBuenoView"/>&nbsp;</td>
								<td><bean:write name="MultaDetVO" property="restoView"/>&nbsp;</td>
								<td><bean:write name="MultaDetVO" property="pagoActualizadoView"/></td>
								<td><bean:write name="MultaDetVO" property="restoActualizadoView"/>&nbsp;</td>
								<td><bean:write name="MultaDetVO" property="aplicadoView"/>&nbsp;</td>
							</tr>
						</logic:iterate>
						
					</logic:notEmpty>
					
					<logic:empty  name="multaAdapterVO" property="multa.listMultaDet">
						<tr><td align="center">
						<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
					</logic:empty>					
				</tbody>
			</table>
			<fieldset>
				<legend><bean:message bundle="gde" key="gde.multaDet.importes.label"/></legend>
				<table class="tabladatos">
					<tr>						     	
			       		<td class="normal">
			       			<label><bean:message bundle="gde" key="gde.multaEditAdpater.totalAplicado.label"/>:</label>
				        	<b><bean:write name="multaAdapterVO" property="multa.totalAplicadoView" bundle="base" formatKey="general.format.currency"/></b>
				        </td>
				    </tr>
				    <tr>						      	
			       		<td class="normal">			       			
				        	<label><bean:message bundle="gde" key="gde.multaEditAdpater.importeMultaAnterior.label"/>:</label>
				        	<b><bean:write name="multaAdapterVO" property="multa.importeMultaAnteriorView" bundle="base" formatKey="general.format.currency"/></b>
				        
				        </td>
				    </tr>
				    <tr>						  		
			       		<td class="normal">			       							      
				        	<label><bean:message bundle="gde" key="gde.multaEditAdpater.totalEmitir.label"/>:</label>
				        	<b><bean:write name="multaAdapterVO" property="multa.importeView" bundle="base" formatKey="general.format.currency"/></b>
				        </td>
				    </tr>
				</table>
			</fieldset>
		</logic:equal>
		<!-- Fin Multa por revision -->		
		
		<!-- Fin MultaDet -->
	</logic:equal>
	</logic:equal>
	
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="multaAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="multaAdapterVO" property="act" value="agregar">
					<logic:equal name="multaAdapterVO" property="detalleValido" value="true">
						<logic:equal name="multaAdapterVO" property="multa.tipoMulta.esImporteManual.id" value="0">
							<html:button property="btnImprimir"  styleClass="boton" onclick="submitForm('imprimir', '');">
								<bean:message bundle="base" key="abm.button.imprimir"/>
							</html:button>
							&nbsp;
							<html:button property="btnModifDetalle" styleClass="boton" onclick="submitForm('modificarPorcentaje','');">
								<bean:message bundle="gde" key="gde.multa.btnModificarPorcentaje"/>
							</html:button>
						</logic:equal>	
						&nbsp;
						<html:button property="btnAgregar"  styleClass="boton" onclick="submitForm('agregar', '');">
							<bean:message bundle="base" key="abm.button.agregar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="multaAdapterVO" property="detalleValido" value="false">	
						<html:button property="btnMostrarDetalle"  styleClass="boton" onclick="submitForm('paramDetalleMulta', '');">
							<bean:message bundle="base" key="abm.button.mostrarDetalle"/>
						</html:button>
					</logic:equal>
				</logic:equal>

   	    	</td
   	    	</td>   	    	
   	    </tr>
   	</table>
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
	<input type="hidden" name="minimo" value="<bean:write name="multaAdapterVO" property="multa.minimoView"/>" id="minimo"/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>
	
	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->