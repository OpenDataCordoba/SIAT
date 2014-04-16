<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/pro/AdministrarProceso.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="pro" key="pro.procesoEditAdapter.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- Proceso -->
	<fieldset>
		<legend><bean:message bundle="pro" key="pro.proceso.title"/></legend>
		
		<table class="tabladatos">
		<!-- codProceso -->
		<tr>
				<logic:equal name="procesoAdapterVO" property="act" value="modificar">
					<td><label><bean:message bundle="pro" key="pro.proceso.codProceso.label"/>: </label></td>
					<td class="normal" colspan="3">
						<bean:write name="procesoAdapterVO" property="proceso.codProceso"/>
					</td>	
			 	</logic:equal>
			 	
				<logic:equal name="procesoAdapterVO" property="act" value="agregar">
					<td><label>(*)&nbsp;<bean:message bundle="pro" key="pro.proceso.codProceso.label"/>: </label></td>
					<td class="normal" colspan="3">
						<html:text name="procesoAdapterVO" property="proceso.codProceso" size="60" maxlength="100" />
					</td>	
			 	</logic:equal>
		</tr>
		
		<!-- desProceso -->
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="pro" key="pro.proceso.desProceso.label"/>: </label></td>
			<td class="normal" colspan="3"><html:text name="procesoAdapterVO" property="proceso.desProceso" size="60" maxlength="100"/></td>			
		</tr>
		
		<!-- classForName -->
		<tr>
			<td><label><bean:message bundle="pro" key="pro.proceso.classForName.label"/>: </label></td>
			<td class="normal" colspan="3"><html:text name="procesoAdapterVO" property="proceso.classForName" size="60" maxlength="100"/></td>			
		</tr>
		
		<!-- directorioInput -->	
		<tr>
			<td><label><bean:message bundle="pro" key="pro.proceso.directorioInput.label"/>: </label></td>
			<td class="normal" colspan="3"><html:text name="procesoAdapterVO" property="proceso.directorioInput" size="60" maxlength="100"/></td>			
		</tr>
		
		<!-- cantPasos -->
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="pro" key="pro.proceso.cantPasos.label"/>: </label></td>
			<td class="normal"><html:text name="procesoAdapterVO" property="proceso.cantPasos" size="2" maxlength="2"/></td>			

		<!-- locked -->			
			<td><label><bean:message bundle="pro" key="pro.proceso.locked.label"/>: </label></td>
			<td class="normal">
				<html:select name="procesoAdapterVO" property="proceso.locked.id" styleClass="select">
					<html:optionsCollection name="procesoAdapterVO" property="listSiNo" label="value" value="id" />
				</html:select>
			</td>					
		
		</tr>
		
		<!-- ejecNodo -->
		<tr>
			<td><label><bean:message bundle="pro" key="pro.proceso.ejecNodo.label"/>: </label></td>
			<td class="normal"><html:text name="procesoAdapterVO" property="proceso.ejecNodo" size="20" maxlength="100"/></td>			
		
		<!-- esAsincronico -->
			<td><label>(*)&nbsp;<bean:message bundle="pro" key="pro.proceso.esAsincronico.label"/>: </label></td>
			<td class="normal">
				<html:select name="procesoAdapterVO" property="proceso.esAsincronico.id" styleClass="select">
					<html:optionsCollection name="procesoAdapterVO" property="listSiNo" label="value" value="id" />
				</html:select>
			</td>					
		</tr>
		
		<!-- TipoEjecucion -->
		<tr>	
			<td><label>(*)&nbsp;<bean:message bundle="pro" key="pro.proceso.tipoEjecucion.label"/>: </label></td>
			<td class="normal" colspan="3">
				<html:select name="procesoAdapterVO" property="proceso.tipoEjecucion.id" styleClass="select" onchange="submitForm('paramTipoEjecucion', '');">
					<html:optionsCollection name="procesoAdapterVO" property="listTipoEjecucion" label="desTipoEjecucion" value="id" />
				</html:select>
			</td>					
		</tr>
		<logic:equal name="procesoAdapterVO" property="paramPeriodic" value="true">
		<!-- CronExpression -->
			<tr>
				<td><label>&nbsp;<bean:message bundle="pro" key="pro.proceso.cronExpression.label"/>: </label></td>
				<td class="normal" colspan="3"><html:text name="procesoAdapterVO" property="proceso.cronExpression" size="40" maxlength="100"/></td>		
			</tr>		
		</logic:equal>	
		<!-- TipoProgEjec -->
		<tr>	
			<td><label>(*)&nbsp;<bean:message bundle="pro" key="pro.proceso.tipoProgEjec.label"/>: </label></td>
			<td class="normal" colspan="3">
				<html:select name="procesoAdapterVO" property="proceso.tipoProgEjec.id" styleClass="select">
					<html:optionsCollection name="procesoAdapterVO" property="listTipoProgEjec" label="desTipoProgEjec" value="id" />
				</html:select>
			</td>					
		</tr>
		
		</table>
	</fieldset>	
	<!-- Proceso -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="procesoAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="procesoAdapterVO" property="act" value="agregar">
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
