//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a ImpSel
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_impSel")
public class ImpSel extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idSellado")
	private Sellado sellado;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idTipoSellado")
	private TipoSellado tipoSellado;

	@Column(name = "costo")
	private Double costo;

	@Column(name = "fechaDesde")
	private Date fechaDesde;

	@Column(name = "fechaHasta")
	private Date fechaHasta;

	// <#Propiedades#>


	// Constructores
	public ImpSel() {
		super();
		// Seteo de valores default
		// propiedad_ejemplo = valorDefault;
	}

	public ImpSel(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static ImpSel getById(Long id) {
		return (ImpSel) BalDAOFactory.getImpSelDAO().getById(id);
	}

	public static ImpSel getByIdNull(Long id) {
		return (ImpSel) BalDAOFactory.getImpSelDAO().getByIdNull(id);
	}

	public static ImpSel getConFechaHastaNullBySellado(Sellado sellado) throws Exception{
		return (ImpSel) BalDAOFactory.getImpSelDAO().getConFechaHastaNullBySellado(sellado);
	}
	
	public static List<ImpSel> getList() {
		return (ArrayList<ImpSel>) BalDAOFactory.getImpSelDAO().getList();
	}

	public static List<ImpSel> getListActivos() {
		return (ArrayList<ImpSel>) BalDAOFactory.getImpSelDAO().getListActiva();
	}

	/**
	 * Devuelve TRUE si existe un impSel entre esas fechas, ya sea que haya empezado antes y/o que termine después.
	 * Es para evitar el solapamiento de los importes de un sellado
	 * @param fechaDesde
	 * @param fechaHasta
	 * @return
	 */
	public static Boolean existe(Long idSellado, Long idImpSelActual, Date fechaDesde, Date fechaHasta){
		return BalDAOFactory.getImpSelDAO().existeImpSel(idSellado, idImpSelActual, fechaDesde, fechaHasta);
	}
	
	// Getters y setters

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

	public Double getCosto() {
		return costo;
	}

	public void setCosto(Double costo) {
		this.costo = costo;
	}

	public Sellado getSellado() {
		return sellado;
	}

	public void setSellado(Sellado sellado) {
		this.sellado = sellado;
	}
	
	public TipoSellado getTipoSellado() {
		return tipoSellado;
	}

	public void setTipoSellado(TipoSellado tipoSellado) {
		this.tipoSellado = tipoSellado;
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
		// limpiamos la lista de errores
		clearError();

		if (hasError()) {
			return false;
		}

		return true;
	}

	private boolean validate() throws Exception {

		// Validaciones
		boolean controlarRango = true;
		
		if (getTipoSellado() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.IMPSEL_TIPOSELLADO_LABEL);
			controlarRango=false;
		}
		
		if (getFechaDesde()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.IMPSEL_FECHADESDE_LABEL);
			controlarRango=false;
		}

		if (null == getCosto()) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.IMPSEL_COSTO_LABEL);
			controlarRango=false;
		}
		
		if(getFechaDesde()!=null && getFechaHasta()!=null && getFechaDesde().after(getFechaHasta())){
			addRecoverableError(BaseError.MSG_VALORMENORQUE,BalError.IMPSEL_FECHAHASTA_LABEL, BalError.IMPSEL_FECHADESDE_LABEL);
			controlarRango=false;
		}
		
		if (controlarRango && (getFechaDesde()!=null || getFechaHasta()!=null) && ImpSel.existe(getSellado().getId(), getId(), getFechaDesde(), getFechaHasta())){
			addRecoverableError(BaseError.MSG_SOLAPAMIENTO_VIGENCIA);
		}
				
		if (hasError()) {
			return false;
		}
		
		if (getCosto() < 0) {
			addRecoverableError(BaseError.MSG_VALORMENORQUECERO, BalError.IMPSEL_COSTO_LABEL);
			controlarRango=false;
		}

		if (hasError()) {
			return false;
		}

		return true;
	}

	// Metodos de negocio
}
