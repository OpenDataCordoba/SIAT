<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/gde/AdministrarCtrlInfDeu.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="gde" key="gde.ctrlInfDeuAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>		
				<td align="left">
					<p><bean:message bundle="gde" key="gde.ctrlInfDeuAdapter.legend"/></p>		
				</td>	
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
			
		<!-- Buscar CtrlInfDeu -->
		<fieldset>
			<legend><bean:message bundle="base" key="base.parametrosBusqueda"/></legend>			
			<table class="tabladatos">
				<tr>
					<td><label>&nbsp;<bean:message bundle="gde" key="gde.tramite.codRefPag.label"/>: </label></td>
					<td class="normal"><html:text name="ctrlInfDeuAdapterVO" property="codigo" size="15" maxlength="100" /></td>
				</tr>
			</table>
			<table class="tablabotones" width="100%">
			<tr>				
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('buscar', '');">
						<bean:message bundle="base" key="abm.button.buscar"/>
					</html:button>
				</td>
			</tr>
		</table>
		</fieldset>
		<!-- Fin Buscar CtrlInfDeu -->
	
		<logic:equal name="ctrlInfDeuAdapterVO" property="paramEncontrado" value="true">
			<!-- Datos CtrlInfDeu -->
			<fieldset>
				<legend><bean:message bundle="gde" key="gde.ctrlInfDeu.title"/></legend>			
				<table class="tabladatos" width="100%">
					<tr>
						<!-- Numero Cuenta -->		
						<td><label>&nbsp;<bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
						<td class="normal"><bean:write name="ctrlInfDeuAdapterVO" property="ctrlInfDeu.cuenta.numeroCuenta"/></td>
					</tr>
					<tr>
						<!-- NroLiquidacion -->		
						<td><label>&nbsp;<bean:message bundle="gde" key="gde.ctrlInfDeu.nroLiquidacion.label"/>: </label></td>
						<td class="normal"><bean:write name="ctrlInfDeuAdapterVO" property="ctrlInfDeu.nroLiquidacion"/></td>
					</tr>	
					<tr>
						<!-- Usuario -->		
						<td><label>&nbsp;<bean:message bundle="gde" key="gde.ctrlInfDeu.usuario.label"/>: </label></td>
						<td class="normal"><bean:write name="ctrlInfDeuAdapterVO" property="ctrlInfDeu.usuario"/></td>
					</tr>
					<tr>
						<!-- Fecha y Hora Generacion -->		
						<td><label>&nbsp;<bean:message bundle="gde" key="gde.ctrlInfDeu.fechaHoraGen.label"/>: </label></td>
						<td class="normal"><bean:write name="ctrlInfDeuAdapterVO" property="ctrlInfDeu.fechaHoraGenView"/></td>
					</tr>
				</table>
			</fieldset>
			<!-- Fin Datos CtrlInfDeu -->
		</logic:equal>
		
		<logic:equal name="ctrlInfDeuAdapterVO" property="paramNoEncontrado" value="true">
				<table width="100%">
					<tr>
						<td class="normal" colspan="6">
							<ul class="error" id="errorsSearch">
							       <bean:message bundle="gde" key="gde.ctrlInfDeuAdapter.noEncontrado.label"/><br/>
							</ul>
						</td>
					</tr>
				</table>
		</logic:equal>			
		
		<table class="tablabotones">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
	   	    	<td  width="100%" align="right">
					<logic:equal name="ctrlInfDeuAdapterVO" property="paramEncontrado" value="true">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="gde" key="gde.ctrlInfDeuAdapter.button.desbloquear"/>
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
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->		