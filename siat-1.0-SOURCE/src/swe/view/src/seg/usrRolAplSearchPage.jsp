<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/swe/submitForm.js"%>	    
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/seg/BuscarUsrRolApl.do">

		<!-- Mensajes/Advertencias -->
		<%@ include file="/swe/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="seg" key="usrRolApl.title"/></h1>
		
		<p>Permite Buscar, Ver, Modificar, Eliminar y Agregar Roles al Usuario de Aplicaciones.</p>
		
		<!-- Filtro -->
		<fieldset>
			<legend>B&uacute;squeda de Roles de Usuario de la Aplicaci&oacute;n</legend>
			
			<p>
				<table class="tabladatos">
					<tr>
						<td><label><bean:message bundle="seg" key="usrRolAplSearchPage.filter.label.aplicacion.codigo"/>:</label></td>
						<td class="normal"><bean:write name="usrRolAplSearchPageVO" property="usrApl.aplicacion.codigo" /> </td>
						<td><label><bean:message bundle="seg" key="usrRolAplSearchPage.filter.label.aplicacion.descripcion"/>:</label></td>
						<td class="normal"><bean:write name="usrRolAplSearchPageVO" property="usrApl.aplicacion.descripcion" /></td>
					</tr>
					<tr>
						<td><label><bean:message bundle="seg" key="usrRolAplSearchPage.filter.label.usrApl.username"/>:</label></td>
						<td class="normal"><bean:write name="usrRolAplSearchPageVO" property="usrApl.username"/></td>
					</tr>
				</table>
			</p>
		</fieldset>	
		<!-- Fin Filtro -->
		
		<!-- Resultado Filtro -->
		<div id="resultadoFiltro">
		<logic:equal name="usrRolAplSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="usrRolAplSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
					<caption><bean:message bundle="seg" key="usrRolAplSearchPage.result.title"/></caption>
                	<tbody>
                	<tr>
						<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="seg" key="usrRolAplSearchPage.result.label.rolApl.codigo"/></th>
						<th align="left"><bean:message bundle="seg" key="usrRolAplSearchPage.result.label.rodApl.descripcion"/></th>
					</tr>
						
					<logic:iterate id="UsrRolAplVO" name="usrRolAplSearchPageVO" property="listResult">
						<tr>
							<!-- Ver/Seleccionar -->
							<td>
								<logic:notEqual name="usrRolAplSearchPageVO" property="inactivo" value="true">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="UsrRolAplVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</logic:notEqual>
								<logic:equal name="usrRolAplSearchPageVO" property="inactivo" value="true">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('seleccionar', '<bean:write name="UsrRolAplVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</logic:equal>									
							</td>
							
							<td>
								<!-- Eliminar-->
								<logic:equal name="usrRolAplSearchPageVO" property="ABMEnabled" value="enabled">		
									<logic:equal name="UsrRolAplVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="UsrRolAplVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="UsrRolAplVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>							
								</logic:equal>							
								<logic:notEqual name="usrRolAplSearchPageVO" property="ABMEnabled" value="enabled">		
									&nbsp;
								</logic:notEqual>
							</td>

							<td><bean:write name="UsrRolAplVO" property="rolApl.codigo"/></td>
							<td><bean:write name="UsrRolAplVO" property="rolApl.descripcion" /></td>

						</tr>
					</logic:iterate>
			
					</tbody>
				</table>
			</logic:notEmpty>
		
			<logic:empty name="usrRolAplSearchPageVO" property="listResult">
				<table>		
					<logic:equal name="userSession" property="navModel.act" value="buscar">
						<tr>						
							<td align="center">
								<br>							
									<bean:message bundle="seg" key="usrRolAplSearchPage.result.label.resultadoVacio"/>
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
					<td align="left">
						<bean:define id="agregarEnabled" name="usrRolAplSearchPageVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar', '');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
					</td>    		    
				</logic:equal>
  		    </tr>
		</table>	
		
		
		<html:hidden name="usrRolAplSearchPageVO" property="usrApl.id" />
		<!-- Fin Resultado Filtro -->	
		<input type="hidden" name="method" value=""/>
		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="pageNumber" value="0" id="pageNumber"> <!--NO TIENE QUE ESTAR PAGINADO -->
		<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
