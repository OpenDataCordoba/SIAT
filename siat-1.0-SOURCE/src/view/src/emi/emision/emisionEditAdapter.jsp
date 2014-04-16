<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/emi/AdministrarEmision.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<logic:equal name="emisionAdapterVO" property="emision.tipoEmision.esImpresionCdm" value="true">
		<h1><bean:message bundle="emi" key="emi.emisionEditAdapter.impresion.title"/></h1>	
	</logic:equal>
	
	<logic:notEqual name="emisionAdapterVO" property="emision.tipoEmision.esImpresionCdm" value="true">
		<h1><bean:message bundle="emi" key="emi.emisionEditAdapter.title"/></h1>	
	</logic:notEqual>

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- Emision -->
	<fieldset>

		<legend><bean:message bundle="emi" key="emi.emision.title"/></legend>	

		<table class="tabladatos">
			<!-- Recurso -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="emisionAdapterVO" property="emision.recurso.id" styleClass="select" onchange="submitForm('paramRecurso', '')" >
						<bean:define id="includeRecursoList" name="emisionAdapterVO" property="listRecurso"/>
						<bean:define id="includeIdRecursoSelected" name="emisionAdapterVO" property="emision.idRecurso"/>
						<%@ include file="/def/gravamen/includeRecurso.jsp" %>
					</html:select>
				</td>				
			</tr>
			<!-- Fecha Emision -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.emision.label"/>: </label></td>
				<td class="normal">
					<html:text name="emisionAdapterVO" property="emision.fechaEmisionView" styleId="fechaEmisionView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaEmisionView');" id="a_fechaEmisionView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
		</table>
	</fieldset>

<logic:equal name="emisionAdapterVO" property="emision.tipoEmision.esEmisionCdm" value="true">
	<!-- Emision CdM 	 -->
		<%@ include file="/emi/emision/emisionCdMEditAdapter.jsp" %>
	<!-- Fin Emision CdM -->
</logic:equal>

<logic:equal name="emisionAdapterVO" property="emision.tipoEmision.esImpresionCdm" value="true" >
	<!-- Impresion CdM 	 -->
	<%@ include file="/emi/emision/impresionCdMEditAdapter.jsp" %>
	<!-- Fin Impresion CdM -->
</logic:equal>

<logic:equal name="emisionAdapterVO" property="emision.tipoEmision.esEmisionCorCdm" value="true">
	<!-- Emision Corregida CdM 	 -->
		<%@ include file="/emi/emision/emisionCorCdMEditAdapter.jsp" %>
	<!-- Fin Emision Corregida CdM -->
</logic:equal>

	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="emisionAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="emisionAdapterVO" property="act" value="agregar">
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
	<input type="hidden" name="idTipoEmision" value="<bean:write name="emisionAdapterVO" property="emision.tipoEmision.id" bundle="base" formatKey="general.format.id"/>"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->