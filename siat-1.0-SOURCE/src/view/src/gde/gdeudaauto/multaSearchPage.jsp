<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/BuscarMulta.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	
	<!-- Busqueda de Recuso -->
	<span id="blockBusqueda" style="display:none"> 
		<bean:define id="adapterVO" name="multaSearchPageVO"/>
		<bean:define id="poseeParam" value="false" />
		<%@ include file="/def/gravamen/includeRecursoSearch.jsp" %>
	</span>

	<span id="blockSimple" style="display:block">
		
		<h1><bean:message bundle="gde" key="gde.multaSearchPage.title"/></h1>	
			
		<table class="tablabotones" width="100%">
			<tr>
				<td align="left">
					<p>
						<logic:equal name="multaSearchPageVO" property="modoSeleccionar" value="true">
							<bean:message bundle="base" key="base.busquedaLegendBusqueda"/>
							<bean:message bundle="gde" key="gde.multa.label"/>
						</logic:equal>
						<logic:notEqual name="multaSearchPageVO" property="modoSeleccionar" value="true">
							<bean:message bundle="gde" key="gde.multaSearchPage.legend"/>
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
						<td><label>(*)<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
						<td class="normal">
							<html:select name="multaSearchPageVO" property="recurso.id" styleClass="select" styleId="cboRecurso" style="width:90%">
								<bean:define id="includeRecursoList" name="multaSearchPageVO" property="listRecurso"/>
								<bean:define id="includeIdRecursoSelected" name="multaSearchPageVO" property="recurso.id"/>
								<%@ include file="/def/gravamen/includeRecurso.jsp" %>
							</html:select>
							
							<a onclick="toggleSearchRecurso('blockSimple', 'blockBusqueda'); setFocus('recursoFilter');" >
								<img title='<bean:message bundle="def" key="def.recurso.filtro.button.busqueda"/>' border="0" 
								src="<%=request.getContextPath()%>/images/iconos/lupita.gif" width="20" height="20">
							</a>
							
						</td>				
					</tr>
					<tr>
			      		<td><label>(*)<bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
			      		<td class="normal">
			      			<html:text name="multaSearchPageVO" property="multa.cuenta.numeroCuenta" size="15" maxlength="20" styleClass="datos"/>
						</td>
					</tr>
					<!-- fecha emision desde y fecha emision hasta -->				
					<tr>
						<td><label><bean:message bundle="gde" key="gde.multaSearchPage.fechaEmisionDesde.label"/>: </label></td>
						<td class="normal">
							<html:text name="multaSearchPageVO" property="fechaEmisionDesdeView" styleId="fechaEmisionDesdeView" size="15" maxlength="10" styleClass="datos" />
							<a class="link_siat" onclick="return show_calendar('fechaEmisionDesdeView');" id="a_fechaEmisionDesdeView">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>
					</tr>
					<tr>
						<td><label><bean:message bundle="gde" key="gde.multaSearchPage.fechaEmisionHasta.label"/>: </label></td>
						<td class="normal">
							<html:text name="multaSearchPageVO" property="fechaEmisionHastaView" styleId="fechaEmisionHastaView" size="15" maxlength="10" styleClass="datos" />
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
			<logic:equal name="multaSearchPageVO" property="viewResult" value="true">
				<logic:notEmpty  name="multaSearchPageVO" property="listResult">	
					<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
						<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
		               	<tbody>
			               	<tr>
								<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
								<logic:notEqual name="multaSearchPageVO" property="modoSeleccionar" value="true">
									<th width="1">&nbsp;</th> <!-- Modificar -->
									
										<th width="1">&nbsp;</th> <!-- Eliminar -->
																	
								</logic:notEqual>
								<th align="left"><bean:message bundle="pad" key="pad.cuenta.label"/></th>
								<th align="left"><bean:message bundle="def" key="def.recurso.label"/></th>
								<th align="left"><bean:message bundle="gde" key="gde.multa.fechaEmision.label"/></th>
								<th align="left"><bean:message bundle="gde" key="gde.tipoMulta.label"/></th>
								<th align="left"><bean:message bundle="gde" key="gde.multa.importe.label"/></th>
								<th align="left"><bean:message bundle="gde" key="gde.multa.fechaVencimiento.label"/></th>
							</tr>
								
							<logic:iterate id="MultaVO" name="multaSearchPageVO" property="listResult">
								<tr>
									<!-- Seleccionar -->
									<logic:equal name="multaSearchPageVO" property="modoSeleccionar" value="true">
										<td>	
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="MultaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</td>
									</logic:equal>									
									<logic:notEqual name="multaSearchPageVO" property="modoSeleccionar" value="true">
										<!-- Ver -->
										<td>
											<logic:equal name="multaSearchPageVO" property="verEnabled" value="enabled">									
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="MultaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="MultaVO" property="verEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
											</logic:notEqual>
										</td>	
										<!-- Modificar-->								
										<td>
											<logic:equal name="multaSearchPageVO" property="modificarEnabled" value="enabled">
												<logic:equal name="MultaVO" property="modificarEnabled" value="enabled">
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="MultaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
													</a>
												</logic:equal>
												<logic:notEqual name="MultaVO" property="modificarEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
												</logic:notEqual>
											</logic:equal>
											<logic:notEqual name="multaSearchPageVO" property="modificarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
											</logic:notEqual>
										</td>
		
										<!-- Eliminar-->								
										<td>
											<logic:equal name="multaSearchPageVO" property="eliminarEnabled" value="enabled">
												<logic:equal name="MultaVO" property="eliminarEnabled" value="enabled">
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="MultaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
													</a>
												</logic:equal>	
												<logic:notEqual name="MultaVO" property="eliminarEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
												</logic:notEqual>
											</logic:equal>
											<logic:notEqual name="multaSearchPageVO" property="eliminarEnabled" value="enabled">										
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
											</logic:notEqual>
										</td>
										
									</logic:notEqual>
									<td><bean:write name="MultaVO" property="cuenta.numeroCuenta"/>&nbsp;</td>
									<td><bean:write name="MultaVO" property="cuenta.recurso.desRecurso" />&nbsp;</td>
									<td><bean:write name="MultaVO" property="fechaEmisionView"/>&nbsp;</td>
									<td><bean:write name="MultaVO" property="tipoMulta.desTipoMulta"/>&nbsp;</td>
									<td><bean:write name="MultaVO" property="importeView"/>&nbsp;</td>
									<td><bean:write name="MultaVO" property="fechaVencimientoView"/>&nbsp;</td>
									
								</tr>
							</logic:iterate>
					
							<tr>
								<td class="paginador" align="center" colspan="20">
									<bean:define id="pager" name="multaSearchPageVO"/>
									<%@ include file="/base/pager.jsp" %>
								</td>
							</tr>
							
						</tbody>
					</table>
				</logic:notEmpty>
				
				<logic:empty name="multaSearchPageVO" property="listResult">
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
				<logic:equal name="multaSearchPageVO" property="viewResult" value="true">
					<td align="right">
	  	    			<logic:equal name="multaSearchPageVO" property="modoSeleccionar" value="false">
							<bean:define id="agregarEnabled" name="multaSearchPageVO" property="agregarEnabled"/>
							<input type="button" <%=agregarEnabled%> class="boton" 
								onClick="submitForm('agregar', '0');" 
								value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
						</logic:equal>
	  	    			<logic:equal name="multaSearchPageVO" property="modoSeleccionar" value="true">
	  	    				<logic:equal name="multaSearchPageVO" property="agregarEnSeleccion" value="true">
								<bean:define id="agregarEnabled" name="multaSearchPageVO" property="agregarEnabled"/>
								<input type="button" <%=agregarEnabled%> class="boton" 
									onClick="submitForm('agregar', '0');" 
									value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
								</logic:equal>
						</logic:equal>
					</td>				
				</logic:equal>
			</tr>
		</table>
		
	</span>
		
	<input type="hidden" name="name"  value="<bean:write name='multaSearchPageVO' property='name'/>" id="name"/>
   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
		
	<input type="hidden" name="method" value=""/>
    <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
    <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
	<input type="hidden" name="idCuenta" value="<bean:write name="multaSearchPageVO" property="multa.cuenta.id" bundle="base" formatKey="general.format.id"/>"/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->