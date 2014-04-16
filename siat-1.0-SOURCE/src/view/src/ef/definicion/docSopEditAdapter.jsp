<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/AdministrarDocSop.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="ef" key="ef.docSopEditAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- DocSop -->
	<fieldset>
		<legend><bean:message bundle="ef" key="ef.docSop.title"/></legend>
		
		<table class="tabladatos">
		<!-- desDocSop -->
		<tr>
			<td><label><bean:message bundle="ef" key="ef.docSop.desDocSop.label"/>: </label></td>
			<td class="normal"><html:textarea name="docSopAdapterVO" property="docSop.desDocSop" cols="80" rows="15"/></td>			
		</tr>
		<!-- determinaAjuste -->
		<tr>	
			<td><label><bean:message bundle="ef" key="ef.docSop.determinaAjuste.label"/>: </label></td>
			<td class="normal">
				<html:select name="docSopAdapterVO" property="docSop.determinaAjuste.id" styleClass="select">
					<html:optionsCollection name="docSopAdapterVO" property="listSiNo" label="value" value="id" />
				</html:select>
			</td>					
		</tr>
		
		<!-- aplicaMulta -->
		<tr>	
			<td><label><bean:message bundle="ef" key="ef.docSop.aplicaMulta.label"/>: </label></td>
			<td class="normal">
				<html:select name="docSopAdapterVO" property="docSop.aplicaMulta.id" styleClass="select">
					<html:optionsCollection name="docSopAdapterVO" property="listSiNo" label="value" value="id" />
				</html:select>
			</td>					
		</tr>
		<!-- compensaSalAFav -->
		<tr>	
			<td><label><bean:message bundle="ef" key="ef.docSop.compensaSalAFav.label"/>: </label></td>
			<td class="normal">
				<html:select name="docSopAdapterVO" property="docSop.compensaSalAFav.id" styleClass="select">
					<html:optionsCollection name="docSopAdapterVO" property="listSiNo" label="value" value="id" />
				</html:select>
			</td>					
		</tr>
		<!-- devuelveSalAFav -->
		<tr>	
			<td><label><bean:message bundle="ef" key="ef.docSop.devuelveSalAFav.label"/>: </label></td>
			<td class="normal">
				<html:select name="docSopAdapterVO" property="docSop.devuelveSalAFav.id" styleClass="select">
					<html:optionsCollection name="docSopAdapterVO" property="listSiNo" label="value" value="id" />
				</html:select>
			</td>					
		</tr>
		<!-- plantilla -->
		<tr>
			<td><label><bean:message bundle="ef" key="ef.docSop.plantilla.label"/>: </label></td>
			<td class="normal"><html:textarea name="docSopAdapterVO" property="docSop.plantilla" cols="80" rows="15"/></td>			
		</tr>
			<!-- <#Campos#> -->
		</table>
	</fieldset>	
	<!-- DocSop -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="docSopAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="docSopAdapterVO" property="act" value="agregar">
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

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
