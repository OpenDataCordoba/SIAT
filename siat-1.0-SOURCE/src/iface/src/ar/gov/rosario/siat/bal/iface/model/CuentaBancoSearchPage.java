//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import ar.gov.rosario.siat.def.iface.model.BancoVO;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del CuentaBanco
 * 
 * @author Tecso
 *
 */
public class CuentaBancoSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "cuentaBancoSearchPageVO";
	
	private CuentaBancoVO cuentaBanco= new CuentaBancoVO();
	private List<BancoVO> listBanco = new ArrayList<BancoVO>();
	private List<AreaVO> listArea = new ArrayList<AreaVO>();
	private List<TipCueBanVO> listTipCueBan = new ArrayList<TipCueBanVO>();

	
	// Constructores
	public CuentaBancoSearchPage() {       
       super(BalSecurityConstants.ABM_CUENTABANCO);        
    }
	
	// Getters y Setters
	public CuentaBancoVO getCuentaBanco() {
		return cuentaBanco;
	}
	public void setCuentaBanco(CuentaBancoVO cuentaBanco) {
		this.cuentaBanco = cuentaBanco;
	}

	public List<BancoVO> getListBanco() {
		return listBanco;
	}

	public void setListBanco(List<BancoVO> listBanco) {
		this.listBanco = listBanco;
	}

	public List<AreaVO> getListArea() {
		return listArea;
	}

	public void setListArea(List<AreaVO> listArea) {
		this.listArea = listArea;
	}

	public List<TipCueBanVO> getListTipCueBan() {
		return listTipCueBan;
	}

	public void setListTipCueBan(List<TipCueBanVO> listTipCueBan) {
		this.listTipCueBan = listTipCueBan;
	}
	
	 public String getName(){    
			return NAME;
		}
		
		public void prepareReport(Long format) {

			ReportVO report = this.getReport(); // no instanciar una nueva
			report.setReportFormat(format);	
			report.setReportTitle("Listados de Cuentas Bancarias");
			report.setReportBeanName("CuentaBanco");
			report.setReportFileName(this.getClass().getName());

			//Banco
			String desBanco = " ";

			BancoVO bancoVO = (BancoVO) ModelUtil.getBussImageModelByIdForList(
					this.getCuentaBanco().getBanco().getId(),
					this.getListBanco());
			if (bancoVO != null){
				desBanco = bancoVO.getDesBanco();
			}
			report.addReportFiltro("Banco", desBanco);
			
			//Area
			String desArea = " ";

			AreaVO areaVO = (AreaVO) ModelUtil.getBussImageModelByIdForList(
					this.getCuentaBanco().getArea().getId(),
					this.getListArea());
			if (areaVO != null){
				desArea = areaVO.getDesArea();
			}
			report.addReportFiltro("Area", desArea);
			
			//Tipo Cuenta Banco
			String desTipCueBan = " ";

			TipCueBanVO tipCueBanVO = (TipCueBanVO) ModelUtil.getBussImageModelByIdForList(
					this.getCuentaBanco().getTipCueBan().getId(),
					this.getListTipCueBan());
			if (tipCueBanVO != null){
				desTipCueBan = tipCueBanVO.getDescripcion();
			}
			report.addReportFiltro("Tipo Cuenta Banco", desTipCueBan);

			ReportTableVO rtCuentaBanco = new ReportTableVO("rtCuentaBanco");
			rtCuentaBanco.setTitulo("B\u00FAsqueda de CuentaBanco");

			// carga de columnas
			rtCuentaBanco.addReportColumn("Nro. Cuenta","nroCuenta");
			rtCuentaBanco.addReportColumn("Tipo Cuenta Banco", "tipCueBan.descripcion");
			rtCuentaBanco.addReportColumn("Banco", "banco.desBanco");
			rtCuentaBanco.addReportColumn("Area", "area.desArea");
			rtCuentaBanco.addReportColumn("Observaciones", "observaciones");
			 
		    report.getReportListTable().add(rtCuentaBanco);

		}

	// View getters
}
