package com.babydev.app.domain.entity;

public enum Location {
    ALBA_IULIA("Alba Iulia"),
    ARAD("Arad"),
    PITESTI("Pitesti"),
    BACAU("Bacau"),
    ORADEA("Oradea"),
    BISTRITA("Bistrita"),
    BOTOSANI("Botosani"),
    BRASOV("Brasov"),
    BRAILA("Braila"),
    BUZAU("Buzau"),
    RESITA("Resita"),
    CLUJ_NAPOCA("Cluj-Napoca"),
    CONSTANTA("Constanta"),
    SFANTU_GHEORGHE("Sfantu Gheorghe"),
    TARGU_MURES("Targu Mures"),
    CRAIOVA("Craiova"),
    PIATRA_NEAMT("Piatra Neamt"),
    TARGU_JIU("Targu Jiu"),
    DEVA("Deva"),
    SLOBOZIA("Slobozia"),
    SUCEAVA("Suceava"),
    ALEXANDRIA("Alexandria"),
    TIMISOARA("Timisoara"),
    TULCEA("Tulcea"),
    VASLUI("Vaslui"),
    RAMNICU_VALCEA("Ramnicu Valcea"),
    FOCSANI("Focsani"),
    GALATI("Galati"),
    GIURGIU("Giurgiu"),
    MIERCUREA_CIUC("Miercurea Ciuc"),
    SLATINA("Slatina"),
    PLOIESTI("Ploiesti"),
    ZALAU("Zalau"),
    SIBIU("Sibiu"),
    CALARASI("Calarasi"),
    SATU_MARE("Satu Mare"),
    SIGHETU_MARMATIEI("Sighetu Marmatiei"),
    DROBETA_TURNU_SEVERIN("Drobeta-Turnu Severin"),
    RADAUTI("Radauti");

    private String name;

    Location(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }
}

