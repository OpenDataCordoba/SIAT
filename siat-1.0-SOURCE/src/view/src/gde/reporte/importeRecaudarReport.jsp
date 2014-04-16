<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/ReporteImporteRecaudar.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="gde" key="gde.importeRecaudarReport.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right"><html:button property="btnVolver"
				styleClass="boton" onclick="submitForm('volver', '');">
				<bean:message bundle="base" key="abm.button.volver" />
			</html:button></td>
		</tr>
	</table>
	<p>
		<logic:equal name="importeRecaudarReportVO" property="modoSeleccionar" value="true">
			<bean:message bundle="base" key="base.busquedaLegendBusqueda"/>
			<bean:message bundle="gde" key="gde.importeRecaudar.label"/>
		</logic:equal>
		<logic:notEqual name="importeRecaudarReportVO" property="modoSeleccionar" value="true">
			<bean:message bundle="gde" key="gde.importeRecaudarReport.legend"/>
		</logic:notEqual>		
	</p>
		
	<!-- Filtro -->
	<fieldset>
	<legend><bean:message bundle="base" key="base.parametrosBusqueda"/></legend>
		<table class="tabladatos">
			<!-- <#Filtros#> -->
		<!-- Recurso -->
		<tr>	
			<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
			<td class="normal" colspan="3">
					<html:select name="importeRecaudarReportVO" property="plan.recurso.id" disabled ="false" styleClass="select" onchange="submitForm('paramPlanByRecurso', '');" >
						<bean:define id="includeRecursoList" name="importeRecaudarReportVO" property="listRecurso"/>
						<bean:define id="includeIdRecursoSelected" name="importeRecaudarReportVO" property="plan.recurso.id"/>
						<%@ include file="/def/gravamen/includeRecurso.jsp" %>
					</html:select>							
			</td>
		</tr>	
		<!-- Plan -->
		<tr>	
			<td><label><bean:message bundle="gde" key="gde.plan.label"/>: </label></td>		
			<td class="normal" colspan="3">
					<html:select name="importeRecaudarReportVO" property="plan.id" disabled ="false" styleClass="select" onchange="submitForm('paramPlan', '');">
						<html:optionsCollection name="importeRecaudarReportVO" property="listPlan" label="desPlan" value="id" />
					</html:select>			
			</td>
		</tr>
		<!-- Procurador, se muestra solo si visualizarComboProcurador (EsViaJudicial || ProcuradorEnSession) -->
		<tr>
			<td><label><bean:message bundle="gde"
				key="gde.procurador.label" />: </label></td>
			<logic:equal name="importeRecaudarReportVO"property="visualizarComboProcurador" value="true">
				<td class="normal"><html:select name="importeRecaudarReportVO"	property="plan.procurador.id" styleClass="select">
					<html:optionsCollection name="importeRecaudarReportVO" property="listProcurador" label="descripcion" value="id" />
				</html:select></td>
			</logic:equal>
			<logic:equal name="importeRecaudarReportVO"property="visualizarComboProcurador" value="false">
				<td class="normal"><html:select name="importeRecaudarReportVO"	property="plan.procurador.id" styleClass="select" disabled="true">
					<html:optionsCollection name="importeRecaudarReportVO" property="listProcurador" label="descripcion" value="id" />
				</html:select></td>
			</logic:equal>
		</tr>
		<!-- FechaVencimientoDesde y Hasta -->
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.importeRecaudarReport.fechaVencimientoDesde.label"/>: </label></td>
			<td class="normal">
				<html:text name="importeRecaudarReportVO" property="fechaVencimientoDesdeView" styleId="fechaVencimientoDesdeView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaVencimientoDesdeView');" id="a_fechaVencimientoDesdeView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>
			<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.importeRecaudarReport.fechaVencimientoHasta.label"/>: </label></td>
			<td class="normal">
				<html:text name="importeRecaudarReportVO" property="fechaVencimientoHastaView" styleId="fechaVencimientoHastaView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaVencimientoHastaView');" id="a_fechaVencimientoHastaView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>
		</tr>					
		</table>
			
		<p align="center">
		<logic:equal name="importeRecaudarReportVO" property="procesando" value="false">
		  	<html:button property="btnLimpiar"  styleClass="boton" onclick="submitForm('limpiar', '');" disabled="false">
				<bean:message bundle="base" key="abm.button.limpiar"/>
			</html:button>
			&nbsp;
		  	<html:button property="btnBuscar"  styleClass="boton" onclick="submitForm('buscar', '');" disabled="false">
				<bean:message bundle="base" key="abm.button.buscar"/>
			</html:button>
		</logic:equal>
		<logic:equal name="importeRecaudarReportVO" property="procesando" value="true">
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
		<logic:equal name="importeRecaudarReportVO" property="procesando" value="true">
		<fieldset>
			<table class="tabladatos">
				<tr>
					<td class="normal">
						<bean:write name="importeRecaudarReportVO" property="desRunningRun"/>
						&nbsp;se encuentra procesando ...&nbsp;
						<img border="0" src="<%=request.getContextPath()%>/images/iconos/barraProcesando.gif"/>
					</td>
				</tr>
			</table>
		</fieldset>	
		</logic:equal>
		<!-- Fin Mensaje de Reporte en Generacion -->
	
		<!-- Mensaje de Reporte en con Error -->
		<logic:equal name="importeRecaudarReportVO" property="error" value="true">
		<fieldset>
			<table class="tabladatos">
				<tr>
					<td class="normal"><bean:write name="importeRecaudarReportVO" property="desErrorRun"/> se proces&oacute; con error: </td>
				</tr>
				<tr>
					<td class="normal">
						<bean:write name="importeRecaudarReportVO" property="estErrorRun"/>
					</td>
				</tr>
			</table>
		</fieldset>	
		</logic:equal>
		<!-- Fin Mensaje de Reporte con Error -->
		
	<!-- Resultado Filtro -->
	<div id="resultadoFiltro">
		<logic:equal name="importeRecaudarReportVO" property="existeReporteGenerado" value="true">
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
				<caption><bean:write name="importeRecaudarReportVO" property="reporteGenerado.titulo" /></caption>
               	<tbody>
	               	<tr>
						<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
						<th align="left"><bean:message bundle="gde" key="gde.importeRecaudarReport.reporteGenerado"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.importeRecaudarReport.ctdResultado"/></th>
					</tr>
					<tr>
						<!-- Seleccionar -->
						<td>
							<a style="cursor: pointer; cursor: hand;" onclick="submitDownload('downloadFile', '<bean:write name="importeRecaudarReportVO" property="reporteGenerado.fileName" />');">
								<img title="<bean:message bundle="gde" key="gde.importeRecaudarReport.button.verReporte"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
							</a>
						</td>
						<td><bean:write name="importeRecaudarReportVO" property="reporteGenerado.descripcion" />&nbsp;</td>
						<td><bean:write name="importeRecaudarReportVO" property="reporteGenerado.ctdResultadosView" />&nbsp;</td>
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

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
