//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.buss.dao;

/**
 * Factory de <Nombre Largo Modulo> DAOs
 * 
 * @author tecso
 * 
 */
public class CyqDAOFactory {

    private static final CyqDAOFactory INSTANCE = new CyqDAOFactory();
    
    private ProcedimientoDAO             procedimientoDAO;
    private EstadoProcedDAO			 	 estadoProcedDAO;
    private AbogadoDAO					 abogadoDAO;
    private JuzgadoDAO					 juzgadoDAO;
    private TipoProcesoDAO				 tipoProcesoDAO;
    private HisEstProcedDAO			  	 hisEstProcedDAO;
    private MotivoBajaDAO				 motivoBajaDAO;
    private MotivoResInfDAO				 motivoResInfDAO;
    private ProCueNoDeuDAO				 proCueNoDeuDAO;
    private ProDetDAO					 proDetDAO;
    private DeudaPrivilegioDAO			 deudaPrivilegioDAO;
    private PagoPrivDAO					 pagoPrivDAO;
    private PagoPrivDeuDAO				 pagoPrivDeuDAO;
    private TipoPrivilegioDAO			 tipoPrivilegioDAO;
    
    private CyqDAOFactory() {
        super();  
        this.procedimientoDAO                = new ProcedimientoDAO();
        this.estadoProcedDAO			 	 = new EstadoProcedDAO();
        this.abogadoDAO						 = new AbogadoDAO();
        this.juzgadoDAO						 = new JuzgadoDAO();
        this.tipoProcesoDAO					 = new TipoProcesoDAO();
        this.hisEstProcedDAO				 = new HisEstProcedDAO();
        this.motivoBajaDAO				 	 = new MotivoBajaDAO();
        this.motivoResInfDAO				 = new MotivoResInfDAO();
        this.proCueNoDeuDAO					 = new ProCueNoDeuDAO();
        this.proDetDAO						 = new ProDetDAO();	
        this.deudaPrivilegioDAO				 = new DeudaPrivilegioDAO();
        this.pagoPrivDAO					 = new PagoPrivDAO();
        this.pagoPrivDeuDAO					 = new PagoPrivDeuDAO();
        this.tipoPrivilegioDAO				 = new TipoPrivilegioDAO();
    }

    public static ProcedimientoDAO getProcedimientoDAO() {
        return INSTANCE.procedimientoDAO;
    } 
    public static EstadoProcedDAO getEstadoProcedDAO() {
        return INSTANCE.estadoProcedDAO;
    } 
    public static AbogadoDAO getAbogadoDAO() {
        return INSTANCE.abogadoDAO;
    }
    public static JuzgadoDAO getJuzgadoDAO() {
        return INSTANCE.juzgadoDAO;
    }
    public static TipoProcesoDAO getTipoProcesoDAO() {
        return INSTANCE.tipoProcesoDAO;
    }
    public static HisEstProcedDAO getHisEstProcedDAO() {
        return INSTANCE.hisEstProcedDAO;
    }
    
    public static MotivoBajaDAO getMotivoBajaDAO() {
        return INSTANCE.motivoBajaDAO;
    }
    
    public static MotivoResInfDAO getMotivoResInfDAO() {
        return INSTANCE.motivoResInfDAO;
    }
    
    public static ProCueNoDeuDAO getProCueNoDeuDAO(){
    	return INSTANCE.proCueNoDeuDAO;
    }
        
    public static ProDetDAO getProDetDAO(){
    	return INSTANCE.proDetDAO;
    }
    
    public static DeudaPrivilegioDAO getDeudaPrivilegioDAO(){
    	return INSTANCE.deudaPrivilegioDAO;
    }
    
    public static PagoPrivDAO getPagoPrivDAO(){
    	return INSTANCE.pagoPrivDAO;
    }
    
    public static PagoPrivDeuDAO getPagoPrivDeuDAO(){
    	return INSTANCE.pagoPrivDeuDAO;
    }
    
    public static TipoPrivilegioDAO getTipoPrivilegioDAO(){
    	return INSTANCE.tipoPrivilegioDAO;
    }
}
