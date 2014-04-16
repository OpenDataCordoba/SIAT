//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.emision;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.Sistema;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.SiatUtil;
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.def.buss.bean.TipObjImp;
import ar.gov.rosario.siat.def.buss.bean.TipObjImpAtr;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.def.buss.bean.Zona;
import ar.gov.rosario.siat.emi.buss.bean.AuxDeuda;
import ar.gov.rosario.siat.emi.buss.bean.Emision;
import ar.gov.rosario.siat.emi.buss.dao.EmiDAOFactory;
import ar.gov.rosario.siat.emi.iface.util.EmiError;
import ar.gov.rosario.siat.exe.buss.bean.CueExe;
import ar.gov.rosario.siat.exe.buss.bean.CueExeCache;
import ar.gov.rosario.siat.exe.buss.bean.Exencion;
import ar.gov.rosario.siat.exe.buss.bean.TipoSujeto;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.buss.bean.EstadoDeuda;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.pad.buss.bean.AsignaRepartidor;
import ar.gov.rosario.siat.pad.buss.bean.Broche;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.ObjImp;
import ar.gov.rosario.siat.pad.buss.dao.CuentaDAO;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpDefinition;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.adpcore.AdpRunState;
import coop.tecso.adpcore.AdpWorker;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Datum;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Procesa la Emision de Tasa General de Inmuebles
 * con ALFAX
 * 
 * @author Tecso Coop. Ltda.
 */
public class EmisionTgiAlfaxWorker implements AdpWorker {

	private static Logger log = Logger.getLogger(EmisionTgiAlfaxWorker.class);

	// Cache de Cue Exe
	private CueExeCache cueExeCache;
	// Mapa entre Cuenta y su Tipo de Adhesion
	private Map<String,String> cuentaTipoAdhesion;
	
	// Mapas para el segun paso de incorporacion de calculos en aux deuda
	private Map<String,String> mapCuentaAtrAseVal;
	
	private Map<String,String> mapCuentaConceptos;
	
	public void reset(AdpRun adpRun) throws Exception {
	}

	public void cancel(AdpRun adpRun) throws Exception {
	}

	public void execute(AdpRun adpRun) throws Exception {
		// verfica numero paso y estado en adprun,
		// llama a cada metodo segun el numero de paso
		Long pasoActual = adpRun.getPasoActual();
		String idEmision = adpRun.getParameter(Emision.ADP_PARAM_ID); 
		String idZona = adpRun.getParameter(Zona.ID_ZONA); //valor de zona segun el dominio Zona
		
		if (pasoActual.equals(1L)) { // Paso 1 de la Emision Tgi: Generar archivo ALFAX

			this.ejecutarPaso1(adpRun, NumberUtil.getLong(idEmision), NumberUtil.getLong(idZona));
		}
		if (pasoActual.equals(2L)) { // Paso 2 de la Emision: creacion de los registros auxDeu

			this.ejecutarPaso2(adpRun, NumberUtil.getLong(idEmision), NumberUtil.getLong(idZona));
		}
		if (pasoActual.equals(3L)) { // Paso 3 de la Emision: incorporacion de deuda a deudaAdmin con sus DeuAdmRecCon

			this.ejecutarPaso3(adpRun, NumberUtil.getLong(idEmision));
		}
	}

	/**
	 * Paso 1 del Proceso de Emision TGI. Generar archivo allfax.
	 * 
	 * @param adpRun
	 * @param idEmision
	 * @param idObra
	 * @param fechaVencimiento
	 * @throws Exception
	 */
	public void ejecutarPaso1(AdpRun adpRun, Long idEmision, Long idZona) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			Emision emision = Emision.getById(idEmision);
			
			String archivoAlfax = null;
			String nombreFileCorrida = "ALFAX emision TGI";
			String descripcionFileCorrida = "Archivo ALFAX de la emisi�n masiva de TGI ";

			// Generacion del archivo ALFAX, en caso de error lo carga en la emision
			String outputDir = adpRun.getProcessDir(AdpRunDirEnum.SALIDA);
			archivoAlfax = generarAlfax(outputDir, emision, idZona);
		
			if (emision.hasError()) {
				String descripcion = emision.getListError().get(0).key().substring(1);
				adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false);
				adpRun.logError(descripcion);
			} else {
				// Carga el FileCorrida a la Corrida de la emision con el archivo ALFAX generado
				emision.getCorrida().addOutputFile(nombreFileCorrida, descripcionFileCorrida, archivoAlfax);
				
				// Copia del ALFAX generado al directorio "ultimo" 
				AdpRun.changeRunMessage("Copiando a directorio 'ultimo'", 0);
				String dirSalida = AdpRun.currentRun().getProcessDir(AdpRunDirEnum.SALIDA);
				File dirUltimo = new File(dirSalida, "ultimo");
				dirUltimo.mkdir();

				File src = new File(archivoAlfax);
				File dst = new File(dirSalida + "/ultimo", "alfax-zona" + idZona);
				AdpRun.copyFile(src, dst);
			
				// Cambiar estado, incremetar paso, y actualiza el pasoCorrida 
				adpRun.changeState(AdpRunState.ESPERA_CONTINUAR, "Se genero el archivo ALFAX", true);
			}
					
			log.debug(funcName + ": exit");

		} catch (Exception e) {
			log.error("Service Error: ",  e);
			adpRun.changeState(AdpRunState.FIN_ERROR, e.getMessage() ,false ,e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	//Metodos privados exclusivos de este worker.
	//String formatLong(Long, size) -> completa con ceros a la izq, si es null retorna ceros sizes
	public String formatLong(Long nro, int size){
		
		String resultado = "";
		
		if(nro != null){
			resultado = StringUtil.formatLong(nro);
		}
		if(resultado.length() > size){
			// disparar excepcion
		}
		
		resultado = StringUtil.completarCaracteresIzq(resultado, size, '0');
		
		return resultado;
	}
	
	public String formatLong(String nro, int size){
		
		String resultado = "";
		
		if(!StringUtil.isNullOrEmpty(nro)){
			resultado = StringUtil.cut(nro);
		}
		if(resultado.length() > size){
			
			// disparar excepcion
		}
		
		resultado = StringUtil.completarCaracteresIzq(resultado, size, '0');
		
		return resultado;
		
	}

	//String formatString(Long, size) -> completa con espacios a la der, si es null retorna size, espacios.
	public String formatString(Long valor, int size){		
		if(valor == null) {
			return StringUtil.getStringEspaciosBanco(size);
		}
		
		return formatString(valor.toString(), size);		
	}
	
	//String formatString(String, size) -> completa con espacios a la der, si es null retorna size, espacios.
	public String formatString(String valor, int size){
		
		if(valor == null) {
			return StringUtil.getStringEspaciosBanco(size);
		}
		
		if(valor.length() > size){
			// disparar excepcion
		}
		
		return StringUtil.completarCaracteresDer(valor, size, ' ');
		
	}
	
	//String formatDouble(Double, size, decimal) -> completa con ceros a la der y izq, si es null retorna size de ceros con comas adecuadas: pej (12.2, 5, 2) -> '012.20' 
	//                                              si decimal es 0, trunca el Double y se trata como formatLong()
	public String formatDouble(Double valor, int size, int decimal){
		if(valor == null){
			valor = 0D;
		}
		String strFormated = "";
		// construccion de la mascara de acuerdo al size y al decimal
		StringBuffer mascara = new StringBuffer();
		
		for(int i= 0 ; i < size ; i++) {
			if (i == size -decimal){
				mascara.append(".");
			}
			mascara.append("0");
		}
		
		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
		simbolos.setDecimalSeparator('.');
		DecimalFormat formatter = new DecimalFormat(mascara.toString(), simbolos);		
		strFormated = formatter.format(valor);
		// completa con ceros a la der y izq, si es null retorna size de ceros con comas adecuadas: pej (12.2, 5, 2) -> '012.20'
		// si decimal es 0, trunca el Double y se trata como formatLong()		
		return strFormated;
	}
	
	//String formatDateYearToSecond(Date, size) ejemplo: '2008-06-04 07:29:48 '
	public String formatDateYearToSecond(Date valor, int size){
		
		String resultado = "";
		
		if(valor != null) {
			resultado = DateUtil.formatDate(valor, DateUtil.yyyy_MM_dd_HH_MM_SS_MASK);
		}
		
		if(resultado.length() > size){
			// disparar excepcion
		}
		
		return StringUtil.completarCaracteresDer(resultado, size, ' '); 
	}
	
	public String formatDateYearToDate(Date valor, int size){

		String resultado = "";
		if(valor != null){
			resultado = DateUtil.formatDate(valor, DateUtil.ddMMYYYY_MASK);
		}
		
		return StringUtil.completarCerosIzq(resultado, size);
	}
	
	public String formatDateYearToMonth(Date valor, int size){
		return formatDateYearToMonth(valor, size, "");
	}

	public String formatDateYearToMonth(Date valor, int size, String valorDefecto){
		
		String resultado = "";
		if(valor != null){
			resultado = DateUtil.formatDate(valor, DateUtil.YYYYMM_MASK);
		} else {
			resultado = valorDefecto;
		}
		
		return StringUtil.completarCerosIzq(resultado, size);
	}

	public String formatCatastral(String catastral, int size){
		
		if(catastral == null) {
			return StringUtil.getStringEspaciosBanco(size);
		}
		
		catastral = StringUtil.replace(catastral, "/", "");
		
		if(catastral.length() > size){
			// disparar excepcion
		}
		
		return StringUtil.completarCaracteresDer(catastral, size, ' ');
	}

	
	/**
	 * Paso 2 del Proceso de Emision TGI. 
	 * Procesar calcdefi y det_calcdefi y lo sube a la tabla auxDeuda
	 * 
	 * @param adpRun
	 * @param idEmision
	 * @throws Exception
	 */
	public void ejecutarPaso2(AdpRun adpRun, Long idEmision, Long idZona) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			SiatHibernateUtil.currentSession().beginTransaction();
			
			Emision emision = Emision.getById(idEmision);
			Zona zona = Zona.getById(idZona);

			// si no hubo errores
			if (!emision.hasError()) {
				//lo que halla que hacer.
			}
			
			// borra los auxDeu existentes para la emision
			// creacion de los auxDeu y carga de los conceptos 
			this.ejecutarPaso2(adpRun, emision, zona);
			
			// No se genera archivo auxDeuda
			
			if (emision.hasError()) {
				SiatHibernateUtil.currentSession().getTransaction().rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				String descripcion = emision.getListError().get(0).key().substring(1);
				adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false);
				adpRun.logError(descripcion);
			} else {
				SiatHibernateUtil.currentSession().getTransaction().commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				// cambiar estado, incremetar paso, y actualiza el pasoCorrida (que fue creado cuando se activo(esqueduleo)) 
				adpRun.changeState(AdpRunState.ESPERA_CONTINUAR, "Registros de AuxDeuda generados exitosamente", true); 
			}

			log.debug(funcName + ": exit");

		} catch (Exception e) {
			log.error("Service Error: ",  e);
			Transaction tx = SiatHibernateUtil.currentSession().getTransaction();
		    try { if(tx != null) tx.rollback(); } catch (Exception ex) {}
			adpRun.changeState(AdpRunState.FIN_ERROR, e.getMessage() ,false ,e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	public void ejecutarPaso3(AdpRun adpRun, Long idEmision) throws Exception {

		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			SiatHibernateUtil.currentSession().beginTransaction();
			Emision emision = Emision.getById(idEmision);
			
			//emision = this.validatePaso3(emision);
			// si no hubo errores
			//if (!emision.hasError()) {
				//lo que halla que hacer.
			//}

			this.generarDeudaAdmin(adpRun, emision);
			
			if(emision.hasError()){
 				// borrado de los registros DeuAdmRecCon	
 				int deuAdmRecConBorrados = GdeDAOFactory.getDeuAdmRecConDAO().deleteListDeuAdmRecConByEmision(emision);
 				log.debug("registros borrados de DeuAdmRecCon: " + deuAdmRecConBorrados);
 				// borrado DeudaAdmin creados
 				int regBorrados = GdeDAOFactory.getDeudaAdminDAO().deleteListDeudaAdminByEmision(emision);
 				log.debug("registros borrados de la DeudaAdmin: " + regBorrados);

 				String descripcion = "Error al generar los registros de Deuda Administrativa. Se limpiaron todos los registros de Deuda y Conceptos de esta Emision ";
				adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false);
				adpRun.logError(descripcion);
	 		}else{
	 			// borrado de los registros auxdeu de la emision
	 			int regBorrados = EmiDAOFactory.getAuxDeudaDAO().deleteAuxDeudaByIdEmision(emision);
	 			log.debug("registros borrados de la AuxDeuda: " + regBorrados);
				// cambiar estado, incremetar paso, y actualiza el pasoCorrida (que fue creado cuando se activo (esqueduleo)) 
				adpRun.changeState(AdpRunState.FIN_OK, "Registros de Deuda Administrativas generados exitosamente", true); 
	 		}
	 		
	 		// Si o si realiza el commit.
			SiatHibernateUtil.currentSession().getTransaction().commit();

			log.debug(funcName + ": exit");

		} catch (Exception e) {
			log.error("Service Error: ",  e);
			Transaction tx = SiatHibernateUtil.currentSession().getTransaction();
		    try { if(tx != null) tx.rollback(); } catch (Exception ex) {}
			adpRun.changeState(AdpRunState.FIN_ERROR, e.getMessage() ,false ,e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	

	/**
 	 * Crea un registro de deuda en una 
	 * estructura intermedia para posterior validacion
	 * @param cuenta
	 * @param emision
	 * @param fechaVencimiento
	 * @param importeBruto
	 * @param importe
	 * @param periodo
	 * @param anio
	 * @param asignadorTgi
	 * @return AusDeuda
	 * @throws Exception
	 */
	private AuxDeuda createAuxDeuda(Long idCuenta, Emision emision, Date fechaVencimiento, 
			Double importeBruto, Double importe, Long periodo, Long anio, String atrAseVal, 
			Double conc1, Double conc2, Double conc3, Double conc4) throws Exception {
		AuxDeuda auxDeuda = new AuxDeuda();

		//Long codRefPag =  GdeDAOFactory.getDeudaDAO().getNextCodRefPago(); 
		auxDeuda.setCodRefPag(0L);
		Cuenta cuenta = Cuenta.getById(idCuenta);
		auxDeuda.setCuenta(cuenta);
		auxDeuda.setRecClaDeu(RecClaDeu.getById(RecClaDeu.ID_ORIGINAL));
		auxDeuda.setRecurso(emision.getRecurso());
		auxDeuda.setViaDeuda(ViaDeuda.getById(ViaDeuda.ID_VIA_ADMIN));
		auxDeuda.setEstadoDeuda(EstadoDeuda.getById(EstadoDeuda.ID_ADMINISTRATIVA));
		ServicioBanco servicioBancoTGI = ServicioBanco.getById(ServicioBanco.ID_SERVICIO_BANCO_TGI); 
		auxDeuda.setServicioBanco(servicioBancoTGI);
		auxDeuda.setPeriodo(periodo);
		auxDeuda.setAnio(anio);
		auxDeuda.setFechaEmision(new Date());
		auxDeuda.setFechaVencimiento(fechaVencimiento);
		auxDeuda.setImporteBruto(NumberUtil.truncate(importeBruto,SiatParam.DEC_IMPORTE_DB));
		auxDeuda.setImporte(NumberUtil.truncate(importe,SiatParam.DEC_IMPORTE_DB));
		auxDeuda.setSaldo(NumberUtil.truncate(importe, SiatParam.DEC_IMPORTE_DB));
		auxDeuda.setActualizacion(0D);
		auxDeuda.setSistema(Sistema.getById(2L));
		auxDeuda.setResto(0L);
		auxDeuda.setObraFormaPago(null);
		auxDeuda.setEmision(emision);
		auxDeuda.setAtrAseVal(atrAseVal);
		auxDeuda.setConc1(conc1);
		auxDeuda.setConc2(conc2);
		auxDeuda.setConc3(conc3);
		auxDeuda.setConc4(conc4);
		
		// persisto
		EmiDAOFactory.getAuxDeudaDAO().update(auxDeuda);

		return auxDeuda;
	}
	 

	public boolean validate(AdpRun adpRun) throws Exception {
		// TODO Ver Validaciones necesarias
		return false;
	}

	private String generarAlfax(String outputDir, Emision emision, Long idZona) throws Exception{

		Cuenta cuenta = null;

		try {
			Long idTipObjImp = TipObjImp.PARCELA;
			Long idTipObjImpAtr = TipObjImpAtr.ID_ZONA_TIPO_PARCELA;
			Long indiceSkip = 0L;
			Long salto = 2500L;  
			long c = 0;
			
			String fileName = "alfax-" + DateUtil.formatDate(emision.getFechaEmision(), DateUtil.YYYYMMDD_MASK) + "-idEmision" + emision.getId() + "-zona" + idZona;
			String pathCompleto = outputDir + File.separator + fileName;
			
			// buffer de escritura para el archivo Alfax
			BufferedWriter buffer = new BufferedWriter(new FileWriter(pathCompleto, false));

			// iterar la lista de Cuentas de manera paginada, mientras no tengamos errores
			boolean contieneCuentas = true;
			boolean sinError = true;
			
			// decidimos sacar las cuentas excluidas
			
			// mapa previamente cargado con clave como long nro de cuenta y valor tipo de adhesion
			//Map<Long,String> cuentaTipoAdhesion = PadDAOFactory.getDebitoAutJDBCDAO().getMapaTipoAdhe();
			this.cuentaTipoAdhesion = PadDAOFactory.getDebitoAutJDBCDAO().getMapaTipoAdhe();
			CuentaDAO cuentaDAO = PadDAOFactory.getCuentaDAO();
			
			// obtencion del asignadorTgi para el recurso y la fecha de emision
			AsignaRepartidor asignadorTgi = new AsignaRepartidor(emision.getRecurso().getId(), emision.getFechaEmision());
			
			// Inicializamos el cache
			Recurso recurso = emision.getRecurso();
			Date fechaEmision = emision.getFechaEmision();
			cueExeCache = new CueExeCache();
			cueExeCache.initialize(recurso, fechaEmision);
			
			while (contieneCuentas && sinError) {
 
				// obtiene la lista de Resultado de cuentas
				log.debug("Leyendo cuentas zona " + idZona);
				List <Object[]> listResultadoCuenta = cuentaDAO.
					getListCuentaActivaByIdsStrValor(idTipObjImp, idTipObjImpAtr, idZona.toString(), indiceSkip, salto, fechaEmision);

				contieneCuentas = (listResultadoCuenta.size() > 0);				
				if(contieneCuentas){
					TipObjImpDefinition toid = TipObjImp.getByCodigo("PARCELA").getDefinitionForManual(); 					
					for (Object[] resultado : listResultadoCuenta) {

						cuenta = (Cuenta) resultado[0];
						
						log.debug("Generando renglon para idCuenta " + cuenta.getId() + " numeroCuenta:" + cuenta.getNumeroCuenta());
						
						ObjImp objImp = (ObjImp) resultado[1];
						
						objImp.loadDefinition(toid, emision.getFechaEmision()); 
						//TipObjImpDefinition toid = objImp.getDefinitionValue(emision.getFechaEmision());
						
						//List<CueExe> listCueExe = cuenta.getListCueExeVigente(emision.getFechaEmision()); 
						List<CueExe> listCueExe = cueExeCache.getListCueExe(cuenta.getId());
						
						// OBTENCION DE CADA DATO:
						// cuenta			
						String nroCuenta = cuenta.getNumeroCuenta();
						// sup-edif  
						Double supEdi = (Double) toid.getValorObjectTipObjImpAtrDefinitionByCodigo("SupEdiTerreno");
						// broche
						Broche broche = cuenta.getBroche();
						Long idBroche = ((broche == null) ? 0L : broche.getId());
						// domicilio
						String domicilio = cuenta.getDesDomEnv() != null ? cuenta.getDesDomEnv() : "";
						// ubic-finca
						String ubicFinca = (String) toid.getValorObjectTipObjImpAtrDefinitionByCodigo("DomicilioFinca");
						// ubic-terreno
						String ubicTerreno = (String) toid.getValorObjectTipObjImpAtrDefinitionByCodigo("UbiTerreno");
						// valTerreno
						Double valTerreno = (Double) toid.getValorObjectTipObjImpAtrDefinitionByCodigo("ValTerreno");
						// valEdif
						Double valEdif = (Double) toid.getValorObjectTipObjImpAtrDefinitionByCodigo("ValEdificada");
						// valLibre
						Double valLibre = (Double) toid.getValorObjectTipObjImpAtrDefinitionByCodigo("ValLibre");
						// sup
						Double sup = (Double) toid.getValorObjectTipObjImpAtrDefinitionByCodigo("SupTerreno");
						// rt
						Long rt = null; // se carga cuando se procesan las exenciones de la cuenta
						// radio
						Long radio = (Long) toid.getValorObjectTipObjImpAtrDefinitionByCodigo("Radio");
						// tipo-jub
						Long tipoJub = null; // se carga cuando se procesan las exenciones de la cuenta 
						// porc-jub: no tiene parte decimal
						Long porcJub = null; // se carga cuando se procesan las exenciones de la cuenta
						// uso
						Long uso = 0L; // se carga cuando se procesan las exenciones de la cuenta
						// vig-quita-desde
						Date vigQuitaDesde = null;  // se carga cuando se procesan las exenciones de la cuenta
						// vig-quita-hasta
						Date vigQuitaHasta = null;  // se carga cuando se procesan las exenciones de la cuenta
						// tipo-mov (no se informa, se manda null)
						Long tipoMov = null;
						// tipo-parcela
						Long tipoParcela = (Long) toid.getValorObjectTipObjImpAtrDefinitionByCodigo("TipoParcela");
						// nro-repartidor
						// nombre
						String nombre = cuenta.getNombreTitularPrincipal();
						// catastral
						String catastral = (String) toid.getValorObjectTipObjImpAtrDefinitionByCodigo("Catastral");
						Long nroBroche = 0L;
						if (broche == null){
							// NOTA: Utilizamos el idBroche, para ser coeherentes con el sistema de broches actual.
							//       Tal vez en posteriores implementaciones convenga utilizar el repartidor: buscarNroRepartidorPorCatastral()
							nroBroche = asignadorTgi.buscarIdBrochePorCatastral(catastral);
							if (nroBroche == null){
								// 		si no encontro repartidor seteo -1 y logeo como error.
								AdpRun.logRun("Falla asignacion Broche para CtaTgi:Catastral " + cuenta.getNumeroCuenta() + ":" + catastral);
								nroBroche = 0L;
							}
						} else {
							// si el broche no es nulo, asigno el repartidor asociado al broche
							// NOTA: En realidad utilizamos el idBroche, para ser coeherentes con el sistema de broches actual.
							//       Tal vez en posteriores implementaciones convenga utilizar el repartidor activo de este broche.
							nroBroche = broche.getId();							
						}
						// porc-ph
						Double porcPh = (Double) toid.getValorObjectTipObjImpAtrDefinitionByCodigo("PorcPHSubParcela");
						// zona
						Long zona = (Long) toid.getValorObjectTipObjImpAtrDefinitionByCodigo("Zona");
						// dec-775 (No se informa, se manda null)
						Long dec775 = null;
						// vig-exe-desde (No se informa, se manda null)
						Date vigExeDesde = null;  
						// vig-exe-hasta (No se informa, se manda null)
						Date vigExeHasta = null;  
						// vig-jub-desde
						Date vigJubDesde = null;  // se carga cuando se procesan las exenciones de la cuenta
						// vig-jub-hasta
						Date vigJubHasta = null;  // se carga cuando se procesan las exenciones de la cuenta
						// fin-contrato
						Date finContrato = null;  // se carga cuando se procesan las exenciones de la cuenta
						// vig-uso
						Date vigUso = null;  // se carga cuando se procesan las exenciones de la cuenta
						// cim (No se informa, se manda null)
						Long cim = null; 
						// cod-gestion
						String codGestion = cuenta.getCodGesCue();
						// radio-ref
						Long radioRef = null; // luego lo tratamos como string, y si es null, espacio en blanco
						// nro-tramite  (No se informa)
						Long nroTramite = 0L;
						// fecha-tramite  (No se informa, se manda null)
						Date fechaTramite = null;
						// debito
						String debito = cuentaTipoAdhesion.get(cuenta.getNumeroCuenta());
						// inciso  (No se informa, se manda null)
						String inciso = "";
						// nuevo-jub  (No se informa, se manda null)
						String nuevoJub = ""; 
						// usuario
						String usuario = DemodaUtil.currentUserContext().getUserName();
						// fecha
						Date fecha = new Date();
						// val_terr_ref
						Double valTerrRef = 0.0D; //no lo pasamos porque se obtiene en informix
						// val_edif_ref
						Double valEdifRef = 0.0D;//no lo pasamos porque se obtiene en informix
						// val_libre_ref
						Double valLibreRef = 0.0D;//no lo pasamos porque se obtiene en informix
	
						if (!ListUtil.isNullOrEmpty(listCueExe)) {
							// Se recorre la lista de Exenciones para la Cuenta y se setean los campos que dependen de ellas.
							for (CueExe cueExe : listCueExe) {
	
								// Se obtiene la Exencion
								Exencion exencion = cueExe.getExencion();
								// Se obtiene el codigo de la Exencion
								String codExencion = exencion.getCodExencion();
								TipoSujeto tipoSujeto = cueExe.getTipoSujeto();
								
								// Exento Total
								if (Exencion.COD_EXENCION_EXENTO_TOTAL.equals(codExencion)) { 
									rt = 3L;
								}
								
								// Quita de Sobretasa por obra en construccion
								if (Exencion.COD_EXENCION_QUITA_SOBRETASA.equals(codExencion) || 
										Exencion.COD_EXENCION_QUITA_SOBRETASA_NU.equals(codExencion)) { 
									vigQuitaDesde = cueExe.getFechaDesde();
									vigQuitaHasta = cueExe.getFechaHasta();
									
									if (rt == null || rt != null && rt != 3L)
										rt = 2L;
								}
								
								// Quita de Sobretasa Inmuebles afectados
								/* 2008-11-12, lo comentamos porque el verdadeo codigo es el de predio_sujeto_expropiacion
								 * esto pensamos que fue un cambio de como interpretar los crt durante la migracion. 
								if (Exencion.COD_EXENCION_QUITA_SOBRETASA_INMUEBLES.equals(codExencion)) { 
									if(vigQuitaDesde == null && vigQuitaHasta == null){
										vigQuitaDesde = cueExe.getFechaDesde();
										vigQuitaHasta = cueExe.getFechaHasta();
									}
									
									if (rt == null || rt != null && rt != 3L && rt != 2L)
										rt = 1L;
								}*/
								
								// PREDIO_SUJETO_EXPROPIACION
								if (Exencion.COD_EXENCION_PREDIO_SUJETO_EXPROPIACION.equals(codExencion)) { 
									if(vigQuitaDesde == null && vigQuitaHasta == null){
										vigQuitaDesde = cueExe.getFechaDesde();
										vigQuitaHasta = cueExe.getFechaHasta();
									}
									
									if (rt == null || rt != null && rt != 3L && rt != 2L)
										rt = 1L;
								}
								
								// 2008-12-02: Si la cuenta esta excluida de la emision, 
								// seteamos el rt a 5
								if (cuenta.getEsExcluidaEmision() != null &&
									cuenta.getEsExcluidaEmision().equals(SiNo.SI.getBussId())) {
									rt = 5L;
								}
								
								// Exento 5 minimo R1 (ex Jubilado)
								if (Exencion.COD_EXENCION_EXENTO_5_MINIMOS.equals(codExencion)) { 
									if (tipoSujeto != null) { 
										// Jubilado Propietario
										if(TipoSujeto.COD_JUBILADO_PROPIETARIO.equals(tipoSujeto.getCodTipoSujeto())){ 
											vigJubDesde = cueExe.getFechaDesde();
											vigJubHasta = cueExe.getFechaHasta();
											tipoSujeto = cueExe.getTipoSujeto();
											tipoJub= 1L;
											porcJub=200L;
										} else if(TipoSujeto.COD_JUBILADO_INQUILINO.equals(tipoSujeto.getCodTipoSujeto())){ 
											// Jubilado Inquilino
											vigJubDesde = cueExe.getFechaDesde();
											vigJubHasta = cueExe.getFechaHasta();
											tipoSujeto = cueExe.getTipoSujeto();
											tipoJub= 2L;
											porcJub=200L;
											//finContrato = cueExe.getFechaVencContInq();
											finContrato = cueExe.getFechaHasta();
										} else {
											vigExeDesde = cueExe.getFechaDesde();
											vigExeHasta = cueExe.getFechaHasta();
											dec775 = 2L;
										}
									} else {
										vigExeDesde = cueExe.getFechaDesde();
										vigExeHasta = cueExe.getFechaHasta();
										dec775 = 2L;
									}
								}

								// Exento 50%
								if (Exencion.COD_EXENCION_EXENTO_50_PORCIENTO.equals(codExencion) ){ 
									uso = 8L;
									vigUso = cueExe.getFechaDesde();
								} else if (Exencion.COD_EXENCION_EXENTO_25_PORCIENTO.equals(codExencion)) {
									uso = 9L;
									vigUso = cueExe.getFechaDesde();
								}

							}
						}
						// ESCRITURA DE DATOS
						String sep = "|";
						
						// cuenta
						buffer.write(this.formatLong(nroCuenta, 10) + sep); 
						// sup-edif
						buffer.write(formatDouble(supEdi,8 ,0) + sep);
						//broche
						buffer.write(formatLong(idBroche, 6) + sep);
						//domicilio
						buffer.write(formatString(domicilio, 30) + sep);
						//ubic-finca
						buffer.write(formatString(ubicFinca, 30) + sep);
						//ubic-terreno
						buffer.write(formatString(ubicTerreno, 46) + sep);
						//valTerreno
						buffer.write(formatDouble(valTerreno, 9+3, 3) + sep); // tendria que ser 9,3 
						//valEdif 
						buffer.write(formatDouble(valEdif, 9+3, 3) + sep);
						//valLibre
						buffer.write(formatDouble(valLibre, 9+3, 3) + sep);
						//sup
						buffer.write(formatDouble(sup, 6, 0) + sep);  
						// rt
						buffer.write(formatLong(rt, 2) + sep);
						// radio
						buffer.write(formatLong(radio, 1) + sep);
						// tipo-jub
						buffer.write(formatLong(tipoJub, 2) + sep);
						// porc-jub
						buffer.write(formatLong(porcJub, 4) + sep);
						// uso
						buffer.write(formatLong(uso,1) + sep);
						// vig-quita-desde
						buffer.write(formatDateYearToMonth(vigQuitaDesde, 6) + sep);
						// vig-quita-hasta
						buffer.write(formatDateYearToMonth(vigQuitaHasta, 6) + sep);
						// tipo-mov
						buffer.write(formatString(StringUtil.formatLong(tipoMov), 1) + sep);
						// tipo-parcela
						buffer.write(formatLong(tipoParcela, 1) + sep);
						// nro-broche
						buffer.write(formatLong(nroBroche, 4) + sep);
						//nombre
						buffer.write(formatString(nombre, 44) + sep); // aumentamos de 30 a 44 para incluir el cuit
						//catastral
						buffer.write(formatCatastral(catastral, 14) + sep);
						//porc-ph
						buffer.write(formatDouble(porcPh, 3+2, 2) + sep);
						//zona
						buffer.write(formatLong(zona, 1) + sep);
						//dec-775 
						buffer.write(formatLong(dec775, 1) + sep);
						//vig-exe-desde
						buffer.write(formatDateYearToMonth(vigExeDesde, 6) + sep);
						//vig-exe-hasta
						buffer.write(formatDateYearToMonth(vigExeHasta, 6) + sep);
						//vig-jub-desde
						buffer.write(formatDateYearToMonth(vigJubDesde, 6) + sep);
						//vig-jub-hasta, en caso de ser tipoJub 1 o 2, hay quen mandar 999999 cuando no tiene vigJudHasta
						if (tipoJub != null && (tipoJub == 1L || tipoJub == 2L)) {
							buffer.write(formatDateYearToMonth(vigJubHasta, 6, "999999") + sep);
						} else { //sino mandamos "", que corresponde a un vigJubHasta en null.
							buffer.write(formatDateYearToMonth(vigJubHasta, 6, "") + sep);
						}
						//fin-contrato
						buffer.write(formatDateYearToMonth(finContrato, 6) + sep);
						//vig-uso
						buffer.write(formatDateYearToMonth(vigUso, 6) + sep);
						//cim
						buffer.write(formatLong(cim, 6) + sep);
						//cod-gestion
						buffer.write(formatLong(codGestion, 10) + sep);
						//radio-ref
						buffer.write(formatString(radioRef, 1) + sep);
						// nro-tramite
						buffer.write(formatLong(nroTramite, 6) + sep);
						// fecha-tramite
						buffer.write(formatDateYearToDate(fechaTramite, 8) + sep);
						// debito
						buffer.write(formatString(debito, 1) + sep);
						// inciso
						buffer.write(formatString(inciso, 2) + sep);
						// nuevo-jub 
						buffer.write(formatLong(nuevoJub, 1) + sep);
						//usuario
						buffer.write(formatString(usuario, 10) + sep);
						//fecha
						buffer.write(formatDateYearToSecond(fecha, 20) + sep);
						//val_terr_ref
						buffer.write(formatDouble(valTerrRef, 9+3, 3) + sep);
						//val_edif_ref
						buffer.write(formatDouble(valEdifRef, 9+3, 3) + sep);
						//val_libre_ref
						buffer.write(formatDouble(valLibreRef, 9+3, 3));
						// crea una nueva linea
						buffer.newLine();
						
						c++;
						
						/*if (c >= 10) {
							contieneCuentas = false;
							break;
						}*/
					}
					
					indiceSkip += salto; // incremento el indice del skip con el salto establecido
				}
				AdpRun.changeRunMessage("Cuentas procesadas: " + c, 0);
				buffer.flush();
				//cerramos y volvemos a abrir session, para fluir los caches de hibernate				
				SiatHibernateUtil.closeSession();
				SiatHibernateUtil.currentSession();
			}
			if(c == 0){
				// --> Resultado vacio
				buffer.write("No se encontraron registros de Cuentas "  );
				// <-- Fin Resultado vacio
			}

			buffer.close();
			
			return pathCompleto;
		} catch (Exception e) {
			String msg = "Error en la generacion del archivo ALFAX. " + (cuenta != null? " Id de cuenta:" + cuenta.getId():"Cuenta nula.");
			log.error(msg, e);
			AdpRun.logRun(msg ,e );
			emision.addRecoverableError(EmiError.EMISION_ERROR_GENERAR_ALFAX);
			throw new Exception(e);
		}
	}
	
	/*
	 * Si tiene una exencion 28 (Quita de Sopretasa Inmuebles afectados) retorna 1
	 * Si tiene una exencion 1 (Quita de Sobretasa por obra en construccion) retorna 2
	 * Si tiene una exencion 5 (Exento Total) retorna 3
	 * Sino, retorna 0
	 *    
	 */
	public Long getCrt(String codExencion){
		if (codExencion.equals("28")) return 1L;
		if (codExencion.equals("1")) return 2L;
		if (codExencion.equals("5")) return 3L;
		return 0L;
	}
	
	
	private void ejecutarPaso2( AdpRun adpRun, Emision emision, Zona zona) throws Exception{
		
		Map<String, Long> mapaAuxDeu = new HashMap<String, Long>();
		
		Datum datum;
		String line; 
		AuxDeuda auxDeuda = null;
		long c = 0;
		
		String fileNameCalDefi    = "calc_defi.z"+ zona.getId();
		String fileNameDetCalDefi = "det_calc_defi.z"+ zona.getId();

		String inputDir = adpRun.getProcessDir(AdpRunDirEnum.ENTRADA); // "/mnt/siat/EmisionTgi/entrada"
		String procesandoDir = adpRun.getProcessDir(AdpRunDirEnum.PROCESANDO); // "/mnt/siat/EmisionTgi/procesando"
		
		// leerlos desde ENTRADA
		File fileCalDefi = new File(inputDir+ File.separator +fileNameCalDefi);
		if(!fileCalDefi.exists()){
			String descripcion = "No se encontro el archivo: " + fileNameCalDefi;
            adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false);
            adpRun.logError(descripcion);
            emision.addRecoverableValueError(descripcion);
		}
		File fileDetCalDefi = new File(inputDir+ File.separator +fileNameDetCalDefi);
		if(!fileDetCalDefi.exists()){
			String descripcion = "No se encontro el archivo: " + fileNameDetCalDefi;
            adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false);
            adpRun.logError(descripcion);
            emision.addRecoverableValueError(descripcion);
		}
		
		if(emision.hasError()){
			return;
		}
		
		// mueve desde ENTRADA a PROCESANDO
		adpRun.moveFile(fileNameCalDefi, AdpRunDirEnum.ENTRADA, AdpRunDirEnum.PROCESANDO);
		adpRun.moveFile(fileNameDetCalDefi, AdpRunDirEnum.ENTRADA, AdpRunDirEnum.PROCESANDO);

		// leerlos desde PROCESANDO
		fileCalDefi = new File(procesandoDir+ File.separator +fileNameCalDefi);
		if(!fileCalDefi.exists()){
			String descripcion = "No se encontro el archivo: " + fileNameCalDefi;
            adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false);
            adpRun.logError(descripcion);
            emision.addRecoverableValueError(descripcion);
		}
		fileDetCalDefi = new File(procesandoDir+ File.separator +fileNameDetCalDefi);
		if(!fileDetCalDefi.exists()){
			String descripcion = "No se encontro el archivo: " + fileNameDetCalDefi;
            adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false);
            adpRun.logError(descripcion);
            emision.addRecoverableValueError(descripcion);
		}

		if(emision.hasError()){
			return;
		}

		BufferedReader inputCalDefi    = new BufferedReader(new FileReader(fileCalDefi));
		
		// borrado de los registros auxdeu de la emision
		int regBorrados = EmiDAOFactory.getAuxDeudaDAO().deleteAuxDeudaByIdEmision(emision);
		log.debug("registros borrados: " + regBorrados);
		SiatHibernateUtil.currentSession().getTransaction().commit();
 		SiatHibernateUtil.currentSession().beginTransaction();
 		
		// Incializacion de caches
 		log.info("Iniciando cache de Detalles calculados");
 		initMapCuentaCalcDetConceptos(fileDetCalDefi.getPath());
 		log.info("Iniciando cache de Datos de Cuentas");
 		initMapCuentaCalcDetCuentas(emision);
 		
 		boolean errorDatos = false;
		// lectura del archivo calDefi: cada 250 registros haciendo commit y beginTransaction. No flush y clear 
	    while (( line = inputCalDefi.readLine()) != null) {
	    		errorDatos = false;
	     		c++;
	     		AdpRun.changeRunMessage("Procesando linea: " + c, 30);
	     		if(c%2500 == 0) {
	     			SiatHibernateUtil.currentSession().getTransaction().commit();
	     			SiatHibernateUtil.closeSession();
	     			SiatHibernateUtil.currentSession().beginTransaction();
	     			SiatHibernateUtil.currentSession().refresh(emision);
	     		}

	    		datum = Datum.parse(line);	//Parseamos la linea de Deuda
	     		if((datum.getColNumMax()==1 || datum.getColNumMax()==0) 
	     				&& (datum.getCols(0)==null || "".equals(datum.getCols(0).trim()))){
	     			continue;
	     		}
	    		if(datum.getColNumMax()<22){
	     			String descripcion = "Error en la creacion de auxDeuda. La linea " + c + "no tiene la cantidad de columnas establecidas";
	     			log.error(descripcion);
	     			emision.addRecoverableValueError(descripcion);
	     			adpRun.logDebug(descripcion);
	     			// continua con la otra linea a pesar del error
	     			continue;
	     		}
	    		// 0010000102|200805||0|0|119.6|0.0|0|119.6|119.6|31/12/1899|31/12/1899|0||0|0| |||||1|0|anadal0|2008-02-22 09:48:10|
	    		// Datos del archivo:
	     		String nroCuenta = datum.getCols(0);
	     		String anioPeriodo  = datum.getCols(1);
	     		// tipoMov
	     		Long codEmis = datum.getLong(3);
	     		// porcJub
	     		// importeTgi
	     		// importeMej
	     		// porcExo
	     		Double importeTotal = datum.getDouble(8);
	     		// importeRecargo
	     		// fechVtoRecargo
	     		Date fechVtoOrig = datum.getDate(11,DateUtil.ddMMYYYY_MASK);
	     		// nroTramite
	     		// fechaTramite
	     		// codRt
	     		// tipoJub
	     		// catastral
	     		// tipoParcela
	     		// zona
	     		// radio
	     		// usuario
	     		// fechaHora

	     		Long idCuenta = getIdCuentaFromMap(nroCuenta);
				if (idCuenta == null){
					String descripcion = "Error en la creacion de auxDeuda. Cuenta no encontrada: "  + nroCuenta;
					log.debug(descripcion);
					adpRun.logDebug(descripcion);
					errorDatos = true;
				}

				if(codEmis == null ){
	     			codEmis = 0L;
	     			String descripcion = "Codigo de emision nulo";
					log.debug(descripcion);
					//emision.addRecoverableValueError(descripcion); no cargo este error
					adpRun.logDebug(descripcion);
	     		}
	     		
	     		// validaciones sobre vigencia, anio y periodo
	     		if(anioPeriodo == null){
	     			String descripcion = "Vigencia nula";
					log.debug(descripcion);
					emision.addRecoverableValueError(descripcion);
					adpRun.logDebug(descripcion);
					errorDatos = true;
	     		}
	     		
	     		if(errorDatos){
	     			continue;
	     		}

				Double importeBruto = importeTotal;
				Double importe = importeTotal; 
				String anio    = anioPeriodo.substring(0, 4);
	     		String periodo = anioPeriodo.substring(4,6);

	     		if(codEmis.longValue() != 0){
	     			// importe total = importe calculado
	     			// importe con exencion = 0
	     			importe = 0.0D;
	     		}
	     		
	     		String atrAseVal = getAtrAseValFromMap(nroCuenta);
	     		Double conc1 = getConceptoFromMap(nroCuenta, anioPeriodo, 1);
	     		Double conc2 = getConceptoFromMap(nroCuenta, anioPeriodo, 2);
	     		Double conc3 = getConceptoFromMap(nroCuenta, anioPeriodo, 3);
	     		Double conc4 = getConceptoFromMap(nroCuenta, anioPeriodo, 4);
	     		
				// Creamos el registro de AuxDeuda TODO ver validaciones durante la creacion
	     		auxDeuda = this.createAuxDeuda (idCuenta, emision, fechVtoOrig,
						importeBruto, importe, Long.valueOf(periodo), Long.valueOf(anio), atrAseVal, conc1, conc2, conc3, conc4);
				
				if(auxDeuda.hasError()){
	     			String descripcion = "Error al crear el registro de auxDeuda correspondiente a la linea " + c;
	     			log.error(descripcion);
	     			emision.addRecoverableValueError(descripcion);
	     			auxDeuda.addErrorMessages(emision);
	    			adpRun.logDebug(descripcion);
	     		}
	    }
	    
	    SiatHibernateUtil.currentSession().getTransaction().commit();
	    SiatHibernateUtil.closeSession();
	    SiatHibernateUtil.currentSession().beginTransaction();
	    SiatHibernateUtil.currentSession().refresh(emision);
	    
	    if (!emision.hasError()){
	    	// mueve los archivos de PROCESANDO A PROCESADO OK
			adpRun.moveFile(fileNameCalDefi, AdpRunDirEnum.PROCESANDO, AdpRunDirEnum.PROCESADO_OK);
			adpRun.moveFile(fileNameDetCalDefi, AdpRunDirEnum.PROCESANDO, AdpRunDirEnum.PROCESADO_OK);
	    }else{
	    	// mueve los archivos de PROCESANDO A PROCESADO CON ERROR
			adpRun.moveFile(fileNameCalDefi, AdpRunDirEnum.PROCESANDO, AdpRunDirEnum.PROCESADO_ERROR);
			adpRun.moveFile(fileNameDetCalDefi, AdpRunDirEnum.PROCESANDO, AdpRunDirEnum.PROCESADO_ERROR);
	    }
	}

	/**
	 * Actualiza cada registro Auxdeu con los conceptos leidos del archivo detCaldefi
	 * En caso de error los logea a traves de adp y carga errores en la emision
	 * @param adpRun
	 * @param emision
	 * @param mapaAuxDeu
	 * @param inputDetCalDefi
	 * @throws Exception
	 */
	private void actualizarAuxDeu( AdpRun adpRun, Emision emision, Map<String, Long> mapaAuxDeu, BufferedReader inputDetCalDefi) throws Exception{
		
		Datum datum;
		String line; 
		AuxDeuda auxDeuda = null;
		long c = 0;
		
		for (String s: mapaAuxDeu.keySet()) {
			log.debug("Key " + s + ", Value " + mapaAuxDeu.get(s));
		}
		
		while (( line = inputDetCalDefi.readLine()) != null ) { 
	    		boolean errorDatos = false;
	     		c++;
	     		if(c%2500==0){
	     			//SiatHibernateUtil.currentSession().flush();
	    			//SiatHibernateUtil.currentSession().clear();
	     			SiatHibernateUtil.currentSession().getTransaction().commit();
	     			SiatHibernateUtil.closeSession();
	     			SiatHibernateUtil.currentSession();
	     			SiatHibernateUtil.currentSession().beginTransaction();
	     		}
	    		datum = Datum.parse(line);	//Parseamos la linea de Deuda
	     		if((datum.getColNumMax()==1 || datum.getColNumMax()==0) 
	     				&& (datum.getCols(0)==null || "".equals(datum.getCols(0).trim()))){
	     			continue;
	     		}
	    		if(datum.getColNumMax()<4){
	     			String descripcion = "Error en la carga de conceptos de auxDeuda. La linea " + c + "no tiene la cantidad de columnas establecidas";
	     			log.error(descripcion);
	     			emision.addRecoverableValueError(descripcion);
	     			adpRun.logDebug(descripcion);
	     			// continua con la otra linea a pesar del error
	     			continue;
	     		}
	    		
	    		// lectura de datos del archivo:
	     		String nroCuenta = ""+datum.getLong(0);
	     		String vigencia = datum.getCols(1);
	     		Long idConcepto = datum.getLong(2); 
	     		Double importe = datum.getDouble(3);
	     		String clave = nroCuenta + vigencia;
	     		
	     		log.debug("KEY: " + clave);

	     		// Validaciones
	     		
	     		// TODO: Derecho por JDBC
	     		auxDeuda = AuxDeuda.getByIdNull(mapaAuxDeu.get(clave));
	     		if (auxDeuda == null){
	     			String descripcion = "Error en la carga de conceptos de auxDeuda. No se encuentra la auxDeuda para la linea " + c;
	     			log.debug(descripcion);
	     			emision.addRecoverableValueError(descripcion);
					adpRun.logDebug(descripcion);
					errorDatos = true;
				}
				if(idConcepto == null){
	     			String descripcion = "Error en la carga de conceptos de auxDeuda. No esta cargada el idConcepto en la linea " + c;
	     			log.debug(descripcion);
	     			emision.addRecoverableValueError(descripcion);
					adpRun.logDebug(descripcion);
					errorDatos = true;
				}
				
				if(errorDatos){continue;}
				
	     		if (idConcepto.longValue() == 1){
	     			auxDeuda.setConc1(importe);
	     		}else if (idConcepto.longValue() == 2){
	     			auxDeuda.setConc2(importe);
	     		}else if (idConcepto.longValue() == 3){
	     			auxDeuda.setConc3(importe);
	     		}else if (idConcepto.longValue() == 4){
	     			auxDeuda.setConc4(importe);
	     		}else{
	     			String descripcion = "Error en la carga de conceptos de auxDeuda. el idConcepto no es un valor entre 1 y cuatro en la linea " + c;
	     			log.debug(descripcion);
	     			emision.addRecoverableValueError(descripcion);
					adpRun.logDebug(descripcion);
					continue;
	     		}
	     		
	     		// TODO: Corte de Control
				// actualizamos el registro de AuxDeuda
	     		EmiDAOFactory.getAuxDeudaDAO().update(auxDeuda);
				
	     		if(auxDeuda.hasError()){
	     			String descripcion = "Error en la carga de conceptos de auxDeuda en la linea" + c;
	     			log.debug(descripcion);
	     			emision.addRecoverableValueError(descripcion);
	     			adpRun.logDebug(descripcion);
	     		}
	    }
	}

	private void generarDeudaAdmin( AdpRun adpRun, Emision emision) throws Exception {
		// cada 250 registros hace commit y beginTransaction. No flush y clear
		int firstResult = 0;
		int maxResults = 2500;
 		boolean contieneAuxDeuda = true;
 		Map<String, Long> cuentas = new HashMap<String, Long>(); //mapa de las cuentas a las que se le genero deuda
 		// Inicializamos el mapa de conceptos de la emision
 		emision.initializeMapCodRecCon();
 		
 		while (contieneAuxDeuda) {
 			// obtencion de la lista de AuxDeuda paginada
 			//List<AuxDeuda> listAuxDeuda = AuxDeuda.getListAuxDeudaByIdEmision(emision.getId(), firstResult, maxResults);
 			List<Object[]> listAuxDeuda = AuxDeuda.getListAuxDeudaByIdEmision(emision.getId(), firstResult, maxResults);
 			contieneAuxDeuda = (listAuxDeuda.size() > 0);
 			
 			for (Object[] datos : listAuxDeuda) {
 				AuxDeuda auxDeuda = (AuxDeuda) datos[0];
 				String nroCuenta = (String) datos[1];
 				// creacion de cada deudaAdmin a partir de cada auxDeuda
 				// creacion de cada DeuAdmRecCon para cada concepto de la auxDeuda
 				AdpRun.changeRunMessage("Cuentas procesadas: " + firstResult, 30);
 				DeudaAdmin deudaAdmin = emision.createDeudaAdminFromAuxDeuda(auxDeuda);
 				if(deudaAdmin.hasError()){
	     			String descripcion = "Error al crear el registro de deudaAdmin a partir de la auxDeuda: " + auxDeuda.getId();
	     			log.error(descripcion);
	     			AdpRun.logRun(descripcion);
	     			emision.addRecoverableValueError(descripcion);
	     			auxDeuda.addErrorMessages(emision);
	     		} else {
	     			Long periodoGrabado = cuentas.get(nroCuenta);
	     			if(periodoGrabado == null){
	     				int bimestre = SiatUtil.calcularBimestre(deudaAdmin.getPeriodo().intValue());
	     				cuentas.put(nroCuenta, deudaAdmin.getAnio()*100 + bimestre);
	     			}
	     		}
 			}
 			
 			firstResult += maxResults; // paginacion
 			
 			// commit y beginTransaction
 			SiatHibernateUtil.currentSession().getTransaction().commit();
			SiatHibernateUtil.closeSession();
			SiatHibernateUtil.currentSession();
 			SiatHibernateUtil.currentSession().beginTransaction();
 		}
 		
 		if(!emision.hasError()){
 		
 			// Recorremos el Mapa
 			AdpRun.changeRunMessage("Actualizando sistema de catastro: " + firstResult, 1);
 			
 			for(String nroCuenta: cuentas.keySet()) {
 				Long anioPeriodo = cuentas.get(nroCuenta);
 				PadDAOFactory.getCatastroJDBCDAO().updatePerEmiCue(nroCuenta,anioPeriodo, 
 								"siat");
 			}
 		}
 		
	}
		
	/**
	 * Carga el mapa con toda los datos de cuentas necesarios para el paso 2, 
	 * extraidos de la tabla pad_cuenta y valores de objimp
	 * El mapa que cargado con el siguiente formato:
	 * clave: [nroCuenta]
	 * valor: [idCuenta];[atrAseVal] 
	 */
	private void initMapCuentaCalcDetCuentas(Emision emision) throws Exception {
		Recurso recurso = emision.getRecurso();
		Long idRecurso = recurso.getId();
		Date fechaAnalisis = emision.getFechaEmision();
		
		Long idAtributo = recurso.getAtributoAse().getId();
    	TipObjImpAtr tipObjImpAtr = TipObjImpAtr.getByIdAtributo(idAtributo);
    	if(tipObjImpAtr != null) {
    		Long idTipObjImpAtr = tipObjImpAtr.getId();
    		this.mapCuentaAtrAseVal = new HashMap<String, String>();
    		PadDAOFactory.getObjImpAtrValDAO().getMapCuentaValTipObjImpAtrVal(this.mapCuentaAtrAseVal, idRecurso, idTipObjImpAtr, fechaAnalisis);
    	} else {
    		throw new Exception("Fallo inicializacion: No se encontro tipObjImpAtr para el idAtributo:" + idAtributo + " definido en el idRecurso: " + idRecurso);
    	}
	}

	/**
	 * Carga el mapa con toda los datos de cuentas necesarios para el paso 2,
	 * extraidos del archivo de detalle de calculos: det_calc_defi.z*
	 * El mapa que cargado con el siguiente formato:
	 * clave: [nroCuenta][anio][periodo]
	 * valor: [conc1];[conc2];[conc3];[conc4] 
	 */
	private void initMapCuentaCalcDetConceptos(String pathDetCalDefi) throws Exception {

		File fileDetCalDefi = new File(pathDetCalDefi);
		this.mapCuentaConceptos = new HashMap<String, String>();

		if(!fileDetCalDefi.exists()) {
			throw new IOException();
		}

		BufferedReader inputDetCalDefi = new BufferedReader(new FileReader(fileDetCalDefi));
		Datum datum;
		String line;
		Long currentLine = 0L;

		while (( line = inputDetCalDefi.readLine()) != null ) { 

			currentLine++;
    		datum = Datum.parse(line);	// Parseamos la linea de deuda
     		
    		if (( datum.getColNumMax()==1 || datum.getColNumMax()==0 ) 
     				&& (datum.getCols(0)==null || "".equals(datum.getCols(0).trim())) ) {
     			continue;
     		}
     		
     		if ( datum.getColNumMax() < 4 ){
     			String descripcion = "Error en la inicializacion del cache. La linea " + currentLine + 
     										" no tiene la cantidad de columnas establecidas";
     			log.error(descripcion);
     			// continua con la otra linea a pesar del error
     			continue;
     		}
    		
    		// lectura de datos del archivo:
     		String nroCuenta = Long.toString(datum.getLong(0));
     		String anioPeriodo = datum.getCols(1);
     		Long idConcepto = datum.getLong(2); 
     		Double importe = datum.getDouble(3);
     		
     		if(idConcepto == null){
     			String descripcion = "Error en la inicializacion del cache. No esta cargada " +
     										"el idConcepto en la linea " + currentLine;
     			log.error(descripcion);
     			continue;
			}
			
     		String clave = nroCuenta + anioPeriodo;
 			String value = mapCuentaConceptos.get(clave);
			if (value == null) {
				value = "";
			} else {
				value += ";";
			}
			value += importe;
			mapCuentaConceptos.put(clave, value);
		}
	}

	private Long getIdCuentaFromMap(String nroCuenta) throws Exception {
		try {
			String clave = new Long(nroCuenta).toString();
			String value = mapCuentaAtrAseVal.get(clave);
			String strIdCuenta = value.split(";")[0];
			
			return new Long(strIdCuenta);
		} catch (Exception e) {
			return null;
		}
	}
	
	private String getAtrAseValFromMap(String nroCuenta) throws Exception {
		try {
			String clave = new Long(nroCuenta).toString();
			String value = mapCuentaAtrAseVal.get(clave);
			String strAtrAseVal = value.split(";")[1];
			
			return strAtrAseVal;
		} catch (Exception e) {
			return null;
		}
	}
		
	private Double getConceptoFromMap(String nroCuenta, String anioPeriodo, int nroConcepto) throws Exception {
		try {
			if (nroConcepto < 1 || nroConcepto > 4)
				return null;
			
			nroCuenta = new Long(nroCuenta).toString();
			String clave = nroCuenta + anioPeriodo;
			String value = mapCuentaConceptos.get(clave);
			String strConcepto = value.split(";")[nroConcepto - 1];
			Double ret  = new Double(strConcepto);
			if (ret == 0.0D) {
				ret = null;
			}
			return ret;
		} catch (Exception e) {
			return null;
		}
	}
	
}
