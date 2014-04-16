//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;


/**
 * Factory de Balance DAOs
 * 
 * @author tecso
 * 
 */
public class BalDAOFactory {

    private static final BalDAOFactory INSTANCE = new BalDAOFactory();
    
    private IndeterminadoJDBCDAO             indeterminadoJDBCDAO;
    private SistemaDAO						 sistemaDAO;
    private PartidaDAO						 partidaDAO;
    private TipoImporteDAO					 tipoImporteDAO;
    private DisParDAO						 disParDAO;
    private DisParDetDAO					 disParDetDAO;
    private DisParRecDAO					 disParRecDAO;
    private DisParPlaDAO					 disParPlaDAO;
    private ImpSelDAO						 impSelDAO;
    private SelladoDAO						 selladoDAO;
    private TipoSelladoDAO					 tipoSelladoDAO;
    private AccionDAO						 accionDAO;
    private AccionSelladoDAO				 accionSelladoDAO;
    private CanalDAO						 canalDAO;
    private AsentamientoDAO					 asentamientoDAO;
    private EjercicioDAO					 ejercicioDAO;
    private EstEjercicioDAO					 estEjercicioDAO;
    private TransaccionDAO					 transaccionDAO;
    private AuxPagDeuDAO					 auxPagDeuDAO;
    private AuxRecaudadoDAO					 auxRecaudadoDAO;
    private SinSalAFavDAO					 sinSalAFavDAO;
    private ParSelDAO						 parSelDAO;
    private TipoDistribDAO					 tipoDistribDAO;
	private ToleranciaDAO					 toleranciaDAO;
	private SinIndetDAO						 sinIndetDAO;
	private SinPartidaDAO					 sinPartidaDAO;
	private AuxSelladoDAO					 auxSelladoDAO;
	private TipoIndetDAO					 tipoIndetDAO;
	private AuxPagCuoDAO					 auxPagCuoDAO;
	private AuxConDeuDAO					 auxConDeuDAO;
	private AuxConDeuCuoDAO					 auxConDeuCuoDAO;
	private AuxConvenioDAO					 auxConvenioDAO;
	private ReclamoDAO					     reclamoDAO;
	private EstadoReclamoDAO				 estadoReclamoDAO;
	private HisCamEstRecDAO                  hisCamEstRecDAO;
	private SaldoAFavorDAO                   saldoAFavorDAO;
	private EstSalAFavDAO                    estSalAFavDAO;
	private TipoOrigenDAO                    tipoOrigenDAO;
	private TipoSalAFavDAO                   tipoSalAFavDAO;
	private AuxImpRecDAO					 auxImpRecDAO;
	
	private ParCueBanDAO                     parCueBanDAO;
	private CuentaBancoDAO                   cuentaBancoDAO;
	private TipCueBanDAO                     tipCueBanDAO;
	private ClasificadorDAO					 clasificadorDAO;
	private NodoDAO					 		 nodoDAO;
	private RelClaDAO						 relClaDAO;
	private RelPartidaDAO					 relPartidaDAO;
	private TipOriMovDAO					 tipOriMovDAO;
	private EstDiaParDAO					 estDiaParDAO;
	private HisEstDiaParDAO					 hisEstDiaParDAO;
	private DiarioPartidaDAO				 diarioPartidaDAO;
	private OtrIngTesDAO					 otrIngTesDAO;
	private EstOtrIngTesDAO					 estOtrIngTesDAO;
	private HisEstOtrIngTesDAO				 hisEstOtrIngTesDAO;
	private OtrIngTesParDAO				 	 otrIngTesParDAO;
	private OtrIngTesRecConDAO				 otrIngTesRecConDAO;
	private Caja7DAO                         caja7DAO;
	private BalanceDAO						 balanceDAO;
	private ImpParDAO						 impParDAO;
	private ArchivoDAO						 archivoDAO;
	private TipoArcDAO						 tipoArcDAO;
	private EstadoArcDAO					 estadoArcDAO;
	private TranArcDAO						 tranArcDAO;
	private AuxCaja7DAO                      auxCaja7DAO;
	
	private FolioDAO						 folioDAO;
	private FolComDAO						 folComDAO;
	private FolDiaCobDAO					 folDiaCobDAO;
	private FolDiaCobColDAO					 folDiaCobColDAO;
	private TipoCobDAO						 tipoCobDAO;
	private EstadoFolDAO					 estadoFolDAO;
	private TipoComDAO             			 tipoComDAO;
	private AuxDeuMdfDAO             		 auxDeuMdfDAO;
	private CompensacionDAO					 compensacionDAO;
	private ComDeuDAO						 comDeuDAO;
	private TipoComDeuDAO					 tipoComDeuDAO;
	private EstadoComDAO					 estadoComDAO;
	private HisEstComDAO					 hisEstComDAO;
	private ReingresoDAO					 reingresoDAO;
	private Caja69DAO						 caja69DAO;
	private TranBalDAO						 tranBalDAO;
	private AseDelDAO						 aseDelDAO;
	private TranDelDAO						 tranDelDAO;
	private IndetBalDAO						 indetBalDAO;
	private SaldoBalDAO						 saldoBalDAO;
	private LeyParAcuDAO					 leyParAcuDAO;
	
	private EstadoEnvioDAO					estadoEnvioDAO;
    private EnvioOsirisDAO					envioOsirisDAO;
    private MulatorJDBCDAO					mulatorJDBCDAO;
    private TipoOperacionDAO				tipoOperacionDAO;
    private CierreBancoDAO					cierreBancoDAO;
    private EstTranAfipDAO					estTranAfipDAO;
    private TranAfipDAO						tranAfipDAO;
    private LogTranAfipDAO					logTranAfipDAO;
    private EstDetPagoDAO					estDetPagoDAO;
    private DetallePagoDAO					detallePagoDAO;
    private LogDetallePagoDAO				logDetallePagoDAO;
    private EstDetDJDAO						estDetDJDAO;
    private DetalleDJDAO					detalleDJDAO;
    private LogDetalleDJDAO					logDetalleDJDAO;
    private NotaImptoDAO					notaImptoDAO;
    private CierreSucursalDAO				cierreSucursalDAO;
    private NovedadEnvioDAO					novedadEnvioDAO;
    private ConciliacionDAO					conciliacionDAO;
    private NotaConcDAO 					notaConcDAO;
    private MovBanDAO 						movBanDAO;
    private MovBanDetDAO 					movBanDetDAO;
    private EnvNotOblDAO					envNotOblDAO;
    private IndeterminadoDAO 				indeterminadoDAO;
    private DupliceDAO 						dupliceDAO;
    private ReingIndetDAO 					reingIndetDAO;
    private IndetAuditDAO 					indetAuditDAO;
	
	private BalDAOFactory() {
        super();  
        this.indeterminadoJDBCDAO                = new IndeterminadoJDBCDAO();
        this.sistemaDAO							 = new SistemaDAO();
        this.partidaDAO							 = new PartidaDAO();
        this.tipoImporteDAO						 = new TipoImporteDAO();
        this.disParDAO							 = new DisParDAO();
        this.disParDetDAO						 = new DisParDetDAO();
        this.disParRecDAO						 = new DisParRecDAO();
        this.disParPlaDAO						 = new DisParPlaDAO();
        this.impSelDAO							 = new ImpSelDAO();
        this.selladoDAO							 = new SelladoDAO();
        this.tipoSelladoDAO						 = new TipoSelladoDAO();
        this.accionDAO							 = new AccionDAO();
        this.accionSelladoDAO 					 = new AccionSelladoDAO();
        this.canalDAO							 = new CanalDAO();
        this.asentamientoDAO					 = new AsentamientoDAO();
        this.ejercicioDAO						 = new EjercicioDAO();
        this.estEjercicioDAO					 = new EstEjercicioDAO();
        this.transaccionDAO						 = new TransaccionDAO();
        this.auxPagDeuDAO						 = new AuxPagDeuDAO();
        this.auxRecaudadoDAO					 = new AuxRecaudadoDAO();
        this.sinSalAFavDAO						 = new SinSalAFavDAO();
        this.parSelDAO							 = new ParSelDAO();
        this.tipoDistribDAO				     	 = new TipoDistribDAO();
        this.toleranciaDAO				         = new ToleranciaDAO();
        this.sinIndetDAO						 = new SinIndetDAO();
        this.sinPartidaDAO						 = new SinPartidaDAO();
        this.auxSelladoDAO						 = new AuxSelladoDAO();
        this.tipoIndetDAO						 = new TipoIndetDAO();
        this.auxPagCuoDAO						 = new AuxPagCuoDAO();
        this.auxConDeuDAO						 = new AuxConDeuDAO();
        this.auxConDeuCuoDAO					 = new AuxConDeuCuoDAO();
        this.auxConvenioDAO						 = new AuxConvenioDAO();
        this.reclamoDAO						     = new ReclamoDAO();        
        this.estadoReclamoDAO					 = new EstadoReclamoDAO(); 
        this.hisCamEstRecDAO					 = new HisCamEstRecDAO(); 
        this.saldoAFavorDAO				    	 = new SaldoAFavorDAO(); 
        this.estSalAFavDAO                       = new EstSalAFavDAO();
        this.tipoOrigenDAO                       = new TipoOrigenDAO();
        this.tipoSalAFavDAO                      = new TipoSalAFavDAO();
        this.parCueBanDAO                        = new ParCueBanDAO();
        this.cuentaBancoDAO                      = new CuentaBancoDAO();
        this.tipCueBanDAO                        = new TipCueBanDAO();
        this.clasificadorDAO					 = new ClasificadorDAO();
        this.nodoDAO							 = new NodoDAO();
        this.relClaDAO							 = new RelClaDAO();
        this.relPartidaDAO						 = new RelPartidaDAO();
        this.tipOriMovDAO						 = new TipOriMovDAO();
        this.estDiaParDAO						 = new EstDiaParDAO();
        this.hisEstDiaParDAO					 = new HisEstDiaParDAO();
        this.diarioPartidaDAO					 = new DiarioPartidaDAO();
        this.otrIngTesDAO						 = new OtrIngTesDAO();
        this.estOtrIngTesDAO					 = new EstOtrIngTesDAO();
        this.hisEstOtrIngTesDAO					 = new HisEstOtrIngTesDAO();
        this.otrIngTesParDAO					 = new OtrIngTesParDAO();
        this.otrIngTesRecConDAO					 = new OtrIngTesRecConDAO();
        this.caja7DAO				        	 = new Caja7DAO();
        this.balanceDAO							 = new BalanceDAO();
        this.impParDAO							 = new ImpParDAO();
        this.archivoDAO							 = new ArchivoDAO();
        this.tipoArcDAO							 = new TipoArcDAO();
        this.estadoArcDAO						 = new EstadoArcDAO();
        this.tranArcDAO							 = new TranArcDAO();       
        this.folioDAO							 = new FolioDAO();
        this.folComDAO							 = new FolComDAO();
        this.folDiaCobDAO						 = new FolDiaCobDAO();
        this.folDiaCobColDAO					 = new FolDiaCobColDAO();
        this.tipoCobDAO							 = new TipoCobDAO();
        this.estadoFolDAO						 = new EstadoFolDAO();
        this.tipoComDAO                			 = new TipoComDAO();
        this.auxDeuMdfDAO                		 = new AuxDeuMdfDAO();
        this.compensacionDAO				 	 = new CompensacionDAO();
        this.comDeuDAO							 = new ComDeuDAO();
        this.tipoComDeuDAO						 = new TipoComDeuDAO();
        this.estadoComDAO						 = new EstadoComDAO();
        this.hisEstComDAO						 = new HisEstComDAO();
        this.reingresoDAO						 = new ReingresoDAO();
        this.caja69DAO							 = new Caja69DAO();
        this.tranBalDAO							 = new TranBalDAO();
        this.aseDelDAO							 = new AseDelDAO();
        this.tranDelDAO							 = new TranDelDAO();
        this.indetBalDAO						 = new IndetBalDAO();
        this.saldoBalDAO						 = new SaldoBalDAO();
        this.auxCaja7DAO						 = new AuxCaja7DAO();
        this.leyParAcuDAO						 = new LeyParAcuDAO();
        this.estadoEnvioDAO						 = new EstadoEnvioDAO();
        this.envioOsirisDAO						 = new EnvioOsirisDAO();
        this.mulatorJDBCDAO						 = new MulatorJDBCDAO();
        this.tipoOperacionDAO					 = new TipoOperacionDAO();
        this.cierreBancoDAO						 = new CierreBancoDAO();
        this.estTranAfipDAO						 = new EstTranAfipDAO();
        this.tranAfipDAO						 = new TranAfipDAO();
        this.logTranAfipDAO						 = new LogTranAfipDAO();
        this.estDetPagoDAO						 = new EstDetPagoDAO();
        this.detallePagoDAO						 = new DetallePagoDAO();
        this.logDetallePagoDAO				     = new LogDetallePagoDAO();
        this.auxImpRecDAO						 = new AuxImpRecDAO();
        this.estDetDJDAO						 = new EstDetDJDAO();
        this.detalleDJDAO						 = new DetalleDJDAO();
        this.logDetalleDJDAO					 = new LogDetalleDJDAO();
        this.notaImptoDAO						 = new NotaImptoDAO();
        this.cierreSucursalDAO					 = new CierreSucursalDAO();
        this.novedadEnvioDAO					 = new NovedadEnvioDAO();
        this.conciliacionDAO					 = new ConciliacionDAO();
        this.notaConcDAO						 = new NotaConcDAO();
        this.movBanDAO						 	 = new MovBanDAO();
        this.movBanDetDAO						 = new MovBanDetDAO();
        this.envNotOblDAO						 = new EnvNotOblDAO();
        this.indeterminadoDAO					 = new IndeterminadoDAO();
        this.dupliceDAO							 = new DupliceDAO();
        this.reingIndetDAO						 = new ReingIndetDAO();
        this.indetAuditDAO						 = new IndetAuditDAO();
    }

    public static IndeterminadoJDBCDAO getIndeterminadoJDBCDAO() {
        return INSTANCE.indeterminadoJDBCDAO;
    }
    
    public static SistemaDAO getSistemaDAO(){
    	return INSTANCE.sistemaDAO;
    }

    public static PartidaDAO getPartidaDAO() {
    	return INSTANCE.partidaDAO;
    }
    
    public static TipoImporteDAO getTipoImporteDAO() {
    	return INSTANCE.tipoImporteDAO;
    }

    public static DisParDAO getDisParDAO() {
		return INSTANCE.disParDAO;
	}

	public static DisParDetDAO getDisParDetDAO() {
		return INSTANCE.disParDetDAO;
	}

	public static DisParPlaDAO getDisParPlaDAO() {
		return INSTANCE.disParPlaDAO;
	}

	public static DisParRecDAO getDisParRecDAO() {
		return INSTANCE.disParRecDAO;
	}
	
	public static ImpSelDAO getImpSelDAO() {
		return INSTANCE.impSelDAO;
	}

	public static SelladoDAO getSelladoDAO() {
		return INSTANCE.selladoDAO;
	}
	
	public static TipoSelladoDAO getTipoSelladoDAO() {
		return INSTANCE.tipoSelladoDAO;
	}
    
	public static AccionDAO getAccionDAO() {
		return INSTANCE.accionDAO;
	}

	public static AccionSelladoDAO getAccionSelladoDAO() {
		return INSTANCE.accionSelladoDAO;
	}

    public static CanalDAO getCanalDAO() {
		return INSTANCE.canalDAO;
	}
    
    public static AsentamientoDAO getAsentamientoDAO() {
		return INSTANCE.asentamientoDAO;
	}

    public static EjercicioDAO getEjercicioDAO() {
		return INSTANCE.ejercicioDAO;
	}
    
    public static EstEjercicioDAO getEstEjercicioDAO() {
		return INSTANCE.estEjercicioDAO;
	}
        
    public static TransaccionDAO getTransaccionDAO() {
		return INSTANCE.transaccionDAO;
	}

    public static AuxPagDeuDAO getAuxPagDeuDAO() {
		return INSTANCE.auxPagDeuDAO;
	}

    public static AuxRecaudadoDAO getAuxRecaudadoDAO() {
		return INSTANCE.auxRecaudadoDAO;
	}    
    
    public static SinSalAFavDAO getSinSalAFavDAO() {
		return INSTANCE.sinSalAFavDAO;
	}    
    
    public static ParSelDAO getParSelDAO() {
		return INSTANCE.parSelDAO;
	}

    public static TipoDistribDAO getTipoDistribDAO() {
		return INSTANCE.tipoDistribDAO;
	}
    
	public static ToleranciaDAO getToleranciaDAO() {
		return INSTANCE.toleranciaDAO;
	}
	
	public static SinIndetDAO getSinIndetDAO() {
		return INSTANCE.sinIndetDAO;
	} 
	
	public static SinPartidaDAO getSinPartidaDAO() {
		return INSTANCE.sinPartidaDAO;
	}
	
	public static AuxSelladoDAO getAuxSelladoDAO() {
		return INSTANCE.auxSelladoDAO;
	}
	
	public static TipoIndetDAO getTipoIndetDAO() {
		return INSTANCE.tipoIndetDAO;
	}

	public static AuxPagCuoDAO getAuxPagCuoDAO() {
		return INSTANCE.auxPagCuoDAO;
	}

	public static AuxConDeuDAO getAuxConDeuDAO() {
		return INSTANCE.auxConDeuDAO;
	}

	public static AuxConDeuCuoDAO getAuxConDeuCuoDAO() {
		return INSTANCE.auxConDeuCuoDAO;
	}
	
	public static AuxConvenioDAO getAuxConvenioDAO() {
		return INSTANCE.auxConvenioDAO;
	}
	
	public static ReclamoDAO getReclamoDAO() {
		return INSTANCE.reclamoDAO;
	}
	
	public static EstadoReclamoDAO getEstadoReclamoDAO() {
		return INSTANCE.estadoReclamoDAO;
	}
	
	public static HisCamEstRecDAO getHisCamEstRecDAO() {
		return INSTANCE.hisCamEstRecDAO;
	}
	public static SaldoAFavorDAO getSaldoAFavorDAO() {
		return INSTANCE.saldoAFavorDAO;
	}
	public static EstSalAFavDAO getEstSalAFavDAO() {
		return INSTANCE.estSalAFavDAO;
	}
	public static TipoOrigenDAO getTipoOrigenDAO() {
		return INSTANCE.tipoOrigenDAO;
	}
	public static TipoSalAFavDAO getTipoSalAFavDAO() {
		return INSTANCE.tipoSalAFavDAO;
	}
	
	public static TipCueBanDAO getTipCueBanDAO() {
		return INSTANCE.tipCueBanDAO;
	}
	public static CuentaBancoDAO getCuentaBancoDAO() {
		return INSTANCE.cuentaBancoDAO;
	}
	public static ParCueBanDAO getParCueBanDAO() {
		return INSTANCE.parCueBanDAO;
	} 
	
	public static ClasificadorDAO getClasificadorDAO() {
		return INSTANCE.clasificadorDAO;
	}
	
	public static NodoDAO getNodoDAO() {
		return INSTANCE.nodoDAO;
	}
	
	public static RelClaDAO getRelClaDAO() {
		return INSTANCE.relClaDAO;
	}
	
	public static RelPartidaDAO getRelPartidaDAO() {
		return INSTANCE.relPartidaDAO;
	}
	
	public static TipOriMovDAO getTipOriMovDAO() {
		return INSTANCE.tipOriMovDAO;
	}
	
	public static EstDiaParDAO getEstDiaParDAO() {
		return INSTANCE.estDiaParDAO;
	}
	
	public static HisEstDiaParDAO getHisEstDiaParDAO(){
		return INSTANCE.hisEstDiaParDAO;
	}
	
	public static DiarioPartidaDAO getDiarioPartidaDAO(){
		return INSTANCE.diarioPartidaDAO;
	}
	
	public static OtrIngTesDAO getOtrIngTesDAO(){
		return INSTANCE.otrIngTesDAO;
	}
	
	public static EstOtrIngTesDAO getEstOtrIngTesDAO(){
		return INSTANCE.estOtrIngTesDAO;
	}
	
	public static HisEstOtrIngTesDAO getHisEstOtrIngTesDAO(){
		return INSTANCE.hisEstOtrIngTesDAO;
	}
	
	public static OtrIngTesParDAO getOtrIngTesParDAO(){
		return INSTANCE.otrIngTesParDAO;
	}
	
	public static OtrIngTesRecConDAO getOtrIngTesRecConDAO(){
		return INSTANCE.otrIngTesRecConDAO;
	}
	
	public static Caja7DAO getCaja7DAO(){
		return INSTANCE.caja7DAO;
	}
	
	public static BalanceDAO getBalanceDAO(){
		return INSTANCE.balanceDAO;
	}
	
	public static ImpParDAO getImpParDAO(){
		return INSTANCE.impParDAO;
	}
	
	public static ArchivoDAO getArchivoDAO(){
		return INSTANCE.archivoDAO;
	}
	
	public static TipoArcDAO getTipoArcDAO(){
		return INSTANCE.tipoArcDAO;
	}
	
	public static EstadoArcDAO getEstadoArcDAO(){
		return INSTANCE.estadoArcDAO;
	}
	
	public static TranArcDAO getTranArcDAO(){
		return INSTANCE.tranArcDAO;
	}
	
	public static FolioDAO getFolioDAO(){
		return INSTANCE.folioDAO;
	}
	
	public static FolComDAO getFolComDAO(){
		return INSTANCE.folComDAO;
	}
	
	public static FolDiaCobDAO getFolDiaCobDAO(){
		return INSTANCE.folDiaCobDAO;
	}
	
	public static FolDiaCobColDAO getFolDiaCobColDAO(){
		return INSTANCE.folDiaCobColDAO;
	}
	
	public static TipoCobDAO getTipoCobDAO(){
		return INSTANCE.tipoCobDAO;
	}
	
	public static EstadoFolDAO getEstadoFolDAO(){
		return INSTANCE.estadoFolDAO;
	}

	public static TipoComDAO getTipoComDAO() {
        return INSTANCE.tipoComDAO;
    }
	
	public static AuxDeuMdfDAO getAuxDeuMdfDAO() {
        return INSTANCE.auxDeuMdfDAO;
    }
	
	public static CompensacionDAO getCompensacionDAO() {
        return INSTANCE.compensacionDAO;
    }

	public static ComDeuDAO getComDeuDAO(){
		return INSTANCE.comDeuDAO;
	}
	
	public static TipoComDeuDAO getTipoComDeuDAO(){
		return INSTANCE.tipoComDeuDAO;
	}
	
	public static EstadoComDAO getEstadoComDAO(){
		return INSTANCE.estadoComDAO;
	}
	
	public static HisEstComDAO getHisEstComDAO(){
		return INSTANCE.hisEstComDAO;
	}
	
	public static ReingresoDAO getReingresoDAO(){
		return INSTANCE.reingresoDAO;
	}

	public static Caja69DAO getCaja69DAO(){
		return INSTANCE.caja69DAO;
	}
	
	public static TranBalDAO getTranBalDAO(){
		return INSTANCE.tranBalDAO;
	}
	
	public static AseDelDAO getAseDelDAO(){
		return INSTANCE.aseDelDAO;
	}
	
	public static TranDelDAO getTranDelDAO(){
		return INSTANCE.tranDelDAO;
	}

	public static SaldoBalDAO getSaldoBalDAO(){
		return INSTANCE.saldoBalDAO;
	}
	
	public static IndetBalDAO getIndetBalDAO(){
		return INSTANCE.indetBalDAO;
	}
	
	public static AuxCaja7DAO getAuxCaja7DAO(){
		return INSTANCE.auxCaja7DAO;
	}
	
	public static LeyParAcuDAO getLeyParAcuDAO(){
		return INSTANCE.leyParAcuDAO;
	}
	
	public static EstadoEnvioDAO getEstadoEnvioDAO(){
    	return INSTANCE.estadoEnvioDAO;
    }
    
    public static EnvioOsirisDAO getEnvioOsirisDAO(){
    	return INSTANCE.envioOsirisDAO;
    }
    
    public static MulatorJDBCDAO getMulatorJDBCDAO(){
    	return INSTANCE.mulatorJDBCDAO;
    }
    
    public static TipoOperacionDAO getTipoOperacionDAO(){
    	return INSTANCE.tipoOperacionDAO;
    }
    
    public static CierreBancoDAO getCierreBancoDAO(){
    	return INSTANCE.cierreBancoDAO;
    }
    
    public static EstTranAfipDAO getEstTranAfipDAO(){
    	return INSTANCE.estTranAfipDAO;
    }
    
    public static TranAfipDAO getTranAfipDAO(){
    	return INSTANCE.tranAfipDAO;
    }
    
    public static LogTranAfipDAO getLogTranAfipDAO(){
    	return INSTANCE.logTranAfipDAO;
    }
  
    public static EstDetPagoDAO getEstDetPagoDAO(){
    	return INSTANCE.estDetPagoDAO;
    }
    
    public static DetallePagoDAO getDetallePagoDAO(){
    	return INSTANCE.detallePagoDAO;
    }
    
    public static LogDetallePagoDAO getLogDetallePagoDAO(){
    	return INSTANCE.logDetallePagoDAO;
    }
    
    public static AuxImpRecDAO getAuxImpRecDAO(){
    	return INSTANCE.auxImpRecDAO;
    }
    
    public static EstDetDJDAO getEstDetDJDAO(){
    	return INSTANCE.estDetDJDAO;
    }
    
    public static DetalleDJDAO getDetalleDJDAO(){
    	return INSTANCE.detalleDJDAO;
    }
    
    public static LogDetalleDJDAO getLogDetalleDJDAO(){
    	return INSTANCE.logDetalleDJDAO;
    }
    
    public static NotaImptoDAO getNotaImptoDAO(){
    	return INSTANCE.notaImptoDAO;
    }
    
    public static CierreSucursalDAO getCierreSucursalDAO(){
    	return INSTANCE.cierreSucursalDAO;
    }
    
    public static NovedadEnvioDAO getNovedadEnvioDAO(){
    	return INSTANCE.novedadEnvioDAO;
    }
    
    public static ConciliacionDAO getConciliacionDAO(){
    	return INSTANCE.conciliacionDAO;
    }
    
    public static NotaConcDAO getNotaConcDAO(){
    	return INSTANCE.notaConcDAO;
    }
    
    public static MovBanDAO getMovBanDAO(){
    	return INSTANCE.movBanDAO;
    }

    public static MovBanDetDAO getMovBanDetDAO(){
    	return INSTANCE.movBanDetDAO;
    }
    
    public static EnvNotOblDAO getEnvNovOblDAO(){
    	return INSTANCE.envNotOblDAO;
    }
    
    public static IndeterminadoDAO getIndeterminadoDAO() {
		return INSTANCE.indeterminadoDAO;
	}
    
    public static DupliceDAO getDupliceDAO() {
		return INSTANCE.dupliceDAO;
	}
    
    public static ReingIndetDAO getReingIndetDAO() {
		return INSTANCE.reingIndetDAO;
	}
    
    public static IndetAuditDAO getIndetAuditDAO() {
		return INSTANCE.indetAuditDAO;
	}
}
