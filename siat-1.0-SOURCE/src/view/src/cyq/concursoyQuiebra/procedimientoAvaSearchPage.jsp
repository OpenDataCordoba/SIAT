<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/cyq/BuscarProcedimientoAva.do">

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
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.fechaAlta.label"/>: </label></td>
				<td class="normal">
					<html:text name="procedimientoSearchPageVO" property="procedimiento.fechaAltaView" styleId="fechaAltaView" size="12" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaAltaView');" id="a_fechaAltaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>

				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.fechaVerOpo.label"/>: </label></td>
				<td class="normal">
					<html:text name="procedimientoSearchPageVO" property="procedimiento.fechaVerOpoView" styleId="fechaVerOpoView" size="12" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaVerOpoView');" id="a_fechaVerOpoView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
			<tr>
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.numero.label"/>: </label></td>
				<td class="normal"><html:text name="procedimientoSearchPageVO" property="procedimiento.numeroView" size="15" maxlength="100"/></td>			

				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.anio.label"/>: </label></td>
				<td class="normal"><html:text name="procedimientoSearchPageVO" property="procedimiento.anioView" size="15" maxlength="100"/></td>			
			</tr>
			
			<tr>
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.numExp.label"/>: </label></td>
				<td class="normal"><html:text name="procedimientoSearchPageVO" property="procedimiento.numExpView" size="15" maxlength="100" styleClass="datos" /></td>
				
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.perOpoDeu.label"/>: </label></td>
				<td class="normal"><html:text name="procedimientoSearchPageVO" property="procedimiento.perOpoDeu" size="15" maxlength="100" styleClass="datos" /></td>				
			</tr>
			
			<tr>
				<td><label><bean:message bundle="cyq" key="cyq.procedimiento.desContribuyente.label"/>: </label></td>
				<td class="normal" colspan="2"><html:text name="procedimientoSearchPageVO" property="procedimiento.desContribuyente" size="20" maxlength="100" styleClass="datos" /></td>				
			</tr>
			<tr>
				<td><label><bean:message bundle="pad" key="pad.contribuyente.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:text name="procedimientoSearchPageVO" property="procedimiento.contribuyente.view" size="20" maxlength="100" styleClass="datos"  disabled="true"/>
					<html:button property="btnBucarTitular" styleClass="boton" onclick="submitForm('buscarTitular', '');">
						<bean:message bundle="pad" key="pad.cuentaSearchPage.button.buscarTitular"/>						
					</html:button>
				</td>
			</tr>
			<tr>	
				<td><label><bean:message bundle="cyq" key="cyq.tipoProceso.label"/>: </label></td>
				<td class="normal" colspan="2">
					<html:select name="procedimientoSearchPageVO" property="procedimiento.tipoProceso.id" styleClass="select">
						<html:optionsCollection name="procedimientoSearchPageVO" property="listTipoProceso" label="desTipoProceso" value="id" />
					</html:select>
				</td>					
			</tr>
			<tr>	
				<td><label><bean:message bundle="cyq" key="cyq.juzgado.label"/>: </label></td>
				<td class="normal" colspan="2">
					<html:select name="procedimientoSearchPageVO" property="procedimiento.juzgado.id" styleClass="select" onchange="submitForm('paramJuzgado', '');">
						<html:optionsCollection name="procedimientoSearchPageVO" property="listJuzgado" label="desJuzgado" value="id" />
					</html:select>
				</td>					
			</tr>
			<tr>	
				<td><label><bean:message bundle="cyq" key="cyq.abogado.label"/>: </label></td>
				<td class="normal" colspan="2">
					<html:select name="procedimientoSearchPageVO" property="procedimiento.abogado.id" styleClass="select">
						<html:optionsCollection name="procedimientoSearchPageVO" property="listAbogado" label="descripcion" value="id" />
					</html:select>
				</td>					
			</tr>
			
			<tr>
				<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="procedimientoSearchPageVO" property="procedimiento.estadoProced.id" styleClass="select">
						<html:optionsCollection name="procedimientoSearchPageVO" property="listEstadoProced" label="desEstadoProced" value="id" />
					</html:select>
				</td>
			</tr>
			
			<tr>
				<td><label>Estado en Historico: </label></td>
				<td class="normal" colspan="3">
					<html:select name="procedimientoSearchPageVO" property="procedimiento.hisEstProced.estadoProced.id" styleClass="select">
						<html:optionsCollection name="procedimientoSearchPageVO" property="listEstadoProced" label="desEstadoProced" value="id" />
					</html:select>
					
					&nbsp; Incluir
					<html:radio name="procedimientoSearchPageVO" property="estadoEnHistorico" value="true"/>
					&nbsp; Excluir
					<html:radio name="procedimientoSearchPageVO" property="estadoEnHistorico" value="false"/>
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
								<th width="1">&nbsp;</th> <!-- Eliminar -->
								<!-- th wi dth="1">&nbsp;< / th> <  Cambiar Estado -->
								<th width="1">&nbsp;</th> <!-- LiquidacionDeudaCyq -->
							</logic:notEqual>
							<!-- <#ColumnTitles#> -->
							<th align="left">
								<bean:message bundle="cyq" key="cyq.procedimiento.numero.abrev"/>/
								<bean:message bundle="cyq" key="cyq.procedimiento.anio.label"/>
							</th>
							<th align="left"><bean:message bundle="cyq" key="cyq.procedimiento.desContribuyente.label"/></th>
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
	
									<!-- Eliminar-->								
									<td>
										<logic:equal name="procedimientoSearchPageVO" property="eliminarEnabled" value="enabled">
											<logic:equal name="ProcedimientoVO" property="eliminarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="ProcedimientoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>
											</logic:equal>	
											<logic:notEqual name="ProcedimientoVO" property="eliminarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="procedimientoSearchPageVO" property="eliminarEnabled" value="enabled">										
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</td>
									<!-- td>
										<log ic:e qual name="procedimientoSearchPageVO" property="cambiarEstadoEnabled" value="enabled">
											<log ic:e qual name="ProcedimientoVO" property="cambiarEstadoBussEnabled" value="true">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('cambiarEstado', '<bea n:w rite name="ProcedimientoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bea n:me ssage bundle="base" key="abm.button.cambiarEstado"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/cambiarEstado0.gif"/>
												</a>
											</log ic:equal>	
											<logi c:no tEqual name="ProcedimientoVO" property="cambiarEstadoBussEnabled" value="true">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/cambiarEstado1.gif"/>
											</lo gic:n otEqual>
										</lo gic:eq ual>
										<log ic:not Equal name="procedimientoSearchPageVO" property="cambiarEstadoEnabled" value="enabled">										
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/cambiarEstado1.gif"/>
										</log ic:not Equal>
									</td -->
									
									<!-- Liquidacion deuda -->
									<td>
										<logic:equal name="procedimientoSearchPageVO" property="liquidacionDeudaEnabled" value="enabled">
											<logic:equal name="ProcedimientoVO" property="liquidacionDeudaBussEnabled" value="true">
												<a style="cursor: pointer; cursor: hand;" 
													onclick="submitForm('liqDeudaCyq','<bean:write name="ProcedimientoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="gde" key="gde.button.liquidacionDeuda"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/liquidacionDeuda0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="ProcedimientoVO" property="liquidacionDeudaBussEnabled" value="true">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/liquidacionDeuda1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="procedimientoSearchPageVO" property="liquidacionDeudaEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/liquidacionDeuda1.gif"/>
										</logic:notEqual>
									</td>
									
								</logic:notEqual>
								<!-- <#ColumnFiedls#> -->
								
								<td nowrap="nowrap">
									<bean:write name="ProcedimientoVO" property="numeroView"/>/<bean:write name="ProcedimientoVO" property="anioView"/>&nbsp;
								</td>
								<td><bean:write name="ProcedimientoVO" property="desContribuyente"/>&nbsp;</td>
								<td><bean:write name="ProcedimientoVO" property="tipoProceso.desTipoProcesoAbrev"/>&nbsp;</td>
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
			<logic:equal name="procedimientoSearchPageVO" property="viewResult" value="true">
  	    		<td align="right">
  	    			<logic:equal name="procedimientoSearchPageVO" property="modoSeleccionar" value="false">
						<bean:define id="agregarEnabled" name="procedimientoSearchPageVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar', '0');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
					</logic:equal>
  	    			<logic:equal name="procedimientoSearchPageVO" property="modoSeleccionar" value="true">
  	    				<logic:equal name="procedimientoSearchPageVO" property="agregarEnSeleccion" value="true">
							<bean:define id="agregarEnabled" name="procedimientoSearchPageVO" property="agregarEnabled"/>
							<input type="button" <%=agregarEnabled%> class="boton" 
								onClick="submitForm('agregar', '0');" 
								value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
							</logic:equal>
					</logic:equal>
				</td>
			</logic:equal>
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
