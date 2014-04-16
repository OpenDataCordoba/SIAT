<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/BuscarConstanciaDeu.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>

	<!-- Busqueda de Recuso -->
	<span id="blockBusqueda" style="display:none"> 
		<bean:define id="adapterVO" name="ConstanciaDeuSearchPageVO"/>
		<bean:define id="poseeParam" value="true" />
		<%@ include file="/def/gravamen/includeRecursoSearch.jsp" %>
	</span>

	<span id="blockSimple" style="display:block">
			
		<h1><bean:message bundle="gde" key="gde.constanciaDeuSearchPage.title"/></h1>
			
		<table class="tablabotones" width="100%">
			<tr>
				<td align="left">
					<p>
						<logic:equal name="ConstanciaDeuSearchPageVO" property="modoSeleccionar" value="true">
							<bean:message bundle="gde" key="gde.constanciaDeuSearchPage.legend"/>
						</logic:equal>						
						<logic:notEqual name="ConstanciaDeuSearchPageVO" property="modoSeleccionar" value="true">
							<bean:message bundle="gde" key="gde.constanciaDeuSearchPage.legend"/>
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
			
			<!-- Recurso -->
			<tr>	
				<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal" colspan="3">
					<logic:equal name="ConstanciaDeuSearchPageVO" property="habilitarRecursoEnabled" value="true" >
						<html:select name="ConstanciaDeuSearchPageVO" property="constanciaDeu.cuenta.recurso.id" disabled ="false" styleClass="select" onchange="submitForm('paramRecurso', '');" styleId="cboRecurso" style="width:90%">
							<bean:define id="includeRecursoList" name="ConstanciaDeuSearchPageVO" property="listRecurso"/>
							<bean:define id="includeIdRecursoSelected" name="ConstanciaDeuSearchPageVO" property="constanciaDeu.cuenta.recurso.id"/>
							<%@ include file="/def/gravamen/includeRecurso.jsp" %>
						</html:select>
						
						<a onclick="toggleSearchRecurso('blockSimple', 'blockBusqueda'); setFocus('recursoFilter');" >
							<img title='<bean:message bundle="def" key="def.recurso.filtro.button.busqueda"/>' border="0" 
							src="<%=request.getContextPath()%>/images/iconos/lupita.gif" width="20" height="20">
						</a>	
									
					</logic:equal>			
					<logic:notEqual name="ConstanciaDeuSearchPageVO" property="habilitarRecursoEnabled" value="true">
						<html:select name="ConstanciaDeuSearchPageVO" property="constanciaDeu.cuenta.recurso.id" disabled ="true" styleClass="select">
							<bean:define id="includeRecursoList" name="ConstanciaDeuSearchPageVO" property="listRecurso"/>
							<bean:define id="includeIdRecursoSelected" name="ConstanciaDeuSearchPageVO" property="constanciaDeu.cuenta.recurso.id"/>
							<%@ include file="/def/gravamen/includeRecurso.jsp" %>
						</html:select>	
					</logic:notEqual>			
				</td>		
			</tr>
			
			<!-- Procurador -->
			<tr>	
				<td><label><bean:message bundle="gde" key="gde.procurador.label"/>: </label></td>
				<td class="normal" colspan="3">
					<logic:equal name="ConstanciaDeuSearchPageVO" property="habilitarProcuradorEnabled" value="true" >					
						<html:select name="ConstanciaDeuSearchPageVO" property="constanciaDeu.procurador.id" disabled ="false" styleClass="select">
							<html:optionsCollection name="ConstanciaDeuSearchPageVO" property="listProcurador" label="descripcion" value="id" />
						</html:select>
					</logic:equal>			
					<logic:notEqual name="ConstanciaDeuSearchPageVO" property="habilitarProcuradorEnabled" value="true">
						<html:select name="ConstanciaDeuSearchPageVO" property="constanciaDeu.procurador.id" disabled ="true" styleClass="select">
							<html:optionsCollection name="ConstanciaDeuSearchPageVO" property="listProcurador" label="descripcion" value="id" />
						</html:select>
					</logic:notEqual>
				</td>					
			</tr>
	
			<!-- nroConstancia y AnioConstancia -->
			<tr>
				<td><label><bean:message bundle="gde" key="gde.constanciaDeu.numero.ref"/>: </label></td>
				<td class="normal"><html:text name="ConstanciaDeuSearchPageVO" property="constanciaDeu.numero" size="20" maxlength="100"/></td>			
				<td><label><bean:message bundle="gde" key="gde.constanciaDeu.anio.ref"/>: </label></td>
				<td class="normal"><html:text name="ConstanciaDeuSearchPageVO" property="constanciaDeu.anio" size="20" maxlength="100"/></td>			
			</tr>
			
			<!-- nroPlanilla y AnioPlanilla -->
			<tr>
				<td><label><bean:message bundle="gde" key="gde.plaEnvDeuPro.nroPlanilla.label"/>: </label></td>
				<td class="normal"><html:text name="ConstanciaDeuSearchPageVO" property="constanciaDeu.plaEnvDeuPro.nroPlanilla" size="20" maxlength="100"/></td>			
				<td><label><bean:message bundle="gde" key="gde.plaEnvDeuPro.anioPlanilla.label"/>: </label></td>
				<td class="normal"><html:text name="ConstanciaDeuSearchPageVO" property="constanciaDeu.plaEnvDeuPro.anioPlanilla" size="20" maxlength="100"/></td>			
			</tr>
			
			<!-- cuenta -->
			<tr>
				<td><label><bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
				<td class="normal" colspan="3">
					<logic:equal name="ConstanciaDeuSearchPageVO" property="habilitarCuentaEnabled" value="true" >
						<html:text name="ConstanciaDeuSearchPageVO" property="constanciaDeu.cuenta.numeroCuenta" size="20" disabled ="false"/>
						<html:button property="btnBuscarCuenta"  styleClass="boton" onclick="submitForm('buscarCuenta', '');">
							<bean:message bundle="exe" key="exe.cueExeSearchPage.button.buscarCuenta"/>
						</html:button>
					</logic:equal>			
					<logic:notEqual name="ConstanciaDeuSearchPageVO" property="habilitarCuentaEnabled" value="true">	
						<html:text name="ConstanciaDeuSearchPageVO" property="constanciaDeu.cuenta.numeroCuenta" size="20" disabled ="true"/>
							<html:button property="btnBuscarCuenta"  styleClass="boton" onclick="submitForm('buscarCuenta', '');">
							<bean:message bundle="exe" key="exe.cueExeSearchPage.button.buscarCuenta"/>
						</html:button>
					</logic:notEqual>
		
				</td>			
			</tr>
			
			<!-- Estado -->
			<tr>	
				<td><label><bean:message bundle="gde" key="gde.constanciaDeu.estConDeu.label"/>: </label></td>
				<td class="normal" colspan="3">
					<logic:equal name="ConstanciaDeuSearchPageVO" property="habilitarEstadoEnabled" value="true" >	
						<html:select name="ConstanciaDeuSearchPageVO" property="constanciaDeu.estConDeu.id" styleClass="select" disabled ="false">
							<html:optionsCollection name="ConstanciaDeuSearchPageVO" property="listEstConDeuVO" label="desEstConDeu" value="id" />
						</html:select>
					</logic:equal>			
					<logic:notEqual name="ConstanciaDeuSearchPageVO" property="habilitarEstadoEnabled" value="true">				
						<html:select name="ConstanciaDeuSearchPageVO" property="constanciaDeu.estConDeu.id" styleClass="select" disabled ="true">
							<html:optionsCollection name="ConstanciaDeuSearchPageVO" property="listEstConDeuVO" label="desEstConDeu" value="id" />
						</html:select>
					</logic:notEqual>
				</td>					
			</tr>
			
			<!-- FechaEnvioDesde y Hasta -->
			<tr>
				<td><label><bean:message bundle="gde" key="gde.ConstanciaDeuSearchPageVO.fechaEnvioDesde.label"/>: </label></td>
				<td class="normal">
					<html:text name="ConstanciaDeuSearchPageVO" property="fechaEnvioDesdeView" styleId="fechaEnvioDesdeView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaEnvioDesdeView');" id="a_fechaEnvioDesdeView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
				<td><label><bean:message bundle="gde" key="gde.ConstanciaDeuSearchPageVO.fechaEnvioHasta.label"/>: </label></td>
				<td class="normal">
					<html:text name="ConstanciaDeuSearchPageVO" property="fechaEnvioHastaView" styleId="fechaEnvioHastaView" size="15" maxlength="10" styleClass="datos" />
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
			<logic:equal name="ConstanciaDeuSearchPageVO" property="viewResult" value="true">
				<logic:notEmpty  name="ConstanciaDeuSearchPageVO" property="listResult">	
					<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
						<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
		               	<tbody>
			               	<tr>
								<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
								<logic:notEqual name="ConstanciaDeuSearchPageVO" property="modoSeleccionar" value="true">
									<th width="1">&nbsp;</th> <!-- Modificar -->
									<th width="1">&nbsp;</th> <!-- Eliminar -->
									<th width="1">&nbsp;</th> <!-- Recomponer / imprimir-->
									<th width="1">&nbsp;</th> <!-- Anular -->
								</logic:notEqual>
								<th align="left"><bean:message bundle="gde" key="gde.constanciaDeu.label"/></th>
								<th align="left"><bean:message bundle="gde" key="gde.procurador.label"/></th>
								<th align="left"><bean:message bundle="def" key="def.recurso.label"/></th>
								<th align="left"><bean:message bundle="gde" key="gde.plaEnvDeuPro.label"/></th>
								<th align="left"><bean:message bundle="pad" key="pad.cuenta.label"/></th>
								<th align="left"><bean:message bundle="gde" key="gde.constanciaDeu.estConDeu.label"/></th>
							</tr>
								
							<logic:iterate id="ConstanciaDeuVO" name="ConstanciaDeuSearchPageVO" property="listResult">
								<tr>
									<!-- Seleccionar -->
									<logic:equal name="ConstanciaDeuSearchPageVO" property="modoSeleccionar" value="true">
										<td>	
											<logic:equal name="ConstanciaDeuVO" property="seleccionarBussEnabled" value="true">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="ConstanciaDeuVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="ConstanciaDeuVO" property="seleccionarBussEnabled" value="true">										
												<input type="checkbox" disabled="disabled"/>
											</logic:notEqual>
										</td>
									</logic:equal>									
									<logic:notEqual name="ConstanciaDeuSearchPageVO" property="modoSeleccionar" value="true">
										<!-- Ver -->
										<td>
											<logic:equal name="ConstanciaDeuSearchPageVO" property="verEnabled" value="enabled">									
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="ConstanciaDeuVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="ConstanciaDeuVO" property="verEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
											</logic:notEqual>
										</td>	
										<!-- Modificar-->								
										<td>
											<logic:equal name="ConstanciaDeuSearchPageVO" property="modificarEnabled" value="enabled">
												<logic:equal name="ConstanciaDeuVO" property="modificarEnabled" value="enabled">
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="ConstanciaDeuVO" property="id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
													</a>
												</logic:equal>
												<logic:notEqual name="ConstanciaDeuVO" property="modificarEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
												</logic:notEqual>
											</logic:equal>
											<logic:notEqual name="ConstanciaDeuSearchPageVO" property="modificarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
											</logic:notEqual>
										</td>
										
										<!-- Eliminar-->								
										<td>
											<logic:equal name="ConstanciaDeuSearchPageVO" property="eliminarEnabled" value="enabled">
												<logic:equal name="ConstanciaDeuVO" property="eliminarBussEnabled" value="true">
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="ConstanciaDeuVO" property="id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
													</a>
												</logic:equal>
												<logic:notEqual name="ConstanciaDeuVO" property="eliminarBussEnabled" value="true">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
												</logic:notEqual>
											</logic:equal>
											<logic:notEqual name="ConstanciaDeuSearchPageVO" property="eliminarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
											</logic:notEqual>
										</td>									
																			
										<!-- Imprimir -->
										
										<logic:notEqual name="ConstanciaDeuVO" property="estConDeu.id" value="3">
											<td>
												<logic:equal name="ConstanciaDeuSearchPageVO" property="imprimirConstanciaEnabled" value="enabled">
													<logic:equal name="ConstanciaDeuVO" property="imprimirBussEnabled" value="true">
														<a style="cursor: pointer; cursor: hand;" onclick="submitForm('impresionConstancia', '<bean:write name="ConstanciaDeuVO" property="id" bundle="base" formatKey="general.format.id"/>');">
															<img title="<bean:message bundle="gde" key="gde.constanciaDeuSearchPage.button.imprimir"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/imprimirPadronPlaEnvDeuJud.gif"/>
														</a>
													</logic:equal>
													<logic:notEqual name="ConstanciaDeuVO" property="imprimirBussEnabled" value="true">
														<img border="0" src="<%=request.getContextPath()%>/images/iconos/imprimirPadronPlaEnvDeuJud.gif"/>
													</logic:notEqual>
												</logic:equal>
												<logic:notEqual name="ConstanciaDeuSearchPageVO" property="imprimirConstanciaEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/imprimirPadronPlaEnvDeuJud.gif"/>
												</logic:notEqual>									
											</td>									
										</logic:notEqual>
										
										<logic:equal name="ConstanciaDeuVO" property="estConDeu.id" value="3">
											<!-- Accion recomponerConstancia -->
											<td>
												<logic:equal name="ConstanciaDeuSearchPageVO" property="recomponerConstanciaEnabled" value="enabled">
													<logic:equal name="ConstanciaDeuVO" property="recomponerConstanciaBussEnabled" value="true">
														<a style="cursor: pointer; cursor: hand;" onclick="submitForm('recomponerConstancia', '<bean:write name="ConstanciaDeuVO" property="id" bundle="base" formatKey="general.format.id"/>');">
															<img title="<bean:message bundle="gde" key="gde.constanciaDeuSearchPage.button.recomponerConstancia"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/imprimirPadronPlaEnvDeuJud.gif"/>
														</a>
													</logic:equal>
													<logic:notEqual name="ConstanciaDeuVO" property="recomponerConstanciaBussEnabled" value="true">
														<img border="0" src="<%=request.getContextPath()%>/images/iconos/imprimirPadronPlaEnvDeuJud.gif"/>
													</logic:notEqual>
												</logic:equal>
												<logic:notEqual name="ConstanciaDeuSearchPageVO" property="recomponerConstanciaEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/imprimirPadronPlaEnvDeuJud.gif"/>
												</logic:notEqual>
											</td>									
										</logic:equal>
										
										
										<!-- Anular -->
										<td>
											<logic:equal name="ConstanciaDeuSearchPageVO" property="anularConstanciaEnabled" value="enabled">
												<logic:equal name="ConstanciaDeuVO" property="anularConstanciaBussEnabled" value="true">
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('anularConstancia', '<bean:write name="ConstanciaDeuVO" property="id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="gde" key="gde.constanciaDeuSearchPage.button.anularConstancia"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/anularConstancia0.gif"/>
													</a>
												</logic:equal>
												<logic:notEqual name="ConstanciaDeuVO" property="anularConstanciaBussEnabled" value="true">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/anularConstancia1.gif"/>
												</logic:notEqual>
											</logic:equal>
											<logic:notEqual name="ConstanciaDeuSearchPageVO" property="anularConstanciaEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/anularConstancia1.gif"/>
											</logic:notEqual>
										</td>									
										
									</logic:notEqual>
									<td><bean:write name="ConstanciaDeuVO" property="numeroBarraAnioConstanciaView"/>&nbsp;</td>
									<td><bean:write name="ConstanciaDeuVO" property="procurador.descripcion" />&nbsp;</td>
									<td><bean:write name="ConstanciaDeuVO" property="cuenta.recurso.desRecurso" />&nbsp;</td>
									<td><bean:write name="ConstanciaDeuVO" property="plaEnvDeuPro.nroBarraAnioPlanillaView"/>&nbsp;</td>
									<td><bean:write name="ConstanciaDeuVO" property="cuenta.numeroCuenta" />&nbsp;</td>
									<td><bean:write name="ConstanciaDeuVO" property="estConDeu.desEstConDeu" />&nbsp;</td>								
								</tr>
							</logic:iterate>
					
							<tr>
								<td class="paginador" align="center" colspan="20">
									<bean:define id="pager" name="ConstanciaDeuSearchPageVO"/>
									<%@ include file="/base/pager.jsp" %>
								</td>
							</tr>
							
						</tbody>
					</table>
				</logic:notEmpty>
				
				<logic:empty name="ConstanciaDeuSearchPageVO" property="listResult">
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
				<logic:equal name="ConstanciaDeuSearchPageVO" property="viewResult" value="true">
					<td align="right">
	  	    			<logic:equal name="ConstanciaDeuSearchPageVO" property="modoSeleccionar" value="false">
	  	    				<bean:define id="agregarEnabled" name="ConstanciaDeuSearchPageVO" property="agregarEnabled"/>
							<input type="button" <%=agregarEnabled%> class="boton" 
								onClick="submitForm('agregar', '0');" 
								value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
						</logic:equal>
	  	    			<logic:equal name="ConstanciaDeuSearchPageVO" property="modoSeleccionar" value="true">
	  	    				<logic:equal name="ConstanciaDeuSearchPageVO" property="agregarEnSeleccion" value="true">
								<bean:define id="agregarEnabled" name="ConstanciaDeuSearchPageVO" property="agregarEnabled"/>
								<input type="button" <%=agregarEnabled%> class="boton" 
									onClick="submitForm('agregar', '0');" 
									value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
								</logic:equal>
						</logic:equal>
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

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
