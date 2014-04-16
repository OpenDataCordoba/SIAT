//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.bean;

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

import ar.gov.rosario.siat.cyq.buss.bean.Procedimiento;
import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import ar.gov.rosario.siat.ef.iface.model.ActaVO;
import ar.gov.rosario.siat.ef.iface.model.ComAjuVO;
import ar.gov.rosario.siat.ef.iface.model.EstadoOrdenVO;
import ar.gov.rosario.siat.ef.iface.model.HisEstOrdConVO;
import ar.gov.rosario.siat.ef.iface.model.OrdConCueVO;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlVO;
import ar.gov.rosario.siat.ef.iface.model.PeriodoOrdenVO;
import ar.gov.rosario.siat.ef.iface.model.PlaFueDatVO;
import ar.gov.rosario.siat.ef.iface.model.SupervisorVO;
import ar.gov.rosario.siat.ef.iface.model.TipoOrdenVO;
import ar.gov.rosario.siat.pad.buss.bean.Contribuyente;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.iface.model.ContribuyenteVO;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.helper.ListUtilBean;

/**
 * Bean correspondiente a OrdenControl
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_ordenControl")
public class OrdenControl extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idOpeInvCon")
	private OpeInvCon opeInvCon;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idContribuyente")
	private Contribuyente contribuyente;

	@Column(name = "numeroOrden")
	private Integer numeroOrden;

	@Column(name = "anioOrden")
	private Integer anioOrden;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idTipoOrden")
	private TipoOrden tipoOrden;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idEstadoOrden")
	private EstadoOrden estadoOrden;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idInspector")
	private Inspector inspector;

	@Column(name = "observacion")
	private String observacion;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idTipoPeriodo")
	private TipoPeriodo tipoPeriodo;

	@Column(name = "idCaso")
	private String idCaso;

	@Column(name = "esPrincipal")
	private Integer esPrincipal;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idOrigenOrden")
	private OrigenOrden origenOrden;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idOrdenOrigen")
	private OrdenControl ordenControlOrigen;

	@Column(name = "fechaEmision")
	private Date fechaEmision;
	
	@Column(name = "fechaCierre")
	private Date fechaCierre;
	
	@Column(name = "fechaImpresion")
	private Date fechaImpresion;
	
	@Column(name="obsInv")
	private String obsInv;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idSupervisor")
	private Supervisor supervisor;
	
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="idOrdenControl")
	private List<OrdConCue> listOrdConCue;
	
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="idOrdenControl")
	private List<HisEstOrdCon> listHistEstOrdCon;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idProcedimiento")
	private Procedimiento procedimiento;
	
	@OneToMany(mappedBy="ordenControl",fetch=FetchType.LAZY)
	@JoinColumn(name="idOrdenControl")
	@OrderBy(clause="idOrdConCue, anio, periodo")
	private List<PeriodoOrden> listPeriodoOrden;
	
	@OneToMany(mappedBy="ordenControl",fetch=FetchType.LAZY)
	@JoinColumn(name="idOrdenControl")
	@OrderBy(clause="numeroActa")
	private List<Acta> listActa;
	
	@OneToMany( mappedBy="ordenControl")
	@JoinColumn(name="idOrdenControl")
	private List<PlaFueDat> listPlaFueDat = new ArrayList<PlaFueDat>();

	@OneToMany( mappedBy="ordenControl")
	@JoinColumn(name="idOrdenControl")
	private List<Comparacion> listComparacion = new ArrayList<Comparacion>();

	@OneToMany( mappedBy="ordenControl")
	@JoinColumn(name="idOrdenControl")
	private List<OrdConBasImp> listOrdConBasImp = new ArrayList<OrdConBasImp>();

	@OneToMany( mappedBy="ordenControl")
	@JoinColumn(name="idOrdenControl")
	@OrderBy(clause="fecha desc")
	private List<DetAju> listDetAju = new ArrayList<DetAju>();
	
	@OneToMany( mappedBy="ordenControl")
	@JoinColumn(name="idOrdenControl")
	private List<MesaEntrada> listMesaEntrada = new ArrayList<MesaEntrada>();

	@OneToMany( mappedBy="ordenControl")
	@JoinColumn(name="idOrdenControl")
	private List<AproOrdCon> listAproOrdCon = new ArrayList<AproOrdCon>();

	@OneToMany( mappedBy="ordenControl")
	@JoinColumn(name="idOrdenControl")
	private List<DetAjuDocSop> listDetAjuDocSop = new ArrayList<DetAjuDocSop>();
	
	@OneToMany( mappedBy="ordenControl")
	@JoinColumn(name="idOrdenControl")
	private List<ComAju> listComAju = new ArrayList<ComAju>();
	
	
	
	// <#Propiedades#>

	// Constructores
	public OrdenControl() {
		super();
		// Seteo de valores default
	}

	public OrdenControl(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static OrdenControl getById(Long id) {
		return (OrdenControl) EfDAOFactory.getOrdenControlDAO().getById(id);
	}

	public static OrdenControl getByIdNull(Long id) {
		return (OrdenControl) EfDAOFactory.getOrdenControlDAO().getByIdNull(id);
	}

	public static List<OrdenControl> getList() {
		return (List<OrdenControl>) EfDAOFactory.getOrdenControlDAO().getList();
	}

	public static List<OrdenControl> getListActivos() {
		return (List<OrdenControl>) EfDAOFactory.getOrdenControlDAO().getListActiva();
	}

	/**
	 * Obtiene una lista de ordenControl filtrando por idCOntribuyente y excluyendo la que tiene el id pasado como parametro	 * 
	 * @param idContribuyente
	 * @param idOrdenControlExcluir - puede ser null
	 * @return
	 */
	public static List<OrdenControl> getByIdContribuyente(Long idContribuyente, Long idOrdenControlExcluir) {
		return EfDAOFactory.getOrdenControlFisDAO().getByIdContribuyente(idContribuyente, idOrdenControlExcluir);		
	}

	public static OrdenControl getByNumeroAnio(Integer numero, Integer anio){
		return EfDAOFactory.getOrdenControlDAO().getByNumeroAnio(numero,anio);
	}
	
	public static OrdenControl getByCaso(String idcaso){
		return EfDAOFactory.getOrdenControlDAO().getByCaso(idcaso);		
	}
	
	public static OrdenControl getByNumeroAnioCaso(Integer numero, Integer anio, String idcaso){
		return EfDAOFactory.getOrdenControlDAO().getByNumeroAnioCaso(numero, anio,idcaso);
	}
	
	public static List<Long> getListOrdenControlByCuentaInPeriodoOrden(Cuenta cuenta){
		return EfDAOFactory.getOrdenControlFisDAO().getListOrdenControlByCuentaInPeriodoOrden(cuenta);
	}
	
	public static List<OrdenControl> getListOrdenControlByCuenta(Cuenta cuenta){
		return EfDAOFactory.getOrdenControlFisDAO().getListOrdenControlByCuenta(cuenta);
	}
	
	// Getters y setters
	public OpeInvCon getOpeInvCon() {
		return opeInvCon;
	}

	public void setOpeInvCon(OpeInvCon opeInvCon) {
		this.opeInvCon = opeInvCon;
	}

	public Contribuyente getContribuyente() {
		return contribuyente;
	}

	public void setContribuyente(Contribuyente contribuyente) {
		this.contribuyente = contribuyente;
	}

	public Integer getNumeroOrden() {
		return numeroOrden;
	}

	public void setNumeroOrden(Integer numeroOrden) {
		this.numeroOrden = numeroOrden;
	}

	public Integer getAnioOrden() {
		return anioOrden;
	}

	public void setAnioOrden(Integer anioOrden) {
		this.anioOrden = anioOrden;
	}

	public TipoOrden getTipoOrden() {
		return tipoOrden;
	}

	public void setTipoOrden(TipoOrden tipoOrden) {
		this.tipoOrden = tipoOrden;
	}

	public EstadoOrden getEstadoOrden() {
		return estadoOrden;
	}

	public void setEstadoOrden(EstadoOrden estadoOrden) {
		this.estadoOrden = estadoOrden;
	}

	public Inspector getInspector() {
		return inspector;
	}

	public void setInspector(Inspector inspector) {
		this.inspector = inspector;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public TipoPeriodo getTipoPeriodo() {
		return tipoPeriodo;
	}

	public void setTipoPeriodo(TipoPeriodo tipoPeriodo) {
		this.tipoPeriodo = tipoPeriodo;
	}

	public String getIdCaso() {
		return idCaso;
	}

	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}

	public Integer getEsPrincipal() {
		return esPrincipal;
	}

	public void setEsPrincipal(Integer esPrincipal) {
		this.esPrincipal = esPrincipal;
	}

	public OrigenOrden getOrigenOrden() {
		return origenOrden;
	}

	public void setOrigenOrden(OrigenOrden origenOrden) {
		this.origenOrden = origenOrden;
	}

	public OrdenControl getOrdenControlOrigen() {
		return ordenControlOrigen;
	}

	public void setOrdenControlOrigen(OrdenControl ordenControlOrigen) {
		this.ordenControlOrigen = ordenControlOrigen;
	}
	
	public List<OrdConCue> getListOrdConCue() {
		return listOrdConCue;
	}

	public void setListOrdConCue(List<OrdConCue> listOrdConCue) {
		this.listOrdConCue = listOrdConCue;
	}
	
	public Date getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public Date getFechaImpresion() {
		return fechaImpresion;
	}
	
	public Date getFechaCierre() {
		return fechaCierre;
	}

	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}
	
    public void setFechaImpresion(Date fechaImpresion) {
		this.fechaImpresion = fechaImpresion;
	}

	public String getObsInv() {
		return obsInv;
	}

	public void setObsInv(String obsInv) {
		this.obsInv = obsInv;
	}

	public List<HisEstOrdCon> getListHistEstOrdCon() {
		return listHistEstOrdCon;
	}

	public void setListHistEstOrdCon(List<HisEstOrdCon> listHistEstOrdCon) {
		this.listHistEstOrdCon = listHistEstOrdCon;
	}	
	
	public Procedimiento getProcedimiento() {
		return procedimiento;
	}
	public void setProcedimiento(Procedimiento procedimiento) {
		this.procedimiento = procedimiento;
	}
	
	public List<PeriodoOrden> getListPeriodoOrden() {
		return listPeriodoOrden;
	}

	public void setListPeriodoOrden(List<PeriodoOrden> listPeriodoOrden) {
		this.listPeriodoOrden = listPeriodoOrden;
	}

	public Supervisor getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(Supervisor supervisor) {
		this.supervisor = supervisor;
	}

	
	public List<Acta> getListActa() {
		return listActa;
	}

	public void setListActa(List<Acta> listActa) {
		this.listActa = listActa;
	}

	public List<PlaFueDat> getListPlaFueDat() {
		return listPlaFueDat;
	}
	

	public void setListPlaFueDat(List<PlaFueDat> listPlaFueDat) {
		this.listPlaFueDat = listPlaFueDat;
	}

	public List<Comparacion> getListComparacion() {
		return listComparacion;
	}

	public void setListComparacion(List<Comparacion> listComparacion) {
		this.listComparacion = listComparacion;
	}

	public List<OrdConBasImp> getListOrdConBasImp() {
		return listOrdConBasImp;
	}

	public void setListOrdConBasImp(List<OrdConBasImp> listOrdConBasImp) {
		this.listOrdConBasImp = listOrdConBasImp;
	}

	public List<DetAju> getListDetAju() {
		return listDetAju;
	}

	public void setListDetAju(List<DetAju> listDetAju) {
		this.listDetAju = listDetAju;
	}

	public List<MesaEntrada> getListMesaEntrada() {
		return listMesaEntrada;
	}

	public void setListMesaEntrada(List<MesaEntrada> listMesaEntrada) {
		this.listMesaEntrada = listMesaEntrada;
	}

	public List<AproOrdCon> getListAproOrdCon() {
		return listAproOrdCon;
	}

	public void setListAproOrdCon(List<AproOrdCon> listAproOrdCon) {
		this.listAproOrdCon = listAproOrdCon;
	}

	public List<DetAjuDocSop> getListDetAjuDocSop() {
		return listDetAjuDocSop;
	}

	public void setListDetAjuDocSop(List<DetAjuDocSop> listDetAjuDocSop) {
		this.listDetAjuDocSop = listDetAjuDocSop;
	}

	public List<ComAju> getListComAju() {
		return listComAju;
	}

	public void setListComAju(List<ComAju> listComAju) {
		this.listComAju = listComAju;
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
		//clearError();

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
	/**
	 * Obtiene la base imponible de la lista, para el periodo y anio pasado como parametro.
	 * @return null si no encuentra ninguna base imponible para los datos.
	 */
	public OrdConBasImp getOrdConBasImp(Integer periodo, Integer anio, OrdConCue ordConCue) {
		
		Long periodoAnioEvaluar = anio*100L+periodo;
		for(OrdConBasImp ordConBasImp: this.getListOrdConBasImpByOrdConCue(ordConCue)){
			Long periodoDesdeBasImp = ordConBasImp.getAnioDesde()*100L+ordConBasImp.getPeriodoDesde();
			Long periodoHastaBasImp = ordConBasImp.getAnioHasta()*100L+ordConBasImp.getPeriodoHasta();
			
			if(periodoAnioEvaluar>=periodoDesdeBasImp && periodoAnioEvaluar<=periodoHastaBasImp)
				return ordConBasImp;
		}
		return null;
	}



	// <#MetodosBeanDetalle#>
	
	// ---> ABM OrdConCue
	public OrdConCue createOrdConCue(OrdConCue ordConCue) throws Exception {
		// Validaciones de negocio
		if (!ordConCue.validateCreate()) {
			return ordConCue;
		}
		EfDAOFactory.getOrdConCueDAO().update(ordConCue);
				
		return ordConCue;
	}
	
	public OrdConCue updateOrdConCue(OrdConCue ordConCue) throws Exception {

		// Validaciones de negocio
		if (!ordConCue.validateUpdate()) {
			return ordConCue;
		}
		EfDAOFactory.getOrdConCueDAO().update(ordConCue);
		
		return ordConCue;
	}

	public OrdConCue deleteOrdConCue(OrdConCue ordConCue) throws Exception {

		// Validaciones de negocio
		if (!ordConCue.validateDelete()) {
			return ordConCue;
		}

		EfDAOFactory.getOrdConCueDAO().delete(ordConCue);

		return ordConCue;
	}
	// <--- ABM OrdConCue	

	// ---> ABM hisEstOrdCon
	public HisEstOrdCon createHisEstOrdCon(HisEstOrdCon hisEstOrdCon) throws Exception {
		// Validaciones de negocio
		if (!hisEstOrdCon.validateCreate()) {
			return hisEstOrdCon;
		}
		EfDAOFactory.getHisEstOrdConDAO().update(hisEstOrdCon);
				
		return hisEstOrdCon;
	}
	
	public HisEstOrdCon updateHisEstOrdCon(HisEstOrdCon hisEstOrdCon) throws Exception {

		// Validaciones de negocio
		if (!hisEstOrdCon.validateUpdate()) {
			return hisEstOrdCon;
		}
		EfDAOFactory.getHisEstOrdConDAO().update(hisEstOrdCon);
		
		return hisEstOrdCon;
	}

	public HisEstOrdCon deleteHisEstOrdCon(HisEstOrdCon hisEstOrdCon) throws Exception {

		// Validaciones de negocio
		if (!hisEstOrdCon.validateDelete()) {
			return hisEstOrdCon;
		}

		EfDAOFactory.getHisEstOrdConDAO().delete(hisEstOrdCon);

		return hisEstOrdCon;
	}
	// <--- ABM hisEstOrdCon

	// ---> ABM periodoOrden
	public PeriodoOrden createPeriodoOrden(PeriodoOrden periodoOrden) throws Exception {
		// Validaciones de negocio
		if (!periodoOrden.validateCreate()) {
			return periodoOrden;
		}
		EfDAOFactory.getPeriodoOrdenDAO().update(periodoOrden);
				
		return periodoOrden;
	}
	
	public PeriodoOrden deletePeriodoOrden(PeriodoOrden periodoOrden) throws Exception {

		// Validaciones de negocio
		if (!periodoOrden.validateDelete()) {
			return periodoOrden;
		}

		EfDAOFactory.getPeriodoOrdenDAO().delete(periodoOrden);

		return periodoOrden;
	}
	
	// <--- ABM periodoOrden
	
	// ---> ABM Acta
	public Acta createActa(Acta acta) throws Exception {
		// Validaciones de negocio
		if (!acta.validateCreate()) {
			return acta;
		}
		EfDAOFactory.getActaDAO().update(acta);
				
		return acta;
	}
	
	public Acta updateActa(Acta acta) throws Exception {

		// Validaciones de negocio
		if (!acta.validateUpdate()) {
			return acta;
		}
		EfDAOFactory.getActaDAO().update(acta);
		
		return acta;
	}

	public Acta deleteActa(Acta acta) throws Exception {

		// Validaciones de negocio
		if (!acta.validateDelete()) {
			return acta;
		}

		EfDAOFactory.getActaDAO().delete(acta);

		return acta;
	}	
	// <--- ABM Acta

	// ---> ABM InicioInv
	public InicioInv createInicioInv(InicioInv inicioInv) throws Exception {
		// Validaciones de negocio
		if (!inicioInv.validateCreate()) {
			return inicioInv;
		}
		EfDAOFactory.getInicioInvDAO().update(inicioInv);
				
		return inicioInv;
	}
	
	public InicioInv updateInicioInv(InicioInv inicioInv) throws Exception {

		// Validaciones de negocio
		if (!inicioInv.validateUpdate()) {
			return inicioInv;
		}
		EfDAOFactory.getInicioInvDAO().update(inicioInv);
		
		return inicioInv;
	}	
	// <--- ABM InicioInv

	// ---> ABM PlaFueDat
	public PlaFueDat createPlaFueDat(PlaFueDat plaFueDat) throws Exception {
		// Validaciones de negocio
		if (!plaFueDat.validateCreate()) {
			return plaFueDat;
		}
		EfDAOFactory.getPlaFueDatDAO().update(plaFueDat);
				
		return plaFueDat;
	}
	
	public PlaFueDat updatePlaFueDat(PlaFueDat plaFueDat) throws Exception {

		// Validaciones de negocio
		if (!plaFueDat.validateUpdate()) {
			return plaFueDat;
		}
		EfDAOFactory.getPlaFueDatDAO().update(plaFueDat);
		
		return plaFueDat;
	}

	public PlaFueDat deletePlaFueDat(PlaFueDat plaFueDat) throws Exception {

		// Validaciones de negocio
		if (!plaFueDat.validateDelete()) {
			return plaFueDat;
		}

		EfDAOFactory.getPlaFueDatDAO().delete(plaFueDat);

		return plaFueDat;
	}	
	// <--- ABM PlaFueDat
	
	// ---> ABM Comparacion
	public Comparacion createComparacion(Comparacion comparacion) throws Exception {
		// Validaciones de negocio
		if (!comparacion.validateCreate()) {
			return comparacion;
		}
		EfDAOFactory.getComparacionDAO().update(comparacion);
				
		return comparacion;
	}
	
	public Comparacion updateComparacion(Comparacion comparacion) throws Exception {

		// Validaciones de negocio
		if (!comparacion.validateUpdate()) {
			return comparacion;
		}
		EfDAOFactory.getComparacionDAO().update(comparacion);
		
		return comparacion;
	}

	public Comparacion deleteComparacion(Comparacion comparacion) throws Exception {

		// Validaciones de negocio
		if (!comparacion.validateDelete()) {
			return comparacion;
		}

		EfDAOFactory.getComparacionDAO().delete(comparacion);

		return comparacion;
	}	
	// <--- ABM Comparacion
	
	//	---> ABM OrdConBasImp
	public OrdConBasImp createOrdConBasImp(OrdConBasImp ordConBasImp) throws Exception {

		// Validaciones de negocio
		if (!ordConBasImp.validateCreate()) {
			return ordConBasImp;
		}

		EfDAOFactory.getOrdConBasImpDAO().update(ordConBasImp);

		return ordConBasImp;
	}
	
	public OrdConBasImp deleteOrdConBasImp(OrdConBasImp ordConBasImp) throws Exception {

		// Validaciones de negocio
		if (!ordConBasImp.validateDelete()) {
			return ordConBasImp;
		}

		EfDAOFactory.getOrdConBasImpDAO().delete(ordConBasImp);

		return ordConBasImp;
	}	
	//	<--- ABM OrdConBasImp
		
	//	---> ABM DetAju
    public DetAju createDetAju(DetAju detAju) throws Exception {

		// Validaciones de negocio
		if (!detAju.validateCreate()) {
			return detAju;
		}

		EfDAOFactory.getDetAjuDAO().update(detAju);

		return detAju;
	}
	
    public DetAju updateDetAju(DetAju detAju) throws Exception {
		
		// Validaciones de negocio
		if (!detAju.validateUpdate()) {
			return detAju;
		}

		EfDAOFactory.getDetAjuDAO().update(detAju);
		
		return detAju;
	}
	
    public DetAju deleteDetAju(DetAju detAju) throws Exception {
	
		// Validaciones de negocio
		if (!detAju.validateDelete()) {
			return detAju;
		}
		
		EfDAOFactory.getDetAjuDAO().delete(detAju);
		
        return detAju;
	}
    // <--- ABM DetAju
    
	//	---> ABM MesaEntrada
	public MesaEntrada createMesaEntrada(MesaEntrada mesaEntrada) throws Exception {

		// Validaciones de negocio
		if (!mesaEntrada.validateCreate()) {
			return mesaEntrada;
		}

		EfDAOFactory.getMesaEntradaDAO().update(mesaEntrada);

		return mesaEntrada;
	}
	
	public MesaEntrada updateMesaEntrada(MesaEntrada mesaEntrada) throws Exception {
		
		// Validaciones de negocio
		if (!mesaEntrada.validateUpdate()) {
			return mesaEntrada;
		}

		EfDAOFactory.getMesaEntradaDAO().update(mesaEntrada);
		
		return mesaEntrada;
	}
	
	public MesaEntrada deleteMesaEntrada(MesaEntrada mesaEntrada) throws Exception {
	
		// Validaciones de negocio
		if (!mesaEntrada.validateDelete()) {
			return mesaEntrada;
		}
		
		EfDAOFactory.getMesaEntradaDAO().delete(mesaEntrada);
		
		return mesaEntrada;
	}
	// <--- ABM MesaEntrada
	
	//	---> ABM AproOrdCon
	public AproOrdCon createAproOrdCon(AproOrdCon aproOrdCon) throws Exception {

		// Validaciones de negocio
		if (!aproOrdCon.validateCreate()) {
			return aproOrdCon;
		}

		EfDAOFactory.getAproOrdConDAO().update(aproOrdCon);

		return aproOrdCon;
	}
	
	public AproOrdCon updateAproOrdCon(AproOrdCon aproOrdCon) throws Exception {
		
		// Validaciones de negocio
		if (!aproOrdCon.validateUpdate()) {
			return aproOrdCon;
		}

		EfDAOFactory.getAproOrdConDAO().update(aproOrdCon);
		
		return aproOrdCon;
	}
	
	public AproOrdCon deleteAproOrdCon(AproOrdCon aproOrdCon) throws Exception {
	
		// Validaciones de negocio
		if (!aproOrdCon.validateDelete()) {
			return aproOrdCon;
		}
		
		EfDAOFactory.getAproOrdConDAO().delete(aproOrdCon);
		
		return aproOrdCon;
	}
	// <--- ABM AproOrdCon
    
	//	---> ABM DetAjuDocSop
	public DetAjuDocSop createDetAjuDocSop(DetAjuDocSop detAjuDocSop) throws Exception {

		// Validaciones de negocio
		if (!detAjuDocSop.validateCreate()) {
			return detAjuDocSop;
		}

		EfDAOFactory.getDetAjuDocSopDAO().update(detAjuDocSop);

		return detAjuDocSop;
	}
	
	public DetAjuDocSop updateDetAjuDocSop(DetAjuDocSop detAjuDocSop) throws Exception {
		
		// Validaciones de negocio
		if (!detAjuDocSop.validateUpdate()) {
			return detAjuDocSop;
		}

		EfDAOFactory.getDetAjuDocSopDAO().update(detAjuDocSop);
		
		return detAjuDocSop;
	}
	
	public DetAjuDocSop deleteDetAjuDocSop(DetAjuDocSop detAjuDocSop) throws Exception {
	
		// Validaciones de negocio
		if (!detAjuDocSop.validateDelete()) {
			return detAjuDocSop;
		}
		
		EfDAOFactory.getDetAjuDocSopDAO().delete(detAjuDocSop);
		
		return detAjuDocSop;
	}
	// <--- ABM DetAjuDocSop

	//	---> ABM ComAju
	public ComAju createComAju(ComAju comAju) throws Exception {

		// Validaciones de negocio
		if (!comAju.validateCreate()) {
			return comAju;
		}

		EfDAOFactory.getComAjuDAO().update(comAju);

		return comAju;
	}
	
	public ComAju updateComAju(ComAju comAju) throws Exception {
		
		// Validaciones de negocio
		if (!comAju.validateUpdate()) {
			return comAju;
		}

		EfDAOFactory.getComAjuDAO().update(comAju);
		
		return comAju;
	}
	
	public ComAju deleteComAju(ComAju comAju) throws Exception {
	
		// Validaciones de negocio
		if (!comAju.validateDelete()) {
			return comAju;
		}
		
		EfDAOFactory.getComAjuDAO().delete(comAju);
		
		return comAju;
	}
	// <--- ABM ComAju

	
	
	
	/**
	 * Hace toVO(1, false); setea el opeInvCon con el domicilio y el contribuyente
	 * @param withListOrdConCue - copia la lista de ordConcue de la ordenControl
	 * @param withListHistEstOrdCon - copia el historico de estados de la ordenControl
	 * @return
	 * @throws Exception
	 */
	public OrdenControlVO toVOForView(boolean withListOrdConCue, boolean withListHistEstOrdCon) throws Exception {
		OrdenControlVO ordenControlVO = (OrdenControlVO) this.toVO(1, false);
		
		if(supervisor==null)
			ordenControlVO.setSupervisor(new SupervisorVO());
		
		contribuyente.loadPersonaFromMCR();
		ordenControlVO.setContribuyente((ContribuyenteVO) contribuyente.toVO(3, false));
		
		if(withListOrdConCue && listOrdConCue!=null){
			List<OrdConCueVO> listOrdConCueVO = new ArrayList<OrdConCueVO>();
			for(OrdConCue ordConCue: listOrdConCue){
				OrdConCueVO ordConCueVO = (OrdConCueVO) ordConCue.toVO(0, false);
				ordConCueVO.setCuenta(ordConCue.getCuenta().toVOWithRecurso());
				listOrdConCueVO.add(ordConCueVO);
			}
			ordenControlVO.setListOrdConCue(listOrdConCueVO);
		}else{
			ordenControlVO.setListOrdConCue(null);
		}
		
		if(withListHistEstOrdCon && listHistEstOrdCon!=null){
			List<HisEstOrdConVO> listHisEstOrdConVO = new ArrayList<HisEstOrdConVO>();
			for(HisEstOrdCon hisEstOrdCon: listHistEstOrdCon){
				HisEstOrdConVO hisEstOrdConVO = new HisEstOrdConVO();
				hisEstOrdConVO.setFecha(hisEstOrdCon.getFecha());
				hisEstOrdConVO.setObservacion(hisEstOrdCon.getObservacion());
				hisEstOrdConVO.setEstadoOrden((EstadoOrdenVO) hisEstOrdCon.getEstadoOrden().toVO(0, false));
				listHisEstOrdConVO.add(hisEstOrdConVO);
			}
			ordenControlVO.setListHisEstOrdCon(listHisEstOrdConVO);
		}else{
			ordenControlVO.setListHisEstOrdCon(null);
		}
		
		if(this.getProcedimiento()!=null){
			ordenControlVO.setProcedimiento(this.getProcedimiento().toVOWithPersona());
		}
		
		return ordenControlVO;
	}

	/**
	 * toVOForView(true, false). Setea la lista de periodoOrden, lista de actas, lista de plaFueDat, lista de comparaciones
	 * @return
	 * @throws Exception
	 */
	public OrdenControlVO toVO4Admin() throws Exception{
		OrdenControlVO ordenControlVO = this.toVOForView(true, false);

		// Lista de periodos Orden
		if(listPeriodoOrden!=null){
			List<PeriodoOrdenVO> listPeriodoOrdenVO = new ArrayList<PeriodoOrdenVO>();
			for(PeriodoOrden p:listPeriodoOrden){
				listPeriodoOrdenVO.add(p.toVO4Admin());
			}
			ordenControlVO.setListPeriodoOrden(listPeriodoOrdenVO);
		}

		// Lista de Actas 
		if(listActa!=null){
			List<ActaVO> listActaVO = new ArrayList<ActaVO>();
			for(Acta acta:listActa){
				listActaVO.add((ActaVO) acta.toVO(1, false));
			}
			ordenControlVO.setListActa(listActaVO);
		}

		// lista de fuentes
		if(listPlaFueDat!=null){
			List<PlaFueDatVO> listPlaFueDatVO = new ArrayList<PlaFueDatVO>();
			for(PlaFueDat plaFueDat:listPlaFueDat){
				listPlaFueDatVO.add((PlaFueDatVO) plaFueDat.toVO4Planilla());
			}
			ordenControlVO.setListPlaFueDat(listPlaFueDatVO);
		}		
		
		// lista de comparaciones
		if(listComparacion!=null){
			ordenControlVO.setListComparacion(ListUtilBean.toVO(listComparacion, 0, false));
		}
		
		// lista de bases imponibles
		if(listOrdConBasImp!=null){
			ordenControlVO.setListOrdConBasImp(ListUtilBean.toVO(listOrdConBasImp, 3, false));
		}
		
		// lista de ajustes
		if(listDetAju!=null){
			ordenControlVO.setListDetAju(ListUtilBean.toVO(listDetAju, 3, false));
		}
		
		// lista de mesaEntrada
		if(listMesaEntrada!=null){
			ordenControlVO.setListMesaEntrada(ListUtilBean.toVO(listMesaEntrada, 1, false));
		}
		
		// lista de aprobaciones
		if(listAproOrdCon!=null){
			ordenControlVO.setListAproOrdCon(ListUtilBean.toVO(listAproOrdCon, 1, false));
		}
		
		// lista de resoluciones
		if(listDetAjuDocSop!=null){
			ordenControlVO.setListDetAjuDocSop(ListUtilBean.toVO(listDetAjuDocSop, 1, false));
		}
		
		// lista de Compensaciones
		List<ComAjuVO> listComAjuVO = new ArrayList<ComAjuVO>();
		if(listComAju!=null){
			for(ComAju comAju: listComAju){
				listComAjuVO.add(comAju.toVO4View());
			}
		}
		ordenControlVO.setListComAju(listComAjuVO);
		
		if(this.getProcedimiento()!=null){
			ordenControlVO.setProcedimiento(this.getProcedimiento().toVOWithPersona());
		}
		
		return ordenControlVO;
	}

	public OrdenControlVO toVO4Print() throws Exception{

		OrdenControlVO ordenControlVO =(OrdenControlVO) this.toVO(2, false);
		ordenControlVO.setTipoOrden((TipoOrdenVO) this.tipoOrden.toVO4Print());
		contribuyente.loadPersonaFromMCR();
		ordenControlVO.setContribuyente((ContribuyenteVO) this.getContribuyente().toVO(4,false)); //hasta llegar a la calle
		// Lista de periodos Orden
		if(listPeriodoOrden!=null){
			List<PeriodoOrdenVO> listPeriodoOrdenVO = new ArrayList<PeriodoOrdenVO>();
			for(PeriodoOrden p:listPeriodoOrden){
				listPeriodoOrdenVO.add(p.toVO4Print());
			}
			ordenControlVO.setListPeriodoOrden(listPeriodoOrdenVO);
		}
		
		return ordenControlVO;
	}

	// metodos para el reporte
	public String getCUIT4Report() throws Exception{
		contribuyente.loadPersonaFromMCR();
		return contribuyente.getPersona().getCuit();
	}

	public String getDomicilio4Report(){
		return contribuyente.getPersona().getDomicilio().getViewDomicilio();
	}
	
	public Long getNextNroActa(){
		return EfDAOFactory.getActaDAO().getUltNroActa(this)+1;
	}
	
	public List<OrdConBasImp>getListOrdConBasImpByOrdConCue(OrdConCue ordConCue){
		List<OrdConBasImp>listOrdConBasImp=new ArrayList<OrdConBasImp>();
		for (OrdConBasImp ordConBasImp: this.getListOrdConBasImp()){
			if ((ordConCue==null && ordConBasImp.getOrdConCue()==null) || (ordConBasImp.getOrdConCue()!=null && ordConBasImp.getOrdConCue().equals(ordConCue)))
				listOrdConBasImp.add(ordConBasImp);
		}
		
		return listOrdConBasImp;
	}


}
