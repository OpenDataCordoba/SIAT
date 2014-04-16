<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios  -->
<html:form styleId="filter" action="/emi/BuscarResLiqDeu.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="emi" key="emi.resLiqDeuSearchPage.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>
					<logic:equal name="resLiqDeuSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="base" key="base.busquedaLegendBusqueda"/>
						<bean:message bundle="emi" key="emi.resLiqDeu.label"/>
					</logic:equal>
					<logic:notEqual name="resLiqDeuSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="emi" key="emi.resLiqDeuSearchPage.legend"/>
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
				<!-- Recurso -->
				<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal">
					<html:select name="resLiqDeuSearchPageVO" property="resLiqDeu.recurso.id" styleClass="select" >
						<bean:define id="includeRecursoList" name="resLiqDeuSearchPageVO" property="listRecurso"/>
						<bean:define id="includeIdRecursoSelected" name="resLiqDeuSearchPageVO" property="resLiqDeu.recurso.id"/>
						<%@ include file="/def/gravamen/includeRecurso.jsp" %>
					</html:select>
				</td>
			</tr>

			<tr>
				<!-- Estado de la Corrida -->
				<td><label><bean:message bundle="emi" key="emi.resLiqDeu.estadoCorrida.label"/>: </label></td>
				<td class="normal">
					<html:select name="resLiqDeuSearchPageVO" property="resLiqDeu.corrida.estadoCorrida.id" styleClass="select">
						<html:optionsCollection name="resLiqDeuSearchPageVO" property="listEstadoCorrida" label="desEstadoCorrida" value="id" />
					</html:select>
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
		<logic:equal name="resLiqDeuSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="resLiqDeuSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<logic:notEqual name="resLiqDeuSearchPageVO" property="modoSeleccionar" value="true">
								<th width="1">&nbsp;</th> <!-- Modificar -->
								<th width="1">&nbsp;</th> <!-- Eliminar -->
								<th width="1">&nbsp;</th> <!-- Administrar Proceso -->
							</logic:notEqual>
							<th align="left"><bean:message bundle="emi" key="emi.resLiqDeu.recurso.label"/></th>
							<th align="left"><bean:message bundle="emi" key="emi.resLiqDeu.periodoDesde.label"/></th>
							<th align="left"><bean:message bundle="emi" key="emi.resLiqDeu.periodoHasta.label"/></th>
							<th align="left"><bean:message bundle="emi" key="emi.resLiqDeu.estadoCorrida.label"/></th>
						</tr>
							
						<logic:iterate id="ResLiqDeuVO" name="resLiqDeuSearchPageVO" property="listResult">
							<tr>
								<!-- Seleccionar -->
								<logic:equal name="resLiqDeuSearchPageVO" property="modoSeleccionar" value="true">
									<td>	
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="ResLiqDeuVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</td>
								</logic:equal>									
								<logic:notEqual name="resLiqDeuSearchPageVO" property="modoSeleccionar" value="true">
									<!-- Ver -->
									<td>
										<logic:equal name="resLiqDeuSearchPageVO" property="verEnabled" value="enabled">									
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="ResLiqDeuVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="ResLiqDeuVO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</td>	
									<!-- Modificar-->								
									<td>
										<logic:equal name="resLiqDeuSearchPageVO" property="modificarEnabled" value="enabled">
											<logic:equal name="ResLiqDeuVO" property="modificarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="ResLiqDeuVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="ResLiqDeuVO" property="modificarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="resLiqDeuSearchPageVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</td>
	
									<!-- Eliminar-->								
									<td>
										<logic:equal name="resLiqDeuSearchPageVO" property="eliminarEnabled" value="enabled">
											<logic:equal name="ResLiqDeuVO" property="eliminarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="ResLiqDeuVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>
											</logic:equal>	
											<logic:notEqual name="ResLiqDeuVO" property="eliminarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="resLiqDeuSearchPageVO" property="eliminarEnabled" value="enabled">										
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</td>
									<!-- Administrar Proceso -->
									<td>
										<logic:equal name="resLiqDeuSearchPageVO" property="administrarProcesoEnabled" value="enabled">
											<logic:equal name="ResLiqDeuVO" property="administrarProcesoEnabled" value="true">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('administrarProceso', '<bean:write name="ResLiqDeuVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="emi" key="emi.resLiqDeuSearchPage.button.administrarProceso"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/admProceso0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="ResLiqDeuVO" property="administrarProcesoEnabled" value="true">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/admProceso1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="resLiqDeuSearchPageVO" property="administrarProcesoEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/admProceso1.gif"/>
										</logic:notEqual>
									</td>
								</logic:notEqual>
								<td><bean:write name="ResLiqDeuVO" property="recurso.desRecurso"/>&nbsp;</td>

								<logic:notEqual name="ResLiqDeuVO" property="esAlfaxView" value="true">
									<td>
										<bean:write name="ResLiqDeuVO" property="periodoDesdeView"/>/<bean:write name="ResLiqDeuVO" property="anioView"/>&nbsp;
									</td>
									
									<td>
										<bean:write name="ResLiqDeuVO" property="periodoHastaView"/>/<bean:write name="ResLiqDeuVO" property="anioView"/>&nbsp;
									</td>
								</logic:notEqual>
								<logic:equal name="ResLiqDeuVO" property="esAlfaxView" value="true">
									<td> - </td>
									<td> - </td>
								</logic:equal>
								<td><bean:write name="ResLiqDeuVO" property="corrida.estadoCorrida.desEstadoCorrida"/>&nbsp;</td>
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="resLiqDeuSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="resLiqDeuSearchPageVO" property="listResult">
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
			<logic:equal name="resLiqDeuSearchPageVO" property="viewResult" value="true">
				<td align="right" width="50%">
  	    			<logic:equal name="resLiqDeuSearchPageVO" property="modoSeleccionar" value="false">
						<bean:define id="agregarEnabled" name="resLiqDeuSearchPageVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar', '0');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
					</logic:equal>
  	    			<logic:equal name="resLiqDeuSearchPageVO" property="modoSeleccionar" value="true">
  	    				<logic:equal name="resLiqDeuSearchPageVO" property="agregarEnSeleccion" value="true">
							<bean:define id="agregarEnabled" name="resLiqDeuSearchPageVO" property="agregarEnabled"/>
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

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
<!-- resLiqDeuSearchPage.jsp -->