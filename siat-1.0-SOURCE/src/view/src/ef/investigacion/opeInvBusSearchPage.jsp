<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/BuscarOpeInvBus.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	
	<logic:equal name="opeInvBusSearchPageVO" property="tipBus" value="1">	
		<h1><bean:message bundle="ef" key="ef.opeInvBusSearchPage.agregar.title"/></h1>	
	</logic:equal>	
	<logic:equal name="opeInvBusSearchPageVO" property="tipBus" value="2">	
		<h1><bean:message bundle="ef" key="ef.opeInvBusSearchPage.eliminar.title"/></h1>	
	</logic:equal>	
				
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left"><p><bean:message bundle="ef" key="ef.opeInvBusSearchPage.legend"/></p></td>				
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
				<td><label><bean:message bundle="ef" key="ef.opeInvBusSearchPage.estado.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="opeInvBusSearchPageVO" property="estadoCorrida.id" styleClass="select">
						<html:optionsCollection name="opeInvBusSearchPageVO" property="listEstadoCorridaVO" label="desEstadoCorrida" value="id"/>
					</html:select>
				</td>					
			</tr>
			
			<tr>	
				<td><label><bean:message bundle="ef" key="ef.opeInvBusSearchPage.fechaDesde.label"/>: </label></td>
				<td class="normal">
					<html:text name="opeInvBusSearchPageVO" property="fechaDesdeView" styleId="fechaDesdeView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>					
				
				<td><label><bean:message bundle="ef" key="ef.opeInvBusSearchPage.fechaHasta.label"/>: </label></td>
				<td class="normal">
					<html:text name="opeInvBusSearchPageVO" property="fechaHastaView" styleId="fechaHastaView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>					
			</tr>

			<!-- <#Filtros#> -->
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
	<logic:equal name="opeInvBusSearchPageVO" property="viewResult" value="true">
		<div id="resultadoFiltro">
				<logic:notEmpty  name="opeInvBusSearchPageVO" property="listResult">	
					<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
						<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
		               	<tbody>
			               	<tr>
								<th width="1">&nbsp;</th> <!-- Ver -->
									<th width="1">&nbsp;</th> <!-- Modificar -->
									<th width="1">&nbsp;</th> <!-- Eliminar -->
									<th width="1">&nbsp;</th> <!-- ADM proceso -->
									<th><bean:message bundle="ef" key="ef.opeInvBus.fechaBusqueda.label"/></th> <!-- Fecha Busqueda -->
									<th><bean:message bundle="ef" key="ef.opeInvBus.desOpeInvBus.label"/></th> <!-- Descripcion -->
									<th align="left"><bean:message bundle="base" key="base.estado.label"/></th> <!-- Estado corrida -->
								<!-- <#ColumnTitles#> -->
							</tr>
								
							<logic:iterate id="OpeInvBusVO" name="opeInvBusSearchPageVO" property="listResult">
								<tr>
										<!-- Ver -->
										<td>
											<logic:equal name="opeInvBusSearchPageVO" property="verEnabled" value="enabled">									
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="OpeInvBusVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="opeInvBusSearchPageVO" property="verEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
											</logic:notEqual>
										</td>	
										<!-- Modificar-->								
										<td>
											<logic:equal name="opeInvBusSearchPageVO" property="modificarEnabled" value="enabled">
												<logic:equal name="OpeInvBusVO" property="modificarEnabled" value="enabled">
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="OpeInvBusVO" property="id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
													</a>
												</logic:equal>
												<logic:notEqual name="OpeInvBusVO" property="modificarEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
												</logic:notEqual>
											</logic:equal>
											<logic:notEqual name="opeInvBusSearchPageVO" property="modificarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
											</logic:notEqual>
										</td>
		
										<!-- Eliminar-->								
										<td>
											<logic:equal name="opeInvBusSearchPageVO" property="eliminarEnabled" value="enabled">
												<logic:equal name="OpeInvBusVO" property="eliminarBussEnabled" value="true">
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="OpeInvBusVO" property="id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
													</a>
												</logic:equal>	
												<logic:notEqual name="OpeInvBusVO" property="eliminarBussEnabled" value="true">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
												</logic:notEqual>
											</logic:equal>
											<logic:notEqual name="opeInvBusSearchPageVO" property="eliminarEnabled" value="enabled">										
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
											</logic:notEqual>
										</td>
										<!-- Accion administrarProceso -->
										<td>
											<logic:equal name="opeInvBusSearchPageVO" property="administrarProcesoEnabled" value="enabled">
												<logic:equal name="OpeInvBusVO" property="administrarProcesoBussEnabled" value="true">
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('administrarProceso', '<bean:write name="OpeInvBusVO" property="id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="gde" key="gde.liqComSearchPage.button.administrarProceso"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/admProceso0.gif"/>
													</a>
												</logic:equal>
												<logic:notEqual name="OpeInvBusVO" property="administrarProcesoBussEnabled" value="true">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/admProceso1.gif"/>
												</logic:notEqual>
											</logic:equal>
											<logic:notEqual name="opeInvBusSearchPageVO" property="administrarProcesoEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/admProceso1.gif"/>
											</logic:notEqual>
										</td>
	
									<!-- <#ColumnFiedls#> -->
									<td><bean:write name="OpeInvBusVO" property="fechaBusquedaView"/></td>
									<td><bean:write name="OpeInvBusVO" property="descripcion"/></td>
									<td><bean:write name="OpeInvBusVO" property="corrida.estadoCorrida.desEstadoCorrida"/></td>
								</tr>
							</logic:iterate>
					
							<tr>
								<td class="paginador" align="center" colspan="20">
									<bean:define id="pager" name="opeInvBusSearchPageVO"/>
									<%@ include file="/base/pager.jsp" %>
								</td>
							</tr>
							
						</tbody>
					</table>
				</logic:notEmpty>
				
				<logic:empty name="opeInvBusSearchPageVO" property="listResult">
					<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
						<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	                	<tbody>
							<tr><td align="center">
								<bean:message bundle="base" key="base.resultadoVacio"/>
							</td></tr>
						</tbody>			
					</table>
				</logic:empty>			
		</div>
	<!-- Fin Resultado Filtro -->

		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left" width="50%">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
	  	    			<logic:equal name="opeInvBusSearchPageVO" property="modoSeleccionar" value="false">
							<bean:define id="agregarEnabled" name="opeInvBusSearchPageVO" property="agregarEnabled"/>
							<input type="button" <%=agregarEnabled%> class="boton" 
								onClick="submitForm('agregar', '0');" 
								value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
						</logic:equal>
	  	    			<logic:equal name="opeInvBusSearchPageVO" property="modoSeleccionar" value="true">
	  	    				<logic:equal name="opeInvBusSearchPageVO" property="agregarEnSeleccion" value="true">
								<bean:define id="agregarEnabled" name="opeInvBusSearchPageVO" property="agregarEnabled"/>
								<input type="button" <%=agregarEnabled%> class="boton" 
									onClick="submitForm('agregar', '0');" 
									value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
								</logic:equal>
						</logic:equal>				
				</td>
			</tr>
		</table>
	</logic:equal>	
		
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
