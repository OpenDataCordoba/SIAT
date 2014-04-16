//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.service;

/**
 * Implementacion de servicios del submodulo Definicion del modulo Estrategia Fiscal
 * @author tecso
 */

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.ef.buss.bean.DocSop;
import ar.gov.rosario.siat.ef.buss.bean.EfDefinicionManager;
import ar.gov.rosario.siat.ef.buss.bean.FuenteInfo;
import ar.gov.rosario.siat.ef.buss.bean.InsSup;
import ar.gov.rosario.siat.ef.buss.bean.Inspector;
import ar.gov.rosario.siat.ef.buss.bean.Investigador;
import ar.gov.rosario.siat.ef.buss.bean.Supervisor;
import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import ar.gov.rosario.siat.ef.iface.model.DocSopAdapter;
import ar.gov.rosario.siat.ef.iface.model.DocSopSearchPage;
import ar.gov.rosario.siat.ef.iface.model.DocSopVO;
import ar.gov.rosario.siat.ef.iface.model.FuenteInfoAdapter;
import ar.gov.rosario.siat.ef.iface.model.FuenteInfoSearchPage;
import ar.gov.rosario.siat.ef.iface.model.FuenteInfoVO;
import ar.gov.rosario.siat.ef.iface.model.InsSupAdapter;
import ar.gov.rosario.siat.ef.iface.model.InsSupVO;
import ar.gov.rosario.siat.ef.iface.model.InspectorAdapter;
import ar.gov.rosario.siat.ef.iface.model.InspectorSearchPage;
import ar.gov.rosario.siat.ef.iface.model.InspectorVO;
import ar.gov.rosario.siat.ef.iface.model.InvestigadorAdapter;
import ar.gov.rosario.siat.ef.iface.model.InvestigadorSearchPage;
import ar.gov.rosario.siat.ef.iface.model.InvestigadorVO;
import ar.gov.rosario.siat.ef.iface.model.SupervisorAdapter;
import ar.gov.rosario.siat.ef.iface.model.SupervisorSearchPage;
import ar.gov.rosario.siat.ef.iface.model.SupervisorVO;
import ar.gov.rosario.siat.ef.iface.service.IEfDefinicionService;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.TipoPeriodicidad;
import coop.tecso.demoda.iface.model.UserContext;

public class EfDefinicionServiceHbmImpl implements IEfDefinicionService {
	private Logger log = Logger.getLogger(EfDefinicionServiceHbmImpl.class);


//	---> ABM Inspector 	
	public InspectorSearchPage getInspectorSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		return new InspectorSearchPage();
	}

	public InspectorSearchPage getInspectorSearchPageResult(UserContext userContext, InspectorSearchPage inspectorSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			inspectorSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
			List<Inspector> listInspector = EfDAOFactory.getInspectorDAO().getBySearchPage(inspectorSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.

			//Aqui pasamos BO a VO
			inspectorSearchPage.setListResult(ListUtilBean.toVO(listInspector,0));

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return inspectorSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public InspectorAdapter getInspectorAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Inspector inspector = Inspector.getById(commonKey.getId());

			// Obtiene la persona
			Persona p = Persona.getById(inspector.getIdPersona());

			InspectorAdapter inspectorAdapter = new InspectorAdapter();
			inspectorAdapter.setInspector((InspectorVO) inspector.toVO(2, true));
			inspectorAdapter.getInspector().setDesPersona(p.getRepresent());

			log.debug(funcName + ": exit");
			return inspectorAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public InspectorAdapter getInspectorAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			InspectorAdapter inspectorAdapter = new InspectorAdapter();

			// Seteo de banderas

			// Seteo la listas para combos, etc

			log.debug(funcName + ": exit");
			return inspectorAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public InspectorAdapter getInspectorAdapterParamPersona(UserContext userContext, InspectorAdapter inspectorAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			inspectorAdapter.clearError();

			// Recupera la persona
			Persona persona = Persona.getById(inspectorAdapter.getInspector().getIdPersona());

			// llena el campo desInspector con los datos de la persona por defecto
			inspectorAdapter.getInspector().setDesInspector(persona.getRepresent());
			inspectorAdapter.getInspector().setDesPersona(persona.getRepresent());

			log.debug(funcName + ": exit");
			return inspectorAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public InspectorAdapter getInspectorAdapterForUpdate(UserContext userContext, CommonKey commonKeyInspector) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			// Obtiene el inspector
			Inspector inspector = Inspector.getById(commonKeyInspector.getId());

			// Obtiene la persona
			Persona p = Persona.getById(inspector.getIdPersona());

			InspectorAdapter inspectorAdapter = new InspectorAdapter();
			inspectorAdapter.setInspector((InspectorVO) inspector.toVO(2, true));
			inspectorAdapter.getInspector().setDesPersona(p.getRepresent());


			log.debug(funcName + ": exit");
			return inspectorAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public InspectorVO createInspector(UserContext userContext, InspectorVO inspectorVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			inspectorVO.clearErrorMessages();

			// Copiado de propiadades de VO al BO
			Inspector inspector = new Inspector();

			this.copyFromVO(inspector, inspectorVO);

			inspector.setEstado(Estado.ACTIVO.getId());

			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			inspector = EfDefinicionManager.getInstance().createInspector(inspector);

			if (inspector.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				inspectorVO =  (InspectorVO) inspector.toVO(0, false);
			}
			inspector.passErrorMessages(inspectorVO);

			log.debug(funcName + ": exit");
			return inspectorVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public InspectorVO updateInspector(UserContext userContext, InspectorVO inspectorVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			inspectorVO.clearErrorMessages();

			// Copiado de propiadades de VO al BO
			Inspector inspector = Inspector.getById(inspectorVO.getId());

			if(!inspectorVO.validateVersion(inspector.getFechaUltMdf())) return inspectorVO;

			this.copyFromVO(inspector, inspectorVO);

			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			inspector = EfDefinicionManager.getInstance().updateInspector(inspector);

			if (inspector.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				inspectorVO =  (InspectorVO) inspector.toVO(3);
			}
			inspector.passErrorMessages(inspectorVO);

			log.debug(funcName + ": exit");
			return inspectorVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	private void copyFromVO(Inspector inspector, InspectorVO inspectorVO) {
		inspector.setIdPersona(inspectorVO.getIdPersona());
		inspector.setDesInspector(inspectorVO.getDesInspector());
		inspector.setFechaDesde(inspectorVO.getFechaDesde());
		inspector.setFechaHasta(inspectorVO.getFechaHasta());		
	}

	public InspectorVO deleteInspector(UserContext userContext, InspectorVO inspectorVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			inspectorVO.clearErrorMessages();

			// Se recupera el Bean dado su id
			Inspector inspector = Inspector.getById(inspectorVO.getId());

			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			inspector = EfDefinicionManager.getInstance().deleteInspector(inspector);

			if (inspector.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				inspectorVO =  (InspectorVO) inspector.toVO(3);
			}
			inspector.passErrorMessages(inspectorVO);

			log.debug(funcName + ": exit");
			return inspectorVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public InspectorAdapter imprimirInspector(UserContext userContext, InspectorAdapter inspectorAdapterVO)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Inspector inspector = Inspector.getById(inspectorAdapterVO.getInspector().getId());

			EfDAOFactory.getInspectorDAO().imprimirGenerico(inspector, inspectorAdapterVO.getReport());

			log.debug(funcName + ": exit");
			return inspectorAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	// <--- ABM Inspector

	// ---> ABM InsSup

	public InsSupAdapter getInsSupAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			InsSup insSup = InsSup.getById(commonKey.getId());

			InsSupAdapter insSupAdapter = new InsSupAdapter();
			insSupAdapter.setInsSup((InsSupVO) insSup.toVO(1, true));

			log.debug(funcName + ": exit");
			return insSupAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public InsSupAdapter getInsSupAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			InsSupAdapter insSupAdapter = new InsSupAdapter();

			Inspector inspector = Inspector.getById(commonKey.getId());

			insSupAdapter.getInsSup().setInspector((InspectorVO)inspector.toVO());

			insSupAdapter.setListSupervisor((ArrayList<SupervisorVO>) ListUtilBean.toVO(Supervisor.getListActivos(), 
					new SupervisorVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));


			// Seteo de banderas

			// Seteo la listas para combos, etc

			log.debug(funcName + ": exit");
			return insSupAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}



	public InsSupAdapter getInsSupAdapterForUpdate(UserContext userContext, CommonKey commonKeyInspector) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			// Obtiene el inspector
			InsSup insSup = InsSup.getById(commonKeyInspector.getId());

			InsSupAdapter insSupAdapter = new InsSupAdapter();

			insSupAdapter.setListSupervisor((ArrayList<SupervisorVO>) ListUtilBean.toVO(Supervisor.getListActivos(), 
					new SupervisorVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));

			insSupAdapter.setInsSup((InsSupVO) insSup.toVO(1, false));

			log.debug(funcName + ": exit");
			return insSupAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public InsSupVO createInsSup(UserContext userContext, InsSupVO insSupVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			insSupVO.clearErrorMessages();

			// Copiado de propiadades de VO al BO
			InsSup insSup = new InsSup();

			this.copyFromVO(insSup, insSupVO);
			
			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			insSup = EfDefinicionManager.getInstance().createInsSup(insSup);

			if (insSup.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				insSupVO =  (InsSupVO) insSup.toVO(0, false);
			}
			insSup.passErrorMessages(insSupVO);

			log.debug(funcName + ": exit");
			return insSupVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public InsSupVO updateInsSup(UserContext userContext, InsSupVO insSupVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			insSupVO.clearErrorMessages();

			// Copiado de propiadades de VO al BO
			InsSup insSup = InsSup.getById(insSupVO.getId());

			if(!insSupVO.validateVersion(insSup.getFechaUltMdf())) return insSupVO;

			this.copyFromVO(insSup, insSupVO);



			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			insSup = EfDefinicionManager.getInstance().updateInsSup(insSup);

			if (insSup.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				insSupVO =  (InsSupVO) insSup.toVO(3);
			}
			insSup.passErrorMessages(insSupVO);

			log.debug(funcName + ": exit");
			return insSupVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	private void copyFromVO(InsSup insSup, InsSupVO insSupVO){ 
		Inspector inspector = Inspector.getByIdNull(insSupVO.getInspector().getId()); 
		insSup.setInspector(inspector); 
		Supervisor supervisor = Supervisor.getByIdNull(insSupVO.getSupervisor().getId()); 
		insSup.setSupervisor(supervisor); 
		insSup.setFechaDesde(insSupVO.getFechaDesde()); 
		insSup.setFechaHasta(insSupVO.getFechaHasta()); 
	}

	public InsSupVO deleteInsSup(UserContext userContext, InsSupVO insSupVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			insSupVO.clearErrorMessages();

			// Se recupera el Bean dado su id
			InsSup insSup = InsSup.getById(insSupVO.getId());

			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			insSup = EfDefinicionManager.getInstance().deleteInsSup(insSup);

			if (insSup.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				insSupVO =  (InsSupVO) insSup.toVO(3);
			}
			insSup.passErrorMessages(insSupVO);

			log.debug(funcName + ": exit");
			return insSupVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public InsSupAdapter imprimirInsSup(UserContext userContext, InsSupAdapter insSupAdapterVO)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			InsSup insSup = InsSup.getById(insSupAdapterVO.getInsSup().getId());

			EfDAOFactory.getInspectorDAO().imprimirGenerico(insSup, insSupAdapterVO.getReport());

			log.debug(funcName + ": exit");
			return insSupAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	// <--- ABM InsSup


	// ---> ABM Investigador 	
	public InvestigadorSearchPage getInvestigadorSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		return new InvestigadorSearchPage();
	}

	public InvestigadorSearchPage getInvestigadorSearchPageResult(UserContext userContext, InvestigadorSearchPage investigadorSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			investigadorSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
			List<Investigador> listInvestigador = EfDAOFactory.getInvestigadorDAO().getBySearchPage(investigadorSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.

			//Aqui pasamos BO a VO
			investigadorSearchPage.setListResult(ListUtilBean.toVO(listInvestigador,0));

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return investigadorSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public InvestigadorAdapter getInvestigadorAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Investigador investigador = Investigador.getById(commonKey.getId());

			InvestigadorAdapter investigadorAdapter = new InvestigadorAdapter();
			investigadorAdapter.setInvestigador((InvestigadorVO) investigador.toVO(0, false));

			Persona persona = Persona.getById(investigador.getIdPersona());
			investigadorAdapter.getInvestigador().setDesPersona(persona.getRepresent());	        		

			log.debug(funcName + ": exit");
			return investigadorAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public InvestigadorAdapter getInvestigadorAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			InvestigadorAdapter investigadorAdapter = new InvestigadorAdapter();

			// Seteo de banderas

			// Seteo la listas para combos, etc

			log.debug(funcName + ": exit");
			return investigadorAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public InvestigadorAdapter getInvestigadorAdapterForUpdate(UserContext userContext, CommonKey commonKeyInvestigador) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Investigador investigador = Investigador.getById(commonKeyInvestigador.getId());

			InvestigadorAdapter investigadorAdapter = new InvestigadorAdapter();
			investigadorAdapter.setInvestigador((InvestigadorVO) investigador.toVO(0, false));

			Persona persona = Persona.getById(investigador.getIdPersona());
			investigadorAdapter.getInvestigador().setDesPersona(persona.getRepresent());	        		

			log.debug(funcName + ": exit");
			return investigadorAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}




	public InvestigadorVO deleteInvestigador(UserContext userContext, InvestigadorVO investigadorVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			investigadorVO.clearErrorMessages();

			// Se recupera el Bean dado su id
			Investigador investigador = Investigador.getById(investigadorVO.getId());

			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			investigador = EfDefinicionManager.getInstance().deleteInvestigador(investigador);

			if (investigador.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				investigadorVO =  (InvestigadorVO) investigador.toVO(3);
			}
			investigador.passErrorMessages(investigadorVO);

			log.debug(funcName + ": exit");
			return investigadorVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	private void copyFromVO(Investigador investigador, InvestigadorVO investigadorVO) {
		investigador.setIdPersona(investigadorVO.getIdPersona());
		investigador.setDesInvestigador(investigadorVO.getDesInvestigador());
		investigador.setFechaDesde(investigadorVO.getFechaDesde());
		investigador.setFechaHasta(investigadorVO.getFechaHasta());		
	}
	// <--- ABM Investigador

	public InvestigadorVO createInvestigador(UserContext userContext, InvestigadorVO investigadorVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			investigadorVO.clearErrorMessages();

			// Copiado de propiadades de VO al BO
			Investigador investigador = new Investigador();

			this.copyFromVO(investigador, investigadorVO);

			investigador.setEstado(Estado.ACTIVO.getId());

			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			investigador = EfDefinicionManager.getInstance().createInvestigador(investigador);

			if (investigador.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				investigadorVO =  (InvestigadorVO) investigador.toVO(0, false);
			}
			investigador.passErrorMessages(investigadorVO);

			log.debug(funcName + ": exit");
			return investigadorVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public InvestigadorVO updateInvestigador(UserContext userContext, InvestigadorVO investigadorVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			investigadorVO.clearErrorMessages();

			// Copiado de propiadades de VO al BO
			Investigador investigador = Investigador.getById(investigadorVO.getId());

			if(!investigadorVO.validateVersion(investigador.getFechaUltMdf())) return investigadorVO;

			this.copyFromVO(investigador, investigadorVO);

			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			investigador = EfDefinicionManager.getInstance().updateInvestigador(investigador);

			if (investigador.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				investigadorVO =  (InvestigadorVO) investigador.toVO(0, false);
			}
			investigador.passErrorMessages(investigadorVO);

			log.debug(funcName + ": exit");
			return investigadorVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public InvestigadorAdapter getInvestigadorAdapterParamPersona(UserContext userContext, InvestigadorAdapter investigadorAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			investigadorAdapter.clearError();

			// Recupera la persona
			Persona persona = Persona.getById(investigadorAdapter.getInvestigador().getIdPersona());

			// llena el campo desInspector con los datos de la persona por defecto
			investigadorAdapter.getInvestigador().setIdPersona(persona.getId());
			investigadorAdapter.getInvestigador().setDesInvestigador(persona.getRepresent());
			investigadorAdapter.getInvestigador().setDesPersona(persona.getRepresent());

			log.debug(funcName + ": exit");
			return investigadorAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}	

	public SupervisorAdapter imprimirSupervisor(UserContext userContext, SupervisorAdapter supervisorAdapterVO)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Supervisor supervisor = Supervisor.getById(supervisorAdapterVO.getSupervisor().getId());

			EfDAOFactory.getSupervisorDAO().imprimirGenerico(supervisor, supervisorAdapterVO.getReport());

			log.debug(funcName + ": exit");
			return supervisorAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	// ---> ABM Supervisor 	
	public SupervisorSearchPage getSupervisorSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		return new SupervisorSearchPage();
	}

	public SupervisorSearchPage getSupervisorSearchPageResult(UserContext userContext, SupervisorSearchPage supervisorSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			supervisorSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
			List<Supervisor> listSupervisor = EfDAOFactory.getSupervisorDAO().getBySearchPage(supervisorSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.

			//Aqui pasamos BO a VO
			supervisorSearchPage.setListResult(ListUtilBean.toVO(listSupervisor,0));

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return supervisorSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public SupervisorAdapter getSupervisorAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Supervisor supervisor = Supervisor.getById(commonKey.getId());

			// Obtiene la persona
			Persona p = Persona.getById(supervisor.getIdPersona());

			SupervisorAdapter supervisorAdapter = new SupervisorAdapter();
			supervisorAdapter.setSupervisor((SupervisorVO) supervisor.toVO(0, false));
			supervisorAdapter.getSupervisor().setDesPersona(p.getRepresent());

			log.debug(funcName + ": exit");
			return supervisorAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public SupervisorAdapter getSupervisorAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			SupervisorAdapter supervisorAdapter = new SupervisorAdapter();

			// Seteo de banderas

			// Seteo la listas para combos, etc

			log.debug(funcName + ": exit");
			return supervisorAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public SupervisorAdapter getSupervisorAdapterParamPersona(UserContext userContext, SupervisorAdapter supervisorAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			supervisorAdapter.clearError();

			// Recupera la persona
			Persona persona = Persona.getById(supervisorAdapter.getSupervisor().getIdPersona());

			// llena el campo desSupervisor con los datos de la persona por defecto
			supervisorAdapter.getSupervisor().setDesSupervisor(persona.getRepresent());
			supervisorAdapter.getSupervisor().setDesPersona(persona.getRepresent());

			log.debug(funcName + ": exit");
			return supervisorAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public SupervisorAdapter getSupervisorAdapterForUpdate(UserContext userContext, CommonKey commonKeySupervisor) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			// Obtiene el supervisor
			Supervisor supervisor = Supervisor.getById(commonKeySupervisor.getId());

			// Obtiene la persona
			Persona p = Persona.getById(supervisor.getIdPersona());

			SupervisorAdapter supervisorAdapter = new SupervisorAdapter();
			supervisorAdapter.setSupervisor((SupervisorVO) supervisor.toVO(0, false));
			supervisorAdapter.getSupervisor().setDesPersona(p.getRepresent());


			log.debug(funcName + ": exit");
			return supervisorAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public SupervisorVO createSupervisor(UserContext userContext, SupervisorVO supervisorVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			supervisorVO.clearErrorMessages();

			// Copiado de propiadades de VO al BO
			Supervisor supervisor = new Supervisor();

			this.copyFromVO(supervisor, supervisorVO);

			supervisor.setEstado(Estado.ACTIVO.getId());

			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			supervisor = EfDefinicionManager.getInstance().createSupervisor(supervisor);

			if (supervisor.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				supervisorVO =  (SupervisorVO) supervisor.toVO(0, false);
			}
			supervisor.passErrorMessages(supervisorVO);

			log.debug(funcName + ": exit");
			return supervisorVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public SupervisorVO updateSupervisor(UserContext userContext, SupervisorVO supervisorVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			supervisorVO.clearErrorMessages();

			// Copiado de propiadades de VO al BO
			Supervisor supervisor = Supervisor.getById(supervisorVO.getId());

			if(!supervisorVO.validateVersion(supervisor.getFechaUltMdf())) return supervisorVO;

			this.copyFromVO(supervisor, supervisorVO);

			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			supervisor = EfDefinicionManager.getInstance().updateSupervisor(supervisor);

			if (supervisor.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				supervisorVO =  (SupervisorVO) supervisor.toVO(3);
			}
			supervisor.passErrorMessages(supervisorVO);

			log.debug(funcName + ": exit");
			return supervisorVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	private void copyFromVO(Supervisor supervisor, SupervisorVO supervisorVO) {
		supervisor.setIdPersona(supervisorVO.getIdPersona());
		supervisor.setDesSupervisor(supervisorVO.getDesSupervisor());
		supervisor.setFechaDesde(supervisorVO.getFechaDesde());
		supervisor.setFechaHasta(supervisorVO.getFechaHasta());		
	}

	public SupervisorVO deleteSupervisor(UserContext userContext, SupervisorVO supervisorVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			supervisorVO.clearErrorMessages();

			// Se recupera el Bean dado su id
			Supervisor supervisor = Supervisor.getById(supervisorVO.getId());

			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			supervisor = EfDefinicionManager.getInstance().deleteSupervisor(supervisor);

			if (supervisor.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				supervisorVO =  (SupervisorVO) supervisor.toVO(3);
			}
			supervisor.passErrorMessages(supervisorVO);

			log.debug(funcName + ": exit");
			return supervisorVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	// <--- ABM Supervisor

	// ---> ABM FuenteInfo 	
	public FuenteInfoSearchPage getFuenteInfoSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		return new FuenteInfoSearchPage();
	}

	public FuenteInfoSearchPage getFuenteInfoSearchPageResult(UserContext userContext, FuenteInfoSearchPage fuenteInfoSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			fuenteInfoSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
			List<FuenteInfo> listFuenteInfo = EfDAOFactory.getFuenteInfoDAO().getBySearchPage(fuenteInfoSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.

			//Aqui pasamos BO a VO
			fuenteInfoSearchPage.setListResult(ListUtilBean.toVO(listFuenteInfo,1));

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return fuenteInfoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FuenteInfoAdapter getFuenteInfoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			FuenteInfo fuenteInfo = FuenteInfo.getById(commonKey.getId());

			// Obtiene la persona


			FuenteInfoAdapter fuenteInfoAdapter = new FuenteInfoAdapter();
			fuenteInfoAdapter.setFuenteInfo((FuenteInfoVO) fuenteInfo.toVO(0, false));


			log.debug(funcName + ": exit");
			return fuenteInfoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FuenteInfoAdapter getFuenteInfoAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			FuenteInfoAdapter fuenteInfoAdapter = new FuenteInfoAdapter();
			List<TipoPeriodicidad> listTipoPeriodicidad	=TipoPeriodicidad.getList();
			listTipoPeriodicidad.add(TipoPeriodicidad.SELECCIONAR);
			fuenteInfoAdapter.setListTipoPeriodicidad(listTipoPeriodicidad);
			fuenteInfoAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));

			// Seteo de banderas

			// Seteo la listas para combos, etc

			log.debug(funcName + ": exit");
			return fuenteInfoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}


	public FuenteInfoAdapter getFuenteInfoAdapterForUpdate(UserContext userContext, CommonKey commonKeyFuenteInfo) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			// Obtiene el supervisor
			FuenteInfo fuenteInfo = FuenteInfo.getById(commonKeyFuenteInfo.getId());


			FuenteInfoAdapter fuenteInfoAdapter = new FuenteInfoAdapter();

			fuenteInfoAdapter.setListTipoPeriodicidad(TipoPeriodicidad.getList());
			fuenteInfoAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));

			fuenteInfoAdapter.setFuenteInfo((FuenteInfoVO) fuenteInfo.toVO(0, false));


			log.debug(funcName + ": exit");
			return fuenteInfoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FuenteInfoVO createFuenteInfo(UserContext userContext, FuenteInfoVO fuenteInfoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			fuenteInfoVO.clearErrorMessages();

			// Copiado de propiadades de VO al BO
			FuenteInfo fuenteInfo = new FuenteInfo();

			this.copyFromVO(fuenteInfo, fuenteInfoVO);

			fuenteInfo.setEstado(Estado.ACTIVO.getId());

			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			fuenteInfo = EfDefinicionManager.getInstance().createFuenteInfo(fuenteInfo);

			if (fuenteInfo.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				fuenteInfoVO =  (FuenteInfoVO) fuenteInfo.toVO(0, false);
			}
			fuenteInfo.passErrorMessages(fuenteInfoVO);

			log.debug(funcName + ": exit");
			return fuenteInfoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FuenteInfoVO updateFuenteInfo(UserContext userContext, FuenteInfoVO fuenteInfoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			fuenteInfoVO.clearErrorMessages();

			// Copiado de propiadades de VO al BO
			FuenteInfo fuenteInfo = FuenteInfo.getById(fuenteInfoVO.getId());

			if(!fuenteInfoVO.validateVersion(fuenteInfo.getFechaUltMdf())) return fuenteInfoVO;

			this.copyFromVO(fuenteInfo, fuenteInfoVO);

			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			fuenteInfo = EfDefinicionManager.getInstance().updateFuenteInfo(fuenteInfo);

			if (fuenteInfo.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				fuenteInfoVO =  (FuenteInfoVO) fuenteInfo.toVO(3);
			}
			fuenteInfo.passErrorMessages(fuenteInfoVO);

			log.debug(funcName + ": exit");
			return fuenteInfoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	private void copyFromVO(FuenteInfo fuenteInfo, FuenteInfoVO fuenteInfoVO) {
		fuenteInfo.setNombreFuente(fuenteInfoVO.getNombreFuente()); 
		fuenteInfo.setTipoPeriodicidad(fuenteInfoVO.getTipoPeriodicidad().getId()); 
		fuenteInfo.setApertura(fuenteInfoVO.getApertura().getBussId()); 
		fuenteInfo.setDesCol1(fuenteInfoVO.getDesCol1());
	}

	public FuenteInfoVO deleteFuenteInfo(UserContext userContext, FuenteInfoVO fuenteInfoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			fuenteInfoVO.clearErrorMessages();

			// Se recupera el Bean dado su id
			FuenteInfo fuenteInfo = FuenteInfo.getById(fuenteInfoVO.getId());

			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			fuenteInfo = EfDefinicionManager.getInstance().deleteFuenteInfo(fuenteInfo);

			if (fuenteInfo.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				fuenteInfoVO =  (FuenteInfoVO) fuenteInfo.toVO(3);
			}
			fuenteInfo.passErrorMessages(fuenteInfoVO);

			log.debug(funcName + ": exit");
			return fuenteInfoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FuenteInfoAdapter imprimirFuenteInfo(UserContext userContext, FuenteInfoAdapter fuenteInfoAdapterVO)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			FuenteInfo fuenteInfo = FuenteInfo.getById(fuenteInfoAdapterVO.getFuenteInfo().getId());

			EfDAOFactory.getFuenteInfoDAO().imprimirGenerico(fuenteInfo, fuenteInfoAdapterVO.getReport());

			log.debug(funcName + ": exit");
			return fuenteInfoAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	public FuenteInfoVO activarFuenteInfo(UserContext userContext, FuenteInfoVO fuenteInfoVO)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			FuenteInfo fuenteInfo = FuenteInfo.getById(fuenteInfoVO.getId());

			fuenteInfo.activar();

			if (fuenteInfo.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				fuenteInfoVO =  (FuenteInfoVO) fuenteInfo.toVO();
			}
			fuenteInfo.passErrorMessages(fuenteInfoVO);

			log.debug(funcName + ": exit");
			return fuenteInfoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	public FuenteInfoVO desactivarFuenteInfo(UserContext userContext, FuenteInfoVO fuenteInfoVO)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			FuenteInfo fuenteInfo = FuenteInfo.getById(fuenteInfoVO.getId());

			fuenteInfo.desactivar();

			if (fuenteInfo.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				fuenteInfoVO =  (FuenteInfoVO) fuenteInfo.toVO();
			}
			fuenteInfo.passErrorMessages(fuenteInfoVO);

			log.debug(funcName + ": exit");
			return fuenteInfoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	

	// <--- ABM FuenteInfo
	
	// ---> ABM DocSop
	public DocSopSearchPage getDocSopSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		return new DocSopSearchPage();
	}

	public DocSopSearchPage getDocSopSearchPageResult(UserContext userContext, DocSopSearchPage docSopSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			docSopSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
			List<DocSop> listDocSop = EfDAOFactory.getDocSopDAO().getBySearchPage(docSopSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.

			//Aqui pasamos BO a VO
			docSopSearchPage.setListResult((ArrayList<DocSopVO>) ListUtilBean.toVO(listDocSop,0));

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return docSopSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DocSopAdapter getDocSopAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			DocSop docSop = DocSop.getById(commonKey.getId());

			// Obtiene la persona
			//Persona p = Persona.getById(docSop.getIdPersona());

			DocSopAdapter docSopAdapter = new DocSopAdapter();
			docSopAdapter.setDocSop((DocSopVO) docSop.toVO(0, false));
	
	        	
			log.debug(funcName + ": exit");
			return docSopAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DocSopAdapter getDocSopAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			DocSopAdapter docSopAdapter = new DocSopAdapter();
			docSopAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
			// Seteo de banderas

			// Seteo la listas para combos, etc

			log.debug(funcName + ": exit");
			return docSopAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public DocSopAdapter getDocSopAdapterParamPersona(UserContext userContext, DocSopAdapter docSopAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			docSopAdapter.clearError();

			// Recupera la persona
			//Persona persona = Persona.getById(docSopAdapter.getDocSop().getIdPersona());
             
			// llena el campo desDocSop con los datos de la persona por defecto
		    //docSopAdapter.getDocSop().setDesDocSop(persona.getRepresent());
			//docSopAdapter.getDocSop().setDesPersona(persona.getRepresent());

			log.debug(funcName + ": exit");
			return docSopAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public DocSopAdapter getDocSopAdapterForUpdate(UserContext userContext, CommonKey commonKeyDocSop) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			// Obtiene el docSop
			DocSop docSop = DocSop.getById(commonKeyDocSop.getId());

			// Obtiene la persona
			//Persona p = Persona.getById(docSop.getIdPersona());

			DocSopAdapter docSopAdapter = new DocSopAdapter();
			docSopAdapter.setDocSop((DocSopVO) docSop.toVO(0, false));
			docSopAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));

			log.debug(funcName + ": exit");
			return docSopAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DocSopVO createDocSop(UserContext userContext, DocSopVO docSopVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			docSopVO.clearErrorMessages();

			// Copiado de propiadades de VO al BO
			DocSop docSop = new DocSop();

			this.copyFromVO(docSop, docSopVO);

			docSop.setEstado(Estado.ACTIVO.getId());

			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			docSop = EfDefinicionManager.getInstance().createDocSop(docSop);

			if (docSop.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				docSopVO =  (DocSopVO) docSop.toVO(0, false);
			}
			docSop.passErrorMessages(docSopVO);

			log.debug(funcName + ": exit");
			return docSopVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DocSopVO updateDocSop(UserContext userContext, DocSopVO docSopVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			docSopVO.clearErrorMessages();

			// Copiado de propiadades de VO al BO
			DocSop docSop = DocSop.getById(docSopVO.getId());

			if(!docSopVO.validateVersion(docSop.getFechaUltMdf())) return docSopVO;

			this.copyFromVO(docSop, docSopVO);

			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			docSop = EfDefinicionManager.getInstance().updateDocSop(docSop);

			if (docSop.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				docSopVO =  (DocSopVO) docSop.toVO(3);
			}
			docSop.passErrorMessages(docSopVO);

			log.debug(funcName + ": exit");
			return docSopVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	private void copyFromVO(DocSop docSop, DocSopVO docSopVO) {
		docSop.setDesDocSop(docSopVO.getDesDocSop());
		docSop.setDeterminaAjuste(docSopVO.getDeterminaAjuste().getBussId());
		docSop.setAplicaMulta(docSopVO.getAplicaMulta().getBussId());
		docSop.setCompensaSalAFav(docSopVO.getCompensaSalAFav().getBussId());
		docSop.setDevuelveSalAFav(docSopVO.getDevuelveSalAFav().getBussId());
		docSop.setPlantilla(docSopVO.getPlantilla());
	}

	public DocSopVO deleteDocSop(UserContext userContext, DocSopVO docSopVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			docSopVO.clearErrorMessages();

			// Se recupera el Bean dado su id
			DocSop docSop = DocSop.getById(docSopVO.getId());

			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			docSop = EfDefinicionManager.getInstance().deleteDocSop(docSop);

			if (docSop.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				docSopVO =  (DocSopVO) docSop.toVO(3);
			}
			docSop.passErrorMessages(docSopVO);

			log.debug(funcName + ": exit");
			return docSopVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DocSopAdapter imprimirDocSop(UserContext userContext, DocSopAdapter supervisorAdapterVO)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			DocSop supervisor = DocSop.getById(supervisorAdapterVO.getDocSop().getId());

			EfDAOFactory.getDocSopDAO().imprimirGenerico(supervisor, supervisorAdapterVO.getReport());

			log.debug(funcName + ": exit");
			return supervisorAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	
	public DocSopVO activarDocSop(UserContext userContext,DocSopVO docSopVO)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			DocSop docSop = DocSop.getById(docSopVO.getId());

			docSop.activar();

			if (docSop.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				docSopVO =  (DocSopVO) docSop.toVO();
			}
			docSop.passErrorMessages(docSopVO);

			log.debug(funcName + ": exit");
			return docSopVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	public DocSopVO desactivarDocSop(UserContext userContext, DocSopVO docSopVO)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			DocSop docSop = DocSop.getById(docSopVO.getId());

			docSop.desactivar();

			if (docSop.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				docSopVO =  (DocSopVO) docSop.toVO();
			}
			docSop.passErrorMessages(docSopVO);

			log.debug(funcName + ": exit");
			return docSopVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	

	// <--- ABM DocSop

}
