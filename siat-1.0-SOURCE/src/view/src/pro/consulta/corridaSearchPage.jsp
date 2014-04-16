<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/pro/BuscarCorrida.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="pro" key="pro.corridaSearchPage.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p><bean:message bundle="pro" key="pro.corridaSearchPage.legend"/></p>
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
			<!-- Proceso -->
			<tr>
				<td><label><bean:message bundle="pro" key="pro.corrida.proceso.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="corridaSearchPageVO" property="corrida.proceso.id" styleClass="select" >
						<html:optionsCollection name="corridaSearchPageVO" property="listProceso" label="desProceso" value="id" />
					</html:select>
				</td>
		    </tr>
		    
		    <!-- Estado -->
   			<tr>
				<td><label><bean:message bundle="pro" key="pro.corrida.estadoCorrida.label"/>: </label></td>
				<td class="normal">
					<html:select name="corridaSearchPageVO" property="corrida.estadoCorrida.id" styleClass="select" >
						<html:optionsCollection name="corridaSearchPageVO" property="listEstadoCorrida" label="desEstadoCorrida" value="id" />
					</html:select>
				</td>
				
				<td><label><bean:message bundle="pro" key="pro.proceso.tipoEjecucion.label"/>: </label></td>
				<td class="normal">
					<html:select name="corridaSearchPageVO" property="corrida.proceso.tipoEjecucion.id" styleClass="select" >
						<html:optionsCollection name="corridaSearchPageVO" property="listTipoEjecucion" label="desTipoEjecucion" value="id" />
					</html:select>
				</td>
		    </tr>
		    
		    <!-- fecha Desde -->
			<tr>
				<td><label><bean:message bundle="pro" key="pro.corrida.fechaDesde.label"/>: </label></td>
				<td class="normal">
					<html:text name="corridaSearchPageVO" property="fechaDesdeView" styleId="fechaDesdeView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			
			<!-- fecha Hasta -->	
				<td><label><bean:message bundle="pro" key="pro.corrida.fechaHasta.label"/>: </label></td>
				<td class="normal">
					<html:text name="corridaSearchPageVO" property="fechaHastaView" styleId="fechaHastaView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>				
			</tr>
		</table>
			
		<p align="center">
			&nbsp;
		  	<html:button property="btnBuscar"  styleClass="boton" onclick="submitForm('buscar', '');">
				<bean:message bundle="base" key="abm.button.buscar"/>
			</html:button>
		</p>
	</fieldset>	
	<!-- Fin Filtro -->
		
	<!-- Resultado Filtro -->
	<div id="resultadoFiltro">
		<logic:equal name="corridaSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="corridaSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<th width="1">&nbsp;</th> <!-- Bajar Log -->
							<th align="left"><bean:message bundle="pro" key="pro.corrida.fechaInicio.label"/></th>
							<th align="left"><bean:message bundle="pro" key="pro.corrida.desCorrida.label"/></th>
							<th align="left"><bean:message bundle="pro" key="pro.corrida.nodo.label"/></th>
							<th align="left"><bean:message bundle="pro" key="pro.corrida.usuario.ref"/></th>
							<th align="left"><bean:message bundle="pro" key="pro.corrida.mensajeEstado.label"/></th>
							<th align="left"><bean:message bundle="pro" key="pro.corrida.estadoCorrida.label"/></th>
						</tr>
							
						<logic:iterate id="CorridaVO" name="corridaSearchPageVO" property="listResult">
							<tr>
								<!-- Seleccionar -->
								<logic:equal name="corridaSearchPageVO" property="modoSeleccionar" value="true">
									<td>	
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="CorridaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</td>
								</logic:equal>									
								<logic:notEqual name="corridaSearchPageVO" property="modoSeleccionar" value="true">
								<!-- Ver -->
									<td>
										<logic:equal name="corridaSearchPageVO" property="verEnabled" value="enabled">									
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="CorridaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="CorridaVO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</td>
									
									<!-- bajar Log -->
									
									<td>
										<logic:equal name="CorridaVO" property="verLogsEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitDownload('downloadLogFile', '<bean:write name="CorridaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="pro" key="pro.corridaSearchPage.button.bajarLog.label"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/corridaLog0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="CorridaVO" property="verLogsEnabled" value="enabled">
											<img title="<bean:message bundle="pro" key="pro.corridaSearchPage.button.bajarLog.label"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/corridaLog1.gif"/>
										</logic:notEqual>	
									</td>		
								</logic:notEqual>
								<td><bean:write name="CorridaVO" property="fechaInicioView"/>&nbsp;</td>
								<td><bean:write name="CorridaVO" property="desCorrida" />&nbsp;</td>
								<td><bean:write name="CorridaVO" property="nodoOwner" />&nbsp;</td>
								<td><bean:write name="CorridaVO" property="usuario" />&nbsp;</td>
								<td><bean:write name="CorridaVO" property="mensajeEstado" />&nbsp;</td>
								<td><bean:write name="CorridaVO" property="estadoCorrida.desEstadoCorrida" />&nbsp;</td>
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="corridaSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="corridaSearchPageVO" property="listResult">
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
			<logic:equal name="corridaSearchPageVO" property="viewResult" value="true">
			
			</logic:equal>
		</tr>
	</table>
		
	<input type="hidden" name="method" value=""/>
    <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
    <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="fileParam" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
