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
 * Bean correspondiente a MinRecConADec
 * Indica los valores minimos de un recurso autoliquidable
 * 
 * @author tecso
 */

@Entity
@Table(name = "def_minRecConADec")
public class MinRecConADec extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne()  
    @JoinColumn(name="idRecConADec")
	private RecConADec recConADec;
	
	@Column(name="minimo")
	private Double minimo;
	
	@Column(name="fechaDesde")
	private Date fechaDesde;
	
	@Column(name="fechaHasta")
	private Date fechaHasta;
	
	@Column(name="valRefDes")
	private Double valRefDes;
	
	@Column(name="valRefHas")
	private Double valRefHas;
	
	
	
	// Constructores
	public MinRecConADec(){
		super();
	}
	// Getters y Setters
	public RecConADec getRecConADec() {
		return recConADec;
	}



	public void setRecConADec(RecConADec recConADec) {
		this.recConADec = recConADec;
	}	
	
	public Double getMinimo() {
		return minimo;
	}
	public void setMinimo(Double minimo) {
		this.minimo = minimo;
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
	// Metodos de Clase
	public static MinRecConADec getById(Long id) {
		return (MinRecConADec) DefDAOFactory.getMinRecConADecDAO().getById(id);  
	}
	
	public static MinRecConADec getByIdNull(Long id) {
		return (MinRecConADec) DefDAOFactory.getMinRecConADecDAO().getByIdNull(id);
	}
	
	public static List<MinRecConADec> getList() {
		return (List<MinRecConADec>) DefDAOFactory.getMinRecConADecDAO().getList();
	}
	
	public static List<MinRecConADec> getListActivos() {			
		return (List<MinRecConADec>) DefDAOFactory.getMinRecConADecDAO().getListActiva();
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
		if (this.minimo==null){
			this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,DefError.MINRECCONADEC_MINIMO_LABEL);
		}
		if (this.fechaDesde==null){
			this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.MINRECCONADEC_FECHADESDE_LABEL);
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
