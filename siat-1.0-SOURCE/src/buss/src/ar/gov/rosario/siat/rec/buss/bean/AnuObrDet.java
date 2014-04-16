//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.rec.buss.dao.RecDAOFactory;
import ar.gov.rosario.siat.rec.iface.util.RecError;
import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente a los detalles de la Anulacion de Obra
 * 
 * @author tecso
 */
@Entity
@Table(name = "cdm_anuObrDet")
public class AnuObrDet extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idAnulacionObra") 
	private AnulacionObra anulacionObra;
	
    @Column(name="idDeuda") 
	private Long idDeuda;
	
    // Constructores
	public AnuObrDet(){
		super();
	}
	
	public AnuObrDet(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static AnuObrDet getById(Long id) {
		return (AnuObrDet) RecDAOFactory.getAnuObrDetDAO().getById(id);
	}
	
	public static AnuObrDet getByIdNull(Long id) {
		return (AnuObrDet) RecDAOFactory.getAnuObrDetDAO().getByIdNull(id);
	}
	
	public static List<AnuObrDet> getList() {
		return (ArrayList<AnuObrDet>) RecDAOFactory.getAnuObrDetDAO().getList();
	}
	
	public static List<AnuObrDet> getListActivos() {			
		return (ArrayList<AnuObrDet>) RecDAOFactory.getAnuObrDetDAO().getListActiva();
	}
	
	// Getters y setters
	public AnulacionObra getAnulacionObra() {
		return anulacionObra;
	}

	public void setAnulacionObra(AnulacionObra anulacionObra) {
		this.anulacionObra = anulacionObra;
	}

	public Long getIdDeuda() {
		return idDeuda;
	}

	public void setIdDeuda(Long idDeuda) {
		this.idDeuda = idDeuda;
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
		if (getAnulacionObra() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.ANUOBRDET_ANULACIONOBRA);
		}
		
		if (getIdDeuda() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.ANUOBRDET_IDDEUDA);
		}
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	
}
