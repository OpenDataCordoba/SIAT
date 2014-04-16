<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/rec/BuscarAnulacionObra.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="rec" key="rec.anulacionObraSearchPage.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>
					<logic:equal name="anulacionObraSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="base" key="base.busquedaLegendBusqueda"/>
						<bean:message bundle="rec" key="rec.anulacionObra.label"/>
					</logic:equal>
					<logic:notEqual name="anulacionObraSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="rec" key="rec.anulacionObraSearchPage.legend"/>
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
				<!-- Lista de Obras -->	
				<td><label><bean:message bundle="rec" key="rec.anulacionObra.obra.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="anulacionObraSearchPageVO" property="anulacionObra.obra.id" styleClass="select">
						<html:optionsCollection name="anulacionObraSearchPageVO" property="listObra" label="desObraConNumeroObra" value="id" />
					</html:select>
				</td>					
			</tr>

			<tr>
				<!-- Estado de la Corrida -->	
				<td><label><bean:message bundle="pro" key="pro.estadoCorrida.label"/>: </label></td>
				<td class="normal">
					<html:select name="anulacionObraSearchPageVO" property="anulacionObra.corrida.estadoCorrida.id" styleClass="select">
						<html:optionsCollection name="anulacionObraSearchPageVO" property="listEstadoCorrida" label="desEstadoCorrida" value="id" />
					</html:select>
				</td>					
			</tr>

			<tr>
				<!-- Fecha Desde -->
				<td><label><bean:message bundle="rec" key="rec.anulacionObraSearchPage.fechaDesde.label"/>: </label></td>
				<td class="normal">
					<html:text name="anulacionObraSearchPageVO" property="fechaDesdeView" styleId="fechaDesdeView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
				
				<!-- Fecha Hasta -->
				<td><label><bean:message bundle="rec" key="rec.anulacionObraSearchPage.fechaHasta.label"/>: </label></td>
				<td class="normal">
					<html:text name="anulacionObraSearchPageVO" property="fechaHastaView" styleId="fechaHastaView" size="15" maxlength="10" styleClass="datos" />
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
		<logic:equal name="anulacionObraSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="anulacionObraSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<logic:notEqual name="anulacionObraSearchPageVO" property="modoSeleccionar" value="true">
								<th width="1">&nbsp;</th> <!-- Modificar -->
								<th width="1">&nbsp;</th> <!-- Eliminar -->
								<th width="1">&nbsp;</th> <!-- Administrar Proceso -->
							</logic:notEqual>
							<th align="left"><bean:message bundle="rec" key="rec.anulacionObra.fechaAnulacion.label"/></th>
							<th align="left"><bean:message bundle="rec" key="rec.anulacionObra.obra.label"/></th>
							<th align="left"><bean:message bundle="rec" key="rec.anulacionObra.observacion.label"/></th>
						</tr>
							
						<logic:iterate id="AnulacionObraVO" name="anulacionObraSearchPageVO" property="listResult">
							<tr>
								<!-- Seleccionar -->
								<logic:equal name="anulacionObraSearchPageVO" property="modoSeleccionar" value="true">
									<td>	
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="AnulacionObraVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</td>
								</logic:equal>									
								<logic:notEqual name="anulacionObraSearchPageVO" property="modoSeleccionar" value="true">
									<!-- Ver -->
									<td>
										<logic:equal name="anulacionObraSearchPageVO" property="verEnabled" value="enabled">									
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="AnulacionObraVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="AnulacionObraVO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</td>	
									<!-- Modificar-->								
									<td>
										<logic:equal name="anulacionObraSearchPageVO" property="modificarEnabled" value="enabled">
											<logic:equal name="AnulacionObraVO" property="modificarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="AnulacionObraVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="AnulacionObraVO" property="modificarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="anulacionObraSearchPageVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</td>
	
									<!-- Eliminar-->								
									<td>
										<logic:equal name="anulacionObraSearchPageVO" property="eliminarEnabled" value="enabled">
											<logic:equal name="AnulacionObraVO" property="eliminarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="AnulacionObraVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>
											</logic:equal>	
											<logic:notEqual name="AnulacionObraVO" property="eliminarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="anulacionObraSearchPageVO" property="eliminarEnabled" value="enabled">										
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</td>

									<!-- Administrar Proceso  -->
									<td>
										<logic:equal name="anulacionObraSearchPageVO" property="administrarProcesoEnabled" value="enabled">
											<logic:equal name="AnulacionObraVO" property="administrarProcesoBussEnabled" value="true">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('administrarProceso', '<bean:write name="AnulacionObraVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="emi" key="emi.emisionSearchPage.button.administrarProceso"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/admProceso0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="AnulacionObraVO" property="administrarProcesoBussEnabled" value="true">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/admProceso1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="anulacionObraSearchPageVO" property="administrarProcesoEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/admProceso1.gif"/>
										</logic:notEqual>
									</td>
								</logic:notEqual>
								<td><bean:write name="AnulacionObraVO" property="fechaAnulacionView" />&nbsp;</td>
								<td><bean:write name="AnulacionObraVO" property="obra.desObra"/>&nbsp;</td>
								<td><bean:write name="AnulacionObraVO" property="observacion"/>&nbsp;</td>
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="anulacionObraSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="anulacionObraSearchPageVO" property="listResult">
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
			<logic:equal name="anulacionObraSearchPageVO" property="viewResult" value="true">
				<td align="right" width="50%">
  	    			<logic:equal name="anulacionObraSearchPageVO" property="modoSeleccionar" value="false">
						<bean:define id="agregarEnabled" name="anulacionObraSearchPageVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar', '0');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
					</logic:equal>
  	    			<logic:equal name="anulacionObraSearchPageVO" property="modoSeleccionar" value="true">
  	    				<logic:equal name="anulacionObraSearchPageVO" property="agregarEnSeleccion" value="true">
							<bean:define id="agregarEnabled" name="anulacionObraSearchPageVO" property="agregarEnabled"/>
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
