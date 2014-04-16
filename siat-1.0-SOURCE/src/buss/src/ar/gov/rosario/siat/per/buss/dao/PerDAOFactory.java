//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.per.buss.dao;

/**
 * Factory de Personas DAOs
 * 
 * @author tecso
 * 
 */
public class PerDAOFactory {

    private static final PerDAOFactory INSTANCE = new PerDAOFactory();
    
    private PerCalleDAO perCalleDAO;
    private PerLocalidadDAO perLocalidadDAO;
    private PerProvinciaDAO perProvinciaDAO;
    
    private PerDAOFactory() {
        super();  
        this.perCalleDAO = new PerCalleDAO();
        this.perLocalidadDAO = new PerLocalidadDAO();
        this.perProvinciaDAO = new PerProvinciaDAO();
    }

    public static PerCalleDAO getPerCalleDAO() {
        return INSTANCE.perCalleDAO;
    }
    
    public static PerLocalidadDAO getPerLocalidadDAO() {
        return INSTANCE.perLocalidadDAO;
    }
    
    public static PerProvinciaDAO getPerProvinciaDAO() {
        return INSTANCE.perProvinciaDAO;
    }        
}
