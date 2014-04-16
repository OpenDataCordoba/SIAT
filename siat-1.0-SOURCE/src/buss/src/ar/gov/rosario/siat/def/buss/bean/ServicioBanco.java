//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

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

import org.hibernate.annotations.OrderBy;

import ar.gov.rosario.siat.bal.buss.bean.Asentamiento;
import ar.gov.rosario.siat.bal.buss.bean.Partida;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.gde.buss.bean.SerBanDesGen;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pro.buss.bean.Proceso;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;


/**
 * Bean correspondiente a Servicio Banco
 * 
 * @author tecso
 */

@Entity
@Table(name = "def_servicioBanco")
public class ServicioBanco extends BaseBO {
	private static final long serialVersionUID = 1L;
	
	public static final Long ID_SERVICIO_BANCO_TGI = 1L;
	public static final Long ID_SERVICIO_BANCO_CDM = 2L;
	
	public static final Long TIPO_ASENTAMIENTO_SIAT = 1L;
	public static final Long TIPO_ASENTAMIENTO_NO_SIAT = 2L;
	public static final Long TIPO_ASENTAMIENTO_MIXTO = 3L;
	
	
	public static final String COD_TGI = "81";
	public static final String COD_CDM = "82";
	public static final String COD_ETUR = "84";
	public static final String COD_CEMENTERIO = "86";
	public static final String COD_DREI = "83";
	public static final String COD_OTROS_TRIBUTOS = "85";
	public static final String COD_SELLADO = "88";
	public static final String COD_OTROS = "87";
	
	
	@Column(name = "codServicioBanco")
	private String codServicioBanco;
	@Column(name = "desServicioBanco")
	private String desServicioBanco;
	
	@Column(name = "esAutoliquidable")
	private Integer esAutoliquidable;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idPartidaIndet")
	private Partida partidaIndet;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idProcesoAse") 
	private Proceso procesoAse;
	
	@Column(name = "tipoAsentamiento")
	private Long tipoAsentamiento;

	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idParCuePue")
	private Partida parCuePue;

	@OneToMany()
	@JoinColumn(name="idServicioBanco")
	@OrderBy(clause="fechaDesde")
	private List<SerBanRec> listSerBanRec;
	
	@OneToMany()
	@JoinColumn(name="idServicioBanco")
	@OrderBy(clause="fechaDesde")
	private List<SerBanDesGen> listSerBanDesGen;
	
	// Constructores
	public ServicioBanco(){
		super();
	}
	// Getters y Setters
	public String getCodServicioBanco(){
		return codServicioBanco;
	}
	public void setCodServicioBanco(String codServicioBanco){
		this.codServicioBanco = codServicioBanco;
	}
	public String getDesServicioBanco(){
		return desServicioBanco;
	}
	public void setDesServicioBanco(String desServicioBanco){
		this.desServicioBanco = desServicioBanco;
	}
	public List<SerBanRec> getListSerBanRec(){
		return listSerBanRec;
	}
	public void setListSerBanRec(List<SerBanRec> listSerBanRec){
		this.listSerBanRec = listSerBanRec;
	}
	public List<SerBanDesGen> getListSerBanDesGen(){
		return listSerBanDesGen;
	}
	public void setListSerBanDesGen(List<SerBanDesGen> listSerBanDesGen){
		this.listSerBanDesGen = listSerBanDesGen;
	}	
	public Proceso getProcesoAse() {
		return procesoAse;
	}
	public void setProcesoAse(Proceso procesoAse) {
		this.procesoAse = procesoAse;
	}
	public Partida getPartidaIndet() {
		return partidaIndet;
	}
	public void setPartidaIndet(Partida partidaIndet) {
		this.partidaIndet = partidaIndet;
	}
	public Integer getEsAutoliquidable() {
		return esAutoliquidable;
	}
	public void setEsAutoliquidable(Integer esAutoliquidable) {
		this.esAutoliquidable = esAutoliquidable;
	}
	public Long getTipoAsentamiento() {
		return tipoAsentamiento;
	}
	public void setTipoAsentamiento(Long tipoAsentamiento) {
		this.tipoAsentamiento = tipoAsentamiento;
	}
	public Partida getParCuePue() {
		return parCuePue;
	}
	public void setParCuePue(Partida parCuePue) {
		this.parCuePue = parCuePue;
	}
	
	// Metodos de clase	
	public static List<ServicioBanco> getVigentes(Recurso recurso){
		return DefDAOFactory.getServicioBancoDAO().getVigente(recurso);
	}
	
	public static ServicioBanco getById(Long id) {
		return (ServicioBanco) DefDAOFactory.getServicioBancoDAO().getById(id);
	}
	
	public static ServicioBanco getByIdNull(Long id) {
		return (ServicioBanco) DefDAOFactory.getServicioBancoDAO().getByIdNull(id);
	}
	
	public static ServicioBanco getByCodigo(String codigo) {
		return (ServicioBanco) DefDAOFactory.getServicioBancoDAO().getByCodigo(codigo);
	}
	
	public static List<ServicioBanco> getList() {
		return (ArrayList<ServicioBanco>) DefDAOFactory.getServicioBancoDAO().getList();
	}
	
	public static List<ServicioBanco> getListActivos() {			
		return (ArrayList<ServicioBanco>) DefDAOFactory.getServicioBancoDAO().getListActiva();
	}
	
	public static List<ServicioBanco> getListPoseeRecurso() {			
		return (ArrayList<ServicioBanco>) DefDAOFactory.getServicioBancoDAO().getListPoseeRecurso();
	}
	
	/**
	 * Devuelve los servicioBanco vigentes para una lista de recursos a la fecha del dia
	 * 
	 * @param listRecurso
	 * @return
	 */
	public static List<ServicioBanco> getListVigenteByListRecurso(List<Recurso> listRecurso) throws Exception{
		return DefDAOFactory.getServicioBancoDAO().getListVigenteByListRecurso(listRecurso);
	}
	
	/**
	 * Devuelve el recursos vigentes a la fecha del dia que envien a judicial 
	 * 
	 * @return
	 */
	public List<Recurso> getListRecursoVigenteQueEnviaJudicial() throws Exception{
		return DefDAOFactory.getServicioBancoDAO().getListRecursoVigenteQueEnviaJudicial(this);
	}
	
	// Metodos de Instancia
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
		
		UniqueMap uniqueMap = new UniqueMap();

		//Validaciones de Requeridos
		if (StringUtil.isNullOrEmpty(getDesServicioBanco())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.SERVICIOBANCO_DESSERVICIOBANCO);
		}
		if (StringUtil.isNullOrEmpty(getCodServicioBanco())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.SERVICIOBANCO_CODSERVICIOBANCO);
		}
		if (getPartidaIndet() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.SERVICIOBANCO_PARTIDAINDET);
		}
		if(!SiNo.getEsValido(getEsAutoliquidable())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.SERVICIOBANCO_ESAUTOLIQUIDABLE);
		}
		if (getTipoAsentamiento() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.SERVICIOBANCO_TIPOASENTAMIENTO);
		}
		
		if (hasError()) {
			return false;
		}
		// Validaciones de Unicidad
		uniqueMap.addString("codServicioBanco");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)){
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, DefError.SERVICIOBANCO_CODSERVICIOBANCO);
		}
		
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
		
		if (GenericDAO.hasReference(this, SerBanRec.class, "servicioBanco")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.SERVICIOBANCO_LABEL , DefError.SERBANREC_LABEL);
			
		}
		
		if (GenericDAO.hasReference(this, SerBanDesGen.class, "servicioBanco")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.SERVICIOBANCO_LABEL , GdeError.SERBANDESGEN_LABEL);
			
		}
		
		if (GenericDAO.hasReference(this, Asentamiento.class, "servicioBanco")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.SERVICIOBANCO_LABEL , BalError.ASENTAMIENTO_LABEL);
			
		}

		if (hasError()) {
			return false;
		}

		return !hasError();
	}

	//	 Metodos de negocio
	// 	 Administracion de Servicio Banco Recursos
	public SerBanRec createSerBanRec(SerBanRec serBanRec) throws Exception {
		
		// Validaciones de negocio
		if (!serBanRec.validateCreate()) {
			return serBanRec;
		}

		DefDAOFactory.getSerBanRecDAO().update(serBanRec);
		
		return serBanRec;
	}	

	public SerBanRec updateSerBanRec(SerBanRec serBanRec) throws Exception {
		
		// Validaciones de negocio
		if (!serBanRec.validateUpdate()) {
			return serBanRec;
		}

		DefDAOFactory.getSerBanRecDAO().update(serBanRec);
		
		return serBanRec;
	}	

	public SerBanRec deleteSerBanRec(SerBanRec serBanRec) throws Exception {
		
		// Validaciones de negocio
		if (!serBanRec.validateDelete()) {
			return serBanRec;
		}
		DefDAOFactory.getSerBanRecDAO().delete(serBanRec);
		
		return serBanRec;
	}	
	
	// Administracion de Servicio Banco Descuentos Generales
	public SerBanDesGen createSerBanDesGen(SerBanDesGen serBanDesGen) throws Exception {
		
		// Validaciones de negocio
		if (!serBanDesGen.validateCreate()) {
			return serBanDesGen;
		}

		GdeDAOFactory.getSerBanDesGenDAO().update(serBanDesGen);
		
		return serBanDesGen;
	}	

	public SerBanDesGen updateSerBanDesGen(SerBanDesGen serBanDesGen) throws Exception {
		
		// Validaciones de negocio
		if (!serBanDesGen.validateUpdate()) {
			return serBanDesGen;
		}

		GdeDAOFactory.getSerBanDesGenDAO().update(serBanDesGen);
		
		return serBanDesGen;
	}	

	public SerBanDesGen deleteSerBanDesGen(SerBanDesGen serBanDesGen) throws Exception {
		
		// Validaciones de negocio
		if (!serBanDesGen.validateDelete()) {
			return serBanDesGen;
		}

		GdeDAOFactory.getSerBanDesGenDAO().delete(serBanDesGen);
		
		return serBanDesGen;
	}	

	
	/**
	 * Activa el Servicio Banco. Previamente valida la activacion 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		DefDAOFactory.getServicioBancoDAO().update(this);
	}

	/**
	 * Desactiva el Servicio Banco. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		DefDAOFactory.getServicioBancoDAO().update(this);
	}
	/**
	 * Pasa el estado a creado 
	 *
	 */
	public void creado(){
		this.setEstado(Estado.CREADO.getId());
		DefDAOFactory.getServicioBancoDAO().update(this);
	}	
	
	
	
	/**
	 * Valida la activacion del Servicio Banco
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del Servicio Banco
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

	/**
	 * Obtiene el Descuento General asociado al Servicio Banco vigente para la fecha indicada.
	 * TODO (arreglar para obtener el mejor de los que encuentra)
	 * @param fecha
	 * @return
	 */
	public SerBanDesGen obtenerDescuentoGeneralVigente(Date fecha){
		return (SerBanDesGen) GdeDAOFactory.getSerBanDesGenDAO().getVigenteBySerBanYFecha(this, fecha);
	}
	
	public String getDesRecursos (){
		String desRecursos = "";
		int i=0;
		for (SerBanRec serBancRec : getListSerBanRec()){
			if (i>0){
				desRecursos+= " - ";
			}
			desRecursos += serBancRec.getRecurso().getDesRecurso();
			i++;
		}
		
		return desRecursos;
	}
	
	/**
	 * Devuelve la lista de recursos que posea el servicio banco.
	 * 
	 * @return
	 */
	public List<Recurso> getListRecursos(){
		List<Recurso> listRecursos = new ArrayList<Recurso>();
		for (SerBanRec serBancRec : getListSerBanRec()){
			listRecursos.add(serBancRec.getRecurso());
		}
		return listRecursos;
	}
	
}
