<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>

<script type="text/javascript">
var TxInicial = 12;
var textoHtml;
var numCol = 1;
function zoom(Factor) {
    tx = document.getElementById("container");
    TxInicial = TxInicial + Factor;
    tx.style.fontSize = TxInicial;
    if (numCol > 1) {
        tb = document.getElementById("container");
        tb.style.fontSize = TxInicial;
    }
}
</script>

<ul>
	<li id="activo">
		<a href="/tramitesonline/inicio.do"><span>Inicio</span></a>
	</li>
	<li>
		<a href="/tramitesonline/todos.do"><span>Todos</span></a>
	</li>
	<li>
		<a href="/tramitesonline/tema.do"><span>Temas</span></a>
	</li>
	<li>
		<a href="/tramitesonline/lugar.do"><span>Lugares</span></a>
	</li>
</ul>

<div id="tools">
	<ul>
		<li><a href="#" onclick="javascript:zoom(1);">A+</a></li>
		<li><a href="#" onclick="javascript:zoom(-1);">A-</a></li>
	</ul>
</div>
