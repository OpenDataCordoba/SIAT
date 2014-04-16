//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.service;

import ar.gov.rosario.siat.def.iface.model.TipObjImpVO;
import ar.gov.rosario.siat.pad.iface.model.AltaOficioAdapter;
import ar.gov.rosario.siat.pad.iface.model.AltaOficioSearchPage;
import ar.gov.rosario.siat.pad.iface.model.AltaOficioVO;
import ar.gov.rosario.siat.pad.iface.model.CierreComercioSearchPage;
import ar.gov.rosario.siat.pad.iface.model.ObjImpAdapter;
import ar.gov.rosario.siat.pad.iface.model.ObjImpAtrValAdapter;
import ar.gov.rosario.siat.pad.iface.model.ObjImpSearchPage;
import ar.gov.rosario.siat.pad.iface.model.ObjImpVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

/**
 * Interfaz para acceso a los Objetos Imponibles del Padron
 * 
 * @author Tecso Coop. Ltda.
 *
 */
public interface IPadObjetoImponibleService {

	/**
	 * Inicializa la pagina de busqueda de Objetos Imponibles
	 * @return Paginador de objetos imponibles
	 * @throws DemodaServiceException
	 */
	public ObjImpSearchPage getObjImpSearchPageInit(UserContext us, TipObjImpVO tipObjImpReadOnly) throws DemodaServiceException;
	
	/***
	 * Obtiene una pagina de los resultado de la busqueda 
	 * @return
	 * @throws DemodaServiceException
	 */
	public ObjImpSearchPage getObjImpSearchPageResult(UserContext userContext, ObjImpSearchPage objImpSearchPage) throws DemodaServiceException;
	
	public ObjImpSearchPage getObjImpSearchPageResultAva(UserContext userContext, ObjImpSearchPage objImpSearchPage) throws DemodaServiceException;
	
	/***
	 * Maneja el cambio de Tipo Objeto Imponible durante la busqueda. 
	 * @return
	 * @throws DemodaServiceException
	 */
	public ObjImpSearchPage getObjImpSearchPageParamTipObjImp(UserContext userContext, ObjImpSearchPage objImpSearchPage) throws DemodaServiceException;	
	
	/**
	 * Instancia un Adapter de Objeto Imponible para la Creacion
	 * @return el Adapter de Objeto Imponible
	 * @throws DemodaServiceException
	 */
	public ObjImpAdapter getObjImpAdapterForCreate(UserContext userContext, CommonKey idTipObjImp) throws DemodaServiceException;

	/**
	 * Instancia un Adapter de Objeto Imponible para Ver
	 * @return el Adapter de Objeto Imponible
	 * @throws DemodaServiceException
	 */	
	public ObjImpAdapter getObjImpAdapterForView(UserContext userContext, CommonKey idObjImp) throws DemodaServiceException;
	
	/**
	 * Crea un objeto imponible en la DB	
	 * @param userContext
	 * @return 
	 */
	public ObjImpAdapter createObjImp(UserContext userContext, ObjImpAdapter objImpAdapterVO) throws DemodaServiceException;
	
	/**
	 * Actualiza un objeto imponible en la DB	
	 * @param userContext
	 * @return 
	 */
	public ObjImpVO updateObjImp(UserContext userContext, ObjImpVO objImpVO) throws DemodaServiceException;
	
	/**
	 * Maneja el cambio de Tipo Objeto Imponible durante la edicion.	
	 * @param userContext
	 * @return 
	 */
	public ObjImpAdapter getObjImpAdapterParamTipObjImp(UserContext userContext, ObjImpAdapter objImpAdapter) throws DemodaServiceException;
	
	/**
	 * Elimina un objeto imponible con sus atributos valorizados de la DB.
	 * Antes de borrarlo valida que no existan referencias al objeto imponible.
	 * @param userContext
	 * @return el objImp del parametro
	 */
	public ObjImpVO deleteObjImp(UserContext userContext, ObjImpVO objImp) throws DemodaServiceException;

	/**
	 * Activa un objeto imponible.
	 * @param userContext
	 * @return el objImp del parametro
	 */
	public ObjImpVO activarObjImp(UserContext userContext, ObjImpVO objImp) throws DemodaServiceException;

	/**
	 * DesActiva un objeto imponible.
	 * @param userContext
	 * @return el objImp del parametro
	 */
	public ObjImpVO desactivarObjImp(UserContext userContext, ObjImpVO objImp) throws DemodaServiceException;
	
	
	public ObjImpAtrValAdapter getObjImpAtrValAdapterForView (UserContext userContext, CommonKey idObjImp, CommonKey idObjImpAtrVal) throws DemodaServiceException;
	

	/**
	 * Modifica un atributo de oobjto imponible
	 * @param userContext session de usuario
	 * @param objImpAtrAdapter adaptador con los datos a modificar. 
	 * @return el Objeto Imponible modificado
	 * @throws DemodaServiceException
	 */
	public ObjImpAtrValAdapter updateObjImpAtrVal(UserContext userContext, ObjImpAtrValAdapter objImpAtrValAdapterVO) throws DemodaServiceException;

	
	// ---> ABM AltaOficio
	public AltaOficioSearchPage getAltaOficioSearchPageInit(UserContext usercontext) throws Exception;
	public AltaOficioSearchPage getAltaOficioSearchPageResult(UserContext usercontext, AltaOficioSearchPage altaOficioSearchPage) throws DemodaServiceException;

	public AltaOficioAdapter getAltaOficioAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws Exception;
	public AltaOficioAdapter getAltaOficioAdapterForView(UserContext userContext, CommonKey commonKey) throws Exception;
	public AltaOficioAdapter getAltaOficioAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public AltaOficioAdapter getAltaOficioAdapterParamPersona(UserContext userContext, AltaOficioAdapter altaOficioAdapterVO) throws DemodaServiceException;
	public AltaOficioAdapter getAltaOficioAdapterParamInspector(UserContext userContext, AltaOficioAdapter altaOficioAdapterVO) throws DemodaServiceException;
	
	public AltaOficioAdapter createAltaOficio(UserContext userContext, AltaOficioAdapter altaOficioAdapter) throws DemodaServiceException;
	public AltaOficioAdapter updateAltaOficio(UserContext userContext,AltaOficioAdapter altaOficioAdapter) throws DemodaServiceException;
	public AltaOficioVO deleteAltaOficio(UserContext userContext, AltaOficioVO altaOficioVO ) throws DemodaServiceException;
	public AltaOficioSearchPage getAltaOficioSearchPageParamCuenta(UserContext userContext,AltaOficioSearchPage altaOficioSearchPage) throws Exception;
	public AltaOficioVO activarCuentaAltaOficio(UserContext userContext, AltaOficioVO altaOficioVO ) throws DemodaServiceException;
	public AltaOficioVO desactivarCuentaAltaOficio(UserContext userContext, AltaOficioVO altaOficioVO ) throws DemodaServiceException;	
	// <--- ABM AltaOficio	
	
	// ---> ABM CierreComercio
	public CierreComercioSearchPage getCierreComercioSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public CierreComercioSearchPage getCierreComercioSearchPageResult(UserContext userContext, CierreComercioSearchPage cierreComercioSearchPage) throws DemodaServiceException;
	// <--- ABM CierreComercio
}
