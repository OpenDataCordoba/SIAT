<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/def/AdministrarAtributo.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="def" key="def.atributoViewAdapter.title"/></h1>	
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
			<legend><bean:message bundle="def" key="def.atributo.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="def" key="def.atributo.codAtributo.label"/>: </label></td>
					<td class="normal"><bean:write name="atributoAdapterVO" property="atributo.codAtributo"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.atributo.desAtributo.label"/>: </label></td>
					<td class="normal"><bean:write name="atributoAdapterVO" property="atributo.desAtributo"/></td>
				</tr>
				
				<tr>
					<td><label><bean:message bundle="def" key="def.tipoAtributo.desTipoAtributo.ref"/>: </label></td>
					<td class="normal"><bean:write name="atributoAdapterVO" property="atributo.tipoAtributo.desTipoAtributo" /></td>
				</tr>
				<tr>	
					<td><label><bean:message bundle="def" key="def.domAtr.desDomAtr.ref"/>: </label></td>
					<td class="normal"><bean:write name="atributoAdapterVO" property="atributo.domAtr.desDomAtrView" /></td>					
				</tr>
				
				<tr>
					<td><label><bean:message bundle="def" key="def.atributo.mascaraVisual.label"/>: </label></td>
					<td class="normal"><bean:write name="atributoAdapterVO" property="atributo.mascaraVisual"/></td>				
				</tr>
				
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="atributoAdapterVO" property="atributo.estado.value"/></td>
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
					<logic:equal name="atributoAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="atributoAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="atributoAdapterVO" property="act" value="desactivar">
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
