//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.rec.iface.model.CatRSDreiVO;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;


/**
 * 
 * Cuna 
 * sirve para obtener un CUNA en base a las posiciones pasadas como parametros
 * sirve para obtener un CUNA en base a los  
 * 
 * @author tecso
 */

public class Cuna  {
	private static final long serialVersionUID = 1L;
	
	//Constantes de error para WS
	
	// arreglos estaticos
	private static Double arr1[] = {};                   					// no se usa
	private static Double arr2[] = {0D, 0.5D, 0.75D };                   	// alic. reduccion habilitacion social
	private static Double arr3[] = {36000D, 72000D, 120000D, 216000D};   	// ingresos brutos
	private static Double arr4[] = {0D, 45D, 85D, 110D, 200D};           	// superficie
	private static Double arr5[] = {0D, 0.02D, 0.08D};                   	// alic. adicional
	private static Double arr6[] = {0D, 5D, 10D, 30D, 40D };              	// adicional etur
	
	private static Integer cantPers = 3;									// maximo cantidad de personal
	private static Double precioUnitario = 1300D;							// maximo precio unitario
	
	
	// variables de instancia
	private Integer codError=0;
	private Integer nroCategoria;
	private CatRSDreiVO catRSDrei;
	
	
	// datos para determina
	private Double alicRedHabSoc;
	private Double ingBruAnu;		
	private Double supHabil;
	private Double alicAdicional;
	private Double adicEtur;
	
	// otros datos para determinacion
	
	// posiciones CUNA
	private Integer pos1=0;
	private Integer pos2=0;
	private Integer pos3=0;
	private Integer pos4=0;
	private Integer pos5=0;
	private Integer pos6=0;
	private Integer dv=0;
	
	private Double importeDrei=0D;
	private Double importeAdicional=0D;
	private Double importeEtur=0D;
	
	
	private String detalleError;
	
	private String cuit;
	private String nroCuenta;
	
	//Constructores
	
	public Cuna(){
		super();
	}
	
	public Cuna(Integer codError){
		this.codError=codError;
	}
	

	/**
	 * Constructor por posicines
	 * 
	 */
	public Cuna(int pos1, int pos2, int pos3, int pos4, int pos5, int pos6, List<CatRSDreiVO> listCatRSDRei) {
		// digito verificador. consideramos que pos1 es pos2
		
		try {
			int suma = pos1*1+pos2*2+pos3*3+pos4*4+pos5*5+pos6*6;
			int dv = (suma-(suma/10)*10);
			
			//
			setPos1(pos1);
			setPos2(pos2);
			setPos3(pos3);
			setPos4(pos4);
			setPos5(pos5);
			setPos6(pos6);
			setDv(dv);
	
			// decremento los indices
			pos2--; pos3--; pos4--; pos5--; pos6--;
			
			// cargo las variables
			setAlicRedHabSoc(arr2[pos2]);
			setIngBruAnu(arr3[pos3]);
			setSupHabil(arr4[pos4]);
			setAlicAdicional(arr5[pos5]);
			setAdicEtur(arr6[pos6]);
			
			this.calcular(listCatRSDRei);
		
		} catch (Exception e) {
			setCodError(MRCategoriaRS.ERR_CUNA);
			setDetalleError(e.getMessage());
			
		}
		
	}
	
	
	/**
	 * constructor con un string
	 * @param cuna
	 * @param listCatRSDRei
	 */
	public Cuna(String cuna, List<CatRSDreiVO> listCatRSDRei) {
		// digito verificador. consideramos que pos1 es pos2
		
		try {
			
			// tiene que ser un string de 7 digitos
			if (cuna==null || cuna.length()<7) {
				setCodError(MRCategoriaRS.ERR_CUNA);
				setDetalleError("Error en creacion de CUMUR");
			}

			setPos1(new Integer(cuna.charAt(0)).intValue());;
			setPos2(new Integer(cuna.charAt(1)).intValue());;
			setPos3(new Integer(cuna.charAt(2)).intValue());;
			setPos4(new Integer(cuna.charAt(3)).intValue());;
			setPos5(new Integer(cuna.charAt(4)).intValue());;
			setPos6(new Integer(cuna.charAt(5)).intValue());;
			
			int suma = pos1*1+pos2*2+pos3*3+pos4*4+pos5*5+pos6*6;
			int dv = (suma-(suma/10)*10);

			if (new Integer(cuna.charAt(0)).intValue() == dv) {
				setDv(dv);
			} else {
				setCodError(MRCategoriaRS.ERR_CUNA);
				setDetalleError("Error en digito verificador de CUMUR");
			}
	
			// decremento los indices
			pos2--; pos3--; pos4--; pos5--; pos6--;
			
			// cargo las variables
			setAlicRedHabSoc(arr2[pos2]);
			setIngBruAnu(arr3[pos3]);
			setSupHabil(arr4[pos4]);
			setAlicAdicional(arr5[pos5]);
			setAdicEtur(arr6[pos6]);
			
			this.calcular(listCatRSDRei);
		
		} catch (Exception e) {
			setCodError(MRCategoriaRS.ERR_CUNA);
			setDetalleError(e.getMessage());
			
		}
		
	}
	
		

	/**
	 * Constructor por importes
	 * 
	 * @param alicRedHabSoc
	 * @param ingBruAnu
	 * @param supHabil
	 * @param alicAdicional
	 * @param pos6
	 */
	public Cuna(Integer cantPer, Double precioUnitario, Double alicRedHabSoc, Double ingBruAnu, Double supHabil, Double alicAdicional, Integer indice6, List<CatRSDreiVO> listCatRSDRei )  throws Exception{
		 
		try {
			
			if (cantPer==null || precioUnitario==null || alicRedHabSoc==null || ingBruAnu==null ||
					supHabil==null || alicAdicional==null || indice6==null) {
				setCodError(MRCategoriaRS.ERR_CUNA_NULOS);
				return;
			}
			
			if (cantPer>cantPers) {
				setCodError(MRCategoriaRS.ERR_CANTIDADPERSONAL);
				return;
			}
			
			if (precioUnitario > this.precioUnitario) {
				setCodError(MRCategoriaRS.ERR_PRECIOUNITARIO);
				return;
			}
			
			if (indice6 > arr6.length-1) {
				setCodError(MRCategoriaRS.ERR_ADICETURFUERARANGO);
				return;
			}
			
			
			// carga las variables
			setAlicRedHabSoc(alicRedHabSoc);
			setIngBruAnu(ingBruAnu);
			setSupHabil(supHabil);
			setAlicAdicional(alicAdicional);
			setAdicEtur(arr6[indice6]);
			
			
			// pos1: es siempre 0
			setPos1(0);
			
			// pos 2: alicRedHab: exacto
			for (int i=0; i<=arr2.length-1; i++) {
				if (alicRedHabSoc.doubleValue() == arr2[i].doubleValue()) {
					setPos2(i+1);
					break;
				}
			}
			
			// pos 3: ing. bru. anu: hasta
			for (int i=0; i<=arr3.length-1; i++) {
				if (ingBruAnu.doubleValue() <= arr3[i].doubleValue()) {
					setPos3(i+1);
					break;
				}
			}
			
			// pos 4: superficie habilitada: hasta
			for (int i=0; i<=arr4.length-1; i++) {
				if (supHabil.doubleValue() <= arr4[i].doubleValue()) {
					setPos4(i+1);
					break;
				}
			}
			
			// pos 5: alicuota adicionales
			for (int i=0; i<=arr5.length-1; i++) {
				if (alicAdicional.doubleValue() == arr5[i].doubleValue()) {
					setPos5(i+1);
					break;
				}
			}
			
			// pos 6: adicEtur
			for (int i=0; i<=arr6.length-1; i++) {
				if (adicEtur.doubleValue() == arr6[i].doubleValue()) {
					setPos6(i+1);
					break;
				}
			}
	

			//errores
			if (getPos2().intValue()==0)
				setCodError(MRCategoriaRS.ERR_ALICREDHABSOCFUERARANGO);

			else if (getPos3().intValue()==0)
				setCodError(MRCategoriaRS.ERR_INGRESOSBRUTOSFUERARANGO);

			else if (getPos4().intValue()==0)
				setCodError(MRCategoriaRS.ERR_SUPERFICIEFUERARANGO);
		
			else if (getPos5().intValue()==0)
				setCodError(MRCategoriaRS.ERR_ALICADICIONALFUERARANGO);
		
			else if (getPos6().intValue()==0)
				setCodError(MRCategoriaRS.ERR_ADICETURFUERARANGO);
			
			else 
				calcular(listCatRSDRei);
				
		} catch (Exception e) {
			setCodError(MRCategoriaRS.ERR_CUNA);
			setDetalleError(e.getMessage());
			
			
		}
		
	 }
	
		
	
	public Integer getDV() {
		int suma = pos1*1+pos2*2+pos3*3+pos4*4+pos5*5+pos6*6;
		int dv = (suma-(suma/10)*10);
		return new Integer(dv);
	}
		
	/**
	 * calcula los tres importes en funcio de los importes para determinacion que deben estar previamente cargados
	 * 
	 *  alicRedHabSoc
	 *  ingBruAnu
	 *  supHabil
	 *	alicAdicional
	 *	adicEtur
	 */
	private void calcular(List<CatRSDreiVO> listCatRSDRei) throws Exception {

		// obtengo la categoria segun el parametro ingresos brutos
		CatRSDreiVO catRSByIngBrutos=null;
		for (CatRSDreiVO catRSDrei: listCatRSDRei){
			if (catRSDrei.getIngBruAnu()>=ingBruAnu){
				catRSByIngBrutos=catRSDrei;
				break;
			}
		}

		// obtengo la categoria segun el parametro superficie afectada
		CatRSDreiVO catRSBySuperficie=null;
		for (CatRSDreiVO catRSDrei: listCatRSDRei){
			if (catRSDrei.getSuperficie() >= supHabil){
				catRSBySuperficie= catRSDrei;
				break;
			}
		}
		
		CatRSDreiVO categoriaATributar;
		// comparo y obtengo  la categoria mayor
		if (catRSByIngBrutos.getNroCategoria() >= catRSBySuperficie.getNroCategoria()){
			categoriaATributar=catRSByIngBrutos;
		}else{
			categoriaATributar=catRSBySuperficie;
		}
		
		// obtengo el importe de Drei
		Double importeDrei=categoriaATributar.getImporte();

		
		// obtengo el adicional
		Double importeAdicional=0D;
		if (alicAdicional!=null){
			importeAdicional = importeDrei * alicAdicional;
		}
		
		// importe de estur es lo que obtiene
		Double importeEtur=adicEtur;
		
		// aqui tenemos: importeDrei, importeAdicional e importeEtur
		// si corresponde la reduccion de la habilitacion social la aplicamos al importe de drei y a los adicionales
		if (!alicRedHabSoc.equals(0D) ) {
			importeDrei -= importeDrei * alicRedHabSoc;
			importeAdicional -= importeAdicional * alicRedHabSoc;
		} 
		
		// setea los impostes
		
		setCatRSDrei(categoriaATributar);
		setNroCategoria(categoriaATributar.getNroCategoria());
		setImporteDrei(importeDrei);
		setImporteAdicional(importeAdicional);
		setImporteEtur(importeEtur);
		
		// fin
		
	}
	

	public String getStrCuna() {
		return pos1.toString() + pos2.toString() + pos3.toString() + pos4.toString() + pos5.toString() + pos6.toString() + "-" + getDV().toString();
	}
	
	
	public String getStrCunaSinGuion() {
		return pos1.toString() + pos2.toString() + pos3.toString() + pos4.toString() + pos5.toString() + pos6.toString() + getDV().toString();
		
	}
	

	public String getCodBar() {
		String txtCodBar;
		
		if (cuit==null || nroCuenta==null || getStrCunaSinGuion()==null)
			return null;
		
		txtCodBar = "6057" + cuit + getStrCunaSinGuion() +  StringUtil.completarCerosIzq(nroCuenta,9);
		txtCodBar = StringUtil.agregaDVMod10AFIP(txtCodBar);
		
		if (txtCodBar.length()!=32)
			return null;
			
		return txtCodBar;
		
	}
	
	public String getCodBarComprimido() {
		return StringUtil.genCodBarComprimidoForAfip(this.getCodBar());
	}
	
	public String getLinea() {
		String txtCUNA;
		
		txtCUNA  = "6057" + "|" + pos1 + "|" + pos2 + "|" + pos3 + "|" + pos4 + "|" + pos5 + "|" + pos6 + "|" + getDV() + "|";
		txtCUNA += NumberUtil.truncate(alicRedHabSoc, 2) + "|" + NumberUtil.truncate(ingBruAnu,2) + "|" + NumberUtil.truncate(supHabil,2) + "|"; 
		txtCUNA += NumberUtil.truncate(importeDrei+importeAdicional,2) + "|";
		txtCUNA += NumberUtil.truncate(importeEtur,2) + "|";
		txtCUNA += NumberUtil.truncate(importeDrei+importeAdicional+importeEtur,2);
		txtCUNA += "\n";
		
		return txtCUNA;
	}
	
	
	public String getTablaParametro() {
		String txtCUNA;
		
		txtCUNA  = "AAAAMM" + "|" + pos1 + pos2 + pos3 + pos4 + pos5 + pos6 + getDV() + "|";
		txtCUNA += NumberUtil.truncate(importeDrei+importeAdicional,2) + "|";
		txtCUNA += NumberUtil.truncate(importeEtur,2);
		txtCUNA += "|,";
		
		return txtCUNA;
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
	
	public Double getImporteTotal() {
		Double total = 0D;
		if (this.importeDrei!=null)total+=this.importeDrei;
		if (this.importeAdicional!=null)total+=this.importeAdicional;
		if (this.importeEtur!=null)total+=this.importeEtur;
		return total;
	}
	
	public String getImporteTotalView(){
		Double total = 0D;
		if (this.importeDrei!=null)total+=this.importeDrei;
		if (this.importeAdicional!=null)total+=this.importeAdicional;
		if (this.importeEtur!=null)total+=this.importeEtur;
		
		return NumberUtil.round(total, SiatParam.DEC_IMPORTE_VIEW).toString();
	}

	public Double getAdicEtur() {
		return adicEtur;
	}

	public void setAdicEtur(Double adicEtur) {
		this.adicEtur = adicEtur;
	}

	public Double getAlicAdicional() {
		return alicAdicional;
	}

	public void setAlicAdicional(Double alicAdicional) {
		this.alicAdicional = alicAdicional;
	}

	public Double getAlicRedHabSoc() {
		return alicRedHabSoc;
	}

	public void setAlicRedHabSoc(Double alicRedHabSoc) {
		this.alicRedHabSoc = alicRedHabSoc;
	}

	public Integer getCodError() {
		return codError;
	}

	public void setCodError(Integer codError) {
		this.codError = codError;
	}

	public String getDetalleError() {
		return detalleError;
	}

	public void setDetalleError(String detalleError) {
		this.detalleError = detalleError;
	}

	public Integer getDv() {
		return dv;
	}

	public void setDv(Integer dv) {
		this.dv = dv;
	}

	public Double getImporteAdicional() {
		return importeAdicional;
	}

	public void setImporteAdicional(Double importeAdicional) {
		this.importeAdicional = importeAdicional;
	}

	public Double getImporteDrei() {
		return importeDrei;
	}

	public void setImporteDrei(Double importeDrei) {
		this.importeDrei = importeDrei;
	}

	public Double getImporteEtur() {
		return importeEtur;
	}

	public void setImporteEtur(Double importeEtur) {
		this.importeEtur = importeEtur;
	}

	public Double getIngBruAnu() {
		return ingBruAnu;
	}

	public void setIngBruAnu(Double ingBruAnu) {
		this.ingBruAnu = ingBruAnu;
	}

	public Integer getNroCategoria() {
		return nroCategoria;
	}

	public void setNroCategoria(Integer nroCategoria) {
		this.nroCategoria = nroCategoria;
	}

	public Integer getPos1() {
		return pos1;
	}

	public void setPos1(Integer pos1) {
		this.pos1 = pos1;
	}

	public Integer getPos2() {
		return pos2;
	}

	public void setPos2(Integer pos2) {
		this.pos2 = pos2;
	}

	public Integer getPos3() {
		return pos3;
	}

	public void setPos3(Integer pos3) {
		this.pos3 = pos3;
	}

	public Integer getPos4() {
		return pos4;
	}

	public void setPos4(Integer pos4) {
		this.pos4 = pos4;
	}

	public Integer getPos5() {
		return pos5;
	}

	public void setPos5(Integer pos5) {
		this.pos5 = pos5;
	}

	public Integer getPos6() {
		return pos6;
	}

	public void setPos6(Integer pos6) {
		this.pos6 = pos6;
	}

	public Double getSupHabil() {
		return supHabil;
	}

	public void setSupHabil(Double supHabil) {
		this.supHabil = supHabil;
	}

	public CatRSDreiVO getCatRSDrei() {
		return catRSDrei;
	}

	public void setCatRSDrei(CatRSDreiVO catRSDrei) {
		this.catRSDrei = catRSDrei;
	}
	
	
	
	
	public String getCuit() {
		return cuit;
	}

	public void setCuit(String cuit) {
		this.cuit = cuit;
	}

	public String getNroCuenta() {
		return nroCuenta;
	}

	public void setNroCuenta(String nroCuenta) {
		this.nroCuenta = nroCuenta;
	}

	public String getDesCategoria() {
		String ret="";
		String[] nroCat = {"I", "II", "III", "IV"};
		if (codError.intValue() == 0) {
			ret="DReI RS - Categoría " + nroCat[this.nroCategoria-1];
			if (this.alicRedHabSoc.doubleValue() != 0D) {
				ret+= " Hab. Soc. ( " + StringUtil.formatDouble(alicRedHabSoc*100, "##") + "% )"; 
			}
		}
		return ret;
	}

	
	public String getDesPublicidad() {
		String ret="";
		if (codError.intValue() == 0) {
			if (this.alicAdicional ==null || this.alicAdicional.doubleValue() == 0D)
				ret = "No Posee";
			else
				ret = StringUtil.formatDouble(this.alicAdicional * 100, "##") + "%";
		}
		return ret;
	}

	
	public String getDesEtur() {
		String ret="";
		String[] desEtur = {
				"No tributa",
				"Bar, Restaurante, Pizzería - Radio 2, 3 y 4",
				"Agencia de Turismo, Casas de cambio, Entidad financiera",
				"Bar, Restaurante, Pizzería - Radio 1 y 5",
				"Hotel, Hospedaje"};

		if (codError.intValue() == 0) {
			ret = desEtur[this.pos6-1];
		}
		return ret;
		
	}
	
}
