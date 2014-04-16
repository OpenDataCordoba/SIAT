<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/ef/AdministrarOrdConDoc.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="ef" key="ef.ordConDocAdapter.title"/></h1>	
		
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
		<bean:define id="actaVO" name="ordConDocAdapterVO" property="ordConDoc.acta"/>
		<%@include file="/ef/fiscalizacion/includeActaViewAdapter.jsp" %>	
		<!-- Acta -->
				
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="ef" key="ef.documentacion.label"/></caption>
	    	<tbody>
			    	<tr>
						<th align="left"><bean:message bundle="ef" key="ef.documentacion.desDocumentacion.label"/></th>
					</tr>
					<tr><td><bean:write name="ordConDocAdapterVO" property="ordConDoc.documentacion.desDoc"/>&nbsp;</td></tr>
			</tbody>
		</table>		
						
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			<td align="right">
				<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('eliminar', '');">
					<bean:message bundle="base" key="abm.button.eliminar"/>
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
<!-- ordConDocViewAdapter.jsp -->