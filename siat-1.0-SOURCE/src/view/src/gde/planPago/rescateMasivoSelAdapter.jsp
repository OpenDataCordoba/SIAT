<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarRescate.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="gde" key="gde.rescateMasivoSelAdapter.title"/></h1>	
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
		<legend><bean:message bundle="gde" key="gde.rescateMasivoSelAdapter.legend"/></legend>
		
			<table class="tabladatos">
				<!-- combo Recurso -->
				<tr>
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="rescateAdapterVO" property="rescate.recurso.desRecurso" /> </td>
				</tr>
				<!-- combo Plan -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.plan.label"/>: </label></td>				
					<td class="normal" colspan="3"><bean:write name="rescateAdapterVO" property="rescate.plan.desPlan" /> </td>
				</tr>
				<!-- fechaDesde y fechaHasta -->
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.rescate.fechaRescateDesde"/>: </label></td>
					<td class="normal">
						<bean:write name="rescateAdapterVO" property="rescate.fechaRescateView"/>
					</td>
					<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.rescate.fechaRescateHasta"/>: </label></td>
					<td class="normal">
						<bean:write name="rescateAdapterVO" property="rescate.fechaVigRescateView"/>
					</td>
				</tr>
				<!-- Inclucion de CasoView -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
					<td colspan="3">
						<bean:define id="IncludedVO" name="rescateAdapterVO" property="rescate"/>
						<%@ include file="/cas/caso/includeCasoView.jsp" %>				
					</td>
				</tr>
				 
				<!-- Observacion -->				
				<tr>
					<td><label><bean:message bundle="gde" key="gde.rescate.observacion"/>: </label></td>
					<td class="normal" colspan="3">	
						<bean:write name="rescateAdapterVO" property="rescate.observacion"/>
					</td>
				</tr>
			</table>
	</fieldset>	
	<fieldset>
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
		<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
		<tr>
			<th width="1">&nbsp;</th> <!-- ver -->
			<th width="1">&nbsp;</th> <!-- eliminar -->
			<th align="left"><bean:message bundle="gde" key="gde.salPorCadMasivoEditAdapter.fechaFor.label"/></th>
			<th align="left"><bean:message bundle="gde" key="gde.salPorCadMasivoEditAdapter.nroConvenio.label"/></th>
			<th align="left"><bean:message bundle="gde" key="gde.salPorCadMasivoEditAdapter.cuenta.label"/></th>
			<th align="left"><bean:message bundle="gde" key="gde.salPorCadMasivoEditAdapter.via.label"/></th>
			<th align="left"><bean:message bundle="gde" key="gde.rescateAdapterSel.cantCuotas.label"/></th>
			<th align="left"><bean:message bundle="gde" key="gde.salPorCadMasivoEditAdapter.totalImporte.label"/></th>
			<th align="left"><bean:message bundle="gde" key="gde.salPorCadMasivoAdapter.observacion"/></th>

		</tr>
			<logic:iterate id="convenio" name="rescateAdapterVO" property="rescate.listConveniosSelAlm">
				<tr>
					<td>
						<a style="cursor:pointer; cursor:hand;" onclick="submitForm('verConvenio','<bean:write name="convenio" property="id" bundle="base" formatKey="general.format.key"/>');">
							<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
						</a>
					</td>
					<td>
						<logic:equal name="rescateAdapterVO" property="rescate.eliminaConvenioActivo" value="true">
							<a style="cursor:pointer; cursor:hand;" onclick="submitConfirmForm('quitarConvenio','<bean:write name="convenio" property="id" bundle="base" formatKey="general.format.key"/>','<bean:message bundle="gde" key="gde.salPorCadMasivoSelAdapter.promptEliminar"/><bean:write name="convenio" property="nroConvenioView"/>');">
								<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
							</a>
						</logic:equal>
						<logic:notEqual name="rescateAdapterVO" property="rescate.eliminaConvenioActivo" value="true">
							<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
						</logic:notEqual>
					</td>
					<td><bean:write name="convenio" property="fechaForView"/>&nbsp;</td>
					<td><bean:write name="convenio" property="nroConvenioView"/>&nbsp;</td>
					<td><bean:write name="convenio" property="cuenta.numeroCuenta"/>&nbsp;</td>
					<td><bean:write name="convenio" property="viaDeuda.desViaDeuda"/>&nbsp;</td>
					<td><bean:write name="convenio" property="cantidadCuotasPlanView"/>&nbsp;</td>
					<td><bean:write name="convenio" property="totImporteConvenioView"/>&nbsp;</td>
					<td><bean:write name="convenio" property="observacionFor"/>&nbsp;</td>
				</tr>
			</logic:iterate>
			<tr>
				<td class="paginador" align="center" colspan="20">
					<bean:define id="pager" name="rescateAdapterVO"/>
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
   	    </tr>
   	</table>
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
		
	<input type="hidden" name="selectedId" value="<bean:write name="rescateAdapterVO" property="rescate.id"bundle="base" formatKey="general.format.key"/>"/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="seleccionar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclucion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
	
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->