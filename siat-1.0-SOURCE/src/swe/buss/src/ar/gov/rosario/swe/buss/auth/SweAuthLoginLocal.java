//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.buss.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

import ar.gov.rosario.swe.SweDAOFactory;
import ar.gov.rosario.swe.buss.bean.Aplicacion;
import ar.gov.rosario.swe.buss.bean.UsrAuth;
import ar.gov.rosario.swe.iface.model.UsrAplVO;
import ar.gov.rosario.swe.iface.model.UsuarioVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class SweAuthLoginLocal extends SweAuthLogin {

	private Logger log = Logger.getLogger(SweAuthLoginLocal.class);
	
	
	public String login(UsuarioVO usuarioVO, String codApp) {
		
		String funcName = DemodaUtil.currentMethodName();		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");	
		String searchResult = SWE_AUTH_INVALID_LOGIN;		
		
		try {
						
			//Encripto en MD5 el password ingresado
			String passEncMD5 = md5Encode(usuarioVO.getPassword());
									
			UsrAuth usrAuth = SweDAOFactory.getUsrAuthDAO().getByUsrNamePassApp(usuarioVO.getPassword(),passEncMD5, codApp);
			
			//Verifico que haya un usuario con los criterios ingresados
			if (null != usrAuth) {
				searchResult = SWE_AUTH_SUCCESS_LOGIN;
			}
						
		} catch (NoSuchAlgorithmException e) {			
				e.printStackTrace();
		}	    
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");	
		
		return searchResult;	
			
	}
	
	
	public String createUser(UsrAplVO usrAplVO) throws Exception {
		
		UsrAuth usrAuth = new UsrAuth();		
		return saveOrUpdateUser(usrAplVO, usrAuth);
	}
	
	public String updateUser(UsrAplVO usrAplVO, String oldUsrName) throws Exception {		
		
		UsrAuth usrAuth = SweDAOFactory.getUsrAuthDAO().getByUsrName(oldUsrName);

		return saveOrUpdateUser(usrAplVO, usrAuth);
	}
	
	public String deleteUser(String usrName) {
		
		String deleteResult = SWE_AUTH_FAIL_DELETE;
		UsrAuth usrAuth = SweDAOFactory.getUsrAuthDAO().getByUsrName(usrName);
		
		if (null != usrAuth) {
			SweDAOFactory.getUsrAuthDAO().delete(usrAuth);
			deleteResult = SWE_AUTH_SUCCESS_UPDATE;
		}	
		return deleteResult;
	}
	
	private String saveOrUpdateUser(UsrAplVO usrAplVO, UsrAuth usrAuth) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String updateResult = SWE_AUTH_INVALID_UPDATE;
		Long idUsrAuth;	
				
			
		
		if (StringUtil.isNullOrEmpty(usrAplVO.getPassword())) {
			return SweAuthLogin.SWE_AUTH_INVALID_EMPTY_PASSWORD;
		}		
		
		if (StringUtil.isNullOrEmpty(usrAplVO.getPassRetype())) {
			return SWE_AUTH_INVALID_RETYPE_PASSWORD;
		}
		
		if (!usrAplVO.getPassRetype().equals(usrAplVO.getPassword())) {
			return SWE_AUTH_INVALID_RETYPE_PASSWORD;
		}
		
		
		Aplicacion aplicacion = (Aplicacion) SweDAOFactory.getAplicacionDAO().getById(usrAplVO.getAplicacion().getId());
		
		usrAuth.setNomUsuario(usrAplVO.getUsername());
		usrAuth.setPassword(md5Encode(usrAplVO.getPassword()));
		usrAuth.setFechaUltMdf(usrAplVO.getFechaUltMdf());	
		usrAuth.setEstado((Estado.ACTIVO.getId()));				
		usrAuth.setAplicacion(aplicacion);		

		if (!usrAuth.validateUpdate()) {
			if (log.isDebugEnabled()) log.debug("Error: " + SWE_AUTH_INVALID_USR_NAME + "después de llamar a validateUpdate()");
			return SWE_AUTH_INVALID_USR_NAME;
				
		}
		
		idUsrAuth = SweDAOFactory.getUsrAuthDAO().update(usrAuth);
		if (null != idUsrAuth) {
			updateResult = SWE_AUTH_SUCCESS_UPDATE;
		}
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");	
		
		return updateResult;
		
	}
		
	
	private String md5Encode(String pass) throws NoSuchAlgorithmException { 
		
	    MessageDigest md = MessageDigest.getInstance("MD5"); //$NON-NLS-1$  
	    byte[] inputBytes = md.digest(pass.getBytes());  	  
	    int size = inputBytes.length;	    
	    StringBuffer hash = new StringBuffer(size); 
	    
	    for (int i = 0; i < size; i++)  {
	    	
	        int u = inputBytes[i] & 255; // unsigned conversion  
	        
	    	if (u < 16) {
				hash.append("0" + Integer.toHexString(u));
			} else {
				hash.append(Integer.toHexString(u));
			} 
	    }
	    
	   return hash.toString();
	}


}
