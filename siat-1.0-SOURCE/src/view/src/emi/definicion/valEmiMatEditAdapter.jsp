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
	
	<h1><bean:message bundle="emi" key="emi.valEmiMatEditAdapter.title"/></h1>	

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
                    <logic:equal name="valEmiMatAdapterVO" property="act" value="agregar">
                        <table class="tabladatos">
                              <tr>
                                    <!-- Recurso -->
                                    <td><label><bean:message bundle="def" key="def.emiMat.recurso.label"/>: </label></td>
                                    <td class="normal">
                                            <html:select name="valEmiMatAdapterVO" property="valEmiMat.emiMat.recurso.id" styleClass="select" onchange="submitForm('paramRecurso', '');">
                                                    <bean:define id="includeRecursoList" name="valEmiMatAdapterVO" property="listRecurso"/>
                                                    <bean:define id="includeIdRecursoSelected" name="valEmiMatAdapterVO" property="valEmiMat.emiMat.recurso.id"/>
                                                    <%@ include file="/def/gravamen/includeRecurso.jsp" %>
                                            </html:select>
                                    </td>
                                </tr>
                              <tr>
                                      <!-- Lista de Matrices de Emision -->	
                                      <td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.valEmiMat.emiMat.label"/>: </label></td>
                                      <td class="normal">
                                              <html:select name="valEmiMatAdapterVO" property="valEmiMat.emiMat.id" styleClass="select">
                                                      <html:optionsCollection name="valEmiMatAdapterVO" property="listEmiMat" label="codEmiMat" value="id" />
                                              </html:select>
                                      </td>
                              </tr>
                        </table>
		      </logic:equal>
		      <logic:equal name="valEmiMatAdapterVO" property="act" value="modificar">
                        <table class="tabladatos">
                              <tr>
                                      <!-- Tipo de Emision -->
                                      <td><label><bean:message bundle="emi" key="emi.valEmiMat.recurso.label"/>: </label></td>
                                      <td class="normal">
                                              <bean:write name="valEmiMatAdapterVO" property="valEmiMat.emiMat.recurso.desRecurso"/>
                                      </td>
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
                                  readonly: false
                                }, gridContainer);

                                function submitGrid() {
                                  var hidden = document.getElementsByName("valEmiMat.valores")[0];
                                  hidden.value=grid.getContent();
                                  submitForm('modificar', '');
                                }
                            </script>
                          </div>
			</logic:equal>
        	</fieldset>	
	<!-- ValEmiMat -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="valEmiMatAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitGrid();">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="valEmiMatAdapterVO" property="act" value="agregar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
						<bean:message bundle="base" key="abm.button.agregar"/>
					</html:button>
				</logic:equal>
   	    	</td>   	    	
   	    </tr>
   	</table>
        <html:hidden name="valEmiMatAdapterVO" property="valEmiMat.valores" />

	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
<!-- valEmiMatEditAdapter.jsp -->