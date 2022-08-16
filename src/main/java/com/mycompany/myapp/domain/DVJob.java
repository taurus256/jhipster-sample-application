package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.BROWSER;
import com.mycompany.myapp.domain.enumeration.OS;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DVJob.
 */
@Entity
@Table(name = "dv_job")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DVJob implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "create_time")
    private Instant createTime;

    @Column(name = "is_error")
    private Boolean isError;

    @Column(name = "is_success")
    private Boolean isSuccess;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_system")
    private OS operationSystem;

    @Enumerated(EnumType.STRING)
    @Column(name = "browser")
    private BROWSER browser;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private DVTask task;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DVJob id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return this.uuid;
    }

    public DVJob uuid(String uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Instant getCreateTime() {
        return this.createTime;
    }

    public DVJob createTime(Instant createTime) {
        this.setCreateTime(createTime);
        return this;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public Boolean getIsError() {
        return this.isError;
    }

    public DVJob isError(Boolean isError) {
        this.setIsError(isError);
        return this;
    }

    public void setIsError(Boolean isError) {
        this.isError = isError;
    }

    public Boolean getIsSuccess() {
        return this.isSuccess;
    }

    public DVJob isSuccess(Boolean isSuccess) {
        this.setIsSuccess(isSuccess);
        return this;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public OS getOperationSystem() {
        return this.operationSystem;
    }

    public DVJob operationSystem(OS operationSystem) {
        this.setOperationSystem(operationSystem);
        return this;
    }

    public void setOperationSystem(OS operationSystem) {
        this.operationSystem = operationSystem;
    }

    public BROWSER getBrowser() {
        return this.browser;
    }

    public DVJob browser(BROWSER browser) {
        this.setBrowser(browser);
        return this;
    }

    public void setBrowser(BROWSER browser) {
        this.browser = browser;
    }

    public DVTask getTask() {
        return this.task;
    }

    public void setTask(DVTask dVTask) {
        this.task = dVTask;
    }

    public DVJob task(DVTask dVTask) {
        this.setTask(dVTask);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DVJob)) {
            return false;
        }
        return id != null && id.equals(((DVJob) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DVJob{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", createTime='" + getCreateTime() + "'" +
            ", isError='" + getIsError() + "'" +
            ", isSuccess='" + getIsSuccess() + "'" +
            ", operationSystem='" + getOperationSystem() + "'" +
            ", browser='" + getBrowser() + "'" +
            "}";
    }
}
