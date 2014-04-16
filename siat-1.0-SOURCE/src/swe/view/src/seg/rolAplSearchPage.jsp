<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/swe/submitForm.js"%>	    
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/seg/BuscarRolApl.do">

		<!-- Mensajes/Advertencias -->
		<%@ include file="/swe/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="seg" key="seg.rolAplSearchPage.title"/></h1>	
		
		<p><bean:message bundle="seg" key="seg.rolAplSearchPage.legend"/></p>
		
		<!-- Filtro -->
		<fieldset>
			<legend><bean:message bundle="seg" key="seg.rolAplSearchPage.subtitle"/></legend>

			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="seg" key="seg.rolAplSearchPage.aplicacion.codigo.label"/>:</label></td>
					<td class="normal"><bean:write name="rolAplSearchPageVO" property="aplicacion.codigo"/></td>
					<td><label><bean:message bundle="seg" key="seg.rolAplSearchPage.aplicacion.descripcion.label"/>:</label></td>
					<td class="normal"><bean:write name="rolAplSearchPageVO" property="aplicacion.descripcion"/></td>
				</tr>
				
				<logic:notEqual name="rolAplSearchPageVO" property="ABMEnabled" value="enabled">
					<tr>
						<td><label><bean:message bundle="seg" key="rolAplSearchPage.filter.label.usrApl.username"/>:</label></td>
						<td class="normal"><bean:write name="rolAplSearchPageVO" property="usrApl.username" /> </td>
					</tr>
				</logic:notEqual>
				
				<tr>
					<td><label><bean:message bundle="seg" key="seg.rolAplSearchPage.codigo.label"/>: </label></td>
					<td class="normal"><html:text name="rolAplSearchPageVO" property="codigo" size="15" maxlength="20" styleClass="datos" onchange="limpiaResultadoFiltro();"/></td>
					<td><label><bean:message bundle="seg" key="seg.rolAplSearchPage.descripcion.label"/>: </label></td>
					<td class="normal"><html:text name="rolAplSearchPageVO" property="descripcion" size="20" maxlength="100" styleClass="datos" onchange="limpiaResultadoFiltro();"/> </td>
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
		<logic:equal name="rolAplSearchPageVO" property="viewResult" value="true">
		        <logic:notEmpty  name="rolAplSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
					<caption><bean:message bundle="seg" key="seg.rolAplSearchPage.result"/></caption>
                	<tbody>
                	<tr>
						<th width="1">&nbsp;</th> <!-- Ver/Seleccionar-->
						<logic:equal name="rolAplSearchPageVO" property="ABMEnabled" value="enabled">
							<th width="1">&nbsp;</th> <!-- Modificar-->
							<th width="1">&nbsp;</th> <!-- Eliminar-->
							<th width="1">&nbsp;</th> <!-- Rol Accion Modulo -->
						</logic:equal>
						<th align="left"><bean:message bundle="seg" key="seg.rolApl.codigo.label"/></th>
						<th align="left"><bean:message bundle="seg" key="seg.rolApl.descripcion.label"/></th>
					</tr>
						
					<logic:iterate id="RodAplVO" name="rolAplSearchPageVO" property="listResult">
						<tr>
							<!-- Ver/Seleccionar -->
							<td>
								<logic:notEqual name="rolAplSearchPageVO" property="inactivo" value="true">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="RodAplVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</logic:notEqual>
								<logic:equal name="rolAplSearchPageVO" property="inactivo" value="true">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="RodAplVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</logic:equal>									
							</td>

							<!-- Modificar-->
							<logic:equal name="rolAplSearchPageVO" property="ABMEnabled" value="enabled">									
								<td>
									<logic:equal name="rolAplSearchPageVO" property="modificarEnabled" value="enabled">
								    	<logic:equal name="RodAplVO" property="modificarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="RodAplVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
											</a>
										</logic:equal>	
									</logic:equal>
									<logic:notEqual name="RodAplVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>									
								</td>
							</logic:equal>							

							<!-- Eliminar-->
							<logic:equal name="rolAplSearchPageVO" property="ABMEnabled" value="enabled">		
								<td>
									<logic:equal name="rolAplSearchPageVO" property="eliminarEnabled" value="enabled">
										<logic:equal name="RodAplVO" property="eliminarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="RodAplVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
											</a>
										</logic:equal>		
									</logic:equal>
									<logic:notEqual name="RodAplVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>							
								</td>
							</logic:equal>							
																
							<!-- Administrar Rol Accion Modulo-->
							<logic:equal name="rolAplSearchPageVO" property="ABMEnabled" value="enabled">		
								<td>
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('buscarRolAccModApl', '<bean:write name="RodAplVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="seg" key="seg.rolAplSearchPage.button.adminAccMod"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/brick_go0.gif"/>
									</a>
								</td>
							</logic:equal>
							<td><bean:write name="RodAplVO" property="codigo" /></td>
							<td><bean:write name="RodAplVO" property="descripcion" /></td>
						</tr>
					</logic:iterate>
			
					<tr>
						<td class="paginador" align="center" colspan="8">
							<bean:define id="pager" name="rolAplSearchPageVO"/>
							<%@ include file="/swe/pager.jsp" %>
						</td>
					</tr>
					
					</tbody>
				</table>
			</logic:notEmpty>
		
			<logic:empty name="rolAplSearchPageVO" property="listResult">
				<table>		
					<logic:equal name="userSession" property="navModel.act" value="buscar">
						<tr>						
							<td align="center">
								<br>							
									<bean:message bundle="seg" key="seg.rolAplSearchPage.resultadoVacio"/>
								<br>
							</td>
						</tr>				
					</logic:equal>
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
				
				<logic:equal name="userSession" property="navModel.act" value="buscar">
					<logic:equal name="rolAplSearchPageVO" property="ABMEnabled" value="enabled">				
	   	    			<td align="right">
							<bean:define id="agregarEnabled" name="rolAplSearchPageVO" property="agregarEnabled"/>
							<input type="button" <%=agregarEnabled%> class="boton" 
								onClick="submitForm('agregar', '');" 
								value="<bean:message bundle="base" key="abm.button.agregar"/>"
							/>
						</td>
					</logic:equal>
				</logic:equal>
			</tr>
		</table>	            				

		<input type="hidden" name="method" value=""/>
		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="pageNumber" value="1" id="pageNumber">
		<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
