<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarProPreDeu.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="gde" key="gde.proPreDeuViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- ProPreDeu -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.proPreDeu.title"/></legend>
			<table class="tabladatos">
				<tr>
					<!-- Via Deuda -->
					<td><label><bean:message bundle="gde" key="gde.proPreDeu.viaDeuda.label"/>: </label></td>
					<td class="normal"><bean:write name="proPreDeuAdapterVO" property="proPreDeu.viaDeuda.desViaDeuda"/></td>
				</tr>
				<tr>
					<!-- Servicio Banco -->
					<td><label><bean:message bundle="gde" key="gde.proPreDeu.servicioBanco.label"/>: </label></td>
					<td class="normal"><bean:write name="proPreDeuAdapterVO" property="proPreDeu.servicioBanco.desServicioBanco"/></td>
				</tr>
				<tr>
					<!-- Fecha Tope -->
					<td><label><bean:message bundle="gde" key="gde.proPreDeu.fechaTope.label"/>: </label></td>
					<td class="normal"><bean:write name="proPreDeuAdapterVO" property="proPreDeu.fechaTopeView"/></td>
				</tr>
				<tr>
					<!-- Estado -->
					<td><label><bean:message bundle="gde" key="gde.proPreDeu.estadoCorrida.label"/>: </label></td>
					<td class="normal"><bean:write name="proPreDeuAdapterVO" property="proPreDeu.corrida.estadoCorrida.desEstadoCorrida"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- ProPreDeu -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	   <logic:equal name="proPreDeuAdapterVO" property="act" value="ver">
		   	    	    <html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						    <bean:message bundle="base" key="abm.button.imprimir"/>
					    </html:button>
					</logic:equal>
				</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="proPreDeuAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
	   	    </tr>
	   	 </table>
	   	<input type="hidden" name="name"  value="<bean:write name='proPreDeuAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->