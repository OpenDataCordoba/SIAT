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
import javax.persistence.Transient;

import org.hibernate.annotations.OrderBy;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.Zona;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.model.CriRepCalleVO;
import ar.gov.rosario.siat.pad.iface.model.CriRepCatVO;
import ar.gov.rosario.siat.pad.iface.model.CriRepVO;
import ar.gov.rosario.siat.pad.iface.model.RepartidorVO;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import ar.gov.rosario.siat.rec.buss.bean.PlanillaCuadra;
import ar.gov.rosario.siat.rec.iface.util.RecError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a Repartidor
 * 
 * @author tecso
 */
@Entity
@Table(name = "pad_repartidor")
public class Repartidor extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "nroRepartidor")
	private Long nroRepartidor;
	
	@Column(name = "desRepartidor")
	private String desRepartidor;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idRecurso") 
	private Recurso recurso;
	
/*	@Embedded
    @AttributeOverrides( {
        @AttributeOverride(name="estado", column = @Column(name="estado", insertable=false, updatable=false) ),
        @AttributeOverride(name="fechaUltMdf", column = @Column(name="fechaUltMdf", insertable=false, updatable=false) ),
        @AttributeOverride(name="usuarioUltMdf", column = @Column(name="usuario", insertable=false, updatable=false) ) 
        
    } )*/
    @Transient
  	private Persona persona;
	
	@Column(name = "legajo")
	private String legajo;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idZona") 
	private Zona zona;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idTipoRepartidor") 
	private TipoRepartidor tipoRepartidor;

	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idBroche") 
	private Broche broche;

	// Listas de Entidades Relacionadas con Repartidor
	@OneToMany()
	@JoinColumn(name="idRepartidor")
	@OrderBy(clause="idSeccion, catastraldesde")
	private List<CriRepCat> listCriRepCat;
	
	@OneToMany()
	@JoinColumn(name="idRepartidor")
	@OrderBy(clause="fechaDesde")
	private List<CriRepCalle> listCriRepCalle;
	
	//Constructores 
	
	public Repartidor(){
		super();
	}
	
	//Getters y Setters
	
	public String getDesRepartidor() {
		return desRepartidor;
	}
	public void setDesRepartidor(String desRepartidor) {
		this.desRepartidor = desRepartidor;
	}
	public String getLegajo() {
		return legajo;
	}
	public void setLegajo(String legajo) {
		this.legajo = legajo;
	}
	public Long getNroRepartidor() {
		return nroRepartidor;
	}
	public void setNroRepartidor(Long nroRepartidor) {
		this.nroRepartidor = nroRepartidor;
	}
	public Persona getPersona() {
		return persona;
	}
	public void setPersona(Persona persona) {
		this.persona = persona;
	}
	public Recurso getRecurso() {
		return recurso;
	}
	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}
	public TipoRepartidor getTipoRepartidor() {
		return tipoRepartidor;
	}
	public void setTipoRepartidor(TipoRepartidor tipoRepartidor) {
		this.tipoRepartidor = tipoRepartidor;
	}
	public Zona getZona() {
		return zona;
	}
	public void setZona(Zona zona) {
		this.zona = zona;
	}	
	public List<CriRepCalle> getListCriRepCalle() {
		return listCriRepCalle;
	}
	public void setListCriRepCalle(List<CriRepCalle> listCriRepCalle) {
		this.listCriRepCalle = listCriRepCalle;
	}
	public List<CriRepCat> getListCriRepCat() {
		return listCriRepCat;
	}
	public void setListCriRepCat(List<CriRepCat> listCriRepCat) {
		this.listCriRepCat = listCriRepCat;
	}
	public Broche getBroche() {
		return broche;
	}
	public void setBroche(Broche broche) {
		this.broche = broche;
	}

	// Metodos de clase	
	public static Repartidor getById(Long id) {
		return (Repartidor) PadDAOFactory.getRepartidorDAO().getById(id);
	}
	
	public static Repartidor getByIdNull(Long id) {
		return (Repartidor) PadDAOFactory.getRepartidorDAO().getByIdNull(id);
	}
	
	public static List<Repartidor> getList() {
		return (ArrayList<Repartidor>) PadDAOFactory.getRepartidorDAO().getList();
	}

	public static List<Repartidor> getListActivos() {			
		return (ArrayList<Repartidor>) PadDAOFactory.getRepartidorDAO().getListActiva();
	}

	public static List<Repartidor> getListActivosByIdTipoRepartidor(Long idTipoRepartidor) {			
		return (ArrayList<Repartidor>) PadDAOFactory.getRepartidorDAO().getListActivosByIdTipoRepartidor(idTipoRepartidor);
	}

	public static List<Repartidor> getListActivosByIdRecurso(Long idRecurso) {
		return (ArrayList<Repartidor>) 
			PadDAOFactory.getRepartidorDAO().getListActivosByIdRecurso(idRecurso);
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
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.REPARTIDOR_RECURSO);
		}
		if(getTipoRepartidor()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.REPARTIDOR_TIPOREPARTIDOR);
		}
		if(StringUtil.isNullOrEmpty(getDesRepartidor())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.REPARTIDOR_PERSONA);
		}
		/*
		if(getPersona()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.REPARTIDOR_PERSONA);
		}*/
		
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
		
		if(this.getTipoRepartidor().getCriRep().getCodCriRep().equals("CATASTRAL")){
			if (GenericDAO.hasReference(this, CriRepCat.class, "repartidor")) {
				addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
						PadError.REPARTIDOR_LABEL , PadError.CRIREPCAT_LABEL);
			}			
		}
		if(this.getTipoRepartidor().getCriRep().getCodCriRep().equals("CALLE")){
			if (GenericDAO.hasReference(this, CriRepCalle.class, "repartidor")) {
				addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
						PadError.REPARTIDOR_LABEL , PadError.CRIREPCALLE_LABEL);
			}			
		}
		
		if (GenericDAO.hasReference(this, PlanillaCuadra.class, "repartidor")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
					PadError.REPARTIDOR_LABEL , RecError.PLANILLACUADRA_LABEL);
		}			
		
		//Validaciones de VO

		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}

	/**
	 * Activa la Repartidor. Previamente valida la activacion 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		PadDAOFactory.getRepartidorDAO().update(this);
	}

	/**
	 * Desactiva la Repartidor. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		PadDAOFactory.getRepartidorDAO().update(this);
	}
	
	/**
	 * Valida la activacion de la Repartidor
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion de la Repartidor
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

	// Administracion de CriRepCat
	public CriRepCat createCriRepCat(CriRepCat criRepCat) throws Exception {
		
		// Validaciones de negocio
		if (!criRepCat.validateCreate()) {
			return criRepCat;
		}

		PadDAOFactory.getCriRepCatDAO().update(criRepCat);
		
		return criRepCat;
	}	

	public CriRepCat updateCriRepCat(CriRepCat criRepCat) throws Exception {
		
		// Validaciones de negocio
		if (!criRepCat.validateUpdate()) {
			return criRepCat;
		}

		PadDAOFactory.getCriRepCatDAO().update(criRepCat);
		
		return criRepCat;
	}	

	public CriRepCat deleteCriRepCat(CriRepCat criRepCat) throws Exception {
		
		// Validaciones de negocio
		if (!criRepCat.validateDelete()) {
			return criRepCat;
		}
				
		PadDAOFactory.getCriRepCatDAO().delete(criRepCat);
		
		return criRepCat;
	}	

	// Administracion de CriRepCalle
	public CriRepCalle createCriRepCalle(CriRepCalle criRepCalle) throws Exception {
		
		// Validaciones de negocio
		if (!criRepCalle.validateCreate()) {
			return criRepCalle;
		}

		PadDAOFactory.getCriRepCalleDAO().update(criRepCalle);
		
		return criRepCalle;
	}	

	public CriRepCalle updateCriRepCalle(CriRepCalle criRepCalle) throws Exception {
		
		// Validaciones de negocio
		if (!criRepCalle.validateUpdate()) {
			return criRepCalle;
		}

		PadDAOFactory.getCriRepCalleDAO().update(criRepCalle);
		
		return criRepCalle;
	}	

	public CriRepCalle deleteCriRepCalle(CriRepCalle criRepCalle) throws Exception {
		
		// Validaciones de negocio
		if (!criRepCalle.validateDelete()) {
			return criRepCalle;
		}
				
		PadDAOFactory.getCriRepCalleDAO().delete(criRepCalle);
		
		return criRepCalle;
	}	

	/**
	 * Devuelve un VO del Bean realizando un toVO especifico. 
	 * <i>(en este caso se realiza un toVO de profundidad tres para obtener el CriRep a travez de TipoRepartidor,
	 * pero un toVO de profundidad uno para el resto de los Bean relacionados)</i>
	 *
	 * @param withList - boolean que indica si se quiere pasar las listas al VO. 
	 * @return RepartidorVO
	 * @throws Exception
	 */
	public RepartidorVO toVOSpecific(boolean withList) throws Exception{
		RepartidorVO repartidorVO;
		repartidorVO = (RepartidorVO) this.toVO(1);
		repartidorVO.getTipoRepartidor().setCriRep((CriRepVO) this.getTipoRepartidor().getCriRep().toVO());
		if(withList==true){
			repartidorVO.setListCriRepCalle((ArrayList<CriRepCalleVO>) ListUtilBean.toVO(this.getListCriRepCalle(),1));
			repartidorVO.setListCriRepCat((ArrayList<CriRepCatVO>) ListUtilBean.toVO(this.getListCriRepCat(),1));
		}
		return repartidorVO;
	}
	
	/** hace un tovo del nivel indicado componiendo tb la persona 
	 *  repartidor
	 * 
	 * @param nivel
	 * @return
	 * @throws Exception
	 */
	public RepartidorVO toVOConPersona(int nivel) throws Exception {
		RepartidorVO repartidorVO;
		repartidorVO = (RepartidorVO) this.toVO(nivel, false);
		
		// seteo la persona
		//Persona persona = Persona.getById(this.getPersona().getId()); 
		//repartidorVO.setPersona((PersonaVO) persona.toVO(1, false));

		return repartidorVO;
	}
	
	
	/**
	 * Devuelve una lista de VOs a partir de una lista de Beans realizando un toVO especifico. 
	 * 
	 * @param listRepartidor - Lista de Repartidores Bean
	 * @return listRepartidorVO - Lista de Repartidores VO
	 * @throws Exception
	 */
	public static List<RepartidorVO> listToVOSpecific(List<Repartidor> listRepartidor) throws Exception{
		
		List<RepartidorVO> listVO = null;
		
		if (listRepartidor != null){
			listVO = new ArrayList<RepartidorVO>();
			
			for(Iterator it = listRepartidor.iterator(); it.hasNext();){
				Repartidor repartidor = (Repartidor) it.next();
				listVO.add(repartidor.toVOSpecific(false));
			}
		}
		return listVO;
	}	
}
