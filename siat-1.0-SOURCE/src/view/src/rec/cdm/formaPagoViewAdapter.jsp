<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/rec/AdministrarFormaPago.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="rec" key="rec.formaPagoViewAdapter.title"/></h1>

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>	
	
		
		<!-- FormaPago -->
		<fieldset>
			<legend><bean:message bundle="rec" key="rec.formaPago.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="formaPagoAdapterVO" property="formaPago.recurso.desRecurso"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="rec" key="rec.formaPago.desFormaPago.label"/>: </label></td>
					<td class="normal" colspan="4"><bean:write name="formaPagoAdapterVO" property="formaPago.desFormaPago"/></td>
				</tr>
				<tr>	
					<td><label><bean:message bundle="rec" key="rec.formaPago.esCantCuotasFijas.label"/>: </label></td>
					<td class="normal"><bean:write name="formaPagoAdapterVO" property="formaPago.esCantCuotasFijas.value"/></td>
					<td><label><bean:message bundle="rec" key="rec.formaPago.cantCuotas.label"/>: </label></td>
					<td class="normal"><bean:write name="formaPagoAdapterVO" property="formaPago.cantCuotasView"/></td>				
				</tr>
				<tr>
					<td><label><bean:message bundle="rec" key="rec.formaPago.descuento.label"/>: </label></td>
					<td class="normal"><bean:write name="formaPagoAdapterVO" property="formaPago.descuentoView"/></td>				

					<td><label><bean:message bundle="rec" key="rec.formaPago.interesFinanciero.label"/>: </label></td>
					<td class="normal"><bean:write name="formaPagoAdapterVO" property="formaPago.interesFinancieroView"/></td>				
				</tr>
				<tr>	
					<td><label><bean:message bundle="rec" key="rec.formaPago.esEspecial.label"/>: </label></td>
					<td class="normal"><bean:write name="formaPagoAdapterVO" property="formaPago.esEspecial.value"/></td>				
					<logic:equal name="formaPagoAdapterVO" property="formaPago.exencionEnabled" value="true">					
						<td><label><bean:message bundle="exe" key="exe.exencion.label"/>: </label></td>
						<td class="normal"><bean:write name="formaPagoAdapterVO" property="formaPago.exencion.desExencion"/></td>
					</logic:equal>
				</tr>
				<!-- Estado -->
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="formaPagoAdapterVO" property="formaPago.estado.value"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- FormaPago -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="formaPagoAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="formaPagoAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="formaPagoAdapterVO" property="act" value="desactivar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
							<bean:message bundle="base" key="abm.button.desactivar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
	   	    </tr>
	   	 </table>
	   	 		
		<input type="hidden" name="method" value=""/>
<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
