package com.clientui.controller;

import com.clientui.beans.CommandeBean;
import com.clientui.beans.PaiementBean;
import com.clientui.beans.ProductBean;
import com.clientui.proxies.MicroserviceCommandeProxy;
import com.clientui.proxies.MicroservicePaiementProxy;
import com.clientui.proxies.MicroserviceProduitsProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


@Controller
public class ClientController {

    @Autowired
    private MicroserviceProduitsProxy mProduitsProxy;

    @Autowired
    private MicroserviceCommandeProxy mCommandesProxy;

    @Autowired
    private MicroservicePaiementProxy mPaiementProxy;

    Logger log = LoggerFactory.getLogger(this.getClass());

    /*
    * Étape (1)
    * Opération qui récupère la liste des produits et on les affichent dans la page d'accueil.
    * Les produits sont récupérés grâce à ProduitsProxy
    * On fini par rentourner la page Accueil.html à laquelle on passe la liste d'objets "produits" récupérés.
    * */
    @RequestMapping("/")
    public String accueil(Model model){
        log.info("Envoi requête vers microservice-produits");
        List<ProductBean> produits =  mProduitsProxy.listeDesProduits();

        model.addAttribute("produits", produits);

        return "Accueil";
    }

    /*
    * Étape (2)
    * Opération qui récupère les détails d'un produit
    * On passe l'objet "produit" récupéré et qui contient les détails en question à  FicheProduit.html
    * */
    @RequestMapping("/details-produit/{id}")
    public String ficheProduit(@PathVariable int id,  Model model){
        log.info("ClientController : Affichage du détail d'un produit");
        ProductBean produit = mProduitsProxy.recupererUnProduit(id);

        model.addAttribute("produit", produit);

        return "FicheProduit";
    }

    /**
     * Étape (3) et (4)
     * Opération qui fait appel au microservice de commande pour placer une commande et récupérer les détails de la commande créée
     **/
    @RequestMapping("/commander-produit/{idProduit}/{montant}")
    public String passerCommande(@PathVariable int idProduit, @PathVariable Double montant, Model model) {
        log.info("ClientController : Affichage de la page de paiement");
        CommandeBean commande = new CommandeBean();

        //On renseigne les propriétés de l'objet de type CommandeBean que nous avons crée
        commande.setProductId(idProduit);
        commande.setQuantite(1);
        commande.setDateCommande(new Date());
        log.info("ClientController : passerCommande : " + commande);

        //appel du microservice commandes grâce à Feign et on récupère en retour les détails de la commande créée, notamment son ID (étape 4).
        ResponseEntity<CommandeBean> commandeAjoutee = mCommandesProxy.ajouterCommande(commande);

        log.info("ClientController : commandeAjoutée : " + commandeAjoutee.getBody());

        //on passe à la vue l'objet commande et le montant de celle-ci afin d'avoir les informations nécessaire pour le paiement
        model.addAttribute("commande", commandeAjoutee.getBody());
        model.addAttribute("montant", montant);

        return "Paiement";
    }

    /**
     * Étape (5)
     * Opération qui fait appel au microservice de paiement pour traiter un paiement
     **/
    @RequestMapping("/payer-commande/{idCommande}/{montant}")
    public String payerCommande(@PathVariable int idCommande, @PathVariable Double montant, Model model){
        log.info("ClientController : Affichage de la page de confirmation");

        PaiementBean paiementAExecuter = new PaiementBean();

        paiementAExecuter.setIdCommande(idCommande);
        paiementAExecuter.setMontant(montant);
        paiementAExecuter.setNumeroCarte(numcarte());

        ResponseEntity<PaiementBean> paiement = mPaiementProxy.payerUneCommande(paiementAExecuter);

        Boolean paiementAccepte = false;
        //si le code est autre que 201 CREATED, c'est que le paiement n'a pas pu aboutir.
        if(paiement.getStatusCode() == HttpStatus.CREATED)
            paiementAccepte = true;

        model.addAttribute("paiementOK", paiementAccepte);

        return "Confirmation";
    }

    //Génére une serie de 16 chiffres au hasard pour simuler vaguement une CB
    private Long numcarte() {
        return ThreadLocalRandom.current().nextLong(1000000000000000L, 9000000000000000L);
    }
}
