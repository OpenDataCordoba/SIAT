<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>
   	    <%@include file="/base/calendar.js"%>
</script>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/pad/AdministrarObjImpAtrVal.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="pad" key="pad.objImpAtrValEditAdapter.title"/></h1>	
	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
		
	<!-- ObjImpAtlVal -->
	<fieldset>
		<legend><bean:message bundle="pad" key="pad.objImp.title"/></legend>
		
		<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="def" key="def.tipObjImp.label"/>: </label></td>
				<td class="normal">
					<bean:write name="objImpAtrValAdapterVO" property="objImp.tipObjImp.desTipObjImp"/>
				</td>
			</tr>
			
			<!-- Fecha Alta-->
			<tr>
				<td><label><bean:message bundle="pad" key="pad.objImp.fechaAlta.label"/>: </label></td>
				<td class="normal">
					<bean:write name="objImpAtrValAdapterVO" property="objImp.fechaAltaView"/>
				</td>
			</tr>			
		</table>
	</fieldset>	
	<!-- ObjImpAtlVal -->
	
	<fieldset>
		<legend><bean:message bundle="pad" key="pad.objImpAtrValEditAdapter.atributosObjImp"/></legend>
		<table class="tabladatos">
			
			<bean:define id="AtrVal" name="objImpAtrValAdapterVO" property="tipObjImpAtrDefinition"/>
			
			<%@ include file="/def/atrDefinition4ViewWithNovedad.jsp" %>

		</table>
	</fieldset>
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
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

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
