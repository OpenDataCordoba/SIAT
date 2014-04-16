<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/ConsultaCodRefPag.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="gde" key="gde.codRefPagAdapter.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				&nbsp;
			</td>				
			<td align="right">
	 			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
			
	<!-- Filtro -->
	<fieldset>
	<legend><bean:message bundle="gde" key="gde.codRefPagAdapter.filtros.legend"/></legend>
		<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="gde" key="gde.liqCodRefPagAdapter.tipoBoleta.label"/>: </td>
				<td class="normal">
					<html:select name="liqCodRefPagSearchPageVO" property="tipoBoleta" styleClass="select">
						<html:optionsCollection name="liqCodRefPagSearchPageVO" property="listTipoBoleta" label="codValue" value="id"/>
					</html:select>
				</td>
			</tr>
			<tr>
				<td><label><bean:message bundle="gde" key="gde.liqCodRefPagAdapter.codRefPag.label"/>: </label></td>
				<td class="normal"><html:text name="liqCodRefPagSearchPageVO" property="codRefPagView" size="15"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="gde" key="gde.liqCodRefPagAdapter.importe.label"/>: </label></td>
				<td class="normal"><html:text name="liqCodRefPagSearchPageVO" property="importeView" size="15"/></td>
			</tr>
			
		</table>
			
		<p align="center">
		  	<html:button property="btnLimpiar"  styleClass="boton" onclick="submitForm('limpiar', '');">
				<bean:message bundle="base" key="abm.button.limpiar"/>
			</html:button>
			&nbsp;
		  	<html:button property="btnBuscar"  styleClass="boton" onclick="submitForm('buscar', '');">
				<bean:message bundle="base" key="abm.button.buscar"/>
			</html:button>
		</p>
	</fieldset>	
	<!-- Fin Filtro -->
		
	<!-- Resultado Filtro -->
	<div id="resultadoFiltro">
		<logic:equal name="liqCodRefPagSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="liqCodRefPagSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody></tbody>
				</table>
				<table border="0" cellpadding="0" cellspacing="1" width="100%">			
						<logic:iterate id="VO" name="liqCodRefPagSearchPageVO" property="listResult">
							<tr>
								<td>
									<!--1 Dedua -->
									<logic:equal name="liqCodRefPagSearchPageVO" property="tipoBoletaView" value="1">
										<fieldset>
											<table class="tabladatos">
												<tr>
													<td><label><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.periodoDeuda"/>: </label></td>
													<td class="normal"><bean:write name="VO" property="periodoDeuda"/></td>
												</tr>
												<tr>
													<td><label><bean:message bundle="gde" key="gde.liqReclamo.Importe.label"/>: </label></td>
													<td class="normal"><bean:write name="VO" property="saldoView"/></td>
												</tr>
												<tr>
													<td><label><bean:message bundle="gde" key="gde.liqReclamo.Cuenta.label"/>: </label></td>
													<td class="normal"><bean:write name="VO" property="nroCuenta"/></td>
												</tr>
												<tr>
													<td><label><bean:message bundle="gde" key="gde.planRecurso.label"/>: </label></td>
													<td class="normal"><bean:write name="VO" property="desRecurso"/></td>
												</tr>
												<tr>
													<td><label><bean:message bundle="gde" key="gde.estadoCuenta.estado.label"/>: </label></td>
													<td class="normal"><bean:write name="VO" property="desEstado"/></td>
												</tr>
												<tr>
													<td><label><bean:message bundle="gde" key="gde.estadoCuenta.viaDeuda.label"/>: </label></td>
													<td class="normal"><bean:write name="VO" property="desViaDeuda"/></td>
												</tr>
											</table>
										</fieldset>	
									</logic:equal>
									
									<!--2 Recibo (de Deuda) -->
									
									<logic:equal name="liqCodRefPagSearchPageVO" property="tipoBoletaView" value="2">
										<fieldset>
											<table class="tabladatos">
												<tr>
													<td><label><bean:message bundle="gde" key="gde.tramite.nroRecibo.label"/>: </label></td>
													<td class="normal"><bean:write name="VO" property="numeroReciboView"/></td>
												</tr>
												<tr>
													<td><label><bean:message bundle="gde" key="gde.planRecurso.label"/>: </label></td>
													<td class="normal"><bean:write name="VO" property="recurso.desRecurso"/></td>
												</tr>
												<tr>
													<td><label><bean:message bundle="gde" key="gde.liqReclamo.Importe.label"/>: </label></td>
													<td class="normal"><bean:write name="VO" property="totalView"/></td>
												</tr>
											</table>
										</fieldset>
									</logic:equal>
									
									<!--3 Convenio Cuota -->
									<logic:equal name="liqCodRefPagSearchPageVO" property="tipoBoletaView" value="3">
										<fieldset>	
											<table class="tabladatos">
												<tr>
													<td><label><bean:message bundle="gde" key="gde.liqReclamo.NumeroCuota.label"/>: </label></td>
													<td class="normal"><bean:write name="VO" property="nroCuota"/></td>
												</tr>
												<tr>
													<td><label><bean:message bundle="gde" key="gde.liqReclamo.Importe.label"/>: </label></td>
													<td class="normal"><bean:write name="VO" property="importeCuotaView"/></td>
												</tr>
												<tr>
													<td><label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.nroConvenio.label"/>: </label></td>
													<td class="normal"><bean:write name="VO" property="nroConvenio"/></td>
												</tr>
												<tr>
													<td><label><bean:message bundle="gde" key="gde.liqReclamo.Cuenta.label"/>: </label></td>
													<td class="normal"><bean:write name="VO" property="nroCuenta"/></td>
												</tr>
												<tr>
													<td><label><bean:message bundle="gde" key="gde.planRecurso.label"/>: </label></td>
													<td class="normal"><bean:write name="VO" property="desRecurso"/></td>
												</tr>
											
											</table>
										</fieldset>	
									</logic:equal>										
									
									<!--4 Convenio Cuota -->
									<logic:equal name="liqCodRefPagSearchPageVO" property="tipoBoletaView" value="4">
										<fieldset>
											<table class="tabladatos">
												<tr>
													<td><label><bean:message bundle="gde" key="gde.tramite.nroRecibo.label"/>: </label></td>
													<td class="normal"><bean:write name="VO" property="numeroReciboView"/></td>
												</tr>
												<tr>
													<td><label><bean:message bundle="gde" key="gde.planRecurso.label"/>: </label></td>
													<td class="normal"><bean:write name="VO" property="recurso.desRecurso"/></td>
												</tr>
												<tr>
													<td><label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.nroConvenio.label"/>: </label></td>
													<td class="normal"><bean:write name="VO" property="convenio.nroConvenio"/></td>
												</tr>
												<tr>
													<td><label><bean:message bundle="gde" key="gde.liqReclamo.Importe.label"/>: </label></td>
													<td class="normal"><bean:write name="VO" property="totalView"/></td>
												</tr>
											</table>
										</fieldset>
									</logic:equal>
								</td>
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="liqCodRefPagSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="liqCodRefPagSearchPageVO" property="listResult">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
                	<tbody>
						<tr><td align="center">
							<bean:message bundle="base" key="base.resultadoVacio"/>
						</td></tr>
					</tbody>			
				</table>
			</logic:empty>
		</logic:equal>			
	</div>
	<!-- Fin Resultado Filtro -->

	<table class="tablabotones">
		<tr>				
			<td align="left">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
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

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->