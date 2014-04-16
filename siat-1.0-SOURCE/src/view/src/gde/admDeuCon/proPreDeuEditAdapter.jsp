<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarProPreDeu.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="gde" key="gde.proPreDeuEditAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- ProPreDeu -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.proPreDeu.title"/></legend>
		
		<table class="tabladatos">
			<!-- Via Deuda-->
			<logic:equal name="proPreDeuAdapterVO" property="proPreDeu.modificarVia" value="true">
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.proPreDeu.viaDeuda.label"/>: </label></td>
				<td class="normal">
					<html:select name="proPreDeuAdapterVO" property="proPreDeu.viaDeuda.id" styleClass="select">
						<html:optionsCollection name="proPreDeuAdapterVO" property="listViaDeuda" label="desViaDeuda" value="id" />
					</html:select>
				</td>					
			</tr>
			</logic:equal>
			<logic:notEqual name="proPreDeuAdapterVO" property="proPreDeu.modificarVia" value="true">
					<td><label><bean:message bundle="gde" key="gde.proPreDeu.viaDeuda.label"/>: </label></td>
					<td class="normal"><bean:write name="proPreDeuAdapterVO" property="proPreDeu.viaDeuda.desViaDeuda"/></td>
			</logic:notEqual>
			
			<!-- Servicio Banco-->
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.proPreDeu.servicioBanco.label"/>: </label></td>
				<td class="normal">
					<html:select name="proPreDeuAdapterVO" property="proPreDeu.servicioBanco.id" styleClass="select">
						<html:optionsCollection name="proPreDeuAdapterVO" property="listServicioBanco" label="desServicioBanco" value="id" />
					</html:select>
				</td>					
			</tr>

			<!-- Fecha Tope-->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.proPreDeu.fechaTope.label"/>: </label></td>
				<td class="normal">
					<html:text name="proPreDeuAdapterVO" property="proPreDeu.fechaTopeView" styleId="fechaTopeView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaTopeView');" id="a_fechaTopeView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
		</table>
	</fieldset>	
	<!-- ProPreDeu -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="proPreDeuAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="proPreDeuAdapterVO" property="act" value="agregar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
						<bean:message bundle="base" key="abm.button.agregar"/>
					</html:button>
				</logic:equal>
   	    	</td>   	    	
   	    </tr>
   	</table>
	<input type="text"   style="display:none"/>
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->