//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.service;

/**
 * Implementacion de servicios de submodulo Gdeuda del modulo gde
 * @author tecso
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.BalReclamoManager;
import ar.gov.rosario.siat.bal.buss.bean.Reclamo;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.SiatUtil;
import ar.gov.rosario.siat.cas.buss.bean.AccionExp;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.cas.iface.util.CasError;
import ar.gov.rosario.siat.cyq.buss.bean.ProDet;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.buss.bean.Banco;
import ar.gov.rosario.siat.def.buss.bean.Categoria;
import ar.gov.rosario.siat.def.buss.bean.RecAli;
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.RecConADec;
import ar.gov.rosario.siat.def.buss.bean.RecTipAli;
import ar.gov.rosario.siat.def.buss.bean.RecTipUni;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.def.buss.bean.TipObjImp;
import ar.gov.rosario.siat.def.buss.bean.TipObjImpAtr;
import ar.gov.rosario.siat.def.buss.bean.TipRecConADec;
import ar.gov.rosario.siat.def.buss.bean.ValUnRecConADe;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.model.AtrEmisionDefinition;
import ar.gov.rosario.siat.def.iface.model.GenericDefinition;
import ar.gov.rosario.siat.def.iface.model.RecAliVO;
import ar.gov.rosario.siat.def.iface.model.RecClaDeuVO;
import ar.gov.rosario.siat.def.iface.model.RecConADecVO;
import ar.gov.rosario.siat.def.iface.model.RecTipUniVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.ServicioBancoVO;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.emi.buss.bean.Emision;
import ar.gov.rosario.siat.esp.buss.bean.Habilitacion;
import ar.gov.rosario.siat.exe.buss.bean.CueExe;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.gde.buss.bean.AgeRet;
import ar.gov.rosario.siat.gde.buss.bean.Anulacion;
import ar.gov.rosario.siat.gde.buss.bean.CierreComercio;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.ConvenioCuota;
import ar.gov.rosario.siat.gde.buss.bean.CtrlInfDeu;
import ar.gov.rosario.siat.gde.buss.bean.DecJur;
import ar.gov.rosario.siat.gde.buss.bean.DecJurDet;
import ar.gov.rosario.siat.gde.buss.bean.DecJurPag;
import ar.gov.rosario.siat.gde.buss.bean.DeuAdmRecCon;
import ar.gov.rosario.siat.gde.buss.bean.DeuRecCon;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAnulada;
import ar.gov.rosario.siat.gde.buss.bean.DeudaJudicial;
import ar.gov.rosario.siat.gde.buss.bean.EstadoDeuda;
import ar.gov.rosario.siat.gde.buss.bean.GdeGDeudaManager;
import ar.gov.rosario.siat.gde.buss.bean.HisInfDeu;
import ar.gov.rosario.siat.gde.buss.bean.LiqDeudaBeanHelper;
import ar.gov.rosario.siat.gde.buss.bean.MotAnuDeu;
import ar.gov.rosario.siat.gde.buss.bean.OriDecJur;
import ar.gov.rosario.siat.gde.buss.bean.PagoDeuda;
import ar.gov.rosario.siat.gde.buss.bean.Plan;
import ar.gov.rosario.siat.gde.buss.bean.Recibo;
import ar.gov.rosario.siat.gde.buss.bean.ReciboBeanHelper;
import ar.gov.rosario.siat.gde.buss.bean.ReciboConvenio;
import ar.gov.rosario.siat.gde.buss.bean.TipDecJurRec;
import ar.gov.rosario.siat.gde.buss.bean.TipPagDecJur;
import ar.gov.rosario.siat.gde.buss.bean.TipoTramite;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.AnulacionVO;
import ar.gov.rosario.siat.gde.iface.model.CierreComercioVO;
import ar.gov.rosario.siat.gde.iface.model.ConvenioSearchPage;
import ar.gov.rosario.siat.gde.iface.model.ConvenioVO;
import ar.gov.rosario.siat.gde.iface.model.CtrlInfDeuAdapter;
import ar.gov.rosario.siat.gde.iface.model.CtrlInfDeuVO;
import ar.gov.rosario.siat.gde.iface.model.CuentaObjImpSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DecJurAdapter;
import ar.gov.rosario.siat.gde.iface.model.DecJurDetAdapter;
import ar.gov.rosario.siat.gde.iface.model.DecJurDetVO;
import ar.gov.rosario.siat.gde.iface.model.DecJurPagAdapter;
import ar.gov.rosario.siat.gde.iface.model.DecJurPagVO;
import ar.gov.rosario.siat.gde.iface.model.DecJurSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DecJurVO;
import ar.gov.rosario.siat.gde.iface.model.DeudaContribSearchPage;
import ar.gov.rosario.siat.gde.iface.model.EstadoCuentaAdapter;
import ar.gov.rosario.siat.gde.iface.model.EstadoCuentaSearchPage;
import ar.gov.rosario.siat.gde.iface.model.EstadoDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.InformeDeudaCaratula;
import ar.gov.rosario.siat.gde.iface.model.LiqAtrEmisionVO;
import ar.gov.rosario.siat.gde.iface.model.LiqAtrValorVO;
import ar.gov.rosario.siat.gde.iface.model.LiqCodRefPagSearchPage;
import ar.gov.rosario.siat.gde.iface.model.LiqConceptoDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqConvenioCuentaAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqCuentaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqCuotaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDetalleDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAdminVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaCyQVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaPagoDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaProcuradorVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqExencionVO;
import ar.gov.rosario.siat.gde.iface.model.LiqExencionesVO;
import ar.gov.rosario.siat.gde.iface.model.LiqPagoDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqReciboVO;
import ar.gov.rosario.siat.gde.iface.model.LiqReclamoAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqReclamoVO;
import ar.gov.rosario.siat.gde.iface.model.MotAnuDeuVO;
import ar.gov.rosario.siat.gde.iface.model.ReciboAdapter;
import ar.gov.rosario.siat.gde.iface.model.ReciboSearchPage;
import ar.gov.rosario.siat.gde.iface.model.ReciboVO;
import ar.gov.rosario.siat.gde.iface.model.RubroVO;
import ar.gov.rosario.siat.gde.iface.model.TipDecJurVO;
import ar.gov.rosario.siat.gde.iface.model.TipPagDecJurVO;
import ar.gov.rosario.siat.gde.iface.model.TipoTramiteVO;
import ar.gov.rosario.siat.gde.iface.model.TramiteAdapter;
import ar.gov.rosario.siat.gde.iface.model.TramiteSearchPage;
import ar.gov.rosario.siat.gde.iface.service.IGdeGDeudaService;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.pad.buss.bean.Contribuyente;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.CuentaTitular;
import ar.gov.rosario.siat.pad.buss.bean.ObjImp;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import ar.gov.rosario.siat.pad.buss.bean.TipoDocumento;
import ar.gov.rosario.siat.pad.buss.bean.VariosWebFacade;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.model.ContribuyenteVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaTitularVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.iface.model.ObjImpVO;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpAtrDefinition;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpDefinition;
import ar.gov.rosario.siat.pad.iface.model.TipoDocumentoVO;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import ar.gov.rosario.siat.seg.buss.bean.UsuarioSiat;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaTimer;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.EstadoPeriodo;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.TipoBoleta;
import coop.tecso.demoda.iface.model.TipoRecibo;
import coop.tecso.demoda.iface.model.UserContext;

public class GdeGDeudaServiceHbmImpl implements IGdeGDeudaService { 
	
	private Logger log = Logger.getLogger(GdeGDeudaServiceHbmImpl.class);

	// ---> Deuda por Contribuyente
	public DeudaContribSearchPage getDeudaContribSearchPageInit(UserContext userContext, CommonKey contribuyenteKey) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			DeudaContribSearchPage deudaContribSearchPage = new DeudaContribSearchPage();
			
			deudaContribSearchPage.setListResult(new ArrayList());
			
			if (contribuyenteKey == null || contribuyenteKey.getId() == null ){
				return deudaContribSearchPage;
			}
			
			Contribuyente contribuyente = Contribuyente.getById(contribuyenteKey.getId());
			//Aqui pasamos BO a VO
			ContribuyenteVO contribuyenteVO = (ContribuyenteVO) contribuyente.toVO(2, false);
			
			// por el momento no permitimos imprimir la lista de deudas porque no tienen implementacion
			contribuyenteVO.setImprimirListDeudaContribBussEnabled(Boolean.FALSE);
			
			deudaContribSearchPage.setContribuyente(contribuyenteVO);
			deudaContribSearchPage.setPageNumber(1L);
			
			// contribuyente.getListCuenta() realiza un count y si supera las 100 cuentas, solo devuelve las primeras
			List<CuentaTitular> listCuentaTitular = contribuyente.getListCuentaBySearchPage(deudaContribSearchPage);
			
			List<CuentaTitularVO> listCuentaTitularVO = new ArrayList<CuentaTitularVO>();
			
			for (CuentaTitular ct:listCuentaTitular){
				CuentaTitularVO cuentaTitularVO = (CuentaTitularVO) ct.toVO(0, false);
				cuentaTitularVO.setCuenta(ct.getCuenta().toVOWithRecurso());
				listCuentaTitularVO.add(cuentaTitularVO);
			}
			
			// seteo de banderas: por el momento sin permiso porque no tienen implementacion
			for (CuentaTitularVO cuentaTitularVO: listCuentaTitularVO) {
				cuentaTitularVO.getCuenta().setEstadoDeudaBussEnabled(Boolean.FALSE);
				cuentaTitularVO.getCuenta().setLiquidacionDeudaBussEnabled(Boolean.TRUE);
			}

			log.debug("contribuyente.SuperaMaxCantCuentas(): " + contribuyente.getSuperaMaxCantCuentas());
			
			// Seteamos bandera para que muestre el paginado
			if (contribuyente.getSuperaMaxCantCuentas()){
				deudaContribSearchPage.setSuperaMaxCantCuentas(true);
				deudaContribSearchPage.clearMessage();
				deudaContribSearchPage.addMessageValue("El Contribuyente posee " + contribuyente.getCountCuentasContrib() + " cuentas.");
			}else{
				deudaContribSearchPage.setSuperaMaxCantCuentas(false);
			}
						
			deudaContribSearchPage.setListResult(listCuentaTitularVO);
			
			// Obtenemos los servivios banco que posean recurso.
			List<ServicioBanco> listServicoBanco = ServicioBanco.getListPoseeRecurso(); 
			
			deudaContribSearchPage.setListServicioBanco((List<ServicioBancoVO>) ListUtilBean.toVO(listServicoBanco, 1, 
					new ServicioBancoVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			
						
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return deudaContribSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DeudaContribSearchPage getDeudaContribSearchPageParamContribuyente(UserContext userContext, DeudaContribSearchPage deudaContribSearchPage) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			deudaContribSearchPage.setListResult(new ArrayList());
			
			Contribuyente contribuyente = Contribuyente.getByIdNull(deudaContribSearchPage.getContribuyente().getId());
			
			List<CuentaTitular> listCuentaTitular = new ArrayList<CuentaTitular>();
			ContribuyenteVO contribuyenteVO;
			
			if (contribuyente != null){
				//Aqui pasamos BO a VO
				contribuyenteVO = (ContribuyenteVO) contribuyente.toVO(2,false);
				
				// por el momento no permitimos imprimir la lista de deudas porque no tiene implementacion
				contribuyenteVO.setImprimirListDeudaContribBussEnabled(Boolean.FALSE);
	
				listCuentaTitular = contribuyente.getListCuentaBySearchPage(deudaContribSearchPage);
			} else {
				// Si el contribuyente no fue encontrado, seteamos la persona.
				Persona persona = Persona.getById(deudaContribSearchPage.getContribuyente().getId());
				contribuyenteVO = new ContribuyenteVO();
				contribuyenteVO.setId(persona.getId());
				contribuyenteVO.setPersona((PersonaVO)persona.toVO(2));
				
			}

			deudaContribSearchPage.setContribuyente(contribuyenteVO);
			
			List<CuentaTitularVO> listCuentaTitularVO = new ArrayList<CuentaTitularVO>();
			
			for (CuentaTitular ct:listCuentaTitular){
				CuentaTitularVO cuentaTitularVO = (CuentaTitularVO) ct.toVO(0, false);
				cuentaTitularVO.setCuenta(ct.getCuenta().toVOWithRecurso());
				
				// Seteamos bandera para el boton emitir.
				if (ct.getCuenta().getRecurso().getPerEmiDeuPuntual() != null &&
						ct.getCuenta().getRecurso().getPerEmiDeuPuntual().intValue() == 1 &&
							ct.getCuenta().getRecurso().permiteCrearEmitir()){
					cuentaTitularVO.setRecursoPermiteEmision(true);
				}
				
				listCuentaTitularVO.add(cuentaTitularVO);
			}
				
			for (CuentaTitularVO cuentaTitularVO: listCuentaTitularVO) {
				cuentaTitularVO.getCuenta().setEstadoDeudaBussEnabled(Boolean.FALSE);
				cuentaTitularVO.getCuenta().setLiquidacionDeudaBussEnabled(Boolean.TRUE);
			}
			
			if (contribuyente != null){
				log.debug("contribuyente.SuperaMaxCantCuentas(): " + contribuyente.getSuperaMaxCantCuentas());
				
				// Seteamos bandera para que muestre el paginado
				if (contribuyente.getSuperaMaxCantCuentas()){
					deudaContribSearchPage.setSuperaMaxCantCuentas(true);
					deudaContribSearchPage.clearMessage();
					deudaContribSearchPage.addMessageValue("El Contribuyente posee " + contribuyente.getCountCuentasContrib() + " cuentas.");
				}else{
					deudaContribSearchPage.setSuperaMaxCantCuentas(false);
				}
			}
			
			deudaContribSearchPage.setListResult(listCuentaTitularVO);
			
			
			// Obtenemos los servivios banco que posean recurso.
			List<ServicioBanco> listServicoBanco = ServicioBanco.getListPoseeRecurso(); 
			
			deudaContribSearchPage.setListServicioBanco((List<ServicioBancoVO>) ListUtilBean.toVO(listServicoBanco, 1, 
					new ServicioBancoVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return deudaContribSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	public DeudaContribSearchPage getDeudaContribSearchPageParamServicioBanco(UserContext userContext, DeudaContribSearchPage deudaContribSearchPage) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			deudaContribSearchPage.setListResult(new ArrayList());
			
			Contribuyente contribuyente = Contribuyente.getByIdNull(deudaContribSearchPage.getContribuyente().getId());
			
			List<CuentaTitular> listCuentaTitular = new ArrayList<CuentaTitular>();
			ContribuyenteVO contribuyenteVO;
			
			if (contribuyente != null){
				//Aqui pasamos BO a VO
				contribuyenteVO = (ContribuyenteVO) contribuyente.toVO(2,false);
				
				// por el momento no permitimos imprimir la lista de deudas porque no tiene implementacion
				contribuyenteVO.setImprimirListDeudaContribBussEnabled(Boolean.FALSE);
	
				listCuentaTitular = contribuyente.getListCuentaBySearchPage(deudaContribSearchPage);
			} else {
				// Si el contribuyente no fue encontrado, seteamos la persona.
				Persona persona = Persona.getById(deudaContribSearchPage.getContribuyente().getId());
				contribuyenteVO = new ContribuyenteVO();
				contribuyenteVO.setId(persona.getId());
				contribuyenteVO.setPersona((PersonaVO)persona.toVO(2));
				
			}

			deudaContribSearchPage.setContribuyente(contribuyenteVO);
			
			List<CuentaTitularVO> listCuentaTitularVO = new ArrayList<CuentaTitularVO>();
			
			for (CuentaTitular ct:listCuentaTitular){
				boolean mostrarCuenta = true;//false;
				
				
				if(mostrarCuenta){
					CuentaTitularVO cuentaTitularVO = (CuentaTitularVO) ct.toVO(0, false);
					cuentaTitularVO.setCuenta(ct.getCuenta().toVOWithRecurso());
					
					// Seteamos bandera para el boton emitir.
					if (ct.getCuenta().getRecurso().getPerEmiDeuPuntual() != null &&
							ct.getCuenta().getRecurso().getPerEmiDeuPuntual().intValue() == 1 && 
								ct.getCuenta().getRecurso().permiteCrearEmitir()){
						cuentaTitularVO.setRecursoPermiteEmision(true);
					}
					
					listCuentaTitularVO.add(cuentaTitularVO);
				}
			}
				
			for (CuentaTitularVO cuentaTitularVO: listCuentaTitularVO) {
				cuentaTitularVO.getCuenta().setEstadoDeudaBussEnabled(Boolean.FALSE);
				cuentaTitularVO.getCuenta().setLiquidacionDeudaBussEnabled(Boolean.TRUE);
			}
			
			if (contribuyente != null){
				log.debug("contribuyente.SuperaMaxCantCuentas(): " + contribuyente.getSuperaMaxCantCuentas());
				
				// Seteamos bandera para que muestre el paginado
				if (contribuyente.getSuperaMaxCantCuentas()){
					deudaContribSearchPage.setSuperaMaxCantCuentas(true);
					deudaContribSearchPage.clearMessage();
					deudaContribSearchPage.addMessageValue("El Contribuyente posee " + contribuyente.getCountCuentasContrib() + " cuentas.");
				}else{
					deudaContribSearchPage.setSuperaMaxCantCuentas(false);
				}
			}
			
			deudaContribSearchPage.setListResult(listCuentaTitularVO);
			
			// Obtenemos los servivios banco que posean recurso.
			List<ServicioBanco> listServicoBanco = ServicioBanco.getListPoseeRecurso(); 
			
			deudaContribSearchPage.setListServicioBanco((List<ServicioBancoVO>) ListUtilBean.toVO(listServicoBanco, 1, 
					new ServicioBancoVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return deudaContribSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	public PrintModel imprimirListDeudaContrib(UserContext userContext, DeudaContribSearchPage deudaContribSearchPage)throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			//Obtiene el printModel
			
			if(!ModelUtil.isNullOrEmpty(deudaContribSearchPage.getServicioBanco())){
				ServicioBanco servicioBanco = ServicioBanco.getById(deudaContribSearchPage.getServicioBanco().getId()); 
				deudaContribSearchPage.getServicioBanco().setDesServicioBanco(servicioBanco.getDesServicioBanco()); 
			} else {
				deudaContribSearchPage.getServicioBanco().setDesServicioBanco(StringUtil.SELECT_OPCION_TODOS);
			}
			
			Double totalGeneral = 0D;
			
			for(CuentaTitularVO cuentaTitularVO:(List<CuentaTitularVO>)deudaContribSearchPage.getListResult()){
				
				cuentaTitularVO.getListDeuda().clear();
				
				Cuenta cuenta = Cuenta.getById(cuentaTitularVO.getCuenta().getId());
				
				// Replicamos la logica de deuda sigue titular
				LiqCuentaVO liqCuentaFilter = new LiqCuentaVO(); 
				
				if (cuenta.getRecurso().getEsDeudaTitular().intValue() == 1){
					liqCuentaFilter.setDeudaSigueTitular(true);
					liqCuentaFilter.setIdCuentaTitular(cuentaTitularVO.getId());
				} else {
				// Si la deuda sigue a la cuenta	
					liqCuentaFilter.setDeudaSigueTitular(false);
				}
				
				cuenta.setLiqCuentaFilter(liqCuentaFilter);
				
				LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(cuenta); 
				
				List<LiqDeudaAdminVO> listLiqDeudaAdmin = liqDeudaBeanHelper.getDeudaAdmin();
				List<LiqDeudaProcuradorVO> listLiqDeudaProcurador =  liqDeudaBeanHelper.getDeudaProcurador();
				List<LiqDeudaCyQVO> listLiqDeudaCyQ =  liqDeudaBeanHelper.getDeudaCyQ();
				
				// Deuda Adimistrativa
				for(LiqDeudaAdminVO liqDeudaAdmin: listLiqDeudaAdmin){
					for(LiqDeudaVO liqDeudaVO:liqDeudaAdmin.getListDeuda()){
	                    cuentaTitularVO.setTotalViaAdmin(cuentaTitularVO.getTotalViaAdmin() + liqDeudaVO.getTotal());
						cuentaTitularVO.getListDeuda().add(liqDeudaVO);
					}
				}
				
				// Deuda Judical 
				for(LiqDeudaProcuradorVO liqDeudaProcurador:listLiqDeudaProcurador){
					for(LiqDeudaVO liqDeudaVO:liqDeudaProcurador.getListDeuda()){
						cuentaTitularVO.setTotalViaJudicial(cuentaTitularVO.getTotalViaJudicial() + liqDeudaVO.getTotal());
						cuentaTitularVO.getListDeuda().add(liqDeudaVO);
					}
				}
				
				// Deuda Cyq
				for(LiqDeudaCyQVO liqDeudaCyQ:listLiqDeudaCyQ){
					for(LiqDeudaVO liqDeudaVO:liqDeudaCyQ.getListDeuda()){
						cuentaTitularVO.setTotalViaCyQ(cuentaTitularVO.getTotalViaCyQ() + liqDeudaVO.getTotal());
						cuentaTitularVO.getListDeuda().add(liqDeudaVO);
					}
				}
				
                cuentaTitularVO.setTotal(cuentaTitularVO.getTotalViaAdmin() + cuentaTitularVO.getTotalViaJudicial() + cuentaTitularVO.getTotalViaCyQ());
			
                totalGeneral += cuentaTitularVO.getTotal();
			}
			
			deudaContribSearchPage.setTotalGeneral(totalGeneral);
			
			PrintModel print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_DEUDA_CONTRIBUYENTE);
			print.putCabecera("usuario", userContext.getUserName());
			print.setData(deudaContribSearchPage);
			print.setTopeProfundidad(4);
			print.setDeleteXMLFile(false);

			return print;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} 
	}
	
	// <--- Deuda por Contribuyente
	
	//	---> Liquidacion de la Deuda
	/**
	 * Inicializacion para el ingreso del usuario Gestion de Recurso
	 *
	 * @throws DemodaServiceException
	 */
	public LiqDeudaAdapter getLiqDeudaAdapterGRInit(UserContext userContext) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			LiqDeudaAdapter liqDeudaAdapter = initLiqDeudaAdapter(null);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return liqDeudaAdapter;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/**
	 * Inicializacion de la pantalla de ingreso a la liquidacion de deuda, para el contribuyente.
	 * 
	 * Recibe el idRecurso.
	 * 
	 */
	public LiqDeudaAdapter getLiqDeudaAdapterContrInit(UserContext userContext, CommonKey recursoKey) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			LiqDeudaAdapter liqDeudaAdapter = new LiqDeudaAdapter();
			
			// Aqui obtiene lista de BOs
			Recurso recurso = Recurso.getById(recursoKey.getId());
			RecursoVO recursoVO =  new RecursoVO();
			
			recursoVO = recurso.toVOWithCategoria();
			
			// Pasamos bandera de autoliquidable
			if (recurso.getEsAutoliquidable().intValue() == 1)
				liqDeudaAdapter.getCuenta().setEsRecursoAutoliquidable(true);
			else
				liqDeudaAdapter.getCuenta().setEsRecursoAutoliquidable(false);
			
			//Bandera para opción de cambio de domicilio
			boolean esComercio = false;
			if (recurso.getTipObjImp() != null) {
				esComercio = recurso.getTipObjImp().getId().equals(TipObjImp.COMERCIO);
			}
			if (recurso.getPerEmiDeuMas().intValue() == 1 && !esComercio) {
				liqDeudaAdapter.setVerCambioDomicilio(true);
			} else {
				liqDeudaAdapter.setVerCambioDomicilio(false);
			}
			
			// Seteo del recurso
			liqDeudaAdapter.getCuenta().setRecurso(recursoVO);
			liqDeudaAdapter.getCuenta().setIdRecurso(recursoVO.getId());
			
			
			
			// Verificamos en la lista de IdRecurso del parametro para no requerir el codigo de gestion personal (Solicitado en mantis 5105)
			String listIdRecSinCodGesPer = null;
			try{ listIdRecSinCodGesPer = SiatParam.getString(SiatParam.LISTA_ID_REC_SIN_COD_GES_PER); }catch (Exception e) {}
			String idRecursoStr = "|" + recursoVO.getId().toString() + "|";
			if (!StringUtil.isNullOrEmpty(listIdRecSinCodGesPer) && listIdRecSinCodGesPer.indexOf(idRecursoStr) >= 0){
				liqDeudaAdapter.setCodGesPerRequerido(false);
				log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> CODGESPER NO REQUERIDO  1 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			}else{
				liqDeudaAdapter.setCodGesPerRequerido(true);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return liqDeudaAdapter;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/**
	 * Inicializacion factorizada del Adapter de Deuda. 
	 * 
	 * - Se recargan los combos recurso, estado, perido, y clasificacion deuda
	 * - Se setea el id recurso. 
	 *  
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public LiqDeudaAdapter initLiqDeudaAdapter(LiqDeudaAdapter liqDeudaAdapter) throws DemodaServiceException {
		
		try{
			
			if (liqDeudaAdapter == null)
				liqDeudaAdapter = new LiqDeudaAdapter();
			
			// Aqui obtiene lista de BOs
			List<Recurso> listRecurso = Recurso.getListTributariosVigentes(new Date());
			
			// Seteo la lista de recursos
			liqDeudaAdapter.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				liqDeudaAdapter.getListRecurso().add(item.toVOWithCategoria());							
			}
			
			// Seteo del id para que no sea nulo
			if (liqDeudaAdapter.getCuenta().getIdRecurso() == null)
				liqDeudaAdapter.getCuenta().setIdRecurso(new Long(-1));
			
			// Seteo del id para que sea nulo
			Recurso recurso = Recurso.getByIdNull(liqDeudaAdapter.getCuenta().getIdRecurso());
			
			if (recurso != null){
				if (recurso.getEsAutoliquidable().intValue() == 1) {
					
					liqDeudaAdapter.getCuenta().setEsRecursoAutoliquidable(true);
					liqDeudaAdapter.setListEstadoPeriodo(EstadoPeriodo.getList(EstadoPeriodo.OpcionTodo));
					
					liqDeudaAdapter.setListRecClaDeu(ListUtilBean.toVO(recurso.getListRecClaDeu(), 0, new RecClaDeuVO(-1, StringUtil.SELECT_OPCION_TODAS)));
					
					// Se vino algo seleccionado, se mantiene, sino inicializamos
					if (liqDeudaAdapter.getCuenta().getEstadoPeriodo() == null)	
						liqDeudaAdapter.getCuenta().setEstadoPeriodo(EstadoPeriodo.OpcionTodo);
					
					if (ModelUtil.isNullOrEmpty(liqDeudaAdapter.getCuenta().getRecClaDeu()))
						liqDeudaAdapter.getCuenta().setRecClaDeu(new RecClaDeuVO(-1, StringUtil.SELECT_OPCION_TODAS));
					
				} else {
					liqDeudaAdapter.getCuenta().setEsRecursoAutoliquidable(false);
				}
				
			} else {
				liqDeudaAdapter.getCuenta().setEsRecursoAutoliquidable(false);
			}
						
			return 	liqDeudaAdapter;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e);
		}
	}
	
	/**
	 * Carga los datos de tipos de periodo y clasificaciones de deuda si el recurso seleccionado es autoliquidable.
	 *
	 */
	public LiqDeudaAdapter getLiqDeudaAdapterParamRecurso(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			liqDeudaAdapterVO.clearError();

			// Aqui obtiene lista de BOs
			List<Recurso> listRecurso = Recurso.getListTributariosVigentes(new Date());
			
			// Seteo la lista de recursos
			liqDeudaAdapterVO.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				liqDeudaAdapterVO.getListRecurso().add(item.toVOWithCategoria());							
			}
			
			// Seteo del id para que sea nulo
			Recurso recurso = Recurso.getByIdNull(liqDeudaAdapterVO.getCuenta().getIdRecurso());
			
			if (recurso != null){
				
				if (recurso.getEsAutoliquidable().intValue() == 1) {
					
					liqDeudaAdapterVO.getCuenta().setEsRecursoAutoliquidable(true);
					liqDeudaAdapterVO.setListEstadoPeriodo(EstadoPeriodo.getList(EstadoPeriodo.OpcionTodo));
					liqDeudaAdapterVO.getCuenta().setEstadoPeriodo(EstadoPeriodo.OpcionTodo);
					
					liqDeudaAdapterVO.setListRecClaDeu(ListUtilBean.toVO(recurso.getListRecClaDeu(), 
																		0, new RecClaDeuVO(-1, StringUtil.SELECT_OPCION_TODAS)));
					
					liqDeudaAdapterVO.getCuenta().setRecClaDeu(new RecClaDeuVO(-1, StringUtil.SELECT_OPCION_TODAS));
					
					liqDeudaAdapterVO.getCuenta().setFechaVtoDesdeView("");
					liqDeudaAdapterVO.getCuenta().setFechaVtoHastaView("");
					
				} else {
					liqDeudaAdapterVO.getCuenta().setEsRecursoAutoliquidable(false);
				}
			}
			
			return 	liqDeudaAdapterVO;			
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
		
	}
	
	
	/**
	 * Param utilizado en la navegacion de la liquidacion de deuda, cuando se intenta desde una cuenta para a otra.
	 * Permite saber si el recurso de la cuenta es autoliquidable para mostrar o no la pantalla de filtros. 
	 * 
	 * @throws DemodaServiceException
	 */
	public LiqDeudaAdapter getLiqDeudaAdapterParamCuenta(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException {
			String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			liqDeudaAdapterVO.clearError();

			Long idCuenta = liqDeudaAdapterVO.getCuenta().getIdCuenta();

			// recupero la cuenta
			Cuenta cuenta = Cuenta.getByIdNull(idCuenta);
			
			if (cuenta != null) {
				
				liqDeudaAdapterVO.getCuenta().setNumeroCuenta(cuenta.getNumeroCuenta());
				liqDeudaAdapterVO.getCuenta().setNroCuenta(cuenta.getNumeroCuenta());
				
				liqDeudaAdapterVO.getCuenta().setIdRecurso(cuenta.getRecurso().getId());
				liqDeudaAdapterVO.getCuenta().setDesRecurso(cuenta.getRecurso().getDesRecurso());
				
				if (cuenta.getRecurso().getEsAutoliquidable().intValue() == 1) {
					
					liqDeudaAdapterVO.getCuenta().setEsRecursoAutoliquidable(true);
					liqDeudaAdapterVO.setListEstadoPeriodo(EstadoPeriodo.getList(EstadoPeriodo.OpcionTodo));
					liqDeudaAdapterVO.getCuenta().setEstadoPeriodo(EstadoPeriodo.OpcionTodo);
					
					liqDeudaAdapterVO.setListRecClaDeu(ListUtilBean.toVO(cuenta.getRecurso().getListRecClaDeu(), 
																		0, new RecClaDeuVO(-1, StringUtil.SELECT_OPCION_TODAS)));
					
					liqDeudaAdapterVO.getCuenta().setRecClaDeu(new RecClaDeuVO(-1, StringUtil.SELECT_OPCION_TODAS));
					
					liqDeudaAdapterVO.getCuenta().setFechaVtoDesdeView("");
					liqDeudaAdapterVO.getCuenta().setFechaVtoHastaView("");
					
				} else {
					liqDeudaAdapterVO.getCuenta().setEsRecursoAutoliquidable(false);
				}
				
				// Si la deuda sigue al titular
				if (cuenta.getRecurso().getEsDeudaTitular().intValue() == 1){
					liqDeudaAdapterVO.getCuenta().setDeudaSigueTitular(true);
				} else {
				// Si la deuda sigue a la cuenta	
					liqDeudaAdapterVO.getCuenta().setDeudaSigueTitular(false);
				}
				
			} else {
				liqDeudaAdapterVO.addRecoverableValueError("La cuenta con id " + idCuenta + " es inexistente");				
			}
			
			log.debug(funcName + ": exit");
			return liqDeudaAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	
	/**	   
	 *  Ingreso utilizado por el usuario interno Gestion de Recurso
	 *  Busca la cuenta	por Recurso y Numero de Cuenta.
	 * 
	 * @return LiqDeudaAdapter
	 * @throws DemodaServiceException
	 */
	public LiqDeudaAdapter getLiqDeudaAdapterByRecursoNroCuenta(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			
			DemodaTimer dt = new DemodaTimer();
			
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			liqDeudaAdapterVO.clearError();

			String numeroCuenta = liqDeudaAdapterVO.getCuenta().getNumeroCuenta();
			Long idRecurso = liqDeudaAdapterVO.getCuenta().getIdRecurso();
			
			if (StringUtil.isNullOrEmpty(numeroCuenta)){
				liqDeudaAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_LABEL );
			}
			
			if (idRecurso == null || idRecurso.equals(-1L) ){
				liqDeudaAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL );				
			}
			
			if(!StringUtil.isNullOrEmpty(liqDeudaAdapterVO.getCuenta().getFechaVtoDesdeView()) && 
		    		!DateUtil.isValidDate(liqDeudaAdapterVO.getCuenta().getFechaVtoDesdeView(), DateUtil.ddSMMSYYYY_MASK)){
		    	
		    	liqDeudaAdapterVO.addRecoverableValueError("El formato del campo Fecha Vencimiento Desde es Inv\u00E1lido");
		    }
			
		    if(!StringUtil.isNullOrEmpty(liqDeudaAdapterVO.getCuenta().getFechaVtoHastaView()) && 
		    		!DateUtil.isValidDate(liqDeudaAdapterVO.getCuenta().getFechaVtoHastaView(), DateUtil.ddSMMSYYYY_MASK)){
		    	
		    	liqDeudaAdapterVO.addRecoverableValueError("El formato del campo Fecha Vencimiento Hasta es Inv\u00E1lido");
		    }
			
			if (liqDeudaAdapterVO.hasError()){
				return initLiqDeudaAdapter(liqDeudaAdapterVO);
			}
			
			// 1:: Se recupera la cuenta
			log.debug(funcName + " 1:: getByIdRecursoYNumeroCuenta()");
			
			Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(idRecurso, numeroCuenta, Estado.ACTIVO);
			
			if (cuenta == null){
				liqDeudaAdapterVO.addRecoverableValueError("La cuenta numero " + numeroCuenta + " es inexistente");				
			}
			
			if (liqDeudaAdapterVO.hasError()){
				return initLiqDeudaAdapter(liqDeudaAdapterVO);
			}
			
			log.info(dt.stop(" LiqDeuda : getByIdRecursoYNumeroCuenta()"));
			
			// Llamada al helper que devuele el estado completo de la deuda
			LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(cuenta);
			
			liqDeudaAdapterVO = liqDeudaBeanHelper.getLiqDeudaAdapter();
			
			
			log.debug(funcName + ": exit");
			return liqDeudaAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			LiqDeudaAdapter liqDeudaAdapterError = initLiqDeudaAdapter(liqDeudaAdapterVO);
			liqDeudaAdapterError.addRecoverableValueError("No se pudo recuperar la cuenta");
			return liqDeudaAdapterError;
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	/**
	 * Dado un recurso y numero de cuenta, devuelve un LiqDeudaAdapter con el "IdCuenta" seteado en caso de haberla encontrado.
	 * 
	 */
	public LiqDeudaAdapter getIdCuentaByRecursoNroCuenta(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Long idCuenta = 0L;
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			liqDeudaAdapterVO.clearError();

			String numeroCuenta = liqDeudaAdapterVO.getCuenta().getNumeroCuenta();
			Long idRecurso = liqDeudaAdapterVO.getCuenta().getIdRecurso();
			
			// Requeridos
			if (StringUtil.isNullOrEmpty(numeroCuenta)){
				liqDeudaAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_LABEL );
			}
			
			if (idRecurso == null || idRecurso.equals(-1L) ){
				liqDeudaAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL );				
			}
			
			// Formato
			/*
			if (!StringUtil.isNullOrEmpty(numeroCuenta) && !StringUtil.isNumeric(numeroCuenta)){
				liqDeudaAdapterVO.addRecoverableValueError("La cuenta ingresada es inv\u00E1lida. S\u00F3lo debe contener n\u00FAmeros; no debe contener guiones ni espacios");
			}
			*/
			
			if(!StringUtil.isNullOrEmpty(liqDeudaAdapterVO.getCuenta().getFechaVtoDesdeView()) && 
		    		!DateUtil.isValidDate(liqDeudaAdapterVO.getCuenta().getFechaVtoDesdeView(), DateUtil.ddSMMSYYYY_MASK)){
		    	
		    	liqDeudaAdapterVO.addRecoverableValueError("El formato del campo Fecha Vencimiento Desde es Inv\u00E1lido");
		    }
			
		    if(!StringUtil.isNullOrEmpty(liqDeudaAdapterVO.getCuenta().getFechaVtoHastaView()) && 
		    		!DateUtil.isValidDate(liqDeudaAdapterVO.getCuenta().getFechaVtoHastaView(), DateUtil.ddSMMSYYYY_MASK)){
		    	
		    	liqDeudaAdapterVO.addRecoverableValueError("El formato del campo Fecha Vencimiento Hasta es Inv\u00E1lido");
		    }
			
			if (liqDeudaAdapterVO.hasError()){
				return initLiqDeudaAdapter(liqDeudaAdapterVO);
			}
			
			// 1:: Se recupera la cuenta
			log.debug(funcName + " 1:: getByIdRecursoYNumeroCuenta()");
			
			// Obtenemos la cuenta 
			Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(idRecurso, numeroCuenta);
			
			// Si no se encontro la cuenta, o el estado es Inactivo, o el estado es Creada
			if (cuenta == null || cuenta.getEstado() == Estado.INACTIVO.getId() 
							   || cuenta.getEstado() == Estado.CREADO.getId()) {
				
				Recurso recurso = Recurso.getByIdNull(idRecurso);
				String dscRecurso = ""; 
				
				if (recurso != null){
					dscRecurso = " para el recurso " + recurso.getDesRecurso();
				}
				
				liqDeudaAdapterVO.addRecoverableValueError("La cuenta numero " + numeroCuenta + " es inexistente" + dscRecurso);				
			}
			
			if (liqDeudaAdapterVO.hasError()){
				return initLiqDeudaAdapter(liqDeudaAdapterVO);			
			}
			
			idCuenta = cuenta.getId();
			
			liqDeudaAdapterVO.getCuenta().setIdCuenta(idCuenta);
			
			log.debug("Filtro: "+liqDeudaAdapterVO.getCuenta().getEstadoPeriodo().getValue());
			
			
			return liqDeudaAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			LiqDeudaAdapter liqDeudaAdapterError = initLiqDeudaAdapter(liqDeudaAdapterVO);
			liqDeudaAdapterError.addRecoverableValueError("No se pudo recuperar la cuenta");
			return liqDeudaAdapterError;
		} 
	}
	
	
	
	public LiqDeudaAdapter getIdCuentaByNroCuentaCodGesPer(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			liqDeudaAdapterVO.clearError();

			String numeroCuenta = liqDeudaAdapterVO.getCuenta().getNumeroCuenta();
			String codGesPer = liqDeudaAdapterVO.getCuenta().getCodGesPer();
			Long idRecurso = liqDeudaAdapterVO.getCuenta().getIdRecurso();
			
			// Requeridos
			if (StringUtil.isNullOrEmpty(numeroCuenta)){
				liqDeudaAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_LABEL );
			}
			if(liqDeudaAdapterVO.getCodGesPerRequerido()){
				if (StringUtil.isNullOrEmpty(codGesPer)){
					liqDeudaAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, "& Codigo Gestion Personal");
				}				
			}
			
			if (idRecurso == null || idRecurso.equals(-1L) ){
				liqDeudaAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL );				
			}
			
			//Bandera para opción de cambio de domicilio
			boolean esComercio = false;
			Recurso recurso = Recurso.getById(idRecurso);
			if (recurso.getTipObjImp() != null) {
				esComercio = recurso.getTipObjImp().getId().equals(TipObjImp.COMERCIO);
			}
			if (recurso.getPerEmiDeuMas().intValue() == 1 && !esComercio) {
				liqDeudaAdapterVO.setVerCambioDomicilio(true);
			} else {
				liqDeudaAdapterVO.setVerCambioDomicilio(false);
			}

			// Formatos	
			/*
			if (!StringUtil.isNullOrEmpty(numeroCuenta) && !StringUtil.isNumeric(numeroCuenta)){
				liqDeudaAdapterVO.addRecoverableValueError("La cuenta ingresada es inv\u00E1lida. S\u00F3lo debe contener n\u00FAmeros; no debe contener guiones ni espacios");
			}
			*/
			if(liqDeudaAdapterVO.getCodGesPerRequerido()){
				if (!StringUtil.isNullOrEmpty(codGesPer) && !StringUtil.isNumeric(codGesPer)){
					liqDeudaAdapterVO.addRecoverableValueError("El c\u00F3digo de gesti\u00F3n ingresado es inv\u00E1lido. S\u00F3lo debe contener n\u00FAmeros; no debe contener guiones ni espacios");
				}				
			}
			if (liqDeudaAdapterVO.hasError()){
				//Recurso recurso = Recurso.getById(idRecurso);
				
				RecursoVO recursoVO =  new RecursoVO();
				recursoVO = recurso.toVOWithCategoria();
								
				// Seteo del recurso
				liqDeudaAdapterVO.getCuenta().setRecurso(recursoVO);
				liqDeudaAdapterVO.getCuenta().setIdRecurso(recursoVO.getId());
				
				return liqDeudaAdapterVO;			
			}
			
			// 1:: Se recupera la cuenta
			log.debug(funcName + " 1:: getByIdRecursoYNumeroCuenta()");

			// Obtenemos la cuenta			
			Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(idRecurso, numeroCuenta);
			
			if(liqDeudaAdapterVO.getCodGesPerRequerido()){
				codGesPer = StringUtil.quitarCerosIzq(codGesPer);
				if (cuenta != null && !cuenta.getCodGesCue().equals(codGesPer)){
					liqDeudaAdapterVO.addRecoverableValueError("El c\u00F3digo de gesti\u00F3n ingresado no pertenece a la cuenta.");				
				}
			}
			
			// Si no se encontro la cuenta, o el estado es Inactivo, o el estado es Creada
			if (cuenta == null || cuenta.getEstado() == Estado.INACTIVO.getId() 
							   || cuenta.getEstado() == Estado.CREADO.getId()) {
				liqDeudaAdapterVO.addRecoverableValueError("La cuenta ingresada es inexistente");				
			}
			
			// Si no encuentra la cuenta por alguna razon, se simula un init.
			if (liqDeudaAdapterVO.hasError()){
				//Recurso recurso = Recurso.getById(idRecurso);
				
				RecursoVO recursoVO =  new RecursoVO();
				recursoVO = recurso.toVOWithCategoria();
								
				// Seteo del recurso
				liqDeudaAdapterVO.getCuenta().setRecurso(recursoVO);
				liqDeudaAdapterVO.getCuenta().setIdRecurso(recursoVO.getId());
				
				return liqDeudaAdapterVO;			
			}
			
			liqDeudaAdapterVO.getCuenta().setIdCuenta(cuenta.getId());
			liqDeudaAdapterVO.getCuenta().setNroCuenta(cuenta.getNumeroCuenta());
			liqDeudaAdapterVO.getCuenta().setNumeroCuenta(cuenta.getNumeroCuenta());
			liqDeudaAdapterVO.getCuenta().setCodRecurso(cuenta.getRecurso().getCodRecurso());
			
			if (cuenta.getRecurso().getEsAutoliquidable().intValue() == 1)
				liqDeudaAdapterVO.getCuenta().setEsRecursoAutoliquidable(true);
			else
				liqDeudaAdapterVO.getCuenta().setEsRecursoAutoliquidable(false);
			
			log.debug(funcName + ": exit");
			return liqDeudaAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			LiqDeudaAdapter liqDeudaAdapterError = initLiqDeudaAdapter(liqDeudaAdapterVO);
			liqDeudaAdapterError.addRecoverableValueError("No se pudo recuperar la cuenta");
			return liqDeudaAdapterError;
		} finally {
			SiatHibernateUtil.closeSession();
		}	
		
	}
	
	
	/**
	 * 
	 * Ingreso utilizado por el usuario web.
	 * Busca la cuenta por Recurso y Numero de Cuenta y Codigo de Gestion Personal.
	 *
	 * @return LiqDeudaAdapter
	 * @throws DemodaServiceException 
	 */
	@Deprecated // Se pasa a utilizar "getIdCuentaByNroCuentaCodGesPer"
	public LiqDeudaAdapter getLiqDeudaAdapterByNroCuentaCodGesPer(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			liqDeudaAdapterVO.clearError();

			String numeroCuenta = liqDeudaAdapterVO.getCuenta().getNumeroCuenta();
			String codGesPer = liqDeudaAdapterVO.getCuenta().getCodGesPer();
			Long idRecurso = liqDeudaAdapterVO.getCuenta().getIdRecurso();
			
			// Requeridos
			if (StringUtil.isNullOrEmpty(numeroCuenta)){
				liqDeudaAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_LABEL );
			}

			if (StringUtil.isNullOrEmpty(codGesPer)){
				liqDeudaAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, "& Codigo Gestion Personal");
			}
			
			if (idRecurso == null || idRecurso.equals(-1L) ){
				liqDeudaAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL );				
			}

			// Formatos	
			/*
			if (!StringUtil.isNullOrEmpty(numeroCuenta) && !StringUtil.isNumeric(numeroCuenta)){
				liqDeudaAdapterVO.addRecoverableValueError("La cuenta ingresada es inv\u00E1lida. S\u00F3lo debe contener n\u00FAmeros; no debe contener guiones ni espacios");
			}
			*/
			
			if (!StringUtil.isNullOrEmpty(codGesPer) && !StringUtil.isNumeric(codGesPer)){
				liqDeudaAdapterVO.addRecoverableValueError("El c\u00F3digo de gesti\u00F3n ingresado es inv\u00E1lido. S\u00F3lo debe contener n\u00FAmeros; no debe contener guiones ni espacios");
			}
			if (liqDeudaAdapterVO.hasError()){
				Recurso recurso = Recurso.getById(idRecurso);
				
				RecursoVO recursoVO =  new RecursoVO();
				recursoVO = recurso.toVOWithCategoria();
								
				// Seteo del recurso
				liqDeudaAdapterVO.getCuenta().setRecurso(recursoVO);
				liqDeudaAdapterVO.getCuenta().setIdRecurso(recursoVO.getId());
				
				return liqDeudaAdapterVO;			
			}
			
			// 1:: Se recupera la cuenta
			log.debug(funcName + " 1:: getByIdRecursoYNumeroCuenta()");

			// Obtenemos la cuenta			
			Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(idRecurso, numeroCuenta);
			
			codGesPer = StringUtil.quitarCerosIzq(codGesPer);
			if (cuenta != null && !cuenta.getCodGesCue().equals(codGesPer)){
				liqDeudaAdapterVO.addRecoverableValueError("El c\u00F3digo de gesti\u00F3n ingresado no pertenece a la cuenta.");				
			}
			
			// Si no se encontro la cuenta, o el estado es Inactivo, o el estado es Creada
			if (cuenta == null || cuenta.getEstado() == Estado.INACTIVO.getId() 
							   || cuenta.getEstado() == Estado.CREADO.getId()) {
				liqDeudaAdapterVO.addRecoverableValueError("La cuenta ingresada es inexistente");				
			}
			
			// Si no encuentra la cuenta por alguna razon, se simula un init.
			if (liqDeudaAdapterVO.hasError()){
				Recurso recurso = Recurso.getById(idRecurso);
				
				RecursoVO recursoVO =  new RecursoVO();
				recursoVO = recurso.toVOWithCategoria();
								
				// Seteo del recurso
				liqDeudaAdapterVO.getCuenta().setRecurso(recursoVO);
				liqDeudaAdapterVO.getCuenta().setIdRecurso(recursoVO.getId());
				
				return liqDeudaAdapterVO;			
			}
			
// Llamada al helper que devuele el estado completo de la deuda
//LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(cuenta);
//liqDeudaAdapterVO = liqDeudaBeanHelper.getLiqDeudaAdapter();
			
			log.debug(funcName + ": exit");
			return liqDeudaAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			LiqDeudaAdapter liqDeudaAdapterError = initLiqDeudaAdapter(liqDeudaAdapterVO);
			liqDeudaAdapterError.addRecoverableValueError("No se pudo recuperar la cuenta");
			return liqDeudaAdapterError;
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	/**
	 * Factorizacion de los puntos de entrada para obtener un LiqDeudaAdapter con los datos de la cuenta y deuda por id de cuenta.
	 * Este metodo es llamado por los demas metodos y tambien es utilizado para ir a la liquidacion de deuda desde 
	 * otros puntos del sistema, por ejemplo desde ver detalles periodo, ver convenio, etc.  
	 *  
	 * Recupera una cuenta por Id y Obtiene todo los datos nesesarios para mostrar en el LiqDeudaAdapter 
	 * 
	 * @return LiqDeudaAdapter
	 * @throws DemodaServiceException
	 */	
	public LiqDeudaAdapter getLiqDeudaAdapterByIdCuenta(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			liqDeudaAdapterVO.clearError();

			Long idCuenta = liqDeudaAdapterVO.getCuenta().getIdCuenta();
			
			LiqCuentaVO liqCuentaFilter = new LiqCuentaVO();
			// Contenemos los filtros en un LiqCuentaVO Auxiliar, 
			// porque los metodos de LiqDeudaBeanHelper instancian liqDeudaAdapter
			LiqDeudaBeanHelper.passFilters(liqDeudaAdapterVO.getCuenta(), liqCuentaFilter);
			
			if (idCuenta == null){
				liqDeudaAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL );				
			}
			
			if (liqDeudaAdapterVO.hasError()){
				return initLiqDeudaAdapter(liqDeudaAdapterVO);
			}
			
			// 1:: Se recupera la cuenta
			log.debug(funcName + " 1:: getByIdRecursoYNumeroCuenta()");
			// No deberia ocurrir nunca que llege un id inexistente, pero por concurrencia podria. 
			Cuenta cuenta = Cuenta.getByIdNull(idCuenta); 
			
			if (cuenta == null){
				liqDeudaAdapterVO.addRecoverableValueError("La cuenta con id " + idCuenta + " es inexistente");
			}
			
			if (liqDeudaAdapterVO.hasError()){
				return initLiqDeudaAdapter(liqDeudaAdapterVO);			
			}
			
			// Seteamos el objeto LiqCuentaVO con los filtros que pueden haberse cargado
			// Este objeto es consultado en los DAO's de deuda. 
			cuenta.setLiqCuentaFilter(liqCuentaFilter);
			
			// Llamada al helper que devuele el estado completo de la deuda
			LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(cuenta);
			
			// Es parametro inica si se recuperan o no los titulares.
			liqDeudaAdapterVO = liqDeudaBeanHelper.getLiqDeudaAdapter(true);
			
			liqDeudaAdapterVO.getCuenta().setIdCuenta(idCuenta);
			
			// Pasamos los filtros de liqCuentaFilter al LiqCuentaVO
			LiqDeudaBeanHelper.passFilters(liqCuentaFilter, liqDeudaAdapterVO.getCuenta());
			if(cuenta.getRecurso().getEsAutoliquidable()!=null && cuenta.getRecurso().getEsAutoliquidable().intValue()==SiNo.SI.getId().intValue())
				liqDeudaAdapterVO.getCuenta().setEsRecursoAutoliquidable(true);
			
			log.debug(funcName + ": exit");
			return liqDeudaAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			LiqDeudaAdapter liqDeudaAdapterError = initLiqDeudaAdapter(liqDeudaAdapterVO);
			liqDeudaAdapterError.addRecoverableValueError("No se pudo recuperar la cuenta");
			return liqDeudaAdapterError;
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	//**************************************************************************************************************
	
	//				Los de arriba son los metodos para ingreso a liquidacion
	
	//**************************************************************************************************************
	
	/**
	 *  Obtiene un registro de deuda para ser mostrado en detalle con sus Conceptos y Pagos
	 * 
	 */
	public LiqDetalleDeudaAdapter getLiqDetalleDeudaAdapter(UserContext userContext, LiqDetalleDeudaAdapter liqDetalleDeudaAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			liqDetalleDeudaAdapterVO.clearError();

			Long idDeuda = liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().getIdDeuda();
			
			if (idDeuda == null){
				liqDetalleDeudaAdapterVO.addRecoverableValueError("El Id de Deuda no puede ser nulo: ");				
			}
			
			if (liqDetalleDeudaAdapterVO.hasError()){
				return liqDetalleDeudaAdapterVO;			
			}
			
			// 1:: Se recupera la cuenta
			log.debug(funcName + " 1:: getByIdRecursoYNumeroCuenta()");
			Deuda deuda = Deuda.getById(idDeuda);
						
			if (deuda == null){
				liqDetalleDeudaAdapterVO.addRecoverableValueError("Error al recuperar el registro de deuda: " + idDeuda );				
			}
			
			if (liqDetalleDeudaAdapterVO.hasError()){
				return liqDetalleDeudaAdapterVO;			
			}
			
			Cuenta cuenta = deuda.getCuenta();
			
			liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().setIdCuenta(cuenta.getId());
			
			liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().setPeriodoDeuda(deuda.getStrPeriodo());
			
			liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().setFechaEmision(DateUtil.formatDate(deuda.getFechaEmision(), DateUtil.ddSMMSYYYY_MASK));
			
			liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().setDesRecurso(cuenta.getRecurso().getDesRecurso());
			
			liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().setNroCuenta(cuenta.getNumeroCuenta());
			
			liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().setDesViaDeuda(deuda.getViaDeuda().getDesViaDeuda());
			
			liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().setDesClasificacionDeuda(deuda.getRecClaDeu().getDesClaDeu());
			
			liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().setDesServicioBanco(deuda.getSistema().getServicioBanco().getDesServicioBanco());
			
			liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().setDesEstadoDeuda(deuda.getEstadoDeuda().getDesEstadoDeuda());
			
			liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().setImporte(deuda.getImporte());
			
			liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().setSaldo(deuda.getSaldo());
			
			liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().setActualizacion(deuda.getActualizacion());
			
			liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().setFechaVencimiento(DateUtil.formatDate(deuda.getFechaVencimiento(), DateUtil.ddSMMSYYYY_MASK));
			
			liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().setFechaPago(deuda.getFechaPago());
			
			// Atributos del calculo al momento de la emision
			 AtrEmisionDefinition atrEmisionDefinition = deuda.getAtributosEmisionDefValue();
			 if (atrEmisionDefinition != null) {
				LiqAtrEmisionVO liqAtrEmisionVO = new LiqAtrEmisionVO(atrEmisionDefinition);
				liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().setLiqAtrEmisionVO(liqAtrEmisionVO);

				// Si tiene algo para mostrar, lo mostramos
				if (liqAtrEmisionVO != null && (!ListUtil.isNullOrEmpty(liqAtrEmisionVO.getListAtributos()) || 
						!ListUtil.isNullOrEmpty(liqAtrEmisionVO.getTablaAtributos()))) {
					liqDetalleDeudaAdapterVO.setMostrarAtributosEmision(true);
				}
			 }

			// Setear los conceptos
			List<? extends DeuRecCon> listConceptos = deuda.getListDeuRecCon();
			Double porcPago = deuda.getSaldo()/deuda.getImporte();
			for (DeuRecCon concepto:listConceptos){
				LiqConceptoDeudaVO liqConcepto = new LiqConceptoDeudaVO();
				
				liqConcepto.setDesConcepto(concepto.getRecCon().getDesRecCon());
				liqConcepto.setImporte(concepto.getImporte());
				liqConcepto.setSaldo(NumberUtil.round(concepto.getImporte()*porcPago,SiatParam.DEC_IMPORTE_VIEW));
				liqConcepto.setIdRecConView(concepto.getRecCon().getId().toString());
				
				liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().getListConceptos().add(liqConcepto);
			}
			
			
			if(cuenta.getRecurso().getEsAutoliquidable().intValue() == SiNo.SI.getId().intValue()){
				// Deshabilitar/Habilitar la modificacion de deuda para recursos autoliquidables
				if(ViaDeuda.ID_VIA_ADMIN == deuda.getViaDeuda().getId().longValue() && 	deuda.getConvenio() == null){
					liqDetalleDeudaAdapterVO.setModificarDeudaBussEnabled(true);
				}else{
					liqDetalleDeudaAdapterVO.setModificarDeudaBussEnabled(false);
				}				
			}
			
			// Cargar datos de procurador si la deuda tiene uno asignado
			if(deuda.getProcurador() != null){
				liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().setPoseeProcurador(true);
				liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().setDesProcurador(deuda.getProcurador().getDescripcion());
				liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().setDomProcurador(deuda.getProcurador().getDomicilio());
				liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().setTelProcurador(deuda.getProcurador().getTelefono());
				liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().setHorAteProcurador(deuda.getProcurador().getHorarioAtencion());
			}

			// Cargar datos de la anulacion si la deuda esta anulada
			if(deuda.getEstadoDeuda().getId().longValue() == EstadoDeuda.ID_ANULADA
					|| deuda.getEstadoDeuda().getId().longValue() == EstadoDeuda.ID_CONDONADA
					|| deuda.getEstadoDeuda().getId().longValue() == EstadoDeuda.ID_PRESCRIPTA){
				Anulacion anulacion = Anulacion.getByIdDeuda(deuda.getId());
				if(anulacion != null){
					liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().setDeudaAnulada(true);	
					liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().setFechaAnulacionView(DateUtil.formatDate(anulacion.getFechaAnulacion(), DateUtil.ddSMMSYYYY_MASK));
					liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().setDesMotAnuDeu(anulacion.getMotAnuDeu().getDesMotAnuDeu());
					liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().setUsuarioAnuDeu(deuda.getUsuario());
				}
			}
			
			// Se carga usuario que emitio la deuda (si la misma esta asociada a un proceso de emision) (Solicitado en Mantis 5467)
			if(deuda.getEmision() != null){
				liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().setUsuarioEmision(deuda.getEmision().getUsuario());
			}else{
				liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().setUsuarioEmision("Desconocido");
			}
			
			log.debug(funcName + ": exit");
			return liqDetalleDeudaAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}		
	}
	
	/**
	 *  Inicializa el Adapter de reclamo, con un id de Deuda como parametro.
	 * 
	 */
	public LiqReclamoAdapter getLiqReclamoAdapterInit(UserContext userContext, LiqReclamoAdapter liqReclamoAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			if(liqReclamoAdapterVO == null){
				throw new DemodaServiceException();
			}
			
			liqReclamoAdapterVO.clearError();

			String strSeletedId =  liqReclamoAdapterVO.getSelectedId();
			String[] arrSeletedId = strSeletedId.split("-");
			
			Long idDeuda = new Long(arrSeletedId[0]);
			Long idEstadoDeuda = new Long(arrSeletedId[1]);
			
			liqReclamoAdapterVO.getLiqReclamo().setIdDeuda(idDeuda);
			liqReclamoAdapterVO.getLiqReclamo().setIdEstadoDeuda(idEstadoDeuda);
			
			log.debug(funcName + " idDeuda: " + idDeuda + " idEstadoDeuda: " + idEstadoDeuda);
			
			Deuda deuda = null;
			
			if (idEstadoDeuda == EstadoDeuda.ID_ADMINISTRATIVA ){
				deuda = DeudaAdmin.getById(idDeuda);
			} else if (idEstadoDeuda == EstadoDeuda.ID_JUDICIAL){
				deuda = DeudaJudicial.getById(idDeuda);
			}
			
			if (deuda == null){
				throw new DemodaServiceException();				
			}
			
			// Fecha de ultimo asentamiento
			try {
				liqReclamoAdapterVO.setFechaUltimoAsentamiento(deuda.getCuenta().getRecurso().getFecUltPag());
			} catch (Exception e){
				e.printStackTrace();
			}
			
			// Utilizadas para la navegacion.
			liqReclamoAdapterVO.getLiqReclamo().setIdDeuda(idDeuda);
			liqReclamoAdapterVO.getLiqReclamo().setIdCuenta(deuda.getCuenta().getId());
			
			// Propiedades utilizadas por VariosWebFacade para guardar el recalmo
			liqReclamoAdapterVO.getLiqReclamo().setIdObjeto(deuda.getRecurso().getCodRecurso() + "-" + deuda.getCuenta().getNumeroCuenta());
			liqReclamoAdapterVO.getLiqReclamo().setCodRecurso(deuda.getRecurso().getCodRecurso());
			liqReclamoAdapterVO.getLiqReclamo().setPeriodo(new Integer(deuda.getPeriodo().toString()));
			liqReclamoAdapterVO.getLiqReclamo().setAnio(new Integer(deuda.getAnio().toString()));
			liqReclamoAdapterVO.getLiqReclamo().setSistema(new Integer(deuda.getSistema().getNroSistema().toString()));
			liqReclamoAdapterVO.getLiqReclamo().setResto(new Integer(deuda.getResto().toString()));
			
			// Propiedades para mostrar en la pantalla de reclamo.
			liqReclamoAdapterVO.getLiqReclamo().setDesRecurso(deuda.getRecurso().getDesRecurso());
			liqReclamoAdapterVO.getLiqReclamo().setNumeroCuenta(deuda.getCuenta().getNumeroCuenta());
			//liqReclamoAdapterVO.getLiqReclamo().setPeriodoAnio(deuda.getPeriodo() + "/" + deuda.getAnio()); modificamos por CdM
			liqReclamoAdapterVO.getLiqReclamo().setPeriodoAnio(deuda.getStrPeriodo());
			liqReclamoAdapterVO.getLiqReclamo().setFechaVto(DateUtil.formatDate(deuda.getFechaVencimiento(), DateUtil.ddSMMSYYYY_MASK));
			liqReclamoAdapterVO.getLiqReclamo().setImporte(deuda.getImporte());
			
			// Seteo la lista de tipos de documentos					  
			liqReclamoAdapterVO.setListTipoDocumento((ArrayList<TipoDocumentoVO>) ListUtilBean.toVO(TipoDocumento.getList()));
			
			
			LiqAtrValorVO lugarPago = new LiqAtrValorVO("-- Seleccionar --", "0"); 
			
			liqReclamoAdapterVO.getListLugaresPago().add(lugarPago);
			
			// Aqui obtiene lista de BOs
			List<Banco> listBanco = Banco.getListActivos();
			
			
			//Aqui pasamos BO a VO
	   		for (Banco banco:listBanco){
	   			
	   			lugarPago = new LiqAtrValorVO();
	   			lugarPago.setValue(banco.getCodBanco());
	   			lugarPago.setLabel(banco.getDesBanco());
	   					
	   			liqReclamoAdapterVO.getListLugaresPago().add(lugarPago);
	   		}
			
						
			log.debug(funcName + ": exit");
			return liqReclamoAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	/**
	 *  Inicializa el Adapter de reclamo, con un id de cuota como parametro.
	 * 
	 */
	public LiqReclamoAdapter getLiqReclamoCuotaAdapterInit(UserContext userContext, LiqReclamoAdapter liqReclamoAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			if(liqReclamoAdapterVO == null){
				throw new DemodaServiceException();
			}
			
			liqReclamoAdapterVO.clearError();

			String strSeletedId =  liqReclamoAdapterVO.getSelectedId();
			
			Long idCuota = new Long(strSeletedId);
			
			ConvenioCuota convenioCuota = ConvenioCuota.getById(idCuota);
			
			if (convenioCuota == null){
				throw new DemodaServiceException();				
			}
			
			Cuenta cuenta = convenioCuota.getConvenio().getCuenta();
			Plan plan = convenioCuota.getConvenio().getPlan();
		
			// Fecha de ultimo asentamiento
			try {
				liqReclamoAdapterVO.setFechaUltimoAsentamiento(cuenta.getRecurso().getFecUltPag());
			} catch (Exception e){
				e.printStackTrace();
			}
			
			// Utilizadas para la navegacion.
			liqReclamoAdapterVO.getLiqReclamo().setIdCuota(idCuota);
			liqReclamoAdapterVO.getLiqReclamo().setIdCuenta(cuenta.getId());
			liqReclamoAdapterVO.getLiqReclamo().setIdConvenio(convenioCuota.getConvenio().getId());
			
			// Propiedades utilizadas por VariosWebFacade para guardar el recalmo
			liqReclamoAdapterVO.getLiqReclamo().setIdObjeto(cuenta.getRecurso().getCodRecurso() + "-" + convenioCuota.getConvenio().getNroConvenio());
			liqReclamoAdapterVO.getLiqReclamo().setCodRecurso(cuenta.getRecurso().getCodRecurso());
			liqReclamoAdapterVO.getLiqReclamo().setSistema(new Integer(convenioCuota.getSistema().getNroSistema().toString()));
			liqReclamoAdapterVO.getLiqReclamo().setPeriodo(convenioCuota.getNumeroCuota());
			liqReclamoAdapterVO.getLiqReclamo().setAnio(0);			
			liqReclamoAdapterVO.getLiqReclamo().setResto(0);
			
			// Propiedades para mostrar en la pantalla de reclamo.
			liqReclamoAdapterVO.getLiqReclamo().setDesRecurso(cuenta.getRecurso().getDesRecurso());
			liqReclamoAdapterVO.getLiqReclamo().setNumeroCuenta(cuenta.getNumeroCuenta());
			liqReclamoAdapterVO.getLiqReclamo().setDesPlan(plan.getDesPlan());
			
			liqReclamoAdapterVO.getLiqReclamo().setNumeroCuota("" + convenioCuota.getNumeroCuota());
			liqReclamoAdapterVO.getLiqReclamo().setFechaVto(DateUtil.formatDate(convenioCuota.getFechaVencimiento(), DateUtil.ddSMMSYYYY_MASK));
			liqReclamoAdapterVO.getLiqReclamo().setImporte(convenioCuota.getImporteCuota());
			
			// Seteo la lista de tipos de documentos					  
			liqReclamoAdapterVO.setListTipoDocumento((ArrayList<TipoDocumentoVO>) ListUtilBean.toVO(TipoDocumento.getList()));
			
            LiqAtrValorVO lugarPago = new LiqAtrValorVO("-- Seleccionar --", "0"); 
			
			liqReclamoAdapterVO.getListLugaresPago().add(lugarPago);
			
			// Aqui obtiene lista de BOs
			List<Banco> listBanco = Banco.getListActivos();
			
			
			//Aqui pasamos BO a VO
	   		for (Banco banco:listBanco){
	   			
	   			lugarPago = new LiqAtrValorVO();
	   			lugarPago.setValue(banco.getCodBanco());
	   			lugarPago.setLabel(banco.getDesBanco());
	   					
	   			liqReclamoAdapterVO.getListLugaresPago().add(lugarPago);
	   		}
			
	   		
			log.debug(funcName + ": exit");
			return liqReclamoAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	/**
	 *  Creacion del reclamo web sobre un registro de deuda a travez del Facade.  
	 * 
	 */
	public LiqReclamoAdapter createReclamoDeuda(UserContext userContext, LiqReclamoAdapter liqReclamoAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			liqReclamoAdapterVO.clearErrorMessages();

			if(liqReclamoAdapterVO == null){
				throw new DemodaServiceException("Parametro LiqReclamoAdapterVO no puede ser null");
			}
			
			liqReclamoAdapterVO.clearError();
			LiqReclamoVO reclamoVO = liqReclamoAdapterVO.getLiqReclamo();
			reclamoVO.setTipoTramite(VariosWebFacade.COD_RECLAMO_ASENTAMIENTO_DEUDA);
			
			// Realiza validacion y luego lo creamos en la db si corresponde.
			Reclamo reclamo = BalReclamoManager.getInstance().createReclamo(reclamoVO);
			if (reclamo.hasError()) {
				tx.rollback();
				reclamo.passErrorMessages(liqReclamoAdapterVO);
				return liqReclamoAdapterVO;
			}

			// createReclamo hace una validacion de que ya no exista el reclamo.
			// si sucede que existe lo carga dentro de message, y se muestra en una pantalla distinta a la del formulario.
			// estos if, manejan esta situacion fuera de lo normal, donde gralmente un error de validacion se informa en los addRecoverableError()
			if (reclamo.getListMessage().isEmpty()) {
				// no hay ningun mensaje, suponemos que esta todo OK
				liqReclamoAdapterVO.addMessageValue("Su reclamo ha sido creado exitosamente, a la brevedad nos comunicaremos con usted.");
				liqReclamoAdapterVO.setReclamoCreado(true);
				tx.commit();
			} else {
				// hay un mensaje, lo transferimos al adapter como esta, si ponemos que no fue creado el reclamo. 
				liqReclamoAdapterVO.addMessage(reclamo.getListMessage().get(0));
				liqReclamoAdapterVO.setReclamoCreado(false);
				tx.rollback();
			}
			
			log.debug(funcName + ": exit");
			return liqReclamoAdapterVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	

	/**
	 *  Creacion del reclamo web sobre un registro de cuota de convenio a travez del Facade.  
	 * 
	 */
	public LiqReclamoAdapter createReclamoCuota(UserContext userContext, LiqReclamoAdapter liqReclamoAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
						
			if(liqReclamoAdapterVO == null){
				throw new DemodaServiceException();
			}
			
			liqReclamoAdapterVO.clearError();
			LiqReclamoVO reclamoVO = liqReclamoAdapterVO.getLiqReclamo();
			Long idCuota = new Long(liqReclamoAdapterVO.getSelectedId());
			ConvenioCuota convenioCuota = ConvenioCuota.getById(idCuota);

			reclamoVO.setTipoTramite(VariosWebFacade.COD_RECLAMO_ASENTAMIENTO_CUOTA);
			reclamoVO.setIdConvenio(convenioCuota.getConvenio().getId());  // Seteamos este id para poder volver al verConvenio.

			// Realiza validacion y luego lo creamos en la db si corresponde.
			Reclamo reclamo = BalReclamoManager.getInstance().createReclamo(reclamoVO);
			if (reclamo.hasError()) {
				tx.rollback();
				reclamo.passErrorMessages(liqReclamoAdapterVO);
				return liqReclamoAdapterVO;
			}

			// createReclamo hace una validacion de que ya no exista el reclamo.
			// si sucede que existe lo carga dentro de message, y se muestra en una pantalla distinta a la del formulario.
			// estos if, manejan esta situacion fuera de lo normal, donde gralmente un error de validacion se informa en los addRecoverableError()
			if (reclamo.getListMessage().isEmpty()) {
				// no hay ningun mensaje, suponemos que esta todo OK
				liqReclamoAdapterVO.addMessageValue("Su reclamo ha sido creado exitosamente, a la brevedad nos comunicaremos con usted.");
				liqReclamoAdapterVO.setReclamoCreado(true);
				tx.commit();
			} else {
				// hay un mensaje, lo transferimos al adapter como esta, si ponemos que no fue creado el reclamo. 
				liqReclamoAdapterVO.addMessage(reclamo.getListMessage().get(0));
				liqReclamoAdapterVO.setReclamoCreado(false);
				tx.rollback();
			}
			
			log.debug(funcName + ": exit");
			return liqReclamoAdapterVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	/**
	 * Inicializa en Adapter de Convenio, para ve el "Detalle de Convenio" desde la liquidacion de la deuda.
	 * - Recibe un LiqConvenioCuentaAdapter  con convenio.IdConvenio seteado
	 * - Devuelve el LiqConvenioCuentaAdapter con todos los datos seteados.
	 * - La propiedad cuenta.idCuenta sera utilizado por el metodo volver.
	 * 
	 * 
	 */
	public LiqConvenioCuentaAdapter getLiqConvenioCuentaInit(UserContext userContext, LiqConvenioCuentaAdapter liqConvenioCuentaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			liqConvenioCuentaVO.clearError();
			
			Long idConvenio = liqConvenioCuentaVO.getConvenio().getIdConvenio();
			
			if (idConvenio == null){
				throw new DemodaServiceException("El id de Convenio no puede ser nulo");				
			}
			
			// 1:: Se recupera el Convenio
			log.debug(funcName + " 1:: Convenio.getById()");
			Convenio convenio = Convenio.getById(idConvenio);
			
			// Pasamos el filtro a la cuenta
			if(convenio.getCuenta() != null)
				convenio.getCuenta().setLiqCuentaFilter(liqConvenioCuentaVO.getCuentaFilter());
						
			// Llamada al helper que devuele el estado completo de la deuda
			LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(convenio);
			
			// Seteo los datos de la cuneta.
			log.debug(funcName + " 2:: liqDeudaBeanHelper.getLiqConvenioCuentaAdapter()");
			
			liqConvenioCuentaVO = liqDeudaBeanHelper.getLiqConvenioCuentaAdapter();
			
			log.debug(funcName + ": exit");
			return liqConvenioCuentaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	/**
	 * Genera el PrintModel del informe de deuda de la cuenta pasada como parametro
	 * 
	 * @param userContext
	 * @param idCuenta 
	 * @return
	 * @throws DemodaServiceException 
	 */
	public PrintModel imprimirInformeDeudaEscribano(UserContext userContext,long idCuenta, InformeDeudaCaratula informeDeudaCaratula) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		PrintModel print = null;
		try {			
			Cuenta cuenta = Cuenta.getByIdNull(idCuenta);
			if (cuenta == null){
				throw new DemodaServiceException("No se encontro la cuenta con id:"+idCuenta);				
			}
			
			print = cuenta.getImprimirInformeDeudaEscribano(informeDeudaCaratula);
			print.putCabecera("Usuario", userContext.getUserName());
			
			
			log.debug(funcName + ": exit");
			return print;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}
	}
	
	/**
	 * Genera el PrintModel del informe de deuda de la cuenta pasada como parametro
	 * 
	 * @param userContext
	 * @param idCuenta 
	 * @return
	 * @throws DemodaServiceException 
	 */
	public PrintModel imprimirInformeLiqDeuda(UserContext userContext,LiqCuentaVO cuentaVO, InformeDeudaCaratula informeDeudaCaratula) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		PrintModel print = null;
		try {			
			Cuenta cuenta = Cuenta.getByIdNull(cuentaVO.getIdCuenta());
			if (cuenta == null){
				throw new DemodaServiceException("No se encontro la cuenta con id:"+cuentaVO.getIdCuenta());				
			}
			log.debug("Filtro Periodo: "+cuentaVO.getEstadoPeriodo().getValue());
			cuenta.setLiqCuentaFilter(cuentaVO);
			
			print = cuenta.getInformeLiqDeuda();
			print.putCabecera("Usuario", userContext.getUserName());
			print.setExcludeFileName("/publico/general/reportes/exclude.xml");
			
			log.debug(funcName + ": exit");
			return print;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}
	}
	
	/**
	 * Imprime los datos de un convenio 
	 * 
	 * 
	 */
	public LiqConvenioCuentaAdapter imprimirLiqConvenioCuenta(UserContext userContext, LiqConvenioCuentaAdapter liqConvenioCuentaAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 


			Long idConvenio = liqConvenioCuentaAdapter.getConvenio().getIdConvenio();
			if (idConvenio == null){
				throw new DemodaServiceException("El id de Convenio no puede ser nulo");				
			}
			Convenio convenio = Convenio.getById(idConvenio);

			liqConvenioCuentaAdapter.getConvenio().setListCuotaDeudaImputadas(convenio.getListCuotaDeudaVO());
			GdeDAOFactory.getConvenioDAO().imprimirGenerico(liqConvenioCuentaAdapter.getContenedorForReport(), liqConvenioCuentaAdapter.getReport());
	   		 
			log.debug(funcName + ": exit");
			return liqConvenioCuentaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	//	<--- Fin Liquidacion de la Deuda
	
	
	// ---> Administrar Deuda Reclamada (SINC de Balance)
	public LiqDeudaAdapter getByRecursoNroCuenta4DeudaReclamada(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			
			DemodaTimer dt = new DemodaTimer();
			
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			liqDeudaAdapterVO.clearError();

			String numeroCuenta = liqDeudaAdapterVO.getCuenta().getNumeroCuenta();
			Long idRecurso = liqDeudaAdapterVO.getCuenta().getIdRecurso();
			
			if (StringUtil.isNullOrEmpty(numeroCuenta)){
				liqDeudaAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_LABEL );
			}
			
			if (idRecurso == null || idRecurso.equals(-1L) ){
				liqDeudaAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL );				
			}
			
			if (liqDeudaAdapterVO.hasError()){
				return liqDeudaAdapterVO;			
			}
			
			// 1:: Se recupera la cuenta
			log.debug(funcName + " 1:: getByIdRecursoYNumeroCuenta()");
			Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(idRecurso, numeroCuenta, Estado.ACTIVO);
			
			if (cuenta == null){
				liqDeudaAdapterVO.addRecoverableValueError("La cuenta numero " + numeroCuenta + " es inexistente");				
			}
			
			if (liqDeudaAdapterVO.hasError()){
				return liqDeudaAdapterVO;			
			}
			
			// Llamada al helper que devuele el estado completo de la deuda
			LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(cuenta);
			
			liqDeudaAdapterVO = liqDeudaBeanHelper.getLiqDeudaAdapter4DeudaReclamada();
			
			log.info(dt.stop(" LiqDeuda : getByRecursoNroCuenta4DeudaReclamada()"));
			
			log.debug(funcName + ": exit");
			return liqDeudaAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			// throw new DemodaServiceException(e);
			// Codigo para R11 quitar mas adelante
			liqDeudaAdapterVO.addRecoverableValueError("No se pudo recuperar la cuenta");
			return liqDeudaAdapterVO;
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	
	//	 <--- FIN Administrar Deuda Reclamada (SINC de Balance)
	
	public ConvenioSearchPage getConvenioSearchPageInit(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ConvenioSearchPage convenioSearchPage = new ConvenioSearchPage();
			
			// Aqui obtiene lista de BOs
			List<Recurso> listRecurso = Recurso.getListTributariosVigentes(new Date());
			
			// Seteo la lista de recursos
			convenioSearchPage.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				convenioSearchPage.getListRecurso().add(item.toVOWithCategoria());							
			}
			
			// Seteo del id para que sea nulo
			convenioSearchPage.getRecurso().setId(new Long(-1));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return convenioSearchPage;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public ConvenioSearchPage getConvenioSearchPageResult(UserContext userContext, ConvenioSearchPage convenioSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			convenioSearchPage.clearError();

			//Aqui realizar validaciones 
			// Si el usuario no es un procurador, se acota la busqueda.
	 	//	if (!userContext.getEsProcurador()){ a partir del 16/10/08 los procuradores consultan cualquier convenio por lo que se vuelve a limitar la busqueda por rec y cuenta
				// Recurso y Cuenta o Numero Convenio
			if (convenioSearchPage.getConvenio().getNroConvenio() == null &&
					ModelUtil.isNullOrEmpty(convenioSearchPage.getRecurso()) && 
					 StringUtil.isNullOrEmpty(convenioSearchPage.getConvenio().getCuenta().getNumeroCuenta())){
				convenioSearchPage.addRecoverableValueError("Debe Ingresar Recurso y Cuenta, o N\u00FAmero de Convenio"); 
				return convenioSearchPage;
			}
			
			if (convenioSearchPage.getConvenio().getNroConvenio() == null){
				if (ModelUtil.isNullOrEmpty(convenioSearchPage.getRecurso())){
					convenioSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL);				
				}
					
				if (StringUtil.isNullOrEmpty(convenioSearchPage.getConvenio().getCuenta().getNumeroCuenta())){
					convenioSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_NUMEROCUENTA_REF);
				}
					
				if (convenioSearchPage.hasError())
					return convenioSearchPage;
			}
	 		//}
			// Filtro para Permitir unicamente Recursos habilitados por el Area del Usuario
			if (convenioSearchPage.getConvenio().getNroConvenio() != null && ModelUtil.isNullOrEmpty(convenioSearchPage.getRecurso())){
				List<Recurso> listRecursoFiltro = Recurso.getListTributarios();
				String listIdRecursoFiltro = null;
				if(!ListUtil.isNullOrEmpty(listRecursoFiltro)){
					listIdRecursoFiltro = ListUtil.getStringIds(listRecursoFiltro);
				}
				
				convenioSearchPage.setListIdRecursoFiltro(listIdRecursoFiltro);
			}
			
			// Aqui obtiene lista de BOs
	   		List<Convenio> listConvenio = GdeDAOFactory.getConvenioDAO().getBySearchPage(convenioSearchPage);  

	   		convenioSearchPage.setListResult(new ArrayList());
	   		
	   		//Aqui pasamos BO a VO
	   		for (Convenio convenio:listConvenio){
	   			ConvenioVO convenioVO = convenio.toVOForSearch();
	   			convenioVO.getRecurso().setDesRecurso(convenio.getRecurso().getDesRecurso());
	   			convenioSearchPage.getListResult().add(convenioVO);
	   		}
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return convenioSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	//	 ---> Consultar Cuenta por Objeto Imponible
	public CuentaObjImpSearchPage getCuentaObjImpSearchPageInit(UserContext userContext) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			CuentaObjImpSearchPage cuentaObjImpSearchPage = new CuentaObjImpSearchPage(); 
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return cuentaObjImpSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public CuentaObjImpSearchPage getCuentaObjImpSearchPageParamObjImp(UserContext userContext, CuentaObjImpSearchPage cuentaObjImpSearchPage) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			//Obtemenos el Objeto Imponible  
			ObjImp objImp = ObjImp.getById(cuentaObjImpSearchPage.getObjImp().getId());

			//Obtenemos el definition del Objeto Imponible
			TipObjImpDefinition tipObjImpDefinition = objImp.getDefinitionValue();
			
			//Aqui pasamos el BO a VO
			ObjImpVO objImpVO = (ObjImpVO) objImp.toVO(1,false);
			
			// Lo setemaos en el Search Page
			cuentaObjImpSearchPage.setObjImp(objImpVO);
			cuentaObjImpSearchPage.setTipObjImpDefinition(tipObjImpDefinition);
			
			//Obtenemos la lista de Cuentas asociadas al Objeto Imponible
			
			List<Cuenta> listCuenta = PadDAOFactory.getCuentaDAO().getListActivaByCuentaObjImpSearchPage(cuentaObjImpSearchPage);
			
			cuentaObjImpSearchPage.setListResult((ArrayList<CuentaVO>) ListUtilBean.toVO(listCuenta, 1, false));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return cuentaObjImpSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	//	 <--- Consultar Cuenta por Objeto Imponible

	// ---> Consulta Estado de Cuenta
	public EstadoCuentaSearchPage getEstadoCuentaSearchPageInit(UserContext userContext, EstadoCuentaSearchPage searchPageFiltro) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			EstadoCuentaSearchPage searchPage = new EstadoCuentaSearchPage();
			//log.debug("searchPageCargado NroCuenta: "+ searchPageFiltro.getCuenta().getNumeroCuenta()+" ID Cuenta "+searchPageFiltro.getCuenta().getId());
			if(searchPageFiltro!=null && searchPageFiltro.getCuenta()!=null && 
				searchPageFiltro.getCuenta().getId()!=null && searchPageFiltro.getCuenta().getId().longValue()>0){

				// Setea la cuenta que viene en el searchPageFiltro
				searchPage.setCuenta((CuentaVO) Cuenta.getById(searchPageFiltro.getCuenta().getId()).toVO(0, false));
				
				// Setea la bandera que habilita el boton de buscar cuenta
				searchPage.setHabilitarCuentaEnabled(searchPageFiltro.getHabilitarCuentaEnabled());
			}
			
			// Seteo la lista de recursos
			List<Recurso> listRecurso = new ArrayList<Recurso>();
			if(searchPageFiltro!=null && searchPageFiltro.getCuenta()!=null && 
					searchPageFiltro.getCuenta().getRecurso()!=null && searchPageFiltro.getCuenta().getRecurso().getId()!=null &&
					searchPageFiltro.getCuenta().getRecurso().getId().longValue()>0){
				// Setea el recurso que viene en el searchPageFiltro
				listRecurso.add(Recurso.getById(searchPageFiltro.getCuenta().getRecurso().getId()));
				searchPage.getCuenta().getRecurso().setId(searchPageFiltro.getCuenta().getRecurso().getId());				
			}else{
				listRecurso = Recurso.getListTributariosVigentes(new Date());
				searchPage.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
				// Seteo del id para que sea nulo
				searchPage.getCuenta().getRecurso().setId(new Long(-1));
			}
			for (Recurso item: listRecurso){				
				searchPage.getListRecurso().add(item.toVOWithCategoria());							
			}
			
			// Seteo la lista de viaDeuda
			List<ViaDeuda> listViaDeuda = ViaDeuda.getListActivos();
			searchPage.setListViaDeuda((ArrayList<ViaDeudaVO>)ListUtilBean.toVO(listViaDeuda, 0, new ViaDeudaVO(-1, StringUtil.SELECT_OPCION_TODOS)));
						
			// Seteo la lista de estadoDeuda
			List<EstadoDeuda> listEstadoDeuda = EstadoDeuda.getListActivos();
			listEstadoDeuda.remove(0);// elimina el primer registro para que no haya 2 estados "impaga"
			searchPage.setListEstadoDeuda((ArrayList<EstadoDeudaVO>)ListUtilBean.toVO(listEstadoDeuda, 0, new EstadoDeudaVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			
			
			// Seteo la lista de clasificacion inicial
			if(searchPage.getCuenta().getRecurso().getId().longValue() > 0){
				// Seteo la lista de clasificaciones dependiendo del recurso seleccionado
				searchPage.setListRecClaDeu((ArrayList<RecClaDeuVO>) ListUtilBean.toVO(
									RecClaDeu.getListByIdRecurso(searchPage.getCuenta().getRecurso().getId()), 0,
									new RecClaDeuVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
			}else{
				searchPage.getListRecClaDeu().add(new RecClaDeuVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));				
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return searchPage;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	

	public EstadoCuentaSearchPage getEstadoCuentaSearchPageParamClasificacion(UserContext userContext, EstadoCuentaSearchPage searchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// Seteo la lista de clasificaciones dependiendo del recurso seleccionado
			searchPage.setListRecClaDeu((ArrayList<RecClaDeuVO>)ListUtilBean.toVO(
								RecClaDeu.getListByIdRecurso(searchPage.getCuenta().getRecurso().getId()), 0,
								new RecClaDeuVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return searchPage;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public EstadoCuentaSearchPage getEstadoCuentaSearchPageParamCuenta(UserContext userContext, EstadoCuentaSearchPage searchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// Seteo la cuenta
			Cuenta cuenta = Cuenta.getById(searchPage.getCuenta().getId());
			searchPage.setCuenta((CuentaVO) cuenta.toVO(0, false));
			searchPage.getCuenta().getRecurso().setId(cuenta.getRecurso().getId());
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return searchPage;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
		
	public EstadoCuentaAdapter getLiqEstadoCuentaAdapter(UserContext userContext, EstadoCuentaSearchPage searchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			searchPage.clearError();

	   		// Crear el LiqEstadoCuentaAdapter a devolver
			EstadoCuentaAdapter liqEstadoCuentaAdapter = new EstadoCuentaAdapter();

			// Setea las fechas de filtro de la busqueda
			liqEstadoCuentaAdapter.setFechaVtoDesde(searchPage.getFechaVtoDesdeView());
			liqEstadoCuentaAdapter.setFechaVtoHasta(searchPage.getFechaVtoHastaView());
			
			// Valida y obtiene la cuenta
			Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuentaForEC(searchPage.getCuenta().getRecurso().getId(), searchPage.getCuenta().getNumeroCuenta());
			if(cuenta==null){
				liqEstadoCuentaAdapter.addRecoverableError(GdeError.ESTADOCUENTA_CUENTA_NO_ENCONTRADA);
				return liqEstadoCuentaAdapter;
			}
							
			
			// Setea la cuenta para realizar la busqueda
			searchPage.setCuenta((CuentaVO) cuenta.toVO(0, false));
			
			// Aqui obtiene lista de deuda BOs
	   		List<Deuda> listDeuda = cuenta.getListDeudaEstadoCuenta(searchPage); 
						
			// Setea la cuenta
			LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(cuenta);
			liqDeudaBeanHelper.esParaEstadoCuenta=true;
			LiqCuentaVO liqCuentaVO = liqDeudaBeanHelper.getCuenta(LiqDeudaBeanHelper.ALL);			
			
			liqEstadoCuentaAdapter.setCuenta(liqCuentaVO);
			liqEstadoCuentaAdapter.setListCuentaRel(liqDeudaBeanHelper.getCuentasRelacionadas());
			liqEstadoCuentaAdapter.setExenciones(liqDeudaBeanHelper.getExenciones());
			liqEstadoCuentaAdapter.setPropietario(cuenta.getDesTitularPrincipal());
			
			// Setea la lista de LiqDeudaPagoDeuda
			for(Deuda deuda: listDeuda){
				// Se setean los datos de la deuda
				LiqDeudaVO liqDeudaVO = new LiqDeudaVO();
								
				double importe = (deuda.getImporte()!=null?deuda.getImporte():0D);
				double actualizacion = (deuda.getActualizacion()!=null?deuda.getActualizacion():0D);
				double saldo = (deuda.getSaldo()!=null?deuda.getSaldo():0D);
				double total = importe-saldo+actualizacion;
								
				liqDeudaVO.setPeriodoDeuda(deuda.getStrPeriodo());
				liqDeudaVO.setCodRefPag(deuda.getCodRefPag().toString());
				liqDeudaVO.setDesViaDeuda(deuda.getViaDeuda().getDesViaDeuda());
				liqDeudaVO.setFechaVencimiento(deuda.getFechaVencimiento());
				liqDeudaVO.setIdDeuda(deuda.getId());
				liqDeudaVO.setIdEstadoDeuda(deuda.getEstadoDeuda().getId());
				liqDeudaVO.setImporte(importe);
				liqDeudaVO.setSaldo(saldo);
				liqDeudaVO.setActualizacion(actualizacion);
				liqDeudaVO.setTotal(total);
				
				// Setea la descripcion del estado de la deuda. 
				// Las constantes ID_ADMINISTRATIVA y ID_JUDICIAL significan que esta IMPAGA (en esas vias), por mas que sea confuso
				String desEstado = "";
				if(deuda.getEstadoDeuda().getId().equals(EstadoDeuda.ID_ADMINISTRATIVA) ||
						deuda.getEstadoDeuda().getId().equals(EstadoDeuda.ID_JUDICIAL)){					
					if(deuda.getEsExcentaPago())
						desEstado=SiatUtil.getValueFromBundle(GdeError.MSG_DEUDA_EXENTA);//Exento de Pago
					else if(deuda.getEsIndeterminada())
						desEstado=SiatUtil.getValueFromBundle(GdeError.MSG_DEUDA_INDETERMINADA);//Pago sin procesar
					else if(deuda.getEsReclamada())
						desEstado=SiatUtil.getValueFromBundle(GdeError.MSG_DEUDA_RECLAMADA);//Pago en estado de análisis
					else if(deuda.getEstaEnAsentamiento())
						desEstado=SiatUtil.getValueFromBundle(GdeError.MSG_DEUDA_INDETERMINADA);//Pago en estado de análisis
					else{
						desEstado=deuda.getEstadoDeuda().getDesEstadoDeuda();
					}
					if (cuenta.getRecurso().getEsAutoliquidable().intValue()==SiNo.SI.getId().intValue()){
						if (deuda.getImporte()>0D && deuda.getSaldo()==0D){
							desEstado=SiatUtil.getValueFromBundle(GdeError.MSG_DEUDA_PAGA);//Paga
						}
					}
				}else{
					desEstado=deuda.getEstadoDeuda().getDesEstadoDeuda();
				}
				
				liqDeudaVO.setDesEstado(desEstado);
				
				
				LiqDeudaPagoDeudaVO liqDeudaPagoDeudaVO = new LiqDeudaPagoDeudaVO();
				liqDeudaPagoDeudaVO.setDeuda(liqDeudaVO);				
				
				//Se obtiene la lista de pagos y se pasan a VO
				List<PagoDeuda> listPagoDeuda = PagoDeuda.getByDeuda(deuda.getId());
				for(PagoDeuda pagoDeuda: listPagoDeuda){
					LiqPagoDeudaVO liqPagoDeuda = new LiqPagoDeudaVO();
					liqPagoDeuda.setDesTipoPago(pagoDeuda.getTipoPago().getDesTipoPago());
					liqPagoDeuda.setFechaPago(pagoDeuda.getFechaPago());
					liqPagoDeuda.setImporte(pagoDeuda.getImporte());
					liqPagoDeuda.setActualizacion(pagoDeuda.getActualizacion());
					liqPagoDeuda.setDescripcion(pagoDeuda.getDescripcion());
					Banco bancoPago = pagoDeuda.getBancoPago();					
					liqPagoDeuda.setDesBancoPago((bancoPago!=null?bancoPago.getDesBanco():""));
					liqDeudaPagoDeudaVO.getListPagoDeuda().add(liqPagoDeuda);
				}
				
				// Se agrega la deuda
				liqEstadoCuentaAdapter.getListDeudaPagoDeuda().add(liqDeudaPagoDeudaVO);
			}			
						
			// Seteo de Permisos
			//liqEstadoCuentaAdapter.setVerDeudaContribEnabled(liqDeudaBeanHelper.getPermisoSwe(GdeSecurityConstants.MTD_VER_DEUDA_CONTRIB));
			liqEstadoCuentaAdapter.setVerConvenioEnabled(liqDeudaBeanHelper.getPermisoSwe(GdeSecurityConstants.MTD_VER_CONVENIO));			
			liqEstadoCuentaAdapter.setVerCuentaDesgUnifEnabled(liqDeudaBeanHelper.getPermisoSwe(GdeSecurityConstants.MTD_VER_CUENTA_DESG_UNIF));
			liqEstadoCuentaAdapter.setVerCuentaRelEnabled(liqDeudaBeanHelper.getPermisoSwe(GdeSecurityConstants.MTD_VER_CUENTA_REL));
						
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return liqEstadoCuentaAdapter;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PrintModel getImprimirEstadoCuenta(UserContext userContext, EstadoCuentaAdapter estadoCuentaAdapter) throws Exception{
		
		// Obtiene el formulario
		PrintModel print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_ESTADO_CUENTA);
				
		// Le setea los datos		
		print.putCabecera("usuario", userContext.getUserName());
		print.setData(estadoCuentaAdapter);
		print.setTopeProfundidad(4);
		
		return print;

	}

	/**
	 * Obtiene el searchPage para ir al estado de cuenta, con la cuenta pasada como parametro
	 */
	public EstadoCuentaSearchPage getEstadoCuentaSeachPageFiltro(UserContext userContext, Long idCuenta) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			// Se obtiene la cuenta
			Cuenta cuenta = Cuenta.getById(idCuenta);
			
			// Se crea el searchPage a devolver y se le setean los datos de la cuenta y el recurso
			EstadoCuentaSearchPage searchPage = new EstadoCuentaSearchPage();
			searchPage.setCuenta(cuenta.toVOWithRecurso());
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return searchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- Consulta Estado de Cuenta	

	public TramiteSearchPage getTramiteSearchPageInit(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			TramiteSearchPage tramiteSearchPage = new TramiteSearchPage();
						
			List<TipoTramite> listTipoTramite = TipoTramite.getList();
			
			tramiteSearchPage.setListTipoTramite( ListUtilBean.toVO(listTipoTramite, 
					new TipoTramiteVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
						
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return tramiteSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TramiteSearchPage getTramiteSearchPageResult(UserContext userContext, TramiteSearchPage tramiteSearchPage) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			tramiteSearchPage.clearError();

	   						   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return null;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TramiteAdapter getTramiteAdapterInit(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			TramiteAdapter tramiteAdapter = new TramiteAdapter();
						
			List<TipoTramite> listTipoTramite = TipoTramite.getList();
			
			tramiteAdapter.setListTipoTramite( ListUtilBean.toVO(listTipoTramite, 
					new TipoTramiteVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
						
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return tramiteAdapter;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TramiteAdapter validarTramite(UserContext userContext, TramiteAdapter tramiteAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			tramiteAdapter.clearError();
			
			if (StringUtil.isNullOrEmpty(tramiteAdapter.getTramite().getTipoTramite().getCodTipoTramite())){
				tramiteAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.TIPOTRAMITE_LABEL);
			}
			
			if (tramiteAdapter.getTramite().getCodRefPag() == null){
				tramiteAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.TRAMITE_CODREFPAG_LABEL);
			}
						
			if (tramiteAdapter.hasError()){
				return tramiteAdapter;
			}
			
			/*
			 *  - Chequeo en siat si es un recibo de tramite ya utilizado.
			 *  	
			 *  	- Si lo encuentra devuelve esValido=fasle (porque ya fue usado)
			 *  
			 *  	- Si no es encontrado en siat, se chequea contra "gravamenes"
			 *  		
			 *  		- Si no se encuentra en gravamenes, se informa error y devuelve esValido=fase.
			 *  	
			 *   		- Si es encontrado en gravamenes, devuelve esValido=true 
			 * 
			 */
			
			// Chequeo que sea valido en gravamenes
	   		//boolean esValido = GdeDAOFactory.getGravamenesJDBCDAO().getEsReciboValido(tramiteAdapter);  
	   		boolean esValido = getEsReciboValido(tramiteAdapter);
	   		
	   		log.debug(funcName + " gravamenes.esReciboValido: " + esValido);
	   		
	   		// Chequeo que no halla sido usado en siat 
	   		if (esValido) {
	   			esValido = GdeDAOFactory.getCtrlInfDeuDAO().chkEsCtrlInfValido(tramiteAdapter);
	   			log.debug(funcName + " siat.chkEsCtrlInfValido: " + esValido);
	   		
		   		// Chequeo si no es valido pero se utilizo el mismo dia y para la misma cuenta. Si es asi se habilita.
		   		if (!esValido) {
		   			Long nroRecibo = 0L;
		   			Long anioRecibo = 0L;
		   			String sCodRefPag = tramiteAdapter.getTramite().getCodRefPag();
		   			if (sCodRefPag.indexOf("/") >= 0) {
		   				try { nroRecibo = Long.valueOf(sCodRefPag.split("/")[0]); } catch (Exception e) {}
		   				try { anioRecibo = Long.valueOf(sCodRefPag.split("/")[1]); } catch (Exception e) {}		   				
		   			} else {
		   				try { nroRecibo = Long.valueOf(sCodRefPag); } catch (Exception e) {}	
		   			}
		   			if(nroRecibo != 0){
		   				CtrlInfDeu ctrlInfDeu = CtrlInfDeu.getByNroYAnio(nroRecibo, anioRecibo);
		   				if(ctrlInfDeu != null){
		   					Date fechaCtrlInfDeu = DateUtil.getDateFromDate(ctrlInfDeu.getFechaHoraGen());
		   					Date fechaActual = new Date();
		   					if(ctrlInfDeu.getCuenta().getId().longValue() == tramiteAdapter.getTramite().getCuenta().getId().longValue() 
		   							&& DateUtil.isDateEqual(fechaCtrlInfDeu, fechaActual)){
		   						esValido = true;
		   					}
		   				}
		   				log.debug(funcName + " siat.chkEsCtrlInfValido: " + esValido);		   				
		   			}
		   		}
	   		}
	   		
	   		if (!esValido) {
	   			tramiteAdapter.addRecoverableValueError("El recibo NO es Valido o ya fue utilizado.");	   			
	   		}
	   		
	   		tramiteAdapter.setEsValido(esValido);
	   						   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return tramiteAdapter;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	private boolean getEsReciboValido(TramiteAdapter tramiteAdapter) throws Exception {
		String sCodRefPag = tramiteAdapter.getTramite().getCodRefPag();
		String tipoTramite = tramiteAdapter.getTramite().getTipoTramite().getCodTipoTramite();
		Cuenta cuenta = null;
		String tipoSellado;
	
		// es un nro recibo-> entonces vamos por el lado del recibo.cuenta y atributos de cuenta
		if (sCodRefPag.indexOf("/") >= 0) {
			//es un recbio / anio
			Long nroRecibo = null;
			try { nroRecibo = Long.parseLong(sCodRefPag.split("/")[0]); } catch (Exception e) {}
			if (nroRecibo == null) {
				tramiteAdapter.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, GdeError.TRAMITE_CODREFPAG_LABEL);	
				return false;
			}
			Recibo recibo = Recibo.getByNumero(nroRecibo);
			if (recibo == null) {
				tramiteAdapter.addRecoverableValueError("No se encontro n&uacute;mero de recibo.");	
				return false;
			}
			cuenta = recibo.getCuenta();

			GenericDefinition atrValDef = cuenta.getRecCueAtrDefinitionValue(new Date());
			tipoSellado = (String) atrValDef.getValor("tiposellado");
			
		// es un codrefpag -> obtenemos deuda y atributo de emision
		} else {
			// es un codrefpag
			Long codRefPag = null;
			try { codRefPag = Long.parseLong(sCodRefPag); } catch (Exception e) {}
			Deuda deuda = Deuda.getByCodRefPag(codRefPag);
			if (deuda == null) {
				tramiteAdapter.addRecoverableValueError("No se encontro Cod. Ref. Pag..");	
				return false;
			}
			AtrEmisionDefinition def = deuda.getAtributosEmisionDefValue();
			tipoSellado = (String) def.getValor("tiposellado");
			
			cuenta = deuda.getCuenta();
		}
	
		// En este punto tengo el valor de tiposellado correpondiente a la deuda o al recibo
		// ahora verificamos que la cuenta la cuenta del recibo o de la deuda
		// tenga el atributo tipo sellado, que corresponde al tramite que se esta
		// realizando.
		if (tipoSellado == null) {
			tramiteAdapter.addRecoverableValueError("No se encontro valorizado el tiposellado en atributo de cuenta.");				
			return false;
		}
		//ok finalmente verificamos que esta cuenta soporte este tipo de tramite.
		if (!tipoSellado.equals(tipoTramite)) {
			tramiteAdapter.addRecoverableValueError("Los datos ingresados no permiten realizar el tramite solicitado.");
			return false;
		}
		
		tramiteAdapter.getTramite().setCuenta((CuentaVO) cuenta.toVO(0, false));
		
		return true;
	}

	public TramiteAdapter registrarUsoTramite(UserContext userContext, TramiteAdapter tramiteAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			tramiteAdapter.clearErrorMessages();
						
            CtrlInfDeu ctrlInfDeu = new CtrlInfDeu();
            
            Long idCuenta = tramiteAdapter.getIdCuenta();
            Cuenta cuenta = Cuenta.getById(idCuenta);
    		
            
            tramiteAdapter.setNroLiquidacion(
            		generarNroLiquidacion(tramiteAdapter.getTramite().getNroReciboForCaratula(), cuenta.getNumeroCuenta()));
            
            ctrlInfDeu.setCuenta(cuenta);
            ctrlInfDeu.setNroTramite(1);

    		String sCodRefPag = tramiteAdapter.getTramite().getCodRefPag();
			Long numero = 0L, anio = 0L;
    		if (sCodRefPag.indexOf("/") >= 0) {
    			try { numero = Long.valueOf(sCodRefPag.split("/")[0]); } catch (Exception e) {}
    			try { anio = Long.valueOf(sCodRefPag.split("/")[1]); } catch (Exception e) {}
    		} else {
    			try { numero = Long.valueOf(sCodRefPag); } catch (Exception e) {}
    		}
            
            ctrlInfDeu.setNroRecibo(numero);
            ctrlInfDeu.setAnioRecibo(anio);
            ctrlInfDeu.setCodId(tramiteAdapter.getTramite().getTipoTramite().getCodTipoTramite());
            ctrlInfDeu.setFechaHoraGen(new Date());
            ctrlInfDeu.setFechaHoraImp(new Date());
            ctrlInfDeu.setNroLiquidacion(tramiteAdapter.getNroLiquidacion());
            ctrlInfDeu.setObservacion("");
            ctrlInfDeu.setEstado(Estado.ACTIVO.getId());
            
			
            ctrlInfDeu = GdeGDeudaManager.getInstance().createCtrlInfDeu(ctrlInfDeu);
            
            if (ctrlInfDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}				
			}
            
            ctrlInfDeu.passErrorMessages(tramiteAdapter);
            
            log.debug(funcName + ": exit");
            return tramiteAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
		
	}

	/**
	 * Genera el Numero de Liquidacion 
	 * 
	 * Recibe el numero de recibo formateado "anio - numero"
	 * 
	 * @author Cristian
	 * @return
	 */
	private String generarNroLiquidacion(String strRecibo, String numeroCuenta){
		
		String funcNane = "generarNroLiquidacion";
		/*
		  El numero se arma concatenando los siguientes datos:
		  2 ultimos numeros del anio de recibo ingresado
		  4 ultimos numeros del numero de recibo ingresado
		  4 ultimos numeros del numero de cuenta
		  suma de los siguientes datos de la fecha actual: ano + mes + dia
		 */

		String nroLiquidacion = "";
		String[] arrRecibo = strRecibo.split(" - ");
		String nroRecibo = arrRecibo[0];
		String anioRecibo = arrRecibo[1];
		if (anioRecibo.equals("0")) anioRecibo = "0000";
		
		String fechaActual = DateUtil.formatDate(new Date(), DateUtil.ddMMYY_MASK); 
		
		log.debug( funcNane + " fechaActual: " + fechaActual);
			 
		int anio = Integer.parseInt(fechaActual.substring(4,6));
		int mes = Integer.parseInt(fechaActual.substring(2,4));
		int dia = Integer.parseInt(fechaActual.substring(0,2));
		int suma = anio + mes + dia;
		
		nroLiquidacion += anioRecibo.substring(2, 4);
		nroLiquidacion += nroRecibo.substring(nroRecibo.length() - 4, nroRecibo.length() );
		nroLiquidacion += numeroCuenta.substring(numeroCuenta.length() -4, numeroCuenta.length());
		nroLiquidacion += suma;
		
		log.debug( funcNane + ": nroRecibo: " + nroRecibo +
				" anioRecibo: " + anioRecibo +
				" fechaActual: " + fechaActual +
				" anio: " + anio +
				" mes: " + mes +
				" dia: " + dia +
				" numeroCuenta: " + numeroCuenta + 
				" nroLiquidacion" + nroLiquidacion );
		
		return nroLiquidacion;
	}
	
	// ---> Anular/Desanular Deuda
	public LiqDeudaAdapter getByRecursoNroCuenta4AnularDeuda(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			
			DemodaTimer dt = new DemodaTimer();
			
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			liqDeudaAdapterVO.clearError();

			String numeroCuenta = liqDeudaAdapterVO.getCuenta().getNumeroCuenta();
			Long idRecurso = liqDeudaAdapterVO.getCuenta().getIdRecurso();
			
			if (StringUtil.isNullOrEmpty(numeroCuenta)){
				liqDeudaAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_LABEL );
			}
			
			if (idRecurso == null || idRecurso.equals(-1L) ){
				liqDeudaAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL );				
			}
			
			if (liqDeudaAdapterVO.hasError()){
				return liqDeudaAdapterVO;			
			}
			
			// 1:: Se recupera la cuenta
			log.debug(funcName + " 1:: getByIdRecursoYNumeroCuenta()");
			Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(idRecurso, numeroCuenta); //), Estado.ACTIVO); Se quita el filtro por estado por solicitud en Mantis 4770, nota: 'Debe permitir anular/desanular deuda de cuentas en cualquier estado, hoy solo filtra las activas.'
			
			if (cuenta == null){
				liqDeudaAdapterVO.addRecoverableValueError("La cuenta numero " + numeroCuenta + " es inexistente");				
			}
			
			if (liqDeudaAdapterVO.hasError()){
				return liqDeudaAdapterVO;			
			}
			
			// Llamada al helper que devuele el estado completo de la deuda
			LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(cuenta);
			
			liqDeudaAdapterVO = liqDeudaBeanHelper.getByRecursoNroCuenta4AnularDeuda();
			
			log.info(dt.stop(" LiqDeuda : getByRecursoNroCuenta4AnularDeuda()"));
			
			log.debug(funcName + ": exit");
			return liqDeudaAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			LiqDeudaAdapter liqDeudaAdapterError = initLiqDeudaAdapter(liqDeudaAdapterVO);
			liqDeudaAdapterError.addRecoverableValueError("No se pudo recuperar la cuenta");
			return liqDeudaAdapterError;
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public LiqDeudaAdapter getLiqDeudaAdapterAnularInit(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			liqDeudaAdapterVO.clearErrorMessages();
			
			// Validar permisos sobre la deuda seleccionada:
			boolean noPermitirAnulacion = false;
			UsuarioSiat usuarioActual = UsuarioSiat.getByUserName(userContext.getUserName());
			Area areaEmiPadron = Area.getByCodigo(Area.COD_EMISION_PADRONES);
			Area areaCobAdm = Area.getByCodigo(Area.COD_COBRANZA_ADMINISTRATIVA);
			Area areaCobJud = Area.getByCodigo(Area.COD_JUDICIALES);
			Area areaCyQ = Area.getByCodigo(Area.COD_CONCURSO_Y_QUIEBRA);
			if(usuarioActual != null  && usuarioActual.getArea() != null && usuarioActual.getArea().getId().longValue() != areaEmiPadron.getId().longValue()
					&& usuarioActual.getArea().getId().longValue() != areaCobAdm.getId().longValue() && usuarioActual.getArea().getId().longValue() != areaCobJud.getId().longValue()
					&& usuarioActual.getArea().getId().longValue() != areaCyQ.getId().longValue()){
				for(String strIdDeuda: liqDeudaAdapterVO.getListIdDeudaSelected()){
					String[] arrIdDeuda = strIdDeuda.split("-");
					Long idDeuda = new Long (arrIdDeuda[0]);
					Long idEstadoDeuda = new Long(arrIdDeuda[1]);
					Deuda deuda = Deuda.getById(idDeuda, idEstadoDeuda);
					Emision emisionDeuda = deuda.getEmision();
					UsuarioSiat usuarioEmiDeuda = null; //UsuarioSiat.getByUserName(deuda.getUsuario()); Se toma el usuario de la emision. Cambio solicitado en Mantis 4851. 
					if(emisionDeuda != null){
						usuarioEmiDeuda = UsuarioSiat.getByUserName(emisionDeuda.getUsuario());
					}
					if(usuarioActual != null && usuarioEmiDeuda != null && usuarioActual.getArea() != null && usuarioEmiDeuda.getArea() != null){
						if(usuarioActual.getArea().getId().longValue() != usuarioEmiDeuda.getArea().getId().longValue()){
							noPermitirAnulacion = true;
							break;
						}
					}
				}				
			}
			if(noPermitirAnulacion){
				liqDeudaAdapterVO.addRecoverableError(GdeError.ANULAR_DEUDA_NO_EMITIDA_ERROR);
				log.debug(funcName + ": exit");
				return liqDeudaAdapterVO;
			}
			
			List<MotAnuDeu> listMotAnuDeu = MotAnuDeu.getListMotAnuDeuAnulan();
			
			// Seteo la listas para combos, etc
			liqDeudaAdapterVO.setListMotAnuDeu(ListUtilBean.toVO(listMotAnuDeu, 0, 
					new MotAnuDeuVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			liqDeudaAdapterVO.setAnulacion(new AnulacionVO());
			liqDeudaAdapterVO.getAnulacion().setFechaAnulacion(new Date());
			
						
			log.debug(funcName + ": exit");
			return liqDeudaAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	/**
	 * Realiza la anulacion de la deuda seleccionada.
	 * 
	 */
	public LiqDeudaAdapter getAnularDeuda(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException {
		
		// Validar Requedidos FechaAnulacion, Motivo, Caso, Observacion y listIdDeudaSelected
		// Recuperar la deuda seleccinada
		// Moverla a deuda anulada junto con sus conceptos 
		// 	- Borrar la deuda origen (ya lo hace duda dao).
		
		// Registrar uso caso
		
		// A la deuda anulada setearle: 
		//	el estadoDeuda corresponiente al motivo
		//  fechaAnulacion
		//  idMotAnuDeu
		//  Observacion
		//  idCaso
				
		// Por cada deuda anulada insertar un registro en anulacion.
		
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession(); 
			tx = session.beginTransaction();
			
			liqDeudaAdapterVO.clearErrorMessages();
			
			if (liqDeudaAdapterVO.getAnulacion().getFechaAnulacion() == null){
				liqDeudaAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.ANULACION_FECHAANULACION);
			}
			if (ModelUtil.isNullOrEmpty(liqDeudaAdapterVO.getAnulacion().getMotAnuDeu())){
				liqDeudaAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.MOTANUDEU_LABEL);
			}			
			if (StringUtil.isNullOrEmpty(liqDeudaAdapterVO.getAnulacion().getObservacion())){
				liqDeudaAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.ANULACION_OBSERVACION);
			}
			
			if (liqDeudaAdapterVO.getListIdDeudaSelected() == null){
				liqDeudaAdapterVO.addRecoverableValueError("Debe seleccionar al menos un registro de deuda a anular");
			}
			
			if (liqDeudaAdapterVO.getAnulacion().getCaso().getIdFormateado() == null){
				liqDeudaAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CasError.CASO_LABEL);
			}
			
			if (liqDeudaAdapterVO.hasError()){
				return liqDeudaAdapterVO;
			}
			
			
			// Recuperamos la deuda seleccinada			
			String[] listIdDeudaSelected = liqDeudaAdapterVO.getListIdDeudaSelected();
			String listIdDeuda4Caso = "";
			List<Deuda> listDeudaAnular = new ArrayList<Deuda>();
			
			if (listIdDeudaSelected != null){
				for (int i=0; i < listIdDeudaSelected.length; i++){
					
					String strIdDeuda = listIdDeudaSelected[i];
					String[] arrIdDeuda = strIdDeuda.split("-");
					
					Long idDeuda = new Long (arrIdDeuda[0]);
					Long idEstadoDeuda = new Long(arrIdDeuda[1]);
					
					Deuda deuda = Deuda.getById(idDeuda, idEstadoDeuda);
					listDeudaAnular.add(deuda);	
					
					listIdDeuda4Caso += deuda.getStrPeriodo();
					
					if (i < listIdDeudaSelected.length -1){
						listIdDeuda4Caso += " - ";
					}
				}
			}
			
			Cuenta cuenta = listDeudaAnular.get(0).getCuenta();
			
			Anulacion anulacion4Caso = new Anulacion();

			// Recuperamos el motivo real de anulacion
			MotAnuDeu motAnuDeu = MotAnuDeu.getById(liqDeudaAdapterVO.getAnulacion().getMotAnuDeu().getId());
			liqDeudaAdapterVO.getAnulacion().setMotAnuDeu((MotAnuDeuVO)motAnuDeu.toVO(0));
			
			liqDeudaAdapterVO.getAnulacion().setListIdDeuda4Caso(listIdDeuda4Caso);
			
			// Registrar uso caso
			// 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_ANULACION_DEUDA); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(liqDeudaAdapterVO.getAnulacion(), anulacion4Caso, 
        			accionExp, cuenta, liqDeudaAdapterVO.getAnulacion().infoString() );
        	
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (liqDeudaAdapterVO.getAnulacion().hasError()){
        		tx.rollback();
        		liqDeudaAdapterVO.getAnulacion().passErrorMessages(liqDeudaAdapterVO);        		
        		return liqDeudaAdapterVO;
        	}
           	// 2) Esta linea debe ir siempre despues de 1).
        	String idCaso = liqDeudaAdapterVO.getAnulacion().getIdCaso();
        	
			EstadoDeuda estadoDeduaAnulada = EstadoDeuda.getById(EstadoDeuda.ID_ANULADA);
	
			// Movemos la deuda a cancelada
			for (Deuda deuda: listDeudaAnular ){
				EstadoDeuda estadoDeudaOrig = deuda.getEstadoDeuda(); 
				GdeGDeudaManager.getInstance().moverDeuda(deuda, estadoDeudaOrig, estadoDeduaAnulada);			

				// Si es deuda de Espectaculos Publicos, nos aseguramos de eliminar
				// los registros de Entradas vendidas (17-09-2009).
				if (cuenta.getRecurso().getCategoria() != null && 
						cuenta.getRecurso().getCategoria().getId().equals(Categoria.ID_ESP_PUB)) {
					Habilitacion.anularEntradasVendidas(deuda.getId());
				}
			}
			
			// Recuperamos la deuda movida 
			List<DeudaAnulada> listDeudaAnulada = new ArrayList<DeudaAnulada>();
			if (listIdDeudaSelected != null){
				for (int i=0; i < listIdDeudaSelected.length; i++){
					
					String strIdDeuda = listIdDeudaSelected[i];
					String[] arrIdDeuda = strIdDeuda.split("-");
					
					Long idDeuda = new Long (arrIdDeuda[0]);
					Long idEstadoDeuda = EstadoDeuda.ID_ANULADA;
					
					Deuda deuda = Deuda.getById(idDeuda, idEstadoDeuda);
					listDeudaAnulada.add((DeudaAnulada)deuda);					
				}
			}
									
			Long idEstadoDeuda = EstadoDeuda.ID_ANULADA;
			if (motAnuDeu.getId().longValue() == MotAnuDeu.ID_CONDONACION)
				idEstadoDeuda = EstadoDeuda.ID_CONDONADA;
			if (motAnuDeu.getId().longValue() == MotAnuDeu.ID_PRESCRIPCION)
				idEstadoDeuda = EstadoDeuda.ID_PRESCRIPTA;
			
			// Recuperamos el estado de deuda real
			EstadoDeuda estadoDeudaUpdate = EstadoDeuda.getById(idEstadoDeuda); 
			
			Date fechaAnulacion = liqDeudaAdapterVO.getAnulacion().getFechaAnulacion();
			String observacion = liqDeudaAdapterVO.getAnulacion().getObservacion();
						
			// Actualizamos al deuda anulada (movida) 
			for (DeudaAnulada deudaAnulada:listDeudaAnulada){
				deudaAnulada.setEstadoDeuda(estadoDeudaUpdate);
				deudaAnulada.setMotAnuDeu(motAnuDeu);
				deudaAnulada.setFechaAnulacion(fechaAnulacion);
				deudaAnulada.setObservacion(observacion);
				deudaAnulada.setIdCaso(idCaso);
				deudaAnulada.setCorrida(null);
				GdeGDeudaManager.getInstance().update(deudaAnulada);
				
				// Por cada deuda anulada insertamos un registro en anulacion.
				Anulacion anulacion = new Anulacion();
				anulacion.setFechaAnulacion(fechaAnulacion);
			    anulacion.setMotAnuDeu(motAnuDeu);
			    anulacion.setIdDeuda(deudaAnulada.getId());
			    anulacion.setIdCaso(idCaso);
			    anulacion.setObservacion(observacion);
			    anulacion.setRecurso(deudaAnulada.getRecurso());
			    anulacion.setViaDeuda(deudaAnulada.getViaDeuda());			    
			    
			    GdeDAOFactory.getAnulacionDAO().update(anulacion);
			}
			
			tx.commit();
			
			log.debug(funcName + ": exit");
			return liqDeudaAdapterVO;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/**
	 * Realiza la vuelta atras de la dedua anulada, movienda a la tabla correspondiente segun la via.
	 * 
	 */
	public void vueltaAtrasAnular(UserContext userContext, CommonKey deudaKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession(); 
			tx = session.beginTransaction();
			
			DeudaAnulada deudaAnulada = DeudaAnulada.getById(deudaKey.getId());
			EstadoDeuda estadoDeudaAnulada = EstadoDeuda.getById(EstadoDeuda.ID_ANULADA); 
			EstadoDeuda estadoDeduaDest;
			
			if (deudaAnulada.getViaDeuda().getId().longValue() == ViaDeuda.ID_VIA_ADMIN){
				estadoDeduaDest = EstadoDeuda.getById(EstadoDeuda.ID_ADMINISTRATIVA);
				GdeGDeudaManager.getInstance().moverDeuda(deudaAnulada, estadoDeudaAnulada, estadoDeduaDest);
			}
			
			if (deudaAnulada.getViaDeuda().getId().longValue() == ViaDeuda.ID_VIA_JUDICIAL){
				estadoDeduaDest = EstadoDeuda.getById(EstadoDeuda.ID_JUDICIAL);
				GdeGDeudaManager.getInstance().moverDeuda(deudaAnulada, estadoDeudaAnulada, estadoDeduaDest);
			}
			
			/*
			 * Si la deuda anulada es de Via CyQ se busca el detalle del procedimiento en el que esta incluida para determinar si correspondia a una deuda en via administrativa o 
			 * en via judicial. (Esto es para volver la deuda a la tabla que corresponde y con el estado impago correspondiente)
			 */
			if (deudaAnulada.getViaDeuda().getId().longValue() == ViaDeuda.ID_VIA_CYQ){
				if(deudaAnulada.getProcedimientoCyQ() != null){
					ProDet proDetDeuda = null;
					for(ProDet proDet: deudaAnulada.getProcedimientoCyQ().getListProDet()){
						if(proDet.getIdDeuda().longValue() == deudaAnulada.getId().longValue()){
							proDetDeuda = proDet;
							break;
						}
					}
					if(proDetDeuda != null){
						if (proDetDeuda.getViaDeuda().getId().longValue() == ViaDeuda.ID_VIA_ADMIN){
							estadoDeduaDest = EstadoDeuda.getById(EstadoDeuda.ID_ADMINISTRATIVA);
							GdeGDeudaManager.getInstance().moverDeuda(deudaAnulada, estadoDeudaAnulada, estadoDeduaDest);
						}
						if(proDetDeuda.getViaDeuda().getId().longValue() == ViaDeuda.ID_VIA_JUDICIAL){
							estadoDeduaDest = EstadoDeuda.getById(EstadoDeuda.ID_JUDICIAL);	
							GdeGDeudaManager.getInstance().moverDeuda(deudaAnulada, estadoDeudaAnulada, estadoDeduaDest);
						}
					}
				}
			}
			
			MotAnuDeu motAnuDeu = MotAnuDeu.getById(MotAnuDeu.ID_DESANULACION);
			
			Anulacion anulacion = new Anulacion();
			anulacion.setFechaAnulacion(new Date());
		    anulacion.setMotAnuDeu(motAnuDeu);
		    anulacion.setIdDeuda(deudaAnulada.getId());		    
		    anulacion.setObservacion("Vuelta Atras Anulacion Deuda");
		    anulacion.setRecurso(deudaAnulada.getRecurso());
		    anulacion.setViaDeuda(deudaAnulada.getViaDeuda());
		    
		    GdeDAOFactory.getAnulacionDAO().update(anulacion);
						
			tx.commit();
			
			log.debug(funcName + ": exit");
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	// <--- Anular/Desanular Deuda

	// ---> Consulta Recibos
	public ReciboSearchPage getReciboSearchPageInit(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ReciboSearchPage reciboSearchPage = new ReciboSearchPage();
			
			reciboSearchPage.setListTipoRecibo(TipoRecibo.getList());
						
			// Aqui obtiene lista de BOs
			List<Recurso> listRecurso = Recurso.getListVigentes(new Date());
			
			// Seteo la lista de recursos
			reciboSearchPage.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				reciboSearchPage.getListRecurso().add(item.toVOWithCategoria());							
			}
			
			// Seteo del id para que sea nulo
			reciboSearchPage.getRecibo().getRecurso().setId(new Long(-1));
									
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return reciboSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ReciboSearchPage getReciboSearchPageResult(UserContext userContext, ReciboSearchPage reciboSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			reciboSearchPage.clearError();
			
			List<ReciboVO> listRecibosVO = new ArrayList<ReciboVO>();
			
			// Recibo de Deuda
			if (reciboSearchPage.getTipoRecibo().equals(TipoRecibo.RECIBODEUDA)){
				
				List<Recibo> listRecibo = GdeDAOFactory.getReciboDAO().getBySearchPage(reciboSearchPage);
				
				for (Recibo recibo: listRecibo){
					ReciboVO reciboVO = new ReciboVO();
					
					reciboVO.setId(recibo.getId());
					reciboVO.setCodRefPag(recibo.getCodRefPag());
					reciboVO.setNroRecibo(recibo.getNroRecibo());
					reciboVO.setAnioRecibo(recibo.getAnioRecibo());
					reciboVO.setFechaGeneracion(recibo.getFechaGeneracion());
					reciboVO.setFechaVencimiento(recibo.getFechaVencimiento());
					reciboVO.setTotImporteRecibo(recibo.getTotImporteRecibo());
					reciboVO.setFechaPago(recibo.getFechaPago());
					reciboVO.setObservacion(recibo.getObservacion());
										
					listRecibosVO.add(reciboVO);
				}
				
			} 
			
			// Recibo de Cuota de Convenio.
			if (reciboSearchPage.getTipoRecibo().equals(TipoRecibo.RECIBOCUOTA)){
				List<ReciboConvenio> listReciboConvenio = GdeDAOFactory.getReciboConvenioDAO().getBySearchPage(reciboSearchPage);
				
				for (ReciboConvenio reciboConvenio: listReciboConvenio){
					ReciboVO reciboVO = new ReciboVO();
					
					reciboVO.setId(reciboConvenio.getId());
					reciboVO.setCodRefPag(reciboConvenio.getCodRefPag());
					reciboVO.setNroRecibo(reciboConvenio.getNroRecibo());
					reciboVO.setAnioRecibo(reciboConvenio.getAnioRecibo());					
					reciboVO.setFechaGeneracion(reciboConvenio.getFechaGeneracion());
					reciboVO.setFechaVencimiento(reciboConvenio.getFechaVencimiento());
					reciboVO.setTotImporteRecibo(reciboConvenio.getTotImporteRecibo());
					reciboVO.setFechaPago(reciboConvenio.getFechaPago());
					
					listRecibosVO.add(reciboVO);
				}
			} 
			
			//Aqui pasamos BO a VO
	   		reciboSearchPage.setListResult(listRecibosVO);
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return reciboSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ReciboAdapter getReciboAdapterForView(UserContext userContext, CommonKey reciboKey, TipoRecibo tipoRecibo) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ReciboAdapter reciboAdapter = null;
			
			if (tipoRecibo.equals(TipoRecibo.RECIBODEUDA)){
				Recibo recibo = Recibo.getById(reciboKey.getId());
				reciboAdapter =  ReciboBeanHelper.getReciboDeuda(recibo.getCuenta(), recibo);				
			}
			
			if (tipoRecibo.equals(TipoRecibo.RECIBOCUOTA)){
				ReciboConvenio reciboConvenio = ReciboConvenio.getById(reciboKey.getId());
				reciboAdapter =  ReciboBeanHelper.getReciboCuota(reciboConvenio.getConvenio(), reciboConvenio);
			}
			
			// Seteamos el tipo de recibo seleccionado en el SearchPage
			reciboAdapter.setTipoRecibo(tipoRecibo);
			
			log.debug(funcName + ": exit");
			return reciboAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	
	// ---> Consulta Codigo Referencia Pago
	public LiqCodRefPagSearchPage getLiqCodRefPagSearchPageInit(UserContext userContext) throws DemodaServiceException {
		String funcName=DemodaUtil.currentMethodName();
		
		if (log.isDebugEnabled())log.debug(funcName + ": enter");
		
		try{
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			LiqCodRefPagSearchPage liqCodRef = new LiqCodRefPagSearchPage();
			liqCodRef.setListTipoBoleta(TipoBoleta.getList());
			
			return liqCodRef;
			
		}catch (Exception exception){
			log.error("Service Error: ",  exception);
			throw new DemodaServiceException(exception);
		}finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public LiqCodRefPagSearchPage getLiqCodRefPagSearchPageResult(UserContext userContext, LiqCodRefPagSearchPage liqCodRefPagSearchPage)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		liqCodRefPagSearchPage.clearError();
		if (log.isDebugEnabled())log.debug(funcName + ": enter");
		
		try{
			
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
						
			if (liqCodRefPagSearchPage.getTipoBoleta()<1){
				liqCodRefPagSearchPage.addRecoverableError(GdeError.CONSULTA_CODREFPAG_TIPO);
				return liqCodRefPagSearchPage;
			}
			
			List listResult = new ArrayList();
			
			if (liqCodRefPagSearchPage.getTipoBoleta().longValue() == TipoBoleta.TIPOCUOTA.getId().longValue()){
				List<ConvenioCuota> listConvenioCuota = ConvenioCuota.getByCodRefPagImporte(liqCodRefPagSearchPage);
				
				for(ConvenioCuota convenioCuota: listConvenioCuota){
					Convenio convenio = convenioCuota.getConvenio();
					LiqCuotaVO cuotaVO = new LiqCuotaVO();
					cuotaVO.setNroConvenio(convenio.getNroConvenio().toString());
					cuotaVO.setNroCuenta(convenio.getCuenta().getNumeroCuenta());
					cuotaVO.setNroCuota(convenioCuota.getNumeroCuota().toString());
					cuotaVO.setImporteCuota(convenioCuota.getImporteCuota());
					cuotaVO.setCodPago(new Long(convenioCuota.getCodRefPag()));
					cuotaVO.setDesRecurso(convenio.getRecurso().getDesRecurso());
					
					listResult.add(cuotaVO);
					
				}
				
			}else if (liqCodRefPagSearchPage.getTipoBoleta().longValue() == TipoBoleta.TIPODEUDA.getId().longValue()){
				List<Deuda> listDeuda = Deuda.getByCodRefPagImporte(liqCodRefPagSearchPage);
				for(Deuda deuda:listDeuda){
					
					LiqDeudaVO liqDeuda = new LiqDeudaVO();
					liqDeuda.setDesEstado(deuda.getEstadoDeuda().getDesEstadoDeuda());
					liqDeuda.setDesViaDeuda(deuda.getViaDeuda().getDesViaDeuda());
					liqDeuda.setPeriodoDeuda(deuda.getStrPeriodo());
					liqDeuda.setCodRefPag(deuda.getCodRefPag().toString());
					liqDeuda.setNroCuenta(deuda.getCuenta().getNumeroCuenta());
					liqDeuda.setSaldo(deuda.getSaldo());
					liqDeuda.setDesRecurso(deuda.getRecurso().getDesRecurso());
					listResult.add(liqDeuda);
				}
			}else if (liqCodRefPagSearchPage.getTipoBoleta().longValue() == TipoBoleta.TIPORECIBO.getId().longValue()){
				List<Recibo> listRecibo = Recibo.getByCodRefPagImporte(liqCodRefPagSearchPage);
				
				for(Recibo recibo:listRecibo){
					
					LiqReciboVO liqRecibo = new LiqReciboVO();
					liqRecibo.setNumeroRecibo(recibo.getNroRecibo());
					liqRecibo.getRecurso().setDesRecurso(recibo.getRecurso().getDesRecurso());
					liqRecibo.setTotal(recibo.getTotImporteRecibo());
					listResult.add(liqRecibo);
				}
			}else if (liqCodRefPagSearchPage.getTipoBoleta().longValue() == TipoBoleta.TIPORECIBOCUOTA.getId().longValue()){
				List<ReciboConvenio> listReciboConv = ReciboConvenio.getByCodRefPagImporte(liqCodRefPagSearchPage);
				for (ReciboConvenio reciboConv:listReciboConv){
					LiqReciboVO liqRecibo = new LiqReciboVO();
					liqRecibo.setNumeroRecibo(reciboConv.getNroRecibo());
					liqRecibo.getRecurso().setDesRecurso(reciboConv.getConvenio().getRecurso().getDesRecurso());
					liqRecibo.setTotal(reciboConv.getTotImporteRecibo());
					liqRecibo.getConvenio().setNroConvenio(reciboConv.getConvenio().getNroConvenio().toString());
					listResult.add(liqRecibo);
				}
			}
			
			liqCodRefPagSearchPage.setListResult(listResult);
			
			return liqCodRefPagSearchPage;
		
		}catch (Exception exception){
			log.error("Service Error: ",  exception);
			throw new DemodaServiceException(exception);
		}finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- Consulta Codigo Referencia Pago
	
	
	// ---> Administrar Declaracion Jurada
	public DecJurSearchPage getDecJurSearchPageInit(UserContext userContext, DecJurSearchPage decJurSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			decJurSearchPage.setListResult(new ArrayList());
			
			//if (decJurSearchPage.getDecJur().getIdDeuda() != null){
			if (decJurSearchPage.getViewResult() == true || decJurSearchPage.getDecJur().getIdDeuda() != null){
				
				Deuda deuda = Deuda.getById(decJurSearchPage.getDecJur().getIdDeuda());
				
				if (decJurSearchPage.getDecJur().getIdDeuda() != null)
				 decJurSearchPage.setPageNumber(1L);
					
				List<DecJur> listDecJur = GdeDAOFactory.getDecJurDAO().getListBySearchPage(decJurSearchPage);
				
				if (deuda != null){
					Cuenta cuenta = deuda.getCuenta();
					
					// Recurso
					decJurSearchPage.getDecJur().setRecurso(cuenta.getRecurso().toVOWithCategoria());
					// Cuenta
					decJurSearchPage.getDecJur().setCuenta(cuenta.toVOWithRecurso());
					// Periodo
					decJurSearchPage.getDecJur().setPeriodo(deuda.getPeriodo().intValue());
					// Anio
					decJurSearchPage.getDecJur().setAnio(deuda.getAnio().intValue());
				
				//decJurSearchPage.setListResult(ListUtilBean.toVO(listDecJur, 2));
				}
				for (DecJur decJur:listDecJur){
					DecJurVO decJurVO = (DecJurVO)decJur.toVO(2);
					
					log.debug(funcName + " esActivo: " + decJurVO.getEstado().getEsActivo());
					
					if (decJurVO.getEstado().getEsActivo())
						decJurVO.setModificarBussEnabled(false);
					
					if (!decJurVO.getEstado().getEsCreado())
						decJurVO.setEliminarBussEnabled(false);
					
					UsuarioSiat usuarioActual = UsuarioSiat.getByUserName(userContext.getUserName());
					UsuarioSiat usuarioAltaDecJur = UsuarioSiat.getByUserName(decJur.getUsuario());
					if(usuarioActual != null && usuarioAltaDecJur != null && usuarioActual.getArea() != null && usuarioAltaDecJur.getArea() != null){
						if(usuarioActual.getArea().getId().longValue() == usuarioAltaDecJur.getArea().getId().longValue() 
								&& decJurVO.getEstado().getId().longValue() != Estado.ANULADO.getId().longValue()){
							decJurVO.setVueltaAtrasBussEnabled(true);
						}
					}
					
					decJurSearchPage.getListResult().add(decJurVO);
				}
				
			} else {
				//Limpio la lista de recursos
				decJurSearchPage.setListRecurso(new ArrayList<RecursoVO>());
				
				// Aqui obtiene lista de BOs
				List<Recurso> listRecurso = Recurso.getListAutoliquidablesVigentes(new Date());
				
				// Seteo la lista de recursos
				decJurSearchPage.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
				for (Recurso item: listRecurso){				
					decJurSearchPage.getListRecurso().add(item.toVOWithCategoria());							
				}
				
				// Seteo del id para que no sea nulo.
				if (decJurSearchPage.getDecJur().getRecurso().getId() == null)
					decJurSearchPage.getDecJur().getRecurso().setId(new Long(-1));
			
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return decJurSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DecJurSearchPage getDecJurSearchPageResult(UserContext userContext, DecJurSearchPage decJurSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			decJurSearchPage.setListResult(new ArrayList());
			
			Integer periodoDesde=decJurSearchPage.getPeriodoDesde();
			Integer anioDesde=decJurSearchPage.getAnioDesde();
			Integer periodoHasta=decJurSearchPage.getPeriodoHasta();
			Integer anioHasta = decJurSearchPage.getAnioHasta();
			
			Long idRecurso=decJurSearchPage.getDecJur().getRecurso().getId();
			
			//Verifico que se haya seleccionado un recurso
			if (idRecurso==null || idRecurso < 1L){
				decJurSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DECJURSEARCHPAGE_RECURSO_LABEL);
			}
			
			//Verifico que se haya ingresado un numero de cuenta
			if (StringUtil.isNullOrEmpty(decJurSearchPage.getDecJur().getCuenta().getNumeroCuenta())){
				decJurSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DECURSEARCHPAGE_CUENTA_LABEL);
			}
			
			//Verifico que periodo desde sea valido si fue ingresado
			if (periodoDesde !=null || anioDesde !=null)
			if ((periodoDesde!=null && anioDesde == null)|| 
					(anioDesde!=null && periodoDesde==null)||
					(periodoDesde<1 || periodoDesde > 12)){
				decJurSearchPage.addRecoverableError(GdeError.DECJURSEARCHPAGE_PERIODODESDE_INVALIDO);
			}
			
			//Verifico que periodo hasta sea valido si fue ingresado
			if (periodoHasta !=null || anioHasta !=null)
			if ((periodoHasta!=null && anioHasta == null)|| 
					(anioHasta!=null && periodoHasta==null)||
					(periodoHasta<1 || periodoHasta > 12)){
				decJurSearchPage.addRecoverableError(GdeError.DECJURSEARCHPAGE_PERIODOHASTA_INVALIDO);
			}
			
			Cuenta cuenta=null;
			
			if (!decJurSearchPage.hasErrorRecoverable()){
				cuenta = Cuenta.getByIdRecursoYNumeroCuenta(idRecurso, decJurSearchPage.getDecJur().getCuenta().getNumeroCuenta());
				if (cuenta ==null){
					decJurSearchPage.addRecoverableError(GdeError.DECJURSEARCHPAGE_CUENTA_NOEXISTE);
				}
			}
			
			//Si hubo errores salgo
			if (decJurSearchPage.hasErrorRecoverable()){
				return decJurSearchPage;
			}
			
			
			decJurSearchPage.getDecJur().setCuenta(cuenta.toVOForView());
			
			decJurSearchPage.getDecJur().getCuenta().setId(cuenta.getId());
			
			List<DecJur> listDecJur = GdeDAOFactory.getDecJurDAO().getListBySearchPage(decJurSearchPage);
			
			for (DecJur decJur:listDecJur){
				// TOVO 1 para que no ingrese dentro de del recurso y luego hago tipDecJur
				DecJurVO decJurVO = (DecJurVO)decJur.toVO(1);
				decJurVO.getTipDecJurRec().setTipDecJur((TipDecJurVO)decJur.getTipDecJurRec().getTipDecJur().toVO(0));
				
				log.debug(funcName + " esActivo: " + decJurVO.getEstado().getEsActivo());
				
				if (decJurVO.getEstado().getEsActivo())
					decJurVO.setModificarBussEnabled(false);
				
				if (!decJurVO.getEstado().getEsCreado())
					decJurVO.setEliminarBussEnabled(false);
				
				UsuarioSiat usuarioActual = UsuarioSiat.getByUserName(userContext.getUserName());
				UsuarioSiat usuarioAltaDecJur = UsuarioSiat.getByUserName(decJur.getUsuario());
				if(usuarioActual != null && usuarioAltaDecJur != null && usuarioActual.getArea() != null && usuarioAltaDecJur.getArea() != null){
					if(usuarioActual.getArea().getId().longValue() == usuarioAltaDecJur.getArea().getId().longValue() 
							&& decJurVO.getEstado().getId().longValue() != Estado.ANULADO.getId().longValue()){
						decJurVO.setVueltaAtrasBussEnabled(true);
					}
				}
				
				decJurSearchPage.getListResult().add(decJurVO);
			}
			
									
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			
			return decJurSearchPage;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/*public DecJurAdapter getDecJurAdapterForCreate(UserContext userContext, Long idCuenta, Long idDeuda) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			DecJurAdapter decJurAdapter = new DecJurAdapter();

			Cuenta cuenta = Cuenta.getById(idCuenta);
			
			Recurso recurso = cuenta.getRecurso();
			
			DecJurVO decJurVO = new DecJurVO();
			
			decJurVO.setCuenta(cuenta.toVOForView());
			log.debug("idDeuda: "+idDeuda);
			if(idDeuda!=null){
				Deuda deuda = Deuda.getById(idDeuda);
				if (deuda!=null){
					decJurAdapter.setPeriodoDesde(deuda.getPeriodo().intValue());
					decJurAdapter.setAnioDesde(deuda.getAnio().intValue());
				}
			}
			
			if (recurso.getCodRecurso().equals(Recurso.COD_RECURSO_DReI)){
				decJurVO.setEsDrei(true);
			}
			
			if (cuenta.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_ETuR)){
				decJurVO.setEsEtur(true);
			}
			
			decJurAdapter.setDecJur(decJurVO);
			
			List<TipDecJurRec> listTipDecJurRec = TipDecJurRec.getListVigenteByRecurso(new Date(), cuenta.getRecurso());
			
			 RecursoDefinition recursoDefinition = recurso.getDefinitionRecAtrValValue(Atributo.getByCodigo(Atributo.COD_RAD_RED_TRIB).getId()); // del atributo radio
		        GenericAtrDefinition genericAtrDefinition = recursoDefinition.getListGenericAtrDefinition().get(0);
		        
		        genericAtrDefinition.getAtributo().getDomAtr().getListDomAtrVal().add
		        	(0,new DomAtrValVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR) );
			decJurAdapter.setGenericAtrDefinition(genericAtrDefinition);
			// Seteo la lista de tipos de declaraciones
			decJurAdapter.getListTipDecJurRec().add(new TipDecJurRecVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			decJurAdapter.getListTipDecJurRec().addAll(ListUtilBean.toVO(listTipDecJurRec, 2));
						
									
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			
			return decJurAdapter;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}*/

	public DecJurAdapter getDecJurAdapterForView(UserContext userContext, Long idDecJur) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			DecJurAdapter decJurAdapter = new DecJurAdapter();

			DecJur decJur = DecJur.getById(idDecJur);
						
			DecJurVO decJurVO = (DecJurVO) decJur.toVO(2);
			
			for (DecJurDetVO decJurDetVO : decJurVO.getListDecJurDet()){
				if (decJurDetVO.getSubtotal1()!=null && decJurDetVO.getSubtotal1().doubleValue()== decJurDetVO.getTotalConcepto().doubleValue()){
					decJurDetVO.setBaseAplicadaView(decJurDetVO.getBaseView());
					decJurDetVO.setMultiploAplicadoView(new RecAliVO(decJurDetVO.getMultiplo()).getAlicuotaView());
				}
				
				if (decJurDetVO.getSubtotal2()!=null && decJurDetVO.getSubtotal2()!=0D && decJurDetVO.getSubtotal2().doubleValue()== decJurDetVO.getTotalConcepto().doubleValue()){
					decJurDetVO.setBaseAplicadaView(decJurDetVO.getTipoUnidad().getCodYDescripcion());
					decJurDetVO.setMultiploAplicadoView(decJurDetVO.getCanUni().toString());
				}
			}
			
			/*
			if(decJur.getTotalPublicidad()==null)decJurVO.setEditaAdicionales(true);
			
			Date fecVigCon = DateUtil.getDate("01/"+ StringUtil.completarCerosIzq(decJur.getPeriodo().toString(),2)+"/"+decJur.getAnio(),DateUtil.ddSMMSYYYY_MASK);
			
			List<RecAli> listAliPub = DefDAOFactory.getRecAliDAO().getListVigenteByIdRecursoYCodigo(recurso.getId(), fecVigCon,RecTipAli.COD_PUBLICIDAD);
			
			decJurAdapter.getListAliPub().add(new RecAliVO(-1, StringUtil.SELECT_OPCION_NINGUNO));
			decJurAdapter.getListAliPub().addAll((List<RecAliVO>) ListUtilBean.toVO(listAliPub));
			
			List<RecAli> listAliMyS = DefDAOFactory.getRecAliDAO().getListVigenteByIdRecursoYCodigo(recurso.getId(), fecVigCon,RecTipAli.COD_MESAS_Y_SILLAS);
			
			decJurAdapter.getListAliMesYSil().add(new RecAliVO(-1, StringUtil.SELECT_OPCION_NINGUNO));
			decJurAdapter.getListAliMesYSil().addAll((List<RecAliVO>) ListUtilBean.toVO(listAliMyS));
			*/
			
			Recurso recurso = decJur.getRecurso();
			
			if (recurso.getCodRecurso().equals(Recurso.COD_RECURSO_DReI)){
				decJurVO.setEsDrei(true);
				decJurVO.setDeclaraAdicionales(true);
				decJurVO.setDeclaraPorCantidad(true);
			}
			
			if (recurso.getCodRecurso().equals(Recurso.COD_RECURSO_ETuR)){
				decJurVO.setEsEtur(true);
			}
			
//			//Seteo para saber si el adapter tiene que mostrar los totales de la declaracion o debe esperar la carga de adicionales
//			if (!decJurVO.isDeclaraAdicionales())
			decJurVO.setMostrarTotales(true);
//			
//			if(decJurVO.isDeclaraAdicionales() && decJurVO.getAliPub()!=null){
//				decJurVO.setMostrarTotales(true);
//			}
			if(decJur.getOriDecJur().getId().longValue() == OriDecJur.ID_ENVIO_OSIRIS.longValue()){
				decJurVO.setMostrarDatosAfip(true);
			}
			
			decJurAdapter.setDecJur(decJurVO);
									
									
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			
			return decJurAdapter;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public DecJurAdapter getDecJurAdapterForUpdate(UserContext userContext, Long idDecJur) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			DecJurAdapter decJurAdapter = new DecJurAdapter();

			DecJur decJur = DecJur.getById(idDecJur);
			
			Recurso recurso = decJur.getRecurso();
			
			DecJurVO decJurVO = (DecJurVO) decJur.toVO(1);
			decJurVO.setListDecJurPag((List<DecJurPagVO>)ListUtilBean.toVO(decJur.getListDecJurPag(),1));
			decJurVO.getTipDecJurRec().setTipDecJur((TipDecJurVO)decJur.getTipDecJurRec().getTipDecJur().toVO(0));
			decJurVO.setListDecJurDet(ListUtilBean.toVO(decJur.getListDecJurDet(),1));
			
			//if(decJur.getTotalPublicidad()==null)decJurVO.setEditaAdicionales(true);
			
			Date fecVigCon = DateUtil.getDate("01/"+ StringUtil.completarCerosIzq(decJur.getPeriodo().toString(),2)+"/"+decJur.getAnio(),DateUtil.ddSMMSYYYY_MASK);
			
			List<RecAli> listAliPub = DefDAOFactory.getRecAliDAO().getListVigenteByIdRecursoYCodigo(recurso.getId(), fecVigCon,RecTipAli.COD_PUBLICIDAD);
			
			decJurAdapter.setListAliPub(ListUtilBean.toVO(listAliPub, new RecAliVO(-1, StringUtil.SELECT_OPCION_NINGUNA)));
			
			
			
			
			List<RecAli> listAliMyS = DefDAOFactory.getRecAliDAO().getListVigenteByIdRecursoYCodigo(recurso.getId(), fecVigCon,RecTipAli.COD_MESAS_Y_SILLAS);
			
			decJurAdapter.setListAliMesYSil(ListUtilBean.toVO(listAliMyS, new RecAliVO(-1, StringUtil.SELECT_OPCION_NINGUNA)));
			
			
			if (recurso.getCodRecurso().equals(Recurso.COD_RECURSO_DReI)){
				decJurVO.setEsDrei(true);
				decJurVO.setDeclaraAdicionales(true);
				decJurVO.setDeclaraPorCantidad(true);
				decJurVO.setDeclaraOtrosPagos(true);
			}
			
			if (recurso.getCodRecurso().equals(Recurso.COD_RECURSO_ETuR)){
				decJurVO.setEsEtur(true);
				decJurVO.setDeclaraAdicionales(true);
				decJurVO.setDeclaraOtrosPagos(true);
			}
			
			if (recurso.getCodRecurso().equals(Recurso.COD_RECURSO_DPUB)){
				decJurVO.setMostrarTotales(true);
			}
			
			//Seteo para saber si el adapter tiene que mostrar los totales de la declaracion o debe esperar la carga de adicionales
			if (!decJurVO.isDeclaraAdicionales())
				decJurVO.setMostrarTotales(true);
			
			if(decJurVO.isDeclaraAdicionales() && decJurVO.getAliPub()!=null){
				decJurVO.setMostrarTotales(true);
			}
			
			decJurAdapter.setDecJur(decJurVO);
			decJurAdapter.getDecJur().setMostrarTotales(true);
									
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			
			for (RecAliVO recAliVO  : decJurAdapter.getListAliPub()){
				log.debug("ALIPUBVIEW: "+recAliVO.getAlicuotaView());
			}
			
			return decJurAdapter;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/*public DecJurAdapter createDecJur(UserContext userContext, DecJurAdapter decJurAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session=null;
		Transaction tx=null;
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session=SiatHibernateUtil.currentSession();
			tx=session.beginTransaction();
			
			//Validar tipo de DDJJ
			TipDecJurRec tipDecJurRec = TipDecJurRec.getByIdNull(decJurAdapter.getDecJur().getTipDecJurRec().getId());
			
			if (tipDecJurRec == null){
				decJurAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,GdeError.DECJURADAPTER_TIPO_LABEL);
			}
			
			// Cantidad personal o radio
			if (decJurAdapter.getDecJur().getValRefMin()==null){
				if (decJurAdapter.getDecJur().isEsDrei()){
					decJurAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,GdeError.DECJURADAPTER_CANPER_LABEL);
				}else if (decJurAdapter.getDecJur().isEsEtur()){
					decJurAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,GdeError.DECJURADAPTER_RADIO_LABEL);
				}
			}
			
			// Validamos periodo/anio o rango de periodo segun usuario logueado
			Integer periodo = null;
			Integer anio = null;
			Integer periodoHasta = null;
			Integer anioHasta = null;
			
			if (userContext.getEsUsuarioCMD()){
				
				// Seteo de valores para
				decJurAdapter.getDecJur().setPeriodo(decJurAdapter.getPeriodoDesde());
				decJurAdapter.getDecJur().setAnio(decJurAdapter.getAnioDesde());
				
				periodo = decJurAdapter.getPeriodoDesde();
				anio = decJurAdapter.getAnioDesde();
				periodoHasta=decJurAdapter.getPeriodoHasta();
				anioHasta = decJurAdapter.getAnioHasta();
				
				log.debug(funcName + " pd: " + periodo +
									" ad: " + anio +
									" ph: " + periodoHasta + 
									" ah: " + anioHasta);
				
				//Verifico que periodo desde sea valido si fue ingresado
				if (periodo !=null || anio !=null){
					if ((periodo!=null && anio == null)|| 
							(anio!=null && periodo==null)||
							(periodo<1 || periodo > 12)){
						decJurAdapter.addRecoverableError(GdeError.DECJURSEARCHPAGE_PERIODODESDE_INVALIDO);
					}
				}
				
				//Verifico que periodo hasta sea valido si fue ingresado
				if (periodoHasta !=null || anioHasta !=null){
					if ((periodoHasta!=null && anioHasta == null)|| 
							(anioHasta!=null && periodoHasta==null)||
							(periodoHasta<1 || periodoHasta > 12)){
						decJurAdapter.addRecoverableError(GdeError.DECJURSEARCHPAGE_PERIODOHASTA_INVALIDO);
					}
				}
				
				// Perdiodo desde no puede estar vacio
				if (periodo ==null || anio ==null)
					decJurAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DECJURADAPTER_PERIODODESDE_LABEL);
				
				// Periodo hasta no puede ser menor a desde
				if (periodo !=null && anio !=null && periodoHasta !=null && anioHasta !=null){
					
					log.debug(funcName + " anio: " + (anio.intValue() == anioHasta.intValue()));
					log.debug(funcName + " per: " + (periodo.intValue() > periodoHasta.intValue()));
					
					if (anioHasta < anio){
						decJurAdapter.addRecoverableError(GdeError.DECJURSEARCHPAGE_PERIODOHASTA_INVALIDO);
					} else if (anio.intValue() == anioHasta.intValue() && periodo.intValue() > periodoHasta.intValue()){
						decJurAdapter.addRecoverableError(GdeError.DECJURSEARCHPAGE_PERIODOHASTA_INVALIDO);
					}
				} 
				
			} else {
			
				periodo = decJurAdapter.getDecJur().getPeriodo();
				anio = decJurAdapter.getDecJur().getAnio();
				
				boolean ingresoPeriodo=true;
				if (periodo == null && anio == null){
					ingresoPeriodo=false;
					decJurAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,GdeError.DECJURADAPTER_PERIODO_LABEL);
				}
	
				if (ingresoPeriodo && (periodo ==null || anio == null || periodo < 1 || periodo > 12 || anio < 1)){
					decJurAdapter.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO,GdeError.DECJURADAPTER_PERIODO_LABEL);
				}
			}
			
			// Fin validacion requeridos y rangos
			if (decJurAdapter.hasErrorRecoverable()){
				return decJurAdapter;
			}
			
			Cuenta cuenta = Cuenta.getById(decJurAdapter.getDecJur().getCuenta().getId());
			
			Deuda deuda=null;
			// Buscamos deuda para el primer periodo ingresado
			if (!decJurAdapter.hasErrorRecoverable()){
				deuda = DeudaAdmin.getByCuentaPeriodoAnioParaDJ(cuenta, periodo.longValue(), anio);
				if (deuda == null){
					decJurAdapter.addRecoverableError(GdeError.DECJURADAPTER_DEUDA_NOENCONTRADA);
				} else {
					// Validamos que no exista un declaracion original si se intenta crear otra original.
					if (tipDecJurRec !=null && tipDecJurRec.getId().longValue() == TipDecJurRec.ID_DJ_ORIGINAL){
						List<DecJur>listDecJurAnt = DecJur.getListByDeuda(deuda);
						if (listDecJurAnt.size() > 0){
							decJurAdapter.addRecoverableError(GdeError.DECJURADAPTER_EXISTEN_DECLARACIONES);
						}
					}
				}
			}
			
			log.debug(funcName + " esCMD: " + userContext.getEsUsuarioCMD());
			log.debug(funcName + " hasErr: " + decJurAdapter.hasError());
			
			// Si es usuario CMD e ingreso rango de periodos, comprobamos que exista deuda para cada periodo.
			if (userContext.getEsUsuarioCMD() && periodoHasta != null && anioHasta != null 
					&& !decJurAdapter.hasError()){
				
				// Confirmamos que el usuario ingreso un rango de periodos
				decJurAdapter.setEsRangoPeriodo(true);
				
				boolean continuar = true; 
				
				// Comienza con los valores correspondiente a periodo desde
				Integer periodoAnalisis = periodo;
				Integer anioAnalisis = anio; 
				Deuda deudaChk = null;
				
				while(continuar){
					
					log.debug(funcName + " periodoAnalisis: " + periodoAnalisis + " anioAnalisis: " + anioAnalisis );
					log.debug(funcName + " periodoHasta: " + periodoHasta + " anioHasta: " + anioHasta );
					
					deudaChk = DeudaAdmin.getByCuentaPeriodoAnioParaDJ(cuenta, periodoAnalisis.longValue(), anioAnalisis);
					
					// Validamos que exista deuda para el periodo
					if (deudaChk == null){
						decJurAdapter.addRecoverableValueError("No se encontr\u00F3 Deuda para el Per\u00EDodo " + periodoAnalisis + 
								" y A\u00F1o " + anioAnalisis);
						log.debug(funcName + " sale por no encontrar deuda");
						break;
					}
					
					// Validamos que no exista declaracion original para el periodo
					if (tipDecJurRec !=null && tipDecJurRec.getId().longValue() == TipDecJurRec.ID_DJ_ORIGINAL){
						List<DecJur>listDecJurAnt = DecJur.getListByDeuda(deudaChk);
						if (listDecJurAnt.size() > 0){
							decJurAdapter.addRecoverableValueError("Tipo de Declaraci\u00F3n Incorrecto, ya existen declaraciones para el Per\u00EDodo " + 
									periodoAnalisis + " y A\u00F1o " + anioAnalisis);
							log.debug(funcName + " sale por existe dec jur para periodo");
							break;
						}
					}
					
					if (periodoAnalisis.intValue() == periodoHasta.intValue() && 
							anioAnalisis.intValue() == anioHasta.intValue()){
						log.debug(funcName + " sale por continuar = false");
						continuar = false;
					}

					if (periodoAnalisis.intValue() <= 12){
						periodoAnalisis ++;
					} 

					if (periodoAnalisis.intValue() > 12 && anioAnalisis.intValue() < anioHasta.intValue()){
						periodoAnalisis = 1;
						anioAnalisis ++;
					}
				}
			}
			
			log.debug(funcName + " hasErr: " + decJurAdapter.hasError());
			
			if (decJurAdapter.hasError()){
				return decJurAdapter;
			}
			
			// Creamos la declaracion jurada y sus detalles 
			DecJur decJur = new DecJur();
			decJur.setCuenta(cuenta);
			decJur.setFechaPresentacion(new Date());
			decJur.setFechaNovedad(new Date());
			decJur.setRecurso(cuenta.getRecurso());
			decJur.setTipDecJurRec(tipDecJurRec);
			decJur.setOriDecJur(OriDecJur.getById(OriDecJur.ID_CMD));
			decJur.setPeriodo(periodo);
			decJur.setAnio(anio);
			decJur.setIdDeuda(deuda.getId());
			decJur.setValRefMin(decJurAdapter.getDecJur().getValRefMin());
			decJur.setSubtotal(0D);
			decJur.setTotalDeclarado(0D);
			decJur.setOtrosPagos(0D);
			Date fecVig = DateUtil.getDate("01/"+ StringUtil.completarCerosIzq(decJur.getPeriodo().toString(),2)+"/"+decJur.getAnio(),DateUtil.ddSMMSYYYY_MASK);
			
			// Obtenemos el Valor de referencia minimo
			if (decJur.getValRefMin()!=null){
				RecMinDec recMinDec = RecMinDec.getVigente(cuenta.getRecurso(), decJur.getValRefMin(), fecVig);
				if (recMinDec == null)
					decJur.setMinRec(0D);
				else
					decJur.setMinRec(recMinDec.getMinimo());
			}
			
			decJur.setEstado(Estado.CREADO.getId());
			
			decJur.setListDecJurDet(new ArrayList<DecJurDet>());
			
			//LLamo a recalcular los valores que llama al update del DAO
			decJur.recalcularValores(true);
			
			DecJur lastDecJur=null;
			
			boolean decDreiExiste=false;
			
			if (decJur.getRecurso().equals(Recurso.getETur())){
				Cuenta cuentaDrei = Cuenta.getByIdRecursoYNumeroCuenta(Recurso.getDReI().getId(), cuenta.getNumeroCuenta());
				lastDecJur = GdeDAOFactory.getDecJurDAO().getLastByCuentaPeriodoYAnio(cuentaDrei, decJur.getPeriodo(),decJur.getAnio());
				log.debug("Recupera la decjur de Drei: "+lastDecJur.getId());
				if (lastDecJur!=null)
					decDreiExiste=true;
				else
					lastDecJur = GdeDAOFactory.getDecJurDAO().getLastDecJurByCuenta(cuentaDrei);
				
			}else{
				lastDecJur = GdeDAOFactory.getDecJurDAO().getLastDecJurByCuenta(cuenta);
			}
				
			// Se crea el detalle (DecJurDet)
			if (lastDecJur != null){
				for (DecJurDet decJurDet : lastDecJur.getListDecJurDet()){
					DecJurDet decJurDetNuevo = new DecJurDet();
					decJurDetNuevo.setDecJur(decJur);
					RecConADec recConADec= decJurDet.getRecConADec();
					if (decJur.getRecurso().equals(Recurso.getETur())){
						if (decDreiExiste)
							decJurDetNuevo.setBase(decJurDet.getBase());
						else
							decJurDetNuevo.setBase(0D);
					}else{
						decJurDetNuevo.setBase(0D);
					}
						
					decJurDetNuevo.setRecConADec(recConADec);
					
					decJurDetNuevo.setSubtotal1(0D);
					decJurDetNuevo.setSubtotal2(0D);
					decJurDetNuevo.setDetalle(decJurDet.getRecConADec().getDesConcepto());
					decJurDetNuevo.setTotalConcepto(0D);
					GdeDAOFactory.getDecJurDetDAO().update(decJurDetNuevo);
					decJur.getListDecJurDet().add(decJurDetNuevo);
				}
				// LLamo al update del DAO
				GdeDAOFactory.getDecJurDAO().update(decJur);
			}
						
			if (decJur.hasError()){
				tx.rollback();
			} else {
				tx.commit();				
				DecJurVO decJurVO =(DecJurVO) decJur.toVO(2);
				decJurAdapter.setDecJur(decJurVO);
				decJurAdapter.getDecJur().setMostrarTotales(true);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			
			return decJurAdapter;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			if (tx!=null)tx.rollback();
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}*/

	public DecJurAdapter updateDecJurAdicionalesAdapter (UserContext userContext, DecJurAdapter decJurAdapter)throws DemodaServiceException{
		String funcName=DemodaUtil.currentMethodName();
		
		if(log.isDebugEnabled())log.debug(funcName + ": enter");
		Session session = null;
		Transaction tx = null;
		
		try{
			
			session = SiatHibernateUtil.currentSession();
			
			tx = session.beginTransaction();
			
			DecJur decJur = DecJur.getById(decJurAdapter.getDecJur().getId());
			
			
			decJur.setAliMesYSil(decJurAdapter.getDecJur().getAliMesYSil());
			decJur.setAliPub(decJurAdapter.getDecJur().getAliPub());
			
			decJur.recalcularValores(true);
			
			if(!decJur.hasError()){
				tx.commit();
			}else{
				tx.rollback();
			}
			
			return decJurAdapter;
			
			
		}catch (Exception exception){
			log.error("ServiceError en: ", exception);
			if (tx !=null)tx.rollback();
			throw new DemodaServiceException(exception); 
		}finally{
			SiatHibernateUtil.closeSession();
		}
		
	}
	
	public DecJurVO deleteDecJur(UserContext userContext, DecJurVO decJurVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			decJurVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			DecJur decJur = DecJur.getById(decJurVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			decJur = GdeGDeudaManager.getInstance().deleteDecJur(decJur);
			
			if (decJur.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			decJur.passErrorMessages(decJurVO);
            
            log.debug(funcName + ": exit");
            return decJurVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	/*public DecJurAdapter procesarDDJJ (UserContext userContext, DecJurAdapter decJurAdapter)throws DemodaServiceException{
		String funcName=DemodaUtil.currentMethodName();
		
		if(log.isDebugEnabled())log.debug(funcName + ": enter");
		Session session = null;
		Transaction tx = null;
		
		try{
			
			session = SiatHibernateUtil.currentSession();
			
			tx = session.beginTransaction();
			
			// Obtenemos la declaracion jurada creada.
			DecJur decJur = DecJur.getById(decJurAdapter.getDecJur().getId());
			
			// obtenemos el registro de deuda asociada.
			DeudaAdmin deuda = DeudaAdmin.getById(decJur.getIdDeuda());
			
			if (deuda == null){
				decJurAdapter.addRecoverableError(GdeError.DECJURADAPTER_DEUDA_NOENCONTRADA);
			}
			Double totalOtrosPagos=0D;
			
			if (decJurAdapter.getDecJur().getOtrosPagos()!=null)
				totalOtrosPagos= decJurAdapter.getDecJur().getOtrosPagos();
			
			if (decJurAdapter.getDecJur().getTotalDeclarado()-totalOtrosPagos < 0)
				decJurAdapter.addRecoverableError(GdeError.DECJURADAPTER_TOTAL_MENOR_A_CERO);
			
			if (decJurAdapter.hasError()){
				return decJurAdapter;
			}
			
			// Vuelvo a calcular los campos totales
			// y actualizamos la declaracion jurada.
			log.debug(funcName + " recalcularValores");
			decJur.recalcularValores(true);
			
			// Obtenemos la Clasificacion Ceuda Rectificativa para el Recurso de la Deuda.
			RecClaDeu clasifRectif = deuda.getRecClaDeuRectificativa();
			
			// Actulizada deuda o crea nuevo regitro de deuda segun otros pagos de la declaracion.
			decJur = this.procesarDeudaSegunOtrosPago(decJur, deuda, clasifRectif);
			
			//Seteo procesada la declaración jurada
			decJur.setEstado(Estado.ACTIVO.getId());
			
			GdeDAOFactory.getDecJurDAO().update(decJur);
			
			
			if(!decJur.hasError()){
				tx.commit();
				log.debug(funcName + ": tx.commit");
			}else{
				tx.rollback();
				log.debug(funcName + ": tx.rollback");
				decJur.passErrorMessages(decJurAdapter);				
			}
			
			log.debug(funcName + " EsRango: " + decJurAdapter.isEsRangoPeriodo());
			
			
			// Checkemos valores de periodoDesde, hasta
			if (decJurAdapter.isEsRangoPeriodo() && !decJurAdapter.hasError()){
				
				tx = session.beginTransaction();

				// Para cada periodo dentro del rango
				//		crear una declaracion jurada con sus detalles
				// 		replicar la logica de creacion de deuda rectificativa, o actualizar deuda segun corresponda.
								
				boolean continuar = true; 
				int cntDeuda = 1; // Contador de cantidad de declaracion jurada procesada, para comenzar a partir de la segunda.
				
				// Comienza con los valores correspondiente a periodo desde
				Integer periodoAnalisis = decJurAdapter.getPeriodoDesde();
				Integer anioAnalisis = decJurAdapter.getAnioDesde(); 
				Integer periodoHasta = decJurAdapter.getPeriodoHasta();
				Integer anioHasta = decJurAdapter.getAnioHasta();
				Cuenta cuenta = Cuenta.getById(decJurAdapter.getDecJur().getCuenta().getId());

				Deuda deudaChk = null;
					
				while(continuar){
					
					log.debug(funcName + " periodoAnalisis: " + periodoAnalisis + " anioAnalisis: " + anioAnalisis + " cntDeuda: " + cntDeuda);
					
					// A partir del segundo periodo, creamos declaraciones para cada uno.
					if (cntDeuda > 1){
						deudaChk = DeudaAdmin.getByCuentaPeriodoAnioParaDJ(cuenta, periodoAnalisis.longValue(), anioAnalisis);
						
						// Validamos que exista deuda para el periodo
						if (deudaChk == null){
							decJurAdapter.addRecoverableValueError("No se encontr\u00F3 Deuda para el Per\u00EDodo " + periodoAnalisis + 
									" y A\u00F1o " + anioAnalisis);
							break;
						}
						
						//  ******************************************
						// 		Creamos la declaracion jurada.
					    //  ******************************************
						DecJur nuevaDecJur = this.createDecJur4Periodo(decJur, deudaChk);
						
						// Controlamos error
						if (nuevaDecJur.hasError()){
							nuevaDecJur.passErrorMessages(decJurAdapter);
							break;
						}
						
						//  ******************************************
						// 		Procesamos la deuda segun otros pagos.
						//  ******************************************
						nuevaDecJur = this.procesarDeudaSegunOtrosPago(nuevaDecJur,(DeudaAdmin) deudaChk, clasifRectif);
						
						// Controlamos error
						if (nuevaDecJur.hasError()){
							nuevaDecJur.passErrorMessages(decJurAdapter);
							break;
						}
					}
						
					if (periodoAnalisis.intValue() == periodoHasta.intValue() && 
							anioAnalisis.intValue() == anioHasta.intValue()){
						continuar = false;
					}

					if (periodoAnalisis.intValue() <= 12){
						periodoAnalisis ++;
					} 

					if (periodoAnalisis.intValue() > 12 && anioAnalisis.intValue() < anioHasta.intValue()){
						periodoAnalisis = 1;
						anioAnalisis ++;
					}
				
					cntDeuda ++;
				}
				
				if(!decJurAdapter.hasError()){
					tx.commit();
					log.debug(funcName + ": tx.commit");
				}else{
					tx.rollback();
					log.debug(funcName + ": tx.rollback");
				}
				
			}
			
			return decJurAdapter;
			
		}catch (Exception exception){
			log.error("ServiceError en: ", exception);
			exception.printStackTrace();
			if (tx !=null)tx.rollback();
			throw new DemodaServiceException(exception); 
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}
	*/
	
	/**
	 * Crea una declaracion de deuda para el periodo de deuda recibido y 
	 * clonado la declaracion jurada recibida. 
	 * 
	 * @param decJurAnt
	 * @param deuda
	 * @return
	 * @throws DemodaServiceException
	 *//*
	private DecJur createDecJur4Periodo(DecJur decJurAnt, Deuda deuda) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
		
			DecJur decJurNueva = new DecJur();
			decJurNueva.setCuenta(decJurAnt.getCuenta());
			decJurNueva.setFechaPresentacion(new Date());
			decJurNueva.setFechaNovedad(new Date());
			decJurNueva.setRecurso(decJurAnt.getCuenta().getRecurso());
			decJurNueva.setTipDecJurRec(decJurAnt.getTipDecJurRec());
			decJurNueva.setOriDecJur(OriDecJur.getById(OriDecJur.ID_CMD));
			decJurNueva.setPeriodo(deuda.getPeriodo().intValue());
			decJurNueva.setAnio(deuda.getAnio().intValue());
			decJurNueva.setIdDeuda(deuda.getId());
			decJurNueva.setValRefMin(decJurAnt.getValRefMin());
			decJurNueva.setSubtotal(decJurAnt.getSubtotal());
			decJurNueva.setTotalDeclarado(decJurAnt.getTotalDeclarado());
			decJurNueva.setOtrosPagos(decJurAnt.getOtrosPagos());
			decJurNueva.setAliMesYSil(decJurAnt.getAliMesYSil());
			decJurNueva.setAliPub(decJurAnt.getAliPub());
			
			Date fecVig = DateUtil.getDate("01/"+ StringUtil.completarCerosIzq(deuda.getPeriodo().toString(),2)+"/"+ deuda.getAnio(),DateUtil.ddSMMSYYYY_MASK);
			
			// Valor de referencia minimo
			if (decJurAnt.getValRefMin()!=null){
				RecMinDec recMinDec = RecMinDec.getVigente(decJurAnt.getRecurso(),decJurAnt.getValRefMin(), fecVig);
				if (recMinDec == null)
					decJurNueva.setMinRec(0D);
				else
					decJurNueva.setMinRec(recMinDec.getMinimo());
			}
			
			decJurNueva.setEstado(Estado.ACTIVO.getId());
			
			// LLamo al update del DAO
			GdeDAOFactory.getDecJurDAO().update(decJurNueva);
			
			decJurNueva.setListDecJurDet(new ArrayList<DecJurDet>());
			decJurNueva.setListDecJurPag(new ArrayList<DecJurPag>());
			
			// Clonamos los detalles
			for (DecJurDet decJurDet : decJurAnt.getListDecJurDet()){
				
				DecJurDet decJurDetNuevo = new DecJurDet();
				decJurDetNuevo.setDecJur(decJurNueva);
				decJurDetNuevo.setRecConADec(decJurDet.getRecConADec());
				decJurDetNuevo.setBase(decJurDet.getBase());
				decJurDetNuevo.setSubtotal1(decJurDet.getSubtotal1());
				decJurDetNuevo.setSubtotal2(decJurDet.getSubtotal2());
				decJurDetNuevo.setDetalle(decJurDet.getDetalle());
				decJurDetNuevo.setTotalConcepto(decJurDet.getTotalConcepto());
				
				GdeDAOFactory.getDecJurDetDAO().update(decJurDetNuevo);
				
				decJurNueva.getListDecJurDet().add(decJurDetNuevo);
			}
			
			//Clonamos los Otros Pagos
			for (DecJurPag decJurPag : decJurAnt.getListDecJurPag()){
				
				DecJurPag decJurPagNuevo = new DecJurPag();
				decJurPagNuevo.setDecJur(decJurNueva);
				decJurPagNuevo.setTipPagDecJur(decJurPag.getTipPagDecJur());
				decJurPagNuevo.setDetalle(decJurPag.getDetalle());
				decJurPagNuevo.setImporte(decJurPag.getImporte());
				decJurPagNuevo.setSaldo(decJurPag.getSaldo());
				decJurPagNuevo.setCertificado(decJurPag.getCertificado());
				decJurPagNuevo.setCuitAgente(decJurPag.getCuitAgente());
				decJurPagNuevo.setFechaPago(decJurPag.getFechaPago());
				
				GdeDAOFactory.getDecJurPagDAO().update(decJurPagNuevo);
				
				decJurNueva.getListDecJurPag().add(decJurPagNuevo);
			}
			
			//LLamo a recalcular los valores que llama al update del DAO
			decJurNueva.recalcularValores(true);
			
			return decJurNueva;
			
		} catch(Exception e){
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		}
	}*/
	
	/**
	 * Procesa deuda, actualiza o crea nueva segun otros pagos
	 * 
	 * @param decJur
	 * @param deuda
	 * @param recClaDeu
	 * @return
	 * @throws DemodaServiceException
	 *//*
	private DecJur procesarDeudaSegunOtrosPago(DecJur decJur, DeudaAdmin deuda, RecClaDeu recClaDeu)throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
		
			// Si tine otros pagos. 
			if(!ListUtil.isNullOrEmpty(decJur.getListDecJurPag())){
				log.debug(funcName + " Si tine otros pagos");
				
				TipPagDecJur tipPagDecJur = TipPagDecJur.getById(TipPagDecJur.ID_DJMISMOPERIODO);
				
				// Obtenemos una lista de TipPagDecJur
				List<TipPagDecJur> listTipPagDecJur = new ArrayList<TipPagDecJur>();	
				for (DecJurPag djp:decJur.getListDecJurPag()){
					listTipPagDecJur.add(djp.getTipPagDecJur());
				}
				
				// Si tiene pago tipo 3
				if (ListUtil.isInList(listTipPagDecJur, tipPagDecJur)){
					log.debug(funcName + " Si tiene pago tipo 3");
					
					deuda = this.createDeuda4DecJur(deuda, recClaDeu, decJur);
					
					// Si NO tiene pago tipo 3	
				} else {
					log.debug(funcName + " NO tiene pago tipo 3");
					// Si la deuda esta Paga o en Convenio, creamos una nueva
					if (deuda.getFechaPago() != null || deuda.getIdConvenio() != null){
						
						deuda = this.createDeuda4DecJur(deuda, recClaDeu, decJur);
					}
					
					deuda = this.updateDeuda4DecJur(deuda, decJur);
				}
				
				// Si no tiene otros Pagos	
			} else {
				log.debug(funcName + " No tiene otros pagos");
				
				// Si la deuda esta Paga o en Convenio, creamos una nueva
				if (deuda.getFechaPago() != null || deuda.getIdConvenio() != null){
					
					deuda = this.createDeuda4DecJur(deuda, recClaDeu, decJur);
				}
				
				deuda = this.updateDeuda4DecJur(deuda, decJur);
				
			}
			
			deuda.passErrorMessages(decJur);
		
			return decJur;
			
		} catch(Exception e){
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		}
	}*/
	
	
	/**
	 * 
	 * 
	 * 
	 * @param deuda
	 * @param decJur
	 * @return
	 * @throws DemodaServiceException
	 *//*
	private DeudaAdmin updateDeuda4DecJur(DeudaAdmin deuda, DecJur decJur) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			
			//Seteo el importe de la deuda
			Double importeDeuda = decJur.getTotalDeclarado();
			deuda.setImporte(decJur.getTotalDeclarado());
			
			//Obtengo la lista de deuRecCon
			List<DeuAdmRecCon> listDeuRecCon = deuda.getListDeuRecCon();
			
			//Seteo el importe de los conceptos
			for (DeuAdmRecCon deuAdmRecCon : listDeuRecCon){
				Double porcentajeRecCon = deuAdmRecCon.getRecCon().getPorcentaje();
				Double importeDeuRecCon=NumberUtil.round(importeDeuda* porcentajeRecCon,SiatParam.DEC_IMPORTE_DB);
				deuAdmRecCon.setImporte(importeDeuRecCon);
				deuAdmRecCon.setImporteBruto(importeDeuRecCon);
				GdeDAOFactory.getDeuAdmRecConDAO().update(deuAdmRecCon);
			}
			
			//Armo el string de conceptos de la deuda
			deuda.setStrConceptosByListRecCon(listDeuRecCon);
			
			//Si la declaracion es rectificativa verifico si hubo pagos registrados de declaraciones y los borro
			if (decJur.getTipDecJurRec().getId().longValue()==TipDecJurRec.ID_DJ_RECTIFICATIVA){
				List<DecJur>listDecJur = DecJur.getListByDeudaDecJurExcluir(deuda, decJur);
				GdeDAOFactory.getPagoDeudaDAO().deletePagosDDJJ(listDecJur);
			}
			
			//Si hubo otros pagos los cargo en PagoDeuda
			Double restoAImporte=0D;
			if (decJur.getOtrosPagos()!=null && decJur.getOtrosPagos()!=0D){
				log.debug("LISTA PAGOS: "+decJur.getListDecJurPag().size());
				for (DecJurPag decJurPag : decJur.getListDecJurPag()){
					Long idTipPagDecJur = decJurPag.getTipPagDecJur().getId().longValue();
					log.debug("id tipo pago: "+idTipPagDecJur);
					if (idTipPagDecJur == TipPagDecJur.ID_RETENCION || 
							idTipPagDecJur==TipPagDecJur.ID_PERCEPCION){
						PagoDeuda pagoDeuda = new PagoDeuda();
						pagoDeuda.setFechaPago(decJurPag.getFechaPago());
						pagoDeuda.setIdDeuda(decJur.getIdDeuda());
						pagoDeuda.setIdPago(decJur.getId());
						pagoDeuda.setImporte(decJurPag.getImporte());
						pagoDeuda.setFechaPago(new Date());
						pagoDeuda.setActualizacion(0D);
						pagoDeuda.setEsPagoTotal(0);
						log.debug("creando pago deuda");
						if (idTipPagDecJur == TipPagDecJur.ID_RETENCION){
							pagoDeuda.setTipoPago(TipoPago.getById(TipoPago.ID_RETENCION_DECLARADA));
						}else if (idTipPagDecJur == TipPagDecJur.ID_PERCEPCION){
							pagoDeuda.setTipoPago(TipoPago.getById(TipoPago.ID_PERCEPCION_DECLARADA));
						}
						GdeDAOFactory.getPagoDeudaDAO().update(pagoDeuda);
					}
					restoAImporte+=decJurPag.getImporte();
				}
			}
			
			log.debug("IMPORTE DEUDA: "+deuda.getImporte() + " ,PAGOS: "+restoAImporte);
			//Seteo el saldo de la deuda
			deuda.setSaldo(deuda.getImporte()-restoAImporte);
			GdeDAOFactory.getDeudaDAO().update(deuda);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			
			return deuda;
			
		} catch(Exception e){
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		}
		
	}*/

	/**
	 * Crea registro de deuda con clasificacion recibida para los datos de periodo de la declaracion recibida.
	 * 
	 * @param deuda
	 * @param clasifRectif
	 * @param decJur
	 * @return
	 * @throws DemodaServiceException
	 *//*
	private DeudaAdmin createDeuda4DecJur(DeudaAdmin deuda, RecClaDeu clasifRectif, DecJur decJur) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
		
	        // Setea los datos en la deudaAdmin que se va a crear, con la lista de conceptos 
	        DeudaAdmin deudaAdmin = new DeudaAdmin();
	        deudaAdmin.setRecurso(deuda.getRecurso());
	        deudaAdmin.setEstadoDeuda(EstadoDeuda.getById(EstadoDeuda.ID_ADMINISTRATIVA));
	        deudaAdmin.setViaDeuda(ViaDeuda.getById(ViaDeuda.ID_VIA_ADMIN));
	        deudaAdmin.setFechaEmision(new Date());
	        deudaAdmin.setEstaImpresa(SiNo.NO.getId());
	        deudaAdmin.setSistema(deuda.getSistema());           
	        deudaAdmin.setCodRefPag(GdeDAOFactory.getDeudaDAO().getNextCodRefPago());
	        deudaAdmin.setAnio(deuda.getAnio());
	        deudaAdmin.setPeriodo(deuda.getPeriodo());
	        deudaAdmin.setCuenta(deuda.getCuenta());
	        deudaAdmin.setFechaVencimiento(deuda.getFechaVencimiento());
	        deudaAdmin.setRecClaDeu(clasifRectif);
	        deudaAdmin.setReclamada(SiNo.NO.getId());
	        deudaAdmin.setResto(1L);  // Se graba con resto distinto de cero para evitar problemas de Asentamiento de la Deuda Original migrada. (Fix Mantis 5077)
	        deudaAdmin.setEstado(Estado.ACTIVO.getId());
	        deudaAdmin.setAtrAseVal(deuda.getAtrAseVal());
	         
	        /*  importe = totalDeclarado - sumatoria de list de decJurPag.importe == 3
	          saldo = importe - sumatoria de list de decJurPag donde tipoPagDecJur != 3 */
	        /*
	        log.debug(funcName + " declarado: " + decJur.getTotalDeclarado());
	        log.debug(funcName + " sum tipo=3: " + decJur.getSumatoriaOtrosPagos(true));
	        log.debug(funcName + " sum tipo!=3: " + decJur.getSumatoriaOtrosPagos(false));
	        
	        Double importeDeuda = decJur.getTotalDeclarado() - decJur.getSumatoriaOtrosPagos(true);
	        
	        if(importeDeuda.doubleValue() < 0)
	        	importeDeuda = 0D;
	        	
	        deudaAdmin.setImporte(importeDeuda);
	        deudaAdmin.setImporteBruto(deudaAdmin.getImporte());
	        
	        Double saldoDeuda = deudaAdmin.getImporte() - decJur.getSumatoriaOtrosPagos(false);
	        
	        if(saldoDeuda.doubleValue() < 0 )
	        	saldoDeuda = 0D;	
	        
	        deudaAdmin.setSaldo(saldoDeuda);
	        
	        log.debug(funcName + " importeDedua: " + importeDeuda);
	        log.debug(funcName + " saldo: " + deudaAdmin.getSaldo());
	        
	        // Calcula el importe y setea los conceptos            
	        List<DeuAdmRecCon> listDeuAdmRecCon= new ArrayList<DeuAdmRecCon>();
	        deudaAdmin.setListDeuRecCon(listDeuAdmRecCon);
	        
	        for(DeuAdmRecCon deuAdmRecConActual: deuda.getListDeuRecCon()){
	        	DeuAdmRecCon deuAdmRecConNuevo = new DeuAdmRecCon();
	        	deuAdmRecConNuevo.setDeuda(deudaAdmin);
	        	deuAdmRecConNuevo.setRecCon(deuAdmRecConActual.getRecCon());
	        	
	        	Double porcentajeRecCon = deuAdmRecConActual.getRecCon().getPorcentaje();
				Double importeDeuRecCon=NumberUtil.round(importeDeuda * porcentajeRecCon, SiatParam.DEC_IMPORTE_DB);
				deuAdmRecConNuevo.setImporte(importeDeuRecCon);
				deuAdmRecConNuevo.setImporteBruto(importeDeuRecCon);
				deuAdmRecConNuevo.setSaldo(0D);
				
	            listDeuAdmRecCon.add(deuAdmRecConNuevo);
	        }
	        
	        deudaAdmin = GdeGDeudaManager.getInstance().createDeudaAdmin(deudaAdmin, listDeuAdmRecCon);
        
	        if (log.isDebugEnabled()) log.debug(funcName + ": exit");
	        
	        return deudaAdmin;
	        
		} catch(Exception e){
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		}
		
	}*/
	
	
	public DecJurDetAdapter getDecJurDetAdapterForCreate(UserContext userContext, Long idDecJur) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			DecJur decJur = DecJur.getById(idDecJur);
			
			DecJurDetAdapter decJurDetAdapter = new DecJurDetAdapter();
			
			decJurDetAdapter.getDecJurDet().setDecJur((DecJurVO)decJur.toVO(1));
			decJurDetAdapter.getDecJurDet().getDecJur().getTipDecJurRec().setTipDecJur((TipDecJurVO)(decJur.getTipDecJurRec().getTipDecJur().toVO()));
			
			Recurso recurso = decJur.getRecurso();
			
			
			if (recurso.equals(Recurso.getByCodigo(Recurso.COD_RECURSO_DReI))){
				decJurDetAdapter.getDecJurDet().getDecJur().setEsDrei(true);
				decJurDetAdapter.getDecJurDet().getDecJur().setDeclaraBaseImp(true);
				decJurDetAdapter.getDecJurDet().getDecJur().setDeclaraPorCantidad(true);
			}
			
			if (recurso.equals(Recurso.getByCodigo(Recurso.COD_RECURSO_ETuR))){
				decJurDetAdapter.getDecJurDet().getDecJur().setEsEtur(true);
				decJurDetAdapter.getDecJurDet().getDecJur().setDeclaraBaseImp(false);
				decJurDetAdapter.getDecJurDet().getDecJur().setDeclaraPorCantidad(false);
			}
			
			if (recurso.equals(Recurso.getByCodigo(Recurso.COD_RECURSO_DPUB))){
				decJurDetAdapter.getDecJurDet().getDecJur().setDeclaraBaseImp(false);
				decJurDetAdapter.getDecJurDet().getDecJur().setDeclaraPorCantidad(true);
			}
			Date fecVigCon = DateUtil.getDate("01/"+ StringUtil.completarCerosIzq(decJur.getPeriodo().toString(),2)+"/"+decJur.getAnio(),DateUtil.ddSMMSYYYY_MASK);
			
			List<RecConADec>listRecConADec;
			if (decJur.getCuenta().getRecurso().getEsPrincipal()==SiNo.NO.getId())
				listRecConADec= DefDAOFactory.getRecConADecDAO().getListVigenteByIdRecurso(decJur.getRecurso().getRecPri().getId(), fecVigCon,TipRecConADec.ID_CONCEPTOS);
			else
				listRecConADec= DefDAOFactory.getRecConADecDAO().getListVigenteByIdRecurso(recurso.getId(), fecVigCon,TipRecConADec.ID_CONCEPTOS);
			
			List<RecAli> listRecAli = DefDAOFactory.getRecAliDAO().getListVigenteByIdRecursoYCodigo(recurso.getId(), fecVigCon,RecTipAli.COD_OGI);
			
			List<RecTipUni> listRecTipUni = DefDAOFactory.getRecTipUniDAO().getListTipUniByRecurso(recurso);
			
			
			decJurDetAdapter.getListRecTipUni().add(new RecTipUniVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			decJurDetAdapter.getListRecTipUni().addAll(ListUtilBean.toVO(listRecTipUni));
		
			
			decJurDetAdapter.getListRecConADec().add(new RecConADecVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			decJurDetAdapter.getListRecConADec().addAll(ListUtilBean.toVO(listRecConADec));
			
			decJurDetAdapter.getListRecAli().add(new RecAliVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			decJurDetAdapter.getListRecAli().addAll(ListUtilBean.toVO(listRecAli));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			
			return decJurDetAdapter;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public DecJurDetAdapter getDecJurDetAdapterForView(UserContext userContext, CommonKey selectedId) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			DecJurDet decJurDet = DecJurDet.getById(selectedId.getId());
			
			DecJurDetAdapter decJurDetAdapter = new DecJurDetAdapter();
			
			decJurDetAdapter.setDecJurDet((DecJurDetVO)decJurDet.toVO(3));
			
			Recurso recurso = decJurDet.getDecJur().getRecurso();
			
			if (recurso.equals(Recurso.getByCodigo(Recurso.COD_RECURSO_DReI))){
				decJurDetAdapter.getDecJurDet().getDecJur().setEsDrei(true);
			}else if (recurso.equals(Recurso.getByCodigo(Recurso.COD_RECURSO_DReI))){
				decJurDetAdapter.getDecJurDet().getDecJur().setEsEtur(true);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			
			return decJurDetAdapter;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DecJurDetAdapter getDecJurDetAdapterForUpdate(UserContext userContext, CommonKey selectedId) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			Long idDecJurDet = selectedId.getId();

			DecJurDet decJurDet = DecJurDet.getById(idDecJurDet);
			
			DecJurDetAdapter decJurDetAdapter = new DecJurDetAdapter();
			
			decJurDetAdapter.setDecJurDet((DecJurDetVO)decJurDet.toVO(2,false));
			decJurDetAdapter.getDecJurDet().getDecJur().getTipDecJurRec().setTipDecJur((TipDecJurVO)(decJurDet.getDecJur().getTipDecJurRec().getTipDecJur().toVO()));
			
			DecJur decJur = decJurDet.getDecJur();
			
			Recurso recurso = decJur.getRecurso();
			
			if (recurso.equals(Recurso.getByCodigo(Recurso.COD_RECURSO_DReI))){
				decJurDetAdapter.getDecJurDet().getDecJur().setEsDrei(true);
				decJurDetAdapter.getDecJurDet().getDecJur().setDeclaraBaseImp(true);
				decJurDetAdapter.getDecJurDet().getDecJur().setDeclaraPorCantidad(true);
			}
			
			if (recurso.equals(Recurso.getByCodigo(Recurso.COD_RECURSO_ETuR))){
				decJurDetAdapter.getDecJurDet().getDecJur().setEsEtur(true);
				decJurDetAdapter.getDecJurDet().getDecJur().setDeclaraBaseImp(false);
				decJurDetAdapter.getDecJurDet().getDecJur().setDeclaraPorCantidad(false);
			}
			
			if (recurso.equals(Recurso.getByCodigo(Recurso.COD_RECURSO_DPUB))){
				decJurDetAdapter.getDecJurDet().getDecJur().setDeclaraBaseImp(false);
				decJurDetAdapter.getDecJurDet().getDecJur().setDeclaraPorCantidad(true);
			}
			
			List<RecTipUni> listRecTipUni = DefDAOFactory.getRecTipUniDAO().getListTipUniByRecurso(recurso);
			decJurDetAdapter.getListRecTipUni().add(new RecTipUniVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			decJurDetAdapter.getListRecTipUni().addAll(ListUtilBean.toVO(listRecTipUni));
			
			Date fecVigCon = DateUtil.getDate("01/"+ StringUtil.completarCerosIzq(decJur.getPeriodo().toString(),2)+"/"+decJur.getAnio(),DateUtil.ddSMMSYYYY_MASK);
			
			List<RecConADec>listRecConADec;
			
			if (decJur.getCuenta().getRecurso().getEsPrincipal().intValue()==SiNo.NO.getId().intValue()){
				listRecConADec= DefDAOFactory.getRecConADecDAO().getListVigenteByIdRecurso(decJur.getRecurso().getRecPri().getId(), fecVigCon,TipRecConADec.ID_CONCEPTOS);
			}else{
				listRecConADec= DefDAOFactory.getRecConADecDAO().getListVigenteByIdRecurso(recurso.getId(), fecVigCon,TipRecConADec.ID_CONCEPTOS);
			}
			List<RecAli> listRecAli = DefDAOFactory.getRecAliDAO().getListVigenteByIdRecursoYCodigo(recurso.getId(), fecVigCon,RecTipAli.COD_OGI);
			
			Long idUnidad=null;
			if(decJurDet.getRecTipUni()!=null)
				idUnidad = decJurDet.getRecTipUni().getId();
			
			if (idUnidad != null && decJurDetAdapter.getDecJurDet().getDecJur().isDeclaraPorCantidad()){
				List<RecConADec>listTipoUnidad = DefDAOFactory.getRecConADecDAO().getListTipoUnidadVigenteByRecursoParamUnidad(recurso, fecVigCon, idUnidad);
				
				decJurDetAdapter.setListTipoUnidad(ListUtilBean.toVO(listTipoUnidad,new RecConADecVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
			}
			
			if(decJur.getCuenta().getRecurso().equals(Recurso.getETur())){
				List<RecConADec>listTipoUnidad = DefDAOFactory.getRecConADecDAO().getListVigenteByIdRecurso(decJur.getRecurso().getId(), fecVigCon,TipRecConADec.ID_TIPO_ACTIVIDAD_ESPECIFICA_ETUR);
				decJurDetAdapter.setListTipoUnidad(ListUtilBean.toVO(listTipoUnidad,new RecConADecVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
			}
			
			decJurDetAdapter.getListRecConADec().add(new RecConADecVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			decJurDetAdapter.getListRecConADec().addAll(ListUtilBean.toVO(listRecConADec, 0));
			
			decJurDetAdapter.getListRecAli().add(new RecAliVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			decJurDetAdapter.getListRecAli().addAll(ListUtilBean.toVO(listRecAli, 1));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			
			return decJurDetAdapter;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
		
	public DecJurDetAdapter getDecJurDetParamUnidad(UserContext userContext, DecJurDetAdapter decJurDetAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			DecJurDetVO decJurDet = decJurDetAdapter.getDecJurDet();
			DecJurVO decJur = decJurDet.getDecJur();
			
			Recurso recurso = Recurso.getById(decJur.getRecurso().getId());
			
			Long idUnidad = decJurDetAdapter.getDecJurDet().getRecTipUni().getId();
			Date fecVigCon = DateUtil.getDate("01/"+ StringUtil.completarCerosIzq(decJur.getPeriodo().toString(),2)+"/"+decJur.getAnio(),DateUtil.ddSMMSYYYY_MASK);
			List<RecConADec>listTipoUnidad = DefDAOFactory.getRecConADecDAO().getListTipoUnidadVigenteByRecursoParamUnidad(recurso, fecVigCon, idUnidad);
			
			decJurDetAdapter.setListTipoUnidad(ListUtilBean.toVO(listTipoUnidad,new RecConADecVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			decJurDetAdapter.setIdFoco("tipUni");
			decJurDet.setValUnidad(null);
			decJurDet.setSubtotal2(null);
			decJurDetAdapter.setDecJurDet(decJurDet);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			
			return decJurDetAdapter;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public DecJurDetAdapter getDecJurDetParamTipUni(UserContext userContext, DecJurDetAdapter decJurDetAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			DecJurDetVO decJurDet = decJurDetAdapter.getDecJurDet();
			
			DecJur decJur = DecJur.getById(decJurDet.getDecJur().getId());
			
			Long idTipUni = decJurDetAdapter.getDecJurDet().getTipoUnidad().getId();
			
			log.debug("IDTIPUNI: "+idTipUni);
			//Si selecciono Seleccionar --> salgo
			if (idTipUni < 0){
				decJurDet.setValUnidad(null);
				decJurDet.setSubtotal2(null);
				decJurDetAdapter.setDecJurDet(decJurDet);
				return decJurDetAdapter;
			}
			Date fecVigCon = DateUtil.getDate("01/"+ StringUtil.completarCerosIzq(decJur.getPeriodo().toString(),2)+"/"+decJur.getAnio(),DateUtil.ddSMMSYYYY_MASK);
			
			RecConADec recConADec = RecConADec.getById(idTipUni);
			
			if (!decJur.getCuenta().getRecurso().equals(Recurso.getETur())){
				ValUnRecConADe valUnRecConADe = recConADec.getValUnRecConADeVigente(fecVigCon);
				
				if (valUnRecConADe != null){
					decJurDet.setValUnidad(valUnRecConADe.getValorUnitario());
					if (decJurDet.getCanUni()!=null){
						decJurDet.setSubtotal2(decJurDet.getCanUni()*decJurDet.getValUnidad());
					}else{
						decJurDet.setSubtotal2(0D);
					}
				}
				decJurDetAdapter.setDecJurDet(decJurDet);
				decJurDetAdapter.setIdFoco("tipUni");
			}else{
				ValUnRecConADe valUnRecConADe = recConADec.getVigenteByFechaYValRef(fecVigCon, decJur.getValRefMin());
				
				if (valUnRecConADe != null){
					if (valUnRecConADe.getRecAli()!=null)
						decJurDet.setMultiplo(valUnRecConADe.getRecAli().getAlicuota());
					decJurDet.setMinimo(valUnRecConADe.getValorUnitario());
					decJurDet.setSubtotal1(decJurDet.getMultiplo()*decJurDet.getBase());
					
					if (decJurDet.getSubtotal1()> decJurDet.getMinimo())
						decJurDet.setTotalConcepto(decJurDet.getSubtotal1());
					else
						decJurDet.setTotalConcepto(decJurDet.getMinimo());
					
				}
				decJurDetAdapter.setDecJurDet(decJurDet);
				decJurDetAdapter.setIdFoco("actEspEtur");
			}
			
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			
			return decJurDetAdapter;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public DecJurDetAdapter createDecJurDet(UserContext userContext, DecJurDetAdapter decJurDetAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session=null;
		Transaction tx=null;
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session=SiatHibernateUtil.currentSession();
			tx=session.beginTransaction();
			
			DecJurDetVO decJurDetVO = decJurDetAdapter.getDecJurDet();
			
			DecJur decJur = DecJur.getById(decJurDetAdapter.getDecJurDet().getDecJur().getId());
			
			DecJurDet decJurDet =getDecJurDetFromVO(decJurDetVO);
			
			if (!decJurDet.hasError() && decJurDet.validateCreate()){
				GdeDAOFactory.getDecJurDetDAO().update(decJurDet);
				decJur.recalcularValores(true);
				decJurDetAdapter.getDecJurDet().setDecJur((DecJurVO)decJur.toVO(1));
			}else{
				decJurDet.passErrorMessages(decJurDetAdapter);
				decJurDetAdapter.setIdFoco(null);
			}
						
			if (!decJurDet.hasError())
				tx.commit();
			else
				tx.rollback();
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			
			return decJurDetAdapter;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			if (tx!=null)tx.rollback();
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public DecJurDetAdapter updateDecJurDet(UserContext userContext, DecJurDetAdapter decJurDetAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session=null;
		Transaction tx=null;
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session=SiatHibernateUtil.currentSession();
			tx=session.beginTransaction();
			
			DecJurDetVO decJurDetVO = decJurDetAdapter.getDecJurDet();
			
			DecJur decJur = DecJur.getById(decJurDetAdapter.getDecJurDet().getDecJur().getId());
			
			DecJurDet decJurDet =getDecJurDetFromVO(decJurDetVO);
			
			if (!decJurDet.hasError() && decJurDet.validateUpdate()){
				GdeDAOFactory.getDecJurDetDAO().update(decJurDet);
				decJur.recalcularValores(true);
				decJurDetAdapter.getDecJurDet().setDecJur((DecJurVO)decJur.toVO(1));
			}else{
				decJurDet.passErrorMessages(decJurDetAdapter);
			}
			
						
			if (!decJurDet.hasError())
				tx.commit();
			else
				tx.rollback();
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			
			return decJurDetAdapter;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			if (tx!=null)tx.rollback();
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public DecJurDetAdapter deleteDecJurDet(UserContext userContext, DecJurDetAdapter decJurDetAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session=null;
		Transaction tx=null;
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session=SiatHibernateUtil.currentSession();
			tx=session.beginTransaction();
			
			DecJurDet decJurDet = DecJurDet.getById(decJurDetAdapter.getDecJurDet().getId());
			
			DecJur decJur = DecJur.getById(decJurDet.getDecJur().getId());
			
			
			if (decJurDet.validateDelete()){
				GdeDAOFactory.getDecJurDetDAO().delete(decJurDet);
				session.flush();
				decJur.recalcularValores(true);
				decJurDetAdapter.setDecJurDet(new DecJurDetVO());
				decJurDetAdapter.getDecJurDet().setDecJur((DecJurVO)decJur.toVO(1));
				tx.commit();
			}else{
				decJurDet.passErrorMessages(decJurDetAdapter);
				tx.rollback();
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			
			return decJurDetAdapter;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			if (tx!=null)tx.rollback();
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	private DecJurDet getDecJurDetFromVO(DecJurDetVO decJurDetVO){
		//Si el id de VO es nulo debo crear uno nuevo
		
		DecJurDet decJurDet;
		log.debug("BASE: "+decJurDetVO.getBase()+" MULTIPLO:"+decJurDetVO.getMultiplo()+ " SUBTOTAL1: "+decJurDetVO.getSubtotal1());
		log.debug("CANTIDAD: "+decJurDetVO.getCanUni()+ " SUBTOTAL2: "+decJurDetVO.getSubtotal2());
		DecJur decJur = DecJur.getById(decJurDetVO.getDecJur().getId());
		
		Date fecVigCon = DateUtil.getDate("01/"+ StringUtil.completarCerosIzq(decJur.getPeriodo().toString(),2)+"/"+decJur.getAnio(),DateUtil.ddSMMSYYYY_MASK);
		
		if (decJurDetVO.getId()==null){
			decJurDet = new DecJurDet();
			decJurDet.setDecJur(decJur);
		}else{
			decJurDet=DecJurDet.getById(decJurDetVO.getId());
		}
		
		RecConADec recConADec = RecConADec.getByIdNull(decJurDetVO.getRecConADec().getId());
		
		decJurDet.setRecConADec(recConADec);
		decJurDet.setBase(decJurDetVO.getBase());
		
		decJurDet.setMultiplo(decJurDetVO.getMultiplo());
		decJurDet.setCanUni(decJurDetVO.getCanUni());
		
		if (!ModelUtil.isNullOrEmpty(decJurDetVO.getRecTipUni())){
			RecTipUni recTipUni = RecTipUni.getById(decJurDetVO.getRecTipUni().getId());
			decJurDet.setRecTipUni(recTipUni);
			decJurDet.setUnidad(recTipUni.getNomenclatura());
		}
		
		decJurDet.setValUnidad(decJurDetVO.getValUnidad());
		
		Double subtotal2=0D;
		
		if (!ModelUtil.isNullOrEmpty(decJurDetVO.getTipoUnidad())){
			RecConADec tipoUnidad = RecConADec.getById(decJurDetVO.getTipoUnidad().getId());
			decJurDet.setTipoUnidad(tipoUnidad);
			decJurDet.setDesTipoUnidad(tipoUnidad.getDesConcepto());
			Double cant=(decJurDet.getCanUni()!=null)?decJurDet.getCanUni():0D;
			Double valUni=(decJurDet.getValUnidad()!=null)?decJurDet.getValUnidad():0D;
			subtotal2= cant * valUni;
		}
			
		
		
		if (decJurDet.getBase()!=null && decJurDet.getMultiplo()!=null)
			decJurDet.setSubtotal1(NumberUtil.round(decJurDet.getBase()*decJurDet.getMultiplo(),SiatParam.DEC_PORCENTAJE_DB));
		else
			decJurDet.setSubtotal1(0D);
		
		
		decJurDet.setSubtotal2(subtotal2);
		Double total=0D;
		Double[]listDobles = {decJurDet.getSubtotal1(),decJurDet.getSubtotal2()};
		total = NumberUtil.getMayorValor(listDobles);
		decJurDet.setTotalConcepto(total);
		if (recConADec != null)
			decJurDet.setDetalle(recConADec.getDesConcepto());
	
		return decJurDet;
	}
	
	
	public DecJurPagAdapter getDecJurPagAdapterForCreate(UserContext userContext, Long idDecJur) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			DecJur decJur = DecJur.getById(idDecJur);
			
			DecJurPagAdapter decJurPagAdapter = new DecJurPagAdapter();
			
			decJurPagAdapter.getDecJurPag().setDecJur((DecJurVO)decJur.toVO(2));
			
			Recurso recurso = decJur.getRecurso();
			
			Date fecVigCon = DateUtil.getDate("01/"+ StringUtil.completarCerosIzq(decJur.getPeriodo().toString(),2)+"/"+decJur.getAnio(),DateUtil.ddSMMSYYYY_MASK);
			
			List<TipPagDecJur>listTipPagDecJur = TipPagDecJur.getListVigenteByRecurso(fecVigCon, recurso);
		
			decJurPagAdapter.getListTipPagDecJur().add(new TipPagDecJurVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR, 1));
			
			for(TipPagDecJur tipPagDecJur: listTipPagDecJur){
				TipPagDecJurVO tipPagDecJurVO=(TipPagDecJurVO)tipPagDecJur.toVO();
				int certificado=0;
				if (tipPagDecJur.getId().longValue()==TipPagDecJur.ID_RETENCION || tipPagDecJur.getId().longValue() == TipPagDecJur.ID_PERCEPCION){
					certificado=1;
				}
				tipPagDecJurVO.setIdWithCertificado(tipPagDecJur.getId()+","+certificado);
				decJurPagAdapter.getListTipPagDecJur().add(tipPagDecJurVO);
			}
			
			decJurPagAdapter.setListAgeRet(ListUtilBean.toVO(AgeRet.getListActivos(),1));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			
			return decJurPagAdapter;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
		
	public DecJurPagAdapter getDecJurPagAdapterForView(UserContext userContext, CommonKey selectedId) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			DecJurPag decJurPag = DecJurPag.getById(selectedId.getId());
			
			DecJurPagAdapter decJurPagAdapter = new DecJurPagAdapter();
			
			decJurPagAdapter.setDecJurPag((DecJurPagVO)decJurPag.toVO(3));
			
			TipPagDecJur tipPagDecJur = decJurPag.getTipPagDecJur();
			
			if (tipPagDecJur.getId().longValue()==TipPagDecJur.ID_PERCEPCION || tipPagDecJur.getId().longValue()== TipPagDecJur.ID_RETENCION){
				decJurPagAdapter.getDecJurPag().setConCertificado(true);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			
			return decJurPagAdapter;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
		
	public DecJurPagAdapter getDecJurPagAdapterForUpdate(UserContext userContext, CommonKey selectedId) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session=null;
		
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session=SiatHibernateUtil.currentSession();
			
			
			DecJurPagAdapter decJurPagAdapter = new DecJurPagAdapter();
			
			DecJurPag decJurPag = DecJurPag.getById(selectedId.getId());
			
			DecJur decJur = decJurPag.getDecJur();
			
			DecJurPagVO decJurPagVO = (DecJurPagVO)decJurPag.toVO(2);
			
			
			int certificado=0;
			
			Long idTipPagDecJur =decJurPagVO.getTipPagDecJur().getId().longValue();
			
			if (idTipPagDecJur==TipPagDecJur.ID_RETENCION || idTipPagDecJur == TipPagDecJur.ID_PERCEPCION){
				certificado=1;
			}
			decJurPagVO.getTipPagDecJur().setIdWithCertificado(idTipPagDecJur+","+certificado);
			
			decJurPagAdapter.setDecJurPag(decJurPagVO);
			
			decJurPagAdapter.getDecJurPag().setDecJur((DecJurVO)decJur.toVO(2));
			
			Recurso recurso = decJur.getRecurso();
			
			Date fecVigCon = DateUtil.getDate("01/"+ StringUtil.completarCerosIzq(decJur.getPeriodo().toString(),2)+"/"+decJur.getAnio(),DateUtil.ddSMMSYYYY_MASK);
			
			List<TipPagDecJur>listTipPagDecJur = TipPagDecJur.getListVigenteByRecurso(fecVigCon, recurso);
		
			//decJurPagAdapter.getListTipPagDecJur().add(new TipPagDecJurVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR, 1));
			
			for(TipPagDecJur tipPagDecJur: listTipPagDecJur){
				TipPagDecJurVO tipPagDecJurVO=(TipPagDecJurVO)tipPagDecJur.toVO();
				certificado=0;
				if (tipPagDecJur.getId().longValue()==TipPagDecJur.ID_RETENCION || tipPagDecJur.getId().longValue() == TipPagDecJur.ID_PERCEPCION){
					certificado=1;
					decJurPagAdapter.getDecJurPag().setConCertificado(true);
				}
				tipPagDecJurVO.setIdWithCertificado(tipPagDecJur.getId()+","+certificado);
				decJurPagAdapter.getListTipPagDecJur().add(tipPagDecJurVO);
			}
			
			decJurPagAdapter.setListAgeRet(ListUtilBean.toVO(AgeRet.getListActivos(),1));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			
			return decJurPagAdapter;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DecJurPagAdapter createDecJurPag(UserContext userContext, DecJurPagAdapter decJurPagAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session=null;
		Transaction tx=null;
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session=SiatHibernateUtil.currentSession();
			tx=session.beginTransaction();
			
			DecJurPagVO decJurPagVO = decJurPagAdapter.getDecJurPag();
			
			DecJur decJur = DecJur.getById(decJurPagVO.getDecJur().getId());
			
			DecJurPag decJurPag = new DecJurPag();
			
			String[] listValoresIdView=decJurPagVO.getTipPagDecJur().getIdWithCertificado().split(",");
			
			Long idTipPagDecJur=Long.parseLong(listValoresIdView[0]);
			
			if (idTipPagDecJur == null || idTipPagDecJur < 1){
				decJurPagAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,GdeError.DECJURPAG_TIPOPAGO_LABEL);
			}
			if (decJurPagVO.getImporte()==null){
				decJurPagAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,GdeError.DECJURPAG_IMPORTE_LABEL);
			}
			
			if(decJurPagAdapter.hasError()){
				return decJurPagAdapter;
			}
			
			TipPagDecJur tipPagDecJur = TipPagDecJur.getById(idTipPagDecJur);
			
			decJurPag.setDecJur(decJur);
			
			decJurPag.setTipPagDecJur(tipPagDecJur);
			
			decJurPag.setImporte(decJurPagVO.getImporte());
			
			if(idTipPagDecJur==TipPagDecJur.ID_RETENCION || idTipPagDecJur == TipPagDecJur.ID_PERCEPCION){
				decJurPag.setCertificado(decJurPagVO.getCertificado());
				decJurPag.setCuitAgente(decJurPagVO.getCuitAgente());
			}
			
			if (decJurPag.validateCreate()){
				GdeDAOFactory.getDecJurPagDAO().update(decJurPag);
				decJur.recalcularValores(true);
				decJurPagAdapter.getDecJurPag().setDecJur((DecJurVO)decJur.toVO(1));
			}else{
				decJurPag.passErrorMessages(decJurPagAdapter);
			}
			
			if (!decJurPag.hasError())
				tx.commit();
			else
				tx.rollback();
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			
			return decJurPagAdapter;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			if (tx!=null)tx.rollback();
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public DecJurPagAdapter deleteDecJurPag(UserContext userContext, DecJurPagAdapter decJurPagAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session=null;
		Transaction tx=null;
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session=SiatHibernateUtil.currentSession();
			tx=session.beginTransaction();
			
			DecJurPag decJurPag = DecJurPag.getById(decJurPagAdapter.getDecJurPag().getId());
			
			DecJur decJur = decJurPag.getDecJur();
			
			
			if (decJurPag.validateDelete()){
				GdeDAOFactory.getDecJurPagDAO().delete(decJurPag);
				session.flush();
				decJur.recalcularValores(true);
				decJurPagAdapter.setDecJurPag(new DecJurPagVO());
				decJurPagAdapter.getDecJurPag().setDecJur((DecJurVO)decJur.toVO(1));
				tx.commit();
			}else{
				decJurPag.passErrorMessages(decJurPagAdapter);
				tx.rollback();
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			
			return decJurPagAdapter;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			if (tx!=null)tx.rollback();
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public DecJurPagAdapter updateDecJurPag(UserContext userContext, DecJurPagAdapter decJurPagAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session=null;
		Transaction tx=null;
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session=SiatHibernateUtil.currentSession();
			tx=session.beginTransaction();
			
			DecJurPagVO decJurPagVO = decJurPagAdapter.getDecJurPag();
			
			DecJurPag decJurPag = DecJurPag.getById(decJurPagVO.getId());
			
			DecJur decJur = decJurPag.getDecJur();
			
			String[] listValoresIdView=decJurPagVO.getTipPagDecJur().getIdWithCertificado().split(",");
			
			Long idTipPagDecJur=Long.parseLong(listValoresIdView[0]);
			
			if (idTipPagDecJur == null || idTipPagDecJur < 1){
				decJurPagAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,GdeError.DECJURPAG_TIPOPAGO_LABEL);
			}
			if (decJurPagVO.getImporte()==null){
				decJurPagAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,GdeError.DECJURPAG_IMPORTE_LABEL);
			}
			
			if(decJurPagAdapter.hasError()){
				return decJurPagAdapter;
			}
			
			TipPagDecJur tipPagDecJur = TipPagDecJur.getById(idTipPagDecJur);
			
			decJurPag.setTipPagDecJur(tipPagDecJur);
			
			decJurPag.setImporte(decJurPagVO.getImporte());
			
			if(idTipPagDecJur==TipPagDecJur.ID_RETENCION || idTipPagDecJur == TipPagDecJur.ID_PERCEPCION){
				decJurPag.setCertificado(decJurPagVO.getCertificado());
				decJurPag.setCuitAgente(decJurPagVO.getCuitAgente());
			}else{
				decJurPag.setCertificado(null);
				decJurPag.setCuitAgente(null);
			}
			
			if (decJurPag.validateCreate()){
				GdeDAOFactory.getDecJurPagDAO().update(decJurPag);
				decJur.recalcularValores(true);
				decJurPagAdapter.getDecJurPag().setDecJur((DecJurVO)decJur.toVO(1));
			}else{
				decJurPag.passErrorMessages(decJurPagAdapter);
			}
			
			if (!decJurPag.hasError())
				tx.commit();
			else
				tx.rollback();
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			
			return decJurPagAdapter;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			if (tx!=null)tx.rollback();
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PrintModel imprimirCierreComercio(UserContext userContext, long idCuenta) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		PrintModel print = null;
		try {			
			Cuenta cuenta = Cuenta.getByIdNull(idCuenta);
						
			if (cuenta == null) {
				throw new DemodaServiceException("No se encontro la cuenta con id:"+idCuenta);				
			}
			
			CierreComercio cierreComercio = GdeDAOFactory.getCierreComercioDAO().getCierreComercioByObjImp(cuenta.getObjImp());
			
			CuentaVO cuentaVO = cuenta.toVO4Print();
			
			// TO VO para los Titulares de la Cuenta vigentes a la fecha de hoy
			List<CuentaTitularVO> listCuentaTitularVO = new ArrayList<CuentaTitularVO>();
			Date fechaAnalisis = cuenta.getFechaBaja();
			for (CuentaTitular titular: cuenta.getListCuentaTitularVigentesCerrado(fechaAnalisis)) {
				CuentaTitularVO titularVO = titular.toVOForCuenta();
				listCuentaTitularVO.add(titularVO);
			}
			cuentaVO.setListCuentaTitular(listCuentaTitularVO);
			
			CierreComercioVO cierreComercioVO = (CierreComercioVO) cierreComercio.toVO(2, false);
			CasoVO casoVO = CasServiceLocator.getCasCasoService().construirCasoVO(cierreComercio.getIdCaso());
			cierreComercioVO.setCaso(casoVO);
			cierreComercioVO.setCuentaVO(cuentaVO);
			
			TipObjImpDefinition tipObjImpDefinitionRubro =cuenta.getObjImp().getDefinitionValue(TipObjImpAtr.ID_RUBROS_TIPO_COMERCIO);
			
			List<TipObjImpAtrDefinition> rubros = tipObjImpDefinitionRubro.getListTipObjImpAtrDefinition();
			
			TipObjImpDefinition tipObjImpDefinitionPermiso =cuenta.getObjImp().getDefinitionValue(TipObjImpAtr.ID_PERMISO);

			// Trae el definition para el atributo Permiso 
			TipObjImpAtrDefinition atrPermiso = tipObjImpDefinitionPermiso.getListTipObjImpAtrDefinition().get(0);
			// Intenta formatear el valor del numero de ficha de habilitacion.
			String permiso = atrPermiso.getValorString();
			try{
				int anio = Integer.valueOf(permiso.substring(permiso.length()-4, permiso.length())).intValue();
				if(anio > 1900 && anio < 3000){
					permiso = permiso.substring(0,permiso.length()-4)+"/"+permiso.substring(permiso.length()-4, permiso.length());
				}
			}catch (Exception e) {
				permiso = atrPermiso.getValorString();
			}
			cierreComercioVO.setPermiso(permiso);
			
			for (String strRubro :rubros.get(0).getMultiValorView()){
			   RubroVO rubro = new RubroVO();
			   rubro.setRubro(strRubro);
			   cierreComercioVO.getListRubro().add(rubro);
			}
		
			// Obtiene el formulario
			print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_CONSTANCIA_CIERRE_GDE);
					
			// Le setea los datos		
			print.putCabecera("Usuario", userContext.getUserName());
			print.putCabecera("FechaActualCompleta", DateUtil.getDateEnLetras(new Date()));
			print.setData(cierreComercioVO);
			print.setTopeProfundidad(6);

			return print;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		}
	}

	// <--- Administrar Declaracion Jurada
	
	
	// ---> Deuda Exencion Marcar/ Desmarcar
	/**
	 * Obtiene una cuenta y sus exenciones.
	 * 
	 */
	public LiqDeudaAdapter getByRecursoNroCuenta4CuentaExencion(	UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO)
			throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			
			DemodaTimer dt = new DemodaTimer();
			
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			liqDeudaAdapterVO.clearError();

			String numeroCuenta = liqDeudaAdapterVO.getCuenta().getNumeroCuenta();
			Long idRecurso = liqDeudaAdapterVO.getCuenta().getIdRecurso();
			
			if (StringUtil.isNullOrEmpty(numeroCuenta)){
				liqDeudaAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_LABEL );
			}
			
			if (idRecurso == null || idRecurso.equals(-1L) ){
				liqDeudaAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL );				
			}
			
			if (liqDeudaAdapterVO.hasError()){
				return liqDeudaAdapterVO;			
			}
			
			// 1:: Se recupera la cuenta
			log.debug(funcName + " 1:: getByIdRecursoYNumeroCuenta()");
			Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(idRecurso, numeroCuenta, Estado.ACTIVO);
			
			if (cuenta == null){
				liqDeudaAdapterVO.addRecoverableValueError("La cuenta numero " + numeroCuenta + " es inexistente");				
			}
			
			if (liqDeudaAdapterVO.hasError()){
				return liqDeudaAdapterVO;			
			}
			
			// Llamada al helper que devuele el estado completo de la deuda
			LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(cuenta);
			
			liqDeudaAdapterVO.setCuenta(liqDeudaBeanHelper.getCuenta());
			
			LiqExencionesVO liqExencionesVO = liqDeudaBeanHelper.getExenciones();
			
			for (LiqExencionVO liqExencion: liqExencionesVO.getListExeVigentes()){
				
				CueExe cueExe = CueExe.getById(liqExencion.getIdCueExe());
				
				if (cueExe.getExencion().esExentoTotal()){
					liqExencion.setSelectExencionEnabled(true);
				} else {
					liqExencion.setSelectExencionEnabled(false);
				}
			}
			
			liqDeudaAdapterVO.setExenciones(liqExencionesVO);
			
			log.info(dt.stop(" LiqDeuda : getByRecursoNroCuenta4CuentaExencion()"));
			
			log.debug(funcName + ": exit");
			return liqDeudaAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			// throw new DemodaServiceException(e);
			// Codigo para R11 quitar mas adelante
			liqDeudaAdapterVO.addRecoverableValueError("No se pudo recuperar la cuenta");
			return liqDeudaAdapterVO;
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/**
	 * Obtiene los periodos de deuda para una cuenta y el rando de fechas de una exencion.
	 * 
	 */
	public LiqDeudaAdapter getByRecursoNroCuenta4DeudaExencion(	UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO)
		throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			liqDeudaAdapterVO.clearError();

			// 1:: Se recupera la cuenta
			log.debug(funcName + " 1:: getByIdRecursoYNumeroCuenta()");
			Cuenta cuenta = Cuenta.getById(liqDeudaAdapterVO.getCuenta().getIdCuenta());
			
			CueExe cueExe = CueExe.getById(new Long(liqDeudaAdapterVO.getSelectedId()));
			
			// Llamada al helper que devuele el estado completo de la deuda
			LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(cuenta);
			
			liqDeudaAdapterVO = liqDeudaBeanHelper.getLiqDeudaAdapter4DeudaExencion(liqDeudaAdapterVO, 
																					cueExe.getFechaDesde(), 
																					cueExe.getFechaHasta());
			
			log.debug(funcName + ": exit");
			return liqDeudaAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			// throw new DemodaServiceException(e);
			// Codigo para R11 quitar mas adelante
			liqDeudaAdapterVO.addRecoverableValueError("No se pudo recuperar la cuenta");
			return liqDeudaAdapterVO;
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public LiqDeudaAdapter aplicarQuitarExencion(UserContext userContext,
			LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession(); 
			tx = session.beginTransaction();
			
			liqDeudaAdapterVO.clearErrorMessages();
			
			if (liqDeudaAdapterVO.getListIdDeudaSelected() == null){
				liqDeudaAdapterVO.addRecoverableValueError("Debe seleccionar al menos un registro de deuda para realiar la operacion");
			}
			
			if (liqDeudaAdapterVO.hasError()){
				return liqDeudaAdapterVO;
			}
			
			// Recuperamos la deuda seleccinada			
			String[] listIdDeudaSelected = liqDeudaAdapterVO.getListIdDeudaSelected();
			
			if (listIdDeudaSelected != null){
				for (int i=0; i < listIdDeudaSelected.length; i++){
					
					String strIdDeuda = listIdDeudaSelected[i];
					String[] arrIdDeuda = strIdDeuda.split("-");
					
					Long idDeuda = new Long (arrIdDeuda[0]);
					Long idEstadoDeuda = new Long(arrIdDeuda[1]);
					
					Deuda deuda = Deuda.getById(idDeuda, idEstadoDeuda);
					
					// Aplicamos
					if (deuda.getSaldo().doubleValue() != 0){
						deuda.setImporte(0D);
						deuda.setSaldo(0D);
						log.debug(funcName + " aplicando exencion a deuda: " + deuda.getId());
					// Quitamos	
					} else {
						deuda.setImporte(deuda.getImporteBruto());
						deuda.setSaldo(deuda.getImporteBruto());
						log.debug(funcName + " quitando exencion a deuda: " + deuda.getId());
					}
					
					GdeGDeudaManager.getInstance().update(deuda);
				}
			}
			
			tx.commit();
			
			log.debug(funcName + ": exit");
			return liqDeudaAdapterVO;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	/**
	 * Vuelta atras de la Declaracion Jurada y su impacto en la Deuda. 
	 * Valida si la Deuda determinada no fue alterada (cancelada, incluida en convenio).
	 * 
	 */
	public DecJurVO vueltaAtrasDecJur(UserContext userContext, DecJurVO decJurVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			decJurVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			DecJur decJur = DecJur.getById(decJurVO.getId());
			
			Deuda deuda = decJur.getDeuda();
			
			
			// Valida si la Deuda determinada esta en la Via Admin y Estado Administrativo 
			if(deuda.getViaDeuda().getId().longValue() != ViaDeuda.ID_VIA_ADMIN) {
				decJurVO.addRecoverableError(GdeError.DECJUR_DEUDA_VIA_DEUDA_ERROR);
				log.debug(funcName + ": exit");
		        return decJurVO;
			}
			if(deuda.getEstadoDeuda().getId().longValue() != EstadoDeuda.ID_ADMINISTRATIVA) {
				decJurVO.addRecoverableError(GdeError.DECJUR_DEUDA_ESTADO_DEUDA_ERROR);
				log.debug(funcName + ": exit");
		        return decJurVO;
			}
			// Valida si la Deuda determinada no fue cancelada 
			if(deuda.getFechaPago() != null) {
				decJurVO.addRecoverableError(GdeError.DECJUR_DEUDA_CANCELADA_ERROR);
				log.debug(funcName + ": exit");
		        return decJurVO;
			}
			// Valida si la Deuda determinada no fue incluida en un convenio
			if(deuda.getConvenio() != null){
				decJurVO.addRecoverableError(GdeError.DECJUR_DEUDA_EN_CONVENIO_ERROR);
				log.debug(funcName + ": exit");
		        return decJurVO;				
			}
			
			DeudaAdmin deudaAdmin = (DeudaAdmin) deuda;
			
			if((decJur.getTipDecJurRec().getId().longValue() == TipDecJurRec.ID_DJ_ORIGINAL.longValue()) || 
					(decJur.getTipDecJurRec().getId().longValue() == TipDecJurRec.ID_DJ_RECTIFICATIVA.longValue()) && decJur.getImporteAntDeu() != null && decJur.getSaldoAntDeu() != null){
				// Restaurar valor de Deuda
				deudaAdmin.setImporteBruto(decJur.getImporteAntDeu());
				deudaAdmin.setImporte(decJur.getImporteAntDeu());
				deudaAdmin.setSaldo(decJur.getSaldoAntDeu());
				
				// Restaura el importe de los conceptos
				for(DeuAdmRecCon deuAdmRecCon: deudaAdmin.getListDeuRecCon()){
					Double porcentajeRecCon = deuAdmRecCon.getRecCon().getPorcentaje();
					Double importeRecCon=NumberUtil.round(decJur.getImporteAntDeu()* porcentajeRecCon,SiatParam.DEC_IMPORTE_DB);
					Double saldoRecCon=NumberUtil.round(decJur.getSaldoAntDeu()* porcentajeRecCon,SiatParam.DEC_IMPORTE_DB);
					
					deuAdmRecCon.setImporte(importeRecCon);
					deuAdmRecCon.setImporteBruto(importeRecCon);
					deuAdmRecCon.setSaldo(saldoRecCon);
					
					deudaAdmin.updateDeuAdmRecCon(deuAdmRecCon);
				}
				
				deudaAdmin.setStrConceptosByListRecCon(deudaAdmin.getListDeuRecCon());
				
				GdeDAOFactory.getDeudaAdminDAO().update(deudaAdmin);				
			}else if(decJur.getTipDecJurRec().getId().longValue() == TipDecJurRec.ID_DJ_RECTIFICATIVA.longValue() && decJur.getImporteAntDeu() == null && decJur.getSaldoAntDeu() == null){
				// Anular Deuda creada.	
				Anulacion anulacionDeuda = new Anulacion();
				anulacionDeuda.setIdDeuda(deuda.getId());
				anulacionDeuda.setFechaAnulacion(new Date());
				anulacionDeuda.setMotAnuDeu(MotAnuDeu.getById(MotAnuDeu.ID_ANULACION));
				anulacionDeuda.setRecurso(deuda.getRecurso());
				anulacionDeuda.setViaDeuda(deuda.getViaDeuda());
				anulacionDeuda.setObservacion("Se anuló la Declaración Jurada que creó la deuda.");
				
			 	GdeDAOFactory.getDeudaAnuladaDAO().update(anulacionDeuda);
			 	
				GdeGDeudaManager.getInstance().anularDeuda(anulacionDeuda, deuda, null);
			}
		
			// Pasar Declaracion Jurada a estado 'Anulada' (o 'Cancelada')
	        decJur.setEstado(Estado.ANULADO.getId());
			
	        GdeDAOFactory.getDecJurDAO().update(decJur);
			
			if (decJur.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			decJur.passErrorMessages(decJurVO);
            
            log.debug(funcName + ": exit");
            return decJurVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	// <----

	/**
	 *  Modificar Deuda Administrativa: Permite modificar el saldo, el importe y la fecha de pago. 
	 * 
	 */
	public LiqDetalleDeudaAdapter modificarDeuda(UserContext userContext, LiqDetalleDeudaAdapter liqDetalleDeudaAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
		
			liqDetalleDeudaAdapterVO.clearError();

			Long idDeuda = liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().getIdDeuda();
			
			if (idDeuda == null){
				liqDetalleDeudaAdapterVO.addRecoverableValueError("El Id de Deuda no puede ser nulo: ");				
			}
			
			if (liqDetalleDeudaAdapterVO.hasError()){
				return liqDetalleDeudaAdapterVO;			
			}
			
			// 1:: Se recupera la deuda
			DeudaAdmin deuda = DeudaAdmin.getById(idDeuda);
						
			if (deuda == null){
				liqDetalleDeudaAdapterVO.addRecoverableValueError("Error al recuperar el registro de deuda: " + idDeuda );				
			}
			
			if (liqDetalleDeudaAdapterVO.hasError()){
				return liqDetalleDeudaAdapterVO;			
			}
			
			deuda.setImporte(liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().getImporte());
			deuda.setSaldo(liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().getSaldo());
			deuda.setActualizacion(liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().getActualizacion());
			deuda.setFechaPago(liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().getFechaPago());
			
			List<DeuAdmRecCon> listConceptos = deuda.getListDeuRecCon();
			for (DeuAdmRecCon concepto:listConceptos){
				LiqConceptoDeudaVO liqConceptoAModificar = null;
				for(LiqConceptoDeudaVO liqConcepto : liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().getListConceptos()){
					if(liqConcepto.getIdRecConView().equals(concepto.getRecCon().getId().toString())){
						liqConceptoAModificar = liqConcepto;
						break;
					}
				}
				if(liqConceptoAModificar != null){
					concepto.setImporte(liqConceptoAModificar.getImporte());
					concepto = deuda.updateDeuAdmRecCon(concepto);
				}
			}
		    deuda.setStrConceptosByListRecCon(listConceptos);
			
			
			GdeDAOFactory.getDeudaAdminDAO().update(deuda);
			session.flush();
			
			if (deuda.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			deuda.passErrorMessages(liqDetalleDeudaAdapterVO);
			
			log.debug(funcName + ": exit");
			return liqDetalleDeudaAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}		
	}

	public CtrlInfDeuAdapter getCtrlInfDeuAdapterForDesbloquear(UserContext userContext, CtrlInfDeuAdapter ctrlInfDeuAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			// Limpiamos flags
			ctrlInfDeuAdapter.setParamEncontrado(false);
			ctrlInfDeuAdapter.setParamNoEncontrado(false);
			
			// Validacion de filtro requerido
			String codigo = ctrlInfDeuAdapter.getCodigo();
			if(StringUtil.isNullOrEmpty(codigo)){
				ctrlInfDeuAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.TRAMITE_CODREFPAG_LABEL);
				log.debug(funcName + ": exit");
				return ctrlInfDeuAdapter;
			}
			
			// Se intenta tomar el valor cargado: Puede ser del tipo numerico y corresponder a un 'codRefPag' o de tipo 'nro/anio'
			Long numero = null, anio = null;
			if (codigo.indexOf("/") >= 0) {
				try { numero = Long.valueOf(codigo.split("/")[0]); } catch (Exception e) {}
				try { anio = Long.valueOf(codigo.split("/")[1]); } catch (Exception e) {}
			} else {
				try { numero = Long.valueOf(codigo); } catch (Exception e) {}
			}
			// Validacion de Formato: No se puede recuperar el codRefPag o el Nro y Anio de Recibo por error en formato
			if(numero == null){
				ctrlInfDeuAdapter.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, GdeError.TRAMITE_CODREFPAG_LABEL);
				log.debug(funcName + ": exit");
				return ctrlInfDeuAdapter;
			}
			
			if(anio == null)
				anio = 0L;
			
			// Buscamos el CtrInfDeu
			CtrlInfDeu ctrlInfDeu = CtrlInfDeu.getByNroYAnio(numero, anio);
			
			if(ctrlInfDeu != null){
				ctrlInfDeuAdapter.setCtrlInfDeu((CtrlInfDeuVO) ctrlInfDeu.toVO(1, false));
				ctrlInfDeuAdapter.setParamEncontrado(true);
				ctrlInfDeuAdapter.setParamNoEncontrado(false);
			}else{
				ctrlInfDeuAdapter.setCtrlInfDeu(new CtrlInfDeuVO());
				ctrlInfDeuAdapter.setParamEncontrado(false);
				ctrlInfDeuAdapter.setParamNoEncontrado(true);
			}
			
			log.debug(funcName + ": exit");
			return ctrlInfDeuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CtrlInfDeuVO deleteCtrlInfDeu(UserContext userContext, CtrlInfDeuVO ctrlInfDeuVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			CtrlInfDeu ctrlInfDeu = CtrlInfDeu.getById(ctrlInfDeuVO.getId());
			
			// Antes de Eliminar el Registro se deja una copia en HisInfDeu
			HisInfDeu hisInfDeu = new HisInfDeu();
			hisInfDeu.setAnioRecibo(ctrlInfDeu.getAnioRecibo());
			hisInfDeu.setCodId(ctrlInfDeu.getCodId());
			hisInfDeu.setCuenta(ctrlInfDeu.getCuenta());
			hisInfDeu.setFechaHoraGen(ctrlInfDeu.getFechaHoraGen());
			hisInfDeu.setFechaHoraImp(ctrlInfDeu.getFechaHoraImp());
			hisInfDeu.setNroLiquidacion(ctrlInfDeu.getNroLiquidacion());
			hisInfDeu.setNroRecibo(ctrlInfDeu.getNroRecibo());
			hisInfDeu.setNroTramite(ctrlInfDeu.getNroTramite());
			hisInfDeu.setObservacion(ctrlInfDeu.getObservacion());
			hisInfDeu.setEstado(Estado.ACTIVO.getId());
			
			GdeGDeudaManager.getInstance().createHisInfDeu(hisInfDeu);

			// Eliminamos el Registro de CtrlInfDeu para desbloquear el tramite.
			GdeGDeudaManager.getInstance().deleteCtrlInfDeu(ctrlInfDeu);				
			
			hisInfDeu.passErrorMessages(ctrlInfDeu);
			
			if (ctrlInfDeu.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				ctrlInfDeuVO =  (CtrlInfDeuVO) ctrlInfDeu.toVO();
			}
			
            ctrlInfDeu.passErrorMessages(ctrlInfDeuVO);
            
            log.debug(funcName + ": exit");
            return ctrlInfDeuVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	
}