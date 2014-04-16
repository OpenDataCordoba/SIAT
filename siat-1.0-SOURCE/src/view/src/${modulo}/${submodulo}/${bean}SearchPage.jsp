<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/${modulo}/Buscar${Bean}.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="${modulo}" key="${modulo}.${bean}SearchPage.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>
					<logic:equal name="${bean}SearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="base" key="base.busquedaLegendBusqueda"/>
						<bean:message bundle="${modulo}" key="${modulo}.${bean}.label"/>
					</logic:equal>
					<logic:notEqual name="${bean}SearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="${modulo}" key="${modulo}.${bean}SearchPage.legend"/>
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
				<td><label><bean:message bundle="${modulo}" key="${modulo}.${bean}.cod${Bean}.label"/>: </label></td>
				<td class="normal"><html:text name="${bean}SearchPageVO" property="${bean}.cod${Bean}" size="15" maxlength="20" /></td>
			</tr>
			<#Filtros.TextCodigo#>
			<#Filtros.TextDescripcion#>
			<tr>
				<td><label><bean:message bundle="${modulo}" key="${modulo}.${bean}.des${Bean}.label"/>: </label></td>
				<td class="normal"><html:text name="${bean}SearchPageVO" property="${bean}.des${Bean}" size="20" maxlength="100"/></td>			
			</tr>
			<#Filtros.TextDescripcion#>
			<#Filtros.Combo#>
			<tr>	
				<td><label><bean:message bundle="${modulo}" key="${modulo}.${bean_a_listar}.label"/>: </label></td>
				<td class="normal">
					<html:select name="${bean}SearchPageVO" property="${bean}.${bean_a_listar}.id" styleClass="select">
						<html:optionsCollection name="${bean}SearchPageVO" property="list${Bean_a_listar}" label="des${Bean_a_listar}" value="id" />
					</html:select>
				</td>					
			</tr>
			<#Filtros.Combo#>
			<#Filtros.ComboSiNo#>
			<tr>	
				<td><label><bean:message bundle="${modulo}" key="${modulo}.${bean}.${propiedad}.label"/>: </label></td>
				<td class="normal">
					<html:select name="${bean}SearchPageVO" property="${bean}.${propiedad}.id" styleClass="select">
						<html:optionsCollection name="${bean}SearchPageVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>					
			</tr>
			<#Filtros.ComboSiNo#>
			<#Filtros.Text#>
			<tr>
				<td><label><bean:message bundle="${modulo}" key="${modulo}.${bean}.${propiedad}.label"/>: </label></td>
				<td class="normal"><html:text name="${bean}SearchPageVO" property="${bean}.${propiedad}" size="20" maxlength="100"/></td>			
			</tr>
			<#Filtros.Text#>
			<#Filtros.TextFecha#>
			<tr>
				<td><label><bean:message bundle="${modulo}" key="${modulo}.${bean}.${propiedadFecha}.label"/>: </label></td>
				<td class="normal">
					<html:text name="${bean}SearchPageVO" property="${bean}.${propiedadFecha}View" styleId="${propiedadFecha}View" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('${propiedadFecha}View');" id="a_${propiedadFecha}View">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
			<#Filtros.TextFecha#>
			<#Filtros.BuscarBeanRef#>
			<tr>
				<td><label><bean:message bundle="${modulo_ref}" key="${modulo_ref}.${bean_ref}.${propiedad_ref}.ref"/>: </label></td>
				<td class="normal" colspan="3">
					<html:text name="${bean}SearchPageVO" property="${bean}.${bean_ref}.${propiedad_ref}" size="20" disabled="true"/>
					<html:button property="btnBuscar${Bean_Ref}"  styleClass="boton" onclick="submitForm('buscar${Bean_Ref}', '');">
						<bean:message bundle="${modulo}" key="${modulo}.${bean}SearchPage.button.buscar${Bean_Ref}"/>
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
	<div id="resultadoFiltro">
		<logic:equal name="${bean}SearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="${bean}SearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<logic:notEqual name="${bean}SearchPageVO" property="modoSeleccionar" value="true">
								<th width="1">&nbsp;</th> <!-- Modificar -->
								<th width="1">&nbsp;</th> <!-- Eliminar -->
								<th width="1">&nbsp;</th> <!-- Activar Desactivar -->
							</logic:notEqual>
							<!-- <#ColumnTitles#> -->
							<#ColumnTitles.Codigo#>
								<th align="left"><bean:message bundle="${modulo}" key="${modulo}.${bean}.cod${Bean}.label"/></th>
							<#ColumnTitles.Codigo#>
							<#ColumnTitles.Descripcion#>
								<th align="left"><bean:message bundle="${modulo}" key="${modulo}.${bean}.des${Bean}.label"/></th>
							<#ColumnTitles.Descripcion#>
							<#ColumnTitles.Propiedad#>
								<th align="left"><bean:message bundle="${modulo}" key="${modulo}.${bean}.${propiedad}.label"/></th>
							<#ColumnTitles.Propiedad#>
							<#ColumnTitles.PropiedadRef#>
								<th align="left"><bean:message bundle="${moduloRef}" key="${moduloRef}.${beanRef}.${propiedad}.label"/></th>
							<#ColumnTitles.PropiedadRef#>
							<#ColumnTitles.Estado#>
								<th align="left"><bean:message bundle="base" key="base.estado.label"/></th>
							<#ColumnTitles.Estado#>
							<!-- <#ColumnTitles#> -->
						</tr>
							
						<logic:iterate id="${Bean}VO" name="${bean}SearchPageVO" property="listResult">
							<tr>
								<!-- Seleccionar -->
								<logic:equal name="${bean}SearchPageVO" property="modoSeleccionar" value="true">
									<td>	
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="${Bean}VO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</td>
								</logic:equal>									
								<logic:notEqual name="${bean}SearchPageVO" property="modoSeleccionar" value="true">
									<!-- Ver -->
									<td>
										<logic:equal name="${bean}SearchPageVO" property="verEnabled" value="enabled">									
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="${Bean}VO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="${Bean}VO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</td>	
									<!-- Modificar-->								
									<td>
										<logic:equal name="${bean}SearchPageVO" property="modificarEnabled" value="enabled">
											<logic:equal name="${Bean}VO" property="modificarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="${Bean}VO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="${Bean}VO" property="modificarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="${bean}SearchPageVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</td>
	
									<!-- Eliminar-->								
									<td>
										<logic:equal name="${bean}SearchPageVO" property="eliminarEnabled" value="enabled">
											<logic:equal name="${Bean}VO" property="eliminarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="${Bean}VO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>
											</logic:equal>	
											<logic:notEqual name="${Bean}VO" property="eliminarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="${bean}SearchPageVO" property="eliminarEnabled" value="enabled">										
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</td>
									<td>
										<!-- Activar -->
										<logic:equal name="${Bean}VO" property="estado.id" value="0">
											<logic:equal name="${bean}SearchPageVO" property="activarEnabled" value="enabled">
												<logic:equal name="${Bean}VO" property="activarEnabled" value="enabled">
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('activar', '<bean:write name="${Bean}VO" property="id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="base" key="abm.button.activar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/activar0.gif"/>
													</a>
												</logic:equal> 
												<logic:notEqual name="${Bean}VO" property="activarEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/activar1.gif"/>
												</logic:notEqual>
											</logic:equal>
											<logic:notEqual name="${bean}SearchPageVO" property="activarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/activar1.gif"/>
											</logic:notEqual>
										</logic:equal> 
										<!-- Desactivar -->
										<logic:equal name="${Bean}VO" property="estado.id" value="1">
											<logic:equal name="${bean}SearchPageVO" property="desactivarEnabled" value="enabled">
												<logic:equal name="${Bean}VO" property="desactivarEnabled" value="enabled">
													<a style="cursor: pointer; cursor: hand;" onclick="submitForm('desactivar', '<bean:write name="${Bean}VO" property="id" bundle="base" formatKey="general.format.id"/>');">
														<img title="<bean:message bundle="base" key="abm.button.desactivar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar0.gif"/>
													</a>
												</logic:equal>
												<logic:notEqual name="${Bean}VO" property="desactivarEnabled" value="enabled">
													<img border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar1.gif"/>
												</logic:notEqual>
											</logic:equal>
											<logic:notEqual name="${bean}SearchPageVO" property="desactivarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/desactivar1.gif"/>
											</logic:notEqual>										
										</logic:equal>
										<!-- En estado creado -->
										<logic:equal name="${Bean}VO" property="estado.id" value="-1">
											<a style="cursor: pointer; cursor: hand;">
											<img border="0" title="<bean:message bundle="base" key="abm.button.creado"/>" src="<%=request.getContextPath()%>/images/iconos/creado0.gif"/>
											</a>
										</logic:equal> 
									</td>
								</logic:notEqual>
								<!-- <#ColumnFiedls#> -->
								<#ColumnFiedls.Codigo#>
									<td><bean:write name="${Bean}VO" property="cod${Bean}"/>&nbsp;</td>
								<#ColumnFiedls.Codigo#>
								<#ColumnFiedls.Descripcion#>
									<td><bean:write name="${Bean}VO" property="des${Bean}"/>&nbsp;</td>
								<#ColumnFiedls.Descripcion#>
								<#ColumnFiedls.PropiedadString#>
									<td><bean:write name="${Bean}VO" property="${propiedad}"/>&nbsp;</td>
								<#ColumnFiedls.PropiedadString#>
								<#ColumnFiedls.PropiedadView#>
									<td><bean:write name="${Bean}VO" property="${propiedad}View"/>&nbsp;</td>
								<#ColumnFiedls.PropiedadView#>								
								<#ColumnFiedls.PropiedadSiNo#>
									<td><bean:write name="${Bean}VO" property="${propiedad}.value"/>&nbsp;</td>
								<#ColumnFiedls.PropiedadSiNo#>
								<#BeanWrites.PropiedadCurrency#>
									<td><bean:message bundle="base" key="base.currency"/><bean:write name="${Bean}VO" property="${propiedad}View"/>&nbsp;</td>
								<#BeanWrites.PropiedadCurrency#>								
								<#ColumnFiedls.Estado#>
									<td><bean:write name="${Bean}VO" property="estado.value"/>&nbsp;</td>
								<#ColumnFiedls.Estado#>
								<!-- <#ColumnFiedls#> -->
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="${bean}SearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="${bean}SearchPageVO" property="listResult">
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
			<logic:equal name="${bean}SearchPageVO" property="viewResult" value="true">
  	    		<td align="right">
  	    			<logic:equal name="${bean}SearchPageVO" property="modoSeleccionar" value="false">
						<bean:define id="agregarEnabled" name="${bean}SearchPageVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar', '0');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
					</logic:equal>
  	    			<logic:equal name="${bean}SearchPageVO" property="modoSeleccionar" value="true">
  	    				<logic:equal name="${bean}SearchPageVO" property="agregarEnSeleccion" value="true">
							<bean:define id="agregarEnabled" name="${bean}SearchPageVO" property="agregarEnabled"/>
							<input type="button" <%=agregarEnabled%> class="boton" 
								onClick="submitForm('agregar', '0');" 
								value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
							</logic:equal>
					</logic:equal>
				</td>
			</logic:equal>
		</tr>
	</table>
	<input type="hidden" name="name"  value="<bean:write name='${bean}SearchPageVO' property='name'/>" id="name"/>
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