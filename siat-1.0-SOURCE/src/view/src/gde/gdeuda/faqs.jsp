<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<!DOCTYPE html 
    PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<style type="text/css">
@import url("/siat/styles/tramites.css");
@import url("/siat/styles/siat.css");
</style>
<title>Siat Online</title>

<script type="text/javascript" src="/siat/base/submitForm.js"></script>
<script type="text/javascript" src="/siat/base/calendar.js"></script>


</head>

<body onload="init()" onunload="unload()">
<div id="container">


<div id="top"><a id="rosariogovar" href="#"
	title="Ir a www.rosario.gov.ar"
	onclick="javascript:window.open('http://www.rosario.gov.ar'); return false;"></a>
<div id="iconostop">
<ul>
	<li><a href="http://www.rosario.gov.ar/tramitesonline/inicio.do"
		class="Inicio" id="icohome" title="Inicio"></a></li>
	<li><a href="http://www.rosario.gov.ar/tramitesonline/busqueda.do"
		class="Busqueda" id="icobuscar" title="Busqueda"></a></li>
	<li><a href="http://www.rosario.gov.ar/tramitesonline/ayuda.do"
		class="Ayuda" id="icoayuda" title="Ayuda"></a></li>
	<li><a href="http://www.rosario.gov.ar/tramitesonline/contacto.do"
		class="Contacto" id="icontacto" title="Contacto"></a></li>


	<li><a href="/siat/login/LoginSsl.do?method=webInit"
		title="Log-in (Web)" id="icologin"></a></li>
	<li><a href="/siat/login/LoginSsl.do?method=intranetInit"
		title="Log-in (Intranet)" id="icologin"></a></li>

</ul>
</div>
</div>


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



<div id="menu">
<ul>
	<li><a href="http://www.rosario.gov.ar/tramitesonline/inicio.do"><span>Inicio</span></a></li>
	<li><a href="http://www.rosario.gov.ar/tramitesonline/todos.do"><span>Todos</span></a></li>
	<li><a href="http://www.rosario.gov.ar/tramitesonline/tema.do"><span>Temas</span></a></li>

	<li><a href="http://www.rosario.gov.ar/tramitesonline/lugar.do"><span>Lugares</span></a></li>
</ul>
</div>

<ul>
</ul>
<div id="contenido"><%@ include file="/gde/gdeuda/includeDivButtons.jsp"%>
<div style="clear: both;"></div>
<h1>Preguntas Frecuentes Generales SIAT</h1>
<h3>Compartidas con todos los tr&aacute;mites de los recursos TGI, DReI, EtuR y CdM</h3>
<dl>
	<dt>&iquest;Qu&eacute; acciones me permite realizar este sistema?</dt>
	<dd>A trav&eacute;s de este sistema puede: <strong> 1)</strong>
	reimprimir cualquier aviso-recibo  correspondiente a un tributo
	municipal que se encuentre en gesti&oacute;n de cobro administrativo, y cuotas
	de Convenios de Pago que no hayan caducado; <strong> 2)</strong>
	visualizar si la cuenta registra deuda concursal o en gesti&oacute;n
	judicial; <strong> 3)</strong> reclamar la falta de asentamiento de
	pagos efectuados que figuren como impagos a la fecha de asentamiento
	que se indica; <strong> 4)</strong> cambiar el domicilio de
	env&iacute;o de la TGI y/o Contribuci&oacute;n de Mejoras.</dd>

	<dt>&iquest;C&oacute;mo obtengo el c&oacute;digo de gesti&oacute;n
	personal?</dt>
	<dd>El c&oacute;digo de gesti&oacute;n personal posibilita acceder
	a informaci&oacute;n de inter&eacute;s del contribuyente, es personal e
	intransferible y destinado al uso exclusivo de aqu&eacute;l.<br />
	El c&oacute;digo de gesti&oacute;n personal se encuentra ubicado en el sector
	superior de su boleta junto con el Nº de cuenta. Ver <a
		href="http://www.rosario.gov.ar/sitio/informacion_municipal/contenido_boletas.jsp">contenido
	de las boletas</a></dd>

	<dt>&iquest;D&oacute;nde efect&uacute;o el reclamo por la
	no-recepci&oacute;n de los recibos?</dt>
	<dd>Este reclamo se puede realizar personalmente en el <a
		href="http://www.rosario.gov.ar/sitio/gobierno/cmds.jsp?nivel=Servicios&ult=Se_3">Centro
	Municipal de Distrito</a> m&aacute;s cercano a su domicilio o, en el caso de
	tratarse de un Contribuyente Estrat&eacute;gico de Rosario, en el edificio ex
	Aduana (Urquiza 902, Of. 139).</dd>

	<dt>Estoy intentando reimprimir un recibo y la p&aacute;gina web
	arroja un error &iquest;c&oacute;mo debo proceder?</dt>
	<dd>Si surge un problema con la p&aacute;gina web env&iacute;e un
	e-mail a <a href="mailto:tgi-user@rosario.gov.ar">tgi-user@rosario.gov.ar</a>
	para poder comunicarse con los encargados del soporte t&eacute;cnico.</dd>

	<dt>&iquest;C&oacute;mo puedo abonar los tributos municipales?
	&iquest;D&oacute;nde puedo abonar las boletas reimpresas v&iacute;a web?</dt>
	<dd>Los contribuyentes de la Tasa General de Inmuebles (TGI)
	pueden cancelar su obligaci&oacute;n tributaria -anticipo mensual- a trav&eacute;s de:</dd>

	<ul class="vinieta">
		<li><strong>Internet:</strong> ingresando al tr&aacute;mite on-line <a
			href="http://www.rosario.gov.ar/tramitesonline/pago_tgi.do">Pago
		on-line de TGI</a></li>
		<li><strong>Pago autom&aacute;tico de servicios (PAS):</strong> en los
		cajeros autom&aacute;ticos de las redes <a
			href="http://www.pagoslink.com.ar/">Link</a> y <a
			href="https://paysrv2.pagomiscuentas.com/Inicio.html">Banelco</a></li>
		<li><strong>D&eacute;bito autom&aacute;tico:</strong> formalizando su
		adhesi&oacute;n a trav&eacute;s de la instituci&oacute;n bancaria en la cual posea
		una cuenta.</li>
	</ul>
	
	<dd>El resto de los tributos municipales -con recibo original o
	reimpresos v&iacute;a web- solamente pueden ser abonados en las
	entidades bancarias habilitadas:</dd>
	<ul class="vinieta">
		<li><a href="http://www.rosario.gov.ar/sitio/gobierno/cmds.jsp?nivel=Servicios&ult=Se_3">
		Centro Municipal de Distrito</a></li>
		<li><a href="http://www.bmros.com.ar/localizaciones/localizaciones.html">
		Banco Municipal de Rosario</a></li>
		<li>Banco Supervielle SA</li>
		<li>Banco Credicoop Coop. Ltdo.</li>
		<li>Banco ITAU SA</li>
		<li>Banco de la Naci&oacute;n Argentina</li>
		<li>Banco Provincia de C&oacute;rdoba</li>
		<li>Nuevo Banco de Entre R&iacute;os</li>
		<li>Nuevo Banco de Santa Fe SA</li>
		<li>Banco Macro SA</li>
		<li>Multipagos</li>
	</ul>
	<dd>Otros lugares de cobro:</dd>

	<dd><strong>Rosario:</strong></dd>
	<ul class="vinieta">
		<li>Asoc. Mutual de Escribanos</li>
		<li>Mutual Swift</li>
		<li>Mutual Asoc. M&eacute;dica</li>
		<li>Mutual San Crist&oacute;bal</li>
	</ul>
	<dd><strong>Provincia de Santa Fe:</strong></dd>
	<ul class="vinieta">
		<li>Asoc. Mutual Racing de Villada (Villada)</li>
		<li>Asoc. Mutual Empalme Central (Va. Constituci&oacute;n)</li>
		<li>Mutual Personal John Deere (Gdro. Baigorria)</li>
		<li>Asoc. Mutual Belgrano (Sta. Isabel)</li>
		<li>Asoc. Mutual Belgrano (Ma. Teresa)</li>
		<li>Club Pe&ntilde;arol Mutual y Biblioteca de El Ortondo</li>
		<li>Asoc. Mutual de Venado Tuerto</li>
		<li>Cooperaci&oacute;n Mutual Patronal (Venado Tuerto)</li>
		<li>Mutual Uni&oacute;n de Alvarez</li>
		<li>Mutual Independencia (Rold&aacute;n)</li>
		<li>Mutual Independencia (Carcara&ntilde;&aacute;)</li>
		<li>Mutual Independencia (Casilda)</li>
		<li>Club Patronal Mutual y Biblioteca de El Ortondo-Sucursal Firmat</li>
	</ul>

	<dt>&iquest;C&oacute;mo abonar un per&iacute;odo de TGI pasada la
	fecha de su vencimiento original?</dt>
	<dd>Presentando el recibo original, sin necesidad de reimprimirlo,
	en los bancos habilitados:</dd>
	<ul class="vinieta">
		<li>Banco Municipal de Rosario</li>
		<li>Banco Credicoop Coop. Ltdo.</li>
		<li>Nuevo Banco de Santa Fe SA</li>
		<li>Nuevo Banco de Entre R&iacute;os</li>
		<li>Banco de la Naci&oacute;n Argentina</li>
		<li>Banco Macro (s&oacute;lo a clientes)</li>
	</ul>
	<dd>Mediante la lectura del C&oacute;digo de barras, en el momento del
	pago, el banco calcular&aacute; el inter&eacute;s diario acumulado.</dd>
	<dt>&iquest;Puedo reimprimir v&iacute;a web cuotas de convenios de
	tributos municipales?</dt>
	<dd>Es posible realizar la reimpresi&oacute;n siempre que el mismo no
	est&eacute; caduco.</dd>

	<dt>&iquest;Cu&aacute;ndo se considera &quot;caduco&quot; un
	Convenio de pago?</dt>
	<dd>Un convenio es caduco cuando se comprueba un atraso de 90
	d&iacute;as corridos en el pago de una cuota o la acumulaci&oacute;n de
	3 cuotas impagas.</dd>


	<dt>&iquest;Cu&aacute;l es la tasa de inter&eacute;s aplicable a
	las deudas tributarias?</dt>
	<dd>Desde el 1&deg; de enero de 2008, las deudas tributarias se
	actualizan por una tasa del 0.06666% diaria. 
	<a href="http://www.rosario.gov.ar/normativa/ver/visualExterna.do?accion=verNormativa&amp;idNormativa=49434">
	Decreto N&deg; 2720/07</a>.</dd>

</dl>



</div>

<h1>&nbsp;</h1>



<div id="pie">Sitio desarrollado por la Municipalidad de Rosario.
Santa Fe. Argentina. 1997 - 2010. Todos los derechos reservados.<br/>
En algunas secciones es necesario tener instalado el 
<a href="http://www.adobe.es/products/acrobat/readstep2.html" target="_blank">Acrobat Reader</a>.
</div>



</div>
</body>

</html>
