//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.util.DefError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a Tipo Objeto Imponible Area Origen
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_tipObjImpAreO")
public class TipObjImpAreO extends BaseBO {
	
	// Propiedades
	private static final long serialVersionUID = 1L;

	@ManyToOne(optional=false) 
    @JoinColumn(name="idAreaOrigen")
	private Area areaOrigen;

	@ManyToOne(optional=false)
    @JoinColumn(name="idTipObjImp")
	private TipObjImp tipObjImp;

	// Constructores
	public TipObjImpAreO(){
		super();
	}
	
	// Getters y setters	
	public Area getAreaOrigen() {
		return areaOrigen;
	}
	public void setAreaOrigen(Area areaOrigen) {
		this.areaOrigen = areaOrigen;
	}
	public TipObjImp getTipObjImp() {
		return tipObjImp;
	}
	public void setTipObjImp(TipObjImp tipObjImp) {
		this.tipObjImp = tipObjImp;
	}
	
	// Metodos de clase

	/**
	 * Obtiene el Area de Origen del Tipo de Objeto Imponible a partir de su Id.
	 * @param id
	 * @return TipObjImpAreO
	 */
	public static TipObjImpAreO getById(Long id) {
		return (TipObjImpAreO) DefDAOFactory.getTipObjImpAreODAO().getById(id);
	}
	
	// Metodos de Instancia
	
	// Validaciones

	/**
	 * Valida la creacion del Area de Origen del Tipo de Objeto Imponible.
	 * Requeridos: Tipo de objeto imponible y el Area de Origen.
	 * Valida que: el Tipo de objeto imponible y el Area de Origen sean unicos
	 * @return boolean
	 * @throws Exception
	 */
	public boolean validateCreate()  throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		if (this.getTipObjImp() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.TIPOBJIMP_LABEL);
		}
		
		if(this.getAreaOrigen() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.AREA_LABEL);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unicidad

		UniqueMap uniqueMap = new UniqueMap();
		// tipoObjetoImponible + areaOrigen unicos

		uniqueMap.addEntity("tipObjImp").addEntity("areaOrigen");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)){
			addRecoverableError(BaseError.MSG_CAMPO_UNICOS2, new Object[]{DefError.TIPOBJIMP_LABEL,DefError.AREA_LABEL});
		}

		return (!hasError());
	}

	// Metodos de negocio

	/**
	 * Activa el Area de Origen del Tipo de Objeto Imponible.
	 * Setea el estado a activo.
	 */
	public void activar(){
		
		this.setEstado(Estado.ACTIVO.getId());
		DefDAOFactory.getTipObjImpAreODAO().update(this);
	}

	/**
	 * Activa el Area de Origen del Tipo de Objeto Imponible.
	 * Setea el estado a inactivo.
	 */
	public void desactivar(){

		this.setEstado(Estado.INACTIVO.getId());
		DefDAOFactory.getTipObjImpAreODAO().update(this);
	}


}
