package dev.michaeldubose.productcompositeservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api.common")
public class SwaggerCommonConfig {
  private String version;
  private String title;
  private String description;
  private String termsOfServiceUrl;
  private String license;
  private String licenseUrl;
  private Contact contact;

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getTermsOfServiceUrl() {
    return termsOfServiceUrl;
  }

  public void setTermsOfServiceUrl(String termsOfServiceUrl) {
    this.termsOfServiceUrl = termsOfServiceUrl;
  }

  public String getLicense() {
    return license;
  }

  public void setLicense(String license) {
    this.license = license;
  }

  public String getLicenseUrl() {
    return licenseUrl;
  }

  public void setLicenseUrl(String licenseUrl) {
    this.licenseUrl = licenseUrl;
  }

  public Contact getContact() {
    return contact;
  }

  public void setContact(Contact contact) {
    this.contact = contact;
  }

  private static class Contact {
    private String name;
    private String url;
    private String email;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }
  }
}
