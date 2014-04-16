<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarLiqCom.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				

	<!-- Busqueda de Recuso -->
	<span id="blockBusqueda" style="display:none"> 
		<bean:define id="adapterVO" name="liqComAdapterVO"/>
		<bean:define id="poseeParam" value="true" />
		<%@ include file="/def/gravamen/includeRecursoSearch.jsp" %>
	</span>

	<span id="blockSimple" style="display:block">
		
		<h1><bean:message bundle="gde" key="gde.liqComEditAdapter.title"/></h1>	
		
		<!-- LiqCom -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.liqCom.title"/></legend>
			
			<table class="tabladatos">
			<logic:equal name="liqComAdapterVO" property="modificarBussEnabled" value="true">
					<!-- fechaLiquidacion -->
					<tr>
						<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.liqCom.fechaLiquidacion.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:text name="liqComAdapterVO" property="liqCom.fechaLiquidacionView" styleId="fechaLiquidacionView" size="15" maxlength="10" styleClass="datos" />
							<a class="link_siat" onclick="return show_calendar('fechaLiquidacionView');" id="a_fechaLiquidacionView">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>
					</tr>
					
					<!-- ServicioBanco -->
					<tr>	
						<td><label><bean:message bundle="def" key="def.servicioBanco.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:select name="liqComAdapterVO" property="liqCom.servicioBanco.id" styleClass="select" onchange="submitForm('paramServicioBanco', '');" styleId="cboServicioBanco">
								<html:optionsCollection name="liqComAdapterVO" property="listServicioBanco" label="desServicioBanco" value="id" />
							</html:select>
						</td>					
					</tr>
					
					<!-- Recurso -->
					<tr>	
						<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:select name="liqComAdapterVO" property="liqCom.recurso.id" styleClass="select" onchange="submitForm('paramRecurso', '');" styleId="cboRecurso" style="width:90%">
								<html:optionsCollection name="liqComAdapterVO" property="listRecurso" label="desRecurso" value="id" />
							</html:select>
							
							<a onclick="toggleSearchRecurso('blockSimple', 'blockBusqueda'); setFocus('recursoFilter');" >
								<img title='<bean:message bundle="def" key="def.recurso.filtro.button.busqueda"/>' border="0" 
								src="<%=request.getContextPath()%>/images/iconos/lupita.gif" width="20" height="20">
							</a>
							
						</td>
					</tr>
					<!-- Procurador -->
					<tr>	
						<td><label><bean:message bundle="gde" key="gde.procurador.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:select name="liqComAdapterVO" property="liqCom.procurador.id" styleClass="select">
								<html:optionsCollection name="liqComAdapterVO" property="listProcurador" label="descripcion" value="id" />
							</html:select>
						</td>					
					</tr>
					
					<!-- fechaPagoHasta -->		
					<tr>				
						<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.liqCom.fechaPagoHasta.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:text name="liqComAdapterVO" property="liqCom.fechaPagoHastaView" styleId="fechaPagoHastaView" size="15" maxlength="10" styleClass="datos" />
							<a class="link_siat" onclick="return show_calendar('fechaPagoHastaView');" id="a_fechaPagoHastaView">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>
					</tr>
			
					<!-- Caso -->
					<tr>
						<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
						<td colspan="3">
							<bean:define id="IncludedVO" name="liqComAdapterVO" property="liqCom"/>
							<bean:define id="voName" value="liqCom" />
							<%@ include file="/cas/caso/includeCaso.jsp" %>
						</td>
					</tr>		
							
			</logic:equal>
			<logic:notEqual name="liqComAdapterVO" property="modificarBussEnabled" value="true">
					<!-- fechaLiquidacion -->
					<tr>
						<td><label><bean:message bundle="gde" key="gde.liqCom.fechaLiquidacion.label"/>: </label></td>
						<td class="normal" colspan="3">
							<bean:write name="liqComAdapterVO" property="liqCom.fechaLiquidacionView"/>
						</td>
					</tr>
					<!-- Recurso -->
					<tr>	
						<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
						<td class="normal" colspan="3">
							<bean:write name="liqComAdapterVO" property="liqCom.recurso.desRecurso"/>
						</td>					
					</tr>
					<!-- Procurador -->
					<tr>	
						<td><label><bean:message bundle="gde" key="gde.procurador.label"/>: </label></td>
						<td class="normal" colspan="3">
							<bean:write name="liqComAdapterVO" property="liqCom.procurador.descripcion"/>
						</td>					
					</tr>
	
					<tr>
					
					<!-- fechaPagoHasta -->		
						<td><label><bean:message bundle="gde" key="gde.liqCom.fechaPagoHasta.label"/>: </label></td>
						<td class="normal">
							<bean:write name="liqComAdapterVO" property="liqCom.fechaPagoHastaView"/>
						</td>
					</tr>
			
					<!-- Caso -->
					<tr>
						<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
						<td colspan="3">
							<bean:define id="IncludedVO" name="liqComAdapterVO" property="liqCom"/>						
							<%@ include file="/cas/caso/includeCasoView.jsp" %>
						</td>
					</tr>		
							
			</logic:notEqual>
					<tr>
						<td><label><bean:message bundle="gde" key="gde.liqCom.observacion.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:textarea name="liqComAdapterVO" property="liqCom.observacion" cols="80" rows="15"/>
						</td>
					</tr>		
				<!-- <#Campos#> -->
			</table>
		</fieldset>	
		<!-- LiqCom -->
		
		<table class="tablabotones" width="100%" >
		   	<tr>
	  	   		<td align="left" width="50%">
		   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
		   	    		<bean:message bundle="base" key="abm.button.volver"/>
		   	    	</html:button>
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="liqComAdapterVO" property="act" value="modificar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="liqComAdapterVO" property="act" value="agregar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
							<bean:message bundle="base" key="abm.button.agregar"/>
						</html:button>
					</logic:equal>
	   	    	</td>   	    	
	   	    </tr>
	   	</table>
	   	
	</span>   	
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
