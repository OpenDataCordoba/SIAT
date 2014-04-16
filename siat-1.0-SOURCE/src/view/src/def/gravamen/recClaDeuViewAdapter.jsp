<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/def/AdministrarRecClaDeu.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="def" key="def.recursoAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>

		<!-- Datos del Recurso  -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.recurso.title"/></legend>			
			<table class="tabladatos">
				<tr>
					<!-- Tipo de Categoria -->										
					<td align="right"><label><bean:message bundle="def" key="def.categoria.tipo.label"/>:</label></td>
					<td class="normal"><bean:write name="recClaDeuAdapterVO" property="recClaDeu.recurso.categoria.tipo.desTipo"/></td>
					<!-- Categoria -->										
					<td align="right"><label><bean:message bundle="def" key="def.categoria.label"/>:</label></td>
					<td class="normal"><bean:write name="recClaDeuAdapterVO" property="recClaDeu.recurso.categoria.desCategoria"/></td>
				</tr>
				<tr>
					<!-- Codigo -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.codRecurso.label"/>:</label></td>
					<td class="normal"><bean:write name="recClaDeuAdapterVO" property="recClaDeu.recurso.codRecurso"/></td>
					<!-- Descripcion -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.desRecurso.label"/>: </label></td>
					<td class="normal"><bean:write name="recClaDeuAdapterVO" property="recClaDeu.recurso.desRecurso"/></td>					
				</tr>
			</table>
		</fieldset>
		<!-- Fin Datos del Recurso -->
	
	<!-- RecClaDeu -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.recClaDeu.title"/></legend>			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="def" key="def.recClaDeu.desClaDeu.label"/>: </label></td>
					<td class="normal"><bean:write name="recClaDeuAdapterVO" property="recClaDeu.desClaDeu"/></td>				
					<td><label><bean:message bundle="def" key="def.recClaDeu.abrClaDeu.label"/>: </label></td>
					<td class="normal"><bean:write name="recClaDeuAdapterVO" property="recClaDeu.abrClaDeu"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.recClaDeu.fechaDesde.label"/>: </label></td>
					<td class="normal"><bean:write name="recClaDeuAdapterVO" property="recClaDeu.fechaDesdeView"/></td>
					<td><label><bean:message bundle="def" key="def.recClaDeu.fechaHasta.label"/>: </label></td>
					<td class="normal"><bean:write name="recClaDeuAdapterVO" property="recClaDeu.fechaHastaView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.recClaDeu.esOriginal.label"/>: </label></td>
					<td class="normal"><bean:write name="recClaDeuAdapterVO" property="recClaDeu.esOriginal.value"/></td>
					<td><label><bean:message bundle="def" key="def.recClaDeu.actualizaDeuda.label"/>: </label></td>
					<td class="normal"><bean:write name="recClaDeuAdapterVO" property="recClaDeu.actualizaDeuda.value"/></td>				
				</tr>		
			</table>
		</fieldset>
		<!-- Fin RecClaDeu -->

		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="recClaDeuAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
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
		