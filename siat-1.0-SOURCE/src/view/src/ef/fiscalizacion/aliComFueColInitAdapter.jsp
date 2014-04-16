<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/ef/AdministrarAliComFueCol.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="ef" key="ef.aliComFueColAdapter.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- DetAju -->
		<fieldset>
			<legend><bean:message bundle="ef" key="ef.detAju.label"/></legend>
			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="ef" key="ef.detAju.fecha.label"/>: </label></td>
					<td class="normal"><bean:write name="aliComFueColAdapterVO" property="detAju.fechaView"/></td>
					
					<td><label><bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
					<td class="normal"><bean:write name="aliComFueColAdapterVO" property="detAju.ordConCue.cuenta.numeroCuenta" /></td>
				</tr>
															
			</table>
		</fieldset>	
		<!-- DetAju -->
		
		<!-- listCompFuente -->
		<div id="listCompFuente" class="scrolable" style="height: 350px;">		
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="ef" key="ef.aliComFueColAdapter.listAlicuotas.label"/></caption>
		    	<tbody>
					<logic:notEmpty  name="aliComFueColAdapterVO" property="listCompFuente">	    	
				    	<tr>
							<th width="1">&nbsp;</th> <!-- modificar -->
							<th align="left"><bean:message bundle="ef" key="ef.fuenteInfo.label"/></th>
							<th align="left"><bean:message bundle="ef" key="ef.plaFueDatCol.label"/></th>
							<th align="left"><bean:message bundle="ef" key="ef.aliComFueCol.label"/></th>
						</tr>
						<logic:iterate id="CompFuenteVO" name="aliComFueColAdapterVO" property="listCompFuente">
				
								
								<logic:iterate id="CompFuenteColVO" name="CompFuenteVO" property="listCompFuenteCol">	
									<tr>
										<!-- modificar -->
										<td>
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('irHistoricos', '<bean:write name="CompFuenteColVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
											</a>
										</td>
										<td><bean:write name="CompFuenteVO" property="plaFueDat.fuenteInfo.nombreFuente"/></td>
										<td><bean:write name="CompFuenteColVO" property="colName"/></td>
										<td><bean:write name="CompFuenteColVO" property="histAlicuota4View" filter="false"/></td>
									</tr>
								</logic:iterate>
						</logic:iterate>
					</logic:notEmpty>
					<logic:empty  name="aliComFueColAdapterVO" property="detAju.listDetAjuDet">
						<tr><td align="center">
						<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
					</logic:empty>
	
				</tbody>
			</table>
		</div>	
		<!-- DetAjuDet -->
				
		<table class="tablabotones">
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

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
<!-- aliComFueColInitAdapter.jsp -->