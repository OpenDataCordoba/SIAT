//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.emision;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.emi.buss.bean.EmiInfCue;
import ar.gov.rosario.siat.emi.buss.bean.ImpMasDeu;
import ar.gov.rosario.siat.emi.buss.dao.EmiDAOFactory;
import ar.gov.rosario.siat.gde.buss.bean.DeuAdmRecCon;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.buss.bean.Recibo;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.pad.buss.bean.AsignaRepartidor;
import ar.gov.rosario.siat.pad.buss.bean.Broche;
import ar.gov.rosario.siat.pad.buss.bean.Contribuyente;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.Domicilio;
import coop.tecso.adpcore.AdpProcessor;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.ProcessMessage;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.SplitedWriter;
import coop.tecso.demoda.iface.helper.SpooledWriter;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

public class ImpMasDeuProcessor extends AdpProcessor {

	private static Logger log = Logger.getLogger(ImpMasDeuProcessor.class);

	// Tamaño de la pagina para flushear la sesion   
	private static final int FLUSH_PAGE_SIZE = 100;
	// Context
	private ImpMasDeuContext context;
	// Id del mensaje en proceso	
	private Long currentMsgId;
	// Asignador de Repartidores
	private AsignaRepartidor asignador;
	// Clave para el Spooler
	private String currentSpoolKey = "0000";
	// Periodicidad
	private String periodicidad = "";
	//Se utiliza para armar el Header
	private Recurso recurso;
	
	public ImpMasDeuProcessor(ImpMasDeuContext context,
			BlockingQueue<ProcessMessage<Long>> msgQueue) throws Exception {
		super(context, msgQueue);
		this.context = context;
	}

	public void execute(AdpRun run) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		Long pasoActual = run.getPasoActual();
		log.debug("Ejecutando paso " + pasoActual);
		try {
	
			if (pasoActual.equals(1L)) {
				ejecutarPaso1(run);
			}

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		
		} catch (Exception e) {
			String msg = "Error durante la ejecucion del paso " + pasoActual;
			log.error(msg, e);
		}
	}

	private void ejecutarPaso1(AdpRun run) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Session session = null;
		Transaction tx = null;
		try {
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			Long idImpMasDeu = Long.parseLong(run.getParameter(ImpMasDeu.ID_IMPMASDEU));
			ImpMasDeu impMasDeu = ImpMasDeu.getById(idImpMasDeu);
			recurso = impMasDeu.getRecurso();
			this.asignador = new AsignaRepartidor(recurso.getId(), new Date());
			this.periodicidad = recurso.getPeriodoDeuda().getDesPeriodoDeuda();
			if (this.periodicidad != null) {
				this.periodicidad = this.periodicidad.toUpperCase();
			}
			
			Long flushCounter = 0L;
			
			while (true) {
				// Obtenemos un mensaje de la cola
				ProcessMessage<Long> msg = getMessage();
			
				// Verificamos si se debe terminar con el thread
				if (msg.isPoisonMessage()) { 
					break; 
				}
				Long idCuenta = msg.getData();
				this.currentMsgId = msg.getId();
				
				log.debug("Analizando cuenta id: " + idCuenta);

				procesarCuenta(impMasDeu,idCuenta);
				flushCounter++;
				run.incRegCounter();
				
				if (flushCounter % FLUSH_PAGE_SIZE == 0) {
					Long status = run.getRunStatus();
					run.changeStatusMsg("Obteniendo deuda: " + status + "%");
					tx.commit();
					SiatHibernateUtil.closeSession();

					session = SiatHibernateUtil.currentSession();
					tx 		= session.beginTransaction();
					log.debug("Flushing Session: "+ flushCounter);
				}
			}
			// Actualizamos la BD
			tx.commit();
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			
		} catch (Exception e) {
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			session.close();
		}
	}
	
	private void procesarCuenta(ImpMasDeu impMasDeu, Long idCuenta) throws Exception {
		// Obtenemos la corrida
		AdpRun run = context.getAdpRun();
		// Obtenemos el spooler writer para los recibos 
		SpooledWriter spoolRecibos = context.getSpoolRecibos();

		try {
			// Obtenemos los parametros del proceso
			Recurso recurso = impMasDeu.getRecurso();
			Integer anio = impMasDeu.getAnio();
			Integer periodoDesde = impMasDeu.getPeriodoDesde();
			Integer periodoHasta = impMasDeu.getPeriodoHasta();

			// Obtenemos la cuenta a procesar
			Cuenta cuenta = EmiDAOFactory.getImpMasDeuDAO().getCuenta(idCuenta);

			// Broche
			Broche broche = cuenta.getBroche();
			if (broche != null) {
				// Analizamos si se imprime o no el broche
				if (broche.getPermiteImpresion() != null && broche.getPermiteImpresion().equals(SiNo.NO.getBussId())) {
					run.logDebug("Cuenta " + cuenta.getNumeroCuenta() + " excluida por broche");
					spoolRecibos.write("br0000",this.currentMsgId, "");
					return;
				}
			}

			// Si la cuenta no permite impresion, no la procesamos
			if (cuenta.getPermiteImpresion() != null && cuenta.getPermiteImpresion().equals(SiNo.NO.getBussId())) {
					run.logDebug("Cuenta " + cuenta.getNumeroCuenta() + " excluida");
					spoolRecibos.error(this.currentMsgId);
					return;
			}

			// Obtenemos la deuda de la cuenta a imprimir
			List<DeudaAdmin> listDeudaAdmin = (ArrayList<DeudaAdmin>) 
				EmiDAOFactory.getImpMasDeuDAO().getListDeudaAdminBy(recurso, idCuenta, anio, periodoDesde, periodoHasta);

			// Obtener el ultimo atributo de asentamiento.
			// (la lista de deuda esta ordenada por periodo)
			String atrAseVal = "";
			for (DeudaAdmin deuda: listDeudaAdmin)
				atrAseVal = deuda.getAtrAseVal();


			Long nroBroche = 0L;
			// Si se aplica el criterio de reparto, obtenemos 
			// el numero de broche.
			// Además setea durante el calculo, el currentSpoolKey
			if (impMasDeu.getAbrirPorBroche().equals(SiNo.SI.getBussId())) {
				nroBroche = obtenerBroche(cuenta, atrAseVal);
			}
			
			synchronized (spoolRecibos) {
				// Si no esta creado el archivo de salida para este
				// broche, lo creamos y lo asociamos al spooler
				if (spoolRecibos.getWriter(this.currentSpoolKey) == null) {
					String unidad = File.separator.equals("\\") ? "c:\\" : "";
					String xmlTmpFilename = unidad + "/tmp/impresionMasiva-" + UUID.randomUUID() + ".xml";
					SplitedWriter xmlFile = new SplitedWriter(xmlTmpFilename, 14000000, createHeader(), footer, false);
					spoolRecibos.addWriter(this.currentSpoolKey, xmlFile);	
				}
			}

			// Escribimos en el archivo de salida para este broche
			String reciboXML = generateXmlRecibo(run, cuenta,listDeudaAdmin, anio, periodoDesde, periodoHasta, nroBroche);
			spoolRecibos.write(this.currentSpoolKey, this.currentMsgId, reciboXML);
			
			run.logDebug("Procesando cuenta id " + idCuenta + ": OK");

			context.getReporte().addReportData(this.currentSpoolKey, nroBroche.toString());
			
		} catch (Exception e) {
			log.error(": Processor Error ",  e);
			// Avisamos al spooler que hubo un error
			spoolRecibos.error(this.currentMsgId);
			// Logs en Adp 
			run.logDebug("Procesando cuenta con id " + idCuenta + ": ERROR");
			run.addWarning("No se pudo procesar la cuenta con id: " + idCuenta, e);
		}
	}
	
	private Long obtenerBroche(Cuenta cuenta, String atrAseVal) {
		Recurso recurso = cuenta.getRecurso();
		Broche broche = cuenta.getBroche();
		Long nroBroche = 0L;
		// Por defecto seteamos la clave del spool a 0000
		this.currentSpoolKey = "0000";
		
		// Caso TGI
		if (recurso.getCodRecurso().equals(Recurso.COD_RECURSO_TGI)) {
			// Si es FINCA y no tiene broche asigando
			if ((atrAseVal.equals( "1")) && (broche == null)) {
				String catastral = cuenta.getCatastral();
				nroBroche = this.asignador.obtenerNroBroche(catastral);
				// Si no se pudo asignar un broche a la FINCA
				if (nroBroche == null) {
					this.currentSpoolKey = "fi0000";
					nroBroche = 0L;
				} else {
					this.currentSpoolKey = "fi" + StringUtil.fillWithCharToLeft(nroBroche.toString(), '0', 4);
				}
			} else {
				if (broche == null) { // Si es BALDIO
					this.currentSpoolKey = "ba0000";
					nroBroche = 0L;
				} else { // Si la cuenta tenia un broche asigando
					this.currentSpoolKey = "br0000";
					nroBroche = broche.getId();
				}
			}
		}
		
		// Caso DReI, ETuR, Derecho Publicitario
		if (recurso.getCodRecurso().equals(Recurso.COD_RECURSO_DReI) || 
			recurso.getCodRecurso().equals(Recurso.COD_RECURSO_ETuR) ||
			recurso.getCodRecurso().equals(Recurso.COD_RECURSO_DPUB)) {

			// Si no tenia un broche asigado, tratamos de asignar uno
			if (broche == null) {
				String catastral = cuenta.getCatastral();
				nroBroche = this.asignador.obtenerNroBroche(catastral);
				if (nroBroche == null) 
					nroBroche = 0L;
			} else {
				nroBroche = broche.getId();
			}
			
			this.currentSpoolKey = StringUtil.fillWithCharToLeft(nroBroche.toString(), '0', 4);
		}
		
		return nroBroche;
	}
	
	private char[] createHeader() {
		StringBuilder b = new StringBuilder();
		b.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n");
		b.append("<Data>\n");
		b.append(writeXMLNode("FechaImpresion", DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK)));
		b.append("<PeriodoDeuda>\n");
		b.append(writeXMLNode("DesPeriodoDeuda", this.periodicidad));
		b.append("</PeriodoDeuda>\n");
		if (null != this.recurso) {
			b.append("<Recurso>\n");
			b.append(writeXMLNode("DesRecurso", this.recurso.getDesRecurso()));
			b.append("</Recurso>\n");
		}
		return b.toString().toCharArray();
	}

	private static char[] footer = "</Data>\n".toCharArray();
	
	/**
	 * Genera un XML que contiene toda la informacion necesaria para un recibo de deuda.
	 */
	private String generateXmlRecibo(AdpRun run, Cuenta cuenta, List<DeudaAdmin> listDeudaAdmin,
			Integer anio, Integer periodoDesde, Integer periodoHasta, Long nroBroche) throws Exception {
		try {
			String strCodPostal = "";
			String strCodSubPostal = "";
			String strLocalidad = "";
			if (nroBroche.equals(900L)) {
				Domicilio domicilio = cuenta.getDomicilioEnvio();
				if (domicilio != null && domicilio.getLocalidad() != null) {
					Long codPostal	  = domicilio.getLocalidad().getCodPostal();
					Long codSubPostal = domicilio.getLocalidad().getCodSubPostal();
					if (codPostal != null) {
						strCodPostal += codPostal;
						strCodSubPostal += ((codSubPostal != null) ? codSubPostal : "");;
						strLocalidad = context.getMapLocalidades().get(strCodPostal + strCodSubPostal);
						
						// Para los codigos postales Fuera de Rosario.
						if (!StringUtil.isNullOrEmpty(domicilio.getCodPostalFueraRosario())) {
							strCodPostal = domicilio.getCodPostalFueraRosario();
						}
					}
				}
			} else if (nroBroche.equals(999L)) {
				strLocalidad = "";
			} else {
				strCodPostal = "2000";
				strLocalidad = "ROSARIO";
			}
			 
			StringBuilder xmlContent = new StringBuilder();
			xmlContent.append("<Recibo>\n");

			// Datos de la Cuenta
			xmlContent.append("\t<Cuenta>\n");
			xmlContent.append("\t\t<Recurso>\n");
			xmlContent.append("\t\t\t").append(writeXMLNode("CodRecurso", cuenta.getRecurso().getCodRecurso()));
			xmlContent.append("\t\t\t").append(writeXMLNode("DesRecurso", cuenta.getRecurso().getDesRecurso()));
			xmlContent.append("\t\t</Recurso>\n");
			xmlContent.append("\t\t").append(writeXMLNode("NumeroCuenta", cuenta.getNumeroCuenta()));
			xmlContent.append("\t\t").append(writeXMLNode("CodGesCue", 	cuenta.getCodGesCue()));
			xmlContent.append("\t\t").append(writeXMLNode("NombreTitularPrincipal", cuenta.getNombreTitularPrincipal()));
			xmlContent.append("\t\t").append(writeXMLNode("CuitTitPri", cuenta.getCuitTitularPrincipalContr()));
			xmlContent.append("\t\t").append(writeXMLNode("Broche"	, nroBroche.toString()));
			xmlContent.append("\t\t").append(writeXMLNode("DesDomEnv"	, cuenta.getDesDomEnv()));
			//Agregado Leandro 21-11-2010
			xmlContent.append("\t\t").append(writeXMLNode("CatDomEnv"	, cuenta.getCatDomEnv()));
			//Fin agregado
			Recurso recurso = cuenta.getRecurso();
			// Si es un recurso autoliquidable, agregamos datos del contribuyente.
			if (recurso.getEsAutoliquidable() != null && recurso.getEsAutoliquidable().equals(SiNo.SI.getBussId())) {
				Contribuyente contribuyente = cuenta.obtenerCuentaTitularPrincipal().getContribuyente();
				String strConvMult = contribuyente.getEsConvenioMultilateral() ? "SI" : "NO";
				xmlContent.append("\t\t<Contribuyente>\n");
				xmlContent.append("\t\t\t").append(writeXMLNode("ConvMultilateral", strConvMult));
				xmlContent.append("\t\t\t").append(writeXMLNode("NroIIBB", contribuyente.getNroIsib()));
				xmlContent.append("\t\t\t").append(writeXMLNode("EsCER", contribuyente.getEsConribuyenteCER() ? "SI" : "NO" ));
				xmlContent.append("\t\t\t").append(writeXMLNode("Catastral", cuenta.getCatastral()));
				xmlContent.append("\t\t</Contribuyente>\n");
			}

			xmlContent.append("\t\t<Localidad>\n");
			xmlContent.append("\t\t\t").append(writeXMLNode("CodPostal"   , strCodPostal));
			xmlContent.append("\t\t\t").append(writeXMLNode("CodSubPostal", strCodSubPostal));
			xmlContent.append("\t\t\t").append(writeXMLNode("DesLocalidad", strCodPostal + " - " + strLocalidad));
			xmlContent.append("\t\t</Localidad>\n");

			// Datos del Objeto Imponible (Si existe)
			if (cuenta.getObjImp() != null) {
				xmlContent.append("\t\t<ObjImp>\n");
				xmlContent.append("\t\t\t").append(writeXMLNode("Clave", cuenta.getObjImp().getClave()));
				xmlContent.append("\t\t\t").append(writeXMLNode("ClaveFuncional", cuenta.getObjImp().getClaveFuncional()));
				xmlContent.append("\t\t</ObjImp>\n");
			}
			xmlContent.append("\t</Cuenta>\n");

			// Datos de la Deuda
			xmlContent.append("\t<ListDeudaAdmin>\n");

			List<EmiInfCue> listEmiInfCue = EmiDAOFactory.getEmiInfCueDAO()
				.getListBy(cuenta.getId(), anio, periodoDesde, periodoHasta);

			for (DeudaAdmin deudaAdmin: listDeudaAdmin) {

				String emiAtrVal = deudaAdmin.getEmiAtrVal();
				emiAtrVal = StringUtil.setXMLContentByTag(emiAtrVal, "A60", nroBroche.toString());
				deudaAdmin.setEmiAtrVal(emiAtrVal);
				
				// Actualizamos la deuda
				GdeDAOFactory.getDeudaAdminDAO().update(deudaAdmin);

				
				xmlContent.append("\t\t<DeudaAdmin>\n");
				xmlContent.append("\t\t\t<ListDeuAdmRecCon>\n");
				for (DeuAdmRecCon concepto: deudaAdmin.getListDeuRecCon()) {
					xmlContent.append("\t\t\t<DeuAdmRecCon>\n");
					xmlContent.append("\t\t\t\t").append(writeXMLNode("Orden", concepto.getRecCon().getOrdenVisualizacion().toString()));
					xmlContent.append("\t\t\t\t").append(writeXMLNode("Codigo", concepto.getRecCon().getCodRecCon()));
					xmlContent.append("\t\t\t\t").append(writeXMLNode("Desc"  , concepto.getRecCon().getDesRecCon()));
					xmlContent.append("\t\t\t\t").append(writeXMLNode("Abr"  , concepto.getRecCon().getAbrRecCon()));
					xmlContent.append("\t\t\t\t").append(writeXMLNode("Importe" ,concepto.getImporte().toString()));
					xmlContent.append("\t\t\t</DeuAdmRecCon>\n");
				}
				xmlContent.append("\t\t\t</ListDeuAdmRecCon>\n");
				
				xmlContent.append("\t\t\t").append(writeXMLNode("FechaEmision"  , DateUtil.formatDate(deudaAdmin.getFechaEmision(), DateUtil.ddSMMSYYYY_MASK)));
				xmlContent.append("\t\t\t").append(writeXMLNode("CodRefPag"     , deudaAdmin.getCodRefPag().toString()));
				xmlContent.append("\t\t\t").append(writeXMLNode("Anio"   		  , deudaAdmin.getAnio().toString()));
				xmlContent.append("\t\t\t").append(writeXMLNode("Periodo"		  , deudaAdmin.getPeriodo().toString()));
				xmlContent.append("\t\t\t").append(writeXMLNode("Importe"		  , deudaAdmin.getImporte().toString()));
				xmlContent.append("\t\t\t").append(writeXMLNode("ImporteBruto"  , deudaAdmin.getImporteBruto().toString()));
				xmlContent.append("\t\t\t").append(writeXMLNode("FecVen"		  , DateUtil.formatDate(deudaAdmin.getFechaVencimiento(), DateUtil.ddSMMSYYYY_MASK)));
				xmlContent.append("\t\t\t").append(writeXMLNode("StrEstadoDeuda", deudaAdmin.getStrEstadoDeuda()));
				
				// Generamos el numero del codigo de barras
				ServicioBanco servicioBanco = deudaAdmin.getSistema().getServicioBanco();
				String numCodBar = createNumCodBar(cuenta, deudaAdmin, servicioBanco);
				xmlContent.append("\t\t\t").append(writeXMLNode("NumCodBar", numCodBar));

				// Generamos el codigo de barras
				String codBar = StringUtil.genCodBarForTxt(numCodBar);
				xmlContent.append("\t\t\t").append(writeXMLNode("CodBar"	 , codBar));

 				for (EmiInfCue info: listEmiInfCue) {
 					Integer periodoDeuda = deudaAdmin.getPeriodo().intValue();
 					Integer anioDeuda 	 = deudaAdmin.getAnio().intValue();
 					if (info.getPeriodoDesde() <= periodoDeuda && info.getPeriodoHasta() >= periodoDeuda && 
 							anioDeuda.equals(info.getAnio())) {
						xmlContent.append("\t\t\t<").append(info.getTag().trim() + ">");
						xmlContent.append(info.getContenido());
						xmlContent.append("\t\t\t</").append(info.getTag().trim() + ">\n");
  					}
 				}
 				
				xmlContent.append("\t\t</DeudaAdmin>\n");
			}

			xmlContent.append("\t</ListDeudaAdmin>\n");

			xmlContent.append("</Recibo>\n");
			
			return xmlContent.toString();
			
		} catch (Exception e) {
			run.logDebug("Procesando cuenta con id " + cuenta.getId() + ": ERROR");
			run.addWarning("No se pudo procesar la cuenta " + cuenta.getNumeroCuenta(), e);
			return "";
		}
	}
	
	private String writeXMLNode(String tagName, String content) {
		return StringUtil.writeXMLNode(tagName, content) + "\n";
	}
		
	private String createNumCodBar(Cuenta cuenta, DeudaAdmin deuda, ServicioBanco servicioBanco) {
		String numCodBar = Recibo.createNumCodBar(cuenta, deuda.getCodRefPag() ,servicioBanco, 
							deuda.getSistema().getId(), deuda.getAnio(), deuda.getPeriodo(), 
							deuda.getImporte(),deuda.getFechaVencimiento(), true);
		return numCodBar;
	}
}