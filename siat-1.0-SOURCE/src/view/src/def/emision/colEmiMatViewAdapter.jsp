<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/def/AdministrarColEmiMat.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="def" key="def.colEmiMatViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- EmiMat -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.emiMat.title"/></legend>
			
			<table class="tabladatos">
				<tr>	
					<td><label><bean:message bundle="def" key="def.emiMat.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="colEmiMatAdapterVO" property="colEmiMat.emiMat.recurso.desRecurso" /></td>
				</tr>

				<tr>
					<td><label><bean:message bundle="def" key="def.emiMat.codEmiMat.label"/>: </label></td>
					<td class="normal"><bean:write name="colEmiMatAdapterVO" property="colEmiMat.emiMat.codEmiMat"/></td>
				</tr>
			</table>
		</fieldset>	

		<!-- ColEmiMat -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.colEmiMat.title"/></legend>
			<table class="tabladatos">
				<!-- Codigo -->
				<tr>
					<td><label><bean:message bundle="def" key="def.colEmiMat.codColumna.label"/>: </label></td>
					<td class="normal"><bean:write name="colEmiMatAdapterVO" property="colEmiMat.codColumna"/></td>
				</tr>

				<!-- Tipo de Dato -->
				<tr>
					<td><label><bean:message bundle="def" key="def.colEmiMat.tipoDato.label"/>: </label></td>
					<td class="normal"><bean:write name="colEmiMatAdapterVO" property="colEmiMat.tipoDato.value"/></td>
				</tr>

				<!-- Tipo de Columna -->
				<tr>
					<td><label><bean:message bundle="def" key="def.colEmiMat.tipoColumna.label"/>: </label></td>
					<td class="normal"><bean:write name="colEmiMatAdapterVO" property="colEmiMat.tipoColumna.value"/></td>
				</tr>

				<!-- Orden -->
				<tr>
					<td><label><bean:message bundle="def" key="def.colEmiMat.orden.label"/>: </label></td>
					<td class="normal"><bean:write name="colEmiMatAdapterVO" property="colEmiMat.ordenView"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- ColEmiMat -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="colEmiMatAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
	   	    </tr>
	   	 </table>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   	
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
		
		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
<!-- colEmiMatViewAdapter.jsp -->