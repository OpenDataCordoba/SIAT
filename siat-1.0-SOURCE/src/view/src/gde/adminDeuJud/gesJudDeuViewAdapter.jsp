<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarGesJudDeu.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="gde" key="gde.gesJudAdapter.title"/></h1>	
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		<!-- GesJud -->
	 	<bean:define id="act" name="gesJudDeuAdapterVO" property="act"/>
	 	<bean:define id="gesJud" name="gesJudDeuAdapterVO" property="gesJudDeu.gesJud"/>	 	
		<%@ include file="/gde/adminDeuJud/includeGesJud.jsp" %>
		<!-- GesJud -->
	
		<!-- ConstanciaDeu -->
		<%@ include file="/gde/adminDeuJud/includeGesJudConstanciaDeuView.jsp" %>
		<!-- FIN ConstanciaDeu -->
						 
		<!-- GesJudDeu -->
			<fieldset>
				<legend><bean:message bundle="gde" key="gde.gesJudDeu.deuda.title"/></legend>
				<bean:define id="gesJudDeu" name="gesJudDeuAdapterVO" property="gesJudDeu"/>
				<table class="tabladatos">
					<tr>
						<td><label><bean:message bundle="gde" key="gde.deuda.label"/>:</label></td>
						<td class="normal"><bean:write name="gesJudDeu" property="deuda.nroBarraAnio"/></td>
						<td><label><bean:message bundle="gde" key="gde.deuda.importe.label"/>:</label></td>
						<td class="normal"><bean:write name="gesJudDeu" property="deuda.importeView" /></td>
					</tr>
					<tr>
						<td><label><bean:message bundle="gde" key="gde.deuda.saldo.label"/>:</label></td>
						<td class="normal"><bean:write name="gesJudDeu" property="deuda.saldoView"/></td>
						<td><label><bean:message bundle="gde" key="gde.deuda.fechaVencimiento.label"/>:</label></td>
						<td class="normal"><bean:write name="gesJudDeu" property="deuda.fechaVencimientoView"/></td>						
					</tr>
					<tr>
						<td><label><bean:message bundle="gde" key="gde.constanciaDeu.numero.ref"/>:</label></td>
						<td class="normal"><bean:write name="gesJudDeu" property="constanciaDeu.numeroView"/></td>
						<td><label><bean:message bundle="gde" key="gde.constanciaDeu.anio.ref"/>:</label></td>
						<td class="normal"><bean:write name="gesJudDeu" property="constanciaDeu.anioView"/></td>						
					</tr>
					<tr>
						<td><label><bean:message bundle="gde" key="gde.gesJudDeu.observaciones.label"/>:</label></td>
						<td class="normal" colspan="3"><bean:write name="gesJudDeu" property="observacion"/></td>						
					</tr>					
				</table>
			</fieldset>
		<!-- FIN GesJudDeu -->
		
		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="gesJudDeuAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
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
