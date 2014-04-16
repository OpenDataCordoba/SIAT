<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/ReporteContribuyenteCer.do" >

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="gde" key="gde.contribuyenteCerReport.title"/></h1>	
		
	<p><bean:message bundle="gde" key="gde.contribuyenteCerReport.legend"/></p>
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
				<tr>
						<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.contribuyenteCerReport.fechaReporte.label"/>: </label></td>
						<td class="normal">
							<html:text name="contribuyenteCerReportVO" property="fechaReporteView" styleId="fechaReporteView" size="15" maxlength="10" styleClass="datos" />
							<a class="link_siat" onclick="return show_calendar('fechaReporteView');" id="a_fechaReporteView">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>
				</tr>
				<!-- combo Recurso -->
				
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal" colspan="3">
						<html:select name="contribuyenteCerReportVO" property="recurso.id" styleClass="select">
							<bean:define id="includeRecursoList"       name="contribuyenteCerReportVO" property="listRecurso"/>
							<bean:define id="includeIdRecursoSelected" name="contribuyenteCerReportVO" property="recurso.id"/>
							<%@ include file="/def/gravamen/includeRecurso.jsp" %>
						</html:select>
					</td>
				</tr>
				   	<!-- periodoDesde -->
	
				<tr>
					<td><LABEL>(*)&nbsp;<bean:message bundle="gde" key="gde.contribuyenteCerReport.periodoDesde.label"/></LABEL></td>
					<td class="normal">
						<html:text name="contribuyenteCerReportVO" property="periodoDesde" size="3" maxlength="2"/>/
						<html:text name="contribuyenteCerReportVO" property="anioDesde"  size="5" maxlength="4"/>
						(mm/aaaa)
					</td>		
			
				<!-- periodoHasta -->	
					<td><LABEL>(*)&nbsp;<bean:message bundle="gde" key="gde.contribuyenteCerReport.periodoHasta.label"/></LABEL></td>
					<td class="normal">
						<html:text name="contribuyenteCerReportVO" property="periodoHasta"  size="3" maxlength="2"/>/
						<html:text name="contribuyenteCerReportVO" property="anioHasta"  size="5" maxlength="4"/>
						(mm/aaaa)
					</td>
				</tr>			
				
				<!-- fechaDesde y fechaHasta -->
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.contribuyenteCerReport.fechaPagoDesde.label"/>: </label></td>
					<td class="normal">
						<html:text name="contribuyenteCerReportVO" property="fechaDesdeView" styleId="fechaDesdeView" size="15" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
					<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.contribuyenteCerReport.fechaPagoHasta.label"/>: </label></td>
					<td class="normal">
						<html:text name="contribuyenteCerReportVO" property="fechaHastaView" styleId="fechaHastaView" size="15" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>				
				</tr>
								
			</table>						
							
			<p align="center">
			<logic:equal name="contribuyenteCerReportVO" property="procesando" value="false">
			  	<html:button property="btnLimpiar"  styleClass="boton" onclick="submitForm('limpiar', '');" disabled="false">
					<bean:message bundle="base" key="abm.button.limpiar"/>
				</html:button>
				&nbsp;
			  	<html:button property="btnBuscar"  styleClass="boton" onclick="submitForm('buscar', '');" disabled="false">
					<bean:message bundle="base" key="abm.button.buscar"/>
				</html:button>
			</logic:equal>
			<logic:equal name="contribuyenteCerReportVO" property="procesando" value="true">
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
		<logic:equal name="contribuyenteCerReportVO" property="procesando" value="true">
		<fieldset>
			<table class="tabladatos">
				<tr>
					<td class="normal">
						<bean:write name="contribuyenteCerReportVO" property="desRunningRun"/>
						&nbsp;se encuentra procesando ...&nbsp;
						<img border="0" src="<%=request.getContextPath()%>/images/iconos/barraProcesando.gif"/>
					</td>
				</tr>
			</table>
		</fieldset>	
		</logic:equal>
		<!-- Fin Mensaje de Reporte en Generacion -->
	
		<!-- Mensaje de Reporte en con Error -->
		<logic:equal name="contribuyenteCerReportVO" property="error" value="true">
		<fieldset>
			<table class="tabladatos">
				<tr>
					<td class="normal"><bean:write name="contribuyenteCerReportVO" property="desErrorRun"/> se proces&oacute; con error: </td>
				</tr>
				<tr>
					<td class="normal">
						<bean:write name="contribuyenteCerReportVO" property="estErrorRun"/>
					</td>
				</tr>
			</table>
		</fieldset>	
		</logic:equal>
		<!-- Fin Mensaje de Reporte con Error -->
		
	<!-- Resultado Filtro -->
	<div id="resultadoFiltro">
		<logic:equal name="contribuyenteCerReportVO" property="existeReporteGenerado" value="true">
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
				<caption><bean:write name="contribuyenteCerReportVO" property="reporteGenerado.titulo" /></caption>
               	<tbody>
	               	<tr>
						<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
						<th align="left"><bean:message bundle="gde" key="gde.recaudadoReport.reporteGenerado"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.recaudadoReport.ctdResultado"/></th>
					</tr>
					<tr>
						<!-- Seleccionar -->
							<td>
								<a style="cursor: pointer; cursor: hand;" onclick="submitDownload('downloadFile', '<bean:write name="contribuyenteCerReportVO" property="reporteGenerado.fileName" />');">
									<img title="<bean:message bundle="gde" key="gde.recaudadoReport.button.verReporte"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
								</a>
							</td>
							<td><bean:write name="contribuyenteCerReportVO" property="reporteGenerado.descripcion" />&nbsp;</td>
							<td><bean:write name="contribuyenteCerReportVO" property="reporteGenerado.ctdResultadosView" />&nbsp;</td>
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
<!-- recaudadoReport.jsp -->
<!-- Fin Tabla que contiene todos los formularios -->