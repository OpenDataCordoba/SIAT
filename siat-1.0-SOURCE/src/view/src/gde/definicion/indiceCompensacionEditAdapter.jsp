<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formulariº1qos -->
<html:form styleId="filter" action="/gde/AdministrarIndiceCompensacion.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="gde" key="gde.indiceCompensacionEditAdapter.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- IndiceCompensacion -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.indiceCompensacion.title"/></legend>
		
		<table class="tabladatos">
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.indiceCompensacion.indice.label"/>: </label></td>
				<td class="normal">
					<html:text name="indiceCompensacionAdapterVO" property="indiceCompensacion.indiceView" size="20" />
				</td>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.indiceCompensacion.periodoDesde.label"/>: </label></td>
				<td class="normal">

					<html:text name="indiceCompensacionAdapterVO" property="indiceCompensacion.periodoMesDesdeView" size="2" maxlength="2" />
					&nbsp;/&nbsp;
					<html:text name="indiceCompensacionAdapterVO" property="indiceCompensacion.periodoAnioDesdeView" size="4" maxlength="4" />

				</td>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.indiceCompensacion.periodoHasta.label"/>: </label></td>
				<td class="normal">
					<html:text name="indiceCompensacionAdapterVO" property="indiceCompensacion.periodoMesHastaView" size="2" maxlength="2" />
					&nbsp;/&nbsp;
					<html:text name="indiceCompensacionAdapterVO" property="indiceCompensacion.periodoAnioHastaView" size="4" maxlength="4" />
					
				</td>
			</tr>
		</table>
	</fieldset>	
	<!-- IndiceCompensacion -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="indiceCompensacionAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="indiceCompensacionAdapterVO" property="act" value="agregar">
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
