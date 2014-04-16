//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.bean;

import java.util.ArrayList;
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
import org.hibernate.annotations.OrderBy;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import ar.gov.rosario.siat.ef.iface.model.FuenteInfoVO;
import ar.gov.rosario.siat.ef.iface.model.PlaFueDatColVO;
import ar.gov.rosario.siat.ef.iface.model.PlaFueDatDetVO;
import ar.gov.rosario.siat.ef.iface.model.PlaFueDatVO;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Bean correspondiente a PlaFueDat
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_plaFueDat")
public class PlaFueDat extends BaseBO {

	@Transient
	private Logger log = Logger.getLogger(PlaFueDat.class);
	
	private static final long serialVersionUID = 1L;

	public static final int CANT_MAX_CONCEPTOS = 12;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idOrdenControl")
	private OrdenControl ordenControl;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idFuenteInfo")
	private FuenteInfo fuenteInfo;

	@Column(name = "fecDesProm")
	private Date fecDesProm;

	@Column(name = "fecHasProm")
	private Date fecHasProm;

	@Column(name = "observacion")
	private String observacion;

	@OneToMany(mappedBy = "plaFueDat")
	@JoinColumn(name = "idPlaFueDat")
	@OrderBy(clause = "orden, nroColumna ASC")
	private List<PlaFueDatCol> listPlaFueDatCol;

	@OneToMany(mappedBy = "plaFueDat")
	@JoinColumn(name = "idPlaFueDat")
	@OrderBy(clause = "anio, periodo ASC")
	private List<PlaFueDatDet> listPlaFueDatDet;

	// <#Propiedades#>

	// Constructores
	public PlaFueDat() {
		super();
		// Seteo de valores default
	}

	public PlaFueDat(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static PlaFueDat getById(Long id) {
		return (PlaFueDat) EfDAOFactory.getPlaFueDatDAO().getById(id);
	}

	public static PlaFueDat getByIdNull(Long id) {
		return (PlaFueDat) EfDAOFactory.getPlaFueDatDAO().getByIdNull(id);
	}

	public static List<PlaFueDat> getList() {
		return (ArrayList<PlaFueDat>) EfDAOFactory.getPlaFueDatDAO().getList();
	}

	public static List<PlaFueDat> getListActivos() {
		return (ArrayList<PlaFueDat>) EfDAOFactory.getPlaFueDatDAO()
				.getListActiva();
	}
	

	// Getters y setters
	public OrdenControl getOrdenControl() {
		return ordenControl;
	}

	public void setOrdenControl(OrdenControl ordenControl) {
		this.ordenControl = ordenControl;
	}

	public FuenteInfo getFuenteInfo() {
		return fuenteInfo;
	}

	public void setFuenteInfo(FuenteInfo fuenteInfo) {
		this.fuenteInfo = fuenteInfo;
	}

	public Date getFecDesProm() {
		return fecDesProm;
	}

	public void setFecDesProm(Date fecDesProm) {
		this.fecDesProm = fecDesProm;
	}

	public Date getFecHasProm() {
		return fecHasProm;
	}

	public void setFecHasProm(Date fecHasProm) {
		this.fecHasProm = fecHasProm;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public List<PlaFueDatCol> getListPlaFueDatCol() {
		return listPlaFueDatCol;
	}

	public void setListPlaFueDatCol(List<PlaFueDatCol> listPlaFueDatCol) {
		this.listPlaFueDatCol = listPlaFueDatCol;
	}

	public List<PlaFueDatDet> getListPlaFueDatDet() {
		return listPlaFueDatDet;
	}

	public void setListPlaFueDatDet(List<PlaFueDatDet> listPlaFueDatDet) {
		this.listPlaFueDatDet = listPlaFueDatDet;
	}

	// Validaciones
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}

		// Validaciones de Negocio
		if (fuenteInfo == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					EfError.FUENTEINFO_LABEL);
		}

		if (hasError()) {
			return false;
		}

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
		if (GenericDAO.hasReference(this, PlaFueDatCol.class, " plaFueDat ")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
					EfError.PLAFUEDAT_LABEL, EfError.PLAFUEDATCOL_LABEL);
		}

		if (GenericDAO.hasReference(this, PlaFueDatDet.class, " plaFueDat ")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
					EfError.PLAFUEDAT_LABEL, EfError.PLAFUEDATDET_LABEL);
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

	/**
	 * Devuelve el minimo nro de columna sin utilizar, al momento de crear un
	 * concepto, para asignarselo.
	 */
	public Integer getMinNroColumnaSinUtilizar() {
		
		if (listPlaFueDatCol != null && listPlaFueDatCol.size() > 0) {
			
			// Ordena los PlafueDatcol en una lista temporal, por nroColumna
			List<PlaFueDatCol> listColTmp = new ArrayList<PlaFueDatCol>();
			listColTmp.addAll(listPlaFueDatCol);
			
			Comparator<PlaFueDatCol> comp = new Comparator<PlaFueDatCol>(){
				
				public int compare(PlaFueDatCol o1, PlaFueDatCol o2) {
					if(o1.getNroColumna()>o2.getNroColumna())
						return 1;
					if(o1.getNroColumna()<o2.getNroColumna())
						return -1;
					return 0;
				}			
			};
			Collections.sort(listColTmp, comp);
			
			int nroColAnterior = 0;
			for (PlaFueDatCol plaFueDatCol : listColTmp) {
				Integer nroColumnaActual = plaFueDatCol.getNroColumna();
				if ((nroColumnaActual - nroColAnterior) > 1)
					return (nroColumnaActual - 1);
				nroColAnterior = nroColumnaActual;
			}
			
			// si llega aca tiene todos los nroColumna consecutivos, devuelve el que sigue
			return listColTmp.size()+1;
		}

		return 1;
	}

	/**
	 * Activa el PlaFueDat. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getPlaFueDatDAO().update(this);
	}

	/**
	 * Desactiva el PlaFueDat. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getPlaFueDatDAO().update(this);
	}

	/**
	 * Valida la activacion del PlaFueDat
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
	 * Valida la desactivacion del PlaFueDat
	 * 
	 * @return boolean
	 */
	private boolean validateDesactivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}

	// <#MetodosBeanDetalle#>
	// ABM PlaFueDatCol
	public PlaFueDatCol createPlaFueDatCol(PlaFueDatCol plaFueDatCol)
			throws Exception {
		// Validaciones de negocio
		if (!plaFueDatCol.validateCreate()) {
			return plaFueDatCol;
		}
		EfDAOFactory.getPlaFueDatColDAO().update(plaFueDatCol);

		return plaFueDatCol;
	}

	public PlaFueDatCol updatePlaFueDatCol(PlaFueDatCol plaFueDatCol)
			throws Exception {

		// Validaciones de negocio
		if (!plaFueDatCol.validateUpdate()) {
			return plaFueDatCol;
		}
		EfDAOFactory.getPlaFueDatColDAO().update(plaFueDatCol);

		return plaFueDatCol;
	}

	public PlaFueDatCol deletePlaFueDatCol(PlaFueDatCol plaFueDatCol)
			throws Exception {

		// Validaciones de negocio
		if (!plaFueDatCol.validateDelete()) {
			return plaFueDatCol;
		}

		EfDAOFactory.getPlaFueDatColDAO().delete(plaFueDatCol);

		return plaFueDatCol;
	}

	// ABM PlaFueDatDet
	public PlaFueDatDet createPlaFueDatDet(PlaFueDatDet plaFueDatDet)throws Exception {
		// Validaciones de negocio
		if (!plaFueDatDet.validateCreate()) {
			return plaFueDatDet;
		}
		EfDAOFactory.getPlaFueDatDetDAO().update(plaFueDatDet);
		
		return plaFueDatDet;
	}
	
	public PlaFueDatDet updatePlaFueDatDet(PlaFueDatDet plaFueDatDet)throws Exception {
		
		// Validaciones de negocio
		if (!plaFueDatDet.validateUpdate()) {
			return plaFueDatDet;
		}
		EfDAOFactory.getPlaFueDatDetDAO().update(plaFueDatDet);
		
		return plaFueDatDet;
	}
	
	public PlaFueDatDet deletePlaFueDatDet(PlaFueDatDet plaFueDatDet)throws Exception {
	
		// Validaciones de negocio
		if (!plaFueDatDet.validateDelete()) {
			return plaFueDatDet;
		}
		
		EfDAOFactory.getPlaFueDatDetDAO().delete(plaFueDatDet);
		
		return plaFueDatDet;
	}


	/**
	 * Calcula el nivel 0. Setea la ordenControl y la fuentaInfo. Setea la lista de conceptos y detalles con nivel 0
	 * @return
	 * @throws Exception
	 */
	public PlaFueDatVO toVO4Planilla() throws Exception {
		PlaFueDatVO plaFueDatVO = (PlaFueDatVO) this.toVO(0, false);
		plaFueDatVO.setListPlaFueDatDet(new ArrayList<PlaFueDatDetVO>());
		
		plaFueDatVO.setOrdenControl(ordenControl.toVOForView(false, false));
		plaFueDatVO.setFuenteInfo((FuenteInfoVO) fuenteInfo.toVO(0, false));
		
		plaFueDatVO.setListPlaFueDatCol(ListUtilBean.toVO(listPlaFueDatCol, 0, false));

		// lista de detalles - para cada PlaFueDatDet itera las columnas
		for(PlaFueDatDet plaFueDatDet: listPlaFueDatDet){
			
			PlaFueDatDetVO plaFueDatDetVO = new PlaFueDatDetVO();
			plaFueDatDetVO.setId(plaFueDatDet.getId());
			plaFueDatDetVO.setPeriodo(plaFueDatDet.getPeriodo());
			plaFueDatDetVO.setAnio(plaFueDatDet.getAnio());
			
			// itera secuencialmente porque la lista ya esta ordenada por "orden, nroColumna"
			// el orden de las columnas del VO pueden diferir de las del Bean, ya que se tiene en cuenta para la visualizacion el nroOrden de PlafueDatCol
			// Esto quiere decir que por ejemplo la columna 1 del VO puede tener el valor de la columna 3 del bean.
			int i=1;
			for(PlaFueDatCol plaFueDatCol: listPlaFueDatCol){
				Double valor = 0D;
				switch (plaFueDatCol.getNroColumna()){
					case 1:valor=plaFueDatDet.getCol1();break;
					case 2:valor=plaFueDatDet.getCol2();break;
					case 3:valor=plaFueDatDet.getCol3();break;
					case 4:valor=plaFueDatDet.getCol4();break;
					case 5:valor=plaFueDatDet.getCol5();break;
					case 6:valor=plaFueDatDet.getCol6();break;
					case 7:valor=plaFueDatDet.getCol7();break;
					case 8:valor=plaFueDatDet.getCol8();break;
					case 9:valor=plaFueDatDet.getCol9();break;
					case 10:valor=plaFueDatDet.getCol10();break;
					case 11:valor=plaFueDatDet.getCol11();break;
					case 12:valor=plaFueDatDet.getCol12();break;
				}
				
				if(plaFueDatCol.getOculta().equals(SiNo.NO.getId())){
					
					switch(i){
						case 1:plaFueDatDetVO.setCol1(valor);break;
						case 2:plaFueDatDetVO.setCol2(valor);break;
						case 3:plaFueDatDetVO.setCol3(valor);break;
						case 4:plaFueDatDetVO.setCol4(valor);break;
						case 5:plaFueDatDetVO.setCol5(valor);break;
						case 6:plaFueDatDetVO.setCol6(valor);break;
						case 7:plaFueDatDetVO.setCol7(valor);break;
						case 8:plaFueDatDetVO.setCol8(valor);break;
						case 9:plaFueDatDetVO.setCol9(valor);break;
						case 10:plaFueDatDetVO.setCol10(valor);break;
						case 11:plaFueDatDetVO.setCol11(valor);break;
						case 12:plaFueDatDetVO.setCol12(valor);break;
					}
				}
				
				// acumula el valor al total de la fila, si posee SumaEnTotal
				if(plaFueDatCol.getSumaEnTotal().equals(SiNo.SI.getId())){
					plaFueDatDetVO.setTotal(plaFueDatDetVO.getTotal()+valor);
				}
				
				i++;
			}
			plaFueDatVO.getListPlaFueDatDet().add(plaFueDatDetVO);
		}
		
		return plaFueDatVO;
	}


	/**
	 * Verifica que la planilla tenga definido todos los periodos (PlaFueDatDet) en el rango pasado como parametro
	 * @param periodoDesde
	 * @param anioDesde
	 * @param periodoHasta
	 * @param anioHasta
	 * @return false si al menos 1 periodo dentro del rango no esta definido
	 */
	public boolean getTienePeriodoDefinido(Integer periodoDesde, Integer anioDesde, Integer periodoHasta, Integer anioHasta) {
		
		
		Date fechaDesde=DateUtil.getDate("01/"+StringUtil.completarCerosIzq(periodoDesde.toString(), 2)+"/"+anioDesde, DateUtil.ddSMMSYYYY_MASK);
		Date fechaHasta=DateUtil.getDate("01/"+StringUtil.completarCerosIzq(periodoHasta.toString(), 2)+"/"+anioHasta, DateUtil.ddSMMSYYYY_MASK);
		
		List<Date>listDate = DateUtil.getListFirstDayEachMonth(fechaDesde, fechaHasta);
		
		log.debug("desde:"+fechaDesde);
		log.debug("Hasta:"+fechaHasta);
		
		/*
		Calendar calendarDesde = Calendar.getInstance();
		calendarDesde.set(Calendar.MONTH, periodoDesde);
		calendarDesde.set(Calendar.YEAR, anioDesde);
		
		Calendar calendarHasta = Calendar.getInstance();
		calendarHasta.set(Calendar.MONTH, periodoHasta);
		calendarHasta.set(Calendar.YEAR, anioHasta);
		
		
		
		while(DateUtil.isDateBeforeOrEqual(calendarDesde.getTime(), calendarHasta.getTime())){
			
			int periodo = calendarDesde.get(Calendar.MONTH);
			int anio = calendarDesde.get(Calendar.YEAR);
			if(PlaFueDatDet.getByPeriodoAnio(this, periodo, anio)==null)
				return false;
			
			calendarDesde.add(Calendar.MONTH, 1);
			if(calendarDesde.get(Calendar.MONTH)==0) // el 0 no se tiene en cuenta
				calendarDesde.add(Calendar.MONTH, 1);	
		}
		*/
		
		for (Date fecha:listDate){
			if(PlaFueDatDet.getByPeriodoAnio(this, DateUtil.getMes(fecha), DateUtil.getAnio(fecha))==null)
				return false;
		}
		return true;
	}

	public PlaFueDatVO toVO4Print()throws Exception{
	 	PlaFueDatVO plaFueDatVO = (PlaFueDatVO) this.toVO(0, false);
		plaFueDatVO.setFuenteInfo((FuenteInfoVO) this.fuenteInfo.toVO4Print());
	    
		// Lista de PlaFueDatCol
		if(listPlaFueDatCol!=null){
			List<PlaFueDatColVO> listPlaFueDatColVO = new ArrayList<PlaFueDatColVO>();
			for(PlaFueDatCol p:listPlaFueDatCol){
				listPlaFueDatColVO.add(p.toVO4Print());
			}
			plaFueDatVO.setListPlaFueDatCol(listPlaFueDatColVO);
		}
		
		// Lista de PlaFueDatDet
		if(listPlaFueDatDet!=null){
			List<PlaFueDatDetVO> listPlaFueDatDetVO = new ArrayList<PlaFueDatDetVO>();
			for(PlaFueDatDet p:listPlaFueDatDet){
				listPlaFueDatDetVO.add(p.toVO4Print());
			}
			plaFueDatVO.setListPlaFueDatDet(listPlaFueDatDetVO);
		}
		return plaFueDatVO;
	}
	
	public String getTituloView(){
		return observacion+
			(!StringUtil.isNullOrEmpty(fuenteInfo.getNombreFuente())?"("+fuenteInfo.getNombreFuente()+")":"");
	}


}
