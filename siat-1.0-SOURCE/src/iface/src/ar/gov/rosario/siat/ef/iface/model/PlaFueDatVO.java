//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;


/**
 * Value Object del PlaFueDat
 * @author tecso
 *
 */
public class PlaFueDatVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "plaFueDatVO";
	
	private Logger log = Logger.getLogger(PlaFueDatVO.class);
	
	private OrdenControlVO ordenControl;
	
	private FuenteInfoVO fuenteInfo = new FuenteInfoVO();
	
    private Date fecDesProm;
	
    private Date fecHasProm;
    
    private String observacion="";
	
    private List<PlaFueDatColVO> listPlaFueDatCol = new ArrayList<PlaFueDatColVO>();
    
    private List<PlaFueDatDetVO> listPlaFueDatDet = new ArrayList<PlaFueDatDetVO>();
    
    private Double totalCol1=null;
    
    private Double totalCol2=null;
    
    private Double totalCol3=null;
    
    private Double totalCol4=null;
    
    private Double totalCol5=null;
    
    private Double totalCol6=null;
    
    private Double totalCol7=null;
    
    private Double totalCol8=null;
    
    private Double totalCol9=null;
    
    private Double totalCol10=null;
    
    private Double totalCol11=null;
    
    private Double totalCol12=null;
	// Buss Flags
	
	
	// View Constants
	
	
	private String fecDesPromView = "";
	private String fecHasPromView = "";


	// Constructores
	public PlaFueDatVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public PlaFueDatVO(int id, String desc) {
		super();
		setId(new Long(id));
	}
	
	// Getters y Setters
	public OrdenControlVO getOrdenControl() {
		return ordenControl;
	}

	public void setOrdenControl(OrdenControlVO ordenControl) {
		this.ordenControl = ordenControl;
	}

	public FuenteInfoVO getFuenteInfo() {
		return fuenteInfo;
	}

	public void setFuenteInfo(FuenteInfoVO fuenteInfo) {
		this.fuenteInfo = fuenteInfo;
	}

	public Date getFecDesProm() {
		return fecDesProm;
	}

	public void setFecDesProm(Date fecDesProm) {
		this.fecDesProm = fecDesProm;
		this.fecDesPromView = DateUtil.formatDate(fecDesProm, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFecHasProm() {
		return fecHasProm;
	}

	public void setFecHasProm(Date fecHasProm) {
		this.fecHasProm = fecHasProm;
		this.fecHasPromView = DateUtil.formatDate(fecHasProm, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public List<PlaFueDatColVO> getListPlaFueDatCol() {
		return listPlaFueDatCol;
	}

	public void setListPlaFueDatCol(List<PlaFueDatColVO> listPlaFueDatCol) {
		this.listPlaFueDatCol = listPlaFueDatCol;
	}


	public List<PlaFueDatDetVO> getListPlaFueDatDet() {
		return listPlaFueDatDet;
	}

	public void setListPlaFueDatDet(List<PlaFueDatDetVO> listPlaFueDatDet) {
		this.listPlaFueDatDet = listPlaFueDatDet;
	}
	
	

	public Double getTotalCol1() {
		return totalCol1;
	}

	public void setTotalCol1(Double totalCol1) {
		this.totalCol1 = totalCol1;
	}

	public Double getTotalCol2() {
		return totalCol2;
	}

	public void setTotalCol2(Double totalCol2) {
		this.totalCol2 = totalCol2;
	}

	public Double getTotalCol3() {
		return totalCol3;
	}

	public void setTotalCol3(Double totalCol3) {
		this.totalCol3 = totalCol3;
	}

	public Double getTotalCol4() {
		return totalCol4;
	}

	public void setTotalCol4(Double totalCol4) {
		this.totalCol4 = totalCol4;
	}

	public Double getTotalCol5() {
		return totalCol5;
	}

	public void setTotalCol5(Double totalCol5) {
		this.totalCol5 = totalCol5;
	}

	public Double getTotalCol6() {
		return totalCol6;
	}

	public void setTotalCol6(Double totalCol6) {
		this.totalCol6 = totalCol6;
	}

	public Double getTotalCol7() {
		return totalCol7;
	}

	public void setTotalCol7(Double totalCol7) {
		this.totalCol7 = totalCol7;
	}

	public Double getTotalCol8() {
		return totalCol8;
	}

	public void setTotalCol8(Double totalCol8) {
		this.totalCol8 = totalCol8;
	}

	public Double getTotalCol9() {
		return totalCol9;
	}

	public void setTotalCol9(Double totalCol9) {
		this.totalCol9 = totalCol9;
	}

	public Double getTotalCol10() {
		return totalCol10;
	}

	public void setTotalCol10(Double totalCol10) {
		this.totalCol10 = totalCol10;
	}

	public Double getTotalCol11() {
		return totalCol11;
	}

	public void setTotalCol11(Double totalCol11) {
		this.totalCol11 = totalCol11;
	}

	public Double getTotalCol12() {
		return totalCol12;
	}

	public void setTotalCol12(Double totalCol12) {
		this.totalCol12 = totalCol12;
	}
	
	// Buss flags getters y setters
	
	
	// View flags getters

	// View getters
	public void setFecDesPromView(String fecDesPromView) {
		this.fecDesPromView = fecDesPromView;
	}
	public String getFecDesPromView() {
		return fecDesPromView;
	}

	public void setFecHasPromView(String fecHasPromView) {
		this.fecHasPromView = fecHasPromView;
	}
	public String getFecHasPromView() {
		return fecHasPromView;
	}

	public String getCantPlaFueDatCol(){		
		return String.valueOf(listPlaFueDatCol.size());
	}
	
	public String getCantPlaFueDatColVisible(){
		
		int cant = 0;
		for(PlaFueDatColVO plaFueDatColVO: listPlaFueDatCol){
			if(plaFueDatColVO.getOculta().equals(SiNo.NO))
				cant++;
		}
		
		return String.valueOf(cant);
	}

	public String getCantPlaFueDatDet(){
		return String.valueOf(listPlaFueDatDet.size());
	}

	public Double getTotal(){
		Double total = 0D;
		try{
			if(listPlaFueDatDet!=null && listPlaFueDatDet.size()>0){
				for(PlaFueDatDetVO plaFueDatDetVO: listPlaFueDatDet){
					total += plaFueDatDetVO.getTotal();
				}
			}
			
		}catch(Exception e){
			System.out.print("----------------------------------"+e);
		}
		return total;
	}

	public String getTituloView(){
		return observacion+
			(!StringUtil.isNullOrEmpty(fuenteInfo.getNombreFuente())?"("+fuenteInfo.getNombreFuente()+")":"");
	}
	
	public String getTotalCol1View(){
		this.getTotal();
		return (this.totalCol1!=null)? StringUtil.redondearDecimales(totalCol1, 1, 2):null;
	}
	
	public String getTotalCol2View(){
		return (this.totalCol2!=null)? StringUtil.redondearDecimales(totalCol2, 1, 2):null;
	}
	
	public String getTotalCol3View(){
		return (this.totalCol3!=null)? StringUtil.redondearDecimales(totalCol3, 1, 2):null;
	}
	
	public String getTotalCol4View(){
		return (this.totalCol4!=null)? StringUtil.redondearDecimales(totalCol4, 1, 2):null;
	}
	
	public String getTotalCol5View(){
		return (this.totalCol5!=null)? StringUtil.redondearDecimales(totalCol5, 1, 2):null;
	}
	
	public String getTotalCol6View(){
		return (this.totalCol6!=null)? StringUtil.redondearDecimales(totalCol6, 1, 2):null;
	}
	
	public String getTotalCol7View(){
		return (this.totalCol7!=null)? StringUtil.redondearDecimales(totalCol7, 1, 2):null;
	}
	
	public String getTotalCol8View(){
		return (this.totalCol8!=null)? StringUtil.redondearDecimales(totalCol8, 1, 2):null;
	}
	
	public String getTotalCol9View(){
		return (this.totalCol9!=null)? StringUtil.redondearDecimales(totalCol9, 1, 2):null;
	}
	
	public String getTotalCol10View(){
		return (this.totalCol10!=null)? StringUtil.redondearDecimales(totalCol10, 1, 2):null;
	}
	
	public String getTotalCol11View(){
		return (this.totalCol11!=null)? StringUtil.redondearDecimales(totalCol11, 1, 2):null;
	}
	
	public String getTotalCol12View(){
		return (this.totalCol12!=null)? StringUtil.redondearDecimales(totalCol12, 1, 2):null;
	}
	
	public void calcularTotales(){
		if(listPlaFueDatDet!=null && listPlaFueDatDet.size()>0){
			for(PlaFueDatDetVO plaFueDatDetVO: listPlaFueDatDet){
				if(plaFueDatDetVO.getCol1()!=null){
					if (totalCol1==null)totalCol1=plaFueDatDetVO.getCol1();else totalCol1+=plaFueDatDetVO.getCol1();
				}
				if(plaFueDatDetVO.getCol2()!=null){
					if (totalCol2==null)totalCol2=plaFueDatDetVO.getCol2();else totalCol2+=plaFueDatDetVO.getCol2();
				}
				if(plaFueDatDetVO.getCol3()!=null){
					if (totalCol3==null)totalCol3=plaFueDatDetVO.getCol3();else totalCol3+=plaFueDatDetVO.getCol3();
				}
				if(plaFueDatDetVO.getCol4()!=null){
					if (totalCol4==null)totalCol4=plaFueDatDetVO.getCol4();else totalCol4+=plaFueDatDetVO.getCol4();
				}
				if(plaFueDatDetVO.getCol5()!=null){
					if (totalCol5==null)totalCol5=plaFueDatDetVO.getCol5();else totalCol5+=plaFueDatDetVO.getCol5();
				}
				if(plaFueDatDetVO.getCol6()!=null){
					if (totalCol6==null)totalCol6=plaFueDatDetVO.getCol6();else totalCol6+=plaFueDatDetVO.getCol6();
				}
				if(plaFueDatDetVO.getCol7()!=null){
					if (totalCol7==null)totalCol7=plaFueDatDetVO.getCol7();else totalCol7+=plaFueDatDetVO.getCol7();
				}
				if(plaFueDatDetVO.getCol8()!=null){
					if (totalCol8==null)totalCol8=plaFueDatDetVO.getCol8();else totalCol8+=plaFueDatDetVO.getCol8();
				}
				if(plaFueDatDetVO.getCol9()!=null){
					if (totalCol9==null)totalCol9=plaFueDatDetVO.getCol9();else totalCol9+=plaFueDatDetVO.getCol9();
				}
				if(plaFueDatDetVO.getCol10()!=null){
					if (totalCol10==null)totalCol10=plaFueDatDetVO.getCol10();else totalCol10+=plaFueDatDetVO.getCol10();
				}
				if(plaFueDatDetVO.getCol11()!=null){
					if (totalCol11==null)totalCol11=plaFueDatDetVO.getCol11();else totalCol11+=plaFueDatDetVO.getCol11();
				}
				if(plaFueDatDetVO.getCol12()!=null){
					if (totalCol12==null)totalCol12=plaFueDatDetVO.getCol12();else totalCol12+=plaFueDatDetVO.getCol12();
				}
				log.debug("Valores Totales: "+totalCol1+", "+totalCol2+", "+totalCol3+", "+totalCol4+", "+totalCol5+", ");
			}
		}
	}
}
