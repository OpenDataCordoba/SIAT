//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import coop.tecso.demoda.iface.helper.NumberUtil;


/**
 * Bean correspondiente a NovedadRS
 * 
 * @author tecso
 */

public class CategoriaRSVO extends SiatBussImageModel {
	
	private static final long serialVersionUID = 1L;
	
	//Constantes de error para WS
	public static final int ERR_SINERROR=0;
	public static final int ERR_ERRORSERVICIO=1;

	public static final int ERR_DATOSENTRADANULOS= 5;
	
	public static final int ERR_NOEXISTECUENTA= 10;
	public static final int ERR_CONVENIOMULTILATERAL = 20;
	public static final int ERR_CONTRIBCUENTASMULTIPLES=30;
	public static final int ERR_CUENTAYATIENEALTARS=70;

	public static final int ERR_CUENTASINREGIMENSIMPLIFICADO=80;
	public static final int ERR_CANTIDADPERSONAL=40;
	public static final int ERR_CUENTACONINICIODECIERRE=90;
	
	public static final int ERR_MDF_NOTIENEALTA=92;
	
	// errores generados durante la creacion del CUNA
	public static final int ERR_INESPERADO=100;
	public static final int ERR_ALICREDHABSOCFUERARANGO=110;
	public static final int ERR_INGBRUTOSFUERARANGO=120;
	public static final int ERR_SUPERFICEFUERARANGO=130;
	public static final int ERR_ALICADICIONALFUERARANGO=140;
	public static final int ERR_ADICETURFUERARANGO=150;
	
	
	//<#Propiedades#>
	
	private Integer codError;
	private Integer nroCategoria;
	private Double importeDrei=0D;
	private Double importeAdicional=0D;
	private Double importeEtur=0D;
	private String datosCuenta;
	
	//Constructores
	
	public CategoriaRSVO(){
		super();
	}
	
	public CategoriaRSVO(Integer codError){
		this.codError=codError;
	}
	
	// Metodos de Clase
	
	
	// Getters y setters

	public Integer getCodError() {
		return codError;
	}

	public void setCodError(Integer codError) {
		this.codError = codError;
	}

	public Integer getNroCategoria() {
		return nroCategoria;
	}

	public void setNroCategoria(Integer nroCategoria) {
		this.nroCategoria = nroCategoria;
	}

	public Double getImporteDrei() {
		return importeDrei;
	}

	public void setImporteDrei(Double importeDrei) {
		this.importeDrei = importeDrei;
	}

	public Double getImporteAdicional() {
		return importeAdicional;
	}

	public void setImporteAdicional(Double importeAdicional) {
		this.importeAdicional = importeAdicional;
	}

	public Double getImporteEtur() {
		return importeEtur;
	}

	public void setImporteEtur(Double importeEtur) {
		this.importeEtur = importeEtur;
	}

	public String getDatosCuenta() {
		return datosCuenta;
	}

	public void setDatosCuenta(String datosCuenta) {
		this.datosCuenta = datosCuenta;
	}

	public String getImporteDreiView(){
		return (this.importeDrei!=null)?NumberUtil.round(this.importeDrei, SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}
	
	public String getImporteAdicionalView(){
		return (this.importeAdicional!=null)?NumberUtil.round(this.importeAdicional, SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}
	
	public String getImporteEturView(){
		return (this.importeEtur!=null)?NumberUtil.round(this.importeEtur, SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}
	
	public String getNroCategoriaView(){
		return (this.nroCategoria!=null)?nroCategoria.toString():"";
	}
	
	public String getImporteTotalView(){
		Double total = 0D;
		if (this.importeDrei!=null)total+=this.importeDrei;
		if (this.importeAdicional!=null)total+=this.importeAdicional;
		if (this.importeEtur!=null)total+=this.importeEtur;
		
		return NumberUtil.round(total, SiatParam.DEC_IMPORTE_VIEW).toString();
	}

	
}
