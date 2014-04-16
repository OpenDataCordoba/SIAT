<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/AdministrarCierreBanco.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.cierreBancoViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- CierreBanco -->		
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.cierreBanco.title"/></legend>
			<table class="tabladatos">			
				<tr>
					<td><label><bean:message bundle="bal" key="bal.cierreBanco.nroCierreBanco.label"/>: </label></td>
					<td class="normal">
						<bean:write name="cierreBancoAdapterVO" property="cierreBanco.nroCierreBancoView" />
					</td>
					<td><label><bean:message bundle="bal" key="bal.cierreBanco.fechaCierre.label"/>: </label></td>
					<td class="normal">
						<bean:write name="cierreBancoAdapterVO" property="cierreBanco.fechaCierreView" />
					</td>				
				</tr>
				<tr>
					<td><label><bean:message bundle="bal" key="bal.cierreBanco.cantTransaccion.label"/>: </label></td>
					<td class="normal">
						<bean:write name="cierreBancoAdapterVO" property="cierreBanco.cantTransaccionView" />
					</td>
					<td><label><bean:message bundle="bal" key="bal.cierreBanco.importeTotal.label"/>: </label></td>
					<td class="normal">
						<bean:write name="cierreBancoAdapterVO" property="cierreBanco.importeTotalView"/>
					</td>				
				</tr>
				<tr>
				<td><label><bean:message bundle="bal" key="bal.cierreBanco.banco.ref"/>: </label></td>
					<td class="normal">
						<bean:write name="cierreBancoAdapterVO" property="cierreBanco.bancoView"/>
					</td>						
				</tr>
			</table>
		</fieldset>		
		<!-- CierreBanco -->
		
	<!-- TranAfip -->		
	<div id="resultadoFiltro" class="scrolable" style="height: 300px;">	
	<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.cierreBanco.listTranAfip.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="cierreBancoAdapterVO" property="cierreBanco.listTranAfip">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
						<th width="1">&nbsp;</th> <!-- Generar DecJur -->	
						<th width="1">&nbsp;</th> <!-- Eliminar -->		
						<th align="left"><bean:message bundle="bal" key="bal.tranAfip.idTransaccionAfip.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.tranAfip.formulario.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.tipoOperacion.title"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.tranAfip.fechaProceso.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.tranAfip.cuit.ref"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.tranAfip.totMontoIngresado.label"/></th>												
					</tr>
					<logic:iterate id="TranAfipVO" name="cierreBancoAdapterVO" property="cierreBanco.listTranAfip">
					<!-- Ver/Seleccionar -->
						<tr>
						<td>
							<logic:notEqual name="cierreBancoAdapterVO" property="modoSeleccionar" value="true">
								<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verTransaccioAfip', '<bean:write name="TranAfipVO" property="id" bundle="base" formatKey="general.format.id"/>');">
									<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
								</a>
							</logic:notEqual>																
						</td>				
						<!-- GenerarTransaccion -->
						<td>
							<logic:equal name="cierreBancoAdapterVO" property="generarDecJurEnabled" value="enabled">		
								<logic:equal name="TranAfipVO" property="generarDecJurEnabled" value="enabled">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('generarDecJur', '<bean:write name="TranAfipVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="bal" key="bal.cierreBancoAdapter.button.generarDecJur"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/aplicar0.gif"/>
									</a>
								</logic:equal>	
								<logic:notEqual name="TranAfipVO" property="generarDecJurEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/aplicar1.gif"/>
								</logic:notEqual>									
							</logic:equal>							
							<logic:notEqual name="cierreBancoAdapterVO" property="generarDecJurEnabled" value="enabled">		
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/aplicar1.gif"/>
							</logic:notEqual>
						</td>	
						<!-- EliminarTransaccion -->
						<td>
							<logic:equal name="cierreBancoAdapterVO" property="eliminarTranAfipEnabled" value="enabled">
								<logic:equal name="TranAfipVO" property="eliminarTranAfipEnabled" value="enabled">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarTranAfip', '<bean:write name="TranAfipVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
									</a>
								</logic:equal>	
								<logic:notEqual name="TranAfipVO" property="eliminarTranAfipEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>									
							</logic:equal>							
							<logic:notEqual name="cierreBancoAdapterVO" property="eliminarTranAfipEnabled" value="enabled">		
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
							</logic:notEqual>
						</td>										
							<td><bean:write name="TranAfipVO" property="idTransaccionAfipView"/>&nbsp;</td>
							<td><bean:write name="TranAfipVO" property="formularioView"/>&nbsp;</td>	
							<td><bean:write name="TranAfipVO" property="tipoOperacion.desTipoOperacion"/>&nbsp;</td>	
							<td><bean:write name="TranAfipVO" property="fechaProcesoView"/>&nbsp;</td>	
							<td><bean:write name="TranAfipVO" property="cuit"/>&nbsp;</td>					
							<td><bean:write name="TranAfipVO" property="totMontoIngresadoView"/>&nbsp;</td>																	
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="cierreBancoAdapterVO" property="cierreBanco.listTranAfip">
					<tr>
						<td align="center">
							<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td>
					</tr>
				</logic:empty>
			</tbody>
	</table>
	</div>
	<!-- TranAfip -->

	<!-- CierreSucursal -->		
	<div id="resultadoFiltro" class="scrolable" style="height: 300px;">	
	<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.cierreBanco.listCierreSucursal.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="cierreBancoAdapterVO" property="cierreBanco.listCierreSucursal">	    	
			    	<tr>
						<th align="left"><bean:message bundle="bal" key="bal.cierreSucursal.nroCieSuc.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.cierreSucursal.sucursal.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.cierreSucursal.tipoSucursal.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.cierreSucursal.fechaCierre.label"/></th>												
						<th align="left"><bean:message bundle="bal" key="bal.cierreSucursal.fechaRegistro.label"/></th>												
					</tr>
					<logic:iterate id="CierreSucursalVO" name="cierreBancoAdapterVO" property="cierreBanco.listCierreSucursal">
						<tr>
							<td><bean:write name="CierreSucursalVO" property="nroCieSucView"/>&nbsp;</td>
							<td><bean:write name="CierreSucursalVO" property="sucursalView"/>&nbsp;</td>	
							<td><bean:write name="CierreSucursalVO" property="tipoSucursalView"/>&nbsp;</td>	
							<td><bean:write name="CierreSucursalVO" property="fechaCierreView"/>&nbsp;</td>	
							<td><bean:write name="CierreSucursalVO" property="fechaRegistroView"/>&nbsp;</td>					
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="cierreBancoAdapterVO" property="cierreBanco.listCierreSucursal">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
	</table>
	</div>
	<!-- CierreSucursal -->
	
	<!-- NotaImpto -->		
	<div id="resultadoFiltro" class="scrolable" style="height: 300px;">	
	<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.cierreBanco.listNotaImpto.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="cierreBancoAdapterVO" property="cierreBanco.listNotaImpto">	    	
			    	<tr>
						<th align="left"><bean:message bundle="bal" key="bal.notaImpto.tipoNota.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.notaImpto.impuesto.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.notaImpto.moneda.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.notaImpto.nroCuenta.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.notaImpto.importe.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.notaImpto.importeIVA.label"/></th>												
					</tr>
					<logic:iterate id="NotaImptoVO" name="cierreBancoAdapterVO" property="cierreBanco.listNotaImpto">
						<tr>
							<td><bean:write name="NotaImptoVO" property="tipoNotaView"/>&nbsp;</td>
							<td><bean:write name="NotaImptoVO" property="impuestoView"/>&nbsp;</td>	
							<td><bean:write name="NotaImptoVO" property="monedaView"/>&nbsp;</td>	
							<td><bean:write name="NotaImptoVO" property="nroCuenta"/>&nbsp;</td>	
							<td><bean:write name="NotaImptoVO" property="importeView"/>&nbsp;</td>					
							<td><bean:write name="NotaImptoVO" property="importeIVAView"/>&nbsp;</td>																	
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="cierreBancoAdapterVO" property="cierreBanco.listNotaImpto">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
	</table>
	</div>
	<!-- NotaImpto -->

	<!-- NovedadEnvio -->		
	<div id="resultadoFiltro" class="scrolable" style="height: 300px;">	
	<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.cierreBanco.listNovedadEnvio.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="cierreBancoAdapterVO" property="cierreBanco.listNovedadEnvio">	    	
			    	<tr>
						<th align="left"><bean:message bundle="bal" key="bal.novedadEnvio.formaPago.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.novedadEnvio.sucursal.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.novedadEnvio.tipoSucursal.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.novedadEnvio.tipoOperacion.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.novedadEnvio.aceptada.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.novedadEnvio.deOficio.label"/></th>												
						<th align="left"><bean:message bundle="bal" key="bal.novedadEnvio.fechaNovedad.label"/></th>												
						<th align="left"><bean:message bundle="bal" key="bal.novedadEnvio.fechaRegistro.label"/></th>												
					</tr>
					<logic:iterate id="NovedadEnvioVO" name="cierreBancoAdapterVO" property="cierreBanco.listNovedadEnvio">
						<tr>
							<td><bean:write name="NovedadEnvioVO" property="formaPagoView"/>&nbsp;</td>
							<td><bean:write name="NovedadEnvioVO" property="sucursalView"/>&nbsp;</td>	
							<td><bean:write name="NovedadEnvioVO" property="tipoSucursalView"/>&nbsp;</td>	
							<td><bean:write name="NovedadEnvioVO" property="tipoOperacionView"/>&nbsp;</td>	
							<td><bean:write name="NovedadEnvioVO" property="aceptada.value"/>&nbsp;</td>					
							<td><bean:write name="NovedadEnvioVO" property="deOficio.value"/>&nbsp;</td>																	
							<td><bean:write name="NovedadEnvioVO" property="fechaNovedadView"/>&nbsp;</td>	
							<td><bean:write name="NovedadEnvioVO" property="fechaRegistroView"/>&nbsp;</td>	
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="cierreBancoAdapterVO" property="cierreBanco.listNovedadEnvio">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
	</table>
	</div>
	<!-- NovedadEnvio -->

		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>	   	    	
	   	    </tr>
	   	 </table>
	    <input type="hidden" name="name"  value="<bean:write name='cierreBancoAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->