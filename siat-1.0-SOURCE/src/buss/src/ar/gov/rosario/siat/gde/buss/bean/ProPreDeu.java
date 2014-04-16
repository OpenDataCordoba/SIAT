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
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente a ProPreDeu: Prescripcion Masiva 
 * de Deuda
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_proPreDeu")
public class ProPreDeu extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	public  static final String ADP_PARAM_ID   = "ID";
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idViaDeuda") 
	private ViaDeuda viaDeuda;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idServicioBanco") 
	private ServicioBanco servicioBanco;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="idCorrida")
	private Corrida corrida;
	
	@Column(name = "fechaTope")
	private Date fechaTope;
	
	@OneToMany(mappedBy="proPreDeu", fetch=FetchType.LAZY)
	@JoinColumn(name="idProPreDeu")
	private List<ProPreDeuDet> listProPreDeuDet;

	// Constructores
	public ProPreDeu(){
		super();
	}
	
	public ProPreDeu(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static ProPreDeu getById(Long id) {
		return (ProPreDeu) GdeDAOFactory.getProPreDeuDAO().getById(id);
	}
	
	public static ProPreDeu getByIdNull(Long id) {
		return (ProPreDeu) GdeDAOFactory.getProPreDeuDAO().getByIdNull(id);
	}
	
	public static List<ProPreDeu> getList() {
		return (ArrayList<ProPreDeu>) GdeDAOFactory.getProPreDeuDAO().getList();
	}
	
	public static List<ProPreDeu> getListActivos() {			
		return (ArrayList<ProPreDeu>) GdeDAOFactory.getProPreDeuDAO().getListActiva();
	}
	
	// Getters y setters
	public ViaDeuda getViaDeuda() {
		return viaDeuda;
	}

	public void setViaDeuda(ViaDeuda viaDeuda) {
		this.viaDeuda = viaDeuda;
	}

	public ServicioBanco getServicioBanco() {
		return servicioBanco;
	}

	public void setServicioBanco(ServicioBanco servicioBanco) {
		this.servicioBanco = servicioBanco;
	}

	public Corrida getCorrida() {
		return corrida;
	}

	public void setCorrida(Corrida corrida) {
		this.corrida = corrida;
	}

	public Date getFechaTope() {
		return fechaTope;
	}

	public void setFechaTope(Date fechaTope) {
		this.fechaTope = fechaTope;
	}
	
	public List<ProPreDeuDet> getListProPreDeuDet() {
		return listProPreDeuDet;
	}

	public void setListProPreDeuDet(List<ProPreDeuDet> listProPreDeuDet) {
		this.listProPreDeuDet = listProPreDeuDet;
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
	
		if (GenericDAO.hasReference(this, ProPreDeuDet.class, "proPreDeu")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							GdeError.PROPREDEU_LABEL, GdeError.PROPREDEUDET_LABEL);
		}
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		if (getViaDeuda() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PROPREDEU_VIADEUDA);
		}
		
		if (getServicioBanco() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PROPREDEU_SERVICIOBANCO);
		}

		if (getFechaTope() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PROPREDEU_FECHATOPE);
		}
		
		if (hasError()) {
			return false;
		}
	
		return true;
	}
	
	// Metodos de negocio
	public ProPreDeuDet createProPreDetDet(ProPreDeuDet proPreDeuDet) throws Exception {
		// Validaciones de negocio
		if (!proPreDeuDet.validateCreate()) {
			return proPreDeuDet;
		}
	
		GdeDAOFactory.getProPreDeuDetDAO().update(proPreDeuDet);
		
		return proPreDeuDet;
	}	
	
	public ProPreDeuDet updateProPreDetDet(ProPreDeuDet proPreDeuDet) throws Exception {
		// Validaciones de negocio
		if (!proPreDeuDet.validateUpdate()) {
			return proPreDeuDet;
		}
	
		GdeDAOFactory.getProPreDeuDetDAO().update(proPreDeuDet);
		
		return proPreDeuDet;
	}

	public ProPreDeuDet deleteProPreDetDet(ProPreDeuDet proPreDeuDet) throws Exception {
		// Validaciones de negocio
		if (!proPreDeuDet.validateDelete()) {
			return proPreDeuDet;
		}
	
		GdeDAOFactory.getProPreDeuDetDAO().delete(proPreDeuDet);
		
		return proPreDeuDet;
	}

}
