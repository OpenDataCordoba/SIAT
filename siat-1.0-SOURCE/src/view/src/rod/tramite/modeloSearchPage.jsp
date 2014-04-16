<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/rod/BuscarModelo.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="rod" key="rod.modeloSearchPage.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<bean:message bundle="rod" key="rod.modeloSearchPage.title"/>
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
				<td><label><bean:message bundle="rod" key="rod.modelo.codModelo.label"/>: </label></td>
				<td class="normal"><html:text name="modeloSearchPageVO" property="modelo.codModelo" size="15" maxlength="20" styleClass="datos" /></td>
			
				<td><label><bean:message bundle="rod" key="rod.modelo.desModelo.label"/>: </label></td>
				<td class="normal"><html:text name="modeloSearchPageVO" property="modelo.desModelo" size="20" maxlength="100" styleClass="datos" /></td>

			</tr>
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
		
			<logic:notEmpty  name="modeloSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							
							<th align="left"><bean:message bundle="rod" key="rod.modelo.codModelo.label"/></th>
							<th align="left"><bean:message bundle="rod" key="rod.modelo.codMarca.label"/></th>
							<th align="left"><bean:message bundle="rod" key="rod.modelo.codTipoGen.label"/></th>
							<th align="left"><bean:message bundle="rod" key="rod.modelo.codTipoEsp.label"/></th>
							<th align="left"><bean:message bundle="rod" key="rod.modelo.desModelo.label"/></th>
							
						</tr>
							
						<logic:iterate id="ModeloVO" name="modeloSearchPageVO" property="listResult">
							<tr>
								<!-- Seleccionar -->
								<logic:equal name="modeloSearchPageVO" property="modoSeleccionar" value="true">
									<td>	
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="ModeloVO" property="codModelo" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</td>
								</logic:equal>									
			
								<td><bean:write name="ModeloVO" property="codModelo" bundle="base" formatKey="general.format.id"/>&nbsp;</td>
								<td><bean:write name="ModeloVO" property="marca.desMarca"/>&nbsp;</td>
								<td><bean:write name="ModeloVO" property="tipoVehiculo.desTipoGen"/>&nbsp;</td>
								<td><bean:write name="ModeloVO" property="tipoVehiculo.desTipoEsp"/>&nbsp;</td>
								<td><bean:write name="ModeloVO" property="desModelo" />&nbsp;</td>
							
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="modeloSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="modeloSearchPageVO" property="listResult">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
                	<tbody>
						<tr><td align="center">
							<bean:message bundle="base" key="base.resultadoVacio"/>
						</td></tr>
					</tbody>			
				</table>
			</logic:empty>
					
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
	<input type="hidden" name="name"  value="<bean:write name='modeloSearchPageVO' property='name'/>" id="name"/>
   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
		
	<input type="hidden" name="method" value=""/>
    <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
    <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>

			
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->