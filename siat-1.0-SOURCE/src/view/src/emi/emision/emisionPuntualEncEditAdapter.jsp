<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/emi/AdministrarEncEmisionPuntual.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				

	<!-- Busqueda de Recuso -->
	<span id="blockBusqueda" style="display:none"> 
		<bean:define id="adapterVO" name="encEmisionPuntualAdapterVO"/>
		<bean:define id="poseeParam" value="true" />  
		<%@ include file="/def/gravamen/includeRecursoSearch.jsp" %>
	</span>

	<span id="blockSimple" style="display:block">

		<h1><bean:message bundle="emi" key="emi.emisionPuntualEncEditAdapter.title"/></h1>	
	
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
	
		<fieldset>
			<legend><bean:message bundle="emi" key="emi.emision.title"/></legend>
			<table class="tabladatos">
				<!-- Viene con Cuenta preseteada -->
				<logic:empty name="encEmisionPuntualAdapterVO" property="listRecurso">
					<!-- Recurso -->
					<tr>
						<td><label><bean:message bundle="emi" key="emi.emision.recurso.label"/>: </label></td>
						<td class="normal" colspan="3">
							<bean:write name="encEmisionPuntualAdapterVO" property="emision.recurso.desRecurso"/>
						</td>
					</tr>
					<!-- Fecha Emision -->
					<tr>
						<td><label><bean:message bundle="emi" key="emi.emision.fechaEmision.label"/>: </label></td>
						<td class="normal">
							<bean:write name="encEmisionPuntualAdapterVO" property="emision.fechaEmisionView"/>
						</td>
					</tr>
					<!-- Cuenta -->
					<tr>	
						<td><label><bean:message bundle="pad" key="pad.cuenta.label"/>:</label></td>
						<td class="normal" colspan="3">
							<bean:write name="encEmisionPuntualAdapterVO" property="emision.cuenta.numeroCuenta"/>
						</td>
					</tr>
				</logic:empty>
				
				<!-- No Viene Cuenta preseteada -->
				<logic:notEmpty name="encEmisionPuntualAdapterVO" property="listRecurso">
					
					<tr>
						<!-- Recurso -->
						<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.emision.recurso.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:select name="encEmisionPuntualAdapterVO" property="emision.recurso.id" styleClass="select" styleId="cboRecurso" style="width:90%" onchange="submitForm('paramRecurso', '');">
								<bean:define id="includeRecursoList" name="encEmisionPuntualAdapterVO" property="listRecurso"/>
								<bean:define id="includeIdRecursoSelected" name="encEmisionPuntualAdapterVO" property="emision.recurso.id"/>
								<%@ include file="/def/gravamen/includeRecurso.jsp" %>
							</html:select>
	
							<a onclick="toggleSearchRecurso('blockSimple', 'blockBusqueda'); setFocus('recursoFilter');" >
								<img title='<bean:message bundle="def" key="def.recurso.filtro.button.busqueda"/>' border="0" 
								src="<%=request.getContextPath()%>/images/iconos/lupita.gif" width="20" height="20">
							</a>  
						</td>
					</tr>
		
					<tr>
						<!-- Fecha Emision -->
						<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.emision.fechaEmision.label"/>: </label></td>
						<td class="normal">
							<html:text name="encEmisionPuntualAdapterVO" property="emision.fechaEmisionView" styleId="fechaEmisionView" size="15" maxlength="10" styleClass="datos" />
							<a class="link_siat" onclick="return show_calendar('fechaEmisionView');" id="a_fechaEmisionView">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>				
					</tr>
		
					<tr>
						<!-- Cuenta -->	
						<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.cuenta.label"/>:</label></td>
						<td class="normal" colspan="3">
							<html:text name="encEmisionPuntualAdapterVO" property="emision.cuenta.numeroCuenta" size="10" maxlength="12"/>
							<html:button property="btnBuscarCuenta"  styleClass="boton" onclick="submitForm('buscarCuenta', '');">
								<bean:message bundle="emi" key="emi.emisionPuntualEncEditAdapter.button.buscarCuenta"/>
							</html:button>
						</td>
					</tr>
			   </logic:notEmpty>
	
				<logic:equal name="encEmisionPuntualAdapterVO" property="mostrarAnioPeriodo" value="true">
					<tr>
						<!-- Anio -->
						<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.emisionPuntualEncEditAdapter.anio.label"/>: </label></td>
						<td class="normal"><html:text name="encEmisionPuntualAdapterVO" property="emision.anioView" size="4" maxlength="4"/></td>			
					</tr>
		
					<tr>
						<!-- Periodo Desde -->
						<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.emisionPuntualEncEditAdapter.periodoDesde.label"/>: </label></td>
						<td class="normal"><html:text name="encEmisionPuntualAdapterVO" property="emision.periodoDesdeView" size="2" maxlength="2"/></td>			
		
						<!-- Periodo Hasta -->
						<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.emisionPuntualEncEditAdapter.periodoHasta.label"/>: </label></td>
						<td class="normal"><html:text name="encEmisionPuntualAdapterVO" property="emision.periodoHastaView" size="2" maxlength="2"/></td>			
					</tr>
				</logic:equal>
				
				<logic:equal name="encEmisionPuntualAdapterVO" property="mostrarCantDeuPer" value="true">
					<tr>
						<!-- Cantidad de Deuda a Emitir por Periodos -->
						<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.emisionPuntualEncEditAdapter.cantDeuPer.label"/>: </label></td>
						<td class="normal"><html:text name="encEmisionPuntualAdapterVO" property="emision.cantDeuPerView" size="4" maxlength="4"/></td>			
					</tr>
				</logic:equal>
	
				<tr>
					<!-- Inclusion de Caso -->
					<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
					<td colspan="3">
						<bean:define id="IncludedVO" name="encEmisionPuntualAdapterVO" property="emision"/>
						<bean:define id="voName" value="emision" />
						<%@ include file="/cas/caso/includeCaso.jsp" %>
					</td>
					<!-- Fin Inclusion de Caso -->			
				</tr>
	
				<tr>
					<!-- Observaciones -->
					<td><label><bean:message bundle="emi" key="emi.emisionPuntualEncEditAdapter.observaciones.label"/>:</label></td>
					<td class="normal" colspan="3">
						<html:text name="encEmisionPuntualAdapterVO" property="emision.observacion" size="50" maxlength="55"/>
					</td>			
				</tr>
			</table>
		</fieldset>	
	
		<!-- Atributos a Valorizar -->
		<logic:equal name="encEmisionPuntualAdapterVO" property="mostrarAtributosEmision" value="true">
			<fieldset>
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="pad" key="pad.cuenta.listRecAtrCueV.label"/></caption>            
					<tbody>
						<logic:iterate id="GenericAtrDefinition" name="encEmisionPuntualAdapterVO" property="emision.recAtrCueEmiDefinition.listGenericAtrDefinition" indexId="count">
							<bean:define id="AtrVal" name="GenericAtrDefinition"/>
							<%@ include file="/def/atrDefinition4Edit.jsp" %>
						</logic:iterate>
					</tbody>
				</table>
			</fieldset>
		</logic:equal>
		<!-- Fin Atributos a Valorizar -->
		
		<table class="tablabotones" width="100%" >
		   	<tr>
	  	   		<td align="left" width="50%">
		   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
		   	    		<bean:message bundle="base" key="abm.button.volver"/>
		   	    	</html:button>
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="encEmisionPuntualAdapterVO" property="act" value="agregar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('emitir', '');">
							<bean:message bundle="emi" key="emi.emisionPuntualEncEditAdapter.button.emitir"/>
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
<!-- emisionPuntualEncEditAdapter.jsp -->