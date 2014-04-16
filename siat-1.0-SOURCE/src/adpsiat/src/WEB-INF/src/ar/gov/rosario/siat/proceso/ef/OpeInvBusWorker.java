//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.ef;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.ef.buss.bean.EstadoOpeInvCon;
import ar.gov.rosario.siat.ef.buss.bean.HisEstOpeInvCon;
import ar.gov.rosario.siat.ef.buss.bean.OpeInvBus;
import ar.gov.rosario.siat.ef.buss.bean.OpeInvCon;
import ar.gov.rosario.siat.ef.buss.bean.OpeInvConCue;
import ar.gov.rosario.siat.ef.buss.bean.SelAlmContr;
import ar.gov.rosario.siat.ef.buss.bean.SelAlmCueTit;
import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.SelAlm;
import ar.gov.rosario.siat.gde.buss.bean.TipoSelAlm;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.pad.buss.bean.Contribuyente;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.CuentaTitular;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import ar.gov.rosario.siat.pad.buss.bean.PersonaFacade;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.adpcore.AdpRunState;
import coop.tecso.adpcore.AdpWorker;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Procesa la Emision de Contribucion de Mejoras
 * @author Tecso Coop. Ltda.
 */
public class OpeInvBusWorker implements AdpWorker {

	private static final String MSJ_ERROR_FORMS = "Error al generar archivos de formularios";
	private static Logger log = Logger.getLogger(OpeInvBusWorker.class);
	
	private OpeInvBus opeInvBus;
	private Double paramPromedioPagoCta;
	private Double paramPromedioPagoContr;
	private Date fecHastaPromPagoCta;
	private Long[] listIdsRecClaDeu = {RecClaDeu.ID_DDJJ_ORIGINAL, RecClaDeu.ID_DDJJ_RECTIFICATIVA_DREI, 
														RecClaDeu.ID_RETENCIONES, RecClaDeu.ID_SALDOS_CONVENIOS};
	private Date fecDesdePromPagoCta;
	private long cantContrProcesados;
	
	private String from ="";
	private String where ="";
	private String query ="";
	private List<String> listPersonas = new ArrayList<String>();
	private List<String> listPersonasNoElim = new ArrayList<String>();
	
	public void reset(AdpRun adpRun) throws Exception {
	}
	
	public void cancel(AdpRun adpRun) throws Exception {
	}

	public void execute(AdpRun adpRun) throws Exception {
	//aca tenes que programar la emision.
		// verfica numero paso y estado en adprun,
		// llama a cada metodo segun el numero de paso
		Long pasoActual = adpRun.getPasoActual();
		if (pasoActual.equals(1L)) {
			String[] param={
							// parametros del Contr
							adpRun.getParameter("NRO_ISIB"),
							adpRun.getParameter("CUIT"),
							adpRun.getParameter(Atributo.COD_CER),
							adpRun.getParameter("PROM_PAGO_CONTR"),
							adpRun.getParameter("FEC_DESDE_PROM_PAGO_CONTR"),
							adpRun.getParameter("FEC_HASTA_PROM_PAGO_CONTR"),
							adpRun.getParameter("ID_ESTADO_OTROS_OPEINV"),

							// parametros del ObjImp		
							adpRun.getParameter(Atributo.COD_LOCALES_EN_OTRAS_PROV),
							adpRun.getParameter(Atributo.COD_LOCFUEROSENSFE),
							adpRun.getParameter(Atributo.COD_UBICACIONES),
							adpRun.getParameter(Atributo.COD_CANT_PERSONAL_SIAT),
							adpRun.getParameter(Atributo.COD_RUBRO),
							adpRun.getParameter(Atributo.COD_NROCOMERCIO),
							adpRun.getParameter(Atributo.COD_CATASTRAL+"_DESDE"),
							adpRun.getParameter(Atributo.COD_CATASTRAL+"_HASTA"),
							adpRun.getParameter(Atributo.COD_RAD_RED_TRIB),

							//Parametros de la cuenta
							adpRun.getParameter("PROM_PAGO_DE_CUENTA"),
							adpRun.getParameter("FEC_DESDE_PROM_PAGO_DE_CUENTA"),
							adpRun.getParameter("FEC_HASTA_PROM_PAGO_DE_CUENTA"),							
							adpRun.getParameter("CANT_PERIODOS_NO_DECLAR"),
							adpRun.getParameter(Atributo.COD_NROCUENTA),
							
							// datos de la OpeInvBus
							adpRun.getParameter("idOpeInvBus")
			};
			ejecutarPaso1(adpRun, param);

		}
		
		
		
	}

	public OpeInvBus validatePaso1(OpeInvBus opeInvBus) throws Exception {		
		return opeInvBus;
	}

	public void ejecutarPaso1(AdpRun adpRun, String[] param) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		opeInvBus = OpeInvBus.getById(new Long(param[21]));


		String msg ="";
		
		try {
			SiatHibernateUtil.currentSession().beginTransaction();

			boolean contieneTransacciones = true;
			Integer firstResult = 0;
			Integer maxResults = 1000;
			long cantPaginado = 0;
			
			paramPromedioPagoCta = param[16]!=null?new Double(param[16]):null;
			paramPromedioPagoContr= param[3]!=null?new Double(param[3]):null;
			fecDesdePromPagoCta = param[17]!=null?DateUtil.getDate(param[17]):null;
			fecHastaPromPagoCta = param[18]!=null?DateUtil.getDate(param[18]):null;
			
			// Crea la selecciona almacenada, del tipo que corresponda
			SelAlm selAlm = null;
			if(paramPromedioPagoCta!=null){
				selAlm = new SelAlmCueTit();
				selAlm.setTipoSelAlm(TipoSelAlm.getById(TipoSelAlm.TIPO_SEL_ALM_CUENTATITULAR));
			}else{
				selAlm = new SelAlmContr();
				selAlm.setTipoSelAlm(TipoSelAlm.getById(TipoSelAlm.TIPO_SEL_ALM_CONTRIBUYENTE));
			}
			GdeDAOFactory.getSelAlmDAO().update(selAlm);
			opeInvBus.setSelAlm(selAlm);
			
			
			// Obtiene la persona por CUIT (Si se ingreso algun valor), puede ser una lista de personas
			List<Persona> listPersona = null;
			boolean continuar = true;
			if(!StringUtil.isNullOrEmpty(param[1])){
				listPersona = PersonaFacade.getInstance().getListPersonaByCuit(param[1]);
				continuar =listPersona.size()>0;
				log.debug("Cant de personas obtenidas:"+listPersona.size());
			}
			
			// si ingreso un CUIT y encontro personas, continua, sino no
			if(continuar){
				
				///// La lista se hace fuera de la consulta, y no como subconsulta, por cuestiones de performance 

				// Obtiene los contribuyente que esten en otros operativos, en el estado pasado como parametro
				List<Long> listIdsContrOtroOpeInv = null;
				if(!StringUtil.isNullOrEmpty(param[6])){
					listIdsContrOtroOpeInv = OpeInvCon.getlistContrByEstOpeInv(new Long(param[6]));
					log.debug("Cantidad de contr con estado en otro operativo:"+listIdsContrOtroOpeInv.size());
					continuar =listIdsContrOtroOpeInv.size()>0;
				}
				
				// Si selecciono un estadoEnOtroOperativo y encontro contribuyentes sigue, sino no
				if(continuar){
										
					// Genera la parte del FROM y WHERE de la query 
					generarQuery(param,listPersona,listIdsContrOtroOpeInv);
					
					while (contieneTransacciones){
						//adpRun.changeMessage("");
						
						List<Integer> listIdsCuentaTitular = EfDAOFactory.getOpeInvBusDAO().
														 getListIdsCuentaTitular(query,firstResult, maxResults);
						
						contieneTransacciones = (listIdsCuentaTitular.size() > 0);
		
						if(contieneTransacciones){
							procesarListIdsCuentaTitular(listIdsCuentaTitular);
							firstResult += maxResults; // Incremento el indice del 1er registro
						}
						cantPaginado++;
						listIdsCuentaTitular = null;
						// Por razones de rendimiento, especificamente para mejorar los tiempos de procesamiento, se encontró
						// que además de la paginación y de realizar un Commit de la transacción cada un cierto numero de transacciones,
						// fue necesario cerrar la session de hibernate y volver a abrirla. Con esto se evita que el procesamiento
						// incremente sus tiempos a medida que aumentan las transacciones procesadas. Se probó realizando un clean 
						// de la session y reiniciando solo la transacción, pero de esta manera no se lograba solucionar este problema.
						if(cantPaginado%10==0){
							SiatHibernateUtil.currentSession().getTransaction().commit();
							SiatHibernateUtil.closeSession();
							SiatHibernateUtil.currentSession();
							SiatHibernateUtil.currentSession().beginTransaction();
						}
					}
				}
			}          
			if (opeInvBus.hasError()) {
            	SiatHibernateUtil.currentSession().getTransaction().rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	String descripcion = opeInvBus.getListError().get(0).key().substring(1);
            	adpRun.changeState(AdpRunState.FIN_ERROR, descripcion, false);
            	adpRun.logError(descripcion);
            	adpRun.changeState(AdpRunState.FIN_ERROR, MSJ_ERROR_FORMS, false);
			} else {
				SiatHibernateUtil.currentSession().getTransaction().commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			
				log.debug(funcName + ": exit");							
				// cambiar estado, incremetar paso, y actualiza el pasoCorrida (que fue creado cuando se activo(esqueduleo))
								
				log.debug("Va a generar el reporte");
				generarReporte(adpRun.getProcessDir(AdpRunDirEnum.SALIDA)+File.separator);

				String obs ="";
				if(opeInvBus.getTipBus().equals(OpeInvBus.TIPO_AGREGAR))
					obs = "La selección de contribuyentes ha finalizado exitosamente.";
				else if(opeInvBus.getTipBus().equals(OpeInvBus.TIPO_ELIMINAR))
					obs = "La eliminación de contribuyentes ha finalizado exitosamente.";
				
				adpRun.changeState(AdpRunState.FIN_OK, obs+msg, true);				

			}
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(SiatHibernateUtil.currentSession().getTransaction() != null) SiatHibernateUtil.currentSession().getTransaction().rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	private void generarReporte(String fileDir) throws Exception {
		log.debug("generarReporte - enter");
		String fileName="BusquedaMasiva_opeInv_"+opeInvBus.getCorrida().getId();
		// Genera el/los archivo/s csv, para excel
		String extensionArchivo = ".csv";
		BufferedWriter buffer = generarCabecera4Report(new FileWriter(fileDir+fileName+extensionArchivo, false));
		log.debug("Creo el archivo CSV:"+fileDir+fileName+extensionArchivo);

		long regCounter = 3;
		int numeroArchivo = 0;		
		
		buffer.newLine();
		if(listPersonas.size()==0){
			if(opeInvBus.getTipBus().equals(OpeInvBus.TIPO_AGREGAR)){
				buffer.write("No se agregado ningun contribuyente al operativo.");
			}else if(opeInvBus.getTipBus().equals(OpeInvBus.TIPO_ELIMINAR)){				
				buffer.write("No se ha eliminado ningun contribuyente del operativo.");
			}
		}else{
			buffer.write("Contribuyente;Cuenta");
		}

		
		for(String datosPersona: listPersonas){
			// verifica si hay que generar otro archivo
			regCounter++;
			if(regCounter == 65534 ){ // - incluyendo a las filas del encabezado y considera que regCounter arranca en cero
				
				// cierra el buffer, genera una nueva planilla
				if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName + " ctdResultados: " + regCounter);
				buffer.close();				
				numeroArchivo++;
				buffer = generarCabecera4Report(new FileWriter(fileDir+fileName+"_"+numeroArchivo+extensionArchivo, false));		
				regCounter = 3; // reinicio contador 
			}else{
				// crea una nueva linea
				buffer.newLine();
			}
			
			// escribe la fila				
			buffer.write(datosPersona);
		}
		
		if(opeInvBus.getTipBus().equals(OpeInvBus.TIPO_ELIMINAR)){	
			// agrega la lista de personas no eliminadas por tener acta
			buffer.newLine();
			buffer.newLine();
			buffer.newLine();
			buffer.write("Lista de Personas que no se eliminaron por tener Actas existentes:");
			buffer.newLine();
			
			for(String datosPersona: listPersonasNoElim){
				// verifica si hay que generar otro archivo
				regCounter++;
				if(regCounter == 65534 ){ // - incluyendo a las filas del encabezado y considera que regCounter arranca en cero
					
					// cierra el buffer, genera una nueva planilla
					if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName + " ctdResultados: " + regCounter);
					buffer.close();				
					numeroArchivo++;
					buffer = generarCabecera4Report(new FileWriter(fileDir+fileName+"_"+numeroArchivo+extensionArchivo, false));		
					regCounter = 3; // reinicio contador 
				}else{
					// crea una nueva linea
					buffer.newLine();
				}
				
				// escribe la fila				
				buffer.write(datosPersona);
			}

		}
	
		
		buffer.close();			
		if(log.isDebugEnabled()) log.debug("Archivos excel generados:"+ numeroArchivo+1);	
	
		if(opeInvBus.getTipBus().equals(OpeInvBus.TIPO_AGREGAR)){
			opeInvBus.getCorrida().addOutputFile("Reporte de seleccion masiva", "Contiene los contribuyentes que fueron agregados al operativo de investigación", fileDir+fileName+extensionArchivo);
		}else if(opeInvBus.getTipBus().equals(OpeInvBus.TIPO_ELIMINAR)){				
			opeInvBus.getCorrida().addOutputFile("Reporte de eliminación masiva", "Contiene los contribuyentes que fueron eliminados del operativo de investigación", fileDir+fileName+extensionArchivo);
		}

		log.debug("generarReporte - exit");
	}
	
	private BufferedWriter generarCabecera4Report(FileWriter fileWriter) throws IOException{
		
		BufferedWriter buffer = new BufferedWriter(fileWriter);
		if(opeInvBus.getTipBus().equals(OpeInvBus.TIPO_AGREGAR)){
			buffer.write("Agregar Masivo - Operativo de Investigacion:;"+opeInvBus.getOpeInv().getDesOpeInv());						
			buffer.newLine();					
			buffer.write("Fecha: "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK));			
			buffer.newLine();
			buffer.write("Cant. contribuyentes agregados al operativo: "+cantContrProcesados);			
			buffer.newLine();		
		}else if(opeInvBus.getTipBus().equals(OpeInvBus.TIPO_ELIMINAR)){
			buffer.write("Eliminar Masivo - Operativo de Investigacion:;"+opeInvBus.getOpeInv().getDesOpeInv());
			buffer.newLine();		
			buffer.write("Fecha: "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK));			
			buffer.newLine();			
			buffer.write("Cant. contribuyentes eliminados del operativo: "+cantContrProcesados);			
			buffer.newLine();
			buffer.write("Cant. contribuyentes NO eliminados del operativo por tener Actas: "+listPersonasNoElim.size());			
			buffer.newLine();
		}

		return buffer;
	}
	

	/**
	 * Genera la parte FROM y WHERE de la query.<br>
	 * Para cada parametro del objImp y del contribuyente genera un inner join a la tabla objImpAtrVal o 
	 * conAtrVal ya que se deben cumplir TODOS los parametros
	 * @param param
	 * @param listPersona
	 * @param listIdsContribuyentes
	 * @param listIdsContrOtroOpeInv
	 * @throws Exception
	 */
	private void generarQuery(String[] param,List<Persona> listPersona, 
			List<Long>listIdsContrOtroOpeInv) throws Exception{
		log.debug("generarQuery - enter");
		from =" cuentaTitular.id FROM pad_cuentatitular cuentaTitular inner join pad_cuenta cuenta on " +
																		"cuentaTitular.idcuenta=cuenta.id ";		
		where = " WHERE cuenta.idRecurso="+Recurso.getDReI().getId();
		
		// NroCuenta
		if(!StringUtil.isNullOrEmpty(param[20])){			
			where += " AND cuenta.numeroCuenta like'"+param[20]+"%'";
		}
		
		// parametros del objImp
		
			// localesEnOtrasProv
			aux4GenQueryObjImp(param[7], "LocProv", Atributo.COD_LOCALES_EN_OTRAS_PROV);		
			
			//locFueRosEnSfe
			aux4GenQueryObjImp(param[8], "LocSfe", Atributo.COD_LOCFUEROSENSFE);
		
			//ubicaciones
			aux4GenQueryObjImp(param[9], "Ubic", Atributo.COD_UBICACIONES);
		
			//cantPersonal
			aux4GenQueryObjImp(param[10], "CantPers", Atributo.COD_CANT_PERSONAL_SIAT);
			
			//nroComercio
			aux4GenQueryObjImp(param[12], "NroComer", Atributo.COD_NROCOMERCIO);
		
			//radio
			aux4GenQueryObjImp(param[15], "Radio", Atributo.COD_RAD_RED_TRIB);
		
			// cataDesde y hasta
			if(!StringUtil.isNullOrEmpty(param[13]) || !StringUtil.isNullOrEmpty(param[14])){
				from +=" inner join pad_objimpatrval objImpAtrValCata on cuenta.idobjimp=objImpAtrValCata.idobjimp" +
				" inner join def_tipobjimpatr tipObjImpAtrCata on objImpAtrValCata.idtipobjimpatr=tipObjImpAtrCata.id ";

				if(!StringUtil.isNullOrEmpty(param[13]))
					where += (StringUtil.isNullOrEmpty(where)?" WHERE ":" and ")+" tipObjImpAtrCata.idatributo="+Atributo.getByCodigo(Atributo.COD_CATASTRAL).getId()+"" +
					" and objImpAtrValCata.strvalor like '"+param[13].trim()+"%' ";
				
				if(!StringUtil.isNullOrEmpty(param[14]))
					where += (StringUtil.isNullOrEmpty(where)?" WHERE ":" and ")+" tipObjImpAtrCata.idatributo="+Atributo.getByCodigo(Atributo.COD_CATASTRAL).getId()+"" +
					" and objImpAtrValCata.strvalor like '"+param[14].trim()+"%' ";
			}
						
			//rubros
			if(!StringUtil.isNullOrEmpty(param[11])){
				String[] idsRubros = param[11].split(",");
				int idx =1;
				for(String idRubro: idsRubros){
					aux4GenQueryObjImp(idRubro, "Rubro"+idx, Atributo.COD_RUBRO);					
					idx++;
				}
			}

		// parametros del contribuyente

			if(!StringUtil.isNullOrEmpty(param[2]) || !StringUtil.isNullOrEmpty(param[0]) ||
					(listPersona !=null && !listPersona.isEmpty()))
				from +=" inner join pad_contribuyente contribuyente on cuentaTitular.idcontribuyente=contribuyente.id";
				
			// CER
			if(!StringUtil.isNullOrEmpty(param[2])){
				from +=" inner join pad_conatrval conAtrVal on conAtrVal.idcontribuyente=contribuyente.id";
				where +=" and conAtrVal.idconatr=3 and conAtrVal.valor="+param[2]+				
				// agrega la vigencia
				" and (conAtrVal.fechaDesde is null or conAtrVal.fechaDesde <=TO_DATE('"+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK)+
					"', '%d/%m/%Y') )"+
				" and (conAtrVal.fechaHasta is null or conAtrVal.fechaHasta >TO_DATE('"+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK)+
					"', '%d/%m/%Y') )";
			}
			
			//nroIsib
			if(!StringUtil.isNullOrEmpty(param[0]))
				where += " and contribuyente.nroisib like '"+param[0]+"%'";	
				
			// idsPersonas (buscadas por CUIT, si se ingreso)	
			if(listPersona !=null && !listPersona.isEmpty()){
				where +=" and contribuyente.idpersona IN(";
				int idx =1;
				for(Persona persona: listPersona){
					where += persona.getId()+ (idx==listPersona.size()?"":",");
					idx++;
				}
				where +=")";
			}
			
		List<Long> listIdsContribuyentes= opeInvBus.getOpeInv().getlistIdsContribuyentes();
		
		if(opeInvBus.getTipBus().equals(OpeInvBus.TIPO_AGREGAR)){
			// filtra los que son titulares ppales de la cuenta
			where +=" AND cuentaTitular.estitularprincipal=1 ";

			// excluye las cuentas seleccionadas del operativo (la que este seleccionada, de cada opeInvCon)
			List<Long> listIdsCuentasSelec = opeInvBus.getOpeInv().getListIdsCuentasSelec();
			if(listIdsCuentasSelec.size()>0)
				where +=" and cuentaTitular.idCuenta NOT IN("+ListUtil.getStringList(listIdsCuentasSelec)+") ";

			// excluye los contribuyentes que ya existen en el operativo
			if(listIdsContribuyentes!=null && !listIdsContribuyentes.isEmpty())
				where +=" and cuentaTitular.idContribuyente NOT IN("+
															ListUtil.getStringList(listIdsContribuyentes)+")";

		}else if(opeInvBus.getTipBus().equals(OpeInvBus.TIPO_ELIMINAR)){
			// filtra por los contribuyentes que ya existen en el operativo
			if(listIdsContribuyentes!=null && !listIdsContribuyentes.isEmpty())
				where +=" and cuentaTitular.idContribuyente IN("+
															ListUtil.getStringList(listIdsContribuyentes)+")";			
		}

		// incluye los contribuyentes que esten en otro/s operativo/s, si se selecciono un estado
		if(listIdsContrOtroOpeInv!=null && !listIdsContrOtroOpeInv.isEmpty())
			where +=" and cuentaTitular.idContribuyente IN("+
														ListUtil.getStringList(listIdsContrOtroOpeInv)+") ";
		query = from+where;
		
		log.debug("query generada:"+query);
		log.debug("generarQuery - exit");
	}
	
	/**
	 * Actualiza las cadenas from y where para los valores pasados como paramtero(del objImp)
	 * @param valor
	 * @param alias
	 * @param codAtr
	 * @throws Exception
	 */
	private void aux4GenQueryObjImp(String valor, String alias, String codAtr) throws Exception{
		if(!StringUtil.isNullOrEmpty(valor)){
			
			String aliasTabla = "objImpAtrVal"+alias;
			
			from +=" inner join pad_objimpatrval " + aliasTabla + " on cuenta.idobjimp="+aliasTabla+
				".idobjimp" + " inner join def_tipobjimpatr tipObjImpAtr" + alias + " on " + aliasTabla
					+ ".idtipobjimpatr=tipObjImpAtr" + alias + ".id ";
			where += " and tipObjImpAtr" + alias +".idatributo="+Atributo.getByCodigo(codAtr).getId()+"" +
				" and "+aliasTabla+".strvalor='"+valor.trim()+"'"+
				
				// agrega la vigencia
				" and ("+aliasTabla+".fechaDesde is null or "+aliasTabla+".fechaDesde <=TO_DATE('"+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK)+
					"', '%d/%m/%Y') )"+
				" and ("+aliasTabla+".fechaHasta is null or "+aliasTabla+".fechaHasta >TO_DATE('"+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK)+
					"', '%d/%m/%Y') )";
		}
	}
	
	private void procesarListIdsCuentaTitular(List<Integer> listIdsCuentaTitular) throws Exception {
		log.debug("procesarListIdsCuentaTitular: enter");
		Session session = SiatHibernateUtil.currentSession();
		int count = 0;
		for(Integer idCuentaTit: listIdsCuentaTitular){
			log.debug("procesando idCuentaTitular:"+idCuentaTit +" ***************************************");
			
			CuentaTitular cuentaTitular = CuentaTitular.getById(idCuentaTit.longValue());
			boolean aplica = true;
			
			// si se ingreso un valor en paramPromedioPagoCta, se calcula el promedio de pagos de la cuenta a la que
			// apunta el idCuentaTitular y si el resultado es >= al ingresado, se la agrega en la seleccion almacenada 
			if(paramPromedioPagoCta!=null){
				Double promedio = calcularPromedio(cuentaTitular.getCuenta());
				if(promedio<paramPromedioPagoCta)
					aplica = false;				
			}
			
			// Se calcula el promedio de pagos del contribuyente, si se ingreso un valor y si paso la validacion anterior
			if(paramPromedioPagoContr!=null && aplica){
				Contribuyente contribuyente = cuentaTitular.getContribuyente();
				Double promedioCta = calcularPromedio(cuentaTitular.getCuenta());
				int cantCuentas = 1;
				List<Cuenta> listCueVig4Titular = contribuyente.getListCueVig4Titular(Recurso.getDReI(), 
																					 cuentaTitular.getCuenta());
				log.debug("cant cuentas vig4titular:"+listCueVig4Titular.size());
				for(Cuenta cuenta:listCueVig4Titular){
					// Si tiene los mismos titulares actualiza la cantCuentas, calcula y acumula el promedio
					if(CuentaTitular.compararListaTit(cuentaTitular.getCuenta().getListCuentaTitularVigentes(
												  new Date()),cuenta.getListCuentaTitularVigentes(new Date()))){
						cantCuentas++;
						promedioCta += calcularPromedio(cuenta);
					}else
						log.debug("resultado de la comparacion: false");
				}
				
				promedioCta = promedioCta / cantCuentas;
				log.debug("promedioCta:"+promedioCta+"     paramPromedioPagoContr:"+paramPromedioPagoContr);
				// Si el promedio obtenido es >= al ingresado por pantalla, aplica
				if(promedioCta<paramPromedioPagoContr)					
					aplica=false;
			}
			
			log.debug("aplica:"+aplica);
			
			if(aplica){
				boolean contieneContr = opeInvBus.getOpeInv().contieneContribuyente(cuentaTitular.getIdContribuyente());
				if(opeInvBus.getTipBus().equals(OpeInvBus.TIPO_AGREGAR) && !contieneContr)
					crearOpeInvCon(cuentaTitular);			
				
				else if(opeInvBus.getTipBus().equals(OpeInvBus.TIPO_ELIMINAR))
					eliminarOpeInvCon(cuentaTitular);

			}
			
			count++;
			if(count==10){
				count = 0;
				session.flush();
			}
		}
		log.debug("procesarListIdsCuentaTitular: exit");
	}

	private void eliminarOpeInvCon(CuentaTitular cuentatitular) throws Exception{
		log.debug("eliminarOpeInvCon - enter");
		OpeInvCon opeInvCon = OpeInvCon.getByContrYOpeInv(cuentatitular.getContribuyente(), opeInvBus.getOpeInv());
		if(opeInvCon!=null){
			
			if(opeInvCon.getActaInv()!=null){
				// no la elimina porque tiene un acta
				listPersonasNoElim.add(opeInvCon.getDatosContribuyente()+";"+opeInvCon.getOpeInvConCueCuentaSelec().getCuenta().getNumeroCuenta());
				
			}else{
				//actualiza la lista de personas para el reporte final
				listPersonas.add(opeInvCon.getDatosContribuyente()+";"+opeInvCon.getOpeInvConCueCuentaSelec().getCuenta().getNumeroCuenta());
				
				HisEstOpeInvCon.delete(opeInvCon);
				log.debug("Elimino el historico");
				
				OpeInvConCue.delete(opeInvCon);
				log.debug("Elimino las cuentas");
				
				opeInvBus.getOpeInv().deleteOpeInvCon(opeInvCon);
				log.debug("Elimino el contribuyente del operativo");
			}
			
			cantContrProcesados++;
		}
		log.debug("eliminarOpeInvCon - exit");
	}
	
	private void crearOpeInvCon(CuentaTitular cuentaTitular) throws Exception {
		log.debug("crearOpeInvCon: enter");
		
		Contribuyente contribuyente = cuentaTitular.getContribuyente();

		// crea un opeInvCon nuevo
		OpeInvCon opeInvCon = new OpeInvCon();
		opeInvCon.setOpeInv(opeInvBus.getOpeInv());
		opeInvCon.setOpeInvBus(opeInvBus);
		opeInvCon.setIdContribuyente(contribuyente.getId());
		opeInvCon.setEstadoOpeInvCon(EstadoOpeInvCon.getById(EstadoOpeInvCon.ID_SELECCIONADO));
		opeInvCon.setCuenta(cuentaTitular.getCuenta());
		opeInvCon.setDomicilio(cuentaTitular.getCuenta().getDomicilioEnvio());
		
		// carga la persona
		contribuyente.loadPersonaFromMCR();
		opeInvCon.setDatosContribuyente(contribuyente.getPersona().getRepresent());
		
		// lo graba (se crea un historico tambien)
		opeInvBus.getOpeInv().createOpeInvCon(opeInvCon, "Seleccionado según criterio: "+opeInvBus.getParamSel());
		SiatHibernateUtil.currentSession().flush();
		log.debug("Creo la opeInvCon");
		
		// crea un nuevo OpeInvConCue
		crearOpeInvConCue(opeInvCon, cuentaTitular.getCuenta(), true);			
		log.debug("Creo la opeInvConCue");
		
		// Busca si tiene mas cuentas relacionadas y las agrega
		List<CuentaTitular> listCuentaTit = CuentaTitular.getList(contribuyente,
																				cuentaTitular.getCuenta());
		log.debug("Cant de cuentas relacionadas:"+listCuentaTit.size());
		for(CuentaTitular cuentaTit: listCuentaTit){
			// crea un nuevo OpeInvConCue
			crearOpeInvConCue(opeInvCon, cuentaTit.getCuenta(), false);
		}

		//actualiza la lista de personas para el reporte final
		listPersonas.add(contribuyente.getPersona().getRepresent()+";"+cuentaTitular.getCuenta().getNumeroCuenta());
		
		cantContrProcesados++;
		log.debug("crearOpeInvCon: exit");	
	}

	private void crearOpeInvConCue(OpeInvCon opeInvCon, Cuenta cuenta, boolean seleccionada) {
		OpeInvConCue opeInvConCue1 = new OpeInvConCue();
		opeInvConCue1.setOpeInvCon(opeInvCon);
		opeInvConCue1.setCuenta(cuenta);
		opeInvConCue1.setSeleccionada(seleccionada);				
		EfDAOFactory.getOpeInvConCueDAO().update(opeInvConCue1);
	}

	/**
	 * Calcula el promedio entre el total de pagos realizados(deudas canceladas) para la cuenta pasada como parametro y los periodos.<br>
	 * Busca deudas administrativas, judiciales y canceladas, todas canceladas(saldo 0)
	 * @param cuenta
	 * @return
	 */
	private Double calcularPromedio(Cuenta cuenta) {
		log.debug("calcularPromedio: enter - va a buscar en las tablas admin, judicial y cancelada");
		List<Deuda> listDeudas = Deuda.GetListImporteCancelado(cuenta, listIdsRecClaDeu, fecDesdePromPagoCta, 
												   										   fecHastaPromPagoCta);		
		Double importeTotal = 0D;
		int cantPeriodos = 0;
		
		for(Deuda deuda: listDeudas){
			log.debug("idDeuda:"+ deuda.getId()+"     clasif de la deuda:"+deuda.getRecClaDeu().getDesClaDeu());
			if(deuda.getRecClaDeu().getId().equals(RecClaDeu.ID_DDJJ_ORIGINAL))
				cantPeriodos++;
			importeTotal +=deuda.getImporte();
		}
		
		double promedio = (cantPeriodos>0?importeTotal/cantPeriodos:0);
		log.debug("calcularPromedio: exit  - valor:"+promedio);
		return promedio;
	}

	
	public boolean validate(AdpRun adpRun) throws Exception {
		return false;
	}

}
