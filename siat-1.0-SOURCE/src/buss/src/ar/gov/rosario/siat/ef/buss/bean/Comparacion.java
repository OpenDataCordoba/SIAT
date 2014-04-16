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
import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import ar.gov.rosario.siat.ef.iface.model.CompFuenteResMensualVO;
import ar.gov.rosario.siat.ef.iface.model.CompFuenteResVO;
import ar.gov.rosario.siat.ef.iface.model.ComparacionVO;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlVO;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.TipoPeriodicidad;

/**
 * Bean correspondiente a Comparacion
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_comparacion")
public class Comparacion extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@Transient
	private Logger log = Logger.getLogger(Comparacion.class); 
	
	@Column(name = "descripcion")
	private String descripcion;

	@Column(name = "fecha")
	private Date fecha;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idOrdenControl")
	private OrdenControl ordenControl;

	@OneToMany(mappedBy = "comparacion")
	@JoinColumn(name = "idComparacion")
	private List<CompFuente> listCompFuente = new ArrayList<CompFuente>();

	@OneToMany(mappedBy = "comparacion")
	@JoinColumn(name = "idComparacion")
	private List<CompFuenteRes> listCompFuenteRes = new ArrayList<CompFuenteRes>();

	// <#Propiedades#>

	// Constructores
	public Comparacion() {
		super();
		// Seteo de valores default
	}

	public Comparacion(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static Comparacion getById(Long id) {
		return (Comparacion) EfDAOFactory.getComparacionDAO().getById(id);
	}

	public static Comparacion getByIdNull(Long id) {
		return (Comparacion) EfDAOFactory.getComparacionDAO().getByIdNull(id);
	}

	public static List<Comparacion> getList() {
		return (ArrayList<Comparacion>) EfDAOFactory.getComparacionDAO()
				.getList();
	}

	public static List<Comparacion> getListActivos() {
		return (ArrayList<Comparacion>) EfDAOFactory.getComparacionDAO()
				.getListActiva();
	}

	// Getters y setters
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String desComparacion) {
		this.descripcion = desComparacion;
	}

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

	public List<CompFuente> getListCompFuente() {
		return listCompFuente;
	}

	public void setListCompFuente(List<CompFuente> listCompFuente) {
		this.listCompFuente = listCompFuente;
	}

	public List<CompFuenteRes> getListCompFuenteRes() {
		return listCompFuenteRes;
	}

	public void setListCompFuenteRes(List<CompFuenteRes> listCompFuenteRes) {
		this.listCompFuenteRes = listCompFuenteRes;
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

		if (GenericDAO.hasReference(this, CompFuente.class, "comparacion")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
					EfError.COMPARACION_LABEL, EfError.COMPFUENTE_LABEL);
		}

		if (GenericDAO.hasReference(this, CompFuenteRes.class, "comparacion")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
					EfError.COMPARACION_LABEL, EfError.COMPFUENTERES_LABEL);
		}
		// <#ValidateDelete#>

		if (hasError()) {
			return false;
		}

		return true;
	}

	private boolean validate() throws Exception {

		// Validaciones
		if (fecha == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					EfError.COMPARACION_FECHA_LABEL);
		}

		if (StringUtil.isNullOrEmpty(getDescripcion())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					EfError.COMPARACION_DESCOMPARACION);
		}

		if (hasError()) {
			return false;
		}

		return true;
	}

	// Metodos de negocio

	/**
	 * Activa el Comparacion. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getComparacionDAO().update(this);
	}

	/**
	 * Desactiva el Comparacion. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getComparacionDAO().update(this);
	}

	/**
	 * Valida la activacion del Comparacion
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
	 * Valida la desactivacion del Comparacion
	 * 
	 * @return boolean
	 */
	private boolean validateDesactivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}

	// ---> ABM CompFuenteRes
	public CompFuenteRes createCompFuenteRes(CompFuenteRes compFuenteRes)
			throws Exception {

		// Validaciones de negocio
		if (!compFuenteRes.validateCreate()) {
			return compFuenteRes;
		}

		EfDAOFactory.getCompFuenteResDAO().update(compFuenteRes);

		return compFuenteRes;
	}

	public CompFuenteRes updateCompFuenteRes(CompFuenteRes compFuenteRes)
			throws Exception {

		// Validaciones de negocio
		if (!compFuenteRes.validateUpdate()) {
			return compFuenteRes;
		}

		EfDAOFactory.getCompFuenteResDAO().update(compFuenteRes);

		return compFuenteRes;
	}

	public CompFuenteRes deleteCompFuenteRes(CompFuenteRes compFuenteRes)
			throws Exception {

		// Validaciones de negocio
		if (!compFuenteRes.validateDelete()) {
			return compFuenteRes;
		}

		EfDAOFactory.getCompFuenteResDAO().delete(compFuenteRes);

		return compFuenteRes;
	}

	public void deleteListCompFuenteRes() {
		EfDAOFactory.getCompFuenteResDAO().deleteListByComparacion(this);
	}

	public void deleteListCompFuente() {
		EfDAOFactory.getCompFuenteDAO().deleteListByComparacion(this);
	}

	// <--- ABM CompFuenteRes
	// <#MetodosBeanDetalle#>

	public ComparacionVO toVO4Update() throws Exception {
		ComparacionVO comparacionVO = (ComparacionVO) this.toVO(0, false);
		comparacionVO.setOrdenControl((OrdenControlVO) ordenControl.toVO(0,
				false));
		comparacionVO.setListCompFuente(ListUtilBean.toVO(listCompFuente, 2,
				false));
		// comparacionVO.setListCompFuenteRes(ListUtilBean.toVO(listCompFuenteRes,
		// 0, false));
		for (CompFuenteRes compFuenteRes : listCompFuenteRes) {
			CompFuenteResVO compFuenteResVO = (CompFuenteResVO) compFuenteRes
					.toVO(0, false);
			if (compFuenteRes.getComFueMin().getPlaFueDat().getFuenteInfo()
					.getTipoPeriodicidad().intValue() == TipoPeriodicidad.MENSUAL
					.getId().intValue()
					&& compFuenteRes.getComFueSus().getPlaFueDat()
							.getFuenteInfo().getTipoPeriodicidad().intValue() == TipoPeriodicidad.MENSUAL
							.getId().intValue()) {
				compFuenteResVO.setAmbasFuentesMensuales(true);

				// armo el compFuenteResMensualVO
				
				CompFuente compFuenteMin = compFuenteRes.getComFueMin();
				CompFuente compFuenteSus = compFuenteRes.getComFueSus();
				List<Object[]> listPeriodos;
				listPeriodos = EfDAOFactory.getPlaFueDatComDAO()
						.getListPeriodosDePlaFueDatCom(compFuenteMin,
								compFuenteSus);
				log.debug("Cantidad Periodos: "+listPeriodos.size());
				for (Object[] object:listPeriodos) {
					CompFuenteResMensualVO compFuenteResMensual = new CompFuenteResMensualVO();
					Integer periodo = (Integer) object[0];
					Integer anio = (Integer) object[1];
					log.debug("periodo: "+periodo);
					log.debug("anio: "+anio);
					PlaFueDatCom plaFueDatComMin = PlaFueDatCom
							.getByPeriodoAnio(compFuenteMin, periodo, anio);
					PlaFueDatCom plaFueDatComSus = PlaFueDatCom
							.getByPeriodoAnio(compFuenteSus, periodo, anio);

					compFuenteResMensual.setPeriodo(periodo);
					compFuenteResMensual.setAnio(anio);
					
					if (plaFueDatComMin!=null)
						compFuenteResMensual.setBaseFuenteMin(plaFueDatComMin.getTotalForComparacion());
					
					if (plaFueDatComSus!=null)
						compFuenteResMensual.setBaseFuenteSus(plaFueDatComSus.getTotalForComparacion());
					
					compFuenteResVO.getListCompFuenteResMensual().add(compFuenteResMensual);
				}

			}
			comparacionVO.getListCompFuenteRes().add(compFuenteResVO);
		}
		

		return comparacionVO;
	}
}
