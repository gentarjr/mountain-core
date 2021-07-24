package com.mountain.library.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.hateoas.Link;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RelLink extends Link {

    private RelLink(String href, String rel) {
        super(href, rel);
    }

    public static RelLink getInstance(Link link) {
        RelLink relLink = new RelLink(link.getHref(), link.getRel().value());
        return relLink;
    }

    @Override
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getHreflang() {
        return super.getHreflang();
    }

    @Override
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getDeprecation() {
        return super.getDeprecation();
    }

    @Override
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getMedia() {
        return super.getMedia();
    }

    @Override
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getTitle() {
        return super.getTitle();
    }

    @Override
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getType() {
        return super.getType();
    }
}
