<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/BuscarProcesoMasivo.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	
	<!-- Busqueda de Recuso -->
	<span id="blockBusqueda" style="display:none"> 
		<bean:define id="adapterVO" name="procesoMasivoSearchPageVO"/>
		<bean:define id="poseeParam" value="false" />
		<%@ include file="/def/gravamen/includeRecursoSearch.jsp" %>
	</span>

	<span id="blockSimple" style="display:block">
		
		<h1><bean:message bundle="gde" key="gde.procesoMasivoSearchPage.title"/></h1>	
			
		<p><bean:message bundle="gde" key="gde.procesoMasivoSearchPage.legend"/></p>
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
					<!-- tipo de proceso masivo -->			
					<tr>
						<td><label><bean:message bundle="gde" key="gde.procesoMasivo.tipProMas.label"/>: </label></td>
						<td class="normal" colspan="3">
							<bean:write name="procesoMasivoSearchPageVO" property="procesoMasivo.tipProMas.desTipProMas"/>
						</td>
					</tr>
					<!-- recurso -->
					<tr>
						<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:select name="procesoMasivoSearchPageVO" property="procesoMasivo.recurso.id" styleClass="select" styleId="cboRecurso" style="width:90%">
								<bean:define id="includeRecursoList"       name="procesoMasivoSearchPageVO" property="listRecurso"/>
								<bean:define id="includeIdRecursoSelected" name="procesoMasivoSearchPageVO" property="procesoMasivo.recurso.id"/>
								<%@ include file="/def/gravamen/includeRecurso.jsp" %>
							</html:select>
							
							<a onclick="toggleSearchRecurso('blockSimple', 'blockBusqueda'); setFocus('recursoFilter');" >
								<img title='<bean:message bundle="def" key="def.recurso.filtro.button.busqueda"/>' border="0" 
								src="<%=request.getContextPath()%>/images/iconos/lupita.gif" width="20" height="20">
							</a>
							
						</td>
					</tr>
					<!-- Id y corrida.estadoCorrida -->
					<tr>
						<td><label><bean:message bundle="gde" key="gde.procesoMasivo.id.label"/>: </label></td>
						<td class="normal">
							<html:text name="procesoMasivoSearchPageVO" property="procesoMasivo.idView" size="15" maxlength="10" />
						</td>
						<td><label><bean:message bundle="pro" key="pro.estadoCorrida.proceso.label"/>: </label></td>
						<td class="normal">
							<html:select name="procesoMasivoSearchPageVO" property="procesoMasivo.corrida.estadoCorrida.id" styleClass="select">
								<html:optionsCollection name="procesoMasivoSearchPageVO" 
									property="listEstadoCorrida" label="desEstadoCorrida" value="id" />
							</html:select>
						</td>
					</tr>
					<!-- fecha envio desde y fecha envio hasta -->				
					<tr>
						<td><label><bean:message bundle="gde" key="gde.procesoMasivoSearchPage.fechaEnvioDesde.label"/>: </label></td>
						<td class="normal">
							<html:text name="procesoMasivoSearchPageVO" property="fechaEnvioDesdeView" styleId="fechaEnvioDesdeView" size="15" maxlength="10" styleClass="datos" />
							<a class="link_siat" onclick="return show_calendar('fechaEnvioDesdeView');" id="a_fechaEnvioDesdeView">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>
						<td><label><bean:message bundle="gde" key="gde.procesoMasivoSearchPage.fechaEnvioHasta.label"/>: </label></td>
						<td class="normal">
							<html:text name="procesoMasivoSearchPageVO" property="fechaEnvioHastaView" styleId="fechaEnvioHastaView" size="15" maxlength="10" styleClass="datos" />
							<a class="link_siat" onclick="return show_calendar('fechaEnvioHastaView');" id="a_fechaEnvioHastaView">
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
			<logic:equal name="procesoMasivoSearchPageVO" property="viewResult" value="true">
				<logic:notEmpty  name="procesoMasivoSearchPageVO" property="listResult">	
					<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
						<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
		               	<tbody>
			               	<tr>
								<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
									<logic:notEqual name="procesoMasivoSearchPageVO" property="modoSeleccionar" value="true">
										<th width="1">&nbsp;</th> <!-- Modificar -->
										<th width="1">&nbsp;</th> <!-- Eliminar -->
										<th width="1">&nbsp;</th> <!-- Proceso -->
									</logic:notEqual>
								<th align="left"> 
									<logic:equal name="procesoMasivoSearchPageVO" property="procesoMasivo.tipProMas.esEnvioJudicial" value="true">
										<bean:message bundle="gde" key="gde.procesoMasivo.fechaEnvio.label"/>
									</logic:equal>
									<logic:equal name="procesoMasivoSearchPageVO" property="procesoMasivo.tipProMas.esPreEnvioJudicial" value="true">
										<bean:message bundle="gde" key="gde.procesoMasivo.fechaPreEnvio.abv"/>
									</logic:equal>
									<logic:equal name="procesoMasivoSearchPageVO" property="procesoMasivo.tipProMas.esReconfeccion" value="true">
										<bean:message bundle="gde" key="gde.procesoMasivo.fechaReconfeccion.abv"/>
									</logic:equal>
									<logic:equal name="procesoMasivoSearchPageVO" property="procesoMasivo.tipProMas.esSeleccionDeuda" value="true">
										<bean:message bundle="gde" key="gde.procesoMasivo.fechaSeleccionDeuda.abv"/>
									</logic:equal>
								</th>
								<th align="left"><bean:message bundle="gde" key="gde.procesoMasivo.id.label"/></th>
								<th align="left"><bean:message bundle="def" key="def.recurso.label"/></th>
								<th align="left"><bean:message bundle="pro" key="pro.estadoCorrida.proceso.label"/></th>
								<th align="left"><bean:message bundle="pro" key="pro.corrida.usuario.ref"/></th>
							</tr>
							<logic:iterate id="ProcesoMasivoVO" name="procesoMasivoSearchPageVO" property="listResult">
								<tr>
									<!-- Seleccionar -->
									<logic:equal name="procesoMasivoSearchPageVO" property="modoSeleccionar" value="true">
										<td>
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="ProcesoMasivoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</td>
									</logic:equal>									
										<logic:notEqual name="procesoMasivoSearchPageVO" property="modoSeleccionar" value="true">
											<!-- Ver -->
											<td>
												<logic:equal name="procesoMasivoSearchPageVO" property="verEnabled" value="enabled">
													<logic:equal name="ProcesoMasivoVO" property="verEnabled" value="enabled">
														<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="ProcesoMasivoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
															<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
														</a>
													</logic:equal>
													<logic:notEqual name="ProcesoMasivoVO" property="verEnabled" value="enabled">
														<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
													</logic:notEqual>
												</logic:equal>
												<logic:notEqual name="procesoMasivoSearchPageVO" property="verEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
												</logic:notEqual>
											</td>
											<!-- Modificar-->								
											<td>
												<logic:equal name="procesoMasivoSearchPageVO" property="modificarEnabled" value="enabled">
													<logic:equal name="ProcesoMasivoVO" property="modificarEnabled" value="enabled">
														<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="ProcesoMasivoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
															<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
														</a>
													</logic:equal>
													<logic:notEqual name="ProcesoMasivoVO" property="modificarEnabled" value="enabled">
														<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
													</logic:notEqual>
												</logic:equal>
												<logic:notEqual name="procesoMasivoSearchPageVO" property="modificarEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
												</logic:notEqual>
											</td>
											<!-- Eliminar-->								
											<td>
												<logic:equal name="procesoMasivoSearchPageVO" property="eliminarEnabled" value="enabled">
													<logic:equal name="ProcesoMasivoVO" property="eliminarEnabled" value="enabled">
														<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="ProcesoMasivoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
															<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
														</a>
													</logic:equal>	
													<logic:notEqual name="ProcesoMasivoVO" property="eliminarEnabled" value="enabled">
														<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
													</logic:notEqual>
												</logic:equal>
												<logic:notEqual name="procesoMasivoSearchPageVO" property="eliminarEnabled" value="enabled">										
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
												</logic:notEqual>
											</td>
											<!-- Proceso -->								
											<td>
												<logic:equal name="procesoMasivoSearchPageVO" property="admProcesoEnabled" value="enabled">
													<logic:equal name="ProcesoMasivoVO" property="admProcesoBussEnabled" value="true">
														<a style="cursor: pointer; cursor: hand;" onclick="submitForm('admProceso', '<bean:write name="ProcesoMasivoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
															<img title="<bean:message bundle="gde" key="gde.procesoMasivoSearchPage.adm.button.proceso"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/proceso0.gif"/>
														</a>
													</logic:equal>
													<logic:notEqual name="ProcesoMasivoVO" property="admProcesoBussEnabled" value="true">
														<img border="0" src="<%=request.getContextPath()%>/images/iconos/proceso1.gif"/>
													</logic:notEqual>
												</logic:equal>
												<logic:notEqual name="procesoMasivoSearchPageVO" property="admProcesoEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/proceso1.gif"/>
												</logic:notEqual>
											</td>
										</logic:notEqual>
		
										<td><bean:write name="ProcesoMasivoVO" property="fechaEnvioView"/>&nbsp;</td>
										<td><bean:write name="ProcesoMasivoVO" property="idView"/>&nbsp;</td>
										<td><bean:write name="ProcesoMasivoVO" property="recurso.desRecurso" />&nbsp;</td>
										<td><bean:write name="ProcesoMasivoVO" property="corrida.estadoCorrida.desEstadoCorrida"/>&nbsp;</td>
										<td><bean:write name="ProcesoMasivoVO" property="corrida.usuario"/>&nbsp;</td>									
								</tr>
							</logic:iterate>
							<tr>
								<td class="paginador" align="center" colspan="20">
									<bean:define id="pager" name="procesoMasivoSearchPageVO"/>
									<%@ include file="/base/pager.jsp" %>
								</td>
							</tr>
						</tbody>
					</table>
					
				</logic:notEmpty>
				<logic:empty name="procesoMasivoSearchPageVO" property="listResult">
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
				<logic:equal name="procesoMasivoSearchPageVO" property="viewResult" value="true">
	  	    			<td align="right">
						<bean:define id="agregarEnabled" name="procesoMasivoSearchPageVO" property="agregarEnabled"/>
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
	<input type="hidden" name="idTipProMas" value="<bean:write name="procesoMasivoSearchPageVO" property="procesoMasivo.tipProMas.idView"/>"/>

	<!-- Inclucion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>

</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
