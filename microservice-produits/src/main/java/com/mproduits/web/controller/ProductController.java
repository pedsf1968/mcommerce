package com.mproduits.web.controller;

        import com.mproduits.configurations.ApplicationPropertiesConfiguration;
        import com.mproduits.dao.ProductDao;
        import com.mproduits.model.Product;
        import com.mproduits.web.exceptions.ProductNotFoundException;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.boot.actuate.health.Health;
        import org.springframework.boot.actuate.health.HealthIndicator;
        import org.springframework.web.bind.annotation.GetMapping;
        import org.springframework.web.bind.annotation.PathVariable;
        import org.springframework.web.bind.annotation.RestController;

        import java.util.List;
        import java.util.Optional;

@RestController
public class ProductController implements HealthIndicator {

    @Autowired
    ProductDao productDao;

    @Autowired
    ApplicationPropertiesConfiguration appProperties;

    Logger log = LoggerFactory.getLogger(this.getClass());

    // Affiche la liste de tous les produits disponibles
    @GetMapping(value = "/Produits")
    public List<Product> listeDesProduits(){
        log.info("ProductController :Récupération de la liste des produits");

        List<Product> products = productDao.findAll();

        if(products.isEmpty()) throw new ProductNotFoundException("Aucun produit n'est disponible à la vente");

        // On limite le nombre de produits suivant le paramètre de configuration
        List<Product> listeLimitee = products.subList(0,appProperties.getLimitDeProduits());

        return listeLimitee;
    }

    //Récuperer un produit par son id
    @GetMapping( value = "/Produits/{id}")
    public Optional<Product> recupererUnProduit(@PathVariable int id) {
        log.info("ProductController : Récupération du produit : " + id);

        Optional<Product> product = productDao.findById(id);

        if(!product.isPresent())  throw new ProductNotFoundException("Le produit correspondant à l'id " + id + " n'existe pas");

        return product;
    }

    @Override
    public Health health() {
        List<Product> products = productDao.findAll();

        if(products.isEmpty()){
            return Health.down().build();
        }

        return Health.up().build();
    }
}

