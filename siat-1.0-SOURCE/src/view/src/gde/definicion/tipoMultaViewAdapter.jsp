<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/gde/AdministrarTipoMulta.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="gde" key="gde.tipoMultaViewAdapter.title"/></h1>	
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- Atributo -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.tipoMulta.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="gde" key="gde.tipoMulta.descripcion.label"/>: </label></td>
					<td class="normal"><bean:write name="tipoMultaAdapterVO" property="tipoMulta.desTipoMulta"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="tipoMultaAdapterVO" property="tipoMulta.recurso.desRecurso"/></td>
				</tr>
				<tr>				
					<td><label><bean:message bundle="gde" key="gde.tipoMulta.asociadaAOrden.label"/>: </label></td>
					<td class="normal"><bean:write name="tipoMultaAdapterVO" property="tipoMulta.asociadaAOrden.value"/></td>
				</tr>
				<tr>				
					<td><label><bean:message bundle="gde" key="gde.tipoMulta.esImporteManual.label"/>: </label></td>
					<td class="normal"><bean:write name="tipoMultaAdapterVO" property="tipoMulta.esImporteManual.value"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.tipoMulta.canMinDes.label"/>: </label></td>
					<td class="normal"><bean:write name="tipoMultaAdapterVO" property="tipoMulta.canMinDesView" /></td>
				</tr>
				<tr>	
					<td><label><bean:message bundle="gde" key="gde.tipoMulta.canMinHas.label"/>: </label></td>
					<td class="normal"><bean:write name="tipoMultaAdapterVO" property="tipoMulta.canMinHasView" /></td>					
				</tr>
				
				<tr>
					<td><label><bean:message bundle="gde" key="gde.tipoMulta.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="tipoMultaAdapterVO" property="tipoMulta.estado.value"/></td>				
				</tr>
			</table>
		</fieldset>	
		<!-- Atributo -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="tipoMultaAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="tipoMultaAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="tipoMultaAdapterVO" property="act" value="desactivar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
							<bean:message bundle="base" key="abm.button.desactivar"/>
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
