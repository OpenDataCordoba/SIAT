<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>
   	    <%@include file="/base/calendar.js"%>
</script>

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
			
			<!-- Fecha Alta-->
			<tr>
				<td><label><bean:message bundle="pad" key="pad.objImp.fechaAlta.label"/>: </label></td>
				<td class="normal">
					<bean:write name="objImpAdapterVO" property="objImp.fechaAltaView"/>
				</td>
			</tr>
			
			
		</table>
	</fieldset>	
	<!-- ObjImp -->
	
	<fieldset>
		<legend><bean:message bundle="pad" key="pad.objImpViewAdapter.atributosObjImp"/></legend>
		
		<logic:notEmpty name="objImpAdapterVO" property="tipObjImpDefinition.listTipObjImpAtrDefinition">
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
				
				<tr>
					<th width="1">&nbsp;</th> <!-- Ver -->
					<th width="1">&nbsp;</th> <!-- Modificar -->
					<th align="left"><bean:message bundle="def" key="def.atributo.label"/></th>
					<th align="left">Valor</th>
					<th align="left">Vigencia</th>
				</tr>
					
				<logic:iterate id="TipObjImpAtrDefinition" name="objImpAdapterVO" property="tipObjImpDefinition.listTipObjImpAtrDefinition" indexId="count">
					<logic:equal name="TipObjImpAtrDefinition" property="esVisible" value="true">
					<bean:define id="AtrVal" name="TipObjImpAtrDefinition"/>
					
					<tr>
						<!-- Ver -->
						<td>
							<logic:equal name="objImpAdapterVO" property="verObjImpAtrValEnabled" value="enabled">							
								<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verObjImpAtrVal', '<bean:write name="AtrVal" property="idDefinition" bundle="base" formatKey="general.format.id"/>');">
									<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
								</a>
							</logic:equal>
							<logic:notEqual name="objImpAdapterVO" property="verObjImpAtrValEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
							</logic:notEqual>							
						</td>
						<!-- Modificar-->
						<td>
							<!-- Si tiene permiso por swe -->
							<logic:equal name="objImpAdapterVO" property="modificarObjImpAtrValEnabled" value="enabled">								
								<!-- Si no es clave ni clave funcional y es Atributo Siat -->
								<logic:equal name="AtrVal" property="esModificable" value="true">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarObjImpAtrVal', '<bean:write name="AtrVal" property="idDefinition" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
									</a>
								</logic:equal>
								<logic:notEqual name="AtrVal" property="esModificable" value="true">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</logic:equal>
							
							<logic:notEqual name="objImpAdapterVO" property="modificarObjImpAtrValEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
							</logic:notEqual>
						</td>
						
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