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

		<!-- Datos del Recurso -->
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
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recClaDeu.desClaDeu.label"/>: </label></td>
					<td class="normal"><html:text name="recClaDeuAdapterVO" property="recClaDeu.desClaDeu" size="10" maxlength="100"/></td>				
					<td><label><bean:message bundle="def" key="def.recClaDeu.abrClaDeu.label"/>: </label></td>
					<td class="normal"><html:text name="recClaDeuAdapterVO" property="recClaDeu.abrClaDeu" size="10" maxlength="10"/></td>
				</tr>
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recClaDeu.fechaDesde.label"/>: </label></td>
					<td class="normal">
						<html:text name="recClaDeuAdapterVO" property="recClaDeu.fechaDesdeView" styleId="fechaDesdeView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
					<td><label><bean:message bundle="def" key="def.recClaDeu.fechaHasta.label"/>: </label></td>
					<td class="normal">
						<html:text name="recClaDeuAdapterVO" property="recClaDeu.fechaHastaView" styleId="fechaHastaView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
				</tr>
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recClaDeu.esOriginal.label"/>: </label></td>
					<td class="normal">
						<html:select name="recClaDeuAdapterVO" property="recClaDeu.esOriginal.id" styleClass="select">
							<html:optionsCollection name="recClaDeuAdapterVO" property="listSiNo" label="value" value="id" />
						</html:select>
					</td>
									
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recClaDeu.actualizaDeuda.label"/>: </label></td>
					<td class="normal">
						<html:select name="recClaDeuAdapterVO" property="recClaDeu.actualizaDeuda.id" styleClass="select">
							<html:optionsCollection name="recClaDeuAdapterVO" property="listSiNo" label="value" value="id" />
						</html:select>
					</td>
				</tr>
				
			</table>
		</fieldset>
		<!-- Fin RecClaDeu -->


	<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
		   	    		<bean:message bundle="base" key="abm.button.volver"/>
		   	    	</html:button>
	   	    	</td>
   	    		<td align="right" width="50%">	   	    	
					<logic:equal name="recClaDeuAdapterVO" property="act" value="modificar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="recClaDeuAdapterVO" property="act" value="agregar">
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
	