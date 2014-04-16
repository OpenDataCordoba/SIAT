<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/AdministrarEnvioOsiris.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	
	<h1><bean:message bundle="bal" key="bal.envioOsirisAdapter.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>	
			<td align="right">
	 			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>

	<logic:notEqual name="envioOsirisAdapterVO" property="paramForzar" value="true">
	<!-- Datos -->
		<fieldset>
		<legend><bean:message bundle="bal" key="bal.envioOsiris.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="bal" key="bal.envioOsiris.idEnvioAfip.label"/>: </label></td>
					<td class="normal">
						<bean:write name="envioOsirisAdapterVO" property="envioOsiris.idEnvioAfipView" />
					</td>
					<td><label><bean:message bundle="bal" key="bal.envioOsiris.idOrgRecAfip.label"/>: </label></td>
					<td class="normal">
						<bean:write name="envioOsirisAdapterVO" property="envioOsiris.idOrgRecAfipView"/>
					</td>				
				</tr>
				<tr>
					<td><label><bean:message bundle="bal" key="bal.envioOsiris.fechaProceso.label"/>: </label></td>
					<td class="normal">
						<bean:write name="envioOsirisAdapterVO" property="envioOsiris.fechaProcesoView"/>
					</td>
					<td><label><bean:message bundle="bal" key="bal.envioOsiris.fechaRecepcion.label"/>: </label></td>
					<td class="normal">
						<bean:write name="envioOsirisAdapterVO" property="envioOsiris.fechaRecepcionView"/>
					</td>
				</tr>
				<tr>	
					<td><label><bean:message bundle="bal" key="bal.envioOsiris.canTranPagoSIAT.label"/>: </label></td>
					<td class="normal">
						<bean:write name="envioOsirisAdapterVO" property="envioOsiris.cantidadPagosView"/>
					</td>	
					<td><label><bean:message bundle="bal" key="bal.envioOsiris.importePagos.label"/>: </label></td>
					<td class="normal">
						<bean:write name="envioOsirisAdapterVO" property="envioOsiris.importePagosView"/>
					</td>				
				</tr>
				<tr>
					<td><label><bean:message bundle="bal" key="bal.envioOsiris.cantidadPagos.label"/>: </label></td>
					<td class="normal">
						<bean:write name="envioOsirisAdapterVO" property="envioOsiris.canTranPagoView"/>
					</td>
					<td><label><bean:message bundle="bal" key="bal.envioOsiris.fechaRegistroMulat.label"/>: </label></td>
					<td class="normal">
						<bean:write name="envioOsirisAdapterVO" property="envioOsiris.fechaRegistroMulatView"/>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="bal" key="bal.envioOsiris.canDecJur.label"/>: </label></td>
					<td class="normal">
						<bean:write name="envioOsirisAdapterVO" property="envioOsiris.canTranDecJurView"/>
					</td>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal">
						<bean:write name="envioOsirisAdapterVO" property="envioOsiris.estadoEnvio.desEstado"/>
					</td>
				</tr>
			</table>
		</fieldset>		
		<!-- Log de Observaciones -->
		<fieldset>
		<legend><bean:message bundle="bal" key="bal.envioOsiris.observacion.label"/></legend>
			<table class="tabladatos">
				<tr>
					<!-- Observacion-->		
					<td colspan="3" class="normal"><html:textarea style="height:150px;width:650px" name="envioOsirisAdapterVO" property="envioOsiris.observacion" cols="75" rows="15" readonly="true"/></td>					
				</tr>	
			</table>
		</fieldset>
	</logic:notEqual>
	
	<logic:equal name="envioOsirisAdapterVO" property="paramForzar" value="true">
		<!-- Proceso -->
		<fieldset>
			<legend><bean:message bundle="pro" key="pro.proceso.title"/></legend>
			<table class="tabladatos">	
				<tr>
					<!-- Descripcion -->
					<td><label><bean:message bundle="pro" key="pro.proceso.desProceso.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="envioOsirisAdapterVO" property="proceso.desProceso"/></td>
				</tr>
				<tr>
					<!-- TipoEjecucion -->
					<td><label><bean:message bundle="pro" key="pro.proceso.tipoEjecucion.label"/>: </label></td>
					<td class="normal">
						<bean:write name="envioOsirisAdapterVO" property="proceso.tipoEjecucion.desTipoEjecucion"/>
					</td>
					<logic:equal name="envioOsirisAdapterVO" property="paramPeriodic" value="true">
						<!-- CronExpression -->
						<td><label>&nbsp;<bean:message bundle="pro" key="pro.proceso.cronExpression.label"/>: </label></td>
						<td class="normal">
							<bean:write name="envioOsirisAdapterVO" property="proceso.cronExpression"/>
						</td>		
					</logic:equal>
				</tr>

			</table>
		</fieldset>	
		<!-- Proceso -->
	</logic:equal>	
	
	<logic:equal name="envioOsirisAdapterVO" property="act" value="ver">
	<!-- CierreBanco -->		
	<div id="resultadoFiltro" class="scrolable" style="height: 300px;">	 
	<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.envioOsiris.listCierreBanco.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="envioOsirisAdapterVO" property="envioOsiris.listCierreBanco">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->		
						<th align="left"><bean:message bundle="bal" key="bal.cierreBanco.nroCierreBanco.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.cierreBanco.banco.ref"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.cierreBanco.fechaCierre.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.cierreBanco.cantTransaccion.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.cierreBanco.importeTotal.label"/></th>					
					</tr>
					<logic:iterate id="CierreBancoVO" name="envioOsirisAdapterVO" property="envioOsiris.listCierreBanco">
					<!-- Ver/Seleccionar -->
						<tr>
						<td>
							<logic:notEqual name="envioOsirisAdapterVO" property="modoSeleccionar" value="true">
								<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verCierreBanco', '<bean:write name="CierreBancoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
									<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
								</a>
							</logic:notEqual>																
						</td>												
						<td><bean:write name="CierreBancoVO" property="nroCierreBancoView"/>&nbsp;</td>
						<td><bean:write name="CierreBancoVO" property="bancoView"/>&nbsp;</td>
						<td><bean:write name="CierreBancoVO" property="fechaCierreView"/>&nbsp;</td>						
						<td><bean:write name="CierreBancoVO" property="cantTransaccionView"/>&nbsp;</td>
						<td><bean:write name="CierreBancoVO" property="importeTotalView"/>&nbsp;</td>			
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="envioOsirisAdapterVO" property="envioOsiris.listCierreBanco">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
	</table>
	</div>
	<!-- CierreBanco -->
	
	<!-- Conciliacion -->		
	<div id="resultadoFiltro" class="scrolable" style="height: 300px;">	 
	<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.envioOsiris.listConciliacion.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="envioOsirisAdapterVO" property="envioOsiris.listConciliacion">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->		
						<th align="left"><bean:message bundle="bal" key="bal.conciliacion.idEnvioAfip.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.conciliacion.banco.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.conciliacion.nroCierreBanco.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.conciliacion.fechaConciliacion.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.conciliacion.cantNota.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.conciliacion.totalConciliado.label"/></th>					
					</tr>
					<logic:iterate id="ConciliacionVO" name="envioOsirisAdapterVO" property="envioOsiris.listConciliacion">
						<!-- Ver/Seleccionar -->
						<tr>
							<td>
								<logic:notEqual name="envioOsirisAdapterVO" property="modoSeleccionar" value="true">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verConciliacion', '<bean:write name="ConciliacionVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</logic:notEqual>																
							</td>				

							<td><bean:write name="ConciliacionVO" property="idEnvioAfipView"/>&nbsp;</td>
							<td><bean:write name="ConciliacionVO" property="bancoView"/>&nbsp;</td>
							<td><bean:write name="ConciliacionVO" property="nroCierreBancoView"/>&nbsp;</td>
							<td><bean:write name="ConciliacionVO" property="fechaConciliacionView"/>&nbsp;</td>						
							<td><bean:write name="ConciliacionVO" property="cantNotaView"/>&nbsp;</td>
							<td><bean:write name="ConciliacionVO" property="totalConciliadoView"/>&nbsp;</td>						
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="envioOsirisAdapterVO" property="envioOsiris.listConciliacion">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
	</table>
	</div>
	<!-- Conciliacion -->
	</logic:equal>	
	
	<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<logic:equal name="envioOsirisAdapterVO" property="act" value="obtenerEnvios">
		   	    	<td align="right" width="50%">
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('obtenerEnvios', '');">
								<bean:message bundle="bal" key="bal.envioOsirisAdapter.button.obtenerEnvios"/>
							</html:button>
			    </td>
				</logic:equal>
				<logic:equal name="envioOsirisAdapterVO" property="act" value="procesarEnvios">
		   	    	<td align="right" width="50%">
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('procesarEnvios', '');">
								<bean:message bundle="bal" key="bal.envioOsirisAdapter.button.procesarEnvios"/>
							</html:button>
			    </td>
				</logic:equal>
				<logic:equal name="envioOsirisAdapterVO" property="act" value="generarTransaccion">
		   	    	<td align="right" width="50%">
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('generarTransaccion', '');">
								<bean:message bundle="bal" key="bal.envioOsirisAdapter.button.generar"/>
							</html:button>
			    	</td>
				</logic:equal>
				<logic:equal name="envioOsirisAdapterVO" property="act" value="generarDecJur">
		   	    	<td align="right" width="50%">
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('generarDecJur', '');">
								<bean:message bundle="bal" key="bal.envioOsirisAdapter.button.generar"/>
							</html:button>
			    	</td>
				</logic:equal>
				<logic:equal name="envioOsirisAdapterVO" property="act" value="cambiarEstado">
		   	    	<td align="right" width="50%">
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('cambiarEstado', '');">
								<bean:message bundle="bal" key="bal.envioOsirisAdapter.button.cambiarEstado"/>
							</html:button>
			    	</td>
				</logic:equal>
 			</tr>
	</table>
		
	<input type="hidden" name="method" value=""/>
    <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
    <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
