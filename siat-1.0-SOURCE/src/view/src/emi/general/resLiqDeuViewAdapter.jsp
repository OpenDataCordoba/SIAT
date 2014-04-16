<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/emi/AdministrarResLiqDeu.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="emi" key="emi.resLiqDeuViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- ResLiqDeu -->
		<fieldset>
			<legend><bean:message bundle="emi" key="emi.resLiqDeu.title"/></legend>
			<table class="tabladatos">
				<tr>
					<!-- Recurso -->
					<td><label><bean:message bundle="emi" key="emi.resLiqDeu.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="resLiqDeuAdapterVO" property="resLiqDeu.recurso.desRecurso"/></td>
				</tr>
				<tr>
					<!-- Fecha de Analisis -->
					<td><label><bean:message bundle="emi" key="emi.resLiqDeu.fechaAnalisis.label"/>: </label></td>
					<td class="normal"><bean:write name="resLiqDeuAdapterVO" property="resLiqDeu.fechaAnalisisView"/></td>
				</tr>
				<logic:notEqual name="resLiqDeuAdapterVO" property="resLiqDeu.esAlfaxView" value="true">
					<tr>
						<!-- Periodo Desde -->
						<td><label><bean:message bundle="emi" key="emi.resLiqDeu.periodoDesde.label"/>: </label></td>
						<td class="normal"><bean:write name="resLiqDeuAdapterVO" property="resLiqDeu.periodoDesdeView"/>
							/<bean:write name="resLiqDeuAdapterVO" property="resLiqDeu.anioView"/>
						</td>
	
						<!-- Periodo Hasta -->
						<td><label><bean:message bundle="emi" key="emi.resLiqDeu.periodoHasta.label"/>: </label></td>
						<td class="normal"><bean:write name="resLiqDeuAdapterVO" property="resLiqDeu.periodoHastaView"/>
							/<bean:write name="resLiqDeuAdapterVO" property="resLiqDeu.anioView"/>
						</td>
					</tr>
				</logic:notEqual>
				<tr>
					<!-- Estado del Proceso-->
					<td><label><bean:message bundle="emi" key="emi.resLiqDeu.estadoCorrida.label"/>: </label></td>
					<td class="normal"><bean:write name="resLiqDeuAdapterVO" property="resLiqDeu.corrida.estadoCorrida.desEstadoCorrida"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- ResLiqDeu -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="resLiqDeuAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
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
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
<!-- resLiqDeuViewAdapter.jsp -->