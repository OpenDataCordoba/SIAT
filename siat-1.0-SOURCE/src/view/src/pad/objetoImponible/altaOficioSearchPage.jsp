<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/pad/BuscarAltaOficio.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="pad" key="pad.altaOficioSearchPage.title"/></h1>	
		
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>
					<logic:equal name="altaOficioSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="base" key="base.busquedaLegendBusqueda"/>
						<bean:message bundle="pad" key="pad.altaOficio.label"/>
					</logic:equal>
					<logic:notEqual name="altaOficioSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="pad" key="pad.altaOficioSearchPage.legend"/>
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
		
		<!-- Cuenta -->
		<tr>
			<td><label><bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
			<td class="normal" colspan="3">
				<html:text name="altaOficioSearchPageVO" property="cuenta.numeroCuenta" size="20" maxlength="100"/>
				<html:button property="btnBuscarCuenta" styleClass="boton" onclick="submitForm('buscarCuenta', '');">
					<bean:message bundle="pad" key="pad.altaOficioSearchPage.button.buscarCuenta"/>
				</html:button>			
			</td>
			
		</tr>
		
		<!-- Nro comercio -->		
		<tr>
			<td><label><bean:message bundle="pad" key="pad.altaOficio.nroComercio.label"/>: </label></td>
			<td class="normal" colspan="3"><html:text name="altaOficioSearchPageVO" property="nroComercio" size="20" maxlength="100"/></td>
		</tr>
		
		<!-- Estado -->		
		<tr>				
			<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
			<td class="normal" colspan="3">
				<html:select name="altaOficioSearchPageVO" property="idEstadoSel" styleClass="select">
					<html:option value="-1">Seleccionar...</html:option>
					<html:optionsCollection name="altaOficioSearchPageVO" property="listEstado" label="value" value="id" />
				</html:select>
			</td>
		</tr>
		
			 					
		<tr>
			<td><label><bean:message bundle="pad" key="pad.altaOficioSearchPage.fechaDesde.label"/>: </label></td>
			<td class="normal">
				<html:text name="altaOficioSearchPageVO" property="fechaDesdeView" styleId="fechaDesdeView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>
			<td><label><bean:message bundle="pad" key="pad.altaOficioSearchPage.fechaHasta.label"/>: </label></td>
			<td class="normal">
				<html:text name="altaOficioSearchPageVO" property="fechaHastaView" styleId="fechaHastaView" size="15" maxlength="10" styleClass="datos" />
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
		<logic:equal name="altaOficioSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="altaOficioSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<logic:notEqual name="altaOficioSearchPageVO" property="modoSeleccionar" value="true">
								<th width="1">&nbsp;</th> <!-- Modificar -->
								<th width="1">&nbsp;</th> <!-- Eliminar -->
								<th width="1">&nbsp;</th> <!-- Activar Desactivar -->
								<th><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.ref"/></th>
								<th><bean:message bundle="pad" key="pad.contribuyente.label"/></th>
								<th><bean:message bundle="pad" key="pad.altaOficio.nroComercio.label"/></th>
								<th><bean:message bundle="pad" key="pad.altaOficio.comercio.fechaAlta.label"/></th>
								<th><bean:message bundle="base" key="base.estado.label"/></th>
							</logic:notEqual>
							<!-- <#ColumnTitles#> -->
						</tr>
							
						<logic:iterate id="cuentaVO" name="altaOficioSearchPageVO" property="listResult">
							<tr>
								<!-- Seleccionar -->
								<logic:equal name="altaOficioSearchPageVO" property="modoSeleccionar" value="true">
									<td>	
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="cuentaVO" property="objImp.id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</td>
								</logic:equal>									
								<logic:notEqual name="altaOficioSearchPageVO" property="modoSeleccionar" value="true">
									<!-- Ver -->
									<td>
										<logic:equal name="altaOficioSearchPageVO" property="verEnabled" value="enabled">									
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="cuentaVO" property="objImp.id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="altaOficioSearchPageVO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</td>	
									<!-- Modificar-->								
									<td>
										<logic:equal name="altaOficioSearchPageVO" property="modificarEnabled" value="enabled">
											<logic:equal name="cuentaVO" property="modificarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="cuentaVO" property="objImp.id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="cuentaVO" property="modificarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="altaOficioSearchPageVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</td>
	
									<!-- Eliminar-->								
									<td>
										<logic:equal name="altaOficioSearchPageVO" property="eliminarEnabled" value="enabled">
											<logic:equal name="cuentaVO" property="eliminarBussEnabled" value="true">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="cuentaVO" property="objImp.id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>
											</logic:equal>	
											<logic:notEqual name="cuentaVO" property="eliminarBussEnabled" value="true">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="altaOficioSearchPageVO" property="eliminarEnabled" value="enabled">										
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</td>
									<td>
										<!-- Activar -->
										<logic:equal name="cuentaVO" property="estado.id" value="0">
											<logic:equal name="altaOficioSearchPageVO" property="activarEnabled" value="enabled">
												<logic:equal name="cuentaVO" property="activarBussEnabled" value="true">
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('activar', '<bean:write name="cuentaVO" property="objImp.id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="base" key="abm.button.activar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/activar0.gif"/>
													</a>
												</logic:equal> 
												<logic:notEqual name="cuentaVO" property="activarBussEnabled" value="true">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/activar1.gif"/>
												</logic:notEqual>
											</logic:equal>
											<logic:notEqual name="altaOficioSearchPageVO" property="activarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/activar1.gif"/>
											</logic:notEqual>
										</logic:equal> 
										<!-- Desactivar -->
										<logic:equal name="cuentaVO" property="estado.id" value="1">
											<logic:equal name="altaOficioSearchPageVO" property="desactivarEnabled" value="enabled">
												<logic:equal name="cuentaVO" property="desactivarBussEnabled" value="true">
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('desactivar', '<bean:write name="cuentaVO" property="objImp.id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="base" key="abm.button.desactivar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar0.gif"/>
													</a>
												</logic:equal>
												<logic:notEqual name="cuentaVO" property="desactivarBussEnabled" value="true">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar1.gif"/>
												</logic:notEqual>
											</logic:equal>
											<logic:notEqual name="altaOficioSearchPageVO" property="desactivarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar1.gif"/>
											</logic:notEqual>										
										</logic:equal>
										<!-- En estado creado -->
										<logic:equal name="cuentaVO" property="estado.id" value="-1">
											<a style="cursor: pointer; cursor: hand;">
											<img border="0" title="<bean:message bundle="base" key="abm.button.creado"/>" src="<%=request.getContextPath()%>/images/iconos/creado0.gif"/>
											</a>
										</logic:equal> 
									</td>
								</logic:notEqual>
								<td><bean:write name="cuentaVO" property="numeroCuenta" />&nbsp;</td>
								<td><bean:write name="cuentaVO" property="nombreTitularPrincipal"/>&nbsp;</td>
								<td><bean:write name="cuentaVO" property="objImp.claveFuncional" />&nbsp;</td>
								<td><bean:write name="cuentaVO" property="objImp.fechaAltaView" />&nbsp;</td>
								<td><bean:write name="cuentaVO" property="estado.value" />&nbsp;</td>
								<!-- <#ColumnFiedls#> -->
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="altaOficioSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="altaOficioSearchPageVO" property="listResult">
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
			<logic:equal name="altaOficioSearchPageVO" property="viewResult" value="true">
  	    		<td align="right">
  	    			<logic:equal name="altaOficioSearchPageVO" property="modoSeleccionar" value="false">
						<bean:define id="agregarEnabled" name="altaOficioSearchPageVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar', '0');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
					</logic:equal>
  	    			<logic:equal name="altaOficioSearchPageVO" property="modoSeleccionar" value="true">
  	    				<logic:equal name="altaOficioSearchPageVO" property="agregarEnSeleccion" value="true">
							<bean:define id="agregarEnabled" name="altaOficioSearchPageVO" property="agregarEnabled"/>
							<input type="button" <%=agregarEnabled%> class="boton" 
								onClick="submitForm('agregar', '0');" 
								value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
							</logic:equal>
					</logic:equal>
				</td>
			</logic:equal>
		</tr>
	</table>
	<input type="hidden" name="name"  value="<bean:write name='altaOficioSearchPageVO' property='name'/>" id="name"/>
	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
							
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
