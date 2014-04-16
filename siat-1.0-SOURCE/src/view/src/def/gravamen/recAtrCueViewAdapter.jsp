<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/def/AdministrarRecAtrCue.do">

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
					<td class="normal"><bean:write name="recAtrCueAdapterVO" property="recurso.categoria.tipo.desTipo"/></td>
					<!-- Categoria -->										
					<td align="right"><label><bean:message bundle="def" key="def.categoria.label"/>:</label></td>
					<td class="normal"><bean:write name="recAtrCueAdapterVO" property="recurso.categoria.desCategoria"/></td>
				</tr>
				<tr>
					<!-- Codigo -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.codRecurso.label"/>:</label></td>
					<td class="normal"><bean:write name="recAtrCueAdapterVO" property="recurso.codRecurso"/></td>
					<!-- Descripcion -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.desRecurso.label"/>: </label></td>
					<td class="normal"><bean:write name="recAtrCueAdapterVO" property="recurso.desRecurso"/></td>					
				</tr>
			</table>
		</fieldset>
		<!-- Fin Datos del Recurso -->

		<!-- RecAtrCue -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.recAtrCue.title"/></legend>			
			<table class="tabladatos">
			
				<tr>
					<td><label><bean:message bundle="def" key="def.recAtrCue.esRequerido.label"/>: </label></td>
					<td class="normal">
						<bean:write name="recAtrCueAdapterVO" property="recAtrCueDefinition.recAtrCue.esRequerido.value"/>
					</td>
					<td class="normal"><ul class="vinieta"><li>
						<bean:message bundle="def" key="def.recAtrCue.esRequerido.description"/>
					</li></ul></td>
				</tr>
				
				<tr>
					<!-- Permite visualizacion desde la consulta de deuda -->
					<td><label><bean:message bundle="def" key="def.recAtrCue.esVisConDeu.label"/>: </label></td>
					<td class="normal">
						<bean:write name="recAtrCueAdapterVO" property="recAtrCueDefinition.recAtrCue.esVisConDeu.value"/>
					</td>
					<td class="normal"><ul class="vinieta"><li>
						<bean:message bundle="def" key="def.recAtrCue.esVisConDeu.description"/>
					</li></ul></td>
				</tr>

				<tr>
					<!-- Permite visualizacion desde el recibo -->
					<td><label><bean:message bundle="def" key="def.recAtrCue.esVisRec.label"/>: </label></td>
					<td class="normal">
						<bean:write name="recAtrCueAdapterVO" property="recAtrCueDefinition.recAtrCue.esVisRec.value"/>
					</td>
					<td class="normal"><ul class="vinieta"><li>
						<bean:message bundle="def" key="def.recAtrCue.esVisRec.description"/>
					</li></ul></td>
				</tr>

				<tr>
					<td><label><bean:message bundle="def" key="def.recAtrCue.poseeVigencia.label"/>: </label></td>
					<td class="normal">
						<bean:write name="recAtrCueAdapterVO" property="recAtrCueDefinition.recAtrCue.poseeVigencia.value"/>
					</td>
					<td class="normal"><ul class="vinieta"><li>
						<bean:message bundle="def" key="def.recAtrCue.poseeVigencia.description"/>
					</li></ul></td>
				</tr>
				
				<bean:define id="AtrVal" name="recAtrCueAdapterVO" property="recAtrCueDefinition"/>
				<tr>
					<td><label><u><bean:message bundle="def" key="def.recAtrCue.valorDefecto.label"/></u></label></td>
				<tr>
				
				<tr>
					<td colspan="3">
						<table border="0">
						
							<logic:notEqual name="recAtrCueAdapterVO" property="recAtrCueDefinition.poseeVigencia" value="true">
								<%@ include file="/def/atrDefinition4View.jsp" %>
							</logic:notEqual>
						
							<logic:equal name="recAtrCueAdapterVO" property="recAtrCueDefinition.poseeVigencia" value="true">
								<%@ include file="/def/atrDefinition4ViewWithVig.jsp" %>
							</logic:equal>
						
						</table>
					</td>
				</tr>
				
			</table>
		</fieldset>
		<!-- Fin RecAtrCue -->

		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="recAtrCueAdapterVO" property="act" value="eliminar">
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