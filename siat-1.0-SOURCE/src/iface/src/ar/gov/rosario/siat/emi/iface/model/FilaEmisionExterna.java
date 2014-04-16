//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.model;


/**
 * Representa una fila del archivo que se utiliza para importar emisiones externas
 * @author alejandro
 *
 */
public class FilaEmisionExterna {

	public static String SEPARATOR = "\\|";
	
	private int nroLinea;
	
	private String numeroCuenta;
	private String codRecurso;
	private Integer anio;
	private Integer periodo;
	private Double importeConcepto1;
	//private Double importeConcepto2;
	//private Double importeConcepto3;
	//private Double importeConcepto4;

	private String leyenda;

	public FilaEmisionExterna(int nroLinea, 
							 String numeroCuenta,
							 String codRecurso,
							 Integer anio,
							 Integer periodo,
							 Double importeConcepto1,
							 String leyenda) {
		super();
		this.nroLinea = nroLinea;
		this.numeroCuenta = numeroCuenta;
		this.codRecurso = codRecurso;
		this.anio = anio;
		this.periodo = periodo;
		this.importeConcepto1 = importeConcepto1;
		//this.importeConcepto2 = importeConcepto2;
		//this.importeConcepto3 = importeConcepto3;
		//this.importeConcepto4 = importeConcepto4;
		this.leyenda = leyenda;
	}

	public int getNroLinea() {
		return nroLinea;
	}
	public void setNroLinea(int nroLinea) {
		this.nroLinea = nroLinea;
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public String getCodRecurso() {
		return codRecurso;
	}

	public void setCodRecurso(String codRecurso) {
		this.codRecurso = codRecurso;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public Integer getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}

	public Double getImporteConcepto1() {
		return importeConcepto1;
	}

	public void setImporteConcepto1(Double importeConcepto1) {
		this.importeConcepto1 = importeConcepto1;
	}

	/*public Double getImporteConcepto2() {
		return importeConcepto2;
	}

	public void setImporteConcepto2(Double importeConcepto2) {
		this.importeConcepto2 = importeConcepto2;
	}

	public Double getImporteConcepto3() {
		return importeConcepto3;
	}

	public void setImporteConcepto3(Double importeConcepto3) {
		this.importeConcepto3 = importeConcepto3;
	}

	public Double getImporteConcepto4() {
		return importeConcepto4;
	}

	public void setImporteConcepto4(Double importeConcepto4) {
		this.importeConcepto4 = importeConcepto4;
	}*/

	public String getLeyenda() {
		return leyenda;
	}

	public void setLeyenda(String leyenda) {
		this.leyenda = leyenda;
	}
	
	public Double getSumaConceptos(){
	
		Double suma = 0D;
		
		if (importeConcepto1 != null)
			suma += importeConcepto1;
		
		/*if (importeConcepto2 != null)
			suma += importeConcepto2;
		
		if (importeConcepto3 != null)
			suma += importeConcepto3;
		
		if (importeConcepto4 != null)
			suma += importeConcepto4;*/
		
		return suma;
		
	}
	
}
