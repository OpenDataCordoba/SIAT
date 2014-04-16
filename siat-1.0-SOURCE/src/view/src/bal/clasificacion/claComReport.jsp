<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/ReporteClaCom.do" >

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="bal" key="bal.claComReport.title"/></h1>	
		
	<p><bean:message bundle="bal" key="bal.claComReport.legend"/></p>
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
				<!-- Clasificador y Nivel -->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.clasificador.label"/>: </label></td>
					<td class="normal">
						<html:select name="claComReportVO" property="clasificador.id" styleClass="select" onchange="submitForm('paramClasificador', '');">
							<html:optionsCollection name="claComReportVO" property="listClasificador" label="descripcion" value="id" />
						</html:select>
					</td>		
					<td><label><bean:message bundle="bal" key="bal.claComReport.nivelHasta.label"/>: </label></td>
					<td class="normal">
						<html:select name="claComReportVO" property="nivel" styleClass="select" >
							<html:optionsCollection name="claComReportVO" property="listNivel" label="valueView" value="value" />
						</html:select>
					</td>		
				</tr>
				<!-- 1er Rango Fechas-->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.claComReport.priFechaDesde.label"/>: </label></td>
					<td class="normal">
						<html:text name="claComReportVO" property="priFechaDesdeView" styleId="priFechaDesdeView" size="15" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('priFechaDesdeView');" id="a_priFechaDesdeView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
					<td><label><bean:message bundle="bal" key="bal.claComReport.priFechaHasta.label"/>: </label></td>
					<td class="normal">
						<html:text name="claComReportVO" property="priFechaHastaView" styleId="priFechaHastaView" size="15" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('priFechaHastaView');" id="a_priFechaHastaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>				
				</tr>
				<!-- 2do Rango Fechas-->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.claComReport.segFechaDesde.label"/>: </label></td>
					<td class="normal">
						<html:text name="claComReportVO" property="segFechaDesdeView" styleId="segFechaDesdeView" size="15" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('segFechaDesdeView');" id="a_segFechaDesdeView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
					<td><label><bean:message bundle="bal" key="bal.claComReport.segFechaHasta.label"/>: </label></td>
					<td class="normal">
						<html:text name="claComReportVO" property="segFechaHastaView" styleId="segFechaHastaView" size="15" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('segFechaHastaView');" id="a_segFechaHastaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>				
				</tr>
			</table>
			
			<p align="center">
			 <logic:equal name="claComReportVO" property="procesando" value="false">
			  	<html:button property="btnLimpiar"  styleClass="boton" onclick="submitForm('limpiar', '');" disabled="false">
					<bean:message bundle="base" key="abm.button.limpiar"/>
				</html:button>
				&nbsp;
			  	<html:button property="btnBuscar"  styleClass="boton" onclick="submitForm('buscar', '');" disabled="false">
					<bean:message bundle="base" key="abm.button.buscar"/>
				</html:button>
			</logic:equal>
			 <logic:equal name="claComReportVO" property="procesando" value="true">
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
		<logic:equal name="claComReportVO" property="procesando" value="true">
		<fieldset>
			<table class="tabladatos">
				<tr>
					<td class="normal">
						<bean:write name="claComReportVO" property="desRunningRun"/>
						&nbsp;se encuentra procesando ...&nbsp;
						<img border="0" src="<%=request.getContextPath()%>/images/iconos/barraProcesando.gif"/>
					</td>
				</tr>
			</table>
		</fieldset>	
		</logic:equal>
		<!-- Fin Mensaje de Reporte en Generacion -->

		<!-- Mensaje de Reporte en con Error -->
		<logic:equal name="claComReportVO" property="error" value="true">
		<fieldset>
			<table class="tabladatos">
				<tr>
					<td  class="normal"><bean:write name="claComReportVO" property="desErrorRun"/> se proces&oacute; con error: </td>
				</tr>
				<tr>				
					<td class="normal">
						<bean:write name="claComReportVO" property="estErrorRun"/>
					</td>
				</tr>
			</table>
		</fieldset>	
		</logic:equal>
		<!-- Fin Mensaje de Reporte con Error -->
				
	<!-- Resultado Filtro -->
	<div id="resultadoFiltro">
		<logic:equal name="claComReportVO" property="existeReporteGenerado" value="true">
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
				<caption><bean:write name="claComReportVO" property="tituloReporte" /></caption>
               	<tbody>
	               	<tr>
						<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
						<th align="left"><bean:message bundle="bal" key="bal.claComReport.reporteGenerado"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.claComReport.ctdResultado"/></th>
					</tr>
					<logic:iterate id="ReporteGeneradoVO" name="claComReportVO" property="listReporteGenerado">
					<tr>
						<!-- Seleccionar -->
							<td>
								<a style="cursor: pointer; cursor: hand;" onclick="submitDownload('downloadFile', '<bean:write name="ReporteGeneradoVO" property="fileName" />');">
									<img title="<bean:message bundle="bal" key="bal.claComReport.button.verReporte"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
								</a>
							</td>
							<td><bean:write name="ReporteGeneradoVO" property="descripcion" />&nbsp;</td>
							<td><bean:write name="ReporteGeneradoVO" property="ctdResultadosView" />&nbsp;</td>
					</tr>
					</logic:iterate>
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
