<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarGesJudEvento.do">

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
	 	<bean:define id="act" name="gesJudEventoAdapterVO" property="act"/>
	 	<bean:define id="gesJud" name="gesJudEventoAdapterVO" property="gesJudEvento.gesJud"/>	 	
		<%@ include file="/gde/adminDeuJud/includeGesJud.jsp" %>
		<!-- GesJud -->
	
		<!-- GesJudEvento -->
			<fieldset>
				<legend><bean:message bundle="gde" key="gde.gesJudEvento.label"/></legend>
				<bean:define id="gesJudEvento" name="gesJudEventoAdapterVO" property="gesJudEvento"/>
				<table class="tabladatos">
					<tr>
						<td><label><bean:message bundle="gde" key="gde.evento.codigo.label"/>:</label></td>
						<td class="normal"><bean:write name="gesJudEvento" property="evento.codigoView"/></td>
						<td><label><bean:message bundle="gde" key="gde.evento.descripcion.label"/>:</label></td>
						<td class="normal"><bean:write name="gesJudEvento" property="evento.descripcion" /></td>
					</tr>
					<tr>
						<td><label><bean:message bundle="gde" key="gde.gesJudEvento.fechaEvento.label"/>:</label></td>
						<td class="normal"><bean:write name="gesJudEvento" property="fechaEventoView"/></td>
						<td><label><bean:message bundle="gde" key="gde.evento.etapaProcesal.label"/>:</label></td>
						<td class="normal"><bean:write name="gesJudEvento" property="evento.etapaProcesal.desEtapaProcesal"/></td>						
					</tr>
					<tr>
						<td><label><bean:message bundle="gde" key="gde.gesJudEvento.observaciones.label"/>:</label></td>
						<td class="normal" colspan="3"><bean:write name="gesJudEvento" property="observacion"/></td>
					</tr>					
				</table>
			</fieldset>
		<!-- FIN GesJudEvento -->
		
		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="gesJudEventoAdapterVO" property="act" value="eliminar">
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
