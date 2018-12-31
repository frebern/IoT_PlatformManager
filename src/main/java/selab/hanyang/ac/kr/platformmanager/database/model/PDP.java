package selab.hanyang.ac.kr.platformmanager.database.model;

import javax.persistence.*;

@Entity
@Table(name = "pdp")
public class PDP {

    @Id
    @Column(name = "pdp_id")
    private int pdpId;

    @Column(name = "name")
    private String name;

    public PDP(){}

    public PDP(String name){
        this.name = name;
    }

    public PDP(int pdpId, String name){
        this.pdpId = pdpId;
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public int getId() {
        return pdpId;
    }
}
