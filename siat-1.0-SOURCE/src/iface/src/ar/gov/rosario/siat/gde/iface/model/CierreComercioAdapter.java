//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.model.ReportVO;


/**
 * Adapter del CierreComercio
 * 
 * @author tecso
 */
public class CierreComercioAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "cierreComercioAdapterVO";
	
    private CierreComercioVO cierreComercio = new CierreComercioVO();
    private List<LiqAtrValorVO> listAtributoObjImp = new ArrayList<LiqAtrValorVO>();
    private List<LiqDeudaAdminVO> listGestionDeudaAdmin = new ArrayList<LiqDeudaAdminVO>();
    private List<MultaVO> listMulta = new ArrayList<MultaVO>();
    private Long idCuenta;
    private MotivoCierreVO motivoCierre = new MotivoCierreVO();
    private List<MotivoCierreVO> listMotivoCierre = new ArrayList<MotivoCierreVO>();
    private boolean aplicaMulta = false;
    private boolean permiteIniCierreCom = true;
    private boolean cierreDefinitivo=false;
    //private boolean agregarMulta = true;

	// Constructores
    public CierreComercioAdapter(){
    	super(GdeSecurityConstants.CIERRE_COMERCIO);
    }
    
    //  Getters y Setters
	public CierreComercioVO getCierreComercio() {
		return cierreComercio;
	}

	public void setCierreComercio(CierreComercioVO cierreComercioVO) {
		this.cierreComercio = cierreComercioVO;
	}
	
	public List<LiqAtrValorVO> getListAtributoObjImp() {
		return listAtributoObjImp;
	}

	public void setListAtributoObjImp(List<LiqAtrValorVO> listAtributoObjImp) {
		this.listAtributoObjImp = listAtributoObjImp;
	}

	public List<LiqDeudaAdminVO> getListGestionDeudaAdmin() {
		return listGestionDeudaAdmin;
	}

	public void setListGestionDeudaAdmin(List<LiqDeudaAdminVO> listGestionDeudaAdmin) {
		this.listGestionDeudaAdmin = listGestionDeudaAdmin;
	}

	public List<MultaVO> getListMulta() {
		return listMulta;
	}

	public void setListMulta(List<MultaVO> listMulta) {
		this.listMulta = listMulta;
	}


    public Long getIdCuenta() {
		return idCuenta;
	}

	public void setIdCuenta(Long idCuenta) {
		this.idCuenta = idCuenta;
	}

	public String getName(){
		return NAME;
	}
			
	public boolean getCierreDefinitivo() {
		return cierreDefinitivo;
	}

	public void setCierreDefinitivo(boolean cierreDefinitivo) {
		this.cierreDefinitivo = cierreDefinitivo;
	}

	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de CierreComercio");     
		 report.setReportBeanName("CierreComercio");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportCierreComercio = new ReportVO();
		 reportCierreComercio.setReportTitle("Datos del CierreComercio");
		 // carga de datos
	     
	     
		 report.getListReport().add(reportCierreComercio);
	
	}
	
	public MotivoCierreVO getMotivoCierre() {
		return motivoCierre;
	}

	public void setMotivoCierre(MotivoCierreVO motivoCierre) {
		this.motivoCierre = motivoCierre;
	}

    
	public List<MotivoCierreVO> getListMotivoCierre() {
		return listMotivoCierre;
	}

	public void setListMotivoCierre(List<MotivoCierreVO> listMotivoCierre) {
		this.listMotivoCierre = listMotivoCierre;
	}

	public boolean isAplicaMulta() {
		return aplicaMulta;
	}

	public void setAplicaMulta(boolean aplicaMulta) {
		this.aplicaMulta = aplicaMulta;
	}

    public boolean isPermiteIniCierreCom() {
		return permiteIniCierreCom;
	}

	public void setPermiteIniCierreCom(boolean permiteIniCierreCom) {
		this.permiteIniCierreCom = permiteIniCierreCom;
	}
	
	/*public boolean isAgregarMulta() {
		return agregarMulta;
	}

	public void setAgregarMulta(boolean agregarMulta) {
		this.agregarMulta = agregarMulta;
	}
	*/
	
	// View getters
}
