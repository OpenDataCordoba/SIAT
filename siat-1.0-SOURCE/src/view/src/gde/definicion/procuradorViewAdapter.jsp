<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarProcurador.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="gde" key="gde.procuradorViewAdapter.title"/></h1>
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right"><html:button property="btnVolver"
				styleClass="boton" onclick="submitForm('volver', '');">
				<bean:message bundle="base" key="abm.button.volver" />
			</html:button></td>
		</tr>
	</table>
	<!-- Procurador -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.procurador.title"/></legend>
			<table class="tabladatos">
				<!-- Descripcion -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procurador.descripcion.label"/>: </label></td>
					<td class="normal"><bean:write name="procuradorAdapterVO" property="procurador.descripcion"/></td>
				</tr>
				
				<!-- Domicilio -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procurador.domicilio.label"/>: </label></td>
					<td class="normal"><bean:write name="procuradorAdapterVO" property="procurador.domicilio"/></td>

				
				<!-- Telefono -->

					<td><label><bean:message bundle="gde" key="gde.procurador.telefono.label"/>: </label></td>
					<td class="normal"><bean:write name="procuradorAdapterVO" property="procurador.telefono"/></td>
				</tr>
				
				<!-- HorarioAtencion -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procurador.horarioAtencion.label"/>: </label></td>
					<td class="normal"><bean:write name="procuradorAdapterVO" property="procurador.horarioAtencion"/></td>

				
				<!-- TipoProcurador -->

					<td><label><bean:message bundle="gde" key="gde.procurador.tipoProcurador.label"/>: </label></td>
					<td class="normal"><bean:write name="procuradorAdapterVO" property="procurador.tipoProcurador.desTipoProcurador"/></td>
				</tr>
				
				<!-- Observarcion -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procurador.observacion.label"/>: </label></td>
					<td class="normal"><bean:write name="procuradorAdapterVO" property="procurador.observacion"/></td>
				</tr>
				
			</table>
		</fieldset>	
		<!-- Procurador -->

		<!-- ProRec -->		
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="gde" key="gde.procurador.listProRec.label"/></caption>
		    	<tbody>
					<logic:notEmpty  name="procuradorAdapterVO" property="procurador.listProRec">
				    	<tr>
							<th width="1">&nbsp;</th> <!-- Ver -->
							<th align="left"><bean:message bundle="gde" key="gde.proRec.fechaDesde.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.proRec.fechaHasta.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.proRec.recurso.label"/></th>
						</tr>
						<logic:iterate id="ProRecVO" name="procuradorAdapterVO" property="procurador.listProRec">
				
							<tr>
								<!-- Ver -->
								<td>
									<logic:equal name="ProRecVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verProRec', '<bean:write name="ProRecVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									
									<logic:notEqual name="ProRecVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
									</logic:notEqual>
								</td>
								<td><bean:write name="ProRecVO" property="fechaDesdeView" />&nbsp;</td>
								<td><bean:write name="ProRecVO" property="fechaHastaView" />&nbsp;</td>	
								<td><bean:write name="ProRecVO" property="recurso.desRecurso" />&nbsp;</td>							
						   </tr>
						</logic:iterate>
					</logic:notEmpty>
					<logic:empty  name="procuradorAdapterVO" property="procurador.listProRec">
						<tr><td align="center">
						<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
					</logic:empty>					
				</tbody>
			</table>
		<!-- ProRec -->


		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="left">
	   	    	    <logic:equal name="procuradorAdapterVO" property="act" value="ver">
		   	    	   <html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						    <bean:message bundle="base" key="abm.button.imprimir"/>
					   </html:button>
	   	            </logic:equal>
					<logic:equal name="procuradorAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="procuradorAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="procuradorAdapterVO" property="act" value="desactivar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
							<bean:message bundle="base" key="abm.button.desactivar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
	   	    </tr>
	   	 </table>
	   	    	 
	   	<input type="hidden" name="name"  value="<bean:write name='procuradorAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
	 	   		   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
