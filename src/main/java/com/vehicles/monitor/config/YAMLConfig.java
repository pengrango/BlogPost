package com.vehicles.monitor.config;

import com.vehicles.monitor.model.Company;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class YAMLConfig {
    private String name;
    private int timeout;
    private List<Company> companies;

    public String getName() {
        return name;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

    @Override
    public String toString() {
        return "YAMLConfig{" +
                "name='" + name + '\'' +
                ", timeout='" + timeout + '\'' +
                ", companies=" + companies +
                '}';
    }
}
