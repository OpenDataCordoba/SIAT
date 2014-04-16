<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/AdministrarPlanFiscal.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="ef" key="ef.planFiscalEditAdapter.title"/></h1>	
	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- PlanFiscal -->
	<fieldset>
		<legend><bean:message bundle="ef" key="ef.planFiscal.title"/></legend>
		
		<table class="tabladatos">
		
		<!-- fechaDesde -->
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="ef" key="ef.planFiscal.fechaDesde.label"/>: </label></td>
			<td class="normal">
				<html:text name="planFiscalAdapterVO" property="planFiscal.fechaDesdeView" styleId="fechaDesdeView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>
		<!-- fechaHasta -->
			<td><label><bean:message bundle="ef" key="ef.planFiscal.fechaHasta.label"/>: </label></td>
			<td class="normal">
				<html:text name="planFiscalAdapterVO" property="planFiscal.fechaHastaView" styleId="fechaHastaView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>
		</tr>
		
				<!-- numero -->
		<tr>
			<td><label><bean:message bundle="ef" key="ef.planFiscal.numero.label"/>: </label></td>
			<td class="normal"><html:text name="planFiscalAdapterVO" property="planFiscal.numero" size="15" maxlength="60"/></td>			
		
			<!--  estado Plan -->
			<logic:equal name="planFiscalAdapterVO" property="act" value="modificar">

				<td><label><bean:message bundle="ef" key="ef.estadoPlanFis.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="planFiscalAdapterVO" property="planFiscal.estadoPlanFis.id" styleClass="select">
						<html:optionsCollection name="planFiscalAdapterVO" property="listEstadoPlanFis" label="desEstadoPlanFis" value="id" />
					</html:select>
				</td>					
			</logic:equal>
		</tr>
		
		<!-- desPlanFiscal -->
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="ef" key="ef.planFiscal.desPlanFiscal.label"/>: </label></td>
			<td class="normal" colspan="3"><html:textarea style="height:35px;width:450px" name="planFiscalAdapterVO" property="planFiscal.desPlanFiscal"/></td>			
		</tr>
		<!-- objetivo -->
		<tr>
			<td><label><bean:message bundle="ef" key="ef.planFiscal.objetivo.label"/>: </label></td>
			<td class="normal" colspan="3"><html:textarea name="planFiscalAdapterVO" property="planFiscal.objetivo" style="height:130px;width:450px" /></td>			
		</tr>
		<!-- fundamentos -->
		<tr>
			<td><label><bean:message bundle="ef" key="ef.planFiscal.fundamentos.label"/>: </label></td>
			<td class="normal" colspan="3"><html:textarea name="planFiscalAdapterVO" property="planFiscal.fundamentos" style="height:130px;width:450px" /></td>			
		</tr>
		<!-- propuestas -->
		<tr>
			<td><label><bean:message bundle="ef" key="ef.planFiscal.propuestas.label"/>: </label></td>
			<td class="normal" colspan="3"><html:textarea name="planFiscalAdapterVO" property="planFiscal.propuestas" style="height:130px;width:450px" /></td>			
		</tr>
		<!-- metTrab -->
		<tr>
			<td><label><bean:message bundle="ef" key="ef.planFiscal.metTrab.label"/>: </label></td>
			<td class="normal" colspan="3"><html:textarea name="planFiscalAdapterVO" property="planFiscal.metTrab" style="height:130px;width:450px" /></td>			
		</tr>
		<!-- necesidades -->
		<tr>
			<td><label><bean:message bundle="ef" key="ef.planFiscal.necesidades.label"/>: </label></td>
			<td class="normal" colspan="3"><html:textarea name="planFiscalAdapterVO" property="planFiscal.necesidades" style="height:130px;width:450px" /></td>			
		</tr>
		<!-- resEsp -->
		<tr>
			<td><label><bean:message bundle="ef" key="ef.planFiscal.resEsp.label"/>: </label></td>
			<td class="normal" colspan="3"><html:textarea name="planFiscalAdapterVO" property="planFiscal.resEsp" style="height:130px;width:450px" /></td>			
		</tr>
			<!-- <#Campos#> -->
		</table>
	</fieldset>	
	<!-- PlanFiscal -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="planFiscalAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="planFiscalAdapterVO" property="act" value="agregar">
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
