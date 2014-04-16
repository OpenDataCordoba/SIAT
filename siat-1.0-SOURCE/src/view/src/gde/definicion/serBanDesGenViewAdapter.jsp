<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/gde/AdministrarSerBanDesGen.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="gde" key="gde.serBanDesGenAdapter.title"/></h1>
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right"><html:button property="btnVolver"
				styleClass="boton" onclick="submitForm('volver', '');">
				<bean:message bundle="base" key="abm.button.volver" />
			</html:button></td>
		</tr>
	</table>
	<!-- Servicio Banco Enc -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.servicioBanco.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="def" key="def.servicioBanco.codServicioBanco.ref"/>: </label></td>
					<td class="normal"><bean:write name="serBanDesGenAdapterVO" property="serBanDesGen.servicioBanco.codServicioBanco"/></td>
			
					<td><label><bean:message bundle="def" key="def.servicioBanco.desServicioBanco.ref"/>: </label></td>
					<td class="normal"><bean:write name="serBanDesGenAdapterVO" property="serBanDesGen.servicioBanco.desServicioBanco"/></td>
				</tr>
			</table>
		</fieldset>
		<!-- Servicio Banco Enc-->

		<!-- SerBanDesGen -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.serBanDesGen.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="gde" key="gde.serBanDesGen.desDesGen.label"/>: </label></td>
					<td class="normal"><bean:write name="serBanDesGenAdapterVO" property="serBanDesGen.desGen.desDesGen"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.serBanDesGen.fechaDesde.label"/>: </label></td>
					<td class="normal"><bean:write name="serBanDesGenAdapterVO" property="serBanDesGen.fechaDesdeView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.serBanDesGen.fechaHasta.label"/>: </label></td>
					<td class="normal"><bean:write name="serBanDesGenAdapterVO" property="serBanDesGen.fechaHastaView"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- SerBanDesGen -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
  	    		<td align="right" width="50%">	   	    	
					<logic:equal name="serBanDesGenAdapterVO" property="act" value="eliminar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('eliminar', '');">
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
		
			

