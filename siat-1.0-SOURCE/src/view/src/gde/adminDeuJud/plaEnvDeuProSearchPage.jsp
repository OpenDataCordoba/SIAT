<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/BuscarPlaEnvDeuPro.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>

	<!-- Busqueda de Recuso -->
	<span id="blockBusqueda" style="display:none"> 
		<bean:define id="adapterVO" name="plaEnvDeuProSearchPageVO"/>
		<bean:define id="poseeParam" value="true" />
		<%@ include file="/def/gravamen/includeRecursoSearch.jsp" %>
	</span>

	<span id="blockSimple" style="display:block">
		
		<h1><bean:message bundle="gde" key="gde.plaEnvDeuProSearchPage.title"/></h1>	
			
		<p>
			<logic:equal name="plaEnvDeuProSearchPageVO" property="modoSeleccionar" value="true">
				<bean:message bundle="base" key="base.busquedaLegendBusqueda"/>
				<bean:message bundle="gde" key="gde.plaEnvDeuPro.label"/>
			</logic:equal>
			<logic:notEqual name="plaEnvDeuProSearchPageVO" property="modoSeleccionar" value="true">
				<bean:message bundle="gde" key="gde.plaEnvDeuProSearchPage.legend"/>
			</logic:notEqual>		
		</p>
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
			
			<!-- Recurso -->
			<tr>	
				<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="plaEnvDeuProSearchPageVO" property="plaEnvDeuPro.procesoMasivo.recurso.id" styleClass="select" onchange="submitForm('paramRecurso', '');" styleId="cboRecurso" style="width:90%">
						<bean:define id="includeRecursoList" name="plaEnvDeuProSearchPageVO" property="listRecurso"/>
						<bean:define id="includeIdRecursoSelected" name="plaEnvDeuProSearchPageVO" property="plaEnvDeuPro.procesoMasivo.recurso.id"/>
						<%@ include file="/def/gravamen/includeRecurso.jsp" %>
					</html:select>
					
					<a onclick="toggleSearchRecurso('blockSimple', 'blockBusqueda'); setFocus('recursoFilter');" >
						<img title='<bean:message bundle="def" key="def.recurso.filtro.button.busqueda"/>' border="0" 
						src="<%=request.getContextPath()%>/images/iconos/lupita.gif" width="20" height="20">
					</a>
					
				</td>		
			</tr>
			
			<!-- Procurador -->
			<tr>	
				<td><label><bean:message bundle="gde" key="gde.procurador.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="plaEnvDeuProSearchPageVO" property="plaEnvDeuPro.procurador.id" styleClass="select">
						<html:optionsCollection name="plaEnvDeuProSearchPageVO" property="listProcurador" label="descripcion" value="id" />
					</html:select>
				</td>					
			</tr>
			
			<!-- nroPlanilla y AnioPlanilla -->
			<tr>
				<td><label><bean:message bundle="gde" key="gde.plaEnvDeuPro.nroPlanilla.label"/>: </label></td>
				<td class="normal"><html:text name="plaEnvDeuProSearchPageVO" property="plaEnvDeuPro.nroPlanilla" size="20" maxlength="100"/></td>			
				<td><label><bean:message bundle="gde" key="gde.plaEnvDeuPro.anioPlanilla.label"/>: </label></td>
				<td class="normal"><html:text name="plaEnvDeuProSearchPageVO" property="plaEnvDeuPro.anioPlanilla" size="20" maxlength="100"/></td>			
			</tr>
			
			<!-- Estado -->
			<tr>	
				<td><label><bean:message bundle="gde" key="gde.plaEnvDeuPro.EstPlaEnvDeuPr.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="plaEnvDeuProSearchPageVO" property="plaEnvDeuPro.estPlaEnvDeuPr.id" styleClass="select">
						<html:optionsCollection name="plaEnvDeuProSearchPageVO" property="listEstPlaEnvDeuPrVO" label="desEstPlaEnvDeuPro" value="id" />
					</html:select>
				</td>					
			</tr>
			
			<!-- FechaEnvioDesde y Hasta -->
			<tr>
				<td><label><bean:message bundle="gde" key="gde.plaEnvDeuProSearchPage.fechaEnvioDesde.label"/>: </label></td>
				<td class="normal">
					<html:text name="plaEnvDeuProSearchPageVO" property="fechaEnvioDesdeView" styleId="fechaEnvioDesdeView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaEnvioDesdeView');" id="a_fechaEnvioDesdeView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
				<td><label><bean:message bundle="gde" key="gde.plaEnvDeuProSearchPage.fechaEnvioHasta.label"/>: </label></td>
				<td class="normal">
					<html:text name="plaEnvDeuProSearchPageVO" property="fechaEnvioHastaView" styleId="fechaEnvioHastaView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaEnvioHastaView');" id="a_fechaEnvioHastaView">
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
		<div id="resultadoFiltro">
			<logic:equal name="plaEnvDeuProSearchPageVO" property="viewResult" value="true">
				<logic:notEmpty  name="plaEnvDeuProSearchPageVO" property="listResult">	
					<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
						<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
		               	<tbody>
			               	<tr>
								<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
								<logic:notEqual name="plaEnvDeuProSearchPageVO" property="modoSeleccionar" value="true">
									<th width="1">&nbsp;</th> <!-- Modificar -->
								</logic:notEqual>
								<th width="1">&nbsp;</th> <!-- imprimir padron -->
								<!-- <th width="1">&nbsp;</th> --><!-- imprimir constancias de deudas  -->							
								<!-- <th width="1">&nbsp;</th> --> <!-- CD -->
								<th width="1">&nbsp;</th> <!-- Recomponer Planilla -->
								<th width="1">&nbsp;</th> <!-- Habilitar Planilla -->
								<th align="left"><bean:message bundle="gde" key="gde.plaEnvDeuPro.label"/></th>
								<th align="left"><bean:message bundle="gde" key="gde.procurador.label"/></th>
								<th align="left"><bean:message bundle="def" key="def.recurso.label"/></th>
								<th align="left"><bean:message bundle="gde" key="gde.plaEnvDeuPro.fechaEnvio.label"/></th>
								<th align="left"><bean:message bundle="gde" key="gde.plaEnvDeuPro.EstPlaEnvDeuPr.label"/></th>
								<!-- <#ColumnTitles#> -->
							</tr>
								
							<logic:iterate id="PlaEnvDeuProVO" name="plaEnvDeuProSearchPageVO" property="listResult">
								<tr>
									<!-- Seleccionar -->
									<logic:equal name="plaEnvDeuProSearchPageVO" property="modoSeleccionar" value="true">
										<td>	
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="PlaEnvDeuProVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</td>
									</logic:equal>									
									<logic:notEqual name="plaEnvDeuProSearchPageVO" property="modoSeleccionar" value="true">
										<!-- Ver -->
										<td>
											<logic:equal name="plaEnvDeuProSearchPageVO" property="verEnabled" value="enabled">									
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="PlaEnvDeuProVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="plaEnvDeuProSearchPageVO" property="verEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
											</logic:notEqual>
										</td>	
										<!-- Modificar-->								
										<td>
											<logic:equal name="plaEnvDeuProSearchPageVO" property="modificarEnabled" value="enabled">
												<logic:equal name="PlaEnvDeuProVO" property="modificarBussEnabled" value="true">
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="PlaEnvDeuProVO" property="id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
													</a>
												</logic:equal>
												<logic:notEqual name="PlaEnvDeuProVO" property="modificarBussEnabled" value="true">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
												</logic:notEqual>
											</logic:equal>
											<logic:notEqual name="plaEnvDeuProSearchPageVO" property="modificarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
											</logic:notEqual>
										</td>
	
										<!-- Imprimir Planilla -->
										<td>
											<logic:equal name="PlaEnvDeuProVO" property="imprimirPadronEnabled" value="true">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('imprimirPadron', '<bean:write name="PlaEnvDeuProVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="gde" key="gde.plaEnvDeuProViewAdapter.button.imprimirPadron"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/imprimirPadronPlaEnvDeuJud.gif"/>
											</a>											
											</logic:equal>
											<logic:notEqual name="PlaEnvDeuProVO" property="imprimirPadronEnabled" value="true">
												<img title="<bean:message bundle="gde" key="gde.plaEnvDeuProViewAdapter.button.imprimirPadron"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/imprimirPadronPlaEnvDeuJud.gif"/>
											</logic:notEqual>
											
										</td>
	
										<!-- Imprimir Constancias de Deuda -->								
										<!--
										<td>
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('imprimirConstanciasPlanilla', '<bean:write name="PlaEnvDeuProVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="gde" key="gde.plaEnvDeuProViewAdapter.button.imprimirConstancias"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/imprimirConstDeudaPlaEnvDeuJud.gif"/>
											</a>											
										</td>
										-->
										<!--  CD -->
									    <!-- 
										<td>
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('imprimirArchivoCD', '<bean:write name="PlaEnvDeuProVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="gde" key="gde.plaEnvDeuProViewAdapter.button.archivoCD"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/cd0.gif"/>
											</a>											
										</td>									
										-->
											
										<!-- Recomponer Planilla -->										
										<td>									
											<logic:equal name="plaEnvDeuProSearchPageVO" property="recomponerPlanillaEnabled" value="enabled">
												<logic:equal name="PlaEnvDeuProVO" property="recomponerPlanillaBussEnabled" value="true">
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('recomponerPlanilla', '<bean:write name="PlaEnvDeuProVO" property="id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="gde" key="gde.plaEnvDeuProSearchPage.button.recomponer"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/recomponerPlaEnvDeuPr0.gif"/>
													</a>
												</logic:equal> 
												<logic:notEqual name="PlaEnvDeuProVO" property="recomponerPlanillaBussEnabled" value="true">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/recomponerPlaEnvDeuPr1.gif"/>
												</logic:notEqual>
											</logic:equal>
											<logic:notEqual name="plaEnvDeuProSearchPageVO" property="recomponerPlanillaEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/recomponerPlaEnvDeuPr1.gif"/>
											</logic:notEqual>
										</td>
										
										<td>
											<!-- Habilitar Planilla -->
											<logic:equal name="plaEnvDeuProSearchPageVO" property="habilitarPlanillaEnabled" value="enabled">
												<logic:equal name="PlaEnvDeuProVO" property="habilitarPlanillaBussEnabled" value="true">
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('habilitarPlanilla', '<bean:write name="PlaEnvDeuProVO" property="id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="gde" key="gde.plaEnvDeuProSearchPage.button.habilitarPlanilla"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/habilitarPlaEnvDeuJud.gif"/>
													</a>
												</logic:equal> 
												<logic:notEqual name="PlaEnvDeuProVO" property="habilitarPlanillaBussEnabled" value="true">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/habilitarPlaEnvDeuJud.gif"/>
												</logic:notEqual>
											</logic:equal>
											<logic:notEqual name="plaEnvDeuProSearchPageVO" property="habilitarPlanillaEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/habilitarPlaEnvDeuJud.gif"/>
											</logic:notEqual>
	
											<!-- En estado creado -->
											<logic:equal name="PlaEnvDeuProVO" property="estado.id" value="-1">
												<a style="cursor: pointer; cursor: hand;">
												<img border="0" title="<bean:message bundle="base" key="abm.button.creado"/>" src="<%=request.getContextPath()%>/images/iconos/creado0.gif"/>
												</a>
											</logic:equal> 
										</td>									
									</logic:notEqual>
									<!-- <#ColumnFiedls#> -->
									<td><bean:write name="PlaEnvDeuProVO" property="nroBarraAnioPlanillaView"/>&nbsp;</td>
									<td><bean:write name="PlaEnvDeuProVO" property="procurador.descripcion"/>&nbsp;</td>
									<td><bean:write name="PlaEnvDeuProVO" property="desRecurso"/>&nbsp;</td>
									<td><bean:write name="PlaEnvDeuProVO" property="fechaEnvioView"/>&nbsp;</td>
									<td><bean:write name="PlaEnvDeuProVO" property="estPlaEnvDeuPr.desEstPlaEnvDeuPro"/>&nbsp;</td>
								</tr>
							</logic:iterate>
					
							<tr>
								<td class="paginador" align="center" colspan="20">
									<bean:define id="pager" name="plaEnvDeuProSearchPageVO"/>
									<%@ include file="/base/pager.jsp" %>
								</td>
							</tr>
							
						</tbody>
					</table>
				</logic:notEmpty>
				
				<logic:empty name="plaEnvDeuProSearchPageVO" property="listResult">
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
				&nbsp;
	 		  	<html:button property="btnVerArchivosProcurador"  styleClass="boton" onclick="submitForm('verArchivosProcurador', '');">
					Ver Archivos de Envio Masivo
				</html:button>
	
				</td>
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

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
