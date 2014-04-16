<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/def/AdministrarValUnRecConADe.do">


		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="def" key="def.valUnRecConADeAdapter.legend"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>

		<!-- RecConADec -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.recConADecAdapter.title"/></legend>			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="def" key="def.recConADec.codConcepto"/>: </label></td>
					<td class="normal">
							<bean:write name="valUnRecConADeAdapterVO" property="valUnRecConADe.recConADec.codConcepto"/>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.recConADec.desConcepto"/>: </label></td>
					<td class="normal" colspan="4">
						<bean:write name="valUnRecConADeAdapterVO" property="valUnRecConADe.recConADec.desConcepto"/>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.recConADec.observacion.label"/>: </label></td>
					<td class="normal" colspan="4">
						<bean:write name="valUnRecConADeAdapterVO" property="valUnRecConADe.recConADec.observacion"/>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.recConADec.fechaDesde"/>: </label></td>
					<td class="normal">
						<bean:write name="valUnRecConADeAdapterVO" property="valUnRecConADe.recConADec.fechaDesdeView"/>
					</td>
					<td><label><bean:message bundle="def" key="def.recConADec.fechaHasta"/>: </label>
					<td class="normal">
						<bean:write name="valUnRecConADeAdapterVO" property="valUnRecConADe.recConADec.fechaHastaView"/>
					</td>
				</tr>
			</table>
		</fieldset>
		
		<!-- Costo Unitario -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.valUnRecConADeAdapter.legend"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="def" key="def.valUnRecConADeAdapter.valRefDes.label"/>: </label></td>
					<td class="normal">
						<html:text name="valUnRecConADeAdapterVO" property="valUnRecConADe.valRefDesView"/>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.valUnRecConADeAdapter.valRefHas.label"/>: </label></td>
					<td class="normal">
						<html:text name="valUnRecConADeAdapterVO" property="valUnRecConADe.valRefHasView"/>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.valUnRecConADeAdapter.valorUnitario.label"/>: </label></td>
					<td class="normal">
						<html:text name="valUnRecConADeAdapterVO" property="valUnRecConADe.valorUnitarioView"/>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.valUnRecConADeAdapter.alicuota.label"/>: </label></td>
					<td class="normal">
						<html:select name="valUnRecConADeAdapterVO" property="valUnRecConADe.recAli.id" >
							<html:optionsCollection name="valUnRecConADeAdapterVO" property="listRecAli" label="alicuotaView" value="id" filter="false"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.valUnRecConADeAdapter.fechaDesde.label"/>: </label></td>
					<td class="normal">
						<html:text name="valUnRecConADeAdapterVO" property="valUnRecConADe.fechaDesdeView" styleId="fechaDesdeView" size="15" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
					<td><label><bean:message bundle="def" key="def.valUnRecConADeAdapter.fechaHasta.label"/>: </label></td>
					<td class="normal">
						<html:text name="valUnRecConADeAdapterVO" property="valUnRecConADe.fechaHastaView" styleId="fechaHastaView" size="15" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
				</tr>
			</table>		
		</fieldset>
		
		<table class="tablabotones" width="100%">
			<tr>
				<logic:equal name="valUnRecConADeAdapterVO" property="act" value="agregar">	
		   	    	<td align="right" width="50%">
						<html:button property="btnAccionBase"  styleId="btnAdd" styleClass="boton" onclick="submitForm('agregar', '');">
							<bean:message bundle="base" key="abm.button.agregar"/>
						</html:button>
					</td>
				</logic:equal>
				<logic:equal name="valUnRecConADeAdapterVO" property="act" value="modificar">
					<td align="right">
						<html:button property="btnAccionBase" styleId="btnMdf"  styleClass="boton" onclick="submitForm('modificar', '');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>
					</td>
				</logic:equal>
 			</tr>
 			<tr>
 				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		<input type="hidden" name="method" value=""/>
		<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
		<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
		<input type="hidden" name="act" id="act" value="<bean:write name="valUnRecConADeAdapterVO" property="act"/>"/>
		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
		<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
		
		