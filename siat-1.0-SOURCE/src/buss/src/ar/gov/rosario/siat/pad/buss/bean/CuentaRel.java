//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

	package ar.gov.rosario.siat.pad.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * CuentaRel
 * @author tecso
 *
 */
@Entity
@Table(name = "pad_cuentaRel")
public class CuentaRel extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idCuentaOrigen") 
    private Cuenta cuentaOrigen;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idCuentaDestino") 
    private Cuenta cuentaDestino;
	
	@Column(name = "fechaDesde")
	private Date fechaDesde;

	@Column(name = "fechaHasta")
	private Date fechaHasta;
	
	@Column(name = "esVisible")
	private int esVisible;

	// Constructores
	
	public CuentaRel() {
		super();
	}

	//Getters y Setters

	public Cuenta getCuentaOrigen() {
		return cuentaOrigen;
	}

	public void setCuentaOrigen(Cuenta cuentaOrigen) {
		this.cuentaOrigen = cuentaOrigen;
	}

	public Cuenta getCuentaDestino() {
		return cuentaDestino;
	}

	public void setCuentaDestino(Cuenta cuentaDestino) {
		this.cuentaDestino = cuentaDestino;
	}
	
	public int getEsVisible() {
		return esVisible;
	}

	public void setEsVisible(int esVisible) {
		this.esVisible = esVisible;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	// Metodos de clase	
	public static CuentaRel getById(Long id) {
		return (CuentaRel) PadDAOFactory.getCuentaRelDAO().getById(id);
	}
	
	public static CuentaRel getByIdNull(Long id) {
		return (CuentaRel) PadDAOFactory.getCuentaRelDAO().getByIdNull(id);
	}
	
	public static List<CuentaRel> getList() {
		return (ArrayList<CuentaRel>) PadDAOFactory.getCuentaRelDAO().getList();
	}
	
	public static List<CuentaRel> getListActivos() {			
		return (ArrayList<CuentaRel>) PadDAOFactory.getCuentaRelDAO().getListActiva();
	}
	
	
	// Validaciones
	/**
	 * Valida la creacion
	 * @author
	 */
	public boolean validateCreate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();
		//Validaciones de Negocio
				
		if (hasError()) {
			return false;
		}
		return !hasError();
	}
	
	/**
	 * Valida la actualizacion
	 * @author
	 */
	public boolean validateUpdate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();

		if (hasError()) {
			return false;
		}
		return !hasError();
	}

	/**
	 * Validaciones comunes de creacion y actualizacion
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();

		//Validaciones de Requeridos	
	
		if (hasError()) {
			return false;
		}

		return !hasError();
	}
	
	
	/**
	 * Valida la eliminacion
	 * @author
	 */
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de VO

		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}

	
	

	
	

}
