<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
	    <%@include file="/base/calendar.js"%>   	       	       
</script>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/def/AdministrarEncTipObjImp.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="def" key="def.tipObjImpAdapter.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- TipObjImp -->
	<fieldset>
		<legend><bean:message bundle="def" key="def.tipObjImp.title"/></legend>
		
			<table class="tabladatos">
				<tr>
					<logic:equal name="tipObjImpEncAdapterVO" property="act" value="agregar">
						<td><label>(*)&nbsp;<bean:message bundle="def" key="def.tipObjImp.codTipObjImp.label"/>: </label></td>
						<td class="normal">
							<html:text name="tipObjImpEncAdapterVO" property="tipObjImp.codTipObjImp" size="10" maxlength="10" styleClass="datos" />		
						</td>
					</logic:equal>
					<logic:notEqual name="tipObjImpEncAdapterVO" property="act" value="agregar">
						<td><label><bean:message bundle="def" key="def.tipObjImp.codTipObjImp.label"/>: </label></td>
						<td class="normal"><bean:write name="tipObjImpEncAdapterVO" property="tipObjImp.codTipObjImp"/></td>
					</logic:notEqual>
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.tipObjImp.desTipObjImp.label"/>: </label></td>
					<td class="normal"><html:text name="tipObjImpEncAdapterVO" property="tipObjImp.desTipObjImp" size="20" maxlength="100" styleClass="datos" /></td>
				</tr>
				<tr>
					<!-- EsSiat -->
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.tipObjImp.esSiat.label"/>: </label></td>
					<td class="normal" colspan="1">	
						<html:select name="tipObjImpEncAdapterVO" property="tipObjImp.esSiat.id" styleClass="select" onchange="submitForm('param', '');">
							<html:optionsCollection name="tipObjImpEncAdapterVO" property="listSiNo" label="value" value="id" />                       	
						</html:select>
					</td>
					
					<logic:equal name="tipObjImpEncAdapterVO" property="selectProcesoEnabled" value="true">
	           		<td><label><bean:message bundle="def" key="def.tipObjImp.proceso.label"/>: </label></td>
	               		<td class="normal">
	                   	  <html:select name="tipObjImpEncAdapterVO" property="tipObjImp.proceso.id" styleClass="select">
	                       	       <html:optionsCollection name="tipObjImpEncAdapterVO" property="listProceso" label="codProceso" value="id" />
	                      </html:select>
	               	</td>                                        
					
 		 		 </logic:equal>						
					
				</tr>				
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.tipObjImp.fechaAlta.label"/>: </label></td>
					<td class="normal">
						<html:text name="tipObjImpEncAdapterVO" property="tipObjImp.fechaAltaView" styleId="fechaAltaView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaAltaView');" id="a_fechaAltaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/>
						</a>
					</td>
					<td><label><bean:message bundle="def" key="def.tipObjImp.fechaBaja.label"/>: </label></td>
					<td class="normal"><bean:write name="tipObjImpEncAdapterVO" property="tipObjImp.fechaBajaView"/></td>
				</tr>
			</table>
	</fieldset>	
	<!-- TipObjImp -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="tipObjImpEncAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="tipObjImpEncAdapterVO" property="act" value="agregar">
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

	<!-- Inclucion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
	
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->