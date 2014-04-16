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
	
	  <!-- historicos -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
			<caption><bean:message bundle="gde" key="gde.gesJud.listHistGesJud.label"/></caption>
			<tbody>
				<logic:notEmpty  name="gesJudAdapterVO" property="gesJud.listHistGesJud">          	
	               	<tr>
						<th align="left"><bean:message bundle="gde" key="gde.histGesjud.fecha.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.histGesjud.descripcion.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.histGesjud.usuario.label"/></th>				
					</tr>
						
					<logic:iterate id="histGesJud" name="gesJudAdapterVO" property="gesJud.listHistGesJud">
						<tr>
							<td><bean:write name="histGesJud" property="fechaView"/>&nbsp;</td>
							<td><bean:write name="histGesJud" property="descripcion"/>&nbsp;</td>
							<td><bean:write name="histGesJud" property="usuario"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
	
				<logic:empty name="gesJudAdapterVO" property="gesJud.listHistGesJud">
					<tr><td align="center">
						<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>			
				</logic:empty>
			</tbody>
		</table>
 <!-- FIN historicos -->


		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volverDeHistoricos', '');">
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
