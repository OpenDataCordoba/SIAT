<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/BuscarCuentasProcurador.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="gde" key="gde.cuentasProcuradorSearchPage.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p><bean:message bundle="gde" key="gde.cuentasProcuradorSearchPage.legend"/></p>
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
		
		<!-- Procurador -->		
		<tr>	
			<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.procurador.label"/>: </label></td>
			<td class="normal" colspan="3">
				<html:select name="cuentasProcuradorSearchPageVO" property="procuradorVO.id" styleClass="select" onchange="submitForm('paramProcurador', '');">
					<html:optionsCollection name="cuentasProcuradorSearchPageVO" property="listProcurador" label="descripcion" value="id" />
				</html:select>
			</td>					
		</tr>		
		
		<!-- Recurso -->
		<tr>	
			<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
			<td class="normal" colspan="3">
					<html:select name="cuentasProcuradorSearchPageVO" property="idRecurso" styleClass="select" >
						<bean:define id="includeRecursoList" name="cuentasProcuradorSearchPageVO" property="listRecurso"/>
						<bean:define id="includeIdRecursoSelected" name="cuentasProcuradorSearchPageVO" property="idRecurso"/>
						<%@ include file="/def/gravamen/includeRecurso.jsp" %>
					</html:select>							
			</td>		
		</tr>
		
		<!-- Cuenta -->
		<tr>	
			<td><label><bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
			<td class="normal" colspan="3">
				<html:text name="cuentasProcuradorSearchPageVO" property="cuenta.numeroCuenta"/>
				<html:button property="btnBuscarCuenta"  styleClass="boton" onclick="submitForm('buscarCuenta', '');">
					<bean:message bundle="exe" key="exe.cueExeSearchPage.button.buscarCuenta"/>
				</html:button>											
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
		<logic:equal name="cuentasProcuradorSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="cuentasProcuradorSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> 
							<th width="1">&nbsp;</th> 
							<th align="left"><bean:message bundle="def" key="def.recurso.label"/></th>
							<th align="left"><bean:message bundle="pad" key="pad.cuenta.label"/></th>
						</tr>
							
						<logic:iterate id="cuenta" name="cuentasProcuradorSearchPageVO" property="listResult">
							<tr>
								<!-- Liquidacion deuda -->
								<td>
									<logic:equal name="cuentasProcuradorSearchPageVO" property="liquidacionDeudaEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('liquidacionDeuda', '<bean:write name="cuenta" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="gde" key="gde.button.liquidacionDeuda"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/liquidacionDeuda0.gif"/>
											</a>
									</logic:equal>
									<logic:notEqual name="cuentasProcuradorSearchPageVO" property="liquidacionDeudaEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/liquidacionDeuda1.gif"/>
									</logic:notEqual>
								</td>
								<!-- Estado cuenta -->								
								<td>
									<logic:equal name="cuentasProcuradorSearchPageVO" property="estadoCuentaEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('estadoCuenta', '<bean:write name="cuenta" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="gde" key="gde.button.estadoCuenta"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/estadoCuenta1.gif"/>
											</a>										
									</logic:equal>
									<logic:notEqual name="cuentasProcuradorSearchPageVO" property="estadoCuentaEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/estadoCuenta1.gif"/>
									</logic:notEqual>
								</td>	
								<td><bean:write name="cuenta" property="recurso.desRecurso" />&nbsp;</td>
								<td><bean:write name="cuenta" property="numeroCuenta" />&nbsp;</td>															
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="cuentasProcuradorSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="cuentasProcuradorSearchPageVO" property="listResult">
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
	<input type="hidden" name="name"         value="<bean:write name='cuentasProcuradorSearchPageVO' property='name'/>" id="name"/>
	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
