<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/swe/submitForm.js"%>	    
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/seg/BuscarRolAccModApl.do">

		<!-- Mensajes/Advertencias -->
		<%@ include file="/swe/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>

		<h1><bean:message bundle="seg" key="rolAccModAplSearchPage.title"/></h1>

		<p><bean:message bundle="seg" key="rolAccModAplSearchPage.legend"/></p>

		<!-- Filtro -->
		<fieldset>
			<legend><bean:message bundle="seg" key="rolAccModAplSearchPage.subtitle"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="seg" key="rolAccModAplSearchPage.rol.label"/>:</label></td>
					<td class="normal"><bean:write  name="rolAccModAplSearchPageVO" property="rolApl.descripcion"/></td>
					<td><label><bean:message bundle="seg" key="rolAccModAplSearchPage.modulo.label"/>: </label></td>
					<td class="normal"><html:select name="rolAccModAplSearchPageVO" property="modApl.id" styleClass="select" onchange="submitForm('buscar', '');">
							<html:optionsCollection name="rolAccModAplSearchPageVO" property="listModApl" label="nombreModulo" value="id" />
						</html:select>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="seg" key="rolAccModAplSearchPage.nombreAccion"/>: </label></td>
					<td class="normal"><html:text name="rolAccModAplSearchPageVO" property="nombreAccion" size="20" maxlength="100" onchange="limpiaResultadoFiltro();"/> </td>
					
					<td><label><bean:message bundle="seg" key="rolAccModAplSearchPage.nombreMetodo"/>: </label></td>
					<td class="normal"><html:text name="rolAccModAplSearchPageVO" property="nombreMetodo" size="20" maxlength="100" onchange="limpiaResultadoFiltro();"/></td> 
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
		<logic:equal name="rolAccModAplSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="rolAccModAplSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
					<caption><bean:message bundle="seg" key="rolAccModAplSearchPage.result.title"/></caption>
                	<tbody>
                	<tr>
						<th width="1">&nbsp;</th> <!-- Ver/Seleccionar-->
						<th width="1">&nbsp;</th> <!-- Eliminar-->
						<th align="left"><bean:message bundle="seg" key="rolAccModAplSearchPage.nombreAccion"/></th>
						<th align="left"><bean:message bundle="seg" key="rolAccModAplSearchPage.nombreMetodo"/></th>
					</tr>
						
					<logic:iterate id="RolAccModAplVO" name="rolAccModAplSearchPageVO" property="listResult">
						<tr>
							<!-- Ver/Seleccionar -->
							<td>
								<logic:notEqual name="rolAccModAplSearchPageVO" property="inactivo" value="true">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="RolAccModAplVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</logic:notEqual>
								<logic:equal name="rolAccModAplSearchPageVO" property="inactivo" value="true">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="RolAccModAplVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</logic:equal>									
							</td>
							
							<td>
								<!-- Eliminar-->
								<logic:equal name="rolAccModAplSearchPageVO" property="ABMEnabled" value="enabled">		
									<logic:equal name="rolAccModAplSearchPageVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="RolAccModAplVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="rolAccModAplSearchPageVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>							
								</logic:equal>							
								<logic:notEqual name="rolAccModAplSearchPageVO" property="ABMEnabled" value="enabled">		
									&nbsp;
								</logic:notEqual>
							</td>
							
							<td><bean:write name="RolAccModAplVO" property="accModApl.nombreAccion" /></td>
							<td><bean:write name="RolAccModAplVO" property="accModApl.nombreMetodo" /></td>							
						</tr>
					</logic:iterate>
			
					<tr>
						<td class="paginador" align="center" colspan="8">
							<bean:define id="pager" name="rolAccModAplSearchPageVO"/>
							<%@ include file="/swe/pager.jsp" %>
						</td>
					</tr>
					
					</tbody>
				</table>
			</logic:notEmpty>
		
			<logic:empty name="rolAccModAplSearchPageVO" property="listResult">
				<table>		
					<logic:equal name="userSession" property="navModel.act" value="buscar">
						<tr>						
							<td align="center">
								<br>							
									<bean:message bundle="seg" key="rolAccModAplSearchPage.result.resultadoVacio.label"/>
								<br>
							</td>
						</tr>				
					</logic:equal>
				</table>
			</logic:empty>
		</logic:equal>
		</div>
		
		<table class="tablabotones">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<logic:equal name="userSession" property="navModel.act" value="buscar">
					<logic:equal name="rolAccModAplSearchPageVO" property="ABMEnabled" value="enabled">		
	   	    			<td align="right">
							<bean:define id="agregarEnabled" name="rolAccModAplSearchPageVO" property="agregarEnabled"/>
							<input type="button" class="boton" 
								onClick="submitForm('agregar', '');" 
								value="<bean:message bundle="base" key="abm.button.agregar"/>"
							/>
						</td>
					</logic:equal>
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
