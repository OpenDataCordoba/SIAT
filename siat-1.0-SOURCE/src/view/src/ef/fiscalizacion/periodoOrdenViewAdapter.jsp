<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/AdministrarPeriodoOrden.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="ef" key="ef.periodoOrdenViewAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- PeriodoOrden -->
	<fieldset>
		<legend><bean:message bundle="ef" key="ef.periodoOrden.title"/></legend>
		
		<table class="tabladatos">
		
			<!-- recurso -->					
			<tr>
				<td><label><bean:message bundle="def" key="def.recurso.label"/></label></td>
				<td class="normal" align="left">
					<bean:write name="periodoOrdenAdapterVO" property="periodoOrden.ordConCue.cuenta.recurso.desRecurso"/>									
				</td>
			</tr>
			
			<!-- cuenta -->
			<tr>
				<td><label><bean:message bundle="pad" key="pad.cuenta.label"/></label></td>
				<td class="normal" align="left">
					<bean:write name="periodoOrdenAdapterVO" property="periodoOrden.ordConCue.cuenta.numeroCuenta"/>									
				</td>
			</tr>						
			
			<!-- periodo -->
			<tr>
				<td><LABEL><bean:message bundle="ef" key="ef.periodoOrden.periodo.label"/></LABEL></td>
				<td class="normal">
					<bean:write name="periodoOrdenAdapterVO" property="periodoOrden.periodoView"/>
				</td>		
			</tr>
			
			<!-- anio -->
			<tr>
				<td><LABEL><bean:message bundle="ef" key="ef.periodoOrden.anio.label"/></LABEL></td>
				<td class="normal">
					<bean:write name="periodoOrdenAdapterVO" property="periodoOrden.anioView"/>
				</td>		
			</tr>			
								
			<!-- <#Campos#> -->
		</table>				
	</fieldset>	
	<!-- PeriodoOrden -->
	
	
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="periodoOrdenAdapterVO" property="agregarBussEnabled" value="true">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('eliminar', '');">
						<bean:message bundle="base" key="abm.button.eliminar"/>
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
<!-- periodoOrdenViewAdapter.jsp -->