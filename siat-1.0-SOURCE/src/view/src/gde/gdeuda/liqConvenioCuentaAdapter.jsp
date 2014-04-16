<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarLiqConvenioCuenta.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				

	<!-- Para la web lo muestro, para usuario Interno NO -->
	<logic:equal name="userSession" property="isAnonimoView" value="1">
		<%@ include file="/gde/gdeuda/includeDivButtons.jsp" %>
	</logic:equal>	
	
	<h1><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.title"/></h1>	
	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>
					<%=ar.gov.rosario.siat.fra.iface.util.Frase.getInstance().getMessageBykey("gde.detalleConvenio.leyendaEncabezado")%>
				</p>
			</td>				
			<td align="right" width="10%">
	 			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	
	<!-- LiqCuenta -->
	<logic:notEqual name="liqConvenioCuentaAdapterVO" property="cuenta.idCuenta" value="">
			<bean:define id="DeudaAdapterVO" name="liqConvenioCuentaAdapterVO"/>
			<%@ include file="/gde/gdeuda/includeLiqCuenta.jsp" %>
	</logic:notEqual>
	<!-- LiqCuenta -->
	
	<!-- Procedimiento -->
	<logic:equal name="liqConvenioCuentaAdapterVO" property="cuenta.idCuenta" value="">
		<bean:define id="procedimientoVO" name="liqConvenioCuentaAdapterVO" property="procedimiento"/>
		<%@ include file="/cyq/concursoyQuiebra/includeEncProcedimiento.jsp"%>
	<!-- Procedimiento -->
	</logic:equal>
	
	<!-- LiqConvenio -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.title"/> </legend>
			
			<!-- Nro Convenio -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.nroConvenio.label"/>:</label>
	      		<bean:write name="liqConvenioCuentaAdapterVO" property="convenio.nroConvenio"/>
			</p>
			
			<!-- Plan de Pago -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.planPago.label"/>:</label>
	      		<bean:write name="liqConvenioCuentaAdapterVO" property="convenio.desPlan"/>
	      	</p>
	      	<logic:equal name="liqConvenioCuentaAdapterVO" property="convenio.poseeCaso" value="true" >
	      		<p>	
					<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.caso.label"/>:</label>
					<bean:write name="liqConvenioCuentaAdapterVO" property="convenio.caso.sistemaOrigen.desSistemaOrigen"/>
					&nbsp;							
					<bean:write name="liqConvenioCuentaAdapterVO" property="convenio.caso.numero"/>	
				</p>
	      	</logic:equal>
			<!-- Via Deuda -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.viaDeuda.label"/>:</label>
	      		<bean:write name="liqConvenioCuentaAdapterVO" property="convenio.desViaDeuda"/>
			</p>
			<logic:equal name="liqConvenioCuentaAdapterVO" property="convenio.poseeProcurador" value="true" >
				<p>	
					<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.procurador.label"/>:</label>
		      		<bean:write name="liqConvenioCuentaAdapterVO" property="convenio.procurador.idView"/> - 
		      		<bean:write name="liqConvenioCuentaAdapterVO" property="convenio.procurador.descripcion"/>
				</p>
	      	</logic:equal>
			
			<p class="msgBold">
			<!-- Estado Convenio -->
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.estadoConvenio.label"/>:</label>
	      		<bean:write name="liqConvenioCuentaAdapterVO" property="convenio.desEstadoConvenio"/>
			</p>
			<!-- Cantidad Cuotas -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.cantCuotas.label"/>:</label>
	      		<bean:write name="liqConvenioCuentaAdapterVO" property="convenio.canCuotasPlan"/>
			</p>
			<!-- Total Conveniado -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.totalImporteConvenido.label"/>:</label>
	      		<bean:write name="liqConvenioCuentaAdapterVO" property="convenio.totImporteConvenio" bundle="base" formatKey="general.format.currency"/>
			</p>
				
	</fieldset>
	<!-- LiqConvenio -->
	
	<!-- Datos de Formalizacion -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.title"/> </legend>
			<!-- Fecha Formalizacion -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.fechaFor.label"/>:</label>
	      		<bean:write name="liqConvenioCuentaAdapterVO" property="convenio.fechaFor"/>
			</p>
			<logic:notEqual name="liqConvenioCuentaAdapterVO" property="convenio.fechaAltaView" value="">
	      		<p>
	      			<label><bean:message bundle="pad" key="pad.cuenta.fechaAlta.label"/>:</label>
	      			<bean:write name="liqConvenioCuentaAdapterVO" property="convenio.fechaAltaView"/>
	      		</p>
	      	</logic:notEqual>
			
			<logic:equal name="liqConvenioCuentaAdapterVO" property="convenio.poseeDatosPersona" value="true" >
				<!-- Apellido -->
				<p>
		      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.apellido.label"/>:</label>
		      		<bean:write name="liqConvenioCuentaAdapterVO" property="convenio.persona.apellido"/>
				<!-- Nombre -->
		      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.nombre.label"/>:</label>
		      		<bean:write name="liqConvenioCuentaAdapterVO" property="convenio.persona.nombres"/>
				</p>
				<!-- Apellido Materno -->
				<p>
		      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.apellidoMaterno.label"/>:</label>
		      		<bean:write name="liqConvenioCuentaAdapterVO" property="convenio.persona.apellidoMaterno"/>
				<!-- Tipo y Nro Doc -->
		      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.tipoNroDoc.label"/>:</label>
		      		<bean:write name="liqConvenioCuentaAdapterVO" property="convenio.persona.documento.tipoyNumeroView"/>
				</p>
			</logic:equal>
			
			<!--  logic:notEqual name="liqConvenioCuentaAdapterVO" property="convenio.poseeDatosPersona" value="true" >
				<p>
					<label>bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.sinDatosPerFor"/></label>
				</p>				
			/logic:notEqual-->
			
			<!-- Tipo Per For -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.desTipoPerFor.label"/>:</label>
	      		<bean:write name="liqConvenioCuentaAdapterVO" property="convenio.tipoPerFor.desTipoPerFor"/>
			<!-- Domicilio -->
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.desDomicilioParticular.label"/>:</label>
	      		<bean:write name="liqConvenioCuentaAdapterVO" property="convenio.persona.domicilio.view"/>
			</p>
			
			<!-- Telefono -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.telefono.label"/>:</label>
	      		<bean:write name="liqConvenioCuentaAdapterVO" property="convenio.persona.telefono"/>
			<!-- Tipo Doc Aportada -->
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.desTipoDocApo.label"/>:</label>
	      		<bean:write name="liqConvenioCuentaAdapterVO" property="convenio.tipoDocApo.desTipoDocApo"/>
			</p>
			
			<!-- Obs -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.observacionFor.label"/>:</label>
	      		<bean:write name="liqConvenioCuentaAdapterVO" property="convenio.observacionFor"/>
			</p>
			<p>
			<!-- Agente Interviniente -->
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.ususarioFor.label"/>:</label>
	      		<bean:write name="liqConvenioCuentaAdapterVO" property="convenio.ususarioFor"/>
			</p>

	</fieldset>
	<!-- Datos de Formalizacion -->
	
	<!-- Periodos Incluidos -->	
	<div class="horizscroll">
	    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
	    	<caption><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.periodosIncluidos.title"/></caption>
	      	<tbody>
	      	<!-- Item LiqDeudaVO -->
		       	<tr>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqDeuda.periodoDeuda.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqDeuda.fechaVto.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqDeuda.saldo.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqDeuda.actualizacion.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqDeuda.total.label"/></th>
				</tr>
				<!-- Item LiqDeudaVO -->
				<logic:iterate id="LiqPeriodo" name="liqConvenioCuentaAdapterVO" property="convenio.listPeriodoIncluido">
			  		<tr>	
			  			<td><bean:write name="LiqPeriodo" property="periodoDeuda"/>&nbsp;</td>
				        <td><bean:write name="LiqPeriodo" property="fechaVto"/>&nbsp;</td>
				        <td><bean:write name="LiqPeriodo" property="saldo" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
				        <td><bean:write name="LiqPeriodo" property="actualizacion"  bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
				        <td><bean:write name="LiqPeriodo" property="total"  bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
			       	</tr>
				</logic:iterate>
				
				<tr>
					<td colspan="2" class="celdatotales"> &nbsp;</td>       		
		       		<td class="celdatotales" align="left">
			        	<b><bean:write name="liqConvenioCuentaAdapterVO" property="convenio.subTotalImpDeuda" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
			        <td class="celdatotales" align="left">
			        	<b><bean:write name="liqConvenioCuentaAdapterVO" property="convenio.subTotalActDeuda" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
		       		<td class="celdatotales" align="left">
			        	<b><bean:write name="liqConvenioCuentaAdapterVO" property="convenio.totalPeriodos" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
		       	</tr>
				<tr>
					<td colspan="5" class="celdatotales" align="right">
						<bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqDeuda.periodos.Total"/>:<bean:write name="liqConvenioCuentaAdapterVO" property="convenio.totalPeriodos" bundle="base" formatKey="general.format.currency"/>
					</td>
				</tr>	
	      	</tbody>
		</table>
	</div>	
	<!-- Fin Periodos Incluidos -->	
	
	<!-- Cuotas Pagas-->	
	<div class="horizscroll">
	    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
	    	<caption><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.cuotasPagas.title"/></caption>
	      	<tbody>
	      	<!-- Item LiqCuotaVO -->
		       	<tr>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.codRefPag.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.nroCuota.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.fechaVto.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.capital.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.interes.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.actualizacion.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.total.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.fechaPago.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.desEstado.label"/></th>
				</tr>
				<!-- Item LiqDeudaVO -->
				<logic:notEmpty name="liqConvenioCuentaAdapterVO" property="convenio.listCuotaPaga">
					<logic:iterate id="LiqCuotaPaga" name="liqConvenioCuentaAdapterVO" property="convenio.listCuotaPaga">
				  		<tr>	
				  			<td><bean:write name="LiqCuotaPaga" property="codRefPagView"/>&nbsp;</td>
				  			<td><bean:write name="LiqCuotaPaga" property="nroCuota"/>&nbsp;</td>
					        <td><bean:write name="LiqCuotaPaga" property="fechaVto"/>&nbsp;</td>
				  			<td><bean:write name="LiqCuotaPaga" property="capital" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					        <td><bean:write name="LiqCuotaPaga" property="interes" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					        <td><bean:write name="LiqCuotaPaga" property="actualizacion"  bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					        <td><bean:write name="LiqCuotaPaga" property="total"  bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					        <td><bean:write name="LiqCuotaPaga" property="fechaPago"/>&nbsp;</td>
					        <td><bean:write name="LiqCuotaPaga" property="desEstado"/>&nbsp;</td>
				       	</tr>
					</logic:iterate>
					
					<tr>
						<td colspan="3" class="celdatotales"> &nbsp;</td>       		
			       		<td class="celdatotales" align="left">
				        	<b><bean:write name="liqConvenioCuentaAdapterVO" property="convenio.subTotalCapCuotasPagas" bundle="base" formatKey="general.format.currency"/></b>
				        </td>
				        <td class="celdatotales" align="left">
				        	<b><bean:write name="liqConvenioCuentaAdapterVO" property="convenio.subTotalIntCuotasPagas" bundle="base" formatKey="general.format.currency"/></b>
				        </td>
				        <td class="celdatotales" align="left">
				        	<b><bean:write name="liqConvenioCuentaAdapterVO" property="convenio.subTotalActCuotasPagas" bundle="base" formatKey="general.format.currency"/></b>
				        </td>
			       		<td class="celdatotales" align="left">
				        	<b><bean:write name="liqConvenioCuentaAdapterVO" property="convenio.totalCuotasPagas" bundle="base" formatKey="general.format.currency"/></b>
				        </td>
				        <td colspan="2"> &nbsp;</td>
			       	</tr>
		       	
					<tr>
						<td colspan="7" class="celdatotales" align="right">
							<bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqDeuda.periodos.Total"/>:
							<bean:write name="liqConvenioCuentaAdapterVO" property="convenio.totalCuotasPagas" bundle="base" formatKey="general.format.currency"/>
						</td>
						<td colspan="2">&nbsp;</td>
					</tr>						
				</logic:notEmpty>
				<logic:empty name="liqConvenioCuentaAdapterVO" property="convenio.listCuotaPaga">
					<tr>
						<td colspan="10"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.cuotasPagas.noSeRegistranCuotasPagas"/></td>
					</tr>
				</logic:empty>
	      	</tbody>
		</table>
		<logic:equal name="liqConvenioCuentaAdapterVO" property="convenio.cuoPagConSel" value="true">
			<p>(*) El importe de la cuota incluye $ <bean:write name="liqConvenioCuentaAdapterVO" property="convenio.importeSelladoView"/> de sellado por formalización de convenio</p>
		</logic:equal>
	</div>	
	<!-- Fin Cuotas Pagas -->
	
	<!-- Cuotas Impagas-->	
	<div class="horizscroll">
	    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
	    	<caption><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.cuotasImpagas.title"/></caption>
	      	<tbody>
	      	<!-- Item LiqCuotaVO -->
		       	<tr>
					<th align="center">
						<bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.blockImpaga.Seleccionar"/> 
						<logic:equal name="liqConvenioCuentaAdapterVO" property="mostrarChkAllCuotasPagas" value="true">
							<br>
					       	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					       	<input type="checkbox" onclick="changeChk('filter', 'listIdCuotaSelected', this)"/>
				       	</logic:equal>
					</th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.codRefPag.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.nroCuota.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.fechaVto.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.capital.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.interes.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.actualizacion.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.total.label"/></th>				
				</tr>
				<!-- Item LiqDeudaVO -->
				<logic:notEmpty name="liqConvenioCuentaAdapterVO" property="convenio.listCuotaInpaga">
					<logic:iterate id="LiqCuotaImpaga" name="liqConvenioCuentaAdapterVO" property="convenio.listCuotaInpaga">
				  		<tr>
				  		    <td align="center">
					  		    <logic:equal name="LiqCuotaImpaga" property="esSeleccionable" value="true">
				  					<html:multibox name="liqConvenioCuentaAdapterVO" property="listIdCuotaSelected" >
	                                	<bean:write name="LiqCuotaImpaga" property="idCuota" bundle="base" formatKey="general.format.id"/>
	                                </html:multibox>
				  				</logic:equal>
				  				<logic:notEqual name="LiqCuotaImpaga" property="esSeleccionable" value="true">
				  					<input type="checkbox" disabled="disabled"/>
				  				</logic:notEqual>
			  				</td>
			  				
			  				<td><bean:write name="LiqCuotaImpaga" property="codRefPagView"/>&nbsp;</td>
				  			<td><bean:write name="LiqCuotaImpaga" property="nroCuota"/>&nbsp;</td>
					        <td><bean:write name="LiqCuotaImpaga" property="fechaVto"/>&nbsp;</td>
					        
					        <!-- No posee Observacion -->
					        <logic:notEqual name="LiqCuotaImpaga" property="poseeObservacion" value="true">
					  			<td><bean:write name="LiqCuotaImpaga" property="capital" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
						        <td><bean:write name="LiqCuotaImpaga" property="interes" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
						        <td><bean:write name="LiqCuotaImpaga" property="actualizacion"  bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
						        <td><bean:write name="LiqCuotaImpaga" property="total"  bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
						        
					        </logic:notEqual>
					        
					        <!-- Es Indeterminada -->
					        <logic:equal name="LiqCuotaImpaga" property="esIndeterminada" value="true">
			       				<td colspan="4">
			       					<b><bean:write name="LiqCuotaImpaga" property="observacion"/>&nbsp;</b>
								</td>
			       			</logic:equal>
			       			
					        <!-- Es Reclamada -->
					        <logic:equal name="LiqCuotaImpaga" property="esReclamada" value="true">
			       				<td colspan="4">
			       					<b><bean:write name="LiqCuotaImpaga" property="observacion"/>&nbsp;</b>
								</td>
			       			</logic:equal>
					        
				       	</tr>
					</logic:iterate>
					
					<tr>	
						<td colspan="4" class="celdatotales"> &nbsp;</td>       		
			       		<td class="celdatotales" align="left">
				        	<b><bean:write name="liqConvenioCuentaAdapterVO" property="convenio.subTotalCapCuotasImpagas" bundle="base" formatKey="general.format.currency"/></b>
				        </td>
				        <td class="celdatotales" align="left">
				        	<b><bean:write name="liqConvenioCuentaAdapterVO" property="convenio.subTotalIntCuotasImpagas" bundle="base" formatKey="general.format.currency"/></b>
				        </td>
				        <td class="celdatotales" align="left">
				        	<b><bean:write name="liqConvenioCuentaAdapterVO" property="convenio.subTotalActCuotasImpagas" bundle="base" formatKey="general.format.currency"/></b>
				        </td>
			       		<td class="celdatotales" align="left">
				        	<b><bean:write name="liqConvenioCuentaAdapterVO" property="convenio.totalCuotasImpagas" bundle="base" formatKey="general.format.currency"/></b>
				        </td>
				       
				    </tr>   
					<tr>
						<td colspan="8" class="celdatotales" align="right">
							<bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqDeuda.periodos.Total"/>:
							<bean:write name="liqConvenioCuentaAdapterVO" property="convenio.totalCuotasImpagas" bundle="base" formatKey="general.format.currency"/>
						</td>
					
					</tr>
				</logic:notEmpty>
				<logic:empty name="liqConvenioCuentaAdapterVO" property="convenio.listCuotaInpaga">
					<tr>						
						<td colspan="8"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.cuotasImpagas.noSeRegistranCuotasImpagas"/></td>
					</tr>
				</logic:empty>
	      	</tbody>
		</table>
		<logic:equal name="liqConvenioCuentaAdapterVO" property="convenio.cuoInpConSel" value="true">
			<p>(*) El importe de la cuota incluye $ <bean:write name="liqConvenioCuentaAdapterVO" property="convenio.importeSelladoView"/> de sellado por formalización de convenio</p>
		</logic:equal>
	</div>	
	<!-- Fin Cuotas Impagas -->
	
	<!-- Acciones -->
	<fieldset>
		<p align="center">
				
			<!-- Reimprimir Recibos -->
			<logic:equal name="liqConvenioCuentaAdapterVO" property="imprimirRecibosVisible" value="true">
				<logic:equal name="liqConvenioCuentaAdapterVO" property="imprimirRecibosEnabled" value="true">
					<button type="button" name="btn1" class="boton" onclick="submitForm('printRecibos', '<bean:write name="liqConvenioCuentaAdapterVO" property="convenio.idConvenio" bundle="base" formatKey="general.format.id"/>');">
			    		<bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.button.reimprimir"/>
			    	</button>
			    </logic:equal>
				<logic:notEqual name="liqConvenioCuentaAdapterVO" property="imprimirRecibosEnabled" value="true">
					<button type="button" name="btn1" class="botonDeshabilitado" disabled="disabled">
			    		<bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.button.reimprimir"/>
			    	</button>
			    </logic:notEqual>			    
			</logic:equal>
			
			<!-- Imprimir Formulario Formalizacion -->
			<logic:equal name="liqConvenioCuentaAdapterVO" property="imprimirFormularioFormalVisible" value="true">
				<logic:equal name="liqConvenioCuentaAdapterVO" property="imprimirFormularioFormalEnabled" value="true">
					<button type="button" name="btn2" class="boton" onclick="submitForm('printForm', '<bean:write name="liqConvenioCuentaAdapterVO" property="convenio.idConvenio" bundle="base" formatKey="general.format.id"/>');">
			    		<bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.button.imprimirFrmFormal"/>
			    	</button>
			    </logic:equal>	
				<logic:notEqual name="liqConvenioCuentaAdapterVO" property="imprimirFormularioFormalEnabled" value="true">
					<button type="button" name="btn2" class="botonDeshabilitado" disabled="disabled">
			    		<bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.button.imprimirFrmFormal"/>
			    	</button>
			    </logic:notEqual>			    	
			</logic:equal>
			
			<!-- Generar Cuota Saldo -->
			<logic:equal name="liqConvenioCuentaAdapterVO" property="generarCuotaSaldoVisible" value="true">
				<logic:equal name="liqConvenioCuentaAdapterVO" property="generarCuotaSaldoEnabled" value="true">
					<button type="button" name="btn3" class="boton" onclick="submitForm('cuotaSaldoInit','<bean:write name="liqConvenioCuentaAdapterVO" property="convenio.idConvenio" bundle="base" formatKey="general.format.id"/>');">
			    		<bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.button.genCuotaSaldo"/>
			    	</button>
			    
			    </logic:equal>
			    <logic:notEqual name="liqConvenioCuentaAdapterVO" property="generarCuotaSaldoEnabled" value="true">
					<button type="button" name="btn3" class="botonDeshabilitado" disabled="disabled">
			    		<bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.button.genCuotaSaldo"/>
			    	</button>
			    </logic:notEqual>
			</logic:equal>
			
			<!-- Ver Pagos Convenio -->
			<button type="button" name="btn10" class="boton" onclick="submitForm('verPagosConvenio','');">
				<bean:message bundle="gde" key="gde.verPagosConvenio.verImputaciones.button.label"/>
			</button>
			
			<!-- Ver Historial de Estados -->
			<logic:equal name="liqConvenioCuentaAdapterVO" property="verHistoricoConvenio" value="true">
				<button type="button" name="btn11" class="boton" onclick="submitForm('verHistorial','');">
					<bean:message bundle="gde" key="gde.verHistorial.verEstadosConvenio.button.label"/>
				</button>
			</logic:equal>
			
			<!-- Rescate Individual -->
			<logic:equal name="liqConvenioCuentaAdapterVO" property="generarRescateVisible" value="true">
				<logic:equal name="liqConvenioCuentaAdapterVO" property="generarRescateEnabled" value="true">
					<button type="button" name="btn12" class="boton" onclick="submitForm('rescateIndividualInit','');">
						<bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.button.rescateIndiv.label"/>
					</button>
				</logic:equal>
				<logic:notEqual name="liqConvenioCuentaAdapterVO" property="generarRescateEnabled" value="true">
					<button type="button" name="btn12" class="botonDeshabilitado" disabled="disabled">
						<bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.button.rescateIndiv.label"/>
					</button>
				</logic:notEqual>
			</logic:equal>
			
			<!-- Generar Saldo por Caducidad -->
			<logic:equal name="liqConvenioCuentaAdapterVO" property="generarSaldoPorCaducidadVisible" value="true">
				<logic:equal name="liqConvenioCuentaAdapterVO" property="generarSaldoPorCaducidadEnabled" value="true">
					<logic:equal name="liqConvenioCuentaAdapterVO" property="tieneCuotaSaldo" value="true">
			    		<button type="button" name="btn4" class="boton" onclick="submitConfirmForm('salPorCadIndividualInit', '<bean:write name="liqConvenioCuentaAdapterVO" property="convenio.idConvenio" bundle="base" formatKey="general.format.id"/>', 'Existe una Cuota Saldo emitida sin Vencer, Desea continuar?');">
			    			<bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.button.genSalPorCaducidad"/>
			    		</button>
			    	</logic:equal>
			    	<logic:notEqual name="liqConvenioCuentaAdapterVO" property="tieneCuotaSaldo" value="true">
			    		<button type="button" name="btn6" class="boton" onclick="submitForm('salPorCadIndividualInit', '<bean:write name="liqConvenioCuentaAdapterVO" property="convenio.idConvenio" bundle="base" formatKey="general.format.id"/>');">
			    			<bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.button.genSalPorCaducidad"/>
			    		</button>
			    	</logic:notEqual>
			    </logic:equal>
			    <logic:notEqual name="liqConvenioCuentaAdapterVO" property="generarSaldoPorCaducidadEnabled" value="true">
			    	<button type="button" name="btn6" class="botonDeshabilitado" disabled="disabled">
			    		<bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.button.genSalPorCaducidad"/>
			    	</button>			    
			    </logic:notEqual>
			</logic:equal>
			
			<!-- Vuelta Atras Saldo por Caducidad -->
			<logic:equal name="liqConvenioCuentaAdapterVO" property="atrasSaldoPorCaducidadVisible" value="true">
				<logic:equal name="liqConvenioCuentaAdapterVO" property="atrasSaldoPorCaducidadEnabled" value="true">
					<button type="button" name="btn5" class="boton" onclick="submitForm('vueltaAtrasSalPorCadInit', '<bean:write name="liqConvenioCuentaAdapterVO" property="convenio.idConvenio" bundle="base" formatKey="general.format.id"/>');">
			    		<bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.button.atrasSalPorCaducidad"/>
			    	</button>
			    </logic:equal>
			    <logic:notEqual name="liqConvenioCuentaAdapterVO" property="atrasSaldoPorCaducidadEnabled" value="true">
					<button type="button" name="btn5" class="botonDeshabilitado" disabled="disabled">
			    		<bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.button.atrasSalPorCaducidad"/>
			    	</button>
			    </logic:notEqual>			    
			</logic:equal>
			
			<!-- Anular Convenio -->
			<logic:equal name="liqConvenioCuentaAdapterVO" property="anularConvenioVisible" value="true">
				<logic:equal name="liqConvenioCuentaAdapterVO" property="anularConvenioEnabled" value="true">
					<button type="button" name="btn5" class="boton" onclick="submitForm('anularConvenioInit', '<bean:write name="liqConvenioCuentaAdapterVO" property="convenio.idConvenio" bundle="base" formatKey="general.format.id"/>');">
			    		<bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.button.anularConvenio"/>
			    	</button>
			    </logic:equal>
			    <logic:notEqual name="liqConvenioCuentaAdapterVO" property="anularConvenioEnabled" value="true">
					<button type="button" name="btn5" class="botonDeshabilitado" disabled="disabled">
			    		<bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.button.anularConvenio"/>
			    	</button>
			    </logic:notEqual>			    
			</logic:equal>
			
			<!-- Aplicar Pago a Cuenta -->
			<logic:equal name="liqConvenioCuentaAdapterVO" property="aplicarPagoACuentaVisible" value="true">
				<logic:equal name="liqConvenioCuentaAdapterVO" property="aplicarPagoACuentaEnabled" value="true">
					<button type="button" name="btn1" class="boton" onclick="submitForm('aplicarPagoACuentaInit','<bean:write name="liqConvenioCuentaAdapterVO" property="convenio.idConvenio" bundle="base" formatKey="general.format.id"/>');">
			    		<bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.button.aplicarPagoACuenta"/>
			    	</button>
			    </logic:equal>
			    <logic:notEqual name="liqConvenioCuentaAdapterVO" property="aplicarPagoACuentaEnabled" value="true">
					<button type="button" name="btn1" class="botonDeshabilitado" disabled="disabled">
			    		<bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.button.aplicarPagoACuenta"/>
			    	</button>
			    </logic:notEqual>			    
			</logic:equal>
			<logic:equal name="liqConvenioCuentaAdapterVO" property="verificarConsistenciaVisible" value="true">
				<button type="button" name="btn20" onclick="submitForm('verificarConsistencia','<bean:write name="liqConvenioCuentaAdapterVO" property="convenio.idConvenio" bundle="base" formatKey="general.format.id"/>');">
					<bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.button.verifConsistencia"/>
				</button>
			</logic:equal>
						
			<!-- Reclamar Asentamiento -->
			<logic:equal name="liqConvenioCuentaAdapterVO" property="reclamarAsentamientoVisible" value="true">
		    	<button type="button" name="btnRecAcent" class="boton" onclick="submitForm('reclamarAcent', '');">
		  			<bean:message bundle="gde" key="gde.liqDeudaAdapter.button.reclamarAcentamiento"/>
				</button>
			</logic:equal>
			
			</td>
			
		</p>
	</fieldset>
	<!-- Fin Acciones -->

	
	<!-- Se arma un boton volver segun sea una inclucion o la liquidacion de la deuda -->
	<logic:equal name="userSession" property="navModel.act" value="includeVerDetalleConvenio">
		<button type="button" name="btnVolver" class="boton" onclick="submitForm('volver', '<bean:write name="liqConvenioCuentaAdapterVO" property="convenio.idConvenio" bundle="base" formatKey="general.format.id"/>');">
			<bean:message bundle="base" key="abm.button.volver"/>
		</button>
	</logic:equal>
	
	<logic:notEqual name="userSession" property="navModel.act" value="includeVerDetalleConvenio">
		<button type="button" name="btnVolver" class="boton"  onclick="submitForm('volver', '<bean:write name="liqConvenioCuentaAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>');">
			<bean:message bundle="base" key="abm.button.volver"/>
		</button>
   	</logic:notEqual>
	

	
	<html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
		<bean:message bundle="base" key="abm.button.imprimir"/>
	</html:button>
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>
	
	<input type="hidden" name="name"         value="<bean:write name='liqConvenioCuentaAdapterVO' property='name'/>" id="name"/>
	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
	
	<input type="hidden" name="validAuto" value="false"/>
	
</html:form>
<!-- Fin formulario -->