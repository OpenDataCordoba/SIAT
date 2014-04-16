//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del PlaFueDatDet
 * @author tecso
 *
 */
public class PlaFueDatDetVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "plaFueDatDetVO";
	
	private PlaFueDatVO plaFueDat;

    private Integer periodo;

    private Integer anio;

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
	
    private Double total = 0D;
    
    
	// Buss Flags
	
	
	// View Constants
	
	
	private String anioView = "";
	private String col1View = "";
	private String col10View = "";
	private String col11View = "";
	private String col12View = "";
	private String col2View = "";
	private String col3View = "";
	private String col4View = "";
	private String col5View = "";
	private String col6View = "";
	private String col7View = "";
	private String col8View = "";
	private String col9View = "";
	private String periodoView = "";
	private String totalView = "";


	// Constructores
	public PlaFueDatDetVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public PlaFueDatDetVO(int id, String desc) {
		super();
		setId(new Long(id));		
	}
	
	// Getters y Setters

	public PlaFueDatVO getPlaFueDat() {
		return plaFueDat;
	}

	public void setPlaFueDat(PlaFueDatVO plaFueDat) {
		this.plaFueDat = plaFueDat;
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
		this.col1View = StringUtil.formatDouble(col1);
	}

	public Double getCol2() {
		return col2;
	}

	public void setCol2(Double col2) {
		this.col2 = col2;
		this.col2View = StringUtil.formatDouble(col2);
	}

	public Double getCol3() {
		return col3;
	}

	public void setCol3(Double col3) {
		this.col3 = col3;
		this.col3View = StringUtil.formatDouble(col3);
	}

	public Double getCol4() {
		return col4;
	}

	public void setCol4(Double col4) {
		this.col4 = col4;
		this.col4View = StringUtil.formatDouble(col4);
	}

	public Double getCol5() {
		return col5;
	}

	public void setCol5(Double col5) {
		this.col5 = col5;
		this.col5View = StringUtil.formatDouble(col5);
	}

	public Double getCol6() {
		return col6;
	}

	public void setCol6(Double col6) {
		this.col6 = col6;
		this.col6View = StringUtil.formatDouble(col6);
	}

	public Double getCol7() {
		return col7;
	}

	public void setCol7(Double col7) {
		this.col7 = col7;
		this.col7View = StringUtil.formatDouble(col7);
	}

	public Double getCol8() {
		return col8;
	}

	public void setCol8(Double col8) {
		this.col8 = col8;
		this.col8View = StringUtil.formatDouble(col8);
	}

	public Double getCol9() {
		return col9;
	}

	public void setCol9(Double col9) {
		this.col9 = col9;
		this.col9View = StringUtil.formatDouble(col9);
	}

	public Double getCol10() {
		return col10;
	}

	public void setCol10(Double col10) {
		this.col10 = col10;
		this.col1View = StringUtil.formatDouble(col1);
	}

	public Double getCol11() {
		return col11;
	}

	public void setCol11(Double col11) {
		this.col11 = col11;
		this.col1View = StringUtil.formatDouble(col1);
	}

	public Double getCol12() {
		return col12;
	}

	public void setCol12(Double col12) {
		this.col12 = col12;
		this.col1View = StringUtil.formatDouble(col1);
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}
	
	public String getTotalView() {
		return (total!=null?StringUtil.redondearDecimales(total, 1, 2):"");
	}

	public void setTotalView(Double total) {
       this.totalView = StringUtil.formatDouble(total);
	}
	// Buss flags getters y setters
	
	
	// View flags getters
	// View getters
	public void setAnioView(String anioView) {
		this.anioView = anioView;
	}
	public String getAnioView() {
		return anioView;
	}

	public void setCol1View(String col1View) {
		this.col1View = col1View;
	}
	
	public String getCol1View(){
          return (col1!=null?StringUtil.redondearDecimales(col1, 1, 2):"");
    }
	public void setCol10View(String col10View) {
		this.col10View = col10View;
	}
	public String getCol10View(){
        return (col10!=null?StringUtil.redondearDecimales(col10, 1, 2):"");
  }

	public void setCol11View(String col11View) {
		this.col11View = col11View;
	}
	public String getCol11View(){
        return (col11!=null?StringUtil.redondearDecimales(col11, 1, 2):"");
  }

	public void setCol12View(String col12View) {
		this.col12View = col12View;
	}
	public String getCol12View(){
        return (col12!=null?StringUtil.redondearDecimales(col12, 1, 2):"");
  }

	public void setCol2View(String col2View) {
		this.col2View = col2View;
	}
	public String getCol2View(){
        return (col2!=null?StringUtil.redondearDecimales(col2, 1, 2):"");
  }

	public void setCol3View(String col3View) {
		this.col3View = col3View;
	}
	public String getCol3View(){
        return (col3!=null?StringUtil.redondearDecimales(col3, 1, 2):"");
  }

	public void setCol4View(String col4View) {
		this.col4View = col4View;
	}
	public String getCol4View(){
        return (col4!=null?StringUtil.redondearDecimales(col4, 1, 2):"");
  }

	public void setCol5View(String col5View) {
		this.col5View = col5View;
	}
	public String getCol5View(){
        return (col5!=null?StringUtil.redondearDecimales(col5, 1, 2):"");
  }

	public void setCol6View(String col6View) {
		this.col6View = col6View;
	}
	public String getCol6View(){
        return (col6!=null?StringUtil.redondearDecimales(col6, 1, 2):"");
  }

	public void setCol7View(String col7View) {
		this.col7View = col7View;
	}
	public String getCol7View(){
        return (col7!=null?StringUtil.redondearDecimales(col7, 1, 2):"");
  }

	public void setCol8View(String col8View) {
		this.col8View = col8View;
	}
	public String getCol8View(){
        return (col8!=null?StringUtil.redondearDecimales(col8, 1, 2):"");
	  }

	public void setCol9View(String col9View) {
		this.col9View = col9View;
	}
	public String getCol9View(){
        return (col9!=null?StringUtil.redondearDecimales(col9, 1, 2):"");
  }
	public void setPeriodoView(String periodoView) {
		this.periodoView = periodoView;
	}
	public String getPeriodoView() {
		return periodoView;
	}

}
