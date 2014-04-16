<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript">

function activarDesactivar() {
	var checks = document.getElementsByName('rangosFechaExtras');
	var mostrar = 'none';
	if(checks[0].checked){
		mostrar='';
	}
	var trs	 = document.getElementsByTagName('tr')
	for(i=0; i < trs.length; i++) {
		var tr = trs[i];
		if (tr.id != null){
			var id = tr.id;
			var idPatron = id.split("#")[0];
			if (idPatron == "fechaExtras") {
				tr.style.display = mostrar;
			} 
		}
	}
}

</script>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/ReporteTotalPar.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="bal" key="bal.totalParReport.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>
					<bean:message bundle="bal" key="bal.totalParReport.legend"/>
				</p>
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
	<legend><bean:message bundle="base" key="base.parametrosBusqueda"/></legend>
		<table class="tabladatos">
			<!-- fechaDesde y fechaHasta -->
			<tr>
				<td><label><bean:message bundle="bal" key="bal.totalParReport.fechaDesde.label"/>: </label></td>
				<td class="normal">
					<html:text name="totalParReportVO" property="fechaDesdeView" styleId="fechaDesdeView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
				<td ><label><bean:message bundle="bal" key="bal.totalParReport.fechaHasta.label"/>: </label></td>
				<td class="normal" >
					<html:text name="totalParReportVO" property="fechaHastaView" styleId="fechaHastaView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>				
			</tr>
			<!-- Partida -->
			<tr>
				<td><label><bean:message bundle="bal" key="bal.partida.label"/>: </label></td>
				<td class="normal" colspan="3">
					<logic:equal name="totalParReportVO" property="procesando" value="false">
						<html:select name="totalParReportVO" property="partida.id" styleClass="select" >
							<html:optionsCollection name="totalParReportVO" property="listPartida" label="desPartidaView" value="id" />
						</html:select>
					</logic:equal>
					<logic:equal name="totalParReportVO" property="procesando" value="true">
						<html:select name="totalParReportVO" property="partida.id" styleClass="select" disabled="true">
							<html:optionsCollection name="totalParReportVO" property="listPartida" label="desPartidaView" value="id" />
						</html:select>
					</logic:equal>
				</td>		
			</tr>
			<!-- Habilitar/Deshabilitar comparacion de multiples fechas -->
			<tr>
				<td><label><bean:message bundle="bal" key="bal.totalParReport.rangosFechaExtras.label"/>:</label></td>
				<td class="normal" >
					<a class="link_siat" onclick="activarDesactivar();" id="link_checkbox">
						<html:checkbox  name="totalParReportVO" property="rangosFechaExtras" />						
					</a>
				</td>
			</tr>
			<logic:equal name="totalParReportVO" property="rangosFechaExtras" value="false">
					<tr id="fechaExtras#1" style="display:none;">
				</logic:equal>
				<logic:equal name="totalParReportVO" property="rangosFechaExtras" value="true">
					<tr id="fechaExtras#1">
				</logic:equal>
					<td><label><bean:message bundle="bal" key="bal.totalParReport.cantRangos.label"/>: </label></td>
					<td class="normal" >
						<html:select name="totalParReportVO" property="cantRangos" styleClass="select" onchange="submitForm('paramRango', '');">
							<html:optionsCollection name="totalParReportVO" property="listRangos" label="valueView" value="value" />
						</html:select>
					</td>		
				</tr>
			<logic:notEmpty  name="totalParReportVO" property="listRangosFecha">	
				<logic:iterate id="RangoFechaVO" name="totalParReportVO" property="listRangosFecha">
					<logic:equal name="totalParReportVO" property="rangosFechaExtras" value="false">
						<tr id="fechaExtras#<bean:write name="RangoFechaVO" property="indice"/>" style="display:none;">
					</logic:equal>
					<logic:equal name="totalParReportVO" property="rangosFechaExtras" value="true">
						<tr id="fechaExtras#<bean:write name="RangoFechaVO" property="indice"/>">
					</logic:equal>
						<td><label><bean:message bundle="bal" key="bal.totalParReport.fechaDesde.label"/>: </label></td>
						<td class="normal">
							<input type="text" size="15" maxlength="10" name="fechaDesde<bean:write name="RangoFechaVO" property="indice"/>"
												value="<bean:write name="RangoFechaVO" property="fechaDesdeView"/>" id="fechaDesde<bean:write name="RangoFechaVO" property="indice"/>" class="datos" />
							<a class="link_siat" onclick="return show_calendar('fechaDesde<bean:write name="RangoFechaVO" property="indice"/>');" id="a_fechaDesde<bean:write name="RangoFechaVO" property="indice"/>">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>
						<td><label><bean:message bundle="bal" key="bal.totalParReport.fechaHasta.label"/>: </label></td>
						<td class="normal">
							<input type="text" size="15" maxlength="10" name="fechaHasta<bean:write name="RangoFechaVO" property="indice"/>"
												value="<bean:write name="RangoFechaVO" property="fechaHastaView"/>" id="fechaHasta<bean:write name="RangoFechaVO" property="indice"/>" class="datos" />
							<a class="link_siat" onclick="return show_calendar('fechaHasta<bean:write name="RangoFechaVO" property="indice"/>');" id="a_fechaHasta<bean:write name="RangoFechaVO" property="indice"/>">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>
					</tr>	
					</logic:iterate>
			</logic:notEmpty>
		</table>
			
		<p align="center">
			 <logic:equal name="totalParReportVO" property="procesando" value="false">
			  	<html:button property="btnLimpiar"  styleClass="boton" onclick="submitForm('limpiar', '');" disabled="false">
					<bean:message bundle="base" key="abm.button.limpiar"/>
				</html:button>
				&nbsp;
			  	<html:button property="btnBuscar"  styleClass="boton" onclick="submitForm('buscar', '');" disabled="false">
					<bean:message bundle="base" key="abm.button.buscar"/>
				</html:button>
			</logic:equal>
			 <logic:equal name="totalParReportVO" property="procesando" value="true">
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
	</fieldset>	
	<!-- Fin Filtro -->
	
	<!-- Mensaje de Reporte en Generacion -->
	<logic:equal name="totalParReportVO" property="procesando" value="true">
	<fieldset>
		<table class="tabladatos">
			<tr>
				<td class="normal">
					<bean:write name="totalParReportVO" property="desRunningRun"/>
					&nbsp;se encuentra procesando ...&nbsp;
					<img border="0" src="<%=request.getContextPath()%>/images/iconos/barraProcesando.gif"/>
				</td>
			</tr>
		</table>
	</fieldset>	
	</logic:equal>
	<!-- Fin Mensaje de Reporte en Generacion -->

	<!-- Mensaje de Reporte en con Error -->
	<logic:equal name="totalParReportVO" property="error" value="true">
	<fieldset>
		<table class="tabladatos">
			<tr>
				<td  class="normal"><bean:write name="totalParReportVO" property="desErrorRun"/> se proces&oacute; con error: </td>
			</tr>
			<tr>				
				<td class="normal">
					<bean:write name="totalParReportVO" property="estErrorRun"/>
				</td>
			</tr>
		</table>
	</fieldset>	
	</logic:equal>
	<!-- Fin Mensaje de Reporte con Error -->
		
	<!-- Resultado Filtro -->
	<div id="resultadoFiltro">
		<logic:equal name="totalParReportVO" property="existeReporteGenerado" value="true">
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
				<caption><bean:write name="totalParReportVO" property="tituloReporte" /></caption>
               	<tbody>
	               	<tr>
						<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
						<th align="left"><bean:message bundle="bal" key="bal.partida.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.totalParReport.total.label"/></th>
					</tr>
					<logic:iterate id="ReporteGeneradoVO" name="totalParReportVO" property="listReporteGenerado">
					<tr>
						<!-- Seleccionar -->
							<td>
								<a style="cursor: pointer; cursor: hand;" onclick="submitDownload('downloadFile', '<bean:write name="ReporteGeneradoVO" property="fileName" />');">
									<img title="<bean:message bundle="bal" key="bal.totalParReport.button.verReporte"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
								</a>
							</td>
							<td><bean:write name="ReporteGeneradoVO" property="titulo" />&nbsp;</td>
							<td><bean:write name="ReporteGeneradoVO" property="descripcion" />&nbsp;</td>
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
	<input type="hidden" name="name"  value="<bean:write name='totalParReportVO' property='name'/>" id="name"/>
   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
		
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