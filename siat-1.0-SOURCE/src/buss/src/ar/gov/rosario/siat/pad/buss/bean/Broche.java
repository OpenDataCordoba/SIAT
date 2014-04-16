//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.model.BroCueVO;
import ar.gov.rosario.siat.pad.iface.model.BrocheVO;
import ar.gov.rosario.siat.pad.iface.model.RepartidorVO;
import ar.gov.rosario.siat.pad.iface.model.TipoBrocheVO;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a Broche
 * 
 * @author tecso
 */
@Entity
@Table(name = "pad_broche")
public class Broche extends BaseBO{

	private static final long serialVersionUID = 1L;

	public static final Long ID_BROCHE_FUERA_DE_ROSARIO = 900L;
	
	@Column(name = "desBroche")
	private String desBroche;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idRecurso") 
	private Recurso recurso;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idTipoBroche") 
	private TipoBroche tipoBroche;
	
	@Column(name = "strDomicilioEnvio")
	private String strDomicilioEnvio;

	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idRepartidor") 
	private Repartidor repartidor;
	
	@Column(name = "telefono")
	private String telefono;
	
	// Listas de Entidades Relacionadas con Broche
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="idBroche")
	@OrderBy(clause="fechaAlta")
	private List<BroCue> listBroCue;

	@Column(name = "exentoEnvioJud")
	private Integer exentoEnvioJud;
	
	@Column(name = "permiteImpresion")
	private Integer permiteImpresion;
	
	// Constructores	
	public Broche(){
		super();
	}

	//Getters y Setters
	
	public String getDesBroche() {
		return desBroche;
	}
	public void setDesBroche(String desBroche) {
		this.desBroche = desBroche;
	}
	public Recurso getRecurso() {
		return recurso;
	}
	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}
	public Repartidor getRepartidor() {
		return repartidor;
	}
	public void setRepartidor(Repartidor repartidor) {
		this.repartidor = repartidor;
	}
	public String getStrDomicilioEnvio() {
		return strDomicilioEnvio;
	}
	public void setStrDomicilioEnvio(String strDomicilioEnvio) {
		this.strDomicilioEnvio = strDomicilioEnvio;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public TipoBroche getTipoBroche() {
		return tipoBroche;
	}
	public void setTipoBroche(TipoBroche tipoBroche) {
		this.tipoBroche = tipoBroche;
	}
	public List<BroCue> getListBroCue() {
		return listBroCue;
	}
	public void setListBroCue(List<BroCue> listBroCue) {
		this.listBroCue = listBroCue;
	}

	public Integer getPermiteImpresion() {
		return permiteImpresion;
	}
	public void setPermiteImpresion(Integer permiteImpresion) {
		this.permiteImpresion = permiteImpresion;
	}

	// Metodos de clase	
	public static Broche getById(Long id) {
		return (Broche) PadDAOFactory.getBrocheDAO().getById(id);
	}
	
	public static Broche getByIdNull(Long id) {
		return (Broche) PadDAOFactory.getBrocheDAO().getByIdNull(id);
	}
	
	public static List<Broche> getList() {
		return (ArrayList<Broche>) PadDAOFactory.getBrocheDAO().getList();
	}
	
	public static List<Broche> getListActivos() {			
		return (ArrayList<Broche>) PadDAOFactory.getBrocheDAO().getListActiva();
	}
	
	public static List<Broche> getListActivosByIdTipoBrocheYIdRecurso(Long idTipoBroche, Long idRecurso) {			
		return (ArrayList<Broche>) PadDAOFactory.getBrocheDAO().getListActivosByIdTipoBrocheYIdRecurso(idTipoBroche, idRecurso);
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
		//Validaciones de Negocio
				
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

	/**
	 * Validaciones comunes de creacion y actualizacion
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();

		//Validaciones de Requeridos	
		if(getRecurso()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.BROCHE_RECURSO);
		}
		if(getTipoBroche()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.BROCHE_TIPOBROCHE);
		}
		if(StringUtil.isNullOrEmpty(getDesBroche())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.BROCHE_DESBROCHE);
		}
		if (getExentoEnvioJud() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.BROCHE_EXENTOENVIOJUD);
		}
		if (getPermiteImpresion() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.BROCHE_PERMITEIMPRESION);
		}
		
		if (hasError()) {
			return false;
		}
		
		return !hasError();
	}
	
	/**
	 * Valida la eliminacion
	 * @author
	 */
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
		
		if (GenericDAO.hasReference(this, BroCue.class, "broche")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
					PadError.BROCHE_LABEL , PadError.BROCUE_LABEL);
		}			
		if (GenericDAO.hasReference(this, Cuenta.class, "broche")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
					PadError.BROCHE_LABEL , PadError.CUENTA_LABEL);
		}
		
		//Validaciones de VO

		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}

	/**
	 * Activa el Broche. Previamente valida la activacion 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		PadDAOFactory.getBrocheDAO().update(this);
	}

	/**
	 * Desactiva el Broche. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		PadDAOFactory.getBrocheDAO().update(this);
	}
	
	/**
	 * Valida la activacion de la Broche
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion de la Broche
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	// Administracion de BroCue
	public BroCue createBroCue(BroCue broCue) throws Exception {
		
		// Validaciones de negocio
		if (!broCue.validateCreate()) {
			return broCue;
		}

		PadDAOFactory.getBroCueDAO().update(broCue);
		
		return broCue;
	}	

	public BroCue updateBroCue(BroCue broCue) throws Exception {
		
		// Validaciones de negocio
		if (!broCue.validateUpdate()) {
			return broCue;
		}

		PadDAOFactory.getBroCueDAO().update(broCue);
		
		return broCue;
	}	

	public BroCue deleteBroCue(BroCue broCue) throws Exception {
		
		// Validaciones de negocio
		if (!broCue.validateDelete()) {
			return broCue;
		}
				
		PadDAOFactory.getBroCueDAO().delete(broCue);
		
		return broCue;
	}	

	
	/**
	 * Devuelve un VO del Bean realizando un toVO especifico. 
	 * <i>(en este caso se realiza un toVO de profundidad uno para obtener el Recurso y el TipoBroche</i>
	 *
	 * @param withList - boolean que indica si se quiere pasar las listas al VO. 
	 * @return BrocheVO
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public BrocheVO toVOSpecific(boolean withList) throws Exception{
		BrocheVO brocheVO;
		brocheVO = (BrocheVO) this.toVO(0, false);
		brocheVO.setTipoBroche((TipoBrocheVO) this.getTipoBroche().toVO(0, false));
		brocheVO.setRecurso((RecursoVO) this.getRecurso().toVO(0, false));
		
		if (this.getRepartidor()!=null) {
			brocheVO.setRepartidor((RepartidorVO) this.getRepartidor().toVO(0, false));
		}
		else {
			brocheVO.setRepartidor(new RepartidorVO(-1, StringUtil.SELECT_OPCION_NINGUNO));
		}
		
		if (withList == true) {
			brocheVO.setListBroCue((ArrayList<BroCueVO>) ListUtilBean.toVO(this.getListBroCue(),0,false));
		}
		return brocheVO;
	}
	
	/**
	 * Devuelve una lista de VOs a partir de una lista de Beans realizando un toVO especifico. 
	 * 
	 * @param listBroche - Lista de Broches Bean
	 * @return listBrocheVO - Lista de Broches VO
	 * @throws Exception
	 */
	public static List<BrocheVO> listToVOSpecific(List<Broche> listBroche) throws Exception{
		
		List<BrocheVO> listVO = null;
		
		if (listBroche != null){
			listVO = new ArrayList<BrocheVO>();
			
			for(Iterator it = listBroche.iterator(); it.hasNext();) {
				Broche broche = (Broche) it.next();
				listVO.add(broche.toVOSpecific(false));
			}
		}
		return listVO;
	}

	/**
	 * Obtiene la lista de repartidores activos para el broche
	 * @return List<Repartidor>
	 */
	public List<Repartidor> getListRepartidorActivos() {
		return (ArrayList<Repartidor>) 
			PadDAOFactory.getRepartidorDAO().getListActivosByIdBroche(this.getId());
	}

	public void setExentoEnvioJud(Integer exentoEnvioJud) {
		this.exentoEnvioJud = exentoEnvioJud;
	}

	public Integer getExentoEnvioJud() {
		return exentoEnvioJud;
	}

}
