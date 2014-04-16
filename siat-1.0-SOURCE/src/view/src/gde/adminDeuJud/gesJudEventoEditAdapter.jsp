<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarGesJudEvento.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="gde" key="gde.gesJudAdapter.title"/></h1>	
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		<!-- GesJud -->
	 	<bean:define id="act" name="gesJudEventoAdapterVO" property="act"/>
	 	<bean:define id="gesJud" name="gesJudEventoAdapterVO" property="gesJudEvento.gesJud"/>	 	
		<%@ include file="/gde/adminDeuJud/includeGesJud.jsp" %>
		<!-- GesJud -->
	
		<!-- GesJudEvento -->
			<fieldset>
				<legend><bean:message bundle="gde" key="gde.evento.title"/></legend>
				<table class="tabladatos">
					<tr>
						<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.evento.label"/>:</label></td>
						<td class="normal">
							<html:select name="gesJudEventoAdapterVO" property="gesJudEvento.evento.id" styleClass="select">
								<html:optionsCollection name="gesJudEventoAdapterVO" property="listEvento" label="descripcion" value="id" />
							</html:select>							
						</td>	
					</tr>
					<tr>
						<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.gesJudEvento.fechaEvento.label"/>:</label></td>
						<td class="normal">
							<html:text name="gesJudEventoAdapterVO" property="gesJudEvento.fechaEventoView" styleId="fechaEventoView" size="15" maxlength="10" styleClass="datos" />
							<a class="link_siat" onclick="return show_calendar('fechaEventoView');" id="a_fechaEventoView">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/>
							</a>						
						</td>						
					</tr>
					<tr>
						<td><label><bean:message bundle="gde" key="gde.gesJudEvento.observaciones.label"/>:</label></td>
						<td class="normal"><html:textarea name="gesJudEventoAdapterVO" property="gesJudEvento.observacion" cols="80" rows="15"/></td>
					</tr>
				</table>
		
		<!-- FIN GesJudEvento -->
		
		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="gesJudEventoAdapterVO" property="act" value="agregar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('agregar', '');">
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
