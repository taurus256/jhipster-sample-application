package com.mycompany.myapp.domain;

import com.mycompany.myapp.domain.enumeration.BROWSER;
import com.mycompany.myapp.domain.enumeration.OS;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DVTask.
 */
@Entity
@Table(name = "dv_task")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DVTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "create_time")
    private Instant createTime;

    @Column(name = "retry_counter")
    private Integer retryCounter;

    @Column(name = "error_desc")
    private String errorDesc;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_system")
    private OS operationSystem;

    @Enumerated(EnumType.STRING)
    @Column(name = "browser")
    private BROWSER browser;

    @Column(name = "resolution_width")
    private Integer resolutionWidth;

    @Column(name = "resolution_height")
    private Integer resolutionHeight;

    @ManyToOne
    private DVUser user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DVTask id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return this.uuid;
    }

    public DVTask uuid(String uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Instant getCreateTime() {
        return this.createTime;
    }

    public DVTask createTime(Instant createTime) {
        this.setCreateTime(createTime);
        return this;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public Integer getRetryCounter() {
        return this.retryCounter;
    }

    public DVTask retryCounter(Integer retryCounter) {
        this.setRetryCounter(retryCounter);
        return this;
    }

    public void setRetryCounter(Integer retryCounter) {
        this.retryCounter = retryCounter;
    }

    public String getErrorDesc() {
        return this.errorDesc;
    }

    public DVTask errorDesc(String errorDesc) {
        this.setErrorDesc(errorDesc);
        return this;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public OS getOperationSystem() {
        return this.operationSystem;
    }

    public DVTask operationSystem(OS operationSystem) {
        this.setOperationSystem(operationSystem);
        return this;
    }

    public void setOperationSystem(OS operationSystem) {
        this.operationSystem = operationSystem;
    }

    public BROWSER getBrowser() {
        return this.browser;
    }

    public DVTask browser(BROWSER browser) {
        this.setBrowser(browser);
        return this;
    }

    public void setBrowser(BROWSER browser) {
        this.browser = browser;
    }

    public Integer getResolutionWidth() {
        return this.resolutionWidth;
    }

    public DVTask resolutionWidth(Integer resolutionWidth) {
        this.setResolutionWidth(resolutionWidth);
        return this;
    }

    public void setResolutionWidth(Integer resolutionWidth) {
        this.resolutionWidth = resolutionWidth;
    }

    public Integer getResolutionHeight() {
        return this.resolutionHeight;
    }

    public DVTask resolutionHeight(Integer resolutionHeight) {
        this.setResolutionHeight(resolutionHeight);
        return this;
    }

    public void setResolutionHeight(Integer resolutionHeight) {
        this.resolutionHeight = resolutionHeight;
    }

    public DVUser getUser() {
        return this.user;
    }

    public void setUser(DVUser dVUser) {
        this.user = dVUser;
    }

    public DVTask user(DVUser dVUser) {
        this.setUser(dVUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DVTask)) {
            return false;
        }
        return id != null && id.equals(((DVTask) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DVTask{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", createTime='" + getCreateTime() + "'" +
            ", retryCounter=" + getRetryCounter() +
            ", errorDesc='" + getErrorDesc() + "'" +
            ", operationSystem='" + getOperationSystem() + "'" +
            ", browser='" + getBrowser() + "'" +
            ", resolutionWidth=" + getResolutionWidth() +
            ", resolutionHeight=" + getResolutionHeight() +
            "}";
    }
}
