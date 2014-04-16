//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.buss.dao;



/**
 * Factory de AFIP DAOs
 * 
 * @author tecso
 * 
 */
public class AfiDAOFactory {

    private static final AfiDAOFactory INSTANCE = new AfiDAOFactory();
    

    private ForDecJurDAO				forDecJurDAO;
    private SocioDAO             		socioDAO;
    private RetYPerDAO		 			retYPerDAO;
    private DatosPagoCtaDAO				datosPagoCtaDAO;
    private TotDerYAccDJDAO	 			totDerYAccDJDAO;
    private LocalDAO			 		localDAO;
    private DatosDomicilioDAO	 		datosDomicilioDAO;
    private OtrosPagosDAO		 		otrosPagosDAO;
    private DecActLocDAO				decActLocDAO;
    private ExeActLocDAO 				exeActLocDAO;
    private ActLocDAO					actLocDAO;
    private HabLocDAO					habLocDAO;
    private EstForDecJurDAO				estForDecJurDAO;

    
    private AfiDAOFactory() {
        super();  

        this.forDecJurDAO				 = new ForDecJurDAO();
        this.socioDAO                  	 = new SocioDAO();
        this.retYPerDAO				 	 = new RetYPerDAO();
        this.datosPagoCtaDAO			 = new DatosPagoCtaDAO();
        this.totDerYAccDJDAO			 = new TotDerYAccDJDAO();
        this.localDAO					 = new LocalDAO();
        this.datosDomicilioDAO			 = new DatosDomicilioDAO();
        this.otrosPagosDAO				 = new OtrosPagosDAO();
        this.decActLocDAO				 = new DecActLocDAO();    
        this.exeActLocDAO 	 			 = new ExeActLocDAO();
        this.actLocDAO			 		 = new ActLocDAO();
        this.habLocDAO		 			 = new HabLocDAO();     
        this.estForDecJurDAO			 = new EstForDecJurDAO();
    }


    public static SocioDAO getSocioDAO() {
        return INSTANCE.socioDAO;
    }

    public static ForDecJurDAO getForDecJurDAO() {
		return INSTANCE.forDecJurDAO;
	}

	public static RetYPerDAO getRetYPerDAO() {
		return INSTANCE.retYPerDAO;
	}
    
	public static DatosPagoCtaDAO getDatosPagoCtaDAO() {
		return INSTANCE.datosPagoCtaDAO;
	}
	
	public static TotDerYAccDJDAO getTotDerYAccDJDAO() {
		return INSTANCE.totDerYAccDJDAO;
	}

	public static LocalDAO getLocalDAO() {
		return INSTANCE.localDAO;
	}
	
	public static DatosDomicilioDAO getDatosDomicilioDAO() {
		return INSTANCE.datosDomicilioDAO;
	}

	public static OtrosPagosDAO getOtrosPagosDAO() {
		return INSTANCE.otrosPagosDAO;
	}

	public static DecActLocDAO getDecActLocDAO() {
		return INSTANCE.decActLocDAO;
	}
	
	public static ExeActLocDAO getExeActLocDAO() {
		return INSTANCE.exeActLocDAO;
	}
	
	public static ActLocDAO getActLocDAO() {
		return INSTANCE.actLocDAO;
	}
	
	public static HabLocDAO getHabLocDAO() {
		return INSTANCE.habLocDAO;
	}
		
	public static EstForDecJurDAO getEstForDecJurDAO() {
		return INSTANCE.estForDecJurDAO;
	}
}
