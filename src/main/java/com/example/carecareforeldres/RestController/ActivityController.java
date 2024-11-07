package com.example.carecareforeldres.RestController;

import com.example.carecareforeldres.Entity.*;
import com.example.carecareforeldres.Repository.OrganisateurRepository;
import com.example.carecareforeldres.Service.IActivityService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/Activity")
public class ActivityController {
    IActivityService iActivityService;
    OrganisateurRepository organisateurRepository;

    private static final String MESSAGE_KEY = "message";
    private static final String MESSAGE_ERROR = "message";


    @GetMapping("/allActivity")
    public List<Activity> getActivity() {
        List<Activity> listActivitys = iActivityService.retrieveAllActivity();
        return listActivitys;
    }

    @GetMapping("/Activity/{id}")
    public Activity retrieveActivity(@PathVariable("id") Long id) {
        return iActivityService.retrieveActivity(id);
    }

    @PostMapping("/addActivity")
    public Activity addEtudiant(@RequestBody Activity b) {
        return iActivityService.addActivity(b);
    }

    @PutMapping("/UpdateActivity")
    public Activity updateActivity(@RequestBody Activity e) {
        return iActivityService.updateActivity(e);
    }

    @DeleteMapping("/RemoveActivity/{id}")
    public void removeActivity(@PathVariable("id") Long id) {
        iActivityService.removeActivity(id);
    }


    @PostMapping("/{idActivity}/{idPatient}")
    public ResponseEntity<?> registerPatientttToEvent(@PathVariable Long idActivity, @PathVariable int idPatient) {
        try {
            iActivityService.registerPatientToActivity(idActivity, idPatient);
            return ResponseEntity.ok("Inscription réussie");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register/{idOrganisateur}")
    @ResponseBody
    public ResponseEntity<?> registerOrganisateurrrToActivity(@RequestBody Activity activity, @PathVariable int idOrganisateur) {
        try {
            iActivityService.registerOrganisateurToActivity(activity, idOrganisateur);
            return ResponseEntity.ok("Inscription réussie");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/react/{activityId}/{patientId}")
    public ResponseEntity<Map<String, String>> reactToActivity(@PathVariable Long activityId,
                                                               @PathVariable int patientId,
                                                               @RequestParam LikeDisliketRate reaction) {
        try {
            iActivityService.reactToActivity(activityId, patientId, reaction);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Réaction enregistrée avec succès.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Une erreur est survenue : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping("/qualityTrend")
    public Map<LocalDate, Map<TypeActivity, Long>> getQualityTrend() {
        return iActivityService.getQualityTrend();
    }

    @GetMapping("/nbrPostlikes/{idActivity}")
    public Integer nbrLike(@PathVariable Long idActivity) {

        return iActivityService.numberOflikesofActivities(idActivity);
    }

    @GetMapping("/nbrPostDislikes/{idActivity}")
    public Integer nbrDisLike(@PathVariable Long idActivity) {

        return iActivityService.numberOfDisikesofActivity(idActivity);
    }

    @PostMapping("/accepter/{idActivite}")
    public ResponseEntity<?> accepterActivite(@PathVariable Long idActivite) {
        try {
            iActivityService.accepterActivite(idActivite);
            return ResponseEntity.ok("Activité acceptée avec succès.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Une erreur est survenue : " + e.getMessage());
        }
    }

    @PostMapping("/refuser/{idActivite}")
    public ResponseEntity<?> refuserActivite(@PathVariable Long idActivite) {
        try {
            iActivityService.refuserActivite(idActivite);
            return ResponseEntity.ok("Activité refusée avec succès.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Une erreur est survenue : " + e.getMessage());
        }
    }

    @GetMapping("/activitiesByEtat/{etat}")
    public List<Activity> getActivitiesByEtat(@PathVariable EtatActivite etat) {
        return iActivityService.getActivitiesByEtat(etat);
    }

    @PostMapping("/favoris/{idPatient}/{idActivity}")
    public ResponseEntity<Object> addActivityToFavoris(@PathVariable Integer idPatient, @PathVariable Long idActivity) {
        Map<String, Object> response = new HashMap<>();
        try {
            iActivityService.addActivityToFavoris(idPatient, idActivity);
            response.put("success", true);
            response.put("message", "Activity ajouté aux favoris avec succès.");
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("error", "L'activité est déjà dans les favoris de l'utilisateur.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Erreur lors de l'ajout de l'activité aux favoris.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @DeleteMapping("/defavoris/{idPatient}/{idActivity}")
    public ResponseEntity<Object> removeActivityFromFavoris(@PathVariable Integer idPatient, @PathVariable Long idActivity) {
        try {
            iActivityService.removeActivityFromFavoris(idPatient, idActivity);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Activity retire des favoris avec succès.");
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/listFavorie/idActivity/{idPatient}")
    public ResponseEntity<List<Activity>> getActivtyFavorisByPatientIddd(@PathVariable Integer idPatient) {
        try {
            List<Activity> activitiesFavoris = iActivityService.getActivtyFavorisByPatientId(idPatient);
            return ResponseEntity.ok(activitiesFavoris);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("ActivityTomorrow")
    public List<Activity> getEventsForTomorrow() {

        return iActivityService.getActivitiesForTomorrow();
    }
    @GetMapping("/ratingsAndBadges")
    public Map<String, Object> assignRatingsAndBadges() {
        return iActivityService.assignRatingsAndBadges();
    }
    @GetMapping("/getOrga/{id}")
    public Organisateur getOrga(@PathVariable Integer id) {
        return organisateurRepository.findById(id).get();
    }


    @GetMapping("/getactOrga/{organisateurId}")
    List<Activity> getActivitiesForOrganisateurrr(@PathVariable Integer organisateurId) {
        return iActivityService.getActivitiesForOrganisateur(organisateurId);
    }

    @GetMapping("/getactPatient/{patientId}")
    List<Activity> getActivitiesForPatient(@PathVariable Integer patientId) {
        return iActivityService.getActivitiesByPatient(patientId);
    }



    }


