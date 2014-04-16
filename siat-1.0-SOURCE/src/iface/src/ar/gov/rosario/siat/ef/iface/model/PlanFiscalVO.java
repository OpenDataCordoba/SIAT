//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;


/**
 * Value Object del PlanFiscal
 * @author tecso
 *
 */
public class PlanFiscalVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "planFiscalVO";
	
	private String desPlanFiscal;

	private Date fechaDesde;

	private Date fechaHasta;

	private EstadoPlanFisVO estadoPlanFis = new EstadoPlanFisVO();

	private String numero;

	private String objetivo;

	private String fundamentos;

	private String propuestas;

	private String metTrab;

	private String necesidades;

	private String resEsp;
	
	// View
	private String fechaDesdeView ="";
	private String fechaHastaView ="";
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public PlanFiscalVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public PlanFiscalVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesPlanFiscal(desc);
	}
	
	// Getters y Setters

	public String getDesPlanFiscal() {
		return desPlanFiscal;
	}

	public void setDesPlanFiscal(String desPlanFiscal) {
		this.desPlanFiscal = desPlanFiscal;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}

	public EstadoPlanFisVO getEstadoPlanFis() {
		return estadoPlanFis;
	}

	public void setEstadoPlanFis(EstadoPlanFisVO estadoPlanFis) {
		this.estadoPlanFis = estadoPlanFis;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getObjetivo() {
		return objetivo;
	}

	public void setObjetivo(String objetivo) {
		this.objetivo = objetivo;
	}

	public String getFundamentos() {
		return fundamentos;
	}

	public void setFundamentos(String fundamentos) {
		this.fundamentos = fundamentos;
	}

	public String getPropuestas() {
		return propuestas;
	}

	public void setPropuestas(String propuestas) {
		this.propuestas = propuestas;
	}

	public String getMetTrab() {
		return metTrab;
	}

	public void setMetTrab(String metTrab) {
		this.metTrab = metTrab;
	}

	public String getNecesidades() {
		return necesidades;
	}

	public void setNecesidades(String necesidades) {
		this.necesidades = necesidades;
	}

	public String getResEsp() {
		return resEsp;
	}

	public void setResEsp(String resEsp) {
		this.resEsp = resEsp;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
	public String getFechaDesdeView() {
		return fechaDesdeView;
	}
	
	public void setFechaDesdeView(String fechaDesdeView) {
		this.fechaDesdeView = fechaDesdeView;
	}
	
	public String getFechaHastaView() {
		return fechaHastaView;
	}
	
	public void setFechaHastaView(String fechaHastaView) {
		this.fechaHastaView = fechaHastaView;
	}
	
}
