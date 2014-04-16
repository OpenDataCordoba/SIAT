//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.iface.service;

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
import ar.gov.rosario.swe.iface.model.RolAccModAplAdapter;
import ar.gov.rosario.swe.iface.model.RolAccModAplSearchPage;
import ar.gov.rosario.swe.iface.model.RolAccModAplVO;
import ar.gov.rosario.swe.iface.model.RolAplAdapter;
import ar.gov.rosario.swe.iface.model.RolAplSearchPage;
import ar.gov.rosario.swe.iface.model.RolAplVO;
import ar.gov.rosario.swe.iface.model.SweContext;
import ar.gov.rosario.swe.iface.model.SweUserSession;
import ar.gov.rosario.swe.iface.model.UsrAplAdapter;
import ar.gov.rosario.swe.iface.model.UsrAplSearchPage;
import ar.gov.rosario.swe.iface.model.UsrAplVO;
import ar.gov.rosario.swe.iface.model.UsrRolAplAdapter;
import ar.gov.rosario.swe.iface.model.UsrRolAplSearchPage;
import ar.gov.rosario.swe.iface.model.UsrRolAplVO;
import ar.gov.rosario.swe.iface.model.UsuarioVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

/**
 * @author tecso
 *
 */
public interface ISweService  {	

	
	/**
	 * Devuelve el contexto para una applicacion
	 */
	public SweContext getSweContext(String codApp) throws DemodaServiceException;
	
	/**
	 * Login de un usuario en SWE
	 * @param usuarioVO
	 * @return
	 * @throws DemodaServiceException
	 */
	public SweUserSession login(String codigoAplicacion, UsuarioVO usuarioVO) throws DemodaServiceException;
	public SweUserSession login(String codigoAplicacion, UsuarioVO usuarioVO, boolean anonimo) throws DemodaServiceException;
		
	// ABM Aplicacion
	public AplicacionAdapter getAplicacionAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public AplicacionAdapter getAplicacionAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public AplicacionAdapter getAplicacionAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public AplicacionAdapter getAplicacionAdapterForDelete(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public AplicacionVO createAplicacion(UserContext userContext, AplicacionVO aplicacionVO ) throws DemodaServiceException;
	public AplicacionVO updateAplicacion(UserContext userContext, AplicacionVO aplicacionVO ) throws DemodaServiceException;
	public AplicacionVO deleteAplicacion(UserContext userContext, AplicacionVO aplicacionVO ) throws DemodaServiceException;

	public AplicacionSearchPage getAplicacionSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public AplicacionSearchPage getAplicacionSearchPageResult(UserContext usercontext, AplicacionSearchPage aplicacionSearchPage) throws DemodaServiceException;
	
	// ABM de Modulos Aplicacion
	public ModAplAdapter getModAplAdapterForView(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException;
	public ModAplAdapter getModAplAdapterForCreate(UserContext userContext, CommonKey aplicacionKey) 
		throws DemodaServiceException;
	public ModAplAdapter getModAplAdapterForUpdate(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException;
	public ModAplAdapter getModAplAdapterForDelete(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException;

	public ModAplVO createModApl(UserContext userContext, ModAplVO aplicacionVO ) throws DemodaServiceException;
	public ModAplVO updateModApl(UserContext userContext, ModAplVO aplicacionVO ) throws DemodaServiceException;
	public ModAplVO deleteModApl(UserContext userContext, ModAplVO aplicacionVO ) throws DemodaServiceException;

	public ModAplSearchPage getModAplSearchPageInit(UserContext usercontext, CommonKey aplicacionKey) 
		throws DemodaServiceException;
	public ModAplSearchPage getModAplSearchPageResult(UserContext usercontext, 
		ModAplSearchPage aplicacionSearchPage) throws DemodaServiceException;
	// Fin ABM de Modulos Aplicacion	
	
	// ABM de Usuarios de Aplicacion
	public UsrAplAdapter getUsrAplAdapterForView(UserContext userContext, CommonKey commonKeyUsrApl) throws DemodaServiceException;
	public UsrAplAdapter getUsrAplAdapterForCreate(UserContext userContext, CommonKey commonKeyAplicacion ) throws DemodaServiceException;
	public CloneUsrAplAdapter getCloneUsrAplAdapterForCreate(UserContext userContext, CommonKey commonKeyAplicacion ) throws DemodaServiceException;
	public UsrAplAdapter getUsrAplAdapterForUpdate(UserContext userContext, CommonKey commonKeyUsrApl) throws DemodaServiceException;
	public UsrAplAdapter getUsrAplAdapterForDelete(UserContext userContext, CommonKey commonKeyUsrApl) throws DemodaServiceException;
	
	public UsrAplVO createUsrApl(UserContext userContext, UsrAplVO usrAplVO ) throws DemodaServiceException;
	public UsrAplVO cloneUsrApl(UserContext userContext, UsrAplVO usrAplVO, UsrAplVO usrAplToCloneVO ) throws DemodaServiceException;
	public UsrAplVO updateUsrApl(UserContext userContext, UsrAplVO usrAplVO ) throws DemodaServiceException;
	public UsrAplVO deleteUsrApl(UserContext userContext, UsrAplVO usrAplVO ) throws DemodaServiceException;

	public UsrAplSearchPage getUsrAplSearchPageInit(UserContext userContext, CommonKey aplicacionKey) throws DemodaServiceException;
	public UsrAplSearchPage getUsrAplSearchPageResult(UserContext userContext, UsrAplSearchPage usrAplSearchPage) throws DemodaServiceException;
	// FIN de Usuarios de Aplicacion

	// ABM de Roles de Usuarios de Aplicacion
	public UsrRolAplSearchPage getUsrRolAplSearchPageInit(UserContext userContext, CommonKey usrAplCommonKey) throws DemodaServiceException;
	public UsrRolAplSearchPage getUsrRolAplSearchPageResult(UserContext userContext, UsrRolAplSearchPage usrRolAplSearchPage) throws DemodaServiceException;
	
	public UsrRolAplAdapter getUsrRolAplAdapterForView(UserContext userContext, CommonKey usrRolAplCommonKey) throws DemodaServiceException;
	public UsrRolAplAdapter getUsrRolAplAdapterForDelete(UserContext userContext, CommonKey usrRolAplCommonKey) throws DemodaServiceException;
	
	public UsrRolAplVO createUsrRolApl(UserContext userContext, UsrRolAplVO usrAplVO ) throws DemodaServiceException;
	public UsrRolAplVO deleteUsrRolApl(UserContext userContext, UsrRolAplVO usrAplVO ) throws DemodaServiceException;

	// Fin de ABM de Roles de Usuarios de Aplicacion
	
	
	
	
	
	// ABM de Roles de Aplicacion
	public RolAplSearchPage getRolAplSearchPageInit(UserContext usercontext, CommonKey aplicacionKey) throws DemodaServiceException;
	public RolAplSearchPage getRolAplSearchPageInitForCreateUsrRolApl(UserContext usercontext, CommonKey usrAplCommonKey) throws DemodaServiceException;
	
	public RolAplSearchPage getRolAplSearchPageResult(UserContext usercontext, RolAplSearchPage rolAplSearchPage) throws DemodaServiceException;
	
	public RolAplAdapter getRolAplAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public RolAplAdapter getRolAplAdapterForCreate(UserContext userContext, CommonKey aplicacionKey) throws DemodaServiceException;
	public RolAplAdapter getRolAplAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public RolAplAdapter getRolAplAdapterForDelete(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public RolAplVO createRolApl(UserContext userContext, RolAplVO rolAplVO ) throws DemodaServiceException;
	public RolAplVO updateRolApl(UserContext userContext, RolAplVO rolAplVO ) throws DemodaServiceException;
	public RolAplVO deleteRolApl(UserContext userContext, RolAplVO rolAplVO ) throws DemodaServiceException;
	// Fin ABM de Roles de Aplicacion
	
	// ABM de Acciones de Modulos Aplicacion
	public AccModAplSearchPage getAccModAplSearchPageInit(UserContext usercontext, AccModAplSearchPage accModAplSearchPage) 
		throws DemodaServiceException;
	public AccModAplSearchPage getAccModAplSearchPageParamMod(UserContext userContext, AccModAplSearchPage accModAplSearchPage) throws DemodaServiceException;
	public AccModAplSearchPage getAccModAplSearchPageResult(UserContext usercontext, 
			AccModAplSearchPage aplicacionSearchPage) throws DemodaServiceException;
	
	public AccModAplAdapter getAccModAplAdapterForView(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException;
	public AccModAplAdapter getAccModAplAdapterForCreate(UserContext userContext, CommonKey modAplKey) 
		throws DemodaServiceException;
	public AccModAplAdapter getAccModAplAdapterForUpdate(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException;
	public AccModAplAdapter getAccModAplAdapterForDelete(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException;

	public AccModAplVO createAccModApl(UserContext userContext, AccModAplVO accModAplVO ) 
		throws DemodaServiceException;
	public AccModAplVO updateAccModApl(UserContext userContext, AccModAplVO accModAplVO ) 
		throws DemodaServiceException;
	public AccModAplVO deleteAccModApl(UserContext userContext, AccModAplVO accModAplVO ) 
		throws DemodaServiceException;
	// Fin ABM de Acciones de Modulos Aplicacion	

	
	// Seleccion de Rol Accion Modulo de la Aplicacion
	public RolAccModAplSearchPage getRolAccModAplSearchPageInit(UserContext userContext, CommonKey commonKey)
		throws DemodaServiceException;
	
	public RolAccModAplSearchPage getRolAccModAplSearchPageResult(UserContext userContext, RolAccModAplSearchPage rolAccModAplSearchPage)
		throws DemodaServiceException;
	
	public RolAccModAplAdapter getRolAccModAplAdapterForView(UserContext userContext, CommonKey commonKey)
		throws DemodaServiceException;
	
	public RolAccModAplAdapter getRolAccModAplAdapterForDelete(UserContext userContext, CommonKey commonKey)
		throws DemodaServiceException;
	
	public RolAplVO createRolAccModAplMultiple(UserContext userContext, RolAplVO rolAplVO, Long[] listId) throws DemodaServiceException;

	public RolAccModAplVO deleteRolAccModApl(UserContext userContext, RolAccModAplVO rolAccModAplVO) throws DemodaServiceException;

	// Fin Rol Accion Modulo Apl
	
	
	// Item de Menu
	public ItemMenuSearchPage getItemMenuSearchPageInit(UserContext userContext, CommonKey aplicacionCommonKey) throws DemodaServiceException;
	public ItemMenuSearchPage getItemMenuHijosSearchPageInit(UserContext userContext, CommonKey itemMenuPadreCommonKey) throws DemodaServiceException;
	public ItemMenuSearchPage getItemMenuSearchPageResult(UserContext userContext, ItemMenuSearchPage itemMenuSearchPage) throws DemodaServiceException;
	
	public ItemMenuAdapter getItemMenuAdapterForView(UserContext userContext, CommonKey itemMenuCommonKey) throws DemodaServiceException;
	public ItemMenuAdapter getItemMenuAdapterForCreateRoot(UserContext userContext, CommonKey aplicacionCommonKey) throws DemodaServiceException;
	public ItemMenuAdapter getItemMenuAdapterForCreate(UserContext userContext, CommonKey itemMenuCommonKey) throws DemodaServiceException;
	public ItemMenuAdapter getItemMenuAdapterForUpdate(UserContext userContext, CommonKey itemMenuCommonKey) throws DemodaServiceException;
	public ItemMenuAdapter getItemMenuAdapterParam(UserContext userContext, ItemMenuAdapter itemMenuAdapter) throws DemodaServiceException;
	public ItemMenuAdapter getItemMenuAdapterForDelete(UserContext userContext, CommonKey itemMenuCommonKey) throws DemodaServiceException;
	
	public ItemMenuVO createItemMenu(UserContext userContext, ItemMenuVO itemMenuVO ) throws DemodaServiceException;
	public ItemMenuVO updateItemMenu(UserContext userContext, ItemMenuVO itemMenuVO ) throws DemodaServiceException;
	public ItemMenuVO deleteItemMenu(UserContext userContext, ItemMenuVO itemMenuVO ) throws DemodaServiceException;
	// Fin de Item de Menu
	
	
}
