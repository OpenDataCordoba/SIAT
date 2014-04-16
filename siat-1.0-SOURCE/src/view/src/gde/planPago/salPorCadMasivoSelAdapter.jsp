<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarSalPorCadMasivo.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="gde" key="gde.salPorCadMasivoSelAdapter.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right"><html:button property="btnVolver"
				styleClass="boton" onclick="submitForm('volver', '');">
				<bean:message bundle="base" key="abm.button.volver" />
			</html:button></td>
		</tr>
	</table>
	<!-- Saldo Por Caducidad -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.salPorCadMasivoAdminAdapter.saldo.legend"/></legend>
		
			<table class="tabladatos">
				<!-- combo Recurso -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.salPorCad.fechaSalCad"/></td>
					<td class="normal"><bean:write name="salPorCadMasivoSelAdapterVO" property="saldoPorCaducidad.fechaSaldoView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal" colspan="3">
						<bean:write name="salPorCadMasivoSelAdapterVO" property="saldoPorCaducidad.recurso.desRecurso"/>
					</td>
				</tr>
				<!-- combo Plan -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.plan.label"/>: </label></td>				
					<td class="normal" colspan="3">
						<bean:write name="salPorCadMasivoSelAdapterVO" property="saldoPorCaducidad.plan.desPlan"/>
					</td>
				</tr>
				<!-- fechaDesde y fechaHasta -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.salPorCadMasivoAdapter.fechaForDesde"/>: </label></td>
					<td class="normal">
						<bean:write name="salPorCadMasivoSelAdapterVO" property="saldoPorCaducidad.fechaFormDesdeSaldoView"/>
					</td>
					<td><label><bean:message bundle="gde" key="gde.salPorCadMasivoAdapter.fechaForHasta"/>: </label></td>
					<td class="normal">
						<bean:write name="salPorCadMasivoSelAdapterVO" property="saldoPorCaducidad.fechaFormHastaSaldoView"/>
					</td>
				</tr>
				<!-- Cuota Superior A -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.salPorCadMasivoAdapter.montoSuperior"/></td>
					<td class="normal"><bean:write name="salPorCadMasivoSelAdapterVO" property="saldoPorCaducidad.cuotaSuperiorAView"/></td>
				</tr>
				<!-- Caso -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
					<td class="normal" colspan="3"/>
						<bean:define id="IncludedVO" name="salPorCadMasivoSelAdapterVO" property="saldoPorCaducidad"/>
						<%@ include file="/cas/caso/includeCasoView.jsp" %>	
					</td>
				</tr>
				<!-- Observacion -->				
				<tr>
					<td><label><bean:message bundle="gde" key="gde.salPorCadMasivoAdapter.observacion"/>: </label></td>
					<td class="normal" colspan="3">	
						<bean:write name="salPorCadMasivoSelAdapterVO" property="saldoPorCaducidad.observacion"/>
					</td>
				</tr>
			</table>
	</fieldset>	
	
	<logic:equal name="salPorCadMasivoSelAdapterVO" property="filtroHabilitado" value="true">
	<!-- Filtro por Estado Proceso de Convenios -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.salPorCadMasivoAdminAdapter.filtroEstadoProcesoConvenio.legend"/></legend>
				<table class="tabladatos">
					<!-- Lista de Estado Proceso de Convenio -->
					<tr>
						<td><label><bean:message bundle="gde" key="gde.salPorCadMasivoEditAdapter.estado.label"/></td>
						<td class="normal">	
							<html:select name="salPorCadMasivoSelAdapterVO" property="estadoProcesoConvenio" styleClass="select"  onchange="submitForm('paramEstadoConvenio', '');">
								<html:optionsCollection name="salPorCadMasivoSelAdapterVO" property="listEstadoProcesoConvenio" label="descripcion" value="value" />
							</html:select>
						</td>
					</tr>
				</table>
		</fieldset>	
	</logic:equal>
	
	<fieldset>
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
		<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
		<tr>
			<th width="1">&nbsp;</th> <!-- ver -->
			<th width="1">&nbsp;</th> <!-- eliminar -->
			<th align="left"><bean:message bundle="gde" key="gde.salPorCadMasivoEditAdapter.fechaFor.label"/></th>
			<th align="left"><bean:message bundle="gde" key="gde.salPorCadMasivoEditAdapter.estado.label"/></th>
			<th align="left"><bean:message bundle="gde" key="gde.salPorCadMasivoEditAdapter.via.label"/></th>
			<th align="left"><bean:message bundle="gde" key="gde.salPorCadMasivoEditAdapter.nroConvenio.label"/></th>
			<th align="left"><bean:message bundle="gde" key="gde.salPorCadMasivoEditAdapter.cuenta.label"/></th>
			<th align="left"><bean:message bundle="gde" key="gde.salPorCadMasivoEditAdapter.totalImporte.label"/></th>
			<th align="left"><bean:message bundle="gde" key="gde.salPorCadMasivoAdapter.observacion"/></th>
		</tr>
			<logic:iterate id="convenio" name="salPorCadMasivoSelAdapterVO" property="saldoPorCaducidad.listConveniosSelAlm">
				<tr>
					<td>
						<a style="cursor:pointer; cursor:hand;" onclick="submitForm('verConvenio','<bean:write name="convenio" property="id" bundle="base" formatKey="general.format.key"/>');">
							<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
						</a>
					</td>
					<td>
						<logic:equal name="salPorCadMasivoSelAdapterVO" property="saldoPorCaducidad.eliminaConvenioActivo" value="true">
							<a style="cursor:pointer; cursor:hand;" onclick="submitConfirmForm('quitarConvenio','<bean:write name="convenio" property="id" bundle="base" formatKey="general.format.key"/>','<bean:message bundle="gde" key="gde.salPorCadMasivoSelAdapter.promptEliminar"/><bean:write name="convenio" property="nroConvenioView"/>');">
								<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
							</a>
						</logic:equal>
						<logic:notEqual name="salPorCadMasivoSelAdapterVO" property="saldoPorCaducidad.eliminaConvenioActivo" value="true">
							<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
						</logic:notEqual>
					</td>
					
					<td><bean:write name="convenio" property="fechaForView"/>&nbsp;</td>
					<td><bean:write name="convenio" property="estadoConvenio.desEstadoConvenio"/>&nbsp;</td>
					<td><bean:write name="convenio" property="viaDeuda.desViaDeuda"/>&nbsp;</td>
					<td><bean:write name="convenio" property="nroConvenioView"/>&nbsp;</td>
					<td><bean:write name="convenio" property="cuenta.numeroCuenta"/>&nbsp;</td>
					<td><bean:write name="convenio" property="totImporteConvenioView"/>&nbsp;</td>
					<td><bean:write name="convenio" property="observacionFor"/>&nbsp;</td>
				</tr>
			</logic:iterate>
			<tr>
				<td class="paginador" align="center" colspan="20">
					<bean:define id="pager" name="salPorCadMasivoSelAdapterVO"/>
						<%@ include file="/base/pager.jsp" %>
				</td>
			</tr>
		</table>
	</fieldset>
	<!-- Rescate -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
	   	    	<logic:equal name="salPorCadMasivoSelAdapterVO" property="filtroHabilitado" value="true">
					<html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
					    <bean:message bundle="base" key="abm.button.imprimir"/>
					</html:button>	
				</logic:equal>
  	    	</td>
   	    </tr>
   	</table>
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
  	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
  	
	<input type="hidden" name="selectedId" value="<bean:write name="salPorCadMasivoSelAdapterVO" property="saldoPorCaducidad.id"bundle="base" formatKey="general.format.key"/>"/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="seleccionar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclucion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
	
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->