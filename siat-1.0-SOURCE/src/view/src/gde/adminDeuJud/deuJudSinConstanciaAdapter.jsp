<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/gde/AdministrarDeuJudSinConstancia.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="gde" key="gde.deuJudSinConstanciaAdapter.title"/></h1>	
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		<!-- DeuJudSinConstancia -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.deuJudSinConstancia.title"/></legend>
			
			<table class="tabladatos">
				<!-- Procurador -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.deuJudSinConstancia.procurador.label"/>: </label></td>
					<td class="normal"><bean:write name="deuJudSinConstanciaAdapterVO" property="deuda.procurador.descripcion"/></td>
				</tr>
				
				<!-- Cuenta -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.deuJudSinConstancia.cuenta.label"/>: </label></td>
					<td class="normal"><bean:write name="deuJudSinConstanciaAdapterVO" property="deuda.cuenta.numeroCuenta"/></td>

				
				<!-- Recurso -->
					<td><label><bean:message bundle="gde" key="gde.deuJudSinConstancia.cuenta.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="deuJudSinConstanciaAdapterVO" property="deuda.cuenta.recurso.desRecurso"/></td>
				</tr>
								
			</table>
		</fieldset>	
		<!-- DeuJudSinConstancia -->
		
		<!-- ConstanciaDeu -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.deuJudSinConstancia.constanciaDeu.title"/></legend>
				<table class="tabladatos">
					<!-- Nro. Constancia -->
					<tr>
						<td><label><bean:message bundle="gde" key="gde.deuJudSinConstancia.constanciaDeu.numero.label"/>: </label></td>
						<td class="normal"><bean:write name="deuJudSinConstanciaAdapterVO" property="constanciaDeu.numeroView"/></td>

					<!-- Anio -->
						<td><label><bean:message bundle="gde" key="gde.deuJudSinConstancia.constanciaDeu.anio.label"/>: </label></td>
						<td class="normal"><bean:write name="deuJudSinConstanciaAdapterVO" property="constanciaDeu.anioView"/></td>
					</tr>
					<tr>
						<td align="right" colspan="10">
							<button type="button" onclick="submitForm('buscarConstancia', '');">
	    						<bean:message bundle="gde" key="gde.deuJudSinConstancia.button.buscarConstancia"/>
				      		</button>
						</td>				
					</tr>
			</table>
		</fieldset>	
		<!--ConstanciaDeu -->
		
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left" width="50%">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>   	    			
			</tr>
			<tr>
	   	    	<td align="right" width="50%">
					<logic:equal name="deuJudSinConstanciaAdapterVO" property="act" value="modificar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('agregar', '');">
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
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
