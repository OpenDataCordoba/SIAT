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
import javax.persistence.Table;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.LiqComProVO;
import ar.gov.rosario.siat.gde.iface.model.ProcuradorVO;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a LiqComPro
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_liqcompro")
public class LiqComPro extends BaseBO {

	private static Logger log = Logger.getLogger(LiqComPro.class);
	
	private static final long serialVersionUID = 1L;

	public static final long ID_LIQCOMPRO_MIGRACION = 999L;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idLiqCom")
	private LiqCom liqCom;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idProcurador")
	private Procurador procurador;

	@Column(name = "fechaLiquidacion")
	private Date fechaLiquidacion;

	@Column(name = "importeAplicado")
	private Double importeAplicado;

	@Column(name = "importeComision")
	private Double importeComision;

	@Column(name = "idLiqComVueAtr")
	private Long idLiqComVueAtr;

	// <#Propiedades#>

	// Constructores
	public LiqComPro() {
		super();
		// Seteo de valores default
	}

	public LiqComPro(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static LiqComPro getById(Long id) {
		return (LiqComPro) GdeDAOFactory.getLiqComProDAO().getById(id);
	}

	public static LiqComPro getByIdNull(Long id) {
		return (LiqComPro) GdeDAOFactory.getLiqComProDAO().getByIdNull(id);
	}

	public static List<LiqComPro> getList() {
		return (ArrayList<LiqComPro>) GdeDAOFactory.getLiqComProDAO().getList();
	}

	public static List<LiqComPro> getListActivos() {
		return (ArrayList<LiqComPro>) GdeDAOFactory.getLiqComProDAO()
				.getListActiva();
	}

	public static List<LiqComPro> getListByLiqCom(Long idLiqCom) {
		return (ArrayList<LiqComPro>) GdeDAOFactory.getLiqComProDAO().getListByLiqCom(idLiqCom);
	}

	// Getters y setters

	public LiqCom getLiqCom() {
		return liqCom;
	}

	public void setLiqCom(LiqCom liqCom) {
		this.liqCom = liqCom;
	}

	public Procurador getProcurador() {
		return procurador;
	}

	public void setProcurador(Procurador procurador) {
		this.procurador = procurador;
	}

	public Date getFechaLiquidacion() {
		return fechaLiquidacion;
	}

	public void setFechaLiquidacion(Date fechaLiquidacion) {
		this.fechaLiquidacion = fechaLiquidacion;
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

	public Long getIdLiqComVueAtr() {
		return idLiqComVueAtr;
	}

	public void setIdLiqComVueAtr(Long idLiqComVueAtr) {
		this.idLiqComVueAtr = idLiqComVueAtr;
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
		// limpiamos la lista de errores
		clearError();

		// <#ValidateDelete#>

		if (hasError()) {
			return false;
		}

		return true;
	}

	private boolean validate() throws Exception {
/*
		// Validaciones
		if (StringUtil.isNullOrEmpty(getCodLiqComPro())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					GdeError.LIQCOMPRO_CODLIQCOMPRO);
		}

		if (StringUtil.isNullOrEmpty(getDesLiqComPro())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					GdeError.LIQCOMPRO_DESLIQCOMPRO);
		}

		if (hasError()) {
			return false;
		}

		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codLiqComPro");
		if (!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO,
					GdeError.LIQCOMPRO_CODLIQCOMPRO);
		}
*/
		return true;
	}

	// Metodos de instancia
	/**
	 * Obtiene la lista de AuxLiqComProDeu asociadas
	 */
	public List<AuxLiqComProDeu> getListAuxLiqComProDeu() {
		return AuxLiqComProDeu.getByIdLiqComPro(this.getId());		
	}
	
	
	
	// Metodos de negocio

	// Metodos de clase
	/**
	 * Obtiene la lista de LiqComPro asociadas (una por cada procurador que tenga la liqCom), por idLiqCom
	 */
	public static List<LiqComPro> getByIdLiqCom(Long id) {
		return GdeDAOFactory.getLiqComProDAO().getByIdLiqCom(id);		
	}
	
	public static LiqComPro get(LiqCom liqCom, Procurador procurador){
		return GdeDAOFactory.getLiqComProDAO().get(liqCom, procurador);
	}
	
	/**
	 * Activa el LiqComPro. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getLiqComProDAO().update(this);
	}

	/**
	 * Desactiva el LiqComPro. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getLiqComProDAO().update(this);
	}

	/**
	 * Valida la activacion del LiqComPro
	 * 
	 * @return boolean
	 */
	private boolean validateActivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}

	/**
	 * Valida la desactivacion del LiqComPro
	 * 
	 * @return boolean
	 */
	private boolean validateDesactivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}

	/**
	 * Hace toVO(0, false) y setea el procurador, con el mismo metodo
	 * @return
	 * @throws Exception
	 */
	public LiqComProVO toVoForView() throws Exception{
		LiqComProVO liqComProVO = (LiqComProVO) this.toVO(0, false);
		liqComProVO.setProcurador((ProcuradorVO) this.procurador.toVO(0, false));
		return liqComProVO;
	}
	
	// Metodos para ABM LiqComProDeu
	public LiqComProDeu createLiqComProDeu(LiqComProDeu liqComProDeu) throws Exception {
		// Validaciones de negocio
		if (!liqComProDeu.validateCreate()) {
			return liqComProDeu;
		}
		
		// graba liqComProDeu
		GdeDAOFactory.getLiqComProDeuDAO().update(liqComProDeu);

		return liqComProDeu;
	}
	
	public LiqComProDeu updateLiqComProDeu(LiqComProDeu liqComProDeu) throws Exception {
		// Validaciones de negocio
		if (!liqComProDeu.validateUpdate()) {
			return liqComProDeu;
		}

		GdeDAOFactory.getLiqComProDeuDAO().update(liqComProDeu);
		
		return liqComProDeu;
	}


	// FIN Metodos para ABM LiqComProDeu	
	
	// Metodos para ABM AuxLiqComProDeu
	public AuxLiqComProDeu createAuxLiqComProDeu(AuxLiqComProDeu auxLiqComProDeu) throws Exception {
		// Validaciones de negocio
		if (!auxLiqComProDeu.validateCreate()) {
			return auxLiqComProDeu;
		}
		
		// graba liqCom
		GdeDAOFactory.getAuxLiqComProDeuDAO().update(auxLiqComProDeu);

		return auxLiqComProDeu;
	}
	
	public AuxLiqComProDeu updateAuxLiqComProDeu(AuxLiqComProDeu auxLiqComProDeu) throws Exception {
		// Validaciones de negocio
		if (!auxLiqComProDeu.validateUpdate()) {
			return auxLiqComProDeu;
		}

		GdeDAOFactory.getAuxLiqComProDeuDAO().update(auxLiqComProDeu);
		
		return auxLiqComProDeu;
	}

	public AuxLiqComProDeu deleteAuxLiqComProDeu(AuxLiqComProDeu auxLiqComProDeu) throws Exception {
		// Validaciones de negocio
		if (!auxLiqComProDeu.validateDelete()) {
			return auxLiqComProDeu;
		}

		GdeDAOFactory.getAuxLiqComProDeuDAO().delete(auxLiqComProDeu);
		
		return auxLiqComProDeu;
	}

	/**
	 * Elimina la lista de AuxLiqComProDeu asociada
	 * @throws Exception
	 */
	public void deleteListAuxLiqComProDeu() throws Exception{		
		List<AuxLiqComProDeu> listAux = getListAuxLiqComProDeu();
		log.debug("Va a eliminar la lista de AuxLiqComProDeu - cant:"+listAux.size());
		for(AuxLiqComProDeu aux: listAux){
			log.debug("Va a eliminar la AuxLiqComProDeu");
			GdeDAOFactory.getAuxLiqComProDeuDAO().delete(aux);
		}
	}	
	// FIN Metodos para ABM AuxLiqComProDeu

	// <#MetodosBeanDetalle#>
}
