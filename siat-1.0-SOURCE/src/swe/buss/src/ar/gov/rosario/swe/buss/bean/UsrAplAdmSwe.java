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

import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Aplicaciones que un usuario SWE puede administrar
 * 
 * @author tecso
 * 
 */

@Entity
@Table(name = "swe_usrapladmswe")
@SequenceGenerator(
    name="usrapladmswe_seq",
    sequenceName="swe_usrapladmswe_id_seq",
    allocationSize = 0
)

public class UsrAplAdmSwe extends BaseBO {
	
	@Id @GeneratedValue(generator="usrapladmswe_seq",strategy=GenerationType.SEQUENCE)
    private Long id; 
    public Long getId() {
        return id;
    }	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idAplicacion")
	private Aplicacion aplicacion;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idusrapl")
	private UsrApl usrApl;
		
	public UsrAplAdmSwe(Aplicacion aplicacion, UsrApl apl) {
		super();
		this.aplicacion = aplicacion;
		usrApl = apl;
	}

	public UsrAplAdmSwe() {
		super();
	}

	public Aplicacion getAplicacion() {
		return aplicacion;
	}
	public void setAplicacion(Aplicacion aplicacion) {
		this.aplicacion = aplicacion;
	}

	public UsrApl getUsrApl() {
		return usrApl;
	}

	public void setUsrApl(UsrApl usrApl) {
		this.usrApl = usrApl;
	}
	
	/*public static List<UsrAplAdmSwe> getByIdsAplicacion(Long[] ids) throws Exception{
		return SweDAOFactory.getUsrAplAdmiSweDAO().getByIdsAplicacion(ids);
	}*/
}
