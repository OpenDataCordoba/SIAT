//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.cyq.buss.dao.CyqDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente a PagoPrivDeu - Pago de Privilegio Deuda
 * 
 * @author tecso
 */
@Entity
@Table(name = "cyq_pagoPrivDeu")
public class PagoPrivDeu extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY) 
    @JoinColumn(name="idPagoPriv") 
    private PagoPriv pagoPriv;

	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idDeudaPriv") 
	private DeudaPrivilegio deudaPrivilegio;

	@Column(name = "importe")
	private Double importe;

	//<#Propiedades#>
	
	// Constructores
	public PagoPrivDeu(){
		super();
		// Seteo de valores default			
	}
	
	public PagoPrivDeu(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static PagoPrivDeu getById(Long id) {
		return (PagoPrivDeu) CyqDAOFactory.getPagoPrivDeuDAO().getById(id);
	}
	
	public static PagoPrivDeu getByIdNull(Long id) {
		return (PagoPrivDeu) CyqDAOFactory.getPagoPrivDeuDAO().getByIdNull(id);
	}
	
	public static List<PagoPrivDeu> getList() {
		return (ArrayList<PagoPrivDeu>) CyqDAOFactory.getPagoPrivDeuDAO().getList();
	}
	
	public static List<PagoPrivDeu> getListActivos() {			
		return (ArrayList<PagoPrivDeu>) CyqDAOFactory.getPagoPrivDeuDAO().getListActiva();
	}
	
	
	// Getters y setters
	public PagoPriv getPagoPriv() {
		return pagoPriv;
	}

	public void setPagoPriv(PagoPriv pagoPriv) {
		this.pagoPriv = pagoPriv;
	}

	public DeudaPrivilegio getDeudaPrivilegio() {
		return deudaPrivilegio;
	}

	public void setDeudaPrivilegio(DeudaPrivilegio deudaPrivilegio) {
		this.deudaPrivilegio = deudaPrivilegio;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
	}

	// Validaciones 
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;
	}

	public boolean validateUpdate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;		
	}

	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
	
		//<#ValidateDelete#>
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
				
		if (hasError()) {
			return false;
		}
		
		return true;
	}

	// Metodos de negocio
	
	

}
