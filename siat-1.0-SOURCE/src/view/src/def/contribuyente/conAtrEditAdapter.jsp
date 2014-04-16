<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/def/AdministrarConAtr.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="def" key="def.conAtrEditAdapter.title"/></h1>	
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
			<logic:equal name="conAtrAdapterVO" property="act" value="agregar">
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.atributo.codAtributo.label"/>: </label></td>
					<td class="normal"><bean:write name="conAtrAdapterVO" property="conAtr.atributo.codAtributo"/></td>
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.atributo.desAtributo.label"/>: </label></td>
					<td class="normal">
						<bean:write name="conAtrAdapterVO" property="conAtr.atributo.desAtributo"/>&nbsp;&nbsp;
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('buscarAtributo', '');">
							<bean:message bundle="def" key="def.conAtrAdapter.adm.button.buscarAtributo"/>
						</html:button>
					</td>
				</tr>
			</logic:equal>
			<logic:equal name="conAtrAdapterVO" property="act" value="modificar">
				<tr>
					<td><label><bean:message bundle="def" key="def.atributo.codAtributo.label"/>: </label></td>
					<td class="normal"><bean:write name="conAtrAdapterVO" property="conAtr.atributo.codAtributo"/></td>
					<td><label><bean:message bundle="def" key="def.atributo.desAtributo.label"/>: </label></td>
					<td class="normal"><bean:write name="conAtrAdapterVO" property="conAtr.atributo.desAtributo"/></td>
				</tr>
			</logic:equal>
		</table>
	</fieldset>	
	<!-- Atributo -->

	<!-- ConAtr -->
	<fieldset>
		<legend><bean:message bundle="def" key="def.conAtr.caracteristicasGenerales.title"/>&nbsp;(<bean:message bundle="def" key="def.conAtr.valorDefecto.label"/>)</legend>
		<table class="tabladatos">
			<tr>
					<bean:define id="AtrVal" name="conAtrAdapterVO" property="genericAtrDefinition"/>
					<%@ include file="/def/atrDefinition4Edit.jsp" %>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.conAtr.esAtrSegmentacion.label"/>: </label></td>
				<td class="normal" colspan="">
					<html:select name="conAtrAdapterVO" property="conAtr.esAtrSegmentacion.id" styleClass="select" >
						<html:optionsCollection name="conAtrAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>
			</tr>
		</table>
	</fieldset>	
	<fieldset>
		<legend><bean:message bundle="def" key="def.conAtr.caracteristicasVisBusq.title"/></legend>
		<table class="tabladatos">
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.conAtr.esVisConDeu.label"/>: </label></td>
				<td class="normal">
					<html:select name="conAtrAdapterVO" property="conAtr.esVisConDeu.id" styleClass="select" >
						<html:optionsCollection name="conAtrAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>
				<td class="normal" colspan="2"><ul class="vinieta"><li>
					<bean:message bundle="def" key="def.conAtr.esVisConDeu.description"/>
				</li></ul></td>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.conAtr.esAtributoBus.label"/>: </label></td>
				<td class="normal">
					<html:select name="conAtrAdapterVO" property="conAtr.esAtributoBus.id" styleClass="select" >
						<html:optionsCollection name="conAtrAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>
				<td class="normal" colspan="2"><ul class="vinieta"><li>
					<bean:message bundle="def" key="def.conAtr.esAtributoBus.description"/>
				</li></ul></td>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.conAtr.admBusPorRan.label"/>: </label></td>
				<td class="normal">
					<html:select name="conAtrAdapterVO" property="conAtr.admBusPorRan.id" styleClass="select" >
						<html:optionsCollection name="conAtrAdapterVO" property="listSiNo" label="value" value="id"/>
					</html:select>
				</td>
				<td class="normal" colspan="2"><ul class="vinieta"><li>
					<bean:message bundle="def" key="def.conAtr.admBusPorRan.description"/>
				</li></ul></td>
			</tr>
		</table>
	</fieldset>	
	<!-- ConAtr -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="conAtrAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="conAtrAdapterVO" property="act" value="agregar">
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

	<!-- Inclucion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
	
	
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->