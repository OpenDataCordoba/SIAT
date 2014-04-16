<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/BuscarCuentaObjImp.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="gde" key="gde.cuentaObjImpSearchPage.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>
					<logic:equal name="cuentaObjImpSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="base" key="base.busquedaLegendBusqueda"/>
						<bean:message bundle="gde" key="gde.cuentaObjImp.label"/>
					</logic:equal>
					<logic:notEqual name="cuentaObjImpSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="gde" key="gde.cuentaObjImpSearchPage.legend"/>
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
		
		
	<!-- ObjImp -->
	<fieldset>
		<legend><bean:message bundle="pad" key="pad.objImp.title"/></legend>
		
		<table class="tabladatos">

			<tr>
				<!-- Tipo Objeto Imponible -->
				<td><label><bean:message bundle="def" key="def.tipObjImp.label"/>: </label></td>
				<td class="normal">
					<bean:write name="cuentaObjImpSearchPageVO" property="objImp.tipObjImp.desTipObjImp"/>
				</td>

				<!-- Clave Funcional -->
				<td><label><bean:write name="cuentaObjImpSearchPageVO" property="tipObjImpDefinition.desClaveFunc" />: </label></td>
				<td class="normal">
					<bean:write name="cuentaObjImpSearchPageVO" property="objImp.claveFuncional"/>
				</td>
			</tr>

				<!-- Vigencia -->
			<tr>
				<td><label><bean:message bundle="base" key="base.vigencia.label"/>: </label></td>
				<td class="normal">
					<bean:write name="cuentaObjImpSearchPageVO" property="objImp.vigencia.value"/>
				</td>
			</tr>
			
			
			<logic:notEmpty name="cuentaObjImpSearchPageVO" property="tipObjImpDefinition.listTipObjImpAtrDefinition">				
				<logic:iterate id="TipObjImpAtrDefinition" name="cuentaObjImpSearchPageVO" property="tipObjImpDefinition.listTipObjImpAtrDefinition" indexId="count">
					<logic:equal name="TipObjImpAtrDefinition" property="atributo.codAtributo" value="DomicilioFinca">
						<bean:define id="AtrVal" name="TipObjImpAtrDefinition"/>
						<tr>	
							<%@ include file="/def/atrDefinition4View.jsp" %>
						</tr>
					</logic:equal>	
					<logic:equal name="TipObjImpAtrDefinition" property="atributo.codAtributo" value="UbiTerreno">
						<bean:define id="AtrVal" name="TipObjImpAtrDefinition"/>
						<tr>	
							<%@ include file="/def/atrDefinition4View.jsp" %>
						</tr>
					</logic:equal>					
				</logic:iterate>			
			</logic:notEmpty>
			
			
			
			<tr>
				<!-- Fecha Alta-->
				<td><label><bean:message bundle="pad" key="pad.objImp.fechaAlta.label"/>: </label></td>
				<td class="normal">
					<bean:write name="cuentaObjImpSearchPageVO" property="objImp.fechaAltaView"/>
				</td>

				<!-- Fecha Baja-->
			 	<td><label><bean:message bundle="pad" key="pad.objImp.fechaBaja.label"/>:</label></td>
			 	<td class="normal">
			 		<bean:write name="cuentaObjImpSearchPageVO" property="objImp.fechaBajaView"/>
			 	</td>
			</tr>
			
			<tr>				
				<td align="right" colspan="4">
					<input type="button" class="boton" onClick="submitForm('buscarObjImp', '');" 
						value="<bean:message bundle="gde" key="gde.cuentaObjImpSearchPage.button.buscarObjImp"/>"/>
				</td>
			</tr>
			
		</table>
	</fieldset>	
	<!-- ObjImp -->
		
	<!-- Resultado Filtro -->
	<div id="resultadoFiltro">
		<logic:equal name="cuentaObjImpSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="cuentaObjImpSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Liquidacion Deuda -->
							<th width="1">&nbsp;</th> <!-- Estado Deuda -->
							<th align="left"><bean:message bundle="def" key="def.recurso.label"/></th>
							<th align="left"><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.ref"/></th>
							<th align="left"><bean:message bundle="base" key="base.estado.label"/></th>
						</tr>
							
						<logic:iterate id="CuentaVO" name="cuentaObjImpSearchPageVO" property="listResult">
							<tr>
								<!-- Liquidacion deuda -->
								<td>
									<logic:equal name="cuentaObjImpSearchPageVO" property="liquidacionDeudaEnabled" value="enabled">
										<logic:equal name="CuentaVO" property="liquidacionDeudaBussEnabled" value="true">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('liquidacionDeuda', '<bean:write name="CuentaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="gde" key="gde.button.liquidacionDeuda"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/liquidacionDeuda0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="CuentaVO" property="liquidacionDeudaBussEnabled" value="true">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/liquidacionDeuda0.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="cuentaObjImpSearchPageVO" property="liquidacionDeudaEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/liquidacionDeuda0.gif"/>
									</logic:notEqual>
								</td>
								<!-- Estado cuenta -->								
								<td>
									<logic:equal name="cuentaObjImpSearchPageVO" property="estadoCuentaEnabled" value="enabled">
										<logic:equal name="CuentaVO" property="estadoCuentaBussEnabled" value="true">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('estadoCuenta', '<bean:write name="CuentaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="gde" key="gde.button.estadoCuenta"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/estadoCuenta1.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="CuentaVO" property="estadoCuentaBussEnabled" value="true">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/estadoCuenta1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="cuentaObjImpSearchPageVO" property="estadoCuentaEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/estadoCuenta1.gif"/>
									</logic:notEqual>
								</td>
								<td><bean:write name="CuentaVO" property="recurso.desRecurso"/>&nbsp;</td>
								<td><bean:write name="CuentaVO" property="numeroCuenta" />&nbsp;</td>
								<td><bean:write name="CuentaVO" property="estado.value" />&nbsp;</td>
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="cuentaObjImpSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="cuentaObjImpSearchPageVO" property="listResult">
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
	<input type="hidden" name="idObjImp" value="<bean:write name="cuentaObjImpSearchPageVO" property="objImp.id" bundle="base" formatKey="general.format.id"/>"/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
