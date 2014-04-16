<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/swe/submitForm.js"%>	    
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/seg/BuscarAplicacion.do">

		<!-- Mensajes/Advertencias -->
		<%@ include file="/swe/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="seg" key="aplicacionSearchPage.title"/></h1>	
		
		<p>Permite Buscar, Ver, Modificar, Eliminar, Agregar Aplicaciones y Administrar sus Modulos, Acciones, Roles y Usuarios.</p>
		
		<!-- Filtro -->
		<fieldset>
			<legend>B&uacute;squeda de Aplicaciones</legend>
			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="seg" key="aplicacionSearchPage.filter.label.codigo"/>: </label></td>
					<td class="normal"><html:text name="aplicacionSearchPageVO" property="codigo" size="15" maxlength="20" styleClass="datos" onchange="limpiaResultadoFiltro();"/></td>
			
					<td><label><bean:message bundle="seg" key="aplicacionSearchPage.filter.label.descripcion"/>: </label></td>
					<td class="normal"><html:text name="aplicacionSearchPageVO" property="descripcion" size="20" maxlength="100" styleClass="datos" onchange="limpiaResultadoFiltro();"/></td>
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
		<logic:equal name="aplicacionSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="aplicacionSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
					<caption><bean:message bundle="seg" key="aplicacionSearchPage.result.title"/></caption>
                	<tbody>
                	<tr>
						<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th width="1">&nbsp;</th> <!-- Administar Modulos -->
						<th width="1">&nbsp;</th> <!-- Administar Roles -->
						<th width="1">&nbsp;</th> <!-- Administar Usuarios -->
						<th width="1">&nbsp;</th> <!-- Administar ItemMenu -->
						<th align="left"><bean:message bundle="seg" key="aplicacionSearchPage.result.label.codigo"/></th>
						<th align="left"><bean:message bundle="seg" key="aplicacionSearchPage.result.label.descripcion"/></th>
					</tr>
						
					<logic:iterate id="AplicacionVO" name="aplicacionSearchPageVO" property="listResult">
						<tr>
							<!-- Ver/Seleccionar -->
							<td>
								<logic:notEqual name="aplicacionSearchPageVO" property="inactivo" value="true">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="AplicacionVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</logic:notEqual>
								<logic:equal name="aplicacionSearchPageVO" property="inactivo" value="true">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="AplicacionVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</logic:equal>									
							</td>
							
							<td>
								<!-- Modificar-->
								<logic:equal name="aplicacionSearchPageVO" property="ABMEnabled" value="enabled">									
									<logic:equal name="aplicacionSearchPageVO" property="modificarEnabled" value="enabled">
										<logic:equal name="AplicacionVO" property="modificarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="AplicacionVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
											</a>
										</logic:equal>
									    <logic:notEqual name="AplicacionVO" property="modificarEnabled" value="enabled">
										   <img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									    </logic:notEqual>
									</logic:equal>
									<logic:notEqual name="aplicacionSearchPageVO" property="modificarEnabled" value="enabled">
									   <img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="aplicacionSearchPageVO" property="ABMEnabled" value="enabled">
									&nbsp;
								</logic:notEqual>
							</td>

							<td>
								<!-- Eliminar-->								
								<logic:equal name="aplicacionSearchPageVO" property="ABMEnabled" value="enabled">		
									<logic:equal name="aplicacionSearchPageVO" property="eliminarEnabled" value="enabled">
										<logic:equal name="AplicacionVO" property="eliminarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="AplicacionVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
											</a>
										</logic:equal>
    									<logic:notEqual name="AplicacionVO" property="eliminarEnabled" value="enabled">
	    									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
		    							</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="aplicacionSearchPageVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>				
								<logic:notEqual name="aplicacionSearchPageVO" property="ABMEnabled" value="enabled">		
									&nbsp;
								</logic:notEqual>
							</td>
							
							<td>		
								<!-- Administrar Modulos-->
								<logic:equal name="aplicacionSearchPageVO" property="ABMEnabled" value="enabled">		
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('buscarModApl', '<bean:write name="AplicacionVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="seg" key="aplicacionSearchPage.button.adminModulos"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/brick0.gif"/>
									</a>
								</logic:equal>							
								<logic:notEqual name="aplicacionSearchPageVO" property="ABMEnabled" value="enabled">		
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/brick1.gif"/>
								</logic:notEqual>
							</td>
							
							<td>		
								<!-- Administrar Roles-->
								<logic:equal name="aplicacionSearchPageVO" property="ABMEnabled" value="enabled">		
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('buscarRolApl', '<bean:write name="AplicacionVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="seg" key="aplicacionSearchPage.button.adminRoles"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/group0.gif"/>
									</a>
								</logic:equal>							
								<logic:notEqual name="aplicacionSearchPageVO" property="ABMEnabled" value="enabled">		
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/group1.gif"/>
								</logic:notEqual>
							</td>
							
							<td>		
								<!-- Administrar Usuarios-->
								<logic:equal name="aplicacionSearchPageVO" property="ABMEnabled" value="enabled">		
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('buscarUsrApl', '<bean:write name="AplicacionVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="seg" key="aplicacionSearchPage.button.adminUsuarios"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/user0.gif"/>
									</a>
								</logic:equal>							
								<logic:notEqual name="aplicacionSearchPageVO" property="ABMEnabled" value="enabled">		
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/user1.gif"/>
								</logic:notEqual>
							</td>
							<td>		
								<!-- Administrar ItemMenu -->
								<logic:equal name="aplicacionSearchPageVO" property="ABMEnabled" value="enabled">		
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('buscarItemMenuRoot', '<bean:write name="AplicacionVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="seg" key="aplicacionSearchPage.button.adminMenus"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/application_side_tree0.gif"/>
									</a>
								</logic:equal>							
								<logic:notEqual name="aplicacionSearchPageVO" property="ABMEnabled" value="enabled">		
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/application_side_tree1.gif"/>
								</logic:notEqual>
							</td>
							
							<td><bean:write name="AplicacionVO" property="codigo"/></td>
							<td><bean:write name="AplicacionVO" property="descripcion" /></td>
						</tr>
					</logic:iterate>
			
					<tr>
						<td class="paginador" align="center" colspan="9">
							<bean:define id="pager" name="aplicacionSearchPageVO"/>
							<%@ include file="/swe/pager.jsp" %>
						</td>
					</tr>
					
					</tbody>
				</table>
			</logic:notEmpty>
		
			<logic:empty name="aplicacionSearchPageVO" property="listResult">
				<table>		
					<logic:equal name="userSession" property="navModel.act" value="buscar">
						<tr>						
							<td align="center">
								<br>							
									<bean:message bundle="seg" key="aplicacionSearchPage.result.label.resultadoVacio"/>
								<br>
							</td>
						</tr>				
					</logic:equal>
				</table>
			</logic:empty>
		</logic:equal>	
		</div>
		<!-- Fin Resultado Filtro -->

		<logic:equal name="userSession" property="navModel.act" value="buscar">
			<table class="tablabotones">
	    			<tr>
   	    			<td align="left">
						<bean:define id="agregarEnabled" name="aplicacionSearchPageVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar', '0');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
					</td>
		        </tr>
			</table>	            				
		</logic:equal>
		
		<input type="hidden" name="method" value=""/>
		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="pageNumber" value="1" id="pageNumber">
		<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
