<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/rec/AdministrarUsoCdM.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="rec" key="rec.usoCdMViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- UsoCdM -->
		<fieldset>
			<legend><bean:message bundle="rec" key="rec.usoCdM.title"/></legend>
			<table class="tabladatos">
				<!-- Descripcion -->
				<tr>
					<td><label><bean:message bundle="rec" key="rec.usoCdM.desUsoCdM.label"/>: </label></td>
					<td class="normal"><bean:write name="usoCdMAdapterVO" property="usoCdM.desUsoCdM"/></td>
				</tr>
				
				<!-- Factor -->
				<tr>
					<td><label><bean:message bundle="rec" key="rec.usoCdM.factor.label"/>: </label></td>
					<td class="normal"><bean:write name="usoCdMAdapterVO" property="usoCdM.factorView"/></td>
				</tr>

				<!-- Usos Catastro -->
				<tr>
					<td><label><bean:message bundle="rec" key="rec.usoCdM.usosCatastro.label"/>: </label></td>
					<td class="normal"><bean:write name="usoCdMAdapterVO" property="usoCdM.usosCatastro"/></td>
				</tr>
				
				<!-- Estado -->
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="usoCdMAdapterVO" property="usoCdM.estado.value"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- UsoCdM -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="usoCdMAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="usoCdMAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="usoCdMAdapterVO" property="act" value="desactivar">
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
