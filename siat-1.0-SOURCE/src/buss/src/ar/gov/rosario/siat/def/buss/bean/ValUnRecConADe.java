//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.util.DefError;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a ValUnRecConADe
 * Indica los valores por unidades de un recurso autoliquidable
 * 
 * @author tecso
 */

@Entity
@Table(name = "def_valUnRecConADe")
public class ValUnRecConADe extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne()  
    @JoinColumn(name="idRecConADec")
	private RecConADec recConADec;
	
	@Column(name="valorUnitario")
	private Double valorUnitario;
	
	@Column(name="fechaDesde")
	private Date fechaDesde;
	
	@Column(name="fechaHasta")
	private Date fechaHasta;
	
	@Column(name="valRefDes")
	private Double valRefDes;
	
	@Column(name="valRefHas")
	private Double valRefHas;
	
	@ManyToOne()
	@JoinColumn(name="idRecAli")
	private RecAli recAli;
	
	
	
	// Constructores
	public ValUnRecConADe(){
		super();
	}
	// Getters y Setters
	public RecConADec getRecConADec() {
		return recConADec;
	}

	public void setRecConADec(RecConADec recConADec) {
		this.recConADec = recConADec;
	}

	public Double getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(Double valorUnitario) {
		this.valorUnitario = valorUnitario;
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
	
	public Double getValRefDes() {
		return valRefDes;
	}
	
	public void setValRefDes(Double valRefDes) {
		this.valRefDes = valRefDes;
	}
	
	public Double getValRefHas() {
		return valRefHas;
	}
	
	public void setValRefHas(Double valRefHas) {
		this.valRefHas = valRefHas;
	}
	
	public RecAli getRecAli() {
		return recAli;
	}
	public void setRecAli(RecAli recAli) {
		this.recAli = recAli;
	}
	// Metodos de Clase
	public static ValUnRecConADe getById(Long id) {
		return (ValUnRecConADe) DefDAOFactory.getValUnRecConADeDAO().getById(id);  
	}
	
	public static ValUnRecConADe getByIdNull(Long id) {
		return (ValUnRecConADe) DefDAOFactory.getValUnRecConADeDAO().getByIdNull(id);
	}
	
	public static List<ValUnRecConADe> getList() {
		return (List<ValUnRecConADe>) DefDAOFactory.getValUnRecConADeDAO().getList();
	}
	
	public static List<ValUnRecConADe> getListActivos() {			
		return (List<ValUnRecConADe>) DefDAOFactory.getValUnRecConADeDAO().getListActiva();
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

	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();

		//Validaciones de Requeridos
		if (this.valorUnitario==null){
			this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,DefError.VALUNRECCONADEC_VALORUNITARIO_LABEL);
		}
		if (this.fechaDesde==null){
			this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.VALUNRECCONADEC_FECHADESDE_LABEL);
		}
		if (this.recConADec==null){
			this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,DefError.VALUNRECCONADEC_RECCONADEC_LABEL);
		}
	
		if (hasError()) {
			return false;
		}
		// Validaciones de Unicidad
		
		// Otras Validaciones
				
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

	// Metodos de negocio

	
	
}
