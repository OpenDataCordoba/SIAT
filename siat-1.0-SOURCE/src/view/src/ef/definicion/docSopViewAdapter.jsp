<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/AdministrarDocSop.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="ef" key="ef.docSopViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- DocSop -->
		<fieldset>
			<legend><bean:message bundle="ef" key="ef.docSop.title"/></legend>
			<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="ef" key="ef.docSop.desDocSop.label"/>: </label></td>
				<td class="normal"><bean:write name="docSopAdapterVO" property="docSop.desDocSop"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="ef" key="ef.docSop.determinaAjuste.label"/>: </label></td>
				<td class="normal"><bean:write name="docSopAdapterVO" property="docSop.determinaAjuste.value"/></td>				
			</tr>
			<tr>
				<td><label><bean:message bundle="ef" key="ef.docSop.aplicaMulta.label"/>: </label></td>
				<td class="normal"><bean:write name="docSopAdapterVO" property="docSop.aplicaMulta.value"/></td>				
			</tr>
			<tr>
				<td><label><bean:message bundle="ef" key="ef.docSop.compensaSalAFav.label"/>: </label></td>
				<td class="normal"><bean:write name="docSopAdapterVO" property="docSop.compensaSalAFav.value"/></td>				
			</tr>
			<tr>
				<td><label><bean:message bundle="ef" key="ef.docSop.devuelveSalAFav.label"/>: </label></td>
				<td class="normal"><bean:write name="docSopAdapterVO" property="docSop.devuelveSalAFav.value"/></td>				
			</tr>
			<tr>
				<td><label><bean:message bundle="ef" key="ef.docSop.plantilla.label"/>: </label></td>
				<td class="normal"><bean:write name="docSopAdapterVO" property="docSop.plantilla"/></td>				
			</tr>
				<!-- <#BeanWrites#> -->
			</table>
		</fieldset>	
		<!-- DocSop -->

		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="left">
	   	    	   <logic:equal name="docSopAdapterVO" property="act" value="ver">
		   	    	    <html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						    <bean:message bundle="base" key="abm.button.imprimir"/>
					    </html:button>
					</logic:equal>
					<logic:equal name="docSopAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="docSopAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="docSopAdapterVO" property="act" value="desactivar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
							<bean:message bundle="base" key="abm.button.desactivar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
	   	    </tr>
	   	 </table>
	    <input type="hidden" name="name"  value="<bean:write name='docSopAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
