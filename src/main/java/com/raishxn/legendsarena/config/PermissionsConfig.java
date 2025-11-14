package com.raishxn.legendsarena.config;

import java.util.List;
import java.util.Map;

public class PermissionsConfig {

    private Map<String, String> eloToGroupMapping;
    private List<String> staffBypass;

    public Map<String, String> getEloToGroupMapping() {
        return eloToGroupMapping;
    }

    public void setEloToGroupMapping(Map<String, String> eloToGroupMapping) {
        this.eloToGroupMapping = eloToGroupMapping;
    }

    public List<String> getStaffBypass() {
        return staffBypass;
    }

    public void setStaffBypass(List<String> staffBypass) {
        this.staffBypass = staffBypass;
    }
}