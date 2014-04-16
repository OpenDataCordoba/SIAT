<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/BuscarOrdenControlFis.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="ef" key="ef.ordenControlFisSearchPage.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>
					<logic:equal name="ordenControlFisSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="base" key="base.busquedaLegendBusqueda"/>
						<bean:message bundle="ef" key="ef.ordenControlFis.label"/>
					</logic:equal>
					<logic:notEqual name="ordenControlFisSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="ef" key="ef.ordenControlFisSearchPage.legend"/>
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
		
			<!-- Nro. Orden -->
			<tr>	
				<td><label><bean:message bundle="ef" key="ef.ordenControl.nroOrden.label"/>: </label></td>
				<td class="normal">
					<html:text name="ordenControlFisSearchPageVO" property="ordenControl.numeroOrden" size="5"/>	
				</td>
			
			<!-- anio orden -->						
				<td><label><bean:message bundle="ef" key="ef.ordenControl.anioOrden.label"/>: </label></td>
				<td class="normal">
					<html:text name="ordenControlFisSearchPageVO" property="ordenControl.anioOrden" maxlength="4" size="5"/>	
				</td>
			</tr>
			
			<!-- Inclusion de Caso -->
			<tr>
				<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
				<td colspan="3">
					<bean:define id="IncludedVO" name="ordenControlFisSearchPageVO" property="ordenControl"/>
					<bean:define id="voName" value="ordenControl" />
					<%@ include file="/cas/caso/includeCaso.jsp" %>
				</td>
			</tr>
			<!-- Fin Inclusion de Caso -->	
			
			<!-- Tipo Orden -->
			<tr>	
				<td><label><bean:message bundle="ef" key="ef.tipoOrden.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="ordenControlFisSearchPageVO" property="ordenControl.tipoOrden.id" styleClass="select">
						<html:optionsCollection name="ordenControlFisSearchPageVO" property="listTipoOrdenVO" label="desTipoOrden" value="id" />
					</html:select>
				</td>
			</tr>
				
			<tr>				
			<!-- Estado Orden -->
				<td><label><bean:message bundle="ef" key="ef.estadoOrden.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="ordenControlFisSearchPageVO" property="ordenControl.estadoOrden.id" styleClass="select">
						<html:optionsCollection name="ordenControlFisSearchPageVO" property="listEstadoOrdenVO" label="desEstadoOrden" value="id" />
					</html:select>
				</td>														
			</tr>				

			<!-- Origen -->
			<tr>	
				<td><label><bean:message bundle="ef" key="ef.origen.ref"/>: </label></td>
				<td class="normal">
					<html:select name="ordenControlFisSearchPageVO" property="ordenControl.origenOrden.id" onchange="submitForm('paramOrigenOrden', '');" styleClass="select">
						<html:optionsCollection name="ordenControlFisSearchPageVO" property="listOrigenOrdenVO" label="desOrigen" value="id" />
					</html:select>
				</td>					
			</tr>
			
			<!-- paramOrigen -->
			
			<logic:notEmpty name="ordenControlFisSearchPageVO" property="listOrigenOrdenProJud">
				<tr>
					<td>&nbsp;</td>
					<td class="normal" colspan="3">
						<html:select name="ordenControlFisSearchPageVO" property="origenOrdenProJud.id" styleClass="select">
							<html:optionsCollection name="ordenControlFisSearchPageVO" property="listOrigenOrdenProJud" label="desOrigen" value="id" />
						</html:select>
					</td>
				</tr>		
			</logic:notEmpty>
			
			<logic:notEmpty name="ordenControlFisSearchPageVO" property="listOpeInv">
				<tr>
					<td>&nbsp;</td>
					<td class="normal" colspan="3">			
						<html:select name="ordenControlFisSearchPageVO" property="ordenControl.opeInvCon.opeInv.id" styleClass="select">
							<html:optionsCollection name="ordenControlFisSearchPageVO" property="listOpeInv" label="desOpeInv" value="id" />
						</html:select>
					</td>
				</tr>		
			</logic:notEmpty>
			
			<!-- FIN paramOrigen -->
			
			<!-- fecha emision desde -->
			<tr>
				<td><label><bean:message bundle="ef" key="ef.ordenControlFisSearchPage.fechaEmiDesde.label"/>: </label></td>
				<td class="normal">
					<html:text name="ordenControlFisSearchPageVO" property="fechaEmisionDesdeView" styleId="fechaEmisionDesdeView" size="10" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaEmisionDesdeView');" id="a_fechaEmisionDesdeView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
				
			<!-- fecha Hasta -->
				<td><label><bean:message bundle="ef" key="ef.ordenControlFisSearchPage.fechaEmiHasta.label"/>: </label></td>
				<td class="normal">
					<html:text name="ordenControlFisSearchPageVO" property="fechaEmisionHastaView" styleId="fechaEmisionHastaView" size="10" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaEmisionHastaView');" id="a_fechaEmisionHastaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
			
			<!-- contribuyente -->
			<tr>
				<td><label><bean:message bundle="pad" key="pad.contribuyente.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:text name="ordenControlFisSearchPageVO" property="ordenControl.contribuyente.persona.represent" disabled="true" size="25" maxlength="100"/>
					<html:button property="btnBuscarCuenta" styleClass="boton" onclick="submitForm('buscarPersona', '');">
						<bean:message bundle="ef" key="ef.ordenControlFisSearchPage.button.buscarPersona"/>
					</html:button>			
				</td>				
			</tr>
			
			<!-- Inspector -->
			<tr>
				<td><label><bean:message bundle="pad" key="pad.inspector.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:text name="ordenControlFisSearchPageVO" property="ordenControl.inspector.desInspector" disabled="true" size="25" maxlength="100"/>
					<logic:equal name="ordenControlFisSearchPageVO" property="buscarInspectorBussEnabled" value="true">
						<html:button  property="btnBuscarInspector" styleClass="boton" onclick="submitForm('buscarInspector', '');">
							<bean:message bundle="ef" key="ef.ordenControlFisSearchPage.button.buscarInspector"/>
						</html:button>			
					</logic:equal>
					<logic:equal name="ordenControlFisSearchPageVO" property="buscarInspectorBussEnabled" value="false">
						<html:button  property="btnBuscarInspector" styleClass="boton" disabled="true">
							<bean:message bundle="ef" key="ef.ordenControlFisSearchPage.button.buscarInspector"/>
						</html:button>			
					</logic:equal>					
					
				</td>				
			</tr>			
			
			<!-- supervisor -->
			<tr>
				<td><label><bean:message bundle="ef" key="ef.supervisor.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:text name="ordenControlFisSearchPageVO" property="ordenControl.supervisor.desSupervisor" disabled="true" size="25" maxlength="100"/>
					<logic:equal name="ordenControlFisSearchPageVO" property="buscarSupervisorBussEnabled" value="true">
						<html:button property="btnBuscarSupervisor" styleClass="boton" onclick="submitForm('buscarSupervisor', '');">
							<bean:message bundle="ef" key="ef.ordenControlFisSearchPage.button.buscarSupervisor"/>
						</html:button>			
					</logic:equal>	
					<logic:equal name="ordenControlFisSearchPageVO" property="buscarSupervisorBussEnabled" value="false">
						<html:button property="btnBuscarSupervisor"  disabled="true" styleClass="boton" onclick="submitForm('buscarSupervisor', '');">
							<bean:message bundle="ef" key="ef.ordenControlFisSearchPage.button.buscarSupervisor"/>
						</html:button>
					</logic:equal>
				</td>				
			</tr>
						
			<!-- <#Filtros#> -->
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
		<logic:equal name="ordenControlFisSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="ordenControlFisSearchPageVO" property="listResult">	
				<div class="horizscroll">
					<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
						<tbody>
		               		<tr style="	font-weight:bold;color:white;background-color:#006699;padding: 10px 15px 5px 30px;text-align: center;font-size:125%;">
		               			<td colspan="12"><bean:message bundle="base" key="base.resultadoBusqueda"/></td>
		               		</tr>	
			               	<tr>
								<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
								<logic:notEqual name="ordenControlFisSearchPageVO" property="modoSeleccionar" value="true">
									<th width="1">&nbsp;</th> <!-- Modificar -->
									<th width="1">&nbsp;</th> <!-- Asignar -->
									<th width="1">&nbsp;</th> <!-- Administrar -->
									<th><bean:message bundle="ef" key="ef.ordenControl.fechaEmision.label"/></th> <!-- fecha emision -->
									<th><bean:message bundle="ef" key="ef.ordenControlFisSearchPage.nroAnio.label"/></th> <!-- nro anio -->
									<th><bean:message bundle="ef" key="ef.tipoOrden.label"/></th> <!-- tipoOrden -->
									<th><bean:message bundle="ef" key="ef.origen.label"/></th> <!-- origenOrden -->
									<th><bean:message bundle="ef" key="ef.ordenControl.expediente.label"/></th> <!-- expediente -->
									<th><bean:message bundle="pad" key="pad.contribuyente.label"/></th> <!-- contrib -->
									<th><bean:message bundle="ef" key="ef.inspector.label"/></th> <!-- inspector -->
									<th><bean:message bundle="ef" key="ef.estadoOrden.label"/></th> <!-- estado -->
								</logic:notEqual>
								<!-- <#ColumnTitles#> -->
							</tr>
								
							<logic:iterate id="OrdenControlFisVO" name="ordenControlFisSearchPageVO" property="listResult">
								<tr>
									<!-- Seleccionar -->
									<logic:equal name="ordenControlFisSearchPageVO" property="modoSeleccionar" value="true">
										<td>	
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="OrdenControlFisVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</td>
									</logic:equal>									
									<logic:notEqual name="ordenControlFisSearchPageVO" property="modoSeleccionar" value="true">
										<!-- Ver -->
										<td>
											<logic:equal name="ordenControlFisSearchPageVO" property="verEnabled" value="enabled">									
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="OrdenControlFisVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="OrdenControlFisVO" property="verEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
											</logic:notEqual>
										</td>	
										<!-- Modificar-->								
										<td>
											<logic:equal name="ordenControlFisSearchPageVO" property="modificarEnabled" value="enabled">
												<logic:equal name="OrdenControlFisVO" property="modificarEnabled" value="enabled">
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="OrdenControlFisVO" property="id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
													</a>
												</logic:equal>
												<logic:notEqual name="OrdenControlFisVO" property="modificarEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
												</logic:notEqual>
											</logic:equal>
											<logic:notEqual name="ordenControlFisSearchPageVO" property="modificarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
											</logic:notEqual>
										</td>
		
										<!-- Asignar-->								
										<td>
											<logic:equal name="ordenControlFisSearchPageVO" property="asignarEnabled" value="enabled">
											  	<a style="cursor: pointer; cursor: hand;" onclick="submitForm('asignarOrdenInit', '<bean:write name="OrdenControlFisVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="ef" key="ef.ordenControlFisSearchPage.button.asignar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/asignarOrdCon0.gif"/>
												</a>	                                                                                                                                                  
											</logic:equal>
											<logic:notEqual name="ordenControlFisSearchPageVO" property="asignarEnabled" value="enabled">		
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/asignarOrdCon1.gif"/>
											</logic:notEqual>
										</td>
										<td>
											<!-- administrar -->
											<logic:equal name="ordenControlFisSearchPageVO" property="administrarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('administrar', '<bean:write name="OrdenControlFisVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="ef" key="ef.ordenControlFisSearchPage.button.administrar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/admOrdCon0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="ordenControlFisSearchPageVO" property="administrarEnabled" value="enabled">										
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/admOrdCon1.gif"/>
											</logic:notEqual>
										</td>
									</logic:notEqual>								
									
									<!-- fecha emision -->
									<td><bean:write name="OrdenControlFisVO" property="fechaEmisionView"/></td>
									
									<!-- nro - anio -->
									<td>
										<bean:write name="OrdenControlFisVO" property="numeroOrden" bundle="base" formatKey="general.format.id"/>/<bean:write name="OrdenControlFisVO" property="anioOrden" bundle="base" formatKey="general.format.id"/>
									</td>
									
									<!-- tipoOrden -->
									<td><bean:write name="OrdenControlFisVO" property="tipoOrden.desTipoOrden"/></td>
									
									<!-- origenOrden -->
									<td><bean:write name="OrdenControlFisVO" property="origenOrden.desOrigen"/></td>
									
									<!-- expediente -->
									<td><bean:write name="OrdenControlFisVO" property="caso.casoView"/></td>
									
									<!-- contrib -->
									<td><bean:write name="OrdenControlFisVO" property="opeInvCon.datosContribuyente"/></td>
									
									<!-- inspector -->
									<td><bean:write name="OrdenControlFisVO" property="inspector.desInspector"/></td>
									
									<!-- estado -->
									<td><bean:write name="OrdenControlFisVO" property="estadoOrden.desEstadoOrden"/></td>
								</tr>
							</logic:iterate>
					
							<tr>
								<td class="paginador" align="center" colspan="20">
									<bean:define id="pager" name="ordenControlFisSearchPageVO"/>
									<%@ include file="/base/pager.jsp" %>
								</td>
							</tr>
							
						</tbody>
					</table>
				</div>	
			</logic:notEmpty>
			
			<logic:empty name="ordenControlFisSearchPageVO" property="listResult">
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
			<logic:equal name="ordenControlFisSearchPageVO" property="viewResult" value="true">
  	    		<td align="right">
  	    			<logic:equal name="ordenControlFisSearchPageVO" property="modoSeleccionar" value="true">
  	    				<logic:equal name="ordenControlFisSearchPageVO" property="agregarEnSeleccion" value="true">
							<bean:define id="agregarEnabled" name="ordenControlFisSearchPageVO" property="agregarEnabled"/>
							<input type="button" <%=agregarEnabled%> class="boton" 
								onClick="submitForm('agregar', '0');" 
								value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
							</logic:equal>
					</logic:equal>
				</td>
			</logic:equal>
		</tr>
	</table>
	<input type="hidden" name="name"  value="<bean:write name='ordenControlFisSearchPageVO' property='name'/>" id="name"/>
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
<!-- ordenControlFisSearchPage.jsp -->