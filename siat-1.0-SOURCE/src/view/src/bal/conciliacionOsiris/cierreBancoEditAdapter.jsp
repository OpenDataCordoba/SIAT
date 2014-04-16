<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/AdministrarCierreBanco.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.cierreBancoEditAdapter.title"/></h1>	

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
					<td><label><bean:message bundle="bal" key="bal.cierreBanco.banco.ref"/>: </label></td>
					<td class="normal"><bean:write name="cierreBancoAdapterVO" property="cierreBanco.bancoView"/></td>						

					<td><label><bean:message bundle="bal" key="bal.cierreBanco.nroCierreBanco.label"/>: </label></td>
					<td class="normal"><bean:write name="cierreBancoAdapterVO" property="cierreBanco.nroCierreBancoView" /></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="bal" key="bal.cierreBanco.fechaCierre.label"/>: </label></td>
					<td class="normal">
						<bean:write name="cierreBancoAdapterVO" property="cierreBanco.fechaCierreView" />
					</td>				

					<td><label><bean:message bundle="bal" key="bal.envioOsiris.idEnvioAfip.label"/>: </label></td>
					<td class="normal">
						<bean:write name="cierreBancoAdapterVO" property="cierreBanco.envioOsiris.idEnvioAfipView" />
					</td>				
				</tr>
				<tr>
					<td><label><bean:message bundle="bal" key="bal.cierreBanco.cantTransaccion.label"/>: </label></td>
					<td class="normal">
						<bean:write name="cierreBancoAdapterVO" property="cierreBanco.cantTransaccionView" />
					</td>
					<td><label><bean:message bundle="bal" key="bal.cierreBanco.cantTransaccionCal.label"/>: </label></td>
					<td class="normal">
						<bean:write name="cierreBancoAdapterVO" property="cierreBanco.cantTransaccionCalView" />
					</td>			
				</tr>
			</table>
		</fieldset>		
		<!-- CierreBanco -->
		
		<!-- Conciliación Movimientos Bancarios -->		
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.cierreBanco.conMovBan.title"/></legend>
			<table class="tabladatos">			
				<tr>
					<td><label><bean:message bundle="bal" key="bal.cierreBanco.totalMovBan.lable"/>: </label></td>
					<td class="normal">
						<bean:write name="cierreBancoAdapterVO" property="totalMovBanView"/>
					</td>				
				</tr>
				<tr>
					<td><label><bean:message bundle="bal" key="bal.cierreBanco.totalCalSobreNotaAbonoN1N2.label"/>: </label></td>
					<td class="normal">
						<bean:write name="cierreBancoAdapterVO" property="totalNotaImptoN1N2View"/>
					</td>				
				</tr>
				<tr>
					<td><label><bean:message bundle="bal" key="bal.cierreBanco.totalCalSobreNotaAbonoN1N2N3.label"/>: </label></td>
					<td class="normal">
						<bean:write name="cierreBancoAdapterVO" property="totalNotaImptoN1N2N3View"/>
					</td>				
				</tr>
				<tr>
					<td><label><bean:message bundle="bal" key="bal.cierreBanco.totalCalSobreNotaOblig.label"/>: </label></td>
					<td class="normal">
						<bean:write name="cierreBancoAdapterVO" property="totalCalSobreNotaObligView"/>
					</td>				
				</tr>
			</table>
		</fieldset>		
		<!-- Conciliación Movimientos Bancarios -->
		
		<!-- Conciliación Rendición de Transacciones -->		
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.cierreBanco.conRenTran.title"/></legend>
			<table class="tabladatos">			
				<tr>
					<td><label><bean:message bundle="bal" key="bal.cierreBanco.totalImporteDetallePago.label"/>: </label></td>
					<td class="normal">
						<bean:write name="cierreBancoAdapterVO" property="totalImporteDetallePagoView"/>
					</td>				
				</tr>
				<tr>
					<td><label><bean:message bundle="bal" key="bal.cierreBanco.totalRendido.label"/>: </label></td>
					<td class="normal">
						<bean:write name="cierreBancoAdapterVO" property="totalRendidoView"/>
					</td>				
				</tr>
				<tr>
					<td><label><bean:message bundle="bal" key="bal.cierreBanco.importeTotalTraIncEli.label"/>: </label></td>
					<td class="normal">
						<bean:write name="cierreBancoAdapterVO" property="importeTotalTraIncEliView"/>
					</td>				
				</tr>
			</table>
		</fieldset>		
		<!-- Conciliación Rendición de Transacciones -->
		
		<!-- Datos Conciliacion: Detales de Movimientos Bancarios Totalizados por Impuesto -->		
		<div id="resultadoFiltro" class="scrolable">	
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="bal" key="bal.cierreBanco.datosMovBanTotalizados.label"/></caption>
		    	<tbody>
					<logic:notEmpty  name="cierreBancoAdapterVO" property="listTotMovBanDet">	    	
				    	<tr>
							<th align="left"><bean:message bundle="bal" key="bal.movBan.fechaAcredit.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.movBanDet.impuesto.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.movBanDet.nroCuenta.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.movBanDet.debito.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.movBanDet.credito.label"/></th>
						</tr>
						<logic:iterate id="NovedadEnvioVO" name="cierreBancoAdapterVO" property="listTotMovBanDet">
							<tr>
								<td><bean:write name="NovedadEnvioVO" property="fechaAcreditView"/>&nbsp;</td>
								<td><bean:write name="NovedadEnvioVO" property="impuestoView"/>&nbsp;</td>
								<td><bean:write name="NovedadEnvioVO" property="nroCuenta"/>&nbsp;</td>	
								<td><bean:write name="NovedadEnvioVO" property="debitoView"/>&nbsp;</td>	
								<td><bean:write name="NovedadEnvioVO" property="creditoView"/>&nbsp;</td>	
							</tr>
						</logic:iterate>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td align="right"><bean:message bundle="bal" key="bal.cierreBanco.totalesMovBan.label"/>:&nbsp;</td>	
							<td><bean:write name="cierreBancoAdapterVO" property="totalDebitoView"/>&nbsp;</td>	
							<td><bean:write name="cierreBancoAdapterVO" property="totalCreditoView"/>&nbsp;</td>	
						</tr>
					</logic:notEmpty>
					<logic:empty  name="cierreBancoAdapterVO" property="listTotMovBanDet">
						<tr><td align="center">
						<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
					</logic:empty>
				</tbody>
		</table>
		</div>
		<!-- Datos Conciliacion: Detales de Movimientos Bancarios Totalizados por Impuesto -->
		
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
								<td align="right"><bean:write name="NotaImptoVO" property="importeView"/>&nbsp;</td>					
								<td align="right"><bean:write name="NotaImptoVO" property="importeIVAView"/>&nbsp;</td>																	
							</tr>
						</logic:iterate>
							<tr>
								<td colspan="4" align="right"><bean:message bundle="bal" key="bal.cierreBanco.totalNotaImpto.label"/>:&nbsp;</td>
								<td align="right"><bean:write name="cierreBancoAdapterVO" property="totalNotaImptoView"/>&nbsp;</td>
								<td align="right"><bean:write name="cierreBancoAdapterVO" property="totalIVANotaImptoView"/>&nbsp;</td>
							</tr>
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
		
		<!-- EnvNotObl -->		
		<div id="resultadoFiltro" class="scrolable" style="height: 300px;">	
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="bal" key="bal.cierreBanco.listEnvNotObl.label"/></caption>
	    		<tbody>
					<logic:notEmpty  name="cierreBancoAdapterVO" property="cierreBanco.envioOsiris.listEnvNotObl">	    	
			    		<tr>
							<th align="left"><bean:message bundle="bal" key="bal.envNotObl.fechaRegistro.label"/></th>												
							<th align="left"><bean:message bundle="bal" key="bal.envNotObl.banco.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.envNotObl.nroCierreBanco.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.envNotObl.bancoOriginal.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.envNotObl.nroCieBanOrig.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.envNotObl.totalCredito.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.envNotObl.totalAcreditado.label"/></th>
						</tr>
						<logic:iterate id="EnvNotOblVO" name="cierreBancoAdapterVO" property="cierreBanco.envioOsiris.listEnvNotObl">
							<tr>
								<td><bean:write name="EnvNotOblVO" property="fechaRegistroView"/>&nbsp;</td>
								<td><bean:write name="EnvNotOblVO" property="bancoView"/>&nbsp;</td>	
								<td><bean:write name="EnvNotOblVO" property="nroCierreBancoView"/>&nbsp;</td>
								<td><bean:write name="EnvNotOblVO" property="bancoOriginalView"/>&nbsp;</td>	
								<td><bean:write name="EnvNotOblVO" property="nroCieBanOrigView"/>&nbsp;</td>
								<td align="right"><bean:write name="EnvNotOblVO" property="totalCreditoView"/>&nbsp;</td>			
								<td align="right"><bean:write name="EnvNotOblVO" property="totalAcreditadoView"/>&nbsp;</td>
																									
							</tr>
						</logic:iterate>
					</logic:notEmpty>
					<logic:empty  name="cierreBancoAdapterVO" property="cierreBanco.envioOsiris.listEnvNotObl">
						<tr><td align="center">
						<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
					</logic:empty>
				</tbody>
		</table>
		</div>
		<!-- EnvNotObl -->

		<table class="tablabotones"  width="100%">
		<tr>				
			<td align="left">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<td align="right">
				<logic:equal name="cierreBancoAdapterVO" property="act" value="conciliar">
		   	    	<html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
					    <bean:message bundle="base" key="abm.button.imprimir"/>
					</html:button>
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('conciliar', '');">
						<bean:message bundle="base" key="abm.button.aprobar"/>
					</html:button>
				</logic:equal>				
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