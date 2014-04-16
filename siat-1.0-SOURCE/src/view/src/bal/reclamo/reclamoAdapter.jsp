<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/AdministrarReclamo.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.reclamoAdapter.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- Reclamo -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.reclamo.title"/></legend>
			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="bal" key="bal.reclamo.codReclamo.label"/>: </label></td>
					<td class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.codReclamo"/></td>
					
					<td><label><bean:message bundle="bal" key="bal.reclamo.desReclamo.label"/>: </label></td>
					<td class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.desReclamo" /></td>
				</tr>
								
				<tr>
					<td><label><bean:message bundle="bal" key="bal.reclamo.fechaAlta.label"/>: </label></td>
					<td class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.fechaAltaView"/></td>
					<td><label><bean:message bundle="bal" key="bal.reclamo.fechaBaja.label"/>: </label></td>
					<td class="normal"><bean:write name="reclamoAdapterVO" property="reclamo.fechaBajaView" /></td>
				</tr>
				
				<tr>
					<td colspan="4"> 
						<bean:define id="modificarEncabezadoEnabled" name="reclamoAdapterVO" property="modificarEncabezadoEnabled"/>
						<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificarEncabezado', 
							'<bean:write name="reclamoAdapterVO" property="reclamo.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.modificar"/>"/>		
					</td>
				</tr>
			</table>
		</fieldset>	
		<!-- Reclamo -->
		
		<!-- reclamo_Detalle -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.reclamo.listreclamo_Detalle.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="reclamoAdapterVO" property="reclamo.listreclamo_Detalle">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="bal" key="bal.atributo.codAtributo.ref"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.atributo.desAtributo.ref"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.tipoAtributo.ref"/></th>						
					</tr>
					<logic:iterate id="reclamo_DetalleVO" name="reclamoAdapterVO" property="reclamo.listreclamo_Detalle">
			
						<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="reclamoAdapterVO" property="verreclamo_DetalleEnabled" value="enabled">
									<logic:equal name="reclamo_DetalleVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verreclamo_Detalle', '<bean:write name="reclamo_DetalleVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="reclamo_DetalleVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="reclamoAdapterVO" property="verreclamo_DetalleEnabled" value="enabled">										
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Modificar-->								
							<td>
								<logic:equal name="reclamoAdapterVO" property="modificarreclamo_DetalleEnabled" value="enabled">
									<logic:equal name="reclamo_DetalleVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarreclamo_Detalle', '<bean:write name="reclamo_DetalleVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="reclamo_DetalleVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="reclamoAdapterVO" property="modificarreclamo_DetalleEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Eliminar-->								
							<td>
								<logic:equal name="reclamoAdapterVO" property="eliminarreclamo_DetalleEnabled" value="enabled">
									<logic:equal name="reclamo_DetalleVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarreclamo_Detalle', '<bean:write name="reclamo_DetalleVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="reclamo_DetalleVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="reclamoAdapterVO" property="eliminarreclamo_DetalleEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="reclamo_DetalleVO" property="atributo.codAtributo"/>&nbsp;</td>
							<td><bean:write name="reclamo_DetalleVO" property="atributo.desAtributo"/>&nbsp;</td>
							<td><bean:write name="reclamo_DetalleVO" property="atributo.tipoAtributo.desTipoAtributo"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="reclamoAdapterVO" property="reclamo.listreclamo_Detalle">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
				<tr>
					<td align="right" colspan="10">
   	    				<bean:define id="agregarEnabled" name="reclamoAdapterVO" property="agregarreclamo_DetalleEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregarreclamo_Detalle', '<bean:write name="reclamoAdapterVO" property="reclamo.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
					</td>
				</tr>
			</tbody>
		</table>
		<!-- reclamo_Detalle -->
				
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
