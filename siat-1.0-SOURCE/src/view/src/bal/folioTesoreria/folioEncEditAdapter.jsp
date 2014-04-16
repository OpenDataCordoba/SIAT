<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/AdministrarEncFolio.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.folioAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
				
		<!-- Folio Enc -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.folio.title"/></legend>
			<table class="tabladatos">
				<!-- Fecha Folio -->
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.folio.fechaFolio.label"/>: </label></td>
					<td class="normal">
						<html:text name="encFolioAdapterVO" property="folio.fechaFolioView" styleId="fechaFolioView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar_change('fechaFolioView');" id="a_fechaFolioView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
				</tr>		
				<!-- Numero -->		
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.folio.numero.label"/>: </label></td>
					<td class="normal"><html:text name="encFolioAdapterVO" property="folio.numeroView" size="10" maxlength="20" styleClass="datos"/></td>					
				</tr>
				<!-- Descripcion -->		
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.folio.descripcion.label"/>: </label></td>
					<td class="normal"><html:text name="encFolioAdapterVO" property="folio.descripcion" size="35" maxlength="100" styleClass="datos"/></td>					
				</tr>
				<!-- Descripcion de Dias de Cobranza-->		
				<tr>
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.folio.desDiaCob.label"/>: </label></td>
					<td class="normal"><html:text name="encFolioAdapterVO" property="folio.desDiaCob" size="35" maxlength="100" styleClass="datos"/></td>					
				</tr>
			</table>
		</fieldset>
		
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<td align="right">
					<logic:equal name="encFolioAdapterVO" property="act" value="modificar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="encFolioAdapterVO" property="act" value="agregar">
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
		