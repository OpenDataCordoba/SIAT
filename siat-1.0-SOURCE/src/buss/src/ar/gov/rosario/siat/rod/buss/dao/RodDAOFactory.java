//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.buss.dao;

/**
 * Factory de Rodado DAOs
 * 
 * @author tecso
 * 
 */
public class RodDAOFactory {

    private static final RodDAOFactory INSTANCE = new RodDAOFactory();
    
    private TramiteRADAO	tramiteRADAO;
    private EstadoTramiteRADAO	estTramiteRADAO;
    private HisTraDAO	hisEstTraRADAO;
    private PropietarioDAO	propietarioDAO;
    
    private RodDAOFactory() {
        super();  
        this.tramiteRADAO = new TramiteRADAO();
        this.estTramiteRADAO = new EstadoTramiteRADAO();
        this.hisEstTraRADAO = new HisTraDAO();
        this.propietarioDAO = new PropietarioDAO();
    }

    public static TramiteRADAO getTramiteRADAO() {
        return INSTANCE.tramiteRADAO;
    }
    
    public static EstadoTramiteRADAO getEstTramiteRADAO() {
        return INSTANCE.estTramiteRADAO;
    }
    
    public static HisTraDAO getHisEstTraDAO() {
        return INSTANCE.hisEstTraRADAO;
    }
    
    public static PropietarioDAO getPropietarioDAO() {
        return INSTANCE.propietarioDAO;
    }
}

