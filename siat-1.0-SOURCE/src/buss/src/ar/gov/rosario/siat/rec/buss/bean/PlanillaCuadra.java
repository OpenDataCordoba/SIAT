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

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.SiatUtil;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.pad.buss.bean.Calle;
import ar.gov.rosario.siat.pad.buss.bean.Repartidor;
import ar.gov.rosario.siat.pad.buss.bean.UbicacionFacade;
import ar.gov.rosario.siat.pad.iface.model.RepartidorVO;
import ar.gov.rosario.siat.rec.buss.dao.RecDAOFactory;
import ar.gov.rosario.siat.rec.iface.model.PlanillaCuadraVO;
import ar.gov.rosario.siat.rec.iface.util.RecError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;


/**
 * Bean correspondiente a PlanillaCuadra
 * 
 * @author tecso
 */
@Entity
@Table(name = "cdm_planillaCuadra")
public class PlanillaCuadra extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idRecurso") 
	private Recurso recurso;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idContrato") 
	private Contrato contrato;

	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idTipoObra") 
	private TipoObra tipoObra;

	@Column (name="descripcion")
	private String descripcion;
	
	@Column (name="fechaCarga")	
	private Date fechaCarga;
	
	@Column (name="costoCuadra")	
	private Double costoCuadra;

	@Column (name="codCallePpal")	
	private Long codCallePpal;

	@Column (name="codCalleDesde")	
	private Long codCalleDesde;

	@Column (name="codCalleHasta")	
	private Long codCalleHasta;
	
	@Column (name="observacion")	
	private String observacion;
	
	@Column (name="manzana1")	
	private String manzana1;
	
	@Column (name="manzana2")	
	private String manzana2;
	
	@Column (name="numeroCuadra")	
	private Integer numeroCuadra;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idEstPlaCua")
	private EstPlaCua estPlaCua; 
	
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="idPlanillaCuadra")
	private List<PlaCuaDet> listPlaCuaDet;
	
	@OneToMany(mappedBy="planillaCuadra", fetch=FetchType.LAZY)
	@JoinColumn(name="idPlanillaCuadra")
	private List<HisEstPlaCua> listHisEstPlaCua;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idObra")
	private Obra obra;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idRepartidor") 
	private Repartidor repartidor;
	
	@OneToMany(mappedBy="planillaCuadra", fetch=FetchType.LAZY)
	@JoinColumn(name="idPlanillaCuadra")
	private List<HisObrRepPla> listHisObrRepPla;
	
	// Constructores
	public PlanillaCuadra(){
		super();
	}
	
	public PlanillaCuadra(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static PlanillaCuadra getById(Long id) {
		return (PlanillaCuadra) RecDAOFactory.getPlanillaCuadraDAO().getById(id);
	}
	
	public static PlanillaCuadra getByIdNull(Long id) {
		return (PlanillaCuadra) RecDAOFactory.getPlanillaCuadraDAO().getByIdNull(id);
	}
	
	public static List<PlanillaCuadra> getList() {
		return (ArrayList<PlanillaCuadra>) RecDAOFactory.getPlanillaCuadraDAO().getList();
	}
	
	public static List<PlanillaCuadra> getListActivos() {			
		return (ArrayList<PlanillaCuadra>) RecDAOFactory.getPlanillaCuadraDAO().getListActiva();
	}
	
	public static PlanillaCuadra getByIdCuenta(Long idCuenta)throws Exception{
		return RecDAOFactory.getPlanillaCuadraDAO().getByIdCuenta(idCuenta);
	}
	
	/**
	 * Retorna la lista de planillas involucradas en las obras de 
	 * listObra, ordenada por numero de Repartidor, numero 
	 * de Obra y numero de Cuadra
	 * 
	 * @param listObra
	 * @return List<PlanillaCuadra>
	 **/
	public static List<PlanillaCuadra> getListByListObra(List<Obra> listObra) {
		Long [] listIdObra = ListUtilBean.getArrLongIdFromListBaseBO(listObra);
		
		return (ArrayList<PlanillaCuadra>) RecDAOFactory.getPlanillaCuadraDAO().
			getListByListIdObra(listIdObra);
	}
	
	// Getters y setters
	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public Contrato getContrato() {
		return contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	public TipoObra getTipoObra() {
		return tipoObra;
	}

	public void setTipoObra(TipoObra tipoObra) {
		this.tipoObra = tipoObra;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getFechaCarga() {
		return fechaCarga;
	}

	public void setFechaCarga(Date fechaCarga) {
		this.fechaCarga = fechaCarga;
	}

	public Double getCostoCuadra() {
		return costoCuadra;
	}

	public void setCostoCuadra(Double costoCuadra) {
		this.costoCuadra = costoCuadra;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getManzana1() {
		return manzana1;
	}

	public void setManzana1(String manzana1) {
		this.manzana1 = manzana1;
	}

	public String getManzana2() {
		return manzana2;
	}

	public void setManzana2(String manzana2) {
		this.manzana2 = manzana2;
	}

	public EstPlaCua getEstPlaCua() {
		return estPlaCua;
	}

	public void setEstPlaCua(EstPlaCua estPlaCua) {
		this.estPlaCua = estPlaCua;
	}

	public List<PlaCuaDet> getListPlaCuaDet() {
		return listPlaCuaDet;
	}

	public void setListPlaCuaDet(List<PlaCuaDet> listPlaCuaDet) {
		this.listPlaCuaDet = listPlaCuaDet;
	}

	public List<HisEstPlaCua> getListHisEstPlaCua() {
		return listHisEstPlaCua;
	}

	public void setListHisEstPlaCua(List<HisEstPlaCua> listHisEstPlaCua) {
		this.listHisEstPlaCua = listHisEstPlaCua;
	}

	public Long getCodCallePpal() {
		return codCallePpal;
	}

	public void setCodCallePpal(Long codCallePpal) {
		this.codCallePpal = codCallePpal;
	}

	public Long getCodCalleDesde() {
		return codCalleDesde;
	}

	public void setCodCalleDesde(Long codCalleDesde) {
		this.codCalleDesde = codCalleDesde;
	}

	public Long getCodCalleHasta() {
		return codCalleHasta;
	}

	public void setCodCalleHasta(Long codCalleHasta) {
		this.codCalleHasta = codCalleHasta;
	}
	
	public Obra getObra() {
		return obra;
	}

	public void setObra(Obra obra) {
		this.obra = obra;
	}
	
	public Repartidor getRepartidor() {
		return repartidor;
	}

	public void setRepartidor(Repartidor repartidor) {
		this.repartidor = repartidor;
	}

	public List<HisObrRepPla> getListHisObrRepPla() {
		return listHisObrRepPla;
	}

	public void setListHisObrRepPla(List<HisObrRepPla> listHisObrRepPla) {
		this.listHisObrRepPla = listHisObrRepPla;
	}

	public Calle getCallePpal() throws Exception {
		Calle callePpal = null;
		
		if(this.getCodCallePpal() != null) {
			callePpal = Calle.getByCodCalle(this.getCodCallePpal());
		}

		return callePpal;
	}

	public Calle getCalleDesde() throws Exception {
		Calle calleDesde = null;
		
		if(this.getCodCalleDesde() != null) {
			calleDesde = Calle.getByCodCalle(this.getCodCalleDesde());
		}
		
		return calleDesde;
	}

	public Calle getCalleHasta() throws Exception {
		Calle calleHasta = null;
		
		if(this.getCodCalleHasta() != null) {
			calleHasta = Calle.getByCodCalle(this.getCodCalleHasta());
		}
		
		return calleHasta;
	}
	
	public Integer getNumeroCuadra() {
		return numeroCuadra;
	}

	public void setNumeroCuadra(Integer numeroCuadra) {
		this.numeroCuadra = numeroCuadra;
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
	
		// Verificamos que no tenga catastrales asignadas
		if(!ListUtil.isNullOrEmpty(this.getListPlaCuaDet())){
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, RecError.PLANILLACUADRA_LABEL, 
						RecError.PLACUADET_LABEL);
		}
		
		// verificamos que no este en una Anulacion
		if (GenericDAO.hasReference(this,AnulacionObra.class, "planillaCuadra")) {
			addRecoverableError(RecError.PLANILLACUADRA_ELIMINAR_EN_ANULACION);
		}
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones
		if (getRecurso() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL);
		}

		if (getContrato() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.CONTRATO_LABEL);
		}

		if (getTipoObra() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.TIPOOBRA_LABEL);
		}
		
		if (getFechaCarga() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.PLANILLACUADRA_FECHACARGA);
		}
		
		if (StringUtil.isNullOrEmpty(getDescripcion())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.PLANILLACUADRA_DESCRIPCION);
		}
		
		if (getCostoCuadra() != null && getCostoCuadra() < 0D)  {
			addRecoverableError(BaseError.MSG_VALORMENORQUECERO, RecError.PLANILLACUADRA_COSTOCUADRA);
		}
		
		// valido que halla ingresado las 3 calles o ninguna
		if ( (getCallePpal() == null || getCalleDesde() == null || getCalleHasta() == null) &&
			 !(getCallePpal() == null && getCalleDesde() == null && getCalleHasta() == null) ) {
			addRecoverableError(RecError.PLANILLACUADRA_CALLESREQUERIDAS);			
		}

		if (hasError()) {
			return false;
		}

		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Cambia el estado de la planilla cuadra 
	 *
	 */
	public void cambiarEstado(EstPlaCua estadoNuevo, String observacion) 
		throws Exception {

		if(!this.validateCambioEstado(estadoNuevo)){
			return;
		}
		
		//Si el nuevo estado es Emitida, seteamos el estado de todos los detalles a Emitidos
		if (estadoNuevo.getId().equals(EstPlaCua.ID_EMITIDA)) {
			for (PlaCuaDet item: this.getListPlaCuaDet()) {
				item.cambiarEstado(EstPlaCuaDet.getById(EstPlaCuaDet.ID_EMITIDA));
			}
		}

		//Si el nuevo estado es Anulada, seteamos el estado de todos los detalles a Anulada
		if (estadoNuevo.getId().equals(EstPlaCua.ID_ANULADA)) {
			for (PlaCuaDet item: this.getListPlaCuaDet()) {
				item.cambiarEstado(EstPlaCuaDet.getById(EstPlaCuaDet.ID_ANULADA));
			}
		}

		// Obtenemos el area actual 
		String areaActual = this.getEstPlaCua().getArea().getDesArea();

		// Obtenemos el area destino
		String areaDestino = estadoNuevo.getArea().getDesArea();

		// Genereamos la descripcion del evento
		String desEvento = "";
		if (estadoNuevo.getId().equals(EstPlaCua.ID_ANULADA)) {
			desEvento = SiatUtil.getValueFromBundle("rec.planillaCuadra.planillaAnulada"); 
		}else if (estadoNuevo.getId().equals(EstPlaCua.ID_NO_EMITIR)) {
			desEvento = SiatUtil.getValueFromBundle("rec.planillaCuadra.planillaNoEmitir");
		}else { 
			desEvento = "Se envi\u00F3 desde " + areaActual + " a " + areaDestino;
		}

		//Seteamos el nuevo estado a nivel de planilla
		this.setEstPlaCua(estadoNuevo);
		
		// Actualizamos en la BD
		RecDAOFactory.getPlanillaCuadraDAO().update(this);

		// Generamos la descripcion completa
		String descripcion = desEvento + ((!StringUtil.isNullOrEmpty(observacion)) ? (": " + observacion) : "");

		// Guardamos el evento en el Historico
		HisEstPlaCua hisEstPlaCua = this.createHisEstPlaCua(estadoNuevo, descripcion);
		
		//Copiamos la lista de errores
		this.getListError().addAll(hisEstPlaCua.getListError());

	}
	
	/**
	 * Valida el cambio de estado
	 * @return boolean
	 */
	private boolean validateCambioEstado(EstPlaCua estadoNuevo) throws Exception {
		//limpiamos la lista de errores
		clearError();

		// Debe haber seleccionado un estado 
		if (estadoNuevo == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.ESTPLACUA_LABEL);
		}
		
		// Si enviamos a emision, validamos que se halla informado al menos una catastral
		if (estadoNuevo != null && estadoNuevo.getId().equals(EstPlaCua.ID_ENVIADA_A_EMISION) && 
				ListUtil.isNullOrEmpty(getListPlaCuaDetNoCarpetas())) {
			addRecoverableError(RecError.PLANILLACUADRA_SIN_CATASTRALES_INFORMADAS);
		}

		// Si la anulamos, verificamos que no esta emitida
		if (estadoNuevo != null && estadoNuevo.getId().equals(EstPlaCua.ID_ANULADA)) { 
		}
		
		if (hasError()) {
			return false;
		}

		//Validaciones 
		return true;
	}
	
	
	// Metodos de negocio
	
	// ---> ABM HisEstPlaCua
	public HisEstPlaCua createHisEstPlaCua(EstPlaCua estPlaCua, String descripcion) throws Exception {

		HisEstPlaCua hisEstPlaCua = new HisEstPlaCua(estPlaCua, this, descripcion);
		hisEstPlaCua = this.createHisEstPlaCua(hisEstPlaCua);

		return hisEstPlaCua;
	}
	// <--- ABM HisEstPlaCua
	
	//	---> ABM PlaCuaDet
	public PlaCuaDet createPlaCuaDet(PlaCuaDet plaCuaDet) throws Exception {
		
		// Seteamos la planilla
		plaCuaDet.setPlanillaCuadra(this);

		// Seteamos el tipo de planilla
		TipPlaCuaDet tipPlaCuaDet = null;
		if (plaCuaDet.isParcela()) {
			tipPlaCuaDet = TipPlaCuaDet.getById(TipPlaCuaDet.ID_PARCELA); 
		}
		
		if (plaCuaDet.isCarpeta()) {
			tipPlaCuaDet = TipPlaCuaDet.getById(TipPlaCuaDet.ID_CARPETA);			
		}
		
		if (plaCuaDet.isUnidadFuncional()) {
			tipPlaCuaDet = TipPlaCuaDet.getById(TipPlaCuaDet.ID_UNIDADFUNCIONAL);			
		}
		
		plaCuaDet.setTipPlaCuaDet(tipPlaCuaDet);
		
		// Seteamos la fecha de incoporacion
		plaCuaDet.setFechaIncorporacion(new Date());

		// Seteamos la fecha de ultima modificacion de datos
		plaCuaDet.setFechaUltMdfDatos(new Date());
		
		// Seteamos el estado
		EstPlaCuaDet estPlaCuaDet = EstPlaCuaDet.getById(EstPlaCuaDet.ID_INFORMADA);
		plaCuaDet.setEstPlaCuaDet(estPlaCuaDet);
		

		// Validaciones de negocio
		if (!plaCuaDet.validateCreate()) {
			return plaCuaDet;
		}

		RecDAOFactory.getPlaCuaDetDAO().update(plaCuaDet);

		return plaCuaDet;
	}
	
	public PlaCuaDet updatePlaCuaDet(PlaCuaDet plaCuaDet) throws Exception {
		
		// Validaciones de negocio
		if (!plaCuaDet.validateUpdate()) {
			return plaCuaDet;
		}
		
		Double unidadesTributarias = plaCuaDet.getCantidadUnidades();
		Double metroFrente = plaCuaDet.getCantidadMetros();

		// actualizo lo hijos en caso de tener
		for(PlaCuaDet plaCuaDetHija:plaCuaDet.getListPlaCuaDetHijos()) {

			Double PPHHijo = plaCuaDetHija.getPorcPH();

			if (PPHHijo != null) {
				Double unidadesTributariasHijo = null;
				Double metroFrenteHijo = null;
				
				if (unidadesTributarias != null)
					 unidadesTributariasHijo = unidadesTributarias * (PPHHijo/100);
				
				if (metroFrente != null)
					metroFrenteHijo = metroFrente * (PPHHijo/100);

				plaCuaDetHija.setCantidadUnidades(unidadesTributariasHijo);
				plaCuaDetHija.setCantidadMetros(metroFrenteHijo);
				
				RecDAOFactory.getPlaCuaDetDAO().update(plaCuaDetHija);
			}
		}

		RecDAOFactory.getPlaCuaDetDAO().update(plaCuaDet);
		
		return plaCuaDet;
	}
	
	public PlaCuaDet deletePlaCuaDet(PlaCuaDet plaCuaDet) throws Exception {
	
		// Validaciones de negocio
		if (!plaCuaDet.validateDelete()) {
			return plaCuaDet;
		}
		
		// Se eliminan los Hijos en caso de tener
		for(PlaCuaDet plaCuaDetHija:plaCuaDet.getListPlaCuaDetHijos()) {
			RecDAOFactory.getPlaCuaDetDAO().delete(plaCuaDetHija);
		}
		
		RecDAOFactory.getPlaCuaDetDAO().delete(plaCuaDet);
		
		return plaCuaDet;
	}
	// <--- ABM PlaCuaDet
	
	//	---> ABM HisEstPlaCua
	public HisEstPlaCua createHisEstPlaCua(HisEstPlaCua hisEstPlaCua) throws Exception {

		// Validaciones de negocio
		if (!hisEstPlaCua.validateCreate()) {
			return hisEstPlaCua;
		}

		RecDAOFactory.getHisEstPlaCuaDAO().update(hisEstPlaCua);

		return hisEstPlaCua;
	}
	
	public HisEstPlaCua updateHisEstPlaCua(HisEstPlaCua hisEstPlaCua) throws Exception {
		
		// Validaciones de negocio
		if (!hisEstPlaCua.validateUpdate()) {
			return hisEstPlaCua;
		}

		RecDAOFactory.getHisEstPlaCuaDAO().update(hisEstPlaCua);
		
		return hisEstPlaCua;
	}
	
	public HisEstPlaCua deleteHisEstPlaCua(HisEstPlaCua hisEstPlaCua) throws Exception {
	
		// Validaciones de negocio
		if (!hisEstPlaCua.validateDelete()) {
			return hisEstPlaCua;
		}
		
		RecDAOFactory.getHisEstPlaCuaDAO().delete(hisEstPlaCua);
		
		return hisEstPlaCua;
	}
	// <--- ABM HisEstPlaCua
	
	// --->ABM HisObrRepPla
	public HisObrRepPla deleteHisObrRepPla(HisObrRepPla hisObrRepPla) throws Exception {
		
		// Validaciones de negocio
		if (!hisObrRepPla.validateDelete()) {
			return hisObrRepPla;
		}
		
		RecDAOFactory.getHisObrRepPlaDAO().delete(hisObrRepPla);
		
		return hisObrRepPla;
	}
	// <--- ABM HisObrRepPla
	
	/** Devuleve true si la planilla cuadra
	 *  tiene las tres calles ingresadas
	 * 
	 */
	public boolean hasCalles(){
		boolean hasCalles = false;
		if (this.getCodCallePpal() != null &&
			this.getCodCallePpal() != null &&
			this.getCodCalleHasta() != null) {
			hasCalles = true;
		}
		return hasCalles;
	}
	
	/** setea las manzanas de esta planilla
	 *  en caso de que halla ingresadas calles
	 * 
	 */
	public void setearManzanas() throws Exception {
		
		// si hay calles ingresadas
		if (this.hasCalles()) {
			
            Calle callePpal = Calle.getByIdNull(getCodCallePpal());
            Calle calleDesde = Calle.getByIdNull(getCodCalleDesde());
            Calle calleHasta = Calle.getByCodCalle(getCodCalleHasta());

            // obtengo la primer interseccion
            List<Double> listXY1 = UbicacionFacade.getInstance().getInterseccion
            	(callePpal.getNombreCalle() , calleDesde.getNombreCalle());
            
            // obtengo la Segunda interseccion
            List<Double> listXY2 = UbicacionFacade.getInstance().getInterseccion
            	(callePpal.getNombreCalle() , calleHasta.getNombreCalle());
            
            // si las dos listas tienen dos valores (x, y)
            if (!ListUtil.isNullOrEmpty(listXY1) &&
            	!ListUtil.isNullOrEmpty(listXY2) &&
            	listXY1.size() == 2 && listXY2.size() == 2) {
            	
            	List<String> listManzanas1 = UbicacionFacade.getInstance().getManzanasCircundantes
            		(listXY1.get(0), listXY1.get(1));

            	List<String> listManzanas2 = UbicacionFacade.getInstance().getManzanasCircundantes
        		(listXY2.get(0), listXY2.get(1));
            	
            	// si recupero 4 y 4 manzanas obtengo la interseccion
                if (!ListUtil.isNullOrEmpty(listManzanas1) &&
                	!ListUtil.isNullOrEmpty(listManzanas2) &&
                	listManzanas1.size() == 4 && listManzanas2.size() == 4) {
                	
                	List<String> interseccion = ListUtil.interseccionString(listManzanas1, listManzanas2);
                	
                	// si la interseccion recupero 2 valores grabo las manzanas
                	if (interseccion.size() == 2) {
                		this.setManzana1(interseccion.get(0));
                		this.setManzana2(interseccion.get(1));

                	}
                	
                }

            }
			
		}
		
		// si no hay calles ingresadas seteo las manzanas nulas
		if (!this.hasCalles()) {
    		this.setManzana1(null);
    		this.setManzana2(null);
		}
		
	}
	
	/** Setea un repartidor a esta planilla cuadra
	 * 
	 * @param repartidor
	 * @return
	 */
	public PlanillaCuadra asignarRepartidor(Repartidor repartidor) {
		
		// TODO: agregar validacion
		this.setRepartidor(repartidor);
		RecDAOFactory.getPlanillaCuadraDAO().update(this);
		
		// creo el historico
		HisObrRepPla hisObrRepPla = new HisObrRepPla(this, repartidor);
		RecDAOFactory.getHisObrRepPlaDAO().update(hisObrRepPla);
		
		return this;
	}
	
	/** Borra el repartidor que tenga asignado esta
	 *  planilla cuadra
	 * 
	 * @param repartidor
	 * @return
	 */
	public PlanillaCuadra desAsignarRepartidor() {
		
		// TODO: agregar validacion
		this.setRepartidor(null);
		RecDAOFactory.getPlanillaCuadraDAO().update(this);

		// creo el historico, indicando que se seteo a nulo el repartidor
		HisObrRepPla hisObrRepPla = new HisObrRepPla(this, null);
		RecDAOFactory.getHisObrRepPlaDAO().update(hisObrRepPla);

		return this;
	}
	
	public PlanillaCuadraVO toVOConRepartidor(int nivel) throws Exception {
		PlanillaCuadraVO planillaCuadraVO = (PlanillaCuadraVO) this.toVO(nivel,false);
		
		if (this.getRepartidor() != null) {
			RepartidorVO repartidorVO = (RepartidorVO) this.getRepartidor().toVOConPersona(0);
			planillaCuadraVO.setRepartidor(repartidorVO);
		}
		
		return planillaCuadraVO;
	}
	
	/** Convierte un lista de planilla cuadra de negocio a
	 *  un lista de planilla cuadra VO con el repartidor
	 *  con la persona correspondiente.
	 * 
	 * @param listRepartidor
	 * @param nivel
	 * @return
	 * @throws Exception
	 */
	public static List<PlanillaCuadraVO> getListVOConRepartidor (List<PlanillaCuadra> 
		listPlanillaCuadra, int nivel) throws Exception {
		
		List<PlanillaCuadraVO> listPlanillaCuadraVO = new ArrayList<PlanillaCuadraVO>();
		
		for (PlanillaCuadra planillaCuadra : listPlanillaCuadra) {
			PlanillaCuadraVO planillaCuadraVO = planillaCuadra.toVOConRepartidor(nivel);
			listPlanillaCuadraVO.add(planillaCuadraVO);
		}
		
		return listPlanillaCuadraVO;
	}

	public List<PlaCuaDet> getListPlaCuaDetNoCarpetas() {
		List<PlaCuaDet> listPlaCuaDet = new ArrayList<PlaCuaDet>();
		for (PlaCuaDet plaCuaDet:this.getListPlaCuaDet()) {
			// si no es carpeta recupero la cuenta tgi
			if(!plaCuaDet.isCarpeta()) {
				listPlaCuaDet.add(plaCuaDet);
			}
		}
		return listPlaCuaDet;
	}

	/** Devuelve true si y solo si la planilla es consistente
	 * 
	 * @return
	 */
	public boolean isConsistente() throws Exception {
		
		//Se verifican que todos los detalles sean consistentes.
		for (PlaCuaDet item:getListPlaCuaDet()) { 
			if (!item.isConsistente()) {}
				return false;
		} 
		
		return true;
	}
	
	/** Devuelve true si y solo si la planilla tiene completa la informacion 
	 * catastral para un determinada obra
	 * 
	 * @return
	 */
	public boolean isInfoCatastralCompleta(Obra obra)  {
		
		//Se verifican que todos los detalles tengan la informacion catastral 
		//suficiente para realizar el calculo del tributo.
		for (PlaCuaDet item:getListPlaCuaDetNoCarpetas()) { 
			if (!item.isInfoCatastralCompleta(obra))
				return false;
		} 
		
		return true;
	} 
	
	/** 
	 * Calcula la suma de los montos a pagar de cada uno de los
	 * contribuyentes de la planilla.
	 * */

	public Double getMontoTotal() {
		
		Double total = 0D;
		
		try {
			for (PlaCuaDet plaCuaDet: this.getListPlaCuaDetNoCarpetas())
				total += plaCuaDet.getTotalAPagar();
		}
		catch (Exception e) {
			return 0D;
		}
		
		return total;
	}

	public String getMontoTotalForReport() {
		return StringUtil.formatDouble(this.getMontoTotal());
	}
	
	/** 
	 * Calcula la cantidad de cuentas de la planilla
	 * */

	public Integer getTotalCuentas() {
		return this.getListPlaCuaDetNoCarpetas().size();
	}

	public String getTotalCuentasForReport() {
		return StringUtil.formatInteger(this.getTotalCuentas());
	}

}
