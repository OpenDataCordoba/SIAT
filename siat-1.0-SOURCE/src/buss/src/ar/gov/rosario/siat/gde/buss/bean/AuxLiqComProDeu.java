//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a AuxLiqComProDeu
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_auxliqcomprodeu")
public class AuxLiqComProDeu extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idLiqComPro")
	private LiqComPro liqComPro;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idProRecCom")
	private ProRecCom proRecCom;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idConDeuCuo")
	private ConDeuCuo conDeuCuo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idReciboDeuda")
	private ReciboDeuda reciboDeuda;

	@Column(name = "importeAplicado")
	private Double importeAplicado;

	@Column(name = "importeComision")
	private Double importeComision;

	
	// Constructores
	public AuxLiqComProDeu(){
		super();
	}
	
	public AuxLiqComProDeu(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static AuxLiqComProDeu getById(Long id) {
		return (AuxLiqComProDeu) GdeDAOFactory.getAuxLiqComProDeuDAO().getById(id);
	}
	
	public static AuxLiqComProDeu getByIdNull(Long id) {
		return (AuxLiqComProDeu) GdeDAOFactory.getAuxLiqComProDeuDAO().getByIdNull(id);
	}
	
	public static List<AuxLiqComProDeu> getList() {
		return (List<AuxLiqComProDeu>) GdeDAOFactory.getAuxLiqComProDeuDAO().getList();
	}
	
	public static List<AuxLiqComProDeu> getListActivos() {			
		return (List<AuxLiqComProDeu>) GdeDAOFactory.getAuxLiqComProDeuDAO().getListActiva();
	}
	
	public static List<AuxLiqComProDeu> getByIdLiqComPro(Long idLiqComPro) {
		return (List<AuxLiqComProDeu>) GdeDAOFactory.getAuxLiqComProDeuDAO().getByIdLiqComPro(idLiqComPro);
	}
	
	// Getters y setters

	public LiqComPro getLiqComPro() {
		return liqComPro;
	}

	public void setLiqComPro(LiqComPro liqComPro) {
		this.liqComPro = liqComPro;
	}

	public ProRecCom getProRecCom() {
		return proRecCom;
	}

	public void setProRecCom(ProRecCom proRecCom) {
		this.proRecCom = proRecCom;
	}

	public ConDeuCuo getConDeuCuo() {
		return conDeuCuo;
	}

	public void setConDeuCuo(ConDeuCuo conDeuCuo) {
		this.conDeuCuo = conDeuCuo;
	}

	public ReciboDeuda getReciboDeuda() {
		return reciboDeuda;
	}

	public void setReciboDeuda(ReciboDeuda reciboDeuda) {
		this.reciboDeuda = reciboDeuda;
	}

	public Double getImporteAplicado() {
		return importeAplicado;
	}

	public void setImporteAplicado(Double importeAplicado) {
		this.importeAplicado = importeAplicado;
	}

	public Double getImporteComision() {
		return importeComision;
	}

	public void setImporteComision(Double importeComision) {
		this.importeComision = importeComision;
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
	
		/*Ejemplo:
		if (GenericDAO.hasReference(this, BeanRelacionado .class, " bean ")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							GdeError.AUXLIQCOMPRODEU_LABEL, GdeError. BEAN_RELACIONADO _LABEL );
		}*/
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
	/*	
		//	Validaciones        
		if (StringUtil.isNullOrEmpty(getCodAuxLiqComProDeu())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.AUXLIQCOMPRODEU_CODAUXLIQCOMPRODEU );
		}
		
		if (StringUtil.isNullOrEmpty(getDesAuxLiqComProDeu())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.AUXLIQCOMPRODEU_DESAUXLIQCOMPRODEU);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codAuxLiqComProDeu");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, ExeError.AUXLIQCOMPRODEU_CODAUXLIQCOMPRODEU);			
		}
		*/
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el AuxLiqComProDeu. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getAuxLiqComProDeuDAO().update(this);
	}

	/**
	 * Desactiva el AuxLiqComProDeu. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getAuxLiqComProDeuDAO().update(this);
	}
	
	/**
	 * Valida la activacion del AuxLiqComProDeu
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del AuxLiqComProDeu
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}


}
