//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.dao;




/**
 * Factory de Recurso DAOs
 * 
 * @author tecso
 * 
 */

public class RecDAOFactory {

    private static final RecDAOFactory INSTANCE = new RecDAOFactory();

    private TipoObraDAO				tipoObraDAO;
    private FormaPagoDAO			formaPagoDAO;
    private TipoContratoDAO			tipoContratoDAO;
    private ContratoDAO			    contratoDAO;
    private EstPlaCuaDAO			estPlaCuaDAO;
    private UsoCdMDAO            	usoCdMDAO;
    private PlaCuaDetDAO            plaCuaDetDAO;
    private PlanillaCuadraDAO       planillaCuadraDAO;
    private HisEstPlaCuaDAO			hisEstPlaCuaDAO;
    private EstPlaCuaDetDAO         estPlaCuaDetDAO;
    private TipPlaCuaDetDAO         tipPlaCuaDetDAO;
    private HisEstadoObraDAO        hisEstadoObraDAO;
    private HisObrRepPlaDAO         hisObrRepPlaDAO;
    private ObraDAO					obraDAO;
    private EstadoObraDAO			estadoObraDAO;
    private ObraFormaPagoDAO		obraFormaPagoDAO;
    private HisCamPlaDAO			hisCamPlaDAO;
    private ObrRepVenDAO			obrRepVenDAO;
    private AnulacionObraDAO		anulacionObraDAO;
    private AnuObrDetDAO			anuObrDetDAO;
    private CatRSDreiDAO			catRSDreiDAO;
    private NovedadRSDAO			novedadRSDAO;
    private TipoTramiteRSDAO		tipoTramiteRSDAO;
    
    
    private RecDAOFactory() {
        super();  
        this.tipoObraDAO		= new TipoObraDAO();
        this.formaPagoDAO		= new FormaPagoDAO();        
        this.tipoContratoDAO	= new TipoContratoDAO();
        this.contratoDAO		= new ContratoDAO();
        this.estPlaCuaDAO		= new EstPlaCuaDAO();
        this.usoCdMDAO			= new UsoCdMDAO();
        this.plaCuaDetDAO		= new PlaCuaDetDAO();
        this.planillaCuadraDAO  = new PlanillaCuadraDAO();
        this.hisEstPlaCuaDAO	= new HisEstPlaCuaDAO();        
        this.estPlaCuaDetDAO	= new EstPlaCuaDetDAO();
        this.tipPlaCuaDetDAO    = new TipPlaCuaDetDAO();
        this.hisEstadoObraDAO   = new HisEstadoObraDAO();
        this.hisObrRepPlaDAO    = new HisObrRepPlaDAO();
        this.obraDAO		    = new ObraDAO();
        this.estadoObraDAO	    = new EstadoObraDAO();
        this.obraFormaPagoDAO	= new ObraFormaPagoDAO();
        this.hisCamPlaDAO       = new HisCamPlaDAO();
        this.obrRepVenDAO		= new ObrRepVenDAO();
        this.anulacionObraDAO	= new AnulacionObraDAO();
        this.anuObrDetDAO		= new AnuObrDetDAO();
        this.catRSDreiDAO		= new CatRSDreiDAO();
        this.novedadRSDAO		= new NovedadRSDAO();
        this.tipoTramiteRSDAO	= new TipoTramiteRSDAO();
        
    }

    public static TipoObraDAO getTipoObraDAO() {
        return INSTANCE.tipoObraDAO;
    }

    public static FormaPagoDAO getFormaPagoDAO() {
        return INSTANCE.formaPagoDAO;
    }

    public static TipoContratoDAO getTipoContratoDAO() {
        return INSTANCE.tipoContratoDAO;
    }

    public static ContratoDAO getContratoDAO() {
        return INSTANCE.contratoDAO;
    }
    
    public static EstPlaCuaDAO getEstPlaCuaDAO() {
        return INSTANCE.estPlaCuaDAO;
    }

    public static UsoCdMDAO getUsoCdMDAO() {
        return INSTANCE.usoCdMDAO;
    }
    
    public static PlaCuaDetDAO getPlaCuaDetDAO() {
        return INSTANCE.plaCuaDetDAO;
    }
    
    public static PlanillaCuadraDAO getPlanillaCuadraDAO() {
        return INSTANCE.planillaCuadraDAO;
    }
    
    public static HisEstPlaCuaDAO getHisEstPlaCuaDAO() {
        return INSTANCE.hisEstPlaCuaDAO;
    }

    public static EstPlaCuaDetDAO getEstPlaCuaDetDAO() {
        return INSTANCE.estPlaCuaDetDAO;
    }
    
    public static TipPlaCuaDetDAO getTipPlaCuaDetDAO() {
        return INSTANCE.tipPlaCuaDetDAO;
    }

    public static HisEstadoObraDAO getHisEstadoObraDAO() {
        return INSTANCE.hisEstadoObraDAO;
    }

    public static HisObrRepPlaDAO getHisObrRepPlaDAO() {
        return INSTANCE.hisObrRepPlaDAO;
    }

    public static ObraDAO getObraDAO() {
        return INSTANCE.obraDAO;
    }
    
    public static EstadoObraDAO getEstadoObraDAO(){
    	return INSTANCE.estadoObraDAO;
    }

    public static ObraFormaPagoDAO getObraFormaPagoDAO(){
    	return INSTANCE.obraFormaPagoDAO;    	
    }
    
    public static HisCamPlaDAO getHisCamPlaDAO(){
    	return INSTANCE.hisCamPlaDAO;
    }
    
    public static ObrRepVenDAO getObrRepVenDAO() {
    	return INSTANCE.obrRepVenDAO;
    }

    public static AnulacionObraDAO getAnulacionObraDAO() {
    	return INSTANCE.anulacionObraDAO;
    }
    
    public static AnuObrDetDAO getAnuObrDetDAO() {
    	return INSTANCE.anuObrDetDAO;
    }
    
    public static CatRSDreiDAO getCatRSDreiDAO(){
    	return INSTANCE.catRSDreiDAO;
    }
    
    public static NovedadRSDAO getNovedadRSDAO(){
    	return INSTANCE.novedadRSDAO;
    }
    
    public static TipoTramiteRSDAO getTipoTramiteRSDAO(){
    	return INSTANCE.tipoTramiteRSDAO;
    }
    
    
    
}
