//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

/** 
 * Clase proxy a las propiedades de session de la applicacion.
 * Como el esquema de request/response de las applicaciones almacena
 * temporalmente los datos de en la session. Utilizamos esta clase para
 * colocar un solo item en el mapa de Atributos del Session. 
 * De esta manera agrupamos los objetos de retorno y mantenimiento de session
 * en una sola clase que facilita su administracion y evita la posible policion
 * de datos en la session del usuario.
 *
 * CHANGELOG
 * UseserSession se pasa al framework cracommon porque sera utilizada para identificar
 * y almacenar los datos del usuario, al llamar a servicios del framework (Menu, Logging, etc)
 * @author  tecso
 * @date    2004-03-04
 * @version 1.1
*/
package coop.tecso.demoda.iface.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DemodaUserSession extends UserContext {
	
	private static final long serialVersionUID = 6753269251579315198L;
	private HashMap userMap = null;
    private String userMenuOptionsStr = "";
    private NavModel navModel = new NavModel();
	private List<NavItem> listStack = new ArrayList<NavItem>(); 

    public DemodaUserSession() {
    	userMap = new HashMap();
    }   
    
    public void put(String name, Object obj) {
    	userMap.put(name, obj);
    }
       
    public Object get(String name) {
    	return userMap.get(name);
    }

    public HashMap getUserMap() {
    	return userMap;
    }

    public Object remove(String name) {
    	return userMap.remove(name);
    }

    public void clear() {
    	userMap.clear();
    }
    

	public List<NavItem> getListStack() {
		return listStack;
	}

	public void setListStack(List<NavItem> listStack) {
		this.listStack = listStack;
	}

	/**
     * Devuelve la lista de menu habilitadas para el usuario
     * @return
     */
    public Set getMenu() {
    	return new HashSet();    	
    }

	public String getUserMenuOptionsStr() {
		return userMenuOptionsStr;
	}
	
	public void setUserMenuOptionsStr(String userMenuOptionsStr) {
		this.userMenuOptionsStr = userMenuOptionsStr;
	}

	public NavModel getNavModel() {
		return navModel;
	}
	public void setNavModel(NavModel navModel) {
		this.navModel = navModel;
	}


	/* Mete un item de navegacion en la pila */
	public NavItem pushNavItem(String action, String method, Long selectedId) {
		System.out.println("pushNavItem: + " + this.listStack);
		NavItem ni = new NavItem(action, method, selectedId);
		this.listStack.add(0, ni);
		return ni;
	}

	/* Lee el ultimo item de navegacion sin retirarlo de la pila */
	public NavItem peekNavItem() {
		return this.listStack.get(0);
	}

	/* Saca el ultimo item de navegacion de la pila */
	public NavItem popNavItem() {
		return this.listStack.remove(0);
	}

}
