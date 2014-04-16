<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/def/AdministrarColEmiMat.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="def" key="def.colEmiMatEditAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>

	<!-- EmiMat -->
	<fieldset>
		<legend><bean:message bundle="def" key="def.emiMat.title"/></legend>
		
		<table class="tabladatos">
			<tr>	
				<td><label><bean:message bundle="def" key="def.emiMat.recurso.label"/>: </label></td>
				<td class="normal"><bean:write name="colEmiMatAdapterVO" property="colEmiMat.emiMat.recurso.desRecurso" /></td>
			</tr>

			<tr>
				<td><label><bean:message bundle="def" key="def.emiMat.codEmiMat.label"/>: </label></td>
				<td class="normal"><bean:write name="colEmiMatAdapterVO" property="colEmiMat.emiMat.codEmiMat"/></td>
			</tr>
		</table>
	</fieldset>	
	
	<!-- ColEmiMat -->
	<fieldset>
		<legend><bean:message bundle="def" key="def.colEmiMat.title"/></legend>
		
		<table class="tabladatos">
			<!-- Codigo -->
			<tr>
				<logic:equal name="colEmiMatAdapterVO" property="act" value="modificar">
					<td><label><bean:message bundle="def" key="def.colEmiMat.codColumna.label"/>: </label></td>
					<td class="normal"><bean:write name="colEmiMatAdapterVO" property="colEmiMat.codColumna"/></td>
			 	</logic:equal>
				<logic:equal name="colEmiMatAdapterVO" property="act" value="agregar">
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.colEmiMat.codColumna.label"/>: </label></td>
					<td class="normal"><html:text name="colEmiMatAdapterVO" property="colEmiMat.codColumna" size="20" maxlength="20"/></td>
			 	</logic:equal>	
			</tr>

			<!-- Tipo Dato -->
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.colEmiMat.tipoDato.label"/>: </label></td>
				<td class="normal">
					<html:select name="colEmiMatAdapterVO" property="colEmiMat.tipoDato.id" styleClass="select">
						<html:optionsCollection name="colEmiMatAdapterVO" property="listEmiMatTipoDato" label="value" value="id" />
					</html:select>
				</td>					
			</tr>

			<!-- Tipo Columna -->
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.colEmiMat.tipoColumna.label"/>: </label></td>
				<td class="normal">
					<html:select name="colEmiMatAdapterVO" property="colEmiMat.tipoColumna.id" styleClass="select">
						<html:optionsCollection name="colEmiMatAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>					
			</tr>

			<!-- Orden -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.colEmiMat.orden.label"/>: </label></td>
				<td class="normal"><html:text name="colEmiMatAdapterVO" property="colEmiMat.ordenView" size="4" maxlength="4"/></td>			
			</tr>


		</table>
	</fieldset>	
	<!-- ColEmiMat -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="colEmiMatAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="colEmiMatAdapterVO" property="act" value="agregar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
						<bean:message bundle="base" key="abm.button.agregar"/>
					</html:button>
				</logic:equal>
   	    	</td>   	    	
   	    </tr>
   	</table>
	
	<input type="text" style="display:none"/>
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
<!-- colEmiMatEditAdapter.jsp -->