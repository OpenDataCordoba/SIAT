<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/AdministrarReclamo.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="bal" key="bal.reclamoEditAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>

	<!-- Reclamo -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.reclamo.title"/></legend>
			<table class="tabladatos">

			<tr>
				<td><label><bean:message bundle="bal" key="bal.reclamo.nroReclamo.label"/>: </label></td>
				<td class="normal"><label><bean:write name="reclamoAdapterVO" property="reclamo.id" bundle="base" formatKey="general.format.id"/></label></td>
			</tr>
		
			<!-- Datos Deuda -->
			<logic:equal name="reclamoAdapterVO" property="reclamo.tipoBoleta" value="1">
			<tr>
				<td><label><bean:message bundle="bal" key="bal.reclamo.tipoBoleta.label"/>: </label></td>
				<td class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.desTipoBoleta"/></td>

				<td><label><bean:message bundle="bal" key="bal.reclamo.periodo.label"/> / <bean:message bundle="bal" key="bal.reclamo.anio.label"/>: </label></td>
				<td class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.periodoView"/> / <bean:write name="reclamoAdapterVO" property="reclamo.anioView"/></td>
			</tr>
			</logic:equal>

			<!-- Datos Cuota -->
			<logic:equal name="reclamoAdapterVO" property="reclamo.tipoBoleta" value="3">
			<tr>
				<td><label><bean:message bundle="bal" key="bal.reclamo.tipoBoleta.label"/>: </label></td>
				<td class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.desTipoBoleta"/></td>
				<td><label><bean:message bundle="bal" key="bal.reclamo.nroConvenio.label"/> / <bean:message bundle="bal" key="bal.reclamo.nroCuota.label"/>: </label></td>
				<td class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.nroConvenioView"/> / <bean:write name="reclamoAdapterVO" property="reclamo.nroCuotaView"/></td>				
			</tr>
			</logic:equal>

			<!-- Datos extra de deuda o cuota: via y procurador-->
			<tr>
				<td><label><bean:message bundle="bal" key="bal.reclamo.desViaDeuda.label"/>: </label></td>
				<td class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.viaDeuda.desViaDeuda"/></td>
				<logic:equal name="reclamoAdapterVO" property="reclamo.viaDeuda.id" value="2"> <!-- solo si es judicial mostramos procurador -->
					<td><label><bean:message bundle="bal" key="bal.reclamo.desProcurador.label"/>: </label></td>
					<td class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.procurador.descripcion"/></td>
				</logic:equal>
			</tr>

			<!-- Datos de la cuenta -->
			<tr>
				<td><label><bean:message bundle="bal" key="bal.reclamo.nroCuenta.label"/>: </label></td>
				<td class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.recurso.codRecurso"/> - <bean:write name="reclamoAdapterVO" property="reclamo.nroCuentaView"/></td>
				<td><label><bean:message bundle="bal" key="bal.canal.label"/>: </label></td>
				<td class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.canal.desCanal"/>&nbsp;&nbsp;<bean:write name="reclamoAdapterVO" property="reclamo.infoUsuarioAlta"/></td>
			</tr>

			<!-- Datos ingresados por el usuario -->
			<tr><td>&nbsp;</td></tr>

			<tr>
				<td><label><bean:message bundle="bal" key="bal.reclamo.desBanco.label"/>: </label></td>
				<td class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.banco.desBanco"/></td>
			</tr>

			<tr>
				<td><label><bean:message bundle="bal" key="bal.reclamo.fechaPago.label"/>: </label></td>
				<td class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.fechaPagoView"/></td>
				<td><label><bean:message bundle="bal" key="bal.reclamo.importePagado.label"/>: </label></td>
				<td class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.importePagadoView"/></td>
			</tr>

			<tr>
				<td><label><bean:message bundle="bal" key="bal.reclamo.nombre.label"/>: </label></td>
				<td class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.nombre"/></td>
				<td><label><bean:message bundle="bal" key="bal.reclamo.apellido.label"/>: </label></td>
				<td class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.apellido"/></td>
			</tr>

			<tr>
				<td><label><bean:message bundle="bal" key="bal.reclamo.nroDoc.label"/>: </label></td>
				<td class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.tipoDocView"/> - <bean:write name="reclamoAdapterVO" property="reclamo.nroDocView"/></td>
				<td><label><bean:message bundle="bal" key="bal.reclamo.correoElectronico.label"/>: </label></td>
				<td class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.correoElectronico"/></td>
			</tr>
			
			<tr>
				<td><label><bean:message bundle="bal" key="bal.reclamo.observacion.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="reclamoAdapterVO" property="reclamo.observacion"/></td>
			</tr>
			
			<tr><td>&nbsp;</td></tr>
			
			</table>
			
		</fieldset>
		<!--Fin Datos Reclamo -->
	
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="left">
	   				<html:button property="btnBuscarMasDatos"  styleClass="boton" onclick="submitForm('buscarMasDatos', '');">
						<bean:message bundle="bal" key="bal.reclamo.button.buscarMasDatos"/>
					</html:button>
				</td>
			</tr>
		</table>		

		<logic:equal name="reclamoAdapterVO" property="paramMasDatos" value="true">
		<!-- Datos Complementarios -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.reclamo.titleDatosExtra"/></legend>
			<table class="tabladatos">
			<logic:equal name="reclamoAdapterVO" property="reclamo.esDeuda" value="true">
			<tr>
				<td colspan="2"><label><bean:message bundle="bal" key="bal.reclamoEditAdapter.deuda.codRefPag.label"/>: </label></td>
				<td class="normal" colspan="2"><bean:write name="reclamoAdapterVO" property="reclamo.deudaReclamada.codRefPagView"/></td>
			</tr>
			</table>
			<tr><td>&nbsp;</td></tr>
			<table class="tabladatos">
			<caption><label><bean:message bundle="bal" key="bal.reclamoEditAdapter.reciboDeuda.label"/> </label></caption>
			<logic:notEmpty  name="reclamoAdapterVO" property="reclamo.listReciboDeuda">	
					<tr>
						<td align="center"><label><bean:message bundle="gde" key="gde.recibo.nroRecibo.label"/> </label></td>
						<td align="center"><label><bean:message bundle="gde" key="gde.recibo.codRefPag.label"/> </label></td>
						<td align="center"><label><bean:message bundle="gde" key="gde.recibo.fechaGeneracion.label"/> </label></td>
						<td align="center"><label><bean:message bundle="gde" key="gde.recibo.fechaVencimiento.label"/> </label></td>
						<td align="center"><label><bean:message bundle="gde" key="gde.recibo.fechaPago.label"/> </label></td>
						<td></td>	
					</tr>
				<logic:iterate id="ReciboVO" name="reclamoAdapterVO" property="reclamo.listReciboDeuda">
					<tr>
						<td align="center"><bean:write name="ReciboVO" property="nroReciboView"/></td>
						<td align="center"><bean:write name="ReciboVO" property="codRefPagView"/></td>
						<td align="center"><bean:write name="ReciboVO" property="fechaGeneracionView"/></td>
						<td align="center"><bean:write name="ReciboVO" property="fechaVencimientoView"/></td>
						<td align="center"><bean:write name="ReciboVO" property="fechaPagoView"/></td>					
						<td></td>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
			</logic:equal>
	
			<logic:equal name="reclamoAdapterVO" property="reclamo.esCuota" value="true">
			<tr>
				<td colspan="2"><label><bean:message bundle="bal" key="bal.reclamoEditAdapter.cuota.codRefPag.label"/>: </label></td>
				<td class="normal" colspan="2"><bean:write name="reclamoAdapterVO" property="reclamo.cuotaReclamada.codRefPagView"/></td>
			</tr>
			</table>
			<tr><td>&nbsp;</td></tr>
			<table class="tabladatos">
			<caption><label><bean:message bundle="bal" key="bal.reclamoEditAdapter.reciboCuota.label"/></label></caption>
			<logic:notEmpty  name="reclamoAdapterVO" property="reclamo.listReciboConvenio">	
					<tr>
						<td align="center"><label><bean:message bundle="gde" key="gde.recibo.nroRecibo.label"/> </label></td>
						<td align="center"><label><bean:message bundle="gde" key="gde.recibo.codRefPag.label"/> </label></td>
						<td align="center"><label><bean:message bundle="gde" key="gde.recibo.fechaGeneracion.label"/> </label></td>
						<td align="center"><label><bean:message bundle="gde" key="gde.recibo.fechaVencimiento.label"/> </label></td>
						<td align="center"><label><bean:message bundle="gde" key="gde.recibo.fechaPago.label"/> </label></td>
						<td></td>
					</tr>
				<logic:iterate id="ReciboVO" name="reclamoAdapterVO" property="reclamo.listReciboConvenio">
					<tr>
						<td align="center"><bean:write name="ReciboVO" property="nroReciboView"/></td>
						<td align="center"><bean:write name="ReciboVO" property="codRefPagView"/></td>
						<td align="center"><bean:write name="ReciboVO" property="fechaGeneracionView"/></td>
						<td align="center"><bean:write name="ReciboVO" property="fechaVencimientoView"/></td>
						<td align="center"><bean:write name="ReciboVO" property="fechaPagoView"/></td>					
						<td></td>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
			</logic:equal>
				
			</table>
		</fieldset>
		<!-- Fin Datos Complementarios -->

		<!-- Indeterminados encontrados-->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.reclamo.titleIndet"/></legend>
				<logic:notEmpty  name="reclamoAdapterVO" property="listIndet">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<th width="1">&nbsp;</th> <!-- Modificar -->
							<th align="left"><bean:message bundle="bal" key="bal.indet.codIndet.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.indet.fechaBalance.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.indet.fechaPago.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.indet.importeCobrado.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.indet.caja.label"/></th>
						</tr>
							
						<logic:iterate id="IndetVO" name="reclamoAdapterVO" property="listIndet">
							<tr>
									<!-- Ver -->
									<td>
										<logic:equal name="reclamoAdapterVO" property="verEnabled" value="enabled">									
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verIndet', '<bean:write name="IndetVO" property="nroIndeterminado" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="IndetVO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</td>	
									<!-- Modificar-->								
									<td>
										<logic:equal name="reclamoAdapterVO" property="modificarEnabled" value="enabled">
											<logic:equal name="IndetVO" property="modificarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarIndet', '<bean:write name="IndetVO" property="nroIndeterminado" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="IndetVO" property="modificarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="reclamoAdapterVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</td>
								<td><bean:write name="IndetVO" property="codIndetView"/>&nbsp;</td>
								<td><bean:write name="IndetVO" property="fechaBalanceView" />&nbsp;</td>
								<td><bean:write name="IndetVO" property="fechaPagoView"/>&nbsp;</td>
								<td><bean:write name="IndetVO" property="importeCobradoView"/>&nbsp;</td>
								<td><bean:write name="IndetVO" property="cajaView"/>&nbsp;</td>								
							</tr>
						</logic:iterate>
					
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="reclamoAdapterVO" property="listIndet">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
                	<tbody>
						<tr><td align="center">
							<bean:message bundle="base" key="base.resultadoVacio"/>
						</td></tr>
					</tbody>			
				</table>
			</logic:empty>
	
			<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	   				<html:button property="btnBuscarIndet"  styleClass="boton" onclick="submitForm('buscarIndet', '');">
						<bean:message bundle="bal" key="bal.reclamo.button.buscarIndet"/>
					</html:button>
				</td>
			</tr>
		</table>	
		</fieldset>
		<!-- Fin Indeterminados encontrados-->
		</logic:equal>
			
			<fieldset>
			<legend><bean:message bundle="bal" key="bal.reclamo.titleCambioEstado"/></legend>
	
			<table class="tabladatos">
			<!-- Estado reclamo -->
			<tr>	
				<td><label><bean:message bundle="bal" key="bal.reclamo.estadoReclamo.label"/>: </label></td>
				<td class="normal">
					<html:select name="reclamoAdapterVO" property="reclamo.estadoReclamo.id" styleClass="select" onchange="submitForm('paramEstadoReclamo', '');">
						<html:optionsCollection name="reclamoAdapterVO" property="listEstadoReclamo" label="desEstadoReclamo" value="id" />
					</html:select>
				</td>					

				<td><label><bean:message bundle="bal" key="bal.reclamo.enviaMail.label"/>: </label></td>
				<td class="normal">
					<input type="checkbox" name="enviarMail">
				</td>
			</tr>
			
			<!-- Respuesta -->
			<tr><td>&nbsp;</td></tr>
			<tr>
				<td><label>A:</label></td>
				<td colspan="3" class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.correoElectronico"/></td>
			</tr>
			
			<tr>
				<td><label>Asunto:</label></td>
				<td colspan="3" class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.asuntoCorreo"/></td>
			</tr>

			<tr>
				<td><label>&nbsp;</label></td>
				<td colspan="3" class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.encabezadoCorreoView" filter="false"/></td>
			</tr>
			
			<tr>
				<td><label><bean:message bundle="bal" key="bal.reclamo.respuesta.label"/>:</label></td>
				<td colspan="3" class="normal">
				<html:textarea style="height:160px;width:380px" name="reclamoAdapterVO" property="reclamo.respuesta"></html:textarea></td>
			</tr>
<!--
<bean:write name="reclamoAdapterVO" property="reclamo.esMigrada"/>-
<bean:write name="reclamoAdapterVO" property="reclamo.esDeuda"/>-
<bean:write name="reclamoAdapterVO" property="reclamo.esCuota"/>-
<bean:write name="reclamoAdapterVO" property="reclamo.tieneRecibo"/>
-->


			</table>
		</fieldset>
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="reclamoAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="reclamoAdapterVO" property="act" value="agregar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
						<bean:message bundle="base" key="abm.button.agregar"/>
					</html:button>
				</logic:equal>
   	    	</td>   	    	
   	    </tr>
   	</table>
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
   
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
