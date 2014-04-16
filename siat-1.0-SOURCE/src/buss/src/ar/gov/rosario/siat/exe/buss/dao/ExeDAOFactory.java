//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.buss.dao;

/**
 * Factory de Exencion DAOs
 * 
 * @author tecso
 * 
 */
public class ExeDAOFactory {

    private static final ExeDAOFactory INSTANCE = new ExeDAOFactory();
    
    private ExencionDAO				  exencionDAO;
    private ExeRecConDAO			  exeRecConDAO;
    private ContribExeDAO			  contribExeDAO;    
    private CueExeDAO				  cueExeDAO;
    private EstadoCueExeDAO			  estadoCueExeDAO;	
    private TipoSujetoDAO             tipoSujetoDAO;
    private TipSujExeDAO              tipSujExeDAO;
    private HisEstCueExeDAO			  hisEstCueExeDAO;    
    private ConvivienteDAO			  convivienteDAO;	
    
    
    private ExeDAOFactory() {
        super();  
        this.exencionDAO				= new ExencionDAO();
        this.exeRecConDAO				= new ExeRecConDAO();
        this.contribExeDAO              = new ContribExeDAO();
        this.cueExeDAO					= new CueExeDAO();
        this.estadoCueExeDAO			= new EstadoCueExeDAO();
        this.tipoSujetoDAO				= new TipoSujetoDAO();
        this.tipSujExeDAO				= new TipSujExeDAO();
        this.hisEstCueExeDAO			= new HisEstCueExeDAO();
        this.convivienteDAO				= new ConvivienteDAO();
    }


    public static ExencionDAO getExencionDAO(){
    	return INSTANCE.exencionDAO;
    }
    
    public static ExeRecConDAO getExeRecConDAO(){
    	return INSTANCE.exeRecConDAO;
    }
        
    public static ContribExeDAO getContribExeDAO(){
    	return INSTANCE.contribExeDAO;
    }
    
    public static CueExeDAO getCueExeDAO(){
    	return INSTANCE.cueExeDAO;
    }
    
    public static EstadoCueExeDAO getEstadoCueExeDAO(){
    	return INSTANCE.estadoCueExeDAO;
    }
    
    public static TipoSujetoDAO getTipoSujetoDAO(){
    	return INSTANCE.tipoSujetoDAO;
    }
    
    public static TipSujExeDAO getTipSujExeDAO(){
    	return INSTANCE.tipSujExeDAO;
    }

    public static HisEstCueExeDAO getHisEstCueExeDAO(){
    	return INSTANCE.hisEstCueExeDAO;
    }
    
    public static ConvivienteDAO getConvivienteDAO(){
    	return INSTANCE.convivienteDAO;
    }
    
}	

