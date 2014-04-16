<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarLiqCom.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="gde" key="gde.liqComViewAdapter.title"/></h1>	
		
		<!-- LiqCom -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.liqCom.title"/></legend>
			<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="gde" key="gde.liqCom.fechaLiquidacion.label"/>: </label></td>
				<td class="normal"><bean:write name="liqComAdapterVO" property="liqCom.fechaLiquidacionView"/></td>				
			</tr>
			<tr>
				<logic:equal name="liqComAdapterVO" property="liqCom.porServicioBanco" value="true">
					<td><label><bean:message bundle="def" key="def.servicioBanco.desServicioBanco.ref"/>: </label></td>
					<td class="normal"><bean:write name="liqComAdapterVO" property="liqCom.servicioBanco.desServicioBanco"/></td>
				</logic:equal>
				<logic:equal name="liqComAdapterVO" property="liqCom.porServicioBanco" value="false">
					<td><label><bean:message bundle="def" key="def.recurso.desRecurso.ref"/>: </label></td>
					<td class="normal"><bean:write name="liqComAdapterVO" property="liqCom.recurso.desRecurso"/></td>
				</logic:equal>
			</tr>
			
				<!-- Caso -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
					<td colspan="3">
						<bean:define id="IncludedVO" name="liqComAdapterVO" property="liqCom"/>
						<%@ include file="/cas/caso/includeCasoView.jsp" %>
					</td>
				</tr>		
						
			<tr>
				<td><label><bean:message bundle="gde" key="gde.liqCom.fechaPagoHasta.label"/>: </label></td>
				<td class="normal"><bean:write name="liqComAdapterVO" property="liqCom.fechaPagoHastaView"/></td>				
			</tr>
			<tr>
				<td><label><bean:message bundle="gde" key="gde.liqCom.observacion.label"/>: </label></td>
				<td class="normal"><bean:write name="liqComAdapterVO" property="liqCom.observacion"/></td>				
			</tr>
				<!-- <#BeanWrites#> -->
			</table>
		</fieldset>	
		<!-- LiqCom -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="liqComAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="liqComAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="liqComAdapterVO" property="act" value="desactivar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
							<bean:message bundle="base" key="abm.button.desactivar"/>
						</html:button>
					</logic:equal>
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
