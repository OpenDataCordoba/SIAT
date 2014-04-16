<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/def/AdministrarConAtr.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="def" key="def.conAtrViewAdapter.title"/></h1>	
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
				<td class="normal"><bean:write name="conAtrAdapterVO" property="conAtr.atributo.codAtributo"/></td>
				<td><label><bean:message bundle="def" key="def.atributo.desAtributo.label"/>: </label></td>
				<td class="normal"><bean:write name="conAtrAdapterVO" property="conAtr.atributo.desAtributo"/></td>
			</tr>
		</table>
	</fieldset>	
	<!-- Atributo -->

	<!-- ConAtr -->
	<fieldset>
		<legend><bean:message bundle="def" key="def.conAtr.caracteristicasGenerales.title"/></legend>
		<table class="tabladatos">
			<tr>
						
				<bean:define id="AtrVal" name="conAtrAdapterVO" property="genericAtrDefinition"/>
			
				<%@ include file="/def/atrDefinition4View.jsp" %>
			</tr>
			<tr>
				<td><label><bean:message bundle="def" key="def.conAtr.esAtrSegmentacion.label"/>: </label></td>
				<td class="normal">
					<bean:write name="conAtrAdapterVO" property="conAtr.esAtrSegmentacion.value"/>
				</td>
			</tr>
		</table>
	</fieldset>	
	<fieldset>
		<legend><bean:message bundle="def" key="def.conAtr.caracteristicasVisBusq.title"/></legend>
		<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="def" key="def.conAtr.esVisConDeu.label"/>: </label></td>
				<td class="normal">
					<bean:write name="conAtrAdapterVO" property="conAtr.esVisConDeu.value"/>
				</td>
			</tr>
			<tr>
				<td><label><bean:message bundle="def" key="def.conAtr.esAtributoBus.label"/>: </label></td>
				<td class="normal">
					<bean:write name="conAtrAdapterVO" property="conAtr.esAtributoBus.value"/>
				</td>
			</tr>
			<tr>
				<td><label><bean:message bundle="def" key="def.conAtr.admBusPorRan.label"/>: </label></td>
				<td class="normal">
					<bean:write name="conAtrAdapterVO" property="conAtr.admBusPorRan.value"/>
				</td>
			</tr>
		</table>
	</fieldset>	
	<!-- ConAtr -->

	<table class="tablabotones" width="100%">
    	<tr>
 	    		<td align="left" width="50%">
	   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>	   	    			
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="conAtrAdapterVO" property="act" value="eliminar">
					<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
						<bean:message bundle="base" key="abm.button.eliminar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="conAtrAdapterVO" property="act" value="activar">
					<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
						<bean:message bundle="base" key="abm.button.activar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="conAtrAdapterVO" property="act" value="desactivar">
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