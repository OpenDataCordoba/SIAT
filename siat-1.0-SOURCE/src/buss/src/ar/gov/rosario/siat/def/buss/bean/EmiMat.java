//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OrderBy;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.util.DefError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a la matriz de Parametros
 * de Emision
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_emiMat")
public class EmiMat extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final String COD_CUMUR_DREI = "CUMUR_DREI";
	
	@Column(name = "codEmiMat")
	private String codEmiMat;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idRecurso") 
	private Recurso recurso;
	
	@OneToMany(mappedBy="emiMat", fetch=FetchType.LAZY)
	@JoinColumn(name = "idEmiMat")
	@OrderBy(clause="orden")
	private List<ColEmiMat> listColEmiMat;
	
	// Constructores
	public EmiMat(){
		super();
		this.listColEmiMat = new ArrayList<ColEmiMat>();
	}
	
	public EmiMat(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static EmiMat getById(Long id) {
		return (EmiMat) DefDAOFactory.getEmiMatDAO().getById(id);
	}
	
	public static EmiMat getByIdNull(Long id) {
		return (EmiMat) DefDAOFactory.getEmiMatDAO().getByIdNull(id);
	}
	
	public static EmiMat getByCodigo(String codEmiMat) throws Exception{
		return (EmiMat) DefDAOFactory.getEmiMatDAO().getByCodigo(codEmiMat);
	}
	
	public static List<EmiMat> getList() {
		return (ArrayList<EmiMat>) DefDAOFactory.getEmiMatDAO().getList();
	}
	
	public static List<EmiMat> getListActivos() {			
		return (ArrayList<EmiMat>) DefDAOFactory.getEmiMatDAO().getListActiva();
	}

	public static List<EmiMat> getListActivosBy(Recurso recurso) throws Exception {
		return (ArrayList<EmiMat>) DefDAOFactory.getEmiMatDAO().getListActivosBy(recurso.getId());
	}

	
	// Getters y setters
	public String getCodEmiMat() {
		return codEmiMat;
	}

	public void setCodEmiMat(String codEmiMat) {
		this.codEmiMat = codEmiMat;
	}
	
	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public List<ColEmiMat> getListColEmiMat() {
		return listColEmiMat;
	}

	public void setListColEmiMat(List<ColEmiMat> listColEmiMat) {
		this.listColEmiMat = listColEmiMat;
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
	
		if (GenericDAO.hasReference(this, ColEmiMat .class, "emiMat")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							DefError.EMIMAT_LABEL, DefError. COLEMIMAT_LABEL );
		}
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		if (StringUtil.isNullOrEmpty(getCodEmiMat())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.EMIMAT_CODEMIMAT );
		}
		
		if (getRecurso() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.EMIMAT_RECURSO );
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codEmiMat");
		uniqueMap.addEntity("recurso");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, DefError.EMIMAT_CODEMIMAT);			
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el EmiMat. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		DefDAOFactory.getEmiMatDAO().update(this);
	}

	/**
	 * Desactiva el EmiMat. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		DefDAOFactory.getEmiMatDAO().update(this);
	}
	
	/**
	 * Valida la activacion del EmiMat
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del EmiMat
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}	
	
	// ---> ABM ColEmiMat
	public ColEmiMat createColEmiMat(ColEmiMat colEmiMat) throws Exception {

		// Validaciones de negocio
		if (!colEmiMat.validateCreate()) {
			return colEmiMat;
		}

		DefDAOFactory.getColEmiMatDAO().update(colEmiMat);

		return colEmiMat;
	}
	
	public ColEmiMat updateColEmiMat(ColEmiMat colEmiMat) throws Exception {
		
		// Validaciones de negocio
		if (!colEmiMat.validateUpdate()) {
			return colEmiMat;
		}

		DefDAOFactory.getColEmiMatDAO().update(colEmiMat);
		
		return colEmiMat;
	}
	
	public ColEmiMat deleteColEmiMat(ColEmiMat colEmiMat) throws Exception {
	
		// Validaciones de negocio
		if (!colEmiMat.validateDelete()) {
			return colEmiMat;
		}
		
		DefDAOFactory.getColEmiMatDAO().delete(colEmiMat);
		
		return colEmiMat;
	}
	// <--- ABM ColEmiMat

	public int cantColumnas() {
		return getListColEmiMat().size();
	}

}