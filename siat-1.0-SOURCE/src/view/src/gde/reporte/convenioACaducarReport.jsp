<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/ReporteConvenioACaducar.do" >

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="gde" key="gde.convenioACaducarReport.title"/></h1>	
		
	<p><bean:message bundle="gde" key="gde.convenioACaducarReport.legend"/></p>
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right"><html:button property="btnVolver"
				styleClass="boton" onclick="submitForm('volver', '');">
				<bean:message bundle="base" key="abm.button.volver" />
			</html:button></td>
		</tr>
	</table>
	<!-- Filtro -->
		<fieldset>
			<legend><bean:message bundle="base" key="base.parametrosBusqueda"/></legend>
			<table class="tabladatos">
				<!-- combo Recurso -->
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal" colspan="3">
						<html:select name="convenioACaducarReportVO" property="recurso.id" styleClass="select" onchange="submitForm('paramRecurso', '');">
							<bean:define id="includeRecursoList"       name="convenioACaducarReportVO" property="listRecurso"/>
							<bean:define id="includeIdRecursoSelected" name="convenioACaducarReportVO" property="recurso.id"/>
							<%@ include file="/def/gravamen/includeRecurso.jsp" %>
						</html:select>
					</td>
				</tr>
				<!-- combo Plan -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.plan.label"/>: </label></td>
					<td class="normal" colspan="3">
						<html:select name="convenioACaducarReportVO" property="convenio.plan.id" styleClass="select" onchange="submitForm('paramPlan', '');">
							<html:optionsCollection name="convenioACaducarReportVO" 
								property="listPlan" label="desPlan" value="id" />
						</html:select>
					</td>
				</tr>
				<!-- fechaConvenioDesde y fechaConvenioHasta -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.convenioACaducarReport.fechaConvenioDesde.label"/>: </label></td>
					<td class="normal">
						<html:text name="convenioACaducarReportVO" property="fechaConvenioDesdeView" styleId="fechaConvenioDesdeView" size="15" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaConvenioDesdeView');" id="a_fechaConvenioDesdeView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
					<td><label><bean:message bundle="gde" key="gde.convenioACaducarReport.fechaConvenioHasta.label"/>: </label></td>
					<td class="normal">
						<html:text name="convenioACaducarReportVO" property="fechaConvenioHastaView" styleId="fechaConvenioHastaView" size="15" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaConvenioHastaView');" id="a_fechaConvenioHastaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>				
				</tr>
				<!-- combo via deuda y fecha caducidad -->
				<tr>
					<td><label><bean:message bundle="def" key="def.viaDeuda.label"/>: </label></td>
					<logic:equal name="convenioACaducarReportVO" property="planSeleccionado" value="false">
						<!-- No se selecciono Plan: visualizo el combo ViaDeuda -->
							<td class="normal" >
								<html:select name="convenioACaducarReportVO" property="convenio.viaDeuda.id" styleClass="select" >
									<html:optionsCollection name="convenioACaducarReportVO" 
										property="listViaDeuda" label="desViaDeuda" value="id" />
								</html:select>
							</td>
					</logic:equal>
					<logic:equal name="convenioACaducarReportVO" property="planSeleccionado" value="true">
						<!-- Se selecciono Plan: no visualizo el combo ViaDeuda -->
							<td class="normal" >
								<html:select name="convenioACaducarReportVO" property="convenio.viaDeuda.id" styleClass="select" disabled="true" >
									<html:optionsCollection name="convenioACaducarReportVO" 
										property="listViaDeuda" label="desViaDeuda" value="id" />
								</html:select>
							</td>
					</logic:equal>
					<td><label><bean:message bundle="gde" key="gde.convenioACaducarReport.fechaCaducidad.label"/>: </label></td>
					<td class="normal">
						<html:text name="convenioACaducarReportVO" property="fechaCaducidadView" styleId="fechaCaducidadView" size="15" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaCaducidadView');" id="a_fechaCaducidadView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>			
				</tr>			
				<!-- cuota desde y cuota hasta -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.convenioACaducarReport.cuotaDesde.label"/>: </label></td>
					<td class="normal"><html:text name="convenioACaducarReportVO" property="cuotaDesdeView" size="10" maxlength="10" styleClass="datos" /></td>
					<td><label><bean:message bundle="gde" key="gde.convenioACaducarReport.cuotaHasta.label"/>: </label></td>
					<td class="normal"><html:text name="convenioACaducarReportVO" property="cuotaHastaView" size="10" maxlength="10" styleClass="datos" /></td>
				</tr>
				<!-- importeCuota desde y importeCuota hasta -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.convenioACaducarReport.importeCuotaDesde.label"/>: </label></td>
					<td class="normal"><html:text name="convenioACaducarReportVO" property="importeCuotaDesdeView" size="10" maxlength="10" styleClass="datos" /></td>
					<td><label><bean:message bundle="gde" key="gde.convenioACaducarReport.importeCuotaHasta.label"/>: </label></td>
					<td class="normal"><html:text name="convenioACaducarReportVO" property="importeCuotaHastaView" size="10" maxlength="10" styleClass="datos" /></td>
				</tr>
	
				<!-- combo Estado Convenio -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.estadoConvenio.label"/>: </label></td>
					<td class="normal" >
						<html:select name="convenioACaducarReportVO" property="convenio.estadoConvenio.id" styleClass="select">
							<html:optionsCollection name="convenioACaducarReportVO" 
								property="listEstadoConvenio" label="desEstadoConvenio" value="id" />
						</html:select>
					</td>
				</tr>
			</table>
			
			<p align="center">
			<logic:equal name="convenioACaducarReportVO" property="procesando" value="false">
			  	<html:button property="btnLimpiar"  styleClass="boton" onclick="submitForm('limpiar', '');" disabled="false">
					<bean:message bundle="base" key="abm.button.limpiar"/>
				</html:button>
				&nbsp;
			  	<html:button property="btnBuscar"  styleClass="boton" onclick="submitForm('buscar', '');" disabled="false">
					<bean:message bundle="base" key="abm.button.buscar"/>
				</html:button>
			</logic:equal>
			<logic:equal name="convenioACaducarReportVO" property="procesando" value="true">
			  	<html:button property="btnLimpiar"  styleClass="boton" onclick="submitForm('limpiar', '');" disabled="true">
					<bean:message bundle="base" key="abm.button.limpiar"/>
				</html:button>
				&nbsp;
			  	<html:button property="btnBuscar"  styleClass="boton" onclick="submitForm('buscar', '');" disabled="true">
					<bean:message bundle="base" key="abm.button.buscar"/>
				</html:button>
				&nbsp;
				<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('refill', '');" disabled='false' styleId="locate_paso">
						<bean:message bundle="pro" key="pro.abm.button.refill"/>
				</html:button>&nbsp;&nbsp;
			</logic:equal>
			</p>			
		</fieldset>	
		<!-- Fin Filtro -->
		
		<!-- Mensaje de Reporte en Generacion -->
		<logic:equal name="convenioACaducarReportVO" property="procesando" value="true">
		<fieldset>
			<table class="tabladatos">
				<tr>
					<td class="normal">
						<bean:write name="convenioACaducarReportVO" property="desRunningRun"/>
						&nbsp;se encuentra procesando ...&nbsp;
						<img border="0" src="<%=request.getContextPath()%>/images/iconos/barraProcesando.gif"/>
					</td>
				</tr>
			</table>
		</fieldset>	
		</logic:equal>
		<!-- Fin Mensaje de Reporte en Generacion -->

		<!-- Mensaje de Reporte en con Error -->
		<logic:equal name="convenioACaducarReportVO" property="error" value="true">
		<fieldset>
			<table class="tabladatos">
				<tr>
					<td  class="normal"><bean:write name="convenioACaducarReportVO" property="desErrorRun"/> se proces&oacute; con error: </td>
				</tr>
				<tr>				
					<td class="normal">
						<bean:write name="convenioACaducarReportVO" property="estErrorRun"/>
					</td>
				</tr>
			</table>
		</fieldset>	
		</logic:equal>
		<!-- Fin Mensaje de Reporte con Error -->
	
	<!-- Resultado Filtro -->
	<div id="resultadoFiltro">
		<logic:equal name="convenioACaducarReportVO" property="existeReporteGenerado" value="true">
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
				<caption><bean:write name="convenioACaducarReportVO" property="reporteGenerado.titulo" /></caption>
               	<tbody>
	               	<tr>
						<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
						<th align="left"><bean:message bundle="gde" key="gde.convenioACaducarReport.reporteGenerado"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.convenioACaducarReport.ctdResultado"/></th>
					</tr>
					<tr>
						<!-- Seleccionar -->
							<td>
								<a style="cursor: pointer; cursor: hand;" onclick="submitDownload('downloadFile', '<bean:write name="convenioACaducarReportVO" property="reporteGenerado.fileName" />');">
									<img title="<bean:message bundle="gde" key="gde.convenioACaducarReport.button.verReporte"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
								</a>
							</td>
							<td><bean:write name="convenioACaducarReportVO" property="reporteGenerado.descripcion" />&nbsp;</td>
							<td><bean:write name="convenioACaducarReportVO" property="reporteGenerado.ctdResultadosView" />&nbsp;</td>
					</tr>
				</tbody>
			</table>
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

	<input type="hidden" name="fileParam" value=""/>		
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclucion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>

</html:form>
<!-- Fin Tabla que contiene todos los formularios -->