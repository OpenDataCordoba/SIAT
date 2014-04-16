//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.rod.buss.dao.RodDAOFactory;
import ar.gov.rosario.siat.rod.iface.util.RodError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;


/**
 * Bean correspondiente a Propietario
 * @author tecso
 */
@Entity
@Table(name = "rod_propietario")
public class Propietario extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	// C
	@Column(name = "apellidoORazon")
	private String apellidoORazon;
	@Column(name = "codTipoDoc")
	private Integer codTipoDoc;
	@Column(name = "desTipoDoc")	
	private String desTipoDoc;
	@Column(name = "nroDoc")
	private Long nroDoc;
	@Column(name = "nroCuit")
	private String nroCuit;
	@Column(name = "nroIB")
	private Integer nroIB;
	@Column(name = "nroProdAgr")
	private Integer nroProdAgr;
	@Column(name = "codTipoProp")
	private Integer codTipoProp;
	@Column(name = "desTipoProp")
	private String desTipoProp;
	@Column(name = "fechaNac")
	private Date fechaNac;
	@Column(name = "codEstCiv")
	private Integer codEstCiv;
	@Column(name = "desEstCiv")
	private String desEstCiv;
	@Column(name = "codSexo")	
	private Integer codSexo;
	@Column(name = "desSexo")
	private String desSexo;
	@Column(name = "tipoPropietario")
	private Integer tipoPropietario; // 1-Propietario Actual
									 // 2-Propietario Anterior
	@ManyToOne()
	@JoinColumn(name="idTramiteRA")
	private TramiteRA tramiteRA;

	@Column(name = "esPropPrincipal")
	private Integer esPropPrincipal;
	
	//<#Propiedades#>
	
	// Constructores
	public Propietario(){
		super();
		// Seteo de valores default			
	}


	public String getApellidoORazon() {
		return apellidoORazon;
	}


	public void setApellidoORazon(String apellidoORazon) {
		this.apellidoORazon = apellidoORazon;
	}


	public Integer getCodTipoDoc() {
		return codTipoDoc;
	}


	public void setCodTipoDoc(Integer codTipoDoc) {
		this.codTipoDoc = codTipoDoc;
	}


	public String getDesTipoDoc() {
		return desTipoDoc;
	}


	public void setDesTipoDoc(String desTipoDoc) {
		this.desTipoDoc = desTipoDoc;
	}


	public Long getNroDoc() {
		return nroDoc;
	}


	public void setNroDoc(Long nroDoc) {
		this.nroDoc = nroDoc;
	}


	public String getNroCuit() {
		return nroCuit;
	}


	public void setNroCuit(String nroCuit) {
		this.nroCuit = nroCuit;
	}

	public Integer getNroIB() {
		return nroIB;
	}


	public void setNroIB(Integer nroIB) {
		this.nroIB = nroIB;
	}


	public Integer getNroProdAgr() {
		return nroProdAgr;
	}


	public void setNroProdAgr(Integer nroProdAgr) {
		this.nroProdAgr = nroProdAgr;
	}


	public Integer getCodTipoProp() {
		return codTipoProp;
	}


	public void setCodTipoProp(Integer codTipoProp) {
		this.codTipoProp = codTipoProp;
	}


	public String getDesTipoProp() {
		return desTipoProp;
	}


	public void setDesTipoProp(String desTipoProp) {
		this.desTipoProp = desTipoProp;
	}


	public Date getFechaNac() {
		return fechaNac;
	}


	public void setFechaNac(Date fechaNac) {
		this.fechaNac = fechaNac;
	}


	public Integer getCodEstCiv() {
		return codEstCiv;
	}


	public void setCodEstCiv(Integer codEstCiv) {
		this.codEstCiv = codEstCiv;
	}


	public String getDesEstCiv() {
		return desEstCiv;
	}


	public void setDesEstCiv(String desEstCiv) {
		this.desEstCiv = desEstCiv;
	}


	public Integer getCodSexo() {
		return codSexo;
	}


	public void setCodSexo(Integer codSexo) {
		this.codSexo = codSexo;
	}


	public String getDesSexo() {
		return desSexo;
	}

	public void setDesSexo(String desSexo) {
		this.desSexo = desSexo;
	}
	
	public Integer getTipoPropietario() {
		return tipoPropietario;
	}


	public void setTipoPropietario(Integer tipoPropietario) {
		this.tipoPropietario = tipoPropietario;
	}

	public TramiteRA getTramiteRA() {
		return tramiteRA;
	}


	public void setTramiteRA(TramiteRA tramiteRA) {
		this.tramiteRA = tramiteRA;
	}

	public Integer getEsPropPrincipal() {
		return esPropPrincipal;
	}


	public void setEsPropPrincipal(Integer esPropPrincipal) {
		this.esPropPrincipal = esPropPrincipal;
	}


	// Metodos de Clase	
	public static Propietario getById(Long id) {
		return (Propietario) RodDAOFactory.getPropietarioDAO().getById(id);
	}
	
	public static Propietario getByIdNull(Long id) {
		return (Propietario) RodDAOFactory.getPropietarioDAO().getByIdNull(id);
	}
	
	public static List<Propietario> getList() {
		return (ArrayList<Propietario>) RodDAOFactory.getPropietarioDAO().getList();
	}
	
	public static List<Propietario> getListActivos() {			
		return (ArrayList<Propietario>) RodDAOFactory.getPropietarioDAO().getListActiva();
	}
	
	public static List<Propietario> getByTramiteRA(Long idTramite) {			
		return (ArrayList<Propietario>) RodDAOFactory.getPropietarioDAO().getByTramiteRA(idTramite);
	}
	
	public static Propietario getByDocTipoPropietario(Long nroDoc, Integer tipoPropietario,Long idTramite) {			
		return RodDAOFactory.getPropietarioDAO().getByDocTipoPropietario(nroDoc,tipoPropietario,idTramite);
	}
	
	// Validaciones 
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		Logger log = Logger.getLogger(Propietario.class);
		if(StringUtil.isNullOrEmpty(getApellidoORazon())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,RodError.PROPIETARIO_APELLIDOORAZON);
		}
		if(getCodTipoDoc()<0){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,RodError.PROPIETARIO_TIPODOC);
		}
		
		if(getNroDoc()==null || getNroDoc().intValue()<0){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,RodError.PROPIETARIO_NRODOC);
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
	
		/*Ejemplo:
		if (GenericDAO.hasReference(this, BeanRelacionado .class, " bean ")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							RodError.${BEAN}_LABEL, RodError. BEAN_RELACIONADO _LABEL );
		}*/
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		
		if (hasError()) {
			return false;
		}
		
		
		return true;
	}

	
}