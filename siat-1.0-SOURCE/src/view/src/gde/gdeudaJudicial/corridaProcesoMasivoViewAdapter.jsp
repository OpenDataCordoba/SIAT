<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarCorridaProcesoMasivo.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="pro" key="pro.adpCorridaViewAdapter.title"/></h1>	
		
		<!-- Corrida -->
		<fieldset>
			<legend><bean:message bundle="pro" key="pro.adpCorridaViewAdapter.datosTitle"/></legend>
			<table class="tabladatos">
				<!-- Descripcion -->
				<tr>
					<td><label><bean:message bundle="pro" key="pro.corrida.desCorrida.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="corridaProcesoMasivoAdapterVO" property="procesoMasivo.corrida.desCorrida"/></td>
				</tr>
				<!-- Fecha Inicio -->
				<tr>
					<td><label><bean:message bundle="pro" key="pro.corrida.fechaOriginal.ref"/>: </label></td>
					<td class="normal"><bean:write name="corridaProcesoMasivoAdapterVO" property="procesoMasivo.corrida.fechaInicioView"/></td>
				<!-- Hora Inicio -->
					<td><label><bean:message bundle="pro" key="pro.corrida.horaOriginal.ref"/>: </label></td>
					<td class="normal"><bean:write name="corridaProcesoMasivoAdapterVO" property="procesoMasivo.corrida.horaInicioView"/></td>
				</tr>

			</table>
		</fieldset>	
		<!-- Corrida -->

		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="corridaProcesoMasivoAdapterVO" property="act" value="retroceder">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('retroceder', '');">
							<bean:message bundle="pro" key="pro.adpCorridaViewAdapter.abm.button.retroceder"/>
						</html:button>
					</logic:equal>
					<logic:equal name="corridaProcesoMasivoAdapterVO" property="act" value="reiniciar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('reiniciar', '');">
							<bean:message bundle="pro" key="pro.adpCorridaViewAdapter.abm.button.reiniciar"/>
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
