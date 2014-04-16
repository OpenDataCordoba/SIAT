<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/AdministrarPlaFueDatDet.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="ef" key="ef.plaFueDatDetEditAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- PlaFueDatDet -->
		<fieldset>
			<legend><bean:message bundle="ef" key="ef.plaFueDatDet.title"/></legend>
			<table class="tabladatos">
			
			<!-- periodoDesde -->
			<tr>
				<td><LABEL><bean:message bundle="ef" key="ef.plaFueDatDetEditAdapter.periodoDesde.label"/></LABEL></td>
				<td class="normal">
					<html:text name="plaFueDatDetAdapterVO" property="periodoDesde" size="3" maxlength="2"/>/
					<html:text name="plaFueDatDetAdapterVO" property="anioDesde"  size="5" maxlength="4"/>
					(mm/aaaa)
				</td>		
			
			<!-- periodoHasta -->	
				<td><LABEL><bean:message bundle="ef" key="ef.plaFueDatDetEditAdapter.periodoHasta.label"/></LABEL></td>
				<td class="normal">
					<html:text name="plaFueDatDetAdapterVO" property="periodoHasta"  size="3" maxlength="2"/>/
					<html:text name="plaFueDatDetAdapterVO" property="anioHasta"  size="5" maxlength="4"/>
					(mm/aaaa)
				</td>
			</tr>					

				
			</table>
		</fieldset>	
		<!-- PlaFueDatDet -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right">
					<logic:equal name="plaFueDatDetAdapterVO" property="act" value="agregar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('agregar', '');">
							<bean:message bundle="base" key="abm.button.agregar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
	   	    </tr>
	   	 </table>
	   	<input type="hidden" name="name"  value="<bean:write name='plaFueDatDetAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
<!-- plaFueDatDetViewAdapter.jsp -->