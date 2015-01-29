package hello;

import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Version;
import org.springframework.format.annotation.DateTimeFormat;

@Document
public class Conference {

    @Id
    private String id;

    //used to define the unique identification flag and version of a Document.Spring Data Mongo will fill this field automatically at runtime
    @Version
    private Integer version;

    private String name;

    private String description;

    @DateTimeFormat(style = "M-")
    private Date startedDate;

    @DateTimeFormat(style = "M-")
    private Date endedDate;

    private String slug;

    /*
     Address is annotated with nothing, and it is an embedded object inConference document, in concept, 
     it is very similar with the@Embedable class in JPA, and its lifecycle is fully controlled by
     its dependent Conference document.
     */
    private Address address;

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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the startedDate
     */
    public Date getStartedDate() {
        return startedDate;
    }

    /**
     * @param startedDate the startedDate to set
     */
    public void setStartedDate(Date startedDate) {
        this.startedDate = startedDate;
    }

    /**
     * @return the endedDate
     */
    public Date getEndedDate() {
        return endedDate;
    }

    /**
     * @param endedDate the endedDate to set
     */
    public void setEndedDate(Date endedDate) {
        this.endedDate = endedDate;
    }

    /**
     * @return the slug
     */
    public String getSlug() {
        return slug;
    }

    /**
     * @param slug the slug to set
     */
    public void setSlug(String slug) {
        this.slug = slug;
    }

    /**
     * @return the address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(Address address) {
        this.address = address;
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
