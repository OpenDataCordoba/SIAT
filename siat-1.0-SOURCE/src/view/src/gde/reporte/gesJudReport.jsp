<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/ReporteGesJud.do" >

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>

	<!-- Busqueda de Recuso -->
	<span id="blockBusqueda" style="display:none"> 
		<bean:define id="adapterVO" name="gesJudReportVO"/>
		<bean:define id="poseeParam" value="true" />
		<%@ include file="/def/gravamen/includeRecursoSearch.jsp" %>
	</span>

	<span id="blockSimple" style="display:block">
			
		<h1><bean:message bundle="gde" key="gde.gesJudReport.title"/></h1>	
			
		<p><bean:message bundle="gde" key="gde.gesJudReport.legend"/></p>
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
						<html:select name="gesJudReportVO" property="cuenta.recurso.id" styleClass="select" onchange="submitForm('paramRecurso', '')" styleId="cboRecurso" style="width:90%">
							<bean:define id="includeRecursoList"       name="gesJudReportVO" property="listRecurso"/>
							<bean:define id="includeIdRecursoSelected" name="gesJudReportVO" property="cuenta.recurso.id"/>
							<%@ include file="/def/gravamen/includeRecurso.jsp" %>
						</html:select>
						
						<a onclick="toggleSearchRecurso('blockSimple', 'blockBusqueda'); setFocus('recursoFilter');" >
							<img title='<bean:message bundle="def" key="def.recurso.filtro.button.busqueda"/>' border="0" 
							src="<%=request.getContextPath()%>/images/iconos/lupita.gif" width="20" height="20">
						</a>
						
					</td>
				</tr>
				
				<!-- Procurador -->
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.procurador.label"/>: </label></td>
					<td class="normal" colspan="3">
						<html:select name="gesJudReportVO" property="gesJud.procurador.id" styleClass="select">
							<html:optionsCollection name="gesJudReportVO" property="listProcurador" label="descripcion" value="id"/>
						</html:select>
					</td>
				</tr>
	
				<!-- Cuenta -->
				<tr>
					<td><label><bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
					<td class="normal" colspan="3">
							<html:text name="gesJudReportVO" property="cuenta.numeroCuenta" size="20"/>
							<html:button property="btnBuscarCuenta"  styleClass="boton" onclick="submitForm('buscarCuenta', '');">
								<bean:message bundle="gde" key="gde.gesJudReportVO.button.buscarCuenta"/>
							</html:button>									
					</td>			
				</tr>
						
				<!-- Tipo Juzgado -->			
				<tr>
					<td><label><bean:message bundle="gde" key="gde.gesJud.tipoJuzgado.label"/>: </label></td>
					<td class="normal" colspan="3">
						<html:select name="gesJudReportVO" property="gesJud.tipoJuzgado.codTipoJuzgado" styleClass="select">
							<html:optionsCollection name="gesJudReportVO" property="listTipoJuzgado" label="desTipoJuzgado" value="codTipoJuzgado"/>
						</html:select>
					</td>
				</tr>
					
				<!-- fechaDesde y fechaHasta -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.gesJudReport.fechaDesde.label"/>: </label></td>
					<td class="normal">
						<html:text name="gesJudReportVO" property="fechaDesdeView" styleId="fechaDesdeView" size="15" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
					<td><label><bean:message bundle="gde" key="gde.gesJudReport.fechaHasta.label"/>: </label></td>
					<td class="normal">
						<html:text name="gesJudReportVO" property="fechaHastaView" styleId="fechaHastaView" size="15" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>				
				</tr>
				
				<!-- Eventos -->
				<tr>
					<td colspan="4">
								
					</td>
				</tr>
				
			</table>
			
			<div id="eventos" class="scrolable" style="height: 400px;">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="500">
					<caption><bean:message bundle="gde" key="gde.gesJudReport.listaEventos.label"/></caption>
	            				<tbody>
		               	<tr>
							<th><bean:message bundle="gde" key="gde.evento.codigo.label"/></th>
							<th><bean:message bundle="gde" key="gde.evento.descripcion.label"/></th>
							<th><bean:message bundle="gde" key="gde.gesJudReport.OperacionEvento.label"/></th>
							<th><bean:message bundle="gde" key="gde.gesJudReport.tiempoEvento.label"/></th>
						</tr>
						<logic:iterate id="EventoVO" name="gesJudReportVO" property="listEvento">								
							<tr>
								<td width="30"><bean:write name="EventoVO" property="codigoView"/></td>
								<td width="170"><bean:write name="EventoVO" property="descripcion"/></td>
								<td width="140">
									<bean:define id="idEventoSelected" name="EventoVO" property="idView"/>
									<select name="<bean:write name="idEventoSelected"/>_operacion">
										<option value="0">Seleccionar...</option>
										<option value="1">Que exista</option>
										<option value="2">Que exista y sea el último</option>
										<option value="3">Que NO exista</option>												
									</select>
								</td>											
								<td width="160">
									<select name="<bean:write name="idEventoSelected"/>_opTiempo">
										<option value="0">&gt;</option>
										<option value="1">&lt;</option>
										<option value="2">=</option>												
									</select>							
									<input type="text" name="<bean:write name="idEventoSelected"/>_tiempo"
									  maxlength="5" size="5"/>
									<select name="<bean:write name="idEventoSelected"/>_unidad">
										<option value="0">Día</option>
										<option value="1">Mes</option>
										<option value="2">Año</option>												
									</select>												
								</td>
							</tr>
						</logic:iterate>
					</tbody>
				</table>
					
			</div>
							
			<p align="center">
			<logic:equal name="gesJudReportVO" property="procesando" value="false">
			  	<html:button property="btnLimpiar"  styleClass="boton" onclick="submitForm('limpiar', '');" disabled="false">
					<bean:message bundle="base" key="abm.button.limpiar"/>
				</html:button>
				&nbsp;
			  	<html:button property="btnBuscar"  styleClass="boton" onclick="submitForm('buscar', '');" disabled="false">
					<bean:message bundle="base" key="abm.button.buscar"/>
				</html:button>
			</logic:equal>
			<logic:equal name="gesJudReportVO" property="procesando" value="true">
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
		<logic:equal name="gesJudReportVO" property="procesando" value="true">
		<fieldset>
			<table class="tabladatos">
				<tr>
					<td class="normal">
						<bean:write name="gesJudReportVO" property="desRunningRun"/>
						&nbsp;se encuentra procesando ...&nbsp;
						<img border="0" src="<%=request.getContextPath()%>/images/iconos/barraProcesando.gif"/>
					</td>
				</tr>
			</table>
		</fieldset>	
		</logic:equal>
		<!-- Fin Mensaje de Reporte en Generacion -->
	
		<!-- Mensaje de Reporte en con Error -->
		<logic:equal name="gesJudReportVO" property="error" value="true">
		<fieldset>
			<table class="tabladatos">
				<tr>
					<td class="normal"><bean:write name="gesJudReportVO" property="desErrorRun"/> se proces&oacute; con error: </td>
				</tr>
				<tr>
					<td class="normal">
						<bean:write name="gesJudReportVO" property="estErrorRun"/>
					</td>
				</tr>
			</table>
		</fieldset>	
		</logic:equal>
		<!-- Fin Mensaje de Reporte con Error -->
			
		<!-- Resultado Filtro -->
		<div id="resultadoFiltro">
			<logic:equal name="gesJudReportVO" property="existeReporteGenerado" value="true">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:write name="gesJudReportVO" property="reporteGenerado.titulo" /></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<th align="left"><bean:message bundle="gde" key="gde.gesJudReporte.reporteGenerado"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.gesJudReporte.ctdResultado"/></th>
						</tr>
						<tr>
							<!-- Seleccionar -->
								<td>
									<a style="cursor: pointer; cursor: hand;" onclick="submitDownload('downloadFile', '<bean:write name="gesJudReportVO" property="reporteGenerado.fileName" />');">
										<img title="<bean:message bundle="gde" key="gde.gesJudReporte.button.verReporte"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</td>
								<td><bean:write name="gesJudReportVO" property="reporteGenerado.descripcion" />&nbsp;</td>
								<td><bean:write name="gesJudReportVO" property="reporteGenerado.ctdResultadosView" />&nbsp;</td>
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
	
	</span>
	
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