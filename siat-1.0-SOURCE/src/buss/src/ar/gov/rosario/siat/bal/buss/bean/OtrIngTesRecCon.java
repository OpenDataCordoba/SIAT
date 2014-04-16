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
import ar.gov.rosario.siat.def.buss.bean.RecCon;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean que representa la apertura en Conceptos de los Otros Ingresos de Tesoreria
 * 
 * @author Tecso
 *
 */
@Entity
@Table(name = "bal_otrIngTesRecCon")
public class OtrIngTesRecCon extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idOtrIngTes") 
	private OtrIngTes otrIngTes;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idRecCon") 
	private RecCon recCon;
	
	@Column(name = "importe")
	private Double importe;
	
	// Constructores 
	public OtrIngTesRecCon(){
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
	public RecCon getRecCon() {
		return recCon;
	}
	public void setRecCon(RecCon recCon) {
		this.recCon = recCon;
	}
	
	// Metodos de clase	
	public static OtrIngTesRecCon getById(Long id) {
		return (OtrIngTesRecCon) BalDAOFactory.getOtrIngTesRecConDAO().getById(id);
	}
	
	public static OtrIngTesRecCon getByIdNull(Long id) {
		return (OtrIngTesRecCon) BalDAOFactory.getOtrIngTesRecConDAO().getByIdNull(id);
	}
	
	public static List<OtrIngTesRecCon> getList() {
		return (ArrayList<OtrIngTesRecCon>) BalDAOFactory.getOtrIngTesRecConDAO().getList();
	}
	
	public static List<OtrIngTesRecCon> getListActivos() {			
		return (ArrayList<OtrIngTesRecCon>) BalDAOFactory.getOtrIngTesRecConDAO().getListActiva();
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
