<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/def/AdministrarEncDomAtr.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="def" key="def.domAtrAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- DomAtrVal -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.domAtr.title"/></legend>
			
			<table class="tabladatos">
				<tr>
					<!-- Codigo -->
					<logic:equal name="encDomAtrAdapterVO" property="act" value="agregar">					
						<td><label>(*)&nbsp;<bean:message bundle="def" key="def.domAtr.codDomAtr.label"/>:</label></td>
						<td class="normal"><html:text name="encDomAtrAdapterVO" property="domAtr.codDomAtr" size="15" maxlength="10" styleClass="datos"/></td>
					</logic:equal>
					<logic:equal name="encDomAtrAdapterVO" property="act" value="modificar">
						<td><label><bean:message bundle="def" key="def.domAtr.codDomAtr.label"/>:</label></td>					
						<td class="normal"><bean:write name="encDomAtrAdapterVO" property="domAtr.codDomAtr"/></td>
					</logic:equal>
					
					<!-- Tipo Atributo -->										
					<logic:equal name="encDomAtrAdapterVO" property="act" value="agregar">
						<td><label>(*)&nbsp;<bean:message bundle="def" key="def.tipoAtributo.label"/>:</label></td>
						<td class="normal">
							<html:select name="encDomAtrAdapterVO" property="domAtr.tipoAtributo.id" styleClass="select">
								<html:optionsCollection name="encDomAtrAdapterVO" property="listTipoAtributo" label="desTipoAtributo" value="id" />
							</html:select>
						</td>
					</logic:equal>
					<logic:equal name="encDomAtrAdapterVO" property="act" value="modificar">
						<td><label><bean:message bundle="def" key="def.tipoAtributo.label"/>:</label></td>					
						<td class="normal"><bean:write name="encDomAtrAdapterVO" property="domAtr.tipoAtributo.desTipoAtributo"/></td>
					</logic:equal>
				</tr>
				<tr>
					<!-- Descripcion -->
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.domAtr.desDomAtr.label"/>: </label></td>
					<td class="normal"><html:text name="encDomAtrAdapterVO" property="domAtr.desDomAtr" size="20" maxlength="100" styleClass="datos"/></td>					
					<!-- Class for name -->
					<td><label><bean:message bundle="def" key="def.domAtr.classForName.label"/>: </label></td>
					<td class="normal"><html:text name="encDomAtrAdapterVO" property="domAtr.classForName" size="20" maxlength="255" styleClass="datos"/></td>					
				</tr>
			</table>
		</fieldset>
		
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<td align="right">
					<logic:equal name="encDomAtrAdapterVO" property="act" value="modificar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="encDomAtrAdapterVO" property="act" value="agregar">
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
