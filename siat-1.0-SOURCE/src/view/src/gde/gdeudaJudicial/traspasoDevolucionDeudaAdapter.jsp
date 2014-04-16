<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarTraspasoDevolucionDeuda.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				

	<logic:equal name="traspasoDevolucionDeudaAdapterVO" property="accionTraspasoDevolucion.esTraspaso" value="true">
		<%@ include file="traspasoDeudaAdapter.jsp" %>
	</logic:equal>
		
	<logic:equal name="traspasoDevolucionDeudaAdapterVO" property="accionTraspasoDevolucion.esDevolucion" value="true">
		<%@ include file="devolucionDeudaAdapter.jsp" %>
	</logic:equal>
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="traspasoDevolucionDeudaAdapterVO" property="act" value="eliminar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('eliminar', '');">
						<bean:message bundle="base" key="abm.button.eliminar"/>
					</html:button>
				</logic:equal>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="traspasoDevolucionDeudaAdapterVO" property="act" value="agregarTraDevDeuDet">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregarListTraDevDeuDet', '');">
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

</html:form>
<!-- Fin Tabla que contiene todos los formularios -->