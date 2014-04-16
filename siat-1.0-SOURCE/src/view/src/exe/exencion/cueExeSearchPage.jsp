<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/exe/BuscarCueExe.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	
	
	<logic:equal name="cueExeSearchPageVO" property="modoVer" value="true">
		<h1><bean:message bundle="exe" key="exe.cueExeSearchPage.modoVer.title"/></h1>	
			
		<table class="tablabotones" width="100%">
			<tr>
				<td align="left">
					<p><bean:message bundle="exe" key="exe.cueExeSearchPage.modoVer.legend"/></p>	
				</td>				
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>	
	
	  <fieldset>
		<legend><bean:message bundle="pad" key="pad.cuenta.label"/></legend>
			<table class="tabladatos">
				<tr>	
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal">
						<bean:write name="cueExeSearchPageVO" property="cueExe.cuenta.recurso.desRecurso"/>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
					<td class="normal">
						<bean:write name="cueExeSearchPageVO" property="cueExe.cuenta.numeroCuenta"/>
					</td>
				</tr>
			</table>
		</fieldset>
	</logic:equal>
	
	
	<logic:notEqual name="cueExeSearchPageVO" property="modoVer" value="true">
		<h1><bean:message bundle="exe" key="exe.cueExeSearchPage.title"/></h1>	
			
		<table class="tablabotones" width="100%">
			<tr>
				<td align="left">
					<p><bean:message bundle="exe" key="exe.cueExeSearchPage.legend"/></p>
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
					<td class="normal" colspan="3">
						<logic:equal name="cueExeSearchPageVO" property="disableCombo" value="true">
							<html:select name="cueExeSearchPageVO" property="cueExe.recurso.id" styleClass="select" disabled="true">
								<html:optionsCollection name="cueExeSearchPageVO" property="listRecurso" label="desRecurso" value="id" />
							</html:select>				
						</logic:equal>
						<logic:equal name="cueExeSearchPageVO" property="disableCombo" value="false">
							<html:select name="cueExeSearchPageVO" property="cueExe.recurso.id" styleClass="select" onchange="submitForm('paramRecurso', '');">
								<bean:define id="includeRecursoList" name="cueExeSearchPageVO" property="listRecurso"/>
								<bean:define id="includeIdRecursoSelected" name="cueExeSearchPageVO" property="cueExe.recurso.id"/>
								<%@ include file="/def/gravamen/includeRecurso.jsp" %>
							</html:select>
						</logic:equal>	
					</td>
				<tr>
				
				<tr>	
					<td><label><bean:message bundle="exe" key="exe.exencion.label"/>: </label></td>
					<td class="normal" colspan="3">
						<logic:equal name="cueExeSearchPageVO" property="disableCombo" value="true">
							<html:select name="cueExeSearchPageVO" property="cueExe.exencion.id" styleClass="select" disabled="true">
								<html:optionsCollection name="cueExeSearchPageVO" property="listExencion" label="desExencion" value="id" />
							</html:select>
						</logic:equal>
						<logic:equal name="cueExeSearchPageVO" property="disableCombo" value="false">
							<html:select name="cueExeSearchPageVO" property="cueExe.exencion.id" styleClass="select">
								<html:optionsCollection name="cueExeSearchPageVO" property="listExencion" label="desExencion" value="id" />
							</html:select>
						</logic:equal>	
					</td>
				</tr>
				
				<tr>
					<td><label><bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
					<td class="normal" colspan="3">
						<html:text name="cueExeSearchPageVO" property="cueExe.cuenta.numeroCuenta" size="20"/>
						<html:button property="btnBuscarCuenta"  styleClass="boton" onclick="submitForm('buscarCuenta', '');">
							<bean:message bundle="exe" key="exe.cueExeSearchPage.button.buscarCuenta"/>
						</html:button>
					</td>			
				</tr>
				
				<tr>
					<td><label><bean:message bundle="exe" key="exe.cueExe.solicDescripcion.label"/>: </label></td>
					<td class="normal" colspan="3">
						<html:text name="cueExeSearchPageVO" property="cueExe.solicDescripcion" size="20"/>
					</td>			
				</tr>
				
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal" colspan="3">
						<html:select name="cueExeSearchPageVO" property="cueExe.estadoCueExe.id" styleClass="select">
							<html:optionsCollection name="cueExeSearchPageVO" property="listEstadoCueExe" label="desEstadoCueExe" value="id" />
						</html:select>
					</td>
				</tr>
				
				
				<tr>
					<td><label>Estado en Historico: </label></td>
					<td class="normal" colspan="3">
						<html:select name="cueExeSearchPageVO" property="cueExe.hisEstCueExe.estadoCueExe.id" styleClass="select">
							<html:optionsCollection name="cueExeSearchPageVO" property="listEstadoCueExe" label="desEstadoCueExe" value="id" />
						</html:select>
						
						&nbsp; Incluir
						<html:radio name="cueExeSearchPageVO" property="estadoEnHistorico" value="true"/>
						&nbsp; Excluir
						<html:radio name="cueExeSearchPageVO" property="estadoEnHistorico" value="false"/>
					</td>
				</tr>
				
				<tr>
					<td><label><bean:message bundle="exe" key="exe.cueExe.fechaDesde.label"/>: </label></td>
					<td class="normal">
						<html:text name="cueExeSearchPageVO" property="cueExe.fechaDesdeView" styleId="fechaDesdeView" size="15" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
				
					<td><label><bean:message bundle="exe" key="exe.cueExe.fechaHasta.label"/>: </label></td>
					<td class="normal">
						<html:text name="cueExeSearchPageVO" property="cueExe.fechaHastaView" styleId="fechaHastaView" size="15" maxlength="10" styleClass="datos" />
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
			  	<html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('baseImprimirDetallado', '1');">
					<bean:message bundle="base" key="abm.button.imprimir.detallado"/>
				</html:button>
			  	&nbsp;
			  	<html:button property="btnBuscar"  styleClass="boton" onclick="submitForm('buscar', '');">
					<bean:message bundle="base" key="abm.button.buscar"/>
				</html:button>
			</p>
		</fieldset>	
		<!-- Fin Filtro -->
	</logic:notEqual>
		
	<!-- Resultado Filtro -->
	<div id="resultadoFiltro">
		<logic:equal name="cueExeSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="cueExeSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<logic:notEqual name="cueExeSearchPageVO" property="modoVer" value="true">
								<th width="1">&nbsp;</th> <!-- Eliminar -->							
								<th width="1">&nbsp;</th> <!-- convivientes -->
							</logic:notEqual>
							<th align="left"><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.ref"/></th>
							<th align="left"><bean:message bundle="exe" key="exe.cueExe.fechaDesde.label"/></th>
							<th align="left"><bean:message bundle="exe" key="exe.cueExe.fechaHasta.label"/></th>
							<th align="left"><bean:message bundle="exe" key="exe.exencion.label"/></th>
							<th align="left"><bean:message bundle="base" key="base.estado.label"/></th>
							<!-- <#ColumnTitles#> -->
						</tr>
							
						<logic:iterate id="CueExeVO" name="cueExeSearchPageVO" property="listResult">
							<tr>
								<!-- Seleccionar -->
								<logic:equal name="cueExeSearchPageVO" property="modoSeleccionar" value="true">
									<td>	
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="CueExeVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</td>
								</logic:equal>									
								<logic:notEqual name="cueExeSearchPageVO" property="modoSeleccionar" value="true">
									<!-- Ver -->
									<td>
										<logic:equal name="cueExeSearchPageVO" property="verEnabled" value="enabled">									
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="CueExeVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="CueExeVO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</td>
									
									<logic:notEqual name="cueExeSearchPageVO" property="modoVer" value="true">
										<!-- Eliminar-->								
										<td>
											<logic:equal name="cueExeSearchPageVO" property="eliminarEnabled" value="enabled">
												<logic:equal name="CueExeVO" property="eliminarEnabled" value="enabled">
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="CueExeVO" property="id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
													</a>
												</logic:equal>	
												<logic:notEqual name="CueExeVO" property="eliminarEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
												</logic:notEqual>
											</logic:equal>
											<logic:notEqual name="cueExeSearchPageVO" property="eliminarEnabled" value="enabled">										
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
											</logic:notEqual>
										</td>
									
										<td>
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verConvivientes', '<bean:write name="CueExeVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="exe" key="exe.cueExeSearchPage.button.admConvivientes"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/admSolCueExeConviv0.gif"/>
											</a>
										</td>
									</logic:notEqual>
								</logic:notEqual>
								
								<td><bean:write name="CueExeVO" property="cuenta.numeroCuenta" />&nbsp;</td>
								<td><bean:write name="CueExeVO" property="fechaDesdeView" />&nbsp;</td>
								<td><bean:write name="CueExeVO" property="fechaHastaView" />&nbsp;</td>
								<td><bean:write name="CueExeVO" property="exencion.desExencion" />&nbsp;</td>
								<td><bean:write name="CueExeVO" property="estadoCueExe.desEstadoCueExe" />&nbsp;</td>
								<!-- <#ColumnFiedls#> -->
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="cueExeSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="cueExeSearchPageVO" property="listResult">
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

	<table class="tablabotones" width="100%">
		<tr>
			<logic:equal name="cueExeSearchPageVO" property="modoVer" value="true">
				<td align="left"  width="30%">							
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<td align="left"  width="30%">
					<logic:equal name="cueExeSearchPageVO" property="administrarCueExeEnabled" value="true">
						<html:button property="btnImprimir"  styleClass="boton" onclick="submitForm('imprimirHistorico', '');">
							<bean:message bundle="exe" key="exe.cueExeSearchPage.button.imprimirInforme"/>
						</html:button>
					</logic:equal>
				</td>
				<td align="right"  width="30%">
					<logic:equal name="cueExeSearchPageVO" property="administrarCueExeEnabled" value="true">
						<input type="button" class="boton" 
							onClick="submitForm('administrarCueExe', '0');" 
							value="<bean:message bundle="exe" key="exe.cueExeSearchPage.button.administrar"/>"/>					
					</logic:equal>					
				</td>			
			</logic:equal>

			<logic:notEqual name="cueExeSearchPageVO" property="modoVer" value="true">
				<td align="left">
					<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
					&nbsp;
					<logic:equal name="cueExeSearchPageVO" property="viewResult" value="true">
	  	    			<logic:equal name="cueExeSearchPageVO" property="modoSeleccionar" value="false">
							<bean:define id="agregarEnabled" name="cueExeSearchPageVO" property="agregarEnabled"/>
							<input type="button" <%=agregarEnabled%> class="boton" 
								onClick="submitForm('agregar', '0');" 
								value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
						</logic:equal>
	  	    			<logic:equal name="cueExeSearchPageVO" property="modoSeleccionar" value="true">
	  	    				<logic:equal name="cueExeSearchPageVO" property="agregarEnSeleccion" value="true">
								<bean:define id="agregarEnabled" name="cueExeSearchPageVO" property="agregarEnabled"/>
								<input type="button" <%=agregarEnabled%> class="boton" 
									onClick="submitForm('agregar', '0');" 
									value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
								</logic:equal>
						</logic:equal>
					</logic:equal>
				</td>
			</logic:notEqual>
		</tr>
	</table>
	<input type="hidden" name="name"  value="<bean:write name='cueExeSearchPageVO' property='name'/>" id="name"/>
	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
	<input type="hidden" name="imprimirDetallado" value="1" id="imprimirDetallado"/>
	
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
