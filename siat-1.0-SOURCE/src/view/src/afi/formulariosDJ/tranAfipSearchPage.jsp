<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/afi/BuscarTranAfip.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="afi" key="afi.tranAfipSearchPage.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>
					<logic:equal name="tranAfipSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="base" key="base.busquedaLegendBusqueda"/>
						<bean:message bundle="afi" key="afi.tranAfip.label"/>
					</logic:equal>
					<logic:notEqual name="tranAfipSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="afi" key="afi.tranAfipSearchPage.legend"/>
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
			
			
			<!-- <#Filtros#> -->
			<#Filtros.TextCodigo#>
			<tr>
				<td><label><bean:message bundle="afi" key="afi.tranAfip.codTranAfip.label"/>: </label></td>
				<td class="normal"><html:text name="tranAfipSearchPageVO" property="tranAfip.codTranAfip" size="15" maxlength="20" /></td>
			</tr>
			<#Filtros.TextCodigo#>
			<#Filtros.TextDescripcion#>
			<tr>
				<td><label><bean:message bundle="afi" key="afi.tranAfip.desTranAfip.label"/>: </label></td>
				<td class="normal"><html:text name="tranAfipSearchPageVO" property="tranAfip.desTranAfip" size="20" maxlength="100"/></td>			
			</tr>
			<#Filtros.TextDescripcion#>
			<#Filtros.Combo#>
			<tr>	
				<td><label><bean:message bundle="afi" key="afi.${bean_a_listar}.label"/>: </label></td>
				<td class="normal">
					<html:select name="tranAfipSearchPageVO" property="tranAfip.${bean_a_listar}.id" styleClass="select">
						<html:optionsCollection name="tranAfipSearchPageVO" property="list${Bean_a_listar}" label="des${Bean_a_listar}" value="id" />
					</html:select>
				</td>					
			</tr>
			<#Filtros.Combo#>
			<#Filtros.ComboSiNo#>
			<tr>	
				<td><label><bean:message bundle="afi" key="afi.tranAfip.${propiedad}.label"/>: </label></td>
				<td class="normal">
					<html:select name="tranAfipSearchPageVO" property="tranAfip.${propiedad}.id" styleClass="select">
						<html:optionsCollection name="tranAfipSearchPageVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>					
			</tr>
			<#Filtros.ComboSiNo#>
			<#Filtros.Text#>
			<tr>
				<td><label><bean:message bundle="afi" key="afi.tranAfip.${propiedad}.label"/>: </label></td>
				<td class="normal"><html:text name="tranAfipSearchPageVO" property="tranAfip.${propiedad}" size="20" maxlength="100"/></td>			
			</tr>
			<#Filtros.Text#>
			<#Filtros.TextFecha#>
			<tr>
				<td><label><bean:message bundle="afi" key="afi.tranAfip.${propiedadFecha}.label"/>: </label></td>
				<td class="normal">
					<html:text name="tranAfipSearchPageVO" property="tranAfip.${propiedadFecha}View" styleId="${propiedadFecha}View" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('${propiedadFecha}View');" id="a_${propiedadFecha}View">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
			<#Filtros.TextFecha#>
			<#Filtros.BuscarBeanRef#>
			<tr>
				<td><label><bean:message bundle="${modulo_ref}" key="${modulo_ref}.${bean_ref}.${propiedad_ref}.ref"/>: </label></td>
				<td class="normal" colspan="3">
					<html:text name="tranAfipSearchPageVO" property="tranAfip.${bean_ref}.${propiedad_ref}" size="20" disabled="true"/>
					<html:button property="btnBuscar${Bean_Ref}"  styleClass="boton" onclick="submitForm('buscar${Bean_Ref}', '');">
						<bean:message bundle="afi" key="afi.tranAfipSearchPage.button.buscar${Bean_Ref}"/>
					</html:button>
				</td>
			</tr>
			<#Filtros.BuscarBeanRef#>
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
	<div id="resultadoFiltro" class="scrolable" style="height: 300px;">	
	<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.cierreBanco.listTranAfip.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="cierreBancoAdapterVO" property="cierreBanco.listTranAfip">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
						<th width="1">&nbsp;</th> <!-- Generar DecJur -->	
						<th width="1">&nbsp;</th> <!-- Eliminar -->		
						<th align="left"><bean:message bundle="bal" key="bal.tranAfip.idTransaccionAfip.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.tranAfip.formulario.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.tipoOperacion.title"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.tranAfip.fechaProceso.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.tranAfip.cuit.ref"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.tranAfip.totMontoIngresado.label"/></th>												
					</tr>
					<logic:iterate id="TranAfipVO" name="cierreBancoAdapterVO" property="cierreBanco.listTranAfip">
					<!-- Ver/Seleccionar -->
						<tr>
						<td>
							<logic:notEqual name="cierreBancoAdapterVO" property="modoSeleccionar" value="true">
								<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verTransaccioAfip', '<bean:write name="TranAfipVO" property="id" bundle="base" formatKey="general.format.id"/>');">
									<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
								</a>
							</logic:notEqual>																
						</td>				
						<!-- GenerarTransaccion -->
						<td>
							<logic:equal name="cierreBancoAdapterVO" property="generarDecJurEnabled" value="enabled">		
								<logic:equal name="TranAfipVO" property="generarDecJurEnabled" value="enabled">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('generarDecJur', '<bean:write name="TranAfipVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="bal" key="bal.cierreBancoAdapter.button.generarDecJur"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/aplicar0.gif"/>
									</a>
								</logic:equal>	
								<logic:notEqual name="TranAfipVO" property="generarDecJurEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/aplicar1.gif"/>
								</logic:notEqual>									
							</logic:equal>							
							<logic:notEqual name="cierreBancoAdapterVO" property="generarDecJurEnabled" value="enabled">		
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/aplicar1.gif"/>
							</logic:notEqual>
						</td>	
						<!-- EliminarTransaccion -->
						<td>
							<logic:equal name="cierreBancoAdapterVO" property="eliminarTranAfipEnabled" value="enabled">
								<logic:equal name="TranAfipVO" property="eliminarTranAfipEnabled" value="enabled">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarTranAfip', '<bean:write name="TranAfipVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
									</a>
								</logic:equal>	
								<logic:notEqual name="TranAfipVO" property="eliminarTranAfipEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>									
							</logic:equal>							
							<logic:notEqual name="cierreBancoAdapterVO" property="eliminarTranAfipEnabled" value="enabled">		
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
							</logic:notEqual>
						</td>										
							<td><bean:write name="TranAfipVO" property="idTransaccionAfipView"/>&nbsp;</td>
							<td><bean:write name="TranAfipVO" property="formularioView"/>&nbsp;</td>	
							<td><bean:write name="TranAfipVO" property="tipoOperacion.desTipoOperacion"/>&nbsp;</td>	
							<td><bean:write name="TranAfipVO" property="fechaProcesoView"/>&nbsp;</td>	
							<td><bean:write name="TranAfipVO" property="cuit"/>&nbsp;</td>					
							<td><bean:write name="TranAfipVO" property="totMontoIngresadoView"/>&nbsp;</td>																	
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="cierreBancoAdapterVO" property="cierreBanco.listTranAfip">
					<tr>
						<td align="center">
							<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td>
					</tr>
				</logic:empty>
			</tbody>
	</table>
	</div>
	<!-- Resultado Filtro -->

	<table class="tablabotones">
		<tr>				
			<td align="left">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<logic:equal name="tranAfipSearchPageVO" property="viewResult" value="true">
  	    		<td align="right">
  	    			<logic:equal name="tranAfipSearchPageVO" property="modoSeleccionar" value="false">
						<bean:define id="agregarEnabled" name="tranAfipSearchPageVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar', '0');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
					</logic:equal>
  	    			<logic:equal name="tranAfipSearchPageVO" property="modoSeleccionar" value="true">
  	    				<logic:equal name="tranAfipSearchPageVO" property="agregarEnSeleccion" value="true">
							<bean:define id="agregarEnabled" name="tranAfipSearchPageVO" property="agregarEnabled"/>
							<input type="button" <%=agregarEnabled%> class="boton" 
								onClick="submitForm('agregar', '0');" 
								value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
							</logic:equal>
					</logic:equal>
				</td>
			</logic:equal>
		</tr>
	</table>
	<input type="hidden" name="name"  value="<bean:write name='tranAfipSearchPageVO' property='name'/>" id="name"/>
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