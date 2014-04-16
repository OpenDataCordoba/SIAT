// Reporte Recaudacion Actividad
// Mantis 0007882: Prod. I - GRS -> Reporte Recaudacion Actividad
// Fecha: 22/04/2011
// Toma como input una selección almacenada hecha para un saldo por caducidad masivo y un idtipobjimpatr. 

Grs.load("<grs.js>");
Grs.load("<siat/siat.js>");
Grs.load("<siat/const.js>");
Grs.load("<siat/util.js>");

Grs.setDebug(1);

Siat.ReportRecaudacionActividad = new function () {
	
var Sql = Grs.sql(Siat.DsName);
var SqlIndet = Grs.sql(Siat.DsNameIndet);
var Adp = Grs.adp();
var Const = Siat.Const;
var Util = Siat.Util;


	//Sqls

	// Linea de transaccion ficticia con los datos obtenidos
	//    SS     NNNNNNNNNN    CCCCCC  YYYYMMDD  IIIIIII.II  CCC    FFFF  IIIIIIIIIIII
	//  sistema+nroComprobante+clave+fechaPago+importePago+caja+formulario+idTranAfip
		
	//Obtenemos los pagos:	
	var sqlPagos = 
			"SELECT linea[50,55] idtranafip, linea[1,2] sistema, " +
			"linea[4,12] cuenta, linea[13,18] periodo, linea[40,43] form, d.* " +
			"FROM bal_archivo a, bal_tranarc d " +
			"WHERE a.id = d.idarchivo " +
			"AND prefix = #s " + // Prefijo
			"AND linea[13,18] IN (#s) " + // Meses
			"AND linea[40,43] <> 6057 " +  // Formulario
			"AND linea[1,2] = #i " + // Sistema
			"AND a.idbalance IS NOT NULL " +
			"INTO TEMP tmp_rgtodo";
			
	//Obtenemos los pagos:	
	var sqlPagosOK =
			"SELECT * " +
			"FROM tmp_rgtodo " +
			"WHERE 1=1" +
			"AND sistema||cuenta||periodo NOT IN " +
			"	(" +
			"	SELECT sist_o||trim(cuenta_o)||clave_o FROM  indet:\"informix\".indet_tot" +
			"	) " +
			"AND sistema||cuenta||periodo NOT IN " +
			"	(" +
			"	SELECT sist_o||trim(cuenta_o)||clave_o FROM  indet:\"informix\".duplicados" +
			"	) " +
			"INTO TEMP tmp_pagook";
			
	//Obtenemos los pagas_rg:	
	var sqlPagasRG =
			"SELECT d.id iddeuda, numerocuenta, anio, periodo, idreccladeu, importe, saldo,fechapago " + 
			"FROM gde_deudaadmin d, pad_cuenta c " +
			"WHERE 1=1 " +
			"AND d.idrecurso = #i " +
			"AND anio IN (#s) " +
			"AND periodo IN (#s) " +
			"AND d.idcuenta = c.id " +
			"AND (fechapago IS NOT NULL OR (saldo > 0 AND saldo < importe)) " +
			"AND idreccladeu in (5,6) " +
			"INTO temp pagas_rg";
	
	//Obtenemos los ForDecJur	
	var sqlForDecJur = 
			"SELECT cuit, anio, periodo, max(id) idfordecjur " +
			"FROM afi_fordecjur f " +
			"WHERE 1=1 " +
			"AND anio IN (#s) " +
			"AND periodo IN (#s) " +
			"AND idrecurso = #i " +
			"GROUP BY 1,2,3 " +
			"INTO TEMP fordecjur";
	
	// Apertura por local
	var sqlLocales1 = 
			"SELECT f.idfordecjur, cuit, numerocuenta, nombrefantasia, anio, periodo, derdettot, derecho, subtotal1, derechototal, id idlocal " +
			"FROM fordecjur f, afi_local a " +
			"WHERE 1=1 " +
			"AND f.idfordecjur = a.idfordecjur " +
			"AND derechototal IS NOT NULL " +
			"INTO TEMP locales";
			
	//Cruzamos con los pagos para ver si hay algo pago para ese periodo
	var sqlLocales2 = 
			"SELECT l.*, 'PAGA' estado FROM locales l " +
			"WHERE 1=1 " +
			"AND numerocuenta||anio||periodo " +
			"IN (select numerocuenta||anio||periodo from pagas_rg) " +
			"UNION ALL " +
			"SELECT l.*, 'IMPAGA' estado FROM locales l " +
			"WHERE 1=1 " +
			"AND numerocuenta||anio||periodo " +
			"NOT IN (select numerocuenta||anio||periodo from pagas_rg) " +
			"INTO TEMP locales_2";
	
	//Abrimos por actividad
	var sqlFinal1 =
			"SELECT l.cuit, nombrefantasia, l.numerocuenta, fechapresentacion, " +
			"l.anio, l.periodo, l.estado, derdettot, l.derecho global, subtotal1, derechototal, codactividad, " +
			"baseimponible, baseimpajustada, alicuota, cantidad, unidadmedida, " +
			"derechocalculado, derechodet, ((derechodet/ derdettot) * 100) porc2, " +
			"l.idfordecjur, l.idlocal " +
			"FROM locales_2 l, afi_decactloc a, afi_fordecjur f " +
			"WHERE 1=1 " +
			"AND l.idlocal = a.idlocal " +
			"AND l.idfordecjur = f.id " +
			"AND l.derdettot <> 0 " +
			"UNION ALL " +
			"SELECT l.cuit, nombrefantasia, l.numerocuenta, fechapresentacion, " +
			"l.anio, l.periodo, l.estado, derdettot, l.derecho global, subtotal1, derechototal, codactividad, " +
			"baseimponible, baseimpajustada, alicuota, cantidad, unidadmedida, " +
			"derechocalculado, derechodet, 0 porc2, " +
			"l.idfordecjur, l.idlocal " +
			"FROM locales_2 l, afi_decactloc a, afi_fordecjur f " +
			"WHERE 1=1 " +
			"AND l.idlocal = a.idlocal " +
			"AND l.idfordecjur = f.id " +
			"AND l.derdettot = 0 " +
			"ORDER BY l.cuit, l.numerocuenta " +
			"INTO TEMP tmp_final";
	
	var sqlFinal2 =
			"SELECT t.*, c.desdomenv, c.cuittitpri, c.nomtitpri, a.denominacion, desconcepto " +
			"FROM tmp_final t, outer pad_cuenta c, " +
			"OUTER mulator:\"informix\".cuitsafip a, " +
			"OUTER def_recconadec r " +
			"WHERE 1=1 " +
			"AND t.numerocuenta = c.numerocuenta " +
			"AND c.idrecurso = #i " +
			"AND t.cuit = a.cuit " +
			"AND codactividad||'' = codconcepto " +
			"INTO TEMP tmp_final2";
	
	var sqlFinal3 =
			"SELECT t.*, id_persona " +
			"FROM tmp_final2 t, outer general:\"informix\".persona_id p " +
			"WHERE 1=1 " +
			"AND cuittitpri[1] = p.cuit00||'' " +
			"AND cuittitpri[3,4] = p.cuit01||'' " +
			"AND cuittitpri[6,13] = p.cuit02||'' " +
			"AND cuittitpri[15] = p.cuit03||'' " +
			"INTO TEMP tmp_final3";sqlLocales2
	
	var sqlFinal4 =
			"SELECT t.*, valor " +
			"FROM tmp_final3 t, OUTER pad_conatrval c " +
			"WHERE 1=1 " +
			"AND t.id_persona = c.idcontribuyente " +
			"AND c.idconatr = 3 AND valor = 1 " +
			"AND fechahasta IS NULL " +
			"INTO TEMP tmp_final4";

	//Para calcular totales y porcentajes por actividad
	var sqlResultTotActiv =
			"SELECT codactividad, desconcepto, sum(derechodet) totactiv " +
			"FROM tmp_final4 " +
			"GROUP by 1,2 " +
			"INTO TEMP tmp_totactiv";
	
	//Valor: Suma Total Actividades
	var sqlValueSumTotActividad =
			"SELECT sum(totactiv)" +
			"FROM tmp_totactiv";
	
	var sqlResultPorActividad2 =
			"SELECT codactividad, desconcepto, totactiv, " +
			"ROUND(((totactiv/#i) * 100),2) porcactiv " +
			"FROM tmp_totactiv " +
			"ORDER BY 4 desc " +
			"INTO TEMP tmp_porcactiv";
	
	//Valor: Suma Porcentajes Actividades
	var sqlValueSumPorActividad =
			"SELECT sum(porcactiv) " +
			"FROM tmp_porcactiv";
	
	// -------------------------------------------------------Por cada Periodo
	
	var sqlResultATotActivMesAnio = 
			"SELECT codactividad, desconcepto, sum(derechodet) totactiv " +
			"FROM tmp_final4 " +
			"WHERE 1=1" +
			"AND anio = #s " +
			"AND periodo = #s  " +
			"GROUP by 1,2 " +
			"INTO TEMP tmp_totactiv#m";
	
	//Valor: Suma Total Actividades
	var sqlValueSumTotActividadMesAnio =
			"SELECT sum(totactiv) " +
			"FROM tmp_totactiv#m";
	 
	var sqlResultPorActivMesAnio =
			"SELECT codactividad, desconcepto, totactiv, " +
			"ROUND(((totactiv/#i) * 100),2) porcactiv " +
			"FROM tmp_totactiv#m " +
			"ORDER by 4 desc " +
			"INTO TEMP tmp_porcactiv#m";
	
	//Valor: Suma Porcentaje Actividades
	var sqlValuePorActivMesAnio =
			"SELECT sum(porcactiv) " +
			"FROM tmp_porcactiv#m";
	
	// Tabal resultado
	var SqlOut = "SELECT * from tmp_final4"; 
	
	
	// processReport: Ejecuta las consultas necesarias para generar la 
	// tabla temporal necesaria para luego generar la salida.	
	var processReport = function (param) {
		Adp.log("Comienza Procesamiento consulta del Recaudacion Actividad");
		Grs.debug("Iniciando consultas SQL\n");
		
		Adp.log("Generando tabla tmp_rgtodo");
		var pagos = Sql.exec(sqlPagos, param.prefix , param.meses , param.sistema);
				
		Adp.log("Generando tabla tmp_pagook");
		var pagosOK = Sql.exec(sqlPagosOK);
				
		Adp.log("Generando tabla pagas_rg");
		var pagasRG = Sql.exec(sqlPagasRG, param.idrecurso, param.anios , param.meses);
		
		Adp.log("Generando tabla fordecjur");
		var forDecJur = Sql.exec(sqlForDecJur, param.anios , param.meses, param.idrecurso);
		
		Adp.log("Generando tabla locales");
		var locales1 = Sql.exec(sqlLocales1);
				
		Adp.log("Generando tabla locales2");
		var locales2 = Sql.exec(sqlLocales2);
		
		Adp.log("Generando tabla tmp_final");
		var final1 = Sql.exec(sqlFinal1);
		
		Adp.log("Generando tabla tmp_final2");
		var final2 = Sql.exec(sqlFinal2, param.idrecurso);
		
		Adp.log("Generando tabla tmp_final3");
		var final3 = Sql.exec(sqlFinal3);

		Adp.log("Generando tabla tmp_final4");
		var final4 = Sql.exec(sqlFinal4);

		//Para calcular totales y porcentajes por actividad
		var resultTotActiv = Sql.exec(sqlResultTotActiv);

		//Valor: Suma Total Actividades
		var valueSumTotActividad = Sql.value(sqlValueSumTotActividad);
		if(!isNaN(valueSumTotActividad) && valueSumTotActividad != null) 
			var resultPorActividad2 = Sql.exec(sqlResultPorActividad2,valueSumTotActividad);
		else ;//quit

		//Valor: Suma Porcentaje Actividades
		if(!isNaN(valueSumTotActividad) && valueSumTotActividad != null) 
			var valueSumPorActividad = Sql.value(sqlValueSumPorActividad); // Suma de porcentajes para validar.
		else ;//quit

		//Por cada Periodo
		var a=param.anioDesde;

		for(var m=param.mesDesde; m < param.mesHasta; m++) {

			var resultATotActivMesAnio = Sql.exec(sqlResultATotActivMesAnio, a, m, a+m+"");
			var valueSumTotActividadMesAnio = Sql.exec(sqlValueSumTotActividadMesAnio, a+m+"");

			if(!isNaN(valueSumTotActividadMesAnio) && valueSumTotActividadMesAnio != null) {
				var resultPorActivMesAnio = Sql.exec(sqlResultPorActivMesAnio,valueSumTotActividadMesAnio, a+m+"", a+m+"");
				var valuePorActivMesAnio = Sql.value(sqlValuePorActivMesAnio, a+m+""); // Suma de porcentajes para validar.
			}
			else ;


			if(valuePorActivMesAnio == 1) {}
			
		}
			
		Adp.log("Finalizando construccion consultas Recaudacion Actividad\n")
	}

	// outputReport: Recorre las tablas de resultado y genera los archivos de salida.
	// Tipicamente este metodo recorre con un cursor las tabla temporal generada,
	// y formatea y genera el resultado en un archivo o pdf/html/csv
	var outputReport = function (param) {

		Adp.log("Iniciando construccion Reporte Recaudacion Actividad\n");

		var filename = Adp.makeFileName("RecaudacionActividad", "csv");  
		var filepath = Adp.addFile(filename, "Recaudacion Actividad", "Reporte Recaudacion de Actividad");
			
		var out = Grs.outCsv(filepath);
		
		out.separator("|");
		//out.head(["Domicilio Envio","Valor........."]);
		
		// Ordena key del mapa resultado.
		//out.order(["desdomenv","strvalor......"]);
		
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

		var param = Adp.getParameters();
		
		// Parametros de test como si fueran chantados por la interfaz.
		// Construccion temporal para probar cosas de JS (Sintaxis)
		if (param == null) {
			param = 	{	
						idrecurso: 15, 
						idreccladeu: 59, 
						fechadesde:"2010-01-01", 
						fechahasta:"2010-04-25",
						prefix:"os",
						sistema:83
						};
		};
		
		param.mesDesde = param.fechadesde.substring(5, 7);
		param.mesHasta = param.fechahasta.substring(5, 7);
		param.anioDesde = param.fechadesde.substring(0, 4);
		param.anioHasta = param.fechahasta.substring(0, 4);
		param.meses = Util.makeSeparatedForString(param.mesDesde,param.mesHasta);
		param.anios = Util.makeSeparatedForString(param.anioDesde,param.anioHasta);
		param.fechaPago = param.anioDesde + param.mesDesde;
		
		//Grs.debug("////////////////////%s\t%s\t%s\t%s\n%s\n%s",param.mesDesde,param.mesHasta,param.anioDesde,param.anioHasta,param.meses, param.anios);
		// Fin parametros test
		
		processReport(param);
		outputReport(param);
		Sql.close();
	}

	
}

Siat.ReportRecaudacionActividad.main();