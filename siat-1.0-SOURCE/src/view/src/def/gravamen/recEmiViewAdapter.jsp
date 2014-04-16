<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/def/AdministrarRecEmi.do">

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
					<td class="normal"><bean:write name="recEmiAdapterVO" property="recEmi.recurso.categoria.tipo.desTipo"/></td>
					<!-- Categoria -->										
					<td align="right"><label><bean:message bundle="def" key="def.categoria.label"/>:</label></td>
					<td class="normal"><bean:write name="recEmiAdapterVO" property="recEmi.recurso.categoria.desCategoria"/></td>
				</tr>
				<tr>
					<!-- Codigo -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.codRecurso.label"/>:</label></td>
					<td class="normal"><bean:write name="recEmiAdapterVO" property="recEmi.recurso.codRecurso"/></td>
					<!-- Descripcion -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.desRecurso.label"/>: </label></td>
					<td class="normal"><bean:write name="recEmiAdapterVO" property="recEmi.recurso.desRecurso"/></td>					
				</tr>
			</table>
		</fieldset>
		<!-- Fin Datos del Recurso -->
	
	<!-- RecEmi -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.recEmi.title"/></legend>			
			<table class="tabladatos">
				<tr>
					<!-- TipoEmision -->										
					<td align="right"><label><bean:message bundle="def" key="def.recEmi.tipoEmision.label"/>:</label></td>
					<td class="normal"><bean:write name="recEmiAdapterVO" property="recEmi.tipoEmision.desTipoEmision"/></td>
					<!-- PeriodoDeuda -->										
					<td align="right"><label><bean:message bundle="def" key="def.recEmi.periodoDeuda.label"/>:</label></td>
					<td class="normal"><bean:write name="recEmiAdapterVO" property="recEmi.periodoDeuda.desPeriodoDeuda"/></td>										
				</tr>				
				<tr>
					<!-- Programa de Calculo -->
					<td align="right"><label><bean:message bundle="def" key="def.recEmi.programa.label"/>:</label></td>
					<td class="normal"><bean:write name="recEmiAdapterVO" property="recEmi.formulario.codFormulario"/></td>														
				</tr>
				<tr>
					<!-- Vencimiento -->										
					<td align="right"><label><bean:message bundle="def" key="def.recEmi.forVen.label"/>:</label></td>
					<td class="normal"><bean:write name="recEmiAdapterVO" property="recEmi.forVen.desVencimiento"/></td>														
				</tr>
				<tr>
					<!-- AtributoEmision -->										
					<td align="right"><label><bean:message bundle="def" key="def.recEmi.atributoEmision.label"/>:</label></td>
					<td class="normal"><bean:write name="recEmiAdapterVO" property="recEmi.atributoEmision.desAtributo"/></td>														
				</tr>	
				<!-- Genera Notificacion
					<tr>
						<td align="right"><label><bean:message bundle="def" key="def.recEmi.generaNotificacion.label"/>:</label></td>
						<td class="normal"><bean:write name="recEmiAdapterVO" property="recEmi.generaNotificacion.value"/></td>														
					</tr> 
				-->				
				<tr>
					<td><label><bean:message bundle="def" key="def.recEmi.fechaDesde.label"/>: </label></td>
					<td class="normal"><bean:write name="recEmiAdapterVO" property="recEmi.fechaDesdeView"/></td>
					<td><label><bean:message bundle="def" key="def.recEmi.fechaHasta.label"/>: </label></td>
					<td class="normal"><bean:write name="recEmiAdapterVO" property="recEmi.fechaHastaView"/></td>
				</tr>		
			</table>
		</fieldset>
		<!-- Fin RecEmi -->

		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="recEmiAdapterVO" property="act" value="eliminar">
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
		