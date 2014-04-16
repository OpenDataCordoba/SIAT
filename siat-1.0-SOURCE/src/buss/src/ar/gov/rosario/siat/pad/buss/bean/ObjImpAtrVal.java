//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.def.buss.bean.TipObjImpAtr;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Representa el valor de cada ObjetoImponible para cada uno de los atributos
 * del TipoObjetoImponibleRelacionado
 * 
 * @author tecso
 *
 */
@Entity
@Table(name = "pad_objImpAtrVal")
public class ObjImpAtrVal extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	public static final Long ID_SECCION_CATASTRAL = 28L;
	public static final Long ID_AGRUPADOR_CATASTRAL = 25L;
	public static final Long ID_PORCENTAJE_PH = 37L;
	public static final Long ID_UBICACION_TERRENO = 42L;
	public static final Long ID_VALUACION_TERRENO = 48L;
	public static final Long ID_UBICACION_FINCA = 36L;	
	public static final Long ID_VALLIBREREF = 47L;
	public static final Long ID_USOPARCELA = 43L;
	public static final Long ID_RADIO = 38L;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idObjImp") 
	private ObjImp objImp;

	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idTipObjImpAtr") 
	private TipObjImpAtr tipObjImpAtr;
	
	@Column(name="idTipObjImpAtr", insertable=false, updatable=false)
	private Long idTipObjImpAtr;
	
	@Column(name = "strValor")
	private String strValor;
	
	@Column(name = "fechaDesde")
	private Date fechaDesde;
	
	@Column(name = "fechaHasta")
	private Date fechaHasta;

	@Column(name = "fechaNovedad")
	private Date fechaNovedad;

	// Constructores
	public ObjImpAtrVal() {
		super();
	}
	
	// Metodos de Clase
	public static ObjImpAtrVal getById(Long id) {
		return (ObjImpAtrVal) PadDAOFactory.getObjImpAtrValDAO().getById(id);
	}
	
	public static ObjImpAtrVal getByIdNull(Long id) {
		return (ObjImpAtrVal) PadDAOFactory.getObjImpAtrValDAO().getByIdNull(id);
	}
	
	public static List<ObjImpAtrVal> getListByIdObjImp(Long id) {
		return (List<ObjImpAtrVal>) PadDAOFactory.getObjImpAtrValDAO().getListByIdObjImp(id, new Date());
	}
	
	public static List<ObjImpAtrVal> getListByIdObjImpAtrVal(Long idTipObjImpAtr, Long idObjImp) {
		return (List<ObjImpAtrVal>) PadDAOFactory.getObjImpAtrValDAO().getListByIdObjImpAtrVal(idTipObjImpAtr, idObjImp, new Date());
	}
	
	public static ObjImpAtrVal getVigenteByIdObjImpAtrVal(Long idTipObjImpAtr, Long idObjImp) {
		return (ObjImpAtrVal) PadDAOFactory.getObjImpAtrValDAO().getVigenteByIdObjImpAtrVal(idTipObjImpAtr, idObjImp, new Date());
	}
	
	public static ObjImpAtrVal getVigenteByIdObjImpAtrValAndValue(Long idTipObjImpAtr, Long idObjImp, String valor) {
		return (ObjImpAtrVal) PadDAOFactory.getObjImpAtrValDAO().getVigenteByIdObjImpAtrValAndValue(idTipObjImpAtr, idObjImp, new Date(), valor);
	}
	
	public static ObjImpAtrVal getVigenteByIdObjImpAtrVal(Long idTipObjImpAtr, Long idObjImp, Date fechaVigencia) {
		return (ObjImpAtrVal) PadDAOFactory.getObjImpAtrValDAO().getVigenteByIdObjImpAtrVal(idTipObjImpAtr, idObjImp, fechaVigencia);
	}

	/**
	 * 
	 * Recupera los Atributos del Objeto Imponible correspondiente al id y 
	 * donde los ids de TipObjImpAtr esten dentro de la lista recibida 
	 * 
	 * 
	 * @author Cristian
	 * @param id
	 * @param listIds
	 * @return
	 */
	public static List<ObjImpAtrVal> getListByIdObjImpIdsTipObjImpAtr(Long id, Long[] listIds) {
		return (List<ObjImpAtrVal>) PadDAOFactory.getObjImpAtrValDAO().getListByIdObjImpIdsTipObjImpAtr(id, listIds, new Date());
	}
	
	 /** Obtiene una lista de Atributo-Valor (de tipo especificado en listIdsTipObjImpAtr) 
	  * para cada Objeto Imponible con id en la lista listIdsObjImp
	  * 
	  * @param listIdsObjImp
	  * @param listIdsTipObjImpAtr
	  * @return List<ObjImpAtrVal>
	  */
	public static List<ObjImpAtrVal> getListObjImpAtrValByListId(Long[] listIdsObjImp, Long[] listIdsTipObjImpAtr) {
		return (List<ObjImpAtrVal>) PadDAOFactory.getObjImpAtrValDAO().getListObjImpAtrValByListIdDAO(listIdsObjImp, listIdsTipObjImpAtr);
	}
	
	// Getters y Setters
	public TipObjImpAtr getTipObjImpAtr() {
		return tipObjImpAtr;
	}
	public void setTipObjImpAtr(TipObjImpAtr tipObjImpAtr) {
		this.tipObjImpAtr = tipObjImpAtr;
	}
	public ObjImp getObjImp() {
		return objImp;
	}
	public void setObjImp(ObjImp objImp) {
		this.objImp = objImp;
	}
	public String getStrValor() {
		return strValor;
	}
	public void setStrValor(String strValor) {
		this.strValor = strValor;
	}

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

	public Date getFechaNovedad() {
		return fechaNovedad;
	}

	public void setFechaNovedad(Date fechaNovedad) {
		this.fechaNovedad = fechaNovedad;
	}
	
	public Long getIdTipObjImpAtr() {
		return idTipObjImpAtr;
	}

	public void setIdTipObjImpAtr(Long idTipObjImpAtr) {
		this.idTipObjImpAtr = idTipObjImpAtr;
	}

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

	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de Requeridos
		
		if (hasError()) {
			return false;
		}
		// Validaciones de Unicidad
	
		// Otras Validaciones
	
		
		return !hasError();
	}
	/**
	 * Valida la eliminacion
	 * @author
	 */
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
		
		if (hasError()) {
			return false;
		}

		return !hasError();
	}
}
