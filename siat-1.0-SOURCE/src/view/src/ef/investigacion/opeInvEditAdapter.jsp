<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/AdministrarOpeInv.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="ef" key="ef.opeInvEditAdapter.title"/></h1>	
	
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
	<fieldset>
		<legend><bean:message bundle="ef" key="ef.opeInv.title"/></legend>
		
		<table class="tabladatos">
		<!-- PlanFiscal -->
		<tr>
			<logic:equal name="opeInvAdapterVO" property="act" value="agregar">	
				<td><label>(*)&nbsp;<bean:message bundle="ef" key="ef.planFiscal.label"/>: </label></td>
				<td class="normal">
					<html:select name="opeInvAdapterVO" property="opeInv.planFiscal.id" styleClass="select">
						<html:optionsCollection name="opeInvAdapterVO" property="listPlanFiscal" label="desPlanFiscal" value="id" />
					</html:select>
				</td>
			</logic:equal>
			<logic:equal name="opeInvAdapterVO" property="act" value="modificar">			
				<td><label><bean:message bundle="ef" key="ef.planFiscal.label"/>: </label></td>
				<td class="normal"><bean:write name="opeInvAdapterVO" property="opeInv.planFiscal.desPlanFiscal"/></td>
			</logic:equal>					
		</tr>
		
		<!-- fechaInicio -->
		<tr>
			<logic:equal name="opeInvAdapterVO" property="act" value="agregar">		
				<td><label>(*)&nbsp;<bean:message bundle="ef" key="ef.opeInv.fechaInicio.label"/>: </label></td>
				<td class="normal">
					<html:text name="opeInvAdapterVO" property="opeInv.fechaInicioView" styleId="fechaInicioView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaInicioView');" id="a_fechaInicioView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</logic:equal>
			<logic:equal name="opeInvAdapterVO" property="act" value="modificar">
				<td><label><bean:message bundle="ef" key="ef.opeInv.fechaInicio.label"/>: </label></td>
				<td class="normal"><bean:write name="opeInvAdapterVO" property="opeInv.fechaInicioView"/></td>			
			</logic:equal>	
		</tr>
		<!-- desOpeInv -->
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="ef" key="ef.opeInv.desOpeInv.label"/>: </label></td>
			<td class="normal"><html:text name="opeInvAdapterVO" property="opeInv.desOpeInv" size="40" maxlength="100"/></td>			
		</tr>
		<!-- observacion -->
		<tr>
			<td><label><bean:message bundle="ef" key="ef.opeInv.observacion.label"/>: </label></td>
			<td class="normal"><html:textarea name="opeInvAdapterVO" property="opeInv.observacion" style="height:35px;width:300px"/></td>			
		</tr>
			<!-- <#Campos#> -->
		</table>
	</fieldset>	
	<!-- OpeInv -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="opeInvAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="opeInvAdapterVO" property="act" value="agregar">
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
