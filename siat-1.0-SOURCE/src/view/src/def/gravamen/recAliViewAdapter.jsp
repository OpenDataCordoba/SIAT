<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/def/AdministrarRecAli.do">

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

		<!-- Datos del Recurso -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.recurso.title"/></legend>			
			<table class="tabladatos">
				<tr>
					<!-- Tipo de Categoria -->										
					<td align="right"><label><bean:message bundle="def" key="def.categoria.tipo.label"/>:</label></td>
					<td class="normal"><bean:write name="recAliAdapterVO" property="recAli.recurso.categoria.tipo.desTipo"/></td>
					<!-- Categoria -->										
					<td align="right"><label><bean:message bundle="def" key="def.categoria.label"/>:</label></td>
					<td class="normal"><bean:write name="recAliAdapterVO" property="recAli.recurso.categoria.desCategoria"/></td>
				</tr>
				<tr>
					<!-- Codigo -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.codRecurso.label"/>:</label></td>
					<td class="normal"><bean:write name="recAliAdapterVO" property="recAli.recurso.codRecurso"/></td>
					<!-- Descripcion -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.desRecurso.label"/>: </label></td>
					<td class="normal"><bean:write name="recAliAdapterVO" property="recAli.recurso.desRecurso"/></td>					
				</tr>
			</table>
		</fieldset>
		<!-- Fin Datos del Recurso -->

		<!-- RecAli -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.recAli.label"/></legend>			
			<table class="tabladatos">
				<!-- TipoAlicuota -->
				<tr>
					<td><label><bean:message bundle="def" key="def.recAli.recTipAli.label"/>: </label></td>
					<td class="normal">
						<bean:write name="recAliAdapterVO" property="recAli.recTipAli.desTipoAlicuota"/>
					</td>
				</tr>
				<tr>
					<td><label>&nbsp;<bean:message bundle="def" key="def.recAli.alicuota.label"/>: </label></td>
					<td class="normal"><bean:write name="recAliAdapterVO" property="recAli.alicuotaView"/></td>
				</tr>
				
				<tr>
					<td><label>&nbsp;<bean:message bundle="def" key="def.recAli.fechaDesde.label"/>: </label></td>
					<td class="normal"><bean:write name="recAliAdapterVO" property="recAli.fechaDesdeView"/></td>
					
					<td><label><bean:message bundle="def" key="def.recAli.fechaHasta.label"/>: </label></td>
					<td class="normal"><bean:write name="recAliAdapterVO" property="recAli.fechaHastaView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.recAli.observacion.label"/>: </label></td>
					<td class="normal"><bean:write name="recAliAdapterVO" property="recAli.observacion"/></td>
				</tr>		
			</table>
		</fieldset>
		<!-- Fin RecAli -->


	<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
		   	    		<bean:message bundle="base" key="abm.button.volver"/>
		   	    	</html:button>
	   	    	</td>
   	   			<td align="right" width="50%">
					<logic:equal name="recAliAdapterVO" property="act" value="eliminar">
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
	