<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/rec/AdministrarTipoObra.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="rec" key="rec.tipoObraViewAdapter.title"/></h1>

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>	
		
		<!-- TipoObra -->
		<fieldset>
			<legend><bean:message bundle="rec" key="rec.tipoObra.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="tipoObraAdapterVO" property="tipoObra.recurso.desRecurso" /></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="rec" key="rec.tipoObra.desTipoObra.label"/>: </label></td>
					<td class="normal"><bean:write name="tipoObraAdapterVO" property="tipoObra.desTipoObra"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="rec" key="rec.tipoObra.costoCuadra.label"/>: </label></td>
					<td class="normal"><bean:write name="tipoObraAdapterVO" property="tipoObra.costoCuadraView"/></td>			
				</tr>
				<tr>
					<td><label><bean:message bundle="rec" key="rec.tipoObra.costoMetroFrente.label"/>: </label></td>
					<td class="normal"><bean:write name="tipoObraAdapterVO" property="tipoObra.costoMetroFrenteView"/></td>			
				</tr>
				<tr>
					<td><label><bean:message bundle="rec" key="rec.tipoObra.costoUT.label"/>: </label></td>
					<td class="normal"><bean:write name="tipoObraAdapterVO" property="tipoObra.costoUTView"/></td>			
				</tr>
				<tr>
					<td><label><bean:message bundle="rec" key="rec.tipoObra.costoModulo.label"/>: </label></td>
					<td class="normal"><bean:write name="tipoObraAdapterVO" property="tipoObra.costoModuloView"/></td>			
				</tr>
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="tipoObraAdapterVO" property="tipoObra.estado.value"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- TipoObra -->

		<table class="tablabotones" width="100%" >
	    	<tr>
  	    		<td align="left" width="50%" >
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				    <logic:equal name="tipoObraAdapterVO" property="act" value="ver">
		   	        	<html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
					    	<bean:message bundle="base" key="abm.button.imprimir"/>
					    </html:button>
					</logic:equal>
				</td>	
				<td align="right" width="50%" >
	   	    		<logic:equal name="tipoObraAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="tipoObraAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="tipoObraAdapterVO" property="act" value="desactivar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
							<bean:message bundle="base" key="abm.button.desactivar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
	   	    </tr>
	   	 </table>
	   	 <input type="hidden" name="name"  value="<bean:write name='tipoObraAdapterVO' property='name'/>" id="name"/>
	   	 <input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
	   	 		
		<input type="hidden" name="method" value=""/>
		<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
		<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>		
		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
