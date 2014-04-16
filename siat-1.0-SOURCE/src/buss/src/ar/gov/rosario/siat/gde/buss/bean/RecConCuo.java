//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean que representa las cuotas de un plan asociadas a un recibo de reconfección.
 * S
 * @author tecso
 */
@Entity
@Table(name = "gde_recConCuo")
public class RecConCuo extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idReciboConvenio") 
	private ReciboConvenio reciboConvenio;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idConvenioCuota") 
	private ConvenioCuota  convenioCuota;
	
	@Column(name = "capitalOriginal")
	private Double capitalOriginal;

	@Column(name = "desCapitalOriginal")
	private Double desCapitalOriginal;

	@Column(name = "totCapitalOriginal")
	private Double totCapitalOriginal;

	@Column(name = "interesFinanciero")
	private Double interesFinanciero;

	@Column(name = "desIntFin")
	private Double desIntFin;

	@Column(name = "totIntFin")
	private Double totIntFin;

	@Column(name = "actualizacion")
	private Double actualizacion;

	@Column(name = "desActualizacion")
	private Double desActualizacion;

	@Column(name = "totActualizacion")
	private Double totActualizacion;
	
	@Column(name="importeSellado")
	private Double importeSellado;
	
	@Transient
	private DesGen desGen;
	
	@Transient
	private DesEsp desEsp;	
	

	
	//Constructores 
	public RecConCuo(){
		super();
	}

	// Getters y Setters
	
	public Double getActualizacion() {
		return actualizacion;
	}
	public void setActualizacion(Double actualizacion) {
		this.actualizacion = actualizacion;
	}
	public Double getCapitalOriginal() {
		return capitalOriginal;
	}
	public void setCapitalOriginal(Double capitalOriginal) {
		this.capitalOriginal = capitalOriginal;
	}
	public ConvenioCuota getConvenioCuota() {
		return convenioCuota;
	}
	public void setConvenioCuota(ConvenioCuota convenioCuota) {
		this.convenioCuota = convenioCuota;
	}
	public Double getDesActualizacion() {
		return desActualizacion;
	}
	public void setDesActualizacion(Double desActualizacion) {
		this.desActualizacion = desActualizacion;
	}
	public Double getDesCapitalOriginal() {
		return desCapitalOriginal;
	}
	public void setDesCapitalOriginal(Double desCapitalOriginal) {
		this.desCapitalOriginal = desCapitalOriginal;
	}
	public Double getDesIntFin() {
		return desIntFin;
	}
	public void setDesIntFin(Double desIntFin) {
		this.desIntFin = desIntFin;
	}
	public Double getInteresFinanciero() {
		return interesFinanciero;
	}
	public void setInteresFinanciero(Double interesFinanciero) {
		this.interesFinanciero = interesFinanciero;
	}
	public ReciboConvenio getReciboConvenio() {
		return reciboConvenio;
	}
	public void setReciboConvenio(ReciboConvenio reciboConvenio) {
		this.reciboConvenio = reciboConvenio;
	}
	public Double getTotActualizacion() {
		return totActualizacion;
	}
	public void setTotActualizacion(Double totActualizacion) {
		this.totActualizacion = totActualizacion;
	}
	public Double getTotCapitalOriginal() {
		return totCapitalOriginal;
	}
	public void setTotCapitalOriginal(Double totCapitalOriginal) {
		this.totCapitalOriginal = totCapitalOriginal;
	}
	public Double getTotIntFin() {
		return totIntFin;
	}
	public void setTotIntFin(Double totIntFin) {
		this.totIntFin = totIntFin;
	}

	public DesGen getDesGen() {
		return desGen;
	}

	public void setDesGen(DesGen desGen) {
		this.desGen = desGen;
	}

	public DesEsp getDesEsp() {
		return desEsp;
	}

	public void setDesEsp(DesEsp desEsp) {
		this.desEsp = desEsp;
	}


	public Double getImporteSellado() {
		return importeSellado;
	}

	public void setImporteSellado(Double importeSellado) {
		this.importeSellado = importeSellado;
	}

	// Metodos de Clase
	public static RecConCuo getById(Long id) {
		return (RecConCuo) GdeDAOFactory.getRecConCuoDAO().getById(id);
	}
	
	public static RecConCuo getByIdNull(Long id) {
		return (RecConCuo) GdeDAOFactory.getRecConCuoDAO().getByIdNull(id);
	}
	
	public static RecConCuo getByReciboConvenioYConvenioCuota(ReciboConvenio reciboConvenio, ConvenioCuota convenioCuota) {
		return (RecConCuo) GdeDAOFactory.getRecConCuoDAO().getByReciboConvenioYConvenioCuota(reciboConvenio, convenioCuota);
	}
	
	public static RecConCuo getConDeuCuoByCuotaEnCuoSaldoPaga(ConvenioCuota convenioCuota)throws Exception{
		return GdeDAOFactory.getRecConCuoDAO().getConDeuCuoByCuotaEnCuoSaldoPaga(convenioCuota);
	}

	public static List<RecConCuo> getList() {
		return (ArrayList<RecConCuo>) GdeDAOFactory.getRecConCuoDAO().getList();
	}
	
	public static List<RecConCuo> getListActivos() {			
		return (ArrayList<RecConCuo>) GdeDAOFactory.getRecConCuoDAO().getListActiva();
	}
	
	
	// Metodos de Instancia
	// Validaciones
	/**
	 * 
	 * @return Un DesGen ó DesEsp (Si uno es nulo, el otro no puede serlo, ambas propiedades son mutuamente excluyentes)
	 */
	public Object getDescuento(){
		if(desEsp==null)
			return desGen;
		return desEsp;
	}
	
	/**
	 * Valida la creacion
	 * @author
	 */
	public boolean validateCreate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();
		//Validaciones de Negocio
				
		if (hasError()) {
			return false;
		}
		return !hasError();
	}
	
	/**
	 * Valida la actualizacion
	 * @author
	 */
	public boolean validateUpdate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();

		if (hasError()) {
			return false;
		}
		return !hasError();
	}

	/**
	 * Validaciones comunes de creacion y actualizacion
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();

		//Validaciones de Requeridos	
		
		if (hasError()) {
			return false;
		}

		return !hasError();
	}
	
	
	/**
	 * Valida la eliminacion
	 * @author
	 */
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de VO

		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}
	
}
