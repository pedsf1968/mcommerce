package com.mcommerce.zuulserver.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class LogFilter extends ZuulFilter {

    Logger log = LoggerFactory.getLogger(this.getClass());
    @Override
    public String filterType() {
        // filtre appliqué en pre-traitement sinon post pour post-traitement
        return "pre";
    }

    @Override
    public int filterOrder() {
        // ordre dans l'exécution des filtres parmis tous les filtres
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        // détermine si on doit filtrer la requête
        // ici on filtre tout donc on renvoie true
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        // traitement de filtrage
        // on envoie juste un message dans la console pour vérifier qu'il y a filtrage
        HttpServletRequest req = RequestContext.getCurrentContext().getRequest();

        log.info("************** Requête interceptée : " + req.getRequestURI());
        return null;
    }
}
