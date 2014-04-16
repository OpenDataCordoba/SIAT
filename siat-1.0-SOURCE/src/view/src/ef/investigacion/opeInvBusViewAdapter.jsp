<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/AdministrarOpeInvBus.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="ef" key="ef.opeInvBusAdapter.title"/></h1>	
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
	<!-- OpeInv -->
	<bean:define id="opeInvVO" name="opeInvBusAdapterVO" property="opeInvBus.opeInv"/>
	<%@include file="/ef/investigacion/includeOpeInvView.jsp" %>	
	<!-- OpeInv -->		
		
	
	<!-- opeInvBus -->
	<bean:define id="opeInvBusVO" name="opeInvBusAdapterVO" property="opeInvBus"/>
	<%@include file="/ef/investigacion/includeOpeInvBusView.jsp" %>		
	<!-- opeInvBus -->

   	 <table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<logic:equal name="opeInvBusAdapterVO" property="act" value="eliminar">
	   	    	<td align="right" width="50%">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('eliminar', '');">
						<bean:message bundle="base" key="abm.button.eliminar"/>
					</html:button>
	   	    	</td>  
	   	    </logic:equal>
	   	    <logic:equal name="opeInvBusAdapterVO" property="act" value="modificar">
	   	    	<td align="right" width="50%">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
	   	    	</td>  
	   	    </logic:equal>	
   	    </tr>
   	</table>
		
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclucion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>

</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
<!-- opeInvBusViewAdapter.jsp -->