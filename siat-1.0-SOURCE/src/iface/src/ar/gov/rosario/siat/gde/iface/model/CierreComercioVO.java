//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.cas.iface.model.CasoContainer;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;


/**
 * Value Object del CierreComercio
 * @author tecso
 *
 */
public class CierreComercioVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "cierreComercioVO";
	
	private MultaVO multa = new MultaVO();
	private CasoVO caso = new CasoVO();
	private CasoContainer casoNoEmiMul= new CasoContainer();
	private Date fechaCeseActividad;
	private Date fechaTramite;
	private MotivoCierreVO motivoCierre=new MotivoCierreVO();
	private Date fechaFallecimiento;
	private Date fechaCierreDef;

	private CuentaVO cuentaVO = new CuentaVO();
	private String permiso="";
	private List<RubroVO> listRubro= new ArrayList<RubroVO>();

	// Buss Flags
	
	
	// View Constants
	private String fechaCeseActividadView="";
	private String fechaFallecimientoView="";
	private String fechaTramiteView="";
	private String fechaCierreDefView;


	// Constructores
	public CierreComercioVO() {
		super();
	
	}
	// Getters y Setters


	public MultaVO getMulta() {
		return multa;
	}

	public void setMulta(MultaVO multa) {
		this.multa = multa;
	}
	
	public CasoVO getCaso() {
		return caso;
	}

	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}


	public CasoContainer getCasoNoEmiMul() {
		return casoNoEmiMul;
	}


	public void setCasoNoEmiMul(CasoContainer casoNoEmiMul) {
		this.casoNoEmiMul = casoNoEmiMul;
	}


	public Date getFechaCeseActividad() {
		return fechaCeseActividad;
	}


	public void setFechaCeseActividad(Date fechaCeseActividad) {
		this.fechaCeseActividad = fechaCeseActividad;
		this.fechaCeseActividadView = DateUtil.formatDate(fechaCeseActividad, DateUtil.ddSMMSYYYY_MASK);
	}


	public Date getFechaTramite() {
		return fechaTramite;
	}


	public void setFechaTramite(Date fechaTramite) {
		this.fechaTramite = fechaTramite;
		this.fechaTramiteView = DateUtil.formatDate(fechaTramite, DateUtil.ddSMMSYYYY_MASK);
	}


	public MotivoCierreVO getMotivoCierre() {
		return motivoCierre;
	}


	public void setMotivoCierre(MotivoCierreVO motivoCierre) {
		this.motivoCierre = motivoCierre;
	}


	public Date getFechaFallecimiento() {
		return fechaFallecimiento;
	}


	public void setFechaFallecimiento(Date fechaFallecimiento) {
		this.fechaFallecimiento = fechaFallecimiento;
		this.fechaFallecimientoView = DateUtil.formatDate(fechaFallecimiento, DateUtil.ddSMMSYYYY_MASK);
	}


	public Date getFechaCierreDef() {
		return fechaCierreDef;
	}


	public void setFechaCierreDef(Date fechaCierreDef) {
		this.fechaCierreDef = fechaCierreDef;
		this.fechaCierreDefView = DateUtil.formatDate(fechaCierreDef, DateUtil.ddSMMSYYYY_MASK);
	}
	
	public String getImprimirEnabled() {
		return (this.getFechaCierreDef()!=null) ? ENABLED : DISABLED;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
	public String getFechaCeseActividadView() {
		return fechaCeseActividadView;
	}

	public void setFechaCeseActividadView(String fechaCeseActividadView) {
		this.fechaCeseActividadView = fechaCeseActividadView;
	}
	

	public String getFechaFallecimientoView() {
		return fechaFallecimientoView;
	}

	public void setFechaFallecimientoView(String fechaFallecimientoView) {
		this.fechaFallecimientoView = fechaFallecimientoView;
	}

	public String getFechaTramiteView() {
		return fechaTramiteView;
	}

	public void setFechaTramiteView(String fechaTramiteView) {
		this.fechaTramiteView = fechaTramiteView;
	}

	public String getFechaCierreDefView() {
		return fechaCierreDefView;
	}

	public void setFechaCierreDefView(String fechaCierreDefView) {
		this.fechaCierreDefView = fechaCierreDefView;
	}

	public CuentaVO getCuentaVO() {
		return cuentaVO;
	}

	public void setCuentaVO(CuentaVO cuentaVO) {
		this.cuentaVO = cuentaVO;
	}


	public String getPermiso() {
		return permiso;
	}


	public void setPermiso(String permiso) {
		this.permiso = permiso;
	}


	public List<RubroVO> getListRubro() {
		return listRubro;
	}


	public void setListRubro(List<RubroVO> listRubro) {
		this.listRubro = listRubro;
	}

	public String getLeyendaCasoView() {
		String leyenda = "";

		if (!StringUtil.isNullOrEmpty(this.getCaso().getSistemaOrigen().getDesSistemaOrigen())) {
			leyenda += this.getCaso().getSistemaOrigen().getDesSistemaOrigen() + "-";
		}
		
		leyenda += this.getCaso().getNumero(); 
		
		return leyenda;
	}

}
