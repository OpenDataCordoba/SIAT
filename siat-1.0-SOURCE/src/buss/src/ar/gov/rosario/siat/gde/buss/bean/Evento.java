//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;


/**
 * Bean correspondiente a Evento
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_evento")
public class Evento extends BaseBO {
	
	private static final long serialVersionUID = 1L;

	public static final Integer COD_ENTREGA_DEUDA_FEHACIENTE = 1;
	public static final Integer COD_INTIMACION_EXTRAJUD = 3;
	public static final Integer COD_ENTREGA_CONST_DEUDA = 6;
	public static final Integer COD_PRESENTACION_DEMANDA = 7;
	public static final Integer COD_SENTENCIA_NRO_Y_FECHA = 24;
	public static final Integer COD_SENTENCIA_NOTIFICACION = 25;
	

	
	@Column(name = "codigo")
	private Integer codigo;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idEtapaProcesal") 
	private EtapaProcesal etapaProcesal;

	@Column(name = "afectaCadJui")
	private Integer afectaCadJui;
	
	@Column(name = "afectaPresSen")
	private Integer afectaPresSen;
	
	@Column(name = "esUnicoEnGesJud")
	private Integer esUnicoEnGesJud;
		
	@Column(name = "predecesores")
	private String predecesores;
	
	
	// Constructores
	public Evento(){
		super();
	}
	
	public Evento(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static Evento getById(Long id) {
		return (Evento) GdeDAOFactory.getEventoDAO().getById(id);
	}
	
	public static Evento getByIdNull(Long id) {
		return (Evento) GdeDAOFactory.getEventoDAO().getByIdNull(id);
	}
	
	public static List<Evento> getList() {
		return (ArrayList<Evento>) GdeDAOFactory.getEventoDAO().getList();
	}
	
	public static List<Evento> getListActivos() {			
		return (ArrayList<Evento>) GdeDAOFactory.getEventoDAO().getListActiva();
	}
	
	public static Evento getByCodigo(Integer codigo) throws Exception {			
		return  GdeDAOFactory.getEventoDAO().getByCodigo(codigo);
	}
	
	public static  List<Evento> getComplementByCodigo(Integer codigo) throws Exception {			
		return  GdeDAOFactory.getEventoDAO().getComplementByCodigo(codigo);
	}
	
	//	 Getters y setters
	public Integer getAfectaCadJui() {
		return afectaCadJui;
	}

	public void setAfectaCadJui(Integer afectaCadJui) {
		this.afectaCadJui = afectaCadJui;
	}

	public Integer getAfectaPresSen() {
		return afectaPresSen;
	}

	public void setAfectaPresSen(Integer afectaPresSen) {
		this.afectaPresSen = afectaPresSen;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public EtapaProcesal getEtapaProcesal() {
		return etapaProcesal;
	}

	public void setEtapaProcesal(EtapaProcesal etapaProcesal) {
		this.etapaProcesal = etapaProcesal;
	}

	public String getPredecesores() {
		return predecesores;
	}

	public void setPredecesores(String predecesores) {
		this.predecesores = predecesores;
	}
	
	@Transient
	public String getAfectaCadJuiSiNo(){
		return SiNo.getById(this.getAfectaCadJui()).getValue();
	}

	@Transient
	public String getAfectaPresSenSiNo(){
		return SiNo.getById(this.getAfectaPresSen()).getValue();
	}
	
	public Integer getEsUnicoEnGesJud() {
		return esUnicoEnGesJud;
	}

	public void setEsUnicoEnGesJud(Integer esUnicoEnGesJud) {
		this.esUnicoEnGesJud = esUnicoEnGesJud;
	}

	// Validaciones 
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de unique
		if (getByCodigo(getCodigo()) != null ) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, GdeError.EVENTO_CODIGO);
		}
		
		if (hasError()) {
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
		if (getCodigo()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.EVENTO_CODIGO);
		}

		if (StringUtil.isNullOrEmpty(getDescripcion())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.EVENTO_DESCRIPCION);
		}
		
		if (getEtapaProcesal() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.EVENTO_ETAPAPROCESAL);
		}
		
		if (getAfectaCadJui() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.EVENTO_AFECTACADJUI);
		}
		
		if (getAfectaPresSen()== null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.EVENTO_AFECTAPRESSEN);
		}
		
		if (getEsUnicoEnGesJud()== null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.EVENTO_ESUNICOENGESJUD);
		}
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el Evento. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getEventoDAO().update(this);
	}

	/**
	 * Desactiva el Evento. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getEventoDAO().update(this);
	}
	
	/**
	 * Valida la activacion del Evento
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del Evento
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/** 
	 * Retorna la lista de eventos predecesores de un evento particular.
	 * Si Predecesores es null, retorna null.
	 * @return List<Evento>
	 */

	public List<Evento> getListEventosPredecesores() throws Exception {
		
		List<Evento> listEventosPredecesores = null;
		
		if (!StringUtil.isNullOrEmpty(getPredecesores())) {
			
			listEventosPredecesores = new ArrayList<Evento>();
			
			//Parseamos  la lista de los Predecesores
			String[] listCodEvento = getPredecesores().split(",");
		
			for (String cod: listCodEvento) {
				listEventosPredecesores.add(
						//Buscamos por Codigo
						GdeDAOFactory.getEventoDAO().getByCodigo
							(Evento.getById(Long.parseLong(cod)).getCodigo()));
			}
		}
	
		return listEventosPredecesores;
	}
	
	public String getStrPredecesoras() throws Exception {
		String strPred=""; 

		if (!StringUtil.isNullOrEmpty(getPredecesores())) {

			List<Evento> listEventosPredecesores = this.getListEventosPredecesores();
			
            for (Evento e:listEventosPredecesores){

				strPred = e.getCodigo()+"-"+e.getDescripcion()+" , ";
			}
		}

		return strPred;
	}
	

	/** 
	 * Agrega un evento predecesor a la String Predecesores.
 	 * @return void
	 */
	public void agregarPredecesor(Evento evento) {
		setPredecesores(getPredecesores() + evento.getCodigo().toString());
	}
	
}
