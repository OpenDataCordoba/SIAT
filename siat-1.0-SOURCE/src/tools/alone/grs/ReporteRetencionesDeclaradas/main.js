// Reporte Convenios Caducos
// Mantis 0007884: Prod. I - GRS -> AFIP - Reporte de Retenciones Declaradas	
// Fecha: 19/04/2011
// Toma como filtro rangos de importes, fechas de presentacion, anio/periodo



Grs.load("<grs.js>");
Grs.load("<siat/siat.js>");
Grs.load("<siat/const.js>");

Grs.setDebug(0);

Siat.ReporteRetencionesDeclaradas = new function () {
	var Sql = Grs.sql(Siat.DsName);
	var Adp = Grs.adp();
	var Const = Siat.Const;
	
	//Sqls
	
	var SqlTmpRetenciones = 
			" SELECT cuit, anio, periodo, fechavencimiento, fechapresentacion, " +
			" cuitagente, denominacion, tipodeduccion, fecha, nroconstancia, importe " +
			" FROM afi_fordecjur f, afi_retyper r " +
			" WHERE f.id = r.idfordecjur " +
			/*
			" AND fechapresentacion >= #d AND fechapresentacion =< #d " + //Rango fechapresentacion
			" AND importe >= i# AND importe =< i# " + //Rango importe
			" AND anio >= i# AND anio =< i# " +		  //Rango anio
			" AND periodo >= i# AND periodo =< i# " + //Rango periodo
			*/
			" INTO TEMP tmpRetenciones";
	
	var SqlOut = "SELECT * from tmpRetenciones"; 
	
	
	// processReport: Ejecuta las consultas necesarias para generar la 
	// tabla temporal necesaria para luego generar la salida.
	var processReport = function (params) {
		Adp.log("Comienza Procesamiento consulta reporte Retenciones Declaradas");

		Sql.exec(SqlTmpRetenciones);
		
//		Adp.log("Seleccionando convenios desde Seleccion almacenada %s", params.idselam);
//		Sql.exec(SqlTmpRetenciones, params.idselam);	

//		Adp.log("Generando tabla de Resultados");
//		Sql.exec(SqlTmpResultado, params.idtipobjimpatr)
					
		Adp.log("Finalizando construccion consulta reporte Retenciones Declaradas\n")
	}

	// outputReport: Recorre las tablas de resultado y genera los archivos de salida.
	// Tipicamente este metodo recorre con un cursor las tabla temporal generada,
	// y formatea y genera el resultado en un archivo o pdf/html/csv
	var outputReport = function (params) {

		Adp.log("Iniciando construccion reporte Retenciones Declaradas...\n");

		var filename = Adp.makeFileName("RetencionesDeclaradas", "csv");  
		var filepath = Adp.addFile(filename, "Retenciones Declaradas", "Reporte de Retenciones Declaradas");
			
		var out = Grs.outCsv(filepath);

		out.separator("|");
		out.head(["CUIT","Anio","Periodo","Fecha Vencimiento","Fecha Presentacion",
		          "CUIT Agente","Denominacion","Tipo Deduccion","Fecha","Nro. Constancia","Importe"]);
		
		out.order(["cuit","anio","periodo","fechavencimiento","fechapresentacion",
		           "cuitagente","denominacion","tipodeduccion","fecha","nroconstancia","importe"]);

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
		Grs.debug("Retenciones Declaradas\n");

//		var params = Adp.getParameters();
//		
//		// Parametros de test como si fueran chantados por la interfaz.
//		if (params == null) {
//			params = {idselam: 116, idtipobjimpatr: 52};
//		}
		// Fin parametros test
		var params = "";
		
		processReport(params);
		outputReport(params);
		Sql.close();
	}


}

Siat.ReporteRetencionesDeclaradas.main();