package org.terrakube.api.rs.vcs;

import com.yahoo.elide.annotation.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.terrakube.api.plugin.security.audit.GenericAuditFields;
import org.terrakube.api.rs.IdConverter;
import org.terrakube.api.rs.Organization;
import org.hibernate.annotations.Type;

import jakarta.persistence.*;

import java.sql.Types;
import java.util.Date;
import java.util.UUID;

@ReadPermission(expression = "team view vcs")
@CreatePermission(expression = "team manage vcs")
@UpdatePermission(expression = "team manage vcs")
@DeletePermission(expression = "team manage vcs")
@Include(rootLevel = false)
@Getter
@Setter
@Entity(name = "vcs")
public class Vcs extends GenericAuditFields {

    @Id
    @JdbcTypeCode(Types.VARCHAR)
    @Convert(converter = IdConverter.class)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "vcs_type")
    private VcsType vcsType;

    @Column(name = "description")
    private String description;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "callback")
    private String callback;

    @Column(name = "endpoint")
    private String endpoint;

    @Column(name = "api_url")
    private String apiUrl;

    @ReadPermission(expression = "read vcs secret")
    @Column(name = "client_secret")
    private String clientSecret;

    @UpdatePermission(expression = "user is a super service")
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private VcsStatus status = VcsStatus.PENDING;

    @ReadPermission(expression = "read access token")
    @Column(name = "access_token")
    private String accessToken;

    @Exclude
    @Column(name = "refresh_token")
    private String refreshToken;

    @Exclude
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "token_expiration")
    private Date tokenExpiration;

    @Column(name = "redirect_url")
    private String redirectUrl;

    @ManyToOne
    private Organization organization;

}
