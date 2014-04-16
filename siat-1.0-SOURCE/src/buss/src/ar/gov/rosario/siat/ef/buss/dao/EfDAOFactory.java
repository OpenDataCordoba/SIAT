//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.dao;

import ar.gov.rosario.siat.pad.buss.dao.InspectorDAO;
import ar.gov.rosario.siat.pad.buss.dao.SupervisorDAO;

/**
 * Factory de <Nombre Largo Modulo> DAOs
 * 
 * @author tecso
 * 
 */
public class EfDAOFactory {

    private static final EfDAOFactory INSTANCE = new EfDAOFactory();
    
    private InspectorDAO			inspectorDAO;
    private EstadoPlanFisDAO		estadoPlanFisDAO;
    private PlanFiscalDAO			planFiscalDAO;
    private InvestigadorDAO 		investigadorDAO;
    private OpeInvDAO				opeInvDAO;
    private HisEstOpeInvDAO			hisEstOpeInvDAO;
    private EstOpeInvDAO			estOpeInvDAO;
    private OpeInvConDAO			opeInvConDAO;
    private EstadoOpeInvConDAO		estadoOpeInvConDAO;
    private HisEstOpeInvConDAO		hisEstOpeInvConDAO;
    private OpeInvBusDAO			opeInvBusDAO;		
    private OpeInvConCueDAO			opeInvConCueDAO;
    private EstadoActaDAO			estadoActaDAO;
    private ActaInvDAO				actaInvDAO;
    private TipoPeriodoDAO			tipoPeriodoDAO;
    private TipoOrdenDAO			tipoOrdenDAO;
    private EstadoOrdenDAO			estadoOrdenDAO;
    private OrigenOrdenDAO			origenOrdenDAO;
    private OrdenControlDAO			ordenControlDAO;
    private OrdConCueDAO			ordConCueDAO;
    private HisEstOrdConDAO			hisEstOrdConDAO;
    private SupervisorDAO			supervisorDAO;
    private OrdenControlFisDAO		ordenControlFisDAO;
    private InsSupDAO               insSupDAO;
    private PeriodoOrdenDAO			periodoOrdenDAO;
	private FuenteInfoDAO			fuenteInfoDAO;
    private TipoActaDAO				tipoActaDAO;
    private ActaDAO					actaDAO;
    private TipoDocDAO				tipoDocDAO;
    private DocumentacionDAO		documentacionDAO;
    private OrdConDocDAO			ordConDocDAO;
    private InicioInvDAO			inicioInvDAO;
    private PlaFueDatDAO			plaFueDatDAO;
    private PlaFueDatColDAO			plaFueDatColDAO;
    private PlaFueDatDetDAO			plaFueDatDetDAO;
    private ComparacionDAO			comparacionDAO;
    private CompFuenteDAO			compFuenteDAO;
    private PlaFueDatComDAO			plaFueDatComDAO;
    private CompFuenteColDAO		compFuenteColDAO;
    private CompFuenteResDAO		compFuenteResDAO;
    private OrdConBasImpDAO			ordConBasImpDAO;
    private DetAjuDetDAO			detAjuDetDAO;
    private DetAjuDAO				detAjuDAO;
    private AliComFueColDAO			aliComFueColDAO;
    private MesaEntradaDAO			mesaEntradaDAO;
    private AproOrdConDAO			aproOrdConDAO;
    private DocSopDAO				docSopDAO;
    private DetAjuDocSopDAO			detAjuDocSopDAO;
    private ComAjuDAO				comAjuDAO;
    private ComAjuDetDAO			comAjuDetDAO;
    
    private EfDAOFactory() {
        super();  
        this.inspectorDAO			  = new InspectorDAO();
        this.estadoPlanFisDAO		  = new EstadoPlanFisDAO();
        this.planFiscalDAO			  = new PlanFiscalDAO();
        this.investigadorDAO		  = new InvestigadorDAO();
        this.opeInvDAO				  = new OpeInvDAO();
        this.estOpeInvDAO			  = new EstOpeInvDAO();
        this.hisEstOpeInvDAO		  = new HisEstOpeInvDAO();
        this.opeInvConDAO			  = new OpeInvConDAO();
        this.estadoOpeInvConDAO		  = new EstadoOpeInvConDAO();
        this.hisEstOpeInvConDAO		  = new HisEstOpeInvConDAO();
        this.opeInvBusDAO			  = new OpeInvBusDAO();
        this.opeInvConCueDAO          = new OpeInvConCueDAO();
        this.estadoActaDAO			  = new EstadoActaDAO();
        this.actaInvDAO				  = new ActaInvDAO();
        this.tipoPeriodoDAO			  = new TipoPeriodoDAO();
        this.tipoOrdenDAO			  = new TipoOrdenDAO();
        this.estadoOrdenDAO			  = new EstadoOrdenDAO();
        this.origenOrdenDAO			  = new OrigenOrdenDAO();
        this.ordenControlDAO		  = new OrdenControlDAO();
        this.ordConCueDAO			  = new OrdConCueDAO();
        this.hisEstOrdConDAO		  = new HisEstOrdConDAO();
        this.supervisorDAO            = new SupervisorDAO();
        this.ordenControlFisDAO		  = new OrdenControlFisDAO();
        this.insSupDAO		          = new InsSupDAO();
        this.periodoOrdenDAO		  = new PeriodoOrdenDAO();
        this.fuenteInfoDAO		       = new FuenteInfoDAO();
        this.tipoActaDAO			  = new TipoActaDAO();
        this.actaDAO			  	  = new ActaDAO();
        this.tipoDocDAO				  = new TipoDocDAO();
        this.documentacionDAO		  = new DocumentacionDAO();
        this.ordConDocDAO			  = new OrdConDocDAO();	
        this.inicioInvDAO			  = new InicioInvDAO();
        this.plaFueDatDAO			  = new PlaFueDatDAO();
        this.plaFueDatColDAO		  = new PlaFueDatColDAO();
        this.plaFueDatDetDAO		  = new PlaFueDatDetDAO();
        this.comparacionDAO			  = new ComparacionDAO();
        this.compFuenteDAO			  = new CompFuenteDAO();
        this.plaFueDatComDAO		  = new PlaFueDatComDAO();
        this.compFuenteColDAO		  = new CompFuenteColDAO();
        this.compFuenteResDAO		  = new CompFuenteResDAO();
        this.ordConBasImpDAO		  = new OrdConBasImpDAO();
        this.detAjuDetDAO			  = new DetAjuDetDAO();
        this.detAjuDAO			  	  = new DetAjuDAO();
        this.aliComFueColDAO		  = new AliComFueColDAO();
        this.mesaEntradaDAO			  = new MesaEntradaDAO();
        this.aproOrdConDAO			  = new AproOrdConDAO();
        this.docSopDAO				  = new DocSopDAO();
        this.detAjuDocSopDAO		  = new DetAjuDocSopDAO();
        this.comAjuDAO				  = new ComAjuDAO();
        this.comAjuDetDAO			  = new ComAjuDetDAO();
    }

    public static InspectorDAO getInspectorDAO(){
    	return INSTANCE.inspectorDAO;
    }

	public static EstadoPlanFisDAO getEstadoPlanFisDAO() {
		return INSTANCE.estadoPlanFisDAO;
	}

	public static PlanFiscalDAO getPlanFiscalDAO() {
		return INSTANCE.planFiscalDAO;
	}
	
	public static InvestigadorDAO getInvestigadorDAO() {
		return INSTANCE.investigadorDAO;
	}

	public static OpeInvDAO getOpeInvDAO() {
		return INSTANCE.opeInvDAO;
	}
	
	public static HisEstOpeInvDAO getHisEstOpeInvDAO() {
		return INSTANCE.hisEstOpeInvDAO;
	}
	
	public static EstOpeInvDAO getEstOpeInvDAO() {
		return INSTANCE.estOpeInvDAO;
	}
	
	public static OpeInvConDAO getOpeInvConDAO() {
		return INSTANCE.opeInvConDAO;
	}
	
	public static EstadoOpeInvConDAO getEstadoOpeInvConDAO() {
		return INSTANCE.estadoOpeInvConDAO;
	}

	public static HisEstOpeInvConDAO getHisEstOpeInvConDAO() {
		return INSTANCE.hisEstOpeInvConDAO;
	}
	
	public static OpeInvBusDAO getOpeInvBusDAO(){
		return INSTANCE.opeInvBusDAO;
	}
	
	public static OpeInvConCueDAO getOpeInvConCueDAO(){
		return INSTANCE.opeInvConCueDAO;
	}

	public static EstadoActaDAO getEstadoActaDAO() {
		return INSTANCE.estadoActaDAO;
	}

	public static ActaInvDAO getActaInvDAO() {
		return INSTANCE.actaInvDAO;
	}

	public static TipoPeriodoDAO getTipoPeriodoDAO() {
		return INSTANCE.tipoPeriodoDAO;
	}

	public static TipoOrdenDAO getTipoOrdenDAO() {
		return INSTANCE.tipoOrdenDAO;
	}

	public static EstadoOrdenDAO getEstadoOrdenDAO() {
		return INSTANCE.estadoOrdenDAO;
	}

	public static OrigenOrdenDAO getOrigenOrdenDAO() {
		return INSTANCE.origenOrdenDAO;
	}

	public static OrdenControlDAO getOrdenControlDAO() {
		return INSTANCE.ordenControlDAO;
	}

	
	public static OrdConCueDAO getOrdConCueDAO() {
		return INSTANCE.ordConCueDAO;
	}

	public static HisEstOrdConDAO getHisEstOrdConDAO() {
		return INSTANCE.hisEstOrdConDAO;
	}
	
	public static SupervisorDAO getSupervisorDAO() {
		return INSTANCE.supervisorDAO;
	}
	
	public static OrdenControlFisDAO getOrdenControlFisDAO(){
		return INSTANCE.ordenControlFisDAO;
	}
	
	public static InsSupDAO getInsSupDAO(){
		return INSTANCE.insSupDAO;
	}

	
	public static PeriodoOrdenDAO getPeriodoOrdenDAO() {
		return INSTANCE.periodoOrdenDAO;
	}

	public static FuenteInfoDAO getFuenteInfoDAO() {
		return INSTANCE.fuenteInfoDAO;
	}
	public static TipoActaDAO getTipoActaDAO(){
		return INSTANCE.tipoActaDAO;
	}
	
	public static ActaDAO getActaDAO(){
		return INSTANCE.actaDAO;
	}
	

	public static TipoDocDAO getTipoDocDAO() {
		return INSTANCE.tipoDocDAO;
	}

	public static DocumentacionDAO getDocumentacionDAO(){
		return INSTANCE.documentacionDAO;
	}

	public static OrdConDocDAO getOrdConDocDAO(){
		return INSTANCE.ordConDocDAO;
	}

	public static InicioInvDAO getInicioInvDAO(){
		return INSTANCE.inicioInvDAO;
	}

	public static PlaFueDatDAO getPlaFueDatDAO() {
		return INSTANCE.plaFueDatDAO;
	}
	
	public static PlaFueDatColDAO getPlaFueDatColDAO() {
		return INSTANCE.plaFueDatColDAO;
	}
	
	public static PlaFueDatDetDAO getPlaFueDatDetDAO() {
		return INSTANCE.plaFueDatDetDAO;
	}
	
	public static ComparacionDAO getComparacionDAO(){
		return INSTANCE.comparacionDAO;
	}

	public static CompFuenteDAO getCompFuenteDAO(){
		return INSTANCE.compFuenteDAO;
	}

	public static PlaFueDatComDAO getPlaFueDatComDAO() {
		return INSTANCE.plaFueDatComDAO;
	}

	public static CompFuenteColDAO getCompFuenteColDAO(){
		return INSTANCE.compFuenteColDAO;
	}

	public static CompFuenteResDAO getCompFuenteResDAO(){
		return INSTANCE.compFuenteResDAO;
	}

	public static OrdConBasImpDAO getOrdConBasImpDAO(){
		return INSTANCE.ordConBasImpDAO; 
	}

	public static DetAjuDetDAO getDetAjuDetDAO(){
		return INSTANCE.detAjuDetDAO;
	}

	public static DetAjuDAO getDetAjuDAO(){
		return INSTANCE.detAjuDAO;
	}

	public static AliComFueColDAO getAliComFueColDAO(){
		return INSTANCE.aliComFueColDAO;
	}

	public static MesaEntradaDAO getMesaEntradaDAO(){
		return INSTANCE.mesaEntradaDAO;
	}

	public static AproOrdConDAO getAproOrdConDAO(){
		return INSTANCE.aproOrdConDAO;
	}

	public static DocSopDAO getDocSopDAO(){
		return INSTANCE.docSopDAO;
	}

	public static DetAjuDocSopDAO getDetAjuDocSopDAO(){
		return INSTANCE.detAjuDocSopDAO;
	}

	public static ComAjuDAO getComAjuDAO(){
		return INSTANCE.comAjuDAO;
	}

	public static ComAjuDetDAO getComAjuDetDAO(){
		return INSTANCE.comAjuDetDAO;
	}


}
