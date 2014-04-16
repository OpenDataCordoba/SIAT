//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.buss.service;

/**
 * Implementacion de servicios del submodulo Impresion 
 * del modulo Emision
 * @author tecso
 */

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.def.buss.bean.DomAtrVal;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.iface.model.AtributoVO;
import ar.gov.rosario.siat.def.iface.model.DomAtrValVO;
import ar.gov.rosario.siat.def.iface.model.GenericAtrDefinition;
import ar.gov.rosario.siat.def.iface.model.RecursoDefinition;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.emi.buss.bean.EmiImpresionManager;
import ar.gov.rosario.siat.emi.buss.bean.ImpMasDeu;
import ar.gov.rosario.siat.emi.buss.dao.EmiDAOFactory;
import ar.gov.rosario.siat.emi.iface.model.ImpMasDeuAdapter;
import ar.gov.rosario.siat.emi.iface.model.ImpMasDeuSearchPage;
import ar.gov.rosario.siat.emi.iface.model.ImpMasDeuVO;
import ar.gov.rosario.siat.emi.iface.model.ProcesoImpMasDeuAdapter;
import ar.gov.rosario.siat.emi.iface.service.IEmiImpresionService;
import ar.gov.rosario.siat.frm.iface.model.FormatoSalida;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import ar.gov.rosario.siat.pro.buss.bean.EstadoCorrida;
import ar.gov.rosario.siat.pro.buss.bean.FileCorrida;
import ar.gov.rosario.siat.pro.buss.bean.PasoCorrida;
import ar.gov.rosario.siat.pro.iface.model.EstadoCorridaVO;
import ar.gov.rosario.siat.pro.iface.model.FileCorridaVO;
import ar.gov.rosario.siat.pro.iface.model.PasoCorridaVO;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.UserContext;

public class EmiImpresionServiceHbmImpl implements IEmiImpresionService {
	private Logger log = Logger.getLogger(EmiImpresionServiceHbmImpl.class);
	
	// ---> ABM Impresion Masiva de Deuda
	@SuppressWarnings("unchecked")
	public ImpMasDeuSearchPage getImpMasDeuSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ImpMasDeuSearchPage impMasDeuSearchPage = new ImpMasDeuSearchPage();
			
			// Seteamos la lista de recursos
			List<Recurso> listRecurso = Recurso.getListWithImpresionMasiva();
			// Por defecto seteamos la opcion TODOS
			impMasDeuSearchPage.getImpMasDeu().getRecurso().setId(-1L);
			
			impMasDeuSearchPage.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Recurso item: listRecurso){				
				impMasDeuSearchPage.getListRecurso().add(item.toVOWithCategoria());							
			}

			// Seteamos la lista de Estado
			List<EstadoCorrida> listEstadoCorrida = EstadoCorrida.getListActivos();
			impMasDeuSearchPage.setListEstadoCorrida((ArrayList<EstadoCorridaVO>) ListUtilBean
					.toVO(listEstadoCorrida, 0, new EstadoCorridaVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			
			return impMasDeuSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	public ImpMasDeuSearchPage getImpMasDeuSearchPageResult(UserContext userContext, ImpMasDeuSearchPage impMasDeuSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			impMasDeuSearchPage.clearError();

			// Aqui obtiene lista de BOs
	   		List<ImpMasDeu> listImpMasDeu = EmiDAOFactory.getImpMasDeuDAO().getBySearchPage(impMasDeuSearchPage);  

	   		List<ImpMasDeuVO> listImpMasDeuVO = new ArrayList<ImpMasDeuVO>();
			for (ImpMasDeu impMasDeu: listImpMasDeu) {
				// Aqui pasamos BO a VO
				ImpMasDeuVO impMasDeuVO = (ImpMasDeuVO) impMasDeu.toVO(2,false);
				// Si tiene atributo de segmentacion
				Atributo atributo = impMasDeu.getAtributo();
				if (atributo != null && atributo.getDomAtr() != null) {
					String codigo = impMasDeu.getAtrValor();
					String desc   = atributo.getDomAtr().getDesValorByCodigo(codigo);
					impMasDeuVO.setAtrValor(desc);
				}
	        	listImpMasDeuVO.add(impMasDeuVO);
			}
	   		
	   		impMasDeuSearchPage.setListResult(listImpMasDeuVO);
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return impMasDeuSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ImpMasDeuAdapter getImpMasDeuAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ImpMasDeu impMasDeu = ImpMasDeu.getById(commonKey.getId());

	        ImpMasDeuAdapter impMasDeuAdapter = new ImpMasDeuAdapter();
	        impMasDeuAdapter.setImpMasDeu((ImpMasDeuVO) impMasDeu.toVO(2, false));
	        
	        Recurso recurso = impMasDeu.getRecurso();
	        if (recurso.getAtrEmiMas() != null) {
	        	Atributo atributo = recurso.getAtrEmiMas();
	        	for (DomAtrVal val: atributo.getDomAtr().getListDomAtrVal()) { 
	        		if (val.getStrValor().equals(impMasDeu.getAtrValor())) {
	        			impMasDeuAdapter.getImpMasDeu().setAtrValor(val.getDesValor());
	        		}
	        	}
	        	impMasDeuAdapter.setSelectAtrValEnabled(true);
	        }
			
			log.debug(funcName + ": exit");
			return impMasDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ImpMasDeuAdapter getImpMasDeuAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			ImpMasDeuAdapter impMasDeuAdapter = new ImpMasDeuAdapter();
			
			// Seteamos la lista de recursos
			List<Recurso> listRecurso = Recurso.getListWithImpresionMasiva();
			// Por defecto seteamos la opcion TODOS
			impMasDeuAdapter.getImpMasDeu().getRecurso().setId(-1L);
			
			impMasDeuAdapter.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				impMasDeuAdapter.getListRecurso().add(item.toVOWithCategoria());							
			}
			
	   		// Seteamos la lista de los formatos de salida
			impMasDeuAdapter.setListFormatoSalida(FormatoSalida.getList(FormatoSalida.OpcionSelecionar));
			
	   		// Seteamos el campo para determinar si se abre por broche
			impMasDeuAdapter.setListSiNo(SiNo.getListSiNo(SiNo.SI));

			log.debug(funcName + ": exit");
			return impMasDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ImpMasDeuAdapter getImpMasDeuAdapterForUpdate(UserContext userContext, CommonKey commonKeyImpMasDeu) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ImpMasDeu impMasDeu = ImpMasDeu.getById(commonKeyImpMasDeu.getId());
			
	        ImpMasDeuAdapter impMasDeuAdapter = new ImpMasDeuAdapter();
	        impMasDeuAdapter.setImpMasDeu((ImpMasDeuVO) impMasDeu.toVO(2, false));

			// Seteamos la lista de recursos
			List<Recurso> listRecurso = Recurso.getListWithImpresionMasiva();

			impMasDeuAdapter.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				impMasDeuAdapter.getListRecurso().add(item.toVOWithCategoria());							
			}
		
	   		// Seteamos la lista de los formatos de salida
			impMasDeuAdapter.setListFormatoSalida(FormatoSalida.getList(FormatoSalida.OpcionSelecionar));

	   		// Seteamos el campo para determinar si se abre por broche
			impMasDeuAdapter.setListSiNo(SiNo.getListSiNo(SiNo.SI));

			log.debug(funcName + ": exit");
			return impMasDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ImpMasDeuAdapter getImpMasDeuAdapterParamRecurso(UserContext userContext, 
			ImpMasDeuAdapter impMasDeuAdapter) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			impMasDeuAdapter.clearError();
			
			RecursoVO recursoVO  = impMasDeuAdapter.getImpMasDeu().getRecurso();
			Recurso recurso = Recurso.getByIdNull(recursoVO.getId());
			
			// Si se selecciono un recurso
			if (recurso != null) {
				Atributo atributo = recurso.getAtrEmiMas();
				// Si tiene el recurso atributo de segmentacion
				if (atributo != null) {
					impMasDeuAdapter.getImpMasDeu().setAtributo((AtributoVO) atributo.toVO());
					
					// Recupero la definicion del AtrVal valorizados
			        RecursoDefinition recursoDefinition = recurso
			        	.getDefinitionRecAtrValValue(atributo.getId());
			        GenericAtrDefinition genericAtrDefinition = recursoDefinition
			        	.getListGenericAtrDefinition().get(0);
	
			        genericAtrDefinition.getAtributo().getDomAtr().getListDomAtrVal()
			        	.add(0,new DomAtrValVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
	
			        impMasDeuAdapter.setGenericAtrDefinition(genericAtrDefinition);
			        
			        impMasDeuAdapter.setSelectAtrValEnabled(true);
				} else {
					impMasDeuAdapter.setSelectAtrValEnabled(false);
				}
			} else {
				impMasDeuAdapter.setSelectAtrValEnabled(false);
			}
			
			log.debug(funcName + ": exit");
			return impMasDeuAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public ImpMasDeuVO createImpMasDeu(UserContext userContext, ImpMasDeuVO impMasDeuVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			impMasDeuVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            ImpMasDeu impMasDeu = new ImpMasDeu();

            this.copyFromVO(impMasDeu, impMasDeuVO);
            
            impMasDeu.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            impMasDeu = EmiImpresionManager.getInstance().createImpMasDeu(impMasDeu);
            
            if (impMasDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				impMasDeuVO =  (ImpMasDeuVO) impMasDeu.toVO(0,false);
			}
			impMasDeu.passErrorMessages(impMasDeuVO);
            
            log.debug(funcName + ": exit");
            return impMasDeuVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ImpMasDeuVO updateImpMasDeu(UserContext userContext, ImpMasDeuVO impMasDeuVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			impMasDeuVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            ImpMasDeu impMasDeu = ImpMasDeu.getById(impMasDeuVO.getId());
			
			if(!impMasDeuVO.validateVersion(impMasDeu.getFechaUltMdf())) return impMasDeuVO;
			
            this.copyFromVO(impMasDeu, impMasDeuVO);
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            impMasDeu = EmiImpresionManager.getInstance().updateImpMasDeu(impMasDeu);
            
            if (impMasDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				impMasDeuVO =  (ImpMasDeuVO) impMasDeu.toVO(0,false);
			}
			impMasDeu.passErrorMessages(impMasDeuVO);
            
            log.debug(funcName + ": exit");
            return impMasDeuVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	private void copyFromVO(ImpMasDeu impMasDeu, ImpMasDeuVO impMasDeuVO) {
		Recurso recurso = Recurso.getByIdNull(impMasDeuVO.getRecurso().getId());
		impMasDeu.setRecurso(recurso);

		AtributoVO atributoVO = impMasDeuVO.getAtributo();
		Atributo atributo = Atributo.getByIdNull(atributoVO.getId());
		impMasDeu.setAtributo(atributo);
		impMasDeu.setAtrValor(impMasDeuVO.getAtrValor());
		impMasDeu.setFormatoSalida(impMasDeuVO.getFormatoSalida().getBussId());
		impMasDeu.setAnio(impMasDeuVO.getAnio());
		impMasDeu.setPeriodoDesde(impMasDeuVO.getPeriodoDesde());
		impMasDeu.setPeriodoHasta(impMasDeuVO.getPeriodoHasta());
		impMasDeu.setAbrirPorBroche(impMasDeuVO.getAbrirPorBroche().getBussId());
	}
	
	public ImpMasDeuVO deleteImpMasDeu(UserContext userContext, ImpMasDeuVO impMasDeuVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			impMasDeuVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			ImpMasDeu impMasDeu = ImpMasDeu.getById(impMasDeuVO.getId());
			
			Corrida corrida = impMasDeu.getCorrida();
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			impMasDeu = EmiImpresionManager.getInstance().deleteImpMasDeu(impMasDeu);
			
			if (impMasDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				
				// Eliminamos la corrida
				AdpRun.deleteRun(corrida.getId());

				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				impMasDeuVO =  (ImpMasDeuVO) impMasDeu.toVO(0,false);
			}
			impMasDeu.passErrorMessages(impMasDeuVO);
            
            log.debug(funcName + ": exit");
            return impMasDeuVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public ProcesoImpMasDeuAdapter getProcesoImpMasDeuAdapterInit(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			ImpMasDeu impMasDeu = ImpMasDeu.getById(commonKey.getId());
			Corrida corrida = impMasDeu.getCorrida();
			
			ProcesoImpMasDeuAdapter procesoImpMasDeuAdapter = new ProcesoImpMasDeuAdapter();
			procesoImpMasDeuAdapter.setImpMasDeu((ImpMasDeuVO) impMasDeu.toVO(2,false));
			
	        Recurso recurso = impMasDeu.getRecurso();
	        if (recurso.getAtrEmiMas() != null) {
	        	Atributo atributo = recurso.getAtrEmiMas();
	        	for (DomAtrVal val: atributo.getDomAtr().getListDomAtrVal()) { 
	        		if (val.getStrValor().equals(impMasDeu.getAtrValor())) {
	        			procesoImpMasDeuAdapter.getImpMasDeu().setAtrValor(val.getDesValor());
	        		}
	        	}
	        	procesoImpMasDeuAdapter.setSelectAtrValEnabled(true);
	        }
			
			// Parametro para conocer el pasoActual (para ubicar botones)
			String pasoActual = corrida.getPasoActual().toString();
			procesoImpMasDeuAdapter.setParamPaso(pasoActual);

			// Obtengo el Paso 1 (si existe)
			PasoCorrida pasoCorrida = corrida.getPasoCorrida(1);
			if (pasoCorrida != null) {
				procesoImpMasDeuAdapter.setPasoCorrida1((PasoCorridaVO) pasoCorrida.toVO(1,false));
			}

			// Obtengo los archivos de salida correspondientes al primer paso
			List<FileCorrida> listFileCorrida1 = FileCorrida.getListByCorridaYPaso(corrida, 1);
			if (!ListUtil.isNullOrEmpty(listFileCorrida1)) {
				procesoImpMasDeuAdapter.setListFileCorrida1((ArrayList<FileCorridaVO>) 
						ListUtilBean.toVO(listFileCorrida1,0, false));				
			}
			
			setBussinessFlags(procesoImpMasDeuAdapter); 

			log.debug(funcName + ": exit");
			return procesoImpMasDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	private void setBussinessFlags(ProcesoImpMasDeuAdapter procesoImpMasDeuAdapter) {

		Long estadoActual = procesoImpMasDeuAdapter.getImpMasDeu()
				.getCorrida().getEstadoCorrida().getId();

		if (!estadoActual.equals(EstadoCorrida.ID_PROCESANDO) || 
				estadoActual.equals(EstadoCorrida.ID_PROCESADO_CON_ERROR)) {
			procesoImpMasDeuAdapter.setActivarEnabled(true);
		}
		
		if (estadoActual.equals(EstadoCorrida.ID_EN_ESPERA_COMENZAR) || 
				estadoActual.equals(EstadoCorrida.ID_PROCESANDO)) { 
			procesoImpMasDeuAdapter.setCancelarEnabled(true);
		}
		
		if (estadoActual.equals(EstadoCorrida.ID_EN_ESPERA_CONTINUAR) || 
				estadoActual.equals(EstadoCorrida.ID_PROCESADO_CON_ERROR)) { 
			procesoImpMasDeuAdapter.setReiniciarEnabled(true);
		}
	}
	
	public ImpMasDeuVO activar(UserContext userContext, ImpMasDeuVO impMasDeuVO) throws DemodaServiceException {
		return impMasDeuVO;
	}
	
	public ImpMasDeuVO cancelar(UserContext userContext, ImpMasDeuVO impMasDeuVO) throws DemodaServiceException {
		return impMasDeuVO;
	}
	
	public ImpMasDeuVO reiniciar(UserContext userContext, ImpMasDeuVO impMasDeuVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		Session session = null;
		Transaction tx = null;
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			impMasDeuVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            ImpMasDeu impMasDeu = ImpMasDeu.getById(impMasDeuVO.getId());
			
			impMasDeu.reiniciar();
   
			if (impMasDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				impMasDeuVO =  (ImpMasDeuVO) impMasDeu.toVO(0,false);
			}
	
			impMasDeu.passErrorMessages(impMasDeuVO);
            
            log.debug(funcName + ": exit");
            return impMasDeuVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM Impresion Masiva de Deuda
}
