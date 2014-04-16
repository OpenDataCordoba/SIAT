//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.ServicioBancoVO;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del ProPreDeu	
 *
 * @author tecso
 */
public class ProPreDeuAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "proPreDeuAdapterVO";
	
    private ProPreDeuVO proPreDeu = new ProPreDeuVO();
    private List<ViaDeudaVO> listViaDeuda = new ArrayList<ViaDeudaVO>();
    private List<ServicioBancoVO> listServicioBanco = new ArrayList<ServicioBancoVO>();

    // Constructores
    public ProPreDeuAdapter(){
    	super(GdeSecurityConstants.ABM_PROPREDEU);
    }
    
    //  Getters y Setters
	public ProPreDeuVO getProPreDeu() {
		return proPreDeu;
	}

	public void setProPreDeu(ProPreDeuVO proPreDeuVO) {
		this.proPreDeu = proPreDeuVO;
	}

	public List<ViaDeudaVO> getListViaDeuda() {
		return listViaDeuda;
	}

	public void setListViaDeuda(List<ViaDeudaVO> listViaDeuda) {
		this.listViaDeuda = listViaDeuda;
	}

	public List<ServicioBancoVO> getListServicioBanco() {
		return listServicioBanco;
	}

	public void setListServicioBanco(List<ServicioBancoVO> listServicioBanco) {
		this.listServicioBanco = listServicioBanco;
	}

	public String getName() {
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte del Proceso de Prescripci\u00F3n de Dueda");     
		 report.setReportBeanName("ProPreDeu");
		 report.setReportFileName(this.getClass().getName());
		 
		 ReportVO reportProPreDeu = new ReportVO();
		 reportProPreDeu.setReportTitle("Datos del Proceso de Prescripci\u00F3n de Dueda");
		 
		 // Via Deuda
		 reportProPreDeu.addReportDato("V\u00EDa Deuda","viaDeuda.desViaDeuda");
		 // Servicio Banco
		 reportProPreDeu.addReportDato("Servicio Banco","servicioBanco.desServicioBanco");
		 // Fecha Tope
		 reportProPreDeu.addReportDato("Fecha Tope","fechaTope");

		 report.getListReport().add(reportProPreDeu);
	
	}
	// View getters
}
