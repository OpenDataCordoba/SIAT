//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a la Deuda Excluida del Envio de Deuda a Judiciales
 * 
 * @author tecso
 */

@Entity
@Table(name = "gde_proMasDeuExc")
public class ProMasDeuExc extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "proMasDeuExc";

	@ManyToOne(optional=false) 
    @JoinColumn(name="idProcesoMasivo") 
	private ProcesoMasivo procesoMasivo;

	@Column(name = "idDeuda")
	private Long idDeuda;              // no nulo
	
	@ManyToOne() 
    @JoinColumn(name="idProcurador") 
	private Procurador procurador;

	@ManyToOne(optional=false)
	@JoinColumn(name="idMotExc")
	private MotExc motExc;
	
	@Column(name = "observacion") // nuleable varchar(255)
	private String observacion;

	@Column(name = "desTitulPrincipal") // nuleable varchar(120)
	private String desTitularPrincipal;

	@ManyToOne(optional=false) 
    @JoinColumn(name="idTipoSelAlmDet") 
	private TipoSelAlm tipoSelAlmDet;   
	
	
	// Constructores
	public ProMasDeuExc(){
		super();
	}
	

	// Getters y Setters
	public Long getIdDeuda() {
		return idDeuda;
	}
	public void setIdDeuda(Long idDeuda) {
		this.idDeuda = idDeuda;
	}
	public ProcesoMasivo getProcesoMasivo() {
		return procesoMasivo;
	}
	public void setProcesoMasivo(ProcesoMasivo procesoMasivo) {
		this.procesoMasivo = procesoMasivo;
	}
	public String getDesTitularPrincipal() {
		return desTitularPrincipal;
	}
	public void setDesTitularPrincipal(String desTitularPrincipal) {
		this.desTitularPrincipal = desTitularPrincipal;
	}
	public MotExc getMotExc() {
		return motExc;
	}
	public void setMotExc(MotExc motExc) {
		this.motExc = motExc;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public Procurador getProcurador() {
		return procurador;
	}
	public void setProcurador(Procurador procurador) {
		this.procurador = procurador;
	}
	public TipoSelAlm getTipoSelAlmDet() {
		return tipoSelAlmDet;
	}
	public void setTipoSelAlmDet(TipoSelAlm tipoSelAlmDet) {
		this.tipoSelAlmDet = tipoSelAlmDet;
	}

	
	// Metodos de Clase
	public static ProMasDeuExc getById(Long id) {
		return (ProMasDeuExc) GdeDAOFactory.getProMasDeuIncDAO().getById(id);  
	}
	
	public static ProMasDeuExc getByIdNull(Long id) {
		return (ProMasDeuExc) GdeDAOFactory.getProMasDeuIncDAO().getByIdNull(id);
	}
	
	public static List<ProMasDeuExc> getList() {
		return (ArrayList<ProMasDeuExc>) GdeDAOFactory.getProMasDeuIncDAO().getList();
	}
	
	public static List<ProMasDeuExc> getListActivos() {			
		return (ArrayList<ProMasDeuExc>) GdeDAOFactory.getProMasDeuIncDAO().getListActiva();
	}


	// Metodos de Instancia

	//Validaciones
	/**
	 * Valida la creacion
	 * @author 
	 */
	public boolean validateCreate() {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de VO

		if (hasError()) {
			return false;
		}
		
		//Validaciones de Negocio
		
		return true;
	}

	/**
	 * Valida la actualizacion
	 * @author
	 */
	public boolean validateUpdate() {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de VO

		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
		
		return true;		
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
	
	/**
	 * Obtiene la deuda administrativa del ProMasDeuExc
	 * @return DeudaAdmin
	 */
	public DeudaAdmin getDeudaAdmin(){
		return (DeudaAdmin) GdeDAOFactory.getDeudaAdminDAO().getByIdNull(this.getIdDeuda());
	}
	
	public Deuda getDeuda(){
		return (Deuda) GdeDAOFactory.getDeudaDAO().getByIdNull(this.getIdDeuda());
	}
}
