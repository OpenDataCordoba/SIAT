<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/swe/submitForm.js"%>	    
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/seg/BuscarAccModApl.do">

		<!-- Mensajes/Advertencias -->
		<%@ include file="/swe/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>

		<h1><bean:message bundle="seg" key="accModApl.title"/></h1>	

		<p><bean:message bundle="seg" key="accModAplSearchPage.label.legend"/></p>

		<!-- Filtro -->
		<fieldset>
			<legend><bean:message bundle="seg" key="accModAplSearchPage.filter.label.title"/></legend>
			<table class="tabladatos">				
				<tr>
					<td><label><bean:message bundle="seg" key="accModAplSearchPage.filter.label.aplicacion"/>: </label></td>
					<td class="normal"><bean:write name="accModAplSearchPageVO" property="aplicacion.descripcion"/></td>
					<td><label><bean:message bundle="seg" key="accModAplSearchPage.filter.label.modulo"/>: </label></td>					
					<td class="normal">
						<logic:equal name="accModAplSearchPageVO" property="habilitarFiltroModulo" value="true">
							<html:select name="accModAplSearchPageVO" property="modApl.id" styleClass="select" onchange="submitForm('paramMod', '');">								
								<html:optionsCollection name="accModAplSearchPageVO" property="listModApl" label="nombreModulo" value="id" />
							</html:select>
						</logic:equal>
						<logic:notEqual name="accModAplSearchPageVO" property="habilitarFiltroModulo" value="true">
							<bean:write name="accModAplSearchPageVO" property="modApl.nombreModulo"/>
						</logic:notEqual>					
					</td>
				</tr>
				
				<tr>
					<td><label><bean:message bundle="seg" key="accModAplSearchPage.filter.label.nombreAccion"/>: </label></td>
					<td class="normal">
						<logic:equal name="accModAplSearchPageVO" property="habilitarFiltroModulo" value="true">
							<!-- muestra la lista de acciones -->
							<html:select name="accModAplSearchPageVO" property="nombreAccion" styleClass="select" >
								<option value="">Todos</option>
								<html:options  name="accModAplSearchPageVO" property="listAcciones" />
							</html:select>
						</logic:equal>
						<logic:notEqual name="accModAplSearchPageVO" property="habilitarFiltroModulo" value="true">
							<!-- muestra el campo de texto -->
							<html:text name="accModAplSearchPageVO" property="nombreAccion" size="20" maxlength="100" styleClass="datos" onchange="limpiaResultadoFiltro();"/>
						</logic:notEqual>	
					</td> 
					<td><label><bean:message bundle="seg" key="accModAplSearchPage.filter.label.nombreMetodo"/>: </label></td>
					<td class="normal"><html:text name="accModAplSearchPageVO" property="nombreMetodo" size="20" maxlength="100" styleClass="datos" onchange="limpiaResultadoFiltro();"/></td> 
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
		<logic:equal name="accModAplSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="accModAplSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
					<caption><bean:message bundle="seg" key="accModAplSearchPage.result.title"/></caption>
                	<tbody>
                	<tr>
                	
                		<logic:notEqual name="accModAplSearchPageVO" property="multiSelectEnabled" value="true">
							<th width="1">&nbsp;</th> <!-- Ver-->
						</logic:notEqual>
                		
                		<logic:equal name="accModAplSearchPageVO" property="multiSelectEnabled" value="true">
                			<th width="1">
                				<input type="checkbox" onclick="changeChk('filter', 'listId', this)"/>
                			</th> <!-- Seleccionar-->
                		</logic:equal>
						
						<logic:notEqual name="accModAplSearchPageVO" property="multiSelectEnabled" value="true">
							<logic:equal name="accModAplSearchPageVO" property="ABMEnabled" value="enabled">
								<th width="1">&nbsp;</th> <!-- Modificar-->
								<th width="1">&nbsp;</th> <!-- Eliminar-->
							</logic:equal>
						</logic:notEqual>
						<logic:equal name="accModAplSearchPageVO" property="habilitarFiltroModulo" value="true">
							<th align="left"><bean:message bundle="seg" key="accModAplSearchPage.filter.label.modulo"/></th>
						</logic:equal>
						<th align="left"><bean:message bundle="seg" key="accModAplSearchPage.filter.label.descripcion"/></th>						
						<th align="left"><bean:message bundle="seg" key="accModAplSearchPage.filter.label.nombreAccion"/></th>
						<th align="left"><bean:message bundle="seg" key="accModAplSearchPage.filter.label.nombreMetodo"/></th>
					</tr>
						
					<logic:iterate id="AccModAplVO" name="accModAplSearchPageVO" property="listResult">
						<tr>
							<!-- Check para seleccion multiple -->
							<logic:equal name="accModAplSearchPageVO" property="multiSelectEnabled" value="true">
								<td align="center">
									<html:multibox name="accModAplSearchPageVO" property="listId">
										<bean:write name="AccModAplVO" property="id" bundle="base" formatKey="general.format.id"/>
									</html:multibox>
								</td>
							</logic:equal>
							<logic:notEqual name="accModAplSearchPageVO" property="multiSelectEnabled" value="true">
								<!-- Ver/Seleccionar -->
								<td>
									<logic:notEqual name="accModAplSearchPageVO" property="inactivo" value="true">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="AccModAplVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:notEqual>
									<logic:equal name="accModAplSearchPageVO" property="inactivo" value="true">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="AccModAplVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>									
								</td>
							
								<!-- Modificar-->
								<logic:equal name="accModAplSearchPageVO" property="ABMEnabled" value="enabled">									
									<td>
										<logic:equal name="accModAplSearchPageVO" property="modificarEnabled" value="enabled">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="AccModAplVO" property="id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
												</a>
										</logic:equal>
										<logic:notEqual name="accModAplSearchPageVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>									
									</td>
								</logic:equal>
	
								<!-- Eliminar-->
								<logic:equal name="accModAplSearchPageVO" property="ABMEnabled" value="enabled">		
									<td>
										<logic:equal name="accModAplSearchPageVO" property="eliminarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="AccModAplVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="accModAplSearchPageVO" property="eliminarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>							
									</td>
								</logic:equal>
							</logic:notEqual>
							<logic:equal name="accModAplSearchPageVO" property="habilitarFiltroModulo" value="true">
								<td><bean:write name="AccModAplVO" property="modApl.nombreModulo" /></td>
							</logic:equal>
							<td><bean:write name="AccModAplVO" property="descripcion" /></td>							
							<td><bean:write name="AccModAplVO" property="nombreAccion" /></td>
							<td><bean:write name="AccModAplVO" property="nombreMetodo" /></td>
						</tr>
					</logic:iterate>
				
					<logic:equal name="accModAplSearchPageVO" property="paged" value="true">	
						<tr>
							<td class="paginador" align="center" colspan="8">
								<bean:define id="pager" name="accModAplSearchPageVO"/>
								<%@ include file="/swe/pager.jsp" %>
							</td>
						</tr>
					</logic:equal>
										
					</tbody>
					</table>
			</logic:notEmpty>
		
			<logic:empty name="accModAplSearchPageVO" property="listResult">
				<table>		
					<logic:equal name="userSession" property="navModel.act" value="buscar">
						<tr>						
							<td align="center">
								<br>							
									<bean:message bundle="seg" key="accModAplSearchPage.result.label.resultadoVacio"/>
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
   	    			<td align="right">
						<logic:equal name="accModAplSearchPageVO" property="ABMEnabled" value="enabled">		
							<bean:define id="agregarEnabled" name="accModAplSearchPageVO" property="agregarEnabled"/>
							<logic:notEqual name="accModAplSearchPageVO" property="multiSelectEnabled" value="true">
								<input type="button" <%=agregarEnabled%> class="boton" 
									onClick="submitForm('agregar', '');" 
									value="<bean:message bundle="base" key="abm.button.agregar"/>"
								/>
							</logic:notEqual>
						</logic:equal>
						
						<logic:equal name="accModAplSearchPageVO" property="multiSelectEnabled" value="true">
							<logic:notEmpty name="accModAplSearchPageVO" property="listResult">
								<input type="button" class="boton" 
									onClick="submitForm('agregarMultiple', '0');" 
									value="<bean:message bundle="base" key="abm.button.seleccionar"/>"
								/>
							</logic:notEmpty>	
						</logic:equal>
						
					</td>
				</logic:equal>
			</tr>
		</table>	            				

		<html:hidden name="accModAplSearchPageVO" property="modApl.aplicacion.descripcion"/>
		<html:hidden name="accModAplSearchPageVO" property="modApl.aplicacion.id"/>
		<html:hidden name="accModAplSearchPageVO" property="modApl.nombreModulo"/>
		<html:hidden name="accModAplSearchPageVO" property="modApl.id"/>
		<input type="hidden" name="method" value=""/>
		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="pageNumber" value="1" id="pageNumber">
		<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
