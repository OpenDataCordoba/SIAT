<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarGesJud.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="gde" key="gde.gesJudViewAdapter.title"/></h1>	
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		<!-- GesJud -->
	 	<bean:define id="act" name="gesJudAdapterVO" property="act"/>
	 	<bean:define id="gesJud" name="gesJudAdapterVO" property="gesJud"/>
		<%@ include file="/gde/adminDeuJud/includeGesJud.jsp" %>
		<!-- GesJud -->
	
	  <!-- Deudas incluidas -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
			<caption><bean:message bundle="gde" key="gde.gesJud.deudasIncluidas.label"/></caption>
			<tbody>
				<logic:notEmpty  name="gesJudAdapterVO" property="gesJud.listGesJudDeu">          	
	               	<tr>
	               		<th width="1">&nbsp;</th> <!-- Ver -->
	               		<th align="left"><bean:message bundle="pad" key="pad.cuenta.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.deuda.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.deuda.importe.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.deuda.saldo.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.deuda.fechaVencimiento.label"/></th>					
					</tr>
						
					<logic:iterate id="gesJudDeu" name="gesJudAdapterVO" property="gesJud.listGesJudDeu">
						<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="gesJudAdapterVO" property="verEnabled" value="enabled">									
									<a style="cursor: pointer; cursor: hand;" 
										onclick="submitForm('verGesJudDeu', '<bean:write name="gesJudDeu" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</logic:equal>
								<logic:notEqual name="gesJudAdapterVO" property="verEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>	
							<td><bean:write name="gesJudDeu" property="deuda.cuenta.numeroCuenta"/>&nbsp;</td>
							<td><bean:write name="gesJudDeu" property="deuda.nroBarraAnio"/>&nbsp;</td>
							<td><bean:write name="gesJudDeu" property="deuda.importeView" />&nbsp;</td>
							<td><bean:write name="gesJudDeu" property="deuda.saldoView"/>&nbsp;</td>
							<td><bean:write name="gesJudDeu" property="deuda.fechaVencimientoView"/>&nbsp;</td>														
						</tr>
					</logic:iterate>
				</logic:notEmpty>
	
				<logic:empty name="gesJudAdapterVO" property="gesJud.listGesJudDeu">
					<tr><td align="center">
						<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>			
				</logic:empty>
			</tbody>
		</table>
 <!-- FIN Deudas a incluir -->
		
<!-- Eventos -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
			<caption><bean:message bundle="gde" key="gde.gesJud.listEventos.label"/></caption>
			<tbody>
				<logic:notEmpty  name="gesJudAdapterVO" property="gesJud.listGesJudEvento">          	
	               	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th align="left"><bean:message bundle="gde" key="gde.evento.codigo.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.evento.descripcion.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.evento.etapaProcesal.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.gesJud.evento.fechaEvento.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.gesJud.observaciones.label"/></th>					
					</tr>
						
					<logic:iterate id="gesJudEvento" name="gesJudAdapterVO" property="gesJud.listGesJudEvento">
						<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="gesJudAdapterVO" property="verEnabled" value="enabled">									
									<a style="cursor: pointer; cursor: hand;" 
										onclick="submitForm('verGesJudEvento', '<bean:write name="gesJudEvento" property="evento.id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</logic:equal>
								<logic:notEqual name="gesJudAdapterVO" property="verEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>																							
							<td><bean:write name="gesJudEvento" property="evento.codigoView"/>&nbsp;</td>
							<td><bean:write name="gesJudEvento" property="evento.descripcion" />&nbsp;</td>
							<td><bean:write name="gesJudEvento" property="evento.etapaProcesal.desEtapaProcesal"/>&nbsp;</td>
							<td><bean:write name="gesJudEvento" property="fechaEventoView"/>&nbsp;</td>
							<td><bean:write name="gesJudEvento" property="observacion" />&nbsp;</td>	
						</tr>
					</logic:iterate>
				</logic:notEmpty>
	
				<logic:empty name="gesJudAdapterVO" property="gesJud.listGesJudEvento">
					<tr><td align="center">
						<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>			
				</logic:empty>	
			</tbody>
		</table>
<!-- Fin Eventos -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="gesJudAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="gesJudAdapterVO" property="act" value="ver">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('verHistorico', '');">
							<bean:message bundle="gde" key="gde.gesJudViewAdapter.button.historicos.label"/>
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
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
