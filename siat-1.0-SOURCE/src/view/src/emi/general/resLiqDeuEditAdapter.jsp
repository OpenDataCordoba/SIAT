<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/emi/AdministrarResLiqDeu.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="emi" key="emi.resLiqDeuEditAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- ResLiqDeu -->
	<fieldset>
		<legend><bean:message bundle="emi" key="emi.resLiqDeu.title"/></legend>
		
		<table class="tabladatos">
			<tr>
				<!-- Recurso -->
				<logic:equal name="resLiqDeuAdapterVO" property="act" value="modificar">
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal" colspan="4">
						<bean:write name="resLiqDeuAdapterVO" property="resLiqDeu.recurso.desRecurso"/>
					</td>
				</logic:equal>
				<logic:equal name="resLiqDeuAdapterVO" property="act" value="agregar">
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal" colspan="4">
						<html:select name="resLiqDeuAdapterVO" property="resLiqDeu.recurso.id" styleClass="select" onchange="submitForm('paramRecurso', '');">
							<bean:define id="includeRecursoList" name="resLiqDeuAdapterVO" property="listRecurso"/>
							<bean:define id="includeIdRecursoSelected" name="resLiqDeuAdapterVO" property="resLiqDeu.recurso.id"/>
							<%@ include file="/def/gravamen/includeRecurso.jsp" %>
						</html:select>
					</td>
				</logic:equal>
			</tr>

			<tr>
				<!-- Fecha de Analisis -->
				<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.resLiqDeu.fechaAnalisis.label"/>: </label></td>

				<td class="normal">
					<html:text name="resLiqDeuAdapterVO" property="resLiqDeu.fechaAnalisisView" styleId="fechaAnalisisView" size="10" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaAnalisisView');" id="a_fechaAnalisisView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
			
			<logic:notEqual name="resLiqDeuAdapterVO" property="resLiqDeu.esAlfaxView" value="true">
				<tr>
					<!-- Anio -->
					<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.impMasDeu.anio.label"/>: </label></td>
					<td class="normal"><html:text name="resLiqDeuAdapterVO" property="resLiqDeu.anioView" size="4" maxlength="4"/></td>			
				</tr>
				<tr>
					<!-- Periodo Desde -->
					<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.resLiqDeu.periodoDesde.label"/>: </label></td>
					<td class="normal"><html:text name="resLiqDeuAdapterVO" property="resLiqDeu.periodoDesdeView" size="2" maxlength="2"/></td>			
	
					<!-- Periodo Hasta -->
					<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.resLiqDeu.periodoHasta.label"/>: </label></td>
					<td class="normal"><html:text name="resLiqDeuAdapterVO" property="resLiqDeu.periodoHastaView" size="2" maxlength="2"/></td>			
				</tr>
			</logic:notEqual>
		</table>
	</fieldset>	
	<!-- ResLiqDeu -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="resLiqDeuAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="resLiqDeuAdapterVO" property="act" value="agregar">
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
<!-- resLiqDeuEditAdapter.jsp -->