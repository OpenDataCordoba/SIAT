//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.emision;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.emi.buss.bean.ResLiqDeu;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.buss.bean.DeudaJudicial;
import ar.gov.rosario.siat.gde.buss.bean.Procurador;
import ar.gov.rosario.siat.gde.buss.dao.ConvenioDAO;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.dao.CuentaDAO;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.adpcore.AdpRunState;
import coop.tecso.adpcore.AdpWorker;
import coop.tecso.demoda.iface.helper.DemodaTimer;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Procesa las cuentas y sus deudas a partir de una fecha.
 * Genera tres archivos con detalle de:  
 *  Deudas por cuenta;
 *  Cuenta - Nom Procurador (si tiene deuda en procuradores)
 *  y Cuenta - Nro Convenios
 * @author Tecso Coop. Ltda.
 */
public class ResLiqDeuAlfaxWorker implements AdpWorker {

	private static Logger log = Logger.getLogger(ResLiqDeuAlfaxWorker.class);
	
	private HashMap<Long,String> cacheProcuradores = new HashMap<Long,String>();
	private HashMap<Long,String> cacheConvenios = new HashMap<Long,String>();
	
	public void reset(AdpRun adpRun) throws Exception {
	}

	public void cancel(AdpRun adpRun) throws Exception {
	}

	public void execute(AdpRun adpRun) throws Exception {
		// verfica numero paso y estado en adprun,
		// llama a cada metodo segun el numero de paso
		Long pasoActual = adpRun.getPasoActual();
		
		if (pasoActual.equals(1L)) {
			this.ejecutarPaso1(adpRun);		
		}
	}

	/** 
	 * @param adpRun
	 * @throws Exception
	 */
	public void ejecutarPaso1(AdpRun run) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		DemodaTimer dt = new DemodaTimer();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String nombreFileCorrDeudaAdmin = "Deuda Admin TGI";
		String descFileCorrDeudaAdmin = "Archivo de periodos adeudados a la fecha de vencimiento por cuenta de TGI";
				
		String nombreFileCorrDeudaJudicial = "Deuda Judicial TGI";
		String descFileCorrDeudaJudicial = "Archivo de Dedua TGI en Via Judicial";
				
		String nombreFileCorrConveniosVigentes = "Convenios Vigentes TGI";
		String descFileCorrConveniosVigentes = "Archivo de Convenios Vigentes para cuenta de TGI";
		
		try {
			
			Session session = SiatHibernateUtil.currentSession();
			
			String outputDir = run.getProcessDir(AdpRunDirEnum.SALIDA);

			// Obtenemos los parametros de Adp
			Long idResLiqDeu = Long.parseLong(run.getParameter(ResLiqDeu.ADP_PARAM_ID));
			ResLiqDeu resLiqDeu = ResLiqDeu.getById(idResLiqDeu);
			Recurso recurso = resLiqDeu.getRecurso();
			Long idRecurso = recurso.getId();
			// Fecha de Analisis
			Date fechaVto = resLiqDeu.getFechaAnalisis();
			
			Long firstResult = 0L;
			Long maxResults = 1000L; 

			AdpRun.changeRunMessage("Inicializando caches", 60);
			
			// Cargamos el cache de procuradores
			//idProcurador -> nom_procurador pic x(30). | dir_proc pic x(30). |  tel_proc pic 9(10).			
			List<Procurador> listProcuradores = Procurador.getList(); 
			
			log.debug("Cargando cache de procuradores ...");
			for (Procurador procurador:listProcuradores){
				String sep = "|";
				String dscProc = formatString(procurador.getDescripcion(), 30) + sep +
								formatString(procurador.getDomicilio(), 30) + sep +
								formatString(procurador.getTelefono(), 30); 
				
				cacheProcuradores.put(procurador.getId(), dscProc);
				log.debug("procurador: " + procurador.getId() + " -> " + dscProc);
			}			
			listProcuradores = null;
			
			
			// Cargamos el cache de convenios
			// idConvenio -> 	sist_plan   pic 99 | nro_plan  pic 9(10) 			
			boolean existenConvenios = true;
			
			ConvenioDAO convenioDAO = GdeDAOFactory.getConvenioDAO();
			
			while (existenConvenios){
				
				List<Convenio> listConvenios = convenioDAO.getListVigentes(firstResult, maxResults); 
				existenConvenios = (listConvenios.size() > 0);
				
				log.debug("Cargando cache de convenios ...");
				for (Convenio convenio:listConvenios){
					String sep = "|";
					String dscConvenio = formatLong(convenio.getSistema().getNroSistema(), 2) + sep +
										formatInteger(convenio.getNroConvenio(), 10); 
					
					cacheConvenios.put(convenio.getId(), dscConvenio);
					log.debug("convenio: " + convenio.getId() + " -> " + dscConvenio);
				}			
				listConvenios = null;
				
				firstResult += maxResults;
				if (existenConvenios){
					session.flush();
					session.clear();
				}
			}
			
			firstResult = 0L;

			// los nomnbre de los archivos que vamos a generar
			String fileName1 = "deuda_admin";
			String pathCompleto1 = outputDir + File.separator + fileName1 + "_" + resLiqDeu.getId();
			
			String fileName2 = "deuda_judicial";
			String pathCompleto2 = outputDir + File.separator + fileName2 + "_" + resLiqDeu.getId();
			
			String fileName3 = "convenios_vigentes";
			String pathCompleto3 = outputDir + File.separator + fileName3 + "_" + resLiqDeu.getId();
			
			// buffer de escritura para el archivo1 
			BufferedWriter bufferDeudaAdmin = new BufferedWriter(new FileWriter(pathCompleto1, false));
			
			BufferedWriter bufferDeudaJudicial = new BufferedWriter(new FileWriter(pathCompleto2, false));
			
			BufferedWriter bufferConveniosVigentes = new BufferedWriter(new FileWriter(pathCompleto3, false));

			// Carga un cache de procuradores
			// Carga un cache con todos los convenios vigentes
			// Iterar la lista de Cuentas de manera paginada
				// Obtener las deudas administrativas
					// Si cumple las condiciones, escribir archivos deuda_admin
					// Si posee convenio,  agregarlo la mapa de convenios, escribir
			
				// Obtener las deuda judiciales 
					// Por cada procurador, agregarlo al mapa de procurador, escribir 
					// Si posee convenio, agregarlo al mapa de convenios, escribir
			
			
			boolean contieneCuentas = true;

			CuentaDAO cuentaDAO = PadDAOFactory.getCuentaDAO();
			
			// Calculamos el total de las cuentas a procesar
			long totalCuentas = cuentaDAO.getCountByRecurso(idRecurso);
			// Cuentas procesadas hasta el momento
			long c= 0; 
			while (contieneCuentas){
				SiatHibernateUtil.closeSession();
				session = SiatHibernateUtil.currentSession();
				

				// obtiene la lista de Resultado de cuentas
				List<Cuenta> listCuenta = cuentaDAO.getListByRecurso(idRecurso, firstResult, maxResults);
					
				contieneCuentas = (listCuenta.size() > 0);
				
				contieneCuentas = false;
				
				if(contieneCuentas){
					for (Cuenta cuenta: listCuenta) {
						AdpRun.changeRunMessage("Cuenta " + (++c) +" de " + totalCuentas, 60);
						
						// idConvenio -> cuenta | sist_plan | nro_plan 
						HashMap<Long,String> conveniosCuenta = new HashMap<Long,String>();  
						// idProcurador -> cuenta | nom_procurador | dir_proc | tel_proc
						HashMap<Long,String> procuradoresCuenta = new HashMap<Long,String>();
						
						// Deuda Administrativa
						List<DeudaAdmin> listDeudaAdminVencida = cuenta.getListDeudaAdminVencidaByCuenta(fechaVto);
					
						for (DeudaAdmin deudaAdmin: listDeudaAdminVencida) {
							eveluarDeduaAdmin(cuenta, deudaAdmin, bufferDeudaAdmin, conveniosCuenta); 
						}
						
						// Deuda Judicial 
						List<DeudaJudicial> listDeudaJudicialVencida = cuenta.getListDeudaJudicialVencidaByCuenta(fechaVto);
					
						for (DeudaJudicial deudaJudicial: listDeudaJudicialVencida) {
							eveluarDeduaJudicial(cuenta, deudaJudicial, procuradoresCuenta, conveniosCuenta); 
						}
						
						//	Escribimos archivo de Deuda Judicial
						if (procuradoresCuenta.size() > 0){
							Set<Long> listKeys = procuradoresCuenta.keySet();

							for (Long idProcurador:listKeys){
								bufferDeudaJudicial.write(procuradoresCuenta.get(idProcurador));
								log.debug("deuda_judicial -> " + procuradoresCuenta.get(idProcurador));
								
								bufferDeudaJudicial.newLine();
							}
						}
						
						// Escribimos archivo de Convenios Vigentes
						if (conveniosCuenta.size() > 0){
							Set<Long> listKeys = conveniosCuenta.keySet();

							for (Long idConvenio:listKeys){
								bufferConveniosVigentes.write(conveniosCuenta.get(idConvenio));
								log.debug("convenios_vigentes -> " + conveniosCuenta.get(idConvenio));
								
								bufferConveniosVigentes.newLine();
							}
						}
						
						conveniosCuenta = null;
						procuradoresCuenta = null;
						session.flush();
						session.clear();
						
					}
					
					log.info(dt.stop("tiempo total en procesar " + firstResult + " cuentas"));
					
					firstResult += maxResults; // incremento el indice del 1er registro
				}
			}
			SiatHibernateUtil.closeSession();
			session = SiatHibernateUtil.currentSession();
			
			bufferDeudaAdmin.close();
			bufferDeudaJudicial.close();
			bufferConveniosVigentes.close();
			
			/* ya generamos los tres archivos, ahora los movemos al /ultimo */
			AdpRun.changeRunMessage("Copiando a directorio 'ultimo'", 0);
			String dirSalida = AdpRun.currentRun().getProcessDir(AdpRunDirEnum.SALIDA);
			File dirUltimo = new File(dirSalida, "ultimo");
			dirUltimo.mkdir();
			AdpRun.deleteDirFiles(dirUltimo);
			AdpRun.copyFile(new File(pathCompleto1), new File(dirUltimo.getPath(), fileName1));
			AdpRun.copyFile(new File(pathCompleto2), new File(dirUltimo.getPath(), fileName2));
			AdpRun.copyFile(new File(pathCompleto3), new File(dirUltimo.getPath(), fileName3));
			
			resLiqDeu = ResLiqDeu.getById(resLiqDeu.getId());
			if (resLiqDeu.hasError()) {
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				String descripcion = resLiqDeu.getListError().get(0).key().substring(1);
				run.changeState(AdpRunState.FIN_ERROR, descripcion, false);
				run.logError(descripcion);
			} else {
				// carga el DeudaAdmin a la Corrida del proceso con el archivo
				resLiqDeu.getCorrida().addOutputFile(nombreFileCorrDeudaAdmin, descFileCorrDeudaAdmin, pathCompleto1);
				
				resLiqDeu.getCorrida().addOutputFile(nombreFileCorrDeudaJudicial, descFileCorrDeudaJudicial, pathCompleto2);
				
				resLiqDeu.getCorrida().addOutputFile(nombreFileCorrConveniosVigentes, descFileCorrConveniosVigentes, pathCompleto3);
				
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				// cambiar estado, incremetar paso, y actualiza el pasoCorrida (que fue creado cuando se activo(esqueduleo)) 
				run.changeState(AdpRunState.FIN_OK, "Se generaron los archivos correctamente", false); 
			}

			log.debug(funcName + ": exit");

		} catch (Exception e) {
			log.error("Service Error: ",  e);
			//session = 
			SiatHibernateUtil.currentSession();
			run.changeState(AdpRunState.FIN_ERROR, e.getMessage() ,false ,e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public boolean validate(AdpRun run) throws Exception {
		return true;
	}
	
	private void eveluarDeduaAdmin(Cuenta cuenta, DeudaAdmin deudaAdmin, BufferedWriter bufferDeudaAdmin,  
			HashMap<Long,String> conveniosCuenta) throws NumberFormatException, IOException, Exception{
		
		// ESCRITURA DE DATOS 
		String sep = "|";
		
		// cuenta
		String nroCuenta = cuenta.getNumeroCuenta();
		
		if (deudaAdmin.getEsExcentaPago()){
			log.debug("Deuda: " + deudaAdmin.getId() + " -> EsExcentaPago");
		} else if (deudaAdmin.getEsConvenio()){
			// Agregamos el convenio al mapa de convenios de la cuenta.
			if (!conveniosCuenta.containsKey(deudaAdmin.getIdConvenio())) {
				String valueConvenio = this.formatLong(Long.valueOf(nroCuenta), 10) + sep +
								cacheConvenios.get(deudaAdmin.getIdConvenio());
				
				conveniosCuenta.put(deudaAdmin.getIdConvenio(), valueConvenio);
			}
			log.debug("Deuda: " + deudaAdmin.getId() + " -> EsConvenio");
		} else if (deudaAdmin.getEsIndeterminada()){
			log.debug("Deuda: " + deudaAdmin.getId() + " -> EsIndeterminada");
		} else if (deudaAdmin.getEsReclamada()){
			log.debug("Deuda: " + deudaAdmin.getId() + " -> EsReclamada");
		} else if (deudaAdmin.getEstaEnAsentamiento() ){
			log.debug("Deuda: " + deudaAdmin.getId() + " -> EstaEnAsentamiento");
		} else {

			// anio
			Long anio = deudaAdmin.getAnio();
			// periodo
			Long periodo = deudaAdmin.getPeriodo();
			
			String strLog = " idDeuda=" + deudaAdmin.getId() + " | idCuenta=" + deudaAdmin.getIdCuenta() + " | ";   

			// cuenta
			bufferDeudaAdmin.write(this.formatLong(Long.valueOf(nroCuenta), 10) + sep); 
			// anio
			bufferDeudaAdmin.write(this.formatLong(anio, 4) + sep);
			strLog += formatLong(anio, 4) + sep;
			// periodo
			bufferDeudaAdmin.write(this.formatLong(periodo, 2));
			strLog += formatLong(periodo, 4);

			log.debug(strLog);
			
			// crea una nueva linea
			bufferDeudaAdmin.newLine();
		
		}		
		
	}
	
	private void eveluarDeduaJudicial(Cuenta cuenta, DeudaJudicial deudaJudicial,   
			HashMap<Long,String> procuradoresCuenta, HashMap<Long,String> conveniosCuenta) throws NumberFormatException, Exception{
		
		//	ESCRITURA DE DATOS 
		String sep = "|";
		
		// cuenta
		String nroCuenta = cuenta.getNumeroCuenta();
		
		// Agregamos al mapa de procudadores
		if (!procuradoresCuenta.containsKey(deudaJudicial.getIdProcurador())){
			// idProcurador -> cuenta | nom_procurador | dir_proc | tel_proc 
			
			String valueProcurador = this.formatLong(Long.valueOf(nroCuenta), 10) + sep +
								cacheProcuradores.get(deudaJudicial.getIdProcurador());

			procuradoresCuenta.put(deudaJudicial.getIdProcurador(), valueProcurador);
		}
		
		if (deudaJudicial.getEsConvenio()){
			// Agregamos el convenio al mapa de convenios de la cuenta.
			if (!conveniosCuenta.containsKey(deudaJudicial.getIdConvenio())) {
				String valueConvenio = this.formatLong(Long.valueOf(nroCuenta), 10) + sep +
								cacheConvenios.get(deudaJudicial.getIdConvenio());
				
				conveniosCuenta.put(deudaJudicial.getIdConvenio(), valueConvenio);
			}
			log.debug("Deuda: " + deudaJudicial.getId() + " -> EsConvenio");
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
	
	
	public String formatInteger(Integer nro, int size){
		
		String resultado = "";
		
		if(nro != null){
			resultado = StringUtil.formatInteger(nro);
		}
		if(resultado.length() > size){
			// disparar excepcion
		}
		
		resultado = StringUtil.completarCaracteresIzq(resultado, size, '0');
		
		return resultado;
		
	}
	
	
	public String formatString(String valor, int size){
		
		if(valor == null) {
			return StringUtil.getStringEspaciosBanco(size);
		}
		
		if(valor.length() > size){
			// disparar excepcion
		}
		
		return StringUtil.completarCaracteresDer(valor, size, ' ');
		
	}
	
	/*
	 deuda_admin
	   cuenta    pic 9(10)
	   anio      pic 9999
	   per       pic 99

	deuda_judicial
	   cuenta           pic 9(10)
	   nom_procurador 	pic x(30).
	   dir_proc         pic x(30).
	   tel_proc         pic 9(10).
	   
	   idProcurador -> cuenta | nom_procurador | dir_proc | tel_proc    
	
	convenios_vigentes:
	   cuenta      pic 9(10)
	   sist_plan   pic 99
	   nro_plan    pic 9(10) 
	   
	   idConvenio -> cuenta | sist_plan | nro_plan 
	 */
}
