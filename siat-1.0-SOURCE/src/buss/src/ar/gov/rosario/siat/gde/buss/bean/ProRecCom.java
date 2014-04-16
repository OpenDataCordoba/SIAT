//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente a ProRecCom
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_proRecCom")
public class ProRecCom extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional=false) 
    @JoinColumn(name="idProRec") 
	private ProRec proRec;
	
	@Column(name = "fecVtoDeuDes") // DATETIME YEAR TO DAY NOT NULL
	private Date fecVtoDeuDes;  

	@Column(name = "fecVtoDeuHas") // DATETIME YEAR TO DAY NOT NULL
	private Date fecVtoDeuHas;  
	
	@Column(name = "porcentajeComision") 
	private Double porcentajeComision;     // DECIMAL(16,6) NOT NULL 
	
	@Column(name = "fechaDesde") 
	private Date fechaDesde;     // DATETIME YEAR TO DAY NOT NULL
	
	@Column(name = "fechaHasta") 
	private Date fechaHasta;     // DATETIME YEAR TO DAY NOT NULL

	
	// Constructores
	public ProRecCom(){
		super();
		// Seteo de valores default	
	}
	
	public ProRecCom(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static ProRecCom getById(Long id) {
		return (ProRecCom) GdeDAOFactory.getProRecComDAO().getById(id);
	}
	
	public static ProRecCom getByIdNull(Long id) {
		return (ProRecCom) GdeDAOFactory.getProRecComDAO().getByIdNull(id);
	}
	
	public static List<ProRecCom> getList() {
		return (ArrayList<ProRecCom>) GdeDAOFactory.getProRecComDAO().getList();
	}
	
	public static List<ProRecCom> getListActivos() {			
		return (ArrayList<ProRecCom>) GdeDAOFactory.getProRecComDAO().getListActiva();
	}
	
	/**
	 * Chekea si existe una Comisison del Recurso, delimitado por desde-hasta, en el intevalo delimitado 
	 * por fechaDesde-fechaHasta.
	 * @param  idProRecComActual
	 * @param  idProRec
	 * @param  fecVtoDeuDes
	 * @param  fecVtoDeuHas
	 * @param  fechaDesde
	 * @param  fechaHasta
	 * @return Boolean
	 */
	public Boolean existeComision(Long idProRecComActual, Long idProRec, Date fecVtoDeuDes , Date fecVtoDeuHas, Date fechaDesde, Date fechaHasta) {
		return GdeDAOFactory.getProRecComDAO().existeComision(idProRecComActual, idProRec, fecVtoDeuDes, fecVtoDeuHas, fechaDesde, fechaHasta);
	}
	
	//	 Getters y setters
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

	public Date getFecVtoDeuDes() {
		return fecVtoDeuDes;
	}

	public void setFecVtoDeuDes(Date fecVtoDeuDes) {
		this.fecVtoDeuDes = fecVtoDeuDes;
	}

	public Date getFecVtoDeuHas() {
		return fecVtoDeuHas;
	}

	public void setFecVtoDeuHas(Date fecVtoDeuHas) {
		this.fecVtoDeuHas = fecVtoDeuHas;
	}

	public Double getPorcentajeComision() {
		return porcentajeComision;
	}

	public void setPorcentajeComision(Double porcentajeComision) {
		this.porcentajeComision = porcentajeComision;
	}

	public ProRec getProRec() {
		return proRec;
	}

	public void setProRec(ProRec proRec) {
		this.proRec = proRec;
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
		
		Boolean check = true;

		//	Validaciones        
		if (getFecVtoDeuDes() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					GdeError.PRORECCOM_FECVTODEUDES_LABEL);
			check = false;
		}
		if (getFecVtoDeuHas() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					GdeError.PRORECCOM_FECVTODEUHAS_LABEL);
			check = false;
		}
		if (getFecVtoDeuDes() != null && getFecVtoDeuHas()!=null && getFecVtoDeuDes().after(getFecVtoDeuHas())){
			addRecoverableError(BaseError.MSG_VALORMENORQUE,GdeError.PRORECCOM_FECVTODEUHAS_LABEL, 
							GdeError.PRORECCOM_FECVTODEUDES_LABEL);
			check = false;
		}
		if (getPorcentajeComision() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					GdeError.PRORECCOM_PORCENTAJECOMISION_LABEL);
			check = false;
		}
		if (getPorcentajeComision() != null && (getPorcentajeComision() <=0 || getPorcentajeComision() > 1)) {
			addRecoverableError(BaseError.MSG_RANGO_INVALIDO,
					GdeError.PRORECCOM_PORCENTAJECOMISION_LABEL);
			check = false;
		}
		if (getFechaDesde()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					GdeError.PRORECCOM_FECHADESDE_LABEL);
			check = false;
		}
		if (getFechaHasta()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					GdeError.PRORECCOM_FECHAHASTA_LABEL);
			check = false;
		}
		if(getFechaDesde()!=null && getFechaHasta()!=null && getFechaDesde().after(getFechaHasta())){
			addRecoverableError(BaseError.MSG_VALORMENORQUE,GdeError.PRORECCOM_FECHAHASTA_LABEL, 
					GdeError.PRORECCOM_FECHADESDE_LABEL);
			check = false;
		}
		
		/* ahora permitimos solapamiento
		if(check && getFecVtoDeuDes() != null && getFecVtoDeuHas() != null && getFechaDesde() != null && getFechaHasta() != null 
				&& existeComision(getId(), proRec.getId(), fecVtoDeuDes, fecVtoDeuHas, fechaDesde, fechaHasta)) {
			addRecoverableError(GdeError.PRORECCOM_COMISIONEXISTENTE_LABEL);
		}
		*/
				
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique

		return true;
	}


	
	// Metodos de negocio
	/**
	 * Obtiene el vigente para el proRec pasado como parametro a la fecha pasada como parametro 
	 * @param proRec
	 * @param fechaVto
	 * @return
	 */
	public static ProRecCom getVigente(ProRec proRec, Date fechaVto, Date fechaVigencia, Long anio) {
		return GdeDAOFactory.getProRecComDAO().getVigente(proRec, fechaVto, fechaVigencia, anio);
	}
}
