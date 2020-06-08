package Entities;

public enum BankServices {

    CREDITING("Give new loans"),
    DEPOSITS("Attract deposits"),
    FINANCIAL_SERVICES("Offer financial services");

    String serviceDescription;

    BankServices(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }
}
