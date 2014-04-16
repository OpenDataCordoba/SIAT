<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/ef/AdministrarActa.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="ef" key="ef.actaAdapter.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- Acta -->
		<bean:define id="modificarEncabezadoEnabled" value="null"/>
		<bean:define id="actaVO" name="actaAdapterVO" property="acta"/>
		<%@include file="/ef/fiscalizacion/includeActaViewAdapter.jsp" %>
		<!-- Acta -->
		
		<!-- OrdConDoc -->

		<logic:equal name="actaAdapterVO" property="acta.tipoActa.id" value="2">
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="ef" key="ef.actaViewAdapter.actaProcedimiento.listOrdConDoc"/></caption>
		    	<tbody>
					<logic:notEmpty  name="actaAdapterVO" property="listOrdConDoc">	    	
				    	<tr>
						</tr>
						<logic:iterate id="OrdConDocVO" name="actaAdapterVO" property="listOrdConDoc">
				
							<tr>							
								<td><bean:write name="OrdConDocVO" property="documentacion.desDoc"/>&nbsp;</td>							
							</tr>
						</logic:iterate>
					</logic:notEmpty>
					<logic:empty  name="actaAdapterVO" property="listOrdConDoc">
						<tr><td align="center">
								<logic:equal name="actaAdapterVO" property="acta.tipoActa.id" value="2">							
									<bean:message bundle="ef" key="ef.actaViewAdapter.actaProcedimiento.listOrdConDoc.vacia"/>
								</logic:equal>
								<logic:notEqual name="actaAdapterVO" property="acta.tipoActa.id" value="2">
									<bean:message bundle="ef" key="ef.actaViewAdapter.actaIniReq.listOrdConDoc.vacia"/>
								</logic:notEqual>
						</td></tr>
					</logic:empty>
	
				</tbody>
			</table>		
		</logic:equal>
		
		<logic:notEqual name="actaAdapterVO" property="acta.tipoActa.id" value="2">		
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="ef" key="ef.actaViewAdapter.actaIniReq.listOrdConDoc"/></caption>
		    	<tbody>
					<logic:notEmpty  name="actaAdapterVO" property="acta.listOrdConDoc">	    	
						<logic:iterate id="OrdConDocVO" name="actaAdapterVO" property="acta.listOrdConDoc">				
							<tr>							
								<td><bean:write name="OrdConDocVO" property="documentacion.desDoc"/>&nbsp;</td>							
							</tr>
						</logic:iterate>
					</logic:notEmpty>
					<logic:empty  name="actaAdapterVO" property="acta.listOrdConDoc">
						<tr><td align="center">
								<logic:equal name="actaAdapterVO" property="acta.tipoActa.id" value="2">							
									<bean:message bundle="ef" key="ef.actaViewAdapter.actaProcedimiento.listOrdConDoc.vacia"/>
								</logic:equal>
								<logic:notEqual name="actaAdapterVO" property="acta.tipoActa.id" value="2">
									<bean:message bundle="ef" key="ef.actaViewAdapter.actaIniReq.listOrdConDoc.vacia"/>
								</logic:notEqual>
						</td></tr>
					</logic:empty>
	
				</tbody>
			</table>
		</logic:notEqual>						
		<!-- OrdConDoc -->
				
		<table class="tablabotones">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td> 
				<logic:equal name="actaAdapterVO" property="act" value="eliminar">
					<td align="left">
	   	    				<bean:define id="eliminarEnabled" name="actaAdapterVO" property="eliminarEnabled"/>
							<input type="button" <%=eliminarEnabled%> class="boton" 
								onClick="submitForm('eliminar', '<bean:write name="actaAdapterVO" property="acta.id" bundle="base" formatKey="general.format.id"/>');" 
								value="<bean:message bundle="base" key="abm.button.eliminar"/>"
							/>
					</td>
				</logic:equal>
				<logic:equal name="actaAdapterVO" property="act" value="ver">
					<td align="left">
	   	    				<bean:define id="imprimirEnabled" name="actaAdapterVO" property="imprimirActaEnabled"/>
							<input type="button" <%=imprimirEnabled%> class="boton" onClick="submitForm('imprimir', '');" value="<bean:message bundle="base" key="abm.button.imprimir"/>"/>
				   </td>
				</logic:equal>	
			</tr>
		</table>
	   	
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
<!-- actaViewAdapter.jsp -->