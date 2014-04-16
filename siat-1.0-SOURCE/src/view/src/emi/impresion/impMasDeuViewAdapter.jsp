<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/emi/AdministrarImpMasDeu.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="emi" key="emi.impMasDeuViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- ImpMasDeu -->
		<fieldset>
			<legend><bean:message bundle="emi" key="emi.impMasDeu.title"/></legend>
			<table class="tabladatos">
				<tr>
					<!-- Recurso -->
					<td><label><bean:message bundle="emi" key="emi.impMasDeu.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="impMasDeuAdapterVO" property="impMasDeu.recurso.desRecurso"/></td>
				</tr>

				<logic:equal name="impMasDeuAdapterVO" property="selectAtrValEnabled" value="true">
					<tr>					
						<!-- Atributo -->
						<td><label><bean:write name="impMasDeuAdapterVO" property="impMasDeu.atributo.desAtributo"/>: </label></td>
						<td class="normal"><bean:write name="impMasDeuAdapterVO" property="impMasDeu.atrValor"/></td>
					</tr>
				</logic:equal>

				<tr>
					<!-- Formato de salida -->	
					<td><label><bean:message bundle="emi" key="emi.impMasDeu.formatoSalida.label"/>: </label></td>
					<td class="normal"><bean:write name="impMasDeuAdapterVO" property="impMasDeu.formatoSalida.value"/></td>					
				</tr>

				<tr>
					<!-- Anio -->
					<td><label><bean:message bundle="emi" key="emi.impMasDeu.anio.label"/>: </label></td>
					<td class="normal"><bean:write name="impMasDeuAdapterVO" property="impMasDeu.anioView"/></td>
				</tr>
				<tr>
					<!-- Periodo Desde -->
					<td><label><bean:message bundle="emi" key="emi.impMasDeu.periodoDesde.label"/>: </label></td>
					<td class="normal"><bean:write name="impMasDeuAdapterVO" property="impMasDeu.periodoDesdeView"/></td>

					<!-- Periodo Hasta -->
					<td><label><bean:message bundle="emi" key="emi.impMasDeu.periodoHasta.label"/>: </label></td>
					<td class="normal"><bean:write name="impMasDeuAdapterVO" property="impMasDeu.periodoHastaView"/></td>
				</tr>

				<tr>
					<!-- Aplicar criterios de reparto -->
					<td><label><bean:message bundle="emi" key="emi.impMasDeu.abrirPorBroche.label"/>: </label></td>
					<td class="normal">
						<bean:write name="impMasDeuAdapterVO" property="impMasDeu.abrirPorBroche.value"/>					
					</td>
					<td class="normal" colspan="2">
						<ul class="vinieta">
							<li><bean:message bundle="emi" key="emi.impMasDeu.abrirPorBroche.description"/></li>
						</ul>
					</td>
				</tr>

				<tr>
					<!-- Estado -->
					<td><label><bean:message bundle="emi" key="emi.impMasDeu.estadoCorrida.label"/>: </label></td>
					<td class="normal"><bean:write name="impMasDeuAdapterVO" property="impMasDeu.corrida.estadoCorrida.desEstadoCorrida"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- ImpMasDeu -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="impMasDeuAdapterVO" property="act" value="eliminar">
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
<!-- impMasDeuViewAdapter.jsp -->