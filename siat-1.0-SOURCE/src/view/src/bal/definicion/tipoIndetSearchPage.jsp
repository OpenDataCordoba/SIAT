<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/BuscarTipoIndet.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="bal" key="bal.tipoIndetSearchPage.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>
					<logic:equal name="tipoIndetSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="base" key="base.busquedaLegendBusqueda"/>
						<bean:message bundle="bal" key="bal.tipoIndet.label"/>
					</logic:equal>
					<logic:notEqual name="tipoIndetSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="bal" key="bal.tipoIndetSearchPage.legend"/>
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
			<tr>
				<td><label><bean:message bundle="bal" key="bal.tipoIndet.codTipoIndet.label"/>: </label></td>
				<td class="normal"><html:text name="tipoIndetSearchPageVO" property="tipoIndet.codTipoIndet" size="15" maxlength="20" /></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.tipoIndet.desTipoIndet.label"/>: </label></td>
				<td class="normal"><html:text name="tipoIndetSearchPageVO" property="tipoIndet.desTipoIndet" size="30" maxlength="100"/></td>			
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.tipoIndet.codIndetMR.label"/>: </label></td>
				<td class="normal"><html:text name="tipoIndetSearchPageVO" property="tipoIndet.codIndetMR" size="15" maxlength="100"/></td>			
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
		<logic:equal name="tipoIndetSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="tipoIndetSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<logic:notEqual name="tipoIndetSearchPageVO" property="modoSeleccionar" value="true">
								<th width="1">&nbsp;</th> <!-- Modificar -->
								<th width="1">&nbsp;</th> <!-- Eliminar -->
								
							</logic:notEqual>
							<!-- <#ColumnTitles#> -->
								<th align="left"><bean:message bundle="bal" key="bal.tipoIndet.codTipoIndet.label"/></th>
								<th align="left"><bean:message bundle="bal" key="bal.tipoIndet.desTipoIndet.label"/></th>
								<th align="left"><bean:message bundle="bal" key="bal.tipoIndet.codIndetMR.label"/></th>
							<!-- <#ColumnTitles#> -->
						</tr>
						<logic:iterate id="TipoIndetVO" name="tipoIndetSearchPageVO" property="listResult">
							<tr>
								<!-- Seleccionar -->
								<logic:equal name="tipoIndetSearchPageVO" property="modoSeleccionar" value="true">
									<td>	
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="TipoIndetVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</td>
								</logic:equal>									
								<logic:notEqual name="tipoIndetSearchPageVO" property="modoSeleccionar" value="true">
									<!-- Ver -->
									<td>
										<logic:equal name="tipoIndetSearchPageVO" property="verEnabled" value="enabled">									
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="TipoIndetVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="TipoIndetVO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</td>	
									<!-- Modificar-->								
									<td>
										<logic:equal name="tipoIndetSearchPageVO" property="modificarEnabled" value="enabled">
											<logic:equal name="TipoIndetVO" property="modificarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="TipoIndetVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="TipoIndetVO" property="modificarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="tipoIndetSearchPageVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</td>
	
									<!-- Eliminar-->								
									<td>
										<logic:equal name="tipoIndetSearchPageVO" property="eliminarEnabled" value="enabled">
											<logic:equal name="TipoIndetVO" property="eliminarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="TipoIndetVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>
											</logic:equal>	
											<logic:notEqual name="TipoIndetVO" property="eliminarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="tipoIndetSearchPageVO" property="eliminarEnabled" value="enabled">										
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</td>
								
								
								
								
								</logic:notEqual>
								<!-- <#ColumnFiedls#> -->
									<td><bean:write name="TipoIndetVO" property="codTipoIndet"/>&nbsp;</td>
									<td><bean:write name="TipoIndetVO" property="desTipoIndet"/>&nbsp;</td>
									<td><bean:write name="TipoIndetVO" property="codIndetMR"/>&nbsp;</td>
								<!-- <#ColumnFiedls#> -->
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="tipoIndetSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="tipoIndetSearchPageVO" property="listResult">
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

	<table class="tablabotones" width="100%">
		<tr>				
			<td align="left" width="100%">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<logic:equal name="tipoIndetSearchPageVO" property="viewResult" value="true">
  	    		<td align="right" width="100%">
  	    			<logic:equal name="tipoIndetSearchPageVO" property="modoSeleccionar" value="false">
						<bean:define id="agregarEnabled" name="tipoIndetSearchPageVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar', '0');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
					</logic:equal>
  	    			<logic:equal name="tipoIndetSearchPageVO" property="modoSeleccionar" value="true">
  	    				<logic:equal name="tipoIndetSearchPageVO" property="agregarEnSeleccion" value="true">
							<bean:define id="agregarEnabled" name="tipoIndetSearchPageVO" property="agregarEnabled"/>
							<input type="button" <%=agregarEnabled%> class="boton" 
								onClick="submitForm('agregar', '0');" 
								value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
							</logic:equal>
					</logic:equal>
				</td>
			</logic:equal>
		</tr>
	</table>
	<input type="hidden" name="name"  value="<bean:write name='tipoIndetSearchPageVO' property='name'/>" id="name"/>
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