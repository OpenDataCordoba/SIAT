<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- JavaScript imports -->
<script type="text/javascript" src="<%= request.getContextPath()%>/base/codeEditor/js/codemirror.js"></script>
<script type="text/javascript" src="<%= request.getContextPath()%>/base/codeEditor/js/mirrorframe.js"></script>

<!-- Styles -->
<style type="text/css">@import url("<%= request.getContextPath()%>/base/codeEditor/js/docs.css");</style>


<style type="text/css">
  .CodeMirror-line-numbers {
    margin: .4em;
    text-align: right;
    padding: 0;
    font-family: monospace;
    font-size: 10pt;
  }
</style>

<script type="text/javascript">

  var editor;

  function switchTab(id){
    var listMenuEditor = document.getElementById('listMenuEditor');
    var listMenuEditorItems = listMenuEditor.childNodes;

    for (var item in listMenuEditorItems) {
      if (listMenuEditorItems[item].tagName == 'LI') 
        listMenuEditorItems[item].id = null;
    } 

    var selectedTab = document.getElementById(id).parentNode;
    selectedTab.id='activo';

    var editArea = document.getElementById('editArea');
    var testArea = document.getElementById('testArea');

    if (id == 'probar') {
      editArea.style.display = "none";
      testArea.style.display = "block";
    }

    if (id == 'editar') {
      editArea.style.display = "block";
      testArea.style.display = "none";
    }

  }

  function submitCode(method) {
    var editArea = document.getElementById('editArea');
    var textArea = document.createElement("TEXTAREA");
    textArea.style.display = 'none';
    textArea.name = "codEmi.codigo";
    textArea.value = editor.getCode();
    editArea.appendChild(textArea);

    submitForm(method,'');
  }
</script>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/def/AdministrarCodEmi.do">

      <!-- Mensajes y/o Advertencias -->
      <%@ include file="/base/warning.jsp" %>
      <!-- Errors  -->
      <html:errors bundle="base"/>
      
      <h1><bean:message bundle="def" key="def.codEmiViewAdapter.title"/></h1>	

      <table class="tablabotones" width="100%">
              <tr>			
                      <td align="right">
                              <html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
                                      <bean:message bundle="base" key="abm.button.volver"/>
                              </html:button>
                      </td>
              </tr>
      </table>
      
      <!-- CodEmi -->
      <fieldset>
              <legend><bean:message bundle="def" key="def.codEmi.title"/></legend>
              <table class="tabladatos">
                      <tr>
                          <!-- Nombre -->
                          <td><label><bean:message bundle="def" key="def.codEmi.nombre.label"/>: </label></td>
                          <td class="normal"><bean:write name="codEmiAdapterVO" property="codEmi.nombre"/></td>

                          <!-- Descripcion -->
                          <td><label><bean:message bundle="def" key="def.codEmi.descripcion.label"/>: </label></td>
                          <td class="normal"><bean:write name="codEmiAdapterVO" property="codEmi.descripcion"/></td>
                      </tr>

                      <tr>
                          <!-- Tipo de Codigo -->
                          <td><label><bean:message bundle="def" key="def.codEmi.tipCodEmi.label"/>: </label></td>
                          <td class="normal"><bean:write name="codEmiAdapterVO" property="codEmi.tipCodEmi.desTipCodEmi"/></td>
                      </tr>

                      <tr>
                          <!-- Recurso -->
                          <td><label><bean:message bundle="def" key="def.codEmi.recurso.label"/>: </label></td>
                          <td class="normal"><bean:write name="codEmiAdapterVO" property="codEmi.recurso.desRecurso"/></td>
                      </tr>

                      <tr>
                        <td colspan="4"> 
                          <bean:define id="modificarEncabezadoEnabled" name="codEmiAdapterVO" property="modificarEncabezadoEnabled"/>
                          <input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificarEncabezado', 
                            '<bean:write name="codEmiAdapterVO" property="codEmi.id" bundle="base" formatKey="general.format.id"/>');" value="<bean:message bundle="base" key="abm.button.modificar"/>"/>		
                        </td>
                      </tr>
              </table>
      </fieldset>	
      <!-- CodEmi -->

      <fieldset>
            <div id="menu">	
              <ul id="listMenuEditor">
                  <li>
                    <a id="editar" onclick="switchTab(this.id);"><span><bean:message bundle="def" key="def.codEmiAdapter.editar.label"/></span></a>
                  </li>

                  <li>
                    <a id="probar" onclick="switchTab(this.id);"><span><bean:message bundle="def" key="def.codEmiAdapter.probar.label"/></span></a>
                  </li>
                </ul>
            </div>

            <div style="display:block; clear:both;">
              <!-- Area de Edicion -->
              <div id="editArea" class="border" style="height:600px; width:480;">
                  <html:textarea styleId="code" name="codEmiAdapterVO" property="codEmi.codigo"></html:textarea>
                  <script type="text/javascript">
                    // Instanciamos el editor
                    var textarea = document.getElementById("code");
                    editor  = new MirrorFrame(CodeMirror.replace(textarea), {
                      height: "600px",
                      content: textarea.value,
                      parserfile: ["tokenizejavascript.js", "parsejavascript.js"],
                      stylesheet: "<%= request.getContextPath()%>/base/codeEditor/css/jscolors.css",
                      path:       "<%= request.getContextPath()%>/base/codeEditor/js/",
                      textWrapping: false,
                      lineNumbers: true,
                      autoMatchParens: true
                    });
                  </script>
              </div>

              <!-- Area de Prueba-->
              <div id="testArea" class="border" style="display:none; height:600px; width:480;">
                <fieldset>
                  <legend><bean:message bundle="def" key="def.codEmiAdapter.parametrosEntrada.label"/></legend>
                  <table class="tabladatos">
                      <tr>
                        <!-- Cuenta -->
                        <td><label>(*)&nbsp;<bean:message bundle="def" key="def.codEmiAdapter.cuenta.label"/>: </label></td>
                        <td class="normal"><html:text name="codEmiAdapterVO" property="cuenta.numeroCuenta" size="10"  maxlength="10"/></td>
                      </tr>

                      <tr>
                        <!-- Anio -->
                        <td><label>(*)&nbsp;<bean:message bundle="def" key="def.codEmiAdapter.anio.label"/>: </label></td>
                        <td class="normal"><html:text name="codEmiAdapterVO" property="anioView" size="4"  maxlength="4"/></td>
                      </tr>

                      <tr>
                        <!-- Periodo -->
                        <td><label>(*)&nbsp;<bean:message bundle="def" key="def.codEmiAdapter.periodo.label"/>: </label></td>
                        <td class="normal"><html:text name="codEmiAdapterVO" property="periodoView" size="2"  maxlength="2"/></td>
                      </tr>

                      <logic:notEmpty name="codEmiAdapterVO" property="codEmi.recAtrCueEmiDefinition.listGenericAtrDefinition">
                          <logic:iterate id="GenericAtrDefinition" name="codEmiAdapterVO" property="codEmi.recAtrCueEmiDefinition.listGenericAtrDefinition" indexId="count">
                                <bean:define id="AtrVal" name="GenericAtrDefinition"/>
                              <%@ include file="/def/atrDefinition4Edit.jsp" %>
                          </logic:iterate>
                      </logic:notEmpty>

                  </table>

                  <p align="center">
                      <html:button  property="btnAccionBase" styleClass="boton" onclick="submitCode('testCode');">
                        <bean:message bundle="def" key="def.codEmiAdapter.probar.label"/>
                      </html:button>
                  </p>
                </fieldset>
                <fieldset>
                 <legend><bean:message bundle="def" key="def.codEmiAdapter.salida.label"/></legend>
                  <p align="left">
                      <html:textarea style="width:610px; height:270px; font-family: monospace; font-size: 8pt; color:grey;" name="codEmiAdapterVO" property="evalOutput" readonly="true"></html:textarea>
                  </p>
                </fieldset>
              </div>
          </div>
      </fieldset>

      <script type="text/javascript">
        switchTab('<bean:write name="codEmiAdapterVO" property="currentTab"/>');
      </script>

      <table class="tablabotones" width="100%">
        <tr>
            <td align="left" width="50%">
              <html:button property="btnVolver"  styleClass="boton" onclick="submitCode('volver');">
                <bean:message bundle="base" key="abm.button.volver"/>
              </html:button>
            </td>
            <td align="right" width="50%">
                <html:button property="btnAccionBase"  styleClass="boton" onclick="submitCode('guardar');">
                  <bean:message bundle="def" key="def.codEmiAdapter.button.grabar"/>
                </html:button>
            </td>
          </tr>
      </table>
      <input type="hidden" name="method" value=""/>
      <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
      <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

      <input type="hidden" name="selectedId" value=""/>
      <input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
        
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
<!-- codEmiViewAdapter.jsp -->