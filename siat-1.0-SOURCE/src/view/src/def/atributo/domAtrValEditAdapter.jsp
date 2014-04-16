<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/def/AdministrarDomAtrVal.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="def" key="def.domAtrValAdapter.title"/></h1>	
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
			
		<!-- DomAtrVal -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.domAtrVal.title"/></legend>
			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="def" key="def.domAtr.codDomAtr.ref"/>: </label></td>
					<td class="normal"><bean:write name="domAtrValAdapterVO" property="domAtrVal.domAtr.codDomAtr"/></td>
			
					<td><label><bean:message bundle="def" key="def.domAtr.desDomAtr.ref"/>: </label></td>
					<td class="normal"><bean:write name="domAtrValAdapterVO" property="domAtrVal.domAtr.desDomAtr"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.tipoAtributo.label"/>: </label></td>
					<td class="normal"><bean:write name="domAtrValAdapterVO" property="domAtrVal.domAtr.tipoAtributo.desTipoAtributo"/></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.domAtrVal.strValor.label"/>: </label></td>
					<td class="normal">
						<html:text name="domAtrValAdapterVO" property="domAtrVal.valor" size="15" maxlength="30" />
					</td>
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.domAtrVal.desValor.label"/>: </label></td>
					<td class="normal">
						<html:text name="domAtrValAdapterVO" property="domAtrVal.desValor" size="20" maxlength="100"/>
					</td>
				</tr>
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.domAtrVal.fechaDesde.label"/>: </label></td>
					<td class="normal">
						<html:text name="domAtrValAdapterVO" property="domAtrVal.fechaDesdeView" styleId="fechaDesdeView" size="15" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
					<td><label><bean:message bundle="def" key="def.domAtrVal.fechaHasta.label"/>: </label></td>
					<td class="normal">
						<html:text name="domAtrValAdapterVO" property="domAtrVal.fechaHastaView" styleId="fechaHastaView" size="15" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
				</tr>				
			</table>
		</fieldset>	
		<!-- DomAtrVal -->
		
		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
		   	    		<bean:message bundle="base" key="abm.button.volver"/>
		   	    	</html:button>
	   	    	</td>
   	    		<td align="right" width="50%">	   	    	
					<logic:equal name="domAtrValAdapterVO" property="act" value="modificar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="domAtrValAdapterVO" property="act" value="agregar">
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
