//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del PagoPrivDeu
 * @author tecso
 *
 */
public class PagoPrivDeuVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "pagoPrivDeuVO";
	
	private PagoPrivVO pagoPriv;

	private DeudaPrivilegioVO deudaPrivilegio = new DeudaPrivilegioVO();

	private Double importe;
	
	private String importeView = "";
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public PagoPrivDeuVO() {
		super();
	}

	// Getters y Setters
	public PagoPrivVO getPagoPriv() {
		return pagoPriv;
	}

	public void setPagoPriv(PagoPrivVO pagoPriv) {
		this.pagoPriv = pagoPriv;
	}

	public DeudaPrivilegioVO getDeudaPrivilegio() {
		return deudaPrivilegio;
	}

	public void setDeudaPrivilegio(DeudaPrivilegioVO deudaPrivilegio) {
		this.deudaPrivilegio = deudaPrivilegio;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
		this.importeView = StringUtil.formatDouble(importe);
	}
	

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
	public String getImporteView() {
		return importeView;
	}
	
	public void setImporteView(String importeView) {
		this.importeView = importeView;
	}
	
}
