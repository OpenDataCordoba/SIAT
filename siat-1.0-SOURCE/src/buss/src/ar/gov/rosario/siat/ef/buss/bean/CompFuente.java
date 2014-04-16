//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.bean;

import java.util.ArrayList;
import java.util.Calendar;
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
import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import ar.gov.rosario.siat.ef.iface.model.CompFuenteColVO;
import ar.gov.rosario.siat.ef.iface.model.CompFuenteVO;
import ar.gov.rosario.siat.ef.iface.model.ComparacionVO;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlVO;
import ar.gov.rosario.siat.ef.iface.model.PlaFueDatComVO;
import ar.gov.rosario.siat.ef.iface.model.PlaFueDatVO;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.TipoPeriodicidad;

/**
 * Bean correspondiente a CompFuente
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_compFuente")
public class CompFuente extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idComparacion")
	private Comparacion comparacion;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idPlaFueDat")
	private PlaFueDat plaFueDat;

	@Column(name = "total")
	private Double total;

	@Column(name = "periodoDesde")
	private Integer periodoDesde;

	@Column(name = "anioDesde")
	private Integer anioDesde;

	@Column(name = "periodoHasta")
	private Integer periodoHasta;

	@Column(name = "anioHasta")
	private Integer anioHasta;

	@OneToMany( mappedBy="compFuente")
	@JoinColumn(name="idCompFuente")
	@OrderBy(clause = "anio, periodo ASC")
	private List<PlaFueDatCom> listPlaFueDatCom = new ArrayList<PlaFueDatCom>();

	@OneToMany( mappedBy="compFuente")
	@JoinColumn(name="idCompFuente")
	@OrderBy(clause = "orden ASC")
	private List<CompFuenteCol> listCompFuenteCol = new ArrayList<CompFuenteCol>();

	// <#Propiedades#>

	// Constructores
	public CompFuente() {
		super();
		// Seteo de valores default
	}

	public CompFuente(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static CompFuente getById(Long id) {
		return (CompFuente) EfDAOFactory.getCompFuenteDAO().getById(id);
	}

	public static CompFuente getByIdNull(Long id) {
		return (CompFuente) EfDAOFactory.getCompFuenteDAO().getByIdNull(id);
	}

	public static List<CompFuente> getList() {
		return (ArrayList<CompFuente>) EfDAOFactory.getCompFuenteDAO()
				.getList();
	}

	public static List<CompFuente> getListActivos() {
		return (ArrayList<CompFuente>) EfDAOFactory.getCompFuenteDAO()
				.getListActiva();
	}

	// Getters y setters
	public Comparacion getComparacion() {
		return comparacion;
	}

	public void setComparacion(Comparacion comparacion) {
		this.comparacion = comparacion;
	}

	public PlaFueDat getPlaFueDat() {
		return plaFueDat;
	}

	public void setPlaFueDat(PlaFueDat plaFueDat) {
		this.plaFueDat = plaFueDat;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Integer getPeriodoDesde() {
		return periodoDesde;
	}

	public void setPeriodoDesde(Integer periodoDesde) {
		this.periodoDesde = periodoDesde;
	}

	public Integer getAnioDesde() {
		return anioDesde;
	}

	public void setAnioDesde(Integer anioDesde) {
		this.anioDesde = anioDesde;
	}

	public Integer getPeriodoHasta() {
		return periodoHasta;
	}

	public void setPeriodoHasta(Integer periodoHasta) {
		this.periodoHasta = periodoHasta;
	}

	public Integer getAnioHasta() {
		return anioHasta;
	}

	public void setAnioHasta(Integer anioHasta) {
		this.anioHasta = anioHasta;
	}

	public List<PlaFueDatCom> getListPlaFueDatCom() {
		return listPlaFueDatCom;
	}

	public void setListPlaFueDatCom(List<PlaFueDatCom> listPlaFueDatCom) {
		this.listPlaFueDatCom = listPlaFueDatCom;
	}

	public List<CompFuenteCol> getListCompFuenteCol() {
		return listCompFuenteCol;
	}

	public void setListCompFuenteCol(List<CompFuenteCol> listCompFuenteCol) {
		this.listCompFuenteCol = listCompFuenteCol;
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
		if (GenericDAO.hasReference(this, CompFuenteRes.class, " comFueMin ") || 
			GenericDAO.hasReference(this, CompFuenteRes.class, " ComFueSus ")	) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
					EfError.COMPFUENTE_LABEL, EfError.COMPFUENTERES_LABEL);
		}
		
		if (GenericDAO.hasReference(this, OrdConBasImp.class, " compFuente ")) {
				addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
						EfError.COMPFUENTE_LABEL, EfError.ORDCONBASIMP_LABEL);
		}		
		
		
		if (hasError()) {
			return false;
		}

		return true;
	}

	private boolean validate() throws Exception {

		// Validaciones
		if (plaFueDat == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					EfError.PLAFUEDAT_LABEL);
		}

		if (hasError()) {
			return false;
		}

		return true;
	}

	// Metodos de negocio

	/**
	 * Activa el CompFuente. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getCompFuenteDAO().update(this);
	}

	/**
	 * Desactiva el CompFuente. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getCompFuenteDAO().update(this);
	}

	/**
	 * Valida la activacion del CompFuente
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
	 * Valida la desactivacion del CompFuente
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
	//	---> ABM PlaFueDatCom
	public PlaFueDatCom createPlaFueDatCom(PlaFueDatCom plaFueDatCom) throws Exception {

		// Validaciones de negocio
		if (!plaFueDatCom.validateCreate()) {
			return plaFueDatCom;
		}

		EfDAOFactory.getPlaFueDatComDAO().update(plaFueDatCom);

		return plaFueDatCom;
	}
	
	public PlaFueDatCom updatePlaFueDatCom(PlaFueDatCom plaFueDatCom) throws Exception {
		
		// Validaciones de negocio
		if (!plaFueDatCom.validateUpdate()) {
			return plaFueDatCom;
		}

		EfDAOFactory.getPlaFueDatComDAO().update(plaFueDatCom);
		
		return plaFueDatCom;
	}
	
	public PlaFueDatCom deletePlaFueDatCom(PlaFueDatCom plaFueDatCom) throws Exception {
	
		// Validaciones de negocio
		if (!plaFueDatCom.validateDelete()) {
			return plaFueDatCom;
		}
		
		EfDAOFactory.getPlaFueDatComDAO().delete(plaFueDatCom);
		
		return plaFueDatCom;
	}
	// <--- ABM PlaFueDatCom
	
	public void deleteListPlaFueDatCom (){
		EfDAOFactory.getPlaFueDatComDAO().deleteListByCompFuente(this);
	}
	
	//	---> ABM CompFuenteCol
    public CompFuenteCol createCompFuenteCol(CompFuenteCol compFuenteCol) throws Exception {

		// Validaciones de negocio
		if (!compFuenteCol.validateCreate()) {
			return compFuenteCol;
		}

		EfDAOFactory.getCompFuenteColDAO().update(compFuenteCol);

		return compFuenteCol;
	}
    
    public void deleteListCompFuenteCol(){
    	EfDAOFactory.getCompFuenteColDAO().deleteByCompFuente(this);
    }
    
	
	public CompFuenteCol updateCompFuenteCol(CompFuenteCol compFuenteCol) throws Exception {
		
		// Validaciones de negocio
		if (!compFuenteCol.validateUpdate()) {
			return compFuenteCol;
		}

		EfDAOFactory.getCompFuenteColDAO().update(compFuenteCol);
		
		return compFuenteCol;
	}
	
	public CompFuenteCol deleteCompFuenteCol(CompFuenteCol compFuenteCol) throws Exception {
	
		// Validaciones de negocio
		if (!compFuenteCol.validateDelete()) {
			return compFuenteCol;
		}
		
		EfDAOFactory.getCompFuenteColDAO().delete(compFuenteCol);
		
		return compFuenteCol;
	}
	// <--- ABM CompFuenteCol

	public CompFuenteVO toVO4View() throws Exception {
		CompFuenteVO compFuenteVO = new CompFuenteVO();
		compFuenteVO.setId(getId());
		compFuenteVO.setAnioDesde(anioDesde);
		compFuenteVO.setAnioHasta(anioHasta);
		
		if(comparacion!=null){
			compFuenteVO.setComparacion((ComparacionVO) comparacion.toVO(0, false));
			compFuenteVO.getComparacion().setOrdenControl((OrdenControlVO) comparacion.getOrdenControl().toVO(0, false));
		}else{
			compFuenteVO.setComparacion(new ComparacionVO());
			compFuenteVO.getComparacion().setOrdenControl((OrdenControlVO) plaFueDat.getOrdenControl().toVO(0, false));
		}
		
		compFuenteVO.setPeriodoDesde(periodoDesde);
		compFuenteVO.setPeriodoHasta(periodoHasta);
		compFuenteVO.setTotal(total);
		compFuenteVO.setPlaFueDat((PlaFueDatVO) plaFueDat.toVO(1, false));
		
		// lista de columnas
		compFuenteVO.setListCompFuenteCol(ListUtilBean.toVO(listCompFuenteCol, 0, false));
		
		// lista de detalles
		// lista de detalles - para cada PlaFueDatDet itera las columnas
		for(PlaFueDatCom plaFueDatCom: listPlaFueDatCom){
			
			PlaFueDatComVO plaFueDatComVO = new PlaFueDatComVO();
			plaFueDatComVO.setId(plaFueDatCom.getId());
			plaFueDatComVO.setPeriodo(plaFueDatCom.getPeriodo());
			plaFueDatComVO.setAnio(plaFueDatCom.getAnio());
			
			// itera secuencialmente porque la lista ya esta ordenada por "orden, nroColumna"
			// el orden de las columnas del VO pueden diferir de las del Bean, ya que se tiene en cuenta para la visualizacion el nroOrden de CompFuenteCol
			// Esto quiere decir que por ejemplo la columna 1 del VO puede tener el valor de la columna 3 del bean.
			int i=1;
			for(CompFuenteCol compFuenteCol: listCompFuenteCol){
				Double valor = 0D;
				switch (compFuenteCol.getNroColumna()){
					case 1:valor=plaFueDatCom.getCol1();break;
					case 2:valor=plaFueDatCom.getCol2();break;
					case 3:valor=plaFueDatCom.getCol3();break;
					case 4:valor=plaFueDatCom.getCol4();break;
					case 5:valor=plaFueDatCom.getCol5();break;
					case 6:valor=plaFueDatCom.getCol6();break;
					case 7:valor=plaFueDatCom.getCol7();break;
					case 8:valor=plaFueDatCom.getCol8();break;
					case 9:valor=plaFueDatCom.getCol9();break;
					case 10:valor=plaFueDatCom.getCol10();break;
					case 11:valor=plaFueDatCom.getCol11();break;
					case 12:valor=plaFueDatCom.getCol12();break;
				}
				
				if(compFuenteCol.getOculta().equals(SiNo.NO.getId())){
					
					switch(i){
						case 1:plaFueDatComVO.setCol1(valor);break;
						case 2:plaFueDatComVO.setCol2(valor);break;
						case 3:plaFueDatComVO.setCol3(valor);break;
						case 4:plaFueDatComVO.setCol4(valor);break;
						case 5:plaFueDatComVO.setCol5(valor);break;
						case 6:plaFueDatComVO.setCol6(valor);break;
						case 7:plaFueDatComVO.setCol7(valor);break;
						case 8:plaFueDatComVO.setCol8(valor);break;
						case 9:plaFueDatComVO.setCol9(valor);break;
						case 10:plaFueDatComVO.setCol10(valor);break;
						case 11:plaFueDatComVO.setCol11(valor);break;
						case 12:plaFueDatComVO.setCol12(valor);break;
					}
				}
				
				// acumula el valor al total de la fila, si posee SumaEnTotal
				if(compFuenteCol.getSumaEnTotal().equals(SiNo.SI.getId())){
					plaFueDatComVO.setTotal(plaFueDatComVO.getTotal()+valor);
				}
				
				i++;
			}
			compFuenteVO.getListPlaFueDatCom().add(plaFueDatComVO);
		}
		
		return compFuenteVO;
	}

	public CompFuenteVO toVO4Ajustes() throws Exception {
		CompFuenteVO compFuenteVO = new CompFuenteVO();
		compFuenteVO.setId(getId());
		compFuenteVO.setAnioDesde(anioDesde);
		compFuenteVO.setAnioHasta(anioHasta);
		
		if(comparacion!=null){
			compFuenteVO.setComparacion((ComparacionVO) comparacion.toVO(0, false));
			compFuenteVO.getComparacion().setOrdenControl((OrdenControlVO) comparacion.getOrdenControl().toVO(0, false));
		}else{
			compFuenteVO.setComparacion(new ComparacionVO());
			compFuenteVO.getComparacion().setOrdenControl((OrdenControlVO) plaFueDat.getOrdenControl().toVO(0, false));
		}
		
		compFuenteVO.setPeriodoDesde(periodoDesde);
		compFuenteVO.setPeriodoHasta(periodoHasta);
		compFuenteVO.setPlaFueDat((PlaFueDatVO) plaFueDat.toVO(1, false));
		
		// lista de columnas
		compFuenteVO.setListCompFuenteCol(ListUtilBean.toVO(listCompFuenteCol, 0, false));
		
		// lista de detalles
		for(PlaFueDatCom plaFueDatCom: listPlaFueDatCom){
			
			PlaFueDatComVO plaFueDatComVO = new PlaFueDatComVO();
			plaFueDatComVO.setId(plaFueDatCom.getId());
			plaFueDatComVO.setPeriodo(plaFueDatCom.getPeriodo());
			plaFueDatComVO.setAnio(plaFueDatCom.getAnio());
			
			int i=1;
			for(CompFuenteCol compFuenteCol: listCompFuenteCol){
				Double valorOrig = 0D;
				double ajuste = 0D;
				switch (compFuenteCol.getNroColumna()){
					case 1:{
						valorOrig=plaFueDatCom.getCol1();
						ajuste = (plaFueDatCom.getAj1()!=null?plaFueDatCom.getAj1():0D);break;
					}
					case 2:{
						valorOrig=plaFueDatCom.getCol2();
						ajuste = (plaFueDatCom.getAj2()!=null?plaFueDatCom.getAj2():0D);break;
					}
					case 3:{
						valorOrig=plaFueDatCom.getCol3();
						ajuste = (plaFueDatCom.getAj3()!=null?plaFueDatCom.getAj3():0D);break;
					}
					case 4:{
						valorOrig=plaFueDatCom.getCol4();
						ajuste = (plaFueDatCom.getAj4()!=null?plaFueDatCom.getAj4():0D);break;
					}
					case 5:{
						valorOrig=plaFueDatCom.getCol5();
						ajuste = (plaFueDatCom.getAj5()!=null?plaFueDatCom.getAj5():0D);break;
					}
					case 6:{
						valorOrig=plaFueDatCom.getCol6();
						ajuste = (plaFueDatCom.getAj6()!=null?plaFueDatCom.getAj6():0D);break;
					}
					case 7:{
						valorOrig=plaFueDatCom.getCol7();
						ajuste = (plaFueDatCom.getAj7()!=null?plaFueDatCom.getAj7():0D);break;
					}
					case 8:{
						valorOrig=plaFueDatCom.getCol8();
						ajuste = (plaFueDatCom.getAj8()!=null?plaFueDatCom.getAj8():0D);break;
					}
					case 9:{
						valorOrig=plaFueDatCom.getCol9();
						ajuste = (plaFueDatCom.getAj9()!=null?plaFueDatCom.getAj9():0D);break;
					}
					case 10:{
						valorOrig=plaFueDatCom.getCol10();
						ajuste = (plaFueDatCom.getAj10()!=null?plaFueDatCom.getAj10():0D);break;
					}
					case 11:{
						valorOrig=plaFueDatCom.getCol11();
						ajuste = (plaFueDatCom.getAj11()!=null?plaFueDatCom.getAj11():0D);break;
					}
					case 12:{
						valorOrig=plaFueDatCom.getCol12();
						ajuste = (plaFueDatCom.getAj12()!=null?plaFueDatCom.getAj12():0D);break;
					}
				}
				
				if(compFuenteCol.getOculta().equals(SiNo.NO.getId()) && 
					compFuenteCol.getSumaEnTotal().equals(SiNo.SI.getId())){
					
					switch(i){
						case 1:{
							plaFueDatComVO.setCol1(valorOrig);
							plaFueDatComVO.setAj1(ajuste);break;
						}
						case 2:{
							plaFueDatComVO.setCol2(valorOrig);
							plaFueDatComVO.setAj2(ajuste);break;
						}
						case 3:{
							plaFueDatComVO.setCol3(valorOrig);
							plaFueDatComVO.setAj3(ajuste);break;
						}
						case 4:{
							plaFueDatComVO.setCol4(valorOrig);
							plaFueDatComVO.setAj4(ajuste);break;
						}
						case 5:{
							plaFueDatComVO.setCol5(valorOrig);
							plaFueDatComVO.setAj5(ajuste);break;
						}
						case 6:{
							plaFueDatComVO.setCol6(valorOrig);
							plaFueDatComVO.setAj6(ajuste);break;
						}
						case 7:{
							plaFueDatComVO.setCol7(valorOrig);
							plaFueDatComVO.setAj7(ajuste);break;
						}
						case 8:{
							plaFueDatComVO.setCol8(valorOrig);
							plaFueDatComVO.setAj8(ajuste);break;
						}
						case 9:{
							plaFueDatComVO.setCol9(valorOrig);
							plaFueDatComVO.setAj9(ajuste);break;
						}
						case 10:{
							plaFueDatComVO.setCol10(valorOrig);
							plaFueDatComVO.setAj10(ajuste);break;
						}
						case 11:{
							plaFueDatComVO.setCol11(valorOrig);
							plaFueDatComVO.setAj11(ajuste);break;
						}
						case 12:{
							plaFueDatComVO.setCol12(valorOrig);
							plaFueDatComVO.setAj12(ajuste);break;
						}
					}
					
					// actualiza total de valore originales de la fila
					plaFueDatComVO.setTotal(plaFueDatComVO.getTotal()+valorOrig);
					
					// actualiza total ajustes de la fila
					plaFueDatComVO.setTotalAjustes(plaFueDatComVO.getTotalAjustes()+ajuste);
					
					// actualiza total de la columna de totales originales
					compFuenteVO.setTotal(compFuenteVO.getTotal()+valorOrig);
					
					// actualiza total de la columna de ajustes
					compFuenteVO.setTotalAjustes(compFuenteVO.getTotalAjustes()+ajuste);
										
					i++;
				}
								
			}
			compFuenteVO.getListPlaFueDatCom().add(plaFueDatComVO);
		}
		
		return compFuenteVO;
	}
	
	/**
	 * Verifica que la planilla tenga definido todos los periodos (PlaFueDatCom) en el rango pasado como parametro
	 * @param periodoDesde
	 * @param anioDesde
	 * @param periodoHasta
	 * @param anioHasta
	 * @return false si al menos 1 periodo dentro del rango no esta definido
	 */
	public boolean getTienePeriodoDefinido(Integer periodoDesde,Integer anioDesde, Integer periodoHasta, Integer anioHasta) {
		Calendar calendarDesde = Calendar.getInstance();
		calendarDesde.set(Calendar.MONTH, periodoDesde);
		calendarDesde.set(Calendar.YEAR, anioDesde);
		
		Calendar calendarHasta = Calendar.getInstance();
		calendarHasta.set(Calendar.MONTH, periodoHasta);
		calendarHasta.set(Calendar.YEAR, anioHasta);
		
		while(DateUtil.isDateBeforeOrEqual(calendarDesde.getTime(), calendarHasta.getTime())){
			
			int periodo = calendarDesde.get(Calendar.MONTH);
			int anio = calendarDesde.get(Calendar.YEAR);
			if(PlaFueDatCom.getByPeriodoAnio(this, periodo, anio)==null)
				return false;
			
			calendarDesde.add(Calendar.MONTH, 1);
			if(calendarDesde.get(Calendar.MONTH)==0) // el 0 no se tiene en cuenta
				calendarDesde.add(Calendar.MONTH, 1);	
		}
		return true;
	}

	
	public CompFuenteCol getCompFuenteCol(int nroCol) {
		for(CompFuenteCol compFuenteCol: listCompFuenteCol){
			if(compFuenteCol.getNroColumna().intValue()==nroCol)
				return compFuenteCol;
		}
		return null;
	}
	
	public String getPeriodoDesde4View(){
		String ret ="";
		if(plaFueDat.getFuenteInfo().getTipoPeriodicidad().equals(TipoPeriodicidad.MENSUAL.getId())){
			ret+=getPeriodoDesde()+"/";
		}
		
		return ret+getAnioDesde();
	}
	
	public String getPeriodoHasta4View(){
		String ret ="";
		if(plaFueDat.getFuenteInfo().getTipoPeriodicidad().equals(TipoPeriodicidad.MENSUAL.getId())){
			ret+=getPeriodoHasta()+"/";
		}
		
		return ret+getAnioHasta();
	}
	
	public String getTotalView(){
        return (total!=null?StringUtil.redondearDecimales(total, 1, 2):"");
  }
	
	public CompFuenteVO toVO4Print()throws Exception{
		CompFuenteVO compFuenteVO = (CompFuenteVO) this.toVO(0, false);
		compFuenteVO.setPlaFueDat((PlaFueDatVO) this.plaFueDat.toVO4Print());
		// Lista de PlaFueDatCol
		if(listCompFuenteCol!=null){
			List<CompFuenteColVO> listCompFuenteColVO = new ArrayList<CompFuenteColVO>();
			for(CompFuenteCol p:listCompFuenteCol){
				listCompFuenteColVO.add(p.toVO4Print());
			}
			compFuenteVO.setListCompFuenteCol(listCompFuenteColVO);
		}
		
		// Lista de PlaFueDatDet
		if(listPlaFueDatCom!=null){
			List<PlaFueDatComVO> listPlaFueDatComVO = new ArrayList<PlaFueDatComVO>();
			for(PlaFueDatCom p:listPlaFueDatCom){
				listPlaFueDatComVO.add(p.toVO4Print());
			}
			compFuenteVO.setListPlaFueDatCom(listPlaFueDatComVO);
		}
		
		return compFuenteVO;
	}

}
