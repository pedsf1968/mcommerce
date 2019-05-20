package com.clientui.controller;

        import com.clientui.beans.ProductBean;
        import com.clientui.proxies.MicroserviceProduitsProxy;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Controller;
        import org.springframework.ui.Model;
        import org.springframework.web.bind.annotation.PathVariable;
        import org.springframework.web.bind.annotation.RequestMapping;

        import java.util.List;
        import java.util.Optional;

@Controller
public class ClientController {

    @Autowired
    MicroserviceProduitsProxy mProduitsProxy;

    @RequestMapping("/")
    public String accueil(Model model){

        // on récupère la liste des produits du proxy
        List<ProductBean> produits = mProduitsProxy.listeDesProduits();

        //on ajoute au model l'attribut produits pour l'envoyer à la page Accueil
        model.addAttribute("produits",produits);

        return "Accueil";
    }

    @RequestMapping("/details-produit/{id}")
    public String ficheProduit(@PathVariable int id, Model model){
        // on récupère le produit du proxy
        ProductBean produit = mProduitsProxy.recupererUnProduit(id);

        // on ajoute le produit à model
        model.addAttribute("produit",produit);

        return "FicheProduit";
    }
}
