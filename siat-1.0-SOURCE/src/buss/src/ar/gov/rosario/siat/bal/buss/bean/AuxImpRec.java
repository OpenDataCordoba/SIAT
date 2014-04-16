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
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean utilizado para acumular datos para Reporte de Recaudacion por Recurso. (tabla auxiliar)
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_auxImpRec")
public class AuxImpRec extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idAsentamiento") 
	private Asentamiento asentamiento;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idTipoImporte") 
	private TipoImporte tipoImporte;
		
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idRecurso") 
	private Recurso recurso;
	
	@Column(name = "importe")
	private Double importe;
	
	//Constructores 
	public AuxImpRec(){
		super();
	}

	// Getters Y Setters
	public Asentamiento getAsentamiento() {
		return asentamiento;
	}
	public void setAsentamiento(Asentamiento asentamiento) {
		this.asentamiento = asentamiento;
	}
	public Double getImporte() {
		return importe;
	}
	public void setImporte(Double importe) {
		this.importe = importe;
	}
	public Recurso getRecurso() {
		return recurso;
	}
	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}
	public TipoImporte getTipoImporte() {
		return tipoImporte;
	}
	public void setTipoImporte(TipoImporte tipoImporte) {
		this.tipoImporte = tipoImporte;
	}
	
	// Metodos de clase	
	public static AuxImpRec getById(Long id) {
		return (AuxImpRec) BalDAOFactory.getAuxImpRecDAO().getById(id);
	}
	
	public static AuxImpRec getByIdNull(Long id) {
		return (AuxImpRec) BalDAOFactory.getAuxImpRecDAO().getByIdNull(id);
	}

	public static AuxImpRec getForAsentamiento(Long idRecurso, Long idTipoImporte, Long idAsentamiento) throws Exception {
		return (AuxImpRec) BalDAOFactory.getAuxImpRecDAO().getForAsentamiento(idRecurso,idTipoImporte, idAsentamiento);
	}
	
	public static List<AuxImpRec> getList() {
		return (ArrayList<AuxImpRec>) BalDAOFactory.getAuxImpRecDAO().getList();
	}
	
	public static List<AuxImpRec> getListByAsentamiento(Asentamiento asentamiento) throws Exception{
		return (ArrayList<AuxImpRec>) BalDAOFactory.getAuxImpRecDAO().getListByAsentamiento(asentamiento);
	}
	
	public static List<AuxImpRec> getListActivos() {			
		return (ArrayList<AuxImpRec>) BalDAOFactory.getAuxImpRecDAO().getListActiva();
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
