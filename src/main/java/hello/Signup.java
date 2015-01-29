package hello;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Version;

@Document
public class Signup {

    @Id
    private String id;

    //used to define the unique identification flag and version of a Document.Spring Data Mongo will fill this field automatically at runtime
    @Version
    private Integer version;

    //There is no JPA @OneToMany like annotations to define the relations between documents for MongoDB.
    //used to declare it is a reference of theConference document
    @DBRef
    private Conference conference;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the conference
     */
    public Conference getConference() {
        return conference;
    }

    /**
     * @param conference the conference to set
     */
    public void setConference(Conference conference) {
        this.conference = conference;
    }

    /**
     * @return the version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

}
