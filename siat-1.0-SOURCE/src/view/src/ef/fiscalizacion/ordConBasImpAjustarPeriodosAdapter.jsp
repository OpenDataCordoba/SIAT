<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" src="<%= request.getContextPath()%>/base/calculator.js"></script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/ef/AdministrarOrdConBasImp.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="ef" key="ef.ordConBasImpAdapter.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<input type="button" class="boton" onclick="submitForm('volver', '<bean:write name="ordConBasImpAdapterVO" property="ordConBasImp.idView" />');"
		   				value="<bean:message bundle="base" key="abm.button.volver"/>"/>					
				</td>
			</tr>
		</table>
		
		<!-- ordConBasImp -->
		<fieldset>
			<legend><bean:message bundle="ef" key="ef.ordConBasImp.label"/></legend>
			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="ef" key="ef.fuenteInfo.label"/>: </label></td>
					<td class="normal"><bean:write name="ordConBasImpAdapterVO" property="ordConBasImp.compFuente.plaFueDat.fuenteInfo.nombreFuente"/></td>
					
				</tr>
				<tr>
					<td><label><bean:message bundle="ef" key="ef.ordConBasImp.periodoDesde.label"/>: </label></td>
					<td class="normal"><bean:write name="ordConBasImpAdapterVO" property="ordConBasImp.periodoAnioDesdeView"/></td>
					
				</tr>
				<tr>
					<td><label><bean:message bundle="ef" key="ef.ordConBasImp.periodoHasta.label"/>: </label></td>
					<td class="normal"><bean:write name="ordConBasImpAdapterVO" property="ordConBasImp.periodoAnioHastaView"/></td>
					
				</tr>																
			</table>
		</fieldset>	
		<!-- ordConBasImp -->
				
			
		<fieldset>
			<legend><bean:message bundle="ef" key="ef.ordConBasImpAdapter.ajustarPeriodos.legend"/></legend>
			
			<table class="tabladatos">
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="ef" key="ef.ordConBasImpAdapter.actividades.label"/>: </label></td>
					<td class="normal">
						<html:select name="ordConBasImpAdapterVO" property="nroColumnaSelec">
							<html:optionsCollection name="ordConBasImpAdapterVO" property="ordConBasImp.compFuente.listCompFuenteCol" label="colName" value="nroColumna"/>
						</html:select>
					</td>
					
				</tr>
				
				<!-- periodoDesde -->
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="ef" key="ef.ordConBasImp.periodoDesde.label"/>: </label></td>
					<td class="normal">
						<html:text name="ordConBasImpAdapterVO" property="periodoDesde" maxlength="2" size="4"/>/
						<html:text name="ordConBasImpAdapterVO" property="anioDesde" maxlength="4" size="4"/>						
					</td>
					
				</tr>
				
				<!-- periodoHasta -->
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="ef" key="ef.ordConBasImp.periodoHasta.label"/>: </label></td>
					<td class="normal">
						<html:text name="ordConBasImpAdapterVO" property="periodoHasta" maxlength="2" size="4"/>/					
						<html:text name="ordConBasImpAdapterVO" property="anioHasta" maxlength="4" size="4"/>
					</td>					
				</tr>
				
				<!-- total ajustar -->
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="ef" key="ef.ordConBasImpAdapter.totalAjustar.label"/>: </label></td>
					<td class="normal">
						<html:text name="ordConBasImpAdapterVO" property="totalAjustar"/>
					</td>					
				</tr>
			</table>
		</fieldset>				

				
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left" width="50%">
		   			<input type="button" class="boton" onclick="submitForm('volver', '<bean:write name="ordConBasImpAdapterVO" property="ordConBasImp.idView" />');"
		   				value="<bean:message bundle="base" key="abm.button.volver"/>"/>
				</td>   	    
					
				<td align="right" width="50%">
		   			<input type="button" class="boton" onclick="submitForm('ajustarPeriodos', '<bean:write name="ordConBasImpAdapterVO" property="ordConBasImp.ordenControl.idView" />');"
		   				value="<bean:message bundle="ef" key="ef.ordConBasImpAdapter.button.ajustarPeriodo"/>"/>
				</td>								
			</tr>
		</table>
	   	
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
		<logic:present name="irA">
			<script type="text/javascript">document.location = document.URL + '#<bean:write name="irA"/>';</script>
		</logic:present>		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
<!-- ordConBasImpAdapter.jsp -->