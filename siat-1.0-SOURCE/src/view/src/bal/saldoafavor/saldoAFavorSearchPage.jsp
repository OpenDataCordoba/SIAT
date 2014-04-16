<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/BuscarSaldoAFavor.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="bal" key="bal.saldoAFavorSearchPage.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>
					<logic:equal name="saldoAFavorSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="base" key="base.busquedaLegendBusqueda"/>
						<bean:message bundle="bal" key="bal.saldoAFavor.label"/>
					</logic:equal>
					<logic:notEqual name="saldoAFavorSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="bal" key="bal.saldoAFavorSearchPage.legend"/>
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
			<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal">
					<html:select name="saldoAFavorSearchPageVO" property="saldoAFavor.cuenta.recurso.id" styleClass="select" >
						<bean:define id="includeRecursoList" name="saldoAFavorSearchPageVO" property="listRecurso"/>
						<bean:define id="includeIdRecursoSelected" name="saldoAFavorSearchPageVO" property="saldoAFavor.cuenta.recurso.id"/>
						<%@ include file="/def/gravamen/includeRecurso.jsp" %>
					</html:select>
			</td>
		</tr>
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.saldoAFavor.cuenta.label"/>: </label></td>
			<td class="normal"><html:text name="saldoAFavorSearchPageVO" property="saldoAFavor.cuenta.numeroCuenta" size="20" maxlength="100"/></td>			
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
		<logic:equal name="saldoAFavorSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="saldoAFavorSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<logic:notEqual name="saldoAFavorSearchPageVO" property="modoSeleccionar" value="true">
								<th width="1">&nbsp;</th> <!-- Modificar -->
								<th width="1">&nbsp;</th> <!-- Eliminar -->
							</logic:notEqual>
							<th align="left"><bean:message bundle="bal" key="bal.saldoAFavor.fechaGeneracion.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.saldoAFavor.origen.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.saldoAFavor.importe.label"/></th>
							<th align="left"><bean:message bundle="base" key="base.estado.label"/></th>
							<!-- <#ColumnTitles#> -->
						</tr>
							
						<logic:iterate id="SaldoAFavorVO" name="saldoAFavorSearchPageVO" property="listResult">
							<tr>
								<!-- Seleccionar -->
								<logic:equal name="saldoAFavorSearchPageVO" property="modoSeleccionar" value="true">
									<td>	
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="SaldoAFavorVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</td>
								</logic:equal>									
								<logic:notEqual name="saldoAFavorSearchPageVO" property="modoSeleccionar" value="true">
									<!-- Ver -->
									<td>
										<logic:equal name="saldoAFavorSearchPageVO" property="verEnabled" value="enabled">									
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="SaldoAFavorVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="SaldoAFavorVO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</td>	
									<!-- Modificar-->								
									<td>
										<logic:equal name="saldoAFavorSearchPageVO" property="modificarEnabled" value="enabled">
											<logic:equal name="SaldoAFavorVO" property="modificarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="SaldoAFavorVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="SaldoAFavorVO" property="modificarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="saldoAFavorSearchPageVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</td>
	
									<!-- Eliminar-->								
									<td>
										<logic:equal name="saldoAFavorSearchPageVO" property="eliminarEnabled" value="enabled">
											<logic:equal name="SaldoAFavorVO" property="eliminarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="SaldoAFavorVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>
											</logic:equal>	
											<logic:notEqual name="SaldoAFavorVO" property="eliminarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="saldoAFavorSearchPageVO" property="eliminarEnabled" value="enabled">										
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</td>
								</logic:notEqual>
								<td><bean:write name="SaldoAFavorVO" property="fechaGeneracionView"/>&nbsp;</td>
								<td><bean:write name="SaldoAFavorVO" property="tipoOrigen.desTipoOrigen"/>&nbsp;</td>
								<td><bean:write name="SaldoAFavorVO" property="importeView"/>&nbsp;</td>
								<td>
									<logic:equal name="SaldoAFavorVO" property="estaCancelado" value="true">
											<bean:message bundle="bal" key="bal.saldoAFavor.estaCancelado.label"/>
									</logic:equal> 
									<logic:equal name="SaldoAFavorVO" property="estaCancelado" value="false">
										<logic:equal name="SaldoAFavorVO" property="enCompensacion" value="true">
											<bean:message bundle="bal" key="bal.saldoAFavor.enCompensacion.label"/>
										</logic:equal> 					
										<logic:equal name="SaldoAFavorVO" property="enCompensacion" value="false">
											<bean:write name="SaldoAFavorVO" property="estado.value"/>
										</logic:equal> 					
									</logic:equal> 					
								&nbsp;</td>
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="saldoAFavorSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="saldoAFavorSearchPageVO" property="listResult">
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
			<logic:equal name="saldoAFavorSearchPageVO" property="viewResult" value="true">
  	    		<td align="right">
  	    			<logic:equal name="saldoAFavorSearchPageVO" property="modoSeleccionar" value="false">
						<bean:define id="agregarEnabled" name="saldoAFavorSearchPageVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar', '0');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
					</logic:equal>
  	    			<logic:equal name="saldoAFavorSearchPageVO" property="modoSeleccionar" value="true">
  	    				<logic:equal name="saldoAFavorSearchPageVO" property="agregarEnSeleccion" value="true">
							<bean:define id="agregarEnabled" name="saldoAFavorSearchPageVO" property="agregarEnabled"/>
							<input type="button" <%=agregarEnabled%> class="boton" 
								onClick="submitForm('agregar', '0');" 
								value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
							</logic:equal>
					</logic:equal>
				</td>
			</logic:equal>
		</tr>
	</table>
	<input type="hidden" name="name"  value="<bean:write name='saldoAFavorSearchPageVO' property='name'/>" id="name"/>
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
