<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/cyq/BuscarProcedimiento.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="cyq" key="cyq.procedimientoSearchPage.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>
					<logic:equal name="procedimientoSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="base" key="base.busquedaLegendBusqueda"/>
						<bean:message bundle="cyq" key="cyq.procedimiento.label"/>
					</logic:equal>
					<logic:notEqual name="procedimientoSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="cyq" key="cyq.procedimientoSearchPage.legend"/>
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
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.numero.label"/>: </label></td>
				<td class="normal"><html:text name="procedimientoSearchPageVO" property="procedimiento.numeroView" size="20" maxlength="100"/></td>			

				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.anio.label"/>: </label></td>
				<td class="normal"><html:text name="procedimientoSearchPageVO" property="procedimiento.anioView" size="20" maxlength="100"/></td>			
			</tr>
			
			<tr>
				<td><label><bean:message bundle="cyq" key="cyq.procedimientoSearchPage.fechaVerOpoDesde.label"/>: </label></td>
				<td class="normal">
					<html:text name="procedimientoSearchPageVO" property="fechaVerOpoDesdeView" styleId="fechaVerOpoDesdeView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaVerOpoDesdeView');" id="a_fechaVerOpoDesdeView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
				<td><label><bean:message bundle="cyq" key="cyq.procedimientoSearchPage.fechaVerOpoHasta.label"/>: </label></td>
				<td class="normal">
					<html:text name="procedimientoSearchPageVO" property="fechaVerOpoHastaView" styleId="fechaVerOpoHastaView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaVerOpoHastaView');" id="a_fechaVerOpoHastaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
			<tr>	
				<td><label><bean:message bundle="cyq" key="cyq.procedimientoSearchPage.poseeOrdenControl.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="procedimientoSearchPageVO" property="poseeOrdenControl.id" styleClass="select">
						<html:optionsCollection name="procedimientoSearchPageVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>					
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
		<logic:equal name="procedimientoSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="procedimientoSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<logic:notEqual name="procedimientoSearchPageVO" property="modoSeleccionar" value="true">
								<th width="1">&nbsp;</th> <!-- Modificar -->
								<!-- th width="1 >&nbsp;</th> < Eliminar -->
								<!-- th wi dth="1" >&nbsp;</th> <  Cambiar Estado -->
							</logic:notEqual>
							<!-- <#ColumnTitles#> -->
							<th align="left">
								<bean:message bundle="cyq" key="cyq.procedimiento.numero.abrev"/>/
								<bean:message bundle="cyq" key="cyq.procedimiento.anio.label"/>
							</th>
							<th align="left"><bean:message bundle="cyq" key="cyq.tipoProceso.label"/></th>
							<th align="left"><bean:message bundle="cyq" key="cyq.procedimiento.fechaVerOpo.label"/></th>
							<th align="left"><bean:message bundle="cyq" key="cyq.procedimiento.observacion.label"/></th>
						</tr>
							
						<logic:iterate id="ProcedimientoVO" name="procedimientoSearchPageVO" property="listResult">
							<tr>
								<!-- Seleccionar -->
								<logic:equal name="procedimientoSearchPageVO" property="modoSeleccionar" value="true">
									<td>	
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="ProcedimientoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</td>
								</logic:equal>									
								<logic:notEqual name="procedimientoSearchPageVO" property="modoSeleccionar" value="true">
									<!-- Ver -->
									<td>
										<logic:equal name="procedimientoSearchPageVO" property="verEnabled" value="enabled">									
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="ProcedimientoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="ProcedimientoVO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</td>	
									<!-- Modificar-->								
									<td>
										<logic:equal name="procedimientoSearchPageVO" property="modificarEnabled" value="enabled">
											<logic:equal name="ProcedimientoVO" property="modificarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="ProcedimientoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="ProcedimientoVO" property="modificarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="procedimientoSearchPageVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</td>
	
									<!-- Eliminar>								
									<td>
										<logi c:e qual name="procedimientoSearchPageVO" property="eliminarEnabled" value="enabled">
											<logi c:eq ual name="ProcedimientoVO" property="eliminarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<b ean:w rite name="ProcedimientoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<b ean:m essage bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>
											</log ic:e qual>	
											<log ic:no tEqual name="ProcedimientoVO" property="eliminarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
											</log ic:no tEqual>
										</lo gic:e qual>
										<lo ic:no Equal name="procedimientoSearchPageVO" property="eliminarEnabled" value="enabled">										
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</log ic:n otEqual>
									</td>
									< td>
										<log ic:eq ual name="procedimientoSearchPageVO" property="cambiarEstadoEnabled" value="enabled">
											<log c:e ual name="ProcedimientoVO" property="cambiarEstadoBussEnabled" value="true">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('cambiarEstado', '<bea n:w rite name="ProcedimientoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bea n:m essage bundle="base" key="abm.button.cambiarEstado"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/cambiarEstado0.gif"/>
												</a>
											</lo gic:e qual>	
											<log ic:notE ual name="ProcedimientoVO" property="cambiarEstadoBussEnabled" value="true">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/cambiarEstado1.gif"/>
											</lo gic:n otEqual>
										</l gic:e qual>
										<lo ic:no tEqual name="procedimientoSearchPageVO" property="cambiarEstadoEnabled" value="enabled">										
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/cambiarEstado1.gif"/>
										</log ic:no tEqual>
									</td-->
								</logic:notEqual>
								<!-- <#ColumnFiedls#> -->
								
								<td nowrap="nowrap">
									<bean:write name="ProcedimientoVO" property="numeroView"/>/<bean:write name="ProcedimientoVO" property="anioView"/>&nbsp;
								</td>
								<td><bean:write name="ProcedimientoVO" property="tipoProceso.desTipoProceso"/>&nbsp;</td>
								<td><bean:write name="ProcedimientoVO" property="fechaVerOpoView"/>&nbsp;</td>
								<td><bean:write name="ProcedimientoVO" property="observacion"/>&nbsp;</td>
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="procedimientoSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="procedimientoSearchPageVO" property="listResult">
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
<!-- procedimientoSearchPage.jsp -->
