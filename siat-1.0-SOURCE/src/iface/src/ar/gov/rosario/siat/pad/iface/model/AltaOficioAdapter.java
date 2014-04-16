//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.TipObjImpVO;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del AltaOficio
 * 
 * @author tecso
 */
public class AltaOficioAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "altaOficioAdapterVO";
	
    private AltaOficioVO altaOficio = new AltaOficioVO();
    private ObjImpVO objImp = new ObjImpVO();
    private ContribuyenteVO contribuyente = new ContribuyenteVO();
    
    private TipObjImpVO tipObjImpComercio = new TipObjImpVO();
	private TipObjImpDefinition tipObjImpDefinition = new TipObjImpDefinition();
    private String fechaInicioActividadOriginal ="";	
	
    private List<SiNo> listSiNo = SiNo.getListSiNo(SiNo.NO);
    
    // Constructores
    public AltaOficioAdapter(){
    	super(EfSecurityConstants.ABM_ALTAOFICIO);
    }
    
    //  Getters y Setters
	public AltaOficioVO getAltaOficio() {
		return altaOficio;
	}

	public void setAltaOficio(AltaOficioVO altaOficioVO) {
		this.altaOficio = altaOficioVO;
	}


	public ObjImpVO getObjImp() {
		return objImp;
	}

	public void setObjImp(ObjImpVO objImp) {
		this.objImp = objImp;
	}

	public TipObjImpVO getTipObjImpComercio() {
		return tipObjImpComercio;
	}

	public void setTipObjImpComercio(TipObjImpVO tipObjImpComercio) {
		this.tipObjImpComercio = tipObjImpComercio;
	}

	public TipObjImpDefinition getTipObjImpDefinition() {
		return tipObjImpDefinition;
	}

	public void setTipObjImpDefinition(TipObjImpDefinition tipObjImpDefinition) {
		this.tipObjImpDefinition = tipObjImpDefinition;
	}

	public ContribuyenteVO getContribuyente() {
		return contribuyente;
	}

	public void setContribuyente(ContribuyenteVO contribuyente) {
		this.contribuyente = contribuyente;
	}

	public String getFechaInicioActividadOriginal() {
		return fechaInicioActividadOriginal;
	}

	public void setFechaInicioActividadOriginal(String fechaInicioActividadOriginal) {
		this.fechaInicioActividadOriginal = fechaInicioActividadOriginal;
	}

	/**
	 * Obtiene el valor del campo fechaInicioActividad de los atributos del comercio (Itera el definition)
	 * @return
	 */
	public Date getFechaInicioActividad(){
		for(TipObjImpAtrDefinition t:getTipObjImpDefinition().getListTipObjImpAtrDefinition()){
			if(t.getAtributo().getCodAtributo().equals("FechaInicio")) {
				Date fecha = DateUtil.getDate(t.getValorString(), DateUtil.YYYYMMDD_MASK);
				return fecha;
			}			
		}
		return null;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}

	
	
	// View getters
}
