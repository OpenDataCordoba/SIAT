<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/pad/AdministrarCueExcSelDeu.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="pad" key="pad.cueExcSelDeuViewAdapter.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- CueExcSelDeu -->
		<fieldset>
			<legend><bean:message bundle="pad" key="pad.cueExcSelDeu.title"/></legend>
			<table class="tabladatos">
				<!-- Recurso -->
				<tr>
					<td><label><bean:message bundle="pad" key="pad.cueExcSel.cuenta.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="cueExcSelDeuAdapterVO" property="cueExcSelDeu.cueExcSel.cuenta.recurso.desRecurso"/></td>
				</tr>
				<!-- Cuenta -->
				<tr>
					<td><label><bean:message bundle="pad" key="pad.cueExcSel.cuenta.label"/>: </label></td>
					<td class="normal"><bean:write name="cueExcSelDeuAdapterVO" property="cueExcSelDeu.cueExcSel.cuenta.numeroCuenta"/></td>
				</tr>
				
				<!-- Inclucion de CasoView -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
					<td colspan="3">
						<bean:define id="IncludedVO" name="cueExcSelDeuAdapterVO" property="cueExcSelDeu"/>
						<%@ include file="/cas/caso/includeCasoView.jsp" %>				
					</td>
				</tr>
				<!-- Fin Inclucion de CasoView -->				
				
				<!-- Observacion -->
				<tr>
					<td><label><bean:message bundle="pad" key="pad.cueExcSelDeu.observacion.label"/>: </label></td>
					<td class="normal"><bean:write name="cueExcSelDeuAdapterVO" property="cueExcSelDeu.observacion"/></td>
				</tr>

			</table>
		</fieldset>	
		<!-- CueExcSel -->
	
		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="cueExcSelDeuAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="cueExcSelDeuAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="cueExcSelDeuAdapterVO" property="act" value="desactivar">
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
