<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/swe/submitForm.js"%>	    
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/seg/BuscarUsrApl.do">

		<!-- Mensajes/Advertencias -->
		<%@ include file="/swe/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="seg" key="usrAplSearchPage.title"/></h1>
		
		<p><bean:message bundle="seg" key="usrAplSearchPage.legend"/></p>
		
		<!-- Filtro -->
		<fieldset>
			<legend><bean:message bundle="seg" key="usrAplSearchPage.subtitle"/></legend>
			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="seg" key="usrAplSearchPage.filter.label.aplicacion.codigo"/>:</label></td>
					<td  class="normal"><bean:write name="usrAplSearchPageVO" property="aplicacion.codigo"/></td>
					<td><label><bean:message bundle="seg" key="usrAplSearchPage.filter.label.aplicacion.descripcion"/>:</label></td>
					<td class="normal"><bean:write name="usrAplSearchPageVO" property="aplicacion.descripcion" /></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="seg" key="usrAplSearchPage.filter.label.username"/>:</label></td>
					<td class="normal"><html:text name="usrAplSearchPageVO" property="username" size="15" maxlength="10" styleClass="datos" onchange="limpiaResultadoFiltro();"/></td>
				    <input type="text" style="display:none"/>
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
		<logic:equal name="usrAplSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="usrAplSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
					<caption><bean:message bundle="seg" key="usrAplSearchPage.result.title"/></caption>
                	<tbody>
                	<tr>
						<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th width="1">&nbsp;</th> <!-- Roles -->
						<th width="1">&nbsp;</th> <!-- Clonar -->
						<th align="left"><bean:message bundle="seg" key="usrAplSearchPage.result.label.username"/></th>
						<th align="left"><bean:message bundle="seg" key="usrAplSearchPage.result.label.fechaAlta"/></th>
						<th align="left"><bean:message bundle="seg" key="usrAplSearchPage.result.label.estado"/></th>
						<th align="left"><bean:message bundle="seg" key="usrAplSearchPage.result.label.fechaBaja"/></th>
					</tr>
						
					<logic:iterate id="UsrAplVO" name="usrAplSearchPageVO" property="listResult">
						<tr>
							<!-- Ver/Seleccionar -->
							<td>
								<logic:notEqual name="usrAplSearchPageVO" property="inactivo" value="true">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="UsrAplVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</logic:notEqual>
								<logic:equal name="usrAplSearchPageVO" property="inactivo" value="true">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="UsrAplVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</logic:equal>									
							</td>
							
							<td>
								<!-- Modificar-->
								<logic:equal name="usrAplSearchPageVO" property="ABMEnabled" value="enabled">									
									<logic:equal name="usrAplSearchPageVO" property="modificarEnabled" value="enabled">
										<logic:equal name="UsrAplVO" property="modificarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="UsrAplVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
											</a>
										</logic:equal>
									</logic:equal>
									<logic:notEqual name="UsrAplVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>									
								</logic:equal>
								<logic:notEqual name="usrAplSearchPageVO" property="ABMEnabled" value="enabled">
									&nbsp;
								</logic:notEqual>
							</td>

							<td>
								<!-- Eliminar-->
								<logic:equal name="usrAplSearchPageVO" property="ABMEnabled" value="enabled">		
									<logic:equal name="usrAplSearchPageVO" property="eliminarEnabled" value="enabled">
										<logic:equal name="UsrAplVO" property="eliminarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="UsrAplVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
											</a>
										</logic:equal>		
									</logic:equal>
									<logic:notEqual name="UsrAplVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>							
								</logic:equal>							
								<logic:notEqual name="usrAplSearchPageVO" property="ABMEnabled" value="enabled">		
									&nbsp;
								</logic:notEqual>
							</td>

							<td>
								<!-- Administrar Roles -->
								<logic:equal name="usrAplSearchPageVO" property="ABMEnabled" value="enabled">		
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('buscarUsrRolApl', '<bean:write name="UsrAplVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="seg" key="usrAplSearchPage.button.buscarUsrRolApl"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/group_go0.gif"/>
									</a>
								</logic:equal>							
								<logic:notEqual name="usrAplSearchPageVO" property="ABMEnabled" value="enabled">		
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/group_go1.gif"/>
								</logic:notEqual>
							</td>							
<!-- COMIENZO DE MODIFICACIONES PARA CLONAR DE DIEGO -->							
								<td>
								<!-- Clonar Usuario -->
								<logic:equal name="usrAplSearchPageVO" property="ABMEnabled" value="enabled">									
									<logic:equal name="usrAplSearchPageVO" property="agregarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('clonar', '<bean:write name="UsrAplVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.clonar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/clonar0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="usrAplSearchPageVO" property="agregarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/clonar1.gif"/>
									</logic:notEqual>									
								</logic:equal>
								<logic:notEqual name="usrAplSearchPageVO" property="ABMEnabled" value="enabled">
									&nbsp;
								</logic:notEqual>
							</td>
<!-- FIN DE MODIFICACIONES PARA CLONAR DE DIEGO -->							
							<td><bean:write name="UsrAplVO" property="username"/></td>
							<td><bean:write name="UsrAplVO" property="fechaAltaView" /></td>
							<td><bean:write name="UsrAplVO" property="estado.value" /></td>
							<td><bean:write name="UsrAplVO" property="fechaBajaView" />&nbsp;</td>

						</tr>
					</logic:iterate>
			
					<tr>
						<td class="paginador" align="center" colspan="9">
							<bean:define id="pager" name="usrAplSearchPageVO"/>
							<%@ include file="/swe/pager.jsp" %>
						</td>
					</tr>
					
					</tbody>
				</table>
			</logic:notEmpty>
		
			<logic:empty name="usrAplSearchPageVO" property="listResult">
				<table>		
					<logic:equal name="userSession" property="navModel.act" value="buscar">
						<tr>						
							<td align="center">
								<br>							
									<bean:message bundle="seg" key="usrAplSearchPage.result.label.resultadoVacio"/>
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
 	    				<td align="left">
						<bean:define id="agregarEnabled" name="usrAplSearchPageVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar', '');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
					</td>
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
