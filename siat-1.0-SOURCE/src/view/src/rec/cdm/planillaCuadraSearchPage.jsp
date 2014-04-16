<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/rec/BuscarPlanillaCuadra.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="rec" key="rec.planillaCuadraSearchPage.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p><bean:message bundle="rec" key="rec.planillaCuadraSearchPage.legend"/></p>
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
				<!-- Recurso -->
				<tr>
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<logic:equal name="planillaCuadraSearchPageVO" property="seleccionRecursoBussEnabled" value="true">		
						<td class="normal" colspan="3">
							<html:select name="planillaCuadraSearchPageVO" property="planillaCuadra.recurso.id" styleClass="select" onchange="submitForm('paramRecurso', '');">
								<bean:define id="includeRecursoList" name="planillaCuadraSearchPageVO" property="listRecurso"/>
								<bean:define id="includeIdRecursoSelected" name="planillaCuadraSearchPageVO" property="planillaCuadra.idRecurso"/>
								<%@ include file="/def/gravamen/includeRecurso.jsp" %>
							</html:select>
						</td>				
					</logic:equal>
					<logic:equal name="planillaCuadraSearchPageVO" property="seleccionRecursoBussEnabled" value="false">		
						<td class="normal" colspan="3">
							<bean:write name="planillaCuadraSearchPageVO" property="planillaCuadra.recurso.desRecurso"/>
						</td>				
					</logic:equal>
				</tr>
				<!-- Contrato -->
				<tr>	
					<td><label><bean:message bundle="rec" key="rec.contrato.label"/>: </label></td>
					<td class="normal">
						<html:select name="planillaCuadraSearchPageVO" property="planillaCuadra.contrato.id" styleClass="select" onchange="submitForm('paramContrato', '');">
							<html:optionsCollection name="planillaCuadraSearchPageVO" property="listContrato" label="descripcion" value="id" />
						</html:select>
					</td>					
				</tr>
				<!-- TipoObra -->
				<tr>	
					<td><label><bean:message bundle="rec" key="rec.tipoObra.label"/>: </label></td>
					<td class="normal">
						<html:select name="planillaCuadraSearchPageVO" property="planillaCuadra.tipoObra.id" styleClass="select" onchange="submitForm('paramTipoObra', '');">
							<html:optionsCollection name="planillaCuadraSearchPageVO" property="listTipoObra" label="desTipoObra" value="id" />
						</html:select>
					</td>					
				</tr>
				<!-- Nro planilla -->			
				<tr>
					<td><label><bean:message bundle="rec" key="rec.planillaCuadra.numeroPlanilla.label"/>: </label></td>
					<td class="normal"><html:text name="planillaCuadraSearchPageVO" property="planillaCuadra.idView" size="15" maxlength="20" styleClass="datos" /></td>
				</tr>
				<!-- Descripcion -->
				<tr>
					<td><label><bean:message bundle="rec" key="rec.planillaCuadra.descripcion.label"/>: </label></td>
					<td class="normal"><html:text name="planillaCuadraSearchPageVO" property="planillaCuadra.descripcion" size="40" maxlength="100" styleClass="datos" /></td>
				</tr>
				<!-- Estado planilla -->
				<tr>	
					<td><label><bean:message bundle="rec" key="rec.estPlaCua.label"/>: </label></td>
					<td class="normal">
						<html:select name="planillaCuadraSearchPageVO" property="planillaCuadra.estPlaCua.id" styleClass="select">
							<html:optionsCollection name="planillaCuadraSearchPageVO" property="listEstPlaCua" label="desEstPlaCua" value="id" />
						</html:select>
					</td>					
				</tr>
				<!-- Calle Principal -->
				<tr>
					<td><label><bean:message bundle="rec" key="rec.planillaCuadra.callePpal.label"/>: </label></td>
					<td class="normal" colspan="3"><html:text name="planillaCuadraSearchPageVO" property="planillaCuadra.callePpal.nombreCalle" size="20" maxlength="100" styleClass="datos" disabled="true"/>
						<html:button property="btnBuscarCallePpal"  styleClass="boton" onclick="submitForm('buscarCalle', '');">
							<bean:message bundle="rec" key="rec.planillaCuadraEditAdapter.button.buscarCalle"/>
						</html:button>
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
		<logic:equal name="planillaCuadraSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="planillaCuadraSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<logic:notEqual name="planillaCuadraSearchPageVO" property="modoSeleccionar" value="true">
								<th width="1">&nbsp;</th> <!-- Modificar -->
								<th width="1">&nbsp;</th> <!-- Informacion Catastral -->								
								<th width="1">&nbsp;</th> <!-- Eliminar -->
								<th width="1">&nbsp;</th> <!-- Cambiar estado -->
							</logic:notEqual>
							<th align="left"><bean:message bundle="rec" key="rec.planillaCuadra.numeroPlanilla.label"/></th>
							<th align="left"><bean:message bundle="rec" key="rec.tipoObra.label"/></th>
							<th align="left"><bean:message bundle="rec" key="rec.planillaCuadra.descripcion.label"/></th>							
							<th align="left"><bean:message bundle="base" key="base.estado.label"/></th>
						</tr>
							
						<logic:iterate id="PlanillaCuadraVO" name="planillaCuadraSearchPageVO" property="listResult" indexId="count">
							<tr>
								<!-- Seleccionar -->
								<logic:equal name="planillaCuadraSearchPageVO" property="modoSeleccionar" value="true">
									<logic:equal name="planillaCuadraSearchPageVO" property="isMultiselect" value="true">
										<logic:equal name="PlanillaCuadraVO" property="seleccionarBussEnabled" value="true">
											<td>
												<html:multibox name="planillaCuadraSearchPageVO" property="listId">
													<bean:write name="PlanillaCuadraVO" property="idView"/>
												</html:multibox>	
											</td>
										</logic:equal>
										<logic:equal name="PlanillaCuadraVO" property="seleccionarBussEnabled" value="false">
											<td>
												<bean:write name="PlanillaCuadraVO" property="leyenda"/>
											</td>
										</logic:equal>
									</logic:equal>										
									<logic:equal name="planillaCuadraSearchPageVO" property="isMultiselect" value="false">
										<td>	
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="PlanillaCuadraVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</td>
									</logic:equal>
								</logic:equal>									
								<logic:notEqual name="planillaCuadraSearchPageVO" property="modoSeleccionar" value="true">
									<!-- Ver -->
									<td>
										<logic:equal name="planillaCuadraSearchPageVO" property="verEnabled" value="enabled">									
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="PlanillaCuadraVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="PlanillaCuadraVO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</td>	
									<!-- Modificar-->								
									<td>
										<logic:equal name="planillaCuadraSearchPageVO" property="modificarEnabled" value="enabled">
											<logic:equal name="PlanillaCuadraVO" property="modificarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="PlanillaCuadraVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="PlanillaCuadraVO" property="modificarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="planillaCuadraSearchPageVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</td>
									
									<!-- Accion informarCatastrales -->
									<td>
										<logic:equal name="planillaCuadraSearchPageVO" property="informarCatastralesEnabled" value="enabled">
											<logic:equal name="PlanillaCuadraVO" property="informarCatastralesBussEnabled" value="true">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('informarCatastrales', '<bean:write name="PlanillaCuadraVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="rec" key="rec.planillaCuadraSearchPage.button.informarCatastrales"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/house0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="PlanillaCuadraVO" property="informarCatastralesBussEnabled" value="true">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/house1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="planillaCuadraSearchPageVO" property="informarCatastralesEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/house1.gif"/>
										</logic:notEqual>
									</td>
									<!-- Eliminar-->								
									<td>
										<logic:equal name="planillaCuadraSearchPageVO" property="eliminarEnabled" value="enabled">
											<logic:equal name="PlanillaCuadraVO" property="eliminarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="PlanillaCuadraVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>
											</logic:equal>	
											<logic:notEqual name="PlanillaCuadraVO" property="eliminarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="planillaCuadraSearchPageVO" property="eliminarEnabled" value="enabled">										
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</td>

									<!-- Accion cambiarEstado -->
									<td>
										<logic:equal name="planillaCuadraSearchPageVO" property="cambiarEstadoEnabled" value="enabled">
											<logic:equal name="PlanillaCuadraVO" property="cambiarEstadoBussEnabled" value="true">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('cambiarEstado', '<bean:write name="PlanillaCuadraVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="rec" key="rec.planillaCuadraSearchPage.button.cambiarEstado"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/cambiarEstado0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="PlanillaCuadraVO" property="cambiarEstadoBussEnabled" value="true">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/cambiarEstado1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="planillaCuadraSearchPageVO" property="cambiarEstadoEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/cambiarEstado1.gif"/>
										</logic:notEqual>
									</td>
								</logic:notEqual>

								<td><bean:write name="PlanillaCuadraVO" property="idView"/>&nbsp;</td>
								<td><bean:write name="PlanillaCuadraVO" property="tipoObra.desTipoObra" />&nbsp;</td>
								<td><bean:write name="PlanillaCuadraVO" property="descripcion" />&nbsp;</td>								
								<td><bean:write name="PlanillaCuadraVO" property="estPlaCua.desEstPlaCua" />&nbsp;</td>
							
							</tr>
						</logic:iterate>

						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="planillaCuadraSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="planillaCuadraSearchPageVO" property="listResult">
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
			<td align="left" width="50%">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<logic:equal name="planillaCuadraSearchPageVO" property="viewResult" value="true">
				<td align="right" width="50%">
  	    			<logic:equal name="planillaCuadraSearchPageVO" property="modoSeleccionar" value="false">
						<bean:define id="agregarEnabled" name="planillaCuadraSearchPageVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar', '0');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
					</logic:equal>
  	    			<logic:equal name="planillaCuadraSearchPageVO" property="modoSeleccionar" value="true">
  	    				<logic:equal name="planillaCuadraSearchPageVO" property="agregarEnSeleccion" value="true">
							<bean:define id="agregarEnabled" name="planillaCuadraSearchPageVO" property="agregarEnabled"/>
							<input type="button" <%=agregarEnabled%> class="boton" 
								onClick="submitForm('agregar', '0');" 
								value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
						</logic:equal>
						<logic:equal name="planillaCuadraSearchPageVO" property="isMultiselect" value="true">
							<input type="button" class="boton"	onClick="submitForm('agregarAObra', '0');" 
								value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
						</logic:equal>						
					</logic:equal>
				</td>				
			</logic:equal>
		</tr>
	</table>
	
	<input type="hidden" name="name" value="<bean:write name='planillaCuadraSearchPageVO' property='name'/>" id="name"/>
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