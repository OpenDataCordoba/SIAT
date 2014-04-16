/*
Proceso Generador de Atributos de Cuenta y Contribuyentes 
 */

Grs.load("<grs.js>")
Grs.load("<siat/siat.js>")
Grs.load("<siat/const.js>")
Grs.load("<siat/util.js>");

Grs.setDebug(1);

Siat.ProcesoAtributo = new function() {

	var Sql = Grs.sql(Siat.DsName);
	var Adp = Grs.adp();
	var Const = Siat.Const;
	var Util = Siat.Util;

	// Constantes para metodos:

	// Para: promMensualDecla
	var ListaAbrRecClaDeu = [ "RS", "Orig", "Rectif", "AF", "AFHist", "AFAcc" ];
	ListaAbrRecClaDeu = Util.quote(ListaAbrRecClaDeu);

	var SqlRecClaDeu = 				"SELECT id,abrcladeu FROM def_reccladeu WHERE abrcladeu IN (#m) AND idrecurso = #s";
	var SqlDeuda = 					"SELECT idreccladeu, importe, id FROM gde_deudaadmin WHERE idcuenta = #f AND idrecurso = #i AND anio = #i AND periodo = #i";
	var SqlImporte = 				"SELECT SUM(importe) importe FROM gde_deudaadmin WHERE idcuenta = #f AND idrecurso = #i AND anio = #i AND periodo = #i AND idreccladeu IN (#m)";
	var SqlMaxFechaPresentacion = 	"SELECT MAX(fechapresentacion) FROM gde_decjur WHERE idrecurso = #i AND iddeuda = #s";
	var SqlTotalDeclarado = 		"SELECT totaldeclarado, subtotal FROM gde_decjur WHERE fechapresentacion = #d AND idrecurso = #i AND iddeuda = #s";

	var CantAniosDeuda = 6;
	var CantPeriodosDeuda = 12;

	// Fin: promMensualDecla
	
	// Para: promMensualDerPub
	var SqlImporteSinIdRecClaDeu =  "SELECT SUM(importe) importe FROM gde_deudaadmin WHERE idcuenta = #f AND anio = #i AND periodo = #i";
	// Fin: promMensualDerPub
	
	// Para: promMensualDeter
	var SqlImporteAcum =  			"SELECT SUM(importe) importeAcum FROM gde_deudaadmin WHERE idcuenta = #f AND fechavencimiento >= #d AND fechavencimiento < #d";
	var SqlPeriDet =  				"SELECT DISTINCT YEAR(fechavencimiento) anio, MONTH(fechavencimiento) periodo FROM gde_deudaadmin WHERE idcuenta = #f AND fechavencimiento >= #d AND fechavencimiento <  #d GROUP BY 1,2 INTO TEMP tmpPeriodosDeuda";
	var SqlCountPeriDet = 			"SELECT COUNT(*) FROM tmpPeriodosDeuda";
	var SqlDropTmpPeriDet = 		"DROP TABLE tmpPeriodosDeuda";
	
	// Fin: promMensualDeter
	
	
	// constantes de querys
	var SqlCuentas = "select * from pad_cuenta where fechaalta < #d and (fechabaja > #d or fechabaja is null);"// and idrecurso = 15";
		// revisar
		// criterio
	
	var SqlCuentaTieneDeuda = "select count(*) n from #m where idcuenta = #i and saldo > 0";
	var SqlCuentaUltFechaPago = "select max(fechapago) from #m where idcuenta = #i";
	var SqlCuentaMontoTotDeudaHist = "select sum(saldo) from #m where idcuenta = #i";

	// proccesCuenta procesa cada cuenta activa y calcula sus atributos.
	// Calcula los atributos de cuenta y los inserta en las tabla de atributos.
	// Para ver los atributos de
	// El parametro 'fechaAnalisis' se utiliza para determinar la caducidad
	// de cuentas y valores de actualizacion de deuda.
	var processCuentas = function(fechaAnalisis) {
		Adp.log("Comienza Procesar Atributos de Cuenta");

		var cursor = Sql.cursor(SqlCuentas, fechaAnalisis, fechaAnalisis);

		while (cuenta = cursor.read()) {
			var atrs = makeAtrCuenta(cuenta, fechaAnalisis);

			Grs.printf("Atributos de la cuenta: %.0f - %.0f\n%s\n", cuenta.idrecurso, cuenta.id, show_props(atrs));

			// insertAtrCuenta(cuenta, atrs);
			// insertAtrObjImp(cuenta, atrs);
		}
		cursor.close();
	}

	var show_props = function(obj) {
		var result = "";
		for ( var i in obj ) {
			result += "." + i + " = " + obj[i] + "\n";
		}
		return result;
	}

	// makeAtrCuenta retorna un objeto con los atributos de la cuenta
	// calculados.
	// Los atributos calculados por 'cuenta' son:
	// tieneDeudaAdm: String "SI"/"NO" indica si tiene Deuda Administrativa
	// tieneDeudaJud: String "SI"/"NO" indica si tiene Deuda Administrativa
	// ultFechaPago: Date ultima fecha de pago de las deudas (adm y jud).
	// montoTotDeudaHist: Number monto total (adm y jud) de saldo
	// montoTotDeudaAct: Number monto total (adm y jud) de saldo actualizado a
	// 'fechaAnalisis'
	// promMensualEmiDec: Number promedio mensual de emitido o declarado segun
	// recurso
	var makeAtrCuenta = function(cuenta, fechaAnalisis) {
		var ret = {};

		ret.tieneDeudaAdm = Sql.value(SqlCuentaTieneDeuda, "gde_deudaadmin",cuenta.id) > 0 ? "SI" : "NO";
		ret.tieneDeudaJud = Sql.value(SqlCuentaTieneDeuda, "gde_deudaadmin",cuenta.id) > 0 ? "SI" : "NO";

		ret.ultFechaPago = Math.max(Sql.value(SqlCuentaUltFechaPago,"gde_deudaadmin", cuenta.id), Sql.value(SqlCuentaUltFechaPago,"gde_deudajudicial", cuenta.id));

		ret.montoTotDeudaHist = Math.sum(Sql.value(SqlCuentaMontoTotDeudaHist,"gde_deudaadmin", cuenta.id), Sql.value(SqlCuentaMontoTotDeudaHist, "gde_deudajudicial", cuenta.id));

		// ret.montoTotDeudaAct = montoTotDeudaAct(cuenta.id, fechaAnalisis);

		// calc promMensualEmiDec
		switch (cuenta.idrecurso) {
		case Const.RecursoIdDrei: // Drei
			ret.promMensualEmiDec = promMensualDecla(cuenta.id,	cuenta.idrecurso, fechaAnalisis);
			break;
		case Const.RecursoIdEtur: // Etur
			ret.promMensualEmiDec = promMensualDecla(cuenta.id, cuenta.idrecurso, fechaAnalisis);
			break;
		case Const.RecursoIdDerPub: // Derecho Publicitario
			ret.promMensualEmiDec = promMensualDerPub(cuenta.id, fechaAnalisis);
			break;
		default: // emision determinada
			ret.promMensualEmiDec = promMensualDeter(cuenta.id, fechaAnalisis);
		break;
		}

		return ret;
	}

	// montoTotDeudaAct calcula el atributo montoTotDeudaAct.
	// Es la suma de los saldos actualizados de las deudas adm y jud
	// para 'idCuenta' a la 'fechaAnalisis'.
	var montoTotDeudaAct = function(idCuenta, fechaAnalisis) {
		/*
		 * var deudas = []; //lista de Deudas a actualizar y sumar var totact =
		 * 0; var ActulizarDeuda =
		 * Grs.import("ar.gov.rosario.siat.........Actulizar");
		 * 
		 * deudas = deudas.concat( Sql.list(SqlSaldosDeuda, "gde_deudaadmin",
		 * idCuenta), Sql.list(SqlSaldosDeuda, "gde_deudajudicial", idCuenta));
		 * 
		 * for(var i=0; i < deudas.length; i++) { totact +=
		 * ActulizaDeuda.actualizar(deuda.saldo, deuda.fechaVencimiento,
		 * fechaAnalisis).getImporte(); } return totact;
		 */
	}
	
	// promMensualDecla
	// Calcula el promedioMensualDeclarado de la deuda para una cuenta, recurso y fecha.
	// Tiene en cuenta 12 periodos o deuda hasta 6 años atrás.
	// Ambos datos son parámetros.
	var promMensualDecla = function(idCuenta, idRecurso, fechaAnalisis) {

		// Creo el objeto listRecClaDeu que actúa como mapa.
		var listRecClaDeu = Sql.list(SqlRecClaDeu, ListaAbrRecClaDeu.join(","), idRecurso);
		var mmRecClaDeu = new Object;

		for ( var i = 0; i < listRecClaDeu.length; i++) {
			var property = listRecClaDeu[i].abrcladeu;
			mmRecClaDeu[property] = listRecClaDeu[i].id;
		}

		// La deuda se toma a partir de la fecha de Analisis.
		var anioanalisis = fechaAnalisis.getFullYear();
		periodoDeuda = new AnioPer(fechaAnalisis.getMonth(), fechaAnalisis.getFullYear());

		var cantperidet = 0;
		var importeacum = 0;
		var promedio = 0;

		while (cantperidet <= CantPeriodosDeuda && ((anioanalisis - periodoDeuda.y) <= CantAniosDeuda)) {
			Grs.printf("\t-->> cantperidet <= CantPeriodosDeuda && ((anioanalisis - periodoDeuda.y) <= CantAniosDeuda)"+cantperidet + "<=" + CantPeriodosDeuda + "&&" + ((anioanalisis - periodoDeuda.y) + "<=" + CantAniosDeuda) + "\n");

			var importedet = 0;
			var cursorDeu = Sql.cursor(SqlDeuda, idCuenta, idRecurso, periodoDeuda.y, periodoDeuda.m);

			/*
			Grs.printf("\t-->> idCuenta: " + idCuenta + "\n");
			Grs.printf("\t-->> idRecurso: " + idRecurso + "\n");
			Grs.printf("\t-->> periodoDeuda.y: " + periodoDeuda.y + "\n");
			Grs.printf("\t-->> periodoDeuda.m: " + periodoDeuda.m + "\n");
			 */

			while (row = cursorDeu.read()) {
				Grs.printf("\t-->> Hay Registros de gde_deudaadmin en ese periodo\n");
				var idreccladeuSQL = row.idreccladeu;
				var iddeudaSQL = row.id;
				var importeSQL = row.importe;

				/*
				Grs.printf("\t-->> row.idreccladeu: " + row.idreccladeu + "\n");
				Grs.printf("\t-->> row.id: " + row.id + "\n");
				Grs.printf("\t-->> row.importe: " + row.importe + "\n");
				 */

				if (idreccladeuSQL == mmRecClaDeu.RS) {
					Grs.printf("\t-->> El idreccladeuSQL del registro corresponde a RS\n");
					importedet = importeSQL;
					cantperidet++;
				} else {
					Grs.printf("\t-->> El idreccladeuSQL del registro No corresponde a RS\n");
					if (idreccladeuSQL == mmRecClaDeu.Orig) {
						Grs.printf("\t-->> El dreccladeuSQL del registro corresponde a Orig\n");
						var valueMaxFechaPresentacion = Sql.value(SqlMaxFechaPresentacion, idRecurso, iddeudaSQL+"");
						if (valueMaxFechaPresentacion != 0 && valueMaxFechaPresentacion != null){
							Grs.printf("\t-->> El valueMaxFechaPresentacion no es nulo ni cero\n");
							var valueTotalDeclarado = Sql.value(SqlTotalDeclarado, valueMaxFechaPresentacion, idRecurso, iddeudaSQL+"");

							// Aqui se valida que haya un registro de decjur con
							// iddeuda = deuda.id de ese idreccladeu
							if (valueTotalDeclarado != 0 && valueTotalDeclarado != null) {
								Grs.printf("\t-->> El valueTotalDeclarado no es nulo ni cero\n");
								importedet = valueTotalDeclarado;
								cantperidet++;
							}
						}
					} else {
						Grs.printf("\t-->> El idreccladeuSQL del registro No corresponde a Orig\n");
						if (importeSQL > 0) {
							Grs.printf("\t-->> El importe es > 0\n");
							importedet = Sql.value(SqlImporte, idCuenta,idRecurso, periodoDeuda.y, periodoDeuda.m, mmRecClaDeu.Rectif + "," + mmRecClaDeu.Orig);
							cantperidet++;
						}
					}
				}
				if (idreccladeuSQL == mmRecClaDeu.AF || idreccladeuSQL == mmRecClaDeu.AFHist || idreccladeuSQL == mmRecClaDeu.AFAcc) {
					Grs .debug("\t-->> idreccladeuSQL == mmRecClaDeu.AF || idreccladeuSQL == mmRecClaDeu.AFHist || idreccladeuSQL == mmRecClaDeu.AFAcc\n");
					if (importedet == 0) {
						Grs.printf("\t-->> El importedet es cero\n");
						cantperidet++;
					}
					importedet += Sql.value(SqlImporte, idCuenta,idRecurso, periodoDeuda.y, periodoDeuda.m, mmRecClaDeu.AF + "," + mmRecClaDeu.AFHist + "," + mmRecClaDeu.AFAcc);
				}
			}
			importeacum += importedet;
			periodoDeuda.restarMes();
			cursorDeu.close;
		}

		if (cantperidet == null && cantperidet != 0) {
			Grs.printf("Calculo Promedio\n");
			promedio = importeacum / cantperidet;
			Grs.printf("\t-->> Calculo Promedio: " + promedio + "\n");
		}
		return promedio;
	}

	// promMensualDerPub
	// Calcula el promedioMensualDerechoPublicitario de la deuda para una cuenta, recurso y fecha.
	// Tiene en cuenta 12 periodos o deuda hasta 6 años atrás.
	// Ambos datos son parámetros.
	var promMensualDerPub = function(idCuenta, fechaAnalisis) {

		// La deuda se toma a partir de la fecha de Analisis.
		var anioanalisis = fechaAnalisis.getFullYear();
		periodoDeuda = new AnioPer(fechaAnalisis.getMonth(), fechaAnalisis.getFullYear());

		var cantperidet = 0;
		var importeacum = 0;
		var promedio = 0;

		while (cantperidet <= CantPeriodosDeuda && ((anioanalisis - periodoDeuda.y) <= CantAniosDeuda)) {
			Grs.printf("\t-->> cantperidet <= CantPeriodosDeuda && ((anioanalisis - periodoDeuda.y) <= CantAniosDeuda)"+cantperidet + "<=" + CantPeriodosDeuda + "&&" + ((anioanalisis - periodoDeuda.y) + "<=" + CantAniosDeuda) + "\n");

			var importedet = 0;

			if (Sql.value(SqlImporteSinIdRecClaDeu, idCuenta, periodoDeuda.y, periodoDeuda.m) > 0) {
				Grs.printf("\t-->> El importe es > 0\n");
				importedet = Sql.value(SqlImporteSinIdRecClaDeu, idCuenta, periodoDeuda.y, periodoDeuda.m);
				cantperidet++;
			}
			
			importeacum += importedet;
			periodoDeuda.restarMes();
			cursorDeu.close;
		}

		if (cantperidet == null && cantperidet != 0) {
			Grs.printf("Calculo Promedio\n");
			promedio = importeacum / cantperidet;
			Grs.printf("\t-->> Calculo Promedio: " + promedio + "\n");
		}
		return promedio;
	}

	// ?
	var promMensualDeter = function(idCuenta, fechaAnalisis) {
		
		Grs.printf("fechaAnalisis: "+fechaAnalisis+"\n");
		
		var fechaAnioAtras = new Date(fechaAnalisis.getFullYear()-1,fechaAnalisis.getMonth(), fechaAnalisis.getDate());

		Grs.printf("fechaAnalisis: "+fechaAnalisis+"\n");
		Grs.printf("fechaAnioAtras: "+fechaAnioAtras+"\n");
		
		var importeacum = 		Sql.value(SqlImporteAcum, idCuenta, fechaAnioAtras, fechaAnalisis);
		var tmpPeriDet = 		Sql.exec(SqlPeriDet, idCuenta, fechaAnioAtras, fechaAnalisis);
		var cantperidet = 		Sql.value(SqlCountPeriDet);
		var dropTmpPeriDet = 	Sql.exec(SqlDropTmpPeriDet);
		
		var promedio = 0;

		if (cantperidet == null && cantperidet != 0) {
			Grs.printf("Calculo Promedio\n");
			promedio = importeacum / cantperidet;
			Grs.printf("\t-->> Calculo Promedio: " + promedio + "\n");
		}
		
		return promedio;
	}

	// public functions //
	this.main = function() {
		var params = Adp.getParameters();
		if (params == null) {
			params = {};
			params.fechaAnalisis = new Date();
		}

		processCuentas(params.fechaAnalisis);
		Sql.close();
	}

	// AnioPer
	// Objeto utilizado para manejar las operaciones sobre años y periodos y controlar el While.
	// Si se ejecuta el restarMes cambia sus valores segun corresponda.
	function AnioPer(m, y) {
		this.m = m;
		this.y = y;
		this.restarMes = function(){
			if (this.m > 1) this.m--;
			else 
			{
				this.m = 12;
				this.y--;
			}
		}

	}

}// end ProcesoAtributo

//start
Siat.ProcesoAtributo.main();
