//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.frm.buss.service;

/**
 * Implementacion de servicios del submodulo Formulario del modulo Frm
 * @author tecso
 */

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.DesImp;
import ar.gov.rosario.siat.def.iface.model.DesImpVO;
import ar.gov.rosario.siat.frm.buss.bean.ForCam;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.frm.buss.bean.FrmFormularioManager;
import ar.gov.rosario.siat.frm.buss.dao.FrmDAOFactory;
import ar.gov.rosario.siat.frm.iface.model.ForCamAdapter;
import ar.gov.rosario.siat.frm.iface.model.ForCamVO;
import ar.gov.rosario.siat.frm.iface.model.FormatoSalida;
import ar.gov.rosario.siat.frm.iface.model.FormularioAdapter;
import ar.gov.rosario.siat.frm.iface.model.FormularioSearchPage;
import ar.gov.rosario.siat.frm.iface.model.FormularioVO;
import ar.gov.rosario.siat.frm.iface.service.IFrmFormularioService;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.UserContext;

public class FrmFormularioServiceHbmImpl implements IFrmFormularioService {
	private Logger log = Logger.getLogger(FrmFormularioServiceHbmImpl.class);

	// ---> ABM Formulario
	public FormularioSearchPage getFormularioSearchPageInit(
			UserContext userContext) throws DemodaServiceException {
		FormularioSearchPage frmSearchPage = new FormularioSearchPage();
		try {
			frmSearchPage.setListDesImp((ArrayList<DesImpVO>) ListUtilBean
					.toVO(DesImp.getListActivos(), 1, new DesImpVO(-1,
							StringUtil.SELECT_OPCION_SELECCIONAR)));
		} catch (Exception e) {
			log.error("Service Error: ", e);
			throw new DemodaServiceException(e);
		}
		return frmSearchPage;
	}

	public FormularioSearchPage getFormularioSearchPageResult(
			UserContext userContext, FormularioSearchPage formularioSearchPage)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			formularioSearchPage.clearError();

			// Aqui realizar validaciones

			// Aqui obtiene lista de BOs
			List<Formulario> listFormulario = FrmDAOFactory.getFormularioDAO()
					.getBySearchPage(formularioSearchPage);

			// Aqui se podria iterar la lista de BO para setear banderas en VOs
			// y otras cosas del negocio.

			// Aqui pasamos BO a VO
			formularioSearchPage.setListResult(ListUtilBean.toVO(
					listFormulario, 1));

			if (log.isDebugEnabled())
				log.debug(funcName + ": exit");
			return formularioSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FormularioAdapter getFormularioAdapterForView(
			UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled())
			log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			Formulario formulario = Formulario.getById(commonKey.getId());

			FormularioAdapter formularioAdapter = new FormularioAdapter();
			formularioAdapter.setFormulario((FormularioVO) formulario.toVO(1));

			log.debug(funcName + ": exit");
			return formularioAdapter;
		} catch (Exception e) {
			log.error("Service Error: ", e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FormularioAdapter getFormularioAdapterForCreate(
			UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled())
			log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			FormularioAdapter formularioAdapter = new FormularioAdapter();

			// Seteo de banderas

			// Seteo la listas para combos, etc
			formularioAdapter.setListDesImp((ArrayList<DesImpVO>) ListUtilBean
					.toVO(DesImp.getListActivos(), 1, new DesImpVO(-1,
							StringUtil.SELECT_OPCION_SELECCIONAR)));

			log.debug(funcName + ": exit");
			return formularioAdapter;
		} catch (Exception e) {
			log.error("Service Error: ", e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FormularioAdapter getFormularioAdapterParam(UserContext userContext,
			FormularioAdapter formularioAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled())
			log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			formularioAdapter.clearError();

			// Logica del param

			log.debug(funcName + ": exit");
			return formularioAdapter;
		} catch (Exception e) {
			log.error("Service Error: ", e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FormularioAdapter getFormularioAdapterForUpdate(
			UserContext userContext, CommonKey commonKeyFormulario)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled())
			log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			Formulario formulario = Formulario.getById(commonKeyFormulario
					.getId());

			FormularioAdapter formularioAdapter = new FormularioAdapter();
			formularioAdapter.setFormulario((FormularioVO) formulario.toVO(1));

			// Seteo la lista para combo, valores, etc
			formularioAdapter.setListDesImp((ArrayList<DesImpVO>) ListUtilBean
					.toVO(DesImp.getListActivos(), 1, new DesImpVO(-1,
							StringUtil.SELECT_OPCION_SELECCIONAR)));

			formularioAdapter.setListFormatoSalida(FormatoSalida.getList());
			
			
			log.debug(funcName + ": exit");
			return formularioAdapter;
		} catch (Exception e) {
			log.error("Service Error: ", e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FormularioVO createFormulario(UserContext userContext,
			FormularioVO formularioVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled())
			log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			formularioVO.clearErrorMessages();

			// Copiado de propiadades de VO al BO
			Formulario formulario = new Formulario();

			this.copyFromVO(formulario, formularioVO);
			formulario.setEstado(Estado.ACTIVO.getId());

			// Aqui la creacion esta delegada en el manager, pero puede
			// corresponder a un Bean contenedor
			formulario = FrmFormularioManager.getInstance().createFormulario(
					formulario);

			if (formulario.hasError()) {
				tx.rollback();
				if (log.isDebugEnabled()) {
					log.debug(funcName + ": tx.rollback");
				}
			} else {
				tx.commit();
				if (log.isDebugEnabled()) {
					log.debug(funcName + ": tx.commit");
				}
				formularioVO = (FormularioVO) formulario.toVO(3);
			}
			formulario.passErrorMessages(formularioVO);

			log.debug(funcName + ": exit");
			return formularioVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ", e);
			if (tx != null)
				tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}


	public FormularioVO updateFormulario(UserContext userContext,
			FormularioVO formularioVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled())
			log.debug(funcName + ": enter");
		try {			
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			formularioVO.clearErrorMessages();

			Formulario formulario = Formulario.getById(formularioVO.getId());

			if (!formularioVO.validateVersion(formulario.getFechaUltMdf()))
				return formularioVO;

			// Copiado de propiadades de VO al BO
			this.copyFromVO(formulario, formularioVO);

			// Aqui la creacion esta delegada en el manager, pero puede
			// corresponder a un Bean contenedor
			formulario = FrmFormularioManager.getInstance().updateFormulario(formulario);

			if (formulario.hasError()) {
				tx.rollback();
				if (log.isDebugEnabled()) {
					log.debug(funcName + ": tx.rollback");
				}
			} else {
				tx.commit();
				if (log.isDebugEnabled()) {
					log.debug(funcName + ": tx.commit");
				}
				formularioVO = (FormularioVO) formulario.toVO(3);
			}
			formulario.passErrorMessages(formularioVO);

			log.debug(funcName + ": exit");
			return formularioVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ", e);
			if (tx != null)
				tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FormularioVO deleteFormulario(UserContext userContext,
			FormularioVO formularioVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled())
			log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			formularioVO.clearErrorMessages();

			// Se recupera el Bean dado su id
			Formulario formulario = Formulario.getById(formularioVO.getId());

			// Se le delega al Manager el borrado, pero puse ser responsabilidad
			// de otro bean
			formulario = FrmFormularioManager.getInstance().deleteFormulario(
					formulario);

			if (formulario.hasError()) {
				tx.rollback();
				if (log.isDebugEnabled()) {
					log.debug(funcName + ": tx.rollback");
				}
			} else {
				tx.commit();
				if (log.isDebugEnabled()) {
					log.debug(funcName + ": tx.commit");
				}
				formularioVO = (FormularioVO) formulario.toVO(3);
			}
			formulario.passErrorMessages(formularioVO);

			log.debug(funcName + ": exit");
			return formularioVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ", e);
			try {
				tx.rollback();
			} catch (Exception ee) {
			}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FormularioVO activarFormulario(UserContext userContext,
			FormularioVO formularioVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled())
			log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			Formulario formulario = Formulario.getById(formularioVO.getId());

			formulario.activar();

			if (formulario.hasError()) {
				tx.rollback();
				if (log.isDebugEnabled()) {
					log.debug(funcName + ": tx.rollback");
				}
			} else {
				tx.commit();
				if (log.isDebugEnabled()) {
					log.debug(funcName + ": tx.commit");
				}
				formularioVO = (FormularioVO) formulario.toVO();
			}
			formulario.passErrorMessages(formularioVO);

			log.debug(funcName + ": exit");
			return formularioVO;
		} catch (Exception e) {
			log.error("Service Error: ", e);
			if (tx != null)
				tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public FormularioVO desactivarFormulario(UserContext userContext,
			FormularioVO formularioVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled())
			log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

			Formulario formulario = Formulario.getById(formularioVO.getId());

			formulario.desactivar();

			if (formulario.hasError()) {
				tx.rollback();
				if (log.isDebugEnabled()) {
					log.debug(funcName + ": tx.rollback");
				}
			} else {
				tx.commit();
				if (log.isDebugEnabled()) {
					log.debug(funcName + ": tx.commit");
				}
				formularioVO = (FormularioVO) formulario.toVO();
			}
			formulario.passErrorMessages(formularioVO);

			log.debug(funcName + ": exit");
			return formularioVO;
		} catch (Exception e) {
			log.error("Service Error: ", e);
			if (tx != null)
				tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	private void copyFromVO(Formulario formulario, FormularioVO formularioVO) {
		formulario.setCodFormulario(formularioVO.getCodFormulario());
		formulario.setDesFormulario(formularioVO.getDesFormulario());
		formulario.setXsl(formularioVO.getXsl());
		formulario.setXslTxt(formularioVO.getXslTxt());
		formulario.setXmlTest(formularioVO.getXmlTest());
		formulario.setDesImp((formularioVO.getDesImp()!=null && formularioVO.getDesImp().getId()>0?DesImp.getById(formularioVO.getDesImp().getId()):null));
	}

	// <--- ABM Formulario

	// ---> ABM ForCam
	public ForCamAdapter getForCamAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled())
			log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ForCam forCam = ForCam.getById(commonKey.getId());

			ForCamAdapter forCamAdapter = new ForCamAdapter();
			forCamAdapter.setForCam((ForCamVO) forCam.toVO(1));

			log.debug(funcName + ": exit");
			return forCamAdapter;
		} catch (Exception e) {
			log.error("Service Error: ", e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ForCamAdapter getForCamAdapterForCreate(UserContext userContext,  CommonKey commonKeyIdForm) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ForCamAdapter forCamAdapter = new ForCamAdapter();
			ForCam forCam = new ForCam();
			forCam.setFormulario(Formulario.getById(commonKeyIdForm.getId()));
			forCamAdapter.setForCam((ForCamVO) forCam.toVO(1));
			// Seteo de banderas

			// Seteo la listas para combos, etc

			log.debug(funcName + ": exit");
			return forCamAdapter;
		} catch (Exception e) {
			log.error("Service Error: ", e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ForCamAdapter getForCamAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled())
			log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ForCam forCam = ForCam.getById(commonKey.getId());

			ForCamAdapter forCamAdapter = new ForCamAdapter();
			forCamAdapter.setForCam((ForCamVO) forCam.toVO(1));

			log.debug(funcName + ": exit");
			return forCamAdapter;
		} catch (Exception e) {
			log.error("Service Error: ", e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ForCamVO createForCam(UserContext userContext, ForCamVO forCamVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled())
			log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			forCamVO.clearErrorMessages();

			// Se recupera de la BD el bean que lo contiene
			Formulario formulario = Formulario.getById(forCamVO.getFormulario().getId());
			
			// Copiado de propiadades de VO al BO
			ForCam forCam = new ForCam();

			this.copyForCamFromVO(forCam, forCamVO);
			forCam.setEstado(Estado.ACTIVO.getId());
			
			// Aqui la creacion esta delegada en el Bean contenedor
			forCam = formulario.createForCam(forCam);

			if (forCam.hasError()) {
				tx.rollback();
				if (log.isDebugEnabled()) {
					log.debug(funcName + ": tx.rollback");
				}
			} else {
				tx.commit();
				if (log.isDebugEnabled()) {
					log.debug(funcName + ": tx.commit");
				}
				forCamVO = (ForCamVO) forCam.toVO(1);
			}
			forCam.passErrorMessages(forCamVO);

			log.debug(funcName + ": exit");
			return forCamVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ", e);
			if (tx != null)
				tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	private void copyForCamFromVO(ForCam forCam, ForCamVO forCamVO) {
		forCam.setCodForCam(forCamVO.getCodForCam());
		forCam.setDesForCam(forCamVO.getDesForCam());
		forCam.setValorDefecto(forCamVO.getValorDefecto());
		forCam.setLargoMax(forCamVO.getLargoMax());
		forCam.setFechaDesde(forCamVO.getFechaDesde());
		forCam.setFechaHasta(forCamVO.getFechaHasta());
		forCam.setFormulario(Formulario.getById(forCamVO.getFormulario().getId()));
	}

	public ForCamVO updateForCam(UserContext userContext, ForCamVO forCamVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled())
			log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			forCamVO.clearErrorMessages();

			// Se recupera de la BD el bean que lo contiene
			Formulario formulario = Formulario.getById(forCamVO.getFormulario().getId());
			
			// Copiado de propiadades de VO al BO
			ForCam forCam = ForCam.getById(forCamVO.getId());
			this.copyForCamFromVO(forCam, forCamVO);
			
			// Aqui la creacion esta delegada en el Bean contenedor
			forCam = formulario.updateForCam(forCam);

			if (forCam.hasError()) {
				tx.rollback();
				if (log.isDebugEnabled()) {
					log.debug(funcName + ": tx.rollback");
				}
			} else {
				tx.commit();
				if (log.isDebugEnabled()) {
					log.debug(funcName + ": tx.commit");
				}
				forCamVO = (ForCamVO) forCam.toVO(1);
			}
			forCam.passErrorMessages(forCamVO);

			log.debug(funcName + ": exit");
			return forCamVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ", e);
			if (tx != null)
				tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ForCamVO deleteForCam(UserContext userContext, ForCamVO forCamVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled())
			log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			forCamVO.clearErrorMessages();

			// Se recupera de la BD el bean que lo contiene
			Formulario formulario = Formulario.getById(forCamVO.getFormulario().getId());
			
			ForCam forCam = ForCam.getById(forCamVO.getId());
			
			// Aqui la creacion esta delegada en el Bean contenedor
			forCam = formulario.deleteForCam(forCam);

			if (forCam.hasError()) {
				tx.rollback();
				if (log.isDebugEnabled()) {
					log.debug(funcName + ": tx.rollback");
				}
			} else {
				tx.commit();
				if (log.isDebugEnabled()) {
					log.debug(funcName + ": tx.commit");
				}
				forCamVO = (ForCamVO) forCam.toVO(1);
			}
			forCam.passErrorMessages(forCamVO);

			log.debug(funcName + ": exit");
			return forCamVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ", e);
			if (tx != null)
				tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
/*
	public ForCamVO activarForCam(UserContext userContext, ForCamVO forCamVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled())
			log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			forCamVO.clearErrorMessages();

			ForCam forCam = ForCam.getById(forCamVO.getId());
			forCam.activar();

			if (forCam.hasError()) {
				tx.rollback();
				if (log.isDebugEnabled()) {
					log.debug(funcName + ": tx.rollback");
				}
			} else {
				tx.commit();
				if (log.isDebugEnabled()) {
					log.debug(funcName + ": tx.commit");
				}
				forCamVO = (ForCamVO) forCam.toVO(1);
			}
			forCam.passErrorMessages(forCamVO);

			log.debug(funcName + ": exit");
			return forCamVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ", e);
			if (tx != null)
				tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ForCamVO desactivarForCam(UserContext userContext, ForCamVO forCamVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled())
			log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			forCamVO.clearErrorMessages();

			ForCam forCam = ForCam.getById(forCamVO.getId());
			forCam.desactivar();

			if (forCam.hasError()) {
				tx.rollback();
				if (log.isDebugEnabled()) {
					log.debug(funcName + ": tx.rollback");
				}
			} else {
				tx.commit();
				if (log.isDebugEnabled()) {
					log.debug(funcName + ": tx.commit");
				}
				forCamVO = (ForCamVO) forCam.toVO(1);
			}
			forCam.passErrorMessages(forCamVO);

			log.debug(funcName + ": exit");
			return forCamVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ", e);
			if (tx != null)
				tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	*/
	// <--- ABM ForCam
	
}
