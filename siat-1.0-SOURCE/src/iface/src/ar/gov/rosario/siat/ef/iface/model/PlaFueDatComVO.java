//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del PlaFueDatCom
 * @author tecso
 *
 */
public class PlaFueDatComVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "plaFueDatComVO";
	
	private CompFuenteVO compFuente;

    private Integer periodo;

    private Integer anio;
    
    private Integer nroCol;

	private Double col1;

	private Double col2;

	private Double col3;

	private Double col4;

	private Double col5;

	private Double col6;

	private Double col7;

	private Double col8;

	private Double col9;

	private Double col10;

	private Double col11;

	private Double col12;

	private Double aj1;

	private Double aj2;

	private Double aj3;

	private Double aj4;

	private Double aj5;

	private Double aj6;

	private Double aj7;

	private Double aj8;

	private Double aj9;

	private Double aj10;

	private Double aj11;

	private Double aj12;

	private Double total=0D;
	private Double valorCol = 0D;
	private Double totalAjustes=0D;
	private Double valorAjuste = 0D;
	private Double totalAjustado = 0D;
	private Double totalPais;
	
	private Double coefStaFe;
	
	private Double coefRosario;

	private Double baseStaFe;
	
	private Double baseRosario;

	private String col1BasRos;
	private String col2BasRos;
	private String col3BasRos;
	private String col4BasRos;
	private String col5BasRos;
	private String col6BasRos;
	private String col7BasRos;
	private String col8BasRos;
	private String col9BasRos;
	private String col10BasRos;
	private String col11BasRos;
	private String col12BasRos;
	
	// Buss Flags
	
	
	// View Constants
	
	private String totalPaisView = "";
	private String coefStaFeView = "";
	private String coefRosarioView = "";
	private String baseStaFeView = "";
	private String baseRosarioView = "";
	private String anioView = "";
	private String periodoView = "";


	// Constructores
	public PlaFueDatComVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public PlaFueDatComVO(int id, String desc) {
		super();
		setId(new Long(id));
	}
	
	// Getters y Setters

	public CompFuenteVO getCompFuente() {
		return compFuente;
	}

	public void setCompFuente(CompFuenteVO compFuente) {
		this.compFuente = compFuente;
	}

	public Integer getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
		this.periodoView = StringUtil.formatInteger(periodo);
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
		this.anioView = StringUtil.formatInteger(anio);
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
		this.setNroCol(1);
		return aj1;
	}

	public void setAj1(Double aj1) {
		
		this.aj1 = aj1;
	}

	public Double getAj2() {
		this.setNroCol(2);
		return aj2;
	}

	public void setAj2(Double aj2) {
		this.aj2 = aj2;
	}

	public Double getAj3() {
		this.setNroCol(3);
		return aj3;
	}

	public void setAj3(Double aj3) {
		this.aj3 = aj3;
	}

	public Double getAj4() {
		this.setNroCol(4);
		return aj4;
	}

	public void setAj4(Double aj4) {
		this.aj4 = aj4;
	}

	public Double getAj5() {
		this.setNroCol(5);
		return aj5;
	}

	public void setAj5(Double aj5) {
		this.aj5 = aj5;
	}

	public Double getAj6() {
		this.setNroCol(6);
		return aj6;
	}

	public void setAj6(Double aj6) {
		this.aj6 = aj6;
	}

	public Double getAj7() {
		this.setNroCol(7);
		return aj7;
	}

	public void setAj7(Double aj7) {
		this.aj7 = aj7;
	}

	public Double getAj8() {
		this.setNroCol(8);
		return aj8;
	}

	public void setAj8(Double aj8) {
		this.aj8 = aj8;
	}

	public Double getAj9() {
		this.setNroCol(9);
		return aj9;
	}

	public void setAj9(Double aj9) {
		this.aj9 = aj9;
	}

	public Double getAj10() {
		this.setNroCol(10);
		return aj10;
	}

	public void setAj10(Double aj10) {
		this.aj10 = aj10;
	}

	public Double getAj11() {
		this.setNroCol(11);
		return aj11;
	}

	public void setAj11(Double aj11) {
		this.aj11 = aj11;
	}

	public Double getAj12() {
		this.setNroCol(12);
		return aj12;
	}

	public void setAj12(Double aj12) {
		this.aj12 = aj12;
	}

	public Double getTotal() {
		return total;
	}
	
	public void setTotal(Double total) {
		this.total = total;
	}
	
	public Double getTotalAjustes() {
		return totalAjustes;
	}

	public void setTotalAjustes(Double totalAjustes) {
		this.totalAjustes = totalAjustes;
	}
	

	
	public Double getTotalPais() {
		return totalPais;
	}
	
	public void setTotalPais(Double totalPais) {
		this.totalPais = totalPais;
		this.totalPaisView =StringUtil.formatDouble(totalPais);
	}
	
	public Double getCoefStaFe() {
		return coefStaFe;
	}
	
	public void setCoefStaFe(Double coefStaFe) {
		this.coefStaFe = coefStaFe;
		this.coefStaFeView =StringUtil.formatDouble(coefStaFe);
	}
	
	public Double getCoefRosario() {
		return coefRosario;
	}
	
	public void setCoefRosario(Double coefRosario) {
		this.coefRosario = coefRosario;
		this.coefRosarioView =StringUtil.formatDouble(coefRosario);
	}
	
	public Double getBaseStaFe() {
		return baseStaFe;
	}
	
	public void setBaseStaFe(Double baseStaFe) {
		this.baseStaFe = baseStaFe;
		this.baseStaFeView =NumberUtil.round(baseStaFe, SiatParam.DEC_IMPORTE_VIEW).toString();
	}

	public Double getBaseRosario() {
		return baseRosario;
	}

	public void setBaseRosario(Double baseRosario) {
		this.baseRosario = baseRosario;
		this.baseRosarioView =NumberUtil.round(baseRosario, SiatParam.DEC_IMPORTE_VIEW).toString();
	}
	
	public String getCol1BasRos() {
		return col1BasRos;
	}

	public void setCol1BasRos(String col1BasRos) {
		this.col1BasRos = col1BasRos;
	}

	public String getCol2BasRos() {
		return col2BasRos;
	}

	public void setCol2BasRos(String col2BasRos) {
		this.col2BasRos = col2BasRos;
	}

	public String getCol3BasRos() {
		return col3BasRos;
	}

	public void setCol3BasRos(String col3BasRos) {
		this.col3BasRos = col3BasRos;
	}

	public String getCol4BasRos() {
		return col4BasRos;
	}

	public void setCol4BasRos(String col4BasRos) {
		this.col4BasRos = col4BasRos;
	}

	public String getCol5BasRos() {
		return col5BasRos;
	}

	public void setCol5BasRos(String col5BasRos) {
		this.col5BasRos = col5BasRos;
	}

	public String getCol6BasRos() {
		return col6BasRos;
	}

	public void setCol6BasRos(String col6BasRos) {
		this.col6BasRos = col6BasRos;
	}

	public String getCol7BasRos() {
		return col7BasRos;
	}

	public void setCol7BasRos(String col7BasRos) {
		this.col7BasRos = col7BasRos;
	}

	public String getCol8BasRos() {
		return col8BasRos;
	}

	public void setCol8BasRos(String col8BasRos) {
		this.col8BasRos = col8BasRos;
	}

	public String getCol9BasRos() {
		return col9BasRos;
	}

	public void setCol9BasRos(String col9BasRos) {
		this.col9BasRos = col9BasRos;
	}

	public String getCol10BasRos() {
		return col10BasRos;
	}

	public void setCol10BasRos(String col10BasRos) {
		this.col10BasRos = col10BasRos;
	}

	public String getCol11BasRos() {
		return col11BasRos;
	}

	public void setCol11BasRos(String col11BasRos) {
		this.col11BasRos = col11BasRos;
	}

	public String getCol12BasRos() {
		return col12BasRos;
	}

	public void setCol12BasRos(String col12BasRos) {
		this.col12BasRos = col12BasRos;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	// View getters
	public String getCol1ForAjustes(){
		this.setNroCol(1);
		return getColForAjuste(1);
	}

	public String getCol2ForAjustes(){
		this.setNroCol(2);
		return getColForAjuste(2);
	}
	public String getCol3ForAjustes(){
		this.setNroCol(3);
		return getColForAjuste(3);
	}
	public String getCol4ForAjustes(){
		this.setNroCol(4);
		return getColForAjuste(4);
	}
	public String getCol5ForAjustes(){
		this.setNroCol(5);
		return getColForAjuste(5);
	}
	public String getCol6ForAjustes(){
		this.setNroCol(6);
		return getColForAjuste(6);
	}
	public String getCol7ForAjustes(){
		this.setNroCol(7);
		return getColForAjuste(7);
	}
	public String getCol8ForAjustes(){
		this.setNroCol(8);
		return getColForAjuste(8);
	}
	public String getCol9ForAjustes(){
		this.setNroCol(9);
		return getColForAjuste(9);
	}
	public String getCol10ForAjustes(){
		this.setNroCol(10);
		return getColForAjuste(10);
	}
	public String getCol11ForAjustes(){
		this.setNroCol(11);
		return getColForAjuste(11);
	}
	public String getCol12ForAjustes(){
		this.setNroCol(12);
		return getColForAjuste(12);
	}
	
	private String getColForAjuste(int nroCol){
	    this.setNroCol(nroCol);
		System.out.println("getColForAjuste-------------- "+this.getNroCol());
		switch (nroCol){
			case 1:{
				valorCol=getCol1();
				valorAjuste = (getAj1()!=null?getAj1():0D);break;
			}
			case 2:{
				valorCol=getCol2();
				valorAjuste = (getAj2()!=null?getAj2():0D);break;
			}
			case 3:{
				valorCol=getCol3();
				valorAjuste = (getAj3()!=null?getAj3():0D);break;
			}
			case 4:{
				valorCol=getCol4();
				valorAjuste = (getAj4()!=null?getAj4():0D);break;
			}
			case 5:{
				valorCol=getCol5();
				valorAjuste = (getAj5()!=null?getAj5():0D);break;
			}
			case 6:{
				valorCol=getCol6();
				valorAjuste = (getAj6()!=null?getAj6():0D);break;
			}
			case 7:{
				valorCol=getCol7();
				valorAjuste = (getAj7()!=null?getAj7():0D);break;
			}
			case 8:{
				valorCol=getCol8();
				valorAjuste = (getAj8()!=null?getAj8():0D);break;
			}
			case 9:{
				valorCol=getCol9();
				valorAjuste = (getAj9()!=null?getAj9():0D);break;
			}
			case 10:{
				valorCol=getCol10();
				valorAjuste = (getAj10()!=null?getAj10():0D);break;
			}
			case 11:{
				valorCol=getCol11();
				valorAjuste = (getAj11()!=null?getAj11():0D);break;
			}
			case 12:{
				valorCol=getCol12();
				valorAjuste = (getAj12()!=null?getAj12():0D);break;
			}
		}
		
		if(valorAjuste!=null && valorAjuste.doubleValue()!=0D){
			String ret = (NumberUtil.round(valorCol,SiatParam.DEC_IMPORTE_VIEW).toString())//+valorAjuste)
			+" ("+NumberUtil.round(valorCol,SiatParam.DEC_IMPORTE_VIEW).toString();
			
			if(valorAjuste>0)
				ret +="+";			
			
			ret+=NumberUtil.round(valorAjuste,SiatParam.DEC_IMPORTE_VIEW).toString()+")";
			return ret;
		}else
			return NumberUtil.round(valorCol,SiatParam.DEC_IMPORTE_VIEW).toString();
	}
	
	public Double getTotalAjustado(){
		return total+totalAjustes;
	}
	
	public Double getTotalAjustadoView(){
		return totalAjustado;
	}
	
	public void setTotalAjustadoView(Double totalAjustado){
		this.totalAjustado =valorCol+valorAjuste;
	}
	
	public Double getTotalAjustesView() {
		this.getColForAjuste(this.getNroCol());
		System.out.println("getTotalAjustesView-------------- "+this.getNroCol());
		return valorAjuste;
	}

	public void setTotalAjustesView(Double valorAjuste) {
		this.valorAjuste = valorAjuste;
	}
	
	public void setAnioView(String anioView) {
		this.anioView = anioView;
	}
	public String getAnioView() {
		return anioView;
	}

	public void setPeriodoView(String periodoView) {
		this.periodoView = periodoView;
	}
	public String getPeriodoView() {
		return periodoView;
	}

	public String getPeriodoAnioView(){
		return getPeriodoView()+"/"+getAnioView();
	}

	public String getTotalPaisView() {
		return totalPaisView;
	}

	public void setTotalPaisView(String totalPaisView) {
		this.totalPaisView = totalPaisView;
	}

	public String getCoefStaFeView() {
		return coefStaFeView;
	}

	public void setCoefStaFeView(String coefStaFeView) {
		this.coefStaFeView = coefStaFeView;
	}

	public String getCoefRosarioView() {
		return coefRosarioView;
	}

	public void setCoefRosarioView(String coefRosarioView) {
		this.coefRosarioView = coefRosarioView;
	}

	public String getBaseStaFeView() {
		return baseStaFeView;
	}

	public void setBaseStaFeView(String baseStaFeView) {
		this.baseStaFeView = baseStaFeView;
	}

	public String getBaseRosarioView() {
		return baseRosarioView;
	}

	public void setBaseRosarioView(String baseRosarioView) {
		this.baseRosarioView = baseRosarioView;
	}

	public Integer getNroCol() {
		return nroCol;
	}

	public void setNroCol(Integer nroCol) {
		this.nroCol = nroCol;
	}
	

}
