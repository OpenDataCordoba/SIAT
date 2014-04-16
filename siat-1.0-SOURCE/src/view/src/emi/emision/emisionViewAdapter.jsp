<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/emi/AdministrarEmision.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	
	<logic:equal name="emisionAdapterVO" property="emision.tipoEmision.esImpresionCdm" value="true">
		<h1><bean:message bundle="emi" key="emi.emisionViewAdapter.impresion.title"/></h1>	
	</logic:equal>
	
	<logic:notEqual name="emisionAdapterVO" property="emision.tipoEmision.esImpresionCdm" value="true">
		<h1><bean:message bundle="emi" key="emi.emisionViewAdapter.title"/></h1>	
	</logic:notEqual>

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- Emision -->
	<fieldset>
	
		<legend><bean:message bundle="emi" key="emi.emision.title"/></legend>	

		<table class="tabladatos">
			<tr>
				<!-- Recurso -->
				<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal"><bean:write name="emisionAdapterVO" property="emision.recurso.desRecurso"/></td>
			</tr>
			<tr>
				<!-- Fecha Emision -->
				<td><label><bean:message bundle="emi" key="emi.emision.label"/>: </label></td>
				<td class="normal"><bean:write name="emisionAdapterVO" property="emision.fechaEmisionView"/></td>
			</tr>
		</table>
	</fieldset>	
	<!-- Emision -->

	<logic:equal name="emisionAdapterVO" property="emision.tipoEmision.esEmisionCdm" value="true">
		<!-- Emision CdM -->
			<%@ include file="/emi/emision/emisionCdMViewAdapter.jsp" %>
		<!-- Fin Emision CdM -->
	</logic:equal>
	
	<logic:equal name="emisionAdapterVO" property="emision.tipoEmision.esImpresionCdm" value="true">
		<!-- Impresion CdM 	 -->	
			<%@ include file="/emi/emision/impresionCdMViewAdapter.jsp" %>
		<!-- Fin Impresion CdM -->
	</logic:equal>

	<logic:equal name="emisionAdapterVO" property="emision.tipoEmision.esEmisionCorCdm" value="true">
		<!-- Emision Corregida CdM 	 -->
		<%@ include file="/emi/emision/emisionCorCdMViewAdapter.jsp" %>
		<!-- Fin Emision Corregida CdM -->
	</logic:equal>
	
		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="emisionAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="emisionAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="emisionAdapterVO" property="act" value="desactivar">
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
		<input type="hidden" name="idTipoEmision" value="<bean:write name="emisionAdapterVO" property="emision.tipoEmision.id" bundle="base" formatKey="general.format.id"/>"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->