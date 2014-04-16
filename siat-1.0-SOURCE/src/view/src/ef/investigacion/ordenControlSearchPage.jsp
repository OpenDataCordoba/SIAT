<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/BuscarOrdenControl.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="ef" key="ef.ordenControlSearchPage.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>
					<logic:equal name="ordenControlSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="base" key="base.busquedaLegendBusqueda"/>
						<bean:message bundle="ef" key="ef.ordenControl.label"/>
					</logic:equal>
					<logic:notEqual name="ordenControlSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="ef" key="ef.ordenControlSearchPage.legend"/>
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
		
			<!-- Numero y anio -->
			
			<tr>
				<td><label><bean:message bundle="ef" key="ef.ordenControl.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:text name="ordenControlSearchPageVO" property="ordenControl.numeroOrdenView" size="6"/>
					&nbsp;/&nbsp;<html:text name="ordenControlSearchPageVO" property="ordenControl.anioOrdenView" size="6"/>
					&nbsp;(NNNN/AAAA)
				</td>
			</tr>

			<!-- Origen -->
			<tr>	
				<td><label><bean:message bundle="ef" key="ef.origen.ref"/>: </label></td>
				<td class="normal">
					<html:select name="ordenControlSearchPageVO" property="ordenControl.origenOrden.id" styleClass="select">
						<html:optionsCollection name="ordenControlSearchPageVO" property="listOrigenOrdenVO" label="desOrigen" value="id" />
					</html:select>
				</td>					
			
			
			<!-- Estado Orden -->
				
				<td><label><bean:message bundle="ef" key="ef.estadoOrden.label"/>: </label></td>
				<td class="normal">
					<html:select name="ordenControlSearchPageVO" property="ordenControl.estadoOrden.id" styleClass="select">
						<html:optionsCollection name="ordenControlSearchPageVO" property="listEstadoOrdenVO" label="desEstadoOrden" value="id" />
					</html:select>
				</td>					
			</tr>
			
			<!-- Tipo Orden -->
			<tr>	
				<td><label><bean:message bundle="ef" key="ef.tipoOrden.label"/>: </label></td>
				<td class="normal">
					<html:select name="ordenControlSearchPageVO" property="ordenControl.tipoOrden.id" styleClass="select">
						<html:optionsCollection name="ordenControlSearchPageVO" property="listTipoOrdenVO" label="desTipoOrden" value="id" />
					</html:select>
				</td>					
			</tr>			
			
			<tr>
				<td><label><bean:message bundle="pad" key="pad.contribuyente.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:text name="ordenControlSearchPageVO" property="ordenControl.contribuyente.persona.represent" disabled="true" size="25" maxlength="100"/>
					<html:button property="btnBuscarCuenta" styleClass="boton" onclick="submitForm('buscarPersona', '');">
						<bean:message bundle="ef" key="ef.ordenControlFisSearchPage.button.buscarPersona"/>
					</html:button>			
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
		<logic:equal name="ordenControlSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="ordenControlSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1"> <!-- Seleccionar -->
								<input type="checkbox" onclick="changeChk('filter', 'idsSelected', this)"/>
							</th>
							<th width="1">&nbsp;</th> <!-- Liquidacion deuda -->
							<th width="1">&nbsp;</th> <!-- Estado cuenta -->
							<th width="1">&nbsp;</th> <!-- Modificar -->
							<th><bean:message bundle="pad" key="pad.persona.label"/></th> <!-- contribuyente -->
							<th><bean:message bundle="pad" key="pad.persona.cuit.label"/>
							<th><bean:message bundle="ef" key="ef.ordenControl.label"/></th>
							<th><bean:message bundle="ef" key="ef.opeInv.label"/>
							
							<th width="1"><bean:message bundle="ef" key="ef.estadoOrden.label"/></th> <!-- Estado -->
							<!-- <#ColumnTitles#> -->
						</tr>
							
						<logic:iterate id="OrdenControlVO" name="ordenControlSearchPageVO" property="listResult">
							<tr>
								<td>
									<html:multibox name="ordenControlSearchPageVO" property="idsSelected">
										<bean:write name="OrdenControlVO" property="idView"/>
									</html:multibox>
								</td>
								<!-- Liquidacion deuda -->
								<td>
									<logic:equal name="ordenControlSearchPageVO" property="liquidacionDeudaEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('liquidacionDeudaContr', '<bean:write name="OrdenControlVO" property="contribuyente.id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="gde" key="gde.deudaContribSearchPage.title"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/liquidacionDeuda0.gif"/>
											</a>
									</logic:equal>
									<logic:notEqual name="ordenControlSearchPageVO" property="liquidacionDeudaEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/liquidacionDeuda1.gif"/>
									</logic:notEqual>
								</td>
								
								<!-- Estado cuenta -->								
								<td>
									<logic:equal name="ordenControlSearchPageVO" property="estadoCuentaEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('estadoCuenta', '<bean:write name="OrdenControlVO" property="opeInvCon.cuenta.idView"/>');">
												<img title="<bean:message bundle="gde" key="gde.button.estadoCuenta"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/estadoCuenta1.gif"/>
											</a>										
									</logic:equal>
									<logic:notEqual name="ordenControlSearchPageVO" property="estadoCuentaEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/estadoCuenta1.gif"/>
									</logic:notEqual>
								</td>
								
								<!-- Modificar -->								
								<td>
									<logic:equal name="ordenControlSearchPageVO" property="modificarEnabled" value="enabled">
										<logic:equal name="OrdenControlVO" property="modificarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="OrdenControlVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="OrdenControlVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="ordenControlSearchPageVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</td>
								<td><bean:write name="OrdenControlVO" property="contribuyente.view"/></td>
								<td><bean:write name="OrdenControlVO" property="contribuyente.persona.cuit"/></td>
								<td><bean:write name="OrdenControlVO" property="ordenControlyTipoView"/></td>
								<td><bean:write name="OrdenControlVO" property="opeInvCon.opeInv.desOpeInv"/></td>
								<td><bean:write name="OrdenControlVO" property="estadoOrden.desEstadoOrden"/></td>
							</tr>
							
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="ordenControlSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="ordenControlSearchPageVO" property="listResult">
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
			<td align="left" width="50%">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<logic:equal name="ordenControlSearchPageVO" property="viewResult" value="true">
  	    		<td align="right">
  	    			<bean:define id="emitirEnabled" name="ordenControlSearchPageVO" property="emitirEnabled"/>
					<input type="button" <%=emitirEnabled%> class="boton" 
						onClick="submitForm('emitir', '0');" 
						value="<bean:message bundle="ef" key="ef.ordenControlContrSearchPage.button.emitir"/>"/>
				</td>
  	    		<td align="right">					
					<bean:define id="emitirManualEnabled" name="ordenControlSearchPageVO" property="emitirManualEnabled"/>
					<input type="button" <%=emitirManualEnabled%> class="boton" 
						onClick="submitForm('emitirManual', '0');" 
						value="<bean:message bundle="ef" key="ef.ordenControlContrSearchPage.button.emitirManual"/>"/>
				</td>
  	    		<td align="right">
					<bean:define id="imprimirEnabled" name="ordenControlSearchPageVO" property="imprimirEnabled"/>
					<input type="button" <%=imprimirEnabled%> class="boton" 
						onClick="submitForm('impresionOrdenesControl', '0');" 
						value="<bean:message bundle="ef" key="ef.ordenControlContrSearchPage.button.imprimir"/>"/>
				</td>
			</logic:equal>
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
<!-- ordenControlSearchPage.jsp -->