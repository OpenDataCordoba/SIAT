<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/swe/submitForm.js"%>	    
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/seg/BuscarItemMenu.do">

		<!-- Mensajes/Advertencias -->
		<%@ include file="/swe/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="seg" key="itemMenu.title"/></h1>
		
		<p>Permite Buscar, Ver, Modificar, Eliminar y Agregar Items de Menues de la Aplicaci&oacute;n.</p>
		
		<!-- Filtro -->
		<fieldset>
			<logic:equal name="itemMenuSearchPageVO" property="esRoot" value="true">
				<legend><label><bean:message bundle="seg" key="itemMenuSearchPage.title.root"/> </label></legend>
			</logic:equal>
			<logic:notEqual name="itemMenuSearchPageVO" property="esRoot" value="true">
				<legend><label><bean:message bundle="seg" key="itemMenuSearchPage.title.hijos"/> </label></legend>
			</logic:notEqual>
				<table class="tabladatos">
						<tr>
							<td><label><bean:message bundle="seg" key="itemMenuSearchPage.filter.label.aplicacion.codigo"/>:</label></td>
							<td class="normal"><bean:write name="itemMenuSearchPageVO" property="aplicacion.codigo" /> </td>
							<td><label><bean:message bundle="seg" key="itemMenuSearchPage.filter.label.aplicacion.descripcion"/>:</label></td>
						    <td class="normal"><bean:write name="itemMenuSearchPageVO" property="aplicacion.descripcion" /></td>
						</tr>
						<tr>
							<td><label><bean:message bundle="seg" key="itemMenuSearchPage.result.label.nivelMenu"/>:</label></td>
							<td class="normal"><bean:write name="itemMenuSearchPageVO" property="nivelMenu" format="#" /></td>
		                </tr>
					<logic:notEqual name="itemMenuSearchPageVO" property="esRoot" value="true">
						<tr>
							<td><label><bean:message bundle="seg" key="itemMenuSearchPage.filter.label.itemMenu.titulo"/>:</label></td>
							<td class="normal"><bean:write name="itemMenuSearchPageVO" property="itemMenu.titulo" /></td>
							<td><label><bean:message bundle="seg" key="itemMenuSearchPage.filter.label.itemMenu.descripcion"/>:</label></td>
							<td class="normal"><bean:write name="itemMenuSearchPageVO" property="itemMenu.descripcion" /></td>
						</tr>
					</logic:notEqual>
				</table>
			
		</fieldset>	
		<!-- Fin Filtro -->
		
		<!-- Resultado Filtro -->
		<div id="resultadoFiltro">
			<logic:notEmpty  name="itemMenuSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%"> 
					
					<logic:equal name="itemMenuSearchPageVO" property="esRoot" value="true">
						<caption><bean:message bundle="seg" key="itemMenuSearchPage.result.title.root"/></caption>
					</logic:equal>
					<logic:notEqual name="itemMenuSearchPageVO" property="esRoot" value="true">
						<caption><bean:message bundle="seg" key="itemMenuSearchPage.result.title.hijos"/></caption>
					</logic:notEqual>
                	<tbody>
                	<tr>
						<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th width="1">&nbsp;</th> <!-- ItemMenuHijos -->
						<th width="1">&nbsp;</th> <!-- AccModApl -->
						<th align="left"><bean:message bundle="seg" key="itemMenuSearchPage.result.label.nroOrden"/></th>
						<th align="left"><bean:message bundle="seg" key="itemMenuSearchPage.result.label.titulo"/></th>
						<th align="left"><bean:message bundle="seg" key="itemMenuSearchPage.result.label.descripcion"/></th>
						<th align="left"><bean:message bundle="seg" key="itemMenuSearchPage.result.label.modulo"/></th>
						<th align="left"><bean:message bundle="seg" key="itemMenuSearchPage.result.label.descAccion"/></th>
					</tr>
						
					<logic:iterate id="ItemMenuVO" name="itemMenuSearchPageVO" property="listResult">
						<tr>
							<!-- Ver/Seleccionar -->
							<td>
								<logic:notEqual name="itemMenuSearchPageVO" property="inactivo" value="true">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="ItemMenuVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</logic:notEqual>
								<logic:equal name="itemMenuSearchPageVO" property="inactivo" value="true">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="ItemMenuVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</logic:equal>									
							</td>
							
							<td>
								<!-- Modificar-->
								<logic:equal name="itemMenuSearchPageVO" property="ABMEnabled" value="enabled">									
									<logic:equal name="itemMenuSearchPageVO" property="modificarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="ItemMenuVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
											</a>
									</logic:equal>
									<logic:notEqual name="itemMenuSearchPageVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>									
								</logic:equal>
								<logic:notEqual name="itemMenuSearchPageVO" property="ABMEnabled" value="enabled">
									&nbsp;
								</logic:notEqual>
							</td>

							<td>
								<!-- Eliminar-->
								<logic:equal name="itemMenuSearchPageVO" property="ABMEnabled" value="enabled">		
									<logic:equal name="itemMenuSearchPageVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="ItemMenuVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="itemMenuSearchPageVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>							
								</logic:equal>							
								<logic:notEqual name="itemMenuSearchPageVO" property="ABMEnabled" value="enabled">		
									&nbsp;
								</logic:notEqual>
							</td>

							<td>
								<!-- Administrar ItemMenuHijos -->
								<logic:equal name="itemMenuSearchPageVO" property="ABMEnabled" value="enabled">
									<logic:equal name="ItemMenuVO" property="administrarHijosEnabled" value="enabled">										
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('inicializarHijos', '<bean:write name="ItemMenuVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="seg" key="itemMenuSearchPage.button.buscarItemMenuHijos"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/application_side_tree0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="ItemMenuVO" property="administrarHijosEnabled" value="enabled">										
										<img title="<bean:message bundle="seg" key="itemMenuSearchPage.button.buscarItemMenuHijos"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/application_side_tree1.gif"/>
									</logic:notEqual>
								</logic:equal>							
								<logic:notEqual name="itemMenuSearchPageVO" property="ABMEnabled" value="enabled">		
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/application_side_tree1.gif"/>
								</logic:notEqual>
							</td>							

							<td>
								<!-- Administrar AccionModulo -->
								<logic:equal name="itemMenuSearchPageVO" property="ABMEnabled" value="enabled">		
									<logic:equal name="ItemMenuVO" property="administrarAccionEnabled" value="enabled">								
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('buscarAccionModulo', '<bean:write name="ItemMenuVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="seg" key="itemMenuSearchPage.button.buscarAccionModulo"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/brick_go0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="ItemMenuVO" property="administrarAccionEnabled" value="enabled">								
										<img title="<bean:message bundle="seg" key="itemMenuSearchPage.button.buscarAccionModulo"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/brick_go1.gif"/>
									</logic:notEqual>
								</logic:equal>							
								<logic:notEqual name="itemMenuSearchPageVO" property="ABMEnabled" value="enabled">		
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/brick_go1.gif"/>
								</logic:notEqual>
							</td>							
							
							<td><bean:write name="ItemMenuVO" property="nroOrdenView"/></td>
							<td><bean:write name="ItemMenuVO" property="titulo"/></td>
							<td><bean:write name="ItemMenuVO" property="descripcion" /></td>
							<td><bean:write name="ItemMenuVO" property="modAplView" /></td>
							<td><bean:write name="ItemMenuVO" property="descripcionView" /></td>							
						</tr>
					</logic:iterate>
			
					</tbody>
				</table>
			</logic:notEmpty>
		
			<logic:empty name="itemMenuSearchPageVO" property="listResult">
				<table>		
					<logic:equal name="userSession" property="navModel.act" value="buscar">
						<tr>						
							<td align="center">
								<logic:equal name="itemMenuSearchPageVO" property="esRoot" value="true">
									<br>							
										<bean:message bundle="seg" key="itemMenuSearchPage.result.label.resultadoVacio.root"/>
									<br>
								</logic:equal>
								<logic:notEqual name="itemMenuSearchPageVO" property="esRoot" value="true">
									<br>							
										<bean:message bundle="seg" key="itemMenuSearchPage.result.label.resultadoVacio.hijos"/>
									<br>
								</logic:notEqual>
								
							</td>
						</tr>				
					</logic:equal>
				</table>
			</logic:empty>
		</div>
		
		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   			<logic:equal name="userSession" property="navModel.act" value="buscar">
	   				<td align="left">
						<bean:define id="agregarEnabled" name="itemMenuSearchPageVO" property="agregarEnabled"/>
							<input type="button" <%=agregarEnabled%> class="boton" 
								onClick="submitForm('agregar', '');" 
								value="<bean:message bundle="base" key="abm.button.agregar"/>"
							/>
					</td>
				</logic:equal>
	   	    </tr>
	   	</table>
	
		<!-- Fin Resultado Filtro -->	
		<input type="hidden" name="method" value=""/>
		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="pageNumber" value="1" id="pageNumber">
		<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
