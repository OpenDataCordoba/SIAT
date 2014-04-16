//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.buss.service;

/**
 * Implementacion de servicios del submodulo Tramite del modulo Rod
 * @author tecso
 */

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.SiatUtil;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.gde.buss.bean.Mandatario;
import ar.gov.rosario.siat.pad.buss.bean.Calle;
import ar.gov.rosario.siat.pad.buss.bean.Localidad;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import ar.gov.rosario.siat.pad.buss.bean.UbicacionFacade;
import ar.gov.rosario.siat.pad.iface.model.CalleVO;
import ar.gov.rosario.siat.pad.iface.model.DomicilioAdapter;
import ar.gov.rosario.siat.pad.iface.model.LocalidadVO;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import ar.gov.rosario.siat.pad.iface.model.Sexo;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.rod.buss.bean.EstadoCivil;
import ar.gov.rosario.siat.rod.buss.bean.EstadoTramiteRA;
import ar.gov.rosario.siat.rod.buss.bean.HisEstTra;
import ar.gov.rosario.siat.rod.buss.bean.Marca;
import ar.gov.rosario.siat.rod.buss.bean.Modelo;
import ar.gov.rosario.siat.rod.buss.bean.Propietario;
import ar.gov.rosario.siat.rod.buss.bean.RodTramiteManager;
import ar.gov.rosario.siat.rod.buss.bean.TipoCarga;
import ar.gov.rosario.siat.rod.buss.bean.TipoDoc;
import ar.gov.rosario.siat.rod.buss.bean.TipoFabricacion;
import ar.gov.rosario.siat.rod.buss.bean.TipoMotor;
import ar.gov.rosario.siat.rod.buss.bean.TipoPago;
import ar.gov.rosario.siat.rod.buss.bean.TipoPropietario;
import ar.gov.rosario.siat.rod.buss.bean.TipoTramite;
import ar.gov.rosario.siat.rod.buss.bean.TipoUso;
import ar.gov.rosario.siat.rod.buss.bean.TipoVehiculo;
import ar.gov.rosario.siat.rod.buss.bean.TramiteRA;
import ar.gov.rosario.siat.rod.buss.dao.RodDAOFactory;
import ar.gov.rosario.siat.rod.iface.model.EstadoCivilVO;
import ar.gov.rosario.siat.rod.iface.model.EstadoTramiteRAVO;
import ar.gov.rosario.siat.rod.iface.model.HisEstTraVO;
import ar.gov.rosario.siat.rod.iface.model.MarcaVO;
import ar.gov.rosario.siat.rod.iface.model.ModeloSearchPage;
import ar.gov.rosario.siat.rod.iface.model.ModeloVO;
import ar.gov.rosario.siat.rod.iface.model.PropietarioAdapter;
import ar.gov.rosario.siat.rod.iface.model.PropietarioVO;
import ar.gov.rosario.siat.rod.iface.model.TipoCargaVO;
import ar.gov.rosario.siat.rod.iface.model.TipoDocVO;
import ar.gov.rosario.siat.rod.iface.model.TipoFabricacionVO;
import ar.gov.rosario.siat.rod.iface.model.TipoMotorVO;
import ar.gov.rosario.siat.rod.iface.model.TipoPagoVO;
import ar.gov.rosario.siat.rod.iface.model.TipoPropietarioVO;
import ar.gov.rosario.siat.rod.iface.model.TipoTramiteVO;
import ar.gov.rosario.siat.rod.iface.model.TipoUsoVO;
import ar.gov.rosario.siat.rod.iface.model.TipoVehiculoVO;
import ar.gov.rosario.siat.rod.iface.model.TramiteRAAdapter;
import ar.gov.rosario.siat.rod.iface.model.TramiteRASearchPage;
import ar.gov.rosario.siat.rod.iface.model.TramiteRAVO;
import ar.gov.rosario.siat.rod.iface.service.IRodTramiteService;
import ar.gov.rosario.siat.rod.iface.util.RodError;
import ar.gov.rosario.siat.seg.buss.bean.UsuarioSiat;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.UserContext;

public class RodTramiteServiceHbmImpl implements IRodTramiteService {
	private Logger log = Logger.getLogger(RodTramiteServiceHbmImpl.class);
	
	// ---> ABM TramiteRA 	
	public TramiteRASearchPage getTramiteRASearchPageInit(UserContext userContext) throws DemodaServiceException {		
		return new TramiteRASearchPage();
	}

	public TramiteRASearchPage getTramiteRASearchPageResult(UserContext userContext, TramiteRASearchPage tramiteRASearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			tramiteRASearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<TramiteRA> listTramiteRA = RodDAOFactory.getTramiteRADAO().getBySearchPage(tramiteRASearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		// Obtenemos el usurio logueado
	   		UsuarioSiat usuarioSiat = UsuarioSiat.getById(userContext.getIdUsuarioSiat());

	   		tramiteRASearchPage.getListResult().clear();
	   		
	   		// Seteamos las banderas de negocio
	   		for(TramiteRA tramiteRA: listTramiteRA){
	   			TramiteRAVO tramiteRAVO = (TramiteRAVO)tramiteRA.toVO(1);
	   			
	   			if(tramiteRA.getEstTra().getArea().getId()!=null && tramiteRA.getEstTra().getArea().getId().equals(usuarioSiat.getArea().getId())){
	   				tramiteRAVO.setModificarBussEnabled(true);
	   				tramiteRAVO.setEliminarBussEnabled(true);
	   				tramiteRAVO.setCambiarEstadoBussEnabled(true);
	   			}else{
	   				tramiteRAVO.setModificarBussEnabled(false);
	   				tramiteRAVO.setEliminarBussEnabled(false);
	   				tramiteRAVO.setCambiarEstadoBussEnabled(false);
	   			}
	   			tramiteRASearchPage.getListResult().add(tramiteRAVO);
	   		}
	   		//tramiteRASearchPage.setListResult(listTramiteRAVO);
	   		// Lista de id's a bloquear para que no se puedan seleccionar
	   		// porque ya estan incluidos 
	   		   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return tramiteRASearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			e.printStackTrace();
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	
	public TramiteRAAdapter getTramiteRAAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TramiteRA tramiteRA = TramiteRA.getById(commonKey.getId());

	        TramiteRAAdapter tramiteRAAdapter = new TramiteRAAdapter();	       	        	        
	        TramiteRAVO tramiteRAVO = (TramiteRAVO) tramiteRA.toVO(1);
	        tramiteRAAdapter.setTramiteRA(tramiteRAVO);
	        
	        if(tramiteRAAdapter.getTramiteRA().getECodTipoMotor()==null){
	        	tramiteRAAdapter.getTramiteRA().setECodTipoMotor(-1);
	        }
	        if(tramiteRAAdapter.getTramiteRA().getICodPago()==null){
	        	tramiteRAAdapter.getTramiteRA().setICodPago(-1);
	        }
	        
	        if(tramiteRAVO.getDBis().equals(1))
	        	tramiteRAAdapter.getTramiteRA().getDDomicilio().setBis(SiNo.SI);
	        else
	        	tramiteRAAdapter.getTramiteRA().getDDomicilio().setBis(SiNo.NO);	       
	        

			// Seteo la lista para combo, valores, etc
	     // lista para los tipos de tramite
			
			Integer size = TipoTramite.getList().size();
			int i = 0;
			while(i<(size/2)){
				tramiteRAAdapter.getListTipoTramite1().add((TipoTramiteVO)TipoTramite.getList().get(i).toVO());
				i++;
			}
			while(i<size){
				tramiteRAAdapter.getListTipoTramite2().add((TipoTramiteVO)TipoTramite.getList().get(i).toVO());
				i++;
			}
			tramiteRAAdapter.setSizeLista(tramiteRAAdapter.getListTipoTramite().size());
			tramiteRAAdapter.setSizeLista1(tramiteRAAdapter.getListTipoTramite1().size());
			tramiteRAAdapter.setSizeLista2(tramiteRAAdapter.getListTipoTramite2().size());
			
			if(!ListUtil.isNullOrEmpty(tramiteRAAdapter.getTramiteRA().getListPropietario())){
				for (PropietarioVO ct : tramiteRAAdapter.getTramiteRA().getListPropietario()) {	
					ct.setSexo(Sexo.getById(ct.getCodSexo()));
					if(ct.getTipoPropietario().equals(1)){							
						if (SiNo.SI.equals(ct.getEsPropPrincipal())) {	
							tramiteRAAdapter.getTramiteRA().setCApellidoORazon(ct.getApellidoORazon());
							tramiteRAAdapter.getTramiteRA().setCDesTipoDoc(ct.getDesTipoDoc());
							tramiteRAAdapter.getTramiteRA().setCCodTipoDoc(ct.getCodTipoDoc());
							tramiteRAAdapter.getTramiteRA().setCNroDoc(ct.getNroDoc());
							tramiteRAAdapter.getTramiteRA().setCNroIB(ct.getNroIB());
							tramiteRAAdapter.getTramiteRA().setCNroProdAgr(ct.getNroProdAgr());
							tramiteRAAdapter.getTramiteRA().setCNroCuit(ct.getNroCuit());
							tramiteRAAdapter.getTramiteRA().setCCodTipoProp(ct.getCodTipoProp());
							tramiteRAAdapter.getTramiteRA().setCDesTipoProp(ct.getDesTipoProp());
							tramiteRAAdapter.getTramiteRA().setCFechaNac(ct.getFechaNac());
							tramiteRAAdapter.getTramiteRA().setCDesEstCiv(ct.getDesEstCiv());
							tramiteRAAdapter.getTramiteRA().setCCodEstCiv(ct.getCodEstCiv());
							tramiteRAAdapter.getTramiteRA().setCDesSexo(ct.getDesSexo());
							
							
						}else{
							if(ct.getTipoPropietario().equals(2)){
								if (SiNo.SI.equals(ct.getEsPropPrincipal())) {							
									tramiteRAAdapter.getTramiteRA().setGApellidoORazon(ct.getApellidoORazon());
									tramiteRAAdapter.getTramiteRA().setGDesTipoDoc(ct.getDesTipoDoc());
									tramiteRAAdapter.getTramiteRA().setGNroDoc(ct.getNroDoc());
									tramiteRAAdapter.getTramiteRA().setGNroCuit(ct.getNroCuit());
								}
							}
						}
					}
				}			
			}
			
	    
			// lista para los tipo de motor
			tramiteRAAdapter.getTramiteRA().getETipoMotor().setCodTipoMotor(tramiteRAVO.getECodTipoMotor());
			tramiteRAAdapter.setListETipoMotor( (ArrayList<TipoMotorVO>)
					ListUtilBean.toVO(TipoMotor.getList(),
					new TipoMotorVO()));
			
			// lista para los tipos de carga
		
			// lista para los tipos de pago
			tramiteRAAdapter.getTramiteRA().getITipoPago().setCodPago(tramiteRAVO.getICodPago());
			tramiteRAAdapter.setListTipoPago( (ArrayList<TipoPagoVO>)
					ListUtilBean.toVO(TipoPago.getList(),
					new TipoPagoVO()));
			

	        // Buscamos el historial del estado del tramite
	        List<HisEstTra> listHisEstTra = tramiteRA.getListHisEstTra();
	        tramiteRAAdapter.getTramiteRA().setListHisEstTra((ArrayList<HisEstTraVO>) ListUtilBean.toVO(listHisEstTra,1,false));

	        List<Propietario> listPropietario = Propietario.getByTramiteRA(tramiteRA.getId());
	        tramiteRAAdapter.getTramiteRA().setListPropietario((ArrayList<PropietarioVO>) ListUtilBean.toVO(listPropietario));
	        
	        if(tramiteRAAdapter.getTramiteRA().getDCodLocalidad().equals(Localidad.getRosario().getCodPostal().toString())){
				tramiteRAAdapter.getTramiteRA().getDDomicilio().setLocalidad((LocalidadVO) Localidad.getRosario().toVO(1));			
				tramiteRAAdapter.getTramiteRA().setDDesLocalidad(tramiteRAAdapter.getTramiteRA().getDDomicilio().getLocalidad().getDescripcionPostal());
				tramiteRAAdapter.getTramiteRA().setDCodLocalidad(tramiteRAAdapter.getTramiteRA().getDDomicilio().getLocalidad().getCodPostal().toString());
			}
			
			log.debug(funcName + ": exit");
			return tramiteRAAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TramiteRAAdapter getTramiteRAAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			TramiteRAAdapter tramiteRAAdapter = new TramiteRAAdapter();
			
			// Seteo de banderas
			
			// Seteo la listas para combos, etc
			
			// lista para los tipos de tramite
			tramiteRAAdapter.setListTipoTramite( (ArrayList<TipoTramiteVO>)
					ListUtilBean.toVO(TipoTramite.getList(),
					new TipoTramiteVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// lista para los tipos de fabricacion
			tramiteRAAdapter.setListTipoFabricacion( (ArrayList<TipoFabricacionVO>)
					ListUtilBean.toVO(TipoFabricacion.getList(),
					new TipoFabricacionVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// lista para los modelos
			tramiteRAAdapter.setListModelo( (ArrayList<ModeloVO>)
					ListUtilBean.toVO(Modelo.getList(),
					new ModeloVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// lista para los tipos de uso
			tramiteRAAdapter.setListTipoUso( (ArrayList<TipoUsoVO>)
					ListUtilBean.toVO(TipoUso.getList(),
					new TipoUsoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// lista para los tipo de motor
			tramiteRAAdapter.setListTipoMotor( (ArrayList<TipoMotorVO>)
					ListUtilBean.toVO(TipoMotor.getList(),
					new TipoMotorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// lista para los tipo de motor
			tramiteRAAdapter.setListETipoMotor( (ArrayList<TipoMotorVO>)
					ListUtilBean.toVO(TipoMotor.getList(),
					new TipoMotorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// lista para los tipos de carga
			tramiteRAAdapter.setListTipoCarga( (ArrayList<TipoCargaVO>)
					ListUtilBean.toVO(TipoCarga.getList(),
					new TipoCargaVO("", StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			//lista para los tipos de propietarios
			tramiteRAAdapter.setListTipoPropietario( (ArrayList<TipoPropietarioVO>)
					ListUtilBean.toVO(TipoPropietario.getList(),
					new TipoPropietarioVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// lista para los tipos de doc
			tramiteRAAdapter.setListTipoDoc( (ArrayList<TipoDocVO>)
					ListUtilBean.toVO(TipoDoc.getList(),
					new TipoDocVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
	
			// lista para los estados civiles
			tramiteRAAdapter.setListEstadoCivil( (ArrayList<EstadoCivilVO>)
					ListUtilBean.toVO(EstadoCivil.getList(),
					new EstadoCivilVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			// lista para los tipos de pago
			tramiteRAAdapter.setListTipoPago( (ArrayList<TipoPagoVO>)
					ListUtilBean.toVO(TipoPago.getList(),
					new TipoPagoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// seteo la localidad con la provincia
			tramiteRAAdapter.getTramiteRA().getDDomicilio().setLocalidad((LocalidadVO) Localidad.getRosario().toVO(1));
			
			tramiteRAAdapter.getTramiteRA().setDDesLocalidad(tramiteRAAdapter.getTramiteRA().getDDomicilio().getLocalidad().getDescripcionPostal());
			tramiteRAAdapter.getTramiteRA().setDCodLocalidad(tramiteRAAdapter.getTramiteRA().getDDomicilio().getLocalidad().getCodPostal().toString());
			
			log.debug(funcName + ": exit");
			return tramiteRAAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public TramiteRAAdapter getTramiteRAAdapterParamTipoFabricacion(UserContext userContext, TramiteRAAdapter tramiteRAAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			tramiteRAAdapter.clearError();
			
			// Logica del param
			
			TipoFabricacion tipoFabricacion=null;
			if(tramiteRAAdapter.getTramiteRA().getBTipoFab().getCodFab()!=null){			
				tipoFabricacion = TipoFabricacion.getTipoFabricacionByCodigo(tramiteRAAdapter.getTramiteRA().getBTipoFab().getCodFab());
				tramiteRAAdapter.getTramiteRA().setBTipoFab((TipoFabricacionVO)tipoFabricacion.toVO());
				tramiteRAAdapter.getTramiteRA().setBCodFab(tipoFabricacion.getCodFab());
				tramiteRAAdapter.getTramiteRA().setBDesFab(tipoFabricacion.getDesFab());
			}
			
			log.debug(funcName + ": exit");
			return tramiteRAAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public TramiteRAAdapter getTramiteRAAdapterForUpdate(UserContext userContext, CommonKey commonKeyTramiteRA) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TramiteRA tramiteRA = TramiteRA.getById(commonKeyTramiteRA.getId());
			
	        TramiteRAAdapter tramiteRAAdapter = new TramiteRAAdapter();
	        TramiteRAVO tramiteRAVO = (TramiteRAVO) tramiteRA.toVO(1);
	        tramiteRAAdapter.setTramiteRA(tramiteRAVO);
	        if(tramiteRAVO.getDBis()==1)
	        	tramiteRAAdapter.getTramiteRA().getDDomicilio().setBis(SiNo.SI);
	        else
	        	tramiteRAAdapter.getTramiteRA().getDDomicilio().setBis(SiNo.NO);
	        

			// Seteo la lista para combo, valores, etc
	     // lista para los tipos de tramite
		
			Integer size = TipoTramite.getList().size();
			int i = 0;
			while(i<(size/2)){
				tramiteRAAdapter.getListTipoTramite1().add((TipoTramiteVO)TipoTramite.getList().get(i).toVO());
				i++;
			}
			while(i<size){
				tramiteRAAdapter.getListTipoTramite2().add((TipoTramiteVO)TipoTramite.getList().get(i).toVO());
				i++;
			}
			tramiteRAAdapter.setSizeLista(tramiteRAAdapter.getListTipoTramite().size());
			tramiteRAAdapter.setSizeLista1(tramiteRAAdapter.getListTipoTramite1().size());
			tramiteRAAdapter.setSizeLista2(tramiteRAAdapter.getListTipoTramite2().size());
			
			if(!ListUtil.isNullOrEmpty(tramiteRAAdapter.getTramiteRA().getListPropietario())){
				for (PropietarioVO ct : tramiteRAAdapter.getTramiteRA().getListPropietario()) {		
					ct.setSexo(Sexo.getById(ct.getCodSexo()));
					if(ct.getTipoPropietario().equals(1)){							
						if (SiNo.SI.equals(ct.getEsPropPrincipal())) {	
							tramiteRAAdapter.getTramiteRA().setCApellidoORazon(ct.getApellidoORazon());
							tramiteRAAdapter.getTramiteRA().setCDesTipoDoc(ct.getDesTipoDoc());
							tramiteRAAdapter.getTramiteRA().setCCodTipoDoc(ct.getCodTipoDoc());
							tramiteRAAdapter.getTramiteRA().setCNroDoc(ct.getNroDoc());
							tramiteRAAdapter.getTramiteRA().setCNroIB(ct.getNroIB());
							tramiteRAAdapter.getTramiteRA().setCNroProdAgr(ct.getNroProdAgr());
							tramiteRAAdapter.getTramiteRA().setCNroCuit(ct.getNroCuit());
							tramiteRAAdapter.getTramiteRA().setCCodTipoProp(ct.getCodTipoProp());
							tramiteRAAdapter.getTramiteRA().setCDesTipoProp(ct.getDesTipoProp());
							tramiteRAAdapter.getTramiteRA().setCFechaNac(ct.getFechaNac());
							tramiteRAAdapter.getTramiteRA().setCDesEstCiv(ct.getDesEstCiv());
							tramiteRAAdapter.getTramiteRA().setCCodEstCiv(ct.getCodEstCiv());
							tramiteRAAdapter.getTramiteRA().setCDesSexo(ct.getDesSexo());
						}
					}else{
						if(ct.getTipoPropietario().equals(2)){
							if (SiNo.SI.equals(ct.getEsPropPrincipal())) {							
								tramiteRAAdapter.getTramiteRA().setGApellidoORazon(ct.getApellidoORazon());
								tramiteRAAdapter.getTramiteRA().setGDesTipoDoc(ct.getDesTipoDoc());
								tramiteRAAdapter.getTramiteRA().setGNroDoc(ct.getNroDoc());
							}
						}
					}
				}			
			}
		
	        // lista para los tipos de fabricacion
	        tramiteRAAdapter.getTramiteRA().getBTipoFab().setCodFab(tramiteRAVO.getBCodFab());
			tramiteRAAdapter.setListTipoFabricacion( (ArrayList<TipoFabricacionVO>)
					ListUtilBean.toVO(TipoFabricacion.getList(),
					new TipoFabricacionVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// lista para los modelos
			tramiteRAAdapter.getTramiteRA().getBModelo().setCodModelo(tramiteRAVO.getBCodModelo());
			tramiteRAAdapter.setListModelo( (ArrayList<ModeloVO>)
					ListUtilBean.toVO(Modelo.getList(),
					new ModeloVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// lista para los tipos de uso
			tramiteRAAdapter.getTramiteRA().getBTipoUso().setCodUso(tramiteRAVO.getBCodUso());
			tramiteRAAdapter.setListTipoUso( (ArrayList<TipoUsoVO>)
					ListUtilBean.toVO(TipoUso.getList(),
					new TipoUsoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// lista para los tipo de motor
			tramiteRAAdapter.getTramiteRA().getBTM().setCodTipoMotor(tramiteRAVO.getBCodTipoMotor());
			tramiteRAAdapter.setListTipoMotor( (ArrayList<TipoMotorVO>)
					ListUtilBean.toVO(TipoMotor.getList(),
					new TipoMotorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// lista para los tipo de motor
			tramiteRAAdapter.getTramiteRA().getETipoMotor().setCodTipoMotor(tramiteRAVO.getECodTipoMotor());
			tramiteRAAdapter.setListETipoMotor( (ArrayList<TipoMotorVO>)
					ListUtilBean.toVO(TipoMotor.getList(),
					new TipoMotorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// lista para los tipos de carga
			tramiteRAAdapter.getTramiteRA().getBTipoCarga().setCodTipoCarga(tramiteRAVO.getBCodTipoCarga());
			tramiteRAAdapter.setListTipoCarga( (ArrayList<TipoCargaVO>)
					ListUtilBean.toVO(TipoCarga.getList(),
					new TipoCargaVO("", StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			
			// lista para los tipos de pago
			tramiteRAAdapter.getTramiteRA().getITipoPago().setCodPago(tramiteRAVO.getICodPago());
			tramiteRAAdapter.setListTipoPago( (ArrayList<TipoPagoVO>)
					ListUtilBean.toVO(TipoPago.getList(),
					new TipoPagoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// seteo la localidad con la provincia
			
			if(tramiteRAAdapter.getTramiteRA().getDCodLocalidad().equals(Localidad.getRosario().getCodPostal().toString())){
				tramiteRAAdapter.getTramiteRA().getDDomicilio().setLocalidad((LocalidadVO) Localidad.getRosario().toVO(1));			
				tramiteRAAdapter.getTramiteRA().setDDesLocalidad(tramiteRAAdapter.getTramiteRA().getDDomicilio().getLocalidad().getDescripcionPostal());
				tramiteRAAdapter.getTramiteRA().setDCodLocalidad(tramiteRAAdapter.getTramiteRA().getDDomicilio().getLocalidad().getCodPostal().toString());
			}
			log.debug(funcName + ": exit");
			return tramiteRAAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TramiteRAVO createTramiteRA(UserContext userContext, TramiteRAVO tramiteRAVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tramiteRAVO.clearErrorMessages();
			
			Integer nroTramite = tramiteRAVO.getNroTramite();
			if(tramiteRAVO.getNroTramite() == null || nroTramite.intValue() < 0){
				tramiteRAVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,RodError.TRAMITERA_NUMERO_TRAMITERA);
			}
			
			/*if(tramiteRAVO.getNroComuna()< 0) {
				tramiteRAVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,RodError.TRAMITERA_NUMERO_COMUNA);
			}*/
			if(tramiteRAVO.getTipoTramite().getCodTipoTramite() < 0){
				tramiteRAVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,RodError.TRAMITERA_TIPOTRAMITE);
			}
			if(tramiteRAVO.getFecha()==null){
				tramiteRAVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,RodError.TRAMITERA_FECHA);
			}
			
			if(tramiteRAVO.hasError()){
				return tramiteRAVO;
			}
			// Copiado de propiadades de VO al BO
            TramiteRA tramiteRA = new TramiteRA();
            
            // valores generales
            tramiteRA.setNroComuna(tramiteRAVO.getNroComuna());
            tramiteRA.setNroTramite(tramiteRAVO.getNroTramite());
            tramiteRA.setCodTipoTramite(tramiteRAVO.getCodTipoTramite());
            tramiteRA.setDesTipoTramite(tramiteRAVO.getDesTipoTramite());
            tramiteRA.setFecha(tramiteRAVO.getFecha());
            tramiteRA.setObservacion(tramiteRAVO.getObservacion());
            tramiteRA.setObservacionAPI(tramiteRAVO.getObservacionAPI());
            tramiteRA.setObservacionRNPA(tramiteRAVO.getObservacionRNPA());
            tramiteRA.setRubros(tramiteRAVO.getRubros());
            
            // seteo valores de tipo A
            tramiteRA.setADigVerif(tramiteRAVO.getADigVerif());
            tramiteRA.setANroPatente(StringUtil.escaparUpper(tramiteRAVO.getANroPatente()));
            
            // seteo valores de tipo B
            tramiteRA.setBAltoCarr(tramiteRAVO.getBAltoCarr());
            tramiteRA.setBAnio(tramiteRAVO.getBAnio());
            tramiteRA.setBAsientos(tramiteRAVO.getBAsientos());
            tramiteRA.setBCantEjes(tramiteRAVO.getBCantEjes());
            tramiteRA.setBCantidad(tramiteRAVO.getBCantidad());
            tramiteRA.setBCapCarga(tramiteRAVO.getBCapCarga());
            tramiteRA.setBCodFab(tramiteRAVO.getBCodFab());
            tramiteRA.setBCodMarca(tramiteRAVO.getBCodMarca());
            tramiteRA.setBCodModelo(tramiteRAVO.getBCodModelo());
            tramiteRA.setBCodTipoCarga(tramiteRAVO.getBCodTipoCarga());
            tramiteRA.setBCodTipoVeh(tramiteRAVO.getBCodTipoVeh());
            tramiteRA.setBCodTipoMotor(tramiteRAVO.getBCodTipoMotor());
            tramiteRA.setBCodUso(tramiteRAVO.getBCodUso());
            tramiteRA.setBDedModelo(tramiteRAVO.getBDesModelo());
            tramiteRA.setBDesMarca(tramiteRAVO.getBDesMarca());
            tramiteRA.setBDesUso(tramiteRAVO.getBDesUso());
            tramiteRA.setBFechaFactura(tramiteRAVO.getBFechaFactura());
            tramiteRA.setBHPoC(tramiteRAVO.getBHPoC());
            tramiteRA.setBLargoCarr(tramiteRAVO.getBLargoCarr());
            tramiteRA.setBNroMotor(tramiteRAVO.getBNroMotor());
            tramiteRA.setBPesoCarga(tramiteRAVO.getBPesoCarga());
            tramiteRA.setBPesoVacio(tramiteRAVO.getBPesoVacio());
            tramiteRA.setBPrecioAlta(tramiteRAVO.getBPrecioAlta());
            tramiteRA.setBUnidadMedida(tramiteRAVO.getBUnidadMedida());
            tramiteRA.setBDesTipoCarga(tramiteRAVO.getBDesTipoCarga());
            tramiteRA.setBDesTipoVeh(tramiteRAVO.getBDesTipoVeh());
            tramiteRA.setBDesFab(tramiteRAVO.getBDesFab());
            tramiteRA.setBDesTipoMotor(tramiteRAVO.getBDesTipoMotor());
            
            // seteo valores de tipo C
            tramiteRA.setCCantDuenios(tramiteRAVO.getCCantDuenios());
            tramiteRA.setCIdPersonaActual(tramiteRAVO.getCPersonaActual().getId());
            
            // seteo los valores de tipo D
            tramiteRA.setDCodCalle(tramiteRAVO.getDCodCalle());
            tramiteRA.setDDesCalle(tramiteRAVO.getDDesCalle());
            tramiteRA.setDDpto(tramiteRAVO.getDDpto());
            tramiteRA.setDEsValido(tramiteRAVO.getDEsValido());
            
            tramiteRA.setDNumero(tramiteRAVO.getDNumero());
            tramiteRA.setDPiso(tramiteRAVO.getDPiso());
            tramiteRA.setDDpto(tramiteRAVO.getDDpto());
            tramiteRA.setDBis(tramiteRAVO.getDDomicilio().getBis().getId());
            tramiteRA.setDDesLocalidad(tramiteRAVO.getDDesLocalidad());
            tramiteRA.setDCodLocalidad(tramiteRAVO.getDCodLocalidad());
            
            
            // seteo los valores de tipo E
            tramiteRA.setECodTipoMotor(tramiteRAVO.getECodTipoMotor());
            tramiteRA.setEDesMarca(tramiteRAVO.getEDesMarca());
            tramiteRA.setEDesTipoMotor(tramiteRAVO.getEDesTipoMotor());
            tramiteRA.setENroMotor(tramiteRAVO.getENroMotor());
            
            // seteo lo valores de tipo F
            tramiteRA.setFDesMotivoBaja(tramiteRAVO.getFDesMotivoBaja());
            
            // seteo los valores de tipo G
            tramiteRA.setGDesDomicilio(tramiteRAVO.getGDesDomicilio());
            
            // seteo los valores de tipo H
            tramiteRA.setHPatenteCorr(StringUtil.escaparUpper(tramiteRAVO.getHPatenteCorr()));
            tramiteRA.setHPatentePad(StringUtil.escaparUpper(tramiteRAVO.getHPatentePad()));
            
            // seteo los valores de tipo I
            tramiteRA.setICodPago(tramiteRAVO.getICodPago());
            tramiteRA.setIDesPago(tramiteRAVO.getIDesPago());
            tramiteRA.setIDesBancoMuni(tramiteRAVO.getIDesBancoMuni());
            tramiteRA.setIFecha1(tramiteRAVO.getIFecha1());
            tramiteRA.setIFecha2(tramiteRAVO.getIFecha2());
            tramiteRA.setIImporte1(tramiteRAVO.getIImporte1());
            tramiteRA.setIImporte2(tramiteRAVO.getIImporte2());
            
            // seteo en estado INGRESADA
            EstadoTramiteRA estadoTramiteRA = EstadoTramiteRA.getById(EstadoTramiteRA.ID_INGRESADO); 
            estadoTramiteRA.setEsEstado(1);
          
            tramiteRA.setEstTra(estadoTramiteRA);
            
            Mandatario mandatario = Mandatario.getByIdNull(userContext.getIdMandatario());
            tramiteRA.setMandatario(mandatario);
            
            tramiteRA = RodTramiteManager.getInstance().createTramiteRA(tramiteRA,tramiteRAVO.getLogCambios());
            
            tramiteRA.setEstado(Estado.ACTIVO.getId());

            if(tramiteRAVO.hasError()){
	            tx.rollback();
		  		return tramiteRAVO;	  		
		  	}			
            boolean hayActual = false;
            boolean hayActualPrincipal = false;
            boolean hayAnterior = false;
            boolean hayAnteriorPrincipal = false;
            int cantPropAct = 0;
            int cantPropAnt = 0;
            Propietario proAct=null;
            Propietario proAnt=null;
            for(PropietarioVO propietarioVO: tramiteRAVO.getListPropietario()){      
            	Propietario propietario = new Propietario();
    			
    			propietario.setApellidoORazon(StringUtil.escaparUpper(propietarioVO.getApellidoORazon()));
    			propietario.setCodEstCiv(propietarioVO.getCodEstCiv());
    			propietario.setCodSexo(propietarioVO.getSexo().getId());
    			propietario.setCodTipoDoc(propietarioVO.getCodTipoDoc());
    			propietario.setDesEstCiv(propietarioVO.getDesEstCiv());
    			propietario.setDesSexo(propietarioVO.getSexo().getValue());
    			propietario.setDesTipoProp(propietarioVO.getDesTipoProp());
    			propietario.setDesTipoDoc(propietarioVO.getDesTipoDoc());
    			propietario.setNroDoc(propietarioVO.getNroDoc());
    			propietario.setNroCuit(propietarioVO.getNroCuit());
    			propietario.setNroIB(propietarioVO.getNroIB());
    			propietario.setNroProdAgr(propietarioVO.getNroProdAgr());
    			propietario.setFechaNac(propietarioVO.getFechaNac());			
    			propietario.setTipoPropietario(propietarioVO.getTipoPropietario());
    			propietario.setTramiteRA(tramiteRA);
    			propietario.setEsPropPrincipal(propietarioVO.getEsPropPrincipal().getId());
    			
    			
    			if(propietario.getTipoPropietario().equals(1)){
        			hayActual=true;
        			cantPropAct++;
        			proAct=propietario;
            		if(!hayActualPrincipal &&  propietario.getEsPropPrincipal().equals(1)){
            			hayActualPrincipal=true;
            		}
        		}
        		if(propietario.getTipoPropietario().equals(2)){
        			hayAnterior=true;
        			cantPropAnt++;
        			proAnt=propietario;
            		if(!hayAnteriorPrincipal && propietario.getEsPropPrincipal().equals(1)){
            			hayAnteriorPrincipal=true;
            		}
        		}
    			propietario = tramiteRA.createPropietario(propietario);
            	
    			if(propietario.hasError()){
    				propietario.passErrorMessages(tramiteRA);
    				break;    				
    			}
            }
            
            if(cantPropAct==1){
            	proAct = Propietario.getById(proAct.getId());
            	proAct.setEsPropPrincipal(1);
            	tramiteRA.updatePropietario(proAct);
            }
            if(cantPropAnt==1){
            	proAnt = Propietario.getById(proAnt.getId());
            	proAnt.setEsPropPrincipal(1);
            	tramiteRA.updatePropietario(proAnt);
            }
            if(cantPropAct > 1 && hayActual && !hayActualPrincipal){
            	tx.rollback();
            	tramiteRAVO.addRecoverableError(RodError.AGREGAR_PROPIETARIOACTUAL_PRINCIPAL);
            	return tramiteRAVO;
            }
            if(cantPropAnt > 1 && hayAnterior && !hayAnteriorPrincipal){
            	tx.rollback();
            	tramiteRAVO.addRecoverableError(RodError.AGREGAR_PROPIETARIOANTERIOR_PRINCIPAL);
            	return tramiteRAVO;
            }
            
	        if (tramiteRA.hasError()) {
	        	tx.rollback();
	        	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tramiteRAVO =  (TramiteRAVO) tramiteRA.toVO(2,false);
			}
	        
			tramiteRA.passErrorMessages(tramiteRAVO);
            
            log.debug(funcName + ": exit");
            return tramiteRAVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TramiteRAVO updateTramiteRA(UserContext userContext, TramiteRAVO tramiteRAVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tramiteRAVO.clearErrorMessages();
			
			if(tramiteRAVO.getFecha()==null){
				tramiteRAVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,RodError.TRAMITERA_FECHA);
			}
			// Copiado de propiadades de VO al BO
            TramiteRA tramiteRA = TramiteRA.getById(tramiteRAVO.getId());
			
			if(!tramiteRAVO.validateVersion(tramiteRA.getFechaUltMdf())) return tramiteRAVO;

			// valores generales
            tramiteRA.setNroComuna(tramiteRAVO.getNroComuna());
            tramiteRA.setNroTramite(tramiteRAVO.getNroTramite());
            tramiteRA.setCodTipoTramite(tramiteRAVO.getCodTipoTramite());
            tramiteRA.setDesTipoTramite(tramiteRAVO.getDesTipoTramite());
            tramiteRA.setFecha(tramiteRAVO.getFecha());
            tramiteRA.setObservacion(tramiteRAVO.getObservacion());
            tramiteRA.setObservacionAPI(tramiteRAVO.getObservacionAPI());
            tramiteRA.setObservacionRNPA(tramiteRAVO.getObservacionRNPA());
            tramiteRA.setRubros(tramiteRAVO.getRubros());
            
            // seteo valores de tipo A
            tramiteRA.setADigVerif(tramiteRAVO.getADigVerif());
            tramiteRA.setANroPatente(tramiteRAVO.getANroPatente());
            
            // seteo valores de tipo B
            tramiteRA.setBAltoCarr(tramiteRAVO.getBAltoCarr());
            tramiteRA.setBAnio(tramiteRAVO.getBAnio());
            tramiteRA.setBAsientos(tramiteRAVO.getBAsientos());
            tramiteRA.setBCantEjes(tramiteRAVO.getBCantEjes());
            tramiteRA.setBCantidad(tramiteRAVO.getBCantidad());
            tramiteRA.setBCapCarga(tramiteRAVO.getBCapCarga());
            tramiteRA.setBCodFab(tramiteRAVO.getBCodFab());
            tramiteRA.setBCodMarca(tramiteRAVO.getBCodMarca());
            tramiteRA.setBCodModelo(tramiteRAVO.getBCodModelo());
            tramiteRA.setBCodTipoCarga(tramiteRAVO.getBCodTipoCarga());
            tramiteRA.setBCodTipoVeh(tramiteRAVO.getBCodTipoVeh());
            tramiteRA.setBCodTipoMotor(tramiteRAVO.getBCodTipoMotor());
            tramiteRA.setBCodUso(tramiteRAVO.getBCodUso());
            tramiteRA.setBDedModelo(tramiteRAVO.getBDesModelo());
            tramiteRA.setBDesMarca(tramiteRAVO.getBDesMarca());
            tramiteRA.setBDesUso(tramiteRAVO.getBDesUso());
            tramiteRA.setBFechaFactura(tramiteRAVO.getBFechaFactura());
            tramiteRA.setBHPoC(tramiteRAVO.getBHPoC());
            tramiteRA.setBLargoCarr(tramiteRAVO.getBLargoCarr());
            tramiteRA.setBNroMotor(tramiteRAVO.getBNroMotor());
            tramiteRA.setBPesoCarga(tramiteRAVO.getBPesoCarga());
            tramiteRA.setBPesoVacio(tramiteRAVO.getBPesoVacio());
            tramiteRA.setBPrecioAlta(tramiteRAVO.getBPrecioAlta());
            tramiteRA.setBUnidadMedida(tramiteRAVO.getBUnidadMedida());
            tramiteRA.setBDesTipoCarga(tramiteRAVO.getBDesTipoCarga());
            tramiteRA.setBDesTipoVeh(tramiteRAVO.getBDesTipoVeh());
            tramiteRA.setBDesFab(tramiteRAVO.getBDesFab());
            tramiteRA.setBDesTipoMotor(tramiteRAVO.getBDesTipoMotor());
            
            // seteo valores de tipo C
            tramiteRA.setCCantDuenios(tramiteRAVO.getCCantDuenios());
            tramiteRA.setCIdPersonaActual(tramiteRAVO.getCPersonaActual().getId());
            
            // seteo los valores de tipo D
            tramiteRA.setDCodCalle(tramiteRAVO.getDCodCalle());
            tramiteRA.setDDesCalle(tramiteRAVO.getDDesCalle());
            tramiteRA.setDDpto(tramiteRAVO.getDDpto());
            //tramiteRA.setDIdDomicilio(tramiteRAVO.getDDomicilio().getId());
            tramiteRA.setDNumero(tramiteRAVO.getDNumero());
            tramiteRA.setDPiso(tramiteRAVO.getDPiso());
            tramiteRA.setDDpto(tramiteRAVO.getDDpto());
            tramiteRA.setDBis(tramiteRAVO.getDDomicilio().getBis().getId());
            tramiteRA.setDDesLocalidad(tramiteRAVO.getDDesLocalidad());
            tramiteRA.setDCodLocalidad(tramiteRAVO.getDCodLocalidad());
            tramiteRA.setDEsValido(tramiteRAVO.getDEsValido());
           
            // seteo los valores de tipo E
            tramiteRA.setECodTipoMotor(tramiteRAVO.getECodTipoMotor());
            tramiteRA.setEDesMarca(tramiteRAVO.getEDesMarca());
            tramiteRA.setEDesTipoMotor(tramiteRAVO.getEDesTipoMotor());
            tramiteRA.setENroMotor(tramiteRAVO.getENroMotor());
            
            // seteo lo valores de tipo F
            tramiteRA.setFDesMotivoBaja(tramiteRAVO.getFDesMotivoBaja());
            
            // seteo los valores de tipo G
            tramiteRA.setGDesDomicilio(tramiteRAVO.getGDesDomicilio());
            
            // seteo los valores de tipo H
            tramiteRA.setHPatenteCorr(tramiteRAVO.getHPatenteCorr());
            tramiteRA.setHPatentePad(tramiteRAVO.getHPatentePad());
            
            // seteo los valores de tipo I
            tramiteRA.setICodPago(tramiteRAVO.getICodPago());
            tramiteRA.setIDesPago(tramiteRAVO.getIDesPago());
            tramiteRA.setIDesBancoMuni(tramiteRAVO.getIDesBancoMuni());
            tramiteRA.setIFecha1(tramiteRAVO.getIFecha1());
            tramiteRA.setIFecha2(tramiteRAVO.getIFecha2());
            tramiteRA.setIImporte1(tramiteRAVO.getIImporte1());
            tramiteRA.setIImporte2(tramiteRAVO.getIImporte2());

			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            tramiteRA = RodTramiteManager.getInstance().updateTramiteRA(tramiteRA);
            
            if(tramiteRA.hasError()){
            	tx.rollback();
            	tramiteRA.passErrorMessages(tramiteRAVO);
            	return tramiteRAVO;
            }
            
            boolean hayActualPrincipal = false;
            boolean hayActual = false;
            boolean hayAnteriorPrincipal = false;
            boolean hayAnterior = false;
            int cantPropAct = 0;
            int cantPropAnt = 0;
            Propietario proAct = null;
            Propietario proAnt = null;
            for(PropietarioVO propietarioVO: tramiteRAVO.getListPropietario()){      
            	
            	Propietario propietario = Propietario.getByDocTipoPropietario(propietarioVO.getNroDoc(),propietarioVO.getTipoPropietario(),tramiteRA.getId());
    			
            	if(propietario!=null){
            		if(propietario.getTipoPropietario().equals(1)){
            			hayActual=true;
	            		if(!hayActualPrincipal && propietario.getEsPropPrincipal().equals(1)){
	            			hayActualPrincipal=true;
	            		}
            		}
            		if(!hayAnteriorPrincipal && propietario.getTipoPropietario().equals(2)){
            			hayAnterior=true;
	            		if(propietario.getEsPropPrincipal().equals(1)){
	            			hayAnteriorPrincipal=true;
	            		}
            		}
	    			propietario.setApellidoORazon(propietarioVO.getApellidoORazon());
	    			propietario.setCodEstCiv(propietarioVO.getCodEstCiv());
	    			propietario.setCodSexo(propietarioVO.getSexo().getId());
	    			propietario.setCodTipoDoc(propietarioVO.getCodTipoDoc());
	    			propietario.setDesEstCiv(propietarioVO.getDesEstCiv());
	    			propietario.setDesSexo(propietarioVO.getSexo().getValue());
	    			propietario.setDesTipoProp(propietarioVO.getDesTipoProp());
	    			propietario.setDesTipoDoc(propietarioVO.getDesTipoDoc());
	    			propietario.setNroDoc(propietarioVO.getNroDoc());
	    			propietario.setNroCuit(propietarioVO.getNroCuit());
	    			propietario.setNroIB(propietarioVO.getNroIB());
	    			propietario.setNroProdAgr(propietarioVO.getNroProdAgr());
	    			propietario.setFechaNac(propietarioVO.getFechaNac());			
	    			propietario.setTipoPropietario(propietarioVO.getTipoPropietario());
	    			//propietario.setTramiteRA(tramiteRA);
	    			propietario.setEsPropPrincipal(propietarioVO.getEsPropPrincipal().getId());
            	
	    			propietario = tramiteRA.updatePropietario(propietario);
            	
            	}else{
            		propietario = new Propietario();            		
            		propietario.setApellidoORazon(propietarioVO.getApellidoORazon());
	    			propietario.setCodEstCiv(propietarioVO.getCodEstCiv());
	    			propietario.setCodSexo(propietarioVO.getSexo().getId());
	    			propietario.setCodTipoDoc(propietarioVO.getCodTipoDoc());
	    			propietario.setDesEstCiv(propietarioVO.getDesEstCiv());
	    			propietario.setDesSexo(propietarioVO.getSexo().getValue());
	    			propietario.setDesTipoProp(propietarioVO.getDesTipoProp());
	    			propietario.setDesTipoDoc(propietarioVO.getDesTipoDoc());
	    			propietario.setNroDoc(propietarioVO.getNroDoc());
	    			propietario.setNroCuit(propietarioVO.getNroCuit());
	    			propietario.setNroIB(propietarioVO.getNroIB());
	    			propietario.setNroProdAgr(propietarioVO.getNroProdAgr());
	    			propietario.setFechaNac(propietarioVO.getFechaNac());			
	    			propietario.setTipoPropietario(propietarioVO.getTipoPropietario());
	    			propietario.setTramiteRA(tramiteRA);
	    			propietario.setEsPropPrincipal(propietarioVO.getEsPropPrincipal().getId());
	    			if(propietario.getTipoPropietario().equals(1)){
            			hayActual=true;
            			cantPropAct++;
            			proAct=propietario;
	            		if(propietario.getEsPropPrincipal().equals(1)){
	            			hayActualPrincipal=true;
	            		}
            		}
            		if(propietario.getTipoPropietario().equals(2)){
            			hayAnterior=true;
            			cantPropAnt++;
            			proAnt=propietario;
	            		if(propietario.getEsPropPrincipal().equals(1)){
	            			hayAnteriorPrincipal=true;
	            		}
            		}
	    			propietario = tramiteRA.createPropietario(propietario);
            	}
            	
    			if(propietario.hasError()){
    				propietario.passErrorMessages(tramiteRA);
    				break;    				
    			}
            }
            
            if(cantPropAct==1){            	
            	proAct = Propietario.getById(proAct.getId());
            	proAct.setEsPropPrincipal(1);
            	tramiteRA.updatePropietario(proAct);
            }
            if(cantPropAnt==1){
            	proAnt = Propietario.getById(proAnt.getId());
            	proAnt.setEsPropPrincipal(1);
            	tramiteRA.updatePropietario(proAnt);
            }
            if(cantPropAct > 1 && hayActual && !hayActualPrincipal){
            	tx.rollback();
            	tramiteRAVO.addRecoverableError(RodError.AGREGAR_PROPIETARIOACTUAL_PRINCIPAL);
            	return tramiteRAVO;
            }
            if(cantPropAnt > 1 && hayAnterior && !hayAnteriorPrincipal){
            	tx.rollback();
            	tramiteRAVO.addRecoverableError(RodError.AGREGAR_PROPIETARIOANTERIOR_PRINCIPAL);
            	return tramiteRAVO;
            }
            if (tramiteRA.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tramiteRAVO =  (TramiteRAVO) tramiteRA.toVO(0,false);
			}
			tramiteRA.passErrorMessages(tramiteRAVO);
            
            log.debug(funcName + ": exit");
            return tramiteRAVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TramiteRAVO deleteTramiteRA(UserContext userContext, TramiteRAVO tramiteRAVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tramiteRAVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			TramiteRA tramiteRA = TramiteRA.getById(tramiteRAVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			tramiteRA = RodTramiteManager.getInstance().deleteTramiteRA(tramiteRA);
			
			if (tramiteRA.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tramiteRAVO =  (TramiteRAVO) tramiteRA.toVO(0,false);
			}
			tramiteRA.passErrorMessages(tramiteRAVO);
            
            log.debug(funcName + ": exit");
            return tramiteRAVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TramiteRAAdapter getTramiteRAAdapterParamTipoTramite(UserContext userContext, TramiteRAAdapter tramiteRAAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			tramiteRAAdapter.clearError();
			
			// Logica del param
			
			//si no es nulo lo seleccionado
			
			TipoTramite tipoTramite=null;
			if(tramiteRAAdapter.getTramiteRA().getTipoTramite().getCodTipoTramite()!=null){			
				tipoTramite = TipoTramite.getTipoTramiteByCodigo(tramiteRAAdapter.getTramiteRA().getTipoTramite().getCodTipoTramite());
				tramiteRAAdapter.getTramiteRA().setTipoTramite((TipoTramiteVO)tipoTramite.toVO());
				tramiteRAAdapter.getTramiteRA().setCodTipoTramite(tipoTramite.getCodTipoTramite());
				tramiteRAAdapter.getTramiteRA().setDesTipoTramite(tipoTramite.getDesTipoTramite());
				tramiteRAAdapter.getTramiteRA().setRubros(tipoTramite.getRubros());
			}
		
			log.debug(funcName + ": exit");
			return tramiteRAAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}


	public TramiteRAAdapter getTramiteRAAdapterParamModelo(UserContext userContext, TramiteRAAdapter tramiteRAAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

	if (log.isDebugEnabled()) log.debug(funcName + ": enter");
	try {
		DemodaUtil.setCurrentUserContext(userContext);
		SiatHibernateUtil.currentSession();

		tramiteRAAdapter.clearError();

		Integer codigo = tramiteRAAdapter.getTramiteRA().getBModelo().getCodModelo();

		// recupero el modelo
		Modelo modelo = Modelo.getModeloByCodigo(codigo);
		ModeloVO modeloVO = new ModeloVO();
		
		modeloVO =(ModeloVO) modelo.toVO(1);
		
		//busco el tipo de vehiculo
		TipoVehiculo tipoVeh = TipoVehiculo.getTipoVehiculoByCodGenCodEsp(modelo.getCodTipoGen(),modelo.getCodTipoEsp());
		
		Marca marca = Marca.getMarcaByCodigo(modelo.getCodMarca());
		
		tramiteRAAdapter.getTramiteRA().setBTipoVeh((TipoVehiculoVO)tipoVeh.toVO());
		tramiteRAAdapter.getTramiteRA().setBCodTipoVeh(modelo.getCodTipoEsp());
		tramiteRAAdapter.getTramiteRA().setBDesTipoVeh(tipoVeh.getDesTipoEsp());
		
		tramiteRAAdapter.getTramiteRA().setBMarca((MarcaVO)marca.toVO());
		tramiteRAAdapter.getTramiteRA().setBCodMarca(modelo.getCodMarca());
		tramiteRAAdapter.getTramiteRA().setBDesMarca(marca.getDesMarca());
		
		tramiteRAAdapter.getTramiteRA().setBModelo(modeloVO);
		tramiteRAAdapter.getTramiteRA().setBDesModelo(modelo.getDesModelo());
		tramiteRAAdapter.getTramiteRA().setBCodModelo(modelo.getCodModelo());
		
		// si la persona
		if (modelo != null) {
			modeloVO = (ModeloVO) modelo.toVO();
		}

		tramiteRAAdapter.getTramiteRA().setBModelo(modeloVO);
		
		log.debug(funcName + ": exit");
		return tramiteRAAdapter;
	} catch (Exception e) {
		log.error("Service Error: ",  e);
		throw new DemodaServiceException(e);
	} finally {
		SiatHibernateUtil.closeSession();
	}	
}

	// <--- ABM TramiteRA
	
	/**
	 * Obtiene el Search Page para la busqueda de modelos
	 */
	public ModeloSearchPage getModeloSearchPageInit(UserContext userContext,Integer codigo, String descrip) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			
			ModeloSearchPage modeloSearchPage = new ModeloSearchPage();
			
			//personaSearchPage.setListLetraCuit(LetraCuit.getList());
			// Aqui obtiene lista de BOs
			List<Modelo> listModelo=Modelo.getModeloByCodDes(codigo,descrip);
			
			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		modeloSearchPage.setListResult(ListUtilBean.toVO(listModelo,1));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return modeloSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/**
	 * Realiza la busqueda de modelo
	 * 
	 */
	public ModeloSearchPage getModeloSearchPageResult(UserContext userContext,ModeloSearchPage modeloSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			List<Modelo> listModelo = Modelo.getModeloByCodDes(modeloSearchPage.getModelo().getCodModelo(),modeloSearchPage.getModelo().getDesModelo());	
			
			modeloSearchPage.setListResult(ListUtilBean.toVO(listModelo,1));
			
			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return modeloSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	
	public TramiteRAAdapter getTramiteRAAdapterParamPersonaActual(UserContext userContext, TramiteRAAdapter tramiteRAAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
	
			tramiteRAAdapter.clearError();
	
			Long codigo = tramiteRAAdapter.getTramiteRA().getCPersonaActual().getId();
	
			// recupero la persona
			Persona persona  = Persona.getById(codigo);
			PersonaVO personaVO = (PersonaVO)persona.toVO(2);
			tramiteRAAdapter.getTramiteRA().setCPersonaActual(personaVO);
			tramiteRAAdapter.getTramiteRA().setCApellidoORazon(personaVO.getView());
			tramiteRAAdapter.getTramiteRA().setCNroCuit(personaVO.getCuit());
			tramiteRAAdapter.getTramiteRA().setCDesSexo(personaVO.getSexo().getValue());
			
			tramiteRAAdapter.getTramiteRA().setCNroDoc(personaVO.getDocumento().getNumero());
			// domicilio
			tramiteRAAdapter.getTramiteRA().setDDomicilio(personaVO.getDomicilio());
			tramiteRAAdapter.getTramiteRA().setDDesCalle(personaVO.getDomicilio().getCalle().getNombreCalle());
			tramiteRAAdapter.getTramiteRA().setDNumero(personaVO.getDomicilio().getNumero());
			tramiteRAAdapter.getTramiteRA().setDPiso(personaVO.getDomicilio().getPiso());
			tramiteRAAdapter.getTramiteRA().setDDpto(personaVO.getDomicilio().getDepto());
			tramiteRAAdapter.getTramiteRA().setDBis(personaVO.getDomicilio().getBis().getId());

			
			log.debug(funcName + ": exit");
			return tramiteRAAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public DomicilioAdapter getTramiteRAAdapterParamLocalidad(UserContext userContext, DomicilioAdapter domicilioAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			// Logica del param
			
			//si no es nulo lo seleccionado
			
			
			Localidad localidad = UbicacionFacade.getInstance().getLocalidad(domicilioAdapter.getDomicilio().getLocalidad().getId()); 				
			domicilioAdapter.getDomicilio().setLocalidad((LocalidadVO) localidad.toVO(1)); // tiene que incluir a la provincia
			
			log.debug(funcName + ": exit");
			return domicilioAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public PropietarioAdapter getPropietarioAdapterParamTipoDoc(UserContext userContext, PropietarioAdapter propietarioAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			propietarioAdapter.clearError();
			
			// Logica del param
			
			//si no es nulo lo seleccionado
			
			TipoDoc tipoDoc=null;
			if(propietarioAdapter.getPropietario().getCodTipoDoc()!=null){			
				tipoDoc = TipoDoc.getTipoDocByCodigo(propietarioAdapter.getPropietario().getCodTipoDoc());	
				propietarioAdapter.getPropietario().setDesTipoDoc(tipoDoc.getDesTipoDoc());
				propietarioAdapter.getPropietario().setCodTipoDoc(tipoDoc.getCodTipoDoc());
			}
		
			log.debug(funcName + ": exit");
			return propietarioAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public PropietarioAdapter getPropietarioAdapterParamEstadoCivil(UserContext userContext, PropietarioAdapter propietarioAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			propietarioAdapter.clearError();
			
			// Logica del param
			
			//si no es nulo lo seleccionado
			
			EstadoCivil estadoCivil=null;
			if(propietarioAdapter.getPropietario().getCodEstCiv()!=null){			
				estadoCivil = EstadoCivil.getEstadoCivilByCodigo(propietarioAdapter.getPropietario().getCodEstCiv());
				propietarioAdapter.getPropietario().setCodEstCiv(estadoCivil.getCodEstCiv());
				propietarioAdapter.getPropietario().setDesEstCiv(estadoCivil.getDesEstCiv());
			}
		
			log.debug(funcName + ": exit");
			return propietarioAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

/*
	public PropietarioAdapter getPropietarioAdapterParamTipoSexo(UserContext userContext, PropietarioAdapter propietarioAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			propietarioAdapter.clearError();
			
			// Logica del param
			
			//si no es nulo lo seleccionado
			
			TipoSexo tipoSexo=null;
			if(propietarioAdapter.getPropietario().getCodSexo()!=null){			
				tipoSexo = TipoSexo.getTipoSexoByCodigo(propietarioAdapter.getPropietario().getCodSexo());		
				propietarioAdapter.getPropietario().setCodSexo(tipoSexo.getCodTipoSexo());
				propietarioAdapter.getPropietario().setDesSexo(tipoSexo.getDesTipoSexo());
			}
		
			log.debug(funcName + ": exit");
			return propietarioAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}*/

	public TramiteRAAdapter getTramiteRAAdapterParamTipoDocPropAnterior(UserContext userContext, TramiteRAAdapter tramiteRAAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			tramiteRAAdapter.clearError();
			
			// Logica del param
			
			//si no es nulo lo seleccionado
			
			TipoDoc tipoDoc=null;
			if(tramiteRAAdapter.getTramiteRA().getGTipoDoc().getCodTipoDoc()!=null){			
				tipoDoc = TipoDoc.getTipoDocByCodigo(tramiteRAAdapter.getTramiteRA().getGTipoDoc().getCodTipoDoc());
				tramiteRAAdapter.getTramiteRA().setGTipoDoc((TipoDocVO)tipoDoc.toVO());	
				tramiteRAAdapter.getTramiteRA().setGDesTipoDoc(tramiteRAAdapter.getTramiteRA().getGTipoDoc().getDesTipoDoc());
			}
		
			log.debug(funcName + ": exit");
			return tramiteRAAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public TramiteRAAdapter getTramiteRAAdapterParamBTipoMotor(UserContext userContext, TramiteRAAdapter tramiteRAAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			tramiteRAAdapter.clearError();
			
			// Logica del param
			
			//si no es nulo lo seleccionado
			
			TipoMotor tipoMotor=null;
			if(tramiteRAAdapter.getTramiteRA().getBTM().getCodTipoMotor()!=null){			
				tipoMotor = TipoMotor.getTipoMotorByCodigo(tramiteRAAdapter.getTramiteRA().getBTM().getCodTipoMotor());
				tramiteRAAdapter.getTramiteRA().setBTM((TipoMotorVO)tipoMotor.toVO());	
				tramiteRAAdapter.getTramiteRA().setBCodTipoMotor(tramiteRAAdapter.getTramiteRA().getBTM().getCodTipoMotor());
				tramiteRAAdapter.getTramiteRA().setBDesTipoMotor(tramiteRAAdapter.getTramiteRA().getBTM().getDesTipoMotor());
			}
		
			log.debug(funcName + ": exit");
			return tramiteRAAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public TramiteRAAdapter getTramiteRAAdapterParamETipoMotor(UserContext userContext, TramiteRAAdapter tramiteRAAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			tramiteRAAdapter.clearError();
			
			// Logica del param
			
			//si no es nulo lo seleccionado
			
			TipoMotor tipoMotor=null;
			if(tramiteRAAdapter.getTramiteRA().getETipoMotor().getCodTipoMotor()!=null){			
				tipoMotor = TipoMotor.getTipoMotorByCodigo(tramiteRAAdapter.getTramiteRA().getETipoMotor().getCodTipoMotor());
				tramiteRAAdapter.getTramiteRA().setETipoMotor((TipoMotorVO)tipoMotor.toVO());	
				tramiteRAAdapter.getTramiteRA().setECodTipoMotor(tramiteRAAdapter.getTramiteRA().getETipoMotor().getCodTipoMotor());
				tramiteRAAdapter.getTramiteRA().setEDesTipoMotor(tramiteRAAdapter.getTramiteRA().getETipoMotor().getDesTipoMotor());
			}
		
			log.debug(funcName + ": exit");
			return tramiteRAAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public TramiteRAAdapter getTramiteRAAdapterParamTipoPago(UserContext userContext, TramiteRAAdapter tramiteRAAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			tramiteRAAdapter.clearError();
			
			// Logica del param
			
			//si no es nulo lo seleccionado
			
			TipoPago tipoPago=null;
			if(tramiteRAAdapter.getTramiteRA().getITipoPago().getCodPago()!=null){			
				tipoPago = TipoPago.getTipoPagoByCodigo(tramiteRAAdapter.getTramiteRA().getITipoPago().getCodPago());
				tramiteRAAdapter.getTramiteRA().setITipoPago((TipoPagoVO)tipoPago.toVO());	
				tramiteRAAdapter.getTramiteRA().setICodPago(tramiteRAAdapter.getTramiteRA().getITipoPago().getCodPago());
				tramiteRAAdapter.getTramiteRA().setIDesPago(tramiteRAAdapter.getTramiteRA().getITipoPago().getDesPago());
			}
		
			log.debug(funcName + ": exit");
			return tramiteRAAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public TramiteRAAdapter getTramiteRAAdapterParamTipoUso(UserContext userContext, TramiteRAAdapter tramiteRAAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			tramiteRAAdapter.clearError();
			
			// Logica del param
			
			//si no es nulo lo seleccionado
			
			TipoUso tipoUso=null;
			if(tramiteRAAdapter.getTramiteRA().getBTipoUso().getCodUso()!=null){			
				tipoUso = TipoUso.getTipoUsoByCodigo(tramiteRAAdapter.getTramiteRA().getBTipoUso().getCodUso());
				tramiteRAAdapter.getTramiteRA().setBTipoUso((TipoUsoVO)tipoUso.toVO());	
				tramiteRAAdapter.getTramiteRA().setBDesUso(tramiteRAAdapter.getTramiteRA().getBTipoUso().getDesUso());
				tramiteRAAdapter.getTramiteRA().setBCodUso(tramiteRAAdapter.getTramiteRA().getBTipoUso().getCodUso());
			}
		
			log.debug(funcName + ": exit");
			return tramiteRAAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public TramiteRAAdapter getTramiteRAAdapterParamPropAnterior(UserContext userContext, TramiteRAAdapter tramiteRAAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
	
			tramiteRAAdapter.clearError();
	
			Long codigo = tramiteRAAdapter.getTramiteRA().getGPropAnterior().getId();
	
			// recupero la persona
			Persona persona  = Persona.getById(codigo);
			PersonaVO personaVO = (PersonaVO)persona.toVO(2);
			tramiteRAAdapter.getTramiteRA().setGPropAnterior(personaVO);
			tramiteRAAdapter.getTramiteRA().setGApellidoORazon(personaVO.getView());
			tramiteRAAdapter.getTramiteRA().setGNroDoc(personaVO.getDocumento().getNumero());
			tramiteRAAdapter.getTramiteRA().setGDesDomicilio(personaVO.getDomicilio().getView());
			tramiteRAAdapter.getTramiteRA().setGNroCuit(personaVO.getCuitFull());
			
			log.debug(funcName + ": exit");
			return tramiteRAAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public TramiteRAAdapter getTramiteRAAdapterParamTipoCarga(UserContext userContext, TramiteRAAdapter tramiteRAAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			tramiteRAAdapter.clearError();
			
			// Logica del param
			
			//si no es nulo lo seleccionado
			
			TipoCarga tipoCarga=null;
			if(tramiteRAAdapter.getTramiteRA().getBTipoCarga().getCodTipoCarga()!=null){			
				tipoCarga = TipoCarga.getTipoCargaByCodigo(tramiteRAAdapter.getTramiteRA().getBTipoCarga().getCodTipoCarga());
				tramiteRAAdapter.getTramiteRA().setBTipoCarga((TipoCargaVO)tipoCarga.toVO());
				tramiteRAAdapter.getTramiteRA().setBCodTipoCarga(tramiteRAAdapter.getTramiteRA().getBTipoCarga().getCodTipoCarga());
				tramiteRAAdapter.getTramiteRA().setBDesTipoCarga(tramiteRAAdapter.getTramiteRA().getBTipoCarga().getDesTipoCarga());
			}
		
			log.debug(funcName + ": exit");
			return tramiteRAAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public PropietarioAdapter getPropietarioAdapterParamTipoPropietario(UserContext userContext, PropietarioAdapter propietarioAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			propietarioAdapter.clearError();
			
			// Logica del param
			
			//si no es nulo lo seleccionado
			
			TipoPropietario tipoPropietario=null;
			if(propietarioAdapter.getPropietario().getCodTipoProp()!=null){			
				tipoPropietario = TipoPropietario.getTipoPropietarioByCodigo(propietarioAdapter.getPropietario().getCodTipoProp());
				propietarioAdapter.getPropietario().setCodTipoProp(tipoPropietario.getCodTipoProp());
				propietarioAdapter.getPropietario().setDesTipoProp(tipoPropietario.getDesTipoProp());
			}
		
			log.debug(funcName + ": exit");
			return propietarioAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public TramiteRAAdapter getTramiteRAAdapterParamCalle(UserContext userContext, TramiteRAAdapter tramiteRAAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
	
			tramiteRAAdapter.clearError();
	
			Long codigo = tramiteRAAdapter.getTramiteRA().getDDomicilio().getCalle().getId();
	
			// recupero la persona
			Calle calle  = Calle.getByCodCalle(codigo);
			
			// domicilio
			tramiteRAAdapter.getTramiteRA().setDDesCalle(calle.getNombreCalle());
			tramiteRAAdapter.getTramiteRA().getDDomicilio().setCalle((CalleVO)calle.toVO());
			
			
			log.debug(funcName + ": exit");
			return tramiteRAAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	/**
	 * Cambio de estado 
	 */
	public TramiteRAAdapter getTramiteRAAdapterForCambiarEstado(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {

	String funcName = DemodaUtil.currentMethodName();

	if (log.isDebugEnabled()) log.debug(funcName + ": enter");
	try {
		DemodaUtil.setCurrentUserContext(userContext);
		SiatHibernateUtil.currentSession(); 

		TramiteRA tramiteRA = TramiteRA.getById(commonKey.getId());
		
		// obtengo la lista de estados a los cuales puede ir el actual
		List<EstadoTramiteRA> listEstTramiteRA = tramiteRA.getEstTra().getListEstTramiteRADestino();
		
		List<EstadoTramiteRAVO> listEstadoTramiteRAVO = (ArrayList<EstadoTramiteRAVO>) ListUtilBean.toVO
			(listEstTramiteRA, 1, new EstadoTramiteRAVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));


        TramiteRAAdapter tramiteRAAdapter = new TramiteRAAdapter();
        tramiteRAAdapter.setListEstTramiteRA(listEstadoTramiteRAVO);	        
        
        tramiteRAAdapter.setTramiteRA((TramiteRAVO) tramiteRA.toVO(2, false));
        tramiteRAAdapter.getTramiteRA().setListPropietario(ListUtilBean.toVO(Propietario.getByTramiteRA(tramiteRA.getId())));
        
        if(tramiteRAAdapter.getTramiteRA().getListPropietario().size()>0){        
			for (PropietarioVO ct : tramiteRAAdapter.getTramiteRA().getListPropietario()) {	
				ct.setSexo(Sexo.getById(ct.getCodSexo()));				
				if(ct.getTipoPropietario().equals(1)){						
					if (SiNo.SI.equals(ct.getEsPropPrincipal())) {						
						tramiteRAAdapter.getTramiteRA().setCApellidoORazon(ct.getApellidoORazon());
						tramiteRAAdapter.getTramiteRA().setCDesTipoDoc(ct.getDesTipoDoc());
						tramiteRAAdapter.getTramiteRA().setCCodTipoDoc(ct.getCodTipoDoc());
						tramiteRAAdapter.getTramiteRA().setCNroDoc(ct.getNroDoc());
						tramiteRAAdapter.getTramiteRA().setCNroIB(ct.getNroIB());
						tramiteRAAdapter.getTramiteRA().setCNroProdAgr(ct.getNroProdAgr());
						tramiteRAAdapter.getTramiteRA().setCNroCuit(ct.getNroCuit());
						tramiteRAAdapter.getTramiteRA().setCCodTipoProp(ct.getCodTipoProp());
						tramiteRAAdapter.getTramiteRA().setCDesTipoProp(ct.getDesTipoProp());
						tramiteRAAdapter.getTramiteRA().setCFechaNac(ct.getFechaNac());
						tramiteRAAdapter.getTramiteRA().setCDesEstCiv(ct.getDesEstCiv());
						tramiteRAAdapter.getTramiteRA().setCCodEstCiv(ct.getCodEstCiv());
						tramiteRAAdapter.getTramiteRA().setCDesSexo(ct.getDesSexo());
						
						
					}else{
						if(ct.getTipoPropietario().equals(2)){
							if (SiNo.SI.equals(ct.getEsPropPrincipal())) {							
								tramiteRAAdapter.getTramiteRA().setGApellidoORazon(ct.getApellidoORazon());
								tramiteRAAdapter.getTramiteRA().setGDesTipoDoc(ct.getDesTipoDoc());
								tramiteRAAdapter.getTramiteRA().setGNroDoc(ct.getNroDoc());
								tramiteRAAdapter.getTramiteRA().setGNroCuit(ct.getNroCuit());
							}
						}
					}
				}
			}			
		}

        
        log.debug(funcName + ": exit");
		return tramiteRAAdapter;
	} catch (Exception e) {
		log.error("Service Error: ",  e);
		throw new DemodaServiceException(e);
	} finally {
		SiatHibernateUtil.closeSession();
	}
}

	public TramiteRAVO cambiarEstadoTramiteRA(UserContext userContext, 
			TramiteRAAdapter tramiteRAAdapter) throws DemodaServiceException {

			String funcName = DemodaUtil.currentMethodName();
			Session session = null;
			Transaction tx = null; 

			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			try {
				DemodaUtil.setCurrentUserContext(userContext);
				session = SiatHibernateUtil.currentSession();
				tx = session.beginTransaction();
				tramiteRAAdapter.clearErrorMessages();
				
					
				TramiteRAVO tramiteRAVO = tramiteRAAdapter.getTramiteRA();
				
				if(tramiteRAVO.getEstadoTramiteRA().getId()==null || tramiteRAVO.getEstadoTramiteRA().getId()<0){
					tramiteRAVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,RodError.ESTADOTRAMITE_LABEL);
					return tramiteRAVO;
				}

				// Obtenemos el BO
				TramiteRA tramiteRA = TramiteRA.getById(tramiteRAVO.getId());

	            // Seteamos el nuevo estado el nuevo estado
	            EstadoTramiteRA newEstadoTramiteRA = EstadoTramiteRA.getByIdNull(tramiteRAVO.getEstadoTramiteRA().getId());
	            
	            // Obtenemos la observacion
	            String observacion = tramiteRAVO.getEstadoTramiteRA().getObservacion();

	            // cambio el estado
	            tramiteRA.cambiarEstado(newEstadoTramiteRA, observacion);

	            if (tramiteRA.hasError()) {
	            	tx.rollback();
	            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				} else {
					tx.commit();
					if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				}

				tramiteRA.passErrorMessages(tramiteRAVO);

	            log.debug(funcName + ": exit");
	            return tramiteRAVO;
			} catch (Exception e) {
				log.error(funcName + ": Service Error: ",  e);
				if(tx != null) tx.rollback();
				throw new DemodaServiceException(e);
			} finally {
				SiatHibernateUtil.closeSession();
			}
		}	
	
	public PropietarioVO validateCreatePropietario(UserContext userContext, PropietarioVO propietarioVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			propietarioVO.clearErrorMessages();
			
			Propietario propietario = new Propietario();
			
			propietario.setApellidoORazon(propietarioVO.getApellidoORazon());
			propietario.setCodEstCiv(propietarioVO.getCodEstCiv());
			propietario.setCodSexo(propietarioVO.getSexo().getId());
			propietario.setCodTipoDoc(propietarioVO.getCodTipoDoc());
			propietario.setDesEstCiv(propietarioVO.getDesEstCiv());
			propietario.setDesSexo(propietarioVO.getSexo().getValue());
			propietario.setDesTipoProp(propietarioVO.getDesTipoProp());
			propietario.setDesTipoDoc(propietarioVO.getDesTipoDoc());
			propietario.setNroDoc(propietarioVO.getNroDoc());
			propietario.setNroCuit(propietarioVO.getNroCuit());
			propietario.setNroIB(propietarioVO.getNroIB());
			propietario.setNroProdAgr(propietarioVO.getNroProdAgr());
			propietario.setFechaNac(propietarioVO.getFechaNac());
			propietario.setEsPropPrincipal(propietarioVO.getEsPropPrincipal().getId());
			
			
            if(propietario.hasError()){
	            tx.rollback();
		  		return propietarioVO;	  		
		  	}
			
         // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            propietario = RodTramiteManager.getInstance().validateCreatePropietario(propietario);
            
	        if (propietario.hasError()) {
	        	tx.rollback();
	        	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				propietarioVO =  (PropietarioVO) propietario.toVO(2,false);
			}
	        
			propietario.passErrorMessages(propietarioVO);
            
            log.debug(funcName + ": exit");
            return propietarioVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PropietarioVO updatePropietario(UserContext userContext, PropietarioVO propietarioVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			propietarioVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            Propietario propietario = Propietario.getById(propietarioVO.getId());
			
			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            propietario = RodTramiteManager.getInstance().updatePropietario(propietario);
            
            if (propietario.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				propietarioVO =  (PropietarioVO) propietario.toVO(0,false);
			}
			propietario.passErrorMessages(propietarioVO);
            
            log.debug(funcName + ": exit");
            return propietarioVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PropietarioVO deletePropietario(UserContext userContext, PropietarioVO propietarioVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			propietarioVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			Propietario propietario = Propietario.getById(propietarioVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			propietario = RodTramiteManager.getInstance().deletePropietario(propietario);
			
			if (propietario.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				propietarioVO =  (PropietarioVO) propietario.toVO(0,false);
			}
			propietario.passErrorMessages(propietarioVO);
            
            log.debug(funcName + ": exit");
            return propietarioVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public PropietarioAdapter imprimirPropietario(UserContext userContext, PropietarioAdapter propietarioAdapterVO ) throws DemodaServiceException {
	    String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Propietario propietario = Propietario.getById(propietarioAdapterVO.getPropietario().getId());

			RodDAOFactory.getPropietarioDAO().imprimirGenerico(propietario, propietarioAdapterVO.getReport());
	   		
			log.debug(funcName + ": exit");
			return propietarioAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();        
	    } 
	}

	public PropietarioAdapter getPropietarioAdapterForCreate(
			UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			PropietarioAdapter propietarioAdapter = new PropietarioAdapter();
			
			
			//lista para los tipos de propietarios
			propietarioAdapter.setListTipoPropietario( (ArrayList<TipoPropietarioVO>)
					ListUtilBean.toVO(TipoPropietario.getList(),
					new TipoPropietarioVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// lista para los tipos de doc
			propietarioAdapter.setListTipoDoc( (ArrayList<TipoDocVO>)
					ListUtilBean.toVO(TipoDoc.getList(),
					new TipoDocVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// lista para los tipos de sexo
			
			//Seteo la lista de sexo 
			propietarioAdapter.setListSexo(Sexo.getList(Sexo.OpcionSeleccionar));
			
			// lista para los estados civiles
			propietarioAdapter.setListEstadoCivil( (ArrayList<EstadoCivilVO>)
					ListUtilBean.toVO(EstadoCivil.getList(),
					new EstadoCivilVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			
			log.debug(funcName + ": exit");
			return propietarioAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	

	}

	public PropietarioAdapter getPropietarioAdapterForUpdate(UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			PropietarioAdapter propietarioAdapter = new PropietarioAdapter();
			
			//lista para los tipos de propietarios
			propietarioAdapter.setListTipoPropietario( (ArrayList<TipoPropietarioVO>)
					ListUtilBean.toVO(TipoPropietario.getList(),
					new TipoPropietarioVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// lista para los tipos de doc
			propietarioAdapter.setListTipoDoc( (ArrayList<TipoDocVO>)
					ListUtilBean.toVO(TipoDoc.getList(),
					new TipoDocVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// lista para los tipos de sexo
			/*propietarioAdapter.setListTipoSexo( (ArrayList<TipoSexoVO>)
					ListUtilBean.toVO(TipoSexo.getList(),
					new TipoSexoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			*/
			propietarioAdapter.setListSexo(Sexo.getList(Sexo.OpcionSeleccionar));
			
			// lista para los estados civiles
			propietarioAdapter.setListEstadoCivil( (ArrayList<EstadoCivilVO>)
					ListUtilBean.toVO(EstadoCivil.getList(),
					new EstadoCivilVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));

			
			log.debug(funcName + ": exit");
			return propietarioAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public PropietarioAdapter getPropietarioAdapterForView(UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
		//	Propietario propietario = Propietario.getById(commonKey.getId());

	        PropietarioAdapter propietarioAdapter = new PropietarioAdapter();
	    //    propietarioAdapter.setPropietario((PropietarioVO) propietario.toVO(1));
			
			
			log.debug(funcName + ": exit");
			return propietarioAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public TramiteRAVO marcarTitularPrincipal(UserContext userContext, TramiteRAVO tramiteRAVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tramiteRAVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			TramiteRA tramiteRA = TramiteRA.getById(tramiteRAVO.getId());
			
			//Propietario propietario = Propietario.getById(tramiteRA.getCIdPersonaActual());
			
			tramiteRA.establecerTitularPrincipal(tramiteRA);

			
			if (tramiteRA.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
			tramiteRA.passErrorMessages(tramiteRAVO);
            
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
            return tramiteRAVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public PrintModel imprimirTramiteRA(UserContext userContext, TramiteRAAdapter tramiteRAAdapterVO)throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			//Obtiene el printModel

			PrintModel print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_TRAMITERA_ROD);
			print.putCabecera("usuario", userContext.getUserName());
			print.setData(tramiteRAAdapterVO);
			//print.setDeleteXMLFile(false);
			print.setTopeProfundidad(2);
			

			return print;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} 
	}

	public String validatePropietario(UserContext userContext, TramiteRAAdapter tramiteRAAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();			
						
			// valida de propietario, actuales y anteriores
			List<PropietarioVO> listPropietarioVO = tramiteRAAdapterVO.getTramiteRA().getListPropietario();
			String descripcion = "";
			for(PropietarioVO propietarioVO:listPropietarioVO){
				descripcion += ". " + StringUtil.escaparUpper(propietarioVO.getApellidoORazon())+ " ";
				if(propietarioVO.getCodSexo()!=null && propietarioVO.getNroDoc()!=null){
					PersonaVO personaVO = new PersonaVO();
					personaVO.setSexo(Sexo.getById(propietarioVO.getCodSexo()));
					personaVO.getDocumento().setNumero(propietarioVO.getNroDoc());
				
					boolean b = PadServiceLocator.getPadPersonaService().existePersonaBySexoyNroDoc(userContext, personaVO);
					
					
					if(b){								
						descripcion += SiatUtil.getValueFromBundle("rod.tramiteRA.existePersona");
					}else{						
						descripcion += SiatUtil.getValueFromBundle("rod.tramiteRA.noExistePersona");
					}
									
				}else{

					if(propietarioVO.getSexo().getId()==-1){
						descripcion += ": "+SiatUtil.getValueFromBundle("rod.tramiteRA.noIngresoSexo");
					}
					if(propietarioVO.getNroDoc()==null){
						descripcion += ": "+SiatUtil.getValueFromBundle("rod.tramiteRA.noIngresoNroDoc");
					}
				}
			
			}
            log.debug(funcName + ": exit");
            return descripcion;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public String validatePatente(UserContext userContext, TramiteRAAdapter tramiteRAAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();			
			
			String descripcion=". ";
			if(!StringUtil.isNullOrEmpty(tramiteRAAdapterVO.getTramiteRA().getANroPatente())){
				String patente = tramiteRAAdapterVO.getTramiteRA().getANroPatente();
				
				descripcion += patente;
				//Set the patente pattern string
				
			    Pattern p = Pattern.compile("^[a-zA-Z][a-zA-Z][a-zA-Z][0-9][0-9][0-9]$");

			    //Match the given string with the pattern
			    Matcher m = p.matcher(patente);

			    //check whether match is found 
			    boolean matchFound = m.matches();

			    if (matchFound)
			    	descripcion += " "+SiatUtil.getValueFromBundle("rod.tramiteRA.patenteValida");
			    else
			    	descripcion += " "+SiatUtil.getValueFromBundle("rod.tramiteRA.patenteInvalida");
			}
			
			if(!StringUtil.isNullOrEmpty(tramiteRAAdapterVO.getTramiteRA().getHPatentePad())){
				String patente = tramiteRAAdapterVO.getTramiteRA().getHPatentePad();
				
				descripcion += ". "+ patente;
				//Set the patente pattern string
			    Pattern p = Pattern.compile("^[a-zA-Z][a-zA-Z][a-zA-Z][0-9][0-9][0-9]$");

			    //Match the given string with the pattern
			    Matcher m = p.matcher(patente);

			    //check whether match is found 
			    boolean matchFound = m.matches();

			    if (matchFound)
			    	descripcion += " "+SiatUtil.getValueFromBundle("rod.tramiteRA.patenteValida");
			    else
			    	descripcion += " "+SiatUtil.getValueFromBundle("rod.tramiteRA.patenteInvalida");
			}
			
			if(!StringUtil.isNullOrEmpty(tramiteRAAdapterVO.getTramiteRA().getHPatenteCorr())){
				String patente = tramiteRAAdapterVO.getTramiteRA().getHPatenteCorr();
				
				descripcion += ". "+patente;
				//Set the patente pattern string
			    Pattern p = Pattern.compile("^[a-zA-Z][a-zA-Z][a-zA-Z][0-9][0-9][0-9]$");

			    //Match the given string with the pattern
			    Matcher m = p.matcher(patente);

			    //check whether match is found 
			    boolean matchFound = m.matches();

			    if (matchFound)
			    	descripcion += " "+SiatUtil.getValueFromBundle("rod.tramiteRA.patenteValida");
			    else
			    	descripcion += " "+SiatUtil.getValueFromBundle("rod.tramiteRA.patenteInvalida");
			}
            log.debug(funcName + ": exit");
            return descripcion;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	

	
}
