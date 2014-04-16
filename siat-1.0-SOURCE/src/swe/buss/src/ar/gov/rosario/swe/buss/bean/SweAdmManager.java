//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import ar.gov.rosario.swe.SweCommonError;
import ar.gov.rosario.swe.SweDAOFactory;
import ar.gov.rosario.swe.buss.auth.SweAuthLoginLocal;
import ar.gov.rosario.swe.iface.model.AccModAplAdapter;
import ar.gov.rosario.swe.iface.model.AccModAplSearchPage;
import ar.gov.rosario.swe.iface.model.AccModAplVO;
import ar.gov.rosario.swe.iface.model.AplicacionAdapter;
import ar.gov.rosario.swe.iface.model.AplicacionSearchPage;
import ar.gov.rosario.swe.iface.model.AplicacionVO;
import ar.gov.rosario.swe.iface.model.CloneUsrAplAdapter;
import ar.gov.rosario.swe.iface.model.ItemMenuAdapter;
import ar.gov.rosario.swe.iface.model.ItemMenuSearchPage;
import ar.gov.rosario.swe.iface.model.ItemMenuVO;
import ar.gov.rosario.swe.iface.model.ModAplAdapter;
import ar.gov.rosario.swe.iface.model.ModAplSearchPage;
import ar.gov.rosario.swe.iface.model.ModAplVO;
import ar.gov.rosario.swe.iface.model.PermiteWeb;
import ar.gov.rosario.swe.iface.model.RolAccModAplAdapter;
import ar.gov.rosario.swe.iface.model.RolAccModAplSearchPage;
import ar.gov.rosario.swe.iface.model.RolAccModAplVO;
import ar.gov.rosario.swe.iface.model.RolAplAdapter;
import ar.gov.rosario.swe.iface.model.RolAplSearchPage;
import ar.gov.rosario.swe.iface.model.RolAplVO;
import ar.gov.rosario.swe.iface.model.TipoAuthVO;
import ar.gov.rosario.swe.iface.model.UsrAplAdapter;
import ar.gov.rosario.swe.iface.model.UsrAplSearchPage;
import ar.gov.rosario.swe.iface.model.UsrAplVO;
import ar.gov.rosario.swe.iface.model.UsrRolAplAdapter;
import ar.gov.rosario.swe.iface.model.UsrRolAplSearchPage;
import ar.gov.rosario.swe.iface.model.UsrRolAplVO;
import ar.gov.rosario.swe.iface.util.SegBussConstants;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.UserContext;

/**
 * Manejador de ABM de Seguridad
 * Singleton
 * @author tecso
 */
public class SweAdmManager  {
	
	public static final String TODAS = "Todas";

	private static Logger log = Logger.getLogger(SweAdmManager.class);
	
	private static final SweAdmManager INSTANCE = new SweAdmManager();

	/**
	 * Constructor privado
	 */
	private SweAdmManager() {
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static SweAdmManager getInstance() {
		return INSTANCE;
	}
	
    public AplicacionAdapter getAplicacionAdapterForView(CommonKey aplicacionKey) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
 	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Aplicacion aplicacion = Aplicacion.getById(aplicacionKey.getId());
        if (aplicacion == null) {
        	throw new DemodaServiceException("No se encontro registro Aplicacion en la Base de Datos.");
        }

		AplicacionAdapter ret = new AplicacionAdapter();
		ret.setAplicacion((AplicacionVO) aplicacion.toVO(0));

 	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");		
		return ret;
	}

    public AplicacionAdapter getAplicacionAdapterForCreate() throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
 	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		AplicacionAdapter aplicacionAdapter = new AplicacionAdapter();
		aplicacionAdapter.getAplicacion().setSegTimeOut(new Long(900)); // por default 15min

		List<TipoAuth>  listTipoAuth = TipoAuth.getListActivos();
		
		aplicacionAdapter.setListTipoAuth((ArrayList<TipoAuthVO>)ListUtilBean.toVO(listTipoAuth, 0, 
                new TipoAuthVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
		
 	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");		
		return aplicacionAdapter;
	}

    public AplicacionAdapter getAplicacionAdapterForUpdate(CommonKey aplicacionKey) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
 	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		Aplicacion aplicacion = Aplicacion.getById(aplicacionKey.getId());
        if (aplicacion == null) {
        	throw new DemodaServiceException("No se encontro registro Aplicacion en la Base de Datos.");
        }

		AplicacionAdapter aplicacionAdapter = new AplicacionAdapter();
		aplicacionAdapter.setAplicacion((AplicacionVO) aplicacion.toVO(1,false));
		
		List<TipoAuth>  listTipoAuth = TipoAuth.getListActivos();
		
		aplicacionAdapter.setListTipoAuth((ArrayList<TipoAuthVO>)ListUtilBean.toVO(listTipoAuth, 0, 
                new TipoAuthVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
		
 	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");		
		return aplicacionAdapter;
	}

    public AplicacionAdapter getAplicacionAdapterForDelete(CommonKey aplicacionKey) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
 	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Aplicacion aplicacion = Aplicacion.getById(aplicacionKey.getId());
        if (aplicacion == null) {
        	throw new DemodaServiceException("No se encontro registro Aplicacion en la Base de Datos.");
        }

		AplicacionAdapter aplicacionAdapter = new AplicacionAdapter();
		aplicacionAdapter.setAplicacion((AplicacionVO)aplicacion.toVO(0));

 	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");		
		return aplicacionAdapter;
	}

    public AplicacionVO createAplicacion(AplicacionVO aplicacionVO) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		//limpiamos la lista de errores del VO
		aplicacionVO.clearError();

		Aplicacion aplicacion = new Aplicacion();  
		// actualizamos elbean con los datos cargados en el VO
		aplicacion.loadFromVO(aplicacionVO);

		// Validaciones de negocio
		if (!aplicacion.validateCreate()) {
			aplicacion.passErrorMessages(aplicacionVO);
			return aplicacionVO;
		}
		
		SweDAOFactory.getAplicacionDAO().update(aplicacion);
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return aplicacionVO;
	}

    public AplicacionVO updateAplicacion(AplicacionVO aplicacionVO) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		//limpiamos la lista de errores del VO
		aplicacionVO.clearError();

	    // Cargamos el Bean desde la DB para actualizarlo.
		Aplicacion aplicacion = Aplicacion.getById(aplicacionVO.getId());
        if (aplicacion == null) {
        	throw new DemodaServiceException("No se encontro registro Aplicacion en la Base de Datos.");
        }

		// actualizamos el bean con los datos cargados en el VO
		aplicacion.loadFromVO(aplicacionVO);

		// Validaciones de negocio
		if (!aplicacion.validateUpdate()) {
			aplicacion.passErrorMessages(aplicacionVO);
			return aplicacionVO;
		}
		
		log.debug("Chequeando aplicacionVO.getTipoAuth().getId()");
		if (aplicacionVO.getTipoAuth().getId() < 1)
		{
			aplicacionVO.addRecoverableError(SweCommonError.APLICACION_TIPOAUTH_REQUIRED);
			return aplicacionVO;
		}
		
		TipoAuth tipoAuth = (TipoAuth) SweDAOFactory.getTipoAuthDAO().getById(aplicacionVO.getTipoAuth().getId());
		aplicacion.setTipoAuth(tipoAuth);
		
		SweDAOFactory.getAplicacionDAO().update(aplicacion);
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return aplicacionVO;
	}

    public AplicacionVO deleteAplicacion(AplicacionVO aplicacionVO) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		//limpiamos la lista de errores del VO
		aplicacionVO.clearError();

		// Cargamos el Bean desde la DB para actualizarlo.
		Aplicacion aplicacion = Aplicacion.getById(aplicacionVO.getId());
        if (aplicacion == null) {
        	throw new DemodaServiceException("No se encontro registro Aplicacion en la Base de Datos.");
        }

		// Validaciones de negocio
		if (!aplicacion.validateDelete()) {
			aplicacion.passErrorMessages(aplicacionVO);
			return aplicacionVO;
		}
		
		SweDAOFactory.getAplicacionDAO().delete(aplicacion);
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return aplicacionVO;
	}

    public AplicacionSearchPage getAplicacionSearchPageInit() throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
			
	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		AplicacionSearchPage aplicacionSearchPage = new AplicacionSearchPage();

		//Encaso de tener que cargar listas ej: combos para los filtros:
		
		//TipoAplicacion.findByActivo();
		//	TipoAppDAO.getList()

		//aplicacion = TipoAplicacion.get(100);
		//aplicacion.getListModulos()

		//aplicacionSearchPage.setListTipoAplicacion(ListUtil.toVO(StringUtil.SELECT_OPCION_TODOS, TipoAplicacion.getList())
		//cambia: la responsabilidad de armar la lista para combos estan statics de los bean.

	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return aplicacionSearchPage;
	}

    public AplicacionSearchPage getAplicacionSearchPageParam(AplicacionSearchPage aplicacionSearchPage) throws Exception {
    	String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		aplicacionSearchPage.clearError();		

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return aplicacionSearchPage;		
	}

    public AplicacionSearchPage getAplicacionSearchPageResult(AplicacionSearchPage aplicacionSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		aplicacionSearchPage.clearError();

		// Verificamos que el usuario logueado no tenga rol de administrardor de SWE.
		// Si tiene ese rol, le mostramos todas las aplicaciones.
		// Sino, le mostramos solo las aplicaciones que puede ver segun swe_usrapladmswe
		UserContext userContext = DemodaUtil.currentUserContext();
		UsrApl loggedUsrApl = UsrApl.getById(userContext.getIdUsuarioSwe());
		if (loggedUsrApl.hasRol(RolApl.ROLAPL_ADMIN_SWE)) {
			aplicacionSearchPage.setIdUsrAplFilter(0L);
		} else {
			aplicacionSearchPage.setIdUsrAplFilter(userContext.getIdUsuarioSwe());
		}
		
		// Aqui obtiene lista de BOs
   		List<Aplicacion> listAplicacion = SweDAOFactory.getAplicacionDAO().findBySearchPage(aplicacionSearchPage);  

		//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
   		
		//Aqui pasamos BO a VO
		aplicacionSearchPage.setListResult(ListUtilBean.toVO(listAplicacion,3));
	    
		// Segurizacion del registro correspondiente a SWE
		for (AplicacionVO aplVO:(ArrayList<AplicacionVO>)aplicacionSearchPage.getListResult()){			
			if( SegBussConstants.CODIGO_SWE.equals(aplVO.getCodigo())) {
				aplVO.setEliminarBussEnabled(false);
				aplVO.setModificarBussEnabled(false);
   			}
		}
				
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return aplicacionSearchPage;
	}
    
	/*-----------------------------------------------------*/
	/* servicios de Usuarios de Aplicacion                 */
	/*-----------------------------------------------------*/

    public UsrAplSearchPage getUsrAplSearchPageInit(CommonKey aplicacionKey) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");

	    // Cargamos el Bean desde la DB para actualizarlo.
		Aplicacion aplicacion = Aplicacion.getById(aplicacionKey.getId());
        if (aplicacion == null) {
        	throw new DemodaServiceException("No se encontro registro Aplicacion en la Base de Datos.");
        }
        
        UsrAplSearchPage usrAplSearchPage = new UsrAplSearchPage((AplicacionVO) aplicacion.toVO(0));
        
	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return usrAplSearchPage;
	}

    public UsrAplSearchPage getUsrAplSearchPageResult(UsrAplSearchPage usrAplSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		usrAplSearchPage.clearError();

		//Aqui realizar validaciones
		usrAplSearchPage.setInactivo(true);
		// Aqui obtiene lista de BOs
   		List<UsrApl> listUsrApl = SweDAOFactory.getUsrAplDAO().findBySearchPage(usrAplSearchPage);  
		usrAplSearchPage.setInactivo(false);

		//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.

		//Aqui pasamos BO a VO
   		usrAplSearchPage.setListResult(ListUtilBean.toVO(listUsrApl,0));
   		
   		// Segurizacion del registro correspondiente al usuario admin de  SWE
   		if (SegBussConstants.CODIGO_SWE.equals(usrAplSearchPage.getAplicacion().getCodigo())){
			for (UsrAplVO usrAplVO:(ArrayList<UsrAplVO>)usrAplSearchPage.getListResult()){			
				if( SegBussConstants.USUARIO_ADMIN.equals(usrAplVO.getUsername().trim())) {
					usrAplVO.setEliminarBussEnabled(false);
					usrAplVO.setModificarBussEnabled(false);
	   			}
			}
   		}
   		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return usrAplSearchPage;
	}
    
    public UsrAplAdapter getUsrAplAdapterForCreate(CommonKey aplicacionCommonKey) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
 	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Aplicacion aplicacion = Aplicacion.getById(aplicacionCommonKey.getId());
        if (aplicacion == null) {
        	throw new DemodaServiceException("No se encontro registro de Aplicacion en la Base de Datos.");
        }
        UsrAplAdapter usrAplAdapter = new UsrAplAdapter((AplicacionVO) aplicacion.toVO(1));
        
        //Si la aplicaci�n es SWE, se llena la lista de aplicaciones para asignarle al usuario que se est� creando
        if(aplicacion.getCodigo().equals(SegBussConstants.CODIGO_SWE)){
        	List<Aplicacion> listAplicacion = Aplicacion.getAllActivas();
     	    usrAplAdapter.setListAplicaciones(ListUtilBean.toVO(listAplicacion));
     	    usrAplAdapter.setListIdsAppSelected(new Long[listAplicacion.size()]);
        }
 	    
        // Setea la lista permiteWeb
        usrAplAdapter.getListPermiteWeb().add(PermiteWeb.PERMITE_WEB);
        usrAplAdapter.getListPermiteWeb().add(PermiteWeb.NO_PERMITE_WEB);
        
        if (log.isDebugEnabled()) log.debug(funcName + ": exit");		
		return usrAplAdapter;
	}
    
    public CloneUsrAplAdapter getCloneUsrAplAdapter(CommonKey usrAplKey) throws Exception {
//	    String funcName = DemodaUtil.currentMethodName();
// 	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");
//		
//		Aplicacion aplicacion = Aplicacion.getById(aplicacionCommonKey.getId());
//        if (aplicacion == null) {
//        	if (log.isDebugEnabled()) log.debug("ERROR: aplicación es NULL");
//        	throw new DemodaServiceException("No se encontro registro de Aplicacion en la Base de Datos.");
//        }
//        CloneUsrAplAdapter cloneUsrAplAdapter = new CloneUsrAplAdapter((AplicacionVO) aplicacion.toVO(1));
//        
//        //Si la aplicaci�n es SWE, se llena la lista de aplicaciones para asignarle al usuario que se est� creando
//        if(aplicacion.getCodigo().equals(SegBussConstants.CODIGO_SWE)){
//        	List<Aplicacion> listAplicacion = Aplicacion.getAllActivas();
//     	    cloneUsrAplAdapter.setListAplicaciones(ListUtilBean.toVO(listAplicacion));
//     	   cloneUsrAplAdapter.setListIdsAppSelected(new Long[listAplicacion.size()]);
//        }
// 	    
//        // Setea la lista permiteWeb
//        cloneUsrAplAdapter.getListPermiteWeb().add(PermiteWeb.PERMITE_WEB);
//        cloneUsrAplAdapter.getListPermiteWeb().add(PermiteWeb.NO_PERMITE_WEB);
//        
//        if (log.isDebugEnabled()) log.debug(funcName + ": exit");		
//		return cloneUsrAplAdapter;
    	
    	 String funcName = DemodaUtil.currentMethodName();
  	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");
 		
 		UsrApl usrApl = UsrApl.getById(usrAplKey.getId());
         if (usrApl == null) {
         	throw new DemodaServiceException("No se encontro registro Usuario de Aplicacion en la Base de Datos.");
         }

         //Si es SWE        
 		CloneUsrAplAdapter cloneUsrAplAdapter = new CloneUsrAplAdapter((UsrAplVO) usrApl.toVO(2));
 		if(usrApl.getListAplicacionesPermitidas()!=null){
 			cloneUsrAplAdapter.setListIdsAppSelected(new Long[usrApl.getListAplicacionesPermitidas().size()]);	
 			int cont =0;
 			for(UsrAplAdmSwe usrAplAdmSwe:usrApl.getListAplicacionesPermitidas()){
 				cloneUsrAplAdapter.getListIdsAppSelected()[cont++] = usrAplAdmSwe.getAplicacion().getId();				
 			}
 		}
         //Si la aplicaci�n es SWE, se llena la lista de aplicaciones para asignarle al usuario que se est� modificando
         if(usrApl.getAplicacion().getCodigo().equals(SegBussConstants.CODIGO_SWE)){
         	List<Aplicacion> listAplicacion = Aplicacion.getAllActivas();
      	    cloneUsrAplAdapter.setListAplicaciones(ListUtilBean.toVO(listAplicacion));     	    
         }
         
         // Setea la lista permiteWeb
         cloneUsrAplAdapter.getUsrApl().setPermiteWeb(
         			usrApl.getPermiteWeb()!=null?PermiteWeb.getById(usrApl.getPermiteWeb()):PermiteWeb.NO_PERMITE_WEB);
         cloneUsrAplAdapter.getListPermiteWeb().add(PermiteWeb.PERMITE_WEB);
         cloneUsrAplAdapter.getListPermiteWeb().add(PermiteWeb.NO_PERMITE_WEB);
         
         
  	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");		
 		return cloneUsrAplAdapter;
	}


    public UsrAplAdapter getUsrAplAdapter(CommonKey usrAplKey) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
 	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		UsrApl usrApl = UsrApl.getById(usrAplKey.getId());
        if (usrApl == null) {
        	throw new DemodaServiceException("No se encontro registro Usuario de Aplicacion en la Base de Datos.");
        }

        //Si es SWE        
		UsrAplAdapter usrAplAdapter = new UsrAplAdapter((UsrAplVO) usrApl.toVO(2));
		if(usrApl.getListAplicacionesPermitidas()!=null){
			usrAplAdapter.setListIdsAppSelected(new Long[usrApl.getListAplicacionesPermitidas().size()]);	
			int cont =0;
			for(UsrAplAdmSwe usrAplAdmSwe:usrApl.getListAplicacionesPermitidas()){
				usrAplAdapter.getListIdsAppSelected()[cont++] = usrAplAdmSwe.getAplicacion().getId();				
			}
		}
        //Si la aplicaci�n es SWE, se llena la lista de aplicaciones para asignarle al usuario que se est� modificando
        if(usrApl.getAplicacion().getCodigo().equals(SegBussConstants.CODIGO_SWE)){
        	List<Aplicacion> listAplicacion = Aplicacion.getAllActivas();
     	    usrAplAdapter.setListAplicaciones(ListUtilBean.toVO(listAplicacion));     	    
        }
        
        // Setea la lista permiteWeb
        usrAplAdapter.getUsrApl().setPermiteWeb(
        			usrApl.getPermiteWeb()!=null?PermiteWeb.getById(usrApl.getPermiteWeb()):PermiteWeb.NO_PERMITE_WEB);
        usrAplAdapter.getListPermiteWeb().add(PermiteWeb.PERMITE_WEB);
        usrAplAdapter.getListPermiteWeb().add(PermiteWeb.NO_PERMITE_WEB);
        
        
 	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");		
		return usrAplAdapter;
	}

    
    
    public UsrAplVO createUsrApl(UsrAplVO usrAplVO) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		//limpiamos la lista de errores del VO
		usrAplVO.clearError();
		
		UsrApl usrApl = new UsrApl();

		//Si el usuario es de SWE agrega la lista de aplicaciones permitidas
		if(usrAplVO.getAplicacion().getCodigo().equals(SegBussConstants.CODIGO_SWE)){
			usrApl.agregarAplicacionesPermitidas(usrAplVO.getListIdsAppSelected());
		}

		//Creamos un UsrAuth si la aplicacion requiere autenticacion local 
		if (usrAplVO.getAplicacion().getTipoAuth().getId() == 1) {		
			
			SweAuthLoginLocal sweAuthLoginLocal = new SweAuthLoginLocal();
			String updateResult = sweAuthLoginLocal.createUser(usrAplVO);	
			
			if (updateResult.equals(SweAuthLoginLocal.SWE_AUTH_INVALID_EMPTY_PASSWORD)) {
				usrAplVO.addNonRecoverableError(SweCommonError.USRAUTH_PASS_REQUIRED);
			}
			
			if (updateResult.equals(SweAuthLoginLocal.SWE_AUTH_INVALID_USR_NAME)) {
				usrAplVO.addNonRecoverableError(SweCommonError.USRAUTH_UNIQUE);
			}
			
			if (updateResult.equals(SweAuthLoginLocal.SWE_AUTH_INVALID_RETYPE_PASSWORD)) {
				usrAplVO.addNonRecoverableError(SweCommonError.USRAUTH_PASS_ERROR);				
			}
			
			if (updateResult.equals(SweAuthLoginLocal.SWE_AUTH_INVALID_UPDATE)) {
				usrAplVO.addNonRecoverableError(SweCommonError.USRAUTH_UPDATE_ERROR);
			}
		}
		
		// actualizamos elbean con los datos cargados en el VO
		usrApl.loadFromVO(usrAplVO);
		usrApl.setFechaAlta(new Date());
		
		// Validaciones de negocio
		if (!usrApl.validateCreate()) {
			usrApl.passErrorMessages(usrAplVO);
			return usrAplVO;
		}
		
		usrApl.setPermiteWeb(usrAplVO.getPermiteWeb().getId());
		
		SweDAOFactory.getUsrAplDAO().update(usrApl); // carga el id en el BO
		usrAplVO.setId(usrApl.getId()); // consultar si lo hacemos?
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return usrAplVO;
	}

    public UsrAplVO cloneUsrApl(UsrAplVO usrAplVO, UsrAplVO usrAplToCloneVO) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		//limpiamos la lista de errores del VO
		usrAplVO.clearError();
		
		UsrApl usrApl = new UsrApl();
		
        
		//Si el usuario es de SWE agrega la lista de aplicaciones permitidas
		if(usrAplVO.getAplicacion().getCodigo().equals(SegBussConstants.CODIGO_SWE)){
			usrApl.agregarAplicacionesPermitidas(usrAplVO.getListIdsAppSelected());
		}
			
        if (usrApl == null) {
        	throw new DemodaServiceException("No se encontro registro UsrApl en la Base de Datos.");
        }

        
		//Creamos un UsrAuth si la aplicacion requiere autenticacion local 
		if (usrAplVO.getAplicacion().getTipoAuth().getId() == 1) {		
			
			SweAuthLoginLocal sweAuthLoginLocal = new SweAuthLoginLocal();
			String updateResult = sweAuthLoginLocal.createUser(usrAplVO);	
			
			if (updateResult.equals(SweAuthLoginLocal.SWE_AUTH_INVALID_EMPTY_PASSWORD)) {
				usrAplVO.addNonRecoverableError(SweCommonError.USRAUTH_PASS_REQUIRED);
			}
			
			if (updateResult.equals(SweAuthLoginLocal.SWE_AUTH_INVALID_USR_NAME)) {
				usrAplVO.addNonRecoverableError(SweCommonError.USRAUTH_UNIQUE);
			}
			
			if (updateResult.equals(SweAuthLoginLocal.SWE_AUTH_INVALID_RETYPE_PASSWORD)) {
				usrAplVO.addNonRecoverableError(SweCommonError.USRAUTH_PASS_ERROR);				
			}
			
			if (updateResult.equals(SweAuthLoginLocal.SWE_AUTH_INVALID_UPDATE)) {
				usrAplVO.addNonRecoverableError(SweCommonError.USRAUTH_UPDATE_ERROR);
			}
		}
		
		
		
		// se traen los valores del objeto a clonar
		
        UsrApl usrAplToClone = UsrApl.getById(usrAplToCloneVO.getId());
        if (usrAplToClone == null) {
        	throw new DemodaServiceException("No se encontro registro UsrApl en la Base de Datos.");
        }
        
        
		
        // actualizamos elbean con los datos cargados en el VO
		usrApl.loadFromVO(usrAplVO);
		
		usrAplToClone.setUsrAplForClone(usrApl);
        
		usrApl.setFechaAlta(new Date());
		
		// Validaciones de negocio
		if (!usrApl.validateCreate()) {
			usrApl.passErrorMessages(usrAplVO);
			return usrAplVO;
		}
		
		usrApl.setPermiteWeb(usrAplVO.getPermiteWeb().getId());
		      
		
		SweDAOFactory.getUsrAplDAO().update(usrApl); // carga el id en el BO
		usrAplVO.setId(usrApl.getId()); // consultar si lo hacemos?
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return usrAplVO;
	}
    
    public UsrAplVO updateUsrApl(UsrAplVO usrAplVO) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		//limpiamos la lista de errores del VO
		usrAplVO.clearError();
		
	    // Cargamos el Bean desde la DB para actualizarlo.
		UsrApl usrApl = UsrApl.getById(usrAplVO.getId());
        if (usrApl == null) {
        	throw new DemodaServiceException("No se encontro registro UsrApl en la Base de Datos.");
        }

		//Creamos un UsrAuth si la aplicacion requiere autenticacion local 
		if (usrAplVO.getAplicacion().getTipoAuth().getId() == 1) {		
			
			SweAuthLoginLocal sweAuthLoginLocal = new SweAuthLoginLocal();
			String updateResult = sweAuthLoginLocal.updateUser(usrAplVO,usrApl.getUsername());	
			
			if (updateResult.equals(SweAuthLoginLocal.SWE_AUTH_INVALID_EMPTY_PASSWORD)) {
				usrAplVO.addNonRecoverableError(SweCommonError.USRAUTH_PASS_REQUIRED);
			}
			
			if (updateResult.equals(SweAuthLoginLocal.SWE_AUTH_INVALID_USR_NAME)) {
				usrAplVO.addNonRecoverableError(SweCommonError.USRAUTH_UNIQUE);
			}
			
			if (updateResult.equals(SweAuthLoginLocal.SWE_AUTH_INVALID_RETYPE_PASSWORD)) {
				usrAplVO.addNonRecoverableError(SweCommonError.USRAUTH_PASS_ERROR);				
			} 
			
			if (updateResult.equals(SweAuthLoginLocal.SWE_AUTH_INVALID_UPDATE)) {
				usrAplVO.addNonRecoverableError(SweCommonError.USRAUTH_UPDATE_ERROR);
			}
		}
        
		//Si el usuario es de SWE se actualiza la lista de aplicaciones permitidas
        if(usrAplVO.getAplicacion().getCodigo().equals(SegBussConstants.CODIGO_SWE)){
        	usrApl.actualizarAplicacionesPermitidas(usrAplVO.getListIdsAppSelected());
        }

		//cambio de Activo a Inactivo
		if (usrApl.getEstado().equals(Estado.ACTIVO.getId())
			&& !usrApl.getEstado().equals(usrAplVO.getEstado().getId())) {
			usrApl.setFechaBaja(new Date());
		}

		//cambio de Inactivo a Activo
		if (usrApl.getEstado().equals(Estado.INACTIVO.getId())
			&& !usrApl.getEstado().equals(usrAplVO.getEstado().getId())) {
			usrApl.setFechaBaja(null);
		}


		// actualizamos el bean con los datos cargados en el VO
		usrApl.loadFromVOForUpdate(usrAplVO);

		// Validaciones de negocio
		if (!usrApl.validateUpdate()) {
			usrApl.passErrorMessages(usrAplVO);
			return usrAplVO;
		}

		SweDAOFactory.getUsrAplDAO().update(usrApl);

	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return usrAplVO;
	}

    public UsrAplVO deleteUsrApl(UsrAplVO usrAplVO) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		//limpiamos la lista de errores del VO
		usrAplVO.clearError();

		// Cargamos el Bean desde la DB para actualizarlo.
		UsrApl usrApl = UsrApl.getById(usrAplVO.getId());
        if (usrApl == null) {
        	throw new DemodaServiceException("No se encontro registro UsrApl en la Base de Datos.");
        }
        
    	//Creamos un UsrAuth si la aplicacion requiere autenticacion local 
		if (usrAplVO.getAplicacion().getTipoAuth().getId() == 1) {		
						
			SweAuthLoginLocal sweAuthLoginLocal = new SweAuthLoginLocal();
			String deleteResult = sweAuthLoginLocal.deleteUser(usrAplVO.getUsername());				
			if (deleteResult.equals(SweAuthLoginLocal.SWE_AUTH_FAIL_DELETE)) {
				usrAplVO.addNonRecoverableError(SweCommonError.USRAUTH_DELETE_ERROR);
			}
		}

		// Validaciones de negocio
		if (!usrApl.validateDelete()) {
			usrApl.passErrorMessages(usrAplVO);
			return usrAplVO;
		}
		
		SweDAOFactory.getUsrAplDAO().delete(usrApl);
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return usrAplVO;
	}
    
    /*-----------------------------------------------------*/
	/* servicios de Modulo de Aplicacion                   */
	/*-----------------------------------------------------*/
    
    public ModAplSearchPage getModAplSearchPageInit(CommonKey aplicacionKey) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
			
	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		ModAplSearchPage modAplSearchPage = new ModAplSearchPage();

	    // Recuperamos el Bean desde la DB .
		Aplicacion aplicacion = Aplicacion.getById(aplicacionKey.getId());
        if (aplicacion == null) {
        	throw new DemodaServiceException("No se encontro registro Aplicacion en la Base de Datos.");
        }
        modAplSearchPage.setAplicacion((AplicacionVO) aplicacion.toVO(0));

	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return modAplSearchPage;
	}
    
    public ModAplSearchPage getModAplSearchPageResult(ModAplSearchPage modAplSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		modAplSearchPage.clearError();

		//Aqui realizar validaciones

		// Aqui obtiene lista de BOs
   		List<ModApl> listModApl = SweDAOFactory.getModAplDAO().findBySearchPage(modAplSearchPage);  

		//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.

		//Aqui pasamos BO a VO
   		modAplSearchPage.setListResult(ListUtilBean.toVO(listModApl,3));
	        
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return modAplSearchPage;
	}
    
    public ModAplAdapter getModAplAdapterForCreate(CommonKey aplicacionKey) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
 	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");
 	    
 	    ModAplAdapter modAplAdapter = new ModAplAdapter();
 	    
	    // Recuperamos el Bean desde la DB .
		Aplicacion aplicacion = Aplicacion.getById(aplicacionKey.getId());
        if (aplicacion == null) {
        	throw new DemodaServiceException("No se encontro registro Aplicacion en la Base de Datos.");
        }
        modAplAdapter.getModApl().setAplicacion((AplicacionVO) aplicacion.toVO(0)); 	    

 	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");		
		return modAplAdapter;
	}
    
    public ModAplAdapter getModAplAdapter(CommonKey modAplKey) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
 	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");

 	   ModApl modApl = ModApl.getById(modAplKey.getId());
        if (modApl == null) {
        	throw new DemodaServiceException("No se encontro registro ModApl en la Base de Datos.");
        }

        ModAplAdapter modAplAdapter = new ModAplAdapter();
        modAplAdapter.setModApl((ModAplVO) modApl.toVO(1));

 	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");		
		return modAplAdapter;
	}
    
    public ModAplVO createModApl(ModAplVO modAplVO) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		//limpiamos la lista de errores del VO
		modAplVO.clearError();

		ModApl modApl = new ModApl();  
		// actualizamos elbean con los datos cargados en el VO
		modApl.loadFromVO(modAplVO);

		// Validaciones de negocio
		if (!modApl.validateCreate()) {
			modApl.passErrorMessages(modAplVO);
			return modAplVO;
		}
		
		SweDAOFactory.getModAplDAO().update(modApl);
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return modAplVO;
	}

    public ModAplVO updateModApl(ModAplVO modAplVO) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		//limpiamos la lista de errores del VO
		modAplVO.clearError();

	    // Cargamos el Bean desde la DB para actualizarlo.
		ModApl aplicacion = ModApl.getById(modAplVO.getId());
        if (aplicacion == null) {
        	throw new DemodaServiceException("No se encontro registro ModApl en la Base de Datos.");
        }

		// actualizamos el bean con los datos cargados en el VO
		aplicacion.loadFromVO(modAplVO);

		// Validaciones de negocio
		if (!aplicacion.validateUpdate()) {
			aplicacion.passErrorMessages(modAplVO);
			return modAplVO;
		}
		
		SweDAOFactory.getModAplDAO().update(aplicacion);
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return modAplVO;
	}

    public ModAplVO deleteModApl(ModAplVO modAplVO) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		//limpiamos la lista de errores del VO
	    modAplVO.clearError();

		// Cargamos el Bean desde la DB para actualizarlo.
		ModApl modApl = ModApl.getById(modAplVO.getId());
        if (modApl == null) {
        	throw new DemodaServiceException("No se encontro registro ModApl en la Base de Datos.");
        }

		// Validaciones de negocio
		if (!modApl.validateDelete()) {
			modApl.passErrorMessages(modAplVO);
			return modAplVO;
		}
		
		SweDAOFactory.getModAplDAO().delete(modApl);
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return modAplVO;
	}

    /*-----------------------------------------------------*/
	/* fin servicios de Modulo de Aplicacion               */
	/*-----------------------------------------------------*/
    
    /*-----------------------------------------------------*/
	/* servicios de Acciones de Modulo de Aplicacion       */
	/*-----------------------------------------------------*/
    
    public AccModAplSearchPage getAccModAplSearchPageInit(AccModAplSearchPage accModAplSearchPage) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();

	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");
	    
	    if(accModAplSearchPage.getInitFor().equals(AccModAplSearchPage.INIT_FOR_ROL)){
			if (log.isDebugEnabled()) log.debug("Entra por INIT_FOR_ROL //////////");

	    	accModAplSearchPage.setListResult(new ArrayList());
	    	accModAplSearchPage.setListAcciones(new ArrayList<String>());
	    	accModAplSearchPage.getListAcciones().add(TODAS);
	    	accModAplSearchPage.setHabilitarFiltroModulo(true);
	    	Aplicacion aplicacion = Aplicacion.getById(accModAplSearchPage.getAplicacion().getId());
	    	
	    	ModApl modApl = null;
	    	if(!ModelUtil.isNullOrEmpty(accModAplSearchPage.getModApl()))
	    		modApl = ModApl.getById(accModAplSearchPage.getModApl().getId());
	    	
	    	accModAplSearchPage.getListAcciones().addAll(AccModApl.getAccionesForMod(aplicacion, modApl));
	    }
	    
	    // Si fue llamado desde el ABM de modulos
		if (!ModelUtil.isNullOrEmpty(accModAplSearchPage.getModApl())){
		    // Recuperamos el Bean desde la DB .
			if (log.isDebugEnabled()) log.debug("Fue llamado desde el ABM de modulos //////////");
			ModApl modApl = ModApl.getById(accModAplSearchPage.getModApl().getId());
	        if (modApl == null) {
	        	throw new DemodaServiceException("No se encontro registro ModApl en la Base de Datos.");
	        }
	        accModAplSearchPage.setModApl((ModAplVO) modApl.toVO(1));
	        accModAplSearchPage.getListModApl().add((ModAplVO) modApl.toVO(1));
		}

		// Si fue llamado de la seleccion de Acciones Modulo del Rol
		if (accModAplSearchPage.getAplicacion().getId() != null){
			if (log.isDebugEnabled()) log.debug("Fue llamado de la seleccion de Acciones Modulo del Rol //////////");
		    // Recuperamos el Bean desde la DB .
			Aplicacion aplicacion = Aplicacion.getById(accModAplSearchPage.getAplicacion().getId());
	        if (aplicacion == null) {
	        	throw new DemodaServiceException("No se encontro registro Aplicacion en la Base de Datos.");
	        }
	        accModAplSearchPage.setAplicacion((AplicacionVO) aplicacion.toVO(1));
	        
	        accModAplSearchPage.getModApl().setAplicacion((AplicacionVO) aplicacion.toVO(1));
	        
	        if (ModelUtil.isNullOrEmpty(accModAplSearchPage.getModApl())){
				// Busco los Modulos activos de la Aplicacion        
		        ArrayList<ModApl>  listModApl = (ArrayList<ModApl>) aplicacion.getListModAplActivos();
		        
		        accModAplSearchPage.setListModApl(
		        		(ArrayList<ModAplVO>)ListUtilBean.toVO(listModApl, 0, new ModAplVO(StringUtil.SELECT_OPCION_TODOS)));
	        }
	        
	        ModApl modApl = null;
	    	if(!ModelUtil.isNullOrEmpty(accModAplSearchPage.getModApl()))
	    		modApl = ModApl.getById(accModAplSearchPage.getModApl().getId());
	    	
	    	//if (log.isDebugEnabled()) log.debug("El módulo es " + modApl.getNombreModulo());
	    	accModAplSearchPage.setListAcciones(new ArrayList<String>());
	        accModAplSearchPage.getListAcciones().addAll(AccModApl.getAccionesForMod(aplicacion, modApl));
		}
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return accModAplSearchPage;
	}
    
    public AccModAplSearchPage getAccModAplSearchPageResult(AccModAplSearchPage accModAplSearchPage) 
    	throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		accModAplSearchPage.clearError();

		//Aqui realizar validaciones

		if(accModAplSearchPage.getNombreAccion().equals(TODAS)){
			accModAplSearchPage.setNombreAccion("");
		}
		// Aqui obtiene lista de BOs
   		List<AccModApl> listAccModApl = SweDAOFactory.getAccModAplDAO().findBySearchPage(accModAplSearchPage);  

		//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.

		//Aqui pasamos BO a VO
   		accModAplSearchPage.setListResult(ListUtilBean.toVO(listAccModApl,3));
	        
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return accModAplSearchPage;
	}

    public AccModAplAdapter getAccModAplAdapterForCreate(CommonKey modAplKey) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
 	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");

 	    AccModAplAdapter accModAplAdapter = new AccModAplAdapter();

	    // Recuperamos el Bean desde la DB .
 	    ModApl modApl = ModApl.getById(modAplKey.getId());
        if (modApl == null) {
        	throw new DemodaServiceException("No se encontro registro ModApl en la Base de Datos.");
        }
        accModAplAdapter.getAccModApl().setModApl((ModAplVO) modApl.toVO(4)); 	    

 	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");		
		return accModAplAdapter;
	}
    
    public AccModAplAdapter getAccModAplAdapter(CommonKey accModAplKey) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
 	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");

 	   AccModApl accModApl = AccModApl.getById(accModAplKey.getId());
        if (accModApl == null) {
        	throw new DemodaServiceException("No se encontro registro AccModApl en la Base de Datos.");
        }

        AccModAplAdapter accModAplAdapter = new AccModAplAdapter();
        accModAplAdapter.setAccModApl((AccModAplVO) accModApl.toVO(4));

 	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");		
		return accModAplAdapter;
	}
    
    public AccModAplVO createAccModApl(AccModAplVO accModAplVO) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		//limpiamos la lista de errores del VO
		accModAplVO.clearError();

		AccModApl accModApl = new AccModApl();  
		// actualizamos elbean con los datos cargados en el VO
		accModApl.loadFromVO(accModAplVO);

		// Validaciones de negocio
		if (!accModApl.validateCreate()) {
			accModApl.passErrorMessages(accModAplVO);
			return accModAplVO;
		}
		
		SweDAOFactory.getAccModAplDAO().update(accModApl);
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return accModAplVO;
	}

    public AccModAplVO updateAccModApl(AccModAplVO accModAplVO) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		//limpiamos la lista de errores del VO
		accModAplVO.clearError();

	    // Cargamos el Bean desde la DB para actualizarlo.
		AccModApl accModApl = AccModApl.getById(accModAplVO.getId());
        if (accModApl == null) {
        	throw new DemodaServiceException("No se encontro registro AccModApl en la Base de Datos.");
        }

		// actualizamos el bean con los datos cargados en el VO
        accModApl.loadFromVOUpdate(accModAplVO);

		// Validaciones de negocio
		if (!accModApl.validateUpdate()) {
			accModApl.passErrorMessages(accModAplVO);
			return accModAplVO;
		}
		
		SweDAOFactory.getAccModAplDAO().update(accModApl);
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return accModAplVO;
	}

    public AccModAplVO deleteAccModApl(AccModAplVO accModAplVO) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		//limpiamos la lista de errores del VO
	    accModAplVO.clearError();

		// Cargamos el Bean desde la DB para actualizarlo.
		AccModApl accModApl = AccModApl.getById(accModAplVO.getId());
        if (accModApl == null) {
        	throw new DemodaServiceException("No se encontro registro AccModApl en la Base de Datos.");
        }

		// Validaciones de negocio
		if (!accModApl.validateDelete()) {
			accModApl.passErrorMessages(accModAplVO);
			return accModAplVO;
		}
		
		SweDAOFactory.getAccModAplDAO().delete(accModApl);
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return accModAplVO;
	}

    /*-----------------------------------------------------*/
	/* fin servicios de Acciones de Modulo de Aplicacion   */
	/*-----------------------------------------------------*/

    
    // UsrRolApl
    
    public UsrRolAplSearchPage getUsrRolAplSearchPageInit(CommonKey usrAplCommonKey) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
			
	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UsrRolAplSearchPage usrRolAplSearchPage = new UsrRolAplSearchPage();

	    // Recuperamos el Bean desde la DB .
		UsrApl usrApl = UsrApl.getById(usrAplCommonKey.getId());
		
        if (usrApl == null) {
        	throw new DemodaServiceException("No se encontro registro de Usuario de Aplicacion en la Base de Datos.");
        }
        usrRolAplSearchPage.setUsrApl((UsrAplVO) usrApl.toVO(1));

	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return usrRolAplSearchPage;
	}
    
    public UsrRolAplSearchPage getUsrRolAplSearchPageResult(UsrRolAplSearchPage usrRolAplSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		usrRolAplSearchPage.clearError();

		//Aqui realizar validaciones

	    // Recuperamos el Bean desde la DB .
		UsrApl usrApl = UsrApl.getById(usrRolAplSearchPage.getUsrApl().getId());
		
        if (usrApl == null) {
        	throw new DemodaServiceException("No se encontro registro de Usuario de Aplicacion en la Base de Datos.");
        }
		
		// Aqui obtiene lista de BOs
   		//List<UsrRolApl> listUsrRolApl = SweDAOFactory.getUsrRolAplDAO().findBySearchPage(usrRolAplSearchPage);
        List<UsrRolApl> listUsrRolApl = usrApl.getListUsrRolApl();

		//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.

		//Aqui pasamos BO a VO
        usrRolAplSearchPage.setListResult(ListUtilBean.toVO(listUsrRolApl,1));
	    
		// Segurizacion del registro correspondiente al Rol "Administrador" del usuario "admin"  de "SWE"
   		if (SegBussConstants.CODIGO_SWE.equals(usrRolAplSearchPage.getUsrApl().getAplicacion().getCodigo())){
			for (UsrRolAplVO usrRolAplVO:(ArrayList<UsrRolAplVO>)usrRolAplSearchPage.getListResult()){			
				log.debug(funcName + " usrRolAplVO.getRolApl().getCodigo(): " + usrRolAplVO.getRolApl().getCodigo());
				log.debug(funcName + " usrRolAplSearchPage.getUsrApl().getUsername(): " + usrRolAplSearchPage.getUsrApl().getUsername());
				if( SegBussConstants.CODIGO_ROL_ADMINISTRAROR.equals(usrRolAplVO.getRolApl().getCodigo())  && 
						SegBussConstants.USUARIO_ADMIN.equals(usrRolAplSearchPage.getUsrApl().getUsername().trim()) ) {
					usrRolAplVO.setEliminarBussEnabled(false);
					usrRolAplVO.setModificarBussEnabled(false);
	   			}
			}
   		}
        
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return usrRolAplSearchPage;
	}
    
    public UsrRolAplAdapter getUsrRolAplAdapter(CommonKey usrRolAplCommonKey) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
 	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");

 	   UsrRolApl usrRolApl = UsrRolApl.getById(usrRolAplCommonKey.getId()); 
        if (usrRolApl == null) {
        	throw new DemodaServiceException("No se encontro registro de Rol de Usuario de Aplicacion en la Base de Datos.");
        }

        UsrRolAplAdapter usrRolAplAdapter = new UsrRolAplAdapter();
        usrRolAplAdapter.setUsrRolApl((UsrRolAplVO) usrRolApl.toVO(2)); // se podria tunear

 	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");		
		return usrRolAplAdapter;
	}
    
    public UsrRolAplVO createUsrRolApl(UsrRolAplVO usrRolAplVO) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		//limpiamos la lista de errores del VO
		usrRolAplVO.clearError();

		UsrRolApl usrRolApl = new UsrRolApl();  
		// actualizamos elbean con los datos cargados en el VO
		usrRolApl.loadFromVOForCreate(usrRolAplVO);

		// Validaciones de negocio
		if (!usrRolApl.validateCreate()) {
			usrRolApl.passErrorMessages(usrRolAplVO);
			return usrRolAplVO;
		}
		
		SweDAOFactory.getUsrRolAplDAO().update(usrRolApl);
		
		usrRolAplVO.setId(usrRolApl.getId());
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return usrRolAplVO;
	}

    public UsrRolAplVO deleteUsrRolApl(UsrRolAplVO usrRolAplVO) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		//limpiamos la lista de errores del VO
	    usrRolAplVO.clearError();

		// Cargamos el Bean desde la DB para actualizarlo.
		UsrRolApl usrRolApl = UsrRolApl.getById(usrRolAplVO.getId());
        if (usrRolApl == null) {
        	throw new DemodaServiceException("No se encontro registro Rol de Usuario de Aplicacion en la Base de Datos.");
        }

		// Validaciones de negocio
		if (!usrRolApl.validateDelete()) {
			usrRolApl.passErrorMessages(usrRolAplVO);
			return usrRolAplVO;
		}
		
		SweDAOFactory.getUsrRolAplDAO().delete(usrRolApl);
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return usrRolAplVO;
	}

    /*-----------------------------------------------------*/
	/* servicios de Roles    de Aplicacion                 */
	/*-----------------------------------------------------*/
    
    public RolAplSearchPage getRolAplSearchPageInit(CommonKey aplicacionKey) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
			
	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");

	    RolAplSearchPage rolAplSearchPage = new RolAplSearchPage();

	    // Recuperamos el Bean desde la DB .
		Aplicacion aplicacion = Aplicacion.getById(aplicacionKey.getId());
        if (aplicacion == null) {
        	throw new DemodaServiceException("No se encontro registro RolApl en la Base de Datos.");
        }
        rolAplSearchPage.setAplicacion((AplicacionVO) aplicacion.toVO(0));

	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return rolAplSearchPage;
	}
    
    public RolAplSearchPage getRolAplSearchPageInitForCreateUsrRolApl( CommonKey usrAplCommonKey) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
			
	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");

	    RolAplSearchPage rolAplSearchPage = new RolAplSearchPage();

	    // Recuperamos el Bean desde la DB .
	    UsrApl usrApl = UsrApl.getById(usrAplCommonKey.getId());
        if (usrApl == null) {
        	throw new DemodaServiceException("No se encontro registro USrApl en la Base de Datos.");
        }
        rolAplSearchPage.setAplicacion((AplicacionVO) usrApl.getAplicacion().toVO(0));
        rolAplSearchPage.setUsrApl((UsrAplVO) usrApl.toVO(0));
        
        if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return rolAplSearchPage;
	}

    
    public RolAplSearchPage getRolAplSearchPageResult(RolAplSearchPage rolAplSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		rolAplSearchPage.clearError();

		//Aqui realizar validaciones

		// Aqui obtiene lista de BOs
		List<RolApl> listRolApl = SweDAOFactory.getRolAplDAO().findBySearchPage(rolAplSearchPage);

		//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.

		//Aqui pasamos BO a VO
   		rolAplSearchPage.setListResult(ListUtilBean.toVO(listRolApl,3));
	    
		// Segurizacion del registro correspondiente a Administrador de  SWE
   		if (SegBussConstants.CODIGO_SWE.equals(rolAplSearchPage.getAplicacion().getCodigo())){
			for (RolAplVO rolAplVO:(ArrayList<RolAplVO>)rolAplSearchPage.getListResult()){			
				log.debug(funcName + " rolAplVO.getCodigo(): " + rolAplVO.getCodigo());
				if( SegBussConstants.CODIGO_ROL_ADMINISTRAROR.equals(rolAplVO.getCodigo())) {
					rolAplVO.setEliminarBussEnabled(false);
					rolAplVO.setModificarBussEnabled(false);
	   			}
			}
   		}
   		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return rolAplSearchPage;
	}
    
    public RolAplAdapter getRolAplAdapter(CommonKey rolAplKey) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
 	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");

 	    RolApl rolApl = RolApl.getById(rolAplKey.getId());
        if (rolApl == null) {
        	throw new DemodaServiceException("No se encontro registro RolApl en la Base de Datos.");
        }

        RolAplAdapter rolAplAdapter = new RolAplAdapter();
        rolAplAdapter.setRolApl((RolAplVO) rolApl.toVO(1));
        rolAplAdapter.setListPermiteWeb(PermiteWeb.getList(null));

 	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");		
		return rolAplAdapter;
	}
    
    public RolAplAdapter getRolAplAdapterForCreate(CommonKey aplicacionKey) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
 	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");
 	    
 	   RolAplAdapter rolAplAdapter = new RolAplAdapter();
 	    
	    // Recuperamos el Bean desde la DB .
		Aplicacion aplicacion = Aplicacion.getById(aplicacionKey.getId());
        if (aplicacion == null) {
        	throw new DemodaServiceException("No se encontro registro RolApl en la Base de Datos.");
        }
        rolAplAdapter.getRolApl().setAplicacion((AplicacionVO) aplicacion.toVO(0)); 	    
        rolAplAdapter.setListPermiteWeb(PermiteWeb.getList(null));
        
 	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");		
		return rolAplAdapter;
	}

    public RolAplVO createRolApl(RolAplVO rolAplVO) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		//limpiamos la lista de errores del VO
		rolAplVO.clearError();

		RolApl rolApl = new RolApl();  
		// actualizamos elbean con los datos cargados en el VO
		rolApl.loadFromVO(rolAplVO);

		// Validaciones de negocio
		if (!rolApl.validateCreate()) {
			rolApl.passErrorMessages(rolAplVO);
			return rolAplVO;
		}
		
		SweDAOFactory.getRolAplDAO().update(rolApl);
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return rolAplVO;
	}

    public RolAplVO updateRolApl(RolAplVO rolAplVO) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		//limpiamos la lista de errores del VO
		rolAplVO.clearError();

	    // Cargamos el Bean desde la DB para actualizarlo.
		RolApl rolApl = RolApl.getById(rolAplVO.getId());
        if (rolApl == null) {
        	throw new DemodaServiceException("No se encontro registro RolApl en la Base de Datos.");
        }

		// actualizamos el bean con los datos cargados en el VO
        rolApl.loadFromVO(rolAplVO);

		// Validaciones de negocio
		if (!rolApl.validateUpdate()) {
			rolApl.passErrorMessages(rolAplVO);
			return rolAplVO;
		}
		
		SweDAOFactory.getRolAplDAO().update(rolApl);
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return rolAplVO;
	}
    
    public RolAplVO deleteRolApl(RolAplVO rolAplVO) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		//limpiamos la lista de errores del VO
	    rolAplVO.clearError();

		// Cargamos el Bean desde la DB para actualizarlo.
		RolApl rolApl = RolApl.getById(rolAplVO.getId());
        if (rolApl == null) {
        	throw new DemodaServiceException("No se encontro registro RolApl en la Base de Datos.");
        }

		// Validaciones de negocio
		if (!rolApl.validateDelete()) {
			rolApl.passErrorMessages(rolAplVO);
			return rolAplVO;
		}
		
		SweDAOFactory.getRolAplDAO().delete(rolApl);
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return rolAplVO;
	}

    // Item Menu
    
    public ItemMenuSearchPage getItemMenuSearchPageInit(CommonKey aplicacionCommonKey) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
			
	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		ItemMenuSearchPage itemMenuSearchPage = new ItemMenuSearchPage();

	    // Recuperamos el Bean desde la DB .
		Aplicacion aplicacion = Aplicacion.getById(aplicacionCommonKey.getId());
		
        if (aplicacion == null) {
        	throw new DemodaServiceException("No se encontro registro de Aplicacion en la Base de Datos.");
        }
        itemMenuSearchPage.setAplicacion((AplicacionVO) aplicacion.toVO(0));

	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return itemMenuSearchPage;
	}
    
    public ItemMenuSearchPage getItemMenuHijosSearchPageInit(CommonKey itemMenuPadreCommonKey) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
			
	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		ItemMenuSearchPage itemMenuSearchPage = new ItemMenuSearchPage();

	    // Recuperamos el Bean desde la DB .
		ItemMenu itemMenuPadre = ItemMenu.getById(itemMenuPadreCommonKey.getId());
		
        if (itemMenuPadre == null) {
        	throw new DemodaServiceException("No se encontro registro de Item Menu en la Base de Datos.");
        }
        
        itemMenuSearchPage.setItemMenu((ItemMenuVO) itemMenuPadre.toVO(1)); // VER LUEGO
        itemMenuSearchPage.setAplicacion((AplicacionVO) itemMenuPadre.getAplicacion().toVO(0));

	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return itemMenuSearchPage;
	}

    
	public ItemMenuSearchPage getItemMenuSearchPageResult(ItemMenuSearchPage itemMenuSearchPage) throws Exception {
		Integer nivelActual = -1; //nivel actual de los items de esta pagina
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		itemMenuSearchPage.clearError();
		
		// es la lista de resultados
		List<ItemMenuVO> listItemMenuVO = new ArrayList<ItemMenuVO>();
		Aplicacion aplicacion = Aplicacion.getById(itemMenuSearchPage.getAplicacion().getId()); 
		if (aplicacion == null) {
			throw new DemodaServiceException("No se encontro registro de Aplicacion en la Base de Datos.");
		}
		
		if(!ModelUtil.isNullOrEmpty(itemMenuSearchPage.getItemMenu())){
			// estamos trabajando sobre un item de menu
			// Recuperamos el Bean desde la DB .
			ItemMenu itemMenu = ItemMenu.getById(itemMenuSearchPage.getItemMenu().getId()); 
			if (itemMenu == null) {
	        	throw new DemodaServiceException("No se encontro registro de Item de Menu en la Base de Datos.");
	        }
	        // cargo en el listResult la lista de itemMenuHijos del itemMenu sobre el que estamos trabajando
	        listItemMenuVO = (ArrayList<ItemMenuVO>) ListUtilBean.toVO(itemMenu.getListItemMenuHijos(),2);	
			// como todos los ItemMenu de esta pagina pertenecen al mismo nivel
			// lo calculamos una unica vez.
			if (nivelActual == -1) {
				nivelActual = itemMenu.calcularNivel();
			}
		}else{
			// estamos trabajando sobre la lista de menu padres.
			// cargo en el listResult la lista de itemMenu de padres de la aplicacion
			listItemMenuVO = (ArrayList<ItemMenuVO>) ListUtilBean.toVO(aplicacion.getListItemMenuRoot(),2);
			nivelActual = 1; //root
		}

		// recorremos la lista de ItemMenuVO y actualizamos los valores de niveles
		// Estos datos son usados por la bandera getAdministrarHijosEnabled()
		for(ItemMenuVO item: listItemMenuVO) {
			item.setNivel(nivelActual);
			item.setMaxNivel(aplicacion.getMaxNivelMenu());
		}

		//carga de la lista de resultados
        itemMenuSearchPage.setListResult(listItemMenuVO);
	    itemMenuSearchPage.setNivelMenu(nivelActual);

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return itemMenuSearchPage;
	}

    
    public ItemMenuAdapter getItemMenuAdapterForCreateRoot(CommonKey aplicacionCommonKey) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
 	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Aplicacion aplicacion = Aplicacion.getById(aplicacionCommonKey.getId());
        if (aplicacion == null) {
        	throw new DemodaServiceException("No se encontro registro de Aplicacion en la Base de Datos.");
        }
        
        ItemMenuAdapter itemMenuAdapter = new ItemMenuAdapter((AplicacionVO) aplicacion.toVO(0));

 	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");		
		return itemMenuAdapter;
	}
    

    public ItemMenuAdapter getItemMenuAdapterForCreate(CommonKey itemMenuPadreCommonKey) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
 	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		ItemMenu itemMenu = ItemMenu.getById(itemMenuPadreCommonKey.getId());
        if (itemMenu == null) {
        	throw new DemodaServiceException("No se encontro registro de ItemMenu en la Base de Datos.");
        }
        
        ItemMenuAdapter itemMenuAdapter = new ItemMenuAdapter();
        itemMenuAdapter.getItemMenu().setItemMenuPadre((ItemMenuVO) itemMenu.toVO(1));
        itemMenuAdapter.getItemMenu().setAplicacion((AplicacionVO) itemMenu.getAplicacion().toVO(0));

 	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");		
		return itemMenuAdapter;
	}

    public ItemMenuVO createItemMenu(ItemMenuVO itemMenuVO) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		//limpiamos la lista de errores del VO
		itemMenuVO.clearError();

		ItemMenu itemMenu = new ItemMenu();  
		// actualizamos elbean con los datos cargados en el VO
		itemMenu.loadFromVOForCreate(itemMenuVO);

		// Validaciones de negocio
		if (!itemMenu.validateCreate()) {
			itemMenu.passErrorMessages(itemMenuVO);
			return itemMenuVO;
		}
		
		SweDAOFactory.getItemMenuDAO().update(itemMenu); // carga el id en el BO
		itemMenuVO.setId(itemMenu.getId()); // consultar si lo hacemos?
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return itemMenuVO;
	}
	
	public ItemMenuAdapter getItemMenuAdapter(CommonKey itemMenuCommonKey) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
 	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
 	   ItemMenu itemMenu = ItemMenu.getById(itemMenuCommonKey.getId());
        if (itemMenu == null) {
        	throw new DemodaServiceException("No se encontro registro de Item de Menu en la Base de Datos.");
        }

        ItemMenuVO itemMenuVO = (ItemMenuVO) itemMenu.toVO(2);
        if(itemMenu.getItemMenuPadre() != null){
        	itemMenuVO.setItemMenuPadre((ItemMenuVO) itemMenu.getItemMenuPadre().toVO(0));
        }else{
        	itemMenuVO.setItemMenuPadre(new ItemMenuVO());
        }
        
		ItemMenuAdapter itemMenuAdapter = new ItemMenuAdapter(itemMenuVO);
		
 	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");		
		return itemMenuAdapter;
	}
	    
    public ItemMenuAdapter getItemMenuAdapterParam(ItemMenuAdapter itemMenuAdapter) throws Exception {
		    String funcName = DemodaUtil.currentMethodName();
	 	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			
	 	    AccModAplVO amVO = itemMenuAdapter.getItemMenu().getAccModApl();
	 	    if(!ModelUtil.isNullOrEmpty(amVO)){
	 	    	AccModApl accModApl = AccModApl.getById(itemMenuAdapter.getItemMenu().getAccModApl().getId());
	 	 	   if (accModApl == null) {
	 	        	throw new DemodaServiceException("No se encontro registro de Accion Modulo en la Base de Datos.");
	 	        }
	 	 	   amVO = (AccModAplVO) accModApl.toVO(1);
	 	    }else{
	 	    	amVO = new AccModAplVO();
	 	    }
	 	    
	 	   itemMenuAdapter.getItemMenu().setAccModApl(amVO);
	 	   
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");		
			return itemMenuAdapter;
		}


    public ItemMenuVO updateItemMenu(ItemMenuVO itemMenuVO) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		//limpiamos la lista de errores del VO
		itemMenuVO.clearError();

	    // Cargamos el Bean desde la DB para actualizarlo.
		ItemMenu itemMenu = ItemMenu.getById(itemMenuVO.getId());
        if (itemMenu == null) {
        	throw new DemodaServiceException("No se encontro registro ItemMenu en la Base de Datos.");
        }

		// actualizamos el bean con los datos cargados en el VO
		itemMenu.loadFromVOForUpdate(itemMenuVO);

		// Validaciones de negocio
		if (!itemMenu.validateUpdate()) {
			itemMenu.passErrorMessages(itemMenuVO);
			return itemMenuVO;
		}
		
		SweDAOFactory.getItemMenuDAO().update(itemMenu);
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return itemMenuVO;
	}

    public ItemMenuVO deleteItemMenu(ItemMenuVO itemMenuVO) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		//limpiamos la lista de errores del VO
		itemMenuVO.clearError();

		// Cargamos el Bean desde la DB para actualizarlo.
		ItemMenu itemMenu = ItemMenu.getById(itemMenuVO.getId());
        if (itemMenu == null) {
        	throw new DemodaServiceException("No se encontro registro ItemMenu en la Base de Datos.");
        }

		// Validaciones de negocio
		if (!itemMenu.validateDelete()) {
			itemMenu.passErrorMessages(itemMenuVO);
			return itemMenuVO;
		}
		
		SweDAOFactory.getItemMenuDAO().delete(itemMenu);
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return itemMenuVO;
	}
	
    
    /*---------------------------------------------------------------------*/
    /*  Servicios de Rol Accion Modulo   								   */			
    /*---------------------------------------------------------------------*/
    public RolAccModAplSearchPage getRolAccModAplSearchPageInit(CommonKey rolAplKey) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");

	    RolAccModAplSearchPage rolAccModAplSearchPage = new RolAccModAplSearchPage();

	    // Recuperamos el Bean desde la DB .
		RolApl rolApl = RolApl.getById(rolAplKey.getId());
        if (rolApl == null || rolApl.getAplicacion() == null) {
        	throw new DemodaServiceException("No se encontro registro RolApl en la Base de Datos.");
        }
        
        // Seteo el Rol
        rolAccModAplSearchPage.setRolApl((RolAplVO)rolApl.toVO(0));
        
        // Seteo la Aplicacion
        rolAccModAplSearchPage.setAplicacion((AplicacionVO) rolApl.getAplicacion().toVO(0));
        
        // Busco los Modulos de la Aplicacion        
        ArrayList<ModApl>  listModApl = (ArrayList<ModApl>) rolApl.getAplicacion().getListModAplActivos();
        
        rolAccModAplSearchPage.setListModApl(ListUtilBean.toVO(listModApl,
														   new ModAplVO(-1, StringUtil.SELECT_OPCION_TODOS)));

	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return rolAccModAplSearchPage;
    	
    }
    
    public RolAccModAplSearchPage getRolAccModAplSearchPageResult(RolAccModAplSearchPage rolAccModAplSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		rolAccModAplSearchPage.clearError();

		//Aqui realizar validaciones

		// Aqui obtiene lista de BOs
		List<RolAccModApl> listRolAccModApl = SweDAOFactory.getRolAccModAplDAO().findBySearchPage(rolAccModAplSearchPage);

		//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.

		//Aqui pasamos BO a VO
		rolAccModAplSearchPage.setListResult(ListUtilBean.toVO(listRolAccModApl,3));
	        
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return rolAccModAplSearchPage;
	}

    public RolAccModAplAdapter getRolAccModAplAdapter(CommonKey rolAccModAplKey) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");
	    
	    // Recuperamos el Bean desde la DB .
		RolAccModApl rolAccModApl = RolAccModApl.getById(rolAccModAplKey.getId());
        if (rolAccModApl == null ) {
        	throw new DemodaServiceException("No se encontro registro RolAccModApl en la Base de Datos.");
        }
        
        RolAccModAplAdapter rolAccModAplAdapter = new RolAccModAplAdapter(); 
        rolAccModAplAdapter.setRolAccModApl((RolAccModAplVO)rolAccModApl.toVO(3));
        
	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
	    
	    return rolAccModAplAdapter;
    } 
    
    
    public RolAccModAplVO createRolAccModApl(RolAccModAplVO rolAccModAplVO) throws Exception {
    	 String funcName = DemodaUtil.currentMethodName();
 		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

 		//limpiamos la lista de errores del VO
 		rolAccModAplVO.clearError();

 		RolAccModApl rolAccModApl = new RolAccModApl();  
 		// actualizamos elbean con los datos cargados en el VO
 		rolAccModApl.loadFromVO(rolAccModAplVO);

 		// Validaciones de negocio
 		if (!rolAccModApl.validateCreate()) {
 			rolAccModApl.passErrorMessages(rolAccModAplVO);
 			return rolAccModAplVO;
 		}
 		
 		SweDAOFactory.getRolAccModAplDAO().update(rolAccModApl);
 		
 	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
 		return rolAccModAplVO;
    }
    
    public RolAccModAplVO deleteRolAccModApl(RolAccModAplVO rolAccModAplVO) throws Exception {
	    String funcName = DemodaUtil.currentMethodName();
	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		//limpiamos la lista de errores del VO
	    rolAccModAplVO.clearError();

		// Cargamos el Bean desde la DB para actualizarlo.
		RolAccModApl rolAccModApl = RolAccModApl.getById(rolAccModAplVO.getId());
        if (rolAccModApl == null) {
        	throw new DemodaServiceException("No se encontro registro RolAccModApl en la Base de Datos.");
        }

		// Validaciones de negocio
		if (!rolAccModApl.validateDelete()) {
			rolAccModApl.passErrorMessages(rolAccModAplVO);
			return rolAccModAplVO;
		}
		
		SweDAOFactory.getRolAccModAplDAO().delete(rolAccModApl);
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return rolAccModAplVO;
	}
}	
