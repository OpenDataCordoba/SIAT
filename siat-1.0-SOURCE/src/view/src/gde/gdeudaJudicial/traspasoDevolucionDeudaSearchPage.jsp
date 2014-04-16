<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/BuscarTraspasoDevolucionDeuda.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<!-- Busqueda de Recuso -->
	<span id="blockBusqueda" style="display:none"> 
		<bean:define id="adapterVO" name="traspasoDevolucionDeudaSearchPageVO"/>
		<bean:define id="poseeParam" value="true" />
		<%@ include file="/def/gravamen/includeRecursoSearch.jsp" %>
	</span>

	<span id="blockSimple" style="display:block">

		<h1><bean:message bundle="gde" key="gde.traspasoDevolucionDeudaSearchPage.title"/></h1>	
			
		<p><bean:message bundle="gde" key="gde.traspasoDevolucionDeudaSearchPage.legend"/></p>
		
		<table class="tablabotones" width="100%">
			<tr>
				<td align="right"><html:button property="btnVolver"
					styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver" />
				</html:button></td>
			</tr>
		</table>
		<!-- Filtro -->
			<fieldset>
				<legend><bean:message bundle="base" key="base.parametrosBusqueda"/></legend>
				
				<table class="tabladatos">
					<tr>
						<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.traspasoDevolucionDeudaSearchPage.accion.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:select name="traspasoDevolucionDeudaSearchPageVO" property="accionTraspasoDevolucion.id" styleClass="select" onchange="submitForm('paramAccion', '');">
								<html:optionsCollection name="traspasoDevolucionDeudaSearchPageVO" property="listAccionTraspasoDevolucion" label="value" value="id" />
							</html:select>
						</td>
					</tr>
					<tr>
						<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:select name="traspasoDevolucionDeudaSearchPageVO" property="recurso.id" styleClass="select" onchange="submitForm('paramRecurso', '');"  styleId="cboRecurso" style="width:90%">
								<bean:define id="includeRecursoList"       name="traspasoDevolucionDeudaSearchPageVO" property="listRecurso"/>
								<bean:define id="includeIdRecursoSelected" name="traspasoDevolucionDeudaSearchPageVO" property="recurso.id"/>
								<%@ include file="/def/gravamen/includeRecurso.jsp" %>
							</html:select>
							
							<a onclick="toggleSearchRecurso('blockSimple', 'blockBusqueda'); setFocus('recursoFilter');" >
								<img title='<bean:message bundle="def" key="def.recurso.filtro.button.busqueda"/>' border="0" 
								src="<%=request.getContextPath()%>/images/iconos/lupita.gif" width="20" height="20">
							</a>
							
						</td>
					</tr>
					<tr>
						<td><label><bean:message bundle="gde" key="gde.traspasoDevolucionDeudaSearchPage.procuradorOrigen.label"/>: </label></td>
						<td class="normal">
							<html:select name="traspasoDevolucionDeudaSearchPageVO" property="procuradorOrigen.id" styleClass="select">
								<html:optionsCollection name="traspasoDevolucionDeudaSearchPageVO" property="listProcuradorOrigen" label="nombreConCod" value="id" />
							</html:select>
						</td>
						<logic:equal name="traspasoDevolucionDeudaSearchPageVO" property="accionTraspasoDevolucion.esTraspaso" value="true">
							<td><label><bean:message bundle="gde" key="gde.traspasoDevolucionDeudaSearchPage.procuradorDestino.label"/>: </label></td>
							<td class="normal">
								<html:select name="traspasoDevolucionDeudaSearchPageVO" property="procuradorDestino.id" styleClass="select">
									<html:optionsCollection name="traspasoDevolucionDeudaSearchPageVO" property="listProcuradorDestino" label="nombreConCod" value="id" />
								</html:select>
							</td>
						</logic:equal>
					</tr>
					<tr>
						<td><label><bean:message bundle="pad" key="pad.cuenta.label" />: </label></td>
						<td class="normal">
							<html:text name="traspasoDevolucionDeudaSearchPageVO" property="cuenta.numeroCuenta" 
								size="10" maxlength="10" styleClass="datos"  /> 
							<html:button property="btnBuscarCuenta" styleClass="boton" onclick="submitForm('buscarCuenta', '');">
								<bean:message bundle="gde" key="gde.traspasoDevolucionDeudaSearchPage.button.buscarCuenta" />
							</html:button>
						</td>
					</tr>
					
					<tr>
						<td><label><bean:message bundle="gde" key="gde.traspasoDevolucionDeudaSearchPage.fechaDesde.label"/>: </label></td>
						<td class="normal">
							<html:text name="traspasoDevolucionDeudaSearchPageVO" property="fechaDesdeView" styleId="fechaDesdeView" size="15" maxlength="10" styleClass="datos" />
							<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>
						<td><label><bean:message bundle="gde" key="gde.traspasoDevolucionDeudaSearchPage.fechaHasta.label"/>: </label></td>
						<td class="normal">
							<html:text name="traspasoDevolucionDeudaSearchPageVO" property="fechaHastaView" styleId="fechaHastaView" size="15" maxlength="10" styleClass="datos" />
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
				  	<html:button property="btnBuscar"  styleClass="boton" onclick="submitForm('buscar', '');">
						<bean:message bundle="base" key="abm.button.buscar"/>
					</html:button>
				</p>
			</fieldset>	
			<!-- Fin Filtro -->
			
			<!-- Resultado Filtro -->
			<div id="resultadoFiltro">
				<logic:equal name="traspasoDevolucionDeudaSearchPageVO" property="viewResult" value="true">			
					<logic:notEmpty  name="traspasoDevolucionDeudaSearchPageVO" property="listResult">	
						<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
							<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	                		<tbody>
		                		<tr>
									<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
									<logic:notEqual name="traspasoDevolucionDeudaSearchPageVO" property="modoSeleccionar" value="true">
										<th width="1">&nbsp;</th> <!-- Modificar -->
										<th width="1">&nbsp;</th> <!-- Eliminar -->
									</logic:notEqual>
									<th align="left"><bean:message bundle="gde" key="gde.traspasoDeuda.fecha"/></th>
									<th align="left"><bean:message bundle="def" key="def.recurso.label"/></th>
									<th align="left"><bean:message bundle="gde" key="gde.traspasoDeuda.proOri"/></th>
									<th align="left"><bean:message bundle="gde" key="gde.traspasoDeuda.proDes"/></th>
									<th align="left"><bean:message bundle="pad" key="pad.cuenta.label"/></th>
								</tr>
	
							<logic:iterate id="TraspasoDevolucionVO" name="traspasoDevolucionDeudaSearchPageVO" property="listResult">
								<tr>
									<!-- Seleccionar -->
									<logic:equal name="traspasoDevolucionDeudaSearchPageVO" property="modoSeleccionar" value="true">
										<td>	
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="TraspasoDevolucionVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</td>
									</logic:equal>									
									<logic:notEqual name="traspasoDevolucionDeudaSearchPageVO" property="modoSeleccionar" value="true">
										<!-- Ver -->
										<td>
											<logic:equal name="traspasoDevolucionDeudaSearchPageVO" property="verEnabled" value="enabled">
												<logic:equal name="TraspasoDevolucionVO" property="verEnabled" value="enabled">
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="TraspasoDevolucionVO" property="id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
													</a>
												</logic:equal>
												<logic:notEqual name="TraspasoDevolucionVO" property="verEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
												</logic:notEqual>
											</logic:equal>
											<logic:notEqual name="traspasoDevolucionDeudaSearchPageVO" property="verEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
											</logic:notEqual>
										</td>
										<!-- Modificar-->								
										<td>
											<logic:equal name="traspasoDevolucionDeudaSearchPageVO" property="modificarEnabled" value="enabled">
												<logic:equal name="TraspasoDevolucionVO" property="modificarEnabled" value="enabled">
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="TraspasoDevolucionVO" property="id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
													</a>
												</logic:equal>
												<logic:notEqual name="TraspasoDevolucionVO" property="modificarEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
												</logic:notEqual>
											</logic:equal>
											<logic:notEqual name="traspasoDevolucionDeudaSearchPageVO" property="modificarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
											</logic:notEqual>
										</td>
										<!-- Eliminar-->								
										<td>
											<logic:equal name="traspasoDevolucionDeudaSearchPageVO" property="eliminarEnabled" value="enabled">
												<logic:equal name="TraspasoDevolucionVO" property="eliminarEnabled" value="enabled">
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="TraspasoDevolucionVO" property="id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
													</a>
												</logic:equal>	
												<logic:notEqual name="TraspasoDevolucionVO" property="eliminarEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
												</logic:notEqual>
											</logic:equal>
											<logic:notEqual name="traspasoDevolucionDeudaSearchPageVO" property="eliminarEnabled" value="enabled">										
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
											</logic:notEqual>
										</td>
									</logic:notEqual>
		
									<td><bean:write name="TraspasoDevolucionVO" property="fechaView"/>&nbsp;</td>
									<td><bean:write name="TraspasoDevolucionVO" property="recurso.desRecurso" />&nbsp;</td>
									
									<logic:equal name="traspasoDevolucionDeudaSearchPageVO" property="accionTraspasoDevolucion.esTraspaso" value="true">
										<td><bean:write name="TraspasoDevolucionVO" property="proOri.descripcion" />&nbsp;</td>
										<td><bean:write name="TraspasoDevolucionVO" property="proDes.descripcion"/>&nbsp;</td>
									</logic:equal>
									<logic:equal name="traspasoDevolucionDeudaSearchPageVO" property="accionTraspasoDevolucion.esDevolucion" value="true">
										<td><bean:write name="TraspasoDevolucionVO" property="procurador.descripcion" />&nbsp;</td>
										<td>&nbsp;</td>
									</logic:equal>
									<td><bean:write name="TraspasoDevolucionVO" property="cuenta.numeroCuenta"/>&nbsp;</td>
								</tr>
							</logic:iterate>
					
							<tr>
								<td class="paginador" align="center" colspan="20">
									<bean:define id="pager" name="traspasoDevolucionDeudaSearchPageVO"/>
									<%@ include file="/base/pager.jsp" %>
								</td>
							</tr>
							</tbody>
						</table>
					</logic:notEmpty>
			
					<logic:empty name="traspasoDevolucionDeudaSearchPageVO" property="listResult">
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
				<logic:equal name="traspasoDevolucionDeudaSearchPageVO" property="viewResult" value="true">
	  	    			<td align="right">
						<bean:define id="agregarEnabled" name="traspasoDevolucionDeudaSearchPageVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar', '0');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
					</td>
				</logic:equal>
			</tr>
		</table>
	
	</span>	
		
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclucion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->