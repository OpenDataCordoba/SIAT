<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/frm/AdministrarForCam.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="frm" key="frm.forCamViewAdapter.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
			
		<!-- ForCam -->
		<fieldset>
			<legend><bean:message bundle="frm" key="frm.forCam.title"/></legend>
			<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="frm" key="frm.forCam.codForCam.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="forCamAdapterVO" property="forCam.codForCam"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="frm" key="frm.forCam.desForCam.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="forCamAdapterVO" property="forCam.desForCam"/></td>
			</tr>
			<!-- largoMax -->
			<tr>
				<td><label><bean:message bundle="frm" key="frm.forCam.largoMax.label"/>: </label></td>
				<td class="normal" colspan="3">
						<bean:write name="forCamAdapterVO" property="forCam.largoMaxView"/>
				</td>
			</tr>
			
			<tr>
				<td><label><bean:message bundle="frm" key="frm.forCam.valorDefecto.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="forCamAdapterVO" property="forCam.valorDefecto"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="frm" key="frm.forCam.fechaDesde.label"/>: </label></td>
				<td class="normal"><bean:write name="forCamAdapterVO" property="forCam.fechaDesdeView"/></td>				
				<td><label><bean:message bundle="frm" key="frm.forCam.fechaHasta.label"/>: </label></td>
				<td class="normal"><bean:write name="forCamAdapterVO" property="forCam.fechaHastaView"/></td>				
			</tr>
				<!-- <#BeanWrites#> -->
			</table>
		</fieldset>	
		<!-- ForCam -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="forCamAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="forCamAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="forCamAdapterVO" property="act" value="desactivar">
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
