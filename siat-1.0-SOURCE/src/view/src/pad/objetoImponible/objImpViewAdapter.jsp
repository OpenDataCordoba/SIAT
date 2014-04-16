<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/pad/AdministrarObjImp.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>

	<h1><bean:message bundle="pad" key="pad.objImpViewAdapter.title"/></h1>	
	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
		
	<!-- ObjImp -->
	<fieldset>
		<legend><bean:message bundle="pad" key="pad.objImp.title"/></legend>
		
		<table class="tabladatos">
			<!-- Tipo Objeto Imponible -->
			<tr>
				<td><label><bean:message bundle="def" key="def.tipObjImp.label"/>: </label></td>
				<td class="normal">
					<bean:write name="objImpAdapterVO" property="objImp.tipObjImp.desTipObjImp"/>
				</td>
			</tr>
			
			<!-- Clave Funcional -->
			<logic:notEmpty name="objImpAdapterVO" property="tipObjImpDefinition.listTipObjImpAtrDefinition">
				<logic:iterate id="TipObjImpAtrDefinition" name="objImpAdapterVO" property="tipObjImpDefinition.listTipObjImpAtrDefinition" indexId="count">
					<bean:define id="AtrVal" name="TipObjImpAtrDefinition"/>
					
					<logic:equal name="AtrVal" property="esClaveFuncional" value="true">
						<%@ include file="/def/atrDefinition4View.jsp" %>
					</logic:equal>
				</logic:iterate>
			</logic:notEmpty>
			
			<tr>
				<!-- Fecha Alta-->
				<td><label><bean:message bundle="pad" key="pad.objImp.fechaAlta.label"/>: </label></td>
				<td class="normal">
					<bean:write name="objImpAdapterVO" property="objImp.fechaAltaView"/>
				</td>

				<!-- Fecha Baja-->				
				<logic:equal name="objImpAdapterVO" property="act" value="desactivar">
				 	<td><label>(*)<bean:message bundle="pad" key="pad.objImp.fechaBaja.label"/>:</label></td>					
					<td class="normal">
						<html:text name="objImpAdapterVO" property="objImp.fechaBajaView" styleId="fechaBajaView" size="15" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaBajaView');" id="a_fechaBajaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>					
				</logic:equal>
				<logic:notEqual name="objImpAdapterVO" property="act" value="desactivar">
				 	<td><label><bean:message bundle="pad" key="pad.objImp.fechaBaja.label"/>:</label></td>
				 	<td class="normal"><bean:write name="objImpAdapterVO" property="objImp.fechaBajaView"/></td>
				</logic:notEqual>
			</tr>
			
		</table>
	</fieldset>	
	<!-- ObjImp -->
	
	<fieldset>
		<legend><bean:message bundle="pad" key="pad.objImpViewAdapter.atributosObjImp"/></legend>
		
		<logic:notEmpty name="objImpAdapterVO" property="tipObjImpDefinition.listTipObjImpAtrDefinition">
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
				<tr>
					<th align="left"><bean:message bundle="def" key="def.atributo.label"/></th>
					<th align="left">Valor</th>
					<th align="left">Vigencia</th>
				</tr>	
				<logic:iterate id="TipObjImpAtrDefinition" name="objImpAdapterVO" property="tipObjImpDefinition.listTipObjImpAtrDefinition" indexId="count">
					<logic:equal name="TipObjImpAtrDefinition" property="esVisible" value="true">
					<bean:define id="AtrVal" name="TipObjImpAtrDefinition"/>
					<tr>	
						<%@ include file="/def/atrDefinitionView4Edit.jsp" %>
					</tr>
					</logic:equal>
				</logic:iterate>
			</table>
		</logic:notEmpty>
	</fieldset>
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="objImpAdapterVO" property="act" value="eliminar">
					<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
						<bean:message bundle="base" key="abm.button.eliminar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="objImpAdapterVO" property="act" value="activar">
					<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
						<bean:message bundle="base" key="abm.button.activar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="objImpAdapterVO" property="act" value="desactivar">
					<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
						<bean:message bundle="base" key="abm.button.desactivar"/>
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