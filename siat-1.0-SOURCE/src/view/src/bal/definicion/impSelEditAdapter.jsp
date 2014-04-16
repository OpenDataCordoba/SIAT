<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/AdministrarImpSel.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="bal" key="bal.impSelEditAdapter.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	<!-- Importe Sellado -->
	<fieldset>
		<legend><bean:message bundle="bal" key="bal.impSel.title"/></legend>
		
		<table class="tabladatos">
		<!-- Sellado -->		
		<tr>
			<td><label><bean:message bundle="bal" key="bal.sellado.codSellado.ref"/>: </label></td>
			<td class="normal">
				<bean:write name="impSelAdapterVO" property="impSel.sellado.codSellado"/>				
			</td>
			<td><label><bean:message bundle="bal" key="bal.sellado.desSellado.ref"/>: </label></td>
			<td class="normal">
				<bean:write name="impSelAdapterVO" property="impSel.sellado.desSellado"/>				
			</td>
		</tr>

		
		<!-- fechaDesde -->
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.impSel.fechaDesde.label"/>: </label></td>
			<td class="normal">
				<html:text name="impSelAdapterVO" property="impSel.fechaDesdeView" styleId="fechaDesdeView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>

		<!-- fechaHasta -->

			<td><label><bean:message bundle="bal" key="bal.impSel.fechaHasta.label"/>: </label></td>
			<td class="normal">
				<html:text name="impSelAdapterVO" property="impSel.fechaHastaView" styleId="fechaHastaView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>
		</tr>
		<!-- Tipo Sellado -->
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.impSel.tipoSellado.label"/>: </label></td>
			<td class="normal">
				<html:select name="impSelAdapterVO" property="impSel.tipoSellado.id" styleClass="select" >
					<html:optionsCollection name="impSelAdapterVO" property="listTipoSellado" label="desTipoSellado" value="id" />
				</html:select>
			</td>
	    
		<!-- costo -->
		
			<td><label><bean:message bundle="bal" key="bal.impSel.costo.label"/>: </label></td>
			<td class="normal"><html:text name="impSelAdapterVO" property="impSel.costoView" size="20" maxlength="100"/></td>			
		</tr>
			<!-- <#Campos#> -->
		</table>
	</fieldset>	
	<!-- Importe Sellado -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="impSelAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="impSelAdapterVO" property="act" value="agregar">
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
