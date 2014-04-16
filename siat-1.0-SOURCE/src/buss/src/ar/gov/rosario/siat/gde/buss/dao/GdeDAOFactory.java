//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import ar.gov.rosario.siat.def.buss.dao.DecJurDAO;
import ar.gov.rosario.siat.def.buss.dao.DecJurDetDAO;
import ar.gov.rosario.siat.def.buss.dao.DecJurPagDAO;
import ar.gov.rosario.siat.def.buss.dao.OriDecJurDAO;
import ar.gov.rosario.siat.def.buss.dao.TipDecJurDAO;
import ar.gov.rosario.siat.def.buss.dao.TipDecJurRecDAO;
import ar.gov.rosario.siat.def.buss.dao.TipPagDecJurDAO;


/**
 * Factory de Gestion Deuda (gdeuda) DAOs
 * 
 * @author tecso
 * 
 */
public class GdeDAOFactory {

    private static final GdeDAOFactory INSTANCE = new GdeDAOFactory();
    
    
    private DeudaDAO 			deudaDAO; // Nota: cuando se instancie desde aqui, boClass sera nulo.
    
    private DeudaAdminDAO 		deudaAdminDAO;
    private SerBanDesGenDAO 	serBanDesGenDAO;
    private DesGenDAO 			desGenDAO;
    private CriAsiProDAO 		criAsiProDAO;
    private EstadoDeudaDAO 		estadoDeudaDAO;
    private DeuAdmRecConDAO 	deuAdmRecConDAO;
    private DeudaJudicialDAO 	deudaJudicialDAO;
    private DeuJudRecConDAO 	deuJudRecConDAO;
    private TipoSelAlmDAO 		tipoSelAlmDAO;
    private SelAlmDAO 			selAlmDAO;
    private SelAlmDeudaDAO      selAlmDeudaDAO;
    private SelAlmDetDAO        selAlmDetDAO;
    
    private ProcesoMasivoDAO 	procesoMasivoDAO;
    private TipProMasDAO 	    tipProMasDAO;
    private DeudaCanceladaDAO 	deudaCanceladaDAO;
    private DeuCanRecConDAO 	deuCanRecConDAO;
    private DeudaAnuladaDAO 	deudaAnuladaDAO;
    private DeuAnuRecConDAO 	deuAnuRecConDAO;
    private TipoProcuradorDAO 	tipoProcuradorDAO;
    private ProcuradorDAO     	procuradorDAO;
    private PlanDAO			 	planDAO;
    private TipoDeudaPlanDAO 	tipoDeudaPlanDAO;	
    private PlanDescuentoDAO 	planDescuentoDAO;
    private PlanIntFinDAO 	 	planIntFinDAO;
    private EstadoConvenioDAO 	estadoConvenioDAO;
    private TipoPerForDAO    	tipoPerForDAO; 
    private TipoDocApoDAO 	 	tipoDocApoDAO;
    private EstadoConCuoDAO     estadoConCuoDAO;
    private ConvenioDAO			convenioDAO;
    private ConvenioCuotaDAO	convenioCuotaDAO;
    private ConvenioDeudaDAO    convenioDeudaDAO;
    private ProRecDAO         	proRecDAO;
    private AccionLogDAO        accionLogDAO;
    private SelAlmLogDAO        selAlmLogDAO;
    private ProMasProExcDAO     proMasProExcDAO;
    private ProMasDeuIncDAO     proMasDeuIncDAO;
    private ProMasDeuExcDAO     proMasDeuExcDAO;
    private MotExcDAO           motExcDAO;
    private ProRecDesHasDAO     proRecDesHasDAO;    
    private DesEspDAO			desEspDAO;		
    private PlanProrrogaDAO		planProrrogaDAO;
    private PlanForActDeuDAO    planForActDeuDAO;
    private PlanExeDAO			planExeDAO;
    private PlanAtrValDAO		planAtrValDAO;
    private PlanVenDAO			planVenDAO;
    private PlanMotCadDAO		planMotCadDAO;
    private PlanClaDeuDAO       planClaDeuDAO;
    
    private EstConDeuDAO        estConDeuDAO;
    private EstPlaEnvDeuPrDAO   estPlaEnvDeuPrDAO;
    private PlaEnvDeuProDAO     plaEnvDeuProDAO;
    private PlaEnvDeuPDetDAO    plaEnvDeuPDetDAO;
    private ConstanciaDeuDAO    constanciaDeuDAO;
    private ConDeuDetDAO        conDeuDetDAO;
    private HistEstConDeuDAO    histEstConDeuDAO;
    private HistEstPlaEnvDPDAO  histEstPlaEnvDPDAO;
    
    private ReciboDeudaDAO		reciboDeudaDAO;
    private ReciboDAO			reciboDAO;
    private DesRecClaDeuDAO		desRecClaDeuDAO;
    private DesAtrValDAO		desAtrValDAO;
    private DesEspExeDAO		desEspExeDAO;
    
    private PagoDeudaDAO		pagoDeudaDAO;
    private TipoPagoDAO			tipoPagoDAO;
    private ProRecComDAO		proRecComDAO;
    
    private ReciboConvenioDAO	reciboConvenioDAO;
    private RescateDAO			rescateDAO;
    private ConDeuCuoDAO		conDeuCuoDAO;
    private RecConCuoDAO		recConCuoDAO;
    
    private ConEstConDAO		conEstConDAO;
    private ConDeuTitDAO		conDeuTitDAO;
    
    private EventoDAO			eventoDAO;
    private EtapaProcesalDAO	etapaProcesalDAO;
    
    private PlanImpMinDAO		planImpMinDAO;
    private GesJudDAO			gesJudDAO;
    private GesJudDeuDAO		gesJudDeuDAO;
    private GesJudEventoDAO		gesJudEventoDAO;
    
    private TraspasoDeudaDAO	traspasoDeudaDAO;
    private DevolucionDeudaDAO	devolucionDeudaDAO;
    private TraDeuDetDAO	    traDeuDetDAO;
    private DevDeuDetDAO	    devDeuDetDAO;
    
    private HistGesJudDAO       histGesJudDAO;
    
    private GravamenesJDBCDAO	gravamenesJDBCDAO;
    private CtrlInfDeuDAO 		ctrlInfDeuDAO;
    
    private LiqComDAO			liqComDAO;
    private LiqComProDAO		liqComProDAO;
    private LiqComProDeuDAO		liqComProDeuDAO;
    private AuxLiqComProDeuDAO	auxLiqComProDeuDAO;
    
    private ResDetDAO			resDetDAO;
    private AuxAplPagCueDAO		auxAplPagCueDAO;
    private SalPorCadDAO		salPorCadDAO;
    private SelAlmPlanesDAO		selAlmPlanesDAO;
    
    private MotAnuDeuDAO		motAnuDeuDAO;
    
    private AnulacionDAO		anulacionDAO;
    
    private ConRecNoLiqDAO		conRecNoLiqDAO;
    
    private SalPorCadDetDAO		salPorCadDetDAO;
    
    private ImporteRecaudarDAO	importeRecaudarDAO;
    private ImporteRecaudadoDAO	importeRecaudadoDAO;
    private PlanRecursoDAO		planRecursoDAO;
    
    private ProPreDeuDAO		proPreDeuDAO;
    private ProPreDeuDetDAO		proPreDeuDetDAO;
    private EstProPreDeuDetDAO	estProPreDeuDetDAO;
    
        
    private TipDecJurDAO		tipDecJurDAO;
    private TipDecJurRecDAO		tipDecJurRecDAO;
    private OriDecJurDAO		oriDecJurDAO;
    private DecJurDAO			decJurDAO;
    private DecJurDetDAO		decJurDetDAO;
    private DecJurPagDAO		decJurPagDAO;
    private TipPagDecJurDAO		tipPagDecJurDAO;
    private TipoMultaDAO		tipoMultaDAO;
    private MultaDAO			multaDAO;
    private DescuentoMultaDAO	descuentoMultaDAO;
    private MultaDetDAO 		multaDetDAO;
    private MultaHistoricoDAO	multaHistoricoDAO;
    private CierreComercioDAO   cierreComercioDAO;
    private MotivoCierreDAO 	motivoCierreDAO;
    private DesgloseDAO 	    desgloseDAO;
    private DesgloseDetDAO 	    desgloseDetDAO;
    
    private MandatarioDAO		mandatarioDAO;
    private RodadoJDBCDAO       rodadoJDBCDAO;
    
    private EstadoCobranzaDAO	estadoCobranzaDAO;
    private CobranzaDAO			cobranzaDAO;
    private EstadoAjusteDAO		estadoAjusteDAO;
    private GesCobDAO			gesCobDAO;
    private CobranzaDetDAO		cobranzaDetDAO;
    private PerCobDAO			perCobDAO;
    private AgeRetDAO			ageRetDAO;
    private IndiceCompensacionDAO		indiceCompensacionDAO;
    
    private HisInfDeuDAO		hisInfDeuDAO;
    
    private GdeDAOFactory() {
        super();  
        this.deudaAdminDAO 			= new DeudaAdminDAO();
        this.serBanDesGenDAO		= new SerBanDesGenDAO();
        this.desGenDAO 				= new DesGenDAO();
        this.criAsiProDAO 			= new CriAsiProDAO();
        this.estadoDeudaDAO 		= new EstadoDeudaDAO();
        this.deuAdmRecConDAO		= new DeuAdmRecConDAO();
        this.deudaJudicialDAO		= new DeudaJudicialDAO();
        this.deuJudRecConDAO		= new DeuJudRecConDAO();
        this.tipoSelAlmDAO          = new TipoSelAlmDAO();
        this.selAlmDAO          	= new SelAlmDAO();
        this.selAlmDeudaDAO         = new SelAlmDeudaDAO();
        this.selAlmDetDAO          	= new SelAlmDetDAO();
        this.tipProMasDAO           = new TipProMasDAO();
        this.procesoMasivoDAO       = new ProcesoMasivoDAO();
        this.deudaCanceladaDAO		= new DeudaCanceladaDAO();
        this.deuCanRecConDAO		= new DeuCanRecConDAO();
        this.deudaAnuladaDAO		= new DeudaAnuladaDAO();
        this.deuAnuRecConDAO		= new DeuAnuRecConDAO();
        this.tipoProcuradorDAO		= new TipoProcuradorDAO();
        this.procuradorDAO			= new ProcuradorDAO();

		this.proRecDAO              = new ProRecDAO();

        this.planDAO			    = new PlanDAO();
        this.tipoDeudaPlanDAO		= new TipoDeudaPlanDAO();
        this.planDescuentoDAO		= new PlanDescuentoDAO();
        this.planIntFinDAO			= new PlanIntFinDAO();
        this.estadoConvenioDAO		= new EstadoConvenioDAO();
        this.tipoPerForDAO			= new TipoPerForDAO();
        this.tipoDocApoDAO			= new TipoDocApoDAO();
        this.estadoConCuoDAO		= new EstadoConCuoDAO();
        this.convenioDAO			= new ConvenioDAO();
        this.convenioCuotaDAO		= new ConvenioCuotaDAO();
        this.convenioDeudaDAO		= new ConvenioDeudaDAO();
        this.accionLogDAO           = new AccionLogDAO();
        this.selAlmLogDAO           = new SelAlmLogDAO();
        this.proMasProExcDAO        = new ProMasProExcDAO();
        this.proMasDeuIncDAO        = new ProMasDeuIncDAO();
        this.proMasDeuExcDAO        = new ProMasDeuExcDAO();
        this.motExcDAO              = new MotExcDAO();
        this.proRecDesHasDAO        = new ProRecDesHasDAO();
        this.desEspDAO				= new DesEspDAO();
        
        this.planProrrogaDAO        = new PlanProrrogaDAO();
        this.planForActDeuDAO		= new PlanForActDeuDAO();
        this.planExeDAO				= new PlanExeDAO();
        this.planAtrValDAO			= new PlanAtrValDAO();
        this.planVenDAO				= new PlanVenDAO();
        this.planMotCadDAO			= new PlanMotCadDAO();
        this.planClaDeuDAO			= new PlanClaDeuDAO();
        
        this.estConDeuDAO           = new EstConDeuDAO();
        this.estPlaEnvDeuPrDAO      = new EstPlaEnvDeuPrDAO();
        this.plaEnvDeuProDAO        = new PlaEnvDeuProDAO();
        this.plaEnvDeuPDetDAO       = new PlaEnvDeuPDetDAO();
        this.constanciaDeuDAO       = new ConstanciaDeuDAO();
        this.conDeuDetDAO           = new ConDeuDetDAO();
        this.histEstConDeuDAO       = new HistEstConDeuDAO();
        this.histEstPlaEnvDPDAO     = new HistEstPlaEnvDPDAO();
        
        this.reciboDeudaDAO			= new ReciboDeudaDAO();
        this.reciboDAO				= new ReciboDAO();
        this.desRecClaDeuDAO		= new DesRecClaDeuDAO();
        this.desAtrValDAO			= new DesAtrValDAO();
        this.desEspExeDAO			= new DesEspExeDAO();
        
        this.pagoDeudaDAO			= new PagoDeudaDAO();
        this.tipoPagoDAO			= new TipoPagoDAO();
        this.proRecComDAO			= new ProRecComDAO();
        
        this.reciboConvenioDAO		= new ReciboConvenioDAO();
        this.rescateDAO				= new RescateDAO();
        this.conDeuCuoDAO			= new ConDeuCuoDAO();
        this.recConCuoDAO			= new RecConCuoDAO();
        
        this.conEstConDAO			= new ConEstConDAO();
        this.conDeuTitDAO			= new ConDeuTitDAO();
        
        this.eventoDAO				= new EventoDAO();
        this.etapaProcesalDAO		= new EtapaProcesalDAO();
        this.planImpMinDAO			= new PlanImpMinDAO();

        this.gesJudDAO				= new GesJudDAO();
        this.gesJudDeuDAO			= new GesJudDeuDAO();
        this.histGesJudDAO          = new HistGesJudDAO();
        this.gesJudEventoDAO		= new GesJudEventoDAO();
        
        this.traspasoDeudaDAO       = new TraspasoDeudaDAO();
        this.devolucionDeudaDAO     = new DevolucionDeudaDAO();
        this.traDeuDetDAO           = new TraDeuDetDAO();
        this.devDeuDetDAO           = new DevDeuDetDAO();
        
        this.ctrlInfDeuDAO			= new CtrlInfDeuDAO();
        this.gravamenesJDBCDAO		= new GravamenesJDBCDAO();
        
        this.liqComDAO				= new LiqComDAO();
        this.liqComProDAO			= new LiqComProDAO();
        this.liqComProDeuDAO		= new LiqComProDeuDAO();
        this.auxLiqComProDeuDAO		= new AuxLiqComProDeuDAO();
        
        this.resDetDAO				= new ResDetDAO();
        this.auxAplPagCueDAO		= new AuxAplPagCueDAO();
        
        this.conRecNoLiqDAO			= new ConRecNoLiqDAO();
        this.salPorCadDAO			= new SalPorCadDAO();

        this.selAlmPlanesDAO		= new SelAlmPlanesDAO();

        this.motAnuDeuDAO			= new MotAnuDeuDAO();
        this.anulacionDAO			= new AnulacionDAO();
        
        this.salPorCadDetDAO		= new SalPorCadDetDAO();
        
        this.importeRecaudarDAO		= new ImporteRecaudarDAO();
        this.importeRecaudadoDAO	= new ImporteRecaudadoDAO();
        
        this.deudaDAO 				= new DeudaAdminDAO();
        this.planRecursoDAO			= new PlanRecursoDAO();
        
        this.proPreDeuDAO 			= new ProPreDeuDAO();
        this.proPreDeuDetDAO		= new ProPreDeuDetDAO();
        this.estProPreDeuDetDAO		= new EstProPreDeuDetDAO();

        this.tipDecJurDAO			= new TipDecJurDAO();
        this.tipDecJurRecDAO		= new TipDecJurRecDAO();
        this.oriDecJurDAO			= new OriDecJurDAO();
        this.decJurDAO				= new DecJurDAO();
        this.decJurDetDAO			= new DecJurDetDAO();
        this.decJurPagDAO			= new DecJurPagDAO();
        this.tipPagDecJurDAO		= new TipPagDecJurDAO();
        this.tipoMultaDAO			= new TipoMultaDAO();
        this.multaDAO				= new MultaDAO();
        this.descuentoMultaDAO		= new DescuentoMultaDAO();
        this.multaDetDAO			= new MultaDetDAO();
        this.cierreComercioDAO		= new CierreComercioDAO();
        this.motivoCierreDAO		= new MotivoCierreDAO();
        this.desgloseDAO		    = new DesgloseDAO();
        this.desgloseDetDAO		    = new DesgloseDetDAO();
        
        this.mandatarioDAO			= new MandatarioDAO();
        this.rodadoJDBCDAO          = new RodadoJDBCDAO();
        this.multaHistoricoDAO		= new MultaHistoricoDAO();
        
        this.estadoCobranzaDAO		= new EstadoCobranzaDAO();
        this.cobranzaDAO			= new CobranzaDAO();
        this.estadoAjusteDAO		= new EstadoAjusteDAO();
        this.gesCobDAO				= new GesCobDAO();
        this.cobranzaDetDAO			= new CobranzaDetDAO();
        
        this.perCobDAO				= new PerCobDAO();
        this.ageRetDAO				= new AgeRetDAO();
        this.indiceCompensacionDAO			= new IndiceCompensacionDAO();
        
        this.hisInfDeuDAO			= new HisInfDeuDAO();
    }

    /**
     * Devuelve una la instancia deudaDAO para poder utilizar los metodos que no dependan de las subclases de deuda. 
     * 
     * @author tecso
     * @return
     */
    public static DeudaDAO getDeudaDAO() {
        return INSTANCE.deudaDAO;
    }
    
    public static DeudaAdminDAO getDeudaAdminDAO() {
        return INSTANCE.deudaAdminDAO;
    }    
    public static SerBanDesGenDAO getSerBanDesGenDAO(){
    	return INSTANCE.serBanDesGenDAO;
    }
    public static DesGenDAO getDesGenDAO(){
    	return INSTANCE.desGenDAO;
    }
    public static CriAsiProDAO getCriAsiProDAO(){
    	return INSTANCE.criAsiProDAO;
    }
    public static EstadoDeudaDAO getEstadoDeudaDAO(){
    	return INSTANCE.estadoDeudaDAO;
    }
    public static DeuAdmRecConDAO getDeuAdmRecConDAO(){
    	return INSTANCE.deuAdmRecConDAO;
    }
    public static DeudaJudicialDAO getDeudaJudicialDAO() {
        return INSTANCE.deudaJudicialDAO;
    }
    public static DeuJudRecConDAO getDeuJudRecConDAO(){
    	return INSTANCE.deuJudRecConDAO;
    }
    public static TipoSelAlmDAO getTipoSelAlmDAO(){
    	return INSTANCE.tipoSelAlmDAO;
    }
    public static SelAlmDAO getSelAlmDAO(){
    	return INSTANCE.selAlmDAO;
    }
    public static SelAlmDeudaDAO getSelAlmDeudaDAO(){
    	return INSTANCE.selAlmDeudaDAO;
    }
    public static SelAlmDetDAO getSelAlmDetDAO(){
    	return INSTANCE.selAlmDetDAO;
    }
    public static ProcesoMasivoDAO getProcesoMasivoDAO(){
    	return INSTANCE.procesoMasivoDAO;
    }    
    public static TipProMasDAO getTipProMasDAO(){
    	return INSTANCE.tipProMasDAO;
    }    
    public static DeudaCanceladaDAO getDeudaCanceladaDAO() {
        return INSTANCE.deudaCanceladaDAO;
    }
    public static DeuCanRecConDAO getDeuCanRecConDAO(){
    	return INSTANCE.deuCanRecConDAO;
    }
    public static DeudaAnuladaDAO getDeudaAnuladaDAO() {
        return INSTANCE.deudaAnuladaDAO;
    }
    public static DeuAnuRecConDAO getDeuAnuRecConDAO(){
    	return INSTANCE.deuAnuRecConDAO;
    }
    
    public static TipoProcuradorDAO getTipoProcuradorDAO(){
    	return INSTANCE.tipoProcuradorDAO;
    }
    
    public static ProcuradorDAO getProcuradorDAO(){
    	return INSTANCE.procuradorDAO;
    }
    
    public static ProRecDAO getProRecDAO(){
    	return INSTANCE.proRecDAO;
    }
    
    public static PlanDAO getPlanDAO(){
    	return INSTANCE.planDAO;
    }
    
    public static TipoDeudaPlanDAO getTipoDeudaPlanDAO(){
    	return INSTANCE.tipoDeudaPlanDAO;
    }
    
    public static PlanDescuentoDAO getPlanDescuentoDAO(){
    	return INSTANCE.planDescuentoDAO;
    }
    
    public static PlanIntFinDAO getPlanIntFinDAO(){
    	return INSTANCE.planIntFinDAO;
    }
    
    public static EstadoConvenioDAO getEstadoConvenioDAO(){
    	return INSTANCE.estadoConvenioDAO;
    }
    
    public static TipoPerForDAO getTipoPerForDAO(){
    	return INSTANCE.tipoPerForDAO;
    }
    
    public static TipoDocApoDAO getTipoDocApoDAO(){
    	return INSTANCE.tipoDocApoDAO;
    }
    
    public static EstadoConCuoDAO getEstadoConCuoDAO(){
    	return INSTANCE.estadoConCuoDAO;
    }
    
    public static ConvenioDAO getConvenioDAO(){
    	return INSTANCE.convenioDAO;
    }
    
    public static ConvenioCuotaDAO getConvenioCuotaDAO(){
    	return INSTANCE.convenioCuotaDAO;
    }
    
    public static ConvenioDeudaDAO getConvenioDeudaDAO(){
    	return INSTANCE.convenioDeudaDAO;
    }
    
    public static AccionLogDAO getAccionLogDAO(){
    	return INSTANCE.accionLogDAO;
    }
    
    public static SelAlmLogDAO getSelAlmLogDAO(){
    	return INSTANCE.selAlmLogDAO;
    }
    
    public static ProMasProExcDAO getProMasProExcDAO(){
    	return INSTANCE.proMasProExcDAO;
    }

	public static DesEspDAO getDesEspDAO() {
		return INSTANCE.desEspDAO;
	}
    
    public static ProMasDeuIncDAO getProMasDeuIncDAO(){
    	return INSTANCE.proMasDeuIncDAO;
    }
    
    public static ProMasDeuExcDAO getProMasDeuExcDAO(){
    	return INSTANCE.proMasDeuExcDAO;
    }

    public static MotExcDAO getMotExcDAO(){
    	return INSTANCE.motExcDAO;
    }
    
    public static ProRecDesHasDAO getProRecDesHasDAO(){
    	return INSTANCE.proRecDesHasDAO;
    }
    
    public static PlanProrrogaDAO getPlanProrrogaDAO(){
    	return INSTANCE.planProrrogaDAO;
    }
    
    public static PlanForActDeuDAO getPlanForActDeuDAO(){
    	return INSTANCE.planForActDeuDAO;
    }
    
    public static PlanExeDAO getPlanExeDAO(){
    	return INSTANCE.planExeDAO;
    }
    
    public static PlanAtrValDAO getPlanAtrValDAO(){
    	return INSTANCE.planAtrValDAO;    	
    }
    
    public static PlanVenDAO getPlanVenDAO(){
    	return INSTANCE.planVenDAO;
    }
    
    public static PlanMotCadDAO getPlanMotCadDAO(){
    	return INSTANCE.planMotCadDAO;
    }
    
    public static PlanClaDeuDAO getPlanClaDeuDAO(){
    	return INSTANCE.planClaDeuDAO;
    }
    
    public static EstConDeuDAO getEstConDeuDAO(){
    	return INSTANCE.estConDeuDAO;
    }
    
    public static EstPlaEnvDeuPrDAO getEstPlaEnvDeuPrDAO(){
    	return INSTANCE.estPlaEnvDeuPrDAO;
    }
    
    public static PlaEnvDeuProDAO getPlaEnvDeuProDAO(){
    	return INSTANCE.plaEnvDeuProDAO;
    }

    public static PlaEnvDeuPDetDAO getPlaEnvDeuPDetDAO(){
    	return INSTANCE.plaEnvDeuPDetDAO;
    }
    
    public static ConstanciaDeuDAO getConstanciaDeuDAO(){
    	return INSTANCE.constanciaDeuDAO;
    }
    
    public static ConDeuDetDAO getConDeuDetDAO(){
    	return INSTANCE.conDeuDetDAO;
    }
    
    public static ReciboDeudaDAO getReciboDeudaDAO(){
    	return INSTANCE.reciboDeudaDAO;
    }
    
    public static ReciboDAO getReciboDAO(){
    	return INSTANCE.reciboDAO;
    }
    
	public static DesRecClaDeuDAO getDesRecClaDeuDAO() {
		return INSTANCE.desRecClaDeuDAO;
	}

	
	public static DesAtrValDAO getDesAtrValDAO() {
		return INSTANCE.desAtrValDAO;
	}

	
	public static DesEspExeDAO getDesEspExeDAO() {
		return INSTANCE.desEspExeDAO;
	}
	
	public static HistEstConDeuDAO getHistEstConDeuDAO(){
		return INSTANCE.histEstConDeuDAO;
	}
	
	
	public static HistEstPlaEnvDPDAO getHistEstPlaEnvDPDAO(){
		return INSTANCE.histEstPlaEnvDPDAO;
	}

	public static PagoDeudaDAO getPagoDeudaDAO(){
		return INSTANCE.pagoDeudaDAO;
	}
	
	public static TipoPagoDAO getTipoPagoDAO(){
		return INSTANCE.tipoPagoDAO;
	}
	
	public static ProRecComDAO getProRecComDAO(){
		return INSTANCE.proRecComDAO;
	}

	public static ReciboConvenioDAO getReciboConvenioDAO(){
		return INSTANCE.reciboConvenioDAO;
	}
	
	public static RescateDAO getRescateDAO(){
		return INSTANCE.rescateDAO;
	}
		
	public static ConDeuCuoDAO getConDeuCuoDAO(){
		return INSTANCE.conDeuCuoDAO;
	}

	public static RecConCuoDAO getRecConCuoDAO(){
		return INSTANCE.recConCuoDAO;
	}
	
	public static ConEstConDAO getConEstConDAO(){
		return INSTANCE.conEstConDAO;
	}

	
	public static ConDeuTitDAO getConDeuTitDAO() {
		return INSTANCE.conDeuTitDAO;
	}

	public static EventoDAO getEventoDAO(){
		return INSTANCE.eventoDAO;
	}
	
	public static EtapaProcesalDAO getEtapaProcesalDAO(){
		return INSTANCE.etapaProcesalDAO;
	}
	
	public static PlanImpMinDAO getPlanImpMinDAO(){
		return INSTANCE.planImpMinDAO;
	}
	
	public static GesJudDAO getGesJudDAO(){
		return INSTANCE.gesJudDAO;
	}
	
	public static GesJudDeuDAO getGesJudDeuDAO(){
		return INSTANCE.gesJudDeuDAO;
	}
	
	public static HistGesJudDAO getHistGesJudDAO(){
		return INSTANCE.histGesJudDAO;
	}
	
	public static DevolucionDeudaDAO getDevolucionDeudaDAO(){
		return INSTANCE.devolucionDeudaDAO;
	}
	
	public static TraspasoDeudaDAO getTraspasoDeudaDAO(){
		return INSTANCE.traspasoDeudaDAO;
	}
	
	public static TraDeuDetDAO getTraDeuDetDAO(){
		return INSTANCE.traDeuDetDAO;
	}
	
	public static DevDeuDetDAO getDevDeuDetDAO(){
		return INSTANCE.devDeuDetDAO;
	}
	
	public static GesJudEventoDAO getGesJudEventoDAO(){
		return INSTANCE.gesJudEventoDAO;
	}
	
	public static CtrlInfDeuDAO getCtrlInfDeuDAO(){
		return INSTANCE.ctrlInfDeuDAO;
	}
		
	public static GravamenesJDBCDAO getGravamenesJDBCDAO(){
		return INSTANCE.gravamenesJDBCDAO;
	}
	
	public static LiqComDAO getLiqComDAO(){
		return INSTANCE.liqComDAO;
	}
	
	public static LiqComProDAO getLiqComProDAO(){
		return INSTANCE.liqComProDAO;
	}	
	
	public static LiqComProDeuDAO getLiqComProDeuDAO(){
		return INSTANCE.liqComProDeuDAO;
	}	
	
	public static AuxLiqComProDeuDAO getAuxLiqComProDeuDAO(){
		return INSTANCE.auxLiqComProDeuDAO;
	}
	
	public static ResDetDAO getResDetDAO(){
		return INSTANCE.resDetDAO;
	}
	
	public static AuxAplPagCueDAO getAuxAplPagCueDAO(){
		return INSTANCE.auxAplPagCueDAO;
	}
	
	public static ConRecNoLiqDAO getConRecNoLiqDAO(){
		return INSTANCE.conRecNoLiqDAO;
	}	
	
	public static SalPorCadDAO getSalPorCadDAO(){
		return INSTANCE.salPorCadDAO;
	}

	
	public static SelAlmPlanesDAO getSelAlmPlanesDAO(){
		return INSTANCE.selAlmPlanesDAO;
	}

	
	public static MotAnuDeuDAO getMotAnuDeuDAO(){
		return INSTANCE.motAnuDeuDAO;
	}

	public static AnulacionDAO getAnulacionDAO(){
		return INSTANCE.anulacionDAO;
	}
	
	public static SalPorCadDetDAO getSalPorCadDetDAO(){
		return INSTANCE.salPorCadDetDAO;
	}

	public static ImporteRecaudarDAO getImporteRecaudarDAO(){
		return INSTANCE.importeRecaudarDAO;
	}

	public static ImporteRecaudadoDAO getImporteRecaudadoDAO(){
		return INSTANCE.importeRecaudadoDAO;
	}
	
	public static PlanRecursoDAO getPlanRecursoDAO(){
		return INSTANCE.planRecursoDAO;
	}

	public static ProPreDeuDAO getProPreDeuDAO(){
		return INSTANCE.proPreDeuDAO;
	}	

	public static ProPreDeuDetDAO getProPreDeuDetDAO(){
		return INSTANCE.proPreDeuDetDAO;
	}	

	public static EstProPreDeuDetDAO getEstProPreDeuDetDAO(){
		return INSTANCE.estProPreDeuDetDAO;
	}
	
	
	public static TipDecJurDAO getTipDecJurDAO(){
		return INSTANCE.tipDecJurDAO;
	}
	
	public static TipDecJurRecDAO getTipDecJurRecDAO(){
		return INSTANCE.tipDecJurRecDAO;
	}
	
	public static OriDecJurDAO getOriDecJurDAO(){
		return INSTANCE.oriDecJurDAO;
	}
	
	public static DecJurDAO getDecJurDAO(){
		return INSTANCE.decJurDAO;
	}
	
	public static DecJurDetDAO getDecJurDetDAO(){
		return INSTANCE.decJurDetDAO;
	}
	
	public static DecJurPagDAO getDecJurPagDAO(){
		return INSTANCE.decJurPagDAO;
	}
	
	public static TipPagDecJurDAO getTipPagDecJurDAO(){
		return INSTANCE.tipPagDecJurDAO;
	}

	public static TipoMultaDAO getTipoMultaDAO(){
		return INSTANCE.tipoMultaDAO;
	}
	
	public static MultaDAO getMultaDAO(){
		return INSTANCE.multaDAO;
	}
	
	public static DescuentoMultaDAO getDescuentoMultaDAO(){
		return INSTANCE.descuentoMultaDAO;
	}
	
	public static MultaDetDAO getMultaDetDAO(){
		return INSTANCE.multaDetDAO;
	}
	
	public static CierreComercioDAO getCierreComercioDAO() {
		return INSTANCE.cierreComercioDAO;
	}
	
	public static MotivoCierreDAO getMotivoCierreDAO() {
		return INSTANCE.motivoCierreDAO;
	}
	
	public static DesgloseDAO getDesgloseDAO() {
		return INSTANCE.desgloseDAO;
	}
	
	public static DesgloseDetDAO getDesgloseDetDAO() {
		return INSTANCE.desgloseDetDAO;
	}
	
	public static MandatarioDAO getMandatarioDAO(){
		return INSTANCE.mandatarioDAO;
	}
	public static RodadoJDBCDAO getRodadoJDBCDAO() {
		return INSTANCE.rodadoJDBCDAO;
	}
	
	public static MultaHistoricoDAO getMultaHistoricoDAO(){
		return INSTANCE.multaHistoricoDAO;
	}
	
	public static EstadoCobranzaDAO getEstadoCobranzaDAO(){
		return INSTANCE.estadoCobranzaDAO;
	}
	
	public static CobranzaDetDAO getCobranzaDetDAO(){
		return INSTANCE.cobranzaDetDAO;
	}
	
	public static CobranzaDAO getCobranzaDAO(){
		return INSTANCE.cobranzaDAO;
	}
	
	public static EstadoAjusteDAO getEstadoAjusteDAO(){
		return INSTANCE.estadoAjusteDAO;
	}
	
	public static GesCobDAO getGesCobDAO(){
		return INSTANCE.gesCobDAO;
	}

	public static PerCobDAO getPerCobDAO(){
		return INSTANCE.perCobDAO;
	}
	
	public static AgeRetDAO getAgeRetDAO(){
		return INSTANCE.ageRetDAO;
	}

	public static IndiceCompensacionDAO getIndiceCompensacionDAO(){
		return INSTANCE.indiceCompensacionDAO;
	}

	public static HisInfDeuDAO getHisInfDeuDAO(){
		return INSTANCE.hisInfDeuDAO;
	}
	
}
