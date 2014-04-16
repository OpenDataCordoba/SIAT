//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.novobjimp;


import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.cas.buss.bean.CasSolicitudManager;
import ar.gov.rosario.siat.cas.buss.bean.TipoSolicitud;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.buss.bean.Seccion;
import ar.gov.rosario.siat.def.buss.bean.TipObjImp;
import ar.gov.rosario.siat.def.buss.bean.Zona;
import ar.gov.rosario.siat.exe.buss.bean.CueExe;
import ar.gov.rosario.siat.exe.buss.bean.EstadoCueExe;
import ar.gov.rosario.siat.exe.buss.bean.ExeExencionManager;
import ar.gov.rosario.siat.exe.buss.bean.Exencion;
import ar.gov.rosario.siat.exe.buss.bean.HisEstCueExe;
import ar.gov.rosario.siat.exe.buss.bean.TipoSujeto;
import ar.gov.rosario.siat.exe.buss.dao.ExeDAOFactory;
import ar.gov.rosario.siat.pad.buss.bean.Broche;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.Domicilio;
import ar.gov.rosario.siat.pad.buss.bean.ObjImp;
import ar.gov.rosario.siat.pad.buss.bean.PadDistribucionManager;
import ar.gov.rosario.siat.pad.buss.bean.PadDomicilioManager;
import ar.gov.rosario.siat.pad.buss.bean.PadObjetoImponibleManager;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpAtrDefinition;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpDefinition;
import ar.gov.rosario.siat.pro.buss.bean.Proceso;
import ar.gov.rosario.siat.seg.buss.bean.UsuarioSiat;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaMail;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Datum;
import coop.tecso.demoda.iface.model.DemodaStringMsg;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.UserContext;

/**
 * Se encarga de procesar las novedades de cualquier objeto imponible.
 * Por lo general los distintos procesos de ADP invocan al metodo
 * dispatchTranasac() y le pasa el nombre del archivo recivido.
 * Este metodo parsea el arvhivo y delega a otros metodos
 * segun el codTransaccion del registro.
 * @author Tecso Coop. Ltda.
 *
 */
public class NovObjImp {
	
	static Logger log = Logger.getLogger(NovObjImp.class);
	
	private static final int HEADCOLS = 8; //nro de columnas de head

	public static final int COLSCODTRANSAC = 0; //"Codigo Transaccion" 
	public static final int COLSCODTIPOBJIMP = 1; //"Codigo Tipo Objeto Imponible"
	public static final int COLSCODAREAORIG = 2; //"Codigo Area Origen"
	public static final int COLSFECHAACCION = 3; //"Fecha Accio"
	public static final int COLSCODACCION = 4; //"Codigo de Accion" 
	public static final int COLSCLAVE = 5;//"Clave"
	public static final int COLSNROCUENTA = 6;//"Nro Cuenta" 
	public static final int COLSFECHAVIG = 7; //"Fecha Vigencia"
	
	public static final int COLSINICIOATR = 8; // Columna donde comienzan los atributos.
	public static final int COLSFINATR = 35; // Columna donde terminan los atributos.
	
	public static final int COLSCLAVEFUNCIONAL4PARCELA = 24; // "Catastral" , clave funcional para el TipObjImp Parcela.
	public static final int COLSTIPOPARCELA4PARCELA = 14; // "Tipo Parcela".  
	public static final int COLSUSOPARCELA4PARCELA = 15; // "Uso Parcela".
	public static final int COLSCRT4PARCELA = 30; // "Codigo de Responsabilidad Tributaria (CRT)".
	public static final int COLSCRTFECHADESDE4PARCELA = 31; // "Fecha Desde de CRT".
	public static final int COLSCRTFECHAHASTA4PARCELA = 32; // "Fecha Hasta de CRT".
	
	boolean statusOk = true;

	public static void sendMailError(Long idCorrida, String filename, String msg) {
		try {
			String server = SiatParam.getString(SiatParam.MAIL_SERVER);
			String to = SiatParam.getString("NovObjImpMailTo", "");
			String from = SiatParam.getString("NovObjImpMailFrom", "");
			String subject = "[siat:novobjimp] Error durante el procesamiento de Novedades";
			String body = "Se detecto un error al procesar archivo recibido.\nPara mas informacion consulte logs\n\n";
			body += "Id Corrida: " + idCorrida + "\n";
			body += "Nombre Archivo: " + filename + "\n";
			body += "Mensaje:\n" + msg;
			
			DemodaMail.send(server, to, from, subject, body, "");
			
		} catch (Exception e) {
			log.error("sendMailError(): Fallo envio de Mail.");
		}
	}
	
	/**
	 * Recorre cada renglon del inputFilename.
	 * Verifica codTransaccion y accion e invoca a otros metodos
	 * segun estos valores: los casos pueden ser:
	 * codTransaccion           accion      metodo invocado
	 * 1-Nov Parcela               A        NovObjImp.altaObjImp()
	 *       "                     B        NovObjImp.bajaObjImp()
	 *       "                     M        NovObjImp.modifObjImp()
	 *
	 * 2-Nov Titulares             A        NovTitular.altaTitular()
	 * 2-Nov Titulares             B        NovTitular.bajaTitular()
	 * 2-Nov Titulares             M        NovTitular.modifTitular()             
	 *
	 * 3-Nov RelacionCuenta        A        NovRelacionCuenta.altaRelacion()
	 * 3-Nov RelacionCuenta        B        NovRelacionCuenta.bajaRelacion()
	 * 3-Nov RelacionCuenta        M        NovRelacionCuenta.modifRelacion()             
	 * 
	 * @param inputFilePath path absoluto al archivo de novedad.
	 */
	public void dispatchTransac(AdpRun run) throws Exception {
		Session session = null;
		Transaction tx = null; 
	
		UserContext userContext = new UserContext();
		userContext.setUserName("siat");
		userContext.setIdUsuarioSiat(UsuarioSiat.ID_USUARIO_SIAT); 
		
		DemodaUtil.setCurrentUserContext(userContext);
		String line = "";
		try{
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
				
			// Abrimos archivo para parsearlo
			BufferedReader is = run.getInputFileReader(AdpRunDirEnum.PROCESANDO);
			line = "";        
			while ((line = AdpRun.readLine(is)) != null) {
			
				// en habilitaciones nos llegan pipes escapados que en realidad son
				// verdaderos separadores de columnas.
				if (run.getProcess() != null 
						&& "NovHabilitaciones".equals(run.getProcess().getCodProceso())) {
					line = line.replace("\\|", "|").trim();
				}
				
				Datum datum = Datum.parse(line);
				//verificamos formato de linea (almenos 8 columnas de cabecera)
				if (datum.getColNumMax() < HEADCOLS) {
					run.logDebug("Error de headcols");
					run.changeStateError("El archivo posee un formato invalido, se experaban al menos " + HEADCOLS + " columnas.");
					return;
				}
				String codAreaOrig = datum.getCols(COLSCODAREAORIG);
				
				if(!validarArea(run,codAreaOrig)){
					return;
				}
				
				String codTransac = datum.getCols(COLSCODTRANSAC);
				String codAccion = datum.getCols(COLSCODACCION);
				if ("1".equals(codTransac)) { //Nov. ObjImp
					NovObjImp novObjImp = new NovObjImp();
					
					if ("A".equals(codAccion)) { statusOk = novObjImp.altaObjImp(run, datum);
					} else if ("B".equals(codAccion)) { statusOk = novObjImp.bajaObjImp(run, datum);
					} else if ("M".equals(codAccion)) { statusOk = novObjImp.modifObjImp(run, datum);
					} else if ("C".equals(codAccion)) { statusOk = novObjImp.correctObjImp(run, datum);
					} else {
						run.changeStateError("Código Acción desconocido: " + codAccion);
						break;
					}
					
				} else if ("2".equals(codTransac)) { //Nov Titulares
					NovTitular novTitular = new NovTitular();
					if ("A".equals(codAccion)) { statusOk = novTitular.altaTitular(run, datum);
					} else if ("B".equals(codAccion)) { statusOk = novTitular.bajaTitular(run, datum);
					} else if ("M".equals(codAccion)) { statusOk = novTitular.modifTitular(run, datum);
					} else {
						run.changeStateError("Código Acción desconocido: " + codAccion);
						break;
					}
					
				} else if ("3".equals(codTransac)) { //Nov RelacionCuenta
					NovRelacionCuenta novRelacion = new NovRelacionCuenta();
					if ("A".equals(codAccion)) { statusOk = novRelacion.altaRelacion(run, datum);
					} else if ("B".equals(codAccion)) { statusOk = novRelacion.bajaRelacion(run, datum);
					} else if ("M".equals(codAccion)) { statusOk = novRelacion.modifRelacion(run, datum);
					} else { 
						run.changeStateError("Código Acción desconocido: " + codAccion);
						break;
					}         		
				} else {
					//tx.rollback();
					run.changeStateError("Codigo de transaccion desconocido: " + codTransac);
					break;//return;
				}
				if(!statusOk)
					break;
			}	
			if(statusOk){
				tx.commit();
				String msg = "Proceso de Novedad, Se realizo con Exito";
				run.changeStateFinOk(msg);				
			}else{
				String msg = "Error producido por linea: " + line;
				run.logError(msg);
				sendMailError(run.getId(), run.getInputFilename(), msg);
				tx.rollback();
			}
		} catch (Exception e) {
			String msg = "Error producido por linea: " + line + "\n";
			msg += "Error Grave: " + e.toString();
			run.logError(msg, e);
			sendMailError(run.getId(), run.getInputFilename(), msg);
			run.changeStateError(msg, e);			
			if(tx != null) tx.rollback();
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public static boolean validarArea(AdpRun run, String codAreaOrig){
	 	if(Proceso.PROCESO_NOVEDADES_CATASTRO.equals(run.getProcess().getCodProceso()) && !("CATASTRO".equals(codAreaOrig) || "HAB".equals(codAreaOrig))){
           	String msg = "El Area de Origen: "+codAreaOrig+" no puede ser procesada por el Proceso: "+run.getProcess().getDesProceso()+".";
           	run.logError(msg);
           	run.changeStateError(msg);
           	return false;
    	}
	 	return true;
	}
	
	public boolean altaObjImp(AdpRun run, Datum datum) throws Exception {
		String codTipObjImp = datum.getCols(COLSCODTIPOBJIMP);
		if ("PARCELA".equals(codTipObjImp)) { return this.altaParcela(run, datum);}
		if ("COMERCIO".equals(codTipObjImp)) { return this.altaComercio(run, datum);}
		if ("CEMENTERIO".equals(codTipObjImp)) { return this.altaSepultura(run, datum);}

		String msg = "Proceso de Novedad, Alta el ObjImp, Codigo de Tipo de Objeto Imponible desconocido: " + datum.getCols(COLSCODTIPOBJIMP);
        run.logError(msg);
        run.changeStateError(msg);
        return false;		
	}
	
	public boolean altaParcela(AdpRun run, Datum datum) throws Exception {
		try {
			
			// Recuperamos el Tipo de Objeto Imponible Parcela
			TipObjImp tipObjImp = TipObjImp.getByCodigo(datum.getCols(COLSCODTIPOBJIMP));
			if (tipObjImp == null) {
	           	String msg = "Proceso de Novedad, Alta el ObjImp, Codigo de Tipo de Objeto Imponible desconocido: " + datum.getCols(COLSCODTIPOBJIMP);
	           	run.logError(msg);
	           	run.changeStateError(msg);
	           	return false;
			}					

			// Verificamos que el ObjImp no exista en la db.
			ObjImp objImp = null;
			objImp = ObjImp.getByTipObjImpYNroCta(tipObjImp.getId(), datum.getCols(COLSCLAVE));
			if(objImp != null){
				String msg = "Proceso de Novedad, Alta de ObjImp: El ObjImp " 
						+"con TipObjImp="+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVE)
						+" existe en la db.";
				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
			// Recuperamos algunos datos generales para ObjImp y sus atributos.
			String clave = datum.getCols(COLSCLAVE);

			//String claveFuncional = datum.getCols(COLSCLAVEFUNCIONAL4PARCELA);

			Date fechaNovedad = datum.getDate(COLSFECHAACCION);
			if (fechaNovedad == null) {
				String msg = "Proceso de Novedad, Alta de ObjImp: fechaNovedad (alias fechaAccion), posee formato invalido. " + datum.getCols(COLSFECHAACCION);
				run.logError(msg);
				run.changeStateError(msg);
				return false;				
			}
			
			Date fechaDesde = datum.getDate(COLSFECHAVIG);
			if (fechaDesde == null) {
				String msg = "Proceso de Novedad, Alta de ObjImp: fechaDesde, posee formato invalido. " + datum.getCols(COLSFECHAVIG);
				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
			
			Date fechaHasta = null;

			// Recuperamos el Definition para Parcela
			TipObjImpDefinition tipObjImpDefinition = tipObjImp.getDefinitionForInterface();
			TipObjImpAtrDefinition atrDefinition;

			// Por cada atributo seteamos su valor en el Definition
			for(int columna = COLSINICIOATR ; columna <= COLSFINATR; columna++){
				atrDefinition = tipObjImpDefinition.getTipObjImpAtrDefinitionByColumna(columna); 
				if(atrDefinition != null){
					String valor = datum.getCols(columna);
					// Cuando estamos en la columna Catastral, la formateamos completando con ceros a la izquierda
					if(columna == COLSCLAVEFUNCIONAL4PARCELA){
						if(!"".equals(valor)){
							Datum datumCata = Datum.parseAtChar(valor, '/');
							String[] catastral = new String[datumCata.getColNumMax()]; 
							catastral[0] = StringUtil.completarCerosIzq(datumCata.getCols(0), 2);
							for(int i=1;i<datumCata.getColNumMax();i++)
								catastral[i] = StringUtil.completarCerosIzq(datumCata.getCols(i), 3);
							valor = "";
							for(int i=0;i<datumCata.getColNumMax()-1;i++)
								valor += catastral[i]+"/";		
							valor += catastral[datumCata.getColNumMax()-1];
						}
					}
					atrDefinition.addValor(valor, fechaNovedad, fechaDesde, fechaHasta);
				}
			}

			// Validamos el Definition
			tipObjImpDefinition.validate();
			if(tipObjImpDefinition.hasError()){
				String msg = "Proceso de Novedad, Alta de ObjImp: Se encontraron errores en atributos para el ObjImp " 
						+"con TipObjImp="+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVE);
				String msgErr = msg;
				for(DemodaStringMsg item: tipObjImpDefinition.getListError()){
					msgErr += "--> "+item.key()+": "+(String) item.params()[0] + "\n";
				}

				run.logError(msgErr);
				run.changeStateError(msg);
				return false;
			}
			
			// Validamos el Domicilio de Envio.
			String domicilioToParse = tipObjImpDefinition.getTipObjImpAtrDefinitionByCodigo("DomicilioEnvio").getValorString();
			if(!Domicilio.isValidToParse(domicilioToParse)){
				String msg = "Proceso de Novedad, Alta de ObjImp: El ObjImp " 
						+"con TipObjImp="+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVE)
						+" tiene el domicilio de envio con formato incorrecto. Valor Recibido: "+domicilioToParse;

				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
		
			// Creamos el ObjImp y sus atributos.
			objImp = new ObjImp();
			objImp.setTipObjImp(tipObjImp);
			objImp.setClave(clave);
			objImp.setClaveFuncional(tipObjImpDefinition.getValClaveFunc());//claveFuncional);
			objImp.setFechaAlta(datum.getDate(COLSFECHAVIG));
			objImp.setEstado(Estado.ACTIVO.getId());

			objImp = PadObjetoImponibleManager.getInstance().createObjImp(objImp, tipObjImpDefinition);
			if (objImp.hasError()) {
	           	String msg = "Proceso de Novedad, Alta el ObjImp, No se pudo dar de Alta Cuenta. (ver log)";
	           	String msgErr = msg + objImp.infoString() + "\n";
	           	msgErr += objImp.errorString();
	           	run.logError(msgErr);
	           	run.changeStateError(msg);				
	           	return false;
			}
			
			String atrTipoParcela = datum.getCols(COLSTIPOPARCELA4PARCELA); 
			//String atrTipoParcela = tipObjImpDefinition.getTipObjImpAtrDefinitionByCodigo("TipoParcela").getAtrValDefinitionVigente().getValor();

			// Si el Atributo TipoParcela es igual a 2 (Baldio)
			if("2".equals(atrTipoParcela)){
				// Obtengo la Cuenta Principal creada para el objImp dado de alta.
				Cuenta cuenta = objImp.getCuentaPrincipalActiva();
				if(cuenta!=null){
				  	// Busco la Zona de la parcela obteniendo la seccion de la catastral y luego la zona a la cual pertenece.
					Zona zona = null;
		    		String catastral = objImp.getClaveFuncional();
		    		String idseccion = StringUtil.substringHasta(catastral,'/');
		    		Seccion seccion = Seccion.getById(new Long(idseccion));
		    		zona = seccion.getZona();

		    		// Busco un broche que corresponda a un repartidor fuera de zona para el domicilio de envio enviado.
		    		Broche broche = PadDistribucionManager.getInstance().obtenerRepartidorFueraDeZona
		    			(cuenta.getDomicilioEnvio(), zona);

		    		// Si se encuentra el broche se asigna a la cuenta
		    		if (broche != null) {
		    			cuenta.asignarBroche(broche, fechaDesde, null);
		    		}

		    		// Si no encuentra broche crea una solicitud de asignacion de broche a cuenta
		    		if (broche == null) {
		    			String descripcion = "Alta de Parcela de Tipo Baldio, Nro cuenta: " +  cuenta.getNumeroCuenta() + 
		    							 	", Zona parcela: " + zona.getDescripcion() + 
		    							 	", Domicilio Envio: " + cuenta.getDomicilioEnvio().getViewDomicilio(); 
		    		
		    			CasSolicitudManager.getInstance().createSolicitud
		    				(TipoSolicitud.COD_ASIGNACION_BROCHE_CUENTA, 
		    				"No se encontro broche para asignar a la cuenta", 
		    				descripcion, cuenta, Area.COD_CATASTRO);
		    		}
		    		
				}
			}
			
			String codresptrib = datum.getCols(COLSCRT4PARCELA);
			Date fechaDesdeCRT = null;
			Date fechaHastaCRT = null;
			// Si el crt==2 o crt==3 creamos exencion.
			if("2".equals(codresptrib) || "3".equals(codresptrib)){
				Exencion exencion = null;
				if("2".equals(codresptrib))
					exencion = Exencion.getByCogido(Exencion.COD_EXENCION_QUITA_SOBRETASA);
				if("3".equals(codresptrib))
					exencion = Exencion.getByCogido(Exencion.COD_EXENCION_EXENTO_TOTAL);
				CueExe cueExe = new CueExe();
				cueExe.setCuenta(objImp.getCuentaPrincipalActiva());
				cueExe.setExencion(exencion);
				if(!StringUtil.isNullOrEmpty(datum.getCols(COLSCRTFECHADESDE4PARCELA)))
		    		fechaDesdeCRT = datum.getDate(COLSCRTFECHADESDE4PARCELA);
		    	if(!StringUtil.isNullOrEmpty(datum.getCols(COLSCRTFECHAHASTA4PARCELA)))
		    		fechaHastaCRT = datum.getDate(COLSCRTFECHAHASTA4PARCELA);
		    	
		    	if(fechaDesdeCRT!=null)
		    		cueExe.setFechaDesde(fechaDesdeCRT);
		    	else
		    		cueExe.setFechaDesde(fechaDesde);
		    	cueExe.setFechaHasta(fechaHastaCRT); 
		    	cueExe.setEstadoCueExe(EstadoCueExe.getById(EstadoCueExe.ID_HA_LUGAR));
		    	cueExe.setTipoSujeto(TipoSujeto.getById(TipoSujeto.ID_NOVEDADES));
		    	cueExe.setObservaciones("Novedades de Catastro, Alta de ObjImp: Codigo Responsabilidad Tributaria = "+codresptrib);
		    	
		    	cueExe = ExeExencionManager.getInstance().createCueExe(cueExe);
		    	
		    	// Se crea Historico de Estado CueExe
				HisEstCueExe hisEstCueExe = new HisEstCueExe();
				hisEstCueExe.setCueExe(cueExe); 
				hisEstCueExe.setEstadoCueExe(cueExe.getEstadoCueExe());
				hisEstCueExe.setObservaciones("Novedades de Catastro, Alta de ObjImp: Codigo Responsabilidad Tributaria = "+codresptrib);
				hisEstCueExe.setLogCambios("Alta de Exencion.");
				hisEstCueExe.setFecha(fechaDesde);
				ExeDAOFactory.getHisEstCueExeDAO().update(hisEstCueExe);
			}
		
				String atrUsoParcela = datum.getCols(COLSUSOPARCELA4PARCELA); 

			// Si el Atributo UsoParcela es informado
			if("8".equals(atrUsoParcela) || "9".equals(atrUsoParcela)){
				// Obtengo la Cuenta Principal creada para el objImp.
				Cuenta cuenta = objImp.getCuentaPrincipalActiva();
				if(cuenta!=null) {
					//TODO: buscar descripcion del valor de dominio
					String descripcion = "Novedades de Catastro, Alta de ObjImp: Uso de Parcela = "+atrUsoParcela+". Nro cuenta: " +  cuenta.getNumeroCuenta(); 		    			
            		CasSolicitudManager.getInstance().createSolicitud(TipoSolicitud.COD_VERIFICAR_EXENCION_CUENTA
							,"Novedades de Catastro, Alta de ObjImp.", descripcion,cuenta, Area.COD_CATASTRO);
				}
			}
			
			
			
			if (objImp.hasError()) {
	           	String msg = "Proceso de Novedad, Alta el ObjImp, No se pudo dar de Alta. (ver log)";
	           	String msgErr = msg + objImp.infoString() + "\n";
	           	msgErr += objImp.errorString();
	           	run.logError(msgErr);
	           	run.changeStateError(msg);	
	           	return false;
			}
		} catch (Exception e) {
			String msg = "Proceso de Novedad, Alta de ObjImp: Error Grave: " + e.toString();
           	run.changeStateError(msg, e);			
			return false;
		}
		return true;
	}
	
	public boolean bajaObjImp(AdpRun run, Datum datum) throws Exception {
		try {
			// Recuperamos el Tipo de Objeto Imponible Parcela
			TipObjImp tipObjImp = TipObjImp.getByCodigo(datum.getCols(COLSCODTIPOBJIMP));
			if (tipObjImp == null) {
	           	String msg = "Proceso de Novedad, Baja ObjImp, Codigo de Tipo de Objeto Imponible desconocido: " + datum.getCols(COLSCODTIPOBJIMP);
	           	run.logError(msg);
	           	run.changeStateError(msg);
	           	return false;
			}
		
			// Verificamos que el ObjImp exista en la db.
			ObjImp objImp = null;
			objImp = ObjImp.getByTipObjImpYNroCta(tipObjImp.getId(), datum.getCols(COLSCLAVE));
			if(objImp == null){
				String msg = "Proceso de Novedad, Baja de ObjImp: El ObjImp con TipObjImp="
						+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVE)
						+" no existe en la db.";
				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
			// Damos de Baja al ObjImp
			Date fechaHasta = datum.getDate(COLSFECHAVIG);
			if (fechaHasta == null) {
				String msg = "Proceso de Novedad, Baja de ObjImp: fechaHasta, posee formato invalido. " + datum.getCols(COLSFECHAVIG);
				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
			objImp.desactivar(fechaHasta);
			
			if (objImp.hasError()) {
	           	String msg = "Proceso de Novedad, Baja de ObjImp, No se pudo dar la baja. (ver log)";
	           	String msgErr = msg + objImp.infoString() + "\n" + objImp.errorString();
	           	run.logError(msgErr);
	           	run.changeStateError(msg);	
	           	return false;
			}			
		} catch (Exception e) {
			String msg = "Proceso de Novedad, Baja de ObjImp: Error Grave: " + e.toString();
           	run.changeStateError(msg, e);			
			return false;
		}
		return true;
	}

	public boolean modifObjImp(AdpRun run, Datum datum) throws Exception {
		String codTipObjImp = datum.getCols(COLSCODTIPOBJIMP);
		if ("PARCELA".equals(codTipObjImp)) { return this.modifParcela(run, datum);}
		if ("COMERCIO".equals(codTipObjImp)) { return this.modifComercio(run, datum);}
		if ("CEMENTERIO".equals(codTipObjImp)) { return this.modifSepultura(run, datum);}

      	String msg = "Proceso de Novedad, Modificacion ObjImp, Codigo de Tipo de Objeto Imponible desconocido: " + datum.getCols(COLSCODTIPOBJIMP);
       	run.logError(msg);
       	run.changeStateError(msg);
       	return false;
	}
	
	
	public boolean correctObjImp(AdpRun run, Datum datum) throws Exception {
		String codTipObjImp = datum.getCols(COLSCODTIPOBJIMP);
		if ("COMERCIO".equals(codTipObjImp)) { return this.correctComercio(run, datum);}

      	String msg = "Proceso de Novedad, Correccion ObjImp, Codigo de Tipo de Objeto Imponible desconocido: " + datum.getCols(COLSCODTIPOBJIMP);
       	run.logError(msg);
       	run.changeStateError(msg);
       	return false;
	}
	
	public boolean modifParcela(AdpRun run, Datum datum) throws Exception {
		try {
			String asuSolicitud = "Modificaciones de Atributos del Objeto Imponible con Catastral: ";
			String desSolicitud = "Modificaciones con efecto retroactivo. Existe emisión posterior a la fecha de vigencia de los nuevos valores. Nuevos Valores: ";
			
			// Recuperamos el Tipo de Objeto Imponible Parcela
			TipObjImp tipObjImp = TipObjImp.getByCodigo(datum.getCols(COLSCODTIPOBJIMP));
			if (tipObjImp == null) {
	           	String msg = "Proceso de Novedad, Modificacion ObjImp, Codigo de Tipo de Objeto Imponible desconocido: " + datum.getCols(COLSCODTIPOBJIMP);
	           	run.logError(msg);
	           	run.changeStateError(msg);
	           	return false;
			}
		
			// Obtenemos que el ObjImp de la db.
			ObjImp objImp = null;
			objImp = ObjImp.getByTipObjImpYNroCta(tipObjImp.getId(), datum.getCols(COLSCLAVE));
			if(objImp == null){
				String msg = "Proceso de Novedad, Modificacion de ObjImp: El ObjImp con TipObjImp="
						+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVE)
						+" no existe en la db.";

				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
		
			// Recuperamos algunos datos generales para ObjImp y sus atributos.
			Date fechaNovedad = datum.getDate(COLSFECHAACCION);
			if (fechaNovedad == null) {
				String msg = "Proceso de Novedad, Alta de ObjImp: fechaNovedad (alias fechaAccion), posee formato invalido. " + datum.getCols(COLSFECHAACCION);
				run.logError(msg);
				run.changeStateError(msg);
				return false;				
			}
			
			Date fechaDesde = datum.getDate(COLSFECHAVIG);
			if (fechaDesde == null) {
				String msg = "Proceso de Novedad, Alta de ObjImp: fechaDesde, posee formato invalido. " + datum.getCols(COLSFECHAVIG);
				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}

			
			Date fechaHasta = null;
		
			asuSolicitud += objImp.getClaveFuncional();
			
			// Recuperamos el Definition para Parcela
			TipObjImpDefinition tipObjImpDefinition = tipObjImp.getDefinitionForInterface();
			TipObjImpAtrDefinition atrDefinition;
			
			// Por cada atributo ,si el valor de la columna es distinto de "" o " ", lo seteamos en el Definition
			for(int columna = COLSINICIOATR ; columna <= COLSFINATR; columna++){
				if(!(datum.getCols(columna).equals("") || datum.getCols(columna).equals(" "))){
					atrDefinition = tipObjImpDefinition.getTipObjImpAtrDefinitionByColumna(columna);
					if(atrDefinition != null){
						String valor = datum.getCols(columna);
						// Cuando estamos en la columna Catastral, la formateamos completando con ceros a la izquierda
						if(columna == COLSCLAVEFUNCIONAL4PARCELA){
							Datum datumCata = Datum.parseAtChar(valor, '/');
							String[] catastral = new String[datumCata.getColNumMax()]; 
							catastral[0] = StringUtil.completarCerosIzq(datumCata.getCols(0), 2);
							for(int i=1;i<datumCata.getColNumMax();i++)
								catastral[i] = StringUtil.completarCerosIzq(datumCata.getCols(i), 3);
							valor = "";
							for(int i=0;i<datumCata.getColNumMax()-1;i++)
								valor += catastral[i]+"/";		
							valor += catastral[datumCata.getColNumMax()-1];
						}
						
						atrDefinition.addValor(valor, fechaNovedad, fechaDesde, fechaHasta);

						desSolicitud += atrDefinition.getAtributo().getDesAtributo()+" = ";
						desSolicitud += datum.getCols(columna)+" ";
					}
				}else{
					atrDefinition = tipObjImpDefinition.getTipObjImpAtrDefinitionByColumna(columna);
					if(atrDefinition != null){
						tipObjImpDefinition.removeTipObjImpAtrDefinition(atrDefinition);						
					}
				}
			}
			desSolicitud += "Fecha Novedad: "+datum.getCols(COLSFECHAACCION);
			desSolicitud += " Fecha Desde de los Atributos: "+datum.getCols(COLSFECHAVIG);
			// Sacamos los atributos que no se cargan por novedad del Definition. (Broche con id=50, QuitaFecha con id=51, NroCuenta con id=52)
			atrDefinition = tipObjImpDefinition.getTipObjImpAtrDefinitionById(50L);
			if(atrDefinition != null){
				tipObjImpDefinition.removeTipObjImpAtrDefinition(atrDefinition);						
			}
			atrDefinition = tipObjImpDefinition.getTipObjImpAtrDefinitionById(51L);
			if(atrDefinition != null){
				tipObjImpDefinition.removeTipObjImpAtrDefinition(atrDefinition);						
			}
			atrDefinition = tipObjImpDefinition.getTipObjImpAtrDefinitionById(52L);
			if(atrDefinition != null){
				tipObjImpDefinition.removeTipObjImpAtrDefinition(atrDefinition);						
			}

			
			// Validamos el Definition
			tipObjImpDefinition.validate();
			if(tipObjImpDefinition.hasError()){
				String msg = "Proceso de Novedad, Modificacion de ObjImp: Se encontraron errores en atributos para el ObjImp " 
					+ "con TipObjImp="+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVE);
				String msgErr = msg + "\n";
				for(DemodaStringMsg item: tipObjImpDefinition.getListError()){
					msgErr += "--> "+item.key()+": "+(String) item.params()[0] + "\n";
				}

				run.logError(msgErr);
				run.changeStateError(msg);
				return false;
			}

			//antes moficicar obtenemos el viejo valor de tipo de parcela:
			String nuevoTipoParcela = datum.getCols(COLSTIPOPARCELA4PARCELA);
			TipObjImpDefinition oldDefinition = objImp.getDefinitionValue();			
			String oldTipoParcela = oldDefinition.getTipObjImpAtrDefinitionByCodigo("TipoParcela").getValorString();
			log.info(String.format("Cambia tipoparcela? viejo:'%s' -> nuevo:'%s'", oldTipoParcela, nuevoTipoParcela));
			
			// Modificamos los atributos del ObjImp
            tipObjImpDefinition = objImp.updateObjImpAtrDefinition(tipObjImpDefinition);
            
			//- Mantis 8119: Error en el procesamiento de novedades de catastro
            String newCatastral = tipObjImpDefinition.getValClaveFunc();
            if(!StringUtil.isNullOrEmpty(newCatastral)){
            	objImp.setClaveFuncional(tipObjImpDefinition.getValClaveFunc());
            	PadDAOFactory.getObjImpDAO().update(objImp);
            }

    		// Verificamos si viene informado un cambio de Domicilio de Envio.
			TipObjImpAtrDefinition atributoDomicilioEnvio =tipObjImpDefinition.getTipObjImpAtrDefinitionByCodigo("DomicilioEnvio");
            if(atributoDomicilioEnvio != null){
    			String domicilioToParse = atributoDomicilioEnvio.getValorString();
    			if(!Domicilio.isValidToParse(domicilioToParse)){
    				String msg = "Proceso de Novedad, Modificacion de ObjImp: El ObjImp " 
    						+"con TipObjImp="+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVE)
    						+" tiene el domicilio de envio con formato incorrecto. Valor Recibido: "+domicilioToParse;

    				run.logError(msg);
    				run.changeStateError(msg);
    				return false;
    			}
            	// Domicilio de Envio
    			Domicilio domicilioEnvio = Domicilio.valueOf(domicilioToParse);
				domicilioEnvio = PadDomicilioManager.getInstance().createDomicilio(domicilioEnvio);					
				if(domicilioEnvio.hasError()){
					String msg = "Proceso de Novedad, Modificacion de ObjImp: El ObjImp " 
						+"con TipObjImp="+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVE)
						+" no pudo crear el nuevo domicilio de envio.";			
					run.logError(msg);
					run.changeStateError(msg);
					return false;   						
				}   					
   				Cuenta cuenta = objImp.getCuentaPrincipalActiva();
				cuenta.cambiarDomicilioEnvio(domicilioEnvio);
            }
			
            //Determinar si el cambio tiene un efecto retroactivo que produzca la necesidad de marcar las cuentas.
            for(Cuenta cuenta: objImp.getListCuentaActiva()){
            	if(cuenta.getRecurso().existeEmisionPosterior(fechaDesde)){
            		CasSolicitudManager.getInstance().createSolicitud(TipoSolicitud.COD_MODIFICACION_DATOS
            														,asuSolicitud,desSolicitud,cuenta, Area.COD_CATASTRO);
            	}
            }
            
			String codresptrib = datum.getCols(COLSCRT4PARCELA);
			Date fechaDesdeCRT = null;
			Date fechaHastaCRT = null;
			// Si el crt==2 o crt==3 creamos una solicitud de verificar exencion.
			if("2".equals(codresptrib) || "3".equals(codresptrib)){
				if(!StringUtil.isNullOrEmpty(datum.getCols(COLSCRTFECHADESDE4PARCELA)))
		    		fechaDesdeCRT = datum.getDate(COLSCRTFECHADESDE4PARCELA);
		    	if(!StringUtil.isNullOrEmpty(datum.getCols(COLSCRTFECHAHASTA4PARCELA)))
		    		fechaHastaCRT = datum.getDate(COLSCRTFECHAHASTA4PARCELA);
		   
		    	desSolicitud = "Cambio de valor del atributo CRT, nuevo valor = "+codresptrib;
		    	desSolicitud+= " Fecha Desde CRT="+DateUtil.formatDate(fechaDesdeCRT,DateUtil.ddSMMSYYYY_MASK)+" Fecha Hasta CRT="+DateUtil.formatDate(fechaHastaCRT,DateUtil.ddSMMSYYYY_MASK);
		    	// Creamos una solicitud de verificar exencion para la cuenta principal.
    			CasSolicitudManager.getInstance().createSolicitud(TipoSolicitud.COD_VERIFICAR_EXENCION_CUENTA
    					,asuSolicitud,desSolicitud,objImp.getCuentaPrincipalActiva(), Area.COD_CATASTRO);    	
			}
    			
			//String atrTipoParcela = datum.getCols(COLSTIPOPARCELA4PARCELA); 
			//String atrTipoParcela = tipObjImpDefinition.getTipObjImpAtrDefinitionByCodigo("TipoParcela").getAtrValDefinitionVigente().getValor();

			// Si el Atributo TipoParcela es informado
			if(!StringUtil.isNullOrEmpty(nuevoTipoParcela)){
				// Obtengo la Cuenta Principal creada para el objImp.
				Cuenta cuenta = objImp.getCuentaPrincipalActiva();
				if(cuenta != null) {
					// cambio realizado por bug:668  16-mar-2009 - fedel
					// Verficamos si hubo cambio de TipoParcela de Baldio(2) a Finca(1)
					// Y en tal caso damos de baja las exenciones quita de sobretasa con 
					// fechaHasta a la fecha indicada en la novedad
					if (oldTipoParcela.equals("2") && nuevoTipoParcela.equals("1")) {
						List<CueExe> listCueExe = cuenta.getListCueExeVigente(fechaDesde);
						for(CueExe cueExe : listCueExe) {
							if (Exencion.ID_EXENCION_QUITA_SOBRETASA.equals(cueExe.getExencion().getId())) {
								cueExe.setFechaHasta(fechaDesde);
								ExeDAOFactory.getExencionDAO().update(cueExe);
							}
						}
					}
					/*
					String descripcion = "Modificacion de Caracter de Parcela (Finca / Baldio). Nro cuenta: " +  cuenta.getNumeroCuenta(); 		    			
            		CasSolicitudManager.getInstance().createSolicitud(TipoSolicitud.COD_MODIFICACION_DATOS
							,asuSolicitud, descripcion,cuenta, Area.COD_CATASTRO);
					*/
				}
			}

			String atrUsoParcela = datum.getCols(COLSUSOPARCELA4PARCELA); 

			// Si el Atributo UsoParcela es informado
			if(!StringUtil.isNullOrEmpty(atrUsoParcela)){
				// Obtengo la Cuenta Principal creada para el objImp.
				Cuenta cuenta = objImp.getCuentaPrincipalActiva();
				if(cuenta!=null) {
					//TODO: buscar descripcion del valor de dominio
					String descripcion = "Modificacion de Uso de la Parcela (Cochera / Baulera). Nro cuenta: " +  cuenta.getNumeroCuenta(); 		    			
            		CasSolicitudManager.getInstance().createSolicitud(TipoSolicitud.COD_VERIFICAR_EXENCION_CUENTA
							,asuSolicitud, descripcion,cuenta, Area.COD_CATASTRO);
				}
			}
			  			 		
			
			
            if (tipObjImpDefinition.hasError()) {
	           	String msg = "Proceso de Novedad, Modificacion de ObjImp, No se pudo modificar. (ver log)";
	           	String msgErr = msg + objImp.infoString() + "\n" + objImp.errorString();
	           	run.logError(msgErr);
	           	run.changeStateError(msg);	
	           	return false;
			}
		} catch (Exception e) {
			String msg = "Proceso de Novedad, Modificacion de ObjImp: Error Grave: " + e.toString();
           	run.changeStateError(msg, e);			
			return false;
		}	
		return true;
	}


	public boolean altaComercio(AdpRun run, Datum datum) throws Exception {
		try {
			
			// Recuperamos el Tipo de Objeto Imponible Comercio
			TipObjImp tipObjImp = TipObjImp.getByCodigo(datum.getCols(COLSCODTIPOBJIMP));
			if (tipObjImp == null) {
	           	String msg = "Proceso de Novedad, Alta el ObjImp, Codigo de Tipo de Objeto Imponible desconocido: " + datum.getCols(COLSCODTIPOBJIMP);
	           	run.logError(msg);
	           	run.changeStateError(msg);
	           	return false;
			}
	
			// Verificamos que el ObjImp no exista en la db.
			ObjImp objImp = null;
			objImp = ObjImp.getByTipObjImpYNroCta(tipObjImp.getId(), datum.getCols(COLSCLAVE));
			if(objImp != null){
				String msg = "Proceso de Novedad, Alta de ObjImp: El ObjImp " 
						+"con TipObjImp="+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVE)
						+" existe en la db.";
				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
			// Recuperamos algunos datos generales para ObjImp y sus atributos.
			String clave = datum.getCols(COLSCLAVE);

			Date fechaNovedad = datum.getDate(COLSFECHAACCION);
			if (fechaNovedad == null) {
				String msg = "Proceso de Novedad, Alta de ObjImp: fechaNovedad (alias fechaAccion), posee formato invalido. " + datum.getCols(COLSFECHAACCION);
				run.logError(msg);
				run.changeStateError(msg);
				return false;				
			}
			
			Date fechaDesde = datum.getDate(COLSFECHAVIG);
			if (fechaDesde == null) {
				String msg = "Proceso de Novedad, Alta de ObjImp: fechaDesde, posee formato invalido. " + datum.getCols(COLSFECHAVIG);
				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
			
			Date fechaHasta = null;

			// Recuperamos el Definition para Parcela
			TipObjImpDefinition tipObjImpDefinition = tipObjImp.getDefinitionForInterface();
	
			// Por cada atributo seteamos su valor en el Definition	
			for(TipObjImpAtrDefinition atrDefinition: tipObjImpDefinition.getListTipObjImpAtrDefinition()){
				// Si no es multivalor simplemente buscamos el valor en la columna indicada en el campo PosColInt
				// del TipObjImpAtr y la seteamos en el Definition
				if(!atrDefinition.getEsMultivalor()){
					int columna = atrDefinition.getTipObjImpAtr().getPosColInt();
					String valor = datum.getCols(columna);
					// Cuando estamos en la columna Catastral, la formateamos completando con ceros a la izquierda
					if("Catastral".equals(atrDefinition.getTipObjImpAtr().getAtributo().getCodAtributo())){
						if(!"".equals(valor)){
							Datum datumCata = Datum.parseAtChar(valor, '/');
							String[] catastral = new String[datumCata.getColNumMax()]; 
							catastral[0] = StringUtil.completarCerosIzq(datumCata.getCols(0), 2);
							for(int i=1;i<datumCata.getColNumMax();i++)
								catastral[i] = StringUtil.completarCerosIzq(datumCata.getCols(i), 3);
							valor = "";
							for(int i=0;i<datumCata.getColNumMax()-1;i++)
								valor += catastral[i]+"/";		
							valor += catastral[datumCata.getColNumMax()-1];
						}
					}	
					atrDefinition.addValor(valor, fechaNovedad, fechaDesde, fechaHasta);					
				// Si es multivalor recorremos los valores entre las columnas indicadas en los campos PosColInt
				// y PosColHas del TipObjImpAtr y los seteamos en el Definition
				}else{
					int colInt = atrDefinition.getTipObjImpAtr().getPosColInt();
					int colHas = atrDefinition.getTipObjImpAtr().getPosColIntHas();
					for(int columna=colInt; columna<=colHas; columna++){
						String multiValor = datum.getCols(columna);
						if(!"".equals(multiValor)){
							Datum datumMulti = Datum.parseAtChar(multiValor, '$');
							log.debug("MULTIVALOR............ ");
							log.debug("COLUMNA: "+columna+ ", COLINT: "+colInt+", COLHASTA: "+colHas);
							log.debug("CODIGO: "+datumMulti.getCols(0));
							log.debug("FECHA DESDE: "+datumMulti.getCols(1));
							String valor = datumMulti.getCols(0); 
							Date fechaDesdeMulti = datumMulti.getDate(1);
							Date fechaHastaMulti = null;
							// soportamos rango de vigencia abierto
							if (datumMulti.getColNumMax() > 1){
								log.debug("FECHA HASTA: "+ datumMulti.getDate(2));
								fechaHastaMulti = datumMulti.getDate(2);
							}
							
							atrDefinition.addValor(valor, fechaNovedad, fechaDesdeMulti, fechaHastaMulti);
						}
					}
					
				}
			}

			// Validamos el Definition
			tipObjImpDefinition.validate();
			if(tipObjImpDefinition.hasError()){
				String msg = "Proceso de Novedad, Alta de ObjImp: Se encontraron errores en atributos para el ObjImp " 
						+"con TipObjImp="+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVE);
				String msgErr = msg;
				for(DemodaStringMsg item: tipObjImpDefinition.getListError()){
					msgErr += "--> "+item.key()+": "+(String) item.params()[0] + "\n";
				}

				run.logError(msgErr);
				run.changeStateError(msg);
				return false;
			}
			
			// Validamos el Domicilio de Envio.
			String domicilioToParse = tipObjImpDefinition.getTipObjImpAtrDefinitionByCodigo("DomicilioFinca").getValorString();
			if(!Domicilio.isValidToParse(domicilioToParse)){
				String msg = "Proceso de Novedad, Alta de ObjImp: El ObjImp " 
						+"con TipObjImp="+tipObjImp.getDesTipObjImp()+" y Clave="+clave
						+" tiene el domicilio de envio con formato incorrecto. Valor Recibido: "+domicilioToParse;

				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
		
			// Creamos el ObjImp y sus atributos.
			objImp = new ObjImp();
			objImp.setTipObjImp(tipObjImp);
			objImp.setClave(clave);
			objImp.setClaveFuncional(tipObjImpDefinition.getValClaveFunc());
			objImp.setFechaAlta(fechaDesde);
			objImp.setEstado(Estado.ACTIVO.getId());
			
			// Crea el ObjImp, sus atributos, y si corresponde crea Cuentas secundarias 
			objImp = PadObjetoImponibleManager.getInstance().createObjImp(objImp, tipObjImpDefinition);
			if (objImp.hasError()) {
	           	String msg = "Proceso de Novedad, Alta el ObjImp, No se pudo dar de Alta Cuenta. (ver log)";
	           	String msgErr = msg + objImp.infoString() + "\n";
	           	msgErr += objImp.errorString();
	           	run.logError(msgErr);
	           	run.changeStateError(msg);				
	           	return false;
			}
						
		} catch (Exception e) {
			String msg = "Proceso de Novedad, Alta de ObjImp: Error Grave: " + e.toString();
           	e.printStackTrace();
			run.changeStateError(msg, e);			
			return false;
		}
		return true;
	}


	public boolean modifComercio(AdpRun run, Datum datum) throws Exception {
		try {
			String asuSolicitud = "Modificaciones de Atributos del Objeto Imponible con Clave Funcional: ";
			String desSolicitud = "Modificaciones con efecto retroactivo. Existe emisión posterior a la fecha de vigencia de los nuevos valores. Nuevos Valores: ";
			
			// Recuperamos el Tipo de Objeto Imponible Comercio
			TipObjImp tipObjImp = TipObjImp.getByCodigo(datum.getCols(COLSCODTIPOBJIMP));
			if (tipObjImp == null) {
	           	String msg = "Proceso de Novedad, Modificacion ObjImp, Codigo de Tipo de Objeto Imponible desconocido: " + datum.getCols(COLSCODTIPOBJIMP);
	           	run.logError(msg);
	           	run.changeStateError(msg);
	           	return false;
			}
		
			
			// Obtenemos que el ObjImp de la db.
			ObjImp objImp = null;
			objImp = ObjImp.getByTipObjImpYNroCta(tipObjImp.getId(), datum.getCols(COLSCLAVE));
			
			//Obtenemos la cuenta
			Cuenta cuentaPri = objImp.getCuentaPrincipal();
			if(objImp == null){
				String msg = "Proceso de Novedad, Modificacion de ObjImp: El ObjImp con TipObjImp="
						+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVE)
						+" no existe en la db.";

				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
		
			// Recuperamos algunos datos generales para ObjImp y sus atributos.
			Date fechaNovedad = datum.getDate(COLSFECHAACCION);
			if (fechaNovedad == null) {
				String msg = "Proceso de Novedad, Modificacion de ObjImp: fechaNovedad (alias fechaAccion), posee formato invalido. " + datum.getCols(COLSFECHAACCION);
				run.logError(msg);
				run.changeStateError(msg);
				return false;				
			}
			
			Date fechaDesde = datum.getDate(COLSFECHAVIG);
			if (fechaDesde == null) {
				String msg = "Proceso de Novedad, Modificacion de ObjImp: fechaDesde, posee formato invalido. " + datum.getCols(COLSFECHAVIG);
				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}

			
			Date fechaHasta = null;
		
			asuSolicitud += objImp.getClaveFuncional();
			
			// Recuperamos el Definition para Comercio
			TipObjImpDefinition tipObjImpDefinition = tipObjImp.getDefinitionForInterface();

			
			List<TipObjImpAtrDefinition> listToRemove = new ArrayList<TipObjImpAtrDefinition>();
			
			// Por cada atributo seteamos su valor en el Definition	
			for(TipObjImpAtrDefinition atrDefinition: tipObjImpDefinition.getListTipObjImpAtrDefinition()){
				// Si no es multivalor simplemente buscamos el valor en la columna indicada en el campo PosColInt
				// del TipObjImpAtr y la seteamos en el Definition
				if(!atrDefinition.getEsMultivalor()){
					int columna = atrDefinition.getTipObjImpAtr().getPosColInt();
					String valor = datum.getCols(columna);
					if(!(valor.equals("") || valor.equals(" "))){
						// Cuando estamos en la columna Catastral, la formateamos completando con ceros a la izquierda
						if("Catastral".equals(atrDefinition.getTipObjImpAtr().getAtributo().getCodAtributo())){
							if(!"".equals(valor)){
								Datum datumCata = Datum.parseAtChar(valor, '/');
								String[] catastral = new String[datumCata.getColNumMax()]; 
								catastral[0] = StringUtil.completarCerosIzq(datumCata.getCols(0), 2);
								for(int i=1;i<datumCata.getColNumMax();i++)
									catastral[i] = StringUtil.completarCerosIzq(datumCata.getCols(i), 3);
								valor = "";
								for(int i=0;i<datumCata.getColNumMax()-1;i++)
									valor += catastral[i]+"/";		
								valor += catastral[datumCata.getColNumMax()-1];
							}
						}	
						
						if("DomicilioFinca".equals(atrDefinition.getTipObjImpAtr().getAtributo().getCodAtributo())){
							cuentaPri.updateDomicilioEnvioForComercio(valor);
							if (cuentaPri.hasError()){
								String msg = "Proceso de Novedad, Modificacion de ObjImp: Se encontraron errores en atributos para el ObjImp " 
									+ "con TipObjImp="+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVE);
								String msgErr = msg + "\n";
								for(DemodaStringMsg item: tipObjImpDefinition.getListError()){
									msgErr += "--> "+item.key()+": "+(String) item.params()[0] + "\n";
								}

								run.logError(msgErr);
								run.changeStateError(msg);
								return false;
							}
						}
						atrDefinition.addValor(valor, fechaNovedad, fechaDesde, fechaHasta);
						desSolicitud += atrDefinition.getAtributo().getDesAtributo()+" = ";
						desSolicitud += valor+" ";
					}else{
						listToRemove.add(atrDefinition);
					}
				// Si es multivalor recorremos los valores entre las columnas indicadas en los campos PosColInt
				// y PosColHas del TipObjImpAtr y los seteamos en el Definition
				}else{
					int colInt = atrDefinition.getTipObjImpAtr().getPosColInt();
					int colHas = atrDefinition.getTipObjImpAtr().getPosColIntHas();
					
					for(int columna=colInt; columna<=colHas; columna++){
						String multiValor = datum.getCols(columna);
						if(!"".equals(multiValor)){
							Datum datumMulti = Datum.parseAtChar(multiValor, '$');
							String valor = datumMulti.getCols(0).trim(); 
							Date fechaDesdeMulti = datumMulti.getDate(1);
							Date fechaHastaMulti = datumMulti.getDate(2);
							log.debug("fechaDesdeMulti: "+fechaDesdeMulti);
							log.debug("fechaHastaMulti: "+fechaHastaMulti);
							atrDefinition.addValor(valor, fechaNovedad, fechaDesdeMulti, fechaHastaMulti);
							desSolicitud += atrDefinition.getAtributo().getDesAtributo()+" = ";
							desSolicitud += valor+" ";
						}
					}
					if(ListUtil.isNullOrEmpty(atrDefinition.getMultiValor())){
						listToRemove.add(atrDefinition);
					}
				}
			}
			
			// Si existen elementos a borrar, los borramos
			for (TipObjImpAtrDefinition atrDefinition:listToRemove){
				tipObjImpDefinition.removeTipObjImpAtrDefinition(atrDefinition);
			}

			desSolicitud += " Fecha Novedad: "+datum.getCols(COLSFECHAACCION);
			desSolicitud += " Fecha Desde de los Atributos (excluyendo los multivalor): "+datum.getCols(COLSFECHAVIG);
			
			// Validamos el Definition
			tipObjImpDefinition.validate();
			if(tipObjImpDefinition.hasError()){
				String msg = "Proceso de Novedad, Modificacion de ObjImp: Se encontraron errores en atributos para el ObjImp " 
					+ "con TipObjImp="+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVE);
				String msgErr = msg + "\n";
				for(DemodaStringMsg item: tipObjImpDefinition.getListError()){
					msgErr += "--> "+item.key()+": "+(String) item.params()[0] + "\n";
				}

				run.logError(msgErr);
				run.changeStateError(msg);
				return false;
			}
			
			// Modificamos los atributos del ObjImp
            tipObjImpDefinition = objImp.updateObjImpAtrDefinition(tipObjImpDefinition);
            
            //Determinar si el cambio tiene un efecto retroactivo que produzca la necesidad de marcar las cuentas.
            for(Cuenta cuenta: objImp.getListCuentaActiva()){
            	if(cuenta.getRecurso().existeEmisionPosterior(fechaDesde)){
            		CasSolicitudManager.getInstance().createSolicitud(TipoSolicitud.COD_MODIFICACION_DATOS
            														,asuSolicitud,desSolicitud,cuenta, Area.COD_CATASTRO);
            	}
            }
            
            // si se da de alta determinado rubro (o quizá algun otro atributo) se debe crear la cuenta secundaria
            // o si se da de baja desactivarla
			SiatHibernateUtil.currentSession().evict(objImp);
			SiatHibernateUtil.currentSession().flush();
			objImp = ObjImp.getByTipObjImpYNroCta(tipObjImp.getId(), datum.getCols(COLSCLAVE));
            PadObjetoImponibleManager.getInstance().altaBajaCuentaSecComercio(objImp, fechaNovedad);
            
            // 		 Llamar a servicio que resuelva el 'Analisis de modificacion sobre Domicilio de Envio'.
            //		(Este caso no deberia ocurrir ya que la modificacion de domicilios de envio se realizara mediante
            //		 un CU de SIAT, pero queda abierta la posibilidad por las dudas.)            
            if (tipObjImpDefinition.hasError()) {
	           	String msg = "Proceso de Novedad, Modificacion de ObjImp, No se pudo modificar. (ver log)";
	           	String msgErr = msg + objImp.infoString() + "\n" + objImp.errorString();
	           	run.logError(msgErr);
	           	run.changeStateError(msg);	
	           	return false;
			}
		} catch (Exception e) {
			String msg = "Proceso de Novedad, Modificacion de ObjImp: Error Grave: " + e.toString();
           	run.changeStateError(msg, e);			
			return false;
		}	
		return true;
	}

	
	public boolean correctComercio(AdpRun run, Datum datum) throws Exception {
		
		try {
			String asuSolicitud = "Correcciones de Atributos del Objeto Imponible con Clave Funcional: ";
			String desSolicitud = "Correcciones con efecto retroactivo. Existe emisión posterior a la fecha de vigencia de los nuevos valores. Nuevos Valores: ";
			
			// Recuperamos el Tipo de Objeto Imponible Comercio
			TipObjImp tipObjImp = TipObjImp.getByCodigo(datum.getCols(COLSCODTIPOBJIMP));
			if (tipObjImp == null) {
	           	String msg = "Proceso de Novedad, Correccion ObjImp, Codigo de Tipo de Objeto Imponible desconocido: " + datum.getCols(COLSCODTIPOBJIMP);
	           	run.logError(msg);
	           	run.changeStateError(msg);
	           	return false;
			}
		
			// Obtenemos que el ObjImp de la db.
			ObjImp objImp = null;
			objImp = ObjImp.getByTipObjImpYNroCta(tipObjImp.getId(), datum.getCols(COLSCLAVE));
			if(objImp == null){
				String msg = "Proceso de Novedad, Correccion de ObjImp: El ObjImp con TipObjImp="
						+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVE)
						+" no existe en la db.";

				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
		
			// Recuperamos algunos datos generales para ObjImp y sus atributos.
			Date fechaNovedad = datum.getDate(COLSFECHAACCION);
			if (fechaNovedad == null) {
				String msg = "Proceso de Novedad, Correccion de ObjImp: fechaNovedad (alias fechaAccion), posee formato invalido. " + datum.getCols(COLSFECHAACCION);
				run.logError(msg);
				run.changeStateError(msg);
				return false;				
			}
			
			Date fechaDesde = datum.getDate(COLSFECHAVIG);
			if (fechaDesde == null) {
				String msg = "Proceso de Novedad, Correccion de ObjImp: fechaDesde, posee formato invalido. " + datum.getCols(COLSFECHAVIG);
				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
			
			Date fechaHasta = null;
		
			asuSolicitud += objImp.getClaveFuncional();
			
			// Recuperamos el Definition para Comercio, los valores vigentes a la fecha desde
			TipObjImpDefinition tipObjImpDefinitionValue = objImp.getDefinitionValue(fechaDesde);
			
			// Recuperamos el Definition para Comercio
			TipObjImpDefinition tipObjImpDefinition = tipObjImp.getDefinitionForInterface();
			
			TipObjImpAtrDefinition atrValueVigente = null;
			
			// Por cada atributo seteamos su valor en el Definition	
			for(TipObjImpAtrDefinition atrDefinition: tipObjImpDefinition.getListTipObjImpAtrDefinition()){
				
				// Si no es multivalor simplemente buscamos el valor en la columna indicada en el campo PosColInt
				// del TipObjImpAtr y la seteamos en el Definition
				if(!atrDefinition.getEsMultivalor()){
					int columna = atrDefinition.getTipObjImpAtr().getPosColInt();
					String valor = datum.getCols(columna);
					if(!StringUtil.isNullOrEmpty(valor)){
						// Cuando estamos en la columna Catastral, la formateamos completando con ceros a la izquierda
						if("Catastral".equals(atrDefinition.getTipObjImpAtr().getAtributo().getCodAtributo())){
							if(!"".equals(valor)){
								Datum datumCata = Datum.parseAtChar(valor, '/');
								String[] catastral = new String[datumCata.getColNumMax()]; 
								catastral[0] = StringUtil.completarCerosIzq(datumCata.getCols(0), 2);
								for(int i=1;i<datumCata.getColNumMax();i++)
									catastral[i] = StringUtil.completarCerosIzq(datumCata.getCols(i), 3);
								valor = "";
								for(int i=0;i<datumCata.getColNumMax()-1;i++)
									valor += catastral[i]+"/";		
								valor += catastral[datumCata.getColNumMax()-1];
							}
						}
						
						atrDefinition.reset();
						
						atrDefinition.addValor(valor, fechaNovedad, fechaDesde, fechaHasta);
						desSolicitud += atrDefinition.getAtributo().getDesAtributo()+" = ";
						desSolicitud += valor+" ";
					}

					// Si es multivalor recorremos los valores entre las columnas indicadas en los campos PosColInt
				// y PosColHas del TipObjImpAtr y los seteamos en el Definition
				}else{
					int colInt = atrDefinition.getTipObjImpAtr().getPosColInt();
					int colHas = atrDefinition.getTipObjImpAtr().getPosColIntHas();
					boolean tieneMultiValor = false; // (osea tiene rubro)
					
					// Buscamos valorizacion vigente.
					atrValueVigente = tipObjImpDefinitionValue.getTipObjImpAtrDefinitionById(atrDefinition.getTipObjImpAtr().getId());
										
					log.debug("IdTipObjImpAtr: " + atrDefinition.getTipObjImpAtr().getId());
					log.debug("colInt: " + colInt);
					log.debug("colHas: " + colHas);
					
					// Insertamos los nuevos valores	
					for(int columna=colInt; columna<=colHas; columna++){
						String multiValor = datum.getCols(columna);
						if(!StringUtil.isNullOrEmpty(multiValor)){
							tieneMultiValor = true;
							Datum datumMulti = Datum.parseAtChar(multiValor, '$');
							String valor = datumMulti.getCols(0); 
							Date fechaDesdeMulti = datumMulti.getDate(1);
							Date fechaHastaMulti = null;
							// soportamos rango de vigencia abierto
							if (datumMulti.getColNumMax() > 1){
								log.debug("FECHA HASTA: "+ datumMulti.getDate(2));
								fechaHastaMulti = datumMulti.getDate(2);
							}
							
							log.debug(" Valor: " + valor);
							
							atrDefinition.addValor(valor, fechaNovedad, fechaDesdeMulti, fechaHastaMulti);
							
							desSolicitud += atrDefinition.getAtributo().getDesAtributo()+" = ";
							desSolicitud += valor+" ";
						}
					}

					// issue 5743, antes de borrarlos verificamos que nos allan informado algun valor.
					// BORRAMOS los TipObjImpAtrVal para el TipObjImpAtr si existe valorizacion vigente
					if (atrValueVigente != null && tieneMultiValor){
						objImp.deleteObjImpAtrValById(atrDefinition.getTipObjImpAtr().getId());
					}
				}
				
			}

			desSolicitud += " Fecha Novedad: "+datum.getCols(COLSFECHAACCION);
			desSolicitud += " Fecha Desde de los Atributos (excluyendo los multivalor): "+datum.getCols(COLSFECHAVIG);
			
			// Validamos el Definition
			tipObjImpDefinition.validate();
			if(tipObjImpDefinition.hasError()){
				String msg = "Proceso de Novedad, Correccion de ObjImp: Se encontraron errores en atributos para el ObjImp " 
					+ "con TipObjImp="+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVE);
				String msgErr = msg + "\n";
				for(DemodaStringMsg item: tipObjImpDefinition.getListError()){
					msgErr += "--> "+item.key()+": "+(String) item.params()[0] + "\n";
				}

				run.logError(msgErr);
				run.changeStateError(msg);
				return false;
			}
			
			// Correccion los atributos del ObjImp
			tipObjImpDefinition = objImp.correctObjImpAtrDefinition(tipObjImpDefinition);
			           
			// si se da de alta determinado rubro (o quizá algun otro atributo) se debe crear la cuenta secundaria
			// o si se da de baja desactivarla
            // Llamamos al algoritmo que decide si se crea o se da de baja si existe, la cuenta Etur.
			SiatHibernateUtil.currentSession().evict(objImp);
			SiatHibernateUtil.currentSession().flush();
			objImp = ObjImp.getByTipObjImpYNroCta(tipObjImp.getId(), datum.getCols(COLSCLAVE));
			PadObjetoImponibleManager.getInstance().altaBajaCuentaSecComercio(objImp, fechaNovedad);
            
		    //Determinar si el cambio tiene un efecto retroactivo que produzca la necesidad de marcar las cuentas.
		    //esto probablemente siga pero solo para algunos atributos (FechaInicio por ejemplo)
		    for(Cuenta cuenta: objImp.getListCuentaActiva()){
		    	if(cuenta.getRecurso().existeEmisionPosterior(fechaDesde)){
		    		CasSolicitudManager.getInstance().createSolicitud(TipoSolicitud.COD_MODIFICACION_DATOS
		    														,asuSolicitud,desSolicitud,cuenta, Area.COD_CATASTRO);
		    	}
		    }
		            
           if (tipObjImpDefinition.hasError()) {
	           	String msg = "Proceso de Novedad, Correcion de ObjImp, No se pudo realizar la correccion. (ver log)";
	           	String msgErr = msg + objImp.infoString() + "\n" + objImp.errorString();
	           	run.logError(msgErr);
	           	run.changeStateError(msg);	
	           	return false;
			}
		} catch (Exception e) {
			String msg = "Proceso de Novedad, Modificacion de ObjImp: Error Grave: " + e.toString();
           	run.changeStateError(msg, e);			
			return false;
		}	
		return true;
	}
	
	
	public boolean altaSepultura(AdpRun run, Datum datum) throws Exception {
		try {
			
			// Recuperamos el Tipo de Objeto Imponible Parcela
			TipObjImp tipObjImp = TipObjImp.getByCodigo(datum.getCols(COLSCODTIPOBJIMP));
			if (tipObjImp == null) {
	           	String msg = "Proceso de Novedad, Alta el ObjImp, Codigo de Tipo de Objeto Imponible desconocido: " + datum.getCols(COLSCODTIPOBJIMP);
	           	run.logError(msg);
	           	run.changeStateError(msg);
	           	return false;
			}					

			// Verificamos que el ObjImp no exista en la db.
			ObjImp objImp = null;
			objImp = ObjImp.getByTipObjImpYNroCta(tipObjImp.getId(), datum.getCols(COLSCLAVE));
			if(objImp != null){
				String msg = "Proceso de Novedad, Alta de ObjImp: El ObjImp " 
						+"con TipObjImp="+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVE)
						+" existe en la db.";
				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
			// Recuperamos algunos datos generales para ObjImp y sus atributos.
			String clave = datum.getCols(COLSCLAVE);

			//String claveFuncional = datum.getCols(COLSCLAVEFUNCIONAL4PARCELA);

			Date fechaNovedad = datum.getDate(COLSFECHAACCION);
			if (fechaNovedad == null) {
				String msg = "Proceso de Novedad, Alta de ObjImp: fechaNovedad (alias fechaAccion), posee formato invalido. " + datum.getCols(COLSFECHAACCION);
				run.logError(msg);
				run.changeStateError(msg);
				return false;				
			}
			
			Date fechaDesde = datum.getDate(COLSFECHAVIG);
			if (fechaDesde == null) {
				String msg = "Proceso de Novedad, Alta de ObjImp: fechaDesde, posee formato invalido. " + datum.getCols(COLSFECHAVIG);
				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
			
			Date fechaHasta = null;

			// Recuperamos el Definition para Sepultura
			TipObjImpDefinition tipObjImpDefinition = tipObjImp.getDefinitionForInterface();
			TipObjImpAtrDefinition atrDefinition;

			// Por cada atributo seteamos su valor en el Definition
			for(int columna = COLSINICIOATR ; columna <= COLSFINATR; columna++){
				atrDefinition = tipObjImpDefinition.getTipObjImpAtrDefinitionByColumna(columna); 
				if(atrDefinition != null){
					String valor = datum.getCols(columna);
					// Cuando estamos en la columna Catastral, la formateamos completando con ceros a la izquierda
					/*if(columna == COLSCLAVEFUNCIONAL4PARCELA){
						if(!"".equals(valor)){
							Datum datumCata = Datum.parseAtChar(valor, '/');
							String[] catastral = new String[datumCata.getColNumMax()]; 
							catastral[0] = StringUtil.completarCerosIzq(datumCata.getCols(0), 2);
							for(int i=1;i<datumCata.getColNumMax();i++)
								catastral[i] = StringUtil.completarCerosIzq(datumCata.getCols(i), 3);
							valor = "";
							for(int i=0;i<datumCata.getColNumMax()-1;i++)
								valor += catastral[i]+"/";		
							valor += catastral[datumCata.getColNumMax()-1];
						}
					}*/
					atrDefinition.addValor(valor, fechaNovedad, fechaDesde, fechaHasta);
				}
			}

			// Validamos el Definition
			tipObjImpDefinition.validate();
			if(tipObjImpDefinition.hasError()){
				String msg = "Proceso de Novedad, Alta de ObjImp: Se encontraron errores en atributos para el ObjImp " 
						+"con TipObjImp="+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVE);
				String msgErr = msg;
				for(DemodaStringMsg item: tipObjImpDefinition.getListError()){
					msgErr += "--> "+item.key()+": "+(String) item.params()[0] + "\n";
				}

				run.logError(msgErr);
				run.changeStateError(msg);
				return false;
			}
			
			// Validamos el Domicilio de Envio.
			String domicilioToParse = tipObjImpDefinition.getTipObjImpAtrDefinitionByCodigo("DomicilioEnvio").getValorString();
			if(!Domicilio.isValidToParse(domicilioToParse)){
				String msg = "Proceso de Novedad, Alta de ObjImp: El ObjImp " 
						+"con TipObjImp="+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVE)
						+" tiene el domicilio de envio con formato incorrecto. Valor Recibido: "+domicilioToParse;

				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
		
			// Creamos el ObjImp y sus atributos.
			objImp = new ObjImp();
			objImp.setTipObjImp(tipObjImp);
			objImp.setClave(clave);
			objImp.setClaveFuncional(tipObjImpDefinition.getValClaveFunc());//claveFuncional);
			objImp.setFechaAlta(datum.getDate(COLSFECHAVIG));
			objImp.setEstado(Estado.ACTIVO.getId());

			objImp = PadObjetoImponibleManager.getInstance().createObjImp(objImp, tipObjImpDefinition);
			if (objImp.hasError()) {
	           	String msg = "Proceso de Novedad, Alta el ObjImp, No se pudo dar de Alta Cuenta. (ver log)";
	           	String msgErr = msg + objImp.infoString() + "\n";
	           	msgErr += objImp.errorString();
	           	run.logError(msgErr);
	           	run.changeStateError(msg);				
	           	return false;
			}
			/*
			String atrTipoParcela = datum.getCols(COLSTIPOPARCELA4PARCELA); 
			//String atrTipoParcela = tipObjImpDefinition.getTipObjImpAtrDefinitionByCodigo("TipoParcela").getAtrValDefinitionVigente().getValor();

			
			if("2".equals(atrTipoParcela)){
				// Obtengo la Cuenta Principal creada para el objImp dado de alta.
				Cuenta cuenta = objImp.getCuentaPrincipalActiva();
				if(cuenta!=null){
				  	// Busco la Zona de la parcela obteniendo la seccion de la catastral y luego la zona a la cual pertenece.
					Zona zona = null;
		    		String catastral = objImp.getClaveFuncional();
		    		String idseccion = StringUtil.substringHasta(catastral,'/');
		    		Seccion seccion = Seccion.getById(new Long(idseccion));
		    		zona = seccion.getZona();

		    		// Busco un broche que corresponda a un repartidor fuera de zona para el domicilio de envio enviado.
		    		Broche broche = PadDistribucionManager.getInstance().obtenerRepartidorFueraDeZona
		    			(cuenta.getDomicilioEnvio(), zona);

		    		// Si se encuentra el broche se asigna a la cuenta
		    		if (broche != null) {
		    			cuenta.asignarBroche(broche, fechaDesde, null);
		    		}

		    		// Si no encuentra broche crea una solicitud de asignacion de broche a cuenta
		    		if (broche == null) {
		    			String descripcion = "Alta de Parcela de Tipo Baldio, Nro cuenta: " +  cuenta.getNumeroCuenta() + 
		    							 	", Zona parcela: " + zona.getDescripcion() + 
		    							 	", Domicilio Envio: " + cuenta.getDomicilioEnvio().getViewDomicilio(); 
		    		
		    			CasSolicitudManager.getInstance().createSolicitud
		    				(TipoSolicitud.COD_ASIGNACION_BROCHE_CUENTA, 
		    				"No se encontro broche para asignar a la cuenta", 
		    				descripcion, cuenta, Area.COD_CATASTRO);
		    		}
		    		
				}
			}
			
			String codresptrib = datum.getCols(COLSCRT4PARCELA);
			Date fechaDesdeCRT = null;
			Date fechaHastaCRT = null;
			// Si el crt==2 o crt==3 creamos exencion.
			if("2".equals(codresptrib) || "3".equals(codresptrib)){
				Exencion exencion = null;
				if("2".equals(codresptrib))
					exencion = Exencion.getByCogido(Exencion.COD_EXENCION_QUITA_SOBRETASA);
				if("3".equals(codresptrib))
					exencion = Exencion.getByCogido(Exencion.COD_EXENCION_EXENTO_TOTAL);
				CueExe cueExe = new CueExe();
				cueExe.setCuenta(objImp.getCuentaPrincipalActiva());
				cueExe.setExencion(exencion);
				if(!StringUtil.isNullOrEmpty(datum.getCols(COLSCRTFECHADESDE4PARCELA)))
		    		fechaDesdeCRT = datum.getDate(COLSCRTFECHADESDE4PARCELA);
		    	if(!StringUtil.isNullOrEmpty(datum.getCols(COLSCRTFECHAHASTA4PARCELA)))
		    		fechaHastaCRT = datum.getDate(COLSCRTFECHAHASTA4PARCELA);
		    	
		    	if(fechaDesdeCRT!=null)
		    		cueExe.setFechaDesde(fechaDesdeCRT);
		    	else
		    		cueExe.setFechaDesde(fechaDesde);
		    	cueExe.setFechaHasta(fechaHastaCRT); 
		    	cueExe.setEstadoCueExe(EstadoCueExe.getById(EstadoCueExe.ID_HA_LUGAR));
		    	cueExe.setTipoSujeto(TipoSujeto.getById(TipoSujeto.ID_NOVEDADES));
		    	cueExe.setObservaciones("Novedades de Catastro, Alta de ObjImp: Codigo Responsabilidad Tributaria = "+codresptrib);
		    	
		    	cueExe = ExeExencionManager.getInstance().createCueExe(cueExe);
		    	
		    	// Se crea Historico de Estado CueExe
				HisEstCueExe hisEstCueExe = new HisEstCueExe();
				hisEstCueExe.setCueExe(cueExe); 
				hisEstCueExe.setEstadoCueExe(cueExe.getEstadoCueExe());
				hisEstCueExe.setObservaciones("Novedades de Catastro, Alta de ObjImp: Codigo Responsabilidad Tributaria = "+codresptrib);
				hisEstCueExe.setLogCambios("Alta de Exencion.");
				hisEstCueExe.setFecha(fechaDesde);
				ExeDAOFactory.getHisEstCueExeDAO().update(hisEstCueExe);
			}
		
				String atrUsoParcela = datum.getCols(COLSUSOPARCELA4PARCELA); 

			// Si el Atributo UsoParcela es informado
			if("8".equals(atrUsoParcela) || "9".equals(atrUsoParcela)){
				// Obtengo la Cuenta Principal creada para el objImp.
				Cuenta cuenta = objImp.getCuentaPrincipalActiva();
				if(cuenta!=null) {
					//TODO: buscar descripcion del valor de dominio
					String descripcion = "Novedades de Catastro, Alta de ObjImp: Uso de Parcela = "+atrUsoParcela+". Nro cuenta: " +  cuenta.getNumeroCuenta(); 		    			
            		CasSolicitudManager.getInstance().createSolicitud(TipoSolicitud.COD_VERIFICAR_EXENCION_CUENTA
							,"Novedades de Catastro, Alta de ObjImp.", descripcion,cuenta, Area.COD_CATASTRO);
				}
			}
			*/
			
			
			if (objImp.hasError()) {
	           	String msg = "Proceso de Novedad, Alta el ObjImp, No se pudo dar de Alta. (ver log)";
	           	String msgErr = msg + objImp.infoString() + "\n";
	           	msgErr += objImp.errorString();
	           	run.logError(msgErr);
	           	run.changeStateError(msg);	
	           	return false;
			}
		} catch (Exception e) {
			String msg = "Proceso de Novedad, Alta de ObjImp: Error Grave: " + e.toString();
           	run.changeStateError(msg, e);			
			return false;
		}
		return true;
	}
	
	public boolean modifSepultura(AdpRun run, Datum datum) throws Exception {
		try {
			String asuSolicitud = "Modificaciones de Atributos del Objeto Imponible con Catastral: ";
			String desSolicitud = "Modificaciones con efecto retroactivo. Existe emisión posterior a la fecha de vigencia de los nuevos valores. Nuevos Valores: ";
			
			// Recuperamos el Tipo de Objeto Imponible Parcela
			TipObjImp tipObjImp = TipObjImp.getByCodigo(datum.getCols(COLSCODTIPOBJIMP));
			if (tipObjImp == null) {
	           	String msg = "Proceso de Novedad, Modificacion ObjImp, Codigo de Tipo de Objeto Imponible desconocido: " + datum.getCols(COLSCODTIPOBJIMP);
	           	run.logError(msg);
	           	run.changeStateError(msg);
	           	return false;
			}
		
			// Obtenemos que el ObjImp de la db.
			ObjImp objImp = null;
			objImp = ObjImp.getByTipObjImpYNroCta(tipObjImp.getId(), datum.getCols(COLSCLAVE));
			if(objImp == null){
				String msg = "Proceso de Novedad, Modificacion de ObjImp: El ObjImp con TipObjImp="
						+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVE)
						+" no existe en la db.";

				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}
		
			// Recuperamos algunos datos generales para ObjImp y sus atributos.
			Date fechaNovedad = datum.getDate(COLSFECHAACCION);
			if (fechaNovedad == null) {
				String msg = "Proceso de Novedad, Alta de ObjImp: fechaNovedad (alias fechaAccion), posee formato invalido. " + datum.getCols(COLSFECHAACCION);
				run.logError(msg);
				run.changeStateError(msg);
				return false;				
			}
			
			Date fechaDesde = datum.getDate(COLSFECHAVIG);
			if (fechaDesde == null) {
				String msg = "Proceso de Novedad, Alta de ObjImp: fechaDesde, posee formato invalido. " + datum.getCols(COLSFECHAVIG);
				run.logError(msg);
				run.changeStateError(msg);
				return false;
			}

			
			Date fechaHasta = null;
		
			asuSolicitud += objImp.getClaveFuncional();
			
			// Recuperamos el Definition para Parcela
			TipObjImpDefinition tipObjImpDefinition = tipObjImp.getDefinitionForInterface();
			TipObjImpAtrDefinition atrDefinition;
			
			// Por cada atributo ,si el valor de la columna es distinto de "" o " ", lo seteamos en el Definition
			for(int columna = COLSINICIOATR ; columna <= COLSFINATR; columna++){
				if(!(datum.getCols(columna).equals("") || datum.getCols(columna).equals(" "))){
					atrDefinition = tipObjImpDefinition.getTipObjImpAtrDefinitionByColumna(columna);
					if(atrDefinition != null){
						String valor = datum.getCols(columna);
						// Cuando estamos en la columna Catastral, la formateamos completando con ceros a la izquierda
						/*if(columna == COLSCLAVEFUNCIONAL4PARCELA){
							Datum datumCata = Datum.parseAtChar(valor, '/');
							String[] catastral = new String[datumCata.getColNumMax()]; 
							catastral[0] = StringUtil.completarCerosIzq(datumCata.getCols(0), 2);
							for(int i=1;i<datumCata.getColNumMax();i++)
								catastral[i] = StringUtil.completarCerosIzq(datumCata.getCols(i), 3);
							valor = "";
							for(int i=0;i<datumCata.getColNumMax()-1;i++)
								valor += catastral[i]+"/";		
							valor += catastral[datumCata.getColNumMax()-1];
						}*/
						atrDefinition.addValor(valor, fechaNovedad, fechaDesde, fechaHasta);

						desSolicitud += atrDefinition.getAtributo().getDesAtributo()+" = ";
						desSolicitud += datum.getCols(columna)+" ";
					}
				}else{
					atrDefinition = tipObjImpDefinition.getTipObjImpAtrDefinitionByColumna(columna);
					if(atrDefinition != null){
						tipObjImpDefinition.removeTipObjImpAtrDefinition(atrDefinition);						
					}
				}
			}
			desSolicitud += "Fecha Novedad: "+datum.getCols(COLSFECHAACCION);
			desSolicitud += " Fecha Desde de los Atributos: "+datum.getCols(COLSFECHAVIG);
			// Sacamos los atributos que no se cargan por novedad del Definition. (Broche con id=50, QuitaFecha con id=51, NroCuenta con id=52)
			/*atrDefinition = tipObjImpDefinition.getTipObjImpAtrDefinitionById(50L);
			if(atrDefinition != null){
				tipObjImpDefinition.removeTipObjImpAtrDefinition(atrDefinition);						
			}
			atrDefinition = tipObjImpDefinition.getTipObjImpAtrDefinitionById(51L);
			if(atrDefinition != null){
				tipObjImpDefinition.removeTipObjImpAtrDefinition(atrDefinition);						
			}
			atrDefinition = tipObjImpDefinition.getTipObjImpAtrDefinitionById(52L);
			if(atrDefinition != null){
				tipObjImpDefinition.removeTipObjImpAtrDefinition(atrDefinition);						
			}*/

			
			// Validamos el Definition
			tipObjImpDefinition.validate();
			if(tipObjImpDefinition.hasError()){
				String msg = "Proceso de Novedad, Modificacion de ObjImp: Se encontraron errores en atributos para el ObjImp " 
					+ "con TipObjImp="+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVE);
				String msgErr = msg + "\n";
				for(DemodaStringMsg item: tipObjImpDefinition.getListError()){
					msgErr += "--> "+item.key()+": "+(String) item.params()[0] + "\n";
				}

				run.logError(msgErr);
				run.changeStateError(msg);
				return false;
			}
			/*
			//antes moficicar obtenemos el viejo valor de tipo de parcela:
			String nuevoTipoParcela = datum.getCols(COLSTIPOPARCELA4PARCELA);
			TipObjImpDefinition oldDefinition = objImp.getDefinitionValue();			
			String oldTipoParcela = oldDefinition.getTipObjImpAtrDefinitionByCodigo("TipoParcela").getValorString();
			log.info(String.format("Cambia tipoparcela? viejo:'%s' -> nuevo:'%s'", oldTipoParcela, nuevoTipoParcela));
			
			// Modificamos los atributos del ObjImp
            tipObjImpDefinition = objImp.updateObjImpAtrDefinition(tipObjImpDefinition);
			 */
    		// Verificamos si viene informado un cambio de Domicilio de Envio.
			TipObjImpAtrDefinition atributoDomicilioEnvio =tipObjImpDefinition.getTipObjImpAtrDefinitionByCodigo("DomicilioEnvio");
            if(atributoDomicilioEnvio != null){
    			String domicilioToParse = atributoDomicilioEnvio.getValorString();
    			if(!Domicilio.isValidToParse(domicilioToParse)){
    				String msg = "Proceso de Novedad, Modificacion de ObjImp: El ObjImp " 
    						+"con TipObjImp="+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVE)
    						+" tiene el domicilio de envio con formato incorrecto. Valor Recibido: "+domicilioToParse;

    				run.logError(msg);
    				run.changeStateError(msg);
    				return false;
    			}
            	// Domicilio de Envio
    			Domicilio domicilioEnvio = Domicilio.valueOf(domicilioToParse);
				domicilioEnvio = PadDomicilioManager.getInstance().createDomicilio(domicilioEnvio);					
				if(domicilioEnvio.hasError()){
					String msg = "Proceso de Novedad, Modificacion de ObjImp: El ObjImp " 
						+"con TipObjImp="+tipObjImp.getDesTipObjImp()+" y Clave="+datum.getCols(COLSCLAVE)
						+" no pudo crear el nuevo domicilio de envio.";			
					run.logError(msg);
					run.changeStateError(msg);
					return false;   						
				}   					
   				Cuenta cuenta = objImp.getCuentaPrincipalActiva();
				cuenta.cambiarDomicilioEnvio(domicilioEnvio);
            }
			
            //Determinar si el cambio tiene un efecto retroactivo que produzca la necesidad de marcar las cuentas.
            for(Cuenta cuenta: objImp.getListCuentaActiva()){
            	if(cuenta.getRecurso().existeEmisionPosterior(fechaDesde)){
            		CasSolicitudManager.getInstance().createSolicitud(TipoSolicitud.COD_MODIFICACION_DATOS
            														,asuSolicitud,desSolicitud,cuenta, Area.COD_CATASTRO);
            	}
            }
            /*
			String codresptrib = datum.getCols(COLSCRT4PARCELA);
			Date fechaDesdeCRT = null;
			Date fechaHastaCRT = null;
			// Si el crt==2 o crt==3 creamos una solicitud de verificar exencion.
			if("2".equals(codresptrib) || "3".equals(codresptrib)){
				if(!StringUtil.isNullOrEmpty(datum.getCols(COLSCRTFECHADESDE4PARCELA)))
		    		fechaDesdeCRT = datum.getDate(COLSCRTFECHADESDE4PARCELA);
		    	if(!StringUtil.isNullOrEmpty(datum.getCols(COLSCRTFECHAHASTA4PARCELA)))
		    		fechaHastaCRT = datum.getDate(COLSCRTFECHAHASTA4PARCELA);
		   
		    	desSolicitud = "Cambio de valor del atributo CRT, nuevo valor = "+codresptrib;
		    	desSolicitud += " Fecha Desde CRT="+DateUtil.formatDate(fechaDesdeCRT,DateUtil.ddSMMSYYYY_MASK)+" Fecha Hasta CRT="+DateUtil.formatDate(fechaHastaCRT,DateUtil.ddSMMSYYYY_MASK);
		    	// Creamos una solicitud de verificar exencion para la cuenta principal.
    			CasSolicitudManager.getInstance().createSolicitud(TipoSolicitud.COD_VERIFICAR_EXENCION_CUENTA
    					,asuSolicitud,desSolicitud,objImp.getCuentaPrincipalActiva(), Area.COD_CATASTRO);    	
			}
    			
			//String atrTipoParcela = datum.getCols(COLSTIPOPARCELA4PARCELA); 
			//String atrTipoParcela = tipObjImpDefinition.getTipObjImpAtrDefinitionByCodigo("TipoParcela").getAtrValDefinitionVigente().getValor();

			// Si el Atributo TipoParcela es informado
			if(!StringUtil.isNullOrEmpty(nuevoTipoParcela)){
				// Obtengo la Cuenta Principal creada para el objImp.
				Cuenta cuenta = objImp.getCuentaPrincipalActiva();
				if(cuenta != null) {
					// cambio realizado por bug:668  16-mar-2009 - fedel
					// Verficamos si hubo cambio de TipoParcela de Baldio(2) a Finca(1)
					// Y en tal caso damos de baja las exenciones quita de sobretasa con 
					// fechaHasta a la fecha indicada en la novedad
					if (oldTipoParcela.equals("2") && nuevoTipoParcela.equals("1")) {
						List<CueExe> listCueExe = cuenta.getListCueExeVigente(fechaDesde);
						for(CueExe cueExe : listCueExe) {
							if (Exencion.ID_EXENCION_QUITA_SOBRETASA.equals(cueExe.getExencion().getId())) {
								cueExe.setFechaHasta(fechaDesde);
								ExeDAOFactory.getExencionDAO().update(cueExe);
							}
						}
					}
				
				}
			}

			String atrUsoParcela = datum.getCols(COLSUSOPARCELA4PARCELA); 

			// Si el Atributo UsoParcela es informado
			if(!StringUtil.isNullOrEmpty(atrUsoParcela)){
				// Obtengo la Cuenta Principal creada para el objImp.
				Cuenta cuenta = objImp.getCuentaPrincipalActiva();
				if(cuenta!=null) {
					//TODO: buscar descripcion del valor de dominio
					String descripcion = "Modificacion de Uso de la Parcela (Cochera / Baulera). Nro cuenta: " +  cuenta.getNumeroCuenta(); 		    			
            		CasSolicitudManager.getInstance().createSolicitud(TipoSolicitud.COD_VERIFICAR_EXENCION_CUENTA
							,asuSolicitud, descripcion,cuenta, Area.COD_CATASTRO);
				}
			}
			  	*/		 		
			
			
            if (tipObjImpDefinition.hasError()) {
	           	String msg = "Proceso de Novedad, Modificacion de ObjImp, No se pudo modificar. (ver log)";
	           	String msgErr = msg + objImp.infoString() + "\n" + objImp.errorString();
	           	run.logError(msgErr);
	           	run.changeStateError(msg);	
	           	return false;
			}
		} catch (Exception e) {
			String msg = "Proceso de Novedad, Modificacion de ObjImp: Error Grave: " + e.toString();
           	run.changeStateError(msg, e);			
			return false;
		}	
		return true;
	}
	
}
