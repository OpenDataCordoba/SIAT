<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/AdministrarCompFuente.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="ef" key="ef.compFuenteEditAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- CompFuente -->
	<fieldset>
		<legend><bean:message bundle="ef" key="ef.compFuente.title"/></legend>
		
		<table class="tabladatos">
		<!-- PlaFueDat -->
		<tr>	
			<td><label>(*)&nbsp;<bean:message bundle="ef" key="ef.plaFueDat.label"/>: </label></td>
			<td class="normal">
				<html:select name="compFuenteAdapterVO" property="compFuente.plaFueDat.id" styleClass="select" onchange="submitForm('paramFuenteInfo', '');">
					<html:optionsCollection name="compFuenteAdapterVO" property="listPlaFueDat" label="tituloView" value="id" />
				</html:select>
			</td>					
		</tr>

		<!-- mensual -->		
		<logic:equal name="compFuenteAdapterVO" property="compFuente.plaFueDat.fuenteInfo.tipoPeriodicidad.id" value="0"> 
			<!-- periodoDesde -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="ef" key="ef.compFuente.periodoDesde.label"/>: </label></td>
				<td class="normal">
					<html:text name="compFuenteAdapterVO" property="compFuente.periodoDesde" size="2" maxlength="2"/>
					&nbsp;/&nbsp;
	
			<!-- anioDesde -->
					<html:text name="compFuenteAdapterVO" property="compFuente.anioDesde" size="4" maxlength="4"/>
					&nbsp;(mm/aaaa)				
				</td>			
			</tr>
	
			<!-- periodoHasta -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="ef" key="ef.compFuente.periodoHasta.label"/>: </label></td>
				<td class="normal">
					<html:text name="compFuenteAdapterVO" property="compFuente.periodoHasta" size="2" maxlength="2"/>
					&nbsp;/&nbsp;				
			<!-- anioHasta -->
			
					<html:text name="compFuenteAdapterVO" property="compFuente.anioHasta" size="4" maxlength="4"/>
					&nbsp;(mm/aaaa)			
				</td>			
			</tr>
		</logic:equal>
		
		<!-- anual -->		
		<logic:equal name="compFuenteAdapterVO" property="compFuente.plaFueDat.fuenteInfo.tipoPeriodicidad.id" value="1"> 
			<!-- anioDesde -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="ef" key="ef.compFuente.anioDesde.label"/>: </label></td>
				<td class="normal">
					<html:text name="compFuenteAdapterVO" property="compFuente.anioDesde" size="5" maxlength="4"/>				
				</td>			
			</tr>
	
			<!-- anioHasta -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="ef" key="ef.compFuente.anioHasta.label"/>: </label></td>
				<td class="normal">
					<html:text name="compFuenteAdapterVO" property="compFuente.anioHasta" size="5" maxlength="4"/>			
				</td>			
			</tr>
		</logic:equal>		
			<!-- <#Campos#> -->
		</table>
	</fieldset>	
	<!-- CompFuente -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="compFuenteAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="compFuenteAdapterVO" property="act" value="agregar">
		   			<input type="button" class="boton" onclick="submitForm('agregar', '<bean:write name="compFuenteAdapterVO" property="compFuente.comparacion.id" bundle="base" formatKey="general.format.id"/>');"
		   				value="<bean:message bundle="base" key="abm.button.agregar"/>"/>		   									

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
<!-- compFuenteEditAdapter.jsp -->
