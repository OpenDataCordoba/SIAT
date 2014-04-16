//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.dao;

import ar.gov.rosario.siat.rec.buss.dao.CatRSDreiDAO;



/**
 * Factory de Definicion DAOs
 * 
 * @author tecso
 * 
 */
public class DefDAOFactory {

    private static final DefDAOFactory INSTANCE = new DefDAOFactory();
    
    private AtributoDAO             atributoDAO;
    private TipoAtributoDAO		    tipoAtributoDAO;	
    private DomAtrDAO				domAtrDAO;
    private DomAtrValDAO          	domAtrValorDAO;
    private TipObjImpDAO           	tipObjImpDAO;
    private TipObjImpAtrDAO         tipObjImpAtrDAO;
    private CategoriaDAO			categoriaDAO;
    private RubroDAO                rubroDAO;
    private SubrubroDAO             subrubroDAO;    
    private TributoDAO              tributoDAO;
    private AreaDAO					areaDAO;
    private TipObjImpAreODAO        tipObjImpAreODAO;
    private TipoDAO        			tipoDAO;
    private ConAtrDAO               conAtrDAO;
    private FeriadoDAO				feriadoDAO;
    private VencimientoDAO			vencimientoDAO;
    private ServicioBancoDAO		servicioBancoDAO;
    private SerBanRecDAO			serBanRecDAO;
    private RecursoDAO 				recursoDAO;
    private RecAtrValDAO 			recAtrValDAO;
    private RecConDAO 				recConDAO;
    private RecClaDeuDAO			recClaDeuDAO;
    private RecGenCueAtrVaDAO 		recGenCueAtrVaDAO;
    private RecAtrCueDAO			recAtrCueDAO;
    private RecEmiDAO 				recEmiDAO;
    private RecAtrCueEmiDAO 		recAtrCueEmiDAO;
    private TipoEmisionDAO			tipoEmisionDAO;
    private PeriodoDeudaDAO			periodoDeudaDAO;
    private GenCueDAO				genCueDAO;
    private GenCodGesDAO			genCodGesDAO;
    private ViaDeudaDAO				viaDeudaDAO;
    private ZonaDAO					zonaDAO;
    private SeccionDAO				seccionDAO;
    private TipoDeudaDAO			tipoDeudaDAO;
    private ParametroDAO			parametroDAO;
    private DesImpDAO				desImpDAO;
    private BancoDAO				bancoDAO;
    private CalendarioDAO			calendarioDAO;
    private EmiMatDAO				emiMatDAO;
    private ColEmiMatDAO			colEmiMatDAO;
    private RecConADecDAO			recConADecDAO;
    private RecTipAliDAO			recTipAliDAO;
    private RecAliDAO				recAliDAO;	
    private RecMinDecDAO			recMinDecDAO;
    private RecTipUniDAO			recTipUniDAO;
    private ValUnRecConADeDAO		valUnRecConADeDAO;
    private TipRecConADecDAO		tipRecConADecDAO;
    private MinRecConADecDAO		minRecConADecDAO;
    private TipCodEmiDAO			tipCodEmiDAO;
    private HistCodEmiDAO			histCodEmiDAO;
    private CodEmiDAO				codEmiDAO;
    private SituacionInmuebleDAO	situacionInmuebleDAO;
    private SolicitudInmuebleDAO	solicitudInmuebleDAO;
    private CategoriaInmuebleDAO	categoriaInmuebleDAO;
    private SuperficieInmuebleDAO	superficieInmuebleDAO;
    private SiatScriptDAO			siatScriptDAO;
    private SiatScriptUsrDAO		siatScriptUsrDAO;
    private RecursoAreaDAO			recursoAreaDAO;
    private CatRSDreiDAO			categoriaRSDreiDAO;

    
    private DefDAOFactory() {
        super();  
        this.atributoDAO                = new AtributoDAO();
        this.tipoAtributoDAO			= new TipoAtributoDAO();
        this.domAtrDAO					= new DomAtrDAO();
        this.domAtrValorDAO             = new DomAtrValDAO();
        this.domAtrValorDAO             = new DomAtrValDAO();
        this.tipObjImpDAO				= new TipObjImpDAO();
        this.tipObjImpAtrDAO			= new TipObjImpAtrDAO();
        this.categoriaDAO               = new CategoriaDAO();
        this.rubroDAO					= new RubroDAO();
        this.subrubroDAO                = new SubrubroDAO();
        this.tributoDAO                 = new TributoDAO();
        this.areaDAO					= new AreaDAO();
        this.tipObjImpAreODAO			= new TipObjImpAreODAO();
        this.tipoDAO                    = new TipoDAO();
        this.conAtrDAO                  = new ConAtrDAO();
        this.feriadoDAO					= new FeriadoDAO();
        this.recursoDAO					= new RecursoDAO();
        this.vencimientoDAO				= new VencimientoDAO();
        this.servicioBancoDAO			= new ServicioBancoDAO();
        this.serBanRecDAO				= new SerBanRecDAO();
        this.recAtrValDAO 				= new RecAtrValDAO();
        this.recConDAO	 				= new RecConDAO();
        this.recClaDeuDAO 				= new RecClaDeuDAO();
        this.recGenCueAtrVaDAO 			= new RecGenCueAtrVaDAO();
        this.recAtrCueDAO				= new RecAtrCueDAO();
        this.recEmiDAO 					= new RecEmiDAO();
        this.recAtrCueEmiDAO 			= new RecAtrCueEmiDAO();
        this.tipoEmisionDAO				= new TipoEmisionDAO();
        this.periodoDeudaDAO			= new PeriodoDeudaDAO();
        this.genCueDAO					= new GenCueDAO();
        this.genCodGesDAO				= new GenCodGesDAO();
        this.viaDeudaDAO				= new ViaDeudaDAO();
        this.zonaDAO					= new ZonaDAO();
        this.seccionDAO					= new SeccionDAO();
        this.tipoDeudaDAO				= new TipoDeudaDAO();
        this.parametroDAO				= new ParametroDAO();
        this.desImpDAO					= new DesImpDAO();
        this.bancoDAO					= new BancoDAO();
        this.calendarioDAO				= new CalendarioDAO();
        this.emiMatDAO					= new EmiMatDAO();
        this.colEmiMatDAO				= new ColEmiMatDAO();
        this.recConADecDAO				= new RecConADecDAO();
        this.recTipAliDAO				= new RecTipAliDAO();
        this.recAliDAO					= new RecAliDAO();
        this.recMinDecDAO				= new RecMinDecDAO();
        this.recTipUniDAO				= new RecTipUniDAO();
        this.valUnRecConADeDAO			= new ValUnRecConADeDAO();
        this.tipRecConADecDAO           = new TipRecConADecDAO();
        this.minRecConADecDAO			= new MinRecConADecDAO();
        this.tipCodEmiDAO				= new TipCodEmiDAO();
        this.histCodEmiDAO				= new HistCodEmiDAO();
        this.codEmiDAO					= new CodEmiDAO();
        this.situacionInmuebleDAO		= new SituacionInmuebleDAO();
        this.solicitudInmuebleDAO		= new SolicitudInmuebleDAO();
        this.categoriaInmuebleDAO		= new CategoriaInmuebleDAO();
        this.superficieInmuebleDAO		= new SuperficieInmuebleDAO();
        this.siatScriptDAO				= new SiatScriptDAO();
        this.siatScriptUsrDAO			= new SiatScriptUsrDAO();
        this.recursoAreaDAO				= new RecursoAreaDAO();
        this.categoriaRSDreiDAO			= new CatRSDreiDAO();        
       
    }

    public static AtributoDAO getAtributoDAO() {
        return INSTANCE.atributoDAO;
    }
    
    public static TipoAtributoDAO getTipoAtributoDAO() {
        return INSTANCE.tipoAtributoDAO;
    }
    
    public static DomAtrDAO getDomAtrDAO() {
        return INSTANCE.domAtrDAO;
    }
    
    public static DomAtrValDAO getDomAtrValDAO() {
        return INSTANCE.domAtrValorDAO;
    }
    
    public static TipObjImpDAO getTipObjImpDAO(){
    	return INSTANCE.tipObjImpDAO;
    }
    
    public static TipObjImpAtrDAO getTipObjImpAtrDAO(){
    	return INSTANCE.tipObjImpAtrDAO;
    }
    
    public static CategoriaDAO getCategoriaDAO() {
        return INSTANCE.categoriaDAO;
    }
    
    public static RubroDAO getRubroDAO() {
        return INSTANCE.rubroDAO;
    }
    
    public static SubrubroDAO getSubrubroDAO() {
        return INSTANCE.subrubroDAO;
    }

    public static TributoDAO getTributoDAO() {
        return INSTANCE.tributoDAO;
    }
    
    public static AreaDAO getAreaDAO() {
        return INSTANCE.areaDAO;
    }

    public static TipObjImpAreODAO getTipObjImpAreODAO() {
        return INSTANCE.tipObjImpAreODAO;
    }

    public static TipoDAO getTipoDAO() {
        return INSTANCE.tipoDAO;
    }
    
    public static ConAtrDAO getConAtrDAO() {
        return INSTANCE.conAtrDAO;
    }
    
    public static FeriadoDAO getFeriadoDAO() {
        return INSTANCE.feriadoDAO;
    }
    
    public static RecursoDAO getRecursoDAO() {
        return INSTANCE.recursoDAO;
    }
    
    public static RecAtrValDAO getRecAtrValDAO() {
        return INSTANCE.recAtrValDAO;
    }
    
    public static RecConDAO getRecConDAO() {
        return INSTANCE.recConDAO;
    }
    
    public static RecClaDeuDAO getRecClaDeuDAO() {
        return INSTANCE.recClaDeuDAO;
    }
    
    public static RecGenCueAtrVaDAO getRecGenCueAtrVaDAO() {
        return INSTANCE.recGenCueAtrVaDAO;
    }
  
    public static RecAtrCueDAO getRecAtrCueDAO() {
        return INSTANCE.recAtrCueDAO;
    }
  
    public static RecEmiDAO getRecEmiDAO() {
        return INSTANCE.recEmiDAO;
    }
    
    public static RecAtrCueEmiDAO getRecAtrCueEmiDAO() {
        return INSTANCE.recAtrCueEmiDAO;
    }
    
    public static TipoEmisionDAO getTipoEmisionDAO() {
        return INSTANCE.tipoEmisionDAO;
    }
   
    public static PeriodoDeudaDAO getPeriodoDeudaDAO() {
        return INSTANCE.periodoDeudaDAO;
    }
  
    public static GenCueDAO getGenCueDAO() {
        return INSTANCE.genCueDAO;
    }
  
    public static GenCodGesDAO getGenCodGesDAO() {
        return INSTANCE.genCodGesDAO;
    }
  
    public static VencimientoDAO getVencimientoDAO() {
    	return INSTANCE.vencimientoDAO;
    }
    
    public static ServicioBancoDAO getServicioBancoDAO(){
    	return INSTANCE.servicioBancoDAO;
    }
    
    public static SerBanRecDAO getSerBanRecDAO(){
    	return INSTANCE.serBanRecDAO;
    }

    public static ViaDeudaDAO getViaDeudaDAO(){
    	return INSTANCE.viaDeudaDAO;
    }

    public static ZonaDAO getZonaDAO(){
    	return INSTANCE.zonaDAO;
    }
    
    public static SeccionDAO getSeccionDAO(){
    	return INSTANCE.seccionDAO;
    }

	public static TipoDeudaDAO getTipoDeudaDAO() {
		return INSTANCE.tipoDeudaDAO;
	}

	public static ParametroDAO getParametroDAO() {
		return INSTANCE.parametroDAO;
	}

	public static DesImpDAO getDesImpDAO() {
		return INSTANCE.desImpDAO;
	}
	
	public static BancoDAO getBancoDAO(){
		return INSTANCE.bancoDAO;
	}
	
	public static CalendarioDAO getCalendarioDAO(){
		return INSTANCE.calendarioDAO;
	}
	
	public static EmiMatDAO	getEmiMatDAO() {
		return INSTANCE.emiMatDAO;
	}
	
	public static ColEmiMatDAO	getColEmiMatDAO() {
		return INSTANCE.colEmiMatDAO;
	}
	
	
	public static RecConADecDAO getRecConADecDAO(){
		return INSTANCE.recConADecDAO;
	}
	
	public static RecTipAliDAO getRecTipAliDAO(){
		return INSTANCE.recTipAliDAO;
	}
	
	public static RecAliDAO getRecAliDAO(){
		return INSTANCE.recAliDAO;
	}
	
	public static RecMinDecDAO getRecMinDecDAO(){
		return INSTANCE.recMinDecDAO;
	}
	
	public static RecTipUniDAO getRecTipUniDAO(){
		return INSTANCE.recTipUniDAO;
	}
	
	public static ValUnRecConADeDAO getValUnRecConADeDAO(){
		return INSTANCE.valUnRecConADeDAO;
	}
	
	public static TipRecConADecDAO getTipRecConADecDAO(){
		return INSTANCE.tipRecConADecDAO;
	}
	
	public static MinRecConADecDAO getMinRecConADecDAO(){
		return INSTANCE.minRecConADecDAO;
	}
	
	public static TipCodEmiDAO getTipCodEmiDAO(){
		return INSTANCE.tipCodEmiDAO;
	}
	
	public static HistCodEmiDAO getHistCodEmiDAO(){
		return INSTANCE.histCodEmiDAO;
	}
	
	public static CodEmiDAO getCodEmiDAO(){
		return INSTANCE.codEmiDAO;
	}

	public static SituacionInmuebleDAO getSituacionInmuebleDAO(){
		return INSTANCE.situacionInmuebleDAO;
	}

	public static SolicitudInmuebleDAO getSolicitudInmuebleDAO(){
		return INSTANCE.solicitudInmuebleDAO;
	}
	
	public static CategoriaInmuebleDAO getCategoriaInmuebleDAO() {
		return INSTANCE.categoriaInmuebleDAO;
	}
	
	public static SuperficieInmuebleDAO getSuperficieInmuebleDAO() {
		return INSTANCE.superficieInmuebleDAO;
	}

	public static SiatScriptDAO getSiatScriptDAO() {
		return INSTANCE.siatScriptDAO;
	}

	public static SiatScriptUsrDAO getSiatScriptUsrDAO() {
		return INSTANCE.siatScriptUsrDAO;
	}
	
	public static RecursoAreaDAO getRecursoAreaDAO(){
		return INSTANCE.recursoAreaDAO;
	}

	public static CatRSDreiDAO getCategoriaRSDreiDAO() {
		return INSTANCE.categoriaRSDreiDAO;
	}
	
}
