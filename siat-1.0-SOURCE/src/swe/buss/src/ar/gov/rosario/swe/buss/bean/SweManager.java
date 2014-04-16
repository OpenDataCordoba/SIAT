//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.buss.bean;


import java.util.List;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.swe.SweCommonError;
import ar.gov.rosario.swe.SweDAOFactory;
import ar.gov.rosario.swe.buss.auth.SweAuthLogin;
import ar.gov.rosario.swe.buss.auth.SweAuthLoginLocal;
import ar.gov.rosario.swe.iface.model.ItemMenuVO;
import ar.gov.rosario.swe.iface.model.PermiteWeb;
import ar.gov.rosario.swe.iface.model.SweContext;
import ar.gov.rosario.swe.iface.model.SweUserSession;
import ar.gov.rosario.swe.iface.model.UsuarioVO;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.DemodaStringMsg;

/**
 * Manejador de Seguridad
 * Singleton
 * @author tecso
 *
 */
public class SweManager  {
	
	private static Log log = LogFactory.getLog(SweManager.class);	
	private static final SweManager INSTANCE = new SweManager();

	/**
	 * Constructor privado
	 */
	private SweManager() {
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static SweManager getInstance() {
		return INSTANCE;
	}
	
	@SuppressWarnings("unchecked")
	public SweContext getSweContext(String codApp) throws Exception {
		SweContext sweContext = new SweContext();
		Aplicacion aplicacion;

		aplicacion = SweDAOFactory.getAplicacionDAO().findByCodigo(codApp);
		if (aplicacion == null) {
			throw new DemodaServiceException("No se encontro Aplicacion con codigo '" + codApp + "'");
		}

		/* cargamos el mapa con todas las accion/metodo por id */
		/* Este mapa luego los SweContext.hasAccess() para verificar accesos de cada usuario */
		List<AccModApl> acciones = aplicacion.getListAccApl(); //lista de todas las acciones de la aplicacion
		for (AccModApl acc: acciones) {
			sweContext.putAccionMetodo(acc.getNombreAccion(), acc.getNombreMetodo(), 
									   acc.getId().toString(), acc.getDescripcion());
		}


		/* cargamos el menu de la aplicacion */
		List<ItemMenu> listItemMenu = aplicacion.getListItemMenuRoot();
		List<ItemMenuVO> listItemMenuVO = ListUtilBean.toVO(listItemMenu, 100);
		sweContext.setListItemMenuApp(listItemMenuVO);

		return sweContext;
	}
	

	@SuppressWarnings("unchecked")
	public List<ItemMenuVO> getListItemMenu(String codApp) throws Exception {
		Aplicacion aplicacion;

		aplicacion = SweDAOFactory.getAplicacionDAO().findByCodigo(codApp);
		if (aplicacion == null) {
			throw new DemodaServiceException("No se encontro Aplicacion con codigo '" + codApp + "'");
		}
		/* cargamos el menu de la aplicacion */
		List<ItemMenu> listItemMenu = aplicacion.getListItemMenuRoot();
		List<ItemMenuVO> listItemMenuVO = ListUtilBean.toVO(listItemMenu, 100);
		return listItemMenuVO;
	}
	
	/**
	 * Devuelve un us segun esecificacion del servicio
	 * @param userVO
	 * @return
	 * @throws ServiceException
	 */
	public SweUserSession login(String codigoAplicacion, UsuarioVO usuarioVO, boolean anonimo) throws Exception {
		SweUserSession sweUserSession = new SweUserSession();
		
		sweUserSession.setCodApplication(codigoAplicacion);
		
		if (StringUtil.isNullOrEmpty(usuarioVO.getUsername())) {
			log.error("login():FAIL:El nombre de usuario ingresado es blanco o nulo");
			sweUserSession.addNonRecoverableError(SweCommonError.USRAUTH_NOMUSR_REQUIRED);
		}
		
		if (StringUtil.isNullOrEmpty(usuarioVO.getPassword())) {
			log.error("login():FAIL:El password ingresado es blanco o nulo");
			sweUserSession.addNonRecoverableError(SweCommonError.USRAUTH_PASS_REQUIRED);
		}
		
		if (sweUserSession.hasError()) {
			log.error("login():FAIL: falla login en loginSegWeb() error:" + sweUserSession.errorString() + " username:" + usuarioVO.getUsername() + " aplicacion:" + codigoAplicacion);
			return sweUserSession;
		}
		
		if (!anonimo)
			sweUserSession = loginSWE(usuarioVO, sweUserSession);		
		
		if (sweUserSession.hasError()) {
			log.error("login():FAIL: falla login en loginSegWeb() error:" + sweUserSession.errorString() + " username:" + usuarioVO.getUsername() + " aplicacion:" + codigoAplicacion);
			return sweUserSession;
		}

		// busca si existe el usuario
		UsrApl usuario = SweDAOFactory.getUsrAplDAO().findByAplUsernameActivo(codigoAplicacion, usuarioVO.getUsername());
		if (usuario == null) {
			log.error("login():FAIL: no se encontro usuario en swe_usrapl para aplicacion. username:" + usuarioVO.getUsername() + " aplicacion:" + codigoAplicacion);
			sweUserSession.addRecoverableError(SweCommonError.LOGIN_NOUSRAPL);
			return sweUserSession;
		}
		
		List<AccModApl> listAccionUsr = usuario.getListAccModUsr(); //Acciones validas del usuario
		if (listAccionUsr == null || listAccionUsr.size() == 0) {
			log.error("login():FAIL: no se encontro ninguna accion disponible para el usuario. username:" + usuarioVO.getUsername() + " aplicacion:" + codigoAplicacion);
			sweUserSession.addRecoverableError(SweCommonError.LOGIN_NOACC);
			return sweUserSession;
		}	
		
		//Pasamos la lista de AccionesModulos del SAT a los SysAction de Demoda
		String idsAccionesModActivos =",";
		for (AccModApl accionMod: listAccionUsr ) {
			idsAccionesModActivos = idsAccionesModActivos + accionMod.getId().toString() + ",";   
		}

		
		//Analisis de los roles
		String idsRol =",";
		String codsRol =",";
		List<UsrRolApl> listUsrRolApl = usuario.getListUsrRolApl();
		for (UsrRolApl usrRol: listUsrRolApl) {
			RolApl rol = usrRol.getRolApl();
			idsRol = idsRol + rol.getId().toString() + ",";
			codsRol = codsRol + rol.getCodigo() + ",";
		}

		// Setea permiteWeb de usrApl
		if(usuario.getPermiteWeb()==null || usuario.getPermiteWeb().equals(PermiteWeb.NO_PERMITE_WEB.getId()))
			sweUserSession.setPermiteWeb(false);
		else if(usuario.getPermiteWeb().equals(PermiteWeb.PERMITE_WEB.getId()))
			sweUserSession.setPermiteWeb(true);
		
		sweUserSession.setIdsAccionesModuloUsuario(idsAccionesModActivos);
		sweUserSession.setIdsRoles(idsRol);
		sweUserSession.setCodsRoles(codsRol);
		sweUserSession.setIdUsuarioSwe(usuario.getId());

		log.info("login():OK: ids acciones pemitidas:" + idsAccionesModActivos + " username:" + usuarioVO.getUsername() + " aplicacion:" + codigoAplicacion + " permiteWeb:" + sweUserSession.getPermiteWeb());
		log.info("login():OK: codigo roles pemitidos:" +  codsRol + " username:" + usuarioVO.getUsername() + " aplicacion:" + codigoAplicacion + " permiteWeb:" + sweUserSession.getPermiteWeb());
		
		return sweUserSession;
	}

	/**
	 * Solicita el login 
	 * retorna 0 si Ok o codigo de error
	 * 
	 * @param UsuarioVO
	 * @return
	 */
	private SweUserSession loginSWE(UsuarioVO usuarioVO, SweUserSession sweUserSession) {
		try {
			String loginResult = "";
			SweAuthLogin sweAuthLocalLogin = new SweAuthLoginLocal();

			//Realizo login
			if ("off".equals(System.getenv("SWE_CHECKLOGIN"))) {
				loginResult = SweAuthLogin.SWE_AUTH_SUCCESS_LOGIN;
			} else {			
				loginResult = sweAuthLocalLogin.login(usuarioVO, sweUserSession.getCodApplication());
				if (!loginResult.equals(SweAuthLogin.SWE_AUTH_SUCCESS_LOGIN)) {
					sweUserSession.addNonRecoverableError(SweCommonError.USRAUTH_LOGIN_ERROR);
				}
			}
			return sweUserSession;
		} catch (Exception swe) {
			log.info("SWE: Fallo login:", swe);
			DemodaStringMsg dsm = new DemodaStringMsg(0, swe.getMessage()); 
			sweUserSession.addNonRecoverableError(dsm);
			return sweUserSession;
		}
	}
}
