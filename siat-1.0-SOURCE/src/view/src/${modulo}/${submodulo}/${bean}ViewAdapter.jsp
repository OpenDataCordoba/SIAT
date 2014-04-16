<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/${modulo}/Administrar${Bean}.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="${modulo}" key="${modulo}.${bean}ViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- ${Bean} -->
		<fieldset>
			<legend><bean:message bundle="${modulo}" key="${modulo}.${bean}.title"/></legend>
			<table class="tabladatos">
				<!-- <#BeanWrites#> -->
				<#BeanWrites.Codigo#>
				<tr>
					<td><label><bean:message bundle="${modulo}" key="${modulo}.${bean}.cod${Bean}.label"/>: </label></td>
					<td class="normal"><bean:write name="${bean}AdapterVO" property="${bean}.cod${Bean}"/></td>
				</tr>
				<#BeanWrites.Codigo#>
				<#BeanWrites.Descripcion#>
				<tr>
					<td><label><bean:message bundle="${modulo}" key="${modulo}.${bean}.des${Bean}.label"/>: </label></td>
					<td class="normal"><bean:write name="${bean}AdapterVO" property="${bean}.des${Bean}"/></td>
				</tr>
				<#BeanWrites.Descripcion#>
				<#BeanWrites.PropiedadRef#>
				<tr>
					<td><label><bean:message bundle="${modulo_ref}" key="${modulo_ref}.${bean_ref}.${propiedad_ref}.ref"/>: </label></td>
					<td class="normal"><bean:write name="${bean}AdapterVO" property="${bean}.${bean_ref}.${propiedad_ref}"/></td>
				</tr>
				<#BeanWrites.PropiedadRef#>
				<#BeanWrites.PropiedadSiNo#>
				<tr>
					<td><label><bean:message bundle="${modulo}" key="${modulo}.${bean}.${propiedad}.label"/>: </label></td>
					<td class="normal"><bean:write name="${bean}AdapterVO" property="${bean}.${propiedad}.value"/></td>				
				</tr>
				<#BeanWrites.PropiedadSiNo#>				
				<#BeanWrites.Propiedad#>
				<tr>
					<td><label><bean:message bundle="${modulo}" key="${modulo}.${bean}.${propiedad}.label"/>: </label></td>
					<td class="normal"><bean:write name="${bean}AdapterVO" property="${bean}.${propiedad}"/></td>				
				</tr>
				<#BeanWrites.Propiedad#>
				<#BeanWrites.PropiedadCurrency#>
				<tr>
					<td><label><bean:message bundle="${modulo}" key="${modulo}.${bean}.${propiedad}.label"/>: </label></td>
					<td class="normal"><bean:message bundle="base" key="base.currency"/><bean:write name="${bean}AdapterVO" property="${bean}.${propiedad}View"/></td>
				</tr>
				<#BeanWrites.PropiedadCurrency#>
				<#BeanWrites.PropiedadFecha#>
				<tr>
					<td><label><bean:message bundle="${modulo}" key="${modulo}.${bean}.${propiedadFecha}.label"/>: </label></td>
					<td class="normal"><bean:write name="${bean}AdapterVO" property="${bean}.${propiedadFecha}View"/></td>				
				</tr>
				<#BeanWrites.PropiedadFecha#>
				<#BeanWrites.Estado#>
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="${bean}AdapterVO" property="${bean}.estado.value"/></td>
				</tr>
				<#BeanWrites.Estado#>
				<!-- <#BeanWrites#> -->
			</table>
		</fieldset>	
		<!-- ${Bean} -->

		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="left">
	   	    	   <logic:equal name="${bean}AdapterVO" property="act" value="ver">
		   	    	    <html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						    <bean:message bundle="base" key="abm.button.imprimir"/>
					    </html:button>
					</logic:equal>
					<logic:equal name="${bean}AdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="${bean}AdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="${bean}AdapterVO" property="act" value="desactivar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
							<bean:message bundle="base" key="abm.button.desactivar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
	   	    </tr>
	   	 </table>
	    <input type="hidden" name="name"  value="<bean:write name='${bean}AdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->