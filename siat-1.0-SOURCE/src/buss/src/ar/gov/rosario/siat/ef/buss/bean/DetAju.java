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
import javax.persistence.Transient;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import ar.gov.rosario.siat.ef.iface.model.DetAjuVO;
import ar.gov.rosario.siat.ef.iface.model.OrdConCueVO;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlVO;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpAtrDefinition;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpDefinition;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a DetAju (determinacion de Ajuste)
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_detAju")
public class DetAju extends BaseBO {

	@Transient
	private Logger log = Logger.getLogger(DetAju.class);
	
	private static final long serialVersionUID = 1L;

	@Column(name = "fecha")
	private Date fecha;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idOrdenControl")
	private OrdenControl ordenControl;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idOrdConCue")
	private OrdConCue ordConCue;

	@OneToMany(mappedBy = "detAju")
	@JoinColumn(name = "idDetAju")
	private List<DetAjuDet> listDetAjuDet;

	@Transient
	private Double totalAjustePos;
	
	@Transient
	private Double totalAjusteNeg;
	
	@Transient
	private Double totalAjusteAct;
	
	@Transient
	private Date fechaActualizacion;
	
	// <#Propiedades#>

	// Constructores
	public DetAju() {
		super();
		// Seteo de valores default
	}

	public DetAju(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static DetAju getById(Long id) {
		return (DetAju) EfDAOFactory.getDetAjuDAO().getById(id);
	}

	public static DetAju getByIdNull(Long id) {
		return (DetAju) EfDAOFactory.getDetAjuDAO().getByIdNull(id);
	}

	public static List<DetAju> getList() {
		return (ArrayList<DetAju>) EfDAOFactory.getDetAjuDAO().getList();
	}

	public static List<DetAju> getListActivos() {
		return (ArrayList<DetAju>) EfDAOFactory.getDetAjuDAO().getListActiva();
	}

	// Getters y setters
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public OrdenControl getOrdenControl() {
		return ordenControl;
	}

	public void setOrdenControl(OrdenControl ordenControl) {
		this.ordenControl = ordenControl;
	}

	public OrdConCue getOrdConCue() {
		return ordConCue;
	}

	public void setOrdConCue(OrdConCue ordConCue) {
		this.ordConCue = ordConCue;
	}

	public List<DetAjuDet> getListDetAjuDet() {
		return listDetAjuDet;
	}

	public void setListDetAjuDet(List<DetAjuDet> listDetAjuDet) {
		this.listDetAjuDet = listDetAjuDet;
	}

	public Double getTotalAjustePos() {
		return totalAjustePos;
	}

	public void setTotalAjustePos(Double totalAjustePos) {
		this.totalAjustePos = totalAjustePos;
	}

	public Double getTotalAjusteNeg() {
		return totalAjusteNeg;
	}

	public void setTotalAjusteNeg(Double totalAjusteNeg) {
		this.totalAjusteNeg = totalAjusteNeg;
	}

	public Double getTotalAjusteAct() {
		return totalAjusteAct;
	}

	public void setTotalAjusteAct(Double totalAjusteAct) {
		this.totalAjusteAct = totalAjusteAct;
	}
	
	public Date getFechaActualizacion() {
		return fechaActualizacion;
	}

	public void setFechaActualizacion(Date fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
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

		if (GenericDAO.hasReference(this, ComAju.class, "detAju")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
					EfError.DETAJU_LABEL, EfError.COMAJU_LABEL);
		}
		if (GenericDAO.hasReference(this, DetAjuDocSop.class, "detAju")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
					EfError.DETAJU_LABEL, EfError.DETAJUDOCSOP_LABEL);
		}
		
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
	public List<CompFuente> getlistCompFuenteDistinct() {
		List<CompFuente> listCompFuente = new ArrayList<CompFuente>();
		for(DetAjuDet detAjuDet: listDetAjuDet){
			log.debug("idPlaFueDat:"+detAjuDet.getPlaFueDatCom().getCompFuente().getPlaFueDat()
					+"    idCompFuente:"+detAjuDet.getPlaFueDatCom().getCompFuente().getId()+
					" fuente:"+detAjuDet.getPlaFueDatCom().getCompFuente().getPlaFueDat().getFuenteInfo().getNombreFuente());
			boolean agregar = true;
			for(CompFuente compFuente:listCompFuente){
				if(compFuente.equals(detAjuDet.getPlaFueDatCom().getCompFuente())){
					agregar=false;
					break;
				}
			}
			
			if(agregar){
				listCompFuente.add(detAjuDet.getPlaFueDatCom().getCompFuente());
			}
		}
		
		return listCompFuente;
	}
	
	/**
	 * Realiza la sumatoria para calcular el total de ajustes positivos y negativos y los guarda en los 2 campos Transient
	 */
	public void calcularTotalesAjustes() throws Exception{
		totalAjustePos=0D;
		totalAjusteNeg=0D;
		
		if(listDetAjuDet!=null){
			for(DetAjuDet detAjuDet: listDetAjuDet){
				
				if(detAjuDet.getAjuste()!=null){
					if(detAjuDet.getAjuste().doubleValue()>0){
						totalAjustePos+= detAjuDet.getAjuste();
					}
					else
						totalAjusteNeg+= detAjuDet.getAjuste();
				}
				
			}
		}
	}
	
	/**
	 * Activa el DetAju. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getDetAjuDAO().update(this);
	}

	/**
	 * Desactiva el DetAju. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getDetAjuDAO().update(this);
	}

	/**
	 * Valida la activacion del DetAju
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
	 * Valida la desactivacion del DetAju
	 * 
	 * @return boolean
	 */
	private boolean validateDesactivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}

	// ---> ABM DetAjuDet
	public DetAjuDet createDetAjuDet(DetAjuDet detAjuDet) throws Exception {
		// Validaciones de negocio
		if (!detAjuDet.validateCreate()) {
			return detAjuDet;
		}
		EfDAOFactory.getDetAjuDetDAO().update(detAjuDet);
		return detAjuDet;
	}

	public DetAjuDet updateDetAjuDet(DetAjuDet detAjuDet) throws Exception {

		// Validaciones de negocio
		if (!detAjuDet.validateUpdate()) {
			return detAjuDet;
		}
		EfDAOFactory.getDetAjuDetDAO().update(detAjuDet);

		return detAjuDet;
	}

	public DetAjuDet deleteDetAjuDet(DetAjuDet detAjuDet) throws Exception {

		// Validaciones de negocio
		if (!detAjuDet.validateDelete()) {
			return detAjuDet;
		}
	
		EfDAOFactory.getDetAjuDetDAO().delete(detAjuDet);

		return detAjuDet;
	}

	// <#MetodosBeanDetalle#>

	public DetAjuVO toVO4View() throws Exception {
		DetAjuVO detAjuVO = new DetAjuVO();
		detAjuVO.setId(getId());
		detAjuVO.setFecha(fecha);
		detAjuVO.setOrdenControl((OrdenControlVO) ordenControl.toVO(1, false));
		detAjuVO.setOrdConCue((OrdConCueVO) ordConCue.toVO(1, false));
		detAjuVO.getOrdConCue().setCuenta(ordConCue.getCuenta().toVOWithRecurso());
		
		// lista de detAjuDet
		Double totalAct=0D;
		for(DetAjuDet detAjuDet:listDetAjuDet){
			log.debug("fechaActualizacion: "+DateUtil.formatDate(this.fechaActualizacion, DateUtil.ddSMMSYYYY_MASK));
			detAjuVO.getListDetAjuDet().add(detAjuDet.toVO4Planilla());
			if(detAjuDet.getAjusteActualizado()!=null)
				totalAct+=detAjuDet.getAjusteActualizado();
		}
		
		calcularTotalesAjustes();
		detAjuVO.setTotalAjusteNeg(totalAjusteNeg);
		detAjuVO.setTotalAjustePos(totalAjustePos);
		detAjuVO.setTotalAjusteAct(totalAct);
		
		return detAjuVO;
	}

	// metodos para el reporte
	public String getRubrosObjImp4Report() throws Exception{
		String rubros = "";
		TipObjImpDefinition def = getOrdConCue().getCuenta().getObjImp().getDefinitionValue();
		TipObjImpAtrDefinition atrDef = def.getTipObjImpAtrDefinitionByCodigo(Atributo.COD_RUBRO);
		for(String str:atrDef.getListMultiValor()){
			rubros+="     "+str+" - "+atrDef.getValorByCodigoFromDominio(str);
		}
		
		return rubros;
	}
	
	public String getCatastral4Report() throws Exception{
		TipObjImpDefinition def = getOrdConCue().getCuenta().getObjImp().getDefinitionValue();
		TipObjImpAtrDefinition atrDef = def.getTipObjImpAtrDefinitionByCodigo(Atributo.COD_CATASTRAL);
		return atrDef.getValorString();
	}
	
	public String getNroPermisoHab4Report() throws Exception{
		TipObjImpDefinition def = getOrdConCue().getCuenta().getObjImp().getDefinitionValue();
		TipObjImpAtrDefinition atrDef = def.getTipObjImpAtrDefinitionByCodigo(Atributo.COD_NROPERMISO);
		return atrDef.getValorString();
	}
	
	public String getFecIniAct4Report() throws Exception{
		TipObjImpDefinition def = getOrdConCue().getCuenta().getObjImp().getDefinitionValue();
		TipObjImpAtrDefinition atrDef = def.getTipObjImpAtrDefinitionByCodigo(Atributo.COD_FECHA_INICIO);
		Date fecha = DateUtil.getDate(atrDef.getValorString(), DateUtil.YYYYMMDD_MASK);
		return DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK);
	}
	
}
