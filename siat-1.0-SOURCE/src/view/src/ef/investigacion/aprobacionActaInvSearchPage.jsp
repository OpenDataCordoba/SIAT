<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/BuscarAprobacionActaInv.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="ef" key="ef.actaInvSearchPage.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>
					<logic:equal name="opeInvConSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="base" key="base.busquedaLegendBusqueda"/>
						<bean:message bundle="ef" key="ef.actaInv.label"/>
					</logic:equal>
					<logic:notEqual name="opeInvConSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="ef" key="ef.actaInvSearchPage.legend"/>
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
			<td><label><bean:message bundle="ef" key="ef.planFiscal.label"/>: </label></td>
			<td class="normal">
				<html:select name="opeInvConSearchPageVO" property="opeInvCon.opeInv.planFiscal.id" styleClass="select" onchange="submitForm('paramPlan','')">
					<html:optionsCollection name="opeInvConSearchPageVO" property="listPlanFiscal" label="desPlanFiscal" value="id" />
				</html:select>
			</td>					
		</tr>
		<tr>	
			<td><label><bean:message bundle="ef" key="ef.opeInv.label"/>: </label></td>
			<td class="normal">
				<html:select name="opeInvConSearchPageVO" property="opeInvCon.opeInv.id" styleClass="select">
					<html:optionsCollection name="opeInvConSearchPageVO" property="listOpeInv" label="desOpeInv" value="id" />
				</html:select>
			</td>					
		</tr>
		<tr>	
			<td><label><bean:message bundle="ef" key="ef.actaInvSearchPage.estadoOpeInvCon.label"/>: </label></td>
			<td class="normal">
				<html:select name="opeInvConSearchPageVO" property="opeInvCon.estadoOpeInvCon.id" styleClass="select">
					<html:optionsCollection name="opeInvConSearchPageVO" property="listEstadoOpeInvCon" label="desEstadoOpeInvCon" value="id" />
				</html:select>
			</td>					
		</tr>
		<tr>	
			<td><label><bean:message bundle="ef" key="ef.actaInv.estadoActa.label"/>: </label></td>
			<td class="normal">
				<html:select name="opeInvConSearchPageVO" property="opeInvCon.actaInv.estadoActa.id" styleClass="select">
					<html:optionsCollection name="opeInvConSearchPageVO" property="listEstadoActa" label="desEstadoActa" value="id" />
				</html:select>
			</td>					
		</tr>
		<tr>	
			<td><label><bean:message bundle="ef" key="ef.investigador.label"/>: </label></td>
			<td class="normal">
				<html:select name="opeInvConSearchPageVO" property="opeInvCon.investigador.id" styleClass="select">
					<html:optionsCollection name="opeInvConSearchPageVO" property="listInvestigador" label="desInvestigador" value="id" />
				</html:select>
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
		<logic:equal name="opeInvConSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="opeInvConSearchPageVO" property="listResult">	
					<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Seleccionar -->
							<th><bean:message bundle="pad" key="pad.persona.label"/></th> <!-- Contribuyente -->
							<th><bean:message bundle="pad" key="pad.persona.cuit.label"/>
							<th><bean:message bundle="pad" key="pad.persona.domicilio.label"/></th>
							<th><bean:message bundle="ef" key="ef.opeInv.label"/></th>
							<th><bean:message bundle="ef" key="ef.investigador.label"/></th>
							<th><bean:message bundle="ef" key="ef.estadoOpeInvCon.label"/></th>
							<!-- <#ColumnTitles#> -->
						</tr>
							
						<logic:iterate id="OpeInvConVO" name="opeInvConSearchPageVO" property="listResult">
							<tr>
								<!-- Seleccionar -->
									<td>
										<logic:equal name="opeInvConSearchPageVO" property="verEnabled" value="enabled">									
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('cambiarEstado', '<bean:write name="OpeInvConVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="OpeInvConVO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</td>
									
								<td>
									<bean:write name="OpeInvConVO" property="contribuyente.persona.represent"/>
								</td>
								<td>
									<bean:write name="OpeInvConVO" property="contribuyente.persona.cuit"/>
								</td>
								<td>
									<bean:write name="OpeInvConVO" property="domicilio.view"/>
								</td>
								<td>
									<bean:write name="OpeInvConVO" property="opeInv.desOpeInv"/>
								</td>
								<td>
									<bean:write name="OpeInvConVO" property="investigador.desInvestigador"/>
								</td>
								<td>
									<bean:write name="OpeInvConVO" property="estadoOpeInvCon.desEstadoOpeInvCon"/>
								</td>								
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="opeInvConSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="opeInvConSearchPageVO" property="listResult">
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
<!-- actaInvSearchPage.jsp -->
