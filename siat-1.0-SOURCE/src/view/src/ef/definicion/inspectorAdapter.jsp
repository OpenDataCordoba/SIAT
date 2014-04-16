<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/ef/AdministrarInspector.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="ef" key="ef.inspectorAdapter.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- Inspector -->
		<fieldset>
			<legend><bean:message bundle="ef" key="ef.inspector.title"/></legend>
			
			<table class="tabladatos">
			 
		    	<tr>
					<td><label><bean:message bundle="ef" key="ef.inspector.desInspector.label"/>: </label></td>
					<td class="normal"><bean:write name="inspectorAdapterVO" property="inspector.desInspector" /></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="ef" key="ef.inspector.fechaDesde.label"/>: </label></td>
					<td class="normal"><bean:write name="inspectorAdapterVO" property="inspector.fechaDesdeView"/></td>
					<td><label><bean:message bundle="ef" key="ef.inspector.fechaHasta.label"/>: </label></td>
					<td class="normal"><bean:write name="inspectorAdapterVO" property="inspector.fechaHastaView" /></td>
				</tr>
				
				<tr>
					<td colspan="4"> 
						<bean:define id="modificarEncabezadoEnabled" name="inspectorAdapterVO" property="modificarEncabezadoEnabled"/>
						<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificarEncabezado', 
							'<bean:write name="inspectorAdapterVO" property="inspector.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.modificar"/>"/>		
					</td>
				</tr>
			</table>
		</fieldset>	
		<!-- Inspector -->
		
		<!-- InsSup -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="ef" key="ef.inspector.listInsSup.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="inspectorAdapterVO" property="inspector.listInsSup">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
		        		<th align="left"><bean:message bundle="ef"  key="ef.inspector.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="ef"  key="ef.inspector.fechaHasta.label"/></th>
						<th align="left"><bean:message bundle="ef"  key="ef.supervisor.label"/></th>						
										
					</tr>
					<logic:iterate id="InsSupVO" name="inspectorAdapterVO" property="inspector.listInsSup">
			
						<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="inspectorAdapterVO" property="verInsSupEnabled" value="enabled">
									<logic:equal name="InsSupVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verInsSup', '<bean:write name="InsSupVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="InsSupVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="inspectorAdapterVO" property="verInsSupEnabled" value="enabled">										
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Modificar-->								
							<td>
								<logic:equal name="inspectorAdapterVO" property="modificarInsSupEnabled" value="enabled">
									<logic:equal name="InsSupVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarInsSup', '<bean:write name="InsSupVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="InsSupVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="inspectorAdapterVO" property="modificarInsSupEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Eliminar-->								
							<td>
								<logic:equal name="inspectorAdapterVO" property="eliminarInsSupEnabled" value="enabled">
									<logic:equal name="InsSupVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarInsSup', '<bean:write name="InsSupVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="InsSupVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="inspectorAdapterVO" property="eliminarInsSupEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="InsSupVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="InsSupVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="InsSupVO" property="supervisor.desSupervisor" />&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="inspectorAdapterVO" property="inspector.listInsSup">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
				<tr>
					<td align="right" colspan="10">
   	    				<bean:define id="agregarEnabled" name="inspectorAdapterVO" property="agregarInsSupEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregarInsSup', '<bean:write name="inspectorAdapterVO" property="inspector.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
					</td>
				</tr>
			</tbody>
		</table>
		<!-- InsSup -->
				
		<table class="tablabotones">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>   	    			
			</tr>
		</table>
	   	
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
