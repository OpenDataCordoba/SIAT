//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.def.buss.bean.Zona;
import ar.gov.rosario.siat.def.iface.model.ZonaVO;
import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import ar.gov.rosario.siat.ef.iface.model.ActaInvVO;
import ar.gov.rosario.siat.ef.iface.model.EstadoOpeInvConVO;
import ar.gov.rosario.siat.ef.iface.model.HisEstOpeInvConVO;
import ar.gov.rosario.siat.ef.iface.model.InvestigadorVO;
import ar.gov.rosario.siat.ef.iface.model.OpeInvConCueVO;
import ar.gov.rosario.siat.ef.iface.model.OpeInvConVO;
import ar.gov.rosario.siat.ef.iface.model.OpeInvVO;
import ar.gov.rosario.siat.pad.buss.bean.Contribuyente;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.Domicilio;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import ar.gov.rosario.siat.pad.iface.model.ContribuyenteVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.iface.model.DomicilioVO;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a OpeInvCon
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_opeInvCon")
public class OpeInvCon extends BaseBO {

	private static Logger log = Logger.getLogger(OpeInvCon.class);
	
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idopeinv")
	private OpeInv opeInv;

	@Column(name = "idContribuyente")
	private Long idContribuyente;

	@Column(name = "datosContribuyente")
	private String datosContribuyente;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idEstadoOpeInvCon")
	private EstadoOpeInvCon estadoOpeInvCon;

	@Column(name = "obsExclusion")
	private String obsExclusion;

	@Column(name = "obsClasificacion")
	private String obsClasificacion;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idZona")
	private Zona zona;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idDomicilio")
	private Domicilio domicilio;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idCuenta")
	private Cuenta cuenta;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idInvestigador")
	private Investigador investigador;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="idOpeInvBus")
	private OpeInvBus opeInvBus;
	
	@OneToMany()
	@JoinColumn(name="idOpeInvCon")
	private List<OpeInvConCue> listOpeInvConCue;
	
	@OneToMany()
	@JoinColumn(name="idOpeInvCon")
	@OrderBy(value="fechaUltMdf")
	private List<HisEstOpeInvCon> listHisEstOpeInvCon;

	@Column(name = "fechaVisita")
	private Date fechaVisita;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idactInv")
	private ActaInv actaInv;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idOrdenControl")
	private OrdenControl ordenControl;

	// <#Propiedades#>

	// Constructores
	public OpeInvCon() {
		super();
		// Seteo de valores default
	}

	public OpeInvCon(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static OpeInvCon getById(Long id) {
		return (OpeInvCon) EfDAOFactory.getOpeInvConDAO().getById(id);
	}

	public static OpeInvCon getByIdNull(Long id) {
		return (OpeInvCon) EfDAOFactory.getOpeInvConDAO().getByIdNull(id);
	}

	public static List<OpeInvCon> getList() {
		return (List<OpeInvCon>) EfDAOFactory.getOpeInvConDAO().getList();
	}

	public static List<OpeInvCon> getListActivos() {
		return (List<OpeInvCon>) EfDAOFactory.getOpeInvConDAO().getListActiva();
	}
	
	/**
	 * Obtiene un opeInvCon con los valores pasados como parametros
	 * @param contribuyente
	 * @param opeInv2
	 * @return NULL si no encuentra nada
	 */
	public static OpeInvCon getByContrYOpeInv(Contribuyente contribuyente,OpeInv opeInv2) {
		return EfDAOFactory.getOpeInvConDAO().getByContrYOpeInv(contribuyente,opeInv2);
	}

	public static List<OpeInvCon> getListByOpeInv(OpeInv opeInv) {
		return (List<OpeInvCon>) EfDAOFactory.getOpeInvConDAO().getListByOpeInv(opeInv);
	}
	
	/**
	 * Obtiene una lista de ids de contribuyentes que esten en algun operativo (exista en un registro de opeInvCon) y en el estado pasado como parametro
	 * @param idEstadoOpeInv
	 * @return
	 */
	public static List<Long> getlistContrByEstOpeInv(Long idEstadoOpeInv) {
		return (List<Long>) EfDAOFactory.getOpeInvConDAO().getlistContrByEstOpeInv(idEstadoOpeInv);
	}

	public static List<OpeInvCon> getListByIds(Long[] idsOpeInvCon) {
		return (List<OpeInvCon>) EfDAOFactory.getOpeInvConDAO().getListByIds(idsOpeInvCon);
	}

	/**
	 * Obtiene una lista de opeInvCon ordenados por zona y catastral de la cuenta asociada (haciendo los join).<br>
	 * Incluye también a los opeInvCon que no tengan cuenta asociada
	 * @param idsOpeInvCon
	 * @return
	 * @throws Exception 
	 */
	public static List<OpeInvCon> getListByIdsOrderByZonaYCataDesc(Long[] idsOpeInvCon) throws Exception {
		return (List<OpeInvCon>) EfDAOFactory.getOpeInvConDAO().getListByIdsOrderByZonaYCataDesc(idsOpeInvCon);
	}
	//Metodos de instancia

	// Getters y setters

	public OpeInv getOpeInv() {
		return opeInv;
	}

	public void setOpeInv(OpeInv opeInv) {
		this.opeInv = opeInv;
	}

	public String getDatosContribuyente() {
		return datosContribuyente;
	}

	public void setDatosContribuyente(String datosContribuyente) {
		this.datosContribuyente = datosContribuyente;
	}

	public EstadoOpeInvCon getEstadoOpeInvCon() {
		return estadoOpeInvCon;
	}

	public void setEstadoOpeInvCon(EstadoOpeInvCon estadoOpeInvCon) {
		this.estadoOpeInvCon = estadoOpeInvCon;
	}

	public String getObsExclusion() {
		return obsExclusion;
	}

	public void setObsExclusion(String obsExclusion) {
		this.obsExclusion = obsExclusion;
	}

	public String getObsClasificacion() {
		return obsClasificacion;
	}

	public void setObsClasificacion(String obsClasificacion) {
		this.obsClasificacion = obsClasificacion;
	}

	public Zona getZona() {
		return zona;
	}

	public void setZona(Zona zona) {
		this.zona = zona;
	}

	public Domicilio getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(Domicilio domicilio) {
		this.domicilio = domicilio;
	}

	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	public Investigador getInvestigador() {
		return investigador;
	}

	public void setInvestigador(Investigador investigador) {
		this.investigador = investigador;
	}

	public Long getIdContribuyente() {
		return idContribuyente;
	}

	public void setIdContribuyente(Long idContribuyente) {
		this.idContribuyente = idContribuyente;
	}

	public OpeInvBus getOpeInvBus() {
		return opeInvBus;
	}

	public void setOpeInvBus(OpeInvBus opeInvBus) {
		this.opeInvBus = opeInvBus;
	}

	public List<OpeInvConCue> getListOpeInvConCue() {
		return listOpeInvConCue;
	}

	public void setListOpeInvConCue(List<OpeInvConCue> listOpeInvConCue) {
		this.listOpeInvConCue = listOpeInvConCue;
	}

	public List<HisEstOpeInvCon> getListHisEstOpeInvCon() {
		return listHisEstOpeInvCon;
	}

	public void setListHisEstOpeInvCon(List<HisEstOpeInvCon> listHisEstOpeInvCon) {
		this.listHisEstOpeInvCon = listHisEstOpeInvCon;
	}

	public Date getFechaVisita() {
		return fechaVisita;
	}

	public void setFechaVisita(Date fechaVisita) {
		this.fechaVisita = fechaVisita;
	}

	
	
	public ActaInv getActaInv() {
		return actaInv;
	}

	public void setActaInv(ActaInv actaInv) {
		this.actaInv = actaInv;
	}

	public OrdenControl getOrdenControl() {
		return ordenControl;
	}

	public void setOrdenControl(OrdenControl ordenControl) {
		this.ordenControl = ordenControl;
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

		// Validaciones

		if (hasError()) {
			return false;
		}

		return true;
	}

	// Metodos de negocio

	/** Hace toVO(0, false) y setea el contribuyente: Si no existe, buscar la persona con el mismo idContribuyente */
	public OpeInvConVO toVO(boolean cargarContrib, boolean cargarOpeInv, boolean cargarListOpeInvConCue, 
																	boolean cargarHistoricos, boolean cargarDomicilio) throws Exception{
		OpeInvConVO opeInvConVO = new OpeInvConVO();
		opeInvConVO.setId(this.getId());
		opeInvConVO.setDatosContribuyente(datosContribuyente);
		opeInvConVO.setObsClasificacion(obsClasificacion);
		opeInvConVO.setObsExclusion(obsExclusion);		
		opeInvConVO.setEstadoOpeInvCon((EstadoOpeInvConVO) estadoOpeInvCon.toVO(0, false));
		opeInvConVO.setInvestigador(investigador!=null?(InvestigadorVO)investigador.toVO(0, false):new InvestigadorVO());
		if(cuenta!=null)log.debug("CUENTA: "+getCuenta().getNumeroCuenta());
		opeInvConVO.setCuenta(this.cuenta!=null?getCuenta().toVOForView():new CuentaVO());		
		opeInvConVO.setZona(zona!=null?(ZonaVO)zona.toVO(0, false):new ZonaVO());
		opeInvConVO.setFechaVisita(fechaVisita);
		opeInvConVO.setActaInv(actaInv!=null?(ActaInvVO)actaInv.toVO(1, false):new ActaInvVO());
		
		//carga las cuentas asociadas
		if (cargarListOpeInvConCue){
			for (OpeInvConCue opeInvConCue : getListOpeInvConCue()){
				OpeInvConCueVO opeInvConCueVO = (OpeInvConCueVO) opeInvConCue.toVO(0, false);
				opeInvConCueVO.setCuenta((CuentaVO) opeInvConCue.getCuenta().toVO(1, false));
				opeInvConCueVO.setNroCuenta(opeInvConCue.getCuenta().getNumeroCuenta());
				opeInvConCueVO.setDesRecurso(opeInvConCue.getCuenta().getRecurso().getDesRecurso());
				opeInvConCueVO.setEsSeleccionada(opeInvConCue.getSeleccionada());
				opeInvConVO.getListOpeInvConCue().add(opeInvConCueVO);
			}
		}
		
		// carga la lista de historicos
		if (cargarHistoricos){			
			for (HisEstOpeInvCon hisEstOpeInvCon: listHisEstOpeInvCon){
				HisEstOpeInvConVO hisEstOpeInvConVO = (HisEstOpeInvConVO) hisEstOpeInvCon.toVO(0, false);
				hisEstOpeInvConVO.setFechaEstado(hisEstOpeInvCon.getFechaUltMdf());
				hisEstOpeInvConVO.setObservaciones(hisEstOpeInvCon.getObservacion());
				hisEstOpeInvConVO.setDesEstado(hisEstOpeInvCon.getEstadoOpeInvCon().getDesEstadoOpeInvCon());
				opeInvConVO.getListHisEstOpeInvCon().add(hisEstOpeInvConVO);
			}
		}
	
		// carga la persona / contribuyente
		if(cargarContrib){
			Contribuyente contr = Contribuyente.getByIdNull(idContribuyente);
			if(contr==null){
				// Busca la persona con el mismo id
				Persona persona = Persona.getById(idContribuyente);
				ContribuyenteVO contrVO = new ContribuyenteVO();
				contrVO.setId(idContribuyente);
				contrVO.setPersona((PersonaVO) persona.toVO(2, false));
				opeInvConVO.setContribuyente(contrVO);
			}else{
				opeInvConVO.setContribuyente((ContribuyenteVO) contr.toVO(3, false ));
			}
		}
		
		//carga el operativo
		if(cargarOpeInv){
			opeInvConVO.setOpeInv((OpeInvVO) opeInv.toVO(1, false));
		}
		
		// carga el domicilio
		if(cargarDomicilio){
			if(getDomicilio()!=null){
				// el domicilio que tiene es el de la cuenta
				opeInvConVO.setDomicilio((DomicilioVO) getDomicilio().toVO(1,false));
			}else{
				//No es contribuyente -> saca el domicilio de la persona, que no se puede grabar cuando se crea el opeInvCon porque lo saca de la base de personas
				Persona persona = Persona.getById(idContribuyente);
				opeInvConVO.setDomicilio(persona.getDomicilio()!=null?(DomicilioVO)persona.getDomicilio().toVO(1, false):new DomicilioVO());				
			}
				
/*			opeInvConVO.setDomicilio((DomicilioVO) getDomicilio().toVO(1,false));
			Contribuyente contr = Contribuyente.getByIdNull(idContribuyente);
			log.debug("idContribuyente:"+idContribuyente+"      contr:"+contr);
			if(contr!=null){
				// Es contribuyente -> carga el domicilio que tiene, que lo saco de la cuenta
				opeInvConVO.setDomicilio(getDomicilio()!=null?(DomicilioVO) getDomicilio().toVO(1,false):new DomicilioVO());
			}else{
				//No es contribuyente -> saca el domicilio de la persona
				Persona persona = Persona.getById(idContribuyente);
				opeInvConVO.setDomicilio(persona.getDomicilio()!=null?(DomicilioVO)persona.getDomicilio().toVO(1, false):new DomicilioVO());
			}
*/
		}
		
		return opeInvConVO;
	}
	/**
	 * Activa el OpeInvCon. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getOpeInvConDAO().update(this);
	}

	/**
	 * Desactiva el OpeInvCon. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getOpeInvConDAO().update(this);
	}

	/**
	 * Valida la activacion del OpeInvCon
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
	 * Valida la desactivacion del OpeInvCon
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
	 * Devuelve el opeInvConCue con la marca seleccionada=SI
	 * @return null si no encuentra ninguno
	 */
	public OpeInvConCue getOpeInvConCueCuentaSelec() {
		if(listOpeInvConCue!=null){
			for(OpeInvConCue opeInvConCue: listOpeInvConCue){
				if(opeInvConCue.getSeleccionada())
					return opeInvConCue;
			}
		}
		return null;
	}


	
	// ABM actaInv
	public ActaInv updateActaInv(ActaInv actaInv) throws Exception {
		// Validaciones de negocio
		if (!actaInv.validateCreate()) {
			return actaInv;
		}
		EfDAOFactory.getActaInvDAO().update(actaInv);

		return actaInv;
	}





	// <#MetodosBeanDetalle#>
}
