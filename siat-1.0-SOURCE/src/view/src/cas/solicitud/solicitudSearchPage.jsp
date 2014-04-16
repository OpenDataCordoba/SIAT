<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/cas/BuscarSolicitud.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="cas" key="cas.solicitudSearchPage.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p><bean:message bundle="cas" key="cas.solicitudSearchPage.legend"/></p>
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
		
			<!--  area origen -->
			<tr>
				<td><label><bean:message bundle="cas" key="cas.solicitud.areaOrigen.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="solicitudSearchPageVO" property="solicitud.areaOrigen.id" styleClass="select">
						<html:optionsCollection name="solicitudSearchPageVO" property="listArea" label="desArea" value="id" />
					</html:select>
				</td>					
			</tr>
			
			<!-- area destino -->
			<tr>
				<td><label><bean:message bundle="cas" key="cas.solicitud.areaDestino.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="solicitudSearchPageVO" property="solicitud.areaDestino.id" styleClass="select">
						<html:optionsCollection name="solicitudSearchPageVO" property="listArea" label="desArea" value="id" />
					</html:select>
				</td>
			</tr>			
			
			<!-- Tipo Solicitud -->
			<tr>
				<td><label><bean:message bundle="cas" key="cas.tipoSolicitud.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="solicitudSearchPageVO" property="solicitud.tipoSolicitud.id" styleClass="select">
						<html:optionsCollection name="solicitudSearchPageVO" property="listTipoSolicitud" label="descripcion" value="id" />
					</html:select>
				</td>
			</tr>

			<!--  asunto -->
			<tr>
				<td><label><bean:message bundle="cas" key="cas.solicitud.asuntoSolicitud.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:text name="solicitudSearchPageVO" property="solicitud.asuntoSolicitud"/>					
				</td>
			</tr>
			
			<!-- cuenta -->
			<tr>
				<td><label><bean:message bundle="cas" key="cas.solicitud.cuenta.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:text name="solicitudSearchPageVO" property="solicitud.cuenta.numeroCuenta"/>
					<html:button property="btnBuscarCuenta"  styleClass="boton" onclick="submitForm('buscarCuenta', '');">
						<bean:message bundle="cas" key="cas.solicitudSearchPage.button.buscarCuenta"/>
					</html:button>					
				</td>
			</tr>
						
			<!-- fecha Desde -->		
			<tr>
				<td><label><bean:message bundle="base" key="base.fechaDesde"/>: </label></td>
				<td class="normal">
					<html:text name="solicitudSearchPageVO" property="fechaDesdeView" styleId="fechaDesdeView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
				
			<!-- fecha Hasta -->
				<td><label><bean:message bundle="base" key="base.fechaHasta"/>: </label></td>
				<td class="normal">
					<html:text name="solicitudSearchPageVO" property="fechaHastaView" styleId="fechaHastaView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
			
			<!-- numero -->
			<tr>
				<td><label><bean:message bundle="cas" key="cas.solicitud.numero.label"/>: </label></td>
				<td class="normal"><html:text name="solicitudSearchPageVO" property="solicitud.idView" size="10" maxlength="10"/></td>
				<td><label><bean:message bundle="cas" key="cas.estSolicitud.label"/>: </label></td>
				
			<!-- estado -->
				<td class="normal">
					<html:select name="solicitudSearchPageVO" property="solicitud.estSolicitud.id" styleClass="select">
						<html:optionsCollection name="solicitudSearchPageVO" property="listEstSolicitud" label="descripcion" value="id" />
					</html:select>
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
		<logic:equal name="solicitudSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="solicitudSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<logic:notEqual name="solicitudSearchPageVO" property="modoSeleccionar" value="true">
								<th width="1">&nbsp;</th> <!-- Modificar -->
								<th width="1">&nbsp;</th> <!-- Eliminar -->
								<th width="1">&nbsp;</th> <!-- Activar Desactivar -->
							</logic:notEqual>
							<th align="left"><bean:message bundle="cas" key="cas.solicitud.numero.label"/></th>
							<th align="left"><bean:message bundle="cas" key="cas.tipoSolicitud.label"/></th>							
							<th align="left"><bean:message bundle="cas" key="cas.solicitud.areaOrigen.label"/></th>
							<th align="left"><bean:message bundle="cas" key="cas.solicitud.areaDestino.label"/></th>
							<th align="left"><bean:message bundle="cas" key="cas.solicitud.fechaAlta.label"/></th>							
							<th align="left"><bean:message bundle="cas" key="cas.solicitud.asuntoSolicitud.label"/></th>
							<th align="left"><bean:message bundle="cas" key="cas.estSolicitud.label"/></th>							
						</tr>
							
						<logic:iterate id="SolicitudVO" name="solicitudSearchPageVO" property="listResult">
							<tr>
								<!-- Seleccionar -->
								<logic:equal name="solicitudSearchPageVO" property="modoSeleccionar" value="true">
									<td>	
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="SolicitudVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</td>
								</logic:equal>									
								<logic:notEqual name="solicitudSearchPageVO" property="modoSeleccionar" value="true">
									<!-- Ver -->
									<td>
										<logic:equal name="solicitudSearchPageVO" property="verEnabled" value="enabled">									
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="SolicitudVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="solicitudSearchPageVO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</td>	
									<!-- Modificar-->
									<td>
										<logic:equal name="solicitudSearchPageVO" property="modificarEnabled" value="enabled">
											<logic:equal name="SolicitudVO" property="modificarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="SolicitudVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="SolicitudVO" property="modificarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="solicitudSearchPageVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</td>
	
									<!-- Eliminar-->								
									<td>
										<logic:equal name="solicitudSearchPageVO" property="eliminarEnabled" value="enabled">
											<logic:equal name="SolicitudVO" property="eliminarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="SolicitudVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>
											</logic:equal>	
											<logic:notEqual name="SolicitudVO" property="eliminarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="solicitudSearchPageVO" property="eliminarEnabled" value="enabled">										
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</td>									
									<td>
										<!-- Accion cambiarEstadoEnabled -->										
										<logic:equal name="solicitudSearchPageVO" property="cambiarEstadoEnabled" value="enabled">
											<logic:equal name="SolicitudVO" property="cambiarEstadoBussEnabled" value="true">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('cambiarEstado', '<bean:write name="SolicitudVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="cas" key="cas.solicitudSearchPage.button.cambiarEstado"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/cambiarEstado0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="SolicitudVO" property="cambiarEstadoBussEnabled" value="true">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/cambiarEstado1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="solicitudSearchPageVO" property="cambiarEstadoEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/cambiarEstado1.gif"/>
										</logic:notEqual>										
									</td>									 
								</logic:notEqual>
								<td><bean:write name="SolicitudVO" property="idView"/>&nbsp;</td>
								<td><bean:write name="SolicitudVO" property="tipoSolicitud.descripcion"/>&nbsp;</td>								
								<td><bean:write name="SolicitudVO" property="areaOrigen.desArea"/>&nbsp;</td>
								<td><bean:write name="SolicitudVO" property="areaDestino.desArea"/>&nbsp;</td>
								<td><bean:write name="SolicitudVO" property="fechaAltaView"/>&nbsp;</td>																
								<td><bean:write name="SolicitudVO" property="asuntoSolicitud"/>&nbsp;</td>
								<td><bean:write name="SolicitudVO" property="estSolicitud.descripcion"/>&nbsp;</td>								
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="solicitudSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="solicitudSearchPageVO" property="listResult">
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
			<logic:equal name="solicitudSearchPageVO" property="viewResult" value="true">
				<td align="right">
  	    			<logic:equal name="solicitudSearchPageVO" property="modoSeleccionar" value="false">
						<bean:define id="agregarEnabled" name="solicitudSearchPageVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar', '0');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
					</logic:equal>
  	    			<logic:equal name="solicitudSearchPageVO" property="modoSeleccionar" value="true">
  	    				<logic:equal name="solicitudSearchPageVO" property="agregarEnSeleccion" value="true">
							<bean:define id="agregarEnabled" name="solicitudSearchPageVO" property="agregarEnabled"/>
							<input type="button" <%=agregarEnabled%> class="boton" 
								onClick="submitForm('agregar', '0');" 
								value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
							</logic:equal>
					</logic:equal>
				</td>				
			</logic:equal>
		</tr>
	
	</table>
	<input type="hidden" name="name"  value="<bean:write name='solicitudSearchPageVO' property='name'/>" id="name"/>
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