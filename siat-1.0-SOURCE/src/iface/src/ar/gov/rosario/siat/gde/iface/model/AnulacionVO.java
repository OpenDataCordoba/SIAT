//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del Anulacion
 * @author tecso
 *
 */
public class AnulacionVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "anulacionVO";
	
	private Date fechaAnulacion;
    private MotAnuDeuVO motAnuDeu = new MotAnuDeuVO();
	private Long idDeuda;
	private CasoVO caso = new CasoVO();
	private String observacion;
	
	private String fechaAnulacionView = "";
	
	private String listIdDeuda4Caso;
	
	// Constructores
	public AnulacionVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public AnulacionVO(int id, String desc) {
		super();
		setId(new Long(id));
		setObservacion(desc);
	}


	
	// Getters y Setters
	public CasoVO getCaso() {
		return caso;
	}
	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}

	public Date getFechaAnulacion() {
		return fechaAnulacion;
	}
	public void setFechaAnulacion(Date fechaAnulacion) {
		this.fechaAnulacion = fechaAnulacion;
		this.fechaAnulacionView = DateUtil.formatDate(fechaAnulacion, DateUtil.ddSMMSYYYY_MASK);
	}

	public Long getIdDeuda() {
		return idDeuda;
	}
	public void setIdDeuda(Long idDeuda) {
		this.idDeuda = idDeuda;
	}

	public MotAnuDeuVO getMotAnuDeu() {
		return motAnuDeu;
	}
	public void setMotAnuDeu(MotAnuDeuVO motAnuDeu) {
		this.motAnuDeu = motAnuDeu;
	}

	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}	
	
	
	public String getListIdDeuda4Caso() {
		return listIdDeuda4Caso;
	}
	public void setListIdDeuda4Caso(String listIdDeuda4Caso) {
		this.listIdDeuda4Caso = listIdDeuda4Caso;
	}

	// View getters
	public void setFechaAnulacionView(String fechaAnulacionView) {
		this.fechaAnulacionView = fechaAnulacionView;
	}
	public String getFechaAnulacionView() {
		return fechaAnulacionView;
	}

	@Override
	public String infoString() {
		String ret ="Anulacion";
		
		if(fechaAnulacion!=null){
			ret+=" - Fecha anulacion: "+DateUtil.formatDate(fechaAnulacion, DateUtil.ddSMMSYYYY_MASK);
		}
		
		if(motAnuDeu!=null){
			ret+=" - Motivo: "+motAnuDeu.getDesMotAnuDeu();
		}

		if(!StringUtil.isNullOrEmpty(listIdDeuda4Caso)){
			ret+= " - periodos de deuda: " + listIdDeuda4Caso;
		}
		
		ret +=" - Observacion: "+observacion;
		
		return ret;
	}
}
