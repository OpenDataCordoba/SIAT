<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/AdministrarOrdenControlFis.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="ef" key="ef.asignarOrdenAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
					<logic:notEqual name="ordenControlFisAdapterVO" property="act" value="verOrdenAnterior">  	    			
			   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
							<bean:message bundle="base" key="abm.button.volver"/>
						</html:button>	   	    			
					</logic:notEqual>	
					<logic:equal name="ordenControlFisAdapterVO" property="act" value="verOrdenAnterior">  	    			
			   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volverDeOrdenAnterior', '');">
							<bean:message bundle="base" key="abm.button.volver"/>
						</html:button>	   	    			
					</logic:equal>
				</td>
			</tr>
		</table>
		
		<!-- OrdenControlFis -->
		<bean:define id="ordenControlFis" name="ordenControlFisAdapterVO" property="ordenControl"/>
		<%@include file="/ef/fiscalizacion/includeOrdenControlFisView.jsp" %>
		<!-- OrdenControlFis -->

<!-- Tabla que contiene todos los formularios -->

	<fieldset>
		<legend><bean:message bundle="ef" key="ef.asignarOrdenAdapter.Inspectores.title"/></legend>
		<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="ef" key="ef.ordenControl.inspector.label"/>:</label></td>
				<td class="normal">
					<html:select name="ordenControlFisAdapterVO" property="ordenControl.inspector.id" styleClass="select" onchange="submitForm('paramSearchInpector','')">
						<html:optionsCollection name="ordenControlFisAdapterVO" property="listInspector" label="desInspector" value="id" />
					</html:select>
				</td>
				<td><label><bean:message bundle="ef" key="ef.ordenControl.supervisor.label"/>:</label></td>
				<td class="normal">
					<html:select name="ordenControlFisAdapterVO" property="ordenControl.supervisor.id" styleClass="select" onchange="submitForm('paramSearchSupervisor','')">
						<html:optionsCollection name="ordenControlFisAdapterVO" property="listSupervisor" label="desSupervisor" value="id" />
					</html:select>
				</td>
			</tr>
			<tr>
				<td>&nbsp; </td>
			</tr>
		</table>	 
		
		<!-- lista de Inspectores -->
		<logic:notEmpty name="ordenControlFisAdapterVO" property="listInspectorSelec" >
			<fieldset>
				<legend><bean:message bundle="ef" key="ef.asignarOrdenAdapter.detalle.label"/></legend>
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="ef" key="ef.ordenControlFisViewAdapter.listInspectores.title"/></caption>
					<tbody>
					            <th width="1">&nbsp;</th> 
								<th><bean:message bundle="ef" key="ef.inspector.label"/></th>
								<th><bean:message bundle="ef" key="ef.asignarOrdenAdapter.ordenesAbiertas.label"/></th>
								<th><bean:message bundle="ef" key="ef.asignarOrdenAdapter.detalle.label"/></th>
					<!-- ParamInspector -->				
						<logic:iterate name="ordenControlFisAdapterVO" property="listInspectorSelec" id="InspectorVO">
							<tr>
								<!-- Lista Inspectores -->
								<td align="center">
							       <bean:define name="InspectorVO" property="id" id="idInspector"/>
								   <html:radio name="ordenControlFisAdapterVO" property="selectedInspector" value="<%=idInspector.toString()%>" onclick="submitForm('paramSearchSupervisorSelect', '');" />
								</td>
					            <td class="normal"><bean:write name="InspectorVO" property="desInspector"/></td>
							  	<td class="normal"><bean:write name="InspectorVO" property="cantOrdenesAbiertas"/></td>
								<td class="normal"><bean:write name="InspectorVO" property="detalleOrden"/></td>
							</tr>	
						</logic:iterate>
					<!-- ParamInspector -->		
				    </tbody>	
				</table>
		</logic:notEmpty>
		<logic:empty name="ordenControlFisAdapterVO" property="listInspectorSelec">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="ef" key="ef.ordenControlFisViewAdapter.listInspectores.title"/></caption>
                	<tbody>
						<tr><td align="center">
							<bean:message bundle="base" key="base.resultadoVacio"/>
						</td></tr>
					</tbody>			
				</table>
		</logic:empty>
		<logic:notEmpty name="ordenControlFisAdapterVO" property="listSupervisorSelec" >
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="ef" key="ef.ordenControlFisViewAdapter.listSupervisores.title"/></caption>
					<tbody>
					            <th width="1">&nbsp;</th> 
								<th><bean:message bundle="ef" key="ef.supervisor.label"/></th>
								
					<!-- ParamSupervisor -->				
						<logic:iterate name="ordenControlFisAdapterVO" property="listSupervisorSelec" id="SupervisorVO">
							<tr>
								<!-- Lista Supervisores -->
								<td align="center">
							       <bean:define name="SupervisorVO" property="id" id="idSupervisores"/>
								   <html:radio name="ordenControlFisAdapterVO" property="selectedSupervisor" value="<%=idSupervisores.toString()%>" />
								</td>
					            <td class="normal"><bean:write name="SupervisorVO" property="desSupervisor"/></td>
					    	</tr>	
						</logic:iterate>
					<!-- ParamSupervisor -->		
				    </tbody>	
				</table>
		</logic:notEmpty>
		<logic:empty name="ordenControlFisAdapterVO" property="listSupervisorSelec">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="ef" key="ef.ordenControlFisViewAdapter.listSupervisores.title"/></caption>
                	<tbody>
						<tr><td align="center">
							<bean:message bundle="base" key="base.resultadoVacio"/>
						</td></tr>
					</tbody>			
				</table>
		</logic:empty>
	</fieldset>
	<fieldset>
		<legend><bean:message bundle="ef" key="ef.ordenControlFisViewAdapter.fechaCierre.title"/></legend>
		<p>&nbsp; </p>
		<p align="center"><label><bean:message bundle="ef" key="ef.ordenControl.fechaCierre.label"/></label>
			<html:text name="ordenControlFisAdapterVO" property="ordenControl.fechaCierreView" styleId="fechaCierreView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaCierreView');" id="a_fechaCierreView">
				<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
		</p>
	</fieldset>	
   <table class="tablabotones">
		<tr align="center">
		<p align="right">
			<input type="button" name="btnOrdenes" value="Asignar Orden" onclick="submitForm('asignarOrden','');" class="boton"/>
		</p>
		</tr>
		<tr>
	    	<td align="left">
	      		<input type="button" name="btnVolver" value="Volver" onclick="submitForm('volver', '');" class="boton"/>
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
<!-- asignarOrdeAdapter.jsp -->