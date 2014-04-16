<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/AdministrarInspector.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="ef" key="ef.inspectorViewAdapter.title"/></h1>	
		
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
				<td class="normal"><bean:write name="inspectorAdapterVO" property="inspector.desInspector"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="ef" key="ef.inspector.fechaDesde.label"/>: </label></td>
				<td class="normal"><bean:write name="inspectorAdapterVO" property="inspector.fechaDesdeView"/></td>				
			</tr>
			<tr>
				<td><label><bean:message bundle="ef" key="ef.inspector.fechaHasta.label"/>: </label></td>
				<td class="normal"><bean:write name="inspectorAdapterVO" property="inspector.fechaHastaView"/></td>				
			</tr>
			</table>
			</fieldset>	
			 <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="ef" key="ef.inspector.listInsSup.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="inspectorAdapterVO" property="inspector.listInsSup">	    	
			    	<tr>
						<th align="left"><bean:message bundle="ef"  key="ef.inspector.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="ef"  key="ef.inspector.fechaHasta.label"/></th>
						<th align="left"><bean:message bundle="ef"  key="ef.supervisor.label"/></th>						
					</tr>
					<logic:iterate id="InsSupVO" name="inspectorAdapterVO" property="inspector.listInsSup">
						<tr>					                         
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
			</tbody>
		</table>

		<!-- Inspector -->

		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	
	   	    	<td align="left">
	   	            <logic:equal name="inspectorAdapterVO" property="act" value="ver">
				   	   <html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						   <bean:message bundle="base" key="abm.button.imprimir"/>
					   </html:button>
					</logic:equal>
					<logic:equal name="inspectorAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
	   	    </tr>
	   	 </table>
	   	<input type="hidden" name="name"  value="<bean:write name='inspectorAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
	   			
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
