//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pro.buss.dao;

/**
 * Factory de Proceso (proceso) DAOs
 * 
 * @author tecso
 * 
 */
public class ProDAOFactory {

    private static final ProDAOFactory INSTANCE = new ProDAOFactory();
    
    private TipoEjecucionDAO tipoEjecucionDAO;
    private TipoProgEjecDAO  tipoProgEjecDAO;
    private ProcesoDAO       procesoDAO;
    private EstadoCorridaDAO estadoCorridaDAO;
    private CorridaDAO       corridaDAO;
    private LogCorridaDAO	 logCorridaDAO;
    private PasoCorridaDAO   pasoCorridaDAO;
    private FileCorridaDAO	 fileCorridaDAO;
    
    private ProDAOFactory() {
        super();  
        this.tipoEjecucionDAO = new TipoEjecucionDAO();
        this.tipoProgEjecDAO  = new TipoProgEjecDAO();
        this.procesoDAO       = new ProcesoDAO();
        this.estadoCorridaDAO = new EstadoCorridaDAO();
        this.corridaDAO 	  = new CorridaDAO();
        this.logCorridaDAO	  = new LogCorridaDAO();
        this.pasoCorridaDAO   = new PasoCorridaDAO();
        this.fileCorridaDAO	  = new FileCorridaDAO();
    }

    public static TipoEjecucionDAO getTipoEjecucionDAO() {
        return INSTANCE.tipoEjecucionDAO;
    }    
    public static TipoProgEjecDAO getTipoProgEjecDAO(){
    	return INSTANCE.tipoProgEjecDAO;
    }
    public static ProcesoDAO getProcesoDAO(){
    	return INSTANCE.procesoDAO;
    }
    public static EstadoCorridaDAO getEstadoCorridaDAO(){
    	return INSTANCE.estadoCorridaDAO;
    }
    public static CorridaDAO getCorridaDAO(){
    	return INSTANCE.corridaDAO;
    }
    public static LogCorridaDAO getLogCorridaDAO(){
    	return INSTANCE.logCorridaDAO;
    }

	public static PasoCorridaDAO getPasoCorridaDAO(){
    	return INSTANCE.pasoCorridaDAO;
    }
	public static FileCorridaDAO getFileCorridaDAO(){
	   	return INSTANCE.fileCorridaDAO;
	}

}
