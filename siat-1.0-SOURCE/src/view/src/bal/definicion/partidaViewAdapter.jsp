<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/AdministrarPartida.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.partidaViewAdapter.title"/></h1>	
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- Partida -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.partida.title"/></legend>
			<table class="tabladatos">
				<!-- Codigo -->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.partida.codPartida.label"/>: </label></td>
					<td class="normal"><bean:write name="partidaAdapterVO" property="partida.codPartida"/></td>
				</tr>
				<!-- Descricion -->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.partida.desPartida.label"/>: </label></td>
					<td class="normal"><bean:write name="partidaAdapterVO" property="partida.desPartida"/></td>
				</tr>
				<!-- preEjeAct -->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.partida.preEjeAct.label"/>: </label></td>
					<td class="normal"><bean:write name="partidaAdapterVO" property="partida.preEjeAct"/></td>			
				</tr>
				<!-- preEjeVen -->
				<tr>
					<td><label><bean:message bundle="bal" key="bal.partida.preEjeVen.label"/>: </label></td>
					<td class="normal"><bean:write name="partidaAdapterVO" property="partida.preEjeVen"/></td>			
				</tr>
				<!-- Estado -->
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="partidaAdapterVO" property="partida.estado.value"/></td>
				</tr>
			</table>
		</fieldset>	

		<!-- Relacion Partida/CuentaBanco -->
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.partida.listParCueBan.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="partidaAdapterVO" property="partida.listParCueBan">	    	
			    	<tr>
						<th align="left"><bean:message bundle="bal" key="bal.parCueBan.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.parCueBan.fechaHasta.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.parCueBan.nroCuenta.label"/></th>						
					</tr>
					<logic:iterate id="ParCueBanVO" name="partidaAdapterVO" property="partida.listParCueBan">
						<tr>					                         
							<td><bean:write name="ParCueBanVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="ParCueBanVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="ParCueBanVO" property="cuentaBanco.nroCuenta" />&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="partidaAdapterVO" property="partida.listParCueBan">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
		</table>
		
		<!-- Relacion Partida/Rubro -->
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.partida.listRelPartida.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="partidaAdapterVO" property="listRelPartida">	    	
			    	<tr>
						<th align="left"><bean:message bundle="bal" key="bal.relPartida.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.relPartida.fechaHasta.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.clasificador.label"/></th>						
						<th align="left"><bean:message bundle="bal" key="bal.nodo.label"/></th>						
					</tr>
					<logic:iterate id="RelPartidaVO" name="partidaAdapterVO" property="listRelPartida">
						<tr>					                         
							<td><bean:write name="RelPartidaVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="RelPartidaVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="RelPartidaVO" property="nodo.clasificador.descripcion" />&nbsp;</td>
							<td><bean:write name="RelPartidaVO" property="nodo.descripcionView" />&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="partidaAdapterVO" property="listRelPartida">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
		</table>
		
		<!-- Partida -->

		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="left">
	   	    	    <logic:equal name="partidaAdapterVO" property="act" value="ver">
				   	   <html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						   <bean:message bundle="base" key="abm.button.imprimir"/>
					   </html:button>
					</logic:equal>
					
					<logic:equal name="partidaAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="partidaAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="partidaAdapterVO" property="act" value="desactivar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
							<bean:message bundle="base" key="abm.button.desactivar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
	   	    </tr>
	   	 </table>
	   	<input type="hidden" name="name"  value="<bean:write name='partidaAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
	   	 		   	 	
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
