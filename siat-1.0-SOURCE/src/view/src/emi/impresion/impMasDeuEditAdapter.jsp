<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/emi/AdministrarImpMasDeu.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="emi" key="emi.impMasDeuEditAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- ImpMasDeu -->
	<fieldset>
		<legend><bean:message bundle="emi" key="emi.impMasDeu.title"/></legend>
		
		<table class="tabladatos">
			<tr>
				<!-- Recurso -->
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="impMasDeuAdapterVO" property="impMasDeu.recurso.id" styleClass="select" onchange="submitForm('paramRecurso', '');">
						<bean:define id="includeRecursoList" name="impMasDeuAdapterVO" property="listRecurso"/>
						<bean:define id="includeIdRecursoSelected" name="impMasDeuAdapterVO" property="impMasDeu.recurso.id"/>
						<%@ include file="/def/gravamen/includeRecurso.jsp" %>
					</html:select>
				</td>				
			</tr>

			<logic:equal name="impMasDeuAdapterVO" property="selectAtrValEnabled" value="true">
				<tr>                  
				    <!--  Atributo -->        
					<td><label><bean:write name="impMasDeuAdapterVO" property="impMasDeu.atributo.desAtributo"/>: </label></td>
					<td class="normal">
					    <html:select name="impMasDeuAdapterVO" property="impMasDeu.atrValor" styleClass="select">
							<html:optionsCollection name="impMasDeuAdapterVO" property="genericAtrDefinition.atributo.domAtr.listDomAtrVal" label="desValor" value="valor" />
						</html:select>
					</td>                                        
				</tr>
			 </logic:equal>

			<tr>
				<!-- Formato de salida -->	
				<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.impMasDeu.formatoSalida.label"/>: </label></td>
				<td class="normal">
					<html:select name="impMasDeuAdapterVO" property="impMasDeu.formatoSalida.id" styleClass="select">
						<html:optionsCollection name="impMasDeuAdapterVO" property="listFormatoSalida" label="value" value="id" />
					</html:select>
				</td>					
			</tr>

			<tr>
				<!-- Anio -->
				<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.impMasDeu.anio.label"/>: </label></td>
				<td class="normal"><html:text name="impMasDeuAdapterVO" property="impMasDeu.anioView" size="4" maxlength="4"/></td>			
			</tr>

			<tr>
				<!-- Periodo Desde -->
				<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.impMasDeu.periodoDesde.label"/>: </label></td>
				<td class="normal"><html:text name="impMasDeuAdapterVO" property="impMasDeu.periodoDesdeView" size="4" maxlength="4"/></td>			

				<!-- Periodo Hasta -->
				<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.impMasDeu.periodoHasta.label"/>: </label></td>
				<td class="normal"><html:text name="impMasDeuAdapterVO" property="impMasDeu.periodoHastaView" size="4" maxlength="4"/></td>			
			</tr>

			<tr>
				<!-- Aplicar criterios de reparto -->
				<td><label><bean:message bundle="emi" key="emi.impMasDeu.abrirPorBroche.label"/>: </label></td>
				<td class="normal">
					<html:select name="impMasDeuAdapterVO" property="impMasDeu.abrirPorBroche.id" styleClass="select" >
						<html:optionsCollection name="impMasDeuAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>
				<td class="normal" colspan="2">
					<ul class="vinieta">
						<li><bean:message bundle="emi" key="emi.impMasDeu.abrirPorBroche.description"/></li>
					</ul>
				</td>
			</tr>
		</table>
	</fieldset>	
	<!-- ImpMasDeu -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="impMasDeuAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="impMasDeuAdapterVO" property="act" value="agregar">
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
<!-- impMasDeuEditAdapter.jsp -->