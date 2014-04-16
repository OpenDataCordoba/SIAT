//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.buss.service;

/**
 * Implementacion de servicios del submodulo DecJur del modulo Afi
 * @author tecso
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.afi.buss.bean.ActLoc;
import ar.gov.rosario.siat.afi.buss.bean.DatosDomicilio;
import ar.gov.rosario.siat.afi.buss.bean.DatosPagoCta;
import ar.gov.rosario.siat.afi.buss.bean.DecActLoc;
import ar.gov.rosario.siat.afi.buss.bean.EstForDecJur;
import ar.gov.rosario.siat.afi.buss.bean.ExeActLoc;
import ar.gov.rosario.siat.afi.buss.bean.ForDecJur;
import ar.gov.rosario.siat.afi.buss.bean.HabLoc;
import ar.gov.rosario.siat.afi.buss.bean.Local;
import ar.gov.rosario.siat.afi.buss.bean.OtrosPagos;
import ar.gov.rosario.siat.afi.buss.bean.RetYPer;
import ar.gov.rosario.siat.afi.buss.bean.Socio;
import ar.gov.rosario.siat.afi.buss.bean.TotDerYAccDJ;
import ar.gov.rosario.siat.afi.buss.dao.AfiDAOFactory;
import ar.gov.rosario.siat.afi.iface.model.ActLocAdapter;
import ar.gov.rosario.siat.afi.iface.model.ActLocVO;
import ar.gov.rosario.siat.afi.iface.model.DatosDomicilioAdapter;
import ar.gov.rosario.siat.afi.iface.model.DatosDomicilioVO;
import ar.gov.rosario.siat.afi.iface.model.DatosPagoCtaAdapter;
import ar.gov.rosario.siat.afi.iface.model.DatosPagoCtaVO;
import ar.gov.rosario.siat.afi.iface.model.DecActLocAdapter;
import ar.gov.rosario.siat.afi.iface.model.DecActLocVO;
import ar.gov.rosario.siat.afi.iface.model.EstForDecJurVO;
import ar.gov.rosario.siat.afi.iface.model.ExeActLocAdapter;
import ar.gov.rosario.siat.afi.iface.model.ExeActLocVO;
import ar.gov.rosario.siat.afi.iface.model.ForDecJurAdapter;
import ar.gov.rosario.siat.afi.iface.model.ForDecJurSearchPage;
import ar.gov.rosario.siat.afi.iface.model.ForDecJurVO;
import ar.gov.rosario.siat.afi.iface.model.HabLocAdapter;
import ar.gov.rosario.siat.afi.iface.model.HabLocVO;
import ar.gov.rosario.siat.afi.iface.model.LocalAdapter;
import ar.gov.rosario.siat.afi.iface.model.LocalVO;
import ar.gov.rosario.siat.afi.iface.model.OtrosPagosAdapter;
import ar.gov.rosario.siat.afi.iface.model.OtrosPagosVO;
import ar.gov.rosario.siat.afi.iface.model.RetYPerAdapter;
import ar.gov.rosario.siat.afi.iface.model.RetYPerVO;
import ar.gov.rosario.siat.afi.iface.model.SocioAdapter;
import ar.gov.rosario.siat.afi.iface.model.SocioVO;
import ar.gov.rosario.siat.afi.iface.model.TotDerYAccDJAdapter;
import ar.gov.rosario.siat.afi.iface.model.TotDerYAccDJVO;
import ar.gov.rosario.siat.afi.iface.service.IAfiFormulariosDJService;
import ar.gov.rosario.siat.afi.iface.util.AfiError;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.gde.buss.bean.DecJur;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.TipPagDecJurVO;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.DemodaStringMsg;
import coop.tecso.demoda.iface.model.FormularioAfip;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.UserContext;

public class AfiFormulariosDJServiceHbmImpl implements IAfiFormulariosDJService {
	private Logger log = Logger.getLogger(AfiFormulariosDJServiceHbmImpl.class);
	
	// ---> ABM ForDecJur 	
	public ForDecJurSearchPage getForDecJurSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			ForDecJurSearchPage forDecJurSearchPage = new ForDecJurSearchPage();
		
			// Se cargar lista de Recurso habilitados
			List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
			listRecurso.add(new RecursoVO(-1 ,StringUtil.SELECT_OPCION_TODOS));
			Recurso recDREI = Recurso.getByCodigo(Recurso.COD_RECURSO_DReI);
			listRecurso.add((RecursoVO) recDREI.toVOWithCategoria());
			Recurso recETUR = Recurso.getByCodigo(Recurso.COD_RECURSO_ETuR);
			listRecurso.add((RecursoVO) recETUR.toVOWithCategoria());
			forDecJurSearchPage.setListRecurso(listRecurso);
			forDecJurSearchPage.getForDecJur().getRecurso().setId(-1L);
			
			// Se carga lista de Formularios que generan DJ
			List<FormularioAfip> listFormulario = new ArrayList<FormularioAfip>(); 
			listFormulario.add(FormularioAfip.TODOS);	
			listFormulario.addAll(FormularioAfip.getListDJ());
			forDecJurSearchPage.setListFormulario(listFormulario);
			forDecJurSearchPage.getForDecJur().setNroFormulario(FormularioAfip.TODOS.getId());
			
			// Se carga la lista de Estados
			forDecJurSearchPage.setListEstForDecJur((ArrayList<EstForDecJurVO>) ListUtilBean.toVO(EstForDecJur.getListActivos(),0,
					new EstForDecJurVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			
			// Tipos de Pago
			List<TipPagDecJurVO> listTipPagDecJur = new ArrayList<TipPagDecJurVO>();
			listTipPagDecJur.add(new TipPagDecJurVO(-1, StringUtil.SELECT_OPCION_TODOS));
			listTipPagDecJur.add(new TipPagDecJurVO(1, "Retenciones"));
			listTipPagDecJur.add(new TipPagDecJurVO(2, "Otros Pagos"));
			forDecJurSearchPage.setListTipPagDecJur(listTipPagDecJur);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return forDecJurSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ForDecJurSearchPage getForDecJurSearchPageResult(UserContext userContext, ForDecJurSearchPage forDecJurSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			forDecJurSearchPage.clearError();
			
			ForDecJurVO forDecJurVO = forDecJurSearchPage.getForDecJur();
			/* Mantis #7876 Prod. I:
			 *  - Modificación sobre la consulta de Formularios de DDJJ AFIP
			 */
			//Validaciones
			forDecJurSearchPage.addRecoverableError(BaseError.MSG_FILTROS_REQUERIDOS);
			//Numero de Envio AFIP
			if (StringUtil.isNullOrEmpty(forDecJurVO.getEnvioOsiris().getIdEnvioAfipView())) {
				forDecJurSearchPage.addRecoverableError(AfiError.FORDECJUR_ENVIO_AFIP);
			}
			//Id. Transaccion AFIP
			if(StringUtil.isNullOrEmpty(forDecJurVO.getTranAfip().getIdTransaccionAfipView())){
				forDecJurSearchPage.addRecoverableError(AfiError.FORDECJUR_IDTRANSACCION_AFIP);
			}
			//CUIT
			if(StringUtil.isNullOrEmpty(forDecJurVO.getCuit())){
				forDecJurSearchPage.addRecoverableError(AfiError.FORDECJUR_CUIT);
			}
			//Fecha Presentacion Desde/Hasta
			Date fechaDesde = forDecJurSearchPage.getFechaPresentacionDesde();
			Date fechaHasta = forDecJurSearchPage.getFechaPresentacionHasta();
			if(null == fechaDesde){
				forDecJurSearchPage.addRecoverableError(AfiError.FORDECJUR_FECHA_PRESENTACION_DESDE);
			}else if(null == fechaHasta){
				forDecJurSearchPage.addRecoverableError(AfiError.FORDECJUR_FECHA_PRESENTACION_HASTA);
			}
			
			//Solo uno de los campos es requerido
			if(forDecJurSearchPage.getListError().size() == 5){
				return forDecJurSearchPage;
			}
			forDecJurSearchPage.clearError();
			
			if(null != fechaDesde && null != fechaHasta){
				//Validaciones de rango
				if(DateUtil.isDateAfter(fechaDesde, fechaHasta)){
					//Fecha Desde mayor o igual a Fecha Hasta
					forDecJurSearchPage.addRecoverableError(BaseError.MSG_VALORMAYORQUE,AfiError.FORDECJUR_FECHA_PRESENTACION_DESDE,AfiError.FORDECJUR_FECHA_PRESENTACION_HASTA);
				}else if(DateUtil.getCantMeses(fechaDesde, fechaHasta) > 3){
					//Rango de fechas superior a 3 meses
					forDecJurSearchPage.addRecoverableError(BaseError.MSG_RANGO_INVALIDO,AfiError.FORDECJUR_FECHA_PRESENTACION);
				}
			}
			
			if(forDecJurSearchPage.hasError()){
				return forDecJurSearchPage;
			}

			// Aqui obtiene lista de BOs
	   		List<ForDecJur> listForDecJur = AfiDAOFactory.getForDecJurDAO().getBySearchPage(forDecJurSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		List<ForDecJurVO> listForDecJurVO = new ArrayList<ForDecJurVO>();
	   		for(ForDecJur forDecJur: listForDecJur){
	   			forDecJurVO = (ForDecJurVO) forDecJur.toVO(1, false);
	   			if(forDecJur.getEstForDecJur().getId().longValue() == EstForDecJur.ID_PROCESADA){
	   				forDecJurVO.setGenerarDecJurBussEnabled(false);
	   			}
	   			listForDecJurVO.add(forDecJurVO);
	   		}
			//Aqui pasamos BO a VO
	   		forDecJurSearchPage.setListResult(listForDecJurVO);
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return forDecJurSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ForDecJurAdapter getForDecJurAdapterForView(UserContext userContext, CommonKey commonKey, Long idDecJur) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Long idForDecJur = null; 
			if(idDecJur != null){
				DecJur decJur = DecJur.getById(idDecJur);
				idForDecJur = decJur.getForDecJur().getId();
			}else{
				idForDecJur = commonKey.getId();				
			}
			
			ForDecJur forDecJur = ForDecJur.getById(idForDecJur);

	        ForDecJurAdapter forDecJurAdapter = new ForDecJurAdapter();
	        forDecJurAdapter.setForDecJur((ForDecJurVO) forDecJur.toVO(1));
	        if(forDecJur.getRegimenEspecial() != null && forDecJur.getRegimenEspecial().intValue() == SiNo.SI.getId().intValue())
	        	forDecJurAdapter.setParamRegimenEspecial(true);
	        
	        List<DatosDomicilioVO> listDatosDomicilio = new ArrayList<DatosDomicilioVO>();
	        for(DatosDomicilio datosDomicilio: forDecJur.getListDatosDomicilio()){
	        	DatosDomicilioVO datosDomicilioVO = (DatosDomicilioVO) datosDomicilio.toVO(0, false);
	        	datosDomicilioVO.setListLocal((ArrayList<LocalVO>) ListUtilBean.toVO(datosDomicilio.getListLocal(),0,false));
	        	datosDomicilioVO.setListSocio((ArrayList<SocioVO>) ListUtilBean.toVO(datosDomicilio.getListSocio(),0,false));
	        	listDatosDomicilio.add(datosDomicilioVO);
	        }
	        forDecJurAdapter.getForDecJur().setListDatosDomicilio(listDatosDomicilio);
			
			log.debug(funcName + ": exit");
			return forDecJurAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ForDecJurAdapter imprimirForDecJur(UserContext userContext, ForDecJurAdapter forDecJurAdapterVO ) throws DemodaServiceException {
	    String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ForDecJur forDecJur = ForDecJur.getById(forDecJurAdapterVO.getForDecJur().getId());

			AfiDAOFactory.getForDecJurDAO().imprimirGenerico(forDecJur, forDecJurAdapterVO.getReport());
	   		
			log.debug(funcName + ": exit");
			return forDecJurAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();        
	    } 
	}

	// <--- ABM ForDecJur
	
	public ActLocAdapter getActLocAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ActLoc actLoc = ActLoc.getById(commonKey.getId());

			ActLocAdapter actLocAdapter = new ActLocAdapter();
			actLocAdapter.setActLoc((ActLocVO) actLoc.toVO(1));
			
			log.debug(funcName + ": exit");
			return actLocAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}		
	}

	public DatosDomicilioAdapter getDatosDomicilioAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			DatosDomicilio datosDomicilio = DatosDomicilio.getById(commonKey.getId());

			DatosDomicilioAdapter datosDomicilioAdapter = new DatosDomicilioAdapter();
			datosDomicilioAdapter.setDatosDomicilio((DatosDomicilioVO) datosDomicilio.toVO(1,false));
			datosDomicilioAdapter.getDatosDomicilio().setListLocal((ArrayList<LocalVO>) ListUtilBean.toVO(datosDomicilio.getListLocal(),0,false));
			datosDomicilioAdapter.getDatosDomicilio().setListSocio((ArrayList<SocioVO>) ListUtilBean.toVO(datosDomicilio.getListSocio(),0,false));
			
			log.debug(funcName + ": exit");
			return datosDomicilioAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}		
	}

	public DatosPagoCtaAdapter getDatosPagoCtaAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			DatosPagoCta datosPagoCta = DatosPagoCta.getById(commonKey.getId());

			DatosPagoCtaAdapter datosPagoCtaAdapter = new DatosPagoCtaAdapter();
			datosPagoCtaAdapter.setDatosPagoCta((DatosPagoCtaVO) datosPagoCta.toVO(1));
			
			log.debug(funcName + ": exit");
			return datosPagoCtaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public DecActLocAdapter getDecActLocAdapterForView(UserContext userContext,	CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			DecActLoc decActLoc = DecActLoc.getById(commonKey.getId());

			DecActLocAdapter decActLocAdapter = new DecActLocAdapter();
			decActLocAdapter.setDecActLoc((DecActLocVO) decActLoc.toVO(1));
			
			if(Recurso.COD_RECURSO_ETuR.equals(decActLoc.getLocal().getForDecJur().getRecurso().getCodRecurso())){
				decActLocAdapter.setParamEtur(true);
			}
			
			log.debug(funcName + ": exit");
			return decActLocAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ExeActLocAdapter getExeActLocAdapterForView(UserContext userContext,	CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ExeActLoc exeActLoc = ExeActLoc.getById(commonKey.getId());

			ExeActLocAdapter exeActLocAdapter = new ExeActLocAdapter();
			exeActLocAdapter.setExeActLoc((ExeActLocVO) exeActLoc.toVO(1));
			
			log.debug(funcName + ": exit");
			return exeActLocAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public HabLocAdapter getHabLocAdapterForView(UserContext userContext,CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			HabLoc habLoc = HabLoc.getById(commonKey.getId());

			HabLocAdapter habLocAdapter = new HabLocAdapter();
			habLocAdapter.setHabLoc((HabLocVO) habLoc.toVO(1));
			
			log.debug(funcName + ": exit");
			return habLocAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public LocalAdapter getLocalAdapterForView(UserContext userContext,CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Local local = Local.getById(commonKey.getId());

			LocalAdapter localAdapter = new LocalAdapter();
			localAdapter.setLocal((LocalVO) local.toVO(1));
			
			log.debug(funcName + ": exit");
			return localAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public OtrosPagosAdapter getOtrosPagosAdapterForView(	UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			OtrosPagos otrosPagos = OtrosPagos.getById(commonKey.getId());

			OtrosPagosAdapter otrosPagosAdapter = new OtrosPagosAdapter();
			otrosPagosAdapter.setOtrosPagos((OtrosPagosVO) otrosPagos.toVO(1));
			
			log.debug(funcName + ": exit");
			return otrosPagosAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public RetYPerAdapter getRetYPerAdapterForView(UserContext userContext,	CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			RetYPer retYPer = RetYPer.getById(commonKey.getId());

			RetYPerAdapter retYPerAdapter = new RetYPerAdapter();
			retYPerAdapter.setRetYPer((RetYPerVO) retYPer.toVO(1));
			
			log.debug(funcName + ": exit");
			return retYPerAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public SocioAdapter getSocioAdapterForView(UserContext userContext,CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Socio socio = Socio.getById(commonKey.getId());

			SocioAdapter socioAdapter = new SocioAdapter();
			socioAdapter.setSocio((SocioVO) socio.toVO(1));
			
			if(socio.getDatosDomicilio() != null){
				socioAdapter.setParamDatosDomicilio(true);
			}
			
			log.debug(funcName + ": exit");
			return socioAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TotDerYAccDJAdapter getTotDerYAccDJAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TotDerYAccDJ totDerYAccDJ = TotDerYAccDJ.getById(commonKey.getId());

			TotDerYAccDJAdapter totDerYAccDJAdapter = new TotDerYAccDJAdapter();
			totDerYAccDJAdapter.setTotDerYAccDJ((TotDerYAccDJVO) totDerYAccDJ.toVO(1));
			
			log.debug(funcName + ": exit");
			return totDerYAccDJAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ForDecJurVO generarDecJur(UserContext userContext, ForDecJurVO forDecJurVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			forDecJurVO.clearErrorMessages();

			ForDecJur forDecJur = ForDecJur.getById(forDecJurVO.getId());	
			
			List<DecJur> listDecJur = null;
			try{
				listDecJur = forDecJur.generarDecJur();						
			}catch (Exception e) {
				forDecJur.addRecoverableValueError("ForDecJur de id: "+forDecJur.getId()+". Error inesperado al intentar generar Declaraciones Juradas SIAT para el Formulario de Declaración Jurada AFIP");
				log.error("ForDecJur de id: "+forDecJur.getId()+". Error inesperado al intentar generar Declaraciones Juradas SIAT para el Formulario de Declaración Jurada AFIP");
				log.error("ForDecJur de id: "+forDecJur.getId(), e);
			}
			
			List<LiqDeudaVO> listDeuda = new ArrayList<LiqDeudaVO>();
			
			if(listDecJur != null && !forDecJur.hasError()){
				// Si termino sin errores pasamos a procesar las declaraciones juradas impactando sobre la deuda
				for (DecJur decJur: listDecJur){
					
					decJur.procesarDDJJ(listDeuda,null, true); 
					
					if (decJur.hasError()){
						decJur.passErrorMessages(forDecJur);
					}
				}				
			}
			
			// Cambia el estado del forDecJur a procesado
			if(forDecJur.hasError()){
				if(tx != null){
					tx.rollback();
					tx = session.beginTransaction();
				}
				String observaciones = "Se encontraron los siguientes errores al intentar generar las DecJur: ";
				for(DemodaStringMsg error: forDecJur.getListError()){
					observaciones += "\n - "+error.key().substring(1);							
				}
				forDecJur.setObservaciones(observaciones);
			}else{
				forDecJur.setEstForDecJur(EstForDecJur.getById(EstForDecJur.ID_PROCESADA)); 
			}	
			AfiDAOFactory.getForDecJurDAO().update(forDecJur);
			if(tx != null) tx.commit();
			
			forDecJur.passErrorMessages(forDecJurVO);

			log.debug(funcName + ": exit");
			return forDecJurVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
}
