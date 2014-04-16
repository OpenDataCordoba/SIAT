//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.cas.buss.bean.Solicitud;
import ar.gov.rosario.siat.cas.buss.bean.TipoSolicitud;
import ar.gov.rosario.siat.cas.iface.util.CasError;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.seg.buss.bean.Oficina;
import ar.gov.rosario.siat.seg.buss.bean.UsuarioSiat;
import ar.gov.rosario.siat.seg.buss.dao.SegDAOFactory;
import ar.gov.rosario.siat.seg.iface.util.SegError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente al Area de Origen
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_area")
public class Area extends BaseBO {

	public static final String COD_SIAT = "SIAT";
	public static final String COD_AREA_DEFAULT_SIAT = "DEF_SIAT";
	public static final String COD_EXENCIONES = "DGLT_EX";	
	public static final String COD_EMISION_PADRONES = "DGGR_MP";
	public static final String COD_JUDICIALES = "COBJUD";	
	public static final String COD_CATASTRO = "CAT";
	public static final String COD_COBRANZA_JUDICIALES_PROCURADOR = "CJ_P";
	public static final String COD_OBRAS_PUBLICAS = "OP";
	public static final String COD_DGGR_CA = "DGGR_CA";
	public static final String COD_COBJUD  = "COBJUD";
	public static final String COD_DGEF_FT  = "DGEF_FT";
	public static final String COD_DGGR_BAL = "DGGR_BAL";
	public static final String COD_CER = "CER";
	public static final String COD_COBRANZA_ADMINISTRATIVA = "DGGR_CA";
	public static final String COD_CONCURSO_Y_QUIEBRA = "DGLT_CYQ";
	
	// Propiedades
	private static final long serialVersionUID = 1L;

	@Column(name = "codArea")
	private String codArea;
	
	@Column(name = "desArea") 
	private String desArea;

	@OneToMany()
	@JoinColumn(name="idArea")
	private List<Oficina> listOficina;

	@OneToMany()
	@JoinColumn(name="idArea")
	private List<RecursoArea> listRecursoArea;
		
	
	// Constructores
	public Area(){
		super();
	}
	
	// Getters y setters
	public String getCodArea() {
		return codArea;
	}
	public void setCodArea(String codArea) {
		this.codArea = codArea;
	}
	public String getDesArea() {
		return desArea;
	}
	public void setDesArea(String desArea) {
		this.desArea = desArea;
	}

	public List<RecursoArea> getListRecursoArea() {
		return listRecursoArea;
	}
	public void setListRecursoArea(List<RecursoArea> listRecursoArea) {
		this.listRecursoArea = listRecursoArea;
	}

	// Metodos de clase
	public static Area getById(Long id) {
		return (Area) DefDAOFactory.getAreaDAO().getById(id);
	}
	
	public static Area getByIdNull(Long id) {
		return (Area) DefDAOFactory.getAreaDAO().getByIdNull(id);
	}

	public static Area getByCodigo(String codigo) throws Exception{
		return  DefDAOFactory.getAreaDAO().getByCodigo(codigo);
	}
	
	public static List<Area> getListActivas() {			
		return (ArrayList<Area>) DefDAOFactory.getAreaDAO().getListActiva();
	}
	
	public static List<Area> getListActivasHasTipoSolicitud() throws Exception {			
		return (ArrayList<Area>) DefDAOFactory.getAreaDAO().getListActivasHasTipoSolicitud();
	}
	
	/** 
	 *  Lista de Areas activas ordenadas por Descripcion
	 */
	public static List<Area> getListActivasOrderByDes() throws Exception{			
		return (ArrayList<Area>) DefDAOFactory.getAreaDAO().getListActivasOrderByDes();
	}
	
	/**
	 * Obtiene la lista de Area Origen activas excluyendo a la lista de Area Origen pasada como parametro
	 * @param  listAreaOrigenExcluidas
	 * @return List<AreaOrigen>
	 */
	public static List<Area> getListActivaExcluyendoList(List<Area> listAreaOrigenExcluidas)   {
		List<Long> listIdExcluidas = ListUtilBean.getListLongIdFromListBaseBO(listAreaOrigenExcluidas);
		return (ArrayList<Area>) DefDAOFactory.getAreaDAO().getListActivaExcluyendoListId(listIdExcluidas);
	}
	
	// Metodos de Instancia
	// Validaciones
	public boolean validateCreate() throws Exception {
		//limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;
	}

	public boolean validateUpdate() throws Exception {
		//limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;
	}

	/**
	 * Valida la eliminacion
	 * @author Ivan
	 */
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
		
		if (GenericDAO.hasReference(this, Solicitud.class, "areaOrigen") || GenericDAO.hasReference(this, Solicitud.class, "areaDestino")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTRO_ASOCIADO, DefError.AREA_LABEL, CasError.SOLICITUD_LABEL);
		}

		if (GenericDAO.hasReference(this, TipoSolicitud.class, "areaDestino")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTRO_ASOCIADO, DefError.AREA_LABEL, CasError.TIPOSOLICITUD_LABEL);
		}

		if (GenericDAO.hasReference(this, TipObjImpAreO.class, "areaOrigen")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTRO_ASOCIADO, DefError.AREA_LABEL, DefError.TIPOBJIMPAREO_LABEL);
		}
		
		if (GenericDAO.hasReference(this, UsuarioSiat.class, "area")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTRO_ASOCIADO, DefError.AREA_LABEL, SegError.USUARIOSIAT_LABEL);
		}		
		// TODO: agragar la validacion que notenga relacionado usuario

		/*Ejemplo:
		if (SiatHibernateUtil.hasReference(this, ${BeanRelacionado1}.class, "${bean}")) {
			addRecoverableError(DefError.ATRIBUTO_${BEANRELACIONADO1}_HASREF);
		}
		if (SiatHibernateUtil.hasReference(this, ${BeanRelacionado2}.class, "${bean}")) {
			addRecoverableError(DefError.ATRIBUTO_${BEANRELACIONADO2}_HASREF);
		}*/
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {		
		if (StringUtil.isNullOrEmpty(getCodArea())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.AREA_CODAREA);
			
		}

		if (StringUtil.isNullOrEmpty(getDesArea())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.AREA_DESAREA);
			
		}

		if (hasError()) {
			return false;
		}
		
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codArea");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, DefError.AREA_CODAREA);			
		}

		if (hasError()) {
			return false;
		}

		return true;
	}

	/**
	 * Valida la activacion del area
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del area
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

	// Metodos de negocio
	
	/**
	 * Activa el Dominio Atributo. Previamente valida la activacion. 
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		DefDAOFactory.getAreaDAO().update(this);
	}

	/**
	 * Desactiva el Dominio Atributo. Previamente valida la desactivacion. 
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		DefDAOFactory.getAreaDAO().update(this);
	}

	/**
	 * Modfica una Oficina de esta area
	 */
	public Oficina updateOficina(Oficina oficina) throws Exception {
		// Validaciones 
		if (!oficina.validateUpdate()) {
			return oficina;
		}

		SegDAOFactory.getOficinaDAO().update(oficina);
		return oficina;
	}
	
	/**
	 * Crea una Oficina para esta area
	 */
	public Oficina createOficina(Oficina oficina) throws Exception {
		// Validaciones 
		if (!oficina.validateCreate()) {
			return oficina;
		}

		SegDAOFactory.getOficinaDAO().update(oficina);
		return oficina;
	}

	/**
	 * Elimina una oficina de esta area
	 */
	public Oficina deleteOficina(Oficina oficina) throws Exception {
		SegDAOFactory.getOficinaDAO().delete(oficina);
		return oficina;
	}

	public List<Oficina> getListOficina() {
		return listOficina;
	}

	public void setListOficina(List<Oficina> listOficina) {
		this.listOficina = listOficina;
	}
}
