//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.bean;

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

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.ObjImp;
import ar.gov.rosario.siat.pad.buss.bean.ObjImpAtrVal;
import ar.gov.rosario.siat.rec.buss.dao.RecDAOFactory;
import ar.gov.rosario.siat.rec.iface.model.PlaCuaDetVO;
import ar.gov.rosario.siat.rec.iface.util.RecError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Bean correspondiente a PlaCuaDet
 * 
 * @author tecso
 */
@Entity
@Table(name = "cdm_plaCuaDet")
public class PlaCuaDet extends BaseBO {
	
	private static Logger log = Logger.getLogger(PlaCuaDet.class);
	
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idPlanillaCuadra")
	private PlanillaCuadra planillaCuadra;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idTipPlaCuaDet")
	private TipPlaCuaDet tipPlaCuaDet;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idEstPlaCuaDet") 
	private EstPlaCuaDet estPlaCuaDet;

	@ManyToOne(fetch=FetchType.LAZY) 
    @JoinColumn(name="idCuentaTGI") 
	private Cuenta cuentaTGI;
	
	@Column(name="cantidadMetros")
	private Double cantidadMetros;
	
	@Column(name="cantidadUnidades")
	private Double cantidadUnidades;

	@Column(name="valuacionTerreno")
	private Double valuacionTerreno;
	
	@Column(name="idradio")
	private Double radio;
	
	@Column(name="agrupador")
	private String agrupador;
	
	@ManyToOne(fetch=FetchType.LAZY) 
    @JoinColumn(name="idPlaCuaDetPadre") 
	private PlaCuaDet plaCuaDetPadre;
	
	@Column(name="porcPH")
	private Double porcPH;
	
	@ManyToOne(fetch=FetchType.LAZY) 
    @JoinColumn(name="idCuentaCdM") 
	private Cuenta cuentaCdM;
	
	@Column(name="fechaIncorporacion")
	private Date fechaIncorporacion;
	
	@Column(name="fechaUltMdfDatos")
	private Date fechaUltMdfDatos;
	
	@Column(name="fechaUltCmbOI")
	private Date fechaUltCmbOI;

	@Column(name="fechaEmision")
	private Date fechaEmision;
	
	@Column(name="importeTotal")
	private Double importeTotal;
	
	@Column(name="usoCatastro")
	private Integer usoCatastro;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idUsoCdM")
	private UsoCdM usoCdM;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idObrForPag")
	private ObraFormaPago obrForPag;
	
	@Column(name="cantCuotas")
	private Integer cantCuotas;

	@Column(name="fechaForm")
	private Date fechaForm;

	@Column(name="valMetFre")
	private Double valMetFre;

	@Column(name="valUniTri")
	private Double valUniTri;

	// esta es la lista de hijos en caso 
	// de que el detalle sea una carpeta
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="idPlaCuaDetPadre")
	private List<PlaCuaDet> listPlaCuaDetHijos;
	

	// Constructores
	public PlaCuaDet(){
		super();
	}
	
	public PlaCuaDet(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static PlaCuaDet getById(Long id) {
		return (PlaCuaDet) RecDAOFactory.getPlaCuaDetDAO().getById(id);
	}
	
	public static PlaCuaDet getByIdNull(Long id) {
		return (PlaCuaDet) RecDAOFactory.getPlaCuaDetDAO().getByIdNull(id);
	}
	
	public static PlaCuaDet getByCuentaTGI(Cuenta cuentaTGI, Long idObra) throws Exception {
		return (PlaCuaDet) RecDAOFactory.getPlaCuaDetDAO().getByIdCuentaTGI(cuentaTGI.getId(), idObra);
	}
	
	public static List<PlaCuaDet> getList() {
		return (ArrayList<PlaCuaDet>) RecDAOFactory.getPlaCuaDetDAO().getList();
	}
	
	public static List<PlaCuaDet> getListActivos() {			
		return (ArrayList<PlaCuaDet>) RecDAOFactory.getPlaCuaDetDAO().getListActiva();
	}
	
	public static PlaCuaDet getByCuentaCdM(Cuenta cuenta) throws Exception{
		return (PlaCuaDet) RecDAOFactory.getPlaCuaDetDAO().getByCuentaCdM(cuenta);
	}
	
	// Getters y setters
	public PlanillaCuadra getPlanillaCuadra() {
		return planillaCuadra;
	}

	public void setPlanillaCuadra(PlanillaCuadra planillaCuadra) {
		this.planillaCuadra = planillaCuadra;
	}

	public Cuenta getCuentaTGI() {
		return cuentaTGI;
	}

	public void setCuentaTGI(Cuenta cuentaTGI) {
		this.cuentaTGI = cuentaTGI;
	}

	public Cuenta getCuentaCdM() {
		return cuentaCdM;
	}

	public void setCuentaCdM(Cuenta cuentaCdM) {
		this.cuentaCdM = cuentaCdM;
	}

	public Double getCantidadMetros() {
		return cantidadMetros;
	}

	public void setCantidadMetros(Double cantidadMetros) {
		this.cantidadMetros = cantidadMetros;
	}

	public Double getCantidadUnidades() {
		return cantidadUnidades;
	}

	public void setCantidadUnidades(Double cantidadUnidades) {
		this.cantidadUnidades = cantidadUnidades;
	}

	public EstPlaCuaDet getEstPlaCuaDet() {
		return estPlaCuaDet;
	}

	public void setEstPlaCuaDet(EstPlaCuaDet estPlaCuaDet) {
		this.estPlaCuaDet = estPlaCuaDet;
	}
	
	public TipPlaCuaDet getTipPlaCuaDet() {
		return tipPlaCuaDet;
	}

	public void setTipPlaCuaDet(TipPlaCuaDet tipPlaCuaDet) {
		this.tipPlaCuaDet = tipPlaCuaDet;
	}

	public Double getValuacionTerreno() {
		return valuacionTerreno;
	}

	public void setValuacionTerreno(Double valuacionTerreno) {
		this.valuacionTerreno = valuacionTerreno;
	}
	
	public Double getRadio() {
		return radio;
	}

	public void setRadio(Double radio) {
		this.radio = radio;
	}

	public String getAgrupador() {
		return agrupador;
	}

	public void setAgrupador(String agrupador) {
		this.agrupador = agrupador;
	}
	
	public PlaCuaDet getPlaCuaDetPadre() {
		return plaCuaDetPadre;
	}

	public void setPlaCuaDetPadre(PlaCuaDet plaCuaDetPadre) {
		this.plaCuaDetPadre = plaCuaDetPadre;
	}

	public Double getPorcPH() {
		return porcPH;
	}

	public void setPorcPH(Double porcPH) {
		this.porcPH = porcPH;
	}

	public Date getFechaIncorporacion() {
		return fechaIncorporacion;
	}

	public void setFechaIncorporacion(Date fechaIncorporacion) {
		this.fechaIncorporacion = fechaIncorporacion;
	}

	public Date getFechaUltMdfDatos() {
		return fechaUltMdfDatos;
	}

	public void setFechaUltMdfDatos(Date fechaUltMdfDatos) {
		this.fechaUltMdfDatos = fechaUltMdfDatos;
	}

	public Date getFechaUltCmbOI() {
		return fechaUltCmbOI;
	}

	public void setFechaUltCmbOI(Date fechaUltCmbOI) {
		this.fechaUltCmbOI = fechaUltCmbOI;
	}

	public Date getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}
	
	public List<PlaCuaDet> getListPlaCuaDetHijos() {
		return listPlaCuaDetHijos;
	}

	public void setListPlaCuaDetHijos(List<PlaCuaDet> listPlaCuaDetHijos) {
		this.listPlaCuaDetHijos = listPlaCuaDetHijos;
	}

	public Integer getCantCuotas() {
		return cantCuotas;
	}

	public void setCantCuotas(Integer cantCuotas) {
		this.cantCuotas = cantCuotas;
	}

	public Date getFechaForm() {
		return fechaForm;
	}

	public void setFechaForm(Date fechaForm) {
		this.fechaForm = fechaForm;
	}

	public Double getImporteTotal() {
		return importeTotal;
	}

	public void setImporteTotal(Double importeTotal) {
		this.importeTotal = importeTotal;
	}

	public ObraFormaPago getObrForPag() {
		return obrForPag;
	}

	public void setObrForPag(ObraFormaPago obrForPag) {
		this.obrForPag = obrForPag;
	}

	public Integer getUsoCatastro() {
		return usoCatastro;
	}

	public void setUsoCatastro(Integer usoCatastro) {
		this.usoCatastro = usoCatastro;
	}

	public UsoCdM getUsoCdM() {
		return usoCdM;
	}

	public void setUsoCdM(UsoCdM usoCdM) {
		this.usoCdM = usoCdM;
	}

	public Double getValMetFre() {
		return valMetFre;
	}

	public void setValMetFre(Double valMetFre) {
		this.valMetFre = valMetFre;
	}

	public Double getValUniTri() {
		return valUniTri;
	}

	public void setValUniTri(Double valUniTri) {
		this.valUniTri = valUniTri;
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
	
		// verificamos que no este en una Anulacion
		if (GenericDAO.hasReference(this,AnulacionObra.class, "plaCuaDet")) {
			addRecoverableError(RecError.PLACUADET_ELIMINAR_EN_ANULACION);
		}
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/** Crea un PlaCuaDetVO con la informacion catastral,
	 *  a partir de este objeto de negocio, debe tener
	 *  cargada la cuenta y el agrupador
	 * 
	 * @return
	 */
	public PlaCuaDetVO toVOWhitcatastrales() {
		PlaCuaDetVO plaCuaDetVO = new PlaCuaDetVO();
		
		ObjImp objImp = this.getCuentaTGI().getObjImp();

		Double pph = this.getPorcPH();
		
		if (pph == null) {
			ObjImpAtrVal porcentajePhAtrVal = ObjImpAtrVal.getVigenteByIdObjImpAtrVal(37L, objImp.getId());
			if (porcentajePhAtrVal != null && 
				!StringUtil.isNullOrEmpty(porcentajePhAtrVal.getStrValor())) {
					pph = new Double(porcentajePhAtrVal.getStrValor());
			}
		}
		
		ObjImpAtrVal ubiFincaAtrVal = ObjImpAtrVal.getVigenteByIdObjImpAtrVal(36L, objImp.getId());

		// Seteo el agrupador
		plaCuaDetVO.setAgrupador(getAgrupador()); 
		// Seteo la Catastral
		plaCuaDetVO.getCuentaTGI().getObjImp().setClaveFuncional(objImp.getClaveFuncional());
		// Seteo el Nro Cuenta
		plaCuaDetVO.getCuentaTGI().setNumeroCuenta(getCuentaTGI().getNumeroCuenta()); 
		// Seteo el ph
		plaCuaDetVO.setPorcPH(pph);

		// Seteo la ubicacion de la finca
		if (ubiFincaAtrVal != null) { 
			plaCuaDetVO.setUbicacionFinca(ubiFincaAtrVal.getStrValor());
		}

		// seteo la unidades tributarias
		plaCuaDetVO.setCantidadUnidades(getCantidadUnidades());
		
		return plaCuaDetVO;
	}
	
	/** Crea un PlaCuaDetVO de nivel n
	 *  y sin listas, agregando 
	 *  informacion del objeto imponible y
	 *  de la cuenta(ubicaion de la finca, 
	 *  ValLibreRef,
	 *  catastral, nro de cuenta de TGI)
	 * 
	 * @return
	 */
	public PlaCuaDetVO toVOConCuentaInfo(int nivel) throws Exception {
	
		PlaCuaDetVO plaCuaDetVO = new PlaCuaDetVO();
		
		// Hacemos el toVO pero sin copiar las listas
		plaCuaDetVO = (PlaCuaDetVO) this.toVO(nivel, false);
		
		// Si no es carpeta componemos los datos de la cuenta
		if (!this.isCarpeta()) {

			ObjImp objImp = this.getCuentaTGI().getObjImp();
				
			// Seteamos la catastral
			plaCuaDetVO.getCuentaTGI().getObjImp().setClaveFuncional(objImp.getClaveFuncional());
			// Seteamos el numero de cuenta
			plaCuaDetVO.getCuentaTGI().setNumeroCuenta(objImp.getClave());
		
		}
		
		return plaCuaDetVO;
	}
	
	/** Crea un PlaCuaDetVO de nivel 1
	 *  y sin listas, agregando 
	 *  informacion del objeto imponible y
	 *  de la cuenta(ubicaion de la finca, 
	 *  catastral, nro de cuenta de TGI)
	 * 
	 * @return
	 */
	
	public PlaCuaDetVO toVOConCuentaInfo() throws Exception {
		return this.toVOConCuentaInfo(0);
	}
	
	/** hace un toVO con la composicion 
	 *  de los hijos en el caso que el detalle
	 *  sea de tipo carpeta
	 * 
	 * @param nivel
	 * @return
	 * @throws Exception
	 */
	public PlaCuaDetVO toVOConHijos(int nivel) throws Exception {
	
		PlaCuaDetVO plaCuaDetVO = new PlaCuaDetVO();		
		
		if (this.isParcela()) {
			plaCuaDetVO = this.toVOConCuentaInfo(nivel);
		}

		if (this.isCarpeta()) {
			plaCuaDetVO = (PlaCuaDetVO) this.toVO(nivel, false);
			
			for(PlaCuaDet plaCuaDetHijo:this.getListPlaCuaDetHijos()) {
				PlaCuaDetVO plaCuaDetHijoVO = plaCuaDetHijo.toVOConCuentaInfo(nivel);
				plaCuaDetVO.getListPlaCuaDet().add(plaCuaDetHijoVO);
			}
		}

		return plaCuaDetVO;
	}
	
	/** hace un toVO con la composicion 
	 *  de los hijos con nivel 1 en el caso que el detalle
	 *  sea de tipo carpeta
	 * 
	 * @param nivel
	 * @return
	 * @throws Exception
	 */
	
	public PlaCuaDetVO toVOConHijos() throws Exception {
		return this.toVOConHijos(1);	
	}

	/** Devuelve true si el detalle es una carpeta 
	 *  (tiene el atributo agrupador)
	 * 
	 * @return
	 */
	public boolean isCarpeta() {
		boolean isAgrupado = false;
		if (!StringUtil.isNullOrEmpty(agrupador)) {
			isAgrupado = true;
		}
		return isAgrupado;
	}

	public boolean isParcela() {
		boolean isParcela = false;

		// si no tiene padre y no es carpeta es parcela
		if (getPlaCuaDetPadre() == null && !this.isCarpeta()) {
			isParcela = true;
		}
		return isParcela;
	}

	public boolean isUnidadFuncional() {
		boolean isUnidadFuncional = false;
		if (getPlaCuaDetPadre() != null) {
			isUnidadFuncional = true;
		}
		return isUnidadFuncional;
	}
	
	/** Calcula el total que debe pagar este detalle
	 *  dependiendo de la obra en cuestion. 
	 *  Si es de tipo carpeta devuelve null
	 * 
	 * @return
	 */
	public Double getTotalAPagar() {
		Double totalAPagar = null;

		// Si ya esta emitida, devolvemos el importe total
		if (this.getEstPlaCuaDet().getId().equals(EstPlaCuaDet.ID_EMITIDA))
			return this.getImporteTotal();
		
		//Recupero el tipo de obra
		TipoObra tipoObra = this.getPlanillaCuadra().getTipoObra();
		
		//Recupero la obra 
		Obra obra = this.getPlanillaCuadra().getObra();
		
		log.debug("obra.getEsPorValuacion() : " + obra.getEsPorValuacion());
		
		//Discrimino entre las dos metodologias disponibles 
		//para el computo del tributo

		// Ordenanza Actual (por valuacion - Ord 8720)
		if (obra.getEsPorValuacion().equals(SiNo.SI.getId())) {
			/*
			// Obra Con Costo Estandar
			if (obra.getEsCostoEsp().equals(SiNo.NO.getId())) {
				totalAPagar = this.getValuacionTerreno() * tipoObra.getCostoModulo();
			}
			// Obra Con Costo Especifico
			if (obra.getEsCostoEsp().equals(SiNo.SI.getId())) {
				PlanillaCuadra planillaCuadra = this.getPlanillaCuadra();
				
				Double sum = 0D;
				// Sum (Vi * Fui)
				for (PlaCuaDet item: planillaCuadra.getListPlaCuaDetNoCarpetas()) 
					sum += item.getValuacionTerreno()*item.getUsoCdM().getFactor();
				
				//  		  (CMTj * Vi * Fui) / Sum (Vi * Fui)  
				totalAPagar = (obra.getCostoEsp() 
									* this.getValuacionTerreno() * this.getUsoCdM().getFactor()) / sum;
			}
			*/
			
			Double sum = 0D;
			// Sum (Vi)
			for (PlaCuaDet item: obra.getListPlaCuaDetNoCarpetas()) 
				sum += item.getValuacionTerreno();
			log.debug("Calculos por valuacion --- sum obra.getListPlaCuaDetNoCarpetas: " + sum);
			
			Double factorPonderacion = 0D;
			if (this.getRadio() == 1D || this.getRadio() == 5D)
				factorPonderacion = 1D;
			if (this.getRadio() == 2D)
				factorPonderacion = 0.8D;
			if (this.getRadio() == 3D)
				factorPonderacion = 0.7D;
			if (this.getRadio() == 4D)
				factorPonderacion = 0.5D;
			
			log.debug("Calculos por valuacion --- factorPonderacion : " + factorPonderacion);
			
			int cantCuadras = obra.getListPlanillaCuadra().size();
			
			log.debug("Calculos por valuacion --- cantCuadras : " + cantCuadras);
			
			totalAPagar = tipoObra.getCostoCuadra() * cantCuadras * factorPonderacion * this.getValuacionTerreno() / sum;
			
			log.debug("Calculos por valuacion --- totalAPagar : " + totalAPagar);
		
		}
		
		// Ordenanza Anterior (por unidades tributarias)
		if (obra.getEsPorValuacion().equals(SiNo.NO.getId())) { 
			Double costoMetroFrente = tipoObra.getCostoMetroFrente();
			Double costoUT = tipoObra.getCostoUT();
		
			// si es parcela calculo directamente el total a pagar
			if(this.isParcela()) {
				totalAPagar = this.getCantidadMetros() * costoMetroFrente + 
					this.getCantidadUnidades() * costoUT;  
			}
		
			// si es unidad funcional saco los valores de
			// metros y UT del padre y caluculo el total 
			// que sera proporcional al procPH
			if(this.isUnidadFuncional()) {
				// recupero la cantidad de metros y la ut del padre
				PlaCuaDet plaCuaDetPadre = this.getPlaCuaDetPadre();
			
				totalAPagar = (plaCuaDetPadre.getCantidadMetros() * costoMetroFrente + 
						plaCuaDetPadre.getCantidadUnidades() * costoUT) * (this.getPorcPH() / 100D);  
			}
			
		}
		
		totalAPagar = NumberUtil.round(totalAPagar, SiatParam.DEC_IMPORTE_DB); 
		
		return totalAPagar;
	}

	/** Devuelve true si y solo si la el detalle de planilla es consistente
	 * 
	 * @return
	 */
	public boolean isConsistente() throws Exception {

		if (getCuentaTGI() == null) {
			return false;
		}
		
		if (getCuentaTGI() != null && getCuentaTGI().getObjImp() == null) {
			return false;
		}
		
		if (!getCuentaTGI().getObjImp().isVigente()) {
			return false;
		}
		
		return true;
	}

	/** Devuelve true si y solo si toda la informacion catastral fue 
	 * cargada
	 */
	public boolean isInfoCatastralCompleta(Obra obra) {
		
		//Si no es por valuacion, debemos tener cargados la cantidad de metros y la UT 
		if (obra.getEsPorValuacion().equals(SiNo.NO.getId())) 
			return (this.getCantidadMetros() != null) && (this.getCantidadUnidades() != null);
		
		// Si es por Valuacion
		if (obra.getEsPorValuacion().equals(SiNo.SI.getId())) {
			//Si es de Costo Especial, debemos tener el cargado el UsoCdM
			if (obra.getEsCostoEsp().equals(SiNo.SI.getId()) && this.getValuacionTerreno() != null) {
				return this.getUsoCdM() != null;
			} 
			//La Valuacion de Terreno NUNCA deberia ser null
			return (this.getValuacionTerreno() != null); 
		}
		
		return  true; 
	}

	/**
	 * Cambia el estado del detalle de una planilla cuadra 
	 *
	 */
	public void cambiarEstado(EstPlaCuaDet estPlaCuaDet) 
		throws Exception {

		this.setEstPlaCuaDet(estPlaCuaDet);
		RecDAOFactory.getPlanillaCuadraDAO().update(this);
	}

	
	public String obtenerDescripcionPlan(){
	
		String descripcionPlan = "";
		
		ObraFormaPago ofp = this.getObrForPag();
		
		// Cantidad de cuotas fijas
		if (ofp.getEsCantCuotasFijas().intValue() == 1 ){
			descripcionPlan = ofp.getDesFormaPago();
		} else {
			
			if (this.getCantCuotas().intValue() == ofp.getCantCuotas().intValue()){
				descripcionPlan = ofp.getDesFormaPago();
			} else {
				descripcionPlan = this.getCantCuotas() + " cuotas de " + ofp.getDesFormaPago();				
			}
		}
		
		return descripcionPlan;
	}
	
	public String getUbicacionFinca() {
		try {
		
		ObjImp objImp = this.getCuentaTGI().getObjImp();

		ObjImpAtrVal ubicacionFincaAtrVal = ObjImpAtrVal
					.getVigenteByIdObjImpAtrVal(ObjImpAtrVal.ID_UBICACION_FINCA, objImp.getId());
		
		return ubicacionFincaAtrVal.getStrValor();
			
		}
		catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Checkea si el plaCuaDet tubo cambio de plan para deuda generada y/o migrada 
	 * 
	 * @return
	 */
	public boolean posseCambioPlan(){
		
		boolean poseeCambio = false;
		
		try {
		
			HisCamPla hisCamPla = HisCamPla.getUltimoHistorioByPlaCuaDet(this);
			
			if (hisCamPla != null) {
				poseeCambio = true;
				log.debug("posseCambioPlan: generada");
			} else {
				
				Cuenta cuenta = this.getCuentaCdM();
				
				if (cuenta != null){
					// Obtenemos la deuda no cancelada
					List<DeudaAdmin> listDeuda = cuenta.getListDeudaAdmin();
	
					for (Deuda deuda:listDeuda){
						
						if (!StringUtil.isNullOrEmpty(deuda.getStrEstadoDeuda())){
							String[] strEstadoDeuda = deuda.getStrEstadoDeuda().split("/");
							String strPer = strEstadoDeuda[0]; 
							
							if (!deuda.getPeriodo().toString().equals(strPer) ){
								poseeCambio = true;
								log.debug("posseCambioPlan: migrada");
								break;
							}
						}
					}
				}
			}
		
		} catch (Exception e){
			e.printStackTrace();
			return poseeCambio;
		}
		
		return poseeCambio;
	}

}

