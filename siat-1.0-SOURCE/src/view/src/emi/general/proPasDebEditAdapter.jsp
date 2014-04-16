<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/emi/AdministrarProPasDeb.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="emi" key="emi.proPasDebEditAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- ProPasDeb -->
	<fieldset>
		<legend><bean:message bundle="emi" key="emi.proPasDeb.title"/></legend>
		
		<table class="tabladatos">
			<tr>
				<!-- Recurso -->
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal" colspan="4">
					<html:select name="proPasDebAdapterVO" property="proPasDeb.recurso.id" styleClass="select" onchange="submitForm('paramRecurso', '');">
						<bean:define id="includeRecursoList" name="proPasDebAdapterVO" property="listRecurso"/>
						<bean:define id="includeIdRecursoSelected" name="proPasDebAdapterVO" property="proPasDeb.recurso.id"/>
						<%@ include file="/def/gravamen/includeRecurso.jsp" %>
					</html:select>
				</td>
			</tr>

			<logic:equal name="proPasDebAdapterVO" property="selectAtrValEnabled" value="true">
				<tr>                  
				    <!--  Atributo -->        
					<td><label><bean:write name="proPasDebAdapterVO" property="proPasDeb.atributo.desAtributo"/>: </label></td>
						<td class="normal">
					        <html:select name="proPasDebAdapterVO" property="proPasDeb.atrValor" styleClass="select">
						<html:optionsCollection name="proPasDebAdapterVO" property="genericAtrDefinition.atributo.domAtr.listDomAtrVal" label="desValor" value="valor" />
						</html:select>
					</td>                                        
				</tr>
			 </logic:equal>

			<tr>
				<!-- Anio -->
				<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.proPasDeb.anio.label"/>: </label></td>
				<td class="normal"><html:text name="proPasDebAdapterVO" property="proPasDeb.anioView" size="4" maxlength="4"/></td>			
			</tr>
			<tr>
				<!-- Periodo -->
				<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.proPasDeb.periodo.label"/>: </label></td>
				<td class="normal"><html:text name="proPasDebAdapterVO" property="proPasDeb.periodoView" size="2" maxlength="2"/></td>			
			</tr>

			<tr>
				<!-- Fecha Envio -->
				<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.proPasDeb.fechaEnvio.label"/>: </label></td>
				<td class="normal">
					<html:text name="proPasDebAdapterVO" property="proPasDeb.fechaEnvioView" styleId="fechaEnvioView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaEnvioView');" id="a_fechaEnvioView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
		
		</table>
	</fieldset>	
	<!-- ProPasDeb -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="proPasDebAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="proPasDebAdapterVO" property="act" value="agregar">
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
<!-- proPasDebEditAdapter.jsp -->