<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/BuscarNodo.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="bal" key="bal.nodoSearchPage.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">				
				<p>
					<logic:equal name="nodoSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="base" key="base.busquedaLegendBusqueda"/>
						<bean:message bundle="bal" key="bal.nodo.label"/>
					</logic:equal>
					<logic:notEqual name="nodoSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="bal" key="bal.nodoSearchPage.legend"/>
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
				<td><label><bean:message bundle="bal" key="bal.clasificador.label"/>: </label></td>
				<td class="normal">
					<html:select name="nodoSearchPageVO" property="nodo.clasificador.id" styleClass="select" >
						<html:optionsCollection name="nodoSearchPageVO" property="listClasificador" label="descripcion" value="id" />
					</html:select>
				</td>		
				<td class="normal">
					<input type="checkbox" name="checkearConsistencia">
					<label><bean:message bundle="bal" key="bal.nodoSearchPage.checkearConsistencia"/></label>
				</td>
			</tr>
		</table>
		<p align="center">
		  	<html:button property="btnLimpiar"  styleClass="boton" onclick="submitForm('limpiar', '');">
				<bean:message bundle="base" key="abm.button.limpiar"/>
			</html:button>
			&nbsp;
			<html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromSearchPage', '1');">
				<bean:message bundle="base" key="abm.button.imprimir"/>
			</html:button>	
			&nbsp;
		  	<html:button property="btnBuscar"  styleClass="boton" onclick="submitForm('buscar', '');">
				<bean:message bundle="base" key="abm.button.buscar"/>
			</html:button>
		</p>
		<!-- Aclaracion -->
		<label><bean:message bundle="bal" key="bal.nodoSearchPage.warning"/></label>
	</fieldset>	
	<!-- Fin Filtro -->
	<a name="arbol">&nbsp;</a>
	<logic:empty  name="nodoSearchPageVO" property="listResult">	
		<div id="resultadoFiltro">
	</logic:empty>
	<logic:notEmpty  name="nodoSearchPageVO" property="listResult">	
		<div id="resultadoFiltro" class="scrolable" style="height: 400px;">
	</logic:notEmpty>
	<!-- Resultado Filtro --> 
		<logic:equal name="nodoSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="nodoSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th>
							<logic:notEqual name="nodoSearchPageVO" property="modoSeleccionar" value="true">
								<th width="1">&nbsp;</th>
								<th width="1">&nbsp;</th>
								<th width="1">&nbsp;</th>
							</logic:notEqual>
							<th width="1">
							<logic:equal name="nodoSearchPageVO" property="arbolExpandido" value="true">
								<a style="cursor: pointer; cursor: hand" onclick="submitForm('expandirContraer', '-1');">
									<img title="<bean:message bundle="base" key="abm.button.contraer"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/contraer0.gif" />
								</a>
							</logic:equal>
							<logic:equal name="nodoSearchPageVO" property="arbolExpandido" value="false">
								<a style="cursor: pointer; cursor: hand" onclick="submitForm('expandirContraer', '-1');">
									<img title="<bean:message bundle="base" key="abm.button.expandir"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/expandir0.gif" />
								</a>
							</logic:equal>
							</th>
							<th align="left"><bean:message bundle="bal" key="bal.nodo.clave.label"/></th>
							<th align="left"><bean:message bundle="bal" key="bal.nodo.descripcion.label"/></th>
						</tr>
						<logic:iterate id="NodoVO" name="nodoSearchPageVO" property="listResult">
							<tr>
									<td>
										<logic:notEqual name="nodoSearchPageVO" property="modoSeleccionar" value="true">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="NodoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:notEqual>
										<logic:equal name="nodoSearchPageVO" property="modoSeleccionar" value="true">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="NodoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>									
									</td>
									<logic:notEqual name="nodoSearchPageVO" property="modoSeleccionar" value="true">															
									<td>
										<logic:equal name="nodoSearchPageVO" property="modificarEnabled" value="enabled">
											<logic:equal name="NodoVO" property="modificarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="NodoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="NodoVO" property="modificarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="nodoSearchPageVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</td>
									<td>
										<logic:equal name="nodoSearchPageVO" property="eliminarEnabled" value="enabled">
											<logic:equal name="NodoVO" property="eliminarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="NodoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>
											</logic:equal>	
											<logic:notEqual name="NodoVO" property="eliminarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="nodoSearchPageVO" property="eliminarEnabled" value="enabled">										
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</td>
									<td>
										<logic:equal name="nodoSearchPageVO" property="agregarEnabled" value="enabled">
											<logic:equal name="NodoVO" property="agregarEnabled" value="true">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('agregar', '<bean:write name="NodoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.agregar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/agregar0.gif"/>
												</a>
											</logic:equal>
											<logic:equal name="NodoVO" property="agregarEnabled" value="false">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/agregar1.gif"/>
											</logic:equal>
										</logic:equal>
										<logic:notEqual name="nodoSearchPageVO" property="agregarEnabled" value="enabled">										
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/agregar1.gif"/>
										</logic:notEqual>
									</td>
								</logic:notEqual>
									<td>
										<a style="cursor: pointer; cursor: hand" onclick="submitForm('expandirContraer', '<bean:write name="NodoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<logic:equal name="NodoVO" property="expandido" value="true">																		
											<img title="<bean:message bundle="base" key="abm.button.contraer"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/contraer0.gif" />
										</logic:equal>
										<logic:equal name="NodoVO" property="expandido" value="false">																		
											<img title="<bean:message bundle="base" key="abm.button.expandir"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/expandir0.gif" />
										</logic:equal>
										</a>
									</td>
								<logic:equal name="NodoVO" property="faltanDatos" value="true">																		
									<td style="color:red"><bean:write name="NodoVO" property="clave"/>&nbsp;</td>
									<td><pre class="normal" style="color:red"><bean:write name="NodoVO" property="descripcionTab" />&nbsp;</pre></td>
								</logic:equal>
								<logic:equal name="NodoVO" property="faltanDatos" value="false">																		
									<td><bean:write name="NodoVO" property="clave"/>&nbsp;</td>
									<td><pre class="normal"><bean:write name="NodoVO" property="descripcionTab" />&nbsp;</pre></td>
								</logic:equal>
							</tr>
						</logic:iterate>
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="nodoSearchPageVO" property="listResult">
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
	<!-- Fin Resultado Filtro -->
	</div>

	<table class="tablabotones">
		<tr>				
			<td align="left">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<logic:equal name="nodoSearchPageVO" property="viewResult" value="true">
    			<td align="right">
				<logic:equal name="nodoSearchPageVO" property="modoSeleccionar" value="false">
						<bean:define id="agregarEnabled" name="nodoSearchPageVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar', '0');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
					</logic:equal>
  	    			<logic:equal name="nodoSearchPageVO" property="modoSeleccionar" value="true">
  	    				<logic:equal name="nodoSearchPageVO" property="agregarEnSeleccion" value="true">
							<bean:define id="agregarEnabled" name="nodoSearchPageVO" property="agregarEnabled"/>
							<input type="button" <%=agregarEnabled%> class="boton" 
								onClick="submitForm('agregar', '0');" 
								value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
						</logic:equal>
					</logic:equal>
				</td>
			</logic:equal>
		</tr>
	</table>
  	<input type="hidden" name="name"  value="<bean:write name='nodoSearchPageVO' property='name'/>" id="name"/>
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

<script type="text/javascript">
	function irAArbol() {
   		document.location = document.URL + '#arbol';
	}
</script>

<logic:equal name="nodoSearchPageVO" property="viewResult" value="true">
	<script type="text/javascript">irAArbol();</script>
</logic:equal>