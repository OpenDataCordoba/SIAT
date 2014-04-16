<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/def/BuscarArea.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="def" key="def.areaSearchPage.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p><bean:message bundle="def" key="def.areaSearchPage.legend"/></p>
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
				<td><label><bean:message bundle="def" key="def.area.codArea.label"/>: </label></td>
				<td class="normal"><html:text name="areaSearchPageVO" property="area.codArea" size="15" maxlength="20" styleClass="datos" /></td>
			
				<td><label><bean:message bundle="def" key="def.area.desArea.label"/>: </label></td>
				<td class="normal"><html:text name="areaSearchPageVO" property="area.desArea" size="20" maxlength="100" styleClass="datos" /></td>
			</tr>
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
		<logic:equal name="areaSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="areaSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<logic:notEqual name="areaSearchPageVO" property="modoSeleccionar" value="true">
								<th width="1">&nbsp;</th> <!-- Modificar -->
								<th width="1">&nbsp;</th> <!-- Eliminar -->
								<th width="1">&nbsp;</th> <!-- Activar Desactivar -->
								<th width="1">&nbsp;</th> <!-- Adm. Oficinas -->
								<th width="1">&nbsp;</th> <!-- Recursos -->
							</logic:notEqual>
							<th align="left"><bean:message bundle="def" key="def.area.codArea.label"/></th>
							<th align="left"><bean:message bundle="def" key="def.area.desArea.label"/></th>
							<th align="left"><bean:message bundle="base" key="base.estado.label"/></th>
						</tr>
							
						<logic:iterate id="AreaVO" name="areaSearchPageVO" property="listResult">
							<tr>
								<!-- Seleccionar -->
								<logic:equal name="areaSearchPageVO" property="modoSeleccionar" value="true">
									<td>	
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="AreaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</td>
								</logic:equal>									
								<logic:notEqual name="areaSearchPageVO" property="modoSeleccionar" value="true">
									<!-- Ver -->
									<td>
										<logic:equal name="areaSearchPageVO" property="verEnabled" value="enabled">									
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="AreaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="AreaVO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</td>	
									<!-- Modificar-->								
									<td>
										<logic:equal name="areaSearchPageVO" property="modificarEnabled" value="enabled">
											<logic:equal name="AreaVO" property="modificarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="AreaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="AreaVO" property="modificarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="areaSearchPageVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</td>
	
									<!-- Eliminar-->								
									<td>
										<logic:equal name="areaSearchPageVO" property="eliminarEnabled" value="enabled">
											<logic:equal name="AreaVO" property="eliminarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="AreaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>
											</logic:equal>	
											<logic:notEqual name="AreaVO" property="eliminarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="areaSearchPageVO" property="eliminarEnabled" value="enabled">										
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</td>
									<td>
										<!-- Activar -->
										<logic:equal name="AreaVO" property="estado.id" value="0">
											<logic:equal name="areaSearchPageVO" property="activarEnabled" value="enabled">
												<logic:equal name="AreaVO" property="activarEnabled" value="enabled">
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('activar', '<bean:write name="AreaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="base" key="abm.button.activar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/activar0.gif"/>
													</a>
												</logic:equal> 
												<logic:notEqual name="AreaVO" property="activarEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/activar1.gif"/>
												</logic:notEqual>
											</logic:equal>
											<logic:notEqual name="areaSearchPageVO" property="activarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/activar1.gif"/>
											</logic:notEqual>
										</logic:equal> 
										<!-- Desactivar -->
										<logic:equal name="AreaVO" property="estado.id" value="1">
											<logic:equal name="areaSearchPageVO" property="desactivarEnabled" value="enabled">
												<logic:equal name="AreaVO" property="desactivarEnabled" value="enabled">
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('desactivar', '<bean:write name="AreaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="base" key="abm.button.desactivar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar0.gif"/>
													</a>
												</logic:equal>
												<logic:notEqual name="AreaVO" property="desactivarEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar1.gif"/>
												</logic:notEqual>
											</logic:equal>
											<logic:notEqual name="areaSearchPageVO" property="desactivarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar1.gif"/>
											</logic:notEqual>										
										</logic:equal>
										<!-- En estado creado -->
										<logic:equal name="AreaVO" property="estado.id" value="-1">
											<a style="cursor: pointer; cursor: hand;">
											<img border="0" title="<bean:message bundle="base" key="abm.button.creado"/>" src="<%=request.getContextPath()%>/images/iconos/creado0.gif"/>
											</a>
										</logic:equal> 
									</td>
								</logic:notEqual>
								
								<!-- Adm. Oficinas -->								
								<td>
									<logic:equal name="areaSearchPageVO" property="admOficinaEnabled" value="enabled">
										<logic:equal name="AreaVO" property="admOficinaEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('admOficina', '<bean:write name="AreaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="seg" key="seg.button.admOficina"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/oficina0.gif"/>
											</a>
										</logic:equal>	
										<logic:notEqual name="AreaVO" property="admOficinaEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/oficina1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="areaSearchPageVO" property="admOficinaEnabled" value="enabled">										
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/oficina1.gif"/>
									</logic:notEqual>
								</td>
								
								<!-- Recursos -->								
								<td>
									<logic:equal name="areaSearchPageVO" property="admRecursoAreaEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('admRecursoArea', '<bean:write name="AreaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="def" key="def.area.button.admRecurso"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/asociarRecursoVia0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="areaSearchPageVO" property="admRecursoAreaEnabled" value="enabled">										
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/asociarRecursoVia1.gif"/>
									</logic:notEqual>
								</td>
								
								<td><bean:write name="AreaVO" property="codArea"/>&nbsp;</td>
								<td><bean:write name="AreaVO" property="desArea"/>&nbsp;</td>
								<td><bean:write name="AreaVO" property="estado.value"/>&nbsp;</td>
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="areaSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="areaSearchPageVO" property="listResult">
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
			<logic:equal name="areaSearchPageVO" property="viewResult" value="true">
				<td align="right">
  	    			<logic:equal name="areaSearchPageVO" property="modoSeleccionar" value="false">
						<bean:define id="agregarEnabled" name="areaSearchPageVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton"
							onClick="submitForm('agregar', '0');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
					</logic:equal>
  	    			<logic:equal name="areaSearchPageVO" property="modoSeleccionar" value="true">
  	    				<logic:equal name="areaSearchPageVO" property="agregarEnSeleccion" value="true">
							<bean:define id="agregarEnabled" name="areaSearchPageVO" property="agregarEnabled"/>
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

	<input type="hidden" name="name"         value="<bean:write name='areaSearchPageVO' property='name'/>" id="name"/>
	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->