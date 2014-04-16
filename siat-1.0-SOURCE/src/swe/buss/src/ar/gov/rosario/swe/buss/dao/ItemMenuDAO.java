//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.classic.Session;

import ar.gov.rosario.swe.buss.bean.Aplicacion;
import ar.gov.rosario.swe.buss.bean.ItemMenu;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.Estado;

public class ItemMenuDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ItemMenuDAO.class);	
	
	public ItemMenuDAO() {
		super(ItemMenu.class);
	}

	/**
	 * Obtiene la lista de Items de Menu hijos activos del Item de Menu padre
	 * @param  itemMenuPadre
	 * @return List<ItemMenu>
	 * @throws Exception
	 */
    public List<ItemMenu> getListItemMenuHijosActivos(ItemMenu itemMenuPadre) throws Exception {
    	String funcName = "getListItemMenuHijosActivos(ItemMenu itemMenuPadre)";
    	if (log.isDebugEnabled()) log.debug("iniciando..." + funcName);

        Session session = SweHibernateUtil.currentSession();
        
	    String consulta = "from ItemMenu im " +
	    	"where im.itemMenuPadre = :itemMenuPadre " +
	    	"and im.estado = :estadoActivo" + 
	    	"order by im.nroOrden ";

	    List<ItemMenu> listItemMenu = (ArrayList<ItemMenu>) session.createQuery(consulta)
		   .setEntity("itemMenuPadre",itemMenuPadre)
		   .setInteger("estadoActivo",Estado.ACTIVO.getId())
		   .list();
	    
    	if (log.isDebugEnabled()) log.debug("Finalizando " + funcName);	    
	    return listItemMenu;
    }

    /**
     * Obtiene la lista de Items de Menu hijos (activos e inactivos) del Item de Menu padre
     * @param itemMenuPadre
     * @return List<ItemMenu>
     * @throws Exception
     */
    public List<ItemMenu> getListItemMenuHijos(ItemMenu itemMenuPadre) throws Exception {
    	String funcName = "getListItemMenuHijosActivos(ItemMenu itemMenuPadre)";
    	if (log.isDebugEnabled()) log.debug("iniciando..." + funcName);

        Session session = SweHibernateUtil.currentSession();
        
	    String consulta = "FROM ItemMenu im " +
	    	"WHERE im.itemMenuPadre = :itemMenuPadre " +
	    	"ORDER BY im.nroOrden ";
	    	

	    List<ItemMenu> listItemMenu = (ArrayList<ItemMenu>) session.createQuery(consulta)
		   .setEntity("itemMenuPadre",itemMenuPadre)
		   .list();
	    
    	if (log.isDebugEnabled()) log.debug("Finalizando " + funcName);	    
	    return listItemMenu;
    }

    
    /**
     * Obtiene la lista de Items de Menu de primer nivel activos e inactivos
     * @return List<ItemMenu>
     * @throws Exception
     */ 
    public List<ItemMenu> getListItemMenuPrimerNivel() throws Exception {
    	String funcName = "getListItemMenuPrimerNivel()";
    	if (log.isDebugEnabled()) log.debug("iniciando..." + funcName);

        Session session = SweHibernateUtil.currentSession();
        
	    String consulta = "FROM ItemMenu im " +
	    	"WHERE im.nivel = :primerNivel " +
	    	"ORDER BY im.nroOrden ";

	    List<ItemMenu> listItemMenu = (ArrayList<ItemMenu>) session.createQuery(consulta)
		   .setInteger("primerNivel",new Integer(1))
		   .list();
	    
    	if (log.isDebugEnabled()) log.debug("Finalizando " + funcName);	    
	    return listItemMenu;
    }

    /**
     * Obtiene la lista de Items de Menu Padres para una aplicacion
     * @param  aplicacion
     * @return List<ItemMenu>
     * @throws Exception
     */
    public List<ItemMenu> getListItemMenuPadresByAplicacion(Aplicacion aplicacion) throws Exception {
    	String funcName = DemodaUtil.currentMethodName();
	    if (log.isDebugEnabled()) log.debug(funcName + ": enter");
	    
        Session session = SweHibernateUtil.currentSession();
        
        String consulta = "";
	    consulta += "from ItemMenu im ";
	    consulta += "left join fetch im.accModApl.modApl ";
	    consulta += "where im.itemMenuPadre IS NULL " ;
	    consulta += "and im.aplicacion = :aplicacion " ; 
	    consulta += "order by im.nroOrden ";

	    List<ItemMenu> listItemMenu = (ArrayList<ItemMenu>) session.createQuery(consulta)
		   .setEntity("aplicacion", aplicacion)
		   .list();
	    
	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");	    
	    return listItemMenu;
    }

}
