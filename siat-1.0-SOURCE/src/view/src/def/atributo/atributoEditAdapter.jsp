<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/def/AdministrarAtributo.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="def" key="def.atributoEditAdapter.title"/></h1>	
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
				<logic:equal name="atributoAdapterVO" property="act" value="modificar">
					<td><label><bean:message bundle="def" key="def.atributo.codAtributo.label"/>: </label></td>
					<td class="normal"><bean:write name="atributoAdapterVO" property="atributo.codAtributo"/></td>
				</logic:equal>
				<logic:equal name="atributoAdapterVO" property="act" value="agregar">
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.atributo.codAtributo.label"/>: </label></td>	
					<td class="normal"><html:text name="atributoAdapterVO" property="atributo.codAtributo" size="15" maxlength="20" /></td>
				</logic:equal>					
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.atributo.desAtributo.label"/>: </label></td>
				<td class="normal">
					<html:text name="atributoAdapterVO" property="atributo.desAtributo" size="20" maxlength="100"/>
				</td>
			</tr>
			
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.tipoAtributo.label"/>: </label></td>
				<td class="normal">
					<html:select name="atributoAdapterVO" property="atributo.tipoAtributo.id" styleClass="select" onchange="submitForm('paramTipoAtributo', '');">
						<html:optionsCollection name="atributoAdapterVO" property="listTipoAtributo" label="desTipoAtributo" value="id" />
					</html:select>
				</td>
			</tr>
			<tr>	
				<td><label><bean:message bundle="def" key="def.domAtr.label"/>: </label></td>
				<td class="normal">
					<html:select name="atributoAdapterVO" property="atributo.domAtr.id" styleClass="select">
						<html:optionsCollection name="atributoAdapterVO" property="listDomAtr" label="desDomAtr" value="id" />
					</html:select>
				</td>					
			</tr>
			
			<tr>
				<td><label><bean:message bundle="def" key="def.atributo.mascaraVisual.label"/>: </label></td>
				<td class="normal"><html:text name="atributoAdapterVO" property="atributo.mascaraVisual" size="20" maxlength="100"/></td>			
			</tr>
		</table>
	</fieldset>	
	<!-- Atributo -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="atributoAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="atributoAdapterVO" property="act" value="agregar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
						<bean:message bundle="base" key="abm.button.agregar"/>
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
