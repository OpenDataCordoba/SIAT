//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.rec;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.Asentamiento;
import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import ar.gov.rosario.siat.pro.buss.bean.EstadoCorrida;
import ar.gov.rosario.siat.rec.buss.bean.NovedadRS;
import ar.gov.rosario.siat.rec.buss.bean.ReporteNovedadRSHelper;
import ar.gov.rosario.siat.rec.buss.bean.TipoTramiteRS;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.adpcore.AdpRunState;
import coop.tecso.adpcore.AdpWorker;
import coop.tecso.adpcore.engine.AdpParameter;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;

/**
 *  Proceso de Aplicacion Masiva de Novedades RS
 *  
 * @author tecso
 *
 */
public class NovedadRSWorker implements AdpWorker {

	private static Logger log = Logger.getLogger(NovedadRSWorker.class);
		
	public void execute(AdpRun adpRun) throws Exception {
		// verfica numero paso y estado en adprun,
		// llama a cada metodo segun el numero de paso
		Long pasoActual = adpRun.getPasoActual();
		if (pasoActual.equals(1L)){ // Aplicacion Masiva de Novedades RS
			this.ejecutar(adpRun);
		}
	}

	public void cancel(AdpRun adpRun) throws Exception {
	}

	public void reset(AdpRun adpRun) throws Exception {
	}

	/**
	 * Metodo de validacion que determina si se creara una corrida de ejecucion. 
	 * 
	 */
	public boolean validate(AdpRun adpRun) throws Exception {
		return NovedadRS.existenNovedadRSSinProcesar();
	}

	/**
	 * Aplicacion Masiva de Novedades RS
	 * 
	 * @param run
	 * @throws Exception
	 */
	public void ejecutar(AdpRun adpRun) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			session = SiatHibernateUtil.currentSession();
			
			//-issue #7805: Acomodar deuda.
			// Validamos si existe una corrida de asentamiento de DReI/ETuR en proceso
			if(hasAsentamientoRunning(adpRun)){
				//Si existe, no ejecutamos las NovedadesRS
				log.debug(funcName + ": exit");
				return;
			}
			 
			String ID_TIPO_TRAMITE_RS_PARAM = "idTipoTramiteRS";
			String FECHA_DESDE_PARAM = "fechaDesde";
			String FECHA_HASTA_PARAM = "fechaHasta";
	
			AdpParameter paramIdTipoTramiteRS = adpRun.getAdpParameter(ID_TIPO_TRAMITE_RS_PARAM);
			AdpParameter paramFechaDesde = adpRun.getAdpParameter(FECHA_DESDE_PARAM);
			AdpParameter paramFechaHasta = adpRun.getAdpParameter(FECHA_HASTA_PARAM);
			
			Long idTipoTramiteRS = null;
			TipoTramiteRS tipoTramiteRS = null;
			if(paramIdTipoTramiteRS != null){
				idTipoTramiteRS = NumberUtil.getLong(paramIdTipoTramiteRS.getValue());
				tipoTramiteRS = TipoTramiteRS.getByIdNull(idTipoTramiteRS);
				AdpRun.currentRun().logDebug("Filtro por Tipo Tramite RS aplicado: "+idTipoTramiteRS);
			}
			
			Date fechaDesde = null; 
			if(paramFechaDesde != null){
				fechaDesde= DateUtil.getDate(paramFechaDesde.getValue(), DateUtil.ddSMMSYYYY_MASK);
				AdpRun.currentRun().logDebug("Filtro por Fecha de Novedad Desde aplicado: "+paramFechaDesde.getValue());
			}
			
			Date fechaHasta = null;
			if(paramFechaHasta != null){
				fechaHasta= DateUtil.getDate(paramFechaHasta.getValue(), DateUtil.ddSMMSYYYY_MASK);
				AdpRun.currentRun().logDebug("Filtro por Fecha de Novedad Hasta aplicado: "+paramFechaHasta.getValue());
			}
			
			AdpRun.currentRun().logDebug("Inicio: "+DateUtil.formatDate(new Date(), DateUtil.yyyy_MM_dd_HH_MM_SS_MASK));
			
			// Se inicializan los mapas para la generacion de reportes
			ReporteNovedadRSHelper reporteNovedadRSHelper = new ReporteNovedadRSHelper();
			
			// Cargar lista de Novedades RS en estado Registrado
			List<NovedadRS> listNovedadRS = NovedadRS.getListRegistradoByFiltros(tipoTramiteRS, fechaDesde, fechaHasta);   //.getListRegistrado();
			
			AdpRun.changeRunMessage("Procesando... 0/"+listNovedadRS.size(), 0);
			int cantProcesada = 0;
			for(NovedadRS novedadRS: listNovedadRS){
				// Vuelve a incluir el objeto en la sesion. Esto es debido a los cierres de session que se realizan para mejorar el rendimiento.
				session.refresh(novedadRS);
				
				AdpRun.currentRun().logDebug("Procesando Novedad RS de id: "+novedadRS.getId());
				
				// Se llama al metodo que anula y emite deuda para la cuenta de la novedad.
				String logAcomodarDeuda = novedadRS.acomodarDeuda(true, reporteNovedadRSHelper); 
				
				AdpRun.currentRun().logDebug("Resultado: "+logAcomodarDeuda);
				cantProcesada++;
				if(cantProcesada%10 == 0){
					AdpRun.changeRunMessage("Procesando... "+cantProcesada+"/"+listNovedadRS.size(), 0);
				}
				
				if((cantProcesada)%10==0){
					SiatHibernateUtil.closeSession();
					session = SiatHibernateUtil.currentSession();
				}
			}
			
			// Se generan los reportes y se cargan a la corrida
			tx = session.beginTransaction();
			Corrida corrida = Corrida.getById(adpRun.getId());
			AdpRun.changeRunMessage("Generando Reportes...", 0);
			
			reporteNovedadRSHelper.generarFormularios(adpRun.getProcessDir(AdpRunDirEnum.SALIDA)+File.separator, corrida);
			
			AdpRun.currentRun().logDebug("Fin: "+DateUtil.formatDate(new Date(), DateUtil.yyyy_MM_dd_HH_MM_SS_MASK));
			
			if (corrida.hasError()) {
	            	tx.rollback();
	            	AdpRun.changeRunMessage("Error al generar reportes", 0);
	            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
	            	AdpRun.currentRun().logDebug("Error al generar reportes.");
			} else {
					tx.commit();
					if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
					AdpRun.currentRun().logDebug("Reportes generados correctamente.");
			}
			
			adpRun.changeState(AdpRunState.FIN_OK, "", false);
            log.debug(funcName + ": exit");
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	/**
	 * Verifica si existe una corrida del proceso de asentamiento DReI/ETuR en ejecucion o por comenzar.
	 * 		Si hay, cambia estado de la corrida y loguea la causa -> return true 
	 * 		Sino -> return false
	 * @param adpRun
	 * @return
	 */
	private boolean hasAsentamientoRunning(AdpRun adpRun){
		// validamos si no existe una corrida en ejecucion o por comenzar para el proceso de asentamiento DReI y ETuR
		Long[] idsEstadoCorrida = {EstadoCorrida.ID_EN_ESPERA_COMENZAR,
								   EstadoCorrida.ID_EN_ESPERA_CONTINUAR,
								   EstadoCorrida.ID_PROCESANDO};
		//Servicio Banco para DReI y ETur
		Long[] idsServicioBanco = {ServicioBanco.getByCodigo(ServicioBanco.COD_DREI).getId(),
								   ServicioBanco.getByCodigo(ServicioBanco.COD_ETUR).getId()};
		
		List<Asentamiento> listAsentamiento = BalDAOFactory.getAsentamientoDAO().getListBy(idsEstadoCorrida, idsServicioBanco);
		if (!ListUtil.isNullOrEmpty(listAsentamiento)) {
			String msg = "No procesado por asentamiento/s "+ListUtil.getStringIds(listAsentamiento)+" en curso.";
			adpRun.changeState(AdpRunState.SIN_PROCESAR, msg, false);
			return true;
		}else {
			return false;
		}
	}
}
