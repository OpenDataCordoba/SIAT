<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/gde/AdministrarAgeRet.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="gde" key="gde.ageRetViewAdapter.title"/></h1>	
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- Atributo -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.ageRet.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="ageRetAdapterVO" property="ageRet.recurso.desRecurso"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.ageRet.desAgeRet.label"/>: </label></td>
					<td class="normal"><bean:write name="ageRetAdapterVO" property="ageRet.desAgeRet"/></td>
				</tr>
				
				<tr>
					<td><label><bean:message bundle="gde" key="gde.ageRet.cuit.label"/>: </label></td>
					<td class="normal"><bean:write name="ageRetAdapterVO" property="ageRet.cuit" /></td>
				</tr>
				<tr>	
					<td><label><bean:message bundle="gde" key="gde.ageRet.fechaDesde.label"/>: </label></td>
					<td class="normal"><bean:write name="ageRetAdapterVO" property="ageRet.fechaDesdeView" /></td>					
				</tr>
				
				<tr>
					<td><label><bean:message bundle="gde" key="gde.ageRet.fechaHasta.label"/>: </label></td>
					<td class="normal"><bean:write name="ageRetAdapterVO" property="ageRet.fechaHastaView"/></td>				
				</tr>
			</table>
		</fieldset>	
		<!-- Atributo -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="ageRetAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="ageRetAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="ageRetAdapterVO" property="act" value="desactivar">
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
