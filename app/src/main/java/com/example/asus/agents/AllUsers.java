package com.example.asus.agents;

public class AllUsers {
    String name;
    long lead;
    long total_lead;


    public AllUsers(){}

    public AllUsers(String name, long lead, long total_lead){
        this.name = name;
        this.lead = lead;
        this.total_lead = total_lead;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLead() {
        return lead;
    }

    public void setLead(long lead) {
        this.lead = lead;
    }

    public long getTotal_lead() {
        return total_lead;
    }

    public void setTotal_lead(long total_lead) {
        this.total_lead = total_lead;
    }
}
