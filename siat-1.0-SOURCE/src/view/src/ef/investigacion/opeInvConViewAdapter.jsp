<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/AdministrarOpeInvCon.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="ef" key="ef.opeInvConEditAdapter.title"/></h1>	
	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
		
	<!-- OpeInv -->
	<bean:define id="opeInvVO" name="opeInvConAdapterVO" property="opeInvCon.opeInv"/>
	<%@include file="/ef/investigacion/includeOpeInvView.jsp" %>	
	<!-- OpeInv -->
	
	<!-- OpeInvCon -->
	<fieldset>
		<legend><bean:message bundle="ef" key="ef.opeInvConAdapter.contribuyente.label"/></legend>
		
		<table class="tabladatos">
		<!-- Persona -->
		<tr>
			<td><label><bean:message bundle="pad" key="pad.persona.label"/>: </label></td>
			<td class="normal" colspan="3">
				<bean:write name="opeInvConAdapterVO" property="opeInvCon.contribuyente.persona.represent"/>
			</td>
		</tr>
		<tr>
			<td><label><bean:message bundle="ef" key="ef.opeInvConAdapter.nroIsib.label"/>: </td>
			<td class="normal"><bean:write name="opeInvConAdapterVO" property="opeInvCon.contribuyente.nroIsib"/></td>
		</tr>
		<tr>
			<td><label><bean:message bundle="ef" key="ef.estadoOpeInvCon.label"/>: </label></td>
			<td class="normal" colspan="3">
				<bean:write name="opeInvConAdapterVO" property="opeInvCon.estadoOpeInvCon.desEstadoOpeInvCon"/>
			</td>
		</tr>		
			<!-- <#Campos#> -->
		</table>
	</fieldset>	
	
	
	<!-- OpeInvCon datos  -->
	<fieldset>
		<legend><bean:message bundle="ef" key="ef.opeInvCon.title"/></legend>
		
		<table class="tabladatos">
			<bean:define id="personaVO" name="opeInvConAdapterVO" property="opeInvCon.contribuyente.persona"/>
			<%@ include file="/pad/persona/includePersona.jsp"%>
		</table>		
	</fieldset>
	
	<!-- OpeInvConCue -->
	<fieldset>
		<legend><bean:message bundle="ef" key="ef.opeInvConCue.legend"/></legend>
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
			<th><bean:message bundle="pad" key="pad.cuenta.label"/></th>
			<th><bean:message bundle="pad" key="pad.cuenta.recurso.label"/></th>
			<th><bean:message bundle="ef" key="ef.opeInvConCue.seleccionada.label"/></th>
			<logic:notEmpty name="opeInvConAdapterVO" property="opeInvCon.listOpeInvConCue">
				<logic:iterate id="opeInvConCue" name="opeInvConAdapterVO" property="opeInvCon.listOpeInvConCue">
					<tr>
						<td><bean:write name="opeInvConCue" property="nroCuenta"/></td>
						<td><bean:write name="opeInvConCue" property="desRecurso"/></td>
						<td><bean:write name="opeInvConCue" property="seleccionadaStr"/></td>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
			<logic:empty name="opeInvConAdapterVO" property="opeInvCon.listOpeInvConCue">
				<tr>
					<td colspan="3" align="center"><bean:message bundle="ef" key="ef.opeInvConAdapter.listOpeInvConCueVacio"/></td>
				</tr>
			</logic:empty>
		</table>
	</fieldset>
	
	<!-- historico estados -->
	<fieldset>
		<legend><bean:message bundle="ef" key="ef.hisEstOpeInvCon.legend"/></legend>
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
			<th><bean:message bundle="ef" key="ef.hisEstOpeInvCon.fecha.label"/></th>
			<th><bean:message bundle="ef" key="ef.hisEstOpeInvCon.estado.label"/></th>
			<th><bean:message bundle="ef" key="ef.hisEstOpeInvCon.obs.label"/></th>
			<th><bean:message bundle="ef" key="ef.hisEstOpeInvCon.usuario.label"/></th>
			<logic:iterate id="hisEstOpeInvConVO" name="opeInvConAdapterVO" property="opeInvCon.listHisEstOpeInvCon">
				<tr>
					<td><bean:write name="hisEstOpeInvConVO" property="fechaEstadoView"/></td>
					<td><bean:write name="hisEstOpeInvConVO" property="desEstado"/></td>
					<td><bean:write name="hisEstOpeInvConVO" property="observaciones"/></td>
					<td><bean:write name="hisEstOpeInvConVO" property="usuario"/></td>
				</tr>
			</logic:iterate>
		</table>
	</fieldset>

	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<logic:equal name="opeInvConAdapterVO" property="act" value="excluirDeSelec">
  	   		<td align="right" width="50%">
	   	    	<html:button property="btnExcluir" styleClass="boton" onclick="submitForm('excluirDeSelec', '');">
	   	    		<bean:message bundle="ef" key="ef.opeInvConAdapter.button.excluir"/>
	   	    	</html:button>
   	    	</td>   	    	
   	    	</logic:equal>
   	    </tr>
   	</table>
   	
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
