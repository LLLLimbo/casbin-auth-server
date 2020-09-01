package cn.limbo.auth.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "casbin_model", schema = "casbin", catalog = "casbin")
public class CasbinModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;

    @Column(name = "id")
    private String id;

    @Column(name = "model_def")
    private String modelDef;

    @Column(name = "model_status")
    private String modelStatus;

    @Column(name = "create_time")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @CreatedDate
    private Date createTime;

    @Column(name = "modify_time")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @LastModifiedDate
    private Date modifyTime;
}
