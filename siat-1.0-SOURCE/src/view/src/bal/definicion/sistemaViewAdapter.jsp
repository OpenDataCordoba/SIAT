<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/AdministrarSistema.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.sistemaViewAdapter.title"/></h1>	
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- Sistema -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.sistema.title"/></legend>
			<table class="tabladatos">
				<!-- Numero -->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.sistema.nroSistema.label"/>: </label></td>
					<td class="normal"><bean:write name="sistemaAdapterVO" property="sistema.nroSistemaView"/></td>
				</tr>
				<!-- Descrpicion -->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.sistema.desSistema.label"/>: </label></td>
					<td class="normal"><bean:write name="sistemaAdapterVO" property="sistema.desSistema"/></td>
				</tr>
				<!-- Servicio Banco -->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.sistema.servicioBanco.label"/>: </label></td>
					<td class="normal"><bean:write name="sistemaAdapterVO" property="sistema.servicioBanco.desServicioBanco"/></td>
				</tr>
				<!-- Es Servicio Banco -->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.sistema.esServicioBanco.label"/>: </label></td>
					<td class="normal"><bean:write name="sistemaAdapterVO" property="sistema.esServicioBanco.value"/></td>
				</tr>				
				<!-- Recurso -->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.sistema.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="sistemaAdapterVO" property="sistema.recurso.desRecurso"/></td>
				</tr>
				<!-- Tipo Deuda -->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.sistema.tipoDeuda.label"/>: </label></td>
					<td class="normal"><bean:write name="sistemaAdapterVO" property="sistema.tipoDeuda.desTipoDeuda"/></td>
				</tr>
				<!-- Estado -->
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="sistemaAdapterVO" property="sistema.estado.value"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- Sistema -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="sistemaAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="sistemaAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="sistemaAdapterVO" property="act" value="desactivar">
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
