<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarPlanDescuento.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="gde" key="gde.planDescuentoEditAdapter.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right"><html:button property="btnVolver"
				styleClass="boton" onclick="submitForm('volver', '');">
				<bean:message bundle="base" key="abm.button.volver" />
			</html:button></td>
		</tr>
	</table>
	<!-- PlanDescuento -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.planDescuento.title"/></legend>
		
		<table class="tabladatos">
			<!-- desPlan -->
			<tr>
				<td><label><bean:message bundle="gde" key="gde.plan.desPlan.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="planDescuentoAdapterVO" property="planDescuento.plan.desPlan" /></td>			
			</tr>
			<!-- recurso -->
			<tr>
				<!-- viaDeuda -->
				<td><label><bean:message bundle="def" key="def.viaDeuda.label"/>: </label></td>
				<td class="normal"><bean:write name="planDescuentoAdapterVO" property="planDescuento.plan.viaDeuda.desViaDeuda"/></td>
			</tr>
			<logic:equal name="planDescuentoAdapterVO" property="esEditable" value="true">
				<!-- cantidadCuotasPlan -->
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.planDescuento.cantidadCuotasPlan.label"/>: </label></td>
					<td class="normal"><html:text name="planDescuentoAdapterVO" property="planDescuento.cantidadCuotasPlanView" size="20" maxlength="100"/></td>			
				</tr>
				<!-- porDesCap -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.planDescuento.porDesCap.label"/>: </label></td>
					<td class="normal"><html:text name="planDescuentoAdapterVO" property="planDescuento.porDesCapView" size="20" maxlength="100"/></td>	
				</tr>		
					<!-- porDesAct -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.planDescuento.porDesAct.label"/>: </label></td>
					<td class="normal"><html:text name="planDescuentoAdapterVO" property="planDescuento.porDesActView" size="20" maxlength="100"/></td>			
				</tr>
				<!-- porDesInt -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.planDescuento.porDesInt.label"/>: </label></td>
					<td class="normal"><html:text name="planDescuentoAdapterVO" property="planDescuento.porDesIntView" size="20" maxlength="100"/></td>			
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.planDescuento.aplTotImp.label"/>: </label> 
					<td class=normal>
						<html:select name="planDescuentoAdapterVO" property="planDescuento.aplTotImp" styleClass="select">
							<html:optionsCollection name="planDescuentoAdapterVO" 
								property="listSiNo" label="value" value="id" />
						</html:select>
					</td>
				</tr>
			</logic:equal>
			<logic:notEqual name="planDescuentoAdapterVO" property="esEditable" value="true">
				<!-- cantidadCuotasPlan -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.planDescuento.cantidadCuotasPlan.label"/>: </label></td>
					<td class="normal"><html:text name="planDescuentoAdapterVO" property="planDescuento.cantidadCuotasPlanView" disabled="true" size="20" maxlength="100"/></td>			
				</tr>
				<!-- porDesCap -->
				<tr>
					<td><label><bean:message bundle="gde"  key="gde.planDescuento.porDesCap.label"/>: </label></td>
					<td class="normal"><html:text name="planDescuentoAdapterVO" property="planDescuento.porDesCapView" disabled="true" size="20" maxlength="100"/></td>	
				</tr>		
					<!-- porDesAct -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.planDescuento.porDesAct.label"/>: </label></td>
					<td class="normal"><html:text name="planDescuentoAdapterVO" property="planDescuento.porDesActView" disabled="true" size="20" maxlength="100"/></td>			
				</tr>
				<!-- porDesInt -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.planDescuento.porDesInt.label"/>: </label></td>
					<td class="normal"><html:text name="planDescuentoAdapterVO" property="planDescuento.porDesIntView" disabled="true" size="20" maxlength="100"/></td>			
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.planDescuento.aplTotImp.label"/>: </label>
					<td class="normal">
						<html:select name="planDescuentoAdapterVO" property="planDescuento.aplTotImp" styleClass="select">
							<html:optionsCollection name="planDescuentoAdapterVO" 
								property="listSiNo" label="value" value="id" />
						</html:select>
					</td>
				</tr>
			</logic:notEqual>
			<!-- fechaDesde -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.planDescuento.fechaDesde.label"/>: </label></td>
				<td class="normal">
					<html:text name="planDescuentoAdapterVO" property="planDescuento.fechaDesdeView" styleId="fechaDesdeView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
				<!-- fechaHasta -->
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.planDescuento.fechaHasta.label"/>: </label></td>
				<td class="normal">
					<html:text name="planDescuentoAdapterVO" property="planDescuento.fechaHastaView" styleId="fechaHastaView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
			<!-- <#Campos#> -->
		</table>
	</fieldset>	
	<!-- PlanDescuento -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="planDescuentoAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="planDescuentoAdapterVO" property="act" value="agregar">
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
