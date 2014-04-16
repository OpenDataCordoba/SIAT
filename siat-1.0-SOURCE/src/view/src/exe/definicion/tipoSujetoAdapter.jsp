<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/exe/AdministrarTipoSujeto.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="exe" key="exe.tipoSujetoAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- TipoSujeto -->
		<fieldset>
			<legend><bean:message bundle="exe" key="exe.tipoSujeto.title"/></legend>
			
			<table class="tabladatos">
				<!-- Codigo -->
				<tr>
					<td><label><bean:message bundle="exe" key="exe.tipoSujeto.codTipoSujeto.label"/>: </label></td>
					<td class="normal"><bean:write name="tipoSujetoAdapterVO" property="tipoSujeto.codTipoSujeto"/></td>
				</tr>
				<!-- Descricion -->
				<tr>
					<td><label><bean:message bundle="exe" key="exe.tipoSujeto.desTipoSujeto.label"/>: </label></td>
					<td class="normal"><bean:write name="tipoSujetoAdapterVO" property="tipoSujeto.desTipoSujeto"/></td>
				</tr>
								
				<!-- Estado -->
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="tipoSujetoAdapterVO" property="tipoSujeto.estado.value"/></td>
				</tr>
				<tr>
					<td colspan="4"> 
						<bean:define id="modificarEncabezadoEnabled" name="tipoSujetoAdapterVO" property="modificarEncabezadoEnabled"/>
						<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificarEncabezado', 
							'<bean:write name="tipoSujetoAdapterVO" property="tipoSujeto.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.modificar"/>"/>		
					</td>
				</tr>
			</table>
		</fieldset>	
		<!-- TipoSujeto -->
		
		<!-- TipSujExe -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="exe" key="exe.tipoSujeto.listTipSujExe.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="tipoSujetoAdapterVO" property="tipoSujeto.listTipSujExe">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="def" key="def.recurso.label"/></th>
						<th align="left"><bean:message bundle="exe" key="exe.exencion.label"/></th>						
					</tr>
					<logic:iterate id="TipSujExeVO" name="tipoSujetoAdapterVO" property="tipoSujeto.listTipSujExe">
			
						<tr>
						
							<!-- Eliminar-->								
							<td>
								<logic:equal name="tipoSujetoAdapterVO" property="eliminarTipSujExeEnabled" value="enabled">
									<logic:equal name="TipSujExeVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarTipSujExe', '<bean:write name="TipSujExeVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="TipSujExeVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="tipoSujetoAdapterVO" property="eliminarTipSujExeEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="TipSujExeVO" property="exencion.recurso.desRecurso"/>&nbsp;</td>
							<td><bean:write name="TipSujExeVO" property="exencion.desExencion"/>&nbsp;</td>							
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="tipoSujetoAdapterVO" property="tipoSujeto.listTipSujExe">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
				<tr>
					<td align="right" colspan="10">
   	    				<bean:define id="agregarEnabled" name="tipoSujetoAdapterVO" property="agregarTipSujExeEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregarTipSujExe', '<bean:write name="tipoSujetoAdapterVO" property="tipoSujeto.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
					</td>
				</tr>
			</tbody>
		</table>
		<!-- TipSujExe -->
				
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
