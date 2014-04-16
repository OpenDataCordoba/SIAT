<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/cyq/AdministrarDeudaCyq.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				

	<!-- Busqueda de Recuso -->
	<span id="blockBusqueda" style="display:none"> 
		<bean:define id="adapterVO" name="liqDeudaAdapterVO"/>
		<bean:define id="poseeParam" value="false" />
		<%@ include file="/def/gravamen/includeRecursoSearch.jsp" %>
	</span>

	<span id="blockSimple" style="display:block">
		
		<h1><bean:message bundle="cyq" key="cyq.deudaCyqCuentaSearch.title"/></h1>	
	
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- LiqDeuda -->
		<fieldset>
			<legend><bean:message bundle="cyq" key="cyq.deudaCyqCuentaSearch.fieldset.title"/></legend>
				
				<p>
					<label><bean:message bundle="cyq" key="cyq.procedimiento.label"/>: </label>
					<bean:write name="liqDeudaAdapterVO" property="procedimiento.numeroView"/> /
					<bean:write name="liqDeudaAdapterVO" property="procedimiento.anioView"/>			
				</p>
				
				<logic:notEmpty name="liqDeudaAdapterVO" property="procedimiento.fechaAutoView">
					<p>
						<label><bean:message bundle="cyq" key="cyq.procedimiento.fechaAuto.label"/>: </label>
						<bean:write name="liqDeudaAdapterVO" property="procedimiento.fechaAutoView"/>			
					</p>
				</logic:notEmpty>
				
				<logic:empty name="liqDeudaAdapterVO" property="procedimiento.fechaAutoView">
					<p>
						<label><bean:message bundle="cyq" key="cyq.procedimiento.fechaVerOpo.label"/>: </label>
						<bean:write name="liqDeudaAdapterVO" property="procedimiento.fechaVerOpoView"/>			
					</p>
				</logic:empty>
				
				<p>
					<label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label>
					<html:select name="liqDeudaAdapterVO" property="idRecurso" styleClass="select" styleId="cboRecurso" style="width:80%">
						<bean:define id="includeRecursoList" name="liqDeudaAdapterVO" property="listRecurso"/>
						<bean:define id="includeIdRecursoSelected" name="liqDeudaAdapterVO" property="idRecurso"/>
						<%@ include file="/def/gravamen/includeRecurso.jsp" %>
					</html:select>
					
					<a onclick="toggleSearchRecurso('blockSimple', 'blockBusqueda'); setFocus('recursoFilter');" >
						<img title='<bean:message bundle="def" key="def.recurso.filtro.button.busqueda"/>' border="0" 
						src="<%=request.getContextPath()%>/images/iconos/lupita.gif" width="20" height="20">
					</a>
					
				</p>
				<p>
		      		<label>(*)&nbsp;<bean:message bundle="pad" key="pad.cuenta.label"/>: 	      		
		      			<html:text name="liqDeudaAdapterVO" property="numeroCuenta" size="15" maxlength="20" styleClass="datos"/>
		      		</label>
				</p>
				
			  	<div style="text-align:right">
			  		<button type="button" name="btnAceptar" onclick="submitForm('validar', '');" class="boton">
			  			<bean:message bundle="cyq" key="cyq.deudaCyqCuentaSearch.button.aceptar"/>
			  		</button>
			  	</div>
		</fieldset>	
		<!-- LiqDeuda -->
		
		<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	  	    <bean:message bundle="base" key="abm.button.volver"/>
		</html:button>

	</span>

    <input type="text" style="display:none"/>	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

</html:form>
<!-- Fin formulario -->