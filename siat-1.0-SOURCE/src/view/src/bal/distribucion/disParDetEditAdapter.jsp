<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/AdministrarDisParDet.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.disParAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>

		<!-- DisParDet -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.disParDet.title"/></legend>			
			<table class="tabladatos">
				<tr>
					<!-- Recurso -->		
					<td><label>&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="disParDetAdapterVO" property="disParDet.disPar.recurso.desRecurso"/></td>
				</tr>
				<tr>
					<!-- DesDisPar -->		
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.disPar.desDisPar.label"/>: </label></td>
					<td class="normal"><bean:write name="disParDetAdapterVO" property="disParDet.disPar.desDisPar"/></td>
				</tr>
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.tipoImporte.label"/>: </label></td>
					<td class="normal">	
						<logic:equal name="disParDetAdapterVO" property="disParDet.disPar.paramTipoImporte" value="false">
							<html:select name="disParDetAdapterVO" property="disParDet.tipoImporte.id" styleClass="select" onchange="submitForm('paramTipoImporte', '');">
								<html:optionsCollection name="disParDetAdapterVO" property="listTipoImporte" label="desTipoImporte" value="id" />
							</html:select>
						</logic:equal>
						<logic:equal name="disParDetAdapterVO" property="disParDet.disPar.paramTipoImporte" value="true">
							<html:select name="disParDetAdapterVO" property="disParDet.tipoImporte.id" styleClass="select" onchange="submitForm('paramTipoImporte', '');" disabled="true">
								<html:optionsCollection name="disParDetAdapterVO" property="listTipoImporte" label="desTipoImporte" value="id" />
							</html:select>
						</logic:equal>
					</td>
				</tr>	
				<tr>
					<td><label><bean:message bundle="def" key="def.recCon.label"/>: </label></td>
					<td class="normal">	
					<logic:equal name="disParDetAdapterVO" property="paramTipoImporte" value="true">
						<html:select name="disParDetAdapterVO" property="disParDet.recCon.id" styleClass="select">
							<html:optionsCollection name="disParDetAdapterVO" property="listRecCon" label="desRecCon" value="id" />
						</html:select>
					</logic:equal>
					<logic:equal name="disParDetAdapterVO" property="paramTipoImporte" value="false">
						<html:select name="disParDetAdapterVO" property="disParDet.recCon.id" styleClass="select" disabled="true">
							<html:optionsCollection name="disParDetAdapterVO" property="listRecCon" label="desRecCon" value="id" />
						</html:select>
					</logic:equal>
					</td>
				</tr>	
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.disParDet.partida.label"/>: </label></td>
					<td class="normal">	
						<html:select name="disParDetAdapterVO" property="disParDet.partida.id" styleClass="select">
							<html:optionsCollection name="disParDetAdapterVO" property="listPartida" label="desPartidaView" value="id" />
						</html:select>
					</td>
					<logic:equal name="disParDetAdapterVO" property="paramRecNoTrib" value="true">
						<td><label>&nbsp;<bean:message bundle="bal" key="bal.disParDet.esEjeAct.label"/>: </label></td>
						<td class="normal">	
							<html:select name="disParDetAdapterVO" property="disParDet.esEjeAct.id" styleClass="select">
								<html:optionsCollection name="disParDetAdapterVO" property="listSiNo" label="value" value="id" />
							</html:select>
						</td>
					</logic:equal>
				</tr>	
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.disParDet.porcentaje.label"/>: </label></td>
					<td class="normal"><html:text name="disParDetAdapterVO" property="disParDet.porcentajeView" size="10" maxlength="22" /></td>
				</tr>	
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.disParDet.fechaDesde.label"/>: </label></td>
					<td class="normal">
						<html:text name="disParDetAdapterVO" property="disParDet.fechaDesdeView" styleId="fechaDesdeView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.disParDet.fechaHasta.label"/>: </label></td>
					<td class="normal">
						<html:text name="disParDetAdapterVO" property="disParDet.fechaHastaView" styleId="fechaHastaView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
				</tr>		
			</table>
		</fieldset>
		<!-- Fin DisParDet -->
		
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<td align="right">
					<logic:equal name="disParDetAdapterVO" property="act" value="modificar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="disParDetAdapterVO" property="act" value="agregar">
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