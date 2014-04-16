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
		<table border="0">
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.atributo.label"/>: </label></td>
				<td class="normal" colspan="3">
				<html:text name="recAtrCueAdapterVO" property="recAtrCueDefinition.atributo.desAtributo" size="10" maxlength="100" disabled="true"/>
					<logic:equal name="recAtrCueAdapterVO" property="act" value="agregar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('buscarAtributo', '');">
							<bean:message bundle="def" key="def.recAtrAdapterAdapter.adm.button.buscarAtributo"/>
						</html:button>
					</logic:equal>
				</td>
			</tr>
			
			<logic:equal  name="recAtrCueAdapterVO" property="paramAtributo" value="true">
				
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recAtrCue.esRequerido.label"/>: </label></td>
					<td class="normal">
						<html:select name="recAtrCueAdapterVO" property="recAtrCueDefinition.recAtrCue.esRequerido.id" styleClass="select" >
							<html:optionsCollection name="recAtrCueAdapterVO" property="listSiNo" label="value" value="id" />
						</html:select>
					</td>
					<td class="normal" colspan="2"><ul class="vinieta"><li>
						<bean:message bundle="def" key="def.recAtrCue.esRequerido.description"/>
					</li></ul></td>
				</tr>
				
				<tr>
					<!-- Permite visualizacion desde la consulta de deuda -->
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recAtrCue.esVisConDeu.label"/>: </label></td>
					<td class="normal">
						<html:select name="recAtrCueAdapterVO" property="recAtrCueDefinition.recAtrCue.esVisConDeu.id" styleClass="select" >
							<html:optionsCollection name="recAtrCueAdapterVO" property="listSiNo" label="value" value="id" />
						</html:select>
					</td>
					<td class="normal" colspan="2"><ul class="vinieta"><li>
						<bean:message bundle="def" key="def.recAtrCue.esVisConDeu.description"/>
					</li></ul></td>
				</tr>
				
				<tr>
					<!-- Permite visualizacion desde el recibo -->
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recAtrCue.esVisRec.label"/>: </label></td>
					<td class="normal">
						<html:select name="recAtrCueAdapterVO" property="recAtrCueDefinition.recAtrCue.esVisRec.id" styleClass="select" >
							<html:optionsCollection name="recAtrCueAdapterVO" property="listSiNo" label="value" value="id" />
						</html:select>
					</td>
					<td class="normal" colspan="2">
						<ul class="vinieta">
							<li><bean:message bundle="def" key="def.recAtrCue.esVisRec.description"/></li>
						</ul>
					</td>
				</tr>

				<logic:equal name="recAtrCueAdapterVO" property="act" value="agregar">
					<tr>
						<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recAtrCue.poseeVigencia.label"/>: </label></td>
						<td class="normal">
							<html:select name="recAtrCueAdapterVO" property="recAtrCueDefinition.recAtrCue.poseeVigencia.id" styleClass="select" onchange="submitForm('paramPoseeVigencia', '');">
								<html:optionsCollection name="recAtrCueAdapterVO" property="listSiNo" label="value" value="id" />
							</html:select>
						</td>
						<td class="normal" colspan="2"><ul class="vinieta"><li>
							<bean:message bundle="def" key="def.recAtrCue.poseeVigencia.description"/>
						</li></ul></td>
					</tr>
				</logic:equal>
				
				<logic:equal name="recAtrCueAdapterVO" property="act" value="modificar">
					<tr>
						<td><label><bean:message bundle="def" key="def.recAtrCue.poseeVigencia.label"/>: </label></td>
						<td class="normal">
							<bean:write name="recAtrCueAdapterVO" property="recAtrCueDefinition.recAtrCue.poseeVigencia.value"/>
						</td>
						<td class="normal" colspan="2"><ul class="vinieta"><li>
							<bean:message bundle="def" key="def.recAtrCue.poseeVigencia.description"/>
						</li></ul></td>
					</tr>
				</logic:equal>
				
			</logic:equal>
			
			<logic:equal  name="recAtrCueAdapterVO" property="paramPoseeVigencia" value="true">
				
				<tr>
					<td colspan="4" align="center"><label><u><bean:message bundle="def" key="def.recAtrCue.valorDefecto.label"/></u></label></td>
				<tr>
			
				<bean:define id="AtrVal" name="recAtrCueAdapterVO" property="recAtrCueDefinition"/>
				
				<tr>
					<td colspan="4">
						<table border="0">
						
							<logic:notEqual name="recAtrCueAdapterVO" property="recAtrCueDefinition.poseeVigencia" value="true">
								<%@ include file="/def/atrDefinition4Edit.jsp" %>
							</logic:notEqual>
						
							<logic:equal name="recAtrCueAdapterVO" property="recAtrCueDefinition.poseeVigencia" value="true">
								<%@ include file="/def/atrDefinition4EditWithVig.jsp" %>
							</logic:equal>
						
						</table>
					</td>
				</tr>
									
			</logic:equal>
		</table>
	</fieldset>
	<!-- Fin RecAtrCue -->


<table class="tablabotones" width="100%">
    	<tr>
 	    		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
  	    		<td align="right" width="50%">	   	    	
				<logic:equal name="recAtrCueAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="recAtrCueAdapterVO" property="act" value="agregar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
						<bean:message bundle="base" key="abm.button.agregar"/>
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
		
	<!-- Inclusion del Codigo Javascript del Calendar-->
<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
	
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
	