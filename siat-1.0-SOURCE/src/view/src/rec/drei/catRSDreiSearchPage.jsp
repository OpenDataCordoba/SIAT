<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/rec/BuscarCatRSDrei.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="rec" key="rec.catRSDreiSearchPage.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>
					<logic:equal name="catRSDreiSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="base" key="base.busquedaLegendBusqueda"/>
						<bean:message bundle="rec" key="rec.catRSDrei.label"/>
					</logic:equal>
					<logic:notEqual name="catRSDreiSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="rec" key="rec.catRSDreiSearchPage.legend"/>
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
				<td><label><bean:message bundle="rec" key="rec.catRSDrei.nroCatRSDrei.label"/>: </label></td>
				<td class="normal"><html:text name="catRSDreiSearchPageVO" property="catRSDrei.nroCategoria" size="20" styleClass="datos" /></td>
			</tr>
					
			<tr>			
			<td><label><bean:message bundle="rec" key="rec.catRSDreiSearchPage.fechacatRSDreiDesde.label"/>: </label></td>
				<td class="normal"><html:text name="catRSDreiSearchPageVO" property="fechaCatRSDreiDesdeView" size="12" styleId="fechaDesdeView"/>
					<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
				<td><label><bean:message bundle="rec" key="rec.catRSDreiSearchPage.fechacatRSDreiHasta.label"/>: </label></td>
				<td class="normal"><html:text name="catRSDreiSearchPageVO" property="fechaCatRSDreiHastaView" size="12" styleId="fechaHastaView"/>
					<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>
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
		<logic:equal name="catRSDreiSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="catRSDreiSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
						<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<logic:notEqual name="catRSDreiSearchPageVO" property="modoSeleccionar" value="true">
								<th width="1">&nbsp;</th> <!-- Modificar -->
								<th width="1">&nbsp;</th> <!-- Eliminar -->
								<th width="1">&nbsp;</th> <!-- Activar Desactivar -->						
							</logic:notEqual>
														
							<th align="left"><bean:message bundle="rec" key="rec.catRSDrei.nroCatRSDrei.label"/></th>						
							<th align="left"><bean:message bundle="rec" key="rec.catRSDrei.ingBruAnu.label"/></th>	
							<th align="left"><bean:message bundle="rec" key="rec.catRSDrei.importe.label"/></th>
							<th align="left"><bean:message bundle="rec" key="rec.catRSDrei.cantEmpleados.label"/></th>													
							<th align="left"><bean:message bundle="rec" key="rec.catRSDreiSearchPage.fechacatRSDreiDesde.label"/></th>							
							<th align="left"><bean:message bundle="rec" key="rec.catRSDreiSearchPage.fechacatRSDreiHasta.label"/></th>									
						</tr>
						
						<logic:iterate id="CatRSDreiVO" name="catRSDreiSearchPageVO" property="listResult">
							<tr>
								<!-- Seleccionar -->
								<logic:equal name="catRSDreiSearchPageVO" property="modoSeleccionar" value="true">
									<td>	
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="CatRSDreiVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</td>
								</logic:equal>									
								<logic:notEqual name="catRSDreiSearchPageVO" property="modoSeleccionar" value="true">
									<!-- Ver -->
									<td>
										<logic:equal name="catRSDreiSearchPageVO" property="verEnabled" value="enabled">									
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="CatRSDreiVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="CatRSDreiVO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</td>	
									<!-- Modificar-->								
									<td>
										<logic:equal name="catRSDreiSearchPageVO" property="modificarEnabled" value="enabled">
											<logic:equal name="CatRSDreiVO" property="modificarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="CatRSDreiVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="CatRSDreiVO" property="modificarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="catRSDreiSearchPageVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</td>	
									<!-- Eliminar-->								
									<td>
										<logic:equal name="catRSDreiSearchPageVO" property="eliminarEnabled" value="enabled">
											<logic:equal name="CatRSDreiVO" property="eliminarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="CatRSDreiVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>
											</logic:equal>	
											<logic:notEqual name="CatRSDreiVO" property="eliminarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="catRSDreiSearchPageVO" property="eliminarEnabled" value="enabled">										
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</td>
									<td>
									<!-- Activar -->
									<logic:equal name="CatRSDreiVO" property="estado.id" value="0">
											<logic:equal name="catRSDreiSearchPageVO" property="activarEnabled" value="enabled">
												<logic:equal name="CatRSDreiVO" property="activarEnabled" value="enabled">
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('activar', '<bean:write name="CatRSDreiVO" property="id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="base" key="abm.button.activar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/activar0.gif"/>
													</a>
												</logic:equal> 
												<logic:notEqual name="CatRSDreiVO" property="activarEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/activar1.gif"/>
												</logic:notEqual>
											</logic:equal>
											<logic:notEqual name="catRSDreiSearchPageVO" property="activarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/activar1.gif"/>
											</logic:notEqual>
									</logic:equal> 
										<!-- Desactivar -->
										<logic:equal name="CatRSDreiVO" property="estado.id" value="1">
											<logic:equal name="catRSDreiSearchPageVO" property="desactivarEnabled" value="enabled">
												<logic:equal name="CatRSDreiVO" property="desactivarEnabled" value="enabled">
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('desactivar', '<bean:write name="CatRSDreiVO" property="id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="base" key="abm.button.desactivar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar0.gif"/>
													</a>
												</logic:equal>
												<logic:notEqual name="CatRSDreiVO" property="desactivarEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar1.gif"/>
												</logic:notEqual>
											</logic:equal>
											<logic:notEqual name="catRSDreiSearchPageVO" property="desactivarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar1.gif"/>
											</logic:notEqual>										
										</logic:equal>
										<!-- En estado creado -->
										<logic:equal name="CatRSDreiVO" property="estado.id" value="-1">
											<a style="cursor: pointer; cursor: hand;">
											<img border="0" title="<bean:message bundle="base" key="abm.button.creado"/>" src="<%=request.getContextPath()%>/images/iconos/creado0.gif"/>
											</a>
										</logic:equal> 
									</td>
								</logic:notEqual>								
								<td><bean:write name="CatRSDreiVO" property="nroCategoriaView"/>&nbsp;</td>
								<td><bean:write name="CatRSDreiVO" property="ingBruAnuView"/>&nbsp;</td>
								<td><bean:write name="CatRSDreiVO" property="importeView"/>&nbsp;</td>
								<td><bean:write name="CatRSDreiVO" property="cantEmpleadosView"/>&nbsp;</td>
								<td><bean:write name="CatRSDreiVO" property="fechaDesdeView"/>&nbsp;</td>
								<td><bean:write name="CatRSDreiVO" property="fechaHastaView"/>&nbsp;</td>								
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="catRSDreiSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="catRSDreiSearchPageVO" property="listResult">
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
			<logic:equal name="catRSDreiSearchPageVO" property="viewResult" value="true">
  	    		<td align="right">
  	    			<logic:equal name="catRSDreiSearchPageVO" property="modoSeleccionar" value="false">
						<bean:define id="agregarEnabled" name="catRSDreiSearchPageVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar', '0');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
					</logic:equal>
  	    			<logic:equal name="catRSDreiSearchPageVO" property="modoSeleccionar" value="true">
  	    				<logic:equal name="catRSDreiSearchPageVO" property="agregarEnSeleccion" value="true">
							<bean:define id="agregarEnabled" name="catRSDreiSearchPageVO" property="agregarEnabled"/>
							<input type="button" <%=agregarEnabled%> class="boton" 
								onClick="submitForm('agregar', '0');" 
								value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
							</logic:equal>
					</logic:equal>
				</td>
			</logic:equal>
		</tr>
	</table>
	
	<!-- 
	<input type="hidden" name="name"  value="<bean:write name='catRSDreiSearchPageVO' property='name'/>" id="name"/>
   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
	 -->	
		
	<input type="hidden" name="method" value=""/>
    <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
    <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->