//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OrderBy;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.util.DefError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a los Codigos de 
 * Emision
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_codEmi")
public class CodEmi extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idRecurso") 
	private Recurso recurso;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idTipCodEmi") 
	private TipCodEmi tipCodEmi;

	@Column(name = "nombre")
	private String nombre;

	@Column(name = "descripcion")
	private String descripcion;

	@Column(name = "codigo")
	private String codigo;

	@Column(name = "fechaDesde")
	private Date fechaDesde;

	@Column(name = "fechaHasta")
	private Date fechaHasta;

	@OneToMany(mappedBy="codEmi", fetch=FetchType.LAZY)
	@JoinColumn(name = "idCodEmi")
	@OrderBy(clause="fechaUltMdf")
	private List<HistCodEmi> listHistCodEmi;
	
	// Constructores
	public CodEmi(){
		super();
	}
	
	public CodEmi(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static CodEmi getById(Long id) {
		return (CodEmi) DefDAOFactory.getCodEmiDAO().getById(id);
	}
	
	public static CodEmi getByIdNull(Long id) {
		return (CodEmi) DefDAOFactory.getCodEmiDAO().getByIdNull(id);
	}
	
	public static List<CodEmi> getList() {
		return (ArrayList<CodEmi>) DefDAOFactory.getCodEmiDAO().getList();
	}
	
	public static List<CodEmi> getListActivos() {			
		return (ArrayList<CodEmi>) DefDAOFactory.getCodEmiDAO().getListActiva();
	}

	public static List<CodEmi> getListActivosBy(Long idTipCodEmi) {			
		return (ArrayList<CodEmi>) DefDAOFactory.getCodEmiDAO().getListActivosBy(idTipCodEmi);
	}

	// Getters y setters
	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public TipCodEmi getTipCodEmi() {
		return tipCodEmi;
	}

	public void setTipCodEmi(TipCodEmi tipCodEmi) {
		this.tipCodEmi = tipCodEmi;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
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

	public List<HistCodEmi> getListHistCodEmi() {
		return listHistCodEmi;
	}

	public void setListHistCodEmi(List<HistCodEmi> listHistCodEmi) {
		this.listHistCodEmi = listHistCodEmi;
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
		
		//	Validaciones        
		if (getTipCodEmi() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.CODEMI_TIPCODEMI);
		}

		if (getTipCodEmi() != null && getTipCodEmi().getId().equals(TipCodEmi.ID_PROGRAMA)) {
			if (getRecurso() == null) {
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.CODEMI_RECURSO);
			}
		}

		if (StringUtil.isNullOrEmpty(getNombre())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.CODEMI_NOMBRE);
		}

		if (StringUtil.isNullOrEmpty(getDescripcion())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.CODEMI_DESCRIPCION);
		}

		if (getFechaDesde() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.CODEMI_FECHADESDE);
		}

		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("nombre");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, DefError.CODEMI_NOMBRE);			
		}
		
		return true;
	}		
	
	// Metodos de negocio
	
	/**
	 * Activa el CodEmi. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		DefDAOFactory.getCodEmiDAO().update(this);
	}

	/**
	 * Desactiva el CodEmi. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		DefDAOFactory.getCodEmiDAO().update(this);
	}
	
	/**
	 * Valida la activacion del CodEmi
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del CodEmi
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

	// ---> ABM HistCodEmi
	public HistCodEmi createHistCodEmi(HistCodEmi histCodEmi) throws Exception {

		// Validaciones de negocio
		if (!histCodEmi.validateCreate()) {
			return histCodEmi;
		}
		
		histCodEmi.setCodEmi(this);
		DefDAOFactory.getEmiMatDAO().update(histCodEmi);

		return histCodEmi;
	}

	public HistCodEmi deleteHistCodEmi(HistCodEmi histCodEmi) throws Exception {

		// Validaciones de negocio
		if (!histCodEmi.validateDelete()) {
			return histCodEmi;
		}
		
		DefDAOFactory.getEmiMatDAO().delete(histCodEmi);

		return histCodEmi;
	}
	// <--- ABM HistCodEmi

	/**
	 * Retorna la lista de atributos utilizados en el algoritmo
	 */
	public List<String> getAtributosCodigo() {
		String code = this.getCodigo();
		List<String> listResult = new ArrayList<String>(); 

		Pattern pattern = Pattern.compile("atributos.getValor\\(\"(.*?)\"\\)");

        Matcher matcher = pattern.matcher(code);

        int pos = 0;
        while (matcher.find(pos)) {
          listResult.add(matcher.group(1));
          pos = matcher.end();
        }

        return listResult;
	}
	
}
