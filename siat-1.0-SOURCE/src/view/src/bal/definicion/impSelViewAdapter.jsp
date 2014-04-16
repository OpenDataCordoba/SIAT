<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/AdministrarImpSel.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.impSelViewAdapter.title"/></h1>	
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		<!-- SelladoImporte -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.impSel.title"/></legend>
			<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="bal" key="bal.sellado.codSellado.ref"/>: </label></td>
				<td class="normal"><bean:write name="impSelAdapterVO" property="impSel.sellado.codSellado"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.sellado.desSellado.ref"/>: </label></td>
				<td class="normal"><bean:write name="impSelAdapterVO" property="impSel.sellado.desSellado"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.impSel.fechaDesde.label"/>: </label></td>
				<td class="normal"><bean:write name="impSelAdapterVO" property="impSel.fechaDesdeView"/></td>				
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.impSel.fechaHasta.label"/>: </label></td>
				<td class="normal"><bean:write name="impSelAdapterVO" property="impSel.fechaHastaView"/></td>				
			</tr>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.impSel.costo.label"/>: </label></td>
				<td class="normal"><bean:write name="impSelAdapterVO" property="impSel.costoView"/></td>				
			</tr>
			<tr>
				<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
				<td class="normal"><bean:write name="impSelAdapterVO" property="impSel.estado.value"/></td>
			</tr>
				<!-- <#BeanWrites#> -->
			</table>
		</fieldset>	
		<!-- SelladoImporte -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="impSelAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="impSelAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="impSelAdapterVO" property="act" value="desactivar">
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
