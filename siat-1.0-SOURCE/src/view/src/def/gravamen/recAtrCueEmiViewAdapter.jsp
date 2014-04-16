<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/def/AdministrarRecAtrCueEmi.do">

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
					<td class="normal"><bean:write name="recAtrCueEmiAdapterVO" property="recAtrCueEmi.recurso.categoria.tipo.desTipo"/></td>
					<!-- Categoria -->										
					<td align="right"><label><bean:message bundle="def" key="def.categoria.label"/>:</label></td>
					<td class="normal"><bean:write name="recAtrCueEmiAdapterVO" property="recAtrCueEmi.recurso.categoria.desCategoria"/></td>
				</tr>
				<tr>
					<!-- Codigo -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.codRecurso.label"/>:</label></td>
					<td class="normal"><bean:write name="recAtrCueEmiAdapterVO" property="recAtrCueEmi.recurso.codRecurso"/></td>
					<!-- Descripcion -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.desRecurso.label"/>: </label></td>
					<td class="normal"><bean:write name="recAtrCueEmiAdapterVO" property="recAtrCueEmi.recurso.desRecurso"/></td>					
				</tr>
			</table>
		</fieldset>
		<!-- Fin Datos del Recurso -->

	<!-- RecAtrCueEmi -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.recAtrCueEmi.title"/></legend>			
			<table class="tabladatos">
				<tr>
					<!-- Codigo -->
					<td><label><bean:message bundle="def" key="def.atributo.codAtributo.label"/>: </label></td>
					<td class="normal"><bean:write name="recAtrCueEmiAdapterVO" property="recAtrCueEmi.atributo.codAtributo"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.atributo.desAtributo.label"/>: </label></td>
					<td class="normal"><bean:write name="recAtrCueEmiAdapterVO" property="recAtrCueEmi.atributo.desAtributo"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.recAtrCueEmi.fechaDesde.label"/>: </label></td>
					<td class="normal">
						<bean:write name="recAtrCueEmiAdapterVO" property="recAtrCueEmi.fechaDesdeView"/>
					</td>
					<td><label><bean:message bundle="def" key="def.recAtrCueEmi.fechaHasta.label"/>: </label></td>
					<td class="normal"><bean:write name="recAtrCueEmiAdapterVO" property="recAtrCueEmi.fechaHastaView"/></td>
				</tr>	
				<tr>
					<!-- Permite visualizacion desde la consulta de deuda -->
					<td><label><bean:message bundle="def" key="def.recAtrCueEmi.esVisConDeu.label"/>: </label></td>
					<td class="normal"><bean:write name="recAtrCueEmiAdapterVO" property="recAtrCueEmi.esVisConDeu.value"/></td>
				</tr>
				<tr>
					<!-- Permite visualizacion desde el recibo de deuda -->
					<td><label><bean:message bundle="def" key="def.recAtrCueEmi.esVisRec.label"/>: </label></td>
					<td class="normal"><bean:write name="recAtrCueEmiAdapterVO" property="recAtrCueEmi.esVisRec.value"/></td>
				</tr>

			</table>
		</fieldset>
		<!-- Fin RecAtrCueEmi -->


		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="recAtrCueEmiAdapterVO" property="act" value="eliminar">
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