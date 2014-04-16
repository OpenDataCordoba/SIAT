<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/${modulo}/Administrar${Bean}.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="${modulo}" key="${modulo}.${bean}Adapter.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- ${Bean} -->
		<fieldset>
			<legend><bean:message bundle="${modulo}" key="${modulo}.${bean}.title"/></legend>
			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="${modulo}" key="${modulo}.${bean}.cod${Bean}.label"/>: </label></td>
					<td class="normal"><bean:write name="${bean}AdapterVO" property="${bean}.cod${Bean}"/></td>
					
					<td><label><bean:message bundle="${modulo}" key="${modulo}.${bean}.des${Bean}.label"/>: </label></td>
					<td class="normal"><bean:write name="${bean}AdapterVO" property="${bean}.des${Bean}" /></td>
				</tr>
								
				<tr>
					<td><label><bean:message bundle="${modulo}" key="${modulo}.${bean}.fechaAlta.label"/>: </label></td>
					<td class="normal"><bean:write name="${bean}AdapterVO" property="${bean}.fechaAltaView"/></td>
					<td><label><bean:message bundle="${modulo}" key="${modulo}.${bean}.fechaBaja.label"/>: </label></td>
					<td class="normal"><bean:write name="${bean}AdapterVO" property="${bean}.fechaBajaView" /></td>
				</tr>
				
				<tr>
					<td colspan="4"> 
						<bean:define id="modificarEncabezadoEnabled" name="${bean}AdapterVO" property="modificarEncabezadoEnabled"/>
						<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificarEncabezado', 
							'<bean:write name="${bean}AdapterVO" property="${bean}.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.modificar"/>"/>		
					</td>
				</tr>
			</table>
		</fieldset>	
		<!-- ${Bean} -->
		
		<!-- ${Bean_Detalle} -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="${modulo}" key="${modulo}.${bean}.list${Bean_Detalle}.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="${bean}AdapterVO" property="${bean}.list${Bean_Detalle}">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="${modulo}" key="${modulo}.atributo.codAtributo.ref"/></th>
						<th align="left"><bean:message bundle="${modulo}" key="${modulo}.atributo.desAtributo.ref"/></th>
						<th align="left"><bean:message bundle="${modulo}" key="${modulo}.tipoAtributo.ref"/></th>						
					</tr>
					<logic:iterate id="${Bean_Detalle}VO" name="${bean}AdapterVO" property="${bean}.list${Bean_Detalle}">
			
						<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="${bean}AdapterVO" property="ver${Bean_Detalle}Enabled" value="enabled">
									<logic:equal name="${Bean_Detalle}VO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver${Bean_Detalle}', '<bean:write name="${Bean_Detalle}VO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="${Bean_Detalle}VO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="${bean}AdapterVO" property="ver${Bean_Detalle}Enabled" value="enabled">										
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Modificar-->								
							<td>
								<logic:equal name="${bean}AdapterVO" property="modificar${Bean_Detalle}Enabled" value="enabled">
									<logic:equal name="${Bean_Detalle}VO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar${Bean_Detalle}', '<bean:write name="${Bean_Detalle}VO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="${Bean_Detalle}VO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="${bean}AdapterVO" property="modificar${Bean_Detalle}Enabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Eliminar-->								
							<td>
								<logic:equal name="${bean}AdapterVO" property="eliminar${Bean_Detalle}Enabled" value="enabled">
									<logic:equal name="${Bean_Detalle}VO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminar${Bean_Detalle}', '<bean:write name="${Bean_Detalle}VO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="${Bean_Detalle}VO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="${bean}AdapterVO" property="eliminar${Bean_Detalle}Enabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="${Bean_Detalle}VO" property="atributo.codAtributo"/>&nbsp;</td>
							<td><bean:write name="${Bean_Detalle}VO" property="atributo.desAtributo"/>&nbsp;</td>
							<td><bean:write name="${Bean_Detalle}VO" property="atributo.tipoAtributo.desTipoAtributo"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="${bean}AdapterVO" property="${bean}.list${Bean_Detalle}">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
				<tr>
					<td align="right" colspan="10">
   	    				<bean:define id="agregarEnabled" name="${bean}AdapterVO" property="agregar${Bean_Detalle}Enabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregar${Bean_Detalle}', '<bean:write name="${bean}AdapterVO" property="${bean}.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
					</td>
				</tr>
			</tbody>
		</table>
		<!-- ${Bean_Detalle} -->
				
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