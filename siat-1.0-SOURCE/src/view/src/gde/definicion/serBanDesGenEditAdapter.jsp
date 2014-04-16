<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>
   	    <%@include file="/base/calendar.js"%>
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/gde/AdministrarSerBanDesGen.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="gde" key="gde.serBanDesGenAdapter.title"/></h1>
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right"><html:button property="btnVolver"
				styleClass="boton" onclick="submitForm('volver', '');">
				<bean:message bundle="base" key="abm.button.volver" />
			</html:button></td>
		</tr>
	</table>
	<!-- Servicio Banco Enc -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.servicioBanco.title"/></legend>
			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="def" key="def.servicioBanco.codServicioBanco.ref"/>: </label></td>
					<td class="normal"><bean:write name="serBanDesGenAdapterVO" property="serBanDesGen.servicioBanco.codServicioBanco"/></td>
			
					<td><label><bean:message bundle="def" key="def.servicioBanco.desServicioBanco.ref"/>: </label></td>
					<td class="normal"><bean:write name="serBanDesGenAdapterVO" property="serBanDesGen.servicioBanco.desServicioBanco"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- Servicio Banco Enc -->		
		
		<!-- SerBanDesGen -->				
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.serBanDesGen.title"/></legend>
			
			<table class="tabladatos">
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.serBanDesGen.desDesGen.label"/>: </label></td>
					<td class="normal">
						<html:select name="serBanDesGenAdapterVO" property="serBanDesGen.desGen.id" styleClass="select" >
								<html:optionsCollection name="serBanDesGenAdapterVO" property="listDesGen" label="desDesGen" value="id" />
						</html:select>
					</td>
				</tr>
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.serBanDesGen.fechaDesde.label"/>: </label></td>
					<td class="normal">
						<html:text name="serBanDesGenAdapterVO" property="serBanDesGen.fechaDesdeView" styleId="fechaDesdeView" size="15" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.serBanDesGen.fechaHasta.label"/>: </label></td>
					<td class="normal">
						<html:text name="serBanDesGenAdapterVO" property="serBanDesGen.fechaHastaView" styleId="fechaHastaView" size="15" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
				</tr>				
			</table>
		</fieldset>	
		<!-- SerBanDesGen -->		

	<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
		   	    		<bean:message bundle="base" key="abm.button.volver"/>
		   	    	</html:button>
	   	    	</td>
   	    		<td align="right" width="50%">	   	    	
					<logic:equal name="serBanDesGenAdapterVO" property="act" value="modificar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="serBanDesGenAdapterVO" property="act" value="agregar">
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
		