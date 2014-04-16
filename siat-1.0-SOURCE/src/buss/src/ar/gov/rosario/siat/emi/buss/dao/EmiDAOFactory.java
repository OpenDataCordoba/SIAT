//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.buss.dao;

/**
 * Factory de Emision DAOs
 * 
 * @author tecso
 * 
 */
public class EmiDAOFactory {

    private static final EmiDAOFactory INSTANCE = new EmiDAOFactory();
    
    private ResLiqDeuDAO	 resLiqDeuDAO;
    
    private ProPasDebDAO	 proPasDebDAO;
    
    private EmisionDAO       emisionDAO;
    
    private AuxDeudaDAO      auxDeudaDAO;
    
    private ValEmiMatDAO     valEmiMatDAO;
    
    private EmiValEmiMatDAO  emiValEmiMatDAO;
    
    private EmiInfCueDAO     emiInfCueDAO;
    
    private ImpMasDeuDAO     impMasDeuDAO;
  
    private EmiDAOFactory() {
        super();
        this.resLiqDeuDAO	  = new ResLiqDeuDAO();
        this.proPasDebDAO	  = new ProPasDebDAO();
        this.emisionDAO   	  = new EmisionDAO();
        this.auxDeudaDAO  	  = new AuxDeudaDAO();
        this.valEmiMatDAO 	  = new ValEmiMatDAO();
        this.emiValEmiMatDAO  = new EmiValEmiMatDAO();
        this.emiInfCueDAO 	  = new EmiInfCueDAO();
        this.impMasDeuDAO 	  = new ImpMasDeuDAO();
    }

    public static ResLiqDeuDAO getResLiqDeuDAO() {
        return INSTANCE.resLiqDeuDAO;
    }
    
    public static ProPasDebDAO getProPasDebDAO() {
        return INSTANCE.proPasDebDAO;
    }

    public static EmisionDAO getEmisionDAO() {
        return INSTANCE.emisionDAO;
    }
    
    public static AuxDeudaDAO getAuxDeudaDAO() {
        return INSTANCE.auxDeudaDAO;
    }    
    
    public static ValEmiMatDAO getValEmiMatDAO() {
        return INSTANCE.valEmiMatDAO;
    }    

    public static EmiValEmiMatDAO getEmiValEmiMatDAO() {
        return INSTANCE.emiValEmiMatDAO;
    } 
    
    public static EmiInfCueDAO getEmiInfCueDAO() {
        return INSTANCE.emiInfCueDAO;
    }

    public static ImpMasDeuDAO getImpMasDeuDAO() {
        return INSTANCE.impMasDeuDAO;
    }
}
