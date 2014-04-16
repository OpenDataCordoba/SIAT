<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/rod/BuscarTramiteRA.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="rod" key="rod.tramiteRASearchPage.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>
					<logic:equal name="tramiteRASearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="base" key="base.busquedaLegendBusqueda"/>
						<bean:message bundle="rod" key="rod.tramiteRA.label"/>
					</logic:equal>
					<logic:notEqual name="tramiteRASearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="rod" key="rod.tramiteRASearchPage.legend"/>
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
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.nroComuna.label"/>: </label></td>
				<td class="normal"><html:text name="tramiteRASearchPageVO" property="tramiteRA.nroComunaView" size="15" maxlength="20" styleClass="datos" /></td>
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.nroTramite.label"/>: </label></td>
				<td class="normal"><html:text name="tramiteRASearchPageVO" property="tramiteRA.nroTramiteView" size="15" maxlength="100" styleClass="datos" /></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="gde" key="gde.mandatario.label"/>: </label></td>
				<td class="normal"><html:text name="tramiteRASearchPageVO" property="tramiteRA.mandatario.descripcion" size="15" maxlength="100" styleClass="datos" /></td>
			</tr>
			<!-- fecha emision desde y fecha emision hasta -->				
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRASearchPage.fechaEmisionDesde.label"/>: </label></td>
				<td class="normal">
					<html:text name="tramiteRASearchPageVO" property="fechaEmisionDesdeView" styleId="fechaEmisionDesdeView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaEmisionDesdeView');" id="a_fechaEmisionDesdeView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRASearchPage.fechaEmisionHasta.label"/>: </label></td>
				<td class="normal">
					<html:text name="tramiteRASearchPageVO" property="fechaEmisionHastaView" styleId="fechaEmisionHastaView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaEmisionHastaView');" id="a_fechaEmisionHastaView">
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
		<logic:equal name="tramiteRASearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="tramiteRASearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<logic:notEqual name="tramiteRASearchPageVO" property="modoSeleccionar" value="true">
								<th width="1">&nbsp;</th> <!-- Modificar -->
								<th width="1">&nbsp;</th> <!-- Eliminar -->
								<th width="1">&nbsp;</th> <!-- Cambiar Estado -->								
							</logic:notEqual>
							<th align="left"><bean:message bundle="rod" key="rod.tramiteRA.fecha.label"/></th>
							<th align="left"><bean:message bundle="rod" key="rod.tramiteRA.ANroPatente.label"/></th>
							<th align="left"><bean:message bundle="rod" key="rod.tramiteRA.nroTramite.label"/></th>
							<th align="left"><bean:message bundle="rod" key="rod.tramiteRA.tipoTramite.label"/></th>
						</tr>
							
						<logic:iterate id="TramiteRAVO" name="tramiteRASearchPageVO" property="listResult">
							<tr>
								<!-- Seleccionar -->
								<logic:equal name="tramiteRASearchPageVO" property="modoSeleccionar" value="true">
									<td>	
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="TramiteRAVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</td>
								</logic:equal>									
								<logic:notEqual name="tramiteRASearchPageVO" property="modoSeleccionar" value="true">
									<!-- Ver -->
									<td>
										<logic:equal name="tramiteRASearchPageVO" property="verEnabled" value="enabled">									
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="TramiteRAVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="TramiteRAVO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</td>	
									<!-- Modificar-->								
									<td>
										<logic:equal name="tramiteRASearchPageVO" property="modificarEnabled" value="enabled">
											<logic:equal name="TramiteRAVO" property="modificarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="TramiteRAVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="TramiteRAVO" property="modificarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="tramiteRASearchPageVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</td>
	
									<!-- Eliminar-->								
									<td>
										<logic:equal name="tramiteRASearchPageVO" property="eliminarEnabled" value="enabled">
											<logic:equal name="TramiteRAVO" property="eliminarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="TramiteRAVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>
											</logic:equal>	
											<logic:notEqual name="TramiteRAVO" property="eliminarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="tramiteRASearchPageVO" property="eliminarEnabled" value="enabled">										
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</td>
									<!-- Accion cambiarEstado -->
									<td>
										<logic:equal name="tramiteRASearchPageVO" property="cambiarEstadoEnabled" value="enabled">
											<logic:equal name="TramiteRAVO" property="cambiarEstadoBussEnabled" value="true">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('cambiarEstado', '<bean:write name="TramiteRAVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="rod" key="rod.tramiteRASearchPage.button.cambiarEstado"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/cambiarEstado0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="TramiteRAVO" property="cambiarEstadoBussEnabled" value="true">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/cambiarEstado1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="tramiteRASearchPageVO" property="cambiarEstadoEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/cambiarEstado1.gif"/>
										</logic:notEqual>
									</td>
									
								</logic:notEqual>
								<td><bean:write name="TramiteRAVO" property="fechaView" />&nbsp;</td>
								<td><bean:write name="TramiteRAVO" property="ANroPatente"/>&nbsp;</td>
								<td><bean:write name="TramiteRAVO" property="nroTramite" bundle="base" formatKey="general.format.id"/>&nbsp;</td>
								<td><bean:write name="TramiteRAVO" property="desTipoTramite"/>&nbsp;</td>
								
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="tramiteRASearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="tramiteRASearchPageVO" property="listResult">
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
			<logic:equal name="tramiteRASearchPageVO" property="viewResult" value="true">
				<td align="right">
  	    			<logic:equal name="tramiteRASearchPageVO" property="modoSeleccionar" value="false">
						<bean:define id="agregarEnabled" name="tramiteRASearchPageVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar', '0');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
					</logic:equal>
  	    			<logic:equal name="tramiteRASearchPageVO" property="modoSeleccionar" value="true">
  	    				<logic:equal name="tramiteRASearchPageVO" property="agregarEnSeleccion" value="true">
							<bean:define id="agregarEnabled" name="tramiteRASearchPageVO" property="agregarEnabled"/>
							<input type="button" <%=agregarEnabled%> class="boton" 
								onClick="submitForm('agregar', '0');" 
								value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
							</logic:equal>
					</logic:equal>
				</td>				
			</logic:equal>
		</tr>
	</table>
	<input type="hidden" name="name"  value="<bean:write name='tramiteRASearchPageVO' property='name'/>" id="name"/>
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