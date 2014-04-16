<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/BuscarDeudaContrib.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="gde" key="gde.deudaContribSearchPage.title"/></h1>	
	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p><bean:message bundle="gde" key="gde.deudaContribSearchPage.legend"/></p>
			</td>				
			<td align="right">
	 			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
		
	<!-- Filtro Datos del Contribuyente -->
    <fieldset>
    	<legend><bean:message bundle="pad" key="pad.contribuyente.title"/></legend>
        <table class="tabladatos">
			<bean:define id="personaVO" name="deudaContribSearchPageVO" property="contribuyente.persona"/>
			<%@ include file="/pad/persona/includePersonaReducida.jsp"%>
		</table>
	</fieldset>

	<fieldset>
		<legend><bean:message bundle="def" key="def.servicioBanco.label"/></legend>
        <table class="tabladatos">
			<tr>	
				<td><label><bean:message bundle="def" key="def.servicioBanco.label"/>: </label></td>
				<td class="normal">
					<html:select name="deudaContribSearchPageVO" property="servicioBanco.id" styleClass="select" onchange="submitForm('paramServicioBanco', '');">
						<html:optionsCollection name="deudaContribSearchPageVO" property="listServicioBanco" label="desServicioBanco" value="id" />
					</html:select>
				</td>					
			</tr>
		</table>
	</fieldset>
	<!-- Fin Filtro -->
		
	<!-- Resultado Filtro -->
	<div id="resultadoFiltro">

		<logic:equal name="deudaContribSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="deudaContribSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th rowspan="2" width="1">&nbsp;</th> <!-- Liquidacion deuda -->
							<th rowspan="2" width="1">&nbsp;</th> <!-- Estado cuenta -->
							<th rowspan="2" align="left"><bean:message bundle="def" key="def.recurso.label"/></th>
							<th rowspan="2" align="left"><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.ref"/></th>
							<th rowspan="2" align="left"><bean:message bundle="pad" key="pad.cuenta.estado.ref"/></th>
							<th colspan="2" align="center"><bean:message bundle="pad" key="pad.cuentaTitular.vigencia.label"/></th>
							<th rowspan="2" align="left"><bean:message bundle="pad" key="pad.cuentaTitular.emitir"/></th>
						</tr>
						<tr>
							<th align="left"><bean:message bundle="pad" key="pad.cuentaTitular.fechaDesde.label"/></th>
							<th align="left"><bean:message bundle="pad" key="pad.cuentaTitular.fechaHasta.label"/></th>
						</tr>	
						<logic:iterate id="CuentaTitularVO" name="deudaContribSearchPageVO" property="listResult">
							<tr>
								<!-- Liquidacion deuda -->
								<td>
									<logic:equal name="deudaContribSearchPageVO" property="liquidacionDeudaEnabled" value="enabled">
										<logic:equal name="CuentaTitularVO" property="cuenta.liquidacionDeudaBussEnabled" value="true">
											<a style="cursor: pointer; cursor: hand;" 
												onclick="submitForm('liquidacionDeuda','<bean:write name="CuentaTitularVO" property="cuenta.id" bundle="base" formatKey="general.format.id"/>-<bean:write name="CuentaTitularVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="gde" key="gde.button.liquidacionDeuda"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/liquidacionDeuda0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="CuentaTitularVO" property="cuenta.liquidacionDeudaBussEnabled" value="true">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/liquidacionDeuda1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="deudaContribSearchPageVO" property="liquidacionDeudaEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/liquidacionDeuda1.gif"/>
									</logic:notEqual>
								</td>
								<!-- Estado cuenta -->								
								<td>
									<logic:equal name="deudaContribSearchPageVO" property="estadoCuentaEnabled" value="enabled">										
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('estadoCuenta', '<bean:write name="CuentaTitularVO" property="cuenta.id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="gde" key="gde.button.estadoCuenta"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/estadoCuenta1.gif"/>
											</a>										
									</logic:equal>
									<logic:notEqual name="deudaContribSearchPageVO" property="estadoCuentaEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/estadoCuenta1.gif"/>
									</logic:notEqual>
								</td>
								<td><bean:write name="CuentaTitularVO" property="cuenta.recurso.desRecurso"/>&nbsp;</td>
								<td><bean:write name="CuentaTitularVO" property="cuenta.numeroCuenta" />&nbsp;</td>
								<td><bean:write name="CuentaTitularVO" property="cuenta.desEstadoView" />&nbsp;</td>
								<td><bean:write name="CuentaTitularVO" property="fechaDesdeView" />&nbsp;</td>
								<td><bean:write name="CuentaTitularVO" property="fechaHastaView" />&nbsp;</td>
								
								<td>
									<logic:equal name="deudaContribSearchPageVO" property="agregarCuentaEnabled" value="enabled">
										<logic:equal name="CuentaTitularVO" property="recursoPermiteEmision" value="true">
											<input type="button" class="boton" 
												   onclick="submitForm('irAEmision', '<bean:write name="CuentaTitularVO" property="cuenta.id" bundle="base" formatKey="general.format.id"/>');"
												   value="<bean:message bundle="pad" key="pad.cuentaTitular.emitir"/>"/>
										</logic:equal>									
										<logic:notEqual name="CuentaTitularVO" property="recursoPermiteEmision" value="true">
											<html:button property="btnEmitir" styleClass="boton" disabled="true">
												<bean:message bundle="pad" key="pad.cuentaTitular.emitir"/>
											</html:button>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="deudaContribSearchPageVO" property="agregarCuentaEnabled" value="enabled">
										<html:button property="btnEmitir" styleClass="boton" disabled="true">
											<bean:message bundle="pad" key="pad.cuentaTitular.emitir"/>
										</html:button>
									</logic:notEqual>
								</td>
							</tr>
						</logic:iterate>
					
					
					<logic:equal name="deudaContribSearchPageVO" property="superaMaxCantCuentas" value="true">
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="deudaContribSearchPageVO"/>
								<%@ include file="/base/pagerSinPU.jsp" %>
							</td>
						</tr>
					</logic:equal>
					
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="deudaContribSearchPageVO" property="listResult">
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
	
	<br>
	
	<table class="tablabotones">
		<tr>				
			<td align="left">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<td align="center">
				<logic:equal name="deudaContribSearchPageVO" property="imprimirListDeudaContribEnabled" value="enabled">
					<html:button property="btnImprimirListDeudaContrib"  styleClass="boton" onclick="submitForm('imprimirListDeudaContrib', '');">
						<bean:message bundle="gde" key="deudaContribSearchPage.imprimirListDeudaContrib.label"/>
					</html:button>
				</logic:equal>
			</td>
			
			<td align="right">
				<logic:equal name="deudaContribSearchPageVO" property="agregarCuentaEnabled" value="enabled">
					<html:button property="btnImprimirListDeudaContrib"  styleClass="boton" onclick="submitForm('agregarCuenta', '0');">
						<bean:message bundle="gde" key="deudaContribSearchPage.agregarCuenta.label"/>
					</html:button>
				</logic:equal>
				<logic:notEqual name="deudaContribSearchPageVO" property="agregarCuentaEnabled" value="enabled">
					<html:button property="btnImprimirListDeudaContrib" styleClass="boton" disabled="true">
						<bean:message bundle="gde" key="deudaContribSearchPage.agregarCuenta.label"/>
					</html:button>				
				</logic:notEqual>
			</td>
		</tr>
	</table>
		
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="paramContribuyente" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>
	<input type="hidden" name="idContrib" value="<bean:write name="deudaContribSearchPageVO" property="contribuyente.id" bundle="base" formatKey="general.format.id"/>"/>

</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
