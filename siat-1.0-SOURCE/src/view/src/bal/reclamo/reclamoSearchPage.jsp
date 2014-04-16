<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/BuscarReclamo.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="bal" key="bal.reclamoSearchPage.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>
					<logic:equal name="reclamoSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="base" key="base.busquedaLegendBusqueda"/>
						<bean:message bundle="bal" key="bal.reclamo.label"/>
					</logic:equal>
					<logic:notEqual name="reclamoSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="bal" key="bal.reclamoSearchPage.legend"/>
					</logic:notEqual>		
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
		<tr>	
			<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td colspan="4" class="normal">
					<html:select name="reclamoSearchPageVO" property="reclamo.recurso.id" styleClass="select" >
						<bean:define id="includeRecursoList" name="reclamoSearchPageVO" property="listRecurso"/>
						<bean:define id="includeIdRecursoSelected" name="reclamoSearchPageVO" property="reclamo.recurso.id"/>
						<%@ include file="/def/gravamen/includeRecurso.jsp" %>
					</html:select>
			</td>
		</tr>
		<tr>	
			<td><label><bean:message bundle="bal" key="bal.reclamo.cuenta.label"/>: </label></td>
			<td class="normal"><html:text name="reclamoSearchPageVO" property="reclamo.nroCuenta" size="20" maxlength="100" styleClass="datos" /></td>
		</tr>
		<tr>	
			<td><label><bean:message bundle="bal" key="bal.reclamo.estadoReclamo.label"/>: </label></td>
			<td class="normal">
				<html:select name="reclamoSearchPageVO" property="reclamo.estadoReclamo.id" styleClass="select">
					<html:optionsCollection name="reclamoSearchPageVO" property="listEstadoReclamo" label="desEstadoReclamo" value="id" />
				</html:select>
			</td>
			<td><label><bean:message bundle="bal" key="bal.reclamo.nroReclamo.label"/>: </label></td>
			<td class="normal"><html:text name="reclamoSearchPageVO" property="reclamo.id" size="20" maxlength="20" styleClass="datos"/></td>
		</tr>
		<tr>
			<td><label><bean:message bundle="bal" key="bal.reclamo.fechaDesde.label"/>: </label></td>
			<td class="normal">
				<html:text name="reclamoSearchPageVO" property="fechaDesdeView" styleId="fechaDesdeView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>
			<td><label><bean:message bundle="bal" key="bal.reclamo.fechaHasta.label"/>: </label></td>
			<td class="normal">
				<html:text name="reclamoSearchPageVO" property="fechaHastaView" styleId="fechaHastaView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>
		</tr>
		
			<!-- <#Filtros#> -->
		</table>
			
		<p align="center">
		  	<html:button property="btnLimpiar"  styleClass="boton" onclick="submitForm('limpiar', '');">
				<bean:message bundle="base" key="abm.button.limpiar"/>
			</html:button>
			&nbsp;
			<html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('baseImprimir', '1');">
				<bean:message bundle="base" key="abm.button.imprimir"/>
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
		<logic:equal name="reclamoSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="reclamoSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<logic:notEqual name="reclamoSearchPageVO" property="modoSeleccionar" value="true">
								<th width="1">&nbsp;</th> <!-- Modificar -->
							</logic:notEqual>
							<!-- <#ColumnTitles#> -->
							<th align="left">Nro. - <bean:message bundle="bal" key="bal.reclamo.fechaAlta.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.reclamo.cuenta.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.reclamo.tipoBoleta.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.reclamo.estadoReclamo.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.reclamo.observacion.label"/></th>

						</tr>
							
						<logic:iterate id="ReclamoVO" name="reclamoSearchPageVO" property="listResult">
							<tr>
								<!-- Seleccionar -->
								<logic:equal name="reclamoSearchPageVO" property="modoSeleccionar" value="true">
									<td>	
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="ReclamoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</td>
								</logic:equal>									
								<logic:notEqual name="reclamoSearchPageVO" property="modoSeleccionar" value="true">
									<!-- Ver -->
									<td>
										<logic:equal name="reclamoSearchPageVO" property="verEnabled" value="enabled">									
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="ReclamoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="ReclamoVO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</td>	
									<!-- Modificar-->								
									<td>
										<logic:equal name="reclamoSearchPageVO" property="modificarEnabled" value="enabled">
											<logic:equal name="ReclamoVO" property="modificarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="ReclamoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="ReclamoVO" property="modificarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="reclamoSearchPageVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</td>
	
								</logic:notEqual>
								<!-- <#ColumnFiedls#> -->
								<td><bean:write name="ReclamoVO" property="id" bundle="base" formatKey="general.format.id"/>-<bean:write name="ReclamoVO" property="fechaAltaView"/>&nbsp;</td>
								<td><bean:write name="ReclamoVO" property="recurso.codRecurso" /> - <bean:write name="ReclamoVO" property="nroCuentaView" />&nbsp;</td>
								<td><bean:write name="ReclamoVO" property="desTipoBoleta" />&nbsp;</td>
								<td><bean:write name="ReclamoVO" property="estadoReclamo.desEstadoReclamo" />&nbsp;</td>			
								<td><bean:write name="ReclamoVO" property="observacion" />&nbsp;</td>

							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="reclamoSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="reclamoSearchPageVO" property="listResult">
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
    
    <input type="hidden" name="name"  value="<bean:write name='reclamoSearchPageVO' property='name'/>" id="name"/>
	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
	
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
