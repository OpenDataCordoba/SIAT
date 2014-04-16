//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.seg.buss.service;

/**
 * 
 * Implementacion de servicios de seguridad
 * @author tecso
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.util.SiatCache;
import ar.gov.rosario.siat.cyq.buss.bean.Abogado;
import ar.gov.rosario.siat.cyq.iface.model.AbogadoVO;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import ar.gov.rosario.siat.ef.buss.bean.Inspector;
import ar.gov.rosario.siat.ef.buss.bean.Investigador;
import ar.gov.rosario.siat.ef.buss.bean.Supervisor;
import ar.gov.rosario.siat.ef.iface.model.InspectorVO;
import ar.gov.rosario.siat.ef.iface.model.InvestigadorVO;
import ar.gov.rosario.siat.ef.iface.model.SupervisorVO;
import ar.gov.rosario.siat.gde.buss.bean.Mandatario;
import ar.gov.rosario.siat.gde.buss.bean.Procurador;
import ar.gov.rosario.siat.gde.iface.model.MandatarioVO;
import ar.gov.rosario.siat.gde.iface.model.ProcuradorVO;
import ar.gov.rosario.siat.seg.buss.bean.Oficina;
import ar.gov.rosario.siat.seg.buss.bean.SegSeguridadManager;
import ar.gov.rosario.siat.seg.buss.bean.UsuarioSiat;
import ar.gov.rosario.siat.seg.buss.dao.SegDAOFactory;
import ar.gov.rosario.siat.seg.iface.model.MenuAdapter;
import ar.gov.rosario.siat.seg.iface.model.OficinaAdapter;
import ar.gov.rosario.siat.seg.iface.model.OficinaSearchPage;
import ar.gov.rosario.siat.seg.iface.model.OficinaVO;
import ar.gov.rosario.siat.seg.iface.model.UsuarioSiatAdapter;
import ar.gov.rosario.siat.seg.iface.model.UsuarioSiatSearchPage;
import ar.gov.rosario.siat.seg.iface.model.UsuarioSiatVO;
import ar.gov.rosario.siat.seg.iface.service.ISegSeguridadService;
import ar.gov.rosario.siat.seg.iface.util.SegError;
import ar.gov.rosario.swe.iface.model.ItemMenuVO;
import ar.gov.rosario.swe.iface.model.SweUserSession;
import ar.gov.rosario.swe.iface.model.UsuarioVO;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.UserContext;


public class SegSeguridadServiceHbmImpl implements ISegSeguridadService { 
	private Logger log = Logger.getLogger(SegSeguridadServiceHbmImpl.class);

	public MenuAdapter getMenu(UserContext userContext, MenuAdapter menuAdapter) throws DemodaServiceException {
		boolean canContinue = true;
		
		Long idItemMenuNivel1 = menuAdapter.getIdItemMenuNivel1();
		Long idItemMenuNivel2 = menuAdapter.getIdItemMenuNivel2();

		try {
			SweUserSession sweTmp = new SweUserSession();
			sweTmp.setIdsAccionesModuloUsuario(userContext.getIdsAccionesModuloUsuario());
			sweTmp.setCodApplication("SIAT");
			
			List<ItemMenuVO> listItemMenuVOUsuario =  SiatCache.getInstance().getSweContext().getTreeMenu(sweTmp);
			
			// si se selecciono un item de nivel2 lleno la lista de nivel3
			if (!idItemMenuNivel2.equals(new Long(0))) {
				// recupero el itemMenu de nivel 2
				ItemMenuVO itemMenuNivel2 = this.getItemMenu( new CommonKey(idItemMenuNivel2), listItemMenuVOUsuario);
				menuAdapter.setListItemMenuNivel3 (itemMenuNivel2.getListItemMenuHijos());
				
				// Seteamos el titulo nivel 3 e item seleccionado nivel 2
				for(ItemMenuVO item : menuAdapter.getListItemMenuNivel2()) {
					item.setSeleccionadoView(false);
					if (item.getId().equals(menuAdapter.getIdItemMenuNivel2())) {
						menuAdapter.setTituloNivel3(item.getTitulo());
						item.setSeleccionadoView(true);
					}
				}
				canContinue = false;
			}
			
			if ( canContinue ) {
				// si se selecciono un item de nivel1 lleno la lista de nivel2 y borro la lista nivel 3
				if (!idItemMenuNivel1.equals(new Long(0))) {
					// recupero el itemMenu de nivel 1
					ItemMenuVO itemMenuNivel1 = this.getItemMenu( new CommonKey (idItemMenuNivel1), listItemMenuVOUsuario);
					menuAdapter.setListItemMenuNivel2 ( itemMenuNivel1.getListItemMenuHijos());
					menuAdapter.setListItemMenuNivel3(new ArrayList<ItemMenuVO>());
					
					// Seteamos titulo nivel 2 y item seleccionado nivel 1
					for(ItemMenuVO item : menuAdapter.getListItemMenuNivel1()) {
						item.setSeleccionadoView(false);
						if (item.getId().equals(menuAdapter.getIdItemMenuNivel1())) {
							menuAdapter.setTituloNivel2(item.getTitulo());
							item.setSeleccionadoView(true);
						}
					}				
					canContinue = false;
				}
			}
			
			if (canContinue) {
				// si no hay ningun nivel seleccionado lleno la lista de nivel 1 y borro la demas.
				if ( idItemMenuNivel1.equals(new Long(0)) ) {
					menuAdapter.setListItemMenuNivel1 (listItemMenuVOUsuario);
					menuAdapter.setListItemMenuNivel2(new ArrayList<ItemMenuVO>());
					menuAdapter.setListItemMenuNivel3(new ArrayList<ItemMenuVO>());
					canContinue = false;
				}
			}
			if(menuAdapter.getTituloNivel2().length()>16){
				menuAdapter.setTituloNivel2(menuAdapter.getTituloNivel2().substring(0, 16).concat("..."));
			}
			if(menuAdapter.getTituloNivel3().length()>26){
				menuAdapter.setTituloNivel3(menuAdapter.getTituloNivel3().substring(0, 26).concat("..."));
			}
			
			return menuAdapter;
        } catch (Exception e) {
        	log.error("Service Exception: getMenu(): ",e);
            throw new DemodaServiceException(e);
        }
	}
	
	public ItemMenuVO getItemMenu(CommonKey itemMenuKey, List<ItemMenuVO> listItemMenuVOUsuario) throws DemodaServiceException {
		ItemMenuVO itemMenu = null; 
        try {
    		//Iterator iterListItemMenu = this.listItemMenu.iterator();
        	Iterator iterListItemMenu =  listItemMenuVOUsuario.iterator();
    		while (iterListItemMenu.hasNext() && itemMenu == null) {
				ItemMenuVO im = (ItemMenuVO) iterListItemMenu.next();
				itemMenu = im.buscarItemById(itemMenuKey.getId());
			}
        	return itemMenu;
        } catch (Exception e) {
        	log.error("Service Exception: ", e);
            throw new DemodaServiceException(e);
        }
	}

	public String getMenu(String a, String b) throws DemodaServiceException {
		return "";
	}
		
	// ---> ABM UsuarioSiat 	
	public UsuarioSiatSearchPage getUsuarioSiatSearchPageInit
		(UserContext usercontext) throws DemodaServiceException {		

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {

			SiatHibernateUtil.currentSession();			

			UsuarioSiatSearchPage usuarioSiatSearchPage = new UsuarioSiatSearchPage();
			
			List<Area> listArea = Area.getListActivas();
			List<AreaVO> listAreaVO = ListUtilBean.toVO(listArea, new AreaVO(-1,StringUtil.SELECT_OPCION_TODOS));
			
			// seteo la lista de areas
			usuarioSiatSearchPage.setListArea(listAreaVO); 
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return usuarioSiatSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public UsuarioSiatSearchPage getUsuarioSiatSearchPageResult
		(UserContext userContext, UsuarioSiatSearchPage usuarioSiatSearchPage) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			usuarioSiatSearchPage.clearError();

			// Aqui obtiene lista de BOs
	   		List<UsuarioSiat> listUsuarioSiat = SegDAOFactory.getUsuarioSiatDAO().getBySearchPage
	   			(usuarioSiatSearchPage);  
			//Aqui pasamos BO a VO
	   		usuarioSiatSearchPage.setListResult(ListUtilBean.toVO(listUsuarioSiat,1));

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return usuarioSiatSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public UsuarioSiatAdapter getUsuarioSiatAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			UsuarioSiat usuarioSiat = UsuarioSiat.getById(commonKey.getId());

	        UsuarioSiatAdapter usuarioSiatAdapter = new UsuarioSiatAdapter();
	        usuarioSiatAdapter.setUsuarioSiat((UsuarioSiatVO) usuarioSiat.toVO(1));
			
			log.debug(funcName + ": exit");
			return usuarioSiatAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	/**
	 * Obtiene datos de un usuario siat.
	 * Obtiene el Area y Oficina del Usuario.
	 * Si no posee Area, asigna el area por defecto.
	 * Si el area posee una sola oficina, asigna esa oficina
	 */
	public UsuarioSiatVO getUsuarioSiatForLogin(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			UsuarioSiatVO usuarioSiatVO = new UsuarioSiatVO();
			
			//Buscamos usuario
			UsuarioSiat usuarioSiat = UsuarioSiat.getByUserName(userContext.getUserName());
			if (usuarioSiat == null) {
				usuarioSiatVO.addRecoverableError(SegError.NO_EXISTE_USUARIOSIAT);
				return usuarioSiatVO;
			}else if(usuarioSiat.getArea()==null){
				usuarioSiat.setArea(Area.getByCodigo(Area.COD_AREA_DEFAULT_SIAT));
			}
			
			// toVO con su lista de oficinas.
	        usuarioSiatVO = (UsuarioSiatVO) usuarioSiat.toVO(2, true);
	        //usuarioSiatVO = (UsuarioSiatVO) usuarioSiat.toVO(1, false);
	        //usuarioSiatVO.setArea((AreaVO) usuarioSiat.getArea().toVO(0,false));
	        //usuarioSiatVO.getArea().setListOficina((ArrayList<OficinaVO>) ListUtilBean.toVO(usuarioSiatVO.getArea().getListRecursoArea(),0,false));
	        	        
			log.debug(funcName + ": exit");
			return usuarioSiatVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public UsuarioSiatAdapter getUsuarioSiatAdapterForCreate(UserContext userContext) 
		throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			UsuarioSiatAdapter usuarioSiatAdapter = new UsuarioSiatAdapter();
			
			// seteo la lista de areas
			List<Area> listArea = Area.getListActivas();
			List<AreaVO> listAreaVO = ListUtilBean.toVO
				(listArea, new AreaVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR));
			usuarioSiatAdapter.setListArea(listAreaVO);
			
			// seteo la lista de procuradores
			List<Procurador> listProcurador = Procurador.getListActivos();
			List<ProcuradorVO> listProcuradorVO = ListUtilBean.toVO
				(listProcurador,1, new ProcuradorVO(-1, UsuarioSiatAdapter.NO_ES_PROCURADOR));
			usuarioSiatAdapter.setListProcurador(listProcuradorVO); 
			
			// Seteo la lista de Investigadores
			List<InvestigadorVO> listInvestigadorVO = ListUtilBean.toVO
			(Investigador.getListActivos(),1, 
					new InvestigadorVO(-1, UsuarioSiatAdapter.NO_ES_INVESTIGADOR));
			usuarioSiatAdapter.setListInvestigador(listInvestigadorVO);

			// seteo la lista de inspectores
			List<Inspector> listInspector = Inspector.getListActivos();
			usuarioSiatAdapter.setListInspector(ListUtilBean.toVO(listInspector, 0, new InspectorVO(-1,UsuarioSiatAdapter.NO_ES_INSPECTOR )));			

			// seteo la lista de Supervisores
			List<Supervisor> listSupervisor = Supervisor.getListActivos();
			usuarioSiatAdapter.setListSupervisor(ListUtilBean.toVO(listSupervisor, 0, new SupervisorVO(-1,UsuarioSiatAdapter.NO_ES_SUPERVISOR)));			

			// seteo la lista de abogados
			List<Abogado> listAbogado = Abogado.getListActivos();
			List<AbogadoVO> listAbogadoVO = ListUtilBean.toVO
				(listAbogado,1, new AbogadoVO(-1, UsuarioSiatAdapter.NO_ES_ABOGADO));
			usuarioSiatAdapter.setListAbogado(listAbogadoVO); 

			
			// seteo la lista de mandatarios
			List<Mandatario> listMandatario = Mandatario.getListActivos();
			List<MandatarioVO> listMandatarioVO = ListUtilBean.toVO
				(listMandatario,1, new MandatarioVO(-1, UsuarioSiatAdapter.NO_ES_MANDATARIO));
			usuarioSiatAdapter.setListMandatario(listMandatarioVO); 
			
			log.debug(funcName + ": exit");
			return usuarioSiatAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public UsuarioSiatAdapter getUsuarioSiatAdapterForUpdate(UserContext userContext, CommonKey commonKeyUsuarioSiat) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			UsuarioSiat usuarioSiat = UsuarioSiat.getById(commonKeyUsuarioSiat.getId());

	        UsuarioSiatAdapter usuarioSiatAdapter = new UsuarioSiatAdapter();
	        usuarioSiatAdapter.setUsuarioSiat((UsuarioSiatVO) usuarioSiat.toVO(1));

			List<Area> listArea = Area.getListActivas();
			List<AreaVO> listAreaVO = ListUtilBean.toVO
				(listArea, new AreaVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR));
			// seteo la lista de areas
			usuarioSiatAdapter.setListArea(listAreaVO);

			List<Procurador> listProcurador = Procurador.getListActivos();
			List<ProcuradorVO> listProcuradorVO = ListUtilBean.toVO
				(listProcurador,1, new ProcuradorVO(-1, UsuarioSiatAdapter.NO_ES_PROCURADOR));
			// seteo la lista de procuradores
			usuarioSiatAdapter.setListProcurador(listProcuradorVO); 
			
			// Seteo la lista de Investigadores
			List<InvestigadorVO> listInvestigadorVO = ListUtilBean.toVO
			(Investigador.getListActivos(),1, 
					new InvestigadorVO(-1, UsuarioSiatAdapter.NO_ES_INVESTIGADOR));
			usuarioSiatAdapter.setListInvestigador(listInvestigadorVO);

			// seteo la lista de abogados
			List<Abogado> listAbogado = Abogado.getListActivos();
			List<AbogadoVO> listAbogadoVO = ListUtilBean.toVO
				(listAbogado,1, new AbogadoVO(-1, UsuarioSiatAdapter.NO_ES_ABOGADO));
			usuarioSiatAdapter.setListAbogado(listAbogadoVO); 

			// seteo la lista de inspectores
			List<Inspector> listInspector = Inspector.getListActivos();
			usuarioSiatAdapter.setListInspector(ListUtilBean.toVO(listInspector, 0, new InspectorVO(-1,UsuarioSiatAdapter.NO_ES_INSPECTOR )));			
			
			// seteo la lista de Supervisores
			List<Supervisor> listSupervisor = Supervisor.getListActivos();
			usuarioSiatAdapter.setListSupervisor(ListUtilBean.toVO(listSupervisor, 0, new SupervisorVO(-1,UsuarioSiatAdapter.NO_ES_SUPERVISOR)));
			
			List<Mandatario> listMandatario = Mandatario.getListActivos();
			List<MandatarioVO> listMandatarioVO = ListUtilBean.toVO
				(listMandatario,1, new MandatarioVO(-1, UsuarioSiatAdapter.NO_ES_MANDATARIO));
			// seteo la lista de mandatario
			usuarioSiatAdapter.setListMandatario(listMandatarioVO); 
			

			log.debug(funcName + ": exit");
			return usuarioSiatAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public UsuarioSiatVO createUsuarioSiat(UserContext userContext, UsuarioSiatVO usuarioSiatVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			usuarioSiatVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            UsuarioSiat usuarioSiat = new UsuarioSiat();
            usuarioSiat.setUsuarioSIAT(usuarioSiatVO.getUsuarioSIAT());

            Area area = Area.getByIdNull(usuarioSiatVO.getArea().getId());
            usuarioSiat.setArea(area);
            
            Procurador procurador = Procurador.getByIdNull(usuarioSiatVO.getProcurador().getId());
            usuarioSiat.setProcurador(procurador);
            
            Investigador investigador = Investigador.getByIdNull(usuarioSiatVO.getInvestigador().getId());
            usuarioSiat.setInvestigador(investigador);

            Inspector inspector = Inspector.getByIdNull(usuarioSiatVO.getInspector().getId());
            usuarioSiat.setInspector(inspector);

            Supervisor supervisor = Supervisor.getByIdNull(usuarioSiatVO.getSupervisor().getId());
            usuarioSiat.setSupervisor(supervisor);
            
            Abogado abogado = Abogado.getByIdNull(usuarioSiatVO.getAbogado().getId());
            usuarioSiat.setAbogado(abogado);
            
            Mandatario mandatario = Mandatario.getByIdNull(usuarioSiatVO.getMandatario().getId());
            usuarioSiat.setMandatario(mandatario);
            
            usuarioSiat.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            usuarioSiat = SegSeguridadManager.getInstance().createUsuarioSiat(usuarioSiat);
            
            if (usuarioSiat.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				usuarioSiatVO =  (UsuarioSiatVO) usuarioSiat.toVO(3);
			}
			usuarioSiat.passErrorMessages(usuarioSiatVO);
            
            log.debug(funcName + ": exit");
            return usuarioSiatVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public UsuarioSiatVO updateUsuarioSiat(UserContext userContext, UsuarioSiatVO usuarioSiatVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			usuarioSiatVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            UsuarioSiat usuarioSiat = UsuarioSiat.getById(usuarioSiatVO.getId());

            if(!usuarioSiatVO.validateVersion(usuarioSiat.getFechaUltMdf())) return usuarioSiatVO;
            
            // puedo modificar el area o el procurador
            Area area = Area.getByIdNull(usuarioSiatVO.getArea().getId());
            usuarioSiat.setArea(area);
            Procurador procurador = Procurador.getByIdNull(usuarioSiatVO.getProcurador().getId());
            usuarioSiat.setProcurador(procurador);

            Investigador investigador = Investigador.getByIdNull(usuarioSiatVO.getInvestigador().getId());
            usuarioSiat.setInvestigador(investigador);
            
            Inspector inspector = Inspector.getByIdNull(usuarioSiatVO.getInspector().getId());
            usuarioSiat.setInspector(inspector);

            Supervisor supervisor = Supervisor.getByIdNull(usuarioSiatVO.getSupervisor().getId());
            usuarioSiat.setSupervisor(supervisor);

            Abogado abogado = Abogado.getByIdNull(usuarioSiatVO.getAbogado().getId());
            usuarioSiat.setAbogado(abogado);
            
            Mandatario mandatario = Mandatario.getByIdNull(usuarioSiatVO.getMandatario().getId());
            usuarioSiat.setMandatario(mandatario);

            usuarioSiat.setEstado(Estado.ACTIVO.getId());
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            usuarioSiat = SegSeguridadManager.getInstance().updateUsuarioSiat(usuarioSiat);
            
            if (usuarioSiat.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				usuarioSiatVO =  (UsuarioSiatVO) usuarioSiat.toVO(3);
			}
			usuarioSiat.passErrorMessages(usuarioSiatVO);
            
            log.debug(funcName + ": exit");
            return usuarioSiatVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public UsuarioSiatVO deleteUsuarioSiat(UserContext userContext, UsuarioSiatVO usuarioSiatVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			usuarioSiatVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			UsuarioSiat usuarioSiat = UsuarioSiat.getById(usuarioSiatVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			usuarioSiat = SegSeguridadManager.getInstance().deleteUsuarioSiat(usuarioSiat);
			
			if (usuarioSiat.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				usuarioSiatVO =  (UsuarioSiatVO) usuarioSiat.toVO(3);
			}
			usuarioSiat.passErrorMessages(usuarioSiatVO);
            
            log.debug(funcName + ": exit");
            return usuarioSiatVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public UsuarioSiatVO activarUsuarioSiat(UserContext userContext, UsuarioSiatVO usuarioSiatVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            UsuarioSiat usuarioSiat = UsuarioSiat.getById(usuarioSiatVO.getId());

            usuarioSiat.activar();

            if (usuarioSiat.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				usuarioSiatVO =  (UsuarioSiatVO) usuarioSiat.toVO();
			}
            usuarioSiat.passErrorMessages(usuarioSiatVO);
            
            log.debug(funcName + ": exit");
            return usuarioSiatVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public UsuarioSiatVO desactivarUsuarioSiat(UserContext userContext, UsuarioSiatVO usuarioSiatVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            UsuarioSiat usuarioSiat = UsuarioSiat.getById(usuarioSiatVO.getId());

            usuarioSiat.desactivar();

            if (usuarioSiat.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				usuarioSiatVO =  (UsuarioSiatVO) usuarioSiat.toVO();
			}
            usuarioSiat.passErrorMessages(usuarioSiatVO);
            
            log.debug(funcName + ": exit");
            return usuarioSiatVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	
	// <--- ABM UsuarioSiat
	
	
	// ---> ABM Oficina 	
	public OficinaSearchPage getOficinaSearchPageInit(UserContext userContext, CommonKey areaKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			OficinaSearchPage oficinaSearchPage = new OficinaSearchPage();

			Area area = Area.getById(areaKey.getId());
			Oficina oficina = new Oficina();
			oficina.setArea(area);
	        oficinaSearchPage.setOficina((OficinaVO) oficina.toVO(1));
				   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return oficinaSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}		
	}

	public OficinaSearchPage getOficinaSearchPageResult(UserContext userContext, OficinaSearchPage oficinaSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			oficinaSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<Oficina> listOficina = SegDAOFactory.getOficinaDAO().getBySearchPage(oficinaSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		oficinaSearchPage.setListResult(ListUtilBean.toVO(listOficina,0));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return oficinaSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public OficinaAdapter getOficinaAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Oficina oficina = Oficina.getById(commonKey.getId());

	        OficinaAdapter oficinaAdapter = new OficinaAdapter();
	        oficinaAdapter.setOficina((OficinaVO) oficina.toVO(1));
			
			log.debug(funcName + ": exit");
			return oficinaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public OficinaAdapter getOficinaAdapterForCreate(UserContext userContext, CommonKey areaKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			Area area = Area.getById(areaKey.getId());
			Oficina oficina = new Oficina();
			oficina.setArea(area);
			
	        OficinaAdapter oficinaAdapter = new OficinaAdapter();
	        oficinaAdapter.setOficina((OficinaVO) oficina.toVO(1));
			
			log.debug(funcName + ": exit");
			return oficinaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public OficinaAdapter getOficinaAdapterForUpdate(UserContext userContext, CommonKey commonKeyOficina) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Oficina oficina = Oficina.getById(commonKeyOficina.getId());
			
	        OficinaAdapter oficinaAdapter = new OficinaAdapter();
	        oficinaAdapter.setOficina((OficinaVO) oficina.toVO(1));

			// Seteo la lista para combo, valores, etc
			
			log.debug(funcName + ": exit");
			return oficinaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public OficinaVO createOficina(UserContext userContext, OficinaVO oficinaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			oficinaVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			Area area = Area.getById(oficinaVO.getArea().getId());

			Oficina oficina = new Oficina();
            oficina.setArea(area);
            oficina.setDesOficina(oficinaVO.getDesOficina());
            oficina.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            oficina = area.createOficina(oficina);
            
            if (oficina.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				oficinaVO =  (OficinaVO) oficina.toVO(3);
			}
			oficina.passErrorMessages(oficinaVO);
            
            log.debug(funcName + ": exit");
            return oficinaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public OficinaVO updateOficina(UserContext userContext, OficinaVO oficinaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			oficinaVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            Oficina oficina = Oficina.getById(oficinaVO.getId());
			
			if(!oficinaVO.validateVersion(oficina.getFechaUltMdf())) return oficinaVO;
			
            oficina.setDesOficina(oficinaVO.getDesOficina());
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			Area area = Area.getById(oficinaVO.getArea().getId());
            oficina = area.updateOficina(oficina);
            
            if (oficina.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				oficinaVO =  (OficinaVO) oficina.toVO(3);
			}
			oficina.passErrorMessages(oficinaVO);
            
            log.debug(funcName + ": exit");
            return oficinaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public OficinaVO deleteOficina(UserContext userContext, OficinaVO oficinaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			oficinaVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			Oficina oficina = Oficina.getById(oficinaVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			Area area = Area.getById(oficinaVO.getId());
			oficina = area.deleteOficina(oficina);
			
			if (oficina.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				oficinaVO =  (OficinaVO) oficina.toVO(3);
			}
			oficina.passErrorMessages(oficinaVO);
            
            log.debug(funcName + ": exit");
            return oficinaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public OficinaVO activarOficina(UserContext userContext, OficinaVO oficinaVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            Oficina oficina = Oficina.getById(oficinaVO.getId());

            oficina.activar();

            if (oficina.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				oficinaVO =  (OficinaVO) oficina.toVO();
			}
            oficina.passErrorMessages(oficinaVO);
            
            log.debug(funcName + ": exit");
            return oficinaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public OficinaVO desactivarOficina(UserContext userContext, OficinaVO oficinaVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            Oficina oficina = Oficina.getById(oficinaVO.getId());

            oficina.desactivar();

            if (oficina.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				oficinaVO =  (OficinaVO) oficina.toVO();
			}
            oficina.passErrorMessages(oficinaVO);
            
            log.debug(funcName + ": exit");
            return oficinaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	
	// <--- ABM Oficina

	public UsuarioVO changePass(UserContext userSession, UsuarioVO userVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
		
			log.debug(funcName + ": exit");
		
			return SegSeguridadManager.getInstance().changePassword(userSession, userVO);
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}
	}

}
