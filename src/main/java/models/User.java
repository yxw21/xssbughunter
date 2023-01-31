package models;

public class User {
    private String username;
    private String email;
    private String domain;
    private String notice;

    private String custom_domain;
    private String external_js_endpoint;
    private String external_js_content;
    private String page_list;
    private boolean auto_crawl_site;

    User() {
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getCustom_domain() {
        return custom_domain;
    }

    public void setCustom_domain(String custom_domain) {
        this.custom_domain = custom_domain;
    }

    public String getExternal_js_endpoint() {
        return external_js_endpoint;
    }

    public void setExternal_js_endpoint(String external_js_endpoint) {
        this.external_js_endpoint = external_js_endpoint;
    }

    public String getExternal_js_content() {
        return external_js_content;
    }

    public void setExternal_js_content(String external_js_content) {
        this.external_js_content = external_js_content;
    }

    public String getPage_list() {
        return page_list;
    }

    public void setPage_list(String page_list) {
        this.page_list = page_list;
    }

    public boolean isAuto_crawl_site() {
        return auto_crawl_site;
    }

    public void setAuto_crawl_site(boolean auto_crawl_site) {
        this.auto_crawl_site = auto_crawl_site;
    }
}
