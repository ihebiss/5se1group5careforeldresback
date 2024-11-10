package com.example.carecareforeldres.Service;

import com.example.carecareforeldres.Entity.Rdv;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface IServiceRdv {
    public Rdv addRdv(Rdv rdv);
    public List<Rdv> retrieveAllRdvs();

    public Rdv updateRdv(Rdv rdv);

    public Rdv retrieveRdv(Long idRDV);

    void removeRdv(Long idRDV);
    Rdv AddRdvAndAssign(Rdv rdv,Integer idMedecin, Integer idPatient, LocalDateTime dateRDV);
    void exportRdvToExcel(String fileName,Integer idMedecin) throws IOException;
    public void verifierEtatMedecin();
    public List<Rdv> retrieveAllRdvsByMedecin(Integer idMedecin);
    public void ArchiverRdv();



}
