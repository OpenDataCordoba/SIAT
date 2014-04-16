//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.TipObjImp;
import ar.gov.rosario.siat.def.buss.bean.TipObjImpAtr;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.ConDeuDetVO;
import ar.gov.rosario.siat.gde.iface.model.ConDeuTitVO;
import ar.gov.rosario.siat.gde.iface.model.ConstanciaDeuPrint;
import ar.gov.rosario.siat.gde.iface.model.ConstanciaDeuVO;
import ar.gov.rosario.siat.gde.iface.model.EstConDeuVO;
import ar.gov.rosario.siat.gde.iface.model.HistEstConDeuVO;
import ar.gov.rosario.siat.gde.iface.model.PlaEnvDeuProVO;
import ar.gov.rosario.siat.gde.iface.model.ProcesoMasivoVO;
import ar.gov.rosario.siat.gde.iface.model.ProcuradorVO;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.CuentaTitular;
import ar.gov.rosario.siat.pad.buss.bean.Domicilio;
import ar.gov.rosario.siat.pad.buss.bean.Localidad;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.iface.model.ObjImpVO;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpAtrDefinition;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpDefinition;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a la agrupacion de Deuda por cada Cuenta incluida en el Envio de Deuda a un Procurador
 * 
 * @author tecso
 */

@Entity
@Table(name = "gde_constanciaDeu")
public class ConstanciaDeu extends BaseBO {

	@Transient
	private static Logger log = Logger.getLogger(ConstanciaDeu.class);
	
	private static final long serialVersionUID = 1L;	
	
	public static final String NAME = "constanciaDeu";


	@Column(name = "numero")
	private Long numero;        // NOT NULL,
	
	@Column(name = "anio")
	private Integer anio;    // NOT NULL,

	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idProcurador") 
	private Procurador procurador;           // NOT NULL
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idCuenta") 
	private Cuenta cuenta;           // NOT NULL

	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idEstConDeu") 
	private EstConDeu estConDeu; // NOT NULL
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
	@JoinColumn(name="idDomicilio") 
	private Domicilio domicilio; // nuleable
	
	@ManyToOne( fetch=FetchType.LAZY) 
	@JoinColumn(name="idProcesoMasivo") 
	private ProcesoMasivo procesoMasivo;   // es nuleable

	@ManyToOne( fetch=FetchType.LAZY) 
	@JoinColumn(name="idPlaEnvDeuPro") 
	private PlaEnvDeuPro plaEnvDeuPro;   // es nuleable
		
	@Column(name = "observacion")
	private String observacion;     // VARCHAR(255)

	@Column(name = "usrCreador")
	private String usrCreador;     // VARCHAR(255)

	@OneToMany()
	@JoinColumn(name="idConstanciaDeu")
	private List<ConDeuDet> listConDeuDet;
	
	@OneToMany()
	@JoinColumn(name="idConstanciaDeu")
	private List<ConDeuTit> listConDeuTit;

	@OneToMany()
	@JoinColumn(name="idConstanciaDeu")
	private List<HistEstConDeu> listHistEstConDeu;

	@Column(name = "fechaHabilitacion")
	private Date fechaHabilitacion;
	
	@Column(name="desDomEnv")
	private String desDomEnv;
	
	@Column(name="desDomUbi")
	private String desDomUbi;
	
	@Column(name="desTitulares")
	private String desTitulares;
	
    @Column(name="idCaso") 
	private String idCaso;
    
	// Constructores
	public ConstanciaDeu(){
		super();
	}
	
	// Getters y Setters
	public Integer getAnio() {
		return anio;
	}
	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	public Cuenta getCuenta() {
		return cuenta;
	}
	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	public ProcesoMasivo getProcesoMasivo() {
		return procesoMasivo;
	}
	public void setProcesoMasivo(ProcesoMasivo procesoMasivo) {
		this.procesoMasivo = procesoMasivo;
	}
	public EstConDeu getEstConDeu() {
		return estConDeu;
	}
	public void setEstConDeu(EstConDeu estConDeu) {
		this.estConDeu = estConDeu;
	}
	public Long getNumero() {
		return numero;
	}
	public void setNumero(Long numero) {
		this.numero = numero;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public PlaEnvDeuPro getPlaEnvDeuPro() {
		return plaEnvDeuPro;
	}
	public void setPlaEnvDeuPro(PlaEnvDeuPro plaEnvDeuPro) {
		this.plaEnvDeuPro = plaEnvDeuPro;
	}
	public Procurador getProcurador() {
		return procurador;
	}
	public void setProcurador(Procurador procurador) {
		this.procurador = procurador;
	}
	public List<ConDeuDet> getListConDeuDet() {
		return listConDeuDet;
	}
	public void setListConDeuDet(List<ConDeuDet> listConDeuDet) {
		this.listConDeuDet = listConDeuDet;
	}

	public Date getFechaHabilitacion() {
		return fechaHabilitacion;
	}

	public void setFechaHabilitacion(Date fechaHabilitacion) {
		this.fechaHabilitacion = fechaHabilitacion;
	}

	public List<ConDeuTit> getListConDeuTit() {
		return listConDeuTit;
	}

	public void setListConDeuTit(List<ConDeuTit> listConDeuTit) {
		this.listConDeuTit = listConDeuTit;
	}

	public List<HistEstConDeu> getListHistEstConDeu() {
		return listHistEstConDeu;
	}
	
	public void setListHistEstConDeu(List<HistEstConDeu> listHistEstConDeu) {
		this.listHistEstConDeu = listHistEstConDeu;
	}
	
	public String getUsrCreador() {
		return usrCreador;
	}

	public void setUsrCreador(String usrCreador) {
		this.usrCreador = usrCreador;
	}
	
	public String getDesDomEnv() {
		return desDomEnv;
	}

	public void setDesDomEnv(String desDomEnv) {
		this.desDomEnv = desDomEnv;
	}

	public Domicilio getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(Domicilio domicilio) {
		this.domicilio = domicilio;
	}
	
	public String getDesTitulares() {
		return desTitulares;
	}

	public void setDesTitulares(String desTitulares) {
		this.desTitulares = desTitulares;
	}
	
	public String getDesDomUbi() {
		return desDomUbi;
	}
	
	public void setDesDomUbi(String desDomUbi) {
		this.desDomUbi = desDomUbi;
	}
	
	public String getIdCaso() {
		return idCaso;
	}	
	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}
	
	
	// Metodos de Clase



	/**
	 * Obtiene la constancia, carga las personas en listConDeuTit y setea la localidad del domicilio
	 */
	public static ConstanciaDeu getById(Long id) throws Exception {
		ConstanciaDeu constanciaDeu = (ConstanciaDeu) GdeDAOFactory.getConstanciaDeuDAO().getById(id);
		constanciaDeu.cargarPersonas();
		constanciaDeu.setearLocalidad();
		return constanciaDeu;
	}
	
	public void setearLocalidad() throws Exception {
		if (getDomicilio() != null && getDomicilio().getLocalidad() != null){
			Localidad localidad = Localidad.getByCodPostSubPost(getDomicilio().getLocalidad().getCodPostal(),	getDomicilio().getLocalidad().getCodSubPostal());
			getDomicilio().setLocalidad(localidad);
		}
	}	
	public static ConstanciaDeu getByIdNull(Long id) {
		return (ConstanciaDeu) GdeDAOFactory.getConstanciaDeuDAO().getByIdNull(id);
	}
	
	public static List<ConstanciaDeu> getList() {
		return (ArrayList<ConstanciaDeu>) GdeDAOFactory.getConstanciaDeuDAO().getList();
	}
	
	public static List<ConstanciaDeu> getListActivos() {			
		return (ArrayList<ConstanciaDeu>) GdeDAOFactory.getConstanciaDeuDAO().getListActiva();
	}

	/**
	 * Obtiene la Constancia de Deuda para una Planilla de Envio de Deuda a Procurador y una cuenta
	 * @param plaEnvDeuPro
	 * @param cuenta
	 * @return ConstanciaDeu
	 */
	public static ConstanciaDeu getConstanciaDeuByPlaEnvDeuProCuenta(PlaEnvDeuPro plaEnvDeuPro, Cuenta cuenta) {			
		return GdeDAOFactory.getConstanciaDeuDAO().getConstanciaDeuByPlaEnvDeuProCuenta(plaEnvDeuPro, cuenta);
	}
	
	public static Long getNextNumero() {			
		return GdeDAOFactory.getConstanciaDeuDAO().getNextVal();
	}

	// Metodos de Instancia
	/**
	 * Carga las personas titulares (para cada uno en listConDeuTit)
	 * @throws Exception 
	 */
	public void cargarPersonas() throws Exception{
		if(listConDeuTit!=null){
			for(ConDeuTit conDeutit: listConDeuTit){
				conDeutit.cargarPersona();
			}
		}
	}
	
	/**
	 * Realiza la habilitacion de la Constancia, seteando la fecha de habilitacion y el estado HABILITADA. guarda el historico
	 */
	public void habilitar(String logEstado) throws Exception{
		setEstConDeu(EstConDeu.getById(EstConDeu.ID_HABILITADA));
		setFechaHabilitacion(new Date());
		GdeGDeudaJudicialManager.getInstance().updateConstanciaDeu(this, logEstado);
	}
	
	/**
	 * Crea un conDeuTit con el idPersona de la persona de la cuentaTitular que se pasa por parametro y lo registra en la BD 
	 * @throws Exception 
	 */
	public void cargarTitular(CuentaTitular cuentaTitular) throws Exception{
		ConDeuTit conDeutit = new ConDeuTit();
		conDeutit.setConstanciaDeu(this);
		conDeutit.setIdPersona(cuentaTitular.getContribuyente().getId());
		conDeutit.setEstado(Estado.ACTIVO.getId());
		createConDeutit(conDeutit);
	}
	
	/**
	 * Devuelve la cadena que se incluira en el archivo del CD para los procuradores
	 * @return
	 * @throws Exception 
	 */
	public String getStringArchivoCD() throws Exception {
		log.debug("getStringArchivoCD :: enter -----------------------------------------------------");
		String contenido ="";
		
		// idProcurador
		if(procurador!=null)
			contenido += StringUtil.completarCerosIzq(String.valueOf(procurador.getId()), 4);
		else
			contenido += StringUtil.completarCerosIzq("", 4);
		
		// nroCuenta
		contenido += StringUtil.completarCerosIzq(String.valueOf(cuenta.getNumeroCuenta()), 10);
		
		// anio
		contenido += anio;
		
		// Recorre los atributos para obtener la catastral y la ubicacion
		String catastral ="";
		String ubicacionFinca="";
		String ubicacionBaldio ="";
		String tipoParcela="";

		TipObjImpDefinition tipObjImpDef = cuenta.getObjImp().getDefinitionValue();
		for (TipObjImpAtrDefinition tipObjImpAtrDef: tipObjImpDef.getListTipObjImpAtrDefinition()){
			
			if(tipObjImpAtrDef.getAtributo().getCodAtributo().trim().equals("Catastral"))
				catastral = tipObjImpAtrDef.getValorView();
			
			// Guarda los datos para armar el domicilio
			if(tipObjImpAtrDef.getAtributo().getCodAtributo().trim().equals("DomicilioFinca"))
				ubicacionFinca = tipObjImpAtrDef.getValorView();
			
			else if(tipObjImpAtrDef.getAtributo().getCodAtributo().trim().equals("UbiTerreno"))
				ubicacionBaldio = tipObjImpAtrDef.getValorView();						
			
			else if(tipObjImpAtrDef.getAtributo().getCodAtributo().trim().equals("TipoParcela"))
				tipoParcela = tipObjImpAtrDef.getValorView();
		}
		
		// catastral
		contenido += StringUtil.replace(catastral, "/", "");
		
		if(listConDeuDet!=null){
			for(ConDeuDet c : listConDeuDet){
				Deuda deuda = c.getDeuda();
				// periodo deuda
				contenido += String.valueOf(deuda.getPeriodo());
				// fecVto formato: yyyyMMdd
				Calendar cal = Calendar.getInstance();
				cal.setTime(deuda.getFechaVencimiento());
				contenido += cal.get(Calendar.YEAR);
				contenido += StringUtil.completarCerosIzq(String.valueOf(cal.get(Calendar.MONTH)), 2);
				contenido += StringUtil.completarCerosIzq(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)), 2);
								
				// ver que mas...
				for(DeuRecCon d: c.getDeuda().getListDeuRecCon()){
					log.debug(d.getRecCon().getCodRecCon()+"     "+d.getRecCon().getDesRecCon()+"   "+
							d.getSaldo());
				}
			}
			if(listConDeuDet.size()<12){
				// completa con ceros(51 ver cuantos son) por cada registro hasta llegar a los 12 registros de deuda
				int cantCompletar = 12 - listConDeuDet.size();
				int cantCerosCompletar = 51*cantCompletar;
				contenido += StringUtil.completarCerosIzq("", cantCerosCompletar);
			}			
		}
		
		// Nombre --> ver si es este nombre u otro
		contenido += StringUtil.llenarConEspacios(
				getCuenta().getNombreTitularPrincipal()!=null?getCuenta().getNombreTitularPrincipal():"", 26);
		
		// Domicilio - Lo guarda dependiendo del tipoParcela encontrado en la lista
		if(tipoParcela.trim().equals("1")){
			contenido += ubicacionFinca;
		
		}else if(tipoParcela.trim().equals("2")){
			contenido += ubicacionBaldio;				
		}

		
		// nroConstancia --> ver si es esta la propiedad que se necesita
		contenido += StringUtil.llenarConEspacios(String.valueOf(getNumero()), 8);
		
		log.debug("getStringArchivoCD :: exit -----------------------------------------------------");
		return contenido;
	}
	
	/**
	 * Devuelve la constanciaVO de nivel 0, sin las listas
	 * @return
	 * @throws Exception 
	 */
	public ConstanciaDeuVO toVOLight() throws Exception{		
		//ConstanciaDeuVO constanciaDeuVO = (ConstanciaDeuVO) this.toVO(0, false);
		ConstanciaDeuVO constanciaDeuVO = new ConstanciaDeuVO();
		constanciaDeuVO.setId(getId());
		constanciaDeuVO.setNumero(getNumero());
		constanciaDeuVO.setAnio(getAnio());
		constanciaDeuVO.setDesDomUbi(this.getDesDomUbi());
		constanciaDeuVO.setDesDomEnv(this.getDesDomEnv());
		constanciaDeuVO.setFechaHabilitacion(getFechaHabilitacion());
		constanciaDeuVO.setObservacion(getObservacion());
		constanciaDeuVO.setDesTitulares(desTitulares);
		constanciaDeuVO.setIdCaso(getIdCaso());
		
		if(getPlaEnvDeuPro()!=null){			
			constanciaDeuVO.getPlaEnvDeuPro().setNroPlanilla(getPlaEnvDeuPro().getNroPlanilla());
			constanciaDeuVO.getPlaEnvDeuPro().setAnioPlanilla(getPlaEnvDeuPro().getAnioPlanilla());
			constanciaDeuVO.getPlaEnvDeuPro().setFechaEnvio(getPlaEnvDeuPro().getFechaEnvio());
		}
		
		constanciaDeuVO.setCuenta(this.cuenta.toVOWithRecurso());
		if (this.cuenta.getObjImp() != null){
			ObjImpVO objImpVO = (ObjImpVO) this.cuenta.getObjImp().toVO(0, false);
			objImpVO.setListObjImpAtrVal(null);		
			constanciaDeuVO.getCuenta().setObjImp(objImpVO);
			
			TipObjImp tipObjImp = this.cuenta.getObjImp().getTipObjImp();			
			TipObjImpAtr tipObjImpAtr = tipObjImp.getAtrClaveFuncional();
			
			if (tipObjImpAtr != null){
				constanciaDeuVO.getCuenta().getObjImp().setDesClaveFuncional(tipObjImpAtr.getAtributo().getDesAtributo());					
			}			
		}
		
		constanciaDeuVO.setEstConDeu((EstConDeuVO) this.estConDeu.toVO(0, false));
		this.setearLocalidad();
		constanciaDeuVO.setDesDomEnv(this.getDesDomEnv());
		constanciaDeuVO.setProcesoMasivo( (this.procesoMasivo!=null?(ProcesoMasivoVO) this.procesoMasivo.toVO(0,false):new ProcesoMasivoVO()));
		constanciaDeuVO.setProcurador((ProcuradorVO) this.getProcurador().toVO(0, false));
		return constanciaDeuVO;
	}

	
	/** Igual al toVOForABM pero con la listConDeuDet mucho mas liviana.
	 * hace el toVO() de nivel 0. le setea la planilla con nivel 0 y a la listConDeuDet la pasa a VO sin los conceptos de las deudas.
	 * Setea la lista de titulares y de historicos, pero light
	 * @return
	 * @throws Exception
	 */
	public ConstanciaDeuVO toVOForView() throws Exception{
		ConstanciaDeuVO constanciaDeuVO = this.toVOLight();
		constanciaDeuVO.setPlaEnvDeuPro((this.plaEnvDeuPro!=null?(PlaEnvDeuProVO) this.plaEnvDeuPro.toVOWithHist():new PlaEnvDeuProVO()));		
		
		// Lista de detalles de la constancia
		if(listConDeuDet!=null){
			List<ConDeuDetVO> listConDeuDetVO = new ArrayList<ConDeuDetVO>();
			for(ConDeuDet conDeuDet: listConDeuDet){
				listConDeuDetVO.add(conDeuDet.toVOforView());
			}
			
			Comparator<ConDeuDetVO> comp = new Comparator<ConDeuDetVO>(){
				public int compare(ConDeuDetVO c1, ConDeuDetVO c2) {
					return c1.getDeuda().getFechaVencimiento().compareTo(c2.getDeuda().getFechaVencimiento());
				}				
			};
			
			Collections.sort(listConDeuDetVO, comp);
			
			constanciaDeuVO.setListConDeuDet(listConDeuDetVO);
		}
		
		// Lista de titulares
		if(listConDeuTit!=null){
			List<ConDeuTitVO> listConDeuTit = new ArrayList<ConDeuTitVO>();
			for(ConDeuTit conDeuTit: this.listConDeuTit){
				listConDeuTit.add(conDeuTit.toVOForABM(false));
			}
			constanciaDeuVO.setListConDeuTit(listConDeuTit);
		}
		
		// Lista de historicos
		if(listHistEstConDeu!=null){
			List<HistEstConDeuVO> listHistorico = new ArrayList<HistEstConDeuVO>();
			for(HistEstConDeu historico: this.listHistEstConDeu){
				listHistorico.add((HistEstConDeuVO) historico.toVO(1, false));
			}
			constanciaDeuVO.setListHistEstConDeu(listHistorico);			
		}
		return constanciaDeuVO;
	}
	
	/**
	 * Igual al primero pero seteando la planilla, el procurador, las lista de titulares e historicos en null.
	 * @return
	 * @throws Exception
	 */
	public ConstanciaDeuVO toVOForView2() throws Exception{
		ConstanciaDeuVO constanciaDeuVO = this.toVOLight();
		constanciaDeuVO.setProcurador(null);
		constanciaDeuVO.setPlaEnvDeuPro(null);		
		constanciaDeuVO.setProcesoMasivo(null);
		constanciaDeuVO.setDesClave(getDesClave());
		
		// Lista de detalles de la constancia
		if(listConDeuDet!=null){
			List<ConDeuDetVO> listConDeuDetVO = new ArrayList<ConDeuDetVO>();
			for(ConDeuDet conDeuDet: listConDeuDet){
				listConDeuDetVO.add(conDeuDet.toVOforView());
			}
			
			Comparator<ConDeuDetVO> comp = new Comparator<ConDeuDetVO>(){
				public int compare(ConDeuDetVO c1, ConDeuDetVO c2) {
					return c1.getDeuda().getFechaVencimiento().compareTo(c2.getDeuda().getFechaVencimiento());
				}				
			};
			
			Collections.sort(listConDeuDetVO, comp);
		
			constanciaDeuVO.setListConDeuDet(listConDeuDetVO);
		
		}
		
		// Lista de titulares
		constanciaDeuVO.setListConDeuTit(null);
		
		
		// Lista de historicos
		constanciaDeuVO.setListHistEstConDeu(null);			
		constanciaDeuVO.calcularTotalSaldo();
		
		return constanciaDeuVO;
	}
	
	

	
	/**
	 * convierte la constanciaDeu a VO, buscando todos los objetos con profundidad 1 y setea la deudaJudicialVO en cada objeto de listConDeuDet
	 * @param copiarList
	 * @return
	 * @throws Exception
	 */
	public ConstanciaDeuVO toVOForABM(boolean copiarList) throws Exception{
		ConstanciaDeuVO constanciaDeuVO = this.toVOLight();
		constanciaDeuVO.setPlaEnvDeuPro((this.plaEnvDeuPro!=null?(PlaEnvDeuProVO) this.plaEnvDeuPro.toVO(0, false):new PlaEnvDeuProVO()));		
		
		// Lista de detalles de la constancia
		if(listConDeuDet!=null && copiarList){
			List<ConDeuDetVO> listConDeuDetVO = new ArrayList<ConDeuDetVO>();
			for(ConDeuDet conDeuDet: listConDeuDet){
				listConDeuDetVO.add(conDeuDet.toVOConDeudaJudicial(copiarList));
			}
			
			Comparator<ConDeuDetVO> comp = new Comparator<ConDeuDetVO>(){
				public int compare(ConDeuDetVO c1, ConDeuDetVO c2) {
					return c1.getDeuda().getFechaVencimiento().compareTo(c2.getDeuda().getFechaVencimiento());
				}				
			};

			
			/*
			Comparator<ConDeuDetVO> comp = new Comparator<ConDeuDetVO>(){
				public int compare(ConDeuDetVO c1, ConDeuDetVO c2) {
					if(c1.getDeuda().getAnio().longValue() > c2.getDeuda().getAnio().longValue()){	
						return 1;
					}
					if(c1.getDeuda().getAnio().longValue() < c2.getDeuda().getAnio().longValue()){
						return -1;
					}					
					if(c1.getDeuda().getAnio().longValue() == c2.getDeuda().getAnio().longValue()){						
						if (c1.getDeuda().getPeriodo().longValue() > c2.getDeuda().getPeriodo().longValue())
							return 1;
						else
							return -1;						
					}
					
					return 0;
				}				
			};
			*/
			
			Collections.sort(listConDeuDetVO, comp);
			
			constanciaDeuVO.setListConDeuDet(listConDeuDetVO);
		}
		
		// Lista de titulares
		if(listConDeuTit!=null && copiarList){
			List<ConDeuTitVO> listConDeuTit = new ArrayList<ConDeuTitVO>();
			for(ConDeuTit conDeuTit: this.listConDeuTit){
				listConDeuTit.add(conDeuTit.toVOForABM(copiarList));
			}
			constanciaDeuVO.setListConDeuTit(listConDeuTit);
		}
		
		// Lista de historicos
		if(listHistEstConDeu!=null && copiarList){
			List<HistEstConDeuVO> listHistorico = new ArrayList<HistEstConDeuVO>();
			for(HistEstConDeu historico: this.listHistEstConDeu){
				listHistorico.add(historico.toVOforView());
			}
			constanciaDeuVO.setListHistEstConDeu(listHistorico);			
		}
		return constanciaDeuVO;
	}
	
	public ConstanciaDeuVO toVOForImprimir() throws Exception{
		ConstanciaDeuVO constanciaDeuVO = toVOForABM(true);
		
		constanciaDeuVO.setCuenta((CuentaVO) getCuenta().toVO(1, true));
		if (cuenta.getObjImp() != null) {
			constanciaDeuVO.getCuenta().setObjImp((ObjImpVO) cuenta.getObjImp().toVO(1, false));
			constanciaDeuVO.getCuenta().getObjImp().setListObjImpAtrVal(null);
		}
		constanciaDeuVO.setDesClave(getDesClave());
		
		constanciaDeuVO.getPlaEnvDeuPro().setListHistEstPlaEnvDP(null);
		constanciaDeuVO.getPlaEnvDeuPro().setListConstanciaDeu(null);			
		constanciaDeuVO.setListHistEstConDeu(null);
		constanciaDeuVO.setProcesoMasivo(null);
		constanciaDeuVO.getProcurador().setListProRec(null);
/*		for(ConDeuDetVO conDeuDetVO: constanciaDeuVO.getListConDeuDet()){
			conDeuDetVO.setConstanciaDeu(null);
			//conDeuDetVO.getDeuda().setListDeuRecCon(null);
			conDeuDetVO.getDeuda().setCuenta(null);
		}*/
//		constanciaDeuVO.calcularTotal();
		return constanciaDeuVO;
	
	}
	
	/**
	 * 	Obtiene la descripcion del objImp ("Catastral" - "Nro Comercio", etc)
	 * @throws Exception 
	 */
	public String getDesClave() throws Exception{
		
		if (getCuenta().getObjImp() != null) {
			TipObjImp tipObjImp = TipObjImp.getById(getCuenta().getObjImp().getTipObjImp().getId()); 
			return tipObjImp.getDefinitionForBusqueda(TipObjImp.FOR_BUSQUEDA).getDesClaveFunc();		
		} else {
			return "";
		}		
	}
	
	// Validaciones
	/**
	 *  
	 */
	public boolean validateCreate() throws Exception {
		//limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio: sacamos todas las validaciones.
		// 
		/*
		 * 
		List<DeudaJudicial> listDeuJud = GdeDAOFactory.getDeudaJudicialDAO().getByNroCtaYProcurador(Long.parseLong(cuenta.getNumeroCuenta()), procurador.getId());
		// sacamos la validacion que la cuenta ingresada este asociada por lo menos a un registro de deuda en la via judicial asignado al procurador.
		// se decidio dejar solo esta validacion
		for(DeudaJudicial deuJud : listDeuJud){
			List<ConDeuDet> listDetalle = ConDeuDet.getByDeudaYEstado(deuJud.getId(), Estado.ACTIVO.getId());
			if(listDetalle!=null && !listDetalle.isEmpty()){
				log.error("deuda ya incluida en una ConDeuDet existente");
				addRecoverableError(GdeError.CONSTANCIADEU_DEU_INCLUIDA_EN_CONDEUDET);
				return false;					
			}
		}
		*/
		
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

	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
		
		if(estaEnGestionJudicial())
			addRecoverableError(GdeError.CONSTANCIADEU_MSJ_ELIMINAR_EN_GESJUD);
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {

		// validaciones comunes
		if(cuenta==null)
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,	GdeError.CONSTANCIADEU_CUENTA_LABEL);
		
		if(procurador==null)
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,	GdeError.CONSTANCIADEU_PROCURADOR_LABEL);
		
		if (hasError()) {
			return false;
		}

		return true;
	}

	// Metodos de negocio

	// --> ABM HistEstConDeu
	/**
	 * Realiza la creación de un registro del Historico de la Constancia de Deuda 
	 * @param logEstado
	 * @return HistEstConDeu
	 * @throws Exception
	 */	
	public HistEstConDeu createHistEstConDeu(String logEstado) throws Exception{
		
			HistEstConDeu histEstConDeu = new HistEstConDeu();
			
			histEstConDeu.setConstanciaDeu(this);
			histEstConDeu.setEstConDeu(EstConDeu.getById(EstConDeu.ID_HABILITADA));
			histEstConDeu.setFechaDesde(new Date());

			histEstConDeu.setLogEstado(logEstado);
			
			return createHistEstConDeu(histEstConDeu);
		}
	
	/**
	 * Realiza la creacion de un registro del Historico de la Constancia de Deuda
	 * @param  histEstConDeu
	 * @return HistEstConDeu
	 * @throws Exception
	 */
	public HistEstConDeu createHistEstConDeu(HistEstConDeu histEstConDeu) throws Exception {
		
		// Validaciones de negocio
		if (!histEstConDeu.validateCreate()) {
			return histEstConDeu;
		}

		GdeDAOFactory.getHistEstConDeuDAO().update(histEstConDeu);
		
		// creacion del hist 
		
		return histEstConDeu;
	}	

	/**
	 * Actualiza un registro del Historico de la Constancia de Deuda
	 * @param  histEstConDeu
	 * @return HistEstConDeu
	 * @throws Exception
	 */
	public HistEstConDeu updateHistEstConDeu(HistEstConDeu histEstConDeu) throws Exception {
		
		// Validaciones de negocio
		if (!histEstConDeu.validateUpdate()) {
			return histEstConDeu;
		}

		GdeDAOFactory.getHistEstConDeuDAO().update(histEstConDeu);
		
		return histEstConDeu;
	}	

	/**
	 * Elimina un registro del Historico de la Constancia de Deuda
	 * @param  histEstConDeu
	 * @return HistEstConDeu
	 * @throws Exception
	 */
	public HistEstConDeu deleteHistEstConDeu(HistEstConDeu histEstConDeu) throws Exception {
		
		// Validaciones de negocio
		if (!histEstConDeu.validateDelete()) {
			return histEstConDeu;
		}

		GdeDAOFactory.getHistEstConDeuDAO().delete(histEstConDeu);
		
		return histEstConDeu;
	}	
	
	
	public int deleteListHistEstConDeu() throws Exception {

		return GdeDAOFactory.getHistEstConDeuDAO().deleteByConstanciaDeu(this);
		
	}	

	// <-- ABM HistEstConDeu
	
	// --> ABM ConDeuDet
	/**
	 * Realiza la creacion del Detalle de la Constancia de Deuda a partir de la deuda
	 * 
	 * @param  deuda
	 * @return ConDeuDet
	 * @throws Exception
	 */
	public ConDeuDet createConDeuDet(Deuda deuda) throws Exception{
		
			ConDeuDet conDeuDet = new ConDeuDet();
			
			conDeuDet.setConstanciaDeu(this);
			conDeuDet.setIdDeuda(deuda.getId()); // o deudaJudicial
			// observacion = obs motivo no prescripta
			conDeuDet.setObservacion(deuda.getObsMotNoPre());
			
			return createConDeuDet(conDeuDet);
		}
	
	
	/**
	 * Realiza la creacion del Detalle de la Constancia de Deuda
	 * @param conDeuDet
	 * @return ConDeuDet
	 * @throws Exception
	 */
	public ConDeuDet createConDeuDet(ConDeuDet conDeuDet) throws Exception {
		
		// Validaciones de negocio
		if (!conDeuDet.validateCreate()) {
			return conDeuDet;
		}

		GdeDAOFactory.getConDeuDetDAO().update(conDeuDet);
		
		return conDeuDet;
	}	

	/**
	 * Actualiza el Detalle de la Constancia de Deuda
	 * @param  conDeuDet
	 * @return ConDeuDet
	 * @throws Exception
	 */
	public ConDeuDet updateConDeuDet(ConDeuDet conDeuDet) throws Exception {
		
		// Validaciones de negocio
		if (!conDeuDet.validateUpdate()) {
			return conDeuDet;
		}

		GdeDAOFactory.getConDeuDetDAO().update(conDeuDet);
		
		return conDeuDet;
	}	

	/**
	 * Borra el Detalle de la Constancia de Deuda
	 * @param conDeuDet
	 * @return ConDeuDet
	 * @throws Exception
	 */
	public ConDeuDet deleteConDeuDet(ConDeuDet conDeuDet) throws Exception {
		
		// Validaciones de negocio
		if (!conDeuDet.validateDelete()) {
			return conDeuDet;
		}

		GdeDAOFactory.getConDeuDetDAO().delete(conDeuDet);
		
		// TODO ver HistEstConDeu
		return conDeuDet;
	}	
	// <-- ABM ConDeuDet

	/**
	 *  Cambia el Estado de la Constancia al de id pasado como parametro. Registra el cambio en el Historico.
	 *  Si se pasa a "Modificado" dispara el cambiar estado de la Planilla asociada a "Modificada".
	 *  Si se pasa a "Cancelado", verifica si todas las constancias de la planilla estan cancelada y cambia el estado
	 *  de la Planilla a "Cancelada". 
	 *  @param idEstConDeu
	 */
	public void cambiarEstConDeu(Long idEstConDeu, String logEstado) throws Exception{
		EstConDeu estConDeu = EstConDeu.getById(idEstConDeu);
		this.setEstConDeu(estConDeu);
		
		GdeDAOFactory.getConstanciaDeuDAO().update(this);
		
		// Logeamos en la tabla de historico.
		HistEstConDeu histEstConDeu = new HistEstConDeu();
		histEstConDeu.setConstanciaDeu(this);
		histEstConDeu.setFechaDesde(new Date());
		histEstConDeu.setLogEstado(logEstado);
		histEstConDeu.setEstConDeu(estConDeu);
		
		this.createHistEstConDeu(histEstConDeu);
		
		// Si se pasa a "MODIFICADO" y la Constancia tiene una Planilla, se modifica su estado a "MODIFICADO"
		if(idEstConDeu == EstConDeu.ID_MODIFICADA && this.getPlaEnvDeuPro()!= null){
			this.getPlaEnvDeuPro().cambiarEstPlaEnvDeuPr(EstPlaEnvDeuPr.ID_MODIFICADA, logEstado);
		}
		// Si se pasa a "Cancelado", verifica si todas las constancias de la planilla estan cancelada y cambia el estado
		// de la Planilla a "Cancelada".
		if(idEstConDeu == EstConDeu.ID_CANCELADA && this.getPlaEnvDeuPro()!= null && this.getPlaEnvDeuPro().validarConstanciasCanceladas()){
			this.getPlaEnvDeuPro().cambiarEstPlaEnvDeuPr(EstPlaEnvDeuPr.ID_CANCELADA, logEstado);
		}
	}
	
	/**
	 * Obtiene la ConstanciaDeuPrint cargado con el contenido adecuado para la generacion del padron de constancias de deuda
	 * @return ConstanciaDeuPrint
	 * @throws Exception
	 */
	public ConstanciaDeuPrint getConstanciaDeuPrint() throws Exception{
		
		ConstanciaDeuPrint constanciaDeuPrint = new ConstanciaDeuPrint();
		constanciaDeuPrint.setNroConstancia(this.getNumero());
		constanciaDeuPrint.setDesTributo(this.getCuenta().getRecurso().getDesRecurso());
		CuentaTitular ctaTitPrinc = this.getCuenta().obtenerCuentaTitularPrincipal();
		if (ctaTitPrinc != null){
			Persona persona = ctaTitPrinc.getContribuyente().getPersona();
			if (persona != null) {
				constanciaDeuPrint.setCuit(persona.getCuit());
				constanciaDeuPrint.setDesTitularPrincipal(persona.getRepresent());
				if (persona.getDomicilio() != null) {
					constanciaDeuPrint.setDesdomicilio(persona.getDomicilio().getViewDomicilio());
				} else {
					constanciaDeuPrint.setDesdomicilio("Sin Datos. PersonaId=" + persona.getId());
				}
			} else {
				constanciaDeuPrint.setCuit("");
				constanciaDeuPrint.setDesTitularPrincipal("Sin datos. PersonaId=" + ctaTitPrinc.getContribuyente().getId());
				constanciaDeuPrint.setDesdomicilio("");				
			}
		}
		
		constanciaDeuPrint.setNroCuenta(this.getCuenta().getNumeroCuenta());
		
		constanciaDeuPrint.setDesDomicilioConstancia(this.getDesDomEnv());
		constanciaDeuPrint.setCatastral(this.getCuenta().getObjImp().getClaveFuncional());

		Procurador proc = this.getProcurador();
		constanciaDeuPrint.setIdProcurador(proc.getId());
		constanciaDeuPrint.setDesProcurador(proc.getDescripcion());
		
		for (ConDeuDet conDeuDet : this.getListConDeuDet()) {
			constanciaDeuPrint.addConDeuDetPrint(conDeuDet.getConDeuDetPrint());
		}
		
		return constanciaDeuPrint;
	}

	// ---> ABM ConDeuTit
		
	public ConDeuTit createConDeutit(ConDeuTit conDeuTit) throws Exception {
		
		// Validaciones de negocio
		if (!conDeuTit.validateCreate()) {
			return conDeuTit;
		}

		GdeDAOFactory.getConDeuTitDAO().update(conDeuTit);
		
		return conDeuTit;
	}	
	
	/**
	 * Borra el Detalle de la Constancia de Deuda
	 * @param conDeuDet
	 * @return ConDeuDet
	 * @throws Exception
	 */
	public ConDeuTit deleteConDeuTit(ConDeuTit conDeuTit) throws Exception {
		
		// Validaciones de negocio
		if (!conDeuTit.validateDelete()) {
			return conDeuTit;
		}

		GdeDAOFactory.getConDeuTitDAO().delete(conDeuTit);
		
		// TODO ver HistEstConDeu
		return conDeuTit;
	}
	
	
	public int deleteListConDeuTit() throws Exception {

		return GdeDAOFactory.getConDeuTitDAO().deleteByConstanciaDeu(this);
		
	}	

	// <--- ABM ConDeuTit
	

	/**
	 * Obtiene la lista de Deuda Judicial activa contenida en cada detalle activo de la constancia
	 * @return List<DeudaJudicial>
	 */
	public List<DeudaJudicial> getListDeudaJudicialActivas(){
		List<DeudaJudicial> listDeudaJudicial = new ArrayList<DeudaJudicial>();
		for (ConDeuDet conDeuDet : this.getListConDeuDet()) {
			if (Estado.ACTIVO.getId().equals(conDeuDet.getEstado()) ){
				DeudaJudicial dj = conDeuDet.getDeudaJudicial();
				
				if (dj != null && Estado.ACTIVO.getId().equals(dj.getEstado())){
					listDeudaJudicial.add(dj);
				}
			}
		}
		return listDeudaJudicial;
	}
	
	
	public void createListConDeuDet(List<Long> listIdDeudaJudicial, String observacion) throws Exception {
		
		for (Long idDeudaJudicial : listIdDeudaJudicial) {
			ConDeuDet conDeuDet = new ConDeuDet();
			conDeuDet.setIdDeuda(idDeudaJudicial);
			conDeuDet.setConstanciaDeu(this);
			conDeuDet.setObservacion(observacion);
			conDeuDet.setEstado(Estado.ACTIVO.getId());
			conDeuDet = this.createConDeuDet(conDeuDet);
			conDeuDet.passErrorMessages(this);
		}
	}
	
	/**
	 * Obtiene el ConDeuDet de la ConstanciaDeu que corresponde al idDeuda.
	 * Si no encuentra nada retorna null.
	 * @param  idDeuda
	 * @return ConDeuDet
	 */
	public ConDeuDet getConDeuDetByIdDeuda(Long idDeuda){
		
		for (ConDeuDet conDeuDet : this.getListConDeuDet()) {
			if (conDeuDet.getIdDeuda().equals(idDeuda)){
				return conDeuDet;
			}
		}
		return null;
	}
	
	/**
	 * Determina si la lista de Detalles de la Constancia contiene ConDeuDet activos 
	 * @return boolean 
	 */
	public boolean tieneConDeuDetActivos(){
		
		boolean tieneConDeuDetActivos = false;
		
		for (ConDeuDet cdd : this.getListConDeuDet()) {
			//TraDeuDet tdd = TraDeuDet.getActivoByIdDeuda(cdd.getIdDeuda());
			if (Estado.ACTIVO.getId().equals(cdd.getEstado())){
				// significa que existe un detalle de constancia activo
				tieneConDeuDetActivos = true;
				break;
			}
		}
		
		return tieneConDeuDetActivos;
	}

	/**
	 * Verifica si existe un GesJudDeu con la constancia
	 * @return
	 */
	public boolean estaEnGestionJudicial(){
		List<GesJudDeu> listGesJudDeu = GesJudDeu.getListByIdConstancia(getId());
		if(listGesJudDeu==null || listGesJudDeu.isEmpty())
			return false;
		return true;
	}

	public String getStrNumeroAnio(){
		if(this.getNumero()!=null && this.getNumero().longValue()>0 && 
																this.getAnio()!=null && this.getAnio().intValue()>0)
			return this.getNumero() + "/" + this.getAnio();
		return "";
	}
	
}
