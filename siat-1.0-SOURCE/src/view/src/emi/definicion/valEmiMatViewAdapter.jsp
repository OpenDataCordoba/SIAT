<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- JavaScript imports -->
<script type="text/javascript" src="<%= request.getContextPath()%>/base/grid.js"></script>
<script type="text/javascript" src="<%= request.getContextPath()%>/base/parseUtils.js"></script>

<!-- Styles -->
<style type="text/css">@import url("<%= request.getContextPath()%>/styles/grid.css");</style>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/emi/AdministrarValEmiMat.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="emi" key="emi.valEmiMatViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- ValEmiMat -->
		<fieldset id="fieldSet">
                    <legend><bean:message bundle="emi" key="emi.valEmiMat.title"/></legend>
                    <table class="tabladatos">
                        <tr>
                                <!-- Tipo de Emision -->
                                <td><label><bean:message bundle="emi" key="emi.valEmiMat.recurso.label"/>: </label></td>
                                <td class="normal">
                                        <bean:write name="valEmiMatAdapterVO" property="valEmiMat.emiMat.recurso.desRecurso"/>
                                </td>
                        </tr>
                        <tr>
                                <!-- Matrices de Emision -->
                                <td><label><bean:message bundle="emi" key="emi.valEmiMat.emiMat.label"/>: </label></td>
                                <td class="normal">
                                        <bean:write name="valEmiMatAdapterVO" property="valEmiMat.emiMat.codEmiMat"/>
                                </td>
                        </tr>
                        <tr>
                                <!-- Estado -->
                                <td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
                                <td class="normal"><bean:write name="valEmiMatAdapterVO" property="valEmiMat.estado.value"/></td>
                        </tr>
                    </table>
                    <div id="gridContainer" align="center" style="margin: 3px 3px 3px 3px;">
                      <script type="text/javascript">

                          var datos = "<bean:write name="valEmiMatAdapterVO" property="valEmiMat.valores"/>";
                          var gridContainer = document.getElementById("gridContainer");

                          var grid = new Grid(datos, {
                            id      : "idGrid",
                            caption : "<bean:write name="valEmiMatAdapterVO" property="valEmiMat.emiMat.codEmiMat"/>",
                            height  : "400px", 
                            width   : "650px", 
                            cellSize: "200px", 
                            readonly: true
                          }, gridContainer);
                      </script>
                    </div>
		</fieldset>	
		<!-- ValEmiMat -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="valEmiMatAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
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
<!-- valEmiMatViewAdapter.jsp -->