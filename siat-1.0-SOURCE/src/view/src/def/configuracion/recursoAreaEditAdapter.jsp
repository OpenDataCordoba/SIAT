<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/def/AdministrarRecursoArea.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>

	<!-- Busqueda de Recuso -->
	<span id="blockBusqueda" style="display:none"> 
		<bean:define id="adapterVO" name="recursoAreaAdapterVO"/>
		<bean:define id="poseeParam" value="false" />
		<%@ include file="/def/gravamen/includeRecursoSearch.jsp" %>
	</span>

	<span id="blockSimple" style="display:block">
		
		<h1><bean:message bundle="def" key="def.recursoAreaAdapter.title"/></h1>		
		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			
					<input type="button" 
						class="boton" 
						onclick="submitForm('volver', '<bean:write name="recursoAreaAdapterVO" property="recursoArea.area.id" bundle="base" formatKey="general.format.id" />');" 
						value="<bean:message bundle="base" key="abm.button.volver"/>" />
				</td>
			</tr>
		</table>
	
		<!-- Area -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.area.title"/></legend>
			<table class="tabladatos">
				<!-- Codigo -->
				<tr>
					<td><label><bean:message bundle="def" key="def.area.codArea.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAreaAdapterVO" property="recursoArea.area.codArea"/></td>
				</tr>
				<!-- Descricion -->
				<tr>
					<td><label><bean:message bundle="def" key="def.area.desArea.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAreaAdapterVO" property="recursoArea.area.desArea"/></td>
				</tr>
				<!-- Estado -->
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAreaAdapterVO" property="recursoArea.area.estado.value"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- Area -->
		
		<logic:equal name="recursoAreaAdapterVO" property="act" value="agregar">
			<!-- RecursoArea Habilidados -->
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="def" key="def.area.listRecursoArea.label"/></caption>
		    	<tbody>
					<logic:notEmpty  name="recursoAreaAdapterVO" property="recursoArea.area.listRecursoArea">	    	
				    	<tr>
							<th align="left"><bean:message bundle="def" key="def.recurso.label"/></th>
							<th align="left"><bean:message bundle="def" key="def.recursoArea.perCreaEmi.label"/></th>
						</tr>
						<logic:iterate id="RecursoAreaVO" name="recursoAreaAdapterVO" property="recursoArea.area.listRecursoArea">
							<tr>
								<td><bean:write name="RecursoAreaVO" property="recurso.desRecurso"/>&nbsp;</td>
								<td><bean:write name="RecursoAreaVO" property="perCreaEmi.value"/>&nbsp;</td>						
							</tr>
						</logic:iterate>
					</logic:notEmpty>
					<logic:empty  name="recursoAreaAdapterVO" property="recursoArea.area.listRecursoArea">
						<tr><td align="center">
							<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
					</logic:empty>
				</tbody>
			</table>
			<!-- RecursoArea Habilitado -->
		</logic:equal>	
	
		<!-- RecursoArea Nuevo -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.area.title"/></legend>			
			<table class="tabladatos">
				
				<logic:equal name="recursoAreaAdapterVO" property="act" value="agregar">
					<tr>
						<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
						<td class="normal">
							<html:select name="recursoAreaAdapterVO" property="recursoArea.recurso.id" styleClass="select" styleId="cboRecurso" style="width:90%">
								<html:optionsCollection name="recursoAreaAdapterVO" property="listRecurso" label="desRecurso" value="id" />
							</html:select>
							
							<a onclick="toggleSearchRecurso('blockSimple', 'blockBusqueda'); setFocus('recursoFilter');" >
								<img title='<bean:message bundle="def" key="def.recurso.filtro.button.busqueda"/>' border="0" 
								src="<%=request.getContextPath()%>/images/iconos/lupita.gif" width="20" height="20">
							</a>
								
						</td>
					</tr>
				</logic:equal>
					
				<logic:equal name="recursoAreaAdapterVO" property="act" value="modificar">
					<tr>
						<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
						<td class="normal">
							<bean:write name="recursoAreaAdapterVO" property="recursoArea.recurso.desRecurso"/>
						</td>
					</tr>				
				</logic:equal>
					
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recursoArea.perCreaEmi.label"/>: </label></td>
					<td class="normal">
						<html:select name="recursoAreaAdapterVO" property="recursoArea.perCreaEmi.id" styleClass="select">
							<html:optionsCollection name="recursoAreaAdapterVO" property="listSiNo" label="value" value="id" />
						</html:select>
					</td>
				</tr>
				
			</table>
		</fieldset>
		<!-- Fin RecursoArea -->
	
		<table class="tablabotones" width="100%">
		    	<tr>
	  	    		<td align="left" width="50%">
			   	    	<input type="button" 
							class="boton" 
							onclick="submitForm('volver', '<bean:write name="recursoAreaAdapterVO" property="recursoArea.area.id" bundle="base" formatKey="general.format.id" />');" 
							value="<bean:message bundle="base" key="abm.button.volver"/>" />
			   	    	
		   	    	</td>
	   	    		<td align="right" width="50%">	   	    	
						<logic:equal name="recursoAreaAdapterVO" property="act" value="modificar">
							<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
								<bean:message bundle="base" key="abm.button.modificar"/>
							</html:button>
						</logic:equal>
						<logic:equal name="recursoAreaAdapterVO" property="act" value="agregar">
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
	