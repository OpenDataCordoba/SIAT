<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/AdministrarPlanFiscal.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="ef" key="ef.planFiscalViewAdapter.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>

		
	<!-- PlanFiscal -->
	<fieldset>
		<legend><bean:message bundle="ef" key="ef.planFiscal.title"/></legend>
		
		<table class="tabladatos">
			<!-- fechaDesde -->
			<tr>
				<td><label><bean:message bundle="ef" key="ef.planFiscal.fechaDesde.label"/>: </label></td>
				<td class="normal">
					<bean:write name="planFiscalAdapterVO" property="planFiscal.fechaDesdeView"/>
				</td>

			<!-- fechaHasta -->
				<td><label><bean:message bundle="ef" key="ef.planFiscal.fechaHasta.label"/>: </label></td>
				<td class="normal">
					<bean:write name="planFiscalAdapterVO" property="planFiscal.fechaHastaView"/>
				</td>
			</tr>
			
			<!-- numero -->
			<tr>
				<td><label><bean:message bundle="ef" key="ef.planFiscal.numero.label"/>: </label></td>
				<td class="normal"><bean:write name="planFiscalAdapterVO" property="planFiscal.numero"/></td>			


			<!-- desPlanFiscal -->

				<td><label><bean:message bundle="ef" key="ef.planFiscal.desPlanFiscal.label"/>: </label></td>
				<td class="normal"><bean:write name="planFiscalAdapterVO" property="planFiscal.desPlanFiscal"/></td>			
			</tr>
			
			<!-- Estado Plan -->
			<tr>
				<td><label><bean:message bundle="ef" key="ef.estadoPlanFis.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="planFiscalAdapterVO" property="planFiscal.estadoPlanFis.desEstadoPlanFis"/></td>			
			</tr>			
					
			<!-- objetivo -->
			<tr>
				<td><label><bean:message bundle="ef" key="ef.planFiscal.objetivo.label"/>: </label></td>
				<td class="normal"  colspan="3" style="border-bottom: 1px solid #999999;"><br><bean:write name="planFiscalAdapterVO" property="planFiscal.objetivo"/><br><br></td>			
			</tr>
			<!-- fundamentos -->
			<tr>
				<td><label><bean:message bundle="ef" key="ef.planFiscal.fundamentos.label"/>: </label></td>
				<td class="normal" colspan="3" style="border-bottom: 1px solid #999999;"><br><bean:write name="planFiscalAdapterVO" property="planFiscal.fundamentos"/><br><br></td>			
			</tr>
			<!-- propuestas -->
			<tr>
				<td><label><bean:message bundle="ef" key="ef.planFiscal.propuestas.label"/>: </label></td>
				<td class="normal" colspan="3" style="border-bottom: 1px solid #999999;"><br><bean:write name="planFiscalAdapterVO" property="planFiscal.propuestas"/><br><br></td>			
			</tr>
			<!-- metTrab -->
			<tr>
				<td><label><bean:message bundle="ef" key="ef.planFiscal.metTrab.label"/>: </label></td>
				<td class="normal" colspan="3" style="border-bottom: 1px solid #999999;"><br><bean:write name="planFiscalAdapterVO" property="planFiscal.metTrab"/><br><br></td>			
			</tr>
			<!-- necesidades -->
			<tr>
				<td><label><bean:message bundle="ef" key="ef.planFiscal.necesidades.label"/>: </label></td>
				<td class="normal" colspan="3" style="border-bottom: 1px solid #999999;"><br><bean:write name="planFiscalAdapterVO" property="planFiscal.necesidades"/><br><br></td>			
			</tr>
			<!-- resEsp -->
			<tr>
				<td><label><bean:message bundle="ef" key="ef.planFiscal.resEsp.label"/>: </label></td>
				<td class="normal" colspan="3" style="border-bottom: 1px solid #999999;"><br><bean:write name="planFiscalAdapterVO" property="planFiscal.resEsp"/><br><br></td>			
			</tr>

			<!-- <#Campos#> -->
		</table>
	</fieldset>	
	<!-- PlanFiscal -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="planFiscalAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="planFiscalAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="planFiscalAdapterVO" property="act" value="desactivar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
							<bean:message bundle="base" key="abm.button.desactivar"/>
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
