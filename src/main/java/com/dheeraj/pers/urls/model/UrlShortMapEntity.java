package com.dheeraj.pers.urls.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "url_short_map")
public class UrlShortMapEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "url", length = 1000)
    private String url;

    @Column(name = "client_ip")
    private String clientIp;

}
