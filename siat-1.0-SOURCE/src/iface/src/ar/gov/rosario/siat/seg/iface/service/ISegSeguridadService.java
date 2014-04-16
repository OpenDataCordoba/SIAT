//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.seg.iface.service;

import java.util.List;

import ar.gov.rosario.siat.seg.iface.model.MenuAdapter;
import ar.gov.rosario.siat.seg.iface.model.OficinaAdapter;
import ar.gov.rosario.siat.seg.iface.model.OficinaSearchPage;
import ar.gov.rosario.siat.seg.iface.model.OficinaVO;
import ar.gov.rosario.siat.seg.iface.model.UsuarioSiatAdapter;
import ar.gov.rosario.siat.seg.iface.model.UsuarioSiatSearchPage;
import ar.gov.rosario.siat.seg.iface.model.UsuarioSiatVO;
import ar.gov.rosario.swe.iface.model.ItemMenuVO;
import ar.gov.rosario.swe.iface.model.UsuarioVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface ISegSeguridadService {
	public MenuAdapter getMenu(UserContext userContext, MenuAdapter menuAdapter ) throws DemodaServiceException;
	public ItemMenuVO getItemMenu(CommonKey itemMenuKey, List<ItemMenuVO> listItemMenuVOUsuario) throws DemodaServiceException;

	public UsuarioSiatVO getUsuarioSiatForLogin(UserContext userContext) throws DemodaServiceException;

	public UsuarioVO changePass(UserContext userSession, UsuarioVO userVO) throws DemodaServiceException;
	
	// ---> ABM UsuarioSiat
	public UsuarioSiatSearchPage getUsuarioSiatSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public UsuarioSiatSearchPage getUsuarioSiatSearchPageResult(UserContext usercontext, UsuarioSiatSearchPage usuarioSiatSearchPage) throws DemodaServiceException;

	public UsuarioSiatAdapter getUsuarioSiatAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public UsuarioSiatAdapter getUsuarioSiatAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public UsuarioSiatAdapter getUsuarioSiatAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;

	public UsuarioSiatVO createUsuarioSiat(UserContext userContext, UsuarioSiatVO usuarioSiatVO ) throws DemodaServiceException;
	public UsuarioSiatVO updateUsuarioSiat(UserContext userContext, UsuarioSiatVO usuarioSiatVO ) throws DemodaServiceException;
	public UsuarioSiatVO deleteUsuarioSiat(UserContext userContext, UsuarioSiatVO usuarioSiatVO ) throws DemodaServiceException;
	public UsuarioSiatVO activarUsuarioSiat(UserContext userContext, UsuarioSiatVO usuarioSiatVO ) throws DemodaServiceException;
	public UsuarioSiatVO desactivarUsuarioSiat(UserContext userContext, UsuarioSiatVO usuarioSiatVO ) throws DemodaServiceException;	
	// <--- ABM UsuarioSiat

	
	// ---> ABM Oficina
	public OficinaSearchPage getOficinaSearchPageInit(UserContext usercontext, CommonKey areaKey) throws DemodaServiceException;
	public OficinaSearchPage getOficinaSearchPageResult(UserContext usercontext, OficinaSearchPage oficinaSearchPage) throws DemodaServiceException;

	public OficinaAdapter getOficinaAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public OficinaAdapter getOficinaAdapterForCreate(UserContext userContext, CommonKey areaKey) throws DemodaServiceException;
	public OficinaAdapter getOficinaAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public OficinaVO createOficina(UserContext userContext, OficinaVO oficinaVO ) throws DemodaServiceException;
	public OficinaVO updateOficina(UserContext userContext, OficinaVO oficinaVO ) throws DemodaServiceException;
	public OficinaVO deleteOficina(UserContext userContext, OficinaVO oficinaVO ) throws DemodaServiceException;
	public OficinaVO activarOficina(UserContext userContext, OficinaVO oficinaVO ) throws DemodaServiceException;
	public OficinaVO desactivarOficina(UserContext userContext, OficinaVO oficinaVO ) throws DemodaServiceException;	
	//
}
