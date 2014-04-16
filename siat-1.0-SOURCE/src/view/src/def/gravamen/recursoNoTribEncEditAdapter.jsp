<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
  	    <%@include file="/base/calendar.js"%>   
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/def/AdministrarEncRecurso.do">

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
			
		<!-- Recurso -->

		<!-- Caracteristicas Principales -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.recursoAdapter.fieldset1"/></legend>
			
			<table>
				<tr>
					<!-- Categoria -->										
					<td align="right"><label>(*)&nbsp;<bean:message bundle="def" key="def.categoria.label"/>:</label></td>
					<td colspan="2" class="normal">
						<html:select name="encRecursoAdapterVO" property="recurso.categoria.id" styleClass="select">
							<html:optionsCollection name="encRecursoAdapterVO" property="listCategoria" label="desCategoria" value="id" />
						</html:select>
					</td>
				</tr>
				<tr>
					<!-- Codigo -->
					<td align="right"><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.codRecurso.label"/>:</label></td>
					<td class="normal"><html:text name="encRecursoAdapterVO" property="recurso.codRecurso" size="10" maxlength="10" styleClass="datos"/></td>
				</tr>					
				<tr>
					<!-- Descripcion -->
					<td align="right"><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.desRecurso.label"/>: </label></td>
					<td class="normal"><html:text name="encRecursoAdapterVO" property="recurso.desRecurso" size="25" maxlength="100" styleClass="datos"/></td>					
				</tr>
				<tr>
					<!-- FechaAlta -->
					<td align="right"><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.fechaAlta.label"/>: </label></td>
					<td class="normal">
					<html:text name="encRecursoAdapterVO" property="recurso.fechaAltaView" styleId="fechaAltaView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaAltaView');" id="a_fechaAltaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
				</tr>
			</table>
		</fieldset>
		<!-- Fin Caracteristicas Principales -->
	
				
	<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<td align="right">
					<logic:equal name="encRecursoAdapterVO" property="act" value="modificar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="encRecursoAdapterVO" property="act" value="agregar">
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
