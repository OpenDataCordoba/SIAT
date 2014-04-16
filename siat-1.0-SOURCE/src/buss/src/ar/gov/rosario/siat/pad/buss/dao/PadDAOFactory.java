//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.dao;

/**
 * Factory de Padron DAOs
 * 
 * @author tecso
 * 
 */
public class PadDAOFactory {

    private static final PadDAOFactory INSTANCE = new PadDAOFactory();
    
    private CuentaDAO             cuentaDAO;
    private EstCueDAO		  	  estCueDAO;
    private TipoDomicilioDAO      tipoDomicilioDAO;
    private DomicilioDAO          domicilioDAO;
    private ConAtrValDAO          conAtrValDAO;
    private ConAtrValSecDAO       conAtrValSecDAO;
    private ContribuyenteDAO      contribuyenteDAO;        
    private ObjImpDAO			  objImpDAO;
    private ObjImpAtrValDAO		  objImpAtrValDAO;
    private TipoTitularDAO		  tipoTitularDAO;
    private CuentaTitularDAO	  cuentaTitularDAO;
    private CuentaRelDAO	 	  cuentaRelDAO;
    private CamDomWebDAO          camDomWebDAO;
    private RepartidorDAO		  repartidorDAO;
    private TipoRepartidorDAO	  tipoRepartidorDAO;
    private CriRepDAO			  criRepDAO;
    private CriRepCatDAO		  criRepCatDAO;
    private CriRepCalleDAO		  criRepCalleDAO;
    private BrocheDAO			  brocheDAO;
    private TipoBrocheDAO		  tipoBrocheDAO;
    private BroCueDAO			  broCueDAO;
    private UbicacionJDBCDAO	  ubicacionJDBCDAO;    
    private RecAtrCueVDAO		  recAtrCueVDAO;	
    private CueExcSelDAO		  cueExcSelDAO;
    private CueExcSelDeuDAO		  cueExcSelDeuDAO;
    private DebitoAutJDBCDAO      debitoAutJDBCDAO;
    private PersonaDAO            personaDAO;
    private TipoDocumentoDAO      tipoDocumentoDAO;
    
    private EstObjImpDAO		  estObjImpDAO;
    private AltaOficioDAO		  altaOficioDAO;
    
    
    private VariosWebJDBCDAO 	  variosWebJDBCDAO;
    private CatastroJDBCDAO 	  catastroJDBCDAO;
    
    private PadDAOFactory() {
        super();  
        this.cuentaDAO                = new CuentaDAO();
        this.estCueDAO		 		  = new EstCueDAO();
        this.tipoDomicilioDAO         = new TipoDomicilioDAO();
        this.domicilioDAO             = new DomicilioDAO();
        this.conAtrValDAO             = new ConAtrValDAO();
        this.conAtrValSecDAO          = new ConAtrValSecDAO();
        this.contribuyenteDAO         = new ContribuyenteDAO();
        this.objImpDAO				  = new ObjImpDAO();
        this.objImpAtrValDAO		  = new ObjImpAtrValDAO();
        this.tipoTitularDAO		      = new TipoTitularDAO();
        this.cuentaTitularDAO		  = new CuentaTitularDAO();
        this.cuentaRelDAO			  = new CuentaRelDAO();
        this.camDomWebDAO             = new CamDomWebDAO();
        this.repartidorDAO			  = new RepartidorDAO();
        this.tipoRepartidorDAO		  = new TipoRepartidorDAO();
        this.criRepDAO				  = new CriRepDAO();
        this.criRepCatDAO			  = new CriRepCatDAO();
        this.criRepCalleDAO			  = new CriRepCalleDAO();
        this.brocheDAO				  = new BrocheDAO();
        this.tipoBrocheDAO			  = new TipoBrocheDAO();
        this.broCueDAO				  = new BroCueDAO();
        this.ubicacionJDBCDAO         = new UbicacionJDBCDAO();
        this.recAtrCueVDAO			  = new RecAtrCueVDAO();
        this.cueExcSelDAO             = new CueExcSelDAO();
        this.cueExcSelDeuDAO          = new CueExcSelDeuDAO();
        this.debitoAutJDBCDAO         = new DebitoAutJDBCDAO();
        
        this.estObjImpDAO			  = new EstObjImpDAO();
        this.altaOficioDAO			  = new AltaOficioDAO();
        this.variosWebJDBCDAO		  = new VariosWebJDBCDAO();
        this.catastroJDBCDAO		  = new CatastroJDBCDAO();
        this.personaDAO			      = new PersonaDAO();
        this.tipoDocumentoDAO		  = new TipoDocumentoDAO();
    }

    public static CuentaDAO getCuentaDAO() {
        return INSTANCE.cuentaDAO;
    }
    
    public static EstCueDAO getEstCueDAO() {
        return INSTANCE.estCueDAO;
    }
    
    public static TipoDomicilioDAO getTipoDomicilioDAO() {
        return INSTANCE.tipoDomicilioDAO;
    }
    
    public static DomicilioDAO getDomicilioDAO() {
        return INSTANCE.domicilioDAO;
    }
    
    public static ConAtrValDAO getConAtrValDAO() {
        return INSTANCE.conAtrValDAO;
    }
    
    public static ConAtrValSecDAO getConAtrValSecDAO() {
        return INSTANCE.conAtrValSecDAO;
    }
    
    public static ContribuyenteDAO getContribuyenteDAO() {
        return INSTANCE.contribuyenteDAO;
    }
    
    public static ObjImpDAO getObjImpDAO() {
        return INSTANCE.objImpDAO;
    }
    
    public static ObjImpAtrValDAO getObjImpAtrValDAO() {
        return INSTANCE.objImpAtrValDAO;
    }
    
    public static TipoTitularDAO getTipoTitularDAO() {
        return INSTANCE.tipoTitularDAO;
    }
    
    public static CuentaTitularDAO getCuentaTitularDAO() {
        return INSTANCE.cuentaTitularDAO;
    }
    
    public static CuentaRelDAO getCuentaRelDAO() {
        return INSTANCE.cuentaRelDAO;
    }
    
    public static CamDomWebDAO getCamDomWebDAO() {
        return INSTANCE.camDomWebDAO;
    }    
    
    public static RepartidorDAO getRepartidorDAO(){
    	return INSTANCE.repartidorDAO;
    }
    
    public static TipoRepartidorDAO getTipoRepartidorDAO(){
    	return INSTANCE.tipoRepartidorDAO;
    }
    
    public static CriRepDAO getCriRepDAO(){
    	return INSTANCE.criRepDAO;
    }
    
    public static CriRepCatDAO getCriRepCatDAO(){
    	return INSTANCE.criRepCatDAO;
    }
    
    public static CriRepCalleDAO getCriRepCalleDAO(){
    	return INSTANCE.criRepCalleDAO;
    }
    
    public static BrocheDAO getBrocheDAO(){
    	return INSTANCE.brocheDAO;
    }
    
    public static TipoBrocheDAO getTipoBrocheDAO(){
    	return INSTANCE.tipoBrocheDAO;
    }
    
    public static BroCueDAO getBroCueDAO(){
    	return INSTANCE.broCueDAO;
    }
    
    public static UbicacionJDBCDAO getUbicacionJDBCDAO(){
    	return INSTANCE.ubicacionJDBCDAO;
    }

	
    public static RecAtrCueVDAO getRecAtrCueVDAO() {
		return INSTANCE.recAtrCueVDAO;
	}

    public static CueExcSelDAO getCueExcSelDAO() {
		return INSTANCE.cueExcSelDAO;
	}

    public static CueExcSelDeuDAO getCueExcSelDeuDAO() {
		return INSTANCE.cueExcSelDeuDAO;
	}
    
    public static DebitoAutJDBCDAO getDebitoAutJDBCDAO(){
    	return INSTANCE.debitoAutJDBCDAO;
    }
        
    public static EstObjImpDAO getEstObjImpDAO(){
    	return INSTANCE.estObjImpDAO;
    }
    
    public static AltaOficioDAO getAltaOficioDAO(){
    	return INSTANCE.altaOficioDAO;
    }
    
    public static VariosWebJDBCDAO getVariosWebJDBCDAO(){
    	return INSTANCE.variosWebJDBCDAO;
    }
    
    public static CatastroJDBCDAO getCatastroJDBCDAO(){
    	return INSTANCE.catastroJDBCDAO;
    }

    public static PersonaDAO getPersonaDAO(){
    	return INSTANCE.personaDAO;
    }

    public static TipoDocumentoDAO getTipoDocumentoDAO(){
    	return INSTANCE.tipoDocumentoDAO;
    }

}
