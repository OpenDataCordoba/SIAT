<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/AdministrarConciliacion.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.conciliacionViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- Conciliacion -->		
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.conciliacion.title"/></legend>
			<table class="tabladatos">			
				<tr>
					<td><label><bean:message bundle="bal" key="bal.conciliacion.idEnvioAfip.label"/>: </label></td>
					<td class="normal">
						<bean:write name="conciliacionAdapterVO" property="conciliacion.idEnvioAfipView" />
					</td>
					<td><label><bean:message bundle="bal" key="bal.conciliacion.fechaConciliacion.label"/>: </label></td>
					<td class="normal">
						<bean:write name="conciliacionAdapterVO" property="conciliacion.fechaConciliacionView" />
					</td>				
				</tr>
				<tr>
					<td><label><bean:message bundle="bal" key="bal.conciliacion.banco.label"/>: </label></td>
					<td class="normal">
						<bean:write name="conciliacionAdapterVO" property="conciliacion.bancoView" />
					</td>
					<td><label><bean:message bundle="bal" key="bal.conciliacion.nroCierreBanco.label"/>: </label></td>
					<td class="normal">
						<bean:write name="conciliacionAdapterVO" property="conciliacion.nroCierreBancoView"/>
					</td>				
				</tr>
				<tr>
				<td><label><bean:message bundle="bal" key="bal.conciliacion.cantNota.label"/>: </label></td>
					<td class="normal">
						<bean:write name="conciliacionAdapterVO" property="conciliacion.cantNotaView"/>
					</td>						
					<td><label><bean:message bundle="bal" key="bal.conciliacion.totalConciliado.label"/>: </label></td>
					<td class="normal">
						<bean:write name="conciliacionAdapterVO" property="conciliacion.totalConciliadoView"/>
					</td>				
				</tr>
			</table>
		</fieldset>		
		<!-- Conciliacion -->
				
		<!-- NotaConc -->		
		<div id="resultadoFiltro" class="scrolable" style="height: 300px;">	
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="bal" key="bal.conciliacion.listNotaConc.label"/></caption>
		    	<tbody>
					<logic:notEmpty  name="conciliacionAdapterVO" property="conciliacion.listNotaConc">	    	
				    	<tr>
							<th align="left"><bean:message bundle="bal" key="bal.notaConc.fechaConciliacion.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.notaConc.nroCuenta.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.notaConc.impuesto.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.notaConc.moneda.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.notaConc.tipoNota.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.notaConc.importe.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.notaConc.fechaAcredit.label"/></th>												
						</tr>
						<logic:iterate id="NotaConcVO" name="conciliacionAdapterVO" property="conciliacion.listNotaConc">
							<tr>
								<td><bean:write name="NotaConcVO" property="fechaConciliacionView"/>&nbsp;</td>																	
								<td><bean:write name="NotaConcVO" property="nroCuenta"/>&nbsp;</td>	
								<td><bean:write name="NotaConcVO" property="impuestoView"/>&nbsp;</td>	
								<td><bean:write name="NotaConcVO" property="monedaView"/>&nbsp;</td>	
								<td><bean:write name="NotaConcVO" property="tipoNotaView"/>&nbsp;</td>
								<td><bean:write name="NotaConcVO" property="importeView"/>&nbsp;</td>					
								<td><bean:write name="NotaConcVO" property="fechaAcreditView"/>&nbsp;</td>																	
							</tr>
						</logic:iterate>
					</logic:notEmpty>
					<logic:empty  name="conciliacionAdapterVO" property="conciliacion.listNotaConc">
						<tr><td align="center">
						<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
					</logic:empty>
				</tbody>
		</table>
		</div>
		<!-- NotaConc -->

		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>	   	    	
	   	    </tr>
	   	 </table>
	    <input type="hidden" name="name"  value="<bean:write name='conciliacionAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->