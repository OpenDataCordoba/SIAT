//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.iface.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.Common;
import coop.tecso.demoda.iface.model.IDemodaContext;

/**
 * 
 * @author tecso
 *
 */
public class SweContext extends Common implements IDemodaContext  {
	
	private static final long serialVersionUID = 1L;

	private static Log log = LogFactory.getLog(SweContext.class);
	private List<ItemMenuVO> listItemMenuApp = null;
	//Mapa con todas la acciones del sistema clave=accion:metodo, valor=id:Descripcion.	
	private Map<String, String> mapAccionesModulo = new HashMap<String, String>();	
	private List<String> listIpInterna = new ArrayList<String>();
	
	public SweContext() {
	}

	public String getIdByAccionMetodo(String accion, String metodo) {
		String idDesc = (String) mapAccionesModulo.get(accion.toLowerCase() + ":" + metodo.toLowerCase());
		if (idDesc == null) {
			return null;
		}
		String tmp[] = idDesc.split(":");
		return tmp[0];
	}

	public List getListItemMenuApp() {
		return listItemMenuApp;
	}

	public void setListItemMenuApp(List<ItemMenuVO> listItemMenuApp) {
		this.listItemMenuApp = listItemMenuApp;
	}
	
	/**
	 * Devuelve el arbol de menu para los cuales el usuario tiene permisos
	 * @param sweUserSession
	 * @return
	 */
	public List<ItemMenuVO> getTreeMenu(SweUserSession sweUserSession) throws DemodaServiceException{
		List<ItemMenuVO> menu = new ArrayList<ItemMenuVO>();

		for (ItemMenuVO item: listItemMenuApp ){
			if (esValido(sweUserSession.getIdsAccionesModuloUsuario(), item)){
				ItemMenuVO nuevo = item.duplicate();
				nuevo.setListItemMenuHijos(getChildMenu(sweUserSession, nuevo));
				if (nuevo.getTieneAccModApl() || nuevo.getTieneHijos()) {
					menu.add(nuevo);
				}
			}
		}
		
		return menu;
	}
	
	
	/**
	 * Dado un item de menu, los hijos para los cuales el usuario tiene acceso.
	 * @param sweUserSession
	 * @param itemMenu
	 * @return
	 */
	protected List<ItemMenuVO> getChildMenu(SweUserSession sweUserSession, ItemMenuVO itemMenuPadre) throws DemodaServiceException {
		List<ItemMenuVO> childMenu = new ArrayList<ItemMenuVO>();
		
		for (ItemMenuVO item: itemMenuPadre.getListItemMenuHijos()){
			if (esValido(sweUserSession.getIdsAccionesModuloUsuario(), item)) {
				ItemMenuVO nuevo = item.duplicate();
				nuevo.setItemMenuPadre(itemMenuPadre);
				nuevo.setListItemMenuHijos(getChildMenu(sweUserSession, nuevo));
				if (nuevo.getTieneAccModApl() || nuevo.getTieneHijos()) {
					childMenu.add(nuevo);
				}
			}
		}
		
		return childMenu;
	}


	/**
	 * @param idsPermitidos
	 * @param item
	 * @return
	 */	
	private boolean esValido(String idsPermitidos, ItemMenuVO item) {		
		if (!item.getTieneAccModApl()) {
			return true; //cualquier nodo es valido
		}
		
		String testId = "," + item.getAccModApl().getId() + ",";
		if (idsPermitidos.indexOf(testId) >= 0) {
			return true;
		}
		
		return false;
	}

	/**
	 * Verifica si tiene acceso para nombreAccion+nombreModulo
	 * <p>ver: hasAccess(String idsAccionesModuloUsuario, String nombreAccion, String nombreMetodo)
	 */
	public long hasAccess(SweUserSession sweUserSession, String nombreAccion, String nombreMetodo) throws Exception {
		return hasAccess(sweUserSession.getIdsAccionesModuloUsuario(), nombreAccion, nombreMetodo);
	}
	
	/**
	 * Si (nombreAccion+nombreMetodo) NO se encuentran en la lista de las levantadas de accionesModulos  (las existentes)	
	 *    devuelve HASACCESS_OK_NOEXISTE
	 *
 	 * Si (nombreAccion+nombreMetodo) SI se encuentra en la lista de las levantadas de accionesModulos  (las existentes)
 	 *  Y Si (nombreAccion+nombreMetodo) SI se encuentra en la lista de accionesModulos (las disponibles para el usuario)
	 *    devuelve HASACCESS_OK
	 *    
 	 * Si (nombreAccion+nombreMetodo) SI se encuentra en la lista de las levantadas de accionesModulos  (las existentes)
 	 *  Y Si (nombreAccion+nombreMetodo) NO se encuentra en la lista de accionesModulos (las disponibles para el usuario)
	 *    devuelve HASACCESS_SINACCESO
	 *     
	 * Verifica si tiene acceso para nombreAccion+nombreModulo
	 */	
	public long hasAccess(String idsAccionesModuloUsuario, String nombreAccion, String nombreMetodo) throws Exception {
		
		if (nombreMetodo.equals("limpiar") ||
			nombreMetodo.equals("volver") ||
			nombreMetodo.equals("seleccionar")) 
		return HASACCESS_OK;
		
		if (nombreAccion == null || nombreMetodo == null) {
			return HASACCESS_NOMBRESNULOS_ERROR;
		}
		
		String idAccModApl = this.getIdByAccionMetodo(nombreAccion, nombreMetodo);
		if (idAccModApl == null){
			return HASACCESS_SINACCESO; 
		}

		if (idsAccionesModuloUsuario.indexOf("," + idAccModApl + ",") > -1) {
			return HASACCESS_OK;
		} else {
			return HASACCESS_SINACCESO;
		}
	}

	public void putAccionMetodo(String accion, String metodo, String id, String desc) {
		//if (log.isDebugEnabled()) log.debug("putIdByAccionMetodo(): agregando accion metodo: " + accion.toLowerCase() + ":" + metodo.toLowerCase() + " = "+ id + ":" + desc);
		mapAccionesModulo.put(accion.toLowerCase() + ":" + metodo.toLowerCase(), id + ":" + desc);
	}	
	
	public String getDescByAccionMetodo(String accion, String metodo) {
		String accMetodo = accion.toLowerCase() + ":" + metodo.toLowerCase();
		String desc = (String) mapAccionesModulo.get(accMetodo);
		if (desc == null)
			return null;
		String tmp[] = desc.split(":");
		return tmp.length < 2 ? null : tmp[1];
	}
	
	/**
	 * Retorna verdadero si la ip del paremtro es considerada interna
	 * segun la lista de ip internas cargadas en la aplicacion.
	 * @param ip
	 * @return
	 */
	public boolean esIpInterna(String ip) {
		
		return true;
	}

	public List<String> getListIpInterna() {
		return listIpInterna;
	}

	public void setListIpInterna(List<String> listIpInterna) {
		this.listIpInterna = listIpInterna;
	}
}
