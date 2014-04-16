<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

						<!-- Boton Ver Log -->
						<!-- logic:equal name="corridaVO" property="activarEnabled" value="enabled" -->
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitDownload('downloadLogFile', '');" disabled='false'>
								<bean:message bundle="pro" key="pro.abm.button.verLog"/>
							</html:button>&nbsp;&nbsp;
						<!-- /logic:equal -->
	