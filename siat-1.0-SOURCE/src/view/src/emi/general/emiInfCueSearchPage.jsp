<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/emi/BuscarEmiInfCue.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="emi" key="emi.emiInfCueSearchPage.title"/></h1>

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="left">
				<p><bean:message bundle="emi" key="emi.emiInfCueSearchPage.legend"/></p>
			</td>						
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
		
	<!-- Filtro -->
	<fieldset>
	<legend><bean:message bundle="base" key="base.parametrosBusqueda"/></legend>
		<table class="tabladatos">
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.emiInfCue.cuenta.label"/>: </label></td>
				<td class="normal">
					<html:text name="emiInfCueSearchPageVO" property="emiInfCue.cuenta.numeroCuenta" 
						size="14" maxlength="10" styleClass="datos" />
				</td>
			</tr>
		</table>
			
		<p align="center">
		  	<html:button property="btnLimpiar"  styleClass="boton" onclick="submitForm('limpiar', '');">
				<bean:message bundle="base" key="abm.button.limpiar"/>
			</html:button>
			&nbsp;
		  	<html:button property="btnBuscar"  styleClass="boton" onclick="submitForm('buscar', '');">
				<bean:message bundle="base" key="abm.button.buscar"/>
			</html:button>
		</p>
	</fieldset>	
	<!-- Fin Filtro -->
		
	<!-- Resultado Filtro -->
	<logic:equal name="emiInfCueSearchPageVO" property="viewResult" value="true">
		<logic:notEmpty  name="emiInfCueSearchPageVO" property="listResult">	
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
				<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
               	<tbody>
					<logic:iterate id="EmiInfCueVO" name="emiInfCueSearchPageVO" property="listResult">
						<tr>
							<td><textarea wrap="off" style="width: 100%; overflow:scroll;"><bean:write name="EmiInfCueVO" property="contenido"/></textarea>&nbsp;
							</td>
						</tr>
					</logic:iterate>
				</tbody>
			</table>
		</logic:notEmpty>
		
		<logic:empty name="emiInfCueSearchPageVO" property="listResult">
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
				<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
               	<tbody>
					<tr><td align="center">
						<bean:message bundle="base" key="base.resultadoVacio"/>
					</td></tr>
				</tbody>			
			</table>
		</logic:empty>
	</logic:equal>			
	<!-- Fin Resultado Filtro -->

	<table class="tablabotones">
		<tr>				
			<td align="left">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
		
	<input type="text" style="display:none"/>
	<input type="hidden" name="method" value=""/>
    <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
    <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
