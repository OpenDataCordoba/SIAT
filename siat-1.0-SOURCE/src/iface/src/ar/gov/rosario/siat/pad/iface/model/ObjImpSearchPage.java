//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.TipObjImpVO;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Datum;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;


/**
 * Paginador de Objetos imponibles
 *  
 * @author Tecso Coop. Ltda.
 */
public class ObjImpSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;
    
	//Tipo objeto imponible

	private static final long Parcela=1L;
	private static final long Comercio=2L;
	
	public static String NAME = "objImpSearchPageVO";

	// Si hay valor para este parametro entonces el combo de tipObjImp se pone con este valor y en readonly
	public static final String PARAM_TIPOBJIMP_READONLY = "paramTipObjImpReadOnly";

	private TipObjImpVO paramTipObjImpReadOnly = null;
	
	private ObjImpVO objImp = new ObjImpVO();

	private List<TipObjImpVO> listTipObjImp = new ArrayList<TipObjImpVO>();
	private TipObjImpDefinition tipObjImpDefinition = new TipObjImpDefinition();
	
	private boolean esBusquedaAvanzada = false;
		
	public ObjImpSearchPage() {       
	       super(PadSecurityConstants.ABM_OBJIMP);        
	}    

	/**
	 * Contenedor de Atributos y Valores del Objeto imponible
	 * @return the defTipObjImp
	 */
	public TipObjImpDefinition getTipObjImpDefinition() {
		return tipObjImpDefinition;
	}
	/**
	 * @param tipObjImpDefinition the defTipObjImp to set
	 */
	public void setTipObjImpDefinition(TipObjImpDefinition tipObjImpDefinition) {
		this.tipObjImpDefinition = tipObjImpDefinition;
	}

	/**
	 * Lista de Tipos de Objetos Imponibles para el combo de filtros
	 * @return the listTipObjImp
	 */
	public List<TipObjImpVO> getListTipObjImp() {
		return listTipObjImp;
	}
	/**
	 * @param listTipObjImp the listTipObjImp to set
	 */
	public void setListTipObjImp(List<TipObjImpVO> listTipObjImp) {
		this.listTipObjImp = listTipObjImp;
	}

	/**
	 * Objeto imponible para valores de filtros del objeto imponible
	 * @return the objImp
	 */
	public ObjImpVO getObjImp() {
		return objImp;
	}
	/**
	 * @param objImp the objImp to set
	 */
	public void setObjImp(ObjImpVO objImp) {
		this.objImp = objImp;
	}

	public TipObjImpVO getParamTipObjImpReadOnly() {
		return paramTipObjImpReadOnly;
	}

	public void setParamTipObjImpReadOnly(TipObjImpVO paramTipObjImpReadOnly) {
		this.paramTipObjImpReadOnly = paramTipObjImpReadOnly;
	}

	public boolean getEsBusquedaAvanzada() {
		return esBusquedaAvanzada;
	}

	public void setEsBusquedaAvanzada(boolean esBusquedaAvanzada) {
		this.esBusquedaAvanzada = esBusquedaAvanzada;
	}
	

	public String getClaveFuncional(){
		String claveFuncional = this.getObjImp().getClaveFuncional();
		if(this.getObjImp().getTipObjImp().getId().longValue() == TipObjImpVO.ID_TIPOBJIMP_PARCELA){
			// La formateamos completando con ceros a la izquierda en cada una de 
			// sus partes (seccion/manzana/grafico/subdivision/subparcela)
			Datum datum = Datum.parseAtChar(claveFuncional, '/');
			String[] catastral = new String[datum.getColNumMax()]; 
			catastral[0] = StringUtil.completarCerosIzq(datum.getCols(0), 2);
			for(int i=1;i<datum.getColNumMax();i++)
				catastral[i] = StringUtil.completarCerosIzq(datum.getCols(i), 3);
			claveFuncional = "";
			for(int i=0;i<datum.getColNumMax()-1;i++)
				claveFuncional += catastral[i]+"/";		
			claveFuncional += catastral[datum.getColNumMax()-1];
		}
		
		return claveFuncional;
	}
	
	/**
	 * Sobrecarga utilizada para loguear cuando se realiza una busqueda avanzada 
	 */
	public String infoString() {
		try {
			String infoString = "";

			if (getEsBusquedaAvanzada()){
				for (TipObjImpAtrDefinition tipObjImpAtrDefinition: this.getTipObjImpDefinition().getListTipObjImpAtrDefinition()){
					infoString += " \n Atributo: " + tipObjImpAtrDefinition.getAtributo().getCodAtributo() + 
							" ## valor: " + tipObjImpAtrDefinition.getValorString() +
							" ## tipo dato: " + tipObjImpAtrDefinition.getAtributo().getTipoAtributo().getDesTipoAtributo() +
							" ## posee dominio: " + tipObjImpAtrDefinition.getPoseeDominio() +
							" ## adm bus rango: " + tipObjImpAtrDefinition.getAdmBusPorRan();
				}
				
			} else{
				infoString = super.infoString();
			}

			return infoString;			
        } catch (Exception e) {
			return this.toString();
        }

	}
	
	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Consulta de Objetos Imponibles");
		 report.setReportBeanName("objImp");
		 report.setReportFileName(this.getClass().getName());
	    
		 //	carga de filtros
		 report.addReportFiltro("Tipo Objeto Imponible", this.getObjImp().getTipObjImp().getDesTipObjImp());
		 report.addReportFiltro("Catastral", this.getObjImp().getDesClaveFuncional());
		 
		  ReportTableVO rtObj = new ReportTableVO("rtObj");
		  rtObj.setTitulo("Listado de Objetos Imponibles");
		  
		 if (this.getObjImp().getTipObjImp().getId()==Parcela){ 
			 //parcela
      
			
			     // carga de columnas
			 rtObj.addReportColumn("Catastral", "claveFuncional");
			 rtObj.addReportColumn("Número de Cuenta", "clave");
			 rtObj.addReportColumn("Vigencia", "desVigencia");
			 rtObj.addReportColumn("Fecha Alta", "fechaAlta");
			 rtObj.addReportColumn("Fecha Baja", "fechaBaja");
			 
		 } 
		
		 if (this.getObjImp().getTipObjImp().getId()==Comercio) {
			 //Comercio
			
			 // carga de columnas
			 rtObj.addReportColumn("Número de Comercio", "claveFuncional");
		     rtObj.addReportColumn("Número de Cuenta", "clave");
		     rtObj.addReportColumn("Vigencia", "desVigencia");
		     rtObj.addReportColumn("Fecha Alta", "fechaAlta");
		     rtObj.addReportColumn("Fecha Baja", "fechaBaja");
		     report.getReportListTable().add(rtObj);
		       }
		    report.getReportListTable().add(rtObj);

	    }
}
