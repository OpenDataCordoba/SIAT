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
 * Bean que representa la distribucion de Otros Ingresos de Tesoreria por Partida
 * 
 * @author Tecso
 *
 */
@Entity
@Table(name = "bal_otrIngTesPar")
public class OtrIngTesPar extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idOtrIngTes") 
	private OtrIngTes otrIngTes;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idPartida") 
	private Partida partida;
	
	@Column(name = "importe")
	private Double importe;
	
	@Column(name = "esEjeAct")
	private Integer esEjeAct;
		
	// Constructores 
	public OtrIngTesPar(){
		super();
	}

	// Getters Y Setters
	
	public Double getImporte() {
		return importe;
	}
	public void setImporte(Double importe) {
		this.importe = importe;
	}
	public OtrIngTes getOtrIngTes() {
		return otrIngTes;
	}
	public void setOtrIngTes(OtrIngTes otrIngTes) {
		this.otrIngTes = otrIngTes;
	}
	public Partida getPartida() {
		return partida;
	}
	public void setPartida(Partida partida) {
		this.partida = partida;
	}
	public Integer getEsEjeAct() {
		return esEjeAct;
	}
	public void setEsEjeAct(Integer esEjeAct) {
		this.esEjeAct = esEjeAct;
	}

	// Metodos de clase	
	public static OtrIngTesPar getById(Long id) {
		return (OtrIngTesPar) BalDAOFactory.getOtrIngTesParDAO().getById(id);
	}
	
	public static OtrIngTesPar getByIdNull(Long id) {
		return (OtrIngTesPar) BalDAOFactory.getOtrIngTesParDAO().getByIdNull(id);
	}
	
	public static List<OtrIngTesPar> getList() {
		return (ArrayList<OtrIngTesPar>) BalDAOFactory.getOtrIngTesParDAO().getList();
	}
	
	public static List<OtrIngTesPar> getListActivos() {			
		return (ArrayList<OtrIngTesPar>) BalDAOFactory.getOtrIngTesParDAO().getListActiva();
	}
	
		
	// Metodos de Instancia
	// Validaciones
	/**
	 * Valida la creacion
	 * @author
	 */
	public boolean validateCreate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();

		
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
		if(getPartida()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.OTRINGTESPAR_PARTIDA);
		}
		if(getOtrIngTes()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.OTRINGTESPAR_OTRINGTES);
		}
		/*if(getImporteEjeAct()==null || getImporteEjeVen()==null
				|| (getImporteEjeAct().doubleValue()+getImporteEjeVen().doubleValue()) == 0){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.OTRINGTESPAR_IMPORTE);
		}*/
		if(getImporte()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.OTRINGTESPAR_IMPORTE);
		}
		
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
				
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}

	
}
