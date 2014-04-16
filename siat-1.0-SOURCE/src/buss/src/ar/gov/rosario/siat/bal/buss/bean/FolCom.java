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
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Bean que representa las compensaciones
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_folCom")
public class FolCom extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idFolio") 
	private Folio folio;
	
	@Column(name = "fecha")
	private Date fecha;
	
	@Column(name = "concepto")
	private String concepto;
	
	@Column(name = "importe")
	private Double importe;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idCueBan") 
	private CuentaBanco cueBan;
	
	@Column(name = "desCueBan")
	private String desCueBan;
	
	@Column(name = "nroComp")
	private String nroComp;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idCompensacion") 
	private Compensacion compensacion;
	
	
	//Constructores 
	public FolCom(){
		super();
	}

	// Getters Y Setters
	public String getConcepto() {
		return concepto;
	}
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	public CuentaBanco getCueBan() {
		return cueBan;
	}
	public void setCueBan(CuentaBanco cueBan) {
		this.cueBan = cueBan;
	}
	public String getDesCueBan() {
		return desCueBan;
	}
	public void setDesCueBan(String desCueBan) {
		this.desCueBan = desCueBan;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Double getImporte() {
		return importe;
	}
	public void setImporte(Double importe) {
		this.importe = importe;
	}
	public String getNroComp() {
		return nroComp;
	}
	public void setNroComp(String nroComp) {
		this.nroComp = nroComp;
	}
	public Compensacion getCompensacion() {
		return compensacion;
	}
	public void setCompensacion(Compensacion compensacion) {
		this.compensacion = compensacion;
	}
	public Folio getFolio() {
		return folio;
	}
	public void setFolio(Folio folio) {
		this.folio = folio;
	}

	// Metodos de clase	
	public static FolCom getById(Long id) {
		return (FolCom) BalDAOFactory.getFolComDAO().getById(id);
	}
	
	public static FolCom getByIdNull(Long id) {
		return (FolCom) BalDAOFactory.getFolComDAO().getByIdNull(id);
	}
		
	public static List<FolCom> getList() {
		return (ArrayList<FolCom>) BalDAOFactory.getFolComDAO().getList();
	}
	
	public static List<FolCom> getListActivos() {			
		return (ArrayList<FolCom>) BalDAOFactory.getFolComDAO().getListActiva();
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
		if(StringUtil.isNullOrEmpty(getConcepto())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.FOLCOM_CONCEPTO_LABEL);
		}
		if(getFecha()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.FOLCOM_FECHA_LABEL);
		}
		if(getImporte()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.FOLCOM_IMPORTE_LABEL);
		}
		if(StringUtil.isNullOrEmpty(getDesCueBan())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.FOLCOM_DESCUEBAN_LABEL);
		}
		if(StringUtil.isNullOrEmpty(getNroComp())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.FOLCOM_NROCOM_LABEL);
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
		
		/*if (GenericDAO.hasReference(this, TranArc.class, "folCom")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				BalError.ARCHIVO_LABEL , BalError.DISPARDET_LABEL);
		}

		if (GenericDAO.hasReference(this, FolComRec.class, "disPar")) {
		addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
			BalError.DISPAR_LABEL , BalError.DISPARREC_LABEL);
		}
		
		if (GenericDAO.hasReference(this, FolComPla.class, "disPar")) {
		addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
			BalError.DISPAR_LABEL , BalError.DISPARPLA_LABEL);
		}*/
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}

	
}
