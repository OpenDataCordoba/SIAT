<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/pro/BuscarProceso.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="pro" key="pro.procesoSearchPage.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>
					<logic:equal name="procesoSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="base" key="base.busquedaLegendBusqueda"/>
						<bean:message bundle="pro" key="pro.proceso.label"/>
					</logic:equal>
					<logic:notEqual name="procesoSearchPageVO" property="modoSeleccionar" value="true">
						<bean:message bundle="pro" key="pro.procesoSearchPage.legend"/>
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
		
		<!-- Codigo -->
		<tr>	
			<td><label><bean:message bundle="pro" key="pro.proceso.codProceso.label"/>: </label></td>
			<td class="normal">
				<html:select name="procesoSearchPageVO" property="proceso.codProceso" styleClass="select">
						<html:options name="procesoSearchPageVO" property="listCodigo"/>					
				</html:select>
			</td>					
		</tr>
		
		<!-- Descripcion -->
		<tr>
			<td><label><bean:message bundle="pro" key="pro.proceso.desProceso.label"/>: </label></td>
			<td class="normal"><html:text name="procesoSearchPageVO" property="proceso.desProceso" size="30" maxlength="100"/></td>			
		</tr>
		
		<!-- Locked -->
		<tr>
			<td><label><bean:message bundle="pro" key="pro.proceso.locked.label"/>: </label></td>
			<td class="normal">
				<html:select name="procesoSearchPageVO" property="proceso.locked.id" styleClass="select">
						<html:optionsCollection name="procesoSearchPageVO" property="listSiNo" label="value" value="id"/>					
				</html:select>
			</td>			
		</tr>
		
			<!-- <#Filtros#> -->
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
	<logic:equal name="procesoSearchPageVO" property="viewResult" value="true">
		<div id="resultadoFiltro"  class="horizscroll">
			<logic:notEmpty  name="procesoSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">					
	               	<tbody>
	               		<tr style="font-weight:bold;color:white;background-color:#006699;padding: 10px 15px 5px 30px;text-align: center;font-size:125%;">
	               			<td colspan="10"><bean:message bundle="base" key="base.resultadoBusqueda"/></td>
	               		</tr>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<logic:notEqual name="procesoSearchPageVO" property="modoSeleccionar" value="true">
								<th width="1">&nbsp;</th> <!-- Modificar -->
								<th width="1">&nbsp;</th> <!-- Eliminar -->								
							</logic:notEqual>
							<th align="left"><bean:message bundle="pro" key="pro.proceso.codProceso.label"/></th>
							<th align="left"><bean:message bundle="pro" key="pro.proceso.desProceso.label"/></th>
							<th align="left"><bean:message bundle="pro" key="pro.proceso.cantPasos.label"/></th>
							<th align="left"><bean:message bundle="pro" key="pro.proceso.classForName.label"/></th>
							<th align="left"><bean:message bundle="pro" key="pro.proceso.usuario.label"/></th>
							<th align="left"><bean:message bundle="pro" key="pro.proceso.locked.label"/></th>
							<th align="left"><bean:message bundle="pro" key="pro.proceso.ejecNodo.label"/></th>
							<!-- <#ColumnTitles#> -->
						</tr>
							
						<logic:iterate id="ProcesoVO" name="procesoSearchPageVO" property="listResult">
							<tr>
								<!-- Seleccionar -->
								<logic:equal name="procesoSearchPageVO" property="modoSeleccionar" value="true">
									<td>	
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="ProcesoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</td>
								</logic:equal>									
								<logic:notEqual name="procesoSearchPageVO" property="modoSeleccionar" value="true">
									<!-- Ver -->
									<td>
										<logic:equal name="procesoSearchPageVO" property="verEnabled" value="enabled">									
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="ProcesoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="ProcesoVO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</td>	
									<!-- Modificar-->								
									<td>
										<logic:equal name="procesoSearchPageVO" property="modificarEnabled" value="enabled">
											<logic:equal name="ProcesoVO" property="modificarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="ProcesoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>
											</logic:equal>
											<logic:notEqual name="ProcesoVO" property="modificarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="procesoSearchPageVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</td>
	
									<!-- Eliminar-->								
									<td>
										<logic:equal name="procesoSearchPageVO" property="eliminarEnabled" value="enabled">
											<logic:equal name="ProcesoVO" property="eliminarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="ProcesoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
												</a>
											</logic:equal>	
											<logic:notEqual name="ProcesoVO" property="eliminarEnabled" value="enabled">
												<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="procesoSearchPageVO" property="eliminarEnabled" value="enabled">										
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</td>																		
								</logic:notEqual>								
								<td><bean:write name="ProcesoVO" property="codProceso"/>&nbsp;</td>
								<td><bean:write name="ProcesoVO" property="desProceso"/>&nbsp;</td>
								<td><bean:write name="ProcesoVO" property="cantPasosView"/>&nbsp;</td>
								<td><bean:write name="ProcesoVO" property="classForNameView"/>&nbsp;</td>
								<td><bean:write name="ProcesoVO" property="usuario"/>&nbsp;</td>
								<td><bean:write name="ProcesoVO" property="locked.value"/>&nbsp;</td>
								<td><bean:write name="ProcesoVO" property="ejecNodo"/>&nbsp;</td>
								<!-- <#ColumnFiedls#> -->
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="10">
								<bean:define id="pager" name="procesoSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="procesoSearchPageVO" property="listResult">
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
	</logic:equal>			
	<!-- Fin Resultado Filtro -->

	<table class="tablabotones">
		<tr>				
			<td align="left">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<logic:equal name="procesoSearchPageVO" property="viewResult" value="true">
  	    		<td align="right">
  	    			<logic:equal name="procesoSearchPageVO" property="modoSeleccionar" value="false">
						<bean:define id="agregarEnabled" name="procesoSearchPageVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar', '0');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
					</logic:equal>
  	    			<logic:equal name="procesoSearchPageVO" property="modoSeleccionar" value="true">
  	    				<logic:equal name="procesoSearchPageVO" property="agregarEnSeleccion" value="true">
							<bean:define id="agregarEnabled" name="procesoSearchPageVO" property="agregarEnabled"/>
							<input type="button" <%=agregarEnabled%> class="boton" 
								onClick="submitForm('agregar', '0');" 
								value="<bean:message bundle="base" key="abm.button.agregar"/>"/>
							</logic:equal>
					</logic:equal>
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
	<input type="text" style="display:none"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
