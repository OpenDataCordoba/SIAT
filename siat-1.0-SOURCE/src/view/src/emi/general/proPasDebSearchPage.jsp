<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/emi/BuscarProPasDeb.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="emi" key="emi.proPasDebSearchPage.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>
					<logic:equal name="proPasDebSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="base" key="base.busquedaLegendBusqueda"/>
						<bean:message bundle="emi" key="emi.proPasDeb.label"/>
					</logic:equal>
					<logic:notEqual name="proPasDebSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="emi" key="emi.proPasDebSearchPage.legend"/>
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
				<td><label><bean:message bundle="emi" key="emi.proPasDeb.recurso.label"/>: </label></td>
				<td class="normal">
					<html:select name="proPasDebSearchPageVO" property="proPasDeb.recurso.id" styleClass="select" >
						<bean:define id="includeRecursoList" name="proPasDebSearchPageVO" property="listRecurso"/>
						<bean:define id="includeIdRecursoSelected" name="proPasDebSearchPageVO" property="proPasDeb.recurso.id"/>
						<%@ include file="/def/gravamen/includeRecurso.jsp" %>
					</html:select>
				</td>
			</tr>

			<tr>
				<!-- Estado de la Corrida -->
				<td><label><bean:message bundle="emi" key="emi.proPasDeb.estadoCorrida.label"/>: </label></td>
				<td class="normal">
					<html:select name="proPasDebSearchPageVO" property="proPasDeb.corrida.estadoCorrida.id" styleClass="select">
						<html:optionsCollection name="proPasDebSearchPageVO" property="listEstadoCorrida" label="desEstadoCorrida" value="id" />
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
		<logic:equal name="proPasDebSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="proPasDebSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<logic:notEqual name="proPasDebSearchPageVO" property="modoSeleccionar" value="true">
								<th width="1">&nbsp;</th> <!-- Modificar -->
								<th width="1">&nbsp;</th> <!-- Eliminar -->
								<th width="1">&nbsp;</th> <!-- Administrar Proceso -->
							</logic:notEqual>
							<th align="left"><bean:message bundle="emi" key="emi.proPasDeb.recurso.ref"/></th>
							<th align="left"><bean:message bundle="emi" key="emi.proPasDeb.periodo.ref"/></th>
							<th align="left"><bean:message bundle="emi" key="emi.proPasDeb.fechaEnvio.ref"/></th>
							<th align="left"><bean:message bundle="emi" key="emi.proPasDeb.estadoCorrida.ref"/></th>
						</tr>
							
						<logic:iterate id="ProPasDebVO" name="proPasDebSearchPageVO" property="listResult">
							<tr>
								<!-- Seleccionar -->
								<logic:equal name="proPasDebSearchPageVO" property="modoSeleccionar" value="true">
									<td>	
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="ProPasDebVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</td>
								</logic:equal>									
								<logic:notEqual name="proPasDebSearchPageVO" property="modoSeleccionar" value="true">
									<!-- Ver -->
									<td>
										<logic:equal name="proPasDebSearchPageVO" property="verEnabled" value="enabled">									
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="ProPasDebVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="ProPasDebVO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</td>	
									<!-- Modificar-->								
									<td>
										<logic:equal name="proPasDebSearchPageVO" property="modificarEnabled" value="enabled">
											<logic:equal name="ProPasDebVO" property="modificarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="ProPasDebVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="ProPasDebVO" property="modificarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="proPasDebSearchPageVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</td>
	
									<!-- Eliminar-->								
									<td>
										<logic:equal name="proPasDebSearchPageVO" property="eliminarEnabled" value="enabled">
											<logic:equal name="ProPasDebVO" property="eliminarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="ProPasDebVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>
											</logic:equal>	
											<logic:notEqual name="ProPasDebVO" property="eliminarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="proPasDebSearchPageVO" property="eliminarEnabled" value="enabled">										
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</td>
									<!-- Administrar Proceso -->
									<td>
										<logic:equal name="proPasDebSearchPageVO" property="administrarProcesoEnabled" value="enabled">
											<logic:equal name="ProPasDebVO" property="administrarProcesoEnabled" value="true">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('administrarProceso', '<bean:write name="ProPasDebVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="emi" key="emi.proPasDebSearchPage.button.administrarProceso"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/admProceso0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="ProPasDebVO" property="administrarProcesoEnabled" value="true">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/admProceso1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="proPasDebSearchPageVO" property="administrarProcesoEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/admProceso1.gif"/>
										</logic:notEqual>
									</td>
								</logic:notEqual>
								<td>
									<bean:write name="ProPasDebVO" property="recurso.desRecurso"/><br/>
									<logic:notEmpty name="ProPasDebVO" property="atributo.desAtributo">
										(<bean:write name="ProPasDebVO" property="atrValor"/>)
									</logic:notEmpty>
								</td>
								<td>
									<bean:write name="ProPasDebVO" property="periodoView"/>/<bean:write name="ProPasDebVO" property="anioView"/>&nbsp;
								</td>
								<td><bean:write name="ProPasDebVO" property="fechaEnvioView"/></td>
								<td><bean:write name="ProPasDebVO" property="corrida.estadoCorrida.desEstadoCorrida"/>&nbsp;</td>
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="proPasDebSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="proPasDebSearchPageVO" property="listResult">
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
			<logic:equal name="proPasDebSearchPageVO" property="viewResult" value="true">
				<td align="right" width="50%">
  	    			<logic:equal name="proPasDebSearchPageVO" property="modoSeleccionar" value="false">
						<bean:define id="agregarEnabled" name="proPasDebSearchPageVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar', '0');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
					</logic:equal>
  	    			<logic:equal name="proPasDebSearchPageVO" property="modoSeleccionar" value="true">
  	    				<logic:equal name="proPasDebSearchPageVO" property="agregarEnSeleccion" value="true">
							<bean:define id="agregarEnabled" name="proPasDebSearchPageVO" property="agregarEnabled"/>
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
<!-- proPasDebSearchPage.jsp -->