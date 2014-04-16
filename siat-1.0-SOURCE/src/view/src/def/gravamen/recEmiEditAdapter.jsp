<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/def/AdministrarRecEmi.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="def" key="def.recursoAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>

		<!-- Datos del Recurso -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.recurso.title"/></legend>			
			<table class="tabladatos">
				<tr>
					<!-- Tipo de Categoria -->										
					<td align="right"><label><bean:message bundle="def" key="def.categoria.tipo.label"/>:</label></td>
					<td class="normal"><bean:write name="recEmiAdapterVO" property="recEmi.recurso.categoria.tipo.desTipo"/></td>
					<!-- Categoria -->										
					<td align="right"><label><bean:message bundle="def" key="def.categoria.label"/>:</label></td>
					<td class="normal"><bean:write name="recEmiAdapterVO" property="recEmi.recurso.categoria.desCategoria"/></td>
				</tr>
				<tr>
					<!-- Codigo -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.codRecurso.label"/>:</label></td>
					<td class="normal"><bean:write name="recEmiAdapterVO" property="recEmi.recurso.codRecurso"/></td>
					<!-- Descripcion -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.desRecurso.label"/>: </label></td>
					<td class="normal"><bean:write name="recEmiAdapterVO" property="recEmi.recurso.desRecurso"/></td>					
				</tr>
			</table>
		</fieldset>
		<!-- Fin Datos del Recurso -->

		<!-- RecEmi -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.recEmi.title"/></legend>			
			<table class="tabladatos">
				<tr>
					<!-- TipoEmision -->										
					<td align="right"><label>(*)&nbsp;<bean:message bundle="def" key="def.recEmi.tipoEmision.label"/>:</label></td>
					<td class="normal">
						<html:select name="recEmiAdapterVO" property="recEmi.tipoEmision.id" styleClass="select">
							<html:optionsCollection name="recEmiAdapterVO" property="listTipoEmision" label="desTipoEmision" value="id" />
						</html:select>
					</td>
					<!-- Periodo Deuda -->										
					<td align="right"><label>(*)&nbsp;<bean:message bundle="def" key="def.recEmi.periodoDeuda.label"/>:</label></td>
					<td colspan="3" class="normal">
						<html:select name="recEmiAdapterVO" property="recEmi.periodoDeuda.id" styleClass="select">
							<html:optionsCollection name="recEmiAdapterVO" property="listPeriodoDeuda" label="desPeriodoDeuda" value="id" />
						</html:select>
					</td>
				</tr>
				<tr>
					<!-- Programa de Calculo -->										
					<td align="right"><label>(*)&nbsp;<bean:message bundle="def" key="def.recEmi.programa.label"/>:</label></td>
					<td class="normal">
						<html:select name="recEmiAdapterVO" property="recEmi.formulario.id" styleClass="select">
							<html:optionsCollection name="recEmiAdapterVO" property="listFormulario" label="codFormulario" value="id" />
						</html:select>
					</td>
				</tr>
				<tr>
					<!-- Atributo de Segmentacion -->										
					<td align="right"><label><bean:message bundle="def" key="def.recEmi.atributoEmision.label"/>:</label></td>
					<td colspan="2" class="normal">
						<html:select name="recEmiAdapterVO" property="recEmi.atributoEmision.id" styleClass="select" style="width:80%;">
							<html:optionsCollection name="recEmiAdapterVO" property="listAtributo" label="desAtributo" value="id" />
						</html:select>
					</td>
				</tr>
				<tr>
					<!-- Vencimiento -->										
					<td align="right"><label><bean:message bundle="def" key="def.recEmi.forVen.label"/>:</label></td>
					<td class="normal">
						<html:select name="recEmiAdapterVO" property="recEmi.forVen.id" styleClass="select">
							<html:optionsCollection name="recEmiAdapterVO" property="listVencimiento" label="desVencimiento" value="id" />
						</html:select>
					</td>
				</tr>
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recEmi.fechaDesde.label"/>: </label></td>
					<td class="normal">
						<html:text name="recEmiAdapterVO" property="recEmi.fechaDesdeView" styleId="fechaDesdeView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
					<td><label><bean:message bundle="def" key="def.recEmi.fechaHasta.label"/>: </label></td>
					<td colspan="2" class="normal">
						<html:text name="recEmiAdapterVO" property="recEmi.fechaHastaView" styleId="fechaHastaView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
				</tr>		
			</table>
		</fieldset>
		<!-- Fin RecEmi -->


	<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
		   	    		<bean:message bundle="base" key="abm.button.volver"/>
		   	    	</html:button>
	   	    	</td>
   	    		<td align="right" width="50%">	   	    	
					<logic:equal name="recEmiAdapterVO" property="act" value="modificar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="recEmiAdapterVO" property="act" value="agregar">
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
	