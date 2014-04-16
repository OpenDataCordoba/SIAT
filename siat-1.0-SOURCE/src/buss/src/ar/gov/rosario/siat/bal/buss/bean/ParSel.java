//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
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
 * Bean correspondiente a ParSel
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_parSel")
public class ParSel extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idSellado") 
	private Sellado sellado;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idPartida") 
	private Partida partida;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idTipoDistrib") 
	private TipoDistrib tipoDistrib;
	
	@Column(name = "monto")
	private Double monto;

	
	// Constructores
	public ParSel(){
		super();
		// Seteo de valores default	
	}
	
	public ParSel(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static ParSel getById(Long id) {
		return (ParSel) BalDAOFactory.getParSelDAO().getById(id);
	}
	
	public static ParSel getByIdNull(Long id) {
		return (ParSel) BalDAOFactory.getParSelDAO().getByIdNull(id);
	}
	
	public static List<ParSel> getList() {
		return (ArrayList<ParSel>) BalDAOFactory.getParSelDAO().getList();
	}
	
	public static List<ParSel> getListActivos() {			
		return (ArrayList<ParSel>) BalDAOFactory.getParSelDAO().getListActiva();
	}
	
	
	
	//	 Getters y setters
	
	public Double getMonto() {
		return monto;
	}

	public void setMonto(Double monto) {
		this.monto = monto;
	}

	public Partida getPartida() {
		return partida;
	}

	public void setPartida(Partida partida) {
		this.partida = partida;
	}

	public Sellado getSellado() {
		return sellado;
	}

	public void setSellado(Sellado sellado) {
		this.sellado = sellado;
	}

	public TipoDistrib getTipoDistrib() {
		return tipoDistrib;
	}

	public void setTipoDistrib(TipoDistrib tipoDistrib) {
		this.tipoDistrib = tipoDistrib;
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
	

		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        

		if (getPartida() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.PARSEL_PARTIDA);
		}
		
		if (getTipoDistrib() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.PARSEL_TIPODISTRIB);
		}
		
		if (getMonto()<0) {
			addRecoverableError(BaseError.MSG_VALORMENORQUECERO, BalError.IMPSEL_COSTO_LABEL);
			
		}
		

		if (hasError()) {
			return false;
		}
		
		
		return true;
	}
	
}
