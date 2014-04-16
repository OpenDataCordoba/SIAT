<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/def/AdministrarEncCodEmi.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<!-- Busqueda de Recuso -->
	<span id="blockBusqueda" style="display:none"> 
		<bean:define id="adapterVO" name="encCodEmiAdapterVO"/>
		<bean:define id="poseeParam" value="false" />
		<%@ include file="/def/gravamen/includeRecursoSearch.jsp" %>
	</span>

	<span id="blockSimple" style="display:block">
	
		<h1><bean:message bundle="def" key="def.codEmiEditAdapter.title"/></h1>	
	
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- CodEmi -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.codEmi.title"/></legend>
			
			<table class="tabladatos">
				<tr>
					<!-- Tipo de Codigo -->	
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.codEmi.tipCodEmi.label"/>: </label></td>
					<td class="normal">
						<html:select name="encCodEmiAdapterVO" property="codEmi.tipCodEmi.id" styleClass="select" onchange="submitForm('paramTipCodEmi', '');">
							<html:optionsCollection name="encCodEmiAdapterVO" property="listTipCodEmi" label="desTipCodEmi" value="id" />
						</html:select>
					</td>					
				</tr>
	
				<logic:equal name="encCodEmiAdapterVO" property="seleccionarRecursoEnabled" value="true">
					<tr>
						<!-- Recurso -->
						<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
						<td class="normal" colspan="4">
							<html:select name="encCodEmiAdapterVO" property="codEmi.recurso.id" styleClass="select" styleId="cboRecurso" style="width:90%">
								<bean:define id="includeRecursoList" name="encCodEmiAdapterVO" property="listRecurso"/>
								<bean:define id="includeIdRecursoSelected" name="encCodEmiAdapterVO" property="codEmi.recurso.id"/>
								<%@ include file="/def/gravamen/includeRecurso.jsp" %>
							</html:select>
							
							<a onclick="toggleSearchRecurso('blockSimple', 'blockBusqueda'); setFocus('recursoFilter');" >
								<img title='<bean:message bundle="def" key="def.recurso.filtro.button.busqueda"/>' border="0" 
								src="<%=request.getContextPath()%>/images/iconos/lupita.gif" width="20" height="20">
							</a>
							
						</td>
					</tr>
				</logic:equal>
	
				<tr>
					<!-- Nombre -->
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.codEmi.nombre.label"/>: </label></td>
					<td class="normal"><html:text name="encCodEmiAdapterVO" property="codEmi.nombre" size="20" maxlength="20"/></td>			
				</tr>
	
				<tr>
					<!-- Descripcion -->
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.codEmi.descripcion.label"/>: </label></td>
					<td class="normal"><html:text name="encCodEmiAdapterVO" property="codEmi.descripcion" size="30" maxlength="100"/></td>			
				</tr>
	
				<tr>
					<!-- Fecha Desde -->
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.codEmi.fechaDesde.label"/>: </label></td>
					<td class="normal">
						<html:text name="encCodEmiAdapterVO" property="codEmi.fechaDesdeView" styleId="fechaDesdeView" size="15" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
					
					<!-- Fecha Hasta -->
					<td><label><bean:message bundle="def" key="def.codEmi.fechaHasta.label"/>: </label></td>
					<td class="normal">
						<html:text name="encCodEmiAdapterVO" property="codEmi.fechaHastaView" styleId="fechaHastaView" size="15" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>				
				</tr>
	
			</table>
		</fieldset>	
		<!-- CodEmi -->
	
		<table class="tablabotones" width="100%" >
		   	<tr>
	  	   		<td align="left" width="50%">
		   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
		   	    		<bean:message bundle="base" key="abm.button.volver"/>
		   	    	</html:button>
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="encCodEmiAdapterVO" property="act" value="modificar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="encCodEmiAdapterVO" property="act" value="agregar">
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
<!-- codEmiEditAdapter.jsp -->