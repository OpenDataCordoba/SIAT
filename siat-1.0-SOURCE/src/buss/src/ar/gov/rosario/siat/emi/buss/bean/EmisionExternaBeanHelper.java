//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.buss.bean;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.def.buss.bean.RecCon;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.iface.model.RecConVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.emi.iface.model.EmisionExternaAdapter;
import ar.gov.rosario.siat.emi.iface.model.FilaEmisionExterna;
import ar.gov.rosario.siat.emi.iface.util.EmiError;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Datum;
import coop.tecso.demoda.iface.model.UserContext;

public class EmisionExternaBeanHelper {

	private static Logger log = Logger
			.getLogger(EmisionExternaBeanHelper.class);

	private EmisionExternaAdapter emisionExternaAdapter;
	private UserContext userContext;
	private int nroFila;

	/**
	 * Contiene el resultado del analisis de la linea actual. Al inicio es TRUE
	 * y al agregar un log de error pasa a false.
	 */
	// private boolean analisisLineaExito=true;

	/**
	 * Inicializa la clase y graba el archivo para analisis
	 * 
	 * @param userContext
	 * @param emisionExternaAdapter
	 * @throws Exception
	 */
	public EmisionExternaBeanHelper(UserContext userContext,
			EmisionExternaAdapter emisionExternaAdapter) throws Exception {
		this.emisionExternaAdapter = emisionExternaAdapter;
		this.userContext = userContext;
	}

	/**
	 * Realiza el analisis del archivo para encontrar potenciales errores.
	 * Agrega entradas en el archivo de log
	 * 
	 * @return El adapter con el resultado del analisis seteado (si fue exitoso
	 *         o no).
	 */
	public EmisionExternaAdapter parseFile() {

		String funcName = "parseFile";

		try {
			log.debug(funcName + " : enter");

			FileReader fr = new FileReader(emisionExternaAdapter.getPathTmp());
			BufferedReader br = new BufferedReader(fr);
			String s;
			nroFila = 0;

			Map<String, String> mapKeysInFile = new HashMap<String, String>();

			Map<String, RecConVO> mapConceptos = new HashMap<String, RecConVO>();
			Map<Long, RecursoVO> mapRecursos = new HashMap<Long, RecursoVO>();

			Recurso recurso;
			Cuenta cuenta;
			RecCon recCon;
			RecConVO recConVO;
			RecursoVO recursoVO;

			while ((s = br.readLine()) != null) {
				if (s != null && s.length() > 0 && s.charAt(0) != '#') { // las
																			// que
																			// empiezan
																			// con
																			// #
																			// se
																			// toman
																			// como
																			// comentarios
																			// .
																			// Las
																			// que
																			// estan
																			// vacias
																			// no
																			// se
																			// tienen
																			// en
																			// cuenta
					s += " "; // Se agrega un espacio al final para que funcione
								// el split como deseamos
					// String[] linea = s.split(FilaEmisionExterna.SEPARATOR);
					String[] linea = Datum.parse(s).getCols();
					FilaEmisionExterna fila = null;
					nroFila++;

					if (validarFormatoLinea(nroFila, linea)) {

						log.debug("validando reglas linea " + nroFila);

						fila = new FilaEmisionExterna(nroFila, linea[0],
								linea[1], NumberUtil.getInt(linea[2]),
								NumberUtil.getInt(linea[3]), NumberUtil
										.getDouble(linea[4]), linea[5]);

						/*
						 * NumberUtil.getDouble(linea[5]),
						 * NumberUtil.getDouble(linea[6]),
						 * NumberUtil.getDouble(linea[7]), linea[8]);
						 */

						/*
						 * Validaciones: Que el numero de cuenta sea valido y la
						 * misma se encuentre vigente. Que el codigo de recurso
						 * sea valido y el mismo se encuentre vigente. Que el
						 * anio y periodo sean datos de tipo numericos. - Que el
						 * anio y periodo a emitir no se halla emitido y sea
						 * consecutivo segun la periodicidad de emision del
						 * recurso. Que que cada codigo de concepto exista y sea
						 * valido. Que cada importe concepto sea numerico y
						 * mayor o igual a cero. Validar que para el archivo
						 * sean unicos numero de cuenta, codigo recurso, anio y
						 * periodo.
						 */
						recurso = Recurso.getByCodigo(fila.getCodRecurso());

						// Que el codigo de recurso sea valido y el mismo se
						// encuentre vigente.
						if (recurso == null) {
							emisionExternaAdapter.getErrors().append(
									"Error en linea " + nroFila
											+ " el codigo de recurso "
											+ fila.getCodRecurso()
											+ " es inexistente.\n");
							continue;
						} else if (!recurso.isVigente()) {
							emisionExternaAdapter.getErrors().append(
									"Error en linea " + nroFila
											+ " el recurso "
											+ fila.getCodRecurso()
											+ " no se encuentra vigente.\n");
							continue;
						} else {

							cuenta = Cuenta.getByIdRecursoYNumeroCuenta(recurso
									.getId(), fila.getNumeroCuenta());
							// Que el numero de cuenta sea valido y la misma se
							// encuentre vigente.
							if (cuenta == null) {
								emisionExternaAdapter.getErrors().append(
										"Error en linea " + nroFila
												+ " la cuenta numero "
												+ fila.getNumeroCuenta()
												+ " es inexistente.\n");
								continue;
							} else if (!cuenta.isVigente()) {
								emisionExternaAdapter
										.getErrors()
										.append(
												"Error en linea "
														+ nroFila
														+ " la cuenta numero "
														+ fila
																.getNumeroCuenta()
														+ " no se encuentra vigente.\n");
								continue;
							} else {

								/*
								 * / Que el anio y periodo a emitir no se halla
								 * emitido y sea consecutivo segun la
								 * periodicidad de emision del recurso. String
								 * ultAnioEmi =
								 * recurso.getUltPerEmi().substring(0, 4);
								 * String ultPerEmi =
								 * recurso.getUltPerEmi().substring(4, 6);
								 * 
								 * log.debug("ultAnioEmi " + ultAnioEmi);
								 * log.debug("ultPerEmi " + ultPerEmi); Integer
								 * ultAnio = new Integer(ultAnioEmi); Integer
								 * ultPeriodo = new Integer(ultPerEmi);
								 * 
								 * // Validacion de anio correcto if
								 * (fila.getAnio().intValue() <
								 * ultAnio.intValue() ||
								 * fila.getAnio().intValue() >
								 * ultAnio.intValue() + 1){
								 * emisionExternaAdapter
								 * .getErrors().append("Error en linea " +
								 * nroFila + " el anio es incorrecto."); }
								 * 
								 * Integer periodoSiguiente = ultPeriodo + 1;
								 * Integer tope = 12;
								 * 
								 * if(recurso.getPeriodoDeuda().getId().equals(
								 * PeriodoDeuda.MENSUAL)){ tope = 12; } else if
								 * (recurso.getPeriodoDeuda().getId().equals(
								 * PeriodoDeuda.BIMESTRAL)){ tope = 6; } else if
								 * (recurso.getPeriodoDeuda().getId().equals(
								 * PeriodoDeuda.TRIMESTRAL)){ tope = 4; }
								 * 
								 * if (ultPeriodo.intValue() ==
								 * tope.intValue()){ periodoSiguiente = 1; }
								 * 
								 * log.debug(funcName + " peridoSiguiente: " +
								 * periodoSiguiente);
								 * 
								 * // Validacion de periodo correcto if
								 * (fila.getPeriodo().intValue() !=
								 * periodoSiguiente.intValue()){
								 * emisionExternaAdapter
								 * .getErrors().append("Error en linea " +
								 * nroFila + " el periodo es incorrecto."); }
								 */

								// Que cada importe concepto sea numerio y mayor
								// o igual a cero.
								if (fila.getImporteConcepto1().doubleValue() < 0) {
									emisionExternaAdapter
											.getErrors()
											.append(
													"Error en linea "
															+ nroFila
															+ " el ImporteConcepto1 es negativo.\n");
								} else {
									recCon = recurso.getRecConByOrden(1);

									// Que que cada codigo de concepto exista y
									// sea valido.
									if (recCon == null) {
										emisionExternaAdapter
												.getErrors()
												.append(
														"Error en linea "
																+ nroFila
																+ " el Concepto 1 es inexistente para el recurso "
																+ recurso
																		.getCodRecurso()
																+ " .\n");
										continue;
									}

									// Totalizamos por concepto
									if (mapConceptos.containsKey(recCon
											.getCodRecCon())) {
										recConVO = mapConceptos.get(recCon
												.getCodRecCon());
									} else {
										recConVO = (RecConVO) recCon.toVO(1,
												false);
									}

									recConVO.setTotal(recConVO.getTotal()
											+ fila.getImporteConcepto1());
									mapConceptos.put(recCon.getCodRecCon(),
											recConVO);
								}

								/*
								 * / Concepto e Importe 2 if
								 * (fila.getImporteConcepto2() != null){
								 * 
								 * recCon = recurso.getRecConByOrden(2);
								 * 
								 * // Que que cada codigo de concepto exista y
								 * sea valido. if (recCon == null){
								 * emisionExternaAdapter
								 * .getErrors().append("Error en linea " +
								 * nroFila +
								 * " el Concepto 2 es inexistente para el recurso "
								 * + recurso.getCodRecurso() + " .\n");
								 * continue; } // Que cada importe concepto sea
								 * numerio y mayor o igual a cero. if
								 * (fila.getImporteConcepto2().doubleValue()<0){
								 * emisionExternaAdapter
								 * .getErrors().append("Error en linea " +
								 * nroFila +
								 * " el ImporteConcepto2 es negativo.\n"); }
								 * 
								 * // Totalizamos por concepto if
								 * (mapConceptos.containsKey
								 * (recCon.getCodRecCon())){ recConVO =
								 * mapConceptos.get(recCon.getCodRecCon()); }
								 * else { recConVO =
								 * (RecConVO)recCon.toVO(1,false); }
								 * recConVO.setTotal(recConVO.getTotal() +
								 * fila.getImporteConcepto2());
								 * mapConceptos.put(recCon.getCodRecCon(),
								 * recConVO); }
								 * 
								 * // Importe Concepto 3 if
								 * (fila.getImporteConcepto3() != null){ recCon
								 * = recurso.getRecConByOrden(3);
								 * 
								 * // Que que cada codigo de concepto exista y
								 * sea valido. if (recCon == null){
								 * emisionExternaAdapter
								 * .getErrors().append("Error en linea " +
								 * nroFila +
								 * " el Concepto 3 es inexistente para el recurso "
								 * + recurso.getCodRecurso() + " .\n");
								 * continue; } // Que cada importe concepto sea
								 * numerio y mayor o igual a cero. if
								 * (fila.getImporteConcepto3().doubleValue()<0){
								 * emisionExternaAdapter
								 * .getErrors().append("Error en linea " +
								 * nroFila +
								 * " el ImporteConcepto3 es negativo.\n"); }
								 * 
								 * // Totalizamos por concepto if
								 * (mapConceptos.containsKey
								 * (recCon.getCodRecCon())){ recConVO =
								 * mapConceptos.get(recCon.getCodRecCon()); }
								 * else { recConVO =
								 * (RecConVO)recCon.toVO(1,false); }
								 * 
								 * recConVO.setTotal(recConVO.getTotal() +
								 * fila.getImporteConcepto3());
								 * mapConceptos.put(recCon.getCodRecCon(),
								 * recConVO); }
								 * 
								 * // Concepto e Importe 4 if
								 * (fila.getImporteConcepto4() != null){ recCon
								 * = recurso.getRecConByOrden(3); // Que que
								 * cada codigo de concepto exista y sea valido.
								 * if (recCon == null){
								 * emisionExternaAdapter.getErrors
								 * ().append("Error en linea " + nroFila +
								 * " el Concepto 4 es inexistente para el recurso "
								 * + recurso.getCodRecurso() + " .\n");
								 * continue; } // Que cada importe concepto sea
								 * numerio y mayor o igual a cero. if
								 * (fila.getImporteConcepto4().doubleValue()<0){
								 * emisionExternaAdapter
								 * .getErrors().append("Error en linea " +
								 * nroFila +
								 * " el ImporteConcepto4 es negativo.\n"); }
								 * 
								 * // Totalizamos por concepto if
								 * (mapConceptos.containsKey
								 * (recCon.getCodRecCon())){ recConVO =
								 * mapConceptos.get(recCon.getCodRecCon()); }
								 * else { recConVO =
								 * (RecConVO)recCon.toVO(1,false); }
								 * 
								 * recConVO.setTotal(recConVO.getTotal() +
								 * fila.getImporteConcepto4());
								 * mapConceptos.put(recCon.getCodRecCon(),
								 * recConVO); }
								 */

								// Que para el archivo sean unicos numero de
								// cuenta, codigo recurso, anio y periodo.
								String key = fila.getNumeroCuenta()
										+ fila.getCodRecurso() + fila.getAnio()
										+ fila.getPeriodo();

								if (mapKeysInFile.containsKey(key)) {
									emisionExternaAdapter.getErrors().append(
											"Error en linea " + nroFila
													+ " Cuenta "
													+ fila.getNumeroCuenta()
													+ " CorRecurso "
													+ fila.getCodRecurso()
													+ " Anio " + fila.getAnio()
													+ " Periodo "
													+ fila.getPeriodo()
													+ " duplicados.\n");
								} else {
									mapKeysInFile.put(key, key);
								}

							} // cuenta ok

						} // recurso ok

						// mientras no exista error agregamos la fila para luego
						// generar auxdeuda
						if (emisionExternaAdapter.getErrors().length() == 0) {
							// if (!emisionExternaAdapter.hasError()){
							emisionExternaAdapter.getListFilas().add(fila);
						}

					} // Fin formato valido
				}

			}// fin while

			if (emisionExternaAdapter.getErrors().length() == 0) {
				// if (!emisionExternaAdapter.hasError()){
				emisionExternaAdapter.setCantLineas(nroFila - 1);

				for (String codRecCon : mapConceptos.keySet()) {
					recConVO = mapConceptos.get(codRecCon);

					log.debug("Rec: " + recConVO.getRecurso().getDesRecurso()
							+ " - " + codRecCon + " TOTAL: "
							+ recConVO.getTotalView());

					if (mapRecursos.containsKey(mapConceptos.get(codRecCon)
							.getRecurso().getId())) {
						recursoVO = mapRecursos.get(mapConceptos.get(codRecCon)
								.getRecurso().getId());
					} else {
						recursoVO = mapConceptos.get(codRecCon).getRecurso();
					}

					recursoVO.getListRecCon().add(recConVO);

					mapRecursos.put(recursoVO.getId(), recursoVO);
				}

				for (Long idRecurso : mapRecursos.keySet()) {
					emisionExternaAdapter.getListRecurso().add(
							mapRecursos.get(idRecurso));
				}
			} else {
				emisionExternaAdapter
						.addRecoverableValueError("Se encontraron errores durante la validacion.");
			}

		} catch (FileNotFoundException e) {
			log.error("No se encontro el archivo a cargar");
			emisionExternaAdapter
					.addRecoverableError(EmiError.UPLOAD_EVENTO_MSG_FILE_NOTFOUND);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			emisionExternaAdapter
					.addRecoverableValueError("Ocurrio un error durante la validacion del archivo");
		}

		log.debug("parseFile : exit");

		return emisionExternaAdapter;
	}

	/**
	 * Valida si el formato de la linea es correcto. Agrega los logs
	 * correspondientes a medida que va encontrando errores
	 * 
	 * @param nroFila
	 * @param linea
	 * @return
	 * @throws Exception
	 */
	private boolean validarFormatoLinea(int nroFila, String[] linea)
			throws Exception {
		boolean valida = true;
		log.debug("validando formato linea " + nroFila + "    largo:"
				+ linea.length);
		if (linea != null && linea.length >= 6) {

			/*
			 * String 0 numeroCuenta, String 1 codRecurso, Integer
			 * 2 anio, Integer 3 periodo, Double 4 importeConcepto1, String 5
			 * leyenda
			 */

			/*
			 * MODIFICACION: se socilita solo un importe
			 * 
			 * Double 5 importeConcepto2, Double 6 importeConcepto3, Double 7
			 * importeConcepto4,
			 */

			String numeroCuenta = linea[0];
			String codRecurso = linea[1];
			String anio = linea[2];
			String periodo = linea[3];
			String importeConcepto1 = linea[4];
			// String importeConcepto2 = linea[5];
			// String importeConcepto3 = linea[6];
			// String importeConcepto4 = linea[7];

			// Numero Cuenta
			if (StringUtil.isNullOrEmpty(numeroCuenta)) {
				emisionExternaAdapter.getErrors().append(
						"En Linea: " + nroFila + " Numero cuenta es vacio.\n");
				valida = false;
			}
			// Cod. Recurso
			if (StringUtil.isNullOrEmpty(codRecurso)) {
				emisionExternaAdapter.getErrors().append(
						"En Linea: " + nroFila
								+ " Codigo de Recurso es vacio.\n");
				valida = false;
			}
			// Anio
			if (StringUtil.isNullOrEmpty(anio)) {
				emisionExternaAdapter.getErrors().append(
						"En Linea: " + nroFila + " Anio es vacio.\n");
				valida = false;
			} else if (NumberUtil.getInt(anio) == null) {
				emisionExternaAdapter.getErrors().append(
						"En Linea: " + nroFila
								+ " El formato de Anio es incorrecto.\n");
				valida = false;
			}
			// Periodo
			if (StringUtil.isNullOrEmpty(periodo)) {
				emisionExternaAdapter.getErrors().append(
						"En Linea: " + nroFila + " Periodo es vacio.\n");
				valida = false;
			} else if (NumberUtil.getInt(periodo) == null) {
				emisionExternaAdapter.getErrors().append(
						"En Linea: " + nroFila
								+ " El formato de Periodo es incorrecto.\n");
				valida = false;
			}
			// ImporteConcepto1 - Al menos este es requerido
			if (StringUtil.isNullOrEmpty(importeConcepto1)) {
				emisionExternaAdapter.getErrors().append(
						"En Linea: " + nroFila
								+ " ImporteConcepto1 es vacio.\n");
				valida = false;
			} else if (NumberUtil.getDouble(importeConcepto1) == null) {
				emisionExternaAdapter
						.getErrors()
						.append(
								"En Linea: "
										+ nroFila
										+ " El formato de ImporteConcepto1 es incorrecto.\n");
				valida = false;
			}

			/*
			 * / ImporteConcepto2 if
			 * (!StringUtil.isNullOrEmpty(importeConcepto2) &&
			 * NumberUtil.getDouble(importeConcepto2)==null){
			 * emisionExternaAdapter.getErrors().append("En Linea: " + nroFila +
			 * " El formato de ImporteConcepto2 es incorrecto.\n");
			 * valida=false; } // ImporteConcepto3 if
			 * (!StringUtil.isNullOrEmpty(importeConcepto3) &&
			 * NumberUtil.getDouble(importeConcepto3)==null){
			 * emisionExternaAdapter.getErrors().append("En Linea: " + nroFila +
			 * " El formato de ImporteConcepto3 es incorrecto.\n");
			 * valida=false; } // ImporteConcepto4 if
			 * (!StringUtil.isNullOrEmpty(importeConcepto4) &&
			 * NumberUtil.getDouble(importeConcepto4)==null){
			 * emisionExternaAdapter.getErrors().append("En Linea: " + nroFila +
			 * " El formato de ImporteConcepto4 es incorrecto.\n");
			 * valida=false; }
			 */

		} else {
			emisionExternaAdapter
					.getErrors()
					.append(
							"En Linea: "
									+ nroFila
									+ " Formato de Línea incorrecto: Cantidad de parametros incorrecto.\n");
			valida = false;
		}
		log.debug("validarFormatoLinea - exit");
		return valida;
	}

}
