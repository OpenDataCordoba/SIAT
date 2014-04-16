    <script type="text/javascript" language="javascript">

    function goPage(pageNumber) {
        var field;
        
        fieldPageNumber = document.getElementById('pageNumber');
        fieldPageNumber.value = pageNumber;
        
        fieldPageMethod = document.getElementById('pageMethod');
        
        submitForm(fieldPageMethod.value, '0');
    }

    </script>

		              <logic:equal name="pager" property="hasPrevPage" value="false" >
		                <bean:message bundle="base" key="pager.button.textlink.anterior"/>
		              </logic:equal>
		              <logic:equal name="pager" property="hasPrevPage" value="true" >
		                <!-- a title="<bean:message bundle="base" key="pager.button.pagina.primera"/>" style="cursor: pointer; cursor: hand;" onClick="javascript:goPage('1')">&lt;&lt;</a>&nbsp; -->
		                <a title="<bean:message bundle="base" key="pager.button.pagina.anterior"/>" style="cursor: pointer; cursor: hand;" onClick="javascript:goPage('<bean:write name="pager" property="prevPage" bundle="base" formatKey="general.format.id"/>')"><bean:message bundle="base" key="pager.button.textlink.anterior"/></a>
		              </logic:equal>

		              &nbsp;<bean:write name="pager" property="pageNumber" bundle="base" formatKey="general.format.id"/>&nbsp;
		              
   		              <logic:equal name="pager" property="hasNextPage" value="false" >
		                <bean:message bundle="base" key="pager.button.textlink.siguiente"/>
		              </logic:equal>
		              <logic:equal name="pager" property="hasNextPage" value="true" >
		                <a title="<bean:message bundle="base" key="pager.button.pagina.siguiente"/>" style="cursor: pointer; cursor: hand;" onClick="javascript:goPage('<bean:write name="pager" property="nextPage" bundle="base" formatKey="general.format.id"/>')"><bean:message bundle="base" key="pager.button.textlink.siguiente"/></a>&nbsp;
		                <!-- a title="<bean:message bundle="base" key="pager.button.pagina.ultima"/>" style="cursor: pointer; cursor: hand;" onClick="javascript:goPage('<bean:write name="pager" property="maxPageNumber" bundle="base" formatKey="general.format.id"/>')">&gt;&gt;</a -->
		              </logic:equal>
