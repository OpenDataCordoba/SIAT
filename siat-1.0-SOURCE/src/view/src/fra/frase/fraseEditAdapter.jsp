<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/fra/AdministrarFrase.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="fra" key="fra.fraseEditAdapter.title"/></h1>	
	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
		
	<!-- Frase -->
	<fieldset>
		<legend><bean:message bundle="fra" key="fra.frase.title"/></legend>
		
		<table class="tabladatos"> 
			<tr>
				<td>
					<label><bean:message bundle="fra" key="fra.frase.modulo.label"/>: </label>
				</td>
				<td class="normal">	
				 	<bean:write name="fraseAdapterVO" property="frase.modulo"/>
				</td>
				<td>
					<label><bean:message bundle="fra" key="fra.frase.pagina.label"/>: </label>
				</td>
				<td class="normal">	
				 	<bean:write name="fraseAdapterVO" property="frase.pagina"/>
				</td>	
			</tr>
			
			<tr>
				<td>
					<label><bean:message bundle="fra" key="fra.frase.etiqueta.label"/>: </label>
				</td>
				<td class="normal">
				 	<bean:write name="fraseAdapterVO" property="frase.etiqueta"/>
				</td>
			</tr>
			<tr>
				<td>
					<label><bean:message bundle="fra" key="fra.frase.valorPublico.label"/>: </label>
				</td>
				<td class="normal"  colspan="3">
				 	<bean:write name="fraseAdapterVO" property="frase.valorPublico"/>
				</td>
			</tr>
			
			<tr>
				<td>
					<label><bean:message bundle="fra" key="fra.frase.desFrase.label"/>: </label>
				</td>
				<td class="normal" colspan="3">
					<html:text name="fraseAdapterVO" property="frase.desFrase" size="80" maxlength="100"/>
				</td>
			</tr>
			
			<tr>
				<td>
					<label><bean:message bundle="fra" key="fra.frase.valorPrivado.label"/>: </label>
				</td>
				<td class="normal" colspan="3">
					<html:textarea name="fraseAdapterVO" property="frase.valorPrivado" rows="2" cols="30"/>
				</td>
			</tr>
			
		</table>
	</fieldset>	
	<!-- Frase -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="fraseAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
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

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->