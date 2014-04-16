<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/pad/BuscarCierreComercio.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="pad" key="pad.cierreComercioSearchPage.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">				
				<p>
					<logic:equal name="cierreComercioSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="base" key="base.busquedaLegend"/>
						<bean:message bundle="pad" key="pad.cierreComercio.label"/>
					</logic:equal>
					<logic:notEqual name="cierreComercioSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="pad" key="pad.cierreComercioSearchPage.legend"/>
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
				<td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.ref"/>: </label></td>
				<td class="normal"><html:text name="cierreComercioSearchPageVO" property="cierreComercio.cuentaVO.numeroCuenta" size="15" maxlength="10" styleClass="datos" /></td>
			</tr>
			
			<tr>
				<td><label><bean:message bundle="pad" key="pad.cierreComercio.fechaCierreDefDesde.label"/>: </label></td>
				<td class="normal">
					<html:text name="cierreComercioSearchPageVO" property="fechaCierreDefDesdeView" styleId="fechaCierreDefDesdeView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaCierreDefDesdeView');" id="a_fechaCierreDefDesdeView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
				
				<td><label><bean:message bundle="pad" key="pad.cierreComercio.fechaCierreDefHasta.label"/>: </label></td>
				<td class="normal">
					<html:text name="cierreComercioSearchPageVO" property="fechaCierreDefHastaView" styleId="fechaCierreDefHastaView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaCierreDefHastaView');" id="a_fechaCierreDefHastaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>

			</tr>

			<tr>
				<td><label><bean:message bundle="pad" key="pad.cierreComercio.fechaCeseActividadDesde.label"/>: </label></td>
				<td class="normal">
					<html:text name="cierreComercioSearchPageVO" property="fechaCeseActividadDesdeView" styleId="fechaCeseActividadDesdeView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaCeseActividadDesdeView');" id="a_fechaCeseActividadDesdeView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>

				<td><label><bean:message bundle="pad" key="pad.cierreComercio.fechaCeseActividadHasta.label"/>: </label></td>
				<td class="normal">
					<html:text name="cierreComercioSearchPageVO" property="fechaCeseActividadHastaView" styleId="fechaCeseActividadHastaView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaCeseActividadHastaView');" id="a_fechaCeseActividadHastaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
			
			<tr>
				<td><label><bean:message bundle="cas" key="cas.sistemaOrigen.label"/>: </label></td>
				<td colspan="3">
					<bean:define id="IncludedVO" name="cierreComercioSearchPageVO" property="cierreComercio"/>
					<bean:define id="voName" value="cierreComercio" />
					<%@ include file="/cas/caso/includeCaso.jsp" %>
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
		<logic:equal name="cierreComercioSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="cierreComercioSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<logic:notEqual name="cierreComercioSearchPageVO" property="modoSeleccionar" value="true">
								<th width="1">&nbsp;</th> <!-- Modificar -->
							</logic:notEqual>
							<th width="1">&nbsp;</th> <!-- Recomponer / imprimir-->
							<th align="left"><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.ref"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.cierreComercio.fechaCierreDefinitivo.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.cierreComercio.fechaCeseActividad.label"/></th>
							<th align="left"><bean:message bundle="cas" key="cas.caso.label"/></th>
						</tr>
							
						<logic:iterate id="CierreComercioVO" name="cierreComercioSearchPageVO" property="listResult">
							<tr>
								<!-- Seleccionar -->
								<logic:equal name="cierreComercioSearchPageVO" property="modoSeleccionar" value="true">
									<td>	
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="CierreComercioVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</td>
								</logic:equal>									
								<logic:notEqual name="cierreComercioSearchPageVO" property="modoSeleccionar" value="true">
									<!-- Ver -->
									<td>
										<logic:equal name="cierreComercioSearchPageVO" property="verEnabled" value="enabled">									
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="CierreComercioVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="CierreComercioVO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</td>	
									<!-- Modificar-->								
									<td>
										<logic:equal name="cierreComercioSearchPageVO" property="modificarEnabled" value="enabled">
											<logic:equal name="CierreComercioVO" property="modificarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="CierreComercioVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="CierreComercioVO" property="modificarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="cierreComercioSearchPageVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</td>
									<td>									
										<logic:equal name="CierreComercioVO" property="imprimirEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('irImprimir',	'<bean:write name="CierreComercioVO" property="cuentaVO.id" bundle = "base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="gde" key="gde.cierreComercio.button.imprimir"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/imprimirPadronPlaEnvDeuJud.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="CierreComercioVO" property="imprimirEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/imprimirPadronPlaEnvDeuJud.gif"/>
										</logic:notEqual>
									</td>
	
								</logic:notEqual>
								<td><bean:write name="CierreComercioVO" property="cuentaVO.numeroCuenta"/>&nbsp;</td>
								<td><bean:write name="CierreComercioVO" property="fechaCierreDefView"/>&nbsp;</td>
								<td><bean:write name="CierreComercioVO" property="fechaCeseActividadView"/>&nbsp;</td>
								<td><bean:write name="CierreComercioVO" property="caso.casoView"/>&nbsp;</td>
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="cierreComercioSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="cierreComercioSearchPageVO" property="listResult">
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
			<logic:equal name="cierreComercioSearchPageVO" property="viewResult" value="true">
  	    		<td align="right">
  	    			<logic:equal name="cierreComercioSearchPageVO" property="modoSeleccionar" value="false">
						<bean:define id="agregarEnabled" name="cierreComercioSearchPageVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar', '0');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
					</logic:equal>
  	    			<logic:equal name="cierreComercioSearchPageVO" property="modoSeleccionar" value="true">
  	    				<logic:equal name="cierreComercioSearchPageVO" property="agregarEnSeleccion" value="true">
							<bean:define id="agregarEnabled" name="cierreComercioSearchPageVO" property="agregarEnabled"/>
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
	<input type="hidden" name="name"         value="<bean:write name='cierreComercioSearchPageVO' property='name'/>" id="name"/>
	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
	<input type="text" style="display:none"/>	
	
	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
	
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
