<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/cyq/AdministrarProcedimiento.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="cyq" key="cyq.procedimientoViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- Procedimiento -->
			<bean:define id="procedimientoVO" name="procedimientoAdapterVO" property="procedimiento"/>
			<%@ include file="/cyq/concursoyQuiebra/includeEncProcedimiento.jsp"%>
		<!-- Procedimiento -->

		<logic:equal name="procedimientoAdapterVO" property="act" value="ver">
			<table class="tablabotones" width="100%">
				<tr>
					<td align="right"> 
						<input type="button" class="boton"  onClick="submitForm('imprimirCaratula', 
							'<bean:write name="procedimientoAdapterVO" property="procedimiento.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.imprimir"/>"/>		
					</td>
				</tr>
			</table>
		</logic:equal>	
		
		<br>

		<fieldset>
			<legend> 
				<a onclick="toggle(this, 'bloqueBaja')" style="cursor: pointer;"> (-) &nbsp; </a> 
				<bean:message bundle="cyq" key="cyq.procedimientoAdapter.bajaConversion.title"/>
			</legend>

			<span id="bloqueBaja" style="display:block">
			
				<table class="tablabotones">

					<logic:equal name="procedimientoAdapterVO" property="procedimiento.poseeFechaBaja" value="true">
						<!-- Motivo -->
						<tr>	
							<td><label><bean:message bundle="cyq" key="cyq.motivoBaja.label"/>: </label></td>
							<td class="normal" colspan="3">
								<bean:write  name="procedimientoAdapterVO" property="procedimiento.motivoBaja.desMotivoBaja"/>
							</td>					
						</tr>
						<!-- FechaBaja -->
						<tr>
							<td><label><bean:message bundle="cyq" key="cyq.procedimiento.fechaBaja.label"/>: </label></td>
							<td class="normal">
								<bean:write name="procedimientoAdapterVO" property="procedimiento.fechaBajaView"/>
							</td>
						</tr>
						<!-- nroSentenciaBaja -->
						<tr>
							<td><label><bean:message bundle="cyq" key="cyq.procedimiento.nroSentenciaBaja.label"/>: </label></td>
							<td class="normal" colspan="3"><bean:write name="procedimientoAdapterVO" property="procedimiento.nroSentenciaBaja"/></td>			
						</tr>
						<!-- observacionBaja -->
						<tr>
							<td><label><bean:message bundle="cyq" key="cyq.procedimiento.observacionBaja.label"/>: </label></td>
							<td class="normal" colspan="3">
								<bean:write name="procedimientoAdapterVO" property="procedimiento.observacionBaja"/>
							</td>
						</tr>
					
					</logic:equal>
					
				</table>
			
			</span>
			
		</fieldset>
		
		<br>
		
		<!-- Procedimiento Anterior -->
		<logic:equal name="procedimientoAdapterVO" property="procedimiento.poseeProcedAnt" value="true">
			<fieldset>
				<legend> 
					<a onclick="toggle(this, 'bloqueProcedAnt')" style="cursor: pointer;"> (-) &nbsp; </a> 
					<bean:message bundle="cyq" key="cyq.procedimiento.procedAnt.label"/>
				</legend>

				<span id="bloqueProcedAnt" style="display:block">
							
				<table class="tabladatos" border="0">
					<tr>
						<td>
							<label>
								<bean:message bundle="cyq" key="cyq.procedimiento.numero.label"/> / 
								<bean:message bundle="cyq" key="cyq.procedimiento.anio.label"/>:
							</label>
						</td>
						<td class="normal" colspan="3">
							<bean:write name="procedimientoAdapterVO" property="procedimiento.procedAnt.numeroView"/> / 
							<bean:write name="procedimientoAdapterVO" property="procedimiento.procedAnt.anioView"/>
						</td>			
					</tr>
				
					<tr>
						<!-- fecha Alta -->
						<td><label><bean:message bundle="cyq" key="cyq.procedimiento.fechaAlta.label"/>: </label></td>
						<td class="normal"><bean:write name="procedimientoAdapterVO" property="procedimiento.procedAnt.fechaAltaView"/></td>
						<!-- fecha boletin -->
						<td><label><bean:message bundle="cyq" key="cyq.procedimiento.fechaBoletin.label"/>: </label></td>
						<td class="normal"><bean:write name="procedimientoAdapterVO" property="procedimiento.procedAnt.fechaBoletinView"/></td>
					</tr>
					
					<tr>
						<!-- auto-->
						<td><label><bean:message bundle="cyq" key="cyq.procedimiento.auto.label"/>: </label></td>
						<td class="normal"><bean:write name="procedimientoAdapterVO" property="procedimiento.procedAnt.auto"/></td>
						<!-- fechaAuto // es la "Fecha de Actualizacion de Deuda" mostrada en la liquidacion de la deuda.--> 
						<td><label><bean:message bundle="cyq" key="cyq.procedimiento.fechaAuto.label"/>: </label></td>
						<td class="normal"><bean:write name="procedimientoAdapterVO" property="procedimiento.procedAnt.fechaAutoView"/></td>
					</tr>
					
					<!-- Caratula -->
					<tr>
						<td><label><bean:message bundle="cyq" key="cyq.procedimiento.caratula.label"/>: </label></td>
						<td class="normal" colspan="3"><bean:write name="procedimientoAdapterVO" property="procedimiento.procedAnt.caratula"/></td>
					</tr>				
		
					<!-- TipoProceso -->
					<tr>
						<td><label><bean:message bundle="cyq" key="cyq.tipoProceso.label"/>: </label></td>
						<td class="normal" colspan="3"><bean:write name="procedimientoAdapterVO" property="procedimiento.procedAnt.tipoProceso.desTipoProceso"/></td>
					</tr>
					<tr>
						<!-- Juzgado-->
						<td><label><bean:message bundle="cyq" key="cyq.juzgado.label"/>: </label></td>
						<td class="normal"><bean:write name="procedimientoAdapterVO" property="procedimiento.procedAnt.juzgado.desJuzgado"/></td>
						<!-- Abogado-->				
						<td><label><bean:message bundle="cyq" key="cyq.abogado.label"/>: </label></td>
						<td class="normal"><bean:write name="procedimientoAdapterVO" property="procedimiento.procedAnt.abogado.descripcion"/></td>
					</tr>
				</table>
				
				</span>
				
			</fieldset>
	
		<br>
		
		</logic:equal>
		
		<fieldset>
			<legend> 
				<a onclick="toggle(this, 'bloqueInformeAbogado')" style="cursor: pointer;"> (-) &nbsp; </a> 
				<bean:message bundle="cyq" key="cyq.procedimientoAdapter.informeAbogado.title"/>				
			</legend>

			<span id="bloqueInformeAbogado" style="display:block">
			
			<table class="tabladatos">
				<tr>
					<!-- fecha Informe individual -->
					<td><label><bean:message bundle="cyq" key="cyq.procedimiento.fechaInfInd.label"/>: </label></td>
					<td class="normal"><bean:write name="procedimientoAdapterVO" property="procedimiento.fechaInfIndView"/></td>
					<!-- fecha Homologacion -->
					<td><label><bean:message bundle="cyq" key="cyq.procedimiento.fechaHomo.label"/>: </label></td>
					<td class="normal"><bean:write name="procedimientoAdapterVO" property="procedimiento.fechaHomoView"/></td>
				</tr>
				
				<tr>
					<!-- Motivo Res Inf -->
					<td><label><bean:message bundle="cyq" key="cyq.motivoResInf.label"/>: </label></td>
					<td class="normal"><bean:write name="procedimientoAdapterVO" property="procedimiento.motivoResInf.desMotivoResInf"/></td>
					<!-- Recurso Res -->
					<td><label><bean:message bundle="cyq" key="cyq.procedimiento.recursoRes.label"/>: </label></td>
					<td class="normal"><bean:write name="procedimientoAdapterVO" property="procedimiento.recursoRes.value"/></td>
				</tr>
				
				<tr>
					<!-- nuevaCaratulaRes -->
					<td><label><bean:message bundle="cyq" key="cyq.procedimiento.nuevaCaratulaRes.label"/>: </label></td>
					<td class="normal"><bean:write name="procedimientoAdapterVO" property="procedimiento.nuevaCaratulaRes"/></td>
					<!-- codExpJudRes -->
					<td><label><bean:message bundle="cyq" key="cyq.procedimiento.codExpJudRes.label"/>: </label></td>
					<td class="normal"><bean:write name="procedimientoAdapterVO" property="procedimiento.codExpJudRes"/></td>
				</tr>
				
				<tr>
					<!-- privGeneral -->
					<td><label><bean:message bundle="cyq" key="cyq.procedimiento.privGeneral.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="procedimientoAdapterVO" property="procedimiento.privGeneralView"/></td>
				</tr>

				<tr>
					<!-- privEspecial -->
					<td><label><bean:message bundle="cyq" key="cyq.procedimiento.privEspecial.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="procedimientoAdapterVO" property="procedimiento.privEspecialView"/></td>
				</tr>
				
				<tr>
					<!-- quirografario -->
					<td><label><bean:message bundle="cyq" key="cyq.procedimiento.quirografario.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="procedimientoAdapterVO" property="procedimiento.quirografarioView"/></td>
				</tr>

			</table>
			
			</span>
			
		</fieldset>
		
		<br>
		
		<fieldset>	
			<legend> 
				<a onclick="toggle(this, 'bloqueOrdenControl')" style="cursor: pointer;"> (-) &nbsp; </a> 
				<bean:message bundle="cyq" key="cyq.procedimiento.listOrdenControl.label"/>				
			</legend>

			<span id="bloqueOrdenControl" style="display:block">
			
			<!-- Historio Estados -->
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="cyq" key="cyq.procedimiento.listOrdenControl.label"/></caption>
		    	<tbody>
					<logic:notEmpty  name="procedimientoAdapterVO" property="procedimiento.listOrdenControl">	    	
				    	<tr>
							<th align="left"><bean:message bundle="ef" key="ef.ordenControl.nroOrden.label"/></th>
							<th align="left"><bean:message bundle="ef" key="ef.ordenControl.anioOrden.label"/></th>
							<th align="left"><bean:message bundle="ef" key="ef.ordenControl.fechaEmision.label"/></th>
							<th align="left"><bean:message bundle="ef" key="ef.estadoOrden.label"/></th>						
						</tr>
						<logic:iterate id="OrdenControlVO" name="procedimientoAdapterVO" property="procedimiento.listOrdenControl">
							<tr>
								<td><bean:write name="OrdenControlVO" property="numeroOrdenView"/>&nbsp;</td>
								<td><bean:write name="OrdenControlVO" property="anioOrdenView"/>&nbsp;</td>
								<td><bean:write name="OrdenControlVO" property="fechaEmisionView"/>&nbsp;</td>
								<td><bean:write name="OrdenControlVO" property="estadoOrden.desEstadoOrden"/>&nbsp;</td>								
							</tr>
						</logic:iterate>
					</logic:notEmpty>
					<logic:empty  name="procedimientoAdapterVO" property="procedimiento.listOrdenControl">
						<tr><td align="center">
						<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
					</logic:empty>
				</tbody>
			</table>
			<!-- Ordenes de Control -->
		
			</span>
		
		</fieldset>
		
		<br>
		
		<fieldset>	
			<legend> 
				<a onclick="toggle(this, 'bloqueHistoricoEstados')" style="cursor: pointer;"> (-) &nbsp; </a> 
				<bean:message bundle="cyq" key="cyq.procedimiento.listHisEstProced.label"/>				
			</legend>

			<span id="bloqueHistoricoEstados" style="display:block">
			
			<!-- Historio Estados -->
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="cyq" key="cyq.procedimiento.listHisEstProced.label"/></caption>
		    	<tbody>
					<logic:notEmpty  name="procedimientoAdapterVO" property="procedimiento.listHisEstProced">	    	
				    	<tr>
							<th align="left"><bean:message bundle="cyq" key="cyq.hisEstProced.fecha.label"/></th>
							<th align="left"><bean:message bundle="base" key="base.estado.label"/></th>
							<th align="left"><bean:message bundle="cyq" key="cyq.hisEstProced.observaciones.label"/></th>
							<th align="left"><bean:message bundle="cyq" key="cyq.hisEstProced.logCambios.label"/></th>						
						</tr>
						<logic:iterate id="HisEstProcedVO" name="procedimientoAdapterVO" property="procedimiento.listHisEstProced">
							<tr>
								<td><bean:write name="HisEstProcedVO" property="fechaView"/>&nbsp;</td>
								<td><bean:write name="HisEstProcedVO" property="estadoProced.desEstadoProced"/>&nbsp;</td>
								<td><bean:write name="HisEstProcedVO" property="observaciones"/>&nbsp;</td>
								<td><bean:write name="HisEstProcedVO" property="logCambios"/>&nbsp;</td>
							</tr>
						</logic:iterate>
					</logic:notEmpty>
					<logic:empty  name="procedimientoAdapterVO" property="procedimiento.listHisEstProced">
						<tr><td align="center">
						<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
					</logic:empty>
				</tbody>
			</table>
			<!-- Historio Estados -->
		
			</span>
		
		</fieldset>
		
		<br>
		
		<fieldset>	
			<legend> 
				<a onclick="toggle(this, 'bloqueListCuentas')" style="cursor: pointer;"> (-) &nbsp; </a> 
				<bean:message bundle="cyq" key="cyq.procedimiento.listCuenta.label"/>				
			</legend>

			<span id="bloqueListCuentas" style="display:block">
		
			<!-- Cuentas -->		
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="cyq" key="cyq.procedimiento.listCuenta.label"/></caption>
		    	<tbody>
					<logic:notEmpty  name="procedimientoAdapterVO" property="procedimiento.listCuenta">	    	
				    	<tr>
								<th width="1">&nbsp;</th> <!-- Liquidacion deuda -->
								<th width="1">&nbsp;</th> <!-- Estado cuenta -->
								<th align="left"><bean:message bundle="def" key="def.recurso.label"/></th>
								<th align="left"><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.ref"/></th>
							</tr>
								
							<logic:iterate id="CuentaVO" name="procedimientoAdapterVO" property="procedimiento.listCuenta">
								<tr>
									<!-- Liquidacion deuda -->
									<td>
										<logic:equal name="procedimientoAdapterVO" property="liquidacionDeudaEnabled" value="enabled">
											<logic:equal name="CuentaVO" property="liquidacionDeudaBussEnabled" value="true">
												<a style="cursor: pointer; cursor: hand;" 
													onclick="submitForm('liquidacionDeuda','<bean:write name="CuentaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="gde" key="gde.button.liquidacionDeuda"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/liquidacionDeuda0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="CuentaVO" property="liquidacionDeudaBussEnabled" value="true">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/liquidacionDeuda1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="procedimientoAdapterVO" property="liquidacionDeudaEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/liquidacionDeuda1.gif"/>
										</logic:notEqual>
									</td>
									<!-- Estado cuenta -->								
									<td>
										<logic:equal name="procedimientoAdapterVO" property="estadoCuentaEnabled" value="enabled">										
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('estadoCuenta', '<bean:write name="CuentaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="gde" key="gde.button.estadoCuenta"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/estadoCuenta1.gif"/>
												</a>										
										</logic:equal>
										<logic:notEqual name="procedimientoAdapterVO" property="estadoCuentaEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/estadoCuenta1.gif"/>
										</logic:notEqual>
									</td>
									<td><bean:write name="CuentaVO" property="recurso.desRecurso"/>&nbsp;</td>
									<td><bean:write name="CuentaVO" property="numeroCuenta" />&nbsp;</td>
								</tr>
							</logic:iterate>
					</logic:notEmpty>
					<logic:empty  name="procedimientoAdapterVO" property="procedimiento.listCuenta">
						<tr><td align="center">
						<bean:message bundle="cyq" key="cyq.procedimientoAdapter.noExistenRegitrosDeCuentas"/>
						</td></tr>
					</logic:empty>
				</tbody>
			</table>
		</span>
	</fieldset>		
		<!-- Fin Cuentas -->
		
	<br>
	
	<!-- Deuda En Gestion Administrativa, un Bloque por Cuenta -->	
	<fieldset>
		<legend> 
			<a onclick="toggle(this, 'bloqueDeudaAdmin')" style="cursor: pointer;"> (-) &nbsp; </a> 
			<bean:message bundle="cyq" key="cyq.procedimientoAdapter.blockAdmin.title"/>				
		</legend>

		<span id="bloqueDeudaAdmin" style="display:block">
						
	<table class="tramonline" border="1" cellpadding="0" cellspacing="1" width="100%">
	    <caption><bean:message bundle="gde" key="gde.liqDeudaAdapter.blockAdmin.title"/></caption>
	</table>
	
	<logic:notEmpty name="procedimientoAdapterVO" property="procedimiento.listGestionDeudaAdmin">
		<div class="scrolable">
	    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
	    	<!--caption><bean:message bundle="gde" key="gde.liqDeudaAdapter.blockAdmin.title"/></caption-->
	      	<tbody>
	      	<!-- Item LiqDeudaAdminVO -->
	       	<logic:iterate id="GDeudaAdmin" name="procedimientoAdapterVO" property="procedimiento.listGestionDeudaAdmin">
		       	
		       	<tr>
		       		<td colspan="6">
		       			<b><bean:message bundle="def" key="def.recurso.label"/>:</b> 
		       			<bean:write name="GDeudaAdmin" property="desRecurso"/> &nbsp;&nbsp;&nbsp;&nbsp;
		       			<b><bean:message bundle="pad" key="pad.cuenta.label"/>:</b> 
		       			<bean:write name="GDeudaAdmin" property="numeroCuenta"/>
		       		</td>
		       	</tr>
		       	
		       	<tr>
		       		<th align="center">
		       			<bean:message bundle="gde" key="gde.liqDeudaAdapter.blockAdmin.seleccionar"/>		       			
		       		</th>		       		
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.periodoDeuda"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.fechaVto"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.saldo"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.actualizacion"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.total"/></th>

				</tr>
				
				<!-- Item LiqDeudaVO -->
				<logic:iterate id="LiqDeudaAdmin" name="GDeudaAdmin" property="listDeuda">
					<tr>
						<!-- Seleccion de deuda para su operacion -->
			  			<td align="center">
			  				<logic:equal name="LiqDeudaAdmin" property="esSeleccionable" value="true">
			  					<html:multibox name="GDeudaAdmin" property="listIdDeudaSelected" >
                                	<bean:write name="LiqDeudaAdmin" property="idSelect"/>
                                </html:multibox>
			  				</logic:equal>
			  				<logic:notEqual name="LiqDeudaAdmin" property="esSeleccionable" value="true">
			  					<input type="checkbox" disabled="disabled"/>
			  				</logic:notEqual>
			  			</td>
			  						  			
			  			<td><bean:write name="LiqDeudaAdmin" property="periodoDeuda"/>&nbsp;</td>
				        <td><bean:write name="LiqDeudaAdmin" property="fechaVto"/>&nbsp;</td>
				        <td><bean:write name="LiqDeudaAdmin" property="saldo" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
				        <td><bean:write name="LiqDeudaAdmin" property="actualizacion" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
				        <td><bean:write name="LiqDeudaAdmin" property="total" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
			       	</tr>
				</logic:iterate>
				<!-- Fin Item LiqDeudaVO -->
		       	<tr>       		
		       		<td colspan="7" class="celdatotales" align="right">
			        	<bean:message bundle="gde" key="gde.liqDeudaAdapter.blockAdmin.subtotal.label"/>: 
			        	<b><bean:write name="GDeudaAdmin" property="subTotal" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
		       	</tr>	       	
	       	</logic:iterate>
	       	<!-- Fin LiqDeudaAdminVO -->	       	
	      	</tbody>
		</table>
		</div>
	</logic:notEmpty>
	 
	<br>
	
	<logic:equal name="procedimientoAdapterVO" property="act" value="ver">    	
		<table class="tablabotones" border="0" cellpadding="0" cellspacing="1" width="100%">
			<tbody>
			<tr>
	       		<td>
	       			<html:button property="btnImprimir"  styleClass="boton" onclick="submitForm('imprimirDeudaAdmin', '');">
						<bean:message bundle="base" key="abm.button.imprimir"/>
					</html:button>
	       		</td>
	       	</tr>
	      	</tbody>
		</table>
	</logic:equal>
		
	</span>
	
	</fieldset>	
	<!-- Fin Deuda En Gestion Administrativa -->		
				
	<br>
	
		
	<!-- Deuda en Gestion Judicial -->
	<fieldset>
		
		<legend> 
			<a onclick="toggle(this, 'bloqueDeudaJudicial')" style="cursor: pointer;"> (-) &nbsp; </a> 
			<bean:message bundle="cyq" key="cyq.procedimientoAdapter.blockJudicial.title"/>				
		</legend>

		<span id="bloqueDeudaJudicial" style="display:block">
		
	<table class="tramonline" border="1" cellpadding="0" cellspacing="1" width="100%">
	    <caption><bean:message bundle="gde" key="gde.liqDeudaAdapter.blockJudicial.title"/></caption>
	</table>
	
	<logic:notEmpty name="procedimientoAdapterVO" property="procedimiento.listProcurador">
		<div class="scrolable">
	    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
	      	<tbody>
	      	<!-- LiqProcuradorVO -->
	       	<logic:iterate id="Procurador" name="procedimientoAdapterVO" property="procedimiento.listProcurador">

		       	<tr>
		       		<td colspan="8">
		       			<b><bean:message bundle="def" key="def.recurso.label"/>:</b> 
		       			<bean:write name="Procurador" property="desRecurso"/> &nbsp;&nbsp;&nbsp;&nbsp;
		       			<b><bean:message bundle="pad" key="pad.cuenta.label"/>:</b> 
		       			<bean:write name="Procurador" property="numeroCuenta"/>	
					</td>
				</tr>
		       	<tr>
		       		<td colspan="8">	       			
		       			<b><bean:message bundle="gde" key="gde.liqDeudaAdapter.blockJudicial.procurador.label"/>:</b> 
		       			<bean:write name="Procurador" property="idProcuradorView"/> - <bean:write name="Procurador" property="desProcurador"/>
		       		</td>
		       	</tr>
		       	<tr>
		       		<th align="center">
		       			<bean:message bundle="gde" key="gde.liqDeudaAdapter.blockAdmin.seleccionar"/> 
		       		</th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.periodoDeuda"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.fechaVto"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.saldo"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.actualizacion"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.total"/></th>
				</tr>
				
				<!-- Item LiqDeudaVO -->
				<logic:iterate id="LiqDeudaJud" name="Procurador" property="listDeuda">
					<tr>
						<!-- Seleccion de deuda para su operacion -->
			  			<td align="center">
			  				<logic:equal name="LiqDeudaJud" property="esSeleccionable" value="true">
                                <input type="checkbox" 
                                		name="listIdDeudaSelected" 
                                		id="procurador<bean:write name="Procurador" property="idProcurador" bundle="base" formatKey="general.format.id"/>"
                                		value="<bean:write name="LiqDeudaJud" property="idSelect"/>"/>
                                
			  				</logic:equal>
			  				<logic:notEqual name="LiqDeudaJud" property="esSeleccionable" value="true">
			  					<input type="checkbox" disabled="disabled"/>
			  				</logic:notEqual>
			  			</td>
			  			
			  			<td><bean:write name="LiqDeudaJud" property="periodoDeuda"/>&nbsp;</td>
				        <td><bean:write name="LiqDeudaJud" property="fechaVto"/>&nbsp;</td>
				        <td><bean:write name="LiqDeudaJud" property="saldo" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
				        <td><bean:write name="LiqDeudaJud" property="actualizacion" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
				        <td><bean:write name="LiqDeudaJud" property="total" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>

			       	</tr>
				</logic:iterate>
				<!-- Fin Item LiqDeudaVO -->
				<tr>
					<td colspan="3" class="celdatotales"> &nbsp;</td>       		
		       		<td class="celdatotales" align="left">
			        	<b><bean:write name="Procurador" property="subTotalImporte" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
			        <td class="celdatotales" align="left">
			        	<b><bean:write name="Procurador" property="subTotalActualizacion" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
		       		<td class="celdatotales" align="left">
			        	<b><bean:write name="Procurador" property="subTotal" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
		       	</tr>
		       	
		       	<tr>       		
		       		<td colspan="6" class="celdatotales" align="right">
			        	<bean:message bundle="gde" key="gde.liqDeudaAdapter.blockJudicial.subtotal.label"/>: 
			        	<b><bean:write name="Procurador" property="subTotal" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
		       	</tr>	       	
	       	</logic:iterate>
	       	<!-- Fin LiqProcuradorVO -->
	      	</tbody>
		</table>
		</div>
	</logic:notEmpty>
	
	<br>
	
	<logic:equal name="procedimientoAdapterVO" property="act" value="ver">    	
		<table class="tablabotones" border="0" cellpadding="0" cellspacing="1" width="100%">
			<tbody>
			<tr>
	       		<td>
	       			<html:button property="btnImprimir"  styleClass="boton" onclick="submitForm('imprimirDeudaJudicial', '');">
						<bean:message bundle="base" key="abm.button.imprimir"/>
					</html:button>       		
	       		</td>	       			
	       	</tr>
	      	</tbody>
		</table>
	</logic:equal>	
	
	</span>
	
	</fieldset>
	<!-- Fin Deuda en Gestion Judicial -->		
	
	<br>
	
	<fieldset>	
		<legend> 
			<a onclick="toggle(this, 'bloqueCueNoDeu')" style="cursor: pointer;"> (-) &nbsp; </a> 
			<bean:message bundle="cyq" key="cyq.procedimiento.listProCueNoDeu.label"/>				
		</legend>

		<span id="bloqueCueNoDeu" style="display:block">
		
		<!-- Historio Estados -->
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="cyq" key="cyq.procedimiento.listProCueNoDeu.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="procedimientoAdapterVO" property="procedimiento.listProCueNoDeu">	    	
			    	<tr>
						<th align="left"><bean:message bundle="def" key="def.recurso.label"/></th>
						<th align="left"><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.label"/></th>
						<th align="left"><bean:message bundle="cyq" key="cyq.proCueNoDeu.observacion.label"/></th>
					</tr>
					<logic:iterate id="ProCueNoDeuVO" name="procedimientoAdapterVO" property="procedimiento.listProCueNoDeu">
						<tr>
							<td><bean:write name="ProCueNoDeuVO" property="recurso.desRecurso"/>&nbsp;</td>								
							<td><bean:write name="ProCueNoDeuVO" property="cuenta.numeroCuenta"/>&nbsp;</td>
							<td><bean:write name="ProCueNoDeuVO" property="observacion"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="procedimientoAdapterVO" property="procedimiento.listProCueNoDeu">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
		</table>
		<!-- Ordenes de Control -->
	
		</span>
	
	</fieldset>
	
	<br>
		
	<table class="tablabotones" width="100%">
    	<tr>
 	    		<td align="left" width="50%">
	   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>	   	    			
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="procedimientoAdapterVO" property="act" value="eliminar">
					<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
						<bean:message bundle="base" key="abm.button.eliminar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="procedimientoAdapterVO" property="act" value="activar">
					<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
						<bean:message bundle="base" key="abm.button.activar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="procedimientoAdapterVO" property="act" value="desactivar">
					<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
						<bean:message bundle="base" key="abm.button.desactivar"/>
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
	
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
