//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.buss.dao;


/**
 * Factory de <Nombre Largo Modulo> DAOs
 * 
 * @author tecso
 * 
 */
public class CasDAOFactory {

    private static final CasDAOFactory INSTANCE = new CasDAOFactory();


    private TipoSolicitudDAO tipoSolicitudDAO;
    private EstSolicitudDAO estSolicitudDAO;
    private SolicitudDAO solicitudDAO;    
    private UsoExpedienteDAO usoExpedienteDAO; 
    private AccionExpDAO accionExpDAO;
    private SistemaOrigenDAO sistemaOrigenDAO;
    private SolicitudCUITDAO solicitudCUITDAO;
    private AreaSolicitudDAO areaSolicitudDAO;
	private SolPendDAO solPendDAO;
	
    private CasDAOFactory() {
        this.tipoSolicitudDAO 	= new TipoSolicitudDAO();
        this.estSolicitudDAO  	= new EstSolicitudDAO();
        this.solicitudDAO  		= new SolicitudDAO();
        this.usoExpedienteDAO 	= new UsoExpedienteDAO();
        this.accionExpDAO 		= new AccionExpDAO();
        this.sistemaOrigenDAO 	= new SistemaOrigenDAO();
        this.solicitudCUITDAO 	= new SolicitudCUITDAO();
        this.areaSolicitudDAO   = new AreaSolicitudDAO();
        this.solPendDAO         = new SolPendDAO();
    }

    public static TipoSolicitudDAO getTipoSolicitudDAO() {
        return INSTANCE.tipoSolicitudDAO;
    }
    
    public static EstSolicitudDAO getEstSolicitudDAO() {
        return INSTANCE.estSolicitudDAO;
    }
    
    public static SolicitudDAO getSolicitudDAO() {
        return INSTANCE.solicitudDAO;
    }    
    
    public static UsoExpedienteDAO getUsoExpedienteDAO(){
    	return INSTANCE.usoExpedienteDAO;
    }
    
    public static AccionExpDAO getAccionExpDAO(){
    	return INSTANCE.accionExpDAO;
    }
    
    public static SistemaOrigenDAO getSistemaOrigenDAO(){
    	return INSTANCE.sistemaOrigenDAO;
    }

	public static SolicitudCUITDAO getSolicitudCUITDAO() {
		return INSTANCE.solicitudCUITDAO;
	}
    
	public static AreaSolicitudDAO getAreaSolicitudDAO() {
		return INSTANCE.areaSolicitudDAO;
	}
	
    public static SolPendDAO getSolPendDAO() {
        return INSTANCE.solPendDAO;
    }
}
