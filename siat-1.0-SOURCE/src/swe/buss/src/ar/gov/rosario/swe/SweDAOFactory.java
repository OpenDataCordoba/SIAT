//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe;

import ar.gov.rosario.swe.buss.dao.AccModAplDAO;
import ar.gov.rosario.swe.buss.dao.AplicacionDAO;
import ar.gov.rosario.swe.buss.dao.ItemMenuDAO;
import ar.gov.rosario.swe.buss.dao.ModAplDAO;
import ar.gov.rosario.swe.buss.dao.RolAccModAplDAO;
import ar.gov.rosario.swe.buss.dao.RolAplDAO;
import ar.gov.rosario.swe.buss.dao.TipoAuthDAO;
import ar.gov.rosario.swe.buss.dao.UsrAplAdmiSweDAO;
import ar.gov.rosario.swe.buss.dao.UsrAplDAO;
import ar.gov.rosario.swe.buss.dao.UsrAuthDAO;
import ar.gov.rosario.swe.buss.dao.UsrRolAplDAO;

/**
 * Factory de Seguridad DAOs
 * 
 * @author tecso
 * 
 */
public class SweDAOFactory {

    private static final SweDAOFactory INSTANCE = new SweDAOFactory();
    
    private AplicacionDAO           aplicacionDAO;
    private RolAplDAO               rolAplDAO;
    private UsrAplDAO			    usrAplDAO; 
    private UsrRolAplDAO            usrRolAplDAO;
    private ItemMenuDAO             itemMenuDAO;
    private ModAplDAO				modAplDAO;
    private AccModAplDAO			accModAplDAO; 
    private RolAccModAplDAO			rolAccModAplDAO;
    private UsrAplAdmiSweDAO		usrAplAdmiSweDAO;
    private UsrAuthDAO				usrAuthDAO;
    private TipoAuthDAO				tipoAuthDAO;
    
    

	private SweDAOFactory() {
        super();
        
        this.aplicacionDAO				= new AplicacionDAO(); 
        this.rolAplDAO                  = new RolAplDAO();
        this.usrAplDAO				    = new UsrAplDAO();
        this.usrRolAplDAO				= new UsrRolAplDAO();
        this.itemMenuDAO                = new ItemMenuDAO();
        this.modAplDAO					= new ModAplDAO();
        this.accModAplDAO               = new AccModAplDAO();
        this.rolAccModAplDAO			= new RolAccModAplDAO();
        this.usrAplAdmiSweDAO			= new UsrAplAdmiSweDAO();
        this.usrAuthDAO					= new UsrAuthDAO();
        this.tipoAuthDAO				= new TipoAuthDAO();
       
    }
    
    public static AplicacionDAO getAplicacionDAO(){
    	return INSTANCE.aplicacionDAO;
    }
   
    public static RolAplDAO getRolAplDAO() {
        return INSTANCE.rolAplDAO;
    }
     
    public static UsrAplDAO getUsrAplDAO() {
        return INSTANCE.usrAplDAO;
    }

    public static UsrRolAplDAO getUsrRolAplDAO() {
        return INSTANCE.usrRolAplDAO;
    }
    
    public static ItemMenuDAO getItemMenuDAO() {
        return INSTANCE.itemMenuDAO;
    }

	public static ModAplDAO getModAplDAO() {
		return INSTANCE.modAplDAO;
	}

	public static AccModAplDAO getAccModAplDAO() {
		return INSTANCE.accModAplDAO;
	}
    
	public static RolAccModAplDAO getRolAccModAplDAO() {
		return INSTANCE.rolAccModAplDAO;
	}

	public static UsrAplAdmiSweDAO getUsrAplAdmiSweDAO() {
		return INSTANCE.usrAplAdmiSweDAO;
	}

	public static UsrAuthDAO getUsrAuthDAO() {
		return INSTANCE.usrAuthDAO;
	}
	
	public static TipoAuthDAO getTipoAuthDAO() {
		return INSTANCE.tipoAuthDAO;
	}
	
}
