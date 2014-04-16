//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import ar.gov.rosario.siat.ef.iface.model.PlaFueDatComVO;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a PlaFueDatCom
 * 
 * @author tecso
 */
/**
 * @author alejandro
 *
 */
@Entity
@Table(name = "ef_plaFueDatCom")
public class PlaFueDatCom extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idcompFuente")
	private CompFuente compFuente;

	@Column(name = "periodo")
	private Integer periodo;

	@Column(name = "anio")
	private Integer anio;

	@Column(name = "col1")
	private Double col1;

	@Column(name = "col2")
	private Double col2;

	@Column(name = "col3")
	private Double col3;

	@Column(name = "col4")
	private Double col4;

	@Column(name = "col5")
	private Double col5;

	@Column(name = "col6")
	private Double col6;

	@Column(name = "col7")
	private Double col7;

	@Column(name = "col8")
	private Double col8;

	@Column(name = "col9")
	private Double col9;

	@Column(name = "col10")
	private Double col10;

	@Column(name = "col11")
	private Double col11;

	@Column(name = "col12")
	private Double col12;

	@Column(name = "aj1")
	private Double aj1;

	@Column(name = "aj2")
	private Double aj2;

	@Column(name = "aj3")
	private Double aj3;

	@Column(name = "aj4")
	private Double aj4;

	@Column(name = "aj5")
	private Double aj5;

	@Column(name = "aj6")
	private Double aj6;

	@Column(name = "aj7")
	private Double aj7;

	@Column(name = "aj8")
	private Double aj8;

	@Column(name = "aj9")
	private Double aj9;

	@Column(name = "aj10")
	private Double aj10;

	@Column(name = "aj11")
	private Double aj11;

	@Column(name = "aj12")
	private Double aj12;

	@Column(name = "coefStaFe")
	private Double coefStaFe;
	
	@Column(name = "coefRosario")
	private Double coefRosario;
	
	// <#Propiedades#>

	// Constructores
	public PlaFueDatCom() {
		super();
		// Seteo de valores default
	}

	public PlaFueDatCom(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static PlaFueDatCom getById(Long id) {
		return (PlaFueDatCom) EfDAOFactory.getPlaFueDatComDAO().getById(id);
	}

	public static PlaFueDatCom getByIdNull(Long id) {
		return (PlaFueDatCom) EfDAOFactory.getPlaFueDatComDAO().getByIdNull(id);
	}

	public static List<PlaFueDatCom> getList() {
		return (ArrayList<PlaFueDatCom>) EfDAOFactory.getPlaFueDatComDAO()
				.getList();
	}

	public static List<PlaFueDatCom> getListActivos() {
		return (ArrayList<PlaFueDatCom>) EfDAOFactory.getPlaFueDatComDAO()
				.getListActiva();
	}

	public static PlaFueDatCom getByPeriodoAnio(CompFuente compFuente, Integer periodo, Integer anio) {
		return EfDAOFactory.getPlaFueDatComDAO().getByPeriodoAnio(compFuente, periodo, anio);
	}
	
	// Getters y setters
	public CompFuente getCompFuente() {
		return compFuente;
	}

	public void setCompFuente(CompFuente compFuente) {
		this.compFuente = compFuente;
	}

	public Integer getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public Double getCol1() {
		return col1;
	}

	public void setCol1(Double col1) {
		this.col1 = col1;
	}

	public Double getCol2() {
		return col2;
	}

	public void setCol2(Double col2) {
		this.col2 = col2;
	}

	public Double getCol3() {
		return col3;
	}

	public void setCol3(Double col3) {
		this.col3 = col3;
	}

	public Double getCol4() {
		return col4;
	}

	public void setCol4(Double col4) {
		this.col4 = col4;
	}

	public Double getCol5() {
		return col5;
	}

	public void setCol5(Double col5) {
		this.col5 = col5;
	}

	public Double getCol6() {
		return col6;
	}

	public void setCol6(Double col6) {
		this.col6 = col6;
	}

	public Double getCol7() {
		return col7;
	}

	public void setCol7(Double col7) {
		this.col7 = col7;
	}

	public Double getCol8() {
		return col8;
	}

	public void setCol8(Double col8) {
		this.col8 = col8;
	}

	public Double getCol9() {
		return col9;
	}

	public void setCol9(Double col9) {
		this.col9 = col9;
	}

	public Double getCol10() {
		return col10;
	}

	public void setCol10(Double col10) {
		this.col10 = col10;
	}

	public Double getCol11() {
		return col11;
	}

	public void setCol11(Double col11) {
		this.col11 = col11;
	}

	public Double getCol12() {
		return col12;
	}

	public void setCol12(Double col12) {
		this.col12 = col12;
	}

	public Double getAj1() {
		return aj1;
	}

	public void setAj1(Double aj1) {
		this.aj1 = aj1;
	}

	public Double getAj2() {
		return aj2;
	}

	public void setAj2(Double aj2) {
		this.aj2 = aj2;
	}

	public Double getAj3() {
		return aj3;
	}

	public void setAj3(Double aj3) {
		this.aj3 = aj3;
	}

	public Double getAj4() {
		return aj4;
	}

	public void setAj4(Double aj4) {
		this.aj4 = aj4;
	}

	public Double getAj5() {
		return aj5;
	}

	public void setAj5(Double aj5) {
		this.aj5 = aj5;
	}

	public Double getAj6() {
		return aj6;
	}

	public void setAj6(Double aj6) {
		this.aj6 = aj6;
	}

	public Double getAj7() {
		return aj7;
	}

	public void setAj7(Double aj7) {
		this.aj7 = aj7;
	}

	public Double getAj8() {
		return aj8;
	}

	public void setAj8(Double aj8) {
		this.aj8 = aj8;
	}

	public Double getAj9() {
		return aj9;
	}

	public void setAj9(Double aj9) {
		this.aj9 = aj9;
	}

	public Double getAj10() {
		return aj10;
	}

	public void setAj10(Double aj10) {
		this.aj10 = aj10;
	}

	public Double getAj11() {
		return aj11;
	}

	public void setAj11(Double aj11) {
		this.aj11 = aj11;
	}

	public Double getAj12() {
		return aj12;
	}

	public void setAj12(Double aj12) {
		this.aj12 = aj12;
	}

	public Double getCoefStaFe() {
		return coefStaFe;
	}

	public void setCoefStaFe(Double coefStaFe) {
		this.coefStaFe = coefStaFe;
	}

	public Double getCoefRosario() {
		return coefRosario;
	}

	public void setCoefRosario(Double coefRosario) {
		this.coefRosario = coefRosario;
	}

	public String getCol1BasRos() {
		return getBaseRosario(1);
	}
	
	public String getCol2BasRos() {
		return getBaseRosario(2);
	}
	
	public String getCol3BasRos() {
		return getBaseRosario(3);
	}
	
	public String getCol4BasRos() {
		return getBaseRosario(4);
	}
	
	public String getCol5BasRos() {
		return getBaseRosario(5);
	}
	
	public String getCol6BasRos() {
		return getBaseRosario(6);
	}
	
	public String getCol7BasRos() {
		return getBaseRosario(7);
	}
	
	public String getCol8BasRos() {
		return getBaseRosario(8);
	}
	
	public String getCol9BasRos() {
		return getBaseRosario(9);
	}
	
	public String getCol10BasRos() {
		return getBaseRosario(10);
	}
	
	public String getCol11BasRos() {
		return getBaseRosario(11);
	}
	
	public String getCol12BasRos() {
		return getBaseRosario(12);
	}

	private String getBaseRosario(int nroCol){
		Double coefSantaFeTmp=(this.coefStaFe!=null?this.coefStaFe:1D);
		Double coefRosarioTmp=(this.coefRosario!=null?this.coefRosario:1D);
				
		Double valorAjCol = null;
		Double valorCol = null;
		switch(nroCol){
			case 1: valorAjCol = this.aj1; valorCol=this.col1;break;
			case 2: valorAjCol = this.aj2; valorCol=this.col2;break;
			case 3: valorAjCol = this.aj3; valorCol=this.col3;break;
			case 4: valorAjCol = this.aj4; valorCol=this.col4;break;
			case 5: valorAjCol = this.aj5; valorCol=this.col5;break;
			case 6: valorAjCol = this.aj6; valorCol=this.col6;break;
			case 7: valorAjCol = this.aj7; valorCol=this.col7;break;
			case 8: valorAjCol = this.aj8; valorCol=this.col8;break;
			case 9: valorAjCol = this.aj9; valorCol=this.col9;break;
			case 10: valorAjCol = this.aj10; valorCol=this.col10;break;
			case 11: valorAjCol = this.aj11; valorCol=this.col11;break;
			case 12: valorAjCol = this.aj12; valorCol=this.col12;break;			
		}
		
		if(valorAjCol==null)
			valorAjCol=0D;
		
		if(valorCol!=null) {
			double valor = (valorCol+valorAjCol)*coefSantaFeTmp*coefRosarioTmp;
			return StringUtil.redondearDecimales(valor, 1, 2); //Se modifica para que devuelva numero con separadores de miles
			//return StringUtil.formatDoubleWithComa(NumberUtil.round(valor, SiatParam.DEC_IMPORTE_VIEW));
		}
		
		return null;
		
	}
	
	private String getBaseRosarioView(int nroCol){
		Double coefSantaFeTmp=(this.coefStaFe!=null?this.coefStaFe:1D);
		Double coefRosarioTmp=(this.coefRosario!=null?this.coefRosario:1D);
				
		Double valorAjCol = null;
		Double valorCol = null;
		switch(nroCol){
			case 1: valorAjCol = this.aj1; valorCol=this.col1;break;
			case 2: valorAjCol = this.aj2; valorCol=this.col2;break;
			case 3: valorAjCol = this.aj3; valorCol=this.col3;break;
			case 4: valorAjCol = this.aj4; valorCol=this.col4;break;
			case 5: valorAjCol = this.aj5; valorCol=this.col5;break;
			case 6: valorAjCol = this.aj6; valorCol=this.col6;break;
			case 7: valorAjCol = this.aj7; valorCol=this.col7;break;
			case 8: valorAjCol = this.aj8; valorCol=this.col8;break;
			case 9: valorAjCol = this.aj9; valorCol=this.col9;break;
			case 10: valorAjCol = this.aj10; valorCol=this.col10;break;
			case 11: valorAjCol = this.aj11; valorCol=this.col11;break;
			case 12: valorAjCol = this.aj12; valorCol=this.col12;break;			
		}
		
		if(valorAjCol==null)
			valorAjCol=0D;
		
		if(valorCol!=null) {
			double valor = (valorCol+valorAjCol)*coefSantaFeTmp*coefRosarioTmp;
			//return StringUtil.redondearDecimales(valor, 1, 2); Se modifica para que devuelva numero con separadores de miles
			return StringUtil.formatDoubleWithComa(NumberUtil.round(valor, SiatParam.DEC_IMPORTE_VIEW));
		}
		
		return null;
		
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
		if (GenericDAO.hasReference(this, DetAjuDet.class, "plaFueDatCom")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							EfError.PLAFUEDATCOM_LABEL, EfError.DETAJUDET_LABEL);
		}
		
		if (hasError()) {
			return false;
		}

		return true;
	}

	private boolean validate() throws Exception {

		// Validaciones
		if (compFuente == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					EfError.COMPFUENTE_LABEL);
		}

		if (hasError()) {
			return false;
		}

		return true;
	}

	// Metodos de negocio

	/**
	 * Activa el PlaFueDatCom. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getPlaFueDatComDAO().update(this);
	}

	/**
	 * Desactiva el PlaFueDatCom. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getPlaFueDatComDAO().update(this);
	}

	/**
	 * Valida la activacion del PlaFueDatCom
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
	 * Valida la desactivacion del PlaFueDatCom
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
	
	// metodos para reporte
	public String getPeriodoAnioView(){
		return getPeriodo()+"/"+getAnio();
	}

	public PlaFueDatComVO toVO4Print()throws Exception{
		PlaFueDatComVO plaFueDatComVO = (PlaFueDatComVO) this.toVO(0, false);
		
		
		
		return plaFueDatComVO;
	}
	
	public Double getTotalForComparacion(){
		Double total=0D;
		List <Integer>listColumnas = new ArrayList<Integer>();
		for (CompFuenteCol compFuenteCol: CompFuenteCol.getListSumanEnTotalByCompFuente(this.compFuente)){
			listColumnas.add(compFuenteCol.getNroColumna());
		}
		
		if (col1!=null && col1!=0D && listColumnas.contains(1)){
			total += this.col1;
		}
		
		if (col2!=null && col2!=0D && listColumnas.contains(2)){
			total += this.col2;
		}
		
		if (col3!=null && col3!=0D && listColumnas.contains(3)){
			total += this.col3;
		}
		
		if (col4!=null && col4!=0D && listColumnas.contains(4)){
			total += this.col4;
		}
		
		if (col5!=null && col5!=0D && listColumnas.contains(5)){
			total += this.col5;
		}
		
		if (col6!=null && col6!=0D && listColumnas.contains(6)){
			total += this.col6;
		}
		
		if (col7!=null && col7!=0D && listColumnas.contains(7)){
			total += this.col7;
		}
		
		if (col8!=null && col8!=0D && listColumnas.contains(8)){
			total += this.col8;
		}
		
		if (col9!=null && col9!=0D && listColumnas.contains(9)){
			total += this.col9;
		}
		
		if (col10!=null && col10!=0D && listColumnas.contains(10)){
			total += this.col10;
		}
		
		if (col11!=null && col11!=0D && listColumnas.contains(11)){
			total += this.col11;
		}
		
		if (col12!=null && col12!=0D && listColumnas.contains(11)){
			total += this.col12;
		}
		
		return total;
	}
	
}
