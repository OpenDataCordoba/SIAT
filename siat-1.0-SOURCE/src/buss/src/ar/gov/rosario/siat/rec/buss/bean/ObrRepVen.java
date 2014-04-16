//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.buss.bean.GdeGDeudaManager;
import ar.gov.rosario.siat.rec.buss.dao.RecDAOFactory;
import ar.gov.rosario.siat.rec.iface.util.RecError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;


/**
 * Bean correspondiente a ObrRepVen: Reprogramacion de Vencimientos de la Obra
 * 
 * @author tecso
 */
@Entity
@Table(name = "cdm_obrRepVen")
public class ObrRepVen extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne()
	@JoinColumn(name="idObra")
	private Obra obra;
	
	@Column(name = "cuotaDesde")
	private Integer cuotaDesde;
	
	@Column(name = "nueFecVen")
	private Date nueFecVen;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	@Column(name = "canDeuAct")
	private Integer canDeuAct;
	
    @Column(name="idCaso") 
	private String idCaso;	

	// Constructores
	public ObrRepVen(){
		super();
	}
	
	public ObrRepVen(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static ObrRepVen getById(Long id) {
		return (ObrRepVen) RecDAOFactory.getObrRepVenDAO().getById(id);
	}
	
	public static ObrRepVen getByIdNull(Long id) {
		return (ObrRepVen) RecDAOFactory.getObrRepVenDAO().getByIdNull(id);
	}
	
	public static List<ObrRepVen> getList() {
		return (ArrayList<ObrRepVen>) RecDAOFactory.getObrRepVenDAO().getList();
	}
	
	public static List<ObrRepVen> getListActivos() {			
		return (ArrayList<ObrRepVen>) RecDAOFactory.getObrRepVenDAO().getListActiva();
	}
	
	// Getters y setters
	public Obra getObra() {
		return obra;
	}

	public void setObra(Obra obra) {
		this.obra = obra;
	}

	public Integer getCuotaDesde() {
		return cuotaDesde;
	}

	public void setCuotaDesde(Integer cuotaDesde) {
		this.cuotaDesde = cuotaDesde;
	}

	public Date getNueFecVen() {
		return nueFecVen;
	}

	public void setNueFecVen(Date nueFecVen) {
		this.nueFecVen = nueFecVen;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getCanDeuAct() {
		return canDeuAct;
	}

	public void setCanDeuAct(Integer canDeuAct) {
		this.canDeuAct = canDeuAct;
	}

	public String getIdCaso() {
		return idCaso;
	}
	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}

	// Validaciones 
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;
	}

	private boolean validate() throws Exception {
		
		//	Validaciones        
		if (cuotaDesde == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.OBRREPVEN_CUOTADESDE);
		}
		
		if (cuotaDesde != null && !( cuotaDesde > 0) ) {
			addRecoverableError(BaseError.MSG_VALORMENORIGUALQUECERO, RecError.OBRREPVEN_CUOTADESDE);
		}
		if (nueFecVen == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.OBRREPVEN_NUEFECVEN);
		}
		if (idCaso == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.OBRREPVEN_CASO);
		}
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	/**
	 * Reprograma los vencimientos de la deuda asociados a una obra
	 * */
	public void reprogramarVencimientos() throws Exception { 
		
		int canDeuAct = 0;
		if (this.getObra() != null) {
			//Para cada uno de los detalles de las Planillas de la Obra
			for (PlaCuaDet plaCuaDet: this.getObra().getListPlaCuaDetNoCarpetas()) {
				
				// Obtenemos la deuda administrativa con perido >= cuotaDesde 
				List<DeudaAdmin> listDeuda = plaCuaDet.getCuentaCdM().getListDeudaAdminByRangoPeriodoAnio
					(this.getCuotaDesde(), null, null, null);
				
				int i = 0;
				// Para Cada una de las deuda
				for (DeudaAdmin deuda:listDeuda) {
					// Reprogramamos la fecha de vencimiento
					deuda.setFechaVencimiento( DateUtil.addMonthsToDate(this.getNueFecVen(),i++));
				
					//actualizamos en la BD
					GdeGDeudaManager.getInstance().updateDeudaAdmin(deuda);
				}
				
				canDeuAct += i;
			}
		}
		// Actualizamos el contador de deudas reprogramadas
		this.setCanDeuAct(canDeuAct);
	}

	@Override
	public String infoString() {
		String ret ="Reprogramacion de Vencimientos de la Obra";

		if(obra!=null){
			ret+=" - Obra: "+obra.getDesObra();
		}

		if(cuotaDesde!=null){
			ret+=" - cuota Desde: "+cuotaDesde;
		}

		if(nueFecVen!=null){
			ret+=" - Nueva fecha Vto.: "+ DateUtil.formatDate(nueFecVen, DateUtil.ddSMMSYYYY_MASK);
		}

		if(descripcion!=null){
			ret+=" - descripcion: "+descripcion;
		}
		
		if(idCaso!=null){
			ret+=" - Caso: "+idCaso;
		}

		return ret;
	}
}
