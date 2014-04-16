// Reporte Convenios Caducos
// Mantis 0007910: Prod. I - GRS -> Reporte de Convenios Caducos 
// Fecha: 19/04/2011
// Toma como input una selección almacenada hecha para un saldo por caducidad masivo y un idtipobjimpatr. 

Grs.load("<grs.js>");
Grs.load("<siat/siat.js>");
Grs.load("<siat/const.js>");

Grs.setDebug(0);

Siat.ReporteConvenioCaduco = new function () {
	var Sql = Grs.sql(Siat.DsName);
	var Adp = Grs.adp();
	var Const = Siat.Const;
	
	//Sqls
	var SqlTmpConvenios = 
		" SELECT idelemento FROM gde_selalmdet " + 
		" WHERE idselalm = #i INTO TEMP tmpConvenios";

	var SqlTmpResultado = 
		" SELECT nroconvenio, numerocuenta, nomtitpri, cuittitpri, desdomenv, strvalor " +
		" FROM tmpConvenios a, gde_convenio c, pad_cuenta x, pad_objimpatrval o " +
		" WHERE 1=1 " +
		" AND idelemento = c.id " +
		" AND c.idcuenta = x.id " +
		" AND x.idobjimp = o.idobjimp " +
		" AND idtipobjimpatr = #i " +
		" INTO TEMP tmpResultado";
	
	var SqlOut = "SELECT * from tmpResultado"; 
	
	
	// processReport: Ejecuta las consultas necesarias para generar la 
	// tabla temporal necesaria para luego generar la salida.
	var processReport = function (params) {
		Adp.log("Comienza Procesamiento consulta del Reporte Convenios Caducos");

		Adp.log("Seleccionando convenios desde Seleccion almacenada %s", params.idselam);
		Sql.exec(SqlTmpConvenios, params.idselam);			

		Adp.log("Generando tabla de Resultados");
		Sql.exec(SqlTmpResultado, params.idtipobjimpatr)
					
		Adp.log("Finalizando construccion consulta Convenios Caducos\n")
	}

	// outputReport: Recorre las tablas de resultado y genera los archivos de salida.
	// Tipicamente este metodo recorre con un cursor las tabla temporal generada,
	// y formatea y genera el resultado en un archivo o pdf/html/csv
	var outputReport = function (params) {

		Adp.log("Iniciando construccion reporte Convenios Caducos\n");

		var filename = Adp.makeFileName("ConveniosCaducos", "csv");  
		var filepath = Adp.addFile(filename, "Convenios Caducos", "Reporte de Convenios Caducos");
			
		var out = Grs.outCsv(filepath);
		
		out.separator("|");
		out.head(["Numero Convenio","Numero Cuenta","Nombre Titular Principal","CUIT Titular Principal","Domicilio Envio","Valor"]);
		// Ordena key del mapa resultado.
		out.order(["nroconvenio","numerocuenta","nomtitpri","cuittitpri","desdomenv","strvalor"]);
		
		var cursor = Sql.cursor(SqlOut);
		var count = 0;
		while (row = cursor.read()) {
			out.row(row);
			++count;
		}
		
		if (count == 0) {
			out.cell("Sin Resultados");	
		}
		
		out.close();
		Adp.log("Finaliza Procesamiento del Reporte");
	}

	this.main = function () {
		Grs.debug("Reporte Convenios Caducos\n");

		var params = Adp.getParameters();
		
		// Parametros de test como si fueran chantados por la interfaz.
		if (params == null) {
			params = {idselam: 116, idtipobjimpatr: 52};
		}
		// Fin parametros test
		
		processReport(params);
		outputReport(params);
		Sql.close();
	}


}

Siat.ReporteConvenioCaduco.main();