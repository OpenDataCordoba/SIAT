// Reporte de Retenciones Declaradas
// Mantis 0007884: Prod. I - GRS -> AFIP - Reporte de Retenciones Declaradas
// Fecha: 29/04/2011
//===================================================================
// Criterios de búsqueda:
//----------------------
//- rangos de importes, fechas de presentacion, anio/periodo

Grs.load("<grs.js>");
Grs.load("<siat/siat.js>");
Grs.load("<siat/const.js>");

Grs.setDebug(2);

Siat.ReporteNovedadRS = new function () {
	var Sql = Grs.sql(Siat.DsName);
	var Adp = Grs.adp();
	var Const = Siat.Const;
	
	//Sqls
	var SqlTmpRetenciones = 
		" SELECT cuit, anio, periodo, fechavencimiento, fechapresentacion, " +
		" cuitagente, denominacion, tipodeduccion, fecha, nroconstancia, importe " +
		" FROM afi_fordecjur f, afi_retyper r " +
		" WHERE f.id = r.idfordecjur " +
		//-- rango de fechas de adhesion
		" [[ AND fechapresentacion >= #d]] "+ 
		" [[ AND fechapresentacion <= #d]] " +
		//-- rango de importes
		" [[ AND importe >= #i ]]"+
		" [[ AND importe <= #i ]]" +
		//-- rango de anios
		" [[ AND TO_DATE (anio||'/'||periodo ,'%Y/%m') >= TO_DATE(#s ,'%Y/%m') ]]" +
		" [[ AND TO_DATE (anio||'/'||periodo ,'%Y/%m') <= TO_DATE(#s ,'%Y/%m') ]]" +
		" INTO TEMP tmpRetenciones";
	
	var SqlTmpRetencionesDrop = 
		"[noerr] drop table tmpRetenciones";

	var SqlOut = "SELECT * from tmpRetenciones"; 
	
	// processReport genera el resultado del proceso 
	var process = function (p) {
		var params = [p.fechaPresDesde, p.fechaPresHasta, p.importeDesde,
		              p.importeHasta, p.anioPerDesde,p.anioPerHasta];
		
		Adp.log("Comienza Procesamiento consulta reporte Retenciones Declaradas");

		Sql.exec(SqlTmpRetencionesDrop);	
		Sql.exec(SqlTmpRetenciones,params);
					
		Adp.log("Finalizando construccion consulta reporte Retenciones Declaradas\n")
	}

	// outputReport: Recorre las tablas de resultado y genera los archivos de salida.
	var output = function (params) {
		Adp.log("Comienza construccion de salida\n");
		
		//generamos csv
		var csvpath, csvout, csvcount;
		csvpath = Adp.makeFileName("RetencionesDeclaradas", "csv");  
		csvout = Grs.outCsv(csvpath, {separator:"|"});
		csvcount = report(csvout, params);
		Adp.addFile(csvpath, "Retenciones Declaradas", "Reporte de Retenciones Declaradas CSV", csvcount);
		csvout.close();
		  
		//generamos html
		var htmlpath, htmlout, htmlcount;
		htmlpath = Adp.makeFilePath("RetencionesDeclaradas", "html");
		htmlout = Grs.outHtml(htmlpath, {});
		htmlcount = report(htmlout, params);		
		Adp.addFile(htmlpath, "Retenciones Declaradas", "Reporte de Retenciones Declaradas PDF", htmlcount);
		htmlout.close();
	
		Adp.log("Finaliza construccion de salida");

	}
	
	// report: Genera un reporte en el archivo independiente del formato.
	// retorna la cantidad de registros procesados.
	var report = function (out, params) {
		
		// Seccion de campos
		out.fieldset("Parametros");
		
		out.field("Fecha Pres. Desde:", params.fechaPresDesde);
		out.field("Fecha Pres. Hasta:", params.fechaPresHasta);
		out.field("Importe Desde:", params.importeDesde);
		out.field("Importe Hasta:", params.importeHasta);
		out.field("Anio/Periodo Desde:", params.anioPerDesde);
		out.field("Anio/Periodo Hasta:", params.anioPerHasta);
		out.endfieldset();

		out.table("Resultados");

		// titulos de las columnas
		out.thead(["CUIT","Anio","Periodo","Fecha Vencimiento","Fecha Presentacion",
		          "CUIT Agente","Denominacion","Tipo Deduccion","Fecha","Nro. Constancia","Importe"]);
		
		// ordenar segun estos key del row
		out.order(["cuit","anio","periodo","fechavencimiento","fechapresentacion",
		           "cuitagente","denominacion","tipodeduccion","fecha","nroconstancia","importe"]);
		
		var count = 0;
		var cursor = Sql.cursor(SqlOut);
		while (row = cursor.read()) {
			out.row(row);
			++count;
		}
		
		if (count == 0)
			out.td("Sin Resultados");	

		out.endtable();
		return count;
	}

	this.main = function () {
		var params = Adp.parameters();

		process(params);
		output(params);
		Sql.close();
	}
}

Siat.ReporteNovedadRS.main();