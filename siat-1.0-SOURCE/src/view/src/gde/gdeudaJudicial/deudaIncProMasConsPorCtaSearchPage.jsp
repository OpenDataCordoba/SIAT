<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	    
	    <%@include file="/base/calendar.js"%>   	    
</script>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter"
	action="/gde/BuscarDeudaIncProMasConsPorCta.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp"%>
	<!-- Errors  -->
	<html:errors bundle="base" />

	<h1><bean:message bundle="gde" key="gde.deudaIncProMasConsPorCtaSearchPage.title" /></h1>

	<p><bean:message bundle="gde" key="gde.deudaIncProMasConsPorCtaSearchPage.legend" /></p>

	<!-- Filtro -->
	<fieldset>
		<legend><bean:message bundle="base" key="base.parametrosBusqueda" /></legend>

		<table class="tabladatos">
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.cuenta.numeroCuenta.ref" />: </label></td>
				<td class="normal">
					<html:text name="deudaProMasConsPorCtaSearchPageVO" property="cuenta.numeroCuenta" 
						size="10" maxlength="10" styleClass="datos"  /> 
					<html:button property="btnBuscarCuenta" styleClass="boton" onclick="submitForm('buscarCuenta', '');">
						<bean:message bundle="gde" key="gde.deudaIncProMasConsPorCtaSearchPage.button.buscarCuenta" />
					</html:button>
				</td>
			</tr>
		</table>
	
		<p align="center"><html:button property="btnLimpiar" styleClass="boton" onclick="submitForm('limpiar', '');">
			<bean:message bundle="base" key="abm.button.limpiar" />
			</html:button> &nbsp; <html:button property="btnBuscar" styleClass="boton" onclick="submitForm('buscar', '');">
				<bean:message bundle="base" key="abm.button.buscar" />
			</html:button>
		</p>
	</fieldset>
	<!-- Fin Filtro -->

	<!-- Resultado Filtro -->
	<div id="resultadoFiltro">
		<logic:equal name="deudaProMasConsPorCtaSearchPageVO" property="viewResult" value="true">
			<!--  cuenta titular -->
			<fieldset>
				<legend><bean:message bundle="gde" key="gde.deudaIncProMasConsPorCtaSearchPage.titularPrincipal.title" /></legend>
				<logic:present name="deudaProMasConsPorCtaSearchPageVO" property="cuenta.cuentaTitularPrincipal">
					<table class="tabladatos">
						<bean:define id="personaVO" name="deudaProMasConsPorCtaSearchPageVO" property="cuenta.cuentaTitularPrincipal.contribuyente.persona" />
						<!-- Persona Fisica -->
						<logic:equal  name="personaVO" property="esPersonaFisica" value="true">
							<tr>
								<td><label><bean:message bundle="pad" key="pad.persona.nombres.label"/>: </label></td>
								<td class="normal"><bean:write name="personaVO" property="nombres"/></td>
								<td><label><bean:message bundle="pad" key="pad.persona.apellido.label"/>: </label></td>
								<td class="normal"><bean:write name="personaVO" property="apellido"/></td>
							</tr>
							<tr>
								<td><label><bean:message bundle="pad" key="pad.tipoDocumento.label"/>: </label></td>
								<td class="normal"><bean:write name="personaVO" property="documento.tipoDocumento.abreviatura"/></td>
								<td><label><bean:message bundle="pad" key="pad.documento.numero.ref"/>: </label></td>
								<td class="normal"><bean:write name="personaVO" property="documento.numeroView"/></td>
							</tr>
							<tr>
								<td><label><bean:message bundle="pad" key="pad.persona.cuit.label"/>: </label></td>
								<td class="normal"><bean:write name="personaVO" property="cuit"/></td>
							</tr>
						</logic:equal>	
						<!-- Fin Persona Fisicar-->
						
						<!-- Persona Juridica-->
						<logic:notEqual  name="personaVO" property="esPersonaFisica" value="true">
							<tr>
								<td><label><bean:message bundle="pad" key="pad.persona.razonSocial.label"/>: </label></td>
								<td class="normal"><bean:write name="personaVO" property="razonSocial"/></td>
								<td><label><bean:message bundle="pad" key="pad.persona.cuit.label"/>: </label></td>				
								<td class="normal"><bean:write name="personaVO" property="cuit"/></td>
							</tr>		
						</logic:notEqual>	
						<!-- Fin Persona Juridica -->	
						<tr>
							<td><label><bean:message bundle="pad" key="pad.cuentaTitular.fechaDesde.label"/>: </label></td>
							<td class="normal"><bean:write name="deudaProMasConsPorCtaSearchPageVO" property="cuenta.cuentaTitularPrincipal.fechaDesdeView"/></td>
							<td><label><bean:message bundle="pad" key="pad.cuentaTitular.fechaHasta.label"/>: </label></td>
							<td class="normal"><bean:write name="deudaProMasConsPorCtaSearchPageVO" property="cuenta.cuentaTitularPrincipal.fechaHastaView"/></td>
						</tr>
					</table>
					</logic:present>
					<logic:notPresent name="deudaProMasConsPorCtaSearchPageVO" property="cuenta.cuentaTitularPrincipal">
						<table class="tabladatos">
							<tr><td class="center"><bean:message bundle="gde" key="gde.deudaIncProMasConsPorCtaSearchPage.cuentaTitularPrincipal.noPosee" /></td></tr>
						</table>
					</logic:notPresent>
				</fieldset>

			<!-- Fin Cuenta Titular -->
		
			<logic:notEmpty name="deudaProMasConsPorCtaSearchPageVO" property="listResult">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="gde" key="gde.deudaIncProMasConsPorCtaSearchPage.deudas.title" /></caption>
					<tbody>
						<tr>
							<!-- Ver/Seleccionar fue sacado porque falta implemtar -->
							<th align="left"><bean:message bundle="gde" key="gde.deuda.anio.label" /></th>
							<th align="left"><bean:message bundle="gde" key="gde.deuda.periodo.label" /></th>
							<th align="left"><bean:message bundle="def" key="def.recClaDeu.label" /></th>
							<th align="left"><bean:message bundle="gde" key="gde.deuda.fechaVencimiento.label" /></th>
							<th align="left"><bean:message bundle="gde" key="gde.deuda.importe.label" /></th>
							<th align="left"><bean:message bundle="gde" key="gde.deuda.saldo.label" /></th>
							<th align="left"><bean:message bundle="gde" key="gde.deuda.saldoActualizado.label" /></th>						
						</tr>
	
						<logic:iterate id="DeudaAdminVO" name="deudaProMasConsPorCtaSearchPageVO" property="listResult">
							<tr>
								<td><bean:write name="DeudaAdminVO" property="anioView" />&nbsp;</td>
								<td><bean:write name="DeudaAdminVO" property="periodoView" />&nbsp;</td>
								<td><bean:write name="DeudaAdminVO" property="recClaDeu.desClaDeu" />&nbsp;</td>
								<td><bean:write name="DeudaAdminVO" property="fechaVencimientoView" /> &nbsp;</td>
								<td><bean:write name="DeudaAdminVO" property="importe"          bundle="base" formatKey="general.format.currency" />&nbsp;</td>
								<td><bean:write name="DeudaAdminVO" property="saldo"            bundle="base" formatKey="general.format.currency" />&nbsp;</td>
								<td><bean:write name="DeudaAdminVO" property="saldoActualizado" bundle="base" formatKey="general.format.currency" />&nbsp;</td>
							</tr>
						</logic:iterate>
						<tr>
							<td colspan="4">Totales:</td>
							<td><bean:write name="deudaProMasConsPorCtaSearchPageVO" property="sumaImporte" bundle="base" formatKey="general.format.currency" />&nbsp;</td>
							<td><bean:write name="deudaProMasConsPorCtaSearchPageVO" property="sumaSaldo" bundle="base" formatKey="general.format.currency" />&nbsp;</td>
							<td><bean:write name="deudaProMasConsPorCtaSearchPageVO" property="sumaSaldoActualizado" bundle="base" formatKey="general.format.currency" />&nbsp;</td>
						</tr>
					</tbody>
				</table>
				
				<bean:define id="liqDeudaAdapterVO" name="deudaProMasConsPorCtaSearchPageVO" property="liqDeudaAdapter" />
				<logic:equal name="deudaProMasConsPorCtaSearchPageVO" property="esDeudaAdministrativa" value="true">
					<%@ include file="/gde/gdeuda/includeDeudaGestionAdmin.jsp" %>
				</logic:equal>
				<logic:notEqual name="deudaProMasConsPorCtaSearchPageVO" property="esDeudaAdministrativa" value="true">
					<!-- Deuda en Gestion Judicial -->
					<logic:notEmpty name="liqDeudaAdapterVO" property="listProcurador">
						<div class="horizscroll">
					    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					    	<caption><bean:message bundle="gde" key="gde.liqDeudaAdapter.blockJudicial.title"/></caption>
					      	<tbody>
					      	<!-- LiqProcuradorVO -->
					       	<logic:iterate id="Procurador" name="liqDeudaAdapterVO" property="listProcurador">
						       	<tr>
						       		<td colspan="8">
						       			<b><bean:message bundle="gde" key="gde.liqDeudaAdapter.blockJudicial.procurador.label"/>:</b> 
						       			<bean:write name="Procurador" property="desProcurador"/>
						       		</td>		       		
						       	</tr>
						       	<tr>
								  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.periodoDeuda"/></th>
								  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.fechaVto"/></th>
								  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.saldo"/></th>
								  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.actualizacion"/></th>
								  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.total"/></th>
								</tr>
								
								<!-- Item LiqDeudaVO -->
								<logic:iterate id="LiqDeudaJud" name="Procurador" property="listDeuda">
									<tr>
							  			<td><bean:write name="LiqDeudaJud" property="periodoDeuda"/>&nbsp;</td>
								        <td><bean:write name="LiqDeudaJud" property="fechaVto"/>&nbsp;</td>
								       	
								       	<!-- Si NO posee Observacion -->				       	
								        <logic:notEqual name="LiqDeudaJud" property="poseeObservacion" value="true">
									        <td><bean:write name="LiqDeudaJud" property="saldo" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
									        <td><bean:write name="LiqDeudaJud" property="actualizacion" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
									        <td><bean:write name="LiqDeudaJud" property="total" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
									    </logic:notEqual>
								       	
								       	<!-- Si posee Convenio -->
								       	<logic:equal name="LiqDeudaJud" property="poseeConvenio" value="true">
								       		<td colspan="4">
							       				<bean:write name="LiqDeudaJud" property="observacion"/>
								       		</td>
								       	</logic:equal>
								       	
									    <!-- Es Indeterminada -->
						       			<logic:equal name="LiqDeudaJud" property="esIndeterminada" value="true">
						       				<td colspan="4">
						       					<b><bean:write name="LiqDeudaJud" property="observacion"/>&nbsp;</b>
											</td>
						       			</logic:equal>
									    
									    <!-- Es Reclamada -->
								       	<logic:equal name="LiqDeudaJud" property="esReclamada" value="true">
						       				<td colspan="4">
						       					<b><bean:write name="LiqDeudaJud" property="observacion"/>&nbsp;</b>
											</td>
								    	</logic:equal>
							
									    <!-- Es Exento Pago -->
								       	<logic:equal name="LiqDeudaJud" property="esExentoPago" value="true">
						       				<td colspan="4">
						       					<b><bean:write name="LiqDeudaJud" property="observacion"/>&nbsp;</b>
											</td>
								    	</logic:equal>
								    	
								    	 <!-- Es Constancia No Habilitada -->
								       	<logic:equal name="LiqDeudaJud" property="esNoHabilitada" value="true">
						       				<td colspan="4">
						       					<b><bean:write name="LiqDeudaJud" property="observacion"/>&nbsp;</b>
											</td>
								    	</logic:equal>
							       	</tr>
								</logic:iterate>
								<!-- Fin Item LiqDeudaVO -->
						       	<tr>       		
						       		<td colspan="7" class="celdatotales" align="right">
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
				<!-- Fin Deuda en Gestion Judicial -->
				</logic:notEqual>
			</logic:notEmpty>

			<logic:empty name="deudaProMasConsPorCtaSearchPageVO" property="listResult">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="gde" key="gde.deudaIncProMasConsPorCtaSearchPage.deudas.title" /></caption>
					<tbody>
						<tr>
							<td align="center"><bean:message bundle="base" key="base.resultadoVacio" /></td>
						</tr>
					</tbody>
				</table>
			</logic:empty>
		</logic:equal>
	</div>
	<!-- Fin Resultado Filtro -->

	<table class="tablabotones">
		<tr>
			<td align="left">
				<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver" />
				</html:button>
			</td>
		</tr>
	</table>

	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>

</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
