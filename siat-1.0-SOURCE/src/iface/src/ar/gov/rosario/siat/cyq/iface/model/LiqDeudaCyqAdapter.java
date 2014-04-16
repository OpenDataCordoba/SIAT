//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.cyq.iface.util.CyqSecurityConstants;
import ar.gov.rosario.siat.gde.iface.model.LiqConvenioCuentaAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqConvenioVO;

/**
 * Adapter del LiqDeuda
 * 
 * @author tecso
 */
public class LiqDeudaCyqAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "liqDeudaCyqAdapterVO";
	

	// Propiedades para mostrar
	private Double total=0D;	
    private String fechaAcentamiento = "";
    private ProcedimientoVO procedimiento = new ProcedimientoVO(-1);
    private String[] listIdDeudaSelected;
    
    private Double totalGeneral=0D;
    private Double totalEspecial=0D;
    private Double totalQuirografario=0D;
    private Double totalSaldo=0D;
    
    private List<LiqConvenioVO> listConvenio = new ArrayList<LiqConvenioVO>();
    private List<LiqConvenioCuentaAdapter> listConvenioCuentaAdapter = new ArrayList<LiqConvenioCuentaAdapter>();
    private List<LiqDeudaPrivilegioVO> listDeuda = new ArrayList<LiqDeudaPrivilegioVO>();
    private List<PagoPrivVO> listPago = new ArrayList<PagoPrivVO>();
    
 	//Flags para controlar la visibilidad de las columnas Seleccionar, Ver y Solicitar. 
	//Por defecto, se muestran todas.
	private Boolean mostrarColumnaSeleccionar = true;
	private Boolean mostrarColumnaVer = true;
    
	// Banderas para mostrar o no los checkBox que permiten seleccionar todo un bloque.
	private Boolean mostrarChkAll = false;
	
	
    private boolean verConvenioEnabled = false; 	// Poder ver detalles de convenios

	
	// Constructores
    public LiqDeudaCyqAdapter(){
    	super(CyqSecurityConstants.LIQ_DEUDACYQ);
    }

    //  Getters y Setters
	public String getFechaAcentamiento() {
		return fechaAcentamiento;
	}
	public void setFechaAcentamiento(String fechaAcentamiento) {
		this.fechaAcentamiento = fechaAcentamiento;
	}
	
	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public ProcedimientoVO getProcedimiento() {
		return procedimiento;
	}

	public void setProcedimiento(ProcedimientoVO procedimiento) {
		this.procedimiento = procedimiento;
	}

	public Boolean getMostrarColumnaSeleccionar() {
		return mostrarColumnaSeleccionar;
	}

	public void setMostrarColumnaSeleccionar(Boolean mostrarColumnaSeleccionar) {
		this.mostrarColumnaSeleccionar = mostrarColumnaSeleccionar;
	}

	public Boolean getMostrarColumnaVer() {
		return mostrarColumnaVer;
	}

	public void setMostrarColumnaVer(Boolean mostrarColumnaVer) {
		this.mostrarColumnaVer = mostrarColumnaVer;
	}
	
	public Double getTotalGeneral() {
		return totalGeneral;
	}
	public void setTotalGeneral(Double totalGeneral) {
		this.totalGeneral = totalGeneral;
	}
	
	public Double getTotalEspecial() {
		return totalEspecial;
	}
	public void setTotalEspecial(Double totalEspecial) {
		this.totalEspecial = totalEspecial;
	}
	
	public Double getTotalQuirografario() {
		return totalQuirografario;
	}
	public void setTotalQuirografario(Double totalQuirografario) {
		this.totalQuirografario = totalQuirografario;
	}

	public List<PagoPrivVO> getListPago() {
		return listPago;
	}
	public void setListPago(List<PagoPrivVO> listPago) {
		this.listPago = listPago;
	}
	
	public Double getTotalSaldo() {
		return totalSaldo;
	}
	public void setTotalSaldo(Double totalSaldo) {
		this.totalSaldo = totalSaldo;
	}
	
	public boolean isVerConvenioEnabled() {
		return verConvenioEnabled;
	}
	public void setVerConvenioEnabled(boolean verConvenioEnabled) {
		this.verConvenioEnabled = verConvenioEnabled;
	}
	
	// view getters


	// Metodos de calculo
	/**
	 * Calcula los totales por privilegio y el saldo total.
	 */
	public Double calcularTotales(){
		
		Double total = 0D;
		
		// Totalizacion de deuda en CyQ
		for (LiqDeudaPrivilegioVO deudaPrivilegio:this.getListDeuda()){
			
			if (deudaPrivilegio.getIdTipoPrivilegio() != null && deudaPrivilegio.getIdTipoPrivilegio().intValue() == 1)
				totalGeneral += new Double(deudaPrivilegio.getImporte());
			
			if (deudaPrivilegio.getIdTipoPrivilegio() != null && deudaPrivilegio.getIdTipoPrivilegio().intValue() == 2)
				totalEspecial += new Double(deudaPrivilegio.getImporte());
			
			if (deudaPrivilegio.getIdTipoPrivilegio() != null && deudaPrivilegio.getIdTipoPrivilegio().intValue() == 3)
				totalQuirografario += new Double(deudaPrivilegio.getImporte());
			
			totalSaldo +=  new Double(deudaPrivilegio.getSaldo());
		}		
		
			
		total += totalGeneral + totalEspecial + totalQuirografario;
			
		return total;
	}
	
	public String[] getListIdDeudaSelected() {
		return listIdDeudaSelected;
	}
	public void setListIdDeudaSelected(String[] listIdDeudaSelected) {
		this.listIdDeudaSelected = listIdDeudaSelected;
	}

	public List<LiqConvenioCuentaAdapter> getListConvenioCuentaAdapter() {
		return listConvenioCuentaAdapter;
	}

	public void setListConvenioCuentaAdapter(
			List<LiqConvenioCuentaAdapter> listConvenioCuentaAdapter) {
		this.listConvenioCuentaAdapter = listConvenioCuentaAdapter;
	}

	public List<LiqDeudaPrivilegioVO> getListDeuda() {
		return listDeuda;
	}

	public void setListDeuda(List<LiqDeudaPrivilegioVO> listDeuda) {
		this.listDeuda = listDeuda;
	}

	public Boolean getMostrarChkAll() {
		return mostrarChkAll;
	}

	public void setMostrarChkAll(Boolean mostrarChkAll) {
		this.mostrarChkAll = mostrarChkAll;
	}

	public List<LiqConvenioVO> getListConvenio() {
		return listConvenio;
	}
	public void setListConvenio(List<LiqConvenioVO> listConvenio) {
		this.listConvenio = listConvenio;
	}

	// Permisos para PagoPriv
	public String getVerPagoPrivEnabled() {
		return SiatBussImageModel.hasEnabledFlag(CyqSecurityConstants.ABM_PAGOPRIV, BaseSecurityConstants.VER);
	}
	
	public String getModificarPagoPrivEnabled() {
		return SiatBussImageModel.hasEnabledFlag(CyqSecurityConstants.ABM_PAGOPRIV, BaseSecurityConstants.MODIFICAR);
	}
	
}