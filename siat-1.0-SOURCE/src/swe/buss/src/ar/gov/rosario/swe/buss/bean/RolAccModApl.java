//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.buss.bean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import ar.gov.rosario.swe.SweCommonError;
import ar.gov.rosario.swe.SweDAOFactory;
import ar.gov.rosario.swe.iface.model.RolAccModAplVO;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Relacion entre Rol y AccionesModulos de una Aplicacion
 * Se implementa como clase por los atributos de auditoria
 * @author tecso
 *
 */

@Entity
@Table(name = "swe_rolaccmodapl")
@SequenceGenerator(
    name="rolaccmodapl_seq",
    sequenceName="swe_rolaccmodapl_id_seq",
    allocationSize = 0
)

public class RolAccModApl extends BaseBO {
	
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(generator="rolaccmodapl_seq",strategy=GenerationType.SEQUENCE)
    private Long id;
 
    public Long getId() {
        return id;
   }
	
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idRolApl")
	private RolApl 			rolApl;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idAccModApl")
	private AccModApl 		accModApl;
	
	public RolAccModApl() {
		super();
	}

	public AccModApl getAccModApl() {
		return accModApl;
	}
	public void setAccModApl(AccModApl accModApl) {
		this.accModApl = accModApl;
	}

	
	public RolApl getRolApl() {
		return rolApl;
	}
	public void setRolApl(RolApl rolApl) {
		this.rolApl = rolApl;
	}

    public static RolAccModApl getById(Long id) {
		return (RolAccModApl) SweDAOFactory.getRolAccModAplDAO().getById(id);
	}
	
	public void loadFromVO(RolAccModAplVO rolAccModAplVO) {
		setAccModApl(AccModApl.getById(rolAccModAplVO.getAccModApl().getId())); 
		setRolApl(RolApl.getById(rolAccModAplVO.getRolApl().getId()));
	}
	
	public boolean validateCreate() {

		//limpiamos la lista de errores
		clearError();

		validate();

		if (hasError()) {
			return false;
		}

		return true;
	}
	
	public boolean validateDelete() {

		//limpiamos la lista de errores
		clearError();

		return true;		
	}
	
	private void validate () {
		
		if (getAccModApl() == null) {
			addRecoverableError(SweCommonError.ROLACCMODAPL_ACCMODAPL_REQUIRED);
		}
	
		if (getRolApl() == null) {
			addRecoverableError(SweCommonError.ROLACCMODAPL_ROLAPL_REQUIRED);
		}
		
	}
	
}
