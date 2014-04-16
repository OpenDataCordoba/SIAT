//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.procesoMasivo;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.IndeterminadoFacade;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.ProcesoMasivo;
import ar.gov.rosario.siat.gde.buss.bean.SelAlmDeuda;
import ar.gov.rosario.siat.gde.buss.bean.TipProMas;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.SelAlmVO;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.adpcore.AdpRunState;
import coop.tecso.adpcore.AdpWorker;
import coop.tecso.adpcore.engine.AdpParameter;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.model.PlanillaVO;

/**
 * Procesa los mensajes de ADP respecto del directorio 
 * Input de novedades de catastro.
 * @author Tecso Coop. Ltda.
 */
public class ProcesoMasivoWorker implements AdpWorker {

	private static Logger log = Logger.getLogger(ProcesoMasivoWorker.class);
	
	public void reset(AdpRun adpRun) throws Exception {
	}

	public void cancel(AdpRun adpRun) throws Exception {
	}

	public void execute(AdpRun adpRun) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		log.debug("User in Context:" + DemodaUtil.currentUserContext().getUserName());
		
		// verfica numero paso y estado en adprun,
		// llama a cada metodo segun el numero de paso
		Long pasoActual = adpRun.getPasoActual();
		
		if (pasoActual.equals(1L)){ // selecccionAlmacenda incluida
			Long idProcesoMasivo = Long.parseLong(adpRun.getParameter("idProcesoMasivo"));
			Long idSelAlm = Long.parseLong(adpRun.getParameter("idSelAlm"));
			// adpRun representa una corrida.
			
			this.ejecutarPaso1(adpRun, idSelAlm, idProcesoMasivo);
		}
		
		if (pasoActual.equals(2L)){ // asignar procuradores
			Long idProcesoMasivo = Long.parseLong(adpRun.getParameter("idProcesoMasivo"));

			this.ejecutarPaso2(adpRun, idProcesoMasivo);
		}
		if (pasoActual.equals(3L)){ // realizar envio
			AdpParameter paramIdProMas = adpRun.getAdpParameter("idProcesoMasivo");
			
			this.ejecutarPaso3(adpRun, NumberUtil.getLong(paramIdProMas.getValue()));
		}

		if (pasoActual.equals(4L)){ // realizar envio
			AdpParameter paramIdProMas = adpRun.getAdpParameter("idProcesoMasivo");
			
			this.ejecutarPaso4(adpRun, NumberUtil.getLong(paramIdProMas.getValue()));
		}

		if(log.isDebugEnabled()) log.debug(funcName + ": exit");
	}

	public boolean validate(AdpRun adpRun) throws Exception {
		return false;
	}

	/**
	 * Realiza la carga de la seleccion almacenada de deuda a incluir 
	 * @param adpRun
	 * @param idSelAlm
	 * @throws Exception
	 */
	public void ejecutarPaso1(AdpRun adpRun, Long idSelAlm, Long idProcesoMasivo) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Session session = null;
		try {
			//DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			session.beginTransaction();

			SelAlmDeuda selAlmDeuda  = SelAlmDeuda.getById(idSelAlm);
			
			// genero los nuevos SelAlmDet con los nuevos parametros generados
			ProcesoMasivo pm = ProcesoMasivo.getById(idProcesoMasivo);
			selAlmDeuda.cargarSelAlmDetIncluidos(adpRun.getParameters(), pm.getFechaEnvio(), pm);

            if (selAlmDeuda.hasError()) {
				session = SiatHibernateUtil.currentSession();
            	session.getTransaction().rollback(); 
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	
            	adpRun.changeState(AdpRunState.FIN_ERROR, selAlmDeuda.errorString(), false); 
			} else {
				
				session = SiatHibernateUtil.currentSession();
            	session.getTransaction().commit(); 
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				// cambiar estado, incremetar paso, y actualiza el pasoCorrida (que fue creado cuando se activo(esqueduleo)) 
				adpRun.changeState(AdpRunState.ESPERA_CONTINUAR, "Carga de la Selecci�n Almacenada de Deuda a Incluir", false); 
			}
            if(log.isDebugEnabled()) log.debug(funcName + ": exit");
            
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			session = SiatHibernateUtil.currentSession();
		    try { session.getTransaction().rollback(); } catch (Exception ex) {}
			adpRun.changeState(AdpRunState.FIN_ERROR, e.getMessage() ,false ,e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	
	/**
	 * Realiza la inclusion y exclusion de las deudas de las selecciones almacenadas incluidas y excluidas  del envio judicial
	 * @param adpRun
	 * @param idProcesoMasivo
	 * @throws Exception
	 */
	public void ejecutarPaso2(AdpRun adpRun, Long idProcesoMasivo) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Session session = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			//DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			session.beginTransaction();

			// Recargamos el cache de indeterminados. Fuerza la relectura de los indeterminados. 
			IndeterminadoFacade.getInstance().invalidateCache();
			
			ProcesoMasivo ej = ProcesoMasivo.getById(idProcesoMasivo);
			ej.initializeCaches();

			
			//antes de iniciar paso 2. limpiamos cualquier cosa que pueda haber por las idas u vueltas
			ej.deleteListProMasDeuInc();
			ej.deleteListProMasDeuExc();
			ej.getCorrida().deleteListFileCorridaByPaso(2);

			//ahora si, procesamos las deudas seleccionadas
			ej.incluirExcluirDeudasAlmacenadas();
			
			if (!ej.hasError()) {
				// generar archivos de deuda incluida y de deuda excluida
				Corrida corrida = Corrida.getById(adpRun.getId());
				
				// eliminar los FileCorrida de la deuda Incluida de la Corrida
				corrida.deleteListFileCorrida(SelAlmVO.DEUDA_INCLUIDA);
				// eliminar los FileCorrida de las cuotas de convenio Incluida de la Corrida				
				corrida.deleteListFileCorrida(SelAlmVO.CONV_CUOTA_INCLUIDA);
				
				// volver a generar los archivos de Deuda incluida:
				// obtengo el path de salida de las planillas a generar
				String processDir = adpRun.getProcessDir(AdpRunDirEnum.SALIDA);
				log.debug("processDir: " + processDir);
				
				// cargo la lista de Planillas de Deuda y Cuotas de Convenio incluidos
				List<PlanillaVO> listPlanillaVO = ej.exportReportesProMasDeuInc();

				// volver a generar los FileCorrida en base a las planillas
				for (PlanillaVO planillaVO : listPlanillaVO) {
					// cargar un FileCorrida por cada planillaVO 
					String descripcion = planillaVO.getDescripcion();
					String fileName= planillaVO.getFileName();
					Long ctdRegistros = planillaVO.getCtdResultados();
					// usamos el titulo como nombre del FileCorrida que se genera. No podemos usar el fileName, ni la desc 
					corrida.addOutputFile(planillaVO.getTitulo(), descripcion, fileName, ctdRegistros);
					if (corrida.hasError()){
						corrida.addErrorMessages(ej);
					}
				}
				
				// eliminar los FileCorrida de la deuda Excluida de la Corrida
				corrida.deleteListFileCorrida(SelAlmVO.DEUDA_EXCLUIDA);
				// eliminar los FileCorrida de las cuotas de convenio Excluidos de la Corrida				
				corrida.deleteListFileCorrida(SelAlmVO.CONV_CUOTA_EXCLUIDA);
				
				// volver a generar los archivos de Deuda excluida:
				// obtengo el path de salida de las planillas a generar
				
				// cargo la lista de Planillas de Deuda excluida y Cuotas de Convenios excluidos
				listPlanillaVO = ej.exportReportesProMasDeuExc();

				// volver a generar los FileCorrida en base a las planillas
				for (PlanillaVO planillaVO : listPlanillaVO) {
					// cargar un FileCorrida por cada planillaVO 
					String descripcion = planillaVO.getDescripcion();
					String fileName= planillaVO.getFileName();
					Long ctdRegistros = planillaVO.getCtdResultados();
					// usamos el titulo como nombre del FileCorrida que se genera. No podemos usar el fileName, ni la desc 
					corrida.addOutputFile(planillaVO.getTitulo(), descripcion, fileName, ctdRegistros);
					if (corrida.hasError()){
						corrida.addErrorMessages(ej);
					}
				}
			}

            if (ej.hasError()) {
				session = SiatHibernateUtil.currentSession();
            	session.getTransaction().rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
            	adpRun.changeState(AdpRunState.FIN_ERROR, ej.errorString(), false); 
			} else {
				session = SiatHibernateUtil.currentSession();
				session.getTransaction().commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				// cambiar estado, incremetar paso, y actualiza el pasoCorrida (que fue creado cuando se activo(esqueduleo)) 
				adpRun.changeState(AdpRunState.ESPERA_CONTINUAR, "Inclusion y Exclusion de Deudas y Convenios de los Detalles de las Selecciones Almacenadas", true);
			}
            if(log.isDebugEnabled()) log.debug(funcName + ": exit");
            
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			session = SiatHibernateUtil.currentSession();
			if (session.getTransaction().isActive()) {
				session.getTransaction().rollback();
			}
			adpRun.changeState(AdpRunState.FIN_ERROR ,e.getMessage() ,false ,e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	/**
	 * Realiza el envio de deudas a procuradores del envio judicial
	 * @param adpRun
	 * @param idProcesoMasivo
	 * @throws Exception
	 */
	public void ejecutarPaso3(AdpRun adpRun, Long idProcesoMasivo) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			
			//DemodaUtil.setCurrentUserContext(userContext);
		    SiatHibernateUtil.currentSession().beginTransaction();

			// Recargamos el cache de indeterminados. Fuerza la relectura de los indeterminados. 
			IndeterminadoFacade.getInstance().invalidateCache();

			log.info("ProcesoMasivo: Comienza ProcesoMasivo: idProcesoMasivo=" + idProcesoMasivo);
			ProcesoMasivo ej = ProcesoMasivo.getById(idProcesoMasivo);
			log.info("ProcesoMasivo: Cargando Caches");
			ej.initializeCaches();
			
			String processDir = AdpRun.getRun(ProcesoMasivo.getById(idProcesoMasivo).getCorrida().getId()).getProcessDir(AdpRunDirEnum.SALIDA);
			if (ej.getTipProMas().getId().equals(TipProMas.ID_PRE_ENVIO_JUDICIAL)) {
				ej.realizarPreEnvio(processDir);
			} else if (ej.getTipProMas().getId().equals(TipProMas.ID_RECONFECCION)) {
				ej.realizarPreEnvio(processDir);
			} else if (ej.getTipProMas().getId().equals(TipProMas.ID_ENVIO_JUDICIAL)) {
				ej.realizarEnvio(processDir);
			} else {
				throw new Exception("Tipo de envio desconocido: idTipProMas=" + ej.getTipProMas().getId() + " idProcesoMasivo=" + ej.getId());
			}
			
			if (ej.hasError()) {
				String errMessage = ej.getListError().get(0).key();
				if (errMessage.startsWith("&")) errMessage = errMessage.substring(1);
            	adpRun.changeState(AdpRunState.FIN_ERROR, errMessage, false);
			
				
				/* fedel: quitamos la vuelta a atras porque ahora soporta reprocesamiento y vamos a manejar el error de esa manera.
				 * ademas la vuelta a atras no soporta los volumenes del envio judicial
				 * 
				log.info("ProcesoMasivo: Se detecto error, retrocediendo procesmiento.");
				if(log.isDebugEnabled()){log.debug("errores encontrados en la realizacion del envio");}
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				adpRun.changeMessage("Se detecto un error. Comienza vuelta a atras del envio.");
				
            	// cierro la sesion para mejorar la performance y volvemos a abrir la transaccion
				SiatHibernateUtil.currentSession().getTransaction().rollback();
				SiatHibernateUtil.closeSession();
				SiatHibernateUtil.currentSession().beginTransaction();
				 
				// limpia la realizacion del envio.
				this.limpiarRealizarEnvio(adpRun, idProcesoMasivo);
            	adpRun.changeState(AdpRunState.FIN_ERROR, "Las deudas han sido reestablecidas a Deuda Administrativa.", false);

				// No tiene en cuenta errores en la limpieza. Realiza Commit.
				SiatHibernateUtil.currentSession().getTransaction().commit();
				log.info("ProcesoMasivo: Finalizado con Error");
				*/
			} else {
				SiatHibernateUtil.currentSession().getTransaction().commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				// cambiar estado, incremetar paso, y actualiza el pasoCorrida (que fue creado cuando se activo(esqueduleo)) 
				adpRun.changeState(AdpRunState.FIN_OK, "Paso finalizado con exito", true);

            	// cierro la sesion para mejorar la performance y volvemos a abrir la transaccion				
				SiatHibernateUtil.closeSession();
				SiatHibernateUtil.currentSession().beginTransaction(); 
				SiatHibernateUtil.currentSession().refresh(ej);
				
				// Eliminacion de los SelAlmDet de la SelAlmInc y SelAlmExc del procesoMasivo. 
				log.info("ProcesoMasivo: Eliminado lista de seleccion almacenada.");
				if(log.isDebugEnabled()) log.debug("Borrado de los SelAlmDet de la SelAlmInc y SelAlmExc del ProcesoMasivo");
				ej.deleteListSelAlmDet();
				
				// No tiene en cuenta errores en el borrado de los SelAlmDet.
				SiatHibernateUtil.currentSession().getTransaction().commit();
				log.info("ProcesoMasivo: Finalizado con Exito");
			}
			
			if(log.isDebugEnabled()) log.debug(funcName + ": exit");
            
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			Transaction tx = SiatHibernateUtil.currentSession().getTransaction();
			try { if(tx != null) tx.rollback(); } catch (Exception ex) {}
			
			/* fedel: quitamos la opcion de limpiarEnvio, ver mas arriba comentario. 
        	// volvemos a abrir la transaccion
			SiatHibernateUtil.currentSession().beginTransaction();
			 
			// limpia la realizacion del envio.
			this.limpiarRealizarEnvio(adpRun, idProcesoMasivo);

			// No tiene en cuenta errores en la limpieza. Realiza Commit.
			SiatHibernateUtil.currentSession().getTransaction().commit();
			*/
			
			adpRun.changeState(AdpRunState.FIN_ERROR, "Se produjo un error sin manejar: " + e.getMessage() ,false ,e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	private void ejecutarPaso4(AdpRun adpRun, Long idProcesoMasivo) {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {			
			//DemodaUtil.setCurrentUserContext(userContext);
		    SiatHibernateUtil.currentSession().beginTransaction();

			log.info("ProcesoMasivo: Comienza ProcesoMasivo: idProcesoMasivo=" + idProcesoMasivo);
			ProcesoMasivo ej = ProcesoMasivo.getById(idProcesoMasivo);
			log.info("ProcesoMasivo: Cargando Caches");
			ej.initializeCaches();
			
			if (ej.getTipProMas().getId().equals(TipProMas.ID_ENVIO_JUDICIAL)) {
				GdeDAOFactory.getProcesoMasivoDAO().generarPlanillasConstancias(ej);
			} else {
				throw new Exception("Tipo de envio desconocido: idTipProMas=" + ej.getTipProMas().getId() + " idProcesoMasivo=" + ej.getId());
			}
			
			if (ej.hasError()) {
				String errMessage = ej.getListError().get(0).key();
				if (errMessage.startsWith("&")) errMessage = errMessage.substring(1);
            	adpRun.changeState(AdpRunState.FIN_ERROR, errMessage, false);
			} else {
				SiatHibernateUtil.currentSession().getTransaction().commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				// cambiar estado, incremetar paso, y actualiza el pasoCorrida (que fue creado cuando se activo(esqueduleo)) 
				adpRun.changeState(AdpRunState.FIN_OK, "Paso finalizado con exito", true);

            	// cierro la sesion para mejorar la performance y volvemos a abrir la transaccion				
				SiatHibernateUtil.closeSession();
				SiatHibernateUtil.currentSession().beginTransaction(); 
				SiatHibernateUtil.currentSession().refresh(ej);
				
				// No tiene en cuenta errores en el borrado de los SelAlmDet.
				SiatHibernateUtil.currentSession().getTransaction().commit();
				log.info("ProcesoMasivo: Finalizado con Exito");
			}
			
			if(log.isDebugEnabled()) log.debug(funcName + ": exit");
            
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			Transaction tx = SiatHibernateUtil.currentSession().getTransaction();
			try { if(tx != null) tx.rollback(); } catch (Exception ex) {}			
			adpRun.changeState(AdpRunState.FIN_ERROR, "Se produjo un error sin manejar: " + e.getMessage() ,false ,e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	/**
	 * Limpia la realizacion del envio.
	 * 
	 * @param adpRun
	 * @param idProcesoMasivo
	 * @throws Exception
	 */
	public void limpiarRealizarEnvio(AdpRun adpRun, Long idProcesoMasivo) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			
			ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(idProcesoMasivo);
			procesoMasivo.initializeCaches();
			
			//String processDir = AdpRun.getRun(ProcesoMasivo.getById(idProcesoMasivo).getCorrida().getId()).getProcessDir(AdpRunDirEnum.SALIDA);
			log.debug("TipProMas: " + procesoMasivo.getTipProMas().getId());
			if (procesoMasivo.getTipProMas().getId().equals(TipProMas.ID_PRE_ENVIO_JUDICIAL)) {
				//procesoMasivo.volverAtrarRealizarPreEnvio(processDir);
			} else if (procesoMasivo.getTipProMas().getId().equals(TipProMas.ID_RECONFECCION)) {
				//procesoMasivo.volverAtrasRealizarPreEnvio(processDir);
			} else if (procesoMasivo.getTipProMas().getId().equals(TipProMas.ID_ENVIO_JUDICIAL)) {
				procesoMasivo.limpiarRealizarEnvio();
			} else {
				throw new Exception("Tipo de envio desconocido: idTipProMas=" + procesoMasivo.getTipProMas().getId() + " idProcesoMasivo=" + procesoMasivo.getId());
			}
			
			if(log.isDebugEnabled()) log.debug(funcName + ": exit");
			return;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
		} 
	}

	
	
}
