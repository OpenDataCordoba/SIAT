<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/def/AdministrarSiatScript.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="def" key="def.siatScriptAdapter.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- SiatScript -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.siatScript.title"/></legend>
			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="def" key="def.siatScript.codSiatScript.label"/>: </label></td>
					<td class="normal"><bean:write name="siatScriptAdapterVO" property="siatScript.codigo"/></td>
					
					<td><label><bean:message bundle="def" key="def.siatScript.pathSiatScript.label"/>: </label></td>
					<td class="normal"><bean:write name="siatScriptAdapterVO" property="siatScript.path"/></td>
				
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.siatScript.desSiatScript.label"/>: </label></td>
					<td class="normal"><bean:write name="siatScriptAdapterVO" property="siatScript.descripcion" /></td>
				</tr>				
								
				<tr>
					<td colspan="4"> 
						<bean:define id="modificarEncabezadoEnabled" name="siatScriptAdapterVO" property="modificarEncabezadoEnabled"/>
						<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificarEncabezado', 
							'<bean:write name="siatScriptAdapterVO" property="siatScript.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.modificar"/>"/>		
					</td>
				</tr>
			</table>
		</fieldset>	
		<!-- SiatScript -->
		
		<!-- SiatScriptUsr -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="def" key="def.siatScript.listSiatScriptUsr.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="siatScriptAdapterVO" property="siatScript.listSiatScriptUsr">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->				
						<th align="left"><bean:message bundle="def" key="def.siatScript.usuarioSiat.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.siatScript.proceso.desProceso.label"/></th>						
					</tr>
					<logic:iterate id="SiatScriptUsrVO" name="siatScriptAdapterVO" property="siatScript.listSiatScriptUsr">
			
						<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="siatScriptAdapterVO" property="verSiatScriptUsrEnabled" value="enabled">
									<logic:equal name="SiatScriptUsrVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verSiatScriptUsr', '<bean:write name="SiatScriptUsrVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="SiatScriptUsrVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="siatScriptAdapterVO" property="verSiatScriptUsrEnabled" value="enabled">										
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Modificar-->								
							<td>
								<logic:equal name="siatScriptAdapterVO" property="modificarSiatScriptUsrEnabled" value="enabled">
									<logic:equal name="SiatScriptUsrVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarSiatScriptUsr', '<bean:write name="SiatScriptUsrVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="SiatScriptUsrVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="siatScriptAdapterVO" property="modificarSiatScriptUsrEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Eliminar-->								
							<td>
								<logic:equal name="siatScriptAdapterVO" property="eliminarSiatScriptUsrEnabled" value="enabled">
									<logic:equal name="SiatScriptUsrVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarSiatScriptUsr', '<bean:write name="SiatScriptUsrVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="SiatScriptUsrVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="siatScriptAdapterVO" property="eliminarSiatScriptUsrEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="SiatScriptUsrVO" property="usuarioSiat.usuarioSIAT"/>&nbsp;</td>
							<td><bean:write name="SiatScriptUsrVO" property="proceso.desProceso"/>&nbsp;</td>							
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="siatScriptAdapterVO" property="siatScript.listSiatScriptUsr">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
				<tr>
					<td align="right" colspan="10">
   	    				<bean:define id="agregarEnabled" name="siatScriptAdapterVO" property="agregarSiatScriptUsrEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregarSiatScriptUsr', '<bean:write name="siatScriptAdapterVO" property="siatScript.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
					</td>
				</tr>
			</tbody>
		</table>
		
		<!-- SiatScriptUsr -->	
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