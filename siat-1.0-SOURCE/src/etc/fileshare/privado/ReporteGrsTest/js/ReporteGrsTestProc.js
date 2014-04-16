// Reporte Grs Test
// Fecha: 19/04/2011
// Toma como input un id de recurso y genera un archivo 
// con info de las cuentas del recurso 

Grs.load("<grs.js>");
Grs.load("<siat/siat.js>");
Grs.load("<siat/const.js>");

Grs.setDebug(1);

Siat.ReporteGrsTest = new function () {
	var Const = Siat.Const;
	var Adp = Grs.adp();
	var Sql = Grs.sql(Const.DsSiat);
	
	//Sqls
	SqlCuentaDrop = "[noerr] drop table tmpOut";
	SqlCuenta = "select *, 12.34 dd from pad_cuenta where idrecurso = #i" +
				" [[and fechaalta < #d and (fechabaja > #d or fechabaja is null)]]" + //seccion condicional
	            " into temp tmpOut";
	
	SqlOut = "select * from tmpOut";
	
	// processReport genera el resultado del proceso 
	var process = function (params) {
		Adp.log("Comienza Procesamiento Reporte Grs Test");

		Adp.message("Generando Reporte paso 1...");
		Sql.exec(SqlCuentaDrop);
		Sql.exec(SqlCuenta, params.idRecurso, params.fechaAnalisis, params.fechaAnalisis);

		Adp.log("Finalizando Procesamiento\n");
	}

	// outputReport: Recorre las tablas de resultado y genera los archivos de salida.
	var output = function (params) {
		Adp.log("Comienza construccion de salida\n");

		//generamos csv
		var csvpath, csvout, csvcount;
		csvpath = Adp.makeFilePath("salida/Cuentas", "csv");
		csvout = Grs.outCsv(csvpath, {separator:"|"});
		csvcount = report(csvout, params);
		Adp.addFile(csvpath, "Test Grs (csv)", "Reporte de Test de Grs", csvcount);
		csvout.close();
		  
		//generamos html
		var htmlpath, htmlout, htmlcount;
		htmlpath = Adp.makeFilePath("salida/Cuentas", "html");
		htmlout = Grs.outHtml(htmlpath, {});
		htmlcount = report(htmlout, params);		
		Adp.addFile(htmlpath, "Test Grs (html)", "Reporte de Test de Grs", htmlcount);
		htmlout.close();
	
		Adp.log("Finaliza construccion de salida");
	}
	
	// report: Genera un reporte en el archivo independiente del formato.
	// retorna la cantidad de registros procesados.
	var report = function (out, params) {
		//cabecera para html
		if (out.type() == "html")
			out.include("<html/stdhead.html>");
		
		//titulos
		out.title("Reporte Demo Grs");
		out.p("Reporte Demo Grs para usar como plantilla al empezar otros reportes");
		
		// Seccion de campos
		out.fieldset("Parametror");
		out.field("Recurso", params.idRecurso);
		out.endfieldset();

		out.table("Resultados");
				
		// titulos de las columnas
		out.thead(["Id","Numero Cuenta", "Titular Principal", "CUIT Titular Principal", "Domicilio Envio"]);

		// ordenar segun estos key del row
		out.order(["id", "numerocuenta", "nomtitpri", "cuittitpri", "desdomenv", "dd"]);
		
		var count = 0;
		var total = Sql.count(SqlOut);
		var cursor = Sql.cursor(SqlOut);
		while (row = cursor.read()) {
			if (count % 10000 == 0)
				Adp.message("Generando salida %s... %.0f%%", out.type(), count*100/total);
			out.row(row);
			++count;
		}
		
		if (count == 0)
			out.td("Sin Resultados");	

		out.endtable();

		//pie para html
		if (out.type() == "html")
			out.include("<html/stdfoot.html>");
		return count;
	}

	this.main = function () {
		var params = Adp.parameters();
		
		// Parametros de test como si fueran chantados por la interfaz.
		if (params == null) {
		   	//Packages.ar.gov.rosario.siat.def.iface.service.DefServiceLocator.DefServiceLocator.getConfiguracionService().initializeSiat();
			params = {
					idRecurso: 163,
					fechaAnalisis: new Date()
					};
		}
		// Fin parametros test
		
		process(params);
		output(params);
		Sql.close();
	}


}

Siat.ReporteGrsTest.main();
