<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/BuscarSalPorCad.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>

	<!-- Busqueda de Recuso -->
	<span id="blockBusqueda" style="display:none"> 
		<bean:define id="adapterVO" name="saldoPorCaducidadSearchPageVO"/>
		<bean:define id="poseeParam" value="true" />
		<%@ include file="/def/gravamen/includeRecursoSearch.jsp" %>
	</span>

	<span id="blockSimple" style="display:block">
			
		<h1><bean:message bundle="gde" key="gde.saldoPorCaducidadSearchPage.title"/></h1>	
			
		<p><bean:message bundle="gde" key="gde.saldoPorCaducidadSearchPage.legend"/></p>
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
					<!-- combo Recurso -->
					<tr>
						<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:select name="saldoPorCaducidadSearchPageVO" property="idRecursoSelected" styleClass="select" onchange="submitForm('paramRecurso','');" styleId="cboRecurso" style="width:90%">
								<html:optionsCollection  name="saldoPorCaducidadSearchPageVO" property="listRecurso" label="desRecurso" value="id"/>
							</html:select>
							
							<a onclick="toggleSearchRecurso('blockSimple', 'blockBusqueda'); setFocus('recursoFilter');" >
								<img title='<bean:message bundle="def" key="def.recurso.filtro.button.busqueda"/>' border="0" 
								src="<%=request.getContextPath()%>/images/iconos/lupita.gif" width="20" height="20">
							</a>
							
						</td>
					</tr>
					<!-- combo Plan -->
					<tr>
						<td><label><bean:message bundle="gde" key="gde.plan.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:select name="saldoPorCaducidadSearchPageVO" property="saldoPorCaducidad.plan.id" styleClass="select">
								<html:optionsCollection name="saldoPorCaducidadSearchPageVO" 
									property="listPlan" label="desPlan" value="id" />
							</html:select>
						</td>
					</tr>
					<!-- fechaDesde y fechaHasta -->
					<tr>
						<td><label><bean:message bundle="gde" key="gde.salPorCadSearchPage.fechaDesde.label"/>: </label></td>
						<td class="normal">
							<html:text name="saldoPorCaducidadSearchPageVO" property="fechaDesdeView" styleId="fechaDesdeView" size="15" maxlength="10" styleClass="datos" onchange="limpiaResultadoFiltro();"/>
							<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>
						<td><label><bean:message bundle="gde" key="gde.salPorCadSearchPage.fechaHasta.label"/>: </label></td>
						<td class="normal">
							<html:text name="saldoPorCaducidadSearchPageVO" property="fechaHastaView" styleId="fechaHastaView" size="15" maxlength="10" styleClass="datos" onchange="limpiaResultadoFiltro();"/>
							<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>				
					</tr>
					<!-- combo Estado Proceso -->
					<tr>
						<td><label><bean:message bundle="pro" key="pro.estadoCorrida.proceso.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:select name="saldoPorCaducidadSearchPageVO" property="saldoPorCaducidad.corrida.estadoCorrida.id" styleClass="select">
								<html:optionsCollection name="saldoPorCaducidadSearchPageVO" 
									property="listEstadoCorrida" label="desEstadoCorrida" value="id" />
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
			<logic:equal name="saldoPorCaducidadSearchPageVO" property="viewResult" value="true">
				<logic:notEmpty  name="saldoPorCaducidadSearchPageVO" property="listResult">	
					<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
						<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
		               	<tbody>
			               	<tr>
								<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
									<logic:notEqual name="saldoPorCaducidadSearchPageVO" property="modoSeleccionar" value="true">
										<th width="1">&nbsp;</th> <!-- Modificar -->
										<th width="1">&nbsp;</th> <!-- Eliminar -->
										<th width="1">&nbsp;</th> <!-- admSeleccion -->
										<th width="1">&nbsp;</th> <!-- admProceso -->
									</logic:notEqual>
								<th align="left"><bean:message bundle="gde" key="gde.salPorCad.fechaSalCad"/></th>
								<th align="left"><bean:message bundle="def" key="def.recurso.label"/></th>
								<th align="left"><bean:message bundle="gde" key="gde.plan.label"/></th>
								<th align="left"><bean:message bundle="base" key="base.estado.label"/></th>
							</tr>
							<logic:iterate id="SaldoVO" name="saldoPorCaducidadSearchPageVO" property="listResult">
								<tr>
									<!-- Ver -->
									<td>
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="SaldoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</td>
									<!-- Modificar -->
									<logic:equal name="SaldoVO" property="modificaActivo" value="true">
										<td>
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="SaldoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
											</a>
										</td>
									</logic:equal>
									<logic:notEqual name="SaldoVO" property="modificaActivo" value="true">
										<td><img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/></td>
									</logic:notEqual>
									
									<!-- Eliminar -->
									<logic:equal name="SaldoVO" property="eliminaActivo" value="true">
										<td>
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="SaldoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
											</a>
										</td>
									</logic:equal>
									<logic:notEqual name="SaldoVO" property="eliminaActivo" value="true">
										<td><img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/></td>
									</logic:notEqual>
									
									<!-- Seleccionar -->
									<logic:equal name="SaldoVO" property="seleccionaActivo" value="true">
										<td>
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="SaldoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
								 			</a>
								 		</td>
								 	</logic:equal>
								 	<logic:notEqual name="SaldoVO" property="seleccionaActivo" value="true">
								 		<td><img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/></td>
								 	</logic:notEqual>
								 	
								 	<!-- Administrar Proceso -->
								 	<logic:equal name="SaldoVO" property="administraActivo" value="true">
								 		<td>
								 			<a style="cursor: pointer; cursor: hand;" onclick="submitForm('administrar', '<bean:write name="SaldoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="gde" key="gde.salPorCadMasivoAdminAdapter.adm.button.proceso"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/admProceso0.gif"/>
											</a>
										</td>
									</logic:equal>
									<logic:notEqual name="SaldoVO" property="administraActivo" value="true">
										<td><img border="0" src="<%=request.getContextPath()%>/images/iconos/admProceso0.gif"/></td>
									</logic:notEqual>
									<td><bean:write name="SaldoVO" property="fechaSaldoView"/>&nbsp;</td>
									<td><bean:write name="SaldoVO" property="recurso.desRecurso"/>&nbsp;</td>
									<td><bean:write name="SaldoVO" property="plan.desPlan" />&nbsp;</td>
									<td><bean:write name="SaldoVO" property="corrida.estadoCorrida.desEstadoCorrida"/>&nbsp;</td>
								</tr>
							</logic:iterate>
							<tr>
								<td class="paginador" align="center" colspan="20">
									<bean:define id="pager" name="saldoPorCaducidadSearchPageVO"/>
									<%@ include file="/base/pager.jsp" %>
								</td>
							</tr>
						</tbody>
					</table>
					
				</logic:notEmpty>
				<logic:empty name="saldoPorCaducidadSearchPageVO" property="listResult">
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
				<logic:equal name="saldoPorCaducidadSearchPageVO" property="viewResult" value="true">
	  	    			<td align="right">
							<input type="button" class="boton" 
							onClick="submitForm('agregar', '0');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						
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