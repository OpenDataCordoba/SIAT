package ar.gov.rosario.siat.${modulo}.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.${modulo}.iface.util.${Modulo}SecurityConstants;

/**
 * SearchPage del ${Bean}
 * 
 * @author Tecso
 *
 */
public class ${Bean}SearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "${bean}SearchPageVO";
	
	private ${Bean}VO ${bean}= new ${Bean}VO();
	
	// Constructores
	public ${Bean}SearchPage() {       
       super(${Modulo}SecurityConstants.ABM_${BEAN});        
    }
	
	// Getters y Setters
	public ${Bean}VO get${Bean}() {
		return ${bean};
	}
	public void set${Bean}(${Bean}VO ${bean}) {
		this.${bean} = ${bean};
	}           

    public String getName(){    
		return NAME;
	}
	
	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Listados de ${Bean}");
		report.setReportBeanName("${Bean}");
		report.setReportFileName(this.getClass().getName());

        /* Codigo de ejemplo para mostrar filtros de Combos en los imprimir
		String desRecurso = "";

		RecursoVO recursoVO = (RecursoVO) ModelUtil.getBussImageModelByIdForList(
				this.getReclamo().getRecurso().getId(),
				this.getListRecurso());
		if (recursoVO != null){
			desRecurso = recursoVO.getDesRecurso();
		}
		report.addReportFiltro("Recurso", desRecurso);*/

		//C�digo
		report.addReportFiltro("C�digo", this.get${Bean}().getCod${Bean}());
       //Descripci�n
		report.addReportFiltro("Descripci�n", this.get${Bean}().getDes${Bean}());
		

		ReportTableVO rt${Bean} = new ReportTableVO("rt${Bean}");
		rt${Bean}.setTitulo("B\u00FAsqueda de ${Bean}");

		// carga de columnas
		rt${Bean}.addReportColumn("C�digo","cod${Bean}");
		rt${Bean}.addReportColumn("Descripci�n", "des${Bean}");
		
		 
	    report.getReportListTable().add(rt${Bean});

	}
	// View getters
}
