<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/exe/AdministrarEstadoCueExe.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="exe" key="exe.estadoCueExeAdapter.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- EstadoCueExe -->
		<fieldset>
			<legend><bean:message bundle="exe" key="exe.estadoCueExe.title"/></legend>
			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="exe" key="exe.estadoCueExe.codEstadoCueExe.label"/>: </label></td>
					<td class="normal"><bean:write name="estadoCueExeAdapterVO" property="estadoCueExe.codEstadoCueExe"/></td>
					
					<td><label><bean:message bundle="exe" key="exe.estadoCueExe.desEstadoCueExe.label"/>: </label></td>
					<td class="normal"><bean:write name="estadoCueExeAdapterVO" property="estadoCueExe.desEstadoCueExe" /></td>
				</tr>
								
				<tr>
					<td><label><bean:message bundle="exe" key="exe.estadoCueExe.fechaAlta.label"/>: </label></td>
					<td class="normal"><bean:write name="estadoCueExeAdapterVO" property="estadoCueExe.fechaAltaView"/></td>
					<td><label><bean:message bundle="exe" key="exe.estadoCueExe.fechaBaja.label"/>: </label></td>
					<td class="normal"><bean:write name="estadoCueExeAdapterVO" property="estadoCueExe.fechaBajaView" /></td>
				</tr>
				
				<tr>
					<td colspan="4"> 
						<bean:define id="modificarEncabezadoEnabled" name="estadoCueExeAdapterVO" property="modificarEncabezadoEnabled"/>
						<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificarEncabezado', 
							'<bean:write name="estadoCueExeAdapterVO" property="estadoCueExe.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.modificar"/>"/>		
					</td>
				</tr>
			</table>
		</fieldset>	
		<!-- EstadoCueExe -->
		
		<!--  -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="exe" key="exe.estadoCueExe.list.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="estadoCueExeAdapterVO" property="estadoCueExe.list">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="exe" key="exe.atributo.codAtributo.ref"/></th>
						<th align="left"><bean:message bundle="exe" key="exe.atributo.desAtributo.ref"/></th>
						<th align="left"><bean:message bundle="exe" key="exe.tipoAtributo.ref"/></th>						
					</tr>
					<logic:iterate id="VO" name="estadoCueExeAdapterVO" property="estadoCueExe.list">
			
						<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="estadoCueExeAdapterVO" property="verEnabled" value="enabled">
									<logic:equal name="VO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="VO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="VO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="estadoCueExeAdapterVO" property="verEnabled" value="enabled">										
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Modificar-->								
							<td>
								<logic:equal name="estadoCueExeAdapterVO" property="modificarEnabled" value="enabled">
									<logic:equal name="VO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="VO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="VO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="estadoCueExeAdapterVO" property="modificarEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Eliminar-->								
							<td>
								<logic:equal name="estadoCueExeAdapterVO" property="eliminarEnabled" value="enabled">
									<logic:equal name="VO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar', '<bean:write name="VO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="VO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="estadoCueExeAdapterVO" property="eliminarEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="VO" property="atributo.codAtributo"/>&nbsp;</td>
							<td><bean:write name="VO" property="atributo.desAtributo"/>&nbsp;</td>
							<td><bean:write name="VO" property="atributo.tipoAtributo.desTipoAtributo"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="estadoCueExeAdapterVO" property="estadoCueExe.list">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
				<tr>
					<td align="right" colspan="10">
   	    				<bean:define id="agregarEnabled" name="estadoCueExeAdapterVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar', '<bean:write name="estadoCueExeAdapterVO" property="estadoCueExe.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
					</td>
				</tr>
			</tbody>
		</table>
		<!--  -->
				
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
