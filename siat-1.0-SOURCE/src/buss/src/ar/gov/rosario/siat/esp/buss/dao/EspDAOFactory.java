//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.buss.dao;



/**
 * Factory de "Derechos de Espectaculos Publicos" DAOs
 * 
 * @author tecso
 * 
 */
public class EspDAOFactory {

    private static final EspDAOFactory INSTANCE = new EspDAOFactory();
    
    private HabilitacionDAO					 habilitacionDAO;
    private PrecioEventoDAO					 precioEventoDAO;
    private TipoEntradaDAO					 tipoEntradaDAO;
    private EntHabDAO						 entHabDAO;
    private HisEstHabDAO					 hisEstHabDAO;
    private EstHabDAO						 estHabDAO;
    private TipoCobroDAO					 tipoCobroDAO;
    private TipoHabDAO						 tipoHabDAO;
    private ValoresCargadosDAO				 valoresCargadosDAO;
    private EntVenDAO						 entVenDAO;
    private TipoEventoDAO					 tipoEventoDAO;
    private HabExeDAO						 habExeDAO;
    private LugarEventoDAO					 lugarEventoDAO;
    
	private EspDAOFactory() {
        super();  
        this.habilitacionDAO				 = new HabilitacionDAO();
        this.precioEventoDAO				 = new PrecioEventoDAO();
        this.tipoEntradaDAO					 = new TipoEntradaDAO();
        this.entHabDAO						 = new EntHabDAO();
        this.hisEstHabDAO					 = new HisEstHabDAO();
        this.estHabDAO						 = new EstHabDAO();
        this.tipoCobroDAO				     = new TipoCobroDAO();
        this.tipoHabDAO						 = new TipoHabDAO();
        this.valoresCargadosDAO				 = new ValoresCargadosDAO();
        this.entVenDAO						 = new EntVenDAO();
        this.tipoEventoDAO					 = new TipoEventoDAO();
        this.habExeDAO						 = new HabExeDAO();
        this.lugarEventoDAO					 = new LugarEventoDAO();
    }

    public static HabilitacionDAO getHabilitacionDAO() {
        return INSTANCE.habilitacionDAO;
    }
    public static PrecioEventoDAO getPrecioEventoDAO(){
    	return INSTANCE.precioEventoDAO;
    }
    public static TipoEntradaDAO getTipoEntradaDAO(){
    	return INSTANCE.tipoEntradaDAO;
    }
    public static EntHabDAO getEntHabDAO(){
    	return INSTANCE.entHabDAO;
    }
    public static HisEstHabDAO getHisEstHabDAO(){
    	return INSTANCE.hisEstHabDAO;
    }
    public static EstHabDAO getEstHabDAO(){
    	return INSTANCE.estHabDAO;
    }
    public static TipoCobroDAO getTipoCobroDAO(){
    	return INSTANCE.tipoCobroDAO;
    }
    public static TipoHabDAO getTipoHabDAO(){
    	return INSTANCE.tipoHabDAO;
    }
    public static ValoresCargadosDAO getValoresCargadosDAO(){
    	return INSTANCE.valoresCargadosDAO;
    }
    public static EntVenDAO getEntVenDAO(){
    	return INSTANCE.entVenDAO;
    }
    public static TipoEventoDAO getTipoEventoDAO(){
    	return INSTANCE.tipoEventoDAO;
    }
    public static HabExeDAO getHabExeDAO(){
    	return INSTANCE.habExeDAO;
    }
    public static LugarEventoDAO getLugarEventoDAO(){
    	return INSTANCE.lugarEventoDAO;
    }
}
