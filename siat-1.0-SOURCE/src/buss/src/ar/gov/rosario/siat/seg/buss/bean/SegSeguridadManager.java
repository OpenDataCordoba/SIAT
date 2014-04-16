//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.seg.buss.bean;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.seg.buss.dao.SegDAOFactory;
import ar.gov.rosario.siat.seg.iface.util.SegError;
import ar.gov.rosario.swe.iface.model.UsuarioVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.UserContext;

/**
 * Manejador del m&oacute;dulo Seg y submodulo ${Submodulo}
 * 
 * @author tecso
 *
 */
public class SegSeguridadManager {
		
	private static Logger log = Logger.getLogger(SegSeguridadManager.class);
	
	private static final SegSeguridadManager INSTANCE = new SegSeguridadManager();
	
	
	/**
	 * Constructor privado
	 */
	private SegSeguridadManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static SegSeguridadManager getInstance() {
		return INSTANCE;
	}

	// ---> ABM UsuarioSiat
	public UsuarioSiat createUsuarioSiat(UsuarioSiat usuarioSiat) throws Exception {

		// Validaciones de negocio
		if (!usuarioSiat.validateCreate()) {
			return usuarioSiat;
		}

		SegDAOFactory.getUsuarioSiatDAO().update(usuarioSiat);

		return usuarioSiat;
	}
	
	public UsuarioSiat updateUsuarioSiat(UsuarioSiat usuarioSiat) throws Exception {
		
		// Validaciones de negocio
		if (!usuarioSiat.validateUpdate()) {
			return usuarioSiat;
		}

		SegDAOFactory.getUsuarioSiatDAO().update(usuarioSiat);
		
		return usuarioSiat;
	}
	
	public UsuarioSiat deleteUsuarioSiat(UsuarioSiat usuarioSiat) throws Exception {
	
		// Validaciones de negocio
		if (!usuarioSiat.validateDelete()) {
			return usuarioSiat;
		}
		
		SegDAOFactory.getUsuarioSiatDAO().delete(usuarioSiat);
		
		return usuarioSiat;
	}
	// <--- ABM UsuarioSiat
	
	/**
	 * Cambio de password a travez de MCRFacade
	 * 
	 * 
	 */
	public UsuarioVO changePassword(UserContext userSession, UsuarioVO userVO) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
            	// ontiene el mCRLoginFacade guardado en userSession
				//MCRLoginFacade mCRLoginFacade = (MCRLoginFacade) userSession.getMcrLoginFacade();

				// obtiene el id de la session de tomcar tambien guardado en el userSession
            	String sessId = (String) userVO.getIdSession();

            	// no debiera ocurrir, pero si el id de session es nulo, va un 999
            	if (sessId == null) 
                	sessId = "999";

            	// invoca a segWeb
            	//mCRLoginFacade.changePasswd(userVO.getNewPassword(), sessId  );

            	//XXX siatgpl:implement
            	
	            if (log.isDebugEnabled()) log.debug(funcName + ": exit");
	            
	            // estamos Ok
	            return userVO;
		} catch (Exception swe) {
			log.error(swe.getMessage());

            swe.printStackTrace();

			// salimos con error
            userVO.addRecoverableError(SegError.MSG_SERVICE_ERROR);
            
            return userVO;
		}
	}
		

}