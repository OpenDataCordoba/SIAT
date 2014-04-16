<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/def/AdministrarSubrubro.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="def" key="def.subrubroAdapter.title"/></h1>	
		
		<!-- Subrubro -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.subrubro.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="def" key="def.categoria.desCategoria.ref"/>: </label></td>
					<td class="normal"><bean:write name="subrubroAdapterVO" property="subrubro.rubro.categoria.desCategoria"/></td>
			
					<td><label><bean:message bundle="def" key="def.rubro.desRubro.ref"/>: </label></td>
					<td class="normal"><bean:write name="subrubroAdapterVO" property="subrubro.rubro.desRubro"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.subrubro.desSubrubro.label"/>: </label></td>
					<td class="normal"><bean:write name="subrubroAdapterVO" property="subrubro.desSubrubro"/></td>
					
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal" ><bean:write name="subrubroAdapterVO" property="subrubro.estado.value"/></td>
				</tr>
			</table>
			<p>
				<logic:equal name="userSession" property="navModel.act" value="eliminar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('borrar', '');">
						<bean:message bundle="base" key="abm.button.eliminar"/>
					</html:button>
				</logic:equal>
			</p>
		</fieldset>	
		<!-- Subrubro -->

		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
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
